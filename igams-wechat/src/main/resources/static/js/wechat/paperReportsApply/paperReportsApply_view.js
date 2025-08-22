$(function(){
    jQuery('#viewPaperReportsApplyForm .chosen-select').chosen({width: '100%'});
    var kdlxdm = $("#viewPaperReportsApplyForm #kdlx").find("option:selected").attr("csdm");
    if("JD"==kdlxdm){
        $("#viewPaperReportsApplyForm #jdkdDiv").show();
    }else{
        $("#viewPaperReportsApplyForm #jdkdDiv").hide();
    }
});

function queryBySjid(sjid){
    var url="/inspection/inspection/viewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewSjxxConfig);
}
/*查看送检信息模态框*/
var viewSjxxConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

//快递类型改变事件
$("#viewPaperReportsApplyForm select[name='kdlx']").unbind("change").change(function(e){
    var kdlxdm = $("#viewPaperReportsApplyForm #kdlx").find("option:selected").attr("csdm");
    if("JD"==kdlxdm){
        $("#viewPaperReportsApplyForm #jdkdDiv").show();
    }else{
        $("#viewPaperReportsApplyForm #jdkdDiv").hide();
    }
});