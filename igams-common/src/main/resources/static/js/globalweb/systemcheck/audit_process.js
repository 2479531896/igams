var auditBtnBind = function(){
	//是否通过选择
	jQuery("#auditAjaxForm #sftg").change(function(){
		var _this = $(this);
		var _value = _this.val()=="0";
		var _div = jQuery("#shxxThlcxh");//退回步骤
		var _shyjEl = jQuery("#shxxShyj");//审核意见
		//审核不通过
		if(_value){
			_div.removeClass("hidden").filter("select").prop("disabled",false);
			_shyjEl.css("width","60%");
			var zffs=$("#shoppingCarForm #zffs");
			var fkf=$("#shoppingCarForm #fkf");
			var yzjgxxs=$("#verifyModAuditForm .yzjgxxs");

            if(yzjgxxs.length>0){
                yzjgxxs.removeAttr("validate");
                $("#verifyModAuditForm .yzjgxxs").attr("style","display:none;color: red;");
            }
			if(zffs.length>0){
				zffs.removeAttr("validate");
				$("#shoppingCarForm #zffsbt").attr("style","display:none;color: red;");
			}
			if(fkf.length>0){
				fkf.removeAttr("validate");
				$("#shoppingCarForm #fkfbt").attr("style","display:none;color: red;");
			}
		}else{
		    $("#verifyModAuditForm .yzjgxxs").attr("validate","{required:true}");
		    $("#shoppingCarForm #zffsbt").attr("style","color: red;");
			var zffs=$("#shoppingCarForm #zffs");
			var fkf=$("#shoppingCarForm #fkf");
			var yzjgxxs=$("#verifyModAuditForm .yzjgxxs");
			if(yzjgxxs.length>0){
                yzjgxxs.attr("validate","{required:true}");
                $("#verifyModAuditForm .yzjgxxs").attr("style","color: red;");
            }
			if(zffs.length>0){
				zffs.attr("validate","{required:true}");
				$("#shoppingCarForm #zffsbt").attr("style","color: red;");
			}
			if(fkf.length>0){
				fkf.attr("validate","{required:true}");
				$("#shoppingCarForm #fkfbt").attr("style","color: red;");
			}
			//退回步骤不显示
			_div.addClass("hidden").filter("select").prop("disabled",true);
			_shyjEl.css("width","85%");
		}
	}).trigger("change");
	//审核历史按钮
	$("#click_audit_steps").click(function(e) {
		if($("#auditAjaxForm .sl_flpop_con").hasClass("hidden"))
		{
			$("#auditAjaxForm .sl_flpop_con").removeClass("hidden").addClass("show");
			var ul_width=$("#auditAjaxForm .audit_steps").width()+30;
			var left_width = (400-ul_width)/2;
			$("#auditAjaxForm .audit_steps").css({marginLeft:left_width});
		}
		else{
			$("#auditAjaxForm .sl_flpop_con").removeClass("show").addClass("hidden");
			$("#auditAjaxForm .audit_steps").css({marginLeft:"0px"});
		}
	});
}

function applyloadCallback(response,status,xhr){
	var formhtml = $("#auditPage");
	var html = formhtml.get(0).outerHTML;
	response = response.replace('<form',"<div");
	response = response.replace('/form>',"/div>");
	formhtml.replaceWith(response)
}

function getQwwcsj(){
	//添加日期控件
	laydate.render({
	   elem: '#auditAjaxForm #qwwcsj'
	  ,theme: '#2381E9'
	});
	//默认期望完成日期
	var qwwcsj = new Date();
	qwwcsj.setDate(qwwcsj.getDate()+3);//获取3天后的日期 
	y = qwwcsj.getFullYear();
	m = qwwcsj.getMonth() + 1;
	d = qwwcsj.getDate();
	qwwcsj = y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
	$("#auditAjaxForm #qwwcsj").val(qwwcsj);
	//根据审核类别和流程序号判断是否显示期望时间
	if($("#auditAjaxForm #shlb").val() != 'QUALITY_FILE_MOD' && $("#auditAjaxForm #shlb").val() != 'QUALITY_FILE_ADD'){
		$("#auditAjaxForm div[name=qwwc]").remove();
	}else if($("#auditAjaxForm #lastStep").val() == "false"){
		$("#auditAjaxForm div[name=qwwc]").remove();
	}
}

jQuery(function(){
	var data = {};
	data[$("#ywzd").val()] = $("#ywid").val();
	data["access_token"] = $("#ac_tk").val();

	data["flg_qf"] = $("#RFSAudit_formAudit  #flag").val();
	$.ajax({
			type : "post",
			url: $("#business_url").val(),
			data : data,
			dataType : "html",
			success : function(data){
				var formhtml = $("#auditPage");
				var html = formhtml.get(0).outerHTML;
				data = data.replace('<form',"<div");
				data = data.replace('/form>',"/div>");
				formhtml.replaceWith(data)
			}
		});
		
	//div加载页面
	//$("#auditPage").load($("#business_url").val(),data,applyloadCallback);
	auditBtnBind();
	getQwwcsj();
});