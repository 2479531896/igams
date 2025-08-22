var yblxList;
var xzYblxList;
var qtyblxid;
//定义计时器(二维码预览使用)
var interval = null;
var yblxdm="";
/**
 * 保存事件
 * @returns {boolean}
 */
function complete(){
	$("#ajaxForm #btn_complete").attr("disabled","true");
	if(!getWxOrNot()){
		return false;
	}
	var actionFlag = $("#ajaxForm #actionFlag").val();//动作标记，1：完善，其他：新增、修改
	//若为新增、修改，检验录单必填项
	if (actionFlag != "1"){
		//校验标本类型，录单必填项
		if (!$("#yblx").val()) {
			$.toptip('请选择标本类型!', 'error');
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			return false;
		}
		//校验其他标本类型，录单必填项
		if(!$("#ajaxForm #qt").is(":hidden")){
			if (!$("#yblxmc").val()) {
				$.toptip('请填写报告显示标本!', 'error');
				$("#ajaxForm #btn_complete").removeAttr("disabled");
				return false;
			}
		}
		//校验采样日期，录单必填项
		if (!$("#cyrq").val()) {
			$.toptip('请选择采样日期!', 'error');
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			return false;
		}
	}
	//若为完善，检验完善必填项
	if (actionFlag == "1"){
		//校验标本体积，完善必填项
		if (!$("#ybtj").val()) {
			$.toptip('请填写标本体积!', 'error');
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			return false;
		}
		//校验前期诊断，录单必填项
		if (!$("#qqzd").val()) {
			$.toptip('请填写前期诊断!', 'error');
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			return false;
		}
		//校验临床症状，录单必填项
		if (!$("#lczz").val()) {
			$.toptip('请填写临床症状!', 'error');
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			return false;
		}
	}
	dealSfwsFlag();
	$("#ajaxForm #newflg").val(checkIsRequired()?"1":"0");//完成标记，1：完成，0：未完成
	showloading("正在保存...")
	$.ajax({
		url: '/wechat/addSaveConsentMap',
		type: 'post',
		dataType: 'json',
		data : $('#ajaxForm').serialize(),
		success: function(result) {
			hideloading()
			if(result.status == "success"){
				$.modal({
					text: result.message,
					buttons: [
						{ text: "进入清单", onClick: function(){
								window.location.replace('/wechat/inspPerfectReport?wxid=' + $("#wxid").val()+"&wbcxdm="+$("#wbcxdm").val());
						}},
						{ text: "再录一单", onClick: function(){
							window.location.replace('/wechat/inspReport?wxid=' + $("#wxid").val()+"&wbcxdm="+$("#wbcxdm").val());
						}},
					]
				});
			}else{
				$("#ajaxForm #btn_complete").removeAttr("disabled");
				$.toptip(result.message, 'error');
			}
		},
		error: function(result) {
			hideloading();
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			$.toptip(JSON.stringify(result), 'error');
		}
	});
}

/**
 * 上一步
 */
function preStep(){
	$("#ajaxForm #btn_prestep").attr("disabled","true");
	if(!getWxOrNot()){
		return false;
	}
	dealSfwsFlag();
	$("#ajaxForm #newflg").val(checkIsRequired()?"1":"0");//完成标记，1：完成，0：未完成
	showloading("正在保存...");
	$.ajax({
		url: '/wechat/addSaveConsentMap',
		type: 'post',
		dataType: 'json',
		data : $('#ajaxForm').serialize(),
		success: function(result) {
			hideloading();
			if(result.status == "success"){
				window.location.replace('/wechat/inspection/modInspection?wxid=' + $("#wxid").val()+"&sjid="+$("#ajaxForm #sjid").val()+"&actionFlag="+($("#ajaxForm #actionFlag").val()!=null?$("#ajaxForm #actionFlag").val():''));
			}else{
				$("#ajaxForm #btn_complete").removeAttr("disabled");
				$("#ajaxForm #btn_prestep").removeAttr("disabled");
				$.toptip(result.message, 'error');
			}
		},
		error: function(result) {
			hideloading();
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			$("#ajaxForm #btn_prestep").removeAttr("disabled");
			$.toptip(JSON.stringify(result), 'error');
		}
	});
	hideloading();
}

