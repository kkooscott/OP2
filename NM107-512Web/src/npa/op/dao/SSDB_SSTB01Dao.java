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
public class SSDB_SSTB01Dao extends DBProcessDao {

    private static SSDB_SSTB01Dao instance = null;
    private static Logger log = Logger.getLogger(SSDB_SSTB01Dao.class);

    private SSDB_SSTB01Dao() {
    }

    /**
     * instance Dao.
     *
     * @return rs
     */
    public static SSDB_SSTB01Dao getInstance() {
        if (instance == null) {
            instance = new SSDB_SSTB01Dao();
        }
        return instance;
    }
    
    public JSONArray GetSSDBROLE(String unit_D, String unit_B, String unit) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();

        sql.append(" select distinct TB01.SS_UID,TB01.SS_UNM from SSDB..SSTB01 TB01 ");
        sql.append(" INNER JOIN SSDB..SSTB02 TB02 ON TB01.SS_UID= TB02.SS_UID   ");
        sql.append(" WHERE 1=1  AND SUBSTRING(SS_GID,1,3) = 'OP3' ");
//        sql.append(" WHERE 1=1  AND SUBSTRING(SS_GID,1,3) = 'OP2' ");

        if (!"".equals(unit)) {
            sql.append(" AND SS_WDID = ?  ");
            qsPara.add(unit);
        }else if (!"".equals(unit_B)) {
            sql.append(" AND SS_WDID = ? ");
            qsPara.add(unit_B);
        }else if (!"".equals(unit_D)) {
            sql.append(" AND SS_WDID = ?  ");
            qsPara.add(unit_D);
        }   
        ArrayList<HashMap> list = null;
        try {
            list = SSpexecuteQuery(sql.toString(), qsPara.toArray());

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return arrayList2JsonArray(list);
    }

}
