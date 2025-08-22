
function shlssz(){
	var ul_width=$("#auditAjaxForm .audit_steps").width()+30;
	var left_width = (400-ul_width)/2;
	$("#auditAjaxForm .audit_steps").css({marginLeft:left_width});
}
//$(function () { $('#collapseOne').collapse('toggle')});

$("#shjl").click(function(){
	if(!$("#auditAjaxForm").valid()){
		return false;
	}
	var getclass=$("#shjlmenu").attr("class");
	if(getclass.indexOf("open")!=-1){//折叠框展开状态
		$("#shjlmenu").removeClass("open");
		$("#shjlmenu").attr("class","glyphicon glyphicon-chevron-down shut");
	}else{
		$("#shjlmenu").removeClass("shut");
		$("#shjlmenu").attr("class","glyphicon glyphicon-chevron-up open");
	}
})

function applyloadCallback(response,status,xhr){
	var formhtml = $("#auditPage");
	var html = formhtml.get(0).outerHTML;
	response = response.replace('<form',"<div");
	response = response.replace('/form>',"/div>");
    formhtml.replaceWith(response)
}

jQuery(function(){
	shlssz();
	var data = {};
	data[$("#ywzd").val()] = $("#ywid").val();
	data["access_token"] = $("#ac_tk").val();

	//div加载页面
	$.ajax({
		type:"post",
		url: $('#auditAjaxForm #urlPrefix').val() + "/ws"+$("#business_url").val(),
		data: data,
		dataType: "html",
		success: function (data){
			var formhtml = $("#auditPage");
			var html = formhtml.get(0).outerHTML;
			data.replace('<form',"<div");
			data.replace('/form>',"/div>");
			formhtml.replaceWith(data);
		}
	})
	// $("#auditPage").load($('#auditAjaxForm #urlPrefix').val() + "/ws"+$("#business_url").val(),data,applyloadCallback);
});