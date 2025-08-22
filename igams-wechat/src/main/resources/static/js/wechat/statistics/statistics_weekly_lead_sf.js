var render;
//加载统计数据
var loadWeekLeadStatis = function(){
	var sfid=$("#sf").val();
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"sfybqk",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				if(data&&this.chart){
					var dataxAxis= new Array();
					var datasf=new Array();
					var databsf=new Array();
					var datafq=new Array();
					var databl=new Array();
					var maxY=0;
					var maxBL=0;
					var tmp_y=0;
					var intval;
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0' && data[i].rq.length>8){
							dataxAxis.push(data[i].rq.substring(5,10));
						}else if(data[i].sfjs=='0'){
							dataxAxis.push(data[i].rq);
						}
						if(data[i].sfjs=='0'){
							if(data[i].num==0)
								datafq.push('');
							else
								datafq.push(data[i].num);
						}
						else if(data[i].sfsf=='0'){
							if(data[i].num==0)
								databsf.push('');
							else
								databsf.push(data[i].num);
						}
						else if(data[i].sfsf=='1'){
							if(data[i].num==0)
								datasf.push('');
							else
								datasf.push(data[i].num);
							databl.push(data[i].bl);
						}
						if(prerq != data[i].rq){
							if(maxY < tmp_y)
								maxY = parseInt(tmp_y)
							tmp_y = 0;
							if(i!=0 && data[i-1].sfsf=='1' && maxBL < Math.abs(data[i-1].bl)){
								maxBL =Math.abs(data[i-1].bl);
							}
							prerq = data[i].rq
						}else{
							tmp_y += parseInt(data[i].num); 
						}
					}
					if(maxY < tmp_y)
						maxY = tmp_y
					if( maxBL < Math.abs(data[data.length-1].bl)){
						maxBL = Math.abs(data[data.length-1].bl);
					}
						
					maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					intval=maxY/4
					
					var sum = 0;//合计
					var datalist = [];
					for(var i=0;i<data.length;i++){
						sum+=parseInt(data[i].num);
					}
					
					maxBL = (parseInt(maxBL/100) + 1)*100
					
					var pieoption = {
						title : {
							text : '标本数：'+sum + '个',
							subtext : searchData.zqs.ybqk,
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
							data: ['收费', '不收费','废弃'],
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
						},
						{
							type: 'value',
							name: '环比',
							min: -maxBL,
							max: maxBL,
							interval: maxBL /2,
							axisLabel: {
								formatter: '{value} %'
							}
						}],
						xAxis: {
							type: 'category',
							data: dataxAxis
						},
						series: [
							{
								name: '收费',
								type: 'bar',
								stack: '总量',
								barGap: 0,
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: datasf
							},
							{
								name: '废弃',
								type: 'bar',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: datafq
							},
							{
								name: '不收费',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: databsf
							},
							{
								name:'环比',
								type:'line',
								yAxisIndex: 1,
								data:databl,
								itemStyle : { normal: {label : {show: true,formatter:"{c}%"}}}
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
		
		},
		{
			id:"sfcss",
			chart: null,
			el: null,
			render:function(data,searchData){
					var prerq = "";
					var dataxAxis= new Array();
					var datasf=new Array();
					var databsf=new Array();
					var datafq=new Array();
					var datahj=new Array();
					var maxY=0;
					var intval;
					var tmp_y=0;
					var sfnum=0;
					var bsfnum=0;
					var fqnum=0;
					if(data &&this.chart){
						for (var i = 0; i < data.length; i++) {
							if(data[i].rq.length>8){
								dataxAxis.push(data[i].rq.substring(5,10));
							}else{
								dataxAxis.push(data[i].rq);
							}
							if(data[i].fqnum=='0')
								datafq.push('');
							else
								datafq.push(data[i].fqnum);
							if(data[i].bsfnum=='0')
								databsf.push('');
							else
								databsf.push(data[i].bsfnum);
							if(data[i].sfnum=='0')
								datasf.push('');
							else
								datasf.push(data[i].sfnum);

							datahj.push(parseInt(data[i].bsfnum)+parseInt(data[i].sfnum)+ parseInt(data[i].fqnum));
						}
						for (var i = 0; i < datasf.length; i++) {
							if(datasf[i]!=""){
								if(datasf[i]>sfnum){
									sfnum=datasf[i];
								}
							}
							if(databsf[i]!=""){
								if(databsf[i]>bsfnum){
									bsfnum=databsf[i];
								}
							}
							if(datafq[i]!=""){
								if(datafq[i]>fqnum){
									fqnum=datafq[i];
								}
							}
						}
						maxY=parseInt(fqnum+bsfnum+sfnum);
						maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
						intval=maxY/4
						var pieoption = {
								title : {
									subtext : searchData.zqs.jcxmnum,
									x : 'left'
								},
								tooltip : {
									trigger: 'axis',
									axisPointer : {			// 坐标轴指示器，坐标轴触发有效
										type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
									}
								},
								legend: {
									data: ['收费', '不收费','废弃'],
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
										name: '收费',
										type: 'bar',
										stack: '总量',
										barGap: 0,
										label: {
											normal: {
												show: true,
												position: 'insideBottom'
											}
										},
										data: datasf
									},
									{
										name: '废弃',
										type: 'bar',
										stack: '总量',
										label: {
											normal: {
												show: true,
												position: 'insideBottom'
											}
										},
										data: datafq
									},
									{
										name: '不收费',
										type: 'bar',
										stack: '总量',
										label: {
											normal: {
												show: true,
												position: 'insideBottom'
											}
										},
										data: databsf
									},
									{
										name: '合计',
										type: 'bar',
										stack: '总量',
										label: {
							                normal: {
							                    show: true,
							                    position: 'insideBottom',
							                    textStyle:{ color:'#ff4848',fontSize: 12 }
							                }
							            },
										itemStyle: {
							                normal:{
							                    color:'rgba(128, 128, 128, 0)'
							                }
							            },
										data: datahj
									},]
							};
							this.chart.setOption(pieoption);
							this.chart.hideLoading();
							//点击事件
							this.chart.on("click",function(){
								//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
							});
					}
			}
		
		},
		{
			id:"sfsfcss",
			chart: null,
			el: null,
			render:function(data,searchData){
					var prerq = "";
					var dataxAxis= new Array();
					var dataDNA=new Array();
					var dataRNA=new Array();
					var dataDR=new Array();
					var datahj=new Array();
					var maxY=0;
					var DRnum=new Array();
					var DNAnum=new Array();
					var RNAnum=new Array();
					var intval;
					if(data &&this.chart){
						for (var i = 0; i < data.length; i++) {
							if(data[i].rq.length>8)	{
								if(dataxAxis.indexOf(data[i].rq.substring(5,10))=='-1'){
									dataxAxis.push(data[i].rq.substring(5,10));
								}
							}else{
								if(dataxAxis.indexOf(data[i].rq)=='-1'){
									dataxAxis.push(data[i].rq);
								}
							}
						}
						for (var i = 0; i < dataxAxis.length; i++) {
							for (var j = 0; j < data.length; j++) {
								if(data[j].rq.length>8){
									var date=data[j].rq.substring(5,10);
									if(dataxAxis[i]==date){
										if(data[j].jcxm=="DNA"){
											DNAnum.push(data[j].num);
											if(data[j].num==0){
												dataDNA.push('');
											}else{
												dataDNA.push(data[j].num);
											}
										}else if(data[j].jcxm=="RNA"){
											RNAnum.push(data[j].num);
											if(data[j].num==0){
												dataRNA.push('');
											}else{
												dataRNA.push(data[j].num);
											}
										}else if(data[j].jcxm=="D+R"){
											DRnum.push(data[j].num);
											if(data[j].num==0){
												dataDR.push('');
											}else{
												dataDR.push(data[j].num);
											}
										}
									}
								}else{
									if(dataxAxis[i]==data[j].rq){
										if(data[j].jcxm=="DNA"){
											DNAnum.push(data[j].num);
											if(data[j].num==0){
												dataDNA.push('')
											}else{
												dataDNA.push(data[j].num)
											}
										}else if(data[j].jcxm=="RNA"){
											RNAnum.push(data[j].num);
											if(data[j].num==0){
												dataRNA.push('')
											}else{
												dataRNA.push(data[j].num)
											}
										}else if(data[j].jcxm=="D+R"){
											DRnum.push(data[j].num);
											if(data[j].num==0){
												dataDR.push('')
											}else{
												dataDR.push(data[j].num)
											}
										}
									}
								}
							}
						}
						for (var i = 0; i < DRnum.length; i++) {
							if(parseInt(DRnum[i]+DNAnum[i])>maxY){
								maxY=parseInt(DRnum[i]+DNAnum[i]+RNAnum[i]);
							}
							datahj.push(parseInt(DRnum[i])+parseInt(DNAnum[i])+ parseInt(RNAnum[i]));
						}
						maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
						intval=maxY/4
						var pieoption = {
								title : {
									subtext : searchData.zqs.ybxxType,
									x : 'left'
								},
								tooltip : {
									trigger: 'axis',
									axisPointer : {			// 坐标轴指示器，坐标轴触发有效
										type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
									}
								},
								legend: {
									data: ['DNA', 'RNA','D+R'],
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
										name: 'DNA',
										type: 'bar',
										stack: '总量',
										barGap: 0,
										label: {
											normal: {
												show: true,
												position: 'insideBottom'
											}
										},
										data: dataDNA
									},
									{
										name: 'RNA',
										type: 'bar',
										stack: '总量',
										label: {
											normal: {
												show: true,
												position: 'insideBottom'
											}
										},
										data: dataRNA
									},
									{
										name: 'D+R',
										type: 'bar',
										stack: '总量',
										label: {
											normal: {
												show: true,
												position: 'insideBottom'
											}
										},
										data: dataDR
									},
									{
										name: '合计',
										type: 'bar',
										stack: '总量',
										label: {
							                normal: {
							                    show: true,
							                    position: 'insideBottom',
							                    textStyle:{ color:'#ff4848',fontSize: 12 }
							                }
							            },
										itemStyle: {
							                normal:{
							                    color:'rgba(128, 128, 128, 0)'
							                }
							            },
										data: datahj
									}]
							};
							this.chart.setOption(pieoption);
							this.chart.hideLoading();
							//点击事件
							this.chart.on("click",function(){
								//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
							});
					}
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
		var _url = "/ws/statistics/getWeekLeadProvinceStatis";
		var param ={};
		param["jsrqstart"]= $("#weekly_Statis_sf #jsrqstart").val()
		param["jsrqend"]= $("#weekly_Statis_sf #jsrqend").val()
		param["jsrqMstart"]= $("#weekly_Statis_sf #jsrqMstart").val()
		param["jsrqMend"]= $("#weekly_Statis_sf #jsrqMend").val()
		param["jsrqYstart"]= $("#weekly_Statis_sf #jsrqYstart").val()
		param["jsrqYend"]= $("#weekly_Statis_sf #jsrqYend").val()
		param["sf"]=sfid
		$.ajax({
			type : "post",
			url : _url,
			data:param,
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
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
	render=_renderArr;
	$("#weekly_Statis_sf .sftj").attr("style","visibility:hidden;");
}

//点击相应echarts获取相应数据
function clickLeadEcharts(_renderArr,_url,map,id){
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
						//throw "loadClientStatis数据获取异常";
					}
				}
			});
			break;
		}
	}
}

