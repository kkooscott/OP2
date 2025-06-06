var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu08').addClass("active");
    $('#menu08span').addClass("selected");
    $('#menu0802').addClass("active");
    init();
});

$(window).resize(function () {
    $('#gridMainList').setGridWidth($(window).width() - 10);
    $('#gridClueList').setGridWidth($('.col-md-12').width() - 63);
    $("#gridList_3").setGridWidth($(window).width() - 92); //報表
    $("#gridList_4").setGridWidth($(window).width() - 92); //認領人
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
    //認領人按鈕事件
    ClaimButton("OP08A02Q_01.jsp");
    //取得戶籍資料
    $('#IPUCLAIM_GET_HH_DATA').click(function () {
        Validator.init(document.getElementById('form1'));
        //證號
        if ($("#IPUCLAIM_OP_PUCP_NAT_CD").val() == "035" && $("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "2") {
            Validator.setMessage("欄位 [ 證號 ] 為\"其他證號\"時，[ 國籍 ]不得為中華民國國籍 !!");
            Validator.setBGColor("IPUCLAIM_OP_PUCP_NAT_CD", "IPUCLAIM_OP_PUCP_IDN_TP");
        } else if ($("#IPUCLAIM_OP_PUCP_NAT_CD").val() != "" && $("#IPUCLAIM_OP_PUCP_NAT_CD").val() != "035" && $("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "1") {
            Validator.setMessage("欄位 [ 證號 ] 為\"身分證號\"時，[ 國籍 ]不得為他國國籍 !!");
            Validator.setBGColor("IPUCLAIM_OP_PUCP_NAT_CD", "IPUCLAIM_OP_PUCP_IDN_TP");
        } else if ($("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "1") {
            Validator.checkID("IPUCLAIM_OP_PUCP_IDN", true, "身分證號");
        } else if ($("#IPUCLAIM_OP_PUCP_IDN_TP").val() == "3") {
            Validator.checkLength("IPUCLAIM_OP_PUCP_IDN", false, "身分證號", 20);
        } else {
            Validator.checkLength("IPUCLAIM_OP_PUCP_IDN", true, "身分證號", 20);
        }
        if (Validator.isValid())
            getOI2PersonByIDN("CLAIM");
        else {
            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
            return false;
        }
    });
    //分頁1 拾得物受理基本資料
    $('#tabInfomation1').click(function () {
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
    //分頁3 民眾認領
    $('#tabInfomation3').click(function () {
        var checkYNDetail = $('#checkYNDetail').val();
        //只是判斷要不要顯示
        clearClaimData();
        if (checkYNDetail == 'Y') {
            $("#tab_4_4_alert").hide();
            $("#tab_4_4_main").show();
            $("#tab_4_4_main2").show();
            if (checkStatusForBasic($("#IPUBASIC_OP_SEQ_NO").val()) == "6") { //如果是結案狀態則不可以修改
                disableAll('IPUCLAIM');
                $("#addClaimBtn").hide();
                $("#clearClaimBtn").hide();
                $("#editClaimBtn").hide();
                $("#deleteClaimBtn").hide();
            } else {
                undisableAll('IPUCLAIM');
                $("#addClaimBtn").show();
                $("#clearClaimBtn").show();
                $("#editClaimBtn").hide();
                $("#deleteClaimBtn").hide();
            }

            showClaimList_OW($('#IPUBASIC_OP_SEQ_NO').val());
            bindCOUNTRYTYPE('', 'IPUCLAIM_OP_PUCP_NAT_CD', ''); //國家
            bindCOUNTRYDDL('2', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', 'ALL'); //鄉鎮
            bindDATATYPE('B03', 'IPUCLAIM_OP_CLAIM_TP_CD', ''); //認領方式
            bindCOUNTRYDDL('2', 'IPUCLAIM_OP_LOST_CITY_CD', 'IPUCLAIM_OP_LOST_TOWN_CD', '', '', 'ALL'); //鄉鎮
            //預設值
            clearClaimData();
        } else {
            $("#tab_4_4_alert").show();
            $("#tab_4_4_main").hide();
            $("#tab_4_4_main2").hide();
            $("#addClaimBtn").hide();
            $("#clearClaimBtn").hide();
            $("#editClaimBtn").hide();
            $("#deleteClaimBtn").hide();
        }
    });
    //分頁4 報表列印
    $('#tabInfomation4').click(function () {
        //只是判斷要不要顯示
        var checkDetail = checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val());
        var checkStatusForBasic = $('#checkStatusForBasic').val();
        if (checkStatusForBasic != 'N') {
            $("#tab_4_3_1_alert").hide();
            if (checkDetail == 'Y') {
                $("#tab_4_3_2_alert").hide();
                $("#tab_4_3_main").show();
                $("#printBtn").show();
                showGridList3($('#IPUBASIC_OP_SEQ_NO').val());
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
        var bool = true;
        var $myGrid = $('#gridList_3');
        var indexInJqgrid = $myGrid.jqGrid('getGridParam', 'selarrrow');
        if (indexInJqgrid.length == 0) {
            $.alert.open('error', '請勾選想列印的資料');
            bool = false;
        }
        if (bool) {
            for (var i = 0; i < indexInJqgrid.length; i++) {
                var selRowId = indexInJqgrid[i];
                var reportType = $myGrid.jqGrid('getCell', selRowId, 'REPORT_TYPE');
                var claimType = $myGrid.jqGrid('getCell', selRowId, 'CLAIM_TYPE');
                var SEQ = $myGrid.jqGrid('getCell', selRowId, 'SEQ');
                if (reportType == "oP02A04Q.doc") { //是有認領權人
                    downloadReport("ReportServlet", {
                        ajaxAction: 'OP02A04Q.doc',
                        reportName: 'oP02A04Q.doc',
                        newReportName: '核對情形回覆函–核對結果正確.doc',
                        OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                        CLAIM_TYPE: claimType,
                        OP_SEQ_NO: SEQ,
                        lockType: 'A'
                    });
                } else if (reportType == "oP02A05Q.doc") { //不是認領權人
                    downloadReport("ReportServlet", {
                        ajaxAction: 'OP02A05Q.doc',
                        reportName: 'oP02A05Q.doc',
                        newReportName: '核對情形回覆函–核對結果正確.doc',
                        OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                        CLAIM_TYPE: claimType,
                        OP_SEQ_NO: SEQ,
                        lockType: 'A'
                    });
                } else if (reportType == "oP02A06Q.doc") { //遺失（拾得）物領據
                    downloadReport("ReportServlet", {
                        ajaxAction: 'OP02A06Q.doc',
                        reportName: 'oP02A06Q.doc,oP02A06Q_1.doc',
                        newReportName: '認領遺失物領據.doc,認領遺失物領據.doc',
                        OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                        CLAIM_TYPE: claimType,
                        OP_SEQ_NO: SEQ,
                        lockType: 'A'
                    });
                } else if (reportType == "oP02A08Q.doc") { //已認領通知函
                    downloadReport("ReportServlet", {
                        ajaxAction: 'OP02A08Q.doc',
                        reportName: 'oP02A08Q.doc',
                        newReportName: '已認領通知函.doc',
                        OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                        CLAIM_TYPE: claimType,
                        OP_SEQ_NO: SEQ,
                        lockType: 'A'
                    });
                }


            }
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

    //民眾自行保管欄位
    $('#value500OpIsCust').show();

    disableAll('IPUBASIC');
    disableAll('IPUDETAIL');

}

var ajaxActionUrl = 'PuMaintainServlet';

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
    //認領人
    if ($('#OP_SEARCH_PUCP_NAME').val() != "") {
        if ($('#OP_SEARCH_PUCP_NAME').val().length > 30) {
            msg += '認領人欄位長度超過30個字<br/>';
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
            url: 'PuValueUnder500Servlet',
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
                OP_PU_YN_OV500: '0',
                OP_PUCP_NAME: $('#OP_SEARCH_PUCP_NAME').val(),
                OP_DEL_FLAG: '0',
                ACTION_TYPE: 'OP08A02Q_01',
                OPR_KIND: appUseList.OPR_KIND,  //202403 查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //202403 查詢目的                
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得日期時間", "拾得地點", "目前狀態", "BASIC_SEQ"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 50
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 70
                }, {
                    name: 'OP_PU_DTTM',
                    index: 'OP_PU_DTTM',
                    width: 40
                }, {
                    name: 'OP_PU_PLACE',
                    index: 'OP_PU_PLACE',
                    width: 100
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
                    $('#IPUCLAIM_OP_AC_RCNO').val(AcRcno);

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

    let search = checkAppUse();//委託查詢 202403
    if (search !== "") {
        $.alert.open({
            type: 'warning',
            content: search
        });
        return false;
    }
    let appUseList = getAppUse(); //202403	

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
    //認領人
    if ($('#OP_SEARCH_PUCP_NAME').val() != "") {
        if ($('#OP_SEARCH_PUCP_NAME').val().length > 30) {
            msg += '認領人欄位長度超過30個字<br/>';
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
            url: 'PuValueUnder500Servlet',
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
                OP_PU_YN_OV500: '0',
                OP_PUCP_NAME: $('#OP_SEARCH_PUCP_NAME').val(),
                OP_DEL_FLAG: '0',
                ACTION_TYPE: 'OP08A02Q_01',
                OPR_KIND: appUseList.OPR_KIND,  //202403 查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //202403 查詢目的
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得日期時間", "拾得地點", "目前狀態", "BASIC_SEQ"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 50
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 70
                }, {
                    name: 'OP_PU_DTTM',
                    index: 'OP_PU_DTTM',
                    width: 40
                }, {
                    name: 'OP_PU_PLACE',
                    index: 'OP_PU_PLACE',
                    width: 100
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
                    $('#IPUCLAIM_OP_AC_RCNO').val(AcRcno);

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

//顯示認領人---START
function showClaimList_OW(OP_BASIC_SEQ_NO) {
    var gridId = "#gridList_4";
    var pagerId = "#pageList_4";
    show_BlockUI_page_noParent('資料準備中…');
    $(gridId).jqGrid('GridUnload');
    $(gridId).jqGrid({
        url: ajaxActionUrl,
        mtype: "POST",
        datatype: "json",
        postData: {
            ajaxAction: 'GetClaimList',
            'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO
        },
        height: "auto",
        autowidth: true,
        colNames: ["收據號碼", "填表日期", "認領方式", "姓名", "證號", "遺失日期", "遺失時間", "遺失地點", "是否為有認領權人", "OP_SEQ_NO", "TABLEFROM"],
        colModel: [{
                name: 'OP_AC_RCNO',
                index: 'OP_AC_RCNO',
                width: 60,
                align: 'left'
            }, {
                name: 'OP_FM_DATE',
                index: 'OP_FM_DATE',
                width: 30,
                align: 'left'
            }, {
                name: 'OP_CLAIM_TP_NM',
                index: 'OP_CLAIM_TP_NM',
                width: 30,
                align: 'left'
            }, {
                name: 'OP_PUCP_NAME',
                index: 'OP_PUCP_NAME',
                width: 30,
                align: 'left'
            }, {
                name: 'OP_PUCP_IDN',
                index: 'OP_PUCP_IDN',
                width: 40,
                align: 'left'
            }, {
                name: 'OP_LOST_DATE',
                index: 'OP_LOST_DATE',
                width: 30,
                align: 'left'
            }, {
                name: 'OP_LOST_TIME',
                index: 'OP_LOST_TIME',
                width: 30,
                align: 'left'
            }, {
                name: 'OP_LOST_PLACE',
                index: 'OP_LOST_PLACE',
                width: 60,
                align: 'left'
            }, {
                name: 'OP_YN_LOSER',
                index: 'OP_YN_LOSER',
                width: 30,
                align: 'left'
            }, {
                name: 'OP_SEQ_NO',
                index: 'OP_SEQ_NO',
                hidden: true
                        // 欄位隱藏
            }, {
                name: 'TABLEFROM',
                index: 'TABLEFROM',
                hidden: true
                        // 欄位隱藏
            }],
        sortname: 'OP_CLAIM_TP_NM',
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
            var row = $(gridId).jqGrid('getRowData', id);
            var OP_SEQ_NO = row.OP_SEQ_NO;
            var TABLEFROM = row.TABLEFROM;
            showClaimValue_OW(OP_SEQ_NO, TABLEFROM);
        }
    });
    $(gridId).jqGrid("clearGridData", true);
    $("#gridList_4").setGridWidth($(window).width() - 92);
    $(gridId).trigger("reloadGrid");
}
//顯示認領人---END

//設定認領人值---START
function showClaimValue_OW(OP_SEQ_NO, TABLEFROM) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
        'ajaxAction': 'showClaimValue',
        'OP_SEQ_NO': OP_SEQ_NO,
        'TABLEFROM': TABLEFROM
    };

    $.ajax({
        url: ajaxActionUrl,
        type: "POST",
        dataType: "json",
        data: ajData,
        success: function (data, textStatus, xhr) {
            var formData = data.formData[0];
            //文件資料
            $('#IPUCLAIM_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
            $('#IPUCLAIM_OP_FM_DATE').val(formData['OP_FM_DATE']);
            //認領人基本資料
            $('#IPUCLAIM_OP_PUCP_NAME').val(formData['OP_PUCP_NAME']);
            $('#IPUCLAIM_OP_PUCP_RNAME').val(formData['OP_PUCP_RNAME']);
            $('#IPUCLAIM_OP_PUCP_IDN_TP').val(formData['OP_PUCP_IDN_TP']);
            $('#IPUCLAIM_OP_PUCP_IDN').val(formData['OP_PUCP_IDN']);
            $('#IPUCLAIM_OP_PUCP_BEFROC').val(formData['OP_PUCP_BEFROC']);
            $('#IPUCLAIM_OP_PUCP_BIRTHDT').val(formData['OP_PUCP_BIRTHDT']);
            $('#IPUCLAIM_OP_PUCP_GENDER').val(formData['OP_PUCP_GENDER']);
            $('#IPUCLAIM_OP_PUCP_NAT_CD').val(formData['OP_PUCP_NAT_CD']);
            $('#IPUCLAIM_OP_PUCP_PHONENO').val(formData['OP_PUCP_PHONENO']);
            $('#IPUCLAIM_OP_PUCP_ZIPCODE').val(formData['OP_PUCP_ZIPCODE']);

            if (formData['OP_PUCP_ADDR_TYPE_CD'] == "1") {
                $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD'][value='1']").prop("checked", true);
                $('#IPUCLAIM_normalPattern').show();
                $('#IPUCLAIM_freePattern').hide();
                $('#IPUCLAIM_GET_HH_DATA').show();
                $('#IPUCLAIM_OP_PUCP_CITY_CD').val(formData['OP_PUCP_CITY_CD']);
                bindCOUNTRYDDL('3', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');
                $('#IPUCLAIM_OP_PUCP_TOWN_CD').val(formData['OP_PUCP_TOWN_CD']);
                bindCOUNTRYDDL('4', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');
                $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val(formData['OP_PUCP_VILLAGE_CD']);
                $('#IPUCLAIM_OP_PUCP_LIN').val(formData['OP_PUCP_LIN']);
                $('#IPUCLAIM_OP_PUCP_ROAD').val(formData['OP_PUCP_ROAD']);
                $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val('');
            } else {
                $("input[name='IPUCLAIM.OP_PUCP_ADDR_TYPE_CD'][value='9']").prop("checked", true);
                $('#IPUCLAIM_normalPattern').hide();
                $('#IPUCLAIM_freePattern').show();
                $('#IPUCLAIM_GET_HH_DATA').hide();
                $('#IPUCLAIM_OP_PUCP_CITY_CD').val('');
                $('#IPUCLAIM_OP_PUCP_TOWN_CD').val('');
                $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val('');
                $('#IPUCLAIM_OP_PUCP_LIN').val('');
                $('#IPUCLAIM_OP_PUCP_ROAD').val('');
                $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val(formData['OP_PUCP_ADDR_OTH']);
            }

            $('#IPUCLAIM_OP_PUCP_EMAIL').val(formData['OP_PUCP_EMAIL']);
            $('#IPUCLAIM_OP_CLAIM_TP_CD').val(formData['OP_CLAIM_TP_CD']);
            //遺失物品資料
            $('#IPUCLAIM_OP_LOST_DATE').val(formData['OP_LOST_DATE']);
            $('#IPUCLAIM_OP_LOST_TIME').val(formData['OP_LOST_TIME']);

            $('#IPUCLAIM_OP_LOST_CITY_CD').val(formData['OP_LOST_CITY_CD']);
            bindCOUNTRYDDL('3', 'IPUCLAIM_OP_LOST_CITY_CD', 'IPUCLAIM_OP_LOST_TOWN_CD', '', '', '');
            $('#IPUCLAIM_OP_LOST_TOWN_CD').val(formData['OP_LOST_TOWN_CD']);
            $('#IPUCLAIM_OP_LOST_PLACE').val(formData['OP_LOST_PLACE']);
            $('#IPUCLAIM_OP_LOST_OJ_DESC').val(formData['OP_LOST_OJ_DESC']);

            //比對結果
            $('#IPUCLAIM_OP_YN_LOSER').val(formData['OP_YN_LOSER']);
            $('#IPUCLAIM_OP_REMARK').val(formData['OP_REMARK']);
            if (checkStatusForBasic($("#IPUBASIC_OP_SEQ_NO").val()) == "6") { //如果是結案狀態則不可以修改

            } else {
                if (TABLEFROM == "Claim") {
                    $("#addClaimBtn").show();
                    $("#clearClaimBtn").show();
                    $("#editClaimBtn").show();
                    $("#deleteClaimBtn").show();
                    undisableAll('IPUCLAIM');
                } else {
                    $("#addClaimBtn").hide();
                    $("#clearClaimBtn").show();
                    $("#editClaimBtn").show();
                    $("#deleteClaimBtn").hide();
                    disableAll('IPUCLAIM');
                    $('#IPUCLAIM_OP_YN_LOSER').prop('disabled', false);
                    $('#IPUCLAIM_OP_REMARK').prop('disabled', false);
                }
            }

            $.unblockUI();
        }
    });
}
//設定認領人值---END

//報表列印---START
function showGridList3(OP_BASIC_SEQ_NO) {
    var gridId = "#gridList_3";
    var pagerId = "#pageList_3";
    show_BlockUI_page_noParent('資料準備中…');
    $(gridId).jqGrid('GridUnload');
    $(gridId).jqGrid({
        url: ajaxActionUrl,
        mtype: "POST",
        datatype: "json",
        postData: {
            ajaxAction: 'GetClaimReportList',
            'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO
        },
        height: "auto",
        autowidth: true,
        colNames: ["檔案名稱", "說明", "REPORT_TYPE", "CLAIM_TYPE", "SEQ"],
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
            }, {
                name: 'REPORT_TYPE',
                index: 'REPORT_TYPE',
                hidden: true
                        // 欄位隱藏
            }, {
                name: 'CLAIM_TYPE',
                index: 'CLAIM_TYPE',
                hidden: true
                        // 欄位隱藏
            }, {
                name: 'SEQ',
                index: 'SEQ',
                hidden: true
                        // 欄位隱藏
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
    $("#gridList_3").setGridWidth($(window).width() - 92);
    $(gridId).trigger("reloadGrid");
}

// 查詢條件 清除條件
function cleanEditor() {
    $('#queryporlet input[type="text"]').val('');
    $('#OP_SEARCH_PUPO_TP_CD').val('');
    $('#OP_SEARCH_PUOJ_ATTR_CD').val('');
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
function clearClaimData() {
    var nowDate = new Date();
    $('#actionClaimType').val('insertClaimList');
    $('#IPUCLAIM_OP_SEQ_NO').val('');
    $('#IPUCLAIM_OP_FM_DATE').val(getROCDateSlash(nowDate));
    $('#IPUCLAIM_OP_PUCP_NAME').val('');
    $('#IPUCLAIM_OP_PUCP_RNAME').val('');
    $('#IPUCLAIM_OP_PUCP_IDN_TP').val('1');
    $('#IPUCLAIM_OP_PUCP_IDN').val('');
    $('#IPUCLAIM_OP_PUCP_BEFROC').val('1');
    $('#IPUCLAIM_OP_PUCP_BIRTHDT').val('');
    $('#IPUCLAIM_OP_PUCP_GENDER').val('');
    $('#IPUCLAIM_OP_PUCP_NAT_CD').val('035');
    $('#IPUCLAIM_OP_PUCP_PHONENO').val('');
    $('#IPUCLAIM_OP_PUCP_ZIPCODE').val('');
    $('#IPUCLAIM_OP_PUCP_CITY_CD').val('');
    $('#IPUCLAIM_OP_PUCP_TOWN_CD').val('');
    $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val('');
    $('#IPUCLAIM_OP_PUCP_LIN').val('');
    $('#IPUCLAIM_OP_PUCP_ROAD').val('');
    $('#IPUCLAIM_OP_PUCP_ADDR_OTH').val('');
    $('#IPUCLAIM_OP_PUCP_EMAIL').val('');
    $('#IPUCLAIM_OP_CLAIM_TP_CD').val('2');
    $('#IPUCLAIM_OP_LOST_DATE').val('');
    $('#IPUCLAIM_OP_LOST_TIME').val('');
    $('#IPUCLAIM_OP_LOST_CITY_CD').val('');
    $('#IPUCLAIM_OP_LOST_TOWN_CD').val('');

    $('#IPUCLAIM_OP_LOST_PLACE').val('');
    $('#IPUCLAIM_OP_LOST_OJ_DESC').val('');
    $('#IPUCLAIM_OP_YN_LOSER').val('');
    $('#IPUCLAIM_OP_REMARK').val('');

//    $( "#addClaimBtn" ).show();
//    $( "#clearClaimBtn" ).show();
//    $( "#editClaimBtn" ).hide();
//    $( "#deleteClaimBtn" ).hide();
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