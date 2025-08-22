var salesModels = [];
//销售达成率
$("#daliyStatis div[name='tjsj_salesrate']").each(function () {
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
		$("#daliyStatis #xszblist_" + dm).html("");
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
				table += "<tr onclick='showSalesAttainmentRateDaliyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\" ,\"" + '' + "\")'>";
				table += "<td class='qu_td' rowspan='2' style='background-color:" + color + "'>" + rq + "<br>" + xszblist[j].zsxm +"</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + dqt + "/" + zts + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].sfsl + "/" + xszbsl + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfsl + "/" + wcsl + "</td>";
				table += "</tr>";
				table += "<tr onclick='showSalesAttainmentRateDaliyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\" ,\"" + '' + "\")'>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + tsrate + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + ratio + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfl + "%</td>";
				table += "</tr>";
			}
			table += "</body>";
			table += "</table></div>";
			$("#daliyStatis #xszblist_" + dm).append(table);
		}
		$("#daliyStatis #tjsj_t_xszb_"+dm).html("");
		$("#daliyStatis #tjsj_t_xszb_"+dm).append(kszq+"~"+jszq);
	};
	salesModels.push(map);
});
//RFS销售达成率
$("#daliyStatis div[name='tjsj_salesrfsrate']").each(function () {

	var map = {}
	var dm = $("#" + this.id).attr("dm")
	var id = "xszbrfslist_" + dm;
	map["id"] = id;
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function (data, searchData) {

		//判断不为空时，拼接table
		var color = "#00b2ee";
		var xszblist = data;
		$("#daliyStatis #xszbrfslist_" + dm).html("");
		var kszq = "";
		var jszq = "";
		if (xszblist != null && xszblist.length > 0) {
			var zblxmc = data[0].zblxcsmc;
			var table = "<table id='tabAttainmenRfstRate'>"
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
				table += "<tr onclick='showSalesAttainmentRateDaliyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\",\"" + 'IMP_REPORT_RFS_TEMEPLATE' + "\")'>";
				table += "<td class='qu_td' rowspan='2' style='background-color:" + color + "'>" + rq + "<br>" + xszblist[j].zsxm +"</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + dqt + "/" + zts + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].sfsl + "/" + xszbsl + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfsl + "/" + wcsl + "</td>";
				table += "</tr>";
				table += "<tr onclick='showSalesAttainmentRateDaliyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\",\"" + 'IMP_REPORT_RFS_TEMEPLATE' + "\")'>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + tsrate + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + ratio + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfl + "%</td>";
				table += "</tr>";
			}
			table += "</body>";
			table += "</table></div>";
			$("#daliyStatis #xszbrfslist_" + dm).append(table);
		}
		$("#daliyStatis #tjsjrfs_t_xszb_xszb_"+dm).html("");
		$("#daliyStatis #tjsjrfs_t_xszb_xszb_"+dm).append(kszq+"~"+jszq);
	};
	salesModels.push(map);
});
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
				var count=0;
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							seriesData.push({value:data[i].num,name:'废弃',sfjs:data[i].sfjs});
						}else if(data[i].sfjs=='2'){
							count+=parseInt(data[i].num);
							seriesData.push({value:data[i].num,name:'外送',sfjs:data[i].sfjs});
						}else if(data[i].sfsf=='0'){
							count+=parseInt(data[i].num);
							seriesData.push({value:data[i].num,name:'不收费',sfsf:data[i].sfsf,sfjs:data[i].sfjs});
						}else if(data[i].sfsf=='1'){
							count+=parseInt(data[i].num);
							seriesData.push({value:data[i].num,name:'收费',sfsf:data[i].sfsf,sfjs:data[i].sfjs});
						}
					}
				}
				var pieoption = {
					    title : {
					    	subtext :  '统计时间:'+ (searchData?searchData.jsrq:'')+'     总数:'+count,
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
					        data: ['收费','不收费','废弃','外送']
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
					                
					            },
					        }
					    ]
					};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(param){
						//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
						var load_flg = $("#load_flg").val();
						if (load_flg == "0") {
							dis_toForward("/ws/statistics/BbsListView?sfjs=" + param.data.sfjs + "&sfsf=" + param.data.sfsf + "&jsrq=" + searchData.jsrq + "&ybztmc=" + param.data.name + "&load_flg=" + load_flg);
						} else if (load_flg == "1") {
							$("#daliyStatis").load("/ws/statistics/BbsListView?sfjs=" + param.data.sfjs + "&sfsf=" + param.data.sfsf + "&jsrq=" + searchData.jsrq + "&ybztmc=" + param.data.name + "&load_flg=" + load_flg);
						}
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
					//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
					if (param.data.name!='送检'&&param.data.name!='复检') {
						var load_flg = $("#load_flg").val();
						if (load_flg == "0") {
							dis_toForward("/ws/statistics/BbsListView?cskz3=IMP_REPORT_RFS_TEMEPLATE&sfjs=" + param.data.sfjs + "&sfsf=" + param.data.sfsf + "&jsrq=" + searchData.jsrq + "&ybztmc=" + param.data.name + "&load_flg=" + load_flg);
						} else if (load_flg == "1") {
							$("#daliyStatis").load("/ws/statistics/BbsListView?cskz3=IMP_REPORT_RFS_TEMEPLATE&sfjs=" + param.data.sfjs + "&sfsf=" + param.data.sfsf + "&jsrq=" + searchData.jsrq + "&ybztmc=" + param.data.name + "&load_flg=" + load_flg);
						}
					}
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
							x : 'left'
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
					           // color:'#2Fc6CB',
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
					           // color:'#37D8FF'
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
						dis_toForward("/ws/statistics/FjsqListView?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&load_flg="+load_flg);
					}else if(load_flg=="1"){
						$("#daliyStatis").load("/ws/statistics/FjsqListView?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&load_flg="+load_flg+"&jsrq="+$("#daliyStatis #jsrq").val());
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
					  //  color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
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
						dis_toForward("/ws/statistics/fqybListView?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&load_flg="+load_flg);
					}else if(load_flg=="1"){
						$("#daliyStatis").load("/ws/statistics/fqybListView?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&load_flg="+load_flg);
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
						dis_toForward("/ws/statistics/positive_PageList?lxmc="+param.data.name+"&bgrq="+searchData.jsrq+"&jyjg="+param.data.jyjg+"&load_flg="+load_flg);
					}else if(load_flg=="1"){
						$("#daliyStatis").load("/ws/statistics/positive_PageList?lxmc="+param.data.name+"&bgrq="+searchData.jsrq+"&jyjg="+param.data.jyjg+"&load_flg="+load_flg+"&jsrq="+$("#daliyStatis #jsrq").val());
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
					 			table+="<th class='width_2'>检测地</th>"
					 			table+="<th class='width_5'>合作伙伴</th>"
					 			table+="<th class='width_3'>标本类型</th>"
					 			table+="<th class='width_4'>项目</th>"
					 			table+="<th class='width_1'>医院名称</th>"
					 			table+="<th class='width_2'>科室</th>"
			 					table+="<th class='width_6'>收费</th>"
				    	   table+="  </tr>"
				    	table+="</thead>"
				    	table+="<tbody>"
						var Dcount=0;
						var Rcount=0;
						var Ccount=0;
				    		if(data){
				    			for (var i = 0; i < data.length; i++) {
				    				table+="<tr>"
					    				if(data[i].jcdwjc){
											table+=" <td class='width_2'>"+data[i].jcdwjc+"</td>"
										}else{
											table+=" <td class='width_2'>&nbsp;</td>"
										}
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
					    				if(data[i].jcxmkzcs){
											if(data[i].jcxmkzcs=="D"){
												Dcount++;
											}else if(data[i].jcxmkzcs=="R"){
												Rcount++;
											}else if(data[i].jcxmkzcs=="C"){
												Ccount++;
											}
											table+=" <td class='width_4'>"+data[i].jcxmkzcs+"</td>"
										}else{
											table+=" <td class='width_4'>&nbsp;</td>"
										}
										if(data[i].sjdwmc){
											// 是否需要判断其它，当前未判断
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
					    					else{
												table+=" <td class='width_6'>"+data[i].sfsf+"</td>"
											}
										}else{
											table+=" <td class='width_6'>&nbsp;</td>"
										}
				    				table+=" </tr>"
								}
				    		}
				      table+= "</tbody>"
				  table+="</table>"
				  if(data.length>0){
					  $("#daliyStatis #tjsj_sjxx").append("D："+Dcount+"\xa0\xa0\xa0\xa0"+"R："+Rcount+"\xa0\xa0\xa0\xa0"+"C："+Ccount+"\xa0\xa0\xa0\xa0"+"统计时间：    "+searchData.jsrq)
				  }
			      $("#sjxxList").append(table);
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
					  $("#daliyStatis #tjsj_t_sjxx").append("统计时间：    "+searchData.jsrq)
				  }
				  $("#notSylist").append(table);
			}
		},{
			id:"jcxmnum",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				var count=0;
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						if(data[i].sfjs=='0'){
							seriesData.push({value:data[i].num,name:'废弃'});
						}else if(data[i].sfjs=='2'){
							count+=parseInt(data[i].num);
							seriesData.push({value:data[i].num,name:'外送'});
						}else if(data[i].sfsf=='0'){
							count+=parseInt(data[i].num);
							seriesData.push({value:data[i].num,name:'不收费'});
						}else if(data[i].sfsf=='1'){
							count+=parseInt(data[i].num);
							seriesData.push({value:data[i].num,name:'收费'});
						}
					}
				}
				var pieoption = {
					    title : {
					    	subtext : '统计时间:'+searchData.jsrq+'     总数:'+count,
							x : 'left'
					    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} {b}: {c} ({d}%)"
					    },
					   // color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:['收费','不收费','废弃','外送']
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
				var count=0;
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						count+=parseInt(data[i].num);
						seriesData.push({value:data[i].num,name:data[i].jcxmmc});
						titleData.push(data[i].jcxmmc);
					}
				}
				var pieoption = {
						 title : {
						    	subtext : '统计时间:'+searchData.jsrq+'     总数:'+count,
								x : 'right'
						    },
					    tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}:\n {c}次({d}%)"
					    },
					 //   color:[ '#FFBBFF','#99ccff','#EE799F'],
					    /*legend: {
					        orient: 'vertical',
					        left: 'left',
					        top:'20',
					        data:titleData
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
		var _url = "/ws/statistics/getSfsfybxx";
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			data:{"sign":$("#daliyStatis #sign").val(),"jsrq":$("#daliyStatis #jsrq").val()},
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
						if(_data==null || _data.length==0){
							$("#daliyStatis #"+_render.id).parent().hide();
						}
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
					if(datas.notSylist==null||datas.notSylist.length==0){
						$("#notSylist").parent().hide();
					}
					if(datas.jcxmType==null||datas.jcxmType.length==0){
						$("#echarts_day_lead_jcxmType").parent().hide();
					}
					if(datas.jcxmnum==null ||datas.jcxmnum.length==0){
						$("#echarts_day_lead_jcxmnum").parent().hide();
					}
					if(datas.rfszqbbs==null||datas.rfszqbbs.length==0){
						$("#echarts_day_lead_rfszqbbs").parent().hide();
					}
					if(datas.rfsbbys==null ||datas.rfsbbys.length==0){
						$("#echarts_day_lead_rfsbbys").parent().hide();
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
	/*echartsBtnInit(_renderArr);*/
}

