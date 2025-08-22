function viewmore(xsmxid){
    $.ajax({
        type:'post',
        url:$("#saleAuditViewForm #urlPrefix").val()+"/ws/production/viewMoreXsmx",
        cache: false,
        data: {"xsmxid":xsmxid,"urlPrefix":$("#saleAuditViewForm #urlPrefix").val()},
        dataType:'json',
        success:function(data){
            // 显示质量类别，物料类别，物料子类别，单位，生产商，货号
            var wlbm=data.xsmxDto.wlbm;
            var wlmc=data.xsmxDto.wlmc;
            var gg=data.xsmxDto.gg;
            var jldw=data.xsmxDto.jldw;
            var sl=data.xsmxDto.sl;
            var hsdj=data.xsmxDto.hsdj;
            var jsze=data.xsmxDto.jsze;
            var suil=data.xsmxDto.suil;
            var cplx=data.xsmxDto.cplx;
            if(wlbm==null){
                wlbm="";
            }
            if(wlmc==null){
                wlmc="";
            }
            if(gg==null){
                gg="";
            }
            if(jldw==null){
                jldw="";
            }
            if(sl==null){
                sl="";
            }
            if(hsdj==null){
                hsdj="";
            }
            if(jsze==null){
                jsze="";
            }
            if(suil==null){
                suil="";
            }
            if(cplx==null){
                cplx="";
            }
            //返回值
            $("#saleAuditViewForm #wlbm").text(wlbm);
            $("#saleAuditViewForm #wlmc").text(wlmc);
            $("#saleAuditViewForm #gg").text(gg);
            $("#saleAuditViewForm #jldw").text(jldw);
            $("#saleAuditViewForm #sl").text(sl);
            $("#saleAuditViewForm #hsdj").text(hsdj);
            $("#saleAuditViewForm #jsze").text(jsze);
            $("#saleAuditViewForm #suil").text(suil);
            $("#saleAuditViewForm #cplx").text(cplx);
            $("#saleAuditViewForm #t_div").animate({left:'0px'},"slow");
            $("#saleAuditViewForm .tr").attr("style","background-color:white;");
            $("#saleAuditViewForm #"+hwllid).attr("style","background-color:#B3F7F7;");
        }
    });
}
function yl(fjid,wjm,zhwjxx){
    var urlPrefix= $("#saleAuditViewForm #urlPrefix").val();
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(zhwjxx!=null && zhwjxx!=''){
        var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid+"&pdf_flg=1";
        window.location.href=url;
    }else{
        if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
            var url=urlPrefix+"/ws/sjxxpripreview?pageflg=1&fjid="+fjid
            window.location.href=url;
        }else if(type.toLowerCase()==".pdf"||type.toLowerCase()==".doc"||type.toLowerCase()==".docx"){
            var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
            window.location.href=url;
        }else {
            alert("暂不支持其他文件的预览，敬请期待！");
        }
    }
}

function closeNav() {
    $("#saleAuditViewForm #t_div").animate({left:'-200px'},"slow");
    $("#saleAuditViewForm .tr").attr("style","background-color:white;");
}