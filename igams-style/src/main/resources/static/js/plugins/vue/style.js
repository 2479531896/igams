/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2018-05-22 16:19:22
 * @version $Id$
 */
$(function(){


	/*****************************滚动监听执行css3动画*******************************/

	/*页面里面添加的单个属性
	<div class="wow slideInLeft" data-wow-duration="2s" data-wow-delay="5s"></div>
	<div class="wow slideInRight" data-wow-offset="10"  data-wow-iteration="10"></div>
	类名前面的wow是每一个带动画的元素都要加的，slideInLeft就是说明动画样式。后面的data-wow-duration（动画持续时间）、data-wow-delay（动画延迟时间）、data-wow-offset（元素的位置露出后距离底部多少像素执行）和data-wow-iteration（动画执行次数）这四个属性可选可不选。*/
	
	if (!(/msie [6|7|8|9]/i.test(navigator.userAgent))){
		var wow = new WOW({  
		    boxClass: 'wow',  //	需要执行动画的元素的 class
		    animateClass: 'animated',  //	animation.css 动画的 class
		    offset: 100,  //元素的位置露出后距离底部多少像素执行
		    mobile: true, //是否在移动设备上执行动画 
		    live: true  //异步加载的内容是否有效
		});  
		wow.init();
	};

	/*****************************网页锚点滚动双向绑定*******************************/
	
	$.fn.scrollToAnchor = function(option) {
		return this.each(function() {
			var options = $.extend({
				fixed: 0,
				offset: 0
			}, {}, typeof option === 'object' && option);
	
			var $anchorList = $(this),
				$anchor = $anchorList.find('a'),
				isScrolling = false;
			
			// 点击锚点时页面滚动
			$anchor.click(function(e) {
				if (!$(this).attr('href').indexOf('#')) {
					e.preventDefault ? e.preventDefault() : e.returnValue = false;
				}
	
				isScrolling = true;
	
				try {
					var $target = $($(this).attr('href'));
	
					$('html, body').stop(true, false).animate({
						scrollTop: $target.offset().top + options.offset
					}, function() {
						isScrolling = false;
					});
	
					if ($(this).siblings('a').length) {
						$(this).addClass('active').siblings().removeClass('active');
					} else {
						$(this).parent().addClass('active').siblings().removeClass('active');
					}
				} catch (e) {}
			});
	
			$(window).on('scroll DOMContentLoaded load', function() {
				// 固定锚点列表
				if ($(window).scrollTop() > options.fixed) {
					$anchorList.addClass('fixed').css({
						position: 'fixed',
						marginTop: 0,
						marginBottom: 0
					});
				} else {
					$anchorList.removeAttr('style').removeClass('fixed');
					$anchor.each(function() {
						if ($(this).siblings('a').length) {
							$(this).removeClass('active');
						} else {
							$(this).parent().removeClass('active');
						}
					});
				}
	
				// 锚点对应项添加高亮样式
				if (!isScrolling) {
					$anchor.each(function() {
						try {
							var $target = $($(this).attr('href'));
	
							if ($(window).scrollTop() >= $target.offset().top + options.offset) {
								if ($(this).siblings('a').length) {
									$(this).addClass('active').siblings().removeClass('active');
								} else {
									$(this).parent().addClass('active').siblings().removeClass('active');
								}
							}
						} catch (e) {}
					});
				}
			});
		});
	};

	// 网页锚点滚动调用js
	$('.floater').scrollToAnchor({
		// fixed: 100,
		offset: -80  //距离顶部位置
	});



	/*****************************返回顶部滚动js动画*******************************/
	
	$(window).scroll(function(){
        if ($(window).scrollTop()>200){ //当滚动到超过200的位置返回顶部按钮出现反之影藏
            $(".backTop").fadeIn(500);
        }
        else{
            $(".backTop").fadeOut(500);
        }
    });


    $(".backTop").click(function(){    	// 当点击跳转链接后，回到页面顶部位置
        $('body,html').animate({scrollTop:0},1000);
        return false;
    });
	



	/*****************************头部导航hover更改当前状态*******************************/

	$(".header .nav li a").hover(function() {
		$(this).parent().siblings().find('.current').removeClass('current').addClass('on');				
	}, function() {
		$(this).parent().siblings().find('.on').removeClass('on').addClass('current');				
	});


	
	/*****************************网页滚动头部导航常驻*******************************/
 	
 	// 	$(window).scroll(function(){

	// 	var qs = $(window).scrollTop();				
	// 	var qs1 = $(".content").offset().top;
	// 	var qs2 = qs1 - qs;
	// 	if (qs2 < 100) {
	// 		$(".header").addClass('headerIS');
	// 	}
		
	// 	if (qs2 >= 0) {
	// 		$(".header").removeClass('headerIS');
	// 	}

	// })


	/*****************************手机站导航切换按钮*******************************/

	$('.headerSJ .menu').click(function(event) {
        $('.headerSJ .navwrap').animate({'left': '0px', 'opacity': '1'}, 800);
        $('.headerSJ .navbg').fadeIn(800);
    });

    $('.headerSJ .navbg').click(function(event) {
        $(this).fadeOut(800);
        $('.headerSJ .navwrap').animate({'left': '-50%', 'opacity': '0'}, 800);
    });

    $(".headerSJ .plus").click(function(){  				//二级栏目手风琴切换效果
    	$(this).toggleClass('minus').parent().siblings().find('.minus').removeClass('minus');
		$(this).next(".SJbod").stop().slideToggle(300).parent().siblings().find(".SJbod").stop().slideUp(500);
	});



	/*****************************检测浏览器版本*******************************/
	var browser=navigator.appName;
	var b_version=navigator.appVersion;
	var version=b_version.split(";");
	var trim_Version=version[1].replace(/[ ]/g,"");
	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0")
	{
		alert("您的浏览器版本过低，请用IE9以上或其他高版本浏览器浏览该网站。");
		window.open("http://chrome.360.cn");
	}
	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0")
	{
	   	alert("您的浏览器版本过低，请用IE9以上或其他高版本浏览器浏览该网站。");
		window.open("http://chrome.360.cn");
	}


})
