function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#viewFileForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#viewFileForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};