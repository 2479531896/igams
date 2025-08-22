var preSelectYbbh = "";

/**
 * 标本状态增加取消操作
 * @returns
 */
function confirm_calcel(){
    $("#ybztDiv input[type='checkbox']").each(function(index){
        if(this.checked){
            var ztmc=$("#"+this.value).next().text();
			this.checked = false;
            // $("#"+this.value).removeAttr("checked");
            $("#ajaxForm #bz").val($("#ajaxForm #bz").val().replace("["+ztmc+"]",""));
        }
    });
    $("#ajaxForm .ybzt_flg").val("");
}


var ybbhTimer = null;

/**
 * 绑定按钮事件
 */
function btnBind(){
	$("#ajaxForm #ybbh").on('keyup',function(e){
		if(e.keyCode==123 || e.keyCode==74|| e.keyCode==13){
			//若键入F12或J或回车，则自动查询
			getInsp();
			if(e.keyCode==74 && $("#ybbh").val().length >=8){
				//若键入J键，且样本编号长度大于等于8位，则不输入
				return false;
			}
		}else if(e.keyCode == 229 && ybbhTimer == null){
			ybbhTimer = setTimeout(function(){
				getInsp();
				ybbhTimer = null;
			}, 800)
		}
	});
	
	$("#ajaxForm #nbbm").on('keyup',function(e){
		if(e.keyCode==74 && $("#nbbm").val().length >=9){
			return false;
		}
	});
	$("#ajaxForm #ybbh").blur(function(){
		getInsp();
	});
	if($("#ajaxForm #ybbh").val()){
		getInsp();
	}
}

function nbbmbt(){
	$("#ajaxForm #nbbmspan").show();
}
function nbbmfbt(){
	$("#ajaxForm #nbbmspan").hide();
}

function getInsp(){
	var ybbh = $("#ajaxForm #ybbh").val().toUpperCase();
	if (ybbh.length > 0) {
		if( ybbh.length >= 8){
			var i=ybbh.indexOf("YBBH=");
			if(i!="-1"){
				ybbh = ybbh.substring(i+5,ybbh.length);
				$("#ajaxForm #ybbh").val(ybbh);
			}
		}
		getInspection(false);
		//如果为全血或者血浆时重新获取sygl
		if($("#ajaxForm #yblx_flg").val()=='1'){
		    ConfirmInfo();
		}
	}
}

function init(){
	setTimeout("$('#ajaxForm #ybbh').focus()", 100);
}

function checkAll(){
	$("#ajaxForm #jclx input[type='checkbox']").each(function(index){
		this.checked = true;
	});
}

