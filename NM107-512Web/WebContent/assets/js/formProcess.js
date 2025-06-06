
var urlBind = 'UtilServlet';
function CheckId(value, element){
	if (value.length != 0 ){
	if (value.length != 10)
		return notyMsg('請輸入有效的身分證字號'); 
	IDN = value;
	IDTable = {
		A : 10,
		B : 11,
		C : 12,
		D : 13,
		E : 14,
		F : 15,
		G : 16,
		H : 17,
		I : 34,
		J : 18,
		K : 19,
		M : 21,
		N : 22,
		O : 35,
		P : 23,
		Q : 24,
		T : 27,
		U : 28,
		V : 29,
		W : 32,
		X : 30,
		Z : 33,
		L : 20,
		R : 25,
		S : 26,
		Y : 31
	};
	LocalDigit = IDTable[IDN[0].toUpperCase()];
	if( /^[A-Za-z][1,2][\d]{8}/.test(value)
			&& ((Math.floor(LocalDigit / 10) + (LocalDigit % 10)
					* 9 + IDN[1] * 8 + IDN[2] * 7 + IDN[3] * 6
					+ IDN[4] * 5 + IDN[5] * 4 + IDN[6] * 3 + IDN[7]
					* 2 + IDN[8] * 1 + IDN[9] * 1) % 10 == 0)){
		
	}else{
		return notyMsg('請輸入有效的身分證字號'); 
	}
	}
}
function bindDDL(contrlItem, data, defaltValue, valueCol, txtCol){	
	$.ajax({
		url:urlBind,
		type : 'POST',
		datatype : 'json',
		data : data,
		async : false,
		success : function(data, textStatus, xhr) {
			$('#' + contrlItem).empty();
			if ("" != defaltValue)
				$('#' + contrlItem).append("<option value=''>" + defaltValue + "</option>");
			
			for (var i = 0; i < data.result.length; i++) {
				$('#' + contrlItem).append("<option value='" +data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
			}
			//$('#' + contrlItem).selectpicker('refresh');
		},
		error : function(jqXHR,textStatus,errorThrown){
			$.alert.open('error',textStatus + " " + errorThrown);
		}
	});
}

function bindFormControlDDL(contrlItem, data, defaltValue, valueCol, txtCol){	
	$.ajax({
		async: false,
		url:urlBind,
		type : 'POST',
		datatype : 'json',
		data : data,
		success : function(data, textStatus, xhr) {
			$('#' + contrlItem).empty();
			if ('' != defaltValue)
				$('#' + contrlItem).append("<option value=''>" + defaltValue + "</option>");
			
			for (var i = 0; i < data.result.length; i++) {
				$('#' + contrlItem).append("<option value='" +data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
			}
		},
		error : function(jqXHR,textStatus,errorThrown){
			$.alert.open('error',textStatus + " " + errorThrown);
		}
	});
}
//只跑3C
function bindFormControlDDLForE82(contrlItem, data, defaltValue, valueCol, txtCol){	
	$.ajax({
		async: false,
		url:urlBind,
		type : 'POST',
		datatype : 'json',
		data : data,
		success : function(data, textStatus, xhr) {
			$('#' + contrlItem).empty();
			if ('' != defaltValue)
				$('#' + contrlItem).append("<option value=''>" + defaltValue + "</option>");
			
			for (var i = 0; i < data.result.length; i++) {
                            if(data.result[i][valueCol]=="A009"||data.result[i][valueCol]=="A010"||data.result[i][valueCol]=="A011"||data.result[i][valueCol]=="A012"||data.result[i][valueCol]=="A030"||data.result[i][valueCol]=="A033"||data.result[i][valueCol]=="A052"||data.result[i][valueCol]=="A069"||data.result[i][valueCol]=="BD03"||data.result[i][valueCol]=="BD04"||data.result[i][valueCol]=="BD05"||data.result[i][valueCol]=="BD10"||data.result[i][valueCol]=="BD11"){
				$('#' + contrlItem).append("<option value='" +data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
                            }
			}
                        if( contrlItem == "IPUDETAIL_OP_TYPE_CD" || contrlItem == "OP_SEARCH_TYPE_CD" ){
                            $('#' + contrlItem).multipleSelect({ 
                                width: 280, //280, 
                                maxHeight: 200, 
                                multipleWidth: 55, 
                                single: true, 
                                filter: true 
                            });
                            $('#' + contrlItem).multipleSelect("refresh");
                        }
		},
		error : function(jqXHR,textStatus,errorThrown){
			$.alert.open('error',textStatus + " " + errorThrown);
		}
	});
}
function bindFormControlDDLForE8(contrlItem, data, defaltValue, valueCol, txtCol){	
	$.ajax({
		async: false,
		url:urlBind,
		type : 'POST',
		datatype : 'json',
		data : data,
		success : function(data, textStatus, xhr) {
			$('#' + contrlItem).empty();
			if ('' != defaltValue)
				$('#' + contrlItem).append("<option value=''>" + defaltValue + "</option>");
			
			for (var i = 0; i < data.result.length; i++) {
				$('#' + contrlItem).append("<option value='" +data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
			}
                        if( contrlItem == "IPUDETAIL_OP_TYPE_CD" || contrlItem == "OP_SEARCH_TYPE_CD" ){
                            $('#' + contrlItem).multipleSelect({ 
                                width: 280, //280, 
                                maxHeight: 200, 
                                multipleWidth: 55, 
                                single: true, 
                                filter: true 
                            });
                            $('#' + contrlItem).multipleSelect("refresh");
                        }
		},
		error : function(jqXHR,textStatus,errorThrown){
			$.alert.open('error',textStatus + " " + errorThrown);
		}
	});
}

function checkUserRole(){
	var returnValue = false;
	var ajData = {
			'ajaxAction' : 'SUPERVISOR'
		};
	
	$.ajax({
		url:'UtilServlet',
		type : 'POST',
		async : false,
		datatype : 'json',
		data : ajData,
		success : function(data) {
			if (data.formData)
				returnValue = true;
		}
	});
	
	return Boolean(returnValue);
}

function getUserRole(){
	var returnValue = '';
	var ajData = {
			'ajaxAction' : 'PERMISSION'
		};
	
	$.ajax({
		url:'UtilServlet',
		type : 'POST',
		async : false,
		datatype : 'json',
		data : ajData,
		success : function(data) {
			if (data.formData != null)
				returnValue = data.formData;
		}
	});
	
	return returnValue;
}

function confirmPersonNum(numADULT, numCHILD) {	
	$.alert.open({
		type : 'confirm',
		content : '輸入之名冊與所填人數不符，是否以名冊輸入之人數為主？',
		callback : function(button, value) {
			if (button == 'yes') {
				if (checkCase(document.getElementById('form1'))){
					$('#NV_ADULT_NUM').val(numADULT);
					$('#NV_INFANT_NUM').val(numCHILD);
					
					doSubmit();
		    	}else {
		    		return false;
		    	}
			}
			else
				return false;
		}
	});
}

function getGridNum(gridName){
	//var gridNum = $('#' + gridName).jqGrid('getGridParam', 'reccount'); //只取畫面上jqgrid的欄位數
	var gridNum = $('#' + gridName).jqGrid('getGridParam','records'); //取jqgrid所有的欄位數
	
	if (gridNum == null){
		gridNum = 0;
	}
	
	return gridNum;
}

function changeListType(disID, enID){
	$('#' + enID).prop("checked", true);
}

function changePattern(showID, hideID){
	if ('' != showID)
		$('#' + showID).removeAttr('style');
	
	if ('' != hideID)
		$('#' + hideID).attr('style','display:none');
}

//帶入片語檔---Start
function bindDATATYPE(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'TYPE',
			'OP_CD_TYPE' : TYPE_CD
		};
	
	bindFormControlDDL(ddl, ajData, tmpvalue, 'OP_CD', 'OP_CD_NM');
}

/*
 *取得各警局複審人員下拉選單 
 */
//帶入片語檔---Start
function bindAuditNameDDL(unit_D, unit_B, unit,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'AUDITNAME',
			'unit_D' : unit_D,
                        'unit_B' : unit_B,
                        'unit' : unit,
		};
	
	bindFormControlDDL(ddl, ajData, tmpvalue, 'SS_UID', 'SS_UNM');
}
//OPDT_OBJECT_CD_COMPARE
function checkDate(IPUBASIC_OP_SEQ_NO){
    var returnValue = true;
    var ajData = {
        'ajaxAction':'BINDDATE',
        'OP_SEQ_NO':IPUBASIC_OP_SEQ_NO
    };
    $.ajax({
		url:'UtilServlet',
		type : 'POST',
		async : false,
		datatype : 'json',
		data : ajData,
		success : function(data) {
			if (data.result.length == 0)
				returnValue = false;
		}
	});
	
	return Boolean(returnValue);
}

function bindE8DATATYPE(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'E8TYPE',
			'E8_CD_TYPE' : TYPE_CD
		};
	
	bindFormControlDDLForE8(ddl, ajData, tmpvalue, 'E8_CD', 'E8_CD_NM');
}
//新的物品種類DDL
function bindOPDATATYPE(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'E8LISTN',
			'E8_CD_TYPE' : TYPE_CD
		};
	
	bindFormControlDDLForE8(ddl, ajData, tmpvalue, 'OP_CODE', 'OP_CODE_NM');
}

