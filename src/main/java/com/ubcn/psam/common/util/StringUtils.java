package com.ubcn.psam.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils {

	public static final String EMPTY_STRING = "";
	public static final String ELLIPSIS = "…";
	public static final char SPACE = ' ';
	public static final Charset EUC_KR = Charset.forName("EUC-KR");

	public static String trim(String source) {
		return source == null ? null : source.trim();
	}

	public static boolean hasLength(String source) {
		return (source != null) && (source.length() > 0);
	}

	public static boolean hasText(String source) {
		if (!hasLength(source)) {
			return false;
		}
		for (int i = 0; i < source.length(); i++) {
			if (!Character.isWhitespace(source.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String repeat(String source, int count) {
		if (!hasLength(source)) {
			return source;
		}
		if (count <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(source.length() * count);
		for (int i = 0; i < count; i++) {
			sb.append(source);
		}
		return sb.toString();
	}

	public static String ellipsize(String source, int byteCount) {
		return ellipsize(source, byteCount, "…", EUC_KR);
	}

	public static String ellipsize(String source, int byteCount, String ellipsis) {
		return ellipsize(source, byteCount, ellipsis, EUC_KR);
	}

	public static String ellipsize(String source, int byteCount, Charset charset) {
		return ellipsize(source, byteCount, "…", charset);
	}

	public static String ellipsize(String source, int byteCount, String ellipsis, Charset charset) {
		if ((source == null) || (byteCount == 0)) {
			return "";
		}
		if (byteCount < 0) {
			return source;
		}
		int i = 0;
		int length = source.length();
		int bytesForLatin1 = "A".getBytes(charset).length;
		int bytesForKorean = "가".getBytes(charset).length;
		int bytesForEllipsis = ellipsis.toString().getBytes(charset).length;
		while ((i < length) && (byteCount >= 0)) {
			if (Character.getType(source.codePointAt(i)) == 5) {
				byteCount -= bytesForKorean;
			} else {
				byteCount -= bytesForLatin1;
			}
			i++;
		}
		if (byteCount >= 0) {
			return source;
		}
		while ((i > 0) && (byteCount < bytesForEllipsis)) {
			i--;
			if (Character.getType(source.codePointAt(i)) == 5) {
				byteCount += bytesForKorean;
			} else {
				byteCount += bytesForLatin1;
			}
		}
		return source.substring(0, i) + ellipsis;
	}

	public static String adjustByteCount(String source, int byteCount) {
		return adjustByteCount(source, byteCount, ' ', EUC_KR);
	}

	public static String adjustByteCount(String source, int byteCount, char padding) {
		return adjustByteCount(source, byteCount, padding, EUC_KR);
	}

	public static String adjustByteCount(String source, int byteCount, Charset charset) {
		return adjustByteCount(source, byteCount, ' ', charset);
	}

	public static String adjustByteCount(String source, int byteCount, char padding, Charset charset) {
		if (byteCount < 0) {
			return source;
		}
		if (byteCount == 0) {
			return "";
		}
		if (charset == null) {
			charset = EUC_KR;
		}
		if (source == null) {
			source = "";
		}
		int i = 0;
		int length = source.length();
		int bytesForLatin1 = "A".getBytes(charset).length;
		int bytesForKorean = "가".getBytes(charset).length;

		int bytesForPadding = Character.getType(padding) == 5 ? bytesForKorean : bytesForLatin1;

		while ((i < length) && (byteCount >= 0)) {
			if (Character.getType(source.codePointAt(i)) == 5) {
				byteCount -= bytesForKorean;
			} else {
				byteCount -= bytesForLatin1;
			}
			i++;
		}

		StringBuilder builder = new StringBuilder(source.substring(0, i));
		while (byteCount > 0) {
			builder.append(padding);
			byteCount -= bytesForPadding;
		}
		return builder.toString();
	}

	public static boolean isNumeric(String s) {
		try {
			double value = Double.parseDouble(s);

			if (value >= 0)
				return true;
			else
				return false;

		} catch (NumberFormatException e) {
			return false;
		}
	}

	// 정수 판별 함수
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) | Character.digit(s.charAt(i + 1), 16));

		}
		return data;
	}

	public static void printByteArray(byte[] bytes) {
		for (byte b1 : bytes) {
			String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
			s1 += " " + Integer.toHexString(b1);
			s1 += " " + b1;
			log.info(s1);
		}
	}

	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (byte b : bytes) {

			sb.append(String.format("%02X", b & 0xff));
		}

		return sb.toString();
	}

	public static String ByteArrayToHexString(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
			hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
			hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
		}
		return new String(hexChars);
	}

	public static String hexToAscii(String hexStr) {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < hexStr.length(); i += 2) {
			String str = hexStr.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}

	/**
	 * 바이너리 바이트 배열을 스트링으로 변환
	 *
	 * @param b
	 * @return
	 */
	public static String byteArrayToBinaryString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; ++i) {
			sb.append(byteToBinaryString(b[i]));
		}
		return sb.toString();
	}

	/**
	 * 바이너리 바이트를 스트링으로 변환
	 *
	 * @param n
	 * @return
	 */
	public static String byteToBinaryString(byte n) {
		StringBuilder sb = new StringBuilder("00000000");
		for (int bit = 0; bit < 8; bit++) {
			if (((n >> bit) & 1) > 0) {
				sb.setCharAt(7 - bit, '1');
			}
		}
		return sb.toString();
	}

	/**
	 * 바이너리 스트링을 바이트배열로 변환
	 *
	 * @param s
	 * @return
	 */
	public static byte[] binaryStringToByteArray(String s) {
		int count = s.length() / 8;
		byte[] b = new byte[count];
		for (int i = 1; i < count; ++i) {
			String t = s.substring((i - 1) * 8, i * 8);
			b[i - 1] = binaryStringToByte(t);
		}
		return b;
	}

	/**
	 * 바이너리 스트링을 바이트로 변환
	 *
	 * @param s
	 * @return
	 */
	public static byte binaryStringToByte(String s) {
		byte ret = 0, total = 0;
		for (int i = 0; i < 8; ++i) {
			ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
			total = (byte) (ret | total);
		}
		return total;
	}
	
	/**
	 * ByteBuf to ByteArr to Hex
	 * @param out
	 */
	public static void byteBufToByteArrHex(ByteBuf out) { 
		int i = 0;
		ByteBuf buf = out;
		int length = buf.readableBytes();
		byte[] arr = new byte[length];
		for(i=0; i<length ; i++) {
			arr[i] = buf.getByte(i);
		}//
		String hexData = StringUtils.byteArrayToHexString(arr);
		
		log.info("전문로그:{}",hexData);
		
		//netty 데이타 처럼 변환
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("        |  0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F");
		sb.append("\n");
		sb.append("--------+---------------------------------------------------");
		int j=0;
		for(i=0; i< hexData.length()/2 ;i++) {
			if(i==0) {
				sb.append("\n");
			}
			
			if(i%16 == 0) {
				sb.append(String.format("%08d| ", i));
			}//
			
			sb.append(hexData.substring(i*2, (2*i)+2));
			sb.append(" ");
			
			if( ((2*i)+2)%32==0 && i< (hexData.length()/2-1) ) {
				//log.info("{}",i);
				sb.append(" |\n");
			}
		}//
		sb.append(" \n");
		sb.append("--------+---------------------------------------------------");
		log.info(sb.toString());
		
	}//
	

	/**
	 * 입력문자열의 특정 문자를 다른 문자로 바꾸어주는 메소드이다.
	 *
	 * @param pm_sString 수정할 문자열
	 * @param pm_sOld    찾을 문자열
	 * @param pm_sNew    바꿀문자열
	 */
	public static String replace(String pm_sString, String pm_sOld, String pm_sNew) {

		if (pm_sString == null || pm_sString.isEmpty() || pm_sOld == null || pm_sOld.isEmpty()) {
			return pm_sString;
		}

		int lm_iIndex = pm_sString.indexOf(pm_sOld);
		StringBuffer lm_sbBuffer = new StringBuffer();

		if (lm_iIndex == -1)
			return pm_sString;

		lm_sbBuffer.append(pm_sString.substring(0, lm_iIndex) + pm_sNew);

		if (lm_iIndex + pm_sOld.length() < pm_sString.length())
			lm_sbBuffer.append(
					replace(pm_sString.substring(lm_iIndex + pm_sOld.length(), pm_sString.length()), pm_sOld, pm_sNew));

		return lm_sbBuffer.toString();
	}

	/**
	 * pm_sString 문자열의 총길이를 pm_iLength 로 왼쪽부터 pm_char 로 채워서 반환한다.
	 *
	 * @param pm_sString 문자열
	 * @param pm_char    왼쪽에 채울 문자
	 * @param pm_iLength 문자열의 총길이
	 * @return 문자열
	 */
	public static String lpad(String pm_sString, char pm_char, int pm_iLength) {
		if (pm_sString == null)
			return null;
		if (pm_sString.length() >= pm_iLength)
			return pm_sString;

		int lm_iCount = pm_iLength - pm_sString.length();
		StringBuffer lm_oBuffer = new StringBuffer();
		for (int i = 0; i < lm_iCount; i++) {
			lm_oBuffer.append(pm_char);
		}
		lm_oBuffer.append(pm_sString);
		return lm_oBuffer.toString();
	}

	/**
	 * pm_sString 문자열의 총길이를 pm_iLength 로 오른쪽부터 pm_char 로 채워서 반환한다.
	 *
	 * @param pm_sString 문자열
	 * @param pm_char    왼쪽에 채울 문자
	 * @param pm_iLength 문자열의 총길이
	 * @return 문자열
	 */
	public static String rpad(String pm_sString, char pm_char, int pm_iLength) {
		if (pm_sString == null)
			return null;
		if (pm_sString.length() >= pm_iLength)
			return pm_sString;

		int lm_iCount = pm_iLength - pm_sString.length();
		StringBuffer lm_oBuffer = new StringBuffer();
		for (int i = 0; i < lm_iCount; i++) {
			lm_oBuffer.append(pm_char);
		}
		return pm_sString + lm_oBuffer.toString();
	}
	
	//byte lpad
	public static String rpadB(String pm_sString, char pm_char, int pm_iLength) {
		if (pm_sString == null)
			return null;
		if (pm_sString.length() >= pm_iLength)
			return pm_sString;

		String rtnData = "";
		
		try {
			int lm_iCount = pm_iLength - pm_sString.getBytes("EUC-KR").length;
			StringBuffer lm_oBuffer = new StringBuffer();
			for (int i = 0; i < lm_iCount; i++) {
				lm_oBuffer.append(pm_char);
			}
			rtnData = pm_sString + lm_oBuffer.toString();
		}catch(UnsupportedEncodingException ex) {
			log.error(StringUtils.getPrintStackTrace(ex));
			
		}
		return rtnData;
	}
	

	/** 문자열 객체가 <code>null</code>인 경우 ""(빈 문자열)을 반환하는 함수. */
	public static String replaceNull(String str) {
		return (str == null) ? "" : str;
	}

	public static String replaceNull(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}

	public static String replaceNumberNull(String str) {
		return (str == null) ? "0" : str;
	}

	public static String remove(String str, char remove) {

		if (isEmpty(str) || str.indexOf(remove) == -1) {

			return str;

		}

		char[] chars = str.toCharArray();

		int pos = 0;

		for (int i = 0; i < chars.length; i++) {

			if (chars[i] != remove) {

				chars[pos++] = chars[i];

			}

		}

		return new String(chars, 0, pos);

	}

	public static boolean isEmpty(String str) {

		return str == null || str.length() == 0;

	}

	public static String removeCommaChar(String str) {

		return remove(str, ',');

	}

	public static String isNullToString(Object object) {

		String string = "";
		if (object != null) {
			string = object.toString().trim();
		}

		return string;

	}

	public static String removeMinusChar(String str) {

		return remove(str, '-');

	}

	/**
	 * 문자열을 바이트 단위로 substring하기
	 *
	 * new String(str.getBytes(), 0, endBytes) 코드를 사용하면 한글 바이트에 딱 맞춰 자르지 않는 경우 깨지는
	 * 문제가 있어서 따로 메서드를 만들었다.
	 *
	 * UTF-8 기준 한글은 3바이트, 알파벳 대소문자나 숫자 및 띄어쓰기는 1바이트로 계산된다.
	 *
	 * @param str
	 * @param beginBytes
	 * @param endBytes
	 * @return
	 */
	public static String substringByBytes(String str, int beginBytes, int endBytes) {
		if (str == null || str.length() == 0) {
			return "";
		}

		if (beginBytes < 0) {
			beginBytes = 0;
		}

		if (endBytes < 1) {
			return "";
		}

		int len = str.length();

		int beginIndex = -1;
		int endIndex = 0;
		int hangleByte = 2;
		int curBytes = 0;
		String ch = null;
		for (int i = 0; i < len; i++) {
			ch = str.substring(i, i + 1);
			curBytes += ch.getBytes().length >= 2 ? hangleByte : 1;

			if (beginIndex == -1 && curBytes >= beginBytes) {
				beginIndex = i;
			}

			if (curBytes > endBytes) {
				break;
			} else {
				endIndex = i + 1;
			}
		}

		return str.substring(beginIndex, endIndex);
	}

	public static boolean checkValidNumeral(String chkVal) {
		char ch;
		for (int i = 0; i < chkVal.length(); i++) {
			ch = chkVal.charAt(i);
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 문장에 숫자가 포함 되어 있는지 체크
	 * @param chkVal
	 * @return
	 */
	public static boolean checkValidNumeral2(String chkVal) {
		
		Pattern p = Pattern.compile("([0-9])");
		Matcher m = p.matcher(chkVal);
		
		return m.find();
	}
	
	/**
	 * exception to String
	 * @param e
	 * @return
	 */
	public static String getPrintStackTrace(Exception e) {
        
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
         
        return errors.toString();         
    }
	

	/**
	 * 전화번호 분리
	 * @param telNo
	 * @param delim
	 * @return
	 */
	public static String rtnSMSTel(String telNo,String delim) {
		String phoneNo = telNo.replaceAll("[^0-9]", "");
		String rtnTelNo = "";
		if(phoneNo.length()==10) {   
			//010 000 0000
			rtnTelNo=phoneNo.substring(0, 3)+delim+phoneNo.substring(3, 6)+delim+phoneNo.substring(6, 10);
		}else if(phoneNo.length()==11) {
			rtnTelNo=phoneNo.substring(0, 3)+delim+phoneNo.substring(3, 7)+delim+phoneNo.substring(7, 11);
		}
		
		return rtnTelNo;
	}
	
	 /*
	  * Spring 암호화 //properties 에서는 ENC(인코딩데이타) 로 사용
	  * @param data
	  */
	 public static void createEncData(String data) {
		 StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		 encryptor.setAlgorithm("PBEWITHMD5ANDDES");  
		 encryptor.setPassword("!ubcn7880@");  
		 String encryptedPass = encryptor.encrypt(data);
		 String decryptedPass = encryptor.decrypt(encryptedPass);
		 System.out.println("Encrypted Password for admin is : "+encryptedPass);  
		 System.out.println("Decrypted Password for admin is : "+decryptedPass);
	 }
	 
	 public static String createEncDataVal(String data) {
		 StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
		 encryptor.setAlgorithm("PBEWITHMD5ANDDES");  
		 encryptor.setPassword("!ubcn7880@");  
		 String encryptedPass = encryptor.encrypt(data);
		 //String decryptedPass = encryptor.decrypt(encryptedPass);
		 
		 return encryptedPass;		 
	 }
	 
	 public static  String emvData(String emvdata) {
	 		StringBuilder sb = new StringBuilder();
			
			sb.append(emvdata.substring(0, 1)); //EMV거래구분	1
			sb.append(",");
			sb.append(emvdata.substring(1, 1+4)); //POS Entry Mode Code	4
			sb.append(",");
			sb.append(emvdata.substring(5, 5+4)); //Card Sequence Number	4
			sb.append(",");
			sb.append(emvdata.substring(9, 9+12)); //Additional POS information	12
			sb.append(",");
			sb.append(emvdata.substring(21, 21+46)); //Issuer Script Results	46
			sb.append(",");
			sb.append(emvdata.substring(67, 67+22)); //Application Cryptogram (AC)	22
			sb.append(",");
			sb.append(emvdata.substring(89, 89+8)); //Cryptogram InformationData	8
			sb.append(",");
			sb.append(emvdata.substring(97, 97+70)); //Issuer Application Data  (IAD)	70
			sb.append(",");
			sb.append(emvdata.substring(167, 167+14)); //Unpredictable Number	14
			sb.append(",");
			sb.append(emvdata.substring(181, 181+10)); //Application Transaction Counter (ATC)	10
			sb.append(",");
			sb.append(emvdata.substring(191, 191+14)); //Terminal Verification Result (TVR)	14
			sb.append(",");
			sb.append(emvdata.substring(205, 205+10)); //Transaction Date	10
			sb.append(",");
			sb.append(emvdata.substring(215, 215+6)); //Transaction Type	6
			sb.append(",");
			sb.append(emvdata.substring(221, 221+18)); //Transaction Amount	18
			sb.append(",");
			sb.append(emvdata.substring(239, 239+10)); //Transaction Currency Code	10
			sb.append(",");
			sb.append(emvdata.substring(249, 249+8)); //Application Interchange Profile(AIP)	8
			sb.append(",");
			sb.append(emvdata.substring(257, 257+10)); //Terminal Country Code	10	437
			sb.append(",");
			sb.append(emvdata.substring(267, 267+18));  //Amount Other	18
			sb.append(",");
			sb.append(emvdata.substring(285, 285+12));  //Cardholder Verification Method(CVM) Results	12
			sb.append(",");
			sb.append(emvdata.substring(297, 297+12)); //Terminal Capabilities	12
			sb.append(",");
			sb.append(emvdata.substring(309, 309+8));  //Terminal Type	8
			sb.append(",");
			sb.append(emvdata.substring(317, 317+22));  //Interface Device(IFD) Serial Number	22
			sb.append(",");
			sb.append(emvdata.substring(339, 339+8));   //Transaction Category Code	8
			sb.append(",");
			sb.append(emvdata.substring(347, 347+36));  //Dedicated File Name	36
			sb.append(",");			
			sb.append(emvdata.substring(383, 383+10)); //Terminal ApplicationVersion Number	10
			sb.append(",");
			sb.append(emvdata.substring(393, 393+14));  //Transaction Sequence Counter	14 
			sb.append(",");
			
			if(emvdata.length()==423 ) {
				sb.append(emvdata.substring(407, 407+14)); //V_PaywaveFFIValu	14
				sb.append(",");		
				sb.append(emvdata.substring(421, 421+2));    //Fallback 사유	2
			}else if(emvdata.length()==409) {
				sb.append(emvdata.substring(407, 407+2));    //Fallback 사유	2
			}
			
			return sb.toString();
	 }
	 
	 /**
	  * 
	  * @param temp
	  * @param snum
	  */			 
	 public static void makeFile(byte[] temp, String snum) {
	    
	   try {
			String fileName=String.format("D:/test/sign%s.bmp",snum); 
			//byte 파일 생성
			File outFile = new File(fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(outFile);
			fileOutputStream.write(temp);
			fileOutputStream.close();
		 }catch(IOException ex) {
			 ex.printStackTrace();
		 }		 
	 }
	 
	 /**
	 * 
	 * @param fileData
	 * @param filePath
	 */
	 public static void makeImgFile(byte[] fileData, String filePath)  {
	
		try {
			//byte 파일 생성
			File outFile = new File(filePath);
			FileOutputStream fileOutputStream = new FileOutputStream(outFile);
			fileOutputStream.write(fileData);
			fileOutputStream.close();
		}catch(IOException ex) {
			log.error(getPrintStackTrace(ex));
		}catch(Exception ex) {
			log.error(getPrintStackTrace(ex));
		}
		
	 }
	 
	 public static void main(String[] args) {
		 
	    /*
		spring.oracle.datasource.jdbcUrl=jdbc:log4jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.100.111)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.100.112)(PORT=1521))(FAILOVER=on)(LOAD_BALANCE=off))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=vmmdb.vmms.ubcn.co.kr)))
		#spring.oracle.datasource.jdbcUrl=jdbc:log4jdbc:oracle:thin:@192.168.100.104:1521/vmmdb4.vmms.ubcn.co.kr
		spring.oracle.datasource.username=vanon
		spring.oracle.datasource.password=vanon338
		
		createEncData("jdbc:log4jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.100.111)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.100.112)(PORT=1521))(FAILOVER=on)(LOAD_BALANCE=off))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=vmmdb.vmms.ubcn.co.kr)))");//
		createEncData("jdbc:log4jdbc:oracle:thin:@192.168.100.104:1521/vmmdb4.vmms.ubcn.co.kr");//
		createEncData("vanon");//
		createEncData("vanon338");//
		
		spring.datasource.jdbcUrl=jdbc:log4jdbc:mariadb:sequential//172.29.100.131,172.29.100.132,172.29.100.133/vanon?serverTimezone=UTC
		#spring.datasource.jdbcUrl=jdbc:log4jdbc:mariadb://172.29.100.112:3306/vanon?serverTimezone=UTC
		spring.datasource.username=vanon
		spring.datasource.password=ubcn0504
		
		createEncData("jdbc:log4jdbc:mariadb:sequential//172.29.100.131,172.29.100.132,172.29.100.133/vanon?serverTimezone=UTC");//
		createEncData("jdbc:log4jdbc:mariadb://172.29.100.112:3306/vanon?serverTimezone=UTC");//
		createEncData("vanon");//
		createEncData("ubcn0504");//
		*/
		/*
		createEncData("jdbc:log4jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.100.111)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.100.112)(PORT=1521))(FAILOVER=on)(LOAD_BALANCE=off))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=vmmdb.vmms.ubcn.co.kr)))");//
		createEncData("jdbc:log4jdbc:oracle:thin:@192.168.100.104:1521/vmmdb4.vmms.ubcn.co.kr");//
		createEncData("vanon");//
		createEncData("vanon338");//
		
		createEncData("jdbc:log4jdbc:mariadb:sequential//172.29.100.131,172.29.100.132,172.29.100.133/vanon?serverTimezone=UTC");//
		createEncData("jdbc:log4jdbc:mariadb://172.29.100.112:3306/vanon?serverTimezone=UTC");//
		createEncData("vanon");//
		createEncData("ubcn0504");//
		*/
		/* 
		String cardBinNo = "";
		String cardNo = "409216******0039";
		
		cardBinNo = cardNo.substring(0, 8);
		if(!cardBinNo.matches("[0-9]+")) { 
			cardBinNo = cardNo.substring(0, 6);
		}//		
		System.out.println(cardBinNo);
		
		//cardNo = "43826500****470*";
		cardNo = "043826500****470*";
		
		cardBinNo = cardNo.substring(0, 8);
		
		System.out.println(cardBinNo);
		
		
		if(!cardBinNo.matches("[0-9]+")) { 
			cardBinNo = cardNo.substring(0, 6);
		}//		
		System.out.println(cardBinNo);
		*/
		 
		//System.out.println("/"+String.format("%10s", ' ')+"/");
		 
		//System.out.println(rtnSMSTel("01031147865","-"));  
		//System.out.println(rtnSMSTel("0103117865","-"));
		 
		 //System.out.println(emvData("M051000010505000010009F5B0000000000000000000000000000000000000000009F260879474A09235D0CB59F2701809F10125210A00005220000000000000000000000FF00000000000000000000000000009F370427698EC39F36020117950500000080009A032406309C01009F02060000000010005F2A020410820218009F1A0204109F03060000000000009F34031F03029F3303E0F8C89F3501229F1E0849463030303030309F5301008407A00000000410100000000000000000009F090200029F4104000000019F6E000000000000"));
		 
		 System.out.println(Long.valueOf("-00002400"));
	 }

}