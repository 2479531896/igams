var hbsfbzDtos=[];
var detectlist=[];
var detectsublist=[];


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
								$("#ajaxForm #errorMsg").text("");
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
											if($("#ajaxForm #sjid").val() == data.sjxxDto.sjid){
												$("#ajaxForm #ybbh").val(data.sjxxDto.ybbh);
												$("#ajaxForm #hzxm").val(data.sjxxDto.hzxm);
											}else{
												$("#ajaxForm #errorMsg").text(ybbh+"已存在，请从送检清单里查找相应的记录！");
											}
										}else{
											$("#ajaxForm #errorMsg").text("");
											$("#ajaxForm #ybbh").val(ybbh);
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
 * 保存信息方法
 * @param event
 * @returns {boolean}
 */
function editconfirm(event){
	var lsfkfs = event.currentTarget.dataset.fkfs
	if(!getWxOrNot()){
		return false;
	}
	$("#ajaxForm #errorMsg").text("");
	$("#ajaxForm .preBtn").attr("disabled", true);
	if (!checkYbbh()){
		$("#ajaxForm .preBtn").attr("disabled", false);
		return false;
	}
	if(!$("#ajaxForm #hzxm").val()){
		$.toptip('请填写患者姓名!', 'error');
		$("#ajaxForm .preBtn").attr("disabled", false);
		return false;
	}
	if(!$("#ajaxForm #jcxmids").val()){
		$.toptip('请选择检测项目!', 'error');
		$("#ajaxForm .preBtn").attr("disabled", false);
		return false;
	}

	var jcxmids = $("#ajaxForm #jcxmids").val().split(",");
	for (var i=0;i<jcxmids.length;i++){
		var isFind=false;
		for(var j=0;j<detectsublist.length;j++){
			if(jcxmids[i]== detectsublist[j].fcsid){
				isFind=true;
				break;
			}
		}
		var zxmConfirm=false;
		if(isFind){
			if($("#ajaxForm #jczxmids").val()){
				var split = $("#ajaxForm #jczxmids").val().split(",");
				for(var j=0;j<split.length;j++){
					if(jcxmids[i]==$("#"+split[j]).attr("fcsid")){
						zxmConfirm=true;
						break;
					}
				}
			}
			if(!zxmConfirm){
				$.toptip('请选择检测子项目!', 'error');
				$("#ajaxForm .preBtn").attr("disabled", false);
				return false;
			}
		}
	}

	var jcxmmc="";
	var json=[];
	$("#ajaxForm input[name='jcxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			jcxmmc+=","+$(this).attr("csmc");
			var hasSub=false;
			for (var i = 0; i < detectsublist.length; i++) {
				if($(this).val()==detectsublist[i].fcsid){
					hasSub=true;
					break;
				}
			}
			if(!hasSub){
				var sz={"jcxmid":$(this).val(),"jczxmid":"","xmglid":""};
				json.push(sz);
			}
		}
	});
	$("#ajaxForm input[name='jczxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			var sz={"jcxmid":$(this).attr("fcsid"),"jczxmid":$(this).val(),"xmglid":""};
			json.push(sz);
		}
	});

	var confirmFlag=false
	var split = $("#ajaxForm #jcxmids").val().split(",");
	for(var i=0;i<split.length;i++){
		if($("#ajaxForm #fk_fkje_"+split[i]).val()&&/^\d+(\.\d{1,2})?$/.test($("#ajaxForm #fk_fkje_"+split[i]).val())){
			confirmFlag=true;
			break;
		}
	}
	if($("#ajaxForm #jczxmids").val()){
		var split = $("#ajaxForm #jczxmids").val().split(",");
		for(var i=0;i<split.length;i++){
			if($("#ajaxForm #fk_fkje_"+split[i]).val()&&/^\d+(\.\d{1,2})?$/.test($("#ajaxForm #fk_fkje_"+split[i]).val())){
				confirmFlag=true;
				break;
			}
		}
	}

	if(!confirmFlag){
		$.toptip('请填写付款金额, 允许保留两位小数!', 'error');
		$("#ajaxForm .preBtn").attr("disabled", false);
		return false;
	}

	// 保存送检信息
	if($("#ajaxForm #sjid").val() != null && $("#ajaxForm #sjid").val() != ""){
		pay(lsfkfs);
	}else{
		$("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));
		$.ajax({
			url: '/wechat/miniprogram/saveInspectFirst',
			type: 'post',
			dataType: 'json',
			data : {"ybbh" : $("#ajaxForm #ybbh").val(),"wxid": $("#ajaxForm #wxid").val(),"hzxm": $("#ajaxForm #hzxm").val(),"db": $("#ajaxForm #db").val(),"jcxmids": $("#ajaxForm #jcxmids").val(),"jcxm":JSON.stringify(json),"jcxmmc":jcxmmc?jcxmmc.substring(1):""},
			// data : {"ybbh" : $("#ajaxForm #ybbh").val(),"wxid": $("#ajaxForm #wxid").val(),"hzxm": $("#ajaxForm #hzxm").val(),"jcxmids":$("#ajaxForm #jcxm option:selected").attr("value"),"jczxm":$("#ajaxForm #jczxm option:selected").attr("value")},
			success: function(res) {
				if(res.status == 'success'){
					$("#ajaxForm #sjid").val(res.sjxxDto.sjid);
					$("#ajaxForm #ybbh").attr("readonly","readonly");
					$("#ajaxForm #hzxm").attr("readonly","readonly");
					pay(lsfkfs);
				}else{
					if(res.message.indexOf("重复使用标本编号")!=-1){
						$.toptip('送检保存失败！重复使用标本编号，请使用新编号或从送检清单里查找相应的记录进行支付！！', 'error');
					}else{
						$.toptip("送检保存失败！"+res.message, 'error');
					}
					$("#ajaxForm .preBtn").attr("disabled", false);
				}
			}
		});
	}
}

