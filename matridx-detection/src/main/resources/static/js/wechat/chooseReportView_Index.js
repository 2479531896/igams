$(function (){
	//Mngs
	$("#chooseReportViewIndex #mngs_input").unbind("click").click(function(){
		var url= "/ws/viewReport?ybbh=" + $("#ybbh").val()+"&flag=0";
		window.location.href=url;
	});
	//甲乙流
	$("#chooseReportViewIndex #influenza_input").unbind("click").click(function(){
		var url= "/ws/viewReport?ybbh=" + $("#ybbh").val()+"&flag=1&jclxdm=TYPE_FLU";
		window.location.href=url;
	});
});