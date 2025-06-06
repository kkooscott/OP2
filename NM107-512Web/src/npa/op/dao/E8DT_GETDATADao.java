package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;

public class E8DT_GETDATADao extends DBProcessDao {
	private static E8DT_GETDATADao instance = null;
	private static Logger log = Logger.getLogger(E8DT_GETDATADao.class);

	private E8DT_GETDATADao() {
	}

	/**
	 * instance Dao.
	 * 
	 * @return rs
	 */
	public static E8DT_GETDATADao getInstance() {
		if (instance == null) {
			instance = new E8DT_GETDATADao();
		}
		return instance;
	}

	// 永續層操作

	// region 查詢 Model Start
	// ----------------------------------------
	

	// 取得類別資料.
	/**
	 * 取得類別資料.
	 * 
	 * @return JsonArray
	 */
	public JSONArray getTYPEByQS(final String E8_CD_TYPE) {
		StringBuilder sql = new StringBuilder();
		ArrayList qsPara = new ArrayList();
		
                if( E8_CD_TYPE.equals("C12") ){
                    sql.append(" SELECT ")
			.append("  E8_CD, ")
			.append("  E8_CD+'.'+E8_CD_NM as E8_CD_NM ")
			.append(" FROM ")
			.append("  E8DT_CD_DATA ")
			.append(" WHERE 1=1 ")
                        //只挑選拾得物需要之物品種類
                        .append(" AND ( SUBSTRING(E8_CD,1,2) in ('A0','B0','BD','BF','DF','E0','F0','G0','I0','K0','L0','M0','M1','N0','N1','N2','N3','N4','N5','N6','N7') ")
                        .append(" OR SUBSTRING(E8_CD,1,4) in ('CE00','DG00','H000','H028','H046','H047','J000','JA00') ) ");
                }else{
                    sql.append(" SELECT ")
			.append("  E8_CD, ")
			.append("  E8_CD_NM ")
			.append(" FROM ")
			.append("  E8DT_CD_DATA ")
			.append(" WHERE 1=1 ");
                }
		
		if (!"".equals(E8_CD_TYPE)){
			sql.append(" AND E8_CD_TYPE=? ");
			qsPara.add(E8_CD_TYPE);
		}
		sql.append(" ORDER BY E8_CD");
		
		ArrayList<HashMap> list = null;
		try {
			list = E8pexecuteQuery(sql.toString(), qsPara.toArray());
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
        
        // 取得類別資料.
	/**
	 * 取得類別資料.
	 * 
	 * @return JsonArray
	 */
	public JSONArray getLISTByQS(final String E8_LIST_TYPE) {
		StringBuilder sql = new StringBuilder();
		ArrayList qsPara = new ArrayList();
		
		sql.append(" SELECT ")
			.append("  E8_LIST_TYPE, ")
			.append("  E8_LIST_NM ")
			.append(" FROM ")
			.append("  E8DT_LIST_DATA ")
			.append(" WHERE 1=1 ");
		
		if (!"".equals(E8_LIST_TYPE)){
			sql.append(" AND E8_LIST_TYPE=? ");
			qsPara.add(E8_LIST_TYPE);
		}
		
		ArrayList<HashMap> list = null;
		try {
			list = E8pexecuteQuery(sql.toString(), qsPara.toArray());
			
		} catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		}
		
		return arrayList2JsonArray(list);
	}
}
