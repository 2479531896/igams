$(document).ready(function(){
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});
//打开文件上传界面
function editfile(divName,btnName){
    $("#addUpdateXxxjForm"+"  #"+btnName).hide();
    $("#addUpdateXxxjForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#addUpdateXxxjForm"+"  #"+btnName).show();
    $("#addUpdateXxxjForm"+"  #"+divName).hide();
}
function displayUpInfo(fjid){
    if(!$("#addUpdateXxxjForm #fjids").val()){
        $("#addUpdateXxxjForm #fjids").val(fjid);
    }else{
        $("#addUpdateXxxjForm #fjids").val($("#addDetectionApplicationForm #fjids").val()+","+fjid);
    }

}

function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= "/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
                        });
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

function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url="/ws/sjxxpripreview?pageflg=1&fjid="+fjid
        window.open(url);
    }else if(type.toLowerCase()==".pdf"){
        var url="/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
        window.open(url);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

$(function () {
    var oFileInput = new FileInput();
    oFileInput.Init("addUpdateXxxjForm","displayUpInfo",2,1,"sign_file");

})
