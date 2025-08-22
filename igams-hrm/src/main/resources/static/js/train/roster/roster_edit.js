$(function(){
    $("#rosterEditForm #checkbox").bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
        onText : "是",      // 设置ON文本
        offText : "否",    // 设置OFF文本
        onColor : "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
        offColor : "info",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
        size : "normal",    // 设置控件大小,从小到大  (mini/small/normal/large)
        // 当开关状态改变时触发
        onSwitchChange : function(event, state) {
            if (state == true) {
                $('#rosterEditForm #sfqdbmxy').val("1");
            } else {
                $('#rosterEditForm #sfqdbmxy').val("0");
            }
        }
    });
});