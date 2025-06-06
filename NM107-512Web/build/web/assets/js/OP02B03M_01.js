var ajaxActionUrl = 'PuMaintainServlet';
var strPermissionData;

//CLAIM_CHECK---START
function checkClaimList(inForm) {
    Validator.init(inForm);
    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    //填表日期
    if( getADDate($('#IPUCLAIM_OP_FM_DATE').val().substring(0,9)) >getADDate ( nowDate) ){
        Validator.setMessage("欄位 [ 填表日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IPUCLAIM_OP_FM_DATE");
    }
    Validator.required('IPUCLAIM_OP_FM_DATE', '填表日期');
    
    //姓名
    Validator.required('IPUCLAIM_OP_PUCP_NAME', '認領人姓名');
    //證號
    if( $("#IPUCLAIM_OP_PUCP_NAT_CD").val() == "035" && $("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "2" ){
        Validator.setMessage("欄位 [ 證號 ] 為\"其他證號\"時，[ 國籍 ]不得為中華民國國籍 !!");
        Validator.setBGColor("IPUCLAIM_OP_PUCP_NAT_CD", "IPUCLAIM_OP_PUCP_IDN_TP");
    }else if( $("#IPUCLAIM_OP_PUCP_NAT_CD").val() != "" && $("#IPUCLAIM_OP_PUCP_NAT_CD").val() != "035" && $("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "1" ){           
        Validator.setMessage("欄位 [ 證號 ] 為\"身分證號\"時，[ 國籍 ]不得為他國國籍 !!");
        Validator.setBGColor("IPUCLAIM_OP_PUCP_NAT_CD", "IPUCLAIM_OP_PUCP_IDN_TP");
    }else if( $("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "1" ){
        Validator.checkID("IPUCLAIM_OP_PUCP_IDN",true,"身分證號");
    }else if( $("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "3" ){
        Validator.checkLength("IPUCLAIM_OP_PUCP_IDN",false,"身分證號",20);
    }else{
        Validator.checkLength("IPUCLAIM_OP_PUCP_IDN",true,"身分證號",20);
    }
    //電話
    Validator.required('IPUCLAIM_OP_PUCP_PHONENO', '認領人電話');
    //地址
    var addrType = $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD']:checked").val();
    if ( addrType == '1' ){ //一般地址
        Validator.required('IPUCLAIM_OP_PUCP_CITY_CD', '認領人地址縣市');
        Validator.required('IPUCLAIM_OP_PUCP_TOWN_CD', '認領人地址鄉鎮');
//        Validator.required('IPUCLAIM_OP_PUCP_VILLAGE_CD', '認領人地址村里');
        Validator.required('IPUCLAIM_OP_PUCP_ROAD', '認領人地址');
    }else if ( addrType == '9' ){ //自由輸入
        Validator.required('IPUCLAIM_OP_PUCP_ADDR_OTH', '認領人地址');
    }
     
    //檢查一下出生年月日民國7碼或西元8碼都應該要在0-9之間
    var pattern7 = new RegExp("[0-9]{7}");
    var pattern8 = new RegExp("[0-9]{8}");

     //出生日期 20241007
    if ($('#IPUCLAIM_OP_PUCP_BIRTHDT').val() !== "") {
        if ($('#IPUCLAIM_OP_PUCP_BEFROC').val() === "0" || $('#IPUCLAIM_OP_PUCP_BEFROC').val() === "1") {
            var lastDay = new Date(Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring(0, 3)) + 1911, Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 4, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2)), 0).getDate();
            //console.log("出生日期__IF 前"+$('#IPUCLAIM_OP_PUCP_BIRTHDT').val() );
            if ($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length !== 7) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期格式(YYYMMDD)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");
            }else if ( !(pattern7.test(  $('#IPUCLAIM_OP_PUCP_BIRTHDT').val() )) ) {
                //console.log("出生日期__IF 內"+$('#IPUCLAIM_OP_PUCP_BIRTHDT').val() );
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的數字日期格式(YYYMMDD)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");           
            } else if (Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 4, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2)) < 1 || Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 4, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2)) > 12) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的月份(1~12月)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");
            } else if (Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length)) < 1 || Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length)) > lastDay) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期(1~" + lastDay + "日)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");
            }
        } else if ($('#IPUCLAIM_OP_PUCP_BEFROC').val() === "2") {
            var lastDay = new Date(Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring(0, 4)), Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring(4, 6)), 0).getDate();
            if ($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length !== 8) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期格式(YYYYMMDD)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");
            }else if ( !(pattern8.test(  $('#IPUCLAIM_OP_PUCP_BIRTHDT').val() )) ){
                //console.log("出生日期__IF 內"+$('#IPUCLAIM_OP_PUCP_BIRTHDT').val() );
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的數字日期格式(YYYYMMDD)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");           
            } else if (Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 4, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2)) < 1 || Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 4, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2)) > 12) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的月份(1~12月)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");
            } else if (Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length)) < 1 || Number($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().substring($('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length - 2, $('#IPUCLAIM_OP_PUCP_BIRTHDT').val().length)) > lastDay) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期(1~" + lastDay + "日)！");
                Validator.setBGColor("IPUCLAIM_OP_PUCP_BIRTHDT");
            }
        }
    }
    //電子郵件
    if( $('#IPUCLAIM_OP_PUCP_EMAIL').val() != '' ){
        Validator.checkMail("IPUCLAIM_OP_PUCP_EMAIL",false,"電子郵件");
    }
    //遺失日期
    if( getADDate($('#IPUCLAIM_OP_LOST_DATE').val().substring(0,9)) > getADDate ( nowDate) ){
        Validator.setMessage("欄位 [ 遺失日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IPUCLAIM_OP_LOST_DATE");
    }
    
    Validator.required('IPUCLAIM_OP_LOST_DATE', '遺失日期');
    Validator.required('IPUCLAIM_OP_LOST_TIME', '遺失時間');
    //新系統上線日期前的資料不用檢核
    if( getADDate( $('#IPUCLAIM_OP_FM_DATE').val() ) >= getADDate ( getOnlineDay() ) ){
        Validator.required('IPUCLAIM_OP_LOST_CITY_CD', '遺失地點縣市');
    }
    Validator.required('IPUCLAIM_OP_LOST_PLACE', '遺失地點地址');
    
    if ( Validator.isValid() )
    	return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//CLAIM_CHECK---END

