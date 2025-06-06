<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="npa.op.util.NPAUtil,npa.op.vo.User"%>
<%@ page import="java.net.*" %>
<%@ page import="npa.op.util.NpaConfig" %>
<%
    User user = NPAUtil.getUser(session);
%>
<!-- BEGIN HEADER -->
<title>拾得遺失物處理系統</title>
<!--<link rel="icon" href="favicon.ico" type="image/x-icon" />-->
<div class="page-header navbar navbar-fixed-top mega-menu">
    <!-- BEGIN TOP NAVIGATION BAR -->
    <div class="page-header-inner">
        <!-- BEGIN LOGO -->
        <img src="assets/img/sysname.png" align="absmiddle" style="position: absolute; top: 34px; left: 220px;"/>
        <!-- END LOGO -->
        <!-- BEGIN USER INFO -->
        <div class="user-nav pull-right">
            <ul id="mega-menu-user" class="mega-menu">
                <li id="menuUser" class="dropdown dropdown-user"><a href="#">
                        <img alt="" src="assets/img/user2.png"></a>
                    <ul class="dropdown-menu">
                        <li style="padding: 4px 4px;">姓名： <%=user.getUserName()%></li>
                        <li style="padding: 4px 4px;">帳號： <%=user.getUserId()%></li>
                        <li style="padding: 4px 4px;">單位：<%=user.getUnitName()%></li>
                        <li style="padding: 4px 4px;">角色：<%=user.getRolePermission()%></li>
                        <li style="padding: 4px 4px;">主機：<%=System.getenv("COMPUTERNAME")%></li>
                        <li class="divider"></li>
                        <li><a href="#"
                               onclick=" self.close(); window.close();top.window.close();return false;"><i
                                    class="fa fa-sign-out"></i>登出</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- END USER INFO -->
        <div class="blue hor-menu">
            <ul id="mega-menu-4" class="mega-menu">
                <%
                    String[] roles = user.getOwnRole();
                    StringBuilder arryRole = new StringBuilder();

                    for (String strRole : roles) {
                        arryRole.append(",'" + strRole + "'");
                        //System.out.println("AAAA:"+arryRole.toString().indexOf("OP300002"));
                    }
                    //System.out.println("AAAA:"+arryRole.toString().indexOf("OP300002"));
                %>
                <%
                    //menu01的權限
                    String[] menu01Role = new String[]{"OP300001", "OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    Boolean flag = false;
                    for (int i = 0; i < menu01Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu01Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu01">
                    <a class="dc-mega" href="OP01A01Q_01.jsp" target="mainFrame"  onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                        &nbsp;&nbsp;&nbsp;&nbsp;民眾網路認領訊息<i style="visibility: hidden" class="fa fa-angle-down"></i>
                        <span id="menu01span"></span>
                    </a>
                </li>
                <% } %>            
                <%
                    //menu02的權限
                    String[] menu02Role = new String[]{"OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu02Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu02Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu02">
                    <a href="#">
                        拾得遺失物受理<i class="fa fa-angle-down"></i><span id="menu02span"></span>
                    </a>
                    <ul>
                        <li id="menu0201" style="display: none"><a href="OP02A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物受理資料新增</a></li>
                        <li id="menu0202" style="display: none"><a href="OP02A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物受理資料維護</a></li>
                        <li id="menu0203" style="display: none"><a href="OP02A03Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物受理資料刪除</a></li>
                        <!--20230515  拾得物刪除案件查詢 自「拾得遺失物受理」移到「綜合查詢」 下面
                        <li id="menu0204" style="display: none"><a href="OP02A04Q_01.jsp" target="mainFrame" 
                                onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物刪除案件查詢</a></li>
                        -->
                        <li id="menu0205" style="display: none"><a href="OP02A05Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物認領資料維護</a></li>
                    </ul>
                </li>
                <% } %>
                <%
                    //menu03的權限
                    String[] menu03Role = new String[]{"OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu03Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu03Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu03">
                    <a href="#">
                        拾得遺失物招領 <i class="fa fa-angle-down"></i><span id="menu03span"></span>
                    </a>
                    <ul>
                        <li id="menu0301" style="display: none"><a href="OP03A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物招領資料維護</a></li>
                        <li id="menu0302" style="display: none"><a href="OP03A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物招領期滿處理資料維護</a></li>
                    </ul>
                </li>
                <% } %>
                <%
                    //menu04的權限
                    String[] menu04Role = new String[]{"OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu04Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu04Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu04">
                    <a href="#">
                        拾得人領回公告 <i class="fa fa-angle-down"></i><span id="menu04span"></span>
                    </a>
                    <ul>
                        <li id="menu0401" style="display: none"><a href="OP04A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得人領回公告資料維護</a></li>
                        <li id="menu0402" style="display: none"><a href="OP04A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得人領回公告期滿處理資料維護</a></li>
                    </ul>
                </li>
                <% } %>
                <%
                    //menu05的權限
                    String[] menu05Role = new String[]{"OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu05Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu05Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu05">
                    <a href="#">
                        結案資料 <i class="fa fa-angle-down"></i><span id="menu05span"></span>
                    </a>
                    <ul>
                        <li id="menu0501" style="display: none"><a href="OP05A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物結案資料維護</a></li>
                    </ul>
                </li>
                <% } %>
                <%
                    //menu06的權限
                    //20231120 為了多加OP300008 署稽查人員權限(只有menu06查詢主選單)
                    String[] menu06Role = new String[]{"OP300001", "OP300002", "OP300003", "OP300004", "OP300005", "OP300006", "OP300008"};
                    flag = false;
                    for (int i = 0; i < menu06Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu06Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu06">
                    <a href="#">
                        綜合查詢 <i class="fa fa-angle-down"></i><span id="menu06span"></span>
                    </a>
                    <ul>
                        <li id="menu0601" style="display: none"><a href="OP06A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物綜合查詢</a></li>
                        <li id="menu0602" style="display: none"><a href="OP06A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物跨轄查詢</a></li>
                        <!--20230515  拾得物刪除案件查詢 自「拾得遺失物受理」移到「綜合查詢」  下面 -->
                        <li id="menu0204" style="display: none"><a href="OP02A04Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物刪除案件查詢</a></li>
                    </ul>
                </li>
                <% } %>
                <%
                    //menu07的權限
                    String[] menu07Role = new String[]{"OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu07Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu07Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu07">
                    <a href="#">
                        報表列印 <i class="fa fa-angle-down"></i><span id="menu10span"></span>
                    </a>
                    <ul>
                        <li id="menu0701" style="display: none"><a href="OP07A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物登記簿列印</a></li>
                        <li id="menu0702" style="display: none"><a href="OP07A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物拍(變)賣清冊列印</a></li>
                        <li id="menu0703" style="display: none"><a href="OP07A03Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物公告招領公告單列印</a></li>
                        <li id="menu0704" style="display: none"><a href="OP07A04Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物公告招領清冊列印</a></li>
                        <li id="menu0705" style="display: none"><a href="OP07A05Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物公告期滿案件清冊列印</a></li>
                        <li id="menu0706" style="display: none"><a href="OP07A06Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得人領回公告單列印</a></li>
                        <li id="menu0707" style="display: none"><a href="OP07A07Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得人逾期未領取拾得物清冊列印</a></li>
                        <li id="menu0708" style="display: none"><a href="OP07A08Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物處理情形統計表列印</a></li>
                        <li id="menu0709" style="display: none"><a href="OP07A09Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物公告件數統計表列印</a></li>
                        <li id="menu0710" style="display: none"><a href="OP07A10Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得人地址列印</a></li>
                        <li id="menu0711" style="display: none"><a href="OP07A11Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>遺失人領回清冊</a></li>
                        <li id="menu0714" style="display: none"><a href="OP07A14Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得公告期滿拾得人領回清冊</a></li>       
                        <li id="menu0712" style="display: none"><a href="OP07A12Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>期滿遺失物拾得人清冊</a></li>
                        <li id="menu0713" style="display: none"><a href="OP07A13Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>受理拾得物發還清冊</a></li>
                        <li id="menu0715" style="display: none"><a href="OP07A15Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物移交地方自治團體清冊</a></li>
                    </ul>
                </li>
                <% } %>
                <%
                    //menu08的權限
                    String[] menu08Role = new String[]{"OP300002", "OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu08Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu08Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu08">
                    <a href="#">
                        伍佰元專區 <i class="fa fa-angle-down"></i><span id="menu08span"></span>
                    </a>
                    <ul>
                        <li id="menu0801" style="display: none"><a href="OP08A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物受理維護</a></li>
                        <li id="menu0802" style="display: none"><a href="OP08A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得遺失物民眾認領維護</a></li>
                        <li id="menu0803" style="display: none"><a href="OP08A03Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>拾得物結案資料維護</a></li>

                    </ul>
                </li>
                <% } %>
                <%
                    //menu09的權限
                    String[] menu09Role = new String[]{"OP300003", "OP300004", "OP300005", "OP300006"};
                    flag = false;
                    for (int i = 0; i < menu09Role.length; i++) {
                        if (!(arryRole.toString().indexOf(menu09Role[i]) == -1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                %>
                <li id="menu09">
                    <a href="#">
                        系統管理 <i class="fa fa-angle-down"></i><span id="menu09span"></span>
                    </a>
                    <ul>
                        <li id="menu0901" style="display: none"><a href="OP09A01Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>批次作業</a></li>
                        <li id="menu0902" style="display: none"><a href="OP09A02Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>查詢統計</a></li>
                        <li id="menu0903" style="display: none"><a href="OP09A03Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>權限管理</a></li>
                        <li id="menu0904" style="display: none"><a href="OP09A04Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>發文字參數維護</a></li>
                        <li id="menu0905" style="display: none"><a href="OP09A05Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>單位地點維護</a></li>
                        <li id="menu0906" style="display: none"><a href="OP09A06Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>單位MAIL維護</a></li>
                        <li id="menu0907" style="display: none"><a href="OP09A07Q_01.jsp" target="mainFrame" 
                                                                   onclick="top.document.getElementById('topFrame').setAttribute('cols', '*,0');clearSession();">
                                <i class="fa fa-angle-right"></i>信箱維護</a></li>
                    </ul>
                </li>
                <% }%>
                <li id="menu10">
                    <a href="OP10A01Q_01.jsp">
                        資訊公告專區 
                        <i style="visibility: hidden" class="fa fa-angle-down"></i>
                        <span id="menu10span"></span>
                    </a>
                </li>
            </ul>
        </div>
        <!-- END HORIZANTAL MENU -->
        <!-- BEGIN RESPONSIVE MENU TOGGLER -->

        <!-- END RESPONSIVE MENU TOGGLER -->
        <!-- BEGIN TOP NAVIGATION MENU -->

        <!-- END TOP NAVIGATION MENU -->
    </div>
    <!-- END TOP NAVIGATION BAR -->
</div>
<!-- END HEADER -->
<script>
    var userName = '<%=user.getUserName()%>';
    var userUnit = '<%=user.getUnitName()%>';
    var userUnitCd = '<%=user.getUnitCd()%>';
</script>

