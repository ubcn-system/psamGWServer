package com.ubcn.psam.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@PropertySource("file:config/psam.properties")
public class PropService {
	
	@Value("${spring.PSAM.gw.server}")
	public String psamGWServNum;
	
	@Value("${spring.netty.serverId}")
	public String serverId;
	
	@Value("${spring.PSAM.server.NUM}")
	public String psamNum;

	public String serverNum;
	
	public String getServerNum() {
		return serverNum;
	}
	public void setServerNum(String serverNum) {
		this.serverNum = serverNum;
	}
	
	
	public String getPsamGWServNum() {
		return psamGWServNum;
	}
	public void setPsamGWServNum(String psamGWServNum) {
		this.psamGWServNum = psamGWServNum;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getPsamNum() {
		return psamNum;
	}
	public void setPsamNum(String psamNum) {
		this.psamNum = psamNum;
	}
	
	
	
	

}