/**
 * 转账、扫码付款
 * @param event
 * @returns
 */
function payModal(sjid,ybbh,hzxm,flag){
	//打开付款前，先对信息进行保存
	$("#ajaxForm #btn_complete").attr("disabled","true");
	$("#ajaxForm #btn_prestep").attr("disabled","true");
	if(!getWxOrNot()){
		return false;
	}
	dealSfwsFlag();
	$("#ajaxForm #newflg").val(checkIsRequired()?"1":"0");//完成标记，1：完成，0：未完成
	//执行上一步中的保存操作
	showloading("正在保存...")
	$.ajax({
		url: '/wechat/addSaveConsentMap',
		type: 'post',
		dataType: 'json',
		data : $('#ajaxForm').serialize(),
		success: function(result) {
			hideloading();
			if(result.status == "success"){
				$.ajax({
					url: '/wechat/inspection/getDetectionPayInfo',
					type: 'post',
					dataType: 'json',
					data : {"sjid" : sjid},
					success: function(data) {
						if(data!=null && data.length >0){
							var htmltext = '<div class="weui-media-box_appmsg" style="height: 35px">' +
								'<div style="flex: 2;text-align: left">标本编号</div>' +
								'<div style="flex: 3;text-align: left"><input disabled id="fk_ybbh" name="fk_ybbh" class="weui-input" style="color: black;font-weight: bold;" value="'+ybbh+'"></div>' +
								'<input type="hidden" id="fk_sjid" name="fk_sjid" value="\''+sjid+'\'">' +
								'<div style="flex: 1"><button type="button" class="weui-icon-scan" style="color: #00AFEC" onClick="getQRCode()" className="weui-btn weui-btn_mini weui-btn_primary weui-wa-hotarea" style="font-size: smaller;padding: 0 5px;">扫码</button></div>' +
								'</div>' +
								'<div class="weui-media-box_appmsg" style="height: 35px">' +
								'<div style="flex: 2;text-align: left">患者姓名</div>' +
								'<div style="flex: 4;text-align: left"><input disabled id="fk_hzxm" name="fk_hzxm" class="weui-input" style="color: black;font-weight: bold;" value="'+hzxm+'"></div>' +
								'</div>'
							for (let i = 0; i < data.length; i++) {
								htmltext += '<div class="weui-media-box_appmsg" style="border-top: 2px solid grey;height: 35px"><div style="font-weight: bolder;text-align: left">'+(data[i].jczxmmc?data[i].jczxmmc:data[i].jcxmmc)+'</div></div>'
								htmltext += '<div class="weui-media-box_appmsg" style="'+(i==data.length-1?'border-bottom: 2px solid grey;':'')+'height: 35px"><div style="text-align: left"><input id="fk_fkje_'+data[i].jcxmid+(data[i].jczxmid?"_"+data[i].jczxmid:"")+'" name="fk_fkje" class="weui-input" type="number" style="color: black;font-weight: bold;" placeholder="请输入金额..."></div></div>'
							}
							$.modal({
								title: "核对用户信息",
								text:htmltext,
								autoClose: false,
								buttons: [
									{
									text: (flag=="scan"?"扫码付款":(flag=="transfer"?"支付宝付款":"确定")),
									onClick: function(){
										pay(flag,sjid,ybbh,hzxm)
									}
								},
									{
										text: "取消",
										className: "default",
										onClick: function(){
											$("#ajaxForm #btn_complete").removeAttr("disabled");
											$("#ajaxForm #btn_prestep").removeAttr("disabled");
											$.closeModal()
										}
									},
								]
							});
						}
					}
				});
			}else{
				$("#ajaxForm #btn_complete").removeAttr("disabled");
				$("#ajaxForm #btn_prestep").removeAttr("disabled");
				$.toptip(result.message, 'error');
			}
		},
		error: function(result) {
			hideloading();
			$("#ajaxForm #btn_complete").removeAttr("disabled");
			$("#ajaxForm #btn_prestep").removeAttr("disabled");
			$.toptip(JSON.stringify(result), 'error');
		}
	});
}

