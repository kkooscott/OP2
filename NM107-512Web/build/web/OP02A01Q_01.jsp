<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="Head.jsp" />
<link href="assets/plugins/jquery-toggles/toggles.css" rel="stylesheet" />
<link href="assets/plugins/jquery-toggles/toggles-modern.css"
	rel="stylesheet" />
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
                                                        <li><a href="#">拾得遺失物受理</a> <i class="fa fa-angle-right"></i>
							</li>
							<li><a href="#">拾得物受理資料新增</a>
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
                                        <input type="hidden" id="actionType" name="actionType" value="">
                                        <input type="hidden" id="actionDetailType" name="actionDetailType" value="">
                                        <input type="hidden" id="IPUBASIC_OP_SEQ_NO" name="IPUBASIC.OP_SEQ_NO" value="">
                                        <input type="hidden" id="IPUDETAIL_OP_SEQ_NO" name="IPUDETAIL.OP_SEQ_NO" value="">
                                        <input type="hidden" id="OP_UPL_FILE_PATH" value="PUOJ_PIC//" >
                                        <input type="hidden" id="OP_UPL_FILE_NAME1" value="" >
                                        <input type="hidden" id="IPHOTO_OP_SEQ_NO1" value="" >
                                        <input type="hidden" id="IPHOTO_OP_FILE_PATH1" value="" >
                                        <input type="hidden" id="IPHOTO_DETELE1" value="" >
                                        <input type="hidden" id="OP_UPL_FILE_NAME2" value="" >
                                        <input type="hidden" id="IPHOTO_OP_SEQ_NO2" value="" >
                                        <input type="hidden" id="IPHOTO_OP_FILE_PATH2" value="" >
                                        <input type="hidden" id="IPHOTO_DETELE2" value="" >
					<div class="tab-content">
						<div class="tab-pane active" id="tabMain">
							<div class="row">
								<table name="mainTB" border="0" width="100%" cellpadding="0"
                                                                       cellspacing="0" class="keyinTableGray">
                                                                       <!--202404 哪天想秀在畫面上就用AppUse.jsp-->
                                                                        <jsp:include page="AppUseHide.jsp" /> 
                                                                        <tr>
                                                                            <a name='button_img' title="總價值超過伍佰元" onclick="valueChoose('1');">
                                                                                <img alt="總價值超過伍佰元" src="assets/img/button500.png" width="400"  height="100">
                                                                            </a>
                                                                        </tr>
                                                                        <tr>
                                                                            <a name='button_img' title="總價值伍佰元(含)以下" onclick="valueChoose('0');">
                                                                                <img alt="總價值伍佰元(含)以下" src="assets/img/button500down.png" width="400"  height="100"/>
                                                                            </a>
                                                                        </tr>
                                                                </table>
							</div>
						</div>
                                                <div class="tab-pane" id="modal-editor-add">
                                                    <div class="modal-body">
                                                        <div class="tabbable-custom " id="divModalContent">
                                                                <button class="btn purple btn-sm" type="button"
                                                                        onclick="javascript:location.href='OP02A02Q_01.jsp'" 
                                                                        style="float: right">
                                                                        <i class="fa fa-undo"></i>&nbsp;返回受理資料維護
                                                                </button>
                                                                <ul class="nav nav-tabs">
                                                                        <li class="active" id="tabInfomation1"><a href="#tab_4_1"
                                                                                data-toggle="tab"> 受理基本資料 </a></li>
                                                                        <li id="tabInfomation2"><a href="#tab_4_2"
                                                                                data-toggle="tab"> 拾得物明細資料 </a></li>
                                                                        <li id="tabInfomation3"><a href="#tab_4_3"
                                                                                data-toggle="tab"> 表單列印 </a></li>
                                                                </ul>
                                                                <div class="tab-content">
                                                                    <div class="tab-pane active" id="tab_4_1">
                                                                        <jsp:include page="OP02B01M_01.jsp" />
                                                                    </div>    
                                                                    <div class="tab-pane" id="tab_4_2">
                                                                        <jsp:include page="OP02B02M_01.jsp" />
                                                                    </div>
                                                                    <div class="tab-pane" id="tab_4_3">
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
					</div>
				</form>

			</div>
		</div>
	</div>

	<jsp:include page="Footer.jsp" />

	<!-- 每個頁面各自使用的js -->
        <script src="assets/js/jquery.blockUI.js"></script>
	<script src="assets/js/OP02A01Q_01.js"></script>
	<script src="assets/js/ddlProcess.js"></script>
	<script src="assets/js/formProcess.js"></script>
	
</body>
</html>