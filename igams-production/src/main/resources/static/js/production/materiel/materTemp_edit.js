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
/**
 * 判断数据修改
 */
function textMod(){
	if($("#ajaxForm #wl_wllb").val()){
		if($("#ajaxForm #wl_wllb").val() != $("#ajaxForm #mt_wllb").val()){
			$("#ajaxForm #lable_wllb").css("color","red");
		}
		if($("#ajaxForm #wl_wlzlb").val() != $("#ajaxForm #mt_wlzlb").val()){
			$("#ajaxForm #lable_wlzlb").css("color","red");
		}
		if($("#ajaxForm #wl_wlzlbtc").val() != $("#ajaxForm #mt_wlzlbtc").val()){
			$("#ajaxForm #lable_wlzlbtc").css("color","red");
		}
		if($("#ajaxForm #wl_wlmc").val() != $("#ajaxForm #mt_wlmc").val()){
			$("#ajaxForm #lable_wlmc").css("color","red");
		}
		if($("#ajaxForm #wl_dlgys").val() != $("#ajaxForm #mt_dlgys").val()){
			$("#ajaxForm #lable_dlgys").css("color","red");
		}
		if($("#ajaxForm #wl_scs").val() != $("#ajaxForm #mt_scs").val()){
			$("#ajaxForm #lable_scs").css("color","red");
		}
		if($("#ajaxForm #wl_ychh").val() != $("#ajaxForm #mt_ychh").val()){
			$("#ajaxForm #lable_ychh").css("color","red");
		}
		if($("#ajaxForm #wl_gg").val() != $("#ajaxForm #mt_gg").val()){
			$("#ajaxForm #lable_gg").css("color","red");
		}
		if($("#ajaxForm #wl_jldw").val() != $("#ajaxForm #mt_jldw").val()){
			$("#ajaxForm #lable_jldw").css("color","red");
		}
		if($("#ajaxForm #wl_bctj").val() != $("#ajaxForm #mt_bctj").val()){
			$("#ajaxForm #lable_bctj").css("color","red");
		}
		if($("#ajaxForm #wl_bzq").val() != $("#ajaxForm #mt_bzq").val()){
			$("#ajaxForm #lable_bzq").css("color","red");
		}
		if($("#ajaxForm #wl_sfwxp").val() != $("input[name='sfwxp']:checked").val()){
			$("#ajaxForm #lable_sfwxp").css("color","red");
		}
		if($("#ajaxForm #wl_jwlbm").val() != $("#ajaxForm #mt_jwlbm").val()){
			$("#ajaxForm #lable_jwlbm").css("color","red");
		}
		if($("#ajaxForm #wl_bz").val() != $("#ajaxForm #mt_bz").val()){
			$("#ajaxForm #lable_bz").css("color","red");
		}
		if($("#ajaxForm #wl_scbj").val() != $("input[name='scbj']:checked").val()){
			$("#ajaxForm #lable_scbj").css("color","red");
		}
		if($("#ajaxForm #wl_cpzch").val() != $("#ajaxForm #mt_cpzch").val()){
			$("#ajaxForm #lable_cpzch").css("color","red");
		}
	}
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
	    url:$("#ajaxForm #urlPrefix").val()+"/systemmain/data/ansyGetJcsjList", 
	    cache: false,
	    data: {"fcsid":wllb,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		zlbHtml += "<option id='"+data[i].csid+"' value='" + data[i].csid + "' cskz1='"+data[i].cskz1+"' cskz2='"+data[i].cskz2+"'>" + data[i].csmc + "</option>";
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

function viewModMaterHistry(wlid){
	var url= $("#ajaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token="+$("#ac_tk").val();
	$.showDialog(url,'查看物料',viewModMaterHistryConfig);
}

var viewModMaterHistryConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function init(){
	if($("#ajaxForm #modbj").val()=="1"){
		$("#ajaxForm #wllbdiv").attr("style", "pointer-events: none;");
		$("#ajaxForm #wlzlbdiv").attr("style", "pointer-events: none;");
	}	
	var bzqflg = $("#ajaxForm [name='bzqflg']");
	if(bzqflg[0].checked){
		$("#ajaxForm #bzqdiv").addClass("hidden").find("input").prop("disabled",true);
		$("#ajaxForm #bzqxxdiv").removeClass("hidden").find("input").prop("disabled",false);
	}else{
		$("#ajaxForm #bzqdiv").removeClass("hidden").find("input").prop("disabled",false);
		$("#ajaxForm #bzqxxdiv").addClass("hidden").find("input").prop("disabled",true);
	}
	if($("#ajaxForm #status").val() == "fail"){
		$.alert($("#ajaxForm #message").val(),function() {
			$.closeModal("modMaterTempModal");
		});
	}
	if($("#auditAjaxForm").length == 0){
		$("#history").attr("style","display:none;");
	}
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
	jQuery('.chosen-select').chosen({width: '100%'});
	btnBind();
	textMod();
	init();
});