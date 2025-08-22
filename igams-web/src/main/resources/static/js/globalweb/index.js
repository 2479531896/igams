var _load=$.fn.load;
var vueid="";
var isFull=false;
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
function dealSpaceQuery(idStr){
	// debugger
	//去除字符中所有空格
	// $(idStr).val($(idStr).val().replaceAll(" ",""));
	//截取字符串第一个空格之前的数据进行返回，，$(idStr).val().indexOf(" ")<=0，即不存在空格或第一个字符就是空格，此时不做处理，维持原字符串
	if ($(idStr).val().indexOf(" ") > 0){
		$(idStr).val(    $(idStr).val().substring( 0 , $(idStr).val().indexOf(" ") )    );
	}
}
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
	//清除cookie
	var exp = new Date()
	exp.setTime(exp.getTime() - 1)
	removeCookie('TOKEN.'+ $('#cookiekey').val()+';domain='+$('#domain').val()+';path=/;expires='+exp.toGMTString())
	var newElement = document.createElement("input");
	newElement.setAttribute("name","access_token");
    newElement.setAttribute("type","hidden");
    newElement.setAttribute("value",$("#ac_tk").val());
    turnForm.appendChild(newElement);
	turnForm.submit();
	return true;
}
function removeCookie(name) {
	var exp = new Date()
	exp.setTime(exp.getTime() - 1)
	document.cookie = `${name}=;expires=${exp.toGMTString()}`
}
function personalData(){
	var url= "/systemrole/user/pagedataModPersonalData?yhid=" +$("#yhid").val();
	$.showDialog(url,'个人信息',modPersonalConfig);
}
function subscribeMessage(){
	var url= "/systemrole/user/pagedataGetMessage?yhid=" +$("#yhid").val()+"&zsxm="+$("#zsxm").val();
	$.showDialog(url,'消息订阅',messageConfig);
}
function resetPass(){
	var url= "/systemrole/user/pagedataModPass?yhid=" +$("#yhid").val();
	$.showDialog(url,'修改密码',modPassConfig);
}

function EasySetup(){
	var url= "/systemrole/user/pagedataModSettingConfirmer?yhid=" +$("#yhid").val();
	$.showDialog(url,'简便设置',modSettingConfirmerConfig);
}
var messageConfig = {
	width		: "1200px",
	modalName	: "messageModal",
	formName	: "setGrMessageForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
//	buttons		: {
//		success : {
//			label : "确 定",
//			className : "btn-primary",
//			callback : function() {
//				var $this = this;
//				var opts = $this["options"]||{};
//
//				$("#setMessageForm input[name='access_token']").val($("#ac_tk").val());
//
//				submitForm(opts["formName"]||"setMessageForm",function(responseText,statusText){
//					if(responseText["status"] == 'success'){
//						$.success(responseText["message"],function() {
//							$.closeModal(opts.modalName);
//						});
//					}else if(responseText["status"] == "fail"){
//						$.error(responseText["message"],function() {
//						});
//					} else{
//						$.alert(responseText["message"],function() {
//						});
//					}
//				});
//				return false;
//			}
//		},
//		cancel : {
//			label : "关 闭",
//			className : "btn-default"
//		}
//	}
};

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
				$("#ajaxForm #glxx").val($("#ajaxForm #glxx").val());
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				if($("#ajaxForm #glxx").val()==""){
					$("#ajaxForm #szz").val("");
				}
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
				var reg= new RegExp("(^|&)dqjs=([^&]*)(&|$)", "i");
				var r = window.location.search.substr(1).match(reg);
				var dqjs="";
				if(r!=null){
					dqjs=unescape(r[2])
				}
				var
				reg= new RegExp("(^|&)yhid=([^&]*)(&|$)", "i");
				var r1 = window.location.search.substr(1).match(reg);
				var yhid="";
				if(r1!=null){
					yhid=unescape(r1[2])
				}
				var dqurl=window.location.protocol+"//"+window.location.host+"/"
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
							if(dqjs!=""&&yhid!=""){
								s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '?access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd +'&_mid='+menuid+'&dqjs='+dqjs +'&yhid='+yhid +'&_dqurl='+dqurl + '"';

							}else{
								s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '?access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd +'&_mid='+menuid +'&_dqurl='+dqurl + '"';

							}
						else
							if(dqjs!=""&&yhid!=""){
								s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '&access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd +'&_mid='+menuid +'&dqjs='+dqjs +'&yhid='+yhid +'&_dqurl='+dqurl + '"';
							}else{
								s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '&access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd +'&_mid='+menuid  +'&_dqurl='+dqurl + '"';

							}
						// s_html +=' href="javascript:tabClick(\'' ;
                        // if(data[i].zybj==null)
                        // 	s_html +='';
                        // else
                        // 	s_html +=data[i].zybj;
                        // s_html += '\')" title="' + data[i].zybt + '">' + data[i].zybt + '</a>';
						// s_html +='</li>';
						if(dqjs!=""&&yhid!=""){
							s_html +='href="?access_token='+ $("#ac_tk").val() + '&zyid=' + data[i].zyid + '&_fjd='+menuid+'&dqjs='+dqjs +'&yhid='+yhid+'&_dqurl='+dqurl + '"'+' title="' + data[i].zybt  +  '" ' ;

						}else{
							s_html +='href="?access_token='+ $("#ac_tk").val() + '&zyid=' + data[i].zyid + '&_fjd='+menuid+'&_dqurl='+dqurl+'"' + ' title="' + data[i].zybt + '" ' ;
						}
						s_html += "onclick= 'return zzHref(\""+data[i].zylj+"\",\""+data[i].zybj+"\")'"
						s_html += '>' + data[i].zybt + '</a>';
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