/**
 * 支付方法
 * @param fkfs
 */
function pay(fkfs){
	var fkje=0;
	var jcxmids = $("#ajaxForm #jcxmids").val().split(",");
	for(var i=0;i<jcxmids.length;i++){
		if($("#ajaxForm #fk_fkje_"+jcxmids[i]).val()){
			fkje+=$("#ajaxForm #fk_fkje_"+jcxmids[i]).val()*1;
		}
	}
	if($("#ajaxForm #jczxmids").val()){
		var split = $("#ajaxForm #jczxmids").val().split(",");
		for(var i=0;i<split.length;i++){
			if($("#ajaxForm #fk_fkje_"+split[i]).val()){
				fkje+=$("#ajaxForm #fk_fkje_"+split[i]).val()*1;
			}
		}
	}
	var zfjcxm = [];
	for(var i=0;i<jcxmids.length;i++){
		if($("#ajaxForm #jczxmids").val()){
			var isFind=false;
			var jczxmids = $("#ajaxForm #jczxmids").val().split(",");
			for(var j=0;j<jczxmids.length;j++){
				if(jcxmids[i]==$("#"+jczxmids[j]).attr("fcsid")){
					isFind=true;
					let json= {};
					json.jcxmid = jcxmids[i];
					json.jczxmid=jczxmids[j];
					var je=0;
					if($("#ajaxForm #fk_fkje_"+jczxmids[j]).val()){
						je=$("#ajaxForm #fk_fkje_"+jczxmids[j]).val()*1;
					}
					json.je = je;
					zfjcxm.push(json);
				}
			}
			if(!isFind){
				let json= {};
				json.jcxmid = jcxmids[i];
				var je=0;
				if($("#ajaxForm #fk_fkje_"+jcxmids[i]).val()){
					je=$("#ajaxForm #fk_fkje_"+jcxmids[i]).val()*1;
				}
				json.je = je;
				zfjcxm.push(json);
			}
		}else{
			let json= {};
			json.jcxmid = jcxmids[i];
			var je=0;
			if($("#ajaxForm #fk_fkje_"+jcxmids[i]).val()){
				je=$("#ajaxForm #fk_fkje_"+jcxmids[i]).val()*1;
			}
			json.je = je;
			zfjcxm.push(json);
		}
	}
	zfjcxm = JSON.stringify(zfjcxm);
	$("#zfjcxm").val(zfjcxm);
	// 判断支付方式 scan:扫码 transfer:转账
	var _this = $("#ajaxForm input:radio[name=zffs]:checked");
	if(_this.val() == "scan"){
		$.ajax({
			url: '/wechat/pay/createOrderQRCode',
			type: 'post',
			dataType: 'json',
			data : {
				"ywid" : $("#ajaxForm #sjid").val(),
				"ybbh" : $("#ajaxForm #ybbh").val(),
				"fkje": fkje,
				"wxid": $("#ajaxForm #wxid").val(),
				"ywlx": $("#ajaxForm #ywlx").val(),
				"zfjcxm":zfjcxm,
				"wbcxdm":$("#wbcxdm").val()
			},
			success: function(data) {
				if(data.status == 'success'){
					// 二维码路径 data.qrCode
					$("#ajaxForm #path").val(data.filePath);
					//去掉患者姓名和样本编号中的空格
					var hzxm = $("#ajaxForm #hzxm").val();
					var ybbh = $("#ajaxForm #ybbh").val();
					for (;hzxm.indexOf(" ")>-1;) {
						hzxm = hzxm.replace(" ","");
					}
					for (;ybbh.indexOf(" ")>-1;) {
						ybbh = ybbh.replace(" ","");
					}
					var url="/wechat/pay/picturePreview?path="+ data.filePath +"&fkje="+fkje +"&ywid="+$("#ajaxForm #sjid").val() +"&ybbh="+ ybbh+"&hzxm="+ hzxm+"&ywlx="+$("#ajaxForm #ywlx").val();
					// $.showDialog(url,'图片预览',JPGMaterConfig);
					$("#hzxxInfo").load(url);
				}else{
					$.toptip('生成订单失败！'+data.message, 'error');
				}
				$("#ajaxForm .preBtn").attr("disabled", false);
			},
			fail:function (data) {
				$.toptip('网络错误！'+data.message, 'error');
				$("#ajaxForm .preBtn").attr("disabled", false);
			}
		});
	}else if(_this.val() == "transfer"){
		// 判断支付方式
		if(fkfs == "1"){
			// 支付宝支付 判断是否为微信环境
			var ua = navigator.userAgent.toLowerCase();
			// if (ua.indexOf('micromessenger') != -1) {
			// 	// 打开引导页
			// 	window.location.href="/wechat/pay/alipayGuide?ywid="+ $("#ajaxForm #sjid").val() +"&ybbh="+ $("#ajaxForm #ybbh").val() +"&fkje="+ fkje +"&wxid="+  $("#ajaxForm #wxid").val() +"&ywlx="+ $("#ajaxForm #ywlx").val();
			// 	$("#ajaxForm .preBtn").attr("disabled", false);
			// }else{
				// 创建支付宝native订单
				$.ajax({
					url: '/wechat/pay/alipayNative',
					type: 'post',
					dataType: 'json',
					data : {
						"ywid" : $("#ajaxForm #sjid").val(),
						"ybbh" : $("#ajaxForm #ybbh").val(),
						"fkje": fkje,
						"wxid": $("#ajaxForm #wxid").val(),
						"ywlx": $("#ajaxForm #ywlx").val(),
						"zfjcxm":zfjcxm,
						"wbcxdm":$("#wbcxdm").val()
					},
					success: function(data) {
						if(data.status == 'success'){
							// 唤起支付宝路径 data.qrCode
							window.location.href = data.qrCode;
						}else{
							$.toptip('生成订单失败失败！'+data.message, 'error');
						}
						$("#ajaxForm .preBtn").attr("disabled", false);
					},
					fail:function (data) {
						$.toptip('网络错误！'+data.message, 'error');
						$("#ajaxForm .preBtn").attr("disabled", false);
					}
				});
			// }
		}
		else if(fkfs == "2"){
			// 微信统一下单
			$.ajax({
				url: '/wechat/pay/wechatPayOrder',
				type: 'post',
				dataType: 'json',
				data : {
					"sjid" : $("#ajaxForm #sjid").val(),
					"ybbh" : $("#ajaxForm #ybbh").val(),
					"fkje": fkje,
					"wxid": $("#ajaxForm #wxid").val(),
					"ywlx": $("#ajaxForm #ywlx").val(),
					"wxzflx":"public",
					"zfjcxm":zfjcxm,
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
					}else{
						$.toptip('生成订单失败失败！'+data.message, 'error');
					}
					$("#ajaxForm .preBtn").attr("disabled", false);
				},
				fail:function (data) {
					$.toptip('网络错误！'+data.message, 'error');
					$("#ajaxForm .preBtn").attr("disabled", false);
				}
			});
		}else{
			$.toptip('未取得支付方式！', 'error');
			$("#ajaxForm .preBtn").attr("disabled", false);
		}
	}else{
		$.toptip('未取得支付方式！', 'error');
		$("#ajaxForm .preBtn").attr("disabled", false);
	}
}
// 关闭二维码
var JPGMaterConfig = {
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function (){
				$.alert("未完善数据请至“录入清单”中补充");
			}
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
			$("#ajaxForm").attr('action',"/wechat/pay/wxPayComplete");
			$("#ajaxForm").attr('method',"get");
			$("#ajaxForm").submit();
		} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
			alert("支付取消,支付取消后跳转到页面："+res.err_msg);
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

