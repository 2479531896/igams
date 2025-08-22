//事件绑定
function btnBind(){
}

function initPage(){
}

function view(fjid,hzm){
	if(hzm.toLowerCase()=="pdf"){
		var url=$("#ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',viewPreViewConfig);
	}else if(hzm.toLowerCase()=="jpg" || hzm.toLowerCase()=="jpeg" || hzm.toLowerCase()=="jfif" || hzm.toLowerCase()=="png"){
		var url=$("#ajaxForm #urlPrefix").val()+"/ws/pripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(hzm.toLowerCase()=="doc" || hzm.toLowerCase()=="docx" || hzm.toLowerCase()=="xls"|| hzm.toLowerCase()=="xlsx"){
		var url=$("#ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid +"&pdf_flg="+"1";
		$.showDialog(url,'文件预览',viewPreViewConfig);
	}else{
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
	
}
function xz(fjid){
	jQuery('<form action="'+$("#ajaxForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

var viewPreViewConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});