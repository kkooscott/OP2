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

public class GetAcRcnoDao extends DBProcessDao {

	private static GetAcRcnoDao instance = null;
    private static Logger log = Logger.getLogger(GetAcRcnoDao.class);

    private GetAcRcnoDao() {
    }

    public static GetAcRcnoDao getInstance() {
	if (instance == null) {
	    instance = new GetAcRcnoDao();
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
     * 取得新的收據編號
     * @param inputAcYYYmm 該序號所屬年月
     * @param inputAcUnitCd 該序號所屬單位
     * @param inputAcUnitNm 該序號所屬單位
     * @param increaseNum 序號增加值 1
     * @return 新的序號
     * @throws SQLException 
     */
    public int getAcRcNoNormal(String inputAcYYYmm, String inputAcUnitCd, String inputAcUnitNm, int increaseNum) {
		String sql = "";
		int returnValue = 0;
		try {			
                        //Candy modify 為了盡量使用sybase jdbc driver, 所以程式盡量寫成jtds與jdbc都能用
			//sql = "? = exec sp_getSerialNumber(?, ?)";			
                        sql = "? = Call sp_getAcRcNo(?, ?, ?, ?)";			
			
			DBUtil DBUtil = new DBUtil();
			returnValue = DBUtil.pexecuteSP(sql,new Object[]{inputAcYYYmm,inputAcUnitCd,inputAcUnitNm,increaseNum});

		} catch (Exception e) {
			log.error("getAcRcNoNormal：" + e.getMessage());
		}
		return returnValue;
    }

    /**
     * 取得新的收據編號
     * @param inputAcYYYmm 該序號所屬年度
     * @param inputAcUnitCd 該序號所屬單位
     * @param inputAcUnitNm 該序號所屬單位
     * @return 新的序號
     */
    public int getAcRcNo(String inputAcYYYmm, String inputAcUnitCd, String inputAcUnitNm) {
    	return getAcRcNoNormal(inputAcYYYmm,inputAcUnitCd,inputAcUnitNm,1);
    }
}
