var pageSize = 5;
var pageEntireNumber = -5;
var pageUndetectNumber = -5;
var pageDetectedNumber = -5;
var pageOverdueNumber = -5;
var hzxxList = [];
var html = '';
var loadMoreEntireFlag = 0;
var loadMoreUndetectFlag = 0;
var loadMoreDetectedFlag = 0;
var loadMoreOverdueFlag = 0;
var loadEntireFlag = 0;
var loadUndetectFlag = 0;
var loadDetectedFlag = 0;
var loadOverdueFlag = 0;
var flag = 0;
var isInLoadMore = false;
var noMoreHtml ='<div class="weui-loadmore" id="noMore">' +
                '   <span class="weui-loadmore__tips">暂无更多信息</span>' +
                '</div>'
function refundInquiryQuery(hzid, fzjcid,orderno,sfje,fkbj,zffs,pt) {
    $('#cancleAppointment' + fzjcid).attr("disabled", true);
    $('#cancleAppointment' + fzjcid).addClass("weui-btn_disabled");
    if ("杰毅医检公众号" == pt){
        weui.dialog({
            title: '<span style="color: red">警告</span>',
            content: '您确定要退款吗？',
            className: 'custom-classname',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                    $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                    $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                    return;
                }
            }, {
                label: '确定',
                type: 'primary',
                onClick: function () {
                    if (zffs && zffs.includes("诺诺")){
                        $.ajax({
                            type: "post",
                            url: "/wechat/refundQuery",
                            data: {
                                "fzjcid": fzjcid,
                                "orderno": orderno,
                                "sfje": sfje,
                            },
                            success: function (result) {
                                if ("success" == result.status) {
                                    var resultInfo = JSON.parse(result.result);
                                    if ("JH200" == resultInfo.code) {
                                        $.ajax({
                                            type: "post",
                                            url: "/wechat/updateInvoiceInfo",
                                            data: {
                                                "refundno": resultInfo.result.refundNo,
                                                "fkbj": "3",
                                                "fzjcid": fzjcid,
                                                "czbs":"1",
                                                // "scbj":"2",
                                            },
                                            success: function (data) {
                                                if (data.status == "success"){setTimeout(function(){
                                                    weui.alert("正在退款中请稍后！", {
                                                        buttons: [{
                                                            label: '确定',
                                                            type: 'primary',
                                                            onClick: function () {
                                                                window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                            }
                                                        }]
                                                    });
                                                },500)
                                                }
                                            },
                                            fail: function (result) {
                                                weui.alert(result.message, {
                                                    buttons: [{
                                                        label: '确定',
                                                        type: 'primary',
                                                        onClick: function () {
                                                        }
                                                    }]
                                                });
                                            }
                                        })
                                    }else{setTimeout(function(){
                                        weui.alert("取消失败请稍后再试！", {
                                            buttons: [{
                                                label: '确定',
                                                type: 'primary',
                                                onClick: function () {
                                                    //请求超出时间，关闭当前页面跳转到列表页面
                                                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                                }
                                            }]
                                        });
                                        $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                                        $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                                    },500)

                                    }
                                }else{
                                    setTimeout(function(){
                                        weui.alert(result.message, {
                                            buttons: [{
                                                label: '确定',
                                                type: 'primary',
                                                onClick: function () {
                                                    window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                }
                                            }]
                                        });
                                    },500)
                                }
                            },
                            fail: function () {setTimeout(function(){
                                weui.alert("取消失败请稍后再试！", {
                                    buttons: [{
                                        label: '确定',
                                        type: 'primary',
                                        onClick: function () {
                                            //请求超出时间，关闭当前页面跳转到列表页面
                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                        }
                                    }]
                                });
                                $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                                $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                            },500)

                            }
                        })

                    }else{
                        let jclx="TYPE_COVID";
                        $.ajax({
                            type: "post",
                            url: "/wechat/confirmPay?jclx="+jclx,
                            data: {
                                "ywid": fzjcid,
                                "ywlx": "XG",
                            },
                            success: function (result) {
                                if ("success" == result.status) {
                                    $.ajax({
                                        url: '/wechat/payRefundApply',
                                        type: 'post',
                                        dataType: 'json',
                                        data : {"zfid":result.payinfoDto.zfid, "tkje":result.payinfoDto.fkje,"wxid": $("#wxid").val()},
                                        success: function(data) {
                                            if(data.status == 'success'){
                                                // 判断退款状态
                                                if(data.jg == "1"){// 退款成功
                                                    $.ajax({
                                                        type: "post",
                                                        url: "/wechat/updateInvoiceInfo",
                                                        data: {
                                                            "refundno": data.refundno,
                                                            "fkbj": "2",
                                                            "fzjcid": fzjcid,
                                                        },
                                                        success: function (data) {
                                                            if (data.status == "success"){setTimeout(function(){
                                                                weui.alert("退款成功！", {
                                                                    buttons: [{
                                                                        label: '确定',
                                                                        type: 'primary',
                                                                        onClick: function () {
                                                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                                                        }
                                                                    }]
                                                                });
                                                            },500)
                                                            }
                                                        },
                                                        fail: function (result) {
                                                            weui.alert(result.message, {
                                                                buttons: [{
                                                                    label: '确定',
                                                                    type: 'primary',
                                                                    onClick: function () {
                                                                        window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                                    }
                                                                }]
                                                            });
                                                        }
                                                    })
                                                }else{
                                                    setTimeout(function(){
                                                        weui.alert("退款中请稍后！", {
                                                            buttons: [{
                                                                label: '确定',
                                                                type: 'primary',
                                                                onClick: function () {
                                                                    window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                                }
                                                            }]
                                                        });
                                                    },500)
                                                }
                                            }else{setTimeout(function(){
                                                weui.alert("退款失败请稍后再试！", {
                                                    buttons: [{
                                                        label: '确定',
                                                        type: 'primary',
                                                        onClick: function () {
                                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                                        }
                                                    }]
                                                });
                                            },300)}
                                        }
                                    });
                                }else{
                                    setTimeout(function(){
                                        weui.alert(result.message, {
                                            buttons: [{
                                                label: '确定',
                                                type: 'primary',
                                                onClick: function () {
                                                    window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                }
                                            }]
                                        });
                                    },500)
                                }
                            },
                            fail: function () {setTimeout(function(){
                                weui.alert("取消失败请稍后再试！", {
                                    buttons: [{
                                        label: '确定',
                                        type: 'primary',
                                        onClick: function () {
                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                        }
                                    }]
                                });
                            },300)

                            $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                            $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                            }
                        })
                    }
                }
            }]
        });
    }else {
        weui.dialog({
            title: '<span style="color: red">警告</span>',
            content: '请到您预约的平台“'+pt+'”进行取消？',
            className: 'custom-classname',
            buttons: [{
                label: '确定',
                type: 'primary',
                onClick: function () {
                    $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                    $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                    return;
                }
            }]
        })
    }
}
function cancleAppointment(hzid, fzjcid,orderno,sfje,fkbj,zffs,pt) {
    $('#cancleAppointment' + fzjcid).attr("disabled", true);
    $('#cancleAppointment' + fzjcid).addClass("weui-btn_disabled");
    // document.getElementById('cancleAppointment'+hzid).classList.add("weui-btn_disabled");
    if ("杰毅医检公众号" == pt){
        weui.dialog({
            title: '<span style="color: red">警告</span>',
            content: '您确定要取消预约吗？',
            className: 'custom-classname',
            buttons: [{
                label: '取消',
                type: 'default',
                onClick: function () {
                    $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                    $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                    return;
                }
            }, {
                label: '确定',
                type: 'primary',
                onClick: function () {
                    if (zffs && zffs.includes("诺诺")){
                        if (fkbj == "1"){
                            $.ajax({
                                type: "post",
                                url: "/wechat/refundQuery",
                                data: {
                                    "fzjcid": fzjcid,
                                    "orderno": orderno,
                                    "sfje": sfje,
                                },
                                success: function (result) {
                                    if ("success" == result.status) {
                                        var resultInfo = JSON.parse(result.result);
                                        if ("JH200" == resultInfo.code) {
                                            $.ajax({
                                                type: "post",
                                                url: "/wechat/updateInvoiceInfo",
                                                data: {
                                                    "refundno": resultInfo.result.refundNo,
                                                    "fkbj": "3",
                                                    "fzjcid": fzjcid,
                                                    "scbj":"2",
                                                },
                                                success: function (data) {
                                                    if (data.status == "success"){setTimeout(function(){
                                                    weui.alert("正在退款中请稍后！", {
                                                            buttons: [{
                                                                label: '确定',
                                                                type: 'primary',
                                                                onClick: function () {
                                                                window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                                }
                                                            }]
                                                        });
                                                    },500)
                                                    }
                                                },
                                                fail: function (result) {
                                                    weui.alert(result.message, {
                                                        buttons: [{
                                                            label: '确定',
                                                            type: 'primary',
                                                            onClick: function () {
                                                            }
                                                        }]
                                                    });
                                                }
                                            })
                                        }else{setTimeout(function(){
                                            weui.alert("取消失败请稍后再试！", {
                                                buttons: [{
                                                    label: '确定',
                                                    type: 'primary',
                                                    onClick: function () {
                                                        //请求超出时间，关闭当前页面跳转到列表页面
                                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                                    }
                                                }]
                                            });
                                            $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                                            $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                                        },500)

                                        }
                                    }else{
                                        setTimeout(function(){
                                            weui.alert(result.message, {
                                                buttons: [{
                                                    label: '确定',
                                                    type: 'primary',
                                                    onClick: function () {
                                                        window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                    }
                                                }]
                                            });
                                        },500)
                                    }
                                },
                                fail: function () {setTimeout(function(){
                                    weui.alert("取消失败请稍后再试！", {
                                        buttons: [{
                                            label: '确定',
                                            type: 'primary',
                                            onClick: function () {
                                                //请求超出时间，关闭当前页面跳转到列表页面
                                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                            }
                                        }]
                                    });
                                    $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                                    $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                                },500)

                                }
                            })
                        }else{setTimeout(function(){
                            weui.dialog({
                                title: '',
                                content: '支付正在确认中',
                                className: 'custom-classname',
                                buttons: [{
                                    label: '直接取消',
                                    type: 'default',
                                    onClick: function () {
                                        cancelDirectly(hzid,fzjcid,"微信无需退款直接取消"+fzjcid)
                                    }
                                }, {
                                    label: '等待支付确认稍后再试',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                }]
                            });
                        },500)

                        }
                    }else{
                        let jclx="TYPE_COVID";
                        $.ajax({
                            type: "post",
                            url: "/wechat/confirmPay?jclx="+jclx,
                            data: {
                                "ywid": fzjcid,
                                "ywlx": "XG",
                            },
                            success: function (result) {
                                if ("success" == result.status) {
                                    $.ajax({
                                        url: '/wechat/payRefundApply',
                                        type: 'post',
                                        dataType: 'json',
                                        data : {"zfid":result.payinfoDto.zfid, "tkje":result.payinfoDto.fkje,"wxid": $("#wxid").val()},
                                        success: function(data) {
                                            if(data.status == 'success'){
                                                // 判断退款状态
                                                if(data.jg == "1"){// 退款成功
                                                    $.ajax({
                                                        type: "post",
                                                        url: "/wechat/updateInvoiceInfo",
                                                        data: {
                                                            "refundno": data.refundno,
                                                            "fkbj": "2",
                                                            "fzjcid": fzjcid,
                                                        },
                                                        success: function (data) {
                                                            if (data.status == "success"){setTimeout(function(){
                                                                weui.alert("退款成功！", {
                                                                    buttons: [{
                                                                        label: '确定',
                                                                        type: 'primary',
                                                                        onClick: function () {
                                                                            cancelDirectly(hzid,fzjcid,"支付宝退款成功"+fzjcid)
                                                                        }
                                                                    }]
                                                                });
                                                            },500)
                                                            }
                                                        },
                                                        fail: function (result) {
                                                            weui.alert(result.message, {
                                                                buttons: [{
                                                                    label: '确定',
                                                                    type: 'primary',
                                                                    onClick: function () {
                                                                    }
                                                                }]
                                                            });
                                                        }
                                                    })
                                                }else{
                                                    setTimeout(function(){
                                                        weui.alert("退款中请稍后！", {
                                                            buttons: [{
                                                                label: '确定',
                                                                type: 'primary',
                                                                onClick: function () {
                                                                window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                                }
                                                            }]
                                                        });
                                                },500)
                                                }
                                            }else{setTimeout(function(){
                                            weui.alert("退款失败请稍后再试！", {
                                                    buttons: [{
                                                        label: '确定',
                                                        type: 'primary',
                                                        onClick: function () {
                                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                                        }
                                                    }]
                                                });
                                            },300)

                                            }
                                        }
                                    });
                                }else if("cancel" == result.status){
                                    cancelDirectly(hzid,fzjcid,"无需确认直接取消"+fzjcid)
                                }else if("ycj" == result.status){
                                    setTimeout(function(){
                                        weui.alert(result.message, {
                                            buttons: [{
                                                label: '确定',
                                                type: 'primary',
                                                onClick: function () {
                                                    window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
                                                }
                                            }]
                                        });
                                    },500)
                                }else {setTimeout(function(){
                                    weui.dialog({
                                        title: '',
                                        content: '支付正在确认中',
                                        className: 'custom-classname',
                                        buttons: [{
                                            label: '无需退款直接取消',
                                            type: 'default',
                                            onClick: function () {
                                                cancelDirectly(hzid,fzjcid,"支付宝无需退款直接取消"+fzjcid)
                                            }
                                        }, {
                                            label: '等待支付确认稍后再试',
                                            type: 'primary',
                                            onClick: function () {
                                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                            }
                                        }]
                                    });
                                },300)

                                }

                            },
                            fail: function () {setTimeout(function(){
                                weui.alert("取消失败请稍后再试！", {
                                    buttons: [{
                                        label: '确定',
                                        type: 'primary',
                                        onClick: function () {
                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                        }
                                    }]
                                });
                            },300)

                                $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                                $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                            }
                        })
                    }
                }
            }]
        });
    }else {
    weui.dialog({
        title: '<span style="color: red">警告</span>',
        content: '请到您预约的平台“'+pt+'”进行取消？',
        className: 'custom-classname',
        buttons: [{
            label: '确定',
            type: 'primary',
            onClick: function () {
                $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
                return;
            }
        }]
    })
    }
}
//取消预约
function cancelDirectly(hzid, fzjcid,mes) {
    let jclx="TYPE_COVID"
    $.ajax({
        type: "post",
        url: "/wechat/cancleAppointmentEdit",
        data: {
            "hzid": hzid,
            "wxid": $("#wxid").val(),
            "fzjcid": fzjcid,
            "mes": mes,
            "jclx":jclx
        },
        success: function (result) {
            if ("success" == result.status) {
                weui.toast(result.message, 1000);
                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
            } else {
                weui.toast(result.message, 1000);
                $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
                $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
            }

        },
        fail: function (result) {
            weui.alert(result.message, {
                buttons: [{
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                    }
                }]
            });
            $('#cancleAppointment' + fzjcid).removeAttr("disabled", true);
            $('#cancleAppointment' + fzjcid).removeClass("weui-btn_disabled");
        }
    })
}
//修改预约
function updateAppointment(hzid, fzjcid,pt) {
    $('#updateAppointment' + fzjcid).attr("disabled", true);
    $('#updateAppointment' + fzjcid).addClass("weui-btn_disabled");
    if ("杰毅医检公众号" == pt) {
        /*weui.dialog({
        title: '<span style="color: red">警告</span>',
        content: '您确定要修改预约吗？',
        className: 'custom-classname',
        buttons: [{
            label: '取消',
            type: 'default',
            onClick: function () {
                $('#updateAppointment'+hzid).removeAttr("disabled",true);
                $('#updateAppointment'+hzid).removeClass("weui-btn_disabled");
                return;
            }
        }, {
            label: '确定',
            type: 'primary',
            onClick: function () {*/
        let jclx="TYPE_COVID";
            window.location.href = '/wechat/detectionMod?wxid=' + $("#wxid").val() + '&hzid=' + hzid + '&fzjcid=' + fzjcid+'&jclx='+jclx;
            $('#updateAppointment' + fzjcid).removeAttr("disabled", true);
            $('#updateAppointment' + fzjcid).removeClass("weui-btn_disabled");
            /*        }
                }]
            });*/
    }else {
        weui.dialog({
            title: '<span style="color: red">警告</span>',
            content: '请到您预约的平台“'+pt+'”进行修改？',
            className: 'custom-classname',
            buttons: [{
                label: '确定',
                type: 'primary',
                onClick: function () {
                    $('#updateAppointment' + fzjcid).removeAttr("disabled", true);
                    $('#updateAppointment' + fzjcid).removeClass("weui-btn_disabled");
                    return;
                }
            }]
        })
    }

}
//获取预约码
function appointmentQrCode(fzjcid,zjh,xm,yyjcrq,cyddz) {
    $('#appointmentQrCode' + fzjcid).attr("disabled", true);
    $('#appointmentQrCode' + fzjcid).addClass("weui-btn_disabled");
    if (zjh!=null && zjh!=""){
        $("#qrcode").qrcode({
            render: 'div',
            size: 200,
            text: fzjcid.toString()
        });
    }
    // 日期格式化为年月日
    var newDate = yyjcrq.slice(0,4) + "年"+ yyjcrq.slice(5,7) +"月"+ yyjcrq.slice(8,10) +"日"+yyjcrq.slice(10);

    $("#info").html('       <div class="weui-form-preview__item" style="height: auto;margin-bottom: 10px;">' +
        '           <label class="weui-form-preview__label">姓名</label>' +
        '           <span class="weui-form-preview__value" style="text-align: left;color: #666666;font-size: 16px;">'+xm+'</span>' +
        '       </div>'+
        '       <div class="weui-form-preview__item" style="height: auto;margin-bottom: 10px;">' +
        '           <label class="weui-form-preview__label">身份证号</label>' +
        '           <span class="weui-form-preview__value" style="text-align: left;color: #666666;font-size: 16px;">'+zjh.substring(0,6)+"********"+zjh.substring(14)+'</span>' +
        '       </div>'+
        '       <div class="weui-form-preview__item" style="height: auto;margin-bottom: 10px;">' +
        '           <label class="weui-form-preview__label">预约时间</label>' +
        '           <span class="weui-form-preview__value" style="text-align: left;color: #666666;font-size: 16px;">'+newDate+'</span>' +
        '       </div>');

    $("#dqcyddz").html('       <div class="weui-form-preview__item" style="height: auto;">' +
        '           <label class="weui-form-preview__label">采样地址</label>' +
        '           <span class="weui-form-preview__value" style="text-align: left;color: #666666;font-size: 16px;">'+cyddz+'</span>' +
        '       </div>')

    $('#qrCodePage').show();
    $("#container").hide();
    $('#appointmentQrCode' + fzjcid).removeAttr("disabled", true);
    $('#appointmentQrCode' + fzjcid).removeClass("weui-btn_disabled");
}
//关闭二维码
function closeQrCodePage(){
    $('#qrCodePage').hide();
    $("#container").show();
    $("#qrcode").html("");
}
/*银行支付提交*/
let fkfs = 0;
function editconfirm(fzjcid,orderno,fkje){
    if (orderno && orderno != "null"){
        $.ajax({
            type: "post",
            url: "/wechat/nuonuoOrderInfo",
            data: {
                "fzjcid": fzjcid,
                "wxid": $("#wxid").val(),
                "orderno": orderno,
            },
            success: function (result) {
                if ("success" == result.status) {
                    var orderInfo = JSON.parse(result.result);
                    if ("JH200" == orderInfo.code) {
                        if (orderInfo.result.payStatus == "1"){
                            weui.alert("该订单已经支付，请勿重新支付！", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        $.ajax({
                                            type: "post",
                                            url: "/wechat/updateInvoiceInfo",
                                            data: {
                                                "ptorderno": orderInfo.result.orderNo,
                                                "fkbj": "1",
                                                "fzjcid": fzjcid,
                                                "sfje": orderInfo.result.amount,
                                                "zffs": "诺诺"+orderInfo.result.payType,
                                            },
                                            success: function (data) {
                                                if (data.status == "success"){
                                                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                                }
                                            },
                                        })
                                    }
                                }]
                            });
                        }else{
                            goPay(fzjcid,orderno,fkje);
                        }
                    }else{
                        goPay(fzjcid,orderno,fkje);
                    }
                }
            }
        })
    }else{
        goPay(fzjcid,orderno,fkje);
    }

}

