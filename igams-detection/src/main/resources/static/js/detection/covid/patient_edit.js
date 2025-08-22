//通过省份级联城市
$("#sf").on('change',function(){
    var csid=$("#sf").val();
    $.ajax({
        url : "/detection/pagedataJscjcity",
        type : "post",
        data : {fcsid:csid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data != null && data.length != 0){
                var csbHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                    csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
                });
                $("#modPatientForm #cs").empty();
                $("#modPatientForm #cs").append(csHtml);
                $("#modPatientForm #cs").trigger("chosen:updated");
            }else{
                var csHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $("#modPatientForm #cs").empty();
                $("#modPatientForm #cs").append(csHtml);
                $("#modPatientForm #cs").trigger("chosen:updated");
            }
        }
    });
})
$(document).ready(function(){
    //所有下拉框添加choose样式
    jQuery('#modPatientForm .chosen-select').chosen({width: '100%'});
});
