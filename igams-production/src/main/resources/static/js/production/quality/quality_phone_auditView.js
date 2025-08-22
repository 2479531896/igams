
function closeNav() {
    $("#qualityAuditViewForm #t_div").animate({left:'-380px'},"slow");
    $("#qualityAuditViewForm .tr").attr("style","background-color:white;");
}

function viewmore(zlxymxid){
    $.ajax({
        type:'post',
        url:$("#qualityAuditViewForm #urlPrefix").val()+"/ws/production/pagedataAgreementDetatils",
        cache: false,
        data: {"zlxymxid":zlxymxid,"urlPrefix":$("#qualityAuditViewForm #urlPrefix").val()},
        dataType:'json',
        success:function(data){
            var wlbm=data.zlxymxDto.wlbm;
            var wlmc=data.zlxymxDto.wlmc;
            var jldw=data.zlxymxDto.jldw;
            var gg=data.zlxymxDto.gg;
            var scs=data.zlxymxDto.scs;
            var sjxmhmc=data.zlxymxDto.sjxmhmc;
            var jszb=data.zlxymxDto.jszb;
            var zlyq=data.zlxymxDto.zlyq;
            var ysbz=data.zlxymxDto.ysbz;
            if(wlbm==null){
                wlbm="";
            }
            if(wlmc==null){
                wlmc="";
            }
            if(jldw==null){
                jldw="";
            }
            if(gg==null){
                gg="";
            }
            if(sjxmhmc==null){
                sjxmhmc="";
            }
            if(jszb==null){
                jszb="";
            }
            if(zlyq==null){
                zlyq="";
            }
            if(ysbz==null){
                ysbz="";
            }
            if(scs==null){
                scs="";
            }
            //返回值
            $("#qualityAuditViewForm #wlbm").text(wlbm);
            $("#qualityAuditViewForm #wlmc").text(wlmc);
            $("#qualityAuditViewForm #jldw").text(jldw);
            $("#qualityAuditViewForm #gg").text(gg);
            $("#qualityAuditViewForm #sjxmhmc").text(sjxmhmc);
            $("#qualityAuditViewForm #jszb").text(jszb);
            $("#qualityAuditViewForm #zlyq").text(zlyq);
            $("#qualityAuditViewForm #ysbz").text(ysbz);
            $("#qualityAuditViewForm #scs").text(scs);
            $("#qualityAuditViewForm #t_div").animate({left:'0px'},"slow");
            $("#qualityAuditViewForm .tr").attr("style","background-color:white;");
            $("#qualityAuditViewForm #"+zlxymxid).attr("style","background-color:#B3F7F7;");
        }
    });
}

function yl(fjid,wjm,zhwjxx){
    var urlPrefix= $("#qualityAuditViewForm #urlPrefix").val();
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
        }else if(type.toLowerCase()==".pdf"){
            var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
            window.location.href=url;
        }else {
            alert("暂不支持其他文件的预览，敬请期待！");
        }
    }
}
