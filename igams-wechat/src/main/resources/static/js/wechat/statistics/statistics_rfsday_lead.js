var loadDayLeadStatis=function(){
	var _eventTag = "dayLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_rfsday_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"rfszqbbs",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				var dataxAxis= new Array();
				var datasyAxis=new Array();
				var maxY=0;
				var intval;
				var tmp_y=0;
				if(data &&this.chart){
					dataxAxis.push('接收');
					datasyAxis.push(data.jswqy)
					dataxAxis.push('取样');
					datasyAxis.push(data.qywtq)
					dataxAxis.push('提取');
					datasyAxis.push(data.tqwkz)
					dataxAxis.push('扩增');
					datasyAxis.push(data.kzwjc)
					dataxAxis.push('检测');
					datasyAxis.push(data.jcwbg)
					dataxAxis.push('报告');
					datasyAxis.push(data.yfbg)
					for (var i = 0; i < datasyAxis.length; i++) {
						if (tmp_y<=datasyAxis[i]){
							tmp_y = datasyAxis[i];
						}
					}
					maxY=parseInt(tmp_y);
					maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					intval=maxY/4
					var pieoption = {
						title : {
							subtext : '',
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
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
						}],
						xAxis: {
							type: 'category',
							data: dataxAxis
						},
						series: [
							{
								name: '',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: datasyAxis
							}]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(){
					});
				}
			}

		},
		{
			id:"rfsbbys",
			chart: null,
			el: null,
			render:function(data,searchData){
				var alldata= new Array();
				var maxY=0;
				var intval;
				var tmp_y=0;
				if(data &&this.chart){
					for (var i = 0; i < data.length; i++) {
						alldata.push({value:[data[i].name,data[i].sj], symbolOffset:[data[i].wz+'%',0]})
						// if (tmp_y<=data[i].sj){
						// 	tmp_y = data[i].sj;
						// }
					}
					// maxY=parseInt(tmp_y);
					// maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					// intval=maxY/4
					var pieoption = {
						title : {
							subtext : '',
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
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
						yAxis: {name: '分钟'},
						// yAxis:  [{
						// 	name: '分钟',
						// 	type: 'value',
						// 	min: 0,
						// 	max: maxY,
						// 	interval: intval
						// }],
						xAxis: {
							type: 'category',
							data:['接收','取样','提取','扩增','检测']
						},
						series: [
							{
								symbolSize: 5,
								data:alldata,
								type: 'scatter',
							}
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(){
					});
				}
			}

		},{
			id:"rrfspjys",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				var dataxAxis= new Array();
				var datasyAxis=new Array();
				var maxY=0;
				var intval;
				var tmp_y=0;
				if(data &&this.chart){
					dataxAxis.push('接收');
					datasyAxis.push(data[0].jsdqy)
					dataxAxis.push('取样');
					datasyAxis.push(data[0].qydtq)
					dataxAxis.push('提取');
					datasyAxis.push(data[0].tqdkz)
					dataxAxis.push('扩增');
					datasyAxis.push(data[0].kzdjc)
					dataxAxis.push('检测');
					for (var i = 0; i < datasyAxis.length; i++) {
						if (tmp_y<=datasyAxis[i]){
							tmp_y = datasyAxis[i];
						}
					}
					maxY=parseInt(tmp_y);
					maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					intval=maxY/4
					var pieoption = {
						title : {
							subtext : '',
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
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
							name: '分钟',
							type: 'value',
							min: 0,
							max: maxY,
							interval: intval
						}],
						xAxis: {
							type: 'category',
							data: dataxAxis
						},
						series: [
							{
								name: '',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: datasyAxis
							}]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(){
					});
				}
			}

		}
	];
	//路径配置
	setTimeout(function(){
		for (var i = 0; i < _renderArr.length; i++) {
			var _render=_renderArr[i];
			var _el=_render.el=document.getElementById(_idPrefix+_render.id);
			if(_el){
				_render.chart=echarts.init(_el,_statis_theme);
				_render.eventTag ="."+_eventTag+"-"+_render.id;
				if(_isShowLoading){
					_render.chart.showLoading({
						effect:_loadEffect
					});
				}
				$(window).off(_render.eventTag).on(
						"resize"+_render.eventTag,(
							function(_cfg){
								return function(){
									if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){
										return;
										}
									_cfg.chart.resize();
									}
								}	
							)(_render)
						);
			}
		}
		var _url = "/ws/statistics/getRfsYbxx";
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			data:{"sign":$("#sign").val(),"jsrq":$("#jsrq").val()},
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
						if(_data==null || _data.length==0){
							$("#rfsdaliyStatis #"+_render.id).parent().hide();
						}
					}
				}else{
				}
			}
		});
	});
}



$(function(){
    loadDayLeadStatis();
})
