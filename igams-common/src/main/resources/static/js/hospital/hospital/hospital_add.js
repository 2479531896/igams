//通过省份级联城市
$("#sf").on('change',function(){
	var csid=$("#sf").val();
	$.ajax({
		url : "/hospital/pagedataJscjcity",
		type : "post",
		data : {fcsid:csid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data != null && data.length != 0){
	    		var csbHtml = "";
	    		csHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #cs").empty();
            	$("#ajaxForm #cs").append(csHtml);
            	$("#ajaxForm #cs").trigger("chosen:updated");
	    	}else{
	    		var csHtml = "";
	    		csHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #cs").empty();
            	$("#ajaxForm #cs").append(csHtml);
            	$("#ajaxForm #cs").trigger("chosen:updated");
	    	}
		}
	});
})

$(function(){
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
})