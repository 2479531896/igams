
//打开文件上传界面
function editfile(divName,btnName){
    $("#reviewProgressForm"+"  #"+btnName).hide();
    $("#reviewProgressForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#reviewProgressForm"+"  #"+btnName).show();
    $("#reviewProgressForm"+"  #"+divName).hide();
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#reviewProgressForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#reviewProgressForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

//下载模板
function xz(fjid){
    jQuery('<form action="'+$("#reviewProgressForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
            var url= $("#reviewProgressForm #urlPrefix").val()+"/common/file/delFile";
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


function displayUpInfo(fjid){

    if(!$("#reviewProgressForm #fjids").val()){
        $("#reviewProgressForm #fjids").val(fjid);
    }else{
        $("#reviewProgressForm #fjids").val($("#reviewProgressForm #fjids").val()+","+fjid);
    }
}
$(function(){

    //0.初始化fileinput
    var sign_params=[];
    sign_params.prefix=$('#reviewProgressForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("reviewProgressForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    //所有下拉框添加choose样式
    jQuery('#reviewProgressForm .chosen-select').chosen({width: '100%'});
    var fjid=$("#reviewProgressForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#reviewProgressForm #ycfj").show();
        $("#reviewProgressForm #fjsc").hide();
    }else{
        $("#reviewProgressForm #ycfj").hide();
        $("#reviewProgressForm #fjsc").show();
    }
});