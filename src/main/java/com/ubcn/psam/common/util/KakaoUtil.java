package com.ubcn.psam.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@PropertySource("file:config/psam.properties")
public class KakaoUtil {
	
	@Value("${kakao.api.url}")
	private String apiURL;	
	@Value("${RM.MSG.FRAME}")
	private String rmMsgFrame;
	@Value("${RM.MSG}")
	private String rmMsg;
	@Value("${RM.TOKEN}")
	private String rmToken;
	@Value("${RM.COMPANYSEQ}")
	private String rmCompanySeq;	
	@Value("${RM.TEL.TEAM}")
	private String rmTel;
	@Value("${RM.SEND.YN}")
	public String RM_SEND_YN;
	
	/**
	 * 해당 전화번호에 카카오 메시지 보내기
	 * @param sndMsg
	 * @param whHOUR
	 * @param resCNT
	 */
	public void SendKakao(String sndMsg) {
		
		String kakaoMsg = "";
		kakaoMsg = String.format(rmMsgFrame,sndMsg,DateUtils.nowDate("h","t"));
		
		log.info(rmMsgFrame);
		log.info(rmMsg);
		log.info(RM_SEND_YN);
		
		String send_Msg = String.format(rmMsg, kakaoMsg);
		
		if(!"YES".equals(RM_SEND_YN)) {
			log.error("메시지: {} 카카오 알림톡 발송 안함",send_Msg);
			return;
		}
		
		log.info(send_Msg);
		
		try {
		  String rtnSend=null;
		  
		  if(rmTel!=null) {		  
			  String[] arrTel = rmTel.split(","); 
			  String sendMsg = "";
			  for(int i=0;i<arrTel.length;i++) {
			      if(!"".equals(arrTel[i]) && arrTel[i].length()>=13) {  
			    	  sendMsg = send_Msg.replace("[TEL]", arrTel[i].subSequence(0, 13));
			    	  log.info(sendMsg);
				  
			    	  rtnSend = getConnectionKakao(apiURL, rmToken,rmCompanySeq,sendMsg);			  
			    	  log.info("요청결과:"+rtnSend);
			      }else {
			    	  log.error("송신불가:"+arrTel[i]);
			      }
			  }//
		  }else {
			  log.error("송신불가: 전화번호 없슴");
		  }//
		  
		}catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex.toString());
		}
	}
	
	
	/**
	 * 
	 * @param apiUrl
	 * @param arrayObj
	 * @return
	 * @throws Exception
	 */
	private String getConnectionKakao(String apiUrl, String authToken,String companySeq,String arrayObj) throws Exception{
		
		URL url 			  = new URL(apiUrl); 	// 요청을 보낸 URL
		String sendData 	  = arrayObj;
		HttpURLConnection con = null;
		StringBuffer buf 	  = new StringBuffer();
		String returnStr 	  = "";
		
		try {
			con = (HttpURLConnection)url.openConnection();
			
			con.setConnectTimeout(10000);		//서버통신 timeout 설정. 페이코 권장 30초
			con.setReadTimeout(10000);			//스트림읽기 timeout 설정. 페이코 권장 30초
			
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.setRequestProperty("token", authToken);
			con.setRequestProperty("company", companySeq);
			
		    con.setDoOutput(true);
		    con.setRequestMethod("POST");
		    con.connect();
		    
		    // 송신할 데이터 전송.
		    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		    
		    dos.write(sendData.getBytes("UTF-8"));
		    dos.flush();
		    dos.close();
		    
		    int resCode = con.getResponseCode();
		    
		    if (resCode == HttpURLConnection.HTTP_OK) {
		    
		    	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			   	
				int c;
			    
			    while ((c = br.read()) != -1) {
			    	buf.append((char)c);
			    }
			    
			    returnStr = buf.toString();
			    br.close();
			    
			    log.info("응답:"+ returnStr);
			    
		    } else {
		    	returnStr = "{ \"code\" : 9999, \"message\" : \"Send Error\" }";
		    	log.error("응답:"+ String.valueOf(resCode));
		    }
		    
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error(ex.toString());
		} finally {
		    con.disconnect();
		}		
		return returnStr;
	}
	

}
