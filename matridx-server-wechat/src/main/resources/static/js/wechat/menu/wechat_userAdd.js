//事件绑定
/*function btnBind(){
	var sel_gzpt = $("#ajaxForm #gzpt");
	//关注平台下拉框改变事件
	sel_gzpt.unbind("change").change(function(){
		gzptEvent();
	});
}*/

/**
 * 关注平台下拉框事件
 */
/*function gzptEvent(){
	var gzpt = $("#ajaxForm #gzpt").val();
	if(gzpt == null || gzpt == ""){
		var bqidlbHtml = "";
		bqidlbHtml += "<option value=''>--请选择--</option>";
		$("#ajaxForm #bqidlb").empty();
    	$("#ajaxForm #bqidlb").append(bqidlbHtml);
		$("#ajaxForm #bqidlb").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/tag/tag/selectTag", 
	    cache: false,
	    data: {"wbcxid":gzpt,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data.bqglDtos != null && data.bqglDtos.length != 0){
	    		var bqidlbHtml = "";
	    		bqidlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data.bqglDtos,function(i){
            		bqidlbHtml += "<option value='" + data.bqglDtos[i].bqid + "'>" + data.bqglDtos[i].bqm + "</option>";
            	});
            	$("#ajaxForm #bqidlb").empty();
            	$("#ajaxForm #bqidlb").append(bqidlbHtml);
            	$("#ajaxForm #bqidlb").trigger("chosen:updated");
	    	}else{
	    		var bqidlbHtml = "";
	    		bqidlbHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #bqidlb").empty();
            	$("#ajaxForm #bqidlb").append(bqidlbHtml);
            	$("#ajaxForm #bqidlb").trigger("chosen:updated");
	    	}
	    }
	});
}*/

function initPage(){
}

$(document).ready(function(){
    //所有下拉框添加choose样式
//	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
//	btnBind();
	//初始化页面数据
	initPage();
});