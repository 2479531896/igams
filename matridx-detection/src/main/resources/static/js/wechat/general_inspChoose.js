$(function (){
    //普检检测
    $("#general_inspChoose .generalChoose").unbind("click").click(function(){
        if(!$("#wxid").val()){
            alert("未正确获取微信信息，请从菜单重新进入！");
            return false
        }
        $("#general_inspChoose").attr("action", "/wechat/detectionPJ/generalReportViewChoose");
        $("#general_inspChoose #fzjclx").val($(this).attr("csid"))
        $("#general_inspChoose #fzjclxdm").val($(this).attr("csdm"))
        document.getElementById("general_inspChoose").submit();
        return true;
    });
});