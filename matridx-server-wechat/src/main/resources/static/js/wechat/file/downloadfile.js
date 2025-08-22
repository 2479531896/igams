var time=9;
var settime = null;

function xzfile(fjid){
	time=9;
    var sign=$("#ajaxForm #sign").val();
    $("#ajaxForm").submit();
    $("#ajaxForm #xz").attr("disabled","disabled");
    settime = setInterval(function(){
    	$("#ajaxForm #xz").text("报告下载"+"("+time+")");
    	time=time-1;
    	if(time==-1){
    		$("#ajaxForm #xz").text("报告下载");
    		$("#ajaxForm #xz").removeAttr("disabled");
    		clearInterval(settime);
    	}
    },1000)
}

$(function(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		$("#ajaxForm #pageone").hide();
		$("#ajaxForm #pagetwo").show();
	} else {
		$("#ajaxForm #pageone").show();
		$("#ajaxForm #pagetwo").hide();
		$("#ajaxForm #xz").text("报告下载"+"("+10+")");
	    var sign=$("#ajaxForm #sign").val();
	    var fjid=$("#ajaxForm #fjid").val();
	    $("#ajaxForm").submit();
		$("#ajaxForm #xz").attr("disabled","disabled");
		settime = setInterval(function(){
			$("#ajaxForm #xz").text("报告下载"+"("+time+")");
			time=time-1;
			if(time==-1){
				$("#ajaxForm #xz").text("报告下载");
				$("#ajaxForm #xz").removeAttr("disabled");
				clearInterval(settime);
			}
		},1000)
	}
})