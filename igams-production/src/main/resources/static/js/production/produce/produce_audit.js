
$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var picking_params=[];
	picking_params.prefix=$("#produce_audit_ajaxForm #urlPrefix").val();
	oFileInput.Init("produce_audit_ajaxForm","produceUpInfo",2,1,"pro_file",null,picking_params);
});


/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#produce_audit_ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid+"&ywid="+$("#produce_audit_ajaxForm #sczlmxid").val();
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#produce_audit_ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid+"&ywid="+$("#produce_audit_ajaxForm #sczlmxid").val();
		$.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function produceUpInfo(fjid){
	if(!$("#produce_audit_ajaxForm #fjids").val()){
		$("#produce_audit_ajaxForm #fjids").val(fjid);
	}else{
		$("#produce_audit_ajaxForm #fjids").val($("#produce_audit_ajaxForm #fjids").val()+","+fjid);
	}
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#produce_audit_ajaxForm #urlPrefix").val()+"/common/file/delFile";
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
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="'+$('#produce_audit_ajaxForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}