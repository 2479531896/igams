/**
 * 返回上一页
 * @returns
 */
$("#weeklyStatis_jsrq #rollbackweekly").bind("click",function(){
	var jsrqstart=$("#weeklyStatis_jsrq #jsrqstart").val();
	var jsrqend=$("#weeklyStatis_jsrq #jsrqend").val();
	console.log("12");
	$("#weeklyStatis_jsrq").load("/ws/statistics/backweekLeadStatisPageByJsrq?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend);
})

/**
 * 查看微信日报
 * @returns
 */
function weekqueryByName(yhid,sign,e){
	var jsrqstart=$("#weeklyStatis_jsrq #jsrqstart").val();
	var jsrqend=$("#weeklyStatis_jsrq #jsrqend").val();
	$("#weeklyStatis_jsrq").load("/ws/statictis/weeklyByYhidAndJsrq?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&yhid="+yhid+"&sign="+sign +"&xsbj=1");
}