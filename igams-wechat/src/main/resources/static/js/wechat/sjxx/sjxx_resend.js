function pageInit(){
    var l_jclxs = $("#resendReportForm input[name='jclx']");
    if (l_jclxs){
        l_jclxs.unbind("click").click(function(){
            searchJczlx(this);
        });
    }
}

function searchJczlx(element){
    if(!element)
        return;
    //没有选中，则页面端进行清空
    if(!element.checked){
        var l_jczlxs = $("#resendReportForm div[name='div_"+$(element).val()+"']");
        for(var i=0;i<l_jczlxs.length;i++){
            $(l_jczlxs[i]).remove();
        }
    }else{
        var jcxmid = $(element).val();
        $.ajax({
            url: '/inspection/inspection/pagedataResendSubDetect',
            type: 'post',
            async: false,
            data: {
                "jcxmid": jcxmid,
                "access_token": $("#ac_tk").val(),
            },
            dataType: 'json',
            success: function(result) {
                var data = result.subdetectlist;
                var jczxmHtml = "";
                if(data != null && data.length > 0){
                    $.each(data,function(i){
                        jczxmHtml += '<div class="col-sm-3 col-xs-6" name="div_'+data[i].fcsid+'"> ' +
                            '<div class="form-group"><label class="checkboxLabel checkbox-inline" >' +
                            '<input type="checkbox" name="jczlx" value="'+data[i].csid+'" fcsid="'+data[i].fcsid+'"/>' +
                            '<span>'+data[i].csmc+'</span>' +
                            "</label></div></div>";
                    });
                    $("#resendReportForm #jczlx_div").append(jczxmHtml);
                }
            },
            error: function(xhr, status, error) {
               $.alert("请求失败，请重试！");
            }
        });
    }
}

$(function(){
    //pageInit();
})
