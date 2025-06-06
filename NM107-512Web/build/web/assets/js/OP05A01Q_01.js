var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu05').addClass("active");
    $('#menu05span').addClass("selected");
    $('#menu0501').addClass("active");
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
    //受理資料與明細按鈕事件
    BasicAndDetailButton("OP05A01Q_01.jsp");
    //認領人按鈕事件
    ClaimButton("OP05A01Q_01.jsp");
    //內部公告按鈕事件
    Ann1Button();
    //網路公告按鈕事件
    //產生網路公告
    $('#getAnnounceBtn2').click(function () {
        getAnn2GridList_OW($('#IPUBASIC_OP_SEQ_NO').val());
    });
    Ann2Button();
    //招領期滿按鈕事件
    AnDlButton();
    //拾得人公告期滿按鈕事件
    PuAnDlButton();
    //結案資料按鈕事件
    //結案資料儲存
    $('#saveFinishBtn').click(function () {
        if (checkFnshList(document.getElementById('form1'))) {
            confirmFnshAndSave();
        }
    });
    //批次結案
    $('#batchSaveFinishBtn').click(function () {
        confirmBatchSave();
    });
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
    //取得戶籍資料
    $('#IPUBASIC_GET_HH_DATA').click(function () {
        Validator.init(document.getElementById('form1'));
        //證號
        if ($("#IPUBASIC_OP_PUPO_NAT_CD").val() == "035" && $("#IPUBASIC_OP_PUPO_IDN_TP").val() == "2") {
            Validator.setMessage("欄位 [ 證號 ] 為\"其他證號\"時，[ 國籍 ]不得為中華民國國籍 !!");
            Validator.setBGColor("IPUBASIC_OP_PUPO_NAT_CD", "IPUBASIC_OP_PUPO_IDN_TP");
        } else if ($("#IPUBASIC_OP_PUPO_NAT_CD").val() != "" && $("#IPUBASIC_OP_PUPO_NAT_CD").val() != "035" && $("#IPUBASIC_OP_PUPO_IDN_TP").val() == "1") {
            Validator.setMessage("欄位 [ 證號 ] 為\"身分證號\"時，[ 國籍 ]不得為他國國籍 !!");
            Validator.setBGColor("IPUBASIC_OP_PUPO_NAT_CD", "IPUBASIC_OP_PUPO_IDN_TP");
        } else if ($("#IPUBASIC_OP_PUPO_IDN_TP").val() == "1") {
            Validator.checkID("IPUBASIC_OP_PUPO_IDN", true, "身分證號");
        } else if ($("#IPUBASIC_OP_PUPO_IDN_TP").val() == "3") {
            Validator.checkLength("IPUBASIC_OP_PUPO_IDN", false, "身分證號", 20);
        } else {
            Validator.checkLength("IPUBASIC_OP_PUPO_IDN", true, "身分證號", 20);
        }
        if (Validator.isValid())
            getOI2PersonByIDN("BASIC");
        else {
            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
            return false;
        }
    });
    //發還單位改變
    $('#OP_RT_unitLevel3').change(function () {
        bindAuditNameDDL($('#OP_RT_unitLevel2').val(), $('#OP_RT_unitLevel3').val(), $('#OP_RT_unitLevel4').val(), 'IFNSH_OP_RT_STAFF_ID', '');
    });
    $('#OP_RT_unitLevel4').change(function () {
        bindAuditNameDDL($('#OP_RT_unitLevel2').val(), $('#OP_RT_unitLevel3').val(), $('#OP_RT_unitLevel4').val(), 'IFNSH_OP_RT_STAFF_ID', '');
    });
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
            showDetailGridList_OW($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUDETAIL_OP_AC_RCNO').val());
        } else {
            $('#tab_4_2_alert').show();
            $('#tab_4_2_main').hide();
        }
    });
    //分頁3 民眾認領
    $('#tabInfomation3').click(function () {
        //只是判斷要不要顯示
        clearClaimData();
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
            $("#tab_4_4_alert").hide();
            $("#tab_4_4_main").show();
            $("#tab_4_4_main2").show();
            if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                $("#addClaimBtn").show();
                $("#clearClaimBtn").show();
                $("#editClaimBtn").hide();
                $("#deleteClaimBtn").hide();
                undisableAll('IPUCLAIM');
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
    //分頁4 內部公告
    $('#tabInfomation4').click(function () {
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
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
            getAnn1GridList($('#IPUBASIC_OP_SEQ_NO').val(), "OP05A01Q");
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
                if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                    $('#getAnnounceBtn2').show();
                    $('#saveAnnounceBtn2').hide();
                    $('#photo3SubmitBtn').prop('disabled', false);
                    $('#photo3DeleteBtn').prop('disabled', false);
                    $('#photo4SubmitBtn').prop('disabled', false);
                    $('#photo4DeleteBtn').prop('disabled', false);
                }
            } else if (check == '2') { //網路公告 2
                $('#getAnnounceBtn2').hide();
                $('#saveAnnounceBtn2').hide();
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
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
            $('#tab_4_7_main').show();
            $('#tab_4_7_alert').hide();
            getIAnDlGridList($('#IPUBASIC_OP_SEQ_NO').val(), "OP05A01Q"); //顯示畫面資料
        } else {
            $('#saveIAnDlBtn').hide();
            $('#tab_4_7_main').hide();
            $('#tab_4_7_alert').show();
        }
    });
    //分頁7 拾得人領回公告
    $('#tabInfomation7').click(function () {
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
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
        if (checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val()) == 'Y') {
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
    //分頁10 報表列印
    $('#tabInfomation10').click(function () {
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
                if (reportType == "oP02A07Q.doc") { //領取拾得物領據
                    downloadReport("ReportServlet", {
                        ajaxAction: 'OP02A07Q.doc',
                        reportName: 'oP02A07Q.doc,oP02A07Q_1.doc',
                        newReportName: '領取拾得物領據.doc,領取拾得物領據.doc',
                        OP_BASIC_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                        CLAIM_TYPE: claimType,
                        OP_SEQ_NO: SEQ,
                        lockType: 'A'
                    });
                }
                if (reportType == "oP02A11Q.doc") { //拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書
                    downloadReport("ReportServlet", {
                        ajaxAction: 'OP02A11Q.doc',
                        reportName: 'oP02A11Q.doc,oP02A12Q.doc',
                        newReportName: '拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc,拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc',
                        OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
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
    $("#OP_SEARCH_CURSTAT_CD").val('5');
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

    if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) { //警局承辦人
        //受理按鈕顯示
        $('#saveBasicBtn').show();
        $('#IPUBASIC_OP_CURSTAT_CD').prop('disabled', false);
        //明細顯示
        $("#addDetailBtn").show();
        $("#clearDetailBtn").show();
        //認領人
        $("#addClaimBtn").show();
        $("#clearClaimBtn").show();
        $("#editClaimBtn").hide();
        $("#deleteClaimBtn").hide();
        //內部公告
        //$('#saveAnnounceBtn').show();
        disableAll('IANNOUNCE'); //內部公告
        //網路公告
        disableAll('IANNOUNCE2');
        //招領期滿處理
        //$('#saveIAnDlBtn').show();
        //undisableAll('IANDL');
        disableAll('IANDL'); //招領期滿處理
        disableAll('IPUANANN'); //拾得人領回公告
        //$('#saveIPuanDlBtn').show();
        //undisableAll('IPUANDL'); //拾得人領回公告期滿
        disableAll('IPUANDL'); //拾得人領回公告期滿
    } else {
        $('#saveBasicBtn').hide();
        //明細顯示
        $("#addDetailBtn").hide();
        $("#clearDetailBtn").hide();
        //認領人
        $("#addClaimBtn").hide();
        $("#clearClaimBtn").hide();
        $("#editClaimBtn").hide();
        $("#deleteClaimBtn").hide();
        //內部公告
        $('#saveAnnounceBtn').hide();
        disableAll('IPUBASIC'); //受理資料
        disableAll('IPUDETAIL'); //明細資料
        disableAll('IPUCLAIM'); //認領資料
        disableAll('IANNOUNCE'); //內部公告
        disableAll('IANNOUNCE2'); //網路公告
        disableAll('IANDL'); //招領期滿處理
        disableAll('IPUANANN'); //拾得人領回公告
        disableAll('IPUANDL'); //拾得人領回公告期滿

    }

    // 先帶出查詢結果
    if (checkList(document.getElementById('form1')))
        showGridListbyQS();
}

var ajaxActionUrl = 'PuMaintainServlet';

//BASIC_SHOW---START
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
                OP_PU_YN_OV500: '1',
                OP_DEL_FLAG: '0',
                ACTION_TYPE: 'OP05A01Q_01',
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
            multiselect: true,
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
                    $('#IPUCLAIM_OP_AC_RCNO').val(AcRcno);
                    $('[id*="tabInfomation"]').removeClass('active');
                    $('[id*="tab_4_"]').removeClass('active');
                    $('#tabInfomation9').addClass('active');
                    $('#tab_4_9').addClass('active');

                    $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                    $('#checkYNDetail').val(checkYNDetail(BasicNo));
                    BasicValueForLog(BasicNo);
                    //$('#tabInfomation5').click();
                    $('#tabInfomation9').click();
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
                OP_PU_YN_OV500: '1',
                OP_DEL_FLAG: '0',
                ACTION_TYPE: 'OP05A01Q_01',
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
            multiselect: true,
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
                    $('#IPUCLAIM_OP_AC_RCNO').val(AcRcno);
                    $('[id*="tabInfomation"]').removeClass('active');
                    $('[id*="tab_4_"]').removeClass('active');
                    $('#tabInfomation9').addClass('active');
                    $('#tab_4_9').addClass('active');

                    $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                    $('#checkYNDetail').val(checkYNDetail(BasicNo));
                    BasicValueForLog(BasicNo);
                    //$('#tabInfomation5').click();
                    $('#tabInfomation9').click();
                    goTab('modal-editor-add', 'tabMain');
                }
            }
        });
        $(gridId).jqGrid("clearGridData", true);
        $(gridId).setGridWidth($(window).width() - 10);
        $(gridId).trigger("reloadGrid");
    }
}
//DETAIL_SHOW---START
function showDetailGridList_OW(OP_BASIC_SEQ_NO, OP_AC_RCNO) {
    var gridId = "#gridClueList";
    var pagerId = "#pagerClueList";
    show_BlockUI_page_noParent('資料準備中…');
    let appUseList = getAppUse();   //202403		
    $(gridId).jqGrid('GridUnload');
    $(gridId).jqGrid({
        url: ajaxActionUrl,
        mtype: "POST",
        datatype: "json",
        postData: {
            ajaxAction: 'GetDetailList',
            'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
            'OP_AC_RCNO': OP_AC_RCNO,
            'OPR_KIND': appUseList.OPR_KIND,  //202403 查詢用途代號
            'OPR_PURP': appUseList.OPR_PURP   //202403 查詢目的
        },
        height: "auto",
        autowidth: true,
        colNames: ["收據編號", "物品名稱", "數量", "單位", "特徵", "OP_SEQ_NO"],
        colModel: [{
                name: 'OP_AC_RCNO',
                index: 'OP_AC_RCNO',
                width: 80
            }, {
                name: 'OP_PUOJ_NM',
                index: 'OP_PUOJ_NM',
                width: 30
            }, {
                name: 'OP_QTY',
                index: 'OP_QTY',
                width: 30
            }, {
                name: 'OP_QTY_UNIT',
                index: 'OP_QTY_UNIT',
                width: 30
            }, {
                name: 'OP_FEATURE',
                index: 'OP_FEATURE',
                width: 150
            }, {
                name: 'OP_SEQ_NO',
                index: 'OP_SEQ_NO',
                hidden: true
                        // 欄位隱藏
            }],
        sortname: 'OP_PUOJ_NM',
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
            var count = $(gridId).jqGrid("getGridParam", "records");//取得此次查詢的總筆數
            if (count > 0) {
                $("#tab_4_3_1_alert").hide();
                $("#tab_4_3_2_alert").hide();
                $("#tab_4_3_main").show();
            } else {
                $("#tab_4_3_1_alert").hide();
                $("#tab_4_3_2_alert").show();
                $("#tab_4_3_main").hide();
            }
            $.unblockUI();
        },
        onCellSelect: function (row, col, content, event) {
            var cm = jQuery(gridId).jqGrid("getGridParam", "colModel");
            selectedColumnName = cm[col].name;
        },
        onSelectRow: function (id, status) {
            var row = $(gridId).jqGrid('getRowData', id);
            var OP_SEQ_NO = row.OP_SEQ_NO;
            showDetailValue_OW(OP_SEQ_NO);
        }
    });
    $(gridId).jqGrid("clearGridData", true);
    $(gridId).setGridWidth($(window).width() - 92);
    $(gridId).trigger("reloadGrid");
}
function showDetailValue_OW(OP_SEQ_NO) {
    clearDetailData();
    show_BlockUI_page_noParent('資料選取中…');
    let appUseList = getAppUse();   //202403	

    var ajData = {
        'ajaxAction': 'showDetailValue',
        'OP_SEQ_NO': OP_SEQ_NO,
        'OPR_KIND': appUseList.OPR_KIND,  //202403 查詢用途代號
        'OPR_PURP': appUseList.OPR_PURP   //202403 查詢目的
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            var formData = JData.formData[0];
            $('#IPUDETAIL_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
            $('#IPUDETAIL_OP_TYPE_CD').val(formData['OP_TYPE_CD']);
            $("#IPUDETAIL_OP_TYPE_CD").children().each(function () {
                if ($(this).val() == formData['OP_TYPE_CD']) {
                    $(this).prop("selected", true);
                }
            });
            $('#IPUDETAIL_OP_TYPE_CD').multipleSelect("refresh");
            $('#IPUDETAIL_OP_PUOJ_NM').val(formData['OP_PUOJ_NM']);
            $('#IPUDETAIL_OP_QTY').val(formData['OP_QTY']);
            $('#IPUDETAIL_OP_QTY_UNIT').val(formData['OP_QTY_UNIT']);
            $('#IPUDETAIL_OP_QTY_UNIT_CHOOSE').val(formData['OP_QTY_UNIT']);
            $('#IPUDETAIL_OP_COLOR_CD').val(formData['OP_COLOR_CD']);
            var type = formData['OP_TYPE_CD'];
            var typeName = formData['OP_TYPE_NM'];

            var listNew = checkDate($('#IPUBASIC_OP_SEQ_NO').val());
            if (listNew == true) {
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線          
//                if (type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
//                        || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") {
//                    $("#IPUDETAIL_OP_MAC_ADDR").show();
//                    $("#MACLabel").show();
//                    $("#IPUDETAIL_OP_MAC_ADDR").val(formData['OP_MAC_ADDR']);
//                    $("#GET_E_MAC_DATA").show();
//                    bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
//                } else {
//                    $("#MACLabel").hide();
//                    $("#IPUDETAIL_OP_MAC_ADDR").hide();
//                    $("#GET_E_MAC_DATA").hide();
//                }
                if (type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
                        || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") {
                    bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                    }
                if (typeName.indexOf('行動電話') >= 0 || typeName.indexOf('無線電話') >= 0 || typeName.indexOf('手機') >= 0) {
//                    $("#IMEILabel").show();
//                    $("#IMEILabel2").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE").val(formData['OP_IMEI_CODE']);
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").val(formData['OP_IMEI_CODE_2']);
//                    $("#GET_E_IMEI_DATA").show();
                    $("#PhoneLabel").show();
                    $("#PhoneLabel2").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").val(formData['OP_PHONE_NUM']);
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").val(formData['OP_PHONE_NUM_2']);
                    $("#GET_E_PHONE_DATA").show();
                    bindE8DATATYPE('A15', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                } else {
//                    $("#IPUDETAIL_OP_IMEI_CODE").hide();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
//                    $("#IMEILabel").hide();
//                    $("#IMEILabel2").hide();
//                    $("#IPUDETAIL_OP_IMEI_CODE").hide();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
//                    $("#GET_E_IMEI_DATA").hide();
                    $("#PhoneLabel").hide();
                    $("#PhoneLabel2").hide();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").hide();
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").hide();
                    $("#GET_E_PHONE_DATA").hide();
                }
                if (!(type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
                        || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") &&
                        !(typeName.indexOf('行動電話') >= 0 || typeName.indexOf('無線電話') >= 0 || typeName.indexOf('手機') >= 0)) {
                    $('#IPUDETAIL_OP_BRAND_CD').empty();
                    $('#IPUDETAIL_OP_BRAND_CD').append("<option value=''>無資料</option>");
                }
                if (formData['OP_BRAND_CD'] == '') {
                    $('#IPUDETAIL_OP_BRAND_CD').empty();
                    $('#IPUDETAIL_OP_BRAND_CD').append("<option value=''>無資料</option>");
                } else {
                    $('#IPUDETAIL_OP_BRAND_CD').val(formData['OP_BRAND_CD']);
                }
            } else {
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線          
//                if (type == 'C003' || type == 'C001') {
//                    $("#IPUDETAIL_OP_MAC_ADDR").show();
//                    $("#GET_E_MAC_DATA").show();
//                    $("#MACLabel").show();
//                    bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
//                } else {
//                    $("#MACLabel").hide();
//                    $("#IPUDETAIL_OP_MAC_ADDR").hide();
//                    $("#GET_E_MAC_DATA").hide();
//                }
//                
                  if (type == 'C003' || type == 'C001') {
                      bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                  }

                if (typeName.indexOf('手機') >= 0) {
//                    $("#IMEILabel").show();
//                    $("#IMEILabel2").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE").val(formData['OP_IMEI_CODE']);
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").val(formData['OP_IMEI_CODE_2']);
//                    $("#GET_E_IMEI_DATA").show();
                    $("#PhoneLabel").show();
                    $("#PhoneLabel2").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").val(formData['OP_PHONE_NUM']);
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").val(formData['OP_PHONE_NUM_2']);
                    $("#GET_E_PHONE_DATA").show();
                    bindE8DATATYPE('A15', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                } else {
//                    $("#IMEILabel").hide();
//                    $("#IMEILabel2").hide();
//                    $("#IPUDETAIL_OP_IMEI_CODE").hide();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
//                    $("#GET_E_IMEI_DATA").hide();
                    $("#PhoneLabel").hide();
                    $("#PhoneLabel2").hide();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").hide();
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").hide();
                    $("#GET_E_PHONE_DATA").hide();
                    $('#IPUDETAIL_OP_BRAND_CD').empty();
                    $('#IPUDETAIL_OP_BRAND_CD').append("<option value=''>無資料</option>");
                }
                if (formData['OP_BRAND_CD'] == '') {
                    $('#IPUDETAIL_OP_BRAND_CD').empty();
                    $('#IPUDETAIL_OP_BRAND_CD').append("<option value=''>無資料</option>");
                } else {
                    $('#IPUDETAIL_OP_BRAND_CD').val(formData['OP_BRAND_CD']);
                }
            }

            $('#IPUDETAIL_OP_FEATURE').val(formData['OP_FEATURE']);
            $('#IPUDETAIL_OP_REMARK').val(formData['OP_REMARK']);
            if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                $('#addDetailBtn').hide();
                $('#editDetailBtn').show();
                $('#deleteDetailBtn').show();
            }
            getPicture(formData['OP_SEQ_NO']);
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
        url: ajaxActionUrl,
        type: "POST",
        dataType: "json",
        data: ajData,
        success: ajSucc,
        error: ajErr
    });

}
//DETAIL_SHOW---END

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
            if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                if (TABLEFROM == "Claim") {
                    $("#addClaimBtn").show();
                    $("#clearClaimBtn").show();
                    $("#editClaimBtn").show();
                    $("#deleteClaimBtn").show();
                    undisableAll('IPUCLAIM');
                } else {
                    disableAll('IPUCLAIM');
                    $("#addClaimBtn").hide();
                    $("#clearClaimBtn").show();
                    $("#editClaimBtn").show();
                    $("#deleteClaimBtn").hide();
                    $('#IPUCLAIM_OP_YN_LOSER').prop('disabled', false);
                    $('#IPUCLAIM_OP_REMARK').prop('disabled', false);
                }
            }
            $.unblockUI();
        }
    });
}
//設定認領人值---END

