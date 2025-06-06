
//IFINISH_CHECK---START
function checkFnshList(inForm) {
    Validator.init(inForm);
    
    var nowDate = new Date();
    nowDate = getROCDateSlash(nowDate);
    //結案日期
    if( getADDate($('#IFNSH_OP_FS_DATE').val().substring(0,9)) >getADDate ( nowDate) ){
        Validator.setMessage("欄位 [ 結案日期 ]：時間需小於系統日期時間！");
        Validator.setBGColor("IFNSH_OP_FS_DATE");
    }
    //發還人員
    Validator.required('IFNSH_OP_FS_DATE', '結案日期');
    
    if ( Validator.isValid() )
    	return true;
    else {
        Validator.showMessage(); //檢核不通過，則顯示錯誤提示
        return false;
    }
}
//IFINISH_CHECK---END