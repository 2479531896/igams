var flModels = [];
$("#weekly_Statis_jcdw_jsrq div[name='echarts_week_lead_jcdwtj']").each(function(){
	var map = {}
	map["id"] = "jcdwtj_"+$("#"+this.id).attr("dm");
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function(data,searchData){
		var prerq = "";
		if(data&&this.chart){
			var dataxAxis= new Array();
			var datasf=new Array();
			var databsf=new Array();
			var datahj=new Array();
			var datalegend=new Array();
			var maxY=0;
			var tmp_y=0;
			var intval;
			for(var i=0;i<data.length;i++){
				var rq;
				if(data[i].rq.length >=10)
					rq = data[i].rq.substr(5)
				else
					rq = data[i].rq
				if(dataxAxis.indexOf(rq) == -1){
					dataxAxis.push(rq);
				}
			}
			for (var i = 0; i < dataxAxis.length; i++) {
				var sf = 0;
				var bsf = 0;
				for (var j = 0; j < data.length; j++) {
					var rq;
					if(data[j].rq.length >=10)
						rq = data[j].rq.substr(5)
					else
						rq = data[j].rq
					if(dataxAxis[i] == rq){
							bsf = parseInt(bsf) + parseInt(data[j].bsfnum);
							sf = parseInt(sf) + parseInt(data[j].sfnum);
					}
				}
				databsf.push(bsf);
				datasf.push(sf);
				if(prerq != dataxAxis[i]){
					if(maxY < tmp_y)
						maxY = parseInt(tmp_y)
					tmp_y = 0;
					prerq = dataxAxis[i];
					tmp_y += parseInt(bsf)+parseInt(sf); 
				}else{
					tmp_y += parseInt(bsf)+parseInt(sf); 
				}
				datahj.push(parseInt(bsf)+parseInt(sf));
			}
			
			
			if(maxY < tmp_y)
				maxY = tmp_y
			maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
			intval=maxY/4
			var pieoption = {
				tooltip : {
					trigger: 'axis',
					axisPointer : {
						type : 'shadow'
					},
					axisLabel:{
						rotate:-90,
						show: true,
						interval:0
					},
				},
				legend: {
					data: ['收费标本', '不收费标本'],
					textStyle: {
						color: '#3E9EE1'
					},
					y:35
				},
			    toolbox: {
			        show : true,
			        feature : {
			            dataView : {show: true, readOnly: false},
			            magicType : {show: true, type: ['line', 'bar']},
			            restore : {show: true},
			            saveAsImage : {show: true}
			        }
			    },
				grid: {
					left: '3%',
					right: '4%',
					top:80,
					height:220,
					containLabel: true
				},
				xAxis: {
					type: 'category',
					data: dataxAxis,
				},
				yAxis:  [{
					name: '个数',
					type: 'value',
					min: 0,
					max: maxY,
					interval: intval,
				}],
				series: [
					{
						name: '收费标本',
						type: 'bar',
						stack: '总量',
						barGap: 0,
						label: {
							normal: {
								show: true,
								position: 'insideBottom',
								formatter: function (params) {
			                    	if (params.value > 0) {
			                        	return params.value;
			                    	} else {
			                        	return '';
			                    	}
			                	}
							}
						},
						data: datasf
					},
					{
						name: '不收费标本',
						type: 'bar',
						stack: '总量',
						label: {
							normal: {
								show: true,
								position: 'insideBottom',
								formatter: function (params) {
			                    	if (params.value > 0) {
			                        	return params.value;
			                    	} else {
			                        	return '';
			                    	}
			                	}
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
					},
				]
			};
			this.chart.clear();
			this.chart.setOption(pieoption);
		}
		this.chart.hideLoading();
		this.chart.on('click',function(e){
		});
	};
	flModels.push(map);
});

var render;
//加载统计数据
var loadWeekLeadStatis = function(){
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
  		{
			id:"jcdwtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				for(var i=0;i<data.length;i++){
					data[i].jcdwmc=data[i].jcdwmc.replace("实验室","");
				}
				var predb = "";
				if(data&&this.chart){
					var datalist = new Array();
					for(var i=0;i<data.length;i++){
						datalist.push({value:data[i].sum,name:data[i].jcdwmc});
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.jcdwtj,
							x : 'left',
							y : 10
						},
						tooltip : {
							trigger : 'item',
							formatter : "{b} : \n{c} ({d}%)"
						},
						calculable : false,//是否可拖拽
						series : [
						{
							type : 'pie',
							radius : ['0%','40%'],
							center : [ '50%', '55%' ],
							startAngle:45,
							itemStyle : {
								normal : {
									label : {
										formatter:"{b}：{c}"
									},
									labelLine : {show : true}
								}
							},
							data : datalist
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
		},
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
		var _url = "/ws/statistics/getWeekLeadJcdwStatisByJsrq";
		var param ={};
		param["jsrqstart"]= $("#weekly_Statis_jcdw_jsrq #jsrqstart").val()
		param["jsrqend"]= $("#weekly_Statis_jcdw_jsrq #jsrqend").val()
		param["jsrqMstart"]= $("#weekly_Statis_jcdw_jsrq #jsrqMstart").val()
		param["jsrqMend"]= $("#weekly_Statis_jcdw_jsrq #jsrqMend").val()
		param["jsrqYstart"]= $("#weekly_Statis_jcdw_jsrq #jsrqYstart").val()
		param["jsrqYend"]= $("#weekly_Statis_jcdw_jsrq #jsrqYend").val()
		
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
	var url="/ws/statistics/getSjxxStatisByTjAndJsrq";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jsrqstart"]= $("#weekly_Statis_jcdw_jsrq #jsrqstart").val()
	map["jsrqend"]= $("#weekly_Statis_jcdw_jsrq #jsrqend").val()

	//所有检测单位测试数的所有统计按钮
	$("#weekly_Statis_jcdw_jsrq #tj1q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1q");
		map["method"]="getAlljcdwByYear";
		clickLeadEcharts(_renderArr,url,map,'jcdwtj');
	})
	//所有检测单位测试数数的月统计按钮
	$("#weekly_Statis_jcdw_jsrq #tj1m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1m");
		map["method"]="getAlljcdwByMon";
		clickLeadEcharts(_renderArr,url,map,'jcdwtj');
		
	})
	//所有检测单位测试数的周统计按钮
	$("#weekly_Statis_jcdw_jsrq #tj1w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1w");
		map["method"]="getAlljcdwByWeek";
		clickLeadEcharts(_renderArr,url,map,'jcdwtj');
	})
	//所有检测单位测试数的日统计按钮
	$("#weekly_Statis_jcdw_jsrq #tj1d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1d");
		map["method"]="getAlljcdwByDay";
		clickLeadEcharts(_renderArr,url,map,'jcdwtj');
	})
	
}

