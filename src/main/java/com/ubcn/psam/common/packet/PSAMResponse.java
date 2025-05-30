package com.ubcn.psam.common.packet;

import java.nio.charset.Charset;

import com.ubcn.psam.common.AbstractPersistentModel;
import com.ubcn.psam.common.PSAMConstants;
import com.ubcn.psam.common.util.ObjectUtils;
import com.ubcn.psam.common.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSAMResponse extends AbstractPersistentModel {/**
	 * 
	 */
	private static final long serialVersionUID = 951736851548273440L;
	
	public static final String FS="\034";
	
	public static final String SERVER_ID = "server_id";
	public static final String TERM_ID = "term_id";
	public static final String TRANS_TYPE = "trans_type";
	public static final String MESSAGE_TYPE = "message_type";
	public static final String INPUT_TYPE = "input_type";
	public static final String TERM_TRANS_NO = "term_trans_no";
	public static final String MESSAGE_VERSION = "message_version";
	public static final String CRYPTO_FLAG = "crypto_type";
	public static final String RFU = "rfu";
//	public static final String TRANS_DATE = "trans_date";
//	public static final String TRANS_TIME = "trans_time";
//	public static final String ACQ_TYPE = "acq_type";
	
	
	//MSGTYPE 01  인증코드키응답
	public static final String vanCode ="van_code"; //VAN구분코드 : 4
	public static final String samServNum = "sam_serv_num"; // SAM서버번호 2
	public static final String samNum ="sam_num"; // SAM번호 2
	public static final String CSN = "csn"; //Chip Serial Number 8
	public static final String secNum = "sec_num"; // 섹터번호 2	
	//public static final String authCodeKey = "auth_code_key"; // 인증 코드 키
	
	//MSGTYPE 02 (코드검증&)카드번호키응답
	public static final String certiCode = "certi_code"; //보안인증 코드 : 32
	public static final String cardNumNey = "card_num_key";
	
	//MSGTYPE 03  멀티SAM 인증서버 상태수집전문
	public static final String totalSam = "total_sam";
	public static final String idleSam = "idle_sam";
	public static final String badSam = "bad_sam";
	
	//MSGTYPE 03  멀티SAM 인증서버 리스타트
	//public static final String samServNum = "sam_serv_num"; // SAM서버번호 2
	
	public static final String REPLY_CODE = "reply_code";
	public static final String REPLY_MESSAGE = "reply_message";
	
	
	
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
	
//	public String getTransDate() {
//		return (String) getAttribute("trans_date");
//	}
//
//	public void setTransDate(String date) {
//		setAttribute("trans_date", date);
//	}
//
//	public String getTransTime() {
//		return (String) getAttribute("trans_time");
//	}
//
//	public void setTransTime(String time) {
//		setAttribute("trans_time", time);
//	}
//	
//	public String getAcqType() {
//		return (String) getAttribute("acq_type");
//	}
//
//	public void setAcqType(String type) {
//		setAttribute("acq_type", type);
//	}
	
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
	
	public String getCardNumKey() {
		return (String)getAttribute("card_num_key");
	}
	public void setCardNumKey(String cardNumKey) {
		setAttribute("card_num_key",cardNumKey);
	}
	
	public String getTotalSam() {
		return (String) getAttribute("total_sam");
	}	
	public void setTotalSam(String totalSam) {
		setAttribute("total_sam", totalSam);
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
		setAttribute("idle_sam", badSam);
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
	
	public PSAMResponse() {
		
	}
	
	public PSAMResponse(PSAMTransData trans) {
		if(trans == null) {
			return;
		}else {
			setTermId(trans.getTermId());
			
			setTransType(trans.getTransType());
			setMessageType(trans.getMessageType());
			setInputType(trans.getInputType());
			setTermTransNo(trans.getTermTransNo());
			setMessageVersion(trans.getMessageType());
			setCryptoFlag(trans.getCryptoFlag());
			
			setReplyCode(trans.getReplyCode());			
			setReplyMessage(trans.getReplyMessage());
			
			if(PSAMConstants.MESSAGE_TYPE_AUTH.equals(trans.getMessageType())) {  //인즈코드키 요청
				setVanCode(trans.getVanCode());
				setSamServNum(trans.getSamServNum());			
				setSamNum(trans.getSamNum());
				setCsn(trans.getCsn());
				setSecNum(trans.getSecNum());
				setCertiCode(trans.getCertiCode());
				
			}else if(PSAMConstants.MESSAGE_TYPE_PURCHASE.equals(trans.getMessageType())) {  // (코드검증&)카드번호키요청
				setVanCode(trans.getVanCode());
				setSamServNum(trans.getSamServNum());			
				setSamNum(trans.getSamNum());
				setCsn(trans.getCsn());
				setSecNum(trans.getSecNum());				
				setCardNumKey(trans.getCardNumKey());
					
			}else if(PSAMConstants.MESSAGE_TYPE_STATUS.equals(trans.getMessageType())) { //멀티SAM 인증서버 상태수집전문
				setSamServNum(trans.getSamServNum());				
				setTotalSam(trans.getTotalSam());
				setIdleSam(trans.getIdleSam());
				setBadSam(trans.getBadSam());
			}else if(PSAMConstants.MESSAGE_TYPE_RESET.equals(trans.getMessageType())) { //멀티SAM 인증서버 SAM 리셋전문	
				setSamServNum(trans.getSamServNum());			
				setSamNum(trans.getSamNum());
			}else {
				//psamConstants.MESSAGE_TYPE_RESTART.equals(request.getMessageType())) // 멀티SAM 인증서버 리스타트전문
				setSamServNum(trans.getSamServNum());			
			}
		}//
		
		return;
	}
	
	
	public byte[] toMessage(Charset charset) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(StringUtils.adjustByteCount(getTermId(), 10, 'X', charset))
		  .append(StringUtils.adjustByteCount(getTransType(), 2, 'X', charset))
		  .append(StringUtils.adjustByteCount(getMessageType(), 2, 'X', charset))
		  .append(StringUtils.adjustByteCount(getInputType(), 1, 'X', charset))
		  .append(StringUtils.adjustByteCount(getTermTransNo(), 5, charset))
		  .append(StringUtils.adjustByteCount((String) ObjectUtils.nullValue(getMessageVersion(), "T1"), 2,charset))
		  .append(StringUtils.adjustByteCount(getCryptoFlag(), 1, '0', charset))
		  .append(StringUtils.adjustByteCount(getRfu(), 10, charset)).append(FS);
		
		if(PSAMConstants.MESSAGE_TYPE_AUTH.equals(getMessageType())) {  //인즈코드키 요청
			sb.append(getVanCode()).append(FS);
			sb.append(getSamServNum()).append(FS);			
			sb.append(getSamNum()).append(FS);
			sb.append(getCsn()).append(FS);
			sb.append(getSecNum()).append(FS);
			sb.append(getCertiCode()).append(FS);
			sb.append(getReplyCode()).append(FS);			
			sb.append(StringUtils.rpadB(StringUtils.substringByBytes((String) ObjectUtils.nullValue(getReplyMessage(), "잠시 후 재조회 요망(5110)"),1,32),' ',32));
		}else if(PSAMConstants.MESSAGE_TYPE_PURCHASE.equals(getMessageType())) {  // (코드검증&)카드번호키요청
			sb.append(getVanCode()).append(FS);
			sb.append(getSamServNum()).append(FS);			
			sb.append(getSamNum()).append(FS);
			sb.append(getCsn()).append(FS);
			sb.append(getSecNum()).append(FS);
			sb.append(getCardNumKey()).append(FS);
			sb.append(getReplyCode()).append(FS);			
			sb.append(StringUtils.rpadB(StringUtils.substringByBytes((String) ObjectUtils.nullValue(getReplyMessage(), "잠시 후 재조회 요망(5110)"),1,32),' ',32));	
		}else if(PSAMConstants.MESSAGE_TYPE_STATUS.equals(getMessageType())) { //멀티SAM 인증서버 상태수집전문
			sb.append(getSamServNum()).append(FS);
			sb.append(getTotalSam()).append(FS);
			sb.append(getIdleSam()).append(FS);
			sb.append(getBadSam()).append(FS);
			sb.append(getReplyCode()).append(FS);			
			sb.append(StringUtils.rpadB(StringUtils.substringByBytes((String) ObjectUtils.nullValue(getReplyMessage(), "잠시 후 재조회 요망(5110)"),1,32),' ',32));
			
		}else if(PSAMConstants.MESSAGE_TYPE_RESET.equals(getMessageType())) { //멀티SAM 인증서버 SAM 리셋전문	
			sb.append(getSamServNum()).append(FS);			
			sb.append(getSamNum()).append(FS);			
			sb.append(getReplyCode()).append(FS);			
			sb.append(StringUtils.rpadB(StringUtils.substringByBytes((String) ObjectUtils.nullValue(getReplyMessage(), "잠시 후 재조회 요망(5110)"),1,32),' ',32));
		}else {
			//psamConstants.MESSAGE_TYPE_RESTART.equals(request.getMessageType())) // 멀티SAM 인증서버 리스타트전문
			sb.append(getSamServNum()).append(FS);			
			sb.append(getReplyCode()).append(FS);			
			sb.append(StringUtils.rpadB(StringUtils.substringByBytes((String) ObjectUtils.nullValue(getReplyMessage(), "잠시 후 재조회 요망(5110)"),1,32),' ',32));
		}
		
		log.info("응답:{}",sb.toString());
		
		return sb.toString().getBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("PSAMResponse [");
		sb.append("ServerId="+getServerId()+",");
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
		sb.append("ReplyCode="+getReplyCode()+",");			
		sb.append("ReplyMessage"+getReplyMessage()+",");
		
		if(PSAMConstants.MESSAGE_TYPE_AUTH.equals(getMessageType())) {  //인즈코드키 요청
			sb.append("SamServNum="+getSamServNum()+",");			
			sb.append("SamNum="+getSamNum()+",");
			sb.append("Csn="+getCsn()+",");
			sb.append("SecNum="+getSecNum()+",");
			sb.append("CardNumKey="+getCardNumKey());
		}else if(PSAMConstants.MESSAGE_TYPE_STATUS.equals(getMessageType())) { //멀티SAM 인증서버 상태수집전문
			sb.append("SamServNum="+getSamServNum()+",");
			sb.append("TotalSam="+getTotalSam()+",");
			sb.append("IdleSam="+getIdleSam()+",");
			sb.append("BadSam="+getBadSam());
		}else if(PSAMConstants.MESSAGE_TYPE_RESET.equals(getMessageType())) { //멀티SAM 인증서버 SAM 리셋전문	
			sb.append("SamServNum="+getSamServNum()+",");			
			sb.append("SamNum="+getSamNum());
		}else {
			//psamConstants.MESSAGE_TYPE_RESTART.equals(request.getMessageType())) // 멀티SAM 인증서버 리스타트전문
			sb.append(getSamServNum()).append(FS);			
		}		
		
		sb.append("]");
		
		
		return sb.toString();
			
	}
	
	

}
