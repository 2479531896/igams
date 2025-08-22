var loadOperationalStatis_jcdw=function(){
	var _eventTag = "operationalStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_operational_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr=[
		{
			id:"jcdw",
			chart: null,
			el: null,
			render:function(data,searchData){
				var xAxisData=new Array();
				var seriesData=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxisData.push(data[i].rq);
					seriesData.push(data[i].num);
				}
				
				var pieoption={
				    color: ['#3398DB'],
				    title: {
				    	text:"日期",
				        subtext : searchData.zqs.jcdw,
				        left: 'center',
				    },
				    tooltip: {
				        trigger: 'axis',
				        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
				            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
				    },
				    grid: {
				        left: '3%',
				        right: '4%',
				        bottom: '3%',
				        containLabel: true
				    },
				    xAxis: [
				        {
				            type: 'category',
				            data: xAxisData,
				            axisTick: {
				                alignWithLabel: true
				            }
				        }
				    ],
				    yAxis: [
				        {
				            type: 'value'
				        }
				    ],
				    series: [
				        {
				            name: '标本测试数',
				            type: 'bar',
				            barWidth: '60%',
				            data: seriesData,
				            label: {
				                show: true,
				                position: 'inside'
				            }
				        }
				    ]
				};
				
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off('click');
				this.chart.on("click",function(param){
					var suffix="";
					$("#operationalStatis_jcdw .jcdw_a").each(function(){
						if(this.style.color=="rgb(0, 0, 0)"){
							suffix=this.id.substring(this.id.length-1,this.id.length);
							$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
							$("#operationalStatis_jcdw #sf_"+suffix).css("color","#000000");
						}
					})
					var url="/wechat/statistics/pagedataClickCategoryEcharts";
					var map ={}
					map["access_token"]=$("#ac_tk").val();
					map["jcdw"] =$("#operationalStatis_jcdw #jcdw").val();
					map["jsrq"] =param.name;
					map["index"]="sf";
					map["suffix"]=suffix;
					clickEcharts(_renderArr,url,map,'sf');
				});
			}
		},
		{
			id:"sf",
			chart: null,
			el: null,
			render:function(data,searchData){
				var legendData=new Array();
				var seriesData=new Array();
				for (var i = 0; i < data.length; i++) {
					legendData.push(data[i].sfmc);
					seriesData.push({value:data[i].num,name:data[i].sfmc,sf:data[i].sf});
				}
				var pieoption={
				    title: {
				        text: '省份',
				        subtext: searchData.zqs.sf,
				        left: 'center'
				    },
				    tooltip: {
				        trigger: 'item',
				        formatter: '{a} <br/>{b} : {c} ({d}%)'
				    },
				    legend: {
				    	 orient: 'vertical',
				         left: 'right',
				        data: legendData
				    },
				    series: [
				       
				        {
				            name: '测试数',
				            type: 'pie',
				            radius: [30, 110],
				            center: ['50%', '50%'],
				            roseType: 'area',
				            data: seriesData
				        }
				    ]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off('click');
				this.chart.on("click",function(param){
					var tjzq="";
					$("#operationalStatis_jcdw .sf_a").each(function(){
						if(this.style.color=="rgb(0, 0, 0)"){
							if(this.id=="sf_Y"){
								tjzq="year"
							}else if(this.id=="sf_M"){
								tjzq="mon"
							}else if(this.id=="sf_W"){
								tjzq="week"
							}else if(this.id=="sf_D"){
								tjzq="day"
							}
						}
					})
					$("#operationalStatis").load("/wechat/statistics/pageListOperational_Statis_sjhb?sf="+param.data.sf+"&jcdw="+$("#operationalStatis_jcdw #jcdw").val()+"&tjzq="+tjzq+"&sfmc="+param.data.name+"&jcdwmc="+$("#operationalStatis_jcdw #jcdwmc").text());
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
		var _url = "/wechat/statistics/pagedataOperational_one_jcdw";
		$.ajax({
			type : "post",
			url : _url,
			data:{
				"access_token":$("#ac_tk").val(),
				"tjzq":$("#operationalStatis #tjzq").val(),
				"jcdw":$("#operationalStatis #jcdw").val()
				},
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
					}
					if(datas.jcdw_tjzq=="year"){
						$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #jcdw_Y").css("color","#000000");
					}else if(datas.jcdw_tjzq=="mon"){
						$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #jcdw_M").css("color","#000000");
					}else if(datas.jcdw_tjzq=="week"){
						$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #jcdw_W").css("color","#000000");
					}else if(datas.jcdw_tjzq=="day"){
						$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #jcdw_D").css("color","#000000");
					}
					
					if(datas.sf_tjzq=="year"){
						$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #sf_Y").css("color","#000000");
					}else if(datas.sf_tjzq=="mon"){
						$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #sf_M").css("color","#000000");
					}else if(datas.sf_tjzq=="week"){
						$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #sf_W").css("color","#000000");
					}else if(datas.sf_tjzq=="day"){
						$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
						$("#operationalStatis_jcdw #sf_D").css("color","#000000");
					}
				}
			}
		});
	})
	echartsBtnInit(_renderArr);
}
//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/wechat/statistics/pagedataOperational_Statis_click";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jcdw"] =$("#operationalStatis_jcdw #jcdw").val();
	$("#operationalStatis_jcdw #jcdw_Y").unbind("click").click(function(){
		map["method"]="jcdw_year";
		$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #jcdw_Y").css("color","#000000");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	
	$("#operationalStatis_jcdw #jcdw_M").unbind("click").click(function(){
		map["method"]="jcdw_mon";
		$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #jcdw_M").css("color","#000000");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	
	$("#operationalStatis_jcdw #jcdw_W").unbind("click").click(function(){
		map["method"]="jcdw_week";
		$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #jcdw_W").css("color","#000000");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	
	$("#operationalStatis_jcdw #jcdw_D").unbind("click").click(function(){
		map["method"]="jcdw_day";
		$("#operationalStatis_jcdw .jcdw_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #jcdw_D").css("color","#000000");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	
	$("#operationalStatis_jcdw #sf_Y").unbind("click").click(function(){
		map["method"]="sf_year";
		$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #sf_Y").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sf');
	})
	
	$("#operationalStatis_jcdw #sf_M").unbind("click").click(function(){
		map["method"]="sf_mon";
		$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #sf_M").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sf');
	})
	
	$("#operationalStatis_jcdw #sf_W").unbind("click").click(function(){
		map["method"]="sf_week";
		$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #sf_W").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sf');
	})
	
	$("#operationalStatis_jcdw #sf_D").unbind("click").click(function(){
		map["method"]="sf_day";
		$("#operationalStatis_jcdw .sf_a").css("color","#FFFFFF");
		$("#operationalStatis_jcdw #sf_D").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sf');
	})
}

//点击相应echarts获取相应数据
function clickEcharts(_renderArr,_url,map,id){
	
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
					}
				}
			});
			break;
		}
	}
}

function init(){
	
}

/**
 * 返回上一页
 * @returns
 */
$("#operationalStatis_jcdw #backBtn").bind("click",function(){
	$("#operationalStatis").load("/wechat/statistics/operationalStatistics?tjzq="+$("#operationalStatis_jcdw #tjzq").val());
})

$(function(){
	init();
	loadOperationalStatis_jcdw();
});