//邏輯判斷---START
//身分證判斷性別
function genderClaim(){
    if( $('#IPUCLAIM_OP_PUCP_IDN_TP').val() == "1" ){
        $('#IPUCLAIM_OP_PUCP_GENDER').attr('disabled',true);
        if( $('#IPUCLAIM_OP_PUCP_IDN').val().charAt(1) == "1" ){
            $('#IPUCLAIM_OP_PUCP_GENDER').val("1");
        }
        else if( $('#IPUCLAIM_OP_PUCP_IDN').val().charAt(1) == "2" ){
            $('#IPUCLAIM_OP_PUCP_GENDER').val("2");
        }
    }
    else{
        if( $('#IPUCLAIM_OP_PUCP_IDN_TP').val() == "3" ){
            $('#IPUCLAIM_OP_PUCP_IDN').val("");
        }                    
        $('#IPUCLAIM_OP_PUCP_GENDER').attr('disabled',false);
    }
}
//邏輯判斷---END

//顯示認領人---START
function showClaimList( OP_BASIC_SEQ_NO ) {
	var gridId = "#gridList_4";
	var pagerId = "#pageList_4";
	
	$(gridId).jqGrid('GridUnload');        
	$(gridId).jqGrid({
		url : ajaxActionUrl,
		mtype : "POST",                
		datatype : "json",
		postData : {
			ajaxAction : 'GetClaimList',
			'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO
		},
		height : "auto",
		autowidth : true,
		colNames : [ "收據號碼", "填表日期", "認領方式", "姓名", "證號", "遺失日期","遺失時間", "遺失地點", "是否為有認領權人", "OP_SEQ_NO", "TABLEFROM"],
		colModel : [ {
			name : 'OP_AC_RCNO',
			index : 'OP_AC_RCNO',
                        width: 60, 
                        align: 'left'
		}, {
			name : 'OP_FM_DATE',
			index : 'OP_FM_DATE',
                        width: 30, 
                        align: 'left'
		}, {
			name : 'OP_CLAIM_TP_NM',
			index : 'OP_CLAIM_TP_NM',
                        width: 30, 
                        align: 'left'
		}, {
			name : 'OP_PUCP_NAME',
			index : 'OP_PUCP_NAME',
                        width: 30, 
                        align: 'left'
		}, {
			name : 'OP_PUCP_IDN',
			index : 'OP_PUCP_IDN',
                        width: 40, 
                        align: 'left'
		}, {
			name : 'OP_LOST_DATE',
			index : 'OP_LOST_DATE',
                        width: 30, 
                        align: 'left'
		}, {
			name : 'OP_LOST_TIME',
			index : 'OP_LOST_TIME',
                        width: 30, 
                        align: 'left'
		}, {
			name : 'OP_LOST_PLACE',
			index : 'OP_LOST_PLACE',
                        width: 60, 
                        align: 'left'
		}, {
			name : 'OP_YN_LOSER',
			index : 'OP_YN_LOSER',
                        width: 30, 
                        align: 'left'
		}, {
			name : 'OP_SEQ_NO',
                        index : 'OP_SEQ_NO',
                        hidden : true
			// 欄位隱藏
		}, {
			name : 'TABLEFROM',
                        index : 'TABLEFROM',
                        hidden : true
			// 欄位隱藏
		} ],
		sortname : 'OP_CLAIM_TP_NM',
		sortorder : "DESC",
		viewrecords : true,
		gridview : true,
		pgbuttons : true,
		pginput : true,
		rowNum : 10,
		rowList : [ 5, 10, 20, 30 ],
		pager : pagerId,
		multiselect : false,
		loadonce : true,
		altRows : true,
		onCellSelect : function(row, col, content, event) {
			var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
			selectedColumnName = cm[col].name;
		},
		onSelectRow : function(id, status) {
			var row = $(gridId).jqGrid('getRowData', id);
			var OP_SEQ_NO = row.OP_SEQ_NO;
                        var TABLEFROM = row.TABLEFROM;
                        showClaimValue(OP_SEQ_NO, TABLEFROM);
		}
	});        
	$(gridId).jqGrid("clearGridData", true);
	$("#gridList_4").setGridWidth($(window).width() - 92);
	$(gridId).trigger("reloadGrid");
}
//顯示認領人---END

