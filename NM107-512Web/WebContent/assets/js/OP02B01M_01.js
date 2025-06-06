var ajaxActionUrl = 'PuMaintainServlet';
//LOG---START
 
function BasicValueForLog(OP_SEQ_NO) {
    var bool;
        let appUseList = getAppUse(); //202404
    var ajData = {
        'ajaxAction': 'BasicValueForLog',
        'OP_SEQ_NO': OP_SEQ_NO,
        'OPR_KIND': appUseList.OPR_KIND,  //202404 查詢用途代號
        'OPR_PURP': appUseList.OPR_PURP   //202404 查詢目的
    };
    var ajSucc = function (JData) {

    };
    var ajErr = function () {

    };

    $.ajax({
        url: ajaxActionUrl,
        type: "POST",
        dataType: "json",
        data: ajData,
        success: ajSucc,
        error: ajErr
    });
    return bool;
}
//LOG---END
//LOG---START
function BasicValueForLog2(OP_AC_RCNO, OP_PUPO_NAME, OP_PUPO_IDN, OP_PUPO_PHONENO, OP_PUPO_BIRTHDT) {
    var bool;
    let appUseList = getAppUse(); //202404

    var ajData = {
        'ajaxAction': 'BasicValueForLog2',
        'OP_AC_RCNO': OP_AC_RCNO,
        'OP_PUPO_NAME': OP_PUPO_NAME,
        'OP_PUPO_IDN': OP_PUPO_IDN,
        'OP_PUPO_PHONENO': OP_PUPO_PHONENO,
        'OP_PUPO_BIRTHDT': OP_PUPO_BIRTHDT,
        'OPR_KIND': appUseList.OPR_KIND,  //202404 查詢用途代號
        'OPR_PURP': appUseList.OPR_PURP   //202404 查詢目的

    };
    var ajSucc = function (JData) {

    };
    var ajErr = function () {

    };

    $.ajax({
        url: ajaxActionUrl,
        type: "POST",
        dataType: "json",
        data: ajData,
        success: ajSucc,
        error: ajErr
    });
    return bool;
}
//LOG---END

