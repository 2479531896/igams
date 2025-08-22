/**
 * 筛选请购明细信息
 * @param e
 * @returns
 */
function selectQrmx(e) {
    var cxnr = $("#chooseQrmxForm #cxnr").val();
    $("#chooseQrmxForm tr[name=trQrmx]").each(function(){
        if(this.dataset.qrdh.indexOf(cxnr) != -1  || this.dataset.sqrmc.indexOf(cxnr) != -1){
            $("#chooseQrmxForm #"+this.id).show();
        }else{
            $("#chooseQrmxForm #"+this.id).hide();
        }
    })
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
    if($("#chooseQrmxForm #checkAll").is(":checked")){
        $("#chooseQrmxForm input[name='checkQrmx']").each(function(i){
            $(this).prop("checked", true);
        })
        $("#chooseQrmxForm #checkAll").prop("checked", true);
    }else{
        $("#chooseQrmxForm input[name='checkQrmx']").each(function(i){
            $(this).removeAttr("checked");
        })
        $("#chooseQrmxForm #checkAll").removeAttr("checked");
    }
}

/**
 * 确认明细行点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function checkClick(qrmxid,e){
    if($("#chooseQrmxForm #check_"+qrmxid).is(":checked")){
        $("#chooseQrmxForm #check_"+qrmxid).removeAttr("checked");
    }else{
        $("#chooseQrmxForm #check_"+qrmxid).prop("checked", true);
    }
    var unchecked = false;
    $("#chooseQrmxForm input[name='checkQrmx']").each(function(i){
        if(!$(this).is(":checked")){
            unchecked = true;
            return false;
        }
    })
    if(unchecked){
        $("#chooseQrmxForm #checkAll").removeAttr("checked");
    }else{
        $("#chooseQrmxForm #checkAll").prop("checked", true);
    }
}

/**
 * 页面初始化
 * @returns
 */
function init(){
    // 初始化请购明细选中状态
    var qrid = $("#chooseQrmxForm input[name='qrid']").val();
    var fkmxJson = [];
    if($("#chooseQgxxForm").length > 0){
        if($("#confirmChoose_xz_formSearch #qrmx_json").val()){
            fkmxJson = JSON.parse($("#confirmChoose_xz_formSearch #qrmx_json").val());
        }
    }
    if(fkmxJson.length > 0){
        for (var i = 0; i < fkmxJson.length; i++) {
            if(qrid == htmxJson[i].qgid){
                $("#chooseQrmxForm #check_" + fkmxJson[i].qrmxid).prop("checked", true);
            }
        }
        var unchecked = false;
        $("#chooseQrmxForm input[name='checkQrmx']").each(function(i){
            if(!$(this).is(":checked")){
                unchecked = true;
                return false;
            }
        })
        if(unchecked){
            $("#chooseQrmxForm #checkAll").removeAttr("checked");
        }else{
            $("#chooseQrmxForm #checkAll").prop("checked", true);
        }
    }
}

$(function(){
    init();
    jQuery('#chooseQrmxForm .chosen-select').chosen({width: '100%'});
})