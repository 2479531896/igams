/**
 * 选择领料信息
 * @returns
 */
function chooselldh(){
    var url = $('#commentSaleFeedbackForm #urlPrefix').val() + "/saleFeedback/saleFeedback/pagedataChooseLlxx?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择领料信息', chooseLlxxConfig);
}
var chooseLlxxConfig = {
    width : "1000px",
    modalName	: "chooseLlxxModal",
    formName	: "chooseLlxxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var preJson = [];
                var selLldh = $("#chooseLlxxForm #yxlldh").tagsinput('items');
                if (selLldh.length==0){
                    $.alert("未获取到选中的领料信息！");
                    return false;
                }
                $("#commentSaleFeedbackForm #lldhs").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                for(var i = 0; i < selLldh.length; i++){
                    var value = selLldh[i].value;
                    var text = selLldh[i].text;
                    $("#commentSaleFeedbackForm #lldhs").tagsinput('add', {"value":value,"text":text});
                }
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
$(function(){
    jQuery('#commentSaleFeedbackForm .chosen-select').chosen({width: '100%'});
})