
//图片旋转
$(function(){
	var winwidth=document.body.clientWidth;
	$(".myimg").css("width",winwidth);
	$(".leftimg").css("width",winwidth/15);
	$(".rightimg").css("width",winwidth/15);
	//右旋转
	var current = 0;
	$(".rightimg").bind('click',function(){
		var img=$(this).parent().prev().children("img");
		var imgheight=img.height();
		current +=90;
		img.css('transform', 'rotate('+current+'deg)');
		img.css({"margin-top": (winwidth-imgheight)/2});
		img.css({"margin-bottom": (winwidth-imgheight)/2});
		$(".leftdiv").css({"margin-top":(winwidth-imgheight)/2});
		$(".rightdiv").css({"margin-top": (winwidth-imgheight)/2});
		$(".leftdiv").css({"margin-bottom":(winwidth-imgheight)/2});
		$(".rightdiv").css({"margin-bottom": (winwidth-imgheight)/2});
	})
	//左旋转
	$(".leftimg").bind('click',function(){
		var img=$(this).parent().next().children("img");
		var imgheight=img.height();
		current -=90;
		img.css('transform', 'rotate('+current+'deg)');
		img.css({"margin-top": (winwidth-imgheight)/2});
		img.css({"margin-bottom": (winwidth-imgheight)/2});
		$(".leftdiv").css({"margin-top":(winwidth-imgheight)/2});
		$(".rightdiv").css({"margin-top": (winwidth-imgheight)/2});
		$(".leftdiv").css({"margin-bottom":(winwidth-imgheight)/2});
		$(".rightdiv").css({"margin-bottom": (winwidth-imgheight)/2});
	})
	$(".leftdiv").bind('mouseover',function(){
		$(".leftimg").css("display","block");
	})
	$(".leftdiv").bind('mouseout',function(){
		$(".leftimg").css("display","none");
	})
	$(".rightdiv").bind('mouseover',function(){
		$(".rightimg").css("display","block");
	})
	$(".rightdiv").bind('mouseout',function(){
		$(".rightimg").css("display","none");
	})
})