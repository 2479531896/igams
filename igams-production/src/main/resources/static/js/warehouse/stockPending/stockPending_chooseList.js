/**
 * 筛选货物信息
 * @param e
 * @returns
 */
function selectHwxx(e) {
	var cxnr = $("#chooseHwxxForm #cxnr").val();
	$("#chooseHwxxForm tr[name=trHwxx]").each(function(){
		if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1 || this.dataset.dhdh.indexOf(cxnr) != -1){
			$("#chooseHwxxForm #"+this.id).show();
		}else{
			$("#chooseHwxxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseHwxxForm #checkAll").is(":checked")){
		$("#chooseHwxxForm input[name='checkHwxx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseHwxxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseHwxxForm input[name='checkHwxx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#chooseHwxxForm #checkAll").removeAttr("checked");
	}
}


/**
 * 货物信息行点击事件
 * @param hwid
 * @param e
 * @returns
 */
function checkClick(hwid,e){
	if($("#chooseHwxxForm #check_"+hwid).is(":checked")){
		$("#chooseHwxxForm #check_"+hwid).removeAttr("checked");
	}else{
		$("#chooseHwxxForm #check_"+hwid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseHwxxForm input[name='checkHwxx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseHwxxForm #checkAll").removeAttr("checked");
	}else{
		$("#chooseHwxxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化货物选中状态
	var hwxxJson_choose= [];
	var hwxxJson = [];
	if($("#editPutInStorageForm").length > 0){
		if($("#editPutInStorageForm #hwxx_json").val()){
			hwxxJson = JSON.parse($("#editPutInStorageForm #hwxx_json").val());
		}
	}	
	if($("#chooseHwxxForm #hwxxJson_choose").val()){
		hwxxJson_choose = JSON.parse($("#chooseHwxxForm #hwxxJson_choose").val());
	}
	
	if(hwxxJson_choose.length > 0){
		if(hwxxJson.length > 0){
			for (var j = 0; j < hwxxJson_choose.length; j++) {
				for (var i = 0; i < hwxxJson.length; i++) {
					if(hwxxJson_choose[j].hwid == hwxxJson[i].hwid){
						$("#chooseHwxxForm #check_" + hwxxJson_choose[j].hwid).prop("checked", true);
					}
				}
			}	
			var unchecked = false;
			$("#chooseHwxxForm input[name='checkHwxx']").each(function(i){
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
}

$(function(){
	init();
	jQuery('#chooseHwxxForm .chosen-select').chosen({width: '100%'});
})