/**
 * 绑定按钮事件
 */
function btnBind(){

}

function init(){
}

// function displayUpInfo(fjid){
//     if(!$("#ajaxForm #fjids").val()){
//         $("#ajaxForm #fjids").val(fjid);
//     }else{
//         $("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
//     }
// }
function displayUpInfo(fjid){
    if(!$("#ajaxForm #fjids").val()){
        $("#ajaxForm #fjids").val(fjid);
    }else{
        $("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
    }
}
//下载模板
function xz(fjid){
    var ss=$("#ac_tk").val();
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url="/ws/sjxxpripreview/?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',viewPreViewConfig);
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

var viewPreViewConfig={
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
$(document).ready(function(){
    $('.str_div').each(function () {
        $("#ajaxForm #str_div"+this.id).html($("#ajaxForm #"+this.id).val());
    });

    //0.初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("ajaxForm","displayUpInfo",2,1,"pro_file");
    btnBind();
    init();
    //所有下拉框添加choose样式
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});


function showInfo(value){
    var url="/express/express/pagedataExpress?mailno="+value+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物流详情',viewExpressConfig);
}
/*查看详情信息模态框*/
var viewExpressConfig = {
    width		: "800px",
    height		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};