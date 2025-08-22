/**
 * 返回上一页
 * @returns
 */
$("#weeklyStatis #rollbackweekly").bind("click",function(){
	var jsrqstart=$("#weeklyStatis #jsrqstart").val();
	var jsrqend=$("#weeklyStatis #jsrqend").val();
	console.log("12");
	$("#weeklyStatis").load("/ws/statistics/backweekLeadStatisPage?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend);
})

/**
 * 查看微信日报
 * @returns
 */
function weekqueryByName(yhid,sign,e){
	var jsrqstart=$("#weeklyStatis #jsrqstart").val();
	var jsrqend=$("#weeklyStatis #jsrqend").val();
	$("#weeklyStatis").load("/ws/statictis/weeklyByYhid?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&yhid="+yhid+"&sign="+sign +"&xsbj=1");
}