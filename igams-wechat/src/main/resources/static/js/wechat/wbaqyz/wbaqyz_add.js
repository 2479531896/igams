//判断代码code是否重复
$("#code").on('blur',function() {
        var code = $("#ajaxForm #code").val();
        $.ajax({
            type: "post",
            url: "/wbaqyz/code",
            data: {code: code, beforeCode: $("#ajaxForm #beforeCode").val(),"access_token": $("#ac_tk").val(),},
            dataType: "json",
            success: function (data) {
                if (data["status"]=="fail")
             {
                    $.error(data["message"])
                    $("#ajaxForm #code").val("")


                }

            }
        })
})
