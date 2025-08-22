/*验证手机号码*/
function PhoneVerify() {
    var phoneNum = $("#sjh").val();
    if (phoneNum != null && phoneNum != '') {
        var reg = /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8}|1\d{10})$/;
        if (!reg.test(phoneNum)) {
            $.toptip('请输入正确的手机号码！', 'error');
            return false;
        }
        return true
    }
    $.toptip('请输入正确的手机号码！', 'error');
    return false
}

/**
 * 发送验证码
 * @returns {Boolean}
 */
function sendCode() {
    if (!PhoneVerify()) {
        return;
    }
    var sec = 60;
    $("#phoneBinding_form #sendMsm").attr("disabled",true); //设置成灰色不可点击
    $("#phoneBinding_form #sendMsm").css("pointer-events","none");

    if ($("#phoneBinding_form #wxid").val()==null || $("#phoneBinding_form #wxid").val() ==""){
        $.toptip('未获取到当前微信信息，请重新至公众号打开!', 'error');
        return;
    }

    interval = setInterval(function countDown() {
        if(sec > 0) {
            $("#phoneBinding_form #sendMsm").text("重新发送("+sec+")");
            sec = sec-1;
        } else {
            clearInterval(interval);
            $("#phoneBinding_form #sendMsm").text("　重新发送　");
            $("#phoneBinding_form #sendMsm").attr('disabled',false);
            $("#phoneBinding_form #sendMsm").css("pointer-events","auto");
        }
    },1000);
    //发送短信验证码
    $.ajax({
        url: "/wechat/sendMessageCode",
        type: "post",
        data: {wxid:$("#phoneBinding_form #wxid").val(),sjh:$("#phoneBinding_form #sjh").val()},
        dataTypep: "JSON",
        success:function(data){
            if(data.status == "success"){
                //获取验证码成功处理
                $.toptip(data.message, 'success');
            }else{
                $.toptip(data.message, 'error');
            }
        }
    })
}

/**
 * 确认验证码并绑定
 * @returns {Boolean}
 */
function Phonebinding(){
    if (!PhoneVerify()) {
        $.toptip('请输入正确的手机号码！', 'error');
        return;
    }
    var yzm = $("#phoneBinding_form #yzm").val();
    if (yzm != null && yzm != ''){
        $("#phoneBinding_form #binding").attr("disabled",true); //设置成灰色不可点击
        $("#phoneBinding_form #binding").css("pointer-events","none");
        $("#phoneBinding_form #binding").css("background-color","#C9C9C9");
        if ($("#phoneBinding_form #wxid").val()==null || $("#phoneBinding_form #wxid").val() ==""){
            $.toptip('未获取到当前微信信息，请重新至公众号打开!', 'error');
            return;
        }
        $.ajax({
            url: "/wechat/checkMessageCode",
            type: "post",
            data: {wxid:$("#phoneBinding_form #wxid").val(),sjh:$("#phoneBinding_form #sjh").val(),yzm:yzm},
            dataTypep: "JSON",
            success:function(data){
                if(data.status == "success"){
                    //获取验证码成功处理
                    $.toptip(data.message, 'success');
                    setTimeout(function () {
                        window.location.replace('/wechat/detectionChoose?wxid=' + $("#wxid").val());
                    }, 1000);
                }else{
                    $.toptip(data.message, 'error');
                    $("#phoneBinding_form #binding").attr('disabled',false);
                    $("#phoneBinding_form #binding").css("pointer-events","auto");
                    $("#phoneBinding_form #binding").css("background-color","#07c160");
                }
            }
        })
    }else {
        $.toptip('请输入验证码!', 'error');
    }
}

$(function(){
    if ($("#phoneBinding_form #wxid").val()!=null && $("#phoneBinding_form #wxid").val() !=""){
        $("#phoneBinding").show();
        $("#erroPage").hide();
    }else {
        $("#phoneBinding").hide();
        $("#erroPage").show();
    }
})