var loadWeekMarketStatis=function(){
	var _eventTag = "weekMarketStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_market_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"ybqk",
			chart: null,
			el: null,
			render:function(data,searchData){
				var total = [];
				var prerq = "";
				if(data&&this.chart){
					var dataxAxis= new Array();
					var datasf=new Array();
					var databsf=new Array();
					var datafq=new Array();
					var databl=new Array();
					var maxY=0;
					var maxBL=0;
					var tmp_y=0;
					var intval;
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0' && data[i].rq.length>8){
							dataxAxis.push(data[i].rq.substring(5,10));
						}else if(data[i].sfjs=='0'){
							dataxAxis.push(data[i].rq);
						}
						if(data[i].sfjs=='0'){
							if(data[i].num==0)
								datafq.push('');
							else
								datafq.push(data[i].num);
						}
						else if(data[i].sfsf=='0'){
							if(data[i].num==0)
								databsf.push('');
							else
								databsf.push(data[i].num);
						}
						else if(data[i].sfsf=='1'){
							if(data[i].num==0)
								datasf.push('');
							else
								datasf.push(data[i].num);
							databl.push(data[i].bl);
						}
						if(prerq != data[i].rq){
							if(maxY < tmp_y)
								maxY = parseInt(tmp_y)
							tmp_y = 0;
							if(i!=0 && data[i-1].sfsf=='1' && maxBL < Math.abs(data[i-1].bl)){
								maxBL =Math.abs(data[i-1].bl);
							}
							prerq = data[i].rq
						}else{
							tmp_y += parseInt(data[i].num); 
						}
					}
					if(maxY < tmp_y)
						maxY = tmp_y
					if( maxBL < Math.abs(data[data.length-1].bl)){
						maxBL = Math.abs(data[data.length-1].bl);
					}
						
					maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					intval=maxY/4
					maxBL = (parseInt(maxBL/100) + 1)*100
					var pieoption = {
						title : {
							subtext : searchData.ybqk,
							x : 'right',
							y : 10
						},
						tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    legend: {
					        data: ['收费标本', '不收费标本','废弃标本'],
					        textStyle: {
					        	color: '#3E9EE1'
				            },
					        y:35
					    },
					    grid: {
					        left: 10,
					        right: 0,
					        top:90,
					        height:200,
					        containLabel: true
					    },
					    yAxis:  [{
				            name: '个数',
					        type: 'value',
					        min: 0,
				            max: maxY,
					        interval: intval
					    },
				        {
				            type: 'value',
				            name: '环比',
				            min: -maxBL,
				            max: maxBL,
				            interval: maxBL /2,
				            axisLabel: {
				                formatter: '{value} %'
				            }
				        }],
					    xAxis: {
					        type: 'category',
					        data: dataxAxis
					    },
					    series: [
					        {
					            name: '收费标本',
					            type: 'bar',
					            stack: '总量',
					            barGap: 0,
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            data: datasf,
					            color:'#37D8FF'
					        },
					        {
					            name: '废弃标本',
					            type: 'bar',
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            data: datafq,
					            color:'#2Fc6CB'
					        },
					        {
					            name: '不收费标本',
					            type: 'bar',
					            stack: '总量',
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            data: databsf,
					            color:'#FECEFF',
					        },
					        {
					            name:'环比',
					            type:'line',
					            yAxisIndex: 1,
					            data:databl,
					            smooth: true,
					            itemStyle : { normal: {label : {show: true,formatter:"{c}%"}}},
					            color:'#F5B985'
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
		{
			id:"ybxx",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				var legendData=new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							seriesData.push({value:data[i].num,name:'废弃标本'});
						}else if(data[i].sfsf=='0'){
							seriesData.push({value:data[i].num,name:'不收费标本'});
						}else if(data[i].sfsf=='1'){
							seriesData.push({value:data[i].num,name:'收费标本'});
						}
					}
				}
				var pieoption = {
						title : {
							subtext : searchData.ybxx,
							x : 'right',
							y : 10
						},
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					    color:[ '#2Fc6CB','#FECEFF','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        data: ['收费标本','不收费标本','废弃标本']
					    },
					    series : [
					        {
					            name: '',
					            type: 'pie',
					            radius : '55%',
					            center: ['50%', '60%'],
					            data:seriesData,
					            label: {
					            	normal: {
					            		  formatter:"{b}: {c}(个)"
					            	}
					            },
					            itemStyle: {
					                emphasis: {
					                    shadowBlur: 10,
					                    shadowOffsetX: 0,
					                    shadowColor: 'rgba(0, 0, 0, 0.5)'
					                },
					                
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
		},
		{
			id:"sjdw",
			chart:null,
			el:null,
			render:function(data,searchData){
				var dataxAxis= new Array();
				var datasf=new Array();
				var databsf=new Array();
				var datafq=new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							dataxAxis.push(data[i].dwjc);
							if(data[i].num!='0'){
								datafq.push(data[i].num);
							}else{
								datafq.push('');
							}
						}else if(data[i].sfsf=='0'){
							if(data[i].num!='0'){
								databsf.push(data[i].num)
							}else{
								databsf.push('');
							}
						}else if(data[i].sfsf=='1'){
							if(data[i].num!='0'){
								datasf.push(data[i].num)
							}else{
								datasf.push('');
							}
						}
					}
				}
				var pieoption  = {
						title : {
							subtext : searchData.sjdw,
							x : 'right',
							y : 10
						},
						tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    legend: {
					        data: ['收费标本', '不收费标本','废弃标本'],
					        textStyle: {
					        	color: '#3E9EE1'
				            },
					        y:35
					    },
					    grid: {
					        left: 20,
					        right: 20,
					        top:80,
					        height:180,
					        containLabel: true
					    },
					    yAxis:  [{
				            name: '个数',
					        type: 'value',
					        min: 0,
					        minInterval: 1
				        }],
					    xAxis: {
					        type: 'category',
					        data: dataxAxis,
					        axisLabel:{
					        	rotate:-60,
					        }
					    },
					    series: [
					        {
					            name: '收费标本',
					            type: 'bar',
					            stack: '总量',
					            barGap: 0,
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            data: datasf,
					            color:'#37D8FF'
					        },
					        {
					            name: '废弃标本',
					            type: 'bar',
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            data: datafq,
					            color:'#2Fc6CB'
					        },
					        {
					            name: '不收费标本',
					            type: 'bar',
					            stack: '总量',
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            color:'#FECEFF',
					            data: databsf
					        },
					    ]
					};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(){
				});
			}
		}
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
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = "/wechat/StatisViewBylrry";
		$.ajax({
			type : "post",
			url : _url,
			data:{"sign":$("#sign").val(),"lrry":$("#lrry").val(),"jsrqstart":$("#jsrqstart").val(),"jsrqend":$("#jsrqend").val()},
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
	echartsBtnInit(_renderArr);
}
//点击相应echarts获取相应数据
function clickWeekEcharts(_renderArr,_url,map,id){
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
//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/wechat/Week_Statis_Tj"
	var map ={}
	map["access_token"]=$("#ac_tk").val()
	map["jsrqstart"]= $("#jsrqstart").val()
	map["jsrqend"]= $("#jsrqend").val()
	map["lrry"]=$("#lrry").val()
	//标本情况按天统计
	$("#ybqk_D").unbind("click").click(function(){
		map["method"]="getYbqkByDay";
		$("#ybqksection a").css("color","#FFFFFF");
		$("#ybqksection #ybqk_D").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybqk')
	});
	//标本情况按周统计
	$("#ybqk_W").unbind("click").click(function(){
		map["method"]="getYbqkByWeek";
		$("#ybqksection a").css("color","#FFFFFF");
		$("#ybqksection #ybqk_W").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybqk')
	});
	//标本情况按月统计
	$("#ybqk_M").unbind("click").click(function(){
		map["method"]="getYbqkByMon";
		$("#ybqksection a").css("color","#FFFFFF");
		$("#ybqksection #ybqk_M").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybqk')
	});
	//标本情况按年统计
	$("#ybqk_Y").unbind("click").click(function(){
		map["method"]="getYbqkByYear";
		$("#ybqksection a").css("color","#FFFFFF");
		$("#ybqksection #ybqk_Y").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybqk')
	});
	//标本信息按天统计
	$("#ybxx_D").unbind("click").click(function(){
		map["method"]="getYbxxByDay";
		$("#ybxxsection a").css("color","#FFFFFF");
		$("#ybxxsection #ybxx_D").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybxx')
	});
	//标本信息按周统计
	$("#ybxx_W").unbind("click").click(function(){
		map["method"]="getYbxxByWeek";
		$("#ybxxsection a").css("color","#FFFFFF");
		$("#ybxxsection #ybxx_W").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybxx')
	});
	//标本信息按月统计
	$("#ybxx_M").unbind("click").click(function(){
		map["method"]="getYbxxByMon";
		$("#ybxxsection a").css("color","#FFFFFF");
		$("#ybxxsection #ybxx_M").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybxx')
	});
	//标本信息按年统计
	$("#ybxx_Y").unbind("click").click(function(){
		map["method"]="getYbxxByYear";
		$("#ybxxsection a").css("color","#FFFFFF");
		$("#ybxxsection #ybxx_Y").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'ybxx')
	});
	//送检单位按天统计
	$("#sjdw_D").unbind("click").click(function(){
		map["method"]="getSjdwByDay";
		$("#sjdwsection a").css("color","#FFFFFF");
		$("#sjdwsection #sjdw_D").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'sjdw')
	});
	//送检单位按周统计
	$("#sjdw_W").unbind("click").click(function(){
		map["method"]="getSjdwByWeek";
		$("#sjdwsection a").css("color","#FFFFFF");
		$("#sjdwsection #sjdw_W").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'sjdw')
	});
	//送检单位按月统计
	$("#sjdw_M").unbind("click").click(function(){
		map["method"]="getSjdwByMon";
		$("#sjdwsection a").css("color","#FFFFFF");
		$("#sjdwsection #sjdw_M").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'sjdw')
	});
	//送检单位按年统计
	$("#sjdw_Y").unbind("click").click(function(){
		map["method"]="getSjdwByYear";
		$("#sjdwsection a").css("color","#FFFFFF");
		$("#sjdwsection #sjdw_Y").css("color","#86777B");
		clickWeekEcharts(_renderArr,url,map,'sjdw')
	});
	
}
$('.btn-triger').click(function () {
    $(this).closest('.float-btn-group').toggleClass('open');
});
$(function(){
	loadWeekMarketStatis();
})