package com.ubcn.psam.common.packet;

import com.ubcn.psam.common.AbstractPersistentModel;
import com.ubcn.psam.common.PSAMConstants;
import com.ubcn.psam.common.util.DateUtils2;

public class PSAMTransData extends AbstractPersistentModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5532227860510117281L;
	public static final String SERV_NAME = "serv_name";
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
		
	//MSGTYPE 03  멀티SAM 인증서버 상태수집전문
	public static final String totalSam = "total_sam";
	public static final String busySam = "busy_sam";
	public static final String idleSam = "idle_sam";
	public static final String badSam = "bad_sam";
	
	public static final String REPLY_CODE = "reply_code";
	public static final String REPLY_MESSAGE = "reply_message";

	public static final String cardNumNey = "card_num_key"; // 카드번호키
	
	public static final String AuthNumNey = "auth_num_key";  //인증번호키
	
	public static final String uniqueno = "uniqueno";
	public static final String serv_name ="serv_name";
	
	public static final String tran_date ="tran_date";
	public static final String tran_time ="tran_time";
	
	public static final String pSamServNum = "psam_servnum";
	
	public String getPSamServNum() {
		return (String) getAttribute("pSamServNum");
	}
	public void setPSamServNum(String pSamServNum) {
		setAttribute("pSamServNum", pSamServNum);
	}
	
	
	public String getAuthNumKey() {
		return (String) getAttribute("auth_num_key");
	}
	public void setAuthNumKey(String auth_num_key) {
		setAttribute("auth_num_key", auth_num_key);
	}
	
	public String getTranDate() {
		return (String) getAttribute("tran_date");
	}
	public void setTranDate(String tran_date) {
		setAttribute("tran_date", tran_date);
	}
	
	public String getTranTime() {
		return (String) getAttribute("tran_time");
	}
	public void setTranTime(String tran_time) {
		setAttribute("tran_time", tran_time);
	}
	
	
	public String getServName() {
		return (String) getAttribute("serv_name");
	}

	public void setServName(String serv_name) {
		setAttribute("serv_name", serv_name);
	}
	
	public String getUniqueno() {
		return (String) getAttribute("uniqueno");
	}

	public void setUniqueno(String uniqueno) {
		setAttribute("uniqueno", uniqueno);
	}	
	
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
	
	public String getTotalSam() {
		return (String) getAttribute("total_sam");
	}	
	public void setTotalSam(String totalSam) {
		setAttribute("total_sam", totalSam);
	}
	
	public String getBusySam() {
		return (String) getAttribute("busy_sam");
	}	
	public void setBusySam(String busy_sam) {
		setAttribute("busy_sam", busy_sam);
	}
	
	
	public String getIdleSam() {
		return (String) getAttribute("idle_sam");
	}	
	public void setIdleSam(String idleSam) {
		setAttribute("idle_sam", idleSam);
	}
	
	public String getBadSam() {
		return (String) getAttribute("bad_sam");
	}	
	public void setBadSam(String badSam) {
		setAttribute("bad_sam", badSam);
	}
		
	public String getReplyCode() {
		return (String) getAttribute("reply_code");
	}

	public void setReplyCode(String code) {
		setAttribute("reply_code", code);
	}

	public String getReplyMessage() {
		return (String) getAttribute("reply_message");
	}

	public void setReplyMessage(String message) {
		setAttribute("reply_message", message);
	}
	
	public String getCardNumKey() {
		return (String)getAttribute("card_num_key");
	}
	public void setCardNumKey(String cardNumKey) {
		setAttribute("card_num_key",cardNumKey);
	}
	
	public PSAMTransData(PSAMRequest request, String uniqueno, String serv_name) {
		
		setUniqueno(uniqueno);
		
		setTranDate(DateUtils2.getToday());
		setTranTime(DateUtils2.getNowTime());
		
		setServerId(serv_name);
		setServName(serv_name);
		setTermId(request.getTermId());
		setTransType(request.getTransType());
		setMessageType(request.getMessageType());
		setInputType(request.getInputType());
		setTermTransNo(request.getTermTransNo());
		setMessageVersion(request.getMessageVersion());
		setCryptoFlag(request.getCryptoFlag());
		
		setVanCode(request.getVanCode());
		setSamServNum(request.getSamServNum());
		setSamNum(request.getSamNum());
		setCsn(request.getCsn());
		setSecNum(request.getSecNum());		
		setCertiCode(request.getCertiCode());
		
	}

}
