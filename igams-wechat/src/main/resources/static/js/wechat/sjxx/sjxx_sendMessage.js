$("#sendMessageForm #fslx").bind('change',function(){
	var bt=$("#sendMessageForm #fslx").find("option:selected").attr("cskz1");
	var nr=$("#sendMessageForm #fslx").find("option:selected").attr("cskz2");
	
	$("#sendMessageForm #bt").html("");
	$("#sendMessageForm #nr").html("");
	$("#sendMessageForm #bt").val(bt);
	$("#sendMessageForm #nr").text(nr);
})

$(function(){
	//所有下拉框添加choose样式
	jQuery('#sendMessageForm .chosen-select').chosen({width: '100%'});
})