package npa.op.util;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.MessageDigest;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import npa.op.util.ExceptionUtil;
import npa.op.vo.User;

public class NPAUtil {
	protected static Logger log = Logger.getLogger(NPAUtil.class);

	/**
	 * 使用SHA256做加密，會比MD5更加安全。警署弱點掃描程式要求。
	 * 
	 * @param password
	 * @return
	 */
	public static String digestPasswordSHA256(String password) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());

			byte[] byteData = md.digest();
			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			digest = sb.toString();
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		return digest;
	}

	/**
	 * 取得本機端的IP
	 * 
	 * @return ip
	 */
	public static String getLocalIp() {
		String localIp = "";
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			localIp = localhost.getHostAddress();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(ExceptionUtil.toString(e));
		}
		return localIp;
	}

	/**
	 * 加入註解
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String[] arg) throws Exception {
		log.debug(NPAUtil.digestPasswordSHA256("syscom"));// a07297066ca3cc65e324e6bd8430225b52a5c594670fece21aca81a6837a076b

	}

	public static User getUser(HttpServletRequest req) {
		HttpSession session = req.getSession();
		return (User) session.getAttribute("user");
	}

	public static String getUserId(HttpServletRequest req) {
		User user = getUser(req);
		String rtn = "";
		if (user != null) {
			rtn = user.getUserId();
		}
		return rtn;
	}
	
	/**
	 * 因為使用java的URLEncoder.encode會將" "空白轉成"+"加號，而網頁端javascript會解出+來
	 * 所以先將空白轉成web可以正確轉出空白的字元%20
	 * 
	 * @param s
	 * @throws UnsupportedEncodingException
	 */
	public static String URLEncode(String s)
			throws UnsupportedEncodingException {
		String encodeString = s;// s.replaceAll(" ", "%20")
		encodeString = encodeString.replaceAll(" ", "%20");// 先將空白轉成web可以正確轉出空白的字元
		encodeString = URLEncoder.encode(encodeString, "utf-8");
		return encodeString;
	}

	/**
	 * 顯示Request的URI、URL、Parameter、Attribute、Header
	 * 
	 * @param req
	 */
	public static void showRequestInfo(HttpServletRequest req) {
		// HttpServletRequest req = (HttpServletRequest) request;
		log.debug("RequestURI-" + req.getRequestURI());
		log.debug("RequestURL-" + req.getRequestURL());
		Enumeration<String> paraNames = req.getParameterNames();
		String key;
		log.debug("---------Request Parameters");
		for (int i = 1; paraNames.hasMoreElements(); i++) {
			key = paraNames.nextElement();
			log.debug(i + ". " + key + "==" + req.getParameter(key));
		}
		log.debug("---------Request Attributes");
		Enumeration<String> attrNames = req.getAttributeNames();
		for (int i = 1; attrNames.hasMoreElements(); i++) {
			key = attrNames.nextElement();
			log.debug(i + ". " + key + "==" + req.getAttribute(key));
		}
		log.debug("---------Request Headers");
		Enumeration<String> headerNames = req.getHeaderNames();
		for (int i = 1; headerNames.hasMoreElements(); i++) {
			key = headerNames.nextElement();
			log.debug(i + ". " + key + "==" + req.getHeader(key));
		}
	}
	
	/**
	 * 從Session中取出登入的User物件
	 * 
	 * @param session
	 * @return
	 */
	public static User getUser(HttpSession session) {
		User user = null;
		if (session != null) {
			user = (User) session.getAttribute("user");
		}
		return user;
	}
}
