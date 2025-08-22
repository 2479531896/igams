
var flModels = [];

//加载统计数据
var loadDocumentStatis = function(){
	var _eventTag = "documentStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_document_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"xzwj",
			chart: null,
			el: null,
			render:function(data,searchData){

				var xAxis= new Array();
				var series=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lrsjStats);
					series.push(data[i].num);
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
							name: '新增数量',
							type: 'bar',
							label: {
								show: true
							},
							barWidth: '60%',
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

		},
		{
			id:"xdwj",
			chart: null,
			el: null,
			render:function(data,searchData){

				var xAxis= new Array();
				var series=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].xgsjStats);
					series.push(data[i].num);
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
							name: '修订数量',
							type: 'bar',
							label: {
								show: true
							},
							barWidth: '60%',
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
		},
		{
			id:"wjfl",
			chart:null,
			el:null,
			render:function(data){
				var seriesData= new Array();
				var dataAxis= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						dataAxis.push(data[i].wjflmc);
						seriesData.push({value:data[i].wjsl,name:data[i].wjflmc});
					}
				}
				var pieoption = {
					tooltip : {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
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
							radius : '45%',
							center: ['50%', '60%'],
							startAngle:45,
							data:seriesData,
							label: {
								normal: {
									formatter:"{b}:\n {c}(个)"
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
		},
		{
			id:"wjyf",
			chart:null,
			el:null,
			render:function(data){
				var dataxAxis= new Array();
				var data0=new Array();
				var data1=new Array();
				var data2=new Array();
				var data3=new Array();
				if(data!=null){
					for (var i = 0; i < 12; i++) {
						dataxAxis.push((i+1).toString()+'月');
					}
					for (var i = 0; i < 12; i++) {
						var flag=false;
						var num;
						for (var j = 0; j < data.length; j++) {
							if((i+1)==parseInt(data[j].lrsj)){
								flag=true;
								num=j;
							}
						}
						if(flag){
							data0.push("0");
							data1.push("0");
							data2.push("0");
							data3.push("0");
							var split = data[num].wjsl.split(",");
							for(var k=0;k<split.length;k++){
								var split_t = split[k].split("-");
								if(split_t[1]=='0'){
									data0[i]=split_t[0];
								}else if(split_t[1]=='1'){
									data1[i]=split_t[0];
								}else if(split_t[1]=='2'){
									data2[i]=split_t[0];
								}else if(split_t[1]=='3'){
									data3[i]=split_t[0];
								}
							}

						}else{
							data0.push("0");
							data1.push("0");
							data2.push("0");
							data3.push("0");
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
						data: ['正常','废弃','停用','修订'],
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
							name: '正常',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							data: data0,
							//color:'#FECEFF'
						},
						{
							name: '废弃',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							// color:'#2Fc6CB',
							data: data1,
						},
						{
							name: '停用',
							type: 'bar',
							stack: '总量',
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							// color:'#2Fc6CB',
							data: data2,
						},
						{
							name: '修订',
							type: 'bar',
							stack: '总量',
							barGap: 0,
							label: {
								normal: {
									show: false,
									position: 'insideBottom'
								}
							},
							data: data3,
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
		var _url = $("#wjglStatis #urlPrefix").val() +"/production/document/pageGetStatistics";
		$.ajax({
			type : "post",
			url : _url,
			data: { "access_token": $("#ac_tk").val(),lrsjDay : "lrsjDay",xgsjDay : "xgsjDay",
				endTimeLr : $("#wjglStatis #endTimeLr").val(),startTimeLr : $("#wjglStatis #startTimeLr").val(),
				startTimeXg : $("#wjglStatis #startTimeXg").val(),endTimeXg : $("#wjglStatis #endTimeXg").val(),"lrsj":$("#wjglStatis #year option:selected").val() },
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['gzglDtoList'];
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

//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
	var obj = $("#wjglStatis #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#wjglStatis #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#wjglStatis #"+otherid).removeClass("bechoosed");
		$("#wjglStatis #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#wjglStatis #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#wjglStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#wjglStatis #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#wjglStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#wjglStatis #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#wjglStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#wjglStatis #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#wjglStatis #"+getid).addClass("bechoosed");
	}
}
let Xzcs = "lrsjDay";
let Xdcs = "xgsjDay";

//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url=$("#wjglStatis #urlPrefix").val() + "/production/document/pageGetStatistics";
	//文件新增
	$("#wjglStatis #lrsjYear").unbind("mouseover").mouseover(function(){
		Xzcs = "lrsjYear";
		initEchartsLeadBtnCss("lrsjYear");
		clickLeadEcharts(_renderArr,url,"lrsjYear",'xzwj');
	})
	//文件新增
	$("#wjglStatis #lrsjMonth").unbind("mouseover").mouseover(function(){
		Xzcs = "lrsjMonth";
		initEchartsLeadBtnCss("lrsjMonth");
		clickLeadEcharts(_renderArr,url,"lrsjMonth",'xzwj');
	})
	//文件新增
	$("#wjglStatis #lrsjWeek").unbind("mouseover").mouseover(function(){
		Xzcs = "lrsjWeek";
		initEchartsLeadBtnCss("lrsjWeek");
		clickLeadEcharts(_renderArr,url,"lrsjWeek",'xzwj');
	})
	//文件新增
	$("#wjglStatis #lrsjDay").unbind("mouseover").mouseover(function(){
		Xzcs = "lrsjDay";
		initEchartsLeadBtnCss("lrsjDay");
		clickLeadEcharts(_renderArr,url,"lrsjDay",'xzwj');
	})
	//文件修订
	$("#wjglStatis #xgsjYear").unbind("mouseover").mouseover(function(){
		Xdcs = "xgsjYear";
		initEchartsLeadBtnCss("xgsjYear");
		clickLeadEcharts(_renderArr,url,"xgsjYear",'xdwj');
	})
	//文件修订
	$("#wjglStatis #xgsjMonth").unbind("mouseover").mouseover(function(){
		Xdcs = "xgsjMonth";
		initEchartsLeadBtnCss("xgsjMonth");
		clickLeadEcharts(_renderArr,url,"xgsjMonth",'xdwj');
	})
	//文件修订
	$("#wjglStatis #xgsjWeek").unbind("mouseover").mouseover(function(){
		Xdcs = "xgsjWeek";
		initEchartsLeadBtnCss("xgsjWeek");
		clickLeadEcharts(_renderArr,url,"xgsjWeek",'xdwj');
	})
	//文件修订
	$("#wjglStatis #xgsjDay").unbind("mouseover").mouseover(function(){
		Xdcs = "xgsjDay";
		initEchartsLeadBtnCss("xgsjDay");
		clickLeadEcharts(_renderArr,url,"xgsjDay",'xdwj');
	})

	$("#wjglStatis #btn_lr_query").unbind("click").click(function(){
		clickLeadEcharts(_renderArr,url,Xzcs,'xzwj');
	})
	$("#wjglStatis #btn_xg_query").unbind("click").click(function(){
		clickLeadEcharts(_renderArr,url,Xdcs,'xdwj');
	})
	$("#wjglStatis #btn_wjyf_query").unbind("click").click(function(){
		var val = $("#wjglStatis #year option:selected").val();
		var bm = $("#wjglStatis #bm option:selected").val();
		if(val||bm){
			clickLeadEcharts(_renderArr,url,null,'wjyf');
		}else{
			loadDocumentStatis()
		}
	})

}

//点击相应echarts获取相应数据
function clickLeadEcharts(_renderArr,_url,cs,id){
	for(var i=0; i<_renderArr.length; i++){
		if(id ==  _renderArr[i].id){
			var _render = _renderArr[i];
			//加载数据
			$.ajax({
				type : "post",
				url : _url,
				data: { "access_token": $("#ac_tk").val(), cs : cs,
					endTimeLr : $("#wjglStatis #endTimeLr").val(),startTimeLr : $("#wjglStatis #startTimeLr").val(),
					startTimeXg : $("#wjglStatis #startTimeXg").val(),endTimeXg : $("#wjglStatis #endTimeXg").val(),year: $("#wjglStatis #year option:selected").val(),jgid: $("#wjglStatis #bm option:selected").val()
				},
				dataType : "json",
				success : function(datas) {
					if(datas){
						var _searchData = datas['gzglDtoList'];
						for(var i=0; i<_renderArr.length; i++){
							var _render = _renderArr[i];
							var _data = datas[_render.id];
							if (_data && _data!="undefined"){
								_render.render(_data,_searchData);
							}
						}
					}
				}
			});
			break;
		}
	}
}


$(function(){
	loadDocumentStatis();
	//所有下拉框添加choose样式
	jQuery('#wjglStatis .chosen-select').chosen({width: '25%'});

	laydate.render({
		elem: '#startTimeLr'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#endTimeLr'
		,theme: '#2381E9',
	});

	laydate.render({
		elem: '#startTimeXg'
		,theme: '#2381E9',
	});
	//添加日期控件
	laydate.render({
		elem: '#endTimeXg'
		,theme: '#2381E9',
	});
});