var socket;

// 初始化页面
function initLabPage() {
	if(socket != null){
		socket.close();
	}
	connect();
	
	// 初始化实验室背景图
	var fjid = $(".outdiv #fjid").val();
	if(fjid){
		$(".outdiv").css("background-image","url(/common/file/getFileInfo?fjid="+fjid+"&access_token="+$("#ac_tk").val()+")");
		$(".outdiv").css("background-size","100% 100%");
	}
}

// 创建socket对象并绑定所有事件 
function connect() {
	// 创建socket 测试地址：echo.websocket.org
	if ('WebSocket' in window) {
		socket = new WebSocket('ws://172.17.52.96:8095/ws');
	} else if ('MozWebSocket' in window) {
		socket = new MozWebSocket('ws://172.17.52.96:8095/ws');
	} else {
		console.log('Unsupported.');
		return;
	}
	$("#divs").append("<span style='color:black;display:block;'>socket状态：" + socketStatus(socket.readyState) + "</span>");
	// Web socket事件监听
	// 在出现问题时触发
	socket.onerror = function(event) {
		$("#divs").append("<span style='color:black;display:block;'>onerror " + event.data + "</span>");
	};
	// 在建立连接后触发
	socket.onopen = function(event) {
		$("#divs").append("<span style='color:black;display:block;'>onopen 与服务器端建立连接</span>");
		$("#divs").append("<span style='color:black;display:block;'>socket状态：" + socketStatus(socket.readyState) + "</span>");
	}; 
	// 在页面从服务器接收到消息时触发
	socket.onmessage = function(event) {
		$("#divs").append("<span style='color:black;display:block;'>onmessage " + event.data + "</span>");
	};
	// 连接关闭时触发
	socket.onclose = function(event) {
		$("#divs").append("<span style='color:black;display:block;'>onclose 与服务器端断开连接</span>");
		$("#divs").append("<span style='color:black;display:block;'>socket状态：" + socketStatus(socket.readyState) + "</span>");
	};
}

// 发送消息按钮
function sendMassage() {
	if(socket.readyState == 3){
		connect();
	}else{
		socket.send('测试消息！----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 结束！');
	}
}

// 断开连接按钮
function end() {
	socket.close();
	$("#divs").append("<span style='color:black;display:block;'>socket状态：" + socketStatus(socket.readyState) + "</span>");
}

// 四种状态：CONNECTING 0、OPEN 1、CLOSING 2、CLOSED 3
function socketStatus(status){
	if(status == 0){
		return "CONNECTING";
	}else if(status == 1){
		return "OPEN";
	}else if(status == 2){
		return "CLOSING";
	}else if(status == 3){
		return "CLOSED";
	}
}

// 发送netty消息按钮
function sendNettyMassage(type) {
	// 获取扩展字段
	$.ajax({
	    type:'post',  
	    url:"/lashome/home/testNettyMsg", 
	    cache: false,
	    data: {"msg":"测试消息","access_token":$("#ac_tk").val(),"type":type},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	if(data.status == "success"){
	    		$.alert("发送成功！");
	    	}else{
	    		$.alert("发送失败！ "+data.message);
	    	}
	    }
	});
}

function closeNettyMessage(type) {
	// 获取扩展字段
	$.ajax({
	    type:'post',  
	    url:"/lashome/home/closeNettyMessage", 
	    cache: false,
	    data: {"msg":"测试消息","access_token":$("#ac_tk").val(),"type":type},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	if(data.status == "success"){
	    		$.alert("发送成功！");
	    	}else{
	    		$.alert("发送失败！ "+data.message);
	    	}
	    }
	});
}
//TODO控制它的点击事件
$(".outdiv .consoleTab").on("click",function(){
//	debugger
	var url = "/lashome/home/labConfigView?access_token="+$("#ac_tk").val();
	$.showDialog(url,'实验室信息设置',viewxxxDeviceConfig);
});
//TODO要修改模态框，这个是之间复制的
/**
 * 编辑设备信息
 */
