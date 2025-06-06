package npa.op.vo;

public class User {
	private String userId;
	private String unitCd1;
	private String unitCd2;
	private String unitCd3;
	private String unitCd1Name;
	private String unitCd2Name;
	private String unitCd3Name;
	private String unitTel;
//	private String password;
	private String userName;
	private String lastLogin;
	private String userState;
	private String idNo;
	private String email;
	private String loginTime;
	private String userTitle;

	/**
	 * 使用者的權限 1：只能看自己 2：能看自己和下屬 3：三層全可被控制
	 */
	private String rolePermission;

	/**
	 * 使用者的IP
	 */
	private String userIp;
	/**
	 * 使用者最後一階的單位代碼
	 */
	private String unitCd;
	/**
	 * 使用者最後一階的單位名稱
	 */
	private String unitName;
	/**
	 * 所擁有的權限
	 */
	private String[] ownFun;
	/**
	 * 所擁有的角色
	 */
	private String[] ownRole;

	/**
	 * E0DT_NPAUNIT的E0_UNIT_LEVEL單位層級
	 */
	private String unitLevel = "";
	
	/**
	 * 使用者的權限 1：只能看自己 2：能看自己和下屬 3：三層全可被控制
	 */
	private boolean isAllowUse;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setUserTitle(String Title) {
		this.userTitle = Title;
	}
	public String getUserTitle() {
		return userTitle;
	}
	
	public String getUnitCd1() {
		return unitCd1;
	}

	public void setUnitCd1(String unitCd1) {
		this.unitCd1 = unitCd1;
	}

	public String getUnitCd2() {
		return unitCd2;
	}

	public void setUnitCd2(String unitCd2) {
		this.unitCd2 = unitCd2;
	}

	public String getUnitCd3() {
		return unitCd3;
	}

	public void setUnitCd3(String unitCd3) {
		this.unitCd3 = unitCd3;
	}

//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUnitCd1Name() {
		return unitCd1Name;
	}

	public void setUnitCd1Name(String unitCd1Name) {
		this.unitCd1Name = unitCd1Name;
	}

	public String getUnitCd2Name() {
		return unitCd2Name;
	}

	public void setUnitCd2Name(String unitCd2Name) {
		this.unitCd2Name = unitCd2Name;
	}

	public String getUnitCd3Name() {
		return unitCd3Name;
	}

	public void setUnitCd3Name(String unitCd3Name) {
		this.unitCd3Name = unitCd3Name;
	}
	
	public String getUnitTel() {
		return unitTel;
	}

	public void setUnitTel(String unitTel) {
		this.unitTel = unitTel;
	}

	public String[] getOwnFun() {
		return ownFun;
	}

	public void setOwnFun(String[] ownFun) {
		this.ownFun = ownFun;
	}

	public String[] getOwnRole() {
		return ownRole;
	}

	public void setOwnRole(String[] ownRole) {
		this.ownRole = ownRole;
	}

	public String getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(String rolePermission) {
		this.rolePermission = rolePermission;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	/**
	 * @return the unitCd
	 */
	public String getUnitCd() {
		return unitCd;
	}

	/**
	 * @param unitCd
	 *            the unitCd to set
	 */
	public void setUnitCd(String unitCd) {
		this.unitCd = unitCd;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName
	 *            the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getOwnRoleString() {
		String s = "";
		for (String e : ownRole) {
			s += "," + e;
		}
		s = s.substring(1);
		return s;
	}

	/**
	 * E0DT_NPAUNIT的E0_UNIT_LEVEL單位層級
	 * 
	 * @return the unitLevel
	 */
	public String getUnitLevel() {
		return unitLevel;
	}

	/**
	 * E0DT_NPAUNIT的E0_UNIT_LEVEL單位層級
	 * 
	 * @param unitLevel
	 *            the unitLevel to set
	 */
	public void setUnitLevel(String unitLevel) {
		this.unitLevel = unitLevel;
	}

	/**
	 * @return the loginTime
	 */
	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	
	public void setIsAllowUse(boolean isAllowUse) {
		this.isAllowUse = isAllowUse;
	}

	public boolean getIsAllowUse() {
		return isAllowUse;
	}
}

