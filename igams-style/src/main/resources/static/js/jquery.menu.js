//@ sourceURL=jquery.menu.js 

$(function() {
	// var flag;
	// if ($('body').hasClass('sidebar-colors')) {
	// flag = true;
	// } else {
	// flag = false;
	// }
	// menuChange();
	// function menuChange() {
	// $('#menu-toggle').toggle(
	// function() {
	// if ($('#wrapper').hasClass('right-sidebar')) {
	// $('body').addClass('right-side-collapsed')
	// $('#sidebar .menu-scroll').css('overflow', 'initial');
	// } else {
	// if ($.cookie('menu_style')) {
	// $('body').addClass('sidebar-collapsed')
	// .removeClass($.cookie('menu_style'));
	// $('#sidebar .menu-scroll').css('overflow',
	// 'initial');
	// // Remove slimscroll when collapsed
	// if ($.cookie('header') == 'header-fixed') {
	// // Use for menu style 1 & 2
	// if ($('body').hasClass('sidebar-collapsed')) {
	// $('#side-menu').attr('style', '').parent(
	// '.slimScrollDiv').replaceWith(
	// $('#side-menu'));
	// } else {
	// // Use for menu style 4
	// setTimeout(function() {
	// $('#side-menu').slimScroll({
	// "height" : $(window).height() - 50,
	// 'width' : '250px',
	// "wheelStep" : 5
	// });
	// $('#side-menu').focus();
	// }, 500)
	// }
	// }
	// } else {
	// $('body').addClass('sidebar-collapsed')
	// .removeClass($.cookie('menu_style'));
	// $('#sidebar .menu-scroll').css('overflow',
	// 'initial');
	// }
	// }
	// },
	// function() {
	// if ($('#wrapper').hasClass('right-sidebar')) {
	// $('body').removeClass('right-side-collapsed');
	// $('#sidebar .menu-scroll').css('overflow', 'hidden');
	// } else {
	// $('body').removeClass('sidebar-collapsed');
	// $('body').addClass($.cookie('menu_style'));
	// if ($.cookie('header') == 'fixed') {
	// $('#side-menu').addClass('sidebar-fixed');
	// }
	// // Add slimscroll when open and have cookie fixed
	// if ($.cookie('header') == 'header-fixed') {
	// if ($('body').hasClass('sidebar-collapsed')) {
	// // Use for menu style 1 & 2
	// $('#side-menu').attr('style', '').parent(
	// '.slimScrollDiv').replaceWith(
	// $('#side-menu'));
	// } else {
	// // Use for menu style 4
	// setTimeout(function() {
	// $('#side-menu').slimScroll({
	// "height" : $(window).height() - 50,
	// 'width' : '250px',
	// "wheelStep" : 5
	// });
	// $('#side-menu').focus();
	// }, 500)
	// }
	// }
	// }
	// });
	// }
	//
	// if ($('#wrapper').hasClass('right-sidebar')) {
	// $('ul#side-menu li').hover(function() {
	// if ($('body').hasClass('right-side-collapsed')) {
	// $(this).addClass('nav-hover');
	// }
	// }, function() {
	// if ($('body').hasClass('right-side-collapsed')) {
	// $(this).removeClass('nav-hover');
	// }
	// });
	// } else {
	// $('ul#side-menu li').hover(function() {
	// if ($('body').hasClass('left-side-collapsed')) {
	// $(this).addClass('nav-hover');
	// }
	// }, function() {
	// if ($('body').hasClass('left-side-collapsed')) {
	// $(this).removeClass('nav-hover');
	// }
	// });
	// }
	//
	// // 三级导航
	// var third = $('.nav-second-level').find('.nav-third-level');
	// third
	// .each(function() {
	// var _this = $(this);
	// _this
	// .prev()
	// .click(
	// function() {
	// if (_this.is(':hidden')) {
	// $('.nav-third-level').slideUp(400);
	// _this.slideDown(400);
	// $(
	// '.navbar-static-side ul li .nav-second-level li div.active')
	// .removeClass('active');
	// _this.prev().addClass('active');
	// } else {
	// $('.nav-third-level').slideDown(400);
	// _this.slideUp(400);
	// }
	// });
	// });
	//

	//	function resizeSize() {
	//		// 仅在非手机端使用大小导航
	//		var _menuLi = $("#side-menu>li").not($(".user-panel")).find(
	//				'.nav-title');
	//		_menuLi.each(function() {
	//			$(this).click(function() {
	//				var url = $(this).attr('data-src');
	//				window.open(url);
	//				if ($.trim($(this).attr('data-src')).length > 0) {
	//					if ($(window).width() > 767) {
	//						if (!$('body').hasClass('sidebar-collapsed')) {
	//							$('body').addClass('sidebar-collapsed');
	//							$('.nav-third-level').addClass('hide');
	//							monitorNumber();
	//						} else {
	//							monitorNumber();
	//						}
	//					} else {
	//					}
	//				}
	//			});
	//		});
	//	}
	//	resizeSize();
	//	$(window).resize(function() {
	//		resizeSize();
	//	});

	//
	// function monitorNumber() {
	// var number = $('.nav-tabs>li').length;
	// if (number > 1) {
	// $('.nav-tabs>li:first i.glyphicon-remove').addClass('hide');
	// $('body').addClass('tab-sidebar-collapsed');
	// } else {
	// $('.nav-tabs>li:first i.glyphicon-remove').removeClass('hide');
	// $('body').removeClass('tab-sidebar-collapsed');
	// }
	// }
	//
	// $('.navbar-nav>li').click(function() {
	// $('.navbar-nav>li.active').removeClass('active');
	// $(this).addClass('active');
	// });
	//
	// /*
	// * $('ul li').live('mouseenter',function(){ $('#tips').show();
	// * }).live('mouseleave',function(){ $('#tips').hide(); });
	// * $(".sidebar-collapsed #side-menu>li").hover(function(){ alert("1");
	// * $(this).addClass('active'); },function(){ alert("2");
	// * $(this).removeClass('active'); });
	// */
	//
	// // 切换主页
	// $('#index-page').click(function() {
	// if ($('#home').is(':hidden')) {
	// $('.nav-tabs li').not($('#tab_tab_home')).remove();
	// $('.nav-tabs').hide();
	// $('.tab-content>div').not($('#home')).remove();
	// $('#home').show();
	// }
	// $('body').removeClass('tab-sidebar-collapsed');
	// });
	//
	
	$('#mega_menu_dropdown').on('shown.bs.dropdown', function () {
		
		//获取一级菜单的高度，使之后几级与之相等
		var rootHeight=$(this).find('ul.dropdown-menu').height();
		var all_child_menu=$(this).find('.mega-menu-submenu');
		  
		all_child_menu.height(rootHeight);
	})
	
	
	
	
	// 水平导航
	$(document).off("click", '.mega-menu-dropdown .mega-menu-root .mCSB_container > li').on("click",'.mega-menu-dropdown .mega-menu-root .mCSB_container > li',function(event) {
		// 当前菜单所在菜单区域
		var currt_menu = $(this).closest('.mega-menu-root');
		// 切换选中样式
		$(currt_menu).find(".mCSB_container > li").removeClass('active');
		$(this).addClass('active');

		// console.log("next_menu:" +
		// $(currt_menu).data("next-menu"));

		// 找到当前菜单对于的下一级菜单区域对象
		var next_menu = $($(currt_menu).data("next-menu"));
		
		console.log("next_menu :" + next_menu.length);
		
		// 判断菜单区域存在
		if(next_menu.length == 1) {

			// console.log("menu-items:" +
			// $(this).data("menu-items"));
			next_menu.find(".mCSB_container > li").removeClass('active');
			
			console.log("next_menu2 :" + next_menu.find(".mCSB_container > li").length);
			// 找到当前菜单对应的菜单集合
			var menu_target = $($(this).data("menu-items"));

			// 递归调用样式清除函数
			function clearClass(target_menus) {

				target_menus.each(function() {

					$(this).removeClass('active');

					$(this).find("ul > li").each(function() {

						$(this).removeClass('active');

						// 找到当前菜单对应的菜单集合
						var menu_target2 = $($(this).data("menu-items"));
						// 判断菜单存在
						if(menu_target2.length == 1) {
							menu_target2.removeClass('active');
							// 递归调用样式清除函数
							clearClass(menu_target2.find("li"));
						}

					});

				});

			}

			// 递归调用样式清除函数
			clearClass(next_menu.find(".mCSB_container > li").not(menu_target[0]));

			// console.log(menu_target.length);
			// 判断菜单存在
			if(menu_target.length == 1) {
				menu_target.addClass('active');
				var sub_menus = menu_target.find("ul > li");
				if(sub_menus.length > 0) {
					sub_menus.eq(0).addClass('active');
				}
			}
		}
		event.stopPropagation();
	}).off("hover", '.mega-menu-dropdown .mega-menu-child ul > li').on("hover",'.mega-menu-dropdown .mega-menu-child ul > li',function(event) {
		// 当前菜单所在菜单区域
		var currt_menu = $(this).parent();
		// 切换选中样式
		$(currt_menu).children("li").removeClass('active');
		$(this).addClass('active');

		// console.log("next_menu:" +
		// $(currt_menu).data("next-menu"));

		// 找到当前菜单对于的下一级菜单区域对象
		/* var next_menu = $($(currt_menu).data("next-menu")); */
		var next_menu = $($(this).data('menu-items'));
		// 判断菜单区域存在
		// console.log('size:'+next_menu.length);
		if(next_menu.length == 1) {

			// console.log("menu-items:" +
			// $(this).data("menu-items"));
			//next_menu.parent().find('li').removeClass('active');
			next_menu.closest('.mega-menu-submenu').find('li').removeClass('active');

			// 找到当前菜单对应的菜单集合
			var menu_target = $($(this).data("menu-items"));

			// 递归调用样式清除函数
			function clearClass(target_menus) {

				target_menus.each(function() {

					$(this).removeClass('active');

					$(this).find("ul > li").each(function() {

						$(this).removeClass('active');

						// 找到当前菜单对应的菜单集合
						var menu_target2 = $($(this).data("menu-items"));
						// 判断菜单存在
						if(menu_target2.length == 1) {
							menu_target2.removeClass('active');
							// 递归调用样式清除函数
							clearClass(menu_target2.find("li"));
						}

					});

				});

			}

			// 递归调用样式清除函数
			clearClass(next_menu.find("li").not(menu_target[0]));

			// console.log(menu_target.length);
			// 判断菜单存在
			if(menu_target.length == 1) {
				menu_target.addClass('active');

				var sub_menus = menu_target.find("ul > li");
				if(sub_menus.length > 0) {
					sub_menus.eq(0).addClass('active');
				}
			}
		} else {
			// 如果下一级菜单不存在
			$(this).closest('.mega-menu-child').next().find(
				'li').removeClass('active');

		}
		event.stopPropagation();
	}).off("click", '.mega-menu-dropdown .mega-menu-content').on("click", '.mega-menu-dropdown .mega-menu-content',function(event) {
		event.stopPropagation();
	});

	$('#mega_menu_dropdown').on('show.bs.dropdown', function() {

		// 当前菜单所在菜单区域
		var root_menu = $(".mega-menu-dropdown .mega-menu-root");
		// 切换选中样式
		var rootmenus = $(root_menu).find('.mCSB_container > li');
		rootmenus.removeClass('active');
		$(rootmenus[0]).addClass('active');

		// 找到当前菜单对于的下一级菜单区域对象
		var next_menu = $($(root_menu).data("next-menu"));
		
		$(next_menu).addClass('ccccccccc');
		
		// 判断菜单区域存在
		if(next_menu.length == 1) {
			// console.log("menu-items:" + $(rootmenus[0]).data("menu-items"));
			// 找到当前菜单对应的菜单集合
			if($(rootmenus[0]).closest('.mega-menu-submenu').hasClass('.mega-menu-root')){
				var menu_target = $($(rootmenus[0]).find('ul>li').data("menu-items"));
				console.log(11);
			}else{
				var menu_target = $($(rootmenus[0]).data("menu-items"));
				console.log(22);
			}
			
			// console.log(menu_target.length);
			next_menu.find('.mCSB_container > li').removeClass('active');
			
			next_menu.find('.mCSB_container > li').addClass('kkkkkkkkkkkkkk');
			menu_target.addClass('ssssssssssssssssss');
			
			
			// 判断菜单存在

			if(menu_target.length == 1) {
				menu_target.addClass('active');
			}
		}
	});

	$('.mega-menu-content').outerClick(function() {
		$('.mega-menu-dropdown.open').removeClass('open');
	});

	// +++++++++++++++++++++++++++++++++++++++++++++2016.11.28++++++++++++++++++++++++++++++++++++++++++++++++++++
	function resizeMenu() {
		var width = $(window).width();

		// 分辨率小于1200的电脑端时导航启用小导航
		if(width < 1200 && width > 767) {
			$('body').addClass('menu-collapse');
			$('.horizontal-menu').show();
		}
		if(width > 767) {

			//销毁手机端事件
			$(document).off('click', '.nav-left-usual');
			$(document).off('click', '.nav-left-menu .nav-title');
			$(document).off('click', '*[data-src]');

			// 仅在非手机端使用tab
			$('#tabs').tabs({
				sortable: true,
				monitor: '#sidebar,.horizontal-menu',
			});

			// 滚动条
			$(".nav-left-multilevel,.nav-left-third,.mega-menu-root,.mega-menu-child").mCustomScrollbar({
				scrollInertia: 550,
				theme: 'dark',
			});

			$(".nav-left-multilevel,.nav-left-third,.mega-menu-root,.mega-menu-child").mousewheel(function(event, delta, deltaX, deltaY) {
				event.preventDefault();
			});

			$(document).off('click', '.nav-left-usual').on('click', '.nav-left-usual', function() {
				// 显示一级菜单
				if(!$(this).hasClass('active')) {
					$('#side-menu>li').removeClass('active');
					$('.nav-left-menu').slideUp(200);
					$(this).addClass('active');
					$(this).next().slideDown(200);
				}
			}).off('mouseover', '.nav-left-menu>ul>li').on('mouseover', '.nav-left-menu>ul>li', function() {
				// 切换一级菜单
				$('.nav-left-menu>ul>li').removeClass('active');
				$('.nav-left-multilevel').removeClass('active');
				$(this).addClass('active');

			}).off('mouseover', '.nav-left-menu>ul>li.active').on('mouseover', '.nav-left-menu>ul>li.active', function() {

				// 显示选中的一级菜单下的二级菜单
				$(this).children().addClass('active');

			}).off('mouseenter', '.menu-collapse .nav-left-menu>ul>li').on('mouseenter', '.menu-collapse .nav-left-menu>ul>li', function() {

				//使二级菜单和一级菜单高度相同
				var yijiHeight = $(this).closest('ul').outerHeight();
				$(this).find('.nav-left-multilevel').height(yijiHeight - 30);

				// 获取一级菜单的位置给二级菜单定位
				var yijiTop = $(this).closest('ul').offset().top;
				var yijiLeft = $(this).closest('ul').offset().left;
				var yijiWidth = $(this).closest('ul').width();

				// 给二级菜单定位
				$('.nav-left-multilevel').css({
					left: yijiLeft + yijiWidth,
					top: yijiTop
				});

			}).off('mouseleave', '.nav-left-multilevel').on('mouseleave', '.nav-left-multilevel', function() {

				// 鼠标离开二级菜单时隐藏二级菜单以及去除其一级菜单的样式
				$('.nav-left-menu').find('.active').removeClass('active');
				$('.menu-collapse .nav-left-usual').removeClass('active');
				$('.menu-collapse .nav-left-menu').hide();

			}).off('mouseenter', '.nav-left-multilevel li').on('mouseenter', '.nav-left-multilevel li', function() {

				// 使三级菜单高度与二级菜单相同
				var erjiHeight = $(this).closest('.nav-left-multilevel').outerHeight();

				// 获取二级菜单的位置给三级菜单定位
				var erjiTop = $(this).closest('.nav-left-multilevel').offset().top;
				var erjiLeft = $(this).closest('.nav-left-multilevel').offset().left;
				var erjiWidth = $(this).closest('.nav-left-multilevel').width();

				// 切换三级菜单
				$('.nav-left-third').removeClass('active');
				$(this).addClass('active');
				if($(this).next().is('.nav-left-third')) {
					$(this).next().height(erjiHeight - 30);
					$(this).next().addClass('active');
					// 给三级菜单定位
					$('.nav-left-third').css({
						left: erjiLeft + erjiWidth + 30,
						top: erjiTop
					});
				}

			}).off('click', '.tab-content,.mega-menu-content').on('click', '.tab-content,.mega-menu-content', function() {
				$('body').addClass('menu-collapse');
				$('.horizontal-menu').show();
				$('.nav-left-menu').find('.active').removeClass('active');
				$('.nav-left-menu').slideUp(200);
			}).off('click', '#menu-toggle').on('click', '#menu-toggle', function() {
				$('body').removeClass('menu-collapse');
			}).off('click', '*[data-addtab]').on('click', '*[data-addtab]', function() {
				
				var url = $(this).data('src');
				setTimeout(ss(), 2000 );
				
				function ss(){
					if($.trim(url).length > 0 && (! $(this).closest('.nav-left-menu').prev().hasClass('active'))) {
						$('body').addClass('menu-collapse');
						$('.horizontal-menu').show();
					}
				}
			});
			
			$('#usual-panel').on('show.bs.dropdown', function () {
				$('.nav-left-usual').removeClass('active');
				$('.nav-left-menu').slideUp();
				$('.nav-left-menu').find('active').removeClass('active');
			})
			
		
			
		}

		if(width < 768) {
			//销毁电脑端事件
			$('body').removeClass('menu-collapse');
			$(document).off('click', '.tab-content,.mega-menu-content');
			$(document).off('click', '*[data-src]');
			$(document).off('mouseenter', '.nav-left-usual');
			$(document).off('mouseover', '.nav-left-menu>ul>li');
			$(document).off('mouseleave', '.nav-left-multilevel');
			$(document).off('mouseenter', '.nav-left-multilevel li');
			$(document).off('mouseenter', '.menu-collapse .nav-left-menu>ul>li');
			$(document).off('click', '#menu-toggle');
			$(".nav-left-multilevel,.nav-left-third").mCustomScrollbar("destroy");
			//$('.nav-left-usual').removeClass('active');

			//折叠所有非一级导航
			$('.nav-left-menu,.nav-left-multilevel').slideUp();

			$(document).off('click', '.nav-left-usual').on('click', '.nav-left-usual', function() {
				//一级点开二级
				var next = $(this).next();
				if(next.is('.nav-left-menu')) {
					$('.nav-left-menu').slideUp();
					if(next.is(':hidden')) {
						next.slideDown();
					} else {
						next.slideUp();
					}
				}
			}).off('click', '.nav-left-menu .nav-title').on('click', '.nav-left-menu .nav-title', function() {
				//二级点开三级
				var next = $(this).next();
				if(next.is('.nav-left-multilevel')) {
					$('.nav-left-multilevel').slideUp();
					if(next.is(':hidden')) {
						next.slideDown();
					} else {
						next.slideUp();
					}
				}
			}).off('click', '*[data-src]').on('click', '*[data-src]', function() {
				//手机端打开页面
				var url = $(this).data('src');
				if($.trim(url).length > 0) {
					window.open(url);
				}
			});
		}

	}
	resizeMenu();
	$(window).resize(function() {
		resizeMenu();
	});

});