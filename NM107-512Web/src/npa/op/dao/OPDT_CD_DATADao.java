package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class OPDT_CD_DATADao extends DBProcessDao {
	private static OPDT_CD_DATADao instance = null;
	private static Logger log = Logger.getLogger(OPDT_CD_DATADao.class);

	private OPDT_CD_DATADao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static OPDT_CD_DATADao getInstance() {
		if (instance == null) {
			instance = new OPDT_CD_DATADao();
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
	public JSONArray getTYPEByQS(final String OP_CD_TYPE) {
		StringBuilder sql = new StringBuilder();
		ArrayList qsPara = new ArrayList();
		
		sql.append(" SELECT ")
			.append("  OP_CD, ")
			.append("  OP_CD_NM ")
			.append(" FROM ")
			.append("  OPDT_CD_DATA ")
			.append(" WHERE 1=1 ");
		
		if (!"".equals(OP_CD_TYPE)){
			sql.append(" AND OP_CD_TYPE=? ");
			qsPara.add(OP_CD_TYPE);
		}
		sql.append(" ORDER BY OP_CD");
		
		ArrayList<HashMap> list = null;
		try {
			list = pexecuteQuery(sql.toString(), qsPara.toArray());
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
