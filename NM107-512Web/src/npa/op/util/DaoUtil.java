package npa.op.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Calendar;

import npa.op.vo.User;
import npa.op.control.UtilServlet;
import npa.op.util.StringUtil;
import static npa.op.util.StringUtil.*;
import npa.op.util.DateUtil;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class DaoUtil {

private static Logger log = Logger.getLogger(UtilServlet.class);   
	
	/**
     * 取得更新時間及人員等相關資訊
     * 
     * @param jObj 需擴充之JSONObject 
     * @param colType 需擴充之欄位類別
     * 
     * @return 已擴充完畢之JSONObject
     */
    public JSONObject getStaticColumn(JSONObject jObj, String colType){
        try{
        	Date current = new Date();
        	User voUser = new User();
        	voUser = (User) jObj.get("userVO");
        	
                jObj.put("OP_" + colType + "_ID", voUser.getUserId());
                jObj.put("OP_" + colType + "_NM", voUser.getUserName());
                jObj.put("OP_" + colType + "_UNIT_CD", voUser.getUnitCd());
                jObj.put("OP_" + colType + "_UNIT_NM", voUser.getUnitName());
                jObj.put("OP_" + colType + "_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current));
            
        } catch(Exception e){
        	log.error(ExceptionUtil.toString(e));
        }
        
        return jObj;
    }
    
    /**
     * 取得更新時間及人員等相關資訊
     * 
     * @param jObj 需擴充之JSONObject 
     * @param colType 需擴充之欄位類別
     * 
     */
    public void getStaticColumnExpUnit(JSONObject jObj){
        try{
        	User voUser = new User();
        	voUser = (User) jObj.get("userVO");
        	
        	jObj.put("CP_AC_UNIT_CD", voUser.getUnitCd1());
			jObj.put("CP_AC_UNIT_NM", voUser.getUnitCd1Name());
			jObj.put("CP_AC_B_UNIT_CD", voUser.getUnitCd2());
			jObj.put("CP_AC_B_UNIT_NM", voUser.getUnitCd2Name());
			jObj.put("CP_AC_UNIT_TEL", voUser.getUnitTel());
            
        } catch(Exception e){
        	log.error(ExceptionUtil.toString(e));
        }
    }

    protected Calendar calendar = Calendar.getInstance();

    public enum DateTimeType {
	Long,
	Short
    }

    public enum DateLocaleType {
	US,
	TW
    }
    
    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyyMMdd(HH:mm)
     * @param date tryParse所能接受的格式
     * @return yyyMMdd(HH:mm)
     */
    public static String getReportDateTime(String date){
	String dateString = "";
	if (nvl(date).length() > 0) {
	    Date d = tryParse(date);
	    int twYear = d.getYear() - 11;
	    // 設定日期格式
	    SimpleDateFormat sdf = new SimpleDateFormat("MMdd(HH:mm)");
	    // 進行轉換
	    dateString = addPrefixZero(twYear+"", 3) + sdf.format(d);
	}
	return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH:mm
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103年04月10日 
     */
    public static String getReportDateTime3(String date){
	String dateString = "";
	if (nvl(date).length() == 8) {
		date = date +"000000";
	}
	if (nvl(date).length() > 0) {
	    Date d = tryParse(date);
	    int twYear = d.getYear() - 11;
	    // 設定日期格式
	    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 ");
	    
	    // 進行轉換
	    dateString = addPrefixZero(twYear+"", 3) + "年" + sdf.format(d);
	}
	return dateString;
    }
    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy/MM/dd 
     * @param date tryParse所能接受的格式
     * @return yyy/MM/dd日 HH:mm 如:103/04/10 
     */
    public static String getReportDateTime4(String date){
	String year="";String mon="";String day="";
		if(nvl(date).length() == 8){
			year=  addPrefixZero(String.valueOf(Integer.parseInt(date.substring(0, 4))-1911),3);
			mon = date.substring(4, 6);
			day = date.substring(6, 8);
			return year+"/"+mon+"/"+day;
		}else if (nvl(date).length() == 7){
			year= date.substring(0, 3);
			mon = date.substring(3, 5);
			day = date.substring(5, 7);
			return year+"/"+mon+"/"+day;
		}else if (nvl(date).length() == 6){
			year= addPrefixZero(date.substring(0, 2),3);
			mon = date.substring(2, 4);
			day = date.substring(4, 6);
			return year+"/"+mon+"/"+day;
		}else {
			return date;
		}
    }
    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH:mm
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103/04/10日 15:53
     */
    public static String getReportDateTime5(String date){
	String dateString = "";
	if (nvl(date).length() > 0) {
	    Date d = tryParse(date);
	    int twYear = d.getYear() - 11;
	    // 設定日期格式
	    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
	    // 進行轉換
	    dateString = addPrefixZero(twYear+"", 3) + "年" + sdf.format(d);
	}
	return dateString;
    }
    public static String getReportDateTime6(String date){
    	String dateString = "";
    	String year="";String mon="";String day="";
    		if(nvl(date).length() == 8){
    			year= date.substring(0, 4);
    			mon = date.substring(4, 6);
    			day = date.substring(6, 8);
    			return year+"/"+mon+"/"+day;
    		}else {
    			return date;
    		}
        }
    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH:mm
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103年04月10日 15:53
     */
    public static String getReportDateTime2(String date){
	String dateString = "";
	if (nvl(date).length() > 0) {
	    Date d = tryParse(date);
	    int twYear = d.getYear() - 11;
	    // 設定日期格式
	    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
	    // 進行轉換
	    dateString = addPrefixZero(twYear+"", 3) + "年" + sdf.format(d);
	}
	return dateString;
    }
   
    static String[] formatStrings = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyyMM","yyyyMMdd" };

    /**
     * 嘗試使用幾個不同的方式將string轉成Date
     * 
     * @param dateString
     *            目前可接受格式"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
     *            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd"
     * @return
     */
    public static Date tryParse(String dateString)
    {
	for (String formatString : formatStrings)
	{
	    try
	    {
		return new SimpleDateFormat(formatString).parse(dateString);
	    } catch (ParseException e) {
	    }
	}

	return null;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11 21:10:03
     * 
     * @param usDateTime
     * @return 回傳格式為yyy/MM/dd HH:mm:ss
     */
    public static String getTwDateTime(Date usDateTime) {
	if (usDateTime == null) {
	    return "";
	}
	String twDateString = "";
	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(usDateTime);
	    int year = cal.get(Calendar.YEAR) - 1911;
	    twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/"
		    + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2)
		    + " " + addPrefixZero(cal.get(Calendar.HOUR_OF_DAY) + "", 2) + ":"
		    + addPrefixZero(cal.get(Calendar.MINUTE) + "", 2) + ":" + addPrefixZero(cal.get(Calendar.SECOND) + "", 2);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
	return twDateString;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11 21:10:03
     * 
     * @param usDateTime
     *            格式為yyyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyy/MM/dd HH:mm:ss
     */
    public static String getTwDateTime(String usDateTime) {
	if (nvl(usDateTime).equals("")) {
	    return "";
	}
	String twDateString = "";
	try {
	    Date date = tryParse(usDateTime);
	    if (date != null) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR) - 1911;
		twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/"
			+ addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2) + " "
			+ addPrefixZero(cal.get(Calendar.HOUR_OF_DAY) + "", 2) + ":"
			+ addPrefixZero(cal.get(Calendar.MINUTE) + "", 2) + ":"
			+ addPrefixZero(cal.get(Calendar.SECOND) + "", 2);

	    }
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
	return twDateString;
    }

    public static String replace2TwYear(String usDateTime) {
	if (nvl(usDateTime).equals("") || nvl(usDateTime).length() < 4) {
	    return "";
	}
	StringBuilder twYear = new StringBuilder();
	twYear.append(Integer.parseInt(usDateTime.substring(0, 4)) - 1911);
	String twDate = twYear.toString() + usDateTime.substring(4);
	twDate = twDate.replaceAll("-", "/");
	return twDate;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11
     * 
     * @param usDate
     *            格式為yyyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyy/MM/dd
     */
    public static String getTwDate(String usDate) {
	if (nvl(usDate).equals("")) {
	    return "";
	}
	String twDateString = "";
	try {
	    Date date = tryParse(usDate);
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int year = cal.get(Calendar.YEAR) - 1911;
	    twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/"
		    + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}

	return twDateString;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11
     * 
     * @param usDate
     * @return 回傳格式為yyy/MM/dd
     */
    public static String getTwDate(Date usDate) {
	if (usDate == null) {
	    return "";
	}
	String twDateString = "";
	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(usDate);
	    int year = cal.get(Calendar.YEAR) - 1911;
	    twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/"
		    + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}

	return twDateString;
    }

    /*
     * 從民國年轉換成西元年
     */
    public static String getUsDateTime(String TwDateTime) {
	if (nvl(TwDateTime).equals("")) {
	    return "";
	}
	StringBuilder usYear = new StringBuilder();
	usYear.append(Integer.parseInt(TwDateTime.substring(0, 3)) + 1911);
	String USDate = usYear.toString() + TwDateTime.substring(3);
	USDate = USDate.replaceAll("-", "/");
	return USDate;
    }

    /*
     * 從西元年轉換成民國年 回傳格式 DateTimeType.Long : yyyy/MM/dd HH:mm:ss
     * DateTimeType.Short : yyyy/MM/dd DateLocaleType.US : 西元格式
     * DateLocaleType.TW : 民國格式
     */
    public static String getFormatDateTime(Date sourceDate, DateTimeType type, DateLocaleType locale) throws ParseException {
	String handleDateStr = "";
	String resultDateStr = "";
	if (type == DateTimeType.Long) {
	    handleDateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(sourceDate);
	    ;
	}
	else if (type == DateTimeType.Short) {
	    handleDateStr = new SimpleDateFormat("yyyy/MM/dd").format(sourceDate);
	    ;
	}

	if (locale == DateLocaleType.US) {
	    resultDateStr = getUsDateTime(handleDateStr);
	}
	else if (locale == DateLocaleType.TW) {
	    resultDateStr = getTwDateTime(handleDateStr);
	}
	return resultDateStr;
    }

    // 下面保留日期運算功能

    /*
	 * 
	 */
    public void SysDate(String date) {
	setDate(date);
    }

    /*
	 * 
	 */
    public void SysDate(int year, int month, int day) {
	setDate(year, month, day);
    }

    /*
	 * 
	 */
    public void setDate(String date) {
	try {
	    DateFormat df = DateFormat.getDateInstance();
	    df.setCalendar(calendar);
	    df.parse(date);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
    }

    /*
	 * 
	 */
    public void setDate(int year, int month, int day) {
	try {
	    calendar.clear();
	    calendar.set(year, month - 1, day);
	} catch (Exception e) {
	    //log.error(ExceptionUtil.toString(e));
	}
    }

    /*
	 * 
	 */
    public void add(int days) {
	calendar.add(Calendar.DAY_OF_YEAR, days);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
	String retVal = calendar.get(Calendar.YEAR)
		+ "/"
		+ (calendar.get(Calendar.MONTH) + 1)
		+ "/"
		+ calendar.get(Calendar.DAY_OF_MONTH);
	return retVal;
    }

    /**
     * 取得系統現在的日期時間，格式為:yyyy/MM/dd HH:mm:ss
     * 
     * @return
     */
    public static String getDateTime() {
	// 目前時間
	java.util.Date now = new java.util.Date();
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// 進行轉換
	String dateString = sdf.format(now);
	return dateString;
    }

    /**
     * 取得系統現在的日期時間，格式為:yyyyMMddHHmmss,給SybaseDB使用
     * 
     * @return
     */
    public static String getSystemTimeDB() {
	// 目前時間
	java.util.Date now = new java.util.Date();
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	// 進行轉換
	String dateString = sdf.format(now);
	return dateString;
    }

    /**
     * 取得yyyy/MM/dd格式
     * 
     * @param now
     * @return
     */
    public static String get8DateFormat(Date now) {
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	// 進行轉換
	String dateString = sdf.format(now);
	return dateString;
    }

    /**
     * 取得yyyy-MM-dd格式
     * 
     * @param now
     * @return
     */
    public static String get8DateFormatDB(Date now) {
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// 進行轉換
	String dateString = sdf.format(now);
	return dateString;
    }

    /**
     * 取得8碼的DB格式(yyyyMMdd)
     * 
     * @param date 年份為民國年
     *            目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd",
     *            "yyy/MM/dd HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyyMMdd
     */
    public static String get8UsDateFormatDB(String date) {
	Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	// 進行轉換
	String dateString = sdf.format(d);
	return dateString;
    }
    /**
     * 取得7碼的民國年格式(yyy/MM/dd)
     * 
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM/dd)
     */
    public static String get7TwDateFormat(String date) {
	Date d = DateUtil.tryParse(date);
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	// 進行轉換
	String dateString = sdf.format(d);
	dateString = DateUtil.getTwDateTime(dateString);
	dateString = dateString.substring(0, 9);//去掉後面的時間
	return dateString;
    }
    /**
     * 取得14碼的DB格式(yyyyMMddHHmmss)
     * @param date 年份為民國年
     *            目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd",
     *            "yyy/MM/dd HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyyMMddHHmmss
     */
    public static String get14UsDateFormatDB(String date) {
	Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	// 進行轉換
	String dateString = sdf.format(d);
	return dateString;
    }
    
    /**
     * 取得13碼的民國年格式(yyy/MM/dd HH:mm:ss)
     * 
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM/dd HH:mm:ss)
     */
    public String get13TwDateFormat(String date) {
	Date d = DateUtil.tryParse(date);
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// 進行轉換
	String dateString = sdf.format(d);
	dateString = DateUtil.getTwDateTime(dateString);
	return dateString;
    }
    
    /**
     * 取得14碼的西元年格式(yyy/MM/dd HH:mm:ss)
     * 
     * @param date 格式為yyyyMMddHHmmss
     * @return 西元年格式(yyyy/MM/dd HH:mm:ss)
     */
    public static String get14TwDateFormat(String date) {
	Date d = DateUtil.tryParse(date);
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// 進行轉換
	String dateString = sdf.format(d);
	return dateString;
    }
    
    /**
     * 取得11碼的民國年格式(yyy/MM/dd HH:mm)
     * 
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM/dd HH:mm)
     */
    public static String get11TwDateFormat(String date) {
	Date d = DateUtil.tryParse(date);
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// 進行轉換
	String dateString = sdf.format(d);
	dateString = DateUtil.getTwDateTime(dateString);
	if(nvl(dateString).length()>0){
	    dateString = dateString.substring(0, dateString.length()-3);
	}
	return dateString;
    }
    /**
     * 取得yyyy/MM/dd HH:mm:ss格式
     * 
     * @param now
     * @return
     */
    public static String get14DateFormat(Date now) {
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// 進行轉換
	String dateString = sdf.format(now);
	return dateString;
    }

    /**
     * 取得yyyy-MM-dd HH:mm:ss格式,給prepareStatement使用
     * 
     * @param now
     * @return
     */
    public static String get14DateFormatDB(Date now) {
	// 設定日期格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 進行轉換
	String dateString = sdf.format(now);
	return dateString;
    }

    /**
     * 將來自DB的各個欄位 西元年月日時分 轉成民國年 YYY/MM/DD HH:MM
     * 
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @return
     */
    public static String to11TwDateTime(String year, String month, String day, String hour, String min) {
	String tmp = "";
	year = nvl(year);
	month = nvl(month);
	day = nvl(day);
	hour = nvl(hour);
	min = nvl(min);
	if (year.length() != 0 && month.length() != 0 && day.length() != 0 && hour.length() != 0 && min.length() != 0) {
	    int twYear;
	    twYear = Integer.parseInt(year) - 1911;
	    tmp = twYear + "/" + month + "/" + day + " " + hour + ":" + min;
	}
	return tmp;
    }

    /**
     * 將來自DB的各個欄位 西元年月日 轉成民國年 YYY/MM/DD
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String to7TwDateTime(String year, String month, String day) {
	String tmp = "";
	year = nvl(year);
	month = nvl(month);
	day = nvl(day);
	if (year.length() != 0 && month.length() != 0 && day.length() != 0) {
	    int twYear;
	    twYear = Integer.parseInt(year) - 1911;
	    tmp = twYear + "/" + month + "/" + day;
	}
	return tmp;
    }

    /**
     * 將來自操作畫面的各個欄位民國年 YYY/MM/DD 轉成西元年月日陣列
     * 
     * @param date
     *            YYY/MM/DD
     * @return 西元年月日陣列，0-西元年，1-月，2-日
     */
    public static String[] split8UsDateTime(String date) {
	String tmp[] = new String[] { "", "", "" };
	date = nvl(date);
	if (date.length() != 0) {
	    int twYear = Integer.parseInt(date.substring(0, date.indexOf("/"))) + 1911;
	    String month = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"));
	    String day = date.substring(date.lastIndexOf("/") + 1);
	    tmp[0] = String.valueOf(twYear);
	    tmp[1] = month;
	    tmp[2] = day;
	}
	return tmp;
    }

    public static String addPrefixZero(String value, int valueLength) {
	String tmp = value;
	if (value != null && value.length() < valueLength) {
	    int diff = valueLength - value.length();
	    for (int i = 0; i < diff; i++) {
		tmp = "0" + tmp;
	    }
	}
	return tmp;
    }
    
    public static void main(String[] args){
	System.out.println(DateUtil.get14UsDateFormatDB("103/6/20"));
	System.out.println(DateUtil.get14UsDateFormatDB("103/6/20 14:33"));
	System.out.println(DateUtil.get14UsDateFormatDB("103/6/20 14:33:12"));
	//System.out.println(DateUtil.get13TwDateFormat("20140620143312"));
	System.out.println(DateUtil.get11TwDateFormat("20140620143312"));
	System.out.println(DateUtil.get7TwDateFormat("20140620143312"));
	System.out.println();
    }
    
    /**
     * 將來自DB的日期欄位 西元年月日YYYYMMDD 轉成民國年 YYY/MM/DD
     * 
     * @param strDate YYYYMMDD
     * @return YYY/MM/DD
     */
    public static String to7TwDateTime(String strDate) {
	String tmp = "";
	if (!"".equals(strDate) && strDate.length() == 8)
		tmp = new StringUtil().MakesUpZero(String.valueOf((Integer.parseInt(strDate.substring(0,4))-1911)),3) + "/"
			+ strDate.substring(4,6) + "/"
			+ strDate.substring(6,8);
	return tmp;
    }
    
    /**
     * 將來自DB的日期欄位 西元年月日YYYYMMDD 轉成民國年 YYY年MM月DD日
     * 
     * @param strDate YYYYMMDD
     * @return YYY年MM月DD日
     */
    public String to7TwDateWithCB(String strDate) {
	String tmp = "";
	if (!"".equals(strDate) && strDate.length() == 8)
		tmp = new StringUtil().MakesUpZero(String.valueOf((Integer.parseInt(strDate.substring(0,4))-1911)), 3) + "年"
			+ strDate.substring(4,6) + "月"
			+ strDate.substring(6,8) + "日";
	return tmp;
    }
    
    /**
     * 將日期欄位 西元年月日YYYMMDD 轉成民國年 YYYYMMDD
     * 
     * @param strDate YYYMMDD
     * @return YYYYMMDD
     */
    public static String toNormalDateTime(String strDate) {
	String tmp = "";
	if (!"".equals(strDate) && strDate.length() == 7)
		tmp = String.valueOf((Integer.parseInt(strDate.substring(0,3))+1911))
			+ strDate.substring(3,5)
			+ strDate.substring(5,7);
	return tmp;
    }

    /**
     * 將日期欄位 西元年月日YYYYMMDD 轉成西元年 YYYY/MM/DD
     * 
     * @param strDate YYYYMMDD
     * @return YYYY/MM/DD
     */
    public String toNormalDateSlash(String strDate) {
	String tmp = "";
	if (!"".equals(strDate) && strDate.length() == 8)
		tmp = strDate.substring(0,4) + "/"
			+ strDate.substring(4,6) + "/"
			+ strDate.substring(6,8);
	return tmp;
    }
}
