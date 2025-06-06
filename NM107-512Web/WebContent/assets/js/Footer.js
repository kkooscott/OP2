var DEFAULT_OPTION = '...'; // 預設下拉選單值
var NOTY_SHOW_TIME = 3000; // 提示訊息持續顯示時間長度
var isSubmit = false; // 設定是否為表單送出
var isValidated = false; // 判斷是否通過驗證
var sysParam = null; // 用來存放所有系統參數 , 取值方式 sysParam[dv_param_key]

/**
 * 頁面完成初始化
 */
$(document).ready(
        function () {
            modalQueryFun();
            // 設定 ajax 預設的參數
            $.ajaxSetup({
                // cache:false,//避免ajax在發生錯誤時就不做ajax了
                type: 'POST',
                dataType: 'json',
                error: function (jqXHR, textStatus, errorThrown) {
                    switch (textStatus) {
                        case 'abort':
                            // do nothing
                            break;
                        default:
                            $.alert.open('error', 'ajax發生錯誤！textStatus = '
                                    + textStatus);
                    }
                },
                statusCode: {
                    901: function () { // 考慮是否要用此種方式呈現 Session Timeout
                        window.location = 'SessionTimeout.jsp';
                    },
                    404: function () {
                        $.alert.open('error', 'url找不到！');
                    },
                }
            });

            // 初始化驗證機制
            $.validator.setDefaults({
                submitHandler: function () {
                    isSubmit = false;
                },
                showErrors: function (map, list) {

                    // there's probably a way to simplify this
                    var focussed = document.activeElement;
                    if (focussed && $(focussed).is("input, textarea, div")) {
                        $(this.currentForm).tooltip("close", {
                            currentTarget: focussed
                        }, true);
                    }
                    this.currentElements.removeAttr("title").removeClass("ui-state-highlight");


                    //移除特元件的highlight 背景顏色
                    $.each(this.currentElements, function (index, element) {
                        if (this.tagName == 'TEXTAREA') {
                            //CKEDITOR textarea background
                            var thisID = 'div#cke_' + this.id + ' div.cke_inner iframe';
                            $(thisID).contents().find("body").css('background-color', "white");
                            $(thisID).prop('title', '');//tooltip 訊息顯示在iframe
                        } else if (this.tagName == 'SELECT') {
                            //bootstrap selectpicker
                            var thisID = '#' + this.id;
                            $(thisID).next().find('button').css('background-color', "white").prop('title', '');//tooltip 訊息
                        } else if (this.tagName == 'INPUT') {
                            if (this.type == 'radio') {
                                var thisID = '#' + this.id;
                                var thisName = this.name;
                                $('input[name="' + thisName + '"]').parent().parent().parent().css('background-color', 'white')
                                        .prop('title', '');
                            } else if (this.type == 'checkbox') {

                            }
                            ;
                        }
                    });


                    if (isSubmit) {
                        if (list.length > 0) {
                            isValidated = false;
                        } else {
                            isValidated = true;
                        }
                        $.each(list, function (index, error) {



                            //特殊元件(隱藏欄位) 更換背景提示顏色
                            if (this.element.tagName == 'TEXTAREA') {
                                //CKEDITOR textarea background
                                var thisID = 'div#cke_' + this.element.id + ' div.cke_inner iframe';
                                $(thisID).contents().find("body").css('background-color', "url('images/ui-bg_flat_55_fbec88_40x100.png') repeat-x scroll 50% 50% #FBEC88");
                                $(thisID).prop('title', '請輸入正確內容');//title 顯示在iframe
                            } else if (this.element.tagName == 'SELECT') {
                                //bootstrap selectpicker
                                var thisID = '#' + this.element.id;
                                $(thisID).next().find('button').css('background-color', "url('images/ui-bg_flat_55_fbec88_40x100.png') repeat-x scroll 50% 50% #FBEC88")
                                        .prop('title', '請選擇項目');
                            } else if (this.element.tagName == 'INPUT') {
                                if (this.element.type == 'radio') {
                                    var thisID = '#' + this.element.id;
                                    var thisName = this.element.name;
                                    $('input[name="' + thisName + '"]').parent().parent().parent().css('background-color', "url('images/ui-bg_flat_55_fbec88_40x100.png') repeat-x scroll 50% 50% #FBEC88")
                                            .prop('title', '請選擇項目');
                                } else if (this.element.type == 'checkbox') {

                                } else {
                                    $(error.element).attr("title", error.message).addClass("ui-state-highlight");
                                }
                                ;

                            } else {
                                $(error.element).attr("title", error.message).addClass("ui-state-highlight");
                            }
                            ;

                        });
                        if (focussed && $(focussed).is("input, textarea, div")) {
                            $(this.currentForm).tooltip("open", {
                                currentTarget: focussed
                            }, true);

                        }
                    }
                }
            });


            // 建立下拉選單檢查規則
            $.validator.addMethod("selectComboboxCheck", function (value,
                    element) {
                var cboName = element.name.replace("_combobox", "");
                if ($('#' + cboName).combobox('value') == '...') {
                    return false;
                } else {
                    return true;
                }
            }, "Required");

            // 建立名為 'regex' 的自訂規則
            $.validator.addMethod("regex", function (value, element, param) {
                var re = new RegExp(param);
                if (value.match(new RegExp(re))) {
                    return true;
                } else {
                    return false;
                }
            });

            // 身分證驗證
            $.validator.addMethod("IDNumber", function (value, element) {
                if (value.length != 10)
                    return false;
                IDN = value;
                IDTable = {
                    A: 10,
                    B: 11,
                    C: 12,
                    D: 13,
                    E: 14,
                    F: 15,
                    G: 16,
                    H: 17,
                    I: 34,
                    J: 18,
                    K: 19,
                    M: 21,
                    N: 22,
                    O: 35,
                    P: 23,
                    Q: 24,
                    T: 27,
                    U: 28,
                    V: 29,
                    W: 32,
                    X: 30,
                    Z: 33,
                    L: 20,
                    R: 25,
                    S: 26,
                    Y: 31
                };
                LocalDigit = IDTable[IDN[0].toUpperCase()];
                return /^[A-Za-z][1,2][\d]{8}/.test(value)
                        && ((Math.floor(LocalDigit / 10) + (LocalDigit % 10)
                                * 9 + IDN[1] * 8 + IDN[2] * 7 + IDN[3] * 6
                                + IDN[4] * 5 + IDN[5] * 4 + IDN[6] * 3 + IDN[7]
                                * 2 + IDN[8] * 1 + IDN[9] * 1) % 10 == 0);
            }, '請輸入有效的身分證字號');

            // 即時訊息相關
            if ($('#vticker').length > 0) {

                // 進行即時訊息資料擷取並設定到畫面中
                $.ajax({
                    url: 'AnnMessageServlet',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        data: function () {
                            var obj = {
                                action: 'queryAll',
                                is_subscribe: 'Y',
                                call_by_header: 'Y'
                            };
                            return JSON.stringify(obj);
                        }
                    },
                    success: function (JData) {
                        for (var i = 0; i < JData.length; i++) {
                            var liContent = document.createElement('li');
                            liContent.innerHTML = JData[i]['dv_publish_desc'];
                            $('#vticker-ul')[0].appendChild(liContent);
                        }

                        // 進行初始化設定
                        $('#vticker').vTicker({
                            speed: 500,
                            pause: 3000,
                            showItems: 1,
                            animation: 'fade',
                            mousePause: false,
                            height: 0,
                            direction: 'up'
                        });
                    }
                });
            }

            // 初始化使用者下拉式選單
            $(function () {
                new DropDownUser($('#dd'));
            });

            getFuncList();
        }
);

