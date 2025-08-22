$(function (){
    //新冠病毒核酸检测预约
    $("#detectionIndex #detectionAppointment_input").unbind("click").click(function(){
        $("#detectionIndex #detectionAppointment_input").attr("disabled","true");
        window.setTimeout(function(){
            $("#detectionIndex #detectionAppointment_input").removeAttr("disabled");
        }, 1000);
        $("#detectionIndex").attr("action", "/wechat/detectionAppointment");
        document.getElementById("detectionIndex").submit();
        return true;
    });
    //查看/修改已预约的检测
    $("#detectionIndex #detectionViewAndMod_input").unbind("click").click(function(){
        $("#detectionIndex #detectionViewAndMod_input").attr("disabled","true");
        window.setTimeout(function(){
            $("#detectionIndex #detectionViewAndMod_input").removeAttr("disabled");
        }, 1000);
        $("#detectionIndex").attr("action", "/wechat/detectionViewAndMod");
        document.getElementById("detectionIndex").submit();
        return true;
    });
    //新冠病毒核酸检测报告查询
    $("#detectionIndex #detectionReport_input").unbind("click").click(function(){
        $("#detectionIndex #detectionReport_input").attr("disabled","true");
        window.setTimeout(function(){
            $("#detectionIndex #detectionReport_input").removeAttr("disabled");
        }, 1000);
        $("#detectionIndex").attr("action", "/wechat/detectionReport");
        document.getElementById("detectionIndex").submit();
        return true;
    });
    //手机号绑定
    $("#detectionIndex #phoneBinding_input").unbind("click").click(function(){
        $("#detectionIndex #detectionReport_input").attr("disabled","true");
        window.setTimeout(function(){
            $("#detectionIndex #detectionReport_input").removeAttr("disabled");
        }, 1000);
        $("#detectionIndex").attr("action", "/wechat/changePhoneBinding");
        document.getElementById("detectionIndex").submit();
        return true;
    });
});
$(function(){
    if ($("#detectionIndex #wxid").val()!=null && $("#detectionIndex #wxid").val() !=""){
        $("#phoneBinding_input").show();
        $("#normal").show();
        $("#erroPage").hide();
    }else {
        $("#phoneBinding_input").hide();
        $("#normal").hide();
        $("#erroPage").show();
    }
})