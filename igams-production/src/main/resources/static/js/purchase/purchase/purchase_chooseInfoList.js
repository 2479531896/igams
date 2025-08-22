/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectQgmx(e) {
	var cxnr = $("#chooseQgmxForm #cxnr").val();
	$("#chooseQgmxForm tr[name=trQgmx]").each(function(){
		if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1 || this.dataset.bz.indexOf(cxnr) != -1){
			$("#chooseQgmxForm #"+this.id).show();
		}else{
			$("#chooseQgmxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseQgmxForm #checkAll").is(":checked")){
		$("#chooseQgmxForm input[name='checkQgmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseQgmxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseQgmxForm input[name='checkQgmx']").each(function(i){
			$(this).prop("checked", false);
		})
		$("#chooseQgmxForm #checkAll").prop("checked", false);
	}
}

/**
 * 请购明细行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(qgmxid,e){
	if($("#chooseQgmxForm #check_"+qgmxid).is(":checked")){
		$("#chooseQgmxForm #check_"+qgmxid).prop("checked", false);
	}else{
		$("#chooseQgmxForm #check_"+qgmxid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseQgmxForm input[name='checkQgmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseQgmxForm #checkAll").prop("checked", false);
	}else{
		$("#chooseQgmxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var qgid = $("#chooseQgmxForm input[name='qgid']").val();
	var htmxJson = [];
	if($("#chooseQgxxForm").length > 0){
		if($("#chooseQgxxForm #qgmx_json").val()){
			htmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
		}
	}else if($("#editContractForm").length > 0){
		if($("#editContractForm #htmx_json").val()){
			htmxJson = JSON.parse($("#editContractForm #htmx_json").val());
		}
	}
	if(htmxJson.length > 0){
		for (var i = 0; i < htmxJson.length; i++) {
			if(qgid == htmxJson[i].qgid){
				$("#chooseQgmxForm #check_" + htmxJson[i].qgmxid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseQgmxForm input[name='checkQgmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseQgmxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseQgmxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseQgxxForm .chosen-select').chosen({width: '100%'});
})