$(document).ready(function(){
	if($("#editbx_ajaxForm #sblx").val()=="ct"){
		$("#editbx_ajaxForm #yblxdiv").removeClass("hidden");
		$("#editbx_ajaxForm #cfsdiv").empty();
		$("#editbx_ajaxForm #jcdwdiv").empty();
	}else if($("#editbx_ajaxForm #sblx").val()=="hz"){
		$("#editbx_ajaxForm #yblxdiv").empty();
		$("#editbx_ajaxForm #cfsdiv").removeClass("hidden");
		$("#editbx_ajaxForm #jcdwdiv").empty();
	}else{
		$("#editbx_ajaxForm #yblxdiv").empty();
		$("#editbx_ajaxForm #cfsdiv").empty();
		$("#editbx_ajaxForm #jcdwdiv").removeClass("hidden");
	}
	//所有下拉框添加choose样式
	jQuery('.chosen-select').chosen({width: '100%'});
});