var formAction = $("#khglxxForm #formAction").val();
function initPage(){
    //添加日期控件
    laydate.render({
        elem: '#khglxxForm #fzrq'
        ,theme: '#2381E9'
    });
}
$(function(){
    //所有下拉框添加choose样式
    jQuery('#khglxxForm .chosen-select').chosen({width: '100%'});
    initPage();
    if (formAction=="modSaveKhgl"){
        $("#khglxxForm #khdm").attr("readonly","readonly")
    }
    var sign_params=[];
    sign_params.prefix=$('#khglxxForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("khglxxForm","displayUpInfo",2,1,"sign_file",null,sign_params);
});
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#khglxxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#khglxxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
//下载模板
function xz(fjid){
    jQuery('<form action="' + $("#khglxxForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
//删除模板
function del(fjid,wjlj,div){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url=$('#khglxxForm #urlPrefix').val()+"/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
                        });
                        $("#khglxxForm"+"  #"+div).remove();
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);
            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}
/*获取省份id*/
function getsf() {
    if (formAction=="addSaveKhgl") {
        var sf = document.getElementById("sf").value;
        $.ajax({
            type: "post",
            url: $("#khglxxForm #urlPrefix").val() + "/storehouse/khgl/pagedataGetKhdm",
            data: {"sf": sf, "access_token": $("#ac_tk").val()},
            success: function (data) {
                if (data.khdm == "" || data.khdm == null) {
                    $.error("该省份未维护参数扩展2");
                    return;
                }
                var cskz2 = $("#khglxxForm #sf").find("option:selected").attr("cskz2");
                $("#khglxxForm #sfdm").val(cskz2)
                $("#khglxxForm #khdm").val(data.khdm)
            },
        });
    }
}
function getbizmc() {
    var bizmc=$("#khglxxForm #biz").find("option:selected").attr("csmc");
    $("#khglxxForm #bizmc").val(bizmc)
}
function getkhgllxdm() {
    var khgllxdm = $("#khglxxForm #khgllx").find("option:selected").attr("csdm");
    $("#khglxxForm #khgllxdm").val(khgllxdm)
}
//打开文件上传界面
function editfile(divName,btnName){
    $("#khglxxForm"+"  #"+btnName).hide();
    $("#khglxxForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#khglxxForm"+"  #"+btnName).show();
    $("#khglxxForm"+"  #"+divName).hide();
}
function displayUpInfo(fjid){
    if(!$("#khglxxForm #fjids").val()){
        $("#khglxxForm #fjids").val(fjid);
    }else{
        $("#khglxxForm #fjids").val($("#khglxxForm #fjids").val()+","+fjid);
    }
}