/**
 * 點選畫面收起下拉式選單
 */
$(document).click(function () {
    // all dropdowns
    $('.wrapper-dropdown-5').removeClass('active');
    $('.wrapper-dropdown-6').removeClass('active');
    $('.wrapper-dropdown-7').removeClass('active');
});

/**
 * 初始化使用者下拉選單
 * 
 * @param el
 *            物件識別名稱
 */
function DropDownUser(el) {
    this.dd = el;
    this.initEvents();
}

/**
 * 初始化使用者下拉選單點擊事件
 */
DropDownUser.prototype = {
    initEvents: function () {
        var obj = this;

        obj.dd.on('click', function (event) {
            $(this).toggleClass('active');
            event.stopPropagation();
        });
    }
};

/**
 * 顯示主要框架中的頁面
 */
function showMainFrame(url) {
    parent.mainFrame.location.href = url;// 似乎要設定這個才有效
    parent.formFrame.cols = "*,0";
}

function logout() {
    parent.mainFrame.location.href = 'Login.jsp';
    parent.mapFrame.location.href = 'blank.html';
    parent.formFrame.cols = "*,0";
}

/**
 * 宣告 jQuery 判斷是否為空的共用函式
 * 
 * 使用方式 : $.isBlank(變數) 判斷方式 : $.isBlank(" ") -> true $.isBlank("") -> true
 * $.isBlank("\n") -> true $.isBlank("a") -> false
 * 
 * $.isBlank(null) -> true $.isBlank(undefined) -> true $.isBlank(false) -> true
 * $.isBlank([]) -> true
 * 
 * @returns true: 是空的 , false: 不是空的
 */
(function ($) {
    $.isBlank = function (obj) {
        return (!obj || $.trim(obj) === "");
    };
})(jQuery);

/**
 * 建立 noty 物件(包裝後)
 * 
 * @param text
 *            顯示的文字
 */
function notyMsg(text) {
    var noty = generateNormal(text, 'success', 'bottom');
    setTimeout(function () {
        $.noty.close(noty.options.id);
    }, NOTY_SHOW_TIME);
}

/**
 * 建立 noty 物件(通知)
 * 
 * @param layout
 */
function generateNormal(text, type, layout) {
    var n = noty({
        text: text,
        type: type,
        dismissQueue: true,
        layout: layout,
        theme: 'defaultTheme'
    });
    return n;
}

/**
 * 紀錄使用者操作行為
 * 
 * @param log_context
 *            紀錄內容
 */
function usrActionRecord(dv_log_context) {
    $.ajax({
        url: 'UsrActionServlet',
        type: 'POST',
        dataType: 'json',
        data: {
            data: function () {
                var obj = {
                    action: 'insertUsrAction',
                    dv_log_context: dv_log_context
                };
                return JSON.stringify(obj);
            }
        },
        success: function (JData) {
            // this respose may not be shown

            // 檢查是否逾時
            if (JData[0] != undefined && JData[0]['url'] != undefined) {
                window.location = JData[0]['url'];
            }
        }
    });
}

/**
 * -----------------------------------------------------------------------------------------------
 * Barry
 * ------------------------------------------------------------------------------------------------
 */