/**
 * 支付框中的扫码事件
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
								showloading("正在识别...")
								// 当needResult 为 1 时，扫码返回的结果
								var result = res.resultStr;
								// result = "http://service.matridx.com/wechat/getUserReport?ybbh=20220216003"
								var s_res = result.split('ybbh=')
								var scanYbbh = s_res[s_res.length-1];
								$.ajax({
									url: '/wechat/pay/getInspectionInfo',
									type: 'post',
									data: {"ybbh": scanYbbh},
									dataType: 'json',
									success: function(data) {
										hideloading();
										if(data.status == "success"){
											$("#fk_ybbh").val(data.sjxxDto.ybbh);
											$("#fk_sjid").val(data.sjxxDto.sjid);
											$("#fk_hzxm").val(data.sjxxDto.hzxm);
											$("#fk_fkje").val("");
										}else{
											$.toptip('未查询到标本'+ybbh, 'error');
										}
									},
									error: function(res) {
										hideloading();
										$.toptip(JSON.stringify(res), 'error');
									}
								});
							},
							fail: function(res) {
								hideloading();
								$.toptip(JSON.stringify(res), 'error');
							}
						});
					}
				});
			});
		}
	});
}

/**
 * 调用外部支付接口
 * @param flag
 * @param sjid
 * @param ybbh
 * @returns {boolean}
 */
