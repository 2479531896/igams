// function GetQueryString(name) {
// 　　　var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
//      var r = window.location.search.substr(1).match(reg);
// 　　　if (r!=null) return (r[2]);
// 　　　return null;
// }

$(document).ready(function(){
	// var id = GetQueryString("id");
	// if(id!=null) {
	// 　　var jsid = decodeURIComponent(id);
	// 	var gdjl = $("#"+jsid).offset().top;
	// 	$('body,html').animate({
	// 	    scrollTop: gdjl
	// 	  });
	// }
	var val = document.getElementById("str").value;
	var dto = JSON.parse(val);
	if(dto.nr!='null'){
		var html="<div style='padding-left:20px;font-size: 40px'><span>"+dto.nr+"</span><span class='glyphicon glyphicon-ok-sign' style='color:green;'></span></div>";
		document.getElementById("answer").innerHTML=html;
	}
});