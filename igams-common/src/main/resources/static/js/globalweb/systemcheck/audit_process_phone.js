var auditBtnBind = function(){
		function sfpass(sftg){
		var _value = sftg=="0";
		var _div = jQuery("#shxxThlcxh");//退回步骤
		var _shyjEl = jQuery("#shxxShyj");//审核意见
		//审核不通过
		if(_value){
			_div.removeClass("hidden").filter("select").prop("disabled",false);
			_shyjEl.css("width","60%");
		}else{
			//退回步骤不显示
			_div.addClass("hidden").filter("select").prop("disabled",true);
			_shyjEl.css("width","85%");
		}
	}
}

function applyloadCallback(response,status,xhr){
	var formhtml = $("#auditPage");
	var html = formhtml.get(0).outerHTML;
	response = response.replace('<form',"<div");
	response = response.replace('/form>',"/div>");
    formhtml.replaceWith(response)
}

/**
 * 增加期望完成时间
 * @returns
 */
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

function shlssz(){
	var ul_width=$("#auditAjaxForm .audit_steps").width()+30;
	var left_width = (400-ul_width)/2;
	$("#auditAjaxForm .audit_steps").css({marginLeft:left_width});
}
$(function () { $('#collapseOne').collapse('toggle')});

function btnBind(){
	var s=$("#auditAjaxForm .btn-sm");
	s.unbind("click").click(function(){
		var _this=$(this);
		var sftg=_this.val();
		$("#sftg").val(sftg);
		var flg=$("#auditAjaxForm #flg").val();
		var shlb=$("#shlb").val();
		if(shlb=='AUDIT_REQUISITIONS'){
			if(sftg=='0'){
				var zffs=$("#shoppingCarForm #zffs");
				var fkf=$("#shoppingCarForm #fkf");
				if(zffs.val()==''){
					$("#shoppingCarForm #zffs").removeAttr("validate");
					$("#shoppingCarForm #zffsbt").attr("style","display:none;color: red;");
				}
				if(fkf.val()==''){
					$("#shoppingCarForm #fkf").removeAttr("validate");
					$("#shoppingCarForm #fkfbt").attr("style","display:none;color: red;");
				}
			}else{
				var zffs=$("#shoppingCarForm #zffs");
				var fkf=$("#shoppingCarForm #fkf");
				if(zffs.val()==''){
					zffs.attr("validate","{required:true}");
					$("#shoppingCarForm #zffsbt").attr("style","color: red;");
					$.alert("请填写支付方式信息");
					return false;
				}
				if(fkf.val()==''){
					fkf.attr("validate","{required:true}");
					$("#shoppingCarForm #fkfbt").attr("style","color: red;");
					$.alert("请填写付款方信息");
					return false;
				}
			}
		}
		
		if(!$("#auditAjaxForm").valid({ignore: ":hidden:not(select)"})){
			$.alert("请填写完整信息");
			return false;
		}
		
		if(flg!="1"){
			if($("#auditAjaxForm #lastStep").val()){
				if($("#recheck_mod_Form").length>0){
					if($("#recheck_mod_Form  #dsyrq").prop("disabled")!=true&&($("#recheck_mod_Form  #dsyrq").val()==null || $("#recheck_mod_Form  #dsyrq").val()=="")){
						$.error("请选择DNA实验日期！");
						return false;
					}
					if($("#recheck_mod_Form  #syrq").prop("disabled")!=true&&($("#recheck_mod_Form  #syrq").val()==null || $("#recheck_mod_Form  #syrq").val()=="")){
						$.error("请选择RNA实验日期！");
						return false;
					}
					if($("#recheck_mod_Form  #qtsyrq").prop("disabled")!=true&&($("#recheck_mod_Form  #qtsyrq").val()==null || $("#recheck_mod_Form  #qtsyrq").val()=="")){
						$.error("请选择其他实验日期！");
						return false;
					}
				}
			}
			$("#auditAjaxForm #flg").val("1");
			$.ajax({  
	            type: "POST",
	            url:$("#auditAjaxForm #urlPrefix").val()+"/ws/auditProcess/auditSave",
	            data:$('#auditAjaxForm').serialize(),// 序列化表单值  
	            async: false,  
	            error: function(request) {
	                $.error("提交失败！");
	 				$("#auditAjaxForm #flg").val("0");
	            },  
	            success: function(data) {
	    			var sf;//1代表返回的是success，0是fail
	    			var msg=data.message;
	    			if(data.result==false){
	    				$.error(msg);
	    				$("#auditAjaxForm #flg").val("0");
	    			}else if(data.result==true){
	    				if(msg.indexOf("<br/>")!=-1){
	    					msg=msg.replace("<br/>","");
	    				}
	    				$.success(msg, function() {
	        				window.location.reload();// 刷新页面
						});
	    			}
	            }  
	         });
		}else{
			$.confirm("请不要重复操作！");
		}
	});
}


$("#shjl").click(function(){
	if(!$("#auditAjaxForm").valid()){
		return false;
	}
	var getclass=$("#shjlmenu").attr("class");
	if(getclass.indexOf("open")!=-1){//折叠框展开状态
		$("#shjlmenu").removeClass("open");
		$("#shjlmenu").attr("class","glyphicon glyphicon-chevron-down shut");
	}else{
		$("#shjlmenu").removeClass("shut");
		$("#shjlmenu").attr("class","glyphicon glyphicon-chevron-up open");
	}
})

jQuery(function(){
	btnBind();
//	shlssz();
	var data = {};
	data[$("#ywzd").val()] = $("#ywid").val();
	data["access_token"] = $("#ac_tk").val();
	// div加载页面
	$.ajax({
		type:"post",
		url: $('#auditAjaxForm #urlPrefix').val() + "/ws"+$("#business_url").val(),
		data: data,
		dataType: "html",
		success: function (data){
			var formhtml = $("#auditPage");
			var html = formhtml.get(0).outerHTML;
			data =data.replace('<form',"<div");
			data =data.replace('/form>',"/div>");
			formhtml.replaceWith(data);
		}
	})
	// $("#auditPage").load($('#auditAjaxForm #urlPrefix').val() + "/ws"+$("#business_url").val(),data,applyloadCallback);
	auditBtnBind();
	getQwwcsj();
	
	//初始化调整样式
	setTimeout(function(){
		$("#bzqflg").attr("style","margin-top:0px;");
		$(".phonewxp1").attr("style","margin-top:0px;");
		$(".phonewxp2").attr("style","margin-top:0px;");
		$(".phonesyzt1").attr("style","margin-top:0px;");
		$(".phonesyzt2").attr("style","margin-top:0px;");
	},200)
});