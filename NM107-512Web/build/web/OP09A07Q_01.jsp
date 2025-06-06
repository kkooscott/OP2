<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" import="npa.tm.vo.*,npa.tm.util.*"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- InstanceBeginEditable name="doctitle" -->
    <!-- InstanceEndEditable -->
    <jsp:include page="Head.jsp" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- BEGIN THEME STYLES -->
    <link href="assets/css/style.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->

</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-full-width">
    <!-- BEGIN HEADER -->
    <jsp:include page="Menu.jsp" />
    <!-- END HEADER -->
    <div class="page-container">
        <!-- BEGIN CONTENT -->
        <div class="page-content-wrapper">
            <div class="page-content">
                <!-- BEGIN PAGE CONTENT-->
                <!-- InstanceBeginEditable name="MainContent" -->
                <!-- BEGIN PAGE HEADER-->
                <div class="row">
                    <div class="col-md-12">
                        <!-- BEGIN PAGE TITLE & BREADCRUMB-->
                        <ul class="page-breadcrumb breadcrumb">
                            <li><i class="fa fa-home"></i>
                                <a href="OP01A01Q_01.jsp">首頁</a> <i class="fa fa-angle-right"></i></li>
                            <li>系統管理 <i class="fa fa-angle-right"></i></li>
                            <li>信箱維護</li>
                        </ul>
                        <!-- END PAGE TITLE & BREADCRUMB-->
                    </div>
                </div>
                <!-- END PAGE HEADER-->
                <form id="form1" onsubmit="return false;">
                <div class="row" >
                    <div class="col-md-12">
                        <div class="tab-content" >
                            <div class="tab-pane active" id="tabMain">
                                <div class="row" >
                                    <div class="col-md-12">
                                        <!-- BEGIN EXAMPLE TABLE PORTLET-->
                                        <div class="portlet box light-grey">
                                            <div class="portlet-title" onclick="expandQuery()">
                                                <div class="caption"><i class="fa fa-search"></i><span style="margin-top:10px">查詢條件</span></div>
                                                <div class="tools">
                                                    <a href="javascript:;" class="collapse"></a>
                                                </div>
                                            </div>
                                            <div class="portlet-body" id="queryporlet">
                                                <div>
                                                    <table border="0" width="100%" cellpadding="0"
                                                           cellspacing="0" class="keyinTableGray">
                                                        <tr>
                                                            <td valign="top">
                                                                <table width="100%" border="0" cellspacing="1" cellpadding="0" class="outerTable fixedSearchBar"  > 
                                                                    <tr>
                                                                        <td class="labelField" width="2%">承辦單位：</td>
                                                                        <td class="dataField" width="40%">
                                                                            <select class="form-control"id="OP_UNIT_CD" 
                                                                                    name="unitLevel"
                                                                                    />
                                                                            </select>	
<!--                                                                            <select class="form-control" id="OP_B_UNIT_CD"
                                                                                    name="unitLevel3"
                                                                                    onchange="bindUNITDDL('4', 'OP_D_UNIT_CD', 'OP_B_UNIT_CD', 'OP_UNIT_CD', '');">
                                                                            </select>
                                                                            <select class="form-control" id="OP_UNIT_CD"
                                                                                        name="unitLevel4" style="display: none">
                                                                            </select>-->
                                                                        </td>
                                                                    </tr> 
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
<!--                                                    <div class="button_rightDiv">
                                                            <button class="btn  green btn-sm" type="button"id="QueryBtn"><i class="fa fa-search"></i>&nbsp;查詢
                                                            </button>&nbsp;
                                                    </div>-->
                                                </div>    
                                            </div>
                                        </div>

                                        <div id="tab_1-2" class="tab-pane">
