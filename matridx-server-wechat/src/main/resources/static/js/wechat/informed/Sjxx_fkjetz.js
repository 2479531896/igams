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
								//http://service.matridx.com/wechat/getUserReport?ybbh=1112
								var s_res = result.split('ybbh=')
								
								$("#ajaxForm #ybbh").val(s_res[s_res.length-1]);
								
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

function editconfirm(){
	$.ajax({
		url: '/wechat/inspection/amountSaveEdit',
		type: 'post',
		dataType: 'json',
		data : {"ybbh" : $("#ajaxForm #ybbh").val(),"hzxm":$("#ajaxForm #hzxm").text(),"fkje": $("#ajaxForm #fkje").val(),"wxid": $("#ajaxForm #wxid").val()},
		success: function(data) {
			if(data.status == 'success'){
				$("#ajaxForm #yfkje").text(data.sjxxDto.fkje);
				$("#ajaxForm #fkje").attr("placeholder",data.sjxxDto.fkje);
			}else{
				$.error(data.message);
			}
		}
	});
}

$(document).ready(function () { 
	var ybbh=document.getElementById("ybbh").value;
	if(ybbh==null || ybbh==""){
		getQRCode();
	}
});