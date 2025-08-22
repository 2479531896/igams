// 评价打分
$('.star span').click(function(){
    $(this).parent().find('span').removeClass('on')
    var index = $(this).index();
    $(this).parent().find('span').each(function(){
        if(index<0){
            return
        }
        $(this).addClass('on');
        index--
    })
})


function showloading(text){
    //loading显示
    $.showLoading(text);
}

function hideloading(){
    setTimeout(function() {
        //loading隐藏
        $.hideLoading();

    },200)
}
//提交
$('#saveEvaluation').click(function(){
    var stars = $(".star .on").length;
    var remark = $("#remark").val();
    var ywid = $("#ywid").val();
    var sfjj = $('input:radio[name="sfjj"]:checked').val();
    var wxid = $("#wxid").val();
    showloading("提交中...");
    $.ajax({
        url: '/wechat/evaluationSave',
        type: 'post',
        dataType: 'json',
        data : {
            stars: stars,
            remark: remark,
            ywid: ywid,
            sfjj: sfjj,
            wxid: wxid,
        },
        success: function(data) {
            if (data && data.status == 'success'){
                hideloading();
                $.toptip(data.message, 'success');
                window.history.back()
            }else {
                hideloading();
                $.toptip(JSON.stringify(data), 'error');
            }
        }
    });
})