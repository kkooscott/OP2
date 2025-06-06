package npa.op.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import npa.op.util.DaoUtil;
import npa.op.util.DateUtil;
import npa.op.util.ExceptionUtil;
import npa.op.util.NPALOG2Util;
import npa.op.util.luence;
import npa.op.util.NPALOG2Util.LOGOPTYPE;
import npa.op.vo.User;
import npa.op.base.BaseDao;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import java.math.BigDecimal;
import npa.op.util.OPUtil;
import static npa.op.util.StringUtil.replaceWhiteChar;

public class OPDT_I_PU_DETAIL extends BaseDao {
	
	private static OPDT_I_PU_DETAIL instance = null;
	public static OPDT_I_PU_DETAIL getInstance() {
		if (instance == null) {
			instance = new OPDT_I_PU_DETAIL();
		}
		return instance;
	}
        
        //region 明細
	/**
	 * 查詢該受理明細序號資料 .(DB原始資料)
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryDetailByIdOriginal(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();
		CachedRowSet crs1,crs2;
		String sql = " SELECT *"
                           + " FROM OPDT_I_PU_DETAIL"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);
				
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());
		
		return arrayList2JsonArray(list);
	}
        
        //region 明細
	/**
        * 查詢該受理明細序號資料 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryIdForDetail(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //序號, 遺失物事件基本資料序號, 受理收據編號, 拾得物物品類別, 拾得物物品類別名稱, 拾得物品名稱, 數量, 單位,
                //顏色, 廠牌, 特徵, IMEI, MAC, 備考
                String sql = " SELECT OP_SEQ_NO, OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_TYPE_CD, OP_TYPE_NM, OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT,"
                           + " OP_COLOR_CD, OP_BRAND_CD, OP_FEATURE, OP_IMEI_CODE, OP_IMEI_CODE_2, OP_PHONE_NUM, OP_PHONE_NUM_2, OP_MAC_ADDR, OP_REMARK" 
                           + " FROM "
                           + " OPDT_I_PU_DETAIL "
                           + " WHERE 1 = 1 ";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_SEQ_NO").equals("")){
                    sql += " AND OP_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_SEQ_NO")));
                }

                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        /**
        * 查詢該收據編號受理明細資料 .
        * 
        * @param jObj
        * @return CachedRowSet
        */
        public CachedRowSet QueryAllForDetail(JSONObject jObj) {
                ArrayList args = new ArrayList();
                List listkey = null;
                //序號, 受理收據編號, 拾得物品名稱, 數量, 單位, 特徵,
                String sql = " SELECT OP_SEQ_NO, OP_AC_RCNO, OP_PUOJ_NM, OP_QTY, OP_QTY_UNIT, OP_FEATURE"                           
                           + " FROM "
                           + " OPDT_I_PU_DETAIL "
                           + " WHERE 1 = 1 ";
                
                //遺失物事件基本資料序號
                if (!jObj.get("OP_BASIC_SEQ_NO").equals("")){
                    sql += " AND OP_BASIC_SEQ_NO = ?";
                    args.add( Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                }
                sql += " ORDER BY OP_PUOJ_NM DESC";

                CachedRowSet crs = this.pexecuteQueryRowSet(sql,args.toArray());
                
                return crs;
        }
        
        // region 新增 model Start
        /**
         * 新增遺失物受理明細資料 .
         * 
         * @param jObj
         * @return boolean
         */
        public boolean add(JSONObject jObj) {
                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_TYPE_CD", replaceWhiteChar((String) jObj.get("OP_TYPE_CD")));
                        if( jObj.has("OP_TYPE_NM") && !jObj.getString("OP_TYPE_NM").equals("") ){
                            String[] opTypeNm = jObj.getString("OP_TYPE_NM").split("\\.");
                            if( opTypeNm.length > 1 ){
                                jObj.put("OP_TYPE_NM", opTypeNm[1]);
                            }else{
                                jObj.put("OP_TYPE_NM", "");
                            }
                        }else{
                            jObj.put("OP_TYPE_NM", "");
                        }
                        jObj.put("OP_PUOJ_NM", jObj.get("OP_PUOJ_NM"));
                        jObj.put("OP_QTY", jObj.getDouble("OP_QTY"));
                        jObj.put("OP_QTY_UNIT", jObj.get("OP_QTY_UNIT"));
                        jObj.put("OP_COLOR_CD", replaceWhiteChar((String) jObj.get("OP_COLOR_CD")));
                        jObj.put("OP_COLOR_NM", jObj.get("OP_COLOR_NM"));
                        jObj.put("OP_BRAND_CD", replaceWhiteChar((String) jObj.get("OP_BRAND_CD")));
                        jObj.put("OP_BRAND_NM", jObj.get("OP_BRAND_NM"));
                        jObj.put("OP_FEATURE", jObj.get("OP_FEATURE"));
                        jObj.put("OP_IMEI_CODE", jObj.get("OP_IMEI_CODE"));
                        jObj.put("OP_MAC_ADDR", jObj.get("OP_MAC_ADDR"));
                        jObj.put("OP_REMARK", jObj.get("OP_REMARK"));
                        jObj.put("OP_ADD_ID", voUser.getUserId());
                        jObj.put("OP_ADD_NM", voUser.getUserName());
                        jObj.put("OP_ADD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_ADD_UNIT_NM", voUser.getUnitName());
                        jObj.put("OP_ADD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        jObj.put("OP_PHONE_NUM", jObj.get("OP_PHONE_NUM"));//電信門號
                        jObj.put("OP_PHONE_NUM_2", jObj.get("OP_PHONE_NUM_2"));//電信門號2
                        jObj.put("OP_IMEI_CODE_2", jObj.get("OP_IMEI_CODE_2"));//IMEI 2
                        
                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "ADD");
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_TYPE_CD",
                                        "OP_TYPE_NM", //5
                                        "OP_PUOJ_NM",
                                        "OP_QTY",
                                        "OP_QTY_UNIT",
                                        "OP_COLOR_CD",
                                        "OP_COLOR_NM", //10
                                        "OP_BRAND_CD",
                                        "OP_BRAND_NM",
                                        "OP_FEATURE",
                                        "OP_IMEI_CODE",
                                        "OP_MAC_ADDR", //15
                                        "OP_REMARK",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD",
                                        "OP_ADD_UNIT_NM", //20
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM",
                                        "OP_PHONE_NUM",
                                        "OP_PHONE_NUM_2",
                                        "OP_IMEI_CODE_2"}; //28

                        sql.append("INSERT INTO OPDT_I_PU_DETAIL ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //20
                        .append(" ?,?,?,?,?,?,?,?)"); //28

                        Object[] paraObject = new Object[arryString.length];

                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        if ("OP_SEQ_NO".equals(strKey)){
                                                if (!"".equals(jObj.get(strKey).toString().trim()))
                                                        paraObject[i] = Integer.parseInt(jObj.get(strKey).toString());
                                                else {
                                                        paraObject[i] = 0;
                                                }
                                        }
                                        
                                        else{
                                                paraObject[i] = jObj.get(strKey);
                                        }
                                i++;
                        }

                        int  insetkey =getIdentityAfterInsert(sql.toString(),paraObject);
                        System.out.println("I_PU_DETAIL__insetkey___"+insetkey); //202402
                        if ( insetkey > 0 ){
                                luence lue = new luence();
                                jObj.put("OP_SEQ_NO", insetkey);
                                String basicSeqNo = jObj.get("OP_BASIC_SEQ_NO").toString();
                                JSONArray basicArray = iPuBasicDao.queryBasicByIdOriginal( Integer.valueOf(basicSeqNo) );
                                String OP_AC_D_UNIT_CD = basicArray.getJSONObject(0).getString("OP_AC_D_UNIT_CD");
                                String OP_AC_B_UNIT_CD = basicArray.getJSONObject(0).getString("OP_AC_B_UNIT_CD");
                                String OP_AC_UNIT_CD = basicArray.getJSONObject(0).getString("OP_AC_UNIT_CD");
                                //202403 錯誤的製作index造成吐大量error log
                                //lue.loadIndexForDetail("OP_PUOJ_NM", OP_AC_D_UNIT_CD, OP_AC_B_UNIT_CD, OP_AC_UNIT_CD, Integer.toString(insetkey), jObj.get("OP_BASIC_SEQ_NO").toString(), jObj.get("OP_PUOJ_NM").toString());
//                                lue.loadIndexForDetail("OP_PUOJ_NM", voUser.getUnitCd1(), Integer.toString(insetkey), jObj.get("OP_BASIC_SEQ_NO").toString(), jObj.get("OP_PUOJ_NM").toString());
                                //lue.loadIndex("OP_PUOJ_NM", jObj.get("OP_BASIC_SEQ_NO").toString(), jObj.get("OP_PUOJ_NM").toString());
                        }else{
                                returnValue = false;
                        }
                        //日誌需要字串
                        jObj.put("OP_QTY", String.valueOf(jObj.getDouble("OP_QTY")));

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;
        }
        // endregion 新增 model END
        
