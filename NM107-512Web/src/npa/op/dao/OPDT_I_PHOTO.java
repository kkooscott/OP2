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

public class OPDT_I_PHOTO extends BaseDao {
	
	private static OPDT_I_PHOTO instance = null;
	public static OPDT_I_PHOTO getInstance() {
		if (instance == null) {
			instance = new OPDT_I_PHOTO();
		}
		return instance;
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

                try {
                        User voUser = new User();
                        OPUtil opUtil = new OPUtil();
                        voUser = (User) jObj.get("userVO");
                        jObj.put("OP_BASIC_SEQ_NO", Integer.valueOf((String) jObj.get("OP_BASIC_SEQ_NO")));
                        jObj.put("OP_AC_RCNO", jObj.get("OP_AC_RCNO"));
                        jObj.put("OP_DTL_SEQ_NO", Integer.valueOf((String) jObj.get("OP_DTL_SEQ_NO")));
                        jObj.put("OP_AN_SEQ_NO", Integer.valueOf((String) jObj.get("OP_AN_SEQ_NO")));
                        jObj.put("OP_PIC_TYPE", jObj.get("OP_PIC_TYPE"));
                        jObj.put("OP_UPL_FUNC_CD", jObj.get("OP_UPL_FUNC_CD"));
                        jObj.put("OP_UPL_FUNC_NM", jObj.get("OP_UPL_FUNC_NM"));
                        jObj.put("OP_FILE_PATH", jObj.get("OP_FILE_PATH"));
                        jObj.put("OP_ORG_FILE_NAME", jObj.get("OP_ORG_FILE_NAME"));
                        jObj.put("OP_UPL_FILE_NAME", jObj.get("OP_UPL_FILE_NAME"));
                        
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
   

                        DaoUtil util = new DaoUtil();
                        jObj = util.getStaticColumn(jObj, "ADD");
                        jObj = util.getStaticColumn(jObj, "UPD");

                        String arryString[] = {//"OP_SEQ_NO",
                                        "OP_BASIC_SEQ_NO",
                                        "OP_AC_RCNO",
                                        "OP_DTL_SEQ_NO",
                                        "OP_AN_SEQ_NO", //5
                                        "OP_PIC_TYPE",
                                        "OP_UPL_FUNC_CD",
                                        "OP_UPL_FUNC_NM",
                                        "OP_FILE_PATH",
                                        "OP_ORG_FILE_NAME", //10
                                        "OP_UPL_FILE_NAME",
                                        "OP_ADD_ID",
                                        "OP_ADD_NM",
                                        "OP_ADD_UNIT_CD",
                                        "OP_ADD_UNIT_NM", //15
                                        "OP_ADD_DT_TM",
                                        "OP_UPD_ID",
                                        "OP_UPD_NM",
                                        "OP_UPD_UNIT_CD",
                                        "OP_UPD_UNIT_NM", //20
                                        "OP_UPD_DT_TM"}; //21

                        sql.append("INSERT INTO OPDT_I_PHOTO ( ");
                        sql.append(Arrays.toString(arryString).substring(1, Arrays.toString(arryString).length()-1));
                        sql.append(" )")
                        .append(" VALUES  (")
                        .append(" ?,?,?,?,?,?,?,?,?,?,") //10
                        .append(" ?,?,?,?,?,?,?,?,?,?)"); //20

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
                        System.out.println(insetkey);
                        if ( insetkey > 0 ){
                                luence lue = new luence();
                                jObj.put("OP_SEQ_NO", insetkey);
                        }else{
                                returnValue = false;
                        }

                } catch (Exception e) {
                        log.error(ExceptionUtil.toString(e));
                }
                return returnValue;
        }
        // endregion 新增 model END
        public JSONArray getPicture(JSONObject obj) {
            ArrayList args = new ArrayList();
            String sql = "SELECT OP_SEQ_NO, OP_FILE_PATH, OP_ORG_FILE_NAME, OP_UPL_FILE_NAME"
                    + " FROM OPDT_I_PHOTO"
                    + " WHERE 1 = 1";
            if( obj.has("OP_DTL_SEQ_NO") ){
                sql += " AND OP_DTL_SEQ_NO = ?";
                args.add(obj.getInt("OP_DTL_SEQ_NO"));
            }
            if( obj.has("OP_AN_SEQ_NO") ){
                sql += " AND OP_AN_SEQ_NO = ?";
                args.add(obj.getInt("OP_AN_SEQ_NO"));
            }
            if( obj.has("OP_BASIC_SEQ_NO") ){
                sql += " AND OP_BASIC_SEQ_NO = ?";
                args.add(obj.getInt("OP_BASIC_SEQ_NO"));
            }
            if( obj.has("OP_AC_RCNO") ){
                sql += " AND OP_AC_RCNO = ?";
                args.add(obj.getString("OP_AC_RCNO"));
            }
            if( obj.has("OP_PIC_TYPE") ){
                sql += " AND OP_PIC_TYPE = ?";
                args.add(obj.getString("OP_PIC_TYPE"));
            }
            
            ArrayList<HashMap> list = null;
            try {
                list = pexecuteQuery(sql, args.toArray());
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }
            return arrayList2JsonArray(list);
        }
        
        // region 刪除 Model Start
        /**
         * 刪除遺失物受理明細照片資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean delete(JSONObject jObj) {
                boolean returnValue = false;
                int resultCount = 0;
		String sqlDelete = "DELETE FROM OPDT_I_PHOTO WHERE OP_DTL_SEQ_NO=? " ;
		resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{jObj.getInt("OP_DTL_SEQ_NO")});
                returnValue = true;
                return returnValue;
	}
        // endregion 刪除 Model END
        
        // region 刪除 Model Start
        /**
         * 刪除遺失物受理明細照片資料.
         * 
         * @param jObj 審核資料欄位
         * @return boolean.
         */
        public boolean deleteByOpSeqNo(int OP_SEQ_NO) {
                boolean returnValue = false;
                int resultCount = 0;
		String sqlDelete = "DELETE FROM OPDT_I_PHOTO WHERE OP_SEQ_NO=? " ;
		resultCount = this.pexecuteUpdate(sqlDelete, new Object[]{OP_SEQ_NO});
                returnValue = true;
                return returnValue;
	}
        // endregion 刪除 Model END
        
        //region 明細
	/**
	 * 查詢遺失物受理明細照片 .
	 * 
	 * @param OP_SEQ_NO
	 * @return JSONArray
	 * @throws SQLException 
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public JSONArray queryPictureById(int OP_SEQ_NO) throws SQLException, IOException, WriterException {
		ArrayList args = new ArrayList();

		String sql = " SELECT *"
                           + " FROM OPDT_I_PHOTO"
                           + " WHERE OP_SEQ_NO = ? ";
                args.add(OP_SEQ_NO);			
		ArrayList<HashMap> list = this.pexecuteQuery(sql,args.toArray());

		return arrayList2JsonArray(list);
	}
	
}
