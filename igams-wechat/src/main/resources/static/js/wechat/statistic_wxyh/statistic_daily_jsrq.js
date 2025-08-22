var salesModels = [];
//销售达成率
$("#statisticwxyh_daily_jsrq div[name='tjsj_salesrate']").each(function () {
	var map = {}
	var dm = $("#" + this.id).attr("dm")
	var id = "xszblist_" + dm;
	map["id"] = id;
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function (data, searchData) {
		//判断不为空时，拼接table
		var color = "#00b2ee";
		var xszblist = data;
		$("#statisticwxyh_daily_jsrq #xszblist_" + dm).html("");
		var kszq = "";
		var jszq = "";
		if (xszblist != null && xszblist.length > 0) {
			var zblxmc = data[0].zblxcsmc;
			var table = "<table id='tabAttainmentRate'>"
			table += "<thead>";
			table += "<tr>";
			table += "<th  class='qu_td' >日期/姓名</th>";
			table += "<th class='other_td'>时间进度</th>";
			table += "<th class='other_td'>达成率</th>";
			table += "<th class='other_td'>免费率</th>";
			table += "</tr>";
			table += "</thead>";
			table += "<body>";
			for (var j = 0; j < xszblist.length; j++) {
				kszq = xszblist[j].kszq;
				jszq = xszblist[j].jszq;
				var color = "#ffe1ff";
				if (j % 2 == 0) {
					color = "#eeeed1";
				}
				var dqt = xszblist[j].dqts;//当前第几天
				var zts = xszblist[j].zts;//总天数
				var str = dqt / zts;
				var tsrate = Number(str * 100).toFixed(2) + "%";
				var wcsl = xszblist[j].wczsl;
				var xszbsl = xszblist[j].sz;
				var wcb = wcsl / xszbsl;
				if (!wcb) {
					wcb = 0;
				}
				var ratio = xszblist[j].xswcl + "%";
				var rq = xszblist[j].kszq;
				if ("Q" == zblxmc) {
					if (rq.indexOf("-01") > 0) {
						rq = "第一季度";
					}
					if (rq.indexOf("-04") > 0) {
						rq = "第二季度";
					}
					if (rq.indexOf("-07") > 0) {
						rq = "第三季度";
					}
					if (rq.indexOf("-10") > 0) {
						rq = "第四季度";
					}
				}
				table += "<tr onclick='showSalesAttainmentRateDaliyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\")'>";
				table += "<td class='qu_td' rowspan='2' style='background-color:" + color + "'>" + rq + "<br>"+xszblist[j].zsxm+"</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + dqt + "/" + zts + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].sfsl + "/" + xszbsl + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfsl + "/" + wcsl + "</td>";
				table += "</tr>";
				table += "<tr onclick='showSalesAttainmentRateDaliyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\")'>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + tsrate + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + ratio + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfl + "%</td>";
				table += "</tr>";
			}
			table += "</body>";
			table += "</table></div>";
			$("#statisticwxyh_daily_jsrq #xszblist_" + dm).append(table);
		}
		$("#statisticwxyh_daily_jsrq #tjsj_t_xszb_"+dm).html("");
		$("#statisticwxyh_daily_jsrq #tjsj_t_xszb_"+dm).append(kszq+"~"+jszq);
	};
	salesModels.push(map);
});
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
					    //color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
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
					            //color:'#FECEFF'
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
					            //color:'#2Fc6CB',
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
					            //color:'#37D8FF'
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
//						if(data[i].lx=="ADDJCXM_TYPE"){
//							seriesData.push({value:data[i].count,name:'加测申请',flg:'ADDJCXM_TYPE',xh:data[i].xh});
//						}else if(data[i].lx=="RECHECK_TYPE"){
//							seriesData.push({value:data[i].count,name:data[i].xh+'次复测',flg:'RECHECK_TYPE',xh:data[i].xh});
//						}
						if(data[i].fjlxdm=="RECHECK"){
							seriesData.push({value:data[i].count,name:data[i].xh+'次复测',flg:data[i].lx,xh:data[i].xh});
						}else{
							seriesData.push({value:data[i].count,name:data[i].lxmc,flg:data[i].lx,xh:data[i].xh});
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
					        top:'20'
//					        data: ['加测申请','1次复测','2次复测']
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
					$("#statisticwxyh_daily_jsrq").load("/ws/ststictis/fjsqViewDailyByJsrq?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&yhid="+$("#statisticwxyh_daily_jsrq #yhid").val());
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
					   // color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
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
					$("#statisticwxyh_daily_jsrq").load("/ws/ststictis/fqybViewDailyByJsrq?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&yhid="+$("#statisticwxyh_daily_jsrq #yhid").val());
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
					   // color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
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
					$("#statisticwxyh_daily_jsrq").load("/ws/ststictis/positiveViewDailyByJsrq?lxmc="+param.data.name+"&bgrq="+searchData.jsrq+"&jyjg="+param.data.jyjg+"&yhid="+$("#statisticwxyh_daily_jsrq #yhid").val());
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
		   			$("#statisticwxyh_daily_jsrq #tjsj_sjxx").append("统计时间：    "+searchData.jsrq)
		   			$("#statisticwxyh_daily_jsrq #sjxxList").append(table);
		    	}
			}
		},{
			id:"notSylist",
			chart:null,
			el:null,
			render:function(data,searchData){
				 var table ="<table class='tab'>"
					 	table+="<thead>"
					 		table+="<tr>"
					 			table+="<th class='width_7'>序号</th>"
					 			table+="<th class='width_5'>标本类型</th>"
					 			table+="<th class='width_2'>项目</th>"
					 			table+="<th class='width_2'>检测地</th>"
					 			table+="<th class='width_1'>合作伙伴</th>"
					 			table+="<th class='width_8'>接收日期</th>"
				    	   table+="  </tr>"
				    	table+="</thead>"
				    	table+="<tbody>"
				    		if(data){
				    			for (var i = 0; i < data.length; i++) {
				    				table+="<tr>"
										table+=" <td class='width_7'>"+(i+1)+"</td>"
					    				if(data[i].yblxmc){
											table+=" <td class='width_5'>"+data[i].yblxmc+"</td>"
										}else{
											table+=" <td class='width_5'>&nbsp;</td>"
										}
					    				if(data[i].jcxmkzcs){
											table+=" <td class='width_2'>"+data[i].jcxmkzcs+"</td>"
										}else{
											table+=" <td class='width_2'>&nbsp;</td>"
										}
					    				if(data[i].jcdwjc){
											table+=" <td class='width_2'>"+data[i].jcdwjc+"</td>"
										}else{
											table+=" <td class='width_2'>&nbsp;</td>"
										}
										if(data[i].db){
											table+=" <td class='width_1'>"+data[i].db+"</td>"
										}else{
											table+=" <td class='width_1'>&nbsp;</td>"
										}
					    				if(data[i].jsrq){
											table+=" <td class='width_8'>"+data[i].jsrq+"</td>"
										}else{
											table+=" <td class='width_8'>&nbsp;</td>"
										}
				    				table+=" </tr>"
								}
				    		}
				      table+= "</tbody>"
				  table+="</table>"
				  if(data.length>0){
					  $("#statisticwxyh_daily_jsrq #tjsj_t_sjxx").append("统计时间：    "+searchData.jsrq)
				  }
				  $("#notSylist").append(table);
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
					   // color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
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
					   // color:[ '#FFBBFF','#99ccff','#EE799F'],
					    /*legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:['DNA','RNA','D+R']
					    },*/
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
	for (var i = 0; i < salesModels.length; i++) {
		_renderArr.push(salesModels[i]);
	}
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
		
		var _url = "/ws/statictis/getdailymodel";
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			data:{"sign":$("#statisticwxyh_daily_jsrq #sign").val(),"jsrq":$("#statisticwxyh_daily_jsrq #jsrq").val(),"yhid":$("#statisticwxyh_daily_jsrq #yhid").val()},
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
						if(_data==null || _data.length==0){
							$("#statisticwxyh_daily_jsrq #"+_render.id).parent().hide();
						}
					}
					if(datas.fjsq.length==0||datas.fjsq==null){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_fjsq").parent().hide();
					}
					if(datas.fqyb.length==0||datas.fqyb==null){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_fqyb").parent().hide();
					}
					if(datas.ybxx.length==0||datas.ybxx==null){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_ybxx").parent().hide();
					}
					if(datas.possiblelist.length==0||datas.possiblelist==null){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_possiblelist").parent().hide();
					}
					if(datas.sjhb==null){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_sjhb").parent().hide();
					}
					if(datas.notSylist==null||datas.notSylist.length==0){
						$("#statisticwxyh_daily_jsrq #notSylist").parent().hide();
					}
					if(datas.sjxxList==null||datas.sjxxList.length==0){
						$("#statisticwxyh_daily_jsrq #sjxxList").parent().hide();
					}
					if(datas.jcxmType==null||datas.jcxmType.length==0){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_jcxmType").parent().hide();
					}
					if(datas.jcxmnum==null ||datas.jcxmnum.length==0){
						$("#statisticwxyh_daily_jsrq #echarts_daily_wxyh_jcxmnum").parent().hide();
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
}

var rollback=function(){
	var xsbj=$("#statisticwxyh_daily_jsrq #xsbj").val();
	if(xsbj=="1"){
		$("#rollback").show();
	}else{
		$("#rollback").hide();
	}
}

/**
 * 返回上一页
 * @returns
 */
$("#statisticwxyh_daily_jsrq #rollback").bind("click",function(){
	var load_flg=$("#load_flg").val();
	$("#statisticwxyh_daily_jsrq").load("/ws/statistics/getZoneListByJsrq?load_flg="+load_flg+"&jsrq="+$("#jsrq").val()+"&sign="+$("#sign").val());
})
function showSalesAttainmentRateDaliyByArea(yhid,zblxmc,qyid,qymc,kszq,jszq){
	var jsrq = $("#statisticwxyh_daily_jsrq #jsrq").val();
	var sign = $("#statisticwxyh_daily_jsrq #sign").val();
	$("#statisticwxyh_daily_jsrq").load("/ws/statistics/salesAttainmentRateByAreaViewAndJsrq?zblxcsmc="+zblxmc+"&yhid="+yhid+"&qyid="+qyid+"&qymc="+qymc+"&kszq="+kszq
			+"&jszq="+jszq+"&jsrq="+jsrq+"&parentId="+yhid+"&flag=daliy_wxyh");
}
$(function(){
	loadModelDaily();
	rollback();
})