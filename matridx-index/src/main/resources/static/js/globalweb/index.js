$(function(){
	//跳转研发送检系统
	$("#xt0").click(function(){
		var access_token=$("#ac_tk").val();
		window.open("http://172.17.60.191/index?access_token="+access_token);  
	})
	//跳转公司内部管理
	$("#xt1").click(function(){
		var access_token=$("#ac_tk").val();
		window.open("http://172.17.60.226:8000/core/report?access_token="+access_token);  
	})
	//跳转生信部系统
	$("#xt2").click(function(){
		var access_token=$("#ac_tk").val();
		window.open("http://172.17.60.190/index?access_token="+access_token); 
	})
	$(".xt").hover(function(){
		var _this = $(this);
        var liIndex = $(this).index();
        var str="xt"+liIndex;
		$("#"+str).addClass("xt-box-shadow");
	},function(){
		$(".xt").removeClass("xt-box-shadow");
	})
	setTimeout(function(){
		$("#welcome").attr("style","display:block;");
		$("#welcome").addClass("animated fadeInLeft");
	},500)
	setTimeout(function(){
		$(".xt").fadeIn("3000");
	},1000)
})
function dealSpaceQuery(idStr){
	// debugger
	//去除字符中所有空格
	// $(idStr).val($(idStr).val().replaceAll(" ",""));
	//截取字符串第一个空格之前的数据进行返回，，$(idStr).val().indexOf(" ")<=0，即不存在空格或第一个字符就是空格，此时不做处理，维持原字符串
	if ($(idStr).val().indexOf(" ") > 0){
		$(idStr).val(    $(idStr).val().substring( 0 , $(idStr).val().indexOf(" ") )    );
	}
}
window.onload = function () {
	if ($("#flag").val() == "checkRole")
	location.href = "/?access_token="+$("#ac_tk").val();
}
