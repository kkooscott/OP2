package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class OPDT_E0_COUNTRYDao extends DBProcessDao {
	private static OPDT_E0_COUNTRYDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_E0_COUNTRYDao.class);

	private OPDT_E0_COUNTRYDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_E0_COUNTRYDao getInstance() {
		if (instance == null) {
			instance = new OPDT_E0_COUNTRYDao();
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
	public JSONArray getAllCountry() {
		String sql = "SELECT "
				+ "  OP_COUNTRY_CD, "
				+ "  OP_COUNTRY_NM "
				+ "FROM "
				+ "  OPDT_E0_COUNTRY "
				+ " WHERE OP_DELETE_FLAG <> '1' ";
				
		sql += " ORDER BY OP_COUNTRY_CD";
		
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql, new Object[] {});	
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
