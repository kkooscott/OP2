/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npa.op.control;

import com.aspose.cells.Cells;
import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.words.BreakType;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.Font;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.Shape;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.MailMergeCleanupOptions;
import com.aspose.words.SectionStart;
import com.aspose.words.DataRelation;
import com.aspose.words.DataRow;
import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import com.aspose.words.ParagraphAlignment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import edu.emory.mathcs.backport.java.util.Arrays;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import static npa.op.util.ReportUtil.*;
import npa.op.base.AjaxBaseServlet;
import static npa.op.base.AjaxBaseServlet.AJAX_REQ_ACTION_KEY;
import npa.op.util.DateUtil;
import static npa.op.util.DateUtil.get8UsDateFormatDB;
import npa.op.util.NPALOG2Util;
import npa.op.util.NPAUtil;
import npa.op.util.NpaConfig;
import npa.op.util.StringUtil;
import npa.op.vo.User;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import npa.op.dao.ReportDao;
import static npa.op.base.AjaxBaseServlet.AJAX_REQ_ACTION_KEY;
import npa.op.dao.OPDT_E0_NPAUNITDao;

/**
 *
 * @author curitis
 */
@WebServlet("/ReportServlet")
public class ReportServlet extends AjaxBaseServlet {

    private static final long serialVersionUID = 1L;
    Logger log = Logger.getLogger(ReportServlet.class);

