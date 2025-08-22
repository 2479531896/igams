var pageSize = 5;
var pageEntireNumber = -5;
var html = '';
var loadMoreEntireFlag = 0;
var loadMoreBgFlag = 0;
var flag = 0;
//查询报告页面为0查询后报告为1
var flag1 = 0;
var noMoreHtml ='<div class="weui-loadmore" id="noMore">' +
    '   <span class="weui-loadmore__tips">暂无更多信息</span>' +
    '</div>'


//加载更多方法
function loadMore() {
    $("#loadMore").removeAttr("hidden", true);
    var pageNumber = 0;
    pageEntireNumber = pageEntireNumber + 5
    pageNumber = pageEntireNumber
    $.ajax({
        type: "post",
        url: "/wechat/getDetectionReport",
        data: {
            "wxid": $("#wxid").val(),
            "pageSize": pageSize,
            "pageNumber": pageNumber,
        },
        success: function (result) {
            var lshtml = '';
            var len = result.fzjcxxDtos.length;
            // if (len > 0) {
            //拼接页面
            for (var i = 0; i < len; i++) {
                //年月日处理
                var newBgrq = result.fzjcxxDtos[i].bgrq;
                var newBgrq1 = newBgrq.slice(0,4) + "年"+ newBgrq.slice(5,7) +"月"+ newBgrq.slice(8,10) +"日";
                lshtml = lshtml + '<div class="weui-panel" style="border-radius: 10px;background-color: #F5F5F5;">' +
                    '<div class="weui-form-preview"style="    border-radius: 16px;">' +
                    '   <div class="weui-form-preview__bd" style="padding: 5px 10px;    ">' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">患者姓名</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + result.fzjcxxDtos[i].xm  + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测日期</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + newBgrq1  + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测项目</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + result.fzjcxxDtos[i].str_jcxmmcs + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测结果</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + result.fzjcxxDtos[i].jcjgmc + '</span>' +
                    '                   </div>' +
                    '   </div>'
                    lshtml = lshtml + '<div class="weui-form-preview__ft">' ;
                if(result.fzjcxxDtos[i].fkbj == "1" && result.fzjcxxDtos[i].scbj == "0" && result.fzjcxxDtos[i].bgrq != null){
                    if (result.fzjcxxDtos[i].kpbj == "0"){
                        lshtml = lshtml + '       <button class="weui-form-preview__btn weui-form-preview__btn_primary" id="invoice" onclick="invoice(\'' + result.fzjcxxDtos[i].wxid + '\',\'' + result.fzjcxxDtos[i].zffs + '\',\'' + result.fzjcxxDtos[i].orderno   + '\',\'' + result.fzjcxxDtos[i].fzjcid + '\',\'' + result.fzjcxxDtos[i].sfje + '\',\'' + result.fzjcxxDtos[i].bgrq + '\');">申请开票</button>';
                    }else{
                        lshtml = lshtml + '       <button class="weui-form-preview__btn weui-form-preview__btn_primary" id="invoiceInfo" onclick="invoiceInfo(\''+ result.fzjcxxDtos[i].fzjcid + '\',\'' + result.fzjcxxDtos[i].zffs+ '\',\'' + result.fzjcxxDtos[i].orderno   + '\');">发票详情</button>';

                    }
                }
                lshtml = lshtml + '<div class="weui-form-preview__ft" style="    height: 50px;">' +
                    '       <button  id="viewReport" style=" margin-left: 170px;margin-right: 32px;background-color: #00AFEC;border-radius: 6px;border: none;color: white;width: 70px;height: 28px;margin-top: 11px;font-size: 13px;" onclick="viewPDF(\'' + result.fzjcxxDtos[i].fjid + '\')">预览</button>' +
                    '       <button  id="viewReport" style="background-color: #00AFEC;border-radius: 6px;border: none;color: white;width: 70px;height: 28px;margin-top: 11px;font-size: 13px;" onclick="xz(\'' + result.fzjcxxDtos[i].fjid + '\',\'' + result.fzjcxxDtos[i].sign + '\')">下载</button>' +
                    '   </div>'
                lshtml = lshtml + '</div>' +
                    '</div>'
                    // lshtml=lshtml+'<div style="height: 5px;background-color:#3c7bfb;">'+'</div>'
            }
            /*} else {*/

            $("#fzjcxxDtos").append(lshtml)

            if (len<5){
                //拼接“没有更多数据”
                    loadMoreEntireFlag = 1
                    $("#fzjcxxDtos").append(noMoreHtml)
            }
            $("#loadMore").attr("hidden", true);
        },
        fail: function (result) {
            layer.msg('加载失败！');
        }
    })
}

