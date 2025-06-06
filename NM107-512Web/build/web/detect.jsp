<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html>

<head>    
    <title>拾得遺失物管理系統 - 內政部警政署</title>
    <jsp:include page="Head.jsp" />
<!--    <link rel="stylesheet" href="assets/css/default.css" type="text/css" title="default"/>    -->
    <styleSwitcher cat="font"></styleSwitcher>    
</head>

<body>
            
        <section id="MainContent">
                <div class="container" style="padding-left: 0px;padding-right: 0px;">
                        <div class="panel panel-default">                               
                                <!-- YOUR CONTENT-->
                                <div class="panel-heading">
					<h2 class="panel-title">說明:</h2>
				</div>
				<div class="panel-body" style="background: #fdf9ed;">
                                        <ol>
                                                <li>WEB到AP<br>
                                                        &nbsp;&nbsp;NM107-512Web service success 系統正常。<br>
                                                </li>
                                                <li>AP到DB<br>
                                                    <label id="DBTIME"</label>
                                                </li>
                                        </ol>
				</div>
                        </div>                        
                </div>                
        </section>       
        <jsp:include page="Footer.jsp" />
        <script src="assets/js/detect.js"></script> 
</body>

</html>
