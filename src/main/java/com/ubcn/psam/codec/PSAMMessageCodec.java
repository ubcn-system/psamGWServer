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
public class PSAMMessageCodec extends ByteToMessageCodec<PSAMResponse>{

	public static final byte STX = 2;
	public static final byte ETX = 3;
	public static final int PDU_MAX_LENGTH = 10009;
	private final String serverId;
	private final Charset encodingCharset;
	
	public PSAMMessageCodec(String serverId, Charset charset) {
		this.serverId = serverId;
		this.encodingCharset = charset;
	}
	
	//단말기 수신
		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

			try {

				if (log.isDebugEnabled()) {
					log.debug("[" + serverId + "][단말기 전문 코덱] decode : 수신\n\tchannel = " + ctx.channel()
							+ "\n\tgarbage = [" + in.readableBytes() + "][" + ByteBufUtil.hexDump(in) + "]");
				}


				if (!in.isReadable()) {
					if (log.isWarnEnabled()) {
						log.warn("[" + serverId + "][단말기 전문 코덱] decode : 경고 - 수신 데이터 없음\n\tchannel = " + ctx.channel());
					}

					return;
				}

				while (in.isReadable()) {
					int index = in.indexOf(in.readerIndex(), in.writerIndex(), (byte) 2);


					if (index < 0) {

							log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> STX1 가 없음\n\tchannel = "
									+ ctx.channel() + "\n\tgarbage = [" + in.readableBytes() + "]["
									+ ByteBufUtil.hexDump(in) + "]");

						in.readerIndex(in.writerIndex());
						break;
					}

					if (index > in.readerIndex()) {

							log.warn("[" + serverId + "][단말기 전문 코덱] decode : <경고> STX 이전 데이터 제거\n\tchannel = "
									+ ctx.channel() + "\n\tgarbage = [" + (index - in.readerIndex()) + "]["
									+ ByteBufUtil.hexDump(in, in.readerIndex(), index - in.readerIndex()) + "]");

						in.readerIndex(index);
					}

					index = in.indexOf(in.readerIndex() + 1, in.writerIndex(), (byte) 3);

					if (index < 0) {


						log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> ETX 없음  \n\tchannel = "
								+ ctx.channel() + "\n\tgarbage = [" + (in.writerIndex()) + "]["
								+ ByteBufUtil.hexDump(in, in.readerIndex(), in.writerIndex() ) + "]");


						index = in.indexOf(in.readerIndex() + 1, in.writerIndex(), (byte) 2);


						if (index > in.readerIndex()) {

								log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> STX 이전 데이터 제거\n\tchannel = "
										+ ctx.channel() + "\n\tgarbage = [" + (index - in.readerIndex()) + "]["
										+ ByteBufUtil.hexDump(in, in.readerIndex(), index - in.readerIndex()) + "]");


							in.readerIndex(index);

						} else {

							if (in.readableBytes() <= 10009)
								break;


							log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> 너무 긴 전문\n\tchannel = "
										+ ctx.channel() + "\n\tgarbage = [" + in.readableBytes() + "]["
										+ ByteBufUtil.hexDump(in) + "]");

							in.readerIndex(in.writerIndex());
							break;
						}

					} else {

						ByteBuf frame = in.readSlice(index - in.readerIndex() + 1);

						if (frame.readableBytes() < 6) {

								log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> 너무 짧은 전문\n\tchannel = "
										+ ctx.channel() + "\n\tframe = [" + frame.readableBytes() + "]["
										+ ByteBufUtil.hexDump(frame) + "]");

							out.add(null);

						} else {

							frame.skipBytes(1);

							String slength = frame.readCharSequence(4, encodingCharset).toString();

							if (!StringUtils.isInteger(slength)) {


									log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> 전문길이 불일치1 \n\tchannel = "
											+ ctx.channel() + "\n\tinput = [" + frame.readableBytes() + "]["
											+ frame.toString(encodingCharset) + "]");

								ctx.channel().close();
							}else {

								int length = Integer.parseInt(slength);

								if (length != frame.readableBytes()) {

										log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> 전문길이 불일치2\n\tchannel = "
												+ ctx.channel() + "\n\tlength = " + length + "\n\tinput = ["
												+ frame.readableBytes() + "][" + frame.toString(encodingCharset) + "]");


									ctx.channel().close();

								} else {

									byte[] message = new byte[length - 1];
									frame.readBytes(message);


									log.info("[" + serverId + "][단말기 수신] \n\tchannel = [ = " + ctx.channel() + " [" + length + "]["
											+ new String(message, encodingCharset) + "]");

									PSAMRequest request = PSAMRequest.fromMessage(serverId, message, encodingCharset);

								 	log.info("channel = [" + ctx.channel()+"]"	+ "\n\t 단말 수신 = " + request.toString());

									out.add(request);
									
									Arrays.fill(message, (byte)0); //메모리 삭제
								}
							}

						}
						
						frame.clear();  //메모리 삭제
					}
				} //while
				
			} catch (Exception ex) {
				if (log.isErrorEnabled()) {
					log.error("[" + serverId + "][단말기 전문 코덱] decode : <오류> " + ex.getMessage() + "\n\tchannel = "
							+ ctx.channel(), ex);
				}
			}

			if (log.isTraceEnabled()) {
				log.trace("[" + serverId + "][단말기 전문 코덱] decode : 완료\n\tchannel = " + ctx.channel());
			}
			
			//in.clear(); //단말기 전문 버퍼 메모리 삭제  -- 이코드가 있으면 전문이 2부분으로 나눠들어올때 이전 버퍼메모리가 삭제되어 처리가 안됟다. 2021.12.30 ISJUNG		
		}

		//단말기 송신
		@Override
		protected void encode(ChannelHandlerContext ctx, PSAMResponse msg, ByteBuf out) throws Exception {


			try {
				if (msg.getServerId() == null) {
					msg.setServerId(serverId);
				}

				if (log.isDebugEnabled()) {
					log.debug("[" + serverId + "][단말기 전문 코덱] encode : 전송\n\tchannel = [" + ctx.channel()
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

				//log.info("[" + serverId + "][단말기 송신] \n\tchannel = [ = " + ctx.channel() + " [" + Integer.valueOf(message.length + 1)  + "]["
				//		+ new String(message, encodingCharset) + "]");
				
				//Arrays.fill(message, (byte) 0);  //응답 : out 메모리 삭제
				
			} catch (Exception ex) {
				if (log.isErrorEnabled()) {
					log.error("[" + serverId + "][단말기 전문 코덱] encode : <오류> " + ex.getMessage() + "\n\tchannel = "
							+ ctx.channel(), ex);
				}
			}finally {
				msg.clearAttributes(); //응답 메모리 삭제	
			}

			if (log.isTraceEnabled()) {
				log.trace("[" + serverId + "][단말기 전문 코덱] encode : 완료\n\tchannel = " + ctx.channel());
			}
			
			
		}

}
