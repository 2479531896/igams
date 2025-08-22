
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
								var s_res = result.split('ybbh=')
								
								$("#ajaxForm #ybbh").val(s_res[s_res.length-1]);
								getInspectionInfo();
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

function getInspectionInfo(){
	if($("#ajaxForm #ybbh").val() =="")
		return;
	$.ajax({
		url: '/wechat/getInspectionInfo',
		type: 'post',
		dataType: 'json',
		data : {"ybbh" : $("#ajaxForm #ybbh").val()},
		success: function(result) {
			if(result.sjxxDto){
				$("#ajaxForm #hzxm").html(result.sjxxDto.hzxm);
				$("#ajaxForm #xb").html(result.sjxxDto.xbmc);
				$("#ajaxForm #nl").html(result.sjxxDto.nl);
				$("#ajaxForm #dh").html(result.sjxxDto.dh);
				$("#ajaxForm #sjdw").html(result.sjxxDto.sjdw);
				$("#ajaxForm #ks").html(result.sjxxDto.ksmc);
				if(result.sjxxDto.qtks){
					$("#ajaxForm #ks").html(result.sjxxDto.ksmc+" - "+result.sjxxDto.qtks);
				}
				$("#ajaxForm #sjys").html(result.sjxxDto.sjys);
				$("#ajaxForm #ysdh").html(result.sjxxDto.ysdh);
				$("#ajaxForm #db").html(result.sjxxDto.db);
				$("#ajaxForm #dbm").html(result.sjxxDto.dbm);
				$("#ajaxForm #yblxmc").html(result.sjxxDto.yblxmc);
				$("#ajaxForm #cyrq").html(result.sjxxDto.cyrq);
				$("#ajaxForm #bz").html(result.sjxxDto.bz);
				if(result.sjjcxmDtos){
					for (var int = 0; int < result.sjjcxmDtos.length; int++) {
						if(int == 0){
							$("#ajaxForm #jcxmmc").html(result.sjjcxmDtos[int].csmc);
						}else{
							$("#ajaxForm #jcxmmc").html($("#ajaxForm #jcxmmc").html()+" , "+result.sjjcxmDtos[int].csmc);
						}
					}
				}
				$("#ajaxForm #sjid").val(result.sjxxDto.sjid);
			}
		}
	});
}

function confirm(){
	if($("#ajaxForm #ybbh").val()=="")
		return;
	if($("#ajaxForm #nbbm").val()=="")
		return;
	$.ajax({
		url: '/wechat/sampleConfirm',
		type: 'post',
		dataType: 'json',
		data : {"ybbh" : $("#ajaxForm #ybbh").val(),"nbbm" : $("#ajaxForm #nbbm").val(),"sjid" : $("#ajaxForm #sjid").val(),"bz" : $("#ajaxForm #bz").val(),"sign":$("#ajaxForm #sign").val()},
		success: function(result) {
			if(result.status == "success"){
				$.success(result.message);
				window.opener=null;
				window.open('','_self');
				window.close();
			}else{
				$.alert(result.message);
			}
		}
	});
}

//事件绑定
function btnBind() {
	//设置回车按下跳转
	$('input[type="text"]').keydown(function(event) {          //jquery获取text框
		if(event.which == 13 )          //回车按键值 13
			getInspectionInfo();
	})

}

function initPage(){
}

$(document).ready(function(){
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});