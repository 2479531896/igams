/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectXqmx(e) {
	var cxnr = $("#chooseXqmxForm #cxnr").val();
	$("#chooseXqmxForm tr[name=trXqmx]").each(function(){
		if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1){
			$("#chooseXqmxForm #"+this.id).show();
		}else{
			$("#chooseXqmxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseXqmxForm #checkAll").is(":checked")){
		$("#chooseXqmxForm input[name='checkXqmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseXqmxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseXqmxForm input[name='checkXqmx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#chooseXqmxForm #checkAll").removeAttr("checked");
	}
}

/**
 * 请购明细行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(cpxqid,e){
	if($("#chooseXqmxForm #check_"+cpxqid).is(":checked")){
		$("#chooseXqmxForm #check_"+cpxqid).removeAttr("checked");
	}else{
		$("#chooseXqmxForm #check_"+cpxqid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseXqmxForm input[name='checkXqmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseXqmxForm #checkAll").removeAttr("checked");
	}else{
		$("#chooseXqmxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var cpxqid = $("#chooseXqmxForm input[name='cpxqid']").val();
	var xqmxJson = [];
	if($("#rece_progress").length > 0){
		if($("#rece_progress #xqmx_json").val()){
			xqmxJson = JSON.parse($("#rece_progress #xqmx_json").val());
		}
	}
	if(xqmxJson.length > 0){
		for (var i = 0; i < xqmxJson.length; i++) {
			if(cpxqid == xqmxJson[i].cpxqid){
				$("#chooseXqmxForm #check_" + xqmxJson[i].xqjhmxid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseXqmxForm input[name='checkXqmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseXqmxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseXqmxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseQgxxForm .chosen-select').chosen({width: '100%'});
})