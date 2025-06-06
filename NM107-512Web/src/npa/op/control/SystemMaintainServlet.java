package npa.op.control;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import npa.op.base.AjaxBaseServlet;
import npa.op.dao.OPDT_E0_NPAUNITDao;
import npa.op.dao.OPDT_FUNC_ROLEDao;
import npa.op.dao.OPDT_MAIL_NOTICE;
import npa.op.dao.SystemDao;
import npa.op.util.DateUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.vo.User;

/**
 * 系統管理
 */
@WebServlet("/SystemMaintainServlet")
public class SystemMaintainServlet extends AjaxBaseServlet {

    private static final long serialVersionUID = 1L;
    Logger log = Logger.getLogger(SystemMaintainServlet.class);

    @Override
    protected void executeAjax(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, User user, JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {

        //取得登入人員資訊
        argJsonObj.put("userVO", user);
        JSONObject jObject;
        JSONArray resultDataArray = new JSONArray();
        SystemDao systemDao = null;
        OPDT_MAIL_NOTICE mailnoticeDao = null;
        CachedRowSet crs1 = null;
        NPALOG2Util NPALOG2 = null;
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        JSONArray resultDataArray1 = new JSONArray();
        HttpSession tmpsession = null;
        boolean result = false;
        String msg = "";
        OPDT_E0_NPAUNITDao daoNPAUNIT = null;

        switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {

            //region OP09A01Q_01 ------Start
            case "GTEschedule":
                systemDao = SystemDao.getInstance();
                crs1 = systemDao.QuerySchdl(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_SEQ_NO", crs1.getString("OP_SEQ_NO"));
                    jObject.put("OP_PRCD_CD", crs1.getString("OP_PRCD_CD"));
                    jObject.put("OP_PRCD_NM", crs1.getString("OP_PRCD_NM"));
                    jObject.put("OP_SCHDL_DT_S", DateUtil.get7TwDateFormat(crs1.getString("OP_SCHDL_DT_S") + "000000"));
                    jObject.put("OP_SCHDL_TM_S", crs1.getString("OP_SCHDL_TM_S"));
                    jObject.put("OP_SCHDL_DT_E", DateUtil.get7TwDateFormat(crs1.getString("OP_SCHDL_DT_E") + "000000"));
                    jObject.put("OP_SCHDL_TM_E", crs1.getString("OP_SCHDL_TM_E"));
                    jObject.put("OP_SUCCESS_NUM", crs1.getString("OP_SUCCESS_NUM"));
                    jObject.put("OP_TOTAL_NUM", crs1.getString("OP_TOTAL_NUM"));

                    resultDataArray.put(jObject);
                }

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontOP09A01(argJsonObj), "", "", "", resultDataArray, this.GetLogForOP09A01Query(), "OP09A01Q_01", user, user.getUserName(), user.getUnitName(), "");

                this.setJqGridData(returnJasonObj, resultDataArray);

                break;
            //region OP09A01Q_01 ------End
            //region OP09A02Q_01 ------Start    
            case "QueryTotal":
                systemDao = SystemDao.getInstance();
                crs1 = systemDao.QueryUERYTM(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_QUERY_NM", crs1.getString("OP_QUERY_NM"));
                    jObject.put("OP_WEB_COUNT", crs1.getString("OP_WEB_COUNT"));
                    jObject.put("OP_ANDROID_COUNT", crs1.getString("OP_ANDROID_COUNT"));
                    jObject.put("OP_IOS_COUNT", crs1.getString("OP_IOS_COUNT"));
                    jObject.put("OP_STATUS_S", crs1.getString("OP_STATUS_S"));
                    jObject.put("OP_STATUS_E", crs1.getString("OP_STATUS_E"));
                    jObject.put("OP_STATUS_T", crs1.getString("OP_STATUS_T"));

                    resultDataArray.put(jObject);
                }

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontOP09A02(argJsonObj), "", "", "", resultDataArray, this.GetLogForOP09A02Query(), "OP09A02Q_01", user, user.getUserName(), user.getUnitName(), "");

                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            //region OP09A02Q_01 ------End
            //region OP09A03Q_01 ------Start
            case "GET_FUNC":
                OPDT_FUNC_ROLEDao FUNCALL = OPDT_FUNC_ROLEDao.getInstance();
                resultDataArray = FUNCALL.getFUNC_ROLE(argJsonObj);

                logCols = new LinkedHashMap<String, String>();
                logCols.put("OP_ROLE_NM", "角色");
                logCols.put("OP_FUNC_NM", "功能名稱");
                logCols.put("OP_ENABLED_CHT", "是否啟用");
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontOP09A03(argJsonObj), "", "", "", resultDataArray, logCols, "OP09A03Q_01", user, user.getUserName(), user.getUnitName(), "");

                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "FUNC_ENABLE":
                OPDT_FUNC_ROLEDao FUNC_ENABLE = OPDT_FUNC_ROLEDao.getInstance();
                String[] tmpID = argJsonObj.get("OP_FUNC_ID").toString().split(",");
                for (int i = 0; i < tmpID.length; i++) {
                    String[] tmpRoleID = tmpID[i].split(";");
                    result = FUNC_ENABLE.UpdateENABLED(argJsonObj.get("OP_ENABLED").toString(), tmpRoleID[0], tmpRoleID[1], argJsonObj);
                    if (!result) {
                        msg = "請洽系統管理員";
                        argJsonObj.put("msg", msg);
                        resultDataArray.put(argJsonObj);
                        this.setFormData(returnJasonObj, resultDataArray);
                    }
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
                jObject = new JSONObject();
                resultDataArray = new JSONArray();
                logCols = new LinkedHashMap<String, String>();
                logCols.put("OP_FUNC_NM", "功能名稱");
                logCols.put("OP_ROLE_NM", "角色名稱");
                logCols.put("OP_ENABLED", "啟用註記");
                String[] tmpIDNM = argJsonObj.get("OP_FUNC_NM").toString().split(",");
                for (int i = 0; i < tmpIDNM.length; i++) {
                    String[] tmpFUNCNM = tmpID[i].split(";");
                    jObject.put("OP_FUNC_NM", tmpFUNCNM[0]);
                    jObject.put("OP_ROLE_NM", tmpFUNCNM[1]);
                    if (argJsonObj.get("OP_ENABLED").equals("Y")) {
                        jObject.put("OP_ENABLED", "已啟用");
                    } else {
                        jObject.put("OP_ENABLED", "已停用");
                    }
                    resultDataArray.put(jObject);

                }

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.D, "", "", "", "", resultDataArray, logCols, "OP09A03Q_01", user, "", "", "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP09A03Q_01 ------End
            //region OP09A04Q_01 ------Start
            case "checkUnitForDocWd":
                systemDao = SystemDao.getInstance();
                result = true;
                jObject = new JSONObject();
                jObject.put("CHECK", "N"); //沒有資料設為 N
                crs1 = systemDao.checkUnitForDocWd(argJsonObj);
                if (crs1.next()) {
                    jObject.put("CHECK", "Y"); //有資料設為 Y
                }
                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "QueryWD_PARA":
                systemDao = SystemDao.getInstance();
                crs1 = systemDao.QueryWD_PARA(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_D_UNIT_CD", crs1.getString("OP_D_UNIT_CD"));
                    jObject.put("OP_D_UNIT_NM", crs1.getString("OP_D_UNIT_NM"));
                    jObject.put("OP_B_UNIT_CD", crs1.getString("OP_B_UNIT_CD"));
                    jObject.put("OP_B_UNIT_NM", crs1.getString("OP_B_UNIT_NM"));
                    jObject.put("OP_DOC_WD1", crs1.getString("OP_DOC_WD1"));
                    jObject.put("OP_YN_GET_NO1", crs1.getString("OP_YN_GET_NO1"));
                    jObject.put("OP_YN_GET_NO1_CHT", "1".equals(crs1.getString("OP_YN_GET_NO1")) ? "是" : "否");
                    jObject.put("OP_DOC_WD2", crs1.getString("OP_DOC_WD2"));
                    jObject.put("OP_YN_GET_NO2", crs1.getString("OP_YN_GET_NO2"));
                    jObject.put("OP_YN_GET_NO2_CHT", "1".equals(crs1.getString("OP_YN_GET_NO2")) ? "是" : "否");
                    jObject.put("OP_SEQ_NO", crs1.getString("OP_SEQ_NO"));

                    resultDataArray.put(jObject);
                }

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontOP09A04(argJsonObj), "", "", "", resultDataArray, this.GetLogForOP09A04Query(), "OP09A04Q_01", user, user.getUserName(), user.getUnitName(), "");

                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "doSaveByOP09A04Q":
                systemDao = SystemDao.getInstance();
                result = systemDao.INSERTWD_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogOP09A04(), "OP09A04Q_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doUpdateByOP09A04Q":
                systemDao = SystemDao.getInstance();
                JSONObject beforeModifyJobj = ((JSONArray) systemDao.queryWD_PARAByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                result = systemDao.UPDATEWD_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogOP09A04(), beforeModifyJobj, argJsonObj, "", "", "", "OP09A04Q_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doDeleteByOP09A04Q":
                systemDao = SystemDao.getInstance();
                result = systemDao.DELETEWD_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.D, "", "", "", "", resultDataArray, this.GetLog09A04QDEL(), "OP09A04Q_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "getMarkers":
                daoNPAUNIT = OPDT_E0_NPAUNITDao.getInstance();
                this.setFormData(
                        returnJasonObj,
                        daoNPAUNIT.getMarkers(
                                argJsonObj.getBoolean("includeLower"),
                                argJsonObj.getString("OP_D_UNIT_CD"),
                                argJsonObj.getString("OP_B_UNIT_CD"),
                                argJsonObj.getString("OP_UNIT_CD")));
                break;
            case "updatePosition":
                daoNPAUNIT = OPDT_E0_NPAUNITDao.getInstance();
                argJsonObj.put("userName", user.getUserName());
                this.setFormData(returnJasonObj, daoNPAUNIT.updatePosition(argJsonObj));
                break;
            //region OP09A04Q_01 ------End
            //region OP09A06Q_01 ------Start
            case "QueryMAIL_PARA":
                systemDao = SystemDao.getInstance();
                crs1 = systemDao.QueryMAIL_PARA(argJsonObj);
                while (crs1.next()) {
                    jObject = new JSONObject();
                    jObject.put("OP_D_UNIT_CD", crs1.getString("OP_D_UNIT_CD"));
                    jObject.put("OP_D_UNIT_NM", crs1.getString("OP_D_UNIT_NM"));
                    jObject.put("OP_B_UNIT_CD", crs1.getString("OP_B_UNIT_CD"));
                    jObject.put("OP_B_UNIT_NM", crs1.getString("OP_B_UNIT_NM"));
                    jObject.put("OP_SEQ_NO", crs1.getString("OP_SEQ_NO"));
                    jObject.put("OP_MAIL_CONTENT", crs1.getString("OP_MAIL_CONTENT"));

                    resultDataArray.put(jObject);
                }

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFrontOP09A06(argJsonObj), "", "", "", resultDataArray, this.GetLogForOP09A06Query(), "OP09A06Q_01", user, user.getUserName(), user.getUnitName(), "");

                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "checkUnitForMail":
                systemDao = SystemDao.getInstance();
                result = true;
                jObject = new JSONObject();
                jObject.put("CHECK", "N"); //沒有資料設為 N
                crs1 = systemDao.checkUnitForMail(argJsonObj);
                if (crs1.next()) {
                    jObject.put("CHECK", "Y"); //有資料設為 Y
                }
                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doSaveByOP09A06Q":
                systemDao = SystemDao.getInstance();
                result = systemDao.INSERTMAIL_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogOP09A06(), "OP09A06Q_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doUpdateByOP09A06Q":
                systemDao = SystemDao.getInstance();
                beforeModifyJobj = ((JSONArray) systemDao.queryMAIL_PARAByIdOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                result = systemDao.UPDATEMAIL_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogOP09A06(), beforeModifyJobj, argJsonObj, "", "", "", "OP09A06Q_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP09A06Q_01 ------End
            case "getMailInfo":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                this.setJqGridData(returnJasonObj, mailnoticeDao.getMailInfo(argJsonObj));
                break;
            case "getMailUnit":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                this.setDDLData(returnJasonObj, mailnoticeDao.getUnit(argJsonObj));
                break;
            case "checkUnitForMailN":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                result = true;
                jObject = new JSONObject();
                jObject.put("CHECK", "N"); //沒有資料設為 N
                crs1 = mailnoticeDao.checkUnitForMail(argJsonObj);
                if (crs1.next()) {
                    jObject.put("CHECK", "Y"); //有資料設為 Y
                }
                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doSaveByOP09A07Q":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                result = mailnoticeDao.add(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
//                        NPALOG2 = new NPALOG2Util();
//			NPALOG2.WriteQueryLog(LOGOPTYPE.N, "","","", "", resultDataArray, this.GetModifyLogOP09A07(), "OP09A07Q_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "UPD_PARAMETER_SIGN":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                resultDataArray.put(mailnoticeDao.getMailInfo(argJsonObj));
//                        NPALOG2 = new NPALOG2Util();
//			NPALOG2.WriteQueryLog(LOGOPTYPE.N, "","","", "", resultDataArray, this.GetModifyLogOP09A07(), "OP09A07Q_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doUpdateByOP09A07Q":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                beforeModifyJobj = ((JSONArray) mailnoticeDao.getMailOriginal(argJsonObj.getInt("OP_SEQ_NO"))).getJSONObject(0);
                result = mailnoticeDao.UPDATEMAIL_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
//                        NPALOG2 = new NPALOG2Util();
//                        NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogOP09A06(), beforeModifyJobj, argJsonObj, "", "", "", "OP09A06Q_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "doDeleteByOP09A07Q":
                mailnoticeDao = OPDT_MAIL_NOTICE.getInstance();
                result = mailnoticeDao.DELETEWD_PARA(argJsonObj);
                if (!result) {
                    msg = "請洽系統管理員";
                }
                argJsonObj.put("msg", msg);
                resultDataArray.put(argJsonObj);
//                NPALOG2 = new NPALOG2Util();
//                NPALOG2.WriteQueryLog(LOGOPTYPE.D, "", "", "", "", resultDataArray, this.GetLog09A04QDEL(), "OP09A04Q_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
        }
    }

    private String GetFrontOP09A01(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        if (!jsonObject.getString("OP_SCHDL_DT_S").equals("")) {
            returnString.append(String.format("&%s=%s", "排程日期起日", jsonObject.getString("OP_SCHDL_DT_S").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_SCHDL_DT_E").equals("")) {
            returnString.append(String.format("&%s=%s", "排程日期迄日", jsonObject.getString("OP_SCHDL_DT_E").replace("/", "").replace(":", "").replace(" ", "")));
        }

        if (!jsonObject.getString("OP_PRCD_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "作業類別", jsonObject.getString("OP_PRCD_NM")));
        }

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLogForOP09A01Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_PRCD_CD", "作業類別代碼");
        logCols.put("OP_PRCD_NM", "作業類別名稱");
        logCols.put("OP_SCHDL_DT_S", "排程開始日期");
        logCols.put("OP_SCHDL_TM_S", "排程開始時間");
        logCols.put("OP_SCHDL_DT_E", "排程結束日期");
        logCols.put("OP_SCHDL_TM_E", "排程結束時間");
        logCols.put("OP_SUCCESS_NUM", "總筆數");
        logCols.put("OP_TOTAL_NUM", "成功筆數");

        return logCols;
    }

    private String GetFrontOP09A02(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        if (!jsonObject.getString("OP_QUERY_DT_TM_S").equals("")) {
            returnString.append(String.format("&%s=%s", "查詢日期起日", jsonObject.getString("OP_QUERY_DT_TM_S").replace("/", "").replace(":", "").replace(" ", "")));
        }
        if (!jsonObject.getString("OP_QUERY_DT_TM_E").equals("")) {
            returnString.append(String.format("&%s=%s", "查詢日期迄日", jsonObject.getString("OP_QUERY_DT_TM_E").replace("/", "").replace(":", "").replace(" ", "")));
        }

        return returnString.substring(1).toString();
    }

    private HashMap<String, String> GetLogForOP09A02Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_QUERY_NM", "功能項");
        logCols.put("OP_WEB_COUNT", "WEB使用筆數");
        logCols.put("OP_ANDROID_COUNT", "ANDROID使用筆數");
        logCols.put("OP_IOS_COUNT", "IOS使用筆數");
        logCols.put("OP_STATUS_S", "查有資料筆數");
        logCols.put("OP_STATUS_E", "查無資料筆數");
        logCols.put("OP_STATUS_T", "總筆數");

        return logCols;
    }

    private String GetFrontOP09A03(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        if (!jsonObject.getString("OP_ROLE_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "角色", jsonObject.getString("OP_ROLE_NM")));
        }
        if (!jsonObject.getString("OP_FUNC_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "功能群組", jsonObject.getString("OP_FUNC_NM")));
        }
        if (!jsonObject.getString("OP_ENABLED_NM").equals("")) {
            returnString.append(String.format("&%s=%s", "是否啟用", jsonObject.getString("OP_ENABLED_NM")));
        }

        if (returnString.length() > 0) {
            return returnString.substring(1).toString();
        } else {
            return returnString.toString();
        }
    }

    private String GetFrontOP09A04(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        if (!jsonObject.getString("OP_D_UNIT_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "警局單位", jsonObject.getString("OP_D_UNIT_NM")));
        }
        if (!jsonObject.getString("OP_B_UNIT_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "分局單位", jsonObject.getString("OP_B_UNIT_NM")));
        }

        if (returnString.length() > 0) {
            return returnString.substring(1).toString();
        } else {
            return returnString.toString();
        }
    }

    private HashMap<String, String> GetLogForOP09A04Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_B_UNIT_NM", "單位");
        logCols.put("OP_DOC_WD1", "招領公告發文字");
        logCols.put("OP_YN_GET_NO1_CHT", "是否開啟自動取號");
        logCols.put("OP_DOC_WD2", "領回公告發文字");
        logCols.put("OP_YN_GET_NO2_CHT", "是否開啟自動取號");

        return logCols;
    }

    //新增修改發文字
    private HashMap<String, String> GetModifyLogOP09A04() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_D_UNIT_NM", "單位總局名稱");
        logCols.put("OP_B_UNIT_NM", "單位分局名稱");
        logCols.put("OP_DOC_WD1", "招領公告文號–字");
        logCols.put("OP_YN_GET_NO1", "招領是否開啟自動取號");
        logCols.put("OP_DOC_WD2", "領回公告文號–字");
        logCols.put("OP_YN_GET_NO2", "領回是否開啟自動取號");

        return logCols;
    }
    //新增修改MAIL

    private HashMap<String, String> GetModifyLogOP09A06() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_D_UNIT_NM", "單位總局名稱");
        logCols.put("OP_B_UNIT_NM", "單位分局名稱");
        logCols.put("OP_MAIL_CONTENT", "信件內容");

        return logCols;
    }

    private String GetFrontOP09A06(JSONObject jsonObject) {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        if (!jsonObject.getString("OP_D_UNIT_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "警局單位", jsonObject.getString("OP_D_UNIT_NM")));
        }
        if (!jsonObject.getString("OP_B_UNIT_CD").equals("")) {
            returnString.append(String.format("&%s=%s", "分局單位", jsonObject.getString("OP_B_UNIT_NM")));
        }

        if (returnString.length() > 0) {
            return returnString.substring(1).toString();
        } else {
            return returnString.toString();
        }
    }

    private HashMap<String, String> GetLogForOP09A06Query() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();

        logCols.put("OP_B_UNIT_NM", "單位");
        logCols.put("OP_MAIL_CONTENT", "信件內容");

        return logCols;
    }

    //刪除發文字
    private HashMap<String, String> GetLog09A04QDEL() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "刪除發文字序號");
        logCols.put("OP_D_UNIT_NM", "發文字總局");
        logCols.put("OP_B_UNIT_NM", "發文字分局");
        return logCols;
    }
}
