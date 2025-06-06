<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
                            <li><a href="#">系統管理</a> <i class="fa fa-angle-right"></i>
                            </li>
                            <li><a href="#">MAIL維護</a>
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
                                                    <tr>
                                                        <td valign="top">
                                                            <table width="100%" border="0" cellspacing="0"
                                                                   cellpadding="0" class="outerTable">
                                                                <tr>
                                                                    <td class="labelField" width="10%">
                                                                        <img src="assets/img/required.png" />單位：
                                                                    </td>
                                                                    <td class="dataField" colspan="3">
                                                                        <select class="form-control" id="OP_Search_unitLevel2"
                                                                                name="unitLevel2" data-width="180px" data-size="8"
                                                                                onchange="bindUNITDDL('3', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', '', '');">
                                                                        </select> 
                                                                        <select class="form-control" id="OP_Search_unitLevel3"
                                                                                name="unitLevel3"
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="6">
                                                                        <button type="button" class="btn green btn-sm"
                                                                                id="queryBtn" name="queryBtn">
                                                                            <i class="fa fa-search"></i>&nbsp;查詢
                                                                        </button>&nbsp;
                                                                        <button type="button" class="btn yellow btn-sm"
                                                                                id="QS_CLEAR" name="QS_CLEAR">
                                                                            <i class="fa fa-eraser"></i>&nbsp;清除
                                                                        </button>
                                                                        <button type="button" class="btn green-seagreen btn-sm"
                                                                                id="addMailPage">
                                                                            <i class="fa fa-plus"></i>&nbsp;新增
                                                                        </button>&nbsp;
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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
                            <div class="row">
                                <div class="col-md-12">
                                    <!-- BEGIN EXAMPLE TABLE PORTLET-->
                                    <div id="data_information" class="row">
                                        <div class="col-md-12">
                                            <div class="portlet box yellow">
                                                <div class="portlet-title">
                                                    <div class="caption">
                                                        <i class="fa fa-pencil"></i>參數維護
                                                    </div>
                                                </div>
                                                <div class="portlet-body">
                                                    <table width="100%" border="0" cellspacing="0"
                                                           cellpadding="0" class="keyinTableGray">
                                                        <tr>
                                                            <td class="labelField" width="10%">單位：</td>
                                                            <td class="dataField" width="40%">
                                                                <select class="form-control" id="OP_unitLevel2_INS"  name="unitLevel2" onchange="bindUNITDDL('3', 'OP_unitLevel2_INS', 'OP_unitLevel3_INS', '', '');" >
                                                                </select> 
                                                                <select class="form-control" id="OP_unitLevel3_INS" name="unitLevel3">
                                                                </select>
                                                            </td>	
                                                        </tr>
                                                        <tr>
                                                            <td class="labelField" width="10%">收據編號：</td>
                                                            <td class="dataField" width="40%">
                                                                <label class="normal-label" id="opUnitNm1123">OPXXXXXXXXXXXXXXXXXX</label>
                                                            </td>	
                                                        </tr>
                                                        <tr>
                                                            <td class="labelField" width="10%">姓名：</td>
                                                            <td class="dataField" width="40%">
                                                               <label class="normal-label" id="opUnitNm1123">○○○ 先生/小姐</label>
                                                            </td>	
                                                        </tr>
                                                        <tr>
                                                            <td class="labelField" width="10%">信件內容：</td>
                                                            <td class="dataField" width="40%">
                                                                <textarea id="OP_MAIL_CONTENT" name="OP_MAIL_CONTENT"
                                                                    rows="13" style="width: 50%;" align="left" maxlength="300"></textarea>
                                                            </td>	
                                                        </tr>
                                                        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
                                                        <tr>
                                                            <td  >
                                                                <button class="btn purple btn-sm" type="button"
                                                                        onclick="goTab('tabMain', 'modal-editor-add');">
                                                                    <i class="fa fa-undo"></i>&nbsp;返回
                                                                </button>
                                                                <button type="button" class="btn green-seagreen btn-sm"
                                                                        id="saveCheckBtn" style="display:none">
                                                                    <i class="fa fa-plus"></i>&nbsp;新增
                                                                </button>
                                                                <button class="btn green btn-sm" type="button"
                                                                        id="updateCheckBtn" style="display:none">
                                                                    <i class="fa fa-pencil"></i>&nbsp;修改
                                                                </button>
                                                                <button class="btn yellow btn-sm" type="button"
                                                                        id="CLEAR" name="QS_CLEAR" style="display:none">
                                                                    <i class="fa fa-eraser"></i>&nbsp;清除
                                                                </button>
                                                                <input type="hidden" class="form-control" id="HidOP_SEQ_NO" value="" >
                                                            </td>
                                                        </tr>
                                                    </table>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- END EXAMPLE TABLE PORTLET-->
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
    <script src="assets/js/OP09A06Q_01.js"></script>
    <script src="assets/js/ddlProcess.js"></script>
    <script src="assets/js/formProcess.js"></script>
</body>
</html>