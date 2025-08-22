$("#maintenanceForm #kdlx").change(function(){
	var cskz2=$("#maintenanceForm #kdlx").find("option:selected").attr("cskz2");
	if(cskz2=="1"){
		$("#kdlxDiv").show();
		$("#maintenanceForm #yzbj").val("1");
	}else{
		$("#maintenanceForm #yzbj").val("0");
		$("#kdlxDiv").hide();
	}
});