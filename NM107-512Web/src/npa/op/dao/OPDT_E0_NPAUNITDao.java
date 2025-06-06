package npa.op.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import org.json.JSONArray;

import static npa.op.util.StringUtil.*;
import npa.op.base.DBProcessDao;
import npa.op.util.DateUtil;
import npa.op.util.ExceptionUtil;
import org.json.JSONObject;
import npa.op.util.StringUtil;

;

public class OPDT_E0_NPAUNITDao extends DBProcessDao {

    private static OPDT_E0_NPAUNITDao instance = null;

    public OPDT_E0_NPAUNITDao() {
    }

    /**
     * instance Dao.
     *
     * @return rs
     */
    public static OPDT_E0_NPAUNITDao getInstance() {
        if (instance == null) {
            instance = new OPDT_E0_NPAUNITDao();
        }
        return instance;
    }

    // 永續層操作
    // region 查詢 Model Start
    // ----------------------------------------
    /**
     * 取得符合條件資料之CD及NAME.
     *
     * @param level 1-警政署 2-警局 3-分局 4-派出所
     * @return JSONArray.
     */
    public JSONArray getUnitByLevel(final String level, final String level2CODE, final String level3CODE) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.setLength(0);

        if ("2".equals(level)) {
            sql.append(" SELECT OP_UNIT_CD,OP_UNIT_NM ")
                    .append(" FROM OPDT_E0_NPAUNIT ")
                    .append(" WHERE 1=1 ")
                    .append(" AND (OP_UNIT_LEVEL = '1' OR OP_UNIT_LEVEL = '2') ")
                    .append(" AND OP_UNIT_FLAG in ('90','50','10') ")
                    .append(" AND OP_DELETE_FLAG = 0 ")
                    .append(" AND OP_IS_SHOW = 1 ")
                    .append(" ORDER BY OP_UNIT_SORT  ");
        } else if ("3".equals(level)) {
            if (level2CODE.equals("A2H00") || level2CODE.equals("A2I00") || level2CODE.equals("A2J00") || level2CODE.equals("A2K00")) {
                //基隆港務警察總隊,臺中港務警察總隊,高雄港務警察總隊,花蓮港務警察總隊
                sql.append(" SELECT OP_UNIT_CD,OP_UNIT_S_NM ")
                        .append(" FROM OPDT_E0_NPAUNIT ")
                        .append(" WHERE 1=1 ")
                        .append(" AND (OP_UNIT_LEVEL = '1' OR OP_UNIT_LEVEL = '3' OR OP_UNIT_LEVEL = '4') ")
                        .append(" AND OP_DEPT_CD = ? ")
                        .append(" AND OP_UNIT_FLAG IN ('91','20','22','60','77') ")
                        .append(" AND OP_DELETE_FLAG = 0 ")
                        .append(" AND OP_IS_SHOW = 1 ")
                        .append(" ORDER BY OP_UNIT_CD  ");
            } else {
                sql.append(" SELECT OP_UNIT_CD,OP_UNIT_S_NM ")
                        .append(" FROM OPDT_E0_NPAUNIT ")
                        .append(" WHERE 1=1 ")
                        .append(" AND (OP_UNIT_LEVEL = '1' OR OP_UNIT_LEVEL = '3') ")
                        .append(" AND OP_DEPT_CD = ? ")
                        .append(" AND OP_UNIT_FLAG IN ('91','20','22','60','61') ")
                        .append(" AND OP_DELETE_FLAG = 0 ")
                        .append(" AND OP_IS_SHOW = 1 ")
                        .append(" ORDER BY OP_UNIT_CD  ");
            }
            qsPara.add(level2CODE);
        } else if ("4".equals(level)) {
            sql.append(" SELECT OP_UNIT_CD,OP_UNIT_S_NM ")
                    .append(" FROM OPDT_E0_NPAUNIT ")
                    .append(" WHERE 1=1 ")
                    .append(" AND (OP_UNIT_LEVEL = '1' OR OP_UNIT_LEVEL = '4') ")
                    .append(" AND OP_UNIT_FLAG IN ('92','30','31','70') ")
                    .append(" AND OP_IS_SHOW = 1 ")
                    .append(" AND OP_DELETE_FLAG = 0 ")
                    .append(" AND OP_BRANCH_CD = ? ")
                    .append(" ORDER BY OP_UNIT_CD  ");
            qsPara.add(level3CODE);

        }