    @Override
    protected void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user,
            JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {

        argJsonObj.put("userVO", user);
        if (argJsonObj.has("paramValue[reportName]")) {
            argJsonObj.put("reportName", argJsonObj.get("paramValue[reportName]"));
        }
        if (argJsonObj.has("paramValue[newReportName]")) {
            argJsonObj.put("newReportName", argJsonObj.get("paramValue[newReportName]"));
        }
        String serverRealPath = request.getServletContext().getRealPath("/reportTemplate") + "//";
        String serverTempPath = NpaConfig.getString("fileurl");
        String reportName = argJsonObj.getString("reportName");

        String[] ajaxAction = argJsonObj.getString(AJAX_REQ_ACTION_KEY).split(",");
        String[] reportNameList = reportName.split(",");
        String[] newReportNameList = argJsonObj.getString("newReportName").split(",");
        List<String> reportPath = new ArrayList<String>();
        List<String> format = new ArrayList<String>();
        List<String> newReportName = new ArrayList<String>();
        List<String> oldReportName = new ArrayList<String>();
        ArrayList<HashMap> detail = null;
        ArrayList<HashMap> basic = null;
        ArrayList<HashMap> claim = null;
        ArrayList<HashMap> Announce = null;
        ArrayList<HashMap> AnDl = null;
        java.util.Date now = new java.util.Date();
        for (int i = 0; i < reportNameList.length; i++) {
            File filePath = new File(serverRealPath + reportNameList[i]);
            reportPath.add(filePath.toString());
            String[] tempSubName = reportNameList[i].split("\\.");
            format.add(tempSubName[1]);
            newReportName.add(newReportNameList[i]);
            oldReportName.add(reportNameList[i]);
        }
        ReportDao ReportDao = null ;
        Map<String, List> list = null;
        DataSet ds = new DataSet();
        DataTable dt = null;
        DataSet ds_1 = new DataSet();
        DataTable dt_1 = null;
        CachedRowSet crs = null;
        CachedRowSet crs_1 = null;
        JSONObject dateConvertJson = null;
        HashMap<String, Object> data = null;
        byte[] image = null;
        FileInputStream fis = null;
        String tempPicPath = "";
        Boolean flagData = false;
        for (int q = 0; q < ajaxAction.length; q++) {
            switch (ajaxAction[q]) {
                case "OP07A01Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    //拾得物總價值
                    if (argJsonObj.has("OP_PU_YN_OV500") && !argJsonObj.getString("OP_PU_YN_OV500").equals("")) {
                        dateConvertJson.put("OP_PU_YN_OV500",argJsonObj.getString("OP_PU_YN_OV500"));
                    }
                    //物品屬性
                    if (argJsonObj.has("OP_PUOJ_ATTR_CD") && !argJsonObj.getString("OP_PUOJ_ATTR_CD").equals("")) {
                        dateConvertJson.put("OP_PUOJ_ATTR_CD",argJsonObj.getString("OP_PUOJ_ATTR_CD"));
                    }
                    list = ReportDao.printByOP07A01Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "拾得物登記簿列印.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    //記日誌
                    JSONArray logResult = new JSONArray();
                    JSONObject logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    LinkedHashMap logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2Util NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A01Q_01", user, "", "", "");
                    
                    break;   
                    case "OP07A01Q.doc":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);

                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    //拾得物總價值
                    if (argJsonObj.has("OP_PU_YN_OV500") && !argJsonObj.getString("OP_PU_YN_OV500").equals("")) {
                        dateConvertJson.put("OP_PU_YN_OV500",argJsonObj.getString("OP_PU_YN_OV500"));
                    }
                    //物品屬性
                    if (argJsonObj.has("OP_PUOJ_ATTR_CD") && !argJsonObj.getString("OP_PUOJ_ATTR_CD").equals("")) {
                        dateConvertJson.put("OP_PUOJ_ATTR_CD",argJsonObj.getString("OP_PUOJ_ATTR_CD"));
                    }
                    
                    crs = ReportDao.printByOP07A01Q_DOC(dateConvertJson);
                    if ( crs.size() > 0 ) {
                        flagData = true;
                    }
                    
                    dt = new DataTable(crs, "Footer");
                    ds.getTables().add(dt);
                    crs =  ReportDao.printUnitHeard(dateConvertJson.getJSONObject("newObj"));
                    dt = new DataTable(crs, "Data1");
                    ds.getTables().add(dt);

                    dt = new DataTable(crs, "Data2");
                    ds.getTables().add(dt);

                    dt = new DataTable(crs, "Data3");
                    ds.getTables().add(dt);
//                    sendToBrowserWord(reportPath.get(0), response, "拾得物登記簿列印.doc", serverTempPath, ds, argJsonObj, true, q);
                    sendToBrowserPDF(reportPath.get(0), response, "拾得物登記簿列印.pdf", serverTempPath, ds, argJsonObj, true, q);
                    response.flushBuffer();
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A01Q_01", user, "", "", "");
                    
                    break;
                    //拾得物收據
                    case "OP02A01Q.doc":
                        com.aspose.words.License license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("拾得物收據.doc", "UTF-8"));
                        //String[] reportType = obj.getString("reportType").split(",");
                        // 需增加才能讓successCallback有反應
//                        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        Document doc = new Document(reportPath.get(0));
                        Document doc2 = new Document(reportPath.get(1));
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                        
                        //受理基本資料序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }
                        
                        data = ReportDao.printByOP02A01Q_DOC(dateConvertJson);
                        crs = (CachedRowSet) data.get("BasicList");
                        crs.beforeFirst();
                        crs.next();
//                      dt = new DataTable(crs, "Footer");
                        detail = (ArrayList) data.get("detailList");
                        int count = detail.size() / 5;
                        if( detail.size() % 5 > 0){
                            count++;
                        }
                        if( count == 0 ){
                            count = 1;
                        }
                        
                        for( int pageNum = 1; pageNum <= count; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                            DataTable dt_2 = new DataTable("Footer");
                            dt_2.getColumns().add("OP_AC_UNIT_NM");
                            dt_2.getColumns().add("OP_AC_RCNO");
                            dt_2.getColumns().add("OP_PUPO_NAME");
                            dt_2.getColumns().add("OP_ADDR");
                            dt_2.getColumns().add("OP_PUPO_PHONENO");
                            dt_2.getColumns().add("OP_PUPO_IDN");
                            dt_2.getColumns().add("OP_PU_PLACE");
                            dt_2.getColumns().add("OP_PU_DATE");
                            dt_2.getColumns().add("OP_PU_TIME");

                            dt_2.getColumns().add("OP_PUOJ_NM1");
                            dt_2.getColumns().add("OP_QTY1");
                            dt_2.getColumns().add("OP_QTY_UNIT1");
                            dt_2.getColumns().add("OP_FEATURE1");
                            dt_2.getColumns().add("OP_PUOJ_NM2");
                            dt_2.getColumns().add("OP_QTY2");
                            dt_2.getColumns().add("OP_QTY_UNIT2");
                            dt_2.getColumns().add("OP_FEATURE2");
                            dt_2.getColumns().add("OP_PUOJ_NM3");
                            dt_2.getColumns().add("OP_QTY3");
                            dt_2.getColumns().add("OP_QTY_UNIT3");
                            dt_2.getColumns().add("OP_FEATURE3");
                            dt_2.getColumns().add("OP_PUOJ_NM4");
                            dt_2.getColumns().add("OP_QTY4");
                            dt_2.getColumns().add("OP_QTY_UNIT4");
                            dt_2.getColumns().add("OP_FEATURE4");
                            dt_2.getColumns().add("OP_PUOJ_NM5");
                            dt_2.getColumns().add("OP_QTY5");
                            dt_2.getColumns().add("OP_QTY_UNIT5");
                            dt_2.getColumns().add("OP_FEATURE5");
                            
                            dt_2.getColumns().add("YEAR");
                            dt_2.getColumns().add("MONTH");
                            dt_2.getColumns().add("DATE");
                            
                            DataRow dr = new DataRow(dt_2);
                            String[] tmp = new String[20];
                            Arrays.fill(tmp,"");
                            //DETAIL
                            for( int j = 0 ; j < 5; j++){ 
                                if ( (j+(pageNum-1)*5) == detail.size() ){
                                    break;
                                }
                                tmp[j*4+0] = detail.get(j+(pageNum-1)*5).get("OP_PUOJ_NM").toString();
                                String[] opQty = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString().toString().split("\\.");
                                if( opQty[1].equals("00") ){
                                    tmp[j*4+1] = opQty[0];
                                }else{
                                    tmp[j*4+1] = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString();
                                }
                                tmp[j*4+2] = detail.get(j+(pageNum-1)*5).get("OP_QTY_UNIT").toString();
                                tmp[j*4+3] = detail.get(j+(pageNum-1)*5).get("OP_FEATURE").toString();
                            }
                            //BASIC
                            now = new java.util.Date();
                            String[] SplitYear = getNowTime(now).split("/");
                            dt_2.getRows().add(crs.getString("OP_AC_UNIT_NM"),crs.getString("OP_AC_RCNO"),crs.getString("OP_PUPO_NAME"),
                                    crs.getString("OP_ADDR"),crs.getString("OP_PUPO_PHONENO"),crs.getString("OP_PUPO_IDN"),
                                    crs.getString("OP_PU_PLACE"),crs.getString("OP_PU_DATE"),crs.getString("OP_PU_TIME"),tmp[0],tmp[1],tmp[2],tmp[3],tmp[4],tmp[5]
                            ,tmp[6],tmp[7],tmp[8],tmp[9],tmp[10],tmp[11],tmp[12],tmp[13],tmp[14],tmp[15],tmp[16],tmp[17],tmp[18],tmp[19],
                            SplitYear[0],SplitYear[1],SplitYear[2]);
                            
                            
                            ds.getTables().add(dt_2);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            //第二聯
                            doc2.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                                //第二聯
                                Document doc3 = new Document(reportPath.get(1));
                                doc3.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc3.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc3.getMailMerge().executeWithRegions(ds); 
                                doc2.appendDocument(doc3, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                                //第二聯
                                doc2.getMailMerge().executeWithRegions(ds);
                            }
                            
                        }
                        doc.appendDocument(doc2, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                        response.setContentType("application/msword");
                        
                        doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                        response.flushBuffer();
                    break;
                    
                    //受理拾得物案陳報單
                     case "OP02A09Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                       //if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                          //  dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        //}
                         if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }
                        data = ReportDao.printByOP02A09Q_DOC(dateConvertJson);
                        crs = (CachedRowSet) data.get("BasicList");

                        crs.beforeFirst();
                        crs.next();
                        ds = new DataSet();
                        DataTable dt_3 = new DataTable("Footer");
                        dt_3.getColumns().add("OP_AC_UNIT_NM");
                        dt_3.getColumns().add("OP_AC_DATE");
                        dt_3.getColumns().add("OP_PUPO_NAME");
                        dt_3.getColumns().add("OP_PU_DATE");
                        dt_3.getColumns().add("OP_PU_TIME");
                        dt_3.getColumns().add("OP_PU_PLACE");
                        dt_3.getColumns().add("OP_AC_RCNO");
                        dt_3.getColumns().add("OP_PUPO_TP_NM");
                        dt_3.getColumns().add("OP_PUPO_NAME");
                        dt_3.getColumns().add("OP_PUOJ_NM");
                        String OP_PUOJ_NM = (String) data.get("DetailList");
                        dt_3.getRows().add( crs.getString("OP_AC_UNIT_NM"), DateUtil.getReportDateTime4(crs.getString("OP_AC_DATE")), crs.getString("OP_PUPO_NAME")
                                           , DateUtil.getReportDateTime3(crs.getString("OP_PU_DATE")), DateUtil.toTwTime(crs.getString("OP_PU_TIME")), crs.getString("OP_PU_PLACE")
                                           , crs.getString("OP_AC_RCNO"),crs.getString("OP_PUPO_TP_NM"), crs.getString("OP_PUPO_NAME"), OP_PUOJ_NM);
                        ds.getTables().add(dt_3);
                        
                        sendToBrowserWord(reportPath.get(0), response, "受理拾得物案陳報單.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //查詢失物招領手機或其他行動裝置電信門號單20220208
                    case "OP02A10Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                        
                        //受理基本資料序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }
                        data = ReportDao.printByOP02A10Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("BasicList");
                        
                        ds = new DataSet();
                        DataTable dt_10 = new DataTable("Footer");
                        dt_10.getColumns().add("OP_AC_UNIT_NM");
                        dt_10.getColumns().add("OP_PUPO_NAME");
                        dt_10.getColumns().add("OP_AC_D_UNIT_NM");
                        dt_10.getRows().add(basic.get(0).get("OP_AC_UNIT_NM"),basic.get(0).get("OP_PUPO_NAME"),basic.get(0).get("OP_AC_D_UNIT_NM"));
                        ds.getTables().add(dt_10);
                        
                        sendToBrowserWord(reportPath.get(0), response, basic.get(0).get("OP_AC_D_UNIT_NM")+"查詢失物招領手機或其他行動裝置電信門號單.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書
                    case "OP02A11Q.doc":
                        license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc", "UTF-8"));
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        doc = new Document(reportPath.get(0));
                        doc2 = new Document(reportPath.get(1));
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                        
                        //受理基本資料序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }
                        
                        data = ReportDao.printByOP02A11Q_DOC(dateConvertJson);
                        crs = (CachedRowSet) data.get("BasicList");
                        crs.beforeFirst();
                        crs.next();
//                      dt = new DataTable(crs, "Footer");
                        detail = (ArrayList) data.get("detailList");
                        count = detail.size() / 6;
                        if( detail.size() % 6 > 0){
                            count++;
                        }
                        if( count == 0 ){
                            count = 1;
                        }
                        
                        for( int pageNum = 1; pageNum <= count; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                            DataTable dt_2 = new DataTable("Footer");
                            dt_2.getColumns().add("OP_AC_UNIT_NM");
                            dt_2.getColumns().add("OP_AC_RCNO");
                            dt_2.getColumns().add("OP_PUPO_NAME");
                            dt_2.getColumns().add("OP_ADDR");
                            dt_2.getColumns().add("OP_PUPO_PHONENO");
                            dt_2.getColumns().add("OP_PUPO_IDN");
                            dt_2.getColumns().add("OP_PU_PLACE");
                            dt_2.getColumns().add("OP_PU_DATE");
                            dt_2.getColumns().add("OP_PU_TIME");

                            dt_2.getColumns().add("OP_PUOJ_NM1");
                            dt_2.getColumns().add("OP_QTY1");
                            dt_2.getColumns().add("OP_QTY_UNIT1");
                            dt_2.getColumns().add("OP_FEATURE1");
                            dt_2.getColumns().add("OP_PUOJ_NM2");
                            dt_2.getColumns().add("OP_QTY2");
                            dt_2.getColumns().add("OP_QTY_UNIT2");
                            dt_2.getColumns().add("OP_FEATURE2");
                            dt_2.getColumns().add("OP_PUOJ_NM3");
                            dt_2.getColumns().add("OP_QTY3");
                            dt_2.getColumns().add("OP_QTY_UNIT3");
                            dt_2.getColumns().add("OP_FEATURE3");
                            dt_2.getColumns().add("OP_PUOJ_NM4");
                            dt_2.getColumns().add("OP_QTY4");
                            dt_2.getColumns().add("OP_QTY_UNIT4");
                            dt_2.getColumns().add("OP_FEATURE4");
                            dt_2.getColumns().add("OP_PUOJ_NM5");
                            dt_2.getColumns().add("OP_QTY5");
                            dt_2.getColumns().add("OP_QTY_UNIT5");
                            dt_2.getColumns().add("OP_FEATURE5");
                            dt_2.getColumns().add("OP_PUOJ_NM6");
                            dt_2.getColumns().add("OP_QTY6");
                            dt_2.getColumns().add("OP_QTY_UNIT6");
                            dt_2.getColumns().add("OP_FEATURE6");
                            
                            dt_2.getColumns().add("YEAR");
                            dt_2.getColumns().add("MONTH");
                            dt_2.getColumns().add("DATE");
                            
                            DataRow dr = new DataRow(dt_2);
                            String[] tmp = new String[24];
                            Arrays.fill(tmp,"");
                            //DETAIL
                            for( int j = 0 ; j < 6; j++){ 
                                if ( (j+(pageNum-1)*6) == detail.size() ){
                                    break;
                                }
                                tmp[j*4+0] = detail.get(j+(pageNum-1)*6).get("OP_PUOJ_NM").toString();
                                String[] opQty = detail.get(j+(pageNum-1)*6).get("OP_QTY").toString().toString().split("\\.");
                                if( opQty[1].equals("00") ){
                                    tmp[j*4+1] = opQty[0];
                                }else{
                                    tmp[j*4+1] = detail.get(j+(pageNum-1)*6).get("OP_QTY").toString();
                                }
                                tmp[j*4+2] = detail.get(j+(pageNum-1)*6).get("OP_QTY_UNIT").toString();
                                tmp[j*4+3] = detail.get(j+(pageNum-1)*6).get("OP_FEATURE").toString();
                            }
                            //BASIC
                            now = new java.util.Date();
                            String[] SplitYear = getNowTime(now).split("/");
                            dt_2.getRows().add(crs.getString("OP_AC_UNIT_NM"),crs.getString("OP_AC_RCNO"),crs.getString("OP_PUPO_NAME"),crs.getString("OP_ADDR"),crs.getString("OP_PUPO_PHONENO"),crs.getString("OP_PUPO_IDN"),crs.getString("OP_PU_PLACE"),crs.getString("OP_PU_DATE"),crs.getString("OP_PU_TIME"),
                            tmp[0],tmp[1],tmp[2],tmp[3],tmp[4],tmp[5],tmp[6],tmp[7],tmp[8],tmp[9],tmp[10],tmp[11],tmp[12],tmp[13],tmp[14],tmp[15],tmp[16],tmp[17],tmp[18],tmp[19],tmp[20],tmp[21],tmp[22],tmp[23],
                            SplitYear[0],SplitYear[1],SplitYear[2]);
                            
                            
                            ds.getTables().add(dt_2);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            //第二聯
                            doc2.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                                //第二聯
                                Document doc3 = new Document(reportPath.get(1));
                                doc3.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc3.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc3.getMailMerge().executeWithRegions(ds); 
                                doc2.appendDocument(doc3, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                                //第二聯
                                doc2.getMailMerge().executeWithRegions(ds);
                            }
                            
                        }
                        doc.appendDocument(doc2, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                        response.setContentType("application/msword");
                        
                        doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                        response.flushBuffer();
                    break;
                    //受理民眾交存拾得遺失物作業程序檢核表
                    case "OP02A03Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }

                        data = ReportDao.printByOP02A03Q_DOC(dateConvertJson);
                        crs = (CachedRowSet) data.get("BasicList");
                        crs.beforeFirst();
                        crs.next();
                        ds = new DataSet();
                        DataTable dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_PUPO_NAME");

                        dt_2.getRows().add( crs.getString("OP_PUPO_NAME") );
                        ds.getTables().add(dt_2);

                        sendToBrowserWord(reportPath.get(0), response, "受理民眾交存拾得遺失物作業程序檢核表.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //核對情形回覆函–核對結果正確
                    case "OP02A04Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }
                        //認領人TABLE類型
                        if (argJsonObj.has("CLAIM_TYPE") && !argJsonObj.getString("CLAIM_TYPE").equals("")) {
                            dateConvertJson.put("CLAIM_TYPE", argJsonObj.getString("CLAIM_TYPE"));
                        }
                        //認領人TABLE 序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }

                        data = ReportDao.printByOP02A04Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        claim = (ArrayList) data.get("claimList");
                        Announce = (ArrayList) data.get("AnnounceList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AC_UNIT_NM");
                        dt_2.getColumns().add("OP_AC_RCNO");
                        dt_2.getColumns().add("OP_AC_STAFF_NM");
                        dt_2.getColumns().add("OP_AC_UNIT_TEL");
                        dt_2.getColumns().add("OP_DOC_WD_NO");
                        dt_2.getColumns().add("OP_PUCP_NAME");
                        dt_2.getColumns().add("OP_FM_DATE");
                        dt_2.getColumns().add("OP_AN_DATE_BEG");
                        dt_2.getColumns().add("OP_AN_DATE_END");

                        String OP_AN_DATE_BEG="",OP_AN_DATE_END="",OP_DOC_WD_NO="";

                        if( Announce.isEmpty() ){
                            OP_AN_DATE_BEG = "";
                            OP_AN_DATE_END = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            OP_AN_DATE_BEG = "中華民國 " + DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_BEG").toString());
                            OP_AN_DATE_END = DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_END").toString());
                            OP_DOC_WD_NO = Announce.get(0).get("OP_DOC_WD") + "字 "+ Announce.get(0).get("OP_DOC_NO") +"號";
                        }
                        
                        if( argJsonObj.getString("CLAIM_TYPE").equals("NOClaim") ){
                            dt_2.getRows().add( basic.get(0).get("OP_AC_UNIT_NM"), basic.get(0).get("OP_AC_RCNO"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                            OP_DOC_WD_NO,"", "", OP_AN_DATE_BEG, OP_AN_DATE_END);
                            ds.getTables().add(dt_2);
                        }else{
                            dt_2.getRows().add( basic.get(0).get("OP_AC_UNIT_NM"), basic.get(0).get("OP_AC_RCNO"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                            OP_DOC_WD_NO,claim.get(0).get("OP_PUCP_NAME"), DateUtil.getReportDateTime3(claim.get(0).get("OP_FM_DATE").toString()), OP_AN_DATE_BEG, OP_AN_DATE_END);
                            ds.getTables().add(dt_2);
                        }

                        sendToBrowserWord(reportPath.get(0), response, "核對情形回覆函–核對結果正確.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //核對情形回覆函–核對結果錯誤
                    case "OP02A05Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }
                        //認領人TABLE類型
                        if (argJsonObj.has("CLAIM_TYPE") && !argJsonObj.getString("CLAIM_TYPE").equals("")) {
                            dateConvertJson.put("CLAIM_TYPE", argJsonObj.getString("CLAIM_TYPE"));
                        }
                        //認領人TABLE 序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }

                        data = ReportDao.printByOP02A04Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        claim = (ArrayList) data.get("claimList");
                        Announce = (ArrayList) data.get("AnnounceList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AC_UNIT_NM");
                        dt_2.getColumns().add("OP_AC_RCNO");
                        dt_2.getColumns().add("OP_AC_STAFF_NM");
                        dt_2.getColumns().add("OP_AC_UNIT_TEL");
                        dt_2.getColumns().add("OP_DOC_WD_NO");
                        dt_2.getColumns().add("OP_PUCP_NAME");
                        dt_2.getColumns().add("OP_FM_DATE");
                        dt_2.getColumns().add("OP_AN_DATE_BEG");
                        dt_2.getColumns().add("OP_AN_DATE_END");

                        if( Announce.isEmpty() ){
                            OP_AN_DATE_BEG = "";
                            OP_AN_DATE_END = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            OP_AN_DATE_BEG = "中華民國 " + DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_BEG").toString());
                            OP_AN_DATE_END = DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_END").toString());
                            OP_DOC_WD_NO = Announce.get(0).get("OP_DOC_WD") + "字 "+ Announce.get(0).get("OP_DOC_NO") +"號";
                        }
                        if( argJsonObj.getString("CLAIM_TYPE").equals("NOClaim") ){
                            dt_2.getRows().add( basic.get(0).get("OP_AC_UNIT_NM"), basic.get(0).get("OP_AC_RCNO"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                            OP_DOC_WD_NO,"", "", OP_AN_DATE_BEG, OP_AN_DATE_END);
                            ds.getTables().add(dt_2);
                        }else{
                            dt_2.getRows().add( basic.get(0).get("OP_AC_UNIT_NM"), basic.get(0).get("OP_AC_RCNO"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                            OP_DOC_WD_NO,claim.get(0).get("OP_PUCP_NAME"), DateUtil.getReportDateTime3(claim.get(0).get("OP_FM_DATE").toString()), OP_AN_DATE_BEG, OP_AN_DATE_END);
                            ds.getTables().add(dt_2);
                        }
                        sendToBrowserWord(reportPath.get(0), response, "核對情形回覆函–核對結果錯誤.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //認領遺失物領據
                    case "OP02A06Q.doc":
                        license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("認領遺失物領據.doc", "UTF-8"));
                        //String[] reportType = obj.getString("reportType").split(",");
                        // 需增加才能讓successCallback有反應
//                        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        doc = new Document(reportPath.get(0));
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                    
                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }
                        //認領人TABLE類型
                        if (argJsonObj.has("CLAIM_TYPE") && !argJsonObj.getString("CLAIM_TYPE").equals("")) {
                            dateConvertJson.put("CLAIM_TYPE", argJsonObj.getString("CLAIM_TYPE"));
                        }
                        //認領人TABLE 序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }

                        data = ReportDao.printByOP02A06Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        detail = (ArrayList) data.get("detailList");
                        claim = (ArrayList) data.get("claimList");
                        //頁首插入編號
                        DocumentBuilder documentBuilder = new DocumentBuilder(doc);
                        documentBuilder.moveToHeaderFooter(HeaderFooterType.HEADER_PRIMARY);
                        documentBuilder.getParagraphFormat().setAlignment(ParagraphAlignment.RIGHT);
                        documentBuilder.write("編號" +basic.get(0).get("OP_AC_RCNO").toString());
                        
                        count = detail.size() / 5;
                        if( detail.size() % 5 > 0){
                            count++;
                        }
                        if( count == 0 ){
                            count = 1;
                        }
                        
                        for( int pageNum = 1; pageNum <= count; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                            dt_2 = new DataTable("Footer");
                            dt_2.getColumns().add("OP_AC_UNIT_NM");

                            dt_2.getColumns().add("OP_PUOJ_NM1");
                            dt_2.getColumns().add("OP_QTY1");
                            dt_2.getColumns().add("OP_QTY_UNIT1");
                            dt_2.getColumns().add("OP_FEATURE1");
                            dt_2.getColumns().add("OP_REMARK1");
                            dt_2.getColumns().add("OP_PUOJ_NM2");
                            dt_2.getColumns().add("OP_QTY2");
                            dt_2.getColumns().add("OP_QTY_UNIT2");
                            dt_2.getColumns().add("OP_FEATURE2");
                            dt_2.getColumns().add("OP_REMARK2");
                            dt_2.getColumns().add("OP_PUOJ_NM3");
                            dt_2.getColumns().add("OP_QTY3");
                            dt_2.getColumns().add("OP_QTY_UNIT3");
                            dt_2.getColumns().add("OP_FEATURE3");
                            dt_2.getColumns().add("OP_REMARK3");
                            dt_2.getColumns().add("OP_PUOJ_NM4");
                            dt_2.getColumns().add("OP_QTY4");
                            dt_2.getColumns().add("OP_QTY_UNIT4");
                            dt_2.getColumns().add("OP_FEATURE4");
                            dt_2.getColumns().add("OP_REMARK4");
                            dt_2.getColumns().add("OP_PUOJ_NM5");
                            dt_2.getColumns().add("OP_QTY5");
                            dt_2.getColumns().add("OP_QTY_UNIT5");
                            dt_2.getColumns().add("OP_FEATURE5");
                            dt_2.getColumns().add("OP_REMARK5");


                            dt_2.getColumns().add("OP_PUCP_NAME");
                            dt_2.getColumns().add("OP_PUCP_ADDR");
                            dt_2.getColumns().add("OP_PUCP_IDN");
                            dt_2.getColumns().add("OP_PUCP_PHONENO");

                            dt_2.getColumns().add("YEAR");
                            dt_2.getColumns().add("MONTH");
                            dt_2.getColumns().add("DATE");
                            String[] tmp = new String[25];
                            Arrays.fill(tmp,"");
                            //DETAIL
                            for( int j = 0 ; j < 5; j++){ 
                                if ( (j+(pageNum-1)*5) == detail.size() ){
                                    break;
                                }
                                tmp[j*5+0] = detail.get(j+(pageNum-1)*5).get("OP_PUOJ_NM").toString();
                                String[] opQty = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString().split("\\.");
                                if( opQty[1].equals("00") ){
                                    tmp[j*5+1] = opQty[0];
                                }else{
                                    tmp[j*5+1] = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString();
                                }
                                tmp[j*5+2] = detail.get(j+(pageNum-1)*5).get("OP_QTY_UNIT").toString();
                                tmp[j*5+3] = detail.get(j+(pageNum-1)*5).get("OP_FEATURE").toString();
                                tmp[j*5+4] = detail.get(j+(pageNum-1)*5).get("OP_REMARK").toString();
                            }
                            now = new java.util.Date();
                            String[] SplitYear = getNowTime(now).split("/");
                            String OP_PUCP_NAME="",OP_PUCP_ADDR="",OP_PUCP_IDN="",OP_PUCP_PHONENO="";
                            if( claim == null ){
                                
                            }else{
                                OP_PUCP_NAME = claim.get(0).get("OP_PUCP_NAME").toString();
                                OP_PUCP_ADDR = claim.get(0).get("OP_PUCP_ADDR").toString();
                                OP_PUCP_IDN = claim.get(0).get("OP_PUCP_IDN").toString();
                                OP_PUCP_PHONENO = claim.get(0).get("OP_PUCP_PHONENO").toString();
                            }
                            dt_2.getRows().add( basic.get(0).get("OP_AC_UNIT_NM"), tmp[0], tmp[1], tmp[2], tmp[3], tmp[4], tmp[5], 
                                    tmp[6], tmp[7], tmp[8], tmp[9], tmp[10], tmp[11], tmp[12], tmp[13], tmp[14], tmp[15], tmp[16], 
                                    tmp[17], tmp[18], tmp[19], tmp[20], tmp[21], tmp[22], tmp[23], tmp[24], OP_PUCP_NAME, 
                                    OP_PUCP_ADDR, OP_PUCP_IDN, OP_PUCP_PHONENO, 
                                    SplitYear[0],SplitYear[1],SplitYear[2]);
                            
                            ds.getTables().add(dt_2);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                            }
                        }
                        
                        response.setContentType("application/msword");
                        
                        doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                        response.flushBuffer();
                    break;
                    //認領遺失物領據
                    case "OP02A07Q.doc":
                        license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("領取拾得物領據.doc", "UTF-8"));
                        //String[] reportType = obj.getString("reportType").split(",");
                        // 需增加才能讓successCallback有反應
//                        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        doc = new Document(reportPath.get(0));
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                    
                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }
                        //認領人TABLE類型
                        if (argJsonObj.has("CLAIM_TYPE") && !argJsonObj.getString("CLAIM_TYPE").equals("")) {
                            dateConvertJson.put("CLAIM_TYPE", argJsonObj.getString("CLAIM_TYPE"));
                        }
                        //認領人TABLE 序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }

                        data = ReportDao.printByOP02A06Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        detail = (ArrayList) data.get("detailList");
                        claim = (ArrayList) data.get("claimList");
                        
                        count = detail.size() / 3;
                        if( detail.size() % 3 > 0){
                            count++;
                        }
                        if( count == 0 ){
                            count = 1;
                        }
                        
                        for( int pageNum = 1; pageNum <= count; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                            dt_2 = new DataTable("Footer");
                            dt_2.getColumns().add("OP_AC_RCNO");
                            dt_2.getColumns().add("OP_AC_UNIT_NM");

                            dt_2.getColumns().add("OP_PUOJ_NM1");
                            dt_2.getColumns().add("OP_QTY1");
                            dt_2.getColumns().add("OP_QTY_UNIT1");
                            dt_2.getColumns().add("OP_FEATURE1");
                            dt_2.getColumns().add("OP_REMARK1");
                            dt_2.getColumns().add("OP_PUOJ_NM2");
                            dt_2.getColumns().add("OP_QTY2");
                            dt_2.getColumns().add("OP_QTY_UNIT2");
                            dt_2.getColumns().add("OP_FEATURE2");
                            dt_2.getColumns().add("OP_REMARK2");
                            dt_2.getColumns().add("OP_PUOJ_NM3");
                            dt_2.getColumns().add("OP_QTY3");
                            dt_2.getColumns().add("OP_QTY_UNIT3");
                            dt_2.getColumns().add("OP_FEATURE3");
                            dt_2.getColumns().add("OP_REMARK3");
//                            dt_2.getColumns().add("OP_PUOJ_NM4");
//                            dt_2.getColumns().add("OP_QTY4");
//                            dt_2.getColumns().add("OP_QTY_UNIT4");
//                            dt_2.getColumns().add("OP_FEATURE4");
//                            dt_2.getColumns().add("OP_REMARK4");
//                            dt_2.getColumns().add("OP_PUOJ_NM5");
//                            dt_2.getColumns().add("OP_QTY5");
//                            dt_2.getColumns().add("OP_QTY_UNIT5");
//                            dt_2.getColumns().add("OP_FEATURE5");
//                            dt_2.getColumns().add("OP_REMARK5");


                            dt_2.getColumns().add("OP_PUCP_NAME");
                            dt_2.getColumns().add("OP_PUCP_ADDR");
                            dt_2.getColumns().add("OP_PUCP_IDN");
                            dt_2.getColumns().add("OP_PUCP_PHONENO");

                            dt_2.getColumns().add("YEAR");
                            dt_2.getColumns().add("MONTH");
                            dt_2.getColumns().add("DATE");
                            String[] tmp = new String[15];
                            Arrays.fill(tmp,"");
                            //DETAIL
                            //20220425 Wennie
//                            for( int j = 0 ; j < 5; j++){ 
                            for( int j = 0 ; j < 3; j++){ 
                                if ( (j+(pageNum-1)*3) == detail.size() ){
                                    break;
                                }
                                tmp[j*5+0] = detail.get(j+(pageNum-1)*3).get("OP_PUOJ_NM").toString();
                                String[] opQty = detail.get(j+(pageNum-1)*3).get("OP_QTY").toString().split("\\.");
                                if( opQty[1].equals("00") ){
                                    tmp[j*5+1] = opQty[0];
                                }else{
                                    tmp[j*5+1] = detail.get(j+(pageNum-1)*3).get("OP_QTY").toString();
                                }
                                tmp[j*5+2] = detail.get(j+(pageNum-1)*3).get("OP_QTY_UNIT").toString();
                                tmp[j*5+3] = detail.get(j+(pageNum-1)*3).get("OP_FEATURE").toString();
                                tmp[j*5+4] = detail.get(j+(pageNum-1)*3).get("OP_REMARK").toString();
                            }
                            now = new java.util.Date();
                            String[] SplitYear = getNowTime(now).split("/");
                            String OP_PUCP_NAME="",OP_PUCP_ADDR="",OP_PUCP_IDN="",OP_PUCP_PHONENO="";
                            if( claim == null ){
                                
                            }else{
                                OP_PUCP_NAME = claim.get(0).get("OP_PUCP_NAME").toString();
                                OP_PUCP_ADDR = claim.get(0).get("OP_PUCP_ADDR").toString();
                                OP_PUCP_IDN = claim.get(0).get("OP_PUCP_IDN").toString();
                                OP_PUCP_PHONENO = claim.get(0).get("OP_PUCP_PHONENO").toString();
                            }
//                            dt_3.getRows().add(basic.get(0).get("OP_AC_RCNO"));
//                            ds.getTables().add(dt_3);
                            dt_2.getRows().add(basic.get(0).get("OP_AC_RCNO"),basic.get(0).get("OP_AC_UNIT_NM"), tmp[0], tmp[1], tmp[2], tmp[3], tmp[4], tmp[5], 
                                    tmp[6], tmp[7], tmp[8], tmp[9], tmp[10], tmp[11], tmp[12], tmp[13], tmp[14], OP_PUCP_NAME, 
                                    OP_PUCP_ADDR, OP_PUCP_IDN, OP_PUCP_PHONENO, 
                                    SplitYear[0],SplitYear[1],SplitYear[2]);
                            
                            
                            ds.getTables().add(dt_2);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                            }
                        }
                        
                        response.setContentType("application/msword");
                        
                        doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                        response.flushBuffer();
                    break;
                    //已認領通知函
                    case "OP02A08Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }
                        //認領人TABLE類型
                        if (argJsonObj.has("CLAIM_TYPE") && !argJsonObj.getString("CLAIM_TYPE").equals("")) {
                            dateConvertJson.put("CLAIM_TYPE", argJsonObj.getString("CLAIM_TYPE"));
                        }
                        //認領人TABLE 序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }

                        data = ReportDao.printByOP02A08Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        claim = (ArrayList) data.get("claimList");
                                    //detailList = GetDetail ( obj.getString("OP_BASIC_SEQ_NO") ) ;
                        String puojNm = ReportDao.getOP_PUOJ_NM( basic.get(0).get("OP_SEQ_NO").toString() );
                        Announce = (ArrayList) data.get("AnnounceList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AC_UNIT_NM");
                        dt_2.getColumns().add("OP_AC_STAFF_NM");
                        dt_2.getColumns().add("OP_AC_UNIT_TEL");
                        dt_2.getColumns().add("OP_PUPO_NAME");
                        dt_2.getColumns().add("OP_PU_DATE");
                        dt_2.getColumns().add("OP_NTC_FIND_PO");
                        dt_2.getColumns().add("OP_AC_RCNO");
                        
                        dt_2.getColumns().add("OP_PUOJ_NM");
      
                        dt_2.getColumns().add("OP_PUCP_NAME");

                        dt_2.getColumns().add("OP_AN_DATE_BEG");
                        dt_2.getColumns().add("OP_AN_DATE_END");
                        dt_2.getColumns().add("OP_DOC_WD_NO");
                        
                        String OP_NTC_FIND_PO="",OP_PUCP_NAME="";
                        if( basic.get(0).get("OP_NTC_FIND_PO").equals("") ){
                            
                        }else{
                            OP_NTC_FIND_PO = "、" + basic.get(0).get("OP_NTC_FIND_PO");
                        }
                        if( claim == null ){
                            OP_PUCP_NAME="";
                        }else{
                            OP_PUCP_NAME = claim.get(0).get("OP_PUCP_NAME").toString();
                        }

                        if( Announce.isEmpty() ){
                            OP_AN_DATE_BEG = "";
                            OP_AN_DATE_END = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            OP_AN_DATE_BEG = "中華民國 " + DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_BEG").toString());
                            OP_AN_DATE_END = DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_END").toString());
                            OP_DOC_WD_NO = Announce.get(0).get("OP_DOC_WD") + "字 "+ Announce.get(0).get("OP_DOC_NO") +"號";
                        }
                        dt_2.getRows().add( basic.get(0).get("OP_AC_UNIT_NM"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                        basic.get(0).get("OP_PUPO_NAME"), DateUtil.getReportDateTime3(basic.get(0).get("OP_PU_DATE").toString()), OP_NTC_FIND_PO, basic.get(0).get("OP_AC_RCNO"), 
                        puojNm, OP_PUCP_NAME, OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD_NO);
                        ds.getTables().add(dt_2);

                        sendToBrowserWord(reportPath.get(0), response, "已認領通知函.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //拾得物公告招領公告單
                    case "OP03A01Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }

                        data = ReportDao.printByOP03A01Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        Announce = (ArrayList) data.get("AnnounceList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AN_UNIT_NM");
                        dt_2.getColumns().add("OP_AN_DATE_BEG");
                        dt_2.getColumns().add("OP_AN_DATE_END");
                        dt_2.getColumns().add("OP_DOC_WD_NO");
                        String OP_AN_B_UNIT_NM = "";
                        if( Announce.isEmpty() ){
                            OP_AN_B_UNIT_NM = "";
                            OP_AN_DATE_BEG = "";
                            OP_AN_DATE_END = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            OP_AN_B_UNIT_NM = Announce.get(0).get("OP_AN_B_UNIT_NM").toString();
                            OP_AN_DATE_BEG = "中華民國 " + DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_BEG").toString());
                            OP_AN_DATE_END = DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_END").toString());
                            OP_DOC_WD_NO = Announce.get(0).get("OP_DOC_WD") + "字 "+ Announce.get(0).get("OP_DOC_NO") +"號";
                        }
                        dt_2.getRows().add( OP_AN_B_UNIT_NM, OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD_NO);
                        ds.getTables().add(dt_2);

                        sendToBrowserWord(reportPath.get(0), response, "拾得物公告招領公告單.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //拾得物公告招領清冊
                    case "OP03A02Q.xls":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                        
                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }
                        
                        list = ReportDao.printByOP03A02Q(dateConvertJson);

                        sendToBrowserXls(reportPath.get(0), response, "拾得物公告招領清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                        response.flushBuffer();
                    break;
                    //拾得人領回通知
                    case "OP03A03Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }

                        data = ReportDao.printByOP03A03Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        puojNm = ReportDao.getOP_PUOJ_NM( basic.get(0).get("OP_SEQ_NO").toString() );
                        Announce = (ArrayList) data.get("AnnounceList");
                        AnDl = (ArrayList) data.get("AnDlList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AN_UNIT_NM");
                        dt_2.getColumns().add("OP_AC_STAFF_NM");
                        dt_2.getColumns().add("OP_AC_UNIT_TEL");
                        dt_2.getColumns().add("OP_PUPO_NAME");
                        dt_2.getColumns().add("OP_PU_DATE");
                        dt_2.getColumns().add("OP_AC_RCNO");
                        
                        dt_2.getColumns().add("OP_PUOJ_NM");

                        dt_2.getColumns().add("OP_AN_DATE_BEG");
                        dt_2.getColumns().add("OP_AN_DATE_END");
                        dt_2.getColumns().add("OP_DOC_WD_NO");

//                        if( Announce.isEmpty() ){
//                            OP_AN_DATE_BEG = "";
//                            OP_AN_DATE_END = "";
//                            OP_DOC_WD_NO = "";
//                        }else{
//                            OP_AN_DATE_BEG = "中華民國 " + DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_BEG").toString());
//                            OP_AN_DATE_END = DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_END").toString());
//                            OP_DOC_WD_NO = Announce.get(0).get("OP_DOC_WD") + "字 "+ Announce.get(0).get("OP_DOC_NO") +"號";
//                        }
                        
                        if( AnDl.isEmpty() ){
                            OP_AN_DATE_BEG = "";
                            OP_AN_DATE_END = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            if( !AnDl.get(0).get("OP_NTC_PUPO_DT").equals("") ){
                                OP_AN_DATE_BEG =  "中華民國 " + DateUtil.getReportDateTime3( AnDl.get(0).get("OP_NTC_PUPO_DT").toString());
                                OP_AN_DATE_END = DateUtil.getReportDateTime3(AnDl.get(0).get("OP_NTC_PUPO_DT").toString());
                            }else{
                                OP_AN_DATE_BEG = "";
                                OP_AN_DATE_END = "";
                            }
                            
                            OP_DOC_WD_NO = AnDl.get(0).get("OP_PUPO_DOC_WD") + "字 "+ AnDl.get(0).get("OP_PUPO_DOC_NO") +"號";
                        }
                        
                        dt_2.getRows().add( Announce.get(0).get("OP_AN_B_UNIT_NM"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                        basic.get(0).get("OP_PUPO_NAME"), DateUtil.getReportDateTime3(basic.get(0).get("OP_PU_DATE").toString()), basic.get(0).get("OP_AC_RCNO"), 
                        puojNm, OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD_NO);
                        ds.getTables().add(dt_2);

                        sendToBrowserWord(reportPath.get(0), response, "拾得人領回通知.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //拾得人領回公告
                    case "OP03A04Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }

                        data = ReportDao.printByOP03A04Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        puojNm = ReportDao.getOP_PUOJ_NM( basic.get(0).get("OP_SEQ_NO").toString() );
                        Announce = (ArrayList) data.get("AnnounceList");
                        AnDl = (ArrayList) data.get("AnDlList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AN_UNIT_NM");
                        dt_2.getColumns().add("OP_AC_STAFF_NM");
                        dt_2.getColumns().add("OP_AC_UNIT_TEL");
                        dt_2.getColumns().add("OP_PUPO_NAME");
                        dt_2.getColumns().add("OP_PUPO_GENDER");
                        dt_2.getColumns().add("OP_PU_DATE");
                        
                        dt_2.getColumns().add("OP_PUOJ_NM");
                        
                        dt_2.getColumns().add("OP_NTC_PUPO_DT");
                        dt_2.getColumns().add("OP_DOC_WD_NO");
                        
                        String OP_NTC_PUPO_DT="",OP_PUPO_GENDER="";
                        
                        if( basic.get(0).get("OP_PUPO_GENDER").equals("1") )
                            OP_PUPO_GENDER = "先生";
                        else if( basic.get(0).get("OP_PUPO_GENDER").equals("2") )
                            OP_PUPO_GENDER = "小姐";
                        else
                            OP_PUPO_GENDER = "";
                        
                        if( AnDl.isEmpty() ){
                            OP_NTC_PUPO_DT = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            if( !AnDl.get(0).get("OP_NTC_PUPO_DT").equals("") )
                                OP_NTC_PUPO_DT =  "中華民國 " + DateUtil.getReportDateTime3( AnDl.get(0).get("OP_NTC_PUPO_DT").toString());
                            else
                                OP_NTC_PUPO_DT = "";
                            
                            OP_DOC_WD_NO = AnDl.get(0).get("OP_PUPO_DOC_WD") + "字 "+ AnDl.get(0).get("OP_PUPO_DOC_NO") +"號";
                        }
                        
                        dt_2.getRows().add( Announce.get(0).get("OP_AN_B_UNIT_NM"), basic.get(0).get("OP_AC_STAFF_NM"), basic.get(0).get("OP_AC_UNIT_TEL"),
                        basic.get(0).get("OP_PUPO_NAME"), OP_PUPO_GENDER, DateUtil.getReportDateTime3( basic.get(0).get("OP_PU_DATE").toString()), 
                        puojNm, OP_NTC_PUPO_DT, OP_DOC_WD_NO);
                        ds.getTables().add(dt_2);

                        sendToBrowserWord(reportPath.get(0), response, "拾得人領回公告.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //拾得物送交地方自治團體函
                    case "OP04A01Q.doc":
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);

                        //受理基本資料序號
                        if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                        }

                        data = ReportDao.printByOP04A01Q_DOC(dateConvertJson);
                        basic = (ArrayList) data.get("basicList");
                        Announce = (ArrayList) data.get("AnnounceList");
                        ds = new DataSet();
                        dt_2 = new DataTable("Footer");
                        dt_2.getColumns().add("OP_AN_UNIT_NM");
                        dt_2.getColumns().add("OP_PUPO_NAME");
                        dt_2.getColumns().add("OP_AN_DATE_BEG");
                        dt_2.getColumns().add("OP_AN_DATE_END");
                        dt_2.getColumns().add("OP_DOC_WD_NO");

                        if( Announce.isEmpty() ){
                            OP_AN_DATE_BEG = "";
                            OP_AN_DATE_END = "";
                            OP_DOC_WD_NO = "";
                        }else{
                            OP_AN_DATE_BEG = "中華民國 " + DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_BEG").toString());
                            OP_AN_DATE_END = DateUtil.getReportDateTime3(Announce.get(0).get("OP_AN_DATE_END").toString());
                            OP_DOC_WD_NO = Announce.get(0).get("OP_DOC_WD") + "字 "+ Announce.get(0).get("OP_DOC_NO") +"號";
                        }
                        dt_2.getRows().add( Announce.get(0).get("OP_AN_B_UNIT_NM"), basic.get(0).get("OP_PUPO_NAME"),OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD_NO);
                        ds.getTables().add(dt_2);

                        sendToBrowserWord(reportPath.get(0), response, "拾得物送交地方自治團體函.doc", serverTempPath, ds, argJsonObj, true, q);
                        response.flushBuffer();
                    break;
                    //拾得人逾期未領取拾得物清冊
                    case "OP04A02Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    
                    //受理基本資料序號
                    if (argJsonObj.has("OP_BASIC_SEQ_NO") && !argJsonObj.getString("OP_BASIC_SEQ_NO").equals("")) {
                        dateConvertJson.put("OP_BASIC_SEQ_NO", argJsonObj.getString("OP_BASIC_SEQ_NO"));
                    }
                    
                    list = ReportDao.printByOP04A02Q(dateConvertJson);
                    
                    sendToBrowserXls(reportPath.get(0), response, "拾得人逾期未領取拾得物清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    break;
                    //代保管財產價值新臺幣伍佰元以下拾得物收據
                    case "OP08A01Q.doc":
                        license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("代保管財產價值新臺幣伍佰元以下拾得物收據.doc", "UTF-8"));
                        //String[] reportType = obj.getString("reportType").split(",");
                        // 需增加才能讓successCallback有反應
//                        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        doc = new Document(reportPath.get(0));
                        doc2 = new Document(reportPath.get(1));
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                        
                        //受理基本資料序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }
                        
                        data = ReportDao.printByOP02A01Q_DOC(dateConvertJson);
                        crs = (CachedRowSet) data.get("BasicList");
                        crs.beforeFirst();
                        crs.next();
//                        dt = new DataTable(crs, "Footer");
                        detail = (ArrayList) data.get("detailList");
                        count = detail.size() / 5;
                        if( detail.size() % 5 > 0){
                            count++;
                        }
                        if( count == 0 ){
                            count = 1;
                        }
                        
                        for( int pageNum = 1; pageNum <= count; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                            dt_2 = new DataTable("Footer");
                            dt_2.getColumns().add("OP_AC_UNIT_NM");
                            dt_2.getColumns().add("OP_AC_RCNO");
                            dt_2.getColumns().add("OP_PUPO_NAME");
                            dt_2.getColumns().add("OP_ADDR");
                            dt_2.getColumns().add("OP_PUPO_PHONENO");
                            dt_2.getColumns().add("OP_PUPO_IDN");
                            dt_2.getColumns().add("OP_PU_PLACE");
                            dt_2.getColumns().add("OP_PU_DATE");

                            dt_2.getColumns().add("OP_PUOJ_NM1");
                            dt_2.getColumns().add("OP_QTY1");
                            dt_2.getColumns().add("OP_QTY_UNIT1");
                            dt_2.getColumns().add("OP_FEATURE1");
                            dt_2.getColumns().add("OP_PUOJ_NM2");
                            dt_2.getColumns().add("OP_QTY2");
                            dt_2.getColumns().add("OP_QTY_UNIT2");
                            dt_2.getColumns().add("OP_FEATURE2");
                            dt_2.getColumns().add("OP_PUOJ_NM3");
                            dt_2.getColumns().add("OP_QTY3");
                            dt_2.getColumns().add("OP_QTY_UNIT3");
                            dt_2.getColumns().add("OP_FEATURE3");
                            dt_2.getColumns().add("OP_PUOJ_NM4");
                            dt_2.getColumns().add("OP_QTY4");
                            dt_2.getColumns().add("OP_QTY_UNIT4");
                            dt_2.getColumns().add("OP_FEATURE4");
                            dt_2.getColumns().add("OP_PUOJ_NM5");
                            dt_2.getColumns().add("OP_QTY5");
                            dt_2.getColumns().add("OP_QTY_UNIT5");
                            dt_2.getColumns().add("OP_FEATURE5");
                            
                            dt_2.getColumns().add("YEAR");
                            dt_2.getColumns().add("MONTH");
                            dt_2.getColumns().add("DATE");
                            
                            DataRow dr = new DataRow(dt_2);
                            String[] tmp = new String[20];
                            Arrays.fill(tmp,"");
                            //DETAIL
                            for( int j = 0 ; j < 5; j++){ 
                                if ( (j+(pageNum-1)*5) == detail.size() ){
                                    break;
                                }
                                tmp[j*4+0] = detail.get(j+(pageNum-1)*5).get("OP_PUOJ_NM").toString();
                                String[] opQty = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString().split("\\.");
                                if( opQty[1].equals("00") ){
                                    tmp[j*4+1] = opQty[0];
                                }else{
                                    tmp[j*4+1] = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString();
                                }
                                tmp[j*4+2] = detail.get(j+(pageNum-1)*5).get("OP_QTY_UNIT").toString();
                                tmp[j*4+3] = detail.get(j+(pageNum-1)*5).get("OP_FEATURE").toString();
                            }
                            //BASIC
                            now = new java.util.Date();
                            String[] SplitYear = getNowTime( now ).split("/");
                            dt_2.getRows().add(crs.getString("OP_AC_UNIT_NM"),crs.getString("OP_AC_RCNO"),crs.getString("OP_PUPO_NAME"),
                                    crs.getString("OP_ADDR"),crs.getString("OP_PUPO_PHONENO"),crs.getString("OP_PUPO_IDN"),
                                    crs.getString("OP_PU_PLACE"),crs.getString("OP_PU_DATE"),tmp[0],tmp[1],tmp[2],tmp[3],tmp[4],tmp[5]
                            ,tmp[6],tmp[7],tmp[8],tmp[9],tmp[10],tmp[11],tmp[12],tmp[13],tmp[14],tmp[15],tmp[16],tmp[17],tmp[18],tmp[19],
                            SplitYear[0],SplitYear[1],SplitYear[2]);
                            
                            
                            ds.getTables().add(dt_2);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            //第二聯
                            doc2.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                                //第二聯
                                Document doc3 = new Document(reportPath.get(1));
                                doc3.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc3.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc3.getMailMerge().executeWithRegions(ds); 
                                doc2.appendDocument(doc3, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                                //第二聯
                                doc2.getMailMerge().executeWithRegions(ds);
                            }
                            
                        }
                        doc.appendDocument(doc2, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                        response.setContentType("application/msword");
                        
                        doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                        response.flushBuffer();
                    break;
                    
                    //未經認領及領回拾得物移交地方自治團體清冊
                    case "OP07A15Q.doc":
                        license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("未經認領及領回拾得物移交地方自治團體清冊.doc", "UTF-8"));
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson = new JSONObject();
                        dateConvertJson.put("userVO", user);
                        doc = new Document(reportPath.get(0));
                        doc2 = new Document(reportPath.get(1));
//                        //受理日期起迄
                        if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                            dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                            dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                        }
                        if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                            dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                            dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                        }
                        //拾得日期起迄
                        if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                            dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                            dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                        }
                        if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                            dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                            dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                        }
                        //通知拾得人領回日期起迄
                        if (argJsonObj.has("OP_NTC_PUPO_DT_S") && !argJsonObj.getString("OP_NTC_PUPO_DT_S").equals("")) {
                            dateConvertJson.put("OP_NTC_PUPO_DT_S", get8UsDateFormatDB(argJsonObj.getString("OP_NTC_PUPO_DT_S")));
                            dateConvertJson.put("OP_NTC_PUPO_DT_S_O", argJsonObj.getString("OP_NTC_PUPO_DT_S"));
                        }
                        if (argJsonObj.has("OP_NTC_PUPO_DT_E") && !argJsonObj.getString("OP_NTC_PUPO_DT_E").equals("")) {
                            dateConvertJson.put("OP_NTC_PUPO_DT_E", get8UsDateFormatDB(argJsonObj.getString("OP_NTC_PUPO_DT_E")));
                            dateConvertJson.put("OP_NTC_PUPO_DT_E_O", argJsonObj.getString("OP_NTC_PUPO_DT_E"));
                        }
                        //單位代碼
                        if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                            dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                        }
                        if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                            dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                        }
                        if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                            dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                        }
                        //含下屬
                        if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                            dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                        }
                        //判斷物品/現金/全部
                        if (argJsonObj.has("OP_TYPE_CD") && argJsonObj.getString("OP_TYPE_CD").equals("1")) {
                        dateConvertJson.put("OP_TYPE_CD",argJsonObj.getString("OP_TYPE_CD"));
                        }
                        if (argJsonObj.has("OP_TYPE_CD") && argJsonObj.getString("OP_TYPE_CD").equals("0")) {
                        dateConvertJson.put("OP_TYPE_CD",argJsonObj.getString("OP_TYPE_CD"));
                        }
                       data = ReportDao.printByOP07A15Q_DOC(dateConvertJson);
                       crs = (CachedRowSet) data.get("BasicList");
                    if ( crs.size() > 0 ) {
                        flagData = true;
                    }
                    for( int pageNum = 1; pageNum <= 1; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                    dt = new DataTable(crs, "Footer");
                    ds.getTables().add(dt);
                    crs =  ReportDao.printUnitHeard2(dateConvertJson.getJSONObject("newObj"));
                    dt = new DataTable(crs, "Data1");
                    ds.getTables().add(dt);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            //第二聯
                            doc2.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                                //第二聯
                                Document doc3 = new Document(reportPath.get(1));
                                doc3.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc3.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc3.getMailMerge().executeWithRegions(ds); 
                                doc2.appendDocument(doc3, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                                //第二聯
                                doc2.getMailMerge().executeWithRegions(ds);
                            }
                    }
                    doc.appendDocument(doc2, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                    response.setContentType("application/msword");
                    doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                    response.flushBuffer();
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A15Q_01", user, "", "", "");
                    
                    break;
                    //拾得人讓與拾得物所有權收據
                    case "OP08A03Q.doc":
                        license = new com.aspose.words.License();
                        license.setLicense("Aspose.Total.Java.lic");
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("拾得人讓與拾得物所有權收據.doc", "UTF-8"));
                        //String[] reportType = obj.getString("reportType").split(",");
                        // 需增加才能讓successCallback有反應
//                        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
                        doc = new Document(reportPath.get(0));
                        doc2 = new Document(reportPath.get(1));
                        dateConvertJson = new JSONObject();
                        ReportDao = ReportDao.getInstance();
                        dateConvertJson.put("userVO", user);
                        
                        //受理基本資料序號
                        if (argJsonObj.has("OP_SEQ_NO") && !argJsonObj.getString("OP_SEQ_NO").equals("")) {
                            dateConvertJson.put("OP_SEQ_NO", argJsonObj.getString("OP_SEQ_NO"));
                        }
                        
                        data = ReportDao.printByOP02A01Q_DOC(dateConvertJson);
                        crs = (CachedRowSet) data.get("BasicList");
                        crs.beforeFirst();
                        crs.next();
//                        dt = new DataTable(crs, "Footer");
                        detail = (ArrayList) data.get("detailList");
                        count = detail.size() / 5;
                        if( detail.size() % 5 > 0){
                            count++;
                        }
                        if( count == 0 ){
                            count = 1;
                        }
                        
                        for( int pageNum = 1; pageNum <= count; pageNum++){ //先決定頁數
                            if(pageNum >1){
                                ds = new DataSet();
                            }
                            dt_2 = new DataTable("Footer");
                            dt_2.getColumns().add("OP_AC_UNIT_NM");
                            dt_2.getColumns().add("OP_AC_RCNO");
                            dt_2.getColumns().add("OP_PUPO_NAME");
                            dt_2.getColumns().add("OP_ADDR");
                            dt_2.getColumns().add("OP_PUPO_PHONENO");
                            dt_2.getColumns().add("OP_PUPO_IDN");
                            dt_2.getColumns().add("OP_PU_PLACE");
                            dt_2.getColumns().add("OP_PU_DATE");

                            dt_2.getColumns().add("OP_PUOJ_NM1");
                            dt_2.getColumns().add("OP_QTY1");
                            dt_2.getColumns().add("OP_QTY_UNIT1");
                            dt_2.getColumns().add("OP_FEATURE1");
                            dt_2.getColumns().add("OP_PUOJ_NM2");
                            dt_2.getColumns().add("OP_QTY2");
                            dt_2.getColumns().add("OP_QTY_UNIT2");
                            dt_2.getColumns().add("OP_FEATURE2");
                            dt_2.getColumns().add("OP_PUOJ_NM3");
                            dt_2.getColumns().add("OP_QTY3");
                            dt_2.getColumns().add("OP_QTY_UNIT3");
                            dt_2.getColumns().add("OP_FEATURE3");
                            dt_2.getColumns().add("OP_PUOJ_NM4");
                            dt_2.getColumns().add("OP_QTY4");
                            dt_2.getColumns().add("OP_QTY_UNIT4");
                            dt_2.getColumns().add("OP_FEATURE4");
                            dt_2.getColumns().add("OP_PUOJ_NM5");
                            dt_2.getColumns().add("OP_QTY5");
                            dt_2.getColumns().add("OP_QTY_UNIT5");
                            dt_2.getColumns().add("OP_FEATURE5");
                            
                            dt_2.getColumns().add("YEAR");
                            dt_2.getColumns().add("MONTH");
                            dt_2.getColumns().add("DATE");
                            
                            DataRow dr = new DataRow(dt_2);
                            String[] tmp = new String[20];
                            Arrays.fill(tmp,"");
                            //DETAIL
                            for( int j = 0 ; j < 5; j++){ 
                                if ( (j+(pageNum-1)*5) == detail.size() ){
                                    break;
                                }
                                tmp[j*4+0] = detail.get(j+(pageNum-1)*5).get("OP_PUOJ_NM").toString();
                                String[] opQty = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString().split("\\.");
                                if( opQty[1].equals("00") ){
                                    tmp[j*4+1] = opQty[0];
                                }else{
                                    tmp[j*4+1] = detail.get(j+(pageNum-1)*5).get("OP_QTY").toString();
                                }
                                tmp[j*4+2] = detail.get(j+(pageNum-1)*5).get("OP_QTY_UNIT").toString();
                                tmp[j*4+3] = detail.get(j+(pageNum-1)*5).get("OP_FEATURE").toString();
                            }
                            //BASIC
                            now = new java.util.Date();
                            String[] SplitYear = getNowTime( now ).split("/");
                            dt_2.getRows().add(crs.getString("OP_AC_UNIT_NM"),crs.getString("OP_AC_RCNO"),crs.getString("OP_PUPO_NAME"),
                                    crs.getString("OP_ADDR"),crs.getString("OP_PUPO_PHONENO"),crs.getString("OP_PUPO_IDN"),
                                    crs.getString("OP_PU_PLACE"),crs.getString("OP_PU_DATE"),tmp[0],tmp[1],tmp[2],tmp[3],tmp[4],tmp[5]
                            ,tmp[6],tmp[7],tmp[8],tmp[9],tmp[10],tmp[11],tmp[12],tmp[13],tmp[14],tmp[15],tmp[16],tmp[17],tmp[18],tmp[19],
                            SplitYear[0],SplitYear[1],SplitYear[2]);
                            
                            
                            ds.getTables().add(dt_2);
                            //第一聯
                            doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            //第二聯
                            doc2.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                            
                            if(pageNum >1){
                                //第一聯
                                Document doc1 = new Document(reportPath.get(0));
                                doc1.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc1.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc1.getMailMerge().executeWithRegions(ds); 
                                doc.appendDocument(doc1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                                //第二聯
                                Document doc3 = new Document(reportPath.get(1));
                                doc3.getFirstSection().getPageSetup().setRestartPageNumbering(true); 
                                doc3.getFirstSection().getPageSetup().setPageStartingNumber(1 + pageNum);
                                doc3.getMailMerge().executeWithRegions(ds); 
                                doc2.appendDocument(doc3, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                            } else {
                                //第一聯
                                doc.getMailMerge().executeWithRegions(ds);
                                //第二聯
                                doc2.getMailMerge().executeWithRegions(ds);
                            }
                            
                        }
                        doc.appendDocument(doc2, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                        response.setContentType("application/msword");
                        
                        doc.save(response.getOutputStream(), com.aspose.words.SaveFormat.DOC);
                        response.flushBuffer();
                    break;
                case "OP07A02Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //收據編號
                    if (argJsonObj.has("OP_AC_RCNO") && !argJsonObj.getString("OP_AC_RCNO").equals("")) {
                        dateConvertJson.put("OP_AC_RCNO",argJsonObj.getString("OP_AC_RCNO"));
                    }
                    list = ReportDao.printByOP07A02Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "拾得物拍變賣清冊列印.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A02Q_01", user, "", "", "");
                    
                    break;
                
                case "OP07A13Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //發還日期起迄
                    if (argJsonObj.has("OP_RT_DATE_S") && !argJsonObj.getString("OP_RT_DATE_S").equals("")) {
                        dateConvertJson.put("OP_RT_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_RT_DATE_S")));
                        dateConvertJson.put("OP_RT_DATE_S_O", argJsonObj.getString("OP_RT_DATE_S"));
                    }
                    if (argJsonObj.has("OP_RT_DATE_E") && !argJsonObj.getString("OP_RT_DATE_E").equals("")) {
                        dateConvertJson.put("OP_RT_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_RT_DATE_E")));
                        dateConvertJson.put("OP_RT_DATE_E_O", argJsonObj.getString("OP_RT_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    //拾得物總價值
                    if (argJsonObj.has("OP_PU_YN_OV500") && !argJsonObj.getString("OP_PU_YN_OV500").equals("")) {
                        dateConvertJson.put("OP_PU_YN_OV500",argJsonObj.getString("OP_PU_YN_OV500"));
                    }
                    list = ReportDao.printByOP07A13Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "受理拾得物發還清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A13Q_01", user, "", "", "");
                    
                    break;
                
                case "OP07A14Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //發還日期起迄
                    if (argJsonObj.has("OP_RT_DATE_S") && !argJsonObj.getString("OP_RT_DATE_S").equals("")) {
                        dateConvertJson.put("OP_RT_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_RT_DATE_S")));
                        dateConvertJson.put("OP_RT_DATE_S_O", argJsonObj.getString("OP_RT_DATE_S"));
                    }
                    if (argJsonObj.has("OP_RT_DATE_E") && !argJsonObj.getString("OP_RT_DATE_E").equals("")) {
                        dateConvertJson.put("OP_RT_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_RT_DATE_E")));
                        dateConvertJson.put("OP_RT_DATE_E_O", argJsonObj.getString("OP_RT_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    //拾得物總價值
                    if (argJsonObj.has("OP_PU_YN_OV500") && !argJsonObj.getString("OP_PU_YN_OV500").equals("")) {
                        dateConvertJson.put("OP_PU_YN_OV500",argJsonObj.getString("OP_PU_YN_OV500"));
                    }
                    list = ReportDao.printByOP07A14Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    sendToBrowserXls(reportPath.get(0), response, "拾得公告期滿拾得人(或委其親友)領回清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A14Q_01", user, "", "", "");
                    
                    break;
               
                case "OP07A03Q.doc":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);

                    if (argJsonObj.has("OP_AN_DATE_BEG") && !argJsonObj.getString("OP_AN_DATE_BEG").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_BEG", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_BEG")));
                        dateConvertJson.put("OP_AN_DATE_BEG_O", argJsonObj.getString("OP_AN_DATE_BEG"));
                    }
                    if (argJsonObj.has("OP_AN_DATE_END") && !argJsonObj.getString("OP_AN_DATE_END").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END")));
                        dateConvertJson.put("OP_AN_DATE_END_O", argJsonObj.getString("OP_AN_DATE_END"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                      if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    if (argJsonObj.has("OP_AC_RCNO") && !argJsonObj.getString("OP_AC_RCNO").equals("")) {
                        dateConvertJson.put("OP_AC_RCNO",argJsonObj.getString("OP_AC_RCNO"));
                    }
                    
                    crs = ReportDao.printByOP07A03Q_DOC(dateConvertJson);
                    if ( crs.size() > 0 ) {
                        flagData = true;
                    }
                    
                    dt = new DataTable(crs, "Footer");
                    ds.getTables().add(dt);
                    sendToBrowserPDF(reportPath.get(0), response, "拾得物公告招領公告單列印.pdf", serverTempPath, ds, argJsonObj, true, q);
                    response.flushBuffer();
                    
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A03Q_01", user, "", "", "");
                    
                    
                    break;
                case "OP07A04Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //有無證件	
                    if (argJsonObj.has("OP_CERT_YN") && !argJsonObj.getString("OP_CERT_YN").equals("")) {
                        dateConvertJson.put("OP_CERT_YN",argJsonObj.getString("OP_CERT_YN"));
                    }
                    //受理員警 OP_AC_STAFF_NM
                    if (argJsonObj.has("OP_AC_STAFF_NM") && !argJsonObj.getString("OP_AC_STAFF_NM").equals("")) {
                        dateConvertJson.put("OP_AC_STAFF_NM",argJsonObj.getString("OP_AC_STAFF_NM"));
                    }
                    list = ReportDao.printByOP07A04Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "拾得物公告招領清冊列印.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A04Q_01", user, "", "", "");
                    
                    
                    break;   
                case "OP07A06Q.doc":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);

                    if (argJsonObj.has("OP_NTC_PUPO_DT_S") && !argJsonObj.getString("OP_NTC_PUPO_DT_S").equals("")) {
                        dateConvertJson.put("OP_NTC_PUPO_DT_S", get8UsDateFormatDB(argJsonObj.getString("OP_NTC_PUPO_DT_S")));
                        dateConvertJson.put("OP_NTC_PUPO_DT_S_O", argJsonObj.getString("OP_NTC_PUPO_DT_S"));
                    }
                    if (argJsonObj.has("OP_NTC_PUPO_DT_E") && !argJsonObj.getString("OP_NTC_PUPO_DT_E").equals("")) {
                        dateConvertJson.put("OP_NTC_PUPO_DT_E", get8UsDateFormatDB(argJsonObj.getString("OP_NTC_PUPO_DT_E")));
                        dateConvertJson.put("OP_NTC_PUPO_DT_E_O", argJsonObj.getString("OP_NTC_PUPO_DT_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    
                     //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    if (argJsonObj.has("OP_AC_RCNO") && !argJsonObj.getString("OP_AC_RCNO").equals("")) {
                        dateConvertJson.put("OP_AC_RCNO",argJsonObj.getString("OP_AC_RCNO"));
                    }
                    
                    crs = ReportDao.printByOP07A06Q_DOC(dateConvertJson);
                    if ( crs.size() > 0 ) {
                        flagData = true;
                    }
                    dt = new DataTable(crs, "Footer");
                    ds.getTables().add(dt);
                    
//                    sendToBrowserWord(reportPath.get(0), response, "拾得物登記簿列印.doc", serverTempPath, ds, argJsonObj, true, q);
                    sendToBrowserPDF(reportPath.get(0), response, "拾得人領回公告單.pdf", serverTempPath, ds, argJsonObj, true, q);
                    response.flushBuffer();
                    
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                                    //查有資料
                                    logResultObj.put("DataResult", "查有資料");
                    } else {
                                    logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A06Q_01", user, "", "", "");

                    
                    break;    
                case "OP07A11Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //公告日期起迄
                    if (argJsonObj.has("OP_AN_DATE_END_S") && !argJsonObj.getString("OP_AN_DATE_END_S").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END_S", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END_S")));
                        dateConvertJson.put("OP_AN_DATE_END_S_O", argJsonObj.getString("OP_AN_DATE_END_S"));
                    }
                    if (argJsonObj.has("OP_AN_DATE_END_E") && !argJsonObj.getString("OP_AN_DATE_END_E").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END_E", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END_E")));
                        dateConvertJson.put("OP_AN_DATE_END_E_O", argJsonObj.getString("OP_AN_DATE_END_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //結案日期起迄
                    if (argJsonObj.has("OP_FS_DATE_S") && !argJsonObj.getString("OP_FS_DATE_S").equals("")) {
                        dateConvertJson.put("OP_FS_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_FS_DATE_S")));
                        dateConvertJson.put("OP_FS_DATE_S_O", argJsonObj.getString("OP_FS_DATE_S"));
                    }
                    if (argJsonObj.has("OP_FS_DATE_E") && !argJsonObj.getString("OP_FS_DATE_E").equals("")) {
                        dateConvertJson.put("OP_FS_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_FS_DATE_E")));
                        dateConvertJson.put("OP_FS_DATE_E_O", argJsonObj.getString("OP_FS_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //姓名地址電話
                    if (argJsonObj.has("printPUCP_NM_YN") && !argJsonObj.getString("printPUCP_NM_YN").equals("")) {
                        dateConvertJson.put("printPUCP_NM_YN",argJsonObj.getString("printPUCP_NM_YN"));
                    }
                    if (argJsonObj.has("printPUCP_IDN_YN") && !argJsonObj.getString("printPUCP_IDN_YN").equals("")) {
                        dateConvertJson.put("printPUCP_IDN_YN",argJsonObj.getString("printPUCP_IDN_YN"));
                    }
                    if (argJsonObj.has("printPUCP_ADDR_YN") && !argJsonObj.getString("printPUCP_ADDR_YN").equals("")) {
                        dateConvertJson.put("printPUCP_ADDR_YN",argJsonObj.getString("printPUCP_ADDR_YN"));
                    }
                    if (argJsonObj.has("printPUCP_TEL_YN") && !argJsonObj.getString("printPUCP_TEL_YN").equals("")) {
                        dateConvertJson.put("printPUCP_TEL_YN",argJsonObj.getString("printPUCP_TEL_YN"));
                    }
                    list = ReportDao.printByOP07A11Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "遺失人領回清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A01Q_01", user, "", "", "");

                    break;  
                case "OP07A12Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //公告日期起迄
                    if (argJsonObj.has("OP_AN_DATE_END_S") && !argJsonObj.getString("OP_AN_DATE_END_S").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END_S", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END_S")));
                        dateConvertJson.put("OP_AN_DATE_END_S_O", argJsonObj.getString("OP_AN_DATE_END_S"));
                    }
                    if (argJsonObj.has("OP_AN_DATE_END_E") && !argJsonObj.getString("OP_AN_DATE_END_E").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END_E", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END_E")));
                        dateConvertJson.put("OP_AN_DATE_END_E_O", argJsonObj.getString("OP_AN_DATE_END_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //姓名地址電話
                    if (argJsonObj.has("printPUPO_NM_YN") && !argJsonObj.getString("printPUPO_NM_YN").equals("")) {
                        dateConvertJson.put("printPUPO_NM_YN",argJsonObj.getString("printPUPO_NM_YN"));
                    }
                    if (argJsonObj.has("printPUPO_ADDR_YN") && !argJsonObj.getString("printPUPO_ADDR_YN").equals("")) {
                        dateConvertJson.put("printPUPO_ADDR_YN",argJsonObj.getString("printPUPO_ADDR_YN"));
                    }
                    if (argJsonObj.has("printPUPO_TEL_YN") && !argJsonObj.getString("printPUPO_TEL_YN").equals("")) {
                        dateConvertJson.put("printPUPO_TEL_YN",argJsonObj.getString("printPUPO_TEL_YN"));
                    }
                    
                    list = ReportDao.printByOP07A12Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "期滿遺失物拾得人清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A01Q_01", user, "", "", "");

                    break;
                case "OP07A10Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //收據編號
                    if (argJsonObj.has("OP_AC_RCNO") && !argJsonObj.getString("OP_AC_RCNO").equals("")) {
                        dateConvertJson.put("OP_AC_RCNO",argJsonObj.getString("OP_AC_RCNO"));
                    }
                    list = ReportDao.printByOP07A10Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    
                    sendToBrowserXls(reportPath.get(0), response, "拾得人地址列印.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer(); 
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                        //查有資料
                        logResultObj.put("DataResult", "查有資料");
                    } else {
                        logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A10Q_01", user, "", "", "");

                    break;
                    case "OP07A07Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //收據編號
                    if (argJsonObj.has("OP_AC_RCNO") && !argJsonObj.getString("OP_AC_RCNO").equals("")) {
                        dateConvertJson.put("OP_AC_RCNO",argJsonObj.getString("OP_AC_RCNO"));
                    }
                    list = ReportDao.printByOP07A07Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    sendToBrowserXls(reportPath.get(0), response, "拾得人逾期未領取拾得物清冊列印.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                                    //查有資料
                                    logResultObj.put("DataResult", "查有資料");
                    } else {
                                    logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A07Q_01", user, "", "", "");

                    break;  
                case "OP07A0501Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //公告期滿日期起迄
                    if (argJsonObj.has("OP_AN_DATE") && !argJsonObj.getString("OP_AN_DATE").equals("")) {
                        dateConvertJson.put("OP_AN_DATE", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE")));
                        dateConvertJson.put("OP_AN_DATE_O", argJsonObj.getString("OP_AN_DATE"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //列印類型
                    if (argJsonObj.has("PRINT_TYPE") && !argJsonObj.getString("PRINT_TYPE").equals("")) {
                        dateConvertJson.put("PRINT_TYPE",argJsonObj.getString("PRINT_TYPE"));
                    }
                    
                    
                    list = ReportDao.printByOP07A0501Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    sendToBrowserXls(reportPath.get(0), response, "拾得遺失物公告期滿案件清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                            //查有資料
                            logResultObj.put("DataResult", "查有資料");
                    } else {
                            logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A05Q_01", user, "", "", "");

                    
                    break;
                case "OP07A0502Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //公告日期起迄
                    if (argJsonObj.has("OP_AN_DATE_END_S") && !argJsonObj.getString("OP_AN_DATE_END_S").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END_S", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END_S")));
                        dateConvertJson.put("OP_AN_DATE_END_S_O", argJsonObj.getString("OP_AN_DATE_END_S"));
                    }
                    if (argJsonObj.has("OP_AN_DATE_END_E") && !argJsonObj.getString("OP_AN_DATE_END_E").equals("")) {
                        dateConvertJson.put("OP_AN_DATE_END_E", get8UsDateFormatDB(argJsonObj.getString("OP_AN_DATE_END_E")));
                        dateConvertJson.put("OP_AN_DATE_END_E_O", argJsonObj.getString("OP_AN_DATE_END_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //列印類型
                    if (argJsonObj.has("PRINT_TYPE") && !argJsonObj.getString("PRINT_TYPE").equals("")) {
                        dateConvertJson.put("PRINT_TYPE",argJsonObj.getString("PRINT_TYPE"));
                    }
                    
                    
                    list = ReportDao.printByOP07A0502Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    sendToBrowserXls(reportPath.get(0), response, "拾得遺失物公告期滿案件清冊.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                                    //查有資料
                                    logResultObj.put("DataResult", "查有資料");
                    } else {
                                    logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A05Q_01", user, "", "", "");

                    break;    
                case "OP07A08Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    
                    //拾得日期起迄
                    if (argJsonObj.has("OP_PU_DATE_S") && !argJsonObj.getString("OP_PU_DATE_S").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_S")));
                        dateConvertJson.put("OP_PU_DATE_S_O", argJsonObj.getString("OP_PU_DATE_S"));
                    }
                    if (argJsonObj.has("OP_PU_DATE_E") && !argJsonObj.getString("OP_PU_DATE_E").equals("")) {
                        dateConvertJson.put("OP_PU_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_PU_DATE_E")));
                        dateConvertJson.put("OP_PU_DATE_E_O", argJsonObj.getString("OP_PU_DATE_E"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    //結案日期起迄
                    if (argJsonObj.has("OP_FS_DATE_S") && !argJsonObj.getString("OP_FS_DATE_S").equals("")) {
                        dateConvertJson.put("OP_FS_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_FS_DATE_S")));
                        dateConvertJson.put("OP_FS_DATE_S_O", argJsonObj.getString("OP_FS_DATE_S"));
                    }
                    if (argJsonObj.has("OP_FS_DATE_E") && !argJsonObj.getString("OP_FS_DATE_E").equals("")) {
                        dateConvertJson.put("OP_FS_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_FS_DATE_E")));
                        dateConvertJson.put("OP_FS_DATE_E_O", argJsonObj.getString("OP_FS_DATE_E"));
                    }
                    
                    list = ReportDao.printByOP07A08Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    sendToBrowserXls(reportPath.get(0), response, "拾得遺失物處理情形統計表.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                                    //查有資料
                                    logResultObj.put("DataResult", "查有資料");
                    } else {
                                    logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A08Q_01", user, "", "", "");

                    break;
                case "OP07A09Q.xls":
                    dateConvertJson = new JSONObject();
                    ReportDao = ReportDao.getInstance();
                    dateConvertJson.put("userVO", user);
                    //單位代碼
                    if (argJsonObj.has("OP_AC_D_UNIT_CD") && !argJsonObj.getString("OP_AC_D_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_D_UNIT_CD", argJsonObj.getString("OP_AC_D_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_B_UNIT_CD") && !argJsonObj.getString("OP_AC_B_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_B_UNIT_CD", argJsonObj.getString("OP_AC_B_UNIT_CD"));
                    }
                    if (argJsonObj.has("OP_AC_UNIT_CD") && !argJsonObj.getString("OP_AC_UNIT_CD").equals("")) {
                        dateConvertJson.put("OP_AC_UNIT_CD", argJsonObj.getString("OP_AC_UNIT_CD"));
                    }
                    //含下屬
                    if (argJsonObj.has("includeYN") && !argJsonObj.getString("includeYN").equals("")) {
                        dateConvertJson.put("includeYN",argJsonObj.getString("includeYN"));
                    }
                    //設定要列印之已受理未公告超過天/月數
                    if (argJsonObj.has("OP_AC_NO_AN") && !argJsonObj.getString("OP_AC_NO_AN").equals("")) {
                        dateConvertJson.put("OP_AC_NO_AN",argJsonObj.getString("OP_AC_NO_AN"));
                    }
                    if (argJsonObj.has("OP_AC_NO_AN_CMB") && !argJsonObj.getString("OP_AC_NO_AN_CMB").equals("")) {
                        dateConvertJson.put("OP_AC_NO_AN_CMB",argJsonObj.getString("OP_AC_NO_AN_CMB"));
                    }
                    //設定要列印之公告期滿未結案超過天/月數
                    if (argJsonObj.has("OP_AN_NO_CLOSE") && !argJsonObj.getString("OP_AN_NO_CLOSE").equals("")) {
                        dateConvertJson.put("OP_AN_NO_CLOSE",argJsonObj.getString("OP_AN_NO_CLOSE"));
                    }
                    if (argJsonObj.has("OP_AN_NO_CLOSE_CMB") && !argJsonObj.getString("OP_AN_NO_CLOSE_CMB").equals("")) {
                        dateConvertJson.put("OP_AN_NO_CLOSE_CMB",argJsonObj.getString("OP_AN_NO_CLOSE_CMB"));
                    }
                    //受理日期起迄
                    if (argJsonObj.has("OP_AC_DATE_S") && !argJsonObj.getString("OP_AC_DATE_S").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_S", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_S")));
                        dateConvertJson.put("OP_AC_DATE_S_O", argJsonObj.getString("OP_AC_DATE_S"));
                    }
                    if (argJsonObj.has("OP_AC_DATE_E") && !argJsonObj.getString("OP_AC_DATE_E").equals("")) {
                        dateConvertJson.put("OP_AC_DATE_E", get8UsDateFormatDB(argJsonObj.getString("OP_AC_DATE_E")));
                        dateConvertJson.put("OP_AC_DATE_E_O", argJsonObj.getString("OP_AC_DATE_E"));
                    }
                    list = ReportDao.printByOP07A09Q(dateConvertJson);
                    if ( list.size() > 0 && dateConvertJson.getString("IS_HAVE").equals("Y") ) { //list 大於0 不一定沒有資料 多判斷 IS_HAVE
                        flagData = true;
                    }
                    sendToBrowserXls(reportPath.get(0), response, "拾得遺失物公告件數統計表.xlsx", serverTempPath, list, dateConvertJson.getJSONObject("newObj"));
                    response.flushBuffer();
                    //記日誌
                    logResult = new JSONArray();
                    logResultObj = new JSONObject();
                    if (flagData) {
                                    //查有資料
                                    logResultObj.put("DataResult", "查有資料");
                    } else {
                                    logResultObj.put("DataResult", "查無資料");
                    }
                    logResult.put(logResultObj);
                    logCols = new LinkedHashMap<String, String>();
                    logCols.put("DataResult", "查詢結果");

                    NPALOG2 = new NPALOG2Util();
                    NPALOG2.WriteQueryLog(NPALOG2Util.LOGOPTYPE.T, GetQryCondition(ajaxAction[q], argJsonObj), "", "", "", logResult, logCols, "OP07A09Q_01", user, "", "", "");

                    break;
            }
        }
    }
    
    /**
    * 傳105/09/10－>105年9月10日
    * @param date
    * @return
    */
    public static String getNowTime( Date inputDate ) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String date = sdf.format(inputDate);
                
    		String[] dateString = date.split("/");
    		Integer tempYear = Integer.valueOf(dateString[0])-1911;
    		String Year = String.valueOf(tempYear);
    		String month = dateString[1];
    		String day = dateString[2];
    		String fullTime = Year+"/"+month+"/"+day;
    		return fullTime;
    }
    
    //查詢條件欄位
    private String GetQryCondition(String action, JSONObject jsonObject) throws SQLException {
        StringBuilder returnString = new StringBuilder();
        LinkedHashMap logCols = new LinkedHashMap<String, String>();
        String unitName = null;
        OPDT_E0_NPAUNITDao npaunitDao = OPDT_E0_NPAUNITDao.getInstance();
        switch (action) {
            //拾得物登記簿列印
            case "OP07A01Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PUOJ_ATTR_CD").equals("")){
                    if( jsonObject.getString("OP_PUOJ_ATTR_CD").equals("0") )
                        returnString.append(String.format("&%s=%s", "物品屬性", "不知悉遺失物所有人"));
                    else
                        returnString.append(String.format("&%s=%s", "物品屬性", "知悉遺失物所有人"));
                }
                if (!jsonObject.getString("OP_PU_YN_OV500").equals("")){
                    if( jsonObject.getString("OP_PU_YN_OV500").equals("0") )
                        returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元(含)以下"));
                    else
                        returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元以上"));
                }
            break;
            case "OP07A01Q.doc":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PUOJ_ATTR_CD").equals("")){
                    if( jsonObject.getString("OP_PUOJ_ATTR_CD").equals("0") )
                        returnString.append(String.format("&%s=%s", "物品屬性", "不知悉遺失物所有人"));
                    else
                        returnString.append(String.format("&%s=%s", "物品屬性", "知悉遺失物所有人"));
                }
                if (!jsonObject.getString("OP_PU_YN_OV500").equals("")){
                    if( jsonObject.getString("OP_PU_YN_OV500").equals("0") )
                        returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元(含)以下"));
                    else
                        returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元以上"));
                }
            break;
            //拾得物拍(變)賣清冊列印
            case "OP07A02Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_RCNO").equals("")){
                        returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
                }
            break;
            //拾得物公告招領公告單列印
            case "OP07A03Q.doc":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_AN_DATE_BEG").equals("")){
                        returnString.append(String.format("&%s=%s", "公告日期起日", jsonObject.getString("OP_AN_DATE_BEG").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END").equals("")){
                        returnString.append(String.format("&%s=%s", "公告日期迄日", jsonObject.getString("OP_AN_DATE_END").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_RCNO").equals("")){
                        returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
                }
            break;
            //拾得物公告招領清冊列印
            case "OP07A04Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_STAFF_NM").equals("")){
                        returnString.append(String.format("&%s=%s", "受理員警", jsonObject.getString("OP_AC_STAFF_NM")));
                }
                if (!jsonObject.getString("OP_CERT_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "證件", jsonObject.getString("OP_CERT_YN")));
                }
            break;
            //拾得遺失物公告期滿案件清冊列印
            case "OP07A0501Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_AN_DATE").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期", jsonObject.getString("OP_AN_DATE").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("PRINT_TYPE").equals("")){
                    if( jsonObject.getString("PRINT_TYPE").equals("1") ){
                        returnString.append(String.format("&%s=%s", "列印清冊", "已屆公告期滿日(含)但仍在公告中案件清冊"));
                    }else if( jsonObject.getString("PRINT_TYPE").equals("2") ){
                        returnString.append(String.format("&%s=%s", "列印清冊", "已公告期滿逾3個月案件清冊"));
                    }else if( jsonObject.getString("PRINT_TYPE").equals("3") ){
                        returnString.append(String.format("&%s=%s", "列印清冊", "已公告期滿但未逾(含)三個月案件清冊"));
                    }
                }
            break;
            case "OP07A0502Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END_S").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期起日", jsonObject.getString("OP_AN_DATE_END_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END_E").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期迄日", jsonObject.getString("OP_AN_DATE_END_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("PRINT_TYPE").equals("")){
                    if( jsonObject.getString("PRINT_TYPE").equals("4") ){
                        returnString.append(String.format("&%s=%s", "列印清冊", "期滿遺失物公告清冊"));
                    }
                }
            break;
            //拾得人領回公告單列印
            case "OP07A06Q.doc":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_NTC_PUPO_DT_S").equals("")){
                        returnString.append(String.format("&%s=%s", "通知拾得人領回日期起日", jsonObject.getString("OP_NTC_PUPO_DT_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_NTC_PUPO_DT_E").equals("")){
                        returnString.append(String.format("&%s=%s", "通知拾得人領回日期迄日", jsonObject.getString("OP_NTC_PUPO_DT_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_RCNO").equals("")){
                        returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
                }
            break;
            //拾得人逾期未領取拾得物清冊列印
            case "OP07A07Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_RCNO").equals("")){
                        returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
                }
            break;
            // 拾得遺失物處理情形統計表列印
            case "OP07A08Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_FS_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "結案日期起日", jsonObject.getString("OP_FS_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_FS_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "結案日期迄日", jsonObject.getString("OP_FS_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
            break;
            // 拾得遺失物公告件數統計表列印
            case "OP07A09Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_AC_NO_AN").equals("")){
                        returnString.append(String.format("&%s=%s", "設定要列印之已受理未公告超過天月數", jsonObject.getString("OP_AC_NO_AN")));
                }
                if (!jsonObject.getString("OP_AC_NO_AN_CMB").equals("")){
                    if( jsonObject.getString("OP_AC_NO_AN_CMB").equals("1") )
                        returnString.append(String.format("&%s=%s", "設定要列印之已受理未公告超過天月數", "天"));
                    else if ( jsonObject.getString("OP_AC_NO_AN_CMB").equals("2") )
                        returnString.append(String.format("&%s=%s", "設定要列印之已受理未公告超過天月數", "月"));
                }
                if (!jsonObject.getString("OP_AN_NO_CLOSE").equals("")){
                        returnString.append(String.format("&%s=%s", "設定要列印之公告期滿未結案超過天/月數", jsonObject.getString("OP_AN_NO_CLOSE")));
                }
                if (!jsonObject.getString("OP_AN_NO_CLOSE_CMB").equals("")){
                    if( jsonObject.getString("OP_AN_NO_CLOSE_CMB").equals("1") )
                        returnString.append(String.format("&%s=%s", "設定要列印之公告期滿未結案超過天/月數", "天"));
                    else if ( jsonObject.getString("OP_AN_NO_CLOSE_CMB").equals("2") )
                        returnString.append(String.format("&%s=%s", "設定要列印之公告期滿未結案超過天/月數", "月"));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
            break;
            //拾得人地址列印
            case "OP07A10Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_RCNO").equals("")){
                        returnString.append(String.format("&%s=%s", "收據編號", jsonObject.getString("OP_AC_RCNO")));
                }
            break;
            //遺失人領回清冊
            case "OP07A11Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END_S").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期起日", jsonObject.getString("OP_AN_DATE_END_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END_E").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期迄日", jsonObject.getString("OP_AN_DATE_END_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_FS_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "結案日期起日", jsonObject.getString("OP_FS_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_FS_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "結案日期迄日", jsonObject.getString("OP_FS_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("printPUCP_NM_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "含", jsonObject.getString("printPUCP_NM_YN")));
                }
                if (!jsonObject.getString("printPUCP_IDN_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "證號", jsonObject.getString("printPUCP_IDN_YN")));
                }
                if (!jsonObject.getString("printPUCP_ADDR_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "地址", jsonObject.getString("printPUCP_ADDR_YN")));
                }
                if (!jsonObject.getString("printPUCP_TEL_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "電話", jsonObject.getString("printPUCP_TEL_YN")));
                }
            break;
            //期滿遺失物拾得人清冊
            case "OP07A12Q.xls":
                ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END_S").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期起日", jsonObject.getString("OP_AN_DATE_END_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AN_DATE_END_E").equals("")){
                        returnString.append(String.format("&%s=%s", "公告期滿日期迄日", jsonObject.getString("OP_AN_DATE_END_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("printPUPO_NM_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "含", jsonObject.getString("printPUPO_NM_YN")));
                }
                if (!jsonObject.getString("printPUPO_ADDR_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "地址", jsonObject.getString("printPUPO_ADDR_YN")));
                }
                if (!jsonObject.getString("printPUPO_TEL_YN").equals("")){
                        returnString.append(String.format("&%s=%s", "電話", jsonObject.getString("printPUPO_TEL_YN")));
                }
            break;
            //受理拾得物發還清冊
            case "OP07A13Q.xls":
            ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_YN_OV500").equals("")){
                    if( jsonObject.getString("OP_PU_YN_OV500").equals("0") )
                        returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元(含)以下"));
                    else
                        returnString.append(String.format("&%s=%s", "拾得物總價值", "伍佰元以上"));
                }
                if (!jsonObject.getString("OP_RT_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "發還日期起日", jsonObject.getString("OP_RT_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_RT_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "發還日期迄日", jsonObject.getString("OP_RT_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
            break;
            //拾得公告期滿拾得人(或委其親友)領回清冊
            case "OP07A14Q.xls":
            ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_RT_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "發還日期起日", jsonObject.getString("OP_RT_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_RT_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "發還日期迄日", jsonObject.getString("OP_RT_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
            break;
            //未經認領及領回拾得物移交地方自治團體清冊
            case "OP07A15Q.doc":
            ///受理單位
                if( !jsonObject.getString("OP_AC_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_B_UNIT_CD").equals("") ){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_B_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                } else if( !jsonObject.getString("OP_AC_D_UNIT_CD").equals("")){
                    unitName = npaunitDao.getUnitName(jsonObject.getString("OP_AC_D_UNIT_CD"));
                    returnString.append(String.format("&%s=%s", "受理單位", unitName));
                }
                if (!jsonObject.getString("includeYN").equals("")){
                        returnString.append(String.format("&%s=%s", "含下屬", jsonObject.getString("includeYN")));
                }
                if (!jsonObject.getString("OP_PU_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期起日", jsonObject.getString("OP_PU_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_PU_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "拾得日期迄日", jsonObject.getString("OP_PU_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_S").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期起日", jsonObject.getString("OP_AC_DATE_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_AC_DATE_E").equals("")){
                        returnString.append(String.format("&%s=%s", "受理日期迄日", jsonObject.getString("OP_AC_DATE_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_NTC_PUPO_DT_S").equals("")){
                        returnString.append(String.format("&%s=%s", "通知拾得人領回日期起日", jsonObject.getString("OP_NTC_PUPO_DT_S").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_NTC_PUPO_DT_E").equals("")){
                        returnString.append(String.format("&%s=%s", "通知拾得人領回日期迄日", jsonObject.getString("OP_NTC_PUPO_DT_E").replace("/", "").replace(":", "").replace(" ", "")));
                }
                if (!jsonObject.getString("OP_TYPE_CD").equals("")){
                    if( jsonObject.getString("OP_TYPE_CD").equals("0") )
                        returnString.append(String.format("&%s=%s", "拾得物種類", "物品"));
                    else
                        returnString.append(String.format("&%s=%s", "拾得物種類", "現金"));
                }
            break;
        }
        return returnString.substring(1).toString();
    }

}
