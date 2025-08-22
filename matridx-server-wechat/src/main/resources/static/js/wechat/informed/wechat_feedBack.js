function searchResult(){
	var lrry=$("#lrry").val();
	var sign=$("#sign").val();
	var entire=$("#searchInput").val();
	$.ajax({
		url:'/wechat/feedback/getNofeefback',
		type:'post',
		data:{"lrry":lrry,"sign":sign,"entire":entire},
		success:function(data){
			$("#sjxxList").html("");
			var html = "";
			if(data.sjxxList!=null){
				for (var i = 0; i < data.sjxxList.length; i++) {
					html += "<div class=\"weui-panel\" style=\"border-radius: 10px;margin: 8px;\" data-sjid=\""+data.sjxxList[i].sjid+"\" data-sign=\""+data.sjxxList[i].sign+"\" data-ybbh=\""+data.sjxxList[i].ybbh+"\"  onclick=\"viewInfo('"+data.sjxxList[i].sjid+"','"+data.sjxxList[i].ybbh+"','"+data.sjxxList[i].sign+"')\">" +
								"<div class=\"weui-cell__bd\" style=\"border-radius: 10px;transform: translate3d(0px, 0px, 0px);\">" +
								"<div class=\"weui-form-preview\" style=\"border-radius: 10px;\">" +
									"<div style=\"height: 0.1rem;\"></div>" +
									"<div class=\"weui-form-preview__bd\" style=\"font-size: 14px;font-weight: bolder;padding: 0px 10px\">" +
										"<div style=\"display: flex\">" +
											"<div class=\"weui-form-preview__item\" style=\"flex: 9\">" +
												"<label class=\"weui-form-preview__label\" style=\"margin-right: 2px;\">姓名：</label>" +
												"<span class=\"weui-form-preview__value\" style=\"text-align: left;word-break: keep-all;\">"+(data.sjxxList[i].hzxm?data.sjxxList[i].hzxm:"")+"</span>" +
											"</div>" +
											"<div class=\"weui-form-preview__item\" style=\"flex: 3\" onclick=\"feedback('"+data.sjxxList[i].ybbh+"')\">" +
												"<label class=\"weui-form-preview__label weui-icon-edit\" style=\"margin-right: 2px;color: #00AFEC;\"></label>" +
												"<label class=\"weui-form-preview__label\" style=\"margin-right: 2px;color: #00AFEC;\">反馈</label>" +
											"</div>" +
										"</div>" +
										"<div style=\"display: flex\">" +
											"<div class=\"weui-form-preview__item\" style=\"flex: 3\">" +
												"<label class=\"weui-form-preview__label\" style=\"margin-right: 2px;\">类型：</label>" +
												"<span class=\"weui-form-preview__value\" style=\"text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;\">"+(data.sjxxList[i].yblxmc?data.sjxxList[i].yblxmc:"")+"</span>" +
											"</div>" +
											"<div class=\"weui-form-preview__item\" style=\"flex: 4\">" +
												"<label class=\"weui-form-preview__label\" style=\"margin-right: 2px;\">接收日期：</label>" +
												"<span class=\"weui-form-preview__value\" style=\"text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;\">"+(data.sjxxList[i].fjsrq?data.sjxxList[i].fjsrq:"")+"</span>" +
											"</div>" +
										"</div>" +
									"</div>" +
								"</div>" +
							"</div>" +
						"</div>"
				}
			}
			$("#sjxxList").append(html)
		}
	});
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
									searchResult();
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


function feedback(ybbh){
    event.stopPropagation();
    window.location.href="/wechat/inspection/feedBack?ybbh="+ybbh;
}
function viewInfo(sjid,ybbh,sign){
	if(sjid!=null){
		window.location.href="/wechat/getUserInfoView?ybbh="+ybbh+"&sign="+sign+"&sjid="+sjid;
	}
}

$(function(){
	searchResult()
})