/**
 * 解碼字串
 * 
 * @param sourceString
 * @returns {String}
 */
function decodeURIByNPA(sourceString) {
    var resultString = '';
    resultString = decodeURI(sourceString).replace('+', ' ')
            .replace('%2B', '+');
    return resultString;
}

/**
 * 檢查密碼是否符合規定
 * 
 * @param pwd1
 * @param pwd2
 * @returns {Boolean}
 */
function chkPwd(pwd1, pwd2) {
    var msg = "";
    if (pwd1.length < 8 || pwd1.length > 20) {// •密碼長度需至少8碼，至多20碼
        msg += "第一個密碼欄位長度需至少8碼，至多20碼!!<br>";
    }
    if (pwd2.length < 8 || pwd2.length > 20) {// •密碼長度需至少8碼，至多20碼
        msg += "第二個密碼欄位長度需至少8碼，至多20碼!!<br>";
    }
    if (pwd1 != pwd2) {
        msg += "兩個密碼欄位資料不一致!!<br>";
    }
    var upperLetter = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z'];
    var lowerLetter = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z'];
    var digital = ['1', '2', '3', '4', '5', '6', '7', '8', '9'];
    var specialLetter = ['~', '!', '@', '#', '$', '^', '*', '_', '-', '?'];
    var hasUpperLetter = false;
    var hasLowerLetter = false;
    var hasDigital = false;
    var hasSpecialLetter = false;
    for (var i = 0; i < pwd1.length; i++) {
        for (var j = 0; j < upperLetter.length; j++) {
            if (pwd1.charAt(i) == upperLetter[j]) {
                hasUpperLetter = true;
                break;
            }
        }
        for (var j = 0; j < lowerLetter.length; j++) {
            if (pwd1.charAt(i) == lowerLetter[j]) {
                hasLowerLetter = true;
                break;
            }
        }
        for (var j = 0; j < digital.length; j++) {
            if (pwd1.charAt(i) == digital[j]) {
                hasDigital = true;
                break;
            }
        }
        for (var j = 0; j < specialLetter.length; j++) {
            if (pwd1.charAt(i) == specialLetter[j]) {
                hasSpecialLetter = true;
                break;
            }
        }
    }
    if (!hasUpperLetter) {
        msg += "密碼欄位缺大寫字元!!<br>";
    }
    if (!hasLowerLetter) {
        msg += "密碼欄位缺小寫字元!!<br>";
    }
    if (!hasDigital) {
        msg += "密碼欄位缺數字字元!!<br>";
    }
    if (!hasSpecialLetter) {
        msg += "密碼欄位缺特殊符號字元('~','!','@','#','$','^','*','_','-','?')!!<br>";
    }

    if (msg.length > 0) {
        $.alert.open({
            type: 'error',
            content: msg
        });
        return false;
    }

    return true;
}
/**
 * 處理Ajax回來後的共同訊息
 */
function processAjaxMsg(data) {
    if (data['successMsg'] != null) {
        notyMsg(data['successMsg']);
    }
    if (data['exceptionMsg'] != null) {
        $.alert.open('error', data['exceptionMsg']);
    }
}


/**
 * -----------------------------------------------------------------------------------------------
 * 下拉式選單
 * ------------------------------------------------------------------------------------------------
 */

/**
 * 取得民國年月日(不含/)
 * @param dateString 欲填入之日期
 */
function getROCDate(dateString) {
    var sYear, sMonth, sDate;
    var returnValue = '';

    if (!(dateString instanceof Date)) {
        if (dateString.toString().indexOf('/') > -1) {
            dateString = new Date(dateString);
        } else if (dateString.length == 8) {
            dateString = new Date(dateString.substr(0, 4) + '/' + dateString.substr(4, 2) + '/' + dateString.substr(6, 2));
        } else
            dateString = '';
    }

    if (dateString.toString().length > 0) {
        sYear = (dateString.getFullYear() - 1911).toString().length < 3 ? '0' + (dateString.getFullYear() - 1911).toString() : (dateString.getFullYear() - 1911).toString();
        sMonth = (dateString.getMonth() + 1).toString().length < 2 ? '0' + (dateString.getMonth() + 1).toString() : (dateString.getMonth() + 1).toString();
        sDate = dateString.getDate().toString().length < 2 ? '0' + dateString.getDate().toString() : dateString.getDate().toString();
        returnValue = sYear.toString() + sMonth.toString() + sDate.toString();
    }

    return returnValue;
}



/**
 * 取得民國年月日(含/)
 * @param dateString 欲填入之日期
 */
function getROCDateSlash(dateString) {
    var sYear, sMonth, sDate;
    var returnValue = '';

    if (!(dateString instanceof Date)) {
        if (dateString.toString().indexOf('/') > -1) {
            dateString = new Date(dateString);
        } else if (dateString.length == 8) {
            dateString = new Date(dateString.substr(0, 4) + '/' + dateString.substr(4, 2) + '/' + dateString.substr(6, 2));
        } else
            dateString = '';
    }

    if (dateString.toString().length > 0) {
        sYear = (dateString.getFullYear() - 1911).toString().length < 3 ? '0' + (dateString.getFullYear() - 1911).toString() : (dateString.getFullYear() - 1911).toString();
        sMonth = (dateString.getMonth() + 1).toString().length < 2 ? '0' + (dateString.getMonth() + 1).toString() : (dateString.getMonth() + 1).toString();
        sDate = dateString.getDate().toString().length < 2 ? '0' + dateString.getDate().toString() : dateString.getDate().toString();
        returnValue = sYear.toString() + '/' + sMonth.toString() + '/' + sDate.toString();
    }

    return returnValue;
}

