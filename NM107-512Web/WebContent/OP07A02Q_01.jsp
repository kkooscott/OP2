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
                                                        <li><a href="#">報表列印</a> <i class="fa fa-angle-right"></i>
							</li>
							<li><a href="#">拾得物拍(變)賣清冊列印</a>
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
													<tr>
														<td valign="top">
															<table width="100%" border="0" cellspacing="0"
																cellpadding="0" class="outerTable">
																<tr>
                                                                                                                                    <td class="labelField" width="10%">
                                                                                                                                        <img src="assets/img/required.png" />受理單位：
                                                                                                                                    </td>
                                                                                                                                    <td class="dataField" width="40%">
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
                                                                                                                                    <td class="labelField" width="10%">含下屬：</td>
                                                                                                                                    <td class="dataField" width="40%">
                                                                                                                                        <input type="checkbox" id="includeYN" name="includeYN" value="Y"/>
                                                                                                                                    </td>
																</tr>
																<tr>
																	<td class="labelField">拾得日期起迄：</td>
																	<td nowrap="nowrap" class="dataField">
																		<input type="text" class="form-control"
                                                                                                                                                    id="OP_SEARCH_PU_DATE_S" value=""
                                                                                                                                                    onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})">
                                                                                                                                                <img onclick="WdatePicker({el:'OP_SEARCH_PU_DATE_S',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({el:'OP_SEARCH_PU_DATE_S',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    src="assets/img/calendar.gif" align="absmiddle"
                                                                                                                                                    alt="選擇日期" style="cursor: pointer" /> ～
                                                                                                                                                <input type="text" class="form-control" id="OP_SEARCH_PU_DATE_E"
                                                                                                                                                    value=""
                                                                                                                                                    onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})">
                                                                                                                                                <img onclick="WdatePicker({el:'OP_SEARCH_PU_DATE_E',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({el:'OP_SEARCH_PU_DATE_E',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    src="assets/img/calendar.gif" align="absmiddle"
                                                                                                                                                    alt="選擇日期" style="cursor: pointer" />
																	</td>
                                                                                                                                        <td class="labelField">受理日期起迄：</td>
																	<td nowrap="nowrap" class="dataField">
																		<input type="text" class="form-control"
                                                                                                                                                    id="OP_SEARCH_AC_DATE_S" value=""
                                                                                                                                                    onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})">
                                                                                                                                                <img onclick="WdatePicker({el:'OP_SEARCH_AC_DATE_S',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({el:'OP_SEARCH_AC_DATE_S',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    src="assets/img/calendar.gif" align="absmiddle"
                                                                                                                                                    alt="選擇日期" style="cursor: pointer" /> ～
                                                                                                                                                <input type="text" class="form-control" id="OP_SEARCH_AC_DATE_E"
                                                                                                                                                    value=""
                                                                                                                                                    onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})">
                                                                                                                                                <img onclick="WdatePicker({el:'OP_SEARCH_AC_DATE_E',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    onfocus="WdatePicker({el:'OP_SEARCH_AC_DATE_E',dateFmt:'yyy/MM/dd'})"
                                                                                                                                                    src="assets/img/calendar.gif" align="absmiddle"
                                                                                                                                                    alt="選擇日期" style="cursor: pointer" />
																	</td>
																</tr>
																<tr>																	
                                                                                                                                        <td class="labelField" width="10%">收據編號：</td>
																	<td class="dataField" colspan="3">
                                                                                                                                            <input type="text" class="form-control" size="30"
																		 id="OP_SEARCH_AC_RCNO" />
                                                                                                                                        </td>
																</tr>
																<tr>
																	<td colspan="4">
																		<button class="btn green btn-sm" type="button"
																			id="printBtn" name="printBtn">
																			<i class="fa fa-print"></i>&nbsp;列印
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
									<!-- END EXAMPLE TABLE PORTLET-->
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
	<script src="assets/js/OP07A02Q_01.js"></script>
	<script src="assets/js/ddlProcess.js"></script>
	<script src="assets/js/formProcess.js"></script>
	
</body>
</html>