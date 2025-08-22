$(function (){
    // 若有跳转地址，则跳转该地址
    if ($("#redirectPage #redirectUrl").val()){
        window.location.href = $("#redirectPage #redirectUrl").val();
    }
    // 若有提示消息
    if ($("#redirectPage #message").val()){
        var message = $("#redirectPage #message").val();
        var splits = message.split("\n");
        var html = "";
        for (var i = 0; i < splits.length; i++){
            html += '<p class="weui-msg__desc">' + splits[i] + '</p>';
        }
        $("#messageTitle").html(html)
    }
    if ($("#redirectPage #wechatMPCode").val()){
        $.ajax({
            url: '/wechat/getJsApiInfo',
            type: 'post',
            data: {
                "url":location.href.split('#')[0],
                "wbcxdm":$("#wechatMPCode").val()
            },
            dataType: 'json',
            success: function(result) {
                //注册信息
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: result.appid, // 必填，公众号的唯一标识
                    timestamp: result.timestamp, // 必填，生成签名的时间戳
                    nonceStr: result.noncestr, // 必填，生成签名的随机串
                    signature: result.sign, // 必填，签名，见附录1
                    jsApiList: ['scanQRCode']
                    // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
                wx.error(function(res) {
                    $("#messageTitle").html('<p class="weui-msg__desc">' + JSON.stringify(res) + '</p>')
                });
                //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
                wx.ready(function() {
                    // 9.1.2 扫描二维码并返回结果
                    document.querySelector('#scanQRCode').onclick = function () {
                        wx.scanQRCode({
                            needResult: 1,
                            desc: 'scanQRCode desc',
                            success: function (res) {
                                var url = res.resultStr + "&wechatid=" + $("#redirectPage #wechatid").val()
                                window.location.href = url;
                            }
                        });
                    };
                });
            }
        });
    }
})