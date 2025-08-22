
/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
    if($("#chooseBommxForm #checkAll").is(":checked")){
        $("#chooseBommxForm input[name='checkBomx']").each(function(i){
            $(this).prop("checked", true);
        })
        $("#chooseBommxForm #checkAll").prop("checked", true);
    }else{
        $("#chooseBommxForm input[name='checkBomx']").each(function(i){
            $(this).removeAttr("checked");
        })
        $("#chooseBommxForm #checkAll").removeAttr("checked");
    }
}

/**
 * 行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(bommxid,e){

    if($("#chooseBommxForm #check_"+bommxid).is(":checked")){
        $("#chooseBommxForm #check_"+bommxid).removeAttr("checked");
    }else{
        $("#chooseBommxForm #check_"+bommxid).prop("checked", true);
    }
    var unchecked = false;
    $("#chooseBommxForm input[name='checkBomx']").each(function(i){
        if(!$(this).is(":checked")){
            unchecked = true;
            return false;
        }
    })
    if(unchecked){
        $("#chooseBommxForm #checkAll").removeAttr("checked");
    }else{
        $("#chooseBommxForm #checkAll").prop("checked", true);
    }
}

/**
 * 页面初始化
 * @returns
 */
function init(){
    // 初始化选中状态
    var bommxid = $("#chooseBommxForm input[name='bommxid']").val();
    var bommx_json = [];
    if($("#progress_details").length > 0){
        if($("#progress_details #bommx_json").val()){
            bommx_json = JSON.parse($("#progress_details #bommx_json").val());
        }
    }
    if(bommx_json.length > 0){
        for (var i = 0; i < bommx_json.length; i++) {
            if(bommxid == bommx_json[i].bommxid){
                $("#chooseBommxForm #check_" + bommx_json[i].bommxid).prop("checked", true);
            }
        }
        var unchecked = false;
        $("#chooseBommxForm input[name='checkBomx']").each(function(i){
            if(!$(this).is(":checked")){
                unchecked = true;
                return false;
            }
        })
        if(unchecked){
            $("#chooseBommxForm #checkAll").removeAttr("checked");
        }else{
            $("#chooseBommxForm #checkAll").prop("checked", true);
        }
    }
}

$(function(){
    init();
})