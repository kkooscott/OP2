var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu02').addClass("active");
    $('#menu02span').addClass("selected");
    $('#menu0201').addClass("active");
    init();
//        showGridList3();
});

$(window).resize(function () {
    $('#gridMainList').setGridWidth($(window).width() - 10);
    $('#gridClueList').setGridWidth($('.col-md-12').width() - 63);
    $("#gridList_3").setGridWidth($(window).width() - 92);
});

function valueChoose(type) {
    $('#IPUBASIC_OP_PU_YN_OV500').val(type);
    if (type == "0") { //500
        $('#value500OpIsCust').show();
        //伍佰元專區預設自行保管 必須具名
//        $("input[name='IPUBASIC.OP_IS_CUST'][value='1']").prop("checked", true);
        $('#IPUBASIC_OP_IS_PUT_NM_1').prop('disabled', true);
        $('#IPUBASIC_OP_IS_PUT_NM_2').prop('disabled', true);
    } else {
        $('#value500OpIsCust').hide();
        $("input[name='IPUBASIC.OP_IS_CUST'][value='0']").prop("checked", true);
        $('#IPUBASIC_OP_IS_PUT_NM_1').prop('disabled', false);
        $('#IPUBASIC_OP_IS_PUT_NM_2').prop('disabled', false);
    }
    goTab('modal-editor-add', 'tabMain');
}

// #region Front End Event

