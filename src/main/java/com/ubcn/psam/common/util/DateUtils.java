package com.ubcn.psam.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;


public class DateUtils {

	public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DATETIME_FORMAT_HYPEN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyyMMdd";
	public static final String TIME_FORMAT = "HHmmss";
	public static final SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);
	public static final SimpleDateFormat format2 = new SimpleDateFormat(TIME_FORMAT);
	public static final SimpleDateFormat format3 = new SimpleDateFormat(DATETIME_FORMAT);

	private static SimpleDateFormat getDateFormat(String format) {
		SimpleDateFormat dt = new SimpleDateFormat(format);
		return dt;
	}

	public static Date parse(String format, String text) throws ParseException {
		return getDateFormat(format).parse(text);
	}

	public static Date parseSimpleDate(String text) throws ParseException {
		return parse("yyyyMMdd", text);
	}

	public static Date parseSimpleTime(String text) throws ParseException {
		return parse("HHmmss", text);
	}

	public static Date parseSimpleDatetime(String text) throws ParseException {
		return parse("yyyyMMddHHmmss", text);
	}

	public static String format(String format, Date date) {
		return getDateFormat(format).format(date);
	}

	public static String formatSimpleDate(Date date) {
		return format1.format(date);
	}

	public static String formatSimpleTime(Date date) {
		return format2.format(date);
	}

	public static String formatSimpleDatetime(Date date) {
		return format3.format(date);
	}
	
	public static String formatSimpleDatetimeHypen(Date date) {
		return format(DATETIME_FORMAT_HYPEN, date);
	}

	/**
	 *
	 * 날짜형태의 String의 날짜 포맷 및 TimeZone을 변경해 주는 메서드
	 *
	 *
	 *
	 * @param strSource      바꿀 날짜 String
	 *
	 * @param fromDateFormat 기존의 날짜 형태
	 *
	 * @param toDateFormat   원하는 날짜 형태
	 *
	 * @param strTimeZone    변경할 TimeZone(""이면 변경 안함)
	 *
	 * @return 소스 String의 날짜 포맷을 변경한 String
	 *
	 */

	public static String convertDate(String strSource, String fromDateFormat, String toDateFormat, String strTimeZone) {

		SimpleDateFormat simpledateformat = null;

		Date date = null;

		String _fromDateFormat = "";

		String _toDateFormat = "";

		if (StringUtils.isNullToString(strSource).trim().equals("")) {
			return "";
		}

		if (StringUtils.isNullToString(fromDateFormat).trim().equals(""))
			_fromDateFormat = "yyyyMMddHHmmss"; // default값

		if (StringUtils.isNullToString(toDateFormat).trim().equals(""))
			_toDateFormat = "yyyy-MM-dd HH:mm:ss"; // default값

		try {

			simpledateformat = new SimpleDateFormat(_fromDateFormat, Locale.getDefault());

			date = simpledateformat.parse(strSource);

			if (!StringUtils.isNullToString(strTimeZone).trim().equals("")) {

				simpledateformat.setTimeZone(TimeZone.getTimeZone(strTimeZone));

			}

			simpledateformat = new SimpleDateFormat(_toDateFormat, Locale.getDefault());

		} catch (ParseException exception) {

		}

		if (simpledateformat != null && simpledateformat.format(date) != null) {

			return simpledateformat.format(date);

		} else {
			return "";
		}
	}

	/**
	 *
	 * 현재(한국기준) 날짜정보를 얻는다. <BR>
	 *
	 * 표기법은 yyyy-mm-dd <BR>
	 *
	 * @return String yyyymmdd형태의 현재 한국시간. <BR>
	 *
	 */

	public static String getToday() {
		String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return nowDate;
	}

	public static String getYesterDate() {
		LocalDateTime ofDateTime = LocalDateTime.now();
		LocalDateTime yesterdate = ofDateTime.plusDays(-1) ;
		return yesterdate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

	}

	public static String getTime() {
		return  LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

	}


	public static String getWeekOfMonth () {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("W"));
	}

	/**
	 *
	 * 입력시간의 유효 여부를 확인
	 *
	 * @param sTime 입력시간
	 *
	 * @return 유효 여부
	 *
	 */

	public static boolean validTime(String sTime) {

		String timeStr = validChkTime(sTime);

		Calendar cal;

		boolean ret = false;

		cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));

		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));

		String HH = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));

		String MM = String.valueOf(cal.get(Calendar.MINUTE));

		String pad2Str = "00";

		String retHH = (pad2Str + HH).substring(HH.length());

		String retMM = (pad2Str + MM).substring(MM.length());
		String retTime = retHH + retMM;

		if (sTime.equals(retTime)) {

			ret = true;

		}

		return ret;

	}

	public static String convertDate(String sDate, String sTime, String sFormatStr) {

		String dateStr = validChkDate(sDate);

		String timeStr = validChkTime(sTime);

		Calendar cal = null;

		cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr, Locale.ENGLISH);

		return sdf.format(cal.getTime());

	}

	/**
	 *
	 * 입력된 일자 문자열을 확인하고 8자리로 리턴
	 *
	 * @param sDate
	 *
	 * @return
	 *
	 */

	public static String validChkDate(String dateStr) {

		String _dateStr = dateStr;

		if (dateStr == null || !(dateStr.trim().length() == 8 || dateStr.trim().length() == 10)) {

			throw new IllegalArgumentException("Invalid date format: " + dateStr);

		}

		if (dateStr.length() == 10) {

			_dateStr = StringUtils.removeMinusChar(dateStr);

		}

		return _dateStr;

	}

	/**
	 *
	 * 입력된 일자 문자열을 확인하고 8자리로 리턴
	 *
	 * @param sDate
	 *
	 * @return
	 *
	 */

	public static String validChkTime(String timeStr) {

		String _timeStr = timeStr;

		if (_timeStr.length() == 5) {

			_timeStr = StringUtils.remove(_timeStr, ':');

		}

		if (_timeStr == null || !(_timeStr.trim().length() == 4)) {

			throw new IllegalArgumentException("Invalid time format: " + _timeStr);

		}

		return _timeStr;

	}

	/**
	 *
	 * 입력일자의 유효 여부를 확인
	 *
	 * @param sDate 일자
	 *
	 * @return 유효 여부
	 *
	 */

	public static boolean validDate(String sDate) {

		String dateStr = validChkDate(sDate);

		Calendar cal;

		boolean ret = false;

		cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));

		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);

		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));

		String year = String.valueOf(cal.get(Calendar.YEAR));

		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);

		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		String pad4Str = "0000";

		String pad2Str = "00";

		String retYear = (pad4Str + year).substring(year.length());

		String retMonth = (pad2Str + month).substring(month.length());

		String retDay = (pad2Str + day).substring(day.length());

		String retYMD = retYear + retMonth + retDay;

		if (sDate.equals(retYMD)) {

			ret = true;

		}

		return ret;

	}

	/**
	 *
	 * 입력일자, 요일의 유효 여부를 확인
	 *
	 * @param sDate 일자
	 *
	 * @param sWeek 요일 (DAY_OF_WEEK)
	 *
	 * @return 유효 여부
	 *
	 */

	public static boolean validDate(String sDate, int sWeek) {

		String dateStr = validChkDate(sDate);

		Calendar cal;

		boolean ret = false;
		cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));

		int Week = cal.get(Calendar.DAY_OF_WEEK);

		if (validDate(sDate)) {
			if (sWeek == Week) {
				ret = true;
			}

		}

		return ret;

	}


    public static int DateDiffMin(String stDate,String endDate) {
    	int diffMin = 0;
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	Date d1 = null;
    	Date d2 = null;
    	
    	try {
    		d1 = format.parse(stDate);
    		d2 = format.parse(endDate);
    	}catch(ParseException ex) {
    		ex.printStackTrace();
    		
    		return diffMin;
    	}
    	
    	long diff = d2.getTime() -d1.getTime();
    	long diff_Min = diff / (60 * 1000);
    	
    	diffMin = Long.valueOf(Optional.ofNullable(diff_Min).orElse(0L)).intValue();
    	
    	return diffMin;   	
    }
    
    public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
    
    /**
	   * 
	   * @param code  "h" - hypen
	   * @param type  "t" - time format
	   * @return
	   */
	 public static String nowDate(String code,String type) {
	    Date date = new Date();
	    String nowDate = "";
	    if("h".equals(code)) {
	       if("t".equals(type)) {	
	    	   nowDate = formatSimpleDatetimeHypen(date);
	       }else {
	    	   nowDate = formatSimpleDatetime(date);
	       }//
	    }else {
	       if("t".equals(type)) {
	    	   nowDate = formatSimpleDatetime(date);
	       }else {
	    	   nowDate = formatSimpleDate(date);   
	       }//
	    }		
		return nowDate;
	}
    
    
	public static void main(String[] args) {
		
		//int diff = DateUtils.DateDiffMin("20220624155958","20220624165550"); 
		
		//System.out.println(diff);
		
		
		
		String i = Integer.toHexString(396);
		
		System.out.println(i);
		
		byte[] ByteArray = hexStringToByteArray("018C");
		
		
		System.out.println(ByteArray.length);
		
		
		//sb.append("\01"); //테스트용
		 
		// sb.append("\0214"); //테스트용	
		
		
	}



}
