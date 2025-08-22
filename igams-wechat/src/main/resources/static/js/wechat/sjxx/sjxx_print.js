
function localIpCheck() {
    $("#printInspectionForm #remoteDiv").hide();
    $("#printInspectionForm #glxx").attr("disabled","disabled");
}

function remoteIpCheck() {
    $("#printInspectionForm #remoteDiv").show();
    $("#printInspectionForm #glxx").removeAttr("disabled");
}

function printerIpChecked(){
    if($("#printInspectionForm #local_ip").attr("checked")){
        $("#printInspectionForm #remoteDiv").hide();
        $("#printInspectionForm #glxx").attr("disabled","disabled");
    }else if($("#printInspectionForm #remote_ip").attr("checked")){
        $("#printInspectionForm #remoteDiv").show();
        $("#printInspectionForm #glxx").removeAttr("disabled");
    }
}


$(document).ready(function(){
    printerIpChecked();
    $("#printInspectionForm #dysl").val("6");
});
