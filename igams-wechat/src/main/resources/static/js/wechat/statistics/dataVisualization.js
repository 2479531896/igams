/**
 * 页面加载查询当前总条数
 * @returns
 */
function Page_Inspection(){
	  var lrsjEnd=getNowFormatDate;
	  $("#lrsjStart").text(lrsjEnd);
	$.ajax({
		url:'/wechat/statistics/pagedataListSjxx',
		type:'post',
		data:{"access_token":$("#ac_tk").val(),"lrsjEnd":lrsjEnd},
		dateType:'JSON',
		success:function(map){
			if(map.data!=null){
				for (var i = 0; i < map.data.length; i++) {
					var	tab="<li>";
						tab+="<div>"+map.data[i].lrsj+"</div>";
						tab+="<div>"+map.data[i].hzxm+"</div>";
						tab+="<div>"+map.data[i].jcxm+"</div>";
						tab+="<div>"+map.data[i].db+"</div>";
						tab+="</li>";
					$("#list_div").append(tab);
				}
			}
		}
	})
}


/**
 * 定时任务查看更新条数
 * @returns
 */
function setTime_Inspection(){
	var lrsjStart= $("#lrsjStart").text();
    var lrsjEnd=getNowFormatDate;
    var remove_num=$("#remove_num").text();
	$("#lrsjStart").text(lrsjEnd);
	if(lrsjStart!=null && lrsjEnd!=null){
		$.ajax({
			url:'/wechat/statistics/pagedataSjxxBylrsj',
			type:'post',
			data:{"access_token":$("#ac_tk").val(),"lrsjEnd":lrsjEnd,"lrsjStart":lrsjStart},
			dateType:'JSON',
			success:function(map){
				if(map.data!=null){
					$("#remove_num").text(map.data.length);
					if(remove_num){
				    	 for (var i = 0; i < remove_num; i++) {
				        	 $("#list_div").children("li:last").remove();
				    	}
				    }
					for (var i = 0; i < map.data.length; i++) {
						var	tab="<li>";
							tab+="<div>"+map.data[i].lrsj+"</div>";
							tab+="<div>"+map.data[i].hzxm+"</div>";
							tab+="<div>"+map.data[i].jcxm+"</div>";
							tab+="<div>"+map.data[i].db+"</div>";
							tab+="</li>";
						$("#list_div").append(tab);
					}
				}
					rolling(map.data.length+1);
			}
		})
	}
}


/**
 * 页面滚动调用方法
 * @returns
 */
function rolling(num){
	var $this = $(".sjxxList");
	scrollNews($this,num);
}

/**
 * 页面滚动实现方法
 * @param obj
 * @returns
 */
function scrollNews(obj,num) {
    var $self = obj.find("ul");
    var lineHeight = 55*(num-1);
    $self.animate({
        "marginTop": -lineHeight + "px"
    }, 1000, function () {
    	for (var i = 0; i <  num-1; i++) {
    		$self.css({
                marginTop: 0
            }).find("li:first").appendTo($self);
		}
    })
}
/**
 * 定时器定时刷新
 * @returns
 */
var timer;
function TimerTask(){
	timer=setInterval(function(){ 
		setTime_Inspection();
		}, 600000);
}
/**
 * 监听窗口缩小或者切换选项卡时关闭定时器
 * @returns
 */
document.addEventListener("visibilitychange", function(){
    if(document.hidden==false){
    	TimerTask();
    }else{
    	clearInterval(timer);
    }
});

/**
 * js获取时间格式为"yyyy-mm-dd HH:mm:ss"
 * @returns
 */
function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    var strHours = date.getHours();
	    var strMinutes = date.getMinutes();
	    var strSeconds = date.getSeconds();
	    
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    if (strHours >= 0 && strHours <= 9) {
	        strHours = "0" + strHours;
	    }
	    if (strMinutes >= 0 && strMinutes <= 9) {
	        strMinutes = "0" + strMinutes;
	    }
	    if (strSeconds >= 0 && strSeconds <= 9) {
	        strSeconds = "0" + strSeconds;
	    }
	    
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + strHours + seperator2 + strMinutes
	            + seperator2 + strSeconds;
	    return currentdate;
	}

/**
 * 页面加载
 * @returns
 */
$(function(){
	Page_Inspection();
	TimerTask();
})