/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectQgmx(e) {
	var cxnr = $("#chooseMxForm #cxnr").val();
	$("#chooseMxForm tr[name=trQgmx]").each(function(){
		if(this.dataset.hwmc.indexOf(cxnr) != -1  || this.dataset.hwbz.indexOf(cxnr) != -1){
			$("#chooseMxForm #"+this.id).show();
		}else{
			$("#chooseMxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseMxForm #checkAll").is(":checked")){
		$("#chooseMxForm input[name='checkQgmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseMxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseMxForm input[name='checkQgmx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#chooseMxForm #checkAll").removeAttr("checked");
	}
}

/**
 * 请购明细行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(qgmxid,e){
	if($("#chooseMxForm #check_"+qgmxid).is(":checked")){
		$("#chooseMxForm #check_"+qgmxid).removeAttr("checked");
	}else{
		$("#chooseMxForm #check_"+qgmxid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseMxForm input[name='checkQgmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseMxForm #checkAll").removeAttr("checked");
	}else{
		$("#chooseMxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var qgid = $("#chooseMxForm input[name='qgid']").val();
	var htmxJson = [];
	if($("#chooseQgForm").length > 0){
		if($("#chooseQgForm #qgmx_json").val()){
			htmxJson = JSON.parse($("#chooseQgForm #qgmx_json").val());
		}
	}else if($("#inStoreForm").length > 0){
		if(t_map.rows.length>0){
			for (var i = 0; i < t_map.rows.length; i++) {
				var sz = {"qgid": '', "qgmxid": ''};
				sz.qgid = t_map.rows[i].qgid;
				sz.qgmxid = t_map.rows[i].qgmxid;
				htmxJson.push(sz);
			}
		}
	}
	if(htmxJson.length > 0){
		for (var i = 0; i < htmxJson.length; i++) {
			if(qgid == htmxJson[i].qgid){
				$("#chooseMxForm #check_" + htmxJson[i].qgmxid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseMxForm input[name='checkQgmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseMxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseMxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
	jQuery('#chooseQgxxForm .chosen-select').chosen({width: '100%'});
})