<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="getAnnounceBtn2">
            <i class="fa fa-paper-plane"></i>&nbsp;產生網路公告
        </button>
        <button type="button" class="btn green btn-sm" style="display:none"
            id="saveAnnounceBtn2">
            <i class="fa fa-paper-plane"></i>&nbsp;發佈網路公告
        </button>
        <br><br>
        <div id = "tab_4_6_alert">
            <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
        </div>
        <div id="tab_4_6_main" style="display:none">
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
                                        <select class="form-control" id="OP_AC2_unitLevel2"
                                                name="unitLevel2" data-width="180px" data-size="8" disabled="disabled"
                                                onchange="bindUNITDDL('3', 'OP_AC2_unitLevel2', 'OP_AC2_unitLevel3', 'OP_AC2_unitLevel4', '');">
                                        </select> 
                                        <select class="form-control" id="OP_AC2_unitLevel3"
                                                name="unitLevel3" disabled="disabled"
                                                onchange="bindUNITDDL('4', 'OP_AC2_unitLevel2', 'OP_AC2_unitLevel3', 'OP_AC2_unitLevel4', '');">
                                        </select> 
                                        <select class="form-control" id="OP_AC2_unitLevel4"
                                                name="unitLevel4" disabled="disabled">
                                        </select>
                                    </td>
                                    <td class="labelField" width="10%">受理日期：</td>
                                    <td class="dataField" width="40%">
                                        <input type="text"
                                                class="form-control" id="IANNOUNCE_OP_AC_DATE2" value=""  disabled="disabled"
                                                onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                        <img    onclick="WdatePicker({el:'IANNOUNCE_OP_AC_DATE2',dateFmt:'yyy/MM/dd'})"
                                                onfocus="WdatePicker({el:'IANNOUNCE_OP_AC_DATE2',dateFmt:'yyy/MM/dd'})"
                                                src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                                style="cursor: pointer" />
                                    </td>
                            </tr>
                            <tr>
                                    <td class="labelField" width="10%">電話：</td>
                                    <td class="dataField" colspan="3">
                                        <input type="text" class="form-control" size="30" maxlength="20" id="IPUBASIC_OP_AC_UNIT_TEL2" disabled="disabled"/>
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
                    <table name="mainTB" border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                        <tr>

                            <td>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                                        <tr>
                                                <td class="labelField" width="10%">發文文號：</td>
                                                <td class="dataField"  colspan="3">
                                                    <input type="text" class="form-control" size="30" maxlength="20" id="IANNOUNCE_OP_DOC_WD2" disabled="disabled"/>字第
                                                    <input type="text" class="form-control" size="11" maxlength="11" id="IANNOUNCE_OP_DOC_NO2" disabled="disabled"/>號
                                                </td>

                                        </tr>
                                        <tr>	
                                                <td class="labelField" width="10%">發文單位：</td>
                                                <td class="dataField" width="40%">
                                                    <input type="text" class="form-control" size="50" maxlength="67" id="IANNOUNCE_OP_AC_UNIT_ALL" disabled="disabled"/>
                                                </td>
                                                <td class="labelField" width="10%">公告日期：</td>
                                                <td class="dataField" width="40%">
                                                    <input type="text"
                                                            class="form-control" id="IANNOUNCE_OP_AN_DATE_BEG2" value=""  disabled="disabled"
                                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                                    <img    onclick="WdatePicker({el:'IANNOUNCE_OP_AN_DATE_BEG2',dateFmt:'yyy/MM/dd'})"
                                                            onfocus="WdatePicker({el:'IANNOUNCE_OP_AN_DATE_BEG2',dateFmt:'yyy/MM/dd'})"
                                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                                            style="cursor: pointer" />
                                                </td>
                                        </tr>
                                        <tr>
                                                <td class="labelField" width="10%"><img src="assets/img/required.png" />公告內容：</td>
                                                <td class="dataField" colspan="3">
                                                    <textarea class="form-control" id="IANNOUNCE_OP_AN_CONTENT2" name="IANNOUNCE.OP_AN_CONTENT2" size="500"
                                                            rows="5" maxlength="600" style="width: 100%;" disabled="disabled"></textarea>
                                                </td>
                                        </tr>
                                       <tr>
                                                <td class="labelField" width="10%">拾得日期時間：</td>
                                                <td class="dataField" colspan="3">
                                                    <input type="text"
                                                            class="form-control" id="IPUBASIC_OP_PU_DATE2" value="" disabled="disabled"
                                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                                    <img    onclick="WdatePicker({el:'IPUBASIC_OP_PU_DATE2',dateFmt:'yyy/MM/dd'})"
                                                            onfocus="WdatePicker({el:'IPUBASIC_OP_PU_DATE2',dateFmt:'yyy/MM/dd'})"
                                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                                            style="cursor: pointer" />
                                                    <input  type="text" class="form-control" id="IPUBASIC_OP_PU_TIME2" disabled="disabled"
                                                            name="IPUBASIC.OP_PU_TIME" value="" size="10"
                                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                                    <img    id="OC_TIMEIMG"
                                                            onclick="WdatePicker({el: 'IPUBASIC_OP_PU_TIME2', dateFmt: 'HH:mm'})"
                                                            onfocus="WdatePicker({el: 'IPUBASIC_OP_PU_TIME2', dateFmt: 'HH:mm'})"
                                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                                            style="cursor: pointer" />
                                                </td>
                                        </tr>
                                        <tr>
                                                <td class="labelField" width="10%"> 拾得地點：</td>
                                                <td class="dataField" colspan="3">
                                                    <select class="form-control" id="IPUBASIC_OP_PU_CITY_CD2"
                                                            name="IPUBASIC.OP_PU_CITY_CD2" data-width="180px" data-size="8" disabled="disabled"
                                                            onchange="bindCOUNTRYDDL('3', 'IPUBASIC_OP_PU_CITY_CD2', 'IPUBASIC_OP_PU_TOWN_CD2', '', '', '');">
                                                    </select>
                                                    <select class="form-control" id="IPUBASIC_OP_PU_TOWN_CD2" disabled="disabled"
                                                            name="IPUBASIC.OP_PU_TOWN_CD2" data-width="180px" data-size="8">
                                                    </select>
                                                    <input type="text"
                                                            class="form-control" size="100" maxlength="100" disabled="disabled"
                                                            id="IPUBASIC_OP_PU_PLACE2" />
                                                </td>							
                                        </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td width="190">
                                <table name="mainTB" border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                                        <tr>
                                            <img id="previewPhoto3" src="assets/img/file-upload-with-preview.png" width="225" height="225"/>
                                        </tr>
                                        <tr>
                                            <input type="file" id="file3" accept="image/*" onchange="previewForAnn(this,'3')" style="display:none"/>
                                        &nbsp;&nbsp;&nbsp;
                                            <button class="btn green btn-sm" type="button" id="photo3SubmitBtn">
                                                    <i class="fa fa-upload"></i>&nbsp;照片上傳
                                            </button>
                                        &nbsp;
                                            <button type="button" class="btn red-intense btn-sm"
                                                    id="photo3DeleteBtn">
                                                    <i class="fa fa-times"></i>&nbsp;照片刪除
                                            </button>
                                        </tr>
                                        <tr>
                                            <img id="previewPhoto4" src="assets/img/file-upload-with-preview.png" width="225" height="225"/>
                                        </tr>
                                        <tr>
                                            <input type="file" id="file4" accept="image/*" onchange="previewForAnn(this,'4')" style="display:none"/>
                                        &nbsp;&nbsp;&nbsp;
                                            <button class="btn green btn-sm" type="button" id="photo4SubmitBtn">
                                                    <i class="fa fa-upload"></i>&nbsp;照片上傳
                                            </button>
                                        &nbsp;
                                            <button type="button" class="btn red-intense btn-sm"
                                                    id="photo4DeleteBtn">
                                                    <i class="fa fa-times"></i>&nbsp;照片刪除
                                            </button>
                                        </tr>  
                                </table>
                            </td>
                        </tr>
                    </table>    
            </div>
            <div>
                    <div class="table-header clearfix">
                            <div class="modal-header">
                                    <h4 class="modal-title">公告事項</h4>
                            </div>
                    </div>
                    <table border="0" width="100%" cellpadding="0" cellspacing="0" class="keyinTableGray">
                            <tr>
                                    <td class="labelField" width="10%">注意事項1：</td>
                                    <td class="dataField" colspan="3">
                                        <label class="normal-label">請失主於<label class="normal-label" id="IANNOUNCE_OP_AN_DATE_END2"></label>公告招領期滿前，上網認領或攜帶國民身分證、本人印章至發文單位認領，逾期即依法處理，倘有冒領情事，自負法律責任。</label>
                                    </td>
                            </tr>
                            <tr>
                                    <td class="labelField" width="10%">
                                        注意事項2：</td>
                                    <td class="dataField" colspan="3">
                                        <label class="normal-label">受理單位如為台北市政府警察局，請至遺失物處理中心(刑事警察大隊)認領。</label>
                                    </td>
                            </tr>
                    </table>
            </div>
        </div>
        
<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP03B02M_01.js"></script>