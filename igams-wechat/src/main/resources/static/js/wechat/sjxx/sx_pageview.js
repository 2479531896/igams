$("#ckzbjs").click(function(){
	var url = "/ws/getckzbjs";
	$.showDialog(url,'参考指标解释',CkzbjsConfig);
})

var CkzbjsConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
};

$(function(){
	$("#outtab li:eq(0)").addClass("active");
	var id=$("#outtab li:eq(0) a").attr("href").substr(1);
	$("#"+id).addClass("in active");
})
