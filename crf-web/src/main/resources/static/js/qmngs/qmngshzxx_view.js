
//下载模板
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}

function changeHide(yzType) {
	if (yzType == 'ADULT') {
		$("#dayThreeLi").show();
		$("#dayFiveLi").show();
		//隐藏
	}else if(yzType == 'IMMUNOSUPPRESSION'){
		$("#dayThreeLi").show();
		$("#dayFiveLi").show();
	}else if(yzType == 'CHILD'){
		$("#dayThreeLi").hide();
		$("#dayFiveLi").hide();
	}else if(yzType == 'NON_INFECTION'){
		$("#dayThreeLi").hide();
		$("#dayFiveLi").hide();
	}
}
$(document).ready(function() {
	
 var cT = $('#yzjs').val();
	//由各种类型显示不同页面
	console.log("12");
   changeHide(cT);
});