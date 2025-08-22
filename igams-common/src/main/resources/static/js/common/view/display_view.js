var dis_to_pageList = [];
var dis_current_page = "";

$(function(){
	var view_url = $("#view_url").val();
	
	if(!$("#view_url") || $("#view_url").val()==""){
		$.alert("未获取到显示的路径信息");
		return;
	}
	
	dis_toForward(view_url);
	
	$(document).off('mouseover', '.gm-top-nav li').on('mouseover', '.gm-top-nav li', function() {

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
		if($ul.find('>.gm-top-nav-hover').size() == 0) {

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

	}).off('mouseleave', '.gm-nav.top-nav .popover').on('mouseleave', '.gm-nav.top-nav .popover', function() {
		
		setTimeout(function(){
			$('.gm-character').popover('hide');
			$('.gm-user').popover('hide');
			$('.gm-skin-change').popover('hide');
//			$('.gm-version').popover('hide');
		},200);
		
		setTimeout(function(){
			$('.gm-version').popover('hide');
		},1000);
		
	}).off('mouseenter', '.gm-logo-lg').on('mouseenter', '.gm-logo-lg', function() {

		$(this).addClass('animated fadeInDown');

	}).off('mouseenter','.navbar-nav>li').on('mouseenter','.navbar-nav>li',function(){
		$(this).tooltip('show');
	});
});

function dis_toForward(pageUrl,displayflg){
	if(pageUrl == null || pageUrl == undefined || pageUrl =="")
		return false;
	
	if(dis_current_page!=""){
		dis_to_pageList.push(dis_current_page);
	}
	
	dis_current_page = pageUrl;
	
	$("#disdiv").load($("#urlPrefix").val()+dis_current_page);
	
	if(displayflg){
		$("#dis_back_button").show();
	}else{
		$("#dis_back_button").hide();
	}
	
	return true;
}

function dis_backPage(displayflg){
	if(displayflg){
		$("#dis_back_button").show();
	}else{
		$("#dis_back_button").hide();
	}
	if(dis_to_pageList.length>0){
		pageUrl = dis_to_pageList.pop();
		dis_current_page=pageUrl;
		$("#disdiv").load($("#urlPrefix").val()+pageUrl);
	}
}