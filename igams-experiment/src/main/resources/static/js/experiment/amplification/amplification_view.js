



$(function(){
    var kzid=$("#viewAmplificationForm #kzid").val();
    $.ajax({
        type:'post',
        url:"/amplification/amplification/pagedataGetInfo",
        cache: false,
        data: {"kzid":kzid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            var list=data;
            for(var i=0;i<list.length;i++){
                $("#nbbh-"+list[i].xh).val(list[i].nbbh);
            }
        }
    });
});