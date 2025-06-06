<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	import="npa.op.util.StringUtil, npa.op.base.BaseActionSupport"%>

<script>try {console.log('p3 start footer.jsp loadtime=' + (new Date() - startTime));}catch(e){ }</script>
<script src="assets/plugins/jquery-1.11.3.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="assets/plugins/bootstrap/js/bootstrap.min.js"
	type="text/javascript"></script>
<script src="assets/plugins/bootstrap/js/bootstrap-lite.js"
	type="text/javascript"></script>
<script src="assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.js"
	type="text/javascript"></script>
<!--  mega menu -->
<script type='text/javascript'
	src='assets/plugins/jquery-mega-drop-down-menu/js/jquery.hoverIntent.minified.js'></script>
<script type='text/javascript'
	src='assets/plugins/jquery-mega-drop-down-menu/js/jquery.dcmegamenu.1.3.3.js'></script>
<!-- validation -->
<script
	src="assets/plugins/jquery-validation/dist/jquery.validate.min.js"></script>
<!--	Noty	-->
<script src="assets/plugins/noty/jquery.noty.js"></script>
<script src="assets/plugins/noty/layouts/bottom.js"
	type="text/javascript"></script>
<script src="assets/plugins/noty/layouts/bottomCenter.js"
	type="text/javascript"></script>
<script src="assets/plugins/noty/themes/default.js"
	type="text/javascript"></script>
<!-- smartalert -->
<script src="assets/plugins/smartalert/js/alert.js"></script>
<!-- jqGrid -->
<script src="assets/plugins/jquery-jqgrid/i18n/grid.locale-tw.js"
	type="text/javascript"></script>
<script src="assets/plugins/jquery-jqgrid/jquery.jqGrid.min.js"></script>
<!-- datePicker -->
<script src="assets/plugins/WdatePicker/WdatePicker.js"
	type="text/javascript"></script>
<!-- Ajax Form -->
<script src="assets/plugins/jquery.form.js"></script>
<!-- Upload -->
<script src="assets/plugins/jquery.ajaxfileupload.js"></script>
<script src="assets/js/ajaxfileupload.js"></script>
<!-- fileDownload -->
<script src="assets/plugins/jquery.fileDownload.js"></script>
<script src="assets/js/Footer.js"></script>
<script src="assets/plugins/jquery.tooltipFix.js"></script>
<script src="assets/plugins/jquery-multiple-select/jquery.multiple.select.js" type="text/javascript"></script>
<script src="assets/js/Validator/Validator.js" type="text/javascript"></script>
<script src="assets/js/loading.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function() {
		// Here we disable the hover rows option globally for IE8
		var isIE8 = !! navigator.userAgent.match(/MSIE 8.0/) || navigator.userAgent.match(/MSIE 7.0/);
		if (isIE8) {
			jQuery.extend(jQuery.jgrid.defaults, { hoverrows:false });	
		}		
		// initialize mega menu
		$('#mega-menu-user').dcMegaMenu({
			rowItems: '1',
			speed: 'fast',
			effect: 'fade'
		});		
		$('#mega-menu-4').dcMegaMenu({
			rowItems: '5',
			speed: 'fast',
			effect: 'fade'
		});
		<%
		String errorMsg = StringUtil.nvl(request
					.getAttribute(BaseActionSupport.ERROR_MESSAGE));
			String warnMsg = StringUtil.nvl(request
					.getAttribute(BaseActionSupport.WARN_MESSAGE));
			String infoMsg = StringUtil.nvl(request
					.getAttribute(BaseActionSupport.INFO_MESSAGE));
			if (!errorMsg.equals("")) {%>
		$.alert.open({
			type: 'error',
			content: '<%=errorMsg%>'
		});
		hasError=true;
		<%}%>
		<%if (!warnMsg.equals("")) {%>
		$.alert.open({
			type: 'warning',
			content: '<%=warnMsg%>'
		});
		<%}%>
		<%if (!infoMsg.equals("")) {%>
		$.alert.open({
			type: 'info',
			content: '<%=infoMsg%>'
		});
		<%}%>
		//override jquery ui dialog widget _focusTabable method for IE8 using dialog with WdatePicker
		$.widget("ui.dialog", $.ui.dialog, {
			_focusTabbable : function(ul, item) {
				return;
			}
		});

    });
    </script>

<div id="alertDiv"></div>
<script>try {console.log('p4 end of footer.jsp loadtime=' + (new Date() - startTime));}catch(e){ }</script>

