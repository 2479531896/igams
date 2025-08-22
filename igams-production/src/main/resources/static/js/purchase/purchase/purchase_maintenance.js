$("#puchasemaintenanceForm #kdlx").change(function(){
	var cskz2=$("#puchasemaintenanceForm #kdlx").find("option:selected").attr("cskz2");
	if(cskz2=="1"){
		$("#kdlxDiv").show();
		$("#puchasemaintenanceForm #yzbj").val("1");
	}else{
		$("#puchasemaintenanceForm #yzbj").val("0");
		$("#kdlxDiv").hide();
	}
});

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#puchasemaintenanceForm #checkAll").is(":checked")){
		$("#puchasemaintenanceForm input[name='checkQgmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#puchasemaintenanceForm #checkAll").prop("checked", true);
	}else{
		$("#puchasemaintenanceForm input[name='checkQgmx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#puchasemaintenanceForm #checkAll").removeAttr("checked");
	}
}

/**
 * 请购明细行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(htmxid,e){
	if($("#puchasemaintenanceForm #check_"+htmxid).is(":checked")){
		$("#puchasemaintenanceForm #check_"+htmxid).removeAttr("checked");
	}else{
		$("#puchasemaintenanceForm #check_"+htmxid).prop("checked", true);
	}
	var unchecked = false;
	$("#puchasemaintenanceForm input[name='checkQgmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#puchasemaintenanceForm #checkAll").removeAttr("checked");
	}else{
		$("#puchasemaintenanceForm #checkAll").prop("checked", true);
	}
}
