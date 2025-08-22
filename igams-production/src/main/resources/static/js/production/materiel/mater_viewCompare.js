//事件绑定
function btnBind(){
}

/**
 * 判断数据修改
 */
function textMod(){
	if($("#ajaxForm #wl_wllb").val()){
		if($("#ajaxForm #wl_wllb").val() != $("#ajaxForm #wllb").val()){
			$("#ajaxForm #lable_wllb").css("color","red");
		}
		if($("#ajaxForm #wl_wlzlb").val() != $("#ajaxForm #wlzlb").val()){
			$("#ajaxForm #lable_wlzlb").css("color","red");
		}
		if($("#ajaxForm #wl_wlzlbtc").val() != $("#ajaxForm #wlzlbtc").val()){
			$("#ajaxForm #lable_wlzlbtc").css("color","red");
		}
		if($("#ajaxForm #wl_wlbm").val() != $("#ajaxForm #wlbm").text()){
			$("#ajaxForm #lable_wlbm").css("color","red");
		}
		if($("#ajaxForm #wl_wlmc").val() != $("#ajaxForm #wlmc").text()){
			$("#ajaxForm #lable_wlmc").css("color","red");
		}
		if($("#ajaxForm #wl_dlgys").val() != $("#ajaxForm #dlgys").text()){
			$("#ajaxForm #lable_dlgys").css("color","red");
		}
		if($("#ajaxForm #wl_scs").val() != $("#ajaxForm #scs").text()){
			$("#ajaxForm #lable_scs").css("color","red");
		}
		if($("#ajaxForm #wl_ychh").val() != $("#ajaxForm #ychh").text()){
			$("#ajaxForm #lable_ychh").css("color","red");
		}
		if($("#ajaxForm #wl_gg").val() != $("#ajaxForm #gg").text()){
			$("#ajaxForm #lable_gg").css("color","red");
		}
		if($("#ajaxForm #wl_jldw").val() != $("#ajaxForm #jldw").text()){
			$("#ajaxForm #lable_jldw").css("color","red");
		}
		if($("#ajaxForm #wl_bctj").val() != $("#ajaxForm #bctj").text()){
			$("#ajaxForm #lable_bctj").css("color","red");
		}
		if($("#ajaxForm #wl_bzq").val() != $("#ajaxForm #bzq").text()){
			$("#ajaxForm #lable_bzq").css("color","red");
		}
		if($("#ajaxForm #wl_sfwxp").val() != $("#ajaxForm #sfwxp").val()){
			$("#ajaxForm #lable_sfwxp").css("color","red");
		}
		if($("#ajaxForm #wl_jwlbm").val() != $("#ajaxForm #jwlbm").text()){
			$("#ajaxForm #lable_jwlbm").css("color","red");
		}
		if($("#ajaxForm #wl_bz").val() != $("#ajaxForm #bz").text()){
			$("#ajaxForm #lable_bz").css("color","red");
		}
		if($("#ajaxForm #wl_scbj").val() != $("#ajaxForm #scbj").val()){
			$("#ajaxForm #lable_scbj").css("color","red");
		}
		if($("#ajaxForm #wl_rf").val() != $("#ajaxForm #rf").val()){
			$("#ajaxForm #lable_rf").css("color","red");
		}
	}
}

function initPage(){
}

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	textMod();
});