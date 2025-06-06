<%@page import="npa.op.util.NPAUtil"%>
<%@page import="npa.op.vo.User"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
    User user = NPAUtil.getUser(session);
%>
<tr>
    <td colspan="3">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="labelField">委託查詢者：</td>
                <td class="dataField"><input id="queryByUser"name="queryByUser" type="text" class="form-control" size="10" value="<%=user.getUserName()%>"  readonly /></td>
                <td class="labelField">委託單位：</td>
                <td class="dataField"><input id="queryByUnit" name="queryByUnit" type="text" class="form-control" size="30" value="<%=user.getUnitName()%>" readonly /></td>
                <!--哪天要每個功能都要秀時本jsp可刪，改用AppUse.jsp
                <td class="labelField">用途：</td>
                -->
                <td class="dataField">
                    <select type="hidden" id="appUseType"    name="appUseType"      class="form-control" data-width="auto"     style="display: none;"></select>
                    <select type="hidden" id="appUseSubType" name="appUseSubType"   class="form-control" data-width="auto"     style="display: none;"style="width: 150px;"></select>
                    <input  type="hidden" id="appUseCustom"  name="appUseCustom"    class="form-control" type="text" size="20" style="display: none;"/>
                    <!--202504    
                    <input                id="appUseCustom"  name="appUseCustom"    class="form-control" type="text" size="20" style="display: "/>
                    -->
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr style="height: 2px">
    <td colspan="4" style="padding: 0px"><hr color="#b1bdbd" style="height: 1px" /></td>
</tr>