function pay(flag,sjid,ybbh,hzxm) {
	if(!$("#fk_ybbh").val()){
		$.toptip('请填写标本编号!', 'error');
		return false;
	}
	var fk_fkjeList = document.getElementsByName("fk_fkje");
	var zfjcxm = [];
	var zje = 0;
	if(fk_fkjeList.length>0){
		for (let i = 0; i < fk_fkjeList.length; i++) {
			let fkje = fk_fkjeList[i].value;//付款金额
			if (fkje && (!/^\d+(\.\d{1,2})?$/.test(fkje) || fkje*1 <= 0)){
				$.toptip('请填写付款金额, 允许保留两位小数!', 'error');
				paying = false;
				return false;
			}
			var fk_id_list = fk_fkjeList[i].id.split("_");
			let jcxm = fk_id_list[2];//检测项目
			let json= {};
			json.jcxmid = jcxm//组装
			json.je = fkje?fkje:0//组装
			if (json.je == 0){
				break;
			}
			if (fk_id_list.length > 3){
				json.jczxmid = fk_id_list[3];//组装
			}
			zfjcxm.push(json);
			zje += new Number(fkje?fkje:0);
		}
		if (zje == 0){
			$.toptip('请填写付款金额!', 'error');
			paying = false;
			return false;
		}
		zje = ""+zje;
		zfjcxm = JSON.stringify(zfjcxm);
		$("#zfjcxm").val(zfjcxm);
	}else {
		$.toptip('请确认该样本是否已选择检测项目并填写金额!', 'error');
		paying = false;
		return false;
	}
	if(flag == "scan"){
		showloading("正在生成订单...");
		$.ajax({
			url: '/wechat/pay/createOrderQRCode',
			type: 'post',
			dataType: 'json',
			data : {
				"ywid" : sjid,
				"ybbh" : ybbh,
				"fkje": zje,
				"wxid": $("#wxid").val(),
				"zfjcxm":zfjcxm,
				"ywlx": $("#ywlx").val(),
				"wbcxdm": $("#wbcxdm").val()
			},
			success: function(data) {
				hideloading();
				if(data.status == 'success'){
					// 二维码路径 data.qrCode
					$("#preview_ajaxForm #path").val(data.filePath);
					//去掉患者姓名和样本编号中的空格
					for (;hzxm.indexOf(" ")>-1;) {
						hzxm = hzxm.replace(" ","");
					}
					for (;ybbh.indexOf(" ")>-1;) {
						ybbh = ybbh.replace(" ","");
					}
					var url="/wechat/pay/picturePreview?path="+ data.filePath +"&fkje="+zje +"&ywid="+sjid +"&ybbh="+ ybbh+"&hzxm="+ hzxm+"&ywlx="+$("#ywlx").val();
					$.closeModal()
					$("#hzxxInfo").load(url);
				}else{
					$.toptip('生成订单失败！'+data.message, 'error');
				}
			},
			error:function (data) {
				hideloading();
				$.toptip(JSON.stringify(date), 'error');
			}
		})
	}else if(flag == "transfer"){
		// 支付宝支付 判断是否为微信环境
		var ua = navigator.userAgent.toLowerCase();
		// if (ua.indexOf('micromessenger') != -1) {
		// 	// 打开引导页
		// 	window.location.href="/wechat/pay/alipayGuide?ywid="+ sjid +"&ybbh="+ ybbh +"&fkje="+ zje +"&wxid="+  $("#wxid").val() +"&ywlx="+ $("#ywlx").val()+"&wbcxdm="+$("#wbcxdm").val();
		// }else{
			showloading("正在生成订单...");
			// 创建支付宝native订单
			$.ajax({
				url: '/wechat/pay/alipayNative',
				type: 'post',
				dataType: 'json',
				data : {"ywid" : sjid, "ybbh" : ybbh,"fkje": zje,"wxid": $("#wxid").val(),"ywlx": $("#ywlx").val(),"zfjcxm":$("#zfjcxm").val(),"wbcxdm": $("#wbcxdm").val()},
				success: function(data) {
					hideloading();
					if(data.status == 'success'){
						$.closeModal()
						// 唤起支付宝路径 data.qrCode
						window.location.href = data.qrCode;
					}else{
						$.toptip('生成订单失败！'+data.message, 'error');
					}
				},
				error:function (data) {
					hideloading();
					$.toptip(JSON.stringify(date), 'error');
				}
			});
		// }
	}else {
		showloading("正在生成订单...");
		// 微信统一下单
		$.ajax({
			url: '/wechat/pay/wechatPayOrder',
			type: 'post',
			dataType: 'json',
			data : {"sjid" : sjid, "ybbh" : ybbh,"fkje": zje,"wxid": $("#wxid").val(),"ywlx": $("#ywlx").val(),"wxzflx":"public","zfjcxm":$("#zfjcxm").val()},
			success: function(data) {
				hideloading();
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
					$.toptip('生成订单失败！'+data.message, 'error');
				}
			},
			error:function (data) {
				hideloading();
				$.toptip(JSON.stringify(date), 'error');
			}
		});
	}
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
			$.alert("支付成功,支付成功后跳转到页面："+res.err_msg);
			// 支付成功后跳转的页面
			$("#preview_ajaxForm").attr('action',"/wechat/pay/wxPayComplete");
			$("#preview_ajaxForm").attr('method',"get");
			$("#preview_ajaxForm").submit();
		} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
			$.alert("支付取消,支付取消后跳转到页面："+res.err_msg);
		} else if (res.err_msg == "get_brand_wcpay_request:fail") {
			WeixinJSBridge.call('closeWindow');
			$.alert("支付失败,支付失败后跳转到页面："+res.err_msg);
			// 支付失败后跳转的页面
			$("#preview_ajaxForm").attr('action',"/wechat/pay/wxPayFaild");
			$("#preview_ajaxForm").attr('method',"get");
			$("#preview_ajaxForm").submit();
		} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
	});
}
var nowDate = new Date();
/**
 * 采样日期改变事件
 */
function changeCyrq(){
	weui.datePicker({
		title: '请选择采样日期',
		defaultValue: ($("#cyrq").val()?$("#cyrq").val().split("-"):[new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate()]),
		onConfirm: function(result){
			var cyrq = result[0]+"-"+(result[1]>9?result[1]:"0"+result[1])+"-"+(result[2]>9?result[2]:"0"+result[2]);
			$("#cyrq").val(cyrq)
		},
		id: 'datePickerCyrq'
	});
}
/**
 * 送检日期改变事件
 */
function changeSjrq(){
	weui.datePicker({
		title: '请选择送检日期',
		defaultValue: ($("#sjrq").val()?$("#sjrq").val().split("-"):[new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate()]),
		onConfirm: function(result){
			var sjrq = result[0]+"-"+(result[1]>9?result[1]:"0"+result[1])+"-"+(result[2]>9?result[2]:"0"+result[2]);
			$("#sjrq").val(sjrq)
		},
		id: 'datePickerSjrq'
	});
}

/**
 * 样本类型改变事件
 */