// 支付方法
function goPay(fzjcid,orderno,fkje){
    weui.alert("请选择支付方式！", {
        buttons: [{
            label: '微信',
            type: 'primary',
            onClick: function () {
                fkfs= "2";
                $(this).attr("style","pointer-events: none;");
                $("#goPay"+fzjcid).attr("style","margin-top: 5px;position: absolute;top: 50px;right: 10px;color:white;border-radius: 5px;padding: 4px 4px;background-color: #00AFEC;border: none;");;
                pay(fzjcid,orderno,fkje);
                // window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
            }
        },{
            label: '支付宝',
            type: 'primary',
            onClick: function () {
                fkfs= "1";
                $(this).attr("style","pointer-events: none;");
                $("#goPay"+fzjcid).attr("style","margin-top: 5px;position: absolute;top: 50px;right: 10px;color:white;border-radius: 5px;padding: 4px 4px;background-color: #00AFEC;border: none;");
                pay(fzjcid,orderno,fkje);
                // window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
            }
        }]
    });
}
// 支付方法
function pay(fzjcid,orderno,amount){
    // 判断支付方式
    if(fkfs == "1"){
        // 支付宝支付 判断是否为微信环境
        var ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf('micromessenger') != -1) {
            // 打开引导页
            window.location.href="/wechat/pay/alipayGuide?ywid="+  fzjcid +"&ybbh=XGBGJC&fkje="+amount+"&wxid="+  $("#wxid").val() +"&ywlx="+ $("#ywlx").val();
            $("#fastPay_ajaxForm .preBtn").attr("disabled", false);
        }else{
            // 创建支付宝native订单
            $.ajax({
                url: '/wechat/pay/alipayNative',
                type: 'post',
                dataType: 'json',
                data : {"ywid" : fzjcid, "ybbh" : "XGBGJC","fkje": amount,"wxid": $("#wxid").val(),"ywlx":  $("#ywlx").val()},
                success: function(data) {
                    if(data.status == 'success'){
                        // 唤起支付宝路径 data.qrCode
                        window.location.href = data.qrCode;
                    }else{
                        setTimeout(function(){
                            weui.alert(data.message, {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                }]
                            });
                        },500)
                    }
                }
            });
        }
    }
    else if(fkfs == "2"){
        $.ajax({
            type: "post",
            url: "/wechat/nuonuoWxPay",
            data: {
                "amount": amount,
                "subject": "检测服务费",
                "wxid": $("#wxid").val(),
                "fzjcid": fzjcid,
                "orderno": orderno,
            },
            success: function (result) {
                if ("success" == result.status) {
                    if($("#updateFlag").val() != 'updateFlag'){
                        var pay = JSON.parse(result.payUrl);
                        if ("JH200" == pay.code) {
                            window.location.href = pay.result.payUtl;
                        }else{
                            setTimeout(function(){
                                weui.alert("系统发生错误！", {
                                    buttons: [{
                                        label: '确定',
                                        type: 'primary',
                                        onClick: function () {
                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                        }
                                    }]
                                });
                            },500)
                        }
                    }else{
                        setTimeout(function(){
                            weui.alert("系统发生错误！", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                }]
                            });
                        },500)
                    }
                } else {
                    weui.alert(result.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                            }
                        }]
                    });
                    $('#submitAppointment').removeAttr("disabled", true);
                    $('#submitAppointment').removeClass("weui-btn_disabled");
                }

            },
            fail: function (result) {
                weui.alert(result.message, {
                    buttons: [{
                        label: '确定',
                        type: 'primary',
                        onClick: function () {
                        }
                    }]
                });
                $('#submitAppointment').removeAttr("disabled", true);
                $('#submitAppointment').removeClass("weui-btn_disabled");
            }
        })
    }else{
        weui.alert("未取得支付方式", {
            buttons: [{
                label: '确定',
                type: 'primary',
                onClick: function () {
                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                }
            }]
        });
    }

}
// //加载更多方法
// function pay(wxid,fzjcid) {
//     let amount = 0.01;
//     $.ajax({
//         type: "post",
//         url: "/wechat/detectionAppointmentSave",
//         data: {
//             "amount": amount,
//             "subject": "新冠病毒检测",
//             "wxid": wxid,
//             "fzjcid": fzjcid,
//             "czbj": 1,
//         },
//         success: function (result) {
//             if ("success" == result.status) {
//                 var pay = JSON.parse(result.payUrl);
//                 if ("JH200" == pay.code) {
//                     window.location.href = pay.result.payUtl;
//                 }else{
//                     window.location.href = '/wechat/detectionViewAndMod?wxid=' + wxid;
//                 }
//             } else {
//                 weui.alert(result.message, {
//                     buttons: [{
//                         label: '确定',
//                         type: 'primary',
//                         onClick: function () {
//                         }
//                     }]
//                 });
//             }
//
//         },
//         fail: function (result) {
//             weui.alert(result.message, {
//                 buttons: [{
//                     label: '确定',
//                     type: 'primary',
//                     onClick: function () {
//                     }
//                 }]
//             });
//         }
//     })
// }

