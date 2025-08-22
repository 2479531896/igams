
function chooseBm(){
    var url=$('#editAnnualPlanForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择实施部门',chooseSsbmConfig);
}
var chooseSsbmConfig = {
    width		: "800px",
    modalName	:"chooseSsbmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editAnnualPlanForm #ssbm').val(sel_row[0].jgid);
                    $('#editAnnualPlanForm #ssbmmc').val(sel_row[0].jgmc);
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

function chooseYh() {
    var url = $('#editAnnualPlanForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择讲师', chooseJsConfig);
}
var chooseJsConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseJsModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#taskListFzrForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editAnnualPlanForm #js').val(sel_row[0].yhid);
                    $('#editAnnualPlanForm #jsxm').val(sel_row[0].yhm+'-'+sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#editAnnualPlanForm #urlPrefix').val() +"/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
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
        elem: '#editAnnualPlanForm #jhpxsj'
        ,theme: '#2381E9'
    });
    //所有下拉框添加choose样式
    jQuery('#editAnnualPlanForm .chosen-select').chosen({width: '100%'});
});