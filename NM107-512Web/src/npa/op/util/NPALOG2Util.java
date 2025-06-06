/*
 * PoliceMailSender.java
 *
 * Created on 2014年09月15日, 下午 6:21
 */
package npa.op.util;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import npa.op.util.ExceptionUtil;
import npa.op.vo.User;
import npa.op.util.NpaConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.InetAddress;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
//20220307 wennie
import java.util.Properties;
import java.io.File;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NPALOG2Util {

    public static int LogToMSMQ = NpaConfig.getInt("LogToMSMQ");    // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
    public static String url = NpaConfig.getString("urlNpaLog");
    private static Logger log = Logger.getLogger(NPALOG2Util.class);

    /**
     * 寫入日誌
     *
     * @param opType 日誌類型(Q、I、U、D...等)
     * @param filter 畫面輸入欄位(查詢條件、新增資料等)
     * @param qId 該筆資料之身分證字號
     * @param qName 該筆資料之真實姓名
     * @param qBirthday 改筆資料之出生日期
     * @param jObj 查詢出來之全部結果
     * @param logCols 查詢結果需記錄之欄位
     * @param functionName 功能名稱
     * @param userVO 登入者資訊
     * @param actUser 實際使用人名稱
     * @param actUnit 實際使用人單位
     * @param OPR_KIND   查詢用途代號
     * @param actUsage   查詢用途文字

     *
     * @return 0: 成功寫入Q Server; 1: 成功Roll Back; 2: 日誌 Exception; -1: 缺少必要資訊
     */
    public int WriteQueryLog
        (LOGOPTYPE opType, String filter, String qId, String qName, 
	String qBirthday, JSONArray jArray, HashMap<String, String> logCols, 
        String functionName,User userVO, String actUser, String actUnit, 
        String actUsage)
        {
        ArrayList<String> logQueryResult = new ArrayList<String>();
        StringBuilder ColNames = new StringBuilder();
        StringBuilder valueString = null;

        if (logCols.size() > 0) {
            for (String strKeys : logCols.keySet()) {
                ColNames.append(logCols.get(strKeys)).append("&");
            }

            logQueryResult.add(ColNames.replace(ColNames.lastIndexOf("&"), ColNames.lastIndexOf("&") + 1, "").toString());

            for (int i = 0; i < jArray.length(); i++) {
                valueString = new StringBuilder();
                JSONObject jObj = jArray.getJSONObject(i);
                for (String strKey : logCols.keySet()) {
                    if (jObj.has(strKey)) {
                        valueString.append(jObj.getString(strKey).replace("/", "").replace(":", "").replace(" ", "")).append("&");
                    }
                }
                logQueryResult.add(valueString.replace(valueString.lastIndexOf("&"), valueString.lastIndexOf("&") + 1, "").toString());
            }
        }

        try {
            System.out.println("this is NPALOG2UTIL_int_WriteQueryLog");
//       Function String logging(
//	 String LOG_UID,  String LOG_CN, String LOG_DID, String LOG_UIP,   // 1~4
//	 String LOG_TDT,  String OPR_SVR, String OPR_TDT, String OPR_PAGE, // 5~8
//	 String OPR_TYPE, String OPR_SN, String OPR_QEND, String OPR_MEND, // 9~12 
// 	 String OPR_NAME, String OPR_DN, String OPR_KIND, String OPR_PURP, //13~16
//	 String OPR_CASEID, String OPR_COND, String QND_IDNO, String QND_NAME,//17~20
//       String QND_BIRTHDT, String QND_CARTYPE, String QND_CARNO, String QND_PHONE, //21~24
//	 String QND_IMEI, String QND_CASENO, String PAGE_OBJ)  //25~26

//       Function int    logging(
//	 int mesgSendWay, int big5ToUnicode, String LOG_UID, String LOG_DID, //1~4
//	 String LOG_UIP, String LOG_TDT, String OPR_SVR, String OPR_TDT,     //5~8
//       String OPR_PAGE, String OPR_TYPE, String OPR_SN, String OPR_QEND,   //9~12
//	 String OPR_MEND, String OPR_NAME, String OPR_DN, String OPR_KIND, String OPR_PURP,   //13~17
//       String OPR_COND, String QND_IDNO, String QND_NAME, String QND_BIRTHDT, //18~21 
//	 String QND_CARTYPE, String QND_CARNO, String PAGE_OBJ)  //22~24
            //202403
                    return logging(
                    LogToMSMQ, // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
                    1, //特定欄位之中文是否需要進big5轉unicode 0:中文需要轉碼 1:無須進行轉碼動作                    userVO.getUserId() + "-" + userVO.getUserName(), 
                    userVO.getUserId(),   //202403 actUser,
                    userVO.getUnitCd(),   //4
                    userVO.getUserIp(),    
                    userVO.getLoginTime(),
                    java.net.InetAddress.getLocalHost().getHostName(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()), //8
                    functionName,    //  request.getRequestURL().toString().substring(request.getRequestURL().lastIndexOf("/") + 1),
                    opType.toString(),
                    "OP2",              //11
                    "",                 //12
                    "",                 //13
                    actUser, 
                    actUnit, 
                    "301",                    //16  202403 
                    "行政類/受處理報案",         //17  202403 actUsage 行政類/受處理報案 
                    "",                       //18  //filter, 
                    qId,                      //19
                    qName,
                    qBirthday,                //21
                    "",
                    "",
                    logQueryResult.size() > 0 ? genQueryXML(logQueryResult.toArray(new String[logQueryResult.size()])) : "XXXX" 
                    ) ;//24
                    
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return 2;
        }
    }

    public int WriteQueryLog_1
        (LOGOPTYPE opType, String filter, String qId, String qName, 
	String qBirthday, JSONArray jArray, HashMap<String, String> logCols, 
        String functionName,User userVO, String actUser, String actUnit, 
        String OPR_KIND , String OPR_PURP)
        {
        ArrayList<String> logQueryResult = new ArrayList<String>();
        StringBuilder ColNames = new StringBuilder();
        StringBuilder valueString = null;

        if (logCols.size() > 0) {
            for (String strKeys : logCols.keySet()) {
                ColNames.append(logCols.get(strKeys)).append("&");
            }

            logQueryResult.add(ColNames.replace(ColNames.lastIndexOf("&"), ColNames.lastIndexOf("&") + 1, "").toString());

            for (int i = 0; i < jArray.length(); i++) {
                valueString = new StringBuilder();
                JSONObject jObj = jArray.getJSONObject(i);
                for (String strKey : logCols.keySet()) {
                    if (jObj.has(strKey)) {
                        valueString.append(jObj.getString(strKey).replace("/", "").replace(":", "").replace(" ", "")).append("&");
                    }
                }
                logQueryResult.add(valueString.replace(valueString.lastIndexOf("&"), valueString.lastIndexOf("&") + 1, "").toString());
            }
        }

        try {
            System.out.println("this is NPALOG2UTIL_WriteQueryLog");
//       Function String logging(
//	 String LOG_UID,  String LOG_CN, String LOG_DID, String LOG_UIP,   // 1~4
//	 String LOG_TDT,  String OPR_SVR, String OPR_TDT, String OPR_PAGE, // 5~8
//	 String OPR_TYPE, String OPR_SN, String OPR_QEND, String OPR_MEND, // 9~12 
// 	 String OPR_NAME, String OPR_DN, String OPR_KIND, String OPR_PURP, //13~16
//	 String OPR_CASEID, String OPR_COND, String QND_IDNO, String QND_NAME,//17~20
//       String QND_BIRTHDT, String QND_CARTYPE, String QND_CARNO, String QND_PHONE, //21~24
//	 String QND_IMEI, String QND_CASENO, String PAGE_OBJ)  //25~26

//       Function int    logging(
//	 int mesgSendWay, int big5ToUnicode, String LOG_UID, String LOG_DID, //1~4
//	 String LOG_UIP, String LOG_TDT, String OPR_SVR, String OPR_TDT,     //5~8
//       String OPR_PAGE, String OPR_TYPE, String OPR_SN, String OPR_QEND,   //9~12
//	 String OPR_MEND, String OPR_NAME, String OPR_DN, String OPR_KIND, String OPR_PURP,   //13~17
//       String OPR_COND, String QND_IDNO, String QND_NAME, String QND_BIRTHDT, //18~21 
//	 String QND_CARTYPE, String QND_CARNO, String PAGE_OBJ)  //22~24
            //202403
                    return logging(
                    LogToMSMQ, // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
                    1, //特定欄位之中文是否需要進big5轉unicode 0:中文需要轉碼 1:無須進行轉碼動作                    userVO.getUserId() + "-" + userVO.getUserName(), 
                    userVO.getUserId(),   //202403  actUser,
                    userVO.getUnitCd(),   //4
                    userVO.getUserIp(),    
                    userVO.getLoginTime(),
                    java.net.InetAddress.getLocalHost().getHostName(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()), //8
                    functionName,    //  request.getRequestURL().toString().substring(request.getRequestURL().lastIndexOf("/") + 1),
                    opType.toString(),
                    "OP2",      //11
                    "",                 //12
                    "",                 //13
                    actUser, 
                    actUnit, 
                    OPR_KIND,             //16  202403 301, 
                    OPR_PURP,             //17  202403 actUsage "行政類/受處理報案"
                    filter,               //18    
                    qId,                  //19
                    qName,
                    qBirthday,            //21
                    "",                   //22
                    "",
                    logQueryResult.size() > 0 ? genQueryXML(logQueryResult.toArray(new String[logQueryResult.size()])) : "XXXX"    //24
                    ) ;//24
                    
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return 2;
        }
    }
        
    /**
     * 寫入日誌
     *
     * @param opType 日誌類型(Q、I、U、D...等)
     * @param jObject 畫面輸入欄位(查詢條件、新增資料等)
     * @param frontCols 畫面輸入欄位中文對應(查詢條件、新增資料等)
     * @param qId 該筆資料之身分證字號
     * @param qName 該筆資料之真實姓名
     * @param qBirthday 改筆資料之出生日期
     * @param jObj 查詢出來之全部結果
     * @param logCols 查詢結果需記錄之欄位
     * @param functionName 功能名稱
     * @param userVO 登入者資訊
     * @param actUser 實際使用人名稱
     * @param actUnit 實際使用人單位
     * @param actUsage 實際使用人用途
     *
     * @return 0: 成功寫入Q Server; 1: 成功Roll Back; 2: 日誌 Exception; -1: 缺少必要資訊
     */
    public int WriteQueryLog
        (LOGOPTYPE opType, JSONObject jObject, HashMap<String, String> frontCols,String qId, 
                String qName, String qBirthday, JSONArray jArray, HashMap<String, String> logCols,
                String functionName, User userVO, String actUser, String actUnit, 
                String actUsage) 
    {
        ArrayList<String> logQueryResult = new ArrayList<String>();
        StringBuilder ColNames = new StringBuilder();
        StringBuilder valueString = null, filter = new StringBuilder();

        if (frontCols != null && frontCols.size() > 0) {
            for (String strKeys : frontCols.keySet()) {
                if (!"".equals(jObject.getString(strKeys))) {
                    filter.append(String.format("&%s=%s", frontCols.get(strKeys), jObject.getString(strKeys)));
                }
            }
        }

        if (logCols.size() > 0) {
            for (String strKeys : logCols.keySet()) {
                ColNames.append(logCols.get(strKeys).replace("/", "").replace(":", "").replace(" ", "")).append("&");
            }

            logQueryResult.add(ColNames.replace(ColNames.lastIndexOf("&"), ColNames.lastIndexOf("&") + 1, "").toString());

            for (int i = 0; i < jArray.length(); i++) {
                valueString = new StringBuilder();
                JSONObject jObj = jArray.getJSONObject(i);
                for (String strKey : logCols.keySet()) {
                    if (jObj.has(strKey)) {
                        valueString.append(jObj.getString(strKey)).append("&");
                    }
                }
                logQueryResult.add(valueString.replace(valueString.lastIndexOf("&"), valueString.lastIndexOf("&") + 1, "").toString());
            }
        }

        try {
            System.out.println("this is NPALOG2UTIL.WriteQueryLog(int) ");
            return logging(LogToMSMQ, // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
                    1, //特定欄位之中文是否需要進big5轉unicode 0:中文需要轉碼 1:無須進行轉碼動作
                    userVO.getUserId(), // 202403+ "-" + userVO.getUserName(), 
                    userVO.getUnitCd(),
                    userVO.getUserIp(), userVO.getLoginTime(),
                    java.net.InetAddress.getLocalHost().getHostName(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                    functionName,//request.getRequestURL().toString().substring(request.getRequestURL().lastIndexOf("/") + 1),
                    opType.toString(),
                    "OP2",
                    "",
                    "",
                    actUser, 
                    actUnit,
                    "301",                //16  202403
                    "行政類/受處理報案",     //17  202403 actUsage 行政類/受處理報案
                    "",                   //18  
                    qId,
                    qName,
                    qBirthday,
                    "",
                    "",
                    logQueryResult.size() > 0 ? genQueryXML(logQueryResult.toArray(new String[logQueryResult.size()])) : "查無資料"
                    );
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return 2;
        }
    }

    /**
     * 寫入日誌
     *
     * @param opType 日誌類型(Q、I、U、D...等)
     * @param qId 該筆資料之身分證字號
     * @param qName 該筆資料之真實姓名
     * @param qBirthday 改筆資料之出生日期
     * @param jObj 查詢出來之全部結果
     * @param logCols 查詢結果需記錄之欄位
     * @param functionName 功能名稱
     * @param userVO 登入者資訊
     * @param actUser 實際使用人名稱
     * @param actUnit 實際使用人單位
     * @param actUsage 實際使用人用途
     *
     * @return 0: 成功寫入Q Server; 1: 成功Roll Back; 2: 日誌 Exception; -1: 缺少必要資訊
     */
    public int WriteModifyLog
        (LOGOPTYPE opType, HashMap<String, String> logCols, JSONObject beforeUpdate, JSONObject afterUpdate, 
                String qId, String qName, String qBirthday, String functionName, 
                User userVO) 
        {
        StringBuilder beforeString = new StringBuilder(), afterString = new StringBuilder();

        if (beforeUpdate.length() > 0) {
            for (String strKeys : logCols.keySet()) {
                if (!"".equals(beforeUpdate.getString(strKeys))) {
                    beforeString.append(String.format("&%s=%s", logCols.get(strKeys), beforeUpdate.getString(strKeys).replace("/", "").replace(":", "").replace(" ", "")));
                }
            }
        }

        if (afterUpdate.length() > 0) {
            for (String strKeys : logCols.keySet()) {
                if (!"".equals(afterUpdate.getString(strKeys))) {
                    afterString.append(String.format("&%s=%s", logCols.get(strKeys), afterUpdate.getString(strKeys).replace("/", "").replace(":", "").replace(" ", "")));
                }
            }
        }

        try {
            System.out.println("this is NPALOG2UTIL.WriteModifyLog");
            return logging(LogToMSMQ, // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
                    1, //特定欄位之中文是否需要進big5轉unicode 0:中文需要轉碼 1:無須進行轉碼動作
                    userVO.getUserId(), // + "-" + userVO.getUserName(),   //202403
                    userVO.getUnitCd(),
                    userVO.getUserIp(),  //5
                    userVO.getLoginTime(),
                    java.net.InetAddress.getLocalHost().getHostName(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                    functionName,//request.getRequestURL().toString().substring(request.getRequestURL().lastIndexOf("/") + 1),
                    opType.toString(),   
                    "OP2",              //11
                    "",
                    LOGMODRESULT.S.toString(),
                    "", 
                    "",
                    afterUpdate.getString("OPR_KIND"),    //202403  16
                    afterUpdate.getString("OPR_PURP"),    //202403  17
                    "",
                    qId,
                    qName,              //20
                    qBirthday,
                    "",
                    "",
                    genUpdateXML(beforeString.toString(), afterString.toString()));
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return 2;
        }
    }

    public enum LOGOPTYPE {
        I,
        D,
        U,
        Q,
        T,
        W,
        Z,
        P,
        N
    }

    //操作成功或失敗
    public enum LOGMODRESULT {
        S,
        F
    }


//    public int logging2(int mesgSendWay, int big5ToUnicode, String LOG_UID, String LOG_DID, String LOG_UIP, String LOG_TDT, String OPR_SVR, String OPR_TDT, String OPR_PAGE, String OPR_TYPE, String OPR_SN, String OPR_QEND, String OPR_MEND, String OPR_NAME, String OPR_DN, String OPR_KIND, String OPR_PURP, String OPR_COND, String QND_IDNO, String QND_NAME, String QND_BIRTHDT, String QND_CARTYPE, String QND_CARNO, String PAGE_OBJ) {
//        System.out.println("NPALOG2:LOG_UID=" + LOG_UID + "@LOG_DID=" + LOG_DID + "@LOG_UIP=" + LOG_UIP + "@LOG_TDT=" + LOG_TDT + "@OPR_SVR=" + OPR_SVR + "@OPR_TDT=" + OPR_TDT + "@OPR_PAGE=" + OPR_PAGE + "@OPR_TYPE=" + OPR_TYPE + "@OPR_SN=" + OPR_SN + "@OPR_QEND=" + OPR_QEND + "@OPR_MEND=" + OPR_MEND + "@OPR_NAME=" + OPR_NAME + "@OPR_DN=" + OPR_DN + "@OPR_PURP=" + OPR_PURP + "@OPR_COND=" + OPR_COND + "@QND_IDNO=" + QND_IDNO + "@QND_NAME=" + QND_NAME + "@QND_BIRTHDT=" + QND_BIRTHDT + "@QND_CARTYPE=" + QND_CARTYPE + "@QND_CARNO=" + QND_CARNO + "@PAGE_OBJ=" + PAGE_OBJ);
//        System.out.println("this is NPALOG2UTIL logging2");
//        return logging(mesgSendWay, big5ToUnicode, LOG_UID, LOG_DID, LOG_UIP, LOG_TDT, OPR_SVR, OPR_TDT, OPR_PAGE, OPR_TYPE, OPR_SN, OPR_QEND, OPR_MEND, OPR_NAME, OPR_DN, OPR_KIND, OPR_PURP, OPR_COND, QND_IDNO, QND_NAME, QND_BIRTHDT, QND_CARTYPE, QND_CARNO, PAGE_OBJ);
//    }


    public int logging(int mesgSendWay, int big5ToUnicode, 
            String LOG_UID, String LOG_DID, String LOG_UIP,   //5
            String LOG_TDT, String OPR_SVR, String OPR_TDT,
            String OPR_PAGE, String OPR_TYPE, String OPR_SN,  //11
            String OPR_QEND, String OPR_MEND, String OPR_NAME, String OPR_DN, //15  
            String OPR_KIND,String OPR_PURP,   //16,17
            String OPR_COND, String QND_IDNO, String QND_NAME,  //20
            String QND_BIRTHDT, String QND_CARTYPE,  //22
            String QND_CARNO, String PAGE_OBJ)   //24
    {
        
//202403
//          String result = logging(LOG_UID.substring(0, 8), LOG_UID.substring(9), LOG_DID, LOG_UIP, LOG_TDT, OPR_SVR, OPR_TDT, OPR_PAGE.contains("-") ? OPR_PAGE : ("系統功能頁面-" + OPR_PAGE), OPR_TYPE, OPR_SN, OPR_QEND, OPR_MEND, OPR_NAME,
//          OPR_DN ,OPR_KIND,OPR_PURP, "", OPR_COND, QND_IDNO, QND_NAME, QND_BIRTHDT, QND_CARTYPE, QND_CARNO, "", "", "", PAGE_OBJ);

            String result = logging(
                    LOG_UID, LOG_UID, LOG_DID, LOG_UIP, 
                    LOG_TDT, OPR_SVR, OPR_TDT, OPR_PAGE.contains("-") ? OPR_PAGE : ("系統功能頁面-" + OPR_PAGE), 
                    OPR_TYPE, OPR_SN, OPR_QEND ,OPR_MEND, 
                    OPR_NAME, OPR_DN ,OPR_KIND ,OPR_PURP, 
                    "", OPR_COND, QND_IDNO, QND_NAME, 
                    QND_BIRTHDT, QND_CARTYPE, QND_CARNO, ""
                    , "", "", PAGE_OBJ);

            System.out.println("NPALIGUtil_logging(int)_result:" + result);
                      log.info("NPALIGUtil_logging(int)_result:" + result);
            return 0;
  
    }

    /**
     * 寫入日誌
     *
     * @param opType 日誌類型(Q、I、U、D...等)
     * @param filter 畫面輸入欄位(查詢條件、新增資料等)
     * @param qId 該筆資料之身分證字號
     * @param qName 該筆資料之真實姓名
     * @param qBirthday 改筆資料之出生日期
     * @param jObj 查詢出來之全部結果
     * @param logCols 查詢結果需記錄之欄位
     * @param functionName 功能名稱
     * @param userVO 登入者資訊
     * @param actUser 實際使用人名稱
     * @param actUnit 實際使用人單位
     * @param actUsage 實際使用人用途
     * @param OPR_KIND 實際查詢人用途代碼
     * @param OPR_CASEID 案管案號
     * @param QND_PHONE 電話號碼
     * @param QND_IMEI 手機序號(IMEI)
     * @param QND_CASENO 案件號碼
     *
     * @return 0: 成功寫入Q Server; 1: 成功Roll Back; 2: 日誌 Exception; -1: 缺少必要資訊
     */
    
    public int WriteQueryLog_New(
            LOGOPTYPE opType, String filter, String qId, String qName, 
            String qBirthday, JSONArray jArray, HashMap<String, String> logCols, String functionName,
            User userVO, String actUser, String actUnit, 
          //String OPR_KIND, String actUsage,           //202403
            String OPR_KIND, String OPR_PURP,           //202504
            String OPR_CASEID, String QND_PHONE, String QND_IMEI, 
            String QND_CASENO
    ) {
        ArrayList<String> logQueryResult = new ArrayList<String>();
        StringBuilder ColNames = new StringBuilder();
        StringBuilder valueString = null;

        if (logCols.size() > 0) {
            for (String strKeys : logCols.keySet()) {
                ColNames.append(logCols.get(strKeys)).append("&");
            }

            logQueryResult.add(ColNames.replace(ColNames.lastIndexOf("&"), ColNames.lastIndexOf("&") + 1, "").toString());

            for (int i = 0; i < jArray.length(); i++) {
                valueString = new StringBuilder();
                JSONObject jObj = jArray.getJSONObject(i);
                for (String strKey : logCols.keySet()) {
                    if (jObj.has(strKey)) {
                        valueString.append(jObj.getString(strKey)).append("&");
                    }
                }
                logQueryResult.add(valueString.replace(valueString.lastIndexOf("&"), valueString.lastIndexOf("&") + 1, "").toString());
            }
        }

        try {
            return logging_New(
                    LogToMSMQ, // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
                    1, //特定欄位之中文是否需要進big5轉unicode 0:中文需要轉碼 1:無須進行轉碼動作
                    userVO.getUserId(),//202403 + "-" + userVO.getUserName(), 
                    userVO.getUnitCd(),
                    userVO.getUserIp(), 
                    userVO.getLoginTime(),
                    java.net.InetAddress.getLocalHost().getHostName(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
                    functionName,//request.getRequestURL().toString().substring(request.getRequestURL().lastIndexOf("/") + 1),
                    opType.toString(),
                    "OP2",
                    "",
                    "",
                    actUser, 
                    actUnit,
                    OPR_KIND,             //16  202403           301
                    OPR_PURP,             //17  202504 actUsage 行政類/受處理報案/自填原因
                 // actUsage,             //17  202403 actUsage 行政類/受處理報案
                    filter,               //18  
                    qId,
                    qName,
                    qBirthday,
                    "",
                    "",
                    logQueryResult.size() > 0 ? genQueryXML(logQueryResult.toArray(new String[logQueryResult.size()])) : ""  ,//24
                    OPR_CASEID,   //25
                    QND_PHONE,    //26
                    QND_IMEI,     //27
                    QND_CASENO    //28
                    );
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return 2;
        }
    }

    /**
     * 寫入日誌
     *
     * @param opType 日誌類型(Q、I、U、D...等)
     * @param jObject 畫面輸入欄位(查詢條件、新增資料等)
     * @param frontCols 畫面輸入欄位中文對應(查詢條件、新增資料等)
     * @param qId 該筆資料之身分證字號
     * @param qName 該筆資料之真實姓名
     * @param qBirthday 改筆資料之出生日期
     * @param jObj 查詢出來之全部結果
     * @param logCols 查詢結果需記錄之欄位
     * @param functionName 功能名稱
     * @param userVO 登入者資訊
     * @param actUser 實際使用人名稱
     * @param actUnit 實際使用人單位
     * @param OPR_KIND 實際查詢人用途代碼
     * @param OPR_PURP  實際使用人用途 (actUsage)
     * @param OPR_CASEID 案管案號
     * @param QND_PHONE 電話號碼
     * @param QND_IMEI 手機序號(IMEI)
     * @param QND_CASENO 案件號碼
     *
     * @return 0: 成功寫入Q Server; 1: 成功Roll Back; 2: 日誌 Exception; -1: 缺少必要資訊
     */
    //202403  沒在用，還取一樣的名子，到底是誰寫的WTF!!
//    public int WriteQueryLog_New_BK(LOGOPTYPE opType, JSONObject jObject, HashMap<String, String> frontCols, String qId, String qName, String qBirthday, JSONArray jArray, HashMap<String, String> logCols, String functionName, User userVO, String actUser, String actUnit, String actUsage, String OPR_KIND, String OPR_CASEID, String QND_PHONE, String QND_IMEI, String QND_CASENO) {
//
//        ArrayList<String> logQueryResult = new ArrayList<String>();
//        StringBuilder ColNames = new StringBuilder();
//        StringBuilder valueString = null, filter = new StringBuilder();
//
//        if (frontCols != null && frontCols.size() > 0) {
//            for (String strKeys : frontCols.keySet()) {
//                if (!"".equals(jObject.getString(strKeys))) {
//                    filter.append(String.format("&%s=%s", frontCols.get(strKeys), jObject.getString(strKeys)));
//                }
//            }
//        }
//
//        if (logCols.size() > 0) {
//            for (String strKeys : logCols.keySet()) {
//                ColNames.append(logCols.get(strKeys)).append("&");
//            }
//
//            logQueryResult.add(ColNames.replace(ColNames.lastIndexOf("&"), ColNames.lastIndexOf("&") + 1, "").toString());
//
//            for (int i = 0; i < jArray.length(); i++) {
//                valueString = new StringBuilder();
//                JSONObject jObj = jArray.getJSONObject(i);
//                for (String strKey : logCols.keySet()) {
//                    if (jObj.has(strKey)) {
//                        valueString.append(jObj.getString(strKey)).append("&");
//                    }
//                }
//                logQueryResult.add(valueString.replace(valueString.lastIndexOf("&"), valueString.lastIndexOf("&") + 1, "").toString());
//            }
//        }
//
//        try {
//            return logging_New(
//                    LogToMSMQ, // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
//                    1, //特定欄位之中文是否需要進big5轉unicode 0:中文需要轉碼 1:無須進行轉碼動作
//                    userVO.getUserId(),//202403  + "-" + userVO.getUserName(), 
//                    userVO.getUnitCd(),
//                    userVO.getUserIp(), 
//                    userVO.getLoginTime(),
//                    java.net.InetAddress.getLocalHost().getHostName(),
//                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
//                    functionName,//request.getRequestURL().toString().substring(request.getRequestURL().lastIndexOf("/") + 1),
//                    opType.toString(),
//                    "OP2",
//                    "",
//                    "",
//                    actUser, 
//                    actUnit, 
//                    "301",                //16  202403
//                    "行政類/受處理報案",     //17  202403 actUsage
//                    "",                   //18 "行政類/受處理報案"  //filter.toString(), 
//                    qId,
//                    qName,
//                    qBirthday,
//                    "",
//                    "",
//                    logQueryResult.size() > 0 ? genQueryXML(logQueryResult.toArray(new String[logQueryResult.size()])) : "查無資料" ,//24
//                    OPR_CASEID, //25
//                    QND_PHONE,  //26
//                    QND_IMEI,   //27
//                    QND_CASENO  //28
//            );
//        } catch (Exception e) {
//            log.error(ExceptionUtil.toString(e));
//            return 2;
//        }
//    }

    //新日誌多 實際查詢人用途代碼、案管案號、電話號碼、手機序號(IMEI)、案件號碼
//    public int logging_New(int mesgSendWay, int big5ToUnicode, String LOG_UID, String LOG_DID, String LOG_UIP, String LOG_TDT, String OPR_SVR, String OPR_TDT, String OPR_PAGE, String OPR_TYPE, String OPR_SN, String OPR_QEND, String OPR_MEND, String OPR_NAME, String OPR_DN, String OPR_PURP, String OPR_COND, String QND_IDNO, String QND_NAME, String QND_BIRTHDT, String QND_CARTYPE, String QND_CARNO, String PAGE_OBJ, String OPR_KIND, String OPR_CASEID, String QND_PHONE, String QND_IMEI, String QND_CASENO) {
//        System.out.println("NPALOG2024:LOG_UID=" + LOG_UID + "@LOG_DID=" + LOG_DID + "@LOG_UIP=" + LOG_UIP + "@LOG_TDT=" + LOG_TDT + "@OPR_SVR=" + OPR_SVR + "@OPR_TDT=" + OPR_TDT + "@OPR_PAGE=" + OPR_PAGE + "@OPR_TYPE=" + OPR_TYPE + "@OPR_SN=" + OPR_SN + "@OPR_QEND=" + OPR_QEND + "@OPR_MEND=" + OPR_MEND + "@OPR_NAME=" + OPR_NAME + "@OPR_DN=" + OPR_DN + "@OPR_PURP=" + OPR_PURP + "@OPR_COND=" + OPR_COND + "@QND_IDNO=" + QND_IDNO + "@QND_NAME=" + QND_NAME + "@QND_BIRTHDT=" + QND_BIRTHDT + "@QND_CARTYPE=" + QND_CARTYPE + "@QND_CARNO=" + QND_CARNO + "@PAGE_OBJ=" + PAGE_OBJ + "@OPR_KIND=" + OPR_KIND + "@OPR_CASEID=" + OPR_CASEID + "@QND_PHONE=" + QND_PHONE + "@QND_IMEI=" + QND_IMEI + "@QND_CASENO=" + QND_CASENO);
//        return logging_New(mesgSendWay, big5ToUnicode, LOG_UID, LOG_DID, LOG_UIP, LOG_TDT, OPR_SVR, OPR_TDT, OPR_PAGE, OPR_TYPE, OPR_SN, OPR_QEND, OPR_MEND, OPR_NAME, OPR_DN, OPR_PURP, OPR_COND, QND_IDNO, QND_NAME, QND_BIRTHDT, QND_CARTYPE, QND_CARNO, PAGE_OBJ, OPR_KIND, OPR_CASEID, QND_PHONE, QND_IMEI, QND_CASENO);
//        //--------------- (1          , 2            , 3      , 4      , 5      , 6      , 7      , 8      , 9       , 10      , 11    , 12      , 13      , 14      , 15    , 16      , 17      , 18      , 19      , 20         , 21         , 22       , 23      , 24      , 25        , 26       , 27      , 28        );
//    }
    
    public int logging_New(
			int mesgSendWay, int big5ToUnicode,    //1~2
			String LOG_UID,  String LOG_DID,       //3~4
			String LOG_UIP,  String LOG_TDT,       //5~6
			String OPR_SVR,  String OPR_TDT,       //7~8
			String OPR_PAGE,    String OPR_TYPE, String OPR_SN,                  // 9~11
			String OPR_QEND,    String OPR_MEND, String OPR_NAME, String OPR_DN, //12~15
			String OPR_KIND ,   String OPR_PURP,                  //16~17
			String OPR_COND, String QND_IDNO, String QND_NAME,    //18~20
			String QND_BIRTHDT, String QND_CARTYPE,               //21~22
			String QND_CARNO, String PAGE_OBJ,                    //23~24
                        String OPR_CASEID, String QND_PHONE,                  //25~26
                        String QND_IMEI, String QND_CASENO                    //27~28
            ) 
        { 
            System.out.println("this_is_NPALOG2UTIL(int)logging_New");
            System.out.println("NPALOG2Util_logging_New_Data_OPR_KIND=__" + OPR_KIND);
            System.out.println("NPALOG2Util_logging_New_Data_OPR_PURP=__" + OPR_PURP); 
                      log.info("NPALOG2Util_logging_New_Data_OPR_KIND=__" + OPR_KIND);
                      log.info("NPALOG2Util_logging_New_Data_OPR_PURP=__" + OPR_PURP); 

            
            String result = 
                logging(LOG_UID,  //202304 LOG_UID.substring(0, 8), 
                        OPR_NAME, //202304 LOG_UID.substring(9), 
                        LOG_DID, LOG_UIP, 
                        LOG_TDT, OPR_SVR, OPR_TDT, OPR_PAGE.contains("-") ? OPR_PAGE : ("系統功能頁面-" + OPR_PAGE), 
                        OPR_TYPE, OPR_SN, OPR_QEND, OPR_MEND, 
                        OPR_NAME, OPR_DN, OPR_KIND, OPR_PURP, 
                        OPR_CASEID, OPR_COND, QND_IDNO, QND_NAME, 
                      //        "", OPR_COND, QND_IDNO, QND_NAME, 
                        QND_BIRTHDT, QND_CARTYPE, QND_CARNO, QND_PHONE, 
                      //QND_BIRTHDT, QND_CARTYPE, QND_CARNO, "", 
                        QND_IMEI, QND_CASENO, PAGE_OBJ);
                      //""      , ""        , PAGE_OBJ);
        System.out.println("NPALOG2Util_int_logging_New__result:_" + result);
        return 0;
    }

    public String logging(
	 String LOG_UID,  String LOG_CN, String LOG_DID, String LOG_UIP,            // 1~4
	 String LOG_TDT,  String OPR_SVR, String OPR_TDT, String OPR_PAGE,          // 5~8
	 String OPR_TYPE, String OPR_SN, String OPR_QEND, String OPR_MEND,          // 9~12 
 	 String OPR_NAME, String OPR_DN, String OPR_KIND, String OPR_PURP,          //13~16
	 String OPR_CASEID, String OPR_COND, String QND_IDNO, String QND_NAME,       //17~20
         String QND_BIRTHDT, String QND_CARTYPE, String QND_CARNO, String QND_PHONE, //21~24
	 String QND_IMEI, String QND_CASENO, String PAGE_OBJ                         //25~27            
        )
            {
            System.out.println("this_is_NPALOG2UTIL_logging(String)");
            
            System.out.println("NPALOG2Util_logging(String)_Data_OPR_KIND= __" + OPR_KIND);
            System.out.println("NPALOG2Util_logging(String)_Data_OPR_PURP= __" + OPR_PURP);  
                      log.info("NPALOG2Util_logging(String)_Data_OPR_KIND= __" + OPR_KIND);
                      log.info("NPALOG2Util_logging(String)_Data_OPR_PURP= __" + OPR_PURP);  

            
        String result = "";
        //20220307 wennie 改
        JSONObject postData = new JSONObject();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            // 20220307 Writing to  json file for BATCH  BY wennie
            Properties properties = new npa.op.util.OPUtil().getProperties();
            String StrWriteFileFlag = properties.getProperty("WriteFileFlag").toString();

            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");

//                JSONObject postData = new JSONObject();
            postData.put("LOG_UID", getString(LOG_UID).toUpperCase());       //使用者帳號
            postData.put("LOG_CN",  getString(OPR_NAME));         	     //使用者中文姓名  //202403 LOG_CN
            postData.put("LOG_DID", getString(LOG_DID).toUpperCase());       //使用者單位代碼
            postData.put("LOG_UIP", LOG_UIP);       	//使用者登入IP
            postData.put("LOG_TDT", transTimeString(LOG_TDT));       //使用者登入時間
            postData.put("OPR_SVR", getString(OPR_SVR).length() == 0 ? InetAddress.getLocalHost().getHostName().toUpperCase() : getString(OPR_SVR).toUpperCase());       //AP系統實體伺服器伺服器代碼(名稱)
            postData.put("OPR_TDT", getString(OPR_TDT).length() == 0 ? new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) : transTimeString(OPR_TDT));       //使用者作業時間
            postData.put("OPR_PAGE", getString(OPR_PAGE).replaceAll("_", "").replaceAll(".jsp", ""));     //網頁名稱及檔名  //202403不能有附檔名，故把.jsp換掉
            postData.put("OPR_TYPE", OPR_TYPE);     	//作業種類
            postData.put("OPR_SN", OPR_SN);         	//AP系統代碼
            postData.put("OPR_QEND", OPR_QEND);     	//查詢結果(可略)
            postData.put("OPR_MEND", OPR_MEND);     	//異動結果(可略)
            postData.put("OPR_NAME", getString(OPR_NAME));     //實際查詢人姓名
            postData.put("OPR_DN", getString(OPR_DN));         //實際查詢人單位
            postData.put("OPR_KIND", getString(OPR_KIND));     //實際查詢人用途種類
            postData.put("OPR_PURP", getString(OPR_PURP));     //實際查詢人用途說明
            postData.put("OPR_CASEID", getString(OPR_CASEID).toUpperCase()); //員警開案案號
            postData.put("OPR_COND", OPR_COND);     	//查詢條件
            postData.put("QND_IDNO", QND_IDNO);     	//身分證號
            postData.put("QND_NAME", QND_NAME);     	//姓名
            postData.put("QND_BIRTHDT", QND_BIRTHDT);   //生日
            postData.put("QND_CARTYPE", QND_CARTYPE);   //車牌種類
            postData.put("QND_CARNO", QND_CARNO);   	//車牌號碼
            postData.put("QND_PHONE", QND_PHONE);   	//電話號碼
            postData.put("QND_IMEI", QND_IMEI);     	//手機序號(IMEI)
            postData.put("QND_CASENO", QND_CASENO); 	//案件號碼
            postData.put("PAGE_OBJ", PAGE_OBJ);     	//全文內容

            post.setEntity(new StringEntity(postData.toString(), "UTF-8"));
            System.out.println("NPALOG2Util_logging_Log json Data = " + postData.toString());
                      log.info("NPALOG2Util_logging_Log json Data = " + postData.toString());

            //20220307 wennie tidy function
            if ("true".equals(StrWriteFileFlag)) {
//              if ( LogToMSMQ == 0 &&!( InetAddress.getLocalHost().getHostName().equals("G9-353809-VM"))) { // "0" 意指「署正式測試傳送HttpResponse」, "1" 意指「本機端不傳送HttpResponse」
                if (!LOG_UIP.equals("999.999.999.999")) {
                    HttpResponse resp = client.execute(post);
                    System.out.println("Response Code = " + resp.getStatusLine().getStatusCode());
                    HttpEntity entity = resp.getEntity();
                    result = EntityUtils.toString(entity);
                    System.out.println("result = " + result);
                    if (resp.getStatusLine().getStatusCode() != 200) {
                        WriteFileLog(postData, OPR_TDT, true);
                    }
                } else {
                    System.out.println("NPALOG2Util_logging_result = " + "LogToMSMQ 為 1, 本機端不傳送日誌，要傳送請將 LogToMSMQ 更改為 0");
                }
            } else {
                if (!LOG_UIP.equals("999.999.999.999")) {
                    WriteFileLog(postData, OPR_TDT, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString();
            if (!LOG_UIP.equals("999.999.999.999")) {
                WriteFileLog(postData, OPR_TDT, false);
            }
        }
        return result;
    }
    //20220307 wennie tidy function

    private void WriteFileLog(JSONObject postData, String OPR_TDT, boolean errorcode) {
        Properties properties = new npa.op.util.OPUtil().getProperties();
        String StrWriteFiles = "";
        if (errorcode) {
            StrWriteFiles = properties.getProperty("WriteFilesError").toString();
        } else {
            StrWriteFiles = properties.getProperty("WriteFiles").toString();
        }
        try {
            String ddate = transTimeString(OPR_TDT);

            File file = new File(StrWriteFiles + ddate + ".txt");
            if (!file.getParentFile().exists() && !file.isDirectory()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();

                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),
                        StandardCharsets.UTF_8), true);

                //PrintWriter printWriter = new PrintWriter(file);
                printWriter.write(postData.toString());
                printWriter.close();
                //System.out.println("write a file in D:\\DO\\message");
            }

        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public String genQueryXML(String[] QRslt) {
        String[] columnName = QRslt[0].split("&");
        int colNum = columnName.length;
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Data");
        for (int i = 1; i < QRslt.length; i++) {
            String[] data = QRslt[i].split("&", colNum);
            Element datarow = root.addElement("DataRow");
            for (int j = 0; j < columnName.length; j++) {
                datarow.addElement(columnName[j]).addText(data[j]);
            }
        }
        return document.asXML();
    }

    public String genUpdateXML(String BeforeUpdate, String AfterUpdate) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("Data");
        Element datarow = root.addElement("DataRow");
        datarow.addElement("BeforeUpdate").addText(BeforeUpdate);
        datarow.addElement("AfterUpdate").addText(AfterUpdate);
        return doc.asXML();
    }

    private String transTimeString(String input) {
        String output = "";
        if (input != null) {
            output = input.replaceAll("/", "").replaceAll(" ", "").replaceAll(":", "");
        }
        return output;
    }

    private String getString(Object o) {
        String tmp = "";
        if (o != null && !o.equals("null")) {
            tmp = o.toString().trim();
        }
        return tmp;
    }
}
