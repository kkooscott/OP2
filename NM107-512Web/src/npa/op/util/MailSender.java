/*
 * PoliceMailSender.java
 *
 * Created on 2014年09月15日, 下午 6:21
 */

package npa.op.util;
import java.util.*;
import javax.mail.internet.*;
import javax.mail.*;
import org.json.JSONObject;

public class MailSender {
    
    /** Creates a new instance of MailSender */
    private String sender=null;
    private String receiver=null;
    private String smtp =null;
    private String subject = null;
    private String content = null;    
    
    public MailSender () {
//        System.out.println("new PoliceMailSender done!");
    }
    
    public boolean doClmCancel(String clmName, String acRcno, String mailContent, String email) {
        boolean result = false;
        
        try{
            mailContent = mailContent.replaceAll("\n", "<br>");
            String tmpcontent = "<html><head><title>拾得遺失物認領結果通知信</title></head><Body>"
                    + "#clmName# 先生/小姐 您好：<br>"
                    + "<br>"
                    + "您的收據編號 #acRcno#  <br>"
                    + "#mailContent#"
                    + "<br>"
                    + "<br>"
                    + "註：此封郵件為系統自動發送，請勿回覆。";
            tmpcontent = tmpcontent.replaceAll("#clmName#", clmName);
            tmpcontent = tmpcontent.replaceAll("#acRcno#", acRcno);
            tmpcontent = tmpcontent.replaceAll("#mailContent#", mailContent);
            
            setSubject("拾得遺失物認領結果通知信");
            setReceiver(email);
            setContent(tmpcontent);
            if( mailContent.equals("") ){ //空的時候不發mail
                
            }else{
                sendMail("text/html");     //html
            }
            
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean doAnDlNotice(String clmName, String acRcno, String mailContent, String email) {
        boolean result = false;
        
        try{
            mailContent = mailContent.replaceAll("\n", "<br>");
            String tmpcontent = "<html><head><title>拾得遺失物拾得人領回通知信</title></head><Body>"
                    + "#clmName# 先生/小姐 您好：<br>"
                    + "<br>"
                    + "您的收據編號 #acRcno#  <br>"
                    + "#mailContent#"
                    + "<br>"
                    + "<br>"
                    + "註：此封郵件為系統自動發送，請勿回覆。";
            tmpcontent = tmpcontent.replaceAll("#clmName#", clmName);
            tmpcontent = tmpcontent.replaceAll("#acRcno#", acRcno);
            tmpcontent = tmpcontent.replaceAll("#mailContent#", mailContent);
            
            setSubject("拾得遺失物拾得人領回通知信");
            setReceiver(email);
            setContent(tmpcontent);
            if( mailContent.equals("") ){ //空的時候不發mail
                
            }else{
                sendMail("text/html");     //html
            }
            
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    public void setSender(String args) throws Exception{
        if(args==null||args=="")
            throw new Exception("Sender must be filled");
        sender = args;
    }

    public void setReceiver(String args) throws Exception{
        if(args==null||args=="")
            throw new Exception("Receiver must be filled");
        receiver = args;
    }

    public void setSMTP(String args) throws Exception{
        if(args==null||args=="")
            throw new Exception("SMTP must be filled");
        smtp = args;
    }
    public void setSubject(String args) throws Exception{
        if(args==null||args=="")
            throw new Exception("Subject must be filled");
        subject = args;
    }
    
    public void setContent(String content) throws Exception{
        this.content = content;
    }
    
    public void sendMail(String format) throws Exception{
        try {           
            ResourceBundle resourceBundle = ResourceBundle.getBundle("setting");
            String isSmtps = resourceBundle.getString("MAIL_IS_SMTPS");
            smtp = resourceBundle.getString("MAIL_SMTP");
            sender = resourceBundle.getString("MAIL_SENDER");
            if(isSmtps.equals("Y")){
                
            }
//            System.out.println("smtp:"+smtp+" sender:"+sender);
            Properties props = new Properties();
             
            /***需要ssl時將smtp改為smtps***/
            if(isSmtps.equals("Y")){
                props.put("mail.smtps.host", smtp);
            } else {
                props.put("mail.smtp.host", smtp);
            }
            /***************************/
//            System.out.println("props put host : "+smtp);
            /***********************↓若需要做smpt認證，則加入此段↓*****************************/
            if(isSmtps.equals("Y")){
                String smtpsPort = resourceBundle.getString("SMTPS_PORT");
                props.put("mail.smtps.port", smtpsPort);    //smtp port
                props.put("mail.smtps.auth","true");    //若需要做smpt認證，則加入此段
//                System.out.println("props put port : "+smtpsPort);
            }
            /******************************************************************************/
            
            Session sendMailSession;            
            sendMailSession = Session.getInstance(props, null);            
            MimeMessage msg = new MimeMessage(sendMailSession);
//            System.out.println("new msg...");
            msg.setFrom(new InternetAddress(sender));
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(receiver,false));
            msg.setSubject(subject,"utf8");
            msg.setSentDate(new java.util.Date());
            msg.setContent(content, format+";charset=utf8");
//            System.out.println("new msg done!");
            /**********************↓若需要做smpt認證，則加入此段↓*******************************/
            if(isSmtps.equals("Y")){
//                System.out.println("getting SMTPS properties...");
                String smtpsServer = resourceBundle.getString("SMTPS_SERVER");
                int smtpsPort = Integer.parseInt(resourceBundle.getString("SMTPS_PORT"));
                String smtpsAccount = resourceBundle.getString("SMTPS_USER");
                String smtpsPas = resourceBundle.getString("SMTPS_PWD");
                Transport transport;
//                System.out.println("Sending mail.. smtpsServer:"+smtpsServer+" smtpsPort:"+smtpsPort+" smtpsAccount:"+smtpsAccount);
                transport = sendMailSession.getTransport("smtps");
                transport.connect(smtpsServer, smtpsPort, smtpsAccount, smtpsPas);
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();
//                System.out.println("end Sending mail... msg:"+msg+" getAllRecipients:"+msg.getAllRecipients());
            }
            /******************************************************************************/

            /****不須smtp認證****/
            if(!isSmtps.equals("Y")){
                Transport.send(msg);
            }
            /*******************/
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}