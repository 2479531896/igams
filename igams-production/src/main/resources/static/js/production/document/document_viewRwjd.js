//事件绑定
function btnBind(){
}

var vm = new Vue({
	el:'#document_viewrwjd',
	data: {
	},
	methods: {
		qrzt(gzid){
			var url=$('#ajaxForm #urlPrefix').val() + "/systemmain/task/pagedataTaskHistory?gzid=" +gzid;
			$.showDialog(url,'查看任务历史进度',viewRwqrlsDocumentConfig);
		},
	}
})
var viewRwqrlsDocumentConfig = {
	width		: "900px",
	height		: "500px",
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