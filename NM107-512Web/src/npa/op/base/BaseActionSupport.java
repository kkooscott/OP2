package npa.op.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import npa.op.util.StringUtil;

public abstract class BaseActionSupport extends ActionSupport {
    protected static Logger log = Logger.getLogger(BaseActionSupport.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String ERROR_MESSAGE="errorMsg";
	public final static String WARN_MESSAGE="warnMsg";
	public final static String INFO_MESSAGE="infoMsg";
	
	public BaseActionSupport() {

	}

	public String nvl(Object o){
		return StringUtil.nvl(o);
	}
	/**
	 * 設定前端網頁所要顯示的錯誤訊息
	 * @param msg 所顯示的訊息內容
	 */
	protected void setErrorMsg(HttpServletRequest req,String msg) {
		req.setAttribute(ERROR_MESSAGE, msg);
	}
	/**
	 * 設定前端網頁所要顯示的警告訊息
	 * @param msg 所顯示的訊息內容
	 */
	protected void setWarnMsg(HttpServletRequest req,String msg) {
		req.setAttribute(WARN_MESSAGE, msg);
	}
	/**
	 * 設定前端網頁所要顯示的訊息
	 * @param msg 所顯示的訊息內容
	 */
	protected void setInfoMsg(HttpServletRequest req,String msg) {
		req.setAttribute(INFO_MESSAGE, msg);
	}
	
	public String execute() throws Exception{
	    HttpServletRequest req = ServletActionContext.getRequest();
	    HttpServletResponse res= ServletActionContext.getResponse();
	    HttpSession session=req.getSession();
	    return execute(req,res,session);
	}
	
	public abstract String execute(HttpServletRequest req,HttpServletResponse res,HttpSession session) throws Exception;
}

