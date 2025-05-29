package com.ubcn.psam.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * jdk1.8.* 날짜 함수
 * @author UBCN
 *
 */
public class DateUtils2 {
	
	public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DATETIME_FORMAT_HYPEN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyyMMdd";
	public static final String TIME_FORMAT = "HHmmss";

	/**
	 * 오늘날짜 구하기
	 * @return
	 */
	public static String getToday() {
		String toDay="";
		//오늘날짜 
		LocalDate now = LocalDate.now();		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);		
		toDay = now.format(formatter);
		
		return toDay;
	}
	
	public static String getToday2(String pattren) {
		String toDay="";
		//오늘날짜 
		LocalDateTime now = LocalDateTime.now();		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattren);		
		toDay = now.format(formatter);
		
		return toDay;
	}
	
	/**
	 * 현재시간 구하기
	 * @return
	 */
	public static String getNowTime() {
		String nowTime="";
		//현재시간 
		LocalTime ntime = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
		
		nowTime = ntime.format(formatter);
		
		return nowTime;
	}
	
	public static String getNowTime2(String pattern) {
		String nowTime="";
		//현재시간 
		LocalDateTime ntime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		
		nowTime = ntime.format(formatter);
		
		return nowTime;
	}
	
	/**
	 * 오늘날짜 구하기
	 * @return
	 */
	public static String getTodayTime() {
		String toDay="";
		//오늘날짜 
		LocalDateTime now = LocalDateTime.now();		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT_HYPEN);		
		toDay = now.format(formatter);
			 
		//LocalTime ntime = LocalTime.now();
		return toDay;
	}
	
	
	/**
	 * 두 시간간의 차 계산
	 * @param stDate
	 * @param endDate
	 * @return
	 */
	public static long DiffSecond(String stDate, String endDate, TimeUnit unit) {
		long rtnSec = 0L;
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Date stTime = null;
			Date endTime = null;
		
			stTime = df.parse(stDate);
			endTime = df.parse(endDate);
			
			long diffMilles = endTime.getTime() - stTime.getTime();
			rtnSec = TimeUnit.SECONDS.convert(diffMilles, unit)/1000;
		
		}catch(ParseException ex) {
			ex.printStackTrace();			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rtnSec;		
	}
	
	
	/**
	 * 입력날짜 + or -  날짜 계산 
	 * @param date
	 * @param daysToAdd
	 * @return
	 */
	public static String getDateAdd(String date, int daysToAdd) {
		LocalDate inDate = LocalDate.parse(date,DateTimeFormatter.ofPattern(DATE_FORMAT));
		LocalDate outDate = inDate.plusDays(daysToAdd);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		
		String rtnDate = "";
		rtnDate = outDate.format(formatter);
		return  rtnDate;
	}
	
	
	/**
	 * 시간차 계산
	 * @param stTime
	 * @param endTime
	 * @return
	 */
	public static long getDiffTime(String stTime, String endTime) {
		SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS",Locale.KOREA);
		long diff = 0L;
		long rtnSec = 0L;
		try {
			Date d1 = df.parse(stTime);
			Date d2 = df.parse(endTime);
			
			diff = d2.getTime() - d1.getTime();			
			rtnSec = diff/1000;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rtnSec;
	}
	
	public static void main(String[] args) {
		/*
		//오늘날짜 
		LocalDate now = LocalDate.now();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		
		String toDay = now.format(formatter);
		
		System.out.println(toDay);
		
		
		//현재시간 
		LocalTime ntime = LocalTime.now();
		formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
		
		String nowTime = ntime.format(formatter);
		
		System.out.println(nowTime);
		*/
		
		//System.out.println( DiffSecond("20221028095401", "20221028095455",TimeUnit.SECONDS));
		
		System.out.println(getDateAdd("20240221", -2));
		
	
		
		System.out.println(getToday2(DateUtils2.DATETIME_FORMAT));
	}
}
