
/**
 * 返回按钮
 * @returns
 */
function back_view(){
    var jsrq=$("#sample_jsrq_formSearch #jsrq").val();
    $("#daliyStatis").load("/wechat/statistics/pageListLocal_daliy_jsrq?jsrq="+jsrq);
}
