package npa.op.control;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import npa.op.base.AjaxBaseServlet;
import npa.op.util.NpaConfig;
import npa.op.util.StringUtil;
import npa.op.vo.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import npa.op.dao.OPDT_I_PHOTO;
import npa.op.dao.OPDT_I_PU_BASIC;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.net.ftp.FTPClient;

/**
 * 入山申辦案件查詢
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends AjaxBaseServlet {
	private static final long serialVersionUID = 1L;
        private static int MAX_PUTFILE_RETRY_COUNT = 5;
	Logger log = Logger.getLogger(UploadServlet.class);

	@Override
	protected void executeAjax(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, User user, JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {
		
		request.setCharacterEncoding("UTF-8"); //處理中文檔名
                response.setCharacterEncoding("UTF-8");//處理返回中文檔名
                PrintWriter out = response.getWriter();
                JSONObject jsonMsg = new JSONObject();
                JSONObject pathObject = new JSONObject();
                JSONObject pathReturn = new JSONObject();
                JSONArray resultDataArray = new JSONArray();
                JSONArray pathArray = new JSONArray();
                StringUtil strUtil = new StringUtil();
                OPDT_I_PHOTO iPhotoDao = new OPDT_I_PHOTO();
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                String TYPE = request.getParameter("TYPE");
                //String fileName = strUtil.full2half(request.getParameter("fileName"));
//                String[] fileName = {"",""};

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
//                String year = String.valueOf(cal.get(Calendar.YEAR));
//                String month = String.valueOf(cal.get(Calendar.MONTH) +1);
//                String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                String year = getCurrEDate().substring(0, 4);
                String month = getCurrEDate().substring(4, 6);
                String day = getCurrEDate().substring(6, 8);
                String systemDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString();
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                boolean result = false;
                String msg = "";
                String sub = "";
                int count = 0;
		switch (TYPE) {			
                    case "IMAGES":
                        String basicSeqNo = StringEscapeUtils.escapeHtml(request.getParameter("OP_BASIC_SEQ_NO"));
                        String acRcno = StringEscapeUtils.escapeHtml(request.getParameter("OP_AC_RCNO"));
                        String dtlSeqNo = StringEscapeUtils.escapeHtml(request.getParameter("OP_DTL_SEQ_NO"));
                        String anSeqNo = StringEscapeUtils.escapeHtml(request.getParameter("OP_AN_SEQ_NO"));
//                        String picType = StringEscapeUtils.escapeHtml(request.getParameter("PIC_TYPE"));
                        String picType = new URI(StringEscapeUtils.escapeHtml(request.getParameter("PIC_TYPE"))).normalize().getPath();
                        String opUplFileName1 = StringEscapeUtils.escapeHtml(request.getParameter("OP_UPL_FILE_NAME1"));
                        String IPhotoOpSeqNo1 = StringEscapeUtils.escapeHtml(request.getParameter("IPHOTO_OP_SEQ_NO1"));
                        String IPhotoDetele1 = StringEscapeUtils.escapeHtml(request.getParameter("IPHOTO_DETELE1"));
                        String IPhotoFilePath1 = StringEscapeUtils.escapeHtml(request.getParameter("IPHOTO_OP_FILE_PATH1"));
                        String opUplFileName2 = StringEscapeUtils.escapeHtml(request.getParameter("OP_UPL_FILE_NAME2"));
                        String IPhotoOpSeqNo2 = StringEscapeUtils.escapeHtml(request.getParameter("IPHOTO_OP_SEQ_NO2"));
                        String IPhotoDetele2 = StringEscapeUtils.escapeHtml(request.getParameter("IPHOTO_DETELE2"));
                        String IPhotoFilePath2 = StringEscapeUtils.escapeHtml(request.getParameter("IPHOTO_OP_FILE_PATH2"));
                        
                        String filepath = StringEscapeUtils.escapeHtml(request.getParameter("PATH"));
                        //先判斷是否要刪除檔案
                        if ( "1".equals(IPhotoDetele1)) {
                            JSONArray pictureArray = iPhotoDao.queryPictureById( Integer.valueOf(IPhotoOpSeqNo1) );
                            opUplFileName1 = pictureArray.getJSONObject(0).getString("OP_UPL_FILE_NAME");
                            IPhotoFilePath1 = pictureArray.getJSONObject(0).getString("OP_FILE_PATH");
   
                            if ("true".equals(NpaConfig.getString("ntlmFlag"))) {
                                //登入NAS，先建立NTLM認證
                                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, NpaConfig.getString("ntlmUser"), NpaConfig.getString("ntlmPwd"));
                                SmbFile smbFile = new SmbFile(NpaConfig.getString("remoteWebFile") + IPhotoFilePath1, auth);
                                try {
                                    System.out.println("#############UploadServlet oldfileName-------- " + opUplFileName1);
                                    if (!smbFile.exists()) {
                                        smbFile.mkdirs();
                                    }
                                    SmbFile file = new SmbFile(smbFile + "//" + opUplFileName1, auth);
                                    boolean deleteFlag = false;
                                    if( !opUplFileName1.equals("") && !IPhotoFilePath1.equals("") ){
                                        file.delete();
                                        deleteFlag = true;
                                    }
                                    if (deleteFlag) {
                                        System.out.println(file.getName() + " is deleted!");
                                        result = iPhotoDao.deleteByOpSeqNo(Integer.valueOf(IPhotoOpSeqNo1));
                                        System.out.println(result);
                                    } else {
                                        System.out.println("Delete operation is failed.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                File path1 = new File(NpaConfig.getString("fileurl") + IPhotoFilePath1);
                                java.io.File file = new java.io.File(path1 + "//" + opUplFileName1);
                                if (file.delete()) {
                                    System.out.println(file.getName() + " is deleted!");
                                    result = iPhotoDao.deleteByOpSeqNo(Integer.valueOf(IPhotoOpSeqNo1));
                                    System.out.println(result);
                                } else {
                                    System.out.println("Delete operation is failed.");
                                }
                            }
                        }
                        if ( "1".equals(IPhotoDetele2)) {
                            JSONArray pictureArray = iPhotoDao.queryPictureById( Integer.valueOf(IPhotoOpSeqNo2) );
                            opUplFileName2 = pictureArray.getJSONObject(0).getString("OP_UPL_FILE_NAME");
                            IPhotoFilePath2 = pictureArray.getJSONObject(0).getString("OP_FILE_PATH");
                            
                            if ("true".equals(NpaConfig.getString("ntlmFlag"))) {
                                //登入NAS，先建立NTLM認證
                                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, NpaConfig.getString("ntlmUser"), NpaConfig.getString("ntlmPwd"));
                                SmbFile smbFile = new SmbFile(NpaConfig.getString("remoteWebFile") + IPhotoFilePath2, auth);
                                try {
                                    System.out.println("#############UploadServlet oldfileName-------- " + opUplFileName2);
                                    if (!smbFile.exists()) {
                                        smbFile.mkdirs();
                                    }
                                    SmbFile file = new SmbFile(smbFile + "//" + opUplFileName2, auth);
                                    boolean deleteFlag = false;
                                    if( !opUplFileName2.equals("") && !IPhotoFilePath2.equals("") ){
                                        file.delete();
                                        deleteFlag = true;
                                    }
                                    if (deleteFlag) {
                                        System.out.println(file.getName() + " is deleted!");
                                        result = iPhotoDao.deleteByOpSeqNo(Integer.valueOf(IPhotoOpSeqNo2));
                                        System.out.println(result);
                                    } else {
                                        System.out.println("Delete operation is failed.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                File path2 = new File(NpaConfig.getString("fileurl") + IPhotoFilePath2);
                                java.io.File file = new java.io.File(path2 + "//" + opUplFileName2);
                                if (file.delete()) {
                                    System.out.println(file.getName() + " is deleted!");
                                    result = iPhotoDao.deleteByOpSeqNo(Integer.valueOf(IPhotoOpSeqNo2));
                                    System.out.println(result);
                                } else {
                                    System.out.println("Delete operation is failed.");
                                }
                            }
                        }
                        if (isMultipart) { 
                            String oldfileName = StringEscapeUtils.escapeHtml(request.getParameter("oldfileName"));
                            System.out.println("#############UploadServlet filepath-------- " + filepath);
                            System.out.println("#############UploadServlet oldfileName-------- " + oldfileName);
                            // Create a factory for disk-based file items
                            FileItemFactory factory = new DiskFileItemFactory();
                            // Create a new file upload handler
                            ServletFileUpload upload = new ServletFileUpload(factory);
                            // Parse the request
                            List items = upload.parseRequest(request);
                            Iterator iterator = items.iterator();
                            int imagecount = 0;
                            while (iterator.hasNext()) {
                                imagecount ++;
                                FileItem item = (FileItem) iterator.next();
                                System.out.println("#############UploadServlet item-------- " + item.isFormField());
                                if (!item.isFormField()) {
                                    //目錄改為受理日期
                                    String opAcDate = ((JSONArray)iPuBasicDao.queryBasicByIdOriginal( Integer.valueOf(basicSeqNo) )).getJSONObject(0).getString("OP_AC_DATE").toString();

                                    if( opAcDate.length() != 0 && opAcDate != null && !"".equals(opAcDate) ){
                                        year = opAcDate.substring(0, 4) ;
                                        month = opAcDate.substring(4, 6) ;
                                        day = opAcDate.substring(6, 8) ;
                                    }
                                    String fileNameFull2half = strUtil.full2half(item.getName().toString());
                                    String fileName = fileNameFull2half.substring(fileNameFull2half.lastIndexOf("\\")+1,fileNameFull2half.length());
                                    String extension = "";
                                    if( item.getName() != null ){
                                        extension = "." + item.getName().toLowerCase().substring(item.getName().lastIndexOf(".") + 1);
                                    }
                                    if ("true".equals(NpaConfig.getString("ntlmFlag"))) {
                                        //登入NAS，先建立NTLM認證
                                        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, NpaConfig.getString("ntlmUser"), NpaConfig.getString("ntlmPwd"));
                                        
                                        System.out.println(year + "-" + month + "-" + day);
                                        //sub = "PUOJ_PIC"+ "\\" + year + "\\" + month + "\\" + day + "\\" + picType;
                                        sub = "PUOJ_PIC";
                                        SmbFile sFileSub = new SmbFile(NpaConfig.getString("remoteWebFile") + sub, auth);
                                        System.out.println("####### UploadServlet 156 UploadServlet PATH= " + NpaConfig.getString("remoteWebFile") + sub);
                                        if (!sFileSub.exists()) {
                                            System.out.println("####### UploadServlet PATH NOT exists= " + NpaConfig.getString("remoteWebFile") + sub);
                                            sFileSub.mkdir();
                                        }
                                        SmbFile sFileYear = new SmbFile(NpaConfig.getString("remoteWebFile") + sub + "\\" + year, auth);
                                        System.out.println("####### UploadServlet 160 PATH= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year);
                                        if (!sFileYear.exists()) {
                                            System.out.println("####### UploadServlet PATH NOT exists= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year);
                                            sFileYear.mkdir();
                                        }
                                        SmbFile sFileMonth = new SmbFile(NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month, auth);
                                        System.out.println("####### UploadServlet 168 PATH= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month);
                                        if (!sFileMonth.exists()) {
                                            System.out.println("####### UploadServlet PATH NOT exists= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month);
                                            sFileMonth.mkdir();
                                        }
                                        SmbFile sFileDate = new SmbFile(NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month + "\\" + day, auth);
                                        System.out.println("####### UploadServlet 174 PATH= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month + "\\" + day);
                                        if (!sFileDate.exists()) {
                                            System.out.println("####### UploadServlet PATH NOT exists= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month + "\\" + day);
                                            sFileDate.mkdir();
                                        }
                                        SmbFile sFilePicType = new SmbFile(NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month + "\\" + day + "\\" + picType, auth);
                                        System.out.println("####### UploadServlet PATH= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month + "\\" + day + "\\" + picType);
                                        if (!sFilePicType.exists()) {
                                            System.out.println("####### UploadServlet PATH NOT exists= " + NpaConfig.getString("remoteWebFile") + sub + "\\" + year + "\\" + month + "\\" + day + "\\" + picType);
                                            sFilePicType.mkdir();
                                        }
                                        System.out.println("############# sFilePicType PATH " + sFilePicType.getPath());
                                        
                                        sub = "PUOJ_PIC"+ "\\" + year + "\\" + month + "\\" + day + "\\" + picType+"\\";
                                        String sRemoteURL = NpaConfig.getString("remoteWebFile");
                                        SmbFile smbFile = new SmbFile(sRemoteURL + sub, auth);
                                        System.out.println(fileName);
//                                        String newFileName = systemDateTime + "_" + basicSeqNo + "_" + fileName;
                                        String newFileName = systemDateTime + "_" + basicSeqNo + "_" + acRcno + "_" + imagecount + extension;
                                        SmbFile uploadedFile = new SmbFile(smbFile + newFileName, auth);
                                        System.out.println("#############UploadServlet oldfileName-------- " + uploadedFile);
                                        SmbFileOutputStream outputSmbFile = new SmbFileOutputStream(uploadedFile);
                                        InputStream resizeImg = Img_compress(item);
//                                        BufferedInputStream bis = new BufferedInputStream(item.getInputStream());
                                        BufferedInputStream bis = new BufferedInputStream( resizeImg );
                                        System.out.println("#############UploadServlet bis-------- " + bis);
                                        
                                        try{
                                            //20170906 加大byte4096==>8192
                                            byte data[] = new byte[8192];
                                            int size = 0;
                                            size = bis.read(data);
                                            System.out.println("#############UploadServlet outputSmbFile.write size："+size);
                                            while (size != -1) {
                                                outputSmbFile.write(data, 0, size);
                                                size = bis.read(data);
                                            }
                                            System.out.println("#############UploadServlet 499 while end size："+size);
                                            count++;
                                            resultDataArray.put(pathObject);
                                            
                                            pathReturn.put("OP_BASIC_SEQ_NO", basicSeqNo);
                                            pathReturn.put("OP_AC_RCNO", acRcno);
                                            if( picType.equals("1") ){
                                                pathReturn.put("OP_DTL_SEQ_NO", dtlSeqNo);
                                                pathReturn.put("OP_AN_SEQ_NO", "0");
                                                pathReturn.put("OP_UPL_FUNC_CD", "1");
                                                pathReturn.put("OP_UPL_FUNC_NM", "明細(不公開)");
                                            }else{
                                                pathReturn.put("OP_DTL_SEQ_NO", "0");
                                                pathReturn.put("OP_AN_SEQ_NO", anSeqNo);
                                                pathReturn.put("OP_UPL_FUNC_CD", "2");
                                                pathReturn.put("OP_UPL_FUNC_NM", "公告(公開)");
                                            }           
                                            pathReturn.put("OP_PIC_TYPE", picType);
                                            pathReturn.put("OP_FILE_PATH", sub);
                                            pathReturn.put("OP_ORG_FILE_NAME", fileName);
                                            pathReturn.put("OP_UPL_FILE_NAME", newFileName);
                                            pathReturn.put("userVO", user);
                                            result = iPhotoDao.add(pathReturn);
                                            if (!result) {
                                                msg = "請洽系統管理員";
                                            }
                                            pathReturn.put("msg", msg);
                                            
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            outputSmbFile.close();
                                            bis.close();
                                        }
                                    } else {
                                        System.out.println(year + "-" + month + "-" + day);
                                        sub = "PUOJ_PIC"+ "\\" + year + "\\" + month + "\\" + day + "\\" + picType;
                                        File path = new File(NpaConfig.getString("fileurl") + sub);
                                        try {
                                            if (!path.exists()) {
                                                boolean status = path.mkdirs();
                                            }
                                        } catch (Exception e) {

                                            e.printStackTrace();

                                        }
//                                        System.out.println(item.getName());
//                                        String newFileName = systemDateTime + "_" + basicSeqNo + "_" + item.getName();
//                                        File uploadedFile = new File(path + "//" + newFileName);
//                                        item.write(uploadedFile);
                                        InputStream resizeImg = Img_compress(item);
//                                        String newFileName = systemDateTime + "_" + basicSeqNo + "_" + fileName;
                                        String newFileName = systemDateTime + "_" + basicSeqNo + "_" + acRcno + "_" + imagecount + extension;
                                        File uploadedFile = new File(path + "//" + newFileName);
                                        FileOutputStream outputFile = new FileOutputStream(uploadedFile);
                                        count++;
                                        resultDataArray.put(pathObject);
                                        try{
                                            //20170906 加大byte4096==>8192
                                            byte data[] = new byte[8192];
                                            int size = 0;
                                            size = resizeImg.read(data);
                                            System.out.println("#############UploadServlet outputSmbFile.write size："+size);
                                            while (size != -1) {
                                                outputFile.write(data, 0, size);
                                                size = resizeImg.read(data);
                                            }
                                            System.out.println("#############UploadServlet 499 while end：");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            resizeImg.close();
                                            outputFile.close();
                                        }
                                        pathReturn.put("OP_BASIC_SEQ_NO", basicSeqNo);
                                        pathReturn.put("OP_AC_RCNO", acRcno);
                                        if( picType.equals("1") ){
                                            pathReturn.put("OP_DTL_SEQ_NO", dtlSeqNo);
                                            pathReturn.put("OP_AN_SEQ_NO", "0");
                                            pathReturn.put("OP_UPL_FUNC_CD", "1");
                                            pathReturn.put("OP_UPL_FUNC_NM", "明細(不公開)");
                                        }else{
                                            pathReturn.put("OP_DTL_SEQ_NO", "0");
                                            pathReturn.put("OP_AN_SEQ_NO", anSeqNo);
                                            pathReturn.put("OP_UPL_FUNC_CD", "2");
                                            pathReturn.put("OP_UPL_FUNC_NM", "公告(公開)");
                                        }           
                                        pathReturn.put("OP_PIC_TYPE", picType);
                                        pathReturn.put("OP_FILE_PATH", sub + "\\");
                                        pathReturn.put("OP_ORG_FILE_NAME", fileName);
                                        pathReturn.put("OP_UPL_FILE_NAME", newFileName);
                                        pathReturn.put("userVO", user);
                                        result = iPhotoDao.add(pathReturn);
                                        if (!result) {
                                            msg = "請洽系統管理員";
                                        }
                                        pathReturn.put("msg", msg);
                                    }

                                } else {
                                    argJsonObj.put(item.getFieldName(), item.getString());
                                }
                            }

                            this.setFormData(returnJasonObj, resultDataArray);
                            System.out.println("#############UploadServlet IMAGES--------END ");
                            break;
                        }
                        
                    case "ANN2_DELETE":
                        String OP_BASIC_SEQ_NO = request.getParameter("OP_BASIC_SEQ_NO");
                        String OP_AC_RCNO = request.getParameter("OP_AC_RCNO");
                        pathReturn = new JSONObject();
                        resultDataArray = new JSONArray();
                        pathReturn.put("OP_BASIC_SEQ_NO", Integer.valueOf(OP_BASIC_SEQ_NO));
                        pathReturn.put("OP_AC_RCNO", OP_AC_RCNO);
                        pathReturn.put("OP_PIC_TYPE", "2");
                        resultDataArray = iPhotoDao.getPicture( pathReturn );
                        if ("true".equals(NpaConfig.getString("ntlmFlag"))) {
                            //登入NAS，先建立NTLM認證
                            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, NpaConfig.getString("ntlmUser"), NpaConfig.getString("ntlmPwd"));
                            for(int i=0;i<resultDataArray.length();i++){
                                SmbFile smbFile = new SmbFile(NpaConfig.getString("remoteWebFile") + resultDataArray.getJSONObject(i).getString("OP_FILE_PATH"), auth);
                                try {
                                    System.out.println("#############UploadServlet oldfileName-------- " + resultDataArray.getJSONObject(i).getString("OP_FILE_PATH"));
                                    if (!smbFile.exists()) {
                                        smbFile.mkdirs();
                                    }
                                    SmbFile file = new SmbFile(smbFile + "//" + resultDataArray.getJSONObject(i).getString("OP_UPL_FILE_NAME"), auth);
                                    boolean deleteFlag = false;
                                    file.delete();
                                    deleteFlag = true;
                                    if (deleteFlag) {
                                        System.out.println(file.getName() + " is deleted!");
                                        result = iPhotoDao.deleteByOpSeqNo(Integer.valueOf(resultDataArray.getJSONObject(i).getString("OP_SEQ_NO")));
                                        System.out.println(result);
                                    } else {
                                        System.out.println("Delete operation is failed.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            for(int i=0;i<resultDataArray.length();i++){
                                File path1 = new File(NpaConfig.getString("fileurl") + resultDataArray.getJSONObject(i).getString("OP_FILE_PATH"));
                                java.io.File file = new java.io.File(path1 + "//" + resultDataArray.getJSONObject(i).getString("OP_UPL_FILE_NAME"));
                                if (file.delete()) {
                                    System.out.println(file.getName() + " is deleted!");
                                    result = iPhotoDao.deleteByOpSeqNo(Integer.valueOf(resultDataArray.getJSONObject(i).getString("OP_SEQ_NO")));
                                    System.out.println(result);
                                } else {
                                    System.out.println("Delete operation is failed.");
                                }
                            }
                        }
                        
                    break;    
		}
	}
        
        //照片壓縮
        private InputStream Img_compress(FileItem file) {
            String attrName = file.getName();
            String attType = attrName.substring(attrName.lastIndexOf(".") + 1);

            int width = 600;
            int heigth = 600;
            ByteArrayInputStream in = null;
            Graphics2D graphics2D = null;

            try (ByteArrayOutputStream out = new ByteArrayOutputStream(file.get().length)) {
                Image img = ImageIO.read(file.getInputStream());
                if (img.getWidth(null) == -1) {

                } else {
                    int newWidth;
                    int newHeight;
                    double rate1 = ((double) img.getWidth(null)) / (double) width
                                           + 0.1;
                    double rate2 = ((double) img.getHeight(null)) / (double) heigth
                                           + 0.1;
                    // 根據縮放比率大的進行縮放控制   
                    double rate = rate1 > rate2 ? rate1 : rate2;
                    newWidth = (int) (((double) img.getWidth(null)) / rate);  //將大於規定尺寸的圖片縮小，將小於規定尺寸的圖片放大
                    newHeight = (int) (((double) img.getHeight(null)) / rate);
                    
                    if( attType.equals("png") ){
                        BufferedImage tag = new BufferedImage(newWidth, newHeight,
                                                          BufferedImage.TYPE_USHORT_555_RGB);
                        graphics2D = tag.createGraphics();
                        graphics2D.setBackground(Color.white);
                        graphics2D.clearRect(0, 0, newWidth, newHeight);
                        graphics2D.drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
                        ImageWriter imgWrier;
                        imgWrier = ImageIO.getImageWritersByFormatName( attType ).next();
                        imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                        imgWrier.write(new IIOImage(tag, null, null));
                        out.flush();
                    }else{
                        BufferedImage tag = new BufferedImage(newWidth, newHeight,
                                                          BufferedImage.TYPE_INT_RGB);
                        img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        tag.getGraphics().drawImage(img.getScaledInstance(newWidth,
                                                                          newHeight,
                                                                          Image.SCALE_SMOOTH),
                                                    0,
                                                    0, null);
                        ImageWriter imgWrier;  //創建壓縮的圖片，用來編碼和寫入圖像的抽象超類。此類必須由在 Java Image I/O 框架的上下文中寫出圖像的類為其創建子類。 
                        ImageWriteParam imgWriteParams;
                        imgWrier = ImageIO.getImageWritersByFormatName( attType ).next();
                        imgWriteParams
                            = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
                                    null);
                        imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        imgWriteParams.setCompressionQuality(0.5f);
                        imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
                        ColorModel colorModel = ColorModel.getRGBdefault();
                        imgWriteParams.setDestinationType(
                                new javax.imageio.ImageTypeSpecifier(
                                        colorModel, colorModel.
                                                createCompatibleSampleModel(100, 100)));
                        imgWrier.reset();
                        imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                        imgWrier.write(null, new IIOImage(tag, null, null),
                                       imgWriteParams);
                    }
                    
                    out.flush();
                    in = new ByteArrayInputStream(out.toByteArray());
                    //        out.close();  
                }
            } catch (IOException e) {
                e.printStackTrace();
//                log.warn("圖片壓縮出現問題", e);
            }
            return in;
        }
        
        //上傳至FTP
//        private void FtpStart(BufferedInputStream bis,String sub, String newFileName) throws FileNotFoundException{
//		String serverIP = NpaConfig.getString("SFTP01_SERVER_IP");
//		int serverPort = Integer.valueOf(NpaConfig.getString("SFTP01_SERVER_PORT")).intValue();
//		String username = NpaConfig.getString("SFTP01_USER");
//		String password = NpaConfig.getString("SFTP01_PASSWORD");
//		String remotePath = NpaConfig.getString("REMOTE_PATH");
//                String remotePathNew = remotePath + "/" + sub.replace("\\", "_") + newFileName;
//                System.out.println("#############UploadServlet FTP remotePathNew-------- " + remotePathNew);
//                System.out.println("#############UploadServlet FTP bis-------- " + bis);
//		FTPClient ftp;
//		String errorMsg = "";
//		
//		try {
//			ftp = new FTPClient();
//			ftp.connect(serverIP, serverPort);
//                        System.out.println("#############UploadServlet FTP Connect to server...");
//			if (ftp.login(username, password)){
//                            System.out.println("#############UploadServlet FTP user login sucessful...");
//				if (!ftp.changeWorkingDirectory(remotePath)){
//                                        System.out.println("#############UploadServlet FTP can not change directory to" + remotePath);
//					return;
//				}
//                                System.out.println("#############UploadServlet FTP change directory to" + remotePath);
//                                remotePathNew = sub.replace("\\", "_") + newFileName;
//                                System.out.println("#############UploadServlet FTP saveFileName-------- " + remotePathNew);
//                                //20170906 加大byte4096==>8192
//                                byte data[] = new byte[8192];
//                                int size = 0;
//                                size = bis.read(data);
//                                System.out.println("#############UploadServlet FTP.write size："+size);
//                                if (size != -1) {
//                                    ftp.storeFile( remotePathNew, bis);
//                                    System.out.println("#############UploadServlet FTP.write end");
//                                }
//			}
//			
//			ftp.disconnect();
//                        System.out.println("#############UploadServlet FTP Disconnect from server...");
//		} catch (SocketException e) {
//			e.printStackTrace();
//			errorMsg = e.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//			errorMsg = e.toString();
//		}
//		if (!errorMsg.equals("")){
//                    System.out.println("#############UploadServlet FTP Exception =>"+errorMsg);
//		}
//	}
        //取得現在日期
        private static String getCurrEDate() {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return String.valueOf(dateFormat.format(date));
        }
		
}
