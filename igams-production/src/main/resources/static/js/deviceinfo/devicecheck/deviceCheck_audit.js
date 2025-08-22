$(document).ready(function() {
    //添加日期控件
    laydate.render({
        elem: ' #auditDeviceCheckForm #ysrq'
        , theme: '#2381E9'
    });
    var val = $("#auditDeviceCheckForm #sfgdzc").val();
    if(val!='1'){
        $("#auditDeviceCheckForm #gdzcbh").removeAttr("validate"); //取消form验证
        $("#auditDeviceCheckForm #validate").hide();
    }
});