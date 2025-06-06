var strPermissionData;

$(document).ready(function() {
        time(1);
	$('#menu09').addClass("active");
	$('#menu09span').addClass("selected");
        $('#menu0902').addClass("active");
	init();
});

$(window).resize(function() {
	$('#gridMainList').setGridWidth($(window).width() - 10);
	$('#gridClueList').setGridWidth($('.col-md-12').width()-63);
        $("#gridList_3").setGridWidth($(window).width() - 92);
});

// #region Front End Event

function init() {
	strPermissionData = getUserRole();
	if (strPermissionData["RolePermission"] == ''){
		$.alert.open('error', '遺失使用者資訊，請重新登入');
		window.close();
	}
	// 帶入單位下拉式選單選項
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
	$('#queryBtn').click(function() {
		if (checkList(document.getElementById('form1')))
			showGridListbyQS();
	});
	$('#QS_CLEAR').click(function() {
		cleanEditor();
	});
}
function registButtonGr() {

}

// 註冊事件區---End

function initDefaultQS() {
    
    dateRange('OP_QUERY_DT_TM_S', 'OP_QUERY_DT_TM_E');
    
    if (checkList(document.getElementById('form1')))
        showGridListbyQS();
}

var ajaxActionUrl = 'SystemMaintainServlet';

function showGridListbyQS() {
        var nowDate = new Date();
        nowDate = getROCDateSlash(nowDate);
	var bool =true;
        var msg = "";
        if( ($('#OP_QUERY_DT_TM_S').val() == "") ){
            msg += '查詢日期起日不可為空<br/>';
            bool = false;
        }
        if( ($('#OP_QUERY_DT_TM_E').val() == "") ){
            msg += '查詢日期迄日不可為空<br/>';
            bool = false;
        }
        //排程日期
        if( ($('#OP_QUERY_DT_TM_S').val() != "") && ($('#OP_QUERY_DT_TM_E').val() != "") ){
            if (($('#OP_QUERY_DT_TM_S').val() > $('#OP_QUERY_DT_TM_E').val())){
                msg += '查詢日期迄日不可小於查詢日期起日<br/>';
		bool = false;
            }
        }
        if( getADDate($('#OP_QUERY_DT_TM_S').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '查詢日期起日不可大於系統日期時間<br/>';
            bool = false;
        }
        if( getADDate($('#OP_QUERY_DT_TM_E').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '查詢日期迄日不可大於系統日期時間<br/>';
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
            show_BlockUI_page_noParent('資料準備中…');
		var gridId = "#gridMainList";
		var pagerId = "#pagerMainList";
		
		$(gridId).jqGrid('GridUnload');
		$(gridId).jqGrid({
			url : ajaxActionUrl,
			mtype : "POST",
			datatype : "json",
			postData : {
				ajaxAction : 'QueryTotal',
				OP_QUERY_DT_TM_S : getADDate($("#OP_QUERY_DT_TM_S").val()),
				OP_QUERY_DT_TM_E : getADDate($("#OP_QUERY_DT_TM_E").val())
				
			},
			height : "auto",
			autowidth : true,
		        colNames : [ "功能項", "WEB使用筆數", "ANDROID使用筆數", "IOS使用筆數", "查有資料筆數", "查無資料筆數", "總筆數" ],
			colModel : [ {
				name : 'OP_QUERY_NM',
				index : 'OP_QUERY_NM',
				width : 50
			}, {
				name : 'OP_WEB_COUNT',
				index : 'OP_WEB_COUNT',
				width : 50
			}, {
				name : 'OP_ANDROID_COUNT',
				index : 'OP_ANDROID_COUNT',
				width : 50
			}, {
				name : 'OP_IOS_COUNT',
				index : 'OP_IOS_COUNT',
				width : 50
			}, {
				name : 'OP_STATUS_S',
				index : 'OP_STATUS_S',
				width : 50
			}, {
				name : 'OP_STATUS_E',
				index : 'OP_STATUS_E',
				width : 50
			}, {
				name : 'OP_STATUS_T',
				index : 'OP_STATUS_T',
				width : 50
			} ],
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
                        loadComplete: function (data) {
                            $.unblockUI();
                        },
			onCellSelect : function(row, col, content, event) {
				var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
				selectedColumnName = cm[col].name;
			},
			onSelectRow : function(id, status) {
				
			}
		});
		$(gridId).jqGrid("clearGridData", true);
		$(gridId).setGridWidth($(window).width() - 10);
		$(gridId).trigger("reloadGrid");
                
	}
}

// 查詢條件 清除條件
function cleanEditor() {
        dateRange('OP_QUERY_DT_TM_S', 'OP_QUERY_DT_TM_E');
	$('#gridMainList').jqGrid('clearGridData');
}

function checkList(inForm) {
    Validator.init(inForm);
    
    if (Validator.isValid())
    	return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}