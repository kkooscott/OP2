var strPermissionData;

$(document).ready(function() {
        time(1);
        strPermissionData = getUserRole();
	$('#menu09').addClass("active");
	$('#menu09span').addClass("selected");
        $('#menu0906').addClass("active");
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
	bindUNITDDL('2', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', '', 'ALL');
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
            if (checkList(document.getElementById('form1')))
                    showGridListbyQS();
	});
        //清除
        $('#QS_CLEAR').click(function() {
		cleanEditorForQuery();
	});
        //轉跳新增畫面
        $('#addMailPage').click(function () {
            cleanEditor();
            goTab('modal-editor-add', 'tabMain');
        });
        //新增畫面新增
        $('#saveCheckBtn').click(function() {
            save('INS');
	});
        //新增畫面修改
        $('#updateCheckBtn').click(function () {
            save('');
        });
        //新增畫面清除
        $('#CLEAR').click(function() {
            cleanEditor();
	});
}

function save(type) {
    var bool = true;
    var msg = '';
    if ($('#OP_unitLevel2_INS').val() == "" || $('#OP_unitLevel3_INS').val() == ""){
         msg += "單位不可空白！<br/>";
         bool =false;
    }
    if ( $('#OP_MAIL_CONTENT').val() == "" ){
         msg += "信件內容不可空白！<br/>";
         bool =false;
    }
    
    if( type == 'INS' ){ //新增
        if ($('#OP_unitLevel2_INS').val() != "" && $('#OP_unitLevel3_INS').val() != ""){
            if( checkUnitForMail( $('#OP_unitLevel2_INS').val(), $('#OP_unitLevel3_INS').val() ) == "Y" ){
                msg += $('#OP_unitLevel3_INS option:selected').text() + "資料已建檔，請用維護功能維護資料。<br/>";
                bool =false;
            }
        }
    }
    
    if (bool) {
       show_BlockUI_page_noParent('資料儲存中…');
        var ajData = {
            'ajaxAction': type == 'INS' ? 'doSaveByOP09A06Q' : 'doUpdateByOP09A06Q',
            'OP_SEQ_NO':$('#HidOP_SEQ_NO').val(),
            'OP_D_UNIT_CD': $('#OP_unitLevel2_INS').val(),
            'OP_D_UNIT_NM': $('#OP_unitLevel2_INS option:selected').text(),
            'OP_B_UNIT_CD': $('#OP_unitLevel3_INS').val(),
            'OP_B_UNIT_NM': $('#OP_unitLevel3_INS option:selected').text(),
            'OP_MAIL_CONTENT': $('#OP_MAIL_CONTENT').val()
        };
        var ajSucc = function (JData) {
            if (JData.formData) {
                if (JData.formData[0]["msg"] == '') {
                    if (type == 'INS') {
                        notyMsg('新增成功', 'success');
                        $('#HidOP_SEQ_NO').val( JData.formData[0]["OP_SEQ_NO"] );
                        $('#updateCheckBtn').show();
                        $('#CLEAR').show();
                        $('#saveCheckBtn').hide();
                    }
                    else {
                        notyMsg('修改成功', 'success');
                        $('#HidOP_SEQ_NO').val( JData.formData[0]["OP_SEQ_NO"] );
                        $('#updateCheckBtn').show();
                        $('#CLEAR').show();
                        $('#saveCheckBtn').hide();
                    }
                    showGridListbyQS();
                }
                else
                {
                    alert(JData.formData[0]["msg"]);
                }
            }
            else
                type == 'INS' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
            $.unblockUI();
        };
        var ajErr = function () {
            $.unblockUI();
            type == 'INS' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
            $.unblockUI();
        };

        $.ajax({
            url: 'SystemMaintainServlet',
            type: "POST",
            dataType: "json",
            async : false,
            data: ajData,
            beforeSend: function () {
                $.blockUI({message: '<div>儲存中，請稍候...</div>'});
            },
            complete: function () {
                $.unblockUI();
            },
            success: ajSucc,
            error: ajErr
        });
    } else
        $.alert.open('error', msg);
}

// 註冊事件區---End

function initDefaultQS() {
        // 帶入登入者單位
	if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1){
		
	}else if (strPermissionData["RolePermission"] == '1' ){
		$("#OP_Search_unitLevel2").prop('disabled', true);
		$("#OP_Search_unitLevel3").prop('disabled', true);
                $("#OP_unitLevel2_INS").prop('disabled', true);
		$("#OP_unitLevel3_INS").prop('disabled', true);
	}else if (strPermissionData["RolePermission"] == '2' ){
		$("#OP_Search_unitLevel2").prop('disabled', true);
		$("#OP_Search_unitLevel3").prop('disabled', true);
                $("#OP_unitLevel2_INS").prop('disabled', true);
		$("#OP_unitLevel3_INS").prop('disabled', true);
		
	}else if (strPermissionData["RolePermission"] == '3' ){
		$("#OP_Search_unitLevel2").prop('disabled', true);
                $("#OP_unitLevel2_INS").prop('disabled', true);
	}
        
	// 帶入登入者單位
        $("#OP_Search_unitLevel2").val(strPermissionData['UNITLEVEL1']);
        bindUNITDDL('3', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', '', '');
	$('#OP_Search_unitLevel3').val(strPermissionData['UNITLEVEL2']);
        
        $("#OP_unitLevel2_INS").val(strPermissionData['UNITLEVEL1']);
        bindUNITDDL('3', 'OP_unitLevel2_INS', 'OP_unitLevel3_INS', '', '');
	$('#OP_unitLevel3_INS').val(strPermissionData['UNITLEVEL2']);
         // 先帶出查詢結果
         if (checkList(document.getElementById('form1')))
            showGridListbyQS();
}

