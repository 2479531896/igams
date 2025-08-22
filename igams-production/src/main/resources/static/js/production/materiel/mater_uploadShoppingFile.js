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

function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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

function xz(fjid){
    jQuery('<form action="'+$("#ajaxForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#ajaxForm #urlPrefix").val()+"/common/file/delFile";
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
	var wlid = $("#ajaxForm #wlid").val();
	var index = $("#ajaxForm #index").val();
	var fjids = $("#shoppingCarForm #fj_"+index+" input").val();
	var arr = fjids.split(",");
	if(arr.length > 0){
		var ids = "";
		for (var i = 0; i < arr.length; i++) {
			if(arr[i] != fjid){
				ids = ids + "," + arr[i];
			}
		}
		ids = ids.substr(1);
		$("#shoppingCarForm #fj_"+index+" input").val(ids);
		if(ids.length == 0){
			// 判断正式附件是否为空
			var dom = $("#ajaxForm div[name='formalFile']").html();
			if(dom == null || dom == "" || dom == undefined){
			    $("#shoppingCarForm #fj_"+index+" a").text("否");
			}
		}
		// 更新临时复件信息到json
		var data = JSON.parse($("#shoppingCarForm #qgmx_json").val())
		for(var i=0;i<data.length;i++){
			if(data[i].wlid == wlid){
				data[i].fjids = $("#shoppingCarForm #fj_"+index+" input").val();
			}
		}
		$("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
		$("#"+fjid).remove();
	}
}

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}

$(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var uploadShopping_params = [];
	uploadShopping_params.prefix = $("#ajaxForm #urlPrefix").val();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"pro_file",null,uploadShopping_params);
})