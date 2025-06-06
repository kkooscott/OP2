package npa.op.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;

import npa.op.vo.User;
import npa.op.control.UtilServlet;
import npa.op.dao.GetAcRcnoDao;
import npa.op.util.StringUtil;
import static npa.op.util.StringUtil.*;
import npa.op.util.DateUtil;

import org.apache.log4j.Logger;
import org.json.JSONObject;
//20220308 Wennie
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class OPUtil {

    private static Logger log = Logger.getLogger(UtilServlet.class);    
    /**
     * 取得新的收據編號
     * 
     * 
     * @return "OP" + YYYMM + 受理單位代碼5碼 + 5位數字(取受理當月該受理單位的流水號) 2020/08/19 Wennie modify
     */
    public static String getNewRCNO(User voUser){
        GetAcRcnoDao daoGetAcRcno = GetAcRcnoDao.getInstance();
        Calendar cal = Calendar.getInstance();
        String strYear = Integer.toString(cal.get(Calendar.YEAR) - 1911);
        String strMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String strPaddingChar = "0";
        String strYM = paddingLeft(strYear, 3, strPaddingChar) + paddingLeft(strMonth, 2, strPaddingChar);
        String strSerialNo = "";
        // 目前時間
        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String YYYYMM = sdf.format(now);
        //先設流水號為0
        int OP_MAX_PRMNO = 0;
        //產生流水號============開始
        OP_MAX_PRMNO = daoGetAcRcno.getAcRcNo(YYYYMM, voUser.getUnitCd(), voUser.getUnitName());
        //取到流水號後補0到五位數
        strSerialNo = String.format("%05d", OP_MAX_PRMNO);
        //利用儲存時間來產生流水號============開始
//        String CodeSequence = "0123456789ABCDFGHJKLMNPQRSTUVXYZ";
//        int dd = cal.get(Calendar.DAY_OF_MONTH);
//        int hh = cal.get(Calendar.HOUR_OF_DAY);
//        int mm = cal.get(Calendar.MINUTE);
//        int ss = cal.get(Calendar.SECOND);
//        
//        // 計算從當月1號0時0分0秒到目前經過多少秒
//        int totalSecond = (dd-1)*86400 + hh*3600 + mm*60 + ss;
//        // 將秒數轉成 2 進位字串，並將字串長度補成為 25
//        String strSecond = paddingLeft(Integer.toBinaryString(totalSecond), 25, "0");
//        // 將 2 進位字串切成長度為 5 的字串，並計算其值
//        int tmp = 0;
//        for( int i=0; i<5; i++){
//            tmp = Integer.parseInt(strSecond.substring(i*5, (i+1)*5), 2);
//            strSerialNo += CodeSequence.charAt(tmp);
//        }
        
        return "OP" + getCurrCDate().substring(0,5) + voUser.getUnitCd() + strSerialNo;
    }
    public static String paddingLeft(String strIn, int intDigit, String strPaddingChar) {
        if (strIn.length() == 0) {
            return "";
        }
        if (strPaddingChar.equals("")) {
            strPaddingChar = " ";
        }
        StringBuffer sbAdd = new StringBuffer();
        if (strIn.length() < intDigit) {
            for (int i = 0; i < intDigit - strIn.length(); i++) {
                sbAdd.append(strPaddingChar);
            }
            strIn = sbAdd.toString() + strIn;
        }
        return strIn;
    }
    public static String getCurrCDate() {
        Calendar cal = Calendar.getInstance();
        return paddingLeft(Integer.toString(cal.get(Calendar.YEAR) - 1911), 3,
                "0")
                + paddingLeft(Integer.toString(cal.get(Calendar.MONTH) + 1), 2,
                "0")
                + paddingLeft(Integer.toString(cal.get(Calendar.DATE)), 2, "0");
    }
    public Properties getProperties(){
        Properties properties = new Properties();
        String configFile = "npa.properties";
        
        try {
            properties.load(new FileInputStream((this.getClass().getResource("/").getPath() + configFile).replace("%20", " ")));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            log.error(ExceptionUtil.toString(ex));
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ExceptionUtil.toString(ex));
        }
        
        return properties;
    }
    
}
