package npa.op.control;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
import npa.op.dao.OPDT_I_FNSH;
import npa.op.dao.OPDT_I_PU_BASIC;
import npa.op.dao.OPDT_I_PU_CLAIM;
import npa.op.dao.OPDT_I_PU_DETAIL;
//import npa.op.util.AesCrypt;
import npa.op.util.DateUtil;
import npa.op.util.ExportUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.util.StringUtil;
import static npa.op.util.StringUtil.replaceWhiteChar;
import npa.op.vo.User;

/**
 * 拾得遺失物受理基本資料、明細、認領
 */
@WebServlet("/PuMaintainServlet")
public class PuMaintainServlet extends AjaxBaseServlet {

    private static final long serialVersionUID = 1L;
    Logger log = Logger.getLogger(PuMaintainServlet.class);

    @Override
    protected void executeAjax(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, User user, JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {

        //取得登入人員資訊
        argJsonObj.put("userVO", user);
        ResourceBundle resource = ResourceBundle.getBundle("npa");
        //AesCrypt crypt = new AesCrypt(resource.getString("crypt"));
        JSONObject jObject;
        JSONArray resultDataArray = new JSONArray();
        JSONArray resultDataArray1 = new JSONArray();
        ExportUtil exportUtil = new ExportUtil();
        CachedRowSet crs1 = null;
        CachedRowSet crs2 = null;
        CachedRowSet crs3 = null;
        NPALOG2Util NPALOG2 = new NPALOG2Util();
        HttpSession Opsession = null;
        OPDT_E_NET_CLAIM ePuNetClmDao = new OPDT_E_NET_CLAIM();
        OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
        OPDT_I_PU_DETAIL iPuDetailDao = new OPDT_I_PU_DETAIL();
        OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
        OPDT_I_ANNOUNCE iAnnounceDao = new OPDT_I_ANNOUNCE();
        JSONArray resultjArray = new JSONArray();
        String[] delId = null;

        boolean result;
        switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {
            //region OP01A01Q_01.jsp ------Start	民眾網路認領訊息
            case "PuNetQueryForDown":
                ePuNetClmDao = OPDT_E_NET_CLAIM.getInstance();
                crs1 = ePuNetClmDao.PuNetQueryForDown(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("SEQ", crs1.getString("OP_SEQ_NO")); //序號
                    jObject.put("BASIC_SEQ", crs1.getString("OP_BASIC_SEQ_NO")); //基本資料序號
                    jObject.put("OP_FM_DATE", DateUtil.to7TwDateTime(crs1.getString("OP_FM_DATE"))); //填單日期
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_PUCP_NAME", crs1.getString("OP_PUCP_NAME")); //認領人姓名
                    jObject.put("OP_PUCP_IDN", crs1.getString("OP_PUCP_IDN")); //認領人證號
                    //jObject.put("OP_LOST_DATE",crs1.getString("OP_LOST_DATE"));
                    //jObject.put("OP_LOST_TIME",crs1.getString("OP_LOST_TIME"));
                    jObject.put("OP_LOST_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_LOST_DATE"), crs1.getString("OP_LOST_TIME"))); //遺失日期時間  //202311 (不知為何都是空白先MARK掉)
                    jObject.put("OP_LOST_PLACE", crs1.getString("OP_LOST_CITY_NM") + crs1.getString("OP_LOST_TOWN_NM") + crs1.getString("OP_LOST_PLACE")); //遺失地點
                    jObject.put("OP_LOST_OJ_DESC", crs1.getString("OP_LOST_OJ_DESC")); //物品描述
                    if (crs1.getString("OP_YN_LOSER").trim().equals("") || crs1.getString("OP_YN_LOSER").equals("0") || crs1.getString("OP_YN_LOSER") == null) {
                        jObject.put("OP_YN_LOSER", "否"); //是否審核(是否為有認領權人)
                    } else {
                        jObject.put("OP_YN_LOSER", "是"); //是否審核(是否為有認領權人)
                    }

                    jObject.put("OP_AC_DATE", DateUtil.to7TwDateTime(crs1.getString("OP_AC_DATE"))); //受理日期

                    resultDataArray.put(jObject);
                }
              //202403本來是上面兩個
              //NPALOG2 = new NPALOG2Util(); //202403
              //  NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFront01A01Q(argJsonObj), "", "", "", resultDataArray, this.GetLog0101Query(), "OP01A01Q_01", user, user.getUserName(), user.getUnitName(), ""); //202403
              //NPALOG2.WriteQueryLog_1(LOGOPTYPE.Q, GetFront01A01Q(argJsonObj), "", "", "", resultDataArray, this.GetLog0101Query(), "OP01A01Q_01", user, user.getUserName(), user.getUnitName(),crs1.getString("OPR_KIND"),crs1.getString("OPR_PURP"));  //202403 
     
                
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "checkLost":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                ePuNetClmDao = OPDT_E_NET_CLAIM.getInstance();
                boolean PuClaimResult = true; //是
                boolean ePuNetClmResult = true; //是
                crs1 = iPuClaimDao.checkLostForPuCaim(argJsonObj);
                if (!crs1.next()) {
                    PuClaimResult = false; //否
                }
                crs2 = ePuNetClmDao.checkLostForPuNetClm(argJsonObj);
                if (!crs2.next()) {
                    ePuNetClmResult = false; //否
                }

                if (PuClaimResult == true || ePuNetClmResult == true) {
                    result = true;
                } else {
                    result = false;
                }

                this.setFormData(returnJasonObj, result);
                break;
            case "checkYNPuClaim":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                result = true;
                crs1 = iPuClaimDao.checkYNPuClaim(argJsonObj);
                if (!crs1.next()) {
                    result = false;
                }

                this.setFormData(returnJasonObj, result);
                break;
            //region OP01A01Q_01.jsp ------End
            //region OP02A01Q_01.jsp ------Start	拾得遺失物受理新增
            case "checkStatusForBasic":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                jObject = new JSONObject();
                crs1 = iPuBasicDao.checkStatusForBasic(argJsonObj);
                String check = "N";
                jObject.put("CHECK", check); //沒有資料設為 N
                if (crs1.next()) {
                    if (crs1.getString("OP_CURSTAT_CD").equals("1")) { //處理中 設為 1
                        jObject.put("CHECK", "1");
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("2")) { //內部公告中,網路公告中 設為 2
                        jObject.put("CHECK", "2");
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("3")) { //公告期滿 設為 3
                        jObject.put("CHECK", "3");
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("4")) { //拾得人領回公告中 設為 4
                        jObject.put("CHECK", "4");
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("5")) { //拾得人領回公告期滿 設為 5
                        jObject.put("CHECK", "5");
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("6")) { //已結案 設為 6
                        jObject.put("CHECK", "6");
                    }
                }
                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "checkSameBasic":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                jObject = new JSONObject();
                crs1 = iPuBasicDao.checkSameBasic(argJsonObj);
                jObject.put("CHECK", true); //沒有資料設為 true
                if (crs1.next()) {
                    jObject.put("CHECK", false); //有資料設為 false
                }
                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "checkStatusYNTrue":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                jObject = new JSONObject();
                String msg = iPuBasicDao.checkStatusYNTrue(argJsonObj);
                jObject.put("msg", msg);

                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "insertBasicList":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                result = iPuBasicDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogBasic(), "OP02B01M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateBasicList":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                JSONObject beforeModifyJobj = ((JSONArray) iPuBasicDao.queryBasicByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                result = iPuBasicDao.update(argJsonObj);
                resultDataArray.put(argJsonObj);
                //202504 會Modify 一定已經過之前的查詢列出已寫過
                //NPALOG2 = new NPALOG2Util();
                //NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogBasic(), beforeModifyJobj, argJsonObj, "", "", "", "OP02B01M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "batchdPuDelete":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                String[] tmpSEQ_UPL = argJsonObj.get("OP_SEQ_NO").toString().split(",");
                for (int i = 0; i < tmpSEQ_UPL.length; i++) {
                    jObject = new JSONObject();
                    //jObject.put("OP_SEQ_NO", tmpSEQ_UPL[i]);
                    jObject.put("OP_SEQ_NO", argJsonObj.get("OP_AC_RCNO").toString());
                    jObject.put("OP_DEL_RSN", argJsonObj.get("OP_DEL_RSN").toString());
                    resultDataArray.put(jObject);
                }
                this.setFormData(returnJasonObj, iPuBasicDao.delete(argJsonObj));

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.D, "", "", "", "", resultDataArray, this.GetLog0203DEL(), "OP02A03Q_01", user, user.getUserName(), user.getUnitName(), "");
                break;
            case "BasicValueForLog":
                jObject = new JSONObject();
                jObject.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO")); //序號
                resultDataArray.put(jObject);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, "", "", "", "", resultDataArray, this.GetLog02B01MQuery(), "OP02B01M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, true);
                break;
            case "BasicValueForLog2":
                jObject = new JSONObject();
                jObject.put("OP_AC_RCNO", argJsonObj.getString("OP_AC_RCNO")); //序號
                jObject.put("OP_PUPO_NAME", argJsonObj.getString("OP_PUPO_NAME"));
                jObject.put("OP_PUPO_IDN", argJsonObj.getString("OP_PUPO_IDN"));
                jObject.put("OP_PUPO_PHONENO", argJsonObj.getString("OP_PUPO_PHONENO"));
                resultDataArray.put(jObject);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, "收據編號=" + argJsonObj.getString("OP_AC_RCNO"), argJsonObj.getString("OP_PUPO_IDN"), argJsonObj.getString("OP_PUPO_NAME"), argJsonObj.getString("OP_PUPO_BIRTHDT"), resultDataArray, this.GetLog02B01MQuery2(), "拾得物受理基本資料-OP02B01M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, true);
                break;
            case "showBasicValue":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                resultDataArray = iPuBasicDao.queryBasicById(argJsonObj.getInt("OP_SEQ_NO"));

                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "GetDetailList":
                iPuDetailDao = OPDT_I_PU_DETAIL.getInstance();
                crs1 = iPuDetailDao.QueryAllForDetail(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_SEQ_NO", crs1.getString("OP_SEQ_NO")); //序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //受理收據編號
                    jObject.put("OP_PUOJ_NM", crs1.getString("OP_PUOJ_NM")); //拾得物品名稱
                    jObject.put("OP_QTY", crs1.getString("OP_QTY")); //數量
                    jObject.put("OP_QTY_UNIT", crs1.getString("OP_QTY_UNIT")); //單位
                    jObject.put("OP_FEATURE", crs1.getString("OP_FEATURE")); //特徵
                    resultDataArray.put(jObject);
                }
                //202403 新增明細完的GetDetailList會error，暫拿掉
                //NPALOG2 = new NPALOG2Util();
                //NPALOG2.WriteQueryLog_1(LOGOPTYPE.Z, "收據編號=" + argJsonObj.getString("OP_AC_RCNO"), "", "", "", resultDataArray, this.GetLog02B02MQuery(), "拾得物品明細資料-OP02B02M_01", user, user.getUserName(), user.getUnitName(), argJsonObj.getString("OPR_KIND"), argJsonObj.getString("OPR_PURP")); //202403
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "checkYNDetail":
                iPuDetailDao = OPDT_I_PU_DETAIL.getInstance();
                result = true;
                crs1 = iPuDetailDao.QueryAllForDetail(argJsonObj);
                if (!crs1.next()) {
                    result = false;
                }

                this.setFormData(returnJasonObj, result);
                break;
            case "showDetailValue":
                iPuDetailDao = OPDT_I_PU_DETAIL.getInstance();
                crs1 = iPuDetailDao.QueryIdForDetail(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_SEQ_NO", crs1.getString("OP_SEQ_NO")); //序號
                    jObject.put("OP_BASIC_SEQ_NO", crs1.getString("OP_BASIC_SEQ_NO")); //遺失物事件基本資料序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //受理收據編號
                    jObject.put("OP_TYPE_CD", replaceWhiteChar(crs1.getString("OP_TYPE_CD"))); //拾得物物品類別
                    jObject.put("OP_TYPE_NM", crs1.getString("OP_TYPE_NM")); //拾得物物品類別名稱
                    jObject.put("OP_PUOJ_NM", crs1.getString("OP_PUOJ_NM")); //拾得物品名稱
                    jObject.put("OP_QTY", crs1.getString("OP_QTY")); //數量
                    jObject.put("OP_QTY_UNIT", crs1.getString("OP_QTY_UNIT")); //單位
                    jObject.put("OP_COLOR_CD", replaceWhiteChar(crs1.getString("OP_COLOR_CD"))); //顏色
                    jObject.put("OP_BRAND_CD", replaceWhiteChar(crs1.getString("OP_BRAND_CD"))); //廠牌          
                    jObject.put("OP_FEATURE", crs1.getString("OP_FEATURE")); //特徵
                    jObject.put("OP_IMEI_CODE", crs1.getString("OP_IMEI_CODE")); //IMEI
                    jObject.put("OP_IMEI_CODE_2", crs1.getString("OP_IMEI_CODE_2")); //IMEI 2
                    jObject.put("OP_PHONE_NUM", crs1.getString("OP_PHONE_NUM")); //電信門號
                    jObject.put("OP_PHONE_NUM_2", crs1.getString("OP_PHONE_NUM_2")); //電信門號2
                    jObject.put("OP_MAC_ADDR", crs1.getString("OP_MAC_ADDR")); //MAC
                    jObject.put("OP_REMARK", crs1.getString("OP_REMARK")); //備考           
                    resultDataArray.put(jObject);
                }
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog_1(LOGOPTYPE.Q, "", "", "", "", resultDataArray, this.GetLog02B02MQuery(), "OP02B02M_01", user, user.getUserName(), user.getUnitName(), argJsonObj.getString("OPR_KIND"), argJsonObj.getString("OPR_PURP")); //202403
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "insertDetailList":
                iPuDetailDao = OPDT_I_PU_DETAIL.getInstance();
                result = iPuDetailDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogDetail(), "OP02B02M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateDetailList":
                iPuDetailDao = OPDT_I_PU_DETAIL.getInstance();
                beforeModifyJobj = ((JSONArray) iPuDetailDao.queryDetailByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                result = iPuDetailDao.update(argJsonObj);
                resultDataArray.put(argJsonObj);
                //202504 會Modify 一定已經過之前的查詢列出已寫過
                //NPALOG2 = new NPALOG2Util();
                //NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogDetail(), beforeModifyJobj, argJsonObj, "", "", "", "OP02B02M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "deleteDetailList":
                iPuDetailDao = OPDT_I_PU_DETAIL.getInstance();
                result = iPuDetailDao.delete(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.D, "", "", "", "", resultDataArray, this.GetLog02B02MDEL(), "OP02B02M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, result);
                break;
            //region OP02A01Q_01.jsp ------End
            //region OP02A02Q_01.jsp ------Start	拾得遺失物受理維護
            case "IPuBasicQueryForDown":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                crs1 = iPuBasicDao.IPuBasicQueryForDown(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("BASIC_SEQ", crs1.getString("OP_SEQ_NO")); //基本資料序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_AC_UNIT_NM", crs1.getString("OP_AC_UNIT_NM")); //受理單位
                    jObject.put("OP_PUPO_TP_NM", crs1.getString("OP_PUPO_TP_NM")); //拾得人類別
                    jObject.put("OP_PUOJ_ATTR_NM", crs1.getString("OP_PUOJ_ATTR_NM")); //物品屬性
                    jObject.put("OP_PUPO_NAME", crs1.getString("OP_PUPO_NAME")); //拾得人姓名
                    jObject.put("OP_PUPO_IDN", crs1.getString("OP_PUPO_IDN")); //身分證/其他證號
                    jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_PU_DATE"), crs1.getString("OP_PU_TIME"))); //拾得日期時間
                    jObject.put("OP_PU_PLACE", crs1.getString("OP_PU_CITY_NM") + crs1.getString("OP_PU_TOWN_NM") + crs1.getString("OP_PU_PLACE")); //拾得地點

                    if (crs1.getString("OP_CURSTAT_CD").equals("1")) {
                        jObject.put("OP_CURSTAT_NM", "處理中"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("2")) {
                        if (crs1.getString("OP_YN_AN").equals("1")) {
                            jObject.put("OP_CURSTAT_NM", "內部公告中"); //目前狀態
                        } else if (crs1.getString("OP_YN_AN").equals("2")) {
                            jObject.put("OP_CURSTAT_NM", "網路公告中"); //目前狀態
                        } else {
                            jObject.put("OP_CURSTAT_NM", ""); //目前狀態
                        }
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("3")) {
                        jObject.put("OP_CURSTAT_NM", "公告期滿"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("4")) {
                        jObject.put("OP_CURSTAT_NM", "拾得人領回公告中"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("5")) {
                        jObject.put("OP_CURSTAT_NM", "拾得人領回公告期滿"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("6")) {
                        jObject.put("OP_CURSTAT_NM", "已結案"); //目前狀態
                    } else {
                        jObject.put("OP_CURSTAT_NM", ""); //目前狀態
                    }
                    resultDataArray.put(jObject);
                }
                NPALOG2 = new NPALOG2Util();

                //NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", "", "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString()+".jsp", user, user.getUserName(), user.getUnitName(), "");

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
//                if (argJsonObj.getString("ACTION_TYPE").equals("OP02A05Q_01")) {
//                    NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE")), "", argJsonObj.getString("OP_PUCP_NAME"), "", resultDataArray, this.GetLogForBasicQuery(argJsonObj.getString("ACTION_TYPE")), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), 
//                        argJsonObj.getString("OPR_KIND"),  //202403
//                        argJsonObj.getString("OPR_PURP"),  //202403 
//                        "", "", "", argJsonObj.getString("OP_AC_RCNO"));
//                } else {
//                    NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE")), "", "", "", resultDataArray, this.GetLogForBasicQuery(argJsonObj.getString("ACTION_TYPE")), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(),
//                        argJsonObj.getString("OPR_KIND"),  //202403
//                        argJsonObj.getString("OPR_PURP"),  //202403                            
//                        "", "", "", argJsonObj.getString("OP_AC_RCNO"));
//                }
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "updateBasicYnOv500":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                beforeModifyJobj = ((JSONArray) iPuBasicDao.queryBasicByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                result = iPuBasicDao.updateBasicYnOv500(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogBasicYnOv500(), beforeModifyJobj, argJsonObj, "", "", "", "OP02B01M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP02A02Q_01.jsp ------End
            //region OP02A04Q_01.jsp ------Start	拾得遺失物刪除案件
            case "IPuBasicQueryDeleteForDown":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                crs1 = iPuBasicDao.IPuBasicQueryForDown(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("BASIC_SEQ", crs1.getString("OP_SEQ_NO")); //基本資料序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_AC_UNIT_NM", crs1.getString("OP_AC_UNIT_NM")); //受理單位
                    jObject.put("OP_PUPO_TP_NM", crs1.getString("OP_PUPO_TP_NM")); //拾得人類別
                    jObject.put("OP_PUOJ_ATTR_NM", crs1.getString("OP_PUOJ_ATTR_NM")); //物品屬性
                    jObject.put("OP_PUPO_NAME", crs1.getString("OP_PUPO_NAME")); //拾得人姓名
                    jObject.put("OP_PUPO_IDN", crs1.getString("OP_PUPO_IDN")); //身分證/其他證號
                    jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_PU_DATE"), crs1.getString("OP_PU_TIME"))); //拾得日期時間
                    jObject.put("OP_PU_PLACE", crs1.getString("OP_PU_PLACE")); //拾得地點
                    jObject.put("OP_DEL_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_DEL_DATE"), crs1.getString("OP_DEL_TIME"))); //刪除日期時間
                    jObject.put("OP_DEL_UNIT_NM", crs1.getString("OP_DEL_UNIT_NM")); //刪除單位
                    jObject.put("OP_DEL_STAFF_NM", crs1.getString("OP_DEL_STAFF_NM")); //刪除人員

                    resultDataArray.put(jObject);
                }
                NPALOG2 = new NPALOG2Util();
              //202403 
              //NPALOG2.WriteQueryLog    (LOGOPTYPE.Q, GetFront02A04Q(argJsonObj), "", "", "", resultDataArray, this.GetLog0204Query(), "OP02A04Q_01.jsp", user, user.getUserName(), user.getUnitName(), "");

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
//                NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFront02A04Q(argJsonObj), "", "", "", 
//                        resultDataArray, this.GetLog0204Query(), 
//                        "拾得物刪除案件查詢-OP02A04Q_01", 
//                        user, user.getUserName(), user.getUnitName(), 
//                        argJsonObj.getString("OPR_KIND"),  //202403
//                        argJsonObj.getString("OPR_PURP"),  //202403
//                        "", "", "", argJsonObj.getString("OP_AC_RCNO"));
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
                case "IPuBasicQueryDeleteForDownSearch":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                crs1 = iPuBasicDao.IPuBasicQueryForDown(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("BASIC_SEQ", crs1.getString("OP_SEQ_NO")); //基本資料序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_AC_UNIT_NM", crs1.getString("OP_AC_UNIT_NM")); //受理單位
                    jObject.put("OP_PUPO_TP_NM", crs1.getString("OP_PUPO_TP_NM")); //拾得人類別
                    jObject.put("OP_PUOJ_ATTR_NM", crs1.getString("OP_PUOJ_ATTR_NM")); //物品屬性
                    jObject.put("OP_PUPO_NAME", crs1.getString("OP_PUPO_NAME")); //拾得人姓名
                    jObject.put("OP_PUPO_IDN", crs1.getString("OP_PUPO_IDN")); //身分證/其他證號
                    jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_PU_DATE"), crs1.getString("OP_PU_TIME"))); //拾得日期時間
                    jObject.put("OP_PU_PLACE", crs1.getString("OP_PU_PLACE")); //拾得地點
                    jObject.put("OP_DEL_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_DEL_DATE"), crs1.getString("OP_DEL_TIME"))); //刪除日期時間
                    jObject.put("OP_DEL_UNIT_NM", crs1.getString("OP_DEL_UNIT_NM")); //刪除單位
                    jObject.put("OP_DEL_STAFF_NM", crs1.getString("OP_DEL_STAFF_NM")); //刪除人員

                    resultDataArray.put(jObject);
                }
                // 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
                // 202504 預設查詢不寫NPALOG，但OP02A04Q_01.jsp 是 『刪除物件查詢』應要寫LOG
                   NPALOG2 = new NPALOG2Util();
//                
//                //202403 
//                System.out.println("this is PuMaintainServlet.IPuBasicQueryDeleteForDownSearch__case");
//                System.out.println("PuMaintainServlet_IPuBasicQueryDeleteForDownSearch OPR_KIND= __" + argJsonObj.getString("OPR_KIND"));
//                System.out.println("PuMaintainServlet_IPuBasicQueryDeleteForDownSearch OPR_PURP= __" + argJsonObj.getString("OPR_PURP")); 
//  
//              //NPALOG2.WriteQueryLog    (LOGOPTYPE.Q, GetFront02A04Q(argJsonObj), "", "", "", resultDataArray, this.GetLog0204Query(), "OP02A04Q_01.jsp", user, user.getUserName(), user.getUnitName(), "");
                NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFront02A04Q(argJsonObj), "", "", "", 
                        resultDataArray, this.GetLog0204Query(), "拾得物刪除案件查詢-OP02A04Q_01", 
                        user, user.getUserName(), user.getUnitName(), 
                        argJsonObj.getString("OPR_KIND"),argJsonObj.getString("OPR_PURP"),  //202403
                        "", "", "", argJsonObj.getString("OP_AC_RCNO"));
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            //region OP02A04Q_01.jsp ------End
                
            //region OP02A05Q_01.jsp ------Start	拾得物認領資料維護
            case "GetClaimReportList":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                crs1 = iPuClaimDao.GetClaimReportList(argJsonObj);
                String FILE_EXPL = "",
                 CLAIM_TYPE = "",
                 SEQ = "";
                boolean hasValue = false,
                 hasTrue = false,
                 hasFalse = false;
                while (crs1.next()) {
                    jObject = new JSONObject();
                    if (crs1.getString("OP_YN_LOSER").equals("1")) { //是有認領權人
                        jObject.put("FILE_NAME", "核對情形回覆函 – 核對結果正確"); //檔案名稱
                        jObject.put("REPORT_TYPE", "oP02A04Q.doc"); //報表類別
                        FILE_EXPL = crs1.getString("OP_PUCP_NAME");
                        CLAIM_TYPE = crs1.getString("TABLEFROM");
                        SEQ = crs1.getString("OP_SEQ_NO");
                        hasTrue = true;
                    } else if (crs1.getString("OP_YN_LOSER").equals("2")) { //不是認領權人
                        jObject.put("FILE_NAME", "核對情形回覆函 – 核對結果錯誤"); //檔案名稱
                        jObject.put("REPORT_TYPE", "oP02A05Q.doc"); //報表類別
                        hasFalse = true;
                    }
                    jObject.put("FILE_EXPL", crs1.getString("OP_PUCP_NAME")); //說明
                    jObject.put("CLAIM_TYPE", crs1.getString("TABLEFROM")); //表格類別
                    jObject.put("SEQ", crs1.getString("OP_SEQ_NO")); //認領人序號

                    resultDataArray.put(jObject);
                    hasValue = true;
                }
                if (hasTrue == false) {
                    jObject = new JSONObject();
                    jObject.put("FILE_NAME", "核對情形回覆函 – 核對結果正確"); //檔案名稱
                    jObject.put("REPORT_TYPE", "oP02A04Q.doc"); //報表類別
                    jObject.put("FILE_EXPL", ""); //說明
                    jObject.put("CLAIM_TYPE", "NOClaim"); //表格類別
                    jObject.put("SEQ", ""); //認領人序號
                    resultDataArray.put(jObject);
                }
                if (hasFalse == false) {
                    jObject = new JSONObject();
                    jObject.put("FILE_NAME", "核對情形回覆函 – 核對結果錯誤"); //檔案名稱
                    jObject.put("REPORT_TYPE", "oP02A05Q.doc"); //報表類別
                    jObject.put("FILE_EXPL", ""); //說明
                    jObject.put("CLAIM_TYPE", "NOClaim"); //表格類別
                    jObject.put("SEQ", ""); //認領人序號
                    resultDataArray.put(jObject);
                }
                // 一張　遺失（拾得）物領據
                jObject = new JSONObject();
                jObject.put("FILE_NAME", "認領遺失物領據"); //檔案名稱
                jObject.put("REPORT_TYPE", "oP02A06Q.doc"); //報表類別
                jObject.put("FILE_EXPL", FILE_EXPL); //說明
                jObject.put("CLAIM_TYPE", CLAIM_TYPE); //表格類別
                jObject.put("SEQ", SEQ); //認領人序號
                resultDataArray.put(jObject);
                //一張　已認領通知函
                jObject = new JSONObject();
                jObject.put("FILE_NAME", "已認領通知函"); //檔案名稱
                jObject.put("REPORT_TYPE", "oP02A08Q.doc"); //報表類別
                jObject.put("FILE_EXPL", ""); //說明
                jObject.put("CLAIM_TYPE", CLAIM_TYPE); //表格類別
                jObject.put("SEQ", SEQ); //認領人序號
                resultDataArray.put(jObject);
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "insertClaimList":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                result = iPuClaimDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogClaim(), "OP02B03M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateClaimList":
                ePuNetClmDao = OPDT_E_NET_CLAIM.getInstance();
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                if (argJsonObj.get("OP_CLAIM_TP_CD").equals("2")) { //臨櫃認領
                    beforeModifyJobj = ((JSONArray) iPuClaimDao.queryClaimByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                    result = iPuClaimDao.update(argJsonObj);
                } else { //民眾認領
                    beforeModifyJobj = ((JSONArray) ePuNetClmDao.queryNetClmByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                    result = ePuNetClmDao.update(argJsonObj);
                }
                resultDataArray.put(argJsonObj);

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogClaim(), beforeModifyJobj, argJsonObj, "", "", "", "OP02B03M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "deleteClaimList":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                result = iPuClaimDao.delete(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.D, "", "", "", "", resultDataArray, this.GetLog02B03MDEL(), "OP02B03M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, result);
                break;
            case "GetClaimList":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                crs1 = iPuClaimDao.QueryAllForClaim(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_SEQ_NO", crs1.getString("OP_SEQ_NO")); //序號
                    jObject.put("OP_BASIC_SEQ_NO", crs1.getString("OP_BASIC_SEQ_NO")); //基本資料序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據號碼
                    String OP_FM_DATE = "", OP_LOST_DATE = "", OP_LOST_TIME = "", OP_YN_LOSER = "";
                    OP_FM_DATE = DateUtil.to7TwDateTime(crs1.getString("OP_FM_DATE"));
                    jObject.put("OP_FM_DATE", OP_FM_DATE); //填表日期
                    jObject.put("OP_CLAIM_TP_NM", crs1.getString("OP_CLAIM_TP_NM")); //認領方式
                    jObject.put("OP_PUCP_NAME", crs1.getString("OP_PUCP_NAME")); //姓名
                    jObject.put("OP_PUCP_RNAME", crs1.getString("OP_PUCP_RNAME")); //姓名羅馬拼音
                    jObject.put("OP_PUCP_IDN", crs1.getString("OP_PUCP_IDN")); //證號
                    OP_LOST_DATE = DateUtil.to7TwDateTime(crs1.getString("OP_LOST_DATE"));
                    jObject.put("OP_LOST_DATE", OP_LOST_DATE); //遺失日期
                    OP_LOST_TIME = DateUtil.to4TwTime(crs1.getString("OP_LOST_TIME"));
                    jObject.put("OP_LOST_TIME", OP_LOST_TIME); //遺失時間
                    jObject.put("OP_LOST_PLACE", crs1.getString("OP_LOST_PLACE")); //遺失地點
                    if (crs1.getString("OP_YN_LOSER").equals("0")) {
                        OP_YN_LOSER = "";
                    } else if (crs1.getString("OP_YN_LOSER").equals("1")) {
                        OP_YN_LOSER = "是";
                    } else if (crs1.getString("OP_YN_LOSER").equals("2")) {
                        OP_YN_LOSER = "否";
                    } else if (crs1.getString("OP_YN_LOSER").equals("3")) {
                        OP_YN_LOSER = "待確認";
                    }
                    jObject.put("OP_YN_LOSER", OP_YN_LOSER); //是否為有認領權人
                    jObject.put("TABLEFROM", crs1.getString("TABLEFROM")); //來自哪個table
                    resultDataArray.put(jObject);
                }
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "showClaimValue":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                ePuNetClmDao = OPDT_E_NET_CLAIM.getInstance();
                if (argJsonObj.getString("TABLEFROM").equals("Claim")) {
                    resultDataArray = iPuClaimDao.queryClaimById(argJsonObj.getInt("OP_SEQ_NO"));
                } else {
                    resultDataArray = ePuNetClmDao.queryNetClmById(argJsonObj.getInt("OP_SEQ_NO"));
                }

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, "", "", "", "", resultDataArray, this.GetLog02B03MQuery(), "OP02B03M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP02A05Q_01.jsp ------End
            case "PuNetQueryForDownSearch":
                ePuNetClmDao = OPDT_E_NET_CLAIM.getInstance();
                crs1 = ePuNetClmDao.PuNetQueryForDown(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("SEQ", crs1.getString("OP_SEQ_NO")); //序號
                    jObject.put("BASIC_SEQ", crs1.getString("OP_BASIC_SEQ_NO")); //基本資料序號
                    jObject.put("OP_FM_DATE", DateUtil.to7TwDateTime(crs1.getString("OP_FM_DATE"))); //填單日期
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_PUCP_NAME", crs1.getString("OP_PUCP_NAME")); //認領人姓名
                    jObject.put("OP_PUCP_IDN", crs1.getString("OP_PUCP_IDN")); //認領人證號
                    //jObject.put("OP_LOST_DATE",crs1.getString("OP_LOST_DATE"));
                    //jObject.put("OP_LOST_TIME",crs1.getString("OP_LOST_TIME"));
                    jObject.put("OP_LOST_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_LOST_DATE"), crs1.getString("OP_LOST_TIME"))); //遺失日期時間
                    jObject.put("OP_LOST_PLACE", crs1.getString("OP_LOST_CITY_NM") + crs1.getString("OP_LOST_TOWN_NM") + crs1.getString("OP_LOST_PLACE")); //遺失地點
                    jObject.put("OP_LOST_OJ_DESC", crs1.getString("OP_LOST_OJ_DESC")); //物品描述
                    if (crs1.getString("OP_YN_LOSER").trim().equals("") || crs1.getString("OP_YN_LOSER").equals("0") || crs1.getString("OP_YN_LOSER") == null) {
                        jObject.put("OP_YN_LOSER", "否"); //是否審核(是否為有認領權人)
                    } else {
                        jObject.put("OP_YN_LOSER", "是"); //是否審核(是否為有認領權人)
                    }

                    jObject.put("OP_AC_DATE", DateUtil.to7TwDateTime(crs1.getString("OP_AC_DATE"))); //受理日期

                    resultDataArray.put(jObject);
                }
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog_New(
                        LOGOPTYPE.Q, GetFront01A01Q(argJsonObj), 
                        "", "", "", resultDataArray, this.GetLog0101Query(),
                        "OP01A01Q_01", 
                        user, user.getUserName(), user.getUnitName(), 
                        argJsonObj.getString("OPR_KIND"),  //202403
                        argJsonObj.getString("OPR_PURP"),  //202403
                        "", "", "", "");
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "IPuBasicQueryForDownSearch":
                iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                crs1 = iPuBasicDao.IPuBasicQueryForDown(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("BASIC_SEQ", crs1.getString("OP_SEQ_NO")); //基本資料序號
                    jObject.put("OP_AC_RCNO", crs1.getString("OP_AC_RCNO")); //收據編號
                    jObject.put("OP_AC_UNIT_NM", crs1.getString("OP_AC_UNIT_NM")); //受理單位
                    jObject.put("OP_PUPO_TP_NM", crs1.getString("OP_PUPO_TP_NM")); //拾得人類別
                    jObject.put("OP_PUOJ_ATTR_NM", crs1.getString("OP_PUOJ_ATTR_NM")); //物品屬性
                    jObject.put("OP_PUPO_NAME", crs1.getString("OP_PUPO_NAME")); //拾得人姓名
                    jObject.put("OP_PUPO_IDN", crs1.getString("OP_PUPO_IDN")); //身分證/其他證號
                    jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_PU_DATE"), crs1.getString("OP_PU_TIME"))); //拾得日期時間
                    jObject.put("OP_PU_PLACE", crs1.getString("OP_PU_CITY_NM") + crs1.getString("OP_PU_TOWN_NM") + crs1.getString("OP_PU_PLACE")); //拾得地點

                    if (crs1.getString("OP_CURSTAT_CD").equals("1")) {
                        jObject.put("OP_CURSTAT_NM", "處理中"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("2")) {
                        if (crs1.getString("OP_YN_AN").equals("1")) {
                            jObject.put("OP_CURSTAT_NM", "內部公告中"); //目前狀態
                        } else if (crs1.getString("OP_YN_AN").equals("2")) {
                            jObject.put("OP_CURSTAT_NM", "網路公告中"); //目前狀態
                        } else {
                            jObject.put("OP_CURSTAT_NM", ""); //目前狀態
                        }
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("3")) {
                        jObject.put("OP_CURSTAT_NM", "公告期滿"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("4")) {
                        jObject.put("OP_CURSTAT_NM", "拾得人領回公告中"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("5")) {
                        jObject.put("OP_CURSTAT_NM", "拾得人領回公告期滿"); //目前狀態
                    } else if (crs1.getString("OP_CURSTAT_CD").equals("6")) {
                        jObject.put("OP_CURSTAT_NM", "已結案"); //目前狀態
                    } else {
                        jObject.put("OP_CURSTAT_NM", ""); //目前狀態
                    }
                    resultDataArray.put(jObject);
                }
                
                //NPALOG2 = new NPALOG2Util();

                //NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", "", "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString()+".jsp", user, user.getUserName(), user.getUnitName(), "");

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
//                if (argJsonObj.getString("ACTION_TYPE").equals("OP02A05Q_01")) {
//                    NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE")), "", argJsonObj.getString("OP_PUCP_NAME"), "", resultDataArray, this.GetLogForBasicQuery(argJsonObj.getString("ACTION_TYPE")), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), 
//                            argJsonObj.getString("OPR_KIND"),  //202403
//                            argJsonObj.getString("OPR_PURP"),  //202403
//                            "", "", "", argJsonObj.getString("OP_AC_RCNO"));
//                } else {
//                    NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE")), "", "", "", resultDataArray, this.GetLogForBasicQuery(argJsonObj.getString("ACTION_TYPE")), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), 
//                            argJsonObj.getString("OPR_KIND"),  //202403
//                            argJsonObj.getString("OPR_PURP"),  //202403
//                            "", "", "", argJsonObj.getString("OP_AC_RCNO"));
//                }
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
        }
    }

    //查詢條件欄位(01A01Q)
    private String GetFront01A01Q(JSONObject jsonObject) {
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
        if (!jsonObject.getString("OP_FM_DATE_S").equals("")) {
            returnString.append(String.format("&%s=%s", "認領填單起日", jsonObject.getString("OP_FM_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_FM_DATE_E").equals("")) {
            returnString.append(String.format("&%s=%s", "認領填單迄日", jsonObject.getString("OP_FM_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_YN_LOSER").equals("")) {
            returnString.append(String.format("&%s=%s", "是否審核", jsonObject.getString("OP_YN_LOSER")));
        }

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLog0101Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_FM_DATE", "填單日期");
        logCols.put("OP_AC_RCNO", "收據編號");
        logCols.put("OP_PUCP_NAME", "認領人姓名");
        logCols.put("OP_PUCP_IDN", "認領人證號");
        logCols.put("OP_LOST_DTTM", "遺失日期時間");
        logCols.put("OP_AC_DATE", "受理日期");
        logCols.put("OP_LOST_PLACE", "遺失地點");
        logCols.put("OP_LOST_OJ_DESC", "物品描述");
        logCols.put("OP_YN_LOSER", "是否審核");

        return logCols;
    }

    //新增修改BASIC
    private HashMap<String, String> GetModifyLogBasic() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        //受理單位資料
//			logCols.put("OP_AC_UNIT_NM","受理單位");
        logCols.put("OP_AC_STAFF_NM", "受理人員");
        logCols.put("OP_AC_RCNO", "收據號碼");
        logCols.put("OP_AC_DATE", "受理日期");
        logCols.put("OP_FM_DATE", "填單日期");
        logCols.put("OP_AC_UNIT_TEL", "電話");
        logCols.put("OP_MANUAL_RCNO", "手開單編號");
        //拾得人基本資料
        logCols.put("OP_IS_CUST", "民眾自行保管");
        logCols.put("OP_IS_PUT_NM", "是否具名");
        logCols.put("OP_INCLUDE_CERT", "有無證件");
        logCols.put("OP_PUPO_TP_NM", "拾得人類別");
        logCols.put("OP_PUOJ_ATTR_NM", "物品屬性");
        logCols.put("OP_PUPO_NAME", "姓名");
        logCols.put("OP_PUPO_RNAME", "羅馬拼音");
        logCols.put("OP_PUPO_IDN_TP", "證號別");
        logCols.put("OP_PUPO_IDN", "證號");
        logCols.put("OP_PUPO_BEFROC", "出生別");
        logCols.put("OP_PUPO_BIRTHDT", "出生日期");
        logCols.put("OP_PUPO_GENDER", "性別");
        logCols.put("OP_PUPO_NAT_NM", "國籍");
        logCols.put("OP_PUPO_PHONENO", "電話");
        logCols.put("OP_PUPO_ZIPCODE", "郵遞區號");
        logCols.put("OP_OC_ADDR_TYPE_CD", "地址別");
        logCols.put("OP_PUPO_CITY_NM", "地址-縣市");
        logCols.put("OP_PUPO_TOWN_NM", "地址-鄉鎮");
        logCols.put("OP_PUPO_VILLAGE_NM", "地址-村里");
        logCols.put("OP_PUPO_LIN", "地址-鄰");
        logCols.put("OP_PUPO_ROAD", "一般地址");
        logCols.put("OP_PUPO_ADDR_OTH", "其他地址");
        logCols.put("OP_PUPO_EMAIL", "電子郵件");
        //拾得地點時間資料
        logCols.put("OP_PU_DATE", "拾得日期");
        logCols.put("OP_PU_TIME", "拾得時間");
        logCols.put("OP_PU_CITY_NM", "拾得地點-縣市");
        logCols.put("OP_PU_TOWN_NM", "拾得地點-鄉鎮");
        logCols.put("OP_PU_PLACE", "拾得地點");
        //其他資料
        logCols.put("OP_CURSTAT_NM", "目前狀態");
        logCols.put("OP_NTC_FIND_PO", "招領人");
        logCols.put("OP_YN_NTC", "是否通知");
        logCols.put("OP_NTC_DATE", "通知日期");
        logCols.put("OP_NTC_PSN_TYPE", "通知者身分");
        logCols.put("OP_NTC_MODE", "通知方式");
        logCols.put("OP_PU_REMARK", "備註");
        //額外
        logCols.put("OP_PU_YN_OV500", "拾得物總價值是否超過500");

        return logCols;
    }

    //修改BASIC 一般受理 伍佰元專區
    private HashMap<String, String> GetModifyLogBasicYnOv500() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "受理基本資料序號");
        logCols.put("OP_PU_YN_OV500", "拾得物總價值是否超過500");

        return logCols;
    }
    //刪除BASIC   

    private HashMap<String, String> GetLog0203DEL() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "受理基本資料序號");
        logCols.put("OP_DEL_RSN", "刪除原因");
        return logCols;
    }
    //查詢BASIC  

    private HashMap<String, String> GetLog02B01MQuery() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "選取受理基本資料序號");
        return logCols;
    }
    //查詢BASIC  

    private HashMap<String, String> GetLog02B01MQuery2() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_AC_RCNO", "選取受理基本資料序號");
        logCols.put("OP_PUPO_PHONENO", "選取拾得人電話");
        logCols.put("OP_PUPO_NAME", "選取拾得人姓名");
        logCols.put("OP_PUPO_IDN", "選取拾得人身分證號");
        return logCols;
    }

