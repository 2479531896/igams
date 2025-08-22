
var flModels = [];

//加载统计数据
var loadTaskLeadStatis = function(){
	var _eventTag = "weekTaskStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_task_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"task",
			chart:null,
			el:null,
			render:function(data){
				var seriesData= new Array();
				var dataAxis= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						dataAxis.push(data[i].rwmc);
						seriesData.push({value:data[i].num,name:data[i].rwmc});
					}
				}
				var pieoption = {
					tooltip : {
						trigger: 'item',
						formatter: "{b} : {c} ({d}%)"
					},
					//color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					legend: {
						orient: 'vertical',
						left: 'left',
						top:'20',
						data: dataAxis
					},
					series : [
						{
							name: '',
							type: 'pie',
							radius : '60%',
							center: ['50%', '60%'],
							startAngle:45,
							data:seriesData,
							label: {
								formatter: "{b} : {c} ({d}%)"
							},
						}
					]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(param){
				});
			}
		},
		{
			id:"depart",
			chart: null,
			el: null,
			render:function(data){

				var xAxis= new Array();
				var series=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].jgmc);
					series.push(data[i].num);
				}
				var pieoption = {
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: [
						{
							type: 'category',
							data: xAxis,
							axisTick: {
								alignWithLabel: true
							},
							axisLabel: {
								rotate: 35,
								margin: 15,
								fontSize: 10,
							},
						}
					],

					yAxis: [
						{
							type: 'value'
						}
					],
					series: [
						{
							name: '数量',
							type: 'bar',
							barWidth: '60%',
							label: {
								show: true
							},
							data: series
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

	];
	for (var i = 0; i < flModels.length; i++) {
		_renderArr.push(flModels[i]);
	}
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
		var _url = $("#taskStatistics #urlPrefix").val() +"/systemmain/task/pagedataGetListTaskStatsInfo";
		$.ajax({
			type : "post",
			url : _url,
			data: { "access_token": $("#ac_tk").val()},
			dataType : "json",
			success : function(datas) {
				if(datas){
					for(var i=0; i<_renderArr.length; i++){
						var _depotrender = _renderArr[i];
						var _data = datas[_depotrender.id];
						_depotrender.render(_data);
					}
				}else{
				}
			}
		});
	});
}


$(function(){
	loadTaskLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#taskStatistics .chosen-select').chosen({width: '100%'});
});