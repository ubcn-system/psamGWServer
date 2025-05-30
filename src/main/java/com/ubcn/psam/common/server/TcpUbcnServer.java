package com.ubcn.psam.common.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.ubcn.psam.codec.PSAMMessageCodec;
import com.ubcn.psam.common.AbstractService;
import com.ubcn.psam.common.util.ArgumentUtils;
import com.ubcn.psam.handler.psamAuthHandler;
import com.ubcn.psam.service.DBService;
import com.ubcn.psam.service.PropService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

@PropertySource("file:config/psam.properties")
public abstract class TcpUbcnServer extends AbstractService implements UbcnServer {
	
	public static final int DEFAULT_IDLE_TIMEOUT_SECONDS = 20; //VAN 연결후 응답 시간 timeout
	public static final int DEFAULT_WAIT_TIMEOUT_SECONDS = 60;
	
	@Autowired 
	public DBService dbService;
	@Autowired
	public PropService propService;
	
	
	@Value("${spring.netty.serverId}")
	private String serverId;

	@Value("${spring.netty.port}")
	private int serverPort;

	private int idleTimeoutSeconds = DEFAULT_IDLE_TIMEOUT_SECONDS;

	@Value("${spring.netty.threads.acceptor}")
	private int bossCount;

	@Value("${spring.netty.threads.worker}")
	private int workerCount;

	@Value("${spring.netty.backlog}")
	private int backlog;  //netty Queue의 크기
	
	private ChannelHandler sessionHandler;
	private ChannelHandler serviceHandler;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	private ChannelGroup clientChannels;
	private ServerBootstrap serverBootstrap;
	private Charset encodingCharset = Charset.forName("EUC-KR");

	private Channel serverChannel;
	
	public TcpUbcnServer() {		
	}
	
	public class ClientChannelHandler extends IdleStateHandler {

		public ClientChannelHandler(int idleTimeoutSeconds) {
			super(0, 0, idleTimeoutSeconds);
		}

		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			clientChannels.add(ctx.channel());

			// if (logger.isDebugEnabled()) {
			// logger.info("[" + TcpUbcnServer.this.getServerId() + "][클라이언트 핸들러]
			// channelActive : 등록\n\tchannel = "
			// + ctx.channel() + "\n\tchannelCount = " + clientChannels.size());

			SocketAddress socketAddress = ctx.channel().remoteAddress();
			String address = socketAddress.toString();
			String ip = address.substring(1, address.indexOf(':'));

			if (!ip.contains("172.29.100.")) {
				logger.info("[" + TcpUbcnServer.this.getServerId() + "][클라이언트 핸들러] channelActive : 등록\n\tchannel = "
						+ ctx.channel() + "\n\tchannelCount  = " + clientChannels.size());
			}
			// }
			super.channelActive(ctx);
		}

		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			super.channelInactive(ctx);

			// if (logger.isDebugEnabled()) {
			// logger.info("[" + TcpUbcnServer.this.getServerId() + "][클라이언트 핸들러]
			// channelInactive : 제거\n\tchannel = "
			// + ctx.channel() + "\n\tchannelCount = " + clientChannels.size());
			// }

			SocketAddress socketAddress = ctx.channel().remoteAddress();
			String address = socketAddress.toString();
			String ip = address.substring(1, address.indexOf(':'));

