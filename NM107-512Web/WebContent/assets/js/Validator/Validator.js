function trimAll() {
	for ( var j = 0; j < document.forms.length; j++) {
		var vForm = document.forms[j];
		for ( var i = 0; i < vForm.elements.length; i++) {
			if (vForm.elements[i].type == "text" || vForm.elements[i].type == "textarea")
				vForm.elements[i].value = vForm.elements[i].value.trim();
		}
	}
}

// 計算value值實際長度
String.prototype.getValueLength = function() {
	var length = 0;
	for ( var j = 0; j < this.length; j++) {
		length += 1;
		var c = escape(this.charAt(j));
		if (c.indexOf("%u") >= 0) {
			length += 1;
		}
	}
	return length;
}

// 加上去除空白的trim的功能
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

// 加上去除左邊空白的ltrim的功能
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");
}

// 加上去除右邊空白的rtrim的功能
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");
}

// 判斷是否正確的民國曆格式
// 輸入格式應為(Y)YYMMDD
String.prototype.isTWDate = function() {
	var nYear = '', nMonth = '', nDay = '';
	var TWDPatern = /^\d{2,3}((0?[1-9])|(1[0-2]))(([0]?[1-9])|([1-2]?[0-9])|(3[0-1]))$/;

	// 判斷日期格式正確性
	if (this.search(TWDPatern) == -1) {
		return false;
	}
	// 判斷是否是正確(合法)日期
	if (this.length == 6) {
		nYear = parseInt(this.substring(0, 2), 10) + 1911;
		nMonth = this.substring(2, 4);
		nDay = this.substring(4, 6);
	} else {
		nYear = parseInt(this.substring(0, 3), 10) + 1911;
		nMonth = this.substring(3, 5);
		nDay = this.substring(5, 7);
	}
	var tmp = new Date(nYear, nMonth - 1, nDay);

	if ((nYear != tmp.getFullYear() || nMonth != (tmp.getMonth() + 1) || nDay != tmp
			.getDate())) {
		return false;
	}
	return true;
}

// 判斷是否正確的西元格式
// 輸入格式應為YYYYMMDD
String.prototype.isEDate = function() {
	var EngPattern = /^\d{4}((0?[1-9])|(1[0-2]))(([0]?[1-9])|([1-2]?[0-9])|(3[0-1]))$/;

	// 判斷日期格式正確性
	if (this.search(EngPattern) == -1) {
		return false;
	}
	// 判斷是否是正確(合法)日期
	var nYear = this.substring(0, 4);
	var nMonth = this.substring(4, 6);
	var nDay = this.substring(6, 8);

	var tmp = new Date(nYear, nMonth - 1, nDay);

	if ((nYear != tmp.getFullYear() || nMonth != (tmp.getMonth() + 1) || nDay != tmp
			.getDate())) {
		return false;
	}
	return true;
}

// 判斷是否正確的時間格式
String.prototype.isTime = function() {
	var TPatern = /^(([0-1][0-9])|(2[0-3]))(([0-5][0-9]))$/

	// 判斷日期格式正確性
	if (this.search(TPatern) == -1) {
		return false;
	}
	return true;
}

// 判斷是否正確的時間格式(僅Hour)
String.prototype.isHour = function() {
	var TPatern = /^(([0-1][0-9])|(2[0-3]))$/

	// 判斷日期格式正確性
	if (this.search(TPatern) == -1) {
		return false;
	}
	return true;
}

// 判斷是否正確的電話格式
String.prototype.isPhoneNo = function() {
	var PhonePatern = /^\(0[0-9]\)([0-9]{7,8})$/

	// 判斷日期格式正確性
	if (this.search(PhonePatern) == -1) {
		return false;
	}
	return true;
}

function Validator_Method_init(form) {
	vErrorMsg = '';
	vErrorIdList = '';
	vForm = form;
	for ( var i = 0; i < vForm.length; i++) {
		var vItem = vForm.elements[i];
		if (vItem.type == "text" || vItem.type == "textarea"
				|| vItem.type == "radio" || vItem.type == "checkbox") {
			vItem.style.backgroundColor = vOkColor;
		} else if (vItem.type == "select-one"
				|| vItem.type == "select-multiple") {
			vItem.style.backgroundColor = vOkColor;
			for ( var j = 0; j < vItem.length; j++)
				vItem.options[j].style.backgroundColor = vOkColor;
		}
	}
}

function Validator_Method_isValid() {
	if (vErrorMsg == '')
		return true;
	else
		return false;
}

function Validator_Method_resetMessage() {
	vErrorMsg = '';
}

function Validator_Method_setMessage(errorMsg) {
	vErrorMsg = vErrorMsg + errorMsg + "<br/>";
}

function Validator_Method_getMessage() {
	return vErrorMsg;
}

function Validator_Method_showMessage() {
	if (vErrorMsg.length != 0)
		$.alert.open(vErrorMsg);
}

function Validator_Method_required(iNm, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	
	var iValue = item.value.trim();
	if (iValue.length == 0) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：必須輸入！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	} else
		return true;
}

function Validator_Method_checkECaseNo(iNm, isRequired, iTitle, maxLen) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	item.value = item.value.toUpperCase();
	var iValue = item.value.trim();
	if (iValue.getValueLength() > maxLen) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位長度超過 " + maxLen + " ！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	return true;
}

function Validator_Method_checkLength(iNm, isRequired, iTitle, maxLen) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.getValueLength() > maxLen) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位長度超過 " + maxLen + " ！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
        

	//檢查是否包含非署造字
	var _unMapChars = ""; // 存放非署造字(or Unicode)
	for (idxTmp = 0; idxTmp < iValue.length; idxTmp++) {
		var checkChar = iValue.charAt(idxTmp);
		if (checkChar.CheckUnicode() == true) {
			if (_unMapChars != "")
				_unMapChars = _unMapChars.concat("、");
			_unMapChars = _unMapChars.concat(checkChar);
		}
	}
	if (_unMapChars != "") {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位內容包含非署造字【" + _unMapChars
				+ "】！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}

	return true;
}
//20230302 警署反應在單簽帶入的送交人姓名會被擋住「晧」字, 為免影響太大，先開一個NEW是不經過
//「非署造字」的檢查，觀察一陣子後可以將所有的checkLength都移到New
function Validator_Method_checkLength_New(iNm, isRequired, iTitle, maxLen) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.getValueLength() > maxLen) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位長度超過 " + maxLen + " ！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
        
//20230302 警署反應之難字問題
	// 檢查是否包含非署造字 (已是以往之作業方式，其他許多專案已棄用)
//	var _unMapChars = ""; // 存放非署造字(or Unicode)
//	for (idxTmp = 0; idxTmp < iValue.length; idxTmp++) {
//		var checkChar = iValue.charAt(idxTmp);
//		if (checkChar.CheckUnicode() == true) {
//			if (_unMapChars != "")
//				_unMapChars = _unMapChars.concat("、");
//			_unMapChars = _unMapChars.concat(checkChar);
//		}
//	}
//	if (_unMapChars != "") {
//		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位內容包含非署造字【" + _unMapChars
//				+ "】！");
//		Validator.addErrorList(iNm);
//		item.style.backgroundColor = vErrorColor;
//		return false;
//	}

	return true;
}
function Validator_Method_checkRadiohasValue(iNm, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.length != null && item.length > 0) {
		for ( var i = 0; i < item.length; i++) {
			if (item[i].checked == true && item[i].value != '')
				return true;
		}
	} else {
		if (item.checked == true)
			return true;
	}
	Validator.setMessage("欄位 [ " + iTitle + " ]：必須選取！");
	Validator.addErrorList(iNm);
	if (item.length != null && item.length > 0) {
		for ( var i = 0; i < item.length; i++)
			item[i].style.backgroundColor = vErrorColor;
	} else
		item.style.backgroundColor = vErrorColor;
	return false;
}

function Validator_Method_checkRadio(iNm, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.length != null && item.length > 0) {
		for ( var i = 0; i < item.length; i++) {
			if (item[i].checked == true)
				return true;
		}
	} else {
		if (item.checked == true)
			return true;
	}
	Validator.setMessage("欄位 [ " + iTitle + " ]：必須選取！");
	Validator.addErrorList(iNm);
	if (item.length != null && item.length > 0) {
		for ( var i = 0; i < item.length; i++)
			item[i].style.backgroundColor = vErrorColor;
	} else
		item.style.backgroundColor = vErrorColor;
	return false;
}
function Validator_Method_checkPhone(iNm,isRequired, iTitle){
    var item = vForm.elements[iNm];
	if( item == null )
	    return;
	if( isRequired == true ){
		if( !Validator.required(iNm, iTitle) )
			return false;
	} else {
          if (item.value.trim()=="") {
             return;
          }
        }
        var regex = /^(09[0-9]{8})$/;
	var iValue = item.value.trim();
	if( regex.exec(iValue)==null){
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確的手機號！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}

	return true;
}
function Validator_Method_checkCombo(iNm, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	
	if (item.selectedIndex != -1 && item.options.value != "")
		return true;

	Validator.setMessage("欄位 [ " + iTitle + " ]：必須選取！");
	Validator.addErrorList(iNm);
	item.style.background = vErrorColor;
	for ( var i = 0; i < item.length; i++)
		item.options[i].style.backgroundColor = vErrorColor;
	return false;
}

// 20130226 過濾所有下拉選單 若有(停用)字眼則不能選擇
function Validator_Method_filterCombo(iNm, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;

	if (item.disabled == true) {
		return true;
	}
	if (item.type != "select-one") {
		return true;
	}

	if (item.selectedIndex != -1 && item.options[item.selectedIndex].text != "") {
		var itemStr = item.options[item.selectedIndex].text;

		if (itemStr.search(/停用/) > 0) {
			Validator.setMessage("選項 [ " + itemStr + " ]：已經停用，請勿選擇！");
			Validator.addErrorList(iNm);
			item.style.background = vErrorColor;
			for ( var i = 0; i < item.length; i++)
				item.options[i].style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkUnit() {
	if (arguments.length < 2)
		return;
	iTitle = arguments[arguments.length - 1];

	var unit1 = vForm.elements[arguments[0]];
	if (unit1.selectedIndex != -1
			&& unit1.options[unit1.selectedIndex].text != "")
		return true;

	Validator.setMessage("欄位 [ " + iTitle + " ]：必須選取！");
	for ( var i = 0; i < arguments.length - 1; i++) {
		var unit = vForm.elements[arguments[i]];
		if (unit != null) {
			Validator.addErrorList(arguments[i]);
			for ( var j = 0; j < unit.length; j++)
				unit.options[j].style.backgroundColor = vErrorColor;
		}
	}

	return false;
}

function Validator_Method_checkIMEI(iNm, isRequired, iTitle)
{
	var item = vForm.elements[iNm];
	if( item == null )
	    return;
	if( isRequired == true ){
		if( !Validator.required(iNm, iTitle) )
			return false;
	} else {
          if (item.value.trim()=="") {
             return;
          }
        }


	var iValue = item.value.trim();
	if( iValue.search(/\W/) != -1 ){
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入不含中文的英數字！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}

	return true;
}

function Validator_Method_checkNumber(iNm, isRequired, iTitle, iMin, iMax)
{
	var item = vForm.elements[iNm];
	if( item == null )
	    return;
	if( isRequired == true ){
		if( !Validator.required(iNm, iTitle) )
			return false;
	} else {
          if (item.value.trim()=="") {
             return;
          }
        }

	
	var iValue = item.value.trim();
	if( isNaN(iValue) == true ){
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確數字！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if( arguments.length == 5 ){
		if( iValue.length!=0 && (parseInt(iValue, 10)<iMin || parseInt(iValue, 10)>iMax) ){
		    Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入介於 " + iMin + " 至 " + iMax + " 之間的正確數字！");
		    Validator.addErrorList(iNm);
		    item.style.backgroundColor = vErrorColor;
		    return false;
		}
	}
	return true;
}

function Validator_Method_checkHasFill(col1, col2, isRequired, iTitle) {
	var item1 = vForm.elements[col1];
	var item2 = vForm.elements[col2];
	
	if (item1 == null || item2 == null)
		return;
	
	var iValue1 = item1.value.trim();
	var iValue2 = item2.value.trim();
	
	if (iValue1 == '' && iValue2 == ''){
		if (isRequired == true) {
			if (!Validator.required(col1, iTitle))
				return false;
		}
	}

	if (iValue1 != ''){
		if (isNaN(iValue1) == true) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確數字！");
			Validator.addErrorList(col1);
			item1.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	
	if (iValue2 != ''){
		if (isNaN(iValue2) == true) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確數字！");
			Validator.addErrorList(col2);
			item2.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	
	return true;
}
function Validator_Method_checkInteger(iNm, isRequired, iTitle, iMin, iMax) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	} else {
		if (item.value.trim() == "") {
			return;
		}
	}

	var iValue = item.value.trim();

	if (iValue.search(/^([-]?\d+)([.][0]+)?$/) == -1) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確整數！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if (arguments.length == 5) {
		if (iValue.length != 0
				&& (parseInt(iValue, 10) < iMin || parseInt(iValue, 10) > iMax)) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入介於 " + iMin + " 至 "
					+ iMax + " 之間的正確整數！");
			Validator.addErrorList(iNm);
			item.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

// USED
function Validator_Method_checkIntegerRange(iNm, isRequired, iTitle, iMin, iMax) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	} else {
		if (item.value.trim() == "") {
			return;
		}
	}

	var iValue = item.value.trim();

	if (iValue.search(/^([-]?\d+)([.][0]+)?$/) == -1) {
		Validator.setMessage("列印筆數請輸入正確整數！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if (arguments.length == 5) {
		if (iValue.length != 0
				&& (parseInt(iValue, 10) < iMin || parseInt(iValue, 10) > iMax)) {
			Validator.setMessage("列印筆數請輸入小於 " + iMax + " 之正整數！");
			Validator.addErrorList(iNm);
			item.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

// USED
function Validator_Method_checkIntegerLength(iNm, isRequired, iTitle, iMin,
		iMax) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	} else {
		if (item.value.trim() == "") {
			return;
		}
	}

	var iValue = item.value.trim();
	if (iValue.search(/^([-]?\d+)([.][0]+)?$/) == -1) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確數值！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if (iValue.length != 0 && iValue.length < iMin || iValue.length > iMax) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入長度介於 " + iMin + " 至 "
				+ iMax + " 之間的正確數值！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	return true;
}

function Validator_Method_checkDate(iNm, isRequired, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.value.trim() == "YYYMMDD")
		item.value = "";
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim().replace(/\//g, '');
	if (iValue.length != 0 && iValue.isTWDate() == false) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確日期(YYYMMDD)！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if (iValue.length == 6)
		item.value = "0" + iValue;
	return true;
}

function Validator_Method_checkEDate(iNm, isRequired, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.length != 0 && iValue.isEDate() == false) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確西元日期！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	return true;
}

function Validator_Method_checkTime(iNm, isRequired, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.value.trim() == "HHNN")
		item.value = "";
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.length != 0 && iValue.isTime() == false) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確時間格式！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	return true;
}

function Validator_Method_checkHour(iNm, isRequired, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.value.trim() == "HH")
		item.value = "";
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.length != 0 && iValue.isHour() == false) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確時間格式！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	return true;
}

function Validator_Method_checkPhoneNo(iNm, isRequired, iTitle) {
	return Validator.checkLength(iNm, isRequired, iTitle, 12);
}

function Validator_Method_checkMobileNo(iNm, isRequired, iTitle) {
	return Validator.checkLength(iNm, isRequired, iTitle, 11);
}

function Validator_Method_checkID(iNm, isRequired, iTitle) {
	var objNm = vForm.elements[iNm];
	var IDPatern = /^([A-Za-z])([1-2])([0-9]{8})$/;

	if (((isRequired || objNm.value.length != 0) && objNm.value.search(IDPatern) == -1)) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：格式錯誤！");
		Validator.addErrorList(iNm);
		objNm.style.backgroundColor = vErrorColor;
		return false;
	}
	else{
		var objValue = $('#' + iNm).val();
		var pattern = "ABCDEFGHJKLMNPQRSTUVXYWZIO";
		var code = pattern.indexOf(objValue.toUpperCase().charAt(0)) + 10;
		var sum = 0;
        sum = Math.floor(code / 10) + 9 * (code % 10) + 8 * parseInt(objValue.charAt(1)) + 7 * parseInt(objValue.charAt(2))
        		+ 6 * parseInt(objValue.charAt(3)) + 5 * parseInt(objValue.charAt(4)) + 4 * parseInt(objValue.charAt(5))
        		+ 3 * parseInt(objValue.charAt(6)) + 2 * parseInt(objValue.charAt(7)) + 1 * parseInt(objValue.charAt(8))
        		+ parseInt(objValue.charAt(9));
        if ((sum % 10) != 0) {
        	Validator.setMessage("欄位 [ " + iTitle + " ]：格式錯誤！");
    		Validator.addErrorList(iNm);
    		objNm.style.backgroundColor = vErrorColor;
    		return false;
        }

	}
	objNm.value = objNm.value.toUpperCase();
	return true;
}

function Validator_Method_checkIdGender(iId, iGender) {
	var itemId = vForm.elements[iId];
	var itemGender = vForm.elements[iGender];
	if (itemId == null || itemGender == null)
		return;

	var valueGender = -1;
	var valueId = vForm.elements[iId].value;

	if (itemGender.type == "select-one")
		valueGender = itemGender[itemGender.selectedIndex].value;
	else if (itemGender[0].type == "radio" || itemGender[0].type == "checkbox") {
		for ( var i = 0; i < itemGender.length; i++) {
			if (itemGender[i].checked == true) {
				valueGender = itemGender[i].value;
				break;
			}
		}
	}

	if (valueId.length == 0 || valueGender == -1)
		return;

	if (valueGender != valueId.charAt(1)) {
		Validator.setMessage("身分證與性別不符！");
		Validator.addErrorList(iId);
		
		vForm.elements[iId].style.backgroundColor = vErrorColor;
		return false;
	}
	return true;
}