function ConfirmInfo(){
	var nbbm = $("#ajaxForm #nbbm").val();
	if (nbbm){
		var yblxdm = nbbm.substring(nbbm.length -1)
		if(yblxdm != $("#ajaxForm #yblxdm").val()){
			$("#ajaxForm label[name='jclxs']").remove()
			$("#ajaxForm #jclxs").val("")
			$("#ajaxForm #button").remove()
			$("#ajaxForm #button2").remove()
			var url = "/inspection/pathogen/pagedataGetConfirmInfo";
			$.ajax({
				type : "POST",
				url : url,
				async:false,
				data : {"yblxdm":yblxdm,"sjid":$("#ajaxForm #sjid").val(),"access_token":$("#ac_tk").val(),"jcdwmc":$("#ajaxForm #jcdwmc").val(),"sjqfmc":$("#ajaxForm #sjqfmc").val()},
				dataType : "json",
				success : function(data){
					if (data.dtoList && data.dtoList.length > 0){
						$("#ajaxForm #yblx_flg").val("1");
						var html = "";
						var json = [];
						$.each(data.dtoList,function(i){
							var sz = {"syglid": '', "sjid": '',"jcxmid":'',"jczxmid":'',"jclxid":'',"jcdw":'',"sfjs":'',"nbzbm":'',"jsrq":'',"jsry":'',"jcbj":'',"syrq":'',"zt":'',"bz":'',"qysj":'',"qyry":'',"dyid":''};
							sz.syglid = data.dtoList[i].syglid;
							sz.sjid = data.dtoList[i].sjid;
							sz.jcxmid = data.dtoList[i].jcxmid;
							sz.jczxmid = data.dtoList[i].jczxmid;
							sz.jclxid = data.dtoList[i].jclxid;
							sz.jcdw = data.dtoList[i].jcdw;
							sz.sfjs = data.dtoList[i].sfjs;
							sz.nbzbm = data.dtoList[i].nbzbm;
							sz.jsrq = data.dtoList[i].jsrq;
							sz.jsry = data.dtoList[i].jsry;
							sz.jcbj = data.dtoList[i].jcbj;
							sz.syrq = data.dtoList[i].syrq;
							sz.zt = data.dtoList[i].zt;
							sz.bz = data.dtoList[i].bz;
							sz.qysj = data.dtoList[i].qysj;
							sz.qyry = data.dtoList[i].qyry;
							sz.dyid = data.dtoList[i].dyid;
							json.push(sz);
							html+="<label name='jclxs' id='"+data.dtoList[i].jclxid+"' nbzbm='"+data.dtoList[i].nbzbm+"' class=\"checkboxLabel checkbox-inline\"style=\"padding-left:0px;\">";
							html+="<input value='"+data.dtoList[i].jclxid+"' checked='true' type=\"checkbox\" name=\"ids\"/>";
							if (!data.dtoList[i].nbzbm)
								$.alert("检测类型信息丢失请联系管理员解决！");
							html+="<span style=\"padding-right: 25px;\"><span style=\"color:DarkOrange\">"+data.dtoList[i].jcxmmc+"</span>(<span style=\"color:red\">"+data.dtoList[i].nbzbm+"</span>)</span></label>";
						})
						$("#ajaxForm #state").val(JSON.stringify(json));
						html+="<button id='button' type='button' style='margin-left: 10px;' class='btn btn-default' onclick='checkAll()'>全选</button>";
						html+="<button id='button2' type='button' style='margin-left: 10px;' class='btn btn-default' onclick='adjustView()'>调整</button>";
						$("#ajaxForm #jclx").append(html);
					}
				}
			});
		}
	}




}
function getInspection(ad_flag){
	var url = "/inspection/pathogen/pagedataInspection";
	var ybbh = $("#ajaxForm #ybbh").val();
	if (preSelectYbbh == ybbh && !ad_flag){
		return;
	}
	preSelectYbbh = ybbh;
	$("#ajaxForm #hzxm").text("");
	$("#ajaxForm #nl").text("");
	$("#ajaxForm #xbmc").text("");
	$("#ajaxForm #sjdw").text("");
	$("#ajaxForm #sjys").text("");
	$("#ajaxForm #xgsj").val("");
	$("#ajaxForm #zyh").text("");
	$("#ajaxForm #sjid").val("");
	if (!ad_flag){
		$("#ajaxForm #nbbm").val("");
	}
	$("#ajaxForm #yblxmc").text("");
	$("#ajaxForm #yblxdm").val("");
	$("#ajaxForm #yblx_flg").val("");
	$("#ajaxForm #state").val("");
	$("#ajaxForm #bz").val("")
	$("#ajaxForm #jstj").val("")
	$("#ajaxForm #ks").text("");
	$("#ajaxForm #db").text("");
	$("#ajaxForm #ybtj").text("");
	$("#ajaxForm #jcxm").text("");
	$("#ajaxForm #dh").text("");
	$("#ajaxForm #ysdh").text("");
	$("#ajaxForm #jqyy").text("");
	$("#ajaxForm #lczz").text("");
	$("#ajaxForm #qqjcmc").text("");
	$("#ajaxForm #qqzd").text("");
	$("#ajaxForm #gzbymc").text("");
	$("#ajaxForm #print_flg").val("");
	$("#ajaxForm #kdfy").val("");
	$("#ajaxForm #kdh").val("");
	$("#ajaxForm #jcdwmc").val("");
	$("#ajaxForm #jcdw").val("");
	$("#ajaxForm #sjqfmc").val("");
	$("#ajaxForm label[name='jclxs']").remove()
	$("#ajaxForm #jclxs").val("")
	$("#ajaxForm #kyxmmc").text("")
	$("#ajaxForm #button").remove()
	$("#ajaxForm #button2").remove()
	$("#ajaxForm #bbsdwd").val("");
	$("#ajaxForm .wdsffh").prop("checked",false)
	$("#ajaxForm .bzsffh").prop("checked",false)

	$.ajax({
		type : "POST",
		url : url,
		async:false,
		data : {"ybbh":ybbh,"access_token":$("#ac_tk").val(),"qfmc":$("#ajaxForm #key").val()},
		dataType : "json",
		success : function(data){

			if (data.dtoList && data.dtoList.length > 0){
				var html = "";
				$.each(data.dtoList,function(i){
					html+="<label name='jclxs' id='"+data.dtoList[i].jclxid+"' nbzbm='"+data.dtoList[i].nbzbm+"' class=\"checkboxLabel checkbox-inline\"style=\"padding-left:0px;\">";
					html+="<input value='"+data.dtoList[i].jclxid+"' checked='true' type=\"checkbox\" name=\"ids\"/>";
					if (!data.dtoList[i].nbzbm)
						$.alert("检测类型信息丢失请联系管理员解决！");
					html+="<span style=\"padding-right: 25px;\"><span style=\"color:DarkOrange\">"+data.dtoList[i].xmmc+(data.dtoList[i].jczxmmc?("_"+data.dtoList[i].jczxmmc):"")+"</span>(<span style=\"color:red\">"+data.dtoList[i].nbzbm+"</span>)</span></label>";
				})
				html+="<button id='button' type='button' style='margin-left: 10px;' class='btn btn-default' onclick='checkAll()'>全选</button>";
				html+="<button id='button2' type='button' style='margin-left: 10px;' class='btn btn-default' onclick='adjustView()'>调整</button>";
				$("#ajaxForm #jclx").append(html);
			}
			if(data.sjkzxxDto) {
				if(data.sjkzxxDto.bbsdwd){
					$("#ajaxForm #bbsdwd").val(data.sjkzxxDto.bbsdwd);
				}
			}

			if(data.sjxxDto) {
				if(data.sjxxDto.kyxmmc){
					$("#ajaxForm #kyxmmc").text(data.sjxxDto.kyxmmc);
					$("#ajaxForm #kyxmmc").attr("title",data.sjxxDto.kyxmmc);
				}

				if(data.sjxxDto.xgsj){
					$("#ajaxForm #xgsj").val(data.sjxxDto.xgsj);
				}

				if(data.sjxxDto.hzxm){
					$("#ajaxForm #hzxm").text(data.sjxxDto.hzxm);
					$("#ajaxForm #hzxm").attr("title",data.sjxxDto.hzxm);
				}
				if(data.sjxxDto.nl){
					$("#ajaxForm #nl").text(data.sjxxDto.nl);
					$("#ajaxForm #nl").attr("title",data.sjxxDto.nl);
				}
				if(data.sjxxDto.xbmc){
					$("#ajaxForm #xbmc").text(data.sjxxDto.xbmc);
					$("#ajaxForm #xbmc").attr("title",data.sjxxDto.xbmc);
				}
				if(data.sjxxDto.sjdw){
					$("#ajaxForm #sjdw").text(data.sjxxDto.hospitalname);
					$("#ajaxForm #sjdw").attr("title",data.sjxxDto.hospitalname);
				}
				if(data.sjxxDto.sjys){
					$("#ajaxForm #sjys").text(data.sjxxDto.sjys);
					$("#ajaxForm #sjys").attr("title",data.sjxxDto.sjys);
				}
				if(data.sjxxDto.zyh){
					$("#ajaxForm #zyh").text(data.sjxxDto.zyh);
					$("#ajaxForm #zyh").attr("title",data.sjxxDto.zyh);
				}
				if(data.sjxxDto.sjid){
					$("#ajaxForm #sjid").val(data.sjxxDto.sjid);
				}
				if(data.sjxxDto.nbbm && !ad_flag){
					$("#ajaxForm #nbbm").val(data.sjxxDto.nbbm.trim());
				}
				if(data.sjxxDto.yblxmc){
					$("#ajaxForm #yblxmc").text(data.sjxxDto.yblxmc);
					$("#ajaxForm #yblxmc").attr("title",data.sjxxDto.yblxmc);
				}
				if(data.sjxxDto.yblxdm){
					$("#ajaxForm #yblxdm").val(data.sjxxDto.yblxdm);
				}
				if(data.sjxxDto.bz){
					$("#ajaxForm #bz").val(data.sjxxDto.bz);
				}
				if(data.sjxxDto.jstj){
					$("#ajaxForm #jstj").val(data.sjxxDto.jstj);
				}
				if(data.sjxxDto.ksmc){
					$("#ajaxForm #ks").text(data.sjxxDto.ksmc);
					$("#ajaxForm #ks").attr("title",data.sjxxDto.ksmc);
				}
				if(data.sjxxDto.db){
					$("#ajaxForm #db").text(data.sjxxDto.db);
					$("#ajaxForm #db").attr("title",data.sjxxDto.db);
				}
				if(data.sjxxDto.ybtj){
					$("#ajaxForm #ybtj").text(data.sjxxDto.ybtj);
					$("#ajaxForm #ybtj").attr("title",data.sjxxDto.ybtj);
				}
				if(data.sjxxDto.jcxmmc){//TODO
					$("#ajaxForm #jcxm").text(data.sjxxDto.jcxmmc);
					$("#ajaxForm #jcxm").attr("title",data.sjxxDto.jcxmmc);
				}
				if(data.sjxxDto.dh){
					$("#ajaxForm #dh").text(data.sjxxDto.dh);
					$("#ajaxForm #dh").attr("title",data.sjxxDto.dh);
				}
				if(data.sjxxDto.ysdh){
					$("#ajaxForm #ysdh").text(data.sjxxDto.ysdh);
					$("#ajaxForm #ysdh").attr("title",data.sjxxDto.ysdh);
				}
				if(data.sjxxDto.jqyy){
					$("#ajaxForm #jqyy").text(data.sjxxDto.jqyy);
					$("#ajaxForm #jqyy").attr("title",data.sjxxDto.jqyy);
				}
				if(data.sjxxDto.lczz){
					$("#ajaxForm #lczz").text(data.sjxxDto.lczz);
					$("#ajaxForm #lczz").attr("title",data.sjxxDto.lczz);
				}
				if(data.sjxxDto.qqjcmc){
					$("#ajaxForm #qqjcmc").text(data.sjxxDto.qqjcmc);
					$("#ajaxForm #qqjcmc").attr("title",data.sjxxDto.qqjcmc);
				}
				if(data.sjxxDto.qqzd){
					$("#ajaxForm #qqzd").text(data.sjxxDto.qqzd);
					$("#ajaxForm #qqzd").attr("title",data.sjxxDto.qqzd);
				}
				if(data.sjxxDto.gzbymc){
					$("#ajaxForm #gzbymc").text(data.sjxxDto.gzbymc);
					$("#ajaxForm #gzbymc").attr("title",data.sjxxDto.gzbymc);
				}
				if(data.sjxxDto.print_flg){
					$("#ajaxForm #print_flg").val(data.sjxxDto.print_flg);
				}
				if(data.sjxxDto.kdfy){
					$("#ajaxForm #kdfy").val(data.sjxxDto.kdfy);
				}
				if(data.sjxxDto.kdh){
					$("#ajaxForm #kdh").val(data.sjxxDto.kdh);
				}
				if(data.sjxxDto.jcdwmc){
					$("#ajaxForm #jcdwmc").val(data.sjxxDto.jcdwmc);
				}
				if(data.sjxxDto.jcdw){
					$("#ajaxForm #jcdw").val(data.sjxxDto.jcdw);
				}
				if(data.sjxxDto.sjqfmc){
					$("#ajaxForm #sjqfmc").val(data.sjxxDto.sjqfmc);
				}
				if(data.sjxxDto.yblx_flg){
                    $("#ajaxForm #yblx_flg").val(data.sjxxDto.yblx_flg);
                }
				var readOnly = false;
				if(data.sjxxDto.yblxmc!=null && (data.sjxxDto.yblxmc.indexOf('血') >= 0 || data.sjxxDto.yblxmc.indexOf('骨髓') >= 0)){
					readOnly = true;
				}
				// 初始化标本状态
				$("#ybztDiv input[type='radio']").each(function(index){
					if(this.dataset.ztbj == "B"){
						if(readOnly){
							$(this).removeAttr("checked");
							$(this).attr("disabled","disabled");
						}else{
							$(this).removeAttr("disabled","disabled");
						}
					}
				});
				// 初始化标本状态
				tjbz(null);
			} else {
				$.error("此标本不存在");
			}
		}
	});
}

