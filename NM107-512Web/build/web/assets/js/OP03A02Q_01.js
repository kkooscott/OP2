var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu03').addClass("active");
    $('#menu03span').addClass("selected");
    $('#menu0302').addClass("active");
    init();
});

$(window).resize(function () {
    $('#gridMainList').setGridWidth($(window).width() - 10);
    $('#gridClueList').setGridWidth($('.col-md-12').width() - 63);
    $("#gridList_3").setGridWidth($(window).width() - 92); //報表
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
    bindDATATYPE('B02', 'OP_SEARCH_PUOJ_ATTR_CD', '請選擇...');

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
    AnDlButton();
    //分頁1 拾得物受理基本資料
    $('#tabInfomation1').click(function () {
        bindDATATYPE('B01', 'IPUBASIC_OP_PUPO_TP_CD', '');
        bindDATATYPE('B02', 'IPUBASIC_OP_PUOJ_ATTR_CD', '');
        bindDATATYPE('B04', 'IPUBASIC_OP_CURSTAT_CD', '');
        bindDATATYPE('B07', 'IPUBASIC_OP_NTC_PSN_TYPE', '請選擇...');
        bindCOUNTRYTYPE('', 'IPUBASIC_OP_PUPO_NAT_CD', ''); //國家
        bindCOUNTRYDDL('2', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', 'ALL'); //鄉鎮
        bindCOUNTRYDDL('2', 'IPUBASIC_OP_PU_CITY_CD', 'IPUBASIC_OP_PU_TOWN_CD', '', '', 'ALL'); //鄉鎮
        showBasicGridList($('#IPUBASIC_OP_SEQ_NO').val());
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
    //分頁3 內部公告
    $('#tabInfomation3').click(function () {
        var checkYNDetail = $('#checkYNDetail').val();
        if (checkYNDetail == 'Y') {
            clearAnn1Data();
            $('#tab_4_5_main').show();
            $('#tab_4_5_alert').hide();
            showAnn1GridList($('#IPUBASIC_OP_SEQ_NO').val());
        } else {
            clearAnn1Data();
            $('#tab_4_5_main').hide();
            $('#tab_4_5_alert').show();
        }
    });
    //分頁4 網路公告
    $('#tabInfomation4').click(function () {
        clearAnn2Data();
        var checkYNDetail = $('#checkYNDetail').val();
        if (checkYNDetail == 'Y') {
            $('#tab_4_6_main').show();
            $('#tab_4_6_alert').hide();
            bindCOUNTRYDDL('2', 'IPUBASIC_OP_PU_CITY_CD2', 'IPUBASIC_OP_PU_TOWN_CD2', '', '', 'ALL'); //鄉鎮
            showAnn2GridList($('#IPUBASIC_OP_SEQ_NO').val());
        } else {
            $('#getAnnounceBtn2').hide();
            $('#tab_4_6_main').hide();
            $('#tab_4_6_alert').show();
        }
    });
    //分頁5 招領期滿處理資料
    $('#tabInfomation5').click(function () {
        clearIAnDlData();
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
            $('#saveIAnDlBtn').show();
            $('#tab_4_7_main').show();
            $('#tab_4_7_alert').hide();
            getIAnDlGridList($('#IPUBASIC_OP_SEQ_NO').val(), "OP03A02Q"); //取得畫面資料
        } else {
            $('#saveIAnDlBtn').hide();
            $('#tab_4_7_main').hide();
            $('#tab_4_7_alert').show();
        }
    });
    //分頁6 報表列印
    $('#tabInfomation6').click(function () {
        //只是判斷要不要顯示
        var checkDetail = $('#checkYNDetail').val();
        var checkStatusForBasic = $('#checkStatusForBasic').val();
        if (checkStatusForBasic != 'N') {
            $("#tab_4_3_1_alert").hide();
            if (checkDetail == 'Y') {
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
                ajaxAction: 'OP03A03Q.doc',
                reportName: 'oP03A03Q.doc',
                newReportName: '拾得人領回通知.doc',
                OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
        }
        if ($('#jqg_gridList_3_2').prop('checked')) {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP03A04Q.doc',
                reportName: 'oP03A04Q.doc',
                newReportName: '拾得人領回公告.doc',
                OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
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
    disableAll('IANNOUNCE'); //內部公告
    disableAll('IANNOUNCE2'); //網路公告
}

var ajaxActionUrl = 'PuMaintainServlet';

//BASIC_SHOW---START

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
function showGridListbyQS() {
    let appUseList = getAppUse();   //202403		
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
                ajaxAction: 'IPuBasicQueryForDown',
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
                OP_PUOJ_ATTR_CD: $('#OP_SEARCH_PUOJ_ATTR_CD').val(),
                OP_PUOJ_ATTR_NM: $('#OP_SEARCH_PUOJ_ATTR_CD').val() == '' ? '' : $('#OP_SEARCH_PUOJ_ATTR_CD option:selected').text(),
                OP_CURSTAT_CD: $("#OP_SEARCH_CURSTAT_CD").val(),
                OP_CURSTAT_NM: $('#OP_SEARCH_CURSTAT_CD').val() == '' ? '' : $('#OP_SEARCH_CURSTAT_CD option:selected').text(),
                OP_DOC_WD: $("#OP_SEARCH_DOC_WD").val(),
                OP_DOC_NO: $("#OP_SEARCH_DOC_NO").val(),
                OP_PU_YN_OV500: '1',
                OP_DEL_FLAG: '0',
                ACTION_TYPE: 'OP03A02Q_01',
                OPR_KIND: appUseList.OPR_KIND,  //202403 查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //202403 查詢目的

            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得人類別", "物品屬性", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "目前狀態", "BASIC_SEQ"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 70
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 100
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
                    width: 50
                }, {
                    name: 'OP_PU_DTTM',
                    index: 'OP_PU_DTTM',
                    width: 50
                }, {
                    name: 'OP_PU_PLACE',
                    index: 'OP_PU_PLACE',
                    width: 80
                }, {
                    name: 'OP_CURSTAT_NM',
                    index: 'OP_CURSTAT_NM',
                    width: 30
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
                    $('#tabInfomation5').addClass('active');
                    $('#tab_4_5').addClass('active');

                    $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                    $('#checkYNDetail').val(checkYNDetail(BasicNo));
                    BasicValueForLog(BasicNo);
                    $('#tabInfomation5').click();
                    goTab('modal-editor-add', 'tabMain');
                }
            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
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
                ajaxAction: 'IPuBasicQueryForDownSearch',
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
                OP_PUOJ_ATTR_CD: $('#OP_SEARCH_PUOJ_ATTR_CD').val(),
                OP_PUOJ_ATTR_NM: $('#OP_SEARCH_PUOJ_ATTR_CD').val() == '' ? '' : $('#OP_SEARCH_PUOJ_ATTR_CD option:selected').text(),
                OP_CURSTAT_CD: $("#OP_SEARCH_CURSTAT_CD").val(),
                OP_CURSTAT_NM: $('#OP_SEARCH_CURSTAT_CD').val() == '' ? '' : $('#OP_SEARCH_CURSTAT_CD option:selected').text(),
                OP_DOC_WD: $("#OP_SEARCH_DOC_WD").val(),
                OP_DOC_NO: $("#OP_SEARCH_DOC_NO").val(),
                OP_PU_YN_OV500: '1',
                OP_DEL_FLAG: '0',
                ACTION_TYPE: 'OP03A02Q_01',
                OPR_KIND: appUseList.OPR_KIND,  //202403 查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //202403 查詢目的
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得人類別", "物品屬性", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "目前狀態", "BASIC_SEQ"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 70
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 100
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
                    width: 50
                }, {
                    name: 'OP_PU_DTTM',
                    index: 'OP_PU_DTTM',
                    width: 50
                }, {
                    name: 'OP_PU_PLACE',
                    index: 'OP_PU_PLACE',
                    width: 80
                }, {
                    name: 'OP_CURSTAT_NM',
                    index: 'OP_CURSTAT_NM',
                    width: 30
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
                    $('#tabInfomation5').addClass('active');
                    $('#tab_4_5').addClass('active');

                    $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                    $('#checkYNDetail').val(checkYNDetail(BasicNo));
                    BasicValueForLog(BasicNo);
                    $('#tabInfomation5').click();
                    goTab('modal-editor-add', 'tabMain');
                }
            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}

////IANDL_SHOW---START
//function getIAnDlGridList_OW(OP_BASIC_SEQ_NO) {
//    show_BlockUI_page_noParent('資料選取中…');
//    var ajData = {
//                    'ajaxAction' : 'getIAnDlGridList',
//                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
//            };
//            var ajSucc = function(JData) {
//                if (JData.formData){
//                    var formData = JData.formData[0];
//                    if( formData['ACTION_TYPE'] == "insertIAnDlList" ){
//                        $('#actionAnDlType').val( formData['ACTION_TYPE'] );
//                        $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
//                        //處理單位
//                        $('#OP_PR_unitLevel2').val(strPermissionData['UNITLEVEL1']);
//                        bindUNITDDL('3', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');
//                        $('#OP_PR_unitLevel3').val(strPermissionData['UNITLEVEL2']);
//                        bindUNITDDL('4', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');
//                        $('#OP_PR_unitLevel4').val(strPermissionData['UNITLEVEL3']);
//                        //處理人員
//                        $('#IANDL_OP_PR_STAFF_NM').val(strPermissionData['NAME']);
//                        
//                        $('#IANDL_OP_PR_DATE').val('');
//                        $('#IANDL_OP_YN_AUCTION').val('N');
//                        $('#IANDL_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
//                        
//                        //拾得人領回公告發文單位            
//                        $('#OP_PUPOANUNIT_unitLevel2').val(strPermissionData['UNITLEVEL1']);
//                        bindUNITDDL('3', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');
//                        $('#OP_PUPOANUNIT_unitLevel3').val(strPermissionData['UNITLEVEL2']);
//                        //bindUNITDDL('4', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');
//                        //$('#OP_PUPOANUNIT_unitLevel4').val(strPermissionData['UNITLEVEL3']);
//                        
//                        var nowDate = new Date();
//                        nowDate.setMonth(nowDate.getMonth() + 3);
//                        $('#IANDL_OP_PUPO_ANDTEND').val(getROCDateSlash(nowDate) );
//                        
//                        $('#IANDL_OP_PUPO_DOC_WD').val( formData['OP_PUPO_DOC_WD'] );
//                        $('#IANDL_OP_PUPO_DOC_NO').val( formData['OP_PUPO_DOC_NO'] );
//                        $('#OP_YN_GET_NO2').val( formData['OP_YN_GET_NO2'] );
//                        if( formData['OP_YN_GET_NO2'] == "1" ){
//                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
//                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true); 
//                        }else{
//                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', false); 
//                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', false); 
//                        }
//                        $('#IANDL_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
//                        $('#IANDL_OP_PR_STAT_DESC').val('');
//                    }else{
//                        $('#actionAnDlType').val( formData['ACTION_TYPE'] );
//                        $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
//                        //處理單位
//                        var data1 = getunit_code(formData['OP_PR_UNIT_CD']);
//                        if ( data1.result[0]["OP_DEPT_CD"]==data1.result[0]["OP_UNIT_CD"]){//只有警局
//                                $('#OP_PR_unitLevel2').val( data1.result[0]["OP_UNIT_CD"]);
//                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
//                        }else if ( data1.result[0]["OP_UNIT_CD"]==data1.result[0]["OP_BRANCH_CD"]|| data1.result[0]["OP_BRANCH_CD"]==''){//只有警分局
//                                $('#OP_PR_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
//                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
//                                $('#OP_PR_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
//                                bindUNITDDL('4','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
//                        }else{
//                                $('#OP_PR_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
//                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
//                                $('#OP_PR_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
//                                bindUNITDDL('4','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
//                                $('#OP_PR_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
//                        }
//                        
//                        //處理人員
//                        $('#IANDL_OP_PR_STAFF_NM').val(formData['OP_PR_STAFF_NM']);
//                        
//                        $('#IANDL_OP_PR_DATE').val( formData['OP_PR_DATE'] );
//                        $('#IANDL_OP_YN_AUCTION').val( formData['OP_YN_AUCTION'] );
//                        $('#IANDL_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
//                        
//                        //拾得人領回公告發文單位
//                        var data2 = getunit_code(formData['OP_PUPOANUNITCD']);
//                        if ( data2.result[0]["OP_DEPT_CD"]==data2.result[0]["OP_UNIT_CD"]){//只有警局
//                                $('#OP_PUPOANUNIT_unitLevel2').val( data2.result[0]["OP_UNIT_CD"]);
//                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
//                        }else if ( data2.result[0]["OP_UNIT_CD"]==data2.result[0]["OP_BRANCH_CD"]|| data2.result[0]["OP_BRANCH_CD"]==''){//只有警分局
//                                $('#OP_PUPOANUNIT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
//                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
//                                $('#OP_PUPOANUNIT_unitLevel3').val(data2.result[0]["OP_UNIT_CD"]);
//                                //bindUNITDDL('4','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
//                        }else{
//                                $('#OP_PUPOANUNIT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
//                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
//                                $('#OP_PUPOANUNIT_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
//                                //bindUNITDDL('4','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
//                                //$('#OP_PUPOANUNIT_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
//                        }
//                        
//                        $('#IANDL_OP_PUPO_ANDTEND').val( formData['OP_PUPO_ANDTEND'] );
//                        
//                        $('#IANDL_OP_PUPO_DOC_WD').val( formData['OP_PUPO_DOC_WD'] );
//                        $('#IANDL_OP_PUPO_DOC_NO').val( formData['OP_PUPO_DOC_NO'] );
//                        
//                        $('#OP_YN_GET_NO2').val( formData['OP_YN_GET_NO2'] );
//                        if( formData['OP_YN_GET_NO2'] == "1" ){
//                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
//                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true); 
//                        }else{
//                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', false); 
//                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', false); 
//                        }
//                        $('#IANDL_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
//                        $('#IANDL_OP_PR_STAT_DESC').val( formData['OP_PR_STAT_DESC'] );
//                        
//                    }
//                    $.unblockUI();
//                }else{
//                    $.alert.open('error', "選取資料失敗!!!");
//                    $.unblockUI();
//                }
//            };
//            var ajErr = function() {
//                    $.alert.open('error', "選取資料失敗!!!");
//                    $.unblockUI();
//            };
//
//            $.ajax({
//                    url : 'AnnounceServlet',
//                    type : "POST",
//                    dataType : "json",
//                    data : ajData,
//                    success : ajSucc,
//                    error : ajErr
//            });
//			
//}
////IANDL_SHOW---END

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
        FILE_NAME: '拾得人領回通知'
    });
    $(gridId).jqGrid('addRowData', 2, {
        FILE_NAME: '拾得人領回公告'
    });

}
//報表列印---END

//邏輯判斷---START
// 查詢條件 清除條件
function cleanEditor() {
    $('#queryporlet input[type="text"]').val('');
    $('#OP_SEARCH_PUPO_TP_CD').val('');
    $('#OP_SEARCH_PUOJ_ATTR_CD').val('');
    $('#OP_SEARCH_CURSTAT_CD').val('');
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
function clearAnn1Data() {
    $('#IANNOUNCE_OP_SEQ_NO').val('');
    $('#IANNOUNCE_OP_AC_DATE').val('');
    $('#IANNOUNCE_OP_AN_DATE_BEG').val('');
    $('#IANNOUNCE_OP_CABINET_NO').val('');
    $('#IANNOUNCE_OP_DOC_DT').val('');
    $('#IANNOUNCE_OP_DOC_WD').val('');
    $('#IANNOUNCE_OP_DOC_NO').val('');
    $('#IANNOUNCE_OP_AN_DATE_END').val('');
    $('#IANNOUNCE_OP_AN_CONTENT').val('');
    $('#IANNOUNCE_OP_AN_REMARK').val('');
    $('#IANNOUNCE_OP_KP_UNIT_NM').val('');
    $('#IANNOUNCE_OP_KP_NM').val('');
    $('#IANNOUNCE_OP_KP_DATE').val('');
    $('#IANNOUNCE_OP_KP_TIME').val('');
    $('#IANNOUNCE_OP_KP_REMARK').val('');
    $('#IANNOUNCE_OP_KP_UNIT_NM1').val('');
    $('#IANNOUNCE_OP_KP_NM1').val('');
    $('#IANNOUNCE_OP_KP_DATE1').val('');
    $('#IANNOUNCE_OP_KP_TIME1').val('');
    $('#IANNOUNCE_OP_KP_REMARK1').val('');
    $('#IANNOUNCE_OP_KP_UNIT_NM2').val('');
    $('#IANNOUNCE_OP_KP_NM2').val('');
    $('#IANNOUNCE_OP_KP_DATE2').val('');
    $('#IANNOUNCE_OP_KP_TIME2').val('');
    $('#IANNOUNCE_OP_KP_REMARK2').val('');
}
function clearAnn2Data() {
    $('#IANNOUNCE_OP_SEQ_NO2').val('');
    $('#OP_AC2_unitLevel2').val('');
    $('#IANNOUNCE_OP_AC_DATE2').val('');
    $('#IPUBASIC_OP_AC_UNIT_TEL2').val('');
    $('#IANNOUNCE_OP_DOC_WD2').val('');
    $('#IANNOUNCE_OP_DOC_NO2').val('');
    $('#IANNOUNCE_OP_AC_UNIT_ALL').val('');
    $('#IANNOUNCE_OP_AN_DATE_BEG2').val('');
    $('#IANNOUNCE_OP_AN_CONTENT2').val('');
    $('#IPUBASIC_OP_PU_DATE2').val('');
    $('#IPUBASIC_OP_PU_TIME2').val('');
    $('#IPUBASIC_OP_PU_CITY_CD2').val('');
    $('#IPUBASIC_OP_PU_TOWN_CD2').val('');
    $('#IPUBASIC_OP_PU_PLACE2').val('');
    $('#IANNOUNCE_OP_AN_DATE_END2').text('');
    $('#file3').val('');
    $('#file4').val('');
    $('#previewPhoto3').attr('src', 'assets/img/file-upload-with-preview.png');
    $('#previewPhoto4').attr('src', 'assets/img/file-upload-with-preview.png');
    $('#OP_UPL_FILE_NAME3').val('');
    $('#IPHOTO_OP_SEQ_NO3').val('');
    $('#IPHOTO_OP_FILE_PATH3').val('');
    $('#IPHOTO_DETELE3').val('');
    $('#OP_UPL_FILE_NAME4').val('');
    $('#IPHOTO_OP_SEQ_NO4').val('');
    $('#IPHOTO_OP_FILE_PATH4').val('');
    $('#IPHOTO_DETELE4').val('');
    $('#getAnnounceBtn2').hide();
    $('#saveAnnounceBtn2').hide();

}
function clearIAnDlData() {
    $('#IANDL_OP_SEQ_NO').val('');
    $('#OP_PR_unitLevel2').val('');
    $('#OP_PR_unitLevel3').val('');
    $('#OP_PR_unitLevel4').val('');
    $('#IANDL_OP_PR_STAFF_NM').val('');
    $('#IANDL_OP_PR_DATE').val('');
    $('#IANDL_OP_YN_AUCTION').val('N');
    $('#IANDL_OP_NTC_PUPO_DT').val('');
    $('#OP_PUPOANUNIT_unitLevel2').val('');
    $('#OP_PUPOANUNIT_unitLevel3').val('');
    $('#OP_PUPOANUNIT_unitLevel4').val('');
    $('#IANDL_OP_PUPO_DOC_WD').val('');
    $('#IANDL_OP_PUPO_DOC_NO').val('');
    $('#IANDL_OP_PUPO_ANDTEND').val('');
    $('#IANDL_OP_PUPO_AN_CONT').val('');
    $('#IANDL_OP_PR_STAT_DESC').val('');
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