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
public class OPDT_ROLEDao extends BaseDao{
    private static OPDT_ROLEDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_ROLEDao.class);

	private OPDT_ROLEDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_ROLEDao getInstance() {
		if (instance == null) {
			instance = new OPDT_ROLEDao();
		}
		return instance;
	}
       
	
        // 取得多筆資料.
	/**
	 * 取得多筆資料.
	 * 
	 * @return JSONArray.
	 */
	public JSONArray getROLE() throws SQLException {
        	StringBuilder sql = new StringBuilder();
                ArrayList args = new ArrayList();
		CachedRowSet crs = null;
		sql.append(" SELECT ")
			.append(" * ")
			.append(" FROM ")
			.append("  OPDT_ROLE   ")
                      .append(" WHERE OPDT_ROLE.OP_ENABLED='Y' ");
                
		sql.append(" ORDER BY  OP_ROLE_ID ");
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql.toString(), args.toArray());
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
                
	}

       
}
