package npa.op.control;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import npa.op.base.AjaxBaseServlet;
import npa.op.dao.OPDT_FUNC_ROLEDao;
import npa.op.util.DateUtil;
import npa.op.vo.User;

/**
 * 入山申辦案件查詢
 */
@WebServlet("/IndexQueryServlet")
public class IndexQueryServlet extends AjaxBaseServlet {
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(IndexQueryServlet.class);

	@Override
	protected void executeAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, User user,
			JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {

		 if (argJsonObj.getString(AJAX_REQ_ACTION_KEY).equals("FUNCLIST")) {
				OPDT_FUNC_ROLEDao daoFunc = OPDT_FUNC_ROLEDao.getInstance();
				this.setFormData(returnJasonObj, daoFunc.getFuncList(user.getOwnRole()));
                        }
		
	}
}
