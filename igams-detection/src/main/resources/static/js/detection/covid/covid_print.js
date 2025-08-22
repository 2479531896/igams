var beforePrint = function() {
    console.log('Functionality to run before printing.');
};

var afterPrint = function() {
    console.log('Functionality to run after printing');
};

if (window.matchMedia) {
    var mediaQueryList = window.matchMedia('print');
    mediaQueryList.addListener(function(mql) {
        if (mql.matches) {
            beforePrint();
        } else {
            afterPrint();
        }
    });
}

window.onbeforeprint = beforePrint;
window.onafterprint = afterPrint;


$(document).ready(function(){
    var data=JSON.parse($("#fzjcxxDtos_json").val());
    for(var i=0;i<data.length;i++){
    //     $.ajax({
    //         url: $("#menuurl").val()+'/wechat/getQRCode',
    //         type: 'post',
    //         dataType: 'json',
    //         data : {"qrCode" : data[i].bbzbh},
    //         success: function(res) {
    //             if(res.status == 'success'){
    //                 $("#qrcode_"+i).attr("src", $("#menuurl").val()+res.filePath);
    //             }else{
    //                 $.alert(res.message);
    //             }
    //             // $("#fastPay_ajaxForm .preBtn").attr("disabled", false);
    //         }
    //     });
        $("#qrcode_"+i).qrcode({
            render: 'table',
            size: 60,
            background: "#ffffff",  //背景颜色
            foreground: "#000000", //二维码颜色
            text: data[i].fzjcid.toString()
        })
    }
    var csqrCode=$("#csqrCode").val();
    if (csqrCode){
        $("#csqrCodeDiv").qrcode({
            render: 'table',
            size: 110,
            background: "#ffffff",  //背景颜色
            foreground: "#000000", //二维码颜色
            text: csqrCode.toString()
        })
    }
    window.print();
});