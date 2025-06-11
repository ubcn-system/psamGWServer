package com.ubcn.psam.codec;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.ubcn.psam.common.packet.PSAMRequest;
import com.ubcn.psam.common.packet.PSAMResponse;
import com.ubcn.psam.common.util.CRCCheck;
import com.ubcn.psam.common.util.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSAMServMessageCodec extends ByteToMessageCodec<PSAMRequest>{
	
	public static final byte STX = 2;
	public static final byte ETX = 3;
	public static final int PDU_MAX_LENGTH = 10009;
	private final String serverId;
	private final Charset encodingCharset;
	
	public PSAMServMessageCodec(String serverId, Charset encodingCharset) {
		this.serverId = serverId;
		this.encodingCharset = encodingCharset;
	}
	
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		//stx 체크여부
			boolean stxCheck = false;
			//전문길이 문자열
			String slength ="";
			//전문길이
			int length = 0;
			//readerIndex
			int index = 0;

			try {
				
				if (!in.isReadable()) {
					if (log.isWarnEnabled()) {
						log.info("[" + serverId + "][PSAM 전문 코덱] decode : 경고 - 수신 데이터 없음\n\tchannel = " + ctx.channel());
					}
					return;
				}
				while (in.isReadable()) {
					
					if(!stxCheck) {
						//STX 체크
						index = in.indexOf(in.readerIndex(), in.writerIndex(), (byte) 2);
						
						log.debug("STX {}",index); //isjung

						//STX가 없으면
						if (index < 0) {

							if (log.isErrorEnabled()) {
								log.error("[" + serverId + "][PSAM 전문 코덱] decode : <오류> STX1 가 없음\n\tchannel = "
										+ ctx.channel() + "\n\tgarbage = [" + in.readableBytes() + "]["
										+ ByteBufUtil.hexDump(in) + "]");
							}

							in.readerIndex(in.writerIndex());
						}

						//STX가 중간에 있으면
						if (index > in.readerIndex()) {
							if (log.isWarnEnabled()) {
								log.warn("[" + serverId + "][VAN 전문 코덱] decode : <경고> STX 이전 데이터 제거\n\tchannel = "
										+ ctx.channel() + "\n\tgarbage = [" + (index - in.readerIndex()) + "]["
										+ ByteBufUtil.hexDump(in, in.readerIndex(), index - in.readerIndex()) + "]");
							}

							//readerindex 위치를 STX 위치로 이동
							in.readerIndex(index);
						}

						stxCheck = true;
						
						//STX 부터  전문길이 까지 버퍼 생성
						ByteBuf frame = in.readSlice(5);
						//STX 1바이트 스킵
						frame.skipBytes(1);
						//길이 4바이트 일기
						slength = frame.readCharSequence(4, encodingCharset).toString();

						//길이 4바이트가 숫자인지 체크
						if (!StringUtils.isNumeric(slength)) {
							if (log.isErrorEnabled()) {
								log.error("[" + serverId + "][PSAM 전문 코덱] decode : <오류> 전문길이 불일치1 \n\tchannel = "
										+ ctx.channel() + "\n\tinput = [" + frame.readableBytes() + "]["
										+ frame.toString(encodingCharset) + "]");
							}
							ctx.channel().close();
						}

						length = Integer.parseInt(slength);

						//전문길이까지 인덱스 이동
						in.readerIndex(5);
						
						frame.clear(); //메모리 삭제 					
					}
					
					log.info("[" + serverId + "][VAN전문 코덱] encode : 응답\n\tchannel = " + ctx
							.channel() + "\n\tencoded = ["+ in.readableBytes()+ "][" + in.toString(encodingCharset)+ "]");


					//전문길이와 전문이 일치하면
					if (length  <= in.writerIndex()) {  //default 1024 이상이면 추가 셋팅 필요 ISJUNG
						
						//단말기ID 부터 EXT 이전 길이만큼 버퍼 생성
						ByteBuf frame2 = in.readSlice(length);

						byte[] message = new byte[length - 1];
						frame2.readBytes(message);
						
						//isjung 20211019----
						//log.info("[" + serverId + "][VAN 전문 코덱] decode : 성공\n\tchannel = " + ctx.channel() + "\n\tKocessResponse = " + new String(message,encodingCharset));
						//-----------
						
						PSAMResponse response = PSAMResponse.fromMessage(message,encodingCharset);
						//if (log.isDebugEnabled()) {
							
						log.info("[" + serverId + "][PSAM 전문 코덱] decode : 성공\n\tchannel = " + ctx.channel() + "\n\tPSAMResponse = " + response.getAttributes().toString());
						//}

						log.info("channel = [" + ctx.channel()+"]"
								+ "\n\t PSAM 수신 = " + response.toString());

						out.add(response);
						
						frame2.clear();  //메모리 삭제 
						Arrays.fill(message, (byte)0); //메모리 삭제 
					}

				}//while


			} catch (Exception ex) {
				if (log.isErrorEnabled()) {
					log.error("[" + serverId + "][VAN 전문 코덱] decode : <오류> " + ex.getMessage() + "\n\tchannel = "
							+ ctx.channel(), ex);
				}
			}

			if (log.isTraceEnabled()) {
				log.trace("[" + serverId + "][VAN 전문 코덱] decode : 완료\n\tchannel = " + ctx.channel());
			}
			
			in.clear();  //요청 버퍼 메모리 삭제 
	}
		
	
	protected void encode(ChannelHandlerContext ctx, PSAMRequest msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		try {
			if (msg.getServerId() == null) {
				msg.setServerId(serverId);
			}

			if (log.isDebugEnabled()) {
				log.debug("[" + serverId + "][PSAM 전문 코덱] encode : 전송\n\tchannel = [" + ctx.channel()
						+ "\n\tTerminalMessageCodec = " + msg.getAttributes().toString());
			}
			
			byte[] message = msg.toMessage(encodingCharset);


			if("V".equals(msg.getMessageVersion().substring(0, 1))){

				int crcLen = CRCCheck.crc16_Bigendian(message, message.length);
				String crcHex = String.format("%04X", crcLen);

				 StringBuilder sb =new StringBuilder();
				 sb.append(crcHex);

				out.writeByte(2);
				out.writeBytes(String.format("%04d", new Object[] { Integer.valueOf(message.length + 5) }).getBytes(encodingCharset));
				out.writeBytes(message);
				out.writeByte(3);
				out.writeBytes( sb.append(crcHex).toString().getBytes(encodingCharset) );

			}else {

				out.writeByte(2);
				out.writeBytes(String.format("%04d", new Object[] { Integer.valueOf(message.length + 1) }).getBytes(encodingCharset));
				out.writeBytes(message);
				out.writeByte(3);

			}

			log.info("channel = [" + ctx.channel()+"]"	+ "\n\t 단말 송신 = " + msg.toString());

			//log.info("[" + serverId + "][PSAM 송신] \n\tchannel = [ = " + ctx.channel() + " [" + Integer.valueOf(message.length + 1)  + "]["
			//		+ new String(message, encodingCharset) + "]");
			
			//Arrays.fill(message, (byte) 0);  //응답 : out 메모리 삭제
			
		} catch (Exception ex) {
			if (log.isErrorEnabled()) {
				log.error("[" + serverId + "][PSAM 전문 코덱] encode : <오류> " + ex.getMessage() + "\n\tchannel = "
						+ ctx.channel(), ex);
			}
		}finally {
			msg.clearAttributes(); //응답 메모리 삭제	
		}

		if (log.isTraceEnabled()) {
			log.trace("[" + serverId + "][PSAM 전문 코덱] encode : 완료\n\tchannel = " + ctx.channel());
		}
		
	}

	


}
