var isEditUser;
var strPermissionData;
var vForm;
var vOkColor = '';
//ID暫存
//var oldFormData;
// 每次查詢應該備份查詢條件，提供 修改/刪除時，重刷jqGrid最後查詢條件使用。
var qsObj = {};
qsObj.cached_lastSearchData = {};
//region Action Post
var ajaxActionUrl = "SystemMaintainServlet?action=";
var queryDate,
        qStartTime,
        qEndTime;
////dialog參數
//var dialogParam = {
//		editIssue : publicPassFunc(),//判斷是新增or編輯
//		no : publicPassFunc(),
//		order : publicPassFunc(),
//		qsObjData: qsObj,
//		QsMedia : publicPassFunc(),
//		QsCcode : publicPassFunc(),
//		QsIssue : publicPassFunc(),
//		QStartTime : publicPassFunc(),
//		QEndTime : publicPassFunc(),
//		issueType : publicPassFunc()
//}
//傳遞dialog用參數
//function publicPassFunc() {
//    return {
//        value: '',
//        get: function () {
//            return this.value;
//        },
//        set: function (val) {
//            this.value = '';
//            if (val != null && val.length > 0) {
//                this.value = val;
//            }
//        }
//    };
//}
$(document).ready(function () {
    time(1);
    $('#menu09').addClass("active");
    $('#menu09span').addClass("selected");
    $('#menu0907').addClass("active");
    init();
//    $('#gvMainBtnDelete').click(function() {
//		if (isDeleteSelectNothing() == true) {
//		    alertPleaseSelectOne();
//		    return false;
//		}
//		else {
//			alertDelete();
//		}
//    });

    //$('#jqNavLabel7Date font').text(initDefaultQS);
    //queryDate = queryTodayAndNextday();
    //showGridListbyDefaultQS();
//    init();
    // 宣告視窗大小更改事件
    $(window).bind('resize', function () {
        var gridId = "#gridMainList";
        $(gridId).setGridWidth($('#mainList').width(), true);
    }).trigger('resize');
});

function init() {
    strPermissionData = getUserRole();
    if (strPermissionData["RolePermission"] == '') {
        $.alert.open('error', '遺失使用者資訊，請重新登入');
        window.close();
    }
    bindUNITDDL('1', 'OP_UNIT_CD', '', '', 'ALL');

    //依照登入單位鎖定三層下拉選單
    initEvent();
}
function initEvent() {
    //帶入預設值
    initDefaultQS();
    //按鍵事件區
    registButton();
}

//function initUnitByPermission(){
//    bindUNITDDL('2', 'TM_PR_D_UNIT_CD', 'TM_PR_B_UNIT_CD', 'TM_PR_UNIT_CD', 'ALL');
//}
// 註冊事件區---Start
function registButton() {
    $('#QueryBtn').click(function () {
        if (checkList(document.getElementById('form1')))
            showgridMainList();
    });
    //新增畫面
    $('#saveCheckBtn').click(function () {
        save('INS');
    });
    //轉跳新增畫面
    $('#ADDBtn').click(function () {
        goTab('modal-editor-autocase', 'tabMain');
        cleanEditor();
        initUnitByPermission2();
    });
    //刪除
    $('#delBtn').click(function () {
        confirmAlter();
    });
    //新增畫面修改
    $('#updateCheckBtn').click(function () {
        save('');
    });
    //新增畫面清除
    $('#CLEAR').click(function () {
        cleanEditor();
        initUnitByPermission2();
    });
}

