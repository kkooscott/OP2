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
                                                        <li><a href="#">系統管理</a> <i class="fa fa-angle-right"></i>
							</li>
							<li><a href="#">發文字參數維護</a>
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
                                                                                                                <td class="labelField"  width="5%">單位：</td>
                                                                                                                <td class="dataField" width="30%">
                                                                                                                    <select class="form-control" id="OP_unitLevel2_INS"  name="unitLevel2" onchange="bindUNITDDL('3', 'OP_unitLevel2_INS', 'OP_unitLevel3_INS', '', '');" >
                                                                                                                    </select> 
                                                                                                                    <select class="form-control" id="OP_unitLevel3_INS" name="unitLevel3">
                                                                                                                    </select>
                                                                                                                </td>	
                                                                                                                <td class="labelField" width="5%">招領公告發文字：</td>
                                                                                                                <td class="dataField" width="30%">
                                                                                                                    <input type="text" class="form-control" id="OP_DOC_WD1" maxlength="20"/>
                                                                                                                </td>
                                                                                                                <td class="labelField" width="5%">是否開啟自動取號：</td>
                                                                                                                <td class="dataField" width="10%">
                                                                                                                    <select class="form-control" id="OP_YN_GET_NO1">
                                                                                                                        <option value="1">是</option>
                                                                                                                        <option value="0">否</option>
                                                                                                                    </select>
                                                                                                                </td>
                                                                                                        </tr>
                                                                                                        <tr>
                                                                                                            <td colspan="2" >
                                                                                                                        <button type="button" class="btn green-seagreen btn-sm"
                                                                                                                            id="saveCheckBtn">
                                                                                                                            <i class="fa fa-plus"></i>&nbsp;新增
                                                                                                                        </button>
                                                                                                                        <button class="btn green btn-sm" type="button"
                                                                                                                                id="updateCheckBtn" style="display:none">
                                                                                                                                <i class="fa fa-pencil"></i>&nbsp;修改
                                                                                                                        </button>&nbsp;
                                                                                                                        <button type="button" class="btn red-intense btn-sm" style="display:none"
                                                                                                                            id="deleteCheckBtn">
                                                                                                                            <i class="fa fa-times"></i>&nbsp;刪除
                                                                                                                        </button>&nbsp;
                                                                                                                        <button class="btn yellow btn-sm" type="button"
                                                                                                                                id="CLEAR" name="CLEAR" style="display:none">
                                                                                                                                <i class="fa fa-eraser"></i>&nbsp;清除
                                                                                                                        </button>
                                                                                                                        <input type="hidden" class="form-control" id="HidOP_SEQ_NO" value="" >
                                                                                                                </td>
                                                                                                                <td class="labelField" width="5%">領回公告發文字：</td>
                                                                                                                <td class="dataField" width="30%">
                                                                                                                    <input type="text" class="form-control"  id="OP_DOC_WD2" maxlength="20"/>
                                                                                                                </td>
                                                                                                                <td class="labelField" width="5%">是否開啟自動取號：</td>
                                                                                                                <td class="dataField" width="30%">
                                                                                                                    <select class="form-control" id="OP_YN_GET_NO2">
                                                                                                                        <option value="1">是</option>
                                                                                                                        <option value="0">否</option>
                                                                                                                    </select>
                                                                                                                </td>
                                                                                                        </tr>
                                                                                            </table>
                                                                                        
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
									<!-- END EXAMPLE TABLE PORTLET-->
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
					</div>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="Footer.jsp" />

	<!-- 每個頁面各自使用的js -->
        <script src="assets/js/jquery.blockUI.js"></script>
	<script src="assets/js/OP09A04Q_01.js"></script>
        <script src="assets/js/ddlProcess.js"></script>
	<script src="assets/js/formProcess.js"></script>
</body>
</html>