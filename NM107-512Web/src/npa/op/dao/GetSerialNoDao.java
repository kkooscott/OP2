package npa.op.dao;

//import npa.op.util.DBUtil;
import com.syscom.db.DBUtil;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.rowset.CachedRowSet;
import npa.op.base.DBProcessDao;
import npa.op.util.StringUtil;

public class GetSerialNoDao extends DBProcessDao {

	private static GetSerialNoDao instance = null;
    private static Logger log = Logger.getLogger(GetSerialNoDao.class);

    private GetSerialNoDao() {
    }

    public static GetSerialNoDao getInstance() {
	if (instance == null) {
	    instance = new GetSerialNoDao();
	}
	return instance;
    }

    public enum SerialCycle {
	Day,
	Month,
	Year,
	Infinite
    }

    /**
     * 取的一個BASE新序號
     * @param inputAcYYYmm 該序號所屬年月
     * @param inputAcBUnitCd 該序號所屬單位
     * @param inputAcBUnitNm 該序號所屬單位
     * @param inputDocTypeCd 該序號所屬類類別
     * @param increaseNum 序號增加值
     * @return 新的序號
     * @throws SQLException 
     */
    public int getSerialNoNormal(String inputAcYYYmm, String inputAcBUnitCd, String inputAcBUnitNm, String inputDocTypeCd, int increaseNum) {
		String sql = "";
		int returnValue = 0;
		try {			
                        //Candy modify 為了盡量使用sybase jdbc driver, 所以程式盡量寫成jtds與jdbc都能用
			//sql = "? = exec sp_getSerialNumber(?, ?)";			
                        sql = "? = Call sp_getAnNo(?, ?, ?, ?, ?)";			
			
			DBUtil DBUtil = new DBUtil();
			returnValue = DBUtil.pexecuteSP(sql,new Object[]{inputAcYYYmm,inputAcBUnitCd,inputAcBUnitNm,inputDocTypeCd,increaseNum});

		} catch (Exception e) {
			log.error("getSerialNoNormal：" + e.getMessage());
		}
		return returnValue;
    }

    /**
     * 取的一個新序號
     * @param inputAcYYYmm 該序號所屬年度
     * @param inputAcBUnitCd 該序號所屬單位
     * @param inputAcBUnitNm 該序號所屬單位
     * @param inputDocTypeCd 該序號所屬類類別
     * @return 新的序號
     */
    public int getSerialNo(String inputAcYYYmm, String inputAcBUnitCd, String inputAcBUnitNm, String inputDocTypeCd) {
    	return getSerialNoNormal(inputAcYYYmm,inputAcBUnitCd,inputAcBUnitNm,inputDocTypeCd,1);
    }

    /**
     * 查詢該序號資料最大值
     * @param inputAcYYYmm 該序號所屬年度
     * @param inputAcBUnitCd 該序號所屬單位
     * @param inputDocTypeCd 該序號所屬類類別
     * @param conn 傳入資料庫連線
     * @return
     */
    public int querySerialNo(String inputAcYYYmm, String inputAcBUnitCd, String inputDocTypeCd) {
        int OP_MAX_PRMNO = 0;
            
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();

        sql.append(" SELECT")
           .append(" OP_MAX_PRMNO")
           .append(" FROM")
           .append(" OPDT_GET_AN_DOCNO")
           .append(" WHERE 1=1 ")
           .append(" AND OP_AC_YYYMM = ? ")
           .append(" AND OP_AC_B_UNIT_CD = ? ")
           .append(" AND OP_DOC_TYPE_CD = ? ");
        
        qsPara.add(inputAcYYYmm);
        qsPara.add(inputAcBUnitCd);
        qsPara.add(inputDocTypeCd);

        try{
            CachedRowSet crs = this.pexecuteQueryRowSet(sql.toString(),qsPara.toArray());

            if (crs.next()) {
                OP_MAX_PRMNO = crs.getInt("OP_MAX_PRMNO");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return OP_MAX_PRMNO;
    }
	
}