			if (!ip.contains("172.29.100.")) {
				logger.info("[" + TcpUbcnServer.this.getServerId() + "][클라이언트 핸들러] channelActive : 제거\n\tchannel = "
						+ ctx.channel() + "\n\tchannelCount  = " + clientChannels.size());
			}
		}

		protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent event) throws Exception {
			Channel channel;
			try {
				channel = ctx.channel();

				if (logger.isDebugEnabled()) {
					logger.debug("[" + TcpUbcnServer.this.getServerId() + "][클라이언트 핸들러] channelIdle : 시작\n\tchannel = "
							+ channel + "\n\tisActive = [" + channel.isActive() + "]\n\tevent = [" + event + "]");
				}

				if ((channel.isActive()) && (event == IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT
						|| event == IdleStateEvent.ALL_IDLE_STATE_EVENT)) {
					if (logger.isDebugEnabled()) {
						logger.debug("[" + TcpUbcnServer.this.getServerId()
								+ "][클라이언트 핸들러] channelIdle :  타임아웃 \n\tchannel = " + channel);
					}

					channel.close();
				} else {
					super.channelIdle(ctx, event);
				}

				if (logger.isDebugEnabled()) {
					logger.debug("[" + TcpUbcnServer.this.getServerId() + "][클라이언트 핸들러] channelIdle : 완료\n\tchannel = "
							+ channel);
				}

			} finally {
				channel = null;
			}
		}
	}
	
	
	public TcpUbcnServer(String serverId, int serverPort) {
		setServerId(serverId);
		setServerPort(serverPort);
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}


	public int getIdleTimeoutSeconds() {
		return idleTimeoutSeconds;
	}


	public void setIdleTimeoutSeconds(int idleTimeoutSeconds) {
		this.idleTimeoutSeconds = idleTimeoutSeconds;
	}


	public int getBossCount() {
		return bossCount;
	}


	public void setBossCount(int bossCount) {
		this.bossCount = bossCount;
	}


	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}


	public ChannelHandler getSessionHandler() {
		return sessionHandler;
	}


	public void setSessionHandler(ChannelHandler sessionHandler) {
		this.sessionHandler = sessionHandler;
	}


	public ChannelHandler getServiceHandler() {
		return serviceHandler;
	}

	public void setServiceHandler(ChannelHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}
	
	
	public ChannelHandler getServiceHandlers() {
		return this.serviceHandler;
	}
	
	
	protected void initService() throws Exception {
		
		String serverId = getServerId();
		int serverPort = getServerPort();
		
		ArgumentUtils.validateNotEmpty(serverId, "serverId");
		ArgumentUtils.validateTrue(serverPort > 0, "serverPort > 0");
		ArgumentUtils.validateTrue(bossCount >= 0, "bossCount >= 0");
		ArgumentUtils.validateTrue(workerCount >= 0, "workerCount >= 0");
		
		try {
			
			logger.info("["+serverId+ "][서비스] initService");
			
			bossGroup = new NioEventLoopGroup(this.bossCount);
			workerGroup = new NioEventLoopGroup(this.workerCount);
			serverBootstrap = new ServerBootstrap();
			
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						
						ChannelPipeline pipeline = ch.pipeline();
						
						pipeline.addLast(new ChannelHandler[] { new LoggingHandler(LogLevel.INFO)});
						pipeline.addLast("control", new ClientChannelHandler(idleTimeoutSeconds));
						pipeline.addLast(new PSAMMessageCodec(serverId,encodingCharset));
						
						pipeline.addLast(new psamAuthHandler(dbService,propService));
						
						
					}
				}).option(ChannelOption.SO_BACKLOG, backlog).option(ChannelOption.SO_REUSEADDR, true)
				  .childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);
			
			if (this.logger.isInfoEnabled()) {
				this.logger.info("[" + serverId + "][서비스] initService : 부트 스트랩 생성 완료");
			}
			
			ChannelFuture f = serverBootstrap.bind(new InetSocketAddress(serverPort)).sync();
			if (this.logger.isInfoEnabled()) {
				 this.logger.info("[" + serverId + "][서비스] initService : 청취 시작 - channel = " + f.channel());
			}//
			this.serverChannel = f.channel();
			
			this.clientChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
					
			if (this.logger.isTraceEnabled()) {
				this.logger.trace("[" + serverId + "][서비스] initService : 완료");
			}
			
		}catch(ChannelException ex) {
			
			if (this.logger.isErrorEnabled()) {
				this.logger.error("[" + serverId + "][서비스] initService : 바인딩 실패  - serverPort = [" + serverPort + "]");
				ex.printStackTrace();
			}
			
			throw ex;
		}
	}
	
	protected abstract void initChannel(SocketChannel paramSocketChannel);
	
	protected void destroyService() throws Exception {
		String serverId = getServerId();
		try {

			if (this.logger.isTraceEnabled()) {
				this.logger.trace("[" + serverId + "][서비스] destroyService : 시작");
			}

			/*  //Channel 이 array 일때
			for (Channel channel : serverChannel) {
				if (channel != null) {
					if (this.logger.isInfoEnabled()) {
						this.logger.info("[" + serverId + "][서비스] destroyService : 청취 종료" + channel.toString());
					}
					channel.close().awaitUninterruptibly();
				}
			}*/
			
			if (serverChannel != null) {
				if (this.logger.isInfoEnabled()) {
					this.logger.info("[" + serverId + "][서비스] destroyService : 청취 종료" + serverChannel.toString());
				}
				serverChannel.close().awaitUninterruptibly();
			}
			

			if (this.clientChannels.size() > 0) {
				if (this.logger.isInfoEnabled()) {
					this.logger.info("[" + serverId + "][서비스] destroyService :클라이언트 접속종료 대기(최대  " + 60 + "초) ");
				}
				if (!this.clientChannels.newCloseFuture().awaitUninterruptibly(60L, TimeUnit.SECONDS)) {
					if (this.logger.isInfoEnabled()) {
						this.logger.info("[" + serverId + "][서비스] destroyService : 클라이언트 강제 접속종료");
					}
					this.clientChannels.close().awaitUninterruptibly();
				}
			}

			if (this.workerGroup != null) {
				if (this.logger.isInfoEnabled()) {
					this.logger.info("[" + serverId + "][서비스] destroyService : 작업 쓰레드 그룹 종료 대기 ");
				}
				this.workerGroup.shutdownGracefully().awaitUninterruptibly();
			}
			if (this.bossGroup != null) {
				if (this.logger.isInfoEnabled()) {
					this.logger.info("[" + serverId + "][서비스] destroyService : 보스 쓰레드 종료 대기");
				}
				this.bossGroup.shutdownGracefully().awaitUninterruptibly();
			}

			Thread.sleep(2000L);

			if (this.logger.isTraceEnabled()) {
				this.logger.trace("[" + serverId + "][서비스] destroyService : 완료");
			}

		} catch (Exception ex) {
			if (this.logger.isErrorEnabled()) {
				this.logger.error("[" + serverId + "][서비스] destroyService : <오류>");
				ex.printStackTrace();
			}
			throw ex;
		} finally {
			this.bossGroup = null;
			this.workerGroup = null;
			this.serverChannel = null;
			this.clientChannels = null;
			this.serverBootstrap = null;
		}
	}
	
	
	

}
