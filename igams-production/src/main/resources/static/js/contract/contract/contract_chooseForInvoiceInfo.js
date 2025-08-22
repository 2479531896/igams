
/**
 * 筛选合同明细信息
 * @param e
 * @returns
 */
function selectHtmx(e) {
    var cxnr = $("#chooseContractInfoForm #cxnr").val();
    $("#chooseContractInfoForm tr[name=trHtmx]").each(function(){
        if(this.dataset.wlbm.indexOf(cxnr) != -1  || this.dataset.wlmc.indexOf(cxnr) != -1 || this.dataset.ychh.indexOf(cxnr) != -1){
            $("#chooseContractInfoForm #"+this.id).show();
        }else{
            $("#chooseContractInfoForm #"+this.id).hide();
        }
    })
}

/**
 * 全选点击事件
 * @param e
 * @returns
 */
function checkAllClick(e){
    if($("#chooseContractInfoForm #checkAll").is(":checked")){
        $("#chooseContractInfoForm input[name='checkHtmx']").each(function(i){
            $(this).prop("checked", true);
        })
        $("#chooseContractInfoForm #checkAll").prop("checked", true);
    }else{
        $("#chooseContractInfoForm input[name='checkHtmx']").each(function(i){
            $(this).prop("checked", false);
        })
        $("#chooseContractInfoForm #checkAll").prop("checked", false);
    }
}

/**
 * 点击事件
 * @param htmxid
 * @param e
 * @returns
 */
function checkClick(htmxid,e){
    if($("#chooseContractInfoForm #check_"+htmxid).is(":checked")){
        $("#chooseContractInfoForm #check_"+htmxid).prop("checked", false);
    }else{
        $("#chooseContractInfoForm #check_"+htmxid).prop("checked", true);
    }
    var unchecked = false;
    $("#chooseContractInfoForm input[name='checkHtmx']").each(function(i){
        if(!$(this).is(":checked")){
            unchecked = true;
            return false;
        }
    })
    if(unchecked){
        $("#chooseContractInfoForm #checkAll").prop("checked", false);
    }else{
        $("#chooseContractInfoForm #checkAll").prop("checked", true);
    }
}

/**
 * 页面初始化
 * @returns
 */
function init(){
    var htid = $("#chooseContractInfoForm input[name='htid']").val();
    var Json = [];
    if($("#chooseContractForm").length > 0){
        if($("#chooseContractForm #htmx_json").val()){
            Json = JSON.parse($("#chooseContractForm #htmx_json").val());
        }
    }else if($("#editInvoiceForm").length > 0){
        if($("#editInvoiceForm #fpmx_json").val()){
            Json = JSON.parse($("#editInvoiceForm #fpmx_json").val());
        }
    }
    if(Json.length > 0){
        for (var i = 0; i < Json.length; i++) {
            if(htid == Json[i].htid){
                $("#chooseContractInfoForm #check_" + Json[i].htmxid).prop("checked", true);
            }
        }
        var unchecked = false;
        $("#chooseContractInfoForm input[name='checkHtmx']").each(function(i){
            if(!$(this).is(":checked")){
                unchecked = true;
                return false;
            }
        })
        if(unchecked){
            $("#chooseContractInfoForm #checkAll").removeAttr("checked");
        }else{
            $("#chooseContractInfoForm #checkAll").prop("checked", true);
        }
    }
}

$(function(){
    init();
});