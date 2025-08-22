//检查两次输入密码是否一致
function checkmm(){
	var mm=$("#input-2").val();
	var yzmm=$("#input-4").val();
	if(yzmm!="" &&mm!=""){
		if(!(mm==yzmm)){
			$("#yzmmts .ts_text").text("密码输入不一致,请重新输入！");
		}
	}
}

//鼠标选中时，去掉提示内容
function clearmm(){
	var mm=$("#input-2").val();
	var yzmm=$("#input-4").val();
	if(yzmm!="" &&mm!=""){
		$("#yzmmts .ts_text").text("");
	}
	$("#mmts .ts_text").text("");
}
function clearymm(){
	$("#ymmts .ts_text").text("")
}
function clearyzymm(){
	$("#yzmmts .ts_text").text("")
}
//点击图标显示密码
function xsmm(){
	$("#input-2").attr("type", "text");
};
function ycmm(){
	$("#input-2").attr("type", "password");
};
//点击图标显示密码
function xsyzmm(){
	$("#input-4").attr("type", "text");
};
function ycyzmm(){
	$("#input-4").attr("type", "password");
};

//确定按钮，提交
function checkpwd(){
	if(!checkPassWord()){
		return;
	}else{
		$.ajax({ 
		    type:'post',  
		    url:"/client/updatePwd", 
		    data: $("#pwd_ajaxform").serialize(), 
		    dataType:'json', 
		    success:function(map){
		    	//返回值
		    	if(map.status=="success"){
		    		$.confirm("修改成功！请重新登录",function(){
		    			window.location.href="/";
			    	});
		    	}else if(map.status=="fail"){
		    		$.error(map.message);
		    	}
		    }
		});
	}
}
//密码校验
function checkPassWord(){
	var isSuccess = false;
	if($("#input-2").val()==""){
		$("#mmts .ts_text").text("请输入新密码！");
	}else if($("#input-4").val()==""){
		$("#yzmmts .ts_text").text("请确认密码！");
	}else if($("#input-4").val()!=$("#input-2").val()){
		$("#yzmmts .ts_text").text("新密码输入不一致,请重新输入！");
	}else if(JudgePwdStrong($("#input-2").val())<3){
		$("#mmts .ts_text").text("密码必须为8 ~ 18 个字符，其中包括以下至少三种字符: 大写字母、小写字母、数字和符号！");
	}else if($("#input-2").val().length>18){
		$("#mmts .ts_text").text("密码超过限制长度 18 位！");
	}else{
		isSuccess = true;
	}
	return isSuccess;
}
function keysub(evt){
	  var evt=evt?evt:(window.event?window.event:null);//兼容IE和FF
	  if (evt.keyCode==13){
		  checkpwd();
	}
}

$( document ).ready(function() {
  const changeText = function (el, text, color) {
    el.text(text).css('color', color);
  };
  $("#input-2").keyup(function(){
	 var len = this.value.length;
	 var pwd = $("#input-2").val();
	  const pbText = $('.form-2 .progress-bar_text');
	  var strong = JudgePwdStrong(pwd);
	  if (strong == 0) {
		  $('.form-2 .progress-bar_item').each(function () {
			  $(this).removeClass('active')
		  });
		  $('.form-2 .active').css('background-color', 'transparent');
		  changeText(pbText, 'Password is blank');
	  } else if (strong==1) {
		  $('.form-2 .progress-bar_item-1').addClass('active');
		  $('.form-2 .progress-bar_item-2').removeClass('active');
		  $('.form-2 .progress-bar_item-3').removeClass('active');
		  $('.form-2 .progress-bar_item-4').removeClass('active');
		  changeText(pbText, '密码强度弱','#FF4B47');
	  } else if (strong==2) {
		  $('.form-2 .progress-bar_item-1').addClass('active');
		  $('.form-2 .progress-bar_item-2').addClass('active');
		  $('.form-2 .progress-bar_item-3').removeClass('active');
		  $('.form-2 .progress-bar_item-4').removeClass('active');
		  changeText(pbText, '密码强度一般','#F9AE35');
	  } else if (strong==3){
		  $('.form-2 .progress-bar_item-1').addClass('active');
		  $('.form-2 .progress-bar_item-2').addClass('active');
		  $('.form-2 .progress-bar_item-3').addClass('active');
		  $('.form-2 .progress-bar_item-4').removeClass('active');
		  changeText(pbText, '密码强度强','#00d0ec');
	  } else {
		  $('.form-2 .progress-bar_item').each(function () {
			  $(this).addClass('active');
		  });
		  changeText(pbText, '密码强度非常强','#2DAF7D');
	  }
	  if(pwd.length>18){
		  changeText(pbText, '密码超过限制长度 18 位！','#FF4B47')
	  }
  });
});

//密码强度判断
function JudgePwdStrong(pwd) {
	var lev = 0;
	//如果密码长度为0，强度为零
	if (pwd.length == 0) {
		lev = 0;
		return lev;
	}
	//如果密码长度小于8，强度为一
	if (pwd.length < 8 || pwd.length > 18) {
		lev = 1;
		return lev;
	}
	//如果密码中存在数字，强度加一
	if (pwd.match(/\d+/g)) {
		lev++;
	}
	//如果密码中存在小写字母，强度加一
	if (pwd.match(/[a-z]+/g)) {
		lev++;
	}
	//如果密码中存在大写字母，强度加一
	if (pwd.match(/[A-Z]+/g)) {
		lev++;
	}
	//如果密码中存在特殊字符，强度加一
	if (pwd.match(/[^a-zA-Z0-9]+/g)) {
		lev++;
	}
	return lev;
}