
//IPUANANN---START
function showIPuanAnnGridList(OP_BASIC_SEQ_NO) {
    show_BlockUI_page_noParent('資料準備中…');
    var ajData = {
                    'ajaxAction' : 'showIPuanAnnGridList',
                    'OP_BASIC_SEQ_NO' : OP_BASIC_SEQ_NO,				
            };
            var ajSucc = function(JData) {
                if (JData.formData){
                    var formData = JData.formData[0];
                    //受理資訊
                    $('#OP_AC3_unitLevel2').val( formData['OP_AC_D_UNIT_CD'] );
                    bindUNITDDL('3', 'OP_AC3_unitLevel2', 'OP_AC3_unitLevel3', 'OP_AC3_unitLevel4', '');
                    $('#OP_AC3_unitLevel3').val( formData['OP_AC_B_UNIT_CD'] );
                    bindUNITDDL('4', 'OP_AC3_unitLevel2', 'OP_AC3_unitLevel3', 'OP_AC3_unitLevel4', '');
                    $('#OP_AC3_unitLevel4').val( formData['OP_AC_UNIT_CD'] );
                    //電話
                    $('#IPUANANN_OP_AC_UNIT_TEL').val( formData['OP_AC_UNIT_TEL'] );
                    //發文文號
                    $('#IPUANANN_OP_PUPO_DOC_WD').val( formData['OP_PUPO_DOC_WD'] );
                    $('#IPUANANN_OP_PUPO_DOC_NO').val( formData['OP_PUPO_DOC_NO'] );
                    //發文單位
                    $('#IPUANANN_OP_PUPOANUNIT_ALL').val( formData['OP_PUPOANUNITNM'] );
                    //公告日期
                    $('#IPUANANN_OP_NTC_PUPO_DT').val( formData['OP_NTC_PUPO_DT'] );
                    //公告內容
                    $('#IPUANANN_OP_PUPO_AN_CONT').val( formData['OP_PUPO_AN_CONT'] );
                    //拾得日期時間
                    $('#IPUANANN_OP_PU_DATE').val( formData['OP_PU_DATE'] );
                    $('#IPUANANN_OP_PU_TIME').val( formData['OP_PU_TIME'] );
                    //拾得地點
                    bindCOUNTRYDDL('2', 'IPUANANN_OP_PU_CITY_CD', 'IPUANANN_OP_PU_TOWN_CD', '', '', 'ALL'); //鄉鎮
                    $('#IPUANANN_OP_PU_CITY_CD').val( formData['OP_PU_CITY_CD'] );
                    bindCOUNTRYDDL('3', 'IPUANANN_OP_PU_CITY_CD', 'IPUANANN_OP_PU_TOWN_CD', '', '', '');
                    $('#IPUANANN_OP_PU_TOWN_CD').val( formData['OP_PU_TOWN_CD'] );
                    $('#IPUANANN_OP_PU_PLACE').val( formData['OP_PU_PLACE'] );
                    
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
//IPUANANN_SHOW---END
