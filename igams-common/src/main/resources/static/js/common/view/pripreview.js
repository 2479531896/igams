function img_load(){
	$(".myimg").css("display", "block");
	var winwidth=790;
	$(".myimg").css("width",winwidth);
	$(".leftimg").css("width",winwidth/15);
	$(".rightimg").css("width",winwidth/15);
	//右旋转
	var current = 0;
	$(".rightimg").bind('click',function(){
		var imgheight=$(".myimg").height();
		var img=$(this).parent().prev().children("img");
		current +=90;
		img.css('transform', 'rotate('+current+'deg)');
		img.css({"margin-top": (790-imgheight)/2});
		$(".leftdiv").css({"margin-top":(790-imgheight)/2});
		$(".rightdiv").css({"margin-top": (790-imgheight)/2});
	})
	//左旋转
	$(".leftimg").bind('click',function(){
		var imgheight=$(".myimg").height();
		var img=$(this).parent().next().children("img");
		current -=90;
		img.css('transform', 'rotate('+current+'deg)');
		img.css({"margin-top": (790-imgheight)/2});
		$(".rightdiv").css({"margin-top": (790-imgheight)/2});
		$(".leftdiv").css({"margin-top": (790-imgheight)/2});
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
}

//图片旋转
$(function(){
	setTimeout(function(){img_load()}, 200);
})

