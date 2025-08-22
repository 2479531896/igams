//事件绑定
function btnBind() {
	
	//付款按钮
	$("#ajaxForm #pay").unbind("click").click(function(){
		$("#ajaxForm").attr('action',"/wechat/pay/payWebsite");
		$("#ajaxForm").attr('method',"get");
		$("#ajaxForm").submit();
	});

}

//患者 查看报告
function checkUserInfo(){
	if(!$("#ybbhDiv").is(":hidden")){
		if(!$("#ajaxForm #ybbh").val()){
			$.toptip('请填写标本编号!', 'error');
			return false;
		}
	}
	if(!$("#hzxmDiv").is(":hidden")){
		if(!$("#ajaxForm #hzxm").val()){
			$.toptip('请填写患者姓名!', 'error');
			return false;
		}
	}
	document.getElementById("ajaxForm").submit();
}
var pageNumber = 0;
var LoadPageSize = 0;
//查询报告信息
function searchReportList(){
	pageNumber = 0;
	LoadPageSize = 0;
	loading = false;
	$("#reportList").html("");
	loadMoreReportList();
}
//医生 加载报告信息
function loadMoreDoctorReportList(){
	pageNumber = pageNumber + 1;
	$.ajax({
		url : '/wechat/getPageListByysdh',
		type : 'post',
		dataType : 'json',
		data : {
			pageSize : 15,
			pageNumber : pageNumber,
			sortName : 'sjxx.lrsj',
			sortOrder : 'desc',
			sortLastName : 'sjxx.sjid',
			sortLastOrder : 'asc',
			hzxm : $("#ajaxForm #hzxm").val()?$("#ajaxForm #hzxm").val():"",
			ysdh : $("#ajaxForm #sj").val()?$("#ajaxForm #sj").val():"",
			sign : $("#ajaxForm #sign").val()?$("#ajaxForm #sign").val():"",
		},
		success : function(data) {
			if(data.total >0 ){
				LoadPageSize += data.total;
				htmlConcat(data.rows);
				loading = false;
				$("#loading").hide();
				if(data.total<15){
					$("#noInfo").hide();
					$("#loadComplete").show();
					loading = true;
				}
			}else {
				$("#loading").hide();
				if(LoadPageSize == 0){
					$("#noInfo").show();
					$("#loadComplete").hide();
				}else {
					$("#noInfo").hide();
					$("#loadComplete").show();
				}
			}
		}
	})
}
//销售 加载报告信息
function loadMoreSalesReportList(){
	pageNumber = pageNumber + 1;
	$.ajax({
		url : '/wechat/getPageListByysdh',
		type : 'post',
		dataType : 'json',
		data : {
			pageSize : 15,
			pageNumber : pageNumber,
			sortName : 'sjxx.lrsj',
			sortOrder : 'desc',
			sortLastName : 'sjxx.sjid',
			sortLastOrder : 'asc',
			hzxm : $("#ajaxForm #hzxm").val()?$("#ajaxForm #hzxm").val():"",
			ysdh : $("#ajaxForm #sj").val()?$("#ajaxForm #sj").val():"",
			sign : $("#ajaxForm #sign").val()?$("#ajaxForm #sign").val():"",
		},
		success : function(data) {
			if(data.total >0 ){
				LoadPageSize += data.total;
				htmlConcat(data.rows);
				loading = false;
				$("#loading").hide();
				if(data.total<15){
					$("#noInfo").hide();
					$("#loadComplete").show();
					loading = true;
				}
			}else {
				$("#loading").hide();
				if(LoadPageSize == 0){
					$("#noInfo").show();
					$("#loadComplete").hide();
				}else {
					$("#noInfo").hide();
					$("#loadComplete").show();
				}
			}
		}
	})
}
//加载报告信息
function loadMoreReportList(){
	var sflx = $("#ajaxForm #sflx").val();
	if(sflx == "DOCTOR"){
		if(!$("#ajaxForm #hzxm").val()){
			$.toptip('请填写患者姓名!', 'error');
			return false;
		}
		if(!$("#ajaxForm #sj").val()){
			$.toptip('请填写医师电话!', 'error');
			return false;
		}
		loadMoreDoctorReportList();
	}else if(sflx == "SALES"){
		loadMoreSalesReportList();
	}
}

