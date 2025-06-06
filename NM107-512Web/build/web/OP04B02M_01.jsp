<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="saveIPuanDlBtn">
            <i class="fa fa-save"></i>&nbsp;儲存
        </button>
        <br><br>
        <div id = "tab_4_9_alert">
            <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
        </div>
        <div id="tab_4_9_main" style="display:none">
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">拾得人領回公告期滿處理資料維護作業</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">送交單位：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="OP_SD_unitLevel2"
                                            name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                            onchange="bindUNITDDL('3', 'OP_SD_unitLevel2', 'OP_SD_unitLevel3', 'OP_SD_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_SD_unitLevel3"
                                            name="unitLevel3" disabled="disabled"
                                            onchange="bindUNITDDL('4', 'OP_SD_unitLevel2', 'OP_SD_unitLevel3', 'OP_SD_unitLevel4', '');">
                                    </select> 
                                    <select class="form-control" id="OP_SD_unitLevel4"
                                            name="unitLevel4" disabled="disabled">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">送交人姓名：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL_OP_SD_NAME" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">送交人職稱：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL_OP_SD_TITLE" disabled="disabled"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">送交日期：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" id="IPUANDL_OP_SD_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUANDL_OP_SD_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUANDL_OP_SD_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />接受之地方自治團體：
                                </td>
                                <td class="dataField" colspan="3">
                                    <input type="text" class="form-control" size="80" maxlength="80" id="IPUANDL_OP_AC_REG_ATNO"/>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">接受人姓名：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL_OP_AC_NAME"/>
                                </td>
                                <td class="labelField" width="10%">接受人職稱：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL_OP_AC_TITLE"/>
                                </td>
                        </tr>
                </table>
        </div>

<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP04B02M_01.js"></script>