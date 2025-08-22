//事件绑定
function btnBind(){
}

function initPage(){
}

$(document).ready(function(){
	$('#genre_ajaxForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	if($("#sfqxxk").val()==1){
		$('#qxxk').bootstrapSwitch('state', true);
	}else{
		$('#qxxk').bootstrapSwitch('state', false);
	}
    //所有下拉框添加choose样式
	jQuery('#genre_ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});