//設定認領人值---START
function showClaimValue(OP_SEQ_NO, TABLEFROM){
	var ajData = {
			'ajaxAction' : 'showClaimValue',
			'OP_SEQ_NO' : OP_SEQ_NO,
                        'TABLEFROM' : TABLEFROM
	};

	$.ajax({
		url : ajaxActionUrl,
		type : "POST",
		dataType : "json",
		data : ajData,
		success : function(data, textStatus, xhr) {
			var formData = data.formData[0];
			//文件資料
                        $('#IPUCLAIM_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
			$('#IPUCLAIM_OP_FM_DATE').val(formData['OP_FM_DATE']);
			//認領人基本資料
                        $('#IPUCLAIM_OP_PUCP_NAME').val(formData['OP_PUCP_NAME']);
                        $('#IPUCLAIM_OP_PUCP_RNAME').val(formData['OP_PUCP_RNAME']);
			$('#IPUCLAIM_OP_PUCP_IDN_TP').val(formData['OP_PUCP_IDN_TP']);
                        $('#IPUCLAIM_OP_PUCP_IDN').val(formData['OP_PUCP_IDN']);
			$('#IPUCLAIM_OP_PUCP_BEFROC').val(formData['OP_PUCP_BEFROC']);
			$('#IPUCLAIM_OP_PUCP_BIRTHDT').val(formData['OP_PUCP_BIRTHDT']);
			$('#IPUCLAIM_OP_PUCP_GENDER').val(formData['OP_PUCP_GENDER']);
                        $('#IPUCLAIM_OP_PUCP_NAT_CD').val(formData['OP_PUCP_NAT_CD']);
			$('#IPUCLAIM_OP_PUCP_PHONENO').val(formData['OP_PUCP_PHONENO']);
			$('#IPUCLAIM_OP_PUCP_ZIPCODE').val(formData['OP_PUCP_ZIPCODE']);
                        
                        if( formData['OP_PUCP_ADDR_TYPE_CD'] == "1"){
                            $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD'][value='1']").prop("checked", true);
                            $('#IPUCLAIM_normalPattern').show();
                            $('#IPUCLAIM_freePattern').hide();
                            $('#IPUCLAIM_GET_HH_DATA').show();
                            $('#IPUCLAIM_OP_PUCP_CITY_CD').val(formData['OP_PUCP_CITY_CD']);
                            bindCOUNTRYDDL('3', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');
                            $('#IPUCLAIM_OP_PUCP_TOWN_CD').val(formData['OP_PUCP_TOWN_CD']);
                            bindCOUNTRYDDL('4', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');
                            $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val(formData['OP_PUCP_VILLAGE_CD']);
                            $('#IPUCLAIM_OP_PUCP_LIN').val(formData['OP_PUCP_LIN']);
                            $('#IPUCLAIM_OP_PUCP_ROAD').val(formData['OP_PUCP_ROAD']);
                            $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val('');
                        }else{
                            $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD'][value='9']").prop("checked", true);
                            $('#IPUCLAIM_normalPattern').hide();
                            $('#IPUCLAIM_freePattern').show();
                            $('#IPUCLAIM_GET_HH_DATA').hide();
                            $('#IPUCLAIM_OP_PUCP_CITY_CD').val('');
                            $('#IPUCLAIM_OP_PUCP_TOWN_CD').val('');
                            $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val('');
                            $('#IPUCLAIM_OP_PUCP_LIN').val('');
                            $('#IPUCLAIM_OP_PUCP_ROAD').val('');
                            $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val(formData['OP_PUCP_ADDR_OTH']);
                        }
                        
                        $('#IPUCLAIM_OP_PUCP_EMAIL').val(formData['OP_PUCP_EMAIL']);
                        $('#IPUCLAIM_OP_CLAIM_TP_CD').val(formData['OP_CLAIM_TP_CD']);
			//遺失物品資料
			$('#IPUCLAIM_OP_LOST_DATE').val(formData['OP_LOST_DATE']);
                        $('#IPUCLAIM_OP_LOST_TIME').val(formData['OP_LOST_TIME']);
                        
                        $('#IPUCLAIM_OP_LOST_CITY_CD').val(formData['OP_LOST_CITY_CD']);
                        bindCOUNTRYDDL('3', 'IPUCLAIM_OP_LOST_CITY_CD', 'IPUCLAIM_OP_LOST_TOWN_CD', '', '', '');
                        $('#IPUCLAIM_OP_LOST_TOWN_CD').val(formData['OP_LOST_TOWN_CD']);
			$('#IPUCLAIM_OP_LOST_PLACE').val(formData['OP_LOST_PLACE']);
                        $('#IPUCLAIM_OP_LOST_OJ_DESC').val(formData['OP_LOST_OJ_DESC']);
                        
                        //比對結果
			$('#IPUCLAIM_OP_YN_LOSER').val(formData['OP_YN_LOSER']);
			$('#IPUCLAIM_OP_REMARK').val(formData['OP_REMARK']);
                        
		}
	});
}
//設定認領人值---END

//確認是否已經有認領權人--START
function checkLost( OP_BASIC_SEQ_NO, OP_SEQ_NO, OP_CLAIM_TP_CD ) {
    var bool = 'Y';
    var ajData = {
            'ajaxAction' : 'checkLost',
            'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,
            'OP_SEQ_NO' : OP_SEQ_NO,
            'OP_CLAIM_TP_CD' : OP_CLAIM_TP_CD
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            bool = 'Y';
        }
        else
            bool = 'N';
    };
    var ajErr = function() {

    };

    $.ajax({
            url : ajaxActionUrl,
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
    return bool;
}
//確認是否已經有認領權人--END

//確認是否有此認領人--START
function checkYNPuClaim( OP_SEQ_NO ) {
    var bool = 'Y';
    var ajData = {
            'ajaxAction' : 'checkYNPuClaim',
            'OP_BASIC_SEQ_NO' : $('#IPUBASIC_OP_SEQ_NO').val(),
            'OP_SEQ_NO' : OP_SEQ_NO,
            'OP_PUCP_NAME' : $('#IPUCLAIM_OP_PUCP_NAME').val(),
            'OP_PUCP_IDN' : $('#IPUCLAIM_OP_PUCP_IDN').val(),
            'OP_PUCP_BIRTHDT' : $('#IPUCLAIM_OP_PUCP_BIRTHDT').val()
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            bool = 'Y';
        }
        else
            bool = 'N';
    };
    var ajErr = function() {

    };

    $.ajax({
            url : ajaxActionUrl,
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
    return bool;
}
//確認是否有此認領人--END

//CLAIM_SAVE---START
function saveClaim( bool, OP_YN_FS ,ACTION) {
        // 受理單位
	if( $('#OP_unitLevel4').val() != '' ){
		OPLevel_NM =$('#OP_unitLevel2 option:selected').text();
		OPLevel_NM +=$('#OP_unitLevel3 option:selected').text();
		OPLevel_NM +=$('#OP_unitLevel4 option:selected').text();
	}else if ($('#OPEN_unitLevel3').val() != ''){
		OPLevel_NM =$('#OP_unitLevel2 option:selected').text();
		OPLevel_NM +=$('#OP_unitLevel3 option:selected').text();
	}else{
		OPLevel_NM =$('#OP_unitLevel2 option:selected').text();
	}

	if (bool){
            show_BlockUI_page_noParent('資料儲存中…');
		var ajData = {
				'ajaxAction' : $('#actionClaimType').val(),
				'OP_UNITLEVEL2' : $('#OP_unitLevel2').val() == '' ? '' : $('#OP_unitLevel2').val(),
				'OP_UNITLEVEL2_NM' : OPLevel_NM,
				'OP_UNITLEVEL3' : $('#OP_unitLevel3').val() == '' ? '' : $('#OP_unitLevel3').val(),
				'OP_UNITLEVEL3_NM' : OPLevel_NM,
				'OP_UNITLEVEL4' : $('#OP_unitLevel4').val() == '' ? '' : $('#OP_unitLevel4').val(),
				'OP_UNITLEVEL4_NM' : OPLevel_NM,
                                'OP_SEQ_NO' : $('#IPUCLAIM_OP_SEQ_NO').val(),
                                'OP_BASIC_SEQ_NO' : $('#IPUBASIC_OP_SEQ_NO').val(),
                                'OP_AC_RCNO' : $('#IPUCLAIM_OP_AC_RCNO').val(),
				'OP_FM_DATE' : $('#IPUCLAIM_OP_FM_DATE').val(),
				'OP_PUCP_NAME' : $('#IPUCLAIM_OP_PUCP_NAME').val(),
                                'OP_PUCP_RNAME' : $('#IPUCLAIM_OP_PUCP_RNAME').val(),
				'OP_PUCP_IDN_TP' : $('#IPUCLAIM_OP_PUCP_IDN_TP').val(),
				'OP_PUCP_IDN' : $('#IPUCLAIM_OP_PUCP_IDN').val(),
				'OP_PUCP_BEFROC' : $('#IPUCLAIM_OP_PUCP_BEFROC').val(),
				'OP_PUCP_BIRTHDT' : $('#IPUCLAIM_OP_PUCP_BIRTHDT').val(),
				'OP_PUCP_GENDER' :  $('#IPUCLAIM_OP_PUCP_GENDER').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_GENDER option:selected').val(),
				'OP_PUCP_NAT_CD' : $('#IPUCLAIM_OP_PUCP_NAT_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_NAT_CD').val(),
				'OP_PUCP_NAT_NM' : $('#IPUCLAIM_OP_PUCP_NAT_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_NAT_CD option:selected').text(),
				'OP_PUCP_PHONENO' : $('#IPUCLAIM_OP_PUCP_PHONENO').val(),
				'OP_PUCP_ZIPCODE' : $('#IPUCLAIM_OP_PUCP_ZIPCODE').val(),
				'OP_PUCP_ADDR_TYPE_CD' : $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD']:checked").val(),
				'OP_PUCP_CITY_CD' : $('#IPUCLAIM_OP_PUCP_CITY_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_CITY_CD').val(),
                                'OP_PUCP_CITY_NM' : $('#IPUCLAIM_OP_PUCP_CITY_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_CITY_CD option:selected').text(),
				'OP_PUCP_TOWN_CD' : $('#IPUCLAIM_OP_PUCP_TOWN_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_TOWN_CD').val(),
                                'OP_PUCP_TOWN_NM' : $('#IPUCLAIM_OP_PUCP_TOWN_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_TOWN_CD option:selected').text(),
                                'OP_PUCP_VILLAGE_CD' : $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val(),
                                'OP_PUCP_VILLAGE_NM' : $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val() == '' ? '' : $('#IPUCLAIM_OP_PUCP_VILLAGE_CD option:selected').text(),
                                'OP_PUCP_LIN' : $('#IPUCLAIM_OP_PUCP_LIN').val(),
                                'OP_PUCP_ROAD' : $('#IPUCLAIM_OP_PUCP_ROAD').val(),
                                'OP_PUCP_ADDR_OTH' : $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val(),
                                'OP_PUCP_EMAIL' : $('#IPUCLAIM_OP_PUCP_EMAIL').val(),
                                'OP_CLAIM_TP_CD' : $('#IPUCLAIM_OP_CLAIM_TP_CD').val() == '' ? '' : $('#IPUCLAIM_OP_CLAIM_TP_CD').val(),
                                'OP_CLAIM_TP_NM' : $('#IPUCLAIM_OP_CLAIM_TP_CD').val() == '' ? '' : $('#IPUCLAIM_OP_CLAIM_TP_CD option:selected').text(),
                                'OP_LOST_DATE' : $('#IPUCLAIM_OP_LOST_DATE').val(),
                                'OP_LOST_TIME' : $('#IPUCLAIM_OP_LOST_TIME').val(),
                                'OP_LOST_CITY_CD' : $('#IPUCLAIM_OP_LOST_CITY_CD').val() == '' ? '' : $('#IPUCLAIM_OP_LOST_CITY_CD').val(),
                                'OP_LOST_CITY_NM' : $('#IPUCLAIM_OP_LOST_CITY_CD').val() == '' ? '' : $('#IPUCLAIM_OP_LOST_CITY_CD option:selected').text(),
                                'OP_LOST_TOWN_CD' : $('#IPUCLAIM_OP_LOST_TOWN_CD').val() == '' ? '' : $('#IPUCLAIM_OP_LOST_TOWN_CD').val(),
                                'OP_LOST_TOWN_NM' : $('#IPUCLAIM_OP_LOST_TOWN_CD').val() == '' ? '' : $('#IPUCLAIM_OP_LOST_TOWN_CD option:selected').text(),
                                'OP_LOST_PLACE' : $('#IPUCLAIM_OP_LOST_PLACE').val(),
                                'OP_LOST_OJ_DESC' : $('#IPUCLAIM_OP_LOST_OJ_DESC').val(),
                                'OP_YN_LOSER' : $('#IPUCLAIM_OP_YN_LOSER').val() == '' ? '' : $('#IPUCLAIM_OP_YN_LOSER').val(),
                                'OP_REMARK' : $('#IPUCLAIM_OP_REMARK').val(),
                                'OP_YN_FS' : OP_YN_FS,
                                'ACTION' : ACTION
			};
			var ajSucc = function(JData) {
                            if (JData.formData){
                                if( $('#actionClaimType').val() == 'insertClaimList' ){
                                    notyMsg('新增成功');
                                    var formData = JData.formData[0];
                                    clearClaimData();
                                    $( "#addClaimBtn" ).show();
                                    $( "#clearClaimBtn" ).show();
                                    $( "#editClaimBtn" ).hide();
                                    $( "#deleteClaimBtn" ).hide();
                                    $.unblockUI();
                                }else if( $('#actionClaimType').val() == 'updateClaimList' ){
                                    notyMsg('修改成功');
                                    var formData = JData.formData[0];
                                    if( formData['OP_CLAIM_TP_CD'] == '2'){
                                        $( "#addClaimBtn" ).show();
                                        $( "#editClaimBtn" ).show();
                                        $( "#deleteClaimBtn" ).show();
                                        $( "#clearClaimBtn" ).show();
                                    }else{
                                        $( "#addClaimBtn" ).hide();
                                        $( "#editClaimBtn" ).show();
                                        $( "#deleteClaimBtn" ).hide();
                                        $( "#clearClaimBtn" ).show();
                                    }
                                    $.unblockUI();
                                }
                            showClaimList_OW( $('#IPUBASIC_OP_SEQ_NO').val() );
                            }else{
                                $('#actionClaimType').val() == 'insertClaimList' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
                                $.unblockUI();
                            }
                            if( checkStatusForBasic( $("#IPUBASIC_OP_SEQ_NO").val() ) == "6" && ACTION != "OP05A01Q_01.jsp" ){ //如果是結案狀態則不可以修改
                                disableAll('IPUCLAIM');
                                $( "#addClaimBtn" ).hide();
                                $( "#editClaimBtn" ).hide();
                                $( "#deleteClaimBtn" ).hide();
                                $( "#clearClaimBtn" ).hide();
                                //刷新查詢結果
                                showGridListbyQS();
                            }
			};
			var ajErr = function() {
				$('#actionClaimType').val() == 'insertClaimList' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
                                $.unblockUI();
			};
	
			$.ajax({
				url : ajaxActionUrl,
				type : "POST",
				dataType : "json",
				data : ajData,
				success : ajSucc,
				error : ajErr
			});
			
		}
}
//CLAIM_SAVE---END
//CLAIM_DELETE---START
function deleteClaim(){
    show_BlockUI_page_noParent('資料刪除中…');
    var ajData = {
                    'ajaxAction' : $('#actionClaimType').val(),
                    'OP_SEQ_NO' : $('#IPUCLAIM_OP_SEQ_NO').val(),				
            };
            var ajSucc = function(JData) {
                    notyMsg('刪除成功');
                    showClaimList_OW( $('#IPUBASIC_OP_SEQ_NO').val() );
                    clearClaimData();
                    $.unblockUI();
            };
            var ajErr = function() {
                    $.alert.open('error', "刪除資料失敗!!!");
                    $.unblockUI();
            };

            $.ajax({
                    url : ajaxActionUrl,
                    type : "POST",
                    dataType : "json",
                    data : ajData,
                    success : ajSucc,
                    error : ajErr
            });
}
//CLAIM_DELETE---END

function ClaimButton( ACTION ){
    //認領資料新增
	$('#addClaimBtn').click(function() {
            if (checkClaimList(document.getElementById('form1'))){
                $('#actionClaimType').val('insertClaimList') ;
                confirmClaimAndInsert( ACTION );
            }
        });
        //認領資料修改
	$('#editClaimBtn').click(function() {
            if (checkClaimList(document.getElementById('form1'))){
                $('#actionClaimType').val('updateClaimList') ;
                confirmClaimAndUpdate( ACTION );
            }
        });
        //認領資料刪除
        $('#deleteClaimBtn').click(function() {
                $('#actionClaimType').val('deleteClaimList');
                deleteClaim();
                $( "#addClaimBtn" ).show();
                $( "#clearClaimBtn" ).show();
                $( "#editClaimBtn" ).hide();
                $( "#deleteClaimBtn" ).hide();
        });
        //認領資料清除
        $('#clearClaimBtn').click(function() {
            clearClaimData();
            $( "#addClaimBtn" ).show();
            $( "#clearClaimBtn" ).show();
            $( "#editClaimBtn" ).hide();
            $( "#deleteClaimBtn" ).hide();
            undisableAll('IPUCLAIM');
        });
        //地址轉換
        $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD']").change(function() {
            if ($("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD']:checked").val() == "1"){
                $('#IPUCLAIM_normalPattern').show();
                $('#IPUCLAIM_freePattern').hide();
                $('#IPUCLAIM_GET_HH_DATA').show();
                $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val('');
            }else if($("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD']:checked").val() == "9"){
                $('#IPUCLAIM_normalPattern').hide();
                $('#IPUCLAIM_freePattern').show();
                $('#IPUCLAIM_GET_HH_DATA').hide();
                $('#IPUCLAIM_OP_PUCP_CITY_CD').val('');
                $('#IPUCLAIM_OP_PUCP_TOWN_CD').val('');
                $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val('');
                $('#IPUCLAIM_OP_PUCP_LIN').val('');
                $('#IPUCLAIM_OP_PUCP_ROAD').val('');
            }
        });
        //身分證轉換
        $("#IPUCLAIM_OP_PUCP_IDN_TP").change(function() {
            genderClaim();
        });
        $("#IPUCLAIM_OP_PUCP_IDN").change(function() {
            genderClaim();
        });
}

//CLAIM_CHECK---START
function confirmClaimAndInsert(ACTION){
        var bool = true;
        strPermissionData = getUserRole();
        if( $('#actionClaimType').val() == 'insertClaimList' && $('#IPUCLAIM_OP_CLAIM_TP_CD').val() == '2' ){ //新增且為臨櫃認領
            if (checkYNPuClaim("") == "Y" ){ //已經新增過
                $.alert.open('error', '此人已經認領過！');
                bool = false;
                return;
            }else{
                bool = true;
            }
        }
        if( bool ){
            if ( $("#IPUCLAIM_OP_YN_LOSER").val() == "1" ){ //此人是遺失人
                if ( checkLost( $("#IPUBASIC_OP_SEQ_NO").val(), "", $('#IPUCLAIM_OP_CLAIM_TP_CD').val()) == "Y"){ //若已有認領人為是
                    $.alert.open('error', '一個收據編號，只能有一個人是遺失人！');
                    bool = false;
                    return;
                }else{
                    if( checkStatusForBasic( $("#IPUBASIC_OP_SEQ_NO").val() ) != "6" ){ //不是結案
                        if (strPermissionData["Roles"].toString().indexOf('OP300003') > -1
                                    || strPermissionData["Roles"].toString().indexOf('OP300004') > -1
                                    || strPermissionData["Roles"].toString().indexOf('OP300005') > -1
                                    || strPermissionData["Roles"].toString().indexOf('OP300006') > -1){
                            $.alert.open({
                                    type : 'confirm',
                                    content : '此人確認為有認領權人，是否要立即結案？若選『Yes』，則將其餘未審核之有認領權人更改為否並寄認領結果通知書，是否繼續？',
                                    callback : function(button, value) {
                                            if (button == 'yes') {
                                                saveClaim(true, '1',ACTION); //結案
                                            }else if( button == 'no' ){
                                                saveClaim(true, '0',ACTION); //未結案
                                            }
                                    }
                            });    
                        }else{
                            saveClaim(true, '0',ACTION);
                        }
                    }else{
                        saveClaim(true, '0',ACTION);
                    }
                }
            } else{
                saveClaim(true, '0',ACTION); //未結案
            }
        }
}
function confirmClaimAndUpdate(ACTION ){
        var bool = true;
        
        if( $('#actionClaimType').val() == 'insertClaimList' && $('#IPUCLAIM_OP_CLAIM_TP_CD').val() == '2' ){ //新增且為臨櫃認領
            if (checkYNPuClaim( $('#IPUCLAIM_OP_SEQ_NO').val() ) == "Y" ){ //已經新增過
                $.alert.open('error', '此人已經認領過！');
                bool = false;
                return;
            }else{
                bool = true;
            }
        }
        if( bool ){
            if ( $("#IPUCLAIM_OP_YN_LOSER").val() == "1" ){ //此人是遺失人
                if ( checkLost( $("#IPUBASIC_OP_SEQ_NO").val(), $("#IPUCLAIM_OP_SEQ_NO").val(), $('#IPUCLAIM_OP_CLAIM_TP_CD').val()) == "Y" ){ //若已有認領人為是
                    $.alert.open('error', '一個收據編號，只能有一個人是遺失人！');
                    bool = false;
                    return;
                }else{
                    if( checkStatusForBasic( $("#IPUBASIC_OP_SEQ_NO").val() ) != "6" ){ //不是結案
                        if (strPermissionData["Roles"].toString().indexOf('OP300003') > -1
                                    || strPermissionData["Roles"].toString().indexOf('OP300004') > -1
                                    || strPermissionData["Roles"].toString().indexOf('OP300005') > -1
                                    || strPermissionData["Roles"].toString().indexOf('OP300006') > -1){
                            $.alert.open({
                                    type : 'confirm',
                                    content : '此人確認為有認領權人，是否要立即結案？若選『Yes』，則將其餘未審核之有認領權人更改為否並寄認領結果通知書，是否繼續？',
                                    callback : function(button, value) {
                                            if (button == 'yes') {
                                                saveClaim(true, '1',ACTION); //結案
                                            }else if( button == 'no' ){
                                                saveClaim(true, '0',ACTION); //未結案
                                            }
                                    }
                            });    
                        }else{
                            saveClaim(true, '0',ACTION);
                        }
                        
                    }else{
                        saveClaim(true, '0',ACTION);
                    }
                }
            } else{
                saveClaim(true, '0',ACTION); //未結案
            }
        }
}
//CLAIM_CHECK---END