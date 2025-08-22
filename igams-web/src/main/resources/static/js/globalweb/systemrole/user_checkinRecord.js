//事件绑定
function btnBind(){
}

function initPage(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #kssj'
	  ,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #jssj'
	  ,theme: '#2381E9'
	});
}

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});