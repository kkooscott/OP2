var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu06').addClass("active");
    $('#menu06span').addClass("selected");
    $('#menu0601').addClass("active");
    init();
});

$(window).resize(function () {
    $('#gridMainList').setGridWidth($(window).width() - 10);
    $('#gridClueList').setGridWidth($('.col-md-12').width() - 63);
    $("#gridList_3").setGridWidth($(window).width() - 92);
    $("#gridList_4").setGridWidth($(window).width() - 92);
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
    bindDATATYPE('B04', 'OP_SEARCH_CURSTAT_CD', '請選擇...');
    bindDATATYPE('B05', 'OP_SEARCH_FS_REC_CD', '請選擇...');//結案紀錄代碼

    //受理基本資料先load(第一頁)
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
        //只是判斷要不要顯示
        var checkYNDetail = $('#checkYNDetail').val();
        clearClaimData();
        if (checkYNDetail == 'Y') {
            $("#tab_4_4_alert").hide();
            $("#tab_4_4_main").show();
            $("#tab_4_4_main2").show();
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
        }
    });
    //分頁4 內部公告
    $('#tabInfomation4').click(function () {
        var checkYNDetail = $('#checkYNDetail').val();
        if (checkYNDetail == 'Y') {
            show_BlockUI_page_noParent('資料準備中…');
            clearAnn1Data();
            $('#tab_4_5_main').show();
            $('#tab_4_5_alert').hide();
            // 帶入登入者單位
            $('#OP_AN_unitLevel2').val(strPermissionData['UNITLEVEL1']);
            bindUNITDDL('3', 'OP_AN_unitLevel2', 'OP_AN_unitLevel3', 'OP_AN_unitLevel4', '');
            $('#OP_AN_unitLevel3').val(strPermissionData['UNITLEVEL2']);
            bindUNITDDL('4', 'OP_AN_unitLevel2', 'OP_AN_unitLevel3', 'OP_AN_unitLevel4', '');
            $('#OP_AN_unitLevel4').val(strPermissionData['UNITLEVEL3']);
            var nowDate = new Date();
            $('#IANNOUNCE_OP_AN_DATE_BEG').val(getROCDateSlash(nowDate));
            $('#IANNOUNCE_OP_DOC_DT').val(getROCDateSlash(nowDate));
            nowDate.setMonth(nowDate.getMonth() + 6);
            $('#IANNOUNCE_OP_AN_DATE_END').val(getROCDateSlash(nowDate));
            getAnn1GridList($('#IPUBASIC_OP_SEQ_NO').val(), "OP06A01Q");
        } else {
            clearAnn1Data();
            $('#saveAnnounceBtn').hide();
            $('#tab_4_5_main').hide();
            $('#tab_4_5_alert').show();
        }
    });
    //分頁5 網路公告
    $('#tabInfomation5').click(function () {
        clearAnn2Data();
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
            $('#tab_4_6_main').show();
            $('#tab_4_6_alert').hide();
            bindCOUNTRYDDL('2', 'IPUBASIC_OP_PU_CITY_CD2', 'IPUBASIC_OP_PU_TOWN_CD2', '', '', 'ALL'); //鄉鎮
            var check = checkYNAnn1($('#IPUBASIC_OP_SEQ_NO').val());
            if (check == '1') { //內部公告 1
                disableAll('IANNOUNCE2');
            } else if (check == '2') { //網路公告 2
                disableAll('IANNOUNCE2');
                getAnn2GridList($('#IPUBASIC_OP_SEQ_NO').val());
            } else { // 沒有資料
                $('#getAnnounceBtn2').hide();
                $('#saveAnnounceBtn2').hide();
                disableAll('IANNOUNCE2');
            }
        } else {
            $('#getAnnounceBtn2').hide();
            $('#tab_4_6_main').hide();
            $('#tab_4_6_alert').show();
        }
    });
    //分頁6 招領期滿處理資料
    $('#tabInfomation6').click(function () {
        clearIAnDlData();
        var checkYNDetail = $('#checkYNDetail').val();
        if (checkYNDetail == 'Y') {
            $('#tab_4_7_main').show();
            $('#tab_4_7_alert').hide();
            getIAnDlGridList($('#IPUBASIC_OP_SEQ_NO').val(), "OP06A01Q"); //取得畫面資料
        } else {
            $('#tab_4_7_main').hide();
            $('#tab_4_7_alert').show();
        }
    });
    //分頁7 拾得人領回公告
    $('#tabInfomation7').click(function () {
        var checkYNDetail = $('#checkYNDetail').val();
        if (checkYNDetail == 'Y') {
            $('#tab_4_8_main').show();
            $('#tab_4_8_alert').hide();
            showIPuanAnnGridList($('#IPUBASIC_OP_SEQ_NO').val()); //顯示畫面資料
        } else {
            $('#tab_4_8_main').hide();
            $('#tab_4_8_alert').show();
        }
    });
    //分頁8 拾得人領回公告期滿
    $('#tabInfomation8').click(function () {
        var checkYNDetail = $('#checkYNDetail').val();
        if (checkYNDetail == 'Y') {
            $('#tab_4_9_main').show();
            $('#tab_4_9_alert').hide();
            getIPuanDlGridList($('#IPUBASIC_OP_SEQ_NO').val()); //顯示畫面資料
        } else {
            $('#saveIPuanDlBtn').hide();
            $('#tab_4_9_main').hide();
            $('#tab_4_9_alert').show();
        }
    });
    //分頁9 結案資料
    $('#tabInfomation9').click(function () {
        bindDATATYPE('B05', 'IFNSH_OP_FS_REC_CD', ''); //認領方式
        getIFnshGridList($('#IPUBASIC_OP_SEQ_NO').val()); //顯示畫面資料
    });
    //分頁10 結案資料(伍佰元)
    $('#tabInfomation10').click(function () {
        bindDATATYPE('B05', 'IFNSH2_OP_FS_REC_CD', ''); //認領方式
        getIFnshGridListFor500($('#IPUBASIC_OP_SEQ_NO').val()); //顯示畫面資料
    });
    //分頁11 報表列印
    $('#tabInfomation11').click(function () {
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
            if ($('#jqg_gridList_3_1').prop('checked'))
            {
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP02A01Q.doc',
                    reportName: 'oP02A01Q.doc,oP02A02Q.doc',
                    newReportName: '拾得物收據.doc,拾得物收據.doc',
                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                    lockType: 'A'
                });
            }
            if ($('#jqg_gridList_3_2').prop('checked'))
            {
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP02A09Q.doc',
                    reportName: 'oP02A09Q.doc',
                    newReportName: '受理拾得物案陳報單.doc',
                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                    lockType: 'A'
                });
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
    //20231120 為了多加OP300008 署稽查人員權限(只有menu06查詢主選單)