//定义计时器(二维码预览使用)
var interval = null;

function getWxOrNot (){
	var wxid = $("#wxid").val();
	if (wxid==null || wxid ==''||wxid==undefined){
		$.alert("没有获取到您的微信信息，请重新至公众号打开！")
		return false;
	}
	return true;
}
//检测项目改变事件
/*function jcxmChanged(){
	var jczxmlength = $("#ajaxForm .t_jczxm").length;
	var jcxmid = $("#ajaxForm #jcxm option:selected").attr("id");
	var dqjczxmid = $("#ajaxForm #jczxm option:selected").attr("id");
	var isjczxmin = false;
	var isjczxmhide = true;
	if (jczxmlength>0){
		for (var i = 0; i < jczxmlength; i++) {
			var jczxmid = $("#ajaxForm .t_jczxm")[i].id;
			var jczxmfcsid = $("#ajaxForm #"+jczxmid).attr("fcsid")
			if (jczxmfcsid != null && jczxmfcsid != '' && jczxmfcsid == jcxmid){
				$("#ajaxForm #"+jczxmid).attr("style","display:block;");
				if (dqjczxmid == jczxmid){
					isjczxmin = true
				}
				isjczxmhide = false;
			}else {
				$("#ajaxForm #"+jczxmid).attr("style","display:none;");
			}
		}
	}
	if (!isjczxmin){
		$("#ajaxForm #jczxm").clearFields();
		$("#ajaxForm #jczxm-1").prop("selected","selected");
	}
	$('#ajaxForm #jczxm').trigger("chosen:updated");
	if (isjczxmhide){
		$("#jczxmForm").hide();
		$("#jczxmForm").attr("disabled","disabled");
	}else {
		$("#jczxmForm").show();
		$("#jczxmForm").removeAttr("disabled");
	}
}*/
//检查检测项目、检测子项目是否已选
/*function checkJcxmAndJczxm(){
	if ($("#flag").val() == 'commonDtetection'){
		var jcxmid = $("#ajaxForm #jcxm option:selected").attr("value");
		if (jcxmid!='-1' && jcxmid!=null){
			var jczxmid = $("#ajaxForm #jczxm option:selected").attr("value");
			if ($("#ajaxForm #jczxmForm").attr("disabled")=="disabled" || (jczxmid!=-1 && jczxmid!=null)){
				return true;
			}else {
				$("#ajaxForm #errorMsg").text("请选择检测子项目！");
				$("#ajaxForm .preBtn").attr("disabled", false);
				return false;
			}
		}else {
			$("#ajaxForm #errorMsg").text("请选择检测项目！");
			$("#ajaxForm .preBtn").attr("disabled", false);
			return false;
		}
	}else {
		return true
	}
}*/

