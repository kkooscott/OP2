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
                                <li><i class="fa fa-home"></i> <a href="OP01A01Q_01.jsp">民眾網路認領訊息</a>
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
                        <input type="hidden" id="checkStatusForBasic" name="checkStatusForBasic" value="">
                        <input type="hidden" id="checkYNDetail" name="checkYNDetail" value="">
                        <input type="hidden" id="actionClaimType" name="actionClaimType" value="">
                        <input type="hidden" id="IPUBASIC_OP_SEQ_NO" name="IPUBASIC.OP_SEQ_NO" value="">
                        <input type="hidden" id="IPUCLAIM_OP_SEQ_NO" name="IPUCLAIM.OP_SEQ_NO" value="">

                        <div class="tab-content">
                            <div class="tab-pane active" id="tabMain">
<!--                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="portlet box yellow">
                                            <div class="portlet-title">
                                                <div class="caption">
                                                    <i class="fa fa-bell-o"></i>待辦件數資訊
                                                </div>
                                            </div>
                                            <div class="portlet-body">
                                                <div class="scroller" id="annScroller"
                                                     style="height: 40px; overflow-y: auto" data-always-visible="1"
                                                     data-rail-visible="1">
                                                    <table width="100%" border="0" cellspacing="0"
                                                           cellpadding="0" class="keyinTableGray">
                                                        <tr>
                                                            <td class="labelField" width="10%">未結案件：</td>
                                                            <td class="dataField" width="15%">
                                                                <label class="normal-label" id="NoFinishCase"></label>
                                                            </td>	
                                                            <td class="labelField" width="10%">未公告案件：</td>
                                                            <td class="dataField" width="15%">
                                                                <label class="normal-label" id="NoAnnounceCase"></label>
                                                            </td>
                                                            <td class="labelField" width="10%">招領期滿案件：</td>
                                                            <td class="dataField" width="15%">
                                                                <label class="normal-label" id="AnDlCase"></label>
                                                            </td>
                                                            <td class="labelField" width="10%">領回期滿案件：</td>
                                                            <td class="dataField" width="15%">
                                                                <label class="normal-label" id="PuanDlCase"></label>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>-->
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
                                                                        <td class="labelField" width="10%">
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
                                                                        <td class="labelField" width="10%">認領填單日期：</td>
                                                                        <td nowrap="nowrap" class="dataField" width="40%">
                                                                            <input type="text" class="form-control"
                                                                                   id="OP_SEARCH_FM_DATE_S" value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_FM_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_FM_DATE_S', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" /> ～
                                                                            <input type="text" class="form-control" id="OP_SEARCH_FM_DATE_E"
                                                                                   value=""
                                                                                   onclick="WdatePicker({dateFmt: 'yyy/MM/dd'})"
                                                                                   onfocus="WdatePicker({dateFmt: 'yyy/MM/dd'})">
                                                                            <img onclick="WdatePicker({el: 'OP_SEARCH_FM_DATE_E', dateFmt: 'yyy/MM/dd'})"
                                                                                 onfocus="WdatePicker({el: 'OP_SEARCH_FM_DATE_E', dateFmt: 'yyy/MM/dd'})"
                                                                                 src="assets/img/calendar.gif" align="absmiddle"
                                                                                 alt="選擇日期" style="cursor: pointer" />
                                                                        </td>
                                                                        <td class="labelField" width="10%">是否審核：</td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control"
                                                                                    data-width="auto" id="OP_SEARCH_YN_LOSER">
                                                                                <option value="">請選擇...</option>
                                                                                <option value="N">否</option>
                                                                                <option value="Y">是</option>
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

                            <!-- 有民眾上網認領-dialog -->
                            <div class="tab-pane" id="modal-editor-add">
                                <div class="modal-body">
                                    <div class="tabbable-custom " id="divModalContent">
                                        <button class="btn purple btn-sm" type="button"
                                                onclick="goTab('tabMain', 'modal-editor-add');$('#data_information').show();"
                                                style="float: right">
                                            <i class="fa fa-undo"></i>&nbsp;返回
                                        </button>
                                        <ul class="nav nav-tabs ">
                                            <li id="tabInfomation1"><a href="#tab_4_1"
                                                                       data-toggle="tab"> 受理基本資料 </a></li>
                                            <li id="tabInfomation2"><a href="#tab_4_2"
                                                                       data-toggle="tab"> 拾得物明細資料 </a></li>
                                            <li class="active" id="tabInfomation3"><a href="#tab_4_3"
                                                                                      data-toggle="tab"> 民眾認領資料 </a></li>
                                            <li id="tabInfomation4"><a href="#tab_4_4"
                                                                       data-toggle="tab"> 表單列印 </a></li>
                                        </ul>
                                        <div class="tab-content">
                                            <div class="tab-pane" id="tab_4_1">
                                                <jsp:include page="OP02B01M_01.jsp" />
                                            </div>    
                                            <div class="tab-pane" id="tab_4_2">
                                                <jsp:include page="OP02B02M_01.jsp" />	
                                            </div>
                                            <div class="tab-pane active" id="tab_4_3">
                                                <jsp:include page="OP02B03M_01.jsp" />
                                            </div>
                                            <div class="tab-pane" id="tab_4_4">
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
        <script src="assets/js/OP01A01Q_01.js"></script>
        <script src="assets/js/ddlProcess.js"></script>
        <script src="assets/js/formProcess.js"></script>
    </body>
</html>