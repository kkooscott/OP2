package npa.op.util;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import npa.e8.ws.client.PhoneNoWebServiceStub;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.net.util.Base64;

/**
 *
 * @author Administrator
 */
public class E8PhoneWebService {
    static ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
    public static String E8WebServiceEPR = resourceBundle.getString("E8WebServiceEPRPhone");
    public static String SOA_User = resourceBundle.getString("E8_SOA_User");
    public static String SOA_Pas = resourceBundle.getString("E8_SOA_Pwd");
    
    public static String getE8WebServiceByPhone(String QryType, String QryValue, String SystemName, String SystemPas, String UserId, String UnitCd, String UserIP)
            throws ServletException, IOException {

        String strReturn = "";
        try {
            Integer timeOutInMilliSeconds = 10000; //Timeout時間
            try {
                System.out.println("getE8WebServiceByPhone開始");
                HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
                auth.setUsername(SOA_User);
                auth.setPassword(SOA_Pas);
                auth.setPreemptiveAuthentication(true);

                PhoneNoWebServiceStub stub = new PhoneNoWebServiceStub(E8WebServiceEPR);
                System.out.println(QryType + 1 + "連線設定");
                
                //**2011.09.09 Modified by KevinKuo 取車籍WebService 改從SOA 需增加的帳密驗證
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CACHED_HTTP_CLIENT, "true");
                PhoneNoWebServiceStub.DoPhoneNoCase req = new PhoneNoWebServiceStub.DoPhoneNoCase();
                System.out.println(QryType + 2 + "帳密驗證");
                
                //2010.10.11 Modified by KevinKuo 設定WebService Client的Timeout時間
                Options axis2options = stub._getServiceClient().getOptions();
                axis2options.setTimeOutInMilliSeconds(timeOutInMilliSeconds);
                System.out.println(QryType + 3 + "設定Timeout");
                
                stub._getServiceClient().setOptions(axis2options);

                req.setPhoneNo(QryValue);

                req.setSystemNm(SystemName);
                byte[] b = Base64.encodeBase64(SystemPas.getBytes(), true);
                String str = new String(b);
                byte[] b1 = Base64.decodeBase64(str);
                req.setSystemPwd(new String(b1));
                req.setUnitCd(UnitCd);
                req.setUserIP(UserIP);
                req.setUserId(UserId);
                System.out.println(QryType + 4 + "request");
                PhoneNoWebServiceStub.DoPhoneNoCaseResponse res = stub.doPhoneNoCase(req);
                System.out.println(QryType + 5 + "response");
                //回傳XML String
                strReturn = res.get_return();
                System.out.println(QryType + 5 + strReturn);
                System.out.println("getE8WebServiceByPhone結束");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }

        return strReturn;
    }
    
    public static String getClientIP(javax.servlet.http.HttpServletRequest request) {
        String clientIP = "";

        if (request != null) {
            clientIP = request.getHeader("Proxy-ip");
            if (clientIP == null) {
                clientIP = request.getRemoteAddr();
            }
        }
        return getString(clientIP);
    }

    /*
     * 取得使用者IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return getString(ip);
    }

    public static String getString(Object s) {
        if (s == null) {
            return "";
        } else {
            return s.toString();
        }
    }
}