//html拼接
function htmlConcat(rows){
	var html = "";
	for(var i=0;i<rows.length;i++){
		html += '<div class="weui-form-preview" style="margin: 10px;border-radius: 10px;background-color: white" onclick="reportDealById(\''+rows[i].sjid+'\',\''+rows[i].ysdh+'\',\''+rows[i].hzxm+'\',\''+rows[i].sign+'\',\''+rows[i].ybbh+'\')">' +
			'<div class="weui-form-preview__bd" style="font-size: 14px;font-weight: bolder;padding: 0px 10px">' +
			'  <div style="display: flex">' +
			'    <div class="weui-form-preview__item" style="flex: 3">' +
			'      <label class="weui-form-preview__label" style="margin-right: 2px;min-width: 2em;">姓名：</label>' +
			'      <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">'+rows[i].hzxm+'</span>' +
			'    </div>' +
			'    <div class="weui-form-preview__item" style="flex: 4">' +
			'      <label class="weui-form-preview__label" style="margin-right: 2px;">送检日期：</label>' +
			'      <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;">'+rows[i].cyrq+'</span>' +
			'    </div>' +
			'  </div>' +
			'  <div style="display: flex">' +
			'    <div class="weui-form-preview__item" style="flex: 3">' +
			'      <label class="weui-form-preview__label" style="margin-right: 2px;min-width: 2em;">编号：</label>' +
			'      <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+rows[i].ybbh+'</span>' +
			'    </div>' +
			'    <div class="weui-form-preview__item" style="flex: 4">' +
			'      <label class="weui-form-preview__label" style="margin-right: 2px;">报告日期：</label>' +
			'      <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+rows[i].bgrq+'</span>' +
			'    </div>' +
			'  </div>' +
			'</div>' +
			'</div>'
	}
	$("#reportList").append(html);
}
var loading = false;
/**
 * 下拉到底部触发事件
 */
$(document.body).infinite(50).on("infinite", function() {
	if(loading) return;
	loading = true;
	$("#loading").show();
	loadMoreReportList();
});

//扫码
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
								
								var ua = navigator.userAgent.toLowerCase();
								//微信浏览器
								if (ua.indexOf('micromessenger') != -1) {
									return;
								}
								
								//获取相应标本的信息，确认是否需要进行支付
								$.ajax({
									url: '/wechat/getInspectionInfo',
									type: 'post',
									dataType: 'json',
									data : {"ybbh" : $("#ajaxForm #ybbh").val(),"sfflg":"1"},
									success: function(result) {
										if(result.sjxxDto && result.sjxxDto.fkbj != "1"){
											$("#ajaxForm #pay_je").html(result.sjxxDto.fkje);
											$("#ajaxForm #pay").removeClass("hidden");
										}else{
											$("#ajaxForm #pay_je").html("");
											$("#ajaxForm #pay").addClass("hidden");
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

//页面初始化
function initPage(){
	$("#loading").hide();
	$("#noInfo").hide();
	$("#loadComplete").hide();
	if($("#ajaxForm #fkbj").val()!="1" && $("#ajaxForm #ybbh").val()!=""
		&& $("#ajaxForm #ybbh").val()!=undefined && $("#ajaxForm #fkje").val()!=undefined && $("#ajaxForm #fkje").val()!=""){
		$("#ajaxForm #pay_je").html($("#ajaxForm #fkje").val());
		$("#ajaxForm #fy").val($("#ajaxForm #fkje").val());
		$("#ajaxForm #alipayDiv").removeClass("hidden");
		$("#ajaxForm #pay").removeClass("hidden");
	}
	var sflx = $("#ajaxForm #sflx").val();
	if(sflx == "DOCTOR"){
		$("#ajaxForm #ybbhDiv").hide();
		$("#ajaxForm #hzxmDiv").show();
		$("#ajaxForm #ysdhDiv").show();
		$("#ajaxForm #checkUserInfoButton").hide();
		$("#ajaxForm #searchReportListButton").show();
	}else if(sflx == "PATIENT"){
		$("#ajaxForm #ybbhDiv").show();
		$("#ajaxForm #hzxmDiv").show();
		$("#ajaxForm #ysdhDiv").hide();
		$("#ajaxForm #checkUserInfoButton").show();
		$("#ajaxForm #searchReportListButton").hide();
		if($("#ajaxForm #ybbh").val()==null||$("#ajaxForm #ybbh").val()==""){
			getQRCode();
		}
	}else if(sflx == "SALES"){
		$("#ajaxForm #ybbhDiv").hide();
		$("#ajaxForm #hzxmDiv").show();
		$("#ajaxForm #ysdhDiv").hide();
		$("#ajaxForm #checkUserInfoButton").hide();
		$("#ajaxForm #searchReportListButton").show();
	}

}

function reportDealById(sjid,ysdh,hzxm,sign,ybbh){
	var sflx = $("#ajaxForm #sflx").val();
	if(sflx == "DOCTOR"){
		window.location.href="/wechat/getUserInfoView?sjid="+sjid+"&sflx="+sflx+"&sign="+encodeURIComponent(sign)+"&ybbh="+ybbh;
	}else if(sflx == "SALES"){
		window.location.href="/wechat/getUserInfoView?sjid="+sjid+"&sflx="+sflx+"&sign="+encodeURIComponent($("#sign").val());
	}
}
$(document).ready(function(){
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});