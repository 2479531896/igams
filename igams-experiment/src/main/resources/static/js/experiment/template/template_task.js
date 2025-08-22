//点击选择按钮，选择负责人列表
$("#selectMrfzr").click('on',function(){
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
					$("#fzrmc").val(zsxm);
					$("#mrfzr").val(yhid);
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

//点击文件上传
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
//点击隐藏文件上传
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}
var viewTemTaskConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function view(fjid){
	var url= "/common/file/pdfPreview?fjid=" + fjid;
    $.showDialog(url,'文件预览',viewTemTaskConfig);
    
}
function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},1);
			},'json');
			jQuery.ajaxSetup({async:true});
		}
	});
}

function displayUpInfo(fjid){
	if(!$("#taskForm #fjids").val()){
		$("#taskForm #fjids").val(fjid);
	}else{
		$("#taskForm #fjids").val($("#taskForm #fjids").val()+","+fjid);
	}
}

$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("taskForm","displayUpInfo",2,1,"tem_file");
    //所有下拉框添加choose样式
	jQuery('#taskForm .chosen-select').chosen({width: '100%'});
});