<!--                                            <div class="button_rightDiv">
                                                        <button class="btn  green-seagreen btn-sm" type="button" id="ADDBtn" ><i class="fa fa-plus"></i>&nbsp;新增
                                                                </button>&nbsp;
                                                        <button class="btn red btn-sm" type="button" id="delBtn" style="color: white;background-color: #e02222;"><i class="fa fa-remove"></i>&nbsp;刪除</button>
                                            </div>-->
                                            <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                                                
                                                <tr>
                                                    <td valign="top">
                                                        <table width="100%" border="0" cellspacing="1" cellpadding="0" class="outerTable"  >
                                                            <div class="table-header clearfix"></div>
                                                            <!-- grid view -->
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <table id="gridMainList"></table> 
                                                                    <div id="pagerMainList"></div>
                                                                </div>
                                                            </div>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>   
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 案件流程-dialog -->
                            <div id="modal-editor-autocase" class="tab-pane">
                                <div class="modal-body">
                                    <!-- 上傳檔案類型資料維護-dialog -->
                                    <div >
                                        <div class="modal-body">
                                            <table border="0" width="100%" cellpadding="0" cellspacing="0"
                                                   class="keyinTableGray">
                                                <div class="button_rightDiv">
<!--                                                    <button type="button" class="btn green btn-sm"
                                                            id="saveCheckBtn">
                                                        <i class="fa fa-plus"></i>&nbsp;新增
                                                    </button>-->
                                                    <button class="btn green btn-sm" type="button"
                                                            id="updateCheckBtn" style="display:none">
                                                            <i class="fa fa-pencil"></i>&nbsp;修改
                                                    </button>
                                                    <button type="button" data-dismiss="modal"
                                                            class="btn purple btn-sm"
                                                            onClick="goTab('tabMain', 'modal-editor-autocase');cleanDetail();showgridMainList();">
                                                        <i class="fa fa-undo"></i>&nbsp;返回
                                                    </button> 
<!--                                                    <button class="btn yellow btn-sm" type="button"
                                                                        id="CLEAR" name="QS_CLEAR" style="display:none">
                                                                    <i class="fa fa-eraser"></i>&nbsp;清除
                                                                </button>
-->                                                    <input type="hidden" class="form-control" id="HidTM_SEQ_NO" value="" >
                                                </div>
                                                <div class="table-header clearfix">
                                                    <div class="modal-header">
                                                        <h4 class="modal-title">承辦人員設定</h4>
                                                    </div>
                                                </div>
                                                <tr>
                                                    <td valign="top">
                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0"
                                                               class="outerTable">
                                                            <tr>
                                                                <td class="labelField" width="10%">承辦單位：</td>
                                                                <td class="dataField" width="40%">
                                                                    <input type="text" class="form-control" id="OP_unitLevel2" name="unitLevel2" size="100" />
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="labelField" width="10%">EMAIL：</td>
                                                                <td class="dataField" colspan="3" >
                                                                    <input type="text" class="form-control" id="OP_EMAIL" name="OP_EMAIL" size="100" />
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
                            <!-- End of 流程-dialog -->
                        </div>
                    </div>
                </div>
              </form>
                <!-- END CONTENT -->
            </div>
        </div>
    </div>
    <jsp:include page="Footer.jsp" />
    <!-- END CORE PLUGINS -->
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <!-- InstanceBeginEditable name="PageLevelPlugin" -->
    <!-- 插入自己的 js 檔案 -->
    <!--[if lt IE 9]>
            <script src="assets/js/CkeditorIE8Fix.js"></script>
    <![endif]--> 	
    <!--	<script src="assets/plugins/ckeditor/ckeditor.js" type="text/javascript"></script>
            <script src="assets/plugins/jquery-multi-select/js/jquery.multi-select.js" type="text/javascript"></script>
            <script src="assets/js/reportUtil.js" type="text/javascript"></script>	-->
    <!-- InstanceEndEditable -->
    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN CORE LEVEL SCRIPTS -->
    <!-- END CORE LEVEL SCRIPTS -->
    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <!-- InstanceBeginEditable name="PageLevelScript" -->
    <!-- 插入自己的 javascript -->
    <script src="assets/js/jquery.blockUI.js"></script>
    <script src="assets/js/OP09A07Q_01.js"></script>
    <script src="assets/js/ddlProcess.js"></script>
    <script src="assets/js/formProcess.js"></script>

    <!-- InstanceEndEditable -->
    <!-- END PAGE LEVEL SCRIPTS -->
    <!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
<!-- InstanceEnd -->


</html>
