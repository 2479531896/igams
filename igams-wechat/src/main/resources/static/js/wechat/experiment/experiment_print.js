
function localIpCheck() {
    $("#printExperimentForm #remoteDiv").hide();
    $("#printExperimentForm #glxx").attr("disabled","disabled");
}

function remoteIpCheck() {
    $("#printExperimentForm #remoteDiv").show();
    $("#printExperimentForm #glxx").removeAttr("disabled");
}

function printerIpChecked(){
    if($("#printExperimentForm #local_ip").attr("checked")){
        $("#printExperimentForm #remoteDiv").hide();
        $("#printExperimentForm #glxx").attr("disabled","disabled");
    }else if($("#printExperimentForm #remote_ip").attr("checked")){
        $("#printExperimentForm #remoteDiv").show();
        $("#printExperimentForm #glxx").removeAttr("disabled");
    }
}


$(document).ready(function(){
    printerIpChecked();
    $("#printExperimentForm #print_num").val("3");
});
