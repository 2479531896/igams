//事件绑定
function btnBind(){
}

function initPage(){
	setTimeout("checkCzdm()",100);
}

function checkCzdm(){
	var chk_czdm =$('#listSet_ajaxForm input[name="czdms"]');
	$.each(chk_czdm,function(i){
		if($(chk_czdm[i]).prop("checked")){
			$(chk_czdm[i]).bootstrapSwitch('state', true);
		}
	});
}

$(document).ready(function(){
    //所有下拉框添加choose样式
//	jQuery('#listSet_ajaxForm .chosen-select').chosen({width: '100%'});
	//初始化复选框
	$('#listSet_ajaxForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	checkCzdm();
});