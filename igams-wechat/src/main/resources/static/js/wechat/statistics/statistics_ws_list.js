//获取元素高度,解决栅格系统同行元素高度不统一的问题,由于元素在初始化可能还未加载完成，设置延时
function getheight(){
	setTimeout(function(){
		var height=$("#getheight").height();
		$(".toptitle").attr("height",height);
	},200)
}
//调整中间部分高度，实现地图部分内容随高度增加变高
function getcenterheight(){
	var pagewidth=$(window).width();
	//当屏幕宽度大于991px时执行
	if(pagewidth>991){
		setTimeout(function(){
			var centerheight=$("#centerbody").height();
			var allheight=$("#all").height();
			var height=700+allheight-centerheight;
			$("#centermap").attr("style","height:"+height+"px;width:100%;margin-top:10px;padding:0px;");
		},200)
	}
}
//旋转动画和页面载入动画
function xzAndzr(){
	 $("#outaction").rotate({animateTo: 180,duration:2000});
	 $("#inaction").rotate({animateTo: -180,duration:2000});
	 $("#xuanzhuan").fadeOut(2000);
	 setTimeout(function(){
			$("#xuanzhuan").attr("style","display:none;");
		},2000)
}
//加载统计数据
var loadSjxxStatis = function(){
	var _eventTag = "SjxxLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"yj_month",
			chart: null,
			el: null,
			render:function(data,searchData){
                        				var total = [];
                        				var prekyfx = "";
                        				if(data&&this.chart){
                        					var dataxAxis= new Array();
                        					var dataseries=new Array();
                        					var datayxlseries=new Array();
                        					var maxY=0;
                        					var maxY_jz=0;
                        					var interval;
                        					var interval_jz;
                        					for (var i = 0; i < data.length; i++) {
                        						dataxAxis.push(data[i].wzzwm);
                        						dataseries.push(Math.round(data[i].gl*100)/100);
                        						datayxlseries.push(Math.round((parseFloat(data[i].jz)+parseFloat(data[i].bzc))* 100)/100)
                        						if(maxY < parseFloat(data[i].gl))
                        							maxY = parseFloat(data[i].gl)
                        						if(maxY_jz <  (parseFloat(data[i].jz)+parseFloat(data[i].bzc)))
                                                    maxY_jz = (parseFloat(data[i].jz)+parseFloat(data[i].bzc))

                        					}
                        					maxY= maxY + 5 - (maxY%5)
                        					interval=maxY/5
                                            maxY_jz= Math.round(maxY_jz* 100)/100
                                            maxY_jz= Math.round((maxY_jz + 5 - (maxY_jz%5))* 100)/100
                                            interval_jz=maxY_jz/5
                        					var pieoption= {
                        					   tooltip : {
                                                    trigger: 'axis',
                                                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                                                    }
                                                },
                        					    legend: {
                        					        data:['物种概率','均值+标准差'],
                        					        textStyle: {
                        					        	color: '#3E9EE1'
                        				            },
                        				            y:35,
                        				            selected:{
                        				            	'均值+标准差':true
                        				            }
                        					    },
                        					    grid: {
                                                    left: '3%',
                                                    right: '4%',
                                                    bottom: '3%',
                                                    containLabel: true,
                                                    top:'30%',
                                                },
                        					    xAxis: [
                        					        {
                        					            type: 'category',

                        					            data: dataxAxis,
                        					            axisLabel: {
                                                            rotate: -60,
                                                        },
                                                        axisTick: {
                                                            alignWithLabel: true
                                                        },
                                                        axisLine: {
                                                            lineStyle: {
                                                                  color: '#FFFFFF'
                                                            },
                                                        },
                        					        }
                        					    ],
                        					    yAxis: [
                        					        {
                        					            type: 'value',
                        					            name: '物种概率',
                        					            min: 0,
                        					            max: maxY,
                        					            interval: interval,
                        					            axisLabel: {
                        					                formatter: '{value} %'
                        					            }
                        					        },
                        					        {
                        					            type: 'value',
                        					            name: '均值+标准差',
                        					            min: 0,
                        					            max: maxY,
                        					            interval: interval,
                        					            axisLabel: {
                        					                formatter: '{value} %'
                        					            }
                        					        }
                        					    ],
                        					    series: [
                        					        {
                        					            name:'物种概率',
                        					            type:'bar',
                        					            barWidth: '40%',
                                                        data:dataseries,
                                                        barGap: 0,
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'top'
                                                            }
                                                        },
                                                        itemStyle : {
                                                            normal: {
                                                                label : {show: true,formatter:"{c}%"},
                                                            }
                                                       }

                        					        },
                        					        {
                        					            name:'均值+标准差',
                        					            type:'line',
                        					            yAxisIndex: 1,
                        					            data:datayxlseries,
                        					            label: {
                                                            normal: {
                                                                show: true,
                                                                textStyle: {
                                                                    color: 'white' // 设置字体颜色为红色
                                                                }
                                                            }
                                                        },
                        					            itemStyle : {
                        					                normal: {
                        					                    label : {show: true,formatter:"{c}%"},
                                                            }
                                                       }
                        					        }
                        					    ]
                        					};
                        					this.chart.setOption(pieoption);
                        					this.chart.hideLoading();
                        					//点击事件
                        					this.chart.on("click",function(){
                        					//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                        					});
                        				}
                        			}
		},{
            id:"yj_week",
            chart: null,
            el: null,
            render:function(data,searchData){
            				var total = [];
            				var prekyfx = "";
            				if(data&&this.chart){
            					var dataxAxis= new Array();
            					var dataseries=new Array();
            					var datayxlseries=new Array();
            					var maxY=0;
            					var maxY_jz=0;
            					var interval;
            					var interval_jz;
            					for (var i = 0; i < data.length; i++) {
            						dataxAxis.push(data[i].wzzwm);
            						dataseries.push(Math.round(data[i].gl*100)/100);
            						datayxlseries.push(Math.round((parseFloat(data[i].jz)+parseFloat(data[i].bzc))* 100)/100)
            						if(maxY < parseFloat(data[i].gl))
            							maxY = parseFloat(data[i].gl)
            						if(maxY_jz <  (parseFloat(data[i].jz)+parseFloat(data[i].bzc)))
                                        maxY_jz = (parseFloat(data[i].jz)+parseFloat(data[i].bzc))

            					}
            					maxY= maxY + 5 - (maxY%5)
            					interval=maxY/5
                                maxY_jz= Math.round(maxY_jz* 100)/100
                                maxY_jz= Math.round((maxY_jz + 5 - (maxY_jz%5))* 100)/100
                                interval_jz=maxY_jz/5
            					var pieoption= {
            					   tooltip : {
                                        trigger: 'axis',
                                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                                        }
                                    },
            					    legend: {
            					        data:['物种概率','均值+标准差'],
            					        textStyle: {
            					        	color: '#3E9EE1'
            				            },
            				            y:35,
            				            selected:{
            				            	'均值+标准差':true
            				            }
            					    },
            					    grid: {
                                        left: '3%',
                                        right: '4%',
                                        bottom: '3%',
                                        containLabel: true,
                                        top:'30%',
                                    },
            					    xAxis: [
            					        {
            					            type: 'category',

            					            data: dataxAxis,
            					            axisLabel: {
                                                rotate: -60,
                                            },
                                            axisTick: {
                                                alignWithLabel: true
                                            },
                                            axisLine: {
                                                lineStyle: {
                                                      color: '#FFFFFF'
                                                },
                                            },
            					        }
            					    ],
            					    yAxis: [
            					        {
            					            type: 'value',
            					            name: '物种概率',
            					            min: 0,
            					            max: maxY,
            					            interval: interval,
            					            axisLabel: {
            					                formatter: '{value} %'
            					            }
            					        },
            					        {
            					            type: 'value',
            					            name: '均值+标准差',
            					            min: 0,
            					            max: maxY,
            					            interval: interval,
            					            axisLabel: {
            					                formatter: '{value} %'
            					            }
            					        }
            					    ],
            					    series: [
            					        {
            					            name:'物种概率',
            					            type:'bar',
            					            barWidth: '40%',
                                            data:dataseries,
                                            barGap: 0,
                                            label: {
                                                normal: {
                                                    show: true,
                                                    position: 'top'
                                                }
                                            },
                                            itemStyle : {
                                                normal: {
                                                    label : {show: true,formatter:"{c}%"},
                                                }
                                           }

            					        },
            					        {
            					            name:'均值+标准差',
            					            type:'line',
            					            yAxisIndex: 1,
            					            data:datayxlseries,
            					            label: {
                                                normal: {
                                                    show: true,
                                                    textStyle: {
                                                        color: 'white' // 设置字体颜色为红色
                                                    }
                                                }
                                            },
            					            itemStyle : {
            					                normal: {
            					                    label : {show: true,formatter:"{c}%"},
                                                }
                                           }
            					        }
            					    ]
            					};
            					this.chart.setOption(pieoption);
            					this.chart.hideLoading();
            					//点击事件
            					this.chart.on("click",function(){
            					//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
            					});
            				}
            			}
        },

	];
	
	// 路径配置
	setTimeout(function(){
		for(var i=0; i<_renderArr.length; i++){
			var _render = _renderArr[i];
			var _el = _render.el = document.getElementById(_idPrefix+_render.id);
			if(_el){
				_render.chart = echarts.init(_el,_statis_theme);
				_render.eventTag = "."+ _eventTag+"-"+_render.id;
				if(_isShowLoading){
					_render.chart.showLoading({
						effect:_loadEffect
					});
				}
				$(window).off(_render.eventTag).on("resize"+_render.eventTag, (function(_cfg){return function(){// resize事件
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		if(setFlag==false){
		    setFlag=true
		    //加载数据
            var _url = "/ws/statistics/pagedataSjxxStatis";
            $.ajax({
                type : "post",
                url : _url,
                data:{"access_token":$("#ac_tk").val(),"csid":$("#csid").val()},
                dataType : "json",
                success : function(datas) {
                    if(datas){
                        var _searchData = datas['searchData'];
                        for(var i=0; i<_renderArr.length; i++){
                            var _render = _renderArr[i];
                            var _data = datas[_render.id];
                            _render.render(_data,_searchData);
                        }
                         base64List= datas['base64List'];
                         var imgDoc=""
                         for(var i=0; i<base64List.length; i++){
                            var str='data:image/png;base64,'+base64List[i]
                            imgDoc += "<img id='imgDto"+i+"' class='pptimg' src='"+str+"'/>"
                         }
                         $("#imglist").append(imgDoc)
                         $("#imglist").hide()


                        if(base64List!=null&&base64List.length!=0){
                            setTimeout(setTimeoutTjnr,$("#qhsj").val()*1000)

                        }
                    }else{
                        //throw "loadClientStatis数据获取异常";
                    }
                }
            });
		}

	});
	
	echartsBtnInit(_renderArr);
}
var showTjnr=true;
var index=0
var ischange =false
var ischange_tjnr =false
var list;
var setFlag=false
var base64List;
function changeImgList(){
    ischange=true
    if(index>base64List.length){
        index=0;
    }
    $("#imgDto"+index).show()
    $("#imgDto"+index).siblings().hide()
    index++;
    setTimeout(changeImgList,$("#pptsj").val()*1000)
}
function setTimeoutTjnr(){
    if(ischange_tjnr==false){
        setTimeoutTjnr_t()
    }
}
function setTimeoutTjnr_t(){
    ischange_tjnr=true
    if(showTjnr==true){
            $("#sj_statis").hide()
            $("#imglist").show()
            showTjnr=false
            if(ischange==false){
                changeImgList()
            }

        }else{
            showTjnr=true
            $("#sj_statis").show()
            $("#imglist").hide()

        }
        setTimeout(setTimeoutTjnr_t,$("#qhsj").val()*1000)
}
//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/ws/statistics/pagedataSjxxStatisByTj";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	//标本信息的所有统计按钮
	$("#sj_statis #tj1q").unbind("click").click(function(){
		initEchartsBtnCss("tj1q");
		map["method"]="getYblxByYear";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//标本信息的月统计按钮
	$("#sj_statis #tj1m").unbind("click").click(function(){
		initEchartsBtnCss("tj1m");
		map["method"]="getYblxByMon";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//标本信息的周统计按钮
	$("#sj_statis #tj1w").unbind("click").click(function(){
		initEchartsBtnCss("tj1w");
		map["method"]="getYblxByWeek";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//标本信息的日统计按钮
	$("#sj_statis #tj1d").unbind("click").click(function(){
		initEchartsBtnCss("tj1d");
		map["method"]="getYblxByDay";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//关注度的所有统计按钮
	$("#sj_statis #tj2q").unbind("click").click(function(){
		initEchartsBtnCss("tj2m");
		map["method"]="getGgzdByYear";
		clickSjxxEcharts(_renderArr,url,map,'ggzd');
	})
	//关注度的月统计按钮
	$("#sj_statis #tj2m").unbind("click").click(function(){
		initEchartsBtnCss("tj2m");
		map["method"]="getGgzdByMon";
		clickSjxxEcharts(_renderArr,url,map,'ggzd');
	})
	//关注度的周统计按钮
	$("#sj_statis #tj2w").unbind("click").click(function(){
		initEchartsBtnCss("tj2w");
		map["method"]="getGgzdByWeek";
		clickSjxxEcharts(_renderArr,url,map,'ggzd');
	})
	//合作伙伴的所有统计按钮
	$("#sj_statis #tj3q").unbind("click").click(function(){
		initEchartsBtnCss("tj3q");
		map["method"]="getDbByYear";
		map["tj"]="mon";
		clickSjxxEcharts(_renderArr,url,map,'db');
	})
	//合作伙伴的月统计按钮
	$("#sj_statis #tj3m").unbind("click").click(function(){
		initEchartsBtnCss("tj3m");
		map["method"]="getDbByMon";
		clickSjxxEcharts(_renderArr,url,map,'db');
		
	})
	//合作伙伴的周统计按钮
	$("#sj_statis #tj3w").unbind("click").click(function(){
		initEchartsBtnCss("tj3w");
		map["method"]="getDbByWeek";
		clickSjxxEcharts(_renderArr,url,map,'db');
	})
	//合作伙伴的日统计按钮
	$("#sj_statis #tj3d").unbind("click").click(function(){
		initEchartsBtnCss("tj3d");
		map["method"]="getDbByDay";
		clickSjxxEcharts(_renderArr,url,map,'db');
	})
	
	//送样-报告-阳性率的所有统计按钮
	$("#sj_statis #tj4q").unbind("click").click(function(){
		initEchartsBtnCss("tj4q");
		map["method"]="getJsrqByYear";
		clickSjxxEcharts(_renderArr,url,map,'jsrq');
	})
	//送样-报告-阳性率的月统计按钮
	$("#sj_statis #tj4m").unbind("click").click(function(){
		initEchartsBtnCss("tj4m");
		map["method"]="getJsrqByMon";
		clickSjxxEcharts(_renderArr,url,map,'jsrq');
	})
	//送样-报告-阳性率的周统计按钮
	$("#sj_statis #tj4w").unbind("click").click(function(){
		initEchartsBtnCss("tj4w");
		map["method"]="getJsrqByWeek";
		clickSjxxEcharts(_renderArr,url,map,'jsrq');
	})
	
	//标本情况的年统计按钮
	$("#sj_statis #tj5q").unbind("click").click(function(){
		initEchartsBtnCss("tj5q");
		map["method"]="getSjxxYearBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的月统计按钮
	$("#sj_statis #tj5m").unbind("click").click(function(){
		initEchartsBtnCss("tj5m");
		map["method"]="getSjxxMonBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的周统计按钮
	$("#sj_statis #tj5w").unbind("click").click(function(){
		initEchartsBtnCss("tj5w");
		map["method"]="getSjxxWeekBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的周统计按钮
	$("#sj_statis #tj5d").unbind("click").click(function(){
		initEchartsBtnCss("tj5d");
		map["method"]="getSjxxDayBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	
	//检验结果的所有统计按钮
	$("#sj_statis #tj6q").unbind("click").click(function(){
		initEchartsBtnCss("tj6q");
		map["method"]="getPossibleByYear";
		clickSjxxEcharts(_renderArr,url,map,'possible');	
	})
	//检验结果的月统计按钮
	$("#sj_statis #tj6m").unbind("click").click(function(){
		initEchartsBtnCss("tj6m");
		map["method"]="getPossibleByMon";
		clickSjxxEcharts(_renderArr,url,map,'possible');})
	//检验结果的周统计按钮
	$("#sj_statis #tj6w").unbind("click").click(function(){
		initEchartsBtnCss("tj6w");
		map["method"]="getPossibleByWeek";
		clickSjxxEcharts(_renderArr,url,map,'possible');})
}

//设置按钮样式
function initEchartsBtnCss(getid,_renderArr){
	var obj = $("#sj_statis #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#sj_statis #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#sj_statis #"+otherid).removeClass("bechoosed");
		$("#sj_statis #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="全"){
		$("#sj_statis #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#sj_statis #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#sj_statis #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#sj_statis #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}
}

$("#ybxxhead a").hover(function(){
	var getid=$(this).attr("id");
	var gettitle=$(this).attr("title");
	$("#"+getid+" .fa").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
},function(){
	var getid=$(this).attr("id");
	var gettitle=$(this).attr("title");
	var getclass=$(this).attr("class");
	var b=getclass.indexOf("bechoosed");
	if(b=="-1"){
		$("#"+getid+" .fa").removeAttr("style");
	}
})

function initpage(){
	$.ajax({ 
	    type:'post',  
	    url:"/ws/statistics/pagedataListDtData",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	
	    	//返回值
	    	$("#sj_statis #jrybs").text(data.dtybsl.num);
	    	$("#sj_statis #jrbgs").text(data.dtbgsl.num);
	    	$("#sj_statis #jrlrybs").text(data.dtlrybsl.num);
	    	$("#sj_statis #jrsys").text(data.symap.num);
	    	$("#sj_statis #jrggs").text("0");
	    	$("#sj_statis #jrdgs").text("0");
	    	if(data.dtggdgsl.length>0){
		    	for(var i=0;i<data.dtggdgsl.length;i++){
		    		if(data.dtggdgsl[i].lx=="阳性"){
		    			$("#sj_statis #jrggs").text(data.dtggdgsl[i].num);
		    			var jrggsEnd=data.dtggdgsl[i].num;
		    		}
		    		if(data.dtggdgsl[i].lx=="疑似"){
		    			$("#sj_statis #jrdgs").text(data.dtggdgsl[i].num);
		    			var jrdgsEnd=data.dtggdgsl[i].num;
		    		}
		    	}
	    	}
	    	var jrybs={
	    		    el:$(".jrybsChange"),
	    		    start:$("#sj_statis #jrybs").text(),//初始值
	    		    end:data.dtybsl.num//结束值
	    		  };
	    	numChange(jrybs);
	    	
//	    	var jrbgs={
//	    		    el:$(".jrbgsChange"),
//	    		    start:$("#sj_statis #jrbgs").text(),//初始值
//	    		    end:data.dtbgsl.num//结束值
//	    		  };
//	    	numChange(jrbgs);
	    	
	    	var jrlrybs={
	    		    el:$(".jrlrybsChange"),
	    		    start:$("#sj_statis #jrlrybs").text(),//初始值
	    		    end:data.dtlrybsl.num//结束值
	    		  };
	    	numChange(jrlrybs);
	    	
	    	var jrsys={
	    		    el:$(".jrsysChange"),
	    		    start:$("#sj_statis #jrsys").text(),//初始值
	    		    end:data.symap.num//结束值
	    		  };
	    	numChange(jrsys);
	    	
	    	var jrggs={
	    		    el:$(".jrggsChange"),
	    		    start:$("#sj_statis #jrggs").text(),//初始值
	    		    end:jrggsEnd//结束值
	    		  };
	    	numChange(jrggs);
	    	
	    	var jrdgs={
	    		    el:$(".jrdgsChange"),
	    		    start:$("#sj_statis #jrdgs").text(),//初始值
	    		    end:jrggsEnd//结束值
	    		  };
	    	numChange(jrdgs);
	    }
	});
}
//定时更新统计数据
var num=0;
var time;
function settime(){
	time=setInterval(function () {
		loadSjxxStatis();
	}, 300000);
}
function cleartime(){
	clearInterval(time);
}
//监听窗口缩小或者切换选项卡时关闭定时器
document.addEventListener("visibilitychange", function(){
    if(document.hidden==false){
    	settime();
    }else{
    	cleartime();
    }
});


//点击各个统计图中的按钮的共通处理方法
function clickSjxxEcharts(_renderArr,_url,map,id){
	for(var i=0; i<_renderArr.length; i++){
		if(id ==  _renderArr[i].id){
			var _render = _renderArr[i];
			//加载数据
			$.ajax({
				type : "post",
				url : _url,
				data: map,
				dataType : "json",
				success : function(datas) {
					if(datas){
						var _searchData = datas['searchData'];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
					}else{
						//throw "loadClientStatis数据获取异常";
					}
				}
			});
			break;
		}
	}
}

//数字变动动画
function numChange(obj){
	var item=obj.el;
	var end=parseInt(obj.end);
	var start = parseInt(obj.start);
	var difference=Math.abs(end-start);
	var millisecond=8000;
	var num=1;
	var second=parseInt(millisecond/difference);
	if(start<end){
		time=setInterval(function(){
			start=start+num;
			if(start>end){
		        start=end;
		        clearInterval(time);
			}
			item.text(start)
		},
		second)
	}else if(start>end){
		time=setInterval(function(){
			start=start-num;
			if(start<end){
				start=end;
				clearInterval(time)
			}
			item.text(start)
		},
		second)
	}
}

$(function(){
	loadSjxxStatis();
	settime();
	initpage();
	getheight();
	xzAndzr();
	//getcenterheight();
});