//ANN2_SHOW---START
function getAnn2GridList_OW(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
        'ajaxAction': 'getAnn2GridList',
        'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            var formData = JData.formData[0];
            $('#IANNOUNCE_OP_SEQ_NO2').val(formData['OP_SEQ_NO']);
            $('#OP_AC2_unitLevel2').val(formData['OP_AC_D_UNIT_CD']);
            bindUNITDDL('3', 'OP_AC2_unitLevel2', 'OP_AC2_unitLevel3', 'OP_AC2_unitLevel4', '');
            $('#OP_AC2_unitLevel3').val(formData['OP_AC_B_UNIT_CD']);
            bindUNITDDL('4', 'OP_AC2_unitLevel2', 'OP_AC2_unitLevel3', 'OP_AC2_unitLevel4', '');
            $('#OP_AC2_unitLevel4').val(formData['OP_AC_UNIT_CD']);
            $('#IANNOUNCE_OP_AC_DATE2').val(formData['OP_AC_DATE']);
            $('#IPUBASIC_OP_AC_UNIT_TEL2').val(formData['OP_AC_UNIT_TEL']);

            $('#IANNOUNCE_OP_DOC_WD2').val(formData['OP_DOC_WD']);
            $('#IANNOUNCE_OP_DOC_NO2').val(formData['OP_DOC_NO']);
            $('#IANNOUNCE_OP_AN_DATE_BEG2').val(formData['OP_AN_DATE_BEG']);
            $('#IANNOUNCE_OP_AN_CONTENT2').val(formData['OP_AN_CONTENT']);
            $('#IANNOUNCE_OP_AC_UNIT_ALL').val(formData['OP_AN_B_UNIT_NM']); //先暫時到分局
            $('#IPUBASIC_OP_PU_DATE2').val(formData['OP_PU_DATE']);
            $('#IPUBASIC_OP_PU_TIME2').val(formData['OP_PU_TIME']);
            $('#IPUBASIC_OP_PU_CITY_CD2').val(formData['OP_PU_CITY_CD']);
            bindCOUNTRYDDL('3', 'IPUBASIC_OP_PU_CITY_CD2', 'IPUBASIC_OP_PU_TOWN_CD2', '', '', '');
            $('#IPUBASIC_OP_PU_TOWN_CD2').val(formData['OP_PU_TOWN_CD']);
            $('#IPUBASIC_OP_PU_PLACE2').val(formData['OP_PU_PLACE']);
            $('#IANNOUNCE_OP_AN_DATE_END2').text(formData['OP_AN_DATE_END']);
            $('#saveAnnounceBtn2').show();
            getPictureForAnn(formData['OP_SEQ_NO']);
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
//ANN2_SHOW---END

//IFINISH_SAVE---START
function confirmFnshAndSave() {
    var bool = true;
    if (bool) {
        $.alert.open({
            type: 'confirm',
            content: '資料結案中，確定是否要結案？結案時，其餘未審核之有認領權人更改為否並寄認領結果通知書。',
            callback: function (button, value) {
                if (button == 'yes')
                    saveFnsh();

            }
        });
    }
}

function saveFnsh() {
    var bool = true;

    var OPENLLevel_NM;
    var RT_OPENLLevel_NM;
    //結案單位
    if ($('#OP_FS_unitLevel4').val() != '') {
        OPENLLevel_NM = $('#OP_FS_unitLevel2 option:selected').text();
        OPENLLevel_NM += $('#OP_FS_unitLevel3 option:selected').text();
        OPENLLevel_NM += $('#OP_FS_unitLevel4 option:selected').text();
    } else if ($('#OP_FS_unitLevel3').val() != '') {
        OPENLLevel_NM = $('#OP_FS_unitLevel2 option:selected').text();
        OPENLLevel_NM += $('#OP_FS_unitLevel3 option:selected').text();
    } else {
        OPENLLevel_NM = $('#OP_FS_unitLevel2 option:selected').text();
    }
    //發還單位
    if ($('#OP_RT_unitLevel4').val() != '') {
        RT_OPENLLevel_NM = $('#OP_RT_unitLevel2 option:selected').text();
        RT_OPENLLevel_NM += $('#OP_RT_unitLevel3 option:selected').text();
        RT_OPENLLevel_NM += $('#OP_RT_unitLevel4 option:selected').text();
    } else if ($('#OP_RT_unitLevel3').val() != '') {
        RT_OPENLLevel_NM = $('#OP_RT_unitLevel2 option:selected').text();
        RT_OPENLLevel_NM += $('#OP_RT_unitLevel3 option:selected').text();
    } else {
        RT_OPENLLevel_NM = $('#OP_RT_unitLevel2 option:selected').text();
    }

    if (bool) {
        show_BlockUI_page_noParent('資料儲存中…');
        var ajData = {
            'ajaxAction': $('#actionFnshType').val(),
            'OP_SEQ_NO': $('#IFNSH_OP_SEQ_NO').val(),
            'OP_BASIC_SEQ_NO': $('#IPUBASIC_OP_SEQ_NO').val(),
            'OP_AC_RCNO': $('#IPUBASIC_OP_AC_RCNO').val(),
            'OP_FS_UNITLEVEL2': $('#OP_FS_unitLevel2').val(),
            'OP_FS_UNITLEVEL2_NM': $('#OP_FS_unitLevel2').val() == '' ? '' : $('#OP_FS_unitLevel2 option:selected').text(),
            'OP_FS_UNITLEVEL3': $('#OP_FS_unitLevel3').val(),
            'OP_FS_UNITLEVEL3_NM': $('#OP_FS_unitLevel3').val() == '' ? '' : $('#OP_FS_unitLevel3 option:selected').text(),
            'OP_FS_UNITLEVEL4': $('#OP_FS_unitLevel4').val(),
            'OP_FS_UNITLEVEL4_NM': $('#OP_FS_unitLevel4').val() == '' ? '' : $('#OP_FS_unitLevel4 option:selected').text(),
            'OP_FS_UNITLEVEL_NM': OPENLLevel_NM,
            'OP_FS_STAFF_NM': $('#IFNSH_OP_FS_STAFF_NM').val(),
            'OP_FS_DATE': $('#IFNSH_OP_FS_DATE').val(),
            'OP_RT_UNITLEVEL2': $('#OP_RT_unitLevel2').val(),
            'OP_RT_UNITLEVEL2_NM': $('#OP_RT_unitLevel2').val() == '' ? '' : $('#OP_RT_unitLevel2 option:selected').text(),
            'OP_RT_UNITLEVEL3': $('#OP_RT_unitLevel3').val(),
            'OP_RT_UNITLEVEL3_NM': $('#OP_RT_unitLevel3').val() == '' ? '' : $('#OP_RT_unitLevel3 option:selected').text(),
            'OP_RT_UNITLEVEL4': $('#OP_RT_unitLevel4').val(),
            'OP_RT_UNITLEVEL4_NM': $('#OP_RT_unitLevel4').val() == '' ? '' : $('#OP_RT_unitLevel4 option:selected').text(),
            'OP_RT_UNITLEVEL_NM': RT_OPENLLevel_NM,
            'OP_RT_STAFF_ID': $('#IFNSH_OP_RT_STAFF_ID').val(),
            'OP_RT_STAFF_NM': $('#IFNSH_OP_RT_STAFF_ID').val() == '' ? '' : $('#IFNSH_OP_RT_STAFF_ID option:selected').text(),
            'OP_FS_REC_CD': $('#IFNSH_OP_FS_REC_CD').val(),
            'OP_FS_REC_NM': $('#IFNSH_OP_FS_REC_CD').val() == '' ? '' : $('#IFNSH_OP_FS_REC_CD option:selected').text(),
            'OP_FS_STAT_DESC': $('#IFNSH_OP_FS_STAT_DESC').val(),
            'OP_RT_DATE': $('#IFNSH_OP_RT_DATE').val(),
            'ACTION_FROM': 'IFNSH'
        };
        var ajSucc = function (JData) {
            if (JData.formData) {
                var formData = JData.formData[0];
                if ($('#actionFnshType').val() == 'insertIFnshList') {
                    notyMsg('儲存成功');
                    $('#actionFnshType').val('updateIFnshList');
                    $('#IFNSH_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                    if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                        undisableAll('IFNSH');
                        $('#saveFinishBtn').show();
                    } else {
                        disableAll('IFNSH'); //如果有資料就不能修改
                        $('#saveFinishBtn').hide();
                    }
                } else if ($('#actionFnshType').val() == 'updateIFnshList') {
                    notyMsg('儲存成功');
                    $('#actionFnshType').val('updateIFnshList');
                    $('#IFNSH_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                    if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                        undisableAll('IFNSH');
                        $('#saveFinishBtn').show();
                    } else {
                        disableAll('IFNSH'); //如果有資料就不能修改
                        $('#saveFinishBtn').hide();
                    }
                }
            } else {
                $('#actionFnshType').val() == 'insertIFnshList' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
            }
            $.unblockUI();
        };
        var ajErr = function () {
            $('#actionFnshType').val() == 'insertIFnshList' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
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
}
//IFINISH_SAVE---END
//批次結案---START
function confirmBatchSave() {
    var bool = true;
    //拾得日期
    if ($('#batchSaveFinishReason').val() == "") {
        $.alert.open('error', '請選擇結案紀錄');
        bool = false;
    }
    if (bool) {
        $.alert.open({
            type: 'confirm',
            content: '資料結案中，確定是否要結案？結案時，其餘未審核之有認領權人更改為否並寄認領結果通知書。',
            callback: function (button, value) {
                if (button == 'yes')
                    SaveFinishBtn();

            }
        });
    }
}
function SaveFinishBtn() {
    var bool = true;
    var $myGrid = $('#gridMainList');
    var indexInJqgrid = $myGrid.jqGrid('getGridParam', 'selarrrow');
    var saveIDs = "";
    var saveAcRcno = "";
    let appUseList = getAppUse();   //202403		

    if (indexInJqgrid.length == 0) {
        $.alert.open('error', '請勾選想結案的資料');
        bool = false;
    }
    for (var i = 0; i < indexInJqgrid.length; i++) {
        var selRowId = indexInJqgrid[i];
        var selTableCurstat = $myGrid.jqGrid('getCell', selRowId, 'OP_CURSTAT_NM');
        if (selTableCurstat == "已結案") {
            $.alert.open('error', '不可結案已結案的資料');
            bool = false;
        }
    }
    if (bool) {
        for (var i = 0; i < indexInJqgrid.length; i++) {
            var selRowId = indexInJqgrid[i];
            var selTableID = $myGrid.jqGrid('getCell', selRowId, 'BASIC_SEQ');
            var selTableAcRcno = $myGrid.jqGrid('getCell', selRowId, 'OP_AC_RCNO');
            saveIDs = saveIDs.concat(',', selTableID);
            saveAcRcno = saveAcRcno.concat(',', selTableAcRcno);
        }
        saveIDs = saveIDs.substring(1);
        saveAcRcno = saveAcRcno.substring(1);
        $.ajax({
            url: 'AnnounceServlet',
            type: "POST",
            dataType: "json",
            data: {
                ajaxAction: 'batchSaveFinish',
                OP_SEQ_NO: saveIDs,
                OP_AC_RCNO: saveAcRcno,
                OP_FS_REC_CD: $('#batchSaveFinishReason').val(),
                OP_FS_REC_NM: $('#batchSaveFinishReason').val() == '' ? '' : $('#batchSaveFinishReason option:selected').text(),
                OP_FS_STAT_DESC:$('#OP_FS_STAT_DESC').val(),
                OPR_KIND: appUseList.OPR_KIND,  //202403 查詢用途代號
                OPR_PURP: appUseList.OPR_PURP   //202403 查詢目的
            },
            clearForm: true,
            resetForm: true,
            success: function (data, textStatus, xhr, $form) {
                notyMsg('已結案' + data.formData + ' 筆資料');
                showGridListbyQS();
            }
        });
    }
}
//批次結案---END

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
                //根據登入的權限決定開放多少層
                if (strPermissionData["RolePermission"] == '1') {
                    $("#OP_RT_unitLevel2").prop('disabled', true);
                    $("#OP_RT_unitLevel3").prop('disabled', true);
                    $("#OP_RT_unitLevel4").prop('disabled', true);
                } else if (strPermissionData["RolePermission"] == '2') {
                    $("#OP_RT_unitLevel2").prop('disabled', true);
                    $("#OP_RT_unitLevel3").prop('disabled', true);
                    $("#OP_RT_unitLevel4").prop('disabled', false);
                } else if (strPermissionData["RolePermission"] == '3') {
                    $("#OP_RT_unitLevel2").prop('disabled', true);
                    $("#OP_RT_unitLevel3").prop('disabled', false);
                    $("#OP_RT_unitLevel4").prop('disabled', false);
                }

//                        $('#IFNSH_OP_RT_STAFF_NM').val(strPermissionData['NAME']);
                //發還人員
                $('#IFNSH_OP_RT_STAFF_ID').val(strPermissionData['ID']);
                //發還日期
                $('#IFNSH_OP_RT_DATE').val(getROCDateSlash(nowDate));
                //結案紀錄
                $('#IFNSH_OP_FS_REC_CD').val('01');
                //結案情形描述
                $('#IFNSH_OP_FS_STAT_DESC').val('');
                undisableAll('IFNSH');
                $('#saveFinishBtn').show();
            } else {
                $('#actionFnshType').val(formData['ACTION_TYPE']);
                $('#IFNSH_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                //發還人員
//                        bindAuditNameDDL(strPermissionData["UNITLEVEL1"],strPermissionData["UNITLEVEL2"],strPermissionData["UNITLEVEL3"],'IFNSH_OP_RT_STAFF_ID','');
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
                    bindAuditNameDDL(data2.result[0]["OP_DEPT_CD"], data2.result[0]["OP_UNIT_CD"], "", 'IFNSH_OP_RT_STAFF_ID', '');
                } else {
                    $('#OP_RT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                    bindUNITDDL('3', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    $('#OP_RT_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
                    bindUNITDDL('4', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');
                    $('#OP_RT_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
                    bindAuditNameDDL(data2.result[0]["OP_DEPT_CD"], data2.result[0]["OP_BRANCH_CD"], data2.result[0]['OP_UNIT_CD'], 'IFNSH_OP_RT_STAFF_ID', '');
                }
                //根據登入的權限決定開放多少層
                if (strPermissionData["RolePermission"] == '1') {
                    $("#OP_RT_unitLevel2").prop('disabled', true);
                    $("#OP_RT_unitLevel3").prop('disabled', true);
                    $("#OP_RT_unitLevel4").prop('disabled', true);
                } else if (strPermissionData["RolePermission"] == '2') {
                    $("#OP_RT_unitLevel2").prop('disabled', true);
                    $("#OP_RT_unitLevel3").prop('disabled', true);
                    $("#OP_RT_unitLevel4").prop('disabled', false);
                } else if (strPermissionData["RolePermission"] == '3') {
                    $("#OP_RT_unitLevel2").prop('disabled', true);
                    $("#OP_RT_unitLevel3").prop('disabled', false);
                    $("#OP_RT_unitLevel4").prop('disabled', false);
                }

                //發還人員
                $('#IFNSH_OP_RT_STAFF_ID').val(formData['OP_RT_STAFF_ID']);
                //發還日期
                $('#IFNSH_OP_RT_DATE').val(formData['OP_RT_DATE']);
                //結案紀錄
                $('#IFNSH_OP_FS_REC_CD').val(formData['OP_FS_REC_CD']);
                //結案情形描述
                $('#IFNSH_OP_FS_STAT_DESC').val(formData['OP_FS_STAT_DESC']);
                if (strPermissionData["Roles"].toString().indexOf('OP300004') > -1) {
                    undisableAll('IFNSH');
                    $('#saveFinishBtn').show();
                } else {
                    disableAll('IFNSH'); //如果有資料就不能修改
                    $('#saveFinishBtn').hide();
                }
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
//IFINISH_SHOW---END

//報表列印---START
function showGridList3(OP_BASIC_SEQ_NO) {
    var gridId = "#gridList_3";
    var pagerId = "#pageList_3";
    show_BlockUI_page_noParent('資料準備中…');
    $(gridId).jqGrid('GridUnload');
    $(gridId).jqGrid({
        url: 'AnnounceServlet',
        mtype: "POST",
        datatype: "json",
        postData: {
            ajaxAction: 'GetReportList',
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
//表單列印---END

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