var tabClick = function(button){
	buttonids = button.split(',');
	if(buttonids){
		for (var i = 0, ii = buttonids.length; i < ii; ++i) {
		    //关闭TAB
		    $("#tab_tab_" + buttonids[i]).remove();
		    $("#tab_" + buttonids[i]).remove();
		}
	}
}

var clickPhoneMenu=function(url){
	var phoneNav = $('.gm-nav.top-nav').height();
	$('body').toggleClass('top-nav');
	var nav = $('.gm-phone-nav');
	if(!nav.hasClass('open')) {
		nav.addClass('open');
	} else {
		nav.removeClass('open');
	}
	$("#phonehome").load(url);
	return false;
}

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
function exit(){
	isFull = false;
	// 判断是否支持退出全屏 API
}
function fullScreen(){
	isFull = true;
	// 获取需要进入全屏的元素，例如整个文档
    var element ;
	 $("#tabs1>li>a").each(function(){
            if($(this).attr("class").indexOf("active")!=-1){
            var id=$(this).parent().attr("id")
            if(id.indexOf("tab_tab_home")==-1){
                element= $("#"+id.replace("tab_","")).get(0)
            }else{
                element= $("#home").get(0)
            }
        }
	});

	// 判断是否支持全屏 API
	if (element.requestFullscreen) {
		// 进入全屏模式
		element.requestFullscreen().then(() => {
			console.log('进入全屏模式成功');
		}).catch(err => {
			console.error('进入全屏模式失败:', err);
		});
	} else if (element.mozRequestFullScreen) { /* Firefox */
		element.mozRequestFullScreen();
	} else if (element.webkitRequestFullscreen) { /* Chrome, Safari 和 Opera */
		element.webkitRequestFullscreen();
	} else if (element.msRequestFullscreen) { /* IE/Edge */
		element.msRequestFullscreen();
	}
//	$("#top_hidden").on("mouseover",function () {
//		$(".top-nav").css("display","block");
//		$("#tabs1").css("display","block");
//		$(".gm-content>.page-content").css("position","static");
//		$(".page-content").css("width","100%");
//		$(".page-content").css("margin-left","0");
//		$("#top_hidden").css("display","none")
//		$(".tab-content").css("min-height",$(".gm-left-nav").height()-$(".nav-tabs").height());
//		$(".tab-pane").css("height",$(".gm-left-nav").height()-$(".nav-tabs").height());
//		$(".iframeClass").css("height",$(".gm-left-nav").height()-$(".nav-tabs").height()-5);
//	});
//	$(".tab-content").on("mouseover",function () {
//		$(".top-nav").css("display","none");
//		$("#tabs1").css("display","none");
//		$(".gm-content>.page-content").css("position","absolute");
//		$(".page-content").css("width","103%");
//		$(".page-content").css("margin-left","-3%");
//		$("#top_hidden").css("display","block")
//		$(".tab-content").css("min-height",$(window).height());
//		$(".tab-pane").css("height",$(window).height());
//		$(".iframeClass").css("height",$(window).height()-5);
//	});
}
function changeTop(){
	fullScreen();
}
function getBbxxList(){
	$("#bbxxList").empty();
	$.ajax({
		type:'post',
		url:"/pagedataBbxxInIndex",
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			var bbxxList = data.bbxxList
			if (bbxxList!=null && bbxxList.length>0){
				var html = '';
				for (var i = 0; i < bbxxList.length; i++) {
					html += '<div class="item">' +
								'<p>' +
									'<B>版本标签：</B><span id="VersionTag">'+bbxxList[i].bbbq+'</span>' +
									'<br>' +
									'<B>上线时间：</B><span id="UpdateTime">'+bbxxList[i].sxsj+'</span>' +
									'<br>' +
									'<B>更新内容：</B><span>'+bbxxList[i].gxnr+'</span></p>' +
									'<hr>' +
								'</p>' +
							'</div>'
				}
				$("#bbxxList").append(html);
			}

		}
	});
}

$(function(){
	$(".menu-navbar ul:first").addClass("active");
	$(".left-nav-root a").each(function(){
		var hrefurl=$(this).attr("href");
		if(hrefurl.indexOf("javascript:void(0)")<0 && hrefurl!="")
			$(this).attr("href",hrefurl+"&_fjd="+$(".menu-navbar ul li a:first").attr("gm-id"))
	})

	});

window.onload = function () {
    //aiAssistant.init();
	if ($("#flag").val() == "checkRole")
		location.href = "/?access_token="+$("#ac_tk").val();
    window.CHATBOT_CONFIG = {
              endpoint: "/chatbot/chat?access_token="+$("#ac_tk").val(),
              displayByDefault: false, // 默认不展示 AI 助手聊天框
              aiChatOptions: {
                conversationOptions: {
                  conversationStarters: [

                  ]
                },
                displayOptions: {
                  height: 600,
                },
                personaOptions: {
                  assistant: {
                    name: '你好，我是你的 AI 助手',
                    // AI 助手的图标
                    avatar: 'https://img.alicdn.com/imgextra/i2/O1CN01Pda9nq1YDV0mnZ31H_!!6000000003025-54-tps-120-120.apng',
                    tagline: '如果遇到任何问题或者需要建议，随时找我聊聊~',
                  }
                }
              },
              dataProcessor: {
                rewritePrompt(prompt) {
                  return prompt;
                }
              }
            };
}
