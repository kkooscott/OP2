var strPermissionData;

var map;
var markers = [];
var geocoder = new google.maps.Geocoder();
var oldPosition = { lat: 0, lng: 0 }, oldInfoMsg = '';    //紀錄和上一次的經緯度是否相同(串info用)
var dragMarker; //可拖曳的標點

$(document).ready(function() {
    showMap();
    showDialog();
    registButton();
    defaultValue();
});

function showMap(){
    google.maps.event.addDomListener(window, 'load', initialMap);    
}

function initialMap(){
    var taiwan = new google.maps.LatLng(23.64550, 120.99705);
    var mapOptions = {
        zoom: 7,
        center: taiwan,       //中心座標
        scaleControl: true,    //比例尺

        //MapType位置
        mapTypeControl: false,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.RIGHT_BOTTOM
        },

        //Zoom位置
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.RIGHT_BOTTOM
        },

        //小人位置
        streetViewControl: true,
        streetViewControlOptions: {
            position: google.maps.ControlPosition.RIGHT_BOTTOM
        } 
    };
    map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);   

    //取得目前位置
    if(window.navigator.geolocation){   
        var geolocation=window.navigator.geolocation;           
        geolocation.getCurrentPosition(getPositionSuccess);
    }else{   
        alert("你的瀏覽器不支援地理定位");   
        map.setCenter(taiwan);   
    }
}

function getPositionSuccess(position){   
    var initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);       
    map.setCenter(initialLocation); //定位到目前位置
    map.setZoom(13);
}

function showDialog(){    
    $('#toolboxtabs').tabs();
    $('#toolboxdialog').dialog({
            position : {
                 my: "center", at: "left center", of: window
            },
            resizable : false,
            height : 250,
            width : 200,
            title : '地圖功能表',
            hide: 'slide',
                show: { effect: 'fade' }, 
            create : function(event, ui){
//                    $('#toolboxaccordion1').accordion('refresh');
            },
            open : function(event, ui) {
                    $('#toolboxdialog').css('overflow', 'hidden');
//                    $('#toolboxaccordion1').accordion('refresh');
            },
            resize : function(event, ui) {
//                    $('#toolboxaccordion1').accordion('refresh');
            }
    });            
}

function registButton() {
    $('#queryBtn').click(function(){
        if(!$('#OP_AC_D_UNIT_CD').val()){
            $.alert.open('警告', '請輸入警局');
            return;
        }        
        setUnitMarkers();     //設置單位標點
    });
    $('#clearBtn').click(function(){
        deleteMarkers();
    });
}

function defaultValue() {	        
    strPermissionData = getUserRole();
    bindUNITDDL('2', 'OP_AC_D_UNIT_CD', 'OP_AC_B_UNIT_CD', 'OP_AC_UNIT_CD', 'ALL'); //受理單位
//    $("#OP_AC_D_UNIT_CD").val(strPermissionData['UNITLEVEL1']).change();
    
    // 帶入登入者單位
    if (strPermissionData["Roles"].toString().indexOf('OP300005') > -1 || strPermissionData["Roles"].toString().indexOf('OP300006') > -1){

    }else if (strPermissionData["RolePermission"] == '1' ){
            $("#OP_AC_D_UNIT_CD").prop('disabled', true);
            $("#OP_AC_B_UNIT_CD").prop('disabled', true);
            $("#OP_AC_UNIT_CD").prop('disabled', true);
    }else if (strPermissionData["RolePermission"] == '2' ){
            $("#OP_AC_D_UNIT_CD").prop('disabled', true);
            $("#OP_AC_B_UNIT_CD").prop('disabled', true);
    }else if (strPermissionData["RolePermission"] == '3' ){
            $("#OP_AC_D_UNIT_CD").prop('disabled', true);
    }
    // 帶入登入者單位
    $('#OP_AC_D_UNIT_CD').val(strPermissionData['UNITLEVEL1']);
    bindUNITDDL('3', 'OP_AC_D_UNIT_CD', 'OP_AC_B_UNIT_CD', 'OP_AC_UNIT_CD', '');
    if (strPermissionData["RolePermission"] == '3' ){
        $('#OP_AC_B_UNIT_CD').val('');
    }else{
        $('#OP_AC_B_UNIT_CD').val(strPermissionData['UNITLEVEL2']);
    }
    bindUNITDDL('4', 'OP_AC_D_UNIT_CD', 'OP_AC_B_UNIT_CD', 'OP_AC_UNIT_CD', '');
    if ( strPermissionData["RolePermission"] == '2' || strPermissionData["RolePermission"] == '3' ){
        $('#OP_AC_UNIT_CD').val('');
    }else{
        $('#OP_AC_UNIT_CD').val(strPermissionData['UNITLEVEL3']);
    }
}

function setUnitMarkers() {
    deleteMarkers();
    $.ajax({
        url: 'SystemMaintainServlet',
        type: 'POST',
        datatype: 'json',
        async: false,
        data: {
            ajaxAction: 'getMarkers',
            includeLower: $('#includeLower').prop('checked'),
            OP_D_UNIT_CD: $('#OP_AC_D_UNIT_CD').val(),
            OP_B_UNIT_CD: $('#OP_AC_B_UNIT_CD').val(),
            OP_UNIT_CD: $('#OP_AC_UNIT_CD').val(),
        },
        success: function (data, textStatus, xhr) {            
            //初始化全域變數
            oldPosition = { lat: 0, lng: 0 }; 
            oldInfoMsg = '';
            dragMarker = null;
            
            var result = data.formData;
            if(result.length === 0) {
                //message('訊息', '查無單位資料');
                $.alert.open('error', '查無單位資料');
                return;
            }            
                        
            var bounds = new google.maps.LatLngBounds();
            //標記單位地點
            for (var i = 0; i < result.length; i++) {
                var marker = setMarkers(result[i]);
                bounds.extend(marker.getPosition());
                markers.push(marker);
            }
            map.fitBounds(bounds);
            map.setZoom(map.getZoom()-3);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus + " " + errorThrown);
        }
    });
}