function clickjcdwtj(jcdw,method,bj,tj){
	var url="/ws/statistics/getSjxxStatisByTjAndJsrq";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jsrqstart"]= $("#weekly_Statis_jcdw_jsrq #jsrqstart").val();
	map["jsrqend"]= $("#weekly_Statis_jcdw_jsrq #jsrqend").val();
	map["tj"] =tj;
	map["jcdw"] =jcdw;
	initEchartsLeadBtnCss(bj+jcdw);
	map["method"]=method;
	clickLeadEcharts(render,url,map,'jcdwtj_'+jcdw);
}

//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
	var obj = $("#weekly_Statis_jcdw_jsrq #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weekly_Statis_jcdw_jsrq #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weekly_Statis_jcdw_jsrq #"+otherid).removeClass("bechoosed");
		$("#weekly_Statis_jcdw_jsrq #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weekly_Statis_jcdw_jsrq #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_jcdw_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weekly_Statis_jcdw_jsrq #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_jcdw_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weekly_Statis_jcdw_jsrq #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_jcdw_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weekly_Statis_jcdw_jsrq #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_jcdw_jsrq #"+getid).addClass("bechoosed");
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
	var load_flg=$("#load_flg").val();
	$("#weekly_Statis_jcdw_jsrq").load("/ws/statistics/weekLeadStatisOtherPageByJsrq?load_flg="+load_flg);
}

$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weekly_Statis_jcdw_jsrq .chosen-select').chosen({width: '100%'});
	initEchartsLeadBtnCss("tj1w");
});