function connectMessage(key) {
	var source = null;
	// 用时间戳模拟登录用户
	if (!!window.EventSource) {
		// 建立连接
		source = new EventSource("/sseEmit/connect/pagedataOA?access_token=" + $("#ac_tk").val()+"&key="+key);
		/**
		 * 连接一旦建立，就会触发open事件
		 * 另一种写法：source.onopen = function (event) {}
		 */
		source.addEventListener('open', function (e) {
			console.log("建立连接。。。");
		}, false);
		/**
		 * 客户端收到服务器发来的数据
		 * 另一种写法：source.onmessage = function (event) {}
		 */
		source.addEventListener('message', function (e) {
			setMessageInnerHTML(e.data);
		});
		/**
		 * 如果发生通信错误（比如连接中断），就会触发error事件
		 * 或者：
		 * 另一种写法：source.onerror = function (event) {}
		 */
		source.addEventListener('error', function (e) {
			if (e.readyState === EventSource.CLOSED) {
				console.log("连接关闭");
			} else {
				closeSse();
			}
			$("#ajaxForm #timeOver").removeAttr("style");
			$.ajax({
				url: "/inspection/inspection/pagedataFlushedInfo",
				data: {
					"access_token": $("#ac_tk").val(),
					"key": key,
				},
				success: function (data) {
					if (data.key){
						$("#ajaxForm #key").val(data.key);
						$("#ajaxForm #dingtalkurl").val(data.dingtalkurl);
						$("#ajaxForm #switchoverurl").val(data.switchoverurl);
					}
				},
				error: function () {
					$.error("连接手机失败，请联系管理员解决原因！");
				}
			});

		}, false);
	} else {
		console.log("你的浏览器不支持SSE");
	}
	// 监听窗口关闭事件，主动去关闭sse连接，如果服务端设置永不过期，浏览器关闭后手动清理服务端数据
	window.onbeforeunload = function () {
		if($("body").attr("isxz")=='true'){
			$("body").attr("isxz","false")
			return
		}
		closeSse();
		if (!$("#ajaxForm #key").val())
			return true;
		$.ajax({
			url: "/sseEmit/connect/pagedataCloseOA",
			data: {
				"access_token": $("#ac_tk").val(),
				"key": $("#ajaxForm #key").val(),
			}
		});
	};

	window.addEventListener('message', (event) => {
		$.ajax({
			url: "/sseEmit/connect/pagedataCloseOA",
			data: {
				"access_token": $("#ac_tk").val(),
				"key": $("#ajaxForm #key").val(),
			}
		});
		closeSse();
	})
	// 关闭Sse连接
	function closeSse() {
		if(source){
			source.close();
		}
		// $.ajax({
		// 	url: "/sseEmit/connect/pagedataCloseOA",
		// 	data: {
		// 		"access_token": $("#ac_tk").val(),
		// 		"userId": $("#ajaxForm #yhid").val(),
		// 	},
		// 	success: function (data) {},
		// 	error: function () {
		// 		$.error("连接手机失败，请联系管理员解决原因！");
		// 	}
		// });

		console.log("close");
	}

	// 将消息显示在网页上
	function setMessageInnerHTML( res) {
		//处理读到的数据

		var obj = JSON.parse(res);
		//显示到页面
		if (obj) {
			if (obj.sjid == $("#ajaxForm #sjid").val() && obj.key == $("#ajaxForm #key").val()){
				if (obj.fjids){
					if ( $("#ajaxForm #fjids").val()){
						$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+obj.fjids)
					}else{
						$("#ajaxForm #fjids").val(obj.fjids);
					}
					if (obj.fjcfbDtoList && obj.fjcfbDtoList.length>0){
						var html = "";
						for (let i = 0; i < obj.fjcfbDtoList.length; i++) {
							// if ($("#ajaxForm #bs").val() != "1"){
							// 	html +="<label  class=\"col-md-2 col-sm-4 col-xs-12 control-label \"></label>";
							// }else{
							// 	html +="<label class=\"col-md-2 col-sm-4 col-xs-12 control-label \">附件：</label>";
							// }
							html += "<div class=\"col-md-3 col-sm-3 col-xs-12\" id=\""+obj.fjcfbDtoList[i].fjid+"\" >";
							html += "<label>"+($("#ajaxForm #bs").val())+".</label>";
							$("#ajaxForm #bs").val($("#ajaxForm #bs").val()*1+1);
							html += " <button style=\"outline:none;margin-bottom:5px;padding:0px;\" onclick=\"xztemporary(\'"+obj.fjcfbDtoList[i].wjlj+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\" class=\"btn btn-link\" type=\"button\" title=\"下载\">";
							if (obj.fjcfbDtoList[i].wjm.length>10){
								html += " <span title='"+obj.fjcfbDtoList[i].wjm+"'>"+obj.fjcfbDtoList[i].wjm.substring(0,10)+"...</span>";
							}else{
								html += " <span title='"+obj.fjcfbDtoList[i].wjm+"'>"+obj.fjcfbDtoList[i].wjm+"</span>";
							}
							html += " <span class=\"glyphicon glyphicon glyphicon-save\"></span>";
							html += " </button>";
							html += " <button title=\"删除\" class=\"f_button\" type=\"button\" onclick=\"deltemporary(\'"+obj.fjcfbDtoList[i].fjid+"\')\">";
							html += " <span class=\"glyphicon glyphicon-remove\"></span>";
							html += " </button>";
							html += " <button title=\"预览\" class=\"f_button\" type=\"button\" onclick=\"yltemporary(\'"+obj.fjcfbDtoList[i].fjid+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\">";
							html += " <span  class=\"glyphicon glyphicon-eye-open\"></span>";
							html += " </button>";
							html += "</div>";
						}
						$("#ajaxForm #redisFj").append(html);
					}
				}
			}
		}

	}

}

