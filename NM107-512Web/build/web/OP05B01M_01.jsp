<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="saveFinishBtn">
            <i class="fa fa-save"></i>&nbsp;儲存並結案
        </button>
        <br><br>
        <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">拾得物結案資料維護作業</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">結案單位：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="OP_FS_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_FS_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            onchange="bindUNITDDL('4', 'OP_FS_unitLevel2', 'OP_FS_unitLevel3', 'OP_FS_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_FS_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">結案人員：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IFNSH_OP_FS_STAFF_NM" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />結案日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IFNSH_OP_FS_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IFNSH_OP_FS_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IFNSH_OP_FS_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">發還單位：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="OP_RT_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_RT_unitLevel3"
                                            name="unitLevel3"
                                            onchange="bindUNITDDL('4', 'OP_RT_unitLevel2', 'OP_RT_unitLevel3', 'OP_RT_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_RT_unitLevel4"
                                            name="unitLevel4">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">發還人員：</td>
                                <td class="dataField" width="40%">
                                    <!--<input type="text" class="form-control" size="30" maxlength="30" id="IFNSH_OP_RT_STAFF_NM"/>-->
                                    <select class="form-control" id="IFNSH_OP_RT_STAFF_ID"
                                            name="IFNSH.OP_RT_STAFF_ID" data-width="180px" data-size="8">
                                    </select>
                                </td>
                                <td class="labelField" width="10%">發還日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IFNSH_OP_RT_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IFNSH_OP_RT_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IFNSH_OP_RT_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">結案紀錄：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="IFNSH_OP_FS_REC_CD"
                                        name="IFNSH.OP_FS_REC_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">結案情形描述：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IFNSH_OP_FS_STAT_DESC" name="IFNSH.OP_FS_STAT_DESC" size="500"
                                            rows="5" maxlength="1000" style="width: 100%;"></textarea>
                                </td>
                        </tr>
                </table>
        </div>

<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP05B01M_01.js"></script>