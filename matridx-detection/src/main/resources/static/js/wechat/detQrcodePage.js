
function creatQrcode(){
    var id = $("#id").val()
    if (id != null && id != undefined && id != ''){
        $("#qrcode").qrcode({
            render: 'div',
            size: 200,
            text: $("#id").val().toUpperCase().toString()
        })
    }
}
// 预览报告
function viewPDF(fjid){
    var url= "/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
    window.location.href=url;
}

$(function () {
    creatQrcode()
})