function flushed() {
	$("#ajaxForm #qrcode").empty();
	$("#ajaxForm #code").empty();
	if($("#ajaxForm #dingtalkurl").val()) {
		$("#ajaxForm #qrcode").qrcode({
			render: 'div',
			size: 185,
			text: $("#ajaxForm #dingtalkurl").val()
		})
	}
	if($("#ajaxForm #switchoverurl").val()) {
		$("#ajaxForm #code").qrcode({
			render: 'div',
			size: 180,
			text: $("#ajaxForm #switchoverurl").val(),
		})
	}
	connectMessage($("#ajaxForm #key").val());
	$("#ajaxForm #timeOver").attr("style","display: none;")
}

$(".bootbox-close").click(function(){
	if (!$("#ajaxForm #key").val())
		return true;
	$.ajax({
		url: "/sseEmit/connect/pagedataCloseOA",
		data: {
			"access_token": $("#ac_tk").val(),
			"key": $("#ajaxForm #key").val(),
		}
	});
});


// function sendMessage(sjid) {
// 	var key = $("#ajaxForm #key").val();
// 	$.ajax({
// 		url: "/inspection/inspection/pagedataSaveInfo",
// 		data: {
// 			"access_token": $("#ac_tk").val(),
// 			"key": key,
// 		},
// 		success: function (data) {},
// 		error: function () {
// 			$.error("连接手机失败，请联系管理员解决原因！");
// 		}
// 	});
// }

