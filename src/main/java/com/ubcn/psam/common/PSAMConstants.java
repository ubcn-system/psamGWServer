package com.ubcn.psam.common;

public class PSAMConstants {
	
	/** 거래 구분 코드 상수
	 *		21: PSAM 요청
	 */
	public static final String TRANS_TYPE_PSAM			= "21";

	/** 전문 구분 코드 상수
	 *		01: 인증
	 *		02: 구매
	 *		03: 상태수집
	 *		04: SAM서버 RESTART
	 *		05: SAM RESET
	 */
	public static final String MESSAGE_TYPE_AUTH		= "01";
	public static final String MESSAGE_TYPE_PURCHASE	= "02";
	public static final String MESSAGE_TYPE_STATUS		= "03";
	public static final String MESSAGE_TYPE_RESTART		= "04";
	public static final String MESSAGE_TYPE_RESET		= "05";
	
	public static final int ClientTimoutSecond = 10; //10초  //ISJUNG
	
	public static final String rtn_code_totalerror = "9999";
	public static final String rtn_code_success = "0000"; 

}
