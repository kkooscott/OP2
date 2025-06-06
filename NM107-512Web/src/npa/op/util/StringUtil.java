package npa.op.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class StringUtil {

	
	public static String nvl(Object o){
		String tmp="";
		if(o != null && !o.equals("null")){
			tmp=o.toString().trim();
		}
		return tmp;
	}

	/**
	 * 將資料庫的查詢結果轉成JSONArray.
	 * 
	 * @param list
	 * @return JSONArray
	 */
	public JSONArray arrayList2JsonArray(final ArrayList<HashMap> list) {
		JSONArray jArray = new JSONArray();
		JSONObject jObj;
		for (HashMap<String, String> e : list) {
			jObj = new JSONObject();
			for (String col : e.keySet()) {
				jObj.put(col, e.get(col));
			}
			jArray.put(jObj);
		}
		return jArray;
	}
	
	/**
     * 長度不足部份補零回傳
     * @param str 字串
     * @param lenSize 字串所需長度
     * @return 回傳補零後字串
     */
    public String MakesUpZero(String str, int lenSize) {
        String zero = "0000000000";
        String returnValue = zero;
 
        returnValue = zero + str;
 
        return returnValue.substring(returnValue.length() - lenSize);
 
    }
    
    /**
     * 將字串中的半全形空白去掉
     */
    public static String replaceWhiteChar(String inString) {
		String strTmp = inString;
		String[] arrWhiteChar = { " ", "@" };

		if (null == strTmp) {
			return "";
		}

		for (int idx = 0; idx < arrWhiteChar.length; idx++) {
			strTmp = strTmp.replaceAll(arrWhiteChar[idx], "");
		}

		return strTmp;
    }
    
    /*
    全形轉半形
    */
    public static String full2half(String str){
        for(char c:str.toCharArray()){
            str = str.replaceAll("　", " ");
            if((int)c >= 65281 && (int)c <= 65374){
                str = str.replace(c, (char)(((int)c)-65248));
            }
            str = str.replaceAll("：", "_");
            str = str.replaceAll(":", "_");
            str = str.replaceAll("%", "_");
            str = str.replaceAll("#", "_");
            str = str.replaceAll("\\+", "_");
        }
        return str;
    }
    
    /**
     * 將性別代碼轉換為中文
     */
    public String getSexCB(String inString) {

		if (null == inString || "".equals(inString)) {
			return "";
		}
		else if ("1".equals(inString)){
			return "男";
		}
		else {
			return "女";
		}
    }
    
    public static String getString(Object s) {
        if (s == null) {
            return "";
        } else {
            return trimSpace(s.toString().trim());
        }
    }
    
    /**
     *去字串前後全形與半形空白
     */
    public static String trimSpace(String strIn) {
        if (strIn != null && !strIn.equals("")) {
            int r = strIn.length();
            int l = 0;
            while (r != 0 && (strIn.substring(r - 1, r).equals("　") || strIn.substring(r - 1, r).equals(" "))) {
                r--;
            }
            while (l < r && (strIn.substring(l, l + 1).equals("　") || strIn.substring(l, l + 1).equals(" "))) {
                l++;
            }
            strIn = strIn.substring(l, r);
        } else {
            strIn = "";
        }

        return strIn.trim();
    }
}