        // region 更新 Model Start
        /**
         * 更新遺失物受理明細資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean update(JSONObject jObj) {

                boolean returnValue = false;
                StringBuilder sql = new StringBuilder();
                Date current = new Date();
                OPDT_I_PU_BASIC iPuBasicDao = new OPDT_I_PU_BASIC();

                try {
                        User voUser = new User();
                        voUser = (User) jObj.get("userVO");
                        
                        jObj.put("OP_TYPE_CD", replaceWhiteChar((String) jObj.get("OP_TYPE_CD")));
                        if( jObj.has("OP_TYPE_NM") && !jObj.getString("OP_TYPE_NM").equals("") ){
                            String[] opTypeNm = jObj.getString("OP_TYPE_NM").split("\\.");
                            if( opTypeNm.length > 1 ){
                                jObj.put("OP_TYPE_NM", opTypeNm[1]);
                            }else{
                                jObj.put("OP_TYPE_NM", "");
                            }
                        }else{
                            jObj.put("OP_TYPE_NM", "");
                        }
                        jObj.put("OP_PUOJ_NM", jObj.get("OP_PUOJ_NM"));
                        jObj.put("OP_QTY", jObj.getDouble("OP_QTY"));
                        jObj.put("OP_QTY_UNIT", jObj.get("OP_QTY_UNIT"));
                        jObj.put("OP_COLOR_CD", replaceWhiteChar((String) jObj.get("OP_COLOR_CD")));
                        jObj.put("OP_COLOR_NM", jObj.get("OP_COLOR_NM"));
                        jObj.put("OP_BRAND_CD", replaceWhiteChar((String) jObj.get("OP_BRAND_CD")));
                        jObj.put("OP_BRAND_NM", jObj.get("OP_BRAND_NM"));
                        jObj.put("OP_FEATURE", jObj.get("OP_FEATURE"));
                        jObj.put("OP_IMEI_CODE", jObj.get("OP_IMEI_CODE"));
                        jObj.put("OP_MAC_ADDR", jObj.get("OP_MAC_ADDR"));
                        jObj.put("OP_REMARK", jObj.get("OP_REMARK"));             
                        jObj.put("OP_UPD_ID", voUser.getUserId());
                        jObj.put("OP_UPD_NM", voUser.getUserName());
                        jObj.put("OP_UPD_UNIT_CD", voUser.getUnitCd());
                        jObj.put("OP_UPD_UNIT_NM",  voUser.getUnitName());
                        jObj.put("OP_UPD_DT_TM", new SimpleDateFormat("yyyyMMddHHmmss").format(current).toString());
                        jObj.put("OP_PHONE_NUM", jObj.get("OP_PHONE_NUM"));//電信門號

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_TYPE_CD",
                                        "OP_TYPE_NM",
                                        "OP_PUOJ_NM",
                                        "OP_QTY", //5
                                        "OP_QTY_UNIT",
                                        "OP_COLOR_CD",
                                        "OP_COLOR_NM",
                                        "OP_BRAND_CD",
                                        "OP_BRAND_NM", //10
                                        "OP_FEATURE",
                                        "OP_IMEI_CODE",
                                        "OP_MAC_ADDR",
                                        "OP_REMARK",
                                        "OP_UPD_ID", //15
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM",
                                        "OP_UPD_DT_TM",
                                        "OP_PHONE_NUM", //20
                                        "OP_PHONE_NUM_2",
                                        "OP_IMEI_CODE_2"};

                        sql.append("UPDATE OPDT_I_PU_DETAIL SET ");

                        for (int index = 0 ;index < arryString.length ;index++){
                                sql.append(arryString[index] + "=?, ");
                        }
                        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1, "");
                        sql.append("WHERE OP_SEQ_NO =? ");


                        Object[] paraObject = new Object[arryString.length+1];

                        // paramenter
                        int i = 0;
                        for (String strKey:arryString){
                                paraObject[i] = new Object[i];
                                if (jObj.has(strKey))
                                        paraObject[i] = jObj.get(strKey);
                                i++;
                        }
                        //paraObject[arryString.length+1] = new Object[arryString.length+1];
                        paraObject[arryString.length] = jObj.getInt("OP_SEQ_NO");

                        returnValue = this.pexecuteUpdate(sql.toString(), paraObject) > 0 ? true : false;
                        
                        jObj.put("OP_SEQ_NO", jObj.get("OP_SEQ_NO"));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        //日誌需要字串
                        jObj.put("OP_QTY", String.valueOf(jObj.getDouble("OP_QTY")));
                        luence lue = new luence();
                        //lue.loadIndex("OP_PUOJ_NM", jObj.get("OP_BASIC_SEQ_NO").toString(), jObj.get("OP_PUOJ_NM").toString());
                        String basicSeqNo = jObj.get("OP_BASIC_SEQ_NO").toString();
                        JSONArray basicArray = iPuBasicDao.queryBasicByIdOriginal( Integer.valueOf(basicSeqNo) );
                        String OP_AC_D_UNIT_CD = basicArray.getJSONObject(0).getString("OP_AC_D_UNIT_CD");
                        String OP_AC_B_UNIT_CD = basicArray.getJSONObject(0).getString("OP_AC_B_UNIT_CD");
                        String OP_AC_UNIT_CD = basicArray.getJSONObject(0).getString("OP_AC_UNIT_CD");
                        //202403 錯誤的製作index造成吐大量error log
                        //lue.loadIndexForDetail("OP_PUOJ_NM", OP_AC_D_UNIT_CD, OP_AC_B_UNIT_CD, OP_AC_UNIT_CD, jObj.get("OP_SEQ_NO").toString(), jObj.get("OP_BASIC_SEQ_NO").toString(), jObj.get("OP_PUOJ_NM").toString());
//                        lue.loadIndexForDetail("OP_PUOJ_NM", voUser.getUnitCd1(), jObj.get("OP_SEQ_NO").toString(), jObj.get("OP_BASIC_SEQ_NO").toString(), jObj.get("OP_PUOJ_NM").toString());

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;

        }
        // endregion 更新 Model END
        
        // region 刪除 Model Start
        /**
         * 刪除遺失物受理明細資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean delete(JSONObject jObj) {
                boolean returnValue = false;
                int resultCount = 0;
                OPDT_I_PHOTO iPhotoDao = new OPDT_I_PHOTO();
		String sqlDelete = "DELETE FROM OPDT_I_PU_DETAIL WHERE OP_SEQ_NO=? " ;
		resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{jObj.getInt("OP_SEQ_NO")});
                if(resultCount > 0){
                    returnValue = true;
                    luence lue = new luence();
                    //202403 錯誤的製作index造成吐大量error log
                    //lue.deleteIndexForDetail("OP_PUOJ_NM", jObj.get("OP_SEQ_NO").toString());
                }
                jObj.put("OP_DTL_SEQ_NO", jObj.getInt("OP_SEQ_NO"));
                returnValue = iPhotoDao.delete(jObj);
                return returnValue;
	}
        // endregion 刪除 Model END
	
}
