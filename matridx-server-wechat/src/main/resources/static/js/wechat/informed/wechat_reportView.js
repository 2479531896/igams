//事件绑定
function btnBind() {
	
}
function initPage(){
}
function view(fjid,sign){
	$("#ajaxForm #fjid").val(fjid);
	$("#ajaxForm #sign").val(sign);
	$("#ajaxForm").attr("action","/wechat/file/pdfPreview");
	document.getElementById("ajaxForm").submit();
}

function xz(fjid,sign){
    //判断是否为微信环境
    var ua = navigator.userAgent.toLowerCase();
    if (ua.indexOf('micromessenger') != -1) {
        // 打开引导页
        window.location.href = "/wechat/informed/downloadGuide?fjid=" + fjid + "&sign=" + encodeURIComponent(sign) + "&action=/wechat/file/loadFile";
    }else{
        //原本的代码
        $("#ajaxForm #fjid").val(fjid);
        $("#ajaxForm #sign").val(decodeURIComponent(sign));
        $("#ajaxForm").attr("action","/wechat/file/loadFile");
        $("#ajaxForm").submit();
    }
}
function yl(fjid,wjm,sign){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg"|| type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"  ){
		var url="/wechat/wechatpreview?fjid="+fjid+"&sign="+encodeURIComponent(sign);
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else{
		$.alert("仅支持图片预览");
	}
}
var JPGMaterConfig = {
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function initSfqx(){
	var sfqx = $("#ajaxForm #sfqx").val();
	if(sfqx != "1"){
		$(".sfqx").remove();
	}
}

$(document).ready(function(){
	//初始化收费显示
	// initSfqx();
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});