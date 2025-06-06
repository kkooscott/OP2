package npa.op.util;

import com.aspose.cells.Cells;
import com.aspose.cells.License;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;
import com.aspose.cells.Worksheet;
import com.aspose.words.AutoFitBehavior;
import com.aspose.words.BreakType;
import com.aspose.words.CellVerticalAlignment;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Field;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.Font;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.LineSpacingRule;
import com.aspose.words.MailMergeCleanupOptions;
import com.aspose.words.Paragraph;
import com.aspose.words.PreferredWidth;
import com.aspose.words.SaveFormat;
import com.aspose.words.Shape;
import com.aspose.words.Table;
import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import static npa.op.base.AjaxBaseServlet.AJAX_REQ_ACTION_KEY;

public class ReportUtil {

    protected static Logger log = Logger.getLogger(ReportUtil.class);

    public static void sendToBrowserXls(String reportPath, HttpServletResponse response,
            String newReportName, String serverTempPath, Map<String, List> lists, JSONObject obj) throws Exception {

        com.aspose.cells.License license = new com.aspose.cells.License();
        license.setLicense("Aspose.Total.Java.lic");
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(newReportName, "UTF-8"));
        // 需增加才能讓successCallback有反應
//        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
        Workbook wb = new Workbook(reportPath);
        WorkbookDesigner designer = new WorkbookDesigner();
        designer.setWorkbook(wb);
        for (int i = 0; i < lists.size(); i++) {
            for (Object key : lists.keySet()) {
                designer.setDataSource(key.toString(), new MapData(lists.get(key)));
            }
        }
        for (Object key : obj.keySet()) {
            designer.setDataSource(key.toString(), obj.getString(key.toString()));
        }
        designer.process(true);
        response.setContentType("application/vnd.ms-excel");
        wb.save(response.getOutputStream(), wb.getFileFormat());
    }

    public static void sendToBrowserWord(String reportPath, HttpServletResponse response,
            String newReportName, String serverTempPath, DataSet ds, JSONObject obj, boolean browser, Integer k) throws Exception {

        com.aspose.words.License license = new com.aspose.words.License();
        license.setLicense("Aspose.Total.Java.lic");
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(newReportName, "UTF-8"));
        String[] ajaxAction = obj.getString(AJAX_REQ_ACTION_KEY).split(",");
        //String[] reportType = obj.getString("reportType").split(",");
        // 需增加才能讓successCallback有反應
//        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
        Document doc = new Document(reportPath);
//        if (obj.has("caseNo")) {
//            String[] reportType = obj.getString("reportType").split(",");
//            setBarCode(doc, obj.getString("caseNo"), reportType[k]);
//        }
        switch (ajaxAction[k]) {
//            case "TM05A.doc":
//                if ("true".equals(obj.getString("PIC"))) {
//                    doc.getMailMerge().setFieldMergingCallback(new HandleImageMergeTM05A());
//                }
//                break;
//            case "TM02B04M.doc":
//                if (!StringUtil.nvl(obj.getString("tempPicPath")).equals("")) {
//                    doc.getMailMerge().setFieldMergingCallback(new HandleImageMergeTM02B04M());
//                }
//                if (ds.getTables().get("MainTable").getResultSet().first()) {
//                    Document doc2 = new Document(reportPath.replace(".doc", "_1.doc"));
//                    if (obj.has("caseNo")) {
//                        String[] reportType = obj.getString("reportType").split(",");
//                        setBarCode(doc2, obj.getString("caseNo"), reportType[k]);
//                    }
//                    doc.appendDocument(doc2, ImportFormatMode.KEEP_DIFFERENT_STYLES);
//                }
//                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
//                break;
//            case "TM02B04M_PIC.doc":
//                doc.getMailMerge().setFieldMergingCallback(new HandleImageMerge());
//                break;
//            case "TM02B08M.doc":
//                if (!"".equals(obj.getString("tempPicPath"))) {
//                    doc.getMailMerge().setFieldMergingCallback(new HandleImageMergeTM02B08M());
//                }
//                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS);
//                break;
//            case "TM02B07M.doc":
//                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
//                        | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
//                        | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
//                break;
//            case "TM02B14M.doc":
////                    	if (!"".equals(obj.getString("tempPicPath"))){
//                doc.getMailMerge().setFieldMergingCallback(new HandleImageMergeTM02B14M());
////                    	}
//                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
//                        | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
//                        | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
//                break;
            case "TM02B12M_2.doc":
                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                break;
            case "TM02B13M_2.doc":
                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                break;
            case "TM02B19M.doc":
                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                break;
            default:
                doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                        | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                        | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
                break;
        }
        doc.getMailMerge().executeWithRegions(ds);
