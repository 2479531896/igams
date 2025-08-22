/**
 * 启动刷新计时器
 * @returns
 */
function startInterval(){
	$("#btn_refresh").attr('disabled',true);
	if(interval != null){
		clearInterval(interval);
	}
	var sec = 60;
	$("#btn_refresh").text(sec+"s 可刷新");
	interval = setInterval(function countDown() {
		if(sec > 0) {
			sec = sec-1;
			$("#btn_refresh").text(sec+"s 可刷新");
		} else {
			clearInterval(interval);
			$("#btn_refresh").text("点击刷新");
			$("#btn_refresh").attr('disabled',false);
		}
	},1000);
}

/**
 * 事件绑定
 * @returns
 */
function btnBind() {
	// 刷新按钮
	$("#btn_refresh").unbind("click").click(function(){
		$("#btn_refresh").attr('disabled',true);
		$.ajax({
			url: '/wechat/pay/createOrderQRCode',
			type: 'post',
			dataType: 'json',
			data : {
				"ywid" : $("#qrcodeDiv #ywid").val(),
				"ybbh" : $("#qrcodeDiv #ybbh").val(),
				"fkje": $("#qrcodeDiv #fkje").val(),
				"wxid": $("#wxid").val(),
				"ywlx":$("#qrcodeDiv #ywlx").val(),
				"zfjcxm":$("#zfjcxm").val(),
				"wbcxdm":$("#wbcxdm").val()
			},
			success: function(data) {
				if(data.status == 'success'){
					// 二维码路径 data.qrCode data.filePath
					$("#qrcode_img").attr("src","/wechat/pay/getPicturePreview?path="+data.filePath);
					startInterval();
					// 有效时间初始化
					initTime();
				}else{
					$("#ajaxForm #errorMsg").text("保存失败！"+data.message);
					$("#ajaxForm button").attr("disabled", false);
				}
			}
		});
	});
}

/**
 * 初始化页面
 * @returns
 */
function initPage(){
	/*var winwidth=$("#imgDiv").width()*0.8;
	$("#qrcode_img").css("width",winwidth);*/
	// 启动计时器
	startInterval();
	// 有效时间初始化
	initTime();
}

/**
 * 初始化有效时间
 * @returns
 */
function initTime(){
	// 计算有效时间 900s
	var curTime = new Date();
	var date = dateFtt("hh:mm:ss",new Date(curTime.setSeconds(curTime.getSeconds() + 900)));
	$("#yxsjShow").text(date);
}

/**
 * 日期格式化
 * @param fmt
 * @param date
 * @returns
 */
function dateFtt(fmt,date) {
	var o = { 
		"M+" : date.getMonth()+1, // 月份
		"d+" : date.getDate(), // 日
		"h+" : date.getHours(), // 小时
		"m+" : date.getMinutes(), // 分
		"s+" : date.getSeconds(), // 秒
		"q+" : Math.floor((date.getMonth()+3)/3), // 季度
		"S" : date.getMilliseconds() // 毫秒
	}; 
	if(/(y+)/.test(fmt)) 
		fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	for(var k in o) 
		if(new RegExp("("+ k +")").test(fmt)) 
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
	return fmt; 
}

$(document).ready(function(){
	// 初始化页面数据
	initPage();
	// 绑定事件
	btnBind();
});
