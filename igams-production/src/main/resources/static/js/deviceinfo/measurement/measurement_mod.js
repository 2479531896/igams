
function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var JPGMaterConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

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

function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
//点击文件上传
function showfile(fileDiv,file_btn){
	$("#"+fileDiv).show();
	$("#"+file_btn).hide();
}
//点击隐藏文件上传
function hidefile(fileDiv,file_btn){
	$("#"+file_btn).show();
	$("#"+fileDiv).hide();
}

function displayUpInfo(fjid){
	if(!$("#measurement_mod_Form #fjids").val()){
		$("#measurement_mod_Form #fjids").val(fjid);
	}else{
		$("#measurement_mod_Form #fjids").val($("#measurement_mod_Form #fjids").val()+","+fjid);
	}
}

$(function(){
	
	var oFileInput = new FileInput();
	oFileInput.Init("measurement_mod_Form","displayUpInfo",2,1,"measure_file",$("#measurement_mod_Form #ywlx").val());
	
	
	laydate.render({
	 	  elem: '#measurement_mod_Form #jdrq'
	 });
	laydate.render({
	 	  elem: '#measurement_mod_Form #yxrq'
	});
	laydate.render({
	 	  elem: '#measurement_mod_Form #jlksrq'
	});
	laydate.render({
	 	  elem: '#measurement_mod_Form #jljsrq'
	});
	laydate.render({
	 	  elem: '#measurement_mod_Form #yjjsrq'
	});
	
	jQuery('#measurement_mod_Form .chosen-select').chosen({width: '100%'});
	
})