package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class OPDT_E0_CITYDao extends DBProcessDao {
	private static OPDT_E0_CITYDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_E0_CITYDao.class);

	private OPDT_E0_CITYDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_E0_CITYDao getInstance() {
		if (instance == null) {
			instance = new OPDT_E0_CITYDao();
		}
		return instance;
	}

	// 永續層操作

	// region 查詢 Model Start
	// ----------------------------------------
	

	// 取得單筆資料.
	/**
	 * 取得單筆資料.
	 * 
	 * @return rs.
	 */
	public JSONArray getAllCity() {
		String sql = "SELECT "
				+ "  OP_CITY_CD, "
				+ "  OP_CITY_NM "
				+ "FROM "
				+ "  OPDT_E0_CITY "
				+ " WHERE OP_DELETE_FLAG <> '1' ";
				
		sql += " ORDER BY OP_CITY_CD";
		
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql, new Object[] {});	
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
