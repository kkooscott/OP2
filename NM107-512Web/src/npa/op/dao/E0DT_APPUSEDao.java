package npa.op.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import npa.op.base.BaseDao;

public class E0DT_APPUSEDao extends BaseDao {

    private static E0DT_APPUSEDao instance = null;
    private static Logger log = Logger.getLogger(E0DT_APPUSEDao.class);

    private E0DT_APPUSEDao() {
    }

    public static E0DT_APPUSEDao getInstance() {
        if (instance == null) {
            instance = new E0DT_APPUSEDao();
        }
        return instance;
    }

    //apped委託查詢用途select
    public JSONArray appendSelect(JSONObject jObj) {
        ArrayList list = new ArrayList();
        String sql = "";

        if (jObj.getString("queryType").equals("appUse")) {
            sql = "SELECT E0_USENAME, E0_USECODE FROM ABDB..E0DT_APPUSE WHERE E0_APP_LEVEL = '1' ";
            list = pexecuteQuery(sql, null);
        } else if (jObj.getString("queryType").equals("appSubUse")) {
            sql = "SELECT E0_USENAME, E0_USECODE FROM ABDB..E0DT_APPUSE WHERE E0_APP_LEVEL = ? ";
            list = pexecuteQuery(sql, new Object[]{jObj.getString("selectAppUse")});
        }
        return arrayList2JsonArray(list);
    }
    
    //apped 委託查詢用途 預設是 『300行政類 301受處理報案』
    public JSONArray appendSubSelect(JSONObject jObj) {
        ArrayList list = new ArrayList();
        String sql = "";

//        if (jObj.getString("queryType").equals("appUse")) {
//            sql = "SELECT E0_USENAME, E0_USECODE FROM ABDB..E0DT_APPUSE WHERE E0_APP_LEVEL = '1'   ";
//            list = pexecuteQuery(sql, null);
//        } else if (jObj.getString("queryType").equals("appSubUse")) {
            sql = "SELECT E0_USENAME, E0_USECODE FROM ABDB..E0DT_APPUSE WHERE E0_APP_LEVEL = '300' ";
            list = pexecuteQuery(sql, null);
            //list = pexecuteQuery(sql, new Object[]{jObj.getString("selectAppUse")});
//        }
        return arrayList2JsonArray(list);
    }
    
}
