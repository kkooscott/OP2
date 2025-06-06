<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="saveAnnounceBtn">
            <i class="fa fa-save"></i>&nbsp;儲存並發佈內部公告
        </button>
        <br><br>
        <div id = "tab_4_5_alert">
            <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
        </div>
        <div id="tab_4_5_main" style="display:none">
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">內部公告</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField">受理單位：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="OP_AC1_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_AC1_unitLevel2', 'OP_AC1_unitLevel3', 'OP_AC1_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_AC1_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            onchange="bindUNITDDL('4', 'OP_AC1_unitLevel2', 'OP_AC1_unitLevel3', 'OP_AC1_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_AC1_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">受理日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_AC_DATE" value=""  disabled="disabled"
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_AC_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_AC_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">發文單位：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="OP_AN_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_AN_unitLevel2', 'OP_AN_unitLevel3', '', '');">
                                    </select> 
                                    <select class="form-control" id="OP_AN_unitLevel3"
                                            name="unitLevel3" disabled="disabled">
                                            <!--onchange="bindUNITDDL('4', 'OP_AN_unitLevel2', 'OP_AN_unitLevel3', 'OP_AN_unitLevel4', '');"-->
                                    </select> 
                                    <!--
                                    <select class="form-control" id="OP_AN_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                    -->
                                </td>
                                <td class="labelField" width="10%">櫃號：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_CABINET_NO"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField"><img src="assets/img/required.png" />保管單位：</td>
                                  <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_KP_UNIT_NM"/>
                                </td>
                                <td class="labelField" width="10%"><img src="assets/img/required.png" />保管單位承辦人：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_KP_NM"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />收受拾得物時間：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_KP_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_KP_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_KP_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                    <input  type="text" class="form-control" id="IANNOUNCE_OP_KP_TIME"
                                            name="IPUBASIC.OP_PU_TIME" value="" size="10"
                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                    <img    id="OC_TIMEIMG"
                                            onclick="WdatePicker({el: 'IANNOUNCE_OP_KP_TIME', dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({el: 'IANNOUNCE_OP_KP_TIME', dateFmt: 'HH:mm'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                            style="cursor: pointer" />
                                </td>
                                 <td class="labelField" width="10%">備註：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="100" maxlength="100" id="IANNOUNCE_OP_KP_REMARK"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">保管單位(1)：</td>
                                  <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_KP_UNIT_NM1"/>
                                </td>
                                <td class="labelField" width="10%">保管單位承辦人(1)：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_KP_NM1"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">收受拾得物時間：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_KP_DATE1" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_KP_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_KP_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                    <input  type="text" class="form-control" id="IANNOUNCE_OP_KP_TIME1"
                                            name="IPUBASIC.OP_PU_TIME" value="" size="10"
                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                    <img    id="OC_TIMEIMG"
                                            onclick="WdatePicker({el: 'IANNOUNCE_OP_KP_TIME', dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({el: 'IANNOUNCE_OP_KP_TIME', dateFmt: 'HH:mm'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                            style="cursor: pointer" />
                                </td>
                                 <td class="labelField" width="10%">備註：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="100" maxlength="100" id="IANNOUNCE_OP_KP_REMARK1"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">保管單位(2)：</td>
                                  <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_KP_UNIT_NM2"/>
                                </td>
                                <td class="labelField" width="10%">保管單位承辦人(2)：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="10" maxlength="10" id="IANNOUNCE_OP_KP_NM2"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">收受拾得物時間：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_KP_DATE2" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_KP_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_KP_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                    <input  type="text" class="form-control" id="IANNOUNCE_OP_KP_TIME2"
                                            name="IPUBASIC.OP_PU_TIME" value="" size="10"
                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                    <img    id="OC_TIMEIMG"
                                            onclick="WdatePicker({el: 'IANNOUNCE_OP_KP_TIME', dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({el: 'IANNOUNCE_OP_KP_TIME', dateFmt: 'HH:mm'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                            style="cursor: pointer" />
                                </td>
                                 <td class="labelField" width="10%">備註：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="100" maxlength="100" id="IANNOUNCE_OP_KP_REMARK2"/>
                                </td>
                        </tr>
                        <tr>						
                                <td class="labelField" width="10%">公告日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_AN_DATE_BEG" value=""  disabled="disabled"
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_AN_DATE_BEG',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_AN_DATE_BEG',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />發文日期：</td>
                                <td class="dataField"  width="40%">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_DOC_DT" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_DOC_DT',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_DOC_DT',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />發文文號：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text" class="form-control" size="30" maxlength="20" id="IANNOUNCE_OP_DOC_WD"/>字第
                                    <input type="text" class="form-control" size="11" maxlength="11" id="IANNOUNCE_OP_DOC_NO"/>號
                                </td>
                                
                        </tr>
                        <tr>						
                                <td class="labelField" width="10%">
                                    公告期滿日期：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" id="IANNOUNCE_OP_AN_DATE_END" value="" disabled="disabled"
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_AN_DATE_END',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_AN_DATE_END',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%"><img src="assets/img/required.png" />公告內容：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IANNOUNCE_OP_AN_CONTENT" name="IANNOUNCE.OP_AN_CONTENT" size="500"
                                            rows="5" maxlength="600" style="width: 100%;"></textarea>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">備註：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IANNOUNCE_OP_AN_REMARK" name="IANNOUNCE.OP_AN_REMARK" size="500"
                                            rows="5" maxlength="300" style="width: 100%;"></textarea>
                                </td>
                        </tr>
                </table>
        </div>
        
<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP03B01M_01.js"></script>