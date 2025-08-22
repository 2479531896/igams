function generatePurchase(){
	$("#generatePurchaseForm #li_pdf").empty();
	$("#generatePurchaseForm #purchaseBtn").attr("disabled","disabled");
	var qgid=$("#generatePurchaseForm #qgid").val();
	$.ajax({
		url: $("#generatePurchaseForm #urlPrefix").val()+"/purchase/purchase/pagedataReplacePurchase",
		type: "post",
		dataType:'json',
		data:{qgid:qgid,access_token:$("#ac_tk").val()},
		success: function(data){
			if(data!=null && data.length>0){
				var html="";
				$("#generatePurchaseForm #li_word").html(html);
				for(var i=0;i<data.length;i++){
					if(data[i].status=="success"){
						html+="<li><a href='#' class='btn3' onclick='xz(\""+data[i].fjcfbDto.fjid+"\",event)' title='"+data[i].fjcfbDto.wjm+"'> <span class='btn-inner'>WORD版(文件"+(i+1)+")</span> <div class='btnbg-x'></div></a></li>";
					}
				}
				$("#generatePurchaseForm #li_word").append(html);
				$.ajax({
					async: false,
					url: $("#generatePurchaseForm #urlPrefix").val()+"/purchase/purchase/pagedataGetPurchasePDF",
					type: "post",
					dataType:'json',
					data:{"ywid":data[0].fjcfbDto.ywid,"ywlx":data[0].pdf_ywlx,"access_token":$("#ac_tk").val()},
					success: function(param){
						if(param.fjcfbDto!=null&&param.fjcfbDto!=""){
							var html="";
							$("#generatePurchaseForm #li_pdf").html(html);
							for(var i=0;i<param.fjcfbDto.length;i++){
								html+="<li><a href='#' class='btn3' onclick='ylpdf(\""+param.fjcfbDto[i].fjid+"\",\""+param.fjcfbDto[i].wjm+"\")' title='"+param.fjcfbDto[i].wjm+"'> <span class='btn-inner'>PDF版(文件"+(i+1)+")</span> <div class='btnbg-x'></div></a></li>";
							}
							$("#generatePurchaseForm #li_pdf").append(html);
						}
					}
				})
			}
		}
	})
}

function ylpdf(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	var url= $("#generatePurchaseForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid+"&pdf_flg=1";
    $.showDialog(url,'文件预览',viewPreViewConfig);
}

var viewPreViewConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function xz(fjid){
	jQuery('<form action="'+$("#generatePurchaseForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

