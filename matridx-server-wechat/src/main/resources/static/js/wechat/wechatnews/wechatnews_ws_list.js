function getmore(){
	if($("#count").val()==10){
			var zxlx=$("#zxlx").val();
			var zxzlx=$("#zxzlx").val();
		$.ajax({ 
		    type:'post',  
		    url:"/ws/news/getMoreWechatNews", 
		    cache: false,
		    data: {"count":10,"start":$("#start").val(),"zxlx":zxlx,"zxzlx":zxzlx},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	$("#count").val(data.count);
		    	$("#start").val(parseInt($("#start").val())+parseInt(data.count));
		    	var html="";
		    	for(var i=0;i<data.list.length;i++){
		    		var zxjj=data.list[i].zxjj;
		    		if(zxjj==null || zxjj==''){
		    			zxjj="　";
		    		}
		    		html+="<div style='height:80px;border-bottom:1px solid #EEEEEE;' onclick=\"loadpage('"+data.list[i].zxdz+"')\">"+
		    		"<div class='col-sm-2 col-md-2 col-xs-2 nopadding' style='height:100%;'>"+
		    		"<span class='iconfont icon-suolvetu' style='font-size:40px !important;color:grey;line-height:200%;'></span>"+
		    		"</div>"+
		    		"<div class='col-sm-10 col-md-10 col-xs-10 nopadding' style='padding-top:15px;'>"+
		    		"<div class='col-sm-12 col-md-12 col-xs-12 nopadding text-left'>"+
		    		"<span class='col-sm-12 col-md-12 col-xs-12 nopadding' style='color:black !important;font-weight:bold !important;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>"+data.list[i].zxbt+"</span>"+
		    		"</div>"+
		    		"<div class='col-sm-12 col-md-12 col-xs-12 nopadding text-left'>"+
		    		"<span class='col-sm-12 col-md-12 col-xs-12 nopadding' style='color:grey;width:100%;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>"+zxjj+"</span>"+
		    		"</div>"+
		    		"<div class='col-sm-12 col-md-12 col-xs-12 nopadding'>"+
		    		"<div class='col-sm-9 col-md-9 col-xs-9 nopadding text-left' >"+
		    		"</div>"+
		    		"<div class='col-sm-3 col-md-3 col-xs-3 nopadding text-center' style='border-radius:15px;background-color:#94C1C1;font-size:10px;'>"+
		    		"<span style='line-height:200%;'>"+data.list[i].fbrq+"</span>"+
		    		"</div>"+
		    		"</div>"+
		    		"</div>"+
		    		"</div>";
		    	}
	    		$("#loadmore").append(html);
	    		$("#loadmore").trigger("chosen:updated");
		    }
		});
	}else{
		$("#clicktext").text("没有更多了...");
	}
}

function loadpage(url){
	window.open(url.toString());
}

//列表下拉滚动刷新数据
window.addEventListener('scroll',()=>{
    // console.log('滚动条距离顶部'+document.documentElement.scrollTop||document.body.scrollTop);
    // console.log('可视区域'+document.documentElement.clientHeight ||document.body.clientHeight);
    // console.log('滚动条内容的总高度'+document.documentElement.scrollHeight||document.body.scrollHeight);
    let scrollTop = document.documentElement.scrollTop||document.body.scrollTop ;
    let clientHeight = document.documentElement.clientHeight || document.body.clientHeight ;
    let scrollHeight = document.documentElement.scrollHeight||document.body.scrollHeight ;
    if(Math.round(scrollTop+clientHeight) == scrollHeight){
    	getmore();
    }	
});

function chance(zxzlx){
     $(this).addClass("active").parents("li").siblings().find("a").removeClass("active");
     $("#info").load("/ws/news/getWechatNewsList?zxlx="+$("#zxlx").val()+"&zxzlx="+zxzlx);
     $("#zxzlx").val(zxzlx);
};
window.addEventListener('load',function(){
  var $navBox = document.getElementById('navBox'),
     $ul = $navBox.querySelector('ul'),
     liArray = $navBox.querySelectorAll('li'),
     liLength = $navBox.querySelectorAll('li').length;
  $ul.style.width = (liArray[0].clientWidth + 10)*liLength +"px";
  var carousel=new iScroll("carousel",{hScrollbar:false, vScrollbar:false, vScroll: false});
});


$(function(){
	if($("#count").val()==10){
		$("#clicktext").text("下拉加载更多...");
	}else{
		$("#clicktext").text("没有更多了...");
	}
})