//加载更多方法
function invoice(wxid,zffs,orderno,fzjcid,sfje,bgrq) {
    if (bgrq && bgrq != "null" && bgrq != "undefined"){
        if (zffs == "诺诺WECHAT"){
            $.ajax({
                type: "post",
                url: "/wechat/getInvoiceLinks",
                data: {
                    "fzjcid": fzjcid,
                    "wxid": $("#wxid").val(),
                    "orderno": orderno,
                },
                success: function (result) {
                    if ("success" == result.status) {
                        var invoice = JSON.parse(result.result);
                        if ("JH200" == invoice.code) {
                            window.location.href = invoice.result.invoiceUrl;
                            $.ajax({
                                type: "post",
                                url: "/wechat/updateInvoiceInfo",
                                data: {
                                    "kpbj": "1",
                                    "fzjcid": fzjcid,
                                },
                                success: function (data) {
                                },
                            })
                        }else{
                            weui.alert("开票失败请稍后再试", {
                            buttons: [{
                                label: '确定',
                                type: 'primary',
                                onClick: function () {
                                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                }
                            }]
                        });
                        }
                    } else {
                        weui.alert(result.message, {
                            buttons: [{
                                label: '确定',
                                type: 'primary',
                                onClick: function () {
                                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                }
                            }]
                        });
                        $('#submitAppointment').removeAttr("disabled", true);
                        $('#submitAppointment').removeClass("weui-btn_disabled");
                    }

                },
                fail: function (result) {
                    weui.alert(result.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                            }
                        }]
                    });
                    $('#submitAppointment').removeAttr("disabled", true);
                    $('#submitAppointment').removeClass("weui-btn_disabled");
                }
            })

        }else{
            window.location.href = '/wechat/invoiceInfo?wxid=' + $("#wxid").val() + '&fzjcid=' + fzjcid + '&sfje=' + sfje;
        }
    }else{
        weui.alert("检测结束才能申请开票！", {
            buttons: [{
                label: '确定',
                type: 'primary',
                onClick: function () {
                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                }
            }]
        });
    }

}

