var strPermissionData;

$(document).ready(function() {
        time(1);
	$('#menu07').addClass("active");
	$('#menu07span').addClass("selected");
	init();
});

function init() {
	strPermissionData = getUserRole();
	if (strPermissionData["RolePermission"] == ''){
		$.alert.open('error', '遺失使用者資訊，請重新登入');
		window.close();
	}
	// 帶入單位下拉式選單選項
	bindUNITDDL('2', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', 'ALL');
	initEvent();
}

function initEvent() {
	//帶入預設值
	initDefaultQS();
        //按鍵事件區
	registButton();
}

// 註冊事件區---Start
function registButton() {
        $('#printBtn').click(function() {
            var msg = "";
            var bool =true;
            var nowDate = new Date();
            nowDate = getROCDateSlash(nowDate);
            //設定要列印之已受理未公告超過天/月數
            /*if( $('#OP_AC_NO_AN').val() != "" &&$('#OP_AC_NO_AN_CMB').val() == ""){
                msg += '設定要列印之已受理未公告超過天/月數必須選取，或不輸入天/月數<br/>';
                bool = false;
            }
            if( $('#OP_AC_NO_AN_CMB').val() != "" && $('#OP_AC_NO_AN').val() == ""){
                msg += '設定要列印之已受理未公告超過天/月數必須輸入，或將天/月選取為空<br/>';
                bool = false;
            }
            //設定要列印之公告期滿未結案超過天/月數
            if( $('#OP_AN_NO_CLOSE').val() != "" && $('#OP_AN_NO_CLOSE_CMB').val() == ""){
                msg += '設定要列印之公告期滿未結案超過天/月數必須選取，或不輸入天/月數<br/>';
                bool = false;
            }
            if( $('#OP_AN_NO_CLOSE_CMB').val() != "" && $('#OP_AN_NO_CLOSE').val() == ""){
                msg += '設定要列印之公告期滿未結案超過天/月數必須輸入，或將天/月選取為空<br/>';
                bool = false;
            }*/
            //受理日期起迄
            if( ($('#OP_AC_DATE_S').val() != "") && ($('#OP_AC_DATE_E').val() != "") ){
                if (($('#OP_AC_DATE_S').val() > $('#OP_AC_DATE_E').val())){
                    msg += '受理日期迄日不可小於受理日期起日<br/>';
                    bool = false;
                }
            }
            if( getADDate($('#OP_AC_DATE_S').val().substring(0,9)) >getADDate ( nowDate) ){
                msg += '受理日期起日不可大於系統日期時間<br/>';
                bool = false;
            }
            if( getADDate($('#OP_AC_DATE_E').val().substring(0,9)) >getADDate ( nowDate) ){
                msg += '受理日期迄日不可大於系統日期時間<br/>';
                bool = false;
            }
            //受理拾得日期、拾得日期及發還日期至少輸入一個條件，且日期區間不得超過一年
        if($('#OP_AC_NO_AN').val() == ""&& $('#OP_AC_NO_AN_CMB').val() == "" && $('#OP_AN_NO_CLOSE').val() == "" && $('#OP_AN_NO_CLOSE_CMB').val() == "" && $('#OP_AC_DATE_S').val() == "" && $('#OP_AC_DATE_E').val() == "" ){
                msg += '設定要列印之已受理未公告超過天/月數、設定要列印之公告期滿未結案超過天/月數或受理日期至少輸入一個條件<br/>';
                bool = false;
        }else{
            if( $('#OP_AC_DATE_S').val() == "" && ($('#OP_AC_DATE_E').val() == "") )
            {
                if( ($('#OP_AC_NO_AN').val() == "") && ($('#OP_AC_NO_AN_CMB').val() != "") ){
                    msg += '設定要列印之已受理未公告超過天/月數不可為空<br/>';
                    bool = false;
                }if( ($('#OP_AC_NO_AN').val() != "") && ($('#OP_AC_NO_AN_CMB').val() == "")){
                    msg += '請選取要列印之已受理未公告超過天或月<br/>';
                    bool = false;
                }if( ($('#OP_AN_NO_CLOSE').val() == "") && ($('#OP_AN_NO_CLOSE_CMB').val() != "") ){
                    msg += '設定要列印之公告期滿未結案超過天/月數不可為空<br/>';
                    bool = false;
                }if ( ($('#OP_AN_NO_CLOSE').val() != "") && ($('#OP_AN_NO_CLOSE_CMB').val() == "") ){
                    msg += '請選取要列印之公告期滿未結案超過天或月<br/>';
                    bool = false;
                }
            }else{
                if( ($('#OP_AC_DATE_S').val() == "") && ($('#OP_AC_DATE_E').val() != "") ){
                    msg += '受理日期起日不可為空<br/>';
                    bool = false;
                }else if( ($('#OP_AC_DATE_S').val() != "") && ($('#OP_AC_DATE_E').val() == "") ){
                    msg += '受理日期迄日不可為空<br/>';
                    bool = false;
                }else{
                    if( ($('#OP_AC_DATE_S').val() < $('#OP_AC_DATE_E').val()) && compareOneYear( 'OP_AC_DATE_S', 'OP_AC_DATE_E' ) == false ){
                        msg += '受理日期起日與受理日期迄日不能超過一年<br/>';
                        bool = false;
                    }
                //設定要列印之已受理未公告超過天/月數
                }if( $('#OP_AC_NO_AN').val() != "" &&$('#OP_AC_NO_AN_CMB').val() == ""){
                    msg += '設定要列印之已受理未公告超過天/月數必須選取，或不輸入天/月數<br/>';
                    bool = false;
                }if( $('#OP_AC_NO_AN_CMB').val() != "" && $('#OP_AC_NO_AN').val() == ""){
                    msg += '設定要列印之已受理未公告超過天/月數必須輸入，或將天/月選取為空<br/>';
                    bool = false;
                //設定要列印之公告期滿未結案超過天/月數
                }if( $('#OP_AN_NO_CLOSE').val() != "" && $('#OP_AN_NO_CLOSE_CMB').val() == ""){
                    msg += '設定要列印之公告期滿未結案超過天/月數必須選取，或不輸入天/月數<br/>';
                    bool = false;
                }if( $('#OP_AN_NO_CLOSE_CMB').val() != "" && $('#OP_AN_NO_CLOSE').val() == ""){
                    msg += '設定要列印之公告期滿未結案超過天/月數必須輸入，或將天/月選取為空<br/>';
                    bool = false;
                }
            }
        }
            if (msg != "") {
                $.alert.open({
                    type: 'warning',
                    content: msg
                });
                return;
            }
            if (bool){
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP07A09Q.xls',
                    reportName: 'OP07A09Q.xlsx',
                    newReportName: '拾得遺失物處理情形統計表清冊.xlsx',
                    OP_AC_D_UNIT_CD: $('#OP_Search_unitLevel2').val(),
                    OP_AC_B_UNIT_CD: $('#OP_Search_unitLevel3').val(),
                    OP_AC_UNIT_CD: $('#OP_Search_unitLevel4').val(),
                    OP_AC_NO_AN:$('#OP_AC_NO_AN').val(),
                    OP_AC_NO_AN_CMB:$('#OP_AC_NO_AN_CMB').val(),
                    OP_AN_NO_CLOSE:$('#OP_AN_NO_CLOSE').val(),
                    OP_AN_NO_CLOSE_CMB:$('#OP_AN_NO_CLOSE_CMB').val(),
                    OP_AC_DATE_S:$('#OP_AC_DATE_S').val(),
                    OP_AC_DATE_E:$('#OP_AC_DATE_E').val(),
                    includeYN:$('input[name=includeYN]:checked').val(),
                    lockType : 'A'
                });
            }
	});
	
}

