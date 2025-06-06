var ajaxActionUrl = 'PuMaintainServlet';

//ANN2_SHOW---START
function showAnn2GridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料準備中…');
    var ajData = {
                    'ajaxAction' : 'showAnn2GridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    $('#OP_AC2_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                    bindUNITDDL('3','OP_AC2_unitLevel2','OP_AC2_unitLevel3','OP_AC2_unitLevel4','');
                    $('#OP_AC2_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
                    bindUNITDDL('4','OP_AC2_unitLevel2','OP_AC2_unitLevel3','OP_AC2_unitLevel4','');
                    $('#OP_AC2_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                    $('#IANNOUNCE_OP_AC_DATE2').val( formData['OP_AC_DATE'] );
                    $('#IPUBASIC_OP_AC_UNIT_TEL2').val( formData['OP_AC_UNIT_TEL'] );
                    
                    $('#IANNOUNCE_OP_DOC_WD2').val( formData['OP_DOC_WD'] );
                    $('#IANNOUNCE_OP_DOC_NO2').val( formData['OP_DOC_NO'] );
                    $('#IANNOUNCE_OP_AN_DATE_BEG2').val( formData['OP_AN_DATE_BEG'] );
                    $('#IANNOUNCE_OP_AN_CONTENT2').val( formData['OP_AN_CONTENT'] );
                    $('#IANNOUNCE_OP_AC_UNIT_ALL').val( formData['OP_AN_B_UNIT_NM'] ); //先暫時到分局
                    $('#IPUBASIC_OP_PU_DATE2').val( formData['OP_PU_DATE'] );
                    $('#IPUBASIC_OP_PU_TIME2').val( formData['OP_PU_TIME'] );
                    $('#IPUBASIC_OP_PU_CITY_CD2').val( formData['OP_PU_CITY_CD'] );
                    bindCOUNTRYDDL('3', 'IPUBASIC_OP_PU_CITY_CD2', 'IPUBASIC_OP_PU_TOWN_CD2', '', '', '');
                    $('#IPUBASIC_OP_PU_TOWN_CD2').val( formData['OP_PU_TOWN_CD'] );
                    $('#IPUBASIC_OP_PU_PLACE2').val( formData['OP_PU_PLACE'] );
                    $('#IANNOUNCE_OP_AN_DATE_END2').text( formData['OP_AN_DATE_END'] );
                    getPictureForAnn(formData['OP_SEQ_NO']);
                    $.unblockUI();
                }else{
                    $.alert.open('error', "選取資料失敗!!!");
                    $.unblockUI();
                }
            };
            var ajErr = function() {
                    $.alert.open('error', "選取資料失敗!!!");
                    $.unblockUI();
            };

            $.ajax({
                    url : 'AnnounceServlet',
                    type : "POST",
                    dataType : "json",
                    data : ajData,
                    success : ajSucc,
                    error : ajErr
            });
			
}
//ANN2_SHOW---END