//        doc.getMailMerge().executeWithRegions((IMailMergeDataSource) ds);
        response.setContentType("application/msword");
//        FontChanger changer = new FontChanger(doc);
//            doc.accept(changer);
//            doc.save("D:\\testWord_Result.PDF", com.aspose.words.SaveFormat.PDF);
        if (browser) {
            doc.save(response.getOutputStream(), SaveFormat.DOC);
        } else {
            doc.save(serverTempPath + newReportName, SaveFormat.DOC);
        }
    }
    
    void responseWord(String file_name, Document doc, HttpServletResponse response, HttpServletRequest request) throws Exception
    {

            responseWord(file_name, doc, response, request, "");

    }

    void responseWord(String file_name, Document doc, HttpServletResponse response, HttpServletRequest request, String reMsg) throws Exception
    {

            if (doc != null)
            {
                    // 設定 response 封包中的標頭
                    // response.setHeader("Content-Disposition",
                    // "attachment; filename=" + NPAUtil.URLEncode(fileName));
                    // 下面寫法只對ie11有效,中文可以正常顯示,但是firefox與chrome會出現亂碼
                    // response.setHeader("Content-Disposition",
                    // "attachment; filename=" + NPAUtil.URLEncode(fileName));
                    // 這個寫法對firefox與chrome有效,中文可以正常顯示,但在ie11上中文會顯示錯誤
                    file_name = processFileName(request, file_name);

                    response.setHeader("Content-Disposition", "attachment; filename=" + file_name);
                    // 設定欲下載檔案的 ContentType
                    response.setContentType("application/msword");
                    // 加上這行才會觸發successCallback or .done 的成功事件
                    response.addCookie(new Cookie("fileDownload", "true"));
                    // response.addCookie(new Cookie("path","/"));
                    ServletOutputStream outputStream = response.getOutputStream();
                    doc.save(outputStream, com.aspose.words.SaveFormat.DOC);
                    // assert doc.getMailMerge().getFieldNames().length == 0 :
                    // "There was a problem with mail merge";
                    outputStream.flush();
                    // response.getWriter().flush();
                    // response.flushBuffer();
            } else
            {
                    if (StringUtil.nvl(reMsg).length() <= 0)
                            reMsg = "查無相關資料！";
                    response.addCookie(new Cookie("fileDownload", "false"));
                    response.setContentType("text/html");// 設成text/html ie7才不會有問題
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(reMsg + "##");
                    response.getWriter().flush();
                    response.flushBuffer();
            }

    }
    
    	/**
	 * 
	 * @Title: processFileName
	 * 
	 * @Description: ie,chrom,firfox下，處理文件名顯示亂碼問題
	 */
	public static String processFileName(HttpServletRequest request, String fileNames) {
		String codedfilename = null;
		try {
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

				String name = java.net.URLEncoder.encode(fileNames, "UTF8");

				codedfilename = name;
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

				codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codedfilename;
	}

    public static void sendToBrowserPDF(String reportPath, HttpServletResponse response,
            String newReportName, String serverTempPath, DataSet ds, JSONObject obj, boolean browser, Integer k) throws Exception {

        com.aspose.words.License license = new com.aspose.words.License();
        license.setLicense("Aspose.Total.Java.lic");
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(newReportName, "UTF-8"));
        String[] ajaxAction = obj.getString(AJAX_REQ_ACTION_KEY).split(",");
        //String[] reportType = obj.getString("reportType").split(",");
        // 需增加才能讓successCallback有反應
//        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        validator.Validator.setHeader(response, "Set-Cookie", "fileDownload=true; path=/");
        Document doc = new Document(reportPath);

         
        doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS
                | MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS
                | MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS | MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
        doc.getMailMerge().executeWithRegions(ds);
//        doc.getMailMerge().executeWithRegions((IMailMergeDataSource) ds);
        response.setContentType("application/msword");
        if (browser) {
            doc.save(response.getOutputStream(), SaveFormat.PDF);
        } else {
            doc.save(serverTempPath + newReportName, SaveFormat.PDF);
        }
    }
    
    protected void download(String url, String FilePath, String FileName, HttpServletRequest request, HttpServletResponse response, boolean delbool) throws FileNotFoundException, IOException {
        java.io.File file = new java.io.File(url + FilePath + FileName);  //建立需下載檔案的 File 物件
        long length = file.length();   //取出該檔案長度
        java.io.FileInputStream fi = new java.io.FileInputStream(file);   //建立該檔案的輸入串流
        //取得客戶端的輸出流, 這邊以 OutputStream 為輸入串流的物件
        java.io.OutputStream os = response.getOutputStream();
        //設定 response 封包中的標頭
        //response.setHeader("Content-Disposition", "attachment; filename=" + FileName); 
        boolean isIE = true;
        if (request.getHeader("User-Agent").toLowerCase().indexOf("chrome") > 0 || request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            isIE = false;
        } else {
            isIE = true;
        }
        if (isIE) // 下面寫法只對ie11有效,中文可以正常顯示,但是firefox與chrome會出現亂碼
        {
            response.setHeader("Content-Disposition", "attachment; filename=" + NPAUtil.URLEncode(FileName));
        } else // 這個寫法對firefox與chrome有效,中文可以正常顯示,但在ie11上中文會顯示錯誤
        {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(FileName.getBytes("utf-8"),
                    "iso-8859-1"));
        }

        //設定欲下載檔案的 ContentType
        response.setContentType("application/zip");
        //設定該檔案的長度
        response.setHeader("Content_Length", Long.toString(length));
        int r = 0;
        //將檔案讀入 FileInputStream  並寫入 OutputStream
        while ((r = fi.read()) != -1) {
            os.write(r);
        }
        os.flush();
        os.close();
        os = null;
        fi.close();
        if (delbool) {
            try {
                file = new java.io.File(url + FilePath + FileName);
                if (file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                }
            } catch (Exception e) {

                e.printStackTrace();

            }

        }
    }

    protected void downloadTXT(String url, String FilePath, String FileName, HttpServletRequest request, HttpServletResponse response, boolean delbool) throws FileNotFoundException, IOException {
        java.io.File file = new java.io.File(url + FilePath + request.getSession().getId() + ".txt");  //建立需下載檔案的 File 物件
        long length = file.length();   //取出該檔案長度
        java.io.FileInputStream fi = new java.io.FileInputStream(file);   //建立該檔案的輸入串流
        //取得客戶端的輸出流, 這邊以 OutputStream 為輸入串流的物件
        java.io.OutputStream os = response.getOutputStream();
        //設定 response 封包中的標頭
        //response.setHeader("Content-Disposition", "attachment; filename=" + FileName); 
        boolean isIE = true;
        if (request.getHeader("User-Agent").toLowerCase().indexOf("chrome") > 0 || request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            isIE = false;
        } else {
            isIE = true;
        }
        if (isIE) // 下面寫法只對ie11有效,中文可以正常顯示,但是firefox與chrome會出現亂碼
        {
            response.setHeader("Content-Disposition", "attachment; filename=" + NPAUtil.URLEncode(FileName));
        } else // 這個寫法對firefox與chrome有效,中文可以正常顯示,但在ie11上中文會顯示錯誤
        {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(FileName.getBytes("utf-8"),
                    "iso-8859-1"));
        }

        //設定欲下載檔案的 ContentType
        //response.setContentType("application/zip");
        response.setContentType("application/octet-stream");
        //設定該檔案的長度
        response.setHeader("Content_Length", Long.toString(length));
        int r = 0;
        //將檔案讀入 FileInputStream  並寫入 OutputStream
        while ((r = fi.read()) != -1) {
            os.write(r);
        }
        os.flush();
        os.close();
        os = null;
        fi.close();
        if (delbool) {
            try {
                file = new java.io.File(url + FilePath + request.getSession().getId() + ".txt");
                if (file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                }
            } catch (Exception e) {

                e.printStackTrace();

            }

        }
    }
    
}
