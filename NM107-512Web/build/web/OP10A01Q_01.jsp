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
                                <li><i class="fa fa-home"></i> <a href="OP10A01Q_01.jsp">資訊公告專區</a>
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
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="portlet box yellow">
                                            <div class="portlet-title">
                                                <div class="caption">
                                                    <i class="fa fa-bell-o"></i>待辦件數資訊
                                                </div>
                                            </div>
                                            <div class="portlet-body">
                                                <div class="scroller" id="annScroller"
                                                     style="height: 80px; overflow-y: auto" data-always-visible="1"
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
                                                    <table width="100%" border="0" cellspacing="0"
                                                           cellpadding="0" class="keyinTableGray">
                                                        <tr>
                                                            <td class="labelField" width="20%">招領即將(7日後)期滿案件：</td>
                                                            <td class="dataField" width="30%">
                                                                <label class="normal-label" id="AnDateEndCaseAfter7"></label>
                                                            </td>
                                                            <td class="labelField" width="20%">領回即將(7日後)期滿案件：</td>
                                                            <td class="dataField" width="30%">
                                                                <label class="normal-label" id="AnDlCaseAfter7"></label>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
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
        <script src="assets/js/OP10A01Q_01.js"></script>
        <script src="assets/js/ddlProcess.js"></script>
        <script src="assets/js/formProcess.js"></script>

    </body>
</html>
