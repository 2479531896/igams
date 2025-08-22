//事件绑定
function btnBind(){
}

function initPage(){
	var cskz=$("#ajaxForm #mt_lb").find("option:selected").attr("cskz");
	if(cskz=="0"){
		$("#ajaxForm #lbdm").val(null);
	}
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
	if(!/^\d+(\.\d{1,2})?$/.test(value)){
		$.error("请填写正确安全库存格式，可保留两位小数!");
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});

$("#ajaxForm #mt_lb").change(function(){
	var cskz=$("#ajaxForm #mt_lb").find("option:selected").attr("cskz");
	var csdm=$("#ajaxForm #mt_lb").find("option:selected").attr("csdm");
	if(cskz=="0"){
		$("#ajaxForm #lbdm").val(null);
	}else{
		$("#ajaxForm #lbdm").val(csdm); 
	}	
});