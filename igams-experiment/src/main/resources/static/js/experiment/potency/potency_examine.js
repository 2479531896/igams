
var t_f_PreInput;
var t_f_SecondPreInput;
var t_f_ThreePreInput;
$("#examineDiv #title_first").on('keydown',function(e){
	var result = true;
	if(e.keyCode==13){
		var title_first=$("#examineDiv #title_first").val();
		$("#examineDiv #label_first").text(title_first);
		$("#examineDiv #title_second").focus();
		result=false;
	}else if(t_f_SecondPreInput==17 &&t_f_PreInput==16 && e.keyCode==74){
		if(t_f_ThreePreInput == 13)
			return false;
		var title_first=$("#examineDiv #title_first").val();
		$("#examineDiv #label_first").text(title_first);
		$("#examineDiv #title_second").focus();
		result=false;
	}
	t_f_ThreePreInput = t_f_SecondPreInput
	t_f_SecondPreInput = t_f_PreInput
	t_f_PreInput = e.keyCode
	if(!result)
		return result;
})
	
var t_s_PreInput;
var t_s_SecondPreInput;
var t_s_ThreePreInput;
$("#examineDiv #title_second").on('keydown',function(e){
	var result = true;
	if(e.keyCode==13){
		var title_second=$("#examineDiv #title_second").val();
		var title_first=$("#examineDiv #title_first").val();
		$("#examineDiv #label_second").text(title_second);
		if(title_second==title_first){
			$("#examineDiv #alert_div").html("");
			$("#examineDiv #alert_div").append("<div class='alert alert-success' " +
					"role='alert'><span class='glyphicon glyphicon-ok' aria-hidden='true' style='font-size: 14px;'></span>&nbsp;&nbsp;&nbsp;&nbsp;编码校验正确！</div");
			$("#examineDiv #title_first").val("");
			$("#examineDiv #title_second").val("");
			$("#examineDiv #title_first").focus();
			var audio = document.getElementById("zqMusic");
			audio.play();
		}else{ 
			$("#examineDiv #alert_div").html("");
			$("#examineDiv #alert_div").append("<div class='alert alert-danger' " +
					"role='alert'><span class='glyphicon glyphicon-remove' aria-hidden='true' style='font-size: 14px;'></span>&nbsp;&nbsp;编码校验错误！</div");
			$("#examineDiv #title_first").val("");
			$("#examineDiv #title_second").val("");
			$("#examineDiv #title_first").focus();
			var audio = document.getElementById("cwMusic");
			audio.play();
		}
		result=false;
	}else if(t_s_SecondPreInput==17 &&t_s_PreInput==16 && e.keyCode==74){
		if(t_s_ThreePreInput == 13)
			return false;
		var title_second=$("#examineDiv #title_second").val();
		var title_first=$("#examineDiv #title_first").val();
		$("#examineDiv #label_second").text(title_second);
		if(title_second==title_first){
			$("#examineDiv #alert_div").html("");
			$("#examineDiv #alert_div").append("<div class='alert alert-success' " +
					"role='alert'><span class='glyphicon glyphicon-ok' aria-hidden='true' style='font-size: 14px;'></span>&nbsp;&nbsp;&nbsp;&nbsp;编码校验正确！</div");
			$("#examineDiv #title_first").val("");
			$("#examineDiv #title_second").val("");
			$("#examineDiv #title_first").focus();
			var audio = document.getElementById("zqMusic");
			audio.play();
		}else{ 
			$("#examineDiv #alert_div").html("");
			$("#examineDiv #alert_div").append("<div class='alert alert-danger' " +
					"role='alert'><span class='glyphicon glyphicon-remove' aria-hidden='true' style='font-size: 14px;'></span>&nbsp;&nbsp;编码校验错误！</div");
			$("#examineDiv #title_first").val("");
			$("#examineDiv #title_second").val("");
			$("#examineDiv #title_first").focus();
			var audio = document.getElementById("cwMusic");
			audio.play();
		}
		result=false;
	}
	
	t_s_ThreePreInput = t_s_SecondPreInput
	t_s_SecondPreInput = t_s_PreInput
	t_s_PreInput = e.keyCode
	if(!result)
		return result;
})
/**
 * 页面加载时第一个输入框获取焦点
 * @returns
 */
$(function(){
	setTimeout(function(){
		$("#examineDiv #title_first").focus();
	},100);
	
})