$(document).ready(function () {
	//进入页面即调用扫码(当标本编号不存在或者为空时)
	/*var ybbh = $("#ajaxForm #ybbh").val();
	if(ybbh == null || ybbh == ""){
		getQRCode();
	}*/
	// 初始化输入框
	if($("#ajaxForm #sjid").val() != null && $("#ajaxForm #sjid").val() != ""){
		// 标本编号和患者姓名改为只读
		$("#ajaxForm #ybbh").attr("readonly","readonly");
		$("#ajaxForm #hzxm").attr("readonly","readonly");
	}
	// 支付方式改变事件
	$("#ajaxForm input[type=radio][name=zffs]").change(function() {
		var _this = $("#ajaxForm input:radio[name=zffs]:checked");
		if(_this.val() == "scan"){
			$("#ajaxForm #buttonText").text("确定");
		}else{
			$("#ajaxForm #buttonText").text("支付宝");
		}
    });
	if(!getWxOrNot()){
		return false;
	}
	/*jcxmChanged();*/
});

//点选框对勾
(function($){
    $.extend({
        inputStyle:function(){
            function check(el,cl){
                $(el).each(function(){
                    $(this).parent('i').removeClass(cl);

                    var checked = $(this).prop('checked');
                    if(checked){
                        $(this).parent('i').addClass(cl);
                    }
                })
            }
            $('input[type="radio"]').on('click',function(){
                check('input[type="radio"]','radio_bg_check');
            })
            $('input[type="checkbox"]').on('click',function(){
                check('input[type="checkbox"]','checkbox_bg_check');
            })
        }

    })

})(jQuery)

