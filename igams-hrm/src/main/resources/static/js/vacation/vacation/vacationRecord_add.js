function changedw_jl() {
    var cskz1=$("#vacationRecord_Form #jqlx option:selected").attr("cskz1");
    $("#vacationRecord_Form #dw").val(cskz1)
}
function chooseXtyh_jl() {
    var url = $("#vacationRecord_Form #urlPrefix").val()+"/train/user/pagedataListUserForChoose?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择用户 ', chooseYhConfig);
}
var chooseYhConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseYhModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#userListForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#userListForm #yxyh").tagsinput('items');
                $("#vacationRecord_Form  #yhids").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                });
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    var text = sel[i].text;
                    $("#vacationRecord_Form  #yhids").tagsinput('add',{"value":value,"text":text});
                }
                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(function(){
    //添加日期控件
    laydate.render({
        elem: '#vacationRecord_Form #kssj'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#vacationRecord_Form #yxq'
        ,theme: '#2381E9'
    });
    // 所有下拉框添加choose样式
    jQuery('#vacationRecord_Form .chosen-select').chosen({width: '100%'});
})