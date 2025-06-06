package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class OPDT_E0_COLORDao extends DBProcessDao {
	private static OPDT_E0_COLORDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_E0_COLORDao.class);

	private OPDT_E0_COLORDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_E0_COLORDao getInstance() {
		if (instance == null) {
			instance = new OPDT_E0_COLORDao();
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
	public JSONArray getAllColor() {
		String sql = "SELECT "
				+ "  OP_COLOR_CD, "
				+ "  OP_COLOR_NM "
				+ " FROM "
				+ "  OPDT_E0_COLOR ";
				
		sql += " ORDER BY OP_COLOR_CD";
		
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql, new Object[] {});	
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
