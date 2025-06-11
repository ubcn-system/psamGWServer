package com.ubcn.psam.handler;

import java.io.IOException;

import com.ubcn.psam.codec.PSAMServMessageCodec;
import com.ubcn.psam.common.PSAMConstants;
import com.ubcn.psam.common.packet.PSAMRequest;
import com.ubcn.psam.common.packet.PSAMResponse;
import com.ubcn.psam.common.packet.PSAMTransData;
import com.ubcn.psam.common.util.StringUtils;
import com.ubcn.psam.model.PSamServer;
import com.ubcn.psam.service.DBService;
import com.ubcn.psam.service.PropService;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class psamAuthHandler extends psamHandlerAdapter {

	private DBService dbService;
	private PropService propService;
	
	public psamAuthHandler(DBService dbService,PropService propService) {
		this.dbService = dbService;
		this.propService = propService;
	}
	
	private PSAMRequest request;
	private PSAMResponse response;
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		if(!(msg instanceof PSAMRequest)) {
			super.channelRead(ctx, msg);
			return;
		}//
		
		request = (PSAMRequest) msg;
		int retCode = 0;
		Channel clientChannel = ctx.channel(); 
		String psamGWServNum = propService.getPsamGWServNum();
		String uniqueno = dbService.GenerateUniqueNo(psamGWServNum); 
		
		PSAMTransData  transData = new PSAMTransData(request,uniqueno,propService.getServerId());
		
		if(clientChannel.attr(PSAMTrans).get()!=null) {
			
			PSAMTransData prePSAMTransData = (PSAMTransData)clientChannel.attr(PSAMTrans).get();
			
			if( (prePSAMTransData != null) && (  prePSAMTransData.getTermId().equals(request.getTermId())  
					&& prePSAMTransData.getTermTransNo().equals(request.getTermTransNo()) 
					&& prePSAMTransData.getTransType().equals(request.getTransType())
					&& prePSAMTransData.getMessageType().equals(request.getMessageType())
					&& prePSAMTransData.getCsn().equals(request.getCsn())
					) ) {
						
				transData.setReplyCode("7001");
				transData.setReplyMessage("요청 데이타 중복");
				response = new PSAMResponse(transData);		
				ctx.writeAndFlush(response);
				
				if (log.isWarnEnabled()) {
					log.warn("[" + prePSAMTransData.getTermId()
							+ "][PSAM 인증 핸들러] <주의> 후속 이중전문 제거, 거래(요청) = " + request.getAttributes().toString()
							+ ", 거래(진행) = " + prePSAMTransData.getAttributes().toString());
				}//
				return;
			}
		}
		clientChannel.attr(PSAMTrans).set(transData);  //수신 데이타 저장
		
		int serverNum = 0;
		if("01".equals(request.getMessageType()) || "02".equals(request.getMessageType()) ) {  //01 : 인증코드키 요청, 02 :  카드번호키 요청
			//SAM 서버 찾기 
			serverNum = this.rtnServerNum(propService.getServerNum(), propService.getPsamServNum())+1;  // 0~ propService.getPsamNum() 까지의 숫자 랜덤
		}else {
			serverNum =  Integer.parseInt(request.getSamServNum());  //요청 서버 번호
			transData.setSamNum(propService.getPsamNum());  // Default PSAM 번호
		}
		
		PSamServer psamServ = dbService.select_PSAM_server(serverNum);
		
		log.info("serverNum:{}:{}:{}",serverNum,psamServ.getSERV_IP(),psamServ.getBAD_SAM());
		
		transData.setTotalSam(Integer.toString(psamServ.getTOTL_SAM()));
		transData.setIdleSam(Integer.toString(psamServ.getIDLE_SAM()));
		transData.setBusySam(Integer.toString(psamServ.getBUSY_SAM()));
		transData.setBadSam(Integer.toString(psamServ.getBAD_SAM())); 
				
		
		
		//1. 요청 정보 저장 
		transData.setReplyCode("9999");
		retCode = dbService.insert_PSAM_TRANINFO(transData);
//		retCode=1;  //테스트 코드
		if(retCode!=1) {
			transData.setReplyCode("7001");
			transData.setReplyMessage("요청 데이타 저장오류");
			response = new PSAMResponse(transData);		
			ctx.writeAndFlush(response);
			
			return;
		}
		
//		transData.setReplyCode("0000");  //테스트 코드
//		transData.setReplyMessage("정상응답");
//		response = new PSAMResponse(transData);		
//		ctx.writeAndFlush(response);
		
		sendTcpRequest(clientChannel, transData,psamServ.getSERV_IP(),psamServ.getSERV_PORT());
		
		return;		
	}//
	
	/**
	 *  1 ~ 5번 까지 서버가 있다면 
	 * 매번 호출시 마다 다흔 서버를 갖고와야 한다.
	 * 이전 서버 번호와 다른 번호를 랜덥으로 선택 해야 한다.
	 * @param preSerevrNum
	 * @return
	 */
	private int rtnServerNum(String preSerevrNum, String psamServNum) {
		int i =0;
		int serverNum = Integer.valueOf(psamServNum);		
		int rtnServerNum= StringUtils.RandNum(serverNum);
		log.info("이전서버번호:{}",preSerevrNum);
		if(preSerevrNum==null || "".equals(preSerevrNum)) {			
			propService.setServerNum(String.valueOf(rtnServerNum));			
		}else {
			if(preSerevrNum.equals(String.valueOf(rtnServerNum))) {
				for(i=0;i<5;i++) {
					rtnServerNum= StringUtils.RandNum(serverNum);
					log.info("서버번호 다시 찾기:{}",rtnServerNum);
					if(!preSerevrNum.equals(String.valueOf(rtnServerNum))) {
						break;
					}//
				}//
			}//
			propService.setServerNum(String.valueOf(rtnServerNum));
		}//
		
		log.info("서버번호:{}",rtnServerNum);
		return rtnServerNum;		
	}
	
	/**
	 * 
	 * @param clientChannel
	 * @param transData
	 * @param pSAMServerIP
	 * @param pSAMServerPort
	 * @throws Exception
	 */
	protected void sendTcpRequest(Channel clientChannel, PSAMTransData transData,String pSAMServerIP,int pSAMServerPort) throws Exception {
		
		PSAMRequest pSAMrequest = new PSAMRequest(transData); 
		
		new Bootstrap().group(clientChannel.eventLoop()).channel(clientChannel.getClass())
			.handler(new ChannelInitializer<SocketChannel>() {
				private boolean hasResponse = false;
				
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("codec",
								new PSAMServMessageCodec(transData.getServerId(), getEncodingCharset()))
								.addLast("readTimeoutHandler", new ReadTimeoutHandler(PSAMConstants.ClientTimoutSecond))
								.addLast(new ChannelHandler[] {
										
										new LoggingHandler(LogLevel.INFO), new ChannelDuplexHandler() {
											
											public void channelActive(ChannelHandlerContext ctx) {
												
												if (log.isTraceEnabled()) {
													log.debug((new StringBuilder()).append("[")
															.append(transData.getTermId())
															.append("][PSAM 핸들러] PSAM  호스트 접속").toString());
												}//
												if (log.isDebugEnabled()) {
													log.debug(
															(new StringBuilder()).append("[")
																	.append(transData.getTermId())
																	.append("][PSAM 핸들러]] ").append("TcpRequest")
																	.append(" = [")
																	.append(new String(pSAMrequest.toMessage(),
																			getEncodingCharset()))
																	.append("]").toString());
												}//
												ctx.writeAndFlush(pSAMrequest);
											}
											
											public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception  {
												if (log.isDebugEnabled()) {
													log.debug((new StringBuilder()).append("[")
															.append(transData.getTermId())
															.append("][PSAM 핸들러]] ").append("TcpResponse")
															.append(" = [").append(msg.toString()).append("]")
															.toString());
												}//
												
												log.info("응답:{}",msg.toString());
												
												processTcpResponse(clientChannel, (PSAMResponse) msg, transData);
												hasResponse = true;
												ctx.channel().close();
												ReferenceCountUtil.release(msg);
											}
											
											public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
												ctx.close();
											}
											
											public void channelInactive(ChannelHandlerContext ctx) throws Exception {
												if (log.isDebugEnabled()) {
													log.debug((new StringBuilder()).append("[")
															.append(transData.getTermId())
															.append("][PSAM 핸들러]] CREDITCARD 호스트 접속 종료")
															.toString());
												}//
												if(hasResponse==false) {
													processTcpResponse(clientChannel, null, transData);
												}//
												
											}
											
											public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
												
												if (log.isErrorEnabled()) {
													log.error((new StringBuilder()).append("[")
															.append(transData.getTermId())
															.append("][PSAM 핸들러]] <오류> 호스트 세센 예외발생 - ")
															.append(cause.getMessage()).toString());
												}//
												
												try {
													processTcpResponse(clientChannel, null, transData);
													hasResponse = true;
												}catch(Exception ex) {													
													if (log.isErrorEnabled()) {
														log.error((new StringBuilder()).append("[")
																.append(transData.getTermId())
																.append("][PSAM 핸들러]]   <오류 >  실패응답 예외발생1  ")
																.append(cause.getMessage()).toString());
													
													}//													
												}finally {
													ctx.close();
												}												
											}
											
										}//class
								});
				}
			}).option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_REUSEADDR, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.connect(pSAMServerIP, pSAMServerPort)
			.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()) {
						return;
					}//
					if (log.isErrorEnabled()) {
						log.error((new StringBuilder()).append("[").append(transData.getTermId())
								.append("][PSAMAUTH 핸들러]] <오류> PSAM 호스트 접속 실패!!!").toString());
					}//
					transData.setReplyCode(null);
					processTcpResponse(clientChannel, null, transData);
				}
			});
		
	}//
	
	protected void processTcpResponse(Channel clientChannel, PSAMResponse pSAMResponse, PSAMTransData transData) 
			throws Exception {
		int rtnCode = 0;
		
		 PSAMResponse rtnPSAMResponse = null;
		
		// 응답 처리
		if ((pSAMResponse == null) && (transData.getReplyCode() == null)) {
			transData.setReplyCode("7005");
			transData.setReplyMessage("잠시 후 재시도 요망");
		}else {
			log.info("단말기응답:{}",pSAMResponse.toString());
			
			transData.setReplyCode(pSAMResponse.getReplyCode());
			transData.setReplyMessage(pSAMResponse.getReplyMessage());
		}
		
		//
		rtnCode = dbService.update_PSAM_TRANINFO(transData);		
		//log.info(clientChannel.attr(PSAMTrans).get().getTermId());
		
		
		if ((clientChannel.isActive()) && (clientChannel.attr(PSAMTrans).get() == transData)) {
		
			pSAMResponse = new PSAMResponse(transData);
			clientChannel.writeAndFlush(pSAMResponse);
		}else if(!clientChannel.isActive()){
			log.warn("[" + transData.getTermId() + clientChannel+"][CREDITCARD 결제 핸들러] <주의> 단말기 접속 종료 - 응답전문 전송불가");
		}else {
			log.warn("[" + transData.getTermId() + clientChannel+"][CREDITCARD 결제 핸들러] <주의> 단말기 선행 전문 - 응답전문 전송제외");
		}
	}//
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		         
		Channel clientChannel = ctx.channel();
		
		PSAMTransData transData = (PSAMTransData) clientChannel.attr(PSAMTrans).get();
		
		if (((transData != null) || (!(cause instanceof IOException))) && (log.isErrorEnabled())) {
			log.error("[" + transData.getTermId()+ "][PSAM 결제 핸들러] <오류> 단말기 세션 예외발생 1- ["
					+ ctx.channel() + "]" + cause.getMessage());
		}//
		
		if(cause instanceof IOException) {
			return;
		}//
		
		if(transData !=null ) {
			try {
				log.info("[" + transData.getTermId()
				+ "][PSAM 결제 핸들러] <오류> 단말기 세션 예외발생 2- processTcpResponse ");
				
				processTcpResponse(clientChannel, null, transData );
				
			}catch(Exception ex) {
				if (log.isErrorEnabled()) {
					log.error("[" + transData.getTermId() + "][PSAM 결제 핸들러] <오류> 실패응답 예외발생 3- ["
							+ ctx.channel() + "]" + cause.getMessage());
				}
			}//
		}
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	
}