function yltemporary(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid+"&temporary=temporary"
		$.showDialog(url,'图片预览',JPGMaterConfig);
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
function yl_print(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin+1,end);
	if(type.toLowerCase()=="jpg" || type.toLowerCase()=="jpeg" || type.toLowerCase()=="jfif" || type.toLowerCase()=="png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterPrintConfig);
	}else if(type.toLowerCase()=="pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var JPGMaterPrintConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		print : {
			label: "打 印",
			className: "btn-primary",
			callback: function () {
				var fjid = $('#pripreviewForm #fjid').val();
				window.open('/ws/printView?fjid='+fjid)
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function xztemporary(wjlj,wjm){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="wjlj" value="'+wjlj+'"/>' +
		'<input type="text" name="wjm" value="'+wjm+'"/>' +
		'<input type="text" name="temporary" value="temporary"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}

function deltemporary(fjid){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			$("#ajaxForm #redisFj #"+fjid).remove();
			var strings = $("#ajaxForm  #fjids").val().split(",");
			if (null!= strings && strings.length>0){
				var string = "";
				for (let i = 0; i < strings.length; i++) {
					if (strings[i] != fjid){
						string += ","+strings[i];
					}
				}
				$("#ajaxForm  #fjids").val(string.substring(1));
			}
		}
	});
}


function adjustView(){
	var sjid = $("#ajaxForm #sjid").val();
	var nbbm = $("#ajaxForm #nbbm").val();
	if(sjid && nbbm){
		var yblxdm = nbbm.substring(nbbm.length -1)
		var url="/inspection/inspection/pagedataAjustView?sjid="+sjid+"&yblxdm="+yblxdm+"&jcdwmc="+$("#ajaxForm #jcdwmc").val()+"&jcdw="+$("#ajaxForm #jcdw").val();
		$.showDialog(url,'调整',ajustViewConfig);
	}
}
/*查看异常清单模态框*/
var ajustViewConfig = {
	width		: "1200px",
	modalName	: "ajustViewModal",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#ajustView_form #info_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length<1){
					$.error("请选中一行");
					return false;
				}
				var json = [];
				$.each(sel_row,function(i){
					var sz = {"dyid": '', "sjid": '', "jclxid": '', "jcxmid": '', "jczxmid": '', "nbzbm": '', "jcdw": ''};
					sz.dyid = sel_row[i].dyid;
					sz.sjid = $("#ajustView_form #id").val();
					sz.jcdw = $("#ajustView_form #jcdw").val();
					sz.jclxid = sel_row[i].dyxx;
					sz.jcxmid = sel_row[i].yxxid;
					if (sel_row[i].zid){
						sz.jczxmid = sel_row[i].zid;
					}else{
						sz.jczxmid = null;
					}
					sz.nbzbm = sel_row[i].dydm;
					json.push(sz);
				})
				$("#ajustView_form #json").val(JSON.stringify(json));
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajustView_form input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajustView_form",function(responseText,statusText){
					if(responseText["status"] == 'success'){

						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							getInspection(true);
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert("不好意思！出错了！");
					}
				},".modal-footer > button");
				return true;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function openException(){
	var sjid = $("#ajaxForm #sjid").val();
	var url="/exception/exception/addException?ywid="+sjid;
	$.showDialog(url,'异常清单',exceptionListConfig);
}
// /**
//  * 内网打印机
//  * @returns
//  */
// $("#ajaxForm #local_ip").click(function(){
// 	$("#ajaxForm #remoteDiv").hide();
// 	$("#ajaxForm #glxx").attr("disabled","disabled");
// })
//
// /**
//  *
//  * @returns
//  */
// $("#ajaxForm #remote_ip").click(function(){
// 	$("#ajaxForm #remoteDiv").show();
// 	$("#ajaxForm #glxx").removeAttr("disabled");
// })
function localIpCheck() {
	$("#ajaxForm #remoteDiv").hide();
	$("#ajaxForm #glxx").attr("disabled","disabled");
}

