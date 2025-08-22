//事件绑定
function btnBind(){
}

var viewMaterCompareConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function view(wlid,index){
	var url = $('#view_ajaxForm #urlPrefix').val() + "/production/materiel/pagedataViewModMaterCompare?wlid="+wlid+"&extend_1="+index+"&access_token="+$("#ac_tk").val();
	$.showDialog(url,'查看物料',viewMaterCompareConfig);
}

function shzt(lsid,shlb){
	var url = $('#view_ajaxForm #urlPrefix').val() + "/systemcheck/auditProcess/auditView?ywid="+lsid+"&shlb="+shlb+"&access_token="+$("#ac_tk").val();
	$.showDialog(url,'查看审核',viewMaterCompareConfig);
}
function queryHqByWlid(wlid){
	$.showDialog($("#view_ajaxForm #urlPrefix").val()+"/ws/materiel/viewHqMater?wlid="+wlid+"&access_token="+$("#ac_tk").val(),'货期信息',viewHqConfig);
}
var viewHqConfig = {
	width		: "800px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function initPage(){
}

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});