function setMarkers(unitDetail){        
    var infoMsg = '', infoBtn = '', jsonDetail = '';
    var position = { lat: parseFloat(unitDetail.OP_LATITUDE), lng: parseFloat(unitDetail.OP_LONGITUDE) };       
    //和上一筆經緯度不同
    if((position.lat !== oldPosition.lat) || (position.lng !== oldPosition.lng)) {
        oldPosition = position;
        oldInfoMsg = '';
    }
    var marker = setPoint(unitDetail, position);    
    if(oldInfoMsg !== ''){
        infoMsg = 
            '<div align="center">'
                +'------------------------------------------'
            +'</div>';
    }
    jsonDetail = '{'
        + '"OP_UNIT_CD":"'+ unitDetail.OP_UNIT_CD +'",'
        + '"OP_UNIT_S_NM":"'+ unitDetail.OP_UNIT_S_NM +'",'
        + '"OP_UNIT_NM":"'+ unitDetail.OP_UNIT_NM +'",'
        + '"OP_UNIT_ADDR":"'+ unitDetail.OP_UNIT_ADDR +'"}';
    //自己不含下屬
    if(!$('#includeLower').prop('checked')){        
        infoBtn = 
                '<div align=center>'
                    + '<button onclick=getPosition('
                        + '"1",'
                        + jsonDetail +')>'
                    + '地址定位</button>'
                    + '<button onclick=getPosition('
                        + '"2",'
                        + jsonDetail +')>'
                    + '拖曳定位</button></div>';
    //自己含下屬                  
    } else {
        infoBtn = 
                '<div align=center>'
                    + '<button onclick=getPosition('
                        + '"1",'
                        + jsonDetail +')>'
                    + '地址定位</button></div>'
    }            
    infoMsg += 
        '<div align="left">'
            +'<b>名稱：' + unitDetail.OP_UNIT_S_NM 
            +'<br>地址：'+ unitDetail.OP_UNIT_ADDR 
            +'<br>電話：'+ unitDetail.OP_PHONE_NO
            + infoBtn
        +'</div>';
    oldInfoMsg += infoMsg;        
    setInfoWindow(marker, oldInfoMsg);  //建立單位訊息
    
    return marker;
}

function setPoint(unitDetail, position){
    //TYPE - 2:警局 3:分局 4:派出所
    var icon = unitDetail.TYPE === '2'? 
                    'assets/img/unitD.png' : 
            unitDetail.TYPE === '3'? 
                    'assets/img/unitB.png' : 'assets/img/unit.png';
    var marker = new google.maps.Marker({
            map: map,
            position: position,
            icon: icon
    });
    dragMarker = marker;
    return marker;
}

function setInfoWindow(marker,msg){
    var info = new google.maps.InfoWindow({
        content: msg
    });
    info.open(map, marker);
    marker.addListener('click', function() {
        info.open(map, marker);
    });
}

function getPosition(type, unitDetail){
    //地址定位
    if(type === '1'){
        dragMarker.setDraggable(false);
        dragMarker.setAnimation(null);    
        geocoder.geocode({ 'address': unitDetail.OP_UNIT_ADDR }, function(results, status) {        
            if (status !== google.maps.GeocoderStatus.OK) {
                alert('Geocode was not successful for the following reason: ' + status);
            }   
            var position=results[0].geometry.location;
            updatePosition(position, unitDetail);
        });
    //拖曳定位
    } else if(type === '2') {
        dragMarker.setDraggable(true);
        dragMarker.setAnimation(google.maps.Animation.BOUNCE);
        dragMarker.addListener('mouseup', function() {
            updatePosition(this.getPosition(), unitDetail);
        });        
    }
}

function updatePosition(position, unitDetail){
    //先定位
    dragMarker.setPosition(position);
    window.setTimeout(function() {
        map.panTo(position);
    }, 500);    
    
    //再詢問是否更新
    window.setTimeout(function() {
    $.alert.open({
            type : 'confirm',
            content : '<div align="left">'
                +'<b>名稱：' + unitDetail.OP_UNIT_S_NM
                +'<br>地址：'+ unitDetail.OP_UNIT_ADDR
                +'<br>確定更新位置？'
            +'</div>',
            callback : function(button, value) {
                if (button == 'yes') {                                        
                    $.ajax({
                        url: 'SystemMaintainServlet',
                        type: 'POST',
                        datatype: 'json',
                        async: false,
                        data: {
                            ajaxAction: 'updatePosition',                            
                            OP_UNIT_CD: unitDetail.OP_UNIT_CD,
                            OP_LONGITUDE: position.lng().toFixed(5),
                            OP_LATITUDE: position.lat().toFixed(5),
                        },
                        success: function (data, textStatus, xhr) {
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert(textStatus + " " + errorThrown);
                        }
                    });
                    setUnitMarkers();
                }
            }
    });
    }, 2000);
}

function deleteMarkers() {
    //Loop through all the markers and remove
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
};