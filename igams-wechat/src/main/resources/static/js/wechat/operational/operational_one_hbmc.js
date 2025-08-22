var loadOperationalStatis_hbmc=function(){
	var _eventTag = "operationalStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_operational_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr=[
		{
			id:"hbmc",
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
				        subtext : searchData.zqs.hbmc	,
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
				this.chart.on("click",function(param){});
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
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = "/wechat/statistics/pagedataOperational_Statis_hbmc_echarts";
		$.ajax({
			type : "post",
			url : _url,
			data:{
				"access_token":$("#ac_tk").val(),
				"tjzq":$("#operationalStatis #tjzq").val(),
				"jcdw":$("#operationalStatis #jcdw").val(),
				"sf":$("#operationalStatis #sf").val(),
				"db":$("#operationalStatis #db").val(),
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
					if(datas.hbmc_tjzq=="year"){
						$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
						$("#operationalStatis_hbmc #hbmc_Y").css("color","#000000");
					}else if(datas.hbmc_tjzq=="mon"){
						$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
						$("#operationalStatis_hbmc #hbmc_M").css("color","#000000");
					}else if(datas.hbmc_tjzq=="week"){
						$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
						$("#operationalStatis_hbmc #hbmc_W").css("color","#000000");
					}else if(datas.hbmc_tjzq=="day"){
						$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
						$("#operationalStatis_hbmc #hbmc_D").css("color","#000000");
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
	map["jcdw"] =$("#operationalStatis_hbmc #jcdw").val();
	map["sf"]=$("#operationalStatis_hbmc #sf").val();
	map["db"]=$("#operationalStatis_hbmc #db").val();
	
	$("#operationalStatis_hbmc #hbmc_Y").unbind("click").click(function(){
		map["method"]="hbmc_year";
		$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
		$("#operationalStatis_hbmc #hbmc_Y").css("color","#000000");
		clickEcharts(_renderArr,url,map,'hbmc');
	})
	
	$("#operationalStatis_hbmc #hbmc_M").unbind("click").click(function(){
		map["method"]="hbmc_mon";
		$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
		$("#operationalStatis_hbmc #hbmc_M").css("color","#000000");
		clickEcharts(_renderArr,url,map,'hbmc');
	})
	
	$("#operationalStatis_hbmc #hbmc_W").unbind("click").click(function(){
		map["method"]="hbmc_week";
		$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
		$("#operationalStatis_hbmc #hbmc_W").css("color","#000000");
		clickEcharts(_renderArr,url,map,'hbmc');
	})
	
	$("#operationalStatis_hbmc #hbmc_D").unbind("click").click(function(){
		map["method"]="hbmc_day";
		$("#operationalStatis_hbmc .hbmc_a").css("color","#FFFFFF");
		$("#operationalStatis_hbmc #hbmc_D").css("color","#000000");
		clickEcharts(_renderArr,url,map,'hbmc');
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
$("#operationalStatis_hbmc #backBtn").bind("click",function(){
	$("#operationalStatis").load("/wechat/statistics/pageListOperational_Statis_sjhb?sf="+$("#operationalStatis_hbmc #sf").val()+"&jcdw="+$("#operationalStatis_hbmc #jcdw").val()+"&tjzq="+$("#operationalStatis_hbmc #tjzq").val()+"&sfmc="+$("#operationalStatis_hbmc #sfmc").val()+"&jcdwmc="+$("#operationalStatis_hbmc #jcdwmc").val());
})

$(function(){
	init();
	loadOperationalStatis_hbmc();
});