

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
								//	http://service.matridx.com/wechat/getUserReport?ybbh=0S12H9K8
 							    var s_res = result.split('ybbh=')
								
								$("#search_input").val(s_res[s_res.length-1]);
								
								if(s_res[s_res.length-1]!=null && s_res[s_res.length-1]!= "")
									getSjxxList();
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
var viewPreModConfig = {
	    width        : "900px",
	    height        : "800px",
	    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
	    buttons        : {
	        cancel : {
	            label : "关 闭",
	            className : "btn-default"
	        }
	    }
	};
var vm = new Vue({
	el:'#feedback',
	data: {
	},
	methods:{
//		view:function(fjid){
//			var url= "/common/file/pdfPreview?fjid=" + fjid;
//            $.showDialog(url,'文件预览',viewPreModConfig);
//            
//		},
		xz:function(fjid){
		    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		xz_bg:function(fjid){
		    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		del_bg:function(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= "/common/file/delFile";
    				jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								$("#"+fjid).remove();
    							});
    						}else if(responseText["status"] == "fail"){
    							$.error(responseText["message"],function() {
    							});
    						} else{
    							$.alert(responseText["message"],function() {
    							});
    						}
    					},1);
    				},'json');
    				jQuery.ajaxSetup({async:true});
    			}
    		});
		}
	}
})
function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin+1,end);
	if(type.toLowerCase()=="jpg" || type.toLowerCase()=="jpeg" || type.toLowerCase()=="jfif" || type.toLowerCase()=="png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()=="pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var JPGMaterConfig = {
		width		: "800px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
//function feedback(ybbh){
//	window.location.href="/wechat/view/displayView?view_url=/wechat/inspection/feedBack?ybbh="+ybbh;
//}
$(function(){
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"sjxx_file",$("#ajaxForm #ywlx").val());
//	getSjxxList();
	// 所有下拉框添加choose样式
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
})