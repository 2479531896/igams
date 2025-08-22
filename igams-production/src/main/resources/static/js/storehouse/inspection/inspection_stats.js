
var flModels = [];

//加载统计数据
var loadInspectionStatis = function(){
	var _eventTag = "inspectionStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_inspect_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"zt",
			chart:null,
			el:null,
			render:function(data){
				var dataxAxis= new Array();
				var dataysh=new Array();
				var datashz=new Array();
				var datawsh=new Array();
				var datawtg=new Array();
				if(data!=null){
					for (var i = 0; i < 12; i++) {
						dataxAxis.push((i+1).toString()+'月');
					}
					for (var i = 0; i < 12; i++) {
						var flag=false;
						var num;
						for (var j = 0; j < data.length; j++) {
							if((i+1)==parseInt(data[j].jyrq)){
								flag=true;
								num=j;
							}
						}
						if(flag){
							if(data[num].yshs){
								dataysh.push(data[num].yshs);
							}else{
								dataysh.push("0");
							}
							if(data[num].shzs){
								datashz.push(data[num].shzs);
							}else{
								datashz.push("0");
							}
							if(data[num].wshs){
								datawsh.push(data[num].wshs);
							}else{
								datawsh.push("0");
							}
							if(data[num].wtgs){
								datawtg.push(data[num].wtgs);
							}else{
								datawtg.push("0");
							}
						}else{
							dataysh.push("0");
							datashz.push("0");
							datawsh.push("0");
							datawtg.push("0");
						}
					}
				}
				var pieoption  = {
					tooltip : {
						trigger: 'axis',
						axisPointer : {            // 坐标轴指示器，坐标轴触发有效
							type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					legend: {
						data: ['已审核','审核中','审核未通过','未提交'],
						textStyle: {
							//color: '#3E9EE1'
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
						name: '数量',
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
							name: '已审核',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							data: dataysh,
							//color:'#FECEFF'
						},
						{
							name: '审核中',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							// color:'#2Fc6CB',
							data: datashz
						},
						{
							name: '审核未通过',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							// color:'#2Fc6CB',
							data: datawtg
						},
						{
							name: '未提交',
							type: 'bar',
							stack: '总量',
							barGap: 0,
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							data: datawsh,
							// color:'#37D8FF'
						},
					]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(){
				});
			}
		},
		{
			id:"qy",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var dataxAxis= new Array();
					var dataseries=new Array();
					var maxY=0;
					var interval;
					var startrq;
					var endrq;
					for(var i = 0; i < 12; i++){
						dataxAxis.push((i+1).toString()+"月");
					}
					for (var i = 0; i < 12; i++) {
						var flag=false;
						var num;
						for (var j = 0; j < data.length; j++) {
							if((i+1)==parseInt(data[j].jyrq)){
								flag=true;
								num=j;
							}
						}
						if(flag){
							dataseries.push(data[num].qysl);
							if(maxY < parseInt(data[num].qysl)){
								maxY = parseInt(data[num].qysl);
							}
						}else{
							dataseries.push("0");
						}
					}
					maxY= maxY + 5 - maxY%5
					interval=maxY/5

					var pieoption = {
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						xAxis : [
							{
								type : 'category',
								data:dataxAxis,
								axisTick: {
									alignWithLabel: true
								},
								axisLine: {
									lineStyle: {
										color: '#d14a61'
									},
								},
							}
						],
						yAxis : [
							{
								type: 'value',
								name: '数量',
								min: 0,
								max: maxY,
								interval: interval,
								axisLabel: {
									formatter: '{value}'
								}
							}
						],
						series : [
							{
								name:'数量',
								type:'line',
								itemStyle: {
									normal: {
										label: {
											show: true, //开启显示
											position: 'top', //在上方显示
											textStyle: { //数值样式
												fontSize: 12
											}
										}
									}
								},
								data:dataseries
							}
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
					});
				}
			}
		},{
			id:"jyjg",
			chart:null,
			el:null,
			render:function(data){
				var dataxAxis= new Array();
				var datatgsl=new Array();
				var databftgsl=new Array();
				var databtgsl=new Array();
				if(data!=null){
					for (var i = 0; i < 12; i++) {
						dataxAxis.push((i+1).toString()+'月');
					}
					for (var i = 0; i < 12; i++) {
						var flag=false;
						var num;
						for (var j = 0; j < data.length; j++) {
							if((i+1)==parseInt(data[j].jyrq)){
								flag=true;
								num=j;
							}
						}
						if(flag){
							if(data[num].tgsl){
								datatgsl.push(data[num].tgsl);
							}else{
								datatgsl.push("0");
							}
							if(data[num].bftgsl){
								databftgsl.push(data[num].bftgsl);
							}else{
								databftgsl.push("0");
							}
							if(data[num].btgsl){
								databtgsl.push(data[num].btgsl);
							}else{
								databtgsl.push("0");
							}
						}else{
							datatgsl.push("0");
							databftgsl.push("0");
							databtgsl.push("0");
						}
					}
				}
				var pieoption  = {
					tooltip : {
						trigger: 'axis',
						axisPointer : {            // 坐标轴指示器，坐标轴触发有效
							type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					legend: {
						data: ['通过','部分通过','不通过'],
						textStyle: {
							//color: '#3E9EE1'
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
						name: '数量',
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
							name: '通过',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							data: datatgsl,
							//color:'#FECEFF'
						},
						{
							name: '部分通过',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							// color:'#2Fc6CB',
							data: databftgsl
						},
						{
							name: '不通过',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							// color:'#2Fc6CB',
							data: databtgsl
						},
					]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(){
				});
			}
		},{
			id:"wllbzb",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].count,name:data[i].lbdm});
					}
				}
				var pieoption = {
					title : {
						x : 'center'
					},
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b}: \n{c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						left: 'left',
						top:'20'
					},
					series : [
						{
							name: '',
							type: 'pie',
							radius: ['15%', '45%'],
							center: ['48%', '60%'],
							data:seriesData,
							label: {
								normal: {
									formatter : "{b}\n{d}%"
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
				this.chart.on("click",function(param){
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
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = $("#inspectStats #urlPrefix").val() +"/inspectionGoods/inspectionGoods/pagedataGetAuditStatistics";
		var rqstart = $("#inspectStats #wllbzb #rqstart").val();
		var rqend = $("#inspectStats #wllbzb #rqend").val();
		$.ajax({
			type : "post",
			url : _url,
			data: { "access_token": $("#ac_tk").val(),"year":$("#inspectStats #zt #year option:selected").val(),"rqstart":rqstart,"rqend":rqend },
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
	inspectEchartsBtnInit(_renderArr);
};


//统计图中各按钮的点击事件初始化
function inspectEchartsBtnInit(_renderArr){
	var url = $("#inspectStats #urlPrefix").val() +"/inspectionGoods/inspectionGoods/pagedataGetAuditStatistics";
	$("#inspectStats #zt #year").change(function(){
		inspectClickLeadEcharts(_renderArr,url,'zt');
	})
	$("#inspectStats #qy #year_qy").change(function(){
		inspectClickLeadEcharts(_renderArr,url,'qy');
	})
	$("#inspectStats #jyjg #year_jyjg").change(function(){
		inspectClickLeadEcharts(_renderArr,url,'jyjg');
	})
	//检验物料类别
	$("#inspectStats #wllbzb #btn_query").click(function(){
		inspectClickLeadEcharts(_renderArr,url,'wllbzb');
	})
}

//点击相应echarts获取相应数据
function inspectClickLeadEcharts(_renderArr,_url,id){
	for(var i=0; i<_renderArr.length; i++){
		if(id ==  _renderArr[i].id){
			let year ="";
			let rqstart ="";
			let rqend ="";
			if (id=="zt"){
				year = $("#inspectStats #zt #year option:selected").val()
			}else if(id=='qy'){
				year = $("#inspectStats #qy #year_qy option:selected").val()
			}else if(id=='jyjg'){
				year = $("#inspectStats #jyjg #year_jyjg option:selected").val()
			}else if(id=='wllbzb'){
				rqstart = $("#inspectStats #wllbzb #rqstart").val();
				rqend = $("#inspectStats #wllbzb #rqend").val();
			}
				//加载数据
			$.ajax({
				type : "post",
				url : _url,
				data: { "access_token": $("#ac_tk").val(),"year":year,"id":id,"rqstart":rqstart,"rqend":rqend},
				dataType : "json",
				success : function(datas) {
					if(datas){
						var _depotrender = _renderArr[i];
						var _data = datas[_depotrender.id];
						if (_data && _data!="undefined"){
							_depotrender.render(_data);
						}
					}else{
					}
				}
			});
			break;
		}
	}
}

$(function(){
	loadInspectionStatis();
	laydate.render({
		elem: '#rqstart'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#rqend'
		,theme: '#2381E9',
	});
	//所有下拉框添加choose样式
	jQuery('#inspectStats .chosen-select').chosen({width: '40%'});
});