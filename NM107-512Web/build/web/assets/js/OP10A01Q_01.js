var strPermissionData;

$(document).ready(function () {
    time(1);
    $('#menu10').addClass("active");
    $('#menu10span').addClass("selected");
    CaseCount();         //未結案件：未公告案件：招領期滿案件：領回期滿案件：
    AnnounceCaseCount(); //招領即將(7日後)期滿案件
    AnDlCaseCount();     //領回即將(7日後)期滿案件
});