//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/ws/statistics/getSjxxStatisByTj";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jsrqstart"]= $("#weekly_Statis_sf #jsrqstart").val()
	map["jsrqend"]= $("#weekly_Statis_sf #jsrqend").val()
	map["sf"]=$("#weekly_Statis_sf #sf").val();
	//标本情况的年统计按钮
	$("#weekly_Statis_sf #tj1q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1q");
		map["method"]="getSjxxYearBySyAndSf";
		clickLeadEcharts(_renderArr,url,map,'sfybqk');
	})
	//标本情况的月统计按钮
	$("#weekly_Statis_sf #tj1m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1m");
		map["method"]="getSjxxMonBySyAndSf";
		clickLeadEcharts(_renderArr,url,map,'sfybqk');
	})
	//标本情况的周统计按钮
	$("#weekly_Statis_sf #tj1w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1w");
		map["method"]="getSjxxWeekBySyAndSf";
		clickLeadEcharts(_renderArr,url,map,'sfybqk');
	})
	//标本情况的日统计按钮
	$("#weekly_Statis_sf #tj1d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1d");
		map["method"]="getSjxxDayBySyAndSf";
		clickLeadEcharts(_renderArr,url,map,'sfybqk');
	})
	
	//检测总次数的年统计按钮
	$("#weekly_Statis_sf #tj2q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2q");
		map["method"]="getSjxxDRByYearAndSf";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'sfcss');
	})
	//检测总次数的月统计按钮
	$("#weekly_Statis_sf #tj2m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2m");
		map["method"]="getSjxxDRByMonAndSf";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'sfcss');
	})
	//检测总次数的周统计按钮
	$("#weekly_Statis_sf #tj2w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2w");
		map["method"]="getSjxxDRByWeekAndSf";
		map["tj"]="week";
		clickLeadEcharts(_renderArr,url,map,'sfcss');
	})
	//检测总次数的日统计按钮
	$("#weekly_Statis_sf #tj2d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2d");
		map["method"]="getSjxxDRByDayAndSf";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'sfcss');
	})
	
	//收费标本检测项目次数（年）
	$("#weekly_Statis_sf #tj3q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3q");
		map["method"]="getYbxxTypeByYearAndSf";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'sfsfcss');
	})
	//收费标本检测项目次数（月）
	$("#weekly_Statis_sf #tj3m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3m");
		map["method"]="getYbxxTypeByMonAndSf";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'sfsfcss');
	})
	//收费标本检测项目次数（周）
	$("#weekly_Statis_sf #tj3w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3w");
		map["method"]="getYbxxTypeByWeekAndSf";
		map["tj"]="week";
		clickLeadEcharts(_renderArr,url,map,'sfsfcss');
	})
	//收费标本检测项目次数（日）
	$("#weekly_Statis_sf #tj3d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3d");
		map["method"]="getYbxxTypeByDayAndSf";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'sfsfcss');
	})
	
}

