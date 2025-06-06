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
import npa.op.dao.OPDT_I_PUAN_DL;
import npa.op.dao.OPDT_I_PU_BASIC;
import npa.op.dao.OPDT_I_PU_CLAIM;
import npa.op.dao.OPDT_I_PU_DETAIL;
//import npa.op.util.AesCrypt;
import npa.op.util.DateUtil;
import npa.op.util.ExportUtil;
import npa.op.util.NPALOG2Util;
import static npa.op.util.StringUtil.replaceWhiteChar;
import npa.op.vo.User;

/**
 * 伍佰元專區受理基本資料、明細
 */
@WebServlet("/PuValueUnder500Servlet")
public class PuValueUnder500Servlet extends AjaxBaseServlet {
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(PuValueUnder500Servlet.class);

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
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                OPDT_I_PU_DETAIL iPuDetailDao = new OPDT_I_PU_DETAIL();
                OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                OPDT_I_PUAN_DL iPuanDlDao = new OPDT_I_PUAN_DL();
                OPDT_I_FNSH iFnshDao = new OPDT_I_FNSH();
		JSONArray resultjArray  = new JSONArray();
                String[] delId = null;
		
		boolean result;
		switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {			
                //region OP08A01Q_01.jsp ------Start	伍佰元專區 受理維護
                    case "IPuBasicQueryForDown":			
                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                            crs1 = iPuBasicDao.IPuBasicQueryForValueUnder500(argJsonObj);
                            while (crs1.next()) {
                                    jObject = new JSONObject();
                                    jObject.put("BASIC_SEQ",crs1.getString("OP_SEQ_NO")); //基本資料序號
                                    jObject.put("OP_AC_RCNO",crs1.getString("OP_AC_RCNO")); //收據編號
                                    jObject.put("OP_AC_UNIT_NM",crs1.getString("OP_AC_UNIT_NM")); //受理單位
                                    jObject.put("OP_PUPO_TP_NM",crs1.getString("OP_PUPO_TP_NM")); //拾得人類別
                                    jObject.put("OP_PUOJ_ATTR_NM",crs1.getString("OP_PUOJ_ATTR_NM")); //物品屬性
                                    jObject.put("OP_PUPO_NAME",crs1.getString("OP_PUPO_NAME")); //拾得人姓名
                                    jObject.put("OP_PUPO_IDN",crs1.getString("OP_PUPO_IDN")); //身分證/其他證號
                                    jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_PU_DATE"), crs1.getString("OP_PU_TIME"))); //拾得日期時間
                                    jObject.put("OP_PU_PLACE",crs1.getString("OP_PU_CITY_NM") + crs1.getString("OP_PU_TOWN_NM") + crs1.getString("OP_PU_PLACE")); //拾得地點
                                    if( crs1.getString("OP_DEL_FLAG").equals("1") ){
                                        jObject.put("OP_CURSTAT_NM", "已刪除"); //目前狀態
                                    }else{
                                        jObject.put("OP_CURSTAT_NM",crs1.getString("OP_CURSTAT_NM")); //目前狀態
                                    }
                                    jObject.put("DEL_FLAG",crs1.getString("OP_DEL_FLAG")); //是否刪除

                                    resultDataArray.put(jObject);
                            }
                            
                            // 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
                            //NPALOG2 = new NPALOG2Util();
                            
                            //NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", "", "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString()+".jsp", user, user.getUserName(), user.getUnitName(), "");
//                            if ( argJsonObj.getString("ACTION_TYPE").equals("OP08A02Q_01") ){
//                                NPALOG2.WriteQueryLog_New(NPALOG2Util.LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", argJsonObj.getString("OP_PUCP_NAME"), "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), ""
//                                    ,"","","","",argJsonObj.getString("OP_AC_RCNO"));
//                            }else{
//                                NPALOG2.WriteQueryLog_New(NPALOG2Util.LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", "", "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), ""
//                                    ,"","","","",argJsonObj.getString("OP_AC_RCNO"));
//                            }

                            this.setJqGridData(returnJasonObj, resultDataArray);
                    break;
                    case "IPuBasicQueryForDownSearch":			
                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                            crs1 = iPuBasicDao.IPuBasicQueryForValueUnder500(argJsonObj);
                            while (crs1.next()) {
                                    jObject = new JSONObject();
                                    jObject.put("BASIC_SEQ",crs1.getString("OP_SEQ_NO")); //基本資料序號
                                    jObject.put("OP_AC_RCNO",crs1.getString("OP_AC_RCNO")); //收據編號
                                    jObject.put("OP_AC_UNIT_NM",crs1.getString("OP_AC_UNIT_NM")); //受理單位
                                    jObject.put("OP_PUPO_TP_NM",crs1.getString("OP_PUPO_TP_NM")); //拾得人類別
                                    jObject.put("OP_PUOJ_ATTR_NM",crs1.getString("OP_PUOJ_ATTR_NM")); //物品屬性
                                    jObject.put("OP_PUPO_NAME",crs1.getString("OP_PUPO_NAME")); //拾得人姓名
                                    jObject.put("OP_PUPO_IDN",crs1.getString("OP_PUPO_IDN")); //身分證/其他證號
                                    jObject.put("OP_PU_DTTM", DateUtil.to7TwDateTime(crs1.getString("OP_PU_DATE"), crs1.getString("OP_PU_TIME"))); //拾得日期時間
                                    jObject.put("OP_PU_PLACE",crs1.getString("OP_PU_CITY_NM") + crs1.getString("OP_PU_TOWN_NM") + crs1.getString("OP_PU_PLACE")); //拾得地點
                                    if( crs1.getString("OP_DEL_FLAG").equals("1") ){
                                        jObject.put("OP_CURSTAT_NM", "已刪除"); //目前狀態
                                    }else{
                                        jObject.put("OP_CURSTAT_NM",crs1.getString("OP_CURSTAT_NM")); //目前狀態
                                    }
                                    jObject.put("DEL_FLAG",crs1.getString("OP_DEL_FLAG")); //是否刪除

                                    resultDataArray.put(jObject);
                            }
                            // 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
                            //NPALOG2 = new NPALOG2Util();
                            
                            //NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", "", "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString()+".jsp", user, user.getUserName(), user.getUnitName(), "");
//                            if ( argJsonObj.getString("ACTION_TYPE").equals("OP08A02Q_01") ){
//                                NPALOG2.WriteQueryLog_New(NPALOG2Util.LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", argJsonObj.getString("OP_PUCP_NAME"), "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), argJsonObj.getString("OPR_PURP")
//                                    ,argJsonObj.getString("OPR_KIND"),"","","",argJsonObj.getString("OP_AC_RCNO"));
//                            }else{
//                                NPALOG2.WriteQueryLog_New(NPALOG2Util.LOGOPTYPE.Q, GetFrontBasic(argJsonObj, argJsonObj.getString("ACTION_TYPE") ), "", "", "", resultDataArray, this.GetLogForBasicQuery( argJsonObj.getString("ACTION_TYPE") ), argJsonObj.getString("ACTION_TYPE").toString(), user, user.getUserName(), user.getUnitName(), argJsonObj.getString("OPR_PURP")
//                                    ,argJsonObj.getString("OPR_KIND"),"","","",argJsonObj.getString("OP_AC_RCNO"));
//                            }

                            this.setJqGridData(returnJasonObj, resultDataArray);
                    break;
                //region OP08A01Q_01.jsp ------End
                //region OP08A03Q_01.jsp ------Start	伍佰元專區 結案資料 
                    case "getIFnshGridList":
                        iFnshDao = OPDT_I_FNSH.getInstance();
                        resultDataArray = iFnshDao.queryFnshByBasicIdFor500(argJsonObj.getInt("OP_BASIC_SEQ_NO"));
                        this.setFormData(returnJasonObj, resultDataArray);
                    break;
                    case "insertIFnshList":
                        iFnshDao = OPDT_I_FNSH.getInstance();
			result = iFnshDao.addFor500(argJsonObj);
			resultDataArray.put(argJsonObj);
                        // 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
                        //NPALOG2 = new NPALOG2Util();
			//NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.N, "","","", "", resultDataArray, this.GetModifyLogFNSH(), "OP08B01M_01", user, user.getUserName(), user.getUnitName(), "");
                        this.setFormData(returnJasonObj, resultDataArray);
                    break;
                    case "updateIFnshList":
                        iFnshDao = OPDT_I_FNSH.getInstance();
                        JSONObject beforeModifyJob = ((JSONArray)iFnshDao.queryFnshByBasicIdOriginal(argJsonObj.getInt("OP_BASIC_SEQ_NO"))).getJSONObject(0);
			result = iFnshDao.updateFor500(argJsonObj);
			resultDataArray.put(argJsonObj);
                        // 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
                        //NPALOG2 = new NPALOG2Util();
                        //NPALOG2.WriteModifyLog(NPALOG2Util.LOGOPTYPE.U, GetModifyLogFNSH(), beforeModifyJob, argJsonObj, "", "", "", "OP08B01M_01", user);
                        this.setFormData(returnJasonObj, resultDataArray);
                    break;
                //region OP08A03Q_01.jsp ------End
		}
	}
        
        //查詢條件欄位(02A02Q)
		private String GetFrontBasic(JSONObject jsonObject, String ACTION_TYPE)
	    {
			StringBuilder returnString = new StringBuilder();
			LinkedHashMap logCols =new LinkedHashMap <String, String>();
                        ///受理單位
                        if( !jsonObject.getString("OP_UNITLEVEL4").equals("") ){
                            returnString.append(String.format("&%s=%s", "受理單位", jsonObject.getString("OP_UNITLEVEL4_NM")));
                        } else if( !jsonObject.getString("OP_UNITLEVEL3").equals("") ){
                            returnString.append(String.format("&%s=%s", "受理單位", jsonObject.getString("OP_UNITLEVEL3_NM")));
                        } else if( !jsonObject.getString("OP_UNITLEVEL2").equals("")){
                            returnString.append(String.format("&%s=%s", "受理單位", jsonObject.getString("OP_UNITLEVEL2_NM")));
                        }
                        if (!jsonObject.getString("OP_AC_RCNO").equals("")){
				returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
			}
                        if (!jsonObject.getString("OP_PUPO_TP_CD").equals("")){
				returnString.append(String.format("&%s=%s", "拾得人類別", jsonObject.getString("OP_PUPO_TP_NM")));
			}
			if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
				returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
			}
                        if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                                returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                        }
                        if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
				returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
			}
                        if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                                returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                        }
                        if (!jsonObject.getString("OP_PUOJ_ATTR_CD").equals("")){
                                returnString.append(String.format("&%s=%s", "物品屬性", jsonObject.getString("OP_PUOJ_ATTR_NM")));
                        }
                        if( ACTION_TYPE.equals("OP08A01Q_01")  ){
                            if (!jsonObject.getString("OP_DEL_FLAG").equals("")){
                                    returnString.append(String.format("&%s=%s", "刪除案件", jsonObject.getString("OP_DEL_FLAG")));
                            }
                        }else if ( ACTION_TYPE.equals("OP08A02Q_01") ){
                            if (!jsonObject.getString("OP_PUCP_NAME").equals("")){
                                    returnString.append(String.format("&%s=%s", "認領人", jsonObject.getString("OP_PUCP_NAME")));
                            }
                        }else if ( ACTION_TYPE.equals("OP08A02Q_01") ){
                            if (!jsonObject.getString("OP_CURSTAT_CD").equals("")){
                                    returnString.append(String.format("&%s=%s", "目前狀態", jsonObject.getString("OP_CURSTAT_NM")));
                            }
                        }

			return returnString.substring(1).toString();
	    }
                
                private HashMap<String, String> GetLogForBasicQuery( String ACTION_TYPE )
	    {
			LinkedHashMap logCols =new LinkedHashMap <String, String>();
                        if( ACTION_TYPE.equals("OP08A02Q_01")  ){
                            logCols.put("OP_AC_RCNO", "收據編號");
                            logCols.put("OP_AC_UNIT_NM", "受理單位");
                            logCols.put("OP_PU_DTTM", "拾得日期時間");
                            logCols.put("OP_PU_PLACE", "拾得地點");
                            logCols.put("OP_CURSTAT_NM", "目前狀態");
                        }else{
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
            //新增修改FNSH
                private HashMap<String, String> GetModifyLogFNSH()
	    {
			LinkedHashMap logCols =new LinkedHashMap <String, String>();
                        logCols.put("OP_FS_UNIT_NM","結案單位");
                        logCols.put("OP_FS_STAFF_NM","結案人員");
                        logCols.put("OP_FS_DATE","結案日期");
			logCols.put("OP_RT_UNIT_NM","發還單位");
                        logCols.put("OP_RT_STAFF_NM","發還人員");
			logCols.put("OP_RT_DATE","發還日期");
			logCols.put("OP_FS_REC_NM","結案紀錄");
			logCols.put("OP_FS_STAT_DESC","結案情形描述");
                        logCols.put("OP_NTC_MODE_NM","通知方式名稱");
                        logCols.put("OP_SD_UNIT_NM","送交機關名稱");
                        logCols.put("OP_SD_NAME","送交人姓名");
                        logCols.put("OP_SD_TITLE","送交人職稱");
                        logCols.put("OP_SD_DATE","送交日期");
                        logCols.put("OP_AC_REG_ATNO","接受之地方自治團體");
                        logCols.put("OP_AC_NAME","接受人姓名");
                        logCols.put("OP_AC_TITLE","接受人職稱");
			
                        return logCols;
	    }
}
