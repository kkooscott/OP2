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
      
    //列印doc  
    $('#printBtn').click(function () {
        var msg = "";
        var bool =true;
        var nowDate = new Date();
        nowDate = getROCDateSlash(nowDate);
        //拾得日期起迄
        if( ($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() != "") ){
            if (($('#OP_SEARCH_PU_DATE_S').val() > $('#OP_SEARCH_PU_DATE_E').val())){
                msg += '拾得日期迄日不可小於拾得日期起日<br/>';
		bool = false;
            }
        }
        if( getADDate($('#OP_SEARCH_PU_DATE_S').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '拾得日期起日不可大於系統日期時間<br/>';
            bool = false;
        }
        if( getADDate($('#OP_SEARCH_PU_DATE_E').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '拾得日期迄日不可大於系統日期時間<br/>';
            bool = false;
        }
        //受理日期起迄
        if( ($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() != "") ){
            if (($('#OP_SEARCH_AC_DATE_S').val() > $('#OP_SEARCH_AC_DATE_E').val())){
		msg += '受理日期迄日不可小於受理日期起日<br/>';
		bool = false;
            }
        }
        if( getADDate($('#OP_SEARCH_AC_DATE_S').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '受理日期起日不可大於系統日期時間<br/>';
            bool = false;
        }
        if( getADDate($('#OP_SEARCH_AC_DATE_E').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '受理日期迄日不可大於系統日期時間<br/>';
            bool = false;
        }
        //通知拾得人領回日期起迄
        if( ($('#OP_SEARCH_NTC_PUPO_DT_S').val() != "") && ($('#OP_SEARCH_NTC_PUPO_DT_E').val() != "") ){
            if (($('#OP_SEARCH_NTC_PUPO_DT_S').val() > $('#OP_SEARCH_NTC_PUPO_DT_E').val())){
		msg += '通知拾得人領回日期迄日不可小於通知拾得人領回日期起日<br/>';
		bool = false;
            }
        }
        if( getADDate($('#OP_SEARCH_NTC_PUPO_DT_S').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '通知拾得人領回日期起日不可大於系統日期時間<br/>';
            bool = false;
        }
        if( getADDate($('#OP_SEARCH_NTC_PUPO_DT_E').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '通知拾得人領回日期迄日不可大於系統日期時間<br/>';
            bool = false;
        }
        //收據編號
//        if( $('#OP_AC_RCNO').val() != "" ){
//            if( $('#OP_AC_RCNO').val().length > 17 ){
//                msg += '收據編號欄位長度超過17碼<br/>';
//                bool = false;
//            }
//        }
        if (msg != "") {
            $.alert.open({
                type: 'warning',
                content: msg
            });
            return;
        }
        if (bool){
            downloadReport("ReportServlet", {
                ajaxAction: 'OP07A15Q.doc',
                reportName: 'oP07A15Q.doc,oP07A16Q.doc',
                newReportName: '未經認領及領回拾得物移交地方自治團體清冊.doc,未經認領及領回拾得物移交地方自治團體清冊.doc',
                OP_AC_D_UNIT_CD: $('#OP_Search_unitLevel2').val(),
                OP_AC_B_UNIT_CD: $('#OP_Search_unitLevel3').val(),
                OP_AC_UNIT_CD: $('#OP_Search_unitLevel4').val(),
                OP_PU_DATE_S:$('#OP_SEARCH_PU_DATE_S').val(),
                OP_PU_DATE_E:$('#OP_SEARCH_PU_DATE_E').val(),
                OP_AC_DATE_S:$('#OP_SEARCH_AC_DATE_S').val(),
                OP_AC_DATE_E:$('#OP_SEARCH_AC_DATE_E').val(),
                OP_NTC_PUPO_DT_S:$('#OP_SEARCH_NTC_PUPO_DT_S').val(),
                OP_NTC_PUPO_DT_E:$('#OP_SEARCH_NTC_PUPO_DT_E').val(),
                includeYN:$('input[name=includeYN]:checked').val(),
//                OP_AC_RCNO:$('#OP_AC_RCNO').val(),
                OP_TYPE_CD:$('#OP_SEARCH_TYPE_CD').val(),
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

