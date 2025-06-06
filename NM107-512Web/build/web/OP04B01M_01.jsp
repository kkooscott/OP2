<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        
        <br><br>
        <div id = "tab_4_8_alert">
            <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
        </div>
        <div id="tab_4_8_main" style="display:none">
            <div>
                    <div class="table-header clearfix">
                            <div class="modal-header">
                                    <h4 class="modal-title">受理資訊</h4>
                            </div>
                    </div>
                    <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                            <tr>
                                    <td class="labelField" width="10%">受理單位：</td>
                                    <td class="dataField"  width="40%">
                                        <select class="form-control" id="OP_AC3_unitLevel2"
                                                name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                                onchange="bindUNITDDL('3', 'OP_AC3_unitLevel2', 'OP_AC3_unitLevel3', 'OP_AC3_unitLevel4', '');">
                                        </select> 
                                        <select class="form-control" id="OP_AC3_unitLevel3"
                                                name="unitLevel3" disabled="disabled"
                                                onchange="bindUNITDDL('4', 'OP_AC3_unitLevel2', 'OP_AC3_unitLevel3', 'OP_AC3_unitLevel4', '');">
                                        </select> 
                                        <select class="form-control" id="OP_AC3_unitLevel4"
                                                name="unitLevel4" disabled="disabled">
                                        </select>
                                    </td>
                                    <td class="labelField" width="10%">電話：</td>
                                    <td class="dataField"  width="40%">
                                        <input type="text" class="form-control" size="30" maxlength="20" id="IPUANANN_OP_AC_UNIT_TEL" disabled="disabled"/>
                                    </td>
                            </tr>
                    </table>
            </div>
            <div>
                    <div class="table-header clearfix">
                            <div class="modal-header">
                                    <h4 class="modal-title">公告資訊</h4>
                            </div>
                    </div>
                    <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                            <tr>
                                    <td class="labelField" width="10%">
                                        <img src="assets/img/required.png" />發文文號：</td>
                                    <td class="dataField" colspan="3">
                                        <input type="text" class="form-control" size="30" maxlength="20" id="IPUANANN_OP_PUPO_DOC_WD" disabled="disabled"/>字第
                                        <input type="text" class="form-control" size="11" maxlength="11" id="IPUANANN_OP_PUPO_DOC_NO" disabled="disabled"/>號
                                    </td>

                            </tr>
                            <tr>	
                                    <td class="labelField" width="10%">發文單位：</td>
                                    <td class="dataField" width="40%">
                                        <input type="text" class="form-control" size="50" maxlength="67" id="IPUANANN_OP_PUPOANUNIT_ALL" disabled="disabled"/>
                                    </td>
                                    <td class="labelField" width="10%">公告日期：</td>
                                    <td class="dataField" width="40%">
                                        <input type="text"
                                                class="form-control" id="IPUANANN_OP_NTC_PUPO_DT" value=""  disabled="disabled"
                                                onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                        <img    onclick="WdatePicker({el:'IPUANANN_OP_NTC_PUPO_DT',dateFmt:'yyy/MM/dd'})"
                                                onfocus="WdatePicker({el:'IPUANANN_OP_NTC_PUPO_DT',dateFmt:'yyy/MM/dd'})"
                                                src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                                style="cursor: pointer" />
                                    </td>
                            </tr>
                            <tr>
                                    <td class="labelField" width="10%"><img src="assets/img/required.png" />公告內容：</td>
                                    <td class="dataField" colspan="3">
                                        <textarea class="form-control" id="IPUANANN_OP_PUPO_AN_CONT" name="IPUANANN.OP_PUPO_AN_CONT" size="500"
                                                rows="5" maxlength="600"" style="width: 100%;" disabled="disabled"></textarea>
                                    </td>
                            </tr>
                            <tr>
                                    <td class="labelField" width="10%">拾得日期時間：</td>
                                    <td class="dataField" colspan="3">
                                        <input type="text"
                                                class="form-control" id="IPUANANN_OP_PU_DATE" value="" disabled="disabled"
                                                onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                        <img    onclick="WdatePicker({el:'IPUANANN_OP_PU_DATE',dateFmt:'yyy/MM/dd'})"
                                                onfocus="WdatePicker({el:'IPUANANN_OP_PU_DATE',dateFmt:'yyy/MM/dd'})"
                                                src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                                style="cursor: pointer" />
                                        <input  type="text" class="form-control" id="IPUANANN_OP_PU_TIME" disabled="disabled"
                                                name="IPUANANN.OP_PU_TIME" value="" size="10"
                                                onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                                onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                        <img    id="OC_TIMEIMG"
                                                onclick="WdatePicker({el: 'IPUANANN_OP_PU_TIME', dateFmt: 'HH:mm'})"
                                                onfocus="WdatePicker({el: 'IPUANANN_OP_PU_TIME', dateFmt: 'HH:mm'})"
                                                src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                                style="cursor: pointer" />
                                    </td>
                            </tr>
                            <tr>
                                    <td class="labelField" width="10%"> 拾得地點：</td>
                                    <td class="dataField" colspan="3">
                                        <select class="form-control" id="IPUANANN_OP_PU_CITY_CD"
                                                name="IPUANANN.OP_PU_CITY_CD" data-width="180px" data-size="8" disabled="disabled"
                                                onchange="bindCOUNTRYDDL('3', 'IPUANANN_OP_PU_CITY_CD', 'IPUANANN_OP_PU_TOWN_CD', '', '', '');">
                                        </select>
                                        <select class="form-control" id="IPUANANN_OP_PU_TOWN_CD" disabled="disabled"
                                                name="IPUANANN.OP_PU_TOWN_CD" data-width="180px" data-size="8">
                                        </select>
                                        <input type="text"
                                                class="form-control" size="100" maxlength="100" disabled="disabled"
                                                id="IPUANANN_OP_PU_PLACE" />
                                    </td>							
                            </tr>
                    </table>
            </div>
        </div>
        
<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP04B01M_01.js"></script>