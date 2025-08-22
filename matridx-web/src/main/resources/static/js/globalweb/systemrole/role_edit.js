//事件绑定
function btnBind(){
}

function initPage(){
	if($('#dwxdbj').prop("checked"))
		$('#dwxdbj').bootstrapSwitch('state', true);
	else
		$('#dwxdbj').bootstrapSwitch('state', false);

	//所有下拉框添加choose样式
	jQuery('#role_ajaxForm .chosen-select').chosen({width: '100%'});
}

$(document).ready(function(){
	//$('#user_ajaxForm input[type="checkbox"]').bootstrapSwitch();
	//基本初始化
    $('#role_ajaxForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
});