//发票详情
function invoiceInfo(fzjcid,zffs,orderno) {
    if (zffs == "诺诺WECHAT"){
        $.ajax({
            type: "post",
            url: "/wechat/nuonuoOrderInfo",
            data: {
                "fzjcid": fzjcid,
                "wxid": $("#wxid").val(),
                "orderno": orderno,
            },
            success: function (result) {
                if ("success" == result.status) {
                    var orderInfo = JSON.parse(result.result);
                    if ("JH200" == orderInfo.code) {
                        if (orderInfo.result.isInvoiced == "1"){
                            weui.alert("发票已开，请注意查收短信", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                }]
                            });
                        }else if (orderInfo.result.isInvoiced == "3"){
                            weui.alert("正在开票中请稍后！", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                }]
                            });
                        }else if (orderInfo.result.isInvoiced == "2"){
                            $.ajax({
                                type: "post",
                                url: "/wechat/invoiceFailNotice",
                                data: {
                                },
                                success: function () {
                                    weui.alert("开票失败！已通知管理员进行处理", {
                                        buttons: [{
                                            label: '确定',
                                            type: 'primary',
                                            onClick: function () {
                                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                            }
                                        }]
                                    });
                                },
                                fail: function (result) {
                                    weui.alert("发生异常请稍后再试！", {
                                        buttons: [{
                                            label: '确定',
                                            type: 'primary',
                                            onClick: function () {
                                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                            }
                                        }]
                                    });
                                }
                            })
                        }else{
                            $.ajax({
                                type: "post",
                                url: "/wechat/updateInvoiceInfo",
                                data: {
                                    "kpbj": "0",
                                    "fzjcid": fzjcid,
                                },
                                success: function (data) {
                                    weui.alert("请重新开票！", {
                                        buttons: [{
                                            label: '确定',
                                            type: 'primary',
                                            onClick: function () {
                                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                            }
                                        }]
                                    });
                                },
                                fail: function (result) {
                                    weui.alert("发生异常请稍后再试！", {
                                        buttons: [{
                                            label: '确定',
                                            type: 'primary',
                                            onClick: function () {
                                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                            }
                                        }]
                                    });
                                }
                            })
                        }

                    }else{
                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                    }
                } else {
                    weui.alert(result.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                            }
                        }]
                    });
                    $('#submitAppointment').removeAttr("disabled", true);
                    $('#submitAppointment').removeClass("weui-btn_disabled");
                }

            },
            fail: function (result) {
                weui.alert(result.message, {
                    buttons: [{
                        label: '确定',
                        type: 'primary',
                        onClick: function () {
                        }
                    }]
                });
                $('#submitAppointment').removeAttr("disabled", true);
                $('#submitAppointment').removeClass("weui-btn_disabled");
            }
        })

    }else{
        $.ajax({
            type: "post",
            url: "/wechat/selectInvoice",
            data: {
                "fzjcid": fzjcid,
            },
            success: function (data) {
                if ("success" == data.status) {
                    var invoiceInfo = JSON.parse(data.invoiceUrl);
                    if ("E0000" == invoiceInfo.code) {
                        if (invoiceInfo.result[0].pdfUrl && invoiceInfo.result[0].pictureUrl){
                            $.ajax({
                                type: "post",
                                url: "/wechat/updateInvoiceInfo",
                                data: {
                                    "imgurl": invoiceInfo.result[0].pictureUrl,
                                    "pdfurl": invoiceInfo.result[0].pdfUrl,
                                    "fzjcid": fzjcid,
                                },
                                success: function (data) {
                                    if (data.status == "success"){
                                        window.location.href = 'https://'+invoiceInfo.result[0].pictureUrl;
                                        // window.location.href = '/wechat/invoiceImgInfo?wxid=' + $("#wxid").val() + '&fzjcid=' + fzjcid;
                                    }
                                },
                                fail: function (result) {
                                    weui.alert(result.message, {
                                        buttons: [{
                                            label: '确定',
                                            type: 'primary',
                                            onClick: function () {
                                            }
                                        }]
                                    });
                                }
                            })
                        }else if (invoiceInfo.result[0].status == "22" || invoiceInfo.result[0].status=="24"){
                            updateInfo(fzjcid);
                        }else{
                            weui.alert("正在生成中请稍后！", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                    }
                                }]
                            });
                            // 打开正在生成的页面
                            // window.location.href = data.imgurl;
                        }
                    }else{
                        updateInfo(fzjcid);
                    }
                }else if("imgurl" == data.status){
                    window.location.href = 'https://'+data.imgurl;
                    // window.location.href = '/wechat/invoiceImgInfo?wxid=' + $("#wxid").val() + '&fzjcid=' + fzjcid;
                } else {
                    weui.alert(data.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                            }
                        }]
                    });
                }

            },
            fail: function (result) {
                weui.alert(result.message, {
                    buttons: [{
                        label: '确定',
                        type: 'primary',
                        onClick: function () {
                        }
                    }]
                });
            }
        })
    }

}

