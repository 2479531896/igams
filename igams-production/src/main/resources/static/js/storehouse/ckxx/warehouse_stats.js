
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
			id:"zt",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var wlzl=new Array();
				var wlsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					wlzl.push(data[i].wlzl);
					wlsl.push(data[i].wlsl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['物料种类', '物料数量']
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
							name: '物料种类',
							type: 'bar',
							barWidth: '20%',
							data: wlzl
						},{
							name: '物料数量',
							type: 'bar',
							barWidth: '20%',
							data: wlsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatus?lx="+param.name+"&ckid=zt","按物料类别统计"+param.seriesName,categoryStatusConfig);
				});
			}

		},
		{
			id:"bmcrk",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var cksl=new Array();
				var dhsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].jgmc);
					dhsl.push(data[i].dhsl);
					cksl.push(data[i].cksl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['出库数量', '入库数量']
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
							name: '出库数量',
							type: 'bar',
							barWidth: '20%',
							data: cksl
						},{
							name: '入库数量',
							type: 'bar',
							barWidth: '20%',
							data: dhsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatus?jgmc="+param.name+"&lx="+param.seriesName+"&ckid=bm","按物料类别统计"+param.seriesName,categoryStatusConfig);
				});
			}

		},
		{
			id:"lc",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var wlzl=new Array();
				var wlsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					wlzl.push(data[i].wlzl);
					wlsl.push(data[i].wlsl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['物料种类', '物料数量']
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
							name: '物料种类',
							type: 'bar',
							barWidth: '20%',
							data: wlzl
						},{
							name: '物料数量',
							type: 'bar',
							barWidth: '20%',
							data: wlsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatus?lx="+param.name+"&ckid=lc","按物料类别统计"+param.seriesName,categoryStatusConfig);
				});
			}

		},
		{
			id:"khfh",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var wlzl=new Array();
				var wlsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					wlzl.push(data[i].wlzl);
					wlsl.push(data[i].wlsl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['物料种类', '物料数量']
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
							name: '物料种类',
							type: 'bar',
							barWidth: '20%',
							data: wlzl
						},{
							name: '物料数量',
							type: 'bar',
							barWidth: '20%',
							data: wlsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatus?khmc="+param.name+"&lx=khfh&ckid=khfh","按物料类别统计"+param.seriesName,categoryStatusConfig);
				});
			}
		},	{
			id:"khjysl",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var wlzl=new Array();
				var wlsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					wlzl.push(data[i].wlzl);
					wlsl.push(data[i].wlsl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['物料种类', '物料数量']
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
							name: '物料种类',
							type: 'bar',
							barWidth: '20%',
							data: wlzl
						},{
							name: '物料数量',
							type: 'bar',
							barWidth: '20%',
							data: wlsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatus?khmc="+param.name+"&lx=khjysl&ckid=khjysl","按物料类别统计"+param.seriesName,categoryStatusConfig);
				});
			}
		},	{
			id:"ckkcl",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var wlzl=new Array();
				var wlsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					wlzl.push(data[i].wlzl);
					wlsl.push(data[i].wlsl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['物料种类', '物料数量']
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
							name: '物料种类',
							type: 'bar',
							barWidth: '20%',
							data: wlzl
						},{
							name: '物料数量',
							type: 'bar',
							barWidth: '20%',
							data: wlsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatus?ckmc="+param.name+"&lx=ckkcl&ckid=ckkcl","按物料类别统计"+param.seriesName,categoryStatusConfig);
				});
			}
		},	{
			id:"ckhwkcl",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var wlzl=new Array();
				var wlsl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					wlzl.push(data[i].wlzl);
					wlsl.push(data[i].wlsl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['物料种类', '物料数量']
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
							name: '物料种类',
							type: 'bar',
							barWidth: '20%',
							data: wlzl
						},{
							name: '物料数量',
							type: 'bar',
							barWidth: '20%',
							data: wlsl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/itemInfo?access_token="+$("#ac_tk").val()+"&lbmc="+param.name+"&lx=ckhwkcl","物料详情"+param.seriesName,infoConfig);
				});
			}
		},	{
			id:"sjscjd",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var sl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					sl.push(data[i].sl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['天数']
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
							name: '天数',
							type: 'bar',
							barWidth: '20%',
							data: sl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
			}
		},	{
			id:"yqscjd",
			chart: null,
			el: null,
			render:function(data){
				var xAxis= new Array();
				var sl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lc);
					sl.push(data[i].sl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					legend: {
						data: ['天数']
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
							name: '天数',
							type: 'bar',
							barWidth: '20%',
							data: sl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
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
		var _url = $("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/pagedataDepotStatsInfo";
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
	depotEchartsBtnInit(_renderArr);
}

infoConfig = {
	width		: "1600px",
	modalName	:"itemInfoModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//统计图中各按钮的点击事件初始化
function depotEchartsBtnInit(_renderArr){
	var url = $("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/pagedataDepotStatsInfo";
	$("#depotStatus #bm #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'bmcrk');
	})
	$("#depotStatus #lc #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'lc');
	})

	$("#depotStatus #zt #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'zt');
	})

	$("#depotStatus #khfh #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'khfh');
	})

	$("#depotStatus #khjysl #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'khjysl');
	})

	$("#depotStatus #sjscjd #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'sjscjd');
	})

	$("#depotStatus #yqscjd #btn_query").click(function(){
		depotClickLeadEcharts(_renderArr,url,'yqscjd');
	})


	// $("#depotStatus #sjscjd #sjYear").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("sjYear");
	// 	depotClickLeadEcharts(_renderArr,url,'sjscjd');
	// })
	// $("#depotStatus #sjscjd #sjMonth").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("sjMonth");
	// 	depotClickLeadEcharts(_renderArr,url,'sjscjd');
	// })
	// $("#depotStatus #sjscjd #sjWeek").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("sjWeek");
	// 	depotClickLeadEcharts(_renderArr,url,'sjscjd');
	// })
	// $("#depotStatus #sjscjd #sjDay").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("sjDay");
	// 	depotClickLeadEcharts(_renderArr,url,'sjscjd');
	// })
	//
	// $("#depotStatus #yqscjd #yqYear").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("yqYear");
	// 	depotClickLeadEcharts(_renderArr,url,'yqscjd','yqYear');
	// })
	// $("#depotStatus #yqscjd #yqMonth").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("yqMonth");
	// 	depotClickLeadEcharts(_renderArr,url,'yqscjd','yqMonth');
	// })
	// $("#depotStatus #yqscjd #yqWeek").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("yqWeek");
	// 	depotClickLeadEcharts(_renderArr,url,'yqscjd','yqWeek');
	// })
	// $("#depotStatus #yqscjd #yqDay").unbind("click").click(function(){
	// 	depotInitEchartsLeadBtnCss("yqDay");
	// 	depotClickLeadEcharts(_renderArr,url,'yqscjd','yqDay');
	// })
}

