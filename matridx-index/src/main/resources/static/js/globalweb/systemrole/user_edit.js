//事件绑定
function btnBind(){
}

function initPage(){
	if($('#sfsd').prop("checked"))
		$('#sfsd').bootstrapSwitch('state', true);
	else
		$('#sfsd').bootstrapSwitch('state', false);
	var chk_jsid =$('#user_ajaxForm input[name="jsids"]');
	$.each(chk_jsid,function(i){
		if($(chk_jsid[i]).prop("checked"))
			$(chk_jsid[i]).bootstrapSwitch('state', true);
	})
}

$(document).ready(function(){
	//$('#user_ajaxForm input[type="checkbox"]').bootstrapSwitch();
	//基本初始化
    $('#user_ajaxForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
});