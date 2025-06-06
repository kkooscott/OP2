<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="npa.op.util.ExceptionUtil"%>
<%@ page import="npa.op.dao.AuthDao"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.BufferedInputStream"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="javax.servlet.http.Cookie"%>
<%@ page import="org.apache.commons.httpclient.*"%>
<%@ page import="org.apache.commons.httpclient.methods.*"%>
<%@ page import="org.apache.commons.httpclient.params.*"%>
<%@ page import="npa.op.util.StringUtil"%>
<%@ page import="npa.op.vo.User"%>
<%@ page import="org.apache.axis2.transport.http.HttpTransportProperties"%>
<%@ page import="npa.ldap.axis2.LdapWebServices"%>
<%@ page import="npa.ldap.axis2.LdapWebServicesStub"%>
<%@ page import="npa.ldap.ws.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.log4j.Logger"%>

<%!private String convertString(String ss) {
		try {
			return new String(ss.getBytes("8859_1"), "UTF8");
		} catch (Exception e) {
		}
		return ss;
	}%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	//**********************************************************************************
	// This page will be accessed when the user successfully logged in to system
	// The identity server policy agent on this web server will query user's profile
	// and append his properties in HTTP HEADER.
	// The following code demonstrate how to retrieve the properties from HTTP HEADER
	// There are three HTTP HEADERs contains Masterlink Specific properties:
	//    HEADER NAME      LDAP ATTRIBUTE               DESC
	//    (Header-Name)         (Ldap-Attr)
	//    ------------------------------------------------------------------------------
	//    ldap-dn          entrydn                      User entry full DIT path
	//    personal-id	uid		使用者代碼   LOGUID
	//    chn-name	cn		中文姓名     LOGCN
	//    eng-name	npa_ecn		英文姓名
	//    pid		npa_pid		身分證字號   LOGPID
	//    sex		npa_sex		性別
	//    birthday	npa_birthday	出生年月日
	//    title		npa_title		職稱
	//    status	inetUserStatus	使用者狀態
	//    weave-org	npa_weave_id	編製單位
	//    org-id	npa_org_id	所屬單位    LOGDID
	//    update-time	npa_update_time	資料更新日期
	//    notes-mail	npa_mail_id	Notes 郵件代碼
	//    e-mail	mail		電子郵件位址
	//    cert-serial	npa_cert_serial	自然人憑證序號
	//    mobile	npa_mobile	手機
	//    ap-list	npa_ap_list	可進入的系統清單
	// Please replace "Header-Name" and "Ldap-Attr" in the following codes :
	// by the one you need, such as "personal-id" "uid".
	//**********************************************************************************
	Logger log = Logger.getLogger(getClass());
	Enumeration e;
	//String Ldap-Attr= "";
	String entrydn = "";
	String uid = "";
	String cn = "";
	String title="";
	String npa_ecn = "";
	String npa_pid = "";
	String npa_sex = "";
	String npa_birthday = "";
	String npa_title = "";
	String inetUserStatus = "";
	String npa_weave_id = "";
	String npa_org_id = "";
	String npa_org_nm = "";
	String npa_update_time = "";
	String npa_mail_id = "";
	String mail = "";
	String npa_cert_serial = "";
	String npa_mobile = "";
	String npa_ap_list = "";
	String userIP = "";
	String novellIp = "";
	String tdt = "";
	String[] role = new String[4];
	String tel = "";
	String role_permission = "";
	boolean isAllowUse = true;

	String urlTest = request.getRequestURL().toString();
//        System.out.println(request.getSession().getId());
        request.changeSessionId();