//照片上傳---START
function uploadImgForAnn(OP_BASIC_SEQ_NO, OP_AC_RCNO, OP_DTL_SEQ_NO, OP_AN_SEQ_NO) {
    var fileList = new FormData();
    var file_data = $('input[type="file"]')[2].files; // for multiple files
    if( file_data.length > 0 )
        fileList.append("file_" + 0, file_data[0]);
    var file_data = $('input[type="file"]')[3].files; // for multiple files
    if( file_data.length > 0 )
        fileList.append("file_" + 1, file_data[0]);
    $.ajax({
        url: 'UploadServlet?PATH=' + $('#OP_UPL_FILE_PATH').val() + '&TYPE=IMAGES&PIC_TYPE=2&OP_UPL_FILE_NAME1=' + $('#OP_UPL_FILE_NAME3').val()+'&IPHOTO_OP_FILE_PATH1=' + $('#IPHOTO_OP_FILE_PATH3').val() + '&IPHOTO_OP_SEQ_NO1='+ $('#IPHOTO_OP_SEQ_NO3').val() +'&IPHOTO_DETELE1='+ $('#IPHOTO_DETELE3').val() +'&OP_UPL_FILE_NAME2=' + $('#OP_UPL_FILE_NAME4').val() + '&IPHOTO_OP_FILE_PATH2=' + $('#IPHOTO_OP_FILE_PATH4').val() + '&IPHOTO_OP_SEQ_NO2='+ $('#IPHOTO_OP_SEQ_NO4').val() +'&IPHOTO_DETELE2='+ $('#IPHOTO_DETELE4').val() + '&OP_BASIC_SEQ_NO=' + OP_BASIC_SEQ_NO + '&OP_AC_RCNO=' + OP_AC_RCNO + '&OP_DTL_SEQ_NO=' + OP_DTL_SEQ_NO + '&OP_AN_SEQ_NO=' + OP_AN_SEQ_NO,
        data: fileList,
        contentType: false,
        processData: false,
        async : false,
        type: 'POST',
        success: function (data) {
            //alert("success");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            notyMsg(textStatus + " " + errorThrown, 'error');
        }
    });
}
function DeleteImgForAnn(OP_BASIC_SEQ_NO, OP_AC_RCNO) {
    $.ajax({
        url: 'UploadServlet?TYPE=ANN2_DELETE&OP_BASIC_SEQ_NO='+OP_BASIC_SEQ_NO+"&OP_AC_RCNO="+OP_AC_RCNO,
        contentType: false,
        processData: false,
        async : false,
        type: 'POST',
        success: function (data) {
            //alert("success");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            notyMsg(textStatus + " " + errorThrown, 'error');
        }
    });
}
function previewForAnn(input,Item) {
    
    if (input.files && input.files[0]) {
        var filepath = input.value.split('\\');
        var filename = filepath[filepath.length - 1];   // 取得檔名
        var ext = /\.(jpg|gif|bmp|jpeg|png|tif)$/i;     // 設定要篩選出的副檔名
        var result = ext.test(filename);                // 檔案檢查結果
        if (!result) {
            $.alert.open('error', "請選擇正確照片格式!!!(附檔名為：jpg, gif, bmp, jpeg, png, tif)");
            input.value="";
            return;
        }
        if ( $('#file3').val().split('\\').pop() == $('#file4').val().split('\\').pop()) {
            $.alert.open('error', "不能選擇相同照片!!!");
            input.value="";
            return;
        }
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#previewPhoto' + Item ).attr('src', e.target.result);
            var KB = format_float(e.total / 1024, 2);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
//照片上傳---END
//照片預覽---START
function getPictureForAnn(AnSeqNo){
    $.ajax({
        url:'PuPicServlet',
        type : 'POST',
        datatype : 'json',
        data : {
            ajaxAction : 'GET_PICTURE',
            OP_AN_SEQ_NO : AnSeqNo,
            FLAG_LOG: 'beforeModifyPic'
        },
        async : false,
        success : function(data, textStatus, xhr) {
            $('#file3').val('');
            $('#file4').val('');
            $('#previewPhoto3').attr('src','assets/img/file-upload-with-preview.png');
            $('#previewPhoto4').attr('src','assets/img/file-upload-with-preview.png');
            if(data.result.length > 0){
                for(var i=0; i<data.result.length;i++){
                    $('#previewPhoto' + (i+3)).attr('src', data.result[i].OP_FILE_PATH );
                    $('#OP_UPL_FILE_NAME' + (i+3)).val(data.result[i].OP_UPL_FILE_NAME );
                    $('#IPHOTO_OP_SEQ_NO' + (i+3)).val(data.result[i].OP_SEQ_NO );
                    $('#IPHOTO_OP_FILE_PATH' + (i+3)).val(data.result[i].OP_FILE_PATH_ORG );
                }
            }
        },
        error : function(jqXHR,textStatus,errorThrown){
            //console.log(textStatus);
        } 
    });
}
//照片預覽---END

function Ann2Button(){
        //產生網路公告
	$('#getAnnounceBtn2').click(function() {
            getAnn2GridList($('#IPUBASIC_OP_SEQ_NO').val());
        });
        //發佈網路公告
	$('#saveAnnounceBtn2').click(function() {
            saveAnn2($('#IPUBASIC_OP_SEQ_NO').val());
        });
        // 照片上傳
        $('#photo3SubmitBtn').click(function() {
            $('#file3').click();
        });
        $('#photo4SubmitBtn').click(function() {
            $('#file4').click();
        });
        // 照片刪除
        $('#photo3DeleteBtn').click(function() {
            $('#previewPhoto3').attr('src', 'assets/img/file-upload-with-preview.png');
            $('#file3').val('');
            $('#IPHOTO_DETELE3').val('1');
        });
        $('#photo4DeleteBtn').click(function() {
            $('#previewPhoto4').attr('src', 'assets/img/file-upload-with-preview.png');
            $('#file4').val('');
            $('#IPHOTO_DETELE4').val('1');
        });
}

//ANN2_SHOW---START
function getAnn2GridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
                    'ajaxAction' : 'getAnn2GridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    $('#IANNOUNCE_OP_SEQ_NO2').val( formData['OP_SEQ_NO'] );
                    $('#OP_AC2_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                    bindUNITDDL('3','OP_AC2_unitLevel2','OP_AC2_unitLevel3','OP_AC2_unitLevel4','');
                    $('#OP_AC2_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
                    bindUNITDDL('4','OP_AC2_unitLevel2','OP_AC2_unitLevel3','OP_AC2_unitLevel4','');
                    $('#OP_AC2_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                    $('#IANNOUNCE_OP_AC_DATE2').val( formData['OP_AC_DATE'] );
                    $('#IPUBASIC_OP_AC_UNIT_TEL2').val( formData['OP_AC_UNIT_TEL'] );
                    
                    $('#IANNOUNCE_OP_DOC_WD2').val( formData['OP_DOC_WD'] );
                    $('#IANNOUNCE_OP_DOC_NO2').val( formData['OP_DOC_NO'] );
                    $('#IANNOUNCE_OP_AN_DATE_BEG2').val( formData['OP_AN_DATE_BEG'] );
                    $('#IANNOUNCE_OP_AN_CONTENT2').val( formData['OP_AN_CONTENT'] );
                    $('#IANNOUNCE_OP_AC_UNIT_ALL').val( formData['OP_AN_B_UNIT_NM'] ); //先暫時到分局
                    $('#IPUBASIC_OP_PU_DATE2').val( formData['OP_PU_DATE'] );
                    $('#IPUBASIC_OP_PU_TIME2').val( formData['OP_PU_TIME'] );
                    $('#IPUBASIC_OP_PU_CITY_CD2').val( formData['OP_PU_CITY_CD'] );
                    bindCOUNTRYDDL('3', 'IPUBASIC_OP_PU_CITY_CD2', 'IPUBASIC_OP_PU_TOWN_CD2', '', '', '');
                    $('#IPUBASIC_OP_PU_TOWN_CD2').val( formData['OP_PU_TOWN_CD'] );
                    $('#IPUBASIC_OP_PU_PLACE2').val( formData['OP_PU_PLACE'] );
                    $('#IANNOUNCE_OP_AN_DATE_END2').text( formData['OP_AN_DATE_END'] );
                    getPictureForAnn(formData['OP_SEQ_NO']);
                    $.unblockUI();
                }else{
                    $.alert.open('error', "選取資料失敗!!!");
                    $.unblockUI();
                }
            };
            var ajErr = function() {
                    $.alert.open('error', "選取資料失敗!!!");
                    $.unblockUI();
            };

            $.ajax({
                    url : 'AnnounceServlet',
                    type : "POST",
                    dataType : "json",
                    data : ajData,
                    success : ajSucc,
                    error : ajErr
            });
			
}
//ANN2_SHOW---END

//ANN2_SAVE---START
function saveAnn2(){
    var bool = true;
    if (bool){
        show_BlockUI_page_noParent('資料儲存中…');
            var ajData = {
                            'ajaxAction' : 'updateAnn2List',
                            'OP_SEQ_NO' : $('#IANNOUNCE_OP_SEQ_NO2').val(),
                            'OP_BASIC_SEQ_NO' : $('#IPUBASIC_OP_SEQ_NO').val()
                    };
                    var ajSucc = function(JData) {
                        if (JData.formData){
                            var formData = JData.formData[0];
                            notyMsg('儲存成功');
                            uploadImgForAnn( $('#IPUBASIC_OP_SEQ_NO').val() , $('#IPUBASIC_OP_AC_RCNO').val(), "0", formData['OP_SEQ_NO']);
                            $('#getAnnounceBtn2').hide();
                            $('#saveAnnounceBtn2').hide();
                            disableAll('IANNOUNCE2');
                        }else{
                            $.alert.open('error', "儲存失敗!!!");
                        }
                        $.unblockUI();
                    };
                    var ajErr = function() {
                            $.alert.open('error', "儲存失敗!!!");
                            $.unblockUI();
                    };

                    $.ajax({
                            url : 'AnnounceServlet',
                            type : "POST",
                            dataType : "json",
                            data : ajData,
                            success : ajSucc,
                            error : ajErr
                    });
    }
}
//ANN2_SAVE---END