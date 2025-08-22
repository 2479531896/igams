

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview?pageflg=1&fjid="+fjid
		window.location.href=url;
	}else if(type.toLowerCase()==".pdf"){
		var url="/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
		window.location.href=url;
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

function xz(fjid){
	jQuery('<form action="/ws/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}

