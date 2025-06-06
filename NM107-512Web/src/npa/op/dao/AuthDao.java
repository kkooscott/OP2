package npa.op.dao;

import npa.op.vo.User;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import npa.op.util.StringUtil;
import npa.op.base.DBProcessDao;

public class AuthDao extends DBProcessDao {
	private static AuthDao instance = null;
	private static Logger log = Logger.getLogger(AuthDao.class);

	private AuthDao() {
	}

	public static AuthDao getInstance() {
		if (instance == null) {
			instance = new AuthDao();
		}
		return instance;
	}

	/**
	 * 檢查登入帳密是否正確以及相關檢查
	 * 
	 * @param id
	 * @param pwd
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String chkUser(String id, String pwd, String ip, HttpSession session) {
		String msg = "";

		return msg;
	}
	
	/**
	 * 設定User的權限層級
	 * 
	 * @param user UserVO
	 */
	public void setUserRolePermission(User user) {
		// 查取出三階單位DB
		String sql = "SELECT MAX(OP_PERMISSION) AS OP_PERMISSION FROM OPDT_ROLE WHERE OP_ENABLED='Y' AND "
						+ "OP_ROLE_ID IN ('" + user.getOwnRoleString().replaceAll(",", "','") + "')";
		
		String rolePermission = getData(sql,"OP_PERMISSION");
		
		user.setRolePermission(rolePermission);

	}

	/**
	 * 設定User所有角色所能使用的功能清單(OwnFun)，但是必須先設定OwnRole
	 * 
	 * @param user
	 */
	public void setUserFuncList(User user) {
		String[] roles = user.getOwnRole();
		// 列出該角色所能使用的功能清單
		String roleSql = "";

		if (roles.length > 0) {
			int idx = 0;
			for (String role : roles) {
				log.debug("role=" + role);
				idx++;
				if (idx == 1) {
					roleSql += "(OP_ROLE_ID = '" + role + "'";
				} else {
					roleSql += " OR OP_ROLE_ID = '" + role + "'";
				}
			}
			roleSql += ")";
		}
		log.debug("roleSql=" + roleSql);
		String sql = "SELECT DISTINCT(FUNC_ROLE.OP_FUNC_ID) FROM OPDT_FUNC_ROLE FUNC_ROLE "
				+ "INNER JOIN OPDT_FUNC FUNC ON FUNC_ROLE.OP_FUNC_ID = FUNC.OP_FUNC_ID AND FUNC.OP_ENABLED='Y' "
				+ "WHERE " + roleSql + " AND FUNC_ROLE.OP_ENABLED='Y'";
		ArrayList<HashMap> list = executeQuery(sql);
		String func = "";
		if (list.size() > 0) {
			String[] funcs = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				func = StringUtil.nvl(list.get(i).get("OP_FUNC_ID"));
				funcs[i] = func;

			}
			user.setOwnFun(funcs);
		}
	}

	/**
	 * 設定User的三層警局單位(UnitCd1,UnitCd2,UnitCd3)與單位層級(unitLevel)與單位旗標(unitFlag)，
	 * 但是必須先設定UnitCd
	 * 
	 * @param user
	 * @param unitCd
	 *            使用者所隸屬的單位
	 */
	public void setUser3LevelUnit(User user) {
		// 查取出三階單位DB
		String unitCd = user.getUnitCd();
		String sql = "SELECT OP_UNIT_S_NM, OP_UNIT_LEVEL, OP_DEPT_CD, OP_BRANCH_CD, OP_PHONE_NO  FROM OPDT_E0_NPAUNIT WHERE OP_UNIT_CD= ?";
		HashMap row = pgetFirstRow(sql, new Object[] { unitCd });
		String unitCd1 = "";
		String unitNm1 = "";
		String unitCd2 = "";
		String unitNm2 = "";
		String unitCd3 = "";
		String unitNm3 = "";
		String unitLevel = "";
		String unitTel = "";
		ArrayList<HashMap> tempData = new ArrayList<HashMap>();
		if (row != null) {
			String depCd = StringUtil.nvl(row.get("OP_DEPT_CD"));
			String branchCd = StringUtil.nvl(row.get("OP_BRANCH_CD"));
			unitLevel = StringUtil.nvl(row.get("OP_UNIT_LEVEL"));
			if (depCd.equalsIgnoreCase(unitCd)) {// 表示只有第一階
				unitCd1 = unitCd;
				tempData = pexecuteQuery(sql, new Object[] { unitCd });
				unitNm1 = tempData.get(0).get("OP_UNIT_S_NM").toString();
				unitTel = tempData.get(0).get("OP_PHONE_NO").toString();
			} else if (branchCd.length() == 0) {// 表示只有第二階
				unitCd1 = depCd;
				tempData = pexecuteQuery(sql, new Object[] { depCd });
				unitNm1 = tempData.get(0).get("OP_UNIT_S_NM").toString();
				unitTel = tempData.get(0).get("OP_PHONE_NO").toString();
				unitCd2 = unitCd;
				unitNm2 = pgetData(sql, "OP_UNIT_S_NM", new Object[] { unitCd });
			} else {// 三階都有
				unitCd1 = depCd;
				tempData = pexecuteQuery(sql, new Object[] { depCd });
				unitNm1 = tempData.get(0).get("OP_UNIT_S_NM").toString();
				unitTel = tempData.get(0).get("OP_PHONE_NO").toString();
				unitCd2 = branchCd;
				unitNm2 = pgetData(sql, "OP_UNIT_S_NM", new Object[] { branchCd });
				unitCd3 = unitCd;
				unitNm3 = pgetData(sql, "OP_UNIT_S_NM", new Object[] { unitCd });
			}
		}
		user.setUnitCd1(unitCd1);
		user.setUnitCd2(unitCd2);
		user.setUnitCd3(unitCd3);
		user.setUnitCd1Name(unitNm1);
		user.setUnitCd2Name(unitNm2);
		user.setUnitCd3Name(unitNm3);
		user.setUnitLevel(unitLevel);
		user.setUnitTel(unitTel);
	}

	/**
	 * 設定User的是否有入山系統使用權限
	 * 
	 * @param User UserVO
	 */
	public void setIsAllowUse(User user) {
		// 查取出三階單位DB
		String sql = "SELECT OP_IS_SHOW FROM OPDT_E0_NPAUNIT WHERE OP_UNIT_CD=? ";
		
		String OP_IS_SHOW = pgetData(sql,"OP_IS_SHOW",new Object[]{user.getUnitCd()});
		
		user.setIsAllowUse("1".equals(OP_IS_SHOW) ? true : false);

	}
}

