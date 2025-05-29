package com.ubcn.psam;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.ubcn.psam.common.server.TcpUbcnServer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;

@SpringBootApplication
@PropertySource("file:config/application.properties")
@ComponentScan(basePackages = "com.ubcn.psam")
@EnableAutoConfiguration(exclude = { DataSourceTransactionManagerAutoConfiguration.class, DataSourceAutoConfiguration.class })
public class PsamGwServerRdbApplication extends TcpUbcnServer {

	public static final String DEFAULT_CHARACTER_ENCODING = "EUC-KR";
	private Charset encodingCharset = Charset.forName("EUC-KR");
	
	public static void main(String[] args) {
		SpringApplication.run(PsamGwServerRdbApplication.class, args);
	}
	
	public String getCharacterEncoding() {
		return encodingCharset.name();
	}

	public void setCharacterEncoding(String charsetName) {
		encodingCharset = Charset.forName(charsetName);
	}


	@Override
	public void setServiceHandlers(List<ChannelHandler> paramList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initChannel(SocketChannel paramSocketChannel) {
		// TODO Auto-generated method stub
		
	}
	

}
