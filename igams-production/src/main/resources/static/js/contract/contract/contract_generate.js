function generateContract(){
	$("#generateContractForm #contractBtn").attr("disabled","disabled");
	var htid=$("#generateContractForm #htid").val();
	$.ajax({
		url: $("#generateContractForm #urlPrefix").val()+"/contract/contract/pagedataReplaceContract",
		type: "post",
		dataType:'json',
		data:{htid:htid,access_token:$("#ac_tk").val()},
		success: function(data){
			if(data.status=="success"){
				$("#generateContractForm #li_word").html("");
				var html="<li><a href='#' class='btn3' onclick='xz(\""+data.fjcfbDto.fjid+"\",event)' title='"+data.fjcfbDto.wjm+"'> <span class='btn-inner'>WORD版</span> <div class='btnbg-x'></div></a></li>";
				$("#generateContractForm #li_word").append(html);
				setTimeout(() => {
					$.ajax({
						url: $("#generateContractForm #urlPrefix").val()+"/contract/contract/pagedataGetConttractPDF",
						type: "post",
						dataType:'json',
						data:{ywid:data.fjcfbDto.ywid,ywlx:data.pdf_ywlx,access_token:$("#ac_tk").val()},
						success: function(param){
							if(param.fjcfbDto!=null&&param.fjcfbDto!=""){
								$("#generateContractForm #li_pdf").html("");
								var html="<li><a href='#' class='btn3' onclick='xz(\""+param.fjcfbDto.fjid+"\",event)' title='"+param.fjcfbDto.wjm+"'> <span class='btn-inner'>PDF版</span> <div class='btnbg-x'></div></a></li>";
								$("#generateContractForm #li_pdf").append(html);
							}else {
								getPDF(data.fjcfbDto.ywid,data.pdf_ywlx);
							}
						}
					})
				}, 3000);
			}
		}
	})
}

function getPDF(ywid,pdf_ywlx){
	setTimeout(() => {
		$.ajax({
			url: $("#generateContractForm #urlPrefix").val()+"/contract/contract/pagedataGetConttractPDF",
			type: "post",
			dataType:'json',
			data:{ywid:ywid,ywlx:pdf_ywlx,access_token:$("#ac_tk").val()},
			success: function(param){
				if(param.fjcfbDto!=null&&param.fjcfbDto!=""){
					$("#generateContractForm #li_pdf").html("");
					var html="<li><a href='#' class='btn3' onclick='xz(\""+param.fjcfbDto.fjid+"\",event)' title='"+param.fjcfbDto.wjm+".docx'> <span class='btn-inner'>PDF版</span> <div class='btnbg-x'></div></a></li>";
					$("#generateContractForm #li_pdf").append(html);
				}else {
					getPDF(ywid,pdf_ywlx);
				}
			}
		})
	}, 3000);
}

function xz(fjid){
	jQuery('<form action="'+$("#generateContractForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}

