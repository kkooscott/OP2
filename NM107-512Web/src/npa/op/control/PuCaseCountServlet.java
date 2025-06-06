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
 * 拾得遺失物受理基本資料、明細、認領
 */
@WebServlet("/PuCaseCountServlet")
public class PuCaseCountServlet extends AjaxBaseServlet {
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(PuCaseCountServlet.class);

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
		JSONArray resultjArray  = new JSONArray();
                String[] delId = null;
		
		boolean result;
		switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {
                    case "CaseCount": //未結案件
                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                            resultDataArray = iPuBasicDao.countBasicCase(argJsonObj);
                            this.setFormData(returnJasonObj, resultDataArray);
                    break;
                    case "AnnounceCaseCount": //招領即將(7日後)期滿案件
                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                            resultDataArray = iPuBasicDao.countAnnounceCase(argJsonObj);
                            this.setFormData(returnJasonObj, resultDataArray);
                    break;
                    case "AnDlCaseCount": //領回即將(7日後)期滿案件
                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
                            resultDataArray = iPuBasicDao.countAnDlCase(argJsonObj);
                            this.setFormData(returnJasonObj, resultDataArray);
                    break;
//                    case "NoAnnounceCase": //未公告案件
//                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
//                            resultDataArray = iPuBasicDao.countBasicCase(argJsonObj);
//                            this.setFormData(returnJasonObj, resultDataArray);
//                    break;
//                    case "AnDlCase": //招領期滿案件
//                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
//                            resultDataArray = iPuBasicDao.countBasicCase(argJsonObj);
//                            this.setFormData(returnJasonObj, resultDataArray);
//                    break;
//                    case "PuanDlCase": //領回期滿案件
//                            iPuBasicDao = OPDT_I_PU_BASIC.getInstance();
//                            resultDataArray = iPuBasicDao.countBasicCase(argJsonObj);
//                            this.setFormData(returnJasonObj, resultDataArray);
//                    break;
                    case "countNetCase": //上網認領件數
                            ePuNetClmDao = OPDT_E_NET_CLAIM.getInstance();
                            resultDataArray = ePuNetClmDao.countNetCase(argJsonObj);
                            this.setFormData(returnJasonObj, resultDataArray);
                    break;
		}
	}
}
