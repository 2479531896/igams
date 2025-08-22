//初始化页面数据
function initPage(){
	alert("页面初始化！");
	//初始化微信信息
	$.ajax({
		url: '/wechat/getJsApiInfo',
		type: 'post',
		data: {
			"url":location.href.split('#')[0],
			"wbcxdm":$("#wbcxdm").val()
		},
		dataType: 'json',
		success: function(result) {
			alert("getJsApiInfo成功！");
			//注册信息
			wx.config({
				debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId: result.appid, // 必填，公众号的唯一标识
				timestamp: result.timestamp, // 必填，生成签名的时间戳
				nonceStr: result.noncestr, // 必填，生成签名的随机串
				signature: result.sign, // 必填，签名，见附录1
				jsApiList: ['checkJsApi','updateAppMessageShareData']
				// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			wx.error(function(res) {
				console.log(res);
			});
			//config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
			wx.ready(function() {
				wx.checkJsApi({
					jsApiList: ['updateAppMessageShareData'],
					success: function(res) {
						alert("checkJsApi成功！");
						wx.updateAppMessageShareData({
						    title: '标题', // 分享标题
						    desc: '描述', // 分享描述
						    link: $("#url").val(), // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						    imgUrl: '', // 分享图标
						    success: function () {
						    	// 设置成功
						    	alert("updateAppMessageShareData设置成功！");
						    }
						});
					}
				});
			});
		}
	});
}

$(document).ready(function(){
	//初始化页面数据
	initPage();
});