function updateInfo(fzjcid) {
    $.ajax({
        type: "post",
        url: "/wechat/updateInvoiceInfo",
        data: {
            "kpbj": "0",
            "fphm": "",
            "fzjcid": fzjcid,
        },
        success: function (data) {
            weui.alert("开票失败请重新开票！", {
                buttons: [{
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                    }
                }]
            });
        },
        fail: function (result) {
            weui.alert(result.message, {
                buttons: [{
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                    }
                }]
            });
        }
    })
}

//加载更多方法
function loadMore() {
    isInLoadMore = true;
    $("#loadMore").removeAttr("hidden", true);
    var pageNumber = 0;
    if (flag == 0) {
        pageEntireNumber = pageEntireNumber + 5
        pageNumber = pageEntireNumber
    } else if (flag == 1) {
        pageUndetectNumber = pageUndetectNumber + 5
        pageNumber = pageUndetectNumber
    } else if (flag == 2) {
        pageDetectedNumber = pageDetectedNumber + 5
        pageNumber = pageDetectedNumber
    } else if (flag == 3) {
        pageOverdueNumber = pageOverdueNumber + 5
        pageNumber = pageOverdueNumber
    }
    $.ajax({
        type: "post",
        url: "/wechat/getDetectionViewAndModList",
        data: {
            "wxid": $("#wxid").val(),
            "pageSize": pageSize,
            "pageNumber": pageNumber,
            "flag": flag,
            "jclx":"TYPE_COVID",
        },
        success: function (result) {
            var lshtml = '';
            var len = result.hzxxList.length;
            // if (len > 0) {
                //拼接页面
                for (var i = 0; i < len; i++) {
                    // 日期格式化为年月日
                    var newDate = result.hzxxList[i].yyjcrq;
                    var newDate2 = newDate.slice(0,4) + "年"+ newDate.slice(5,7) +"月"+ newDate.slice(8,10) +"日"+newDate.slice(10);
                    lshtml = lshtml + '<div class="weui-panel" style="border-radius: 10px;">' +
                        '<div class="weui-form-preview" style="height: 225px;">' +
                        '   <div class="weui-form-preview__hd weui-media-box_appmsg" style="padding: 5px;">' +
                        '   <div class="weui-media-box__bd" style="flex: 1;text-align:left">'
                    if (result.hzxxList[i].scbj == "2") {
                        lshtml = lshtml + '<button class="weui-btn weui-btn_mini weui-btn_disabled"style="background-color: white;margin-left: -5px;" disabled>已失效</button>'
                    } else if ((result.hzxxList[i].cjsj == null || result.hzxxList[i].cjsj == '') && result.hzxxList[i].yyjcrq < $("#nowDate").val() && result.hzxxList[i].scbj != "2") {
                        lshtml = lshtml + '<button class="weui-btn weui-btn_mini weui-btn_disabled" style="color:red; background-color: white;margin-left: -5px;" disabled>已过期</button>'
                    } else if ((result.hzxxList[i].cjsj == null || result.hzxxList[i].cjsj == '') && result.hzxxList[i].yyjcrq >= $("#nowDate").val() && result.hzxxList[i].scbj != "2") {
                        lshtml = lshtml + '<button class="weui-btn weui-btn_mini" style="color: #00AFEC;background-color: white;margin-left: -5px;" disabled>待接收</button>'
                    } else if (result.hzxxList[i].cjsj != null && result.hzxxList[i].sysj == null && result.hzxxList[i].scbj != "2") {
                        lshtml = lshtml + '<button style="background-color: white;color: #00AFEC;font-weight: 600;font-size: 16px;border: none;margin-left: 5px;" disabled>待检测</button>'
                    }else if (result.hzxxList[i].sysj != null && result.hzxxList[i].bgrq == null && result.hzxxList[i].scbj != "2") {
                        lshtml = lshtml + '<button class="weui-btn weui-btn_mini weui-btn_primary" style="color: #00AFEC;background-color: white;margin-left: -5px;" disabled>检测中</button>'
                    }else if (result.hzxxList[i].bgrq != null && result.hzxxList[i].scbj != "2") {
                        lshtml = lshtml + '<button class="weui-btn weui-btn_mini weui-btn_primary" style="color: #00AFEC;background-color: white;margin-left: -5px;" disabled>已检测</button>'
                    }
                    lshtml = lshtml + '</div>' +
                        '       <div class="weui-media-box__bd" style="flex: 1;text-align: right">' +
                        '           <span class="weui-form-preview__label" style="margin: 0;font-size: 14px;margin-left: -70px;letter-spacing: -1px;">检测时间：'+newDate2+'</span>' +
                        '       </div>'  +
                        '   </div>';
                    if(result.hzxxList[i].fkbj == "1" && result.hzxxList[i].scbj == "0"){
                        lshtml = lshtml +'<div class="weui-form-preview__bd" style="padding: 5px 10px 0px">' +
                            '                   <div class="weui-form-preview__item">' +
                            '                       <label class="weui-form-preview__label">电子发票</label>' ;
                        if(result.hzxxList[i].kpbj == "1" && result.hzxxList[i].bgrq != null){
                            lshtml = lshtml +'   <input type="button" class="weui-btn_primary" style="color:white;border-radius: 5px;padding: 4px 4px;" onclick="invoiceInfo(\'' + result.hzxxList[i].fzjcid + '\',\'' + result.hzxxList[i].zffs+ '\',\'' + result.hzxxList[i].orderno   + '\');" value="发票详情"> ' +
                                // '<button style="background: whitesmoke;border: 1px solid black;border-radius: 25%" type="button" class="btn-viewChooseNormale" onclick="invoiceInfo(\'' + result.hzxxList[i].fzjcid + '\');" >发票详情</button>' +
                                '                   </div></div>' ;
                        }else {
                            lshtml = lshtml +'<input type="button" class="weui-btn_primary" style="color:white;border-radius: 5px;padding: 4px 4px;" onclick="invoice(\'' + result.hzxxList[i].wxid + '\',\'' + result.hzxxList[i].zffs + '\',\'' + result.hzxxList[i].orderno   + '\',\'' + result.hzxxList[i].fzjcid + '\',\'' + result.hzxxList[i].sfje + '\',\'' + result.hzxxList[i].bgrq + '\');" value="申请开票"> ' +
                                // '  <button type="button" class="btn-viewChooseNormale" onclick="invoice(\'' + result.hzxxList[i].wxid + '\',\'' + result.hzxxList[i].fzjcid + '\');" >申请开票</button>' +
                                '                   </div></div>' ;
                        }
                    }

                    lshtml = lshtml +'   <div class="weui-form-preview__bd" style="padding: 0px 10px 5px">' +
                        '                   <div class="weui-form-preview__item" style="margin-top: 5px;">' +
                        '                       <label class="" style="float: left;margin-right: 2px;">姓名</label>' +
                        '                       <span class="weui-form-preview__value" style="word-break: keep-all;text-align: left;">' + result.hzxxList[i].xm + '</span>';
                    if (result.hzxxList[i].scbj == "0" && result.hzxxList[i].fkbj == "0" && (result.hzxxList[i].cjsj == null || result.hzxxList[i].cjsj == '') && result.hzxxList[i].yyjcrq >= $("#nowDate").val()){
                        lshtml =lshtml + '<div class="weui-form-preview__item">' +
                            '                   <input id="goPay'+result.hzxxList[i].fzjcid+'" type="button" class="weui-btn_primary" style="margin-top: 5px;position: absolute;top: 50px;right: 10px;color:white;border-radius: 5px;padding: 4px 4px;background-color: #00AFEC;border: none;" onclick="editconfirm(\'' + result.hzxxList[i].fzjcid+ '\',\'' + result.hzxxList[i].orderno  + '\',\'' + result.hzxxList[i].fkje + '\');" value="&nbsp;去支付&nbsp;"> '+
                            '             </div>' ;
                    }
                    if (result.hzxxList[i].scbj == "0" && result.hzxxList[i].fkbj == "1" && (result.hzxxList[i].cjsj == null || result.hzxxList[i].cjsj == '') && result.hzxxList[i].yyjcrq >= $("#nowDate").val()){
                        lshtml =lshtml + '<div class="weui-form-preview__item">' +
                            '                   <input id="goPay'+result.hzxxList[i].fzjcid+'" type="button" class="weui-btn_primary" style="margin-top: 5px;position: absolute;top: 50px;right: 10px;color:white;border-radius: 5px;padding: 4px 4px;background-color: #00AFEC;border: none;" onclick="refundInquiryQuery(\'' + result.hzxxList[i].hzid + '\',\'' + result.hzxxList[i].fzjcid + '\',\'' + result.hzxxList[i].orderno + '\',\'' + result.hzxxList[i].sfje + '\',\'' + result.hzxxList[i].fkbj + '\',\'' + result.hzxxList[i].zffs + '\',\'' + result.hzxxList[i].pt + '\');" value="&nbsp;退款&nbsp;"> '+
                            '             </div>' ;
                    }
                    if (result.hzxxList[i].fkbj == "3" && (result.hzxxList[i].cjsj == null || result.hzxxList[i].cjsj == '') && result.hzxxList[i].yyjcrq >= $("#nowDate").val()){
                        lshtml =lshtml + '<span class="glyphicon glyphicon-remove" style="margin-top: 5px;position: absolute;top: 50px;right: 10px;color: red" >退款中</span>'
                    }
                    lshtml = lshtml+   '</div>';

                    lshtml = lshtml+
                        '                   <div class="weui-form-preview__item">' +
                        '                       <label class="" style="float: left;margin-right: 2px;">检测项目</label>' +
                        '                       <span class="weui-form-preview__value" style="word-break: keep-all; text-align: left">' + (result.hzxxList[i].jcxmmc!=null?result.hzxxList[i].jcxmmc:"") + '</span>' +
                        '                   </div>' +
                        '       <div class="weui-form-preview__item" style="height: auto;">' +
                        '           <label class="" style="float: left;margin-right: 2px;">采样地址</label>' +
                        '           <span class="weui-form-preview__value" style="text-align: left">'+(result.hzxxList[i].cyd!=null?result.hzxxList[i].cyd:"")+'</span>' +
                        '       </div>';
                    lshtml =lshtml + '   </div>';
                    if ((result.hzxxList[i].cjsj == null || result.hzxxList[i].cjsj == '') && result.hzxxList[i].scbj != "2") {
                        lshtml = lshtml + '<div class="weui-form-preview__ft" style="height: 50px;">' +
                            '       <button  style=" font-size: 13px; color:#00AFEC;background-color: #FFFFFF;height: 28px; border: 1px solid;border-radius: 6px;margin-right: 32px;margin-top: 11px; line-height: 28px;width: 70px;margin-left: 75px;" id="cancleAppointment' + result.hzxxList[i].fzjcid + '" onclick="cancleAppointment(\'' + result.hzxxList[i].hzid + '\',\'' + result.hzxxList[i].fzjcid + '\',\'' + result.hzxxList[i].orderno + '\',\'' + result.hzxxList[i].sfje + '\',\'' + result.hzxxList[i].fkbj + '\',\'' + result.hzxxList[i].zffs + '\',\'' + result.hzxxList[i].pt + '\')">取消预约</button>' +
                            '       <button  style=" font-size: 13px;background-color: #00AFEC;color: white;line-height: 28px;height: 28px;width: 50px;margin-right: 32px;margin-top: 11px;border-radius: 6px;border: none;width: 70px;" id="updateAppointment' + result.hzxxList[i].fzjcid + '" onclick="updateAppointment(\'' + result.hzxxList[i].hzid + '\',\'' + result.hzxxList[i].fzjcid + '\',\'' + result.hzxxList[i].pt + '\')">修改</button>' +
                            '       <button  style=" font-size: 13px;background-color: #00AFEC;color: white;line-height: 28px;height: 28px;width: 50px;margin-top: 11px;border-radius: 6px;border: none;width: 70px;" id="appointmentQrCode' + result.hzxxList[i].fzjcid + '" onclick="appointmentQrCode(\'' + result.hzxxList[i].fzjcid + '\',\'' + result.hzxxList[i].zjh + '\',\'' + result.hzxxList[i].xm + '\',\'' + result.hzxxList[i].yyjcrq + '\',\'' + (result.hzxxList[i].cyd!=null?result.hzxxList[i].cyd:"") + '\')">预约码</button>' +
                            '   </div>'
                    }else if (result.hzxxList[i].bgrq != null) {
                        lshtml = lshtml + '<div class="weui-form-preview__ft" style="height: 50px;">' +
                            '       <button  style="background-color: #00AFEC;color: white;line-height: 30px;height: 30px;width: 50px;margin: auto;border-radius: 6px;border: none;width: 80px;margin-right: 20px;"  id="detectionReport' + result.hzxxList[i].fzjcid + '" onclick="detectionReport(\'' + result.hzxxList[i].fzjcid + '\')">查看报告</button>' +
                            '   </div>'
                    }
                    lshtml = lshtml + '</div>' +
                        '</div>'
                }
            /*} else {*/

            if (flag == 0) {
                $("#hzxxEntireList").append(lshtml)
            } else if (flag == 1) {
                $("#hzxxUndetectList").append(lshtml)
            } else if (flag == 2) {
                $("#hzxxDetectedList").append(lshtml)
            } else if (flag == 3) {
                $("#hzxxOverdueList").append(lshtml)
            }
            if (len<5){
                //拼接“没有更多数据”
                if (flag == 0) {
                    loadMoreEntireFlag = 1
                    $("#hzxxEntireList").append(noMoreHtml)
                    console.log(flag+"---"+len)
                } else if (flag == 1) {
                    loadMoreUndetectFlag = 1
                    $("#hzxxUndetectList").append(noMoreHtml)
                    console.log(flag+"---"+len)
                } else if (flag == 2) {
                    loadMoreDetectedFlag = 1
                    $("#hzxxDetectedList").append(noMoreHtml)
                    console.log(flag+"---"+len)
                } else if (flag == 3) {
                    loadMoreOverdueFlag = 1
                    $("#hzxxOverdueList").append(noMoreHtml)
                    console.log(flag+"---"+len)
                }
            }
            $("#loadMore").attr("hidden", true);
            isInLoadMore = false;
        },
        fail: function (result) {
            layer.msg('加载失败！');
            isInLoadMore = false;
        }
    })
}

