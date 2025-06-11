package com.ubcn.psam.scheduler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ubcn.psam.codec.PSAMServMessageCodec;
import com.ubcn.psam.common.PSAMConstants;
import com.ubcn.psam.common.packet.PSAMRequest;
import com.ubcn.psam.common.packet.PSAMResponse;
import com.ubcn.psam.common.packet.PSAMTransData;
import com.ubcn.psam.common.util.StringUtils;
import com.ubcn.psam.model.PSamServer;
import com.ubcn.psam.service.DBService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@PropertySource("file:config/psam.properties")
public class psamStateSchedule {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.netty.threads.worker:0}")
	private int workerCount;
	
	@Value("${spring.netty.serverId}")
	private String serverId;
	
	@Value("${spring.PSAM.gw.server}")
	private String psamGWServNum;
	
	@Value("${spring.PSAM.state.terminalid}")
	private String stateTerminalid;
	
	private NioEventLoopGroup workerGroup;
	
	public void jobStart() {
		 workerGroup = new NioEventLoopGroup(workerCount);
		
		 log.info("PAM 서버 체크 시작-----------");
		 List<PSamServer> pList = dbService.selectPSAMservList();
		 PSamServer pSamInfo=null;
		 
		 int i =0;
		 for(i=0;i<pList.size();i++) {
		 //for(i=0;i<1;i++) {
			 pSamInfo = pList.get(i);
			 
			 log.info("포트:{}/{}",pSamInfo.getSERV_NUM(),pSamInfo.getADM_PORT());
			 
			 checkServer(pSamInfo.getSERV_NUM(), pSamInfo.getSERV_IP(), pSamInfo.getSERV_PORT());
				 
			 try {
				Thread.sleep(5000);
			 }catch (InterruptedException e) {
			 	// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(StringUtils.getPrintStackTrace(e));
			 }
		 }//
		 log.info("PAM 서버 체크 마침-----------");
	}
	
	private void checkServer(int serverNo, String serverIP, int serverPort) {
		
	    int rtnCode = 0;
		PSAMRequest pSAMrequest = new PSAMRequest();
		Charset encodingCharset = Charset.forName("EUC-KR");
		
		pSAMrequest.setTermId(stateTerminalid);
		pSAMrequest.setTransType("21");
		pSAMrequest.setMessageType("03");
		pSAMrequest.setInputType("S");
		pSAMrequest.setTermTransNo(String.format("%5s", " "));
		pSAMrequest.setMessageVersion("T1");
		pSAMrequest.setCryptoFlag("0");
		pSAMrequest.setRfu(String.format("%10s", " "));
		pSAMrequest.setSamServNum(String.format("%05d", serverNo));
		
		String uniqueno = dbService.GenerateUniqueNo(psamGWServNum);		
		PSAMTransData pSAMTranData = new PSAMTransData(pSAMrequest, uniqueno, serverId);
		
		pSAMTranData.setPSamServNum(Integer.toString(serverNo));  //PSAM  서버 번호
		
		pSAMTranData.setTermTransNo(pSAMrequest.getTermTransNo().trim());
		pSAMTranData.setReplyCode(PSAMConstants.rtn_code_totalerror);
		rtnCode = dbService.insert_PSAM_TRANINFO(pSAMTranData);
		
		log.info("PSAM 서버 접속");

		try {
			 new Bootstrap()
					.group(workerGroup)
					.channel(NioSocketChannel.class)
					.handler(
							new ChannelInitializer<SocketChannel>() {
								private boolean hasResponse = false;
								@Override
								protected void initChannel(SocketChannel ch) throws Exception {
									// TODO Auto-generated method stub
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast("codec",
											new PSAMServMessageCodec(serverId, encodingCharset))
											.addLast("readTimeoutHandler", new ReadTimeoutHandler(PSAMConstants.ClientTimoutSecond))
											.addLast(
													//new ChannelInboundHandlerAdapter() {
													new ChannelHandler[] {	
														new LoggingHandler(LogLevel.INFO), new ChannelDuplexHandler() {
														
														@Override	// SAM서버 관리포트로 요청전문 송신
														public void channelActive(ChannelHandlerContext ctx) throws Exception {
															if (log.isTraceEnabled()) {
																log.debug((new StringBuilder()).append("[")
																		.append(stateTerminalid)
																		.append("][PSAM 핸들러] PSAM  호스트 접속").toString());
															}//
															if (log.isDebugEnabled()) {
																log.debug(
																		(new StringBuilder()).append("[")
																				.append(stateTerminalid)
																				.append("][PSAM 핸들러]] ").append("TcpRequest")
																				.append(" = [")
																				.append(new String(pSAMrequest.toMessage(),
																						encodingCharset))
																				.append("]").toString());
															}//
															
															ctx.writeAndFlush(pSAMrequest);	// request로 요청전문인 out 생성 및 전문송신
														}

														@Override   // SAM서버 관리포트에서 응답전문 수신
														public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
															// 사전수행, TcpMessageCodec.decode(ctx, in, out); -> deserialized()
															// PSAM메시지 생성하여 다음 핸들러로 전달

															//PSAM 서버 정보 저장
															
															log.info("응답-----------{}",msg.toString());
															
															processTcpResponse(pSAMTranData, (PSAMResponse) msg);
															hasResponse = true; 
															ctx.channel().close();
															ReferenceCountUtil.release(msg);
														}
														
														public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
															ctx.close();
														}

														@Override
														public void channelInactive(ChannelHandlerContext ctx) throws Exception {
															
															if (log.isDebugEnabled()) {
																log.debug((new StringBuilder()).append("[")
																		.append(stateTerminalid)
																		.append("][PSAM 핸들러]] CREDITCARD 호스트 접속 종료")
																		.toString());
															}//
															if(hasResponse==false) {
																processTcpResponse(pSAMTranData, null);
															}//
														}

														@Override
														public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
															
															if (log.isErrorEnabled()) {
																log.error((new StringBuilder()).append("[")
																		.append(stateTerminalid)
																		.append("][PSAM 핸들러]] <오류> 호스트 세센 예외발생 - ")
																		.append(cause.getMessage()).toString());
															}//
															
															try {
																processTcpResponse(pSAMTranData, null);
																hasResponse = true;
															}catch(Exception ex) {													
																if (log.isErrorEnabled()) {																
																	log.error((new StringBuilder()).append("[")
																			.append(stateTerminalid)
																			.append("][PSAM 핸들러]]   <오류 >  실패응답 예외발생1  ")
																			.append(cause.getMessage()).toString());
																
																}//													
															}finally {
																ctx.close();
															}							
														  }
														}
													}
													
											);
									}
								}
								
							
					).option(ChannelOption.TCP_NODELAY, true)		//0703 추가
					.option(ChannelOption.SO_REUSEADDR, false)		//0703 추가
					.option(ChannelOption.SO_KEEPALIVE, false)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) //CONNECT_TIMEOUT_MILLIS 5초
					.connect(serverIP, serverPort)
					.addListener(new ChannelFutureListener() {
 								@Override
								public void operationComplete(ChannelFuture future) throws Exception {
									if (future.isSuccess()) {
										log.info("접속 성공");
										return;
									}
									// TODO: 접속 오류 처리
									log.info("Call checkServer_connect_addListener_operationComplete() : 실패");
								}
							}
							).sync();
		}catch(InterruptedException ex) {			   
			log.error(StringUtils.getPrintStackTrace(ex));
		}
	}
	
	private void processTcpResponse(PSAMTransData pSAMTranData, PSAMResponse pSAMResponse)  {
		int rtnCode = 0;
		try {
			if(pSAMResponse== null) {
				pSAMTranData.setSamNum("0");
				pSAMTranData.setTotalSam("0");
				pSAMTranData.setBusySam("0");  //이건 없는데..
				pSAMTranData.setIdleSam("0");
				pSAMTranData.setBadSam("0");
				pSAMTranData.setReplyCode("7005");
				pSAMTranData.setReplyMessage("SAM 서버응답 오류");
			}else {
				
				pSAMTranData.setSamNum("0");
				pSAMTranData.setTotalSam(pSAMResponse.getTotalSam());
				pSAMTranData.setBusySam("0");  
				pSAMTranData.setIdleSam(pSAMResponse.getIdleSam());
				pSAMTranData.setBadSam(pSAMResponse.getBadSam());
				pSAMTranData.setReplyCode(pSAMResponse.getReplyCode());
				pSAMTranData.setReplyMessage(pSAMResponse.getReplyMessage());
			
			}
			
			rtnCode = dbService.PSAM_SERVER_UPDATE(pSAMTranData);
			
			//PSAM 서버 정보 업데이트 
			
			
			if(rtnCode==0) {
				log.error("PSAM 상태 정보 저장 오류");
			}
		}catch(Exception ex) {
			log.error(StringUtils.getPrintStackTrace(ex));
		}
		
	}
}
