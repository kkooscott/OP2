package npa.op.control;


import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import npa.op.base.AjaxBaseServlet;
//import npa.op.util.AesCrypt;
import npa.op.util.DateUtil;
import static npa.op.util.DateUtil.*;
import npa.op.util.E8ImeiWebService;
import npa.op.util.E8PhoneWebService;
import npa.op.util.ExportUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.util.StringUtil;
import npa.op.util.OI2WebService;
import static npa.op.util.StringUtil.replaceWhiteChar;
import npa.op.vo.User;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * call其他系統資料
 */
@WebServlet("/WebServiceServlet")
public class WebServiceServlet extends AjaxBaseServlet {
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(WebServiceServlet.class);

	@Override
	protected void executeAjax(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, User user, JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {
		
		//取得登入人員資訊
		argJsonObj.put("userVO", user);
		ResourceBundle resource = ResourceBundle.getBundle("npa");
		//AesCrypt crypt = new AesCrypt(resource.getString("crypt"));
		JSONObject jObject;
		JSONArray resultDataArray = new JSONArray();
		JSONArray resultDataArray1 = new JSONArray();
		ExportUtil exportUtil = new ExportUtil();
		CachedRowSet crs1 = null;
		CachedRowSet crs2 = null;
		CachedRowSet crs3 = null;
		NPALOG2Util NPALOG2 = new NPALOG2Util();
		HttpSession Opsession = null;
		JSONArray resultjArray  = new JSONArray();
                String[] delId = null;
		
		boolean result;
		switch (argJsonObj.getString(AJAX_REQ_ACTION_KEY)) {
                    //取得人口系統居住地
                    case "getOI2PersonByIDN":
                        try{
                            int count = 0;
                            String strOiPersonData = "";
                            if( argJsonObj.getString("IDN_TYPE").equals("BASIC") ){ //BASIC
                                strOiPersonData = OI2WebService.getOiWebService(argJsonObj.getString("OP_PUPO_IDN"), "OP", "OP", user.getUserId(), user.getUnitCd(), user.getUserIp(), user.getUserName());
                            }else if ( argJsonObj.getString("IDN_TYPE").equals("CLAIM") ){ //CLAIM
                                strOiPersonData = OI2WebService.getOiWebService(argJsonObj.getString("OP_PUCP_IDN"), "OP", "OP", user.getUserId(), user.getUnitCd(), user.getUserIp(), user.getUserName());
                            }
//                            strOiPersonData = "<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//                                            "<ComCodeResponse>" +
//                                            "<CodeDatas>" +
//                                            "<row>" +
//                                            "<A1_ID> 3481426</A1_ID>" +
//                                            "<A1_IDNO>A123456789</A1_IDNO>" +
//                                            "<A1_NAME>謝條根</A1_NAME>" +
//                                            "<A1_BIRDT>19570510</A1_BIRDT>" +
//                                            "<A1_CITY>65000</A1_CITY>" +
//                                            "<A1_CITY_NM>新北市</A1_CITY_NM>" +
//                                            "<A1_TOWN>65000160</A1_TOWN>" +
//                                            "<A1_TOWN_NM>泰山區</A1_TOWN_NM>" +
//                                            "<A1_VILLAGE>65000160003</A1_VILLAGE>" +
//                                            "<A1_VILLAGE_NM>山腳里</A1_VILLAGE_NM>" +
//                                            "<A1_NEIGH>033</A1_NEIGH>" +
//                                            "<A1_ROAD>泰林路二段２９２巷３８號</A1_ROAD>" +
//                                            "<A1_RNAME>Ado'　Kaliting PacidalUyongʉ'e Yatauyungana</A1_RNAME>"+
//                                            "</row>" +
//                                            "<RTNCode>查有資料</RTNCode>" +
//                                            "</CodeDatas>" +
//                                            "</ComCodeResponse>]]>";
//                            System.out.print(strOiPersonData);
//                            strOiPersonData = strOiPersonData.replace("UTF-8","big5");
//                            strOiPersonData = strOiPersonData.replace("<![CDATA[","");
//                            strOiPersonData = strOiPersonData.replace("]]>","");
//                            Document oiDoc = loadXMLFromString(strOiPersonData);
//                            NodeList RTNCodeNodes = oiDoc.getElementsByTagName("RTNCode");
//                            JSONObject obj = new JSONObject();
//                            if (RTNCodeNodes != null && RTNCodeNodes.getLength() > 0) { //表示沒錯誤
//                                String RTNCode = "";
//                                RTNCode = RTNCodeNodes.item(0).getTextContent();
//                                if (RTNCode.equals("查無資料")) { //查無資料
//                                    obj = null;
//                                }else{
//                                    NodeList RowList = oiDoc.getElementsByTagName("row");
//                                    count = RowList.getLength();
//                                    int i = 0;
//                                    for (i = 0; i < count; i++) {
//                                        obj.put("A1_ID", oiDoc.getElementsByTagName("A1_ID").item(i).getTextContent());
//                                        obj.put("A1_IDNO", oiDoc.getElementsByTagName("A1_IDNO").item(i).getTextContent());
//                                        obj.put("A1_NAME", oiDoc.getElementsByTagName("A1_NAME").item(i).getTextContent());
//                                        obj.put("A1_BIRDT", convertBirthday(oiDoc.getElementsByTagName("A1_BIRDT").item(i).getTextContent()).replace("/", ""));
//                                        obj.put("A1_CITY", oiDoc.getElementsByTagName("A1_CITY").item(i).getTextContent());
//                                        obj.put("A1_TOWN", oiDoc.getElementsByTagName("A1_TOWN").item(i).getTextContent());
//                                        obj.put("A1_VILLAGE", oiDoc.getElementsByTagName("A1_VILLAGE").item(i).getTextContent());
//                                        //Integer tempValue = Integer.valueOf(strOiPersonData.substring(strOiPersonData.indexOf("<A1_NEIGH>") + 10, strOiPersonData.indexOf("</A1_NEIGH>")));
//                                        obj.put("A1_NEIGH", fullToHalf(oiDoc.getElementsByTagName("A1_NEIGH").item(i).getTextContent()));
//                                        obj.put("A1_ROAD", fullToHalf(oiDoc.getElementsByTagName("A1_ROAD").item(i).getTextContent()));
//                                        obj.put("A1_RNAME", oiDoc.getElementsByTagName("A1_RNAME").item(i).getTextContent());
//                                    }
//                                }
//                            }
                            JSONObject obj = new JSONObject();
                            obj.put("A1_ID", strOiPersonData.substring(strOiPersonData.indexOf("<A1_ID>") + 7, strOiPersonData.indexOf("</A1_ID>")));
                            obj.put("A1_IDNO", strOiPersonData.substring(strOiPersonData.indexOf("<A1_IDNO>") + 9, strOiPersonData.indexOf("</A1_IDNO>")));
                            obj.put("A1_NAME", strOiPersonData.substring(strOiPersonData.indexOf("<A1_NAME>") + 9, strOiPersonData.indexOf("</A1_NAME>")));
                            obj.put("A1_BIRDT", convertBirthday(strOiPersonData.substring(strOiPersonData.indexOf("<A1_BIRDT>") + 10, strOiPersonData.indexOf("</A1_BIRDT>"))).replace("/", ""));
                            obj.put("A1_CITY", strOiPersonData.substring(strOiPersonData.indexOf("<A1_CITY>") + 9, strOiPersonData.indexOf("</A1_CITY>")));
                            obj.put("A1_TOWN", strOiPersonData.substring(strOiPersonData.indexOf("<A1_TOWN>") + 9, strOiPersonData.indexOf("</A1_TOWN>")));
                            obj.put("A1_VILLAGE", strOiPersonData.substring(strOiPersonData.indexOf("<A1_VILLAGE>") + 12, strOiPersonData.indexOf("</A1_VILLAGE>")));
                            //Integer tempValue = Integer.valueOf(strOiPersonData.substring(strOiPersonData.indexOf("<A1_NEIGH>") + 10, strOiPersonData.indexOf("</A1_NEIGH>")));
                            obj.put("A1_NEIGH", fullToHalf(strOiPersonData.substring(strOiPersonData.indexOf("<A1_NEIGH>") + 10, strOiPersonData.indexOf("</A1_NEIGH>"))));
                            obj.put("A1_ROAD", fullToHalf(strOiPersonData.substring(strOiPersonData.indexOf("<A1_ROAD>") + 9, strOiPersonData.indexOf("</A1_ROAD>"))));
                            if (strOiPersonData.contains("<A1_RNAME>")) {
                                obj.put("A1_RNAME", strOiPersonData.substring(strOiPersonData.indexOf("<A1_RNAME>") + 10, strOiPersonData.indexOf("</A1_RNAME>")));
                            }else{
                                obj.put("A1_RNAME", "");
                            }
                            this.setFormData(returnJasonObj, obj);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    break;
                    //取得E化IMEI碼、MAC碼(IMEI碼、MAC碼)
                    case "getE82ImeiByCase":
                        try{
                            int count = 0;
                            JSONObject obj = new JSONObject();
                            String strE8ImeiData = "";
                            if( argJsonObj.getString("QRY_TYPE").equals("IMEI") ){
                                strE8ImeiData = E8ImeiWebService.getE8WebServiceByImeiOrMac(argJsonObj.getString("QRY_TYPE"), argJsonObj.getString("IMEI"), "OP2", "", user.getUserId(), user.getUnitCd(), user.getUserIp());
                            }else if( argJsonObj.getString("QRY_TYPE").equals("MAC") ){
                                strE8ImeiData = E8ImeiWebService.getE8WebServiceByImeiOrMac(argJsonObj.getString("QRY_TYPE"), argJsonObj.getString("MAC"), "OP2", "", user.getUserId(), user.getUnitCd(), user.getUserIp());
                            }else if( argJsonObj.getString("QRY_TYPE").equals("PHONE") ){
                                strE8ImeiData = E8PhoneWebService.getE8WebServiceByPhone(argJsonObj.getString("QRY_TYPE"), argJsonObj.getString("PHONE"), "OP2", "", user.getUserId(), user.getUnitCd(), user.getUserIp());
                            }
                            log.debug("strE8ImeiData:" + strE8ImeiData);
//                            strE8ImeiData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
//                                            "<Result>" +
//                                            "<Row>" +
//                                            "<Imei>123456789012345</Imei>" +
//                                            "<Mac>00:AA:BB:CC:DD:99</Mac>" +
//                                            "<E8_E_CASE_NO>P09603223300008</E8_E_CASE_NO>" +
//                                            "<E8_AC_UNIT_CD>DPZ1</E8_AC_UNIT_CD>" +
//                                            "<E8_AC_UNIT_NM>臺北縣政府警察局板橋分局板橋派出所</E8_AC_UNIT_NM>" +
//                                            "<E8_RP_DT>20070305</E8_RP_DT>" +
//                                            "<E8_RP_TM>0956</E8_RP_TM>" +
//                                            "<E8_CC_TYPE_CD>B001</E8_CC_TYPE_CD>" +
//                                            "<E8_CC_TYPE_NM>竊盜</E8_CC_TYPE_NM>" +
//                                            "<E8_OC_CITY_CD>63000</E8_OC_CITY_CD>" +
//                                            "<E8_OC_CITY_NM>臺北市</E8_OC_CITY_NM>" +
//                                            "</Row>" +
//                                            "<Row>" +
//                                            "<Imei>123456789543210</Imei>" +
//                                            "<Mac>CC:00:AA:CC:DD:BB</Mac>" +
//                                            "<E8_E_CASE_NO>E08203223300008</E8_E_CASE_NO>" +
//                                            "<E8_AC_UNIT_CD>DPZ12</E8_AC_UNIT_CD>" +
//                                            "<E8_AC_UNIT_NM>新北市政府警察局板橋分局板橋派出所</E8_AC_UNIT_NM>" +
//                                            "<E8_RP_DT>20180305</E8_RP_DT>" +
//                                            "<E8_RP_TM>0956</E8_RP_TM>" +
//                                            "<E8_CC_TYPE_CD>B001</E8_CC_TYPE_CD>" +
//                                            "<E8_CC_TYPE_NM>竊盜嫌疑</E8_CC_TYPE_NM>" +
//                                            "<E8_OC_CITY_CD>63001</E8_OC_CITY_CD>" +
//                                            "<E8_OC_CITY_NM>新北市</E8_OC_CITY_NM>" +
//                                            "</Row>" +
//                                            "<Message>查有資料</Message>" +
//                                            "</Result>";
                            strE8ImeiData = strE8ImeiData.replace("utf-8","big5");
                            Document e8Doc = loadXMLFromString(strE8ImeiData);
                            NodeList RTNCodeNodes = e8Doc.getElementsByTagName("Message");
                            if (RTNCodeNodes != null && RTNCodeNodes.getLength() > 0) { //表示沒錯誤
                                String RTNCode = "";
                                RTNCode = RTNCodeNodes.item(0).getTextContent();
                                if (RTNCode.equals("查無資料")) { //查無資料
                                }else{
                                    NodeList RowList = e8Doc.getElementsByTagName("Row");
                                    count = RowList.getLength();
                                    int i = 0;
                                    for (i = 0; i < count; i++) {
                                        obj = new JSONObject();
                                        obj.put("IMEI",e8Doc.getElementsByTagName("Imei").item(i).getTextContent());
                                        obj.put("IMEI2",e8Doc.getElementsByTagName("Imei2").item(i).getTextContent());
                                        if( argJsonObj.getString("QRY_TYPE").equals("IMEI") || argJsonObj.getString("QRY_TYPE").equals("MAC") ){
                                            obj.put("MAC",e8Doc.getElementsByTagName("Mac").item(i).getTextContent());
                                        }else{
                                            obj.put("MAC",e8Doc.getElementsByTagName("E8_MAC").item(i).getTextContent());
                                        }
                                        obj.put("PHONE",e8Doc.getElementsByTagName("PhoneNo").item(i).getTextContent());
                                        obj.put("PHONE2",e8Doc.getElementsByTagName("PhoneNo2").item(i).getTextContent());
                                        obj.put("E8_E_CASE_NO",e8Doc.getElementsByTagName("E8_E_CASE_NO").item(i).getTextContent());
                                        obj.put("E8_AC_UNIT_CD",e8Doc.getElementsByTagName("E8_AC_UNIT_CD").item(i).getTextContent());
                                        obj.put("E8_AC_UNIT_NM",e8Doc.getElementsByTagName("E8_AC_UNIT_NM").item(i).getTextContent());
                                        String date = e8Doc.getElementsByTagName("E8_RP_DT").item(i).getTextContent();
                                        String time = e8Doc.getElementsByTagName("E8_RP_TM").item(i).getTextContent();
                                        obj.put("E8_RP_DT", DateUtil.to7TwDateTime(date, time));
                                        //obj.put("E8_RP_TM", e8Doc.getElementsByTagName("E8_RP_TM").item(i).getTextContent());
                                        //obj.put("E8_CC_TYPE_CD",e8Doc.getElementsByTagName("E8_CC_TYPE_CD").item(i).getTextContent());
                                        obj.put("E8_CC_TYPE_NM",e8Doc.getElementsByTagName("E8_CC_TYPE_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_CC_TYPE_NM").item(i).getTextContent());
                                        //obj.put("E8_OC_CITY_CD",e8Doc.getElementsByTagName("E8_OC_CITY_CD").item(i).getTextContent());
                                        obj.put("E8_OC_CITY_NM",e8Doc.getElementsByTagName("E8_OC_CITY_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_OC_CITY_NM").item(i).getTextContent());
                                        resultDataArray.put(obj);
                                    }
                                }
                            }
                            NPALOG2 = new NPALOG2Util();
                            
                            //NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFront06A02Q(argJsonObj, argJsonObj.getString("QRY_TYPE")), "", "", "", resultDataArray, this.GetLogFor0602Query(), "OP06A02Q_01.jsp", user, user.getUserName(), user.getUnitName(), "");
                            NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFront06A02Q(argJsonObj, argJsonObj.getString("QRY_TYPE")), "", "", "", resultDataArray, this.GetLogFor0602Query(), "OP06A02Q_01.jsp", user, user.getUserName(), user.getUnitName(), 
                                    argJsonObj.getString("OPR_PURP"),
                                    argJsonObj.getString("OPR_KIND"), "", "", "", "");
                            this.setJqGridData(returnJasonObj, resultDataArray);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    break;
                    //取得E化IMEI碼、MAC碼(物品種類、廠牌、顏色、發生日期)
                    case "getE82ImeiByBrandColor":
                        try{
                            int count = 0;
                            JSONObject obj = new JSONObject();
                            String strE8ImeiData = "";
                            strE8ImeiData = E8ImeiWebService.getE8WebServiceByBrandColor(argJsonObj.getString("KIND_CD"), argJsonObj.getString("BRAND_CD"), argJsonObj.getString("COLOR_CD"),argJsonObj.getString("OC_DT"), "OP2", "", user.getUserId(), user.getUnitCd(), user.getUserIp());
//                            strE8ImeiData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
//                                            "<Result>" +
//                                            "<Row>" +
//                                            "<Imei>123456789012345</Imei>" +
//                                            "<Mac>00:AA:BB:CC:DD:99</Mac>" +
//                                            "<E8_E_CASE_NO>P09603223300008</E8_E_CASE_NO>" +
//                                            "<E8_AC_UNIT_CD>DPZ1</E8_AC_UNIT_CD>" +
//                                            "<E8_AC_UNIT_NM>臺北縣政府警察局板橋分局板橋派出所</E8_AC_UNIT_NM>" +
//                                            "<E8_RP_DT>20070305</E8_RP_DT>" +
//                                            "<E8_RP_TM>0956</E8_RP_TM>" +
//                                            "<E8_CC_TYPE_CD>B001</E8_CC_TYPE_CD>" +
//                                            "<E8_CC_TYPE_NM>竊盜</E8_CC_TYPE_NM>" +
//                                            "<E8_OC_CITY_CD>63000</E8_OC_CITY_CD>" +
//                                            "<E8_OC_CITY_NM>臺北市</E8_OC_CITY_NM>" +
//                                            "</Row>" +
//                                            "<Row>" +
//                                            "<Imei>123456789543210</Imei>" +
//                                            "<Mac>CC:00:AA:CC:DD:BB</Mac>" +
//                                            "<E8_E_CASE_NO>E08203223300008</E8_E_CASE_NO>" +
//                                            "<E8_AC_UNIT_CD>DPZ12</E8_AC_UNIT_CD>" +
//                                            "<E8_AC_UNIT_NM>新北市政府警察局板橋分局板橋派出所</E8_AC_UNIT_NM>" +
//                                            "<E8_RP_DT>20180305</E8_RP_DT>" +
//                                            "<E8_RP_TM>0956</E8_RP_TM>" +
//                                            "<E8_CC_TYPE_CD>B001</E8_CC_TYPE_CD>" +
//                                            "<E8_CC_TYPE_NM>竊盜嫌疑</E8_CC_TYPE_NM>" +
//                                            "<E8_OC_CITY_CD>63001</E8_OC_CITY_CD>" +
//                                            "<E8_OC_CITY_NM>新北市</E8_OC_CITY_NM>" +
//                                            "</Row>" +
//                                            "<Message>查有資料</Message>" +
//                                            "</Result>";
                            strE8ImeiData = strE8ImeiData.replace("utf-8","big5");
                            Document e8Doc = loadXMLFromString(strE8ImeiData);
                            NodeList RTNCodeNodes = e8Doc.getElementsByTagName("Message");
                            if (RTNCodeNodes != null && RTNCodeNodes.getLength() > 0) { //表示沒錯誤
                                String RTNCode = "";
                                RTNCode = RTNCodeNodes.item(0).getTextContent();
                                if (RTNCode.equals("查無資料")) { //查無資料
                                }else{
                                    NodeList RowList = e8Doc.getElementsByTagName("Row");
                                    count = RowList.getLength();
                                    int i = 0;
                                    for (i = 0; i < count; i++) {
                                        obj = new JSONObject();
                                        obj.put("IMEI",e8Doc.getElementsByTagName("Imei").item(i).getTextContent());
                                        obj.put("IMEI2",e8Doc.getElementsByTagName("Imei2").item(i).getTextContent());
                                        if( argJsonObj.getString("QRY_TYPE").equals("IMEI") || argJsonObj.getString("QRY_TYPE").equals("MAC") ){
                                            obj.put("MAC",e8Doc.getElementsByTagName("Mac").item(i).getTextContent());
                                        }else{
                                            obj.put("MAC",e8Doc.getElementsByTagName("E8_MAC").item(i).getTextContent());
                                        }
                                        obj.put("PHONE",e8Doc.getElementsByTagName("PhoneNo").item(i).getTextContent());
                                        obj.put("PHONE2",e8Doc.getElementsByTagName("PhoneNo2").item(i).getTextContent());
                                        obj.put("E8_E_CASE_NO",e8Doc.getElementsByTagName("E8_E_CASE_NO").item(i).getTextContent());
                                        obj.put("E8_AC_UNIT_CD",e8Doc.getElementsByTagName("E8_AC_UNIT_CD").item(i).getTextContent());
                                        obj.put("E8_AC_UNIT_NM",e8Doc.getElementsByTagName("E8_AC_UNIT_NM").item(i).getTextContent());
                                        String date = e8Doc.getElementsByTagName("E8_RP_DT").item(i).getTextContent();
                                        String time = e8Doc.getElementsByTagName("E8_RP_TM").item(i).getTextContent();
                                        obj.put("E8_RP_DT", DateUtil.to7TwDateTime(date, time));
                                        //obj.put("E8_RP_TM", e8Doc.getElementsByTagName("E8_RP_TM").item(i).getTextContent());
                                        //obj.put("E8_CC_TYPE_CD",e8Doc.getElementsByTagName("E8_CC_TYPE_CD").item(i).getTextContent());
                                        obj.put("E8_CC_TYPE_NM",e8Doc.getElementsByTagName("E8_CC_TYPE_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_CC_TYPE_NM").item(i).getTextContent());
                                        //obj.put("E8_OC_CITY_CD",e8Doc.getElementsByTagName("E8_OC_CITY_CD").item(i).getTextContent());
                                        obj.put("E8_OC_CITY_NM",e8Doc.getElementsByTagName("E8_OC_CITY_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_OC_CITY_NM").item(i).getTextContent());
                                        resultDataArray.put(obj);
                                    }
                                }
                            }
                            NPALOG2 = new NPALOG2Util();
                            
                            //NPALOG2.WriteQueryLog(LOGOPTYPE.Q, GetFront06A02Q(argJsonObj, argJsonObj.getString("QRY_TYPE")), "", "", "", resultDataArray, this.GetLogFor0602Query(), "OP06A02Q_01", user, user.getUserName(), user.getUnitName(), "");
                            NPALOG2.WriteQueryLog_New(LOGOPTYPE.Q, GetFront06A02Q(argJsonObj, argJsonObj.getString("QRY_TYPE")), "", "", "", resultDataArray, this.GetLogFor0602Query(), "OP06A02Q_01", user, user.getUserName(), user.getUnitName(), 
                                    argJsonObj.getString("OPR_PURP"),
                                    argJsonObj.getString("OPR_KIND"), "", "", "", "");
                            this.setJqGridData(returnJasonObj, resultDataArray);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    break;
		}
	}
        
        private static String fullToHalf(String str){
            for(char c:str.toCharArray()){
                str = str.replaceAll("　", " ");
                if((int)c >= 65281 && (int)c <= 65374){
                    str = str.replace(c, (char)(((int)c)-65248));
                }
            }
            return str;
        }
        
        public static Document loadXMLFromString(String xml) throws Exception
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new ByteArrayInputStream(xml.getBytes()));
        }
        
        //查詢條件欄位(跨轄查詢_認領人)
            private String GetFront06A02Q(JSONObject jsonObject, String ACTION_TYPE)
        {
                    StringBuilder returnString = new StringBuilder();
                    LinkedHashMap logCols =new LinkedHashMap <String, String>();
                    
                    if( ACTION_TYPE.equals("IMEI") ){
                        if (!jsonObject.getString("QRY_TYPE").equals("")){
                            returnString.append(String.format("&%s=%s", "查詢類別", "IMEI碼"));
                        }
                        if (!jsonObject.getString("IMEI").equals("")){
                                returnString.append(String.format("&%s=%s", "IMEI碼", jsonObject.getString("IMEI")));
                        }
                    }else if( ACTION_TYPE.equals("MAC") ){
                        if (!jsonObject.getString("QRY_TYPE").equals("")){
                            returnString.append(String.format("&%s=%s", "查詢類別", "MAC碼"));
                        }
                        if (!jsonObject.getString("MAC").equals("")){
                                returnString.append(String.format("&%s=%s", "MAC碼", jsonObject.getString("MAC")));
                        }
                    }else if( ACTION_TYPE.equals("PHONE") ){
                        if (!jsonObject.getString("QRY_TYPE").equals("")){
                            returnString.append(String.format("&%s=%s", "查詢類別", "電信門號"));
                        }
                        if (!jsonObject.getString("MAC").equals("")){
                                returnString.append(String.format("&%s=%s", "電信門號", jsonObject.getString("PHONE")));
                        }
                    }else if( ACTION_TYPE.equals("COLOR") ){
                        if (!jsonObject.getString("QRY_TYPE").equals("")){
                            returnString.append(String.format("&%s=%s", "查詢類別", "廠牌顏色"));
                        }
                        if (!jsonObject.getString("KIND_CD").equals("")){
                                returnString.append(String.format("&%s=%s", "物品種類", jsonObject.getString("KIND_NM")));
                        }
                        if (!jsonObject.getString("BRAND_CD").equals("")){
                                returnString.append(String.format("&%s=%s", "廠牌", jsonObject.getString("BRAND_NM")));
                        }
                        if (!jsonObject.getString("COLOR_CD").equals("")){
                                returnString.append(String.format("&%s=%s", "顏色", jsonObject.getString("COLOR_NM")));
                        }
                        if (!jsonObject.getString("OC_DT").equals("")){
                                returnString.append(String.format("&%s=%s", "拾得日期", jsonObject.getString("OC_DT")));
                        }
                    }

                    return returnString.substring(1).toString();
        }
            private HashMap<String, String> GetLogFor0602Query()
        {
                    LinkedHashMap logCols =new LinkedHashMap <String, String>();

                    logCols.put("IMEI", "IMEI碼");
                    logCols.put("MAC", "MAC碼");
                    logCols.put("E8_E_CASE_NO", "e化案號");
                    logCols.put("E8_AC_UNIT_NM", "受理單位");
                    logCols.put("E8_RP_DT", "報案日期時間");
                    logCols.put("E8_CC_TYPE_NM", "刑案案類");
                    logCols.put("E8_OC_CITY_NM", "發生地點縣市");

                    return logCols;
        }
}
