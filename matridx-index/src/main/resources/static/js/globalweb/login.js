
/**
* 登陆按钮绑定事件
*/
jQuery(function($){
	$("#dl").unbind("click").click(function(){
		var ts = '<span class="glyphicon glyphicon-minus-sign"></span>';
		if(!$("#username").val() || $("#username").val()==""){
			$("#tips").empty().append(ts + "用户名不能为空！");
			$("#tiplogin").empty();
			$("#tips").show();
			return false;
		}
		if(!$("#password").val() || $("#password").val()==""){
			$("#tips").empty().append(ts + "密码不能为空！");
			$("#tiplogin").empty();
			$("#tips").show();
			return false;
		}
		/*if($("#yzmDiv") && $("#yzmDiv:visible").size()>0){
			if(!$("#yzm").val()|| $("#yzm").val()==""){
				$("#tips").empty().append(ts + "验证码不能为空！");
				$("#tiplogin").empty();
				$("#tips").show();
				return false;
			}
		}*/
		$.ajax({ 
		    type:'post',  
		    url:"/oauth/token", 
		    cache: false,
		    username:$("#username").val(),
		    password:$("#password").val(),
		    data: {"grant_type":"password","username":$("#username").val(),"password":$("#password").val()},  
		    dataType:'json', 
		    success:function(data){
		    	var access_token=data.access_token;
		    	var refresh_token=data.refresh_token;
		    	$("#access_token").val(data.access_token);
		    	$("#ref").val(data.refresh_token);
//		    	//alert("access_token:"+data.access_token + "  token_type:" +  data.token_type + "  refresh_token:" +  data.refresh_token + "  expires_in:" +  data.expires_in + "  scope:" +  data.scope);
		        document.forms[0].submit();
		    	
		    },
		    error:function(){ 
		    	alert("请求失败")
		    }
		});
		return false;
	});
});

$(document).ajaxStart(function () {
    //this 里面有很多信息 比如url 什么的 所有的请求都会出发它 d
    //do somesing
	//var $loading = $('<div id="loading">Loading....</div>').insertBefore("#dictionary");
    $(document).ajaxStart(function(){
        //$loading.show();
    }).ajaxStop(function(){
        //$loading.hide();
    });
})
