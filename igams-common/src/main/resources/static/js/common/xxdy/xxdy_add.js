//选择消息对应类型
$("#dylx").on('change',function(){
	var dylxcsdm =$("#ajaxForm #dylx").find("option:selected").attr("csdm");
	var cskz1 =$("#ajaxForm #dylx").find("option:selected").attr("cskz1");
	var cskz2 =$("#ajaxForm #dylx").find("option:selected").attr("cskz2");
	$("#ajaxForm #yxxid").val('');
	if(!cskz1){
		$("#ajaxForm #dyxxSelect").hide();
		$("#ajaxForm #dyxx").removeAttr("validate");
		$("#ajaxForm #dyxxInput").show();
		$("#ajaxForm #dyxx_input").attr("validate","{required:true}");
		$("#ajaxForm #dyxx").empty();
	}else{
		$("#ajaxForm #dyxxInput").hide();
		$("#ajaxForm #dyxx_input").removeAttr("validate");
		$("#ajaxForm #dyxxSelect").show();
		$("#ajaxForm #dyxx").attr("validate","{required:true}");
		$.ajax({
			url : $("#ajaxForm #urlPrefix").val()+"/xxdy/xxdy/pagedataDylxdeal",
			type : "post",
			data : {dylxcsdm:dylxcsdm,cskz1:cskz1,cskz2:cskz2,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success:function(data){
				if(data.resultList != null && data.resultList.length != 0){
		    		var csHtml = "";
		    		csHtml += "<option value=''>--请选择--</option>";
	            	$.each(data.resultList,function(i){  
	            		if(cskz1 == "RY"){
	            			csHtml += "<option value='" + data.resultList[i].yhid + "'>" + data.resultList[i].yhm+"-"+data.resultList[i].zsxm + "</option>";
	            		}else if(cskz1 == "JS"){
	            			csHtml += "<option value='" + data.resultList[i].jsid + "'>" + data.resultList[i].jsmc + "</option>";
	            		}else if(cskz1 == "JCSJ" || cskz1 == "XM" || cskz1 == "LS"){
	            			csHtml += "<option value='" + data.resultList[i].csid + "'>" + data.resultList[i].csmc + "</option>";
	            		}else if(cskz1 == "BM" ){
	                         csHtml += "<option value='" + data.resultList[i].jgid + "'>" + data.resultList[i].jgmc + "</option>";
	                    }else if(cskz1 == "YJ" ){
	                         csHtml += "<option value='" + data.resultList[i].wzid + "'>" + data.resultList[i].wzywm+"--" +data.resultList[i].wzzwm+ "</option>";
	                   }
	            	});
	
	            	$("#ajaxForm #dyxx").empty();
	            	$("#ajaxForm #dyxx").append(csHtml);
	            	$("#ajaxForm #dyxx").trigger("chosen:updated");
		    	}else{
		    		var csHtml = "";
		    		csHtml += "<option value=''>--请选择--</option>";
		    		$("#ajaxForm #dyxx").empty();
	            	$("#ajaxForm #dyxx").append(csHtml);
	            	$("#ajaxForm #dyxx").trigger("chosen:updated");
	            	
	            	$("#ajaxForm #dyxxSelect").hide();
					$("#ajaxForm #dyxx").removeAttr("validate");
					$("#ajaxForm #dyxxInput").show();
					$("#ajaxForm #dyxx_input").attr("validate","{required:true,stringMaxLength:64}");
		    	}
				dtoList = data.dtoList;
				if (dtoList!= null && dtoList.length != 0){
					dtoList = data.dtoList;
					var html = "";
					html += "<option value=''>--请选择--</option>";
					$.each(data.list,function(i){
						html += "<option value='" + data.list[i].csid + "' csmc='" + data.list[i].csmc + "'>" + data.list[i].csmc + "</option>";
					});
					$("#ajaxForm #yxxid").empty();
					$("#ajaxForm #yxxid").append(html);
					$("#ajaxForm #yxxid").trigger("chosen:updated");
					$("#ajaxForm #yxxInput").hide();
					$("#ajaxForm #yxx").removeAttr("validate");
					$("#ajaxForm #yxxSelect").show();
					$("#ajaxForm #yxxid").attr("validate","{required:true}");
				}else{
					$("#ajaxForm #yxxSelect").hide();
					$("#ajaxForm #yxxid").removeAttr("validate");
					$("#ajaxForm #zidSelect").hide();
					$("#ajaxForm #zid").removeAttr("validate");
					$("#ajaxForm #yxxInput").show();
					$("#ajaxForm #yxx").attr("validate","{required:true,stringMaxLength:64}");
				}
			}
		});
	}
	
})

$(function(){
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	var list = $("#ajaxForm #list").val();
	var cskz1 = $("#ajaxForm #cskz1").val();
	if(!cskz1){
		$("#ajaxForm #dyxxSelect").hide();
		$("#ajaxForm #dyxx").removeAttr("validate");
		$("#ajaxForm #dyxxInput").show();
		$("#ajaxForm #dyxx_input").attr("validate","{required:true}");
	}else{
		$("#ajaxForm #dyxxInput").hide();
		$("#ajaxForm #dyxx_input").removeAttr("validate");
		$("#ajaxForm #dyxxSelect").show();
		$("#ajaxForm #dyxx").attr("validate","{required:true}");
	}
	if (list){
		$("#ajaxForm #yxxInput").hide();
		$("#ajaxForm #yxx").removeAttr("validate");
		$("#ajaxForm #yxxSelect").show();
		$("#ajaxForm #yxxid").attr("validate","{required:true}");
	}
	var dtos = $("#ajaxForm #dtoList").val();
	if (dtos&&dtos.length > 0){
		$("#ajaxForm #zidSelect").show();
		$("#ajaxForm #zid").attr("validate","{required:true}");
		dtoList = JSON.parse(dtos);
		changeYxxid()
	}


})
var dtoList = null;
//选择原信息
function changeYxxid(){
	var html = "";
	html += "<option value=''>--请选择--</option>";
	var csid =$("#ajaxForm #yxxid").find("option:selected").val();
	var count = 0;
	$.each(dtoList,function(i){
		if (dtoList[i].fcsid == csid){
			count ++;
			html += "<option value='" + dtoList[i].csid + "' csmc='" + dtoList[i].csmc + "'";
			if ($("#ajaxForm #zidKey").val() && $("#ajaxForm #zidKey").val() == dtoList[i].csid){
				html += " selected='true'";
			}
			html += ">" + dtoList[i].csmc + "</option>";
		}
		$("#ajaxForm #zid").empty();
		$("#ajaxForm #zid").append(html);
		$("#ajaxForm #zid").trigger("chosen:updated");
		if (count != 0){
			$("#ajaxForm #zidSelect").show();
			$("#ajaxForm #zid").attr("validate","{required:true}");

		}else{
			$("#ajaxForm #zidSelect").hide();
			$("#ajaxForm #zid").removeAttr("validate");

		}
	});
}