function init(){
	var flag = $("#ajaxForm #flag").val();
	var param="";
	if(flag&&flag=='ResFirst'){
		param=flag;
	}
	$.ajax({
		url:'/wechat/getInspBasicData',
		type:'post',
		dataType:'json',
		async:false,
		data:{
			"flag": param
		},
		success:function(data){
			detectlist=data.detectlist;
			detectsublist = data.detectsublist;
			if(detectlist!=null&&detectlist.length>0){
				var html="";
				if(param=='ResFirst'){
					var jcxmids='';
					for (var i = 0; i < detectlist.length; i++) {
						html += '<span class="RadioStyle">';
						html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'"  csmc="'+detectlist[i].csmc+'" cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" checked onclick="changeJcxm()"  validate="{required:true}"/>';
						html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
						html +='</span>';
						jcxmids += ","+detectlist[i].csid;
					}
					if(jcxmids){
						jcxmids = jcxmids.substring(1);
					}
					$("#ajaxForm #jcxmids").val(jcxmids);
				}else{
					for (var i = 0; i < detectlist.length; i++) {
						html += '<span class="RadioStyle">';
						html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'" csmc="'+detectlist[i].csmc+'"  cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" onclick="changeJcxm()"  validate="{required:true}"/>';
						html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
						html +='</span>';
					}
				}
				$("#jcxm").empty();
				$("#jcxm").append(html);
				changePayModule();
			}
		},
		error : function(data) {
			$.toptip(JSON.stringify(date), 'error');
		}
	})
	$("#jczxmDiv").hide();
}

