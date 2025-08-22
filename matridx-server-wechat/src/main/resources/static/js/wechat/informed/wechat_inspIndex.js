$(function (){
	//快捷录入
	$("#inspIndex #fast_input").unbind("click").click(function(){
		$("#inspIndex #fast_input").attr("disabled","true");
		window.setTimeout(function(){
			$("#inspIndex #fast_input").removeAttr("disabled");
		}, 1000);
		$("#inspIndex").attr("action", "/wechat/inspFastReport");
		document.getElementById("inspIndex").submit();
		return true;
	});
	//正常录入
	$("#inspIndex #normal_input").unbind("click").click(function(){
		$("#inspIndex #normal_input").attr("disabled","true");
		window.setTimeout(function(){
			$("#inspIndex #normal_input").removeAttr("disabled");
		}, 1000);
		$("#inspIndex").attr("action", "/wechat/inspReport");
		document.getElementById("inspIndex").submit();
		return true;
	});
	//完善数据
	$("#inspIndex #perfect_input").unbind("click").click(function(){
		$("#inspIndex #perfect_input").attr("disabled","true");
		window.setTimeout(function(){
			$("#inspIndex #perfect_input").removeAttr("disabled");
		}, 1000);
		$("#inspIndex").attr("action", "/wechat/inspPerfectReport");
		document.getElementById("inspIndex").submit();
		return true;
	});
});
