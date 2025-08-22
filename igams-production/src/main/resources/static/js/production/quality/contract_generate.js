function generatQualiteContract(){
    $("#generatQualityForm #contractBtn").attr("disabled","disabled");
    var zlxyid=$("#generatQualityForm #zlxyid").val();
    $.ajax({
        url: $("#generatQualityForm #urlPrefix").val()+"/agreement/agreement/pagedataReplaceContract",
        type: "post",
        dataType:'json',
        data:{zlxyid:zlxyid,access_token:$("#ac_tk").val()},
        success: function(data){
            if(data.status=="success"){
                $("#generatQualityForm #li_word").html("");
                var html="<li><a href='#' class='btn3' onclick='xz(\""+data.fjcfbDto.fjid+"\",event)' title='"+data.fjcfbDto.wjm+"'> <span class='btn-inner'>WORD版</span> <div class='btnbg-x'></div></a></li>";
                $("#generatQualityForm #li_word").append(html);
                setTimeout(() => {
                    $.ajax({
                        url: $("#generatQualityForm #urlPrefix").val()+"/agreement/agreement/pagedataGetConttractPDF",
                        type: "post",
                        dataType:'json',
                        data:{ywid:data.fjcfbDto.ywid,ywlx:data.pdf_ywlx,access_token:$("#ac_tk").val()},
                        success: function(param){
                            if(param.fjcfbDto!=null&&param.fjcfbDto!=""){
                                $("#generatQualityForm #li_pdf").html("");
                                var html="<li><a href='#' class='btn3' onclick='xz(\""+param.fjcfbDto.fjid+"\",event)' title='"+param.fjcfbDto.wjm+"'> <span class='btn-inner'>PDF版</span> <div class='btnbg-x'></div></a></li>";
                                $("#generatQualityForm #li_pdf").append(html);
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
            url: $("#generatQualityForm #urlPrefix").val()+"/agreement/agreement/pagedataGetConttractPDF",
            type: "post",
            dataType:'json',
            data:{ywid:ywid,ywlx:pdf_ywlx,access_token:$("#ac_tk").val()},
            success: function(param){
                if(param.fjcfbDto!=null&&param.fjcfbDto!=""){
                    $("#generatQualityForm #li_pdf").html("");
                    var html="<li><a href='#' class='btn3' onclick='xz(\""+param.fjcfbDto.fjid+"\",event)' title='"+param.fjcfbDto.wjm+".docx'> <span class='btn-inner'>PDF版</span> <div class='btnbg-x'></div></a></li>";
                    $("#generatQualityForm #li_pdf").append(html);
                }else {
                    getPDF(ywid,pdf_ywlx);
                }
            }
        })
    }, 3000);
}

function xz(fjid){
    jQuery('<form action="'+$("#generatQualityForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

