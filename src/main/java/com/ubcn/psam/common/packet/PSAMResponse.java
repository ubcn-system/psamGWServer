package com.ubcn.psam.common.packet;

import java.nio.charset.Charset;

import com.ubcn.psam.common.AbstractPersistentModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSAMResponse extends AbstractPersistentModel {/**
	 * 
	 */
	private static final long serialVersionUID = 951736851548273440L;
	
	public static final String SERVER_ID = "server_id";
	public static final String TERM_ID = "term_id";
	public static final String TRANS_TYPE = "trans_type";
	public static final String MESSAGE_TYPE = "message_type";
	public static final String INPUT_TYPE = "input_type";
	public static final String TERM_TRANS_NO = "term_trans_no";
	public static final String MESSAGE_VERSION = "message_version";
	public static final String CRYPTO_FLAG = "crypto_type";
	public static final String RFU = "rfu";
	
	public String getServerId() {
		return (String) getAttribute("server_id");
	}

	public void setServerId(String id) {
		setAttribute("server_id", id);
	}
	
	public String getTermId() {
		return (String) getAttribute("term_id");
	}

	public void setTermId(String id) {
		setAttribute("term_id", id);
	}
	
	public String getTransType() {
		return (String) getAttribute("trans_type");
	}

	public void setTransType(String type) {
		setAttribute("trans_type", type);
	}
	
	
	public String getMessageType() {
		return (String) getAttribute("message_type");
	}

	public void setMessageType(String type) {
		setAttribute("message_type", type);
	}
	
	public String getInputType() {
		return (String) getAttribute("input_type");
	}

	public void setInputType(String type) {
		setAttribute("input_type", type);
	}

	public String getTermTransNo() {
		return (String) getAttribute("term_trans_no");
	}

	public void setTermTransNo(String no) {
		setAttribute("term_trans_no", no);
	}
	
	public String getMessageVersion() {
		return (String) getAttribute("message_version");
	}

	public void setMessageVersion(String version) {
		setAttribute("message_version", version);
	}

	public String getCryptoFlag() {
		return (String) getAttribute("crypto_type");
	}

	public void setCryptoFlag(String flag) {
		setAttribute("crypto_type", flag);
	}
	
	public String getRfu() {
		return (String) getAttribute("rfu");
	}

	public void setRfu(String rfu) {
		setAttribute("rfu", rfu);
	}
	
	public byte[] toMessage(Charset charset) {
		StringBuilder sb = new StringBuilder();
		
		
		return sb.toString().getBytes();
	}

}
