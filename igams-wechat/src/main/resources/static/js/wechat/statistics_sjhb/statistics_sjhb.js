//获取元素高度,解决栅格系统同行元素高度不统一的问题,由于元素在初始化可能还未加载完成，设置延时
function getheight(){
	setTimeout(function(){
		var height=$("#getheighthb").height();
		$(".toptitlehb").attr("height",height);
	},200)
}
//调整中间部分高度，实现地图部分内容随高度增加变高
function getcenterheight(){
	var pagewidth=$(window).width();
	//当屏幕宽度大于991px时执行
	if(pagewidth>991){
		setTimeout(function(){
			var centerheight=$("#centerbodyhb").height();
			var allheight=$("#allhb").height();
			var height=700+allheight-centerheight;
			$("#centermaphb").attr("style","height:"+height+"px;width:100%;margin-top:10px;padding:0px;");
		},200)
	}
}
//旋转动画和页面载入动画
function xzAndzr(){
	 $("#outactionhb").rotate({animateTo: 180,duration:2000});
	 $("#inactionhb").rotate({animateTo: -180,duration:2000});
	 $("#xuanzhuanhb").fadeOut(2000);
	 setTimeout(function(){
			$("#xuanzhuanhb").attr("style","display:none;");
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
			id:"yblxhb",
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
			id:"ggzdhb",
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
			id:"dbhb",
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
						clickSjxxEcharts(_renderArr,'/wechat/statisticsBySjhb/pagedataSjxxStatisByTj',map,'dbhb');
					});
				}
			}
		},
		{
			id:"jsrqhb",
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
			id:"ybqkhb",
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
			id:"possiblehb",
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
			id:"bgwcdhb",
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
		var _url = "/wechat/statisticsBySjhb/pagedataSjxxStatis";
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
	var url="/wechat/statisticsBySjhb/pagedataSjxxStatisByTj";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	//标本信息的所有统计按钮
	$("#sj_statishb #tj1qhb").unbind("click").click(function(){
		initEchartsBtnCss("tj1qhb");
		map["method"]="getYblxByYear";
		clickSjxxEcharts(_renderArr,url,map,'yblxhb');
	})
	//标本信息的月统计按钮
	$("#sj_statishb #tj1mhb").unbind("click").click(function(){
		initEchartsBtnCss("tj1mhb");
		map["method"]="getYblxByMon";
		clickSjxxEcharts(_renderArr,url,map,'yblxhb');
	})
	//标本信息的周统计按钮
	$("#sj_statishb #tj1whb").unbind("click").click(function(){
		initEchartsBtnCss("tj1whb");
		map["method"]="getYblxByWeek";
		clickSjxxEcharts(_renderArr,url,map,'yblxhb');
	})
	//标本信息的日统计按钮
	$("#sj_statishb #tj1dhb").unbind("click").click(function(){
		initEchartsBtnCss("tj1dhb");
		map["method"]="getYblxByDay";
		clickSjxxEcharts(_renderArr,url,map,'yblxhb');
	})
	//关注度的所有统计按钮
	$("#sj_statishb #tj2qhb").unbind("click").click(function(){
		initEchartsBtnCss("tj2qhb");
		map["method"]="getGgzdByYear";
		clickSjxxEcharts(_renderArr,url,map,'ggzdhb');
	})
	//关注度的月统计按钮
	$("#sj_statishb #tj2mhb").unbind("click").click(function(){
		initEchartsBtnCss("tj2mhb");
		map["method"]="getGgzdByMon";
		clickSjxxEcharts(_renderArr,url,map,'ggzdhb');
	})
	//关注度的周统计按钮
	$("#sj_statishb #tj2whb").unbind("click").click(function(){
		initEchartsBtnCss("tj2whb");
		map["method"]="getGgzdByWeek";
		clickSjxxEcharts(_renderArr,url,map,'ggzdhb');
	})
	//合作伙伴的所有统计按钮
	$("#sj_statishb #tj3qhb").unbind("click").click(function(){
		initEchartsBtnCss("tj3qhb");
		map["method"]="getDbByYear";
		map["tj"]="mon";
		clickSjxxEcharts(_renderArr,url,map,'dbhb');
	})
	//合作伙伴的月统计按钮
	$("#sj_statishb #tj3mhb").unbind("click").click(function(){
		initEchartsBtnCss("tj3mhb");
		map["method"]="getDbByMon";
		clickSjxxEcharts(_renderArr,url,map,'dbhb');
		
	})
	//合作伙伴的周统计按钮
	$("#sj_statishb #tj3whb").unbind("click").click(function(){
		initEchartsBtnCss("tj3whb");
		map["method"]="getDbByWeek";
		clickSjxxEcharts(_renderArr,url,map,'dbhb');
	})
	//合作伙伴的日统计按钮
	$("#sj_statishb #tj3dhb").unbind("click").click(function(){
		initEchartsBtnCss("tj3dhb");
		map["method"]="getDbByDay";
		clickSjxxEcharts(_renderArr,url,map,'dbhb');
	})
	
	//送样-报告-阳性率的所有统计按钮
	$("#sj_statishb #tj4qhb").unbind("click").click(function(){
		initEchartsBtnCss("tj4qhb");
		map["method"]="getJsrqByYear";
		clickSjxxEcharts(_renderArr,url,map,'jsrqhb');
	})
	//送样-报告-阳性率的月统计按钮
	$("#sj_statishb #tj4mhb").unbind("click").click(function(){
		initEchartsBtnCss("tj4mhb");
		map["method"]="getJsrqByMon";
		clickSjxxEcharts(_renderArr,url,map,'jsrqhb');
	})
	//送样-报告-阳性率的周统计按钮
	$("#sj_statishb #tj4whb").unbind("click").click(function(){
		initEchartsBtnCss("tj4whb");
		map["method"]="getJsrqByWeek";
		clickSjxxEcharts(_renderArr,url,map,'jsrqhb');
	})
	
	//标本情况的年统计按钮
	$("#sj_statishb #tj5qhb").unbind("click").click(function(){
		initEchartsBtnCss("tj5qhb");
		map["method"]="getSjxxYearBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqkhb');
	})
	//标本情况的月统计按钮
	$("#sj_statishb #tj5mhb").unbind("click").click(function(){
		initEchartsBtnCss("tj5mhb");
		map["method"]="getSjxxMonBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqkhb');
	})
	//标本情况的周统计按钮
	$("#sj_statishb #tj5whb").unbind("click").click(function(){
		initEchartsBtnCss("tj5whb");
		map["method"]="getSjxxWeekBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqkhb');
	})
	//标本情况的周统计按钮
	$("#sj_statishb #tj5dhb").unbind("click").click(function(){
		initEchartsBtnCss("tj5dhb");
		map["method"]="getSjxxDayBySy";
		clickSjxxEcharts(_renderArr,url,map,'ybqkhb');
	})
	
	//检验结果的所有统计按钮
	$("#sj_statishb #tj6qhb").unbind("click").click(function(){
		initEchartsBtnCss("tj6qhb");
		map["method"]="getPossibleByYear";
		clickSjxxEcharts(_renderArr,url,map,'possiblehb');	
	})
	//检验结果的月统计按钮
	$("#sj_statishb #tj6mhb").unbind("click").click(function(){
		initEchartsBtnCss("tj6mhb");
		map["method"]="getPossibleByMon";
		clickSjxxEcharts(_renderArr,url,map,'possiblehb');})
	//检验结果的周统计按钮
	$("#sj_statishb #tj6whb").unbind("click").click(function(){
		initEchartsBtnCss("tj6whb");
		map["method"]="getPossibleByWeek";
		clickSjxxEcharts(_renderArr,url,map,'possiblehb');})
}

