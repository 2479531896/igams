/**
 * 微信JSAPI支付
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


$(document).ready(function(){
	var code = $("#code").val();
	var ybbh = $("#ybbh").val();
	var fkje = $("#fkje").val();
	var state = $("#state").val();
	alert("code:"+code+" fkje:"+fkje+" ybbh:"+ybbh+" state"+state);
	$.ajax({
		url: '/wechat/pay/wxPay',
		type: 'post',
		dataType: 'json',
		data : {"ybbh" : ybbh, "fkje" : fkje, "code" : code},
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
