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
      
    //列印EXCEL    
    $('#printBtn').click(function () {
        var msg = "";
        var bool =true;
        var nowDate = new Date();
        nowDate = getROCDateSlash(nowDate);
        if( $("input[name='suspectType']:checked").val() == "4" ){
             //受理拾得日期及公告期滿日期至少輸入一個條件，且日期區間不得超過一年
            if($('#OP_CASE_DATE_S').val() == "" &&  $('#OP_CASE_DATE_E').val() == "" && $('#OP_SEARCH_AC_DATE_S').val() == "" && $('#OP_SEARCH_AC_DATE_E').val() == "" && $('#OP_SEARCH_PU_DATE_S').val() == "" && $('#OP_SEARCH_PU_DATE_E').val() == ""){
                msg += '公告期滿日期、受理日期及拾得日期至少輸入一個條件<br/>';
                bool = false;
            }else{
                if( $('#OP_SEARCH_AC_DATE_S').val() == "" && $('#OP_SEARCH_AC_DATE_E').val() == "")
                {
                    if( ($('#OP_CASE_DATE_S').val() == "") && ($('#OP_CASE_DATE_E').val() != "") ){
                        msg += '公告期滿日期起日不可為空<br/>';
                        bool = false;
                    }else if( ($('#OP_CASE_DATE_S').val() != "") && ($('#OP_CASE_DATE_E').val() == "") ){
                        msg += '公告期滿日期迄日不可為空<br/>';
                        bool = false;
                    }else{
                        if( ($('#OP_CASE_DATE_S').val() < $('#OP_CASE_DATE_E').val()) && compareOneYear( 'OP_CASE_DATE_S', 'OP_CASE_DATE_E' ) == false ){
                            msg += '公告期滿日期起日與公告期滿日期迄日不能超過一年<br/>';
                            bool = false;
                        }
                    }
                    if(($('#OP_SEARCH_PU_DATE_S').val() == "") && ($('#OP_SEARCH_PU_DATE_E').val() != "")){
                        msg += '拾得日期起日不可為空<br/>';
                        bool = false;
                    }else if( ($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() == "")  ){
                        msg += '拾得日期迄日不可為空<br/>';
                        bool = false;
                    }else{
                        if(($('#OP_SEARCH_PU_DATE_S').val() < $('#OP_SEARCH_PU_DATE_E').val()) && compareOneYear( 'OP_SEARCH_PU_DATE_S', 'OP_SEARCH_PU_DATE_E' ) == false ){
                            msg += '拾得日期起日與拾得日期迄日不能超過一年<br/>';
                            bool = false;
                        }
                    }
                }else{
                    if( ($('#OP_SEARCH_AC_DATE_S').val() == "") && ($('#OP_SEARCH_AC_DATE_E').val() != "") ){
                        msg += '受理日期起日不可為空<br/>';
                        bool = false;
                    }else if( ($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() == "") ){
                        msg += '受理日期迄日不可為空<br/>';
                        bool = false;
                    }else{
                        if( ($('#OP_SEARCH_AC_DATE_S').val() < $('#OP_SEARCH_AC_DATE_E').val()) && compareOneYear( 'OP_SEARCH_AC_DATE_S', 'OP_SEARCH_AC_DATE_E' ) == false ){
                            msg += '受理日期起日與受理日期迄日不能超過一年<br/>';
                            bool = false;
                        }
                    }
                }
            }
            //公告期滿日期起迄
            if( ($('#OP_CASE_DATE_S').val() != "") && ($('#OP_CASE_DATE_E').val() != "") ){
                if (($('#OP_CASE_DATE_S').val() > $('#OP_CASE_DATE_E').val())){
                    msg += '公告期滿日期迄日不可小於公告期滿日期起日<br/>';
                    bool = false;
                }
            }
            if( getADDate($('#OP_CASE_DATE_S').val().substring(0,9)) >getADDate ( nowDate) ){
                msg += '公告期滿日期起日不可大於系統日期時間<br/>';
                bool = false;
            }
            if( getADDate($('#OP_CASE_DATE_E').val().substring(0,9)) >getADDate ( nowDate) ){
                msg += '公告期滿日期迄日不可大於系統日期時間<br/>';
                bool = false;
            }
        }else{
            //受理日期、公告期滿日期及拾得日期至少輸入一個條件，且日期區間不得超過一年
            if($('#OP_CASE_DATE').val() == "" && $('#OP_SEARCH_AC_DATE_S').val() == "" && $('#OP_SEARCH_AC_DATE_E').val() == "" && $('#OP_SEARCH_PU_DATE_S').val() == "" && $('#OP_SEARCH_PU_DATE_E').val() == ""){
                msg += '公告期滿日期、受理日期及拾得日期至少輸入一個條件<br/>';
                bool = false;
            }else{
                if( $('#OP_SEARCH_AC_DATE_S').val() == "" && $('#OP_SEARCH_AC_DATE_E').val() == "")
                {
                    if(($('#OP_SEARCH_PU_DATE_S').val() == "") && ($('#OP_SEARCH_PU_DATE_E').val() != "")){
                        msg += '拾得日期起日不可為空<br/>';
                        bool = false;
                    }else if( ($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() == "")  ){
                        msg += '拾得日期迄日不可為空<br/>';
                        bool = false;
                    }else{
                        if(($('#OP_SEARCH_PU_DATE_S').val() < $('#OP_SEARCH_PU_DATE_E').val()) && compareOneYear( 'OP_SEARCH_PU_DATE_S', 'OP_SEARCH_PU_DATE_E' ) == false ){
                            msg += '拾得日期起日與拾得日期迄日不能超過一年<br/>';
                            bool = false;
                        }
                    }
                }else{
                    if( ($('#OP_SEARCH_AC_DATE_S').val() == "") && ($('#OP_SEARCH_AC_DATE_E').val() != "") ){
                        msg += '受理日期起日不可為空<br/>';
                        bool = false;
                    }else if( ($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() == "") ){
                        msg += '受理日期迄日不可為空<br/>';
                        bool = false;
                    }else{
                        if( ($('#OP_SEARCH_AC_DATE_S').val() < $('#OP_SEARCH_AC_DATE_E').val()) && compareOneYear( 'OP_SEARCH_AC_DATE_S', 'OP_SEARCH_AC_DATE_E' ) == false ){
                            msg += '受理日期起日與受理日期迄日不能超過一年<br/>';
                            bool = false;
                        }
                    }
                }
            }
            if( getADDate($('#OP_CASE_DATE').val().substring(0,9)) >getADDate ( nowDate) ){
                msg += '公告期滿日期不可大於系統日期時間<br/>';
                bool = false;
            }
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
       
        if (msg != "") {
            $.alert.open({
                type: 'warning',
                content: msg
            });
            return;
        }
        if (bool){
            if($('input[name=suspectType]:checked').val() === "4"){
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP07A0502Q.xls',
                    reportName: 'oP07A05Q_02.xlsx',
                    newReportName: '拾得遺失物公告期滿案件清冊.xlsx',
                    OP_AC_D_UNIT_CD: $('#OP_Search_unitLevel2').val(),
                    OP_AC_B_UNIT_CD: $('#OP_Search_unitLevel3').val(),
                    OP_AC_UNIT_CD: $('#OP_Search_unitLevel4').val(),
                    OP_AN_DATE_END_S:$('#OP_CASE_DATE_S').val(),
                    OP_AN_DATE_END_E:$('#OP_CASE_DATE_E').val(),
                    OP_AC_DATE_S:$('#OP_SEARCH_AC_DATE_S').val(),
                    OP_AC_DATE_E:$('#OP_SEARCH_AC_DATE_E').val(),
                    OP_PU_DATE_S:$('#OP_SEARCH_PU_DATE_S').val(),
                    OP_PU_DATE_E:$('#OP_SEARCH_PU_DATE_E').val(),
                    includeYN:$('input[name=includeYN]:checked').val(),
                    PRINT_TYPE:$('input[name=suspectType]:checked').val(),
                    lockType : 'A'
                });
            }else{
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP07A0501Q.xls',
                    reportName: 'oP07A05Q_01.xlsx',
                    newReportName: '拾得遺失物公告期滿案件清冊.xlsx',
                    OP_AC_D_UNIT_CD: $('#OP_Search_unitLevel2').val(),
                    OP_AC_B_UNIT_CD: $('#OP_Search_unitLevel3').val(),
                    OP_AC_UNIT_CD: $('#OP_Search_unitLevel4').val(),
                    OP_AN_DATE:$('#OP_CASE_DATE').val(),
                    OP_AC_DATE_S:$('#OP_SEARCH_AC_DATE_S').val(),
                    OP_AC_DATE_E:$('#OP_SEARCH_AC_DATE_E').val(),
                    OP_PU_DATE_S:$('#OP_SEARCH_PU_DATE_S').val(),
                    OP_PU_DATE_E:$('#OP_SEARCH_PU_DATE_E').val(),
                    includeYN:$('input[name=includeYN]:checked').val(),
                    PRINT_TYPE:$('input[name=suspectType]:checked').val(),
                    lockType : 'A'
                });
            }
            
        }
    });
    
    $("input[name='suspectType']").change(function() {
        if ($("input[name='suspectType']:checked").val() == "1"){
            $('#OP_CASE_DATE_1').show();
            $('#OP_CASE_DATE_2').hide();
        }else if($("input[name='suspectType']:checked").val() == "2"){
            $('#OP_CASE_DATE_1').show();
            $('#OP_CASE_DATE_2').hide();
        }else if($("input[name='suspectType']:checked").val() == "3"){
            $('#OP_CASE_DATE_1').show();
            $('#OP_CASE_DATE_2').hide();
        }else if($("input[name='suspectType']:checked").val() == "4"){
            $('#OP_CASE_DATE_1').hide();
            $('#OP_CASE_DATE_2').show();
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

