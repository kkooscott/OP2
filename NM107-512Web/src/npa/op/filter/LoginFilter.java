package npa.op.filter;

import java.io.*;
import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.*;
import javax.servlet.http.*;
import npa.op.util.ExceptionUtil;
import npa.op.vo.User;
import org.apache.commons.codec.binary.Hex;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class LoginFilter
 */

public class LoginFilter implements Filter {
    protected static Logger log = Logger.getLogger(LoginFilter.class);
    protected FilterConfig filterConfig = null;
    protected boolean isEnabled = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	this.filterConfig = filterConfig;
	String value = filterConfig.getInitParameter("isEnabled");
	if (value == null) {
	    this.isEnabled = true;
	} else if (value.equalsIgnoreCase("true")) {
	    this.isEnabled = true;
	} else if (value.equalsIgnoreCase("yes")) {
	    this.isEnabled = true;
	} else if (value.equalsIgnoreCase("y")) {
	    this.isEnabled = true;
	} else {
	    this.isEnabled = false;
	}
    }

    @Override
    public void destroy() {
	this.filterConfig = null;
    }

    @SuppressWarnings({ "unused" })
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	HttpServletRequest req = (HttpServletRequest) request;
	HttpServletResponse res = (HttpServletResponse) response;
	HttpSession session = req.getSession();
        res.addHeader("X-Frame-Options", "SAMEORIGIN");  //20230327
	String requestURI = req.getRequestURI();
	if(session.isNew()){
		// 填入預設的Session資料
		// 取得 IP ============================
		String ip = req.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		    ip = req.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		    ip = req.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		    ip = req.getHeader("HTTP_CLIENT_IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		    ip = req.getHeader("HTTP_X_FORWARDED_FOR");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		    ip = req.getRemoteAddr();
	}

	
	if (isEnabled) {

	    String pageName = null;
	    String dirName = null;
	    boolean isJSP = false;
	    boolean isIndex = false;
	    boolean isException = false;
	    if (requestURI.endsWith(".jsp")) {
		isJSP = true;
		if (requestURI.indexOf("index.jsp") != -1
			|| requestURI.indexOf("SecureLogin.jsp") != -1				
			|| requestURI.indexOf("SessionTimeout.jsp") != -1
			|| requestURI.indexOf("DocumentModeNotSupport.jsp") != -1 
			|| requestURI.indexOf("BrowserNotSupport.jsp") != -1
			|| requestURI.indexOf("CaseRealtimeImport.jsp") != -1 
			|| requestURI.indexOf("Detect.jsp") != -1 || requestURI.indexOf("EBoardServlet") != -1
			|| requestURI.indexOf("MainFrameset.jsp") != -1	|| requestURI.indexOf("NoSSOLogin.jsp") != -1)  //

		    isIndex = true;
		if (requestURI.indexOf("exception.jsp") != -1)
		    isException = true;
		if (!isIndex && !isException) {// 非index and 非exception頁面才檢查
		    try {
			// 從URI取得request的頁面名稱
			int lastIndex = requestURI.lastIndexOf("/");
			if (lastIndex != requestURI.length() - 1) {
			    pageName = requestURI.substring(lastIndex + 1);
			    int last2Index = requestURI.lastIndexOf("/", lastIndex - 1);
			    if (last2Index != -1) {
				dirName = requestURI.substring(last2Index + 1, lastIndex);
			    }
			}
			User user = (User) session.getAttribute("user");
			// 是否登入
			if (user != null) {
			    chain.doFilter(request, response);// 登入過就繼續
			} else {
//			    String viewerURL = "SessionTimeout.jsp";
//			    res.sendRedirect(viewerURL);
                            SecureRandom secRandom = new SecureRandom();

                            byte[] result = new byte[32];
                            secRandom.nextBytes(result);
                            String viewerURL = "SessionTimeout.jsp?r="+Hex.encodeHexString(result);
                            res.sendRedirect(viewerURL);
			    return;
			}
		    } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		    }
		} else {// 不需要經過login的頁面
		    chain.doFilter(request, response);
		}
	    } else {// 不需要經過login的頁面
		chain.doFilter(request, response);
	    }

	} else {
	    chain.doFilter(request, response);
	}

    }
}