function showgridMainList() {
    var bool = true;
    var selectedColumnName;
    if (bool) {
        show_BlockUI_page_noParent('資料準備中…');
        var selectedColumnName;
        var gridId = "#gridMainList";
        var pagerId = "#pagerMainList";

        $(gridId).jqGrid('GridUnload');
        $(gridId).jqGrid({
            url: 'SystemMaintainServlet',
            mtype: "POST",
            datatype: "json",
            postData: {
                ajaxAction: 'getMailInfo',
                OP_UNIT_CD: $("#OP_UNIT_CD").val(),
                OP_UNIT_NM: $('#OP_UNIT_CD').val() == '' ? '' : $('#OP_UNIT_CD option:selected').text()
            },
            height: "auto",
            autowidth: true,
            colNames: ["OP_SEQ_NO", 'OP_UNIT_CD', "承辦單位", "EMAIL"],
            colModel: [
                {name: 'OP_SEQ_NO', index: 'OP_SEQ_NO', hidden: true}, // 欄位隱藏
                {name: 'OP_UNIT_CD', index: 'OP_UNIT_CD', hidden: true},
                {name: 'OP_UNIT_NM', index: 'OP_UNIT_NM', align: 'center', width: 50},
                {name: 'OP_EMAIL', index: 'OP_EMAIL', align: 'center', width: 50}
            ],
            viewrecords: true,
            gridview: true,
            pgbuttons: true,
            pginput: true,
            rowNum: 10,
            rowList: [5, 10, 20, 30],
            pager: pagerId,
            loadonce: true,
            altRows: true,
            loadComplete: function (data) {
                $.unblockUI();
            },
            onCellSelect: function (row, col, content, event) {
                var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
                selectedColumnName = cm[col].name;
            },
            onSelectRow: function (id, status) {
                if (selectedColumnName != 'cb') {
                    $(gridId).setSelection(id, false);
                }
                if (selectedColumnName != 'action' && selectedColumnName != 'cb')
                    goTab('modal-editor-autocase', 'tabMain');
                var row = $(gridId).jqGrid('getRowData', id);
                var no = row.OP_SEQ_NO;
                $('#HidTM_SEQ_NO').val(no);
                document.getElementById("OP_unitLevel2").disabled = true;
                $('#OP_unitLevel2').val(row.OP_UNIT_NM);
                $('#OP_EMAIL').val(row.OP_EMAIL);
                $('#updateCheckBtn').show();
                $('#saveCheckBtn').hide();
            },
            gridComplete: function () {
                $(gridId).setJgridRowCSS();
                $.unblockUI();
            }

        });
    }
    $(gridId).jqGrid("clearGridData", true);
    $(gridId).setGridWidth($(window).width() - 20);
    $(gridId).trigger("reloadGrid");
}


