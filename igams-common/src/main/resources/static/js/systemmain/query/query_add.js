$(function(){
	var cxqfdm = $("#ajaxForm #cxqfdm").val();
	if(cxqfdm == "STATISTICS"){
		$("#cxbmDiv").show();
		$("#lbqfDiv").show();
		$("#pxDiv").show();
		$("#ajaxForm #yzbj").val("1");
	}
	
	$("#ajaxForm #cxqf").change(function(){
		var csdm=$("#ajaxForm #cxqf").find("option:selected").attr("csdm");
		if(csdm=="STATISTICS"){
			$("#cxbmDiv").show();
			$("#lbqfDiv").show();
			$("#pxDiv").show();
			$("#ajaxForm #yzbj").val("1");
		}else if (csdm=="AIINSERT" || csdm=="AISELECT" ||csdm=="AIDEL" ){
		    $("#cxbmDiv").show();
		    $("#ajaxForm #yzbj").val("0");
            $("#lbqfDiv").hide();
            $("#pxDiv").hide();
		}else{
			$("#ajaxForm #yzbj").val("0");
			$("#cxbmDiv").hide();
			$("#lbqfDiv").hide();
			$("#pxDiv").hide();
		}
	});
});