
var loadOperationalStatis_sf=function(){
	var _eventTag = "operationalStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_operational_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr=[
		{
			id:"sjsf",
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
				        subtext : searchData.zqs.sjsf,
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
					$("#operationalStatis_sjhb .sjsf_a").each(function(){
						if(this.style.color=="rgb(0, 0, 0)"){
							suffix=this.id.substring(this.id.length-1,this.id.length);
							$("#operationalStatis_sjhb .sjhb_a").css("color","#FFFFFF");
							$("#operationalStatis_sjhb #sjhb_"+suffix).css("color","#000000");
						}
					})
					var url="/wechat/statistics/pagedataClickCategoryEcharts";
					var map ={}
					map["access_token"]=$("#ac_tk").val();
					map["jcdw"] =$("#operationalStatis_sjhb #jcdw").val();
					map["sf"]=$("#operationalStatis_sjhb #sf").val();
					map["jsrq"] =param.name;
					map["index"]="sjhb";
					map["suffix"]=suffix;
					clickEcharts(_renderArr,url,map,'sjhb');
				});
			}
		},
		{
			id:"sjhb",
			chart: null,
			el: null,
			render:function(data,searchData){
				var legendData=new Array();
				var seriesData=new Array();
				for (var i = 0; i < data.length; i++) {
					legendData.push(data[i].hbmc);
					seriesData.push({value:data[i].num,name:data[i].hbmc});
				}
				var pieoption= {
				    title: {
				        text: '合作伙伴',
				        subtext: searchData.zqs.sjhb,
				        left: 'center'
				    },
				    tooltip: {
				        trigger: 'item',
				    },
				    legend: {
				        left: 'center',
				        top: 'bottom',
				        data: legendData
				    },
				    series: [
				        {
				        	name: '合作伙伴',
				            type: 'pie',
				            radius: ['30%', '70%'],
				            avoidLabelOverlap: false,
				            itemStyle: {
				                borderRadius: 10,
				                borderColor: '#fff',
				                borderWidth: 2
				            },
				            data: seriesData
				        }
				    ]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off('click');
				this.chart.on("click",function(param){
					$("#operationalStatis_sjhb .sjhb_a").each(function(){
						if(this.style.color=="rgb(0, 0, 0)"){
							if(this.id=="sjhb_Y"){
								tjzq="year"
							}else if(this.id=="sjhb_M"){
								tjzq="mon"
							}else if(this.id=="sjhb_W"){
								tjzq="week"
							}else if(this.id=="sjhb_D"){
								tjzq="day"
							}
						}
					})
					$("#operationalStatis").load("/wechat/statistics/pageStatisticsOperational_Statis_hbmc?sf="+$("#operationalStatis_sjhb #sf").val()+"&jcdw="+$("#operationalStatis_sjhb #jcdw").val()+"&db="+param.data.name+"&tjzq="+tjzq+"&sfmc="+$("#operationalStatis_sjhb #sfmc").text()+"&jcdwmc="+$("#operationalStatis_sjhb #jcdwmc").val());
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
		var _url = "/wechat/statistics/pagedataOperational_Statis_sjhb_echarts";
		$.ajax({
			type : "post",
			url : _url,
			data:{
				"access_token":$("#ac_tk").val(),
				"tjzq":$("#operationalStatis_sjhb #tjzq").val(),
				"jcdw":$("#operationalStatis_sjhb #jcdw").val(),
				"sf":$("#operationalStatis_sjhb #sf").val(),
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
					
					if(datas.sjsf_tjzq=="year"){
						$("#operationalStatis_sjhb .sjsf_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjsf_Y").css("color","#000000");
					}else if(datas.sjsf_tjzq=="mon"){
						$("#operationalStatis_sjhb sjsf_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjsf_M").css("color","#000000");
					}else if(datas.sjsf_tjzq=="week"){
						$("#operationalStatis_sjhb sjsf_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjsf_W").css("color","#000000");
					}else if(datas.sjsf_tjzq=="day"){
						$("#operationalStatis_sjhb sjsf_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjsf_D").css("color","#000000");
					}
					
					if(datas.sjhb_tjzq=="year"){
						$("#operationalStatis_sjhb .sjhb_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjhb_Y").css("color","#000000");
					}else if(datas.sjhb_tjzq=="mon"){
						$("#operationalStatis_sjhb sjhb_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjhb_M").css("color","#000000");
					}else if(datas.sjhb_tjzq=="week"){
						$("#operationalStatis_sjhb sjhb_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjhb_W").css("color","#000000");
					}else if(datas.sjhb_tjzq=="day"){
						$("#operationalStatis_sjhb sjhb_a").css("color","#FFFFFF");
						$("#operationalStatis_sjhb #sjhb_D").css("color","#000000");
					}
				}
			}
		});
	});
	echartsBtnInit(_renderArr);
}

//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/wechat/statistics/pagedataChangeTjzq_Statis_sjhb_echarts";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jcdw"] =$("#operationalStatis_sjhb #jcdw").val();
	map["sf"]=$("#operationalStatis_sjhb #sf").val();
	
	$("#operationalStatis_sjhb #sjsf_Y").unbind("click").click(function(){
		map["method"]="sjsf_year";
		$("#operationalStatis_sjhb .sjsf_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjsf_Y").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjsf');
	})
	$("#operationalStatis_sjhb #sjsf_M").unbind("click").click(function(){
		map["method"]="sjsf_mon";
		$("#operationalStatis_sjhb .sjsf_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjsf_M").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjsf');
	})
	
	$("#operationalStatis_sjhb #sjsf_W").unbind("click").click(function(){
		map["method"]="sjsf_week";
		$("#operationalStatis_sjhb .sjsf_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjsf_W").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjsf');
	})
	
	$("#operationalStatis_sjhb #sjsf_D").unbind("click").click(function(){
		map["method"]="sjsf_day";
		$("#operationalStatis_sjhb .sjsf_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjsf_D").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjsf');
	})
	
	$("#operationalStatis_sjhb #sjhb_Y").unbind("click").click(function(){
		map["method"]="sjhb_year";
		$("#operationalStatis_sjhb .sjhb_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjhb_Y").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjhb');
	})
	
	$("#operationalStatis_sjhb #sjhb_M").unbind("click").click(function(){
		map["method"]="sjhb_mon";
		$("#operationalStatis_sjhb .sjhb_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjhb_M").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjhb');
	})
	
	$("#operationalStatis_sjhb #sjhb_W").unbind("click").click(function(){
		map["method"]="sjhb_week";
		$("#operationalStatis_sjhb .sjhb_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjhb_W").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjhb');
	})
	
	$("#operationalStatis_sjhb #sjhb_D").unbind("click").click(function(){
		map["method"]="sjhb_day";
		$("#operationalStatis_sjhb .sjhb_a").css("color","#FFFFFF");
		$("#operationalStatis_sjhb #sjhb_D").css("color","#000000");
		clickEcharts(_renderArr,url,map,'sjhb');
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
					}else{
						
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
$("#operationalStatis_sjhb #backBtn").bind("click",function(){
	$("#operationalStatis").load("/wechat/statistics/pageGetListOperationalStatistics_one_jcdw?jcdw="+$("#operationalStatis_sjhb #jcdw").val()+"&access_token="+$("#ac_tk").val()+"&tjzq="+$("#operationalStatis_sjhb #tjzq").val()+"&jcdwmc="+$("#operationalStatis_sjhb #jcdwmc").val());
})

$(function(){
	init();
	loadOperationalStatis_sf();
});