<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="saveBasicBtn">
            <i class="fa fa-save"></i>&nbsp;儲存
        </button>
        <br><br>
        <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">受理單位資料</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <!--202404 哪天想秀在畫面上就用AppUse.jsp-->
                        <jsp:include page="AppUseHide.jsp" /> 
                        <tr>
                                <td class="labelField">受理單位：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="OP_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            onchange="bindUNITDDL('4', 'OP_unitLevel2', 'OP_unitLevel3', 'OP_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">受理人員：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IPUBASIC_OP_AC_STAFF_NM" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">收據號碼：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="17"  id="IPUBASIC_OP_AC_RCNO" disabled="disabled"/>
                                </td>
                        </tr>
                        <tr>						
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />受理日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IPUBASIC_OP_AC_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUBASIC_OP_AC_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUBASIC_OP_AC_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                                <td class="labelField" width="10%">填單日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IPUBASIC_OP_FM_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            disabled="disabled"> 
                                    <img    onclick="WdatePicker({el:'IPUBASIC_OP_FM_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUBASIC_OP_FM_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />電話：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="20" id="IPUBASIC_OP_AC_UNIT_TEL"/>
                                </td>
                                <td class="labelField" width="10%">手開單編號：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="17" id="IPUBASIC_OP_MANUAL_RCNO"/>
                                </td>
                        </tr>
                </table>
        </div>
        <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">拾得人基本資料</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <tr id="value500OpIsCust" style="display:none">
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />民眾自行保管：</td>
                                <td class="dataField" width="40%">
                                    <input type="radio" name="IPUBASIC.OP_IS_CUST" title="是" id="IPUBASIC_OP_IS_CUST_1" value="1">是
                                    <input type="radio" name="IPUBASIC.OP_IS_CUST" title="否" id="IPUBASIC_OP_IS_CUST_2" value="0">否
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />是否具名：</td>
                                <td class="dataField" width="40%">
                                    <input type="radio" name="IPUBASIC.OP_IS_PUT_NM" title="具名" id="IPUBASIC_OP_IS_PUT_NM_1" value="1" checked="checked">具名
                                    <input type="radio" name="IPUBASIC.OP_IS_PUT_NM" title="不具名" id="IPUBASIC_OP_IS_PUT_NM_2" value="0">不具名
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />有無證件：</td>
                                <td class="dataField" width="40%">
                                    <input type="radio" name="IPUBASIC.OP_INCLUDE_CERT" title="有證件" id="IPUBASIC_OP_INCLUDE_CERT_1" value="1">有
                                    <input type="radio" name="IPUBASIC.OP_INCLUDE_CERT" title="無證件" id="IPUBASIC_OP_INCLUDE_CERT_2" value="0" checked="checked">無             
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />類別：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_PUPO_TP_CD"
                                            name="IPUBASIC.OP_PUPO_TP_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />物品屬性：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_PUOJ_ATTR_CD"
                                            name="IPUBASIC.OP_PUOJ_ATTR_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img name="NAME_CHECK" src="assets/img/required.png" />姓名：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="50" maxlength="50" id="IPUBASIC_OP_PUPO_NAME" />
                                </td>
                                <td class="labelField" width="10%">
                                    羅馬拼音：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="50" maxlength="50" id="IPUBASIC_OP_PUPO_RNAME" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img name="NAME_CHECK" src="assets/img/required.png" />證號：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_PUPO_IDN_TP">
                                        <option value="1">身分證號</option>
                                        <option value="2">其他證號</option>
                                        <option value="3">無證號</option>
                                    </select>
                                    <input type="text"
                                        class="form-control" size="30" maxlength="20"
                                        id="IPUBASIC_OP_PUPO_IDN" />
                                </td>
                                <td class="labelField" width="10%">
                                    <img name="NAME_CHECK" src="assets/img/required.png" />性別：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_PUPO_GENDER">
                                        <option value>請選擇...</option>
                                        <option value="1">男</option>
                                        <option value="2">女</option>
                                        <option value="3">法人</option>
                                        <option value="4">其他</option>
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%" >出生日期：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_PUPO_BEFROC">
                                        <option value="1">民國</option>
                                        <option value="0">民前</option>
                                        <option value="2">西元</option>
                                    </select>
                                    <input type="text"
                                        class="form-control" size="30" maxlength="8"
                                        id="IPUBASIC_OP_PUPO_BIRTHDT" />
                                    <br>
                                    <label class="normal-label">民前：001/01/01 -> 0010101</label>
                                    <br>
                                    <label class="normal-label">民國：099/01/01 -> 0990101</label>
                                    <br>
                                    <label class="normal-label">西元：2000/01/01 -> 20000101</label>
                                </td>
                                <td class="labelField" width="10%">
                                    <img name="NAME_CHECK" src="assets/img/required.png" />電話：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" size="30" maxlength="20"
                                            id="IPUBASIC_OP_PUPO_PHONENO" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">國籍：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_PUPO_NAT_CD"
                                            name="IPUBASIC.OP_PUPO_NAT_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">郵遞區號：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" size="30" maxlength="5"
                                            id="IPUBASIC_OP_PUPO_ZIPCODE" />
                                </td>												
                        </tr>
                        <tr>
                                <td class="labelField">
                                    <img name="NAME_CHECK" src="assets/img/required.png" />地址：</td>
                                <td class="dataField" colspan="3">
                                    <input type="radio" name="IPUBASIC.OP_OC_ADDR_TYPE_CD" title="一般地址" id="IPUBASIC_OP_OC_ADDR_TYPE_CD_1" value="1" checked="checked">一般地址
                                    <input type="radio" name="IPUBASIC.OP_OC_ADDR_TYPE_CD" title="自由輸入" id="IPUBASIC_OP_OC_ADDR_TYPE_CD_2" value="9">自由輸入
                                    <button class="btn green-seagreen btn-sm" type="button"
                                            id="IPUBASIC_GET_HH_DATA">
                                            <i class="fa fa-search"></i>&nbsp;取得戶籍資料
                                    </button>
                                    <div id="normalPattern">
                                        <select class="form-control" id="IPUBASIC_OP_PUPO_CITY_CD"
                                                name="IPUBASIC.OP_PUPO_CITY_CD" data-width="180px" data-size="8"
                                                onchange="bindCOUNTRYDDL('3', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', '');">
                                        </select>
                                        <select class="form-control" id="IPUBASIC_OP_PUPO_TOWN_CD"
                                                name="IPUBASIC.OP_PUPO_TOWN_CD" data-width="180px" data-size="8"
                                                onchange="bindCOUNTRYDDL('4', 'IPUBASIC_OP_PUPO_CITY_CD', 'IPUBASIC_OP_PUPO_TOWN_CD', 'IPUBASIC_OP_PUPO_VILLAGE_CD', '', '');">
                                        </select> 
                                        <select class="form-control" id="IPUBASIC_OP_PUPO_VILLAGE_CD"
                                                name="IPUBASIC.OP_PUPO_VILLAGE_CD" data-width="180px" data-size="8">
                                        </select>
                                        &nbsp;
                                        <input type="text"
                                                class="form-control" size="3" maxlength="10"
                                                id="IPUBASIC_OP_PUPO_LIN" />&nbsp;鄰
                                        <br>
                                        <input type="text"
                                                class="form-control" size="100" maxlength="40"
                                                id="IPUBASIC_OP_PUPO_ROAD" />
                                    </div>
                                    <div id="freePattern" style="display:none">
                                        <input type="text"
                                                class="form-control" size="100" maxlength="80"
                                                id="IPUBASIC_OP_PUPO_ADDR_OTH" />
                                    </div>
                                </td>												
                        </tr>
                        <tr>
                                <td class="labelField">電子郵件：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" size="50" maxlength="50"
                                            id="IPUBASIC_OP_PUPO_EMAIL" />
                                </td>												
                        </tr>
                </table>
        </div>
        <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">拾得地點時間資料</h4>
                        </div>
                </div>
                <table name="mainTB" border="0" width="100%" cellpadding="0"
                        cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />拾得日期時間：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" id="IPUBASIC_OP_PU_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUBASIC_OP_PU_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUBASIC_OP_PU_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                    <input  type="text" class="form-control" id="IPUBASIC_OP_PU_TIME"
                                            name="IPUBASIC.OP_PU_TIME" value="" size="10"
                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                    <img    id="OC_TIMEIMG"
                                            onclick="WdatePicker({el: 'IPUBASIC_OP_PU_TIME', dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({el: 'IPUBASIC_OP_PU_TIME', dateFmt: 'HH:mm'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">
                                        <img src="assets/img/required.png" />拾得地點：</td>
                                <td class="dataField">
                                    <select class="form-control" id="IPUBASIC_OP_PU_CITY_CD"
                                            name="IPUBASIC.OP_PU_CITY_CD" data-width="180px" data-size="8"
                                            onchange="bindCOUNTRYDDL('3', 'IPUBASIC_OP_PU_CITY_CD', 'IPUBASIC_OP_PU_TOWN_CD', '', '', '');">
                                    </select>
                                    <select class="form-control" id="IPUBASIC_OP_PU_TOWN_CD"
                                            name="IPUBASIC.OP_PU_TOWN_CD" data-width="180px" data-size="8">
                                    </select>
                                    <input type="text"
                                            class="form-control" size="100" maxlength="100"
                                            id="IPUBASIC_OP_PU_PLACE" />
                                </td>							
                        </tr>
                </table>
        </div>
        <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">其他資料</h4>
                        </div>
                </div>
                <table name="mainTB" border="0" width="100%" cellpadding="0"
                        cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">目前狀態：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="IPUBASIC_OP_CURSTAT_CD" disabled="disabled"
                                            name="IPUBASIC.OP_CURSTAT_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>						
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">招領人：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" size="80" maxlength="80"
                                            id="IPUBASIC_OP_NTC_FIND_PO" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">是否通知：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="IPUBASIC_OP_YN_NTC">
                                        <option value="0">未通知</option>
                                        <option value="1">已通知</option>
                                    </select>
                                    <label class="normal-label"><font color="red">(若選擇已通知，通知日期、通知方式、通知者身分則為必填欄位。)</font></label>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">通知日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IPUBASIC_OP_NTC_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUBASIC_OP_NTC_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUBASIC_OP_NTC_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                                <td class="labelField" width="10%">通知者身分：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUBASIC_OP_NTC_PSN_TYPE"
                                            name="IPUBASIC.OP_NTC_PSN_TYPE" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">通知方式：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" size="100" maxlength="100"
                                            id="IPUBASIC_OP_NTC_MODE" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">備註：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IPUBASIC_OP_PU_REMARK" name="IPUBASIC.OP_PU_REMARK" size="500"
                                            rows="5" maxlength="300" style="width: 100%;"></textarea>
                                </td>
                        </tr>													
                </table>
        </div>
        <div id="deleteData" style="display:none">
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">刪除資料</h4>
                        </div>
                </div>
                <table name="mainTB" border="0" width="100%" cellpadding="0"
                        cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">刪除單位：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="OP_DEL_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_DEL_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            onchange="bindUNITDDL('4', 'OP_DEL_unitLevel2', 'OP_DEL_unitLevel3', 'OP_DEL_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_DEL_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">刪除人員：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IPUBASIC_OP_DEL_STAFF_NM" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">刪除日期時間：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" id="IPUBASIC_OP_DEL_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUBASIC_OP_DEL_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUBASIC_OP_DEL_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                    <input  type="text" class="form-control" id="IPUBASIC_OP_DEL_TIME"
                                            name="IPUBASIC.OP_DEL_TIME" value="" size="10"
                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                    <img    id="OC_TIMEIMG2"
                                            onclick="WdatePicker({el: 'IPUBASIC_OP_DEL_TIME', dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({el: 'IPUBASIC_OP_DEL_TIME', dateFmt: 'HH:mm'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%"><img src="assets/img/required.png" />刪除原因：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IPUBASIC_OP_DEL_RSN" name="IPUBASIC.OP_DEL_RSN" size="300"
                                            rows="5" maxlength="100" style="width: 100%;"></textarea>
                                </td>
                        </tr>													
                </table>
        </div>
        

<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP02B01M_01.js"></script>