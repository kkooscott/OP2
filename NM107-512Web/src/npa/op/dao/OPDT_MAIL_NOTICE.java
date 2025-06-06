/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npa.op.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.sql.rowset.CachedRowSet;
import npa.op.base.BaseDao;
import npa.op.base.DBProcessDao;
import npa.op.util.DaoUtil;
import npa.op.util.ExceptionUtil;
import npa.op.util.OPUtil;
import npa.op.util.luence;
import npa.op.vo.User;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class OPDT_MAIL_NOTICE extends BaseDao {

    private static OPDT_MAIL_NOTICE instance = null;
    private static Logger log = Logger.getLogger(OPDT_MAIL_NOTICE.class);

    private OPDT_MAIL_NOTICE() {
    }

    /**
     * instance Dao.
     *
     * @return rs
     */
    public static OPDT_MAIL_NOTICE getInstance() {
        if (instance == null) {
            instance = new OPDT_MAIL_NOTICE();
        }
        return instance;
    }

    public JSONArray getMailInfo(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.append(" SELECT ")
                .append(" OP_SEQ_NO,OP_UNIT_CD,OP_UNIT_NM,OP_EMAIL ")
                .append(" FROM ")
                .append(" OPDT_MAIL_NOTICE ")
                .append(" WHERE 1=1 ");
        if (jObj.has("OP_UNIT_CD")) {
            if (jObj.get("OP_UNIT_CD") != null) {
                if(!jObj.get("OP_UNIT_CD").toString().equals("")){
                    sql.append("AND OP_UNIT_CD = ?");
                    qsPara.add(jObj.get("OP_UNIT_CD"));
                }
            }
        }
        sql.append(" ORDER BY OP_SEQ_NO ");
        ArrayList<HashMap> list = null;
        try {
            list = pexecuteQuery(sql.toString(), qsPara.toArray());

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return arrayList2JsonArray(list);
    }

    public boolean add(JSONObject jObj) {
        boolean returnvalue = false;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();
        OPDT_MAIL_NOTICE daoMail = OPDT_MAIL_NOTICE.getInstance();
        try {
            User voUser = new User();
            OPUtil opUtil = new OPUtil();
            voUser = (User) jObj.get("userVO");
            jObj.put("OP_UNIT_CD", jObj.get("OP_UNIT_CD"));
            jObj.put("OP_UNIT_NM", jObj.get("OP_UNIT_NM"));
            jObj.put("OP_EMAIL", jObj.get("OP_EMAIL"));
            jObj.put("OP_ADD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
            jObj.put("OP_ADD_ID", voUser.getUserId());
            jObj.put("OP_ADD_NM", voUser.getUserName());
            jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd());
            jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_ID", "null");
            jObj.put("OP_UPD_NM", "null");
            jObj.put("OP_UPD_UNIT_CD", "null");
            jObj.put("OP_UPD_UNIT_NM", "null");

            DaoUtil util = new DaoUtil();
            jObj = util.getStaticColumn(jObj, "ADD");
            jObj = util.getStaticColumn(jObj, "UPD");

            String arryString[] = {
                "OP_UNIT_CD",
                "OP_UNIT_NM",
                "OP_EMAIL",
                "OP_ADD_DT_TM",
                "OP_UPD_DT_TM",
                "OP_ADD_ID",
                "OP_ADD_NM",
                "OP_ADD_UNIT_CD",
                "OP_ADD_UNIT_NM",
                "OP_UPD_ID",
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM"
            };

            sql.append("INSERT INTO OPDT_MAIL_NOTICE ( ");
            sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length() - 1));
            sql.append(" )")
                    .append(" VALUES  (")
                    .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                    .append(" ?,?,?)"); //13

            Object[] paraObject = new Object[arryString.length];

            int i = 0;
            for (String strKey : arryString) {
                paraObject[i] = new Object[i];
                if (jObj.has(strKey)) {
                    if ("OP_SEQ_NO".equals(strKey)) {
                        if (!"".equals(jObj.get(strKey).toString().trim())) {
                            paraObject[i] = Integer.parseInt(jObj.get(strKey).toString());
                        } else {
                            paraObject[i] = 0;
                        }
                    } else {
                        paraObject[i] = jObj.get(strKey);
                    }
                }
                i++;
            }

            returnvalue = pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;;

        } catch (Exception e) {

        }
        return returnvalue;
    }

    public CachedRowSet checkUnitForMail(JSONObject jObj) {
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        subsql.append(" SELECT OP_SEQ_NO,OP_MAIL ");
        subsql.append(" FROM OPDT_MAIL_NOTICE WHERE 1=1 ");
        if (!jObj.get("OP_EMAIL").equals("")) {
            subsql.append(" AND OP_EMAIL = ? ");
            args.add(jObj.get("OP_EMAIL"));
        }
        CachedRowSet crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());

        return crs;
    }

    public JSONArray getMailOriginal(int OP_SEQ_NO) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.append(" SELECT * ")
                .append(" FROM ")
                .append(" OPDT_MAIL_NOTICE ")
                .append(" WHERE OP_SEQ_NO = ? ");
        qsPara.add(OP_SEQ_NO);
        ArrayList<HashMap> list = null;
        try {
            list = pexecuteQuery(sql.toString(), qsPara.toArray());

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return arrayList2JsonArray(list);
    }

    public boolean UPDATEMAIL_PARA(JSONObject jObj) {
        boolean returnValue = false;
        StringBuilder sql = new StringBuilder();
        Date current = new Date();
        User voUser = new User();
        Object[] paraObject = null;
        ReportDao UnitNM = ReportDao.getInstance();
        try {
            voUser = (User) jObj.get("userVO");
            jObj.put("OP_EMAIL", jObj.get("OP_EMAIL"));

            jObj.put("OP_UPD_ID", voUser.getUserId().toString());
            jObj.put("OP_UPD_NM", voUser.getUserName());
            jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd().toString());
            jObj.put("OP_UPD_UNIT_NM", voUser.getUnitName());
            jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
            jObj.put("OP_SEQ_NO", jObj.getDouble("OP_SEQ_NO"));
            
            String[] arryString = new String[]{
                "OP_EMAIL",
                "OP_UPD_ID",
                "OP_UPD_NM",
                "OP_UPD_UNIT_CD",
                "OP_UPD_UNIT_NM",
                "OP_UPD_DT_TM"
                };

            sql.append("UPDATE OPDT_MAIL_NOTICE SET ");

            for (int index = 0; index < arryString.length; index++) {
                sql.append(arryString[index] + "=?, ");
            }
            sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, "");
            sql.append("WHERE OP_SEQ_NO =? ");
            paraObject = new Object[arryString.length + 1];
            // paramenter
            int i = 0;
            for (String strKey : arryString) {
                paraObject[i] = new Object[i];
                if (jObj.has(strKey)) {
                    paraObject[i] = jObj.get(strKey);
                }
                i++;
            }
            paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

            returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return returnValue;

    }

    public boolean DELETEWD_PARA(JSONObject jObj) {
        boolean returnValue = false;
        int resultCount = 0;
        StringBuilder sql = new StringBuilder();
        String sqlDelete = "DELETE FROM OPDT_MAIL_NOTICE WHERE OP_SEQ_NO=? ";
        resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{jObj.getInt("OP_SEQ_NO")});

        if (resultCount > 0) {
            returnValue = true;
        }

        return returnValue;
    }
    
    public JSONArray getUnit(JSONObject jObj){
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.setLength(0);
        
        sql.append("SELECT OP_UNIT_CD,OP_UNIT_NM,OP_UNIT_S_NM ");
        sql.append("FROM OPDT_MAIL_NOTICE ");
        sql.append("WHERE 1=1");
        ArrayList<HashMap> list = pexecuteQuery(sql.toString(), qsPara.toArray());	
        return arrayList2JsonArray(list);
    }
}
