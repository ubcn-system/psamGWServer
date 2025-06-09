package com.ubcn.psam.common.packet;

import java.nio.charset.Charset;

import com.ubcn.psam.common.AbstractPersistentModel;
import com.ubcn.psam.common.PSAMConstants;
import com.ubcn.psam.common.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSAMRequest extends AbstractPersistentModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4792482397371088780L;
	
	public static final String FS="\034";
	
	public static final String SERVER_ID = "server_id";
	public static final String TERM_ID = "term_id";
	public static final String TRANS_TYPE = "trans_type";
	public static final String MESSAGE_TYPE = "message_type";
	public static final String INPUT_TYPE = "input_type";
	public static final String TERM_TRANS_NO = "term_trans_no";
	public static final String MESSAGE_VERSION = "message_version";
	public static final String CRYPTO_FLAG = "encryption_flag";
	public static final String RFU = "rfu";
	
	//MSGTYPE 01  인증코드키요청
	public static final String vanCode ="van_code"; //VAN구분코드 : 4
	public static final String samServNum = "sam_serv_num"; // SAM서버번호 2
	public static final String samNum ="sam_num"; // SAM번호 2
	public static final String CSN = "csn"; //Chip Serial Number 8
	public static final String secNum = "sec_num"; // 섹터번호 2
	//MSGTYPE 02 (코드검증&)카드번호키요청전문
	//MSGTYPE 01  인증코드키요청
	public static final String certiCode = "certi_code"; //보안인증 코드 : 32
	//MSGTYPE 03  멀티SAM 인증서버 상태수집전문
	//MSGTYPE 03  멀티SAM 인증서버 리스타트
	//public static final String samServNum = "sam_serv_num"; // SAM서버번호 2
			
		
	
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
		return (String) getAttribute("encryption_flag");
	}

	public void setCryptoFlag(String flag) {
		setAttribute("encryption_flag", flag);
	}

	public String getRfu() {
		return (String) getAttribute("rfu");
	}

	public void setRfu(String rfu) {
		setAttribute("rfu", rfu);
	}
	
	public String getVanCode() {
		return (String)getAttribute("van_code");
	}
	public void setVanCode(String vanCode) {
		setAttribute("van_code",vanCode);
	}
	
	public String getSamServNum() {
		return (String)getAttribute("sam_serv_num");
	}
	public void setSamServNum(String samServNum) {
		setAttribute("sam_serv_num",samServNum);
	}
	
	public String getSamNum() {
		return (String)getAttribute("sam_num");
	}
	public void setSamNum(String samNum) {
		setAttribute("sam_num",samNum);
	}
	
	public String getCsn() {
		return (String)getAttribute("csn");
	}
	public void setCsn(String csn) {
		setAttribute("csn",csn);
	}
	
	public String getSecNum() {
		return (String)getAttribute("sec_num");
	}
	public void setSecNum(String secNum) {
		setAttribute("sec_num",secNum);
	}
	
	public String getCertiCode() {
		return (String)getAttribute("certi_code");
	}
	public void setCertiCode(String certiCode) {
		setAttribute("certi_code",certiCode);
	}

	public static PSAMRequest fromMessage(String serverId,byte message[], Charset charset) {
		PSAMRequest request = new PSAMRequest();
		
		try {
			request.setServerId(serverId);
			request.setTermId(new String(message, 0, 10, charset));
			request.setTransType(new String(message, 10, 2, charset));
			request.setMessageType(new String(message, 12, 2, charset));
			request.setInputType(new String(message, 14, 1, charset));
			request.setTermTransNo(new String(message, 15, 5, charset));
			request.setMessageVersion(new String(message, 20, 2, charset));
			request.setCryptoFlag(new String(message, 22, 1, charset));
			request.setRfu((new String(message, 23, 10, charset)).trim());
			
			String fields[] = (new String(message,34, message.length - 34,charset)).split(FS);
			String msgData = new String(message,0,message.length,charset);
			log.info("전문:{}",msgData);
			
			if(PSAMConstants.MESSAGE_TYPE_AUTH.equals(request.getMessageType())) {  //인즈코드키 요청
				request.setVanCode(fields[0]);
				request.setSamServNum(fields[1]);
				request.setSamNum(fields[2]);
				request.setCsn(fields[3]);
				request.setSecNum(fields[4]);
			}else if(PSAMConstants.MESSAGE_TYPE_PURCHASE.equals(request.getMessageType())) { //. (코드검증&)카드번호키요청
				request.setVanCode(fields[0]);
				request.setSamServNum(fields[1]);
				request.setSamNum(fields[2]);
				request.setCsn(fields[3]);
				request.setSecNum(fields[4]);
				request.setCertiCode(fields[5]);
			}else if(PSAMConstants.MESSAGE_TYPE_RESET.equals(request.getMessageType())) { //멀티SAM 인증서버 SAM 리셋전문	
				request.setSamServNum(fields[0]);
				request.setSamNum(fields[1]);
			}else {
				//psamConstants.MESSAGE_TYPE_STATUS.equals(request.getMessageType()))  // 멀티SAM 인증서버 상태수집전문
				//psamConstants.MESSAGE_TYPE_RESTART.equals(request.getMessageType())) // 멀티SAM 인증서버 리스타트전문
				request.setSamServNum(fields[0]);
			}
			
		}catch(Exception ex) {
			StringUtils.getPrintStackTrace(ex);
		}
		
		return request;
	}
	
	public PSAMRequest() {
		
	}
	
	public PSAMRequest(PSAMTransData transData) {
		setServerId(transData.getServerId());
		setTermId(transData.getTermId());
		setTransType(transData.getTransType());
		setMessageType(transData.getMessageType());
		setInputType(transData.getInputType());
		setTermTransNo(transData.getTermTransNo());
		setMessageVersion(transData.getMessageVersion());
		setCryptoFlag(transData.getCryptoFlag());
		
		setVanCode(transData.getVanCode());
		setSamServNum(transData.getSamServNum());
		setSamNum(transData.getSamNum());
		setCsn(transData.getCsn());
		setSecNum(transData.getSecNum());		
		setCertiCode(transData.getCertiCode());
	}
	
	public byte[] toMessage() {
		return toMessage(StringUtils.EUC_KR);
	}
	
	
	public byte[] toMessage(Charset charset) {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(getTermId());
			sb.append(getTransType());
			sb.append(getMessageType());
			sb.append(getInputType());
			sb.append(getTermTransNo());
			sb.append(getMessageVersion());
			sb.append(getCryptoFlag());
			sb.append(getRfu());
			sb.append(FS);
			if(PSAMConstants.MESSAGE_TYPE_AUTH.equals(getMessageType())) {  //인즈코드키 요청
				sb.append(getVanCode()).append(FS);
				sb.append(getSamServNum()).append(FS);
				sb.append(getSamNum()).append(FS);
				sb.append(getCsn()).append(FS);
				sb.append(getSecNum());
			}else if(PSAMConstants.MESSAGE_TYPE_PURCHASE.equals(getMessageType())) { //. (코드검증&)카드번호키요청
				sb.append(getVanCode()).append(FS);
				sb.append(getSamServNum()).append(FS);
				sb.append(getSamNum()).append(FS);
				sb.append(getCsn()).append(FS);
				sb.append(getSecNum()).append(FS);
				sb.append(getCertiCode());
			}else if(PSAMConstants.MESSAGE_TYPE_RESET.equals(getMessageType())) { //멀티SAM 인증서버 SAM 리셋전문	
				sb.append(getSamServNum()).append(FS);
				sb.append(getSamNum());
			}else {
				//psamConstants.MESSAGE_TYPE_STATUS.equals(request.getMessageType()))  // 멀티SAM 인증서버 상태수집전문
				//psamConstants.MESSAGE_TYPE_RESTART.equals(request.getMessageType())) // 멀티SAM 인증서버 리스타트전문
				sb.append(getSamServNum());
			}//			
		}catch(Exception ex) {
			StringUtils.getPrintStackTrace(ex);
		}
		
		return sb.toString().getBytes(charset);
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PSAMRequest [");
		sb.append("TremID="+getTermId()+",");
		sb.append("TransType="+getTransType()+",");
		sb.append("MessageType="+getMessageType()+",");
		sb.append("InputType="+getInputType()+",");
		sb.append("TermTransNo="+getTermTransNo()+",");
		sb.append("MessageVersion="+getMessageVersion()+",");
		sb.append("CryptoFlag="+getCryptoFlag()+",");
		sb.append("VanCode="+getVanCode()+",");
		sb.append("SamServNum="+getSamServNum()+",");
		sb.append("SamNum="+getSamNum()+",");
		sb.append("Csn="+getCsn()+",");
		sb.append("SecNum="+getSecNum()+",");
		sb.append("CertiCode="+getCertiCode()+",");
		sb.append("]");
		
		return sb.toString();  
	}
	
	
	
}
