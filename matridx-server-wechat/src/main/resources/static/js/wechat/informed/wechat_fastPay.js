/**
 * 扫码事件
 * @returns
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
								$("#fastPay_ajaxForm #errorMsg").text("");
								// 当needResult 为 1 时，扫码返回的结果
								var result = res.resultStr;
								//http://service.matridx.com/wechat/getUserReport?ybbh=1112
								var s_res = result.split('ybbh=')
								var ybbh = s_res[s_res.length-1];
								$.ajax({
									url: '/wechat/pay/getInspectionInfo',
									type: 'post',
									data: {"ybbh": ybbh},
									dataType: 'json',
									success: function(data) {
										if(data.status == "success"){
											if($("#fastPay_ajaxForm #sjid").val() == data.sjxxDto.sjid){
												$("#fastPay_ajaxForm #ybbh").val(data.sjxxDto.ybbh);
												$("#fastPay_ajaxForm #hzxm").val(data.sjxxDto.hzxm);
												$("#fastPay_ajaxForm #fkje").val("");
											}else{
												$("#fastPay_ajaxForm #errorMsg").text(ybbh+"已存在，请从送检清单里查找相应的记录！");
											}
										}else{
											$("#fastPay_ajaxForm #errorMsg").text("");
											$("#fastPay_ajaxForm #ybbh").val(ybbh);
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

function editconfirm(event){
	$("#fastPay_ajaxForm #errorMsg").text("");
	$("#fastPay_ajaxForm .preBtn").attr("disabled", true);
	if(!$("#fastPay_ajaxForm #ybbh").val()){
		$("#fastPay_ajaxForm #errorMsg").text("请填写标本编号！");
		$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
		return false;
	}
	if(!$("#fastPay_ajaxForm #hzxm").val()){
		$("#fastPay_ajaxForm #errorMsg").text("请填写患者姓名！");
		$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
		return false;
	}
	if(!$("#fastPay_ajaxForm #fkje").val() || !/^\d+(\.\d{1,2})?$/.test($("#fastPay_ajaxForm #fkje").val()) || $("#fastPay_ajaxForm #fkje").val() <= 0){
		$("#fastPay_ajaxForm #errorMsg").text("请填写付款金额, 允许保留两位小数！");
		$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
		return false;
	}
	// 保存送检信息
	if($("#fastPay_ajaxForm #sjid").val() != null && $("#fastPay_ajaxForm #sjid").val() != ""){
		pay(event);
	}else{
		$("#fastPay_ajaxForm #ybbh").val($("#fastPay_ajaxForm #ybbh").val().replace(/\s+/g,""));
		$.ajax({
			url: '/wechat/miniprogram/saveInspectFirst',
			type: 'post',
			dataType: 'json',
			data : {"ybbh" : $("#fastPay_ajaxForm #ybbh").val(),"wxid": $("#wxid").val(),"hzxm": $("#fastPay_ajaxForm #hzxm").val()},
			success: function(res) {
				if(res.status == 'success'){
					$("#fastPay_ajaxForm #sjid").val(res.sjxxDto.sjid);
					$("#fastPay_ajaxForm #ybbh").attr("readonly","readonly");
					$("#fastPay_ajaxForm #hzxm").attr("readonly","readonly");
					pay(event);
				}else{
					if(res.message.indexOf("重复使用标本编号")!=-1){
						$("#fastPay_ajaxForm #errorMsg").text("送检保存失败！重复使用标本编号，请使用新编号或从送检清单里查找相应的记录进行支付！！");
					}else{
						$("#fastPay_ajaxForm #errorMsg").text("送检保存失败！"+res.message);
					}
					$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
				}
			}
		});
	}
}
// 支付方法
function pay(event){
	// 判断支付方式 scan:扫码 transfer:转账
	var _this = $("#fastPay_ajaxForm input:radio[name=zffs]:checked");
	if(_this.val() == "scan"){
		$.ajax({
			url: '/wechat/pay/createOrderQRCode',
			type: 'post',
			dataType: 'json',
			data : {"ywid" : $("#fastPay_ajaxForm #sjid").val(), "ybbh" : $("#fastPay_ajaxForm #ybbh").val(),"fkje": $("#fastPay_ajaxForm #fkje").val(),"wxid": $("#wxid").val(),"ywlx": $("#fastPay_ajaxForm #ywlx").val()},
			success: function(data) {
				if(data.status == 'success'){
					// 二维码路径 data.qrCode
					$("#fastPay_ajaxForm #path").val(data.filePath);
					var url="/wechat/pay/picturePreview?path="+ data.filePath +"&fkje="+$("#fastPay_ajaxForm #fkje").val() +"&ywid="+$("#fastPay_ajaxForm #sjid").val() +"&ybbh="+ $("#fastPay_ajaxForm #ybbh").val()+"&ywlx="+$("#fastPay_ajaxForm #ywlx").val();
					$.showDialog(url,'图片预览',JPGMaterConfig);
				}else{
					$("#fastPay_ajaxForm #errorMsg").text("生成订单失败！"+data.message);
				}
				$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
			}
		});
	}else if(_this.val() == "transfer"){
		// 判断支付方式
		if(event.currentTarget.dataset.fkfs == "1"){
			// 支付宝支付 判断是否为微信环境
			var ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') != -1) {
				// 打开引导页
				window.location.href="/wechat/pay/alipayGuide?ywid="+ $("#fastPay_ajaxForm #sjid").val() +"&ybbh="+ $("#fastPay_ajaxForm #ybbh").val() +"&fkje="+ $("#fastPay_ajaxForm #fkje").val() +"&wxid="+  $("#wxid").val() +"&ywlx="+ $("#fastPay_ajaxForm #ywlx").val();
				$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
			}else{
				// 创建支付宝native订单
				$.ajax({
					url: '/wechat/pay/alipayNative',
					type: 'post',
					dataType: 'json',
					data : {"ywid" : $("#fastPay_ajaxForm #sjid").val(), "ybbh" : $("#fastPay_ajaxForm #ybbh").val(),"fkje": $("#fastPay_ajaxForm #fkje").val(),"wxid": $("#wxid").val(),"ywlx": $("#fastPay_ajaxForm #ywlx").val()},
					success: function(data) {
						if(data.status == 'success'){
							// 唤起支付宝路径 data.qrCode
							window.location.href = data.qrCode;
						}else{
							$("#fastPay_ajaxForm #errorMsg").text("生成订单失败失败！"+data.message);
						}
						$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
					}
				});
			}
		}
		else if(event.currentTarget.dataset.fkfs == "2"){
			// 微信统一下单
			$.ajax({
				url: '/wechat/pay/wechatPayOrder',
				type: 'post',
				dataType: 'json',
				data : {"sjid" : $("#fastPay_ajaxForm #sjid").val(), "ybbh" : $("#fastPay_ajaxForm #ybbh").val(),"fkje": $("#fastPay_ajaxForm #fkje").val(),"wxid": $("#wxid").val(),"ywlx": $("#fastPay_ajaxForm #ywlx").val(),"wxzflx":"public"},
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
					}else{
						$("#fastPay_ajaxForm #errorMsg").text("生成订单失败失败！"+data.message);
					}
					$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
				}
			});
		}else{
			$("#fastPay_ajaxForm #errorMsg").text("未取得支付方式！");
			$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
		}
	}else{
		$("#fastPay_ajaxForm #errorMsg").text("未取得支付方式！");
		$("#fastPay_ajaxForm .preBtn").attr("disabled", false);
	}
}
// 关闭二维码
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
			$("#fastPay_ajaxForm").attr('action',"/wechat/pay/wxPayComplete");
			$("#fastPay_ajaxForm").attr('method',"get");
			$("#fastPay_ajaxForm").submit();
		} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
			alert("支付取消,支付取消后跳转到页面："+res.err_msg);
		} else if (res.err_msg == "get_brand_wcpay_request:fail") {
			WeixinJSBridge.call('closeWindow');
			alert("支付失败,支付失败后跳转到页面："+res.err_msg);
			// 支付失败后跳转的页面
			$("#fastPay_ajaxForm").attr('action',"/wechat/pay/wxPayFaild");
			$("#fastPay_ajaxForm").attr('method',"get");
			$("#fastPay_ajaxForm").submit();
		} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
	});
}

//定义计时器(二维码预览使用)
var interval = null;

$(document).ready(function () {
	var ybbh = $("#fastPay_ajaxForm #ybbh").val();
	if(ybbh == null || ybbh == ""){
		getQRCode();
	}
	// 初始化输入框
	if($("#fastPay_ajaxForm #sjid").val() != null && $("#fastPay_ajaxForm #sjid").val() != ""){
		// 标本编号和患者姓名改为只读
		$("#fastPay_ajaxForm #ybbh").attr("readonly","readonly");
		$("#fastPay_ajaxForm #hzxm").attr("readonly","readonly");
	}
	// 扫码支付初始化
	$("#fastPay_ajaxForm #transferButton").hide();
	// 支付方式改变事件
	$("#fastPay_ajaxForm input[type=radio][name=zffs]").change(function() {
		var _this = $("#fastPay_ajaxForm input:radio[name=zffs]:checked");
		if(_this.val() == "scan"){
			$("#fastPay_ajaxForm #scanButton").show();
			$("#fastPay_ajaxForm #transferButton").hide();
		}else{
			$("#fastPay_ajaxForm #scanButton").hide();
			$("#fastPay_ajaxForm #transferButton").show();
		}
    });
});