// //设置按钮样式
// function depotInitEchartsLeadBtnCss(getid,_renderArr){
// 	var obj = $("#depotStatus #"+getid)
// 	var gettitle=obj.attr("title");
// 	var a=$("#depotStatus #"+getid).siblings();
// 	var ah=a.length;
// 	for(var i=0;i<ah;i++){
// 		var otherid=a[i].id;
// 		$("#depotStatus #"+otherid).removeClass("bechoosed");
// 		$("#depotStatus #"+otherid+" .fa").attr("style","");
// 	}
// 	//点击统计图按钮，按照全部查询，并显示相应统计图
// 	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
// 	if(gettitle=="年"){
// 		$("#depotStatus #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
// 		$("#depotStatus #"+getid).addClass("bechoosed");
// 	}else if(gettitle=="月"){
// 		$("#depotStatus #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
// 		$("#depotStatus #"+getid).addClass("bechoosed");
// 	}else if(gettitle=="周"){
// 		$("#depotStatus #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
// 		$("#depotStatus #"+getid).addClass("bechoosed");
// 	}else if(gettitle=="日"){
// 		$("#depotStatus #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
// 		$("#depotStatus #"+getid).addClass("bechoosed");
// 	}
// }

//点击相应echarts获取相应数据
function depotClickLeadEcharts(_renderArr,_url,id){
	for(var i=0; i<_renderArr.length; i++){
		if(id ==  _renderArr[i].id){
			let startTime ="";
			let endTime ="";
			if (id=="lc"){
				startTime = $("#depotStatus #lc #lcstartTime").val();
				endTime =  $("#depotStatus #lc #lcendTime").val();
			}else if (id=="bmcrk"){
				startTime = $("#depotStatus #bm #startTime").val();
				endTime =  $("#depotStatus #bm #endTime").val();
			}else if (id=="zt"){
				startTime = $("#depotStatus #zt #ztstartTime").val();
				endTime =  $("#depotStatus #zt #ztendTime").val();
			}else if (id=="khfh"){
				startTime = $("#depotStatus #khfh #khfhstartTime").val();
				endTime =  $("#depotStatus #khfh #khfhendTime").val();
			}else if (id=="khjysl"){
				startTime = $("#depotStatus #khjysl #khjyslstartTime").val();
				endTime =  $("#depotStatus #khjysl #khjyslendTime").val();
			}else if (id=="sjscjd"){
				startTime = $("#depotStatus #sjscjd #sjscjdstartTime").val();
				endTime =  $("#depotStatus #sjscjd #sjscjdendTime").val();
			}else if (id=="yqscjd"){
				startTime = $("#depotStatus #yqscjd #yqscjdstartTime").val();
				endTime =  $("#depotStatus #yqscjd #yqscjdendTime").val();
			}
				//加载数据
			$.ajax({
				type : "post",
				url : _url,
				data: { "access_token": $("#ac_tk").val(),"lx":id,"startTime":startTime,"endTime":endTime},
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
var categoryStatusConfig = {
	width		: "500px",
	modalName	:"categoryStatusModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

$(function(){
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#depotStatus .chosen-select').chosen({width: '100%'});

	laydate.render({
		elem: '#ztstartTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#ztendTime'
		,theme: '#2381E9',
	});

	laydate.render({
		elem: '#startTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#endTime'
		,theme: '#2381E9',
	});
	laydate.render({
		elem: '#lcstartTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#lcendTime'
		,theme: '#2381E9',
	});
	laydate.render({
		elem: '#khfhendTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#khfhstartTime'
		,theme: '#2381E9',
	});
	laydate.render({
		elem: '#khjyslstartTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#khjyslendTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#yqscjdstartTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#yqscjdendTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#sjscjdstartTime'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#sjscjdendTime'
		,theme: '#2381E9',
	});
});