//点击隐藏文件上传
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}
//点击文件上传
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif" || type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var viewPreViewConfig = {
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
			var url= "/experimentS/experimentS/pagedataRemoveFj";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"]){
						$.success("操作成功！",function() {
							$("#"+fjid).remove();
						});
					}else if(responseText["status"]){
						$.error("操作失败！",function() {
						});
					} else{
						$.alert("系统异常",function() {
						});
					}
				},1);
			},'json');
			jQuery.ajaxSetup({async:true});
		}
	});
}



function displayUpInfo(fjid){
	if(!$("#uploadFileAjaxForm #fjids").val()){
		$("#uploadFileAjaxForm #fjids").val(fjid);
	}else{
		$("#uploadFileAjaxForm #fjids").val($("#uploadFileAjaxForm #fjids").val()+","+fjid);
	}
}

$(function(){
	var oFileInput = new FileInput();
	oFileInput.Init("uploadFileAjaxForm","displayUpInfo",2,1,"pro_file");

})
