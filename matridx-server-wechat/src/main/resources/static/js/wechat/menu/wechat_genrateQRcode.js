//事件绑定
function btnBind(){

}

function initPage(){
}

/**
 * 生成二维码
 */
function genrateQRcode(){
	$.ajax({
		url : '/menu/menu/generateQRcode',
		type : 'post',
		data : {
			"sceneStr" : $("#ajaxForm #sceneStr").val(),
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success : function(result) {
			if(result.status == "success"){
				$("#ajaxForm #ticketSpan").text(result.message);
				$("#ajaxForm #ticket").show();
				$("#ajaxForm #ticket").attr("href",result.ticket);
			}else{
				$.alert(result.message);
			}
		}
	});
}

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	$("#ajaxForm #ticket").hide();
});