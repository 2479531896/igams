$(function(){
	$("#tab ul li:eq(0)").addClass("active");
	var tabbodyid=$("#tab ul li:eq(0) a").attr("href");
	$(tabbodyid).addClass("active in");
});