//CHECK---START
function checkStatusForBasic(OP_BASIC_SEQ_NO) {
    var bool;
    var ajData = {
        'ajaxAction': 'checkStatusForBasic',
        'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
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
        url: ajaxActionUrl,
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

//CHECK---START
function checkStatusYNTrue(OP_BASIC_SEQ_NO, OP_CURSTAT_CD, ACTION) {
    var bool;
    var ajData = {
        'ajaxAction': 'checkStatusYNTrue',
        'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
        'OP_CURSTAT_CD': OP_CURSTAT_CD,
        'ACTION': ACTION
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            var formData = JData.formData[0];
            if (formData['msg'] == "") {
                bool = true;
            } else {
                $.alert.open({
                    type: 'error',
                    content: formData['msg']
                });
                bool = false;
            }
        }
    };
    var ajErr = function () {

    };

    $.ajax({
        url: ajaxActionUrl,
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

//CHECK---START
function checkSameBasic(OP_PUPO_NAME, OP_PUPO_IDN, OP_PU_DATE, OP_PU_TIME) {
    var bool;
    var ajData = {
        'ajaxAction': 'checkSameBasic',
        'OP_PUPO_NAME': OP_PUPO_NAME,
        'OP_PUPO_IDN': OP_PUPO_IDN,
        'OP_PU_DATE': OP_PU_DATE,
        'OP_PU_TIME': OP_PU_TIME
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
        url: ajaxActionUrl,
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

//邏輯判斷---START
//身分證判斷性別
function gender() {
    if ($('#IPUBASIC_OP_PUPO_IDN_TP').val() == "1") {
        $('#IPUBASIC_OP_PUPO_GENDER').attr('disabled', true);
        if ($('#IPUBASIC_OP_PUPO_IDN').val().charAt(1) == "1") {
            $('#IPUBASIC_OP_PUPO_GENDER').val("1");
        } else if ($('#IPUBASIC_OP_PUPO_IDN').val().charAt(1) == "2") {
            $('#IPUBASIC_OP_PUPO_GENDER').val("2");
        }
    } else {
        if ($('#IPUBASIC_OP_PUPO_IDN_TP').val() == "3") {
            $('#IPUBASIC_OP_PUPO_IDN').val("");
        }
        $('#IPUBASIC_OP_PUPO_GENDER').attr('disabled', false);
    }
}

//判斷IMEI or MAC or Phone
function TypeDecide() {
    var type = $("#IPUDETAIL_OP_TYPE_CD").val();
    var typeName = $('#IPUDETAIL_OP_TYPE_CD').val() == '' ? '' : $('#IPUDETAIL_OP_TYPE_CD option:selected').text();
    var listNew = checkDate($('#IPUBASIC_OP_SEQ_NO').val());
    if (listNew == true) {
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線          
//        if (type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
//                || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") {
//            $("#IPUDETAIL_OP_MAC_ADDR").show();
//            $("#GET_E_MAC_DATA").show();
//            $("#MACLabel").show();
//            bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
//        } else {
//            $("#MACLabel").hide();
//            $("#IPUDETAIL_OP_MAC_ADDR").hide();
//            $("#GET_E_MAC_DATA").hide();
//        }

        if (type == "A009" || type == "A010" || type == "A011" || type == "A012" || type == "A030" || type == "A033"
                || type == "A052" || type == "A069" || type == "BD03" || type == "BD04" || type == "BD05" || type == "BD10" || type == "BD11") {
            bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
        } 

        if (typeName.indexOf('行動電話') >= 0 || typeName.indexOf('無線電話') >= 0 || typeName.indexOf('手機') >= 0) {
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線          
//            $("#IMEILabel").show();
//            $("#IMEILabel2").show();
//            $("#IPUDETAIL_OP_IMEI_CODE").show();
//            $("#IPUDETAIL_OP_IMEI_CODE_2").show();
//            $("#GET_E_IMEI_DATA").show();
            $("#PhoneLabel").show();
            $("#PhoneLabel2").show();
            $("#IPUDETAIL_OP_PHONE_NUMBER").show();
            $("#IPUDETAIL_OP_PHONE_NUMBER_2").show();
            $("#GET_E_PHONE_DATA").show();
            bindE8DATATYPE('A15', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
        } else {
//            $("#IMEILabel").hide();
//            $("#IMEILabel2").hide();
//            $("#IPUDETAIL_OP_IMEI_CODE").hide();
//            $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
//            $("#GET_E_IMEI_DATA").hide();
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
    } else {
//202406警署承辦規範需求: 拾得物 應以外觀描述為主，故拾得這兒刪除輸入與表現MAC和IMEI, 但功能不刪，以利未來有的再度上線          
//        if (type == 'C003'|| type == 'C001') {
//            $("#IPUDETAIL_OP_MAC_ADDR").show();
//            $("#GET_E_MAC_DATA").show();
//            $("#MACLabel").show();
//            bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
//        } else {
//            $("#MACLabel").hide();
//            $("#IPUDETAIL_OP_MAC_ADDR").hide();
//            $("#GET_E_MAC_DATA").hide();
//        }

        if (type == 'C003'|| type == 'C001') {
            bindE8DATATYPE('A16', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
        } 


        if (typeName.indexOf('手機') >= 0) {
//            $("#IMEILabel").show();
//            $("#IMEILabel2").show();
//            $("#IPUDETAIL_OP_IMEI_CODE").show();
//            $("#IPUDETAIL_OP_IMEI_CODE_2").show();
//            $("#GET_E_IMEI_DATA").show();
            $("#PhoneLabel").show();
            $("#PhoneLabel2").show();
            $("#IPUDETAIL_OP_PHONE_NUMBER").show();
            $("#IPUDETAIL_OP_PHONE_NUMBER_2").show();
            $("#GET_E_PHONE_DATA").show();
            bindE8DATATYPE('A15', 'IPUDETAIL_OP_BRAND_CD', '請選擇...'); //廠牌
        } else {
//            $("#IMEILabel").hide();
//            $("#IMEILabel2").hide();
//            $("#IPUDETAIL_OP_IMEI_CODE").hide();
//            $("#IPUDETAIL_OP_IMEI_CODE_2").hide();
//            $("#GET_E_IMEI_DATA").hide();
            $("#PhoneLabel").hide();
            $("#PhoneLabel2").hide();
            $("#IPUDETAIL_OP_PHONE_NUMBER").hide();
            $("#IPUDETAIL_OP_PHONE_NUMBER_2").hide();
            $("#GET_E_PHONE_DATA").hide();
            $('#IPUDETAIL_OP_BRAND_CD').empty();
            $('#IPUDETAIL_OP_BRAND_CD').append("<option value=''>無資料</option>");
        }
    }
}
//邏輯判斷---END

//BASIC_CHECK---START
function checkBasicList(inForm) {
    Validator.init(inForm);
    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    //受理日期
    if (getADDate($('#IPUBASIC_OP_AC_DATE').val().substring(0, 9)) > getADDate(nowDate)) {
        Validator.setMessage("欄位 [ 受理日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IPUBASIC_OP_AC_DATE");
    }
    Validator.required('IPUBASIC_OP_AC_DATE', '受理日期');

    //單位電話
    Validator.required('IPUBASIC_OP_AC_UNIT_TEL', '單位電話');

    var putNm = $("input[name='IPUBASIC.OP_IS_PUT_NM']:checked").val();
    if (putNm == '1') { //具名
        Validator.required('IPUBASIC_OP_PUPO_TP_CD', '類別');
        Validator.required('IPUBASIC_OP_PUOJ_ATTR_CD', '物品所屬');
        Validator.required('IPUBASIC_OP_PUPO_NAME', '拾得人姓名');
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
        Validator.required('IPUBASIC_OP_PUPO_GENDER', '拾得人性別');
        Validator.required('IPUBASIC_OP_PUPO_PHONENO', '拾得人電話');
        var addrType = $("input[name='IPUBASIC.OP_OC_ADDR_TYPE_CD']:checked").val();
        if (addrType == '1') { //一般地址
            Validator.required('IPUBASIC_OP_PUPO_CITY_CD', '拾得人地址縣市');
            Validator.required('IPUBASIC_OP_PUPO_TOWN_CD', '拾得人地址鄉鎮');
//            Validator.required('IPUBASIC_OP_PUPO_VILLAGE_CD', '拾得人地址村里');
            Validator.required('IPUBASIC_OP_PUPO_ROAD', '拾得人地址');
        } else if (addrType == '9') { //自由輸入
            Validator.required('IPUBASIC_OP_PUPO_ADDR_OTH', '拾得人地址');
        }
    } else {
        //證號
        if ($("#IPUBASIC_OP_PUPO_NAT_CD").val() == "035" && $("#IPUBASIC_OP_PUPO_IDN_TP").val() == "2") {
            Validator.setMessage("欄位 [ 證號 ] 為\"其他證號\"時，[ 國籍 ]不得為中華民國國籍 !!");
            Validator.setBGColor("IPUBASIC_OP_PUPO_NAT_CD", "IPUBASIC_OP_PUPO_IDN_TP");
        } else if ($("#IPUBASIC_OP_PUPO_NAT_CD").val() != "" && $("#IPUBASIC_OP_PUPO_NAT_CD").val() != "035" && $("#IPUBASIC_OP_PUPO_IDN_TP").val() == "1") {
            Validator.setMessage("欄位 [ 證號 ] 為\"身分證號\"時，[ 國籍 ]不得為他國國籍 !!");
            Validator.setBGColor("IPUBASIC_OP_PUPO_NAT_CD", "IPUBASIC_OP_PUPO_IDN_TP");
        } else if ($("#IPUBASIC_OP_PUPO_IDN_TP").val() == "1" && $("#IPUBASIC_OP_PUPO_IDN").val() != "") {
            Validator.checkID("IPUBASIC_OP_PUPO_IDN", true, "身分證號");
        } else if ($("#IPUBASIC_OP_PUPO_IDN_TP").val() == "3") {
            Validator.checkLength("IPUBASIC_OP_PUPO_IDN", false, "身分證號", 20);
        } else {
            Validator.checkLength("IPUBASIC_OP_PUPO_IDN", false, "身分證號", 20);
        }
    }
    
    //檢查一下出生年月日民國7碼或西元8碼都應該要在0-9之間
    var pattern7 = new RegExp("[0-9]{7}");
    var pattern8 = new RegExp("[0-9]{8}");
          
    //出生日期 20241007
    if ($('#IPUBASIC_OP_PUPO_BIRTHDT').val() !== "") {
        if ($('#IPUBASIC_OP_PUPO_BEFROC').val() === "0" || $('#IPUBASIC_OP_PUPO_BEFROC').val() === "1") {
            var lastDay = new Date(Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring(0, 3)) + 1911, Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 4, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2)), 0).getDate();
                //console.log("出生日期__IF 前"+$('#IPUBASIC_OP_PUPO_BIRTHDT').val() );
            if ($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length !== 7) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期格式(YYYMMDD)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            }else if  ( !(pattern7.test(  $('#IPUBASIC_OP_PUPO_BIRTHDT').val() )) ) {
                //console.log("出生日期__IF 內"+$('#IPUBASIC_OP_PUPO_BIRTHDT').val() );
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的數字日期格式(YYYMMDD)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            } else if (Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 4, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2)) < 1 || Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 4, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2)) > 12) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的月份(1~12月)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            } else if (Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length)) < 1 || Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length)) > lastDay) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期(1~" + lastDay + "日)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            }
        } else if ($('#IPUBASIC_OP_PUPO_BEFROC').val() === "2") {
            var lastDay = new Date(Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring(0, 4)), Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring(4, 6)), 0).getDate();
            if ($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length !== 8) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期格式(YYYYMMDD)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            }else if ( !(pattern8.test(  $('#IPUBASIC_OP_PUPO_BIRTHDT').val() )) ) {
                //console.log("出生日期__IF 內"+$('#IPUBASIC_OP_PUPO_BIRTHDT').val() );
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的數字日期格式(YYYYMMDD)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            } else if (Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 4, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2)) < 1 || Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 4, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2)) > 12) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的月份(1~12月)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            } else if (Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length)) < 1 || Number($('#IPUBASIC_OP_PUPO_BIRTHDT').val().substring($('#IPUBASIC_OP_PUPO_BIRTHDT').val().length - 2, $('#IPUBASIC_OP_PUPO_BIRTHDT').val().length)) > lastDay) {
                Validator.setMessage("欄位 [ 出生日期 ]：請填寫正確的日期(1~" + lastDay + "日)！");
                Validator.setBGColor("IPUBASIC_OP_PUPO_BIRTHDT");
            }
        }
    }
    //電子郵件
    if ($('#IPUBASIC_OP_PUPO_EMAIL').val() != '') {
        Validator.checkMail("IPUBASIC_OP_PUPO_EMAIL", false, "電子郵件");
    }
    //拾得日期
    if (getADDate($('#IPUBASIC_OP_PU_DATE').val().substring(0, 9)) > getADDate(nowDate)) {
        Validator.setMessage("欄位 [ 拾得日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IPUBASIC_OP_PU_DATE");
    }
    if (getADDate($('#IPUBASIC_OP_PU_DATE').val().substring(0, 9)) > getADDate($('#IPUBASIC_OP_AC_DATE').val().substring(0, 9))) {
        Validator.setMessage("欄位 [ 拾得日期 ]：時間需小於受理日期時間！");
        Validator.setBGColor("IPUBASIC_OP_PU_DATE");
    }
    Validator.required('IPUBASIC_OP_PU_DATE', '拾得日期');
    Validator.required('IPUBASIC_OP_PU_TIME', '拾得時間');
    //新系統上線日期前的資料不用檢核
    if (getADDate($("#IPUBASIC_OP_FM_DATE").val()) >= getADDate(getOnlineDay())) {
        Validator.required('IPUBASIC_OP_PU_CITY_CD', '拾得地點縣市');
    }
    Validator.required('IPUBASIC_OP_PU_PLACE', '拾得地點地址');

    var ynNtc = $("#IPUBASIC_OP_YN_NTC").val();
    if (ynNtc == '1') { //已通知
        Validator.required('IPUBASIC_OP_NTC_DATE', '通知日期');
        Validator.required('IPUBASIC_OP_NTC_PSN_TYPE', '通知者身分');
        Validator.required('IPUBASIC_OP_NTC_MODE', '通知方式');
    }

    if (Validator.isValid())
        return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//BASIC_CHECK---END