/**
 * 取得西元年月日(不含/)
 * @param dateString 欲填入之日期
 */
function getADDate(dateString) {
    var sYear, sMonth, sDate;
    var returnValue = '';

    if (isNaN(dateString.replace(/\//g, ''))) {
        dateString = '';
    }

    if (dateString.indexOf('/') == -1 && dateString.length == 7) {
        dateString = dateString.substr(0, 3) + '/' + dateString.substr(3, 2) + '/' + dateString.substr(5, 2);
    } else if (dateString.indexOf('/') == -1 && dateString.length == 8)
        dateString = dateString.substr(0, 4) + '/' + dateString.substr(4, 2) + '/' + dateString.substr(6, 2);

    if (dateString.toString().length > 0 && dateString.length != 8) {
        sYear = (parseInt(dateString.split('/')[0]) > 1911 ? dateString.split('/')[0] : (parseInt(dateString.split('/')[0]) + 1911).toString());
        sMonth = dateString.split('/')[1].length == 1 ? '0' + dateString.split('/')[1] : dateString.split('/')[1];
        sDate = dateString.split('/')[2].length == 1 ? '0' + dateString.split('/')[2] : dateString.split('/')[2];
        returnValue = sYear.toString() + sMonth.toString() + sDate.toString();
    } else
        returnValue = dateString;

    return returnValue;
}

/**
 * 取得西元年月日(含/)
 * @param dateString 欲填入之日期
 */
function getADDateSlash(dateString) {
    var sYear, sMonth, sDate;
    var returnValue = '';

    if (isNaN(dateString.replace(/\//g, ''))) {
        dateString = '';
    }

    if (dateString.indexOf('/') == -1 && dateString.length == 8) {
        dateString = dateString.substr(0, 4) + '/' + dateString.substr(4, 2) + '/' + dateString.substr(6, 2);
    }

    if (dateString.toString().length > 0 && dateString.length > 8) {
        sYear = parseInt(dateString.split('/')[0]) > 1911 ? dateString.split('/')[0] : (parseInt(dateString.split('/')[0]) + 1911).toString();
        sMonth = dateString.split('/')[1].length == 1 ? '0' + dateString.split('/')[1] : dateString.split('/')[1];
        sDate = dateString.split('/')[2].length == 1 ? '0' + dateString.split('/')[2] : dateString.split('/')[2];
        returnValue = sYear.toString() + '/' + sMonth.toString() + '/' + sDate.toString();
    } else
        returnValue = dateString;

    return returnValue;
}

/**
 * 設定國人對應民國年；外國人對應西元年
 * @param inputColumn 年別判斷來源欄位
 * @param outputcolumn 判斷後異動之欄位
 * @param hasDate 是否需要異動相關日期輸入格式
 * @param dateColumn 需異動之日期欄位
 * @param imgId 需異動之日期所屬日曆按鈕
 */
function changeYearType(inputColumn, outputColumn, hasDate, dateColumn, imgId) {
    $('#' + outputColumn).val($('#' + inputColumn).val());
    if (hasDate != '') {
        changeDateFormat(dateColumn, imgId, $('#' + outputColumn).val());
    }
}

/**
 * 因應民國年或西元年選項修改日期輸入格式
 * @param dateColumn 需異動之日期欄位
 * @param imgId 需異動之日期所屬日曆按鈕
 * @param strType 0:年前;1:民國;2:西元
 */
function changeDateFormat(dateColumn, imgId, strType) {
    if (strType == '2') {
        if ($('#' + dateColumn).val().length != 8)
            $('#' + dateColumn).val('');
    } else {
        if ($('#' + dateColumn).val().length != 7)
            $('#' + dateColumn).val('');
    }
}

/**
 * 切換tab
 * @param tabActive 欲顯示tab
 * @param tab 欲隱藏tab
 */
function goTab(tabActive, tab) {
    $('#' + tabActive).attr('class', 'tab-pane active');
    $('#' + tab).attr('class', 'tab-pane');
    window.scrollTo(0, 0);
}

/**
 * show massege dialog
 * @param dialogName 欲顯示之dialogName
 * @param action dialog show or hide
 */
function controlDialog(dialogName, action) {
    $('#' + dialogName).dialog(action);
}

function getFuncList() {
    $.ajax({
        url: 'IndexQueryServlet',
        type: 'POST',
        datatype: 'json',
        data:
                {
                    ajaxAction: 'FUNCLIST'
                },
        success: function (data, textStatus, xhr) {
            $('#list li').remove();

            var strAppend = '';
            for (var i = 0; i < data.formData.length; i++) {
                var funID = data.formData[i].OP_FUNC_GROUP;
                $('#' + funID).removeAttr('style');
            }
            $('#list').append(strAppend);

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus + " " + errorThrown);
        }
    });
}

function clearSession() {
    $.ajax({
        url: 'UtilServlet',
        type: 'POST',
        datatype: 'json',
        data: {ajaxAction: 'CLEARSESSION'},
        async: false,
        success: function (data, textStatus, xhr) {
            if (data.formData) {
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            notyMsg(textStatus + " " + errorThrown, 'error');
        }
    })
}

function expandQuery(e) {
    var evt = e || window.event; // this assign evt with the event object
    var current = evt.target || evt.srcElement; // this assign current with the event target
    if (current.className != 'tools' && current.className != 'collapse' && current.className != 'expand') {
        $(".expand").removeClass("expand").addClass("collapse");
        $("#queryporlet").show();
    }
}

function toogleQuery(e) {
    var evt = e || window.event; // this assign evt with the event object
    var current = evt.target || evt.srcElement; // this assign current with the
    if (current.className == 'expand') {
        $(".expand").removeClass("expand").addClass("collapse");
        $("#queryporlet").show();
    } else {
        $(".collapse").removeClass("collapse").addClass("expand");
        $("#queryporlet").hide();
    }
}

function initUI(inputID, inputTitle) {
    $('#' + inputID).dialog({
        closeOnEscape: false,
        closeText: "hide",
        autoOpen: false,
        modal: true,
        minHeight: "auto",
        width: 900, //lg
        title: inputTitle,
        open: function (event, ui) {
            //$('#modal-notify').css('overflow', 'hidden'); // this line does
        }
    });


}

/*
 * 顯示BlockUI
 * @param msg:顯示的內容訊息
 */
var show_BlockUI_page = function (msg, $area) {
    if ($.isBlank(msg))
        msg = '執行中…';
    if (parent.$.blockUI && parent.$.unblockUI) {
        var blockui_option = {
            css: {
                border: 'none',
                padding: '10px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .7,
                color: '#fff',
                'font-family': 'Lucida Grande,Lucida Sans,Arial,sans-serif'
            },
            message: '<h2><img src="assets/img/ajax-loader.gif" />　' + msg + '</h2>',
            overlayCSS: {
                backgroundColor: '#000',
                opacity: 0.2,
                cursor: 'wait'
            }
        };
        if ($.isBlank($area))
            parent.$.blockUI(blockui_option);
        else
            $area.block(blockui_option);
    }
}

var show_BlockUI_page_noParent = function (msg, $area) {
    if ($.isBlank(msg))
        msg = '執行中…';
    if ($.blockUI && $.unblockUI) {
        var blockui_option = {
            css: {
                border: 'none',
                padding: '10px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .7,
                color: '#fff',
                'font-family': 'Lucida Grande,Lucida Sans,Arial,sans-serif'
            },
            message: '<h2><img src="assets/img/ajax-loader.gif" />　' + msg + '</h2>',
            overlayCSS: {
                backgroundColor: '#000',
                opacity: 0.2,
                cursor: 'wait'
            }
        };
        if ($.isBlank($area))
            $.blockUI(blockui_option);
        else
            $area.block(blockui_option);
    }
}


// 因為fileDownload 會需要在cookie中加入fileDownload=true 才會觸發 successCallback的事件，
// 所以需要在每個檔案下載之前把cookie裡面的fileDownload=true清除，不然會被一直默認檔案下載成功而不會觸發查無資料的訊息
function deleteFileDownloadCookie()
{
    document.cookie = 'fileDownload=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function downloadReport(url, data)
{
    deleteFileDownloadCookie();

    if (data.lockType == "A") {
        show_BlockUI_page_noParent('報表產製中…');
    } else {
        show_BlockUI_page('報表產製中…');
    }
    /*fileDownload 的httpMethod=post，會自動產生iframe tag 資料，
     * 不能把iframe拿掉，因為日曆控製項有錯或印下張報表印不出來
     * 設為POST，failCallback 回傳responseHtml，會被吃掉(寫在iframe裡)，且responseHtml會變成空白*/
    $.fileDownload(url, {
        httpMethod: "POST",
        data: data,
        popupWindowTitle: '',
        successCallback: function (url)
        {
            if (data.lockType == "A") {
                $.unblockUI();
            } else {
                parent.$.unblockUI();
            }
//        			resetCountDownTimer();  //啟用倒數計時功能時請 unmark
            notyMsg('檔案下載成功', 'success');
//            $.alert.open('訊息', '檔案下載成功!');
            //$('iframe').css({"border": "0px"});
        },
        failCallback: function (responseHtml, url)
        {
            if (data.lockType == "A") {
                $.unblockUI();
            } else {
                parent.$.unblockUI();
            }
            var msg = responseHtml.toString().split("##");
            if (msg.length > 1)
            {
                // alert(msg[0]);
//        				$.alert.open('訊息', msg[0]);
            } else
                // alert('檔案產生失敗!');
                $.alert.open('error', '檔案產生失敗!');
            //resetCountDownTimer(); //啟用倒數計時功能時請 unmark			
//        			var url = 'data:application/octet-stream,hello%20world';
//        			$('iframe[src*=blank]').attr('src', url).hide()// .appendTo('body');
        }
    })
}
function dateRange(inputID1, inputID2) {
    var date = new Date();
    var y = date.getFullYear() - 1911;
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var by = y;
    var bm = m - 1;
    if (m === 1) {
        bm = 12;
        by = y - 1;
    }
    if (bm < 10) {
        bm = "0" + bm;
    }
    if (m < 10) {
        m = "0" + m;
    }
    if (d < 10) {
        d = "0" + d;
    }
    $("#" + inputID1).val(by + "/" + bm + "/" + d);
    $("#" + inputID2).val(y + "/" + m + "/" + d);
}

function dateRangeForThree(inputID1, inputID2) {
    var date = new Date();
    var y = date.getFullYear() - 1911;
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var by = y;
    var bm = m - 3;
    if (m === 1) {
        bm = 12;
        by = y - 1;
    }
    if (bm < 10) {
        bm = "0" + bm;
    }
    if (m < 10) {
        m = "0" + m;
    }
    if (d < 10) {
        d = "0" + d;
    }
    $("#" + inputID1).val(by + "/" + bm + "/" + d);
    $("#" + inputID2).val(y + "/" + m + "/" + d);
}

function dateRangeForOneYear(inputID1, inputID2) {
    var date = new Date();
    var y = date.getFullYear() - 1911;
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var by = (date.getFullYear() - 1) - 1911;
    var bm = m;
    if (m === 1) {
        //bm = 12;
        by = y - 1;
    }
    if (bm < 10) {
        bm = "0" + bm;
    }
    if (m < 10) {
        m = "0" + m;
    }
    if (d < 10) {
        d = "0" + d;
    }
    $("#" + inputID1).val(by + "/" + bm + "/" + d);
    $("#" + inputID2).val(y + "/" + m + "/" + d);
}

function compareOneYear(inputID1, inputID2) {
    var returnValue = true;
    var startDate = $("#" + inputID1).val();
    var endDate = $("#" + inputID2).val();

    var startY = startDate.split("/")[0];
    var startM = startDate.split("/")[1];
    var startD = startDate.split("/")[2];

    var endY = endDate.split("/")[0];
    var endM = endDate.split("/")[1];
    var endD = endDate.split("/")[2];


    if (endY - startY > 1) {
        returnValue = false;
        return returnValue;
    } else if (endY > startY && (endM - startM) > 0) {
        returnValue = false;
        return returnValue;
    } else if (endY > startY && (endM > startM) && (endD - startD) > 1) {
        returnValue = false;
        return returnValue;
    } else if (endY === startY && (endM === startM) && (endD < startD)) {
        returnValue = false;
        return returnValue;
    } else if (endY > startY && (endM === startM) && (startD < endD)) {
        returnValue = false;
        return returnValue;
    } else {
        return returnValue;
    }
}

function fullToHalf(val) {
    var value = val || "";
    var result = "";
    if (value) {
        for (i = 0; i <= value.length; i++) {
            if (value.charCodeAt(i) == 12288) {
                result += " ";
            } else {
                if (value.charCodeAt(i) > 65280 && value.charCodeAt(i) < 65375) {
                    result += String.fromCharCode(value.charCodeAt(i) - 65248);
                } else {
                    result += String.fromCharCode(value.charCodeAt(i));
                }
            }
        }
    }
    return result;
}

//Grid列表標色
$.fn.extend({
    setJgridRowCSS: function () {
        var self = this;
        $("tr:odd", $(self)).addClass("oddTD");
        $("tr:even", $(self)).addClass("evenTD");
        $("tr", $(self)).hover(function () {
            $(this).addClass('hoverTD');
            $(this).removeClass('table-td-content');
        }, function () {
            $(this).removeClass('hoverTD');
            $(this).addClass('table-td-content');
        });
    }
});

//202403
//委託查詢共用select(用途)
function appendSelectFunction(appUseType, appUseSubType, appUseCustom) {

    $('#' + appUseCustom).css({'display': ''});  //202504

    if ($('#' + appUseType).length > 0) {
        //初始化委託查詢用途select
        //『300行政類 301受處理報案』
        $.ajax({
            async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
            url: "UtilServlet",
            dataType: 'json',
            data: {
                ajaxAction: 'appendSelect',
                queryType: 'appUse'
            },
            success: function (data, textStatus, xhr) {
                processAjaxMsg(data);
                var formData = data.formData;
                appendSelectOptions(appUseType, formData, 'E0_USENAME', 'E0_USECODE');

                //$('#' + appUseType).val('600').trigger('click');       //預設自行輸入
                //$('#' + appUseCustom).val(getUserRoleName(maxOwnRole));//帶入各角色自行輸入的值
                //202403 警署承辦人說預設選 行政類300 受理案件處理301
                  $('#' + appUseType).val('300');                        //預設『行政類』『300』
                  $('#' + appUseType).val('300').trigger('click');       //預設『行政類』『300』

            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });

        //202403 
                var selectObj = document.getElementById(appUseSubType);
              //selectObj.options.length = 0;
              //selectObj.options[0] = new Option('請選擇', '');
                $.ajax({
                    async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
                    url: "UtilServlet",
                    dataType: 'json',
                    data: {
                        ajaxAction: 'appendSubSelect',
                        queryType: 'appSubUse',
                        selectAppUse: this.value
                    },
                    success: function (data, textStatus, xhr) {
                        processAjaxMsg(data);
                        var formData = data.formData;
                        appendSelectOptions(appUseSubType, formData, 'E0_USENAME', 'E0_USECODE');
                         $('#' + appUseSubType).val('301');                        //預設『行政類』『300』301受處理報案
                         $('#' + appUseSubType).val('301').trigger('click');       //預設『行政類』『300』301受處理報案
                         $('#' + appUseCustom).css({'display': ''});  //202504   
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });

        //202504
        //委託查詢select綁事件  //onchange 的時侯發生
//        $('#' + appUseType).on('change', function () {
//            if (this.value == '600') {
//                $('#' + appUseSubType).css({'display': 'none'});
//                $('#' + appUseCustom).css({'display': ''});
//            } else {
//                $('#' + appUseSubType).css({'display': ''}); 
//                $('#' + appUseCustom).css({'display': 'none'});
//                var selectObj = document.getElementById(appUseSubType);
//                selectObj.options.length = 0;
//                selectObj.options[0] = new Option('請選擇', '');
//                $.ajax({
//                    async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
//                    url: "UtilServlet",
//                    dataType: 'json',
//                    data: {
//                        ajaxAction: 'appendSelect',
//                        queryType: 'appSubUse',
//                        selectAppUse: this.value
//                    },
//                    success: function (data, textStatus, xhr) {
//                        processAjaxMsg(data);
//                        var formData = data.formData;
//                        appendSelectOptions(appUseSubType, formData, 'E0_USENAME', 'E0_USECODE');
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//                    }
//                });
//            }
//        });
        
        //202504 改成不管選什麼都要秀自填自由欄位並檢核必填非空白
        //委託查詢select綁事件  //onchange 的時侯發生
        $('#' + appUseType).on('change', function () {
            //202504
            if (this.value == '600') {
                $('#' + appUseSubType).css({'display': 'none'});
            }
    
                $('#' + appUseCustom).css({'display': ''});
                var selectObj = document.getElementById(appUseSubType);
                selectObj.options.length = 0;
                selectObj.options[0] = new Option('請選擇', '');
                $.ajax({
                    async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
                    url: "UtilServlet",
                    dataType: 'json',
                    data: {
                        ajaxAction: 'appendSelect',
                        queryType: 'appSubUse',
                        selectAppUse: this.value
                    },
                    success: function (data, textStatus, xhr) {
                        processAjaxMsg(data);
                        var formData = data.formData;
                        appendSelectOptions(appUseSubType, formData, 'E0_USENAME', 'E0_USECODE');
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
        });
    }
}


//委託查詢共用select(用途)
function appendSelectFunctionDefault(appUseType, appUseSubType, appUseCustom) {
    
    
    if ($('#' + appUseType).length > 0) {
        //初始化委託查詢用途select
        //『300行政類 301受處理報案』

        //202403 『300行政類 301受處理報案』
        $.ajax({
            async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
            url: "UtilServlet",
            dataType: 'json',
            data: {
                ajaxAction: 'appendSubSelect',
                queryType: 'appSubUse'
            },
            success: function (data, textStatus, xhr) {
                processAjaxMsg(data);
                var formData = data.formData;
                appendSelectOptions(appUseSubType, formData, 'E0_USENAME', 'E0_USECODE');

                //$('#' + appUseType).val('600').trigger('click');       //預設自行輸入
                //$('#' + appUseCustom).val(getUserRoleName(maxOwnRole));//帶入各角色自行輸入的值
                //202403 警署承辦人說預設選 行政類300 受理案件處理301
                  $('#' + appUseSubType).val('301');                   //預設『300行政類 301受處理報案』
                  $('#' + appUseSubType).val('301').trigger('click');  //預設『300行政類 301受處理報案』
                
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });

    }
}



//委託查詢共用select(用途)
//function appendSelectFunction(appUseType, appUseSubType, appUseCustom) {
//    if ($('#' + appUseType).length > 0) {
//        //初始化委託查詢用途select
//
//        $.ajax({
//            async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
//            url: "UtilServlet",
//            dataType: 'json',
//            data: {
//                ajaxAction: 'appendSelect',
//                queryType: 'appUse'
//            },
//            success: function (data, textStatus, xhr) {
//                processAjaxMsg(data);
//                var formData = data.formData;
//                appendSelectOptions(appUseType, formData, 'E0_USENAME', 'E0_USECODE');
//                $('#' + appUseType).val('600').trigger('click');//預設自行輸入
//                //$('#' + appUseCustom).val(getUserRoleName(maxOwnRole));//帶入各角色自行輸入的值
//            },
//            error: function (jqXHR, textStatus, errorThrown) {
//            }
//        });
//
//        //委託查詢select綁事件
//        $('#' + appUseType).on('change', function () {
//
//            if (this.value == '600') {
//                $('#' + appUseSubType).css({'display': 'none'});
//                $('#' + appUseCustom).css({'display': ''});
//            } else {
//                $('#' + appUseSubType).css({'display': ''});
//                $('#' + appUseCustom).css({'display': 'none'});
//                var selectObj = document.getElementById(appUseSubType);
//                selectObj.options.length = 0;
//                selectObj.options[0] = new Option('請選擇', '');
//                $.ajax({
//                    async: false, // 設定同步以避免頁框出來但是內容來不及顯現更改的狀況
//                    url: "UtilServlet",
//                    dataType: 'json',
//                    data: {
//                        ajaxAction: 'appendSelect',
//                        queryType: 'appSubUse',
//                        selectAppUse: this.value
//                    },
//                    success: function (data, textStatus, xhr) {
//                        processAjaxMsg(data);
//                        var formData = data.formData;
//                        appendSelectOptions(appUseSubType, formData, 'E0_USENAME', 'E0_USECODE');
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//                    }
//                });
//            }
//        });
//    }
//}

/**
 * pure javascript add select options
 * 
 * @selectId select ID
 * @data option data
 * @key option key name
 * @value option value name
 */
function appendSelectOptions(selectId, data, key, value) {
    var getSelectObj = document.getElementById(selectId);

    if (getSelectObj == null) {
        return false;
    }
    var i = 0, j = data.length;
    for (i; i < j; i++) {
        var genOptions = document.createElement("option");
        var oText = document.createTextNode(data[i][key]);
        genOptions.appendChild(oText);
        genOptions.setAttribute("value", data[i][value]);
        getSelectObj.appendChild(genOptions);
    }
}


//委託查詢共用功能
function modalQueryFun() {
    //初始化委託查詢的select
    
    //202403預設是『300行政類 301受處理報案』
      appendSelectFunction('appUseType', 'appUseSubType', 'appUseCustom');
    //預設是『300行政類 301受處理報案』
    //appendSelectFunctionDefault('appUseType', 'appUseSubType', 'appUseCustom');

}

function checkAppUse() {
    let appUseType = $('#appUseType').val();
    //202504
    //if (appUseType == '600') {
    //    let appUseCustom = $('#appUseCustom').val();
    //    if (appUseCustom.trim() == "") {
    //        return '請輸入用途';
    //    } else {
    //        return '';
    //    }
    //} else {
        //202504
        if (appUseType == ""){
            return '請選擇用途大類';
        }
        let appUseSubType = $('#appUseSubType').val();
        if ((appUseType != '600' ) && (appUseSubType == '')) {
            return '請選擇用途小類';
        }
        //202504 刪除半空全空後如果仍是空白，請填用途說明
        if ($('#appUseCustom_Show').length > 0) {
            // ID 為 appUseCustom_Show 的元素存在，執行相應操
            let appUseCustom_Show = $('#appUseCustom_Show').val();        
            if (appUseCustom_Show.trim() == "") { 
                $('#appUseCustom_Show').css('backgroundColor', '#FFFF00');            
                return '請輸入用途說明';
            } else {
                 // 該元素不存在，可考慮進行其他處理
                 
            }    
        }
        return '';
}

function getAppUse() {
    let OPR_KIND = "";
    let OPR_PURP = "";
    let applist = {};
    let appUseType = $('#appUseType').val();
    if (appUseType == '600') {
        applist.OPR_KIND = '600';
        applist.OPR_PURP = getSelectedText('appUseType') + "/" + $('#appUseCustom_Show').val().trim();  //202504
    } else {
        //202504 判斷這個#appUseCustom_Show 在不在，在的話表示是用 AppUse.jsp 沒有的話表示是用AppUseHide.jsp 
        if ($('#appUseCustom_Show').length > 0) {
            // ID 為 appUseCustom_Show 的元素存在，執行相應操作
            applist.OPR_KIND = $('#appUseSubType').val();
            applist.OPR_PURP = getSelectedText('appUseType') + "/" + getSelectedText('appUseSubType')+ "/" + $('#appUseCustom_Show').val().trim(); //202504 現已變成空白必填，所以這一欄就要必帶入。
        } else {
            // 該元素不存在，可考慮進行其他處理
            applist.OPR_KIND = $('#appUseSubType').val();
            applist.OPR_PURP = getSelectedText('appUseType') + "/" + getSelectedText('appUseSubType')+ "/" + $('#appUseCustom').val().trim(); //202504 現已變成空白必填，所以這一欄就要必帶入。
        }
    }
    
    return applist;
}

function getSelectedText(list) {
    // Get the dropdown element
    let dropdown = document.getElementById(list);

    // Get the selected option
    let selectedOption = dropdown.options[dropdown.selectedIndex];

    // Get the text content of the selected option
    let selectedText = selectedOption.text;  

    // Display the selected text
    return selectedText;
}
//202504 打算把檢核用途說明寫在這兒    
//    //取得戶籍資料   
//    $('#IPUBASIC_GET_HH_DATA').click(function () {
//        Validator.init(document.getElementById('form1'));
//        //證號
//        if ($("#IPUBASIC_OP_PUPO_NAT_CD").val() == "035" && $("#IPUBASIC_OP_PUPO_IDN_TP").val() == "2") {
//            Validator.setMessage("欄位 [ 證號 ] 為\"其他證號\"時，[ 國籍 ]不得為中華民國國籍 !!");
//            Validator.setBGColor("IPUBASIC_OP_PUPO_NAT_CD", "IPUBASIC_OP_PUPO_IDN_TP");
//        } else if ($("#IPUBASIC_OP_PUPO_NAT_CD").val() != "" && $("#IPUBASIC_OP_PUPO_NAT_CD").val() != "035" && $("#IPUBASIC_OP_PUPO_IDN_TP").val() == "1") {
//            Validator.setMessage("欄位 [ 證號 ] 為\"身分證號\"時，[ 國籍 ]不得為他國國籍 !!");
//            Validator.setBGColor("IPUBASIC_OP_PUPO_NAT_CD", "IPUBASIC_OP_PUPO_IDN_TP");
//        } else if ($("#IPUBASIC_OP_PUPO_IDN_TP").val() == "1") {
//            Validator.checkID("IPUBASIC_OP_PUPO_IDN", true, "身分證號");
//        } else if ($("#IPUBASIC_OP_PUPO_IDN_TP").val() == "3") {
//            Validator.checkLength("IPUBASIC_OP_PUPO_IDN", false, "身分證號", 20);
//        } else {
//            Validator.checkLength("IPUBASIC_OP_PUPO_IDN", true, "身分證號", 20);
//        }
//        if (Validator.isValid())
//            getOI2PersonByIDN("BASIC");
//        else {
//            Validator.showMessage(); //檢核不通過，則顯示錯誤提示
//            return false;
//        }
//    });

