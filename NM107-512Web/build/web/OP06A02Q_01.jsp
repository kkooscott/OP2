<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <jsp:include page="Head.jsp" />
    </head>
    <body class="page-header-fixed page-full-width">
        <!-- BEGIN HEADER -->
        <jsp:include page="Menu.jsp" />
        <!-- END HEADER -->

        <div class="clearfix"></div>
        <!-- BEGIN CONTAINER -->
        <div class="page-container">
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <div class="page-content">
                    <!-- BEGIN PAGE HEADER-->
                    <div class="row">
                        <div class="col-md-12">
                            <!-- BEGIN PAGE TITLE & BREADCRUMB-->
                            <ul class="page-breadcrumb breadcrumb">	
                                <li><i class="fa fa-home"></i> <a href="OP01A01Q_01.jsp">首頁</a>
                                    <i class="fa fa-angle-right"></i>
                                </li>
                                <li><a href="#">綜合查詢</a> <i class="fa fa-angle-right"></i>
                                </li>
                                <li><a href="#">拾得遺失物跨轄查詢</a>
                                </li>
                                <li style="float:right;">
                                    <a>上網認領：</a>
                                    <label id="countNetCase" class="badge badge-danger">0</label>
                                </li>
                            </ul>
                            <!-- END PAGE TITLE & BREADCRUMB-->
                        </div>
                    </div>
                    <!-- END PAGE HEADER-->

                    <!-- BEGIN PAGE CONTENT-->
                    <form id="form1" onsubmit="return false;">
                        <div class="tab-content">
                            <div class="tab-pane active" id="tabMain">
                                <div class="row">
                                    <div class="col-md-12">
                                        <!-- BEGIN EXAMPLE TABLE PORTLET-->
                                        <div class="portlet box grey-cascade">
                                            <div class="portlet-title" onclick="expandQuery(event)">
                                                <div class="caption">
                                                    <i class="fa fa-search"></i><span style="margin-top: 10px">查詢條件</span>
                                                </div>
                                                <div class="tools">
                                                    <a href="#" onclick="toogleQuery(event)" class="collapse"></a>
                                                </div>
                                            </div>
                                            <div class="portlet-body" id="queryporlet">
                                                <div>
                                                    <table border="0" width="100%" cellpadding="0"
                                                           cellspacing="0" class="keyinTableGray">
                                                        <jsp:include page="AppUse.jsp" />
                                                        <tr>
                                                            <td valign="top">
                                                                <table width="100%" border="0" cellspacing="0"
                                                                       cellpadding="0" class="outerTable">
                                                                    <tr>
                                                                        <td class="labelField" width="10%">查詢類別：</td>
                                                                        <td class="dataField" colspan="3">
                                                                            <select class="form-control"
                                                                                    data-width="auto" id="OP_STATUS_CD">
                                                                                <option value="1" selected="selected">認領人</option>
                                                                                <option value="2">IMEI碼</option>
                                                                                <option value="3">MAC碼</option>
                                                                                <option value="4">廠牌顏色</option>
                                                                                <option value="5">電信門號</option>
                                                                            </select>

                                                                            <label id = "label1" class="normal-label"><font color="red">(『認領人姓名』或『認領人身分證號』欄位，至少填寫一個欄位。)</font></label>
                                                                        </td>	
                                                                    </tr>
                                                                    <tr id="type1">
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />認領人姓名：
                                                                        </td>
                                                                        <td class="dataField" width="40%">
                                                                            <input type="text" class="form-control" size="30" maxlength="30"
                                                                                   id="OP_SEARCH_PUCP_NAME" />
                                                                        </td>
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />認領人身分證號：
                                                                        </td>
                                                                        <td class="dataField" width="40%">
                                                                            <input type="text" class="form-control" size="30" maxlength="20"
                                                                                   id="OP_SEARCH_PUCP_IDN" />
                                                                        </td>
                                                                    </tr>
                                                                    <tr id="type2" style="display:none">
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />IMEI碼：
                                                                        </td>
                                                                        <td class="dataField" colspan="3">
                                                                            <input type="text" class="form-control" size="30" maxlength="15"
                                                                                   id="OP_SEARCH_IMEI_CODE" />
                                                                        </td>
                                                                    </tr>
                                                                    <tr id="type3" style="display:none">
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />MAC碼：
                                                                        </td>
                                                                        <td class="dataField" colspan="3">
                                                                            <input type="text" class="form-control" size="30" maxlength="30"
                                                                                   id="OP_SEARCH_MAC_ADDR" />
                                                                        </td>
                                                                    </tr>
                                                                    <tr id="type4" style="display:none">
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />物品種類：
                                                                        </td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control" id="OP_SEARCH_TYPE_CD"
                                                                                    name="OP_SEARCH_TYPE_CD" data-width="180px" data-size="8">
                                                                            </select>
                                                                        </td>
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />廠牌：
                                                                        </td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control" id="OP_SEARCH_BRAND_CD"
                                                                                    name="OP_SEARCH_BRAND_CD" data-width="180px" data-size="8">
                                                                            </select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr id="type41" style="display:none">
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />顏色：
                                                                        </td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control" id="OP_SEARCH_COLOR_CD"
                                                                                    name="OP_SEARCH_COLOR_CD" data-width="180px" data-size="8">
                                                                            </select>
                                                                        </td>
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />拾得日期：
                                                                        </td>
                                                                        <td nowrap="nowrap" class="dataField">
                                                                            <input type="text" class="form-control"
                                                                                   id="OP_SEARCH_PU_DATE_S" value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_PU_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_PU_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" />
                                                                        </td>
                                                                    </tr>
                                                                    <tr id="type5" style="display:none">
                                                                        <td class="labelField" width="10%">
                                                                            <img src="assets/img/required.png" />電信門號：
                                                                        </td>
                                                                        <td class="dataField" colspan="3">
                                                                            <input type="text" class="form-control" size="30" maxlength="30"
                                                                                   id="OP_SEARCH_PHONE_NUM" />
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="4">
                                                                            <button class="btn green btn-sm" type="button"
                                                                                    id="queryBtn" name="queryBtn">
                                                                                <i class="fa fa-search"></i>&nbsp;查詢
                                                                            </button>&nbsp;
                                                                            <button class="btn yellow btn-sm" type="button"
                                                                                    id="QS_CLEAR" name="QS_CLEAR">
                                                                                <i class="fa fa-eraser"></i>&nbsp;清除
                                                                            </button>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- END EXAMPLE TABLE PORTLET-->
                                    </div>
                                    <!-- toolbar -->
                                    <div class="row">
                                        <div class="col-md-12" id="mainList">
                                            <!-- grid view -->
                                            <table id="gridMainList">
                                                <tr>
                                                    <td />
                                                </tr>
                                            </table>
                                            <div id="pagerMainList"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp" />

        <!-- 每個頁面各自使用的js -->
        <script src="assets/js/jquery.blockUI.js"></script>
        <script src="assets/js/OP06A02Q_01.js"></script>
        <script src="assets/js/ddlProcess.js"></script>
        <script src="assets/js/formProcess.js"></script>

    </body>
</html>