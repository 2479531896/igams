/**
 * 选择负责人列表
 * @returns
 */
function chooseTyry(){
    var url=$('#supplierdisableForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',chooseTyryConfig);
}
var chooseTyryConfig = {
    width		: "800px",
    modalName	:"chooseTyryModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#supplierdisableForm #tyry').val(sel_row[0].yhid);
                    $('#supplierdisableForm #tyrymc').val(sel_row[0].zsxm);
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

$(document).ready(function(){
    //添加日期控件
    laydate.render({
        elem: '#supplierdisableForm #tysj'
        ,theme: '#2381E9'
        , type: 'datetime'
        , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
    });
    //所有下拉框添加choose样式
    jQuery('#supplierdisableForm .chosen-select').chosen({width: '100%'});
});