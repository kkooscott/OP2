var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu06').addClass("active");
    $('#menu06span').addClass("selected");
    $('#menu0602').addClass("active");
    init();
});

$(window).resize(function () {
    $('#gridMainList').setGridWidth($(window).width() - 10);
    $('#gridClueList').setGridWidth($('.col-md-12').width() - 63);
    $("#gridList_3").setGridWidth($(window).width() - 92);
});

// #region Front End Event

function init() {
    strPermissionData = getUserRole();
    if (strPermissionData["RolePermission"] == '') {
        $.alert.open('error', '遺失使用者資訊，請重新登入');
        window.close();
    }
    // 帶入單位下拉式選單選項
    //bindUNITDDL('2', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', 'ALL');
    initEvent();
}

function initEvent() {
    initDefaultQS();
    registButton();
}

// 註冊事件區---Start
function registButton() {
    $('#queryBtn').click(function () {
        if ($('#OP_STATUS_CD option:selected').val() == "1") {
            if (checkList(document.getElementById('form1')))
                showGridListbyClaim();
        } else if ($('#OP_STATUS_CD option:selected').val() == "2") {
            if (checkList(document.getElementById('form1')))
                showGridListbyIMEI("IMEI");
        } else if ($('#OP_STATUS_CD option:selected').val() == "3") {
            if (checkList(document.getElementById('form1')))
                showGridListbyIMEI("MAC");
        } else if ($('#OP_STATUS_CD option:selected').val() == "4") {
            if (checkList(document.getElementById('form1')))
                showGridListbyIMEI("COLOR");
        } else if ($('#OP_STATUS_CD option:selected').val() == "5") {
            if (checkList(document.getElementById('form1')))
                showGridListbyIMEI("PHONE");
        }
    });
    $('#QS_CLEAR').click(function () {
        cleanEditor();
    });
    $('#OP_STATUS_CD').change(function () {
        $('#gridMainList').jqGrid('clearGridData');
        if ($('#OP_STATUS_CD option:selected').val() == "1") { //認領人
            $('#type1').show();
            $('#label1').show();
            $('#type2').hide();
            $('#type3').hide();
            $('#type4').hide();
            $('#type41').hide();
            $('#type5').hide();
        } else if ($('#OP_STATUS_CD option:selected').val() == "2") { //IMEI
            $('#type1').hide();
            $('#label1').hide();
            $('#type2').show();
            $('#type3').hide();
            $('#type4').hide();
            $('#type41').hide();
            $('#type5').hide();
        } else if ($('#OP_STATUS_CD option:selected').val() == "3") { // MAC
            $('#type1').hide();
            $('#label1').hide();
            $('#type2').hide();
            $('#type3').show();
            $('#type4').hide();
            $('#type41').hide();
            $('#type5').hide();
        } else if ($('#OP_STATUS_CD option:selected').val() == "4") { //廠牌顏色
            bindE8DATATYPE2('C12', 'OP_SEARCH_TYPE_CD', '請選擇...'); //物品類別
            bindCOLORTYPE('', 'OP_SEARCH_COLOR_CD', '請選擇...'); //顏色
            $('#type1').hide();
            $('#label1').hide();
            $('#type2').hide();
            $('#type3').hide();
            $('#type4').show();
            $('#type41').show();
            $('#type5').hide();
        } else if ($('#OP_STATUS_CD option:selected').val() == "5") { // PHONE
            $('#type1').hide();
            $('#label1').hide();
            $('#type2').hide();
            $('#type3').hide();
            $('#type4').hide();
            $('#type41').hide();
            $('#type5').show();
        }
    });
    //判斷廠牌
    $("#OP_SEARCH_TYPE_CD").change(function () {
        var type = $("#OP_SEARCH_TYPE_CD").val();
        var typeName = $('#OP_SEARCH_TYPE_CD').val() == '' ? '' : $('#OP_SEARCH_TYPE_CD option:selected').text();
        if (type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
                || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") {
            bindE8DATATYPE('A16', 'OP_SEARCH_BRAND_CD', '請選擇...'); //廠牌
        }

        if (typeName.indexOf('行動電話') >= 0 || typeName.indexOf('無線電話') >= 0 || typeName.indexOf('手機') >= 0) {
            bindE8DATATYPE('A15', 'OP_SEARCH_BRAND_CD', '請選擇...'); //廠牌
        }

        if (!(type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
                || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") &&
                !(typeName.indexOf('行動電話') >= 0 || typeName.indexOf('無線電話') >= 0 || typeName.indexOf('手機') >= 0)) {
            $('#OP_SEARCH_BRAND_CD').empty();
            $('#OP_SEARCH_BRAND_CD').append("<option value=''>無資料</option>");
        }
    });
}
function registButtonGr() {

}

// 註冊事件區---End

function initDefaultQS() {

}

var ajaxActionUrl = 'CompositeSearchServlet';

//認領人
function showGridListbyClaim() {


    //委託查詢 202403
    let search = checkAppUse();
    if (search !== "") {
        $.alert.open({
            type: 'warning',
            content: search
        });
        return false;
    }
    let appUseList = getAppUse();

    var bool = true;
    var msg = "";
    //認領人姓名　認領人身分證號
    if (($('#OP_SEARCH_PUCP_NAME').val() == "") && ($('#OP_SEARCH_PUCP_IDN').val() == "")) {
        msg += '『認領人姓名』或『認領人身分證號』欄位，至少填寫一個欄位。<br/>';
        bool = false;
    }
    if (msg != "") {
        $.alert.open({
            type: 'warning',
            content: msg
        });
        return;
    }

    if (bool) {
        show_BlockUI_page_noParent('資料準備中…');
        var gridId = "#gridMainList";
        var pagerId = "#pagerMainList";

        $(gridId).jqGrid('GridUnload');
        $(gridId).jqGrid({
            url: ajaxActionUrl,
            mtype: "POST",
            datatype: "json",
            postData: {
                ajaxAction: 'CompositeSearchForClaim',
                OP_STATUS_CD: $('#OP_STATUS_CD').val(),
                OP_STATUS_NM: $('#OP_STATUS_CD').val() == '' ? '' : $('#OP_STATUS_CD option:selected').text(),
                OP_PUCP_NAME: $("#OP_SEARCH_PUCP_NAME").val(),
                OP_PUCP_IDN:  $("#OP_SEARCH_PUCP_IDN").val(),
                //委託查詢 202403
                OPR_KIND: appUseList.OPR_KIND,
                OPR_PURP: appUseList.OPR_PURP
            },
            height: "auto",
            autowidth: true,
            colNames: ["認領人姓名", "認領人身分證號", "聯絡電話", "電子信箱", "收據編號", "填單日期", "承辦單位", "結案人員", "結案日期"],
            colModel: [{
                    name: 'OP_PUCP_NAME',
                    index: 'OP_PUCP_NAME',
                    width: 30
                }, {
                    name: 'OP_PUCP_IDN',
                    index: 'OP_PUCP_IDN',
                    width: 60
                }, {
                    name: 'OP_PUCP_PHONENO',
                    index: 'OP_PUCP_PHONENO',
                    width: 60
                }, {
                    name: 'OP_PUCP_EMAIL',
                    index: 'OP_PUCP_EMAIL',
                    width: 90
                }, {
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 70
                }, {
                    name: 'OP_FM_DATE',
                    index: 'OP_FM_DATE',
                    width: 50
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 110
                }, {
                    name: 'OP_FS_STAFF_NM',
                    index: 'OP_FS_STAFF_NM',
                    width: 50
                }, {
                    name: 'OP_FS_DATE',
                    index: 'OP_FS_DATE',
                    width: 50
                }],
            sortname: 'OP_PUCP_NAME',
            sortorder: "DESC",
            viewrecords: true,
            gridview: true,
            pgbuttons: true,
            pginput: true,
            rowNum: 10,
            rowList: [5, 10, 20, 30],
            pager: pagerId,
            multiselect: false,
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

            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}
// IMEI MAC COLOR
function showGridListbyIMEI(TYPE) {
    
    //委託查詢 202403
    let search = checkAppUse();
    if (search !== "") {
        $.alert.open({
            type: 'warning',
            content: search
        });
        return false;
    }
    let appUseList = getAppUse();
    
    var bool = true;
    var msg = "";
    var Action = "";
    if (TYPE == "COLOR") {
        Action = "getE82ImeiByBrandColor";
    } else {
        Action = "getE82ImeiByCase";
    }
    if (TYPE == "IMEI") {
        if ($('#OP_SEARCH_IMEI_CODE').val() == "") {
            msg += '『IMEI碼』，為必填欄位。<br/>';
            bool = false;
        }
    } else if (TYPE == "MAC") {
        if ($('#OP_SEARCH_MAC_ADDR').val() == "") {
            msg += '『MAC碼』，為必填欄位。<br/>';
            bool = false;
        }
    } else if (TYPE == "COLOR") {
        if ($('#OP_SEARCH_TYPE_CD option:selected').text() == "" || $('#OP_SEARCH_TYPE_CD option:selected').text() == "請選擇...") {
            msg += '『物品種類』，為必填欄位。<br/>';
            bool = false;
        }
        if ($('#OP_SEARCH_BRAND_CD option:selected').text() == "" || $('#OP_SEARCH_BRAND_CD option:selected').text() == "無資料" || $('#OP_SEARCH_BRAND_CD option:selected').text() == "請選擇...") {
            msg += '『廠牌』，為必填欄位。<br/>';
            bool = false;
        }
        if ($('#OP_SEARCH_COLOR_CD').val() == "") {
            msg += '『顏色』，為必填欄位。<br/>';
            bool = false;
        }
        if ($('#OP_SEARCH_PU_DATE_S').val() == "") {
            msg += '『拾得日期』，為必填欄位。<br/>';
            bool = false;
        }
    } else if (TYPE == "PHONE") {
        if ($('#OP_SEARCH_PHONE_NUM').val() == "") {
            msg += '『電信門號』，為必填欄位。<br/>';
            bool = false;
        }
    }
    if (msg != "") {
        $.alert.open({
            type: 'warning',
            content: msg
        });
        return;
    }

    if (bool) {
        show_BlockUI_page_noParent('資料準備中…');
        var gridId = "#gridMainList";
        var pagerId = "#pagerMainList";

        $(gridId).jqGrid('GridUnload');
        $(gridId).jqGrid({
            url: 'WebServiceServlet',
            mtype: "POST",
            datatype: "json",
            postData: {
                ajaxAction: Action,
                QRY_TYPE: TYPE,
                IMEI: $('#OP_SEARCH_IMEI_CODE').val(),
                MAC: $('#OP_SEARCH_MAC_ADDR').val(),
                KIND_CD: $('#OP_SEARCH_TYPE_CD option:selected').val(),
                KIND_NM: $('#OP_SEARCH_TYPE_CD').val() == '' ? '' : $('#OP_SEARCH_TYPE_CD option:selected').text(),
                BRAND_CD: $('#OP_SEARCH_BRAND_CD').val(),
                BRAND_NM: $('#OP_SEARCH_BRAND_CD').val() == '' ? '' : $('#OP_SEARCH_BRAND_CD option:selected').text(),
                COLOR_CD: $("#OP_SEARCH_COLOR_CD").val(),
                COLOR_NM: $('#OP_SEARCH_COLOR_CD').val() == '' ? '' : $('#OP_SEARCH_COLOR_CD option:selected').text(),
                PHONE: $('#OP_SEARCH_PHONE_NUM').val(),
                OC_DT: getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                //委託查詢 202403
                OPR_KIND: appUseList.OPR_KIND,
                OPR_PURP: appUseList.OPR_PURP
            },
            height: "auto",
            autowidth: true,
            colNames: ["IMEI碼1", "IMEI碼2", "MAC碼", "電信門號1", "電信門號2", "e化案號", "受理單位", "報案日期時間", "刑案案類", "發生地點縣市"],
            colModel: [{
                    name: 'IMEI',
                    index: 'IMEI',
                    width: 60
                }, {
                    name: 'IMEI2',
                    index: 'IMEI2',
                    width: 60
                }, {
                    name: 'MAC',
                    index: 'MAC',
                    width: 60
                }, {
                    name: 'PHONE',
                    index: 'PHONE',
                    width: 60
                }, {
                    name: 'PHONE2',
                    index: 'PHONE2',
                    width: 60
                }, {
                    name: 'E8_E_CASE_NO',
                    index: 'E8_E_CASE_NO',
                    width: 60
                }, {
                    name: 'E8_AC_UNIT_NM',
                    index: 'E8_AC_UNIT_NM',
                    width: 100
                }, {
                    name: 'E8_RP_DT',
                    index: 'E8_RP_DT',
                    width: 50
                }, {
                    name: 'E8_CC_TYPE_NM',
                    index: 'E8_CC_TYPE_NM',
                    width: 50
                }, {
                    name: 'E8_OC_CITY_NM',
                    index: 'E8_OC_CITY_NM',
                    width: 50
                }],
            sortname: 'IMEI',
            sortorder: "DESC",
            viewrecords: true,
            gridview: true,
            pgbuttons: true,
            pginput: true,
            rowNum: 10,
            rowList: [5, 10, 20, 30],
            pager: pagerId,
            multiselect: false,
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

            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}

// 查詢條件 清除條件
function cleanEditor() {
    $('#OP_SEARCH_PUCP_NAME').val('');
    $('#OP_SEARCH_PUCP_IDN').val('');
    $('#OP_SEARCH_IMEI_CODE').val('');
    $('#OP_SEARCH_PHONE_NUM').val('')
    $('#OP_SEARCH_MAC_ADDR').val('');
    $('#OP_SEARCH_TYPE_CD').val('');
    $('#OP_SEARCH_BRAND_CD').val('');
    $('#OP_SEARCH_COLOR_CD').val('');
    $('#OP_SEARCH_PU_DATE_S').val('');
    $('#OP_SEARCH_TYPE_CD').multipleSelect("uncheckAll");
    $('#OP_SEARCH_TYPE_CD').multipleSelect("refresh");
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