//设置按钮样式
function initEchartsBtnCss(getid,_renderArr){
	var obj = $("#sj_statishb #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#sj_statishb #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#sj_statishb #"+otherid).removeClass("bechoosed");
		$("#sj_statishb #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="全"){
		$("#sj_statishb #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statishb #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#sj_statishb #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statishb #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#sj_statishb #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statishb #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#sj_statishb #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#sj_statishb #"+getid).addClass("bechoosed");
	}
}

$("#ybxxheadhb a").hover(function(){
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
	    url:"/wechat/statisticsBySjhb/pagedataDtData",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	$("#sj_statishb #jrybshb").text(data.dtybsl.num);
	    	$("#sj_statishb #jrbgshb").text(data.dtbgsl.num);
	    	$("#sj_statishb #jrlrybshb").text(data.dtlrybsl.num);
	    	$("#sj_statishb #jrsyshb").text(data.symap.num);
	    	$("#sj_statishb #jrggshb").text("0");
	    	$("#sj_statishb #jrdgshb").text("0");
	    	if(data.dtggdgsl.length>0){
		    	for(var i=0;i<data.dtggdgsl.length;i++){
		    		if(data.dtggdgsl[i].lx=="阳性"){
		    			$("#sj_statishb #jrggshb").text(data.dtggdgsl[i].num);
		    		}
		    		if(data.dtggdgsl[i].lx=="疑似"){
		    			$("#sj_statishb #jrdgshb").text(data.dtggdgsl[i].num);
		    		}
		    	}
	    	}
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
	}, 600000);
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

$(function(){
	loadSjxxStatis();
	settime();
	initpage();
	getheight();
	xzAndzr();
	//getcenterheight();
});