var flModels = [];
$("#weekly_Statis_partner div[name='echarts_week_lead_hbtj']").each(function(){
	var map = {}
	map["id"] = "hbtj_"+$("#"+this.id).attr("dm");
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


//加载统计数据
var loadWeekLeadStatis = function(){
		console.log("第三方实验室");
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var tldb = $("#weekly_Statis_partner #tldb").val();
	var tlnum = $("#weekly_Statis_partner #tlnum").val();
	var tldbs = tldb.split(",");
	var tlnums = tlnum.split(",");
	var _renderArr = [
  		{
			id:"hbztj",
			chart: null,
			el: null,
			render:function(data,searchData){
				var predb = "";
				if ((data||(tldbs!=""&& tldbs.length>0))&&this.chart){
					var datalist = new Array();
					var tesxdate =  $("#weekly_Statis_partner #jsrqstart").val()+"~"+$("#weekly_Statis_partner #jsrqend").val();
				if(data&&this.chart){
					var sum = 0;//合计
					tesxdate =searchData.zqs.hbztj;
					var len = 0;
					if (data.length>15){
						len = 15;
					}else {
						len = data.length;
					}
					for(var i=0;i<len;i++){
						if(predb == data[i].db){
							sum = sum + parseInt(data[i].sfnum) + parseInt(data[i].bsfnum);
						}else{
							if(i != 0 && sum > 0){
								var map = {value:sum, name:data[i-1].db+" "+sum,flg:data[i-1].db}
								datalist.push(map);
								sum = parseInt(data[i].sfnum) + parseInt(data[i].bsfnum);
							}else{
								sum = sum + parseInt(data[i].sfnum) + parseInt(data[i].bsfnum);
							}
						}
						if(i == data.length-1 && sum > 0){
							var map = {value:sum, name:data[i].db+" "+sum,flg:data[i].db}
							datalist.push(map);
						}
						predb = data[i].db;
					}
				   }else if(tldbs!="" && tldbs.length > 0 && this.chart){
						var len = 0;
						if (tldbs.length>15){
							len = 15;
						}else {
							len = tldbs.length;
						}
						for (var i = 0; i < len; i++) {
							var map = { value: tlnums[i], name: tldbs[i] + " " + tlnums[i], flg: [i] }
							datalist.push(map);
							predb = tldbs[i];
						}
					}
					var pieoption = {
						title: {
							subtext: tesxdate,
							x: 'left',
							y: 10
						},
						tooltip: {
							trigger: 'item',
							formatter: "{b} : {c} ({d}%)"
						},
						calculable: false,//是否可拖拽
						series: [
							{
								name: '合作伙伴',
								type: 'pie',
								radius: '55%',
								center: ['50%', '50%'],
								data: datalist.sort(function(a, b) { return a.value - b.value; }),
								roseType: 'radius'
							}]
					};
					this.chart.hideLoading();
					this.chart.setOption(pieoption);
					
					//点击事件
					this.chart.on("click", function(e) {
						window.location.href = "#" + e.data.flg;
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
		for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						if(_render.id=='hbztj')
						_render.render(null,null);
		}
		//加载数据
		var _url = "/ws/statistics/getWeekLeadPartnerStatis";
		var param ={};
		param["jsrqstart"]= $("#weekly_Statis_partner #jsrqstart").val()
		param["jsrqend"]= $("#weekly_Statis_partner #jsrqend").val()
		param["jsrqMstart"]= $("#weekly_Statis_partner #jsrqMstart").val()
		param["jsrqMend"]= $("#weekly_Statis_partner #jsrqMend").val()
		param["jsrqYstart"]= $("#weekly_Statis_partner #jsrqYstart").val()
		param["jsrqYend"]= $("#weekly_Statis_partner #jsrqYend").val()
		param["zq"]= $("#weekly_Statis_partner [name=zq]").val()
		param["hbfl"]= $("#weekly_Statis_partner #hbfl").val()
		param["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val()
		param["dbhbs"]= $("#weekly_Statis_partner #dbhbs").val()
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
						if(_render.id=='hbztj')continue;
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
	map["jsrqstart"]= $("#weekly_Statis_partner #jsrqstart").val()
	map["jsrqend"]= $("#weekly_Statis_partner #jsrqend").val()

	//合作伙伴总数的所有统计按钮
	$("#weekly_Statis_partner #tj3q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3q");
		map["method"]="getAllhbByYear";
		map["zq"]= $("#weekly_Statis_partner #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbztj');
	})
	//合作伙伴总数的月统计按钮
	$("#weekly_Statis_partner #tj3m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3m");
		map["method"]="getAllhbByMon";
		map["zq"]= $("#weekly_Statis_partner #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbztj');
		
	})
	//合作伙伴总数的周统计按钮
	$("#weekly_Statis_partner #tj3w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3w");
		map["method"]="getAllhbByWeek";
		map["zq"]= $("#weekly_Statis_partner #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbztj');
	})
	//合作伙伴总数的日统计按钮
	$("#weekly_Statis_partner #tj3d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3d");
		map["method"]="getAllhbByDay";
		map["zq"]= $("#weekly_Statis_partner #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbztj');
	})
	//伙伴的所有统计按钮
	$("#weekly_Statis_partner a[name='tj1q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByYear";
		map["zq"]= $("#weekly_Statis_partner #zq_"+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm"));
	})
	//伙伴的月统计按钮
	$("#weekly_Statis_partner a[name='tj1m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByMon";
		map["zq"]= $("#weekly_Statis_partner #zq_"+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm"));
	})
	//伙伴的周统计按钮
	$("#weekly_Statis_partner a[name='tj1w']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByWeek";
		map["zq"]= $("#weekly_Statis_partner #zq_"+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm"));
	})
	//伙伴的日统计按钮
	$("#weekly_Statis_partner a[name='tj1d']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByDay";
		map["zq"]= $("#weekly_Statis_partner #zq_"+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm"));
	})
	
	//周期改变事件
	$("#weekly_Statis_partner input[name='zq']").unbind("change").change(function(e){
		var re = new RegExp("^[0-9]*[1-9][0-9]*$");
		if (e.currentTarget.value != "" ) {
			if (!re.test(e.currentTarget.value)) {
	            $.alert("周期最小为1");
	            e.currentTarget.value = 1;
	            e.currentTarget.focus();
	        }
		}else{
			e.currentTarget.value = 14;
        	e.currentTarget.focus();
		}
		var dateid = "ybxxhead";
		var hz = ""
		if($("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm")){
			dateid += "_"+ $("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm");
			hz = "_"+ $("#weekly_Statis_partner #"+e.currentTarget.id).attr("dm")
		}
		var method = $("#"+$("#weekly_Statis_partner #"+dateid+" .bechoosed")[0].id).attr("method")
		map["method"]=method;
		map["zq"]= e.currentTarget.value;
		map["hbfl"]= $("#weekly_Statis_partner #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner #hbzfl").val();
		clickLeadEcharts(_renderArr,url,map,$("#weekly_Statis_partner #"+e.currentTarget.id).attr("tjm")+hz);
	});
}

//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
	var obj = $("#weekly_Statis_partner #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weekly_Statis_partner #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weekly_Statis_partner #"+otherid).removeClass("bechoosed");
		$("#weekly_Statis_partner #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weekly_Statis_partner #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weekly_Statis_partner #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weekly_Statis_partner #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weekly_Statis_partner #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner #"+getid).addClass("bechoosed");
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
	$("#weekly_Statis_partner").load("/wechat/statistics/pageListLocal_weekly");
}
$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weekly_Statis_partner .chosen-select').chosen({width: '100%'});
});