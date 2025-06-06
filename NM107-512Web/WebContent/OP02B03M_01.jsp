<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<button type="button" class="btn green-seagreen btn-sm" style="display:none"
    id="addClaimBtn">
    <i class="fa fa-plus"></i>&nbsp;新增
</button>
<button type="button" class="btn green btn-sm" style="display:none"
    id="editClaimBtn">
    <i class="fa fa-pencil"></i>&nbsp;修改
</button>
<button type="button" class="btn red-intense btn-sm" style="display:none"
    id="deleteClaimBtn">
    <i class="fa fa-times"></i>&nbsp;刪除
</button>
<button type="button" class="btn yellow btn-sm" style="display:none"
    id="clearClaimBtn">
    <i class="fa fa-eraser"></i>&nbsp;清除
</button>
<br><br>
<div id = "tab_4_4_alert">
    <h4>無任何拾得物品明細資料，請先至拾得物品明細資料頁籤輸入拾得物品明細資料。</h4>
</div>
<div id="tab_4_4_main" style="display:none">
    <div class="row">
        <div class="col-md-12">                                                                                                    
            <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">文件資料</h4>
                        </div>
                </div>
                <table border="0" width="100%" cellpadding="0" cellspacing="0"
                        class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">收據號碼：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="30" maxlength="17"  id="IPUCLAIM_OP_AC_RCNO" disabled="disabled"/>
                                </td>
                                <td class="labelField" width="10%">填表日期：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" id="IPUCLAIM_OP_FM_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUCLAIM_OP_FM_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUCLAIM_OP_FM_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                </table>
            </div>
            <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">認領人基本資料</h4>
                        </div>
                </div>
                <table name="addTB" border="0" width="100%" cellpadding="0"
                        cellspacing="0" class="keyinTableGray">                                                                                                          
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />姓名：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="50" maxlength="50" id="IPUCLAIM_OP_PUCP_NAME" />
                                </td>
                                <td class="labelField" width="10%">
                                    羅馬拼音：</td>
                                <td class="dataField" width="40%">
                                    <input type="text" class="form-control" size="50" maxlength="50" id="IPUCLAIM_OP_PUCP_RNAME" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />證號：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUCLAIM_OP_PUCP_IDN_TP">
                                        <option value="1">身分證號</option>
                                        <option value="2">其他證號</option>
                                        <option value="3">無證號</option>
                                    </select>
                                    <input type="text"
                                        class="form-control" size="30" maxlength="20"
                                        id="IPUCLAIM_OP_PUCP_IDN" />
                                </td>
                                <td class="labelField" width="10%">性別：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUCLAIM_OP_PUCP_GENDER">
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
                                    <select class="form-control" id="IPUCLAIM_OP_PUCP_BEFROC">
                                        <option value="1">民國</option>
                                        <option value="0">民前</option>
                                        <option value="2">西元</option>
                                    </select>
                                    <input type="text"
                                        class="form-control" size="30" maxlength="8"
                                        id="IPUCLAIM_OP_PUCP_BIRTHDT" />
                                    <br>
                                    <label class="normal-label">民前：001/01/01 -> 0010101</label>
                                    <br>
                                    <label class="normal-label">民國：099/01/01 -> 0990101</label>
                                    <br>
                                    <label class="normal-label">西元：2000/01/01 -> 20000101</label>
                                </td>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />電話：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" size="30" maxlength="20"
                                            id="IPUCLAIM_OP_PUCP_PHONENO" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">國籍：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUCLAIM_OP_PUCP_NAT_CD"
                                            name="IPUCLAIM.OP_PUCP_NAT_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">郵遞區號：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" size="30" maxlength="5"
                                            id="IPUCLAIM_OP_PUCP_ZIPCODE" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">
                                    <img src="assets/img/required.png" />地址：</td>
                                <td class="dataField" colspan="3">
                                    <input type="radio" name="IPUCLAIM.OP_PUCP_ADDR_TYPE_CD" title="一般地址" id="IPUCLAIM_OP_PUCP_ADDR_TYPE_CD_1" value="1" checked="checked">一般地址
                                    <input type="radio" name="IPUCLAIM.OP_PUCP_ADDR_TYPE_CD" title="自由輸入" id="IPUCLAIM_OP_PUCP_ADDR_TYPE_CD_2" value="9">自由輸入
                                    <button class="btn green-seagreen btn-sm" type="button"
                                            id="IPUCLAIM_GET_HH_DATA">
                                            <i class="fa fa-search"></i>&nbsp;取得戶籍資料
                                    </button>
                                    <div id="IPUCLAIM_normalPattern">
                                        <select class="form-control" id="IPUCLAIM_OP_PUCP_CITY_CD"
                                                name="IPUCLAIM.OP_PUCP_CITY_CD" data-width="180px" data-size="8"
                                                onchange="bindCOUNTRYDDL('3', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');">
                                        </select>
                                        <select class="form-control" id="IPUCLAIM_OP_PUCP_TOWN_CD"
                                                name="IPUCLAIM.OP_PUCP_TOWN_CD" data-width="180px" data-size="8"
                                                onchange="bindCOUNTRYDDL('4', 'IPUCLAIM_OP_PUCP_CITY_CD', 'IPUCLAIM_OP_PUCP_TOWN_CD', 'IPUCLAIM_OP_PUCP_VILLAGE_CD', '', '');">
                                        </select> 
                                        <select class="form-control" id="IPUCLAIM_OP_PUCP_VILLAGE_CD"
                                                name="IPUCLAIM.OP_PUCP_VILLAGE_CD" data-width="180px" data-size="8">
                                        </select>
                                        &nbsp;
                                        <input type="text"
                                                class="form-control" size="3" maxlength="10"
                                                id="IPUCLAIM_OP_PUCP_LIN" />&nbsp;鄰
                                        <br>
                                        <input type="text"
                                                class="form-control" size="100" maxlength="40"
                                                id="IPUCLAIM_OP_PUCP_ROAD" />
                                    </div>
                                    <div id="IPUCLAIM_freePattern" style="display:none">
                                        <input type="text"
                                                class="form-control" size="100" maxlength="80"
                                                id="IPUCLAIM_OP_PUCP_ADDR_OTH" />
                                    </div>
                                </td>												
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">電子郵件：</td>
                                <td class="dataField" width="40%">
                                    <input type="text"
                                            class="form-control" size="50" maxlength="50"
                                            id="IPUCLAIM_OP_PUCP_EMAIL" />
                                </td>
                                <td class="labelField" width="10%">認領方式：</td>
                                <td class="dataField" width="40%">
                                    <select class="form-control" id="IPUCLAIM_OP_CLAIM_TP_CD" disabled="disabled"
                                            name="IPUCLAIM.OP_CLAIM_TP_CD" data-width="180px" data-size="8">
                                    </select>
                                </td>
                        </tr>
                </table>
            </div>
            <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">遺失物品資料</h4>
                        </div>
                </div>
                <table name="mainTB" border="0" width="100%" cellpadding="0"
                        cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">
                                    <img src="assets/img/required.png" />遺失日期時間：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" id="IPUCLAIM_OP_LOST_DATE" value=""
                                            onclick="WdatePicker({dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({dateFmt:'yyy/MM/dd'})"> 
                                    <img    onclick="WdatePicker({el:'IPUCLAIM_OP_LOST_DATE',dateFmt:'yyy/MM/dd'})"
                                            onfocus="WdatePicker({el:'IPUCLAIM_OP_LOST_DATE',dateFmt:'yyy/MM/dd'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇日期"
                                            style="cursor: pointer" />
                                    <input  type="text" class="form-control" id="IPUCLAIM_OP_LOST_TIME"
                                            name="IPUCLAIM.OP_LOST_TIME" value="" size="10"
                                            onclick="WdatePicker({dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({dateFmt: 'HH:mm'})">
                                    <img    id="OC_TIMEIMG"
                                            onclick="WdatePicker({el: 'IPUCLAIM_OP_LOST_TIME', dateFmt: 'HH:mm'})"
                                            onfocus="WdatePicker({el: 'IPUCLAIM_OP_LOST_TIME', dateFmt: 'HH:mm'})"
                                            src="assets/img/calendar.gif" align="absmiddle" alt="選擇時間"
                                            style="cursor: pointer" />
                                </td>
                        </tr>
                        <tr>
                                <td class="labelField">
                                        <img src="assets/img/required.png" />遺失地點：</td>
                                <td class="dataField">
                                    <select class="form-control" id="IPUCLAIM_OP_LOST_CITY_CD"
                                            name="IPUCLAIM.OP_LOST_CITY_CD" data-width="180px" data-size="8"
                                            onchange="bindCOUNTRYDDL('3', 'IPUCLAIM_OP_LOST_CITY_CD', 'IPUCLAIM_OP_LOST_TOWN_CD', '', '', '');">
                                    </select>
                                    <select class="form-control" id="IPUCLAIM_OP_LOST_TOWN_CD"
                                            name="IPUCLAIM.OP_LOST_TOWN_CD" data-width="180px" data-size="8">
                                    </select>
                                    <input type="text"
                                            class="form-control" size="100" maxlength="100"
                                            id="IPUCLAIM_OP_LOST_PLACE" />
                                </td>							
                        </tr>
                        <tr>
                                <td class="labelField" width="10%">物品描述：</td>
                                <td class="dataField" colspan="3">
                                    <textarea class="form-control" id="IPUCLAIM_OP_LOST_OJ_DESC" name="IPUCLAIM.OP_LOST_OJ_DESC" size="500"
                                            rows="5" maxlength="300" style="width: 100%;"></textarea>
                                </td>
                        </tr>

                </table>
            </div>
            <div>
                <div class="table-header clearfix">
                        <div class="modal-header">
                                <h4 class="modal-title">比對結果</h4>
                        </div>
                </div>
                <table name="mainTB" border="0" width="100%" cellpadding="0"
                        cellspacing="0" class="keyinTableGray">
                        <tr>
                                <td class="labelField" width="10%">是否為有認領權人：</td>
                                <td class="dataField" colspan="3">
                                    <select class="form-control" id="IPUCLAIM_OP_YN_LOSER">
                                        <option value="0"></option>
                                        <option value="1">是</option>
                                        <option value="2">否</option>
                                        <option value="3">待確認</option>
                                    </select>
                                </td>

                        </tr>
                        <tr>
                                <td class="labelField" width="10%">備註：</td>
                                <td class="dataField" colspan="3">
                                    <input type="text"
                                            class="form-control" size="100" maxlength="100"
                                            id="IPUCLAIM_OP_REMARK" />
                                </td>
                        </tr>                                                                                                          

                </table>
            </div>
        </div>
    </div>
    <br><br>
    <div class="row" id="tab_4_4_main2" style="display:none">
            <div class="col-md-12">
                    <div class="table-header clearfix">
                        <div class="modal-header">
                            <h4 class="modal-title">其他民眾認領資料</h4>
                        </div>										
                    </div>
                    <!-- grid view -->
                    <div>
                            <table id="gridList_4">
                                    <tr>
                                            <td />
                                    </tr>
                            </table>
                            <div id="pageList_4"></div>
                    </div>
            </div>
    </div>
</div>


<!-- 每個頁面各自使用的js -->
<script src="assets/js/OP02B03M_01.js"></script>