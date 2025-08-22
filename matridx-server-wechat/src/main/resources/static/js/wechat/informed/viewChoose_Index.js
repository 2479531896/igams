$(function (){
	//病原体检测
	$("#viewChooseIndex #reportViewChoose_input").unbind("click").click(function(){
		if(!$("#wxid").val()){
			alert("未正确获取微信信息，请从菜单重新进入！");
			return false
		}
		$.ajax({
			url :"/wechat/getUserInfoByWxid",
			type : "POST",
			dataType : "json",
			data : {
				"wxid" : $("#wxid").val(),
				"wbcxdm" : $("#viewChooseIndex #wbcxdm").val()
			},
			success : function(data) {
				if (data.code == 'success'){
					if (data.flag) {
						var redirect_uri = encodeURIComponent($("#prefixUrl").val()+"wechat/getWechatUserInfo?toUrl=wechat%2FviewChooseByUserAuthWithAuthorize&wbcxdm="+data.wbcxdm)
						//window.open("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+data.appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
						window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+data.appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
					}else {
						$("#viewChooseIndex #jcxmdms").val("");
						$("#viewChooseIndex #disabledjcxmdms").val("");
						$("#viewChooseIndex #flag").val("");
						$("#viewChooseIndex").attr("action", "/wechat/reportViewChooseByUserAuthWithoutCode");
						document.getElementById("viewChooseIndex").submit();
					}
				}else {
					alert(data.message);
					return false
				}
			},
			error : function(data) {
				alert("网络异常！请从菜单重新进入！");
				return false
			},
			fail: function (result) {
				alert("网络异常！请从菜单重新进入！");
				return false
			}
		})
		return true;
	});
	//Res检测
	$("#viewChooseIndex #resFirstChoose_input").unbind("click").click(function(){
		if(!$("#wxid").val()){
			alert("未正确获取微信信息，请从菜单重新进入！");
			return false
		}
		$.ajax({
			url :"/wechat/getUserInfoByWxid",
			type : "POST",
			dataType : "json",
			data : {
				"wxid" : $("#wxid").val(),
				"wbcxdm" : $("#viewChooseIndex #wbcxdm").val()
			},
			success : function(data) {
				if (data.code == 'success'){
					if (data.flag) {
						var redirect_uri = encodeURIComponent($("#prefixUrl").val()+"wechat/getWechatUserInfo?toUrl=wechat%2FviewChooseByUserAuthWithAuthorize%3Fjcxmdms%3D047%26flag%3DResFirst&wbcxdm="+data.wbcxdm)
						//window.open("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+data.appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
						window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+data.appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
					}else {
						$("#viewChooseIndex #jcxmdms").val("047");
						$("#viewChooseIndex #disabledjcxmdms").val("");
						$("#viewChooseIndex #flag").val("ResFirst");
						$("#viewChooseIndex").attr("action", "/wechat/reportViewChooseByUserAuthWithoutCode");
						document.getElementById("viewChooseIndex").submit();
					}
				}else {
					alert(data.message);
					return false
				}
			},
			error : function(data) {
				alert("网络异常！请从菜单重新进入！");
				return false
			},
			fail: function (result) {
				alert("网络异常！请从菜单重新进入！");
				return false
			}
		})
		return true;
	});
    //普检检测
    $("#viewChooseIndex #generalInspectionChoose_input").unbind("click").click(function(){
        if(!$("#wxid").val()){
            alert("未正确获取微信信息，请从菜单重新进入！");
            return false
        }
        $("#viewChooseIndex").attr("action", "/wechat/detectionChoose");
        document.getElementById("viewChooseIndex").submit();
        return true;
    });

	$("#viewChooseIndex #generalInspectionChoose_input").unbind("click").click(function(){
		if(!$("#wxid").val()){
			alert("未正确获取微信信息，请从菜单重新进入！");
			return false
		}
		$.ajax({
			url :"/wechat/getUserInfoByWxid",
			type : "POST",
			dataType : "json",
			data : {
				"wxid" : $("#wxid").val(),
				"wbcxdm" : $("#viewChooseIndex #wbcxdm").val()
			},
			success : function(data) {
				if (data.code == 'success'){
					if (data.flag) {
						var redirect_uri = encodeURIComponent($("#prefixUrl").val()+"wechat/getWechatUserInfo?toUrl=wechat%2FdetectionPJ%2FgeneralInspectionChooseMap&wbcxdm="+data.wbcxdm)
						//window.open("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+data.appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
						window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+data.appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
					}else {
						$("#viewChooseIndex").attr("action", "/wechat/detectionPJ/generalInspectionChoose");
						document.getElementById("viewChooseIndex").submit();
					}
				}else {
					alert(data.message);
					return false
				}
			},
			error : function(data) {
				alert("网络异常！请从菜单重新进入！");
				return false
			},
			fail: function (result) {
				alert("网络异常！请从菜单重新进入！");
				return false
			}
		})
		return true;
	});
});