package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class OPDT_E0_TOWNDao extends DBProcessDao {
	private static OPDT_E0_TOWNDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_E0_TOWNDao.class);

	private OPDT_E0_TOWNDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_E0_TOWNDao getInstance() {
		if (instance == null) {
			instance = new OPDT_E0_TOWNDao();
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
	public JSONArray getCityByQS(final String OP_CITY_CD) {
		StringBuilder sql = new StringBuilder();
		ArrayList qsPara = new ArrayList();
		
		sql.append(" SELECT ")
			.append("  OP_TOWN_CD, ")
			.append("  OP_TOWN_S_NAME ")
			.append(" FROM ")
			.append("  OPDT_E0_TOWN ")
			.append(" WHERE OP_DELETE_FLAG <> '1' ");
		
		if (!"".equals(OP_CITY_CD)){
			sql.append(" AND OP_CITY_CD=? ");
			qsPara.add(OP_CITY_CD);
		}
		sql.append(" ORDER BY OP_TOWN_CD");
		
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql.toString(), qsPara.toArray());
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
