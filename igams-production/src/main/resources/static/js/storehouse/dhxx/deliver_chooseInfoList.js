/**
 * 筛选明细信息
 * @param e
 * @returns
 */
function selectFhmx(e) {
	var cxnr = $("#chooseFhmxForm #cxnr").val();
	$("#chooseFhmxForm tr[name=trFhmx]").each(function(){
		if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1){
			$("#chooseFhmxForm #"+this.id).show();
		}else{
			$("#chooseFhmxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseFhmxForm #checkAll").is(":checked")){
		$("#chooseFhmxForm input[name='checkFhmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseFhmxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseFhmxForm input[name='checkFhmx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#chooseFhmxForm #checkAll").removeAttr("checked");
	}
}

/**
 * 明细行点击事件
 * @param htmxid
 * @param e
 * @returns
 */
function checkClick(fhmxid,e){
	if($("#chooseFhmxForm #check_"+fhmxid).is(":checked")){
		$("#chooseFhmxForm #check_"+fhmxid).removeAttr("checked");
	}else{
		$("#chooseFhmxForm #check_"+fhmxid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseFhmxForm input[name='checkFhmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseFhmxForm #checkAll").removeAttr("checked");
	}else{
		$("#chooseFhmxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化明细选中状态
	var fhid = $("#chooseFhmxForm input[name='fhid']").val();
	var fhmxJson = [];
	if($("#chooseFhglForm").length > 0){
		if($("#chooseFhglForm #fhmx_json").val()){
			fhmxJson = JSON.parse($("#chooseFhglForm #fhmx_json").val());
		}
	}else if($("#deliverGoodsForm").length > 0){
		if($("#deliverGoodsForm #hwmx_json").val()){
			fhmxJson = JSON.parse($("#deliverGoodsForm #hwmx_json").val());
		}
	}
	if(fhmxJson.length > 0){
		for (var i = 0; i < fhmxJson.length; i++) {
			if(fhid == fhmxJson[i].fhid){
				$("#chooseFhmxForm #check_" + fhmxJson[i].fhmxid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseFhmxForm input[name='checkFhmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseFhmxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseFhmxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseFhmxForm .chosen-select').chosen({width: '100%'});
})