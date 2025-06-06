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
import npa.op.dao.ReportDao;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import java.math.BigDecimal;
import static npa.op.util.DateUtil.get8UsDateFormatDB;
import npa.op.util.OPUtil;
import static npa.op.util.StringUtil.replaceWhiteChar;

public class SystemDao extends BaseDao {
	
	private static SystemDao instance = null;
	public static SystemDao getInstance() {
		if (instance == null) {
			instance = new SystemDao();
		}
		return instance;
	}
        
        //region 明細
	/**
        * 查詢排程資料 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QuerySchdl(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                String Isql = " SELECT *  FROM  OPDT_I_SCHDL_LOG  WHERE 1 = 1 ";              
               
                if (!jObj.get("OP_SCHDL_DT_S").equals("")){
                    Isql += " AND OP_SCHDL_DT_S >= ?";
                    args.add(jObj.get("OP_SCHDL_DT_S"));
                }
                if (!jObj.get("OP_SCHDL_DT_E").equals("")){
                    Isql += " AND OP_SCHDL_DT_E <= ?";
                    args.add(jObj.get("OP_SCHDL_DT_E"));
                }
                if (!jObj.get("OP_PRCD_CD").equals("")){
                    Isql += " AND OP_PRCD_CD = ?";
                    args.add( jObj.get("OP_PRCD_CD"));
                }
                String Esql = " SELECT *  FROM  OPDT_E_SCHDL_LOG  WHERE 1 = 1 ";
                if (!jObj.get("OP_SCHDL_DT_S").equals("")){
                    Esql += " AND OP_SCHDL_DT_S >= ?";
                    args.add(jObj.get("OP_SCHDL_DT_S"));
                }
                if (!jObj.get("OP_SCHDL_DT_E").equals("")){
                    Esql += " AND OP_SCHDL_DT_E <= ?";
                    args.add(jObj.get("OP_SCHDL_DT_E"));
                }
                if (!jObj.get("OP_PRCD_CD").equals("")){
                    Esql += " AND OP_PRCD_CD = ?";
                    args.add( jObj.get("OP_PRCD_CD"));
                }
                
                String sql = "SELECT * FROM (";
                sql += Isql;
                sql += " UNION ALL ";
                sql += Esql;
                sql += " )A ORDER BY  A.OP_SCHDL_DT_S DESC,A.OP_SCHDL_TM_S DESC ";
                
                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢統計 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryUERYTM(JSONObject jObj) {
            StringBuilder subsql = new StringBuilder();
                ArrayList args = new ArrayList();
                subsql.append(" select  A.OP_QUERY_NM, sum(OP_WEB_COUNT) as OP_WEB_COUNT, sum(OP_ANDROID_COUNT) as OP_ANDROID_COUNT, sum(OP_IOS_COUNT) as OP_IOS_COUNT, sum(OP_STATUS_S) as OP_STATUS_S, sum(OP_STATUS_E) as OP_STATUS_E, sum(OP_STATUS_T)  as OP_STATUS_T from ( ");
                //查有資料
                subsql.append(" SELECT OP_QUERY_NM, 0 as OP_WEB_COUNT, 0 as OP_ANDROID_COUNT, 0 as OP_IOS_COUNT, OP_STATUS as OP_STATUS_S, 0 as OP_STATUS_E, 0 as OP_STATUS_T ");
                subsql.append("  FROM  OPDT_E_QUERYTM   ");
                subsql.append(" WHERE 1=1  AND OP_STATUS =  1 ");
                if (!jObj.get("OP_QUERY_DT_TM_S").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM >= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_S")+"000000");
                }
                if (!jObj.get("OP_QUERY_DT_TM_E").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM <= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_E")+"999999");
                }
                
                subsql.append(" UNION ALL ");
                
                //查無資料
                subsql.append("  SELECT OP_QUERY_NM, 0 as OP_WEB_COUNT, 0 as OP_ANDROID_COUNT, 0 as OP_IOS_COUNT, OP_STATUS as OP_STATUS_S, 1 as OP_STATUS_E, 0 as OP_STATUS_T ");
                subsql.append(" FROM  OPDT_E_QUERYTM ");
                subsql.append(" WHERE 1=1  AND OP_STATUS =  0  ");
                 if (!jObj.get("OP_QUERY_DT_TM_S").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM >= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_S")+"000000");
                }
                if (!jObj.get("OP_QUERY_DT_TM_E").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM <= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_E")+"999999");
                }
                
                subsql.append(" UNION ALL ");
                
                //總筆數
                subsql.append(" SELECT OP_QUERY_NM, 0 as OP_WEB_COUNT, 0 as OP_ANDROID_COUNT, 0 as OP_IOS_COUNT, 0 as OP_STATUS_S,0 as OP_STATUS_E, 1 as OP_STATUS_T ");
                subsql.append(" FROM  OPDT_E_QUERYTM ");
                subsql.append(" WHERE 1=1  ");
                 if (!jObj.get("OP_QUERY_DT_TM_S").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM >= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_S")+"000000");
                }
                if (!jObj.get("OP_QUERY_DT_TM_E").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM <= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_E")+"999999");
                }
                
                subsql.append(" UNION ALL ");
                
                //WEB
                subsql.append(" SELECT OP_QUERY_NM, 1 as OP_WEB_COUNT, 0 as OP_ANDROID_COUNT, 0 as OP_IOS_COUNT, 0 as OP_STATUS_S,0 as OP_STATUS_E, 0 as OP_STATUS_T ");
                subsql.append(" FROM  OPDT_E_QUERYTM ");
                subsql.append(" WHERE 1=1 AND OP_DEVICE_CD =  1 ");
                 if (!jObj.get("OP_QUERY_DT_TM_S").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM >= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_S")+"000000");
                }
                if (!jObj.get("OP_QUERY_DT_TM_E").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM <= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_E")+"999999");
                }
                
                subsql.append(" UNION ALL ");
                
                //ANDROID
                subsql.append(" SELECT OP_QUERY_NM, 0 as OP_WEB_COUNT, 1 as OP_ANDROID_COUNT, 0 as OP_IOS_COUNT, 0 as OP_STATUS_S,0 as OP_STATUS_E, 0 as OP_STATUS_T ");
                subsql.append(" FROM  OPDT_E_QUERYTM ");
                subsql.append(" WHERE 1=1 AND OP_DEVICE_CD =  2 ");
                 if (!jObj.get("OP_QUERY_DT_TM_S").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM >= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_S")+"000000");
                }
                if (!jObj.get("OP_QUERY_DT_TM_E").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM <= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_E")+"999999");
                }
                
                subsql.append(" UNION ALL ");
                
                //IOS
                subsql.append(" SELECT OP_QUERY_NM, 0 as OP_WEB_COUNT, 0 as OP_ANDROID_COUNT, 1 as OP_IOS_COUNT, 0 as OP_STATUS_S,0 as OP_STATUS_E, 0 as OP_STATUS_T ");
                subsql.append(" FROM  OPDT_E_QUERYTM ");
                subsql.append(" WHERE 1=1 AND OP_DEVICE_CD =  3 ");
                 if (!jObj.get("OP_QUERY_DT_TM_S").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM >= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_S")+"000000");
                }
                if (!jObj.get("OP_QUERY_DT_TM_E").equals("")){
                    subsql.append(" AND OP_QUERY_DT_TM <= ? " );
                    args.add(jObj.get("OP_QUERY_DT_TM_E")+"999999");
                }
                
                //GROUP BY
                subsql.append("  )A group by  A.OP_QUERY_NM ");
              
                
                CachedRowSet crs = this.pexecuteQueryRowSet(subsql.toString(),args.toArray());
                
                return crs;
        }
       /**
        * 查詢 發文字參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryWD_PARA(JSONObject jObj) {
            StringBuilder subsql = new StringBuilder();
                ArrayList args = new ArrayList();
                subsql.append(" SELECT OP_SEQ_NO,OP_D_UNIT_CD,OP_D_UNIT_NM,OP_B_UNIT_CD,OP_B_UNIT_NM,  ");
                subsql.append(" OP_DOC_WD1,OP_YN_GET_NO1,OP_DOC_WD2,OP_YN_GET_NO2, ");
                subsql.append(" OP_UPD_DTTM,OP_UPD_ID,OP_UPD_NM,OP_UPD_UNIT_CD,OP_UPD_UNIT_NM ");
                subsql.append(" FROM OPDT_DOC_WD_PARA WHERE 1=1 ");
                if (!jObj.get("OP_D_UNIT_CD").equals("")){
                    subsql.append(" AND OP_D_UNIT_CD = ? " );
                    args.add(jObj.get("OP_D_UNIT_CD"));
                }
                if (!jObj.get("OP_B_UNIT_CD").equals("")){
                    subsql.append(" AND OP_B_UNIT_CD = ? " );
                    args.add(jObj.get("OP_B_UNIT_CD"));
                }
               subsql.append(" ORDER BY  OP_B_UNIT_CD  ");
                
                CachedRowSet crs = this.pexecuteQueryRowSet(subsql.toString(),args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢 發文字參數 是否存在
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet checkUnitForDocWd(JSONObject jObj) {
            StringBuilder subsql = new StringBuilder();
                ArrayList args = new ArrayList();
                subsql.append(" SELECT OP_SEQ_NO,OP_D_UNIT_CD,OP_D_UNIT_NM,OP_B_UNIT_CD,OP_B_UNIT_NM,  ");
                subsql.append(" OP_DOC_WD1,OP_YN_GET_NO1,OP_DOC_WD2,OP_YN_GET_NO2, ");
                subsql.append(" OP_UPD_DTTM,OP_UPD_ID,OP_UPD_NM,OP_UPD_UNIT_CD,OP_UPD_UNIT_NM ");
                subsql.append(" FROM OPDT_DOC_WD_PARA WHERE 1=1 ");
                if (!jObj.get("OP_D_UNIT_CD").equals("")){
                    subsql.append(" AND OP_D_UNIT_CD = ? " );
                    args.add(jObj.get("OP_D_UNIT_CD"));
                }
                if (!jObj.get("OP_B_UNIT_CD").equals("")){
                    subsql.append(" AND OP_B_UNIT_CD = ? " );
                    args.add(jObj.get("OP_B_UNIT_CD"));
                }
                
                CachedRowSet crs = this.pexecuteQueryRowSet(subsql.toString(),args.toArray());
                
                return crs;
        }
        
        /**
        * 卻認該分局是否已新曾發文字參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public boolean CHECK_PARA(JSONObject jObj) {
            boolean returnValue = false;
            StringBuilder sql = new StringBuilder();
            ArrayList args = new ArrayList();
            Object[] paraObject =null;
            try{
                jObj.put("OP_B_UNIT_CD", jObj.get("OP_B_UNIT_CD"));
               
                sql.append("SELECT * FROM  INTO OPDT_DOC_WD_PARA WHERE  OP_B_UNIT_CD = ? ");

                returnValue=pexecuteUpdate(sql.toString(),args.toArray()) > 0 ? true : false;
                
            } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
            }
            return returnValue;
        }
        
        //region 明細
	/**
	 *  查詢 發文字參數 .(DB原始資料)
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryWD_PARAByIdOriginal(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_DOC_WD_PARA"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
      
        /**
        * 新增 發文字參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public boolean INSERTWD_PARA(JSONObject jObj) {
            boolean returnValue = false;
            StringBuilder sql = new StringBuilder();
            Date current = new Date();
            User voUser = new User();
            Object[] paraObject =null;
            ReportDao UnitNM =  ReportDao.getInstance();
            try{
                voUser = (User) jObj.get("userVO");
                jObj.put("OP_D_UNIT_CD", jObj.get("OP_D_UNIT_CD"));
                jObj.put("OP_D_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_D_UNIT_CD").toString()));
                jObj.put("OP_B_UNIT_CD", jObj.get("OP_B_UNIT_CD"));
                jObj.put("OP_B_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_B_UNIT_CD").toString()));
                jObj.put("OP_DOC_WD1",  jObj.get("OP_DOC_WD1"));
                jObj.put("OP_YN_GET_NO1",  jObj.get("OP_YN_GET_NO1"));
                jObj.put("OP_DOC_WD2",  jObj.get("OP_DOC_WD2"));
                jObj.put("OP_YN_GET_NO2",  jObj.get("OP_YN_GET_NO2"));

                jObj.put("OP_ADD_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                jObj.put("OP_UPD_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                jObj.put("OP_ADD_ID", voUser.getUserId().toString());
                jObj.put("OP_ADD_NM", voUser.getUserName());
                jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd().toString());
                jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
                jObj.put("OP_UPD_ID", voUser.getUserId().toString());
                jObj.put("OP_UPD_NM", voUser.getUserName());
                jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd().toString());
                jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());

                String[] arryString = new String[] {    "OP_D_UNIT_CD",
                                                        "OP_D_UNIT_NM",
                                                        "OP_B_UNIT_CD",
                                                        "OP_B_UNIT_NM",
                                                        "OP_DOC_WD1",
                                                        "OP_YN_GET_NO1",
                                                        "OP_DOC_WD2",
                                                        "OP_YN_GET_NO2",
                                                        "OP_ADD_DTTM",
                                                        "OP_ADD_ID",
                                                        "OP_ADD_NM",
                                                        "OP_ADD_UNIT_CD",
                                                        "OP_ADD_UNIT_NM",
                                                        "OP_UPD_DTTM",
                                                        "OP_UPD_ID",
                                                        "OP_UPD_NM",
                                                        "OP_UPD_UNIT_CD",
                                                        "OP_UPD_UNIT_NM"};
                sql.append("INSERT INTO OPDT_DOC_WD_PARA ( ");
                sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                sql.append(" )")
                        .append(" VALUES  (");
                         for (int index = 0 ;index < arryString.length ;index++){
                        sql.append("?, ");
                }
                sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                sql.append(" ) ");

                //sql.append(" select @@identity  ");

                paraObject = new Object[arryString.length];

                int i = 0;
                for (String strKey:arryString){
                        paraObject[i] = new Object[i];
                        if (jObj.has(strKey))
                            paraObject[i] = jObj.get(strKey);
                        i++;
                }
                
                returnValue=pexecuteUpdate(sql.toString(),paraObject) > 0 ? true : false;
            } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
            }
            return returnValue;
        }
      
        /**
        * 更新 發文字參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public boolean UPDATEWD_PARA(JSONObject jObj) {
            boolean returnValue = false;
            StringBuilder sql = new StringBuilder();
            Date current = new Date();
            User voUser = new User();
            Object[] paraObject =null;
            ReportDao UnitNM =  ReportDao.getInstance();
            try{
                voUser = (User) jObj.get("userVO");
                jObj.put("OP_SEQ_NO", jObj.getDouble("OP_SEQ_NO"));
                jObj.put("OP_D_UNIT_CD", jObj.get("OP_D_UNIT_CD"));
                jObj.put("OP_D_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_D_UNIT_CD").toString()));
                jObj.put("OP_B_UNIT_CD", jObj.get("OP_B_UNIT_CD"));
                jObj.put("OP_B_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_B_UNIT_CD").toString()));
                jObj.put("OP_DOC_WD1",  jObj.get("OP_DOC_WD1"));
                jObj.put("OP_YN_GET_NO1",  jObj.get("OP_YN_GET_NO1"));
                jObj.put("OP_DOC_WD2",  jObj.get("OP_DOC_WD2"));
                jObj.put("OP_YN_GET_NO2",  jObj.get("OP_YN_GET_NO2"));

                jObj.put("OP_UPD_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                jObj.put("OP_UPD_ID", voUser.getUserId().toString());
                jObj.put("OP_UPD_NM", voUser.getUserName());
                jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd().toString());
                jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());

                String[] arryString = new String[] {    "OP_D_UNIT_CD",
                                                        "OP_D_UNIT_NM",
                                                        "OP_B_UNIT_CD",
                                                        "OP_B_UNIT_NM",
                                                        "OP_DOC_WD1",
                                                        "OP_YN_GET_NO1",
                                                        "OP_DOC_WD2",
                                                        "OP_YN_GET_NO2",
                                                        "OP_UPD_DTTM",
                                                        "OP_UPD_ID",
                                                        "OP_UPD_NM",
                                                        "OP_UPD_UNIT_CD",
                                                        "OP_UPD_UNIT_NM"};
                
                sql.append("UPDATE OPDT_DOC_WD_PARA SET ");
				
                for (int index = 0 ;index < arryString.length ;index++){
                        sql.append(arryString[index] + "=?, ");
                }
                sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                sql.append("WHERE OP_SEQ_NO =? ");
                 paraObject = new Object[arryString.length+1];
                // paramenter
                int i = 0;
                for (String strKey:arryString){
                        paraObject[i] = new Object[i];
                        if (jObj.has(strKey))
                                paraObject[i] = jObj.get(strKey);
                        i++;
                }
                paraObject[arryString.length] = jObj.getDouble("OP_SEQ_NO");

                returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
              
            } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
            }
            return returnValue;
            
        }
        
        /**
        * 刪除 發文字參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public boolean DELETEWD_PARA(JSONObject jObj) {
            boolean returnValue = false;
            int resultCount = 0;
            StringBuilder sql = new StringBuilder();
            String sqlDelete = "DELETE FROM OPDT_DOC_WD_PARA WHERE OP_SEQ_NO=? " ;
            resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{jObj.getDouble("OP_SEQ_NO")});
            
            if(resultCount > 0)
                    returnValue = true;
            
            return returnValue;
        }
        
        //region 明細
	/**
	 *  查詢 信件 .(DB原始資料)
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryMAIL_PARAByIdOriginal(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_MAIL_PARA"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
        
        /**
        * 查詢 信件參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryMAIL_PARA(JSONObject jObj) {
            StringBuilder subsql = new StringBuilder();
                ArrayList args = new ArrayList();
                subsql.append(" SELECT OP_SEQ_NO,OP_D_UNIT_CD,OP_D_UNIT_NM,OP_B_UNIT_CD,OP_B_UNIT_NM,  ");
                subsql.append(" OP_MAIL_CONTENT ");
                subsql.append(" FROM OPDT_MAIL_PARA WHERE 1=1 ");
                if (!jObj.get("OP_D_UNIT_CD").equals("")){
                    subsql.append(" AND OP_D_UNIT_CD = ? " );
                    args.add(jObj.get("OP_D_UNIT_CD"));
                }
                if (!jObj.get("OP_B_UNIT_CD").equals("")){
                    subsql.append(" AND OP_B_UNIT_CD = ? " );
                    args.add(jObj.get("OP_B_UNIT_CD"));
                }
                subsql.append(" ORDER BY  OP_B_UNIT_CD  ");
                
                CachedRowSet crs = this.pexecuteQueryRowSet(subsql.toString(),args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢 信件參數 是否存在
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet checkUnitForMail(JSONObject jObj) {
            StringBuilder subsql = new StringBuilder();
                ArrayList args = new ArrayList();
                subsql.append(" SELECT OP_SEQ_NO,OP_D_UNIT_CD,OP_D_UNIT_NM,OP_B_UNIT_CD,OP_B_UNIT_NM,  ");
                subsql.append(" OP_MAIL_CONTENT ");
                subsql.append(" FROM OPDT_MAIL_PARA WHERE 1=1 ");
                if (!jObj.get("OP_D_UNIT_CD").equals("")){
                    subsql.append(" AND OP_D_UNIT_CD = ? " );
                    args.add(jObj.get("OP_D_UNIT_CD"));
                }
                if (!jObj.get("OP_B_UNIT_CD").equals("")){
                    subsql.append(" AND OP_B_UNIT_CD = ? " );
                    args.add(jObj.get("OP_B_UNIT_CD"));
                }
                
                CachedRowSet crs = this.pexecuteQueryRowSet(subsql.toString(),args.toArray());
                
                return crs;
        }
        
        /**
        * 新增 信件參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public boolean INSERTMAIL_PARA(JSONObject jObj) {
            boolean returnValue = false;
            StringBuilder sql = new StringBuilder();
            Date current = new Date();
            User voUser = new User();
            Object[] paraObject =null;
            ReportDao UnitNM =  ReportDao.getInstance();
            try{
                voUser = (User) jObj.get("userVO");
                jObj.put("OP_MAIL_TYPE_CD", "1");
                jObj.put("OP_MAIL_TYPE_NM", "審核結果-非遺失人");
                jObj.put("OP_UNIT_CD", "");
                jObj.put("OP_UNIT_NM", "");
                jObj.put("OP_B_UNIT_CD", jObj.get("OP_B_UNIT_CD"));
                jObj.put("OP_B_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_B_UNIT_CD").toString()));
                jObj.put("OP_D_UNIT_CD", jObj.get("OP_D_UNIT_CD"));
                jObj.put("OP_D_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_D_UNIT_CD").toString()));
                jObj.put("OP_MAIL_CONTENT",  jObj.get("OP_MAIL_CONTENT"));
                
                jObj.put("OP_ADD_ID", voUser.getUserId().toString());
                jObj.put("OP_ADD_NM", voUser.getUserName());
                jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd().toString());
                jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
                jObj.put("OP_ADD_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                jObj.put("OP_UPD_ID", voUser.getUserId().toString());
                jObj.put("OP_UPD_NM", voUser.getUserName());
                jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd().toString());
                jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                jObj.put("OP_UPD_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

                String[] arryString = new String[] {    "OP_MAIL_TYPE_CD",
                                                        "OP_MAIL_TYPE_NM",
                                                        "OP_UNIT_CD",
                                                        "OP_UNIT_NM",
                                                        "OP_B_UNIT_CD",
                                                        "OP_B_UNIT_NM",
                                                        "OP_D_UNIT_CD",
                                                        "OP_D_UNIT_NM",
                                                        "OP_MAIL_CONTENT",
                                                        "OP_ADD_ID",
                                                        "OP_ADD_NM",
                                                        "OP_ADD_UNIT_CD",
                                                        "OP_ADD_UNIT_NM",
                                                        "OP_ADD_DTTM",
                                                        "OP_UPD_ID",
                                                        "OP_UPD_NM",
                                                        "OP_UPD_UNIT_CD",
                                                        "OP_UPD_UNIT_NM",
                                                        "OP_UPD_DTTM"};
                sql.append("INSERT INTO OPDT_MAIL_PARA ( ");
                sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                sql.append(" )")
                        .append(" VALUES  (");
                         for (int index = 0 ;index < arryString.length ;index++){
                        sql.append("?, ");
                }
                sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                sql.append(" ) ");

                //sql.append(" select @@identity  ");

                paraObject = new Object[arryString.length];

                int i = 0;
                for (String strKey:arryString){
                        paraObject[i] = new Object[i];
                        if (jObj.has(strKey))
                            paraObject[i] = jObj.get(strKey);
                        i++;
                }
                
                returnValue=pexecuteUpdate(sql.toString(),paraObject) > 0 ? true : false;
                if( returnValue ){
                    jObj.put("OP_SEQ_NO", returnValue );
                }
            } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
            }
            return returnValue;
        }
      
        /**
        * 更新 信件參數
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public boolean UPDATEMAIL_PARA(JSONObject jObj) {
            boolean returnValue = false;
            StringBuilder sql = new StringBuilder();
            Date current = new Date();
            User voUser = new User();
            Object[] paraObject =null;
            ReportDao UnitNM =  ReportDao.getInstance();
            try{
                voUser = (User) jObj.get("userVO");
                jObj.put("OP_SEQ_NO", jObj.getDouble("OP_SEQ_NO"));
                jObj.put("OP_D_UNIT_CD", jObj.get("OP_D_UNIT_CD"));
                jObj.put("OP_D_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_D_UNIT_CD").toString()));
                jObj.put("OP_B_UNIT_CD", jObj.get("OP_B_UNIT_CD"));
                jObj.put("OP_B_UNIT_NM", UnitNM.getUnitNm(jObj.get("OP_B_UNIT_CD").toString()));
                jObj.put("OP_MAIL_CONTENT",  jObj.get("OP_MAIL_CONTENT"));

                jObj.put("OP_UPD_ID", voUser.getUserId().toString());
                jObj.put("OP_UPD_NM", voUser.getUserName());
                jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd().toString());
                jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                jObj.put("OP_UPD_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());

                String[] arryString = new String[] {    "OP_D_UNIT_CD",
                                                        "OP_D_UNIT_NM",
                                                        "OP_B_UNIT_CD",
                                                        "OP_B_UNIT_NM",
                                                        "OP_MAIL_CONTENT",
                                                        "OP_UPD_ID",
                                                        "OP_UPD_NM",
                                                        "OP_UPD_UNIT_CD",
                                                        "OP_UPD_UNIT_NM",
                                                        "OP_UPD_DTTM"};
                
                sql.append("UPDATE OPDT_MAIL_PARA SET ");
				
                for (int index = 0 ;index < arryString.length ;index++){
                        sql.append(arryString[index] + "=?, ");
                }
                sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                sql.append("WHERE OP_SEQ_NO =? ");
                 paraObject = new Object[arryString.length+1];
                // paramenter
                int i = 0;
                for (String strKey:arryString){
                        paraObject[i] = new Object[i];
                        if (jObj.has(strKey))
                                paraObject[i] = jObj.get(strKey);
                        i++;
                }
                paraObject[arryString.length] = jObj.getDouble("OP_SEQ_NO");

                returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
              
            } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
            }
            return returnValue;
            
        }
}
