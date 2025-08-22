
/*入库日期 插件*/
laydate.render({
    elem: ' #modAdministrationStockForm #rkrq'
    , theme: '#2381E9'
});

function getKwList(){
    var ckid = $("#ckid").val()
    $.ajax({
        url : $("#modAdministrationStockForm #urlPrefix").val()+"/storehouse/stock/pagedataCkxxList",
        type : "post",
        data : {fckid:ckid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        async:false,
        success:function(data){
            if(data != null && data.length != 0){
                var html = "";
                $.each(data,function(i){
                    for (var j = 0; j < data[i].length; j++) {
                        if (data[i][j].cklb == $("#kwlbid").val()){
                            var select = false;
                            if (data[i][j].ckid == $("#kw").val()){
                                html += "<option value='" + data[i][j].ckid + "' text='" + data[i][j].ckmc + "' selected>"+data[i][j].ckmc+"</option>";
                            }else {
                                html += "<option value='" + data[i][j].ckid + "' text='" + data[i][j].ckmc + "'>"+data[i][j].ckmc+"</option>";
                            }

                        }
                    }
                });
                $("#modAdministrationStockForm #kwid").empty();
                $("#modAdministrationStockForm #kwid").append(html);
                $("#modAdministrationStockForm #kwid").trigger("chosen:updated");
            }else{
                var html = "";
                $("#modAdministrationStockForm #kwid").empty();
                $("#modAdministrationStockForm #kwid").append(html);
                $("#modAdministrationStockForm #kwid").trigger("chosen:updated");
            }
        }
    });
}

$(function(){
    getKwList();
});