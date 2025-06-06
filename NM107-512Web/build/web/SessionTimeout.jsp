<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<title>拾得遺失物管理系統</title>
<!--<link rel="shortcut icon" href="favicon.ico" />-->

<!-- ALERT -->
<link href="assets/plugins/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link href="assets/css/components.css" rel="stylesheet" type="text/css" />
<link href="assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="assets/css/layout.css" rel="stylesheet" type="text/css" />
<link href="assets/css/themes/default.css" rel="stylesheet"
	type="text/css" id="style_color" />
<link href="assets/css/custom.css" rel="stylesheet" type="text/css" />
<style type="text/css">

.loginLable {
	font-size: 22px;
	font-family: "微軟正黑體", "新細明體", "Trebuchet MS";
	color: #64B9E0;
	font-weight: bold;
}

.loginText {
	font-size: 22px;
	font-family: "微軟正黑體", "新細明體", "Trebuchet MS";
	border: 1px solid #64B9E0;
	ling-height: 300%;
	color: #084860;
	width: 250px;
}

.loginLableSmall {
	font-size: 16px;
	font-family: "微軟正黑體", "新細明體", "Trebuchet MS";
	color: #555555;
	font-weight: bold;
}

.forgetLable {
	font-size: 18px;
	font-family: "微軟正黑體", "新細明體", "Trebuchet MS";
	color: #64B9E0;
	font-weight: bold;
}

.accountLable {
	font-size: 16px;
	font-family: "微軟正黑體", "新細明體", "Trebuchet MS";
	color: #64B9E0;
	font-weight: bold;
}

.accountText {
	font-size: 16px;
	font-family: "微軟正黑體", "新細明體", "Trebuchet MS";
	border: 1px solid #64B9E0;
	ling-height: 300%;
	color: #084860;
}

A {
	text-decoration: underline;
	color: #64B9E0;
}

H4 {
	border-bottom: 1px solid #CCC;
}
</style>

</head>

<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="loginL"></td>
			<td class="login" width="1024" valign="top">
				<div id="divMain"
					style="position: relative; top: 0px; left: 0px; overflow: hidden;">
					<!-- login 畫面 -->
					<div id="divlogin"
						style="position: absolute; top: 370px; left: 270px">
						<table border="0" cellpadding="8" cellspacing="0"
							style="border-collapse: separate; border-spacing: 12px;">
							<tr>
								<td class="loginLable"><img src="assets/img/warning32.png"
									border="0" align="baseline" />&nbsp;很抱歉，您的網頁連線已經逾時！</td>
							</tr>
							<tr>
								<td class="loginLableSmall">因為資訊安全考量，若是畫面閒置超過40分鐘，就會被伺服器當作連線逾時，而被自動登出。</td>
							</tr>
							<tr>
								<td class="loginLableSmall">若要繼續使用本系統，請返回警政知識聯網，再重新登入本系統。</td>
							</tr>
							<tr>
								<td><button type="button" class="btn green btn-sm"
										style="font-size: 16px; font-family: '微軟正黑體', '新細明體';"
										onclick="self.close(); window.close();top.window.close();return false;">返回警政知識聯網</button></td>
							</tr>
						</table>
					</div>


				</div>
			</td>
			<td class="loginR"></td>
		</tr>
	</table>
	<!--<script src="assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>-->
        <script src="assets/plugins/jquery-1.11.1.min.js" type="text/javascript"></script>
	<script type="text/javascript">

$(document).ready(function(){
	
	//設定全螢幕
	$('.loginL').css({
		width : ($(window).width() - 1024) / 2
	});
	$('.loginR').css({
		width : ($(window).width() - 1024) / 2
	});
	$('div#divMain').css({
		height : $(window).height()
	});	
	//宣告視窗大小更改事件
	$(window).resize(function() {
		//設定全螢幕
		$('.loginL').css({
			width : ($(window).width() - 1024) / 2
		});
		$('.loginR').css({
			width : ($(window).width() - 1024) / 2
		});
	});
	$(document).resize(function() {
		//設定全螢幕
		$('.loginL').css({
			width : ($(window).width() - 1024) / 2
		});
		$('.loginR').css({
			width : ($(window).width() - 1024) / 2
		});
	});
});
</script>
</body>
</html>
