//事件绑定
function btnBind() {
	//支付宝支付
	var btn_alipay = $("#ajaxForm #btn_alipay");
	//微信支付
	var btn_wxpay = $("#ajaxForm #btn_wxpay");
	//扫码
	var btn_qrscan = $("#ajaxForm #btn_qrscan");
	//扫码确认
	var btn_qrok = $("#ajaxForm #btn_qrok");
	
	//绑定支付宝支付功能
	if(btn_alipay != null){
		btn_alipay.unbind("click").click(function(){
			var ua = navigator.userAgent.toLowerCase();
			//微信浏览器
			if (ua.indexOf('micromessenger') != -1) {
				$("#ajaxForm").attr('action',"/wechat/pay/aliPaySkip");
				$("#ajaxForm").attr('method',"get");
				$("#ajaxForm").submit();
			}else{
				$("#ajaxForm").attr('action',"/wechat/pay/aliPay");
				$("#ajaxForm").attr('method',"get");
				$("#ajaxForm").submit();
			}
		});
	}
	
	//绑定微信支付功能
	if(btn_wxpay != null){
		btn_wxpay.unbind("click").click(function(){
			var code = GetQueryString("code");
			$.ajax({
				url: '/wechat/pay/wxPay',
				type: 'post',
				dataType: 'json',
				data : {"sjid" : $("#ajaxForm #sjid").val(), "fkje":$("#ajaxForm #fy").val(), "code":code},
				success: function(data) {
					if(data && data.msg == "success"){
						var appId = data.signParam.appId; // 公众号名称,由商户传入
						var timeStamp = data.signParam.timeStamp; // 时间戳,自1970年以来的秒数
						var nonceStr = data.signParam.nonceStr; // 随机串
						var packages = data.signParam.package; //微信订单
						var signType = data.signParam.signType; // 微信签名方式
						var paySign = data.signParam.paySign // 微信签名
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
					}else{
						$.alert(data.msg);
					}
				}
			});
		});
	}
	
	//绑定扫码功能
	if(btn_qrscan != null){
		btn_qrscan.unbind("click").click(function(){
			getQRCode();
		});
	}
	
	//绑定确定按钮功能
	if(btn_qrok != null){
		btn_qrok.unbind("click").click(function(){
			getInfoAndDislayDiv();
		});
	}
}

/**
 * 截取code
 * @param name
 * @returns
 */
function GetQueryString(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]);
	return null;
}

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
			$("#ajaxForm").attr('action',"/wechat/pay/wxPayComplete");
			$("#ajaxForm").attr('method',"get");
			$("#ajaxForm").submit();
		} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
			alert("支付取消,支付取消后跳转到页面："+res.err_msg);
			// 支付取消后跳转的页面
			$("#ajaxForm").attr('action',"/wechat/pay/payBySampleCode");
			$("#ajaxForm").attr('method',"get");
			$("#ajaxForm").submit();
		} else if (res.err_msg == "get_brand_wcpay_request:fail") {
			WeixinJSBridge.call('closeWindow');
			alert("支付失败,支付失败后跳转到页面："+res.err_msg);
			// 支付失败后跳转的页面
			$("#ajaxForm").attr('action',"/wechat/pay/wxPayFaild");
			$("#ajaxForm").attr('method',"get");
			$("#ajaxForm").submit();
		} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
	});
}


function initPage(){
	if($("#ajaxForm #ybbh").val()==null||$("#ajaxForm #ybbh").val()==""){
		getQRCode();
	}
}

function getInfoAndDislayDiv(){
	var ybbh = $("#ajaxForm #ybbh").val();
	if(ybbh!=null && ybbh!= ""){
		$.ajax({
			url: '/wechat/getInspectionInfo',
			type: 'post',
			dataType: 'json',
			data : {"ybbh" : $("#ajaxForm #ybbh").val(),"sfflg":"1"},
			success: function(result) {
				if(result.sjxxDto){
					$("#ajaxForm #hzxm").html(result.sjxxDto.hzxm);
					$("#ajaxForm #yblxmc").html(result.sjxxDto.yblxmc);
					if(result.sjjcxmDtos){
						for (var int = 0; int < result.sjjcxmDtos.length; int++) {
							if(int == 0){
								$("#ajaxForm #jcxmmc").html(result.sjjcxmDtos[int].csmc);
							}else{
								$("#ajaxForm #jcxmmc").html($("#ajaxForm #jcxmmc").html()+" , "+result.sjjcxmDtos[int].csmc);
							}
						}
					}
					$("#ajaxForm #jcxm").html(result.sjxxDto.jcxm);
					$("#ajaxForm #cyrq").html(result.sjxxDto.cyrq);
					if(result.sjxxDto.fkbj == '1'){
						$("#ajaxForm #disfy").html(result.sjxxDto.fy);
					}else{
						$("#ajaxForm #disfy").html("");
					}
					$("#ajaxForm #fy").val(result.sjxxDto.fy);
					$("#ajaxForm #sjid").val(result.sjxxDto.sjid);
					jQuery("#ajaxForm #inspInfo").removeClass("hidden");
					jQuery("#ajaxForm #errorPoint").addClass("hidden");
					if(result.sjxxDto.fkbj != '1'){
						jQuery("#ajaxForm #btn_alipay").removeClass("hidden");
						jQuery("#ajaxForm #btn_wxpay").removeClass("hidden");
					}else{
						jQuery("#ajaxForm #btn_alipay").addClass("hidden");
						jQuery("#ajaxForm #btn_wxpay").addClass("hidden");
					}
				}else{
					$("#ajaxForm #hzxm").html("");
					$("#ajaxForm #yblxmc").html("");
					$("#ajaxForm #jcxm").html("");
					$("#ajaxForm #cyrq").html("");
					$("#ajaxForm #disfy").html("");
					$("#ajaxForm #fy").val("");
					$("#ajaxForm #sjid").val("");
					jQuery("#ajaxForm #inspInfo").addClass("hidden");
					jQuery("#ajaxForm #errorPoint").removeClass("hidden");
					jQuery("#ajaxForm #btn_alipay").addClass("hidden");
					jQuery("#ajaxForm #btn_wxpay").addClass("hidden");
				}
			}
		});
	}else{
		jQuery("#ajaxForm #inspInfo").addClass("hidden");
		jQuery("#ajaxForm #btn_alipay").addClass("hidden");
		jQuery("#ajaxForm #btn_wxpay").addClass("hidden");
		jQuery("#ajaxForm #errorPoint").addClass("hidden");
	}
}

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
				alert("error")
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
								//http://service.matridx.com/wechat/getUserReport?ybbh=1112
								var s_res = result.split('ybbh=')
								
								$("#ajaxForm #ybbh").val(s_res[s_res.length-1]);
								
								if(s_res[s_res.length-1]!=null && s_res[s_res.length-1]!= ""){
									getInfoAndDislayDiv();
								}
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

$(document).ready(function(){
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	jQuery("#ajaxForm #errorPoint").addClass("hidden");
});