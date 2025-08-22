function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url="/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".mp4"){
        var url= "/ws/videoView?fjid=" + fjid;
        $.showDialog(url,'视频预览',viewVideoConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var viewVideoConfig={
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

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


function xz(fjid,name){
    jQuery('<form action="'+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' +
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
    .appendTo('body').submit().remove();
}

function view(id,name){
    $.showDialog("/record/record/viewRecord?processinstanceid="+id+"&access_token="+$("#ac_tk").val(),name,viewSpdConfig);
}

var viewSpdConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
