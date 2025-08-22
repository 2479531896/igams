var index = 0;



isCardID=function(){
    var sId=document.getElementById("sfid").value;
    if (sId!=null&&sId!=''){
        return true;
    }else {
        layer.msg('请输入证件号码');
    }

}

phone=function() {
    var sjh = document.getElementById("sjh").value;
    var myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
    if (sjh && !myreg.test(sjh)) {
        layer.msg("请填写正确的手机号码");
    }
}


var sendMsgFlg = true
/**
    **
    * 发送验证码
    * @returns {Boolean}
*/
sendCode=function () {
    if (!sendMsgFlg) {
        return false;
    }
    sendMsgFlg = false
    if(!$("#ajaxForm #sjh").val()) {
        layer.msg("请填写正确的手机号码");
        sendMsgFlg = true
        return false
    }
    //发送短信验证码
    $.ajax({
        url: "/wechat/sendCode",
        type: "post",
        data: {wxid:$("#ajaxForm #wxid").val(),sj:$("#ajaxForm #sjh").val(),zjh:$("#ajaxForm #sfid").val()},
        dataTypep: "JSON",
        success:function(data){
            if(data.status == "success"){
                var sec = 60;
                $("#ajaxForm #sendMsm").attr("disabled",true); //设置成灰色不可点击
                $("#ajaxForm #sendMsm").css("pointer-events","none");
                $("#ajaxForm #sendMsm").css("background-color","#white");
                //获取验证码成功处理
                interval = setInterval(function countDown() {
                    if(sec > 0) {
                        $("#ajaxForm #sendMsm").text(sec+"秒后重新获取");
                        sec = sec-1;
                    } else {
                        clearInterval(interval);
                        sendMsgFlg = true
                        $("#ajaxForm #sendMsm").text("重新发送");
                        $("#ajaxForm #sendMsm").attr('disabled',false);
                        $("#ajaxForm #sendMsm").css("pointer-events","auto");
                        $("#ajaxForm #sendMsm").css("background-color","#white");
                    }
                },1000);
            }else{
                sendMsgFlg = true
                layer.msg(data.message);
            }
        }
    })
}
