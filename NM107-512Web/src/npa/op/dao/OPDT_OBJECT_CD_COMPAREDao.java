/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npa.op.dao;

import java.util.ArrayList;
import java.util.HashMap;
import npa.op.base.DBProcessDao;
import npa.op.util.ExceptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 *
 * @author Administrator
 */
public class OPDT_OBJECT_CD_COMPAREDao extends DBProcessDao {

    private static OPDT_OBJECT_CD_COMPAREDao instance = null;
    private static Logger log = Logger.getLogger(OPDT_OBJECT_CD_COMPAREDao.class);

    private OPDT_OBJECT_CD_COMPAREDao() {
    }

    /**
     * instance Dao.
     *
     * @return rs
     */
    public static OPDT_OBJECT_CD_COMPAREDao getInstance() {
        if (instance == null) {
            instance = new OPDT_OBJECT_CD_COMPAREDao();
        }
        return instance;
    }

    // 永續層操作
    // region 查詢 Model Start
    // ----------------------------------------
    // 取得多筆資料.
    /**
     * 取得多筆資料.
     *
     * @return rs.
     */
    public JSONArray getTYPEByQS() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ")
                .append(" OP_CODE, ")//.append(" CASE WHEN OP_CODE_OLD IS NULL THEN OP_CODE ELSE OP_CODE+'_'+OP_CODE_OLD END AS OP_CODE, ")
                .append(" OP_CODE_NM ")
                .append(" FROM ")
                .append(" OPDT_OBJECT_NEW ")
                .append(" WHERE 1=1 ");

        ArrayList<HashMap> list = null;
        try {
            list = executeQuery(sql.toString());
            for (int i = 0; i < list.size(); i++) {
                String letter = Integer.toString(letterToNumber(list.get(i).get("OP_CODE").toString().substring(0, 1)));
                String num = list.get(i).get("OP_CODE").toString().substring(3, 4);
                String typenm = list.get(i).get("OP_CODE_NM").toString();
                list.get(i).put("OP_CODE_NM", letter+"."+num + "  " + typenm);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return arrayList2JsonArray(list);
    }

    public int letterToNumber(String letter) {
        letter = letter.toUpperCase();
        if (!letter.matches("^[A-Z]+$")) {
            throw new IllegalArgumentException();
        }

        int power = letter.length() - 1;
        int sum = 0;
        for (int i = 0; i < letter.length(); i++) {
            char c = letter.charAt(i);
            int ordinal = c - 64;

            sum += ordinal * Math.pow(26, power);
            power--;
        }
        return sum;
    }

    public JSONArray checkDate(String opAcRcno) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.append(" SELECT * ")
                .append(" FROM ")
                .append(" OPDT_I_PU_BASIC ")
                .append(" WHERE 1=1 ")
                .append(" AND OP_ADD_DT_TM < '20220329100000' ")
                .append(" AND OP_SEQ_NO = ? ");
        qsPara.add(Integer.parseInt(opAcRcno));
        ArrayList<HashMap> list = null;
        try {
            list = pexecuteQuery(sql.toString(), qsPara.toArray());

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return arrayList2JsonArray(list);
    }
}
