
$("#fzjcxmid").on('change',function(){
    var csid=$("#fzjcxmid").val();
    $.ajax({
        url : "/systemmain/data/ansyGetJcsjList",
        type : "post",
        data : {"fcsid":csid,"jclb":"MOLECULAR_SUBITEM","access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data != null && data.length != 0){
                var csbHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                    csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
                });
                $("#uploadDetectionPJForm #fzjczxmid").empty();
                $("#uploadDetectionPJForm #fzjczxmid").append(csHtml);
                $("#uploadDetectionPJForm #fzjczxmid").trigger("chosen:updated");
            }else{
                var csHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $("#uploadDetectionPJForm #fzjczxmid").empty();
                $("#uploadDetectionPJForm #fzjczxmid").append(csHtml);
                $("#uploadDetectionPJForm #fzjczxmid").trigger("chosen:updated");
            }
        }
    });
})


//点击隐藏文件上传
function cancelfile(){
    $("#fileDiv").hide();
    $("#file_btn").show();
}
//点击文件上传
function editfile(){
    $("#fileDiv").show();
    $("#file_btn").hide();
}

function displayUpInfo(fjid){
    if(!$("#uploadDetectionPJForm #fjids").val()){
        $("#uploadDetectionPJForm #fjids").val(fjid);
    }else{
        $("#uploadDetectionPJForm #fjids").val($("#uploadDetectionPJForm #fjids").val()+","+fjid);
    }
}

$(function(){
    var oFileInput = new FileInput();
    oFileInput.Init("uploadDetectionPJForm","displayUpInfo",2,1,"pro_file");
    //所有下拉框添加choose样式
    jQuery('#uploadDetectionPJForm .chosen-select').chosen({width: '100%'});
})
