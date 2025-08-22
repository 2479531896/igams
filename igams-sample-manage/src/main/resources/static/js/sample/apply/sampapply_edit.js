/**
 * 绑定按钮事件
 */
function btnBind(){
	
	var inp_sqs = $("#ajaxForm #sqs");
	//数量改变事件
	inp_sqs.unbind("onblur").blur(function(){
		sqsBlurEvent();
	});
}

function chooseJg() {
	var url = "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择机构', chooseJgConfig);
}
var chooseJgConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseJgModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#listDepartmentForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#ajaxForm #jgid').val(sel_row[0].jgid);
					$('#ajaxForm #jgmc').val(sel_row[0].jgmc);
					$.closeModal(opts.modalName);
	    		}else{
	    			$.error("请选中一行");
	    			return;
	    		}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 数量改变事件
 */
function sqsBlurEvent(){
	var sqs = $("#ajaxForm #sqs").val();
	if(sqs == null || sqs==""){
		return;
	}
}

$(document).ready(function(){
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	
	btnBind();
});