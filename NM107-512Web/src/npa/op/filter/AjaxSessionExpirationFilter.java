package npa.op.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import npa.op.util.NPAUtil;
import npa.op.vo.User;

import org.apache.log4j.Logger;

/**
 * 本Filter專門用來檢查client端的ajax是否有登入系統
 */

public class AjaxSessionExpirationFilter implements Filter {
    protected static Logger log = Logger.getLogger(AjaxSessionExpirationFilter.class);
    protected FilterConfig filterConfig = null;
    protected boolean isEnabled = true;
    private int customSessionExpiredErrorCode = 901;

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

	customSessionExpiredErrorCode = Integer
		.parseInt(filterConfig.getInitParameter("customSessionExpiredErrorCode"));
    }

    @Override
    public void destroy() {
	this.filterConfig = null;
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
     
	if (isEnabled) {
	    HttpSession session = ((HttpServletRequest) request).getSession();
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
		String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
                //String ajaxHeader = "XMLHttpRequest";
		String requestURI = ((HttpServletRequest) request).getRequestURI();
                String servletPath = ((HttpServletRequest)request).getServletPath();
//                System.out.println("requestURI:"+requestURI);
                boolean isSecureLogin = false;
                if(((HttpServletRequest) request).getHeader("referer") != null && 
                        ((HttpServletRequest) request).getHeader("referer").contains("SecureLogin.jsp")){
                    isSecureLogin = true;
                }
		// 需排除首頁會做的ajax
		// SysParamServlet,ChangeUnitServlet,UserApplyServlet是不需要登入就可以使用的ajax
		if ("XMLHttpRequest".equals(ajaxHeader) && !requestURI.contains("SysParamServlet") && !isSecureLogin
			&& !requestURI.contains("ChangeUnitServlet") && !requestURI.contains("UserApplyServlet") && !requestURI.contains("ForgetPwdServlet")) {
//                if(!servletPath.equals("/index.jsp")){
		    NPAUtil.showRequestInfo((HttpServletRequest)request);
		    log.info("偵測到Ajax呼叫,但沒有登入系統因此送出error code{" + customSessionExpiredErrorCode + "}");
		    HttpServletResponse resp = (HttpServletResponse) response;
		    resp.sendError(this.customSessionExpiredErrorCode);
		} else {
		    chain.doFilter(request, response);
		}
	    } else {
		chain.doFilter(request, response);
	    }

	} else {
	    chain.doFilter(request, response);
	}
    }

}
