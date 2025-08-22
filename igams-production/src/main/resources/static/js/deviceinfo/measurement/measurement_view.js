function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'ͼƬԤ��',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'�ļ�Ԥ��',JPGMaterConfig);
	}else {
		$.alert("�ݲ�֧�������ļ���Ԥ���������ڴ���");
	} 
}

var JPGMaterConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "�� ��",
				className : "btn-default"
			}
		}
	};

function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action����·�������ͷ���
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}