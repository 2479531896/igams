
function selectZlmx(e) {
	var cxnr = $("#chooseZlmxForm #cxnr").val();
	$("#chooseZlmxForm tr[name=trZlmx]").each(function(){
		if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1){
			$("#chooseZlmxForm #"+this.id).show();
		}else{
			$("#chooseZlmxForm #"+this.id).hide();
		}
	})
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
	if($("#chooseZlmxForm #checkAll").is(":checked")){
		$("#chooseZlmxForm input[name='checkZlmx']").each(function(i){
			$(this).prop("checked", true);
		})
		$("#chooseZlmxForm #checkAll").prop("checked", true);
	}else{
		$("#chooseZlmxForm input[name='checkZlmx']").each(function(i){
			$(this).removeAttr("checked");
		})
		$("#chooseZlmxForm #checkAll").removeAttr("checked");
	}
}


function checkClick(sczlid,e){
	if($("#chooseZlmxForm #check_"+sczlid).is(":checked")){
		$("#chooseZlmxForm #check_"+sczlid).removeAttr("checked");
	}else{
		$("#chooseZlmxForm #check_"+sczlid).prop("checked", true);
	}
	var unchecked = false;
	$("#chooseZlmxForm input[name='checkZlmx']").each(function(i){
		if(!$(this).is(":checked")){
			unchecked = true;
			return false;
		}
	})
	if(unchecked){
		$("#chooseZlmxForm #checkAll").removeAttr("checked");
	}else{
		$("#chooseZlmxForm #checkAll").prop("checked", true);
	}
}

/**
 * 页面初始化
 * @returns
 */
function init(){
	// 初始化请购明细选中状态
	var sczlid = $("#chooseZlmxForm input[name='sczlid']").val();
	var sczl_json = [];
	if($("#arrival_produce_formSearch").length > 0){
		if($("#arrival_produce_formSearch #sczl_json").val()){
			sczl_json = JSON.parse($("#arrival_produce_formSearch #sczl_json").val());
		}
	}
	if(sczl_json.length > 0){
		for (var i = 0; i < sczl_json.length; i++) {
			if(sczlid == sczl_json[i].sczlid){
				$("#chooseZlmxForm #check_" + sczl_json[i].sczlmxid).prop("checked", true);
			}
		}
		var unchecked = false;
		$("#chooseZlmxForm input[name='checkZlmx']").each(function(i){
			if(!$(this).is(":checked")){
				unchecked = true;
				return false;
			}
		})
		if(unchecked){
			$("#chooseZlmxForm #checkAll").removeAttr("checked");
		}else{
			$("#chooseZlmxForm #checkAll").prop("checked", true);
		}
	}
}

$(function(){
	init();
})