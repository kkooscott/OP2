/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npa.op.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.sql.rowset.CachedRowSet;
import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;
import npa.op.vo.User;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import npa.op.base.BaseDao;
import javax.sql.rowset.CachedRowSet;
import npa.op.util.DateUtil;

/**
 *
 * @author curitis
 */
public class OPDT_FUNC_ROLEDao extends BaseDao{
    private static OPDT_FUNC_ROLEDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_FUNC_ROLEDao.class);

	private OPDT_FUNC_ROLEDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_FUNC_ROLEDao getInstance() {
		if (instance == null) {
			instance = new OPDT_FUNC_ROLEDao();
		}
		return instance;
	}
       
	
        // 取得多筆資料.
	/**
	 * 取得多筆資料.
	 * 
	 * @return JSONArray.
	 */
	public JSONArray getFUNC_ROLE(JSONObject jObj) throws SQLException {
        	StringBuilder sql = new StringBuilder();
                JSONObject jObject = new JSONObject();
                JSONArray resultDataArray = new JSONArray();
                ArrayList args = new ArrayList();
		CachedRowSet crs = null;
		sql.append(" SELECT ")
                   .append(" leftfunc.OP_FUNC_NM as OP_FUNC_GROUP_NM, FUNCROLE.OP_FUNC_ID,FUNCROLE.OP_ROLE_ID,OP_ROLE_NM,OPDT_FUNC.OP_FUNC_NM,FUNCROLE.OP_ENABLED,CASE WHEN FUNCROLE.OP_ENABLED = 'Y' THEN '是'  ELSE '否' END AS OP_ENABLED_CHT ")
                   .append(" FROM ")
                   .append("  OPDT_FUNC_ROLE FUNCROLE  ")
                   .append("  INNER JOIN OPDT_ROLE  ON FUNCROLE.OP_ROLE_ID = OPDT_ROLE.OP_ROLE_ID  ")
                   .append("  INNER JOIN OPDT_FUNC ON FUNCROLE.OP_FUNC_ID = OPDT_FUNC.OP_FUNC_ID ")
                   .append("  INNER JOIN OPDT_FUNC leftfunc ON SUBSTRING(OPDT_FUNC.OP_FUNC_GROUP,1,6) = leftfunc.OP_FUNC_GROUP  ")
                   //.append(" WHERE OPDT_ROLE.OP_ENABLED='Y'  and len(OPDT_FUNC.OP_FUNC_GROUP)>6 ");
                   .append(" WHERE OPDT_ROLE.OP_ENABLED='Y'  and (len(OPDT_FUNC.OP_FUNC_GROUP)>6 or OPDT_FUNC.OP_FUNC_GROUP = 'menu01') ");
                if (jObj.has("OP_ROLE_ID")){
                    if (!"".equals(jObj.get("OP_ROLE_ID"))){
                        sql.append(" AND  FUNCROLE.OP_ROLE_ID=? ");
                        args.add(jObj.get("OP_ROLE_ID"));
                    }
                }
                 if (jObj.has("OP_ENABLED")){
                    if (!"".equals(jObj.get("OP_ENABLED"))){
                        sql.append(" AND  FUNCROLE.OP_ENABLED=? ");
                        args.add(jObj.get("OP_ENABLED"));
                    }
                }
                if (jObj.has("OP_FUNC_GROUP")){
                    if (!"".equals(jObj.get("OP_FUNC_GROUP"))){
                        sql.append("  AND  SUBSTRING(OPDT_FUNC.OP_FUNC_GROUP,1,6) = (SELECT ISNULL(OP_FUNC_GROUP,'')  FROM OPDT_FUNC WHERE OP_FUNC_ID=?   )  ");
                        args.add(jObj.get("OP_FUNC_GROUP"));
                    }
                }
		sql.append(" ORDER BY  FUNCROLE.OP_ROLE_ID,FUNCROLE.OP_FUNC_ID ");
		try {
                    crs = this.pexecuteQueryRowSet(sql.toString(),args.toArray());
                    while (crs.next()) {
                        jObject = new JSONObject();
                        jObject.put("OP_FUNC_GROUP_NM",crs.getString("OP_FUNC_GROUP_NM"));
                        jObject.put("OP_FUNC_ID",crs.getString("OP_FUNC_ID"));
                        jObject.put("OP_ROLE_ID",crs.getString("OP_ROLE_ID"));
                        jObject.put("OP_ROLE_NM",crs.getString("OP_ROLE_NM"));
                        jObject.put("OP_FUNC_NM",crs.getString("OP_FUNC_NM"));
                        jObject.put("OP_ENABLED",crs.getString("OP_ENABLED"));
                        jObject.put("OP_ENABLED_CHT",crs.getString("OP_ENABLED_CHT"));
                        resultDataArray.put(jObject);
                    } 
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
                
                return resultDataArray;
	}

       
        // update.
	/**
	 *update
	 * 
	 * @return boolean.
	 */
	public boolean UpdateENABLED(String OP_ENABLED ,String OP_FUNC_ID ,String OP_ROLE_ID,JSONObject jObj) {
                boolean returnValue = false;
		StringBuilder sql = new StringBuilder();
                ArrayList args = new ArrayList();
		User voUser = new User();
                voUser = (User) jObj.get("userVO");
                Date current = new Date();
	        try{
                    
                    sql.append("UPDATE OPDT_FUNC_ROLE SET OP_ENABLED = ? , OP_UPD_DTTM = ? ,OP_UPD_NM = ? WHERE OP_FUNC_ID = ? and OP_ROLE_ID= ? ");
                    args.add(OP_ENABLED);
                    args.add(new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                    args.add(voUser.getUserName());
                    args.add(OP_FUNC_ID);
                    args.add(OP_ROLE_ID);
                            
                    returnValue = this.pexecuteUpdate(sql.toString(), args.toArray()) > 0 ? true : false;
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return returnValue;
	}
        
        
        // 取得單筆資料.
        /**
         * 取得單筆資料.
         * 
         * @return rs.
         */
        public JSONArray getFuncList(String[] role) {
                StringBuilder arryRole = new StringBuilder();

                for (String strRole:role){
                        arryRole.append(",'" + strRole + "'");
                }
                
                String sql = " SELECT OP_FUNC_NM, OP_FUNC_URL, OP_FUNC_GROUP FROM OPDT_FUNC "
                                + " INNER JOIN ( "
                                + " SELECT OP_FUNC_ID, MAX(OP_ROLE_ID) AS MAXROLE FROM OPDT_FUNC_ROLE "
                                + " WHERE OP_ROLE_ID IN(" + arryRole.substring(1).toString() + ") "
                                + " AND OP_ENABLED='Y' "
                                + " GROUP BY OP_FUNC_ID ) AS A "
                                + " ON A.OP_FUNC_ID=OPDT_FUNC.OP_FUNC_ID "
                                + " AND OPDT_FUNC.OP_ENABLED='Y' ";

                ArrayList<HashMap> list = executeQuery(sql);

                return arrayList2JsonArray(list);
        }
}
