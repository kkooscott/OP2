package npa.op.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import com.aspose.words.DataSet;
import com.aspose.words.DataTable;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import npa.op.vo.FileType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;

public class ExportUtil {
	private static Logger log = Logger.getLogger(ExportUtil.class);
	
	public void popupPDF(DataSet ds, HttpServletResponse response, HttpServletRequest request, String templateFileName,
			String outputFileName) {
		try {
			String dataDir = request.getServletContext().getRealPath("/reportTemplate")+ "/";
			String fileName = templateFileName;
			exportFileByDs(dataDir + fileName, outputFileName, ds, "pdf", response, request);

		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
	}

	/**
	 * 傳入DataSet取得PDF
	 * 
	 * @param srcFilePath 含檔名之完整路徑
	 * @param fileName 輸出之PDF檔名
	 * @param ds 要合併至word的資料
	 * @param response
	 */
	private void exportFileByDs(String srcFilePath, String fileName, DataSet ds, String subFileType, HttpServletResponse response, HttpServletRequest request) {
		Document doc = null;
		License license = new License();
        
		try {
			license.setLicense("Aspose.Words.lic");
			
			// Open the document.
			doc = new Document(srcFilePath);
			// Execute nested mail merge with regions
			doc.getMailMerge().executeWithRegions(ds);
			
			boolean isIE = request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") >  0  ?  true  :  false;
			log.debug("瀏覽器為：" + request.getHeader("USER-AGENT"));
			
			FileType ootputFileType = FileType.getInstance();
			ootputFileType.setsubFileType(subFileType);
			
			if (isIE)
				//下面寫法只對ie11有效,中文可以正常顯示,但是firefox與chrome會出現亂碼
				response.setHeader("Content-Disposition", "attachment; filename=" + NPAUtil.URLEncode(fileName + "." + subFileType));
			else
				//這個寫法對firefox與chrome有效,中文可以正常顯示,但在ie11上中文會顯示錯誤
				response.setHeader("Content-Disposition", "attachment; filename=" 
									+ new String((fileName + "." + subFileType).getBytes("utf-8"), "iso-8859-1"));
			// 設定欲下載檔案的 ContentType
			response.setContentType("application/" + ootputFileType.getcontentType());
			response.addCookie(new Cookie("fileDownload", "true"));
			
			OutputStream os =  response.getOutputStream();
			
			doc.save(os, ootputFileType.getsavedFormat());
			
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		    log.error(ExceptionUtil.toString(e));
		}
	}
	
	public void exportFileByRS(String srcFilePath, String fileName, ResultSet rs, HttpServletResponse response) {
		Document doc = null;
		DataSet ds = new DataSet();
		CachedRowSet crs = null;
		
		try {
			crs = new FixedCachedRowSetImpl();
			
			crs.populate(rs);
			DataTable dt = new DataTable(crs, "MAINTABLE");
	        ds.getTables().add(dt);
	        
			// Open the document.
			doc = new Document(srcFilePath);
			// Execute nested mail merge with regions
			doc.getMailMerge().executeWithRegions(ds);
			
			response.setHeader("Content-Disposition", "attachment; filename=" 
								+ new String((fileName + ".pdf").getBytes("utf-8"), "iso-8859-1"));
			// 設定欲下載檔案的 ContentType
			response.setContentType("application/pdf");
			doc.save(response.getOutputStream(), SaveFormat.PDF);

		} catch (Exception e) {
			e.printStackTrace();
		    log.error(ExceptionUtil.toString(e));
		}
	}
	
	public ByteArrayInputStream exportIntoFile(String srcFilePath, String fileName, DataSet ds, HttpServletResponse response) {
		Document doc = null;
		License license = new License();
		ByteArrayInputStream returnValue = null;
        
		try {
			license.setLicense("Aspose.Words.lic");
			
			// Open the document.
			doc = new Document(srcFilePath);
			// Execute nested mail merge with regions
			doc.getMailMerge().executeWithRegions(ds);
			
			//下面寫法只對ie11有效,中文可以正常顯示,但是firefox與chrome會出現亂碼
			//response.setHeader("Content-Disposition", "attachment; filename=" + NPAUtil.URLEncode(fileName + ".pdf"));
			//這個寫法對firefox與chrome有效,中文可以正常顯示,但在ie11上中文會顯示錯誤
			response.setHeader("Content-Disposition", "attachment; filename=" 
								+ new String((fileName + ".pdf").getBytes("utf-8"), "iso-8859-1"));
			// 設定欲下載檔案的 ContentType
			response.setContentType("application/pdf");
			ByteArrayOutputStream dstStream = new ByteArrayOutputStream();
			doc.save(dstStream, SaveFormat.PDF);
			returnValue = new ByteArrayInputStream(dstStream.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
		    log.error(ExceptionUtil.toString(e));
		}
		
		return returnValue;
	}
}