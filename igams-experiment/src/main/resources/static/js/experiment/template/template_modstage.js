$("#fzrxz").click('on',function(){
	var url="/systemmain/task/pagedataCommonListUserPage";
	$.showDialog(url,'选择负责人',addCkxxConfig);
})
//负责人列表弹出框
var addCkxxConfig = {
	width		: "800px",
	modalName	:"addCkxxConfig",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row=$('#taskListFzrForm #tb_list').bootstrapTable('getSelections');
				if(sel_row.length==1){
					var yhid=sel_row[0].yhid;
					var zsxm=sel_row[0].zsxm;
					$("#mrfzr").val(zsxm);
					$("#fzrval").val(yhid);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};