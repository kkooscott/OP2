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
                                <select class="form-control" id="OP_FS2_unitLevel2"
                                        name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                        onchange="bindUNITDDL('3', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');">
                                </select> 
                                <select class="form-control" id="OP_FS2_unitLevel3"
                                        name="unitLevel3" disabled="disabled"
                                        onchange="bindUNITDDL('4', 'OP_FS2_unitLevel2', 'OP_FS2_unitLevel3', 'OP_FS2_unitLevel4', '');">
                                </select> 
                                <select class="form-control" id="OP_FS2_unitLevel4"
                                        name="unitLevel4" disabled="disabled">
                                </select>
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">結案人員：</td>
                            <td class="dataField" width="40%">
                                <input type="text" class="form-control" size="30" maxlength="30" id="IFNSH2_OP_FS_STAFF_NM" disabled="disabled"/>
                            </td>
                            <td class="labelField" width="10%">
                                <img src="assets/img/required.png" />結案日期：</td>
                            <td class="dataField" width="40%">
                                <input type="text"
                                        class="form-control" id="IFNSH2_OP_FS_DATE" value=""
                                        onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                        onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                <img    onclick="WdatePicker({el:'IFNSH2_OP_FS_DATE',dateFmt:'yyy/MM/dd'})"
                                        onfocus="WdatePicker({el:'IFNSH2_OP_FS_DATE',dateFmt:'yyy/MM/dd'})"
                                        src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                        style="cursor: pointer" />
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">結案紀錄：</td>
                            <td class="dataField" colspan="3">
                                <select class="form-control" id="IFNSH2_OP_FS_REC_CD"
                                    name="IFNSH2.OP_FS_REC_CD" data-width="180px" data-size="8">
                                </select>
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">結案情形描述：</td>
                            <td class="dataField" colspan="3">
                                <textarea class="form-control" id="IFNSH2_OP_FS_STAT_DESC" name="IFNSH2.OP_FS_STAT_DESC" size="500"
                                        rows="5" maxlength="1000" style="width: 100%;"></textarea>
                            </td>
                    </tr>
                    <tr>                                                                                                                           
                            <td class="labelField">通知方式：</td>
                            <td class="dataField">
                                <input type="radio" name="IPUANDL2.OP_NTC_MODE" title="Email通知" id="IPUANDL2_OP_NTC_MODE_1" value="1" checked="checked">Email通知
                                <input type="radio" name="IPUANDL2.OP_NTC_MODE" title="電話通知" id="IPUANDL2_OP_NTC_MODE_2" value="2">電話通知
                                <input type="radio" name="IPUANDL2.OP_NTC_MODE" title="書面通知" id="IPUANDL2_OP_NTC_MODE_3" value="3">書面通知
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">送交單位：</td>
                            <td class="dataField" colspan="3">
                                <select class="form-control" id="OP_SD2_unitLevel2"
                                        name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                        onchange="bindUNITDDL('3', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');">
                                </select> 
                                <select class="form-control" id="OP_SD2_unitLevel3"
                                        name="unitLevel3" disabled="disabled"
                                        onchange="bindUNITDDL('4', 'OP_SD2_unitLevel2', 'OP_SD2_unitLevel3', 'OP_SD2_unitLevel4', '');">
                                </select> 
                                <select class="form-control" id="OP_SD2_unitLevel4"
                                        name="unitLevel4" disabled="disabled">
                                </select>
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">送交人姓名：</td>
                            <td class="dataField" width="40%">
                                <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL2_OP_SD_NAME" disabled="disabled"/>
                            </td>
                            <td class="labelField" width="10%">送交人職稱：</td>
                            <td class="dataField" width="40%">
                                <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL2_OP_SD_TITLE" disabled="disabled"/>
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">送交日期：</td>
                            <td class="dataField" colspan="3">
                                <input type="text"
                                        class="form-control" id="IPUANDL2_OP_SD_DATE" value=""
                                        onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                        onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                <img    onclick="WdatePicker({el:'IPUANDL2_OP_SD_DATE',dateFmt:'yyy/MM/dd'})"
                                        onfocus="WdatePicker({el:'IPUANDL2_OP_SD_DATE',dateFmt:'yyy/MM/dd'})"
                                        src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                        style="cursor: pointer" />
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">接受之地方自治團體：</td>
                            <td class="dataField" colspan="3">
                                <input type="text" class="form-control" size="80" maxlength="80" id="IPUANDL2_OP_AC_REG_ATNO"/>
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">接受人姓名：</td>
                            <td class="dataField" colspan="3">
                                <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL2_OP_AC_NAME"/>
                            </td>
                    </tr>
                    <tr>
                            <td class="labelField" width="10%">接受人職稱：</td>
                            <td class="dataField" colspan="3">
                                <input type="text" class="form-control" size="30" maxlength="30" id="IPUANDL2_OP_AC_TITLE"/>
                            </td>
                    </tr>
            </table>
        </div>
        
<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP08B01M_01.js"></script>