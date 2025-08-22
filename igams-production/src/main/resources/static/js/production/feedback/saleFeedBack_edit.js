/**
 * 选择客户列表
 * @returns
 */
function chooseKh(){
    var url=$('#editFeedBackForm #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择客户',addKhConfig);
}
var addKhConfig = {
    width		: "800px",
    modalName	:"addKhModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#client_list_ajaxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editFeedBackForm #khid').val(sel_row[0].khid);
                    $('#editFeedBackForm #khmc').val(sel_row[0].khmc);
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
$(document).ready(function() {
	jQuery('#editFeedBackForm .chosen-select').chosen({ width: '100%' });
});