package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class OPDT_E0_VILLAGEDao extends DBProcessDao {
	private static OPDT_E0_VILLAGEDao instance = null;
	private static Logger log = Logger.getLogger(OPDT_E0_VILLAGEDao.class);

	private OPDT_E0_VILLAGEDao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_E0_VILLAGEDao getInstance() {
		if (instance == null) {
			instance = new OPDT_E0_VILLAGEDao();
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
	public JSONArray getCityByQS(final String OP_TOWN_CD) {
		StringBuilder sql = new StringBuilder();
		ArrayList qsPara = new ArrayList();
		
		sql.append(" SELECT ")
			.append("  OP_VILLAGE_CD, ")
			.append("  OP_VILLAGE_S_NM ")
			.append(" FROM ")
			.append("  OPDT_E0_VILLAGE ")
			.append(" WHERE OP_DELETE_FLAG <> '1' ");
		
		if (!"".equals(OP_TOWN_CD)){
			sql.append(" AND OP_TOWN_CD=? ");
			qsPara.add(OP_TOWN_CD);
		}
		sql.append(" ORDER BY OP_VILLAGE_CD");
		
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql.toString(), qsPara.toArray());
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
