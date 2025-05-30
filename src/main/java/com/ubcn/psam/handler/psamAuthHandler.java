package com.ubcn.psam.handler;

import com.ubcn.psam.common.packet.PSAMRequest;
import com.ubcn.psam.common.packet.PSAMResponse;
import com.ubcn.psam.common.packet.PSAMTransData;
import com.ubcn.psam.service.DBService;
import com.ubcn.psam.service.PropService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
		
		Channel clientChannel = ctx.channel(); //일반 보류
		
		PSAMTransData  transData = new PSAMTransData(request); 
		
		transData.setReplyCode("7001");
		transData.setReplyMessage(getCharacterEncoding());
		
		
		response = new PSAMResponse(transData);		
		ctx.writeAndFlush(response);
		
		return;
		
	}
	
}
