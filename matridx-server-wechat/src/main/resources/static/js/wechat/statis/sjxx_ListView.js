//事件绑定
function btnBind() {
	
}
function initPage(){
}
function view(fjid, sign){
	var url= "/wechat/statistics/pdfPreview?fjid=" + fjid+"&sign="+encodeURIComponent(sign);
    $.showDialog(url,'送检报告',PdfMaterConfig);
}

$(".ckzbjs").click(function(){
	var url = "/wechat/getckzbjs";
	$.showDialog(url,'参考指标解释',CkzbjsConfig);
})

//function toswxx(){
//	var xpxx=$("#ww").val();
//	window.open("http://172.17.60.226:8000/core/review/"+xpxx+"/");
//}

function yl(fjid,wjm, sign){
		var begin=wjm.lastIndexOf(".");
		var end=wjm.length;
		var type=wjm.substring(begin,end);
		if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif" || type.toLowerCase()==".png"){
			var url="/wechat/wechatpreview/?fjid="+fjid+"&sign="+encodeURIComponent(sign);
			$.showDialog(url,'图片预览',JPGMaterConfig);
		}else if(type.toLowerCase()==".pdf"){
			var url= "/wechat/statistics/pdfPreview?fjid=" + fjid+"&sign="+encodeURIComponent(sign);
		    $.showDialog(url,'送检报告',PdfMaterConfig);
		}else{
			$.alert("仅支持图片和pdf预览！");
		}
}
var PdfMaterConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var MzsmConfig = {
//	width		: "800px",
//	height		: "500px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
};

var CkzbjsConfig = {
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var JPGMaterConfig = {
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
function toaddbr(){
	var ggzbsmxx=$("#gg").text();
	var reg = new RegExp("\n","g");
	var ggzbsm=ggzbsmxx.replace(reg,"<br><br>");
	$("#gg").html(ggzbsm);
	var ckwxxx=$("#ckwx").text();
	var ckwx=ckwxxx.replace(reg,"<br>");
	$("#ckwx").html(ckwx);
}
function sjxx_back_view(){
	var jyjg=$("#ajaxForm #jyjg").val();
	var lxmc=null;
	if(jyjg=="1") {
		lxmc="阳性";
	}else if(jyjg=="0") {
		lxmc="阴性";
	}else if(jyjg=="2") {
		lxmc="阴性";
	}
	$("#daliyStatis").load("/wechat/statistics/positive_PageList?bgrq="+$("#ajaxForm #bgrq").val()+"&lxmc="+lxmc+"&jyjg="+$("#ajaxForm #jyjg").val()+"&jsrq="+$("#ajaxForm #bgrq").val()+"&load_flg="+$("#ajaxForm #load_flg").val());
}
$(document).ready(function(){
	toaddbr();
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	//获取年龄信息 判断是否显示年龄单位
	var nlxx=$("#nl").text();
	if(/^[0-9]+$/.test(nlxx)){
		$("#nldw").show();
	}else{
		$("#nldw").hide();
	}
	
});