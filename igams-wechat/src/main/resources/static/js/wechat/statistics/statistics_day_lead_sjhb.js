
/**
 * 返回按钮
 * @returns
 */
function back_view(){
    var jsrq=$("#sjhb_formSearch #jsrq").val();
    $("#daliyStatis").load("/wechat/statistics/pageListLocal_daliy?jsrq="+jsrq);
}
