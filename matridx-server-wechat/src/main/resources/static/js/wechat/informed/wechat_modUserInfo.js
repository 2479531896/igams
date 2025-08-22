//事件绑定
function btnBind() {
}

function initPage(){
	if($("#ajaxForm #sj").val()){
		$("#ajaxForm #yzm").hide();
	}else{
		$("#ajaxForm #sj").removeAttr("readonly");
		$("#ajaxForm #unbind").hide();
		$("#ajaxForm #sendMsm").show();
	}
}

var interval = null;

/**
 * 修改信息
 * @returns {Boolean}
 */
function modUser(){
	$("#ajaxForm #modUserButton").attr('disabled',true);
	$("#ajaxForm #modUserButton").addClass('weui-btn_disabled');
	$("#ajaxForm #modUserButton").removeClass('weui-btn_bule');
	if(!$("#ajaxForm #yhm").val()){
		$("#ajaxForm #modUserButton").attr('disabled',false);
		$("#ajaxForm #modUserButton").removeClass('weui-btn_disabled');
		$("#ajaxForm #modUserButton").addClass('weui-btn_bule');
		$.toptip('请填写用户名！', 'warning');
		return false;
	}
	if(!$("#ajaxForm #sj").val()){
		$("#ajaxForm #modUserButton").attr('disabled',false);
		$("#ajaxForm #modUserButton").removeClass('weui-btn_disabled');
		$("#ajaxForm #modUserButton").addClass('weui-btn_bule');
		$.toptip('请填写手机！', 'warning');
		return false;
	}
	if(!$("#yzm").is(":hidden")&&(!$("#cskz1").val())){
		$("#ajaxForm #modUserButton").attr('disabled',false);
		$("#ajaxForm #modUserButton").removeClass('weui-btn_disabled');
		$("#ajaxForm #modUserButton").addClass('weui-btn_bule');
		$.toptip('请填写验证码！', 'warning');
		return false;
	}
	$.ajax({
		url: '/wechat/modSaveWeChatUser',
		type: 'post',
		data: $("#ajaxForm").serialize(),
		dataType: 'json',
		success: function(result) {
			if(result.status == "success"){
				$.toptip(result.message, 'success');
				if(interval){
					clearInterval(interval);
					$("#ajaxForm #sendMsm").text("短信验证");
					$("#ajaxForm #sendMsm").attr('disabled',false);
				}
				$("#ajaxForm #cskz1").val("");
				$("#ajaxForm #sj").attr("readonly","readonly")
				$("#ajaxForm #unbind").show();
				$("#ajaxForm #sendMsm").hide();
				$("#ajaxForm #yzm").hide();
				setTimeout(function(){
					$("#ajaxForm #modUserButton").attr('disabled',false);
					$("#ajaxForm #modUserButton").removeClass('weui-btn_disabled');
					$("#ajaxForm #modUserButton").addClass('weui-btn_bule');
				},2000)

			}else{
				setTimeout(function(){
					$("#ajaxForm #modUserButton").attr('disabled',false);
					$("#ajaxForm #modUserButton").removeClass('weui-btn_disabled');
					$("#ajaxForm #modUserButton").addClass('weui-btn_bule');
				},2000)
				$.toptip(result.message, 'error');
			}
		}
	});
}

/*验证手机号码*/
function PhoneVerify() {
	var phoneNum = $("#ajaxForm #sj").val();
	if (phoneNum != null && phoneNum != '') {
		var reg = /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8}|1\d{10})$/;
		if (!reg.test(phoneNum)) {
			$.toptip('请输入正确的手机号码！', 'warning');
			return false;
		}
		return true
	}
	$.toptip('请输入正确的手机号码！', 'warning');
	return false
}

$("#ajaxForm #unbind").click(function unbind(){
	$.confirm("您确定要解除绑定吗？", function() {
		//点击确认后的回调函数
		$.ajax({
			url:"/wechat/updateSj",
			type:"post",
			data:{
				wxid:$("#wxid").val(),
				Sj:"",tj_flag:"1",
				wbcxdm:$("#wbcxdm").val()
			},
			dataTypep:"JSON",
			success:function(data){
				if(data.state=="success"){
					$.toptip(data.message, 'success');
					$("#ajaxForm #sj").removeAttr("readonly");
					$("#ajaxForm #sj").val("");
					$("#ajaxForm #unbind").hide();
					$("#ajaxForm #sendMsm").show();
					$("#ajaxForm #sendMsm").text("短信验证");
					$("#ajaxForm #yzm").show();
				}else {
					$.toptip(data.message, 'error');
				}
			}
		})
	}, function() {
		//点击取消后的回调函数
	});
})

$("#ajaxForm #sendMsm").click(function unbind(){
	$("#ajaxForm #sendMsm").attr('disabled',true);
	if (!PhoneVerify()) {
		$("#ajaxForm #sendMsm").attr('disabled',false);
		return;
	}
	//发送短信验证码
	$.ajax({
		url: "/wechat/getSms",
		type: "post",
		data: {wxid:$("#ajaxForm #wxid").val(),cskz2:$("#ajaxForm #sj").val(),},
		dataTypep: "JSON",
		success:function(result){
			if (result.status == 'success') {
				$.toptip('发送成功！', 'success');
				var sec = 60;
				//获取验证码成功处理
				interval = setInterval(function countDown() {
					if(sec > 0) {
						$("#ajaxForm #sendMsm").text("重新发送("+sec+")");
						sec = sec-1;
					} else {
						clearInterval(interval);
						$("#ajaxForm #sendMsm").text("重新发送");
						$("#ajaxForm #sendMsm").attr('disabled',false);
					}
				},1000);
			}else {
				$("#ajaxForm #sendMsm").attr('disabled',false);
				$.toptip('发送失败！', 'error');
			}
		}
	})
})

$(document).ready(function(){
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});