function viewReport(){
    $("#ajaxForm #submitAppointment").attr("disabled", "disabled");
    flag1 = 1;
    if(!$("#ajaxForm #sjh").val()) {
        layer.msg("请填写正确的手机号码");
        $("#ajaxForm #submitAppointment").removeAttr("disabled");
        return false
    }else if(!$("#ajaxForm #sfid").val()) {
        layer.msg("请填写正确的身份证号");
        $("#ajaxForm #submitAppointment").removeAttr("disabled");
        return false
    }else if(!$("#ajaxForm #yzm").val()) {
        layer.msg("请填写验证码");
        $("#ajaxForm #submitAppointment").removeAttr("disabled");
        return false
    }
    $.ajax({
        url: '/wechat/checkCode',
        type: 'post',
        data: {yzm: $("#ajaxForm #yzm").val(), sj: $("#ajaxForm #sjh").val(), zjh: $("#ajaxForm #sfid").val()},
        dataType: 'json',
        success: function (data) {
            if (data.status == "success") {
                loadMoreBg()
                $("#ajaxForm #submitAppointment").removeAttr("disabled");
                // var url="/wechat/detectionReport?wxid="+$("#ajaxForm #wxid").val()+"&zjh="+$("#ajaxForm #sfid").val()+"&sj="+$("#ajaxForm #sjh").val();
                // window.location.href=url;
            } else {
                layer.msg(data.message);
                $("#ajaxForm #submitAppointment").removeAttr("disabled");
            }
        }
    });

}



var bgPageNumber=-5;
//加载更多方法
function loadMoreBg() {
    $("#loadMore").removeAttr("hidden", true);
    bgPageNumber += 5;
    $.ajax({
        type: "post",
        url: "/wechat/getDetectionReport",
        data: {
            "wxid": $("#wxid").val(),
            "pageSize": pageSize,
            "pageNumber": bgPageNumber,
            "zjh": $("#ajaxForm #sfid").val(),
            "sj": $("#ajaxForm #sjh").val()
        },
        success: function (result) {
            var lshtml = '';
            var len = result.fzjcxxDtos.length;
            // if (len > 0) {
            //拼接页面
            for (var i = 0; i < len; i++) {
                //年月日处理
                var newBgrq = result.fzjcxxDtos[i].bgrq;
                var newBgrq1 = newBgrq.slice(0,4) + "年"+ newBgrq.slice(5,7) +"月"+ newBgrq.slice(8,10) +"日";
                lshtml = lshtml + '<div class="weui-panel" style="border-radius: 10px;background-color: #F5F5F5;">' +
                    '<div class="weui-form-preview"style="    border-radius: 16px;">' +
                    '   <div class="weui-form-preview__bd" style="padding: 5px 10px;    ">' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">患者姓名</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + result.fzjcxxDtos[i].xm  + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测日期</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + newBgrq1  + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测项目</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + result.fzjcxxDtos[i].str_jcxmmcs + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测结果</label>' +
                    '                       <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">' + result.fzjcxxDtos[i].jcjgmc + '</span>' +
                    '                   </div>' +
                    '   </div>'
                    lshtml = lshtml + '<div class="weui-form-preview__ft">' ;
                if(result.fzjcxxDtos[i].fkbj == "1" && result.fzjcxxDtos[i].scbj == "0" && result.fzjcxxDtos[i].bgrq != null){
                    if (result.fzjcxxDtos[i].kpbj == "0"){
                        lshtml = lshtml + '       <button class="weui-form-preview__btn weui-form-preview__btn_primary" id="invoice" onclick="invoice(\'' + result.fzjcxxDtos[i].wxid + '\',\'' + result.fzjcxxDtos[i].zffs + '\',\'' + result.fzjcxxDtos[i].orderno   + '\',\'' + result.fzjcxxDtos[i].fzjcid + '\',\'' + result.fzjcxxDtos[i].sfje + '\',\'' + result.fzjcxxDtos[i].bgrq + '\');">申请开票</button>';
                    }else{
                        lshtml = lshtml + '       <button class="weui-form-preview__btn weui-form-preview__btn_primary" id="invoiceInfo" onclick="invoiceInfo(\''+ result.fzjcxxDtos[i].fzjcid + '\',\'' + result.fzjcxxDtos[i].zffs+ '\',\'' + result.fzjcxxDtos[i].orderno   + '\');">发票详情</button>';

                    }
                }
                lshtml = lshtml + '<div class="weui-form-preview__ft" style="    height: 50px;">' +
                    '       <button  id="viewReport" style=" background-color: #00AFEC;border-radius: 6px;border: none; color: white;width: 60px;height: 30px;margin-right: 60px;" onclick="viewPDF(\'' + result.fzjcxxDtos[i].fjid + '\')">预览</button>' +
                    '       <button  id="viewReport" style=" background-color: #00AFEC;border-radius: 6px;border: none; color: white;width: 60px;height: 30px;" onclick="xz(\'' + result.fzjcxxDtos[i].fjid + '\',\'' + result.fzjcxxDtos[i].sign + '\')">下载</button>' +
                    '   </div>'
                lshtml = lshtml + '</div>' +
                    '</div>'
                    // lshtml=lshtml+'<div style="height: 5px;background-color:#3c7bfb;">'+'</div>'
            }
            /*} else {*/

            $("#selectbBg").append(lshtml)

            if (len<5){
                //拼接“没有更多数据”
                loadMoreBgFlag = 1
                    $("#selectbBg").append(noMoreHtml)
            }
            $("#loadMore").attr("hidden", true);
            $("#bghidden").attr("hidden",true)
        },
        fail: function (result) {
            layer.msg('加载失败！');
        }
    })
}

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
                                        window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                                    window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                    window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
                }
            }]
        });
    }

}

