/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    init();    
});

function init(){
    $.ajax({
        url: 'UtilServlet',
        type: 'POST',
        datatype: 'json',
        data: {
            ajaxAction: 'getDBtime', 
        },
        async: false,
        success: function (data, textStatus, xhr) {            
            if(data.formData.length > 0){
              $('#DBTIME').text("現在DB日期"+data.formData[0][""]+"  DB連線正常");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $('#DBTIME').text("AP到DB連線異常");
        }
    })
}
