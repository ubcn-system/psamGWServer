package com.ubcn.psam.handler;

import java.nio.charset.Charset;
import java.util.Calendar;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.ubcn.psam.common.packet.PSAMResponse;
import com.ubcn.psam.common.util.DateUtils;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class psamHandlerAdapter extends ChannelDuplexHandler implements InitializingBean, DisposableBean {

	
	private String handlerID;
	private Charset encodingCharset;
	
	public psamHandlerAdapter() {
		handlerID = "psamHandler";
		encodingCharset = Charset.forName("EUC-KR");
	}

	public String getHandlerID() {
		return handlerID;
	}

	public void setHandlerID(String handlerID) {
		this.handlerID = handlerID;
	}

	public Charset getEncodingCharset() {
		return encodingCharset;
	}

	public void setEncodingCharset(Charset encodingCharset) {
		this.encodingCharset = encodingCharset;
	}
	
	public String getCharacterEncoding() {
		return encodingCharset.name();
	}

	public void setCharacterEncoding(String charsetName) {
		encodingCharset = Charset.forName(charsetName);
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub		
	}
	
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
		if(msg==null) {
			Calendar calendar = Calendar.getInstance();
			PSAMResponse res = new PSAMResponse();			
			//res.setTransDate(DateUtils.formatSimpleDate(calendar.getTime()));
			//res.setTransTime(DateUtils.formatSimpleTime(calendar.getTime()));
			//res.setAcqType("N");
			res.setReplyCode("7002");
			res.setReplyMessage("전문오류");
			
			ctx.writeAndFlush(res);
		}else {
			super.channelRead(ctx, msg);			
		}//
		
		return;
		
		
	}
	
	
}
