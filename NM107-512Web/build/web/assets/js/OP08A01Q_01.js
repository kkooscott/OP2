var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu08').addClass("active");
    $('#menu08span').addClass("selected");
    $('#menu0801').addClass("active");
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
            showGridListbyQS();
    });
    //清除
    $('#QS_CLEAR').click(function () {
        cleanEditor();
    });
    BasicAndDetailButton("OP08A01Q_01.jsp");
    //轉換至一般受理
    $('#updateBasicYnOv500').click(function () {
        updateBasicYnOv500();
    });
    //刪除
    $('#puDeleteBtn').click(function () {
        confirmDelete();
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
            showDetailGridList_OW($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUDETAIL_OP_AC_RCNO').val());
            if ($('#checkStatusForBasic').val() == "1") { //處理中
                if ($('#IPUBASIC_OP_PU_YN_OV500').val() == "1" || $('#IPUBASIC_OP_DEL_FLAG').val() == "1") { //伍佰元以上 或 已經刪除資料
                    disableAll('IPUDETAIL');
                    $("#addDetailBtn").hide();
                    $("#clearDetailBtn").hide();
                } else {
                    undisableAll('IPUDETAIL');
                    $("#addDetailBtn").show();
                    $("#clearDetailBtn").show();
                }
            } else {
                disableAll('IPUDETAIL');
                $("#addDetailBtn").hide();
                $("#clearDetailBtn").hide();
            }
        } else {
            $('#tab_4_2_alert').show();
            $('#tab_4_2_main').hide();
        }
    });
    //分頁3 報表列印
    $('#tabInfomation3').click(function () {
        //只是判斷要不要顯示
        var checkDetail = checkYNDetail($('#IPUBASIC_OP_SEQ_NO').val());
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
        }
    });
    $('#printBtn').click(function () {
//            if($('#jqg_gridList_3_1').prop('checked')){
//                downloadReport("ReportServlet", {
//                    ajaxAction: 'OP02A01Q.doc',
//                    reportName: 'oP02A01Q.doc,oP02A02Q.doc',
//                    newReportName: '拾得物收據.doc,拾得物收據.doc',
//                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                    lockType : 'A'
//                });
//            }
        if ($('#jqg_gridList_3_1').prop('checked')) {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP02A03Q.doc',
                reportName: 'oP02A03Q.doc',
                newReportName: '受理民眾交存拾得遺失物作業程序檢核表.doc',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
        }
        if ($('#jqg_gridList_3_2').prop('checked')) {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP08A01Q.doc',
                reportName: 'oP08A01Q.doc,oP08A02Q.doc',
                newReportName: '代保管財產價值新臺幣伍佰元以下拾得物收據.doc,代保管財產價值新臺幣伍佰元以下拾得物收據.doc',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
        }
//        if ($('#jqg_gridList_3_3').prop('checked')) {
//            downloadReport("ReportServlet", {
//                ajaxAction: 'OP08A03Q.doc',
//                reportName: 'oP08A03Q.doc,oP08A04Q.doc',
//                newReportName: '拾得人讓與拾得物所有權收據.doc,拾得人讓與拾得物所有權收據.doc',
//                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                lockType: 'A'
//            });
//        }
        if ($('#jqg_gridList_3_3').prop('checked')) {
            downloadReport("ReportServlet", {
                ajaxAction: 'OP02A09Q.doc',
                reportName: 'oP02A09Q.doc',
                newReportName: '受理拾得物案陳報單.doc',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                lockType: 'A'
            });
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

    $('#saveBasicBtn').show();
    //民眾自行保管欄位
    $('#value500OpIsCust').show();
}

var ajaxActionUrl = 'PuMaintainServlet';

// 202404 警署承辦需求: 列表;預設查詢，因未進去查，不寫NPALOG
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
                OP_DEL_FLAG: $('#OP_SEARCH_DEL_FLAG').val(),
                ACTION_TYPE: 'OP08A01Q_01'
            },
            height: "auto",
            autowidth: true,
            colNames: ["收據編號", "受理單位", "拾得人類別", "物品屬性", "拾得人姓名", "身分證/其他證號", "拾得日期時間", "拾得地點", "目前狀態", "BASIC_SEQ", "DEL_FLAG"],
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
                }, {
                    name: 'DEL_FLAG',
                    index: 'DEL_FLAG',
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
                var DelFlag = row.DEL_FLAG;

                $('#actionType').val('updateBasicList');
                $('#IPUBASIC_OP_SEQ_NO').val(BasicNo);
                $('#IPUBASIC_OP_AC_RCNO').val(AcRcno);
                $('#IPUDETAIL_OP_AC_RCNO').val(AcRcno);
                $('[id*="tabInfomation"]').removeClass('active');
                $('[id*="tab_4_"]').removeClass('active');
                $('#tabInfomation1').addClass('active');
                $('#tab_4_1').addClass('active');

                $('#checkStatusForBasic').val(checkStatusForBasic(BasicNo));
                $('#checkYNDetail').val(checkYNDetail(BasicNo));
                $('#IPUBASIC_OP_DEL_FLAG').val(DelFlag);
                BasicValueForLog(BasicNo);
                
                if (DelFlag == "0") { //不是刪除資料
                    $('#updateBasicYnOv500').show();
                    $('#puDeleteLabel').show();
                    $('#puDeleteReason').show();
                    $('#puDeleteBtn').show();
                    undisableAll('IPUBASIC');
                    undisableAll('IPUDETAIL');
                    $('#deleteData').hide();
                } else { //刪除資料
                    $('#updateBasicYnOv500').hide();
                    $('#puDeleteLabel').hide();
                    $('#puDeleteReason').hide();
                    $('#puDeleteBtn').hide();
                    disableAll('IPUBASIC');
                    disableAll('IPUDETAIL');
                    $('#deleteData').show();
                }
                //202403 改500以下派出所層級不要秀刪除案件

                if (strPermissionData["RolePermission"] != '1') { //不是刪除資料
                    $('#puDeleteLabel').show();
                    $('#puDeleteReason').show();
                    $('#puDeleteBtn').show();
                } else { //刪除資料
                    $('#puDeleteLabel').hide();
                    $('#puDeleteReason').hide();
                    $('#puDeleteBtn').hide();
                }

                $('#tabInfomation1').click();
                goTab('modal-editor-add', 'tabMain');
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

            if (formData['OP_DEL_FLAG'] == "1") { //已經刪除資料
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
            }

            if (formData['OP_CURSTAT_CD'] == "1") { //處理中
                if (formData['OP_PU_YN_OV500'] == "1" || formData['OP_DEL_FLAG'] == "1") { //伍佰元以上 或 已經刪除資料
                    disableAll('IPUBASIC');
                    $('#saveBasicBtn').hide();
                } else {
                    undisableAll('IPUBASIC');
                    $('#saveBasicBtn').show();
                    //更新邏輯 伍佰元專區預設自行保管 必須具名
                    //拾得人基本資料
                    if (formData['OP_IS_CUST'] == "1") {
                        $('#IPUBASIC_OP_IS_PUT_NM_1').prop('disabled', true);
                        $('#IPUBASIC_OP_IS_PUT_NM_2').prop('disabled', true);
                        $("input[name='IPUBASIC.OP_IS_PUT_NM'][value='1']").prop("checked", true);
                        $("img[name=NAME_CHECK]").show();
                    } else {
                        $('#IPUBASIC_OP_IS_PUT_NM_1').prop('disabled', false);
                        $('#IPUBASIC_OP_IS_PUT_NM_2').prop('disabled', false);
                    }
                }
            } else {
                disableAll('IPUBASIC');
                $('#saveBasicBtn').hide();
            }


            $('#IPUDETAIL_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
            $.unblockUI();
        }
    });
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
            if ($('#checkStatusForBasic').val() == "1") { //處理中
                $('#addDetailBtn').hide();
                $('#editDetailBtn').show();
                $('#deleteDetailBtn').show();
                if ($('#IPUBASIC_OP_PU_YN_OV500').val() == "1" || $('#IPUBASIC_OP_DEL_FLAG').val() == "1") { //伍佰元以上 或 已經刪除資料
                    $('#addDetailBtn').hide();
                    $('#editDetailBtn').hide();
                    $('#deleteDetailBtn').hide();
                    $('#clearDetailBtn').hide();
                }
            } else {
                $('#addDetailBtn').hide();
                $('#editDetailBtn').hide();
                $('#deleteDetailBtn').hide();
                $('#clearDetailBtn').hide();
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

//轉至一般受理---START
function updateBasicYnOv500() {
    $.alert.open({
        type: 'confirm',
        content: '此案件為伍佰元專區案件，是否要轉成一般受理案件受理？',
        callback:
                function (button, value) {
                    if (button == 'yes') {
                        show_BlockUI_page_noParent('資料變更中…');
                        $('#IPUBASIC_OP_PU_YN_OV500').val('1');
                        var ajData = {
                            'ajaxAction': 'updateBasicYnOv500',
                            'OP_PU_YN_OV500': $('#IPUBASIC_OP_PU_YN_OV500').val(),
                            'OP_SEQ_NO': $('#IPUBASIC_OP_SEQ_NO').val()
                        };
                        var ajSucc = function (JData) {
                            if (JData.formData) {
                                notyMsg('修改成功');
                                disableAll('IPUBASIC');
                                $('#saveBasicBtn').hide();
                                disableAll('IPUDETAIL');
                                $('#addDetailBtn').hide();
                                $('#editDetailBtn').hide();
                                $('#deleteDetailBtn').hide();
                                $('#clearDetailBtn').hide();

                                $('#puDeleteLabel').hide();
                                $('#puDeleteReason').hide();
                                $('#puDeleteBtn').hide();
                                $('#updateBasicYnOv500').hide();
                                //刷新查詢結果
                                showGridListbyQS();
                            } else {
                                $.alert.open('error', "修改失敗!!!");
                            }
                            $.unblockUI();
                        };
                        var ajErr = function () {
                            $.alert.open('error', "修改失敗!!!");
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
                }
    });

}
//轉至伍佰元專區---END

//刪除---START
function confirmDelete() {
    var bool = true;
    var msg = "";
    //拾得日期
    if ($('#puDeleteReason').val() == "") {
        msg += '請填入刪除原因<br/>';
        bool = false;
    }
    if ($('#checkStatusForBasic').val() != "1") {
        msg += '只能刪除處理中的資料<br/>';
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
        $.alert.open({
            type: 'confirm',
            content: '資料刪除中，確定是否要刪除?',
            callback: function (button, value) {
                if (button == 'yes')
                    DeleteBtn();

            }
        });
    }
}
function DeleteBtn() {
    var bool = true;
    if (bool) {

        $.ajax({
            url: ajaxActionUrl,
            type: "POST",
            dataType: "json",
            data: {
                ajaxAction: 'batchdPuDelete',
                OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                OP_AC_RCNO: $('#IPUBASIC_OP_AC_RCNO').val(),
                OP_DEL_RSN: $('#puDeleteReason').val()
            },
            clearForm: true,
            resetForm: true,
            success: function (data, textStatus, xhr, $form) {
                notyMsg('已刪除此筆資料');
                $('#puDeleteReason').val('');
                disableAll('IPUBASIC');
                $('#saveBasicBtn').hide();
                disableAll('IPUDETAIL');
                $('#addDetailBtn').hide();
                $('#editDetailBtn').hide();
                $('#deleteDetailBtn').hide();
                $('#clearDetailBtn').hide();

                $('#puDeleteLabel').hide();
                $('#puDeleteReason').hide();
                $('#puDeleteBtn').hide();
                $('#updateBasicYnOv500').hide();
                $('#IPUBASIC_OP_DEL_FLAG').val("1");
                //刷新查詢結果
                showGridListbyQS();
            }
        });
    }
}
//刪除---END

//報表列印---START
function showGridList3(CP_SEQ_NO) {
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
//        $(gridId).jqGrid('addRowData',1,{
//                        FILE_NAME:'拾得物收據'
//                });
    $(gridId).jqGrid('addRowData', 1, {
        FILE_NAME: '受理民眾交存拾得遺失物作業程序檢核表'
    });
    $(gridId).jqGrid('addRowData', 2, {
        FILE_NAME: '代保管財產價值新臺幣伍佰元以下拾得物收據(二聯)'
    });
//    $(gridId).jqGrid('addRowData', 3, {
//        FILE_NAME: '拾得人讓與拾得物所有權收據(二聯)'
//    });
    $(gridId).jqGrid('addRowData', 3, {
        FILE_NAME: '受理拾得物案陳報單'
    });
}

// 查詢條件 清除條件
function cleanEditor() {
    $('#queryporlet input[type="text"]').val('');
    $('#OP_SEARCH_PUPO_TP_CD').val('');
    $('#OP_SEARCH_PUOJ_ATTR_CD').val('');
    $('#OP_SEARCH_DEL_FLAG').val('0');
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
    $('#addDetailBtn').show();
    $('#editDetailBtn').hide();
    $('#deleteDetailBtn').hide();
    $('#IPUDETAIL_OP_TYPE_CD').multipleSelect("uncheckAll");
    $('#IPUDETAIL_OP_TYPE_CD').multipleSelect("refresh");
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