function init() {
    strPermissionData = getUserRole();
    if (strPermissionData["RolePermission"] == '') {
        $.alert.open('error', '遺失使用者資訊，請重新登入');
        window.close();
    }
    // 受理資料帶入
    bindUNITDDL('2', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', 'ALL');
    bindDATATYPE('B01', 'IPUBASIC_OP_PUPO_TP_CD', '');
    bindDATATYPE('B02', 'IPUBASIC_OP_PUOJ_ATTR_CD', '');
    bindDATATYPE('B04', 'IPUBASIC_OP_CURSTAT_CD', '');
    bindDATATYPE('B07', 'IPUBASIC_OP_NTC_PSN_TYPE', '請選擇...');
    bindCOUNTRYTYPE('', 'IPUBASIC_OP_PUPO_NAT_CD', ''); //國家
    bindCOUNTRYDDL('2', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', 'ALL'); //鄉鎮
    bindCOUNTRYDDL('2', 'IPUBASIC_OP_PU_CITY_CD', 'IPUBASIC_OP_PU_TOWN_CD', '', '', 'ALL'); //鄉鎮
    // 明細資料帶入
    //新的物品種類DDL
    //if (checkDate($('#IPUBASIC_OP_SEQ_NO').val()) == true) {
    //    bindE8DATATYPE('C12', 'IPUDETAIL_OP_TYPE_CD', '請選擇...');
    //} else {
        bindOPDATATYPE('C12', 'IPUDETAIL_OP_TYPE_CD', '請選擇...');//物品類別
    //}
    bindCOLORTYPE('', 'IPUDETAIL_OP_COLOR_CD', '請選擇...'); //顏色
    bindDATATYPE('C39', 'IPUDETAIL_OP_QTY_UNIT_CHOOSE', '請選擇...'); //單位
    initEvent();
}

function initEvent() {
    //帶入預設值
    initDefaultQS();
    //按鍵事件區
    registButton();
}

// 按鍵事件區---Start
function registButton() {
    //基本資料儲存
    $('#saveBasicBtn').click(function () {

        if($('#IPUBASIC_OP_PU_YN_OV500').val()=='0'){//500以下
            var check=$("input[name='IPUBASIC.OP_IS_CUST']:checked").length;
            console.log("500以下的OP02A01Q.JS 下saveBasicBtn");
            
            //第一關check該check的欄位
            if (checkBasicList(document.getElementById('form1'))){  
                
                    //第二關check[ 民眾自行保管 ]欄位
                    if(check==0){
                        alert("欄位 [ 民眾自行保管 ]：必須輸入！");
                    }else{
                       //confirmAndSave(ACTION);   //以上兩關都過完了再可以真的存入
                         //檢查是否重複開案
                        confirmAndSave_OW("OP02A01Q_01.jsp");
                    }


             }
             
//    20221124員警反應沒有輸入 「民眾自行保管」 跳出錯誤訊息後，還是存入，存入後還是查不到
//    以下寫怪怪的，寫進去兩次   
//        if (checkBasicList(document.getElementById('form1')))
//            confirmAndSave(ACTION);
//        if($('#IPUBASIC_OP_PU_YN_OV500').val()=='0'){//500以下
//            var check=$("input[name='IPUBASIC.OP_IS_CUST']:checked").length;
//            if(check==0){
//                alert("欄位 [ 民眾自行保管 ]：必須輸入！");
//            }
//        }
//        if (checkBasicList(document.getElementById('form1')))
//            confirmAndSave_OW("OP02A01Q_01.jsp");

        }else{  //500元以上
            console.log("500以上的OP02A01Q.JS 下saveBasicBtn");
            if (checkBasicList(document.getElementById('form1')))
            confirmAndSave("OP02A01Q_01.jsp");

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
    //明細資料新增
    $('#addDetailBtn').click(function () {
        if (checkDetailList(document.getElementById('form1'))) {
            $('#actionDetailType').val('insertDetailList');
            confirmDetailAndSave("OP02A01Q_01.jsp");
        }
    });
    //明細資料修改
    $('#editDetailBtn').click(function () {
        if (checkDetailList(document.getElementById('form1'))) {
            $('#actionDetailType').val('updateDetailList');
            confirmDetailAndSave("OP02A01Q_01.jsp");
            $('#addDetailBtn').show();
            $('#editDetailBtn').hide();
            $('#deleteDetailBtn').hide();
        }
    });
    //明細資料刪除
    $('#deleteDetailBtn').click(function () {
        $('#actionDetailType').val('deleteDetailList');
        deleteDetail();
    });
    //明細資料清除
    $('#clearDetailBtn').click(function () {
        clearDetailData();
        $('#actionDetailType').val('insertDetailList');
        $('#IPUDETAIL_OP_SEQ_NO').val('');
        $('#addDetailBtn').show();
        $('#editDetailBtn').hide();
        $('#deleteDetailBtn').hide();
    });
    // 照片上傳
    $('#photo1SubmitBtn').click(function () {
        $('#file1').click();
    });
    $('#photo2SubmitBtn').click(function () {
        $('#file2').click();
    });
    // 照片刪除
    $('#photo1DeleteBtn').click(function () {
        $('#previewPhoto1').attr('src', 'assets/img/file-upload-with-preview.png');
        $('#file1').val('');
        $('#IPHOTO_DETELE1').val('1');
    });
    $('#photo2DeleteBtn').click(function () {
        $('#previewPhoto2').attr('src', 'assets/img/file-upload-with-preview.png');
        $('#file2').val('');
        $('#IPHOTO_DETELE2').val('1');
    });
    //地址轉換
    $("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD']").change(function () {
        if ($("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD']:checked").val() == "1") {
            $('#normalPattern').show();
            $('#freePattern').hide();
            $('#IPUBASIC_GET_HH_DATA').show();
            $('#IPUBASIC_OP_PUPO_ADDR_OTH').val('');
        } else if ($("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD']:checked").val() == "9") {
            $('#normalPattern').hide();
            $('#freePattern').show();
            $('#IPUBASIC_GET_HH_DATA').hide();
            $('#IPUBASIC_OP_PUPO_CITY_CD').val('');
            $('#IPUBASIC_OP_PUPO_TOWN_CD').val('');
            $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val('');
            $('#IPUBASIC_OP_PUPO_LIN').val('');
            $('#IPUBASIC_OP_PUPO_ROAD').val('');
        }
    });
    //民眾自行保管
    $("input[name='IPUBASIC.OP_IS_CUST']").change(function () {
        if ($("input[name='IPUBASIC.OP_IS_CUST']:checked").val() == "1") { //是
            $('#IPUBASIC_OP_IS_PUT_NM_1').prop('disabled', true);
            $('#IPUBASIC_OP_IS_PUT_NM_2').prop('disabled', true);
            $("input[name='IPUBASIC.OP_IS_PUT_NM'][value='1']").prop("checked", true);
            $("img[name=NAME_CHECK]").show();
        } else if ($("input[name='IPUBASIC.OP_IS_CUST']:checked").val() == "0") { //否
            $('#IPUBASIC_OP_IS_PUT_NM_1').prop('disabled', false);
            $('#IPUBASIC_OP_IS_PUT_NM_2').prop('disabled', false);
        }
    });
    //是否具名
    $("input[name='IPUBASIC.OP_IS_PUT_NM']").change(function () {
        if ($("input[name='IPUBASIC.OP_IS_PUT_NM']:checked").val() == "1") {
            $("img[name=NAME_CHECK]").show();
        } else if ($("input[name='IPUBASIC.OP_IS_PUT_NM']:checked").val() == "0") {
            $("img[name=NAME_CHECK]").hide();
            $("#IPUBASIC_OP_PUPO_NAME").val('不具名');
        }
    });
    //身分證轉換
    $("#IPUBASIC_OP_PUPO_IDN_TP").change(function () {
        gender();
    });
    $("#IPUBASIC_OP_PUPO_IDN").change(function () {
        gender();
    });
    //判斷IMEI or MAC
    $("#IPUDETAIL_OP_TYPE_CD").change(function () {
        TypeDecide();
    });
    //單位
    $("#IPUDETAIL_OP_QTY_UNIT_CHOOSE").change(function () {
        var value = $("#IPUDETAIL_OP_QTY_UNIT_CHOOSE").val();
        if (value == '') {
            $("#IPUDETAIL_OP_QTY_UNIT").val('');
        } else {
            $("#IPUDETAIL_OP_QTY_UNIT").val($('#IPUDETAIL_OP_QTY_UNIT_CHOOSE option:selected').text());
        }
    });
    //查詢e化報案imei
    $('#GET_E_IMEI_DATA').click(function () {
        Validator.init(document.getElementById('form1'));
        var value = $("#IPUDETAIL_OP_IMEI_CODE").val();
        Validator.required('IPUDETAIL_OP_IMEI_CODE', 'IMEI');
        if ($("#IPUDETAIL_OP_IMEI_CODE_2").val().trim() !== "") {
            value = value + "," + $("#IPUDETAIL_OP_IMEI_CODE_2").val();
            Validator.required('IPUDETAIL_OP_IMEI_CODE_2', 'IMEI2');
        }
        if (Validator.isValid()) {
            window.open("E8ImeiDataInfo.jsp?imeiOrMac=" + value + "&type=IMEI", '新分頁');
        } else {
            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
            return false;
        }
    });
    //查詢e化報案MAC
    $('#GET_E_MAC_DATA').click(function () {
        Validator.init(document.getElementById('form1'));
        var value = $("#IPUDETAIL_OP_MAC_ADDR").val();
        Validator.required('IPUDETAIL_OP_MAC_ADDR', 'MAC');
        if (Validator.isValid()) {
            window.open("E8ImeiDataInfo.jsp?imeiOrMac=" + value + "&type=MAC", '新分頁');
        } else {
            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
            return false;
        }
    });
    //查詢e化報案phone
    $('#GET_E_PHONE_DATA').click(function () {
        Validator.init(document.getElementById('form1'));
        var value = $("#IPUDETAIL_OP_PHONE_NUMBER").val();
        Validator.required('IPUDETAIL_OP_PHONE_NUMBER', 'PHONE');
        if ($("#IPUDETAIL_OP_PHONE_NUMBER_2").val().trim() !== "") {
            value = value + "," + $("#IPUDETAIL_OP_PHONE_NUMBER_2").val();
            Validator.required('IPUDETAIL_OP_PHONE_NUMBER_2', 'PHONE');
        }
        if (Validator.isValid()) {
            window.open("E8ImeiDataInfo.jsp?imeiOrMac=" + value + "&type=PHONE", '新分頁');
        } else {
            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
            return false;
        }
    });


    $('#printBtn').click(function () {
        if ($('#IPUBASIC_OP_PU_YN_OV500').val() == "0") {
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
//            if ($('#jqg_gridList_3_3').prop('checked'))
//            {
//                downloadReport("ReportServlet", {
//                    ajaxAction: 'OP08A03Q.doc',
//                    reportName: 'oP08A03Q.doc,oP08A04Q.doc',
//                    newReportName: '拾得人讓與拾得物所有權收據.doc,拾得人讓與拾得物所有權收據.doc',
//                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                    lockType: 'A'
//                });
//            }
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
            if ($('#jqg_gridList_3_4').prop('checked'))
            {
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP02A10Q.doc',
                    reportName: 'oP02A10Q.doc',
                    newReportName: '查詢失物招領手機或其他行動裝置電信門號單.doc',
                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                    lockType: 'A'
                });
            }
//            if ($('#jqg_gridList_3_6').prop('checked'))
//            {
//                downloadReport("ReportServlet", {
//                    ajaxAction: 'OP02A11Q.doc',
//                    reportName: 'oP02A11Q.doc,oP02A12Q.doc',
//                    newReportName: '拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc,拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc',
//                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                    lockType: 'A'
//                });
//            }
        } else {
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
//            if ($('#jqg_gridList_3_3').prop('checked')) {
//                downloadReport("ReportServlet", {
//                    ajaxAction: 'OP08A01Q.doc',
//                    reportName: 'oP08A01Q.doc,oP08A02Q.doc',
//                    newReportName: '代保管財產價值新臺幣伍佰元以下拾得物收據.doc,代保管財產價值新臺幣伍佰元以下拾得物收據.doc',
//                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                    lockType: 'A'
//                });
//            }
//            if ($('#jqg_gridList_3_3').prop('checked'))
//            {
//                downloadReport("ReportServlet", {
//                    ajaxAction: 'OP08A03Q.doc',
//                    reportName: 'oP08A03Q.doc,oP08A04Q.doc',
//                    newReportName: '拾得人讓與拾得物所有權收據.doc,拾得人讓與拾得物所有權收據.doc',
//                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                    lockType: 'A'
//                });
//            }
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
            if ($('#jqg_gridList_3_4').prop('checked'))
            {
                downloadReport("ReportServlet", {
                    ajaxAction: 'OP02A10Q.doc',
                    reportName: 'oP02A10Q.doc',
                    newReportName: '查詢失物招領手機或其他行動裝置電信門號單.doc',
                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
                    lockType: 'A'
                });
            }
//            if ($('#jqg_gridList_3_7').prop('checked'))
//            {
//                downloadReport("ReportServlet", {
//                    ajaxAction: 'OP02A11Q.doc',
//                    reportName: 'oP02A11Q.doc,oP02A12Q.doc',
//                    newReportName: '拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc,拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書.doc',
//                    OP_SEQ_NO: $('#IPUBASIC_OP_SEQ_NO').val(),
//                    lockType: 'A'
//                });
//            }
        }



    });

}

// 按鍵事件區---End
// 帶入預設值---Start
function initDefaultQS() {
    var nowDate = new Date();
    $('#actionType').val('insertBasicList');
    $('#actionDetailType').val('insertDetailList');

    $('#IPUBASIC_OP_AC_DATE').val(getROCDateSlash(nowDate));
    $('#IPUBASIC_OP_FM_DATE').val(getROCDateSlash(nowDate));

    // 帶入登入者單位
    $('#OP_unitLevel2').val(strPermissionData['UNITLEVEL1']);
    bindUNITDDL('3', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
    $('#OP_unitLevel3').val(strPermissionData['UNITLEVEL2']);
    bindUNITDDL('4', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');
    $('#OP_unitLevel4').val(strPermissionData['UNITLEVEL3']);
    $('#IPUBASIC_OP_AC_STAFF_NM').val(strPermissionData['NAME']);
    $('#IPUBASIC_OP_PUPO_NAT_CD').val('035');

    $('#saveBasicBtn').show();

}
// 帶入預設值---End
var ajaxActionUrl = 'PuMaintainServlet';

//BASIC_CHECK---START
function confirmAndSave_OW(ACTION) {
    var bool = true;  //bool 要開案
    //檢查是否重複開案
    if ($('#actionType').val() == "insertBasicList") {
        if ($("input[name='IPUBASIC.OP_IS_PUT_NM']:checked").val() == "1") {
            //以拾得人姓名、拾得人身分證、拾得日期、拾得時間為條件確認是否重複開案
            bool = checkSameBasic($('#IPUBASIC_OP_PUPO_NAME').val(), $('#IPUBASIC_OP_PUPO_IDN').val(), $('#IPUBASIC_OP_PU_DATE').val(), $('#IPUBASIC_OP_PU_TIME').val());
        }
    }
    if (bool) {
        saveBasic_OW(true, ACTION);
    } else {
        $.alert.open({
            type: 'confirm',
            content: '此案件已經存在，是否仍要繼續開案？',
            callback: function (button, value) {
                if (button == 'yes') {
                    saveBasic_OW(true, ACTION);
                }
            }
        });
    }
}
//BASIC_CHECK---END
function confirmAndSave(ACTION) {
    var bool = true;
    if (ACTION == "OP05A01Q_01.jsp") { //500以上結案更改狀態
        bool = checkStatusYNTrue($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUBASIC_OP_CURSTAT_CD').val(), ACTION);
    } else if (ACTION == "OP08A03Q_01.jsp") { //500以下結案更改狀態 20210525
        bool = checkStatusYNTrue($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUBASIC_OP_CURSTAT_CD').val(), ACTION);
    }
    if (bool) {
        saveBasic(true, ACTION);
    }
}
//BASIC_SAVE---START
function saveBasic_OW(bool, ACTION) {
    if ($('#OP_unitLevel4').val() != '') {
        OPLevel_NM = $('#OP_unitLevel2 option:selected').text();
        OPLevel_NM += $('#OP_unitLevel3 option:selected').text();
        OPLevel_NM += $('#OP_unitLevel4 option:selected').text();
    } else if ($('#OPEN_unitLevel3').val() != '') {
        OPLevel_NM = $('#OP_unitLevel2 option:selected').text();
        OPLevel_NM += $('#OP_unitLevel3 option:selected').text();
    } else {
        OPLevel_NM = $('#OP_unitLevel2 option:selected').text();
    }

    if (bool) {
        show_BlockUI_page_noParent('資料儲存中…');
        var ajData = {
            'ajaxAction': $('#actionType').val(),
            'OP_PU_YN_OV500': $('#IPUBASIC_OP_PU_YN_OV500').val(),
            'OP_UNITLEVEL2': $('#OP_unitLevel2').val() == '' ? '' : $('#OP_unitLevel2').val(),
            'OP_UNITLEVEL2_NM': $('#OP_unitLevel2').val() == '' ? '' : $('#OP_unitLevel2 option:selected').text(),
            'OP_UNITLEVEL3': $('#OP_unitLevel3').val() == '' ? '' : $('#OP_unitLevel3').val(),
            'OP_UNITLEVEL3_NM': $('#OP_unitLevel3').val() == '' ? '' : $('#OP_unitLevel3 option:selected').text(),
            'OP_UNITLEVEL4': $('#OP_unitLevel4').val() == '' ? '' : $('#OP_unitLevel4').val(),
            'OP_UNITLEVEL4_NM': $('#OP_unitLevel4').val() == '' ? '' : $('#OP_unitLevel4 option:selected').text(),
            'OP_ALL_UNITLEVEL_NM': OPLevel_NM,
            'OP_SEQ_NO': $('#IPUBASIC_OP_SEQ_NO').val(),
            'OP_AC_RCNO': $('#IPUBASIC_OP_AC_RCNO').val(),
            'OP_AC_STAFF_NM': $('#IPUBASIC_OP_AC_STAFF_NM').val(),
            'OP_AC_DATE': $('#IPUBASIC_OP_AC_DATE').val(),
            'OP_FM_DATE': $('#IPUBASIC_OP_FM_DATE').val(),
            'OP_AC_UNIT_TEL': $('#IPUBASIC_OP_AC_UNIT_TEL').val(),
            'OP_MANUAL_RCNO': $('#IPUBASIC_OP_MANUAL_RCNO').val(),
            'OP_IS_CUST': $("input[name='IPUBASIC.OP_IS_CUST']:checked").val(),
            'OP_IS_PUT_NM': $("input[name='IPUBASIC.OP_IS_PUT_NM']:checked").val(),
            'OP_INCLUDE_CERT': $("input[name='IPUBASIC.OP_INCLUDE_CERT']:checked").val(),
            'OP_PUPO_TP_CD': $('#IPUBASIC_OP_PUPO_TP_CD').val(),
            'OP_PUPO_TP_NM': $('#IPUBASIC_OP_PUPO_TP_CD option:selected').text(),
            'OP_PUOJ_ATTR_CD': $('#IPUBASIC_OP_PUOJ_ATTR_CD').val(),
            'OP_PUOJ_ATTR_NM': $('#IPUBASIC_OP_PUOJ_ATTR_CD option:selected').text(),
            'OP_PUPO_NAME': $('#IPUBASIC_OP_PUPO_NAME').val(),
            'OP_PUPO_RNAME': $('#IPUBASIC_OP_PUPO_RNAME').val(),
            'OP_PUPO_IDN_TP': $('#IPUBASIC_OP_PUPO_IDN_TP').val(),
            'OP_PUPO_IDN': $('#IPUBASIC_OP_PUPO_IDN').val(),
            'OP_PUPO_BEFROC': $('#IPUBASIC_OP_PUPO_BEFROC').val(),
            'OP_PUPO_BIRTHDT': $('#IPUBASIC_OP_PUPO_BIRTHDT').val(),
            'OP_PUPO_GENDER': $('#IPUBASIC_OP_PUPO_GENDER').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_GENDER option:selected').val(),
            'OP_PUPO_NAT_CD': $('#IPUBASIC_OP_PUPO_NAT_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_NAT_CD').val(),
            'OP_PUPO_NAT_NM': $('#IPUBASIC_OP_PUPO_NAT_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_NAT_CD option:selected').text(),
            'OP_PUPO_PHONENO': $('#IPUBASIC_OP_PUPO_PHONENO').val(),
            'OP_PUPO_ZIPCODE': $('#IPUBASIC_OP_PUPO_ZIPCODE').val(),
            'OP_OC_ADDR_TYPE_CD': $("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD']:checked").val(),
            'OP_PUPO_CITY_CD': $('#IPUBASIC_OP_PUPO_CITY_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_CITY_CD').val(),
            'OP_PUPO_CITY_NM': $('#IPUBASIC_OP_PUPO_CITY_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_CITY_CD option:selected').text(),
            'OP_PUPO_TOWN_CD': $('#IPUBASIC_OP_PUPO_TOWN_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_TOWN_CD').val(),
            'OP_PUPO_TOWN_NM': $('#IPUBASIC_OP_PUPO_TOWN_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_TOWN_CD option:selected').text(),
            'OP_PUPO_VILLAGE_CD': $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val(),
            'OP_PUPO_VILLAGE_NM': $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val() == '' ? '' : $('#IPUBASIC_OP_PUPO_VILLAGE_CD option:selected').text(),
            'OP_PUPO_LIN': $('#IPUBASIC_OP_PUPO_LIN').val(),
            'OP_PUPO_ROAD': $('#IPUBASIC_OP_PUPO_ROAD').val(),
            'OP_PUPO_ADDR_OTH': $('#IPUBASIC_OP_PUPO_ADDR_OTH').val(),
            'OP_PUPO_EMAIL': $('#IPUBASIC_OP_PUPO_EMAIL').val(),
            'OP_PU_DATE': $('#IPUBASIC_OP_PU_DATE').val(),
            'OP_PU_TIME': $('#IPUBASIC_OP_PU_TIME').val(),
            'OP_PU_CITY_CD': $('#IPUBASIC_OP_PU_CITY_CD').val() == '' ? '' : $('#IPUBASIC_OP_PU_CITY_CD').val(),
            'OP_PU_CITY_NM': $('#IPUBASIC_OP_PU_CITY_CD').val() == '' ? '' : $('#IPUBASIC_OP_PU_CITY_CD option:selected').text(),
            'OP_PU_TOWN_CD': $('#IPUBASIC_OP_PU_TOWN_CD').val() == '' ? '' : $('#IPUBASIC_OP_PU_TOWN_CD').val(),
            'OP_PU_TOWN_NM': $('#IPUBASIC_OP_PU_TOWN_CD').val() == '' ? '' : $('#IPUBASIC_OP_PU_TOWN_CD option:selected').text(),
            'OP_PU_PLACE': $('#IPUBASIC_OP_PU_PLACE').val(),
            'OP_CURSTAT_CD': $('#IPUBASIC_OP_CURSTAT_CD').val() == '' ? '' : $('#IPUBASIC_OP_CURSTAT_CD').val(),
            'OP_CURSTAT_NM': $('#IPUBASIC_OP_CURSTAT_CD').val() == '' ? '' : $('#IPUBASIC_OP_CURSTAT_CD option:selected').text(),
            'OP_NTC_FIND_PO': $('#IPUBASIC_OP_NTC_FIND_PO').val(),
            'OP_YN_NTC': $('#IPUBASIC_OP_YN_NTC').val(),
            'OP_NTC_DATE': $('#IPUBASIC_OP_NTC_DATE').val(),
            'OP_NTC_PSN_TYPE': $('#IPUBASIC_OP_NTC_PSN_TYPE').val(),
            'OP_NTC_MODE': $('#IPUBASIC_OP_NTC_MODE').val(),
            'OP_PU_REMARK': $('#IPUBASIC_OP_PU_REMARK').val(),
            'ACTION': ACTION
        };
        var ajSucc = function (JData) {
            if (JData.formData) {
                if ($('#actionType').val() == 'insertBasicList') {
                    notyMsg('新增成功');
                    var formData = JData.formData[0];
                    $('#IPUBASIC_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
                    $('#IPUDETAIL_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
                    $('#IPUBASIC_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                    $('#IPUBASIC_OP_PU_YN_OV500').val(formData['OP_PU_YN_OV500']);
                    $('#actionType').val('updateBasicList');
                    $("#tab_4_2_main").show();
                    $("#tab_4_2_alert").hide();
                    $("#tab_4_3_1_alert").hide();
                    $("#tab_4_3_2_alert").show();
                    $("#addDetailBtn").show();
                    $("#clearDetailBtn").show();
                    $.unblockUI();
                } else if ($('#actionType').val() == 'updateBasicList') {
                    notyMsg('修改成功');
                    var formData = JData.formData[0];
                    $('#IPUBASIC_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
                    $('#IPUDETAIL_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
                    $('#IPUBASIC_OP_SEQ_NO').val(formData['OP_SEQ_NO']);
                    $('#IPUBASIC_OP_PU_YN_OV500').val(formData['OP_PU_YN_OV500']);
                    $("#tab_4_2_main").show();
                    $("#tab_4_2_alert").hide();
                    $("#tab_4_3_1_alert").hide();
                    $("#tab_4_3_2_alert").show();
                    $("#addDetailBtn").show();
                    $("#clearDetailBtn").show();
                    $.unblockUI();
                }
            } else {
                $('#actionType').val() == 'insertBasicList' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
                $.unblockUI();
            }
        };
        var ajErr = function () {
            $('#actionType').val() == 'insertBasicList' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
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
//BASIC_SAVE---END

//DETAIL_SHOW---START
function showDetailGridList_OW(OP_BASIC_SEQ_NO, OP_AC_RCNO) {
    var gridId = "#gridClueList";
    var pagerId = "#pagerClueList";
    show_BlockUI_page_noParent('資料準備中…');
    //let appUseList = getAppUse();   //202403	
    
    $(gridId).jqGrid('GridUnload');
    $(gridId).jqGrid({
        url: ajaxActionUrl,
        mtype: "POST",
        datatype: "json",
        postData: {
            ajaxAction: 'GetDetailList',
            'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
            'OP_AC_RCNO': OP_AC_RCNO
            //202403新增明細完的GetDetailList會error，暫拿掉
            //'OPR_KIND': appUseList.OPR_KIND,  //202403 查詢用途代號
            //'OPR_PURP': appUseList.OPR_PURP   //202403 查詢目的
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
                $("#printBtn").show();
                showGridList3();
            } else {
                $("#tab_4_3_1_alert").hide();
                $("#tab_4_3_2_alert").show();
                $("#tab_4_3_main").hide();
                $("#printBtn").hide();
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
//                
                if (type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
                        || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") {
                    bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                } 

                
                if (typeName.indexOf('行動電話') >= 0 || typeName.indexOf('無線電話') >= 0 || typeName.indexOf('手機') >= 0) {
//                    $("#IMEILabel").show();
//                    $("#IMEILabel2").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").show();
//                    $("#IPUDETAIL_OP_IMEI_CODE").val(formData['OP_IMEI_CODE']);
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").val(formData['OP_IMEI_CODE_2']);
//                    $("#GET_E_IMEI_DATA").show();
//                    $("#PhoneLabel").show();
//                    $("#PhoneLabel2").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").show();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").val(formData['OP_PHONE_NUM']);
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").val(formData['OP_PHONE_NUM_2']);
                    $("#GET_E_PHONE_DATA").show();
                    bindE8DATATYPE('A15', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                } else {
//                    $("#IPUDETAIL_OP_IMEI_CODE").hide();
//                    $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
//                    $("#IMEILabel").hide();
//                    $("#IMEILabel2").hide();
//                    $("#GET_E_IMEI_DATA").hide();
                    $("#PhoneLabel").hide();
                    $("#PhoneLabel2").hide();
                    $("#IPUDETAIL_OP_PHONE_NUMBER").hide();
                    $("#GET_E_PHONE_DATA").hide();
                    $("#IPUDETAIL_OP_PHONE_NUMBER_2").hide();
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
//                    $("#IPUDETAIL_OP_MAC_ADDR").val(formData['OP_MAC_ADDR']);
//                    bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
//                } else {
//                    $("#MACLabel").hide();
//                    $("#IPUDETAIL_OP_MAC_ADDR").hide();
//                    $("#GET_E_MAC_DATA").hide();
//                }
                
                if (type == 'C003' || type == 'C001') {
                    bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
                } 
                
//
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線                         
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
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線                         
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
            $('#addDetailBtn').hide();
            $('#editDetailBtn').show();
            $('#deleteDetailBtn').show();
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
    if ($('#IPUBASIC_OP_PU_YN_OV500').val() == "0") {
        $(gridId).jqGrid('addRowData', 1, {
            FILE_NAME: '受理民眾交存拾得遺失物作業程序檢核表'
        });
        $(gridId).jqGrid('addRowData', 2, {
            FILE_NAME: '代保管財產價值新臺幣伍佰元以下拾得物收據(二聯)'
        });
//        $(gridId).jqGrid('addRowData', 3, {
//            FILE_NAME: '拾得人讓與拾得物所有權收據(二聯)'
//        });
        $(gridId).jqGrid('addRowData', 3, {
            FILE_NAME: '受理拾得物案陳報單'
        });
        $(gridId).jqGrid('addRowData', 4, {
            FILE_NAME: '查詢失物招領手機或其他行動裝置電信門號單'
        });
//        $(gridId).jqGrid('addRowData', 6, {
//            FILE_NAME: '拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書'
//        });
    } else {
        $(gridId).jqGrid('addRowData', 1, {
            FILE_NAME: '拾得物收據'
        });
        $(gridId).jqGrid('addRowData', 2, {
            FILE_NAME: '受理民眾交存拾得遺失物作業程序檢核表'
        });
//        $(gridId).jqGrid('addRowData', 3, {
//            FILE_NAME: '代保管財產價值新臺幣伍佰元以下拾得物收據(二聯)'
//        });
//        $(gridId).jqGrid('addRowData', 4, {
//            FILE_NAME: '拾得人讓與拾得物所有權收據(二聯)'
//        });
        $(gridId).jqGrid('addRowData', 3, {
            FILE_NAME: '受理拾得物案陳報單'
        });
        $(gridId).jqGrid('addRowData', 4, {
            FILE_NAME: '查詢失物招領手機或其他行動裝置電信門號單'
        });
//        $(gridId).jqGrid('addRowData', 5, {
//            FILE_NAME: '拾得人將拾得之手機或其他行動裝置讓與招領警察機關同意書'
//        });
    }
} 
//報表列印---END

//邏輯判斷---START
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