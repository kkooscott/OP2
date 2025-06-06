package npa.op.base;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import npa.op.util.ExceptionUtil;
import npa.op.vo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import com.syscom.util.StringUtils;
import npa.op.util.StringUtil;

/**
 * 之後可以改成不要用Servlet而是用像struts一樣的Action,就可以定義全域變數而不需要一直透過參數傳遞
 * 
 * @author Barry
 * 
 */
public abstract class AjaxBaseServlet extends BaseServlet {
    /**
     * Ajax從前端傳到後端時所指定的動作
     */
    public static final String AJAX_REQ_ACTION_KEY = "ajaxAction";

    /**
     * Ajax傳回到前端時預設使用的成功訊息Key
     */
    public static final String AJAX_RES_MSG_SUCCESS_KEY = "successMsg";
    /**
     * Ajax傳回到前端時預設使用的錯誤訊息Key
     */
    public static final String AJAX_RES_MSG_EXCEPTION_KEY = "exceptionMsg";
    /**
     * Ajax傳回到前端時預設使用的訊息物件名稱
     */
    // public static final String AJAX_RES_MSG_OBJECT_KEY = "msgData";

    /**
     * Ajax傳回到jqGrid時填入欄位資料預設的Key
     */
    public static final String AJAX_RES_JQGRID_DATA_KEY = "rows";
    /**
     * Ajax傳回到前端時填入表單資料的Key
     */
    public static final String AJAX_RES_FORM_DATA_KEY = "formData";

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	JSONObject returnJasonObj = new JSONObject();
	JSONArray jArray = null;
	JSONObject argJsonObj = new JSONObject();
	JSONObject msgJsonObj = new JSONObject();
	// 將傳入的參數傳成json
	Enumeration<String> paras = request.getParameterNames();
	String para;
	while (paras.hasMoreElements()) {
	    para = paras.nextElement();
	    //argJsonObj.put(para, StringUtils.escapeHtml(request.getParameter(para)));
	    argJsonObj.put(para, StringUtils.encode((String)validator.Validator.validateAttr(StringUtil.getString(request.getParameter(para)))));
	}

	try {
	    HttpSession session = request.getSession(true);
	    User user = (User) session.getAttribute("user");
	    if (user == null){
	    	returnJasonObj.put(AJAX_RES_MSG_EXCEPTION_KEY, "無法取得使用者資訊，請重新登入");
	    	//throw new Exception("無法取得使用者資訊，請重新登入");
	    }
	    else{
	    	executeAjax(request, response, session, user, argJsonObj, returnJasonObj);
	    }
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	    // msgJsonObj.put("exceptionMsg", ExceptionUtil.toString(e));//
	    // 存放列外的錯誤訊息
	    // msgJsonObj.put(AJAX_RES_MSG_EXCEPTION_KEY, e.getMessage());//
	    // 存放列外的錯誤訊息
	    returnJasonObj.put(AJAX_RES_MSG_EXCEPTION_KEY, e.getMessage());
	} finally {
	      // returnJasonObj.put(AJAX_RES_MSG_OBJECT_KEY, msgJsonObj);
                String accept = request.getHeader("Accept");
                if (accept.contains("application/json")) {// 判斷Browser是否支援json
                    response.setContentType("application/json");
                } else {
                    response.setContentType("text/html");// 設成text/html ie7才不會有問題
                }
                    response.setCharacterEncoding("UTF-8");
                if (argJsonObj.has("ajaxAction")){
                    if (argJsonObj.getString("ajaxAction").substring(0,3).equals("OP0")){

                    }else{
                        response.getWriter().write(returnJasonObj.toString());
                        response.getWriter().flush();
                    }
                }else{
                        response.getWriter().write(returnJasonObj.toString());
                    response.getWriter().flush();
                }
	}

    }

    /**
     * 設定前端網頁的jqGrid資料
     * 
     * @param returnJasonObj
     * @param data
     */
    public void setJqGridData(JSONObject returnJasonObj, Object data) {
	returnJasonObj.put(AJAX_RES_JQGRID_DATA_KEY, data);// rows存放jqGrid的資料
    }

    /**
     * 設定前端網頁的表單資料
     * 
     * @param returnJasonObj
     * @param data
     */
    public void setFormData(JSONObject returnJasonObj, Object data) {
	returnJasonObj.put(AJAX_RES_FORM_DATA_KEY, data);// formData存放form表單的資料
    }

    /**
     * 設定前端網頁的訊息內容
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setReturnMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_SUCCESS_KEY, msg);// infoMsg存放一般的訊息
    }
    
    /**
     * 設定前端網頁的DDL資料
     * 
     * @param returnJasonObj
     * @param data
     */
    public void setDDLData(JSONObject returnJasonObj, Object data) {
	returnJasonObj.put("result", data);// rows存放jqGrid的資料
    }

    /**
     * 請implements此method，盡量不要catch exception，若有需要則請在throws出來exception。
     * 程式的寫法可參考npa.le2.control.CrowdActivityReportServlet此範例程式
     * 
     * @param request
     *            原來的HttpServletRequest物件
     * @param response
     *            原來的HttpServletResponse物件
     * @param argJsonObj
     *            前端所傳送過來的參數請透過此jason物件取得，盡量不要使用request.getParameter
     * @param msgJsonObj
     *            存放要傳送到前端的成功與錯誤訊息
     * @param returnJasonObj
     *            存放前端畫面所需要使用的jqGrid與表單資料
     * @throws Exception
     */
    protected abstract void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user,
	    JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception;
}