//  if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1) {
    if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1 || strPermissionData["Roles"].toString().indexOf('OP300008') > -1) {

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
//        if (checkList(document.getElementById('form1')))
//           showGridListbyQS();

    disableAll('IPUBASIC'); //受理資料
    disableAll('IPUDETAIL'); //明細資料
    disableAll('IPUCLAIM'); //認領資料
    disableAll('IANNOUNCE'); //內部公告
    disableAll('IANNOUNCE2'); //網路公告
    disableAll('IANDL'); //招領期滿處理
    disableAll('IPUANANN'); //拾得人領回公告
    disableAll('IPUANDL'); //拾得人領回公告期滿
    disableAll('IFNSH'); //結案資料
    disableAll('IFNSH2'); //結案資料(伍佰元)
    disableAll('IPUANDL2'); //拾得人領回公告期滿(伍佰元)
}

var ajaxActionUrl = 'PuMaintainServlet';

//BASIC_SHOW---START
function showGridListbyQS() {
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
        
        let appUseList = getAppUse(); //202403
        
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
            url: 'CompositeSearchServlet',
            mtype: "POST",
            datatype: "json",
            postData: {
                ajaxAction: 'CompositeSearchForBasic',
                OP_UNITLEVEL2: $('#OP_Search_unitLevel2').val(),
                OP_UNITLEVEL2_NM: $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
                OP_UNITLEVEL3: $('#OP_Search_unitLevel3').val(),
                OP_UNITLEVEL3_NM: $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text(),
                OP_UNITLEVEL4: $('#OP_Search_unitLevel4').val(),
                OP_UNITLEVEL4_NM: $('#OP_Search_unitLevel4').val() == '' ? '' : $('#OP_Search_unitLevel4 option:selected').text(),
                UNITLEVEL_NM: OPENLLevel_NM,
                OP_AC_STAFF_NM: $("#OP_SEARCH_AC_STAFF_NM").val(),
                OP_AC_RCNO: $("#OP_SEARCH_AC_RCNO").val(),
                OP_PUPO_NAME: $("#OP_SEARCH_PUPO_NAME").val(),
                OP_PUPO_GENDER: $("#OP_SEARCH_PUPO_GENDER").val(),
                OP_PUPO_GENDER_NM: $('#OP_SEARCH_PUPO_GENDER').val() == '' ? '' : $('#OP_SEARCH_PUPO_GENDER option:selected').text(),
                OP_PUPO_IDN_TP: $("#OP_SEARCH_PUPO_IDN_TP").val(),
                OP_PUPO_IDN_TP_NM: $('#OP_SEARCH_PUPO_IDN_TP').val() == '' ? '' : $('#OP_SEARCH_PUPO_IDN_TP option:selected').text(),
                OP_PUPO_IDN: $("#OP_SEARCH_PUPO_IDN").val(),
                OP_PUOJ_NM: $("#OP_SEARCH_PUOJ_NM").val(),
                OP_PU_DATE_S: getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                OP_PU_DATE_E: getADDate($("#OP_SEARCH_PU_DATE_E").val()),
                OP_AC_DATE_S: getADDate($("#OP_SEARCH_AC_DATE_S").val()),
                OP_AC_DATE_E: getADDate($("#OP_SEARCH_AC_DATE_E").val()),
                OP_PUOJ_ATTR_CD: $('#OP_SEARCH_PUOJ_ATTR_CD').val(),
                OP_PUOJ_ATTR_NM: $('#OP_SEARCH_PUOJ_ATTR_CD').val() == '' ? '' : $('#OP_SEARCH_PUOJ_ATTR_CD option:selected').text(),
                OP_CURSTAT_CD: $("#OP_SEARCH_CURSTAT_CD").val(),
                OP_CURSTAT_NM: $('#OP_SEARCH_CURSTAT_CD').val() == '' ? '' : $('#OP_SEARCH_CURSTAT_CD option:selected').text(),
                OP_FS_REC_CD: $("#OP_SEARCH_FS_REC_CD").val(),
                OP_FS_REC_NM: $('#OP_SEARCH_FS_REC_CD').val() == '' ? '' : $('#OP_SEARCH_FS_REC_CD option:selected').text(),
                OP_PU_YN_OV500: $("#OP_SEARCH_PU_YN_OV500").val(),
                OP_RT_STAFF_NM: $("#OP_SEARCH_RT_STAFF_NM").val(),
                ACTION_TYPE: 'OP06A01Q_01',
                OPR_KIND: appUseList.OPR_KIND,  //30.委託查詢 202403 _202403 //31.查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //31.委託查詢 202403 _202403 //32.查詢目的

            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "物品名稱", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "目前狀態", "超過伍佰元", "BASIC_SEQ", "OP_PUPO_PHONENO", "OP_PUPO_BIRTHDT"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 70
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 90
                }, {
                    name: 'OP_PUOJ_NM',
                    index: 'OP_PUOJ_NM',
                    width: 90
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
                    name: 'OP_PU_YN_OV500',
                    index: 'OP_PU_YN_OV500',
                    width: 40
                }, {
                    name: 'BASIC_SEQ',
                    index: 'BASIC_SEQ',
                    hidden: true
                            // 欄位隱藏
                }, {
                    name: 'OP_PUPO_PHONENO',
                    index: 'OP_PUPO_PHONENO',
                    hidden: true
                            // 欄位隱藏
                }, {
                    name: 'OP_PUPO_BIRTHDT',
                    index: 'OP_PUPO_BIRTHDT',
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
                var YnOV500 = row.OP_PU_YN_OV500;
                var OP_PUPO_NAME = row.OP_PUPO_NAME;
                var OP_PUPO_IDN = row.OP_PUPO_IDN;
                var OP_PUPO_PHONENO = row.OP_PUPO_PHONENO;
                var OP_PUPO_BIRTHDT = row.OP_PUPO_BIRTHDT;

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
                    BasicValueForLog2(AcRcno, OP_PUPO_NAME, OP_PUPO_IDN, OP_PUPO_PHONENO, OP_PUPO_BIRTHDT);

                    if (YnOV500 == "是") { //大於伍佰元以上
                        $('#tabInfomation1').show();
                        $('#tabInfomation2').show();
                        $('#tabInfomation3').show();
                        $('#tabInfomation4').show();
                        $('#tabInfomation5').show();
                        $('#tabInfomation6').show();
                        $('#tabInfomation7').show();
                        $('#tabInfomation8').show();
                        $('#tabInfomation9').show();
                        $('#tabInfomation10').hide();
                        $('#tabInfomation11').show();
                        $('#value500OpIsCust').hide();
                        $('#tabInfomation1').click();
                        goTab('modal-editor-add', 'tabMain');
                    } else { //小於伍佰元
                        $('#tabInfomation1').show();
                        $('#tabInfomation2').show();
                        $('#tabInfomation3').show();
                        $('#tabInfomation4').hide();
                        $('#tabInfomation5').hide();
                        $('#tabInfomation6').hide();
                        $('#tabInfomation7').hide();
                        $('#tabInfomation8').hide();
                        $('#tabInfomation9').hide();
                        $('#tabInfomation10').show();
                        $('#tabInfomation11').hide();
                        $('#value500OpIsCust').show();
                        $('#tabInfomation1').click();
                        goTab('modal-editor-add', 'tabMain');
                    }

                }
            },
            gridComplete: function () {
                $("#gridMainList").setJgridRowCSS();
                $("#gridMainList").setGridWidth($(window).width() - 10);
                $.unblockUI();
                var ids = $(gridId).jqGrid('getDataIDs');
                var logCols = "OP_AC_RCNO=收據編號@OP_AC_UNIT_NM=受理單位@OP_PUOJ_NM=物品名稱@OP_PUPO_NAME=拾得人姓名@OP_PUPO_IDN=身分證其他證號@OP_PU_DTTM=拾得日期時間@OP_PU_PLACE=拾得地點@OP_CURSTAT_NM=目前狀態@OP_PU_YN_OV500=價值伍佰元";
                var logVals = new Array();
                for (var i = 0; i < ids.length; i++) {
                    var id = ids[i];
                    var row = $(gridId).jqGrid('getRowData', id);
                    logVals.push("@OP_AC_RCNO=" + row.OP_AC_RCNO);
                    logVals.push("OP_AC_UNIT_NM=" + row.OP_AC_UNIT_NM);
                    logVals.push("OP_PUOJ_NM=" + row.OP_PUOJ_NM);
                    logVals.push("OP_PUPO_NAME=" + row.OP_PUPO_NAME);
                    logVals.push("OP_PUPO_IDN=" + row.OP_PUPO_IDN);
                    logVals.push("OP_PU_DTTM=" + row.OP_PU_DTTM);
                    logVals.push("OP_PU_PLACE=" + row.OP_PU_PLACE);
                    logVals.push("OP_CURSTAT_NM=" + row.OP_CURSTAT_NM);
                    logVals.push("OP_PU_YN_OV500=" + row.OP_PU_YN_OV500);

                }

                //var searchCriteria = "發生日期開始日期=1080101&發生日期結束日期=1080111";

                if (logVals.length != 0) {
                    var basicForm1 =
                            {'ajaxAction': 'npalog',
                                'logCols': logCols,
                                'logVals': logVals.toString().substring(1),
                                'OP_UNITLEVEL2': $('#OP_Search_unitLevel2').val(),
                                'OP_UNITLEVEL2_NM': $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
                                'OP_UNITLEVEL3': $('#OP_Search_unitLevel3').val(),
                                'OP_UNITLEVEL3_NM': $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text(),
                                'OP_UNITLEVEL4': $('#OP_Search_unitLevel4').val(),
                                'OP_UNITLEVEL4_NM': $('#OP_Search_unitLevel4').val() == '' ? '' : $('#OP_Search_unitLevel4 option:selected').text(),
                                'UNITLEVEL_NM': OPENLLevel_NM,
                                'OP_AC_STAFF_NM': $("#OP_SEARCH_AC_STAFF_NM").val(),
                                'OP_AC_RCNO': $("#OP_SEARCH_AC_RCNO").val(),
                                'OP_PUPO_NAME': $("#OP_SEARCH_PUPO_NAME").val(),
                                'OP_PUPO_GENDER': $("#OP_SEARCH_PUPO_GENDER").val(),
                                'OP_PUPO_GENDER_NM': $('#OP_SEARCH_PUPO_GENDER').val() == '' ? '' : $('#OP_SEARCH_PUPO_GENDER option:selected').text(),
                                'OP_PUPO_IDN_TP': $("#OP_SEARCH_PUPO_IDN_TP").val(),
                                'OP_PUPO_IDN_TP_NM': $('#OP_SEARCH_PUPO_IDN_TP').val() == '' ? '' : $('#OP_SEARCH_PUPO_IDN_TP option:selected').text(),
                                'OP_PUPO_IDN': $("#OP_SEARCH_PUPO_IDN").val(),
                                'OP_PUOJ_NM': $("#OP_SEARCH_PUOJ_NM").val(),
                                'OP_PU_DATE_S': getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                                'OP_PU_DATE_E': getADDate($("#OP_SEARCH_PU_DATE_E").val()),
                                'OP_AC_DATE_S': getADDate($("#OP_SEARCH_AC_DATE_S").val()),
                                'OP_AC_DATE_E': getADDate($("#OP_SEARCH_AC_DATE_E").val()),
                                'OP_PUOJ_ATTR_CD': $('#OP_SEARCH_PUOJ_ATTR_CD').val(),
                                'OP_PUOJ_ATTR_NM': $('#OP_SEARCH_PUOJ_ATTR_CD').val() == '' ? '' : $('#OP_SEARCH_PUOJ_ATTR_CD option:selected').text(),
                                'OP_CURSTAT_CD': $("#OP_SEARCH_CURSTAT_CD").val(),
                                'OP_CURSTAT_NM': $('#OP_SEARCH_CURSTAT_CD').val() == '' ? '' : $('#OP_SEARCH_CURSTAT_CD option:selected').text(),
                                'OP_FS_REC_CD': $("#OP_SEARCH_FS_REC_CD").val(),
                                'OP_FS_REC_NM': $('#OP_SEARCH_FS_REC_CD').val() == '' ? '' : $('#OP_SEARCH_FS_REC_CD option:selected').text(),
                                'OP_PU_YN_OV500': $("#OP_SEARCH_PU_YN_OV500").val(),
                                'OP_RT_STAFF_NM': $("#OP_SEARCH_RT_STAFF_NM").val(),
                                'Page': '拾得遺失物綜合查詢-OP06A01Q_01',
                                'OPR_KIND': appUseList.OPR_KIND,  //202403 //查詢用途代號
                                'OPR_PURP': appUseList.OPR_PURP   //202403 //查詢目的
                            };
                    //                      'npalog', {searchCriteria:searchCriteria,logCols:logCols,logVals:logVals.toString().substring(1),caseTypeList: caseTypeList.toString(), acidTypeList: acidTypeList.toString(), statTypeList: statTypeList.toString(), userType: $('input[name=TM_NAME_GROUP]:checked').val(),dateTimeType:$('input[name=dateTimeType]:checked').val()});

                    $.ajax({
                        url: 'CompositeSearchServlet',
                        type: 'POST',
                        datatype: 'json',
                        data: basicForm1,
                        async: true,
                        success: function (data, textStatus, xhr) {

                        },
                        error: function (jqXHR, textStatus,
                                errorThrown) {
                            notyMsg(textStatus + " " + errorThrown,
                                    'error');
                        }
                    })
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

    //202504
    //console.log ("appUseList.OPR_KIND__"+appUseList.OPR_KIND);      
    //console.log ("appUseList.OPR_PURP__"+appUseList.OPR_PURP);    
    

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
            url: 'CompositeSearchServlet',
            mtype: "POST",
            datatype: "json",
            postData: {
              //ajaxAction: 'CompositeSearchForBasic',        //202504
                ajaxAction: 'CompositeSearchForBasicSearch',  //202504
                OP_UNITLEVEL2: $('#OP_Search_unitLevel2').val(),       //1
                OP_UNITLEVEL2_NM: $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
                OP_UNITLEVEL3: $('#OP_Search_unitLevel3').val(),
                OP_UNITLEVEL3_NM: $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text(),
                OP_UNITLEVEL4: $('#OP_Search_unitLevel4').val(),
                OP_UNITLEVEL4_NM: $('#OP_Search_unitLevel4').val() == '' ? '' : $('#OP_Search_unitLevel4 option:selected').text(),
                UNITLEVEL_NM: OPENLLevel_NM,
                OP_AC_STAFF_NM: $("#OP_SEARCH_AC_STAFF_NM").val(),
                OP_AC_RCNO: $("#OP_SEARCH_AC_RCNO").val(),
                OP_PUPO_NAME: $("#OP_SEARCH_PUPO_NAME").val(),
                OP_PUPO_GENDER: $("#OP_SEARCH_PUPO_GENDER").val(),
                OP_PUPO_GENDER_NM: $('#OP_SEARCH_PUPO_GENDER').val() == '' ? '' : $('#OP_SEARCH_PUPO_GENDER option:selected').text(),
                OP_PUPO_IDN_TP: $("#OP_SEARCH_PUPO_IDN_TP").val(),
                OP_PUPO_IDN_TP_NM: $('#OP_SEARCH_PUPO_IDN_TP').val() == '' ? '' : $('#OP_SEARCH_PUPO_IDN_TP option:selected').text(),
                OP_PUPO_IDN: $("#OP_SEARCH_PUPO_IDN").val(),
                OP_PUOJ_NM: $("#OP_SEARCH_PUOJ_NM").val(),
                OP_PU_DATE_S: getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                OP_PU_DATE_E: getADDate($("#OP_SEARCH_PU_DATE_E").val()),
                OP_AC_DATE_S: getADDate($("#OP_SEARCH_AC_DATE_S").val()),
                OP_AC_DATE_E: getADDate($("#OP_SEARCH_AC_DATE_E").val()),
                OP_PUOJ_ATTR_CD: $('#OP_SEARCH_PUOJ_ATTR_CD').val(),
                OP_PUOJ_ATTR_NM: $('#OP_SEARCH_PUOJ_ATTR_CD').val() == '' ? '' : $('#OP_SEARCH_PUOJ_ATTR_CD option:selected').text(),
                OP_CURSTAT_CD: $("#OP_SEARCH_CURSTAT_CD").val(),
                OP_CURSTAT_NM: $('#OP_SEARCH_CURSTAT_CD').val() == '' ? '' : $('#OP_SEARCH_CURSTAT_CD option:selected').text(),
                OP_FS_REC_CD: $("#OP_SEARCH_FS_REC_CD").val(),
                OP_FS_REC_NM: $('#OP_SEARCH_FS_REC_CD').val() == '' ? '' : $('#OP_SEARCH_FS_REC_CD option:selected').text(),
                OP_PU_YN_OV500: $("#OP_SEARCH_PU_YN_OV500").val(),
                OP_RT_STAFF_NM: $("#OP_SEARCH_RT_STAFF_NM").val(),
                ACTION_TYPE: 'OP06A01Q_01',
                OPR_KIND: appUseList.OPR_KIND,  //202403 查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //202403 查詢目的
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "物品名稱", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "目前狀態", "超過伍佰元", "BASIC_SEQ", "OP_PUPO_PHONENO", "OP_PUPO_BIRTHDT"],
            colModel: [{
                    name: 'OP_AC_RCNO',
                    index: 'OP_AC_RCNO',
                    width: 70
                }, {
                    name: 'OP_AC_UNIT_NM',
                    index: 'OP_AC_UNIT_NM',
                    width: 90
                }, {
                    name: 'OP_PUOJ_NM',
                    index: 'OP_PUOJ_NM',
                    width: 90
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
                    name: 'OP_PU_YN_OV500',
                    index: 'OP_PU_YN_OV500',
                    width: 40
                }, {
                    name: 'BASIC_SEQ',
                    index: 'BASIC_SEQ',
                    hidden: true
                            // 欄位隱藏
                }, {
                    name: 'OP_PUPO_PHONENO',
                    index: 'OP_PUPO_PHONENO',
                    hidden: true
                            // 欄位隱藏
                }, {
                    name: 'OP_PUPO_BIRTHDT',
                    index: 'OP_PUPO_BIRTHDT',
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
                var YnOV500 = row.OP_PU_YN_OV500;
                var OP_PUPO_NAME = row.OP_PUPO_NAME;
                var OP_PUPO_IDN = row.OP_PUPO_IDN;
                var OP_PUPO_PHONENO = row.OP_PUPO_PHONENO;
                var OP_PUPO_BIRTHDT = row.OP_PUPO_BIRTHDT;

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
                    BasicValueForLog2(AcRcno, OP_PUPO_NAME, OP_PUPO_IDN, OP_PUPO_PHONENO, OP_PUPO_BIRTHDT);

                    if (YnOV500 == "是") { //大於伍佰元以上
                        $('#tabInfomation1').show();
                        $('#tabInfomation2').show();
                        $('#tabInfomation3').show();
                        $('#tabInfomation4').show();
                        $('#tabInfomation5').show();
                        $('#tabInfomation6').show();
                        $('#tabInfomation7').show();
                        $('#tabInfomation8').show();
                        $('#tabInfomation9').show();
                        $('#tabInfomation10').hide();
                        $('#tabInfomation11').show();
                        $('#value500OpIsCust').hide();
                        $('#tabInfomation1').click();
                        goTab('modal-editor-add', 'tabMain');
                    } else { //小於伍佰元
                        $('#tabInfomation1').show();
                        $('#tabInfomation2').show();
                        $('#tabInfomation3').show();
                        $('#tabInfomation4').hide();
                        $('#tabInfomation5').hide();
                        $('#tabInfomation6').hide();
                        $('#tabInfomation7').hide();
                        $('#tabInfomation8').hide();
                        $('#tabInfomation9').hide();
                        $('#tabInfomation10').show();
                        $('#tabInfomation11').hide();
                        $('#value500OpIsCust').show();
                        $('#tabInfomation1').click();
                        goTab('modal-editor-add', 'tabMain');
                    }

                }
            },
            gridComplete: function () {
                $("#gridMainList").setJgridRowCSS();
                $("#gridMainList").setGridWidth($(window).width() - 10);
                $.unblockUI();
                var ids = $(gridId).jqGrid('getDataIDs');
                var logCols = "OP_AC_RCNO=收據編號@OP_AC_UNIT_NM=受理單位@OP_PUOJ_NM=物品名稱@OP_PUPO_NAME=拾得人姓名@OP_PUPO_IDN=身分證其他證號@OP_PU_DTTM=拾得日期時間@OP_PU_PLACE=拾得地點@OP_CURSTAT_NM=目前狀態@OP_PU_YN_OV500=價值伍佰元";
                var logVals = new Array();
                for (var i = 0; i < ids.length; i++) {
                    var id = ids[i];
                    var row = $(gridId).jqGrid('getRowData', id);
                    logVals.push("@OP_AC_RCNO=" + row.OP_AC_RCNO);
                    logVals.push("OP_AC_UNIT_NM=" + row.OP_AC_UNIT_NM);
                    logVals.push("OP_PUOJ_NM=" + row.OP_PUOJ_NM);
                    logVals.push("OP_PUPO_NAME=" + row.OP_PUPO_NAME);
                    logVals.push("OP_PUPO_IDN=" + row.OP_PUPO_IDN);
                    logVals.push("OP_PU_DTTM=" + row.OP_PU_DTTM);
                    logVals.push("OP_PU_PLACE=" + row.OP_PU_PLACE);
                    logVals.push("OP_CURSTAT_NM=" + row.OP_CURSTAT_NM);
                    logVals.push("OP_PU_YN_OV500=" + row.OP_PU_YN_OV500);

                }

                //var searchCriteria = "發生日期開始日期=1080101&發生日期結束日期=1080111";

                if (logVals.length != 0) {
                    var basicForm1 =
                            {'ajaxAction': 'npalog',
                                'logCols': logCols,
                                'logVals': logVals.toString().substring(1),
                                'OP_UNITLEVEL2': $('#OP_Search_unitLevel2').val(),
                                'OP_UNITLEVEL2_NM': $('#OP_Search_unitLevel2').val() == '' ? '' : $('#OP_Search_unitLevel2 option:selected').text(),
                                'OP_UNITLEVEL3': $('#OP_Search_unitLevel3').val(),
                                'OP_UNITLEVEL3_NM': $('#OP_Search_unitLevel3').val() == '' ? '' : $('#OP_Search_unitLevel3 option:selected').text(),
                                'OP_UNITLEVEL4': $('#OP_Search_unitLevel4').val(),
                                'OP_UNITLEVEL4_NM': $('#OP_Search_unitLevel4').val() == '' ? '' : $('#OP_Search_unitLevel4 option:selected').text(),
                                'UNITLEVEL_NM': OPENLLevel_NM,
                                'OP_AC_STAFF_NM': $("#OP_SEARCH_AC_STAFF_NM").val(),
                                'OP_AC_RCNO': $("#OP_SEARCH_AC_RCNO").val(),
                                'OP_PUPO_NAME': $("#OP_SEARCH_PUPO_NAME").val(),
                                'OP_PUPO_GENDER': $("#OP_SEARCH_PUPO_GENDER").val(),
                                'OP_PUPO_GENDER_NM': $('#OP_SEARCH_PUPO_GENDER').val() == '' ? '' : $('#OP_SEARCH_PUPO_GENDER option:selected').text(),
                                'OP_PUPO_IDN_TP': $("#OP_SEARCH_PUPO_IDN_TP").val(),
                                'OP_PUPO_IDN_TP_NM': $('#OP_SEARCH_PUPO_IDN_TP').val() == '' ? '' : $('#OP_SEARCH_PUPO_IDN_TP option:selected').text(),
                                'OP_PUPO_IDN': $("#OP_SEARCH_PUPO_IDN").val(),
                                'OP_PUOJ_NM': $("#OP_SEARCH_PUOJ_NM").val(),
                                'OP_PU_DATE_S': getADDate($("#OP_SEARCH_PU_DATE_S").val()),
                                'OP_PU_DATE_E': getADDate($("#OP_SEARCH_PU_DATE_E").val()),
                                'OP_AC_DATE_S': getADDate($("#OP_SEARCH_AC_DATE_S").val()),
                                'OP_AC_DATE_E': getADDate($("#OP_SEARCH_AC_DATE_E").val()),
                                'OP_PUOJ_ATTR_CD': $('#OP_SEARCH_PUOJ_ATTR_CD').val(),
                                'OP_PUOJ_ATTR_NM': $('#OP_SEARCH_PUOJ_ATTR_CD').val() == '' ? '' : $('#OP_SEARCH_PUOJ_ATTR_CD option:selected').text(),
                                'OP_CURSTAT_CD': $("#OP_SEARCH_CURSTAT_CD").val(),
                                'OP_CURSTAT_NM': $('#OP_SEARCH_CURSTAT_CD').val() == '' ? '' : $('#OP_SEARCH_CURSTAT_CD option:selected').text(),
                                'OP_FS_REC_CD': $("#OP_SEARCH_FS_REC_CD").val(),
                                'OP_FS_REC_NM': $('#OP_SEARCH_FS_REC_CD').val() == '' ? '' : $('#OP_SEARCH_FS_REC_CD option:selected').text(),
                                'OP_PU_YN_OV500': $("#OP_SEARCH_PU_YN_OV500").val(),
                                'OP_RT_STAFF_NM': $("#OP_SEARCH_RT_STAFF_NM").val(),
                                'Page': '拾得遺失物綜合查詢-OP06A01Q_01',
                                'OPR_KIND': appUseList.OPR_KIND,  //202403 //查詢用途代號
                                'OPR_PURP': appUseList.OPR_PURP   //202403 //查詢目的
                            };
                    //                      'npalog', {searchCriteria:searchCriteria,logCols:logCols,logVals:logVals.toString().substring(1),caseTypeList: caseTypeList.toString(), acidTypeList: acidTypeList.toString(), statTypeList: statTypeList.toString(), userType: $('input[name=TM_NAME_GROUP]:checked').val(),dateTimeType:$('input[name=dateTimeType]:checked').val()});

                    $.ajax({
                        url: 'CompositeSearchServlet',
                        type: 'POST',
                        datatype: 'json',
                        data: basicForm1,
                        async: true,
                        success: function (data, textStatus, xhr) {

                        },
                        error: function (jqXHR, textStatus,
                                errorThrown) {
                            notyMsg(textStatus + " " + errorThrown,
                                    'error');
                        }
                    })
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
            disableAll('IPUCLAIM');
            $.unblockUI();
        }
    });
}
//設定認領人值---END

