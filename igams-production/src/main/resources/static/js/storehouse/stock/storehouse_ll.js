/**
 * 选择取样人列表
 * @returns
 */
function chooseQyr(){
    var url=$('#storehouseLlForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择领料人员',addQyrConfig);
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
                    $('#storehouseLlForm #qlry').val(sel_row[0].yhid);
                    $('#storehouseLlForm #zsxm').val(sel_row[0].zsxm);
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
   elem: '#storehouseLlForm #llrq'
  ,theme: '#2381E9'
    ,trigger: 'click'
});