function changeJcxm(){
	var str='';
	var jcxmids='';
	$("#ajaxForm input[name='jcxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			str += ","+$(this).attr("cskz1");
			jcxmids += ","+$(this).val();
		}
	});
	if(jcxmids){
		jcxmids = jcxmids.substring(1);
	}
	$("#ajaxForm #jcxmids").val(jcxmids);
	var html='';
	var oncoFlag=false;
	var tNGSFlag=false;
	for (var i = 0; i < detectlist.length; i++) {
		var xzFlag=false;
		$("#ajaxForm input[name='jcxm']").each(function(){
			var flag=$(this).prop("checked");
			if(flag){
				var cskz3=$(this).attr("cskz3");
				var cskz4=$(this).attr("cskz4");
				if(cskz3==detectlist[i].cskz3&&cskz4==detectlist[i].cskz4){
					xzFlag=true;
				}
				if(cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&cskz4=='Q'){
					oncoFlag=true;
				}else if(cskz3=='IMP_REPORT_SEQ_INDEX_TEMEPLATE'&&cskz4=='O'){
					oncoFlag=true;
				}
				if(cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&cskz4=='T'){
					tNGSFlag=true;
				}else if(cskz3.indexOf('IMP_REPORT_SEQ_TNGS') > -1 && cskz4=='K'){
					tNGSFlag=true;
				}
			}
		});
		if(oncoFlag){
			if(detectlist[i].cskz3=='IMP_REPORT_SEQ_INDEX_TEMEPLATE'&&detectlist[i].cskz4=='O'){
				xzFlag=true;
			}else if(detectlist[i].cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&detectlist[i].cskz4=='Q'){
				xzFlag=true;
			}
		}
		if(tNGSFlag){
			if(detectlist[i].cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&detectlist[i].cskz4=='T'){
				xzFlag=true;
			}else if(detectlist[i].cskz3.indexOf('IMP_REPORT_SEQ_TNGS') > -1 && detectlist[i].cskz4=='K'){
				xzFlag=true;
			}
		}
		if(jcxmids.indexOf(detectlist[i].csid)!=-1){
			html += '<span class="RadioStyle">';
			html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'"  csmc="'+detectlist[i].csmc+'" cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" checked onclick="changeJcxm()"  validate="{required:true}"/>';
			html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
			html +='</span>';

		}else{
			if(!xzFlag){
				html += '<span class="RadioStyle">';
				html+='<input type="checkbox" id="'+detectlist[i].csid+'" name="jcxm" value="'+detectlist[i].csid+'"  csmc="'+detectlist[i].csmc+'"  cskz1="'+detectlist[i].cskz1+'"  cskz3="'+detectlist[i].cskz3+'"  cskz4="'+detectlist[i].cskz4+'" onclick="changeJcxm()"  validate="{required:true}"/>';
				html +='<label for="'+detectlist[i].csid+'" style="font-size: small;background-color: '+detectlist[i].cskz2+'">'+detectlist[i].csmc+'</label>';
				html +='</span>';
			}
		}
	}
	$("#jcxm").empty();
	$("#jcxm").append(html);
	if(str.indexOf("Z")!=-1){
		$("#jczxmDiv").show();
		var zxmHtml='';
		for (var i = 0; i < detectsublist.length; i++) {
			for(var j = 0; j < detectlist.length; j++){
				if(detectsublist[i].fcsid==detectlist[j].csid){
					detectsublist[i].cskz2=detectlist[j].cskz2;
					break;
				}
			}
			zxmHtml += '<span class="RadioStyle">';
			if($("#jczxmids").val().indexOf(detectsublist[i].csid)!=-1){
				zxmHtml+='<input type="checkbox" id="'+detectsublist[i].csid+'" name="jczxm" csmc="'+detectsublist[i].csmc+'" fcsid="'+detectsublist[i].fcsid+'" value="'+detectsublist[i].csid+'"  checked onclick="changeJczxm()" validate="{required:true}"/>';
			}else{
				zxmHtml+='<input type="checkbox" id="'+detectsublist[i].csid+'" name="jczxm" csmc="'+detectsublist[i].csmc+'" fcsid="'+detectsublist[i].fcsid+'" value="'+detectsublist[i].csid+'" onclick="changeJczxm()"  validate="{required:true}"/>';
			}
			zxmHtml +='<label for="'+detectsublist[i].csid+'" style="font-size: small;background-color: '+detectsublist[i].cskz2+'">'+detectsublist[i].csmc+'</label>';
			zxmHtml +='</span>';
		}
		$("#jczxm").empty();
		$("#jczxm").append(zxmHtml);
	}else {
		$("#jczxmDiv").hide();
		$("#jczxmids").val('');
	}
	changePayModule();
}

