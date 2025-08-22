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

function showTable(){
	$("#auditAjaxForm #yin_table").attr("style","table-layout: fixed;width: 80%;")
	$("#auditAjaxForm #show").attr("style","display: none")
	$("#auditAjaxForm #hide").attr("style","display: block")
}
function hideTable(){
	$("#auditAjaxForm #yin_table").attr("style","table-layout: fixed;width: 80%;display: none")
	$("#auditAjaxForm #show").attr("style","display: block")
	$("#auditAjaxForm #hide").attr("style","display: none")
}



function showViewInfo(id,flg) {
	if ($("#RFSAudit_audit_formSearch #btn_viewmore").length>0){
		RFS_audit_DealById(id,'viewmore',$("#RFSAudit_audit_formSearch #btn_viewmore").attr("tourl"),null,flg);
	}else if ($("#RFSAudit_audit_formSearch #btn_view").length>0){
		RFS_audit_DealById(id,'view',$("#RFSAudit_audit_formSearch #btn_view").attr("tourl"),null,flg);
	}
}


$(function(){
	jQuery('#auditAjaxForm .chosen-select').chosen({width: '100%'});
	if ($("#auditAjaxForm #lcxh").val() == "1" && $("#auditAjaxForm #shlb").val() == "AUDIT_RFS_FC"){
		$("#auditAjaxForm #showJcdw").attr("style","display: none;");
		$("#auditAjaxForm #hiddenJcdw").removeAttr("style");
		$("#auditAjaxForm #hiddenButton").removeAttr("style");
	}

})

function modfJcdw(){
	$.confirm('确定要修改当前检测单位?',function(result){
		if(result){
			var fjid=$("#auditAjaxForm #ywid").val();
			var jcdw=$("#auditAjaxForm #jcdw").val();
			var yjcdw=$("#auditAjaxForm #yjcdw").val();
			$.ajax({
				url:"/recheck/recheck/pagedataUpdateJcdw",
				type : "POST",
				data:{"fjid":fjid,"jcdw":jcdw,"yjcdw":yjcdw,"access_token":$("#ac_tk").val()},
				dataType:"json",
				success:function(data){
					if(data.status){
						$.success("修改成功！");
						$("#btn_cancel").click();
						searchRFS_audit_Result()
					}else{
						$.error("修改失败！");
					}
				}
			})
		}
	})
}


