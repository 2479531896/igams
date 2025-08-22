$(function (){
	//正常录入
	$("#general_inspIndex #normal_input").unbind("click").click(function(){
		$("#general_inspIndex #normal_input").attr("disabled","true");
		window.setTimeout(function(){
			$("#general_inspIndex #normal_input").removeAttr("disabled");
		}, 1000);
		$("#general_inspIndex").attr("action", "/wechat/detectionPJ/generalInspectionInput");
		document.getElementById("general_inspIndex").submit();
		return true;
	});
	//完善数据
	$("#general_inspIndex #perfect_input").unbind("click").click(function(){
		$("#general_inspIndex #perfect_input").attr("disabled","true");
		window.setTimeout(function(){
			$("#general_inspIndex #perfect_input").removeAttr("disabled");
		}, 1000);
		$("#general_inspIndex").attr("action", "/wechat/detectionPJ/generalInspectionPerfect");
		document.getElementById("general_inspIndex").submit();
		return true;
	});
});
