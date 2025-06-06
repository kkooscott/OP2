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
                                <li><a href="#">伍佰元專區</a> <i class="fa fa-angle-right"></i>
                                </li>
                                <li><a href="#">拾得物結案資料維護</a>
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
                        <input type="hidden" id="IPUBASIC_OP_PU_YN_OV500" name="IPUBASIC.OP_PU_YN_OV500" value="">
                        <input type="hidden" id="checkStatusForBasic" name="checkStatusForBasic" value="">
                        <input type="hidden" id="checkYNDetail" name="checkYNDetail" value="">
                        <input type="hidden" id="actionType" name="actionType" value="updateBasicList">
                        <input type="hidden" id="actionDetailType" name="actionDetailType" value="">
                        <input type="hidden" id="actionClaimType" name="actionClaimType" value="">
                        <input type="hidden" id="actionFnshType" name="actionFnshType" value="">
                        <input type="hidden" id="IPUBASIC_OP_SEQ_NO" name="IPUBASIC.OP_SEQ_NO" value="">
                        <input type="hidden" id="IPUDETAIL_OP_SEQ_NO" name="IPUDETAIL.OP_SEQ_NO" value="">
                        <input type="hidden" id="IPUCLAIM_OP_SEQ_NO" name="IPUCLAIM.OP_SEQ_NO" value="">
                        <input type="hidden" id="IFNSH_OP_SEQ_NO" name="IFNSH.OP_SEQ_NO" value="">
                        <input type="hidden" id="IPUANDL_OP_SEQ_NO" name="IPUANDL.OP_SEQ_NO" value="">
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
                                                        <!--202403 哪天想秀在畫面上就用AppUse.jsp-->
                                                        <jsp:include page="AppUseHide.jsp" />

                                                        <tr>
                                                            <td valign="top">
                                                                <table width="100%" border="0" cellspacing="0"
                                                                       cellpadding="0" class="outerTable">
                                                                    <tr>
                                                                        <td class="labelField">
                                                                            <img src="assets/img/required.png" />受理單位：
                                                                        </td>
                                                                        <td class="dataField" colspan="3">
                                                                            <select class="form-control" id="OP_Search_unitLevel2"
                                                                                    name="unitLevel2" data-width="180px" data-size="8"
                                                                                    onchange="bindUNITDDL('3', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', '');">
                                                                            </select> 
                                                                            <select class="form-control" id="OP_Search_unitLevel3"
                                                                                    name="unitLevel3"
                                                                                    onchange="bindUNITDDL('4', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', '');">
                                                                            </select> 
                                                                            <select class="form-control" id="OP_Search_unitLevel4"
                                                                                    name="unitLevel4">
                                                                            </select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="labelField" width="10%">收據編號：</td>
                                                                        <td class="dataField" width="40%">
                                                                            <input type="text" class="form-control" size="30"
                                                                                   id="OP_SEARCH_AC_RCNO" />
                                                                        </td>
                                                                        <td class="labelField" width="10%">拾得人類別：</td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control" id="OP_SEARCH_PUPO_TP_CD"
                                                                                    data-width="180px" data-size="8">
                                                                            </select>
                                                                        </td>	
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="labelField">拾得日期起迄：</td>
                                                                        <td nowrap="nowrap" class="dataField">
                                                                            <input type="text" class="form-control"
                                                                                   id="OP_SEARCH_PU_DATE_S" value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_PU_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_PU_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" /> ～
                                                                            <input type="text" class="form-control" id="OP_SEARCH_PU_DATE_E"
                                                                                   value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_PU_DATE_E', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_PU_DATE_E', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" />
                                                                        </td>
                                                                        <td class="labelField">受理日期起迄：</td>
                                                                        <td nowrap="nowrap" class="dataField">
                                                                            <input type="text" class="form-control"
                                                                                   id="OP_SEARCH_AC_DATE_S" value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_AC_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_AC_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" /> ～
                                                                            <input type="text" class="form-control" id="OP_SEARCH_AC_DATE_E"
                                                                                   value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_AC_DATE_E', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_AC_DATE_E', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" />
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="labelField" width="10%">物品屬性：</td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control" id="OP_SEARCH_PUOJ_ATTR_CD"
                                                                                    data-width="180px" data-size="8">
                                                                            </select>
                                                                        </td>
                                                                        <td class="labelField" width="10%">目前狀態：</td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control"
                                                                                    data-width="auto" id="OP_SEARCH_CURSTAT_CD">
                                                                                <option value="">請選擇...</option>
                                                                                <option value="1">處理中</option>
                                                                                <option value="6">已結案</option>
                                                                            </select>
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

                            <!-- 案件審核-dialog -->
                            <div class="tab-pane" id="modal-editor-add">
                                <div class="modal-body">
                                    <div class="tabbable-custom " id="divModalContent">                                                                    
                                        <button class="btn purple btn-sm" type="button"
                                                onclick="goTab('tabMain', 'modal-editor-add');"
                                                style="float: right">
                                            <i class="fa fa-undo"></i>&nbsp;返回
                                        </button>
                                        <ul class="nav nav-tabs ">
                                            <li id="tabInfomation1"><a href="#tab_4_1"
                                                                       data-toggle="tab"> 受理基本資料 </a></li>
                                            <li id="tabInfomation2"><a href="#tab_4_2"
                                                                       data-toggle="tab"> 拾得物明細資料 </a></li>
                                            <li id="tabInfomation3"><a href="#tab_4_3"
                                                                       data-toggle="tab"> 民眾認領資料 </a></li>
                                            <li class="active" id="tabInfomation4"><a href="#tab_4_4"
                                                                                      data-toggle="tab"> 結案資料 </a></li>
                                            <li id="tabInfomation5"><a href="#tab_4_5"
                                                                       data-toggle="tab"> 表單列印 </a></li>
                                        </ul>
                                        <div class="tab-content">
                                            <div class="tab-pane" id="tab_4_1">
                                                <jsp:include page="OP02B01M_01.jsp" />
                                            </div>    
                                            <div class="tab-pane" id="tab_4_2">
                                                <jsp:include page="OP02B02M_01.jsp" />
                                            </div>
                                            <div class="tab-pane" id="tab_4_3">                                                                                    
                                                <jsp:include page="OP02B03M_01.jsp" />
                                            </div>
                                            <div class="tab-pane active" id="tab_4_4">
                                                <jsp:include page="OP08B01M_01.jsp" />
                                            </div>
                                            <div class="tab-pane" id="tab_4_5">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <button type="button" class="btn green btn-sm" style="display:none"
                                                                id="printBtn">
                                                            <i class="fa fa-print"></i>&nbsp;列印
                                                        </button>
                                                        <br><br>    
                                                        <div id = "tab_4_3_1_alert">
                                                            <h4>請先輸入受理基本資料並按儲存按鈕，以產生拾得遺失物收據號碼。</h4>
                                                        </div>
                                                        <div id = "tab_4_3_2_alert" style="display:none">
                                                            <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
                                                        </div>
                                                        <div id = "tab_4_3_main" style="display:none">
                                                            <div class="table-header clearfix">
                                                                <div class="modal-header">
                                                                    <h4 class="modal-title">表單列印</h4>
                                                                </div>										
                                                            </div>
                                                            <!-- grid view -->
                                                            <div>
                                                                <table id="gridList_3">
                                                                    <tr>
                                                                        <td />
                                                                    </tr>
                                                                </table>
                                                                <div id="pageList_3"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- End of 審核-dialog -->
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp" />

        <!-- 每個頁面各自使用的js -->
        <script src="assets/js/jquery.blockUI.js"></script>
        <script src="assets/js/OP08A03Q_01.js"></script>
        <script src="assets/js/ddlProcess.js"></script>
        <script src="assets/js/formProcess.js"></script>

    </body>
</html>