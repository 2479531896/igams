
    //返回主页
    $('#backHome').on('click', function () {
        $('#backHome').attr("disabled", true);
        $('#backHome').addClass("weui-btn_disabled");
        window.setTimeout(function(){
            $('#backHome').removeAttr("disabled", true);
            $('#backHome').removeClass("weui-btn_disabled");
        }, 1000);
        //关闭当前页面、返回主页
        window.location.replace('/wechat/detectionChoose?wxid=' + $("#wxid").val());
        return true;
    });
    //跳转绑定页面
    $('#changeBinding').on('click', function () {
        $('#changeBinding').attr("disabled", true);
        $('#changeBinding').addClass("weui-btn_disabled");
        window.setTimeout(function(){
            $('#changeBinding').removeAttr("disabled", true);
            $('#changeBinding').removeClass("weui-btn_disabled");
        }, 1000);
        window.location.href = '/wechat/phoneBinding?wxid=' + $("#wxid").val();
        return true;
    });

    $(function(){
        if ($("#wxid").val()!=null && $("#wxid").val() !=""){
            $("#changePhoneBinding").show();
            $("#erroPage").hide();
        }else {
            $("#changePhoneBinding").hide();
            $("#erroPage").show();
        }
    })