function yqEditConfig(yqid){
	var url = "/lashome/home/labConfigView?access_token="+$("#ac_tk").val();
	$.showDialog(url,'实验室信息设置',viewxxxDeviceConfig);
}
var viewxxxDeviceConfig = {
		width		: "800px",
		modalName	:"viewxxxDeviceConfig",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/**
 * 初始化仪器信息
 * @returns
 */
function initLabDevice(){
	var sysid = $("#home_page #sysid").val();
	// 初始化仪器信息
	$.ajax({
		type : 'post',
		url :"/lashome/home/getDeviceList",
		cache : false,
		data : {
			"access_token" : $("#ac_tk").val(),
			"sysid" : sysid,
		},
		dataType : 'json',
		success : function(data) {
			var pageHeigh = $('.outdiv').height();
			var pageWidth = $('.outdiv').width();
			if(data.status == "success"){
				for(var i=0; i< data.yqxxDtos.length; i++){
					$('.outdiv').append("<div id='yq["+i+"]' class='device' data-fid='"+data.yqxxDtos[i].yqid+
							"' style='background-size: 100% 100%; width:"+(data.yqxxDtos[i].kd)*pageWidth+"px; height:"+(data.yqxxDtos[i].gd)*pageHeigh+"px;" +
							"top:"+(data.yqxxDtos[i].dbjl)*pageHeigh+"px; left:"+(data.yqxxDtos[i].zcjl)*pageWidth+"px;" +
							"background-image:url(/common/file/getFileInfo?fjid="+data.yqxxDtos[i].fjid+"&access_token="+$("#ac_tk").val()+");' " +
							"onclick=\"yqEditConfig('"+data.yqxxDtos[i].yqid+"')\"></div>");
//					onclick='"+yqEditConfig(data.yqxxDtos[i].yqid)+";'
//					$('#yq['+i+']').css("top",(data.yqxxDtos[i].dbjl)*100+"px");
//					$('#yq['+i+']').css("left",(data.yqxxDtos[i].zcjl)*100+"px");
				}
			}
		}
	});
}

/**
 * 初始化位置尺寸信息
 * @returns
 */
function initInformation(){
	//设置背景的长宽比
	var outdivHeight = $('.outdiv').height();
	$('.outdiv').css("height",outdivHeight+"px");
	$('.outdiv').css("width",outdivHeight*1.5+"px");
//	var outdivHeight = $('.outdiv').height();
//	$('.outdiv').css("height",$('.outdiv').height()+"px");
//	$('.outdiv').css("width",$('.outdiv').height()*1.5+"px");
//	alert($('.outdiv').height());
//	alert($('.outdiv').width());
	//控制台的长宽和位置
	$('.consoleTab').css("height",$('.outdiv').height()*0.35+"px");
	$('.consoleTab').css("width",$('.outdiv').width()*0.3+"px");
	$('.consoleTab').css("left",($('.outdiv').width()-$('.consoleTab').width())*0.5+"px");
	$('.consoleTab').css("top",$('.outdiv').width()*0.4+"px");
	
}


function xliney(){
	//测试的代码
//    debugger
//    var startX = $(".one").offset().left + $(".one").width();
//    var startY = $(".one").offset().top + $(".one").height() / 2;
//    var endX = $(".two").offset().left;
//    var endY = $(".two").offset().top + $(".two").height() / 2;
    
    var startX = $(".one").position().left + $(".one").width()*0.5;
    var startY = $(".one").position().top;
    var endX = $(".two").position().left + $(".two").width()*0.5;
    var endY = $(".two").position().top;
    console.log(startX, startY, endX, endY);
    
//	  debugger
	  $("#line").attr({ x1: startX, y1: startY, x2: endX, y2: endY });
//	  var flag = false;
//	  $(".two")[0].onmousedown = function() {
//	      flag = true;
//	  };
//	  document.onmousemove = function() {
//	      if (flag == true) {
//	        // console.log("111111111");
//	        var endX = event.clientX;
//	        var endY = event.clientY;
//	        // console.log("endX", endX);
//	        // console.log("endY", endY);
//	        $(".two").css({ left: endX, top: endY });
//	        $("#line").attr({ x1: startX, y1: startY, x2: endX, y2: endY });
//	      }
//	    };
//	    document.onmouseup = function() {
//	        flag = false;
//	    };
	   // $("#line").attr({ x1: "100", y1: "100", x2: "500", y2: "520" });
}

$(document).ready(function() {
	// 初始化页面
	initLabPage();
	//初始化仪器信息
	initLabDevice();
	//初始化长宽位置等信息
	initInformation();
	// 所有下拉框添加choose样式
	jQuery('.outdiv .chosen-select').chosen({ width : '100%' });
	
	//求箭头位置的中心
	var aleft = $('.divleft').offset().left + $('.divleft').width()*0.5;
	var aleft2 = $('.divleft').position().left + $('.divleft').width()*0.5;
	var abootom = $('.divleft').position().top + $('.divleft').height();
//	alert(aleft);
	$('.arrowleftSsm').css("left",aleft+"px");
	$('.arrowleftSsm').css("left",aleft2+"px");
//	$('.arrowleftSsm').css("top",abootom+"px");
	
    xliney()
});