/**
 * 绑定按钮事件
 */
function btnBind(){
	
	var sel_wllb = $("#ajaxForm #mt_wllb");
	var bzqflg = $("#ajaxForm [name='bzqflg']");
	var sel_wlzlb=$("#ajaxForm #mt_wlzlb");
	
	//物料类别下拉框改变事件
	sel_wllb.unbind("change").change(function(){
		wllbEvent();
	});
	//物料子类别下拉框改变事件
	sel_wlzlb.unbind("change").change(function(){
		wlzlbEvent();
	});
	//保质期选项改变事件
	bzqflg.unbind("click").click(function(){
		if(bzqflg[0].checked){
			$("#ajaxForm #bzqdiv").addClass("hidden").find("input").prop("disabled",true);
			$("#ajaxForm #bzqxxdiv").removeClass("hidden").find("input").prop("disabled",false);
		}else{
			$("#ajaxForm #bzqdiv").removeClass("hidden").find("input").prop("disabled",false);
			$("#ajaxForm #bzqxxdiv").addClass("hidden").find("input").prop("disabled",true);
		}
	});
}

//物料子类别下拉框事件
function wlzlbEvent(){
	var wlzlb = $("#ajaxForm #mt_wlzlb").val();
	var cskz2= $("#ajaxForm #"+wlzlb).attr("cskz2");
	if(cskz2=="1"){
		$("#ajaxForm #zlbtcxs").show();
		$("#ajaxForm #mt_wlzlbtc").attr("validate","{required:true}");
	}else{
		$("#ajaxForm #zlbtcxs").hide();
		$("#ajaxForm #mt_wlzlbtc").removeAttr("validate");
	}
}
/**
 * 物料类别下拉框事件
 */
function wllbEvent(){
	var wllb = $("#ajaxForm #mt_wllb").val();
	if(wllb == null || wllb==""){
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		$("#ajaxForm #mt_wlzlb").empty();
    	$("#ajaxForm #mt_wlzlb").append(zlbHtml);
		$("#ajaxForm #mt_wlzlb").trigger("chosen:updated");
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$('#mater_formSearch #urlPrefix').val() + "/systemmain/data/ansyGetJcsjList", 
	    cache: false,
	    data: {"fcsid":wllb,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		zlbHtml += "<option id='"+data[i].csid+"' value='" + data[i].csid + "' cskz1='"+data[i].cskz1+"' + cskz2='"+data[i].cskz2+"'>" + data[i].csdm+"--" +data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #mt_wlzlb").empty();
            	$("#ajaxForm #mt_wlzlb").append(zlbHtml);
            	$("#ajaxForm #mt_wlzlb").trigger("chosen:updated");
	    	}else{
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #mt_wlzlb").empty();
            	$("#ajaxForm #mt_wlzlb").append(zlbHtml);
            	$("#ajaxForm #mt_wlzlb").trigger("chosen:updated");
	    	}
	    }
	});
}
/**
 * 判断人份格式
 */
function checkrf(e) {
	if (e.value!='') {
		if (!/^[0-9]*[1-9][0-9]*$/.test(e.value) || e.value <= 0) {
			$.error("请填写大于0的整数!");
			$("#ajaxForm #rf").val("")
		}
	}
}
function init(){
	var bzqflg = $("#ajaxForm [name='bzqflg']");
	if(bzqflg[0].checked){
		$("#ajaxForm #bzqdiv").addClass("hidden").find("input").prop("disabled",true);
		$("#ajaxForm #bzqxxdiv").removeClass("hidden").find("input").prop("disabled",false);
	}else{
		$("#ajaxForm #bzqdiv").removeClass("hidden").find("input").prop("disabled",false);
		$("#ajaxForm #bzqxxdiv").addClass("hidden").find("input").prop("disabled",true);
	}
}

function selectSame(){
	$.ajax({ 
	    type:'post',  
	    url:$('#mater_formSearch #urlPrefix').val() + "/production/materiel/pagedataSelectSameMater",
	    cache: false,
	    data: {"wlmc":$("#mt_wlmc").val(),"gg":$("#mt_gg").val(),"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data.prewlglDtos && data.prewlglDtos.length > 0){
	    		$("#preid").attr("style","display:block;");
	    		$("#prewlbm").text(data.prewlglDtos[0].wlbm);
	    		$("#prewlbm").attr("title",data.prewlglDtos[0].wlbm);
	    		$("#prewlmc").text(data.prewlglDtos[0].wlmc);
	    		$("#prewlmc").attr("title",data.prewlglDtos[0].wlmc);
	    		$("#pregg").text(data.prewlglDtos[0].gg);
	    		$("#pregg").attr("title",data.prewlglDtos[0].gg);
	    		$("#precount").text(data.count);
	    	}else{
	    		$("#prewlbm").text("");
	    		$("#prewlbm").attr("title","");
	    		$("#prewlmc").text("");
	    		$("#prewlmc").attr("title","");
	    		$("#pregg").text("");
	    		$("#pregg").attr("title","");
	    		$("#precount").text("");
	    	}
	    }
	});
}

$(document).ready(function(){
	var wlzlb = $("#ajaxForm #mt_wlzlb").val();
	if(wlzlb!=null && wlzlb!=""){
		var cskz2= $("#ajaxForm #"+wlzlb).attr("cskz2");
		if(cskz2=="1"){
			$("#ajaxForm #zlbtcxs").show();
			$("#ajaxForm #mt_wlzlbtc").attr("validate","{required:true}");
		}else{
			$("#ajaxForm #zlbtcxs").hide();
			$("#ajaxForm #mt_wlzlbtc").removeAttr("validate");
		}
	}
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	btnBind();
	init()
});
