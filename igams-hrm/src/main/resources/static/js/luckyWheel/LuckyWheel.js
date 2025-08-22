var myLucky;
var prizes = [];
var MaxSpeed = 20;
var MinSpeed = 5;
var speed = 2;
var isStart = false;
var jsrq; //结束日期
var ksrq; //开始日期
var urlPrefix = $("#urlPrefix").val();
var isHave = false; //是否已抽过
var result = false;
var pageStart = -1;
var pageSize = 5;
var isAfter = true;
$(document).ready(function() {
	getPrizesAndInitLuckyWheel()
});

//初始化大转盘
function luckyWheel() {
	let bodyheight = $(document.body).height();
	let bodywidth = $(document.body).width();
	var luckyWheel = new LuckyCanvas.LuckyWheel('#my-lucky', {
		width: bodywidth - 30,
		height: bodywidth - 30,
		blocks: [{
			padding: '90px',
			imgs: [{
				src: '/images/luckCanvas/wheelBackgroundRed.png',
				width: '100%',
				rotate: true
			}]
		}],
		prizes: prizes,
		buttons: [{
			radius: '50%',
			imgs: [{
				src: '/images/luckCanvas/arrowButton.png',
				width: '200%',
				top: '-200%'
			}]
		}],
		defaultConfig: {
			speed: MinSpeed
		},
		start: function() {
			if (isHave) {
				$(".alertText").html("很抱歉！<br>您已抽过奖！");
				$(".zzc").show();
				$(".alertDiv").show();
			} else {
                let jsrqDate = new Date(jsrq +" 23:59:59");
                //判断是否在活动时间内
                if (new Date() > jsrqDate) {
                    $(".alertText").html("很抱歉！<br>活动已结束！");
                    $(".zzc").show();
                    $(".alertDiv").show();
                    return;
                }
                let ksrqDate = new Date(ksrq);
                //判断是否在活动时间内
                if (new Date() < ksrqDate) {
                    $(".alertText").html("很抱歉！<br>活动未开始！");
                    $(".zzc").show();
                    $(".alertDiv").show();
                    return;
                }
				// 开始游戏
				myLucky.play();
				isStart = true;
				speedUp();
				getResult();
			}
		},
		end: function(e) {
			$(".alertText").html((result ? "恭喜中奖！" : "很遗憾") + "<br>" + (result ? "抽中了“" + e.fonts[0].text +
				"”!" : e.fonts[0].text));
			$(".zzc").show();
			$(".alertDiv").show();
		}
	})
	return luckyWheel;
}

//获取奖品,并初始化大转盘
function getPrizesAndInitLuckyWheel() {
	prizes = [];
	let color1 = "#FFF4F4"
	let color2 = "#FFFFFF"
	$.ajax({
		type: "POST",
		url: urlPrefix + "/award/award/minidataGetLotteryInfo",
		data: {
			"access_token": $("#access_token").val(),
			"jpglid": $("#jpglid").val()
		},
		dataType: "json",
		success: function(data) {
			if (data.jpglDto) {
				if (data.jpglDto.bt) {
					document.title = data.jpglDto.bt;
				}
				if (data.jpglDto.jsrq) {
					jsrq = data.jpglDto.jsrq;
				}
				if (data.jpglDto.ksrq) {
					ksrq = data.jpglDto.ksrq;
				}
			}
			if (data.isHave) {
				isHave = data.isHave;
			}
			if (data.jpmxDtos && data.jpmxDtos.length > 0) {
				for (var i = 0; i < data.jpmxDtos.length; i++) {
					let prize = {
						jpmxid: data.jpmxDtos[i].jpmxid,
						background: i % 2 == 1 ? color1 : color2,
						fonts: [{
							text: data.jpmxDtos[i].jpmc,
							fontColor: '#D4862E',
							fontSize: '30px',
							top: '15%',
							lengthLimit: '85%'
						}],
						imgs: [{
							src: data.jpmxDtos[i].wjlj?data.jpmxDtos[i].wjlj:'/images/luckCanvas/img' + (i + 1 >= 6 ? 1 : i + 1) + '.png',
							width: '100px',
							top: '50%',
						}]
					};
					prizes.push(prize)
				}
				myLucky = luckyWheel();
			}
		},
		error: function(data) {
			$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
		}
	});
}