//BASIC_SHOW---START
function showBasicGridList(OP_SEQ_NO) {
    show_BlockUI_page_noParent('資料準備中…');
        let appUseList = getAppUse(); //202404
    var ajData = {
        'ajaxAction': 'showBasicValue',
        'OP_SEQ_NO': OP_SEQ_NO,
        'OPR_KIND': appUseList.OPR_KIND,  //202404 查詢用途代號
        'OPR_PURP': appUseList.OPR_PURP   //202404 查詢目的

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

            $('#IPUDETAIL_OP_AC_RCNO').val(formData['OP_AC_RCNO']);
            $('#IPUCLAIM_OP_AC_RCNO').val(formData['OP_AC_RCNO']);

            $.unblockUI();
        }
    });
}
//BASIC_SHOW---END

function BasicAndDetailButton(ACTION) {
    //基本資料儲存
    $('#saveBasicBtn').click(function () {
        //民眾自行保管
        if($('#IPUBASIC_OP_PU_YN_OV500').val()=='0'){//500以下
            var check=$("input[name='IPUBASIC.OP_IS_CUST']:checked").length;
            if(check==0){
                alert("欄位 [ 民眾自行保管 ]：必須輸入！");
            }
        }
        if (checkBasicList(document.getElementById('form1')))
            confirmAndSave(ACTION);
    });
    //明細資料新增
    $('#addDetailBtn').click(function () {
        if (checkDetailList(document.getElementById('form1'))) {
            $('#actionDetailType').val('insertDetailList');
            confirmDetailAndSave(ACTION);
        }
    });
    //明細資料修改
    $('#editDetailBtn').click(function () {
        if (checkDetailList(document.getElementById('form1'))) {
            $('#actionDetailType').val('updateDetailList');
            confirmDetailAndSave(ACTION);
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
    $("#IPUBASIC_OP_PUPO_IDN").keyup(function () {
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
            Validator.required('IPUDETAIL_OP_PHONE_NUMBER_2', 'PHONE2');
        }
        if (Validator.isValid()) {
            window.open("E8ImeiDataInfo.jsp?imeiOrMac=" + value + "&type=PHONE", '新分頁');
        } else {
            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
            return false;
        }
    });
    
}

//BASIC_CHECK---START
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
//BASIC_CHECK---END
//BASIC_SAVE---START
function saveBasic(bool, ACTION) {
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
            'OP_UNITLEVEL2_NM': OPLevel_NM,
            'OP_UNITLEVEL3': $('#OP_unitLevel3').val() == '' ? '' : $('#OP_unitLevel3').val(),
            'OP_UNITLEVEL3_NM': OPLevel_NM,
            'OP_UNITLEVEL4': $('#OP_unitLevel4').val() == '' ? '' : $('#OP_unitLevel4').val(),
            'OP_UNITLEVEL4_NM': OPLevel_NM,
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
                    
                    //20230206_拾得反應新增一案成功後新增明細頁無法新增明細
                    //和500以下不用自動跳轉，若要自動跳轉打開即可
                    //goTab('tab_4_2', 'tab_4_1');
                    //goTab(id = "tabInfomation2", id = "tabInfomation1");
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
                    
                    
                    //20230206_拾得反應新增一案成功後新增明細頁無法新增明細
                    //和500以下不用自動跳轉，若要自動跳轉打開即可
                    //goTab('tab_4_2', 'tab_4_1');
                    //goTab(id = "tabInfomation2", id = "tabInfomation1");
                    $("#tab_4_2_main").show();
                    $("#tab_4_2_alert").hide();
                    $("#tab_4_3_1_alert").hide();
                    $("#tab_4_3_2_alert").show();
                    
                    $("#addDetailBtn").show();
                    $("#clearDetailBtn").show();
                }
                $.unblockUI();
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
var OP_OI_A1_ID = "";
function getOI2PersonByIDN(TYPE) {
    if (TYPE == "BASIC") {
        $.ajax({
            type: "POST",
            url: 'WebServiceServlet',
            data: {ajaxAction: 'getOI2PersonByIDN', OP_PUPO_IDN: $('#IPUBASIC_OP_PUPO_IDN').val().toUpperCase(), IDN_TYPE: TYPE},
            dataType: "json",
            success: function (data, textStatus, xhr) {
                if (data.formData != undefined) {
                    OP_OI_A1_ID = data.formData.A1_ID;
                    $('#IPUBASIC_OP_PUPO_NAME').val(data.formData.A1_NAME);
                    $('#IPUBASIC_OP_PUPO_RNAME').val(data.formData.A1_RNAME);
                    $('#IPUBASIC_OP_PUPO_CITY_CD').val(data.formData.A1_CITY);
                    bindCOUNTRYDDL('3', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', '');
                    $('#IPUBASIC_OP_PUPO_TOWN_CD').val(data.formData.A1_TOWN);
                    bindCOUNTRYDDL('4', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', '');
                    $('#IPUBASIC_OP_PUPO_VILLAGE_CD').val(data.formData.A1_VILLAGE);
                    //$('#IPUBASIC_OP_PUPO_LIN').val(fullToHalf(data.formData.A1_NEIGH));
                    //$('#IPUBASIC_OP_PUPO_ROAD').val(fullToHalf(data.formData.A1_ROAD));
                    $('#IPUBASIC_OP_PUPO_LIN').val(data.formData.A1_NEIGH);
                    $('#IPUBASIC_OP_PUPO_ROAD').val(data.formData.A1_ROAD);
                } else {
                    $.alert.open({type: 'error', content: '查無資料！'});
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                notyMsg(textStatus + " " + errorThrown, 'error');
            }
        });
    } else if (TYPE == "CLAIM") {
        $.ajax({
            type: "POST",
            url: 'WebServiceServlet',
            data: {ajaxAction: 'getOI2PersonByIDN', OP_PUCP_IDN: $('#IPUCLAIM_OP_PUCP_IDN').val().toUpperCase(), IDN_TYPE: TYPE},
            dataType: "json",
            success: function (data, textStatus, xhr) {
                if (data.formData != undefined) {
                    OP_OI_A1_ID = data.formData.A1_ID;
                    $('#IPUCLAIM_OP_PUCP_NAME').val(data.formData.A1_NAME);
                    $('#IPUCLAIM_OP_PUCP_RNAME').val(data.formData.A1_RNAME);
                    $('#IPUCLAIM_OP_PUCP_CITY_CD').val(data.formData.A1_CITY);
                    bindCOUNTRYDDL('3', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');
                    $('#IPUCLAIM_OP_PUCP_TOWN_CD').val(data.formData.A1_TOWN);
                    bindCOUNTRYDDL('4', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');
                    $('#IPUCLAIM_OP_PUCP_VILLAGE_CD').val(data.formData.A1_VILLAGE);
//                    $('#IPUCLAIM_OP_PUCP_LIN').val(fullToHalf(data.formData.A1_NEIGH));
//                    $('#IPUCLAIM_OP_PUCP_ROAD').val(fullToHalf(data.formData.A1_ROAD));
                    $('#IPUCLAIM_OP_PUCP_LIN').val(data.formData.A1_NEIGH);
                    $('#IPUCLAIM_OP_PUCP_ROAD').val(data.formData.A1_ROAD);
                } else {
                    $.alert.open({type: 'error', content: '查無資料！'});
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                notyMsg(textStatus + " " + errorThrown, 'error');
            }
        });
    }
}