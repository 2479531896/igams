//事件绑定
function btnBind() {
	// 支付宝付款按钮
	$("#ajaxForm #btn_alipay").unbind("click").click(function(){
		$("#ajaxForm").attr('action',"/wechat/pay/aliPay");
		$("#ajaxForm").attr('method',"get");
		$("#ajaxForm").submit();
	});
	// 微信付款按钮
	$("#ajaxForm #btn_wxpay").unbind("click").click(function(){
		alert("fkje:"+$("#ajaxForm #fy").val())
		var ua = navigator.userAgent.toLowerCase();
		//判断是否是微信浏览器
		if(ua.indexOf('micromessenger') != -1){
			alert("准备调用JSAPI支付")
			//获取网页默认授权
			var appid = $("#appid").val();
			var applicationurl = $("#applicationurl").val()+"/wechat/pay/wxCodeView";
			var ybbh = $("#ajaxForm #ybbh").val();
			var fkje = $("#ajaxForm #fy").val();
			var state = ybbh+","+fkje;
			alert(appid+"...."+applicationurl+"..."+ybbh)
			window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid='+ appid +'&redirect_uri='+ applicationurl +'&response_type=code&scope=snsapi_base&state='+state+'#wechat_redirect';
		}else{
			alert("准备调用H5支付")
			//调用H5支付
			$.ajax({
				url: '/wechat/pay/wxH5Pay',
				type: 'post',
				dataType: 'json',
				data : {"fkje":$("#ajaxForm #fy").val(),"ybbh":$("#ajaxForm #ybbh").val()},
				success: function(data) {
					alert("统一下单完成："+data.msg)
					if(data && data.msg == "success"){
						alert("准备跳转微信支付： data.mweb_url："+data.mweb_url)
						var mweb_url = data.mweb_url;// 获取支付跳转路径
						window.location.href=mweb_url;
						alert("支付完成")
					}else{
						$.alert(data.msg);
					}
				}
			});
		}
	});
}

function initPage(){
	
}

$(document).ready(function(){
	// 绑定事件
	btnBind();
	// 初始化页面数据
	initPage();
});