function changeYblx(){
	if (xzYblxList.length == 0){
		$.toptip('未获取到对应标本类型，请重新打开或联系系统管理员!', 'error');
		return false;
	}
	weui.picker(
		xzYblxList, {
		defaultValue: [$("#yblx").val()?$("#yblx").val():xzYblxList[0].value],
		onConfirm: function (result) {
			$("#xzyblxmc").val(result[0].label)
			$("#yblx").val(result[0].value)
			initQtYblx(false);
			initQqjc();
		},
		id: 'yblxchange',
		title: '请选择标本类型'
	});
}

/**
 * 初始化样本类型
 */
function initYblx(isInit){
	//初始化所有样本类型、样本类型选择list
	var yblxInputList =$("#samplelist input");
	var jcxmcsdms = $("#jcxmdms").val()
	xzYblxList = [];
	yblxList = [];
	var dqyblxmc;
	for (var i = 0; i < yblxInputList.length; i++) {
		var yblxcsid = yblxInputList[i].id;
		var yblxcsmc = yblxInputList[i].getAttribute("csmc");
		var yblxcsdm = yblxInputList[i].getAttribute("csdm");
		var yblxcskz1 = yblxInputList[i].getAttribute("extend_1");
		var yblxcskz2 = yblxInputList[i].getAttribute("extend_2");
		if (jcxmcsdms!='' && jcxmcsdms != null && jcxmcsdms != undefined){
			var szjcxmcsdms = jcxmcsdms.split(',');
			var  flag = true;
			for (var j = 0; j <szjcxmcsdms.length; j++) {
				//一个不满足就不允许
				if (yblxcskz2.indexOf(szjcxmcsdms[j])==-1){
					flag = false;
				}
			}
			if (flag){
				var xzlsyblx = {label:yblxcsmc,value:yblxcsid};
				xzYblxList.push(xzlsyblx)
			}
		}
		var lsyblx = {csid:yblxcsid,csmc:yblxcsmc,csdm:yblxcsdm,cskz1:yblxcskz1,cskz2:yblxcskz2};
		yblxList.push(lsyblx)
		if(yblxcsid == $("#yblx").val()){
			yblxdm = yblxcsdm;
			$("#xzyblxmc").val(dqyblxmc)
		}
	}
	initQtYblx(isInit);
	initQqjc();
}

/**
 * 初始化其它样本类型
 */
function initQtYblx(isInit){
	var yblxid = $("#yblx").val()
	for (var i = 0; i < yblxList.length; i++){
		var lsyblx = yblxList[i];
		if (yblxid == lsyblx.csid){
			var oldyblxcsdm = yblxdm;
			yblxdm = lsyblx.csdm;
			var newyblxcsdm = yblxdm;
			$("#xzyblxmc").val(lsyblx.csmc)
			$("#qt").show();
			if (oldyblxcsdm != 'XXX' && newyblxcsdm != 'XXX' && !isInit){
				$("#yblxmc").val(lsyblx.csmc);
			}
		}
	}
}


/**
 * 限制前期检测
 */
function initQqjc(){
	var yblxid=$("#yblx").val();
	//限制前期检测
	var dqyblx;
	var dqcsmc;
	var dqcsdm;
	for (var i = 0; i < yblxList.length; i++) {
	    if (yblxid == yblxList[i].csid) {
			dqyblx = yblxList[i]
			dqcsmc=dqyblx.csmc;
			dqcsdm=dqyblx.csdm;
			break;
        }
	}

	var length = $("#ajaxForm .qqjc").length;
	//若标本类型没有选择则全部显示；若选择了，则根据cskz2显示
	if (typeof(dqcsmc) != "undefined"){
		if(length>0){
			for(var i=0;i<length;i++){
				var id=$("#ajaxForm .qqjc")[i].id;
				var cskz2= $("#ajaxForm #"+id).attr("cskz2");
				var cskz2s = cskz2.split(",");
				if (cskz2s.includes(dqcsdm)){
					$("#ajaxForm #"+id).show();
					$("#ajaxForm input[name='sjqqjcs["+i+"].yjxm']").val(id.split("-")[1]);
				}else {
					$("#ajaxForm #"+id.split("-")[1]).val("");
					$("#ajaxForm #"+id).hide();
				}
			}
		}
	}
}

