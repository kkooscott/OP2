var urlBind = 'UtilServlet';

//UNITDDL---Start
function bindUNITDDL(level, inputID2, inputID3, inputID4, action){
	var QDDL_LEVEL2='',QDDL_LEVEL3='';
	
	if ('3' == level)
		QDDL_LEVEL2 = $('#' + inputID2).val();
	else if ('4' == level){
		QDDL_LEVEL2 = $('#' + inputID2).val();
		QDDL_LEVEL3 = $('#' + inputID3).val();
	}
	
	if ('2' == level || '3' == level){
		if ('2' == level || '' == QDDL_LEVEL2){
			if ('' == action){
				$('#' + inputID3).empty();
				$('#' + inputID3).append("<option value=''>無資料</option>");
			}
			else{
				$('select[name="unitLevel3"]').empty();
				$('select[name="unitLevel3"]').append("<option value=''>無資料</option>");
			}
		}
		
		if ('' == action){
			$('#' + inputID4).empty();
			$('#' + inputID4).append("<option value=''>無資料</option>");
		}
		else{
			$('select[name="unitLevel4"]').empty();
			$('select[name="unitLevel4"]').append("<option value=''>無資料</option>");
		}
		
	}
	
	if (!('2' != level && '' == QDDL_LEVEL2)){
		$.ajax({
			url:urlBind,
			type : 'POST',
			datatype : 'json',
			async : false,
			data :
				{
				ajaxAction : 'NPAUNIT',
				QDDL_LEVEL : level,
				QDDL_LEVEL2 : QDDL_LEVEL2,
				QDDL_LEVEL3 : QDDL_LEVEL3
			},
			success : function(data, textStatus, xhr) {
				
				if ("2" == level){
					setDDLValue(data, inputID2);
					
					if ("ALL" == action){
						initDDLValue(data, 'unitLevel2');
					}
					else{
						if (QDDL_LEVEL2 != '')
							bindUNITDDL('3', inputID2, inputID3, inputID4, '');
					}
				}else if ("3" == level ){
					setDDLValue(data, inputID3);
					
					if ("ALL" == action){
						initDDLValue(data, 'unitLevel3');
					}
					else{
						if (QDDL_LEVEL3 != '')
							bindUNITDDL('4', inputID2, inputID3, inputID4, '');
                                                    
                                    }
				}
				else if ("4" == level ){
                                   if (QDDL_LEVEL3 == ''){
                                      $('#' + inputID4).empty();
                                                       $('#' + inputID4).append("<option value=''>無資料</option>");    
                                   }else
					setDDLValue(data, inputID4);
					
					if ("ALL" == action){
						initDDLValue(data, 'unitLevel4');
					}
				}
			},
			error : function(jqXHR,textStatus,errorThrown){
				$.alert.open('error', textStatus + " " + errorThrown);
			}
		});
	}
}

function setDDLValue(data, inputID){
	$('#' + inputID).empty();
	if (data.result.length > 0){
		$('#' + inputID).append("<option value=''>請選擇...</option>");
		for (var i = 0; i < data.result.length; i++) {
                    if("OP_unitLevel2"==inputID)
			$('#' + inputID).append("<option value='" +data.result[i]["OP_UNIT_CD"] + "'>" + data.result[i]["OP_UNIT_NM"] + "</option>");
                    else
                        $('#' + inputID).append("<option value='" +data.result[i]["OP_UNIT_CD"] + "'>" + data.result[i]["OP_UNIT_S_NM"] + "</option>");
		}
	}
	else
		$('#' + inputID).append("<option value=''>無資料</option>");
}

function initDDLValue(data, inputID){
	$('select[name="' + inputID +'"]').empty();
	if (data != null || data.result.length > 0){
		$('select[name="' + inputID +'"]').append("<option value=''>請選擇...</option>");
		for (var i = 0; i < data.result.length; i++) {
                    if("unitLevel2"==inputID)
			$('select[name="' + inputID +'"]').append("<option value='" +data.result[i]["OP_UNIT_CD"] + "'>" + data.result[i]["OP_UNIT_NM"] + "</option>");
                    else
                        $('select[name="' + inputID +'"]').append("<option value='" +data.result[i]["OP_UNIT_CD"] + "'>" + data.result[i]["OP_UNIT_S_NM"] + "</option>");
		}
	}
	else
		$('select[name="' + inputID +'"]').append("<option value=''>無資料</option>");
}
//UNITDDL---End

