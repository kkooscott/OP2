
//ANDL_CHECK---START
function checkAnDlList(inForm) {
    Validator.init(inForm);
    //通知拾得人領回日期 與 拾得人領回公告期滿日期 關係
    if( $("#IANDL_OP_NTC_PUPO_DT").val() == "" ){
        var nowDate2 = new Date();
        nowDate2.setMonth(nowDate2.getMonth() + 3);
        $('#IANDL_OP_PUPO_ANDTEND').val(getROCDateSlash(nowDate2) );
    }else{
        var nowDate3 = new Date(getADDateSlash($("#IANDL_OP_NTC_PUPO_DT").val()));
        nowDate3.setMonth(nowDate3.getMonth() + 3);
        
        $('#IANDL_OP_PUPO_ANDTEND').val(getROCDateSlash(nowDate3) );
    }
    
    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    //處理日期
    if( getADDate($('#IANDL_OP_PR_DATE').val().substring(0,9)) >getADDate ( nowDate) ){
        Validator.setMessage("欄位 [ 處理日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IANDL_OP_PR_DATE");
    }
    Validator.required('IANDL_OP_PR_DATE', '處理日期');
    //通知拾得人領回日期
    if( $('#IANDL_OP_NTC_PUPO_DT').val() != '' ){
        if( getADDate($('#IANDL_OP_NTC_PUPO_DT').val().substring(0,9)) >getADDate ( nowDate) ){
            Validator.setMessage("欄位 [ 通知拾得人領回日期 ]：時間需小於系統日期時間！");
            Validator.setBGColor("IANDL_OP_NTC_PUPO_DT");
        }
    }
    Validator.required('IANDL_OP_NTC_PUPO_DT', '通知拾得人領回日期');
    //公告期滿日期
    if( $('#IANDL_OP_NTC_PUPO_DT').val() != '' ){
        if( getADDate($('#IANDL_OP_PUPO_ANDTEND').val().substring(0,9)) <= getADDate ( nowDate) ){
            Validator.setMessage("通知拾得人領回日期 + 3個月 必需 > 系統日期！");
            Validator.setBGColor("IANDL_OP_NTC_PUPO_DT");
        }
    }
    
    Validator.checkLength("IANDL_OP_PUPO_DOC_WD",true,"發文文號-字",60);
    Validator.checkLength("IANDL_OP_PUPO_DOC_NO",true,"發文文號-號",11);
    Validator.checkLength("IANDL_OP_PUPO_AN_CONT",true,"公告內容",600);
    
    if ( Validator.isValid() )
    	return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//ANDL_CHECK---END

//ANDL_SAVE---START
function confirmAnDlAndSave(){
    var bool = true;

    if( bool ){
        var check = checkStatusForBasic($('#IPUBASIC_OP_SEQ_NO').val());
        if( check == '4' ){ //拾得人領回公告中 4
            $.alert.open({
		type : 'confirm',
                async : false,
		content : '此拾得遺失物案件已發佈拾得人領回公告，若您要重新儲存公告期滿資料，則原拾得人領回公告會被撤除，再重新公告，且拾得人領回公告期滿日期會被重新計算。確定要儲存公告期滿資料嗎？',
		callback : function(button, value) {
			if (button == 'yes') 
			    saveAnDl();
		}
            });
        }else{
            saveAnDl();
        }	
    }
}

function saveAnDl(){
    var bool = true;
    
    var OPENLLevel_NM;
    var OP_PUPOANUNIT_Level_NM;
    if($('#OP_PR_unitLevel4').val() != ''){
            OPENLLevel_NM =$('#OP_PR_unitLevel2 option:selected').text();
            OPENLLevel_NM +=$('#OP_PR_unitLevel3 option:selected').text();
            OPENLLevel_NM +=$('#OP_PR_unitLevel4 option:selected').text();
    }else if ($('#OP_PR_unitLevel3').val() != ''){
            OPENLLevel_NM =$('#OP_PR_unitLevel2 option:selected').text();
            OPENLLevel_NM +=$('#OP_PR_unitLevel3 option:selected').text();
    }else{
            OPENLLevel_NM =$('#OP_PR_unitLevel2 option:selected').text();
    }
    
//    if($('#OP_PUPOANUNIT_unitLevel4').val() != ''){
//            OP_PUPOANUNIT_Level_NM =$('#OP_PUPOANUNIT_unitLevel2 option:selected').text();
//            OP_PUPOANUNIT_Level_NM +=$('#OP_PUPOANUNIT_unitLevel3 option:selected').text();
//            OP_PUPOANUNIT_Level_NM +=$('#OP_PUPOANUNIT_unitLevel4 option:selected').text();
//    }else 
    if ($('#OP_PUPOANUNIT_unitLevel3').val() != ''){
            OP_PUPOANUNIT_Level_NM =$('#OP_PUPOANUNIT_unitLevel2 option:selected').text();
            OP_PUPOANUNIT_Level_NM +=$('#OP_PUPOANUNIT_unitLevel3 option:selected').text();
    }else{
            OP_PUPOANUNIT_Level_NM =$('#OP_PUPOANUNIT_unitLevel2 option:selected').text();
    }
    if (bool){
        show_BlockUI_page_noParent('資料儲存中…');
            var ajData = {
                            'ajaxAction' : $('#actionAnDlType').val(),
                            'OP_SEQ_NO' : $('#IANDL_OP_SEQ_NO').val(),
                            'OP_BASIC_SEQ_NO' : $('#IPUBASIC_OP_SEQ_NO').val(),
                            'OP_AC_RCNO' : $('#IPUBASIC_OP_AC_RCNO').val(),
                            'OP_PR_UNITLEVEL2' : $('#OP_PR_unitLevel2').val(),
                            'OP_PR_UNITLEVEL2_NM' : $('#OP_PR_unitLevel2').val() == '' ? '' : $('#OP_PR_unitLevel2 option:selected').text(),
                            'OP_PR_UNITLEVEL3' : $('#OP_PR_unitLevel3').val(),
                            'OP_PR_UNITLEVEL3_NM' : $('#OP_PR_unitLevel3').val() == '' ? '' : $('#OP_PR_unitLevel3 option:selected').text(),
                            'OP_PR_UNITLEVEL4' : $('#OP_PR_unitLevel4').val(),
                            'OP_PR_UNITLEVEL4_NM' : $('#OP_PR_unitLevel4').val() == '' ? '' : $('#OP_PR_unitLevel4 option:selected').text(),
                            'OP_PR_UNITLEVEL_NM' : OPENLLevel_NM,
                            'OP_PR_STAFF_NM' : $('#IANDL_OP_PR_STAFF_NM').val(),
                            'OP_PR_DATE' : $('#IANDL_OP_PR_DATE').val(),
                            'OP_PR_STAT_DESC' : $('#IANDL_OP_PR_STAT_DESC').val(),
                            'OP_YN_AUCTION' : $('#IANDL_OP_YN_AUCTION').val(),
                            'OP_NTC_PUPO_DT' : $('#IANDL_OP_NTC_PUPO_DT').val(),
                            'OP_PUPO_ANDTEND' : $('#IANDL_OP_PUPO_ANDTEND').val(),
                            'OP_PUPO_DOC_WD' : $('#IANDL_OP_PUPO_DOC_WD').val(),
                            'OP_PUPO_DOC_NO' : $('#IANDL_OP_PUPO_DOC_NO').val(),
                            'OP_PUPOANUNIT_UNITLEVEL2' : $('#OP_PUPOANUNIT_unitLevel2').val(),
                            'OP_PUPOANUNIT_UNITLEVEL2_NM' : $('#OP_PUPOANUNIT_unitLevel2').val() == '' ? '' : $('#OP_PUPOANUNIT_unitLevel2 option:selected').text(),
                            'OP_PUPOANUNIT_UNITLEVEL3' : $('#OP_PUPOANUNIT_unitLevel3').val(),
                            'OP_PUPOANUNIT_UNITLEVEL3_NM' : $('#OP_PUPOANUNIT_unitLevel3').val() == '' ? '' : $('#OP_PUPOANUNIT_unitLevel3 option:selected').text(),
//                            'OP_PUPOANUNIT_UNITLEVEL4' : $('#OP_PUPOANUNIT_unitLevel4').val(),
//                            'OP_PUPOANUNIT_UNITLEVEL4_NM' : $('#OP_PUPOANUNIT_unitLevel4').val() == '' ? '' : $('#OP_PUPOANUNIT_unitLevel4 option:selected').text(),
                            'OP_PUPOANUNIT_UNITLEVEL_NM' : OP_PUPOANUNIT_Level_NM,
                            'OP_PUPO_AN_CONT' : $('#IANDL_OP_PUPO_AN_CONT').val(),
                            'OP_YN_GET_NO2' : $('#OP_YN_GET_NO2').val()
                    };
                    var ajSucc = function(JData) {
                        if (JData.formData){
                            var formData = JData.formData[0];
                            if( $('#actionAnDlType').val() == 'insertIAnDlList' ){
                                notyMsg('儲存成功');
                                $('#actionAnDlType').val('updateIAnDlList');
                                $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                            }else if( $('#actionAnDlType').val() == 'updateIAnDlList' ){
                                $('#actionAnDlType').val('updateIAnDlList');
                                notyMsg('儲存成功');
                                $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                            }
                        }else{
                            $('#actionAnDlType').val() == 'insertAnDlList' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
                        }
                        $.unblockUI();
                    };
                    var ajErr = function() {
                            $('#actionAnDlType').val() == 'insertAnDlList' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
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
//ANDL_SAVE---END

//IANDL_SHOW---START
function showIAnDlGridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料準備中…');
    var ajData = {
                    'ajaxAction' : 'showIAnDlGridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    if( formData['ACTION_TYPE'] == "insertIAnDlList" ){
                        $('#actionAnDlType').val( formData['ACTION_TYPE'] );
                        $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        //處理單位
                        $('#OP_PR_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                        bindUNITDDL('3', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');
                        $('#OP_PR_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                        bindUNITDDL('4', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');
                        $('#OP_PR_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                        //處理人員
                        $('#IANDL_OP_PR_STAFF_NM').val(strPermissionData['NAME']);
                        
                        $('#IANDL_OP_PR_DATE').val('');
                        $('#IANDL_OP_YN_AUCTION').val('N');
                        $('#IANDL_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
                        
                        //拾得人領回公告發文單位            
                        $('#OP_PUPOANUNIT_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                        bindUNITDDL('3', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');
                        $('#OP_PUPOANUNIT_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                        bindUNITDDL('4', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');
                        $('#OP_PUPOANUNIT_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                        
                        var nowDate = new Date();
                        nowDate.setMonth(nowDate.getMonth() + 3);
                        $('#IANDL_OP_PUPO_ANDTEND').val(getROCDateSlash(nowDate) );
                        
                        $('#IANDL_OP_PUPO_DOC_WD').val('');
                        $('#IANDL_OP_PUPO_DOC_NO').val('');
                        $('#IANDL_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
                        $('#IANDL_OP_PR_STAT_DESC').val('');
                    }else{
                        $('#actionAnDlType').val( formData['ACTION_TYPE'] );
                        $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        //處理單位
                        var data1 = getunit_code(formData['OP_PR_UNIT_CD']);
                        if ( data1.result[0]["OP_DEPT_CD"]==data1.result[0]["OP_UNIT_CD"]){//只有警局
                                $('#OP_PR_unitLevel2').val( data1.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                        }else if ( data1.result[0]["OP_UNIT_CD"]==data1.result[0]["OP_BRANCH_CD"]|| data1.result[0]["OP_BRANCH_CD"]==''){//只有警分局
                                $('#OP_PR_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                                $('#OP_PR_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('4','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                        }else{
                                $('#OP_PR_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                                $('#OP_PR_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
                                bindUNITDDL('4','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                                $('#OP_PR_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
                        }
                        
                        //處理人員
                        $('#IANDL_OP_PR_STAFF_NM').val(formData['OP_PR_STAFF_NM']);
                        
                        $('#IANDL_OP_PR_DATE').val( formData['OP_PR_DATE'] );
                        $('#IANDL_OP_YN_AUCTION').val( formData['OP_YN_AUCTION'] );
                        $('#IANDL_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
                        
                        //拾得人領回公告發文單位
                        var data2 = getunit_code(formData['OP_PUPOANUNITCD']);
                        if ( data2.result[0]["OP_DEPT_CD"]==data2.result[0]["OP_UNIT_CD"]){//只有警局
                                $('#OP_PUPOANUNIT_unitLevel2').val( data2.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                        }else if ( data2.result[0]["OP_UNIT_CD"]==data2.result[0]["OP_BRANCH_CD"]|| data2.result[0]["OP_BRANCH_CD"]==''){//只有警分局
                                $('#OP_PUPOANUNIT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                                $('#OP_PUPOANUNIT_unitLevel3').val(data2.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('4','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                        }else{
                                $('#OP_PUPOANUNIT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                                $('#OP_PUPOANUNIT_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
                                bindUNITDDL('4','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                                $('#OP_PUPOANUNIT_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
                        }
                        
                        $('#IANDL_OP_PUPO_ANDTEND').val( formData['OP_PUPO_ANDTEND'] );
                        
                        $('#IANDL_OP_PUPO_DOC_WD').val( formData['OP_PUPO_DOC_WD'] );
                        $('#IANDL_OP_PUPO_DOC_NO').val( formData['OP_PUPO_DOC_NO'] );
                        $('#IANDL_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
                        $('#IANDL_OP_PR_STAT_DESC').val( formData['OP_PR_STAT_DESC'] );
                        
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
//IANDL_SHOW---END

//IANDL_SHOW---START
function getIAnDlGridList(OP_BASIC_SEQ_NO, ACTION) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
                    'ajaxAction' : 'getIAnDlGridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    if( formData['ACTION_TYPE'] == "insertIAnDlList" ){
                        $('#actionAnDlType').val( formData['ACTION_TYPE'] );
                        $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        //處理單位
                        $('#OP_PR_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                        bindUNITDDL('3', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');
                        $('#OP_PR_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                        bindUNITDDL('4', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');
                        $('#OP_PR_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                        //處理人員
                        $('#IANDL_OP_PR_STAFF_NM').val(strPermissionData['NAME']);
                        
                        $('#IANDL_OP_PR_DATE').val('');
                        $('#IANDL_OP_YN_AUCTION').val('N');
                        $('#IANDL_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
                        
                        //拾得人領回公告發文單位            
                        $('#OP_PUPOANUNIT_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                        bindUNITDDL('3', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');
                        $('#OP_PUPOANUNIT_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                        bindUNITDDL('4', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');
                        $('#OP_PUPOANUNIT_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                        
                        var nowDate = new Date();
                        nowDate.setMonth(nowDate.getMonth() + 3);
                        $('#IANDL_OP_PUPO_ANDTEND').val(getROCDateSlash(nowDate) );
                        
                        $('#IANDL_OP_PUPO_DOC_WD').val( formData['OP_PUPO_DOC_WD'] );
                        $('#IANDL_OP_PUPO_DOC_NO').val( formData['OP_PUPO_DOC_NO'] );
                        $('#OP_YN_GET_NO2').val( formData['OP_YN_GET_NO2'] );
                        
                        if(ACTION == "OP04A01Q" || ACTION == "OP03A02Q"){
                            if( formData['OP_YN_GET_NO2'] == "1" ){
                                $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                                $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true); 
                            }else{
                                $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', false); 
                                $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', false); 
                            }
                        }else if( ACTION == "OP05A01Q" ){
//                            if ( strPermissionData["Roles"].toString().indexOf('OP300004') > -1 ){ //警局承辦人
//                                if( formData['OP_YN_GET_NO2'] == "1" ){
//                                    $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
//                                    $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true); 
//                                }else{
//                                    $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', false); 
//                                    $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', false); 
//                                }
//                            }else{
//                                $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
//                                $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true);
//                            }
                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true);
                        }else{
                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true);
                        }
                        
                        $('#IANDL_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
                        $('#IANDL_OP_PR_STAT_DESC').val('');
                    }else{
                        $('#actionAnDlType').val( formData['ACTION_TYPE'] );
                        $('#IANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        //處理單位
                        var data1 = getunit_code(formData['OP_PR_UNIT_CD']);
                        if ( data1.result[0]["OP_DEPT_CD"]==data1.result[0]["OP_UNIT_CD"]){//只有警局
                                $('#OP_PR_unitLevel2').val( data1.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                        }else if ( data1.result[0]["OP_UNIT_CD"]==data1.result[0]["OP_BRANCH_CD"]|| data1.result[0]["OP_BRANCH_CD"]==''){//只有警分局
                                $('#OP_PR_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                                $('#OP_PR_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('4','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                        }else{
                                $('#OP_PR_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                                $('#OP_PR_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
                                bindUNITDDL('4','OP_PR_unitLevel2','OP_PR_unitLevel3','OP_PR_unitLevel4','');
                                $('#OP_PR_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
                        }
                        
                        //處理人員
                        $('#IANDL_OP_PR_STAFF_NM').val(formData['OP_PR_STAFF_NM']);
                        
                        $('#IANDL_OP_PR_DATE').val( formData['OP_PR_DATE'] );
                        $('#IANDL_OP_YN_AUCTION').val( formData['OP_YN_AUCTION'] );
                        $('#IANDL_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
                        
                        //拾得人領回公告發文單位
                        var data2 = getunit_code(formData['OP_PUPOANUNITCD']);
                        if ( data2.result[0]["OP_DEPT_CD"]==data2.result[0]["OP_UNIT_CD"]){//只有警局
                                $('#OP_PUPOANUNIT_unitLevel2').val( data2.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                        }else if ( data2.result[0]["OP_UNIT_CD"]==data2.result[0]["OP_BRANCH_CD"]|| data2.result[0]["OP_BRANCH_CD"]==''){//只有警分局
                                $('#OP_PUPOANUNIT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                                $('#OP_PUPOANUNIT_unitLevel3').val(data2.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('4','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                        }else{
                                $('#OP_PUPOANUNIT_unitLevel2').val(data2.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                                $('#OP_PUPOANUNIT_unitLevel3').val(data2.result[0]["OP_BRANCH_CD"]);
                                bindUNITDDL('4','OP_PUPOANUNIT_unitLevel2','OP_PUPOANUNIT_unitLevel3','OP_PUPOANUNIT_unitLevel4','');
                                $('#OP_PUPOANUNIT_unitLevel4').val(data2.result[0]['OP_UNIT_CD']);
                        }
                        
                        $('#IANDL_OP_PUPO_ANDTEND').val( formData['OP_PUPO_ANDTEND'] );
                        
                        $('#IANDL_OP_PUPO_DOC_WD').val( formData['OP_PUPO_DOC_WD'] );
                        $('#IANDL_OP_PUPO_DOC_NO').val( formData['OP_PUPO_DOC_NO'] );
                        
                        $('#OP_YN_GET_NO2').val( formData['OP_YN_GET_NO2'] );
                        if(ACTION == "OP04A01Q" || ACTION == "OP03A02Q"){
                            if( formData['OP_YN_GET_NO2'] == "1" ){
                                $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                                $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true); 
                            }else{
                                $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', false); 
                                $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', false); 
                            }
                        }else if( ACTION == "OP05A01Q" ){
                            if ( strPermissionData["Roles"].toString().indexOf('OP300004') > -1 ){ //警局承辦人
                                if( formData['OP_YN_GET_NO2'] == "1" ){
                                    $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                                    $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true); 
                                }else{
                                    $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', false); 
                                    $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', false); 
                                }
                            }else{
                                $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                                $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true);
                            }
                        }else{
                            $('#IANDL_OP_PUPO_DOC_WD').prop('disabled', true); 
                            $('#IANDL_OP_PUPO_DOC_NO').prop('disabled', true);
                        }
                        $('#IANDL_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
                        $('#IANDL_OP_PR_STAT_DESC').val( formData['OP_PR_STAT_DESC'] );
                        
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
//IANDL_SHOW---END

function AnDlButton(){
    //招領期滿儲存
    $('#saveIAnDlBtn').click(function() {
        if (checkAnDlList(document.getElementById('form1'))){
                confirmAnDlAndSave();
        }
    });
}
