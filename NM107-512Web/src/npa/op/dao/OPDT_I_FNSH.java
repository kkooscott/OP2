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
import npa.op.util.luence;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.vo.User;
import npa.op.base.BaseDao;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import java.math.BigDecimal;
import npa.op.util.OPUtil;
import static npa.op.util.StringUtil.replaceWhiteChar;

public class OPDT_I_FNSH extends BaseDao {
	
	private static OPDT_I_FNSH instance = null;
	public static OPDT_I_FNSH getInstance() {
		if (instance == null) {
			instance = new OPDT_I_FNSH();
		}
		return instance;
	}
        
        /**
	 * 查詢結案資料明細 .(DB原始資料)
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryFnshByBasicIdOriginal(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                
                String sql = " SELECT *"
                    + " FROM OPDT_I_FNSH"
                    + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.clear();
                args.add(OP_BASIC_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
        
        /**
	 * 查詢結案資料明細 .(DB原始資料)
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryFnshByBasicIdOriginalFor500(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                
                String sql = " SELECT A.OP_SEQ_NO IFNSH_OP_SEQ_NO, A.OP_FS_UNIT_CD, A.OP_FS_UNIT_NM, A.OP_FS_STAFF_ID, A.OP_FS_STAFF_NM, A.OP_FS_DATE, A.OP_FS_REC_CD,"
                           + " A.OP_FS_REC_NM, A.OP_FS_STAT_DESC,"
                           + " B.OP_SEQ_NO IPUANDL_OP_SEQ_NO, B.OP_AC_REG_ATNO, B.OP_AC_NAME, B.OP_AC_TITLE, B.OP_SD_UNIT_CD,"
                           + " B.OP_SD_UNIT_NM, B.OP_SD_NAME, B.OP_SD_TITLE, B.OP_SD_DATE, B.OP_NTC_MODE_CD, B.OP_NTC_MODE_NM"
                           + " FROM OPDT_I_FNSH A"
                           + " LEFT JOIN OPDT_I_PUAN_DL B ON A.OP_BASIC_SEQ_NO = B.OP_BASIC_SEQ_NO"
                           + " WHERE A.OP_BASIC_SEQ_NO = ? ";
                args.clear();
                args.add(OP_BASIC_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
        
        /**
	 * 查詢結案資料明細 .
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryFnshByBasicId(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
		CachedRowSet crs1,crs2;
                //結案資料
		String sql = " SELECT *"
                           + " FROM OPDT_I_FNSH"
                           + " WHERE OP_BASIC_SEQ_NO = ? ";
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
                
                if( list.size() == 0 ){ //結案資料沒有資料
                    jObject.put("ACTION_TYPE", "insertIFnshList");
                    jObject.put("OP_SEQ_NO", "");
                    jObject.put("OP_RT_UNIT_CD", "");
                    jObject.put("OP_RT_STAFF_NM", "");
                    jObject.put("OP_RT_DATE", "");
                    jObject.put("OP_FS_UNIT_CD", "");
                    jObject.put("OP_FS_STAFF_NM", "");
                    jObject.put("OP_FS_DATE", "");
                    jObject.put("OP_FS_REC_CD", "");
                    jObject.put("OP_FS_STAT_DESC", "");
                    resultDataArray.put(jObject);
                    return resultDataArray;
                }else{
                    list.get(0).put("ACTION_TYPE", "updateIFnshList");
                    String OP_RT_DATE="",OP_FS_DATE="";
                    if( !list.get(0).get("OP_RT_DATE").toString().equals("") ){
                        OP_RT_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_RT_DATE").toString());
                        list.get(0).put("OP_RT_DATE", OP_RT_DATE);
                    }
                    if( !list.get(0).get("OP_FS_DATE").toString().equals("") ){
                        OP_FS_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_FS_DATE").toString());
                        list.get(0).put("OP_FS_DATE", OP_FS_DATE);
                    }

                    return arrayList2JsonArray(list);
                }

	}
        
        /**
	 * 查詢結案資料明細 .(伍佰元專區)
	 * 
	 * @param OP_BASIC_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryFnshByBasicIdFor500(int OP_BASIC_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
                JSONArray resultDataArray = new JSONArray();
                JSONObject jObject = new JSONObject();
		CachedRowSet crs1,crs2;
                //結案資料
		String sql = " SELECT A.OP_SEQ_NO IFNSH_OP_SEQ_NO, A.OP_FS_UNIT_CD, A.OP_FS_UNIT_NM, A.OP_FS_STAFF_ID, A.OP_FS_STAFF_NM, A.OP_FS_DATE, A.OP_FS_REC_CD,"
                           + " A.OP_FS_REC_NM, A.OP_FS_STAT_DESC,"
                           + " B.OP_SEQ_NO IPUANDL_OP_SEQ_NO, B.OP_AC_REG_ATNO, B.OP_AC_NAME, B.OP_AC_TITLE, B.OP_SD_UNIT_CD,"
                           + " B.OP_SD_UNIT_NM, B.OP_SD_NAME, B.OP_SD_TITLE, B.OP_SD_DATE, B.OP_NTC_MODE_CD, B.OP_NTC_MODE_NM"
                           + " FROM OPDT_I_FNSH A"
                           + " LEFT JOIN OPDT_I_PUAN_DL B ON A.OP_BASIC_SEQ_NO = B.OP_BASIC_SEQ_NO"
                           + " WHERE A.OP_BASIC_SEQ_NO = ? ";
                args.add( OP_BASIC_SEQ_NO );
				
		ArrayList<HashMap> fnshList = this.pexecuteQuery(sql,args.toArray());
                
                if( fnshList.size() == 0 ){ //結案資料沒有資料
                    jObject.put("ACTION_TYPE", "insertIFnshList");
                    //結案資料
                    jObject.put("IFNSH_OP_SEQ_NO", "");
                    jObject.put("OP_FS_UNIT_CD", "");
                    jObject.put("OP_FS_STAFF_NM", "");
                    jObject.put("OP_FS_DATE", "");
                    jObject.put("OP_FS_REC_CD", "");
                    jObject.put("OP_FS_STAT_DESC", "");
                    
                    //送交資料
                    jObject.put("IPUANDL_OP_SEQ_NO", "");
                    jObject.put("OP_SD_UNIT_CD", "");
                    jObject.put("OP_SD_NAME", "");
                    jObject.put("OP_SD_TITLE", "");
                    jObject.put("OP_SD_DATE", "");
                    jObject.put("OP_NTC_MODE_CD", "");
                    jObject.put("OP_NTC_MODE_NM", "");
                    jObject.put("OP_AC_REG_ATNO", "");
                    jObject.put("OP_AC_NAME", "");
                    jObject.put("OP_AC_TITLE", "");
                    resultDataArray.put(jObject);
                    return resultDataArray;
                }else{
                    fnshList.get(0).put("ACTION_TYPE", "updateIFnshList");
                    String OP_RT_DATE="",OP_FS_DATE="",OP_SD_DATE="";
                    if( !fnshList.get(0).get("OP_FS_DATE").toString().equals("") ){
                        OP_FS_DATE = DateUtil.to7TwDateTime(fnshList.get(0).get("OP_FS_DATE").toString());
                        fnshList.get(0).put("OP_FS_DATE", OP_FS_DATE);
                    }
                    if( !fnshList.get(0).get("OP_SD_DATE").toString().equals("") ){
                        OP_SD_DATE = DateUtil.to7TwDateTime(fnshList.get(0).get("OP_SD_DATE").toString());
                        fnshList.get(0).put("OP_SD_DATE", OP_SD_DATE);
                    }
                    if( fnshList.get(0).get("OP_SD_UNIT_CD").toString().equals("") || fnshList.get(0).get("OP_SD_UNIT_CD") == null ){
                        fnshList.get(0).put("OP_SD_UNIT_CD", fnshList.get(0).get("OP_FS_UNIT_CD").toString());
                        fnshList.get(0).put("OP_SD_UNIT_NM", fnshList.get(0).get("OP_FS_UNIT_NM").toString());
                    }

                    return arrayList2JsonArray(fnshList);
                }

	}
        
        // region 新增 model Start
        /**
         * 新增結案資料 .
         * 
         * @param jObj
         * @return boolean
         */
        public boolean add(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_YN_FS", "1"); //1：已結案
                        if( jObj.get("ACTION_FROM").equals("CLAIM") ){//認領
                            jObj.put("OP_RT_UNIT_CD", voUser.getUnitCd());
                            jObj.put("OP_RT_UNIT_NM", voUser.getUnitName());
                            jObj.put("OP_RT_STAFF_ID", voUser.getUserId());
                            jObj.put("OP_RT_STAFF_NM", voUser.getUserName());
                            jObj.put("OP_RT_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
                            jObj.put("OP_FS_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
                        }else{
                            if (!jObj.get("OP_RT_UNITLEVEL4").equals("")){
                                    jObj.put("OP_RT_UNIT_CD", jObj.get("OP_RT_UNITLEVEL4"));
                            }else if (!jObj.get("OP_RT_UNITLEVEL3").equals("")){
                                    jObj.put("OP_RT_UNIT_CD", jObj.get("OP_RT_UNITLEVEL3"));
                            }else if (!jObj.get("OP_RT_UNITLEVEL2").equals("")){
                                    jObj.put("OP_RT_UNIT_CD", jObj.get("OP_RT_UNITLEVEL2"));
                            }
                            jObj.put("OP_RT_UNIT_NM", jObj.get("OP_RT_UNITLEVEL_NM"));
                            //歸還人員之後需要改下拉選單
//                            jObj.put("OP_RT_STAFF_ID", jObj.get("OP_RT_STAFF_ID"));
//                            jObj.put("OP_RT_STAFF_NM", jObj.get("OP_RT_STAFF_NM"));
                            jObj.put("OP_RT_STAFF_ID", jObj.get("OP_RT_STAFF_ID"));
                            jObj.put("OP_RT_STAFF_NM", jObj.get("OP_RT_STAFF_NM"));
                            
                            if( jObj.get("OP_RT_DATE").equals("") ){
                                jObj.put("OP_RT_DATE", "");
                            }else{
                                jObj.put("OP_RT_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_RT_DATE").toString()));
                            }
                            if( jObj.get("OP_FS_DATE").equals("") ){
                                jObj.put("OP_FS_DATE", "");
                            }else{
                                jObj.put("OP_FS_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FS_DATE").toString()));
                            }
                        }
               
                        jObj.put("OP_FS_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_FS_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_FS_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_FS_STAFF_NM", voUser.getUserName());
                        
                        
                        if( jObj.has("OP_FS_REC_CD") ){ 
                            if( jObj.get("OP_FS_REC_CD").equals("") ){ 
                                jObj.put("OP_FS_REC_CD", "");
                            }else{
                                jObj.put("OP_FS_REC_CD", jObj.get("OP_FS_REC_CD"));
                            }
                        }else{
                            jObj.put("OP_FS_REC_CD", "01");
                        }
                        
                        if( jObj.has("OP_FS_REC_NM") ){ 
                            if( jObj.get("OP_FS_REC_NM").equals("") ){ 
                                jObj.put("OP_FS_REC_NM", "");
                            }else{
                                jObj.put("OP_FS_REC_NM", jObj.get("OP_FS_REC_NM"));
                            }
                        }else{
                            jObj.put("OP_FS_REC_NM", "遺失人領回");
                        }
                        
                        if( jObj.has("OP_FS_STAT_DESC") ){ 
                            if( jObj.get("OP_FS_STAT_DESC").equals("") ){ 
                                jObj.put("OP_FS_STAT_DESC", "");
                            }else{
                                jObj.put("OP_FS_STAT_DESC", jObj.get("OP_FS_STAT_DESC"));
                            }
                        }else{
                            jObj.put("OP_FS_STAT_DESC", "");
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
                                        "OP_YN_FS",
                                        "OP_RT_UNIT_CD", //5
                                        "OP_RT_UNIT_NM",
                                        "OP_RT_STAFF_ID",
                                        "OP_RT_STAFF_NM",
                                        "OP_RT_DATE",
                                        "OP_FS_UNIT_CD", //10
                                        "OP_FS_UNIT_NM",
                                        "OP_FS_STAFF_ID",
                                        "OP_FS_STAFF_NM",
                                        "OP_FS_DATE",
                                        "OP_FS_REC_CD", //15
                                        "OP_FS_REC_NM",
                                        "OP_FS_STAT_DESC",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD", //20
                                        "OP_ADD_UNIT_NM",
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD", //25
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //27

                        sql.append("INSERT INTO OPDT_I_FNSH ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?,?,?,?)"); //26

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
                                boolean fnshbol = false;
                                jObj.put("OP_CURSTAT_CD", "6");
                                jObj.put("OP_CURSTAT_NM", "已結案");
                                fnshbol = iPuBasicDao.updateBasicForStatus( jObj );
                                
                                if( fnshbol ){ //將未審核之有認領權人 更改為否
                                    OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                                    iPuClaimDao.updateClaimSetNo( jObj );
                                }
                                
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
         * 新增結案資料 .(伍佰元專區)
         * 
         * @param jObj
         * @return boolean
         */
        public boolean addFor500(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                OPDT_I_PUAN_DL iPuanDlDao = new OPDT_I_PUAN_DL();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_YN_FS", "1"); //1：已結案
                        
                        jObj.put("OP_RT_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_RT_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_RT_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_RT_STAFF_NM", voUser.getUserName());
                        jObj.put("OP_RT_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
                        
                        if( jObj.get("ACTION_FROM").equals("CLAIM") ){//認領
                            jObj.put("OP_FS_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
                        }else{
                            if( jObj.get("OP_FS_DATE").equals("") ){
                                jObj.put("OP_FS_DATE", "");
                            }else{
                                jObj.put("OP_FS_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FS_DATE").toString()));
                            }
                        }
                        
               
                        jObj.put("OP_FS_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_FS_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_FS_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_FS_STAFF_NM", voUser.getUserName());
                        
                        if( jObj.has("OP_FS_REC_CD") ){ 
                            if( jObj.get("OP_FS_REC_CD").equals("") ){ 
                                jObj.put("OP_FS_REC_CD", "");
                            }else{
                                jObj.put("OP_FS_REC_CD", jObj.get("OP_FS_REC_CD"));
                            }
                        }else{
                            jObj.put("OP_FS_REC_CD", "01");
                        }
                        
                        if( jObj.has("OP_FS_REC_NM") ){ 
                            if( jObj.get("OP_FS_REC_NM").equals("") ){ 
                                jObj.put("OP_FS_REC_NM", "");
                            }else{
                                jObj.put("OP_FS_REC_NM", jObj.get("OP_FS_REC_NM"));
                            }
                        }else{
                            jObj.put("OP_FS_REC_NM", "遺失人領回");
                        }
                        
                        if( jObj.has("OP_FS_STAT_DESC") ){ 
                            if( jObj.get("OP_FS_STAT_DESC").equals("") ){ 
                                jObj.put("OP_FS_STAT_DESC", "");
                            }else{
                                jObj.put("OP_FS_STAT_DESC", jObj.get("OP_FS_STAT_DESC"));
                            }
                        }else{
                            jObj.put("OP_FS_STAT_DESC", "");
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
                                        "OP_YN_FS",
                                        "OP_RT_UNIT_CD", //5
                                        "OP_RT_UNIT_NM",
                                        "OP_RT_STAFF_ID",
                                        "OP_RT_STAFF_NM",
                                        "OP_RT_DATE",
                                        "OP_FS_UNIT_CD", //10
                                        "OP_FS_UNIT_NM",
                                        "OP_FS_STAFF_ID",
                                        "OP_FS_STAFF_NM",
                                        "OP_FS_DATE",
                                        "OP_FS_REC_CD", //15
                                        "OP_FS_REC_NM",
                                        "OP_FS_STAT_DESC",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD", //20
                                        "OP_ADD_UNIT_NM",
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD", //25
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //27

                        sql.append("INSERT INTO OPDT_I_FNSH ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?,?,?,?)"); //26

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
                        
                        boolean iPuanDlCheck = iPuanDlDao.addFor500( jObj );
                        
                        if ( insetkey > 0 && iPuanDlCheck == true){
                            
                                boolean fnshbol = false;
                                jObj.put("OP_CURSTAT_CD", "6");
                                jObj.put("OP_CURSTAT_NM", "已結案");
                                fnshbol = iPuBasicDao.updateBasicForStatus( jObj );
                                
                                if( fnshbol ){ //將未審核之有認領權人 更改為否
                                    OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                                    iPuClaimDao.updateClaimSetNo( jObj );
                                }
                                
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
         * 批次結案資料 .
         * 
         * @param jObj
         * @return boolean
         */
        public int batchAdd(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
//                boolean returnValue = false;
                int returnValue = 0;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        String[] tmpSEQ_UPL = jObj.get("OP_SEQ_NO").toString().split(",");
                        String[] tmpRCNO_UPL = jObj.get("OP_AC_RCNO").toString().split(",");
                        
                        jObj.put("OP_YN_FS", "1"); //1：已結案
                        jObj.put("OP_RT_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_RT_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_RT_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_RT_STAFF_NM", voUser.getUserName());
                        jObj.put("OP_RT_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());
                        jObj.put("OP_FS_DATE", new SimpleDateFormat("yyyyMMdd").format(current).toString());

                        jObj.put("OP_FS_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_FS_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_FS_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_FS_STAFF_NM", voUser.getUserName());
                        if( jObj.get("OP_FS_REC_CD").equals("") ){ 
                            jObj.put("OP_FS_REC_CD", "");
                        }else{
                            jObj.put("OP_FS_REC_CD", jObj.get("OP_FS_REC_CD"));
                        }
                        if( jObj.get("OP_FS_REC_NM").equals("") ){ 
                            jObj.put("OP_FS_REC_NM", "");
                        }else{
                            jObj.put("OP_FS_REC_NM", jObj.get("OP_FS_REC_NM"));
                        }
                        jObj.put("OP_FS_STAT_DESC", jObj.get("OP_FS_STAT_DESC"));
                        
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
                                        "OP_YN_FS",
                                        "OP_RT_UNIT_CD", //5
                                        "OP_RT_UNIT_NM",
                                        "OP_RT_STAFF_ID",
                                        "OP_RT_STAFF_NM",
                                        "OP_RT_DATE",
                                        "OP_FS_UNIT_CD", //10
                                        "OP_FS_UNIT_NM",
                                        "OP_FS_STAFF_ID",
                                        "OP_FS_STAFF_NM",
                                        "OP_FS_DATE",
                                        "OP_FS_REC_CD", //15
                                        "OP_FS_REC_NM",
                                        "OP_FS_STAT_DESC",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD", //20
                                        "OP_ADD_UNIT_NM",
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD", //25
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //27
                                                    
                        sql.append("INSERT INTO OPDT_I_FNSH ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                           .append(" VALUES  (")
                           .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                           .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                           .append(" ?,?,?,?,?,?)"); //26
                        
                        for(int tmpSeqUplCount=0 ; tmpSeqUplCount < tmpSEQ_UPL.length ; tmpSeqUplCount++){
                                jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf(tmpSEQ_UPL[tmpSeqUplCount]));
                                jObj.put("OP_AC_RCNO", tmpRCNO_UPL[tmpSeqUplCount]);

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

                                int  insetkey = getIdentityAfterInsert(sql.toString(),paraObject);
                                System.out.println(insetkey);
                                if ( insetkey > 0 ){
                                        boolean fnshbol = false;
                                        jObj.put("OP_CURSTAT_CD", "6");
                                        jObj.put("OP_CURSTAT_NM", "已結案");
                                        fnshbol = iPuBasicDao.updateBasicForStatus( jObj );
                                        
                                        if( fnshbol ){ //將未審核之有認領權人 更改為否
                                            OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                                            iPuClaimDao.updateClaimSetNo( jObj );
                                        }
                                        returnValue = returnValue + 1;
                                }else{
                                        returnValue = returnValue;
                                }
                            
                        }

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;
        }
        // endregion 新增 model END
        
        // region 更新 Model Start
        /**
         * 更新結案資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean update(JSONObject jObj) {

                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        
                        if (!jObj.get("OP_RT_UNITLEVEL4").equals("")){
                                jObj.put("OP_RT_UNIT_CD", jObj.get("OP_RT_UNITLEVEL4"));
                        }else if (!jObj.get("OP_RT_UNITLEVEL3").equals("")){
                                jObj.put("OP_RT_UNIT_CD", jObj.get("OP_RT_UNITLEVEL3"));
                        }else if (!jObj.get("OP_RT_UNITLEVEL2").equals("")){
                                jObj.put("OP_RT_UNIT_CD", jObj.get("OP_RT_UNITLEVEL2"));
                        }
                        jObj.put("OP_RT_UNIT_NM", jObj.get("OP_RT_UNITLEVEL_NM"));
                        //歸還人員之後需要改下拉選單
//                            jObj.put("OP_RT_STAFF_ID", jObj.get("OP_RT_STAFF_ID"));
//                            jObj.put("OP_RT_STAFF_NM", jObj.get("OP_RT_STAFF_NM"));
                        jObj.put("OP_RT_STAFF_ID", jObj.get("OP_RT_STAFF_ID"));
                        jObj.put("OP_RT_STAFF_NM", jObj.get("OP_RT_STAFF_NM"));
                        if( jObj.get("OP_RT_DATE").equals("") ){
                            jObj.put("OP_RT_DATE", "");
                        }else{
                            jObj.put("OP_RT_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_RT_DATE").toString()));
                        }
                        
                        jObj.put("OP_FS_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_FS_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_FS_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_FS_STAFF_NM", voUser.getUserName());
                        if( jObj.get("OP_FS_DATE").equals("") ){
                            jObj.put("OP_FS_DATE", "");
                        }else{
                            jObj.put("OP_FS_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FS_DATE").toString()));
                        }
                        
                        jObj.put("OP_FS_REC_CD", jObj.get("OP_FS_REC_CD"));
                        jObj.put("OP_FS_REC_NM", jObj.get("OP_FS_REC_NM"));
                        jObj.put("OP_FS_STAT_DESC", jObj.get("OP_FS_STAT_DESC"));
  
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_RT_UNIT_CD",
                                        "OP_RT_UNIT_NM", //5
                                        "OP_RT_STAFF_ID",
                                        "OP_RT_STAFF_NM",
                                        "OP_RT_DATE",
                                        "OP_FS_UNIT_CD",
                                        "OP_FS_UNIT_NM", //10
                                        "OP_FS_STAFF_ID",
                                        "OP_FS_STAFF_NM",
                                        "OP_FS_DATE",
                                        "OP_FS_REC_CD",
                                        "OP_FS_REC_NM", //15
                                        "OP_FS_STAT_DESC",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM", //20
                                        "OP_UPD_DT_TM"}; //21

                        sql.append("UPDATE OPDT_I_FNSH SET ");

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
                                
                        if( returnValue ){ //將未審核之有認領權人 更改為否
                            OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                            iPuClaimDao.updateClaimSetNo( jObj );
                        }
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        // endregion 更新 Model END
        
        // region 更新 Model Start
        /**
         * 更新結案資料. (伍佰元專區)
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean updateFor500(JSONObject jObj) {
                OPDT_I_PUAN_DL iPuanDlDao = new OPDT_I_PUAN_DL();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        
                        jObj.put("OP_FS_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_FS_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_FS_STAFF_ID", voUser.getUserId());
                        jObj.put("OP_FS_STAFF_NM", voUser.getUserName());
                        if( jObj.get("OP_FS_DATE").equals("") ){
                            jObj.put("OP_FS_DATE", "");
                        }else{
                            jObj.put("OP_FS_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FS_DATE").toString()));
                        }
                        
                        jObj.put("OP_FS_REC_CD", jObj.get("OP_FS_REC_CD"));
                        jObj.put("OP_FS_REC_NM", jObj.get("OP_FS_REC_NM"));
                        jObj.put("OP_FS_STAT_DESC", jObj.get("OP_FS_STAT_DESC"));
  
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_FS_UNIT_CD",
                                        "OP_FS_UNIT_NM", //10
                                        "OP_FS_STAFF_ID",
                                        "OP_FS_STAFF_NM",
                                        "OP_FS_DATE",
                                        "OP_FS_REC_CD",
                                        "OP_FS_REC_NM", //15
                                        "OP_FS_STAT_DESC",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM", //20
                                        "OP_UPD_DT_TM"}; //21

                        sql.append("UPDATE OPDT_I_FNSH SET ");

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
                        paraObject[arryString.length] = jObj.getInt("IFNSH_OP_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
                        
                        boolean iPuanDlCheck = iPuanDlDao.addFor500( jObj );
                        
                        if( returnValue && iPuanDlCheck ){
                            returnValue = true;
                            if( returnValue ){ //將未審核之有認領權人 更改為否
                                OPDT_I_PU_CLAIM iPuClaimDao = new OPDT_I_PU_CLAIM();
                                iPuClaimDao.updateClaimSetNo( jObj );
                            }
                        }else{
                            returnValue = false;
                        }
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        
                        

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        // endregion 更新 Model END
        
        // region 刪除 Model Start
        /**
         * 刪除遺失物受理明細資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean delete(JSONObject jObj) {
                boolean returnValue = false;
                int resultCount = 0;
		String sqlDelete = "DELETE FROM OPDT_I_FNSH WHERE OP_BASIC_SEQ_NO=? " ;
		resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{jObj.getInt("OP_BASIC_SEQ_NO")});
                if(resultCount > 0){
                    returnValue = true;
                }
                return returnValue;
	}
        // endregion 刪除 Model END
	
}
