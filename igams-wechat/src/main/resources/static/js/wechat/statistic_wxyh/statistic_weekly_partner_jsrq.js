var flModels = [];
$("#weekly_Statis_partner_jsrq div[name='echarts_week_lead_hbtj']").each(function(){
	var map = {}
	map["id"] = "hbtj_"+$("#"+this.id).attr("dm");
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function(data,searchData){
		var prerq = "";
		if(data&&this.chart){
			$("#weekly_Statis_partner_jsrq #hbtj_"+$("#echarts_week_lead_" + this.id).attr("dm")+"yxxs").val(searchData.zqs["hbtj_"+$("#echarts_week_lead_" + this.id).attr("dm")+"yxxs"]);
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
						if(data[j].sfsf=='0'){
							bsf = bsf + parseInt(data[j].num);
						}else if(data[j].sfsf=='1'){
							sf = sf + parseInt(data[j].num);
						}
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
				title : {
					subtext : searchData.zqs["hbtj_"+$("#echarts_week_lead_" + this.id).attr("dm")+"yxxs"],
					x : 'left',
					y : 10
				},
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
console.log('11');
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
  		{
			id:"hbztj",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					$("#weekly_Statis_partner_jsrq #hbztjyxxs").val(searchData.zqs["hbztjyxxs"]);
					var sum = 0;//合计
					var predb=data[0].db;
					var datalist = new Array();
					for(var i=0;i<data.length;i++){
						if(predb == data[i].db){
							sum = sum + parseInt(data[i].num);
						}else{
							var map = {value:sum, name:predb+" "+sum,flg:predb}
							datalist.push(map);
							predb=data[i].db;
							sum = parseInt(data[i].num);
						}
					}
					var map = {value:sum, name:predb+" "+sum,flg:predb}
					datalist.push(map);
					var pieoption = {
						title : {
							subtext : searchData.zqs.hbztj+" "+searchData.zqs["hbztjyxxs"],
							x : 'left',
							y : 10
						},
						tooltip : {
							trigger : 'item',
							formatter : "{b} : {c} ({d}%)"
						},
						calculable : false,//是否可拖拽
						series : [
						   {
					            name:'合作伙伴',
					            type:'pie',
							    radius : ['0%','50%'],
							    center : [ '50%', '55%' ],
					            data:datalist.sort(function (a, b) { return a.value - b.value; }),
						 }]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(e){
						 window.location.href = "#"+e.data.flg;
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
		var _url = "/ws/ststictis/getPartnerStatisWeeklyByJsrq";
		var param ={};
		param["jsrqstart"]= $("#weekly_Statis_partner_jsrq #jsrqstart").val()
		param["jsrqend"]= $("#weekly_Statis_partner_jsrq #jsrqend").val()
		param["jsrqMstart"]= $("#weekly_Statis_partner_jsrq #jsrqMstart").val()
		param["jsrqMend"]= $("#weekly_Statis_partner_jsrq #jsrqMend").val()
		param["jsrqYstart"]= $("#weekly_Statis_partner_jsrq #jsrqYstart").val()
		param["jsrqYend"]= $("#weekly_Statis_partner_jsrq #jsrqYend").val()
		param["zq"]= $("#weekly_Statis_partner_jsrq [name=zq]").val()
		param["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val()
		param["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val()
		param["yhid"]= $("#weekly_Statis_partner_jsrq #yhid").val()
	    param["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val()
	    param["method"]= $("#weekly_Statis_partner_jsrq #method").val()
		param["ptgss"]= $("#weekly_Statis_partner_jsrq #ptgss").val()
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
	var url="/ws/statictis/getweeklymodel_conditionByJsrq";
	var map ={}
	map["jsrqstart"]= $("#weekly_Statis_partner_jsrq #jsrqstart").val()
	map["jsrqend"]= $("#weekly_Statis_partner_jsrq #jsrqend").val()
	map["yhid"]= $("#weekly_Statis_partner_jsrq #yhid").val()
	map["ptgss"]= $("#weekly_Statis_partner_jsrq #ptgss").val();
	//合作伙伴总数的所有统计按钮
	$("#weekly_Statis_partner_jsrq #tj3q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3q");
		map["method"]="getAllhbByYear";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
	    map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbmcs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbztjyxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbztj');
	})
	//合作伙伴总数的月统计按钮
	$("#weekly_Statis_partner_jsrq #tj3m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3m");
		map["method"]="getAllhbByMon";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbmcs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbztjyxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbztj');
		
	})
	//合作伙伴总数的周统计按钮
	$("#weekly_Statis_partner_jsrq #tj3w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3w");
		map["method"]="getAllhbByWeek";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbmcs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbztjyxxs").val()
	    clickLeadEcharts(_renderArr,url,map,'hbztj');
	})
	//合作伙伴总数的日统计按钮
	$("#weekly_Statis_partner_jsrq #tj3d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3d");
		map["method"]="getAllhbByDay";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_hbztj").val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["dbmcs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbztjyxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbztj');
	})
	
	//伙伴的所有统计按钮
	$("#weekly_Statis_partner_jsrq a[name='tj1q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByYear";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_"+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		var hbmc = $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
		map["dbmcs"]=hbmc;
		map["dbs"]=hbmc;
	    map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbtj_"+hbmc+"yxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	
	//伙伴的月统计按钮
	$("#weekly_Statis_partner_jsrq a[name='tj1m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByMon";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_"+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		var hbmc = $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
		map["dbmcs"]=hbmc;
		map["dbs"]=hbmc;
	    map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbtj_"+hbmc+"yxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//伙伴的周统计按钮
	$("#weekly_Statis_partner_jsrq a[name='tj1w']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByWeek";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_"+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		var hbmc = $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
		map["dbs"]=hbmc;
		map["dbmcs"]=hbmc;
	    map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbtj_"+hbmc+"yxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//伙伴的日统计按钮
	$("#weekly_Statis_partner_jsrq a[name='tj1d']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getHbByDay";
		map["zq"]= $("#weekly_Statis_partner_jsrq #zq_"+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		var hbmc = $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
		map["dbmcs"]=hbmc;
		map["dbs"]=hbmc;
	    map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
		map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbtj_"+hbmc+"yxxs").val()
		clickLeadEcharts(_renderArr,url,map,'hbtj_'+$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	
	//周期改变事件
	$("#weekly_Statis_partner_jsrq input[name='zq']").unbind("change").change(function(e){
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
		if($("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm")){
			dateid += "_"+ $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
			hz = "_"+ $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm")
		}
		var method = $("#"+$("#weekly_Statis_partner_jsrq #"+dateid+" .bechoosed")[0].id).attr("method")
		map["method"]=method;
		map["zq"]= e.currentTarget.value;
		map["hbfl"]= $("#weekly_Statis_partner_jsrq #hbfl").val();
		map["hbzfl"]= $("#weekly_Statis_partner_jsrq #hbzfl").val();
		map["yhid"]= $("#weekly_Statis_partner_jsrq #yhid").val();
		if(e.currentTarget.id=="zq_hbztj"){
			var hbmc = $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
			map["dbmcs"]=$("#weekly_Statis_partner_jsrq #dbhbs").val();
			map["dbs"]=$("#weekly_Statis_partner_jsrq #dbhbs").val();
			map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
			map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbztjyxxs").val()
		}else{
			var hbmc = $("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("dm");
			map["dbmcs"]=hbmc;
			map["dbs"]=hbmc;
			map["dbhbs"]= hbmc;
			map["yxxs"]= $("#weeklyStatis_wxyh_jsrq #hbtj_"+hbmc+"yxxs").val()
		}
		clickLeadEcharts(_renderArr,url,map,$("#weekly_Statis_partner_jsrq #"+e.currentTarget.id).attr("tjm")+hz);
	});
	$("#weekly_Statis_partner_jsrq  [name='jcxm']").unbind("click").click(function(e){
		var yxxid=e.currentTarget.id+"yxxs";
		var yxxs=$("#weekly_Statis_partner_jsrq #"+yxxid).val();
		$.showDialog("/ws/statistics/pagedataScreenContent?bdid="+e.currentTarget.id+"&yxxs="+yxxs,'筛选',ScreenContentConfig);
	});
	var ScreenContentConfig = {
		width        : "800px",
		modalName : "ScreenContentModal",
		buttons : {
			success : {
				label : "确定",
				className : "btn-primary",
				callback : function    () {
					var $this = this;
					var opts = $this["options"]||{};
					$("#weeklyStatis_jsrq_screencontent input[name='access_token']").val($("#ac_tk").val());
					var bdid=$("#weeklyStatis_jsrq_screencontent #bdid").val();
					var id="ybxxhead";
					if(bdid.indexOf("hbtj_")!=-1){
						id=id+"_"+bdid.split("_")[1];
					}

					var yxxs="";
					$("#weeklyStatis_jsrq_screencontent #dyxx input[type='checkbox']").each(function(index){
						if (this.checked){
							yxxs+=","+this.value;
						}
					});
					if (yxxs.length>0){
						yxxs=yxxs.substr(1);
					}
					var  zqid = $("#weekly_Statis_partner_jsrq #"+id+" .bechoosed");
					if (zqid[0].id.indexOf("Day")){
						map["tj"]="day";
					}else if (zqid[0].id.indexOf("Mon")){
						map["tj"]="mon";
					}else if (zqid[0].id.indexOf("Week")){
						map["tj"]="week";
					}else if (zqid[0].id.indexOf("Year")){
						map["tj"]="year";
					}
					map["yxxs"]=yxxs;
					if(bdid.indexOf("hbtj_")!=-1){
						if("tj1d"==zqid[0].name){
							map["method"]="getHbByDay";
						}else if("tj1w"==zqid[0].name){
							map["method"]="getHbByWeek";
						}else if("tj1m"==zqid[0].name){
							map["method"]="getHbByMon";
						}else if("tj1q"==zqid[0].name){
							map["method"]="getHbByYear";
						}
						var hbmc = bdid.split("_")[1];
						map["dbmcs"]=hbmc;
						map["dbs"]=hbmc;
						map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
						map["zq"]= $("#weekly_Statis_partner_jsrq #zq_"+$("#weekly_Statis_partner_jsrq #"+zqid[0].id).attr("dm")).val();
					}else{
						if("tj3d"==zqid[0].id){
							map["method"]="getAllhbByDay";
						}else if("tj3w"==zqid[0].id){
							map["method"]="getAllhbByWeek";
						}else if("tj3m"==zqid[0].id){
							map["method"]="getAllhbByMon";
						}else if("tj3q"==zqid[0].id){
							map["method"]="getAllhbByYear";
						}
						map["dbmcs"]=$("#weekly_Statis_partner_jsrq #dbhbs").val();
						map["dbs"]=$("#weekly_Statis_partner_jsrq #dbhbs").val();
						map["dbhbs"]= $("#weekly_Statis_partner_jsrq #dbhbs").val();
						map["zq"]= $("#weekly_Statis_partner_jsrq #zq_hbztj").val();
					}
					clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val());
					$.closeModal(opts.modalName);
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
}

//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
	var obj = $("#weekly_Statis_partner_jsrq #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weekly_Statis_partner_jsrq #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weekly_Statis_partner_jsrq #"+otherid).removeClass("bechoosed");
		$("#weekly_Statis_partner_jsrq #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weekly_Statis_partner_jsrq #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weekly_Statis_partner_jsrq #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weekly_Statis_partner_jsrq #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weekly_Statis_partner_jsrq #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weekly_Statis_partner_jsrq #"+getid).addClass("bechoosed");
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
function backZoneWeekList(){
    var bj = $("#weekly_Statis_partner_jsrq #bj").val();
	var ptgss=$("#weekly_Statis_partner_jsrq #ptgss").val();
	var ldtzbj='';
	if(ptgss){
		ldtzbj='1';
	}
    if(bj=='1'){//返回不同的页面
        $("#weekly_Statis_partner_jsrq").load("/ws/statictis/weeklyByYhidAndJsrq?sign="+encodeURIComponent($("#sign").val())+"&jsrqstart="+$("#jsrqstart").val()+"&jsrqend="+$("#jsrqend").val()+"&yhid="+$("#weekly_Statis_partner_jsrq #yhid").val()+"&ptgss="+$("#weekly_Statis_partner_jsrq #ptgss").val()+"&ldtzbj="+ldtzbj);
    }else{
    	$("#weekly_Statis_partner_jsrq").load("/ws/statictis/weeklyByYhidAndJsrq?sign="+encodeURIComponent($("#sign").val())+"&jsrqstart="+$("#jsrqstart").val()+"&jsrqend="+$("#jsrqend").val()+"&yhid="+$("#weekly_Statis_partner_jsrq #yhid").val()+"&ptgss="+$("#weekly_Statis_partner_jsrq #ptgss").val()+"&xsbj=1"+"&ldtzbj="+ldtzbj);
    }
}
function weekiy_back(){
	$("#weekly_Statis_partner_jsrq").load("/wechat/statistics/pageListLocal_weekly_jsrq");
}
$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weekly_Statis_partner_jsrq .chosen-select').chosen({width: '100%'});
});