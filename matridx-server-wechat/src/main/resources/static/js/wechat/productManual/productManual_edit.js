//打开文件上传界面
function editfile(divName,btnName){
    $("#productManualForm"+"  #"+btnName).hide();
    $("#productManualForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#productManualForm"+"  #"+btnName).show();
    $("#productManualForm"+"  #"+divName).hide();
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
        var url=$("#productManualForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#productManualForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action='+$("#productManualForm #urlPrefix").val()+'"/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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


function displayUpInfo(fjid){
    if(!$("#productManualForm #fjids").val()){
        $("#productManualForm #fjids").val(fjid);
    }else{
        $("#productManualForm #fjids").val($("#productManualForm #fjids").val()+","+fjid);
    }
}
$(function(){

    //0.初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("productManualForm","displayUpInfo",2,1,"sign_file",null);
    //所有下拉框添加choose样式
    jQuery('#productManualForm .chosen-select').chosen({width: '100%'});
    var fjid=$("#productManualForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#productManualForm #ycfj").show();
        $("#productManualForm #fjsc").hide();
    }else{
        $("#productManualForm #ycfj").hide();
        $("#productManualForm #fjsc").show();
    }
    //添加日期控件
    laydate.render({
        elem: '#productManualForm #gxsj'
        ,theme: '#2381E9'
        ,trigger: 'click'
    });
});