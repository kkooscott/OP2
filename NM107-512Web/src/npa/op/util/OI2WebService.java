package npa.op.util;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import npa.oi.ws.client.*;
import npa.oi.ws.client.OI2Webservice3Stub.GetInfoWithSplitAddrByIdnoResponse;
import npa.oi.ws.client.OI2Webservice3Stub.InfoPara;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HttpTransportProperties;

public class OI2WebService {

    static ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
    public static String OIWebServiceEPR = resourceBundle.getString("OIWebServiceEPR");
    public static String SOA_User = resourceBundle.getString("OI_SOA_User");
    public static String SOA_Pas = resourceBundle.getString("OI_SOA_Pwd");
    
    public static String getOiWebService(String idno, String SystemName, String SystemPwd, String UserId, String UnitCd, String userIp, String userName)
            throws ServletException, IOException {
        String strReturn = "";
        try {
            Integer timeOutInMilliSeconds = 10000; //Timeout時間
            try {
            	HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
                auth.setUsername(SOA_User);
                auth.setPassword(SOA_Pas);
                auth.setPreemptiveAuthentication(true);

//                OI2WebserviceStub stub = new OI2WebserviceStub(OIWebServiceEPR);
                OI2Webservice3Stub stub = new OI2Webservice3Stub(OIWebServiceEPR);

                //**2011.09.09 Modified by KevinKuo 取車籍WebService 改從SOA 需增加的帳密驗證
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CACHED_HTTP_CLIENT, "true");
//                OI2WebserviceStub.GetInfoWithSplitAddrByIdno req = new OI2WebserviceStub.GetInfoWithSplitAddrByIdno();
                OI2Webservice3Stub.GetInfoWithSplitAddrByIdno req = new OI2Webservice3Stub.GetInfoWithSplitAddrByIdno();

                //2010.10.11 Modified by KevinKuo 設定WebService Client的Timeout時間
                Options axis2options = stub._getServiceClient().getOptions();
                axis2options.setTimeOutInMilliSeconds(timeOutInMilliSeconds);
//                axis2options.setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_PROTOCOL_VERSION,
//                org.apache.axis2.transport.http.HTTPConstants.HEADER_PROTOCOL_11);
//                axis2options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.TRUE);
//                axis2options.setProperty(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING, "UTF-8");
                stub._getServiceClient().setOptions(axis2options);

                InfoPara para = new InfoPara();
                para.setId("");
                para.setIdno(idno);

                req.setInput(para);
                req.setSystemID(SystemName);
                req.setSystemPWD(SystemPwd);
                req.setSSOuserID(UserId);
                req.setSSOunitID(UnitCd);
                req.setEncoding("unicode");
                req.setUserIp(userIp);
                req.setSSOuserName(userName);

                GetInfoWithSplitAddrByIdnoResponse res = stub.getInfoWithSplitAddrByIdno(req);
                //回傳XML String
                strReturn = res.get_return();

//                        System.out.println("\nOiWSProcess strReturn:"+ strReturn);
//                        String name = strReturn.substring(strReturn.indexOf("<A1_NAME>")+9,strReturn.indexOf("</A1_NAME>"));
//                        System.out.println("\nOiWSProcess name:"+name+" hex:"+String.format("%040x", new BigInteger(1, name.getBytes("UTF-8"))));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
        return strReturn;
    }
    
    public static String getClientIP(javax.servlet.http.HttpServletRequest request){
        String clientIP = "";

        if (request != null){
            clientIP = request.getHeader("Proxy-ip");
            if (clientIP == null){
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
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
       }
       return getString(ip);
   }

    public static String getString(Object s) {
        if (s == null) return "";
        else return s.toString();
    }
}
