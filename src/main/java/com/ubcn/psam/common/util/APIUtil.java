package com.ubcn.psam.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIUtil extends Thread {
	
	private HttpURLConnection getHttpURLConnection(String strUrl, String method, int timeout) {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL(strUrl);

			conn = (HttpURLConnection) url.openConnection(); //HttpURLConnection 객체 생성
			conn.setRequestMethod(method); //Method 방식 설정. GET/POST/DELETE/PUT/HEAD/OPTIONS/TRACE
			conn.setConnectTimeout(timeout); //연결제한 시간 설정. 5초 간 연결시도
			conn.setRequestProperty("Content-Type", "application/json");
            
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}		
		return conn;		
	}
	
	private HttpURLConnection getHttpURLConnection_TOKEN(String strUrl, String method, String apitoken, String apiComp, int timeout) {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL(strUrl);

			conn = (HttpURLConnection) url.openConnection(); //HttpURLConnection 객체 생성
			conn.setRequestMethod(method); //Method 방식 설정. GET/POST/DELETE/PUT/HEAD/OPTIONS/TRACE
			conn.setConnectTimeout(timeout); //연결제한 시간 설정. 5초 간 연결시도
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("token", apitoken);
			conn.setRequestProperty("company", apiComp);
			
            
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}		
		return conn;		
	}
	
	
	private StringBuilder readResopnseData(InputStream in) {
		if(in == null ) return null;

		StringBuilder sb = new StringBuilder();
		String line = "";
		
		try (InputStreamReader ir = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(ir)){
			while( (line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb;
	}

	
	private String getHttpRespons(HttpURLConnection conn) {
		StringBuilder sb = null;
		String retData = "";
		try {
			if(conn.getResponseCode() == 200) {
            // 정상적으로 데이터를 받았을 경우
            	//데이터 가져오기
				sb = readResopnseData(conn.getInputStream());
				retData = sb.toString();
			}else{
            // 정상적으로 데이터를 받지 못했을 경우
            
            	//오류코드, 오류 메시지 표출
				log.info("응답코드: {}",conn.getResponseCode());
				log.debug("응답메시지:{}",conn.getResponseMessage());
				//오류정보 가져오기
				sb = readResopnseData(conn.getErrorStream());
				retData = sb.toString();				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			conn.disconnect(); //연결 해제
		};
		
		return retData;
	}
	
	public String HttpConnectionGET(String apiURL, String apiTimeout) {
		String retStr = "";
		HttpURLConnection conn = null;
		conn = this.getHttpURLConnection(apiURL, "GET", Integer.parseInt(apiTimeout));
		retStr = this.getHttpRespons(conn);
		
		return retStr;
	}
	
	
	public String HttpConnectionGET_TOKEN(String apiURL, String apiMethod, String apiToken, String apiComp,String apiTimeout) {
		String retStr = "";
		HttpURLConnection conn = null;
		conn = this.getHttpURLConnection_TOKEN(apiURL, apiMethod, apiToken, apiComp, Integer.parseInt(apiTimeout));
		retStr = this.getHttpRespons(conn);
		
		return retStr;
	}
	
	/**
	 * 
	 * @param apiUrl
	 * @param authToken
	 * @param companySeq
	 * @param msg
	 * @return
	 * @throws Exception
	 */	
	public String HttpConnectionPOST(String apiUrl,String apiParam, String apiTimeOut) throws Exception {
		
		URL url = new URL(apiUrl);
		HttpURLConnection con = null;
		StringBuffer buf = new StringBuffer();
		String retStr = "";
		
		try {
			con = (HttpURLConnection)url.openConnection();
			
			con.setConnectTimeout(Integer.parseInt(apiTimeOut));
			con.setReadTimeout(Integer.parseInt(apiTimeOut));
			
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			//con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//con.setRequestProperty("token",authToken);
			//con.setRequestProperty("company", companySeq);
			
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.connect();
			
			//송신데이타 전송 
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.write(apiParam.getBytes("UTF-8"));
			dos.flush();
			dos.close();
			
			int resCode = con.getResponseCode();
			
			log.info("응답:{}",resCode);

			if( resCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				
				int c;
				
				while( (c=br.read()) != -1      ) {
					buf.append((char)c);
				}
				
				
				retStr = buf.toString();
				br.close();
				
				log.info("응답:"+retStr);
				
			}else {
				retStr = "{ \"code\" : 9999, \"message\" : \"Send Error\" }";
		    	//log.error("응답:"+ String.valueOf(resCode));
			}//
			
		}catch(MalformedURLException e) {
			e.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}finally {
			
		}
		
		return retStr;
	}
	
	/**
	 * -- 오동작 사용안함
	 * @param apiUrl
	 * @param apiToken
	 * @param apiComp
	 * @param apiParam
	 * @param apiTimeOut
	 * @return
	 * @throws Exception
	 */
	public String HttpConnectionAPI(String apiUrl,String apiMethod,String apiToken, String apiComp,String apiTimeOut,String apiParam) throws Exception {
		
		URL url = new URL(apiUrl);
		HttpURLConnection con = null;
		StringBuffer buf = new StringBuffer();
		String retStr = "";
		
		try {
			con = (HttpURLConnection)url.openConnection();
			
			con.setConnectTimeout(Integer.parseInt(apiTimeOut));
			con.setReadTimeout(Integer.parseInt(apiTimeOut));
			
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			//con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("token",apiToken);
			con.setRequestProperty("company", apiComp);
			
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.connect();
			
			//송신데이타 전송 
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			//dos.write(apiParam.getBytes("UTF-8"));
			dos.flush();
			dos.close();
			
			int resCode = con.getResponseCode();
			
			log.info("응답:{}",resCode);

			if( resCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				
				int c;
				
				while( (c=br.read()) != -1      ) {
					buf.append((char)c);
				}
				
				
				retStr = buf.toString();
				br.close();
				
				log.info("응답:"+retStr);
				
			}else {
				retStr = "{ \"code\" : 9999, \"message\" : \"Send Error\" }";
		    	//log.error("응답:"+ String.valueOf(resCode));
			}//
			
		}catch(MalformedURLException e) {
			e.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}finally {
			
		}
		
		return retStr;
	}
	
	
	
	
	public static void main(String[] args) {
		
		/*
		APIUtil apiUtil = new APIUtil();
		
		//http://receipt.ubcn.co.kr/kakao/?trandate=20240408&transeqno=2000070412&terminalid=10038&phone=010-3114-7865
		String apiUrl="http://api.ubcn.co.kr/pay/servicecoin/tid/2000070611";
		String apiMethod="GET";
		String apiToken="KJK4X3RRppSuk25XciTr/DjSBI0deoB/dxT/4qi75NCWSnv1XmfS6ZPXjHOrn1DyOjj3Ig==";
		String apiComp="3";
		String apiTimeOut= "10000";
		String apiParam="";
		
		
		String retStr="";
		try {
			//retStr = apiUtil.HttpConnectionGET("http://receipt.ubcn.co.kr/kakao/?trandate=20240408&transeqno=2000070412&terminalid=10038&phone=010-3114-7865","","10000");
			
			//retStr =apiUtil.HttpConnectionGET("http://receipt.ubcn.co.kr/kakao/?trandate=20240408&transeqno=2000070412&terminalid=10038&phone=010-3114-7865","10000");
			
			//retStr = apiUtil.HttpConnectionGET(apiUrl,"10000");
			
			retStr = apiUtil.HttpConnectionGET_TOKEN(apiUrl,"GET",apiToken, apiComp,apiTimeOut);
			System.out.println("조회"+retStr);
			
			retStr = apiUtil.HttpConnectionGET_TOKEN(apiUrl,"DELETE",apiToken, apiComp,apiTimeOut);
			System.out.println("응답:"+retStr);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		//System.out.println(retStr);
		*/
	
	
	}
	

}
