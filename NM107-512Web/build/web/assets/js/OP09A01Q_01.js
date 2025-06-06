var strPermissionData;

$(document).ready(function() {
        time(1);
	$('#menu09').addClass("active");
	$('#menu09span').addClass("selected");
        $('#menu0901').addClass("active");
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
        dateRange('OP_SCHDL_DT_S', 'OP_SCHDL_DT_E');
        
        if (checkList(document.getElementById('form1')))
            showGridListbyQS();
}

var ajaxActionUrl = 'SystemMaintainServlet';

function showGridListbyQS() {
        var nowDate = new Date();
        nowDate = getROCDateSlash(nowDate);
	var bool =true;
        var msg = "";
        if( ($('#OP_SCHDL_DT_S').val() == "") ){
            msg += '排程日期起日不可為空<br/>';
            bool = false;
        }
        if( ($('#OP_SCHDL_DT_E').val() == "") ){
            msg += '排程日期迄日不可為空<br/>';
            bool = false;
        }
        //排程日期
        if( ($('#OP_SCHDL_DT_S').val() != "") && ($('#OP_SCHDL_DT_E').val() != "") ){
            if (($('#OP_SCHDL_DT_S').val() > $('#OP_SCHDL_DT_E').val())){
                msg += '排程日期迄日不可小於排程日期起日<br/>';
		bool = false;
            }
        }
        if( getADDate($('#OP_SCHDL_DT_S').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '排程日期起日不可大於系統日期時間<br/>';
            bool = false;
        }
        if( getADDate($('#OP_SCHDL_DT_E').val().substring(0,9)) >getADDate ( nowDate) ){
            msg += '排程日期迄日不可大於系統日期時間<br/>';
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
            var selectedColumnName;
            var gridId = "#gridMainList";
            var pagerId = "#pagerMainList";
    
            $( gridId).jqGrid('GridUnload');
            $( gridId).jqGrid({
                    url : 'SystemMaintainServlet',
                    mtype : "POST",
                    datatype : "json",
                    postData : {
                           ajaxAction : 'GTEschedule',
                            OP_SCHDL_DT_S : getADDate($("#OP_SCHDL_DT_S").val()),
                            OP_SCHDL_DT_E : getADDate($("#OP_SCHDL_DT_E").val()),
                            OP_PRCD_CD : $("#OP_PRCD_CD").val(),
                            OP_PRCD_NM : $('#OP_PRCD_CD').val() == '' ? '' : $('#OP_PRCD_CD option:selected').text()
                    },
                    height : "auto",
                    autowidth : true,
                    colNames : [ "作業類別代碼", "作業類別名稱", "排程開始日期", "排程開始時間", "排程結束日期", "排程結束時間", "總筆數", "成功筆數", "OP_SEQ_NO" ],
                    colModel : [ {
                                    name : 'OP_PRCD_CD',
                                    index : 'OP_PRCD_CD',
                                    width : 30
                            }, {
                                    name : 'OP_PRCD_NM',
                                    index : 'OP_PRCD_NM',
                                    width : 70
                            }, {
                                    name : 'OP_SCHDL_DT_S',
                                    index : 'OP_SCHDL_DT_S',
                                    width : 50
                            }, {
                                    name : 'OP_SCHDL_TM_S',
                                    index : 'OP_SCHDL_TM_S',
                                    width : 50
                            }, {
                                    name : 'OP_SCHDL_DT_E',
                                    index : 'OP_SCHDL_DT_E',
                                    width : 50
                            }, {
                                    name : 'OP_SCHDL_TM_E',
                                    index : 'OP_SCHDL_TM_E',
                                    width : 50
                            }, {
                                    name : 'OP_SUCCESS_NUM',
                                    index : 'OP_SUCCESS_NUM',
                                    width : 20
                            }, {
                                    name : 'OP_TOTAL_NUM',
                                    index : 'OP_TOTAL_NUM',
                                    width : 20
                            }, {
                                    name : 'OP_SEQ_NO',
                                    index : 'OP_SEQ_NO',
                                    hidden : true
                            // 欄位隱藏
                            } ],
                    viewrecords : true,
                    gridview: true,
                    altRows: true,
                    pgbuttons: true,
                    pginput: true,
                    rowNum: 10,
                    rowList: [10, 20, 30],
                    pager: pagerId,
                    loadonce : true,
                    multiselect: false,
                    loadComplete: function (data) {
                        $.unblockUI();
                    },
                    onCellSelect : function(row, col, content, event) {
                            var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
                            selectedColumnName = cm[col].name;
                    },
                    onSelectRow: function (id, status) {
                    },
                    gridComplete: function () {
                    }
            });
            $(gridId).jqGrid("clearGridData", true);
            $(gridId).trigger("reloadGrid");
            
	}
}

// 查詢條件 清除條件
function cleanEditor() {
	dateRange('OP_SCHDL_DT_S', 'OP_SCHDL_DT_E');
        //是否審核 default N
        $("select[id='OP_PRCD_CD']").val("");
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