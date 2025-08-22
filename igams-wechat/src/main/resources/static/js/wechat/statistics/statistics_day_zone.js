/**
 * 返回上一页
 * @returns
 */
$("#daliyStatis #rollback").bind("click",function(){
	var jsrq=$("#daliyStatis #jsrq").val();
	$("#daliyStatis").load("/wechat/statistics/pageListLocal_daliy?jsrq="+jsrq);
})

/**
 * 查看微信日报
 * @returns
 */
function queryByName(yhid,sign,e){
	var jsrq=$("#daliyStatis #jsrq").val();
	$("#daliyStatis").load("/ws/statictis/dailyByYhid?jsrq="+jsrq+"&yhid="+yhid+"&sign="+sign+"&xsbj=1");
}