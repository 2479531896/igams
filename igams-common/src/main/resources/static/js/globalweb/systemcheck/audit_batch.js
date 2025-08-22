var batchAuditBtnBind = function(){
	//是否通过选择
	jQuery("#batchAuditAjaxForm #sftg").change(function(){
		var _this = $(this);
		var _value = _this.val()=="0";
		//审核不通过
		if(_value){
			$("#batchAuditAjaxForm #shjd").attr("style","display:block;");
			$("#batchAuditAjaxForm #shxxThlcxh").attr("validate","{required:true}");
		}else{
			//退回步骤不显示
			$("#batchAuditAjaxForm #shjd").attr("style","display:none;");
			$("#batchAuditAjaxForm #shxxThlcxh").removeAttr("validate");
		}
	}).trigger("change");
	//
	if($("#batchAuditAjaxForm #sftg").val() == "0"){
		$("#batchAuditAjaxForm #shjd").attr("style","display:block;");
		$("#batchAuditAjaxForm #shxxThlcxh").attr("validate","{required:true}");
	}else{
		$("#batchAuditAjaxForm #shjd").attr("style","display:none;");
		$("#batchAuditAjaxForm #shxxThlcxh").removeAttr("validate");
	}
	
}

function getQwwcsj(){
	//默认期望完成日期
	var qwwcsj = new Date();
	qwwcsj.setDate(qwwcsj.getDate()+3);//获取3天后的日期 
	y = qwwcsj.getFullYear();
	m = qwwcsj.getMonth() + 1;
	d = qwwcsj.getDate();
	qwwcsj = y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d) + " " + qwwcsj.toTimeString().substr(0, 8);
	$("#batchAuditAjaxForm #qwwcsj").val(qwwcsj);
	//根据审核类别判断是否显示期望时间
	if(1 > 0){
		$("#batchAuditAjaxForm #qwwc").remove();
	}
//	if($("#batchAuditAjaxForm #shlb").val() != 'QUALITY_FILE_MOD'&& $("#batchAuditAjaxForm #shlb").val() != 'QUALITY_FILE_ADD'){
//		$("#batchAuditAjaxForm #qwwc").remove();
//	}
	//获取当前系统时间戳来保证唯一性
	var timestamp = new Date().getTime();
	$("#batchAuditAjaxForm #batchaudit_loadYmCode").val(timestamp);
}
jQuery(function(){
	if($("#batchAuditAjaxForm #urlPrefix").val()+'/systemcheck/auditProcess/batchaudit'!=$("#business_url").val()){
		var data = {};
		data["ywids"] = $("#ywids").val();
		data["access_token"] = $("#ac_tk").val();
		$.ajax({
			type : "post",
			url: $("#business_url").val(),
			data : data,
			dataType : "html",
			success : function(data){
				var formhtml = $("#auditPage");
				data = data.replace('<form',"<div");
				data = data.replace('/form>',"/div>");
				formhtml.replaceWith(data)
			}
		});
	}
	//所有下拉框添加choose样式
	jQuery('.chosen-select').chosen({width: '100%'});
	batchAuditBtnBind();
	getQwwcsj();
});