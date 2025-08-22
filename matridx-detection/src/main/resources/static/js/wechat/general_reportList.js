var pageNumber = 0;
var pageSize = 15;
var loadMroeFlag = true;
var loadingeFlag = true;
var searchInput;
var cyrq;
var jssj;
var JPGMaterConfig = {
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 标记格式化
 * @param row
 * @returns {string}
 */
function sloganFormatter(row){
    var html = "";
    if(!row.jssj){
        html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #53C31B\">未接收</div>";
    }else{
        if (!row.bgrq) {
            html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: rgb(182,182,182)\">未出报告</div>";
        }else{
            html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #FF4D4F\">已出报告</div>";
        }
    }
    return html;
}

/**
 * 送检单位格式化
 * @param row
 * @returns string
 */
function sjdwFormatter(row){
    var sjdw = "";
    if (row.sjdwmc) {
        if( row.sjdwbj == '1'){
            sjdw = row.sjdwmc;
        }else{
            if (row.sjdwjc){
                sjdw = row.sjdwjc;
            }else {
                sjdw = row.sjdwmc;
            }
        }
    }
	return sjdw
}

/**
 * 操作按钮格式化
 * @param row
 */
function buttonFormatter(row){
    var html = "";
    if(!row.jssj){
        html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="modInspection(\''+row.fzjcid+'\',\''+row.jssj+'\',\''+row.bgrq+'\')"><i class="weui-icon-edit"></i>　修改</div>'
    }
    html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="reportDealById(\''+row.fzjcid+'\',\''+row.ybbh+'\')"><i class="weui-icon-reBack"></i>　查看</div>'
    return html;
}


/**
 * 修改操作
 * @param fzjcid
 * @param jssj
 * @param bgrq
 * @returns {boolean}
 */
function modInspection(fzjcid,jssj,bgrq){
	if(jssj && jssj != "null"){
        $.alert({
            title: '警告',
            text: '<span style="color: red;">送检信息已收样，不能修改！</span>',
            onOK: function () {
                //点击确认
                return false;
            }
        });
        return false;
	}
    if(bgrq && bgrq != "null"){
        $.alert({
            title: '警告',
            text: '<span style="color: red;">送检信息已出报告，不能修改！</span>',
            onOK: function () {
                //点击确认
                return false;
            }
        });
        return false;
    }
	var fzjclx=$("#fzjclx").val();
	var wxid = $("#wxid").val();
	var wbcxdm = $("#wbcxdm").val();
	jQuery('<form action="/wechat/detectionPJ/modGeneralInspection" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="wxid" value="'+wxid+'"/>' +
            '<input type="text" name="wbcxdm" value="'+wbcxdm+'"/>' +
            '<input type="text" name="fzjcid" value="'+fzjcid+'"/>' +
            '<input type="text" name="fzjclx" value="'+fzjclx+'"/>' +
        '</form>')
    .appendTo('body').submit().remove();
}

/**
 * 删除按钮
 * @param event
 * @returns
 */
function del(fzjcid,zt,jssj){
    if(jssj && jssj !="null"){
        $.alert("已接收的样本不可删除！");
        return
    }
    $.confirm({
        title: '警告',
        text: '<span style="color: red;">确认删除？</span>',
        onOK: function () {
            //点击确认
            $.ajax({
                url:'/wechat/detectionPJ/delGeneralInspection',
                type:'post',
                dataType:'json',
                data:{"fzjcid":fzjcid,"wxid":$("#wxid").val()},
                success:function(data){
                    if(data.status=="success"){
                        $.alert({
                            title: '提示',
                            text: '删除成功！',
                            onOK: function () {
                                //点击确认
                                searchReportResult();
                            }
                        });
                    }else if(data.state=="false"){
                        $.alert("删除失败！");
                    }
                }
            })
        },
        onCancel: function () {
            $.closeModal();
        }
    });
}

/**
 * 根据条件查询
 */
function searchReportResult(){
    // 模糊查询内容
    searchInput = $('#report_formSearch #searchInput').val();
    pageNumber = 0;
    loadMroeFlag = true;
    $("#infoList").html("")
    loadMore()
}

/**
 * 样本编号扫码事件
 */
function getScanYbbhQRCode(){
    $.ajax({
        url: '/wechat/getJsApiInfo',
        type: 'post',
        data: {
            "url":location.href.split('#')[0],
            "wbcxdm":$("#wbcxdm").val()
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
                jsApiList: ['checkJsApi','scanQRCode']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function(res) {
                console.log(res);
            });
            //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
            wx.ready(function() {
                wx.checkJsApi({
                    jsApiList: ['scanQRCode'],
                    success: function(res) {
                        //扫码
                        wx.scanQRCode({
                            needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
                            scanType: ["qrCode", "barCode"],
                            success: function(res) {
                                // 当needResult 为 1 时，扫码返回的结果
                                var result = res.resultStr;
                                // result = "http://service.matridx.com/wechat/getUserReport?ybbh=1112"
                                var s_res = result.split('ybbh=')
                                var scanYbbh = s_res[s_res.length-1];
                                $("#searchInput").val(s_res[s_res.length-1]);

                                if(scanYbbh!=null && scanYbbh!= "")
                                    searchReportResult();
                            },
                            fail: function(res) {
                                alert(JSON.stringify(res));
                            }
                        });
                    }
                });
            });
        }
    });
}

/**
 * 加载更多
 */
