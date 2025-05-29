package com.ubcn.psam.common.server;

import java.util.List;

import com.ubcn.psam.common.Service;

import io.netty.channel.ChannelHandler;

public abstract interface UbcnServer extends Service {

	public abstract String getServerId();

	public abstract void setServerId(String paramString);

	public abstract int getServerPort();

	public abstract void setServerPort(int paramInt);

	public abstract int getIdleTimeoutSeconds();

	public abstract void setIdleTimeoutSeconds(int paramInt);

	public abstract ChannelHandler getSessionHandler();

	public abstract void setSessionHandler(ChannelHandler paramChannelHandler);

	public abstract ChannelHandler getServiceHandler();

	public abstract void setServiceHandler(ChannelHandler paramChannelHandler);

	public abstract ChannelHandler getServiceHandlers();

	public abstract void setServiceHandlers(List<ChannelHandler> paramList);
}
