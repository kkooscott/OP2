<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
<head>
    <% ResourceBundle resourceBundle = ResourceBundle.getBundle("npa"); %>
    <style type="text/css">
        html, body, #map-canvas { height: 100%; margin: 0; padding: 0;}
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <jsp:include page="Head.jsp" />
</head>
<body class="page-header-fixed page-full-width">
	<!-- BEGIN HEADER -->
	<jsp:include page="Menu.jsp" />
	<!-- END HEADER -->

	<div id="map-canvas"></div>

        <div id="toolboxdialog" title="Dialog Title" style="display: none">
                <!--工具箱頁籤 -->
                <div id="toolboxtabs">
                    <ul>
                        <li><a href="#toolboxaccordion1">受理單位</a></li>
                    </ul>                                                                                                                    
                    <div id="toolboxaccordion1">
                            <!--<h3>&nbsp;&nbsp;&nbsp;&nbsp;地址</h3>-->
                            <!--<h3>&nbsp;&nbsp;單位</h3>-->                                                
                            <div class="row">
                                    <div class="col-sm-12 col-xs-12">
                                            <input type="checkbox" id="includeLower" name="includeLower" style="display: none"/>
                                            <!--<input type="checkbox" id="includeLower" name="includeLower" checked />含下屬<br>-->
                                            <select class="form-control" id="OP_AC_D_UNIT_CD" name="unitLevel2" data-width="180px" data-size="8"
                                                    onchange="bindUNITDDL('3', 'OP_AC_D_UNIT_CD', 'OP_AC_B_UNIT_CD', 'OP_AC_UNIT_CD', '');"></select><br>
                                            <select class="form-control" id="OP_AC_B_UNIT_CD" name="unitLevel3" data-width="180px" data-size="8"
                                                    onchange="bindUNITDDL('4', 'OP_AC_D_UNIT_CD', 'OP_AC_B_UNIT_CD', 'OP_AC_UNIT_CD', '');"></select><br>
                                            <select class="form-control" id="OP_AC_UNIT_CD" name="unitLevel4" data-width="180px" data-size="8"></select>
                                            <br>
                                            <div align="center">
                                                    <button type="submit" class="btn-xs btn-default" id="queryBtn">
                                                            <i class="fa fa-search"></i>&nbsp;查詢</button>
                                                    <button type="submit" class="btn-xs btn-default" id="clearBtn">
                                                            <i class="fa fa-eraser"></i>&nbsp;清除</button>
                                            </div>
                                    </div>
                            </div>                                                                                  
                    </div>
                    <!-- end of toolboxaccordion -->                                                
                </div>
                <!-- end of toolboxtabs -->
        </div>
        
	<jsp:include page="Footer.jsp" />

	<!-- 每個頁面各自使用的js -->
        <script src="assets/js/jquery.blockUI.js"></script>
        <script src="https://maps.google.com/maps/api/js?v=3&client=gme-nationalpoliceagency1&sensor=false&libraries=drawing,geometry,places,visualization&signature=f4SOmDWlkIvvMUkwNVu0Es9CClU=" type="text/javascript"></script>
	<script src="assets/js/OP09A05Q_01.js"></script>
        <script src="assets/js/ddlProcess.js"></script>
	<script src="assets/js/formProcess.js"></script>
</body>
</html>