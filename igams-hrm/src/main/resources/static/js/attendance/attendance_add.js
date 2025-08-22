
function btnBind(){
	
}

function initPage(){
	
}

$(document).ready(function(){
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
	//所有下拉框添加choose样式
	jQuery('#Attendance_ajaxForm .chosen-select').chosen({width: '100%'});
});