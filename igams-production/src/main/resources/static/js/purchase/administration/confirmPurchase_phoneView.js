function viewmore(qrmxid){
    $.ajax({
        type:'post',
        url:$("#purchaseAuditViewForm #urlPrefix").val()+"/ws/production/viewMorePurchaseConfirm",
        cache: false,
        data: {"qrmxid":qrmxid},
        dataType:'json',
        success:function(data){
            var hwmc=data.xzqgqrmxDto.hwmc;
            var hwgg=data.xzqgqrmxDto.hwgg;
            var sl=data.xzqgqrmxDto.sl;
            var jg=data.xzqgqrmxDto.jg;
            var hwjldw=data.xzqgqrmxDto.hwjldw;
            if(hwmc==null){
                hwmc="";
            }
            if(hwgg==null){
                hwgg="";
            }
            if(hwjldw==null){
                hwjldw="";
            }
            if(jg==null || jg==''){
                jg=0;
            }
            if(sl==null || sl==''){
                sl=0;
            }
            //返回值
            $("#purchaseAuditViewForm #hwmc").text(hwmc);
            $("#purchaseAuditViewForm #hwgg").text(hwgg);
            $("#purchaseAuditViewForm #hwjldw").text(hwjldw);
            $("#purchaseAuditViewForm #dj").text(jg);
            $("#purchaseAuditViewForm #sl").text(sl);
            $("#purchaseAuditViewForm #t_div").animate({left:'0px'},"slow");
            $("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
            $("#purchaseAuditViewForm #"+qrmxid).attr("style","background-color:#B3F7F7;");
        }
    });
}

function closeNav() {
    $("#purchaseAuditViewForm #t_div").animate({left:'-200px'},"slow");
    $("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
}