function remoteIpCheck() {
	$("#ajaxForm #remoteDiv").show();
	$("#ajaxForm #glxx").removeAttr("disabled");
}

function printerIpChecked(){
	if($("#ajaxForm #local_ip").attr("checked")){
		$("#ajaxForm #remoteDiv").hide();
		$("#ajaxForm #glxx").attr("disabled","disabled");
	}else if($("#ajaxForm #remote_ip").attr("checked")){
		$("#ajaxForm #remoteDiv").show();
		$("#ajaxForm #glxx").removeAttr("disabled");
	}
}

var vm = new Vue({
	el:'#confirm',
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


function tjbz(obj){
	var chk_value =[];//定义一个数组  
    $('input[name="zts"]:checked').each(function(){//遍历每一个名字为zts的复选框，其中选中的执行函数  
    chk_value.push($(this).val());//将选中的值添加到数组chk_value中  
    });
    var ybzts="";
    for(var i=0;i<chk_value.length;i++){
	 	var ybzt=$("#"+chk_value[i]).next().text();	 	
	 	ybzts = ybzts+"["+ybzt+"]";
   	}
	var bz=$("#ajaxForm #bz").val();
	var head = bz.indexOf('[');
	var end = bz.lastIndexOf(']');
	if(head!="-1"&&end!="-1"){
		var text=bz.replace(bz.substring(head,end+1),"");
		$("#ajaxForm #bz").val(text+ybzts)
	}else{
		$("#ajaxForm #bz").val(bz+ybzts);
	}
}

var libPreInput;
var libSecondPreInput;
$("#ajaxForm input").on('keydown',function(e){
    if(e.keyCode != 13 && e.keyCode != 16&& e.keyCode != 17&& e.keyCode != 74&& e.keyCode != 37){
        return true;
    }
    libSecondPreInput = libPreInput
    libPreInput = e.keyCode
    return false;
});
document.onkeydown=function(e){
    if(e.keyCode==123)
        return false;
};
var switchoverflag = 2;
function switchover(){
	if (switchoverflag == 1){
		$("#ajaxForm #text").show();
		$("#ajaxForm #textD").hide();
		switchoverflag= 2;
		$("#ajaxForm #code").show();
		$("#ajaxForm #qrcode").hide();
	}else{
		$("#ajaxForm #textD").show();
		$("#ajaxForm #text").hide();
		switchoverflag= 1;
		$("#ajaxForm #qrcode").show();
		$("#ajaxForm #code").hide();
	}
}

$(document).ready(function(){
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"sjxx_file",$("#ajaxForm #ywlx").val());
    btnBind();
    init();
    //所有下拉框添加choose样式
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
    printerIpChecked();
    if ($("#ajaxForm #key").val()){
		if($("#ajaxForm #dingtalkurl").val()) {
			$("#ajaxForm #qrcode").qrcode({
				render: 'div',
				size: 185,
				text: $("#ajaxForm #dingtalkurl").val()
			})
		}
		if($("#ajaxForm #switchoverurl").val()) {
			$("#ajaxForm #code").qrcode({
				render: 'div',
				size: 180,
				text: $("#ajaxForm #switchoverurl").val(),
			})
		}
		connectMessage($("#ajaxForm #key").val());
	}
    
});

/**
 * 点击显示文件上传
 * @returns
 */
function confirmEditfile(){
	$("#ajaxForm #fileDiv").show();
	$("#ajaxForm #file_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function confirmCancelfile(){
	$("#ajaxForm #fileDiv").hide();
	$("#ajaxForm #file_btn").show();
}

$('#confirmInspectionModal').on('hide.bs.modal', function (e) {
  // 模态框右上角叉叉关闭模态框时关闭长链接
  $.ajax({
        url: "/sseEmit/connect/pagedataCloseOA",
        data: {
            "access_token": $("#ac_tk").val(),
            "key": $("#ajaxForm #key").val(),
        }
    });
});