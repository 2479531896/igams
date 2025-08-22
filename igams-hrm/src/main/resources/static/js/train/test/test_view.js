function updateDf(grksmxid){
    var df=$('#testViewForm #input_'+grksmxid).val();
    if(df==null||df==''){
        $.error("请打分！");
        return;
    }
    var fs=$('#testViewForm #fs_'+grksmxid).text();
    var df_t=parseFloat(df);
    var fs_t=parseFloat(fs);
    if(df_t>fs_t){
        $.error("所打分数大于本题总分！");
        return;
    }
    $.ajax({
        async:false,
        type:'post',
        url:$('#testViewForm #urlPrefix').val()+"/train/test/pagedataUpdateDf",
        cache: false,
        data: {"grksmxid":grksmxid,"df":$('#testViewForm #input_'+grksmxid).val(),"grksid":$('#testViewForm #grksid').val(),"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            $('#testViewForm #zf').text(data.zf);
            $('#testViewForm #df_'+grksmxid).text($('#testViewForm #input_'+grksmxid).val());
        }
    });
}
