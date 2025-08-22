/**
 * 绑定按钮事件
 */
function btnBind(){

}

function init(){
	
}

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}

$(document).ready(function(){
	//0.初始化fileinput
	var sign_params=[];
	sign_params.prefix=$('#ajaxForm #urlPrefix').val();
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"sign_file",null,sign_params);
	btnBind();
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});