$(function(){
	laydate.render({
		  elem: '#statistical #jsrq'
		});
	$("#layer").slideDown("slow");
})


function confirm_view(){
	var jsrq=$("#statistical #materInfoTab #jsrq").val();
	if(jsrq!=null&&jsrq!=""){
		$("#statistical").load("/wechat/statistics/pageListLocal_daliy?jsrq="+jsrq);
	}else{
		$.alert("统计周期不能为空");
	}
	
}