    //新增修改DETAIL
    private HashMap<String, String> GetModifyLogDetail() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        //拾得物品明細資料
        logCols.put("OP_AC_RCNO", "收據號碼");
        logCols.put("OP_PUOJ_NM", "物品名稱");
        logCols.put("OP_TYPE_NM", "物品種類");
        logCols.put("OP_BRAND_NM", "廠牌");
        logCols.put("OP_IMEI_CODE", "IMEI碼");
        logCols.put("OP_MAC_ADDR", "MAC碼");
        logCols.put("OP_QTY", "數量");
        logCols.put("OP_QTY_UNIT", "單位");
        logCols.put("OP_COLOR_NM", "顏色");
        logCols.put("OP_FEATURE", "特徵");
        logCols.put("OP_REMARK", "備考");

        return logCols;
    }

    //刪除DETAIL  
    private HashMap<String, String> GetLog02B02MDEL() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "刪除明細資料序號");
        return logCols;
    }

    //查詢DETAIL  
    private HashMap<String, String> GetLog02B02MQuery() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "明細資料序號");
        logCols.put("OP_AC_RCNO", "受理收據編號");
        logCols.put("OP_PUOJ_NM", "拾得物品名稱");
        logCols.put("OP_QTY", "數量");
        logCols.put("OP_QTY_UNIT", "單位");
        return logCols;
    }

    //查詢條件欄位(02A02Q)
    private String GetFrontBasic(JSONObject jsonObject, String ACTION_TYPE) {
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
        if (!jsonObject.getString("OP_AC_RCNO").equals("")) {
            returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
        }
        if (!jsonObject.getString("OP_PUPO_TP_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "拾得人類別", jsonObject.getString("OP_PUPO_TP_NM")));
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

        if (ACTION_TYPE.equals("OP02A05Q_01")) {
            if (!jsonObject.getString("OP_PUCP_NAME").equals("")) {
                returnString.append(String.format("&%s=%s", "認領人", jsonObject.getString("OP_PUCP_NAME")));
            }
        } else if (ACTION_TYPE.equals("OP03A01Q_01")) {
            if (!jsonObject.getString("OP_YN_AN").equals("")) {
                returnString.append(String.format("&%s=%s", "是否公告", jsonObject.getString("OP_YN_AN")));
            }
        } else if (ACTION_TYPE.equals("OP03A02Q_01") || ACTION_TYPE.equals("OP05A01Q_01")) {
            if (!jsonObject.getString("OP_CURSTAT_NM").equals("")) {
                returnString.append(String.format("&%s=%s", "目前狀態", jsonObject.getString("OP_CURSTAT_NM")));
            }
        } else if (ACTION_TYPE.equals("OP04A01Q_01")) {
            if (!jsonObject.getString("OP_NTC_GB_REC_NM").equals("")) {
                returnString.append(String.format("&%s=%s", "通知領回紀錄", jsonObject.getString("OP_NTC_GB_REC_NM")));
            }
        }

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLogForBasicQuery(String ACTION_TYPE) {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        if (ACTION_TYPE.equals("OP02A05Q_01")) {
            logCols.put("OP_AC_RCNO", "收據編號");
            logCols.put("OP_AC_UNIT_NM", "受理單位");
            logCols.put("OP_PUPO_TP_NM", "拾得人類別");
            logCols.put("OP_PU_DTTM", "拾得日期時間");
            logCols.put("OP_PU_PLACE", "拾得地點");
            logCols.put("OP_CURSTAT_NM", "目前狀態");
        } else {
            logCols.put("OP_AC_RCNO", "收據編號");
            logCols.put("OP_AC_UNIT_NM", "受理單位");
            logCols.put("OP_PUPO_TP_NM", "拾得人類別");
            logCols.put("OP_PUOJ_ATTR_NM", "物品屬性");
            logCols.put("OP_PUPO_NAME", "拾得人姓名");
            logCols.put("OP_PUPO_IDN", "身分證其他證號");
            logCols.put("OP_PU_DTTM", "拾得日期時間");
            logCols.put("OP_PU_PLACE", "拾得地點");
            logCols.put("OP_CURSTAT_NM", "目前狀態");
        }

        return logCols;
    }

    //查詢條件欄位(02A04Q)
    private String GetFront02A04Q(JSONObject jsonObject) {
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
        if (!jsonObject.getString("OP_AC_RCNO").equals("")) {
            returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
        }
        if (!jsonObject.getString("OP_PUPO_TP_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "拾得人類別", jsonObject.getString("OP_PUPO_TP_NM")));
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

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLog0204Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_AC_RCNO", "收據編號");
        logCols.put("OP_AC_UNIT_NM", "受理單位");
        logCols.put("OP_PUPO_TP_NM", "拾得人類別");
        logCols.put("OP_PUOJ_ATTR_NM", "物品屬性");
        logCols.put("OP_PUPO_NAME", "拾得人姓名");
        logCols.put("OP_PUPO_IDN", "身分證其他證號");
        logCols.put("OP_PU_DTTM", "拾得日期時間");
        logCols.put("OP_PU_PLACE", "拾得地點");
        logCols.put("OP_DEL_DTTM", "刪除日期時間");
        logCols.put("OP_DEL_UNIT_NM", "刪除單位");
        logCols.put("OP_DEL_STAFF_NM", "刪除人員");

        return logCols;
    }

    private HashMap<String, String> GetLog0205Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_AC_RCNO", "收據編號");
        logCols.put("OP_AC_UNIT_NM", "受理單位");
        logCols.put("OP_PUPO_TP_NM", "拾得人類別");
        logCols.put("OP_PU_DTTM", "拾得日期時間");
        logCols.put("OP_PU_PLACE", "拾得地點");
        logCols.put("OP_CURSTAT_NM", "目前狀態");

        return logCols;
    }

    //新增修改CLAIM
    private HashMap<String, String> GetModifyLogClaim() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        //文件資料
        logCols.put("OP_AC_RCNO", "收據號碼");
        logCols.put("OP_FM_DATE", "填單日期");
        //認領人基本資料
        logCols.put("OP_PUCP_NAME", "姓名");
        logCols.put("OP_PUCP_IDN_TP", "證號別");
        logCols.put("OP_PUCP_IDN", "證號");
        logCols.put("OP_PUCP_BEFROC", "出生別");
        logCols.put("OP_PUCP_BIRTHDT", "出生日期");
        logCols.put("OP_PUCP_GENDER", "性別");
        logCols.put("OP_PUCP_NAT_NM", "國籍");
        logCols.put("OP_PUCP_PHONENO", "電話");
        logCols.put("OP_PUCP_ZIPCODE", "郵遞區號");
        logCols.put("OP_PUCP_ADDR_TYPE_CD", "地址別");
        logCols.put("OP_PUCP_CITY_NM", "地址-縣市");
        logCols.put("OP_PUCP_TOWN_NM", "地址-鄉鎮");
        logCols.put("OP_PUCP_VILLAGE_NM", "地址-村里");
        logCols.put("OP_PUCP_LIN", "地址-鄰");
        logCols.put("OP_PUCP_ROAD", "一般地址");
        logCols.put("OP_PUCP_ADDR_OTH", "其他地址");
        logCols.put("OP_PUCP_EMAIL", "電子郵件");
        logCols.put("OP_CLAIM_TP_NM", "認領方式");
        //遺失地點時間資料
        logCols.put("OP_LOST_DATE", "遺失日期");
        logCols.put("OP_LOST_TIME", "遺失時間");
        logCols.put("OP_LOST_CITY_NM", "遺失地點-縣市");
        logCols.put("OP_LOST_TOWN_NM", "遺失地點-鄉鎮");
        logCols.put("OP_LOST_PLACE", "遺失地點");
        logCols.put("OP_LOST_OJ_DESC", "物品描述");
        //比對結果
        logCols.put("OP_YN_LOSER", "是否為有認領權人");
        logCols.put("OP_REMARK", "備註");

        return logCols;
    }

    //刪除CLAIM  
    private HashMap<String, String> GetLog02B03MDEL() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "刪除認領人資料序號");
        return logCols;
    }

    //查詢CLAIM 
    private HashMap<String, String> GetLog02B03MQuery() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_CLAIM_TP_NM", "認領方式");
        logCols.put("OP_SEQ_NO", "選取認領人資料序號");
        return logCols;
    }
}