function bindE8DATATYPE2(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'E8TYPE',
			'E8_CD_TYPE' : TYPE_CD
		};
	
	bindFormControlDDLForE82(ddl, ajData, tmpvalue, 'E8_CD', 'E8_CD_NM');
}

function bindE8LISTTYPE(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'E8LIST',
			'E8_LIST_TYPE' : TYPE_CD
		};
	
	bindFormControlDDL(ddl, ajData, tmpvalue, 'E8_LIST_NM', 'E8_LIST_NM');
}

function bindCOUNTRYTYPE(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'COUNTRY',
		};
	
	bindFormControlDDL(ddl, ajData, tmpvalue, 'OP_COUNTRY_CD', 'OP_COUNTRY_NM');
}

function bindCOLORTYPE(TYPE_CD,ddl,tmpvalue){	
	var ajData = {
			'ajaxAction' : 'COLOR',
		};
	
	bindFormControlDDL(ddl, ajData, tmpvalue, 'OP_COLOR_CD', 'OP_COLOR_NM');
}
function getunit_code(code){
	var tmpdata;
	var ajData = {
			'ajaxAction' : 'UNIT',
			'UNITCODE' : code
		};
	$.ajax({
		async: false,
		url : 'UtilServlet',
		type : "POST",
		dataType : "json",
		data : ajData,
		success : function(data, textStatus, xhr) {
			tmpdata =  data;
		}
	});
	return tmpdata;
} 
//帶入片語檔---End