        ArrayList<HashMap> list = pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(list);
    }

    /**
     * 取得符合條件資料之CD及NAME.
     *
     * @param level 1-警政署 2-警局 3-分局 4-派出所
     * @return JSONArray.
     */
    public JSONArray getUnitCode(final String UnitCode) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();

        sql.append("SELECT ")
                .append("  OP_UNIT_CD, ")
                .append("  OP_BRANCH_CD, ")
                .append("  OP_DEPT_CD ")
                .append("FROM ")
                .append("  OPDT_E0_NPAUNIT ")
                .append("  WHERE OP_UNIT_CD = ?");
        qsPara.add(UnitCode);

        ArrayList<HashMap> list = pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(list);
    }

    /**
     * 取得符合條件資料之經緯度.
     *
     * @return JSONArray.
     */
    public JSONArray getMarkers(final Boolean includeLower, final String levelCODE, final String level2CODE, final String level3CODE) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.setLength(0);
        //警局含下屬
        if (!levelCODE.equals("") && level2CODE.equals("") && level3CODE.equals("")) {
            //指定警局
            sql.append(" SELECT '2' AS TYPE,  OP_UNIT_CD ,OP_UNIT_NM, OP_UNIT_S_NM , OP_UNIT_ADDR, OP_LONGITUDE, OP_LATITUDE, ");
            sql.append(" CASE ");
            sql.append(" WHEN OP_AREA_NO ='' THEN OP_PHONE_NO ");
            sql.append(" WHEN OP_AREA_NO<>'' THEN '('+OP_AREA_NO+')'+OP_PHONE_NO ");
            sql.append(" END AS OP_PHONE_NO ");
            sql.append(" FROM OPDT_E0_NPAUNIT ");
            sql.append(" WHERE 1=1 ");
            //sql.append(" AND OP_UNIT_LEVEL = '2' ");
            sql.append(" AND (OP_UNIT_LEVEL = '1' OR OP_UNIT_LEVEL = '2') ");
            sql.append(" AND OP_UNIT_FLAG in ('90','50','10') ");
            sql.append(" AND OP_DELETE_FLAG = 0 ");
            sql.append(" AND OP_DEPT_CD = ? ");
            qsPara.add(levelCODE);

            //含下屬 : 所有分局
            if (includeLower) {
                sql.append(" UNION ");
                sql.append(" SELECT '3' AS TYPE, OP_UNIT_CD ,OP_UNIT_NM, OP_UNIT_S_NM , OP_UNIT_ADDR, OP_LONGITUDE, OP_LATITUDE, ");
                sql.append(" CASE ");
                sql.append(" WHEN OP_AREA_NO ='' THEN OP_PHONE_NO ");
                sql.append(" WHEN OP_AREA_NO<>'' THEN '('+OP_AREA_NO+')'+OP_PHONE_NO ");
                sql.append(" END AS OP_PHONE_NO ");
                sql.append(" FROM OPDT_E0_NPAUNIT ");
                sql.append(" WHERE 1=1 ");
                sql.append(" AND OP_UNIT_LEVEL = '3' ");
                sql.append(" AND OP_DEPT_CD = ? ");
                //sql.append(" AND OP_UNIT_FLAG IN ('20','22') ");
                sql.append(" AND OP_UNIT_FLAG IN ('20','22','60','61') ");
                sql.append(" AND OP_DELETE_FLAG = 0 ");
                sql.append(" AND OP_IS_SHOW = 1 ");
                qsPara.add(levelCODE);
            }
            sql.append(" ORDER BY OP_LONGITUDE, OP_LATITUDE ");
        }
        //分局
        if (!levelCODE.equals("") && !level2CODE.equals("") && level3CODE.equals("")) {
            sql.append(" SELECT '3' AS TYPE, OP_UNIT_CD ,OP_UNIT_NM, OP_UNIT_S_NM , OP_UNIT_ADDR, OP_LONGITUDE, OP_LATITUDE, ");
            sql.append(" CASE ");
            sql.append(" WHEN OP_AREA_NO ='' THEN OP_PHONE_NO ");
            sql.append(" WHEN OP_AREA_NO<>'' THEN '('+OP_AREA_NO+')'+OP_PHONE_NO ");
            sql.append(" END AS OP_PHONE_NO ");
            sql.append(" FROM OPDT_E0_NPAUNIT ");
            sql.append(" WHERE 1=1 ");
            sql.append(" AND OP_UNIT_LEVEL = '3' ");
            sql.append(" AND OP_UNIT_CD = ? ");
            //sql.append(" AND OP_UNIT_FLAG IN ('20','22') ");
            sql.append(" AND OP_UNIT_FLAG IN ('20','22','60','61') ");
            sql.append(" AND OP_DELETE_FLAG = 0 ");
            sql.append(" AND OP_IS_SHOW = 1 ");
            qsPara.add(level2CODE);

            //含下屬 : 所有派出所
            if (includeLower) {
                sql.append(" UNION ");
                sql.append(" SELECT '4' AS TYPE, OP_UNIT_CD ,OP_UNIT_NM, OP_UNIT_S_NM , OP_UNIT_ADDR, OP_LONGITUDE, OP_LATITUDE, ");
                sql.append(" CASE ");
                sql.append(" WHEN OP_AREA_NO ='' THEN OP_PHONE_NO ");
                sql.append(" WHEN OP_AREA_NO<>'' THEN '('+OP_AREA_NO+')'+OP_PHONE_NO ");
                sql.append(" END AS OP_PHONE_NO ");
                sql.append(" FROM OPDT_E0_NPAUNIT ");
                sql.append(" WHERE 1=1 ");
                sql.append(" AND OP_UNIT_LEVEL = '4' ");
                //sql.append(" AND OP_UNIT_FLAG = '30' ");
                sql.append(" AND OP_UNIT_FLAG IN ('30','31','70') ");
                sql.append(" AND OP_IS_SHOW = 1 ");
                sql.append(" AND OP_DELETE_FLAG = 0 ");
                if ("A1000".equals(levelCODE)) { //警政署第三層為''時，會抓出值 (不應該有值才對)
                    sql.append(" AND OP_BRANCH_CD = null ");
                } else {
                    sql.append(" AND OP_BRANCH_CD = ? ");
                    qsPara.add(level2CODE);
                }
            }
            sql.append(" ORDER BY OP_LONGITUDE, OP_LATITUDE ");
        } //派出所
        else if (!levelCODE.equals("") && !level2CODE.equals("") && !level3CODE.equals("")) {
            //指定派出所
            sql.append(" SELECT '4' AS TYPE, OP_UNIT_CD ,OP_UNIT_NM, OP_UNIT_S_NM , OP_UNIT_ADDR, OP_LONGITUDE, OP_LATITUDE, ");
            sql.append(" CASE ");
            sql.append(" WHEN OP_AREA_NO ='' THEN OP_PHONE_NO ");
            sql.append(" WHEN OP_AREA_NO<>'' THEN '('+OP_AREA_NO+')'+OP_PHONE_NO ");
            sql.append(" END AS OP_PHONE_NO ");
            sql.append(" FROM OPDT_E0_NPAUNIT ");
            sql.append(" WHERE 1=1 ");
            sql.append(" AND OP_UNIT_LEVEL = '4' ");
            //sql.append(" AND OP_UNIT_FLAG = '30' ");
            sql.append(" AND OP_UNIT_FLAG IN ('30','31','70') ");
            sql.append(" AND OP_IS_SHOW = 1 ");
            sql.append(" AND OP_DELETE_FLAG = 0 ");

            sql.append(" AND OP_UNIT_CD = ? ");
            qsPara.add(level3CODE);

            if ("A1000".equals(levelCODE)) { //警政署第三層為''時，會抓出值 (不應該有值才對)
                sql.append(" AND OP_BRANCH_CD = null ");
            } else {
                sql.append(" AND OP_BRANCH_CD = ? ");
                qsPara.add(level2CODE);
            }
            sql.append(" ORDER BY OP_LONGITUDE, OP_LATITUDE ");
        }

        ArrayList<HashMap> list = pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(list);
    }

    public Integer updatePosition(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();

        Integer entitySccess = null;
        String sysDate = DateUtil.getSystemTimeDB().substring(0, 8);
        String sysTime = DateUtil.getSystemTimeDB().substring(8);

        sql.append(" UPDATE OPDT_E0_NPAUNIT SET ");
        sql.append(" OP_LONGITUDE = ?, OP_LATITUDE = ?, OP_UPD_DTTM = ?, OP_UPD_NM = ? ");
        sql.append(" WHERE OP_UNIT_CD = ?");
        qsPara.add(jObj.getDouble("OP_LONGITUDE"));
        qsPara.add(jObj.getDouble("OP_LATITUDE"));
        qsPara.add(sysDate + sysTime);
        qsPara.add(jObj.getString("userName"));
        qsPara.add(jObj.getString("OP_UNIT_CD"));

        try {
            entitySccess = this.pexecuteUpdate(sql.toString(), qsPara.toArray());
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return entitySccess;
    }

    public String getUnitName(String unitcd) {
        if (StringUtil.nvl(unitcd).equals("")) {
            return "";
        }

        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();

        sql.append(" SELECT")
                .append(" OP_UNIT_NM")
                .append(" FROM")
                .append(" OPDT_E0_NPAUNIT");
        if (unitcd != "") {
            sql.append(" WHERE OP_UNIT_CD = ?");
            qsPara.add(unitcd);
        }
        String OP_UNIT_NM = "";

        try {
            CachedRowSet crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());

            if (crs.next()) {
                OP_UNIT_NM = StringUtil.nvl(crs.getString("OP_UNIT_NM"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OP_UNIT_NM;
    }

    public JSONArray getDBTime() {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();

        sql.append(" SELECT CONVERT(varchar(100), GETDATE(), 102) ");

        ArrayList<HashMap> list = null;
        try {
            list = pexecuteQuery(sql.toString(), qsPara.toArray());
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return arrayList2JsonArray(list);
    }
}
