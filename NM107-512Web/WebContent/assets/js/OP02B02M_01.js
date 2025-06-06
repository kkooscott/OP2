var ajaxActionUrl = 'PuMaintainServlet';

//照片上傳---START
function uploadImg(OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_DTL_SEQ_NO, OP_AN_SEQ_NO) {
    var fileList = new FormData();
    var file_data = $('input[type="file"]')[0].files; // for multiple files
    if (file_data.length > 0)
        fileList.append("file_" + 0, file_data[0]);
    var file_data = $('input[type="file"]')[1].files; // for multiple files
    if (file_data.length > 0)
        fileList.append("file_" + 1, file_data[0]);
    $.ajax({
        url: 'UploadServlet?PATH=' + $('#OP_UPL_FILE_PATH').val() + '&TYPE=IMAGES&PIC_TYPE=1&OP_UPL_FILE_NAME1=' + $('#OP_UPL_FILE_NAME1').val() + '&IPHOTO_OP_FILE_PATH1=' + $('#IPHOTO_OP_FILE_PATH1').val() + '&IPHOTO_OP_SEQ_NO1=' + $('#IPHOTO_OP_SEQ_NO1').val() + '&IPHOTO_DETELE1=' + $('#IPHOTO_DETELE1').val() + '&OP_UPL_FILE_NAME2=' + $('#OP_UPL_FILE_NAME2').val() + '&IPHOTO_OP_FILE_PATH2=' + $('#IPHOTO_OP_FILE_PATH2').val() + '&IPHOTO_OP_SEQ_NO2=' + $('#IPHOTO_OP_SEQ_NO2').val() + '&IPHOTO_DETELE2=' + $('#IPHOTO_DETELE2').val() + '&OP_BASIC_SEQ_NO=' + OP_BASIC_SEQ_NO + '&OP_AC_RCNO=' + OP_AC_RCNO + '&OP_DTL_SEQ_NO=' + OP_DTL_SEQ_NO + '&OP_AN_SEQ_NO=' + OP_AN_SEQ_NO,
        data: fileList,
        contentType: false,
        processData: false,
        async: false,
        type: 'POST',
        success: function (data) {
            //alert("success");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            notyMsg(textStatus + " " + errorThrown, 'error');
        }
    });
}
function DeleteImg(OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_DTL_SEQ_NO, OP_AN_SEQ_NO) {
    $.ajax({
        url: 'UploadServlet?PATH=' + $('#OP_UPL_FILE_PATH').val() + '&TYPE=IMAGES&PIC_TYPE=1&OP_UPL_FILE_NAME1=' + $('#OP_UPL_FILE_NAME1').val() + '&IPHOTO_OP_FILE_PATH1=' + $('#IPHOTO_OP_FILE_PATH1').val() + '&IPHOTO_OP_SEQ_NO1=' + $('#IPHOTO_OP_SEQ_NO1').val() + '&IPHOTO_DETELE1=' + $('#IPHOTO_DETELE1').val() + '&OP_UPL_FILE_NAME2=' + $('#OP_UPL_FILE_NAME2').val() + '&IPHOTO_OP_FILE_PATH2=' + $('#IPHOTO_OP_FILE_PATH2').val() + '&IPHOTO_OP_SEQ_NO2=' + $('#IPHOTO_OP_SEQ_NO2').val() + '&IPHOTO_DETELE2=' + $('#IPHOTO_DETELE2').val() + '&OP_BASIC_SEQ_NO=' + OP_BASIC_SEQ_NO + '&OP_AC_RCNO=' + OP_AC_RCNO + '&OP_DTL_SEQ_NO=' + OP_DTL_SEQ_NO + '&OP_AN_SEQ_NO=' + OP_AN_SEQ_NO,
        contentType: false,
        processData: false,
        async: false,
        type: 'POST',
        success: function (data) {
            //alert("success");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            notyMsg(textStatus + " " + errorThrown, 'error');
        }
    });
}
function preview(input, Item) {

    if (input.files && input.files[0]) {
        var filepath = input.value.split('\\');
        var filename = filepath[filepath.length - 1];   // 取得檔名
        var ext = /\.(jpg|gif|bmp|jpeg|png|tif)$/i;     // 設定要篩選出的副檔名
        var result = ext.test(filename);                // 檔案檢查結果
        if (!result) {
            $.alert.open('error', "請選擇正確照片格式!!!(附檔名為：jpg, gif, bmp, jpeg, png, tif)");
            input.value = "";
            return;
        }
        if ($('#file1').val().split('\\').pop() == $('#file2').val().split('\\').pop()) {
            $.alert.open('error', "不能選擇相同照片!!!");
            input.value = "";
            return;
        }
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#previewPhoto' + Item).attr('src', e.target.result);
            var KB = format_float(e.total / 1024, 2);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
function format_float(num, pos)
{
    var size = Math.pow(10, pos);
    return Math.round(num * size) / size;
}
//照片上傳---END
//照片預覽---START
function getPicture(DtlSeqNo) {
    $.ajax({
        url: 'PuPicServlet',
        type: 'POST',
        datatype: 'json',
        data: {
            ajaxAction: 'GET_PICTURE',
            OP_DTL_SEQ_NO: DtlSeqNo,
            FLAG_LOG: 'beforeModifyPic'
        },
        async: false,
        success: function (data, textStatus, xhr) {
            $('#file1').val('');
            $('#file2').val('');
            $('#previewPhoto1').attr('src', 'assets/img/file-upload-with-preview.png');
            $('#previewPhoto2').attr('src', 'assets/img/file-upload-with-preview.png');
            if (data.result.length > 0) {
                for (var i = 0; i < data.result.length; i++) {
                    $('#previewPhoto' + (i + 1)).attr('src', data.result[i].OP_FILE_PATH);
                    $('#OP_UPL_FILE_NAME' + (i + 1)).val(data.result[i].OP_UPL_FILE_NAME);
                    $('#IPHOTO_OP_SEQ_NO' + (i + 1)).val(data.result[i].OP_SEQ_NO);
                    $('#IPHOTO_OP_FILE_PATH' + (i + 1)).val(data.result[i].OP_FILE_PATH_ORG);
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            //console.log(textStatus);
        }
    });
}
//照片預覽---END

//DETAIL_CHECK---START
function checkDetailList(inForm) {
    Validator.init(inForm);
    //202410 Validator.checkLength  變 Validator.checkLength_New
    Validator.checkLength_New("IPUDETAIL_OP_PUOJ_NM", true, "物品名稱", 20);
    Validator.checkNumber("IPUDETAIL_OP_QTY", true, "數量", 0, 99999999.99);
    Validator.checkLength_New("IPUDETAIL_OP_QTY_UNIT", true, "單位", 6);
    Validator.required('IPUDETAIL_OP_TYPE_CD', '物品種類');
    Validator.checkLength_New("IPUDETAIL_OP_FEATURE", false, "特徵", 100);
    Validator.checkLength_New("IPUDETAIL_OP_REMARK", false, "備考", 100);
    Validator.checkIMEI("IPUDETAIL_OP_IMEI_CODE", false, "IMEI碼");
    Validator.checkLength_New("IPUDETAIL_OP_MAC_ADDR", false, "MAC碼", 30);
    Validator.checkIMEI("IPUDETAIL_OP_IMEI_CODE_2", false, "IMEI碼2");
    Validator.checkPhone("IPUDETAIL_OP_PHONE_NUMBER", false, "電信門號");
    Validator.checkPhone("IPUDETAIL_OP_PHONE_NUMBER_2", false, "電信門號2");
    
    if (Validator.isValid())
        return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//DETAIL_CHECK---END

//CHECK---START
function checkYNDetail(OP_BASIC_SEQ_NO) {
    var bool = 'Y';
    var ajData = {
        'ajaxAction': 'checkYNDetail',
        'OP_BASIC_SEQ_NO': OP_BASIC_SEQ_NO,
    };
    var ajSucc = function (JData) {
        if (JData.formData) {
            bool = 'Y';
        } else
            bool = 'N';
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

//DETAIL_SHOW---START
function showDetailGridList(OP_BASIC_SEQ_NO, OP_AC_RCNO) {
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
            showDetailValue(OP_SEQ_NO);
        }
    });
    $(gridId).jqGrid("clearGridData", true);
    $(gridId).setGridWidth($(window).width() - 92);
    $(gridId).trigger("reloadGrid");
}
function showDetailValue(OP_SEQ_NO) {
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

//DETAIL_SAVE---START
function confirmDetailAndSave(ACTION) {
    var bool = true;
    if (bool) {
        show_BlockUI_page_noParent('資料儲存中…');
        var ajData = {
            'ajaxAction': $('#actionDetailType').val(),
            'OP_SEQ_NO': $('#IPUDETAIL_OP_SEQ_NO').val(),
            'OP_BASIC_SEQ_NO': $('#IPUBASIC_OP_SEQ_NO').val(),
            'OP_AC_RCNO': $('#IPUDETAIL_OP_AC_RCNO').val(),
            'OP_TYPE_CD': $('#IPUDETAIL_OP_TYPE_CD option:selected').val(),
            'OP_TYPE_NM': $('#IPUDETAIL_OP_TYPE_CD').val() == '' ? '' : $('#IPUDETAIL_OP_TYPE_CD option:selected').text(),
            'OP_PUOJ_NM': $('#IPUDETAIL_OP_PUOJ_NM').val(),
            'OP_QTY': $('#IPUDETAIL_OP_QTY').val(),
            'OP_QTY_UNIT': $('#IPUDETAIL_OP_QTY_UNIT').val(),
            'OP_COLOR_CD': $('#IPUDETAIL_OP_COLOR_CD').val(),
            'OP_COLOR_NM': $('#IPUDETAIL_OP_COLOR_CD').val() == '' ? '' : $('#IPUDETAIL_OP_COLOR_CD option:selected').text(),
            'OP_BRAND_CD': $('#IPUDETAIL_OP_BRAND_CD').val(),
            'OP_BRAND_NM': $('#IPUDETAIL_OP_BRAND_CD').val() == '' ? '' : $('#IPUDETAIL_OP_BRAND_CD option:selected').text(),
            'OP_FEATURE': $('#IPUDETAIL_OP_FEATURE').val(),
            'OP_IMEI_CODE': $('#IPUDETAIL_OP_IMEI_CODE').val(),
            'OP_IMEI_CODE_2': $('#IPUDETAIL_OP_IMEI_CODE_2').val(),
            'OP_MAC_ADDR': $('#IPUDETAIL_OP_MAC_ADDR').val(),
            'OP_PHONE_NUM': $('#IPUDETAIL_OP_PHONE_NUMBER').val(),
            'OP_PHONE_NUM_2': $('#IPUDETAIL_OP_PHONE_NUMBER_2').val(),
            'OP_REMARK': $('#IPUDETAIL_OP_REMARK').val(),
            'ACTION': ACTION
        };
        var ajSucc = function (JData) {
            if (JData.formData) {
                var formData = JData.formData[0];
                if ($('#actionDetailType').val() == 'insertDetailList') {
                    notyMsg('新增成功');
                    $('#actionDetailType').val('insertDetailList');
                    $("#editDetailBtn").hide();
                    $("#deleteDetailBtn").hide();
                    uploadImg(formData['OP_BASIC_SEQ_NO'], formData['OP_AC_RCNO'], formData['OP_SEQ_NO'], "0");
                    clearDetailData();
                } else if ($('#actionDetailType').val() == 'updateDetailList') {
                    notyMsg('修改成功');
                    $('#actionDetailType').val('insertDetailList');
                    $("#editDetailBtn").hide();
                    $("#deleteDetailBtn").hide();
                    uploadImg(formData['OP_BASIC_SEQ_NO'], formData['OP_AC_RCNO'], formData['OP_SEQ_NO'], "0");
                    var formData = JData.formData[0];
                    clearDetailData();
                }
                $.unblockUI();
                showDetailGridList_OW($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUDETAIL_OP_AC_RCNO').val());
            } else {
                $('#actionDetailType').val() == 'insertDetailList' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
                $.unblockUI();
            }
        };
        var ajErr = function () {
            $('#actionDetailType').val() == 'insertDetailList' ? $.alert.open('error', "新增失敗!!!") : $.alert.open('error', "修改失敗!!!");
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
//DETAIL_SAVE---END

//DETAIL_DELETE---START
function deleteDetail() {
    show_BlockUI_page_noParent('資料刪除中…');
    var ajData = {
        'ajaxAction': $('#actionDetailType').val(),
        'OP_SEQ_NO': $('#IPUDETAIL_OP_SEQ_NO').val(),
    };
    var ajSucc = function (JData) {
        notyMsg('刪除成功');
        $('#actionDetailType').val('insertDetailList');
        $('#IPUDETAIL_OP_SEQ_NO').val('');
        $('#addDetailBtn').show();
        $('#editDetailBtn').hide();
        $('#deleteDetailBtn').hide();
        $('#IPHOTO_DETELE1').val('1');
        $('#IPHOTO_DETELE2').val('1');
        DeleteImg('0', '0', '0', '0');
        showDetailGridList_OW($('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUDETAIL_OP_AC_RCNO').val());
        clearDetailData();
        $.unblockUI();
    };
    var ajErr = function () {
        $.alert.open('error', "刪除資料失敗!!!");
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
//DETAIL_DELETE---END

//同物品種類按鈕
function DoSameAsIPUDETAIL_OP_TYPE_CD() {
    if ($('#IPUDETAIL_OP_TYPE_CD').val() != "") {
        var str = $('#IPUDETAIL_OP_TYPE_CD option:selected').text();
        var A = str.substring(5);
        $('#IPUDETAIL_OP_PUOJ_NM').val(A);
    }
}