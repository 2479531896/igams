function jcxmChanged(){
    var jczxmlength = $("#detectionChooseIndex .t_jczxm").length;
    var jcxmid = $("#detectionChooseIndex #jcxm option:selected").attr("id");
    var dqjczxmid = $("#detectionChooseIndex #jczxm option:selected").attr("id");
    var isjczxmin = false;
    var isjczxmhide = true;
    if (jczxmlength>0){
        for (var i = 0; i < jczxmlength; i++) {
            var jczxmid = $("#detectionChooseIndex .t_jczxm")[i].id;
            var jczxmfcsid = $("#detectionChooseIndex #"+jczxmid).attr("fcsid")
            if (jczxmfcsid != null && jczxmfcsid != '' && jczxmfcsid == jcxmid){
                $("#detectionChooseIndex #"+jczxmid).attr("style","display:block;");
                if (dqjczxmid == jczxmid){
                    isjczxmin = true
                }
                isjczxmhide = false;
            }else {
                $("#detectionChooseIndex #"+jczxmid).attr("style","display:none;");
            }
        }
    }
    if (!isjczxmin){
        $("#detectionChooseIndex #jczxm").clearFields();
        $("#detectionChooseIndex #jczxm-1").prop("selected","selected");
    }
    $('#detectionChooseIndex #jczxm').trigger("chosen:updated");
    if (isjczxmhide){
        $("#jczxmForm").hide();
        $("#jczxmForm").attr("disabled","disabled");
    }else {
        $("#jczxmForm").show();
        $("#jczxmForm").removeAttr("disabled");
    }
}

function goToInspReport(){
    $("#detectionChooseIndex #toInspReport").attr("disabled","true");
    var jcxmid = $("#detectionChooseIndex #jcxm option:selected").attr("value");
    if (jcxmid!='-1' && jcxmid!=null){
        var jczxmid = $("#detectionChooseIndex #jczxm option:selected").attr("value");
        if ($("#detectionChooseIndex #jczxmForm").attr("disabled")=="disabled" || (jczxmid!=-1 && jczxmid!=null)){
            window.setTimeout(function(){
                $("#detectionChooseIndex #toInspReport").removeAttr("disabled");
            }, 1000);
            $("#detectionChooseIndex").attr("action", "/wechat/inspReport");
            document.getElementById("detectionChooseIndex").submit();
            return true;
        }else {
            alert("请选择检测子项目！");
            $("#detectionChooseIndex #toInspReport").removeAttr("disabled");
            return false;
        }
    }else {
        alert("请选择检测项目！");
        $("#detectionChooseIndex #toInspReport").removeAttr("disabled");
        return false;
    }
}

$(function (){
    jcxmChanged();
})