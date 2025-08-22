$(function(){
    $('#historyview_formSearch #jcxm').attr("style","display:block;");
    $('#historyview_formSearch #hzxm').attr("style","display:none;");
    $("#historyview_formSearch #checkbox").bootstrapSwitch({
        handleWidth: '30px',
        labelWidth: '20px',
        onText : "姓名",      // 设置ON文本
        offText : "项目",    // 设置OFF文本
        onColor : "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
        offColor : "info",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
        size : "normal",    // 设置控件大小,从小到大  (mini/small/normal/large)
        // 当开关状态改变时触发
        onSwitchChange : function(event, state) {
            if (state == true) {
                $('#historyview_formSearch #jcxm').attr("style","display:none;");
                $('#historyview_formSearch #hzxm').attr("style","display:block;");
            } else {
                $('#historyview_formSearch #jcxm').attr("style","display:block;");
                $('#historyview_formSearch #hzxm').attr("style","display:none;");
            }
        }
    });
});