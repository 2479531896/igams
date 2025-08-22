/**
 * 绑定按钮事件
 */
function btnBind(){

}

function init(){

}

function displayUpInfo(fjid){
    if(!$("#ajaxForm #fjids").val()){
        $("#ajaxForm #fjids").val(fjid);
    }else{
        $("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
    }
}

//下载模板
function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
            var url= "/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
                        });
                        $("#ajaxForm"+"  #"+div).remove();
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

$(document).ready(function(){
    //0.初始化fileinput
    var sign_params=[];
    sign_params.prefix=$('#ajaxForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("ajaxForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    btnBind();
    init();
    //所有下拉框添加choose样式
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});