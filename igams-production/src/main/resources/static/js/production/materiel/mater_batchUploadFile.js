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

/**
 * 判断上传文件类型
 * @returns
 */
function onFile(){
	var file=document.getElementById('proqg_file');
	file.onchange = function() {
    	var path = this.value;
        var fix = path.substr(path.lastIndexOf('.'));
        var lowFix = fix.toLowerCase();
        if(lowFix !== '.zip'){
        	$.alert("请传入zip格式文件！");
        	$("#batchUploadForm .fileinput-remove-button").click();
        }  
    }
}

//function xz(fjid){
//    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
//            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
//            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
//        '</form>')
//    .appendTo('body').submit().remove();
//}
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#batchUploadForm #urlPrefix").val()+"/common/file/delFile";
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
/**
 * 删除临时文件
 * @param fjid
 * @returns
 */
function tempDel(fjid){
	var wlid = $("#batchUploadForm #wlid").val();
	var index = $("#batchUploadForm #index").val();
	var fjids = $("#shoppingCarForm #ysfjids").val();
	var arr = fjids.split(",");
	if(arr.length > 0){
		var ids = "";
		for (var i = 0; i < arr.length; i++) {
			if(arr[i] != fjid){
				ids = ids + "," + arr[i];
			}
		}
		ids = ids.substr(1);
		$("#shoppingCarForm #ysfjids").val(ids);
		$("#"+fjid).remove();
	}
}

function displayUpInfo(fjid){
	if(!$("#batchUploadForm #ysfjids").val()){
		$("#batchUploadForm #ysfjids").val(fjid);
	}else{
		$("#batchUploadForm #ysfjids").val($("#batchUploadForm #ysfjids").val()+","+fjid);
	}
}

$(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var batchUpload_params = [];
	batchUpload_params.prefix = $("#batchUploadForm #urlPrefix").val();
	oFileInput.Init("batchUploadForm","displayUpInfo",2,1,"proqg_file",null,batchUpload_params);
})