//事件绑定
function btnBind() {
	
}
function initPage(){
}
function view(fjid){
	var url= "/ws/file/pdfPreview?fjid=" + fjid;
    $.showDialog(url,'送检报告',PdfMaterConfig);
}
/*$("#mzsm").click(function(){
	var url="/inspection/sjxx/getmzsm?access_token="+$("#ac_tk").val();
	$.showDialog(url,'免责声明',MzsmConfig);
})*/
$(".ckzbjs").click(function(){
	var url = "/ws/sjxx/getckzbjs";
	$.showDialog(url,'参考指标解释',CkzbjsConfig);
})


function toswxx(){
	var xpxx=$("#ww").val();
	var url=$("#ajaxForm #bioaudurl").val();
	window.open(url + "/core/review/"+xpxx+"/");
}
function xz(fjid){
	jQuery('<form action="/ws/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

function xzword(fjid){
	jQuery('<form action="/ws/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

function yl(fjid,wjm){
		var begin=wjm.lastIndexOf(".");
		var end=wjm.length;
		var type=wjm.substring(begin,end);
		if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif" || type.toLowerCase()==".png"){
			var url="/ws/pripreview/?fjid="+fjid
			$.showDialog(url,'图片预览',JPGMaterConfig);
		}else if(type.toLowerCase()==".pdf"){
			var url= "/ws/file/pdfPreview?fjid=" + fjid;
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
	var back_flg=$("#ajaxForm #back_flg").val();
	if(back_flg=="weekly"){
		$("#view_div").load("/ws/statistics/positive_PageList_back?bgrq="+$("#ajaxForm #bgrq").val()+"&jyjg="+$("#ajaxForm #jyjg").val()+"&load_flg="+$("#ajaxForm #load_flg").val()+"&jsrq="+$("#ajaxForm #bgrq").val());
	}else{
		$("#view_div").load("/ws/statistics/positive_PageList?bgrq="+$("#ajaxForm #bgrq").val()+"&jyjg="+$("#ajaxForm #jyjg").val()+"&load_flg="+$("#ajaxForm #load_flg").val()+"&jsrq="+$("#ajaxForm #bgrq").val());
	}
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