
var loadModelDaily=function(){
	var _eventTag = "dailyStatis_wxyh";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_daily_wxyh_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"ybxx",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							seriesData.push({value:data[i].num,name:'废弃'});
						}else if(data[i].sfsf=='0'){
							seriesData.push({value:data[i].num,name:'不收费'});
						}else if(data[i].sfsf=='1'){
							seriesData.push({value:data[i].num,name:'收费'});
						}
					}
				}
				var pieoption = {
					    title : {
					    	subtext : '统计时间:     '+searchData.jsrq,
							x : 'right'
					    },
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					    color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data: ['收费','不收费','废弃']
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
				this.chart.on("click",function(){
					//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
				});
			}
		},{
			id:"rybxx",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				var dataAxis= new Array();
				var namedata = new Array();
				var count=0;
				if(data!=null){
					if (data.rybxx!=null){
						for (var i = 0; i < data.rybxx.length; i++) {
							if(data.rybxx[i].sfjs=='0'){
								seriesData.push({value:data.rybxx[i].num,name:'废弃',sfjs:data.rybxx[i].sfjs});
							}else if(data.rybxx[i].sfsf=='0'){
								count+=parseInt(data.rybxx[i].num);
								seriesData.push({value:data.rybxx[i].num,name:'不收费',sfsf:data.rybxx[i].sfsf,sfjs:data.rybxx[i].sfjs});
							}else if(data.rybxx[i].sfsf=='1'){
								count+=parseInt(data.rybxx[i].num);
								seriesData.push({value:data.rybxx[i].num,name:'收费',sfsf:data.rybxx[i].sfsf,sfjs:data.rybxx[i].sfjs});
							}
						}
					}
					namedata.push("收费")
					namedata.push("不收费")
					namedata.push("废弃")
					if (data.rfssjfj!=null){
						if (data.rfssjfj.sjnum>0){
							namedata.push("送检")
							dataAxis.push({value:data.rfssjfj.sjnum,name:'送检'})
						}
						if (data.rfssjfj.fjnum>0){
							namedata.push("复检")
							dataAxis.push({value:data.rfssjfj.fjnum,name:'复检'})
						}
					}else {
						dataAxis.push({value:0,name:'送检'})
						dataAxis.push({value:0,name:'复检'})
						namedata.push("送检")
						namedata.push("复检")
					}
				}
				var pieoption = {
					title : {
						subtext :  '统计时间:'+searchData.jsrq+'     总数:'+count,
						x : 'right'
					},
					tooltip : {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
					},
					//color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					legend: {
						orient: 'vertical',
						left: 'left',
						top:'20',
						data: namedata
					},
					series : [
						{
							name: '个数',
							type: 'pie',
							radius: ['36%', '50%'],
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

						},
						{
							name:'占比',
							type:'pie',
							selectedMode: 'single',
							radius: '35%',
							center: ['50%', '60%'],
							label: {
								normal: {
									position: 'inner',
									formatter:"{b}:\n {c}(个)"
								}
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data:dataAxis
						},
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
			id:"rfszqbbs",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				var dataxAxis= new Array();
				var datasyAxis=new Array();
				var maxY=0;
				var intval;
				var tmp_y=0;
				if(data &&this.chart){
					dataxAxis.push('接收');
					datasyAxis.push(data.jswqy)
					dataxAxis.push('取样');
					datasyAxis.push(data.qywtq)
					dataxAxis.push('提取');
					datasyAxis.push(data.tqwkz)
					dataxAxis.push('扩增');
					datasyAxis.push(data.kzwjc)
					dataxAxis.push('检测');
					datasyAxis.push(data.jcwbg)
					dataxAxis.push('报告');
					datasyAxis.push(data.yfbg)
					for (var i = 0; i < datasyAxis.length; i++) {
						if (tmp_y<=datasyAxis[i]){
							tmp_y = datasyAxis[i];
						}
					}
					maxY=parseInt(tmp_y);
					maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					intval=maxY/4
					var pieoption = {
						title : {
							subtext : '',
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
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
								name: '',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: datasyAxis
							}]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(){
					});
				}
			}

		},
		{
			id:"rfsbbys",
			chart: null,
			el: null,
			render:function(data,searchData){
				var alldata= new Array();
				var maxY=0;
				var intval;
				var tmp_y=0;
				if(data &&this.chart){
					for (var i = 0; i < data.length; i++) {
						alldata.push({value:[data[i].name,data[i].sj], symbolOffset:[data[i].wz+'%',0]})
						// if (tmp_y<=data[i].sj){
						// 	tmp_y = data[i].sj;
						// }
					}
					// maxY=parseInt(tmp_y);
					// maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					// intval=maxY/4
					var pieoption = {
						title : {
							subtext : '',
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
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
						yAxis: {name: '分钟'},
						// yAxis:  [{
						// 	name: '分钟',
						// 	type: 'value',
						// 	min: 0,
						// 	max: maxY,
						// 	interval: intval
						// }],
						xAxis: {
							type: 'category',
							data:['接收','取样','提取','扩增','检测']
						},
						series: [
							{
								symbolSize: 5,
								data:alldata,
								type: 'scatter',
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
			id:"sjhb",
			chart:null,
			el:null,
			render:function(data,searchData){
				var dataxAxis= new Array();
				var datasf=new Array();
				var databsf=new Array();
				var datafq=new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							dataxAxis.push(data[i].sjhb);
							if(data[i].num!='0'){
								datafq.push(data[i].num);
							}else{
								datafq.push('');
							}
						}else if(data[i].sfsf=='0'){
							if(data[i].num!='0'){
								databsf.push(data[i].num)
							}else{
								databsf.push('');
							}
						}else if(data[i].sfsf=='1'){
							if(data[i].num!='0'){
								datasf.push(data[i].num)
							}else{
								datasf.push('');
							}
						}
					}
				}
				var pieoption  = {
						title : {
					    	subtext : '统计时间:     '+searchData.jsrq,
							x : 'right'
					    },
						tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    legend: {
					        data: ['收费','不收费','废弃'],
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
					            name: '废弃',
					            type: 'bar',
					            label: {
					                normal: {
					                    show: true,
					                    position: 'insideBottom'
					                }
					            },
					            data: datafq,
					            color:'#FECEFF'
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
					            color:'#2Fc6CB',
					            data: databsf
					        },
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
					            data: datasf,
					            color:'#37D8FF'
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
					    	subtext : '统计时间:     '+searchData.jsrq,
							x : 'right'
					    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}: {c} ({d}%)"
					    },
					  /*  color:[ '#FECEFF','#2Fc6CB','#37D8FF'],*/
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
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
					var lrrys = $("#statisticwxyh_daily #lrrys").val();
					if(lrrys){
						lrrys = lrrys.substr(1); //删除第一个字符
						lrrys = lrrys.substr(0, lrrys.length-1);
						lrrys = lrrys.split(" ").join("");
					}
					$("#statisticwxyh_daily").load("/wechat/statistics/fjsqViewDaily?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&jsrq="+searchData.jsrq
							+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&yhid="+$("#statisticwxyh_daily #yhid").val()+"&lrrys="+lrrys);
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
					    	subtext : '统计时间:     '+searchData.jsrq,
							x : 'right'
					    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}: {c} ({d}%)"
					    },
					    color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:datas
					    },
					    series : [
					        {
					            name: '',
					            type: 'pie',
					            radius : '45%',
					            center: ['50%', '60%'],
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
					var lrrys = $("#statisticwxyh_daily #lrrys").val();
					if(lrrys){
						lrrys = lrrys.substr(1); //删除第一个字符
						lrrys = lrrys.substr(0, lrrys.length-1);
						lrrys = lrrys.split(" ").join("");
					}
					$("#statisticwxyh_daily").load("/wechat/statistics/fqybViewDaily?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&yhid="+$("#statisticwxyh_daily #yhid").val()+"&lrrys="+lrrys);
				});
			}
		},{
			id:"possiblelist",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].num,name:data[i].lx,jyjg:data[i].jyjg});
					}
				}
				var pieoption = {
					    title : {
					    	subtext : '统计时间:     '+searchData.jsrq,
							x : 'right'
					    },
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					    color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data: ['阳性','阴性','疑似']
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
					var lrrys = $("#statisticwxyh_daily #lrrys").val();
					if(lrrys){
						lrrys = lrrys.substr(1); //删除第一个字符
						lrrys = lrrys.substr(0, lrrys.length-1);
						lrrys = lrrys.split(" ").join("");
					}
					$("#statisticwxyh_daily").load("/wechat/statistics/positiveViewDaily?lxmc="+param.data.name+"&bgrq="+searchData.jsrq+"&jsrq="+$("#statisticwxyh_daily #jsrq").val()+"&jyjg="+param.data.jyjg+"&yhid="+$("#statisticwxyh_daily #yhid").val()+"&lrrys="+lrrys);
				});
			}
		},{
			id:"sjxxList",
			chart:null,
			el:null,
			render:function(data,searchData){
		    		if(data.length>0){
		    			 var table ="<table class='tab'>"
							 	table+="<thead>"
							 		table+="<tr>"
							 			table+="<th class='width_5'>合作伙伴</th>"
							 			table+="<th class='width_3'>标本类型</th>"
							 			table+="<th class='width_4'>检测项目</th>"
							 			table+="<th class='width_1'>医院名称</th>"
							 			table+="<th class='width_2'>科室</th>"
					 					table+="<th class='width_6'>收费</th>"
						    	   table+="  </tr>"
						    	table+="</thead>"
						    	table+="<tbody>"
				    			for (var i = 0; i < data.length; i++) {
				    				table+="<tr>"
				    					if(data[i].db){
											table+=" <td class='width_5'>"+data[i].db+"</td>"
										}else{
											table+=" <td class='width_5'>&nbsp;</td>"
										}
					    				if(data[i].yblxmc){
											table+=" <td class='width_3'>"+data[i].yblxmc+"</td>"
										}else{
											table+=" <td class='width_3'>&nbsp;</td>"
										}
					    				if(data[i].jcxmmc){
											table+=" <td class='width_4'>"+data[i].jcxmmc+"</td>"
										}else{
											table+=" <td class='width_4'>&nbsp;</td>"
										}
										if(data[i].sjdw){
											table+=" <td class='width_1'>"+data[i].sjdwmc+"</td>"
										}else{
											table+=" <td class='width_1'>&nbsp;</td>"
										}
					    				if(data[i].dwmc){
											table+=" <td class='width_2'>"+data[i].dwmc+"</td>"
										}else{
											table+=" <td class='width_2'>&nbsp;</td>"
										}
					    				if(data[i].sfsf){
					    					if(data[i].sfsf=="否")
					    						table+=" <td class='width_6' style='color:red;'>"+data[i].sfsf+"</td>"
					    					else
					    						table+=" <td class='width_6'>"+data[i].sfsf+"</td>"
										}else{
											table+=" <td class='width_6'>&nbsp;</td>"
										}
				    				table+=" </tr>"
								}
		    			 	table+= "</tbody>"
		   				 table+="</table>"
		   			$("#statisticwxyh_daily #tjsj_sjxx").append("统计时间：    "+searchData.jsrq)
		   			$("#statisticwxyh_daily #sjxxList").append(table);
		    	}
			}
		},{
			id:"jcxmnum",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							seriesData.push({value:data[i].num,name:'废弃样例'});
						}else if(data[i].sfsf=='0'){
							seriesData.push({value:data[i].num,name:'不收费样例'});
						}else if(data[i].sfsf=='1'){
							seriesData.push({value:data[i].num,name:'收费样例'});
						}
					}
				}
				var pieoption = {
					    title : {
					    	subtext : '统计时间:     '+searchData.jsrq,
							x : 'right'
					    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}: {c} ({d}%)"
					    },
					    color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:['收费样例','不收费样例','废弃样例']
					    },
					    series : [
					        {
					            name: '',
					            type: 'pie',
					            radius : '45%',
					            center: ['50%', '60%'],
					            data:seriesData,
					            label: {
					            	normal: {
					            		  formatter:"{b}:\n {c}次({d}%)"
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
		},{
			id:"jcxmType",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].num,name:data[i].jcxmmc});
					}
				}
				var pieoption = {
						 title : {
						    	subtext : '统计时间:     '+searchData.jsrq,
								x : 'right'
						    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}:\n {c}次({d}%)"
					    },
					    color:[ '#FFBBFF','#99ccff','#EE799F'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:['DNA','RNA','D+R']
					    },
					    series : [
					        {
					            name: '检测项目',
					            type: 'pie',
					            radius: ['20%', '40%'],
					            center: ['50%', '60%'],
					            data:seriesData,
					            label: {
					            	normal: {
					            		  formatter:"{b}:\n{c}次({d}%)"
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
	]
	//路径配置
	setTimeout(function(){
		for (var i = 0; i < _renderArr.length; i++) {
			var _render=_renderArr[i];
			var _el=_render.el=document.getElementById(_idPrefix+_render.id);
			if(_el){
				_render.chart=echarts.init(_el,_statis_theme);
				_render.eventTag ="."+_eventTag+"-"+_render.id;
				if(_isShowLoading){
					_render.chart.showLoading({
						effect:_loadEffect
					});
				}
				$(window).off(_render.eventTag).on(
						"resize"+_render.eventTag,(
							function(_cfg){
								return function(){
									if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){
										return;
										}
									_cfg.chart.resize();
									}
								}	
							)(_render)
						);
			}
		}
		
		var _url = "/wechat/statistics/getDailyModel";
		var lrrys = $("#statisticwxyh_daily #lrrys").val();
		if(lrrys){
			lrrys = lrrys.substr(1); //删除第一个字符
			lrrys = lrrys.substr(0, lrrys.length-1);
			lrrys = lrrys.split(" ").join("");
		}
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			data:{"sign":$("#statisticwxyh_daily #sign").val(),"jsrq":$("#statisticwxyh_daily #jsrq").val(),"yhid":$("#statisticwxyh_daily #yhid").val(),"lrrys":lrrys},
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
					}
					if(datas.fjsq.length==0||datas.fjsq==null){
						$("#statisticwxyh_daily #echarts_daily_wxyh_fjsq").parent().hide();
					}
					if(datas.fqyb.length==0||datas.fqyb==null){
						$("#statisticwxyh_daily #echarts_daily_wxyh_fqyb").parent().hide();
					}
					if(datas.ybxx.length==0||datas.ybxx==null){
						$("#statisticwxyh_daily #echarts_daily_wxyh_ybxx").parent().hide();
					}
					if(datas.possiblelist.length==0||datas.possiblelist==null){
						$("#statisticwxyh_daily #echarts_daily_wxyh_possiblelist").parent().hide();
					}
					if(datas.sjhb==null){
						$("#statisticwxyh_daily #echarts_daily_wxyh_sjhb").parent().hide();
					}
					if(datas.sjxxList==null||datas.sjxxList.length==0){
						$("#statisticwxyh_daily #sjxxList").parent().hide();
					}
					if(datas.jcxmType==null||datas.jcxmType.length==0){
						$("#statisticwxyh_daily #echarts_daily_wxyh_jcxmType").parent().hide();
					}
					if(datas.jcxmnum==null ||datas.jcxmnum.length==0){
						$("#statisticwxyh_daily #echarts_daily_wxyh_jcxmnum").parent().hide();
					}
					if(datas.rfszqbbs==null ||datas.rfszqbbs.length==0){
						$("#statisticwxyh_daily #echarts_daily_wxyh_rfszqbbs").parent().hide();
					}
					if(datas.rfsbbys==null ||datas.rfsbbys.length==0){
						$("#statisticwxyh_daily #echarts_daily_wxyh_rfsbbys").parent().hide();
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
}

$(function(){
	loadModelDaily();
})