var _url=window.location.href;

$(function() {


	var skin=localStorage.lastname;
	if(skin !=undefined){
		$('.gm-global-bg').attr('class','').addClass('gm-global-bg '+skin+'');
	}

	//调用tips插件
//	$('.gm-nav.top-nav [data-bs-toggle="tooltip"]').tooltip();

	$(document).off('click', '#gm-sy-search').on('click', '#gm-sy-search', function() {

		//搜索框
		$(this).addClass('open');
	}).off('blur', '#gm-sy-search').on('blur', '#gm-sy-search', function() {

		$(this).removeClass('open');

	}).off('mouseenter', '.gm-logo-lg').on('mouseenter', '.gm-logo-lg', function() {

		$(this).addClass('animated fadeInDown');

	}).off('click', '.gm-shade').on('click', '.gm-shade', function() {

		$('.gm-shade').hide();

		$('body').attr('class', '');

		$(window).off('resize.bootstrap-table');

		if($('.gm-top-logo').hasClass('animated fadeInDown')) {
			$('.gm-top-logo').removeClass('animated fadeInDown');
		}

		if($('.gm-left-nav').hasClass('open')) {
			$('.gm-left-nav').removeClass('open');
		}

		if($('.gm-left-nav-panel').find('.active')) {
			$('.gm-left-nav-panel').find('.active').removeClass('active');
			$('.current-mark').closest('ul').parent('li').addClass('active');
		}

		if($('.gm-top-nav-content').find('.active')) {
			$('.gm-top-nav-content').find('.active').removeClass('active');
		}

		clearNav();

		$('.gm-character').popover('hide');
		$('.gm-user').popover('hide');

		//换肤
		var _skin=$('.gm-global-bg').attr('data-skin');
		$('body').addClass(_skin);

	}).off('click', '.top.nav-toggle').on('click', '.top.nav-toggle', function() {

		//头部导航开关
		if(!$('body').hasClass('top-header')) {
			$('body').addClass('top-header');
			$('.gm-shade').show();

			clearNav();

			setPosition();
			$(window).resize(function() {
				setPosition();
			});
			$('.top-nav-close').removeClass('hide');
		}
	}).off('click', '.gm-top-nav .gm-nav-multistep-list li').on('click', '.gm-top-nav .gm-nav-multistep-list li', function() {

		var next = $(this).next();
		next.siblings('ul').removeClass('active');
		if(next.is('ul')) {
			next.addClass('active');
		}
	}).off('mouseover', '.gm-top-nav li').on('mouseover', '.gm-top-nav li', function() {

		var thisWidth = $(this).width();

		$(window).resize(function() {
			thisWidth = $(this).width();
		});

		var $ul = $(this).closest('ul');
		$ul.find('>li:first').removeClass('hover');

		$(this).addClass('hover');

		//判断在此之前有几个ul
		var prevSize = 0;
		$(this).prevAll().each(function() {
			if($(this).is('ul')) {
				prevSize++;
			}
		});

		var $index = $(this).index();
		var $top = ($index - prevSize) * 38 + 15;
		if($ul.find('>.gm-top-nav-hover').length == 0) {

			$ul.append('<div class="gm-top-nav-hover"></div>');
		}
		$ul.find('>.gm-top-nav-hover').css({
			'top': $top,
			'width': thisWidth,
			'left': '15px'
		});

	}).off('mouseleave', '.gm-top-nav li').on('mouseleave', '.gm-top-nav li', function() {

		//定位头部导航的hover

		$(this).removeClass('hover');

		$ul = $(this).closest('ul');
		var $hover = $ul.find('>.gm-top-nav-hover');

	}).off('click', '.gm-top-nav li').on('click', '.gm-top-nav li', function() {
		$(this).siblings('.current').removeClass('current');
		$(this).addClass('current');

		setPosition();

	}).off('click', '.top-header>.glyphicon-remove').on('click', '.top-header>.glyphicon-remove', function() {
		$('body').removeClass('top-header');
		$(this).remove();

		$('.gm-nav-item-list').find('.gm-top-nav-hover').remove();

		$('.gm-shade').hide();
		//		$('.gm-content').css('margin-top', '80px');

	}).off('click', '.gm-search').on('click', '.gm-search', function() {

		//搜索框
		$('body').addClass('top-search');
		$('.gm-search-block input').focus();
		$('.gm-shade').show();

	}).off('click', '.gm-search-block i').on('click', '.gm-search-block i', function() {

		//隐藏搜索框
		$('body').removeClass('top-search');
		$('.gm-shade').hide();

	}).off('click', '.gm-lately-use').on('click', '.gm-lately-use', function() {

		//最近使用
		if($('body').hasClass('left-nav')){
			$('body').removeClass('left-nav');
		}
		$(this).siblings().popover('hide');

		$('body').addClass('top-lately');
		$('.gm-shade').show();
		$('#tab-general').show();
	}).off('click', '.gm-phone-toggle').on('click', '.gm-phone-toggle', function() {
		//手机端导航
		var phoneNav = $('.gm-nav.top-nav').height();
		$('body').toggleClass('top-nav');
		var nav = $('.gm-phone-nav');
		if(!nav.hasClass('open')) {
			nav.addClass('open');
//			nav.css({
//				'margin-top': phoneNav + 20
//			});
		} else {
			nav.removeClass('open');
//			nav.css({
//				'margin-top': -phoneNav
//			});
		}

	}).off('click', '.gm-phone-nav a').on('click', '.gm-phone-nav a', function() {
		var next = $(this).next();
		var closestLi = $(this).closest('li');
		var closestUl = $(this).closest('ul');
		if(next.is('ul')) {
			next.addClass('active');
			closestUl.addClass('gm-phone-nav-hide');
			closestLi.addClass('active');
		}else if($(this).attr("data-url") && $(this).attr("data-url")!="") {
			var phoneNav = $('.gm-nav.top-nav').height();
			$('body').toggleClass('top-nav');
			var nav = $('.gm-phone-nav');
			if(!nav.hasClass('open')) {
				nav.addClass('open');
			} else {
				nav.removeClass('open');
			}
			//$.addtabs.closeAll('.gm-content');
		}
	}).off('click', '.gm-phone-nav-back').on('click', '.gm-phone-nav-back', function() {
		var closestUl = $(this).closest('ul');
		var closestUlul = closestUl.parent().parent();

		closestUlul.removeClass('gm-phone-nav-hide');
		closestUl.removeClass('active');
	}).off('click', '.gm-lately-use-block .title>i').on('click', '.gm-lately-use-block .title>i', function() {
		$('.gm-shade').hide();
		$('body').removeClass('top-lately');
	})
		//	.off('click', '.gm-left-nav-title>li').on('click', '.gm-left-nav-title>li', function() {
		//		var panel = $(this).attr('data-content');
		//		if($.trim(panel).length > 0) {
		//			if(!$('.' + panel + '').hasClass('active')) {
		//				$('.gm-left-nav>div').removeClass('active');
		//				$('.' + panel + '').addClass('active');
		//				$('body').addClass('left-nav');
		//				$('.gm-shade').show();
		//				if(!$('.gm-left-nav').hasClass('open')) {
		//					$('.gm-left-nav').addClass('open');
		//				}
		//			} else {
		//				$('.gm-left-nav>div').removeClass('active');
		//				$('body').removeClass('left-nav');
		//				$('.gm-shade').hide();
		//				$('.gm-left-nav').removeClass('open');
		//			}
		//		}
		//
		//	})
		.off('click', '.mCSB_container li a').on('click', '.mCSB_container li a', function() {

		//左侧导航
		if(! $('body').hasClass('left-nav')){
			$('body').addClass('left-nav');
			$(window).off('resize.bootstrap-table');
			$('.gm-left-nav').addClass('open');
			if(typeof($(this).attr("data-src"))!="undefined"){
				$('.current-root-mark').removeClass('current-root-mark');
			}
			$('.gm-shade').show();
		}
		$(this).closest('.gm-left-nav-ul').prev('.total-nav-title').addClass('active');

		var next = $(this).next();
		var closeLi = $(this).closest('li');
		if(next.is('ul')) {
			if(!closeLi.hasClass('active')) {
				closeLi.addClass('active');
			} else {
				closeLi.removeClass('active');
			}
		}

		$('.gm-left-nav').mCustomScrollbar('update');


	}).off('click','.total-nav-title').on('click','.total-nav-title',function(){


		if(! $('body').hasClass('left-nav')){
			$('body').addClass('left-nav');
			$('.gm-left-nav').addClass('open');
			if(typeof($(this).attr("data-src"))!="undefined"){
				$('.current-root-mark').removeClass('current-root-mark');
			}
			$('.gm-shade').show();
		}

		$('.total-nav-title').removeClass('active');
		$(this).addClass('active');

	}).off('click', '.mCSB_container li').on('click', '.mCSB_container li', function() {

		var $this = $(this);
		$('.gm-left-nav').mCustomScrollbar('scrollTo', $this);

	}).off('click', '.gm-skin-change-panel .item').on('click', '.gm-skin-change-panel .item', function() {
    		//切换皮肤
		skin = $(this).find('>a').attr('role');
		$('.gm-global-bg').attr('class','').addClass('gm-global-bg '+skin+'');
		$('.gm-global-bg').attr('data-skin',skin);
		localStorage.lastname= skin;
		$('.menu-navbar').attr('class','').addClass('navbar navbar-default navbar-static hidden-xs menu-navbar '+skin+'');
		$('.menu-navbar').attr('data-skin',skin);
		try{
			$("iframe").contents().find("body").attr('class','').addClass(skin);
		}catch(e){

		}

	}).off('mouseleave', '.gm-nav.top-nav .popover').on('mouseleave', '.gm-nav.top-nav .popover', function() {

		setTimeout(function(){
			$('.gm-character').popover('hide');
			$('.gm-user').popover('hide');
			$('.gm-skin-change').popover('hide');
//			$('.gm-version').popover('hide');
		},200);
		//设置版本信息自动隐藏时间为1秒
		setTimeout(function(){
			$('.gm-version').popover('hide');
		},1000);

	}).off('click','.top-nav .navbar-nav>li').on('click','.top-nav .navbar-nav>li',function(){

		if($('.left-nav-root').hasClass('active')){
			$('.left-nav-root').removeClass('active')
		}
	}).off('click','.mCSB_container li a[data-src]').on('click','.mCSB_container li a[data-src]',function(){

		var $ul=$(this).closest('ul');
		$ul.find('.current-mark').removeClass('current-mark');
		$(this).addClass('current-mark');

	}).off('mouseenter','.navbar-nav>li').on('mouseenter','.navbar-nav>li',function(){
		//$(this).tooltip('show');
	});


	//打开页面时给data-skin赋值
	$('.gm-global-bg').attr('data-skin',skin);

	//弹出框
	$('.gm-user').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{
			show: 0,
			hide: 100
		},
		content: function() {
			var html = [];
			html.push($('.gm-user-module').html());
			return html.join("");
		}
	});




	$('.gm-character').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{
			show: 0,
			hide: 100
		},
		content: function() {
			var html = [];
			html.push($('.gm-character-module').html());
			return html.join("");
		}
	}).on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
		$(this).siblings().popover('hide');
	});

	$('.gm-skin-change').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{
			show: 0,
			hide: 100
		},
		content: function() {
			var html = [];
			html.push($('.gm-skin-change-module').html());
			return html.join("");
		}
	}).on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
		$(this).siblings().popover('hide');
	});

	$('.gm-user').on('shown.bs.popover', function() {
		$('.gm-shade').show();
	}).on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
		$(this).siblings().popover('hide');
	});

	$('.gm-character').on('shown.bs.popover', function() {
		$('.gm-shade').show();
	});
	//版本信息 弹出框设置
	$('.gm-version').popover({
		trigger: 'click',
		placement: 'bottom',
		html: true,
		delay:{
			show: 0,
			hide: 100
		},
		content: function() {
			var html = [];
			html.push($('.gm-version-module').html());
			return html.join("");
		}
	});
	//导航滚动条插件
	$('.gm-left-nav').mCustomScrollbar({
//		theme: "rounded-dots",
//		scrollButtons: {
//			enable: false,
//			scrollType: "continuous",
//			scrollSpeed: 20,
//			scrollAmount: 40
//		},
		advanced: {
			updateOnBrowserResize: true,
			updateOnContentResize: true,
			autoExpandHorizontalScroll: true
		}
	});

	$('#tab-general .tab-pane').mCustomScrollbar({
		theme: "inset-dark"
	});


	/*$('#tabs').tabs({
		monitor: '.gm-nav-item-list,.gm-lately-use-block,.gm-left-nav-panel',
		contextmenu:false
	});*/

	setLeftNavHeight();
	function setLeftNavHeight() {
		$windowHeight = $(window).height();
		$('.gm-left-area').height($windowHeight);
	}

	//设置iframe区域最小高度
	setIframeHeight();
	var windowsHeight=$(window).height();
	function setIframeHeight(){
		var minHeight=$(window).height()-91;
		var _height=minHeight;
		$('.tab-content-first').css({
			'min-height':minHeight,
		});
		$('.tab-content-first').find('.tab-pane-first').css({
			'height':_height,
		});
	}

	$(window).resize(function() {
		setLeftNavHeight();
		setIframeHeight();
		$('.gm-top-nav-hover').remove();
		var windowsWidth=$(window).width();
		if(windowsWidth < 768){
			//parent.location.reload();
		}
	});

	//给头部导航的每层加标识
	$('.gm-nav-item-list').attr('data-level', 'dqcj-1');
	var erji = $('.gm-nav-item-list').find('>ul');
	var sanji = erji.find('>ul');
	if(erji) {
		erji.each(function() {
			$(this).attr('data-level', 'dqcj-2');
		});
	}
	if(sanji) {
		sanji.each(function() {
			$(this).attr('data-level', 'dqcj-3');
		});
	}

	function clearNav() {
		var nav = $('.gm-nav-item-list');
		if(nav.find('.current')) {
			nav.find('.current').removeClass('current');
		}
		if(nav.find('.active')) {
			nav.find('.active').removeClass('ative');
		}
		if(nav.find('.gm-top-nav-hover')) {
			nav.find('.gm-top-nav-hover').remove();
		}

		var $first = $('.gm-nav-item-list>li:first');
		var $ul = $first.next('ul');
		var $second = $ul.find('>li:first');
		var $third = $second.next('ul').find('>li:first');
		$first.addClass('current');
		$second.addClass('current');
		$third.addClass('current');
		$ul.addClass('active');
		$second.next('ul').addClass('active');

		$(window).resize(function() {
			setPosition();
		});

		if($('body').find('.top-nav-close').length == 0) {
			$('body').prepend('<i class="glyphicon glyphicon-remove top-nav-close hide"></i>');
		}

		var navheight = $('.gm-top-nav').outerHeight();
		//		$('.gm-content').css('margin-top', navheight);
	}

	function setPosition() {
		//头部导航定位
		var first = $('.gm-top-nav .gm-nav-item-list');
		var second = $('ul [data-level="dqcj-2"]');
		var third = $('ul [data-level="dqcj-3"]');
		var level = $('.gm-top-nav .gm-nav-multistep-list');

		second.css({
			'left': '0px'
		});

		third.css({
			'left': '0px'
		});

		var firstWidth = first.width();
		second.css({
			'left': firstWidth,
			'top': '0px',
			'width': firstWidth,
		});

		third.css({
			'left': firstWidth,
			'top': '0px',
			'width': firstWidth,
		});

		var h_max = 0;
		var h_first = first.outerHeight();
		first.find('ul.active').each(function() {
			var h = $(this).outerHeight();
			h_max = h > h_max ? h : h_max;
		});
		h_first = h_max > h_first ? h_max : h_first;
		first.css({
			'height':h_first
		});
		second.css({
			'height':h_first
		});
		third.css({
			'height':h_first
		});



	}
	setPhonenav();

	$(window).resize(function() {
		setPhonenav();
		setIframeHeight();
	});


	function setPhonenav() {

		//手机端导航
		if(windowsHeight<767){
			//$('.gm-shade').hide();
			//销毁tooltip事件
			$('.gm-nav.top-nav [data-toggle="tooltip"]').tooltip("destroy");
		}

		var phoneNav = $('.gm-nav.top-nav').height();
		$('.gm-phone-nav').css({
			//'margin-top': phoneNav
		});
	}
	if(_url.indexOf("&zyid=")!=-1){

		var reg= new RegExp("(^|&)zyid=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		var zyid=unescape(r[2])
		var reg1= new RegExp("(^|&)_fjd=([^&]*)(&|$)", "i");
		var r1 = window.location.search.substr(1).match(reg1);
		var fid=unescape(r1[2])
		$(".menu-navbar ul").each(function () {
			if($(this).find("li").find("a").attr("gm-id")==fid){
				$(".menu-navbar ul").removeClass("active");
				$(this).addClass("active");
				var menuid=fid;
				$.ajax({
					type:'post',
					url:$(this).find("li").find("a").attr("gm-url"),
					cache: false,
					data: {"menuId":fid,"access_token":$("#ac_tk").val()},
					dataType:'json',
					success:function(data){
						if(data != null && data.length != 0){
							var isFirst = true;
							var s_html="";
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
										if(dqjs!=""&&yhid!=""){
											s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '?access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd  +'&_mid='+menuid +'&dqjs='+dqjs +'&yhid='+yhid+'&_dqurl='+dqurl + '"';

										}else{
											s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '?access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd  +'&_mid='+menuid +'&_dqurl='+dqurl + '"';

										}
									else
										if(dqjs!=""&&yhid!=""){
											s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '&access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd  +'&_mid='+menuid +'&dqjs='+dqjs +'&yhid='+yhid+'&_dqurl='+dqurl + '"';

										}else{
											s_html +='<li><a data-addtab="' + data[i].zyid + '" data-url="' + data[i].zylj + '&access_token=' + $("#ac_tk").val() + '&zyid=' + data[i].zyid +'&_fjd='+data[i].fjd  +'&_mid='+menuid +'&_dqurl='+dqurl + '"';

										}
										if(dqjs!=""&&yhid!=""){
											s_html +='href="?access_token='+ $("#ac_tk").val() + '&zyid=' + data[i].zyid + '&_fjd='+menuid+'&dqjs='+dqjs +'&yhid='+yhid+'"'+' title="' + data[i].zybt + '"'

										}else{
											s_html +='href="?access_token='+ $("#ac_tk").val() + '&zyid=' + data[i].zyid + '&_fjd='+menuid+'&_dqurl='+dqurl+'"' + ' title="' + data[i].zybt + '"'
										}
									s_html += "onclick= 'return zzHref(\""+data[i].zylj+"\",\""+data[i].zybj+"\")'"
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
						$('a[data-addtab]').each(function(){
							if(zyid==$(this).attr("data-addtab")){
								$(this).parent().parent().parent().addClass("active")
								$(this).attr("data-target",".nav-tabs")
								_click($(this));

							}
						})
					},
					error:function(e){
						console.log(e)
						alert("请求失败")
					}
				});
			}
		})

	}
});

function zzHref(url,zybj){
	if(url==""||url==null||url==undefined){
		return false;
	}
	if(zybj!=null&&zybj!=undefined){
        buttonids = zybj.split(',');
        if(buttonids){
            for (var i = 0, ii = buttonids.length; i < ii; ++i) {
                //关闭TAB
                $("#tab_tab_" + buttonids[i]).remove();
                $("#tab_" + buttonids[i]).remove();
            }
        }
    }

	if((url!=""&&url.indexOf("https://")!=-1)||(url!=""&&url.indexOf("http://")!=-1)){
		if(vueid!=""){
			$.addtabs.close({id:vueid})
			$.addtabs.drop();
			$("#tab_tab_" + vueid).remove();
		}

	}

	return false;
}

function cs(){
	var cs={ajax: false,
		content: undefined,
		id: "090102",
		target: ".nav-tabs",
		title: "用户列表",
		url: "/systemrole/user/pageListUser?access_token=c47f57b0-39fc-4497-81ae-a6a2a5d20192&zyid=090102"}
	$.addtabs.add(cs);
}

function closeTab(id){
	var cs={
		id:id
	}
	$.addtabs.close(cs);
}