
//ANN1_SHOW---START
function showAnn1GridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料準備中…');
    var ajData = {
                    'ajaxAction' : 'showAnn1GridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    $('#OP_AC1_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                    bindUNITDDL('3','OP_AC1_unitLevel2','OP_AC1_unitLevel3','OP_AC1_unitLevel4','');
                    $('#OP_AC1_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
                    bindUNITDDL('4','OP_AC1_unitLevel2','OP_AC1_unitLevel3','OP_AC1_unitLevel4','');
                    $('#OP_AC1_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                    $('#IANNOUNCE_OP_AC_DATE').val( formData['OP_AC_DATE'] );
                    $('#OP_AN_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                    bindUNITDDL('3','OP_AN_unitLevel2','OP_AN_unitLevel3','','');
                    $('#OP_AN_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
//                    bindUNITDDL('4','OP_AN_unitLevel2','OP_AN_unitLevel3','OP_AN_unitLevel4','');
//                    $('#OP_AN_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                    $('#IANNOUNCE_OP_AN_DATE_BEG').val( formData['OP_AN_DATE_BEG'] );       
                    $('#IANNOUNCE_OP_CABINET_NO').val( formData['OP_CABINET_NO'] );
                    $('#IANNOUNCE_OP_DOC_DT').val( formData['OP_DOC_DT'] );
                    $('#IANNOUNCE_OP_DOC_WD').val( formData['OP_DOC_WD'] );
                    $('#IANNOUNCE_OP_DOC_NO').val( formData['OP_DOC_NO'] );
                    $('#IANNOUNCE_OP_AN_DATE_END').val( formData['OP_AN_DATE_END'] );
                    $('#IANNOUNCE_OP_AN_CONTENT').val( formData['OP_AN_CONTENT'] );
                    $('#IANNOUNCE_OP_AN_REMARK').val( formData['OP_AN_REMARK'] );
                    
                    $('#IANNOUNCE_OP_KP_UNIT_NM').val( formData['OP_KP_UNIT_NM'] );
                    $('#IANNOUNCE_OP_KP_NM').val( formData['OP_KP_NM'] );
                    $('#IANNOUNCE_OP_KP_DATE').val( formData['OP_KP_DATE'] );
                    $('#IANNOUNCE_OP_KP_TIME').val( formData['OP_KP_TIME'] );
                    $('#IANNOUNCE_OP_KP_REMARK').val( formData['OP_KP_REMARK'] );
                    
                    $('#IANNOUNCE_OP_KP_UNIT_NM1').val( formData['OP_KP_UNIT_NM1'] );
                    $('#IANNOUNCE_OP_KP_NM1').val( formData['OP_KP_NM1'] );
                    $('#IANNOUNCE_OP_KP_DATE1').val( formData['OP_KP_DATE1'] );
                    $('#IANNOUNCE_OP_KP_TIME1').val( formData['OP_KP_TIME1'] );
                    $('#IANNOUNCE_OP_KP_REMARK1').val( formData['OP_KP_REMARK1'] );
                    $('#IANNOUNCE_OP_KP_UNIT_NM2').val( formData['OP_KP_UNIT_NM2'] );
                    $('#IANNOUNCE_OP_KP_NM2').val( formData['OP_KP_NM2'] );
                    $('#IANNOUNCE_OP_KP_DATE2').val( formData['OP_KP_DATE2'] );
                    $('#IANNOUNCE_OP_KP_TIME2').val( formData['OP_KP_TIME2'] );
                    $('#IANNOUNCE_OP_KP_REMARK2').val( formData['OP_KP_REMARK2'] );
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
//ANN1_SHOW---END
//ANN1_CHECK---START
function checkAnn1List(inForm) {
    Validator.init(inForm);
    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    //發文日期
    if( getADDate($('#IANNOUNCE_OP_DOC_DT').val().substring(0,9)) >getADDate ( nowDate) ){
        Validator.setMessage("欄位 [ 發文日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IANNOUNCE_OP_DOC_DT");
    }
    Validator.required('IANNOUNCE_OP_DOC_DT', '發文日期');
    
    Validator.required('IANNOUNCE_OP_KP_UNIT_NM', '保管單位');
    Validator.required('IANNOUNCE_OP_KP_NM', '保管單位承辦人');
    Validator.required('IANNOUNCE_OP_KP_DATE', '收受拾得物時間');
    Validator.required('IANNOUNCE_OP_KP_TIME', '收受拾得物時間');
    
    Validator.checkLength("IANNOUNCE_OP_DOC_WD",true,"發文文號-字",20);
    Validator.checkLength("IANNOUNCE_OP_DOC_NO",true,"發文文號-號",11);
    Validator.checkLength("IANNOUNCE_OP_AN_CONTENT",true,"公告內容",600);
    Validator.checkLength("IANNOUNCE_OP_AN_REMARK",false,"備註",100);
    Validator.checkLength("IANNOUNCE_OP_KP_REMARK",false,"備註",100);
    Validator.checkLength("IANNOUNCE_OP_KP_REMARK1",false,"備註",100);
    Validator.checkLength("IANNOUNCE_OP_KP_REMARK2",false,"備註",100);
    
    if ( Validator.isValid() )
    	return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//ANN1_CHECK---END

//CHECK---START
function checkYNAnn1( OP_BASIC_SEQ_NO ) {
    var bool;
    var ajData = {
            'ajaxAction' : 'checkYNAnn1',
            'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
    };
    var ajSucc = function(JData) {
        if (JData.formData){
            var formData = JData.formData[0];
            bool = formData['CHECK'];
        }
    };
    var ajErr = function() {
        
    };

    $.ajax({
            url : 'AnnounceServlet',
            type : "POST",
            dataType : "json",
            async : false,
            data : ajData,
            success : ajSucc,
            error : ajErr
    });
    return bool;
}
//CHECK---END

//ANN1_SHOW---START
function getAnn1GridList(OP_BASIC_SEQ_NO, ACTION) {
    var strPermissionData = getUserRole();
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
                    'ajaxAction' : 'getAnn1GridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    if( formData['ACTION_TYPE'] == "insertAnn1List" ){
                        $('#actionAnn1Type').val( formData['ACTION_TYPE'] );
                        $('#IANNOUNCE_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        $('#OP_AC1_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                        bindUNITDDL('3','OP_AC1_unitLevel2','OP_AC1_unitLevel3','OP_AC1_unitLevel4','');
                        $('#OP_AC1_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
                        bindUNITDDL('4','OP_AC1_unitLevel2','OP_AC1_unitLevel3','OP_AC1_unitLevel4','');
                        $('#OP_AC1_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                        $('#IANNOUNCE_OP_DOC_WD').val( formData['OP_DOC_WD'] );
                        $('#IANNOUNCE_OP_DOC_NO').val( formData['OP_DOC_NO'] );
                        $('#OP_YN_GET_NO1').val( formData['OP_YN_GET_NO1'] );
                        if(ACTION == "OP03A01Q"){
                            if( formData['OP_YN_GET_NO1'] == "1" ){
                                $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                                $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true); 
                            }else{
                                $('#IANNOUNCE_OP_DOC_WD').prop('disabled', false); 
                                $('#IANNOUNCE_OP_DOC_NO').prop('disabled', false); 
                            }
                        }else if( ACTION == "OP05A01Q" ){
//                            if ( strPermissionData["Roles"].toString().indexOf('OP300004') > -1 ){ //警局承辦人
//                                if( formData['OP_YN_GET_NO1'] == "1" ){
//                                    $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
//                                    $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true); 
//                                }else{
//                                    $('#IANNOUNCE_OP_DOC_WD').prop('disabled', false); 
//                                    $('#IANNOUNCE_OP_DOC_NO').prop('disabled', false); 
//                                }
//                            }else{
//                                $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
//                                $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true);
//                            }
                            $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                            $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true);
                        }else{
                            $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                            $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true);
                        }
                        $('#IANNOUNCE_OP_AC_DATE').val( formData['OP_AC_DATE'] );
                        $('#IANNOUNCE_OP_AN_CONTENT').val( formData['OP_AN_CONTENT'] );
                        $('#IANNOUNCE_OP_CABINET_NO').val( formData['OP_CABINET_NO'] );
                        $('#IANNOUNCE_OP_KP_UNIT_NM').val( formData['OP_KP_UNIT_NM'] );
                        $('#IANNOUNCE_OP_KP_NM').val( formData['OP_KP_NM'] );
                        $('#IANNOUNCE_OP_KP_DATE').val( formData['OP_KP_DATE'] );
                        $('#IANNOUNCE_OP_KP_TIME').val( formData['OP_KP_TIME'] );
                        $('#IANNOUNCE_OP_KP_REMARK').val( formData['OP_KP_REMARK'] );
                    
                        $('#IANNOUNCE_OP_KP_UNIT_NM1').val( formData['OP_KP_UNIT_NM1'] );
                        $('#IANNOUNCE_OP_KP_NM1').val( formData['OP_KP_NM1'] );
                        $('#IANNOUNCE_OP_KP_DATE1').val( formData['OP_KP_DATE1'] );
                        $('#IANNOUNCE_OP_KP_TIME1').val( formData['OP_KP_TIME1'] );
                        $('#IANNOUNCE_OP_KP_REMARK1').val( formData['OP_KP_REMARK1'] );
                        $('#IANNOUNCE_OP_KP_UNIT_NM2').val( formData['OP_KP_UNIT_NM2'] );
                        $('#IANNOUNCE_OP_KP_NM2').val( formData['OP_KP_NM2'] );
                        $('#IANNOUNCE_OP_KP_DATE2').val( formData['OP_KP_DATE2'] );
                        $('#IANNOUNCE_OP_KP_TIME2').val( formData['OP_KP_TIME2'] );
                        $('#IANNOUNCE_OP_KP_REMARK2').val( formData['OP_KP_REMARK2'] );
                    }else{
                        $('#actionAnn1Type').val( formData['ACTION_TYPE'] );
                        $('#IANNOUNCE_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        $('#OP_AC1_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                        bindUNITDDL('3','OP_AC1_unitLevel2','OP_AC1_unitLevel3','OP_AC1_unitLevel4','');
                        $('#OP_AC1_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
                        bindUNITDDL('4','OP_AC1_unitLevel2','OP_AC1_unitLevel3','OP_AC1_unitLevel4','');
                        $('#OP_AC1_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                        $('#IANNOUNCE_OP_AC_DATE').val( formData['OP_AC_DATE'] );
                        $('#OP_AN_unitLevel2').val(formData['OP_AC_D_UNIT_CD'] );
                        bindUNITDDL('3','OP_AN_unitLevel2','OP_AN_unitLevel3','','');
                        $('#OP_AN_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
//                        bindUNITDDL('4','OP_AN_unitLevel2','OP_AN_unitLevel3','OP_AN_unitLevel4','');
//                        $('#OP_AN_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                        $('#IANNOUNCE_OP_AN_DATE_BEG').val( formData['OP_AN_DATE_BEG'] );
                        $('#IANNOUNCE_OP_DOC_DT').val( formData['OP_DOC_DT'] );
                        $('#IANNOUNCE_OP_DOC_WD').val( formData['OP_DOC_WD'] );
                        $('#IANNOUNCE_OP_DOC_NO').val( formData['OP_DOC_NO'] );
                        $('#OP_YN_GET_NO1').val( formData['OP_YN_GET_NO1'] );
                        if(ACTION == "OP03A01Q"){
                            if( formData['OP_YN_GET_NO1'] == "1" ){
                                $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                                $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true); 
                            }else{
                                $('#IANNOUNCE_OP_DOC_WD').prop('disabled', false); 
                                $('#IANNOUNCE_OP_DOC_NO').prop('disabled', false); 
                            }
                        }else if( ACTION == "OP05A01Q" ){
                            if ( strPermissionData["Roles"].toString().indexOf('OP300004') > -1 ){ //警局承辦人
                                if( formData['OP_YN_GET_NO1'] == "1" ){
                                    $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                                    $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true); 
                                }else{
                                    $('#IANNOUNCE_OP_DOC_WD').prop('disabled', false); 
                                    $('#IANNOUNCE_OP_DOC_NO').prop('disabled', false); 
                                }
                            }else{
                                $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                                $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true);
                            }
                        }else{
                            $('#IANNOUNCE_OP_DOC_WD').prop('disabled', true); 
                            $('#IANNOUNCE_OP_DOC_NO').prop('disabled', true);
                        }
                        $('#IANNOUNCE_OP_AN_DATE_END').val( formData['OP_AN_DATE_END'] );
                        $('#IANNOUNCE_OP_AN_CONTENT').val( formData['OP_AN_CONTENT'] );
                        $('#IANNOUNCE_OP_AN_REMARK').val( formData['OP_AN_REMARK'] );
                        $('#IANNOUNCE_OP_CABINET_NO').val( formData['OP_CABINET_NO'] );
                        $('#IANNOUNCE_OP_KP_UNIT_NM').val( formData['OP_KP_UNIT_NM'] );
                        $('#IANNOUNCE_OP_KP_NM').val( formData['OP_KP_NM'] );
                        $('#IANNOUNCE_OP_KP_DATE').val( formData['OP_KP_DATE'] );
                        $('#IANNOUNCE_OP_KP_TIME').val( formData['OP_KP_TIME'] );
                        $('#IANNOUNCE_OP_KP_REMARK').val( formData['OP_KP_REMARK'] );
                        $('#IANNOUNCE_OP_KP_UNIT_NM1').val( formData['OP_KP_UNIT_NM1'] );
                        $('#IANNOUNCE_OP_KP_NM1').val( formData['OP_KP_NM1'] );
                        $('#IANNOUNCE_OP_KP_DATE1').val( formData['OP_KP_DATE1'] );
                        $('#IANNOUNCE_OP_KP_TIME1').val( formData['OP_KP_TIME1'] );
                        $('#IANNOUNCE_OP_KP_REMARK1').val( formData['OP_KP_REMARK1'] );
                        $('#IANNOUNCE_OP_KP_UNIT_NM2').val( formData['OP_KP_UNIT_NM2'] );
                        $('#IANNOUNCE_OP_KP_NM2').val( formData['OP_KP_NM2'] );
                        $('#IANNOUNCE_OP_KP_DATE2').val( formData['OP_KP_DATE2'] );
                        $('#IANNOUNCE_OP_KP_TIME2').val( formData['OP_KP_TIME2'] );
                        $('#IANNOUNCE_OP_KP_REMARK2').val( formData['OP_KP_REMARK2'] );
                    }
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
//ANN1_SHOW---END

function Ann1Button(){
    //儲存並發佈內部公告
    $('#saveAnnounceBtn').click(function() {
        if (checkAnn1List(document.getElementById('form1'))){
                confirmAnn1AndSave();
        }
    });
}

//ANN1_SAVE---START
function confirmAnn1AndSave(){
    var bool = true;
    
    if( bool ){
        var check = checkYNAnn1($('#IPUBASIC_OP_SEQ_NO').val());
        if( check == '2' ){ //網路公告 2
            $.alert.open({
		type : 'confirm',
                async : false,
		content : '此拾得遺失物案件已發佈網路公告，若您要重新儲存內部公告資料，則原網路公告會被撤除。若此案件仍需要網路公告，儲存內部公告後，需至網路公告資料頁籤，再重按一次產生網路公告按鈕，並再重新發佈網路公告。確定要儲存內部公告資料嗎？',
		callback : function(button, value) {
			if (button == 'yes') 
			    saveAnn1();
		}
            });
        }else{
            saveAnn1();
        }	
    }
}

function saveAnn1(){
    var bool = true;
    var OP_Level_NM1, OP_Level_NM2, OP_Level_NM3;
		
//    if($('#OP_AN_unitLevel4').val() != ''){
//            OP_Level_NM1 = $('#OP_AN_unitLevel2 option:selected').text();
//            OP_Level_NM2 = $('#OP_AN_unitLevel2 option:selected').text();
//            OP_Level_NM2 +=$('#OP_AN_unitLevel3 option:selected').text();
//            OP_Level_NM3 = $('#OP_AN_unitLevel2 option:selected').text();
//            OP_Level_NM3 +=$('#OP_AN_unitLevel3 option:selected').text();
//            OP_Level_NM3 +=$('#OP_AN_unitLevel4 option:selected').text();
//    }else if ($('#OP_AN_unitLevel3').val() != ''){
//            OP_Level_NM1 = $('#OP_AN_unitLevel2 option:selected').text();
//            OP_Level_NM2 = $('#OP_AN_unitLevel2 option:selected').text();
//            OP_Level_NM2 +=$('#OP_AN_unitLevel3 option:selected').text();
//    }else{
//            OP_Level_NM1 = $('#OP_AN_unitLevel2 option:selected').text();
//    }
    if ($('#OP_AN_unitLevel3').val() != ''){
            OP_Level_NM1 = $('#OP_AN_unitLevel2 option:selected').text();
            OP_Level_NM2 = $('#OP_AN_unitLevel2 option:selected').text();
            OP_Level_NM2 +=$('#OP_AN_unitLevel3 option:selected').text();
    }else{
            OP_Level_NM1 = $('#OP_AN_unitLevel2 option:selected').text();
    }
    
    if (bool){
        show_BlockUI_page_noParent('資料儲存中…');
            var ajData = {
                            'ajaxAction' : $('#actionAnn1Type').val(),
                            'OP_SEQ_NO' : $('#IANNOUNCE_OP_SEQ_NO').val(),
                            'OP_BASIC_SEQ_NO' : $('#IPUBASIC_OP_SEQ_NO').val(),
                            'OP_AC_RCNO' : $('#IPUBASIC_OP_AC_RCNO').val(),
                            'OP_AN_D_UNIT_CD' : $('#OP_AN_unitLevel2').val(),
                            'OP_AN_D_UNIT_NM' : OP_Level_NM1,
                            'OP_AN_B_UNIT_CD' : $('#OP_AN_unitLevel3').val(),
                            'OP_AN_B_UNIT_NM' : OP_Level_NM2,
//                            'OP_AN_UNIT_CD' : $('#OP_AN_unitLevel4').val(),
//                            'OP_AN_UNIT_NM' : OP_Level_NM3,
                            'OP_AN_DATE_BEG' : $('#IANNOUNCE_OP_AN_DATE_BEG').val(),
                            'OP_DOC_DT' : $('#IANNOUNCE_OP_DOC_DT').val(),
                            'OP_DOC_WD' : $('#IANNOUNCE_OP_DOC_WD').val(),
                            'OP_DOC_NO' : $('#IANNOUNCE_OP_DOC_NO').val(),
                            'OP_AN_DATE_END' : $('#IANNOUNCE_OP_AN_DATE_END').val(),
                            'OP_AN_CONTENT' : $('#IANNOUNCE_OP_AN_CONTENT').val(),
                            'OP_AN_REMARK' : $('#IANNOUNCE_OP_AN_REMARK').val(),
                            'OP_YN_GET_NO1' : $('#OP_YN_GET_NO1').val(),
                            'OP_CABINET_NO' : $('#IANNOUNCE_OP_CABINET_NO').val(),
                            'OP_KP_UNIT_NM' : $('#IANNOUNCE_OP_KP_UNIT_NM').val(),
                            'OP_KP_NM' : $('#IANNOUNCE_OP_KP_NM').val(),
                            'OP_KP_TIME' : $('#IANNOUNCE_OP_KP_TIME').val(),
                            'OP_KP_DATE' : $('#IANNOUNCE_OP_KP_DATE').val(),
                            'OP_KP_REMARK' : $('#IANNOUNCE_OP_KP_REMARK').val(),
                            'OP_KP_UNIT_NM1' : $('#IANNOUNCE_OP_KP_UNIT_NM1').val(),
                            'OP_KP_NM1' : $('#IANNOUNCE_OP_KP_NM1').val(),
                            'OP_KP_TIME1' : $('#IANNOUNCE_OP_KP_TIME1').val(),
                            'OP_KP_DATE1' : $('#IANNOUNCE_OP_KP_DATE1').val(),
                            'OP_KP_REMARK1' : $('#IANNOUNCE_OP_KP_REMARK1').val(),
                            'OP_KP_UNIT_NM2' : $('#IANNOUNCE_OP_KP_UNIT_NM2').val(),
                            'OP_KP_NM2' : $('#IANNOUNCE_OP_KP_NM2').val(),
                            'OP_KP_TIME2' : $('#IANNOUNCE_OP_KP_TIME2').val(),
                            'OP_KP_DATE2' : $('#IANNOUNCE_OP_KP_DATE2').val(),
                            'OP_KP_REMARK2' : $('#IANNOUNCE_OP_KP_REMARK2').val(),
                            
                    };
                    var ajSucc = function(JData) {
                        if (JData.formData){
                            var formData = JData.formData[0];
                            if( $('#actionAnn1Type').val() == 'insertAnn1List' ){
                                notyMsg('儲存成功');
                                $('#actionAnn1Type').val('updateAnn1List');
                                $('#IANNOUNCE_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                                $('#getAnnounceBtn2').show();
                            }else if( $('#actionAnn1Type').val() == 'updateAnn1List' ){
                                notyMsg('儲存成功');
                                $('#getAnnounceBtn2').show();
                            }
                            DeleteImgForAnn( $('#IPUBASIC_OP_SEQ_NO').val(), $('#IPUBASIC_OP_AC_RCNO').val());
                        }else{
                            $('#actionAnn1Type').val() == 'insertAnn1List' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
                        }
                        $.unblockUI();
                    };
                    var ajErr = function() {
                            $('#actionAnn1Type').val() == 'insertAnn1List' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
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
//ANN1_SAVE---END