
$(document).ready(function(){

    // //0.初始化fileinput
    // var oFileInput = new FileInput();
    // var picking_params=[];
    // picking_params.prefix=$("#progressAuditViewForm #urlPrefix").val();
    // oFileInput.Init("progressAuditViewForm","displayUpInfo",2,1,"pro_file",null,picking_params);
});
function closeNav() {
    $("#progressAuditViewForm #t_div").animate({left:'-380px'},"slow");
    $("#progressAuditViewForm .tr").attr("style","background-color:white;");
}

function viewmore(xqjhmxid){
    $.ajax({
        type:'post',
        url:$("#progressAuditViewForm #urlPrefix").val()+"/ws/production/viewMoreProgressDetails",
        cache: false,
        data: {"xqjhmxid":xqjhmxid,"url":$("#progressAuditViewForm #url").val(),"urlPrefix":$("#progressAuditViewForm #urlPrefix").val()},
        dataType:'json',
        success:function(data){
            var wlmc=data.xqjhmxDto.wlmc;
            if(wlmc==null){
                wlmc="";
            }
            var wlbm=data.xqjhmxDto.wlbm;
            if(wlbm==null){
                wlbm="";
            }
            var gg=data.xqjhmxDto.gg;
            if(gg==null){
                gg="";
            }
            var jldw=data.xqjhmxDto.jldw;
            if(jldw==null){
                jldw="";
            }
            var scs=data.xqjhmxDto.scs;
            if(scs==null){
                scs="";
            }
            var sl=data.xqjhmxDto.sl;
            if(sl==null){
                sl="";
            }
            var scsl=data.xqjhmxDto.scsl;
            if(scsl==null){
                scsl="";
            }
            var yq=data.xqjhmxDto.yq;
            if(yq==null){
                yq="";
            }
            //返回值
            $("#progressAuditViewForm #wlbm").text(wlbm);
            $("#progressAuditViewForm #wlmc").text(wlmc);
            $("#progressAuditViewForm #gg").text(gg);
            $("#progressAuditViewForm #jldw").text(jldw);
            $("#progressAuditViewForm #scs").text(scs);
            $("#progressAuditViewForm #sl").text(sl);
            $("#progressAuditViewForm #scsl").text(scsl);
            $("#progressAuditViewForm #yq").text(yq);
            $("#progressAuditViewForm #t_div").animate({left:'0px'},"slow");
            $("#progressAuditViewForm .tr").attr("style","background-color:white;");
            $("#progressAuditViewForm #"+xqjhmxid).attr("style","background-color:#B3F7F7;");
        }
    });
}
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action="'+$('#progressAuditViewForm #urlPrefix').val() + '/ws/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
function yl(fjid,wjm,zhwjxx){
    var urlPrefix= $("#progressAuditViewForm #urlPrefix").val();
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
// /**
//  * 附件上传回调
//  * @param fjid
//  * @returns
//  */
// function displayUpInfo(fjid){
//     if(!$("#progressAuditViewForm #fjids").val()){
//         $("#progressAuditViewForm #fjids").val(fjid);
//     }else{
//         $("#progressAuditViewForm #fjids").val($("#progressAuditViewForm #fjids").val()+","+fjid);
//     }
// }