//及時上網認領 --Start
function time(count) {
    strPermissionData = getUserRole();
    var ajData = {
            'ajaxAction' : 'countNetCase',
            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
            'OP_PERMISSION' : strPermissionData["RolePermission"]
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            $('#countNetCase').text( formData['NETCOUNT'] );
        }
    };
    var ajErr = function() {

    };
    $.ajax({
            url : 'PuCaseCountServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });

    setTimeout(time, 30000);
}
//及時上網認領 --End

//未結案件 --Start
function CaseCount() {
    strPermissionData = getUserRole();
    var ajData = {
            'ajaxAction' : 'CaseCount',
            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
            'OP_PERMISSION' : strPermissionData["RolePermission"],
            'TYPE' : 'CaseCount'
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            $('#NoFinishCase').text( formData['NoFinishCase'] + "件");
            $('#NoAnnounceCase').text( formData['NoAnnounceCase'] + "件");
            $('#AnDlCase').text( formData['AnDlCase'] + "件");
            $('#PuanDlCase').text( formData['PuanDlCase'] + "件");
        }
    };
    var ajErr = function() {

    };
    $.ajax({
            url : 'PuCaseCountServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
}
//未結案件 --End

//招領即將(7日後)期滿案件 --Start
function AnnounceCaseCount() {
    strPermissionData = getUserRole();
    var ajData = {
            'ajaxAction' : 'AnnounceCaseCount',
            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
            'OP_PERMISSION' : strPermissionData["RolePermission"],
            'TYPE' : 'CaseCount'
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            $('#AnDateEndCaseAfter7').text( formData['AnDateEndCaseAfter7'] + "件");
        }
    };
    var ajErr = function() {

    };
    $.ajax({
            url : 'PuCaseCountServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
}
//招領即將(7日後)期滿案件 --End

//領回即將(7日後)期滿案件 --Start
function AnDlCaseCount() {
    strPermissionData = getUserRole();
    var ajData = {
            'ajaxAction' : 'AnDlCaseCount',
            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
            'OP_PERMISSION' : strPermissionData["RolePermission"],
            'TYPE' : 'CaseCount'
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            $('#AnDlCaseAfter7').text( formData['AnDlCaseAfter7'] + "件");
        }
    };
    var ajErr = function() {

    };
    $.ajax({
            url : 'PuCaseCountServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
}
//領回即將(7日後)期滿案件 --End

//未公告案件 --Start
//function NoAnnounceCase() {
//    strPermissionData = getUserRole();
//    var ajData = {
//            'ajaxAction' : 'NoAnnounceCase',
//            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
//            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
//            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
//            'OP_PERMISSION' : strPermissionData["RolePermission"],
//            'TYPE' : 'NoAnnounceCase'
//    };
//    var ajSucc = function(JData) {
//        if (JData.formData){
//            var formData = JData.formData[0];
//            $('#NoAnnounceCase').text( formData['CASECOUNT'] + "件");
//        }
//    };
//    var ajErr = function() {
//
//    };
//    $.ajax({
//            url : 'PuCaseCountServlet',
//            type : "POST",
//            dataType : "json",
//            async : false,
//            data : ajData,
//            success : ajSucc,
//            error : ajErr
//    });
//}
//未公告案件 --End

//招領期滿案件 --Start
//function AnDlCase() {
//    strPermissionData = getUserRole();
//    var ajData = {
//            'ajaxAction' : 'AnDlCase',
//            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
//            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
//            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
//            'OP_PERMISSION' : strPermissionData["RolePermission"],
//            'TYPE' : 'AnDlCase'
//    };
//    var ajSucc = function(JData) {
//        if (JData.formData){
//            var formData = JData.formData[0];
//            $('#AnDlCase').text( formData['CASECOUNT'] + "件");
//        }
//    };
//    var ajErr = function() {
//
//    };
//    $.ajax({
//            url : 'PuCaseCountServlet',
//            type : "POST",
//            dataType : "json",
//            async : false,
//            data : ajData,
//            success : ajSucc,
//            error : ajErr
//    });
//}
//招領期滿案件 --End

//領回期滿案件 --Start
//function PuanDlCase() {
//    strPermissionData = getUserRole();
//    var ajData = {
//            'ajaxAction' : 'PuanDlCase',
//            'OP_UNITLEVEL2' : strPermissionData['UNITLEVEL1'],
//            'OP_UNITLEVEL3' : strPermissionData['UNITLEVEL2'],
//            'OP_UNITLEVEL4' : strPermissionData['UNITLEVEL3'],
//            'OP_PERMISSION' : strPermissionData["RolePermission"],
//            'TYPE' : 'PuanDlCase'
//    };
//    var ajSucc = function(JData) {
//        if (JData.formData){
//            var formData = JData.formData[0];
//            $('#PuanDlCase').text( formData['CASECOUNT'] + "件");
//        }
//    };
//    var ajErr = function() {
//
//    };
//    $.ajax({
//            url : 'PuCaseCountServlet',
//            type : "POST",
//            dataType : "json",
//            async : false,
//            data : ajData,
//            success : ajSucc,
//            error : ajErr
//    });
//}
//領回期滿案件 --End

//COUNTRY---START
function bindCOUNTRYDDL(level, inputID2, inputID3, inputID4, inputID5, action) {
    var QDDL_LEVEL2 = '', QDDL_LEVEL3 = '', QDDL_LEVEL4 = '';
    if ('3' == level) {
        QDDL_LEVEL2 = $('#' + inputID2).val();
    } else if ('4' == level) {
        QDDL_LEVEL2 = $('#' + inputID2).val();
        QDDL_LEVEL3 = $('#' + inputID3).val();
    } else if ('5' == level) {
        QDDL_LEVEL2 = $('#' + inputID2).val();
        QDDL_LEVEL3 = $('#' + inputID3).val();
        QDDL_LEVEL4 = $('#' + inputID4).val();
    }

    if ('2' == level || '3' == level || '4' == level || '5' == level) {
        if ('2' == level || '' == QDDL_LEVEL2) {
            if ('' == action) {
                $('#' + inputID3).empty();
                $('#' + inputID3).append("<option value=''>無資料</option>");
                $('#' + inputID4).empty();
                $('#' + inputID4).append("<option value=''> 無資料 </option>");
                $('#' + inputID5).empty();
                $('#' + inputID5).append("<option value=''>無資料</option>");
            } else {
                $('select[name="COUNTRYLevel3"]').empty();
                $('select[name="COUNTRYLevel3"]').append("<option value=''>無資料</option>");
                $('select[name="COUNTRYLevel4"]').empty();
                $('select[name="COUNTRYLevel4"]').append("<option value=''>無資料</option>");
                $('select[name="COUNTRYLevel5"]').empty();
                $('select[name="COUNTRYLevel5"]').append("<option value=''>無資料</option>");
            }
        }
        if ('3' == level || '' == QDDL_LEVEL3) {
            if ('' == action) {
                $('#' + inputID4).empty();
                $('#' + inputID4).append("<option value=''>無資料</option>");
                $('#' + inputID5).empty();
                $('#' + inputID5).append("<option value=''>無資料</option>");
            } else {
                $('select[name="COUNTRYLevel4"]').empty();
                $('select[name="COUNTRYLevel4"]').append("<option value=''>無資料</option>");
                $('select[name="COUNTRYLevel5"]').empty();
                $('select[name="COUNTRYLevel5"]').append("<option value=''>無資料</option>");
            }
        }
        if ('' == action) {
            $('#' + inputID5).empty();
            $('#' + inputID5).append("<option value=''>無資料</option>");
        } else {
            $('select[name="COUNTRYLevel5"]').empty();
            $('select[name="COUNTRYLevel5"]').append("<option value=''>無資料</option>");
        }

    }
    var tmptype = '';
    if ('2' == level)
        tmptype = 'CITY';
    else if ('3' == level)
        tmptype = 'TOWN';
    else if ('4' == level)
        tmptype = 'VILLAGE';
    else
        tmptype = 'ROAD';

    if (!('2' != level && '' == QDDL_LEVEL2)) {
        if (!('4' == level && '' == QDDL_LEVEL3) || !('5' == level && '' == QDDL_LEVEL4)) {
            $.ajax({
                async: false,
                url: urlBind,
                type: 'POST',
                datatype: 'json',
                data:
                        {
                            ajaxAction: tmptype,
                            QDDL_LEVEL: level,
                            OP_CITY_CD: QDDL_LEVEL2,
                            OP_TOWN_CD: QDDL_LEVEL3,
                            OP_VILLAGE_CD: QDDL_LEVEL4,
                        },
                success: function (data, textStatus, xhr) {
                    if ("2" == level) {
                        setCOUNTRYDDLValue(data, inputID2, 'OP_CITY_CD', 'OP_CITY_NM');

                        if ("ALL" == action) {
                            initCOUNTRYDDLValue(data, 'COUNTYRLevel2', 'OP_CITY_CD', 'OP_CITY_NM');
                        } else {
                            if (QDDL_LEVEL2 != '')
                                setCOUNTRYDDLValue('3', inputID2, inputID3, inputID4, inputID5, '');
                        }
                    } else if ("3" == level) {
                        if (QDDL_LEVEL2 != '')
                            setCOUNTRYDDLValue(data, inputID3, 'OP_TOWN_CD', 'OP_TOWN_S_NAME');

                        if ("ALL" == action) {
                            initCOUNTRYDDLValue(data, 'COUNTYRLevel3', 'OP_TOWN_CD', 'OP_TOWN_S_NAME');
                        }

                    } else if ("4" == level) {
                        if (QDDL_LEVEL3 != '') {
                            setCOUNTRYDDLValue(data, inputID4, 'OP_VILLAGE_CD', 'OP_VILLAGE_S_NM');
                        }
                        if ("ALL" == action) {
                            initCOUNTRYDDLValue(data, 'COUNTYRLevel4');
                        }
                    } else if ("5" == level) {
                        if (QDDL_LEVEL3 != '') {
                            setROADDDLValue(data, inputID5, 'OP_ROAD_CD', 'OP_ROAD_S_NM');
                        }
                        if ("ALL" == action) {
                            setROADDDLValue(data, 'COUNTYRLevel5');
                        }
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $.alert.open('error', textStatus + " " + errorThrown);
                }
            });
        }
    }
}
function bindCOUNTRYDDL_INS(level, inputID2, inputID3, inputID4, inputID5, action) {
    var QDDL_LEVEL2 = '', QDDL_LEVEL3 = '', QDDL_LEVEL4 = '';
    if ('3' == level) {
        QDDL_LEVEL2 = $('#' + inputID2).val();
    } else if ('4' == level) {
        QDDL_LEVEL2 = $('#' + inputID2).val();
        QDDL_LEVEL3 = $('#' + inputID3).val();
    } else if ('5' == level) {
        QDDL_LEVEL2 = $('#' + inputID2).val();
        QDDL_LEVEL3 = $('#' + inputID3).val();
        QDDL_LEVEL4 = $('#' + inputID4).val();
    }

    if ('2' == level || '3' == level || '4' == level || '5' == level) {
        if ('2' == level || '' == QDDL_LEVEL2) {
            if ('' == action) {
                $('#' + inputID3).empty();
                $('#' + inputID3).append("<option value=''>無資料</option>");
                $('#' + inputID4).empty();
                $('#' + inputID4).append("<option value=''> 無資料 </option>");
                $('#' + inputID5).empty();
                $('#' + inputID5).append("<option value=''>無資料</option>");
            } else {
                $('select[name="COUNTRYLevel3"]').empty();
                $('select[name="COUNTRYLevel3"]').append("<option value=''>無資料</option>");
                $('select[name="COUNTRYLevel4"]').empty();
                $('select[name="COUNTRYLevel4"]').append("<option value=''>無資料</option>");
                $('select[name="COUNTRYLevel5"]').empty();
                $('select[name="COUNTRYLevel5"]').append("<option value=''>無資料</option>");
            }
        }
        if ('3' == level || '' == QDDL_LEVEL3) {
            if ('' == action) {
                $('#' + inputID4).empty();
                $('#' + inputID4).append("<option value=''>無資料</option>");
                $('#' + inputID5).empty();
                $('#' + inputID5).append("<option value=''>無資料</option>");
            } else {
                $('select[name="COUNTRYLevel4"]').empty();
                $('select[name="COUNTRYLevel4"]').append("<option value=''>無資料</option>");
                $('select[name="COUNTRYLevel5"]').empty();
                $('select[name="COUNTRYLevel5"]').append("<option value=''>無資料</option>");
            }
        }
        if ('' == action) {
            $('#' + inputID5).empty();
            $('#' + inputID5).append("<option value=''>無資料</option>");
        } else {
            $('select[name="COUNTRYLevel5"]').empty();
            $('select[name="COUNTRYLevel5"]').append("<option value=''>無資料</option>");
        }

    }
    var tmptype = '';
    if ('2' == level)
        tmptype = 'CITY';
    else if ('3' == level)
        tmptype = 'TOWN';
    else if ('4' == level)
        tmptype = 'VILLAGE';
    else
        tmptype = 'ROAD_INS';

    if (!('2' != level && '' == QDDL_LEVEL2)) {
        if (!('4' == level && '' == QDDL_LEVEL3) || !('5' == level && '' == QDDL_LEVEL4)) {
            $.ajax({
                async: false,
                url: urlBind,
                type: 'POST',
                datatype: 'json',
                data:
                        {
                            ajaxAction: tmptype,
                            QDDL_LEVEL: level,
                            OP_CITY_CD: QDDL_LEVEL2,
                            OP_TOWN_CD: QDDL_LEVEL3,
                            OP_VILLAGE_CD: QDDL_LEVEL4,
                        },
                success: function (data, textStatus, xhr) {
                    if ("2" == level) {
                        setCOUNTRYDDLValue(data, inputID2, 'OP_CITY_CD', 'OP_CITY_NM');

                        if ("ALL" == action) {
                            initCOUNTRYDDLValue(data, 'COUNTYRLevel2', 'OP_CITY_CD', 'OP_CITY_NM');
                        } else {
                            if (QDDL_LEVEL2 != '')
                                setCOUNTRYDDLValue('3', inputID2, inputID3, inputID4, inputID5, '');
                        }
                    } else if ("3" == level) {
                        if (QDDL_LEVEL2 != '')
                            setCOUNTRYDDLValue(data, inputID3, 'OP_TOWN_CD', 'OP_TOWN_S_NAME');

                        if ("ALL" == action) {
                            initCOUNTRYDDLValue(data, 'COUNTYRLevel3', 'OP_TOWN_CD', 'OP_TOWN_S_NAME');
                        }

                    } else if ("4" == level) {
                        if (QDDL_LEVEL3 != '') {
                            setCOUNTRYDDLValue(data, inputID4, 'OP_VILLAGE_CD', 'OP_VILLAGE_S_NM');
                        }
                        if ("ALL" == action) {
                            initCOUNTRYDDLValue(data, 'COUNTYRLevel4');
                        }
                    } else if ("5" == level) {
                        if (QDDL_LEVEL3 != '') {
                            setROADDDLValue(data, inputID5, 'OP_ROAD_CD', 'OP_ROAD_S_NM');
                        }
                        if ("ALL" == action) {
                            setROADDDLValue(data, 'COUNTYRLevel5');
                        }
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $.alert.open('error', textStatus + " " + errorThrown);
                }
            });
        }
    }
}
function setCOUNTRYDDLValue(data, inputID, valueCol, txtCol) {
    $('#' + inputID).empty();
    if (data.result.length > 0) {
        $('#' + inputID).append("<option value=''>請選擇...</option>");
        for (var i = 0; i < data.result.length; i++) {
            $('#' + inputID).append("<option value='" + data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
        }
    } else
        $('#' + inputID).append("<option value=''>無資料</option>");
}

function initCOUNTRYDDLValue(data, inputID, valueCol, txtCol) {
    $('select[name="' + inputID + '"]').empty();
    if (data != null || data.result.length > 0) {
        $('select[name="' + inputID + '"]').append("<option value=''>請選擇...</option>");
        for (var i = 0; i < data.result.length; i++) {
            $('select[name="' + inputID + '"]').append("<option value='" + data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
        }
    } else
        $('select[name="' + inputID + '"]').append("<option value=''>無資料</option>");
}

function setCOUNTRYDDLValue(data, inputID, valueCol, txtCol) {
    $('#' + inputID).empty();
    if (data.result.length > 0) {
        $('#' + inputID).append("<option value=''>請選擇...</option>");
        for (var i = 0; i < data.result.length; i++) {
            $('#' + inputID).append("<option value='" + data.result[i][valueCol] + "'>" + data.result[i][txtCol] + "</option>");
        }
    } else
        $('#' + inputID).append("<option value=''>無資料</option>");
}

function setROADDDLValue(data, inputID, valueCol, txtCol) {
    dataResult = $.map(data.result, function (obj) {
        return {id: obj.TM_ROAD_CD, text: obj.TM_ROAD_S_NM};
    });
    $('#' + inputID).select2({
        width: '200px',
        placeholder: '請選擇...',
        allowClear: true,
        data: dataResult
    });
}
//COUNTTRY---END