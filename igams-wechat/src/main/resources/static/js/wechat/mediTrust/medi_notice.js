function chooseWxyh() {
    var url = "/train/user/pagedataListUserForChoose?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择负责人', chooseYhConfig);
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
                $("#medi_ajaxForm  #ryid").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                });
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    var text = sel[i].text;
                    $("#medi_ajaxForm  #ryid").tagsinput('add',{"value":value,"text":text});
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


function btnBind(){

}

function initPage(){

}

$(document).ready(function(){
    //绑定事件
    btnBind();
    //初始化页面数据
    initPage();
});