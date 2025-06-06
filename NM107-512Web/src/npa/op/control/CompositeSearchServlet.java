package npa.op.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import npa.op.base.AjaxBaseServlet;
import npa.op.dao.OPDT_E_NET_CLAIM;
import npa.op.dao.OPDT_I_ANNOUNCE;
import npa.op.dao.OPDT_I_PU_BASIC;
import npa.op.dao.OPDT_I_PU_CLAIM;
import npa.op.dao.OPDT_I_PU_DETAIL;
//import npa.op.util.AesCrypt;
//import npa.op.util.DBUtil;
import com.syscom.db.DBUtil;
import npa.op.util.DateUtil;
import npa.op.util.ExportUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import static npa.op.util.StringUtil.replaceWhiteChar;
import npa.op.vo.User;

/**
 * 拾得遺失物綜合查詢
 */
@WebServlet("/CompositeSearchServlet")
public class CompositeSearchServlet extends AjaxBaseServlet {

    private static final long serialVersionUID = 1L;
    Logger log = Logger.getLogger(CompositeSearchServlet.class);

    @Override
    protected void executeAjax(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, User user, JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {

        //取得登入人員資訊
        argJsonObj.put("userVO", user);
        ResourceBundle resource = ResourceBundle.getBundle("npa");
        //AesCrypt crypt = new AesCrypt(resource.getString("crypt"));
        JSONObject jObject;
        JSONObject tempObj;
        JSONArray resultDataArray = new JSONArray();
        JSONArray resultDataArray1 = new JSONArray();
        ExportUtil exportUtil = new ExportUtil();
        CachedRowSet crs1 = null;
        CachedRowSet crs2 = null;
        CachedRowSet crs3 = null;
        NPALOG2Util NPALOG2 = new NPALOG2Util();
        HttpSession Opsession = null;
        OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
        OPDT_I_PU_DETAIL iPuDetailDao = new OPDT_I_PU_DETAIL();
        OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
        JSONArray resultjArray = new JSONArray();
        ArrayList<HashMap> mapList = new ArrayList<HashMap>();
        String[] delId = null;

        boolean result;
        switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {
            //region OP06A01Q_01.jsp ------Start	拾得物綜合查詢
            case "npalog":
                resultDataArray = new JSONArray();
                String[] tmplogVals = argJsonObj.get("logVals").toString().split("@");
                for (int i = 0; i < tmplogVals.length; i++) {
                    String[] tmplogVals2 = tmplogVals[i].toString().split(",");
                    //System.out.println("CompositeSearchServlet_tmplogVals[i].toString()____"+tmplogVals[i].toString());
                    tempObj = new JSONObject();
                    //202403 (ERROR 1)，不具名，身份証字號=null的錯誤
                    for (int j = 0; j < tmplogVals2.length; j++) {
                        //202403 split("符號"，限制)
                        //System.out.println("tmplogVals2[j]___"+j+"___"+tmplogVals2[j]);
                        tempObj.put(tmplogVals2[j].toString().split("=",2)[0], tmplogVals2[j].toString().split("=",2)[1]);
                    }
                    resultDataArray.put(tempObj);
                }
                String[] tmplogCols = argJsonObj.get("logCols").toString().split("@");
//                        logCols = new LinkedHashMap<String, String>();
                LinkedHashMap logCols = new LinkedHashMap<String, String>();

                for (int i = 0; i < tmplogCols.length; i++) {
                    //202403
                    logCols.put(tmplogCols[i].toString().split("=",2)[0], tmplogCols[i].toString().split("=",2)[1]);
                }
                String searchCriteria;
                if (argJsonObj.has("searchCriteria") && !argJsonObj.getString("searchCriteria").equals("")) {
                    searchCriteria = argJsonObj.getString("searchCriteria");
                } else {
                    searchCriteria = GetFrontBasic(argJsonObj);
                }
                String OP_PUPO_IDN = "",
                 OP_PUPO_NAME = "",
                 OP_AC_RCNO = "",
                 Page = "拾得遺失物綜合查詢-OP06A01Q_01";
                if (argJsonObj.has("OP_PUPO_IDN") && !argJsonObj.getString("OP_PUPO_IDN").equals("")) {
                    OP_PUPO_IDN = argJsonObj.getString("OP_PUPO_IDN");
                }
                if (argJsonObj.has("OP_PUPO_NAME") && !argJsonObj.getString("OP_PUPO_NAME").equals("")) {
                    OP_PUPO_NAME = argJsonObj.getString("OP_PUPO_NAME");
                }
                if (argJsonObj.has("OP_AC_RCNO") && !argJsonObj.getString("OP_AC_RCNO").equals("")) {
                    OP_AC_RCNO = argJsonObj.getString("OP_AC_RCNO");
                }
                if (argJsonObj.has("Page") && !argJsonObj.getString("Page").equals("")) {
                    Page = argJsonObj.getString("Page");
                }
//202403  
//            NPALOG2 = new NPALOG2Util();
//            NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFrontBasic(argJsonObj), argJsonObj.getString("OP_PUPO_IDN"), argJsonObj.getString("OP_PUPO_NAME"), "", resultDataArray, this.GetLogForBasicQuery(), "拾得遺失物綜合查詢-OP06A01Q_01", user, user.getUserName(), user.getUnitName(), "", "", "", "", "", argJsonObj.getString("OP_AC_RCNO"));

//            FUNCTION WriteQueryLog_New(
//            LOGOPTYPE opType, String filter, String qId, String qName, 
//            String qBirthday, JSONArray jArray, HashMap<String, String> logCols, String functionName,
//            User userVO, String actUser, String actUnit, String actUsage,
//            String OPR_KIND, String OPR_CASEID, String QND_PHONE, String QND_IMEI, 
//            String QND_CASENO)
                
//              NPALOG2.WriteQueryLog_New(
//                        LOGOPTYPE.Q, searchCriteria, OP_PUPO_IDN, OP_PUPO_NAME, 
//                        "", resultDataArray, logCols, Page, 
//                        user, user.getUserName(), user.getUnitName(), "",
//                        "", "", "", "", OP_AC_RCNO);

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG                  
//                NPALOG2 = new NPALOG2Util();                
//                NPALOG2.WriteQueryLog_New(
//                        LOGOPTYPE.Q, searchCriteria, OP_PUPO_IDN, OP_PUPO_NAME, 
//                        "", resultDataArray, logCols, Page, 
//                        user, user.getUserName(), user.getUnitName(), 
//                        argJsonObj.getString("OPR_KIND"),
//                        argJsonObj.getString("OPR_PURP"), 
//                        "", "", "", OP_AC_RCNO);                
                
                break;
            case "CompositeSearchForBasic":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                ArrayList<HashMap> tmpList = new ArrayList();
                tmpList = iPuBasicDao.CompositeSearchForBasic(argJsonObj);
//                            crs1 = iPuBasicDao.CompositeSearchForBasic(argJsonObj);
//                            while (crs1.next()) {
                OP_PUPO_IDN = "";
                OP_PUPO_NAME = "";
                OP_AC_RCNO = "";
                String BASIC_SEQ = "",
                 OP_AC_UNIT_NM = "",
                 OP_PUOJ_NM = "",
                 OP_PU_DTTM = "",
                 OP_PU_PLACE = "",
                 OP_CURSTAT_NM = "",
                 OP_PU_YN_OV500 = "",
                 OP_PUPO_PHONENO = "";
                if (!tmpList.isEmpty()) {
//                                for(int i = 1; i <= tmpList.size(); i++){
//                                    HashMap map = new HashMap();
//                                    map.put("BASIC_SEQ", tmpList.get(i-1).get("OP_SEQ_NO") ); //基本資料序號
//                                    map.put("OP_AC_RCNO", tmpList.get(i-1).get("OP_AC_RCNO") ); //收據編號
//                                    map.put("OP_AC_UNIT_NM", tmpList.get(i-1).get("OP_AC_UNIT_NM") ); //受理單位
//                                    map.put("OP_PUOJ_NM", tmpList.get(i-1).get("OP_PUOJ_NM") ); //物品名稱
//                                    map.put("OP_PUPO_NAME", tmpList.get(i-1).get("OP_PUPO_NAME") ); //拾得人姓名
//                                    map.put("OP_PUPO_IDN", tmpList.get(i-1).get("OP_PUPO_IDN") ); //身分證/其他證號
//                                    map.put("OP_PU_DTTM", DateUtil.to7TwDateTime(tmpList.get(i-1).get("OP_PU_DATE").toString(), tmpList.get(i-1).get("OP_PU_TIME").toString()) ); //拾得日期時間
//                                    map.put("OP_PU_PLACE", tmpList.get(i-1).get("OP_PU_CITY_NM").toString() + tmpList.get(i-1).get("OP_PU_TOWN_NM").toString() + tmpList.get(i-1).get("OP_PU_PLACE").toString() ); //拾得地點
//                                    map.put("OP_CURSTAT_NM", tmpList.get(i-1).get("OP_CURSTAT_NM") ); //目前狀態
//                                    if( tmpList.get(i-1).get("OP_PU_YN_OV500").equals("1") ){
//                                        map.put("OP_PU_YN_OV500", "是" ); //伍佰元以上
//                                    }else{
//                                        map.put("OP_PU_YN_OV500", "否" ); //伍佰元以下
//                                    }
//                                    mapList.add(map);
//                                }
                    if (tmpList.size() != 1) { //在table中找到兩筆以上資料
                        for (int i = 0; i < tmpList.size(); i++) {
                            HashMap basicMap = (HashMap) tmpList.get(i);
                            if (!BASIC_SEQ.equals(basicMap.get("OP_SEQ_NO").toString())) { //只有一筆資料
                                if (!BASIC_SEQ.equals("")) {
                                    jObject = new JSONObject();
                                    jObject.put("BASIC_SEQ", BASIC_SEQ); //基本資料序號
                                    jObject.put("OP_AC_RCNO", OP_AC_RCNO); //收據編號
                                    jObject.put("OP_AC_UNIT_NM", OP_AC_UNIT_NM); //受理單位
                                    OP_PUOJ_NM = OP_PUOJ_NM.length() <= 30 ? OP_PUOJ_NM : OP_PUOJ_NM.substring(0, 30) + "...";
                                    jObject.put("OP_PUOJ_NM", OP_PUOJ_NM); //物品名稱
                                    jObject.put("OP_PUPO_NAME", OP_PUPO_NAME); //拾得人姓名
                                    jObject.put("OP_PUPO_IDN", OP_PUPO_IDN); //身分證/其他證號
                                    jObject.put("OP_PU_DTTM", OP_PU_DTTM); //拾得日期時間
                                    jObject.put("OP_PU_PLACE", OP_PU_PLACE); //拾得地點
                                    jObject.put("OP_CURSTAT_NM", OP_CURSTAT_NM); //目前狀態
                                    jObject.put("OP_PU_YN_OV500", OP_PU_YN_OV500); //伍佰元以上
                                    jObject.put("OP_PUPO_PHONENO", OP_PUPO_PHONENO); //電話

                                    resultDataArray.put(jObject);
                                }
                                BASIC_SEQ = basicMap.get("OP_SEQ_NO").toString(); //基本資料序號
                                OP_AC_RCNO = basicMap.get("OP_AC_RCNO").toString(); //收據編號
                                OP_AC_UNIT_NM = basicMap.get("OP_AC_UNIT_NM").toString(); //受理單位
                                OP_PUOJ_NM = basicMap.get("OP_PUOJ_NM").toString(); //物品名稱
                                OP_PUPO_NAME = basicMap.get("OP_PUPO_NAME").toString(); //拾得人姓名
                                OP_PUPO_IDN = basicMap.get("OP_PUPO_IDN").toString(); //身分證/其他證號
                                OP_PUPO_PHONENO = basicMap.get("OP_PUPO_PHONENO").toString(); //電話
                                OP_PU_DTTM = DateUtil.to7TwDateTime(basicMap.get("OP_PU_DATE").toString(), basicMap.get("OP_PU_TIME").toString()); //拾得日期時間
                                OP_PU_PLACE = basicMap.get("OP_PU_CITY_NM").toString() + basicMap.get("OP_PU_TOWN_NM").toString() + basicMap.get("OP_PU_PLACE").toString(); //拾得地點
                                OP_CURSTAT_NM = basicMap.get("OP_CURSTAT_NM").toString(); //目前狀態
                                if (basicMap.get("OP_PU_YN_OV500").equals("1")) {
                                    OP_PU_YN_OV500 = "是"; //伍佰元以上
                                } else {
                                    OP_PU_YN_OV500 = "否"; //伍佰元以上
                                }

                            } else { //不只有一筆資料
                                OP_PUOJ_NM = OP_PUOJ_NM + "、" + basicMap.get("OP_PUOJ_NM").toString(); //物品名稱
                            }
                            if (i == (tmpList.size() - 1)) {//最後一筆資料直接儲存
                                jObject = new JSONObject();
                                jObject.put("BASIC_SEQ", BASIC_SEQ); //基本資料序號
                                jObject.put("OP_AC_RCNO", OP_AC_RCNO); //收據編號
                                jObject.put("OP_AC_UNIT_NM", OP_AC_UNIT_NM); //受理單位
                                OP_PUOJ_NM = OP_PUOJ_NM.length() <= 30 ? OP_PUOJ_NM : OP_PUOJ_NM.substring(0, 30) + "...";
                                jObject.put("OP_PUOJ_NM", OP_PUOJ_NM); //物品名稱
                                jObject.put("OP_PUPO_NAME", OP_PUPO_NAME); //拾得人姓名
                                jObject.put("OP_PUPO_IDN", OP_PUPO_IDN); //身分證/其他證號
                                jObject.put("OP_PU_DTTM", OP_PU_DTTM); //拾得日期時間
                                jObject.put("OP_PU_PLACE", OP_PU_PLACE); //拾得地點
                                jObject.put("OP_CURSTAT_NM", OP_CURSTAT_NM); //目前狀態
                                jObject.put("OP_PU_YN_OV500", OP_PU_YN_OV500); //伍佰元以上
                                jObject.put("OP_PUPO_PHONENO", OP_PUPO_PHONENO); //電話

                                resultDataArray.put(jObject);
                            }
                        }
                    } else { //只有一筆資料
                        HashMap basicMap = (HashMap) tmpList.get(0);
                        jObject = new JSONObject();
                        jObject.put("BASIC_SEQ", basicMap.get("OP_SEQ_NO").toString()); //基本資料序號
                        jObject.put("OP_AC_RCNO", basicMap.get("OP_AC_RCNO").toString()); //收據編號
                        jObject.put("OP_AC_UNIT_NM", basicMap.get("OP_AC_UNIT_NM").toString()); //受理單位
                        jObject.put("OP_PUOJ_NM", basicMap.get("OP_PUOJ_NM").toString()); //物品名稱
                        jObject.put("OP_PUPO_NAME", basicMap.get("OP_PUPO_NAME").toString()); //拾得人姓名
                        jObject.put("OP_PUPO_IDN", basicMap.get("OP_PUPO_IDN").toString()); //身分證/其他證號
                        jObject.put("OP_PUPO_PHONENO", basicMap.get("OP_PUPO_PHONENO").toString()); //電話
                        jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(basicMap.get("OP_PU_DATE").toString(), basicMap.get("OP_PU_TIME").toString())); //拾得日期時間
                        jObject.put("OP_PU_PLACE", basicMap.get("OP_PU_CITY_NM").toString() + basicMap.get("OP_PU_TOWN_NM").toString() + basicMap.get("OP_PU_PLACE").toString()); //拾得地點
                        jObject.put("OP_CURSTAT_NM", basicMap.get("OP_CURSTAT_NM").toString()); //目前狀態
                        if (basicMap.get("OP_PU_YN_OV500").equals("1")) {
                            jObject.put("OP_PU_YN_OV500", "是"); //伍佰元以上
                        } else {
                            jObject.put("OP_PU_YN_OV500", "否"); //伍佰元以上
                        }

                        resultDataArray.put(jObject);
                    }
                }
//                            String BASIC_SEQ = "", OP_AC_RCNO = "", OP_AC_UNIT_NM = "", OP_PUOJ_NM = "", OP_PUPO_NAME = "", OP_PUPO_IDN = ""
//                                    , OP_PU_DTTM = "", OP_PU_PLACE = "", OP_CURSTAT_NM = "", OP_PU_YN_OV500 = "";
//                            if(!mapList.isEmpty()){
//                                if(mapList.size()!=1){ //在table中找到兩筆以上資料
//                                    for(int i = 0; i < mapList.size(); i++){
//                                        HashMap basicMap = (HashMap) mapList.get(i);
//                                        if( !BASIC_SEQ.equals(basicMap.get("BASIC_SEQ").toString()) ){ //只有一筆資料
//                                            if( !BASIC_SEQ.equals("") ){
//                                                jObject = new JSONObject();
//                                                jObject.put("BASIC_SEQ", BASIC_SEQ ); //基本資料序號
//                                                jObject.put("OP_AC_RCNO", OP_AC_RCNO ); //收據編號
//                                                jObject.put("OP_AC_UNIT_NM", OP_AC_UNIT_NM ); //受理單位
//                                                OP_PUOJ_NM = OP_PUOJ_NM.length() <= 30 ? OP_PUOJ_NM : OP_PUOJ_NM.substring(0,30)+"...";
//                                                jObject.put("OP_PUOJ_NM", OP_PUOJ_NM ); //物品名稱
//                                                jObject.put("OP_PUPO_NAME", OP_PUPO_NAME ); //拾得人姓名
//                                                jObject.put("OP_PUPO_IDN", OP_PUPO_IDN ); //身分證/其他證號
//                                                jObject.put("OP_PU_DTTM", OP_PU_DTTM ); //拾得日期時間
//                                                jObject.put("OP_PU_PLACE", OP_PU_PLACE ); //拾得地點
//                                                jObject.put("OP_CURSTAT_NM", OP_CURSTAT_NM ); //目前狀態
//                                                jObject.put("OP_PU_YN_OV500", OP_PU_YN_OV500 ); //伍佰元以上
//
//                                                resultDataArray.put(jObject);                                   
//                                            }
//                                            BASIC_SEQ = basicMap.get("BASIC_SEQ").toString(); //基本資料序號
//                                            OP_AC_RCNO = basicMap.get("OP_AC_RCNO").toString(); //收據編號
//                                            OP_AC_UNIT_NM = basicMap.get("OP_AC_UNIT_NM").toString(); //受理單位
//                                            OP_PUOJ_NM = basicMap.get("OP_PUOJ_NM").toString(); //物品名稱
//                                            OP_PUPO_NAME = basicMap.get("OP_PUPO_NAME").toString(); //拾得人姓名
//                                            OP_PUPO_IDN = basicMap.get("OP_PUPO_IDN").toString(); //身分證/其他證號
//                                            OP_PU_DTTM = basicMap.get("OP_PU_DTTM").toString(); //拾得日期時間
//                                            OP_PU_PLACE = basicMap.get("OP_PU_PLACE").toString(); //拾得地點
//                                            OP_CURSTAT_NM = basicMap.get("OP_CURSTAT_NM").toString(); //目前狀態
//                                            OP_PU_YN_OV500 = basicMap.get("OP_PU_YN_OV500").toString(); //伍佰元以上
//                                            
//                                        }else{ //不只有一筆資料
//                                            OP_PUOJ_NM = OP_PUOJ_NM + "、" + basicMap.get("OP_PUOJ_NM").toString(); //物品名稱
//                                        }
//                                        if(i == (mapList.size()-1)){//最後一筆資料直接儲存
//                                            jObject = new JSONObject();
//                                            jObject.put("BASIC_SEQ", BASIC_SEQ ); //基本資料序號
//                                            jObject.put("OP_AC_RCNO", OP_AC_RCNO ); //收據編號
//                                            jObject.put("OP_AC_UNIT_NM", OP_AC_UNIT_NM ); //受理單位
//                                            OP_PUOJ_NM = OP_PUOJ_NM.length() <= 30 ? OP_PUOJ_NM : OP_PUOJ_NM.substring(0,30)+"...";
//                                            jObject.put("OP_PUOJ_NM", OP_PUOJ_NM ); //物品名稱
//                                            jObject.put("OP_PUPO_NAME", OP_PUPO_NAME ); //拾得人姓名
//                                            jObject.put("OP_PUPO_IDN", OP_PUPO_IDN ); //身分證/其他證號
//                                            jObject.put("OP_PU_DTTM", OP_PU_DTTM ); //拾得日期時間
//                                            jObject.put("OP_PU_PLACE", OP_PU_PLACE ); //拾得地點
//                                            jObject.put("OP_CURSTAT_NM", OP_CURSTAT_NM ); //目前狀態
//                                            jObject.put("OP_PU_YN_OV500", OP_PU_YN_OV500 ); //伍佰元以上
//
//                                            resultDataArray.put(jObject);
//                                        }
//                                    } 
//                                }else{ //只有一筆資料
//                                    HashMap basicMap = (HashMap) mapList.get(0);
//                                    jObject = new JSONObject();
//                                    jObject.put("BASIC_SEQ", basicMap.get("BASIC_SEQ").toString() ); //基本資料序號
//                                    jObject.put("OP_AC_RCNO", basicMap.get("OP_AC_RCNO").toString() ); //收據編號
//                                    jObject.put("OP_AC_UNIT_NM", basicMap.get("OP_AC_UNIT_NM").toString() ); //受理單位
//                                    jObject.put("OP_PUOJ_NM", basicMap.get("OP_PUOJ_NM").toString() ); //物品名稱
//                                    jObject.put("OP_PUPO_NAME", basicMap.get("OP_PUPO_NAME").toString() ); //拾得人姓名
//                                    jObject.put("OP_PUPO_IDN", basicMap.get("OP_PUPO_IDN").toString() ); //身分證/其他證號
//                                    jObject.put("OP_PU_DTTM", basicMap.get("OP_PU_DTTM").toString() ); //拾得日期時間
//                                    jObject.put("OP_PU_PLACE", basicMap.get("OP_PU_PLACE").toString() ); //拾得地點
//                                    jObject.put("OP_CURSTAT_NM", basicMap.get("OP_CURSTAT_NM").toString() ); //目前狀態
//                                    jObject.put("OP_PU_YN_OV500", basicMap.get("OP_PU_YN_OV500").toString() ); //伍佰元以上
//                                    
//                                    resultDataArray.put(jObject);
//                                }
//                            }

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
//                NPALOG2 = new NPALOG2Util();
//                            NPALOG2.WriteQueryLog_New(
//                                    LOGOPTYPE.Q, GetFrontBasic(argJsonObj), 
//                                    argJsonObj.getString("OP_PUPO_IDN"), argJsonObj.getString("OP_PUPO_NAME"), 
//                                    "", resultDataArray, this.GetLogForBasicQuery(), "拾得遺失物綜合查詢-OP06A01Q_01", 
//                                    user, user.getUserName(), user.getUnitName(), 
//                                    "",
//                                    "", 
//                                    "", "", "", 
//                                    argJsonObj.getString("OP_AC_RCNO"));
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;

            //region OP06A01Q_01.jsp ------End	拾得物綜合查詢
            case "CompositeSearchForBasicSearch":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                tmpList = new ArrayList();
                tmpList = iPuBasicDao.CompositeSearchForBasic(argJsonObj);
                OP_PUPO_IDN = "";
                OP_PUPO_NAME = "";
                OP_AC_RCNO = "";
                BASIC_SEQ = "";
                 OP_AC_UNIT_NM = "";
                 OP_PUOJ_NM = "";
                 OP_PU_DTTM = "";
                 OP_PU_PLACE = "";
                 OP_CURSTAT_NM = "";
                 OP_PU_YN_OV500 = "";
                 OP_PUPO_PHONENO = "";
                if (!tmpList.isEmpty()) {
                    if (tmpList.size() != 1) { //在table中找到兩筆以上資料
                        for (int i = 0; i < tmpList.size(); i++) {
                            HashMap basicMap = (HashMap) tmpList.get(i);
                            if (!BASIC_SEQ.equals(basicMap.get("OP_SEQ_NO").toString())) { //只有一筆資料
                                if (!BASIC_SEQ.equals("")) {
                                    jObject = new JSONObject();
                                    jObject.put("BASIC_SEQ", BASIC_SEQ); //基本資料序號
                                    jObject.put("OP_AC_RCNO", OP_AC_RCNO); //收據編號
                                    jObject.put("OP_AC_UNIT_NM", OP_AC_UNIT_NM); //受理單位
                                    OP_PUOJ_NM = OP_PUOJ_NM.length() <= 30 ? OP_PUOJ_NM : OP_PUOJ_NM.substring(0, 30) + "...";
                                    jObject.put("OP_PUOJ_NM", OP_PUOJ_NM); //物品名稱
                                    jObject.put("OP_PUPO_NAME", OP_PUPO_NAME); //拾得人姓名
                                    jObject.put("OP_PUPO_IDN", OP_PUPO_IDN); //身分證/其他證號
                                    jObject.put("OP_PU_DTTM", OP_PU_DTTM); //拾得日期時間
                                    jObject.put("OP_PU_PLACE", OP_PU_PLACE); //拾得地點
                                    jObject.put("OP_CURSTAT_NM", OP_CURSTAT_NM); //目前狀態
                                    jObject.put("OP_PU_YN_OV500", OP_PU_YN_OV500); //伍佰元以上
                                    jObject.put("OP_PUPO_PHONENO", OP_PUPO_PHONENO); //電話

                                    resultDataArray.put(jObject);
                                }
                                BASIC_SEQ = basicMap.get("OP_SEQ_NO").toString(); //基本資料序號
                                OP_AC_RCNO = basicMap.get("OP_AC_RCNO").toString(); //收據編號
                                OP_AC_UNIT_NM = basicMap.get("OP_AC_UNIT_NM").toString(); //受理單位
                                OP_PUOJ_NM = basicMap.get("OP_PUOJ_NM").toString(); //物品名稱
                                OP_PUPO_NAME = basicMap.get("OP_PUPO_NAME").toString(); //拾得人姓名
                                OP_PUPO_IDN = basicMap.get("OP_PUPO_IDN").toString(); //身分證/其他證號
                                OP_PUPO_PHONENO = basicMap.get("OP_PUPO_PHONENO").toString(); //電話
                                OP_PU_DTTM = DateUtil.to7TwDateTime(basicMap.get("OP_PU_DATE").toString(), basicMap.get("OP_PU_TIME").toString()); //拾得日期時間
                                OP_PU_PLACE = basicMap.get("OP_PU_CITY_NM").toString() + basicMap.get("OP_PU_TOWN_NM").toString() + basicMap.get("OP_PU_PLACE").toString(); //拾得地點
                                OP_CURSTAT_NM = basicMap.get("OP_CURSTAT_NM").toString(); //目前狀態
                                if (basicMap.get("OP_PU_YN_OV500").equals("1")) {
                                    OP_PU_YN_OV500 = "是"; //伍佰元以上
                                } else {
                                    OP_PU_YN_OV500 = "否"; //伍佰元以上
                                }

                            } else { //不只有一筆資料
                                OP_PUOJ_NM = OP_PUOJ_NM + "、" + basicMap.get("OP_PUOJ_NM").toString(); //物品名稱
                            }
                            if (i == (tmpList.size() - 1)) {//最後一筆資料直接儲存
                                jObject = new JSONObject();
                                jObject.put("BASIC_SEQ", BASIC_SEQ); //基本資料序號
                                jObject.put("OP_AC_RCNO", OP_AC_RCNO); //收據編號
                                jObject.put("OP_AC_UNIT_NM", OP_AC_UNIT_NM); //受理單位
                                OP_PUOJ_NM = OP_PUOJ_NM.length() <= 30 ? OP_PUOJ_NM : OP_PUOJ_NM.substring(0, 30) + "...";
                                jObject.put("OP_PUOJ_NM", OP_PUOJ_NM); //物品名稱
                                jObject.put("OP_PUPO_NAME", OP_PUPO_NAME); //拾得人姓名
                                jObject.put("OP_PUPO_IDN", OP_PUPO_IDN); //身分證/其他證號
                                jObject.put("OP_PU_DTTM", OP_PU_DTTM); //拾得日期時間
                                jObject.put("OP_PU_PLACE", OP_PU_PLACE); //拾得地點
                                jObject.put("OP_CURSTAT_NM", OP_CURSTAT_NM); //目前狀態
                                jObject.put("OP_PU_YN_OV500", OP_PU_YN_OV500); //伍佰元以上
                                jObject.put("OP_PUPO_PHONENO", OP_PUPO_PHONENO); //電話

                                resultDataArray.put(jObject);
                            }
                        }
                    } else { //只有一筆資料
                        HashMap basicMap = (HashMap) tmpList.get(0);
                        jObject = new JSONObject();
                        jObject.put("BASIC_SEQ", basicMap.get("OP_SEQ_NO").toString()); //基本資料序號
                        jObject.put("OP_AC_RCNO", basicMap.get("OP_AC_RCNO").toString()); //收據編號
                        jObject.put("OP_AC_UNIT_NM", basicMap.get("OP_AC_UNIT_NM").toString()); //受理單位
                        jObject.put("OP_PUOJ_NM", basicMap.get("OP_PUOJ_NM").toString()); //物品名稱
                        jObject.put("OP_PUPO_NAME", basicMap.get("OP_PUPO_NAME").toString()); //拾得人姓名
                        jObject.put("OP_PUPO_IDN", basicMap.get("OP_PUPO_IDN").toString()); //身分證/其他證號
                        jObject.put("OP_PUPO_PHONENO", basicMap.get("OP_PUPO_PHONENO").toString()); //電話
                        jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(basicMap.get("OP_PU_DATE").toString(), basicMap.get("OP_PU_TIME").toString())); //拾得日期時間
                        jObject.put("OP_PU_PLACE", basicMap.get("OP_PU_CITY_NM").toString() + basicMap.get("OP_PU_TOWN_NM").toString() + basicMap.get("OP_PU_PLACE").toString()); //拾得地點
                        jObject.put("OP_CURSTAT_NM", basicMap.get("OP_CURSTAT_NM").toString()); //目前狀態
                        if (basicMap.get("OP_PU_YN_OV500").equals("1")) {
                            jObject.put("OP_PU_YN_OV500", "是"); //伍佰元以上
                        } else {
                            jObject.put("OP_PU_YN_OV500", "否"); //伍佰元以上
                        }

                        resultDataArray.put(jObject);
                    }
                }
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFrontBasic(argJsonObj), 
                        argJsonObj.getString("OP_PUPO_IDN"), argJsonObj.getString("OP_PUPO_NAME"), 
                        "", resultDataArray, this.GetLogForBasicQuery(), "拾得遺失物綜合查詢-OP06A01Q_01", 
                        user, user.getUserName(), user.getUnitName(),
                        argJsonObj.getString("OPR_KIND"),  //202403 
                        argJsonObj.getString("OPR_PURP"),   
                        "", "", "", argJsonObj.getString("OP_AC_RCNO"));
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            //region OP06A02Q_01.jsp ------Start	拾得物跨轄查詢
            case "CompositeSearchForClaim":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                crs1 = iPuClaimDao.CompositeSearchForClaim(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_PUCP_NAME", crs1.getString("OP_PUCP_NAME")); //認領人姓名
                    jObject.put("OP_PUCP_IDN", crs1.getString("OP_PUCP_IDN")); //認領人身分證號
                    jObject.put("OP_PUCP_PHONENO", crs1.getString("OP_PUCP_PHONENO")); //聯絡電話
                    jObject.put("OP_PUCP_EMAIL", crs1.getString("OP_PUCP_EMAIL")); //電子信箱
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_FM_DATE", DateUtil.to7TwDateTime(crs1.getString("OP_FM_DATE"))); //填單日期
                    jObject.put("OP_AC_UNIT_NM", crs1.getString("OP_AC_UNIT_NM")); //承辦單位
                    jObject.put("OP_FS_STAFF_NM", crs1.getString("OP_FS_STAFF_NM")); //結案人員
                    jObject.put("OP_FS_DATE", DateUtil.to7TwDateTime(crs1.getString("OP_FS_DATE"))); //結案日期

                    resultDataArray.put(jObject);
                }
// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG       
// 202504 預設查詢不寫NPALOG，但OP06A02Q_01.jsp 是 『拾得物跨轄查詢』應要寫LOG
                NPALOG2 = new NPALOG2Util();
               
                NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFront06A02Q(argJsonObj), 
                                          argJsonObj.getString("OP_PUCP_IDN"), argJsonObj.getString("OP_PUCP_NAME"), 
                                          "", resultDataArray, this.GetLogFor0602Query(), 
                                          "拾得遺失物跨轄查詢OP06A02Q_01", 
                                          user, user.getUserName(), user.getUnitName(),
                                          argJsonObj.getString("OPR_KIND"),
                                          argJsonObj.getString("OPR_PURP"), 
                                          "", "", "", "");
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            //region OP06A02Q_01.jsp ------End	拾得物跨轄查詢

        }
    }

    private String getOP_PUOJ_NM(String OP_BASIC_SEQ_NO) {
        ArrayList qsPara = new ArrayList();
        String puojNm = "";
        String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT"
                + " FROM OPDT_I_PU_DETAIL "
                + " WHERE OP_BASIC_SEQ_NO = ? ";
        qsPara.add(Integer.parseInt(OP_BASIC_SEQ_NO));

        try {
            ArrayList<HashMap> detaillist = this.pexecuteQuery(sql, qsPara.toArray());

            if (!detaillist.isEmpty()) {
                for (int i = 1; i <= detaillist.size(); i++) {
                    puojNm = puojNm + detaillist.get(i - 1).get("OP_PUOJ_NM");
                    if (i <= detaillist.size() - 1) {
                        puojNm = puojNm + ",";
                    }
                }
            }
            puojNm = puojNm.length() <= 30 ? puojNm : puojNm.substring(0, 30) + "...";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return puojNm;
    }

    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement
     *
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return
     */
    protected ArrayList<HashMap> pexecuteQuery(String sql, Object[] objs) {
        return DBUtil.getInstance().pexecuteQuery(sql, objs);
    }

    //查詢條件欄位
    private String GetFrontBasic(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        ///受理單位
        if (!jsonObject.getString("OP_UNITLEVEL4").equals("")) {
            returnString.append(String.format("&%s=%s", "受理單位", jsonObject.getString("OP_UNITLEVEL4_NM")));
        } else if (!jsonObject.getString("OP_UNITLEVEL3").equals("")) {
            returnString.append(String.format("&%s=%s", "受理單位", jsonObject.getString("OP_UNITLEVEL3_NM")));
        } else if (!jsonObject.getString("OP_UNITLEVEL2").equals("")) {
            returnString.append(String.format("&%s=%s", "受理單位", jsonObject.getString("OP_UNITLEVEL2_NM")));
        }
        if (!jsonObject.getString("OP_AC_STAFF_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "受理人員", jsonObject.getString("OP_AC_STAFF_NM")));
        }
        if (!jsonObject.getString("OP_AC_RCNO").equals("")) {
            returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
        }
        if (!jsonObject.getString("OP_PUPO_NAME").equals("")) {
            returnString.append(String.format("&%s=%s", "拾得人姓名", jsonObject.getString("OP_PUPO_NAME")));
        }
        if (!jsonObject.getString("OP_PUPO_GENDER_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "性別", jsonObject.getString("OP_PUPO_GENDER_NM")));
        }
        if (!jsonObject.getString("OP_PUPO_IDN_TP_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "證號別", jsonObject.getString("OP_PUPO_IDN_TP_NM")));
        }
        if (!jsonObject.getString("OP_PUPO_IDN").equals("")) {
            returnString.append(String.format("&%s=%s", "身分證/其他證號", jsonObject.getString("OP_PUPO_IDN")));
        }
        if (!jsonObject.getString("OP_PUOJ_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "物品名稱", jsonObject.getString("OP_PUOJ_NM")));
        }
        if (!jsonObject.getString("OP_PU_DATE_S").equals("")) {
            returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_PU_DATE_E").equals("")) {
            returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_AC_DATE_S").equals("")) {
            returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_AC_DATE_E").equals("")) {
            returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_PUOJ_ATTR_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "物品屬性", jsonObject.getString("OP_PUOJ_ATTR_NM")));
        }
        if (!jsonObject.getString("OP_CURSTAT_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "目前狀態", jsonObject.getString("OP_CURSTAT_NM")));
        }
        if (!jsonObject.getString("OP_FS_REC_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "結案紀錄", jsonObject.getString("OP_FS_REC_NM")));
        }
        if (!jsonObject.getString("OP_PU_YN_OV500").equals("")) {
            if (jsonObject.getString("OP_PU_YN_OV500").equals("1")) {
                returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元以上"));
            } else if (jsonObject.getString("OP_PU_YN_OV500").equals("0")) {
                returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元(含)以下"));
            }
        }

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLogForBasicQuery() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_AC_RCNO", "收據編號");
        logCols.put("OP_AC_UNIT_NM", "受理單位");
//                    logCols.put("OP_PUPO_TP_NM", "拾得人類別");
//                    logCols.put("OP_PUOJ_ATTR_NM", "物品屬性");
        logCols.put("OP_PUOJ_NM", "物品名稱");
        logCols.put("OP_PUPO_NAME", "拾得人姓名");
        logCols.put("OP_PUPO_IDN", "身分證其他證號");
        logCols.put("OP_PU_DTTM", "拾得日期時間");
        logCols.put("OP_PU_PLACE", "拾得地點");
        logCols.put("OP_CURSTAT_NM", "目前狀態");
        logCols.put("OP_PU_YN_OV500", "價值伍佰元");

        return logCols;
    }
    //查詢條件欄位(跨轄查詢_認領人)

    private String GetFront06A02Q(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        if (!jsonObject.getString("OP_STATUS_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "查詢類別", jsonObject.getString("OP_STATUS_NM")));
        }
        if (!jsonObject.getString("OP_PUCP_NAME").equals("")) {
            returnString.append(String.format("&%s=%s", "認領人姓名", jsonObject.getString("OP_PUCP_NAME")));
        }
        if (!jsonObject.getString("OP_PUCP_IDN").equals("")) {
            returnString.append(String.format("&%s=%s", "認領人身分證號", jsonObject.getString("OP_PUCP_IDN")));
        }

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLogFor0602Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_PUCP_NAME", "認領人姓名");
        logCols.put("OP_PUCP_IDN", "認領人身分證號");
        logCols.put("OP_PUCP_PHONENO", "聯絡電話");
        logCols.put("OP_PUCP_EMAIL", "電子信箱");
        logCols.put("OP_AC_RCNO", "收據編號");
        logCols.put("OP_FM_DATE", "填單日期");
        logCols.put("OP_AC_UNIT_NM", "承辦單位");
        logCols.put("OP_FS_STAFF_NM", "結案人員");
        logCols.put("OP_FS_DATE", "結案日期");

        return logCols;
    }

}
