
$(function(){
    var oFileInput = new FileInput();
	oFileInput.Init("poolingUploadForm","displayUpInfo",2,1,"pro_file",$("#poolingUploadForm #ywlx").val());
});

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

function displayUpInfo(fjid){
	if(!$("#poolingUploadForm #fjids").val()){
		$("#poolingUploadForm #fjids").val(fjid);
	}else{
		$("#poolingUploadForm #fjids").val($("#poolingUploadForm #fjids").val()+","+fjid);
	}
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