function disableAll( ID_CONTAINS ){
    $('[id*="' + ID_CONTAINS + '"]').prop('disabled', true);
    if( ID_CONTAINS == 'IPUDETAIL' ){
        $('#photo1SubmitBtn').prop('disabled', true); 
        $('#photo1DeleteBtn').prop('disabled', true); 
        $('#photo2SubmitBtn').prop('disabled', true); 
        $('#photo2DeleteBtn').prop('disabled', true);
        $('#GET_E_MAC_DATA').prop('disabled', true);
        $('#GET_E_IMEI_DATA').prop('disabled', true);
        $('#GET_E_PHONE_DATA').prop('disabled', true);
    }else if( ID_CONTAINS == 'IANNOUNCE2' ){
        $('#photo3SubmitBtn').prop('disabled', true); 
        $('#photo3DeleteBtn').prop('disabled', true); 
        $('#photo4SubmitBtn').prop('disabled', true); 
        $('#photo4DeleteBtn').prop('disabled', true); 
    }else if( ID_CONTAINS == 'IFNSH' ){
        $("#OP_RT_unitLevel2").prop('disabled', true);
        $("#OP_RT_unitLevel3").prop('disabled', true);
        $("#OP_RT_unitLevel4").prop('disabled', true);
    }
}
function undisableAll( ID_CONTAINS ){
    $('[id*="' + ID_CONTAINS + '"]').prop('disabled', false);
    if( ID_CONTAINS == 'IPUBASIC' ){
        $('#OP_unitLevel2').prop('disabled', true);
        $('#OP_unitLevel3').prop('disabled', true);
        $('#OP_unitLevel4').prop('disabled', true);
        $('#IPUBASIC_OP_AC_STAFF_NM').prop('disabled', true);
        $('#IPUBASIC_OP_AC_RCNO').prop('disabled', true);
        $('#IPUBASIC_OP_FM_DATE').prop('disabled', true);
        $('#IPUBASIC_OP_CURSTAT_CD').prop('disabled', true);
    }else if( ID_CONTAINS == 'IPUDETAIL' ){
        $('#IPUDETAIL_OP_AC_RCNO').prop('disabled', true); 
        $('#photo1SubmitBtn').prop('disabled', false); 
        $('#photo1DeleteBtn').prop('disabled', false); 
        $('#photo2SubmitBtn').prop('disabled', false); 
        $('#photo2DeleteBtn').prop('disabled', false);
        $('#GET_E_MAC_DATA').prop('disabled', false);
        $('#GET_E_IMEI_DATA').prop('disabled', false);
        $('#GET_E_PHONE_DATA').prop('disabled', false);
    }else if( ID_CONTAINS == 'IPUCLAIM' ){
        $('#IPUCLAIM_OP_AC_RCNO').prop('disabled', true);
        $('#IPUCLAIM_OP_CLAIM_TP_CD').prop('disabled', true);
    }else if( ID_CONTAINS == 'IANNOUNCE2' ){
        $('#photo3SubmitBtn').prop('disabled', false); 
        $('#photo3DeleteBtn').prop('disabled', false); 
        $('#photo4SubmitBtn').prop('disabled', false); 
        $('#photo4DeleteBtn').prop('disabled', false); 
    }else if( ID_CONTAINS == 'IANDL' ){
        $('#OP_PR_unitLevel2').prop('disabled', true);
        $('#OP_PR_unitLevel3').prop('disabled', true);
        $('#OP_PR_unitLevel4').prop('disabled', true);
        $('#IANDL_OP_PR_STAFF_NM').prop('disabled', true);
        $('#OP_PUPOANUNIT_unitLevel2').prop('disabled', true);
        $('#OP_PUPOANUNIT_unitLevel3').prop('disabled', true);
        $('#OP_PUPOANUNIT_unitLevel4').prop('disabled', true); 
        $('#IANDL_OP_PUPO_ANDTEND').prop('disabled', true); 
    }else if( ID_CONTAINS == 'IPUANDL' ){
        $('#OP_PR_unitLevel2').prop('disabled', true);
        $('#OP_PR_unitLevel3').prop('disabled', true);
        $('#OP_PR_unitLevel4').prop('disabled', true);
        $('#IPUANDL_OP_SD_NAME').prop('disabled', true);
        $('#IPUANDL_OP_SD_TITLE').prop('disabled', true);
    }else if( ID_CONTAINS == 'IFNSH' ){
        $('#IFNSH_OP_FS_STAFF_NM').prop('disabled', true);
    }else if( ID_CONTAINS == 'IFNSH2' ){
        $('#IFNSH2_OP_FS_STAFF_NM').prop('disabled', true);
    }else if( ID_CONTAINS == 'IPUANDL' ){
        $('#IPUANDL2_OP_SD_NAME').prop('disabled', true);
        $('#IPUANDL2_OP_SD_TITLE').prop('disabled', true);
    }
}

//GET_ONLINE_DAY
function getOnlineDay() {
    var day;
    var ajData = {
            'ajaxAction' : 'GETONLINEDAY'				
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            day = formData['DAY'];
        }
    };
    var ajErr = function() {
        
    };

    $.ajax({
            url : 'UtilServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
    return day;
}