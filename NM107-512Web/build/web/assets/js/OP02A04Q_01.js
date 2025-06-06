var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu02').addClass("active");
    $('#menu02span').addClass("selected");
    $('#menu0204').addClass("active");
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
    bindUNITDDL('2', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', 'ALL');
    bindDATATYPE('B01', 'OP_SEARCH_PUPO_TP_CD', '請選擇...');

    //受理基本資料
    bindDATATYPE('B01', 'IPUBASIC_OP_PUPO_TP_CD', '');
    bindDATATYPE('B02', 'IPUBASIC_OP_PUOJ_ATTR_CD', '');
    bindDATATYPE('B04', 'IPUBASIC_OP_CURSTAT_CD', '');
    bindDATATYPE('B07', 'IPUBASIC_OP_NTC_PSN_TYPE', '請選擇...');
    bindCOUNTRYTYPE('', 'IPUBASIC_OP_PUPO_NAT_CD', ''); //國家
    bindCOUNTRYDDL('2', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', 'ALL'); //鄉鎮
    bindCOUNTRYDDL('2', 'IPUBASIC_OP_PU_CITY_CD', 'IPUBASIC_OP_PU_TOWN_CD', '', '', 'ALL'); //鄉鎮

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
    $('#queryBtn').click(function () {
        if (checkList(document.getElementById('form1')))
            showGridListbyQSC();
    });
    //清除
    $('#QS_CLEAR').click(function () {
        cleanEditor();
    });
    //分頁1 拾得物受理基本資料
    $('#tabInfomation1').click(function () {
        showBasicValue($('#IPUBASIC_OP_SEQ_NO').val());
    });
    //分頁2 拾得物明細
    $('#tabInfomation2').click(function () {
        var checkStatusForBasic = $('#checkStatusForBasic').val();
        if (checkStatusForBasic != 'N') {
            $('#tab_4_2_alert').hide();
            $('#tab_4_2_main').show();
            //新的物品種類DDL
            if (checkDate($('#IPUBASIC_OP_SEQ_NO').val()) == true) {
                bindE8DATATYPE('C12', 'IPUDETAIL_OP_TYPE_CD', '請選擇...');
            } else {
                bindOPDATATYPE('C12', 'IPUDETAIL_OP_TYPE_CD', '請選擇...');//物品類別
            }
            bindCOLORTYPE('', 'IPUDETAIL_OP_COLOR_CD', '請選擇...'); //顏色
            bindDATATYPE('C39', 'IPUDETAIL_OP_QTY_UNIT_CHOOSE', '請選擇...'); //單位
            clearDetailData();
            showDetailGridList($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUDETAIL_OP_AC_RCNO').val());
        } else {
            $('#tab_4_2_alert').show();
            $('#tab_4_2_main').hide();
        }
    });
    //分頁3 報表列印
    $('#tabInfomation3').click(function () {
        //只是判斷要不要顯示
        var checkYNDetail = $('#checkYNDetail').val();
        var checkStatusForBasic = $('#checkStatusForBasic').val();
        if (checkStatusForBasic != 'N') {
            $("#tab_4_3_1_alert").hide();
            if (checkYNDetail == 'Y') {
                $("#tab_4_3_2_alert").hide();
                $("#tab_4_3_main").show();
                $("#printBtn").show();
                showGridList3();
            } else {
                $("#tab_4_3_2_alert").show();
                $("#tab_4_3_main").hide();
                $("#printBtn").hide();
            }
        } else {
            $("#tab_4_3_1_alert").show();
            $("#tab_4_3_2_alert").hide();
            $("#tab_4_3_main").hide();
            $("#printBtn").hide();
        }
    });
    $('#printBtn').click(function () {
        if ($('#jqg_gridList_3_1').prop('checked')) {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP02A01Q.doc',
                reportName: 'oP02A01Q.doc,oP02A02Q.doc',
                newReportName: '拾得物收據.doc,拾得物收據.doc',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
        }
        if ($('#jqg_gridList_3_2').prop('checked')) {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP02A03Q.doc',
                reportName: 'oP02A03Q.doc',
                newReportName: '受理民眾交存拾得遺失物作業程序檢核表.doc',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
        }
        if ($('#jqg_gridList_3_3').prop('checked'))
        {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP02A09Q.doc',
                reportName: 'oP02A09Q.doc',
                newReportName: '受理拾得物案陳報單.doc',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
        }
    });

}
function registButtonGr() {

}

// 註冊事件區---End

function initDefaultQS() {
    dateRangeForOneYear('OP_SEARCH_AC_DATE_S', 'OP_SEARCH_AC_DATE_E');
    // 帶入登入者單位
    if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1) {

    } else if (strPermissionData["RolePermission"] == '1') {
        $("#OP_Search_unitLevel2").prop('disabled', true);
        $("#OP_Search_unitLevel3").prop('disabled', true);
        $("#OP_Search_unitLevel4").prop('disabled', true);
    } else if (strPermissionData["RolePermission"] == '2') {
        $("#OP_Search_unitLevel2").prop('disabled', true);
        $("#OP_Search_unitLevel3").prop('disabled', true);

    } else if (strPermissionData["RolePermission"] == '3') {
        $("#OP_Search_unitLevel2").prop('disabled', true);
    }
    // 帶入登入者單位
    $('#OP_Search_unitLevel2').val(strPermissionData['UNITLEVEL1']);
    bindUNITDDL('3', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', '');
    if (strPermissionData["RolePermission"] == '3') {
        $('#OP_Search_unitLevel3').val('');
    } else {
        $('#OP_Search_unitLevel3').val(strPermissionData['UNITLEVEL2']);
    }
    bindUNITDDL('4', 'OP_Search_unitLevel2', 'OP_Search_unitLevel3', 'OP_Search_unitLevel4', '');
    if (strPermissionData["RolePermission"] == '2' || strPermissionData["RolePermission"] == '3') {
        $('#OP_Search_unitLevel4').val('');
    } else {
        $('#OP_Search_unitLevel4').val(strPermissionData['UNITLEVEL3']);
    }

    // 先帶出查詢結果
    if (checkList(document.getElementById('form1')))
        showGridListbyQS();

    disableAll('IPUBASIC');
    disableAll('IPUDETAIL');
    $('#deleteData').show();

}
  
var ajaxActionUrl = 'PuMaintainServlet';

function showGridListbyQS() {
    
    let appUseList = getAppUse();
    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    var bool = true;
    var msg = "";
    //拾得日期
    if (($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() != "")) {
        if (($('#OP_SEARCH_PU_DATE_S').val() > $('#OP_SEARCH_PU_DATE_E').val())) {
            msg += '拾得日期迄日不可小於拾得日期起日<br/>';
            bool = false;
        }
    }
    if (getADDate($('#OP_SEARCH_PU_DATE_S').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '拾得日期起日不可大於系統日期時間<br/>';
        bool = false;
    }
    if (getADDate($('#OP_SEARCH_PU_DATE_E').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '拾得日期迄日不可大於系統日期時間<br/>';
        bool = false;
    }
    //受理日期
    if (($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() != "")) {
        if (($('#OP_SEARCH_AC_DATE_S').val() > $('#OP_SEARCH_AC_DATE_E').val())) {
            msg += '受理日期迄日不可小於受理日期起日<br/>';
            bool = false;
        }
    }
    if (getADDate($('#OP_SEARCH_AC_DATE_S').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '受理日期起日不可大於系統日期時間<br/>';
        bool = false;
    }
    if (getADDate($('#OP_SEARCH_AC_DATE_E').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '受理日期迄日不可大於系統日期時間<br/>';
        bool = false;
    }
    //收據編號
    if ($('#OP_SEARCH_AC_RCNO').val() != "") {
        if ($('#OP_SEARCH_AC_RCNO').val().length > 17) {
            msg += '收據編號欄位長度超過17碼<br/>';
            bool = false;
        }
    }
    //有收據編號不用限制受理拾得日期，沒有的話須則一日期區間為一年
    if ($('#OP_SEARCH_AC_RCNO').val() == "" && $('#OP_SEARCH_PU_DATE_S').val() == "" && $('#OP_SEARCH_PU_DATE_E').val() == "" && $('#OP_SEARCH_AC_DATE_S').val() == "" && $('#OP_SEARCH_AC_DATE_E').val() == "") {
        msg += '收據編號、拾得日期或受理日期至少輸入一個條件<br/>';
        bool = false;
    } else {
        if ($('#OP_SEARCH_AC_RCNO').val() == "") {
            if (($('#OP_SEARCH_PU_DATE_S').val() == "") && ($('#OP_SEARCH_PU_DATE_E').val() != "")) {
                msg += '拾得日期起日不可為空<br/>';
                bool = false;
            } else if (($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() == "")) {
                msg += '拾得日期迄日不可為空<br/>';
                bool = false;
            } else {
                if (($('#OP_SEARCH_PU_DATE_S').val() < $('#OP_SEARCH_PU_DATE_E').val()) && compareOneYear('OP_SEARCH_PU_DATE_S', 'OP_SEARCH_PU_DATE_E') == false) {
                    msg += '拾得日期起日與拾得日期迄日不能超過一年<br/>';
                    bool = false;
                }
            }
            if (($('#OP_SEARCH_AC_DATE_S').val() == "") && ($('#OP_SEARCH_AC_DATE_E').val() != "")) {
                msg += '受理日期起日不可為空<br/>';
                bool = false;
            } else if (($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() == "")) {
                msg += '受理日期迄日不可為空<br/>';
                bool = false;
            } else {
                if (($('#OP_SEARCH_AC_DATE_S').val() < $('#OP_SEARCH_AC_DATE_E').val()) && compareOneYear('OP_SEARCH_AC_DATE_S', 'OP_SEARCH_AC_DATE_E') == false) {
                    msg += '受理日期起日與受理日期迄日不能超過一年<br/>';
                    bool = false;
                }
            }
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
        var OPENLLevel_NM;

        if ($('#OP_Search_unitLevel4').val() != '') {
            OPENLLevel_NM = $('#OP_Search_unitLevel2 option:selected').text();
            OPENLLevel_NM += $('#OP_Search_unitLevel3 option:selected').text();
            OPENLLevel_NM += $('#OP_Search_unitLevel4 option:selected').text();
        } else if ($('#OP_Search_unitLevel3').val() != '') {
            OPENLLevel_NM = $('#OP_Search_unitLevel2 option:selected').text();
            OPENLLevel_NM += $('#OP_Search_unitLevel3 option:selected').text();
        } else {
            OPENLLevel_NM = $('#OP_Search_unitLevel2 option:selected').text();
        }
        $(gridId).jqGrid('GridUnload');
        $(gridId).jqGrid({
            url: ajaxActionUrl,
            mtype: "POST",
            datatype: "json",
            postData: {
                ajaxAction: 'IPuBasicQueryDeleteForDown',
                OP_UNITLEVEL2: $('#OP_Search_unitLevel2').val(),
                OP_UNITLEVEL2_NM: $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
                OP_UNITLEVEL3: $('#OP_Search_unitLevel3').val(),
                OP_UNITLEVEL3_NM: $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text(),
                OP_UNITLEVEL4: $('#OP_Search_unitLevel4').val(),
                OP_UNITLEVEL4_NM: $('#OP_Search_unitLevel4').val() == '' ? '' : $('#OP_Search_unitLevel4 option:selected').text(),
                UNITLEVEL_NM: OPENLLevel_NM,
                OP_AC_RCNO: $("#OP_SEARCH_AC_RCNO").val(),
                OP_PUPO_TP_CD: $('#OP_SEARCH_PUPO_TP_CD').val(),
                OP_PUPO_TP_NM: $('#OP_SEARCH_PUPO_TP_CD').val() == '' ? '' : $('#OP_SEARCH_PUPO_TP_CD option:selected').text(),
                OP_PU_DATE_S: getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                OP_PU_DATE_E: getADDate($("#OP_SEARCH_PU_DATE_E").val()),
                OP_AC_DATE_S: getADDate($("#OP_SEARCH_AC_DATE_S").val()),
                OP_AC_DATE_E: getADDate($("#OP_SEARCH_AC_DATE_E").val()),
                OP_PU_YN_OV500: '1',
                OP_DEL_FLAG: '1',
                ACTION_TYPE: 'OP02A04Q_01',
              //委託查詢 202403 202403
                OPR_KIND: appUseList.OPR_KIND,
                OPR_PURP: appUseList.OPR_PURP
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得人類別", "物品屬性", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "刪除日期時間", "刪除單位", "刪除人員", "BASIC_SEQ"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 85
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 80
                }, {
                    name: 'OP_PUPO_TP_NM',
                    index: 'OP_PUPO_TP_NM',
                    width: 40
                }, {
                    name: 'OP_PUOJ_ATTR_NM',
                    index: 'OP_PUOJ_ATTR_NM',
                    width: 50
                }, {
                    name: 'OP_PUPO_NAME',
                    index: 'OP_PUPO_NAME',
                    width: 50
                }, {
                    name: 'OP_PUPO_IDN',
                    index: 'OP_PUPO_IDN',
                    width: 60
                }, {
                    name: 'OP_PU_DTTM',
                    index: 'OP_PU_DTTM',
                    width: 50
                }, {
                    name: 'OP_PU_PLACE',
                    index: 'OP_PU_PLACE',
                    width: 100
                }, {
                    name: 'OP_DEL_DTTM',
                    index: 'OP_DEL_DTTM',
                    width: 50
                }, {
                    name: 'OP_DEL_UNIT_NM',
                    index: 'OP_DEL_UNIT_NM',
                    width: 80
                }, {
                    name: 'OP_DEL_STAFF_NM',
                    index: 'OP_DEL_STAFF_NM',
                    width: 50
                }, {
                    name: 'BASIC_SEQ',
                    index: 'BASIC_SEQ',
                    hidden: true
                            // 欄位隱藏
                }],
            sortname: 'OP_PU_DTTM',
            sortorder: "DESC",
            viewrecords: true,
            gridview: true,
            pgbuttons: true,
            pginput: true,
            rowNum: 10,
            rowList: [5, 10, 20, 30],
            pager: pagerId,
            multiselect: false,
            multiboxonly: true,
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
                var row = $(gridId).jqGrid('getRowData', id);
                var BasicNo = row.BASIC_SEQ;
                var AcRcno = row.OP_AC_RCNO;

                if (selectedColumnName != 'cb') {
                    $(gridId).setSelection(id, false);
                }
                if (selectedColumnName != 'cb') {
                    $('#IPUBASIC_OP_SEQ_NO').val(BasicNo);
                    $('#IPUBASIC_OP_AC_RCNO').val(AcRcno);
                    $('#IPUDETAIL_OP_AC_RCNO').val(AcRcno);
                    $('[id*="tabInfomation"]').removeClass('active');
                    $('[id*="tab_4_"]').removeClass('active');
                    $('#tabInfomation1').addClass('active');
                    $('#tab_4_1').addClass('active');

                    $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                    $('#checkYNDetail').val(checkYNDetail(BasicNo));
                    BasicValueForLog(BasicNo);
                    $('#tabInfomation1').click();
                    goTab('modal-editor-add', 'tabMain');
                }
            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}

function showGridListbyQSC() {

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

    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    var bool = true;
    var msg = "";
    //拾得日期
    if (($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() != "")) {
        if (($('#OP_SEARCH_PU_DATE_S').val() > $('#OP_SEARCH_PU_DATE_E').val())) {
            msg += '拾得日期迄日不可小於拾得日期起日<br/>';
            bool = false;
        }
    }
    if (getADDate($('#OP_SEARCH_PU_DATE_S').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '拾得日期起日不可大於系統日期時間<br/>';
        bool = false;
    }
    if (getADDate($('#OP_SEARCH_PU_DATE_E').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '拾得日期迄日不可大於系統日期時間<br/>';
        bool = false;
    }
    //受理日期
    if (($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() != "")) {
        if (($('#OP_SEARCH_AC_DATE_S').val() > $('#OP_SEARCH_AC_DATE_E').val())) {
            msg += '受理日期迄日不可小於受理日期起日<br/>';
            bool = false;
        }
    }
    if (getADDate($('#OP_SEARCH_AC_DATE_S').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '受理日期起日不可大於系統日期時間<br/>';
        bool = false;
    }
    if (getADDate($('#OP_SEARCH_AC_DATE_E').val().substring(0, 9)) > getADDate(nowDate)) {
        msg += '受理日期迄日不可大於系統日期時間<br/>';
        bool = false;
    }
    //收據編號
    if ($('#OP_SEARCH_AC_RCNO').val() != "") {
        if ($('#OP_SEARCH_AC_RCNO').val().length > 17) {
            msg += '收據編號欄位長度超過17碼<br/>';
            bool = false;
        }
    }
    //有收據編號不用限制受理拾得日期，沒有的話須則一日期區間為一年
    if ($('#OP_SEARCH_AC_RCNO').val() == "" && $('#OP_SEARCH_PU_DATE_S').val() == "" && $('#OP_SEARCH_PU_DATE_E').val() == "" && $('#OP_SEARCH_AC_DATE_S').val() == "" && $('#OP_SEARCH_AC_DATE_E').val() == "") {
        msg += '收據編號、拾得日期或受理日期至少輸入一個條件<br/>';
        bool = false;
    } else {
        if ($('#OP_SEARCH_AC_RCNO').val() == "") {
            if (($('#OP_SEARCH_PU_DATE_S').val() == "") && ($('#OP_SEARCH_PU_DATE_E').val() != "")) {
                msg += '拾得日期起日不可為空<br/>';
                bool = false;
            } else if (($('#OP_SEARCH_PU_DATE_S').val() != "") && ($('#OP_SEARCH_PU_DATE_E').val() == "")) {
                msg += '拾得日期迄日不可為空<br/>';
                bool = false;
            } else {
                if (($('#OP_SEARCH_PU_DATE_S').val() < $('#OP_SEARCH_PU_DATE_E').val()) && compareOneYear('OP_SEARCH_PU_DATE_S', 'OP_SEARCH_PU_DATE_E') == false) {
                    msg += '拾得日期起日與拾得日期迄日不能超過一年<br/>';
                    bool = false;
                }
            }
            if (($('#OP_SEARCH_AC_DATE_S').val() == "") && ($('#OP_SEARCH_AC_DATE_E').val() != "")) {
                msg += '受理日期起日不可為空<br/>';
                bool = false;
            } else if (($('#OP_SEARCH_AC_DATE_S').val() != "") && ($('#OP_SEARCH_AC_DATE_E').val() == "")) {
                msg += '受理日期迄日不可為空<br/>';
                bool = false;
            } else {
                if (($('#OP_SEARCH_AC_DATE_S').val() < $('#OP_SEARCH_AC_DATE_E').val()) && compareOneYear('OP_SEARCH_AC_DATE_S', 'OP_SEARCH_AC_DATE_E') == false) {
                    msg += '受理日期起日與受理日期迄日不能超過一年<br/>';
                    bool = false;
                }
            }
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
        var OPENLLevel_NM;

        if ($('#OP_Search_unitLevel4').val() != '') {
            OPENLLevel_NM = $('#OP_Search_unitLevel2 option:selected').text();
            OPENLLevel_NM += $('#OP_Search_unitLevel3 option:selected').text();
            OPENLLevel_NM += $('#OP_Search_unitLevel4 option:selected').text();
        } else if ($('#OP_Search_unitLevel3').val() != '') {
            OPENLLevel_NM = $('#OP_Search_unitLevel2 option:selected').text();
            OPENLLevel_NM += $('#OP_Search_unitLevel3 option:selected').text();
        } else {
            OPENLLevel_NM = $('#OP_Search_unitLevel2 option:selected').text();
        }
        $(gridId).jqGrid('GridUnload');
        $(gridId).jqGrid({
            url: ajaxActionUrl,
            mtype: "POST",
            datatype: "json",
            postData: {
                ajaxAction: 'IPuBasicQueryDeleteForDownSearch',
                OP_UNITLEVEL2: $('#OP_Search_unitLevel2').val(),
                OP_UNITLEVEL2_NM: $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
                OP_UNITLEVEL3: $('#OP_Search_unitLevel3').val(),
                OP_UNITLEVEL3_NM: $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text(),
                OP_UNITLEVEL4: $('#OP_Search_unitLevel4').val(),
                OP_UNITLEVEL4_NM: $('#OP_Search_unitLevel4').val() == '' ? '' : $('#OP_Search_unitLevel4 option:selected').text(),
                UNITLEVEL_NM: OPENLLevel_NM,
                OP_AC_RCNO: $("#OP_SEARCH_AC_RCNO").val(),
                OP_PUPO_TP_CD: $('#OP_SEARCH_PUPO_TP_CD').val(),
                OP_PUPO_TP_NM: $('#OP_SEARCH_PUPO_TP_CD').val() == '' ? '' : $('#OP_SEARCH_PUPO_TP_CD option:selected').text(),
                OP_PU_DATE_S: getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                OP_PU_DATE_E: getADDate($("#OP_SEARCH_PU_DATE_E").val()),
                OP_AC_DATE_S: getADDate($("#OP_SEARCH_AC_DATE_S").val()),
                OP_AC_DATE_E: getADDate($("#OP_SEARCH_AC_DATE_E").val()),
                OP_PU_YN_OV500: '1',
                OP_DEL_FLAG: '1',
                ACTION_TYPE: 'OP02A04Q_01',
                //委託查詢 202403
                OPR_KIND: appUseList.OPR_KIND,
                OPR_PURP: appUseList.OPR_PURP
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得人類別", "物品屬性", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "刪除日期時間", "刪除單位", "刪除人員", "BASIC_SEQ"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 85
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 80
                }, {
                    name: 'OP_PUPO_TP_NM',
                    index: 'OP_PUPO_TP_NM',
                    width: 40
                }, {
                    name: 'OP_PUOJ_ATTR_NM',
                    index: 'OP_PUOJ_ATTR_NM',
                    width: 50
                }, {
                    name: 'OP_PUPO_NAME',
                    index: 'OP_PUPO_NAME',
                    width: 50
                }, {
                    name: 'OP_PUPO_IDN',
                    index: 'OP_PUPO_IDN',
                    width: 60
                }, {
                    name: 'OP_PU_DTTM',
                    index: 'OP_PU_DTTM',
                    width: 50
                }, {
                    name: 'OP_PU_PLACE',
                    index: 'OP_PU_PLACE',
                    width: 100
                }, {
                    name: 'OP_DEL_DTTM',
                    index: 'OP_DEL_DTTM',
                    width: 50
                }, {
                    name: 'OP_DEL_UNIT_NM',
                    index: 'OP_DEL_UNIT_NM',
                    width: 80
                }, {
                    name: 'OP_DEL_STAFF_NM',
                    index: 'OP_DEL_STAFF_NM',
                    width: 50
                }, {
                    name: 'BASIC_SEQ',
                    index: 'BASIC_SEQ',
                    hidden: true
                            // 欄位隱藏
                }],
            sortname: 'OP_PU_DTTM',
            sortorder: "DESC",
            viewrecords: true,
            gridview: true,
            pgbuttons: true,
            pginput: true,
            rowNum: 10,
            rowList: [5, 10, 20, 30],
            pager: pagerId,
            multiselect: false,
            multiboxonly: true,
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
                var row = $(gridId).jqGrid('getRowData', id);
                var BasicNo = row.BASIC_SEQ;
                var AcRcno = row.OP_AC_RCNO;

                if (selectedColumnName != 'cb') {
                    $(gridId).setSelection(id, false);
                }
                if (selectedColumnName != 'cb') {
                    $('#IPUBASIC_OP_SEQ_NO').val(BasicNo);
                    $('#IPUBASIC_OP_AC_RCNO').val(AcRcno);
                    $('#IPUDETAIL_OP_AC_RCNO').val(AcRcno);
                    $('[id*="tabInfomation"]').removeClass('active');
                    $('[id*="tab_4_"]').removeClass('active');
                    $('#tabInfomation1').addClass('active');
                    $('#tab_4_1').addClass('active');

                    $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                    $('#checkYNDetail').val(checkYNDetail(BasicNo));
                    BasicValueForLog(BasicNo);
                    $('#tabInfomation1').click();
                    goTab('modal-editor-add', 'tabMain');
                }
            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}

function showBasicValue(OP_SEQ_NO) {
    show_BlockUI_page_noParent('資料準備中…');
    var ajData = {
        'ajaxAction': 'showBasicValue',
        'OP_SEQ_NO': OP_SEQ_NO
    };

    $.ajax({
        url: ajaxActionUrl,
        type: "POST",
        dataType: "json",
        data: ajData,
        success: function (data, textStatus, xhr) {
            var formData = data.formData[0];
            //受理單位資料
            var data1 = getunit_code(formData['OP_AC_UNIT_CD']);
            if (data1.result[0]["OP_DEPT_CD"] == data1.result[0]["OP_UNIT_CD"]) {//只有警局
                $('#OP_unitLevel2').val(data1.result[0]["OP_UNIT_CD"]);
                bindUNITDDL('3', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
            } else if (data1.result[0]["OP_UNIT_CD"] == data1.result[0]["OP_BRANCH_CD"] || data1.result[0]["OP_BRANCH_CD"] == '') {//只有警分局
                $('#OP_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                bindUNITDDL('3', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
                $('#OP_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
                bindUNITDDL('4', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
            } else {
                $('#OP_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                bindUNITDDL('3', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
                $('#OP_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
                bindUNITDDL('4', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
                $('#OP_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
            }

            $('#IPUBASIC_OP_AC_STAFF_NM').val(formData['OP_AC_STAFF_NM']);
            $('#IPUBASIC_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
            $('#IPUBASIC_OP_AC_DATE').val(formData['OP_AC_DATE']);
            $('#IPUBASIC_OP_FM_DATE').val(formData['OP_FM_DATE']);
            $('#IPUBASIC_OP_AC_UNIT_TEL').val(formData['OP_AC_UNIT_TEL']);
            $('#IPUBASIC_OP_MANUAL_RCNO').val(formData['OP_MANUAL_RCNO']);

            //拾得人基本資料
            if (formData['OP_IS_CUST'] == "1") {
                $("input[name='IPUBASIC.OP_IS_CUST'][value='1']").prop("checked", true);
            } else {
                $("input[name='IPUBASIC.OP_IS_CUST'][value='0']").prop("checked", true);
            }
            if (formData['OP_IS_PUT_NM'] == "1") {
                $("input[name='IPUBASIC.OP_IS_PUT_NM'][value='1']").prop("checked", true);
                $("img[name=NAME_CHECK]").show();
            } else {
                $("input[name='IPUBASIC.OP_IS_PUT_NM'][value='0']").prop("checked", true);
                $("img[name=NAME_CHECK]").hide();
            }
            if (formData['OP_INCLUDE_CERT'] == "1") {
                $("input[name='IPUBASIC.OP_INCLUDE_CERT'][value='1']").prop("checked", true);
            } else {
                $("input[name='IPUBASIC.OP_INCLUDE_CERT'][value='0']").prop("checked", true);
            }
            $('#IPUBASIC_OP_PUPO_TP_CD').val(formData['OP_PUPO_TP_CD']);
            $('#IPUBASIC_OP_PUOJ_ATTR_CD').val(formData['OP_PUOJ_ATTR_CD']);
            $('#IPUBASIC_OP_PUPO_NAME').val(formData['OP_PUPO_NAME']);
            $('#IPUBASIC_OP_PUPO_RNAME').val(formData['OP_PUPO_RNAME']);
            $('#IPUBASIC_OP_PUPO_IDN_TP').val(formData['OP_PUPO_IDN_TP']);
            $('#IPUBASIC_OP_PUPO_IDN').val(formData['OP_PUPO_IDN']);
            $('#IPUBASIC_OP_PUPO_BEFROC').val(formData['OP_PUPO_BEFROC']);
            $('#IPUBASIC_OP_PUPO_BIRTHDT').val(formData['OP_PUPO_BIRTHDT']);

            $('#IPUBASIC_OP_PUPO_GENDER').val(formData['OP_PUPO_GENDER']);
            $('#IPUBASIC_OP_PUPO_NAT_CD').val(formData['OP_PUPO_NAT_CD']);
            $('#IPUBASIC_OP_PUPO_PHONENO').val(formData['OP_PUPO_PHONENO']);
            $('#IPUBASIC_OP_PUPO_ZIPCODE').val(formData['OP_PUPO_ZIPCODE']);
            if (formData['OP_OC_ADDR_TYPE_CD'] == "1") {
                $("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD'][value='1']").prop("checked", true);
                $('#normalPattern').show();
                $('#freePattern').hide();
                $('#IPUBASIC_GET_HH_DATA').show();
                $('#IPUBASIC_OP_PUPO_CITY_CD').val(formData['OP_PUPO_CITY_CD']);
                bindCOUNTRYDDL('3', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', '');
                $('#IPUBASIC_OP_PUPO_TOWN_CD').val(formData['OP_PUPO_TOWN_CD']);
                bindCOUNTRYDDL('4', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', '');
                $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val(formData['OP_PUPO_VILLAGE_CD']);
                $('#IPUBASIC_OP_PUPO_LIN').val(formData['OP_PUPO_LIN']);
                $('#IPUBASIC_OP_PUPO_ROAD').val(formData['OP_PUPO_ROAD']);
                $('#IPUBASIC_OP_PUPO_ADDR_OTH').val('');
            } else {
                $("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD'][value='9']").prop("checked", true);
                $('#normalPattern').hide();
                $('#freePattern').show();
                $('#IPUBASIC_GET_HH_DATA').hide();
                $('#IPUBASIC_OP_PUPO_CITY_CD').val('');
                $('#IPUBASIC_OP_PUPO_TOWN_CD').val('');
                $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val('');
                $('#IPUBASIC_OP_PUPO_LIN').val('');
                $('#IPUBASIC_OP_PUPO_ROAD').val('');
                $('#IPUBASIC_OP_PUPO_ADDR_OTH').val(formData['OP_PUPO_ADDR_OTH']);
            }

            $('#IPUBASIC_OP_PUPO_EMAIL').val(formData['OP_PUPO_EMAIL']);

            //拾得地點時間資料
            $('#IPUBASIC_OP_PU_DATE').val(formData['OP_PU_DATE']);
            $('#IPUBASIC_OP_PU_TIME').val(formData['OP_PU_TIME']);

            $('#IPUBASIC_OP_PU_CITY_CD').val(formData['OP_PU_CITY_CD']);
            bindCOUNTRYDDL('3', 'IPUBASIC_OP_PU_CITY_CD', 'IPUBASIC_OP_PU_TOWN_CD', '', '', '');
            $('#IPUBASIC_OP_PU_TOWN_CD').val(formData['OP_PU_TOWN_CD']);
            $('#IPUBASIC_OP_PU_PLACE').val(formData['OP_PU_PLACE']);

            //其他資料
            $('#IPUBASIC_OP_CURSTAT_CD').val(formData['OP_CURSTAT_CD']);
            $('#IPUBASIC_OP_NTC_FIND_PO').val(formData['OP_NTC_FIND_PO']);
            $('#IPUBASIC_OP_YN_NTC').val(formData['OP_YN_NTC']);
            $('#IPUBASIC_OP_NTC_DATE').val(formData['OP_NTC_DATE']);
            $('#IPUBASIC_OP_NTC_PSN_TYPE').val(formData['OP_NTC_PSN_TYPE']);
            $('#IPUBASIC_OP_NTC_MODE').val(formData['OP_NTC_MODE']);
            $('#IPUBASIC_OP_PU_REMARK').val(formData['OP_PU_REMARK']);
            $('#IPUBASIC_OP_PU_YN_OV500').val(formData['OP_PU_YN_OV500']);

            //刪除資料
            var data2 = getunit_code(formData['OP_DEL_UNIT_CD']);
            if (data2.result[0]["OP_DEPT_CD"] == data2.result[0]["OP_UNIT_CD"]) {//只有警局
                $('#OP_DEL_unitLevel2').val(data2.result[0]["OP_UNIT_CD"]);
                bindUNITDDL('3', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');
            } else if (data2.result[0]["OP_UNIT_CD"] == data2.result[0]["OP_BRANCH_CD"] || data2.result[0]["OP_BRANCH_CD"] == '') {//只有警分局
                $('#OP_DEL_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                bindUNITDDL('3', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');
                $('#OP_DEL_unitLevel3').val(data2.result[0]["OP_UNIT_CD"]);
                bindUNITDDL('4', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');
            } else {
                $('#OP_DEL_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                bindUNITDDL('3', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');
                $('#OP_DEL_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
                bindUNITDDL('4', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');
                $('#OP_DEL_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
            }
            $('#IPUBASIC_OP_DEL_STAFF_NM').val(formData['OP_DEL_STAFF_NM']);
            $('#IPUBASIC_OP_DEL_DATE').val(formData['OP_DEL_DATE']);
            $('#IPUBASIC_OP_DEL_TIME').val(formData['OP_DEL_TIME']);
            $('#IPUBASIC_OP_DEL_RSN').val(formData['OP_DEL_RSN']);

            $('#IPUDETAIL_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
            $.unblockUI();
        }
    });
}
//報表列印---START
function showGridList3() {
    var gridId = "#gridList_3";
    var pagerId = "#pageList_3";

    $(gridId).jqGrid('GridUnload');
    $(gridId).jqGrid({
        height: "auto",
        autowidth: true,
        colNames: ["檔案名稱", "說明"],
        colModel: [{
                name: 'FILE_NAME',
                index: 'FILE_NAME',
                width: 300,
                align: 'left'
            }, {
                name: 'FILE_EXPL',
                index: 'FILE_EXPL',
                width: 300,
                align: 'left'
            }],
        sortname: 'FILE_NAME',
        sortorder: "DESC",
        viewrecords: true,
        gridview: true,
        pgbuttons: true,
        pginput: true,
        rowNum: 10,
        rowList: [5, 10, 20, 30],
        pager: pagerId,
        multiselect: true,
        loadonce: true,
        altRows: true,
        onCellSelect: function (row, col, content, event) {
            var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
            selectedColumnName = cm[col].name;
        },
        onSelectRow: function (id, status) {

        }
    });
    $(gridId).jqGrid("clearGridData", true);
    $("#gridList_3").setGridWidth($(window).width() - 92);
    $(gridId).trigger("reloadGrid");
    $(gridId).jqGrid('addRowData', 1, {
        FILE_NAME: '拾得物收據'
    });
    $(gridId).jqGrid('addRowData', 2, {
        FILE_NAME: '受理民眾交存拾得遺失物作業程序檢核表'
    });
    $(gridId).jqGrid('addRowData', 3, {
        FILE_NAME: '受理拾得物案陳報單'
    });

}
//報表列印---END

//邏輯判斷---START
// 查詢條件 清除條件
function cleanEditor() {
    $('#queryporlet input[type="text"]').val('');
    $('#OP_SEARCH_PUPO_TP_CD').val('');
    $('#gridMainList').jqGrid('clearGridData');
}
function clearDetailData() {
    $('#IPUDETAIL_OP_PUOJ_NM').val('');
    $('#IPUDETAIL_OP_TYPE_CD').val('');
    $('#IPUDETAIL_OP_QTY').val('');
    $('#IPUDETAIL_OP_QTY_UNIT').val('');
    $('#IPUDETAIL_OP_QTY_UNIT_CHOOSE').val('');
    $('#IPUDETAIL_OP_COLOR_CD').val('');
    $('#IPUDETAIL_OP_FEATURE').val('');
    $('#IPUDETAIL_OP_REMARK').val('');
    $('#IPUDETAIL_OP_BRAND_CD').empty();
    $('#IPUDETAIL_OP_BRAND_CD').append("<option value=''>無資料</option>");
    $('#IPUDETAIL_OP_IMEI_CODE').val('');
    $("#IPUDETAIL_OP_IMEI_CODE").hide();
    $('#IPUDETAIL_OP_IMEI_CODE_2').val('');
    $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
    $("#IMEILabel").hide();
    $("#IMEILabel2").hide();
    $("#GET_E_IMEI_DATA").hide();
    $('#IPUDETAIL_OP_MAC_ADDR').val('');
    $("#IPUDETAIL_OP_MAC_ADDR").hide();
    $("#MACLabel").hide();
    $("#GET_E_MAC_DATA").hide();
    $("#PhoneLabel").hide();
    $("#PhoneLabel2").hide();
    $("#IPUDETAIL_OP_PHONE_NUMBER").val('');
    $("#IPUDETAIL_OP_PHONE_NUMBER").hide();
    $("#IPUDETAIL_OP_PHONE_NUMBER_2").val('');
    $("#IPUDETAIL_OP_PHONE_NUMBER_2").hide();
    $("#GET_E_PHONE_DATA").hide();
    $('#file1').val('');
    $('#file2').val('');
    $('#previewPhoto1').attr('src', 'assets/img/file-upload-with-preview.png');
    $('#previewPhoto2').attr('src', 'assets/img/file-upload-with-preview.png');
    $('#OP_UPL_FILE_NAME1').val('');
    $('#IPHOTO_OP_SEQ_NO1').val('');
    $('#IPHOTO_OP_FILE_PATH1').val('');
    $('#IPHOTO_DETELE1').val('');
    $('#OP_UPL_FILE_NAME2').val('');
    $('#IPHOTO_OP_SEQ_NO2').val('');
    $('#IPHOTO_OP_FILE_PATH2').val('');
    $('#IPHOTO_DETELE2').val('');
    $('#IPUDETAIL_OP_TYPE_CD').multipleSelect("uncheckAll");
    $('#IPUDETAIL_OP_TYPE_CD').multipleSelect("refresh");
}
//邏輯判斷---END

function checkList(inForm) {
    Validator.init(inForm);

    if (Validator.isValid())
        return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}