function Validator_Method_checkDateInterval(bDtNm, eDtNm, isRequired, iTitle) {
	var bDt = vForm.elements[bDtNm];
	var eDt = vForm.elements[eDtNm];
	if (bDt == null || eDt == null)
		return;

	if (bDt.value.trim() == "YYYMMDD")
		bDt.value = "";
	if (eDt.value.trim() == "YYYMMDD")
		eDt.value = "";

	if (isRequired == false
			&& (bDt.value.trim().length != 0 || eDt.value.trim().length != 0))
		isRequired = true;
	var begin = Validator.checkDate(bDtNm, isRequired, iTitle + "_起始日期");
	var end = Validator.checkDate(eDtNm, isRequired, iTitle + "_結束日期");

	if (begin == false || end == false)
		return false;

	var bDtValue = bDt.value.trim().replace(/\//g, "");
	var eDtValue = eDt.value.trim().replace(/\//g, "");
	if (bDtValue.length != 0 && eDtValue.length != 0) {
		if (parseInt(bDtValue, 10) > parseInt(eDtValue, 10)) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：起始日期必須小於或等於結束日期！");
			Validator.addErrorList(bDtNm);
			Validator.addErrorList(eDtNm);
			bDt.style.backgroundColor = vErrorColor;
			eDt.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkDateIntervalSP(bDtNm, eDtNm, isRequired, bTitle, eTitle, operator) {
	var bDt = vForm.elements[bDtNm];
	var eDt = vForm.elements[eDtNm];
	if (bDt == null || eDt == null)
		return;

	if (bDt.value.trim() == "YYYMMDD")
		bDt.value = "";
	if (eDt.value.trim() == "YYYMMDD")
		eDt.value = "";

	if (isRequired == false
			&& (bDt.value.trim().length != 0 || eDt.value.trim().length != 0))
		isRequired = true;

	var bDtValue = bDt.value.trim().replace(/\//g, "");
	var eDtValue = eDt.value.trim().replace(/\//g, "");
	if (bDtValue.length != 0 && eDtValue.length != 0) {
		if (parseInt(bDtValue, 10) >= parseInt(eDtValue, 10)) {
			Validator.setMessage("欄位 [ " + bTitle + " ]：" + bTitle + "必須小於" + eTitle + "！");
			Validator.addErrorList(bDtNm);
			Validator.addErrorList(eDtNm);
			bDt.style.backgroundColor = vErrorColor;
			eDt.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkNumberInterval(bNum, eNum, isRequired, iTitle) {
	var bData = vForm.elements[bNum];
	var eData = vForm.elements[eNum];
	if (bData == null || eData == null)
		return;

	if (isRequired == false && (bData.value.trim().length != 0 || eData.value.trim().length != 0))
		isRequired = true;
	
	if (Validator.checkNumber(bNum,true, "最晚日數") && Validator.checkNumber(bNum,true, "最早日數")){
		var bDataValue = bData.value.trim().replace(/\//g, "");
		var eDataValue = eData.value.trim().replace(/\//g, "");
		if (bDataValue.length != 0 && eDataValue.length != 0) {
			if (parseInt(bDataValue, 10) > parseInt(eDataValue, 10)) {
				Validator.setMessage("欄位 [ " + iTitle + " ]：最晚日數必須小於或等於最早日數！");
				Validator.addErrorList(bNum);
				Validator.addErrorList(eNum);
				bData.style.backgroundColor = vErrorColor;
				eData.style.backgroundColor = vErrorColor;
				return false;
			}
		}
		return true;
	}
	else	
		return false;
}

function Validator_Method_checkTimeInterval(bTmNm, eTmNm, isRequired, iTitle) {
	var bTm = vForm.elements[bTmNm];
	var eTm = vForm.elements[eTmNm];
	if (bTm == null || eTm == null)
		return;

	if (bTm.value.trim() == "HHNN")
		bTm.value = "";
	if (eTm.value.trim() == "HHNN")
		eTm.value = "";

	if (isRequired == false
			&& (bTm.value.trim().length != 0 || eTm.value.trim().length != 0))
		isRequired = true;

	var begin = Validator.checkTime(bTmNm, isRequired, iTitle + "_起始時間");
	var end = Validator.checkTime(eTmNm, isRequired, iTitle + "_結束時間");
	if (begin == false || end == false)
		return false;

	var bTmValue = bTm.value.trim();
	var eTmValue = eTm.value.trim();
	if (bTmValue.length != 0 && eTmValue.length != 0) {
		if (parseInt(bTmValue, 10) > parseInt(eTmValue, 10)) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：起始時間必須小於或等於結束時間！");
			Validator.addErrorList(bTmNm);
			Validator.addErrorList(eTmNm);
			bTm.style.backgroundColor = vErrorColor;
			eTm.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkDTInterval(bDtNm, bTmNm, eDtNm, eTmNm,
		isRequired, iTitle) {
	var bDt = vForm.elements[bDtNm];
	var bTm = vForm.elements[bTmNm];
	var eDt = vForm.elements[eDtNm];
	var eTm = vForm.elements[eTmNm];
	if (bDt == null || bTm == null || eDt == null || eTm == null)
		return;

	if (bDt.value.trim() == "YYYMMDD")
		bDt.value = "";
	if (eDt.value.trim() == "YYYMMDD")
		eDt.value = "";
	if (bTm.value.trim() == "HHNN")
		bTm.value = "";
	if (eTm.value.trim() == "HHNN")
		eTm.value = "";

	if (isRequired == false
			&& (bDt.value.trim().length != 0 || bTm.value.trim().length != 0
					|| eDt.value.trim().length != 0 || eTm.value.trim().length != 0))
		isRequired = true;

	var beginDt = Validator.checkDate(bDtNm, isRequired, iTitle + "_起始日期");
	var endDt = Validator.checkDate(eDtNm, isRequired, iTitle + "_結束日期");

	if (beginDt == true && endDt == true && bDt.value.trim() != ''
			&& bTm.value.trim() == '' && eDt.value.trim() != ''
			&& eTm.value.trim() == '') {
		bTm.value = "0000";
		eTm.value = "2359";
	}
	var beginTm = Validator.checkTime(bTmNm, isRequired, iTitle + "_起始時間");
	var endTm = Validator.checkTime(eTmNm, isRequired, iTitle + "_結束時間");

	if (beginDt == false || beginTm == false || endDt == false
			|| endTm == false)
		return false;

	var bDtValue = bDt.value.trim();
	var bTmValue = bTm.value.trim();
	var eDtValue = eDt.value.trim();
	var eTmValue = eTm.value.trim();
	if (bDtValue.length != 0 && bTmValue.length != 0 && eDtValue.length != 0
			&& eTmValue.length != 0) {
		if (parseInt(bDtValue + bTmValue, 10) > parseInt(eDtValue + eTmValue,
				10)) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：起始日期時間必須小於或等於結束日期時間！");
			Validator.addErrorList(bDtNm);
			Validator.addErrorList(bTmNm);
			Validator.addErrorList(eDtNm);
			Validator.addErrorList(eTmNm);
			bDt.style.backgroundColor = vErrorColor;
			bTm.style.backgroundColor = vErrorColor;
			eDt.style.backgroundColor = vErrorColor;
			eTm.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkDHInterval(bDtNm, bTmNm, eDtNm, eTmNm,
		isRequired, iTitle) {
	var bDt = vForm.elements[bDtNm];
	var bTm = vForm.elements[bTmNm];
	var eDt = vForm.elements[eDtNm];
	var eTm = vForm.elements[eTmNm];
	if (bDt == null || bTm == null || eDt == null || eTm == null)
		return;

	if (bDt.value.trim() == "YYYMMDD")
		bDt.value = "";
	if (eDt.value.trim() == "YYYMMDD")
		eDt.value = "";
	if (bTm.value.trim() == "HH")
		bTm.value = "";
	if (eTm.value.trim() == "HH")
		eTm.value = "";

	if (isRequired == false
			&& (bDt.value.trim().length != 0 || bTm.value.trim().length != 0
					|| eDt.value.trim().length != 0 || eTm.value.trim().length != 0))
		isRequired = true;

	var beginDt = Validator.checkDate(bDtNm, isRequired, iTitle + "_起始日期");
	var endDt = Validator.checkDate(eDtNm, isRequired, iTitle + "_結束日期");
	
	if (beginDt == true && endDt == true && bDt.value.trim() != ''
			&& bTm.value.trim() == '' && eDt.value.trim() != ''
			&& eTm.value.trim() == '') {
		bTm.value = "0000";
		eTm.value = "2359";
	}
	var beginTm = Validator.checkHour(bTmNm, isRequired, iTitle + "_起始時間");
	var endTm = Validator.checkHour(eTmNm, isRequired, iTitle + "_結束時間");

	if (beginDt == false || beginTm == false || endDt == false
			|| endTm == false)
		return false;

	var bDtValue = bDt.value.trim().replace(/\//g, "");
	var bTmValue = bTm.value.trim() + "00";
	var eDtValue = eDt.value.trim().replace(/\//g, "");
	var eTmValue = eTm.value.trim() + "00";
	if (bDtValue.length != 0 && bTmValue.length != 0 && eDtValue.length != 0
			&& eTmValue.length != 0) {
		if (parseInt(bDtValue + bTmValue, 10) > parseInt(eDtValue + eTmValue,
				10)) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：起始日期時間必須小於或等於結束日期時間！");
			Validator.addErrorList(bDtNm);
			Validator.addErrorList(bTmNm);
			Validator.addErrorList(eDtNm);
			Validator.addErrorList(eTmNm);
			bDt.style.backgroundColor = vErrorColor;
			bTm.style.backgroundColor = vErrorColor;
			eDt.style.backgroundColor = vErrorColor;
			eTm.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkddlDTInterval(bDtNm, bTmNm, bTmm, eDtNm, eTmNm,
		eTmm, isRequired, iTitle) {
	var bDt = vForm.elements[bDtNm];
	var bTm = vForm.elements[bTmNm];
	var bMM = vForm.elements[bTmm];
	var eDt = vForm.elements[eDtNm];
	var eTm = vForm.elements[eTmNm];
	var eMM = vForm.elements[eTmm];
	if (bDt == null || bTm == null || bMM == null || eDt == null || eTm == null
			|| eMM == null)
		return;

	if (bDt.value.trim() == "YYYMMDD")
		bDt.value = "";
	if (eDt.value.trim() == "YYYMMDD")
		eDt.value = "";
	if (bTm.value.trim() == "HH")
		bTm.value = "";
	if (eTm.value.trim() == "HH")
		eTm.value = "";
	if (bMM.value.trim() == "mm")
		bMM.value = "";
	if (eMM.value.trim() == "mm")
		eMM.value = "";

	if (isRequired == false
			&& (bDt.value.trim().length != 0 || bTm.value.trim().length != 0
					|| bMM.value.trim().length != 0
					|| eDt.value.trim().length != 0
					|| eTm.value.trim().length != 0 || eMM.value.trim().length != 0))
		isRequired = true;

	var beginDt = Validator.checkDate(bDtNm, isRequired, iTitle + "_起始日期");
	var endDt = Validator.checkDate(eDtNm, isRequired, iTitle + "_結束日期");
	
	if (beginDt == true && endDt == true && bDt.value.trim() != ''
			&& bTm.value.trim() == '' && eDt.value.trim() != ''
			&& eTm.value.trim() == '') {
		bTm.value = "0000";
		eTm.value = "2359";
	}
	var beginTm = Validator.checkHour(bTmNm, isRequired, iTitle + "_起始時間");
	var endTm = Validator.checkHour(eTmNm, isRequired, iTitle + "_結束時間");

	if (beginDt == false || beginTm == false || endDt == false
			|| endTm == false)
		return false;

	var bDtValue = bDt.value.trim().replace(/\//g, "");
	var bTmValue = bTm.value.trim() + bMM.value.trim();
	var eDtValue = eDt.value.trim().replace(/\//g, "");
	var eTmValue = eTm.value.trim() + eMM.value.trim();
	if (bDtValue.length != 0 && bTmValue.length != 0 && eDtValue.length != 0
			&& eTmValue.length != 0) {
		if (parseInt(bDtValue + bTmValue, 10) > parseInt(eDtValue + eTmValue,
				10)) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：起始日期時間必須小於或等於結束日期時間！");
			Validator.addErrorList(bDtNm);
			Validator.addErrorList(bTmNm);
			Validator.addErrorList(eDtNm);
			Validator.addErrorList(eTmNm);
			Validator.addErrorList(bTmm);
			Validator.addErrorList(eTmm);
			bDt.style.backgroundColor = vErrorColor;
			bTm.style.backgroundColor = vErrorColor;
			eDt.style.backgroundColor = vErrorColor;
			eTm.style.backgroundColor = vErrorColor;
			bMM.style.backgroundColor = vErrorColor;
			eMM.style.backgroundColor = vErrorColor;
			return false;
		}
	}
	return true;
}

function Validator_Method_checkAllEmpty() {
	for ( var i = 0; i < vForm.length; i++) {
		if (vForm.elements[i].type == "text"
				|| vForm.elements[i].type == "areatext"
				|| vForm.elements[i].type == "select-one") {
			
			if ((vForm.elements[i].value.length != 0)
					&& (vForm.elements[i].value != "YYYMMDD"))
				return false;
		} else if (vForm.elements[i].type == "radio"
				|| vForm.elements[i].type == "checkbox") {
			if (vForm.elements[i].checked == true)
				return false;
		}
	}
	Validator.setMessage("必須至少輸入其中一個欄位！");
	return true;
}

function Validator_Method_checkAllEmptyExceptCheckBox() {
	for ( var i = 0; i < vForm.length; i++) {
		if (vForm.elements[i].type == "text"
				|| vForm.elements[i].type == "areatext"
				|| vForm.elements[i].type == "select-one") {
			
			if ((vForm.elements[i].value.length != 0)
					&& (vForm.elements[i].value != "YYYMMDD"))
				return false;
		} else if (vForm.elements[i].type == "radio") {
			if (vForm.elements[i].checked == true
					&& vForm.elements[i].value != '')
				return false;
		}
	}
	Validator.setMessage("必須至少輸入其中一個欄位！");
	return true;
}

function Validator_Method_addErrorList(iNm) {
	vErrorIdList += iNm + ',';
}

function Validator_Method_getErrorList() {
	return vErrorIdList;
}

function Validator_Method_setErrorList() {
	vForm.ValidatorErrorIdList.value = vErrorIdList;
}

function Validator_Method_markError(PForm1) {
	var errList = PForm1.ValidatorErrorIdList.value;
	if (errList.length == 0)
		return;
	var err = errList.split(",");
	for ( var i = 0; i < err.length; i++) {
		if (err[i].length != 0) {
			var item = PForm1.elements[err[i]];
			if (item != null) {
				if (item.type == "text" || item.type == "textarea")
					item.style.backgroundColor = vErrorColor;
				else if (item.type == "select-one") {
					for ( var j = 0; j < item.length; j++)
						item.options[j].style.backgroundColor = vErrorColor;
				} else if (item.type == "select-multiple") {
					item.style.backgroundColor = vErrorColor;
					for ( var j = 0; j < item.length; j++)
						item.options[j].style.backgroundColor = vErrorColor;
				} else if (item.type == "radio" || item.type == "checkbox")
					item.style.backgroundColor = vErrorColor;
				else if (item[0].type == "radio" || item[0].type == "checkbox") {
					for ( var j = 0; j < item.length; j++)
						item[j].style.backgroundColor = vErrorColor;
				}
			}
		}
	}
}

function Validator_Method_checkPK() {
	for ( var i = 0; i < arguments.length; i++) {
		if (arguments[i] != "" && arguments[i] != null) {
			var item = vForm.elements[arguments[i]];
			if (item != null) {
				var iValue = item.value.trim();
				if (iValue.length != 0)
					return true;
			}
		}
	}
	Validator.setMessage("請先選取一筆資料！");
	return false;
}

function Validator_Method_setBGColor() {
	for ( var i = 0; i < arguments.length; i++) {
		if (arguments[i] != "" && arguments[i] != null) {
			Validator.addErrorList(arguments[i]);
			var item = vForm.elements[arguments[i]];
			if (item.type == "text" || item.type == "textarea")
				item.style.backgroundColor = vErrorColor;
			else if (item.type == "select-one") {
				for ( var j = 0; j < item.length; j++)
					item.options[j].style.backgroundColor = vErrorColor;
			} else if (item.type == "select-multiple") {
				item.style.backgroundColor = vErrorColor;
				for ( var j = 0; j < item.length; j++)
					item.options[j].style.backgroundColor = vErrorColor;
			} else if (item.type == "radio" || item.type == "checkbox")
				item.style.backgroundColor = vErrorColor;
			else if (item[0].type == "radio" || item[0].type == "checkbox") {
				for ( var j = 0; j < item.length; j++)
					item[j].style.backgroundColor = vErrorColor;
			}
		}
	}
}

// Candy Add
// 判斷是否正確的民國前日曆格式
// 輸入格式應為(Y)YYMMDD
String.prototype.isBTWDate = function() {
	var TWDPatern = /^\d{2,3}((0?[1-9])|(1[0-2]))(([0]?[1-9])|([1-2]?[0-9])|(3[0-1]))$/

	// 判斷日期格式正確性
	if (this.search(TWDPatern) == -1) {
		return false;
	}
	// 判斷是否是正確(合法)日期
	var nYear, nMonth, nDay;
	
	if (this.length == 6) {
		nYear = 1911 - parseInt(this.substring(0, 2), 10);
		nMonth = this.substring(2, 4);
		nDay = this.substring(4, 6);
	} else {
		nYear = 1911 - parseInt(this.substring(0, 3), 10);
		nMonth = this.substring(3, 5);
		nDay = this.substring(5, 7);
	}
	var tmp = new Date(nYear, nMonth - 1, nDay);

	if ((nYear != tmp.getFullYear() || nMonth != (tmp.getMonth() + 1) || nDay != tmp
			.getDate())) {
		return false;
	}
	return true;
}

function Validator_Method_checkBDate(iNm, isRequired, iTitle) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.length != 0 && iValue.isBTWDate() == false) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確日期！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if (iValue.length == 6)
		item.value = "0" + iValue;
	return true;
}

// 判斷是否正確的民國年月格式
// 輸入格式應為(Y)YYMM
String.prototype.isTWYYYMM = function() {
	var TWYYYMMPatern = /^\d{2,3}((0?[1-9])|(1[0-2]))$/;

	// 判斷日期格式正確性
	if (this.search(TWYYYMMPatern) == -1) {
		return false;
	}

	// 判斷是否是正確(合法)日期
	var nYear, nMonth, nDay;
	
	if (this.length == 4) {
		nYear = parseInt(this.substring(0, 2), 10) + 1911;
		nMonth = this.substring(2, 4);
		nDay = "01";
	} else {
		nYear = parseInt(this.substring(0, 3), 10) + 1911;
		nMonth = this.substring(3, 5);
		nDay = "01";
	}
	var tmp = new Date(nYear, nMonth - 1, nDay);

	if ((nYear != tmp.getFullYear() || nMonth != (tmp.getMonth() + 1) || nDay != tmp
			.getDate())) {
		return false;
	}
	return true;
}
String.prototype.isGraterThanSysYYYMMDD = function() {
	var nYear = '', nMonth = '', nDay = '';
	var tmp = new Date();
	
	if (this.length == 6) {
		nYear = parseInt(this.substring(0, 2), 10) + 1911;
		nMonth = this.substring(2, 4);
		nDay = this.substring(4, 6);
	} else {
		nYear = parseInt(this.substring(0, 3), 10) + 1911;
		nMonth = this.substring(3, 5);
		nDay = this.substring(5, 7);
	}
	
	var nowYear = tmp.getFullYear();
	var nowMonth = tmp.getMonth() + 1;
	var nowDay = tmp.getDate();
	
	if (nowMonth.toString().length == 1) {
		nowMonth = "0" + nowMonth;
	} else {
		nowMonth = "" + nowMonth;
	}
	if (nowDay.toString().length == 1) {
		nowDay = "0" + nowDay;
	} else {
		nowDay = "" + nowDay;
	}

	var nowYYYMM = parseInt(nowYear + nowMonth + nowDay, 10);
	var inputYYYMM = parseInt(nYear + nMonth + nDay, 10);
	if (inputYYYMM >= nowYYYMM) {
		return true;
	} else {
		return false;
	}
}
String.prototype.isGraterThanSysYYYYMMDD = function() {
	var nYear = '', nMonth = '', nDay = '';
	var tmp = new Date();
	
	nYear = parseInt(this.substring(0, 4), 10);
	nMonth = this.substring(4, 6);
	nDay = this.substring(6, 8);
	
	var nowYear = tmp.getFullYear();
	var nowMonth = tmp.getMonth() + 1;
	var nowDay = tmp.getDate();
	
	if (nowMonth.toString().length == 1) {
		nowMonth = "0" + nowMonth;
	} else {
		nowMonth = "" + nowMonth;
	}
	
	if (nowDay.toString().length == 1) {
		nowDay = "0" + nowDay;
	} else {
		nowDay = "" + nowDay;
	}

	var nowYYYMMDD = parseInt(nowYear + nowMonth + nowDay, 10);
	var inputYYYMMDD = parseInt(nYear + nMonth + nDay, 10);
	if (inputYYYMMDD >= nowYYYMMDD) {
		return true;
	} else {
		return false;
	}
}
function Validator_Method_compareWithSysDate(iNm, isRequired, iTitle, type, txtlength, operator) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim().replace(/\//g,'');
	if (iValue.length == txtlength){
		if (iValue.length != 0) {
			var isDateFormat = false;
			if (type == 'TW')
				isDateFormat = iValue.isTWDate();
			else
				isDateFormat = iValue.isEDate();
			
			if (isDateFormat == false){
				Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確年月！");
				Validator.addErrorList(iNm);
				item.style.backgroundColor = vErrorColor;
				return false;
			}
		}
		
		var islaterThanSysDate = false;
		if (type == 'TW')
			islaterThanSysDate = iValue.isGraterThanSysYYYMMDD();
		else
			islaterThanSysDate = iValue.isGraterThanSysYYYYMMDD();
		
		if (operator == '<' && islaterThanSysDate == true) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：必須小於系統年月日！");
			Validator.addErrorList(iNm);
			item.style.backgroundColor = vErrorColor;
			return false;
		} else if (operator == '>' && islaterThanSysDate == false) {
			Validator.setMessage("欄位 [ " + iTitle + " ]：必須大於系統年月日！");
			Validator.addErrorList(iNm);
			item.style.backgroundColor = vErrorColor;
			return false;
		}
		return true;
	}
	else{
		Validator.setMessage("欄位 [ " + iTitle + " ]：不符合日期輸入規範！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
}

function Validator_Method_compareWithSysYYYMM(iNm, logicalOperator, isRequired,
		iTitle, whichDateNm) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.length != 0 && iValue.isTWYYYMM() == false) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：請輸入正確年月！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}

	var tmp = new Date();
	var nYear, nMonth;
	
	if (iValue.length == 4) {
		nYear = parseInt(iValue.substring(0, 2), 10) + 1911;
		nMonth = iValue.substring(2, 4);
	} else {
		nYear = parseInt(iValue.substring(0, 3), 10) + 1911;
		nMonth = iValue.substring(3, 5);
	}
	var nowYear = tmp.getFullYear();
	var nowMonth = tmp.getMonth() + 1;
	if (nowMonth.toString().length == 1) {
		nowMonth = "0" + nowMonth;
	} else {
		nowMonth = "" + nowMonth;
	}

	var nowYYYMM = parseInt(nowYear + nowMonth, 10);
	var inputYYYMM = parseInt(nYear + nMonth, 10);

	var operatorName = "";
	switch (logicalOperator) {
	case ">":
		operatorName = "大於";
		break;
	case "<":
		operatorName = "小於";
		break;
	case ">=":
		operatorName = "大於等於";
		break;
	case "<=":
		operatorName = "小於等於";
		break;
	case "=":
		operatorName = "等於";
		break;
	default:
		operatorName = logicalOperator;
	}
	if (!eval(inputYYYMM + logicalOperator + nowYYYMM)) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：" + whichDateNm + "必須"
				+ operatorName + "系統年月！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	} else {
		if (iValue.length == 4)
			item.value = "0" + iValue;
		return true;
	}

}

// Add By Jonas 20060416
function Validator_Method_CheckAllCheckbox(iNm1, iNm2, iNm3, iNm4, iNm5, iNm6,
		iNm7, iTitle) {
	if (Validator.checkOneCheckbox(iNm1) || Validator.checkOneCheckbox(iNm2)
			|| Validator.checkOneCheckbox(iNm3)
			|| Validator.checkOneCheckbox(iNm4)
			|| Validator.checkOneCheckbox(iNm5)
			|| Validator.checkOneCheckbox(iNm6)
			|| Validator.checkOneCheckbox(iNm7))
		return true;
	Validator.setMessage("欄位 [ " + iTitle + " ]：必須選取！");
	var item = vForm.elements[iNm1];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
	item = vForm.elements[iNm2];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
	item = vForm.elements[iNm3];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
	item = vForm.elements[iNm4];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
	item = vForm.elements[iNm5];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
	item = vForm.elements[iNm6];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
	item = vForm.elements[iNm7];
	if (item != null && item.style != null)
		item.style.backgroundColor = vErrorColor;
}

function Validator_Method_checkRangeLength(iNm, isRequired, iTitle, minLen,
		maxLen) {
	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (isRequired == true) {
		if (!Validator.required(iNm, iTitle))
			return false;
	}

	var iValue = item.value.trim();
	if (iValue.length > maxLen) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位長度超過 " + maxLen + " ！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}
	if (iValue.length < minLen) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位長度少於 " + minLen + " ！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}

	// 檢查是否包含非署造字
	var _unMapChars = ""; // 存放非署造字(or Unicode)
	for (idxTmp = 0; idxTmp < iValue.length; idxTmp++) {
		var checkChar = iValue.charAt(idxTmp);
		if (checkChar.CheckUnicode() == true) {
			if (_unMapChars != "")
				_unMapChars = _unMapChars.concat("、");
			_unMapChars = _unMapChars.concat(checkChar);
		}
	}
	if (_unMapChars != "") {
		Validator.setMessage("欄位 [ " + iTitle + " ]：欄位內容包含非署造字【" + _unMapChars
				+ "】！");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	}

	return true;
}

// Add By Jonas 20060416
function Validator_Method_CheckOneCheckbox(iNm) {

	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.checked == true)
		return true;
	return false;
}

// Add By Stone 20140108
function Validator_Method_CheckMinYear(iNm, iTitle, minYear) {

	var item = vForm.elements[iNm];
	if (item == null)
		return;
	if (item.value.trim() == "YYYMMDD")
		item.value = "";

	var iValue = item.value.trim().replace(/\//g, '');
	if (iValue.length != 0 && iValue.isTWDate() == false)
		return;
	var tmp = new Date();
	var nYear, nMonth, nDay;
	
	if (iValue.length == 6) {
		nYear = parseInt(iValue.substring(0, 2), 10) + 1911;
		nMonth = iValue.substring(2, 4);
		nDay = iValue.substring(4, 6);
	} else {
		nYear = parseInt(iValue.substring(0, 3), 10) + 1911;
		nMonth = iValue.substring(3, 5);
		nDay = iValue.substring(5, 7);
	}
	var nowYear = tmp.getFullYear();
	var nowMonth = tmp.getMonth() + 1;
	var nowDay = tmp.getDate();
	if (nowMonth.toString().length == 1) {
		nowMonth = "0" + nowMonth;
	} else {
		nowMonth = "" + nowMonth;
	}

	if (nowDay.toString().length == 1) {
		nowDay = "0" + nowDay;
	} else {
		nowDay = "" + nowDay;
	}

	var nowYYYMMDD = parseInt(nowYear.toString() + nowMonth.toString()
			+ nowDay.toString(), 10);
	var inputYYYMMDD = parseInt(nYear + minYear + nMonth + nDay, 10);

	if (iValue.length != 0 && inputYYYMMDD < nowYYYMMDD) {
		Validator.setMessage("資料有誤");
		Validator.addErrorList(iNm);
		item.style.backgroundColor = vErrorColor;
		return false;
	} else {
		return true;
	}
}

// 20141002 Stone for NV2
function Validator_Method_IsCheckBoxChecked(iNm, iTitle) {
	if ($("input[name='" + iNm + "']:checked").length == 0) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：必須至少選擇一項！");
	}
}

// 20141003 Stone for NV2
function Validator_Method_IsRadioChecked(iNm, iTitle) {
	if ($("input[name='" + iNm + "']:checked").length == 0) {
		Validator.setMessage("欄位 [ " + iTitle + " ]：必須選取！");
	}
}

// 20141002 Stone for NV2
function setColorRecover() {
	$("input[type='text']").removeAttr('style');
	$("input[type='password']").removeAttr('style');
	$("textarea").removeAttr('style');
}

//20141002 Stone for NV2
function Validator_Method_checkMail(iNm, isRequired, iTitle) {
	var objNm = vForm.elements[iNm];
	var IDPatern = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9])+$/;
	
	if (IDPatern.exec(objNm.value) == null)
	 {
		Validator.setMessage("欄位 [ " + iTitle + " ]：格式錯誤！");
		Validator.addErrorList(iNm);
		objNm.style.backgroundColor = vErrorColor;
		return false;                
	 }
	
	return true;
}

function Validator_Method_checkSysDateAndPeriod(bNm, eNm, isRequired, iTitle, type, txtlength, operator){
	var isPeriodMapping = Validator.checkDateInterval(bNm, eNm, isRequired, iTitle);
	var isSysDateMapping = Validator.compareWithSysDate(bNm, true, iTitle, type, txtlength, operator);
	
	return (isPeriodMapping && isSysDateMapping);
}

var Validator = new Validator_Class();
var vErrorColor = '#FFFF00';
var vOkColor = '';
var vErrorMsg;
var vErrorIdList;
var vForm;

function Validator_Class() {
	this.init = Validator_Method_init;
	this.isValid = Validator_Method_isValid;
	this.setMessage = Validator_Method_setMessage;
	this.getMessage = Validator_Method_getMessage;
	this.showMessage = Validator_Method_showMessage;
	this.resetMessage = Validator_Method_resetMessage;
	this.required = Validator_Method_required;
	this.checkECaseNo = Validator_Method_checkECaseNo;
        //20230302 警署反應在單簽帶入的送交人姓名會被擋住「晧」字, 為免影響太大，先開一個NEW是不經過
        //「非署造字」的檢查，觀察一陣子後可以將所有的checkLength都移到New
        this.checkLength           = Validator_Method_checkLength;
        this.checkLength_New = Validator_Method_checkLength_New;
        this.checkRadio = Validator_Method_checkRadio;
	this.checkRadiohasValue = Validator_Method_checkRadiohasValue;
	this.checkCheckbox = Validator_Method_checkRadio;
	this.checkCombo = Validator_Method_checkCombo;
	this.filterCombo = Validator_Method_filterCombo;
	this.checkUnit = Validator_Method_checkUnit;
        this.checkIMEI = Validator_Method_checkIMEI; // Add By Ethan 20180710
        this.checkPhone =Validator_Method_checkPhone;
	this.checkNumber = Validator_Method_checkNumber;
	this.checkHasFill = Validator_Method_checkHasFill; // Add By Stone 20141110
	this.checkInteger = Validator_Method_checkInteger;
	this.checkIntegerRange = Validator_Method_checkIntegerRange; // 20131009 Stone
	this.checkIntegerLength = Validator_Method_checkIntegerLength; // 20130903 Stone
	this.checkDate = Validator_Method_checkDate;
	this.checkEDate = Validator_Method_checkEDate;
	this.checkTime = Validator_Method_checkTime;
	this.checkHour = Validator_Method_checkHour;

	this.checkPhoneNo = Validator_Method_checkPhoneNo;
	this.checkMobileNo = Validator_Method_checkMobileNo;
	this.checkID = Validator_Method_checkID;
	this.checkIdGender = Validator_Method_checkIdGender;
	this.checkDateInterval = Validator_Method_checkDateInterval;
	this.checkDateIntervalSP = Validator_Method_checkDateIntervalSP; // Add By Stone 20150115
	this.checkNumberInterval = Validator_Method_checkNumberInterval; // Add By Stone 20130903
	this.checkTimeInterval = Validator_Method_checkTimeInterval;
	this.checkDTInterval = Validator_Method_checkDTInterval;
	this.checkDHInterval = Validator_Method_checkDHInterval;
	this.checkddlDTInterval = Validator_Method_checkddlDTInterval;
	this.checkAllEmpty = Validator_Method_checkAllEmpty;
	this.checkAllEmptyExceptCheckBox = Validator_Method_checkAllEmptyExceptCheckBox;
	this.addErrorList = Validator_Method_addErrorList;
	this.setErrorList = Validator_Method_setErrorList;
	this.getErrorList = Validator_Method_getErrorList;
	this.markError = Validator_Method_markError;
	this.checkPK = Validator_Method_checkPK;
	this.setBGColor = Validator_Method_setBGColor;
	this.checkBDate = Validator_Method_checkBDate;
	this.compareWithSysDate = Validator_Method_compareWithSysDate;
	this.compareWithSysYYYMM = Validator_Method_compareWithSysYYYMM;
	this.checkRangeLength = Validator_Method_checkRangeLength;
	this.checkAllCheckbox = Validator_Method_CheckAllCheckbox; // Add By Jonas 20060416
	this.checkOneCheckbox = Validator_Method_CheckOneCheckbox; // Add By Jonas 20060416
	this.CheckMinYear = Validator_Method_CheckMinYear; // Add By Stone 20140108
	this.isCheckBoxChecked = Validator_Method_IsCheckBoxChecked; // Add By Stone 20141002
	this.isRadioChecked = Validator_Method_IsRadioChecked; // Add By Stone 20141003
	this.checkMail = Validator_Method_checkMail; // Add By Stone 20141012
	this.checkSysDateAndPeriod = Validator_Method_checkSysDateAndPeriod; // Add By Stone 20150114
}

// 文鼎科技 Unicode 偵測 Javascript 函式
// 版本：V1.00
// function CheckUnicode(s)
// 輸入：要檢查的字串
// 輸出：當字串裡含有無法轉到MS950的，則傳回true，若都可轉則傳回false

String.prototype.CheckUnicode = function() {
	for (i = 0, ib = this.length; i < ib; i++) {
		unicode = this.charCodeAt(i);
		j1 = 0;
		j2 = _ua.length / 2;

		while (j2 - j1 > 0) {
			j = Math.floor((j2 + j1) / 2);
			begin = parseInt(_ua[j * 2], 16);
			end = parseInt(_ua[j * 2 + 1], 16);
			if (unicode > end) {
				if (j1 == j)
					break;
				j1 = j;
			} else if (unicode < begin) {
				if (j2 == j)
					break;
				j2 = j;
			} else
				return true;
		}
	}
	return false;
}

_ua = new Array("0080", "00a6", "00a8", "00ae", "00b2", "00b6", "00b8", "00d6",
		"00d8", "00f6", "00f8", "02c6", "02c8", "02c8", "02cc", "02cc", "02ce",
		"02d8", "02da", "0390", "03a2", "03a2", "03aa", "03b0", "03c2", "03c2",
		"03ca", "2012", "2015", "2017", "201a", "201b", "201e", "2024", "2028",
		"2031", "2033", "2034", "2036", "203a", "203c", "20ab", "20ad", "2102",
		"2104", "2104", "2106", "2108", "210a", "215f", "216a", "218f", "2194",
		"2195", "219a", "2214", "2216", "2219", "221b", "221d", "2221", "2222",
		"2224", "2224", "2226", "2228", "222c", "222d", "222f", "2233", "2236",
		"2251", "2253", "225f", "2262", "2265", "2268", "2294", "2296", "2298",
		"229a", "22a4", "22a6", "22be", "22c0", "24ff", "2501", "2501", "2503",
		"250b", "250d", "250f", "2511", "2513", "2515", "2517", "2519", "251b",
		"251d", "2523", "2525", "252b", "252d", "2533", "2535", "253b", "253d",
		"254f", "2575", "2580", "2590", "2592", "2596", "259f", "25a2", "25b1",
		"25b4", "25bb", "25be", "25c5", "25c8", "25ca", "25cc", "25cd", "25d0",
		"25e1", "25e6", "2604", "2607", "263f", "2641", "2641", "2643", "2fff",
		"3004", "3007", "3013", "3013", "3016", "301c", "301f", "3020", "302a",
		"3104", "312a", "32a2", "32a4", "338d", "3390", "339b", "339f", "33a0",
		"33a2", "33c3", "33c5", "33cd", "33cf", "33d0", "33d3", "33d4", "33d6",
		"33fd", "4e02", "4e02", "4e04", "4e06", "4e12", "4e13", "4e17", "4e17",
		"4e1a", "4e1d", "4e20", "4e25", "4e27", "4e2a", "4e2c", "4e2c", "4e2f",
		"4e2f", "4e34", "4e37", "4e3a", "4e3a", "4e3d", "4e41", "4e44", "4e44",
		"4e46", "4e46", "4e49", "4e4a", "4e4c", "4e4c", "4e50", "4e51", "4e54",
		"4e55", "4e57", "4e57", "4e5a", "4e5b", "4e60", "4e68", "4e6a", "4e72",
		"4e74", "4e7d", "4e80", "4e81", "4e85", "4e85", "4e87", "4e87", "4e89",
		"4e8a", "4e8f", "4e90", "4e96", "4e98", "4e9a", "4e9a", "4e9c", "4e9d",
		"4ea0", "4ea0", "4ea3", "4ea3", "4ea7", "4ea7", "4ea9", "4eaa", "4eaf",
		"4eb2", "4eb4", "4eb5", "4eb7", "4eb8", "4ebb", "4ebf", "4ec5", "4ec5",
		"4ecc", "4ecc", "4ece", "4ed3", "4edb", "4edb", "4ee0", "4ee0", "4ee2",
		"4ee2", "4ee6", "4ee7", "4eea", "4eef", "4ef8", "4efa", "4efc", "4efc",
		"4efe", "4efe", "4f03", "4f03", "4f06", "4f07", "4f0c", "4f0c", "4f16",
		"4f17", "4f1a", "4f1c", "4f1e", "4f21", "4f23", "4f2b", "4f2e", "4f2e",
		"4f31", "4f32", "4f35", "4f35", "4f37", "4f37", "4f39", "4f39", "4f40",
		"4f40", "4f42", "4f42", "4f44", "4f45", "4f4a", "4f4b", "4f65", "4f66",
		"4f68", "4f68", "4f6d", "4f6d", "4f71", "4f72", "4f8a", "4f8a", "4f8c",
		"4f8c", "4f8e", "4f8e", "4f93", "4f93", "4f99", "4f99", "4f9f", "4fad",
		"4fb0", "4fb1", "4fb4", "4fb4", "4fb8", "4fb8", "4fbc", "4fbe", "4fc6",
		"4fc6", "4fc8", "4fc8", "4fcc", "4fcc", "4fd2", "4fd2", "4fd5", "4fd5",
		"4fe2", "4feb", "4fed", "4fed", "4ff0", "4ff0", "4ff2", "4ff2", "4ff9",
		"4ff9", "4ffb", "4ffd", "4fff", "4fff", "5001", "5004", "5008", "5008",
		"500a", "500a", "5010", "5010", "501d", "501d", "5024", "5024", "502e",
		"502e", "5032", "5032", "5034", "5034", "5036", "5036", "5038", "503b",
		"503d", "503f", "5042", "5042", "5044", "5044", "5050", "5050", "5052",
		"5052", "5054", "5054", "5056", "5056", "5058", "5059", "5066", "5067",
		"506c", "506c", "5071", "5071", "5078", "5079", "507b", "507c", "507e",
		"507f", "5081", "5081", "5084", "5084", "5086", "5086", "5088", "508a",
		"508f", "5090", "5093", "5093", "5097", "5097", "509f", "50a1", "50a4",
		"50ab", "50b9", "50b9", "50bc", "50bc", "50c0", "50c0", "50c3", "50c3",
		"50cc", "50cd", "50d0", "50d0", "50d2", "50d2", "50d8", "50d9", "50dc",
		"50dc", "50de", "50df", "50e1", "50e2", "50eb", "50eb", "50f2", "50f2",
		"50f4", "50f4", "50f7", "50f7", "50fa", "50fa", "50fc", "50fc", "5101",
		"5101", "510d", "510f", "5116", "5116", "5119", "5119", "511b", "511b",
		"511d", "511e", "5123", "5123", "5127", "5128", "512b", "512c", "512f",
		"512f", "5136", "5136", "513e", "513e", "5142", "5142", "514a", "514a",
		"514e", "5151", "5153", "5153", "5156", "5156", "5158", "5158", "5160",
		"5160", "5164", "5164", "5166", "5166", "516a", "516a", "516f", "5170",
		"5172", "5174", "5179", "517b", "517d", "517f", "5181", "5186", "5188",
		"5188", "518b", "518c", "518e", "518e", "5190", "5190", "5196", "5196",
		"5199", "519d", "519f", "519f", "51a1", "51a1", "51a3", "51a3", "51a6",
		"51a9", "51ab", "51ab", "51ad", "51af", "51b2", "51b5", "51b8", "51b8",
		"51ba", "51bb", "51bf", "51c3", "51c7", "51c7", "51c9", "51c9", "51cf",
		"51cf", "51d1", "51d3", "51d5", "51d6", "51d9", "51db", "51df", "51df",
		"51e2", "51ef", "51f2", "51f2", "51f4", "51f4", "51f7", "51f7", "51fb",
		"51fc", "51fe", "51ff", "5202", "5202", "5204", "5205", "520b", "520b",
		"520d", "520d", "520f", "520f", "5214", "5215", "5218", "521b", "521f",
		"5220", "5222", "5223", "5226", "5227", "522b", "522d", "522f", "522f",
		"5234", "5234", "5239", "5239", "523c", "5240", "5242", "5242", "5245",
		"5245", "5248", "5248", "524f", "5251", "5253", "5253", "5257", "5259",
		"5260", "5260", "5263", "5268", "5270", "5271", "5273", "5273", "5276",
		"5276", "5279", "5279", "527e", "527e", "5285", "5286", "528e", "5290",
		"5292", "5292", "5294", "5295", "529a", "529a", "529c", "529e", "52a1",
		"52a2", "52a4", "52a5", "52a7", "52a8", "52af", "52ba", "52bd", "52bd",
		"52bf", "52bf", "52c4", "52c6", "52c8", "52c8", "52ca", "52cc", "52ce",
		"52d1", "52d4", "52d4", "52da", "52da", "52dc", "52dc", "52e0", "52e1",
		"52e5", "52e5", "52e7", "52e8", "52ea", "52ea", "52ec", "52ee", "52f2",
		"52f2", "52f6", "52f6", "52f9", "52f9", "52fd", "52fd", "5300", "5304",
		"5307", "5307", "530c", "530c", "5313", "5314", "5318", "5318", "531b",
		"531b", "531e", "531e", "5324", "5329", "532b", "532c", "532e", "532e",
		"5332", "5333", "5335", "5336", "5338", "5338", "533a", "533b", "5342",
		"5342", "5346", "5346", "534b", "534b", "534e", "5350", "5355", "5356",
		"5358", "5359", "535b", "535b", "535d", "535d", "535f", "535f", "5362",
		"5362", "5364", "5365", "5367", "536b", "536d", "536d", "5374", "5374",
		"5376", "5376", "537a", "537a", "537d", "537e", "5380", "5381", "5383",
		"5383", "5385", "5389", "538b", "538d", "5390", "5391", "5393", "5393",
		"5395", "5395", "539b", "539b", "53a0", "53a3", "53a6", "53a6", "53a8",
		"53ab", "53ae", "53b1", "53b3", "53b3", "53b5", "53b8", "53ba", "53ba",
		"53bc", "53c2", "53c4", "53c7", "53cc", "53cc", "53ce", "53d3", "53d5",
		"53d5", "53d8", "53da", "53dc", "53de", "53e0", "53e0", "53e7", "53e7",
		"53f4", "53f4", "53f6", "53f7", "53f9", "53fa", "53fd", "5400", "5402",
		"5402", "5405", "5405", "5413", "5417", "541a", "541a", "5421", "5423",
		"542f", "542f", "5432", "5432", "5434", "5434", "543a", "543a", "543f",
		"543f", "5444", "5444", "5449", "5449", "544b", "544d", "5450", "5453",
		"5455", "545f", "5469", "546a", "546d", "546e", "5479", "5479", "5483",
		"5483", "5485", "5485", "5489", "548a", "548f", "548f", "5493", "5494",
		"5497", "5497", "5499", "5499", "549b", "549f", "54a3", "54a4", "54b2",
		"54b2", "54b4", "54b5", "54b9", "54b9", "54ca", "54cd", "54d0", "54d5",
		"54d7", "54dd", "54df", "54df", "54e3", "54e3", "54ec", "54ec", "54ef",
		"54f0", "54f4", "54f6", "54f9", "54f9", "54fe", "54fe", "5500", "5500",
		"5502", "5502", "550d", "550d", "5513", "5513", "5515", "5516", "5518",
		"5519", "551b", "5525", "5528", "5529", "552b", "552b", "553a", "553a",
		"553d", "553d", "553f", "553f", "5542", "5542", "5547", "5547", "5549",
		"5549", "554c", "554c", "5553", "5554", "5558", "555b", "555d", "555d",
		"5560", "5560", "5567", "5569", "556b", "5574", "5578", "557a", "5585",
		"5586", "5590", "5590", "5596", "5597", "559b", "559b", "559e", "559e",
		"55a0", "55a0", "55a9", "55a9", "55af", "55b0", "55b4", "55b4", "55b6",
		"55ba", "55bc", "55be", "55c1", "55c1", "55d7", "55d8", "55de", "55de",
		"55e0", "55e0", "55ea", "55ee", "55f0", "55f1", "55f3", "55f5", "55f8",
		"55f8", "55fb", "55fb", "5603", "5603", "5605", "5605", "5607", "5607",
		"560a", "560b", "5611", "5611", "5618", "561a", "561e", "561e", "5620",
		"5626", "5628", "5628", "562b", "562b", "562d", "562d", "5631", "5631",
		"5637", "5637", "563c", "563c", "5643", "5644", "5647", "5647", "564b",
		"564b", "564d", "564d", "564f", "5652", "5654", "5656", "565b", "565d",
		"565f", "565f", "5661", "5661", "5667", "5667", "5675", "5675", "567a",
		"567d", "5688", "568b", "5691", "5692", "5694", "5694", "5696", "5696",
		"569b", "569b", "569e", "56a4", "56a9", "56a9", "56af", "56b1", "56b8",
		"56bb", "56bf", "56bf", "56c4", "56c4", "56c7", "56c7", "56ce", "56d0",
		"56d2", "56d2", "56d5", "56d6", "56d8", "56d9", "56dc", "56dc", "56e2",
		"56e3", "56e6", "56e6", "56e8", "56e9", "56ec", "56ed", "56ef", "56ef",
		"56f1", "56f6", "56f8", "56f8", "56fb", "56fe", "5700", "5700", "5705",
		"5706", "570e", "5711", "5715", "5715", "5717", "5717", "5719", "5719",
		"571d", "571d", "5721", "5721", "5724", "5727", "572b", "572b", "5731",
		"5732", "5735", "573a", "573c", "573d", "573f", "573f", "5742", "5744",
		"5746", "5746", "5748", "5748", "5753", "5760", "5763", "5763", "5765",
		"5765", "5767", "5767", "576c", "576c", "576e", "576e", "5778", "577a",
		"577e", "577f", "5781", "5781", "5784", "578a", "578d", "578e", "5790",
		"5792", "5796", "5796", "579c", "579c", "57a1", "57a1", "57a6", "57ad",
		"57af", "57b4", "57b7", "57b7", "57bb", "57bb", "57be", "57be", "57c0",
		"57c0", "57c4", "57c5", "57c8", "57ca", "57cd", "57cd", "57d1", "57d1",
		"57d3", "57d3", "57d6", "57db", "57dd", "57de", "57e6", "57e6", "57e8",
		"57e8", "57ea", "57eb", "57ef", "57ef", "57fe", "57ff", "5803", "5803",
		"580f", "580f", "5811", "5813", "5815", "5818", "581a", "581a", "581f",
		"581f", "5822", "5822", "5826", "5826", "582b", "582b", "583a", "583a",
		"583c", "583c", "583e", "583e", "5840", "5847", "5850", "5850", "5856",
		"5856", "585c", "585c", "585f", "5861", "5866", "5867", "5869", "586a",
		"586c", "586c", "586e", "586e", "5870", "5870", "5872", "5873", "5877",
		"5878", "5884", "5884", "588c", "588d", "5892", "5892", "5895", "5897",
		"5899", "589b", "58a2", "58a2", "58a4", "58a4", "58a7", "58a7", "58aa",
		"58aa", "58ad", "58ad", "58b0", "58b0", "58b2", "58b2", "58b4", "58b9",
		"58c0", "58c0", "58c3", "58c4", "58ca", "58cd", "58d0", "58d0", "58d7",
		"58d7", "58dc", "58dc", "58e0", "58e1", "58e5", "58e6", "58ea", "58ea",
		"58ed", "58ee", "58f0", "58f3", "58f5", "58f8", "58fb", "58fb", "5900",
		"5902", "5904", "5905", "5907", "590b", "5910", "5911", "5913", "5913",
		"5918", "5918", "591b", "591b", "591d", "591f", "5921", "5921", "5923",
		"5923", "5926", "5926", "5928", "5928", "5930", "5930", "5932", "5936",
		"5939", "593b", "593d", "593d", "593f", "593f", "5941", "5943", "5946",
		"5946", "594b", "594d", "5952", "5952", "5956", "5956", "5959", "5959",
		"595b", "595b", "595d", "595f", "5963", "5966", "5968", "5968", "596c",
		"596c", "596f", "596f", "5975", "5975", "597a", "597a", "5986", "5989",
		"598b", "598c", "5991", "5991", "5994", "5995", "599a", "599c", "599f",
		"599f", "59a9", "59ad", "59b0", "59b0", "59b7", "59b8", "59bf", "59bf",
		"59c2", "59c2", "59c4", "59c4", "59c9", "59c9", "59d5", "59d5", "59d7",
		"59d7", "59d9", "59d9", "59df", "59df", "59e2", "59e2", "59e7", "59e7",
		"59eb", "59eb", "59ef", "59f0", "59f8", "59f9", "5a02", "5a02", "5a04",
		"5a08", "5a0b", "5a0b", "5a0d", "5a0e", "5a10", "5a10", "5a12", "5a12",
		"5a14", "5a14", "5a1a", "5a1a", "5a1d", "5a1d", "5a21", "5a22", "5a24",
		"5a24", "5a26", "5a28", "5a2a", "5a2c", "5a2f", "5a32", "5a34", "5a34",
		"5a3a", "5a3b", "5a3d", "5a3d", "5a3f", "5a3f", "5a45", "5a45", "5a4b",
		"5a4b", "5a4e", "5a4f", "5a54", "5a54", "5a59", "5a59", "5a61", "5a61",
		"5a63", "5a63", "5a68", "5a68", "5a6b", "5a6b", "5a6e", "5a6f", "5a71",
		"5a76", "5a79", "5a79", "5a7e", "5a7e", "5a80", "5a82", "5a85", "5a89",
		"5a8d", "5a8d", "5a91", "5a91", "5a96", "5a96", "5a98", "5a99", "5aa0",
		"5aa1", "5aa3", "5aa4", "5aa8", "5aa8", "5aaa", "5aab", "5aad", "5aad",
		"5ac3", "5ac3", "5ac5", "5ac5", "5ace", "5ad4", "5ae4", "5ae4", "5ae7",
		"5ae7", "5aef", "5af2", "5afc", "5afc", "5afe", "5afe", "5b00", "5b00",
		"5b04", "5b04", "5b06", "5b06", "5b0a", "5b0a", "5b0d", "5b0e", "5b11",
		"5b12", "5b15", "5b15", "5b18", "5b18", "5b1c", "5b1c", "5b1f", "5b1f",
		"5b22", "5b22", "5b29", "5b29", "5b2b", "5b2b", "5b31", "5b31", "5b33",
		"5b33", "5b35", "5b37", "5b39", "5b3b", "5b41", "5b42", "5b44", "5b44",
		"5b46", "5b46", "5b49", "5b4a", "5b4f", "5b4f", "5b52", "5b52", "5b59",
		"5b59", "5b5e", "5b5e", "5b60", "5b61", "5b66", "5b68", "5b6a", "5b6a",
		"5b6d", "5b6d", "5b6f", "5b6f", "5b74", "5b74", "5b76", "5b76", "5b79",
		"5b79", "5b7c", "5b7c", "5b7e", "5b7e", "5b80", "5b80", "5b82", "5b82",
		"5b86", "5b86", "5b8a", "5b8a", "5b8d", "5b8d", "5b90", "5b91", "5b94",
		"5b94", "5b96", "5b96", "5b9d", "5ba1", "5ba9", "5bab", "5baf", "5baf",
		"5bb1", "5bb2", "5bb7", "5bb7", "5bba", "5bbe", "5bc3", "5bc3", "5bc8",
		"5bc9", "5bcf", "5bcf", "5bd5", "5bd5", "5bd7", "5bd7", "5bda", "5bdd",
		"5bed", "5bed", "5bf3", "5bf4", "5bf7", "5bf7", "5bf9", "5bf9", "5bfb",
		"5c00", "5c02", "5c02", "5c05", "5c06", "5c13", "5c14", "5c17", "5c19",
		"5c1b", "5c1e", "5c20", "5c21", "5c23", "5c23", "5c26", "5c27", "5c29",
		"5c29", "5c2b", "5c2b", "5c2d", "5c2f", "5c32", "5c32", "5c34", "5c36",
		"5c3d", "5c3d", "5c42", "5c43", "5c49", "5c4a", "5c52", "5c53", "5c57",
		"5c57", "5c5a", "5c5b", "5c5e", "5c5f", "5c61", "5c61", "5c66", "5c66",
		"5c6b", "5c6b", "5c70", "5c70", "5c72", "5c72", "5c75", "5c78", "5c7d",
		"5c7d", "5c7f", "5c85", "5c87", "5c87", "5c8e", "5c8e", "5c96", "5c9c",
		"5c9e", "5c9e", "5cb2", "5cb2", "5cb4", "5cb4", "5cb9", "5cc5", "5ccd",
		"5ccd", "5cd1", "5cd1", "5cd5", "5cd5", "5cdc", "5cdd", "5ce0", "5ce7",
		"5ce9", "5ce9", "5ceb", "5ceb", "5cef", "5cef", "5cf2", "5cf3", "5cf5",
		"5cf5", "5cfa", "5cfa", "5cfc", "5cfc", "5cfe", "5cfe", "5d02", "5d05",
		"5d08", "5d0a", "5d10", "5d10", "5d13", "5d13", "5d15", "5d15", "5d18",
		"5d18", "5d1c", "5d1c", "5d21", "5d21", "5d2a", "5d2d", "5d2f", "5d2f",
		"5d3b", "5d3b", "5d3e", "5d3e", "5d44", "5d44", "5d46", "5d46", "5d48",
		"5d48", "5d4d", "5d4d", "5d4f", "5d4f", "5d53", "5d54", "5d56", "5d58",
		"5d5a", "5d5d", "5d5f", "5d61", "5d64", "5d64", "5d66", "5d66", "5d6a",
		"5d6a", "5d6d", "5d6e", "5d70", "5d70", "5d73", "5d76", "5d78", "5d78",
		"5d7b", "5d7b", "5d83", "5d83", "5d85", "5d85", "5d8b", "5d8c", "5d8e",
		"5d91", "5d96", "5d96", "5d98", "5d98", "5d9b", "5d9b", "5da3", "5da6",
		"5dab", "5dab", "5db3", "5db3", "5db6", "5db6", "5db9", "5db9", "5dbb",
		"5dbb", "5dbe", "5dbf", "5dc1", "5dc1", "5dc4", "5dc5", "5dc8", "5dc8",
		"5dca", "5dca", "5dcc", "5dcc", "5dce", "5dce", "5dd0", "5dd0", "5dd3",
		"5dd3", "5dd7", "5dd7", "5dd9", "5ddc", "5de3", "5de4", "5de9", "5dea",
		"5dec", "5ded", "5def", "5def", "5df5", "5df6", "5df8", "5df8", "5dfa",
		"5dfc", "5e00", "5e01", "5e05", "5e05", "5e07", "5e09", "5e0b", "5e0b",
		"5e0d", "5e0d", "5e0f", "5e10", "5e12", "5e13", "5e1c", "5e1c", "5e1e",
		"5e1e", "5e26", "5e27", "5e2a", "5e2a", "5e2c", "5e2c", "5e2e", "5e32",
		"5e35", "5e35", "5e39", "5e3c", "5e3f", "5e3f", "5e42", "5e42", "5e46",
		"5e49", "5e50", "5e52", "5e56", "5e56", "5e5a", "5e5a", "5e5e", "5e5e",
		"5e64", "5e65", "5e71", "5e71", "5e77", "5e77", "5e7a", "5e7a", "5e7f",
		"5e7f", "5e81", "5e81", "5e83", "5e83", "5e85", "5e86", "5e8e", "5e8e",
		"5e90", "5e94", "5e98", "5e99", "5e9d", "5e9f", "5ea1", "5ea1", "5ea9",
		"5ea9", "5eaf", "5eaf", "5eba", "5ebd", "5ebf", "5ec0", "5ec3", "5ec3",
		"5ecd", "5ecd", "5ecf", "5ed0", "5ee4", "5ee4", "5eea", "5eeb", "5eed",
		"5eed", "5ef0", "5ef0", "5ef4", "5ef5", "5ef8", "5ef9", "5efb", "5efd",
		"5f00", "5f00", "5f03", "5f03", "5f06", "5f06", "5f09", "5f09", "5f0c",
		"5f0e", "5f10", "5f11", "5f16", "5f16", "5f19", "5f19", "5f1c", "5f1c",
		"5f1e", "5f1e", "5f20", "5f21", "5f25", "5f25", "5f2a", "5f2c", "5f2f",
		"5f2f", "5f32", "5f32", "5f34", "5f34", "5f39", "5f3b", "5f3d", "5f3f",
		"5f41", "5f42", "5f45", "5f45", "5f47", "5f47", "5f4d", "5f4d", "5f50",
		"5f53", "5f55", "5f55", "5f5a", "5f5c", "5f5e", "5f61", "5f63", "5f63",
		"5f66", "5f66", "5f68", "5f68", "5f6e", "5f6e", "5f72", "5f72", "5f75",
		"5f75", "5f7a", "5f7b", "5f83", "5f84", "5f8d", "5f8f", "5f93", "5f95",
		"5f9a", "5f9a", "5f9d", "5f9d", "5fa2", "5fa4", "5fa7", "5fa7", "5fb0",
		"5fb1", "5fb3", "5fb4", "5fb8", "5fb8", "5fba", "5fba", "5fc2", "5fc2",
		"5fc4", "5fc4", "5fc6", "5fc8", "5fca", "5fcb", "5fce", "5fce", "5fd3",
		"5fd3", "5fda", "5fdc", "5fdf", "5fdf", "5fe2", "5fe2", "5fe6", "5fe7",
		"5fe9", "5fe9", "5fec", "5fec", "5ff0", "5ff0", "5ff2", "5ff2", "5ff6",
		"5ff6", "5ff9", "5ff9", "5ffc", "5ffc", "5ffe", "5ffe", "6001", "6008",
		"6018", "6018", "601f", "601f", "6023", "6023", "6030", "6031", "6036",
		"6036", "6038", "6038", "603a", "603f", "6048", "6048", "604a", "604b",
		"604e", "604f", "6051", "6051", "6056", "6057", "605c", "605c", "6060",
		"6061", "6071", "6071", "6073", "607e", "6082", "6082", "608b", "608b",
		"608f", "608f", "6091", "6091", "6093", "6093", "6098", "6099", "609e",
		"609e", "60a1", "60a1", "60a4", "60a7", "60a9", "60af", "60b3", "60b3",
		"60c2", "60c2", "60d0", "60d0", "60d2", "60d2", "60d6", "60d7", "60de",
		"60de", "60e3", "60e3", "60e5", "60e5", "60e7", "60ef", "60fd", "60fd",
		"6102", "6102", "6107", "6107", "610c", "610c", "6111", "6111", "6117",
		"6117", "6119", "6119", "611e", "611e", "6120", "6122", "6124", "6126",
		"612a", "612a", "612d", "612d", "6130", "6131", "6133", "6133", "6135",
		"6135", "6138", "613a", "613c", "613d", "6142", "6143", "6150", "6151",
		"6157", "6157", "6159", "6159", "615c", "615c", "6160", "6160", "6164",
		"6164", "6169", "6169", "616d", "616d", "616f", "616f", "6178", "6178",
		"617b", "617b", "617d", "617d", "617f", "617f", "6181", "6181", "6184",
		"6188", "618f", "618f", "6195", "6195", "6197", "6199", "619c", "619c",
		"619e", "619e", "61a0", "61a0", "61a3", "61a3", "61a5", "61a6", "61b7",
		"61b7", "61b9", "61b9", "61bb", "61bb", "61bd", "61bd", "61c0", "61c0",
		"61c4", "61c4", "61ce", "61d5", "61d7", "61d7", "61d9", "61dd", "61e1",
		"61e2", "61ec", "61ec", "61ef", "61ef", "61f3", "61f4", "6202", "6202",
		"6205", "6206", "620b", "620b", "620f", "620f", "6213", "6213", "6217",
		"6218", "621c", "621e", "6226", "6226", "6228", "6228", "622c", "622c",
		"622f", "622f", "6231", "6231", "6235", "6235", "6237", "6239", "623b",
		"623c", "6244", "6245", "624c", "624c", "624f", "624f", "6255", "6257",
		"625d", "625d", "625f", "625f", "6267", "626c", "6275", "6275", "6278",
		"6278", "6282", "6282", "6285", "6285", "628b", "628b", "628d", "628d",
		"6290", "6290", "6299", "62a7", "62b2", "62b2", "62b7", "62b7", "62ba",
		"62ba", "62c0", "62c1", "62c3", "62c3", "62c5", "62c5", "62d5", "62d5",
		"62dd", "62ea", "6304", "6306", "630a", "630a", "6312", "6312", "6317",
		"6327", "632e", "632e", "6330", "6331", "6335", "6335", "6337", "6337",
		"633f", "633f", "6352", "6353", "635b", "6364", "6366", "6366", "636a",
		"636a", "636c", "636c", "6373", "6374", "6379", "6379", "637e", "637f",
		"6386", "6386", "638b", "638b", "6393", "6393", "6395", "6395", "639a",
		"639a", "63a6", "63a6", "63b2", "63bc", "63bf", "63bf", "63c1", "63c1",
		"63d1", "63d1", "63d4", "63d4", "63de", "63de", "63e2", "63e2", "63e6",
		"63e6", "63ec", "63ec", "63f7", "63f8", "63fa", "6405", "6407", "6408",
		"6411", "6411", "6419", "6419", "641d", "641d", "6429", "6429", "6431",
		"6432", "6438", "6438", "643a", "643c", "6442", "6442", "6444", "644a",
		"644c", "644c", "644f", "644f", "6455", "6457", "645a", "645a", "6462",
		"6464", "646a", "646a", "6471", "6471", "647c", "647c", "647e", "647e",
		"6480", "6481", "6483", "6484", "6486", "6486", "648d", "648e", "6491",
		"6491", "6494", "6494", "649b", "649b", "64a1", "64a1", "64a7", "64a8",
		"64aa", "64aa", "64af", "64af", "64b4", "64ba", "64c0", "64c0", "64c6",
		"64c6", "64c8", "64c8", "64cc", "64cc", "64d1", "64d1", "64d3", "64d3",
		"64d5", "64d5", "64dc", "64df", "64e1", "64e1", "64e5", "64e5", "64e7",
		"64e7", "64ea", "64ea", "64ee", "64ee", "64f5", "64f6", "64f9", "64f9",
		"6502", "6502", "6505", "6505", "6508", "6508", "650a", "650b", "6511",
		"6512", "651a", "651a", "651e", "651f", "6527", "6528", "6530", "6531",
		"6534", "6535", "653a", "653a", "653c", "653c", "6540", "6540", "6542",
		"6542", "6544", "6544", "6547", "6547", "654b", "654e", "6550", "6550",
		"6552", "6552", "655a", "655b", "655f", "6561", "6569", "6569", "656b",
		"656b", "656d", "656e", "6570", "6571", "657d", "657e", "6585", "6586",
		"6588", "658b", "658d", "658f", "6593", "6593", "6598", "6598", "659a",
		"659a", "65a3", "65a3", "65a6", "65a6", "65a9", "65a9", "65ad", "65ad",
		"65b1", "65b1", "65b4", "65b5", "65ba", "65ba", "65be", "65be", "65c0",
		"65c0", "65c7", "65ca", "65d1", "65d1", "65d4", "65d5", "65d8", "65d9",
		"65dc", "65dc", "65e0", "65e0", "65e3", "65e4", "65e7", "65e7", "65ea",
		"65eb", "65f6", "65f9", "65fe", "65ff", "6601", "6601", "6616", "661b",
		"661e", "661e", "6623", "6623", "6629", "662a", "662c", "662c", "6630",
		"6630", "6637", "6638", "663b", "6640", "6644", "6644", "6646", "6646",
		"6648", "6648", "664b", "664b", "664d", "664e", "6650", "6650", "6653",
		"6658", "6660", "6660", "6663", "6663", "6667", "6667", "6669", "6669",
		"666b", "666b", "666d", "666d", "6673", "6673", "6675", "6675", "667d",
		"667d", "667f", "667f", "6681", "6683", "6685", "6685", "668e", "668f",
		"6692", "6693", "669a", "669c", "669e", "669e", "66a3", "66a7", "66ac",
		"66ad", "66b3", "66b3", "66b6", "66b6", "66bc", "66bc", "66bf", "66bf",
		"66c1", "66c3", "66c5", "66c5", "66cd", "66ce", "66d0", "66d1", "66d3",
		"66d5", "66d7", "66d7", "66df", "66df", "66e1", "66e2", "66e5", "66e5",
		"66e7", "66e7", "66ea", "66ea", "66ef", "66ef", "66f1", "66f1", "66f5",
		"66f5", "66fa", "66fb", "66fd", "66fd", "6702", "6702", "6706", "6707",
		"670c", "670c", "670e", "670e", "6711", "6711", "6716", "6716", "6719",
		"671a", "671c", "671c", "671e", "671e", "6724", "6725", "6729", "6729",
		"672f", "6730", "6732", "6732", "6736", "6737", "6740", "6744", "674a",
		"674a", "6752", "6752", "6754", "6754", "6758", "6758", "675b", "675b",
		"6761", "6769", "676b", "676b", "676e", "676e", "6780", "6780", "6782",
		"6782", "6788", "6788", "678a", "678a", "678f", "678f", "6796", "6796",
		"679b", "679b", "679e", "679e", "67a0", "67ad", "67b1", "67b1", "67bc",
		"67bf", "67c7", "67c7", "67d5", "67d7", "67e0", "67e1", "67e8", "67e8",
		"67f9", "67f9", "67fb", "67fb", "67fd", "67fe", "6800", "6811", "6815",
		"6815", "6819", "6819", "681b", "681b", "681e", "681e", "6822", "6824",
		"6827", "6827", "682c", "682c", "6830", "6830", "6836", "6837", "683e",
		"683f", "6847", "6847", "684a", "684a", "6852", "6852", "6855", "686a",
		"686c", "686c", "6870", "6870", "6873", "6873", "687a", "687a", "6884",
		"6884", "6888", "6888", "688d", "688e", "6895", "6895", "6898", "689a",
		"689e", "689e", "68a5", "68a6", "68b6", "68c3", "68c5", "68c5", "68ca",
		"68ca", "68cf", "68cf", "68d9", "68d9", "68db", "68db", "68e2", "68e2",
		"68e5", "68e5", "68ed", "68ed", "68fe", "6903", "6909", "6909", "6916",
		"6916", "6918", "6924", "6926", "6929", "692b", "692e", "6931", "6931",
		"6936", "6936", "693a", "693a", "693e", "693e", "6943", "6943", "6946",
		"6947", "694d", "694d", "6950", "6950", "6955", "6955", "6961", "6961",
		"6964", "6964", "6967", "6967", "6972", "6973", "697c", "6981", "6984",
		"6985", "6987", "698c", "698f", "698f", "6992", "6992", "6998", "6998",
		"699d", "699d", "699f", "699f", "69a2", "69a2", "69b2", "69b2", "69b8",
		"69b8", "69ba", "69ba", "69c0", "69c0", "69c5", "69c5", "69c7", "69c8",
		"69d1", "69d2", "69d5", "69d8", "69da", "69e1", "69e3", "69e3", "69e9",
		"69ea", "69ef", "69f0", "69f5", "69f5", "69f9", "69fa", "6a03", "6a03",
		"6a0b", "6a0c", "6a0e", "6a0e", "6a10", "6a10", "6a12", "6a12", "6a1a",
		"6a1a", "6a1c", "6a1c", "6a22", "6a22", "6a24", "6a24", "6a29", "6a31",
		"6a33", "6a33", "6a36", "6a37", "6a42", "6a43", "6a45", "6a45", "6a4a",
		"6a4a", "6a4c", "6a4c", "6a52", "6a53", "6a57", "6a57", "6a5c", "6a5c",
		"6a63", "6a63", "6a65", "6a65", "6a6c", "6a6c", "6a6e", "6a6e", "6a70",
		"6a75", "6a77", "6a7d", "6a82", "6a82", "6a86", "6a86", "6a88", "6a88",
		"6a8a", "6a8b", "6a8f", "6a8f", "6a98", "6a99", "6a9d", "6a9d", "6aa7",
		"6aa7", "6aa9", "6aab", "6ab0", "6ab2", "6ab5", "6ab5", "6abc", "6abc",
		"6abe", "6ac1", "6ac4", "6ac4", "6ac8", "6aca", "6ace", "6ace", "6ad2",
		"6ad2", "6ad4", "6ad8", "6ae2", "6ae4", "6ae6", "6ae6", "6ae9", "6ae9",
		"6aed", "6aed", "6af2", "6af2", "6af4", "6af7", "6afd", "6aff", "6b01",
		"6b01", "6b05", "6b07", "6b0c", "6b0e", "6b14", "6b15", "6b1b", "6b1d",
		"6b1f", "6b1f", "6b22", "6b22", "6b24", "6b24", "6b26", "6b27", "6b29",
		"6b2b", "6b2e", "6b2e", "6b30", "6b30", "6b35", "6b35", "6b40", "6b40",
		"6b44", "6b44", "6b4f", "6b4f", "6b52", "6b53", "6b57", "6b58", "6b5a",
		"6b5a", "6b5d", "6b5d", "6b68", "6b69", "6b6b", "6b6c", "6b6e", "6b71",
		"6b73", "6b75", "6b7a", "6b7a", "6b7c", "6b7d", "6b81", "6b81", "6b85",
		"6b85", "6b87", "6b87", "6b8b", "6b8b", "6b90", "6b90", "6b92", "6b93",
		"6b9a", "6b9a", "6b9c", "6b9d", "6ba1", "6ba1", "6ba8", "6ba9", "6bac",
		"6bac", "6bb1", "6bb1", "6bb4", "6bb4", "6bb8", "6bb9", "6bbb", "6bbb",
		"6bbe", "6bbe", "6bc1", "6bc2", "6bce", "6bce", "6bd1", "6bd1", "6bd5",
		"6bd5", "6bd9", "6bd9", "6bdc", "6bdd", "6bdf", "6bdf", "6be1", "6be1",
		"6be5", "6be5", "6be9", "6bea", "6bed", "6bee", "6bf1", "6bf1", "6bf4",
		"6bf6", "6bfa", "6bfa", "6c07", "6c07", "6c0a", "6c0a", "6c0e", "6c0e",
		"6c12", "6c12", "6c17", "6c17", "6c1c", "6c1c", "6c1e", "6c1e", "6c22",
		"6c22", "6c29", "6c29", "6c2d", "6c2d", "6c31", "6c32", "6c35", "6c35",
		"6c37", "6c37", "6c39", "6c3a", "6c3c", "6c3d", "6c44", "6c45", "6c47",
		"6c49", "6c51", "6c51", "6c53", "6c53", "6c56", "6c56", "6c58", "6c58",
		"6c5a", "6c5a", "6c62", "6c64", "6c6c", "6c6c", "6c6e", "6c6e", "6c75",
		"6c75", "6c77", "6c77", "6c79", "6c79", "6c7c", "6c7c", "6c7f", "6c7f",
		"6c91", "6c91", "6c97", "6c97", "6c9e", "6caa", "6caf", "6caf", "6cb2",
		"6cb2", "6cb5", "6cb5", "6cc8", "6cc8", "6ccb", "6ccb", "6cce", "6cce",
		"6cd8", "6cd8", "6cdf", "6cdf", "6ce4", "6ce4", "6ce6", "6ce6", "6cea",
		"6cea", "6cf4", "6cf4", "6cf6", "6cf8", "6cfa", "6cff", "6d02", "6d02",
		"6d05", "6d06", "6d13", "6d15", "6d1c", "6d1c", "6d21", "6d21", "6d23",
		"6d24", "6d26", "6d26", "6d43", "6d57", "6d5b", "6d5d", "6d6b", "6d6b",
		"6d71", "6d73", "6d81", "6d81", "6d8f", "6d8f", "6d96", "6d96", "6d99",
		"6da9", "6dad", "6dad", "6db0", "6db1", "6db6", "6db6", "6db9", "6db9",
		"6dc1", "6dc1", "6dc3", "6dc3", "6dce", "6dce", "6de7", "6de7", "6df8",
		"6df8", "6dfe", "6dff", "6e01", "6e02", "6e04", "6e04", "6e06", "6e18",
		"6e1e", "6e1e", "6e29", "6e2a", "6e37", "6e37", "6e42", "6e42", "6e48",
		"6e48", "6e4c", "6e4c", "6e4f", "6e50", "6e57", "6e57", "6e59", "6e59",
		"6e6a", "6e6a", "6e6c", "6e6d", "6e70", "6e70", "6e75", "6e76", "6e7a",
		"6e87", "6e8a", "6e8c", "6e91", "6e91", "6e95", "6e95", "6e9a", "6e9a",
		"6ea8", "6ea9", "6eac", "6ead", "6eb5", "6eb5", "6eb8", "6eb8", "6ebb",
		"6ebb", "6ed7", "6ed7", "6ed9", "6edb", "6edd", "6eea", "6ef0", "6ef0",
		"6ef3", "6ef3", "6efa", "6efa", "6f04", "6f04", "6f0b", "6f0c", "6f10",
		"6f11", "6f16", "6f17", "6f1b", "6f1b", "6f1d", "6f1d", "6f24", "6f24",
		"6f28", "6f28", "6f34", "6f34", "6f3d", "6f3d", "6f42", "6f42", "6f44",
		"6f4d", "6f56", "6f56", "6f59", "6f59", "6f5c", "6f5c", "6f65", "6f65",
		"6f68", "6f68", "6f71", "6f71", "6f74", "6f75", "6f79", "6f79", "6f81",
		"6f81", "6f83", "6f83", "6f8a", "6f8a", "6f8f", "6f8f", "6f91", "6f91",
		"6f98", "6f9d", "6f9f", "6f9f", "6fb5", "6fb5", "6fb7", "6fb7", "6fbb",
		"6fbb", "6fbe", "6fbe", "6fc5", "6fc5", "6fd0", "6fd3", "6fd6", "6fd7",
		"6fd9", "6fda", "6fe5", "6fe5", "6fea", "6fea", "6ff3", "6ff3", "6ff5",
		"6ff6", "6ff8", "6ff9", "6ffd", "6ffd", "7002", "7003", "7008", "7008",
		"7010", "7010", "7012", "7013", "701e", "701e", "7025", "7025", "702c",
		"702e", "7036", "7036", "703d", "703d", "7047", "7047", "704b", "704b",
		"704d", "7050", "7053", "7054", "7059", "7059", "705c", "705c", "7067",
		"7067", "706c", "706f", "7072", "7073", "7075", "7075", "7077", "7077",
		"7079", "7079", "707b", "707b", "707e", "7081", "7087", "7089", "708b",
		"708d", "708f", "7090", "7097", "7097", "709b", "709e", "70a0", "70a0",
		"70a2", "70a3", "70a5", "70a8", "70aa", "70aa", "70b2", "70b2", "70b6",
		"70b6", "70b9", "70b9", "70bb", "70bd", "70bf", "70c4", "70c9", "70c9",
		"70cc", "70cc", "70d0", "70d0", "70d5", "70d6", "70db", "70db", "70df",
		"70df", "70e3", "70e3", "70e5", "70ee", "70f1", "70f2", "70f5", "70f5",
		"70fe", "70fe", "7101", "7101", "7103", "7103", "7105", "7105", "7107",
		"7108", "710f", "710f", "7111", "7112", "7114", "7116", "7118", "7118",
		"711d", "711d", "7124", "7124", "7127", "7127", "7129", "712d", "7133",
		"7135", "7137", "7139", "713b", "7140", "7145", "7145", "7148", "7148",
		"714a", "714a", "714f", "714f", "7151", "7151", "7155", "7155", "7157",
		"7157", "715b", "715b", "716b", "716b", "716d", "716d", "716f", "716f",
		"7171", "7171", "7173", "7177", "7179", "717a", "717c", "717c", "717e",
		"717f", "7183", "7183", "7188", "7188", "718b", "718e", "7191", "7191",
		"7193", "7193", "7195", "7196", "7198", "7198", "71a2", "71a3", "71a6",
		"71a6", "71ab", "71ab", "71ad", "71ae", "71b4", "71b4", "71b6", "71b7",
		"71ba", "71bb", "71cc", "71cd", "71d1", "71d1", "71d3", "71d3", "71d7",
		"71d7", "71dd", "71de", "71e3", "71e3", "71e9", "71eb", "71ef", "71ef",
		"71f3", "71f3", "71f5", "71f7", "71fa", "71fa", "7200", "7200", "7204",
		"7204", "7208", "7209", "720b", "720b", "720e", "720f", "7211", "7212",
		"7215", "7218", "721c", "721c", "7220", "7221", "7224", "7225", "722b",
		"722b", "722e", "722f", "7231", "7234", "7237", "7237", "723c", "723c",
		"7240", "7240", "7243", "7243", "7245", "7245", "724d", "724e", "7250",
		"7251", "7254", "7255", "7257", "7257", "725c", "725c", "7264", "7266",
		"7268", "7268", "726b", "726b", "726d", "726d", "7271", "7271", "7275",
		"7275", "727a", "727a", "7282", "7283", "7287", "7287", "728a", "728a",
		"728f", "728f", "7294", "7294", "7299", "7299", "729c", "729c", "729f",
		"72a0", "72ab", "72ab", "72ad", "72ad", "72b1", "72b3", "72b6", "72b9",
		"72bb", "72bc", "72be", "72be", "72c7", "72c8", "72cd", "72cd", "72cf",
		"72cf", "72d3", "72d3", "72d5", "72d5", "72db", "72db", "72dd", "72de",
		"72e2", "72e2", "72e5", "72e5", "72e7", "72e7", "72ec", "72f2", "72f5",
		"72f5", "7302", "7306", "7309", "7309", "730d", "730e", "7310", "7310",
		"7314", "7315", "731a", "731a", "731f", "7321", "7324", "7324", "7328",
		"7328", "732a", "732c", "732e", "732f", "7338", "7339", "733d", "733d",
		"7341", "7341", "7346", "7348", "734b", "734b", "734f", "734f", "7353",
		"7356", "735c", "735c", "7363", "7364", "736d", "736d", "7371", "7371",
		"7374", "7374", "7379", "7379", "738c", "738d", "738f", "7391", "7398",
		"739c", "739e", "739e", "73a3", "73a3", "73a7", "73a7", "73aa", "73aa",
		"73ae", "73b1", "73ba", "73ba", "73bd", "73bd", "73c1", "73c1", "73c4",
		"73c4", "73c9", "73c9", "73ce", "73d1", "73d5", "73d5", "73df", "73df",
		"73e1", "73e2", "73e4", "73e4", "73e6", "73e6", "73ec", "73ec", "73ef",
		"73f3", "73f7", "73f7", "73f9", "73f9", "73fb", "73fb", "7402", "7402",
		"740e", "7415", "7417", "7419", "741c", "741c", "741e", "741f", "7427",
		"7427", "7437", "7439", "743b", "743e", "7443", "7443", "7445", "7445",
		"7447", "7449", "744c", "744c", "7453", "7453", "7456", "7456", "7458",
		"7458", "745d", "745d", "7460", "7461", "7465", "7466", "7468", "7468",
		"746b", "746c", "7474", "7474", "7476", "7478", "747a", "747b", "7482",
		"7482", "7484", "7484", "748c", "748f", "7491", "7491", "7493", "7493",
		"7496", "7496", "7499", "7499", "749b", "749b", "749d", "749d", "74a2",
		"74a2", "74a4", "74a4", "74ac", "74ac", "74ae", "74ae", "74b3", "74b4",
		"74b9", "74b9", "74bc", "74bc", "74c4", "74c4", "74c6", "74c9", "74cc",
		"74ce", "74d0", "74d3", "74e7", "74e7", "74ea", "74eb", "74ed", "74ed",
		"74ef", "74f3", "74f8", "74fa", "74fc", "74fc", "7501", "7501", "7505",
		"7506", "7509", "750a", "750e", "750e", "7519", "7519", "751b", "751b",
		"751e", "751e", "7520", "7520", "7523", "7524", "7527", "7527", "7534",
		"7536", "753b", "753c", "7541", "7546", "7549", "754a", "754d", "754d",
		"7550", "7553", "7555", "7558", "755e", "755e", "7560", "7561", "7567",
		"7569", "756d", "756e", "7571", "7575", "757a", "757c", "7581", "7583",
		"7585", "7585", "7588", "7589", "758d", "758e", "7592", "7593", "7596",
		"7597", "759b", "759c", "759e", "75a1", "75a6", "75a6", "75a8", "75a9",
		"75ac", "75af", "75b1", "75b1", "75b4", "75b4", "75b7", "75b7", "75c3",
		"75c3", "75c6", "75c6", "75c8", "75c9", "75d3", "75d3", "75d6", "75d6",
		"75dc", "75dc", "75e5", "75e5", "75e8", "75ec", "75ee", "75ee", "7602",
		"7602", "7604", "7607", "760e", "760e", "7612", "7612", "7617", "7618",
		"762a", "762c", "762e", "762e", "7636", "7637", "7639", "7639", "763b",
		"763b", "763e", "7641", "7644", "7645", "764a", "764b", "764d", "764f",
		"7651", "7651", "7654", "7655", "765b", "765b", "765d", "765e", "7663",
		"7663", "7666", "7668", "766b", "766b", "766f", "766f", "7673", "7674",
		"7676", "7677", "767a", "767a", "7680", "7680", "7683", "7683", "7685",
		"7685", "768c", "768d", "7690", "7691", "7694", "7694", "7697", "7698",
		"769f", "76a3", "76a5", "76a5", "76a7", "76a9", "76ac", "76ac", "76b1",
		"76b3", "76b6", "76b7", "76b9", "76b9", "76bc", "76bc", "76c0", "76c1",
		"76c7", "76c7", "76cb", "76cc", "76cf", "76d1", "76d5", "76d9", "76e0",
		"76e0", "76e2", "76e2", "76e8", "76e8", "76eb", "76eb", "76f6", "76f6",
		"76fd", "76fd", "76ff", "7700", "7702", "7702", "7706", "7706", "770c",
		"770f", "7714", "7714", "7716", "7718", "771c", "771c", "771e", "771e",
		"7721", "7721", "7724", "7724", "7726", "7726", "772a", "772c", "772e",
		"772e", "7730", "7730", "773f", "7743", "7748", "7749", "7750", "7751",
		"7753", "7753", "7757", "7758", "775d", "775d", "7764", "7764", "7770",
		"7778", "777a", "777b", "7786", "7786", "778a", "778a", "7790", "7790",
		"7792", "7794", "7796", "7796", "7798", "7798", "77a4", "77a4", "77a6",
		"77a6", "77a9", "77a9", "77ae", "77af", "77b8", "77b9", "77be", "77be",
		"77c0", "77c1", "77c3", "77c3", "77c5", "77c6", "77c8", "77c8", "77cb",
		"77cb", "77d1", "77d2", "77d6", "77d6", "77dd", "77dd", "77df", "77df",
		"77e1", "77e1", "77e4", "77e4", "77e6", "77e6", "77ea", "77eb", "77f4",
		"77f6", "77fe", "7801", "7804", "7804", "7807", "7808", "780a", "780b",
		"7815", "781c", "781e", "781e", "7824", "7824", "7836", "7836", "7839",
		"7842", "7844", "7844", "7846", "7847", "784b", "784b", "784f", "784f",
		"7851", "7851", "7853", "785b", "785f", "785f", "7861", "7861", "7863",
		"7863", "7866", "7867", "7872", "7878", "787a", "787a", "787d", "787d",
		"7882", "7882", "7888", "7888", "788a", "788b", "788d", "788d", "7890",
		"7890", "7892", "7892", "789b", "789d", "78a6", "78a6", "78ae", "78af",
		"78b1", "78b1", "78b5", "78b9", "78bd", "78bd", "78bf", "78c0", "78c2",
		"78c2", "78c6", "78c7", "78d2", "78d3", "78d6", "78d9", "78dc", "78dc",
		"78e4", "78e4", "78e6", "78e6", "78eb", "78eb", "78ee", "78ee", "78f0",
		"78f1", "78f5", "78f6", "78f8", "78f8", "7900", "7900", "7903", "7903",
		"7906", "7908", "790a", "790b", "790d", "790d", "790f", "790f", "7915",
		"7916", "7918", "7918", "791a", "791a", "791f", "7920", "7922", "7922",
		"792e", "792e", "7930", "7930", "7932", "7934", "7936", "7937", "793b",
		"793c", "7943", "7943", "794d", "794e", "7958", "7959", "7962", "7962",
		"7966", "7966", "796c", "796c", "796e", "796f", "7971", "7971", "7975",
		"7978", "797b", "797b", "797e", "797e", "7980", "7980", "7983", "7987",
		"7989", "7989", "798c", "798c", "7991", "7991", "7999", "7999", "799d",
		"799f", "79a3", "79a3", "79a5", "79a5", "79a9", "79a9", "79af", "79af",
		"79b5", "79b5", "79bc", "79bc", "79c2", "79c4", "79c6", "79c7", "79ca",
		"79ca", "79cc", "79cc", "79d0", "79d0", "79d3", "79d4", "79d7", "79d7",
		"79d9", "79db", "79e1", "79e2", "79e5", "79e5", "79e8", "79e8", "79ef",
		"79f5", "79f9", "79f9", "79fc", "79ff", "7a01", "7a01", "7a06", "7a07",
		"7a09", "7a09", "7a0e", "7a0f", "7a16", "7a16", "7a1d", "7a1d", "7a21",
		"7a21", "7a23", "7a25", "7a27", "7a27", "7a29", "7a2a", "7a2c", "7a2d",
		"7a32", "7a36", "7a38", "7a38", "7a3a", "7a3a", "7a3e", "7a3e", "7a41",
		"7a43", "7a45", "7a45", "7a49", "7a49", "7a4f", "7a53", "7a55", "7a55",
		"7a59", "7a59", "7a5d", "7a5e", "7a63", "7a66", "7a6a", "7a6a", "7a6f",
		"7a6f", "7a72", "7a73", "7a77", "7a77", "7a7c", "7a7d", "7a82", "7a83",
		"7a8d", "7a8e", "7a91", "7a91", "7a93", "7a93", "7a9a", "7a9d", "7aa1",
		"7aa1", "7aa4", "7aa7", "7aad", "7aad", "7ab0", "7ab0", "7ab9", "7ab9",
		"7abb", "7abd", "7ac2", "7ac3", "7ac6", "7ac6", "7ac8", "7ac9", "7acc",
		"7ad0", "7ad2", "7ad7", "7ada", "7ade", "7ae1", "7ae2", "7ae7", "7aea",
		"7aec", "7aec", "7af0", "7af5", "7af8", "7af8", "7afc", "7afc", "7afe",
		"7afe", "7b02", "7b03", "7b07", "7b07", "7b0b", "7b0d", "7b14", "7b17",
		"7b1c", "7b1c", "7b1f", "7b1f", "7b21", "7b21", "7b27", "7b27", "7b29",
		"7b29", "7b36", "7b37", "7b39", "7b3a", "7b3c", "7b3f", "7b41", "7b43",
		"7b53", "7b53", "7b55", "7b55", "7b57", "7b57", "7b59", "7b5f", "7b62",
		"7b62", "7b68", "7b68", "7b6a", "7b6c", "7b6f", "7b6f", "7b79", "7b81",
		"7b83", "7b83", "7b86", "7b86", "7b89", "7b89", "7b92", "7b93", "7b9a",
		"7b9a", "7b9e", "7b9f", "7ba2", "7ba3", "7ba5", "7bab", "7bae", "7bae",
		"7bb0", "7bb0", "7bb2", "7bb3", "7bb6", "7bb6", "7bba", "7bbd", "7bbf",
		"7bbf", "7bc2", "7bc3", "7bc5", "7bc5", "7bc8", "7bc8", "7bcd", "7bcd",
		"7bcf", "7bd3", "7bd6", "7bd7", "7bec", "7bef", "7bf5", "7bf6", "7bfa",
		"7bfa", "7bfc", "7bfc", "7c04", "7c04", "7c08", "7c08", "7c12", "7c18",
		"7c1a", "7c1b", "7c24", "7c24", "7c2e", "7c2f", "7c31", "7c32", "7c34",
		"7c36", "7c3a", "7c3a", "7c41", "7c42", "7c44", "7c44", "7c46", "7c46",
		"7c4b", "7c4b", "7c4e", "7c4f", "7c51", "7c52", "7c55", "7c56", "7c58",
		"7c58", "7c5d", "7c5e", "7c61", "7c62", "7c68", "7c68", "7c6d", "7c6d",
		"7c70", "7c71", "7c74", "7c74", "7c76", "7c77", "7c7b", "7c7c", "7c7e",
		"7c7e", "7c82", "7c83", "7c86", "7c87", "7c8b", "7c8b", "7c8e", "7c90",
		"7c93", "7c93", "7c99", "7c9d", "7ca0", "7ca0", "7ca4", "7ca4", "7ca6",
		"7ca6", "7ca9", "7cae", "7cb0", "7cb0", "7cb6", "7cb8", "7cc0", "7cc4",
		"7cc6", "7cc7", "7cc9", "7cc9", "7ccd", "7ccd", "7ccf", "7ccf", "7cd3",
		"7cd3", "7cd8", "7cd8", "7cda", "7cdb", "7ce1", "7ce1", "7ce3", "7ce6",
		"7ce9", "7ce9", "7ceb", "7ceb", "7ced", "7ced", "7cf3", "7cf3", "7cf5",
		"7cf5", "7cf9", "7cfa", "7cfc", "7cfc", "7cff", "7cff", "7d23", "7d27",
		"7d2a", "7d2a", "7d2d", "7d2d", "7d34", "7d34", "7d37", "7d37", "7d48",
		"7d49", "7d4b", "7d4d", "7d57", "7d57", "7d59", "7d5a", "7d5d", "7d5d",
		"7d60", "7d60", "7d64", "7d65", "7d6c", "7d6c", "7d74", "7d78", "7d7e",
		"7d7e", "7d82", "7d82", "7d87", "7d87", "7d89", "7d8b", "7d90", "7d90",
		"7d95", "7d95", "7d97", "7d9b", "7da4", "7da5", "7da8", "7da8", "7dab",
		"7dab", "7db3", "7db3", "7db6", "7db6", "7dc3", "7dc3", "7dc8", "7dc8",
		"7dcd", "7dcd", "7dcf", "7dd1", "7dd3", "7dd6", "7ddc", "7ddc", "7de2",
		"7de2", "7de4", "7de5", "7deb", "7deb", "7ded", "7ded", "7df5", "7df5",
		"7df8", "7df8", "7dfc", "7e02", "7e04", "7e07", "7e18", "7e19", "7e26",
		"7e28", "7e2c", "7e2c", "7e4a", "7e4b", "7e4d", "7e4f", "7e5b", "7e5b",
		"7e5d", "7e5d", "7e64", "7e67", "7e6c", "7e6c", "7e6e", "7e6e", "7e71",
		"7e71", "7e7f", "7e7f", "7e83", "7e85", "7e89", "7e89", "7e8e", "7e8e",
		"7e90", "7e90", "7e92", "7e92", "7e9d", "7f35", "7f37", "7f37", "7f3b",
		"7f3c", "7f40", "7f42", "7f46", "7f47", "7f49", "7f49", "7f4e", "7f4e",
		"7f52", "7f53", "7f56", "7f57", "7f59", "7f5a", "7f62", "7f62", "7f64",
		"7f64", "7f6f", "7f6f", "7f71", "7f71", "7f74", "7f74", "7f78", "7f78",
		"7f80", "7f82", "7f84", "7f84", "7f8f", "7f90", "7f93", "7f93", "7f97",
		"7f99", "7f9f", "7f9f", "7fa3", "7fa3", "7faa", "7fab", "7fae", "7fae",
		"7fb4", "7fb4", "7fc4", "7fc4", "7fc6", "7fc6", "7fc8", "7fc8", "7fd3",
		"7fd3", "7fd6", "7fd6", "7fd8", "7fda", "7fdd", "7fdd", "7fe4", "7fe4",
		"7fe7", "7fe7", "7ff6", "7ff6", "7ffa", "7ffa", "8002", "8002", "8008",
		"800a", "8013", "8013", "801a", "801a", "801d", "801d", "8020", "8020",
		"8022", "8023", "8025", "8025", "8027", "8027", "802b", "802b", "802d",
		"802f", "8031", "8032", "8038", "8038", "803a", "803c", "8040", "8042",
		"8044", "8045", "8049", "8049", "804b", "804e", "8053", "8055", "8057",
		"8057", "8059", "8059", "805b", "805b", "805f", "8063", "8065", "8066",
		"8068", "806b", "806d", "806e", "8074", "8074", "807a", "807c", "8080",
		"8081", "8083", "8083", "8088", "8088", "808d", "808e", "8091", "8091",
		"8094", "8094", "8097", "8097", "809e", "80a0", "80a4", "80a4", "80a6",
		"80a8", "80ac", "80ac", "80b0", "80b0", "80b3", "80b3", "80b6", "80b7",
		"80b9", "80b9", "80bb", "80c1", "80c6", "80c6", "80cb", "80cb", "80d2",
		"80d3", "80df", "80df", "80e2", "80e2", "80e7", "80ec", "80ee", "80ee",
		"80f6", "80f7", "80ff", "80ff", "8103", "8104", "8107", "8107", "8109",
		"8109", "810b", "8114", "8117", "8117", "811a", "811a", "811c", "811c",
		"8120", "8120", "8126", "8126", "8128", "8128", "812a", "812a", "812e",
		"812e", "8131", "8138", "813b", "813c", "813f", "8142", "8145", "8145",
		"8148", "8149", "8156", "815a", "815d", "815d", "815f", "815f", "8163",
		"8163", "8168", "8168", "816a", "816a", "816c", "816d", "8175", "8175",
		"817b", "817e", "8181", "8181", "8184", "8185", "818e", "818e", "8190",
		"8194", "8196", "8196", "81a1", "81a1", "81a4", "81a5", "81aa", "81aa",
		"81ad", "81ad", "81af", "81af", "81b6", "81b6", "81b8", "81b8", "81c1",
		"81c1", "81c8", "81c8", "81cb", "81cb", "81ce", "81ce", "81d3", "81d4",
		"81d6", "81d6", "81dc", "81dc", "81e4", "81e4", "81eb", "81eb", "81ef",
		"81f1", "81f5", "81f6", "81fd", "81fd", "8203", "8203", "8206", "8206",
		"820e", "820f", "8213", "8213", "8217", "821a", "8223", "8224", "8226",
		"8227", "8229", "8229", "822d", "822e", "8230", "8231", "823b", "823b",
		"823e", "823e", "8241", "8241", "8243", "8243", "8246", "8246", "8248",
		"8248", "824a", "824a", "824c", "824d", "8254", "8254", "825d", "825d",
		"8260", "8260", "8262", "8262", "8265", "8265", "8267", "8267", "826a",
		"826a", "8270", "8270", "8273", "8273", "8276", "8276", "8279", "827b",
		"8281", "8282", "8286", "8289", "828c", "828c", "8295", "8297", "829c",
		"829c", "82a6", "82a6", "82aa", "82aa", "82b2", "82b2", "82bf", "82bf",
		"82c1", "82c1", "82c4", "82d0", "82d8", "82d8", "82da", "82da", "82dd",
		"82dd", "82e2", "82e2", "82e9", "82e9", "82ee", "82ee", "82f7", "82f8",
		"82fc", "82fd", "82ff", "82ff", "830a", "830b", "830e", "8315", "8318",
		"8318", "831a", "831a", "831d", "831d", "831f", "831f", "8321", "8321",
		"8323", "8323", "832e", "832e", "8330", "8330", "833d", "833e", "8346",
		"8346", "8355", "8355", "8357", "8372", "8379", "8379", "8380", "8380",
		"8382", "8382", "8384", "8385", "8391", "8391", "839c", "839c", "839f",
		"839f", "83a1", "83a1", "83ac", "83ad", "83b1", "83bc", "83be", "83be",
		"83cd", "83cd", "83d0", "83d0", "83d2", "83d3", "83da", "83da", "83e6",
		"83e6", "83ed", "83ed", "83f7", "83f7", "8400", "8400", "8402", "8402",
		"8405", "8405", "8408", "8408", "8414", "841a", "841c", "8422", "8424",
		"8428", "842a", "842a", "842e", "842e", "843e", "843e", "8441", "8441",
		"8448", "8448", "844a", "844a", "844f", "844f", "8453", "8453", "8455",
		"8455", "8458", "8458", "845c", "845c", "8462", "8462", "8464", "8464",
		"846a", "846a", "8471", "8472", "847b", "847c", "847f", "8481", "8483",
		"8485", "8487", "848c", "8492", "8493", "8495", "8496", "84a3", "84a3",
		"84a5", "84a6", "84ad", "84ad", "84b3", "84b3", "84b5", "84b5", "84b7",
		"84b7", "84bd", "84be", "84c3", "84c3", "84c8", "84c8", "84d5", "84d5",
		"84d8", "84da", "84dc", "84e6", "84ed", "84ed", "84f5", "84f5", "84f8",
		"84f8", "8501", "8501", "8503", "8505", "8510", "8510", "851b", "851b",
		"8522", "8522", "8532", "853a", "853c", "853c", "853f", "853f", "8542",
		"8542", "854b", "854c", "854f", "8550", "8552", "8552", "855a", "855a",
		"855c", "855c", "855f", "855f", "856f", "8570", "8572", "8574", "857d",
		"857d", "857f", "857f", "8592", "8593", "8597", "8597", "85a5", "85a5",
		"85ab", "85ae", "85b2", "85b2", "85bb", "85bc", "85c1", "85c1", "85ca",
		"85ca", "85cc", "85cc", "85d3", "85d4", "85d6", "85d6", "85db", "85db",
		"85e0", "85e0", "85e7", "85e7", "85ee", "85ee", "85f3", "85f5", "85fc",
		"85fc", "8602", "8603", "8608", "8608", "860d", "8610", "8612", "8616",
		"861d", "861d", "8628", "8628", "862b", "862b", "862f", "8630", "8637",
		"8637", "863d", "863d", "8641", "8642", "8644", "8645", "8649", "864a",
		"864f", "864f", "8651", "8651", "8657", "8658", "865a", "865a", "865d",
		"865d", "8660", "8660", "8666", "8666", "866c", "866c", "8672", "8672",
		"8675", "8676", "8678", "8678", "867d", "8684", "8688", "8689", "868f",
		"868f", "8692", "8692", "869b", "869b", "869f", "86a0", "86a6", "86a6",
		"86ab", "86ae", "86b2", "86b2", "86ca", "86ca", "86cd", "86cf", "86d2",
		"86d2", "86d5", "86d5", "86e0", "86e1", "86e5", "86e5", "86e7", "86e7",
		"86ee", "86f4", "86fc", "86fd", "86ff", "86ff", "870f", "8710", "8714",
		"8717", "871d", "871d", "871f", "871f", "872b", "872b", "872f", "872f",
		"8736", "8736", "8739", "8739", "873d", "873d", "8744", "8745", "8747",
		"874b", "8770", "8772", "877c", "8780", "8786", "8786", "878a", "878c",
		"878e", "878e", "8795", "8795", "8799", "8799", "87a0", "87a1", "87a5",
		"87a9", "87b1", "87b1", "87c1", "87c1", "87c7", "87c7", "87cd", "87d0",
		"87d5", "87d6", "87da", "87da", "87e9", "87e9", "87ee", "87ee", "87f0",
		"87f1", "87f5", "87f5", "87f8", "87f8", "87fd", "87fd", "8804", "8804",
		"8807", "8807", "880e", "880f", "8812", "8812", "8818", "8818", "881a",
		"881a", "881e", "881e", "8827", "8827", "882d", "882d", "8834", "8834",
		"883a", "883a", "8842", "8842", "8845", "8847", "8849", "8849", "884f",
		"8851", "8854", "8854", "8858", "8858", "885c", "885c", "885e", "8860",
		"8864", "8866", "886c", "886c", "886e", "886e", "8873", "8873", "8878",
		"8878", "887a", "887b", "8884", "8887", "888a", "888a", "888f", "8890",
		"8894", "8894", "889c", "889d", "88a0", "88a0", "88a3", "88a3", "88a5",
		"88a6", "88a9", "88a9", "88ad", "88b0", "88b3", "88b5", "88bb", "88bb",
		"88bf", "88bf", "88c3", "88c8", "88d1", "88d1", "88d3", "88d3", "88e0",
		"88e0", "88e2", "88e6", "88e9", "88ea", "88ed", "88ed", "88f5", "88f5",
		"88ff", "8900", "8903", "8904", "8908", "8908", "890d", "890d", "890f",
		"890f", "891b", "891d", "8920", "8920", "8924", "8924", "8928", "8928",
		"8934", "8934", "8939", "893a", "893f", "8940", "8943", "8943", "8945",
		"8945", "8947", "8948", "894a", "894a", "894d", "894e", "8954", "8955",
		"8965", "8965", "8967", "8968", "8970", "8970", "8975", "8975", "8977",
		"8978", "897d", "897d", "8980", "8980", "8984", "8984", "8987", "8987",
		"8989", "898a", "898c", "898e", "8990", "8992", "8994", "8994", "8999",
		"899a", "89a0", "89a0", "89a5", "89a5", "89a7", "89a9", "89ab", "89ab",
		"89b0", "89b1", "89b3", "89b5", "89b8", "89b8", "89bb", "89bc", "89c1",
		"89d1", "89d7", "89d8", "89de", "89de", "89e7", "89e7", "89ea", "89ea",
		"89ee", "89ef", "89f5", "89f5", "89f9", "89f9", "89fd", "89fd", "8a01",
		"8a01", "8a05", "8a06", "8a09", "8a09", "8a0b", "8a0b", "8a0d", "8a0d",
		"8a14", "8a14", "8a19", "8a1a", "8a1c", "8a1c", "8a20", "8a21", "8a24",
		"8a24", "8a26", "8a26", "8a28", "8a29", "8a2b", "8a2b", "8a2e", "8a2f",
		"8a32", "8a33", "8a35", "8a35", "8a37", "8a38", "8a3d", "8a3d", "8a42",
		"8a43", "8a47", "8a47", "8a49", "8a49", "8a4b", "8a4b", "8a53", "8a53",
		"8a5a", "8a5a", "8a5c", "8a5d", "8a5f", "8a5f", "8a64", "8a65", "8a67",
		"8a67", "8a6a", "8a6a", "8a6f", "8a6f", "8a78", "8a78", "8a7d", "8a7e",
		"8a80", "8a80", "8a88", "8a8a", "8a8e", "8a8e", "8a90", "8a90", "8a94",
		"8a94", "8a97", "8a97", "8a9b", "8a9d", "8a9f", "8a9f", "8aa2", "8aa2",
		"8aa9", "8aa9", "8aac", "8aaf", "8ab1", "8ab1", "8ab3", "8ab5", "8ab7",
		"8ab7", "8ac1", "8ac1", "8aca", "8aca", "8acc", "8acc", "8ace", "8ace",
		"8ad0", "8ad0", "8ada", "8ada", "8ae3", "8ae3", "8ae5", "8ae5", "8ae9",
		"8aea", "8aec", "8aec", "8af9", "8af9", "8afd", "8afd", "8b03", "8b03",
		"8b09", "8b09", "8b0c", "8b0c", "8b1f", "8b1f", "8b21", "8b21", "8b29",
		"8b29", "8b2d", "8b2d", "8b32", "8b32", "8b34", "8b34", "8b38", "8b38",
		"8b3f", "8b3f", "8b43", "8b44", "8b4c", "8b4d", "8b5b", "8b5b", "8b5e",
		"8b5e", "8b61", "8b62", "8b64", "8b64", "8b69", "8b69", "8b6e", "8b6e",
		"8b71", "8b73", "8b75", "8b76", "8b7c", "8b7c", "8b81", "8b81", "8b83",
		"8b83", "8b87", "8b87", "8b89", "8b89", "8b8d", "8b8d", "8b8f", "8b91",
		"8b97", "8b97", "8b9b", "8b9b", "8b9d", "8b9d", "8ba0", "8c36", "8c38",
		"8c38", "8c3a", "8c3a", "8c40", "8c40", "8c44", "8c44", "8c51", "8c53",
		"8c58", "8c59", "8c5b", "8c5b", "8c5e", "8c5e", "8c60", "8c60", "8c63",
		"8c63", "8c67", "8c67", "8c6e", "8c6e", "8c74", "8c74", "8c7c", "8c7c",
		"8c7e", "8c7f", "8c83", "8c83", "8c87", "8c88", "8c8b", "8c8b", "8c8e",
		"8c8e", "8c96", "8c96", "8c9b", "8c9b", "8c9f", "8c9f", "8ca6", "8ca6",
		"8cad", "8cae", "8cb1", "8cb1", "8cc6", "8cc6", "8cc9", "8cc9", "8ccb",
		"8ccb", "8ccd", "8cce", "8cd0", "8cd0", "8cd4", "8cd4", "8cd6", "8cd6",
		"8cd8", "8cd8", "8cdb", "8cdb", "8ce9", "8ce9", "8ceb", "8ceb", "8cef",
		"8cef", "8cf2", "8cf2", "8cf6", "8cf7", "8cff", "8cff", "8d01", "8d01",
		"8d03", "8d03", "8d0b", "8d0c", "8d0e", "8d0e", "8d11", "8d12", "8d18",
		"8d18", "8d1a", "8d1a", "8d1c", "8d63", "8d65", "8d65", "8d6a", "8d6a",
		"8d71", "8d71", "8d75", "8d75", "8d7a", "8d7a", "8d7c", "8d7c", "8d7e",
		"8d7f", "8d82", "8d83", "8d86", "8d88", "8d8b", "8d8b", "8d97", "8d98",
		"8d9a", "8d9a", "8d9d", "8d9e", "8da2", "8da2", "8da4", "8da4", "8da6",
		"8da6", "8da9", "8da9", "8db0", "8db1", "8db8", "8db8", "8dbb", "8dbb",
		"8dbd", "8dbd", "8dc0", "8dc0", "8dc3", "8dc4", "8dc9", "8dca", "8dd2",
		"8dd2", "8dd4", "8dd4", "8dde", "8dde", "8de5", "8de5", "8ded", "8ded",
		"8df5", "8df9", "8dfb", "8dfb", "8e01", "8e01", "8e08", "8e08", "8e0b",
		"8e0c", "8e0e", "8e0e", "8e28", "8e28", "8e2a", "8e2a", "8e2c", "8e2d",
		"8e2f", "8e2f", "8e32", "8e32", "8e37", "8e37", "8e3a", "8e3b", "8e43",
		"8e43", "8e46", "8e46", "8e4f", "8e4f", "8e51", "8e52", "8e58", "8e58",
		"8e68", "8e68", "8e6b", "8e6b", "8e6e", "8e6e", "8e70", "8e71", "8e75",
		"8e75", "8e77", "8e77", "8e79", "8e79", "8e7d", "8e80", "8e83", "8e83",
		"8e8f", "8e8f", "8e99", "8e99", "8e9b", "8e9c", "8ea2", "8ea2", "8ea7",
		"8ea7", "8ead", "8eb1", "8eb3", "8eb9", "8ebb", "8ebc", "8ebe", "8ebf",
		"8ec1", "8ec1", "8ec3", "8ec8", "8ece", "8ece", "8ed0", "8ed0", "8ed5",
		"8ed6", "8ed9", "8eda", "8ee2", "8ee4", "8eea", "8eea", "8eed", "8eed",
		"8ef0", "8ef0", "8ef2", "8ef3", "8efd", "8efd", "8f04", "8f04", "8f0c",
		"8f0c", "8f0f", "8f0f", "8f19", "8f19", "8f21", "8f22", "8f27", "8f28",
		"8f2b", "8f2b", "8f2d", "8f2d", "8f30", "8f31", "8f3a", "8f3a", "8f3c",
		"8f3d", "8f41", "8f41", "8f4a", "8f4a", "8f4c", "8f4c", "8f5c", "8f5c",
		"8f65", "8f9a", "8f9d", "8f9e", "8fa0", "8fa2", "8fa4", "8fa5", "8fa7",
		"8fa7", "8fa9", "8fac", "8fb3", "8fb3", "8fb5", "8fbe", "8fc0", "8fc1",
		"8fc3", "8fc3", "8fc7", "8fc8", "8fca", "8fca", "8fcc", "8fcc", "8fcf",
		"8fd0", "8fd8", "8fdf", "8fe7", "8fe7", "8fe9", "8fe9", "8fec", "8fec",
		"8fef", "8fef", "8ff1", "8ff3", "8ff9", "8ff9", "9007", "900a", "900e",
		"900e", "9012", "9013", "9018", "9018", "9025", "902c", "9030", "9030",
		"9033", "9033", "9037", "9037", "9039", "903b", "9040", "9040", "9043",
		"9043", "9045", "9046", "9048", "9048", "904c", "904c", "9056", "9057",
		"905a", "905a", "905f", "905f", "9061", "9061", "9064", "9066", "906a",
		"906a", "906c", "906c", "9071", "9071", "9089", "9089", "908c", "908c",
		"908e", "908e", "9092", "9093", "9096", "9096", "909a", "909a", "909c",
		"909d", "90a4", "90a4", "90a8", "90a9", "90ab", "90ae", "90b7", "90b7",
		"90b9", "90bc", "90c0", "90c0", "90c2", "90c2", "90c4", "90c4", "90c6",
		"90c6", "90c9", "90c9", "90cc", "90cd", "90cf", "90d3", "90de", "90de",
		"90e6", "90e7", "90ee", "90ee", "90f6", "90f8", "910a", "910a", "910c",
		"910c", "9113", "9113", "9115", "9115", "9125", "9125", "9137", "9137",
		"913c", "913d", "9142", "9142", "9151", "9151", "9154", "9154", "9159",
		"9159", "915b", "915e", "9166", "9167", "916b", "916b", "916d", "916d",
		"9170", "9171", "9176", "9176", "917b", "917f", "9188", "9188", "918c",
		"918c", "918e", "918e", "9194", "9198", "91a4", "91a4", "91a6", "91a6",
		"91a9", "91a9", "91b6", "91b6", "91b8", "91b8", "91bb", "91bb", "91bf",
		"91bf", "91c4", "91c4", "91c8", "91c8", "91ca", "91ca", "91d2", "91d2",
		"91d6", "91d6", "91db", "91db", "91de", "91e1", "91e5", "91e5", "91ef",
		"91f0", "91f2", "91f2", "91f6", "91f6", "91fa", "91fc", "91fe", "91fe",
		"9208", "9208", "920b", "920b", "920e", "920e", "9213", "9213", "9218",
		"9218", "921b", "921b", "921d", "921d", "921f", "9222", "9228", "922c",
		"922f", "922f", "9235", "9235", "923b", "923c", "9241", "9244", "9247",
		"9247", "9255", "9255", "9258", "9259", "925c", "925d", "925f", "925f",
		"9262", "9262", "9268", "926b", "926e", "926e", "9271", "9271", "9273",
		"9275", "9277", "9277", "9281", "9281", "9284", "9284", "9289", "9289",
		"928f", "9290", "9292", "9292", "929e", "929f", "92ad", "92b1", "92b8",
		"92b8", "92ba", "92ba", "92bd", "92bf", "92d4", "92d4", "92d6", "92d6",
		"92da", "92dc", "92e2", "92e3", "92e5", "92e5", "92eb", "92ed", "92f2",
		"92f6", "92fd", "92fd", "9303", "9303", "9305", "9305", "9307", "9307",
		"930a", "930a", "9311", "9311", "9317", "9317", "931c", "931c", "932c",
		"932c", "9330", "9332", "9337", "9337", "933a", "933b", "933d", "9345",
		"9348", "9348", "9353", "9353", "935d", "935d", "935f", "935f", "9362",
		"9362", "9366", "9366", "9368", "9369", "936b", "936b", "936e", "936f",
		"9372", "9374", "9378", "9378", "937d", "937d", "937f", "937f", "9381",
		"9381", "9384", "9387", "938b", "938b", "9390", "9390", "9393", "9393",
		"939c", "939c", "93a0", "93a0", "93ab", "93ab", "93ad", "93ad", "93b6",
		"93b6", "93b8", "93bf", "93c1", "93c1", "93c5", "93c6", "93c9", "93c9",
		"93cb", "93cb", "93d3", "93d3", "93db", "93db", "93e0", "93e0", "93e5",
		"93e5", "93e9", "93eb", "93ed", "93ed", "93ef", "93f4", "9401", "9402",
		"9404", "9405", "9408", "9408", "9417", "9417", "941a", "941f", "9421",
		"9427", "942d", "942d", "942f", "942f", "9434", "9434", "943e", "943e",
		"9441", "9443", "944d", "944e", "9453", "9454", "9456", "9456", "9458",
		"945c", "945f", "945f", "9461", "9461", "9465", "9467", "946c", "946c",
		"9479", "947b", "9484", "9576", "9578", "9579", "957e", "957f", "9581",
		"9581", "9584", "9585", "9587", "9587", "958a", "958a", "9595", "9597",
		"9599", "959a", "959d", "959d", "95a0", "95a0", "95a2", "95a2", "95a6",
		"95a7", "95aa", "95aa", "95af", "95af", "95b2", "95b4", "95b8", "95b8",
		"95c1", "95c2", "95c4", "95c4", "95ce", "95cf", "95d7", "95d9", "95dd",
		"95dd", "95e6", "961b", "961d", "961d", "961f", "961f", "9625", "9627",
		"9629", "9629", "962b", "962b", "9633", "9638", "963e", "963e", "9641",
		"9641", "9645", "9649", "9652", "9652", "9655", "9657", "9659", "965a",
		"9660", "9660", "9665", "9669", "966e", "966e", "9679", "967b", "967f",
		"967f", "9681", "9682", "968c", "968c", "968f", "9690", "9696", "9696",
		"969a", "969a", "969d", "969d", "969f", "96a0", "96a3", "96a3", "96a5",
		"96a6", "96ab", "96ab", "96ad", "96ad", "96af", "96af", "96b2", "96b2",
		"96b5", "96b7", "96ba", "96ba", "96bd", "96be", "96cf", "96d1", "96e0",
		"96e0", "96e4", "96e4", "96e6", "96e7", "96eb", "96ee", "96f3", "96f4",
		"96fc", "96fc", "96fe", "96fe", "9701", "9701", "9703", "9703", "970a",
		"970a", "970c", "970c", "9714", "9715", "9717", "9717", "971a", "971b",
		"9721", "9721", "972d", "972d", "9731", "9731", "9733", "9734", "9736",
		"9737", "973b", "973c", "9740", "9741", "9745", "9745", "974a", "974a",
		"974c", "9751", "9753", "9755", "9757", "9757", "9759", "9759", "975d",
		"975d", "975f", "975f", "9763", "9765", "9767", "9767", "976b", "976b",
		"976d", "976d", "976f", "976f", "9771", "9771", "9775", "9775", "9779",
		"9779", "9786", "9787", "9789", "9789", "978c", "978c", "9790", "9793",
		"9795", "9796", "979b", "979b", "979f", "979f", "97a7", "97a7", "97a9",
		"97a9", "97af", "97b2", "97b4", "97b5", "97b8", "97b8", "97ba", "97ba",
		"97bc", "97be", "97c0", "97c0", "97c2", "97c2", "97c8", "97c8", "97ca",
		"97ca", "97d1", "97d2", "97da", "97db", "97e0", "97e0", "97e2", "97e2",
		"97e4", "97e4", "97e6", "97ec", "97ee", "97ef", "97f2", "97f2", "97f4",
		"97f5", "97f7", "97f7", "97fc", "97fc", "9809", "9809", "980b", "980b",
		"9814", "9815", "9819", "981a", "981f", "981f", "9822", "9823", "9825",
		"9825", "982a", "982a", "982c", "982c", "982e", "982e", "9831", "9831",
		"9833", "9834", "9836", "9836", "983a", "983a", "983c", "9840", "9842",
		"9842", "9847", "9847", "984b", "984b", "9854", "9856", "985a", "985a",
		"9861", "9861", "9866", "9866", "9868", "9868", "986c", "986e", "9875",
		"98a7", "98aa", "98ab", "98b0", "98b0", "98b4", "98b5", "98b7", "98b7",
		"98b9", "98b9", "98c3", "98c3", "98c5", "98c5", "98c7", "98c8", "98ca",
		"98ca", "98cd", "98da", "98dc", "98de", "98e0", "98e1", "98e4", "98e4",
		"98e6", "98e6", "98e8", "98e8", "98ec", "98ec", "98ee", "98ee", "98f0",
		"98f1", "98f3", "98f3", "98f5", "98f5", "98f7", "98f8", "98fb", "98fb",
		"98ff", "98ff", "9901", "9901", "9904", "9904", "9906", "9906", "990b",
		"990b", "990d", "990f", "9919", "9919", "991c", "991d", "9920", "9920",
		"9922", "9923", "9926", "9926", "9934", "9934", "9936", "9939", "993b",
		"993b", "9940", "9940", "9942", "9942", "9944", "9944", "9946", "9946",
		"994a", "994a", "994d", "994d", "994f", "994f", "995a", "995a", "995d",
		"995d", "9960", "9960", "9962", "9995", "999a", "999b", "999f", "99a0",
		"99a2", "99a2", "99a4", "99a4", "99a9", "99aa", "99b6", "99b8", "99bc",
		"99bc", "99be", "99c0", "99c4", "99c6", "99c8", "99c8", "99ca", "99ca",
		"99da", "99da", "99de", "99de", "99e0", "99e1", "99e6", "99e6", "99e8",
		"99e8", "99eb", "99eb", "99ef", "99ef", "99f2", "99f3", "99f5", "99f5",
		"9a00", "9a00", "9a08", "9a08", "9a0c", "9a0c", "9a10", "9a10", "9a12",
		"9a13", "9a17", "9a18", "9a1f", "9a1f", "9a21", "9a21", "9a26", "9a26",
		"9a28", "9a28", "9a2f", "9a2f", "9a33", "9a33", "9a3b", "9a3c", "9a47",
		"9a47", "9a4b", "9a4b", "9a51", "9a51", "9a58", "9a58", "9a5c", "9a5d",
		"9a61", "9a61", "9a63", "9a63", "9a6c", "9aa7", "9aa9", "9aaa", "9aac",
		"9aac", "9aae", "9aae", "9ab2", "9ab2", "9ab5", "9ab6", "9aba", "9aba",
		"9abd", "9abd", "9ac3", "9ac5", "9ac8", "9ac9", "9acb", "9acc", "9ace",
		"9ace", "9ad7", "9ad7", "9ad9", "9adb", "9add", "9ade", "9ae0", "9ae0",
		"9ae2", "9ae2", "9ae4", "9ae5", "9ae8", "9aea", "9af0", "9af0", "9af4",
		"9af5", "9af8", "9af8", "9aff", "9b00", "9b02", "9b02", "9b07", "9b07",
		"9b09", "9b09", "9b0f", "9b0f", "9b13", "9b14", "9b1b", "9b1d", "9b21",
		"9b21", "9b26", "9b26", "9b2a", "9b2a", "9b2c", "9b2d", "9b30", "9b30",
		"9b34", "9b34", "9b36", "9b36", "9b38", "9b39", "9b3d", "9b3d", "9b40",
		"9b40", "9b47", "9b47", "9b49", "9b49", "9b50", "9b50", "9b53", "9b53",
		"9b57", "9b57", "9b5c", "9b5e", "9b62", "9b63", "9b65", "9b65", "9b69",
		"9b6b", "9b6d", "9b6e", "9b72", "9b73", "9b78", "9b79", "9b7f", "9b7f",
		"9b81", "9b81", "9b83", "9b84", "9b89", "9b8f", "9b94", "9b94", "9b96",
		"9b99", "9b9c", "9b9d", "9b9f", "9b9f", "9ba3", "9ba3", "9ba7", "9ba7",
		"9ba9", "9ba9", "9bac", "9bac", "9bb0", "9bb4", "9bb7", "9bb7", "9bba",
		"9bbc", "9bbe", "9bbe", "9bc2", "9bc2", "9bc5", "9bc5", "9bcb", "9bd2",
		"9bd8", "9bd8", "9bdd", "9bdd", "9bdf", "9bdf", "9be3", "9be3", "9be9",
		"9be9", "9bed", "9bef", "9bf1", "9bf6", "9bf9", "9bfc", "9bfe", "9c04",
		"9c0a", "9c0a", "9c0c", "9c0c", "9c0f", "9c11", "9c15", "9c16", "9c18",
		"9c1b", "9c1e", "9c20", "9c22", "9c22", "9c26", "9c27", "9c2a", "9c2a",
		"9c2e", "9c30", "9c35", "9c35", "9c38", "9c38", "9c3a", "9c3a", "9c42",
		"9c43", "9c45", "9c45", "9c47", "9c47", "9c4f", "9c4f", "9c51", "9c51",
		"9c53", "9c53", "9c5a", "9c5d", "9c61", "9c61", "9c64", "9c65", "9c69",
		"9c6c", "9c6f", "9c70", "9c72", "9c72", "9c76", "9c76", "9c7b", "9ce4",
		"9ce8", "9ce8", "9ceb", "9cec", "9cee", "9cf0", "9cf8", "9cf8", "9cfe",
		"9cfe", "9d01", "9d02", "9d0a", "9d0f", "9d11", "9d11", "9d13", "9d13",
		"9d16", "9d16", "9d1a", "9d1a", "9d1c", "9d1c", "9d21", "9d21", "9d24",
		"9d24", "9d27", "9d27", "9d2a", "9d2c", "9d32", "9d32", "9d34", "9d35",
		"9d39", "9d3a", "9d3c", "9d3c", "9d44", "9d44", "9d46", "9d49", "9d4d",
		"9d4e", "9d50", "9d50", "9d55", "9d55", "9d5e", "9d5e", "9d62", "9d66",
		"9d6d", "9d6e", "9d76", "9d76", "9d7a", "9d7a", "9d7c", "9d7c", "9d7e",
		"9d7e", "9d83", "9d83", "9d8d", "9d8f", "9d91", "9d91", "9d93", "9d93",
		"9d95", "9d95", "9da5", "9da5", "9dab", "9dab", "9dae", "9dae", "9db0",
		"9db0", "9dbd", "9dbd", "9dc0", "9dc0", "9dc4", "9dc4", "9dc6", "9dc6",
		"9dc9", "9dc9", "9dd4", "9dd4", "9de0", "9de0", "9de7", "9de7", "9dea",
		"9dea", "9df1", "9df1", "9dfc", "9dfc", "9e08", "9e08", "9e0a", "9e0a",
		"9e0c", "9e0c", "9e0e", "9e0e", "9e16", "9e16", "9e18", "9e18", "9e1c",
		"9e1c", "9e1f", "9e74", "9e76", "9e78", "9e7b", "9e7b", "9e7e", "9e7e",
		"9e81", "9e81", "9e84", "9e85", "9e8f", "9e90", "9e95", "9e96", "9e98",
		"9e98", "9e9e", "9e9e", "9ea2", "9ea3", "9ea6", "9ea6", "9ea8", "9ea8",
		"9eaa", "9eac", "9eaf", "9eaf", "9eb1", "9eb3", "9eb8", "9eba", "9ebd",
		"9ebd", "9ebf", "9ebf", "9ec1", "9ec1", "9ec4", "9ec7", "9ec9", "9ecb",
		"9ed2", "9ed2", "9ed7", "9ed7", "9ed9", "9ed9", "9ee1", "9ee3", "9ee9",
		"9eea", "9eec", "9eec", "9ef1", "9ef1", "9ef8", "9ef8", "9efe", "9efe",
		"9f02", "9f05", "9f08", "9f08", "9f0b", "9f0d", "9f11", "9f11", "9f14",
		"9f14", "9f17", "9f17", "9f1d", "9f1d", "9f1f", "9f1f", "9f21", "9f21",
		"9f26", "9f27", "9f39", "9f3a", "9f3c", "9f3c", "9f3f", "9f3f", "9f44",
		"9f45", "9f50", "9f51", "9f53", "9f53", "9f5a", "9f5a", "9f62", "9f62",
		"9f68", "9f69", "9f6d", "9f6d", "9f73", "9f73", "9f7c", "9f7d", "9f7f",
		"9f8c", "9f8e", "9f8f", "9f93", "9f93", "9f96", "9f97", "9f99", "9f9b",
		"9f9d", "9f9f", "9fa1", "9fa1", "9fa3", "9fa3", "fe32", "fe32", "fe45",
		"fe48", "fe53", "fe53", "fe58", "fe58", "fe67", "fe67", "fe6c", "ff00",
		"ff02", "ff02", "ff07", "ff07", "ff3b", "ff3b", "ff3d", "ff3e", "ff40",
		"ff40", "ff5f", "ffdf", "ffe2", "ffe2", "ffe4", "ffe4", "ffe6", "ffed");

// 以下為防右鍵code

function clickIE4() {
	if (event.button == 2) {
		return false;
	}
}

function clickNS4(e) {
	if (document.layers || document.getElementById && !document.all) {
		if (e.which == 2 || e.which == 3) {
			return false;
		}
	}
}

// if (document.layers){
// document.captureEvents(Event.MOUSEDOWN);
// document.onmousedown=clickNS4;
// }
// else if (document.all&&!document.getElementById){
// document.onmousedown=clickIE4;
// }
// document.oncontextmenu=new Function("return false")

// //防右鍵code結束

// //設定登出倒數開始
if (typeof (window.top.ResetCountDown) == "function")
	window.top.ResetCountDown();
// //設定登出倒數結束
