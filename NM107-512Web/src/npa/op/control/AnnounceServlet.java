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
import npa.op.dao.OPDT_I_ANNOUNCE;
import npa.op.dao.OPDT_I_AN_DL;
import npa.op.dao.OPDT_I_FNSH;
import npa.op.dao.OPDT_I_PUAN_DL;
import npa.op.dao.OPDT_I_PU_CLAIM;
//import npa.op.util.AesCrypt;
import npa.op.util.DateUtil;
import npa.op.util.ExportUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.vo.User;

/**
 * 拾得遺失物招領、招領期滿、領回公告、領回期滿
 */
@WebServlet("/AnnounceServlet")
public class AnnounceServlet extends AjaxBaseServlet {

    private static final long serialVersionUID = 1L;
    Logger log = Logger.getLogger(AnnounceServlet.class);

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
        OPDT_I_ANNOUNCE iAnnounceDao = new OPDT_I_ANNOUNCE();
        OPDT_I_AN_DL iAnDlDao = new OPDT_I_AN_DL();
        OPDT_I_PUAN_DL iPuanDlDao = new OPDT_I_PUAN_DL();
        OPDT_I_FNSH iFnshDao = new OPDT_I_FNSH();
        OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
        JSONArray resultjArray = new JSONArray();
        String[] delId = null;

