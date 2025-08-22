function convertSql(){
    var before = $("#sqLConvertForm #before").val();
    if(before){
        $.ajax({
            url: $("#sqLConvertForm #urlPrefix").val()+'/systemmain/query/pagedataConvertSql',
            type: 'post',
            data: {
                "before": $("#sqLConvertForm #before").val(),
                "access_token": $("#ac_tk").val(),
            },
            dataType: 'json',
            async:false,
            success: function(result) {
                if("success"==result.status){
                    $("#sqLConvertForm #after").val(result.sql);
                }else{
                    $.error(result.message);
                    return;
                }
            }
        });
    }else{
        $.error("请输入转换前的数据！");
        return;
    }

}