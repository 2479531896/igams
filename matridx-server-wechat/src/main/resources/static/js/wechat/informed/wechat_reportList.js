var pageNumber = 0;
var pageSize = 15;
var loadMroeFlag = true;
var loadingeFlag = true;
var searchInput;
var cyrq;
var jsrq;
var JPGMaterConfig = {
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//定义计时器(二维码预览使用)
var interval = null;
/**
 * 付款标记格式化
 * @param row
 * @returns {string}
 */
function fkbjFormatter(row){
    var html = "";
    if(row.sfje != null && row.sfje != "" && row.sfje != "null" && Number(row.sfje) > 0){
        html += "<img src=\"/images/inspectionServices/payed.png\" style=\"position: absolute;right: 5px;width: 1rem;\">"
    }else {
        html += "<img src=\"/images/inspectionServices/disPay.png\" style=\"position: absolute;right: 5px;width: 1rem;\">"
    }
    return html;
}

/**
 * 标记格式化
 * @param row
 * @returns {string}
 */
function sloganFormatter(row){
    var html = "";
    if(!row.jsrq){
        if(row.zt == '80'){
            html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #00AFEC\">运输中</div>";
        }else if(row.sfws != '3'){
            html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #53C31B\">待完善</div>";
        }else {
            html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #53C31B\">未接收</div>";
        }
    }else{
          if(!row.syrq&&!row.dsyrq&&!row.qtsyrq&&!row.fxwcsj&&!row.bgrq){
            html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #53C31B\">已接收</div>";
          }else if((row.syrq||row.dsyrq||row.qtsyrq)&&!row.fxwcsj&&!row.bgrq){
              html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #53C31B\">实验中</div>";
          }else if (!row.bgrq) {
             //根据sfws判断
             if (row.sfws != '3') {
                 html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #53C31B\">待完善</div>";
             }else if(row.fxwcsj){
                html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: rgb(182,182,182)\">报告审核中</div>";
             }
         }else if(row.sfzq!=null&& row.lcfk!=null){
             html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #FAAD14\">已反馈</div>";
         }else{
              html += "<div style=\"position: absolute;right: 5px;font-size: 15px;color: #FF4D4F\">未反馈</div>";
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
    if(!row.jsrq){
        html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="modInspection(\''+row.sjid+'\',\''+row.jsrq+'\',\''+row.bgrq+'\')"><i class="weui-icon-edit"></i>　修改</div>'
    }else if (!row.bgrq){
        html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="perfectInfo(\''+row.sjid+'\',\''+row.jsrq+'\',\''+row.bgrq+'\')"><i class="weui-icon-perfect"></i>　完善</div>'
    }else {
        html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="feedback(\''+row.sjid+'\',\''+row.ybbh+'\',\''+row.ybbh+'\')"><i class="weui-icon-reBack"></i>　反馈</div>'
    }
    html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="complaint(\''+row.sjid+'\',\''+row.ybbh+'\')"><i class="weui-icon-complaint"></i>　投诉</div>'
    html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="payModal(\''+row.sjid+'\',\''+row.ybbh+'\',\''+row.hzxm+'\')"><i class="weui-icon-pay"></i>　支付</div>'
    /*将清单中扫码支付和转账付款合并成一个按钮*/
    // html += '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="payModal(\''+row.sjid+'\',\''+row.ybbh+'\',\''+row.hzxm+'\',\'scan\')"><i class="weui-icon-payScan"></i>　扫码付款</div>' +
    //         '<div style="line-height: 40px;text-align: center;flex: 1;font-size: 14px;" onclick="payModal(\''+row.sjid+'\',\''+row.ybbh+'\',\''+row.hzxm+'\',\'transfer\')"><i class="weui-icon-transfer"></i>　转账付款</div>'

    return html;
}

/**
 * 投诉操作
 * @param sjid
 * @param ybbh
 * @param hzxm
 */
function complaint(sjid,ybbh){
    window.location.href = "/wechat/inspComplaint?wxid=" + $("#wxid").val() + "&wbcxdm=" + $("#wbcxdm").val() + "&ywid=" + sjid + "&ybbh=" + ybbh;
}

/**
 * 修改操作
 * @param sjid
 * @param jsrq
 * @param bgrq
 * @returns {boolean}
 */
function modInspection(sjid,jsrq,bgrq){
	if(jsrq && jsrq != "null"){
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
	var flag=$("#flag").val();
	var jcxmdms=$("#jcxmdms").val();
	var wxid = $("#wxid").val();
	var wbcxdm = $("#wbcxdm").val();
	jQuery('<form action="/wechat/inspection/modInspection" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="wxid" value="'+wxid+'"/>' +
            '<input type="text" name="wbcxdm" value="'+wbcxdm+'"/>' +
            '<input type="text" name="sjid" value="'+sjid+'"/>' +
            '<input type="text" name="flag" value="'+flag+'"/>' +
            '<input type="text" name="jcxmdms" value="'+jcxmdms+'"/>' +
        '</form>')
    .appendTo('body').submit().remove();
}
/**
 * 完善操作
 * @param sjid
 * @param jsrq
 * @param bgrq
 * @returns {boolean}
 */
function perfectInfo(sjid,jsrq,bgrq){
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
    var flag=$("#flag").val();
    var jcxmdms=$("#jcxmdms").val();
    var wxid = $("#wxid").val();
    var wbcxdm = $("#wbcxdm").val();
    jQuery('<form action="/wechat/inspection/modInspection" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="wxid" value="'+wxid+'"/>' +
        '<input type="text" name="wbcxdm" value="'+wbcxdm+'"/>' +
        '<input type="text" name="sjid" value="'+sjid+'"/>' +
        '<input type="text" name="flag" value="'+flag+'"/>' +
        '<input type="text" name="actionFlag" value="1"/>' +//actionFlag 1:完善 未传:修改
        '<input type="text" name="jcxmdms" value="'+jcxmdms+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

/**
 * 删除按钮
 * @param event
 * @returns
 */
function del(sjid,zt,jsrq){
    if(jsrq && jsrq !="null"){
        $.alert("已接收的样本不可删除！");
        return
    }
    $.confirm({
        title: '警告',
        text: '<span style="color: red;">确认删除？</span>',
        onOK: function () {
            //点击确认
            $.ajax({
                url:'/wechat/miniprogram/delInspection',
                type:'post',
                dataType:'json',
                data:{"sjid":sjid,"wxid":$("#wxid").val(),"zt":zt},
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



	/*$.confirm("确认删除？", function(result){
		if(result){
			$.ajax({
				url:'/wechat/miniprogram/delInspection',
				type:'post',
				dataType:'json',
				data:{"sjid":sjid,"wxid":$("#wxid").val(),"zt":zt},
				success:function(data){
					if(data.status=="success"){
						$.closeModal("operateModal");
						$.confirm("删除成功！",function(){
							searchReportResult();
						})
					}else if(data.state=="false"){
						$.alert("删除失败！");
					}
				}
			})
		}
	})*/
}

/**
 * 反馈按钮
 * @returns
 */
function feedback(ybbh){
	window.location.href="/wechat/inspection/feedBack?ybbh="+ybbh;
}

/**
 * 调整按钮
 * @returns
 */
// function adjust(sjid,ybbh,hzxm){
//     window.location.href="/wechat/inspection/adjust?sjid="+sjid+"&ybbh="+ybbh+"&hzxm="+hzxm+"&wxid="+$("#wxid").val();
// }

/**
 * 转账、扫码付款
 * @param event
 * @returns
 */
var paying = false;
function payModal(sjid,ybbh,hzxm,flag){
    $.ajax({
        url: '/wechat/inspection/getDetectionPayInfo',
        type: 'post',
        dataType: 'json',
        data : {"sjid" : sjid},
        success: function(data) {
            if(data!=null && data.length >0){
                var htmltext = '<div class="weui-media-box_appmsg" style="height: 35px">' +
                    '<div style="flex: 2;text-align: left">标本编号</div>' +
                    '<div style="flex: 3;text-align: left"><input disabled id="fk_ybbh" name="fk_ybbh" class="weui-input" style="color: black;font-weight: bold;" value="'+ybbh+'"></div>' +
                    '<input type="hidden" id="fk_sjid" name="fk_sjid" value="\''+sjid+'\'">' +
                    '<div style="flex: 1"><button type="button" class="weui-icon-scan" style="color: #00AFEC" onClick="getQRCode()" className="weui-btn weui-btn_mini weui-btn_primary weui-wa-hotarea" style="font-size: smaller;padding: 0 5px;">扫码</button></div>' +
                    '</div>' +
                    '<div class="weui-media-box_appmsg" style="height: 35px">' +
                    '<div style="flex: 2;text-align: left">患者姓名</div>' +
                    '<div style="flex: 4;text-align: left"><input disabled id="fk_hzxm" name="fk_hzxm" class="weui-input" style="color: black;font-weight: bold;" value="'+hzxm+'"></div>' +
                    '</div>'
                for (let i = 0; i < data.length; i++) {
                    htmltext += '<div class="weui-media-box_appmsg" style="border-top: 2px solid grey;height: 35px"><div style="font-weight: bolder;text-align: left">'+(data[i].jczxmmc?data[i].jczxmmc:data[i].jcxmmc)+'</div></div>'
                    var sfje='0';
                    if(data[i].sfje){
                        sfje=data[i].sfje;
                    }
                    htmltext += '<div class="weui-media-box_appmsg" style="'+(i==data.length-1?'border-bottom: 2px solid grey;':'')+'height: 35px"><div style="text-align: left;width:100%"><input id="fk_fkje_'+data[i].jcxmid+(data[i].jczxmid?"_"+data[i].jczxmid:"")+'" name="fk_fkje" class="weui-input" type="number" style="color: black;font-weight: bold;" placeholder="请输入金额..."></div></div>'
                }
                htmltext += '<div class="weui-media-box_appmsg" style="height: 50px">' +
                    '<div style="flex: 2;text-align: left">支付方式</div>' +
                    '<div style="flex: 4;text-align: left;display: flex;">' +
                    '<span class="RadioStyle" style="flex: 1;margin: 0 2px">' +
                    '<input type="radio" id="scanPay" name="fk_fkfs" value="scan" validate="{required:true}" checked/>' +
                    '<label for="scanPay" style="font-size: small;width: 50px">扫码</label>' +
                    '</span>' +
                    '<span class="RadioStyle" style="flex: 1;margin: 0 2px">' +
                    '<input type="radio" id="transferPay" name="fk_fkfs" value="transfer" validate="{required:true}"/>' +
                    '<label for="transferPay" style="font-size: small;width: 50px">转账</label>' +
                    '</span>' +
                    '</div>' +
                    '</div>';
                    $.modal({
                    title: "核对用户信息",
                    text:htmltext,
                    autoClose: false,
                    buttons: [
                        {
                            text: (flag=="scan"?"扫码付款":(flag=="transfer"?"支付宝付款":"支付")),
                            onClick: function(){
                                if(paying){
                                    return
                                }
                                paying = true
                                pay((flag?flag:$("input:radio[name='fk_fkfs']:checked").val()),sjid,ybbh,hzxm)
                            }
                        },
                        {
                            text: "取消",
                            className: "default",
                            onClick: function(){
                                $.closeModal()
                            }
                        },
                    ]
                });
            }else {
                $.toptip('请选择检测项目后支付！', 'error');
            }

        },
        fail:function (data) {
            $.toptip('网络错误！'+data.message, 'error');
        }
    })

}

/**
 * 调用外部支付接口
 * @param flag
 * @param sjid
 * @param ybbh
 * @returns {boolean}
 */
function pay(flag,sjid,ybbh,hzxm) {
    if(!$("#fk_ybbh").val()){
        $.toptip('请填写标本编号!', 'error');
        paying = false;
        return false;
    }
    var fk_fkjeList = document.getElementsByName("fk_fkje");
    var zfjcxm = [];
    var zje = 0;
    if(fk_fkjeList.length>0){
        for (let i = 0; i < fk_fkjeList.length; i++) {
            let fkje = fk_fkjeList[i].value;//付款金额
            if (fkje && (!/^\d+(\.\d{1,2})?$/.test(fkje) || fkje*1 <= 0)){
                $.toptip('请填写付款金额, 允许保留两位小数!', 'error');
                paying = false;
                return false;
            }
            var fk_id_list = fk_fkjeList[i].id.split("_");
            let jcxm = fk_id_list[2];//检测项目
            let json= {};
            json.jcxmid = jcxm//组装
            json.je = fkje?fkje:0//组装
            if (json.je == 0){
                continue;
            }
            if (fk_id_list.length > 3){
                json.jczxmid = fk_id_list[3];//组装
            }
            zfjcxm.push(json);
            zje += new Number(fkje?fkje:0);
        }
        if (zje == 0){
            $.toptip('请填写付款金额!', 'error');
            paying = false;
            return false;
        }
        zje = ""+zje;
        zfjcxm = JSON.stringify(zfjcxm);
        $("#zfjcxm").val(zfjcxm);
    }else {
        $.toptip('请确认该样本是否已选择检测项目并填写金额!', 'error');
        paying = false;
        return false;
    }
    if(flag == "scan"){
        $.ajax({
            url: '/wechat/pay/createOrderQRCode',
            type: 'post',
            dataType: 'json',
            data : {
                "ywid" : sjid,
                "ybbh" : ybbh,
                "fkje": zje,
                "wxid": $("#wxid").val(),
                "ywlx": $("#ywlx").val(),
		        "zfjcxm":zfjcxm,
                "wbcxdm":$("#wbcxdm").val()
            },
            success: function(data) {
                if(data.status == 'success'){
                    // 二维码路径 data.qrCode
                    $("#preview_ajaxForm #path").val(data.filePath);
                    //去掉患者姓名和样本编号中的空格
                    for (;hzxm.indexOf(" ")>-1;) {
                        hzxm = hzxm.replace(" ","");
                    }
                    for (;ybbh.indexOf(" ")>-1;) {
                        ybbh = ybbh.replace(" ","");
                    }
                    var url="/wechat/pay/picturePreview?path="+ data.filePath +"&fkje="+zje +"&ywid="+sjid +"&ybbh="+ ybbh +"&hzxm="+ hzxm+"&ywlx="+$("#ywlx").val();
                    $.closeModal()
                    $("#app").load(url);
                    paying = false
                    // window.location.href=(url);
                }else{
                    $.toptip('生成订单失败！'+data.message, 'error');
                    paying = false
                }
            },
            fail:function (data) {
                $.toptip('网络错误！'+data.message, 'error');
                paying = false
            }
        })
    }else if(flag == "transfer"){
        // 支付宝支付 判断是否为微信环境
        var ua = navigator.userAgent.toLowerCase();
        // if (ua.indexOf('micromessenger') != -1) {
        //     paying = false
        //     // 打开引导页
        //     window.location.href="/wechat/pay/alipayGuide?ywid="+ sjid +"&ybbh="+ ybbh +"&fkje="+ zje +"&wxid="+  $("#wxid").val() +"&ywlx="+ $("#ywlx").val()+"&zfjcxm="+$("#zfjcxm").val();
        // }else{
            // 创建支付宝native订单
            $.ajax({
                url: '/wechat/pay/alipayNative',
                type: 'post',
                dataType: 'json',
                data : {
                    "ywid" : sjid,
                    "ybbh" : ybbh,
                    "fkje": zje,
                    "wxid": $("#wxid").val(),
                    "ywlx": $("#ywlx").val(),
		            "zfjcxm":$("#zfjcxm").val(),
                    "wbcxdm":$("#wbcxdm").val()
                },
                success: function(data) {
                    if(data.status == 'success'){
                        $.closeModal()
                        paying = false
                        // 唤起支付宝路径 data.qrCode
                        window.location.href = data.qrCode;
                    }else{
                        $.toptip('生成订单失败！'+data.message, 'error');
                        paying = false
                    }
                }
            });
        // }
    }else {
        // 微信统一下单
        $.ajax({
            url: '/wechat/pay/wechatPayOrder',
            type: 'post',
            dataType: 'json',
            data : {
                "sjid" : sjid,
                "ybbh" : ybbh,
                "fkje": zje,
                "wxid": $("#wxid").val(),
                "ywlx": $("#ywlx").val(),
                "wxzflx":"public",
		"zfjcxm":$("#zfjcxm").val(),
                "wbcxdm":$("#wbcxdm").val()
            },
            success: function(data) {
                if(data.status == 'success'){
                    // 唤起微信支付路径
                    var appId = data.payData.appId; // 公众号名称,由商户传入
                    var timeStamp = data.payData.timeStamp; // 时间戳,自1970年以来的秒数
                    var nonceStr = data.payData.nonceStr; // 随机串
                    var packages = data.payData.package; //微信订单
                    var signType = data.payData.signType; // 微信签名方式
                    var paySign = data.payData.paySign // 微信签名
                    //准备发起微信支付
                    if (typeof WeixinJSBridge == "undefined") {
                        if (document.addEventListener) {
                            document.addEventListener('WeixinJSBridgeReady',onBridgeReady, false);
                        } else if (document.attachEvent) {
                            document.attachEvent('WeixinJSBridgeReady',onBridgeReady);
                            document.attachEvent('onWeixinJSBridgeReady',onBridgeReady);
                        }
                    } else {
                        onBridgeReady(appId,timeStamp,nonceStr,packages,signType,paySign);
                    }
                    paying = false
                }else{
                    $.toptip('生成订单失败！'+data.message, 'error');
                    paying = false
                }
            }
        });
    }
}

/**
 * 根据条件查询
 */
function searchReportResult(){
    // 模糊查询内容
    searchInput = $('#report_formSearch #searchInput').val();
    //采样日期
    cyrq = $('#cyrq').val();
    //接收日期
    jsrq = $('#jsrq').val();
    pageNumber = 0;
    loadMroeFlag = true;
    $("#sjxxList").html("")
    loadMore()
}

/**
 * 采样日期改变事件
 */
function chageCyrq(){
    weui.datePicker({
        id: 'datePickerCyrq',
        onChange: function(result){
        },
        onConfirm: function(result){
            var cyrq = result[0].value + '-' + ((result[1].value>9)?result[1].value:'0'+result[1].value) + '-' + ((result[2].value>9)?result[2].value:'0'+result[2].value)
            $("#cyrq").val(cyrq)
        }
    });
}

/**
 * 接受日期改变事件
 */
function chageJsrq(){
    weui.datePicker({
        id: 'datePickerJsrq',
        onChange: function(result){
        },
        onConfirm: function(result){
            var jsrq = result[0].value + '-' + ((result[1].value>9)?result[1].value:'0'+result[1].value) + '-' + ((result[2].value>9)?result[2].value:'0'+result[2].value)
            $("#jsrq").val(jsrq)
        }
    });
}

/**
 * 清空筛选条件
 */
function emptyFilter(){
    $("#cyrq").val("");
    $("#jsrq").val("");
    $("#sfjeStart").val("");
    $("#sfjeEnd").val("");
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
 * 支付框中的扫码事件
 */
function getQRCode(){
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
                                // result = "http://service.matridx.com/wechat/getUserReport?ybbh=20220216003"
                                var s_res = result.split('ybbh=')
                                var scanYbbh = s_res[s_res.length-1];
                                $.ajax({
                                    url: '/wechat/pay/getInspectionInfo',
                                    type: 'post',
                                    data: {"ybbh": scanYbbh},
                                    dataType: 'json',
                                    success: function(data) {
                                        if(data.status == "success"){
                                            $("#fk_ybbh").val(data.sjxxDto.ybbh);
                                            $("#fk_sjid").val(data.sjxxDto.sjid);
                                            $("#fk_hzxm").val(data.sjxxDto.hzxm);
                                            $("#fk_fkje").val("");
                                        }else{
                                            $.toptip('未查询到标本'+ybbh, 'error');
                                        }
                                    }
                                });
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
            url: '/wechat/reportlist',
            type: 'post',
            dataType: 'json',
            data : {"sortName" : "sjxx.lrsj",
                "sortOrder" : "desc",
                "pageNumber": pageNumber,
                "pageSize": pageSize,
                "wxid": $("#wxid").val(),
                "sign": $("#sign").val(),
                "sfjeStart": $("#sfjeStart").val(),
                "sfjeEnd": $("#sfjeEnd").val(),
                "entire":searchInput,
                "jsrq":jsrq,
                "cyrq":cyrq,
                "jcxmdms":$("#jcxmdms").val(),
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
        html += '<div class="weui-panel weui-cell_swiped" style="border-radius: 10px;margin: 8px;" data-sjid="'+rows[i].sjid+'">' +
            '            <div class="weui-cell__bd" style="border-radius: 10px;transform: translate3d(0px, 0px, 0px);">' +
            '                <div class="weui-form-preview" style="border-radius: 10px;">' +
            '                    <div style="height: 0.1rem;"></div>' +
            '                    <div class="weui-form-preview__hd weui-media-box_appmsg" style="border-left: 3px solid #00AFEC;margin-bottom: 0;padding: 0.1rem 0.2rem;" onclick="reportDealById(\''+rows[i].sjid+'\',\''+rows[i].ybbh+'\',\''+rows[i].hzxm+'\')">' +
            '                        <div class="weui-media-box__bd" style="flex: 2">' +
            '                            <label class="weui-form-preview__value" style="margin: 0;text-align: left">'+(rows[i].hzxm?rows[i].hzxm:"")+'</label>' +
            '                        </div>' +
            '                        <div class="weui-media-box__bd" style="flex: 3">' +
            '                            <label class="weui-form-preview__value" style="margin: 0;font-size: 15px;text-align: left;line-height: normal;color: rgba(0,0,0,0.5)">'+(rows[i].jsrq?rows[i].jsrq.substring(0,(rows[i].jsrq.indexOf(".")!=-1?rows[i].jsrq.indexOf("."):rows[i].jsrq.length)):"暂无接收时间")+'</label>' +
            // '                            <label class="weui-form-preview__label" style="margin: 0;font-size: 15px;text-align: center;line-height: normal;">'+(rows[i].jsrq?rows[i].jsrq.substring(0,(rows[i].jsrq.indexOf(".")!=-1?rows[i].jsrq.indexOf("."):rows[i].jsrq.length)):"暂无接收时间")+'</label>' +
            '                        </div>'+
            fkbjFormatter(rows[i]) +
            '                    </div>' +
            '                    <div class="weui-form-preview__bd" style="font-size: 14px;font-weight: bolder;padding: 0px 10px" onclick="reportDealById(\''+rows[i].sjid+'\',\''+rows[i].ybbh+'\',\''+rows[i].hzxm+'\')">' +
            sloganFormatter(rows[i])+
            '                       <div style="display: flex">' +
            '                        <div class="weui-form-preview__item" style="flex: 4">' +
            '                            <label class="" style="float: left;margin-right: 2px;">编号：</label>' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].ybbh?rows[i].ybbh:"")+'</span>' +
            '                        </div>' +
            '                        <div class="weui-form-preview__item" style="flex: 4">' +
            '                            <label class="" style="float: left;margin-right: 2px;">金额：</label>' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].sfje?rows[i].sfje:"")+'</span>' +
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
            '            <div class="weui-cell__ft" style="background-color: var(--weui-RED)" onclick="del(\''+rows[i].sjid+'\',\''+rows[i].zt+'\',\''+rows[i].jsrq+'\')">' +
            '                <a class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" style="margin: auto">删除</a>' +
            '            </div>' +
            '        </div>'
    }
    $("#sjxxList").append(html)
    $('.weui-cell_swiped').swipeout()
}


//查看
function reportDealById(sjid,ybbh,hzxm){
    var sign = $("#sign").val()
    jQuery('<form action="/wechat/getReportView" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="wxid" value="'+$("#wxid").val()+'"/>' +
        '<input type="text" name="sjid" value="'+sjid+'"/>' +
        '<input type="text" name="hzxm" value="'+hzxm+'"/>' +
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

/**
 * 微信支付
 * @param data
 */
function onBridgeReady(appId,timeStamp,nonceStr,packages,signType,paySign,ddh) {
    WeixinJSBridge.invoke('getBrandWCPayRequest', {
        "appId" : appId, // 公众号名称,由商户传入
        "timeStamp" : timeStamp, // 时间戳,自1970年以来的秒数
        "nonceStr" : nonceStr, // 随机串
        "package" : packages, //微信订单
        "signType" : signType, // 微信签名方式
        "paySign" : paySign // 微信签名
    }, function(res) {
        if (res.err_msg == "get_brand_wcpay_request:ok") {
            alert("支付成功,支付成功后跳转到页面："+res.err_msg);
            // 支付成功后跳转的页面
            $("#preview_ajaxForm").attr('action',"/wechat/pay/wxPayComplete");
            $("#preview_ajaxForm").attr('method',"get");
            $("#preview_ajaxForm").submit();
        } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
            alert("支付取消,支付取消后跳转到页面："+res.err_msg);
        } else if (res.err_msg == "get_brand_wcpay_request:fail") {
            WeixinJSBridge.call('closeWindow');
            alert("支付失败,支付失败后跳转到页面："+res.err_msg);
            // 支付失败后跳转的页面
            $("#preview_ajaxForm").attr('action',"/wechat/pay/wxPayFaild");
            $("#preview_ajaxForm").attr('method',"get");
            $("#preview_ajaxForm").submit();
        } // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
    });
}
