
var loadOperationalStatis=function(){
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
				var legendData=new Array();
				var seriesData=new Array();
				for (var i = 0; i < data.length; i++) {
					legendData.push(data[i].jcdwmc);
					seriesData.push({value:data[i].num,name:data[i].jcdwmc+"("+data[i].num+")",jcdw:data[i].jcdw});
				}
				var pieoption={
				    title: {
				        text: '检测单位',
				        subtext : searchData.zqs.jcdw,
				        left: 'center',
				    },
				    tooltip: {
				        trigger: 'item',
				        formatter: '{a} <br/>{b} : {c} ({d}%)'
				    },
				    legend: {
				        orient: 'vertical',
				        left: 'left',
				        data: legendData
				    },
				    series: [
				        {
				            name: '检测单位',
				            type: 'pie',
				            radius: '55%',
				            center: ['50%', '60%'],
				            data:seriesData,
				            emphasis: {
				                itemStyle: {
				                    shadowBlur: 10,
				                    shadowOffsetX: 0,
				                    shadowColor: 'rgba(0, 0, 0, 0.5)'
				                }
				            }
				        }
				    ]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(param){
					$("#operationalStatis").load("/wechat/statistics/pageGetListOperationalStatistics_one_jcdw?jcdw="+param.data.jcdw+"&access_token="+$("#ac_tk").val()+"&tjzq="+$("#operationalStatis #tjzq").val()+"&jcdwmc="+param.data.name);
				});
			}
		},
		{
			id:"sys",
			chart: null,
			el: null,
			render:function(data,searchData){
				var legendData=new Array();
				var seriesData=new Array();
				for (var i = 0; i < data.length; i++) {
					legendData.push(data[i].jcdwmc);
					seriesData.push({value:data[i].num,name:data[i].jcdwmc+"("+data[i].num+")",jcdw:data[i].jcdw});
				}
				var pieoption={
				    title: {
				        text: '未上机',
				        subtext : searchData.zqs.sys,
				        left: 'center',
				    },
				    tooltip: {
				        trigger: 'item',
				        formatter: '{a} <br/>{b} : {c} ({d}%)'
				    },
				    legend: {
				        orient: 'vertical',
				        left: 'left',
				        data: legendData
				    },
				    series: [
				        {
				            name: '检测单位',
				            type: 'pie',
				            radius: '55%',
				            center: ['50%', '60%'],
				            data:seriesData,
				            emphasis: {
				                itemStyle: {
				                    shadowBlur: 10,
				                    shadowOffsetX: 0,
				                    shadowColor: 'rgba(0, 0, 0, 0.5)'
				                }
				            }
				        }
				    ]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(param){
					$("#operationalStatis").load("/wechat/statistics/pageGetListOperationalStatistics_two_jcdw?jcdw="+param.data.jcdw+"&access_token="+$("#ac_tk").val()+"&tjzq="+$("#operationalStatis #tjzq").val()+"&jcdwmc="+param.data.name);
				});
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
		var _url = "/wechat/statistics/pagedataOperational";
		$.ajax({
			type : "post",
			url : _url,
			data:{
				"access_token":$("#ac_tk").val(),
				"tjzq":$("#operationalStatis #tjzq").val()
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
				}
			}
		});
	})
	echartsBtnInit(_renderArr);
}

function echartsBtnInit(_renderArr){
	var url="/wechat/statistics/pagedataOperational_firstPage_click";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["tjzq"] =$("#operationalStatis #tjzq").val();
	//周期改变事件
	$("#operationalStatis #jcdw_D").unbind("click").click(function(e){
		map["method"]="jcdw_day";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_D").css("color","#000000");
		$("#operationalStatis #tjzq").val("day");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	$("#operationalStatis #jcdw_W").unbind("click").click(function(e){
		map["method"]="jcdw_week";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_W").css("color","#000000");
		$("#operationalStatis #tjzq").val("week");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	$("#operationalStatis #jcdw_M").unbind("click").click(function(e){
		map["method"]="jcdw_mon";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_M").css("color","#000000");
		$("#operationalStatis #tjzq").val("mon");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	$("#operationalStatis #jcdw_Y").unbind("click").click(function(e){
		map["method"]="jcdw_year";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_Y").css("color","#000000");
		$("#operationalStatis #tjzq").val("year");
		clickEcharts(_renderArr,url,map,'jcdw');
	})
	
	$("#operationalStatis #sys_D").unbind("click").click(function(e){
		map["method"]="sys_day";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #sys_D").css("color","#000000");
		$("#operationalStatis #tjzq").val("day");
		clickEcharts(_renderArr,url,map,'sys');
	})
	$("#operationalStatis #sys_W").unbind("click").click(function(e){
		map["method"]="sys_week";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #sys_W").css("color","#000000");
		$("#operationalStatis #tjzq").val("week");
		clickEcharts(_renderArr,url,map,'sys');
	})
	$("#operationalStatis #sys_M").unbind("click").click(function(e){
		map["method"]="sys_mon";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #sys_M").css("color","#000000");
		$("#operationalStatis #tjzq").val("mon");
		clickEcharts(_renderArr,url,map,'sys');
	})
	$("#operationalStatis #sys_Y").unbind("click").click(function(e){
		map["method"]="sys_year";
		$("#operationalStatis a").css("color","#FFFFFF");
		$("#operationalStatis #sys_Y").css("color","#000000");
		$("#operationalStatis #tjzq").val("year");
		clickEcharts(_renderArr,url,map,'sys');
	})
}

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
	var tjzq=$("#operationalStatis #tjzq").val();
	if(tjzq=="year"){
		$("#operationalStatis .btn-float").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_Y").css("color","#000000");
	}else if(tjzq=="mon"){
		$("#operationalStatis .btn-float").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_M").css("color","#000000");
	}else if(tjzq=="week"){
		$("#operationalStatis .btn-float").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_W").css("color","#000000");
	}else if(tjzq=="day"){
		$("#operationalStatis .btn-float").css("color","#FFFFFF");
		$("#operationalStatis #jcdw_D").css("color","#000000");
	}
}


$(function(){
	init();
	loadOperationalStatis();
	
});