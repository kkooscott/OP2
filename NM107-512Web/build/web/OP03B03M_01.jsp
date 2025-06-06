<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="saveIAnDlBtn">
            <i class="fa fa-save"></i>&nbsp;儲存並發佈拾得人領回公告
        </button>
        <br><br>
        <div id = "tab_4_7_alert">
            <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
        </div>
        <div id="tab_4_7_main" style="display:none">
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">拾得物招領期滿處理資料</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">處理單位：</td>
                                <td class="dataField"  width="40%">
                                    <select class="form-control" id="OP_PR_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_PR_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            onchange="bindUNITDDL('4', 'OP_PR_unitLevel2', 'OP_PR_unitLevel3', 'OP_PR_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_PR_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">處理人員：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IANDL_OP_PR_STAFF_NM" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />處理日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANDL_OP_PR_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANDL_OP_PR_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANDL_OP_PR_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">是否公告中拍賣：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IANDL_OP_YN_AUCTION">
                                        <option value="N">否</option>
                                        <option value="Y">是</option>                                    
                                    </select>
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />通知拾得人領回日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IANDL_OP_NTC_PUPO_DT" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IANDL_OP_NTC_PUPO_DT',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANDL_OP_NTC_PUPO_DT',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />拾得人領回公告發文單位：
                                </td>
                                <td class="dataField"  width="40%">
                                    <select class="form-control" id="OP_PUPOANUNIT_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_PUPOANUNIT_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            <!--onchange="bindUNITDDL('4', 'OP_PUPOANUNIT_unitLevel2', 'OP_PUPOANUNIT_unitLevel3', 'OP_PUPOANUNIT_unitLevel4', '');">-->
                                    </select> 
                                    <!--
                                    <select class="form-control" id="OP_PUPOANUNIT_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                    -->
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />發文文號：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text" class="form-control" size="30" maxlength="20" id="IANDL_OP_PUPO_DOC_WD"/>字第
                                    <input type="text" class="form-control" size="11" maxlength="11" id="IANDL_OP_PUPO_DOC_NO"/>號
                                </td>
                                
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">公告期滿日期：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" id="IANDL_OP_PUPO_ANDTEND" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            disabled="disabled"> 
                                    <img    onclick="WdatePicker({el:'IANDL_OP_PUPO_ANDTEND',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IANDL_OP_PUPO_ANDTEND',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%"><img src="assets/img/required.png" />公告內容：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IANDL_OP_PUPO_AN_CONT" name="IANDL.OP_PUPO_AN_CONT" size="500"
                                            rows="5" maxlength="600" style="width: 100%;"></textarea>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">處理情形描述：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IANDL_OP_PR_STAT_DESC" name="IANDL.OP_PR_STAT_DESC" size="500"
                                            rows="5" maxlength="1000" style="width: 100%;"></textarea>
                                </td>
                        </tr>
                </table>
        </div>
        
<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP03B03M_01.js"></script>