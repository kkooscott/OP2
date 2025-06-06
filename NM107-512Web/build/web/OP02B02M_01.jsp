<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<button type="button" class="btn green-seagreen btn-sm" style="display:none"
    id="addDetailBtn">
    <i class="fa fa-plus"></i>&nbsp;新增
</button>
<button type="button" class="btn green btn-sm" style="display:none"
    id="editDetailBtn">
    <i class="fa fa-pencil"></i>&nbsp;修改
</button>
<button type="button" class="btn red-intense btn-sm" style="display:none"
    id="deleteDetailBtn">
    <i class="fa fa-times"></i>&nbsp;刪除
</button>
<button type="button" class="btn yellow btn-sm" style="display:none"
    id="clearDetailBtn">
    <i class="fa fa-eraser"></i>&nbsp;清除
</button>
<br><br>
<div id = "tab_4_2_alert">
    <h4>請先輸入受理基本資料並按儲存按鈕，以產生拾得遺失物收據號碼。</h4>
</div>
<div id="tab_4_2_main" style="display:none">
    <div>
        <div class="table-header clearfix">
                <div class="modal-header">
                        <h4 class="modal-title">拾得物品明細資料</h4>
                </div>
        </div>
        <table name="mainTB" border="0" width="100%" cellpadding="0"
                cellspacing="0" class="keyinTableGray">
            <!--202404 哪天想秀在畫面上就用AppUse.jsp-->
            <jsp:include page="AppUseHide.jsp" /> 
            <tr>
                <td width="190">
                    <table name="mainTB" border="0" width="100%" cellpadding="0"
                            cellspacing="0" class="keyinTableGray">
                            <tr>
                                <img id="previewPhoto1" src="assets/img/file-upload-with-preview.png" width="225" height="225"/>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div id="tabs">
                                        <input type="file" id="file1" accept="image/*" onchange="preview(this,'1')" style="display:none"/>
                                    &nbsp;&nbsp;&nbsp;
                                        <button class="btn green btn-sm" type="button" id="photo1SubmitBtn">
                                                <i class="fa fa-upload"></i>&nbsp;照片上傳
                                        </button>
                                    &nbsp;
                                        <button type="button" class="btn red-intense btn-sm"
                                                id="photo1DeleteBtn">
                                                <i class="fa fa-times"></i>&nbsp;照片刪除
                                        </button>
                                    </div>
                                </td>
                            </tr>    
                    </table>
                </td> 
                <td width="190">
                    <table name="mainTB" border="0" width="100%" cellpadding="0"
                            cellspacing="0" class="keyinTableGray">
                        <tr>
                            <img id="previewPhoto2" src="assets/img/file-upload-with-preview.png" width="225" height="225"/>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div id="tabs">
                                    <input type="file" id="file2" accept="image/*" onchange="preview(this,'2')" style="display:none"/>
                                &nbsp;&nbsp;&nbsp;
                                    <button class="btn green btn-sm" type="button" id="photo2SubmitBtn">
                                            <i class="fa fa-upload"></i>&nbsp;照片上傳
                                    </button>
                                &nbsp;
                                    <button type="button" class="btn red-intense btn-sm"
                                            id="photo2DeleteBtn">
                                            <i class="fa fa-times"></i>&nbsp;照片刪除
                                    </button>
                                </div>
                            </td>
                        </tr>    
                    </table>
                </td>
                <td>
                    <table name="mainTB" border="0" width="100%" cellpadding="0"
                            cellspacing="0" class="keyinTableGray">                                                                                                        
                        <tr>
                                <td class="labelField" width="10%">收據號碼：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="20" maxlength="17" id="IPUDETAIL_OP_AC_RCNO" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />物品名稱：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="16" maxlength="16" id="IPUDETAIL_OP_PUOJ_NM"/>
                                    <button type="button" class="btn green btn-sm" id="sameasIPUDETAIL_OP_TYPE_CD" onClick="DoSameAsIPUDETAIL_OP_TYPE_CD()">
                                        <i class="fa fa-save"></i>&nbsp;同物品種類
                                    </button>
                                </td>
                        </tr>
                        <tr>
                            <td class="labelField" width="10%">
                                <img src="assets/img/required.png" />物品種類：</td>
                            <td class="dataField" width="40%">
                                <select class="form-control" id="IPUDETAIL_OP_TYPE_CD" multiple="multiple"
                                        name="IPUDETAIL.OP_TYPE_CD" data-width="180px" data-size="8">
                                </select>
                                <br>
                                <label id="MACLabel" style="display:none">MAC：</label>
                                <input type="text" class="form-control" size="20" maxlength="30" id="IPUDETAIL_OP_MAC_ADDR" style="display:none"/>
                                <button class="btn green-seagreen btn-sm" type="button" style="display:none"
                                        id="GET_E_MAC_DATA">
                                        <i class="fa fa-search"></i>&nbsp;查詢e化報案
                                </button>
                                <br>
                                <label id="IMEILabel" style="display:none">IMEI1：</label>
                                <input type="text" class="form-control" size="20" maxlength="15" id="IPUDETAIL_OP_IMEI_CODE" style="display:none"/>
                                <br>
                                <label id="IMEILabel2" style="display:none">IMEI2：</label>
                                <input type="text" class="form-control" size="20" maxlength="15" id="IPUDETAIL_OP_IMEI_CODE_2" style="display:none"/>
                                <button class="btn green-seagreen btn-sm" type="button" style="display:none"
                                        id="GET_E_IMEI_DATA">
                                        <i class="fa fa-search"></i>&nbsp;查詢e化報案
                                </button>
                                <br>
                                <label id="PhoneLabel" style="display:none">電信門號1：</label>
                                <input type="text" class="form-control" size="18" maxlength="30" id="IPUDETAIL_OP_PHONE_NUMBER" style="display:none"/>
                                <br>
                                <label id="PhoneLabel2" style="display:none">電信門號2：</label>
                                <input type="text" class="form-control" size="18" maxlength="30" id="IPUDETAIL_OP_PHONE_NUMBER_2" style="display:none"/>
                                <button class="btn green-seagreen btn-sm" type="button" style="display:none"
                                        id="GET_E_PHONE_DATA">
                                        <i class="fa fa-search"></i>&nbsp;查詢e化報案
                                </button>
                            </td>
                            <td class="labelField" width="10%">
                                廠牌：</td>
                            <td class="dataField" width="40%">
                                <select class="form-control" id="IPUDETAIL_OP_BRAND_CD"
                                        name="IPUDETAIL.OP_BRAND_CD" data-width="180px" data-size="8">
                                </select>
                            </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />數量：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="20" maxlength="20" id="IPUDETAIL_OP_QTY"/> 
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />單位：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="6" maxlength="30" id="IPUDETAIL_OP_QTY_UNIT"/> 
                                    <select class="form-control" id="IPUDETAIL_OP_QTY_UNIT_CHOOSE"
                                        name="IPUDETAIL.OP_QTY_UNIT_CHOOSE" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">顏色：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="IPUDETAIL_OP_COLOR_CD"
                                        name="IPUDETAIL.OP_COLOR_CD" data-width="180px" data-size="8">
                                    </select>                                                                                   
                                </td>
                        </tr>
                        <tr>
                            <td class="labelField" width="10%">特徵：</td>
                            <td class="dataField" colspan="3">
                                <input type="text" class="form-control" size="66" maxlength="100" id="IPUDETAIL_OP_FEATURE"/>                                                                                    
                            </td>
                        </tr>    
                        <tr>
                             <td class="labelField" width="10%">備考：</td>
                            <td class="dataField" colspan="3">
                                <input type="text" class="form-control" size="66" maxlength="100" id="IPUDETAIL_OP_REMARK"/> 
                            </td>     
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <br><br>
    <div class="row">
            <div class="col-md-12">
                    <div class="table-header clearfix">
                        <div class="modal-header">
                            <h4 class="modal-title">其他物品明細</h4>
                        </div>										
                    </div>
                    <!-- grid view -->
                    <div>
                            <table id="gridClueList">
                                    <tr>
                                            <td />
                                    </tr>
                            </table>
                            <div id="pagerClueList"></div>
                    </div>
            </div>
    </div>
</div>


<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP02B02M_01.js"></script>
