var strPermissionData;
$(document).ready(function() {
        time(1);
	$('#menu09').addClass("active");
	$('#menu09span').addClass("selected");
	$('#menu0903').addClass("active");
	init();
});

// #region Front End Event

function init() {
	strPermissionData = getUserRole();
	if (strPermissionData["RolePermission"] == ''){
		$.alert.open('error', '遺失使用者資訊，請重新登入');
		window.close();
	}
	// 帶入單位下拉式選單選項
        bindROLEDDL('OP_ROLE_ID');
        bindFUNCDDL('OP_FUNC_ID');
        
        showgridMainList();
        
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
        //查詢
	$('#queryBtn').click(function() {
                showgridMainList();
	});
        //清除
	$('#QS_CLEAR').click(function() {
		cleanEditor();
	});
        //啟用
        $('#ENBtn').click(function() {
            confirmAlter('Y');
	});
        //不啟用
        $('#DISBtn').click(function() {
            confirmAlter('N');
	});
}
// 註冊事件區---End

function initDefaultQS() {
    
}

var ajaxActionUrl = 'SystemMaintainServlet';
//function  btnSave(){
//	controlBtn('EDIT', 'VIEW');
//		
//	var ajData = {
//			'ajaxAction' : 'insertList',
//			'CP_ROLE_CD' : $('#CP_ROLE_CD').val(),	
//			'CP_FUNC_ID' : getValue()			
//		};
//		var ajSucc = function(JData) {
//			if (JData.formData){
//				
//					notyMsg('修改成功');
//					$('#addListBtn').removeAttr('disabled');
//					$('#updateListBtn').attr('disabled','');
//								
//			}
//			else
//				 $.alert.open('error', "修改失敗!!!");
//		};
//		var ajErr = function() {
//			 $.alert.open('error', "修改失敗!!!");
//		};
//
//		$.ajax({
//			url : ajaxActionUrl,
//			type : "POST",
//			dataType : "json",
//			data : ajData,
//			success : ajSucc,
//			error : ajErr
//		});
//	
//}

function showgridMainList() {
    var selectedColumnName;
    var gridId = "#gridMainList";
    var pagerId = "#pagerMainList";
    
    show_BlockUI_page_noParent('資料準備中…');   
    $( gridId).jqGrid('GridUnload');
	$( gridId).jqGrid(
        {
                url : 'SystemMaintainServlet',
                mtype : "POST",
                datatype : "json",
                postData : {
                        ajaxAction : 'GET_FUNC',
                        OP_FUNC_GROUP : $('#OP_FUNC_ID').val(),
                        OP_FUNC_NM : $('#OP_FUNC_ID option:selected').text(),
                        OP_ROLE_ID : $('#OP_ROLE_ID').val(),
                        OP_ROLE_NM : $('#OP_ROLE_ID option:selected').text(),
                        OP_ENABLED : $('#OP_ENABLED').val(),
                        OP_ENABLED_NM : $('#OP_ENABLED option:selected').text()
                },
                height : "auto",
                autowidth : true,
                colNames : ["角色","功能名稱","是否啟用","",""],
                colModel : [ {
                        name : 'OP_ROLE_NM',
                        index : 'OP_ROLE_NM',
                        width : 80
                }, {
                        name : 'OP_FUNC_NM',
                        index : 'OP_FUNC_NM',
                        width : 80
                }, {
                        name : 'OP_ENABLED_CHT',
                        index : 'OP_ENABLED_CHT',
                        width : 50
                }, {
                        name : 'OP_ROLE_ID',
                        index : 'OP_ROLE_ID',
                        hidden : true
                }, {
                        name : 'OP_FUNC_ID',
                        index : 'OP_FUNC_ID',
                        hidden : true 
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
                multiselect: true,
                loadComplete: function (data) {
                    $.unblockUI();
                },
                onCellSelect : function(row, col, content, event) {
                        var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
                        selectedColumnName = cm[col].name;
                },
                onSelectRow: function (id, status) {
                    if (selectedColumnName!= 'cb') {   
                          $(gridId).setSelection(id, false);			  
                    }
                },
                gridComplete: function () {
                    $(gridId).setJgridRowCSS();
                }
        });
	$(gridId).jqGrid("clearGridData", true);
	$(gridId).trigger("reloadGrid");
}

function confirmAlter(OP_ENABLED){
    var $myGrid = $('#gridMainList');
	var indexInJqgrid = $myGrid.jqGrid('getGridParam', 'selarrrow');
	var delIDs = "",name="";
        for (var i = 0; i < indexInJqgrid.length; i++) {
		var selRowId = indexInJqgrid[i];
		var selFUNCID = $myGrid.jqGrid('getCell', selRowId, 'OP_FUNC_ID');
                var selROLEID = $myGrid.jqGrid('getCell', selRowId, 'OP_ROLE_ID');
                delIDs = delIDs.concat(',', selFUNCID+';'+selROLEID);
                var selFUNCNM = $myGrid.jqGrid('getCell', selRowId, 'OP_FUNC_NM');
                var selROLENM = $myGrid.jqGrid('getCell', selRowId, 'OP_ROLE_NM');
                name = name.concat(',',selFUNCNM+";"+selROLENM);
	}
	delIDs = delIDs.substring(1);
        name = name.substring(1);
        var tmpmsg = '';
        if (OP_ENABLED=='Y')
            tmpmsg = '角色功能啟用中，確定是否要啟用?';
        else
            tmpmsg='角色功能停止啟用中，確定是否要停止啟用?';
        if (delIDs.length>0){
            $.alert.open({
                    type : 'confirm',
                    content : tmpmsg,
                    callback : function(button, value) {
                            if (button == 'yes') 
                                    DeleteBtn(delIDs,name,OP_ENABLED);

                    }
            });
        }else
            $.alert.open('error', "請選任一筆資料!!!")
}
function DeleteBtn(delIDs,name,OP_ENABLED){
    
	show_BlockUI_page_noParent('資料儲存中…');
	$.ajax({
		url :'SystemMaintainServlet',
		type : "POST",
		dataType : "json",
		data : {
			ajaxAction : 'FUNC_ENABLE',
                        OP_ENABLED :OP_ENABLED,
                        OP_FUNC_NM : name,
			OP_FUNC_ID : delIDs
		},
		clearForm : true,
		resetForm : true,
		success : function(data) {
			showgridMainList();
			notyMsg('已註記資料', 'success');
                        $.unblockUI();
		}
	});
}

// 查詢條件 清除條件
function cleanEditor() {
	$('#queryporlet input[type="text"]').val('');
        $('#OP_ROLE_ID').val('');
        $('#OP_FUNC_ID').val('');
        $('#OP_ENABLED').val('');
        $('#gridMainList').jqGrid('clearGridData');
}

//角色下拉顯示方式
function bindROLEDDL(ROLE) {

    var ajData = {
        'ajaxAction': 'FUNC'
    };

    bindFormControlDDL(ROLE, ajData, '請選擇角色...', 'OP_ROLE_ID', 'OP_ROLE_NM');

}

//系統功能下拉顯示方式
function bindFUNCDDL(FUNCGROUP) {

    var ajData = {
        'ajaxAction': 'FUNCGROUP'
    };

    bindFormControlDDL(FUNCGROUP, ajData, '請選擇群組...', 'OP_FUNC_ID', 'OP_FUNC_NM');

}