//function clicksftj(sf,method,bj,tj){
//	debugger
//	var url="/ws/statistics/getSjxxStatisByTj";
//	var map ={}
//	map["access_token"]=$("#ac_tk").val();
//	map["jsrqstart"]= $("#weekly_Statis_sf #jsrqstart").val();
//	map["jsrqend"]= $("#weekly_Statis_sf #jsrqend").val();
//	map["tj"] =tj;
//	map["sf"] =sf;
//	initEchartsLeadBtnCss(bj+sf);
//	map["method"]=method;
//	clickLeadEcharts(render,url,map,'sftj_'+sf);
//}

//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
	var obj = $("#weekly_Statis_sf #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weekly_Statis_sf #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weekly_Statis_sf #"+otherid).removeClass("bechoosed");
		$("#weekly_Statis_sf #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weekly_Statis_sf #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_sf #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weekly_Statis_sf #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_sf #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weekly_Statis_sf #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_sf #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weekly_Statis_sf #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_sf #"+getid).addClass("bechoosed");
	}
}

$("#ybxxhead a").hover(function(){
	var getid=$(this).attr("id");
	var gettitle=$(this).attr("title");
	$("#"+getid+" .fa").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
},function(){
	var getid=$(this).attr("id");
	var gettitle=$(this).attr("title");
	var getclass=$(this).attr("class");
	var b=getclass.indexOf("bechoosed");
	if(b=="-1"){
		$("#"+getid+" .fa").removeAttr("style");
	}
})

$("div[name='ybxxhead'] a").hover(function(){
	var getid=$(this).attr("id");
	var gettitle=$(this).attr("title");
	$("#"+getid+" .fa").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
},function(){
	var getid=$(this).attr("id");
	var gettitle=$(this).attr("title");
	var getclass=$(this).attr("class");
	var b=getclass.indexOf("bechoosed");
	if(b=="-1"){
		$("#"+getid+" .fa").removeAttr("style");
	}
})

function init(){
}

function weekiy_back(){
	$("#weekly_Statis_sf").load("/ws/statistics/weekLeadStatisOtherPage?load_flg="+$("#weekly_Statis_sf #load_flg").val());
}

$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weekly_Statis_sf .chosen-select').chosen({width: '100%'});
});