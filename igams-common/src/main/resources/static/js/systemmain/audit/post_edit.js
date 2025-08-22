//事件绑定
function btnBind(){
}

function initPage(){
}

$(document).ready(function(){
    //所有下拉框添加choose样式
	$('#auditPost_ajaxForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	if($("#sfdwxz").val()==1){
		$('#dwxz').bootstrapSwitch('state', true);
	}else{
		$('#dwxz').bootstrapSwitch('state', false);
	}
	jQuery('#auditPost_ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});