function detectionReport(fzjcid){
    $('#detectionReport' + fzjcid).attr("disabled", true);
    $('#detectionReport' + fzjcid).addClass("weui-btn_disabled");
    var url="/wechat/viewReport?fzjcid="+fzjcid;
    window.location.href=url;
    $('#detectionReport' + fzjcid).removeAttr("disabled", true);
    $('#detectionReport' + fzjcid).removeClass("weui-btn_disabled");
}

$(window).scroll(function () {

    // scrollTop是滚动条滚动时，距离顶部的距离
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    // windowHeight是可视区的高度
    var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
    // scrollHeight是滚动条的总高度
    var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
    // 滚动条到底部
    if (scrollTop + windowHeight > scrollHeight - 20) {
        // 到了底部之后想做的操作,如到了底部之后加载
        if (!isInLoadMore){
            if (flag == 0) {
                if (loadMoreEntireFlag == 0) {
                    loadMore()
                }
            } else if (flag == 1) {
                if (loadMoreUndetectFlag == 0) {
                    loadMore()
                }
            } else if (flag == 2) {
                if (loadMoreDetectedFlag == 0) {
                    loadMore()
                }
            } else if (flag == 3) {
                if (loadMoreOverdueFlag == 0) {
                    loadMore()
                }
            }
        }
    }

});