        boolean result;
        switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {
            //region OP03A01Q_01.jsp ------Start	拾得物招領維護資料作業 
            case "getAnn1GridList":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                resultDataArray = iAnnounceDao.queryAnnounceByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"), user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "showAnn1GridList":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                resultDataArray = iAnnounceDao.showAnnounceByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "insertAnn1List":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                result = iAnnounceDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogAnn(), "OP03B01M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateAnn1List":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();

                JSONObject beforeModifyJobj = ((JSONArray) iAnnounceDao.queryAnnounceByBasicIdOriginal(argJsonObj.getInt("OP_BASIC_SEQ_NO"))).getJSONObject(0);
                result = iAnnounceDao.update(argJsonObj);
                resultDataArray.put(argJsonObj);

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogAnn(), beforeModifyJobj, argJsonObj, "", "", "", "OP03B01M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "getAnn2GridList":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                resultDataArray = iAnnounceDao.getAnnounceByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "showAnn2GridList":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                resultDataArray = iAnnounceDao.showAnnounce2ByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "checkYNAnn1":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                jObject = new JSONObject();
                crs1 = iAnnounceDao.QueryAllForAnn1(argJsonObj);
                String check = "N";
                jObject.put("CHECK", check); //沒有資料設為 N
                if (crs1.next()) {
                    if (crs1.getString("OP_YN_AN").equals("2")) { //如果已經網路公告 設為 2
                        jObject.put("CHECK", "2");
                    } else { //內部公告 設為 1
                        jObject.put("CHECK", "1");
                    }
                }
                resultDataArray.put(jObject);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateAnn2List":
                iAnnounceDao = OPDT_I_ANNOUNCE.getInstance();
                result = iAnnounceDao.updateForStatus(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.Q, "", "", "", "", resultDataArray, this.GetLog03B02MQuery(), "OP03B02M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP03A01Q_01.jsp ------End
            //region OP03A02Q_01.jsp ------Start	拾得物招領期滿維護資料作業    
            case "getIAnDlGridList":
                iAnDlDao = OPDT_I_AN_DL.getInstance();
                resultDataArray = iAnDlDao.queryAnDlByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"), user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "showIAnDlGridList":
                iAnDlDao = OPDT_I_AN_DL.getInstance();
                resultDataArray = iAnDlDao.showAnDlByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "insertIAnDlList":
                iAnDlDao = OPDT_I_AN_DL.getInstance();
                result = iAnDlDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogAnDl(), "OP03B03M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateIAnDlList":
                iAnDlDao = OPDT_I_AN_DL.getInstance();
                beforeModifyJobj = ((JSONArray) iAnDlDao.queryAnDlByBasicIdOriginal(argJsonObj.getInt("OP_BASIC_SEQ_NO"))).getJSONObject(0);
                result = iAnDlDao.update(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogAnDl(), beforeModifyJobj, argJsonObj, "", "", "", "OP03B03M_01.jsp", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP03A02Q_01.jsp ------End
            //region OP04A01Q_01.jsp ------Start	拾得人領回公告    
            case "showIPuanAnnGridList":
                iAnDlDao = OPDT_I_AN_DL.getInstance();
                resultDataArray = iAnDlDao.queryIPuanAnnByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP04A01Q_01.jsp ------End
            //region OP04A02Q_01.jsp ------Start	拾得人領回公告期滿  
            case "getIPuanDlGridList":
                iPuanDlDao = OPDT_I_PUAN_DL.getInstance();
                resultDataArray = iPuanDlDao.queryPuanDlByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "insertIPuanDlList":
                iPuanDlDao = OPDT_I_PUAN_DL.getInstance();
                result = iPuanDlDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogPuanDl(), "OP04B01M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateIPuanDlList":
                iPuanDlDao = OPDT_I_PUAN_DL.getInstance();
                beforeModifyJobj = ((JSONArray) iPuanDlDao.queryPuanDlByBasicIdOriginal(argJsonObj.getInt("OP_BASIC_SEQ_NO"))).getJSONObject(0);
                result = iPuanDlDao.update(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogPuanDl(), beforeModifyJobj, argJsonObj, "", "", "", "OP04B01M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            //region OP04A02Q_01.jsp ------End
            //region OP05A01Q_01.jsp ------Start	結案資料
            case "GetReportList":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                crs1 = iPuClaimDao.GetClaimReportList(argJsonObj);
                String FILE_EXPL = "",
                 CLAIM_TYPE = "",
                 SEQ = "";
                while (crs1.next()) {
                    if (crs1.getString("OP_YN_LOSER").equals("1")) { //是有認領權人
                        FILE_EXPL = crs1.getString("OP_PUCP_NAME");
                        CLAIM_TYPE = crs1.getString("TABLEFROM");
                        SEQ = crs1.getString("OP_SEQ_NO");
                    }
                }
                // 一張　遺失（拾得）物領據
                jObject = new JSONObject();
                jObject.put("FILE_NAME", "領取拾得物領據"); //檔案名稱
                jObject.put("REPORT_TYPE", "oP02A07Q.doc"); //報表類別
                jObject.put("FILE_EXPL", FILE_EXPL); //說明
                jObject.put("CLAIM_TYPE", CLAIM_TYPE); //表格類別
                jObject.put("SEQ", SEQ); //認領人序號
                resultDataArray.put(jObject);
                //拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書
                jObject = new JSONObject();
                jObject.put("FILE_NAME", "拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書"); //檔案名稱
                jObject.put("REPORT_TYPE", "oP02A11Q.doc"); //報表類別
                jObject.put("FILE_EXPL", FILE_EXPL); //說明
                jObject.put("CLAIM_TYPE", CLAIM_TYPE); //表格類別
                jObject.put("SEQ", SEQ); //認領人序號
                resultDataArray.put(jObject);
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            //region OP08A03Q_01.jsp ------Start	結案資料
            case "GetReportList_500":
                iPuClaimDao = OPDT_I_PU_CLAIM.getInstance();
                crs1 = iPuClaimDao.GetClaimReportList(argJsonObj);
                FILE_EXPL = "";
                CLAIM_TYPE = "";
                SEQ = "";
                while (crs1.next()) {
                    if (crs1.getString("OP_YN_LOSER").equals("1")) { //是有認領權人
                        FILE_EXPL = crs1.getString("OP_PUCP_NAME");
                        CLAIM_TYPE = crs1.getString("TABLEFROM");
                        SEQ = crs1.getString("OP_SEQ_NO");
                    }
                }
                // 一張　遺失（拾得）物領據
                jObject = new JSONObject();
                jObject.put("FILE_NAME", "領取拾得物領據"); //檔案名稱
                jObject.put("REPORT_TYPE", "oP02A07Q.doc"); //報表類別
                jObject.put("FILE_EXPL", FILE_EXPL); //說明
                jObject.put("CLAIM_TYPE", CLAIM_TYPE); //表格類別
                jObject.put("SEQ", SEQ); //認領人序號
                resultDataArray.put(jObject);
                //拾得人讓與拾得物所有權收據
                jObject = new JSONObject();
                jObject.put("FILE_NAME", "拾得人讓與拾得物所有權收據(二聯)"); //檔案名稱
                jObject.put("REPORT_TYPE", "OP08A03Q.doc"); //報表類別
                jObject.put("FILE_EXPL", FILE_EXPL); //說明
                jObject.put("CLAIM_TYPE", CLAIM_TYPE); //表格類別
                jObject.put("SEQ", SEQ); //認領人序號
                resultDataArray.put(jObject);
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "getIFnshGridList":
                iFnshDao = OPDT_I_FNSH.getInstance();
                resultDataArray = iFnshDao.queryFnshByBasicId(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "insertIFnshList":
                iFnshDao = OPDT_I_FNSH.getInstance();
                result = iFnshDao.add(argJsonObj);
                resultDataArray.put(argJsonObj);
                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetModifyLogFNSH(), "OP05B01M_01", user, user.getUserName(), user.getUnitName(), "");
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "updateIFnshList":
                iFnshDao = OPDT_I_FNSH.getInstance();
                beforeModifyJobj = ((JSONArray) iFnshDao.queryFnshByBasicIdOriginal(argJsonObj.getInt("OP_BASIC_SEQ_NO"))).getJSONObject(0);
                result = iFnshDao.update(argJsonObj);
                resultDataArray.put(argJsonObj);

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteModifyLog(LOGOPTYPE.U, GetModifyLogFNSH(), beforeModifyJobj, argJsonObj, "", "", "", "OP05B01M_01", user);
                this.setFormData(returnJasonObj, resultDataArray);
                break;
            case "batchSaveFinish":
                iFnshDao = OPDT_I_FNSH.getInstance();
                String[] tmpSEQ_UPL = argJsonObj.get("OP_SEQ_NO").toString().split(",");
                for (int i = 0; i < tmpSEQ_UPL.length; i++) {
                    jObject = new JSONObject();
                    jObject.put("OP_SEQ_NO", tmpSEQ_UPL[i]);
                    jObject.put("OP_FS_REC_NM", argJsonObj.get("OP_FS_REC_NM").toString());
                    resultDataArray.put(jObject);
                }
                this.setFormData(returnJasonObj, iFnshDao.batchAdd(argJsonObj));

                NPALOG2 = new NPALOG2Util();
                NPALOG2.WriteQueryLog_1(LOGOPTYPE.N, "", "", "", "", resultDataArray, this.GetLog0501BatchFNSH(), "OP05A01Q_01", user, user.getUserName(), user.getUnitName(),argJsonObj.getString("OPR_KIND"),argJsonObj.getString("OPR_PURP"));  //202403 
                break;
            //region OP05A01Q_01.jsp ------End
        }
    }
    //新增修改ANN

    private HashMap<String, String> GetModifyLogAnn() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_AN_B_UNIT_NM", "發文單位");
        logCols.put("OP_CABINET_NO", "櫃號");
        logCols.put("OP_AN_DATE_BEG", "公告日期");
        logCols.put("OP_DOC_DT", "發文日期");
        logCols.put("OP_DOC_WD", "發文文號-字第");
        logCols.put("OP_DOC_NO", "發文文號-號");
        logCols.put("OP_AN_DATE_END", "公告期滿日期");
        logCols.put("OP_AN_CONTENT", "公告內容");
        logCols.put("OP_AN_REMARK", "備註");

        return logCols;
    }
    //發布網路公告

    private HashMap<String, String> GetLog03B02MQuery() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "公告序號發佈網路公告");
        return logCols;
    }
    //新增修改ANN

    private HashMap<String, String> GetModifyLogAnDl() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_PR_UNIT_NM", "處理單位");
        logCols.put("OP_PR_STAFF_NM", "處理人員");
        logCols.put("OP_PR_DATE", "處理日期");
        logCols.put("OP_PR_STAT_DESC", "處理情形描述");
        logCols.put("OP_YN_AUCTION", "是否拍賣");
        logCols.put("OP_NTC_PUPO_DT", "通知領回紀錄");
        logCols.put("OP_PUPO_ANDTEND", "公告期滿日期");
        logCols.put("OP_PUPO_DOC_WD", "拾得人領回公告文號–字");
        logCols.put("OP_PUPO_DOC_NO", "拾得人領回公告文號–號");
        logCols.put("OP_PUPOANUNITNM", "拾得人領回公告發文單位名稱");
        logCols.put("OP_PUPO_AN_CONT", "拾得人領回公告內容");

        return logCols;
    }
    //新增修改PUAN_DL

    private HashMap<String, String> GetModifyLogPuanDl() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SD_UNIT_NM", "送交單位");
        logCols.put("OP_SD_NAME", "送交人姓名");
        logCols.put("OP_SD_TITLE", "送交人職稱");
        logCols.put("OP_SD_DATE", "送交日期");
        logCols.put("OP_AC_REG_ATNO", "接受之地方自治團體");
        logCols.put("OP_AC_NAME", "接受人姓名");
        logCols.put("OP_AC_TITLE", "接受人職稱");

        return logCols;
    }
    //結案 

    private HashMap<String, String> GetLog0501BatchFNSH() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_SEQ_NO", "受理基本序號批次結案");
        logCols.put("OP_FS_REC_NM", "結案紀錄");
        return logCols;
    }
    //新增修改FNSH

    private HashMap<String, String> GetModifyLogFNSH() {
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        logCols.put("OP_FS_UNIT_NM", "結案單位");
        logCols.put("OP_FS_STAFF_NM", "結案人員");
        logCols.put("OP_FS_DATE", "結案日期");
        logCols.put("OP_RT_UNIT_NM", "發還單位");
        logCols.put("OP_RT_STAFF_NM", "發還人員");
        logCols.put("OP_RT_DATE", "發還日期");
        logCols.put("OP_FS_REC_NM", "結案紀錄");
        logCols.put("OP_FS_STAT_DESC", "結案情形描述");

        return logCols;
    }
}