function loadMore(){
    if (loadMroeFlag){
        loadMroeFlag =false
        pageNumber = pageNumber+1;
        $("#loading").show();
        $("#noInfo").hide();
        $("#loadComplete").hide();
        $.ajax({
            url: '/wechat/detectionPJ/generalReportlist',
            type: 'post',
            dataType: 'json',
            data : {"sortName" : "fzjcxx.lrsj",
                "sortOrder" : "desc",
                "pageNumber": pageNumber,
                "pageSize": pageSize,
                "wxid": $("#wxid").val(),
                "sign": $("#sign").val(),
                "entire":searchInput,
                "fzjclx":$("#fzjclx").val(),
            },
            success: function(data) {
                if (data.rows.length>0){
                    htmlConcat(data.rows);
                    loadMroeFlag = true
                    if (data.rows.length<pageSize){
                        loadMroeFlag = false
                        $("#loading").hide();
                        $("#noInfo").hide();
                        $("#loadComplete").show();
                    }
                }else {
                    loadMroeFlag = false
                    if (pageNumber==1){
                        $("#loading").hide();
                        $("#noInfo").show();
                        $("#loadComplete").hide();
                    }else {
                        $("#loading").hide();
                        $("#noInfo").hide();
                        $("#loadComplete").show();
                    }
                }
            }
        });
    }
}

/**
 * html拼接
 * @param rows
 */
function htmlConcat(rows){
    var html = '';
    for (var i = 0; i < rows.length; i++) {
        html += '<div class="weui-panel weui-cell_swiped" style="border-radius: 10px;margin: 8px;" data-fzjcid="'+rows[i].fzjcid+'">' +
            '            <div class="weui-cell__bd" style="border-radius: 10px;transform: translate3d(0px, 0px, 0px);">' +
            '                <div class="weui-form-preview" style="border-radius: 10px;">' +
            '                    <div style="height: 0.1rem;"></div>' +
            '                    <div class="weui-form-preview__hd weui-media-box_appmsg" style="border-left: 3px solid #00AFEC;margin-bottom: 0;padding: 0.1rem 0.2rem;">' +
            '                        <div class="weui-media-box__bd" style="flex: 2">' +
            '                            <label class="weui-form-preview__value" style="margin: 0;text-align: left">'+(rows[i].xm?rows[i].xm:"")+'</label>' +
            '                        </div>' +
            '                        <div class="weui-media-box__bd" style="flex: 3">' +
            '                            <label class="weui-form-preview__value" style="margin: 0;font-size: 15px;text-align: left;line-height: normal;color: rgba(0,0,0,0.5)">'+(rows[i].jssj?rows[i].jssj.substring(0,(rows[i].jssj.indexOf(".")!=-1?rows[i].jssj.indexOf("."):rows[i].jssj.length)):"暂无接收时间")+'</label>' +
            // '                            <label class="weui-form-preview__label" style="margin: 0;font-size: 15px;text-align: center;line-height: normal;">'+(rows[i].jssj?rows[i].jssj.substring(0,(rows[i].jssj.indexOf(".")!=-1?rows[i].jssj.indexOf("."):rows[i].jssj.length)):"暂无接收时间")+'</label>' +
            '                        </div>'+
            '                    </div>' +
            '                    <div class="weui-form-preview__bd" style="font-size: 14px;font-weight: bolder;padding: 0px 10px">' +
            sloganFormatter(rows[i])+
            '                       <div style="display: flex">' +
            '                        <div class="weui-form-preview__item" style="flex: 4">' +
            '                            <label class="" style="float: left;margin-right: 2px;">编号：</label>' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].ybbh?rows[i].ybbh:"")+'</span>' +
            '                        </div>' +
            '                       </div>' +
            '                       <div style="display: flex">' +
            '                        <div class="weui-form-preview__item" style="flex: 3">' +
            '                            <label class="" style="float: left;margin-right: 2px;">类型：</label>' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].yblxmc?rows[i].yblxmc:"")+'</span>' +
            '                        </div>' +
            '                        <div class="weui-form-preview__item" style="flex: 4">' +
            '                            <label class="" style="float: left;margin-right: 2px;">单位：</label>' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+sjdwFormatter(rows[i])+'</span>' +
            '                        </div>' +
            '                       </div>' +
            '                    </div>' +
            '                    <div class="weui-form-preview__ft">' +
            buttonFormatter(rows[i])+
            '                    </div>' +
            '                </div>' +
            '            </div>' +
            '            <div class="weui-cell__ft" style="background-color: var(--weui-RED)" onclick="del(\''+rows[i].fzjcid+'\',\''+rows[i].zt+'\',\''+rows[i].jssj+'\')">' +
            '                <a class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" style="margin: auto">删除</a>' +
            '            </div>' +
            '        </div>'
    }
    $("#infoList").append(html)
    $('.weui-cell_swiped').swipeout()
}


//查看
function reportDealById(fzjcid,ybbh){
    var sign = $("#sign").val()
    jQuery('<form action="/wechat/getGeneralReportView" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="wxid" value="'+$("#wxid").val()+'"/>' +
        '<input type="text" name="fzjcid" value="'+fzjcid+'"/>' +
        '<input type="text" name="ybbh" value="'+ybbh+'"/>' +
        '<input type="text" name="sign" value="'+sign+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

$(function(){
    // halfScreen()
	loadMore()
    $("#loadComplete").hide();
    $("#noInfo").hide();

});

/**
 * 下拉到底部触发事件
 */
$(document.body).infinite(50).on("infinite", function() {
    var loading = false;
    if(loading){
        return
    }
    loading = true;
    if (loadMroeFlag) {
        loadMore()
    }
    loading = false
});