/*筛选*/

/*全部*/
function getEntire() {
    //设置标记为0
    if (flag != 0) {
        flag = 0;
        $('#entire').removeClass("unChooseBtn");
        $('#undetect').removeClass("chooseBtn");
        $('#detected').removeClass("chooseBtn");
        $('#overdue').removeClass("chooseBtn");
        $('#entire').addClass("chooseBtn");
        $('#undetect').addClass("unChooseBtn");
        $('#detected').addClass("unChooseBtn");
        $('#overdue').addClass("unChooseBtn");
        $("#hzxxEntireList").show()
        $("#hzxxUndetectList").hide()
        $("#hzxxDetectedList").hide()
        $("#hzxxOverdueList").hide()

        /*按钮下的横杠*/
        $('#entire').addClass("anhg");
        $('#undetect').removeClass("anhg");
        $('#detected').removeClass("anhg");
        $('#overdue').removeClass("anhg");

        if (loadMoreEntireFlag == 0) {
            loadMore()
        }
    }
}

/*待检测*/
function getUndetect() {
    //设置标记为1
    if (flag != 1) {
        flag = 1;
        $('#entire').removeClass("chooseBtn");
        $('#undetect').removeClass("unChooseBtn");
        $('#detected').removeClass("chooseBtn");
        $('#overdue').removeClass("chooseBtn");
        $('#entire').addClass("unChooseBtn");
        $('#undetect').addClass("chooseBtn");
        $('#detected').addClass("unChooseBtn");
        $('#overdue').addClass("unChooseBtn");

        /*按钮下的横杠*/
        $('#entire').removeClass("anhg");
        $('#undetect').addClass("anhg");
        $('#detected').removeClass("anhg");
        $('#overdue').removeClass("anhg");

        $("#hzxxEntireList").hide()
        $("#hzxxUndetectList").show()
        $("#hzxxDetectedList").hide()
        $("#hzxxOverdueList").hide()
        if (loadMoreUndetectFlag == 0) {
            loadMore()
        }
    }
}