window.onload = function() {
	var list = new Array();
	for (var i = 50; i < document.all.length; i++) {
		if ((document.all[i].type == "text" && document.all[i].style.display != "none" && document.all[i].name != "cyrq") || document.all[i].tagName == "TEXTAREA")
			list.push(i);
	}

	for (var i = 0; i < list.length - 1; i++) {
		document.all[list[i]].setAttribute("nextFocusIndex", list[i + 1]);
		document.all[list[i]].onkeydown = JumpToNext;
	}
	for (var i = list.length - 1; i < document.all.length; i++) {
		if (document.all[i].type == "button") {
			document.all[list[list.length - 1]].setAttribute("nextFocusIndex", i);
			document.all[list[list.length - 1]].onkeydown = JumpToNext;
			break;
		}
	}
	document.all[list[0]].focus();
}

function JumpToNext() {
	if (event.keyCode == 13) {
		var nextFocusIndex = this.getAttribute("nextFocusIndex");
		document.all[nextFocusIndex].focus();
	}
}

/**
 * 判断是否获取到微信id
 * @returns {boolean}
 */
function getWxOrNot (){
	var wxid = $("#wxid").val();
	if (wxid==null || wxid ==''){
		$.alert("没有获取到您的微信信息，请重新至公众号打开！")
		return false;
	}
	return true;
}

$(document).ready(function(){
	if(!getWxOrNot()){
		return false;
	}
	//初始化样本类型
	initYblx(true);
});

function pushHistory() {
	var state = {
		title: "title",
		url: "#"
	};
	window.history.pushState(state, "title", "#");
}

$(function(){
	pushHistory()
	window.addEventListener("popstate", function(e) {
		window.history.go(-3)
	}, false);
});


/**
 * 处理是否完善
 */
function dealSfwsFlag(){
	var sfws = $("#ajaxForm #sfws").val() ? $("#ajaxForm #sfws").val() : '0';
	if (sfws == '0' || sfws == '2'){
		//若是否完善为0，则表示第一页未完善
		$("#ajaxForm #sfws").val(checkIsPerfect()?'2':'0');
	}else{
		//若是否完善为1或者3，则表示第一页已完善
		$("#ajaxForm #sfws").val(checkIsPerfect()?'3':'1');
	}
}
/**
 * 校验所有出报告必填项是否均已填写
 * @returns {boolean}
 */
function checkIsPerfect() {
	//校验录单必填项
	if (!checkIsRequired()){
		return false;
	}
	//校验标本体积，完善必填项
	if (!$("#ybtj").val()) {
		return false;
	}
	//校验前期诊断，完善必填项
	if (!$("#qqzd").val()) {
		return false;
	}
	//校验临床症状，完善必填项
	if (!$("#lczz").val()) {
		return false;
	}
	return true;
}

function checkIsRequired() {
	//校验标本类型，录单必填项
	if (!$("#yblx").val()) {
		return false;
	}
	//校验其他标本类型，录单必填项
	if(!$("#ajaxForm #qt").is(":hidden")){
		if (!$("#yblxmc").val()) {
			return false;
		}
	}
	//校验采样日期，录单必填项
	if (!$("#cyrq").val()) {
		return false;
	}
	return true;
}

function showloading(text){
	//loading显示
	$.showLoading(text);
}

function hideloading(){
	setTimeout(function() {
		//loading隐藏
		$.hideLoading();

	},200)
}

function showInfo(){
	var actionFlag = $("#ajaxForm #actionFlag").val()=='1'?true:false;//true:完善，false:录单
	$.alert({
		title: '必填项提示',
		text: '<span style="font-weight: bold">当前为“'+(actionFlag?'完善':'录单') + '”流程</span><br>' +
			'<span style="color:red;">*</span>　为当前流程必填项' + '<span style="font-size: 10px">' + (actionFlag?'（完善/出报告）':'（录单/接收）　') + '</span><br>' +
			'<span style="color:#00AFEC;">*</span>　为' + (actionFlag?'上一流程必填项':'下一流程必填项') + '<span style="font-size: 10px">' +(actionFlag?'（录单/接收）　':'（完善/出报告）') + '</span>',
		onOK: function () {
			//点击确认
		}
	})
}