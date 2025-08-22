/**
 * 绑定按钮事件
 */
function btnBind(){
}

function init(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #pzsj'
	  ,theme: '#2381E9'
	});
}

var vm = new Vue({
	el:'#document_modtime',
	data: {
	},
	methods:{
	}
})

$(document).ready(function(){
	//所有下拉框添加choose样式
	jQuery('.chosen-select').chosen({width: '100%'});
	btnBind();
	init()
});