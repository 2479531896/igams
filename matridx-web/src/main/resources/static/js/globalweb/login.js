function Base64() {  

    // private property  
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";  

    // public method for encoding  
    this.encode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = _utf8_encode(input);  
        while (i < input.length) {  
            chr1 = input.charCodeAt(i++);  
            chr2 = input.charCodeAt(i++);  
            chr3 = input.charCodeAt(i++);  
            enc1 = chr1 >> 2;  
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
            enc4 = chr3 & 63;  
            if (isNaN(chr2)) {  
                enc3 = enc4 = 64;  
            } else if (isNaN(chr3)) {  
                enc4 = 64;  
            }  
            output = output +  
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +  
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);  
        }  
        return output;  
    }  

    // public method for decoding  
    this.decode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3;  
        var enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");  
        while (i < input.length) {  
            enc1 = _keyStr.indexOf(input.charAt(i++));  
            enc2 = _keyStr.indexOf(input.charAt(i++));  
            enc3 = _keyStr.indexOf(input.charAt(i++));  
            enc4 = _keyStr.indexOf(input.charAt(i++));  
            chr1 = (enc1 << 2) | (enc2 >> 4);  
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);  
            chr3 = ((enc3 & 3) << 6) | enc4;  
            output = output + String.fromCharCode(chr1);  
            if (enc3 != 64) {  
                output = output + String.fromCharCode(chr2);  
            }  
            if (enc4 != 64) {  
                output = output + String.fromCharCode(chr3);  
            }  
        }  
        output = _utf8_decode(output);  
        return output;  
    }  

    // private method for UTF-8 encoding  
    _utf8_encode = function (string) {  
        string = string.replace(/\r\n/g,"\n");  
        var utftext = "";  
        for (var n = 0; n < string.length; n++) {  
            var c = string.charCodeAt(n);  
            if (c < 128) {  
                utftext += String.fromCharCode(c);  
            } else if((c > 127) && (c < 2048)) {  
                utftext += String.fromCharCode((c >> 6) | 192);  
                utftext += String.fromCharCode((c & 63) | 128);  
            } else {  
                utftext += String.fromCharCode((c >> 12) | 224);  
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);  
                utftext += String.fromCharCode((c & 63) | 128);  
            }  

        }  
        return utftext;  
    }  

    // private method for UTF-8 decoding  
    _utf8_decode = function (utftext) {  
        var string = "";  
        var i = 0;  
        var c = c1 = c2 = 0;  
        while ( i < utftext.length ) {  
            c = utftext.charCodeAt(i);  
            if (c < 128) {  
                string += String.fromCharCode(c);  
                i++;  
            } else if((c > 191) && (c < 224)) {  
                c2 = utftext.charCodeAt(i+1);  
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));  
                i += 2;  
            } else {  
                c2 = utftext.charCodeAt(i+1);  
                c3 = utftext.charCodeAt(i+2);  
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));  
                i += 3;  
            }  
        }  
        return string;  
    }  
}
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
		var base = new Base64();
		var hash = base.encode($("#password").val());
		$.ajax({ 
		    type:'post',  
		    url:"/oauth/token", 
		    cache: false,
		    username:$("#username").val(),
		    password:$("#password").val(),
		    data: {"grant_type":"matridx","client_id":$("#username").val(),"client_secret":hash},  
		    dataType:'json', 
		    success:function(data){
		    	$("#access_token").val(data.access_token);
		    	$("#ref").val(data.refresh_token);
		    	//alert("access_token:"+data.access_token + "  token_type:" +  data.token_type + "  refresh_token:" +  data.refresh_token + "  expires_in:" +  data.expires_in + "  scope:" +  data.scope);
		        document.forms[0].submit();
		    },
		    error:function(event, XMLHttpRequest, ajaxOptions, thrownError){
				if(event.responseJSON == undefined){
		    		$.alert("请先确认用户是否存在或者用户已经被锁定！");
		    	}else if(event.responseJSON.status == 452){
		    		$.alert("用户已经被锁定，请先联系管理员进行解锁！");
		    	}else if(event.responseJSON.error_description!=null){
    		    	$.alert(event.responseJSON.error_description);
		    	}else{
		    		$.alert("用户名或者密码不正确！");
		    	}
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
