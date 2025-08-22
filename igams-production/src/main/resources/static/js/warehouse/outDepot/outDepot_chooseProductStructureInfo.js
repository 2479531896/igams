

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
    if($("#chooseProductStructureForm #checkAll").is(":checked")){
        $("#chooseProductStructureForm input[name='checkCpjgmx']").each(function(i){
            $(this).prop("checked", true);
        })
        $("#chooseProductStructureForm #checkAll").prop("checked", true);
    }else{
        $("#chooseProductStructureForm input[name='checkCpjgmx']").each(function(i){
            $(this).removeAttr("checked");
        })
        $("#chooseProductStructureForm #checkAll").removeAttr("checked");
    }
}

/**
 * 请购明细行点击事件
 * @param cpjgmxid
 * @param e
 * @returns
 */
function checkClick(cpjgmxid,e){
    if($("#chooseProductStructureForm #check_"+cpjgmxid).is(":checked")){
        $("#chooseProductStructureForm #check_"+cpjgmxid).removeAttr("checked");
    }else{
        $("#chooseProductStructureForm #check_"+cpjgmxid).prop("checked", true);
    }
    var unchecked = false;
    $("#chooseProductStructureForm input[name='checkCpjgmx']").each(function(i){
        if(!$(this).is(":checked")){
            unchecked = true;
            return false;
        }
    })
    if(unchecked){
        $("#chooseProductStructureForm #checkAll").removeAttr("checked");
    }else{
        $("#chooseProductStructureForm #checkAll").prop("checked", true);
    }
}

/**
 * 页面初始化
 * @returns
 */
function init(){
    // 初始化明细选中状态
    var htmxid = $("#chooseProductStructureForm input[name='htmxid']").val();
    var htmxJson = [];
    if($("#chooseContractDetailForm").length > 0){
        if($("#chooseContractDetailForm #htmx_json").val()){
            htmxJson = JSON.parse($("#chooseContractDetailForm #htmx_json").val());
        }
    }
    if(htmxJson.length > 0){
        for (var i = 0; i < htmxJson.length; i++) {
            if(htmxid == htmxJson[i].htmxid){
                $("#chooseProductStructureForm #check_" + htmxJson[i].cpjgmxid).prop("checked", true);
            }
        }
        var unchecked = false;
        $("#chooseProductStructureForm input[name='checkCpjgmx']").each(function(i){
            if(!$(this).is(":checked")){
                unchecked = true;
                return false;
            }
        })
        if(unchecked){
            $("#chooseProductStructureForm #checkAll").removeAttr("checked");
        }else{
            $("#chooseProductStructureForm #checkAll").prop("checked", true);
        }
    }
}

$(function(){
    init();
    jQuery('#chooseProductStructureForm .chosen-select').chosen({width: '100%'});
})