function changeJczxm(){
	var jczxmids='';
	$("#ajaxForm input[name='jczxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			jczxmids += ","+$(this).val();
		}
	});
	if(jczxmids){
		jczxmids = jczxmids.substring(1);
	}
	$("#jczxmids").val(jczxmids);
	changePayModule();
}

function changePayModule(){
	var sfbzHtml='';
	if( $("#ajaxForm #jcxmids").val()){
		var jcxmids = $("#ajaxForm #jcxmids").val().split(",");
		for (var i=0;i<jcxmids.length;i++){
			if($("#ajaxForm #jczxmids").val()){
				var split = $("#ajaxForm #jczxmids").val().split(",");
				var zxmFind=false;
				for(var j=0;j<split.length;j++){
					if(jcxmids[i]== $("#"+split[j]).attr("fcsid")){
						zxmFind=true;
						sfbzHtml += '<div class="weui-cell">' +
										'<div class="weui-cell__bd">' +
											'<div class="weui-cell__desc">'+$("#"+jcxmids[i]).attr("csmc")+'-'+$("#"+split[j]).attr("csmc")+'</div>';
						// var isFind=false;
						// var sfbz;
						// if(hbsfbzDtos!=null&&hbsfbzDtos.length>0){
						// 	for(var x=0;x<hbsfbzDtos.length;x++){
						// 		if(hbsfbzDtos[x].xm==jcxmids[i]&&hbsfbzDtos[x].zxm==split[j]){
						// 			isFind=true;
						// 			sfbz=hbsfbzDtos[x].sfbz;
						// 			break;
						// 		}
						// 	}
						// }
						// if(isFind){
						// 	sfbzHtml += '<input id="fk_fkje_'+split[j]+'" name="fk_fkje_'+split[j]+'" class="weui-input" type="number" placeholder="请填写付款金额(应付'+sfbz+')"/></div></div>';
						// }else{
							sfbzHtml += '<input id="fk_fkje_'+split[j]+'" name="fk_fkje_'+split[j]+'" class="weui-input" type="number" placeholder="请填写付款金额"/></div></div>';
						// }
					}
				}
				if(!zxmFind){
					sfbzHtml += '<div class="weui-cell">' +
									'<div class="weui-cell__bd">' +
										'<div class="weui-cell__desc">'+$("#"+jcxmids[i]).attr("csmc")+'</div>';
					// var isFind=false;
					// var sfbz;
					// if(hbsfbzDtos!=null&&hbsfbzDtos.length>0){
					// 	for(var x=0;x<hbsfbzDtos.length;x++){
					// 		if(hbsfbzDtos[x].xm==jcxmids[i]){
					// 			isFind=true;
					// 			sfbz=hbsfbzDtos[x].sfbz;
					// 			break;
					// 		}
					// 	}
					// }
					// if(isFind){
					// 	sfbzHtml += '<input id="fk_fkje_'+jcxmids[i]+'" name="fk_fkje_'+jcxmids[i]+'" class="weui-input" type="number" placeholder="请填写付款金额(应付'+sfbz+')"/></div></div>';
					// }else{
						sfbzHtml += '<input id="fk_fkje_'+jcxmids[i]+'" name="fk_fkje_'+jcxmids[i]+'" class="weui-input" type="number" placeholder="请填写付款金额"/></div></div>';
					// }
				}
			}else{
				var notchooose=false;
				for(var j=0;j<detectsublist.length;j++){
					if(jcxmids[i]== detectsublist[j].fcsid){
						notchooose=true;
					}
				}
				if(!notchooose){
					sfbzHtml += '<div class="weui-cell">' +
									'<div class="weui-cell__bd">' +
										'<div class="weui-cell__desc">'+$("#"+jcxmids[i]).attr("csmc")+'</div>';
					// var isFind=false;
					// var sfbz;
					// if(hbsfbzDtos!=null&&hbsfbzDtos.length>0){
					// 	for(var x=0;x<hbsfbzDtos.length;x++){
					// 		if(hbsfbzDtos[x].xm==jcxmids[i]){
					// 			isFind=true;
					// 			sfbz=hbsfbzDtos[x].sfbz;
					// 			break;
					// 		}
					// 	}
					// }
					// if(isFind){
					// 	sfbzHtml += '<input id="fk_fkje_'+jcxmids[i]+'" name="fk_fkje_'+jcxmids[i]+'" class="weui-input" type="number" placeholder="请填写付款金额(应付'+sfbz+')"></div></div>';
					// }else{
						sfbzHtml += '<input id="fk_fkje_'+jcxmids[i]+'" name="fk_fkje_'+jcxmids[i]+'" class="weui-input" type="number" placeholder="请填写付款金额"></div></div>';
					// }
				}
			}
		}
	}
	if(sfbzHtml){
		$("#sfbzDiv").empty();
		$("#sfbzDiv").append(sfbzHtml);
		$("#sfbzDiv").show();
	}else{
		$("#sfbzDiv").empty();
		$("#sfbzDiv").hide();
	}

}

