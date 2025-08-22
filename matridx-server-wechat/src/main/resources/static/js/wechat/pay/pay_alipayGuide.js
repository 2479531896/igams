/**
 * 初始化页面
 * @returns
 */
function initPage(){
	// 判断是否为微信环境
	var ua = navigator.userAgent.toLowerCase();
	if (ua.indexOf('micromessenger') != -1) {
		// 微信环境，显示引导页
		
	}else{
		var maskdiv = '<div id="divmask" style="position:absolute;width:100%;height:100%;background-color:black;opacity: 0.8;top: 0px;left:0px;vertical-align:middle;text-align:center;">';
		maskdiv += '<span id="message" style="color:white;">订单生成中，请稍等。。。</sapn>';
		maskdiv += '</div>';
		$("#mask").append(maskdiv);
		// 创建支付宝native订单
		$.ajax({
			url: '/wechat/pay/alipayNative',
			type: 'post',
			dataType: 'json',
			data : {"ywid" : $("#ywid").val(), "ybbh" : $("#ybbh").val(),"fkje": $("#fkje").val(),"wxid": $("#wxid").val(),"ddid": $("#ddid").val(),"ywlx": $("#ywlx").val(),zfjcxm:$("#zfjcxm").val(),"wbcxdm": $("#wbcxdm").val()},
			success: function(data) {
				if(data.status == 'success'){
					$("#divmask").remove("div");
					// 唤起支付宝路径 data.qrCode
					window.location.href = data.qrCode;
				}else{
					$("#divmask").remove("div");
					var maskdiv = '<div id="divmask" style="position:absolute;width:100%;height:100%;background-color:black;opacity: 0.8;top: 0px;left:0px;vertical-align:middle;text-align:center;">';
					maskdiv += '<span id="message" style="color:white;">生成订单失败，请返回微信重试！'+data.message+'</sapn>';
					maskdiv += '</div>';
					$("#mask").append(maskdiv);
				}
			}
		});
	}
}

$(document).ready(function(){
	// 初始化页面数据
	initPage();
});
