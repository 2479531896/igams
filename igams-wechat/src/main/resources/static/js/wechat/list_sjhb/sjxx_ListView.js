//事件绑定
function btnBind() {
	
}
function initPage(){
}
function view(fjid){
	$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
	var url= "/common/file/pdfPreview?fjid=" + fjid;
    $.showDialog(url,'送检报告',PdfMaterConfig);
}
$("#mzsm").click(function(){
	var url="/inspection/sjxx/pagedataMzsm?access_token="+$("#ac_tk").val();
	$.showDialog(url,'免责声明',MzsmConfig);
})
$(".ckzbjs").click(function(){
	var url = "/inspection/sjxx/getckzbjs?access_token="+$("#ac_tk").val();
	$.showDialog(url,'参考指标解释',CkzbjsConfig);
})


function toswxx(){
	var xpxx=$("#ww").val();
	var url=$("#ajaxForm #bioaudurl").val();
	window.open(url + "/core/review/"+xpxx+"/");
}
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

function xzword(fjid){
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
		if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"){
			var url="/ws/sjxxpripreview?fjid="+fjid
			$.showDialog(url,'图片预览',JPGMaterConfig);
		}else if(type.toLowerCase()==".pdf"){
			var url= "/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreViewConfig);
		}else {
			$.alert("暂不支持其他文件的预览，敬请期待！");
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
function toaddbr(){
	var ggzbsmxx=$("#gg").text();
	var reg = new RegExp("\n","g");
	var ggzbsm=ggzbsmxx.replace(reg,"<br><br>");
	$("#gg").html(ggzbsm);
	var ckwxxx=$("#ckwx").text();
	var ckwx=ckwxxx.replace(reg,"<br>");
	$("#ckwx").html(ckwx);
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