/** 
* 筛选请购明细信息
 * @param e
 * @returns
 */
function selectHtmx(e) {
	var cxnr = $("#chooseHtmxForm #cxnr").val();
	$("#chooseHtmxForm tr[name=trHtmx]").each(function(){
		if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1){
			$("#chooseHtmxForm #"+this.id).show();
		}else{
			$("#chooseHtmxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseHtmxForm #checkAll").is(":checked")){
		$("#chooseHtmxForm input[name='checkHtmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseHtmxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseHtmxForm input[name='checkHtmx']").each(function(i){
			$(this).prop("checked", false);
		})
		$("#chooseHtmxForm #checkAll").prop("checked", false);
	}
}

/**
 * 请购明细行点击事件
 * @param htmxid
 * @param e
 * @returns
 */
function checkClick(htmxid,e){
	if($("#chooseHtmxForm #check_"+htmxid).is(":checked")){
		$("#chooseHtmxForm #check_"+htmxid).prop("checked", false);
	}else{
		$("#chooseHtmxForm #check_"+htmxid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseHtmxForm input[name='checkHtmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseHtmxForm #checkAll").prop("checked", false);
	}else{
		$("#chooseHtmxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var htid = $("#chooseHtmxForm input[name='htid']").val();
	var htmxJson = [];
	if($("#chooseHtxxForm").length > 0){
		if($("#chooseHtxxForm #htmx_json").val()){
			htmxJson = JSON.parse($("#chooseHtxxForm #htmx_json").val());
		}
	}else if($("#arrivalGoodsEditForm").length > 0){
		if($("#arrivalGoodsEditForm #hwmx_json").val()){
			htmxJson = JSON.parse($("#arrivalGoodsEditForm #hwmx_json").val());
		}
	}
	if(htmxJson.length > 0){
		for (var i = 0; i < htmxJson.length; i++) {
			if(htid == htmxJson[i].htid){
				$("#chooseHtmxForm #check_" + htmxJson[i].htmxid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseHtmxForm input[name='checkHtmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseHtmxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseHtmxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseHtxxForm .chosen-select').chosen({width: '100%'});
})