function  save(type) {
    var bool = true;
    var msg = '';
    if ($('#OP_unitLevel2').val() == "") {
        msg += "承辦單位不可空白！<br/>";
        bool = false;
    }
    if ($('#OP_EMAIL').val() != "") {
        if (checkEmail("OP_EMAIL", true, "EMAIL") == false) {
            msg += "請輸入正確EMAIL格式！<br/>";
            bool = false;
        }
    }

//    if (type == 'INS') { //新增
//        if ($('#OP_unitLevel2').val() != "" && $('#OP_unitLevel3').val() != "" && $('#OP_EMAIL_NM').val() != "" && $('#OP_EMAIL').val() != "") {
//            if (checkUnitForMailN($('#OP_unitLevel2').val(), $('#OP_unitLevel3').val(), $('#OP_EMAIL').val()) == "Y") {
//                msg += $('#OP_unitLevel2 option:selected').text() + $('#OP_unitLevel3 option:selected').text() + "  " + $('#OP_EMAIL_NM').val() + "EMAIL資料已建檔，請用修改功能修改資料。<br/>";
//                bool = false;
//            }
//        } else if ($('#OP_unitLevel2').val() != "" && $('#OP_EMAIL_NM').val() != "" && $('#OP_EMAIL').val() != "") {
//            if (checkUnitForMailN($('#OP_unitLevel2').val(), $('#OP_unitLevel3').val(), $('#OP_EMAIL').val()) == "Y") {
//                msg += $('#OP_unitLevel2 option:selected').text() + "  " + $('#OP_EMAIL_NM').val() + "EMAIL資料已建檔，請用修改功能修改資料。<br/>";
//                bool = false;
//            }
//        }
//    }
    if (bool) {
        show_BlockUI_page_noParent('資料儲存中…');
        var ajData = {
            'ajaxAction': type == 'INS' ? 'doSaveByOP09A07Q' : 'doUpdateByOP09A07Q',
            'OP_SEQ_NO': $('#HidTM_SEQ_NO').val(),
            'OP_EMAIL': $('#OP_EMAIL').val()
        };
        var ajSucc = function (JData) {
            if (JData.formData) {
                if (JData.formData[0]["msg"] == '') {
                    if (type == 'INS') {
                        notyMsg('新增成功', 'success');
                        $('#HidTM_SEQ_NO').val(JData.formData[0]["OP_SEQ_NO"]);
                        $('#updateCheckBtn').show();
                        $('#CLEAR').show();
                        $('#saveCheckBtn').hide();
                    } else {
                        notyMsg('修改成功', 'success');
                        $('#HidTM_SEQ_NO').val(JData.formData[0]["OP_SEQ_NO"]);
                        $('#updateCheckBtn').show();
                        $('#CLEAR').show();
                        $('#saveCheckBtn').hide();
                    }
                    goTab('tabMain', 'modal-editor-autocase');
                    showgridMainList();
                    cleanDetail();
                } else
                {
                    alert(JData.formData[0]["msg"]);
                }
            } else
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
            async: false,
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
function confirmAlter() {
    var $myGrid = $('#gridMainList');
    var indexInJqgrid = $myGrid.jqGrid('getGridParam', 'selarrrow');
    var delIDs = "", dname = "", bname = "", EMAILid = "", EMAILname = "", EMAIL = "";
    if (indexInJqgrid == undefined) {
        $.alert.open('error', "請查詢後，選擇任一筆資料!!!");
    } else {
        for (var i = 0; i < indexInJqgrid.length; i++) {
            var selRowId = indexInJqgrid[i];
            var selTableID = $myGrid.jqGrid('getCell', selRowId, 'OP_SEQ_NO');
            delIDs = delIDs.concat(',', selTableID);

            var selTabledName = $myGrid.jqGrid('getCell', selRowId, 'OP_D_UNIT_NM');
            dname = dname.concat(',', selTabledName);

            var selTablebName = $myGrid.jqGrid('getCell', selRowId, 'OP_B_UNIT_NM');
            bname = bname.concat(',', selTablebName);

            var selTablebEMAILname = $myGrid.jqGrid('getCell', selRowId, 'OP_EMAIL_NM');
            EMAILname = EMAILname.concat(',', selTablebEMAILname);

            var selTablebEMAIL = $myGrid.jqGrid('getCell', selRowId, 'OP_EMAIL');
            EMAIL = EMAIL.concat(',', selTablebEMAIL);

            var selTablebEMAILid = $myGrid.jqGrid('getCell', selRowId, 'OP_EMAIL_ID');
            EMAILid = EMAILid.concat(',', selTablebEMAILid);
        }
        delIDs = delIDs.substring(1);
        dname = dname.substring(1);
        bname = bname.substring(1);
        EMAILname = EMAILname.substring(1);
        EMAIL = EMAIL.substring(1);
        EMAILid = EMAILid.substring(1);
        if (delIDs.length > 0) {
            $.alert.open({
                type: 'confirm',
                content: '資料刪除中，確定是否要刪除?',
                callback: function (button, value) {
                    if (button == 'yes')
                        DeleteBtn(delIDs, dname, bname, EMAILname, EMAIL, EMAILid);

                }
            });
        } else
            $.alert.open('error', "請選任一筆資料!!!")
    }
}
function DeleteBtn(delIDs, dname, bname, EMAILname, EMAIL, EMAILid) {


    $.ajax({
        url: 'SystemMaintainServlet',
        type: "POST",
        dataType: "json",
        data: {
            ajaxAction: 'doDeleteByOP09A07Q',
            OP_SEQ_NO: delIDs,
            OP_D_UNIT_NM: dname,
            OP_B_UNIT_NM: bname,
            OP_EMAIL_NAME: EMAILname,
            OP_EMAIL: EMAIL,
            OP_EMAIL_ID: EMAILid
        },
        clearForm: true,
        resetForm: true,
        success: function (data) {
            showgridMainList();
//			notyMsg('已刪除 ' + name.split(",").length + ' 筆資料','error');
            notyMsg('已刪除資料', 'success');
        }
    });
}

function cleanDetail() {
    $('#OP_unitLevel2').val('');
    $('#OP_EMAIL').val('');
}

function cleanEditor() {

    $('#OP_unitLevel2').val('');
    $('#OP_EMAIL').val('');
    $('#updateCheckBtn').hide();
    $('#CLEAR').show();
    $('#HidTM_SEQ_NO').val('');
}
//function AuditNameDDL() {
//    if ($('#OP_unitLevel3').val() != '') {
//        OP_B_UNIT_CD = $('#OP_unitLevel3').val();
//        bindAuditNameDDL_1('OP_EMAIL_NM', OP_B_UNIT_CD, '2', 'OP_EMAIL_NM', 'OP300004');
//    } else if ($('#OP_unitLevel2').val() != '') {
//        OP_D_UNIT_CD = $('#OP_unitLevel2').val();
//        bindAuditNameDDL_1('OP_EMAIL_NM', TM_D_UNIT_CD, '1', 'OP_EMAIL_NM', 'OP300004');
//    }
//}
//CHECK---START
function checkUnitForMailN(OP_D_UNIT_CD, OP_B_UNIT_CD, OP_EMAIL) {
    var bool;
    var ajData = {
        'ajaxAction': 'checkUnitForMailN',
        'OP_D_UNIT_CD': OP_D_UNIT_CD,
        'OP_B_UNIT_CD': OP_B_UNIT_CD,
        'OP_EMAIL': OP_EMAIL
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            var formData = JData.formData[0];
            bool = formData['CHECK'];
        }
    };
    var ajErr = function () {

    };

    $.ajax({
        url: 'SystemMaintainServlet',
        type: "POST",
        dataType: "json",
        async: false,
        data: ajData,
        success: ajSucc,
        error: ajErr
    });
    return bool;
}
//CHECK---END
//function getUserRole() {
//    var returnValue = '';
//    var ajData = {
//        'ajaxAction': 'PERMISSION'
//    };
//
//    $.ajax({
//        url: 'UtilServlet',
//        type: 'POST',
//        async: false,
//        datatype: 'json',
//        data: ajData,
//        success: function (data) {
//            if (data.formData != null)
//                returnValue = data.formData;
//        }
//    });
//
//    return returnValue;
//}
function checkList(inForm) {
    Validator.init(inForm);

    if (Validator.isValid())
        return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
function Method_init(form) {
    vErrorMsg = '';
    vErrorIdList = '';
    vForm = form;
    for (var i = 0; i < vForm.length; i++) {
        var vItem = vForm.elements[i];
        if (vItem.type == "text" || vItem.type == "textarea"
                || vItem.type == "radio" || vItem.type == "checkbox") {
            vItem.style.backgroundColor = vOkColor;
        } else if (vItem.type == "select-one"
                || vItem.type == "select-multiple") {
            vItem.style.backgroundColor = vOkColor;
            for (var j = 0; j < vItem.length; j++)
                vItem.options[j].style.backgroundColor = vOkColor;
        }
    }
}
function Method_isValid() {
    if (vErrorMsg == '')
        return true;
    else
        return false;
}
function Method_showMessage() {
    if (vErrorMsg.length != 0)
        $.alert.open(vErrorMsg);
}
function initDefaultQS() {

    // 帶入登入者單位
    if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1) {

    } else if (strPermissionData["RolePermission"] == '1') {
        $("#OP_unitLevel2").prop('disabled', true);
        $("#OP_UNIT_CD").prop('disabled', true);
    } else if (strPermissionData["RolePermission"] == '2') {
        $("#OP_unitLevel2").prop('disabled', true);
        $("#OP_UNIT_CD").prop('disabled', true);
    } else if (strPermissionData["RolePermission"] == '3') {
        $("#OP_unitLevel2").prop('disabled', true);
        $("#OP_UNIT_CD").prop('disabled', true);
    }

    // 帶入登入者單位
    getUnit('OP_UNIT_CD');
    // 先帶出查詢結果
    if (checkList(document.getElementById('form1')))
        showgridMainList();
}
function initUnitByPermission2() {
    strPermissionData = getUserRole();
    if (strPermissionData["RolePermission"] == '') {
        $.alert.open('error', '遺失使用者資訊，請重新登入');
        window.close();
    } else {
        //set 三層式選單
        // 帶入登入者單位
        if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1) {

        } else if (strPermissionData["RolePermission"] == '1') {
            $("#OP_unitLevel2").prop('disabled', true);
            $("#OP_UNIT_CD").prop('disabled', true);
        } else if (strPermissionData["RolePermission"] == '2') {
            $("#OP_unitLevel2").prop('disabled', true);
            $("#OP_UNIT_CD").prop('disabled', true);

        } else if (strPermissionData["RolePermission"] == '3') {
            $("#OP_unitLevel2").prop('disabled', true);
            $("#OP_UNIT_CD").prop('disabled', true);
        }

        // 帶入登入者單位
        getUnit('OP_UNIT_CD');
    }
}

function detail(OP_SEQ_NO) {

    var ajData = {
        'ajaxAction': 'UPD_PARAMETER_SIGN',
        'OP_SEQ_NO': OP_SEQ_NO
    };

    $.ajax({
        url: 'SystemMaintainServlet',
        type: "POST",
        dataType: "json",
        data: ajData,
        success: function (data, textStatus, xhr) {
            var formData = data.formData[0];
            var data1 = "";
            initUnitByPermission2();
            
            $('#OP_EMAIL').val(formData['OP_EMAIL']);
        }
    });
}
function checkEmail(iNm, isRequired, iTitle)
{
    var iValue = $('#OP_EMAIL').val().trim();

    //20230515 警署需求取消email格式檢核機制(剩下只檢查@)
    //var pattern = new RegExp("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
      var pattern = new RegExp("@");

    // test for pattern
    flag = pattern.test(iValue);

    if (iValue.length != 0 && flag == false) {
        ;
        return false;
    }
    return true;
}

function getUnit(id){
    var ajData = {
      'ajaxAction':'getMailUnit'
    };
    $.ajax({
        url: 'SystemMaintainServlet',
        type: "POST",
        dataType: "json",
        data: ajData,
        success:function(data, textStatus, xhr){
            setDDLValue(data, id);
        }
    });
}

$('#OP_UNIT_CD').on('change', function(){
    showgridMainList();
});