/*已检测*/
function getDetected() {
    //设置标记为2
    if (flag != 2) {
        flag = 2;
        $('#entire').removeClass("chooseBtn");
        $('#undetect').removeClass("chooseBtn");
        $('#detected').removeClass("unChooseBtn");
        $('#overdue').removeClass("chooseBtn");
        $('#entire').addClass("unChooseBtn");
        $('#undetect').addClass("unChooseBtn");
        $('#detected').addClass("chooseBtn");
        $('#overdue').addClass("unChooseBtn");

        /*按钮下的横杠*/
        $('#entire').removeClass("anhg");
        $('#undetect').removeClass("anhg");
        $('#detected').addClass("anhg");
        $('#overdue').removeClass("anhg");

        $("#hzxxEntireList").hide()
        $("#hzxxUndetectList").hide()
        $("#hzxxDetectedList").show()
        $("#hzxxOverdueList").hide()
        if (loadMoreDetectedFlag == 0) {
            loadMore()
        }
    }
}

/*已过期*/
function getOverdue() {
    //设置标记为3
    if (flag != 3) {
        flag = 3;
        $('#entire').removeClass("chooseBtn");
        $('#undetect').removeClass("chooseBtn");
        $('#detected').removeClass("chooseBtn");
        $('#overdue').removeClass("unChooseBtn");
        $('#entire').addClass("unChooseBtn");
        $('#undetect').addClass("unChooseBtn");
        $('#detected').addClass("unChooseBtn");
        $('#overdue').addClass("chooseBtn");

        /*按钮下的横杠*/
        $('#entire').removeClass("anhg");
        $('#undetect').removeClass("anhg");
        $('#detected').removeClass("anhg");
        $('#overdue').addClass("anhg");

        $("#hzxxEntireList").hide()
        $("#hzxxUndetectList").hide()
        $("#hzxxDetectedList").hide()
        $("#hzxxOverdueList").show()
        if (loadMoreOverdueFlag == 0) {
            loadMore()
        }
    }
}


$(document).ready(function () {
    // 在这里写你的代码...
    loadMore();
    // layer.msg('该检测正在进行或已完成，不可修改！');
    // $("#loadMore").attr("hidden",true);
});
$(function(){
    if ($("#container #fzjcid").val()&&$("#container #fzjcid").val()!="null"){
        $.ajax({
            type: "post",
            url: "/wechat/nuonuoOrderInfo",
            data: {
                "fzjcid": $("#container #fzjcid").val(),
                "wxid": $("#wxid").val(),
                "orderno": $("#container #orderno").val(),
            },
            success: function (result) {
                if ("success" == result.status) {
                    var orderInfo = JSON.parse(result.result);
                    if ("JH200" == orderInfo.code) {
                        if (orderInfo.result.payStatus == "1"){
                            $.ajax({
                                type: "post",
                                url: "/wechat/updateInvoiceInfo",
                                data: {
                                    "ptorderno": orderInfo.result.orderNo,
                                    "fkbj": "1",
                                    "fzjcid": $("#container #fzjcid").val(),
                                    "sfje": orderInfo.result.amount,
                                    "zffs": "诺诺"+orderInfo.result.payType,
                                },
                                success: function (data) {
                                    if (data.status == "success"){
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                },
                            })
                        }
                    }
                }
            }
        })
    }
    if ($("#container #wxid").val()!=null && $("#container #wxid").val() !=""){
        $("#container").show();
        $("#erroPage").hide();
        $("#qrCodePage").hide();
    }else {
        $("#container").hide();
        $("#qrCodePage").hide();
        $("#erroPage").show();
    }
})