function showGridListbyQS() {
	var bool =true;
	
	if (bool){
            show_BlockUI_page_noParent('資料準備中…');
		var gridId = "#gridMainList";
		var pagerId = "#pagerMainList";
		
		$(gridId).jqGrid('GridUnload');
		$(gridId).jqGrid({
			url : 'SystemMaintainServlet',
			mtype : "POST",
			datatype : "json",
			postData : {
				ajaxAction : 'QueryMAIL_PARA',
				OP_D_UNIT_CD : $("#OP_Search_unitLevel2").val(),
                                OP_D_UNIT_NM : $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
				OP_B_UNIT_CD : $("#OP_Search_unitLevel3").val(),
                                OP_B_UNIT_NM : $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text()
			},
			height : "auto",
			autowidth : true,
			colNames : [ "單位", "信件內容", "OP_SEQ_NO","OP_B_UNIT_CD","OP_D_UNIT_CD" ],
			colModel : [ {
				name : 'OP_B_UNIT_NM',
				index : 'OP_B_UNIT_NM',
				width : 70
			}, {
				name : 'OP_MAIL_CONTENT',
				index : 'OP_MAIL_CONTENT',
				width : 100
			
                        }, {
				name : 'OP_SEQ_NO',
				index : 'OP_SEQ_NO',
				hidden : true
			// 欄位隱藏
                        }, {
				name : 'OP_B_UNIT_CD',
				index : 'OP_B_UNIT_CD',
				hidden : true
			// 欄位隱藏
                        }, {
				name : 'OP_D_UNIT_CD',
				index : 'OP_D_UNIT_CD',
				hidden : true
			// 欄位隱藏
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
				var row = $(gridId).jqGrid('getRowData', id);
				var no = row.OP_SEQ_NO;
				$('#HidOP_SEQ_NO').val(no);
                                
				$('#updateCheckBtn').show();
                                $('#CLEAR').show();
                                $('#saveCheckBtn').hide();
                                $('#OP_unitLevel2_INS').val(row.OP_D_UNIT_CD);
                                if(row.OP_D_UNIT_CD != ''){
                                    bindUNITDDL('3', 'OP_unitLevel2_INS', 'OP_unitLevel3_INS', '', '');
                                    $('#OP_unitLevel3_INS').val(row.OP_B_UNIT_CD);
                                }
                                $('#OP_MAIL_CONTENT').val(row.OP_MAIL_CONTENT);
                                goTab('modal-editor-add', 'tabMain');
			}
		});
		$(gridId).jqGrid("clearGridData", true);
		$(gridId).setGridWidth($(window).width() - 20);
		$(gridId).trigger("reloadGrid");
             
	}
}

//CHECK---START
function checkUnitForMail( OP_D_UNIT_CD, OP_B_UNIT_CD ) {
    var bool;
    var ajData = {
            'ajaxAction' : 'checkUnitForMail',
            'OP_D_UNIT_CD' : OP_D_UNIT_CD,
            'OP_B_UNIT_CD' : OP_B_UNIT_CD,
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            bool = formData['CHECK'];
        }
    };
    var ajErr = function() {
        
    };

    $.ajax({
            url : 'SystemMaintainServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
    return bool;
}
//CHECK---END

// 查詢條件 清除條件
function cleanEditorForQuery() {
        $("#OP_Search_unitLevel2").val(strPermissionData['UNITLEVEL1']);
        bindUNITDDL('3', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', '', '');
        $("#OP_Search_unitLevel3").val(strPermissionData['UNITLEVEL2']);
        $('#gridMainList').jqGrid('clearGridData');
}

function cleanEditor() {
        
        $("#OP_unitLevel2_INS").val(strPermissionData['UNITLEVEL1']);
        bindUNITDDL('3', 'OP_unitLevel2_INS', 'OP_unitLevel3_INS', '', '');
	$('#OP_unitLevel3_INS').val(strPermissionData['UNITLEVEL2']);
        
        $('#OP_MAIL_CONTENT').val('');
        $('#saveCheckBtn').show();
        $('#updateCheckBtn').hide();
        $('#CLEAR').show();
        $('#HidOP_SEQ_NO').val('');
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

// 需求新增區塊(非template提供)----End