//        System.out.println(request.getSession().getId());
	//out.println("<script>alert('urlTest:' + urlTest)</script>");
	log.debug(urlTest);
	//System.out.println("urlTest:"+urlTest);

	User user = new User();
	if (!urlTest.contains("localhost") && !urlTest.contains("10.1.") && !urlTest.contains("192.168.") && !urlTest.contains("127.0.0.1")) {
		//out.println("<script>alert('進入8085')</script>");
		e = request.getHeaderNames();
		log.debug("SSO Header的清單--");
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			log.debug(key + "--"
					+ StringUtil.nvl(validator.Validator.getHeader(request, key)));
			//key = key.toUpperCase();
			if (key.equalsIgnoreCase("ldap-dn"))
				entrydn = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("LOGUID"))//2010 新單一簽入 personal-id
				uid = StringUtil.replaceWhiteChar(StringUtil
						.nvl(validator.Validator.getHeader(request, key)));
			else if (key.equalsIgnoreCase("LOGCN"))//2010 新單一簽入 chn-name
				cn = StringUtil.replaceWhiteChar(StringUtil
						.nvl(convertString(validator.Validator.getHeader(request, key))));
			else if (key.equalsIgnoreCase("LOGPID"))//2010 新單一簽入 pid
				npa_pid = StringUtil.replaceWhiteChar(StringUtil
						.nvl(validator.Validator.getHeader(request, key)));
			else if (key.equalsIgnoreCase("LOGDID"))//2010 新單一簽入 org-id
				npa_org_id = StringUtil.replaceWhiteChar(StringUtil
						.nvl(validator.Validator.getHeader(request, key)));
			else if (key.equalsIgnoreCase("LOGDIDCN"))//2010 新單一簽入 org-id
				npa_org_nm = StringUtil.replaceWhiteChar(StringUtil
						.nvl(convertString(validator.Validator.getHeader(request, key))));
			else if (key.equalsIgnoreCase("eng-name"))
				npa_ecn = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("sex"))
				npa_sex = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("birthday"))
				npa_birthday = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("title"))
				npa_title = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("status"))
				inetUserStatus = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("weave-org"))
				npa_weave_id = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("update-time"))
				npa_update_time = StringUtil
						.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("notes-mail"))
				npa_mail_id = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("e-mail"))
				mail = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("cert-serial"))
				npa_cert_serial = StringUtil
						.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("mobile"))
				npa_mobile = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("ap-list"))
				npa_ap_list = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("LOGUIP"))
				userIP = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("proxy-ip"))
				novellIp = StringUtil.nvl(validator.Validator.getHeader(request, key));
			else if (key.equalsIgnoreCase("LOGTDT"))
				tdt = new SimpleDateFormat("yyyy")
				.format(new Date()).substring(0, 2) + StringUtil.nvl(validator.Validator.getHeader(request, key));
		}
	} else {
		//out.println("<script>alert('進入預設')</script>");
//		uid = "syscom";
//                cn = "開發人員";
//                npa_title = "警務正";
                tdt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
	//	userIP = "10.1.2.3";
                userIP = "999.999.999.999";
		tel = "02-12345678";
                
                /* 派出所人員 */
//                role = new String[]{"OP300001","OP300002"};
//                npa_org_id = "AD6Q1";
//                npa_org_nm = "新北市政府警察局板橋分局板橋派出所";
//                role_permission = "1";
                  uid = "WNI424V2";
                  cn  = "派出所測試員";
                  npa_title = "警務佐";
                  role = new String[]{"OP300001","OP300002"};
                  npa_org_id = "AD6Q1";
		  npa_org_nm = "新北市政府警察局板橋分局板橋派出所";
		  role_permission = "1";


                /* 分局人員 */
//                role = new String[]{"OP300001","OP300002","OP300003"};
//                npa_org_id = "AD641";
//                npa_org_nm = "新北市政府警察局板橋分局偵查隊";
//                role_permission = "2";
                /* 總局人員 */
//                role = new String[]{"OP300001","OP300002","OP300003","OP300004"};
//                npa_org_id = "ADZ00";
//				  npa_org_nm = "新北市政府警察局刑事警察大隊";
//                role = new String[]{"OP300005"};
//			      role_permission = "4";

                //台北市刑事警察大隊
                //role = new String[]{"OP300001","OP300002","OP300003","OP300004"};
                //npa_org_id = "AW000";
		//npa_org_nm = "台北市政府警察局刑事警察大隊";
                //role = new String[]{"OP300005"};
                //role_permission = "4";                

                /* 開發人員 */
              //role = new String[]{"OP300001","OP300002","OP300003","OP300004","OP300005","OP300006"};
              //role = new String[]{"OP300005"};

                //role = new String[]{"OP300007"};
                //role = new String[]{"OP300008"};
                 
//                npa_org_id = "AD6Q1";
//		npa_org_nm = "新北市政府警察局板橋分局板橋派出所";
//		role_permission = "1";

//              npa_org_id = "AD6M1";
//		npa_org_nm = "新北市政府警察局板橋分局信義派出所";
//		role_permission = "1";

//                role = new String[]{"OP300008"};
//                npa_org_id = "A1801";
//		npa_org_nm = "警察署政風室";
//		role_permission = "4";

//              正式在用的警署經辦id與角色
//              uid = "WNI424V2";
//              cn  = "王凱民";
//              npa_title = "資訊工程師";
//              role = new String[]{"OP300006"};
//              npa_org_id = "A1E41";
//		npa_org_nm = "警察署資訊室前膽應用科";
//		role_permission = "4";
                
	}

	//for test

	// 20110803, uid,cn,npa_org_id,userIp 其中有空值則直接踢掉

	// 若可取得登入者姓名, 則設定使用者資料至session中, 並取得角色
	if (uid.length() > 0 && !urlTest.contains("localhost") && !urlTest.contains("10.1.") && !urlTest.contains("192.168.") && !urlTest.contains("127.0.0.1")) {
		//out.println("<script>alert('進入8085 - 2')</script>");
		String addr = request.getRemoteAddr();
		log.debug("LOGUIP(使用者IP) 為 " + userIP);
		log.debug("proxy-ip(NovellIP) 為 " + novellIp);
		log.debug("getRemoteAddr 為 " + addr);
		log.debug("LOGDID(所屬單位) 為 " + npa_org_id);
		log.debug("LOGDIDCN(所屬單位名稱) 為 " + npa_org_nm);
		log.debug("LOGPID(身分證字號) 為 " + npa_pid);
		log.debug("LOGUID(使用者代碼) 為 " + uid);
		log.debug("LOGCN(中文姓名) 為 " + cn);
		log.debug("LOGTDT(登入時間) 為 " + tdt);
		String headerMsg = "(身分證字號:" + npa_pid + " ,帳號：" + uid
				+ " ,姓名：" + cn + ", 單位代碼：" + npa_org_id + " )";

		try {
			/*UtilityLib.setSessionUserCode(uid, request);
			UtilityLib.setSessionUserName(cn, request);
			UtilityLib.setSessionUnitCode(npa_org_id, request);
			UtilityLib.setSessionUnitName(UtilityLib.getUnit(npa_org_id).getE0UnitNm(), request);
			UtilityLib.setSessionLOGTDT(tdt, request);
			UtilityLib.setSessionUserIP(userIP, request);
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			//SID 申請的帳號及密碼
			HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
			auth.setUsername("cn=OP3_SID,ou=APIDs,o=NPA");
			auth.setPassword("OP3_SID");
			auth.setPreemptiveAuthentication(true);

			String url = "http://172.16.6.131:8085/NPALdapws/services/LdapWebServices";
			LdapWebServicesStub stub = new LdapWebServicesStub(url);
			stub._getServiceClient()
					.getOptions()
					.setProperty(
							org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
							auth);
			stub._getServiceClient()
					.getOptions()
					.setProperty(
							org.apache.axis2.transport.http.HTTPConstants.CACHED_HTTP_CLIENT,
							"true");
			LdapWebServices ldap = stub;
                        //取得使用者職稱
                        AttrSet titleAttrSet = ldap.getUserAttributes(uid, new String[]{"cn","fullName","title"});
						System.out.println("titleAttrSet:"+titleAttrSet);
						Attr[] aList = (Attr[]) titleAttrSet.getAttributes().toArray(new Attr[] {});
					System.out.println("aList:"+aList+" size:"+aList.length+" DN 為 " + titleAttrSet.getDn());

					for (int c = 0; c < aList.length; c++) {
						Attr x = aList[c];
						System.out.print("titleAttrSet屬性名稱 : " + x.getName() + "");
						if (x.getValues() != null) {
							List vAry = x.getValues();

							for (int i = 0; i < vAry.size(); i++) {
								Object v = vAry.get(i);
								System.out.println("\t值:" + v + "");
								if ("title".equalsIgnoreCase(x.getName())) {
									npa_title = StringUtil.nvl(vAry.get(i));
									System.out.println("npa_title value" + i + ":" + StringUtil.nvl(npa_title));
								}
							}

						}
					}
						//System.out.println("titleAttr:"+titleAttr+" getName:"+titleAttr.getName());
                        //List vtitleAry = titleAttr.getValues();
						//System.out.println("vtitleAry:"+vtitleAry+" size:"+(vtitleAry==null? "null":vtitleAry.size()));
						//if(vtitleAry != null && vtitleAry.size() > 0){
						//	npa_title=StringUtil.nvl(vtitleAry.get(0));
						//}
                        
                        //取得使用者角色
			AttrSet[] s = ldap.getUserRolesByAPP(uid, "OP3");
                        log.debug("getUserRoles s:" + s + " size:" + s.length);
			if (s != null && s.length > 0) {
                            
				String[] roles = new String[s.length];//該帳號所擁有的權限
				for (int idx = 0; idx < s.length; idx++) {
					AttrSet ss = s[idx];

					Attr[] a = (Attr[]) ss.getAttributes().toArray(new Attr[] {});
					log.debug("LDAP DN 為 " + ss.getDn());

					for (int c = 0; c < a.length; c++) {
						Attr x = a[c];
						log.debug("屬性名稱 : " + x.getName() + "");
						if (x.getValues() != null) {
							List vAry = x.getValues();

							for (int i = 0; i < vAry.size(); i++) {
								Object v = vAry.get(i);
								log.debug("\t值:" + v + "");
								if ("cn".equalsIgnoreCase(x.getName())) {
									roles[idx] = StringUtil.nvl(vAry.get(i));
									log.debug("role" + idx + ":" + StringUtil.nvl(roles[idx]));
								}
							}

						}
					}
				}

				//設定User
				user.setUserIp(userIP);
				user.setUserId(uid);
				user.setUserName(cn);
				user.setIdNo(npa_pid);//身分證
				/*user.setUnitCd1("A1000");
				user.setUnitCd1Name("警政署");
				user.setUnitCd2("A1101");
				user.setUnitCd2Name("警政署勤指");
				user.setUnitCd3("A1111");
				user.setUnitCd3Name("警政署勤指指揮管制科");*/
				user.setUnitCd(npa_org_id);//使用者的單位代碼
				user.setUnitName(npa_org_nm);
				user.setOwnRole(roles);
				user.setLoginTime(tdt);
				user.setUserTitle(npa_title);
				//
				AuthDao dao = AuthDao.getInstance();
				dao.setUser3LevelUnit(user);
				dao.setUserFuncList(user);
				dao.setUserRolePermission(user);
				dao.setIsAllowUse(user);
                                
//                                session.setAttribute("user", validator.Validator.escapeHtml(user));
                                validator.Validator.escapeSes(session, "user", user);
				//log.debug("scopeUnitSql = "+user.getScopeUnitSql());
				//response.sendRedirect("index.jsp");
			} else {
				log.debug("ldap.getUserRolesByAPP(LOGUID, \"CP\") 為空值,無使用權限! ");
				out.println("<script language=\"javascript\"> alert('無使用權限，請洽系統管理者！');window.open('https://idp.eportal.npa.gov.tw:8443/nidp/idff/sso', '_top');</script>");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			out.println("<script language=\"javascript\"> alert('無法取得單一簽入角色，請洽系統管理者！  主機:"
					+ System.getenv("COMPUTERNAME")
					+ "  "
					+ ExceptionUtil.toString(ex)
					+ "');window.open('https://idp.eportal.npa.gov.tw:8443/nidp/idff/sso', '_top');</script>");
		}
	} else {
		//out.println("<script>alert('進入預設 - 2')</script>"); 
		//設定User
		user.setUserIp(userIP);
		user.setUserId(uid);
		user.setUserName(cn);
		user.setUserTitle(npa_title);
		user.setIdNo(npa_pid);//身分證
                
//		user.setUnitCd1("A1000");
//		user.setUnitCd1Name("警政署");
//		user.setUnitCd2("A1E01");
//		user.setUnitCd2Name("警政署資訊室");
//		user.setUnitCd3("A1E41");
//		user.setUnitCd3Name("警政署資訊室資料作業科(開發用)");
                
                //台北市政府刑事警察大隊
//		user.setUnitCd1("AW000");
//		user.setUnitCd1Name("台北市政府警察局");
//		user.setUnitCd2("AWZ00");
//		user.setUnitCd2Name("刑事警察大隊");
//		user.setUnitCd3("");
//		user.setUnitCd3Name("");
                
		//user.setUnitCd3("A1E31");
		//user.setUnitCd3Name("警政署資訊室資料作業設計科(開發用)");
                
//              user.setUnitCd1("AW000");
//		user.setUnitCd1Name("臺北市政府警察局");
//		user.setUnitCd2("AW200");
//		user.setUnitCd2Name("大安分局");
//		user.setUnitCd3("AW2O1");
//		user.setUnitCd3Name("敦化南路派出所");
//                user.setUnitCd1("BM000");
//		user.setUnitCd1Name("嘉義市政府警察局");
//		user.setUnitCd2("BM100");
//		user.setUnitCd2Name("第一分局");
//		user.setUnitCd3("BM1M1");
//		user.setUnitCd3Name("長榮派出所");

                user.setUnitCd1("AD000");
		user.setUnitCd1Name("新北市政府警察局");
		user.setUnitCd2("AD600");
		user.setUnitCd2Name("板橋分局");
                user.setUnitCd3("AD6Q1");
	        user.setUnitCd3Name("板橋派出所");
//		user.setUnitCd3("AD6M1");
//		user.setUnitCd3Name("信義派出所");                

                
//              user.setUnitCd1("A1000");
//		user.setUnitCd1Name("警政署");
//		user.setUnitCd2("A1801");
//		user.setUnitCd2Name("警察署政風室");                                npa_org_id = "A1801";

                
		user.setUnitCd(npa_org_id);//使用者的單位代碼
		user.setUnitName(npa_org_nm);
		user.setOwnRole(role);
		user.setLoginTime(tdt);
		user.setUnitTel(tel);
		user.setRolePermission(role_permission);
		user.setIsAllowUse(isAllowUse);

//		session.setAttribute("user", validator.Validator.escapeHtml(user));
                validator.Validator.escapeSes(session, "user", user);
	}
%>