<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="npa.op.util.E8ImeiWebService"%>
<%@page import="npa.op.control.WebServiceServlet"%>
<%@page import ="org.w3c.dom.Document"%>
<%@page import ="org.w3c.dom.NodeList"%>
<%@page import ="npa.op.util.DateUtil"%>
<%@page import ="npa.op.vo.*,npa.op.util.*"%>
<%
    User user = NPAUtil.getUser(session);
    String errMsg = "";
    String outputStr = "";
    int count = 0;
    String rcNo = "";
    String acUnitNm = "";
    String acDt = "";
    String puDt = "";
    String imeiCode = "";
    String type = "";
    imeiCode = StringEscapeUtils.escapeHtml(request.getParameter("imeiOrMac"));
    type = StringEscapeUtils.escapeHtml(request.getParameter("type"));
    System.out.println(imeiCode);
    System.out.println(type);
    System.out.println(user.getUserId());
    System.out.println(user.getUnitCd());
    System.out.println(user.getUserIp());
    if (imeiCode != null && !imeiCode.equals("")) {
        String strE8ImeiData = "";
        if (type.equals("PHONE")) {
			System.out.println("START");
            strE8ImeiData = E8PhoneWebService.getE8WebServiceByPhone(type, imeiCode, "OP2", "", user.getUserId(), user.getUnitCd(), user.getUserIp());
			System.out.println("END");
        } else {
            strE8ImeiData = E8ImeiWebService.getE8WebServiceByImeiOrMac(type, imeiCode, "OP2", "", user.getUserId(), user.getUnitCd(), user.getUserIp());
        }
//        strE8ImeiData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
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
//                                            "<Message>查有資料</Message>" +
//                                            "</Result>";
        strE8ImeiData = strE8ImeiData.replace("utf-8", "big5");
        Document e8Doc = WebServiceServlet.loadXMLFromString(strE8ImeiData);
        NodeList RTNCodeNodes = e8Doc.getElementsByTagName("Message");
        if (RTNCodeNodes != null && RTNCodeNodes.getLength() > 0) {   //表示沒錯誤
            String RTNCode = "";
            RTNCode = RTNCodeNodes.item(0).getTextContent();
            if (RTNCode.equals("查無資料")) {   //查無資料
                outputStr += "<tr>";
                outputStr += "<td align='right'>查詢結果：</td>";
                outputStr += "<td align='left'>無</td>";
                outputStr += "<td align='right'>IMEI碼：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "<td align='right'>IMEI碼2：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "<td align='right'>電信門號：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "<td align='right'>電信門號2：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "</tr>";
                outputStr += "<tr>";
                outputStr += "<td align='right'>MAC碼：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "<td align='right'>e化案號：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "</tr>";
                outputStr += "<tr>";
                outputStr += "<td align='right'>受理單位：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "<td align='right'>報案日期時間：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "</tr>";
                outputStr += "<tr>";
                outputStr += "<td align='right'>刑案案類：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "<td align='right'>發生地點縣市：</td>";
                outputStr += "<td align='left'></td>";
                outputStr += "</tr>";

            } else {    //查有資料 塞欄位值
                NodeList RowList = e8Doc.getElementsByTagName("Row");
                count = RowList.getLength();
                int i = 0;
                if (count > 1) {    //有多筆
                    outputStr += "<tr><td align='left' colspan='4' class='must'>" + "*查詢結果為多筆：" + "</td></tr>";
                    for (i = 0; i < count; i++) {
                        String IMEI = e8Doc.getElementsByTagName("Imei").item(i).getTextContent();
                        String IMEI2 = e8Doc.getElementsByTagName("Imei2").item(i).getTextContent();
                        String PHONE = e8Doc.getElementsByTagName("PhoneNo").item(i).getTextContent();
                        String PHONE2 = e8Doc.getElementsByTagName("PhoneNo2").item(i).getTextContent();
						String MAC = "";
						if (type.equals("PHONE")) {
							MAC = e8Doc.getElementsByTagName("E8_MAC").item(i).getTextContent();
						}else{
							MAC = e8Doc.getElementsByTagName("Mac").item(i).getTextContent();
						}
                        String E8_E_CASE_NO = e8Doc.getElementsByTagName("E8_E_CASE_NO").item(i).getTextContent();
                        String E8_AC_UNIT_NM = e8Doc.getElementsByTagName("E8_AC_UNIT_NM").item(i).getTextContent();
                        String date = e8Doc.getElementsByTagName("E8_RP_DT").item(i).getTextContent();
                        String time = e8Doc.getElementsByTagName("E8_RP_TM").item(i).getTextContent();
                        String E8_RP_DT = DateUtil.to7TwDateTime(date, time);
                        String E8_CC_TYPE_NM = e8Doc.getElementsByTagName("E8_CC_TYPE_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_CC_TYPE_NM").item(i).getTextContent();
                        String E8_OC_CITY_NM = e8Doc.getElementsByTagName("E8_OC_CITY_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_OC_CITY_NM").item(i).getTextContent();
                        System.out.println("IMEI :" + IMEI + "IMEI2 :" + IMEI2 + " MAC :" + MAC + " E8_E_CASE_NO :" + E8_E_CASE_NO + " E8_AC_UNIT_NM :" + E8_AC_UNIT_NM + " E8_RP_DT : " + E8_RP_DT + "E8_CC_TYPE_NM : " + E8_CC_TYPE_NM + "E8_OC_CITY_NM : " + E8_OC_CITY_NM);
                        outputStr += "<tr><td colspan='4'><font color=blue>" + "==========第" + (i + 1) + "筆==========" + "</font></td></tr>";
                        outputStr += "<tr>";
                        outputStr += "<td align='right'>查詢結果：</td>";
                        outputStr += "<td align='left'>有</td>";
                        outputStr += "<td align='right'>IMEI碼：</td>";
                        outputStr += "<td align='left'>" + IMEI + "</td>";
                        outputStr += "<td align='right'>IMEI碼2：</td>";
                        outputStr += "<td align='left'>" + IMEI2 + "</td>";
                        outputStr += "<td align='right'>電信門號：</td>";
                        outputStr += "<td align='left'>" + PHONE + "</td>";
                        outputStr += "<td align='right'>電信門號2：</td>";
                        outputStr += "<td align='left'>" + PHONE2 + "</td>";
                        outputStr += "</tr>";
                        outputStr += "<tr>";
                        outputStr += "<td align='right'>MAC碼：</td>";
                        outputStr += "<td align='left'>" + MAC + "</td>";
                        outputStr += "<td align='right'>e化案號：</td>";
                        outputStr += "<td align='left'>" + E8_E_CASE_NO + "</td>";
                        outputStr += "</tr>";
                        outputStr += "<tr>";
                        outputStr += "<td align='right'>受理單位：</td>";
                        outputStr += "<td align='left'>" + E8_AC_UNIT_NM + "</td>";
                        outputStr += "<td align='right'>報案日期時間：</td>";
                        outputStr += "<td align='left'>" + E8_RP_DT + "</td>";
                        outputStr += "</tr>";
                        outputStr += "<tr>";
                        outputStr += "<td align='right'>刑案案類：</td>";
                        outputStr += "<td align='left'>" + E8_CC_TYPE_NM + "</td>";
                        outputStr += "<td align='right'>發生地點縣市：</td>";
                        outputStr += "<td align='left'>" + E8_OC_CITY_NM + "</td>";
                        outputStr += "</tr>";
                        outputStr += "<tr></tr>";

                    }
                } else {
                    String IMEI = e8Doc.getElementsByTagName("Imei").item(i).getTextContent();
                    String IMEI2 = e8Doc.getElementsByTagName("Imei2").item(i).getTextContent();
                    String PHONE = e8Doc.getElementsByTagName("PhoneNo").item(i).getTextContent();
                    String PHONE2 = e8Doc.getElementsByTagName("PhoneNo2").item(i).getTextContent();
                    String MAC = "";
					if (type.equals("PHONE")) {
						MAC = e8Doc.getElementsByTagName("E8_MAC").item(i).getTextContent();
					}else{
						MAC = e8Doc.getElementsByTagName("Mac").item(i).getTextContent();
					}
                    String E8_E_CASE_NO = e8Doc.getElementsByTagName("E8_E_CASE_NO").item(i).getTextContent();
                    String E8_AC_UNIT_NM = e8Doc.getElementsByTagName("E8_AC_UNIT_NM").item(i).getTextContent();
                    String date = e8Doc.getElementsByTagName("E8_RP_DT").item(i).getTextContent();
                    String time = e8Doc.getElementsByTagName("E8_RP_TM").item(i).getTextContent();
                    String E8_RP_DT = DateUtil.to7TwDateTime(date, time);
                    String E8_CC_TYPE_NM = e8Doc.getElementsByTagName("E8_CC_TYPE_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_CC_TYPE_NM").item(i).getTextContent();
                    String E8_OC_CITY_NM = e8Doc.getElementsByTagName("E8_OC_CITY_CD").item(i).getTextContent() + "/" + e8Doc.getElementsByTagName("E8_OC_CITY_NM").item(i).getTextContent();
                    System.out.println("IMEI :" + IMEI + "IMEI2 :" + IMEI2 + " MAC :" + MAC + " E8_E_CASE_NO :" + E8_E_CASE_NO + " E8_AC_UNIT_NM :" + E8_AC_UNIT_NM + " E8_RP_DT : " + E8_RP_DT + "E8_CC_TYPE_NM : " + E8_CC_TYPE_NM + "E8_OC_CITY_NM : " + E8_OC_CITY_NM);
                    outputStr += "<tr>";
                    outputStr += "<td align='right'>查詢結果：</td>";
                    outputStr += "<td align='left'>有</td>";
                    outputStr += "<td align='right'>IMEI碼：</td>";
                    outputStr += "<td align='left'>" + IMEI + "</td>";
                    outputStr += "<td align='right'>IMEI碼2：</td>";
                    outputStr += "<td align='left'>" + IMEI2 + "</td>";
                    outputStr += "<td align='right'>電信門號：</td>";
                    outputStr += "<td align='left'>" + PHONE + "</td>";
                    outputStr += "<td align='right'>電信門號2：</td>";
                    outputStr += "<td align='left'>" + PHONE2 + "</td>";
                    outputStr += "</tr>";
                    outputStr += "<tr>";
                    outputStr += "<td align='right'>MAC碼：</td>";
                    outputStr += "<td align='left'>" + MAC + "</td>";
                    outputStr += "<td align='right'>e化案號：</td>";
                    outputStr += "<td align='left'>" + E8_E_CASE_NO + "</td>";
                    outputStr += "</tr>";
                    outputStr += "<tr>";
                    outputStr += "<td align='right'>受理單位：</td>";
                    outputStr += "<td align='left'>" + E8_AC_UNIT_NM + "</td>";
                    outputStr += "<td align='right'>報案日期時間：</td>";
                    outputStr += "<td align='left'>" + E8_RP_DT + "</td>";
                    outputStr += "</tr>";
                    outputStr += "<tr>";
                    outputStr += "<td align='right'>刑案案類：</td>";
                    outputStr += "<td align='left'>" + E8_CC_TYPE_NM + "</td>";
                    outputStr += "<td align='right'>發生地點縣市：</td>";
                    outputStr += "<td align='left'>" + E8_OC_CITY_NM + "</td>";
                    outputStr += "</tr>";
                }
            }

        } else {    //沒Message 表示有錯
            NodeList failNodes = e8Doc.getElementsByTagName("faultstring");
            String failStr = failNodes.item(0).getTextContent();
            System.out.println("E化系統資IMEI料查詢錯誤 : " + failStr);
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>e化報案受理資料</title>
    </head>

    <body>
        <form action="" class="Form1">
            <table width="90%">
                <tr><td>
                        <fieldset>
                            <legend>與「受理e化報案系統」互查</legend>
                            <table width="100%">
                                <%=validator.Validator.escapeHtml(outputStr)%>
                                <tr><td align="left" colspan="4"><font color=blue>說明：詳細受理資料請至「e化報案管理平台」查詢</font></td></tr>
                            </table>
                        </fieldset>
                    </td></tr>
            </table>
        </form>
    </body>
</html>
