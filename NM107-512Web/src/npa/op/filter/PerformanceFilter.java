package npa.op.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class PerformanceFilter
 */
public class PerformanceFilter implements Filter {
    protected static Logger log = Logger.getLogger(PerformanceFilter.class);
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	if (isEnabled) {
	    HttpServletRequest r = (HttpServletRequest) request;
	    String requestURI = r.getRequestURI();
	    long begin = System.currentTimeMillis();
	    chain.doFilter(request, response);
	    //filterConfig.getServletContext().log("Request process in " + (System.currentTimeMillis() - begin) + " milliseconds");
	    log.info("PerformanceFilter -- "+requestURI+" 處理了 " + (System.currentTimeMillis() - begin) + " milliseconds");
	}else {
	    chain.doFilter(request, response);
	}
	
    }

}