function initDefaultQS() {

	// 帶入登入者單位
	if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1){
		
	}else if (strPermissionData["RolePermission"] == '1' ){
		$("#OP_Search_unitLevel2").prop('disabled', true);
		$("#OP_Search_unitLevel3").prop('disabled', true);
		$("#OP_Search_unitLevel4").prop('disabled', true);
	}else if (strPermissionData["RolePermission"] == '2' ){
		$("#OP_Search_unitLevel2").prop('disabled', true);
		$("#OP_Search_unitLevel3").prop('disabled', true);
	}else if (strPermissionData["RolePermission"] == '3' ){
		$("#OP_Search_unitLevel2").prop('disabled', true);
	}
	// 帶入登入者單位
        $('#OP_Search_unitLevel2').val(strPermissionData['UNITLEVEL1']);
        bindUNITDDL('3', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', '');
        if (strPermissionData["RolePermission"] == '3' ){
            $('#OP_Search_unitLevel3').val('');
	}else{
            $('#OP_Search_unitLevel3').val(strPermissionData['UNITLEVEL2']);
        }
        bindUNITDDL('4', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', '');
        if ( strPermissionData["RolePermission"] == '2' || strPermissionData["RolePermission"] == '3' ){
            $('#OP_Search_unitLevel4').val('');
	}else{
            $('#OP_Search_unitLevel4').val(strPermissionData['UNITLEVEL3']);
        }
}