

function openByt(sjid,event) {
    var url=$("#sampleAllotBoxForm #urlPrefix").val()+"/inspection/inspection/pagedataViewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewBytConfig);
}
/**
 * 病原体查看页面模态框
 */
var viewBytConfig={
    width		: "800px",
    modalName	:"viewBytModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}