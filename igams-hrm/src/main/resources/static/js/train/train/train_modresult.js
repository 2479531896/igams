function updateJlbh(gzid){
    var jlbh=$("#modresultTrainForm #jlbh_"+gzid).val();
    var tgbj = $("#modresultTrainForm #tgbj_"+gzid+" option:selected").attr("value");
    if(jlbh||tgbj){
        $.ajax({
            type : "POST",
            async:false,
            url :  $("#modresultTrainForm #urlPrefix").val()+"/train/train/pagedataUpdateJlbh",
            data : {"gzid":gzid,"jlbh":jlbh,"tgbj":tgbj,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success:function(data){
                if(data.status=="success"){
                    $.success("更新成功!");
                }else{
                    $.error("更新失败!");
                }
            }
        });
    }
}

$(document).ready(function(){
    jQuery('#modresultTrainForm .chosen-select').chosen({width: '100%'});
});