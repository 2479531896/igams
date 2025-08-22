/**
 * 返回上一页
 * @returns
 */
$("#weeklyStatis_jsrq_pt #rollbackweekly").bind("click",function(){
	var jsrqstart=$("#weeklyStatis_jsrq_pt #jsrqstart").val();
	var jsrqend=$("#weeklyStatis_jsrq_pt #jsrqend").val();
	console.log("12");
	$("#weeklyStatis_jsrq_pt").load("/ws/statistics/backweekLeadStatisPageByJsrq?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend);
})

/**
 * 查看对应归属平台信息
 * params ldtzbj 领导页面跳转标记
 * @returns
 */
function weekqueryByPtgs(ptgs,sign,e){
	var jsrqstart=$("#weeklyStatis_jsrq_pt #jsrqstart").val();
	var jsrqend=$("#weeklyStatis_jsrq_pt #jsrqend").val();
	$("#weeklyStatis_jsrq_pt").load("/ws/statictis/weeklyByYhidAndJsrq?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&ptgss="+ptgs+"&sign="+sign +"&xsbj=1"+"&ldtzbj=1");
}