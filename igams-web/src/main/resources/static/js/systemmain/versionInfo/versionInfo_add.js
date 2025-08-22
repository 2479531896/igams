//事件绑定
function btnBind(){
}

function initPage(){
}

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#addVersionInfoForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	
});

/**
 * 初始化页面
 * @returns
 */
function initPage(){

	//添加日期控件 
	laydate.render({
	   elem: '#addVersionInfoForm #sxsj'
	  /*,theme: '#2381E9'*/
	   ,type: 'datetime'
	   ,ready: function(date){
			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
				var myDate = new Date(); //实例一个时间对象；
				this.dateTime.hours=myDate.getHours();
				this.dateTime.minutes=myDate.getMinutes();
	        	this.dateTime.seconds=myDate.getSeconds();
			}
		}
	});	
}
/**
 * 选择操作人员列表
 * @returns
 */
function chooseCzry(){
	var url="/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择操作人员',addCzryConfig);	
}
var addCzryConfig = {
	width		: "800px",
	modalName	:"addCzryModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
//					$('#addVersionInfoForm #czryid').val(sel_row[0].yhid);
					$('#addVersionInfoForm #czry').val(sel_row[0].zsxm);
					//保存操作习惯
/*					$.ajax({
					    type:'post',  
					    url:$('#addVersionInfoForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
					    cache: false,
					    data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},  
					    dataType:'json', 
					    success:function(data){}
					});*/
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
