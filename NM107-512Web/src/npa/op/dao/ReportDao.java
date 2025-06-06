package npa.op.dao;

import static npa.op.util.StringUtil.nvl;

import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import npa.op.base.BaseDao;
import npa.op.util.DateUtil;
import npa.op.util.ExceptionUtil;
import npa.op.util.StringUtil;
import npa.op.vo.User;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportDao extends BaseDao {
    private static ReportDao instance = null;
    private static Logger log = Logger.getLogger(ReportDao.class);
    OPDT_E0_NPAUNITDao daoNPAUNIT = OPDT_E0_NPAUNITDao.getInstance();
    private ReportDao()
    {
    }

    public static ReportDao getInstance()
    {
	if (instance == null)
	{
	    instance = new ReportDao();
	}
	return instance;
    }
    //拾得物收據
    public HashMap printByOP02A01Q_DOC(JSONObject obj) {
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_SEQ_NO, BASIC.OP_AC_RCNO, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_DATE, BASIC.OP_PUPO_NAME, BASIC.OP_PUPO_IDN, BASIC.OP_PU_DATE, BASIC.OP_PU_TIME,");
            subsql.append(" '' OP_ADDR, BASIC.OP_OC_ADDR_TYPE_CD, BASIC.OP_PUPO_CITY_NM, BASIC.OP_PUPO_TOWN_NM, BASIC.OP_PUPO_VILLAGE_NM, BASIC.OP_PUPO_LIN, BASIC.OP_PUPO_ROAD, BASIC.OP_PUPO_ADDR_OTH,");
            subsql.append(" BASIC.OP_PUPO_PHONENO, BASIC.OP_AC_STAFF_NM, BASIC.OP_CURSTAT_CD, BASIC.OP_PU_CITY_NM, BASIC.OP_PU_TOWN_NM, BASIC.OP_PU_PLACE,");
            subsql.append(" '' OP_PUOJ_NM1, '' OP_QTY1, '' OP_QTY_UNIT1, '' OP_FEATURE1,");
            subsql.append(" '' OP_PUOJ_NM2, '' OP_QTY2, '' OP_QTY_UNIT2, '' OP_FEATURE2,");
            subsql.append(" '' OP_PUOJ_NM3, '' OP_QTY3, '' OP_QTY_UNIT3, '' OP_FEATURE3,");
            subsql.append(" '' OP_PUOJ_NM4, '' OP_QTY4, '' OP_QTY_UNIT4, '' OP_FEATURE4,");
            subsql.append(" '' OP_PUOJ_NM5, '' OP_QTY5, '' OP_QTY_UNIT5, '' OP_FEATURE5");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" WHERE 1=1 ");
            
            //受理基本資料序號
            if (obj.has("OP_SEQ_NO") && !obj.getString("OP_SEQ_NO").equals("")) {
                subsql.append( " AND BASIC.OP_SEQ_NO = ?  ");
                args.add(obj.getInt("OP_SEQ_NO"));
            }
            
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            while (crs.next()) {
                crs.updateString("OP_AC_RCNO",  crs.getString("OP_AC_RCNO"));
                crs.updateString("OP_PU_PLACE",  crs.getString("OP_PU_CITY_NM")+crs.getString("OP_PU_TOWN_NM")+crs.getString("OP_PU_PLACE"));
                if (crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE")))
                    crs.updateString("OP_PU_DATE", "");
                else
                    crs.updateString("OP_PU_DATE", getDBToReportDate(crs.getString("OP_PU_DATE")));
                if (crs.getString("OP_PU_TIME")==null || "".equals(crs.getString("OP_PU_TIME")))
                    crs.updateString("OP_PU_TIME", "");
                else
                    crs.updateString("OP_PU_TIME", DateUtil.toTwTime(crs.getString("OP_PU_TIME")));
                StringBuilder addr = new StringBuilder();
                if ("1".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_CITY_NM")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_CITY_NM"));
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_TOWN_NM")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_TOWN_NM"));
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_VILLAGE_NM")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_VILLAGE_NM"));
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_LIN")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_LIN"));
                        addr.append("鄰");
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_ROAD")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_ROAD"));
                    }

                }else if ("9".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_ADDR_OTH")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_ADDR_OTH"));
                    }
                }else{
                    addr.append("");
                }
                crs.updateString("OP_ADDR", addr.toString());
                ArrayList<HashMap> detailList = GetDetail ( crs.getString("OP_SEQ_NO") ) ;
                map.put("detailList", detailList);
            }
            map.put("BasicList", crs);
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    //拾得物收據
    public HashMap printByOP02A11Q_DOC(JSONObject obj) {
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_SEQ_NO, BASIC.OP_AC_RCNO, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_DATE, BASIC.OP_PUPO_NAME, BASIC.OP_PUPO_IDN, BASIC.OP_PU_DATE, BASIC.OP_PU_TIME,");
            subsql.append(" '' OP_ADDR, BASIC.OP_OC_ADDR_TYPE_CD, BASIC.OP_PUPO_CITY_NM, BASIC.OP_PUPO_TOWN_NM, BASIC.OP_PUPO_VILLAGE_NM, BASIC.OP_PUPO_LIN, BASIC.OP_PUPO_ROAD, BASIC.OP_PUPO_ADDR_OTH,");
            subsql.append(" BASIC.OP_PUPO_PHONENO, BASIC.OP_AC_STAFF_NM, BASIC.OP_CURSTAT_CD, BASIC.OP_PU_CITY_NM, BASIC.OP_PU_TOWN_NM, BASIC.OP_PU_PLACE,");
            subsql.append(" '' OP_PUOJ_NM1, '' OP_QTY1, '' OP_QTY_UNIT1, '' OP_FEATURE1,");
            subsql.append(" '' OP_PUOJ_NM2, '' OP_QTY2, '' OP_QTY_UNIT2, '' OP_FEATURE2,");
            subsql.append(" '' OP_PUOJ_NM3, '' OP_QTY3, '' OP_QTY_UNIT3, '' OP_FEATURE3,");
            subsql.append(" '' OP_PUOJ_NM4, '' OP_QTY4, '' OP_QTY_UNIT4, '' OP_FEATURE4,");
            subsql.append(" '' OP_PUOJ_NM5, '' OP_QTY5, '' OP_QTY_UNIT5, '' OP_FEATURE5,");
            subsql.append(" '' OP_PUOJ_NM6, '' OP_QTY6, '' OP_QTY_UNIT6, '' OP_FEATURE6");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" WHERE 1=1 ");
            
            //受理基本資料序號
            if (obj.has("OP_SEQ_NO") && !obj.getString("OP_SEQ_NO").equals("")) {
                subsql.append( " AND BASIC.OP_SEQ_NO = ?  ");
                args.add(obj.getInt("OP_SEQ_NO"));
            }
            
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            while (crs.next()) {
                crs.updateString("OP_AC_RCNO",  crs.getString("OP_AC_RCNO"));
                crs.updateString("OP_PU_PLACE",  crs.getString("OP_PU_CITY_NM")+crs.getString("OP_PU_TOWN_NM")+crs.getString("OP_PU_PLACE"));
                if (crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE")))
                    crs.updateString("OP_PU_DATE", "");
                else
                    crs.updateString("OP_PU_DATE", getDBToReportDate(crs.getString("OP_PU_DATE")));
                if (crs.getString("OP_PU_TIME")==null || "".equals(crs.getString("OP_PU_TIME")))
                    crs.updateString("OP_PU_TIME", "");
                else
                    crs.updateString("OP_PU_TIME", DateUtil.toTwTime(crs.getString("OP_PU_TIME")));
                StringBuilder addr = new StringBuilder();
                if ("1".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_CITY_NM")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_CITY_NM"));
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_TOWN_NM")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_TOWN_NM"));
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_VILLAGE_NM")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_VILLAGE_NM"));
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_LIN")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_LIN"));
                        addr.append("鄰");
                    }
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_ROAD")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_ROAD"));
                    }

                }else if ("9".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                    if (!StringUtil.nvl(crs.getString("OP_PUPO_ADDR_OTH")).equals("")) {
                        addr.append( crs.getString("OP_PUPO_ADDR_OTH"));
                    }
                }else{
                    addr.append("");
                }
                crs.updateString("OP_ADDR", addr.toString());
                ArrayList<HashMap> detailList = GetDetailMobile ( crs.getString("OP_SEQ_NO") ) ;
                map.put("detailList", detailList);
            }
            map.put("BasicList", crs);
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    //受理拾得物案陳報單
    public HashMap printByOP02A09Q_DOC(JSONObject obj)throws SQLException
    {
       HashMap<String, Object> map = new HashMap();
        ArrayList qsPara = new ArrayList();
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        ArrayList<HashMap> basicList = null;
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_AC_UNIT_NM ,BASIC.OP_AC_DATE ,BASIC.OP_PUPO_NAME ,BASIC.OP_PU_DATE ,BASIC.OP_PU_TIME,BASIC.OP_PU_CITY_NM ,BASIC.OP_PU_TOWN_NM ,BASIC.OP_PU_PLACE ,BASIC.OP_AC_RCNO ,BASIC.OP_PUPO_TP_NM "); 
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC");
            subsql.append(" WHERE 1=1 ");

            //受理基本資料序號
             if (obj.has("OP_SEQ_NO") && !obj.getString("OP_SEQ_NO").equals("")) 
             {
                subsql.append( " AND BASIC.OP_SEQ_NO = ?  ");
                args.add(obj.getInt("OP_SEQ_NO"));
              }
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            crs.next();
            crs.updateString("OP_PU_PLACE",  crs.getString("OP_PU_CITY_NM")+crs.getString("OP_PU_TOWN_NM")+crs.getString("OP_PU_PLACE"));
            map.put("BasicList", crs);
         
            String puojNm = getOpPuojNmAndQty( obj.getString("OP_SEQ_NO"));
            map.put("DetailList",  puojNm);
        }
         catch (JSONException e) 
          {
                log.error(ExceptionUtil.toString(e));
          }
       
        return map;
    }
    public HashMap printByOP02A10Q_DOC(JSONObject obj)throws SQLException
    {
       HashMap<String, Object> map = new HashMap();
        ArrayList qsPara = new ArrayList();
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        ArrayList<HashMap> basicList = null;
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_AC_UNIT_NM,BASIC.OP_PUPO_NAME,BASIC.OP_AC_D_UNIT_NM "); 
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC");
            subsql.append(" WHERE 1=1 ");

            //受理基本資料序號
             if (obj.has("OP_SEQ_NO") && !obj.getString("OP_SEQ_NO").equals("")) 
             {
                subsql.append( " AND BASIC.OP_SEQ_NO = ?  ");
                args.add(obj.getInt("OP_SEQ_NO"));
              }
            basicList = this.pexecuteQuery(subsql.toString(), args.toArray());
            map.put("BasicList", basicList);
        }
         catch (JSONException e) 
          {
                log.error(ExceptionUtil.toString(e));
          }
       
        return map;
    }
    //受理民眾交存拾得遺失物作業程序檢核表
    public HashMap printByOP02A03Q_DOC(JSONObject obj) {
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_PUPO_NAME");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" WHERE 1=1 ");
            
            
            //受理基本資料序號
            if (obj.has("OP_SEQ_NO") && !obj.getString("OP_SEQ_NO").equals("")) {
                subsql.append( " AND BASIC.OP_SEQ_NO = ?  ");
                args.add(obj.getInt("OP_SEQ_NO"));
            }
            
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            map.put("BasicList", crs);
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    
    //核對情形回覆函–核對結果正確,核對情形回覆函–核對結果錯誤
    public HashMap printByOP02A04Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
        ArrayList<HashMap> claimList = null;
         ArrayList<HashMap> AnnounceList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO,"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            sql = " SELECT OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD,"
                       + " OP_DOC_NO"
                       + " FROM OPDT_I_ANNOUNCE "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnnounceList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnnounceList", AnnounceList);
            qsPara.clear();
            if( obj.getString("CLAIM_TYPE").equals("Claim") ){
                sql = " SELECT  OP_PUCP_NAME, OP_FM_DATE"
                    + " FROM OPDT_I_PU_CLAIM "
                    + " WHERE OP_SEQ_NO = ? ";
                qsPara.add( Integer.parseInt( obj.getString("OP_SEQ_NO") ) );
                
                claimList = this.pexecuteQuery( sql, qsPara.toArray());
                map.put("claimList", claimList);
            }else if ( obj.getString("CLAIM_TYPE").equals("NetClm") ){
                sql = " SELECT  OP_PUCP_NAME, OP_FM_DATE"
                    + " FROM OPDT_E_NET_CLAIM "
                    + " WHERE OP_SEQ_NO = ? ";
                qsPara.add( Integer.parseInt( obj.getString("OP_SEQ_NO") ) );
                
                claimList = this.pexecuteQuery( sql, qsPara.toArray());
                map.put("claimList", claimList);
            }else if ( obj.getString("CLAIM_TYPE").equals("NOClaim") ){
                map.put("claimList", claimList);
            }
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    
    //遺失（拾得）物領據
    public HashMap printByOP02A06Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
        ArrayList<HashMap> claimList = null;
        ArrayList<HashMap> detailList = null;
        ArrayList<HashMap> AnnounceList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_AC_RCNO,OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO,"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";//序號
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );//拾得遺失物事件基本資料序號

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            detailList = GetDetail ( obj.getString("OP_BASIC_SEQ_NO") ) ;
            map.put("detailList", detailList);
            
            qsPara.clear();
            if( obj.has("CLAIM_TYPE") && !obj.getString("CLAIM_TYPE").equals("") ){
                if( obj.getString("CLAIM_TYPE").equals("Claim") )
                {
                    sql = " SELECT  OP_PUCP_NAME, OP_FM_DATE,''OP_PUCP_ADDR, OP_PUCP_IDN, OP_PUCP_PHONENO,"
                        + " OP_PUCP_ADDR_TYPE_CD, OP_PUCP_CITY_NM, OP_PUCP_TOWN_NM, OP_PUCP_VILLAGE_NM, OP_PUCP_LIN,"
                        + " OP_PUCP_ROAD, OP_PUCP_ADDR_OTH"//認領人姓名住址等相關資訊
                        + " FROM OPDT_I_PU_CLAIM "//民眾網路認領資料
                        + " WHERE OP_SEQ_NO = ? ";//序號
                    qsPara.add( Integer.parseInt( obj.getString("OP_SEQ_NO") ) );

                    claimList = this.pexecuteQuery( sql, qsPara.toArray());
                }else if ( obj.getString("CLAIM_TYPE").equals("NetClm") )
                {sql = " SELECT  OP_PUCP_NAME, OP_FM_DATE,''OP_PUCP_ADDR, OP_PUCP_IDN, OP_PUCP_PHONENO,"
                        + " OP_PUCP_ADDR_TYPE_CD, OP_PUCP_CITY_NM, OP_PUCP_TOWN_NM, OP_PUCP_VILLAGE_NM, OP_PUCP_LIN,"
                        + " OP_PUCP_ROAD, OP_PUCP_ADDR_OTH"//認領人姓名住址等相關資訊
                        + " FROM OPDT_E_NET_CLAIM "//民眾網路認領資料
                        + " WHERE OP_SEQ_NO = ? ";//序號
                    qsPara.add( Integer.parseInt( obj.getString("OP_SEQ_NO") ) );

                    claimList = this.pexecuteQuery( sql, qsPara.toArray());
                }
                StringBuilder addr = new StringBuilder();
                if( claimList.get(0).get("OP_PUCP_ADDR_TYPE_CD").equals("1") ){
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_CITY_NM")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_CITY_NM"));
                    }
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_TOWN_NM")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_TOWN_NM"));
                    }
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_VILLAGE_NM")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_VILLAGE_NM"));
                    }
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_ROAD")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_ROAD"));
                    }
                }else if ( claimList.get(0).get("OP_PUCP_ADDR_TYPE_CD").equals("9") ){
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_ADDR_OTH")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_ADDR_OTH"));
                    }
                }else{
                    addr.append("");
                }
                claimList.get(0).replace("OP_PUCP_ADDR", addr);
                map.put("claimList", claimList);
            }else{
                map.put("claimList", claimList);
            }
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    
    //已認領通知函
    public HashMap printByOP02A08Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
        ArrayList<HashMap> claimList = null;
        ArrayList<HashMap> detailList = null;
         ArrayList<HashMap> AnnounceList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_SEQ_NO, OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO,"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL, OP_PU_DATE, OP_NTC_FIND_PO, OP_PUPO_NAME"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            sql = " SELECT OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD,"
                       + " OP_DOC_NO"
                       + " FROM OPDT_I_ANNOUNCE "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnnounceList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnnounceList", AnnounceList);
            
            qsPara.clear();
            if( obj.has("CLAIM_TYPE") && !obj.getString("CLAIM_TYPE").equals("") ){
                if( obj.getString("CLAIM_TYPE").equals("Claim") ){
                    sql = " SELECT  OP_PUCP_NAME, OP_FM_DATE, '' OP_PUCP_ADDR, OP_PUCP_IDN, OP_PUCP_PHONENO,"
                        + " OP_PUCP_ADDR_TYPE_CD, OP_PUCP_CITY_NM, OP_PUCP_TOWN_NM, OP_PUCP_VILLAGE_NM, OP_PUCP_LIN,"
                        + " OP_PUCP_ROAD, OP_PUCP_ADDR_OTH"
                        + " FROM OPDT_I_PU_CLAIM "
                        + " WHERE OP_SEQ_NO = ? ";
                    qsPara.add( Integer.parseInt( obj.getString("OP_SEQ_NO") ) );

                    claimList = this.pexecuteQuery( sql, qsPara.toArray());
                }else if ( obj.getString("CLAIM_TYPE").equals("NetClm") ){
                    sql = " SELECT  OP_PUCP_NAME, OP_FM_DATE, '' OP_PUCP_ADDR, OP_PUCP_IDN, OP_PUCP_PHONENO,"
                        + " OP_PUCP_ADDR_TYPE_CD, OP_PUCP_CITY_NM, OP_PUCP_TOWN_NM, OP_PUCP_VILLAGE_NM, OP_PUCP_LIN,"
                        + " OP_PUCP_ROAD, OP_PUCP_ADDR_OTH"
                        + " FROM OPDT_E_NET_CLAIM "
                        + " WHERE OP_SEQ_NO = ? ";
                    qsPara.add( Integer.parseInt( obj.getString("OP_SEQ_NO") ) );

                    claimList = this.pexecuteQuery( sql, qsPara.toArray());
                }
                StringBuilder addr = new StringBuilder();
                if( claimList.get(0).get("OP_PUCP_ADDR_TYPE_CD").equals("1") ){
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_CITY_NM")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_CITY_NM"));
                    }
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_TOWN_NM")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_TOWN_NM"));
                    }
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_VILLAGE_NM")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_VILLAGE_NM"));
                    }
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_ROAD")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_ROAD"));
                    }
                }else if ( claimList.get(0).get("OP_PUCP_ADDR_TYPE_CD").equals("9") ){
                    if (!StringUtil.nvl(claimList.get(0).get("OP_PUCP_ADDR_OTH")).equals("")) {
                        addr.append( claimList.get(0).get("OP_PUCP_ADDR_OTH"));
                    }
                }else{
                    addr.append("");
                }
                claimList.get(0).replace("OP_PUCP_ADDR", addr);
                map.put("claimList", claimList);
            }else{
                map.put("claimList", claimList);
            }
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    
    //拾得物公告招領公告單
    public HashMap printByOP03A01Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
         ArrayList<HashMap> AnnounceList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_SEQ_NO, OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO,"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL, OP_PU_DATE, OP_NTC_FIND_PO, OP_PUPO_NAME"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            sql = " SELECT OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD, OP_AN_UNIT_NM, OP_AN_B_UNIT_NM, OP_AN_D_UNIT_NM,"
                       + " OP_DOC_NO"
                       + " FROM OPDT_I_ANNOUNCE "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnnounceList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnnounceList", AnnounceList);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    //拾得物公告招領清冊
    public Map<String, List> printByOP03A02Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        String OP_AC_UNIT_NM = "";
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT DETAIL.OP_PUOJ_NM, DETAIL.OP_QTY, DETAIL.OP_QTY, DETAIL.OP_QTY_UNIT, DETAIL.OP_REMARK, DETAIL.OP_TYPE_CD, DETAIL.OP_TYPE_NM,");
            subsql.append(" '' OP_PU_ADDR, BASIC.OP_PU_CITY_NM, BASIC.OP_PU_TOWN_NM, BASIC.OP_PU_PLACE, BASIC.OP_PU_DATE,");
            subsql.append(" BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_B_UNIT_CD, BASIC.OP_AC_B_UNIT_NM, BASIC.OP_AC_D_UNIT_CD, BASIC.OP_AC_D_UNIT_NM");
            subsql.append(" FROM OPDT_I_PU_DETAIL DETAIL ");
            subsql.append(" LEFT JOIN OPDT_I_PU_BASIC BASIC on BASIC.OP_SEQ_NO = DETAIL.OP_BASIC_SEQ_NO  ");
            subsql.append(" WHERE 1=1 ");
            subsql.append( " AND DETAIL.OP_BASIC_SEQ_NO = ?  ");
            
            //基本受理序號
            args.add( Integer.parseInt(obj.getString("OP_BASIC_SEQ_NO")) );
              
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());

            Boolean flagData = false;
            while (crs.next()) {
                flagData = true;
                HashMap data = new HashMap();
                StringBuilder addr = new StringBuilder();
                StringBuilder puojNm = new StringBuilder();
                //拾得地點
                if (!StringUtil.nvl(crs.getString("OP_PU_CITY_NM")).equals("")) {
                    addr.append( crs.getString("OP_PU_CITY_NM"));
                }
                if (!StringUtil.nvl(crs.getString("OP_PU_TOWN_NM")).equals("")) {
                    addr.append( crs.getString("OP_PU_TOWN_NM"));
                }
                if (!StringUtil.nvl(crs.getString("OP_PU_PLACE")).equals("")) {
                    addr.append( crs.getString("OP_PU_PLACE"));
                }
                //拾得物品
                if (!StringUtil.nvl(crs.getString("OP_PUOJ_NM")).equals("")) {
                    puojNm.append( crs.getString("OP_PUOJ_NM") + " ");
                }
                if( !StringUtil.nvl(crs.getString("OP_TYPE_CD")).equals("") )
                {
                  //G001是其他  (在舊物品種類中，G001是新台幣。  在新物品種類中A001是新臺幣 )
                  //if( StringUtil.nvl(crs.getString("OP_TYPE_CD")).equals("G001") )
                    if( StringUtil.nvl(crs.getString("OP_TYPE_CD")).equals("A001") )
                    { //新台幣
                        puojNm.append( " 若干元");
                    }
                    else{
                        if (!StringUtil.nvl(crs.getString("OP_QTY")).equals("")) {
                            String[] opQty = crs.getString("OP_QTY").toString().split("\\.");
                            if( opQty[1].equals("00") ){
                                puojNm.append( opQty[0] );
                            }else{
                                puojNm.append( crs.getString("OP_QTY"));
                            }
                        }
                        if (!StringUtil.nvl(crs.getString("OP_QTY_UNIT")).equals("")) {
                            puojNm.append( crs.getString("OP_QTY_UNIT"));
                        }
                    }
                }
                data.put("OP_PUOJ_NM", puojNm.toString());
                
                data.put("OP_PU_ADDR", addr.toString());
                
                if (crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE")))
                    data.put("OP_PU_DATE", "");
                else
                    data.put("OP_PU_DATE", DateUtil.get14DateFormat(crs.getString("OP_PU_DATE")+"000000" ).substring(0,9));
                
                data.put("OP_REMARK", crs.getString("OP_REMARK"));
                
                datas.add(data);
            }
            if (flagData) {
                list.put("header", datas);
            } else {
                HashMap data = new HashMap();
                data.put("OP_PUOJ_NM", "");
                data.put("OP_PU_ADDR", "");
                data.put("OP_PU_DATE", "");
                data.put("OP_REMARK", "");
                datas.add(data);
                list.put("header", datas);
            }
            JSONArray unitCd = daoNPAUNIT.getUnitCode(voUser.getUnitCd());
            System.out.println(unitCd.getJSONObject(0).getString("OP_BRANCH_CD"));
            OP_AC_UNIT_NM = getUnitNm( unitCd.getJSONObject(0).getString("OP_BRANCH_CD") );
            tempObj.put("UNIT", OP_AC_UNIT_NM + "公告招領拾得物清冊");

            tempObj.put("TODAY", "製表日期：" + getNowTime());
            tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
            obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    
    //拾得人領回通知
    public HashMap printByOP03A03Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
        ArrayList<HashMap> AnnounceList = null;
        ArrayList<HashMap> AnDlList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_SEQ_NO, OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO,"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL, OP_PU_DATE, OP_NTC_FIND_PO, OP_PUPO_NAME"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            sql = " SELECT OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD, OP_AN_UNIT_NM, OP_AN_B_UNIT_NM, OP_AN_D_UNIT_NM,"
                       + " OP_DOC_NO"
                       + " FROM OPDT_I_ANNOUNCE "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnnounceList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnnounceList", AnnounceList);
            
            sql = " SELECT OP_NTC_PUPO_DT, OP_PUPO_DOC_WD, OP_PUPO_DOC_NO"
                       + " FROM OPDT_I_AN_DL "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnDlList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnDlList", AnDlList);
            
            qsPara.clear();
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    
    //拾得人領回公告
    public HashMap printByOP03A04Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
        ArrayList<HashMap> AnnounceList = null;
        ArrayList<HashMap> AnDlList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_SEQ_NO, OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO, OP_PUPO_GENDER"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL, OP_PU_DATE, OP_NTC_FIND_PO, OP_PUPO_NAME"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            sql = " SELECT OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD, OP_AN_UNIT_NM, OP_AN_B_UNIT_NM, OP_AN_D_UNIT_NM,"
                       + " OP_DOC_NO"
                       + " FROM OPDT_I_ANNOUNCE "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnnounceList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnnounceList", AnnounceList);
            
            sql = " SELECT OP_NTC_PUPO_DT, OP_PUPO_DOC_WD, OP_PUPO_DOC_NO"
                       + " FROM OPDT_I_AN_DL "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnDlList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnDlList", AnDlList);
            
            qsPara.clear();
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    
    //拾得物送交地方自治團體函
    public HashMap printByOP04A01Q_DOC(JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        User voUser = new User();
        ArrayList<HashMap> basicList = null;
         ArrayList<HashMap> AnnounceList = null;
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            String sql = " SELECT  OP_SEQ_NO, OP_AC_UNIT_NM, OP_AC_B_UNIT_NM, OP_AC_D_UNIT_NM, OP_AC_RCNO,"
                       + " OP_AC_STAFF_NM, OP_AC_UNIT_TEL, OP_PU_DATE, OP_NTC_FIND_PO, OP_PUPO_NAME"
                       + " FROM OPDT_I_PU_BASIC "
                       + " WHERE OP_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( obj.getString("OP_BASIC_SEQ_NO") ) );

            basicList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("basicList", basicList);
            
            sql = " SELECT OP_AN_DATE_BEG, OP_AN_DATE_END, OP_DOC_WD, OP_AN_UNIT_NM, OP_AN_B_UNIT_NM, OP_AN_D_UNIT_NM,"
                       + " OP_DOC_NO"
                       + " FROM OPDT_I_ANNOUNCE "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            AnnounceList = this.pexecuteQuery( sql, qsPara.toArray());
            map.put("AnnounceList", AnnounceList);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return map;
    }
    //拾得人逾期未領取拾得物清冊
    public Map<String, List> printByOP04A02Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT DETAIL.OP_PUOJ_NM, DETAIL.OP_FEATURE, DETAIL.OP_QTY, DETAIL.OP_QTY_UNIT,  ");
            subsql.append(" BASIC.OP_PUPO_NAME, ANDL.OP_NTC_PUPO_DT, BASIC.OP_AC_UNIT_NM  ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC INNER JOIN OPDT_I_PU_DETAIL DETAIL ON DETAIL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO ");
            subsql.append(" LEFT JOIN OPDT_I_AN_DL ANDL ON ANDL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO");
            subsql.append(" WHERE 1=1 ");
            subsql.append(" AND BASIC.OP_CURSTAT_CD = '5'  ");
            subsql.append(" AND BASIC.OP_SEQ_NO = DETAIL.OP_BASIC_SEQ_NO  ");
            subsql.append(" AND ISNULL(BASIC.OP_DEL_FLAG,'0')<>'1' ");

            //收據編號
            if (obj.has("OP_BASIC_SEQ_NO") && !obj.getString("OP_BASIC_SEQ_NO").equals("")) {
                subsql.append( " AND DETAIL.OP_BASIC_SEQ_NO = ?  ");
                args.add(obj.getInt("OP_BASIC_SEQ_NO"));
            }
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            Boolean flagData = false;
            while (crs.next()) {
                flagData = true;
                HashMap data = new HashMap();
                StringBuilder addr = new StringBuilder();
                String strOP_PUOJ_NM = "";
                if(crs.getString("OP_PUOJ_NM")!=null && !"".equals(crs.getString("OP_PUOJ_NM"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUOJ_NM") + "-";
                }
                if(crs.getString("OP_FEATURE")!=null && !"".equals(crs.getString("OP_FEATURE"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_FEATURE") + "-";
                }
                if(crs.getString("OP_QTY")!=null && !"".equals(crs.getString("OP_QTY"))){
                    String[] opQty = crs.getString("OP_QTY").toString().split("\\.");
                    if( opQty[1].equals("00") ){
                        strOP_PUOJ_NM = strOP_PUOJ_NM + opQty[0] + "-";
                    }else{
                        strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_QTY") + "-";
                    }
                }
                if(crs.getString("OP_QTY_UNIT")!=null && !"".equals(crs.getString("OP_QTY_UNIT"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_QTY_UNIT") ;
                }
                data.put("OP_PUOJ_NM", strOP_PUOJ_NM);
                data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));
                if (crs.getString("OP_NTC_PUPO_DT")==null || "".equals(crs.getString("OP_NTC_PUPO_DT"))){
                    data.put("OP_NTC_PUPO_DT", "");
                } else{
                    data.put("OP_NTC_PUPO_DT", DateUtil.to7TwDateTime(crs.getString("OP_NTC_PUPO_DT").toString()));
                }
                data.put("OP_AC_UNIT_NM", crs.getString("OP_AC_UNIT_NM"));
                datas.add(data);
            }
            if (flagData) {
                list.put("header", datas);
            } else {
                HashMap data = new HashMap();
                data.put("OP_PUOJ_NM", "");
                data.put("OP_PUPO_NAME", "");
                data.put("OP_NTC_PUPO_DT", "");
                data.put("OP_AC_UNIT_NM", "");
                datas.add(data);
                list.put("header", datas);
            }
            obj.put("newObj", tempObj);
            
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }

    public Map<String, List> printByOP07A01Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" select BASIC.OP_SEQ_NO,BASIC.OP_AC_RCNO, '' OP_PUOJ_NM, BASIC.OP_AC_UNIT_NM,BASIC.OP_AC_UNIT_CD,BASIC.OP_AC_DATE,BASIC.OP_PUPO_NAME ");
            subsql.append(" ,BASIC.OP_OC_ADDR_TYPE_CD,BASIC.OP_PUPO_CITY_NM,BASIC.OP_PUPO_TOWN_NM,BASIC.OP_PUPO_VILLAGE_NM,BASIC.OP_PUPO_LIN,BASIC.OP_PUPO_ROAD,BASIC.OP_PUPO_ADDR_OTH ");
            subsql.append(" ,BASIC.OP_PUPO_PHONENO,BASIC.OP_AC_STAFF_NM,BASIC.OP_PU_YN_OV500,ANNOUNCE.OP_AN_DATE_BEG,ANNOUNCE.OP_DOC_WD,OP_DOC_WD,ANNOUNCE.OP_DOC_NO,BASIC.OP_CURSTAT_CD ");
            subsql.append(" from OPDT_I_PU_BASIC BASIC ");
//            subsql.append(" inner join OPDT_I_PU_DETAIL DETAIL on BASIC.OP_SEQ_NO = DETAIL.OP_BASIC_SEQ_NO ");
            subsql.append(" left join OPDT_I_ANNOUNCE ANNOUNCE on BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO  ");
            subsql.append(" where 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' ");
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //物品屬性
            if (obj.has("OP_PUOJ_ATTR_CD") && !obj.getString("OP_PUOJ_ATTR_CD").equals("")) {
                subsql.append( " AND BASIC.OP_PUOJ_ATTR_CD = ?  ");
                args.add(obj.getString("OP_PUOJ_ATTR_CD"));
            }
            //拾得物總價值
             if (obj.has("OP_PU_YN_OV500") && !obj.getString("OP_PU_YN_OV500").equals("")) {
                subsql.append( " AND BASIC.OP_PU_YN_OV500 = ?  ");
                args.add(obj.getString("OP_PU_YN_OV500"));
            }
             subsql.append(" order by BASIC.OP_AC_DATE ");//20220505 加時間排序
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              int i =1;
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    if ("1".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_CITY_NM")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_CITY_NM"));
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_TOWN_NM")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_TOWN_NM"));
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_VILLAGE_NM")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_VILLAGE_NM"));
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_LIN")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_LIN"));
                            addr.append("鄰");
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_ROAD")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_ROAD"));
                        }
                          
                    }else if ("9".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_ADDR_OTH")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_ADDR_OTH"));
                        }
                    }else{
                        addr.append("");
                    }
                    data.put("SEQ", i);
                    i++;
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                    data.put("OP_PUOJ_NM", getOpPuojNmAndQty(crs.getString("OP_SEQ_NO")));//要在去抓資料
                    data.put("OP_AC_UNIT_NM", crs.getString("OP_AC_UNIT_NM"));
                     if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE")))
                        data.put("OP_AC_DATE", "");
                    else
                        data.put("OP_AC_DATE", DateUtil.to7TwDateTime( crs.getString("OP_AC_DATE").toString() ));
                    data.put("OP_ADDR", addr.toString());
                    data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));//OP_AC_UNIT_CD
                    data.put("OP_PUPO_PHONENO", crs.getString("OP_PUPO_PHONENO"));
                    data.put("OP_AC_UNIT_NM", getUnitSNm(crs.getString("OP_AC_UNIT_CD")));
                    String OP_DOC_WD="";
                    if (crs.getString("OP_DOC_WD") ==null || crs.getString("OP_DOC_NO")==null )
                        OP_DOC_WD="";
                    else 
                        OP_DOC_WD = crs.getString("OP_DOC_WD")+crs.getString("OP_DOC_NO");
                    data.put("OP_DOC_WD", OP_DOC_WD);
                    data.put("OP_AC_STAFF_NM", crs.getString("OP_AC_STAFF_NM"));
                    
                    StringBuilder CURSTAT = new StringBuilder();
                    if ("1".equals(crs.getString("OP_CURSTAT_CD"))){
                        if("1".equals(crs.getString("OP_PU_YN_OV500")))
                            CURSTAT.append("■ 處理中 □ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");// 換行符號\n
                        else
                            CURSTAT.append("■ 處理中 □ 已結案");// 換行符號\n
                    }else if ("2".equals(crs.getString("OP_CURSTAT_CD"))){
                        CURSTAT.append("□ 處理中 ■ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("3".equals(crs.getString("OP_CURSTAT_CD"))){
                        CURSTAT.append("□ 處理中 □ 公告中\n■ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("4".equals(crs.getString("OP_CURSTAT_CD"))){
                         CURSTAT.append("□ 處理中 □ 公告中\n□ 公告期滿 \n■ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("5".equals(crs.getString("OP_CURSTAT_CD"))){
                         CURSTAT.append("□ 處理中 □ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n■ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("6".equals(crs.getString("OP_CURSTAT_CD"))){
                        if("1".equals(crs.getString("OP_PU_YN_OV500")))
                            CURSTAT.append("□ 處理中 □ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n■ 已結案");
                        else
                            CURSTAT.append("□ 處理中 ■ 已結案");
                    }
                    data.put("OP_CURSTAT_CD", CURSTAT.toString());
                     if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG")))
                        data.put("OP_AN_DATE_BEG", "");
                    else
                        data.put("OP_AN_DATE_BEG", DateUtil.to7TwDateTime( crs.getString("OP_AN_DATE_BEG").toString() ));
                   
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("SEQ", "");
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("OP_AC_UNIT_NM", "");
                    data.put("OP_AC_DATE", "");
                    data.put("OP_ADDR", "");
                    data.put("OP_PUPO_NAME", "");
                    data.put("OP_PUPO_PHONENO", "");
                    data.put("OP_AC_UNIT_NM", "");
                    data.put("OP_DOC_WD", "");
                    data.put("OP_AC_STAFF_NM", "");
                    data.put("OP_CURSTAT_CD", "");
                    data.put("OP_AN_DATE_BEG", "");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr="";
                if (obj.has("OP_PU_YN_OV500") && StringUtil.nvl(obj.getString("OP_PU_YN_OV500")).equals("0")){
                    tmpstr="(伍佰元(含)以下)";
                }
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得物登記簿"+tmpstr);
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得物登記簿"+tmpstr);
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得物登記簿"+tmpstr);
                } else {
                    tempObj.put("UNIT", "拾得物登記簿"+tmpstr);
                }

                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public CachedRowSet printByOP07A01Q_DOC(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" select BASIC.OP_SEQ_NO,0 SEQ,BASIC.OP_AC_RCNO, '' OP_PUOJ_NM, '' OP_ADDR,BASIC.OP_AC_UNIT_NM,BASIC.OP_AC_UNIT_CD,BASIC.OP_AC_DATE,BASIC.OP_PUPO_NAME ");
            subsql.append(" ,BASIC.OP_OC_ADDR_TYPE_CD,BASIC.OP_PUPO_CITY_NM,BASIC.OP_PUPO_TOWN_NM,BASIC.OP_PUPO_VILLAGE_NM,BASIC.OP_PUPO_LIN,BASIC.OP_PUPO_ROAD,BASIC.OP_PUPO_ADDR_OTH ");
            subsql.append(" ,BASIC.OP_PUPO_PHONENO,BASIC.OP_AC_STAFF_NM,ANNOUNCE.OP_AN_DATE_BEG,BASIC.OP_PU_YN_OV500,ANNOUNCE.OP_DOC_WD,OP_DOC_WD,ANNOUNCE.OP_DOC_NO,BASIC.OP_CURSTAT_CD ");
            subsql.append(" from OPDT_I_PU_BASIC BASIC ");
//            subsql.append(" inner join OPDT_I_PU_DETAIL DETAIL on BASIC.OP_SEQ_NO = DETAIL.OP_BASIC_SEQ_NO ");
            subsql.append(" left join OPDT_I_ANNOUNCE ANNOUNCE on BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO  ");
            subsql.append(" where 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' ");
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //物品屬性
            if (obj.has("OP_PUOJ_ATTR_CD") && !obj.getString("OP_PUOJ_ATTR_CD").equals("")) {
                subsql.append( " AND BASIC.OP_PUOJ_ATTR_CD = ?  ");
                args.add(obj.getString("OP_PUOJ_ATTR_CD"));
            }
            //拾得物總價值
             if (obj.has("OP_PU_YN_OV500") && !obj.getString("OP_PU_YN_OV500").equals("")) {
                subsql.append( " AND BASIC.OP_PU_YN_OV500 = ?  ");
                args.add(obj.getString("OP_PU_YN_OV500"));
            }
            String tmpstr="";
                if (obj.has("OP_PU_YN_OV500") && StringUtil.nvl(obj.getString("OP_PU_YN_OV500")).equals("0")){
                    tmpstr="(伍佰元(含)以下)";
                }
             if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得物登記簿"+tmpstr);
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得物登記簿"+tmpstr);
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得物登記簿"+tmpstr);
                } else {
                    tempObj.put("UNIT", "拾得物登記簿"+tmpstr);
                }

                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
                subsql.append(" order by BASIC.OP_AC_DATE ");//20220505 加時間排序
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              int i =1;
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    if ("1".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_CITY_NM")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_CITY_NM"));
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_TOWN_NM")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_TOWN_NM"));
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_VILLAGE_NM")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_VILLAGE_NM"));
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_LIN")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_LIN"));
                            addr.append("鄰");
                        }
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_ROAD")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_ROAD"));
                        }
                          
                    }else if ("9".equals(crs.getString("OP_OC_ADDR_TYPE_CD"))){
                        if (!StringUtil.nvl(crs.getString("OP_PUPO_ADDR_OTH")).equals("")) {
                            addr.append( crs.getString("OP_PUPO_ADDR_OTH"));
                        }
                    }else{
                        addr.append("");
                    }
                    crs.updateInt("SEQ", i);
                    crs.updateString("OP_AC_RCNO",  crs.getString("OP_AC_RCNO"));
                    crs.updateString("OP_PUOJ_NM", getOpPuojNmAndQty(crs.getString("OP_SEQ_NO")));
                    crs.updateString("OP_AC_UNIT_NM", crs.getString("OP_AC_UNIT_NM"));
                    if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE")))
                         crs.updateString("OP_AC_DATE", "");
                    else
                        crs.updateString("OP_AC_DATE", DateUtil.get14DateFormat(crs.getString("OP_AC_DATE")+"000000" ).substring(0,9));
                    
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG")))
                        crs.updateString("OP_AN_DATE_BEG", "");
                    else
                        crs.updateString("OP_AN_DATE_BEG", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_BEG")+"000000" ).substring(0,9));
                    crs.updateString("OP_ADDR", addr.toString());
                    crs.updateString("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));
                    crs.updateString("OP_PUPO_PHONENO", crs.getString("OP_PUPO_PHONENO"));
                    crs.updateString("OP_AC_UNIT_NM", getUnitSNm(crs.getString("OP_AC_UNIT_CD")));
                    String OP_DOC_WD="";
                    if (crs.getString("OP_DOC_WD") ==null || crs.getString("OP_DOC_NO")==null )
                        OP_DOC_WD="";
                    else 
                        OP_DOC_WD = crs.getString("OP_DOC_WD")+crs.getString("OP_DOC_NO");
                    crs.updateString("OP_DOC_WD",  OP_DOC_WD);
                    crs.updateString("OP_AC_STAFF_NM", crs.getString("OP_AC_STAFF_NM"));
                    
                    i++;
                    
                    StringBuilder CURSTAT = new StringBuilder();
                    if ("1".equals(crs.getString("OP_CURSTAT_CD"))){
                        if("1".equals(crs.getString("OP_PU_YN_OV500")))
                            CURSTAT.append("■ 處理中 □ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");// 換行符號\n
                        else
                            CURSTAT.append("■ 處理中 □ 已結案");// 換行符號\n
                    }else if ("2".equals(crs.getString("OP_CURSTAT_CD"))){
                        CURSTAT.append("□ 處理中 ■ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("3".equals(crs.getString("OP_CURSTAT_CD"))){
                        CURSTAT.append("□ 處理中 □ 公告中\n■ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("4".equals(crs.getString("OP_CURSTAT_CD"))){
                         CURSTAT.append("□ 處理中 □ 公告中\n□ 公告期滿 \n■ 拾得人領回公告中\n□ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("5".equals(crs.getString("OP_CURSTAT_CD"))){
                         CURSTAT.append("□ 處理中 □ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n■ 拾得人領回公告期滿\n□ 已結案");
                    }else if ("6".equals(crs.getString("OP_CURSTAT_CD"))){
                        if("1".equals(crs.getString("OP_PU_YN_OV500")))
                            CURSTAT.append("□ 處理中 □ 公告中\n□ 公告期滿 \n□ 拾得人領回公告中\n□ 拾得人領回公告期滿\n■ 已結案");
                        else
                            CURSTAT.append("□ 處理中 ■ 已結案");
                    }
                    crs.updateString("OP_CURSTAT_CD", CURSTAT.toString());
                }
             
               
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return crs;
    }
    public CachedRowSet printUnitHeard(JSONObject obj){
        CachedRowSet crs =null ;
        ArrayList qsPara = new ArrayList();
        StringBuilder sql = new StringBuilder();
        sql.setLength(0);
        sql.append(" SELECT '"+obj.getString("UNIT")+"' as UNIT ,'"+obj.getString("TODAY")+"' as TODAY ,'"+obj.getString("NOW_UNIT")+"' as  NOW_UNIT  ");
        try{
            crs  = this.pexecuteQueryRowSet(sql.toString(),qsPara.toArray());

        } catch (Exception e){
            e.printStackTrace();
        }
         return crs;
    }
    public CachedRowSet printUnitHeard2(JSONObject obj){
        CachedRowSet crs =null ;
        ArrayList qsPara = new ArrayList();
        StringBuilder sql = new StringBuilder();
        sql.setLength(0);
        sql.append(" SELECT '"+obj.getString("UNIT")+"' as UNIT ");
        try{
            crs  = this.pexecuteQueryRowSet(sql.toString(),qsPara.toArray());

        } catch (Exception e){
            e.printStackTrace();
        }
         return crs;
    }
    public String getUnitSNm(String OP_UNIT_CD) {
		StringBuilder sql = new StringBuilder();
		ArrayList qsPara = new ArrayList();
                String OP_UNIT_S_NM = "";
		sql.setLength(0);
                sql.append(" SELECT OP_UNIT_S_NM  FROM OPDT_E0_NPAUNIT where OP_UNIT_CD = ? ");
                qsPara.add(OP_UNIT_CD);       
                
                 try{
		    CachedRowSet crs = this.pexecuteQueryRowSet(sql.toString(),qsPara.toArray());

                     while (crs.next()) {
                        OP_UNIT_S_NM = StringUtil.nvl(crs.getString("OP_UNIT_S_NM"));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
		return OP_UNIT_S_NM;
             
	}
         
    public String getUnitNm(String OP_UNIT_CD) {
            StringBuilder sql = new StringBuilder();
            ArrayList qsPara = new ArrayList();
            String OP_UNIT_NM = "";
            sql.setLength(0);
            sql.append(" SELECT OP_UNIT_NM  FROM OPDT_E0_NPAUNIT where OP_UNIT_CD = ? ");
            qsPara.add(OP_UNIT_CD);       

             try{
                CachedRowSet crs = this.pexecuteQueryRowSet(sql.toString(),qsPara.toArray());

                 while (crs.next()) {
                    OP_UNIT_NM = StringUtil.nvl(crs.getString("OP_UNIT_NM"));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return OP_UNIT_NM;

    }
    public String getOP_PUOJ_NM(String OP_BASIC_SEQ_NO) {
        ArrayList qsPara = new ArrayList();
        String puojNm = "";
        String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT"
                   + " FROM OPDT_I_PU_DETAIL "
                   + " WHERE OP_BASIC_SEQ_NO = ? ";
        qsPara.add( Integer.parseInt(OP_BASIC_SEQ_NO) );       

        try{
            ArrayList<HashMap> detaillist = this.pexecuteQuery( sql, qsPara.toArray());

            if(!detaillist.isEmpty()){
                for(int i = 1; i <= detaillist.size(); i++){
                    puojNm = puojNm + detaillist.get(i-1).get("OP_PUOJ_NM");
                    if( i <= detaillist.size()-1 )
                        puojNm = puojNm + ",";
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return puojNm;
    }
    public String getOpPuojNmAndQty(String OP_BASIC_SEQ_NO) {
        ArrayList qsPara = new ArrayList();
        String puojNm = "";
        String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT"
                   + " FROM OPDT_I_PU_DETAIL "
                   + " WHERE OP_BASIC_SEQ_NO = ? ";
        qsPara.add( Integer.parseInt(OP_BASIC_SEQ_NO) );       

        try{
            ArrayList<HashMap> detaillist = this.pexecuteQuery( sql, qsPara.toArray());

            if(!detaillist.isEmpty()){
                for(int i = 1; i <= detaillist.size(); i++){
                    puojNm = puojNm + detaillist.get(i-1).get("OP_PUOJ_NM");
                    if (!StringUtil.nvl(detaillist.get(i-1).get("OP_QTY")).equals("")) {
                        String[] opQty = detaillist.get(i-1).get("OP_QTY").toString().split("\\.");
                        if( opQty[1].equals("00") ){
                            puojNm = puojNm + opQty[0];
                        }else{
                            puojNm = puojNm + detaillist.get(i-1).get("OP_QTY").toString();
                        }
                    }
                    if (!StringUtil.nvl(detaillist.get(i-1).get("OP_QTY_UNIT")).equals("")) {
                        puojNm = puojNm + detaillist.get(i-1).get("OP_QTY_UNIT").toString();
                    }
                    if( i <= detaillist.size()-1 )
                        puojNm = puojNm + ",";
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return puojNm;
    }
    
    public String getOpPuojNmAndQty2(String OP_BASIC_SEQ_NO) {
        ArrayList qsPara = new ArrayList();
        String puojNm = "";
        String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT"
                   + " FROM OPDT_I_PU_DETAIL "
                   + " WHERE OP_BASIC_SEQ_NO = ? "
                 //+ " AND OP_TYPE_CD = 'G001' ";
                   + " AND OP_TYPE_CD = 'A001' "; //G001是其他  (在舊物品種類中，G001是新台幣。  在新物品種類中A001是新臺幣 )
                          
        qsPara.add( Integer.parseInt(OP_BASIC_SEQ_NO) );       

        try{
            ArrayList<HashMap> detaillist = this.pexecuteQuery( sql, qsPara.toArray());

            if(!detaillist.isEmpty()){
                for(int i = 1; i <= detaillist.size(); i++){
                    puojNm = puojNm + detaillist.get(i-1).get("OP_PUOJ_NM");
                    if (!StringUtil.nvl(detaillist.get(i-1).get("OP_QTY")).equals("")) {
                        String[] opQty = detaillist.get(i-1).get("OP_QTY").toString().split("\\.");
                        if( opQty[1].equals("00") ){
                            puojNm = puojNm + opQty[0];
                        }else{
                            puojNm = puojNm + detaillist.get(i-1).get("OP_QTY").toString();
                        }
                    }
                    if (!StringUtil.nvl(detaillist.get(i-1).get("OP_QTY_UNIT")).equals("")) {
                        puojNm = puojNm + detaillist.get(i-1).get("OP_QTY_UNIT").toString();
                    }
                    if( i <= detaillist.size()-1 )
                        puojNm = puojNm + ",";
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return puojNm;
    }
    
    public String getOpPuojNmAndQty3(String OP_BASIC_SEQ_NO) {
        ArrayList qsPara = new ArrayList();
        String puojNm = "";
        String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT"
                   + " FROM OPDT_I_PU_DETAIL "
                   + " WHERE OP_BASIC_SEQ_NO = ? "
                 //+ " AND OP_TYPE_CD <> 'G001' ";
                   + " AND OP_TYPE_CD <> 'A001' ";  //G001是其他  (在舊物品種類中，G001是新台幣。  在新物品種類中A001是新臺幣 )
        qsPara.add( Integer.parseInt(OP_BASIC_SEQ_NO) );       

        try{
            ArrayList<HashMap> detaillist = this.pexecuteQuery( sql, qsPara.toArray());

            if(!detaillist.isEmpty()){
                for(int i = 1; i <= detaillist.size(); i++){
                    puojNm = puojNm + detaillist.get(i-1).get("OP_PUOJ_NM");
                    if (!StringUtil.nvl(detaillist.get(i-1).get("OP_QTY")).equals("")) {
                        String[] opQty = detaillist.get(i-1).get("OP_QTY").toString().split("\\.");
                        if( opQty[1].equals("00") ){
                            puojNm = puojNm + opQty[0];
                        }else{
                            puojNm = puojNm + detaillist.get(i-1).get("OP_QTY").toString();
                        }
                    }
                    if (!StringUtil.nvl(detaillist.get(i-1).get("OP_QTY_UNIT")).equals("")) {
                        puojNm = puojNm + detaillist.get(i-1).get("OP_QTY_UNIT").toString();
                    }
                    if( i <= detaillist.size()-1 )
                        puojNm = puojNm + ",";
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return puojNm;
    }
    
    public String[] getOP_PUCP_NAME(String OP_BASIC_SEQ_NO, JSONObject obj) {
        ArrayList qsPara = new ArrayList();
        StringBuilder addr = new StringBuilder();
        String returnValue[] = {"",""};
        String strClaim = "";
        String bool = "N";
        String ClaimSql = " SELECT OP_BASIC_SEQ_NO, OP_PUCP_NAME, OP_PUCP_IDN, OP_PUCP_PHONENO, OP_PUCP_EMAIL, OP_AC_RCNO, OP_AC_UNIT_NM, OP_YN_LOSER,"
                        + " OP_PUCP_ZIPCODE, OP_PUCP_ADDR_TYPE_CD, OP_PUCP_CITY_NM, OP_PUCP_TOWN_NM, OP_PUCP_VILLAGE_NM, OP_PUCP_LIN, OP_PUCP_ROAD, OP_PUCP_ADDR_OTH,"
                        + " 'Claim' as TABLEFROM"
                        + " FROM "
                        + " OPDT_I_PU_CLAIM "
                        + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '2' AND OP_CLAIM_TP_NM = '臨櫃認領'";
                
        String NetClmSql = " SELECT OP_BASIC_SEQ_NO, OP_PUCP_NAME, OP_PUCP_IDN, OP_PUCP_PHONENO, OP_PUCP_EMAIL, OP_AC_RCNO, OP_AC_UNIT_NM, OP_YN_LOSER,"
                         + " OP_PUCP_ZIPCODE, OP_PUCP_ADDR_TYPE_CD, OP_PUCP_CITY_NM, OP_PUCP_TOWN_NM, OP_PUCP_VILLAGE_NM, OP_PUCP_LIN, OP_PUCP_ROAD, OP_PUCP_ADDR_OTH,"
                         + " 'NetClm' as TABLEFROM"
                         + " FROM "
                         + " OPDT_E_NET_CLAIM "
                         + " WHERE 1 = 1 AND OP_CLAIM_TP_CD = '1' AND OP_CLAIM_TP_NM = '上網認領'";
        String strQuery = "SELECT * FROM ( "
                        + ClaimSql
                        + " UNION "
                        + NetClmSql
                        + ") U "
                        + " WHERE 1 = 1 AND  U.OP_BASIC_SEQ_NO = ? AND U.OP_YN_LOSER = '1' ";
        qsPara.add( Integer.parseInt(OP_BASIC_SEQ_NO) );       

        try{
            ArrayList<HashMap> claimlist = this.pexecuteQuery( strQuery, qsPara.toArray());

            if( !claimlist.isEmpty() ){
                bool = "Y";
                if(obj.has("printPUCP_NM_YN") && obj.getString("printPUCP_NM_YN").equals("Y")){
                    strClaim += claimlist.get(0).get("OP_PUCP_NAME");
                    //證號
                    if(obj.has("printPUCP_IDN_YN") && obj.getString("printPUCP_IDN_YN").equals("Y")){
                        strClaim =  strClaim +  " " + claimlist.get(0).get("OP_PUCP_IDN");
                    }
                    //地址
                    if(obj.has("printPUCP_ADDR_YN") && obj.getString("printPUCP_ADDR_YN").equals("Y")){
                        if (!StringUtil.nvl(claimlist.get(0).get("OP_PUCP_CITY_NM")).equals("")) {
                            addr.append( claimlist.get(0).get("OP_PUCP_CITY_NM"));
                        }
                        if (!StringUtil.nvl(claimlist.get(0).get("OP_PUCP_TOWN_NM")).equals("")) {
                            addr.append( claimlist.get(0).get("OP_PUCP_TOWN_NM"));
                        }
                        if (!StringUtil.nvl(claimlist.get(0).get("OP_PUCP_VILLAGE_NM")).equals("")) {
                            addr.append( claimlist.get(0).get("OP_PUCP_VILLAGE_NM"));
                        }
                        if (!StringUtil.nvl(claimlist.get(0).get("OP_PUCP_ROAD")).equals("")) {
                            addr.append( claimlist.get(0).get("OP_PUCP_ROAD"));
                        }
                        if (!StringUtil.nvl(claimlist.get(0).get("OP_PUCP_ADDR_OTH")).equals("")) {
                            addr.append( claimlist.get(0).get("OP_PUCP_ADDR_OTH"));
                        }
                        strClaim = strClaim + " " + addr;
                    }
                    //電話
                   if(obj.has("printPUCP_TEL_YN") && obj.getString("printPUCP_TEL_YN").equals("Y")){
                       strClaim =  strClaim +  " " + claimlist.get(0).get("OP_PUCP_PHONENO");
                   }
                }
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        returnValue[0] = strClaim;
        returnValue[1] = bool;
        return returnValue;
    }
    
    
    /**
    * 傳105/09/10－>105年9月10日
    * @param date
    * @return
    */
    public static String getNowTime() {
    		java.util.Date now = new java.util.Date();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		String date = sdf.format(now);
    		String[] dateString = date.split("/");
    		Integer tempYear = Integer.valueOf(dateString[0])-1911;
    		String Year = String.valueOf(tempYear);
    		String month = dateString[1];
    		String day = dateString[2];
    		String fullTime = Year+"年"+month+"月"+day+"日";
    		return fullTime;
    }
    public static String getNowTime2() {
    		java.util.Date now = new java.util.Date();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    		String date = sdf.format(now);
    		String[] dateString = date.split("/");
    		Integer tempYear = Integer.valueOf(dateString[0])-1911;
    		String Year = String.valueOf(tempYear);
    		String month = dateString[1];
    		String day = dateString[2];
    		String fullTime = Year+"/"+month+"/"+day;
    		return fullTime;
    }
    /**
     * 取得7碼的民國年格式(yyy年MM月dd日)
     * 
     * @param date DB的格式為yyyyMMdd，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy年MM月dd日)
     */
    public static String getDBToReportDate(String date) {
        String finalDate="";
        String year = "";
        String month = "";
        String day = "";
        if( !date.equals("") ){
            year = date.substring(0, 4);
            month = date.substring(4,6);
            day = date.substring(6,date.length());
            Integer newYear = Integer.valueOf(year)-1911;
            if ( newYear < 100 )
                finalDate = "0"+String.valueOf(newYear)+"年"+month+"月"+day+"日";
            else
                finalDate = String.valueOf(newYear)+"年"+month+"月"+day+"日";
        }
        return finalDate;
    }
    
    public ArrayList<HashMap> GetDetail( String OP_SEQ_NO ) {
            ArrayList qsPara = new ArrayList();
            ArrayList<HashMap> detaillist = null;
            String puojNm = "";
            String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT, OP_FEATURE, OP_REMARK"
                       + " FROM OPDT_I_PU_DETAIL "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            qsPara.add( Integer.parseInt( OP_SEQ_NO ) );

            try{
                detaillist = this.pexecuteQuery( sql, qsPara.toArray());
            } catch (Exception e){
                e.printStackTrace();
            }

            return detaillist;
    }
    
    public ArrayList<HashMap> GetDetailMobile( String OP_SEQ_NO ) {
            ArrayList qsPara = new ArrayList();
            ArrayList<HashMap> detaillist = null;
            String puojNm = "";
            JSONArray ja = checkDate(OP_SEQ_NO);
            String sql = " SELECT  OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT, OP_FEATURE, OP_REMARK"
                       + " FROM OPDT_I_PU_DETAIL "
                       + " WHERE OP_BASIC_SEQ_NO = ? ";
            if(ja.length() > 0){
                sql += " AND (OP_TYPE_CD = 'C001' OR OP_TYPE_CD = 'C003') ";
            } else {
                sql += " AND (OP_TYPE_CD = 'BD03' OR OP_TYPE_CD = 'BD04' OR OP_TYPE_CD = 'BD05' OR OP_TYPE_CD = 'BD11' OR OP_TYPE_CD = 'BD13') ";
            }
            qsPara.add( Integer.parseInt( OP_SEQ_NO ) );

            try{
                detaillist = this.pexecuteQuery( sql, qsPara.toArray());
            } catch (Exception e){
                e.printStackTrace();
            }

            return detaillist;
    }

    public JSONArray checkDate(String opBasicSeqno) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.append(" SELECT * ")
                .append(" FROM ")
                .append(" OPDT_I_PU_DETAIL ")
                .append(" WHERE 1=1 ")
//                .append(" AND OP_ADD_DT_TM < '20220101000000' ")
                .append(" AND OP_BASIC_SEQ_NO = ? ");
        qsPara.add(Integer.parseInt(opBasicSeqno));
        ArrayList<HashMap> list = null;
        try {
            list = pexecuteQuery(sql.toString(), qsPara.toArray());

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return arrayList2JsonArray(list);
    }
    
    public Map<String, List> printByOP07A02Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT DETAIL.OP_AC_RCNO, DETAIL.OP_PUOJ_NM, DETAIL.OP_QTY, DETAIL.OP_QTY_UNIT ");
            subsql.append(" FROM OPDT_I_PU_DETAIL DETAIL ");
            subsql.append(" INNER JOIN OPDT_I_PU_BASIC BASIC on BASIC.OP_SEQ_NO = DETAIL.OP_BASIC_SEQ_NO ");
            subsql.append(" WHERE 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' AND BASIC.OP_CURSTAT_CD = '5' ");
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
               }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //收據編號
            if (obj.has("OP_AC_RCNO") && !obj.getString("OP_AC_RCNO").equals("")) {
                subsql.append( " AND DETAIL.OP_AC_RCNO = ?  ");
                args.add(obj.getString("OP_AC_RCNO"));
            }
            
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                    data.put("OP_PUOJ_NM", crs.getString("OP_PUOJ_NM"));
                    data.put("OP_QTY_UNIT", crs.getString("OP_QTY_UNIT"));
                    if (!StringUtil.nvl(crs.getString("OP_QTY")).equals("")) {
                        String[] opQty = crs.getString("OP_QTY").toString().split("\\.");
                        if( opQty[1].equals("00") ){
                            data.put("OP_QTY", opQty[0]);
                        }else{
                            data.put("OP_QTY", crs.getString("OP_QTY"));
                        }
                    }
                    String OP_DOC_WD="";
                    
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("OP_QTY_UNIT", "");
                    data.put("OP_QTY", "");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr="";
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得物拍(變)賣清冊");
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得物拍(變)賣清冊");
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得物拍(變)賣清冊");
                } else {
                    tempObj.put("UNIT", "拾得物拍(變)賣清冊");
                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
     public CachedRowSet printByOP07A03Q_DOC(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" select ANNOUNCE.OP_AN_DATE_BEG,  ANNOUNCE.OP_AN_DATE_END, ANNOUNCE.OP_DOC_WD,ANNOUNCE.OP_DOC_NO, ANNOUNCE.OP_AN_B_UNIT_CD ");
            subsql.append(" ,'' as UNIT,'' TODAY,'' NOW_UNIT ");
            subsql.append(" from OPDT_I_ANNOUNCE ANNOUNCE  ");
            subsql.append(" inner join OPDT_I_PU_BASIC BASIC on BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO   ");
            subsql.append(" where 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' ");
            //公告日期
            if (obj.has("OP_AN_DATE_BEG") && !obj.getString("OP_AN_DATE_BEG").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_BEG >= ?  ");
                args.add(obj.getString("OP_AN_DATE_BEG"));
            }
            if (obj.has("OP_AN_DATE_END") && !obj.getString("OP_AN_DATE_END").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END <= ?  ");
                args.add(obj.getString("OP_AN_DATE_END"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //收據編號
            if (obj.has("OP_AC_RCNO") && !obj.getString("OP_AC_RCNO").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AC_RCNO = ?  ");
                args.add(obj.getString("OP_AC_RCNO"));
            }
           
            
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            
                while (crs.next()) {
                    HashMap data = new HashMap();
                    
                    crs.updateString("UNIT",getUnitNm(crs.getString("OP_AN_B_UNIT_CD")));
                    crs.updateString("TODAY","製表日期：" + getNowTime());
                    crs.updateString("NOW_UNIT","製表單位：" + voUser.getUnitName());
                    
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG")))
                        crs.updateString("OP_AN_DATE_BEG", "");
                    else
                        crs.updateString("OP_AN_DATE_BEG", DateUtil.to7TwDateWithCB(crs.getString("OP_AN_DATE_BEG")));
                    if (crs.getString("OP_AN_DATE_END")==null || "".equals(crs.getString("OP_AN_DATE_END")))
                        crs.updateString("OP_AN_DATE_END", "");
                    else
                        crs.updateString("OP_AN_DATE_END", DateUtil.to7TwDateWithCB(crs.getString("OP_AN_DATE_END")));
                    String OP_DOC_WD="";
                    if (crs.getString("OP_DOC_WD") ==null || crs.getString("OP_DOC_NO")==null )
                        OP_DOC_WD="";
                    else 
                        OP_DOC_WD = crs.getString("OP_DOC_WD")+crs.getString("OP_DOC_NO");
                    crs.updateString("OP_DOC_WD",  OP_DOC_WD);
                   
 
                }
             
               
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return crs;
    }
   
    public Map<String, List> printByOP07A04Q(JSONObject obj) {
        //20230529_「拾得物公告招領清冊」，增修4個欄位
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_SEQ_NO,BASIC.OP_AC_RCNO, ");
            subsql.append(" BASIC.OP_PU_CITY_NM, BASIC.OP_PU_TOWN_NM, BASIC.OP_PU_PLACE,");
            subsql.append(" BASIC.OP_PU_DATE,BASIC.OP_AC_DATE,BASIC.OP_AC_STAFF_NM, ");
            subsql.append(" ANNOUNCE.OP_AN_DATE_BEG, ANNOUNCE.OP_DOC_WD, ANNOUNCE.OP_DOC_NO, ANNOUNCE.OP_AN_DATE_END, ");
            subsql.append(" BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_STAFF_NM, ");
            subsql.append(" BASIC.OP_IS_PUT_NM,BASIC.OP_PUPO_NAME , BASIC.OP_PUPO_PHONENO ,BASIC.OP_PUPO_TP_NM, ");
            subsql.append(" BASIC.OP_OC_ADDR_TYPE_CD,BASIC.OP_PUPO_CITY_NM,BASIC.OP_PUPO_TOWN_NM,BASIC.OP_PUPO_ROAD,BASIC.OP_PUPO_ADDR_OTH ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" LEFT JOIN OPDT_I_ANNOUNCE ANNOUNCE ON BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO ");
            subsql.append(" where 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' AND BASIC.OP_CURSTAT_CD = '2' ");
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //有無證件
            if(obj.has("OP_CERT_YN") && !obj.getString("OP_CERT_YN").equals("")){
                subsql.append(" AND BASIC.OP_INCLUDE_CERT = ? ");
                args.add(obj.getString("OP_CERT_YN"));
            }
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                    String puojNM = "";
                    puojNM = getOpPuojNmAndQty(crs.getString("OP_SEQ_NO"));
                    
                    data.put("OP_PUOJ_NM", puojNM);
                    data.put("ADDR",crs.getString("OP_PU_CITY_NM")+crs.getString("OP_PU_TOWN_NM")+crs.getString("OP_PU_PLACE"));
                   // data.put("OP_PU_DATE", crs.getString("OP_PU_DATE"));
                    if (crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE"))){
                        data.put("OP_PU_DATE","");
                    } else{
                        data.put("OP_PU_DATE",DateUtil.get14DateFormat(crs.getString("OP_PU_DATE")+"000000" ).substring(0,9));
                    }
                    String acDate = "";
                    if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE"))){
                        acDate = "";
                    } else{
                        acDate = DateUtil.get14DateFormat(crs.getString("OP_AC_DATE")+"000000" ).substring(0,9);
                    }
                    if(obj.has("OP_AC_STAFF_NM") && obj.getString("OP_AC_STAFF_NM").equals("Y")){
                        data.put("OP_AC_DATE",acDate + crs.getString("OP_AC_STAFF_NM"));
                    }else{
                        data.put("OP_AC_DATE", acDate);
                    }
                    String OP_DOC_WD="";
                    if (crs.getString("OP_DOC_WD") ==null || crs.getString("OP_DOC_NO")==null ){
                        OP_DOC_WD="";
                    } else {
                        OP_DOC_WD = "         " + crs.getString("OP_DOC_WD")+crs.getString("OP_DOC_NO");
                    }    
                    //data.put("OP_DOC_WD", OP_DOC_WD);
                    if (crs.getString("OP_AN_DATE_END")==null || "".equals(crs.getString("OP_AN_DATE_END"))){
                        data.put("OP_AN_DATE_END","");
                    } else{
                        data.put("OP_AN_DATE_END",DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_END")+"000000").substring(0,9));
                    }
                    
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG"))){
                        data.put("OP_AN_DATE_BEG", "" + OP_DOC_WD );
                    } else{
                        data.put("OP_AN_DATE_BEG", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_BEG")+"000000").substring(0,9) + OP_DOC_WD);
                    }
                    
                    //20230529_「拾得物公告招領清冊」，增修4個欄位
                    data.put("OP_AC_UNIT_NM", crs.getString("OP_AC_UNIT_NM"));
                    data.put("OP_AC_STAFF_NM", crs.getString("OP_AC_STAFF_NM"));
                    
                    if ( crs.getString("OP_OC_ADDR_TYPE_CD").equals("1") ){ //一般輸入
                      data.put("OP_PUPO_NAME_PHONENO_ADDR", crs.getString("OP_PUPO_NAME")+"."+crs.getString("OP_PUPO_PHONENO")+"."+crs.getString("OP_PUPO_CITY_NM")+crs.getString("OP_PUPO_TOWN_NM")+crs.getString("OP_PUPO_ROAD"));                        
                    }else{ //通常是"9"                                       //自由輸入
                           //if ( crs.getString("OP_OC_ADDR_TYPE_CD").equals("9") ){
                      data.put("OP_PUPO_NAME_PHONENO_ADDR", crs.getString("OP_PUPO_NAME")+"."+crs.getString("OP_PUPO_ADDR_OTH"));                        
                    }

                    data.put("OP_PUPO_TP_NM", crs.getString("OP_PUPO_TP_NM"));
                    
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("ADDR","");
                    data.put("OP_PU_DATE", "");
                    data.put("OP_AC_DATE", "");
                    data.put("OP_DOC_WD", "");
                    data.put("OP_AN_DATE_END", "");
                    data.put("OP_AN_DATE_BEG", "");
                    
                    data.put("OP_AC_UNIT_NM", "");
                    data.put("OP_AC_STAFF_NM", "");
                    data.put("OP_PUPO_NAME", "");
                    data.put("OP_PUPO_NAME_PHONENO_ADDR",  "");
                    data.put("OP_PUPO_TP_NM", "");
                    
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr="";
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得物公告招領清冊");
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得物公告招領清冊");
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得物公告招領清冊");
                } else {
                    tempObj.put("UNIT", "拾得物公告招領清冊");
                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public Map<String, List> printByOP07A0501Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        //xYxMxD 公告期滿日期_E
        int year = 0;
        int month = 0;
        int day = 0;
        String[] arrAnDateEnd;
        Calendar c = Calendar.getInstance();
        if(!obj.has("OP_AN_DATE")){
            arrAnDateEnd = getNowTime2().split("/");
        }else{
            arrAnDateEnd = obj.getString("OP_AN_DATE_O").split("/");
        }
        String strTitle="\n於民國"+arrAnDateEnd[0]+"年"+arrAnDateEnd[1]+"月"+arrAnDateEnd[2]+"日";
        String pritnType = obj.getString("PRINT_TYPE");
        if(pritnType.equals("1")){
            strTitle += "已屆公告期滿日(含)但仍在公告中案件清冊";
           } else if(pritnType.equals("2")){
            strTitle += "已公告期滿逾3個月案件清冊";
           } else if(pritnType.equals("3")){
            strTitle += "已公告期滿但未逾(含)三個月案件清冊";
           }
        try {
            subsql.append(" SELECT BASIC.OP_AC_UNIT_CD, BASIC.OP_CURSTAT_NM, BASIC.OP_AC_RCNO, BASIC.OP_MANUAL_RCNO, BASIC.OP_AC_DATE, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_STAFF_NM, ");
            subsql.append(" ANNOUNCE.OP_AN_UNIT_NM, ANNOUNCE.OP_AN_DATE_BEG, ANNOUNCE.OP_AN_DATE_END, ANNOUNCE.OP_DOC_WD, BASIC.OP_PUPO_NAME, BASIC.OP_PUPO_IDN ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC INNER JOIN OPDT_I_PU_DETAIL DETAIL ON DETAIL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO ");
            subsql.append(" LEFT JOIN OPDT_I_ANNOUNCE ANNOUNCE ON ANNOUNCE.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO ");

            subsql.append(" WHERE 1=1 ");
            subsql.append(" AND ISNULL(BASIC.OP_DEL_FLAG,'0') <> '1' AND BASIC.OP_CURSTAT_CD <> '6' AND ISNULL(ANNOUNCE.OP_YN_CLAIM,'0') <> '1'  ");
            subsql.append(" AND ANNOUNCE.OP_AN_DATE_END <> '' AND ANNOUNCE.OP_AN_DATE_END <> NULL ");
            if( pritnType.equals("1") ){
                //已屆公告期滿日(含)但仍在公告中案件清冊 printCode = 1
                //OP_AN_DATE_END<=輸入之公告期滿日
                if( obj.has("OP_AN_DATE") && !obj.getString("OP_AN_DATE").equals("") ){
                    subsql.append( " AND ANNOUNCE.OP_AN_DATE_END <= ?  ");
                    args.add(obj.getString("OP_AN_DATE"));
                }
            } else if( pritnType.equals("2") ){
                //已公告期滿逾3個月案件清冊 printCode = 2
                // OP_AN_DATE_END<(輸入之公告期滿日 – 3個月) 
               if( obj.has("OP_AN_DATE") && !obj.getString("OP_AN_DATE").equals("") ){
                    year = Integer.parseInt(obj.getString("OP_AN_DATE").substring(0, 4));
                    month = Integer.parseInt(obj.getString("OP_AN_DATE").substring(4, 6))-1;
                    day = Integer.parseInt(obj.getString("OP_AN_DATE").substring(6, 8));
                    c.set(year, month, day);
                    c.add(Calendar.MONTH, -3);
                    String opAnDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
                    subsql.append( " AND ANNOUNCE.OP_AN_DATE_END < ?  ");
                    args.add( opAnDate );
                }
            } else if( pritnType.equals("3") ){
                //已公告期滿但未逾(含)三個月案件清冊 printCode = 3
                //OP_AN_DATE_END>=(輸入之公告期滿日-3個月) and OP_AN_DATE_END< 輸入之公告期滿日)
                if( obj.has("OP_AN_DATE") && !obj.getString("OP_AN_DATE").equals("") ){
                    year = Integer.parseInt(obj.getString("OP_AN_DATE").substring(0, 4));
                    month = Integer.parseInt(obj.getString("OP_AN_DATE").substring(4, 6))-1;
                    day = Integer.parseInt(obj.getString("OP_AN_DATE").substring(6, 8));
                    c.set(year, month, day);
                    c.add(Calendar.MONTH, -3);
                    String opAnDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
                    subsql.append( " AND ANNOUNCE.OP_AN_DATE_END >= ? AND ANNOUNCE.OP_AN_DATE_END <= ? ");
                    args.add( opAnDate );
                    args.add( obj.getString("OP_AN_DATE") );
                }
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //有無證件
            if(obj.has("OP_CERT") && !obj.getString("OP_CERT").equals("")){
                subsql.append(" AND BASIC.OP_INCLUDE_CERT = ? ");
                args.add(obj.getString("OP_CERT"));
            }
            //排序
            subsql.append(" ORDER BY  BASIC.OP_AC_UNIT_CD,ANNOUNCE.OP_AN_UNIT_CD,BASIC.OP_AC_RCNO ");
            
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    data.put("OP_CURSTAT_NM", crs.getString("OP_CURSTAT_NM"));
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));	
                    data.put("OP_MANUAL_RCNO", crs.getString("OP_MANUAL_RCNO"));
                    if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE"))){
                        data.put("OP_AC_DATE", "");
                    } else{
                        data.put("OP_AC_DATE", DateUtil.get14DateFormat(crs.getString("OP_AC_DATE")+"000000").substring(0,9));
                    }
                    data.put("OP_AC_UNIT_NM", crs.getString("OP_AC_UNIT_NM"));	
                    data.put("OP_AC_STAFF_NM", crs.getString("OP_AC_STAFF_NM"));	
                    data.put("OP_AN_UNIT_NM", crs.getString("OP_AN_UNIT_NM"));	
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG"))){
                        data.put("OP_AN_DATE_BEG", "");
                    } else{
                        data.put("OP_AN_DATE_BEG", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_BEG")+"000000").substring(0,9));
                    }
                    if (crs.getString("OP_AN_DATE_END")==null || "".equals(crs.getString("OP_AN_DATE_END"))){
                        data.put("OP_AN_DATE_END", "");
                    } else{
                        data.put("OP_AN_DATE_END", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_END")+"000000").substring(0,9));
                    }	
                    data.put("OP_DOC_WD", crs.getString("OP_DOC_WD"));
                    data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));	
                    data.put("OP_PUPO_IDN", crs.getString("OP_PUPO_IDN"));
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_CURSTAT_NM","");
                    data.put("OP_AC_RCNO","");
                    data.put("OP_MANUAL_RCNO","");
                    data.put("OP_AC_DATE","");	
                    data.put("OP_AC_UNIT_NM","");
                    data.put("OP_AC_STAFF_NM","");
                    data.put("OP_AN_UNIT_NM","");
                    data.put("OP_AN_DATE_BEG","");
                    data.put("OP_AN_DATE_END","");	
                    data.put("OP_DOC_WD","");
                    data.put("OP_PUPO_NAME","");
                    data.put("OP_PUPO_IDN","");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + strTitle);
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + strTitle);
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + strTitle);
                } else {
                    tempObj.put("UNIT", strTitle);
                }
//                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(crs.getString("OP_AN_UNIT_CD"))+strTitle);
//                }else{
//                    tempObj.put("UNIT", strTitle);
//                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public Map<String, List> printByOP07A0502Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_SEQ_NO, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_RCNO, BASIC.OP_PUPO_NAME, BASIC.OP_AC_DATE, ANNOUNCE.OP_AN_DATE_BEG, ANNOUNCE.OP_AN_DATE_END ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" LEFT JOIN OPDT_I_ANNOUNCE ANNOUNCE ON BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO ");
            subsql.append(" WHERE 1 = 1 ");
            subsql.append(" AND ISNULL(BASIC.OP_DEL_FLAG,'0') <> '1' AND BASIC.OP_CURSTAT_CD <> '6' AND ISNULL(ANNOUNCE.OP_YN_CLAIM,'0') <> '1'  ");

            //公告期滿日期
            if (obj.has("OP_AN_DATE_END_S") && !obj.getString("OP_AN_DATE_END_S").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END >= ?  ");
                args.add(obj.getString("OP_AN_DATE_END_S"));
            }
            if (obj.has("OP_AN_DATE_END_E") && !obj.getString("OP_AN_DATE_END_E").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END <= ?  ");
                args.add(obj.getString("OP_AN_DATE_END_E"));
                
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                    data.put("OP_PUOJ_NM", getOP_PUOJ_NM(crs.getString("OP_SEQ_NO")));
                    data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));
                    if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE"))){
                        data.put("OP_AC_DATE", "");
                    } else{
                        data.put("OP_AC_DATE", DateUtil.get14DateFormat(crs.getString("OP_AC_DATE")+"000000").substring(0,9));
                    }	
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG"))){
                        data.put("OP_AN_DATE_BEG", "");
                    } else{
                        data.put("OP_AN_DATE_BEG", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_BEG")+"000000").substring(0,9));
                    }	
                    if (crs.getString("OP_AN_DATE_END")==null || "".equals(crs.getString("OP_AN_DATE_END"))){
                        data.put("OP_AN_DATE_END", "");
                    } else{
                        data.put("OP_AN_DATE_END", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_END")+"000000").substring(0,9));
                    }	
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("OP_PUPO_NAME", "");
                    data.put("OP_AC_DATE", "");
                    data.put("OP_AN_DATE_BEG", "");
                    data.put("OP_AN_DATE_END", "");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr=" 107年 月期滿遺失物公告清冊 ";
//                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(crs.getString("OP_AN_UNIT_CD"))+tmpstr);
//                }else{
//                    tempObj.put("UNIT", tmpstr);
//                }
                 if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + tmpstr);
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + tmpstr);
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + tmpstr);
                } else {
                    tempObj.put("UNIT", tmpstr);
                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public CachedRowSet printByOP07A06Q_DOC(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" select ANNOUNCE.OP_BASIC_SEQ_NO,ANNOUNCE.OP_AN_DATE_BEG,  ANNOUNCE.OP_DOC_WD,ANNOUNCE.OP_DOC_NO, ANNOUNCE.OP_AN_UNIT_CD, ANNOUNCE.OP_AN_B_UNIT_CD, BASIC.OP_PUPO_NAME, BASIC.OP_PU_DATE ");
            subsql.append(" ,'' OP_PUOJ_NM,'' as UNIT,'' TODAY,'' NOW_UNIT, BASIC.OP_PUPO_GENDER   ");
            subsql.append(" from OPDT_I_ANNOUNCE ANNOUNCE  ");
            subsql.append(" inner join OPDT_I_PU_BASIC BASIC on BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO   ");
            subsql.append(" inner join OPDT_I_AN_DL DL on BASIC.OP_SEQ_NO = DL.OP_BASIC_SEQ_NO   ");
            subsql.append(" where 1=1 ");
            subsql.append(" AND BASIC.OP_CURSTAT_CD = '4' AND isNull(BASIC.OP_DEL_FLAG, '0') <> '1' ");
            //領回日期
            if (obj.has("OP_NTC_PUPO_DT_S") && !obj.getString("OP_NTC_PUPO_DT_S").equals("")) {
                subsql.append( " AND DL.OP_NTC_PUPO_DT >= ?  ");
                args.add(obj.getString("OP_NTC_PUPO_DT_S"));
            }
            if (obj.has("OP_NTC_PUPO_DT_E") && !obj.getString("OP_NTC_PUPO_DT_E").equals("")) {
                subsql.append( " AND DL.OP_NTC_PUPO_DT <= ?  ");
                args.add(obj.getString("OP_NTC_PUPO_DT_E"));
            }
            //受理日期
            if (obj.has("OP_CASE_DATE_S1") && !obj.getString("OP_CASE_DATE_S1").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_CASE_DATE_S1"));
            }
            if (obj.has("OP_CASE_DATE_E1") && !obj.getString("OP_CASE_DATE_E1").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_CASE_DATE_E1"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //收據編號
            if (obj.has("OP_AC_RCNO") && !obj.getString("OP_AC_RCNO").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AC_RCNO = ?  ");
                args.add(obj.getString("OP_AC_RCNO"));
            }
            //排序
            subsql.append( " ORDER BY BASIC.OP_AC_UNIT_CD,BASIC.OP_AC_RCNO  ");
           
           
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            
                while (crs.next()) {
                    HashMap data = new HashMap();
                    
                    crs.updateString("UNIT",getUnitNm(crs.getString("OP_AN_B_UNIT_CD")));
                    crs.updateString("TODAY","製表日期：" + getNowTime());
                    crs.updateString("NOW_UNIT","製表單位：" + voUser.getUnitName());
                    crs.updateString("OP_PUOJ_NM", getOP_PUOJ_NM(crs.getString("OP_BASIC_SEQ_NO")));
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG")))
                        crs.updateString("OP_AN_DATE_BEG", "");
                    else
                        crs.updateString("OP_AN_DATE_BEG", DateUtil.to7TwDateWithCB(crs.getString("OP_AN_DATE_BEG")));
                    if (crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE")))
                        crs.updateString("OP_PU_DATE", "");
                    else
                        crs.updateString("OP_PU_DATE", DateUtil.to7TwDateWithCB(crs.getString("OP_PU_DATE")));
                    String OP_DOC_WD="";
                    if (crs.getString("OP_DOC_WD") ==null || crs.getString("OP_DOC_NO")==null )
                        OP_DOC_WD="";
                    else 
                        OP_DOC_WD = crs.getString("OP_DOC_WD")+crs.getString("OP_DOC_NO");
                    crs.updateString("OP_DOC_WD",  OP_DOC_WD);
                    
                    if (crs.getString("OP_PUPO_GENDER")==null || "".equals(crs.getString("OP_PUPO_GENDER"))){
                        crs.updateString("OP_PUPO_GENDER", "");
                    }else if( crs.getString("OP_PUPO_GENDER").equals("1") ){
                        crs.updateString("OP_PUPO_GENDER", "先生");
                    }else if( crs.getString("OP_PUPO_GENDER").equals("2") ){
                        crs.updateString("OP_PUPO_GENDER", "小姐");
                    }else if( crs.getString("OP_PUPO_GENDER").equals("3") ){
                        crs.updateString("OP_PUPO_GENDER", "");
                    }else if( crs.getString("OP_PUPO_GENDER").equals("4") ){
                        crs.updateString("OP_PUPO_GENDER", "");
                    }
                   
 
                }
             
               
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return crs;
    }
    public Map<String, List> printByOP07A11Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        //自107年01月01日 至 107年01月01日止公告期滿(共15件)
         String now_unit = "";
        try {
            subsql.append(" SELECT BASIC.OP_SEQ_NO, BASIC.OP_AC_RCNO, BASIC.OP_AC_DATE, ANNOUNCE.OP_CABINET_NO, ANNOUNCE.OP_AN_DATE_BEG, ANNOUNCE.OP_AN_DATE_END ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" LEFT JOIN OPDT_I_ANNOUNCE ANNOUNCE ON BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO LEFT JOIN OPDT_I_FNSH FNSH ON BASIC.OP_SEQ_NO = FNSH.OP_BASIC_SEQ_NO ");
            subsql.append(" WHERE 1 = 1 ");
          
            //公告期滿日期
            if (obj.has("OP_AN_DATE_END_S") && !obj.getString("OP_AN_DATE_END_S").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END >= ?  ");
                args.add(obj.getString("OP_AN_DATE_END_S"));
                now_unit = "自";
                now_unit = now_unit + DateUtil.get14DateFormat(obj.getString("OP_AN_DATE_END_S")+"000000" ).substring(0,9)+"日公告期滿";
            }
            if (obj.has("OP_AN_DATE_END_E") && !obj.getString("OP_AN_DATE_END_E").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END <= ?  ");
                args.add(obj.getString("OP_AN_DATE_END_E"));
                now_unit = now_unit + " 至 " + DateUtil.get14DateFormat(obj.getString("OP_AN_DATE_END_E")+"000000" ).substring(0,9);
                 
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            //結案日期
            if (obj.has("OP_FS_DATE_S") && !obj.getString("OP_FS_DATE_S").equals("")) {
                subsql.append( " AND FNSH.OP_FS_DATE >= ?  ");
                args.add(obj.getString("OP_FS_DATE_S"));
            }
            if (obj.has("OP_FS_DATE_E") && !obj.getString("OP_FS_DATE_E").equals("")) {
                subsql.append( " AND FNSH.OP_FS_DATE <= ?  ");
                args.add(obj.getString("OP_FS_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            
            
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
                while (crs.next()) {
                    String strArray[] = {"",""};
                    String showYN = "";
                    HashMap data = new HashMap();
                    String strOP_PUCP_NAME = "";
                    strArray = getOP_PUCP_NAME( crs.getString("OP_SEQ_NO"), obj);
                    if( strArray[1].equals("Y") ){  //有遺失人才顯示
                        flagData = true;
                        strOP_PUCP_NAME = strArray[0];
                        data.put("OP_PUCP_NAME", strOP_PUCP_NAME);
                        data.put("OP_CABINET_NO", crs.getString("OP_CABINET_NO"));
                        data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                        data.put("OP_PUOJ_NM", getOP_PUOJ_NM( crs.getString("OP_SEQ_NO") ));
                        if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE"))){
                            data.put("OP_AC_DATE", "");
                        } else{
                            data.put("OP_AC_DATE", DateUtil.get14DateFormat(crs.getString("OP_AC_DATE")+"000000").substring(0,9));
                        }
                        if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG"))){
                            data.put("OP_AN_DATE_BEG", "");
                        } else{
                            data.put("OP_AN_DATE_BEG", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_BEG")+"000000").substring(0,9));
                        }
                        if (crs.getString("OP_AN_DATE_END")==null || "".equals(crs.getString("OP_AN_DATE_END"))){
                            data.put("OP_AN_DATE_END", "");
                        } else{
                            data.put("OP_AN_DATE_END", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_END")+"000000").substring(0,9));
                        }

                        datas.add(data);
                    }
                }
                if (flagData) {
                    list.put("header", datas);
                    //now_unit = now_unit +"(共"+crs.size() +"件)";
                    now_unit = now_unit +"(共"+datas.size() +"件)";
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_CABINET_NO", "");
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("OP_PUCP_NAME","");
                    data.put("OP_AC_DATE", "");
                    data.put("OP_AN_DATE_BEG", "");
                    data.put("OP_AN_DATE_END", "");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr="";
//                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得物拍(變)賣清冊");
//                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得物拍(變)賣清冊");
//                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得物拍(變)賣清冊");
//                } else {
//                    tempObj.put("UNIT", "拾得物拍(變)賣清冊");
//                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                
                tempObj.put("NOW_UNIT", now_unit);
                
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public Map<String, List> printByOP07A12Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        String now_unit = "自";
        try {
            subsql.append(" SELECT BASIC.OP_SEQ_NO, BASIC.OP_AC_RCNO, BASIC.OP_PUPO_NAME, BASIC.OP_PUPO_PHONENO, BASIC.OP_AC_DATE,  ");
            subsql.append(" ANNOUNCE.OP_AN_DATE_BEG, ANNOUNCE.OP_AN_DATE_END, ISNULL(ANNOUNCE.OP_CABINET_NO,'') AS OP_CABINET_NO, ");
            subsql.append(" BASIC.OP_PUPO_CITY_NM, BASIC.OP_PUPO_TOWN_NM, BASIC.OP_PUPO_VILLAGE_NM, BASIC.OP_PUPO_LIN, BASIC.OP_PUPO_ROAD, BASIC.OP_PUPO_ADDR_OTH ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" LEFT JOIN OPDT_I_ANNOUNCE ANNOUNCE ON BASIC.OP_SEQ_NO = ANNOUNCE.OP_BASIC_SEQ_NO ");

            subsql.append(" WHERE 1 = 1 ");
            //String as[] = obj.getString("OP_AN_DATE_END_S_O").split("/");
            //String ae[] = obj.getString("OP_AN_DATE_END_E_O").split("/");
            //String as[] = {"109","01","01"};
            //String ae[] = {"109","01","31"};
            //公告期滿日期
            if (obj.has("OP_AN_DATE_END_S") && !obj.getString("OP_AN_DATE_END_S").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END >= ?  ");
                String as[] = obj.getString("OP_AN_DATE_END_S_O").split("/");
                args.add(obj.getString("OP_AN_DATE_END_S"));
                now_unit = now_unit +as[0]+"年"+as[1]+"月"+as[2]+"日";
            }
            if (obj.has("OP_AN_DATE_END_E") && !obj.getString("OP_AN_DATE_END_E").equals("")) {
                subsql.append( " AND ANNOUNCE.OP_AN_DATE_END <= ?  ");
                String ae[] = obj.getString("OP_AN_DATE_END_E_O").split("/");
                args.add(obj.getString("OP_AN_DATE_END_E"));
                now_unit = now_unit +" 至 "+ ae[0]+"年"+ae[1]+"月"+ae[2] +"日止公告期滿";
                 
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    
                    data.put("OP_CABINET_NO", crs.getString("OP_CABINET_NO"));
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                    data.put("OP_PUOJ_NM", getOP_PUOJ_NM(crs.getString("OP_SEQ_NO")) );
                   
                    if (obj.has("printPUPO_NM_YN") && obj.getString("printPUPO_NM_YN").equals("Y")){
                        data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));
                        if(obj.has("printPUPO_ADDR_YN") && obj.getString("printPUPO_ADDR_YN").equals("Y")){
                            if (!StringUtil.nvl(crs.getString("OP_PUPO_CITY_NM")).equals("")) {
                                addr.append( crs.getString("OP_PUPO_CITY_NM"));
                            }
                            if (!StringUtil.nvl(crs.getString("OP_PUPO_TOWN_NM")).equals("")) {
                                addr.append( crs.getString("OP_PUPO_TOWN_NM"));
                            }
                            if (!StringUtil.nvl(crs.getString("OP_PUPO_VILLAGE_NM")).equals("")) {
                                addr.append( crs.getString("OP_PUPO_VILLAGE_NM"));
                            }
                            if (!StringUtil.nvl(crs.getString("OP_PUPO_LIN")).equals("")) {
                                addr.append( crs.getString("OP_PUPO_LIN"));
                                addr.append("鄰");
                            }
                            if (!StringUtil.nvl(crs.getString("OP_PUPO_ROAD")).equals("")) {
                                addr.append( crs.getString("OP_PUPO_ROAD"));
                            }
                            if (!StringUtil.nvl(crs.getString("OP_PUPO_ADDR_OTH")).equals("")) {
                                addr.append( crs.getString("OP_PUPO_ADDR_OTH"));
                            }
                            data.put("ADDR", addr);
                        } else{
                           data.put("ADDR", ""); 
                        }
                        if(obj.has("printPUPO_TEL_YN") && obj.getString("printPUPO_TEL_YN").equals("Y")){
                            data.put("OP_PUPO_PHONENO", crs.getString("OP_PUPO_PHONENO"));
                        } else{
                            data.put("OP_PUPO_PHONENO", "");
                        }
                    } else{
                        data.put("OP_PUPO_NAME","");
                        data.put("OP_PUCP_NAME", "");
                        data.put("OP_PUPO_PHONENO", "");
                        data.put("ADDR", "");
                    } 
                    if (crs.getString("OP_AC_DATE")==null || "".equals(crs.getString("OP_AC_DATE"))){
                        data.put("OP_AC_DATE", "");
                    } else{
                        data.put("OP_AC_DATE", DateUtil.get14DateFormat(crs.getString("OP_AC_DATE")+"000000").substring(0,9));
                    }
                       
                    if (crs.getString("OP_AN_DATE_BEG")==null || "".equals(crs.getString("OP_AN_DATE_BEG"))){
                        data.put("OP_AN_DATE_BEG", "");
                    } else{
                        data.put("OP_AN_DATE_BEG", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_BEG")+"000000").substring(0,9));
                    }
                       
                    if (crs.getString("OP_AN_DATE_END")==null || "".equals(crs.getString("OP_AN_DATE_END"))){
                        data.put("OP_AN_DATE_END", "");
                    } else{
                        data.put("OP_AN_DATE_END", DateUtil.get14DateFormat(crs.getString("OP_AN_DATE_END")+"000000").substring(0,9));
                    }
                           
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    now_unit = now_unit +"(共"+crs.size() +"件)";
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_CABINET_NO", "");
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("OP_PUCP_NAME","");
                    data.put("OP_PUPO_NAME","");
                    data.put("ADDR","");
                    data.put("OP_PUPO_PHONENO","");
                    data.put("OP_AC_DATE", "");
                    data.put("OP_AN_DATE_BEG", "");
                    data.put("OP_AN_DATE_END", "");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr="";
//                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得物拍(變)賣清冊");
//                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得物拍(變)賣清冊");
//                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
//                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得物拍(變)賣清冊");
//                } else {
//                    tempObj.put("UNIT", "拾得物拍(變)賣清冊");
//                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", now_unit);
                obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    
     public Map<String, List> printByOP07A13Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
       
        try {
            subsql.append(" SELECT BASIC.OP_AC_RCNO, BASIC.OP_SEQ_NO, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_DATE, BASIC.OP_AC_STAFF_NM, BASIC.OP_PUPO_NAME, BASIC.OP_PUPO_IDN, BASIC.OP_IS_PUT_NM,");
            subsql.append(" FNSH.OP_RT_UNIT_CD, FNSH.OP_RT_UNIT_NM, FNSH.OP_RT_DATE, FNSH.OP_RT_STAFF_NM");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC");
            subsql.append(" LEFT JOIN OPDT_I_FNSH FNSH ON BASIC.OP_SEQ_NO = FNSH.OP_BASIC_SEQ_NO");
            subsql.append(" WHERE 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' AND BASIC.OP_CURSTAT_CD = '6' AND OP_YN_FS ='1' AND OP_FS_REC_CD = '01' ");
            
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            //發還日期
            if (obj.has("OP_RT_DATE_S") && !obj.getString("OP_RT_DATE_S").equals("")) {
                subsql.append( " AND FNSH.OP_RT_DATE >= ?  ");
                args.add(obj.getString("OP_RT_DATE_S"));
            }
            if (obj.has("OP_RT_DATE_E") && !obj.getString("OP_RT_DATE_E").equals("")) {
                subsql.append( " AND FNSH.OP_RT_DATE <= ?  ");
                args.add(obj.getString("OP_RT_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
               }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //拾得物總價值
            if (obj.has("OP_PU_YN_OV500") && !obj.getString("OP_PU_YN_OV500").equals("")) {
                subsql.append( " AND BASIC.OP_PU_YN_OV500 = ?  ");
                args.add(obj.getString("OP_PU_YN_OV500"));
            }
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
              int i = 1;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    StringBuilder addr = new StringBuilder();
                    String strArray[] = {"",""};
                    data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                    data.put("OP_PUOJ_NM", getOpPuojNmAndQty(crs.getString("OP_SEQ_NO")));
                    String showYN = "";
                    String strOP_PUCP_NAME = "";
                    obj.put("printPUCP_NM_YN", "Y");
                    obj.put("printPUCP_IDN_YN", "Y");
                    obj.put("printPUCP_TEL_YN", "Y");
                    strArray = getOP_PUCP_NAME( crs.getString("OP_SEQ_NO"), obj);
                    if( strArray[1].equals("Y") ){  //有遺失人才顯示
                        strOP_PUCP_NAME = strArray[0];
                        data.put("OP_PUCP_NAME", strOP_PUCP_NAME);
                    }else{
                        data.put("OP_PUCP_NAME", "");
                    }
                    String acUN = "";
                    acUN = getUnitSNm(crs.getString("OP_AC_UNIT_CD"));
                    data.put("OP_AC_UNIT_NM",acUN + DateUtil.to7TwDateTime2( crs.getString("OP_AC_DATE").toString()) + crs.getString("OP_AC_STAFF_NM"));
                   //判斷拾得人姓名或不具名
                    if(crs.getString("OP_IS_PUT_NM").equals("0")){
                        data.put("OP_PUPO_NAME","不具名");
                      }else{ 
                        String pupoNM = "";
                        pupoNM = crs.getString("OP_PUPO_NAME");
                        data.put("OP_PUPO_NAME", pupoNM + crs.getString("OP_PUPO_IDN"));
                    }
                    String rtUN = "";
                    rtUN = getUnitSNm(crs.getString("OP_RT_UNIT_CD"));
                    data.put("OP_RT_UNIT_NM",rtUN+DateUtil.to7TwDateTime2(crs.getString("OP_RT_DATE").toString())+crs.getString("OP_RT_STAFF_NM"));
                    data.put("SEQ", i);
                    i++;
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_AC_RCNO", "");
                    data.put("OP_PUOJ_NM", "");
                    data.put("OP_PUCP_NAME", "");
                    data.put("OP_AC_UNIT_NM", "");
                    data.put("OP_PUPO_NAME", "");
                    data.put("OP_RT_UNIT_NM", "");
                    data.put("SEQ", "");
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                String tmpstr="";
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "受理拾得物發還清冊");
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "受理拾得物發還清冊");
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "受理拾得物發還清冊");
                } else {
                    tempObj.put("UNIT", "受理拾得物發還清冊");
                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
               
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }   
       
        return list;
    }
     
     public Map<String, List> printByOP07A14Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
       
        try {
            subsql.append(" SELECT BASIC.OP_AC_RCNO, BASIC.OP_SEQ_NO, BASIC.OP_AC_UNIT_NM, BASIC.OP_PU_DATE, BASIC.OP_AC_UNIT_CD, BASIC.OP_PUPO_NAME,");
            subsql.append(" FNSH.OP_RT_DATE");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC");
            subsql.append(" LEFT JOIN OPDT_I_FNSH FNSH ON BASIC.OP_SEQ_NO = FNSH.OP_BASIC_SEQ_NO");
            subsql.append(" WHERE 1=1 AND isNull(BASIC.OP_DEL_FLAG,'0')<>'1' AND BASIC.OP_CURSTAT_CD = '6' AND OP_YN_FS ='1' AND OP_FS_REC_CD = '02' ");
            //發還日期
            if (obj.has("OP_RT_DATE_S") && !obj.getString("OP_RT_DATE_S").equals("")) {
                subsql.append( " AND FNSH.OP_RT_DATE >= ?  ");
                args.add(obj.getString("OP_RT_DATE_S"));
            }
            if (obj.has("OP_RT_DATE_E") && !obj.getString("OP_RT_DATE_E").equals("")) {
                subsql.append( " AND FNSH.OP_RT_DATE <= ?  ");
                args.add(obj.getString("OP_RT_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
               }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
           //拾得物總價值
            if (obj.has("OP_PU_YN_OV500") && !obj.getString("OP_PU_YN_OV500").equals("")) {
                subsql.append( " AND BASIC.OP_PU_YN_OV500 = ?  ");
                args.add(obj.getString("OP_PU_YN_OV500"));
            }
              crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
              Boolean flagData = false;
              int i = 1;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    //收據編號
                    if(crs.getString("OP_AC_RCNO")==null || "".equals(crs.getString("OP_AC_RCNO"))){
                        data.put("OP_AC_RCNO","");
                    }else{
                        data.put("OP_AC_RCNO",crs.getString("OP_AC_RCNO"));
                    }
                    //受理單位
                    if(crs.getString("OP_AC_UNIT_CD")==null || "".equals(crs.getString("OP_AC_UNIT_CD"))){
                        data.put("OP_AC_UNIT_NM","");
                    }else {
                        data.put("OP_AC_UNIT_NM",getUnitSNm(crs.getString("OP_AC_UNIT_CD")));
                    }
                    //拾得日期
                    if(crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE")) ){
                        data.put("OP_PU_DATE","");
                    }else{
                        data.put("OP_PU_DATE",DateUtil.to7TwDateTime2( crs.getString("OP_PU_DATE").toString()));
                    }
                    //拾得人
                    if(crs.getString("OP_PUPO_NAME")==null || "".equals(crs.getString("OP_PUPO_NAME"))){
                        data.put("OP_PUPO_NAME","");
                    }else{
                        data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));
                    }
                    //拾得時間
                    if(crs.getString("OP_RT_DATE")== null || "".equals(crs.getString("OP_RT_DATE").toString())){
                        data.put("OP_RT_DATE","");
                    }else{
                        data.put("OP_RT_DATE",DateUtil.to7TwDateTime2( crs.getString("OP_RT_DATE").toString()));
                    }
                    //編號
                    data.put("SEQ", i);
                    i++;
                    datas.add(data);
                }
                if (flagData) {
                    list.put("header", datas);
                    obj.put("IS_HAVE", "Y"); //判斷有無資料
                } else {
                    HashMap data = new HashMap();
                    data.put("OP_AC_RCNO","");
                    data.put("OP_AC_UNIT_NM", "");
                    data.put("OP_PU_DATE", "");
                    data.put("OP_PUPO_NAME", "");
                    data.put("OP_RT_DATE", "");
                    data.put("SEQ", "");
                    
                    datas.add(data);
                    list.put("header", datas);
                    obj.put("IS_HAVE", "N"); //判斷有無資料
                }
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "拾得公告期滿拾得人(或委其親友)領回清冊");
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "拾得公告期滿拾得人(或委其親友)領回清冊");
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "拾得公告期滿拾得人(或委其親友)領回清冊");
                } else {
                    tempObj.put("UNIT", "拾得公告期滿拾得人(或委其親友)領回清冊");
                }
                tempObj.put("TODAY", "製表日期：" + getNowTime());
                tempObj.put("NOW_UNIT", "製表單位：" + voUser.getUnitName());
                obj.put("newObj", tempObj);
               
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }   
       
        return list;
    }
    //未經認領及領回拾得物移交地方自治團體清冊
   public HashMap printByOP07A15Q_DOC(JSONObject obj) {
        User voUser = new User();
        HashMap<String, Object> map = new HashMap();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
       
        try {
            subsql.append(" SELECT DISTINCT BASIC.OP_AC_RCNO, 0 SEQ, '' OP_PUOJ_NM, BASIC.OP_SEQ_NO, ");
            subsql.append(" FNSH.OP_YN_FS, FNSH.OP_FS_REC_CD, ANDL.OP_NTC_PUPO_DT, PUANDL.OP_AC_REG_ATNO, PUANDL.OP_SD_UNIT_NM, PUANDL.OP_SD_UNIT_CD, ");
            subsql.append(" BASIC.OP_PUPO_NAME, ANDL.OP_NTC_PUPO_DT, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_UNIT_CD, BASIC.OP_PU_CITY_NM, BASIC.OP_PU_TOWN_NM, BASIC.OP_PU_PLACE, BASIC.OP_PU_DATE  ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC LEFT JOIN OPDT_I_AN_DL ANDL ON ANDL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO ");
            subsql.append(" LEFT JOIN OPDT_I_FNSH FNSH ON FNSH.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO");
            subsql.append(" LEFT JOIN OPDT_I_PUAN_DL PUANDL ON PUANDL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO");
            subsql.append(" LEFT JOIN OPDT_I_PU_DETAIL DETAIL ON DETAIL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO ");
            subsql.append(" WHERE 1=1 ");
            subsql.append(" AND PUANDL.OP_AC_REG_ATNO IS NOT NULL  ");
            subsql.append(" AND PUANDL.OP_AC_REG_ATNO <>'' ");
            subsql.append(" AND ISNULL(BASIC.OP_DEL_FLAG,'0')<>'1' ");
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            //通知拾得人領回日期起迄
            if (obj.has("OP_NTC_PUPO_DT_S") && !obj.getString("OP_NTC_PUPO_DT_S").equals("")) {
                subsql.append( " AND ANDL.OP_NTC_PUPO_DT >= ?  ");
                args.add(obj.getString("OP_NTC_PUPO_DT_S"));
            }
            if (obj.has("OP_NTC_PUPO_DT_E") && !obj.getString("OP_NTC_PUPO_DT_E").equals("")) {
                subsql.append( " AND ANDL.OP_NTC_PUPO_DT <= ?  ");
                args.add(obj.getString("OP_NTC_PUPO_DT_E"));
            }
            //判斷物品/現金/全部
//                   if (obj.has("OP_TYPE_CD") && obj.getString("OP_TYPE_CD").equals("1")){
//                        subsql.append( " AND DETAIL.OP_TYPE_CD = 'G001'  ");
//                   }else if (obj.has("OP_TYPE_CD") && obj.getString("OP_TYPE_CD").equals("0")){
//                        subsql.append( " AND DETAIL.OP_TYPE_CD <> 'G001' ");
//                   }

                   //G001是其他  (在舊物品種類中，G001是新台幣。  在新物品種類中A001是新臺幣 )
                   //判斷物品/現金/全部
                   if (obj.has("OP_TYPE_CD") && obj.getString("OP_TYPE_CD").equals("1")){
                        subsql.append( " AND DETAIL.OP_TYPE_CD = 'A001'  ");
                   }else if (obj.has("OP_TYPE_CD") && obj.getString("OP_TYPE_CD").equals("0")){
                        subsql.append( " AND DETAIL.OP_TYPE_CD <> 'A001' ");
                   }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            subsql.append(" ORDER BY BASIC.OP_PU_DATE");
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
                if (obj.has("OP_AC_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_UNIT_CD")) + "未經認領及領回拾得物移交地方自治團體清冊");
                } else if (obj.has("OP_AC_B_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_B_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_B_UNIT_CD")) + "未經認領及領回拾得物移交地方自治團體清冊");
                } else if (obj.has("OP_AC_D_UNIT_CD") && !StringUtil.nvl(obj.getString("OP_AC_D_UNIT_CD")).equals("")) {
                    tempObj.put("UNIT", getUnitNm(obj.getString("OP_AC_D_UNIT_CD")) + "未經認領及領回拾得物移交地方自治團體清冊");
                } else {
                    tempObj.put("UNIT", "未經認領及領回拾得物移交地方自治團體清冊");
                }
                obj.put("newObj", tempObj);
                Boolean flagData = false;
                int i = 1;
                while (crs.next()) {
                    flagData = true;
                    HashMap data = new HashMap();
                    //收據編號
                    if(crs.getString("OP_AC_RCNO")==null || "".equals(crs.getString("OP_AC_RCNO"))){
                        crs.updateString("OP_AC_RCNO","");
                    }else{
                        crs.updateString("OP_AC_RCNO",crs.getString("OP_AC_RCNO"));
                    }
                    //拾得日期
                    if(crs.getString("OP_PU_DATE")==null || "".equals(crs.getString("OP_PU_DATE")) ){
                        crs.updateString("OP_PU_DATE","");
                    }else{
                        crs.updateString("OP_PU_DATE",DateUtil.to7TwDateTime( crs.getString("OP_PU_DATE").toString()));
                    }
                    //通知或公告領取日期
                    if(crs.getString("OP_NTC_PUPO_DT")==null || "".equals(crs.getString("OP_NTC_PUPO_DT"))){
                        crs.updateString("OP_NTC_PUPO_DT","");
                    }else{
                        crs.updateString("OP_NTC_PUPO_DT", DateUtil.to7TwDateTime(crs.getString("OP_NTC_PUPO_DT").toString()));
                    }
                    //拾得地點
                    crs.updateString("OP_PU_PLACE",  crs.getString("OP_PU_CITY_NM")+crs.getString("OP_PU_TOWN_NM")+crs.getString("OP_PU_PLACE"));
                    //判斷物品/現金/全部
                   if (obj.has("OP_TYPE_CD") && obj.getString("OP_TYPE_CD").equals("1")){
                        crs.updateString("OP_PUOJ_NM", getOpPuojNmAndQty2( crs.getString("OP_SEQ_NO")));
                   }else if (obj.has("OP_TYPE_CD") && obj.getString("OP_TYPE_CD").equals("0")){
                        crs.updateString("OP_PUOJ_NM", getOpPuojNmAndQty3( crs.getString("OP_SEQ_NO")));
                   }else{
                       crs.updateString("OP_PUOJ_NM", getOpPuojNmAndQty( crs.getString("OP_SEQ_NO")));
                   }
                    //保管地警察機關
                    crs.updateString("OP_SD_UNIT_NM",getUnitSNm(crs.getString("OP_SD_UNIT_CD")));
                    //是否結案
                    if(crs.getString("OP_YN_FS")==null || "".equals(crs.getString("OP_YN_FS"))){
                        crs.updateString("OP_YN_FS","否");
                    }else{
                        crs.updateString("OP_YN_FS","是");
                    }
                    //項次
                    crs.updateInt("SEQ",i);
                    i++;
                } map.put("BasicList", crs);
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }   
        return map;
    }

    public Map<String, List> printByOP07A10Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT BASIC.OP_PUPO_ZIPCODE, BASIC.OP_OC_ADDR_TYPE_CD, BASIC.OP_PUPO_NAME, BASIC.OP_PUPO_CITY_NM, BASIC.OP_PUPO_TOWN_NM, BASIC.OP_PUPO_VILLAGE_NM, "); 
            subsql.append(" BASIC.OP_PUPO_LIN, BASIC.OP_PUPO_ROAD ,BASIC.OP_PUPO_ADDR_OTH ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" WHERE 1=1 ");
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //收據編號
            if (obj.has("OP_AC_RCNO") && !obj.getString("OP_AC_RCNO").equals("")) {
                subsql.append( " AND BASIC.OP_AC_RCNO = ?  ");
                args.add(obj.getString("OP_AC_RCNO"));
            }
            
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            Boolean flagData = false;
            while (crs.next()) {
                flagData = true;
                HashMap data = new HashMap();
                StringBuilder addr = new StringBuilder();
                String strOP_PUOJ_NM = "";
                
                if(crs.getString("OP_PUPO_ZIPCODE")!=null && !"".equals(crs.getString("OP_PUPO_ZIPCODE"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_ZIPCODE") + " ";
                }
                strOP_PUOJ_NM = strOP_PUOJ_NM + "\n";
                if(crs.getString("OP_PUPO_CITY_NM")!=null && !"".equals(crs.getString("OP_PUPO_CITY_NM"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_CITY_NM") ;
                }
                if(crs.getString("OP_PUPO_TOWN_NM")!=null && !"".equals(crs.getString("OP_PUPO_TOWN_NM"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_TOWN_NM") ;
                }
                if(crs.getString("OP_PUPO_VILLAGE_NM")!=null && !"".equals(crs.getString("OP_PUPO_VILLAGE_NM"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_VILLAGE_NM") ;
                }
                if(crs.getString("OP_PUPO_LIN")!=null && !"".equals(crs.getString("OP_PUPO_LIN"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_LIN") ;
                    strOP_PUOJ_NM = strOP_PUOJ_NM + "鄰";
                }
                if(crs.getString("OP_PUPO_ROAD")!=null && !"".equals(crs.getString("OP_PUPO_ROAD"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_ROAD") ;
                }
                if(crs.getString("OP_PUPO_ADDR_OTH")!=null && !"".equals(crs.getString("OP_PUPO_ADDR_OTH"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUPO_ADDR_OTH") ;
                }
                strOP_PUOJ_NM += "\n" +  crs.getString("OP_PUPO_NAME") +"\t\t\t\t\t" + "先生/小姐"; 
                
                data.put("OP_PUOJ_NM", strOP_PUOJ_NM);

                datas.add(data);
            }
            if (flagData) {
                list.put("header", datas);
                obj.put("IS_HAVE", "Y"); //判斷有無資料
            } else {
                HashMap data = new HashMap();
                data.put("OP_PUOJ_NM", "");
                datas.add(data);
                list.put("header", datas);
                obj.put("IS_HAVE", "N"); //判斷有無資料
            }
            tempObj.put("TODAY", "製表日期：" + getNowTime());
            tempObj.put("NOW_UNIT", "列印單位：" + voUser.getUnitName());
            obj.put("newObj", tempObj);
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public Map<String, List> printByOP07A07Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT DETAIL.OP_AC_RCNO, DETAIL.OP_PUOJ_NM, DETAIL.OP_FEATURE, DETAIL.OP_QTY, DETAIL.OP_QTY_UNIT,  ");
            subsql.append(" BASIC.OP_PUPO_NAME, ANDL.OP_NTC_PUPO_DT, BASIC.OP_AC_UNIT_NM  ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC INNER JOIN OPDT_I_PU_DETAIL DETAIL ON DETAIL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO ");
            subsql.append(" LEFT JOIN OPDT_I_AN_DL ANDL ON ANDL.OP_BASIC_SEQ_NO = BASIC.OP_SEQ_NO");
            subsql.append(" WHERE 1=1 ");
            subsql.append(" AND BASIC.OP_CURSTAT_CD = '5'  ");
            subsql.append(" AND BASIC.OP_SEQ_NO = DETAIL.OP_BASIC_SEQ_NO  ");
            subsql.append(" AND ISNULL(BASIC.OP_DEL_FLAG,'0')<>'1' ");

            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //收據編號
            if (obj.has("OP_AC_RCNO") && !obj.getString("OP_AC_RCNO").equals("")) {
                subsql.append( " AND DETAIL.OP_AC_RCNO = ?  ");
                args.add(obj.getString("OP_AC_RCNO"));
            }
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            Boolean flagData = false;
            while (crs.next()) {
                flagData = true;
                HashMap data = new HashMap();
                StringBuilder addr = new StringBuilder();
                data.put("OP_AC_RCNO", crs.getString("OP_AC_RCNO"));
                String strOP_PUOJ_NM = "";
                if(crs.getString("OP_PUOJ_NM")!=null && !"".equals(crs.getString("OP_PUOJ_NM"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_PUOJ_NM") + "-";
                }
                if(crs.getString("OP_FEATURE")!=null && !"".equals(crs.getString("OP_FEATURE"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_FEATURE") + "-";
                }
                if(crs.getString("OP_QTY")!=null && !"".equals(crs.getString("OP_QTY"))){
                    String[] opQty = crs.getString("OP_QTY").toString().split("\\.");
                    if( opQty[1].equals("00") ){
                        strOP_PUOJ_NM = strOP_PUOJ_NM + opQty[0] + "-";
                    }else{
                        strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_QTY") + "-";
                    }
                }
                if(crs.getString("OP_QTY_UNIT")!=null && !"".equals(crs.getString("OP_QTY_UNIT"))){
                    strOP_PUOJ_NM = strOP_PUOJ_NM + crs.getString("OP_QTY_UNIT") ;
                }
                data.put("OP_PUOJ_NM", strOP_PUOJ_NM);
                data.put("OP_PUPO_NAME", crs.getString("OP_PUPO_NAME"));
                if (crs.getString("OP_NTC_PUPO_DT")==null || "".equals(crs.getString("OP_NTC_PUPO_DT"))){
                    data.put("OP_NTC_PUPO_DT", "");
                } else{
                    data.put("OP_NTC_PUPO_DT", DateUtil.to7TwDateTime(crs.getString("OP_NTC_PUPO_DT").toString()));
                }
                data.put("OP_AC_UNIT_NM", crs.getString("OP_AC_UNIT_NM"));
                datas.add(data);
            }
            if (flagData) {
                list.put("header", datas);
                obj.put("IS_HAVE", "Y"); //判斷有無資料
            } else {
                HashMap data = new HashMap();
                data.put("OP_AC_RCNO", "");
                data.put("OP_PUOJ_NM", "");
                data.put("OP_PUPO_NAME", "");
                data.put("OP_NTC_PUPO_DT", "");
                data.put("OP_AC_UNIT_NM", "");
                datas.add(data);
                list.put("header", datas);
                obj.put("IS_HAVE", "N"); //判斷有無資料
            }
            obj.put("newObj", tempObj);
            
            
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
       
        return list;
    }
    public Map<String, List> printByOP07A08Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        CachedRowSet crs1 = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try {
            subsql.append(" SELECT DISTINCT BASIC.OP_SEQ_NO, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM, ISNULL(FNSH.OP_FS_REC_CD,'') AS OP_FS_REC_CD ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" LEFT JOIN OPDT_I_FNSH FNSH ON BASIC.OP_SEQ_NO = FNSH.OP_BASIC_SEQ_NO ");
            subsql.append(" WHERE 1 = 1 ");
            subsql.append(" AND isNull(BASIC.OP_DEL_FLAG, '0') <> '1' ");
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
                }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //拾得日期
            if (obj.has("OP_PU_DATE_S") && !obj.getString("OP_PU_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE >= ?  ");
                args.add(obj.getString("OP_PU_DATE_S"));
            }
            if (obj.has("OP_PU_DATE_E") && !obj.getString("OP_PU_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_PU_DATE <= ?  ");
                args.add(obj.getString("OP_PU_DATE_E"));
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            //結案日期
            if (obj.has("OP_FS_DATE_S") && !obj.getString("OP_FS_DATE_S").equals("")) {
                subsql.append( " AND FNSH.OP_FS_DATE >= ?  ");
                args.add(obj.getString("OP_FS_DATE_S"));
            }
            if (obj.has("OP_FS_DATE_E") && !obj.getString("OP_FS_DATE_E").equals("")) {
                subsql.append( " AND FNSH.OP_FS_DATE <= ?  ");
                args.add(obj.getString("OP_FS_DATE_E"));
            }
            subsql.append(" order by BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM");
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            String acUnitNm = "";
            int totalAcCount = 0;          //計算受理件數，也就是opSeqNo<>null
            int totalFsRecCd01 = 0;      //計算遺失人領回件數，也就是fsRecCd='01'
            int totalFsRecCd02 = 0;      //計算拾得人領取件數，也就是fsRecCd='02'
            int totalFsRecCdOther = 0; //計算未領回件數，也就是fsRecCd<>'01' and fsRecCd<>'02'
            totalAcCount = crs.size(); //拾得遺失物合計
            List<HashMap> caseList = new ArrayList<HashMap>();
            while(crs.next()){
                HashMap map = new HashMap();
                map.put("OP_AC_UNIT_CD", crs.getString(2));
                map.put("OP_AC_UNIT_NM", crs.getString(3));
                map.put("OP_FS_REC_CD", crs.getString(4));
                caseList.add(map);
            }
            //取得不重複的單位代碼
            String countDis = subsql.toString().replace(" SELECT DISTINCT BASIC.OP_SEQ_NO, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM, ISNULL(FNSH.OP_FS_REC_CD,'') AS OP_FS_REC_CD "," SELECT DISTINCT BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM ");
            crs1 = this.pexecuteQueryRowSet(countDis, args.toArray());
            //根據不重複的單位代碼去撈List sqlPuBasic裡面的資料作計算
            int count01 = 0;
            int count02 = 0;
            int countOther = 0;
            int index = 0; //用來表示撈到sqlPuBasic的第幾筆資料
            Boolean flagData = false;
            while (crs1.next()) {
                flagData = true;
                HashMap data = new HashMap();
                String disUnitCd = crs1.getString(1);
                String disUnitNm = crs1.getString(2);
                for (int j = index; j< caseList.size(); j++){
                    HashMap puBD = (HashMap) caseList.get(j);
                    if ( puBD.get("OP_AC_UNIT_CD").equals(disUnitCd) && puBD.get("OP_AC_UNIT_NM").equals(disUnitNm) ){
                        acUnitNm = puBD.get("OP_AC_UNIT_NM").toString();
                        if( puBD.get("OP_FS_REC_CD").equals("01") )
                            count01++;
                        else if( puBD.get("OP_FS_REC_CD").equals("02") )
                            count02++;
                        else
                            countOther++;                       
                    } else{
                        index = j;                      //記住找到第幾筆，下次從此比開始找
                        j = caseList.size();   //跳出for迴圈
                    }
                }
                data.put("OP_AC_UNIT_NM", acUnitNm);
                data.put("ALL", String.valueOf(count01 + count02 + countOther));
                data.put("OP_FS_REC_CD_1", String.valueOf(count01));
                data.put("OP_FS_REC_CD_2", String.valueOf(count02));
                data.put("TOTAL", String.valueOf(count01+count02));
                data.put("OP_FS_REC_CD_OTH", String.valueOf(countOther));
                datas.add(data);
                totalFsRecCd01 = totalFsRecCd01 + count01;
                totalFsRecCd02 = totalFsRecCd02 + count02;
                totalFsRecCdOther = totalFsRecCdOther + countOther;
                count01 = 0;
                count02 = 0;
                countOther = 0;
                acUnitNm = "";
            }
            HashMap totaldata = new HashMap();
            totaldata.put("OP_AC_UNIT_NM", "合計");
            totaldata.put("ALL", String.valueOf(totalAcCount));
            totaldata.put("OP_FS_REC_CD_1", String.valueOf(totalFsRecCd01));
            totaldata.put("OP_FS_REC_CD_2", String.valueOf(totalFsRecCd02));
            totaldata.put("TOTAL", String.valueOf(totalFsRecCd01+totalFsRecCd02));
            totaldata.put("OP_FS_REC_CD_OTH", String.valueOf(totalFsRecCdOther));
            datas.add(totaldata);
            if (flagData) {
                list.put("header", datas);
                obj.put("IS_HAVE", "Y"); //判斷有無資料
            } else {
                HashMap data = new HashMap();
                data.put("OP_AC_UNIT_NM", "");
                data.put("ALL", "");
                data.put("OP_FS_REC_CD_1", "");
                data.put("OP_FS_REC_CD_2", "");
                data.put("TOTAL", "");
                data.put("OP_FS_REC_CD_OTH", "");
                datas.add(data);
                list.put("header", datas);
                obj.put("IS_HAVE", "N"); //判斷有無資料
            }
            tempObj.put("UNIT", "拾得遺失物處理情形統計表");
            tempObj.put("TODAY", "製表日期：" + getNowTime());
            tempObj.put("NOW_UNIT", "列印單位：" + voUser.getUnitName());
            obj.put("newObj", tempObj);
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
        return list;
    }
    public Map<String, List> printByOP07A09Q(JSONObject obj) {
        User voUser = new User();
        voUser = (User) obj.get("userVO");
        Map<String, List> list = new HashMap<String, List>();
        StringBuilder subsql = new StringBuilder();
        ArrayList args = new ArrayList();
        CachedRowSet crs = null;
        List<Map> datas = new ArrayList<Map>();
        JSONObject tempObj = new JSONObject();
        try{
            subsql.append(" SELECT DISTINCT BASIC.OP_SEQ_NO, BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_DATE, ISNULL(AN.OP_YN_AN,'') as OP_YN_AN, ISNULL(AN.OP_AN_DATE_END,'') as OP_AN_DATE_END, ISNULL(FNSH.OP_YN_FS,'') as OP_YN_FS, ISNULL(AN.OP_YN_AN_END,'') as OP_YN_AN_END, BASIC.OP_CURSTAT_CD ");
            subsql.append(" FROM OPDT_I_PU_BASIC BASIC ");
            subsql.append(" LEFT JOIN OPDT_I_ANNOUNCE AN ON AN.OP_BASIC_SEQ_NO=BASIC.OP_SEQ_NO ");
            subsql.append(" LEFT JOIN OPDT_I_FNSH FNSH ON FNSH.OP_BASIC_SEQ_NO=BASIC.OP_SEQ_NO ");
            subsql.append(" WHERE isNull(BASIC.OP_DEL_FLAG,'0')<>'1' ");
           if (obj.has("includeYN")&& obj.getString("includeYN").equals("Y")){
                if( obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("") && !obj.getString("OP_AC_D_UNIT_CD").equals("0000") && !obj.getString("OP_AC_D_UNIT_CD").equals("A1000") ){
                    //警政署以外警察局單位
                    if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                              subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                              args.add(obj.getString("OP_AC_UNIT_CD"));
                    }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_B_UNIT_CD  = ?  ");
                          args.add(obj.getString("OP_AC_B_UNIT_CD"));
                    }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                          subsql.append( " AND BASIC.OP_AC_D_UNIT_CD = ?  ");
                          args.add(obj.getString("OP_AC_D_UNIT_CD"));
                    }
               }
            }else{
                if (obj.has("OP_AC_UNIT_CD") && !obj.getString("OP_AC_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_UNIT_CD"));
                }else if (obj.has("OP_AC_B_UNIT_CD") && !obj.getString("OP_AC_B_UNIT_CD").equals("")) {
                     subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_B_UNIT_CD"));
                }else if (obj.has("OP_AC_D_UNIT_CD") && !obj.getString("OP_AC_D_UNIT_CD").equals("")) {
                      subsql.append( " AND BASIC.OP_AC_UNIT_CD = ?  ");
                      args.add(obj.getString("OP_AC_D_UNIT_CD"));
                }
            }
            //受理日期
            if (obj.has("OP_AC_DATE_S") && !obj.getString("OP_AC_DATE_S").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE >= ?  ");
                args.add(obj.getString("OP_AC_DATE_S"));
            }
            if (obj.has("OP_AC_DATE_E") && !obj.getString("OP_AC_DATE_E").equals("")) {
                subsql.append( " AND BASIC.OP_AC_DATE <= ?  ");
                args.add(obj.getString("OP_AC_DATE_E"));
            }
            subsql.append("ORDER BY BASIC.OP_AC_UNIT_CD, BASIC.OP_AC_UNIT_NM, BASIC.OP_AC_RCNO");
            String sysDtTm = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            int year = Integer.parseInt(sysDtTm.substring(0, 4));
            int month = Integer.parseInt(sysDtTm.substring(4, 6))-1;
            int day = Integer.parseInt(sysDtTm.substring(6, 8));
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.set(year, month, day);
            crs = this.pexecuteQueryRowSet(subsql.toString(), args.toArray());
            ArrayList<HashMap> mapList = new ArrayList<HashMap>();
            while(crs.next()){
                HashMap map = new HashMap();
                map.put("OP_AC_UNIT_CD", crs.getString(2));
                map.put("OP_AC_UNIT_NM", crs.getString(3));
                map.put("OP_AC_DATE", crs.getString(4));
                map.put("OP_YN_AN", crs.getString(5)); //OP_YN_AN是否公告 0 or 空白 or Null等於未公告;1：已發內部公告;2：已發網路公告
                map.put("OP_AN_DATE_END", crs.getString(6));//公告期滿日
                map.put("OP_YN_FS", crs.getString(7));//是否結案 0 or 空白 or Null等於未結案; 1:已結案
                map.put("OP_YN_AN_END", crs.getString(8));//是否公告期滿 0 or 空白 or Null等於未期滿; 1:已期滿
                map.put("OP_CURSTAT_CD",crs.getString(9));//目前狀態代碼 有1~6
                mapList.add(map);
            }
            String acUnitNm = "";
            int acNoAnCount = 0;                //受理超過X天未公告件數，也就是opAcDate < acNoAnDate
            int anEndnoCloseCount = 0;      //公告期滿X個月未結案件數，也就是opAnDateEnd < anEndnoClose
            int innerAnCount = 0;                //計算拾內部公告件數，也就是opYnAn=1
            int webAnCount = 0;                 //計算拾外部公告件數，也就是opYnAn=2
            int totalAnCount = 0;                //計算內外部公告件數，也就是opYnAn=1 or opYnAn=2
            int allAcNoAnCount = 0;
            int allAnEndnoCloseCount = 0;
            int allInnerAnCount = 0;
            int allWebAnCount = 0;
            int allTotalAnCount = 0;
            String acNoAnDate = "";         //已受理未公告deadline日期
            String anEndnoCloseDate = "" ;  //已公告期滿未結案deadline日期
            Boolean flagData = false;
            if(!mapList.isEmpty()){
                flagData = true;
                if(mapList.size()!=1){//在table中找到兩筆以上資料
                    for(int i = 0; i < mapList.size(); i++){
                        HashMap puBD = (HashMap) mapList.get(i);
                        if(!acUnitNm.equals(puBD.get("OP_AC_UNIT_NM").toString())){ //如果該單位只有一筆資料就馬上印出
                            if(!acUnitNm.equals("") && i != (mapList.size()-1)){
                                HashMap data = new HashMap();
                                data.put("OP_AC_UNIT_NM", acUnitNm);
                                data.put("OverMonCount", acNoAnCount);
                                data.put("OP_YN_AN_IN", innerAnCount);
                                data.put("OP_YN_AN_OUT", webAnCount);
                                data.put("YN_AN_ALL", totalAnCount);
                                data.put("AN_UNFinish", anEndnoCloseCount);
                                datas.add(data);
                            }
                            acNoAnCount = 0;
                            anEndnoCloseCount = 0;
                            innerAnCount = 0;
                            webAnCount = 0;
                            totalAnCount = 0;
                            
                            acUnitNm = puBD.get("OP_AC_UNIT_NM").toString();
                            
                            //是否為已受理未公告案件
                            if(puBD.get("OP_CURSTAT_CD").toString().equals("1")){
                                //計算該筆案件最後需公告日期
                                java.util.Calendar puBD2 = Calendar.getInstance();
                                puBD2.setTime(new java.text.SimpleDateFormat("yyyyMMdd").parse(puBD.get("OP_AC_DATE").toString()));
                                //判斷已受理但未公告案件條件是否為未填
                                if("".equals(obj.optString("OP_AC_NO_AN")) && "".equals(obj.optString("OP_AC_NO_AN_CMB"))){
                                    acNoAnCount++;
                                    allAcNoAnCount++;
                                }else{
                                    if(!"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "1".equals(obj.optString("OP_AC_NO_AN_CMB"))){   //天
                                        puBD2.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(obj.optString("OP_AC_NO_AN"))));
                                        acNoAnDate = new SimpleDateFormat("yyyyMMdd").format(puBD2.getTime());
                                        //如果系統日期大於等於最後需公告日期, 則計入已受理未公告件數
                                        if(Integer.parseInt(acNoAnDate) <= Integer.parseInt(sysDtTm)){
                                            acNoAnCount++;
                                            allAcNoAnCount++;
                                        }
                                    }else if(!"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "2".equals(obj.optString("OP_AC_NO_AN_CMB"))){   //月
                                        puBD2.add(Calendar.MONTH, (Integer.parseInt(obj.optString("OP_AC_NO_AN"))) );
                                        acNoAnDate = new SimpleDateFormat("yyyyMMdd").format(puBD2.getTime());
                                        //如果系統日期大於等於最後需公告日期, 則計入已受理未公告件數
                                        if(Integer.parseInt(acNoAnDate) <= Integer.parseInt(sysDtTm)){
                                            acNoAnCount++;
                                            allAcNoAnCount++;
                                        }
                                    }
                                }
                            }
                            //是否為公告中的案件
                            if( puBD.get("OP_CURSTAT_CD").toString().equals("2") && !puBD.get("OP_YN_FS").toString().equals("1") ){
                                //是否為內部公告,若是,則計入內部公告件數
                                if(puBD.get("OP_YN_AN").toString().equals("1")){
                                    innerAnCount++;
                                    totalAnCount++;
                                    allInnerAnCount++;
                                    allTotalAnCount++;
                                }
                                //是否為網路公告,若是,則計入網路公告件數
                                else if(puBD.get("OP_YN_AN").toString().equals("2")){
                                    webAnCount++;
                                    totalAnCount++;
                                    allWebAnCount++;
                                    allTotalAnCount++;
                                }
                            }
                            //公告期滿日期不為空值表示為公告期滿案件
                            if(!StringUtil.getString(puBD.get("OP_AN_DATE_END").toString()).equals("")){
                                //是否為已公告期滿但未結案案件
                                if(puBD.get("OP_YN_AN_END").toString().equals("1") && !puBD.get("OP_YN_FS").toString().equals("1")){
                                    //計算該筆案件最後需結案日期
                                    java.util.Calendar puBD4 = Calendar.getInstance();
                                    puBD4.setTime(new java.text.SimpleDateFormat("yyyyMMdd").parse(puBD.get("OP_AN_DATE_END").toString()));
                                    //判斷已公告期滿但未結案案件條件是否為未填
                                    if("".equals(obj.optString("OP_AN_NO_CLOSE")) && "".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){
                                        anEndnoCloseCount++;
                                        allAnEndnoCloseCount++;
                                    }else{
                                        if(!"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "1".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){   //天
                                            puBD4.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(obj.optString("OP_AN_NO_CLOSE"))));
                                            anEndnoCloseDate = new SimpleDateFormat("yyyyMMdd").format(puBD4.getTime());
                                            //若系統日期大於等於最後需結案日期,則為已期滿未結案案件
                                            if(Integer.parseInt(anEndnoCloseDate) <= Integer.parseInt(sysDtTm)){
                                                anEndnoCloseCount++;
                                                allAnEndnoCloseCount++;
                                            }
                                        }else if(!"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "2".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){   //月
                                            puBD4.add(Calendar.MONTH, (Integer.parseInt(obj.optString("OP_AN_NO_CLOSE"))) );
                                            anEndnoCloseDate = new SimpleDateFormat("yyyyMMdd").format(puBD4.getTime());
                                            //若系統日期大於等於最後需結案日期,則為已期滿未結案案件
                                            if(Integer.parseInt(anEndnoCloseDate) <= Integer.parseInt(sysDtTm)){
                                                anEndnoCloseCount++;
                                                allAnEndnoCloseCount++;
                                            }
                                        }
                                    }
                                }
                            }
                            if(i == (mapList.size()-1)){//如果整個table只有一筆資料就印出該單位統計資料以及合計
                                HashMap data = new HashMap();
                                data.put("OP_AC_UNIT_NM", acUnitNm);
                                data.put("OverMonCount", acNoAnCount);
                                data.put("OP_YN_AN_IN", innerAnCount);
                                data.put("OP_YN_AN_OUT", webAnCount);
                                data.put("YN_AN_ALL", totalAnCount);
                                data.put("AN_UNFinish", anEndnoCloseCount);
                                datas.add(data);
                                data = new HashMap();
                                data.put("OP_AC_UNIT_NM", "合計");
                                data.put("OverMonCount", allAcNoAnCount);
                                data.put("OP_YN_AN_IN", allInnerAnCount);
                                data.put("OP_YN_AN_OUT", allWebAnCount);
                                data.put("YN_AN_ALL", allTotalAnCount);
                                data.put("AN_UNFinish", allAnEndnoCloseCount);
                                datas.add(data);
                            }
                        } else{//如果該單位不止一筆資料，開始計算
                            //是否為已受理未公告案件
                            if(puBD.get("OP_CURSTAT_CD").toString().equals("1")){
                                //計算該筆案件最後需公告日期
                                java.util.Calendar puBD2 = Calendar.getInstance();
                                puBD2.setTime(new java.text.SimpleDateFormat("yyyyMMdd").parse(puBD.get("OP_AC_DATE").toString()));
                                //判斷已受理但未公告案件條件是否為未填
                                if("".equals(obj.optString("OP_AC_NO_AN")) && "".equals(obj.optString("OP_AC_NO_AN_CMB"))){
                                    acNoAnCount++;
                                    allAcNoAnCount++;
                                }else{
                                    if(!"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "1".equals(obj.optString("OP_AC_NO_AN_CMB"))){   //天
                                        puBD2.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(obj.optString("OP_AC_NO_AN"))));
                                        acNoAnDate = new SimpleDateFormat("yyyyMMdd").format(puBD2.getTime());
                                        if(Integer.parseInt(acNoAnDate) <= Integer.parseInt(sysDtTm)){
                                            acNoAnCount++;
                                            allAcNoAnCount++;
                                        }
                                    }else if(!"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "2".equals(obj.optString("OP_AC_NO_AN_CMB"))){   //月
                                        puBD2.add(Calendar.MONTH, (Integer.parseInt(obj.optString("OP_AC_NO_AN"))) );
                                        acNoAnDate = new SimpleDateFormat("yyyyMMdd").format(puBD2.getTime());
                                        if(Integer.parseInt(acNoAnDate) <= Integer.parseInt(sysDtTm)){
                                            acNoAnCount++;
                                            allAcNoAnCount++;
                                        }
                                    }
                                }
                            }
                            //是否為公告中的案件
                            if( puBD.get("OP_CURSTAT_CD").toString().equals("2") && !puBD.get("OP_YN_FS").toString().equals("1") ){
                                //是否為內部公告,若是,則計入內部公告件數
                                if(puBD.get("OP_YN_AN").toString().equals("1")){
                                    innerAnCount++;
                                    totalAnCount++;
                                    allInnerAnCount++;
                                    allTotalAnCount++;
                                }
                                //是否為網路公告,若是,則計入網路公告件數
                                else if(puBD.get("OP_YN_AN").toString().equals("2")){
                                    webAnCount++;
                                    totalAnCount++;
                                    allWebAnCount++;
                                    allTotalAnCount++;
                                }
                            }
                            //公告期滿日期不為空值表示為公告期滿案件
                            if(!StringUtil.getString(puBD.get("OP_AN_DATE_END").toString()).equals("")){
                                //是否為已公告期滿但未結案案件
                                if(puBD.get("OP_YN_AN_END").toString().equals("1") && !puBD.get("OP_YN_FS").toString().equals("1")){
                                    //計算該筆案件最後需結案日期
                                    java.util.Calendar puBD4 = Calendar.getInstance();
                                    puBD4.setTime(new java.text.SimpleDateFormat("yyyyMMdd").parse(puBD.get("OP_AN_DATE_END").toString()));
                                    //判斷已公告期滿但未結案案件條件是否為未填
                                    if("".equals(obj.optString("OP_AN_NO_CLOSE")) && "".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){
                                        anEndnoCloseCount++;
                                        allAnEndnoCloseCount++;
                                    }else{
                                        if(!"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "1".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){   //天
                                            puBD4.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(obj.optString("OP_AN_NO_CLOSE"))));
                                            anEndnoCloseDate = new SimpleDateFormat("yyyyMMdd").format(puBD4.getTime());
                                            //若系統日期大於等於最後需結案日期,則為已期滿未結案案件
                                            if(Integer.parseInt(anEndnoCloseDate) <= Integer.parseInt(sysDtTm)){
                                                anEndnoCloseCount++;
                                                allAnEndnoCloseCount++;
                                            }
                                        }else if(!"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "2".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){   //月
                                            puBD4.add(Calendar.MONTH, (Integer.parseInt(obj.optString("OP_AN_NO_CLOSE"))) );
                                            anEndnoCloseDate = new SimpleDateFormat("yyyyMMdd").format(puBD4.getTime());
                                            //若系統日期大於等於最後需結案日期,則為已期滿未結案案件
                                            if(Integer.parseInt(anEndnoCloseDate) <= Integer.parseInt(sysDtTm)){
                                                anEndnoCloseCount++;
                                                allAnEndnoCloseCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(i == (mapList.size()-1)){
                            HashMap data = new HashMap();
                            data.put("OP_AC_UNIT_NM", acUnitNm);
                            data.put("OverMonCount", acNoAnCount);
                            data.put("OP_YN_AN_IN", innerAnCount);
                            data.put("OP_YN_AN_OUT", webAnCount);
                            data.put("YN_AN_ALL", totalAnCount);
                            data.put("AN_UNFinish", anEndnoCloseCount);
                            datas.add(data);
                            data = new HashMap();
                            data.put("OP_AC_UNIT_NM", "合計");
                            data.put("OverMonCount", allAcNoAnCount);
                            data.put("OP_YN_AN_IN", allInnerAnCount);
                            data.put("OP_YN_AN_OUT", allWebAnCount);
                            data.put("YN_AN_ALL", allTotalAnCount);
                            data.put("AN_UNFinish", allAnEndnoCloseCount);
                            datas.add(data);
                        }
                    }
                } else{//在table中只有找到一筆資料
                    HashMap puBD = (HashMap) mapList.get(0);
                    acUnitNm = puBD.get("OP_AC_UNIT_NM").toString();
                    //是否為已受理未公告案件
                    if(!puBD.get("OP_AC_DATE").toString().equals("") && (puBD.get("OP_YN_AN").toString().equals("0") || puBD.get("OP_YN_AN").toString().equals(""))){
                        //計算該筆案件最後需公告日期
                        java.util.Calendar puBD2 = Calendar.getInstance();
                        puBD2.setTime(new java.text.SimpleDateFormat("yyyyMMdd").parse(puBD.get("OP_AC_DATE").toString()));
                        //判斷已受理但未公告案件條件是否為未填
                        if("".equals(obj.optString("OP_AC_NO_AN")) && "".equals(obj.optString("OP_AC_NO_AN_CMB"))){
                            acNoAnCount++;
                            allAcNoAnCount++;
                        }else{
                            if(!"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "1".equals(obj.optString("OP_AC_NO_AN_CMB"))){   //天
                                puBD2.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(obj.optString("OP_AC_NO_AN"))));
                                acNoAnDate = new SimpleDateFormat("yyyyMMdd").format(puBD2.getTime());
                                //如果系統日期大於等於最後需公告日期, 則計入已受理未公告件數
                                if(Integer.parseInt(acNoAnDate) <= Integer.parseInt(sysDtTm)){
                                    acNoAnCount++;
                                    allAcNoAnCount++;
                                }
                            }else if(!"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "2".equals(obj.optString("OP_AC_NO_AN_CMB"))){   //月
                                puBD2.add(Calendar.MONTH, (Integer.parseInt(obj.optString("OP_AC_NO_AN"))) );
                                acNoAnDate = new SimpleDateFormat("yyyyMMdd").format(puBD2.getTime());
                                //如果系統日期大於等於最後需公告日期, 則計入已受理未公告件數
                                if(Integer.parseInt(acNoAnDate) <= Integer.parseInt(sysDtTm)){
                                    acNoAnCount++;
                                    allAcNoAnCount++;
                                }
                            }
                        }
                    }
                    //是否為公告中的案件
                    if( puBD.get("OP_CURSTAT_CD").toString().equals("2") && !puBD.get("OP_YN_FS").toString().equals("1") ){
                        //是否為內部公告,若是,則計入內部公告件數
                        if(puBD.get("OP_YN_AN").toString().equals("1")){
                            innerAnCount++;
                            totalAnCount++;
                            allInnerAnCount++;
                            allTotalAnCount++;
                        }
                        //是否為網路公告,若是,則計入網路公告件數
                        else if(puBD.get("OP_YN_AN").toString().equals("2")){
                            webAnCount++;
                            totalAnCount++;
                            allWebAnCount++;
                            allTotalAnCount++;
                        }
                    }
                    //公告期滿日期不為空值表示為公告期滿案件
                    if(!StringUtil.getString(puBD.get("OP_AN_DATE_END").toString()).equals("")){                        
                        //是否為已公告期滿但未結案案件
                        if(puBD.get("OP_YN_AN_END").toString().equals("1") && !puBD.get("OP_YN_FS").toString().equals("1")){
                            //計算該筆案件最後需結案日期
                            java.util.Calendar puBD4 = Calendar.getInstance();
                            puBD4.setTime(new java.text.SimpleDateFormat("yyyyMMdd").parse(puBD.get("OP_AN_DATE_END").toString()));
                            //判斷已公告期滿但未結案案件條件是否為未填
                            if("".equals(obj.optString("OP_AN_NO_CLOSE")) && "".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){
                                anEndnoCloseCount++;
                                allAnEndnoCloseCount++;
                            }else{
                                if(!"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "1".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){   //天
                                puBD4.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(obj.optString("OP_AN_NO_CLOSE"))));
                                anEndnoCloseDate = new SimpleDateFormat("yyyyMMdd").format(puBD4.getTime());
                                    //若系統日期大於等於最後需結案日期,則為已期滿未結案案件
                                    if(Integer.parseInt(anEndnoCloseDate) <= Integer.parseInt(sysDtTm)){
                                        anEndnoCloseCount++;
                                        allAnEndnoCloseCount++;
                                    }
                                }else if(!"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "2".equals(obj.optString("OP_AN_NO_CLOSE_CMB"))){   //月
                                puBD4.add(Calendar.MONTH, (Integer.parseInt(obj.optString("OP_AN_NO_CLOSE"))) );
                                anEndnoCloseDate = new SimpleDateFormat("yyyyMMdd").format(puBD4.getTime());
                                    //若系統日期大於等於最後需結案日期,則為已期滿未結案案件
                                    if(Integer.parseInt(anEndnoCloseDate) <= Integer.parseInt(sysDtTm)){
                                        anEndnoCloseCount++;
                                        allAnEndnoCloseCount++;
                                    }
                                }
                            }
                        }
                    }
                    
                    HashMap data = new HashMap();
                    data.put("OP_AC_UNIT_NM", acUnitNm);
                    data.put("OverMonCount", acNoAnCount);
                    data.put("OP_YN_AN_IN", innerAnCount);
                    data.put("OP_YN_AN_OUT", webAnCount);
                    data.put("YN_AN_ALL", totalAnCount);
                    data.put("AN_UNFinish", anEndnoCloseCount);
                    datas.add(data);
                    data = new HashMap();
                    data.put("OP_AC_UNIT_NM", "合計");
                    data.put("OverMonCount", allAcNoAnCount);
                    data.put("OP_YN_AN_IN", allInnerAnCount);
                    data.put("OP_YN_AN_OUT", allWebAnCount);
                    data.put("YN_AN_ALL", allTotalAnCount);
                    data.put("AN_UNFinish", allAnEndnoCloseCount);
                    datas.add(data);
                }
            }
            if (flagData) {
                list.put("header", datas);
                obj.put("IS_HAVE", "Y"); //判斷有無資料
            } else {
                HashMap data = new HashMap();
                data.put("OP_AC_UNIT_NM", "");
                data.put("OverMonCount", "");
                data.put("OP_YN_AN_IN", "");
                data.put("OP_YN_AN_OUT", "");
                data.put("YN_AN_ALL", "");
                data.put("AN_UNFinish", "");
                datas.add(data);
                list.put("header", datas);
                obj.put("IS_HAVE", "N"); //判斷有無資料
            }
            tempObj.put("UNIT", "拾得遺失物公告件數統計表");
            tempObj.put("TODAY", "製表日期：" + getNowTime());
            tempObj.put("NOW_UNIT", "列印單位：" + voUser.getUnitName());
            //受理超過 ( 幾 )(天/月) 未公告件數
            String OP_AC_NO_AN = "已受理超過";
            String OP_AC_NO_AN2 = "已受理";
            if( !"".equals(obj.optString("OP_AC_NO_AN")) ){
                OP_AC_NO_AN = OP_AC_NO_AN + obj.optString("OP_AC_NO_AN");
            }
            if( !"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "1".equals(obj.optString("OP_AC_NO_AN_CMB")) ){ //天
                OP_AC_NO_AN = OP_AC_NO_AN + "天";
                OP_AC_NO_AN = OP_AC_NO_AN + "未公告件數";
                tempObj.put("OP_AC_NO_AN", OP_AC_NO_AN );
            }else if( !"".equals(obj.optString("OP_AC_NO_AN_CMB")) && "2".equals(obj.optString("OP_AC_NO_AN_CMB")) ){ //月
                OP_AC_NO_AN = OP_AC_NO_AN + "個月";
                OP_AC_NO_AN = OP_AC_NO_AN + "未公告件數";
                tempObj.put("OP_AC_NO_AN", OP_AC_NO_AN );
            }else{
                OP_AC_NO_AN2 = OP_AC_NO_AN2 + "未公告件數";//未填受理超過( 幾 )(天/月) 未公告件數的條件
                tempObj.put("OP_AC_NO_AN", OP_AC_NO_AN2 );
            }
            
            //公告期滿 ( 幾 )(天/月) 未結案件數
            String OP_AN_NO_CLOSE = "公告期滿超過";
            String OP_AN_NO_CLOSE2 = "公告期滿";
            if( !"".equals(obj.optString("OP_AN_NO_CLOSE")) ){
                OP_AN_NO_CLOSE = OP_AN_NO_CLOSE + obj.optString("OP_AN_NO_CLOSE");
            }
            if( !"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "1".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) ){ //天
                OP_AN_NO_CLOSE = OP_AN_NO_CLOSE + "天";
                OP_AN_NO_CLOSE = OP_AN_NO_CLOSE + "未結案件數";
                tempObj.put("OP_AN_NO_CLOSE", OP_AN_NO_CLOSE );
            }else if( !"".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) && "2".equals(obj.optString("OP_AN_NO_CLOSE_CMB")) ){ //月
                OP_AN_NO_CLOSE = OP_AN_NO_CLOSE + "個月";
                OP_AN_NO_CLOSE = OP_AN_NO_CLOSE + "未結案件數";
                tempObj.put("OP_AN_NO_CLOSE", OP_AN_NO_CLOSE );
            }else{
                 OP_AN_NO_CLOSE2 = OP_AN_NO_CLOSE2 + "未結案件數";//未填公告期滿 ( 幾 )(天/月) 未結案件數的條件
                 tempObj.put("OP_AN_NO_CLOSE", OP_AN_NO_CLOSE2);
            }
            obj.put("newObj", tempObj);
        }catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
        }
        return list;
    }

 
}
