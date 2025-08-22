var loadDayLeadStatis=function(){
	var _eventTag = "dayLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_day_lead_"; //id前缀
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
				this.chart.on("click",function(){
					//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
				});
			}
		},
		{
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
					var load_flg=$("#load_flg").val();
					if(load_flg=="0"){
						dis_toForward("/wechat/statistics/FjsqListView?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&load_flg="+load_flg);
					}else if(load_flg=="1"){
						$("#daliyStatis").load("/wechat/statistics/FjsqListView?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&load_flg="+load_flg+"&jsrq="+$("#daliyStatis #jsrq").val());
					}
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
					var load_flg=$("#load_flg").val();
					if(load_flg=="0"){
						dis_toForward("/wechat/statistics/fqybListView?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&load_flg="+load_flg);
					}else if(load_flg=="1"){
						$("#daliyStatis").load("/wechat/statistics/fqybListView?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&load_flg="+load_flg);
					}
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
					var load_flg=$("#load_flg").val();
					if(load_flg=="0"){
						dis_toForward("/wechat/statistics/positive_PageList?lxmc="+param.data.name+"&bgrq="+searchData.jsrq+"&jyjg="+param.data.jyjg+"&load_flg="+load_flg);
					}else if(load_flg=="1"){
						$("#daliyStatis").load("/wechat/statistics/positive_PageList?lxmc="+param.data.name+"&bgrq="+searchData.jsrq+"&jyjg="+param.data.jyjg+"&load_flg="+load_flg+"&jsrq="+$("#daliyStatis #jsrq").val());
					}
					
				});
			}
		},{
			id:"sjxxList",
			chart:null,
			el:null,
			render:function(data,searchData){
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
				    		if(data){
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
											table+=" <td class='width_1'>"+data[i].sjdw+"</td>"
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
				    		}
				      table+= "</tbody>"
				  table+="</table>"
			$("#daliyStatis #tjsj_sjxx").append("统计时间：    "+searchData.jsrq)
			$("#sjxxList").append(table);
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
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} {b}: {c} ({d}%)"
					    },
					    color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:['收费','不收费','废弃']
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
					            		  formatter:"{b}:\n{c}次"
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
				var titleData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].num,name:data[i].jcxmmc});
						titleData.push(data[i].jcxmmc);
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
					        data:titleData
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
		}/*,{
			id:"species",
			chart:null,
			el:null,
			render:function(data,searchData){
				var dataXAxis= new Array();
				var dataYAxis=new Array();
				for (var i = 0; i < data.length; i++) {
					dataXAxis.push(data[i].wzzwm);
					dataYAxis.push(data[i].num);
				}
				var pieoption  = {
					    color: ['#3398DB'],
					    tooltip : {
					        trigger: 'axis',
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        containLabel: true
					    },
					    xAxis : [
					        {
					            type : 'category',
					            data : dataXAxis,
					            axisTick: {
					                alignWithLabel: true
					            }
					        }
					    ],
					    //拖动
					   dataZoom: [{
					        type: 'slider',
					        filterMode: 'weakFilter',
					        showDataShadow: true,
					        top:250,
					        height: 20,
					        borderColor: 'transparent',
					        backgroundColor: '#e2e2e2',
					        handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
					        handleSize: 25,
					        handleStyle: {
					            shadowBlur: 6,
					            shadowOffsetX: 1,
					            shadowOffsetY: 2,
					            shadowColor: '#aaa'
					        },
					        labelFormatter: ''
					    }, {
					        type: 'inside',
					        filterMode: 'weakFilter'
					    }],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					        {
					            name:'直接访问',
					            type:'bar',
					            barWidth: '60%',
					            data:dataYAxis
					        }
					    ]
				}
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(param){
				});
			}
		}*/
	];
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
		var _url = "/wechat/statistics/getSfsfybxx";
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			data:{"sign":$("#sign").val(),"jsrq":$("#jsrq").val()},
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
					}
					if(datas.fjsq.length==0||datas.fjsq==null){
						$("#echarts_day_lead_fjsq").parent().hide();
					}
					if(datas.fqyb.length==0||datas.fqyb==null){
						$("#echarts_day_lead_fqyb").parent().hide();
					}
					if(datas.ybxx.length==0||datas.ybxx==null){
						$("#echarts_day_lead_ybxx").parent().hide();
					}
					if(datas.possiblelist.length==0||datas.possiblelist==null){
						$("#echarts_day_lead_possiblelist").parent().hide();
					}
					if(datas.sjhb==null){
						$("#echarts_day_lead_sjhb").parent().hide();
					}
					if(datas.sjxxList==null||datas.sjxxList.length==0){
						$("#sjxxList").parent().hide();
					}
					if(datas.jcxmType==null||datas.jcxmType.length==0){
						$("#echarts_day_lead_jcxmType").parent().hide();
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
	/*echartsBtnInit(_renderArr);*/
}
/*var infoObj = document.getElementById("sjxxList");
infoObj.addEventListener("touchstart", function (event) {
})*/
$(function(){
    loadDayLeadStatis();
})