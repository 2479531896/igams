//获取元素高度,解决栅格系统同行元素高度不统一的问题,由于元素在初始化可能还未加载完成，设置延时
function getheight(){
	setTimeout(function(){
		var height=$("#getheight").height();
		$(".toptitle").attr("height",height);
	},200)
}
//调整中间部分高度，实现地图部分内容随高度增加变高
function getcenterheight(){
	var pagewidth=$(window).width();
	//当屏幕宽度大于991px时执行
	if(pagewidth>991){
		setTimeout(function(){
			var centerheight=$("#centerbody").height();
			var allheight=$("#all").height();
			var height=700+allheight-centerheight;
			$("#centermap").attr("style","height:"+height+"px;width:100%;margin-top:10px;padding:0px;");
		},200)
	}
}
//旋转动画和页面载入动画
function xzAndzr(){
	 $("#outaction").rotate({animateTo: 180,duration:2000});
	 $("#inaction").rotate({animateTo: -180,duration:2000});
	 $("#xuanzhuan").fadeOut(2000);
	 setTimeout(function(){
			$("#xuanzhuan").attr("style","display:none;");
		},2000)
}
//加载统计数据
var loadSjxxStatis = function(){
	var _eventTag = "SjxxLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_sjxx_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"yblx",
			chart: null,
			el: null,
			render:function(datas,searchData){
				var sum = 0;//合计
				var prekyfx = "";
				if(datas&&this.chart){
					var datalist = [];
					for (var i = 0; i < datas.length; i++) {
						datalist.push({value:datas[i].cn,name:datas[i].csmc})
						sum+=datas[i].cn;
					}
					var pieoption = {
							title : {
								text : '标本数：'+sum + '个',
								subtext : searchData.zqs.yblx,
								x : 'left',
								y : 10
							},
						    tooltip : {
						        trigger: 'item',
						        formatter: "{c} ({d}%)"
						    },
						    toolbox: {
						        show : true,
						        feature : {
						            magicType : {
						                show: true,
						                type: ['pie', 'funnel']
						            },
						        }
						    },
						    calculable : true,
						    series : [
						        {
						            name:'半径模式',
						            type:'pie',
						            radius : [20, 70],
						            center : ['50%', '50%'],
						            roseType : 'radius',
						            label: {
						                normal: {
						                    show: true
						                },
						                emphasis: {
						                    show: true
						                }
						            },
						            lableLine: {
						                normal: {
						                    show: false
						                },
						                emphasis: {
						                    show: true
						                }
						            },
						            data:datalist,
						            startAngle: -75,
						        },
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
			id:"ggzd",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					var dataAxis= new Array();
					var dataseries=new Array();
					var preWzfl = "";
					var preWzflNum = 0;
					for (var i = 0; i < data.length; i++) {
						dataAxis.push({value:data[i].wznum,name:data[i].wzzwm})
						if(preWzfl != data[i].wzfl){
							if(preWzfl != "")
								dataseries.push({value: preWzflNum,name:preWzfl});
							preWzflNum = parseInt(data[i].wznum);
							preWzfl = data[i].wzfl;
						}else{
							preWzflNum += parseInt(data[i].wznum);
						}
					}
					if(data.length > 0)
						dataseries.push({value: preWzflNum,name:preWzfl});
					var pieoption = {
							title : {
								subtext : searchData.zqs.ggzd,
								x : 'left'
							},
						    tooltip: {
						        trigger: 'item',
						        formatter: "{b}: {c} ({d}%)"
						    },
						    series: [
						        {
						            name:'类型',
						            type:'pie',
						            selectedMode: 'single',
						            radius: [0, '39%'],

						            label: {
						                normal: {
						                    position: 'inner'
						                }
						            },
						            labelLine: {
						                normal: {
						                    show: false
						                }
						            },
						            data:dataseries
						        },
						        {
						            type:'pie',
						            radius: ['40%', '55%'],
						            label: {
						                normal: {
						                    formatter: '{b}: {c}',
						                    borderWidth: 1,
						                    borderRadius: 4,
						                    rich: {
						                        a: {
						                            color: '#999',
						                            lineHeight: 22,
						                            align: 'center'
						                        },
						                        hr: {
						                            borderColor: '#aaa',
						                            width: '100%',
						                            borderWidth: 0.5,
						                            height: 0
						                        },
						                        b: {
						                            fontSize: 16,
						                            lineHeight: 33
						                        },
						                        per: {
						                            color: '#eee',
						                            backgroundColor: '#334455',//背景颜色
						                            padding: [2, 4],
						                            borderRadius: 2
						                        }
						                    }
						                }
						            },
						            data:dataAxis
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
			id:"db",
			chart: null,
			el: null,
			render:function(data,searchData){
				var total = [];
				var prekyfx = "";
				if(data&&this.chart){
					var dataxAxis= new Array();
					var dataseries=new Array();
					for (var i = 0; i < data.length; i++) {
						dataxAxis.push(data[i].mc);
						dataseries.push(data[i].num)
					}
					/*var color="#"+data[0].color;*/
					var pieoption = {
						    color: ['#3398DB'],
						    title : {
								subtext : searchData.zqs.db,
								x : 'left'
							},
						    tooltip : {
						        trigger: 'axis',
						        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
						            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
						        }
						    },
						    grid: {
						        left: '3%',
						        right: '4%',
						        bottom: '3%',
						        containLabel: true,
						        top:'30%',
						    },
						    xAxis : [
						        {
						            type : 'category',
						            data : dataxAxis,
						            axisLabel: {
						                rotate: -60,
						            },
						            axisTick: {
						                alignWithLabel: true
						            },
						            axisLine: {
						                lineStyle: {
						                	  color: '#FFFFFF'
						                },
						            },
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						            axisLine: {
						                lineStyle: {
						                	  color: '#FFFFFF'
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
											/*color:color,*/
											label: {
												show: true, //开启显示
												position: 'top', //在上方显示
												textStyle: { //数值样式
													color: 'white',
													fontSize: 12
												}
											}
										}
						            },
						        }
						    ]
						};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
						var map ={}
						map["access_token"]=$("#ac_tk").val();
						map["method"]="getSjdwbyDb";
						map["db"]=param.name;
						map["jsrqstart"]=searchData.jsrqstart;
						map["jsrqend"]=searchData.jsrqend;
					/*	map["color"]=param.color;*/
						clickSjxxEcharts(_renderArr,'/wechat/statistics/pagedataSjxxStatisByTj',map,'db');
					});
				}
			}
		},
		{
			id:"jsrq",
			chart: null,
			el: null,
			render:function(data,searchData){
				var total = [];
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
						if(data[i].rq.length>8){
							dataxAxis.push(data[i].rq.substring(5,10));
						}else{
							dataxAxis.push(data[i].rq);
						}
						dataseries.push(data[i].num);
						databgseries.push(data[i].sjbg);
						datayxlseries.push(parseFloat(data[i].yxl)*100);
						if(maxY < data[i].num)
							maxY = parseInt(data[i].num)
						if(maxY < data[i].sjbg)
							maxY = parseInt(data[i].sjbg)
					}
					maxY= parseInt(maxY) + 5 - parseInt(maxY%5)
					interval=maxY/5
					
					var pieoption= {
						title : {
							subtext : searchData.zqs.jsrq,
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
					        data:['标本数量','报告数量','阳性率'],
					        textStyle: {
					        	color: '#3E9EE1'
				            },
				            y:30,
				            selected:{
				            	'阳性率':false
				            }
					    },
					    grid: {
					        top:'80px',
					        height:160,
					        right:50
					    },
					    xAxis: [
					        {
					            type: 'category',
					            boundaryGap: false,
					            data: dataxAxis,
					        }
					    ],
					    yAxis: [
					        {	
					            type: 'value',
					            name: '数量',
					            min: 0,
					            max: maxY,
					            interval: interval,
					            axisLabel: {
					                formatter: '{value}'
					            }
					        },
					        {
					            type: 'value',
					            name: '阳性率',
					            min: 0,
					            max: 100,
					            interval: 20,
					            axisLabel: {
					                formatter: '{value} %'
					            }
					        }
					    ],
					    series: [
					        {
					            name:'标本数量',
					            type:'line',
					            itemStyle : { normal: {label : {show: true}}},
					            data:dataseries
					        },
					        {
					            name:'报告数量',
					            type:'line',
					            data:databgseries,
					           itemStyle : { normal: {label : {show: true}}}
					        },
					        {
					            name:'阳性率',
					            type:'line',
					            yAxisIndex: 1,
					            data:datayxlseries,
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
			id:"ybqk",
			chart: null,
			el: null,
			render:function(data,searchData){
				var total = [];
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
						tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    legend: {
					        data: ['收费标本', '不收费标本','废弃标本'],
					        textStyle: {
					        	color: '#3E9EE1'
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
					            name: '收费标本',
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
					            name: '废弃标本',
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
					            name: '不收费标本',
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
			id:"possible",
			chart: "macarons",
			el: null,
			render:function(data,searchData){
				var spirit = 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAYAAAA4qEECAAADYElEQVR4nO2dz0sUYRjHP7tIdAmxQ1LdlhCKMohAIsgiyEuHjkUEFQTlpejS/xCCBB06RBGBBKIG4cGyH0qHBKE9eKyFqBQPRQeNCt06vGNY7bq7szPfeZLnAwuzM+/zgw/DDvMu70wOIVveLscJOwycA44A24CfwAfgKXAbeFVvovlC/o/vuVwuTj+x0FWiYdGbgXvA8RrjHgAXgIVaCbMU3SKr1BhtwEtgZx1jTwI7gG7ga5pNNUO+9pBMuEN9klfYD9xMqZdEsCj6AHAiRtxZYFeyrSSHRdGnYsblCD8jJrEoek8TsbsT6yJhLIrelFFsqlgUPZtRbKpYFP2kidjxxLpIGIuiB4AvMeLmgJGEe0kMi6I/AVdjxPVSx91hVlgUDXAXuEaY16jFMnAJeJhqR01iVTTAdeAYUFxjzBRwCLgl6agJrM51rDAO7AP2EmbxthPO8vfAc2Ams84axLpoCGKLrH1mm8eC6KPAGaAL2Fpj7AZgY7T9DfhRY/wc4eflPmH+OjOynI8uEGbpukXlJ4Dz84V8aWWHcj46q4thFzCNTjJRren2UrlLWPM3WYjuAMYIk/tq2oCx9lK5Q11YLboFGARaxXVX0woMtpfK0uuTWvRFoFNcsxKdhF5kqEX3iuuthbQXtehG/gdMG2kvlm/B1xUuWoSLFmFF9CRwg2TnM4pRzskEc8bGiugR4ArhNjkpJqKcJv51sSJ63eOiRbhoES5ahIsW4aJFuGgRLlqEixbhokW4aBEuWoSLFuGiRbhoES5ahIsW4aJFuGgRLlqEWvTHKvs/p1izWu5qvaSCWvTlCvtmgeEUaw5TeUVtpV5SQy16COgBRoHXhMWb3aS7PnAhqjEQ1RwFeuYL+aEUa/5DFmtYHkefOEwQVmcBvKD+FQNvgNN/P+pHiV8MRbhoES5ahIsW4aJFuGgRLlqEixbhokW4aBEuWoSLFuGiRbhoES5ahIsW4aJFuGgRLlqEixbhokVYEx3nudGKXE1jTfS6xUWLcNEiXLQIFy3CRYtw0SJctAgXLcJFi3DRIv430eUq2+axJvp7jePPqmzHySXFmuhHwFKVYzNA/6rv/VR/s9BSlMsM1kTPEN4DPkU4I8vAO6APOAgsrhq7GO3ri8aUo5ipKIep1zv9AtipgOACGIrLAAAAAElFTkSuQmCC';
					var datayAxis=[];
					var dataseries=[];
					var maxData = 0;
				for (var i = 0; i < data.length; i++) {
					datayAxis.push(data[i].lx);
					dataseries.push(data[i].num);
					maxData += parseInt(data[i].num);
				}
				var labelset={
						normal: {
			                show: true,
			                formatter: function (params) {
			                    return (parseInt(params.value) / maxData * 100).toFixed(1) + ' %';
			                },
			                position: 'right',
			                offset: [10, 0],
			                textStyle: {
			                    color: 'green',
			                    fontSize: 18
			                }
			            }
				};
				var pieoption = {
						title : {
							subtext : searchData.zqs.possible,
							x : 'left'
						},
						tooltip: {
					    },
					    xAxis: {
					        max: maxData,
					        splitLine: {show: false},
					        offset: 10,
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
					        data:datayAxis,
					        inverse: true,
					        axisTick: {show: false},
					        axisLine: {show: false},
					        axisLabel: {
					            margin: 10,
					            textStyle: {
					                color: '#999',
					                fontSize: 16
					            }
					        }
					    },
					    grid: {
					        top: 'center',
					        height: 180,
					        right: 80,
					        left: 50
					    },
					    series: [{
					        // current data
					        type: 'pictorialBar',
					        symbol: spirit,
					        symbolRepeat: 'fixed',
					        symbolMargin: '5%',
					        symbolClip: true,
					        symbolSize: 30,
					        symbolBoundingData: maxData,
					        data: dataseries,
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
					            data: [{
					                type: 'max'
					            }]
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
					        label:labelset,
					        animationDuration: 0,
					        symbolRepeat: 'fixed',
					        symbolMargin: '5%',
					        symbol: spirit,
					        symbolSize: 30,
					        symbolBoundingData: maxData,
					        data: dataseries,
					        z: 5
					    }]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(){
					//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
				});
			}

		},{
			id:"bgwcd",
			chart: "macarons",
			el: null,
			render:function(data,searchData){
				var wcd;
				if(parseInt(data.sjxxqytybs.num)!=0){
					var wcd=(data.sjxxwad.num/data.sjxxqytybs.num*100).toFixed(1);
				}else{
					wcd=0;
				}
					var pieoption = {
						    tooltip : {
						        formatter: "{a} <br/>{b} : {c}%"
						    },
						    series: [
						        {
						            name: '送检报告',
						            type: 'gauge',
						            detail: {formatter:'{value}%'},
						            data: [{value: wcd}],
						            axisLine: {            // 坐标轴线  
					                     lineStyle: {       // 属性lineStyle控制线条样式  
					                         color: [[0.2, '#c23531'], [0.8, '#63869e'], [1, '#91c7ae']]
					                                }  
					                            },  
						            title : {               //设置仪表盘中间显示文字样式
				                        textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				                            fontWeight: 'bolder',
				                            fontSize: 10,
				                            fontStyle: 'italic',
				                            color:"white"
				                        }
				                    }
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

		},{
			id:"sjlqk",
			chart:null,
			el:null,
			render:function(data,searchData){
				var dlength = 0;
				var table ="<table class='tab'>"
			 	table+="<thead>"
			 		table+="<tr>"
			 			table+="<th class='width_2'>检测单位</th>"
			 			table+="<th class='width_1'>数据</th>"
			 			table+="<th class='width_3'>数据量(M)</th>"
		    	   table+="  </tr>"
		    	table+="</thead>"
		    	table+="<tbody>"
	    		if(data){
	    			dlength = data.length > 6?6:data.length;
	    			for (var i = 0; i < dlength; i++) {
	    				table+="<tr>"
		    				if(data[i].jcdwmc){
								table+=" <td class='width_2'>"+data[i].jcdwmc+"</td>"
							}else{
								table+=" <td class='width_2'>&nbsp;</td>"
							}
		    				if(data[i].sj){
								table+=" <td class='width_1'>"+data[i].sj+"</td>"
							}else{
								table+=" <td class='width_1'>&nbsp;</td>"
							}
		    				if(data[i].sjl){
								table+=" <td class='width_3'>"+data[i].sjl+"</td>"
							}else{
								table+=" <td class='width_3'>&nbsp;</td>"
							}
	    				table+=" </tr>"
					}
	    		}
				table+= "</tbody></table>"
				$("#sjlqklist").empty();
				$("#sjlqklist").append(table);
				$("#syssjl").html(dlength + "/" + data.length);
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
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = "/wechat/statistics/pagedataSjxxStatis";
		$.ajax({
			type : "post",
			url : _url,
			data:{"access_token":$("#ac_tk").val()},
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
//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/wechat/statistics/pagedataSjxxStatisByTj";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	//标本信息的所有统计按钮
	$("#sj_statis #tj1q").unbind("click").click(function(){
		initEchartsBtnCss("tj1q");
		map["method"]="getYblxByYear";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//标本信息的月统计按钮
	$("#sj_statis #tj1m").unbind("click").click(function(){
		initEchartsBtnCss("tj1m");
		map["method"]="getYblxByMon";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//标本信息的周统计按钮
	$("#sj_statis #tj1w").unbind("click").click(function(){
		initEchartsBtnCss("tj1w");
		map["method"]="getYblxByWeek";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//标本信息的日统计按钮
	$("#sj_statis #tj1d").unbind("click").click(function(){
		initEchartsBtnCss("tj1d");
		map["method"]="getYblxByDay";
		clickSjxxEcharts(_renderArr,url,map,'yblx');
	})
	//关注度的所有统计按钮
	$("#sj_statis #tj2q").unbind("click").click(function(){
		initEchartsBtnCss("tj2m");
		map["method"]="getGgzdByYear";
		clickSjxxEcharts(_renderArr,url,map,'ggzd');
	})
	//关注度的月统计按钮
	$("#sj_statis #tj2m").unbind("click").click(function(){
		initEchartsBtnCss("tj2m");
		map["method"]="getGgzdByMon";
		clickSjxxEcharts(_renderArr,url,map,'ggzd');
	})
	//关注度的周统计按钮
	$("#sj_statis #tj2w").unbind("click").click(function(){
		initEchartsBtnCss("tj2w");
		map["method"]="getGgzdByWeek";
		clickSjxxEcharts(_renderArr,url,map,'ggzd');
	})
	//合作伙伴的所有统计按钮
	$("#sj_statis #tj3q").unbind("click").click(function(){
		initEchartsBtnCss("tj3q");
		map["method"]="getDbByYear";
		map["tj"]="mon";
		clickSjxxEcharts(_renderArr,url,map,'db');
	})
	//合作伙伴的月统计按钮
	$("#sj_statis #tj3m").unbind("click").click(function(){
		initEchartsBtnCss("tj3m");
		map["method"]="getDbByMon";
		clickSjxxEcharts(_renderArr,url,map,'db');
		
	})
	//合作伙伴的周统计按钮
	$("#sj_statis #tj3w").unbind("click").click(function(){
		initEchartsBtnCss("tj3w");
		map["method"]="getDbByWeek";
		clickSjxxEcharts(_renderArr,url,map,'db');
	})
	//合作伙伴的日统计按钮
	$("#sj_statis #tj3d").unbind("click").click(function(){
		initEchartsBtnCss("tj3d");
		map["method"]="getDbByDay";
		clickSjxxEcharts(_renderArr,url,map,'db');
	})
	
	//送样-报告-阳性率的所有统计按钮
	$("#sj_statis #tj4q").unbind("click").click(function(){
		initEchartsBtnCss("tj4q");
		map["method"]="getJsrqByYear";
		clickSjxxEcharts(_renderArr,url,map,'jsrq');
	})
	//送样-报告-阳性率的月统计按钮
	$("#sj_statis #tj4m").unbind("click").click(function(){
		initEchartsBtnCss("tj4m");
		map["method"]="getJsrqByMon";
		clickSjxxEcharts(_renderArr,url,map,'jsrq');
	})
	//送样-报告-阳性率的周统计按钮
	$("#sj_statis #tj4w").unbind("click").click(function(){
		initEchartsBtnCss("tj4w");
		map["method"]="getJsrqByWeek";
		clickSjxxEcharts(_renderArr,url,map,'jsrq');
	})
	
	//标本情况的年统计按钮
	$("#sj_statis #tj5q").unbind("click").click(function(){
		initEchartsBtnCss("tj5q");
		map["method"]="getSjxxYearBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的月统计按钮
	$("#sj_statis #tj5m").unbind("click").click(function(){
		initEchartsBtnCss("tj5m");
		map["method"]="getSjxxMonBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的周统计按钮
	$("#sj_statis #tj5w").unbind("click").click(function(){
		initEchartsBtnCss("tj5w");
		map["method"]="getSjxxWeekBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的周统计按钮
	$("#sj_statis #tj5d").unbind("click").click(function(){
		initEchartsBtnCss("tj5d");
		map["method"]="getSjxxDayBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqk');
	})
	
	//检验结果的所有统计按钮
	$("#sj_statis #tj6q").unbind("click").click(function(){
		initEchartsBtnCss("tj6q");
		map["method"]="getPossibleByYear";
		clickSjxxEcharts(_renderArr,url,map,'possible');	
	})
	//检验结果的月统计按钮
	$("#sj_statis #tj6m").unbind("click").click(function(){
		initEchartsBtnCss("tj6m");
		map["method"]="getPossibleByMon";
		clickSjxxEcharts(_renderArr,url,map,'possible');})
	//检验结果的周统计按钮
	$("#sj_statis #tj6w").unbind("click").click(function(){
		initEchartsBtnCss("tj6w");
		map["method"]="getPossibleByWeek";
		clickSjxxEcharts(_renderArr,url,map,'possible');})
}

//设置按钮样式
function initEchartsBtnCss(getid,_renderArr){
	var obj = $("#sj_statis #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#sj_statis #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#sj_statis #"+otherid).removeClass("bechoosed");
		$("#sj_statis #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="全"){
		$("#sj_statis #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#sj_statis #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#sj_statis #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#sj_statis #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statis #"+getid).addClass("bechoosed");
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

function initpage(){
	$.ajax({ 
	    type:'post',  
	    url:"/wechat/statistics/pagedataListDtData",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	
	    	//返回值
	    	$("#sj_statis #jrybs").text(data.dtybsl.num);
	    	$("#sj_statis #jrbgs").text(data.dtbgsl.num);
	    	$("#sj_statis #jrlrybs").text(data.dtlrybsl.num);
	    	$("#sj_statis #jrsys").text(data.symap.num);
	    	$("#sj_statis #jrggs").text("0");
	    	$("#sj_statis #jrdgs").text("0");
	    	if(data.dtggdgsl.length>0){
		    	for(var i=0;i<data.dtggdgsl.length;i++){
		    		if(data.dtggdgsl[i].lx=="阳性"){
		    			$("#sj_statis #jrggs").text(data.dtggdgsl[i].num);
		    			var jrggsEnd=data.dtggdgsl[i].num;
		    		}
		    		if(data.dtggdgsl[i].lx=="疑似"){
		    			$("#sj_statis #jrdgs").text(data.dtggdgsl[i].num);
		    			var jrdgsEnd=data.dtggdgsl[i].num;
		    		}
		    	}
	    	}
	    	var jrybs={
	    		    el:$(".jrybsChange"),
	    		    start:$("#sj_statis #jrybs").text(),//初始值
	    		    end:data.dtybsl.num//结束值
	    		  };
	    	numChange(jrybs);
	    	
//	    	var jrbgs={
//	    		    el:$(".jrbgsChange"),
//	    		    start:$("#sj_statis #jrbgs").text(),//初始值
//	    		    end:data.dtbgsl.num//结束值
//	    		  };
//	    	numChange(jrbgs);
	    	
	    	var jrlrybs={
	    		    el:$(".jrlrybsChange"),
	    		    start:$("#sj_statis #jrlrybs").text(),//初始值
	    		    end:data.dtlrybsl.num//结束值
	    		  };
	    	numChange(jrlrybs);
	    	
	    	var jrsys={
	    		    el:$(".jrsysChange"),
	    		    start:$("#sj_statis #jrsys").text(),//初始值
	    		    end:data.symap.num//结束值
	    		  };
	    	numChange(jrsys);
	    	
	    	var jrggs={
	    		    el:$(".jrggsChange"),
	    		    start:$("#sj_statis #jrggs").text(),//初始值
	    		    end:jrggsEnd//结束值
	    		  };
	    	numChange(jrggs);
	    	
	    	var jrdgs={
	    		    el:$(".jrdgsChange"),
	    		    start:$("#sj_statis #jrdgs").text(),//初始值
	    		    end:jrggsEnd//结束值
	    		  };
	    	numChange(jrdgs);
	    }
	});
}
//定时更新统计数据
var num=0;
var time;
function settime(){
	time=setInterval(function () {
		loadSjxxStatis();
		initpage();
	}, 300000);
}
function cleartime(){
	clearInterval(time);
}
//监听窗口缩小或者切换选项卡时关闭定时器
document.addEventListener("visibilitychange", function(){
    if(document.hidden==false){
    	settime();
    }else{
    	cleartime();
    }
});


//点击各个统计图中的按钮的共通处理方法
function clickSjxxEcharts(_renderArr,_url,map,id){
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

//数字变动动画
function numChange(obj){
	var item=obj.el;
	var end=parseInt(obj.end);
	var start = parseInt(obj.start);
	var difference=Math.abs(end-start);
	var millisecond=8000;
	var num=1;
	var second=parseInt(millisecond/difference);
	if(start<end){
		time=setInterval(function(){
			start=start+num;
			if(start>end){
		        start=end;
		        clearInterval(time);
			}
			item.text(start)
		},
		second)
	}else if(start>end){
		time=setInterval(function(){
			start=start-num;
			if(start<end){
				start=end;
				clearInterval(time)
			}
			item.text(start)
		},
		second)
	}
}

$(function(){
	loadSjxxStatis();
	settime();
	initpage();
	getheight();
	xzAndzr();
	//getcenterheight();
});
