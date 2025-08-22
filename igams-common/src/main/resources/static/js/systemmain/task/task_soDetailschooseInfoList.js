/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectXsmx() {
	var gjz = $("#chooseXsmxForm #gjz").val();
	$("#chooseXsmxForm tr[name=trXsmx]").each(function(){
		if(this.dataset.wlbm.indexOf(gjz) != -1  || this.dataset.wlmc.indexOf(gjz) != -1){
			$("#chooseXsmxForm #"+this.id).show();
		}else{
			$("#chooseXsmxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseXsmxForm #checkAll").is(":checked")){
		$("#chooseXsmxForm input[name='checkXsmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseXsmxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseXsmxForm input[name='checkXsmx']").each(function(i){
			$(this).prop("checked", false);
		})
		$("#chooseXsmxForm #checkAll").prop("checked", false);
	}
}

/**
 * 请购明细行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(xsmxid,e){
	if($("#chooseXsmxForm #check_"+xsmxid).is(":checked")){
		$("#chooseXsmxForm #check_"+xsmxid).prop("checked", false);
	}else{
		$("#chooseXsmxForm #check_"+xsmxid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseXsmxForm input[name='checkXsmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseXsmxForm #checkAll").prop("checked", false);
	}else{
		$("#chooseXsmxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var qgid = $("#chooseXsmxForm input[name='xsid']").val();
	var xsmxJson = [];
	if($("#chooseDdhForm").length > 0){
		if($("#chooseDdhForm #xsmx_json").val()){
			xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
		}
	}
	// else if($("#editContractForm").length > 0){
	// 	if($("#editContractForm #htmx_json").val()){
	// 		htmxJson = JSON.parse($("#editContractForm #htmx_json").val());
	// 	}
	// }
	if(xsmxJson.length > 0){
		for (var i = 0; i < xsmxJson.length; i++) {
			if(qgid == xsmxJson[i].xsid){
				$("#chooseXsmxForm #check_" + xsmxJson[i].xsid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseXsmxForm input[name='checkXsmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseXsmxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseXsmxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseDdhForm .chosen-select').chosen({width: '100%'});
})