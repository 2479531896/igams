/**
 * 资讯类型下拉框事件
 */
function zxlxEvent(){
	var zxlx = $("#ajaxForm #zxlx").val();
	if(zxlx == null || zxlx==""){
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		$("#ajaxForm #zxzlx").empty();
    	$("#ajaxForm #zxzlx").append(zlbHtml);
		$("#ajaxForm #zxzlx").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/systemmain/data/ansyGetJcsjList", 
	    cache: false,
	    data: {"fcsid":zxlx,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		$("#ajaxForm #zlx").attr("style","display:open;");
	    		$("#ajaxForm #zxzlx").attr("disabled",false);
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		zlbHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #zxzlx").empty();
            	$("#ajaxForm #zxzlx").append(zlbHtml);
            	$("#ajaxForm #zxzlx").trigger("chosen:updated");
	    	}else{
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #zlx").attr("style","display:none;");
	    		$("#ajaxForm #zxzlx").attr("disabled","disabled");
	    		$("#ajaxForm #zxzlx").empty();
            	$("#ajaxForm #zxzlx").append(zlbHtml);
            	$("#ajaxForm #zxzlx").trigger("chosen:updated");
	    	}
	    }
	});
}

/**
 * 绑定按钮事件
 */
function btnBind(){
	
	var sel_zxlx = $("#ajaxForm #zxlx");
	
	//异常类别下拉框改变事件
	sel_zxlx.unbind("change").change(function(){
		zxlxEvent();
	});
	if($("#ajaxForm #zxzlxmc").val()!=null && $("#ajaxForm #zxzlxmc").val()!=''){
		$("#ajaxForm #zlx").attr("style","display:open;");
	}
}

//添加日期控件
laydate.render({
   elem: '#fbrq'
  ,value: new Date()
});

$(function(){
	btnBind();
	// 所有下拉框添加choose样式
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
})