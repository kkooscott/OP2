package npa.op.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import npa.op.util.DaoUtil;
import npa.op.util.DateUtil;
import npa.op.util.ExceptionUtil;
import npa.op.util.NPALOG2Util;
//import npa.op.util.luence;  //202402拿掉不用到的luence
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.vo.User;
import npa.op.base.BaseDao;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import npa.op.util.OPUtil;

public class OPDT_I_PUAN_DL extends BaseDao {
	
	private static OPDT_I_PUAN_DL instance = null;
	public static OPDT_I_PUAN_DL getInstance() {
		if (instance == null) {
			instance = new OPDT_I_PUAN_DL();
		}
		return instance;
	}
        
        /**
	 * 查詢拾得人公告期滿資料明細 .(DB原始資料)
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryPuanDlByBasicIdOriginal(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                
                String sql = " SELECT *"
                    + " FROM OPDT_I_PUAN_DL"
                    + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.clear();
                args.add(OP_BASIC_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}

	/**
	 * 查詢拾得人公告期滿資料明細 .
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryPuanDlByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
		CachedRowSet crs1,crs2;
                //招領期滿
		String sql = " SELECT *"
                           + " FROM OPDT_I_PUAN_DL"
                           + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
                
                if( list.size() == 0 ){ //拾得人公告期滿沒有資料
                    jObject.put("ACTION_TYPE", "insertIPuanDlList");
                    jObject.put("OP_SEQ_NO", "");
                    jObject.put("OP_SD_UNIT_CD", "");
                    jObject.put("OP_SD_NAME", "");
                    jObject.put("OP_SD_TITLE", "");
                    jObject.put("OP_SD_DATE", "");
                    jObject.put("OP_AC_REG_ATNO", "");
                    jObject.put("OP_AC_NAME", "");
                    jObject.put("OP_AC_TITLE", "");
                    resultDataArray.put(jObject);
                    return resultDataArray;
                }else{
                    list.get(0).put("ACTION_TYPE", "updateIPuanDlList");
                    String OP_SD_DATE="";
                    if( !list.get(0).get("OP_SD_DATE").toString().equals("") ){
                        OP_SD_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_SD_DATE").toString());
                        list.get(0).put("OP_SD_DATE", OP_SD_DATE);
                    }

                    return arrayList2JsonArray(list);
                }

	}
        
        // region 新增 model Start
        /**
         * 新增拾得人公告期滿資料明細 .
         * 
         * @param jObj
         * @return boolean
         */
        public boolean add(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean bool = false;
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_PUPO_YN_INPR", "0"); //0 or 空白or null；未輸入
                        jObj.put("OP_AC_REG_ATNO", jObj.get("OP_AC_REG_ATNO"));
                        jObj.put("OP_AC_NAME", jObj.get("OP_AC_NAME"));
                        jObj.put("OP_AC_TITLE", jObj.get("OP_AC_TITLE"));

                        if (!jObj.get("OP_SD_UNITLEVEL4").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL4"));
                        }else if (!jObj.get("OP_SD_UNITLEVEL3").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL3"));
                        }else if (!jObj.get("OP_SD_UNITLEVEL2").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL2"));
                        }
                        jObj.put("OP_SD_UNIT_NM", jObj.get("OP_SD_UNITLEVEL_NM"));
                        jObj.put("OP_SD_NAME", voUser.getUserName());
                        jObj.put("OP_SD_TITLE", voUser.getUserTitle());
                        if( jObj.get("OP_SD_DATE").equals("") ){
                            jObj.put("OP_SD_DATE", "");
                        }else{
                            jObj.put("OP_SD_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_SD_DATE").toString()));
                        }
                        jObj.put("OP_NTC_MODE_CD", "0");
                        jObj.put("OP_NTC_MODE_NM", "");
                        
                        jObj.put("OP_ADD_ID", voUser.getUserId());
                        jObj.put("OP_ADD_NM", voUser.getUserName());
                        jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_ADD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
   

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "ADD");
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_PUPO_YN_INPR",
                                        "OP_AC_REG_ATNO", //5
                                        "OP_AC_NAME",
                                        "OP_AC_TITLE",
                                        "OP_SD_UNIT_CD",
                                        "OP_SD_UNIT_NM",
                                        "OP_SD_NAME", //10
                                        "OP_SD_TITLE",
                                        "OP_SD_DATE",
                                        "OP_NTC_MODE_CD",
                                        "OP_NTC_MODE_NM",
                                        "OP_ADD_ID", //15
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD",
                                        "OP_ADD_UNIT_NM",
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID", //20
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //24

                        sql.append("INSERT INTO OPDT_I_PUAN_DL ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?)"); //21

                        Object[] paraObject = new Object[arryString.length];

                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        if ("OP_SEQ_NO".equals(strKey)){
                                                if (!"".equals(jObj.get(strKey).toString().trim()))
                                                        paraObject[i] = Integer.parseInt(jObj.get(strKey).toString());
                                                else {
                                                        paraObject[i] = 0;
                                                }
                                        }
                                        else{
                                                paraObject[i] = jObj.get(strKey);
                                        }
                                i++;
                        }

                        int  insetkey =getIdentityAfterInsert(sql.toString(),paraObject);
                        System.out.println(insetkey);
                        if ( insetkey > 0 ){
                                jObj.put("OP_SEQ_NO", insetkey);
                                jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                                jObj.put("OP_CURSTAT_CD", "5");
                                jObj.put("OP_CURSTAT_NM", "拾得人領回公告期滿");
                                iPuBasicDao.updateBasicForStatus(jObj);
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;
        }
        // endregion 新增 model END
        
        // region 新增 model Start
        /**
         * 新增拾得人公告期滿資料明細 . (伍佰元專區)
         * 
         * @param jObj
         * @return boolean
         */
        public boolean addFor500(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean bool = false;
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_PUPO_YN_INPR", "0"); //0 or 空白or null；未輸入
                        if( jObj.has("OP_AC_REG_ATNO") ){
                            jObj.put("OP_AC_REG_ATNO", jObj.get("OP_AC_REG_ATNO"));
                        }else{
                            jObj.put("OP_AC_REG_ATNO", "");
                        }
                        if( jObj.has("OP_AC_NAME") ){
                            jObj.put("OP_AC_NAME", jObj.get("OP_AC_NAME"));
                        }else{
                            jObj.put("OP_AC_NAME", "");
                        }
                        if( jObj.has("OP_AC_TITLE") ){
                            jObj.put("OP_AC_TITLE", jObj.get("OP_AC_TITLE"));
                        }else{
                            jObj.put("OP_AC_TITLE", "");
                        }
                        
                        if( jObj.get("ACTION_FROM").equals("CLAIM") ){//認領
                            jObj.put("OP_SD_UNIT_CD", voUser.getUnitCd());
                            jObj.put("OP_SD_UNIT_NM", voUser.getUnitName());
                            jObj.put("OP_SD_NAME", voUser.getUserName());
                            jObj.put("OP_SD_TITLE", voUser.getUserTitle());
                            jObj.put("OP_SD_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
                            jObj.put("OP_NTC_MODE_CD", "1");
                            jObj.put("OP_NTC_MODE_NM", "Email通知");
                        }else{
                            if (!jObj.get("OP_SD_UNITLEVEL4").equals("")){
                                    jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL4"));
                            }else if (!jObj.get("OP_SD_UNITLEVEL3").equals("")){
                                    jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL3"));
                            }else if (!jObj.get("OP_SD_UNITLEVEL2").equals("")){
                                    jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL2"));
                            }
                            jObj.put("OP_SD_UNIT_NM", jObj.get("OP_SD_UNITLEVEL_NM"));
                            jObj.put("OP_SD_NAME", jObj.get("OP_SD_NAME"));
                            jObj.put("OP_SD_TITLE", jObj.get("OP_SD_TITLE"));
                            if( jObj.get("OP_SD_DATE").equals("") ){
                                jObj.put("OP_SD_DATE", "");
                            }else{
                                jObj.put("OP_SD_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_SD_DATE").toString()));
                            }
                            jObj.put("OP_NTC_MODE_CD", jObj.get("OP_NTC_MODE_CD"));
                            if( jObj.get("OP_NTC_MODE_CD").equals("1") ){
                                jObj.put("OP_NTC_MODE_NM", "Email通知");
                            }else if( jObj.get("OP_NTC_MODE_CD").equals("2") ){
                                jObj.put("OP_NTC_MODE_NM", "電話通知");
                            }else if( jObj.get("OP_NTC_MODE_CD").equals("3") ){
                                jObj.put("OP_NTC_MODE_NM", "書面通知");
                            }
                        }
                        
                                
                        jObj.put("OP_ADD_ID", voUser.getUserId());
                        jObj.put("OP_ADD_NM", voUser.getUserName());
                        jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_ADD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
   

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "ADD");
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_PUPO_YN_INPR",
                                        "OP_AC_REG_ATNO", //5
                                        "OP_AC_NAME",
                                        "OP_AC_TITLE",
                                        "OP_SD_UNIT_CD",
                                        "OP_SD_UNIT_NM",
                                        "OP_SD_NAME", //10
                                        "OP_SD_TITLE",
                                        "OP_SD_DATE",
                                        "OP_NTC_MODE_CD",
                                        "OP_NTC_MODE_NM",
                                        "OP_ADD_ID", //15
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD",
                                        "OP_ADD_UNIT_NM",
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID", //20
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //24

                        sql.append("INSERT INTO OPDT_I_PUAN_DL ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?)"); //23

                        Object[] paraObject = new Object[arryString.length];

                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        if ("OP_SEQ_NO".equals(strKey)){
                                                if (!"".equals(jObj.get(strKey).toString().trim()))
                                                        paraObject[i] = Integer.parseInt(jObj.get(strKey).toString());
                                                else {
                                                        paraObject[i] = 0;
                                                }
                                        }
                                        else{
                                                paraObject[i] = jObj.get(strKey);
                                        }
                                i++;
                        }

                        int  insetkey =getIdentityAfterInsert(sql.toString(),paraObject);
                        System.out.println("###OPDT_I_PUAN_DL insetkey =" + insetkey);
                        if ( insetkey > 0 ){
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;
        }
        // endregion 新增 model END
        
        // region 更新 Model Start
        /**
         * 更新拾得人公告期滿資料明細.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean update(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_REG_ATNO", jObj.get("OP_AC_REG_ATNO"));
                        jObj.put("OP_AC_NAME", jObj.get("OP_AC_NAME"));
                        jObj.put("OP_AC_TITLE", jObj.get("OP_AC_TITLE"));

                        if (!jObj.get("OP_SD_UNITLEVEL4").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL4"));
                        }else if (!jObj.get("OP_SD_UNITLEVEL3").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL3"));
                        }else if (!jObj.get("OP_SD_UNITLEVEL2").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL2"));
                        }
                        jObj.put("OP_SD_UNIT_NM", jObj.get("OP_SD_UNITLEVEL_NM"));
                        jObj.put("OP_SD_NAME", voUser.getUserName());
                        jObj.put("OP_SD_TITLE", voUser.getUserTitle());
                        if( jObj.get("OP_SD_DATE").equals("") ){
                            jObj.put("OP_SD_DATE", "");
                        }else{
                            jObj.put("OP_SD_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_SD_DATE").toString()));
                        }
                        
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_AC_REG_ATNO",
                                        "OP_AC_NAME",
                                        "OP_AC_TITLE",
                                        "OP_SD_UNIT_CD", //5
                                        "OP_SD_UNIT_NM",
                                        "OP_SD_NAME",
                                        "OP_SD_TITLE",
                                        "OP_SD_DATE",
                                        "OP_UPD_ID", //10
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //14

                        sql.append("UPDATE OPDT_I_PUAN_DL SET ");

                        for (int index = 0 ;index < arryString.length ;index++){
                                sql.append(arryString[index] + "=?, ");
                        }
                        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                        sql.append("WHERE OP_SEQ_NO =? ");


                        Object[] paraObject = new Object[arryString.length+1];

                        // paramenter
                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        paraObject[i] = jObj.get(strKey);
                                i++;
                        }
                        //paraObject[arryString.length+1] = new Object[arryString.length+1];
                        paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
                        
                        if ( returnValue ){
                                jObj.put("OP_BASIC_SEQ_NO", jObj.get("OP_BASIC_SEQ_NO"));
                                jObj.put("OP_CURSTAT_CD", "5");
                                jObj.put("OP_CURSTAT_NM", "拾得人領回公告期滿");
                                iPuBasicDao.updateBasicForStatus(jObj);
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        
        // region 更新 Model Start
        /**
         * 更新拾得人公告期滿資料明細. (伍佰元專區)
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean updateFor500(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_REG_ATNO", jObj.get("OP_AC_REG_ATNO"));
                        jObj.put("OP_AC_NAME", jObj.get("OP_AC_NAME"));
                        jObj.put("OP_AC_TITLE", jObj.get("OP_AC_TITLE"));

                        if (!jObj.get("OP_SD_UNITLEVEL4").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL4"));
                        }else if (!jObj.get("OP_SD_UNITLEVEL3").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL3"));
                        }else if (!jObj.get("OP_SD_UNITLEVEL2").equals("")){
                                jObj.put("OP_SD_UNIT_CD", jObj.get("OP_SD_UNITLEVEL2"));
                        }
                        jObj.put("OP_SD_UNIT_NM", jObj.get("OP_SD_UNITLEVEL_NM"));
                        jObj.put("OP_SD_NAME", voUser.getUserName());
                        jObj.put("OP_SD_TITLE", voUser.getUserTitle());
                        if( jObj.get("OP_SD_DATE").equals("") ){
                            jObj.put("OP_SD_DATE", "");
                        }else{
                            jObj.put("OP_SD_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_SD_DATE").toString()));
                        }
                        jObj.put("OP_NTC_MODE_CD", jObj.get("OP_NTC_MODE_CD"));
                        if( jObj.get("OP_NTC_MODE_CD").equals("1") ){
                            jObj.put("OP_NTC_MODE_NM", "Email通知");
                        }else if( jObj.get("OP_NTC_MODE_CD").equals("2") ){
                            jObj.put("OP_NTC_MODE_NM", "電話通知");
                        }else if( jObj.get("OP_NTC_MODE_CD").equals("3") ){
                            jObj.put("OP_NTC_MODE_NM", "書面通知");
                        }
                        
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_AC_REG_ATNO",
                                        "OP_AC_NAME",
                                        "OP_AC_TITLE",
                                        "OP_SD_UNIT_CD", //5
                                        "OP_SD_UNIT_NM",
                                        "OP_SD_NAME",
                                        "OP_SD_TITLE",
                                        "OP_SD_DATE",
                                        "OP_NTC_MODE_CD", //10
                                        "OP_NTC_MODE_NM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM", //15
                                        "OP_UPD_DT_TM"}; //16

                        sql.append("UPDATE OPDT_I_PUAN_DL SET ");

                        for (int index = 0 ;index < arryString.length ;index++){
                                sql.append(arryString[index] + "=?, ");
                        }
                        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                        sql.append("WHERE OP_SEQ_NO =? ");


                        Object[] paraObject = new Object[arryString.length+1];

                        // paramenter
                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        paraObject[i] = jObj.get(strKey);
                                i++;
                        }
                        //paraObject[arryString.length+1] = new Object[arryString.length+1];
                        paraObject[arryString.length] = jObj.getInt("IPUANDL_OP_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
                        
                        if ( returnValue ){
                                returnValue = true;
                        }else{
                                returnValue = false;
                        }
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        	
}
