/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectLlmx(e) {
    var cxnr = $("#chooseLlmxForm #cxnr").val();
    $("#chooseLlmxForm tr[name=trLlmx]").each(function(){
        if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1){
            $("#chooseLlmxForm #"+this.id).show();
        }else{
            $("#chooseLlmxForm #"+this.id).hide();
        }
    })
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
    if($("#chooseLlmxForm #checkAll").is(":checked")){
        $("#chooseLlmxForm input[name='checkLlmx']").each(function(i){
            $(this).prop("checked", true);
        })
        $("#chooseLlmxForm #checkAll").prop("checked", true);
    }else{
        $("#chooseLlmxForm input[name='checkLlmx']").each(function(i){
            $(this).prop("checked", false);
        })
        $("#chooseLlmxForm #checkAll").prop("checked", false);
    }
}

/**
 * 请购明细行点击事件
 * @param llmxid
 * @param e
 * @returns
 */
function checkClick(llmxid,e){
    if($("#chooseLlmxForm #check_"+llmxid).is(":checked")){
        $("#chooseLlmxForm #check_"+llmxid).prop("checked", false);
    }else{
        $("#chooseLlmxForm #check_"+llmxid).prop("checked", true);
    }
    var unchecked = false;
    $("#chooseLlmxForm input[name='checkLlmx']").each(function(i){
        if(!$(this).is(":checked")){
            unchecked = true;
            return false;
        }
    })
    if(unchecked){
        $("#chooseLlmxForm #checkAll").prop("checked", false);
    }else{
        $("#chooseLlmxForm #checkAll").prop("checked", true);
    }
}

/**
 * 页面初始化
 * @returns
 */
function init(){
    // 初始化请购明细选中状态
    var llid = $("#chooseLlmxForm input[name='llid']").val();
    var llmxJson = [];
    if($("#chooseLlxxForm").length > 0){
        if($("#chooseLlxxForm #llmx_json").val()){
            llmxJson = JSON.parse($("#chooseLlxxForm #llmx_json").val());
        }
    }
    if(llmxJson.length > 0){
        for (var i = 0; i < llmxJson.length; i++) {
            if(llid == llmxJson[i].llid){
                $("#chooseLlmxForm #check_" + llmxJson[i].llmxid).prop("checked", true);
            }
        }
        var unchecked = false;
        $("#chooseLlmxForm input[name='checkLlmx']").each(function(i){
            if(!$(this).is(":checked")){
                unchecked = true;
                return false;
            }
        })
        if(unchecked){
            $("#chooseLlmxForm #checkAll").removeAttr("checked");
        }else{
            $("#chooseLlmxForm #checkAll").prop("checked", true);
        }
    }
}

$(function(){
    init();
})