$("#jcdw_default").unbind("click").click(function(e){
	var load_flg=$("#daliyStatis #load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
	            '<input type="text" name="view_url" value="/ws/statistics/getDailyByJcdw?load_flg='+load_flg+"&jsrq="+$("#jsrq").val()+"&sign="+$("#sign").val()+'"/>' +
	        '</form>')
	    .appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#daliyStatis").load("/ws/statistics/getDailyByJcdw?load_flg="+load_flg+"&jsrq="+$("#jsrq").val()+"&sign="+$("#sign").val());
	}
	return true;
});

$("#sjhb_default").unbind("click").click(function(e){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/getDailyBySjhb?load_flg='+load_flg+"&jsrq="+$("#jsrq").val()+"&sign="+$("#sign").val()+'"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#daliyStatis").load("/ws/statistics/getDailyBySjhb?load_flg="+load_flg+"&jsrq="+$("#jsrq").val()+"&sign="+$("#sign").val());
	}
	return true;
});
/*var infoObj = document.getElementById("sjxxList");
infoObj.addEventListener("touchstart", function (event) {
})*/
$(function(){
    loadDayLeadStatis();
})

function qytjlist(){
	var load_flg=$("#load_flg").val();
	$("#daliyStatis").load("/ws/statistics/getZoneList?load_flg="+load_flg+"&jsrq="+$("#jsrq").val()+"&sign="+$("#sign").val());
}

function showSalesAttainmentRateDaliyByArea(yhid,zblxmc,qyid,qymc,kszq,jszq,cskz3){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
	            '<input type="text" name="view_url" value="/ws/statistics/salesAttainmentRateByAreaView?load_flg='+load_flg+'&zblxcsmc='+zblxmc+'&yhid='+yhid+'&qyid='+qyid+'&qymc='+qymc+'&kszq='+kszq+'&jszq='+jszq+'&cskz3='+cskz3+'&flag=daliy"/>' +
	        '</form>')
	    .appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#daliyStatis").load("/ws/statistics/salesAttainmentRateByAreaView?load_flg="+load_flg+"&zblxcsmc="+zblxmc+"&yhid="+yhid+"&qyid="+qyid+"&qymc="+qymc+"&kszq="+kszq+"&jszq="+jszq+'&cskz3='+cskz3+"&flag=daliy");
	}
}