//IFINISH_SHOW---START
function getIFnshGridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
        'ajaxAction': 'getIFnshGridList',
        'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            var formData = JData.formData[0];
            if (formData['ACTION_TYPE'] == "insertIFnshList") {
                $('#actionFnshType').val(formData['ACTION_TYPE']);
                $('#IFNSH_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                //結案單位
                $('#OP_FS_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                bindUNITDDL('3', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                $('#OP_FS_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                bindUNITDDL('4', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                $('#OP_FS_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                //結案人員
                $('#IFNSH_OP_FS_STAFF_NM').val(strPermissionData['NAME']);
                //結案日期
                var nowDate = new Date();
                $('#IFNSH_OP_FS_DATE').val(getROCDateSlash(nowDate));
                //發還單位
                $('#OP_RT_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                bindUNITDDL('3', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                $('#OP_RT_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                bindUNITDDL('4', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                $('#OP_RT_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                //發還人員
                bindAuditNameDDL(strPermissionData["UNITLEVEL1"], strPermissionData["UNITLEVEL2"], strPermissionData["UNITLEVEL3"], 'IFNSH_OP_RT_STAFF_ID', '');
//                        $('#IFNSH_OP_RT_STAFF_NM').val(strPermissionData['NAME']);

                $('#IFNSH_OP_RT_STAFF_ID').val(strPermissionData['ID']);
                //發還日期
                $('#IFNSH_OP_RT_DATE').val(getROCDateSlash(nowDate));
                //結案紀錄
                $('#IFNSH_OP_FS_REC_CD').val('');
                //結案情形描述
                $('#IFNSH_OP_FS_STAT_DESC').val('');
            } else {
                $('#actionFnshType').val(formData['ACTION_TYPE']);
                $('#IFNSH_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                //發還人員
                bindAuditNameDDL(strPermissionData["UNITLEVEL1"], strPermissionData["UNITLEVEL2"], strPermissionData["UNITLEVEL3"], 'IFNSH_OP_RT_STAFF_ID', '');
                //結案單位
                var data1 = getunit_code(formData['OP_FS_UNIT_CD']);
                if (data1.result[0]["OP_DEPT_CD"] == data1.result[0]["OP_UNIT_CD"]) {//只有警局
                    $('#OP_FS_unitLevel2').val(data1.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('3', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                } else if (data1.result[0]["OP_UNIT_CD"] == data1.result[0]["OP_BRANCH_CD"] || data1.result[0]["OP_BRANCH_CD"] == '') {//只有警分局
                    $('#OP_FS_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                    $('#OP_FS_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('4', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                } else {
                    $('#OP_FS_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                    $('#OP_FS_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
                    bindUNITDDL('4', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');
                    $('#OP_FS_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
                }
                //結案人員
                $('#IFNSH_OP_FS_STAFF_NM').val(formData['OP_FS_STAFF_NM']);
                //結案日期
                $('#IFNSH_OP_FS_DATE').val(formData['OP_FS_DATE']);
                //發還單位     
                var data2 = getunit_code(formData['OP_RT_UNIT_CD']);
                if (data2.result[0]["OP_DEPT_CD"] == data2.result[0]["OP_UNIT_CD"]) {//只有警局
                    $('#OP_RT_unitLevel2').val(data2.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('3', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    bindAuditNameDDL(data2.result[0]["OP_DEPT_CD"], "", "", 'IFNSH_OP_RT_STAFF_ID', '');
                } else if (data2.result[0]["OP_UNIT_CD"] == data2.result[0]["OP_BRANCH_CD"] || data2.result[0]["OP_BRANCH_CD"] == '') {//只有警分局
                    $('#OP_RT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    $('#OP_RT_unitLevel3').val(data2.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('4', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    bindAuditNameDDL(data2.result[0]["OP_DEPT_CD"], data2.result[0]["OP_BRANCH_CD"], "", 'IFNSH_OP_RT_STAFF_ID', '');
                } else {
                    $('#OP_RT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    $('#OP_RT_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
                    bindUNITDDL('4', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    $('#OP_RT_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
                    bindAuditNameDDL(data2.result[0]["OP_DEPT_CD"], data2.result[0]["OP_BRANCH_CD"], data2.result[0]['OP_UNIT_CD'], 'IFNSH_OP_RT_STAFF_ID', '');
                }
                //發還人員
                $('#IFNSH_OP_RT_STAFF_ID').val(formData['OP_RT_STAFF_ID']);
                //發還日期
                $('#IFNSH_OP_RT_DATE').val(formData['OP_RT_DATE']);
                //結案紀錄
                $('#IFNSH_OP_FS_REC_CD').val(formData['OP_FS_REC_CD']);
                //結案情形描述
                $('#IFNSH_OP_FS_STAT_DESC').val(formData['OP_FS_STAT_DESC']);
            }
            $.unblockUI();
        } else {
            $.alert.open('error', "選取資料失敗!!!");
            $.unblockUI();
        }
    };
    var ajErr = function () {
        $.alert.open('error', "選取資料失敗!!!");
        $.unblockUI();
    };

    $.ajax({
        url: 'AnnounceServlet',
        type: "POST",
        dataType: "json",
        data: ajData,
        success: ajSucc,
        error: ajErr
    });

}
function getIFnshGridListFor500(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
        'ajaxAction': 'getIFnshGridList',
        'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            var formData = JData.formData[0];
            if (formData['ACTION_TYPE'] == "insertIFnshList") {
                $('#actionFnshType').val(formData['ACTION_TYPE']);
                $('#IFNSH_OP_SEQ_NO').val(formData['IFNSH_OP_SEQ_NO']);
                $('#IPUANDL_OP_SEQ_NO').val(formData['IPUANDL_OP_SEQ_NO']);
                //結案單位
                $('#OP_FS2_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                bindUNITDDL('3', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                $('#OP_FS2_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                bindUNITDDL('4', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                $('#OP_FS2_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                //結案人員
                $('#IFNSH2_OP_FS_STAFF_NM').val(strPermissionData['NAME']);
                //結案日期
                var nowDate = new Date();
                $('#IFNSH2_OP_FS_DATE').val(getROCDateSlash(nowDate));
                //通知方式
                $("input[name='IPUANDL2.OP_NTC_MODE'][value='1']").prop("checked", true);
                //送交單位
                $('#OP_SD2_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                bindUNITDDL('3', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                $('#OP_SD2_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                bindUNITDDL('4', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                $('#OP_SD2_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                //送交人員
                $('#IPUANDL2_OP_SD_NAME').val(strPermissionData['NAME']);
                //送交職稱
                $('#IPUANDL2_OP_SD_TITLE').val(strPermissionData['TITLE']);
                //送交日期
                $('#IPUANDL2_OP_SD_DATE').val(getROCDateSlash(nowDate));
                //結案紀錄
                $('#IFNSH2_OP_FS_REC_CD').val('');
                //結案情形描述
                $('#IFNSH2_OP_FS_STAT_DESC').val('');
                //接受之地方自治團體
                $('#IPUANDL2_OP_AC_REG_ATNO').val('');
                //接受人姓名
                $('#IPUANDL2_OP_AC_NAME').val('');
                //接受人職稱
                $('#IPUANDL2_OP_AC_TITLE').val('');

            } else {
                $('#actionFnshType').val(formData['ACTION_TYPE']);
                $('#IFNSH_OP_SEQ_NO').val(formData['IFNSH_OP_SEQ_NO']);
                $('#IPUANDL_OP_SEQ_NO').val(formData['IPUANDL_OP_SEQ_NO']);
                //結案單位
                var data1 = getunit_code(formData['OP_FS_UNIT_CD']);
                if (data1.result[0]["OP_DEPT_CD"] == data1.result[0]["OP_UNIT_CD"]) {//只有警局
                    $('#OP_FS2_unitLevel2').val(data1.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('3', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                } else if (data1.result[0]["OP_UNIT_CD"] == data1.result[0]["OP_BRANCH_CD"] || data1.result[0]["OP_BRANCH_CD"] == '') {//只有警分局
                    $('#OP_FS2_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                    $('#OP_FS2_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('4', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                } else {
                    $('#OP_FS2_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                    $('#OP_FS2_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
                    bindUNITDDL('4', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');
                    $('#OP_FS2_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
                }
                //結案人員
                $('#IFNSH2_OP_FS_STAFF_NM').val(formData['OP_FS_STAFF_NM']);
                //結案日期
                $('#IFNSH2_OP_FS_DATE').val(formData['OP_FS_DATE']);
                //通知方式
                if (formData['OP_NTC_MODE_CD'] == "2") {
                    $("input[name='IPUANDL2.OP_NTC_MODE'][value='2']").prop("checked", true);
                } else if (formData['OP_NTC_MODE_CD'] == "3") {
                    $("input[name='IPUANDL2.OP_NTC_MODE'][value='3']").prop("checked", true);
                } else {
                    $("input[name='IPUANDL2.OP_NTC_MODE'][value='1']").prop("checked", true);
                }
                //送交單位   
                var data2 = getunit_code(formData['OP_SD_UNIT_CD']);
                if (data2.result[0]["OP_DEPT_CD"] == data2.result[0]["OP_UNIT_CD"]) {//只有警局
                    $('#OP_SD2_unitLevel2').val(data2.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('3', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                } else if (data2.result[0]["OP_UNIT_CD"] == data2.result[0]["OP_BRANCH_CD"] || data2.result[0]["OP_BRANCH_CD"] == '') {//只有警分局
                    $('#OP_SD2_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                    $('#OP_SD2_unitLevel3').val(data2.result[0]["OP_UNIT_CD"]);
                    bindUNITDDL('4', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                } else {
                    $('#OP_SD2_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                    $('#OP_SD2_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
                    bindUNITDDL('4', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');
                    $('#OP_SD2_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
                }
                //送交人員
                $('#IPUANDL2_OP_SD_NAME').val(formData['OP_SD_NAME']);
                //送交職稱
                $('#IPUANDL2_OP_SD_TITLE').val(formData['OP_SD_TITLE']);
                //送交日期
                $('#IPUANDL2_OP_SD_DATE').val(formData['OP_SD_DATE']);
                //結案紀錄
                $('#IFNSH2_OP_FS_REC_CD').val(formData['OP_FS_REC_CD']);
                //結案情形描述
                $('#IFNSH2_OP_FS_STAT_DESC').val(formData['OP_FS_STAT_DESC']);
                //接受之地方自治團體
                $('#IPUANDL2_OP_AC_REG_ATNO').val(formData['OP_AC_REG_ATNO']);
                //接受人姓名
                $('#IPUANDL2_OP_AC_NAME').val(formData['OP_AC_NAME']);
                //接受人職稱
                $('#IPUANDL2_OP_AC_TITLE').val(formData['OP_AC_TITLE']);

            }
            $.unblockUI();
        } else {
            $.alert.open('error', "選取資料失敗!!!");
            $.unblockUI();
        }
    };
    var ajErr = function () {
        $.alert.open('error', "選取資料失敗!!!");
        $.unblockUI();
    };

    $.ajax({
        url: 'PuValueUnder500Servlet',
        type: "POST",
        dataType: "json",
        data: ajData,
        success: ajSucc,
        error: ajErr
    });

}
//IFINISH_SHOW---END

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
        FILE_NAME: '受理拾得物案陳報單'
    });

}
//報表列印---END

// 查詢條件 清除條件
function cleanEditor() {
    $('#queryporlet input[type="text"]').val('');
    $('#OP_SEARCH_PUPO_TP_CD').val('');
    $('#OP_SEARCH_PUOJ_ATTR_CD').val('');
    $('#OP_SEARCH_CURSTAT_CD').val('');
    $('#OP_SEARCH_FS_REC_CD').val('');
    $('#OP_SEARCH_PUPO_GENDER').val('');
    $('#OP_SEARCH_PUPO_IDN_TP').val('');
    dateRangeForOneYear('OP_SEARCH_AC_DATE_S', 'OP_SEARCH_AC_DATE_E');
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
}
function clearAnn1Data() {
    $('#IANNOUNCE_OP_SEQ_NO').val('');
    $('#IANNOUNCE_OP_AC_DATE').val('');
    $('#IANNOUNCE_OP_AN_DATE_BEG').val('');
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