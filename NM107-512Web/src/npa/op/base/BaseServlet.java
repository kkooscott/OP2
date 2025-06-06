package npa.op.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;

import npa.op.util.ExceptionUtil;
import npa.op.util.MessageUtil;
import npa.op.vo.User;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class BaseServlet
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected static Logger log = Logger.getLogger(BaseServlet.class);
    
    User userVo = new User();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
	    execute(request, response);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
	    execute(request, response);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
    }

    protected abstract void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 設定前端網頁所要顯示的錯誤訊息
     * 
     * @param msg
     *            所顯示的訊息內容
     */
    protected void setErrorMsg(HttpServletRequest request, String msg) {
	request.setAttribute(BaseActionSupport.ERROR_MESSAGE, msg);
    }

    /**
     * 設定前端網頁所要顯示的警告訊息
     * 
     * @param msg
     *            所顯示的訊息內容
     */
    protected void setWarnMsg(HttpServletRequest request, String msg) {
	request.setAttribute(BaseActionSupport.WARN_MESSAGE, msg);
    }

    /**
     * 設定前端網頁所要顯示的訊息
     * 
     * @param msg
     *            所顯示的訊息內容
     */
    protected void setInfoMsg(HttpServletRequest request, String msg) {
	request.setAttribute(BaseActionSupport.INFO_MESSAGE, msg);
    }

    /**
     * 取得標準訊息文字，訊息內容定義在messages.properties。請改用MsgString.XXXX_XXX。
     * @param key 
     * @return
     */
    public static String getMsg(String key){
	return MessageUtil.getString(key);
    }
    
    protected Properties getProperties(){
    	Properties properties = new Properties();
        String configFile = "/npa.properties";
        
        try {
            properties.load(new FileInputStream(this.getClass().getResource("/").getPath() + configFile));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            log.error(ExceptionUtil.toString(ex));
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ExceptionUtil.toString(ex));
        }
        
        return properties;
    }
}