//发票详情
function invoiceInfo(fzjcid,zffs,orderno){
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
                                        window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
                                    }
                                }]
                            });
                        }else if (orderInfo.result.isInvoiced == "3"){
                            weui.alert("正在开票中请稍后！", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                                                window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                                                window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                                                window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                                                window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
                                            }
                                        }]
                                    });
                                }
                            })
                        }

                    }else{
                        window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
                    }
                } else {
                    weui.alert(result.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                                window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
                        window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
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
            if (flag==0){
                if (loadMoreEntireFlag == 0) {
                loadMore()
                }
            } else if(flag==1&&flag1==1){
                if (loadMoreBgFlag == 0) {
                    loadMoreBg()
                }
            }

    }
});



$(document).ready(function () {
    // 在这里写你的代码...
    loadMore();
    // layer.msg('该检测正在进行或已完成，不可修改！');
    // $("#loadMore").attr("hidden",true);
});

function detectionReport(fzjcid){
    var url="/wechat/viewReport?fzjcid="+fzjcid;
    window.location.href=url;
}

function othersReport(){
    var url =  "/wechat/moreReport";
    window.location.href=url;
}


function myReport() {
    //设置标记为0
    if (flag != 0) {
        flag = 0;
        $('#myReport').removeClass("unChooseBtn");
        $('#selectReport').removeClass("chooseBtn");
        $('#myReport').addClass("chooseBtn");
        $('#selectReport').addClass("unChooseBtn");
        $('#fzjcxxDtos').show()
        $('#selectbBg').hide()
        /*按钮下的横杠*/
        $('#myReport').addClass("anhg");
        $('#selectReport').removeClass("anhg");
    }
}
function selectReport() {
    //设置标记为1
    if (flag != 1) {
        flag = 1;
        $('#selectReport').removeClass("unChooseBtn");
        $('#myReport').removeClass("chooseBtn");
        $('#selectReport').addClass("chooseBtn");
        $('#myReport').addClass("unChooseBtn");
        $('#selectbBg').show()
        $('#fzjcxxDtos').hide()
        /*按钮下的横杠*/
        $('#myReport').removeClass("anhg");
        $('#selectReport').addClass("anhg");
    }
}



function viewPDF(fjid){
    var url= "/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
    window.location.href=url;
}
function xz(fjid,sign){
    $("#ajaxForm1 #fjid").val(fjid);
    $("#ajaxForm1 #sign").val(sign);
    $("#ajaxForm1").attr("action","/wechat/file/loadFile");
    $("#ajaxForm1").submit();
}
