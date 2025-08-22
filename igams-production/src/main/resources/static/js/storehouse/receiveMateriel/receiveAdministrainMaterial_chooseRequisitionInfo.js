/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectQgmx(e) {
	var cxnr = $("#chooseRequisitionInfoForm #cxnr").val();
	$("#chooseRequisitionInfoForm tr[name=trQgmx]").each(function(){
		if(this.dataset.hwmc.indexOf(cxnr) != -1  || this.dataset.hwbz.indexOf(cxnr) != -1){
			$("#chooseRequisitionInfoForm #"+this.id).show();
		}else{
			$("#chooseRequisitionInfoForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseRequisitionInfoForm #checkAll").is(":checked")){
		$("#chooseRequisitionInfoForm input[name='checkQgmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseRequisitionInfoForm #checkAll").prop("checked", true);
	}else{
		$("#chooseRequisitionInfoForm input[name='checkQgmx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#chooseRequisitionInfoForm #checkAll").removeAttr("checked");
	}
}

/**
 * 请购明细行点击事件
 * @param xzkcid
 * @param e
 * @returns
 */
function checkClick(xzkcid,e){
	if($("#chooseRequisitionInfoForm #check_"+xzkcid).is(":checked")){
		$("#chooseRequisitionInfoForm #check_"+xzkcid).removeAttr("checked");
	}else{
		$("#chooseRequisitionInfoForm #check_"+xzkcid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseRequisitionInfoForm input[name='checkQgmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseRequisitionInfoForm #checkAll").removeAttr("checked");
	}else{
		$("#chooseRequisitionInfoForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var qgid = $("#chooseRequisitionInfoForm input[name='qgid']").val();
	var htmxJson = [];
	if($("#chooseRequisitionForm").length > 0){
		if($("#chooseRequisitionForm #qgmx_json").val()){
			htmxJson = JSON.parse($("#chooseRequisitionForm #qgmx_json").val());
		}
	}else if($("#editXzPickingForm").length > 0){
		if(t_map.rows.length>0){
			for (var i = 0; i < t_map.rows.length; i++) {
				var sz = {"qgid": '', "xzkcid": '', "qgmxid": ''};
				sz.qgid = t_map.rows[i].qgid;
				sz.xzkcid = t_map.rows[i].xzkcid;
				sz.qgmxid = t_map.rows[i].qgmxid;
				htmxJson.push(sz);
			}
		}
	}
	if(htmxJson.length > 0){
		for (var i = 0; i < htmxJson.length; i++) {
			if(qgid == htmxJson[i].qgid){
				$("#chooseRequisitionInfoForm #check_" + htmxJson[i].xzkcid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseRequisitionInfoForm input[name='checkQgmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseRequisitionInfoForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseRequisitionInfoForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseQgxxForm .chosen-select').chosen({width: '100%'});
})