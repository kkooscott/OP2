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
import npa.op.util.MailSender;
import npa.op.util.OPUtil;
import npa.op.util.StringUtil;

public class OPDT_I_PU_CLAIM extends BaseDao {
	
	private static OPDT_I_PU_CLAIM instance = null;
	public static OPDT_I_PU_CLAIM getInstance() {
		if (instance == null) {
			instance = new OPDT_I_PU_CLAIM();
		}
		return instance;
	}
        
        /**
        * 跨轄查詢認領資料 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet CompositeSearchForClaim(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //基本資料序號, 姓名, 證號, 電話, EMAIL, 收據號碼, 單位名稱,填表日期, 來自哪個table
                String ClaimSql = " SELECT OP_BASIC_SEQ_NO, OP_PUCP_NAME, OP_PUCP_IDN, OP_PUCP_PHONENO, OP_PUCP_EMAIL, OP_AC_RCNO, OP_AC_UNIT_NM,"
                                + " OP_FM_DATE, 'Claim' as TABLEFROM"
                                + " FROM "
                                + " OPDT_I_PU_CLAIM "
                                + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '2' AND OP_CLAIM_TP_NM = '臨櫃認領'";
                
                String NetClmSql = " SELECT OP_BASIC_SEQ_NO, OP_PUCP_NAME, OP_PUCP_IDN, OP_PUCP_PHONENO, OP_PUCP_EMAIL, OP_AC_RCNO, OP_AC_UNIT_NM,"
                                 + " OP_FM_DATE, 'NetClm' as TABLEFROM"
                                 + " FROM "
                                 + " OPDT_E_NET_CLAIM "
                                 + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '1' AND OP_CLAIM_TP_NM = '上網認領'";
                String strQuery = "SELECT U.*,ISNULL(A.OP_FS_STAFF_NM,'') as OP_FS_STAFF_NM, ISNULL(A.OP_FS_DATE,'') as OP_FS_DATE FROM ( "
                                + ClaimSql
                                + " UNION "
                                + NetClmSql
                                + ") U "
                                + " LEFT JOIN OPDT_I_FNSH A ON A.OP_BASIC_SEQ_NO = U.OP_BASIC_SEQ_NO"
                                + " WHERE 1 = 1 ";
                
                //認領人姓名
                if (!jObj.get("OP_PUCP_NAME").equals("")){
                    strQuery += " AND U.OP_PUCP_NAME = ?";
                    args.add( jObj.getString("OP_PUCP_NAME"));
                }
                //認領人身分證號
                if (!jObj.get("OP_PUCP_IDN").equals("")){
                    strQuery += " AND U.OP_PUCP_IDN = ?";
                    args.add( jObj.getString("OP_PUCP_IDN"));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(strQuery,args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢該收據編號認領資料 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryAllForClaim(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //序號, 基本資料序號, 收據號碼, 填表日期, 認領方式, 姓名, 姓名羅馬拼音, 證號,      //2019/8/12 增加姓名羅馬拼音
                //遺失日期, 遺失時間, 遺失地點, 是否為有認領權人, 來自哪個table
                String ClaimSql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_FM_DATE, OP_CLAIM_TP_NM, OP_PUCP_NAME, OP_PUCP_RNAME, OP_PUCP_IDN,"
                                + " OP_LOST_DATE, OP_LOST_TIME, OP_LOST_PLACE, OP_YN_LOSER, 'Claim' as TABLEFROM"
                                + " FROM "
                                + " OPDT_I_PU_CLAIM "
                                + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '2' AND OP_CLAIM_TP_NM = '臨櫃認領'";
                
                String NetClmSql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_FM_DATE, OP_CLAIM_TP_NM, OP_PUCP_NAME, OP_PUCP_RNAME, OP_PUCP_IDN,"
                                 + " OP_LOST_DATE, OP_LOST_TIME, OP_LOST_PLACE, OP_YN_LOSER, 'NetClm' as TABLEFROM"
                                 + " FROM "
                                 + " OPDT_E_NET_CLAIM "
                                 + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '1' AND OP_CLAIM_TP_NM = '上網認領'";
                String strQuery = "SELECT * FROM ( "
                                + ClaimSql
                                + " UNION "
                                + NetClmSql
                                + ") U "
                                + " WHERE 1 = 1 ";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    strQuery += " AND U.OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(strQuery,args.toArray());
                
                return crs;
        }
        
        /**
        * 篩選認領人報表 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet GetClaimReportList(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //序號, 基本資料序號, 收據號碼, 填表日期, 認領方式, 姓名, 證號,
                //遺失日期, 遺失時間, 遺失地點, 是否為有認領權人, 來自哪個table
                String ClaimSql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_FM_DATE, OP_CLAIM_TP_NM, OP_PUCP_NAME, OP_PUCP_IDN,"
                                + " OP_LOST_DATE, OP_LOST_TIME, OP_LOST_PLACE, OP_YN_LOSER, 'Claim' as TABLEFROM"
                                + " FROM "
                                + " OPDT_I_PU_CLAIM "
                                + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '2' AND OP_CLAIM_TP_NM = '臨櫃認領'";
                
                String NetClmSql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_FM_DATE, OP_CLAIM_TP_NM, OP_PUCP_NAME, OP_PUCP_IDN,"
                                 + " OP_LOST_DATE, OP_LOST_TIME, OP_LOST_PLACE, OP_YN_LOSER, 'NetClm' as TABLEFROM"
                                 + " FROM "
                                 + " OPDT_E_NET_CLAIM "
                                 + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '1' AND OP_CLAIM_TP_NM = '上網認領'";
                String strQuery = "SELECT * FROM ( "
                                + ClaimSql
                                + " UNION "
                                + NetClmSql
                                + ") U "
                                + " WHERE 1 = 1 ";

                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    strQuery += " AND U.OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }
                
                strQuery += " AND U.OP_YN_LOSER IN ('1','2')";

                CachedRowSet crs = this.pexecuteQueryRowSet(strQuery,args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢該收據編號是否有認領權人 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet checkLostForPuCaim(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                String sql = " SELECT *"                           
                           + " FROM "
                           + " OPDT_I_PU_CLAIM "
                           + " WHERE 1 = 1 ";
                
                sql += " AND OP_YN_LOSER = '1'";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    sql += " AND OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }
                
                //認領序號
                if (!jObj.get("OP_SEQ_NO").equals("") && jObj.get("OP_CLAIM_TP_CD").equals("2")){
                    sql += " AND OP_SEQ_NO <> ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_SEQ_NO")));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢該收據編號是否已經有重複認領人 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet checkYNPuClaim(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //序號, 受理收據編號, 拾得物品名稱, 數量, 單位, 特徵,
                String sql = " SELECT *"                           
                           + " FROM "
                           + " OPDT_I_PU_CLAIM "
                           + " WHERE 1 = 1 ";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    sql += " AND OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }
                
                //認領人序號
                if ( jObj.has("OP_SEQ_NO") ){
                    if ( !jObj.get("OP_SEQ_NO").equals("") ){
                        sql += " AND OP_SEQ_NO <> ?";
                        args.add( Integer.valueOf((String) jObj.get("OP_SEQ_NO")));
                    }
                }
                
                //認領人姓名
                if ( jObj.has("OP_PUCP_NAME") ){
                    sql += " AND OP_PUCP_NAME = ?";
                    args.add( jObj.get("OP_PUCP_NAME"));
                }
                
                //認領人證號
                if ( jObj.has("OP_PUCP_IDN") ){
                    sql += " AND OP_PUCP_IDN = ?";
                    args.add( jObj.get("OP_PUCP_IDN"));
                }
                
                //認領人生日
                if ( jObj.has("OP_PUCP_BIRTHDT") ){
                    sql += " AND OP_PUCP_BIRTHDT = ?";
                    args.add( jObj.get("OP_PUCP_BIRTHDT"));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        //region 明細
	/**
	 * 查詢認領資料明細 .
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryClaimById(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_I_PU_CLAIM"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
                String OP_FM_DATE = "", OP_PUCP_BIRTHDT = "", OP_LOST_DATE = "", OP_LOST_TIME = "";
                OP_FM_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_FM_DATE").toString());
		list.get(0).put("OP_FM_DATE", OP_FM_DATE);
                if( list.get(0).get("OP_PUCP_BEFROC").toString().equals("2") ){ //西元
                    //直接出去
                }else{ //民前 民國
                    OP_PUCP_BIRTHDT = DateUtil.convertBirthdayTime(list.get(0).get("OP_PUCP_BIRTHDT").toString(), list.get(0).get("OP_PUCP_BEFROC").toString());
                    list.get(0).put("OP_PUCP_BIRTHDT", OP_PUCP_BIRTHDT);
                }
                OP_LOST_DATE = DateUtil.to7TwDateTime(list.get(0).get("OP_LOST_DATE").toString());
		list.get(0).put("OP_LOST_DATE", OP_LOST_DATE);
                OP_LOST_TIME = DateUtil.to4TwTime(list.get(0).get("OP_LOST_TIME").toString());
		list.get(0).put("OP_LOST_TIME", OP_LOST_TIME);
                
		return arrayList2JsonArray(list);
	}
        
        //region 明細
	/**
	 * 查詢認領資料明細 .(DB原始資料)
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryClaimByIdOriginal(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_I_PU_CLAIM"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
        
        // region 新增 model Start
        /**
         * 新增遺失物認領資料 .
         * 
         * @param jObj
         * @return boolean
         */
        public boolean add(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                OPDT_I_ANNOUNCE iAnnDao = new OPDT_I_ANNOUNCE();
                OPDT_I_FNSH iFnshDao = new OPDT_I_FNSH();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        int BASIC_SEQ_NO =  Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO"));
                        jObj.put("OP_BASIC_SEQ_NO", BASIC_SEQ_NO);
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        
                        JSONArray resultDataArray = iPuBasicDao.queryBasicById( BASIC_SEQ_NO );
                        jObj.put("OP_AC_UNIT_CD", resultDataArray.getJSONObject(0).get("OP_AC_UNIT_CD"));
			jObj.put("OP_AC_UNIT_NM", resultDataArray.getJSONObject(0).get("OP_AC_UNIT_NM"));
                        
                        resultDataArray = iAnnDao.queryAnnounceByBasicIdOriginal( BASIC_SEQ_NO );
                        if( resultDataArray != null && !resultDataArray.equals("") && resultDataArray.length() > 0 ){
                            jObj.put("OP_AN_UNIT_CD", resultDataArray.getJSONObject(0).get("OP_AN_UNIT_CD"));
                            jObj.put("OP_AN_UNIT_NM", resultDataArray.getJSONObject(0).get("OP_AN_UNIT_NM"));
                        }else{
                            jObj.put("OP_AN_UNIT_CD", "");
                            jObj.put("OP_AN_UNIT_NM", "");
                        }
//                        if( jObj.has("OP_AN_UNIT_CD") ){
//                            jObj.put("OP_AN_UNIT_CD", jObj.get("OP_AN_UNIT_CD"));
//                        }else{
//                            jObj.put("OP_AN_UNIT_CD", "");
//                        }
//                        if( jObj.has("OP_AN_UNIT_NM") ){
//                            jObj.put("OP_AN_UNIT_NM", jObj.get("OP_AN_UNIT_NM"));
//                        }else{
//                            jObj.put("OP_AN_UNIT_NM", "");
//                        }
                        if( jObj.get("OP_FM_DATE").equals("") ){
                            jObj.put("OP_FM_DATE", "");
                        }else{
                            jObj.put("OP_FM_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FM_DATE").toString()));
                        }                    
           
                        jObj.put("OP_PUCP_NAME", jObj.get("OP_PUCP_NAME"));
                        if( jObj.has("OP_PUCP_RNAME") ){
                            jObj.put("OP_PUCP_RNAME", jObj.get("OP_PUCP_RNAME"));
                        }else{
                            jObj.put("OP_PUCP_RNAME", "");
                        }
                       
                        jObj.put("OP_PUCP_IDN_TP", jObj.get("OP_PUCP_IDN_TP"));
                        jObj.put("OP_PUCP_IDN", jObj.get("OP_PUCP_IDN"));
                        jObj.put("OP_PUCP_GENDER", jObj.get("OP_PUCP_GENDER"));
                        jObj.put("OP_PUCP_BEFROC", jObj.get("OP_PUCP_BEFROC"));
                        if( jObj.get("OP_PUCP_BIRTHDT").equals("") ){
                            jObj.put("OP_PUCP_BIRTHDT", "");
                        }else{
                            if( jObj.get("OP_PUCP_BEFROC").equals("2") ){ //西元
                                jObj.put("OP_PUCP_BIRTHDT", jObj.get("OP_PUCP_BIRTHDT"));
                            }else{ //民前 民國
                                jObj.put("OP_PUCP_BIRTHDT", DateUtil.toNormalDateTime(jObj.get("OP_PUCP_BIRTHDT").toString(), jObj.get("OP_PUCP_BEFROC").toString()));
                            }
                        }
                        jObj.put("OP_PUCP_NAT_CD", jObj.get("OP_PUCP_NAT_CD"));
                        jObj.put("OP_PUCP_NAT_NM", jObj.get("OP_PUCP_NAT_NM"));
                        jObj.put("OP_PUCP_ZIPCODE", jObj.get("OP_PUCP_ZIPCODE"));
                        jObj.put("OP_PUCP_ADDR_TYPE_CD", jObj.get("OP_PUCP_ADDR_TYPE_CD"));
                        if ( jObj.get("OP_PUCP_ADDR_TYPE_CD").equals("1") ){ //一般地址
                            jObj.put("OP_PUCP_ADDR_TYPE_NM", "一般地址");
                            jObj.put("OP_PUCP_CITY_CD", jObj.get("OP_PUCP_CITY_CD"));
                            jObj.put("OP_PUCP_CITY_NM", jObj.get("OP_PUCP_CITY_NM"));
                            jObj.put("OP_PUCP_TOWN_CD", jObj.get("OP_PUCP_TOWN_CD"));
                            jObj.put("OP_PUCP_TOWN_NM", jObj.get("OP_PUCP_TOWN_NM"));
                            jObj.put("OP_PUCP_VILLAGE_CD", jObj.get("OP_PUCP_VILLAGE_CD"));
                            jObj.put("OP_PUCP_VILLAGE_NM", jObj.get("OP_PUCP_VILLAGE_NM"));
                            jObj.put("OP_PUCP_LIN", jObj.get("OP_PUCP_LIN"));
                            jObj.put("OP_PUCP_ROAD", jObj.get("OP_PUCP_ROAD"));
                            jObj.put("OP_PUCP_ADDR_OTH", "");
                        }else{ //自由輸入
                            jObj.put("OP_PUCP_ADDR_TYPE_NM", "其他");
                            jObj.put("OP_PUCP_CITY_CD", "");
                            jObj.put("OP_PUCP_CITY_NM", "");
                            jObj.put("OP_PUCP_TOWN_CD", "");
                            jObj.put("OP_PUCP_TOWN_NM", "");
                            jObj.put("OP_PUCP_VILLAGE_CD", "");
                            jObj.put("OP_PUCP_VILLAGE_NM", "");
                            jObj.put("OP_PUCP_LIN", "");
                            jObj.put("OP_PUCP_ROAD", "");
                            jObj.put("OP_PUCP_ADDR_OTH", jObj.get("OP_PUCP_ADDR_OTH")); 
                        }
                        jObj.put("OP_PUCP_PHONENO", jObj.get("OP_PUCP_PHONENO"));
                        jObj.put("OP_PUCP_EMAIL", jObj.get("OP_PUCP_EMAIL"));
           
                        if( jObj.get("OP_LOST_DATE").equals("") ){
                            jObj.put("OP_LOST_DATE", "");
                        }else{
                            jObj.put("OP_LOST_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_LOST_DATE").toString()));
                        } 
                        jObj.put("OP_LOST_TIME", jObj.get("OP_LOST_TIME").toString().replace(":",""));
                        jObj.put("OP_LOST_CITY_CD", jObj.get("OP_LOST_CITY_CD"));
                        jObj.put("OP_LOST_CITY_NM", jObj.get("OP_LOST_CITY_NM"));
                        jObj.put("OP_LOST_TOWN_CD", jObj.get("OP_LOST_TOWN_CD"));
                        jObj.put("OP_LOST_TOWN_NM", jObj.get("OP_LOST_TOWN_NM"));
                        jObj.put("OP_LOST_PLACE", jObj.get("OP_LOST_PLACE"));
                        jObj.put("OP_LOST_OJ_DESC", jObj.get("OP_LOST_OJ_DESC"));
                        jObj.put("OP_REMARK", jObj.get("OP_REMARK"));
                        jObj.put("OP_CLAIM_TP_CD", jObj.get("OP_CLAIM_TP_CD"));
                        jObj.put("OP_CLAIM_TP_NM", jObj.get("OP_CLAIM_TP_NM"));
                        jObj.put("OP_YN_LOSER", jObj.get("OP_YN_LOSER"));
                        
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
                                        "OP_AC_UNIT_CD",
                                        "OP_AC_UNIT_NM", //5
                                        "OP_AN_UNIT_CD",
                                        "OP_AN_UNIT_NM",
                                        "OP_FM_DATE",
                                        "OP_PUCP_NAME",
                                        "OP_PUCP_RNAME", //10
                                        "OP_PUCP_IDN_TP",
                                        "OP_PUCP_IDN",
                                        "OP_PUCP_GENDER",
                                        "OP_PUCP_BEFROC",
                                        "OP_PUCP_BIRTHDT", //15
                                        "OP_PUCP_NAT_CD",
                                        "OP_PUCP_NAT_NM",
                                        "OP_PUCP_ZIPCODE",
                                        "OP_PUCP_ADDR_TYPE_CD",
                                        "OP_PUCP_ADDR_TYPE_NM", //20
                                        "OP_PUCP_CITY_CD",
                                        "OP_PUCP_CITY_NM",
                                        "OP_PUCP_TOWN_CD",
                                        "OP_PUCP_TOWN_NM",
                                        "OP_PUCP_VILLAGE_CD",//25
                                        "OP_PUCP_VILLAGE_NM",
                                        "OP_PUCP_LIN",
                                        "OP_PUCP_ROAD",
                                        "OP_PUCP_ADDR_OTH",
                                        "OP_PUCP_PHONENO", //30
                                        "OP_PUCP_EMAIL",
                                        "OP_LOST_DATE",
                                        "OP_LOST_TIME",
                                        "OP_LOST_CITY_CD",
                                        "OP_LOST_CITY_NM", //35
                                        "OP_LOST_TOWN_CD",
                                        "OP_LOST_TOWN_NM",
                                        "OP_LOST_PLACE",
                                        "OP_LOST_OJ_DESC",
                                        "OP_REMARK", //40
                                        "OP_CLAIM_TP_CD",
                                        "OP_CLAIM_TP_NM",
                                        "OP_YN_LOSER",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM", //45
                                        "OP_ADD_UNIT_CD",
                                        "OP_ADD_UNIT_NM",
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM", //50
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //53

                        sql.append("INSERT INTO OPDT_I_PU_CLAIM ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //30
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //40
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //50
                        .append(" ?,?)"); //52

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
                            if( jObj.get("OP_YN_LOSER").equals("2") ){  //若確定不是有認領權人 寄Email
                                MailSender mailSender = new MailSender();
                                if( !jObj.get("OP_PUCP_EMAIL").toString().trim().equals("") ){
                                    String OP_MAIL_CONTENT = "";
                                    CachedRowSet crs1 =null;
                                    JSONObject jObject = new JSONObject();
                                    jObject.put("OP_D_UNIT_CD", voUser.getUnitCd1());
                                    jObject.put("OP_B_UNIT_CD", voUser.getUnitCd2());
                                    SystemDao systemDao = SystemDao.getInstance();
                                    crs1 = systemDao.checkUnitForMail(jObject);
                                    if ( crs1.next() ){
                                        OP_MAIL_CONTENT = crs1.getString("OP_MAIL_CONTENT");
                                    }
                                    if( !OP_MAIL_CONTENT.trim().equals("") ){
                                        mailSender.doClmCancel(jObj.get("OP_PUCP_NAME").toString(), 
                                            jObj.get("OP_AC_RCNO").toString(), 
                                            OP_MAIL_CONTENT, 
                                            jObj.get("OP_PUCP_EMAIL").toString());
                                    }
                                }
                                
                            }
                            
                            if( jObj.has("OP_YN_FS") ){ //是否立即結案
                                if( jObj.get("OP_YN_FS").equals("1") ){ //是
                                    boolean fnshbol = false;
                                    //BASIC狀態更改成功後，新增一筆至結案table
                                    jObj.put("OP_BASIC_SEQ_NO", String.valueOf(BASIC_SEQ_NO));
                                    jObj.put("OP_YN_FS", "1");
                                    jObj.put("ACTION_FROM", "CLAIM"); //認領
                                    if( jObj.get("ACTION").equals("OP08A02Q_01.jsp") ){ //伍佰元專區
                                        fnshbol = iFnshDao.addFor500(jObj );
                                    }else{
                                        fnshbol = iFnshDao.add( jObj );
                                    }
                                    if( fnshbol ){ //將未審核之有認領權人 更改為否
                                        updateClaimSetNo( jObj );
                                    }
                                }
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
        
        // region 更新 Model Start
        /**
         * 更新遺失物認領資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean update(JSONObject jObj) {
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();
                OPDT_I_ANNOUNCE iAnnDao = new OPDT_I_ANNOUNCE();
                OPDT_I_FNSH iFnshDao = new OPDT_I_FNSH();
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        
                        
                        if( jObj.get("OP_FM_DATE").equals("") ){
                            jObj.put("OP_FM_DATE", "");
                        }else{
                            jObj.put("OP_FM_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_FM_DATE").toString()));
                        }
                        int BASIC_SEQ_NO =  Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO"));
                        JSONArray resultDataArray = iAnnDao.queryAnnounceByBasicIdOriginal( BASIC_SEQ_NO );
                        if( resultDataArray != null && !resultDataArray.equals("") && resultDataArray.length() > 0 ){
                            jObj.put("OP_AN_UNIT_CD", resultDataArray.getJSONObject(0).get("OP_AN_UNIT_CD"));
                            jObj.put("OP_AN_UNIT_NM", resultDataArray.getJSONObject(0).get("OP_AN_UNIT_NM"));
                        }else{
                            jObj.put("OP_AN_UNIT_CD", "");
                            jObj.put("OP_AN_UNIT_NM", "");
                        }
           
                        jObj.put("OP_PUCP_NAME", jObj.get("OP_PUCP_NAME"));
                        if( jObj.has("OP_PUCP_RNAME") ){
                            jObj.put("OP_PUCP_RNAME", jObj.get("OP_PUCP_RNAME"));
                        }else{
                            jObj.put("OP_PUCP_RNAME", "");
                        }
                        jObj.put("OP_PUCP_IDN_TP", jObj.get("OP_PUCP_IDN_TP"));
                        jObj.put("OP_PUCP_IDN", jObj.get("OP_PUCP_IDN"));
                        jObj.put("OP_PUCP_GENDER", jObj.get("OP_PUCP_GENDER"));
                        jObj.put("OP_PUCP_BEFROC", jObj.get("OP_PUCP_BEFROC"));
                        if( jObj.get("OP_PUCP_BIRTHDT").equals("") ){
                            jObj.put("OP_PUCP_BIRTHDT", "");
                        }else{
                            if( jObj.get("OP_PUCP_BEFROC").equals("2") ){ //西元
                                jObj.put("OP_PUCP_BIRTHDT", jObj.get("OP_PUCP_BIRTHDT"));
                            }else{ //民前 民國
                                jObj.put("OP_PUCP_BIRTHDT", DateUtil.toNormalDateTime(jObj.get("OP_PUCP_BIRTHDT").toString(), jObj.get("OP_PUCP_BEFROC").toString()));
                            }
                        }
                        jObj.put("OP_PUCP_NAT_CD", jObj.get("OP_PUCP_NAT_CD"));
                        jObj.put("OP_PUCP_NAT_NM", jObj.get("OP_PUCP_NAT_NM"));
                        jObj.put("OP_PUCP_ZIPCODE", jObj.get("OP_PUCP_ZIPCODE"));
                        jObj.put("OP_PUCP_ADDR_TYPE_CD", jObj.get("OP_PUCP_ADDR_TYPE_CD"));
                        if ( jObj.get("OP_PUCP_ADDR_TYPE_CD").equals("1") ){ //一般地址
                            jObj.put("OP_PUCP_ADDR_TYPE_NM", "一般地址");
                            jObj.put("OP_PUCP_CITY_CD", jObj.get("OP_PUCP_CITY_CD"));
                            jObj.put("OP_PUCP_CITY_NM", jObj.get("OP_PUCP_CITY_NM"));
                            jObj.put("OP_PUCP_TOWN_CD", jObj.get("OP_PUCP_TOWN_CD"));
                            jObj.put("OP_PUCP_TOWN_NM", jObj.get("OP_PUCP_TOWN_NM"));
                            jObj.put("OP_PUCP_VILLAGE_CD", jObj.get("OP_PUCP_VILLAGE_CD"));
                            jObj.put("OP_PUCP_VILLAGE_NM", jObj.get("OP_PUCP_VILLAGE_NM"));
                            jObj.put("OP_PUCP_LIN", jObj.get("OP_PUCP_LIN"));
                            jObj.put("OP_PUCP_ROAD", jObj.get("OP_PUCP_ROAD"));
                            jObj.put("OP_PUCP_ADDR_OTH", "");
                        }else{ //自由輸入
                            jObj.put("OP_PUCP_ADDR_TYPE_NM", "其他");
                            jObj.put("OP_PUCP_CITY_CD", "");
                            jObj.put("OP_PUCP_CITY_NM", "");
                            jObj.put("OP_PUCP_TOWN_CD", "");
                            jObj.put("OP_PUCP_TOWN_NM", "");
                            jObj.put("OP_PUCP_VILLAGE_CD", "");
                            jObj.put("OP_PUCP_VILLAGE_NM", "");
                            jObj.put("OP_PUCP_LIN", "");
                            jObj.put("OP_PUCP_ROAD", "");
                            jObj.put("OP_PUCP_ADDR_OTH", jObj.get("OP_PUCP_ADDR_OTH")); 
                        }
                        jObj.put("OP_PUCP_PHONENO", jObj.get("OP_PUCP_PHONENO"));
                        jObj.put("OP_PUCP_EMAIL", jObj.get("OP_PUCP_EMAIL"));
           
                        if( jObj.get("OP_LOST_DATE").equals("") ){
                            jObj.put("OP_LOST_DATE", "");
                        }else{
                            jObj.put("OP_LOST_DATE", DateUtil.get8UsDateFormatDB(jObj.get("OP_LOST_DATE").toString()));
                        } 
                        jObj.put("OP_LOST_TIME", jObj.get("OP_LOST_TIME").toString().replace(":",""));
                        jObj.put("OP_LOST_CITY_CD", jObj.get("OP_LOST_CITY_CD"));
                        jObj.put("OP_LOST_CITY_NM", jObj.get("OP_LOST_CITY_NM"));
                        jObj.put("OP_LOST_TOWN_CD", jObj.get("OP_LOST_TOWN_CD"));
                        jObj.put("OP_LOST_TOWN_NM", jObj.get("OP_LOST_TOWN_NM"));
                        jObj.put("OP_LOST_PLACE", jObj.get("OP_LOST_PLACE"));
                        jObj.put("OP_LOST_OJ_DESC", jObj.get("OP_LOST_OJ_DESC"));
                        jObj.put("OP_REMARK", jObj.get("OP_REMARK"));
                        jObj.put("OP_CLAIM_TP_CD", jObj.get("OP_CLAIM_TP_CD"));
                        jObj.put("OP_CLAIM_TP_NM", jObj.get("OP_CLAIM_TP_NM"));
                        jObj.put("OP_YN_LOSER", jObj.get("OP_YN_LOSER"));          
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_FM_DATE",
                                        "OP_AN_UNIT_CD",
                                        "OP_AN_UNIT_NM",
                                        "OP_PUCP_NAME", //5
                                        "OP_PUCP_RNAME",
                                        "OP_PUCP_IDN_TP",
                                        "OP_PUCP_IDN",
                                        "OP_PUCP_GENDER",
                                        "OP_PUCP_BEFROC", //10
                                        "OP_PUCP_BIRTHDT",
                                        "OP_PUCP_NAT_CD",
                                        "OP_PUCP_NAT_NM",
                                        "OP_PUCP_ZIPCODE",
                                        "OP_PUCP_ADDR_TYPE_CD", //15
                                        "OP_PUCP_ADDR_TYPE_NM",
                                        "OP_PUCP_CITY_CD",
                                        "OP_PUCP_CITY_NM",
                                        "OP_PUCP_TOWN_CD",
                                        "OP_PUCP_TOWN_NM", //20
                                        "OP_PUCP_VILLAGE_CD",
                                        "OP_PUCP_VILLAGE_NM",
                                        "OP_PUCP_LIN",
                                        "OP_PUCP_ROAD",
                                        "OP_PUCP_ADDR_OTH", //25
                                        "OP_PUCP_PHONENO",
                                        "OP_PUCP_EMAIL",
                                        "OP_LOST_DATE",
                                        "OP_LOST_TIME",
                                        "OP_LOST_CITY_CD", //30
                                        "OP_LOST_CITY_NM",
                                        "OP_LOST_TOWN_CD",
                                        "OP_LOST_TOWN_NM",
                                        "OP_LOST_PLACE",
                                        "OP_LOST_OJ_DESC", //35
                                        "OP_REMARK",
                                        "OP_CLAIM_TP_CD",
                                        "OP_CLAIM_TP_NM",
                                        "OP_YN_LOSER",
                                        "OP_UPD_ID", //40
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //44

                        sql.append("UPDATE OPDT_I_PU_CLAIM SET ");

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
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_CLAIM_TP_CD", jObj.get("OP_CLAIM_TP_CD"));
                        if ( returnValue ){
                            
                            if( jObj.get("OP_YN_LOSER").equals("2") ){  //若確定不是有認領權人 寄Email
                                MailSender mailSender = new MailSender();
                                if( !jObj.get("OP_PUCP_EMAIL").toString().trim().equals("") ){
                                    String OP_MAIL_CONTENT = "";
                                    CachedRowSet crs1 =null;
                                    JSONObject jObject = new JSONObject();
                                    jObject.put("OP_D_UNIT_CD", voUser.getUnitCd1());
                                    jObject.put("OP_B_UNIT_CD", voUser.getUnitCd2());
                                    SystemDao systemDao = SystemDao.getInstance();
                                    crs1 = systemDao.checkUnitForMail(jObject);
                                    if ( crs1.next() ){
                                        OP_MAIL_CONTENT = crs1.getString("OP_MAIL_CONTENT");
                                    }
                                    if( !OP_MAIL_CONTENT.equals("") ){
                                        mailSender.doClmCancel(jObj.get("OP_PUCP_NAME").toString(), 
                                            jObj.get("OP_AC_RCNO").toString(), 
                                            OP_MAIL_CONTENT, 
                                            jObj.get("OP_PUCP_EMAIL").toString());
                                    }
                                }
                                
                            }
                            
                            if( jObj.has("OP_YN_FS") ){ //是否立即結案
                                if( jObj.get("OP_YN_FS").equals("1") ){ //是
                                    boolean fnshbol = false;
                                    //BASIC狀態更改成功後，新增一筆至結案table
                                    jObj.put("OP_YN_FS", "1");
                                    jObj.put("ACTION_FROM", "CLAIM"); //認領
                                    if( jObj.get("ACTION").equals("OP08A02Q_01.jsp") ){ //伍佰元專區
                                        fnshbol = iFnshDao.addFor500(jObj );
                                    }else{
                                        fnshbol = iFnshDao.add( jObj );
                                    }
                                    
                                    if( fnshbol ){ //將未審核之有認領權人 更改為否
                                        updateClaimSetNo( jObj );
                                    }
                                }
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
		String sqlDelete = "DELETE FROM OPDT_I_PU_CLAIM WHERE OP_SEQ_NO=? " ;
		resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{jObj.getInt("OP_SEQ_NO")});
                if(resultCount > 0)
                    returnValue = true;
                return returnValue;
	}
        // endregion 刪除 Model END
        
        /**
         * 更新遺失物受理伍佰元上下資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean updateBasicYnOv500(JSONObject jObj) {

                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();

                try {
                        User voUser = new User();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));          
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_PU_YN_OV500",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD", //5
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //7

                        sql.append("UPDATE OPDT_I_PU_BASIC SET ");

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
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
                        jObj.put("OP_PU_YN_OV500", jObj.get("OP_PU_YN_OV500"));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        // endregion 更新 Model END
        
        /**
         * 批次更新有認領權人設定為否.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean updateClaimSetNo(JSONObject jObj) {

                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                StringBuilder NetSql = new StringBuilder();
                Date current = new Date();
                ArrayList args = new ArrayList();
                try {
                        User voUser = new User();
                        voUser = (User) jObj.get("userVO");
                        //先寄信給要更新為否的有認領權人
                        String ClaimSql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_PUCP_NAME, OP_PUCP_EMAIL"
                                        + " FROM OPDT_I_PU_CLAIM"
                                        + " WHERE 1 = 1 AND OP_YN_LOSER IN ('0', '', null)"
                                        + " AND OP_CLAIM_TP_CD = '2' AND OP_CLAIM_TP_NM = '臨櫃認領' ";
                        String NetClmSql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_PUCP_NAME, OP_PUCP_EMAIL"
                                         + " FROM OPDT_E_NET_CLAIM "
                                         + " WHERE 1 = 1 AND OP_YN_LOSER IN ('0', '', null)"
                                         + " AND OP_CLAIM_TP_CD = '1' AND OP_CLAIM_TP_NM = '上網認領'";
                        String strQuery = "SELECT * FROM ( "
                                        + ClaimSql
                                        + " UNION "
                                        + NetClmSql
                                        + ") U "
                                        + " WHERE 1 = 1 "
                                        + " AND U.OP_BASIC_SEQ_NO = ?";
                        args.add( jObj.getInt("OP_BASIC_SEQ_NO") );
                        CachedRowSet crs = this.pexecuteQueryRowSet(strQuery,args.toArray());
                        
                        MailSender mailSender = new MailSender();
                        
                        String OP_MAIL_CONTENT = "";
                        CachedRowSet crs1 = null;
                        JSONObject jObject = new JSONObject();
                        jObject.put("OP_D_UNIT_CD", voUser.getUnitCd1());
                        jObject.put("OP_B_UNIT_CD", voUser.getUnitCd2());
                        SystemDao systemDao = SystemDao.getInstance();
                        crs1 = systemDao.checkUnitForMail(jObject);
                        //確認是否有寫維護信件
                        if ( crs1.next() ){
                            OP_MAIL_CONTENT = crs1.getString("OP_MAIL_CONTENT");
                        }
                        if( !OP_MAIL_CONTENT.trim().equals("") ){ //有寫維護信件才寄信
                            while (crs.next()) {
                                if( !crs.getString("OP_PUCP_EMAIL").trim().equals("") ){
                                    mailSender.doClmCancel( crs.getString("OP_PUCP_NAME"), 
                                    crs.getString("OP_AC_RCNO"), 
                                    OP_MAIL_CONTENT, 
                                    crs.getString("OP_PUCP_EMAIL"));
                                }
                            }
                        }
                        
                        jObj.put("OP_YN_LOSER", "2"); //否      
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_YN_LOSER",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD", //5
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM"}; //7

                        sql.append("UPDATE OPDT_I_PU_CLAIM SET ");
                        NetSql.append("UPDATE OPDT_E_NET_CLAIM SET ");

                        for (int index = 0 ;index < arryString.length ;index++){
                                sql.append(arryString[index] + "=?, ");
                                NetSql.append(arryString[index] + "=?, ");
                        }
                        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                        NetSql.replace(NetSql.lastIndexOf(","), NetSql.lastIndexOf(",")+1, "");
                        
                        sql.append("WHERE OP_BASIC_SEQ_NO =? AND OP_YN_LOSER IN ('0', '', null) AND OP_CLAIM_TP_CD = '2' AND OP_CLAIM_TP_NM = '臨櫃認領'");
                        NetSql.append("WHERE OP_BASIC_SEQ_NO =? AND OP_YN_LOSER IN ('0', '', null) AND OP_CLAIM_TP_CD = '1' AND OP_CLAIM_TP_NM = '上網認領'");


                        Object[] paraObject = new Object[arryString.length+1];

                        // paramenter
                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        paraObject[i] = jObj.get(strKey);
                                i++;
                        }
                        paraObject[arryString.length] = jObj.getInt("OP_BASIC_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
                        returnValue = this.pexecuteUpdate(NetSql.toString(), paraObject) > 0 ? true : false;
                        returnValue = true;
                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                        returnValue = false;
                }
                return returnValue;
        }
        // endregion 更新 Model END
	
		
}