//调用
$(function(){
    $.inputStyle();
	var db = $("#ajaxForm #db").val();
	if(db){
		$.ajax({
			url: '/wechat/getChargingStandards',
			type: 'post',
			async:false,
			data: {
				"hbmc": db
			},
			dataType: 'json',
			success: function(result) {
				if(result.hbsfbzDtos != null && result.hbsfbzDtos.length > 0){
					hbsfbzDtos=result.hbsfbzDtos;
				}else{
					hbsfbzDtos=[];
				}
			}
		});
	}
	init();
})

/**
 * 刷新合作伙伴
 * @returns {boolean}
 */
function getChargingStandards(){
	var db = $("#ajaxForm #db").val();
	if(!db){
		hbsfbzDtos=[];
		return false;
	}
	$.ajax({
		url: '/wechat/getChargingStandards',
		type: 'post',
		async:false,
		data: {
			"hbmc": db
		},
		dataType: 'json',
		success: function(result) {
			if(result.hbsfbzDtos != null && result.hbsfbzDtos.length > 0){
				hbsfbzDtos=result.hbsfbzDtos;
			}else{
				hbsfbzDtos=[];
			}
			changePayModule();
		}
	});

}

function checkYbbh(){
	let ybbh = $("#ajaxForm #ybbh").val();
	if(!ybbh){
		$.toptip("请填写样本编号!", 'error');
		return false;
	}
	//若样本编号中包含除英文、数字以及-以外的字符，则提示错误
	if(!/^[a-zA-Z0-9-]+$/.test(ybbh)){
		$.toptip("样本编号只能包含英文、数字以及-!", 'error');
		return false;
	}
	return true;
}
