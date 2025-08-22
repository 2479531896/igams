function view(fjid){
	var url= "/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
	//定时器是要适用于苹果系统，不加得话会无法触发window.location.href
	setTimeout(
        function(){
            window.location.href = url;
       }, 0);
}

