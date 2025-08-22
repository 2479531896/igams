$(function(){
	var view_url = $("#view_url").val();
	if(!$("#view_url") || $("#view_url").val()==""){
		$.alert("未获取到显示的路径信息");
		return;
	}
	var code = $("#code").val();
	if(!code){
		setTimeout(function(){
			code = $("#code").val();
			$("#disdiv").load(view_url,{"code":code});
		}, 100);
	}else{
		$("#disdiv").load(view_url,{"code":code});
	}
});