//获取中奖结果
function getResult() {
	isStart = false;
	speedDown();
	$.ajax({
		type: "POST",
		url: urlPrefix + "/award/award/minidataGetLotteryResult",
		data: {
			"access_token": $("#access_token").val(),
			"jpglid": $("#jpglid").val()
		},
		dataType: "json",
		success: function(data) {
			if (data) {
				if (data.jpmxDto) {
					for (let i = 0; i < prizes.length; i++) {
						if (prizes[i].jpmxid == data.jpmxDto.jpmxid) {
							myLucky.stop(i);
							isHave = true
							break;
						}
					}
				} else {
					$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
					return;
				}
				if (data.result != null) {
					result = data.result;
				} else {
					$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
					return;
				}
			} else {
				$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
				return;
			}
		},
		error: function(data) {
			$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
			return;
		}
	});
}


//获取中奖历史纪录
function getHistory(flag) {
	$.ajax({
		type: "POST",
		url: urlPrefix + "/award/award/minidataGetLotteryRecords",
		data: {
			"access_token": $("#access_token").val(),
			"jpglid": $("#jpglid").val(),
			"pageSize": 5,
			"pageStart":pageStart
		},
		dataType: "json",
		success: function(data) {
			if (data) {
                $(".alertText2").html("——— 中奖纪录 ———");
                var html = ""
                if(data.personal){
                    html += '<div class="weui-flex" style="border-bottom: 2px solid whitesmoke;">'
							+'<div style="flex:1">我</div>'
							+'<div style="flex:2;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">'+data.personal.jpmc+'</div>'
							+'</div>';
                }
                if(data.JpjgDtos && data.JpjgDtos.length>0){
                    //下一页
                    if(flag){
                        if(data.JpjgDtos.length < pageSize){
                            isAfter = false;
                        } else{
                            isAfter = true;
                        }
                    }
					for (var i = 0; i < data.JpjgDtos.length; i++) {
						html += '<div class="weui-flex" style="font-size: smaller;">'
								+'<div style="flex:1">'+data.JpjgDtos[i].zsxm+'</div>'
								+'<div style="flex:2;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">'+data.JpjgDtos[i].jpmc+'</div>'
								+'</div>';
					}
                }else {
                    if(flag){
                        isAfter = false
                    }
                }
                initNextButton(flag)
                $(".alertText3").html(html);
                $(".zzc").show();
                $(".alertDiv2").show();
			} else {
				$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
				return;
			}
		},
		error: function(data) {
			$.toptip("网络错误，请稍后再试！" + JSON.stringify(data), 5000, 'error');
			return;
		}
	});
}

//上一页
function upPage(){
	pageStart --;
    getHistory(false);
	isAfter = true;
}
//下一页
function downPage(){
	pageStart ++;
    getHistory(true);
}
//减速
function speedDown() {
	delayedLoop(MaxSpeed, MinSpeed, 500)
}
//加速
function speedUp() {
	delayedLoop(MinSpeed, MaxSpeed, 500)
}

//延时
function delayedLoop(start, end, delay) {
	speed = start;
	if (start > end && !isStart) {
		myLucky.defaultConfig.speed = speed;
		setTimeout(() => {
			delayedLoop(start - 1, end, delay);
		}, delay);
	} else if (start < end && isStart) {
		myLucky.defaultConfig.speed = start;
		setTimeout(() => {
			delayedLoop(start + 1, end, delay);
		}, delay);
	}
}

//隐藏弹框
function hideAlert() {
	$(".zzc").hide();
	$(".alertDiv").hide();
	$(".alertDiv2").hide();
	$(".alertText").html("")
	$(".alertText2").html("")
	$(".alertText3").html("")
	pageStart = -1;
	isAfter = true;
}

//初始化按钮
function initNextButton(flag){
    //上一页按钮
    if(pageStart >= 1){
        $(".weui-btn_up").show();
    } else {
        $(".weui-btn_up").hide();
    }
    //下一页按钮
    if(isAfter){
        $(".weui-btn_down").show();
    } else {
        $(".weui-btn_down").hide();
    }

}