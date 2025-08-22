function yl(fjid,wjm,zhwjxx){
    var urlPrefix= $("#marketingContractViewFjForm #urlPrefix").val();
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


function xz(fjid){
     var url =  $("#marketingContractViewFjForm #urlPrefix").val() + "/ws/production/downloadFile?fjid="+fjid;
     window.location.href=url;
}