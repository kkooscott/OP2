/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package npa.op.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.apache.log4j.Logger;

import npa.op.util.NpaConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;


/**
 *
 * @author Wen
 */
@WebServlet("/ImageServlet")
public class ImageServlet  extends HttpServlet{
    Logger log = Logger.getLogger(ImageServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doProcess(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doProcess(req, resp);
    }

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("image/jpeg");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        Base64 base64 = new Base64();

        try {
            String fileName = StringEscapeUtils.escapeHtml(validator.Validator.getParameter(request, "file"));
            String path = "";

            String reSlash = fileName.replaceAll("\\\\","/");//將"\"更換為"/"
            String[] splitStr = reSlash.split("/");//做字串分割

            if("true".equals(NpaConfig.getString("ntlmFlag"))){
                System.out.println("ImageServlet==================================SOURCECD===================="+StringEscapeUtils.escapeHtml(splitStr[0].substring(0,1)));
                if (splitStr[0].equals("PUOJ_PIC")){
                    //登入NAS，先建立NTLM認證
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, NpaConfig.getString("ntlmUser"), NpaConfig.getString("ntlmPwd"));
                    SmbFile smbFileIn = new SmbFile(NpaConfig.getString("remoteWebFile")+reSlash, auth);
                    System.out.println("#############ImageServlet 73 smbFileIn "+StringEscapeUtils.escapeHtml(smbFileIn.getPath()));
                    SmbFileInputStream inputSmbFile = null;
                    try {
                        inputSmbFile = new SmbFileInputStream(smbFileIn);
                        System.out.println("#############ImageServlet 78 inputSmbFile ");
                        bis = new BufferedInputStream(inputSmbFile);
                        // get the output stream
                        bos = new BufferedOutputStream(response.getOutputStream());
                        System.out.println("#############ImageServlet 82  BOS");
                        // output the image as streaming
                        //20170906 加大byte4096==>8192
                        byte data[] = new byte[8192];
                        int size = 0;
                        size = bis.read(data);
                        try{
                            while (size != -1) {
                                byte[] encodedText = null;
                                encodedText = base64.encodeBase64Chunked(data);
                                bos.write(base64.decodeBase64(encodedText), 0, size);
                                size = bis.read(data);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
//                        while ((size =bis.read(data))!= 0) {
//                            bos.write(data, 0, size);
//                        }
                        System.out.println("#############ImageServlet 92 END ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputSmbFile != null) inputSmbFile.close();
                        //if (outputFile != null) outputFile.close();
                    }
                }else{
                    //登入NAS，先建立NTLM認證
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, NpaConfig.getString("ntlmUser"), NpaConfig.getString("ntlmPwd"));

                    SmbFile smbFileIn = new SmbFile(NpaConfig.getString("remoteWebFile")+reSlash, auth);
                    System.out.println("#############ImageServlet 135 smbFileIn "+StringEscapeUtils.escapeHtml(smbFileIn.getPath()));
                    SmbFileInputStream inputSmbFile = null;
                    //FileOutputStream outputFile = null;
                    try {
                        inputSmbFile = new SmbFileInputStream(smbFileIn);
                        System.out.println("#############ImageServlet 140 inputSmbFile ");
                        bis = new BufferedInputStream(inputSmbFile);
                        // get the output stream
                        bos = new BufferedOutputStream(response.getOutputStream());
                        System.out.println("#############ImageServlet 144 BOS");
                        // output the image as streaming
                        //20170906 加大byte4096==>8192
                        byte data[] = new byte[8192];
                        int size = 0;
                        size = bis.read(data);
                        try{
                            while (size != -1) {
                                byte[] encodedText = null;
                                encodedText = base64.encodeBase64Chunked(data);
                                bos.write(base64.decodeBase64(encodedText), 0, size);
                                size = bis.read(data);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
//                        while ((size =bis.read(data))!= 0) {
//                            bos.write(data, 0, size);
//                        }
                        System.out.println("#############ImageServlet 154 END ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputSmbFile != null) inputSmbFile.close();
                        //if (outputFile != null) outputFile.close();
                    }
                }
            }else{
                    
                if (splitStr[0].equals("PUOJ_PIC")){
                    path = parsingImagePath(NpaConfig.getString("fileurl"), reSlash);
                }else{
                    path = parsingImagePath(NpaConfig.getString("fileurl"), reSlash);
                }
//                String normalized = new URI(path).normalize().getPath();
                // get file as input streaming
                bis = new BufferedInputStream(new FileInputStream(new File(path)));
//                bis = new BufferedInputStream(new FileInputStream(new File(normalized)));
                // get the output stream
                bos = new BufferedOutputStream(response.getOutputStream());
                // output the image as streaming
                //20170906 加大byte4096==>8192
                byte data[] = new byte[8192];
                int size = 0;
                size = bis.read(data);
                try{
                    while (size != -1) {
                        bos.write(data, 0, size);
                        size = bis.read(data);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
//                while ((size =bis.read(data))!= 0) {
//                    bos.write(data, 0, size);
//                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        }
    }

     private String parsingImagePath(String dir, String path) {
        System.out.println("path:" + StringEscapeUtils.escapeHtml(path));
        //return "C:\\uploads\\"+path;
        return dir+path;
    }
     
}

