var _load=$.fn.load;
var vueid="";

$.fn.extend({
	load:function(url,param,calbck){
		//其他操作和处理
		//..
		//此处用apply方法调用原来的load方法，因为load方法属于对象，所以不可直接对象._load（...）
		if(!param){
			param = {};
		}
		param["access_token"] = $("#ac_tk").val();
		return _load.apply(this,[url,param,calbck]);
	}
});

function loaddruid(){
	var turnForm = document.createElement("form");
	//一定要加入到body中！！
	document.body.appendChild(turnForm);
	turnForm.method = 'post';
	turnForm.action = "/druid";
	var newElement = document.createElement("input");
	newElement.setAttribute("name","access_token");
	newElement.setAttribute("type","hidden");
	newElement.setAttribute("value",$("#ac_tk").val());
	turnForm.appendChild(newElement);
	turnForm.submit();
	return true;
}

function logout(){
	var turnForm = document.createElement("form");
	//一定要加入到body中！！
	document.body.appendChild(turnForm);
	turnForm.method = 'post';
	turnForm.action = "/logout";
	var newElement = document.createElement("input");
	newElement.setAttribute("name","access_token");
	newElement.setAttribute("type","hidden");
	newElement.setAttribute("value",$("#ac_tk").val());
	turnForm.appendChild(newElement);
	turnForm.submit();
	return true;
}

function personalData(){
	var url= "/systemrole/user/pagedataModPersonalData?yhid=" +$("#yhid").val();
	$.showDialog(url,'个人信息',modPersonalConfig);
}

function resetPass(){
	var url= "/systemrole/user/pagedataModPass?yhid=" +$("#yhid").val();
	$.showDialog(url,'修改密码',modPassConfig);
}

function EasySetup(){
	var url= "/systemrole/user/pagedataModSettingConfirmer?yhid=" +$("#yhid").val();
	$.showDialog(url,'简便设置',modSettingConfirmerConfig);
}

var modPersonalConfig = {
	width		: "800px",
	height		: "500px",
	modalName	: "modPersonalDataModal",
	formName	: "ajaxForm_t",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm_t").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm_t input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm_t",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$.closeModal(opts.modalName);
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var modPassConfig = {
	width		: "800px",
	modalName	: "modPassModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				if($("#mm").val() == $("#ymm").val()){
					$.error("新密码与原密码一致，请重新输入！");
					return false;
				}
				if($("#mm").val() != $("#yzmm").val()){
					$.error("新密码输入不一致,请重新输入！");
					return false;
				}
				if($("#mm").val().length>18){
					$.error("密码超过限制长度 18 位！");
					return false;
				}else if(JudgePwdStrong($("#mm").val())<3){
					$.error("密码必须为8 ~ 18 个字符，其中包括以下至少三种字符: 大写字母、小写字母、数字和符号！");
					return false;
				}

				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$.closeModal(opts.modalName);
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//密码强度判断
function JudgePwdStrong(pwd) {
	var lev = 0;
	//如果密码长度为0，强度为零
	if (pwd.length == 0) {
		lev = 0;
		return lev;
	}
	//如果密码长度小于8，强度为一
	if (pwd.length < 8) {
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
var modSettingConfirmerConfig = {
	width		: "800px",
	modalName	: "modSettingConfirmerModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if(!$("#ajaxForm #szz").val()){
					$.error("请按照提示或选择按钮中选择人员");
					return false;
				}
				if($("#ajaxForm #szz").val() == $("#ajaxForm #yhid").val()){
					$.error("确认人不能为本人！");
					return false;
				}
				$("#ajaxForm #glxx").val($("#ajaxForm #glxx").val()+"＆"+$("#ajaxForm #ddid").val());
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$.closeModal(opts.modalName);
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

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

$("a[name='menus']").unbind("click").click(function(){
	$(".menu-navbar ul").removeClass("active");
	$(this.parentElement.parentElement).addClass("active");
	var menuid=jQuery(this).attr("gm-id");
	$.ajax({
		type:'post',
		url:jQuery(this).attr("gm-url"),
		cache: false,
		data: {"menuId":jQuery(this).attr("gm-id"),"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				var isFirst = true;
				var s_html="";
				$(".gm-left-nav-ul").html("");
				$.each(data,function(i){
					var cdcc = data[i].cdcc;
					if(cdcc==2){
						if(s_html!="" && !isFirst){
							s_html += "</ul>";
							s_html += "</li>";
						}
						s_html +='<li class="left-nav-root"><a href="javascript:void(0)"><span class="menu-first-font">' + data[i].btsx;
						s_html +='</span><span>' + data[i].zybt + '</span><i class="glyphicon glyphicon-chevron-right"></i></a>';
						isFirst =true;
					}else{
						if(isFirst){
							s_html +='<ul class="">';
						}
						if(data[i].zylj.indexOf("?") <0)
							s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '?access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid+'&_fjd='+data[i].fjd + '"';
						else
							s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '&access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid+'&_fjd='+data[i].fjd + '"';

						s_html +='href="?access_token='+ $("#ac_tk").val() + '&zyid=' + data[i].zyid + '&_fjd='+menuid+'"'+' title="' + data[i].zybt + '" ' ;
						s_html += "onclick= 'return zzHref(\""+data[i].zylj+"\")'"
						s_html += '"'+'>' + data[i].zybt + '</a>';
						s_html +='</li>';
						isFirst = false;
					}
				});
				if(s_html!="" && !isFirst){
					s_html += "</ul>";
					s_html += "</li>";
				}
				$(".gm-left-nav-ul").html(s_html);
			}else{
				$.alert("未获取到菜单信息！");
				$(".gm-left-nav-ul").html("");
			}
		},
		error:function(){
			alert("请求失败")
		}
	});
});

var overscroll = function(el) {
	el.addEventListener('touchstart', function() {
		var top = el.scrollTop
			, totalScroll = el.scrollHeight
			, currentScroll = top + el.offsetHeight;
		if(top === 0) {
			el.scrollTop = 1;
		} else if(currentScroll === totalScroll) {
			el.scrollTop = top - 1;
		}
	});
	el.addEventListener('touchmove', function(evt) {
		if(el.offsetHeight < el.scrollHeight)
			evt._isScroller = true;
	});
}
overscroll(document.querySelector('.left-nav'));
document.body.addEventListener('touchmove', function(evt) {
	if(evt._isScroller &&!evt._isScroller) {
		evt.preventDefault();
	}
});

$(function(){
	$(".menu-navbar ul:first").addClass("active");

	$(".left-nav-root a").each(function(){
		var hrefurl=$(this).attr("href");
		$(this).attr("href",hrefurl+"&_fjd="+$(".menu-navbar ul li a:first").attr("gm-id"))
	})
});

window.onload = function () {
	if ($("#flag").val() == "checkRole")
		location.href = "/?access_token="+$("#ac_tk").val();
}