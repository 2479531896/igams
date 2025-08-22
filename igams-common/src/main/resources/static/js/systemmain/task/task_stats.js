
var flModels = [];

//加载统计数据
var loadWeekLeadStatis = function(){
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"rwxx",
			chart: null,
			el: null,
			render:function(data,searchData){

				var xAxis= new Array();
				var series=new Array();
				for (var i = 0; i < searchData.length; i++) {
					xAxis.push(searchData[i].zsxm);
					series.push(searchData[i].wwcsl);
				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
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
							data: xAxis,
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
							name: '未完成数量',
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
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#rwglStatis #urlprefix").val() +"/systemmain/task/pagedataStatsInfoList?zsxm="+param.name+"&zt="+$("#rwglStatis #zt").val(),taskStatsConfig);
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
		var _url = $("#rwglStatis #urlprefix").val() +"/systemmain/task/pagedataStatsInfo";
		clickStateEcharts(_renderArr,_url)
	});
	echartsBtnInit(_renderArr);
}

//点击相应echarts获取相应数据
function clickStateEcharts(_renderArr,_url){
	$.ajax({
		type : "post",
		url : _url,
		data: { "access_token": $("#ac_tk").val(),"zt": $("#rwglStatis #zt").val() },
		dataType : "json",
		success : function(datas) {
			if(datas){
				var _searchData = datas['gzglDtoList'];
				var wwczs = datas['wwczs'];
				var bm = datas['bm'];
				$("#rwglStatis #bm").text(bm);
				$("#rwglStatis #wwczs").text(wwczs);
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
}

function echartsBtnInit(_renderArr){
	var _url = $("#rwglStatis #urlprefix").val() +"/systemmain/task/pagedataStatsInfo";
	$("#rwglStatis #zt").unbind("change").change(function(e){
		clickStateEcharts(_renderArr,_url);
	})
}

var taskStatsConfig = {
	width		: "800px",
	modalName	: "taskStatsModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};



function init(){
}
$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#rwglStatis .chosen-select').chosen({width: '100%'});
});