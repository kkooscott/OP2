
//IPUANDL_CHECK---START
function checkPuanDlList(inForm) {
    Validator.init(inForm);
    
    //20230302 警署反應在單簽帶入的送交人姓名會被擋住「晧」字, 為免影響太大，先開一個NEW是不經過
    //「非署造字」的檢查，觀察一陣子後可以將所有的checkLength都移到New
    Validator.checkLength_New("IPUANDL_OP_SD_NAME", false, "送交人姓名", 30);
    Validator.checkLength_New("IPUANDL_OP_SD_TITLE", false, "送交人職稱", 30);                    
    Validator.checkLength_New("IPUANDL_OP_AC_REG_ATNO", true, "接受之地方自治團體", 80);
    Validator.checkLength_New("IPUANDL_OP_AC_NAME", false, "接受人姓名", 30);
    Validator.checkLength_New("IPUANDL_OP_AC_TITLE", false, "接受人職稱", 30);    
    
    if ( Validator.isValid() )
    	return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//IPUANDL_CHECK---END

//IPUANDL_SAVE---START
function confirmPuanDlAndSave(){
    var bool = true;
    savePuanDl();
}

function savePuanDl(){
    var bool = true;
    
    var OPENLLevel_NM;
    if($('#OP_SD_unitLevel4').val() != ''){
            OPENLLevel_NM =$('#OP_SD_unitLevel2 option:selected').text();
            OPENLLevel_NM +=$('#OP_SD_unitLevel3 option:selected').text();
            OPENLLevel_NM +=$('#OP_SD_unitLevel4 option:selected').text();
    }else if ($('#OP_SD_unitLevel3').val() != ''){
            OPENLLevel_NM =$('#OP_SD_unitLevel2 option:selected').text();
            OPENLLevel_NM +=$('#OP_SD_unitLevel3 option:selected').text();
    }else{
            OPENLLevel_NM =$('#OP_SD_unitLevel2 option:selected').text();
    }
    if (bool){
        show_BlockUI_page_noParent('資料儲存中…');
            var ajData = {
                            'ajaxAction' : $('#actionPuanDlType').val(),
                            'OP_SEQ_NO' : $('#IPUANDL_OP_SEQ_NO').val(),
                            'OP_BASIC_SEQ_NO' : $('#IPUBASIC_OP_SEQ_NO').val(),
                            'OP_AC_RCNO' : $('#IPUBASIC_OP_AC_RCNO').val(),
                            'OP_SD_UNITLEVEL2' : $('#OP_SD_unitLevel2').val(),
                            'OP_SD_UNITLEVEL2_NM' : $('#OP_SD_unitLevel2').val() == '' ? '' : $('#OP_SD_unitLevel2 option:selected').text(),
                            'OP_SD_UNITLEVEL3' : $('#OP_SD_unitLevel3').val(),
                            'OP_SD_UNITLEVEL3_NM' : $('#OP_SD_unitLevel3').val() == '' ? '' : $('#OP_SD_unitLevel3 option:selected').text(),
                            'OP_SD_UNITLEVEL4' : $('#OP_SD_unitLevel4').val(),
                            'OP_SD_UNITLEVEL4_NM' : $('#OP_SD_unitLevel4').val() == '' ? '' : $('#OP_SD_unitLevel4 option:selected').text(),
                            'OP_SD_UNITLEVEL_NM' : OPENLLevel_NM,
                            'OP_SD_NAME' : $('#IPUANDL_OP_SD_NAME').val(),
                            'OP_SD_TITLE' : $('#IPUANDL_OP_SD_TITLE').val(),
                            'OP_SD_DATE' : $('#IPUANDL_OP_SD_DATE').val(),
                            'OP_AC_REG_ATNO' : $('#IPUANDL_OP_AC_REG_ATNO').val(),
                            'OP_AC_NAME' : $('#IPUANDL_OP_AC_NAME').val(),
                            'OP_AC_TITLE' : $('#IPUANDL_OP_AC_TITLE').val()
                    };
                    var ajSucc = function(JData) {
                        if (JData.formData){
                            var formData = JData.formData[0];
                            if( $('#actionPuanDlType').val() == 'insertIPuanDlList' ){
                                notyMsg('儲存成功');
                                $('#actionPuanDlType').val('updateIPuanDlList');
                                $('#IPUANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                            }else if( $('#actionPuanDlType').val() == 'updateIPuanDlList' ){
                                notyMsg('儲存成功');
                                $('#actionPuanDlType').val('updateIPuanDlList');
                                $('#IPUANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                            }
                        }else{
                            $('#actionPuanDlType').val() == 'insertIPuanDlList' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
                        }
                        $.unblockUI();
                    };
                    var ajErr = function() {
                            $('#actionPuanDlType').val() == 'insertIPuanDlList' ? $.alert.open('error', "儲存失敗!!!") : $.alert.open('error', "儲存失敗!!!");
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
//IPUANDL_SAVE---END

//IPUANDL_SHOW---START
function getIPuanDlGridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料選取中…');
    var ajData = {
                    'ajaxAction' : 'getIPuanDlGridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    if( formData['ACTION_TYPE'] == "insertIPuanDlList" ){
                        $('#actionPuanDlType').val( formData['ACTION_TYPE'] );
                        $('#IPUANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        //送交單位
                        $('#OP_SD_unitLevel2').val(strPermissionData['UNITLEVEL1']);
                        bindUNITDDL('3', 'OP_SD_unitLevel2', 'OP_SD_unitLevel3', 'OP_SD_unitLevel4', '');
                        $('#OP_SD_unitLevel3').val(strPermissionData['UNITLEVEL2']);
                        bindUNITDDL('4', 'OP_SD_unitLevel2', 'OP_SD_unitLevel3', 'OP_SD_unitLevel4', '');
                        $('#OP_SD_unitLevel4').val(strPermissionData['UNITLEVEL3']);
                        //送交人姓名
                        $('#IPUANDL_OP_SD_NAME').val(strPermissionData['NAME']);
                        //送交人職稱
                        $('#IPUANDL_OP_SD_TITLE').val(strPermissionData['TITLE']);
                        //送交日期
                        var nowDate = new Date();
                        $('#IPUANDL_OP_SD_DATE').val(getROCDateSlash(nowDate) );
                        //接受之地方自治團體
                        $('#IPUANDL_OP_AC_REG_ATNO').val('');
                        $('#IPUANDL_OP_AC_NAME').val('');
                        $('#IPUANDL_OP_AC_TITLE').val('');
                    }else{
                        $('#actionPuanDlType').val( formData['ACTION_TYPE'] );
                        $('#IPUANDL_OP_SEQ_NO').val( formData['OP_SEQ_NO'] );
                        //送交單位
                        var data1 = getunit_code(formData['OP_SD_UNIT_CD']);
                        if ( data1.result[0]["OP_DEPT_CD"]==data1.result[0]["OP_UNIT_CD"]){//只有警局
                                $('#OP_SD_unitLevel2').val( data1.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('3','OP_SD_unitLevel2','OP_SD_unitLevel3','OP_SD_unitLevel4','');
                        }else if ( data1.result[0]["OP_UNIT_CD"]==data1.result[0]["OP_BRANCH_CD"]|| data1.result[0]["OP_BRANCH_CD"]==''){//只有警分局
                                $('#OP_SD_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_SD_unitLevel2','OP_SD_unitLevel3','OP_SD_unitLevel4','');
                                $('#OP_SD_unitLevel3').val(data1.result[0]["OP_UNIT_CD"]);
                                bindUNITDDL('4','OP_SD_unitLevel2','OP_SD_unitLevel3','OP_SD_unitLevel4','');
                        }else{
                                $('#OP_SD_unitLevel2').val(data1.result[0]["OP_DEPT_CD"]);
                                bindUNITDDL('3','OP_SD_unitLevel2','OP_SD_unitLevel3','OP_SD_unitLevel4','');
                                $('#OP_SD_unitLevel3').val(data1.result[0]["OP_BRANCH_CD"]);
                                bindUNITDDL('4','OP_SD_unitLevel2','OP_SD_unitLevel3','OP_SD_unitLevel4','');
                                $('#OP_SD_unitLevel4').val(data1.result[0]['OP_UNIT_CD']);
                        }
                        //送交人姓名
                        $('#IPUANDL_OP_SD_NAME').val( formData['OP_SD_NAME'] );
                        //送交人職稱
                        $('#IPUANDL_OP_SD_TITLE').val( formData['OP_SD_TITLE'] );
                        //送交日期
                        $('#IPUANDL_OP_SD_DATE').val( formData['OP_SD_DATE'] );
                        //接受之地方自治團體
                        $('#IPUANDL_OP_AC_REG_ATNO').val( formData['OP_AC_REG_ATNO'] );
                        $('#IPUANDL_OP_AC_NAME').val( formData['OP_AC_NAME'] );
                        $('#IPUANDL_OP_AC_TITLE').val( formData['OP_AC_TITLE'] );
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
//IPUANDL_SHOW---END

function PuAnDlButton(){
    //拾得人公告招領期滿儲存
    $('#saveIPuanDlBtn').click(function() {
        if (checkPuanDlList(document.getElementById('form1'))){
                confirmPuanDlAndSave();
        }
    });
}