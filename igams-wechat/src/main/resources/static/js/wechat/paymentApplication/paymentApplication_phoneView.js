function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    var urlPrefix= $("#ajaxForm #urlPrefix").val();
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=urlPrefix+"/ws/sjxxpripreview?pageflg=1&fjid="+fjid
        window.location.href=url;
    }else if(type.toLowerCase()==".pdf"){
        var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
        window.location.href=url;
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

function xz(fjid){
    var url =  "/ws/file/downloadFile?fjid="+fjid;
    window.location.href=url;
}