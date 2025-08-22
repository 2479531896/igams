/**
 * 选择取样人列表
 * @returns
 */
function chooseQyr(){
    var url=$('#samplingForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择取样人',addQyrConfig);
}
var addQyrConfig = {
    width		: "800px",
    modalName	:"addQyrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#samplingForm #qyr').val(sel_row[0].yhid);
                    $('#samplingForm #qyrmc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#samplingForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
    if (value==null||value==''){
        return false;
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

laydate.render({
   elem: '#samplingForm #lrsj'
  ,theme: '#2381E9'
    ,trigger: 'click'
});