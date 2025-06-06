package npa.op.util;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import npa.e8.ws.client.*;
import npa.e8.ws.client.ImeiWebServiceStub;
import npa.e8.ws.client.ImeiWebServiceStub.DoCaseByBrandColorResponse;
import npa.e8.ws.client.ImeiWebServiceStub.DoImeiOrMacCase;
import npa.e8.ws.client.ImeiWebServiceStub.DoImeiOrMacCaseResponse;
import npa.e8.ws.client.PhoneNoWebServiceStub.DoPhoneNoCaseResponse;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.net.util.Base64;

public class E8ImeiWebService {

    static ResourceBundle resourceBundle = ResourceBundle.getBundle("npa");
    public static String E8WebServiceEPR = resourceBundle.getString("E8WebServiceEPR");
    public static String SOA_User = resourceBundle.getString("E8_SOA_User");
    public static String SOA_Pas = resourceBundle.getString("E8_SOA_Pwd");

    public static String getE8WebServiceByImeiOrMac(String QryType, String QryValue, String SystemName, String SystemPas, String UserId, String UnitCd, String UserIP)
            throws ServletException, IOException {
        String strReturn = "";
        try {
            Integer timeOutInMilliSeconds = 10000; //Timeout時間
            try {
                HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
                auth.setUsername(SOA_User);
                auth.setPassword(SOA_Pas);
                auth.setPreemptiveAuthentication(true);

                ImeiWebServiceStub stub = new ImeiWebServiceStub(E8WebServiceEPR);

                //**2011.09.09 Modified by KevinKuo 取車籍WebService 改從SOA 需增加的帳密驗證
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CACHED_HTTP_CLIENT, "true");
                ImeiWebServiceStub.DoImeiOrMacCase req = new ImeiWebServiceStub.DoImeiOrMacCase();

                //2010.10.11 Modified by KevinKuo 設定WebService Client的Timeout時間
                Options axis2options = stub._getServiceClient().getOptions();
                axis2options.setTimeOutInMilliSeconds(timeOutInMilliSeconds);
//                axis2options.setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_PROTOCOL_VERSION,
//                org.apache.axis2.transport.http.HTTPConstants.HEADER_PROTOCOL_11);
//                axis2options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.TRUE);
//                axis2options.setProperty(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING, "UTF-8");
                stub._getServiceClient().setOptions(axis2options);

                req.setQryType(QryType);
                req.setQryValue(QryValue);

                req.setSystemNm(SystemName);
                byte[] b = Base64.encodeBase64(SystemPas.getBytes(), true);
                String str = new String(b);
                byte[] b1 = Base64.decodeBase64(str);
                req.setSystemPwd(new String(b1));
                req.setUnitCd(UnitCd);
                req.setUserIP(UserIP);
                req.setUserId(UserId);

                DoImeiOrMacCaseResponse res = stub.doImeiOrMacCase(req);
                //回傳XML String
                strReturn = res.get_return();

//                System.out.println("\nOiWSProcess strReturn:"+ strReturn);
//                String name = strReturn.substring(strReturn.indexOf("<A1_NAME>")+9,strReturn.indexOf("</A1_NAME>"));
//                System.out.println("\nOiWSProcess name:"+name+" hex:"+String.format("%040x", new BigInteger(1, name.getBytes("UTF-8"))));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
        return strReturn;
    }

    public static String getE8WebServiceByBrandColor(String kindCD, String brandCD, String colorCD, String ocDt, String SystemName, String SystemPwd, String UserId, String UnitCd, String UserIP)
            throws ServletException, IOException {
        String strReturn = "";
        try {
            Integer timeOutInMilliSeconds = 10000; //Timeout時間
            try {
                HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
                auth.setUsername(SOA_User);
                auth.setPassword(SOA_Pas);
                auth.setPreemptiveAuthentication(true);

                ImeiWebServiceStub stub = new ImeiWebServiceStub(E8WebServiceEPR);

                //**2011.09.09 Modified by KevinKuo 取車籍WebService 改從SOA 需增加的帳密驗證
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
                stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CACHED_HTTP_CLIENT, "true");
                ImeiWebServiceStub.DoCaseByBrandColor req = new ImeiWebServiceStub.DoCaseByBrandColor();

                //2010.10.11 Modified by KevinKuo 設定WebService Client的Timeout時間
                Options axis2options = stub._getServiceClient().getOptions();
                axis2options.setTimeOutInMilliSeconds(timeOutInMilliSeconds);
//                axis2options.setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_PROTOCOL_VERSION,
//                org.apache.axis2.transport.http.HTTPConstants.HEADER_PROTOCOL_11);
//                axis2options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.TRUE);
//                axis2options.setProperty(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING, "UTF-8");
                stub._getServiceClient().setOptions(axis2options);

                req.setBrandCd(brandCD);
                req.setColorCd(colorCD);
                req.setKindCd(kindCD);
                req.setOcDt(ocDt);

                req.setSystemNm(SystemName);
                req.setSystemPwd(SystemPwd);
                req.setUnitCd(UnitCd);
                req.setUserIP(UserIP);
                req.setUserId(UserId);

                DoCaseByBrandColorResponse res = stub.doCaseByBrandColor(req);
                //回傳XML String
                strReturn = res.get_return();

//                System.out.println("\nOiWSProcess strReturn:"+ strReturn);
//                String name = strReturn.substring(strReturn.indexOf("<A1_NAME>")+9,strReturn.indexOf("</A1_NAME>"));
//                System.out.println("\nOiWSProcess name:"+name+" hex:"+String.format("%040x", new BigInteger(1, name.getBytes("UTF-8"))));
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
