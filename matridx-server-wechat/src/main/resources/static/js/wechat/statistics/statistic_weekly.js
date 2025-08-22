var flModels = [];
	$("#weeklyStatis_wxyh div[name='echarts_weekly_wxyh_fltj']").each(function(){
		var map = {}
		map["id"] = "fltj_"+$("#"+this.id).attr("dm");
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
							if(data[j].sfsf=='0'){
								bsf = parseInt(bsf) + parseInt(data[j].num);
							}else if(data[j].sfsf=='1'){
								sf = parseInt(sf) + parseInt(data[j].num);
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
						data: ['收费', '不收费'],
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
						height:200,
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
							name: '收费',
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
							name: '不收费',
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
			this.chart.on('click',function(){
				
			});
		};
		flModels.push(map);
	});
var loadDomWeekly=function(){
	var _eventTag = "weeklyStatis_wxyh";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_weekly_wxyh_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"ybqk",
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
					
					maxBL = (parseInt(maxBL/100) + 1)*100
					
					var pieoption = {
						title : {
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
					});
				}
			}
		
		},{
			id:"ybtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					var sum = 0;//合计
					var datalist = [];
					for(var i=0;i<data.length;i++){
						datalist.push({value:data[i].cn,name:data[i].csmc});
						sum+=data[i].cn;
					}
					var pieoption = {
						title : {
							text : '标本数：：'+sum + '个',
							subtext : searchData.zqs.ybtj,
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
							type : 'pie',
							radius : ['0%','50%'],
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
							data : datalist.length==0?[{}]:datalist
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
		},{
			id:"jcxmnum",
			chart: null,
			el: null,
			render:function(data,searchData){
					var prerq = "";
					var dataxAxis= new Array();
					var datasf=new Array();
					var databsf=new Array();
					var datafq=new Array();
					var maxY=0;
					var intval;
					var tmp_y=0;
					var sfnum=0;
					var bsfnum=0;
					var fqnum=0;
					if(data &&this.chart){
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
							}
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
		
		},{
			id:"ybxxType",
			chart: null,
			el: null,
			render:function(data,searchData){
					var prerq = "";
					var dataxAxis= new Array();
					var dataDNA=new Array();
					var dataRNA=new Array();
					var dataDR=new Array();
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
		
		},{
			id:"lcfkList",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var datalist = new Array();
					for(var i=0;i<data.length;i++){
						datalist.push({value:data[i].num,name:data[i].sfzq});
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.lcfkList,
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
							type : 'pie',
							radius : ['0%','50%'],
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
		},{
			id:"jyjgOfYblx",
			chart:null,
			el:null,
			render:function(data,searchData){
				var yblxMap=new Array();
				var topThree=new Array();
				var yblxOfsum=new Array();
				var topThreeAll=new Array();
				var lastSum=new Array();
				var yblx=new Array();
				if(data){
					//查询出来的数据去重复，只保留标本类型
					for (var i = 0; i < data.length; i++) {
						if(yblxMap.indexOf(data[i].yblx)=="-1"){
							yblxMap.push(data[i].yblx);
						}
					}
				}
				//循环标本类型和data，获取到sum，存到yblx
				for (var i = 0; i < yblxMap.length; i++) {
					for (var j = 0; j < data.length; j++) {
						if(yblxMap[i]==data[j].yblx){
							if(data[j].sum>0){
								yblx.push(yblxMap[i])
								break;
							}
						}
					}
				}
				//difference为sum为0的标本类型，
				var difference = yblxMap.filter(function(v){ return !(yblx.indexOf(v) > -1) }).concat(yblx.filter(function(v){ return !(yblxMap.indexOf(v) > -1)}));
				
				//拿到sum 不为0的yblx和sum ，存到yblxOfsum
				for (var i = 0; i < yblxMap.length; i++) {
					for (var j = 0; j < data.length; j++) {
						if(yblxMap[i]==data[j].yblx){
							if(data[j].sum>0){
								  yblxOfsum.push({yblx:yblxMap[i],sum:data[j].sum})
								break;
							}
						}
					}
				}
				//手动给difference的标本类型赋值 sum为0，存到yblxOfsum
				for (var i = 0; i < difference.length; i++) {
					 yblxOfsum.push({yblx:difference[i],sum:0});
				}
				//yblxOfsum按照sum排序，从大到小
				yblxOfsum.sort((a, b) => {
					  return  b.sum-a.sum
					})
				//取出yblxOfsum，取前三
				for (var i = 0; i <yblxOfsum.length; i++) {
					topThree.push(yblxOfsum[i].yblx);
					if(i==2){
						break;
					}
				}
				//根据yblxOfsum从data中获取到（阳性，阴性，疑似）的num，存到topThreeAll
				for (var i = 0; i < data.length; i++) {
					for (var j = 0; j < topThree.length; j++) {
						if(data[i].yblx==topThree[j]){
							topThreeAll.push(data[i]);
						}
					}
				}
				//判断topThreeAll不为空时，拼接table
				if(topThreeAll!=null&&topThreeAll.length>0){
					var table ="<table id='tab1'>"
							table+="<thead>";
								table+="<tr>";
									table+="<th  class='yblx_td'>标本类型</th>";
									table+="<th class='other_td'>检验结果</th>";
									table+="<th class='other_td'>个数</th>";
									table+="<th class='other_td'>占比</th>";
								table+="</tr>";
						table+="</thead>";
						table+="<body>"
							for (var i = 3; i < topThreeAll.length+3; i++) {
									var color="#FFFFFF" ;
									if(topThreeAll[i-3].jyjg=="阳性"){
										color="#FFE1FF";
									}else if(topThreeAll[i-3].jyjg=="阴性"){
										color="#F0F8FF	";
									}else if(topThreeAll[i-3].jyjg=="疑似"){
										color="#EEEED1";
									}
									var str=topThreeAll[i-3].num/topThreeAll[i-3].sum;
									if(!str){
										str=0;
									}
									var ratio=Number(str*100).toFixed(2)+"%";
									table+="<tr>";
									if(i%3==0){
										table+="<td class='yblx_td' rowspan='3'>"+topThreeAll[i-3].yblx+"</td>";
									}
									table+="<td class='other_td' style='background-color:"+color+"'>"+topThreeAll[i-3].jyjg+"</td>";
									table+="<td class='other_td' style='background-color:"+color+"'>"+topThreeAll[i-3].num+"</td>";
									table+="<td class='other_td' style='background-color:"+color+"'>"+ratio+"</td>";
									table+="</tr>";
							}
						table+="</body>"
					table+="</table>"
				}
			$("#weeklyStatis_wxyh #tjsj_jyjgOfYblx").html("");
			$("#weeklyStatis_wxyh #jyjgOfYblx").html("");
			$("#weeklyStatis_wxyh #tjsj_jyjgOfYblx").append(searchData.zqs.jyjgOfYblx);
			$("#weeklyStatis_wxyh #jyjgOfYblx").append(table);
			}
		},{
			id:"ksList",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var datalist=new Array();
					for(var i=0;i<data.length;i++){
						datalist.push({value:data[i].num,name:data[i].dwmc});
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.ksList,
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
							type : 'pie',
							radius : ['0%','50%'],
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
		},{
			id:"sjdwOfsjysOfks",
			chart:null,
			el:null,
			render:function(data,searchData){
				var table="<table id='tab2'>";
						table+="<thead>";
							table+="<tr>";
								table+="<th class='first_td' >合作对象</th>";
								table+="<th>上周合作</th>";
								table+="<th>本周合作</th>";
								table+="<th>同比增长</th>";
							table+="</tr>";
						table+="</thead>";
					for (var i = 0; i < data.length; i++) {
						table+="<tbody>"
							table+="<tr>";
								table+="<td class='first_td'>"+data[i].hzdw+"</td>";
								table+="<td>"+data[i].before+"</td>"
								table+="<td>"+data[i].now+"</td>"
								table+="<td>"+data[i].more+"</td>"
							table+="</tr>";
						table+="</tbody>"
					}
				table+="</table>";
			$("#weeklyStatis_wxyh #tjsj_sjdwOfsjysOfks").append(searchData.zqs.sjdwOfsjysOfks)
			$("#weeklyStatis_wxyh #sjdwOfsjysOfks").append(table);
			}
		},{
			id:"dbtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					var dataAxis= new Array();
					var dataseries=new Array();
					for (var i = 0; i < data.length; i++) {
						  dataAxis.push(data[i].mc);
						  dataseries.push(data[i].num);
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.dbtj,
							x : 'left',
							y : 10
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: '3%',
							containLabel: true
						},
						xAxis : [
							{
								type : 'category',
								data:dataAxis,
								axisLabel: {
									rotate: -45,
								},
								axisTick: {
									alignWithLabel: true
								},
								axisLine: {
									lineStyle: {
										  color: '#000000'
									},
								},
							}
						],
						yAxis : [
							{
								type : 'value',
								axisLine: {
									lineStyle: {
										  color: '#000000'
									},
								}
							}
						],
						series : [
							{
								name:'送检数量',
								type:'bar',
								barWidth: '70%',
								data:dataseries,
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
								}
							}
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
						var map ={}
						map["method"]="getSjdwBydb";
						map["db"]=param.name;
						map["jsrqstart"]=searchData.jsrqstart;
						map["jsrqend"]=searchData.jsrqend;
						clickLeadEcharts(_renderArr,'/wechat/statistics/getWeekLeadStatisByrq',map,'dbtj');
					});
				}
			}
		},{
			id:"fjsq",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].lx=="ADDJCXM_TYPE"){
							seriesData.push({value:data[i].count,name:'加测申请',flg:'ADDJCXM_TYPE',xh:data[i].xh});
						}else if(data[i].lx=="RECHECK_TYPE"){
							seriesData.push({value:data[i].count,name:data[i].xh+'次复测',flg:'RECHECK_TYPE',xh:data[i].xh});
						}
					}
				}
				var pieoption = {
					    title : {
					    	subtext : searchData.zqs.fjsq,
							x : 'left'
					    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}: {c} ({d}%)"
					    },
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'30',
					        data: ['加测申请','1次复测','2次复测']
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
					            		  formatter:"{b}: {c}(个)"
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
					/*var load_flg=$("#weeklyStatis #load_flg").val();
					var url="/ws/statistics/week_PageList_Fjsq?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&lrsjstart="+searchData.jsrqstart+"&lrsjend="+searchData.jsrqend+"&lrsjMstart="+searchData.jsrqMstart+"&lrsjMend="+searchData.jsrqMend+"&lrsjYstart="+searchData.jsrqYstart+"&lrsjYend="+searchData.jsrqYend+"&load_flg="+load_flg;
					if(load_flg=="0"){
						dis_toForward(url);
					}else if(load_flg=="1"){
						$("#weeklyStatis").load(url);
					}*/
				});
			}
		},{
			id:"fqyb",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				var datas=new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						datas.push(data[i].ybzt);
						seriesData.push({value:data[i].count,name:data[i].ybzt,zt:data[i].zt});
					}
				}
				var pieoption = {
					    title : {
					    	subtext : searchData.zqs.fqyb,
							x : 'left'
					    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}: {c} ({d}%)"
					    },
					    color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
							data:datas,
							textStyle: {
								color: '#3E9EE1'
							},
							y:35
						},
					    series : [
					        {
					            name: '',
					            type: 'pie',
					            radius : '45%',
					            center: ['48%', '60%'],
					            data:seriesData,
					            label: {
					            	normal: {
					            		  formatter:"{b}: {c}(个)"
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
					/*var load_flg=$("#weeklyStatis #load_flg").val();
					var url="/ws/statistics/week_PageList_Fqyb?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&jsrqstart="+searchData.jsrqstart+"&jsrqend="+searchData.jsrqend+"&jsrqMstart="+searchData.jsrqMstart+"&jsrqMend="+searchData.jsrqMend+"&jsrqYstart="+searchData.jsrqYstart+"&jsrqYend="+searchData.jsrqYend+"&load_flg="+load_flg;
					if(load_flg=="0"){
						dis_toForward(url);
					}else if(load_flg=="1"){
						$("#weeklyStatis").load(url);
					}*/
				});
			}
		},{
			id:"rqtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					var dataxAxis= new Array();
					var dataseries=new Array();
					var databgseries=new Array();
					var datayxlseries=new Array();
					var maxY=0;
					var interval;
					var intervalyxl;
					for (var i = 0; i < data.length; i++) {
						var rq;
						if(data[i].rq.length >=10)
							rq = data[i].rq.substr(5)
						else
							rq = data[i].rq
						dataxAxis.push(rq);
						dataseries.push(data[i].num);
						databgseries.push(data[i].sjbg);
						datayxlseries.push(parseInt(data[i].yxl)*100);
						if(maxY < parseInt(data[i].num))
							maxY = parseInt(data[i].num)
						if(maxY < parseInt(data[i].sjbg))
							maxY = parseInt(data[i].sjbg)
					}
					
					maxY= parseInt(maxY) + 5 - parseInt(maxY%5)
					interval=maxY/5
					
					var pieoption = {
						 title : {
						    	subtext : searchData.zqs.rqtj,
								x : 'left'
						    },
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						legend: {
							data:['送检数量','报告数量'],
							textStyle: {
								color: '#3E9EE1'
							},
							y:30
						},
						grid: {
							top:80,
							right: 50
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
								name:'送检数量',
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
							},
							{
								name:'报告数量',
								type:'line',
								data:databgseries,
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
		},{
			id:"bgjgtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var xAxis= new Array();
					var yAxis= new Array();
					var sum =0;
					for (var i = 0; i < data.length; i++) {
						yAxis.push(data[i].lx);
						xAxis.push(data[i].num);
						sum += data[i].num;
					}
					var spirit = 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAADYElEQVR4nO2dz0sUYRjHP7tIdAmxQ1LdlhCKMohAIsgiyEuHjkUEFQTlpejS/xCCBB06RBGBBKIG4cGyH0qHBKE9eKyFqBQPRQeNCt06vGNY7bq7szPfeZLnAwuzM+/zgw/DDvMu70wOIVveLscJOwycA44A24CfwAfgKXAbeFVvovlC/o/vuVwuTj+x0FWiYdGbgXvA8RrjHgAXgIVaCbMU3SKr1BhtwEtgZx1jTwI7gG7ga5pNNUO+9pBMuEN9klfYD9xMqZdEsCj6AHAiRtxZYFeyrSSHRdGnYsblCD8jJrEoek8TsbsT6yJhLIrelFFsqlgUPZtRbKpYFP2kidjxxLpIGIuiB4AvMeLmgJGEe0kMi6I/AVdjxPVSx91hVlgUDXAXuEaY16jFMnAJeJhqR01iVTTAdeAYUFxjzBRwCLgl6agJrM51rDAO7AP2EmbxthPO8vfAc2Ams84axLpoCGKLrH1mm8eC6KPAGaAL2Fpj7AZgY7T9DfhRY/wc4eflPmH+OjOynI8uEGbpukXlJ4Dz84V8aWWHcj46q4thFzCNTjJRren2UrlLWPM3WYjuAMYIk/tq2oCx9lK5Q11YLboFGARaxXVX0woMtpfK0uuTWvRFoFNcsxKdhF5kqEX3iuuthbQXtehG/gdMG2kvlm/B1xUuWoSLFmFF9CRwg2TnM4pRzskEc8bGiugR4ArhNjkpJqKcJv51sSJ63eOiRbhoES5ahIsW4aJFuGgRLlqEixbhokW4aBEuWoSLFuGiRbhoES5ahIsW4aJFuGgRLlqEWvTHKvs/p1izWu5qvaSCWvTlCvtmgeEUaw5TeUVtpV5SQy16COgBRoHXhMWb3aS7PnAhqjEQ1RwFeuYL+aEUa/5DFmtYHkefOEwQVmcBvKD+FQNvgNN/P+pHiV8MRbhoES5ahIsW4aJFuGgRLlqEixbhokW4aBEuWoSLFuGiRbhoES5ahIsW4aJFuGgRLlqEixbhokVYEx3nudGKXE1jTfS6xUWLcNEiXLQIFy3CRYtw0SJctAgXLcJFi3DRIv430eUq2+axJvp7jePPqmzHySXFmuhHwFKVYzNA/6rv/VR/s9BSlMsM1kTPEN4DPkU4I8vAO6APOAgsrhq7GO3ri8aUo5ipKIep1zv9AtipgOACGIrLAAAAAElFTkSuQmCC';
	
					var pieoption = {
						title : {
							subtext : searchData.zqs.bgjgtj,
							x : 'left',
							y : 10
						},
						tooltip: {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'cross'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						xAxis: {
							max: sum,
							splitLine: {show: false},
							axisLine: {
								lineStyle: {
									color: '#999'
								}
							},
							axisLabel: {
								margin: 10
							}
						},
						yAxis: {
							data: yAxis,
							inverse: true,
							axisTick: {show: false},
							axisLine: {show: false},
							axisLabel: {
								margin: 0,
								textStyle: {
									color: '#999',
									fontSize: 16
								}
							}
						},
						grid: {
							top: 'center',
							height: 180,
							right: 70
						},
						series: [{
							// current data
							type: 'pictorialBar',
							symbol: spirit,
							symbolRepeat: 'fixed',
							symbolMargin: '5%',
							symbolClip: true,
							symbolSize: 30,
							symbolBoundingData: sum,
							data: xAxis,
							markLine: {
								symbol: 'none',
								label: {
									normal: {
										formatter: 'max: {c}',
										position: 'start'
									}
								},
								lineStyle: {
									normal: {
										color: 'green',
										type: 'dotted',
										opacity: 0.2,
										width: 2
									}
								},
							},
							z: 10
						}, {
							// full data
							type: 'pictorialBar',
							itemStyle: {
								normal: {
									opacity: 0.2
								}
							},
							label: {
								normal: {
									show: true,
									formatter: function (params) {
										return (params.value / sum * 100).toFixed(0) + ' %';
									},
									position: 'right',
									offset: [0, 0],
									textStyle: {
										color: 'green',
										fontSize: 18
									}
								}
							},
							animationDuration: 0,
							symbolRepeat: 'fixed',
							symbolMargin: '5%',
							symbol: spirit,
							symbolSize: 30,
							symbolBoundingData: sum,
							data: xAxis,
							z: 5
						}]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
						/*var load_flg=$("#weeklyStatis #load_flg").val();
						var url="/ws/statistics/positive_PageList?lxmc="+param.name+"&bgrqstart="+searchData.jsrqstart+"&bgrqend="+searchData.jsrqend+"&bgrqMstart="+searchData.jsrqMstart+"&bgrqMend="+searchData.jsrqMend+"&bgrqYstart="+searchData.jsrqYstart+"&bgrqYend="+searchData.jsrqYend+"&load_flg="+load_flg;
						if(load_flg=="0"){
							dis_toForward(url);
						}else if(load_flg=="1"){
							$("#weeklyStatis").load(url);
						}*/
					});
				}
			}
		},{
			id:"weektj",
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
					for (var i = 0; i < data.length; i++) {
						var rq;
						if(data[i].rq.length >=10)
							rq = data[i].rq.substr(5)
						else
							rq = data[i].rq
						if(i==0)
							startrq = rq;
						else if (i == (data.length)-1)
							endrq = rq;
						dataxAxis.push(rq);
						dataseries.push(data[i].num);
						if(maxY < data[i].num)
							maxY = data[i].num
						
					}
					
					maxY= maxY + 5 - maxY%5
					interval=maxY/5
					
					var pieoption = {
					    title : {
							subtext : searchData.zqs.bgjgtj,
							x : 'left',
							y : 10
						},
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						/*legend: {
							y:25
						},*/
						grid: {
							y:80
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
								name:'送检数量',
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
		}
	]
	
	for (var i = 0; i < flModels.length; i++) {
		_renderArr.push(flModels[i]);
	}
	
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
			var _url = "/wechat/statistics/getWeeklyModel";
			var param ={};
			param["jsrqstart"]= $("#weeklyStatis_wxyh #jsrqstart").val();
			param["jsrqend"]= $("#weeklyStatis_wxyh #jsrqend").val();
			param["yhid"]= $("#weeklyStatis_wxyh #yhid").val();
			var lrrys = $("#weeklyStatis_wxyh #lrrys").val();
			if(lrrys){
				lrrys = lrrys.substr(1); //删除第一个字符
				lrrys = lrrys.substr(0, lrrys.length-1);
				lrrys = lrrys.split(" ").join("");
			}
			param["lrrys"]= lrrys;
			param["sign"]= $("#weeklyStatis_wxyh #sign").val();
			param["zq"]= $("#weeklyStatis_wxyh [name=zq]").val();
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
							if(datas[_render.id]&&datas[_render.id]!="searchData"){
								if(datas[_render.id].length==0){
									$("#echarts_weekly_wxyh_"+_render.id).parent().hide();
								}
							}else {
								 $("#echarts_weekly_wxyh_"+_render.id).parent().hide();
							}
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
	var obj = $("#weeklyStatis_wxyh #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weeklyStatis_wxyh #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weeklyStatis_wxyh #"+otherid).removeClass("bechoosed");
		$("#weeklyStatis_wxyh #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weeklyStatis_wxyh #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_wxyh #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weeklyStatis_wxyh #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_wxyh #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weeklyStatis_wxyh #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_wxyh #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weeklyStatis_wxyh #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_wxyh #"+getid).addClass("bechoosed");
	}
}
function echartsBtnInit(_renderArr){
	var url="/wechat/statistics/getWeeklyModel_Condition";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jsrqstart"]= $("#weeklyStatis_wxyh #jsrqstart").val();
	map["jsrqend"]= $("#weeklyStatis_wxyh #jsrqend").val();
	map["yhid"]= $("#weeklyStatis_wxyh #yhid").val();
	var lrrys = $("#weeklyStatis_wxyh #lrrys").val();
	if(lrrys){
		lrrys = lrrys.substr(1); //删除第一个字符
		lrrys = lrrys.substr(0, lrrys.length-1);
		lrrys = lrrys.split(" ").join("");
	}
	map["lrrys"]= lrrys;
	//标本情况点击年
	$("#weeklyStatis_wxyh #tj1q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1q");
		map["method"]="getYbxxWeekly_year";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况点击周
	$("#weeklyStatis_wxyh #tj1w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1w");
		map["method"]="getYbxxWeekly_week";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况点击月
	$("#weeklyStatis_wxyh #tj1m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1m");
		map["method"]="getYbxxWeekly_mon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况点击天
	$("#weeklyStatis_wxyh #tj1d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1d");
		map["method"]="getYbxxWeekly_day";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本类型点击年
	$("#weeklyStatis_wxyh #tj2q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2q");
		map["method"]="getSfsf_year";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//标本类型点击周
	$("#weeklyStatis_wxyh #tj2w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2w");
		map["method"]="getSfsf_week";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//标本类型点击月
	$("#weeklyStatis_wxyh #tj2m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2m");
		map["method"]="getSfsf_mon";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//标本类型点击天
	$("#weeklyStatis_wxyh #tj2d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2d");
		map["method"]="getSfsf_day";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//测试数信息点击年
	$("#weeklyStatis_wxyh #tj3q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3q");
		map["method"]="getYbxxDR_year";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//测试数信息点击周
	$("#weeklyStatis_wxyh #tj3w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3w");
		map["method"]="getYbxxDR_week";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//测试数信息点击月
	$("#weeklyStatis_wxyh #tj3m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3m");
		map["method"]="getYbxxDR_mon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//测试数信息点击天
	$("#weeklyStatis_wxyh #tj3d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3d");
		map["method"]="getYbxxDR_day";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//收费测试数信息点击年
	$("#weeklyStatis_wxyh #tj4q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4q");
		map["method"]="getYbxxType_year";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费测试数信息点击周
	$("#weeklyStatis_wxyh #tj4w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4w");
		map["method"]="getYbxxType_week";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费测试数信息点击月
	$("#weeklyStatis_wxyh #tj4m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4m");
		map["method"]="getYbxxType_mon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费测试数信息点击天
	$("#weeklyStatis_wxyh #tj4d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4d");
		map["method"]="getYbxxType_day";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//标本阳性率	
	$("#weeklyStatis_wxyh #tj5q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5q");
		map["method"]="getJyjgWeekly_year";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//标本阳性率
	$("#weeklyStatis_wxyh #tj5w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5w");
		map["method"]="getJyjgWeekly_week";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//标本阳性率
	$("#weeklyStatis_wxyh #tj5m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5m");
		map["method"]="getJyjgWeekly_mon";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//标本阳性率
	$("#weeklyStatis_wxyh #tj5d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5d");
		map["method"]="getJyjgWeekly_day";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//送检科室
	$("#weeklyStatis_wxyh #tj6q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6q");
		map["method"]="getKsByWeekly_year";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//送检科室
	$("#weeklyStatis_wxyh #tj6w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6w");
		map["method"]="getKsByWeekly_week";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//送检科室
	$("#weeklyStatis_wxyh #tj6m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6m");
		map["method"]="getKsByWeekly_mon";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//送检科室
	$("#weeklyStatis_wxyh #tj6d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6d");
		map["method"]="getKsByWeekly_day";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//送检排名
	$("#weeklyStatis_wxyh #tj7q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj7q");
		map["method"]="getSjhb_year";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//送检排名
	$("#weeklyStatis_wxyh #tj7w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj7w");
		map["method"]="getSjhb_week";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//送检排名
	$("#weeklyStatis_wxyh #tj7m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj7m");
		map["method"]="getSjhb_mon";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//送检排名
	$("#weeklyStatis_wxyh #tj7d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj7d");
		map["method"]="getSjhb_day";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//复检情况
	$("#weeklyStatis_wxyh #tj8q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8q");
		map["method"]="getFjsqDaily_year";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况
	$("#weeklyStatis_wxyh #tj8w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8w");
		map["method"]="getFjsqDaily_week";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况
	$("#weeklyStatis_wxyh #tj8m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8m");
		map["method"]="getFjsqDaily_mon";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况
	$("#weeklyStatis_wxyh #tj8d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8d");
		map["method"]="getFjsqDaily_day";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//废弃标本
	$("#weeklyStatis_wxyh #tj9q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj9q");
		map["method"]="getFqybByYbztDaily_year";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本
	$("#weeklyStatis_wxyh #tj9w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj9w");
		map["method"]="getFqybByYbztDaily_week";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本
	$("#weeklyStatis_wxyh #tj9m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj9m");
		map["method"]="getFqybByYbztDaily_mon";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本
	$("#weeklyStatis_wxyh #tj9d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj9d");
		map["method"]="getFqybByYbztDaily_day";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//分类的所有统计按钮
	$("#weeklyStatis_wxyh a[name='tj10q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByYear";
		map["sfflg"]= $("#weeklyStatis_wxyh #sfflg_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_wxyh #zq_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_wxyh #zfl_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_wxyh #hbmc_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm"));
	})
	//分类的月统计按钮
	$("#weeklyStatis_wxyh a[name='tj10m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByMon";
		map["sfflg"]= $("#weeklyStatis_wxyh #sfflg_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_wxyh #zq_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_wxyh #zfl_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_wxyh #hbmc_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm"));
	})
	//分类的周统计按钮
	$("#weeklyStatis_wxyh a[name='tj10w']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByWeek";
		map["sfflg"]= $("#weeklyStatis_wxyh #sfflg_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_wxyh #zq_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_wxyh #zfl_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_wxyh #hbmc_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm"));
	})
	//分类的日统计按钮
	$("#weeklyStatis_wxyh a[name='tj10d']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByDay";
		map["sfflg"]= $("#weeklyStatis_wxyh #sfflg_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_wxyh #zq_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_wxyh #zfl_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_wxyh #hbmc_"+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm"));
	})
	
	//子类别下拉框改变事件
	$("#weeklyStatis_wxyh select[name='zfl']").unbind("change").change(function(e){
		var zflid = e.currentTarget.id;
		var flid = zflid.split("_")[1];
		var sel_hbmc = $("#weeklyStatis_wxyh #hbmc_"+flid);
		var zfl = e.currentTarget.value;
		$.ajax({ 
			type:'post',  
			url:"/wechat/statistics/getWeeklyPartner_Hbqx", 
			cache: false,
			data: {"zfl":zfl,"fl":flid,"access_token":$("#ac_tk").val(),"xtyhid":$("#weeklyStatis_wxyh #yhid").val()},  
			dataType:'json', 
			success:function(data){
				if(data.sjhbxxDtos != null && data.sjhbxxDtos.length != 0){
					var hbHtml = "";
					hbHtml += "<option value=''>全部</option>";
					$.each(data.sjhbxxDtos,function(i){   		
						hbHtml += "<option value='" + data.sjhbxxDtos[i].hbmc + "'>" + data.sjhbxxDtos[i].hbmc + "</option>";
					});
					sel_hbmc.empty();
					sel_hbmc.append(hbHtml);
					sel_hbmc.trigger("chosen:updated");
				}else{
					var hbHtml = "";
					hbHtml += "<option value=''>全部</option>";
					sel_hbmc.empty();
					sel_hbmc.append(hbHtml);
					sel_hbmc.trigger("chosen:updated");
				}
			}
		});
		
		var statisTitle = $("#weeklyStatis_wxyh #ybxxhead_"+flid+" .bechoosed").attr("title");
		var zflmap ={}
		if(statisTitle=="年"){
			zflmap["method"]="getFlByYear";
		}else if(statisTitle=="月"){
			zflmap["method"]="getFlByMon";
		}else if(statisTitle=="周"){
			zflmap["method"]="getFlByWeek";
		}else if(statisTitle=="日"){
			zflmap["method"]="getFlByDay";
		}
		zflmap["access_token"]=$("#ac_tk").val();
		zflmap["jsrqstart"]= $("#weeklyStatis_wxyh #jsrqstart").val()
		zflmap["jsrqend"]= $("#weeklyStatis_wxyh #jsrqend").val()
		zflmap["zq"]= $("#weeklyStatis_wxyh #zq_" + flid).val()
		zflmap["sfflg"]= $("#weeklyStatis_wxyh #sfflg_" + flid).val()
		zflmap["hbfl"]= flid
		zflmap["hbzfl"]= $("#weeklyStatis_wxyh #zfl_" + flid).val()
		zflmap["db"]= $("#weeklyStatis_wxyh #hbmc_" + flid).val()
		zflmap["yhid"]= $("#weeklyStatis_wxyh #yhid").val()
		clickLeadEcharts(_renderArr,url,zflmap,'fltj_'+flid);
	});
	
	//周期改变事件
	$("#weeklyStatis_wxyh input[name='zq']").unbind("change").change(function(e){
		var re = new RegExp("^[0-9]*[1-9][0-9]*$");
	    if (e.currentTarget.value != "") {
	        if (!re.test(e.currentTarget.value)) {
	            $.alert("周期最小为1");
	            e.currentTarget.value = 1;
	            e.currentTarget.focus();
	        }
	    }else{
	    	e.currentTarget.value = 14;
        	e.currentTarget.focus();
	    }
		var flid = $("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("dm");
	    var dateid = "ybxxhead"+"_"+flid;
		var hz = "_"+ flid;
		var method = $("#"+$("#weeklyStatis_wxyh #"+dateid+" .bechoosed")[0].id).attr("method")
		map["method"]=method;
		map["zq"]= e.currentTarget.value;
		map["db"]= $("#weeklyStatis_wxyh #hbmc_"+flid).val();
		map["hbzfl"]= $("#weeklyStatis_wxyh #zfl_"+flid).val();
		map["hbfl"]= flid;
		map["sfflg"]= $("#weeklyStatis_wxyh #sfflg_"+flid).val()
		map["yhid"]= $("#weeklyStatis_wxyh #yhid").val()
		clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_wxyh #"+e.currentTarget.id).attr("tjm")+hz);
	});
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
//合作伙伴点击
$("#weeklyStatis_wxyh button[name='hzhb']").unbind("click").click(function(e){
	var hbid = e.currentTarget.id;
	var flid = hbid.split("_")[1];
	var hbzfl = $("#weeklyStatis_wxyh #zfl_" + flid).val();
	var lrrys = $("#weeklyStatis_wxyh #lrrys").val();
	if(lrrys){
		lrrys = lrrys.substr(1); //删除第一个字符
		lrrys = lrrys.substr(0, lrrys.length-1);
		lrrys = lrrys.split(" ").join("");
	}
	jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="view_url" value="/wechat/statistics/weeklyPartnerPage?fl='+flid+'&zfl='+hbzfl+'&yhid='+ $("#weeklyStatis_wxyh #yhid").val()+'&lrrys='+ lrrys+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
	return true;
});

$(function(){
	loadDomWeekly();
	//所有下拉框添加choose样式
	jQuery('#weeklyStatis .chosen-select').chosen({width: '100%'});
})