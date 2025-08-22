var xsxzcssqs_hbbj=false;//环比图标记
var ptqstj_hbbj=false;//环比图标记
var cpqstj_hbbj=false;//环比图标记
var pagedatas;
var qgqs_lineFlag=false;
var flModels = [];
// $("#weeklyStatis_jsrq div[name='echarts_week_lead_fltj']").each(function(){
// 	var map = {}
// 	map["id"] = "fltj_"+$("#"+this.id).attr("dm");
// 	map["chart"] = null;
// 	map["el"] = null;
// 	map["render"] = function(data,searchData){
// 		var prerq = "";
// 		if(data&&this.chart){
//
// 			var datasf=new Array();
// 			var databsf=new Array();
// 			var datahj=new Array();
// 			var datalegend=new Array();
// 			var maxY=0;
// 			var tmp_y=0;
// 			var intval;
// 			for(var i=0;i<data.length;i++){
// 				var rq;
// 				if(data[i].rq.length >=10)
// 					rq = data[i].rq.substr(5)
// 				else
// 					rq = data[i].rq
//
// 				datalegend.push(rq)
// 				databsf.push(data[i].bsfnum);
// 				datasf.push(data[i].sfnum);
//
// 				tmp_y = parseInt(data[i].bsfnum)+parseInt(data[i].sfnum);
// 				if(maxY < tmp_y)
// 					maxY = parseInt(tmp_y)
//
// 				datahj.push(tmp_y);
// 			}
// 			intval=maxY/4
// 			var pieoption = {
// 				tooltip : {
// 					trigger: 'axis',
// 					axisPointer : {
// 						type : 'shadow'
// 					},
// 					axisLabel:{
// 						rotate:-90,
// 						show: true,
// 						interval:0
// 					},
// 				},
// 				legend: {
// 					data: ['收费', '不收费'],
// 					textStyle: {
// 						color: '#3E9EE1'
// 					},
// 					y:35
// 				},
// 				toolbox: {
// 					show : true,
// 					feature : {
// 						dataView : {show: true, readOnly: false},
// 						magicType : {show: true, type: ['line', 'bar']},
// 						restore : {show: true},
// 						saveAsImage : {show: true}
// 					}
// 				},
// 				grid: {
// 					left: '3%',
// 					right: '4%',
// 					top:80,
// 					height:200,
// 					containLabel: true
// 				},
// 				xAxis: {
// 					type: 'category',
// 					data: datalegend,
// 				},
// 				yAxis:  [{
// 					name: '个数',
// 					type: 'value',
// 					min: 0,
// 					max: maxY,
// 					interval: intval,
// 				}],
// 				series: [
// 					{
// 						name: '收费',
// 						type: 'bar',
// 						stack: '总量',
// 						barGap: 0,
// 						label: {
// 							normal: {
// 								show: true,
// 								position: 'insideBottom',
// 								formatter: function (params) {
// 									if (params.value > 0) {
// 										return params.value;
// 									} else {
// 										return '';
// 									}
// 								}
// 							}
// 						},
// 						data: datasf
// 					},
// 					{
// 						name: '不收费',
// 						type: 'bar',
// 						stack: '总量',
// 						label: {
// 							normal: {
// 								show: true,
// 								position: 'insideBottom',
// 								formatter: function (params) {
// 									if (params.value > 0) {
// 										return params.value;
// 									} else {
// 										return '';
// 									}
// 								}
// 							}
// 						},
// 						data: databsf
// 					},
// 					{
// 						name: '合计',
// 						type: 'bar',
// 						stack: '总量',
// 						label: {
// 							normal: {
// 								show: true,
// 								position: 'insideBottom',
// 								textStyle:{ color:'#ff4848',fontSize: 12 }
// 							}
// 						},
// 						itemStyle: {
// 							normal:{
// 								color:'rgba(128, 128, 128, 0)'
// 							}
// 						},
// 						data: datahj
// 					},
// 				]
// 			};
// 			this.chart.clear();
// 			this.chart.setOption(pieoption);
// 		}
// 		this.chart.hideLoading();
// 		this.chart.on('click',function(){
//
// 		});
// 	};
// 	flModels.push(map);
// });
//销售达成率
$("#weeklyStatis_jsrq div[name='salesAttainmentRate']").each(function () {
	var map = {}
	var dm = $("#" + this.id).attr("dm")
	var id = "salesAttainmentRate_" + dm;
	map["id"] = id;
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function (data, searchData) {
		//判断不为空时，拼接table
		var color = "#00b2ee";
		var xszblist = data;
		$("#weeklyStatis_jsrq #echarts_week_lead_salesAttainmentRate_" + dm).html("");
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
				table += "<tr onclick='showSalesAttainmentRateWeeklyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\")'>";
				table += "<td class='qu_td' rowspan='2' style='background-color:" + color + "'>" + rq + "<br>"+xszblist[j].zsxm+"</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + dqt + "/" + zts + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].sfsl + "/" + xszbsl + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfsl + "/" + wcsl + "</td>";
				table += "</tr>";
				table += "<tr onclick='showSalesAttainmentRateWeeklyByArea(\"" + xszblist[j].yhid + "\",\"" + zblxmc + "\",\"" + xszblist[j].qyid + "\",\"" + xszblist[j].qymc + "\",\"" + xszblist[j].kszq + "\",\"" + xszblist[j].jszq + "\")'>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + tsrate + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + ratio + "</td>";
				table += "<td class='other_td' style='background-color:" + color + "'>" + xszblist[j].bsfl + "%</td>";
				table += "</tr>";
			}
			table += "</body>";
			table += "</table></div>";
			$("#weeklyStatis_jsrq #echarts_week_lead_salesAttainmentRate_" + dm).append(table);
		}
		$("#weeklyStatis_jsrq #tjsj_salesAttainmentRate_"+dm).html("");
		$("#weeklyStatis_jsrq #tjsj_salesAttainmentRate_"+dm).append(searchData.zqs.salesAttainmentRate);
	};
	flModels.push(map);
});

//统计销售平台达成率
// $("#weeklyStatis_jsrq div[name='tjxsdcl']").each(function () {
// 	var map = {}
// 	var dm = $("#" + this.id).attr("dm")
// 	var id = "tjxsdcl_" + dm;
// 	map["id"] = id;
// 	map["chart"] = null;
// 	map["el"] = null;
// 	var yxxid="tjxsdclyxxs_"+dm;
// 	var zqid="tjxsdcl_"+dm;
// 	map["render"] = function (data, searchData) {
//
// 		if (data){
// 			$("#weeklyStatis_jsrq #"+id+"yxxs").val(searchData.zqs[yxxid]);
// 			for (var i = 0; i < data.length; i++) {
// 				var table="<table id='tab2'>";
// 				table+="<thead>";
// 				table+="<tr>";
// 				table+="<th >平台</th>";
// 				table+="<th >指标</th>";
// 				table+="<th >达成率</th>";
// 				table+="<th >GAP</th>";
// 				table+="</tr>";
// 				table+="</thead>";
// 				var color="";
// 				for (var j = 0; j < data.length; j++) {
// 					if (j+1!==data.length){
// 						color = "#FFFFFF";
// 						if (j % 2 == 0) {
// 							color = "#FCE4D6";
// 						}
// 					}else {
// 						color="#F4B084";
// 					}
// 					table+="<tbody>"
// 					table+="<tr>";
// 					table+="<td  style='background-color:" + color + "!important'>"+data[j].mc+"</td>";
// 					table+="<td style='background-color:" + color + "!important'>"+data[j].zb+"</td>"
// 					table+="<td style='background-color:" + color + "!important'>"+data[j].dcl+"</td>"
// 					table+="<td style='background-color:" + color + "!important'>"+data[j].gap+"</td>"
// 					table+="</tr>";
// 					table+="</tbody>"
// 				}
// 				table+="</table>";
// 			}
// 			$("#weeklyStatis_jsrq #echarts_week_lead_tjxsdcl_" + dm).html("");
// 			$("#weeklyStatis_jsrq #tjsj_tjxsdcl_" + dm).html("");
// 			$("#weeklyStatis_jsrq #echarts_week_lead_tjxsdcl_" + dm).append(table)
// 			$("#weeklyStatis_jsrq #tjsj_tjxsdcl_" + dm).append(searchData.zqs[zqid]+"-"+searchData.zqs[yxxid]);
// 			if (data.length == 0){
// 				$("#weeklyStatis_jsrq #tjsj_tjxsdcl_" + dm).html("");
// 				$("#weeklyStatis_jsrq #echarts_week_lead_"+ dm).html("");
// 			}
// 		}
// 	};
// 	flModels.push(map);
// });
//加载统计数据
var loadWeekLeadStatis = function(){
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"jcxmnum",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				var dataxAxis= new Array();
				var datasf=new Array();
				var databsf=new Array();
				var datafq=new Array();
				var datahj=new Array();
				var maxY=0;
				var intval;
				var tmp_y=0;
				var sfnum=0;
				var bsfnum=0;
				var fqnum=0;
				if(data &&this.chart){
					$("#weeklyStatis_jsrq #jcxmnumyxxs" ).val(searchData.zqs.jcxmnumyxxs);
					for (var i = 0; i < data.length; i++) {
						if(data[i].rq.length>8){
							dataxAxis.push(data[i].rq.substring(5,10));
						}else{
							dataxAxis.push(data[i].rq);
						}
						if(data[i].fqnum==0)
							datafq.push('');
						else
							datafq.push(data[i].fqnum);
						if(data[i].bsfnum==0)
							databsf.push('');
						else
							databsf.push(data[i].bsfnum);
						if(data[i].sfnum==0)
							datasf.push('');
						else
							datasf.push(data[i].sfnum);

						datahj.push(parseInt(data[i].fqnum+data[i].bsfnum+data[i].sfnum))
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
							subtext : searchData.zqs.jcxmnum +"-"+searchData.zqs.jcxmnumyxxs,
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
		{
			id:"ybxxType",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				var dataxAxis= new Array();
				var dataDNA=new Array();
				var dataRNA=new Array();
				var dataDR=new Array();
				var dataR=new Array();
				var dataO=new Array();
				var dataT=new Array();
				var dataHj=new Array();
				var maxY=0;
				var DRnum=new Array();
				var DNAnum=new Array();
				var RNAnum=new Array();
				var Rnum=new Array();
				var Onum=new Array();
				var Tnum=new Array();
				var intval;
				if(data &&this.chart){
					$("#weeklyStatis_jsrq #ybxxTypeyxxs" ).val(searchData.zqs.ybxxTypeyxxs);
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
									}else if(data[j].jcxm=="R"){
										Rnum.push(data[j].num);
										if(data[j].num==0){
											dataR.push('');
										}else{
											dataR.push(data[j].num);
										}
									}else if(data[j].jcxm=="Once"){
										Onum.push(data[j].num);
										if(data[j].num==0){
											dataO.push('');
										}else{
											dataO.push(data[j].num);
										}
									}else if(data[j].jcxm=="tNGS"){
										Tnum.push(data[j].num);
										if(data[j].num==0){
											dataT.push('');
										}else{
											dataT.push(data[j].num);
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
									}else if(data[j].jcxm=="R"){
										Rnum.push(data[j].num);
										if(data[j].num==0){
											dataR.push('')
										}else{
											dataR.push(data[j].num)
										}
									}else if(data[j].jcxm=="Once"){
										Onum.push(data[j].num);
										if(data[j].num==0){
											dataO.push('');
										}else{
											dataO.push(data[j].num);
										}
									}else if(data[j].jcxm=="tNGS"){
										Tnum.push(data[j].num);
										if(data[j].num==0){
											dataT.push('');
										}else{
											dataT.push(data[j].num);
										}
									}
								}
							}
						}
					}
					var Ydata=new Array();
					var DRsum = 0;
					var DNAsum = 0;
					var RNAsum = 0;
					var Rsum = 0;
					var Osum = 0;
					var Tsum = 0;
					for (var i = 0; i < DRnum.length; i++) {
						DRsum = DRsum + DRnum[i];
						DNAsum = DNAsum + DNAnum[i];
						RNAsum = RNAsum + RNAnum[i];
						Rsum = Rsum + Rnum[i];
						Osum = Osum + Onum[i];
						Tsum = Tsum + Tnum[i];
						if((parseInt(DRnum[i])+parseInt(DNAnum[i])+ parseInt(RNAnum[i])+ parseInt(Rnum[i])+ parseInt(Onum[i])+ parseInt(Tnum[i]))>maxY){
							maxY=parseInt(DRnum[i])+parseInt(DNAnum[i])+ parseInt(RNAnum[i])+ parseInt(Rnum[i])+ parseInt(Onum[i])+ parseInt(Tnum[i]);
						}
						dataHj.push(parseInt(DRnum[i])+parseInt(DNAnum[i])+ parseInt(RNAnum[i])+ parseInt(Rnum[i]) + parseInt(Onum[i])+ parseInt(Tnum[i]));
					}
					if (DNAsum != 0)
						Ydata.push('DNA')
					if (RNAsum != 0)
						Ydata.push('RNA')
					if (DRsum != 0)
						Ydata.push('D+R')
					if (Rsum != 0)
						Ydata.push('R')
					if (Tsum != 0)
						Ydata.push('tNGS')
					if (Osum != 0)
						Ydata.push('Once')
					maxY= parseInt(maxY) + 6 - parseInt(maxY%6)
					intval=maxY/6
					var pieoption = {
						title : {
							subtext : searchData.zqs.ybxxType  +"-"+searchData.zqs.ybxxTypeyxxs,
							x : 'left'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
							data: Ydata,
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
							},{
								name: 'R',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: dataR
							},{
								name: 'tNGS',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: dataT
							},{
								name: 'Once',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: dataO
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
								data: dataHj
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
		{
			id:"qgqs",
			chart: null,
			el: null,
			render:function(data,searchData){
				var dataxAxis= new Array();//横坐标
				var dataDls=new Array();//cso的值数组
				var dataDsf=new Array();//第三方的值数组
				var dataZx=new Array();//直销的值数组
				var datahj = new Array();//合计的值数组
				var hbzzDls = new Array();//cso的环比增长值数组（环比增长，（本月值-上个月值）/上个月值*100%
				var hbzzDsf = new Array();//第三方的环比增长值数组
				var hbzzZx = new Array();//直销的环比增长值数组
				var hbzzhj = new Array();//合计的环比增长值数组
				var tempHbzz = new Array();//存直销、cso、第三方的环比百分值，用以计算最大环比数值
				var maxY=0;
				var maxBL=0;
				var minBL=0;
				var intval;
				var line_intval
				if(data &&this.chart){
					$("#weeklyStatis_jsrq #qgqsyxxs" ).val(searchData.zqs.qgqsyxxs);
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
						var sum=0;
						for (var j = 0; j < data.length; j++) {
							if(data[j].rq.length>8){
								var date=data[j].rq.substring(5,10);
								if(dataxAxis[i]==date){
									if(data[j].hbflmc=="直销"){
										sum+=parseInt(data[j].zxnum);
										dataZx.push(data[j].zxnum);
									}else if(data[j].hbflmc=="第三方"){
											dataDsf.push(data[j].dsfnum);
										sum+=parseInt(data[j].dsfnum);
									}else if(data[j].hbflmc=="CSO"){
										dataDls.push(data[j].dlsnum);
										sum+=parseInt(data[j].dlsnum);
									}
								}
							}else{
								if(dataxAxis[i]==data[j].rq){
									if(data[j].hbflmc=="直销"){
										dataZx.push(data[j].zxnum);
										sum+=parseInt(data[j].zxnum);
									}else if(data[j].hbflmc=="第三方"){
											dataDsf.push(data[j].dsfnum);
										sum+=parseInt(data[j].dsfnum);
									}else if(data[j].hbflmc=="CSO"){
											dataDls.push(data[j].dlsnum);
										sum+=parseInt(data[j].dlsnum);
									}
								}
							}
						}
						if (dataDls.length<i+1){
							dataDls.push(0)
						}
						if (dataZx.length<i+1){
							dataZx.push(0)
						}
						if (dataDsf.length<i+1){
							dataDsf.push(0)
						}
						datahj.push(sum);
					}
					for(var i = 0; i < datahj.length; i++){
						var temp = 0;
						if (i==0){
							hbzzDls.push(0);
							hbzzDsf.push(0);
							hbzzZx.push(0);
							hbzzhj.push(0);
							tempHbzz.push(0);
						}else{
							if (dataDls[i-1] ==0 ){
								hbzzDls.push( 0 );
							}else {
								// hbzzDls.push( Math.round((dataDls[i]-dataDls[i-1])/dataDls[i-1])*100 );
								// tempHbzz.push( Math.round((dataDls[i]-dataDls[i-1])/dataDls[i-1])*100 );
								hbzzDls.push( Math.round((dataDls[i]-dataDls[i-1])/dataDls[i-1]*100) );
								tempHbzz.push( Math.round((dataDls[i]-dataDls[i-1])/dataDls[i-1]*100) );
							}
							if (dataDsf[i-1] ==0 ){
								hbzzDsf.push( 0 );
							}else{
								// hbzzDsf.push( Math.round((dataDsf[i]-dataDsf[i-1])/dataDsf[i-1])*100 );
								// tempHbzz.push( Math.round((dataDsf[i]-dataDsf[i-1])/dataDsf[i-1])*100 );
								hbzzDsf.push( Math.round((dataDsf[i]-dataDsf[i-1])/dataDsf[i-1]*100) );
								tempHbzz.push( Math.round((dataDsf[i]-dataDsf[i-1])/dataDsf[i-1]*100) );
							}
							if (dataZx[i-1] ==0 ){
								hbzzZx.push(  0 );
							}else{
								// hbzzZx.push(  Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1])*100 );
								// tempHbzz.push( Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1])*100 );
								hbzzZx.push(  Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1]*100) );
								tempHbzz.push( Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1]*100) );
							}
							if (datahj[i-1] ==0 ){
								hbzzhj.push(  0 );
							}else{
								// hbzzZx.push(  Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1])*100 );
								// tempHbzz.push( Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1])*100 );
								hbzzhj.push(  Math.round((datahj[i] -datahj[i-1])/datahj[i-1]*100) );
								tempHbzz.push( Math.round((datahj[i] -datahj[i-1])/datahj[i-1]*100) );
							}
						}
					}
					//算最大环比值
					var t_minBL = Math.min.apply(null,tempHbzz);
					var t_maxBL = Math.max.apply(null,tempHbzz);
					minBL = t_minBL;
					maxBL = t_maxBL;
					for (var i = 0; i < dataDls.length; i++) {
						var number=0;
						if (dataDls[i]){
							number+=dataDls[i];
						}
						if (dataDsf[i]){
							number+=dataDsf[i];
						}
						if (dataZx[i]){
							number+=dataZx[i];
						}
						if(parseInt(number)>maxY){
							maxY=parseInt(dataDls[i]+dataDsf[i]+dataZx[i]);
						}
					}
					maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
					intval=maxY/4
					maxBL = parseInt(maxBL) + 4 - parseInt(maxBL%4);
					line_intval=(maxBL-minBL)/4;
					var pieoption = {
						title : {
							subtext : searchData.zqs.qgqszqs +"-"+searchData.zqs.qgqsyxxs,
							x : 'left',
							y: '-13'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
							data: ['直销', '第三方','CSO'],
							textStyle: {
								color: '#3E9EE1'
							},
							y:12
						},
						grid: {
							left: '3%',
							right: '4%',
							top:80,
							height:200,
							containLabel: true
						},
						yAxis:  [{
							name: '',
							type: 'value',
							min: 0,
							max: maxY,
							interval: intval
						}],
						xAxis: {
							type: 'category',
							data: dataxAxis
						},
						//折线切换
						toolbox: {
							show : true,
							right: 30,
							top: -13,
							feature: {
								myTool1: {
									show: true,
									title: '切换环比图',
									icon: 'image:///images/huanbi.jpg',
									onclick: function (){
										qgqs_lineFlag = true;
										clickLeadEcharts(_renderArr,null,null,"qgqs",null,data,searchData);
									}
								}
							}
						},
						series: [
							{
								name: '直销',
								type: 'bar',
								stack: '总量',
								barGap: 0,
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								// itemStyle:{
								// 	color: 'rgb(249,174,0)',
								// },
								data: dataZx
							},
							{
								name: '第三方',
								type: 'bar',
								stack: '总量',
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								// itemStyle:{
								// 	color: 'rgba(209, 95, 126)',
								// },
								data: dataDsf
							},
							{
								name: 'CSO',
								type: 'bar',
								stack: '总量',
								// itemStyle:{
								// 	color: '#8e0000',
								// },
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: dataDls
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
							}
						]
					};
					var lineItem = [];
					lineItem.push({
						name:'直销',
						type:'line',
						itemStyle: {
							// color: 'rgb(249,174,0)',
							normal: {
								label: {
									show: true, //开启显示
									position: 'top', //在上方显示
									formatter:"{c}%",//在上方显示
									textStyle: { //数值样式
										fontSize: 12
									}
								},
								formatter: "{c}%"
							}
						},
						data:hbzzZx
					});
					lineItem.push({
						name:'第三方',
						type:'line',
						itemStyle: {
							// color: 'rgba(209, 95, 126)',
							normal: {
								label: {
									show: true, //开启显示
									position: 'top', //在上方显示
									formatter:"{c}%",//在上方显示
									textStyle: { //数值样式
										fontSize: 12
									}
								},
								formatter: "{c}%"
							}
						},
						data:hbzzDsf
					});
					lineItem.push({
						name:'CSO',
						type:'line',
						itemStyle: {
							// color: '#8e0000',
							normal: {
								label: {
									show: true, //开启显示
									position: 'top', //在上方显示
									formatter:"{c}%",//在上方显示
									textStyle: { //数值样式
										fontSize: 12
									}
								},
								formatter: "{c}%"
							}
						},
						data:hbzzDls
					});
					lineItem.push({
						name: '合计',
						type: 'line',
						itemStyle: {
							color: '#8e0000',
							normal: {
								label: {
									show: true, //开启显示
									position: 'top', //在上方显示
									formatter:"{c}%",//在上方显示
									textStyle: { //数值样式
										fontSize: 12
									}
								},
								formatter: "{c}%"
							}
						},
						data: hbzzhj
					});
					var pieoptionLine = {
						title : {
							subtext : searchData.zqs.qgqszqs +"-"+searchData.zqs.qgqsyxxs,
							x : 'left',
							y:'-13'
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
							data: ['直销', '第三方','CSO','合计'],
							textStyle: {
								color: '#3E9EE1'
							},
							y:12
						},
						grid: {
							left: '3%',
							right: '4%',
							top:80,
							height:200,
							containLabel: true
						},
						yAxis:  [
							{
								name: '环比',
								type: 'value',
								min: minBL,
								max: maxBL,
								interval: line_intval,
								axisLabel:{
									formatter: '{value}%'
								}
							}
						],
						xAxis: {
							type: 'category',
							data: dataxAxis,
						},
						//柱状和折线切换
						toolbox: {
							show : true,
							right: 30,
							top: -13,
							feature: {
								myTool1: {
									show: true,
									title: '切换柱状图',
									icon: 'image:///images/bar.jpg',
									onclick: function (){
										qgqs_lineFlag = false;
										clickLeadEcharts(_renderArr,null,null,"qgqs",null,data,searchData);
									}
								}
							}
						},
						series: lineItem
					};
					this.chart.clear();
					if (qgqs_lineFlag){
						this.chart.setOption(pieoptionLine);
					}else{
						this.chart.setOption(pieoption);
					}
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(){
						//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
					});
				}

			}

		},{
			id:"hbflcsszb",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				var namedata = new Array();
				$("#weeklyStatis_jsrq #hbflcsszbyxxs").val(searchData.zqs.hbflcsszbyxxs);
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].num,name:data[i].yxx});
						namedata.push(data[i].yxx);
					}
				}
				var pieoption = {
					title : {
						subtext : searchData.zqs.hbflcsszbzqs+"-"+searchData.zqs.hbflcsszbyxxs,
						x : 'left',
						y: 10
					},
					tooltip : {
						trigger: 'item',
						formatter : "{b} : {d}% / {c} "
					},
					//color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
					// legend: {
					// 	orient: 'vertical',
					// 	left: 'left',
					// 	top:'20',
					// 	data: namedata
					// },
					series : [
						{
							name: '个数',
							type: 'pie',
							radius: ['0%', '25%'],
							startAngle:45,
							data:seriesData,
							label: {
								normal: {
									formatter : "{b}\n{d}%/{c}"
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
			id:"sjqfcsszb",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				$("#weeklyStatis_jsrq #sjqfcsszbyxxs").val(searchData.zqs.sjqfcsszbyxxs);
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].num,name:data[i].sjqfmc});
					}
				}
				var pieoption = {
					title : {
						subtext : searchData.zqs.sjqfcsszbzqs+"-"+searchData.zqs.sjqfcsszbyxxs,
						x : 'left',
						y: 10
					},
					tooltip : {
						trigger: 'item',
						formatter : "{b} : {d}% / {c} "
					},
					calculable : false,//是否可拖拽
					series : [
						{
							type : 'pie',
							radius : ['0%','25%'],
							center : [ '50%', '55%' ],
							startAngle:45,
							itemStyle : {
								normal : {
									label : {
										formatter : "{b}\n{d}%/{c}"
									},
									labelLine : {show : true}
								}
							},
							data : seriesData
						}]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件

				this.chart.on("click",function(param){
				});
			}
		},{
			id:"xsxzcssqs",
			chart:null,
			el:null,
			render:function(data,searchData){
				$("#weeklyStatis_jsrq #xsxzcssqsyxxs").val(searchData.zqs.xsxzcssqsyxxs);
				var prerq = "";
				var dataxAxis= new Array();
				var kydata=new Array();
				var rydata=new Array();
				var tjdata=new Array();
				var dsfdata=new Array();
				var zxdata=new Array();
				var csodata=new Array();
				var hjdata=new Array();
				var maxY=0;
				var intval;
				var num=0;
				var line_maxY=0;
				var line_intval;
				var line_minY=0;
				var line_kydata=new Array();
				var line_rydata=new Array();
				var line_tjdata=new Array();
				var line_dsfdata=new Array();
				var line_zxdata=new Array();
				var line_csodata=new Array();
				if(data!=null){
					for(var i=0;i<data.length;i++){
						if (!dataxAxis.contains(data[i].rq)){
							dataxAxis.push(data[i].rq)
						}
						if (parseInt(data[i].num)>maxY){
							maxY = parseInt(data[i].num);
						}
						if("科研"==data[i].sjqfmc){
							kydata.push(parseInt(data[i].num));
							line_kydata.push(data[i].bl);
						}else if("入院"==data[i].sjqfmc){
							rydata.push(parseInt(data[i].num));
							line_rydata.push(data[i].bl);
						}else if("特检"==data[i].sjqfmc){
							tjdata.push(parseInt(data[i].num));
							line_tjdata.push(data[i].bl);
						}else if("第三方实验室"==data[i].sjqfmc){
							dsfdata.push(parseInt(data[i].num));
							line_dsfdata.push(data[i].bl);
						}else if("直销"==data[i].sjqfmc){
							zxdata.push(parseInt(data[i].num));
							line_zxdata.push(data[i].bl);
						}else if("CSO"==data[i].sjqfmc){
							csodata.push(parseInt(data[i].num));
							line_csodata.push(data[i].bl);
						}
						if(line_maxY < parseInt(data[i].bl))
							line_maxY = parseInt(data[i].bl);
						if(line_minY > parseInt(data[i].bl)&&parseInt(data[i].bl)<0)
							line_minY = parseInt(data[i].bl);
					}
				}
				maxY= parseInt(maxY) + 4 - parseInt(maxY%4)
				intval=maxY/4
				line_maxY= parseInt(line_maxY) + 4 - parseInt(line_maxY%4)
				line_intval=line_maxY/4
				var pieoption = {
					title : {
						subtext : searchData.zqs.xsxzcssqszqs+"-"+searchData.zqs.xsxzcssqsyxxs,
						x : 'left',
						y: '-13'
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
						data:  ['科研', '入院','特检','第三方实验室','直销','CSO'],
						textStyle: {
							color: '#3E9EE1'
						},
						y:12
					},
					toolbox: {
						show : true,
						right: 30,
						top: -13,
						feature : {
							myTool1: {
								show: true,
								title: '切换环比图',
								icon: 'image:///images/huanbi.jpg',
								onclick: function (){
									xsxzcssqs_hbbj=true;
									clickLeadEcharts(_renderArr,null,null,"xsxzcssqs",null,data,searchData);
								}
							}
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
						axisLabel: {
							formatter: '{value}'
						}
					}],
					series: [
						{
							name:'科研',
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
							data:kydata
						}, {
							name:'入院',
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
							data:rydata
						}, {
							name:'特检',
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
							data:tjdata
						},  {
							name:'第三方实验室',
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
							data:dsfdata
						},  {
							name:'直销',
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
							data:zxdata
						},   {
							name:'CSO',
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
							data:csodata
						}
					]
				};
				var names = [];
				names.push({
					name:'科研',
					type:'line',
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								formatter:"{c}%",
								textStyle: { //数值样式
									fontSize: 12
								},
							},
							formatter: "{c}%"
						}
					},
					data:line_kydata
				})
				names.push({
					name:'入院',
					type:'line',
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								formatter:"{c}%",
								textStyle: { //数值样式
									fontSize: 12
								},
							},
							formatter: "{c}%"
						}
					},
					data:line_rydata
				})
				names.push({
					name:'特检',
					type:'line',
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								formatter:"{c}%",
								textStyle: { //数值样式
									fontSize: 12
								},
							},
							formatter: "{c}%"
						}
					},
					data:line_tjdata
				})
				names.push({
					name:'第三方实验室',
					type:'line',
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								formatter:"{c}%",
								textStyle: { //数值样式
									fontSize: 12
								},
							},
							formatter: "{c}%"
						}
					},
					data:line_dsfdata
				})
				names.push({
					name:'直销',
					type:'line',
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								formatter:"{c}%",
								textStyle: { //数值样式
									fontSize: 12
								},
							},
							formatter: "{c}%"
						}
					},
					data:line_zxdata
				})
				names.push({
					name:'CSO',
					type:'line',
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								formatter:"{c}%",
								textStyle: { //数值样式
									fontSize: 12
								},
							},
							formatter: "{c}%"
						}
					},
					data:line_csodata
				})
				var lineoption = {
					title : {
						subtext : searchData.zqs.xsxzcssqszqs+"-"+searchData.zqs.xsxzcssqsyxxs,
						x : 'left',
						y:'-13'
					},
					tooltip: {
						trigger: 'axis',
						axisPointer : {			// 坐标轴指示器，坐标轴触发有效
							type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
						}
					},
					legend: {
						data:  ['科研', '入院','特检','第三方实验室','直销','CSO'],
						textStyle: {
							color: '#3E9EE1'
						},
						y:12
					},
					toolbox: {
						show : true,
						right: 30,
						top: -13,
						feature : {
							myTool1: {
								show: true,
								title: '切换折线图',
								icon: 'image:///images/bar.jpg',
								onclick: function (){
									xsxzcssqs_hbbj=false;
									clickLeadEcharts(_renderArr,null,null,"xsxzcssqs",null,data,searchData);
								}
							}
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						top:80,
						height:200,
						containLabel: true
					},
					xAxis : [
						{
							type : 'category',
							data:dataxAxis,
						}
					],
					yAxis : [
						{
							type: 'value',
							name: '增长率(%)',
							min: line_minY,
							max: line_maxY,
							interval: line_intval,
							axisLabel: {
								formatter: '{value}'+'%'
							}
						}
					],
					series : names
				};
				this.chart.clear();
				if(xsxzcssqs_hbbj){
					this.chart.setOption(lineoption);
				}else{
					this.chart.setOption(pieoption);
				}

				this.chart.hideLoading();
				//点击事件

				this.chart.on("click",function(param){
				});
			}
		},

		{
			id:"cpqstj",
			chart: null,
			el: null,
			render:function(data,searchData){
				var dataxAxis= new Array();
				var maxYBl =0;
				var maxY ="";
				var intervalBl="";
				var interval="";
				var minYBl="";

				var dataAllNames=new Array();
				if(data&&this.chart){
					$("#weeklyStatis_jsrq #cpqstjyxxs" ).val(searchData.zqs.cpqstjyxxs);
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
					var names = [];
					var namesBl = [];
					for (var i = 0; i < dataxAxis.length; i++) {
						for (var j = 0; j < data.length; j++) {
							if(data[j].rq.length>8){
								var date=data[j].rq.substring(5,10);
								if(dataxAxis[i]==date){
									if (data[j].jcxmmc){
										if (dataAllNames.length>0){
											var flag=true;
											for (var k = 0; k <dataAllNames.length ; k++) {
												if (dataAllNames[k]==data[j].jcxmmc){
													flag=false;
													break
												}
											}
											if (flag==true){
												dataAllNames.push(data[j].jcxmmc);
											}
										}else {
											dataAllNames.push(data[j].jcxmmc);
										}

									}
								}
							}else{
								var date=data[j].rq;
								if(dataxAxis[i]==date){
									if (data[j].jcxmmc){
										if (dataAllNames.length>0){
											var flag=true;
											for (var k = 0; k <dataAllNames.length ; k++) {
												if (dataAllNames[k]==data[j].jcxmmc){
													flag=false;
													break
												}
											}
											if (flag==true){
												dataAllNames.push(data[j].jcxmmc);
											}
										}else {
											dataAllNames.push(data[j].jcxmmc);
										}
									}
								}
							}
						}
					}
					for (var k = 0; k <dataAllNames.length ; k++) {
						var dataSl=[];
						var dataBl=[];
						for (var i = 0; i <dataxAxis.length ; i++) {
							var flag=false;
							for (var j = 0; j < data.length; j++) {
								if (data[j].rq.length>8){
									if (dataAllNames[k]==data[j].jcxmmc&&data[j].rq.substring(5,10)==dataxAxis[i]){
										dataSl.push(data[j].sl);
										dataBl.push(data[j].bl);
										if (data[j].sl>maxY){
											maxY=data[j].sl
										}
										if (Number(data[j].bl)>Number(maxYBl)){
											maxYBl=data[j].bl
										}
										if (Number(data[j].bl)<Number(minYBl)){
											minYBl=data[j].bl
										}
										flag=true;
										break
									}
								}else {
									if (dataAllNames[k]==data[j].jcxmmc&&data[j].rq==dataxAxis[i]){
										dataSl.push(data[j].sl);
										dataBl.push(data[j].bl);
										if (data[j].sl>maxY){
											maxY=data[j].sl
										}
										if (Number(data[j].bl)>Number(maxYBl)){
											maxYBl=data[j].bl
										}
										if (Number(data[j].bl)<Number(minYBl)){
											minYBl=data[j].bl
										}
										flag=true;
										break
									}
								}
							}
							if (flag==false){
								dataSl.push("0");
								dataBl.push("0");
							}
						}
						names.push({
								name:dataAllNames[k],
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
								data:dataSl

							})
						namesBl.push({
							name:dataAllNames[k],
							type:'line',
							itemStyle: {
								normal: {
									label: {
										show: true, //开启显示
										position: 'top',
										formatter:"{c}%",//在上方显示
										textStyle: { //数值样式
											fontSize: 12
										}
									}
								}
							},
							data:dataBl
						})
					}
					maxY= parseInt(maxY) + dataAllNames.length - parseInt(maxY%(dataAllNames.length))
					interval=maxY/(dataAllNames.length);
					maxYBl= parseInt(maxYBl) + dataAllNames.length - parseInt(maxYBl%(dataAllNames.length))
					intervalBl=maxYBl/(dataAllNames.length);
					if (minYBl==""){
						minYBl=0;
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.cpqstjzqs +"-"+searchData.zqs.cpqstjyxxs,
							x : 'left',
							y:'-13'
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
							data:dataAllNames,
							textStyle: {
								color: '#3E9EE1'
							},
							y:12
						},
						toolbox: {
							show : true,
							right: 20,
							top: -13,
							feature : {
								myTool1: {
									show: true,
									title: '切换环比图',
									icon: 'image:///images/huanbi.jpg',
									onclick: function (){
										cpqstj_hbbj=true;
										clickLeadEcharts(_renderArr,null,null,"cpqstj",null,data,searchData);
									}
								}
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							top:80,
							height:200,
							containLabel: true
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
								name: '',
								min: 0,
								max: maxY,
								interval: interval,
								axisLabel: {
									formatter: '{value}'
								}
							}
						],
						series : names
					};
					var lineoption = {
						title : {
							subtext : searchData.zqs.cpqstjzqs +"-"+searchData.zqs.cpqstjyxxs,
							x : 'left',
							y:'-13'
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
							data:dataAllNames,
							textStyle: {
								color: '#3E9EE1'
							},
							y:12
						},
						toolbox: {
							show : true,
							right: 20,
							top: -13,
							feature : {
								myTool1: {
									show: true,
									title: '切换折线图',
									icon: 'image:///images/line.jpg',
									onclick: function (){
										cpqstj_hbbj=false;
										clickLeadEcharts(_renderArr,null,null,"cpqstj",null,data,searchData);
									}
								}
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							top:80,
							height:200,
							containLabel: true
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
								name: '增长率(%)',
								max: maxYBl,
								min:minYBl,
								interval: intervalBl,
								axisLabel: {
									formatter: '{value} %'
								}
							}
						],
						series : namesBl
					};
					this.chart.clear();
					if(cpqstj_hbbj){
						this.chart.setOption(lineoption);
					}else{
						this.chart.setOption(pieoption);
					}
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
					});
				}
			}
		},


		{
			id:"ptqstj",
			chart: null,
			el: null,
			render:function(data,searchData){
				var dataxAxis= new Array();
				var maxY ="";
				var interval="";
				var intervalBl="";
				var maxYBl=0;
				var minYBl="";
				var dataAllNames=new Array();
				if(data&&this.chart){
					$("#weeklyStatis_jsrq #ptqstjyxxs" ).val(searchData.zqs.ptqstjyxxs);
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
					var names = [];
					var namesBl = [];
					for (var i = 0; i < dataxAxis.length; i++) {
						for (var j = 0; j < data.length; j++) {
							if(data[j].rq.length>8){
								var date=data[j].rq.substring(5,10);
								if(dataxAxis[i]==date){
									if (data[j].ptmc){
										if (dataAllNames.length>0){
											var flag=true;
											for (var k = 0; k <dataAllNames.length ; k++) {
												if (dataAllNames[k]==data[j].ptmc){
													flag=false;
													break
												}
											}
											if (flag==true){
												dataAllNames.push(data[j].ptmc);
											}
										}else {
											dataAllNames.push(data[j].ptmc);
										}

									}
								}
							}else{
								var date=data[j].rq;
								if(dataxAxis[i]==date){
									if (data[j].ptmc){
										if (dataAllNames.length>0){
											var flag=true;
											for (var k = 0; k <dataAllNames.length ; k++) {
												if (dataAllNames[k]==data[j].ptmc){
													flag=false;
													break
												}
											}
											if (flag==true){
												dataAllNames.push(data[j].ptmc);
											}
										}else {
											dataAllNames.push(data[j].ptmc);
										}
									}
								}
							}
						}
					}
					for (var k = 0; k <dataAllNames.length ; k++) {
						var dataSl=[];
						var dataBl=[];
						for (var i = 0; i <dataxAxis.length ; i++) {
							var flag=false;
							for (var j = 0; j < data.length; j++) {
								if (data[j].rq.length>8){
									if (dataAllNames[k]==data[j].ptmc&&data[j].rq.substring(5,10)==dataxAxis[i]){
										dataSl.push(data[j].sl);
										dataBl.push(data[j].bl);
										if (data[j].sl>maxY){
											maxY=data[j].sl
										}
										if (Number(data[j].bl)>Number(maxYBl)){
											maxYBl=data[j].bl
										}
										if (Number(data[j].bl)<Number(minYBl)){
											minYBl=data[j].bl
										}
										flag=true;
										break
									}
								}else {
									if (dataAllNames[k]==data[j].ptmc&&data[j].rq==dataxAxis[i]){
										dataSl.push(data[j].sl);
										dataBl.push(data[j].bl);
										if (data[j].sl>maxY){
											maxY=data[j].sl
										}
										if (Number(data[j].bl)>Number(maxYBl)){
											maxYBl=data[j].bl
										}
										if (Number(data[j].bl)<Number(minYBl)){
											minYBl=data[j].bl
										}
										flag=true;
										break
									}
								}
							}
							if (flag==false){
								dataSl.push("0");
								dataBl.push("0");
							}
						}
						names.push({
							name:dataAllNames[k],
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
							data:dataSl
						})
						namesBl.push({
							name:dataAllNames[k],
							type:'line',
							itemStyle: {
								normal: {
									label: {
										show: true, //开启显示
										position: 'top', //在上方显示
										formatter:"{c}%",//在上方显示
										textStyle: { //数值样式
											fontSize: 12
										}
									}
								}
							},
							data:dataBl
						})
					}
					var maxX=parseInt(maxY) + dataAllNames.length - parseInt(maxY%(dataAllNames.length))
					interval=maxX/(dataAllNames.length);
					maxY= parseInt(maxY) + interval+dataAllNames.length;
					var maxXBl=parseInt(maxYBl) + dataAllNames.length - parseInt(maxYBl%(dataAllNames.length))
					intervalBl=maxXBl/(dataAllNames.length);

					maxYBl= parseInt(maxYBl) + intervalBl+dataAllNames.length;
					if (minYBl==""){
						minYBl=0;
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.ptqstjzqs+"-"+searchData.zqs.ptqstjyxxs,
							x : 'left',
							y:'-13'
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
							data:dataAllNames,
							textStyle: {
								color: '#3E9EE1'
							},
							y:12
						},
						toolbox: {
							show : true,
							right: 20,
							top: -13,
							feature : {
								myTool1: {
									show: true,
									title: '切换环比图',
									icon: 'image:///images/huanbi.jpg',
									onclick: function (){
										ptqstj_hbbj=true;
										clickLeadEcharts(_renderArr,null,null,"ptqstj",null,data,searchData);
									}
								}
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							top:80,
							height:200,
							containLabel: true
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
								name: '',
								min: 0,
								max: maxY,
								interval: interval,
								axisLabel: {
									formatter: '{value}'
								}
							}
						],
						series : names
					};

					var lineoption = {
						title : {
							subtext : searchData.zqs.ptqstjzqs+"-"+searchData.zqs.ptqstjyxxs,
							x : 'left',
							y:'-13'
						},
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							},

						},
						legend: {
							data:dataAllNames,
							textStyle: {
								color: '#3E9EE1'
							},
							y:12
						},
						toolbox: {
							show : true,
							right: 20,
							top: -13,
							feature : {
								myTool1: {
									show: true,
									title: '切换折线图',
									icon: 'image:///images/line.jpg',
									onclick: function (){
										ptqstj_hbbj=false;
										clickLeadEcharts(_renderArr,null,null,"ptqstj",null,data,searchData);
									}
								}
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							top:80,
							height:200,
							containLabel: true
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
								name: '增长率(%)',
								max: maxYBl,
								min:minYBl,
								interval: intervalBl,
								axisLabel: {
									formatter: '{value} %'
								}
							}
						],
						series : namesBl
					};
					this.chart.clear();
					if(ptqstj_hbbj){
						this.chart.setOption(lineoption);
					}else{
						this.chart.setOption(pieoption);
					}
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
					});
				}
			}
		},
		{
			id:"ptywzbtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var seriesData=new Array();
					$("#weeklyStatis_jsrq #ptywzbtjyxxs" ).val(searchData.zqs.ptywzbtjyxxs);
					if(data!=null){
						for (var i = 0; i < data.length; i++) {
							seriesData.push({value:data[i].sl,name:data[i].ptmc});
						}
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.ptywzbtjzqs+"-"+searchData.zqs.ptywzbtjyxxs,
							x : 'left',
							y:'-13'
						},
						// legend: {
						// 	orient: 'vertical',
						// 	left: 'left',
						// 	top:'20',
						// },
						tooltip : {
							trigger: 'item',
							formatter : "{b} : {d}% / {c} "
						},
						calculable : false,//是否可拖拽
						series : [
							{
								type : 'pie',
								radius : ['0%','25%'],
								center : [ '50%', '55%' ],
								startAngle:45,
								itemStyle : {
									normal : {
										label : {
											formatter : "{b}\n{d}%/{c}"
										},
										labelLine : {show : true}
									}
								},
								data : seriesData
							}]
					};
					this.chart.setOption(pieoption,true);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
					});
				}
			}
		},
		{
			id:"cpywzbtj",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var seriesData=new Array();
					$("#weeklyStatis_jsrq #cpywzbtjyxxs" ).val(searchData.zqs.cpywzbtjyxxs);
					if(data!=null){
						for (var i = 0; i < data.length; i++) {
							seriesData.push({value:data[i].sl,name:data[i].jcxmmc});
						}
					}
					var pieoption = {
						title : {
							subtext : searchData.zqs.cpywzbtjzqs+"-"+searchData.zqs.cpywzbtjyxxs,
							x : 'left',
							y: -13
						},
						// legend: {
						// 	orient: 'horizontal',
						// 	left: 'left',
						// 	top:'20',
						// },
						tooltip : {
							trigger: 'item',
							formatter : "{b} : {d}% / {c} "
						},
						calculable : false,//是否可拖拽
						series : [
							{
								type : 'pie',
								radius : ['0%','25%'],
								center : [ '50%', '55%' ],
								startAngle:45,
								itemStyle : {
									normal : {
										label : {
											formatter : "{b}\n{d}%/{c}"
										},
										labelLine : {show : true}
									}
								},
								data : seriesData
							}]
					};
					this.chart.setOption(pieoption,true);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
					});
				}
			}
		},
		/*
		{
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
							formatter : "{b} : \n{c} ({d}%)"
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
		},
*/
		{
			id:"ptzbdcl",
			chart: null,
			el: null,
			render:function(data,searchData) {
				if (data) {
					$("#weeklyStatis_jsrq #ptzbdclsyxxs").val(searchData.zqs.ptzbdclsyxxs);
					for (var i = 0; i < data.length; i++) {
						var table="<table id='tab2'>";
						table+="<thead>";
						table+="<tr>";
						table+="<th >平台</th>";
						// table+="<th '>测试数</th>";
						table+="<th '>m+t/R/目标</th>";
						table+="<th >达成率</th>";
						table+="<th >GAP</th>";
						table+="</tr>";
						table+="</thead>";
						var color="";
						for (var j = 0; j < data.length; j++) {
							if (j+1!==data.length){
								color = "#FFFFFF";
								if (j % 2 == 0) {
									color = "#DDEBF7";
								}
							}else {
								color="#9BC2E6";
							}

							table+="<tbody>"
							table+="<tr>";
							table+="<td style='background-color:" + color + "!important'>"+data[j].ptmc+"</td>";
							// table+="<td style='background-color: " + color + "!important'>"+data[j].sumnum+"</td>"
							table+="<td style='background-color:" + color + "!important'>"+data[j].sumnum+"/"+data[j].rnum+"/"+data[j].sz+"</td>"
							table+="<td style='background-color: " + color + "!important'>"+data[j].dcl+"</td>"
							table+="<td style='background-color: " + color + "!important'>"+data[j].gap+"</td>"
							table+="</tr>";
							table+="</tbody>"
						}
						table+="</table>";
					}
					$("#weeklyStatis_jsrq #tjsj_ptzbdcl").html("");
					$("#weeklyStatis_jsrq #ptzbdcl").html("");
					$("#weeklyStatis_jsrq #tjsj_ptzbdcl").append(searchData.zqs.ptzbdcl+"-"+searchData.zqs.ptzbdclsyxxs)
					$("#weeklyStatis_jsrq #ptzbdcl").append(table);
					if (data.length == 0){
						$("#weeklyStatis_jsrq #tjsj_ptzbdcl").html("");
						$("#weeklyStatis_jsrq #ptzbdcl").html("");
					}
				}else{
					$("#weeklyStatis_jsrq #tjsj_ptzbdcl").html("");
					$("#weeklyStatis_jsrq #ptzbdcl").html("");
				}
			}
		},
		{
			id:"ywzrfz",
			chart: null,
			el: null,
			render:function(data,searchData) {
				if (data) {
					$("#weeklyStatis_jsrq #ywzrfzsyxxs").val(searchData.zqs.ywzrfzsyxxs);
					var ywzrfznum=0;
					var ywzrfznum2=0;
					var ywzr = new Array();
					var ywzr2 = new Array();
					for (var i = 0; i < data.length; i++) {
						if(data[i].zbflcsdm == 'YWZRTJ'){
							ywzr.push(data[i]);
							ywzrfznum++;
						}else{
							ywzr2.push(data[i]);
							ywzrfznum2++;
						}
					}
					if (data.length>0) {
						var table="<table id='tab3'>";
						table+="<thead>";
						table+="<tr>";
						table+="<th >代表</th>";
						table+="<th >m+t/R/指标</th>";
						table+="<th >达成率</th>";
						table+="<th >GAP</th>";
						table+="</tr>";
						table+="</thead>";
						var color="";
						for (var i = 0; i < ywzr.length; i++) {
							if (i+1!==ywzrfznum){
								color = "#FFFFFF";
								if (i % 2 == 0) {
									color = "#DDEBF7";
								}

							}else {
								color="#9BC2E6";
							}
							table+="<tbody>"
							table+="<tr>";
							table+="<td style='background-color:" + color + "!important'>"+ywzr[i].zsxm+"</td>";
							table+="<td style='background-color:" + color + "!important'>"+ywzr[i].sumnum+"/"+ywzr[i].rsumnum+"/"+ywzr[i].sz+"</td>"
							table+="<td style='background-color:" + color + "!important'>"+ywzr[i].dcl+"</td>"
							table+="<td style='background-color:" + color + "!important'>"+ywzr[i].gap+"</td>"
							table+="</tr>";
						}
						for (var i = 0; i < ywzr2.length; i++) {
							if (i+1!==ywzrfznum2){
								color = "#FFFFFF";
								if (i % 2 == 0) {
									color = "#DDEBF7";
								}
							}else {
								color="#9BC2E6";
							}
							table+="<tr>";
							table+="<td style='background-color:" + color + "!important'>"+ywzr2[i].zsxm+"</td>";
							table+="<td style='background-color:" + color + "!important'>"+ywzr2[i].sumnum+"/"+ywzr2[i].rsumnum+"/"+ywzr2[i].sz+"</td>"
							table+="<td style='background-color:" + color + "!important'>"+ywzr2[i].dcl+"</td>"
							table+="<td style='background-color:" + color + "!important'>"+ywzr2[i].gap+"</td>"
							table+="</tr>";
							table+="</tbody>"
						}
						table+="</table>";
						$("#weeklyStatis_jsrq #tjsj_ywzrfz").html("");
						$("#weeklyStatis_jsrq #ywzrfz").html("");
						$("#weeklyStatis_jsrq #tjsj_ywzrfz").append(searchData.zqs.ywzrfz+"-"+searchData.zqs.ywzrfzsyxxs)
						$("#weeklyStatis_jsrq #ywzrfz").append(table);
					}
					if (data.length == 0){
						$("#weeklyStatis_jsrq #tjsj_ywzrfz").html("");
						$("#weeklyStatis_jsrq #ywzrfz").html("");
					}
				}else{
					$("#weeklyStatis_jsrq #tjsj_ywzrfz").html("");
					$("#weeklyStatis_jsrq #ywzrfz").html("");
				}
			}
		},
		// {
		// 	id:"sjdwOfsjysOfks",
		// 	chart:null,
		// 	el:null,
		// 	render:function(data,searchData){
		// 		var table="<table id='tab2'>";
		// 		table+="<thead>";
		// 		table+="<tr>";
		// 		table+="<th class='first_td' >合作对象</th>";
		// 		table+="<th>上周合作</th>";
		// 		table+="<th>本周合作</th>";
		// 		table+="<th>同比增长</th>";
		// 		table+="</tr>";
		// 		table+="</thead>";
		// 		for (var i = 0; i < data.length; i++) {
		// 			table+="<tbody>"
		// 			table+="<tr>";
		// 			table+="<td class='first_td'>"+data[i].hzdw+"</td>";
		// 			table+="<td>"+data[i].before+"</td>"
		// 			table+="<td>"+data[i].now+"</td>"
		// 			table+="<td>"+data[i].more+"</td>"
		// 			table+="</tr>";
		// 			table+="</tbody>"
		// 		}
		// 		table+="</table>";
		// 		$("#weeklyStatis_jsrq #tjsj_sjdwOfsjysOfks").append(searchData.zqs.sjdwOfsjysOfks)
		// 		$("#weeklyStatis_jsrq #sjdwOfsjysOfks").append(table);
		// 	}
		// },
		// {
		// 	id:"ksList",
		// 	chart: null,
		// 	el: null,
		// 	render:function(data,searchData){
		// 		if(data&&this.chart){
		// 			var datalist=new Array();
		// 			for(var i=0;i<data.length;i++){
		// 				datalist.push({value:data[i].num,name:data[i].dwmc});
		// 			}
		// 			var pieoption = {
		// 				title : {
		// 					subtext : searchData.zqs.ksList,
		// 					x : 'left',
		// 					y : 10
		// 				},
		// 				tooltip : {
		// 					trigger : 'item',
		// 					formatter : "{b} : \n{c} ({d}%)"
		// 				},
		// 				calculable : false,//是否可拖拽
		// 				series : [
		// 					{
		// 						type : 'pie',
		// 						radius : ['0%','50%'],
		// 						center : [ '50%', '55%' ],
		// 						startAngle:45,
		// 						itemStyle : {
		// 							normal : {
		// 								label : {
		// 									formatter:"{b} : {c}\n({d}%)"
		// 								},
		// 								labelLine : {show : true}
		// 							}
		// 						},
		// 						data : datalist
		// 					}]
		// 			};
		// 			this.chart.setOption(pieoption);
		// 			this.chart.hideLoading();
		// 			//点击事件
		// 			this.chart.on("click",function(){
		// 				//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
		// 			});
		// 		}
		// 	}
		// },
		// {
		// 	id:"dbtj",
		// 	chart: null,
		// 	el: null,
		// 	render:function(data,searchData){
		// 		var prekyfx = "";
		// 		if(data&&this.chart){
		// 			var dataAxis= new Array();
		// 			var dataseries=new Array();
		// 			for (var i = 0; i < data.length; i++) {
		// 				dataAxis.push(data[i].mc);
		// 				dataseries.push(data[i].num);
		// 			}
		// 			var pieoption = {
		// 				title : {
		// 					subtext : searchData.zqs.dbtj,
		// 					x : 'left',
		// 					y : 10
		// 				},
		// 				tooltip : {
		// 					trigger: 'axis',
		// 					axisPointer : {			// 坐标轴指示器，坐标轴触发有效
		// 						type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
		// 					}
		// 				},
		// 				grid: {
		// 					left: '3%',
		// 					right: '4%',
		// 					bottom: '3%',
		// 					containLabel: true
		// 				},
		// 				xAxis : [
		// 					{
		// 						type : 'category',
		// 						data:dataAxis,
		// 						axisLabel: {
		// 							rotate: -45,
		// 						},
		// 						axisTick: {
		// 							alignWithLabel: true
		// 						},
		// 						axisLine: {
		// 							lineStyle: {
		// 								color: '#000000'
		// 							},
		// 						},
		// 					}
		// 				],
		// 				yAxis : [
		// 					{
		// 						type : 'value',
		// 						axisLine: {
		// 							lineStyle: {
		// 								color: '#000000'
		// 							},
		// 						}
		// 					}
		// 				],
		// 				series : [
		// 					{
		// 						name:'送检数量',
		// 						type:'bar',
		// 						barWidth: '70%',
		// 						data:dataseries,
		// 						itemStyle: {
		// 							normal: {
		// 								label: {
		// 									show: true, //开启显示
		// 									position: 'top', //在上方显示
		// 									textStyle: { //数值样式
		// 										fontSize: 12
		// 									}
		// 								}
		// 							}
		// 						}
		// 					}
		// 				]
		// 			};
		// 			this.chart.setOption(pieoption);
		// 			this.chart.hideLoading();
		// 			//点击事件
		// 			this.chart.on("click",function(param){
		// 				var map ={}
		// 				map["method"]="getSjdwBydb";
		// 				map["db"]=param.name;
		// 				map["jsrqstart"]=searchData.jsrqstart;
		// 				map["jsrqend"]=searchData.jsrqend;
		// 				clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
		// 			});
		// 		}
		// 	}
		// },
		/*{
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
						formatter: "{a} <br/>{b}: \n{c} ({d}%)"
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
					var load_flg=$("#weeklyStatis_jsrq #load_flg").val();
					var url="/ws/statistics/week_PageList_Fqyb_jsrq?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&jsrqstart="+searchData.jsrqstart+"&jsrqend="+searchData.jsrqend+"&jsrqMstart="+searchData.jsrqMstart+"&jsrqMend="+searchData.jsrqMend+"&jsrqYstart="+searchData.jsrqYstart+"&jsrqYend="+searchData.jsrqYend+"&load_flg="+load_flg;
					if(load_flg=="0"){
						dis_toForward(url);
					}else if(load_flg=="1"){
						$("#weeklyStatis_jsrq").load(url);
					}
				});
			}
		},*/
		/*{
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
							y:12
						},
						grid: {
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
		},*/
		/*{
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
						/!*title : {
							subtext : startrq + '~' + endrq,
							x : 'left',
							y : 10
						},*!/
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						/!*legend: {
							data:['送检数量'],
							textStyle: {
								color: '#3E9EE1'
							}
						},
						grid: {
							right: 50
						},*!/
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
		}*/

		{
			id:"rfs",
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
					var datahj=new Array();
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
					for (j = 0; j < datasf.length; j++) {
						datahj.push(parseInt(datasf[j]?datasf[j]:0)+parseInt(databsf[j]?databsf[j]:0)+parseInt(datafq[j]?datafq[j]:0))
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
							subtext : searchData.zqs.rfs,
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
			id: "topDsf20",
			chart: null,
			el: null,
			render: function (data, searchData) {
				var prekyfx = "";
				if (data && this.chart) {
					$("#weeklyStatis_jsrq #topDsf20yxxs" ).val(searchData.zqs.topDsf20yxxs);
					var dataAxis = new Array();
					var dataseries = new Array();
					var sum = 0;
					var len = data.length;
					if (data.length>10){
						len = 10;
					}
					var max = 0;
					for (var i = len - 1; i >= 0; i--) {
						dataAxis.push(data[i].hbmc);
						dataseries.push(data[i].sfnum);
						sum= sum +parseInt(data[i].sfnum);
						if (data[i].sfnum>max){
							max = data[i].sfnum;
						}
					}
					max =Math.round(max * 1.3)
					var pieoption = {
						title : {
							subtext : searchData.zqs.topDsf20+"-"+searchData.zqs.topDsf20yxxs,
							x : 'left',
							y : 10
						},
						tooltip: {
							trigger: 'axis',
							axisPointer: {			// 坐标轴指示器，坐标轴触发有效
								type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function (params) {//弹窗内容
								let relVal = params[0].name;
								//marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
								relVal += '<br/>' + params[0].marker + params[0].seriesName + '  : ' + parseFloat(params[0].value) + '/' + (100 *
									parseFloat(params[0].value) / sum).toFixed(2) + "%";
								return relVal;
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: '3%',
							containLabel: true
						},
						xAxis: [
							{
								type: 'value',
								axisLine: {
									lineStyle: {
										color: '#00b2ee'
									},
								},
								max:max
							}
						],
						yAxis: [
							{
								type: 'category',
								data: dataAxis,
								offset: 10,
								nameTextStyle: {
									fontSize: 10,
									fontcolor: '#00b2ee',
								},
								axisTick: {
									alignWithLabel: true
								},
								axisLine: {
									lineStyle: {
										color: '#00b2ee'
									},
								},
							}
						],
						series: [
							{
								name: '数量',
								type: 'bar',
								barWidth: '70%',
								data: dataseries,
								label: {
									show: true, //开启显示
									position: 'right', //在右方显示
									textStyle: { //数值样式
										fontSize: 12
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%';
										return percent;
									}
								},
								itemStyle: {
									color: 'rgb(77,187,194)'
								},
							}
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click", function (param) {
					});
				}
			}
			},{
			id:"topZx20",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					$("#weeklyStatis_jsrq #topZx20yxxs" ).val(searchData.zqs.topZx20yxxs);
					var dataAxis= new Array();
					var dataseries=new Array();
					var  sum = 0;
					var len = data.length;
					if (data.length>10){
						len = 10;
					}
					var max = 0;
					for (var i =len-1; i >= 0; i--) {
						dataAxis.push(data[i].hbmc);
						dataseries.push(data[i].sfnum);
						sum= sum +parseInt(data[i].sfnum);
						if (data[i].sfnum>max){
							max = data[i].sfnum;
						}
					}
					max =Math.round(max * 1.3)
					var pieoption = {
						title : {
							subtext : searchData.zqs.topZx20+"-"+searchData.zqs.topZx20yxxs,
							x : 'left',
							y : 10
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function (params) {//弹窗内容
								let relVal = params[0].name;
								//marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
								relVal += '<br/>' + params[0].marker + params[0].seriesName + '  : ' + parseFloat(params[0].value) + '/' + (100 *
									parseFloat(params[0].value) / sum).toFixed(2) + "%";
								return relVal;
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
								type : 'value',
								axisLine: {
									lineStyle: {
										color: 'rgb(249,174,0)'
									},
								},
								max:max
							}
						],
						yAxis : [
							{
								type : 'category',
								data:dataAxis,
								offset: 10,
								nameTextStyle: {
									fontSize: 10,
									fontcolor:'rgb(249,174,0)',
								},
								axisTick: {
									alignWithLabel: true
								},
								axisLine: {
									lineStyle: {
										color: 'rgb(249,174,0)'
									},
								},
							}
						],
						series : [
							{
								name:'数量',
								type:'bar',
								barWidth: '70%',
								data:dataseries,
								label: {
									show: true, //开启显示
									position: 'right', //在右方显示
									textStyle: { //数值样式
										fontSize: 12
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%';
										return percent;
									}
								},
								itemStyle: {
									color: 'rgb(249,174,0)'
								},
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
			id:"topCSO20",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					$("#weeklyStatis_jsrq #topCSO20yxxs" ).val(searchData.zqs.topCSO20yxxs);
					var dataAxis= new Array();
					var dataseries=new Array();
					var  sum = 0;
					var len = data.length;
					if (data.length>10){
						len = 10;
					}
					var max = 0;
					for (var i =len-1; i >= 0; i--) {
						dataAxis.push(data[i].hbmc);
						dataseries.push(data[i].sfnum);
						sum= sum +parseInt(data[i].sfnum);
						if (data[i].sfnum>max){
							max = data[i].sfnum;
						}
					}
					max =Math.round(max * 1.3)
					var pieoption = {
						title : {
							subtext : searchData.zqs.topCSO20+"-"+searchData.zqs.topCSO20yxxs,
							x : 'left',
							y : 10
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function (params) {//弹窗内容
								let relVal = params[0].name;
								//marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
								relVal += '<br/>' + params[0].marker + params[0].seriesName + '  : ' + parseFloat(params[0].value) + '/' + (100 *
									parseFloat(params[0].value) / sum).toFixed(2) + "%";
								return relVal;
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
								type : 'value',
								axisLine: {
									lineStyle: {
										color: 'rgb(209,95,126)'
									},
								},
								max:max
							}
						],
						yAxis : [
							{
								type : 'category',
								data:dataAxis,
								offset: 10,
								nameTextStyle: {
									fontSize: 10,
									fontcolor:'rgb(209,95,126)',
								},
								axisTick: {
									alignWithLabel: true
								},
								axisLine: {
									lineStyle: {
										color: 'rgb(209,95,126)'
									},
								},
							}
						],
						series : [
							{
								name:'数量',
								type:'bar',
								barWidth: '70%',
								data:dataseries,
								label: {
									show: true, //开启显示
									position: 'right', //在右方显示
									textStyle: { //数值样式
										fontSize: 12,
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%'+'';
										return percent;
									}
								},
								itemStyle: {
									color:'rgb(209,95,126)',
								},
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
			id:"topRY20",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prekyfx = "";
				if(data&&this.chart){
					$("#weeklyStatis_jsrq #topRY20yxxs" ).val(searchData.zqs.topRY20yxxs);
					var dataAxis= new Array();
					var dataseries=new Array();
					var  sum = 0;
					var len = data.length;
					if (data.length>10){
						len = 10;
					}
					var max = 0;
					for (var i =len-1; i >= 0; i--) {
						dataAxis.push(data[i].dwmc);
						dataseries.push(data[i].sfnum);
						sum= sum +parseInt(data[i].sfnum);
						if (data[i].sfnum>max){
							max = data[i].sfnum;
						}
					}
					max =Math.round(max * 1.3)
					var pieoption = {
						title : {
							subtext : searchData.zqs.topRY20+"-"+searchData.zqs.topRY20yxxs,
							x : 'left',
							y : 10
						},
						tooltip : {
							trigger: 'axis',
							axisPointer : {			// 坐标轴指示器，坐标轴触发有效
								type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function (params) {//弹窗内容
								let relVal = params[0].name;
								//marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
								relVal += '<br/>' + params[0].marker + params[0].seriesName + '  : ' + parseFloat(params[0].value) + '/' + (100 *
									parseFloat(params[0].value) / sum).toFixed(2) + "%";
								return relVal;
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
								type : 'value',
								axisLine: {
									lineStyle: {
										color: 'rgb(209,95,126)'
									},
								},
								max:max
							}
						],
						yAxis : [
							{
								type : 'category',
								data:dataAxis,
								offset: 10,
								axisLabel: {
									formatter: function (value) {
										var maxlength = 7;
										if (value.length > maxlength) {
											return value.substring(0, maxlength) + '...';
										} else {
											return value;
										}
									},
								},
								nameTextStyle: {
									fontSize: 10,
									fontcolor:'rgb(209,95,126)',
								},
								axisTick: {
									alignWithLabel: true
								},
								axisLine: {
									lineStyle: {
										color: 'rgb(209,95,126)'
									},
								},
							}
						],
						series : [
							{
								name:'数量',
								type:'bar',
								barWidth: '70%',
								data:dataseries,
								label: {
									show: true, //开启显示
									position: 'right', //在右方显示
									textStyle: { //数值样式
										fontSize: 12,
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%';
										return percent;
									}
								},
								itemStyle: {
									color:'rgb(209,95,126)',
								},
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
		},
		{
			id: "ksSfcssList",
			chart: null,
			el: null,
			render: function (data, searchData) {
				if (data && this.chart) {
					$("#weeklyStatis_jsrq #ksSfcssListyxxs" ).val(searchData.zqs.ksSfcssListyxxs);
					var datalist = new Array();
					for (var i = 0; i < (data.length<10?data.length:10); i++) {
						datalist.push({value: data[i].cnum, name: data[i].ksmc});
					}
					var pieoption = {
						title: {
							subtext : searchData.zqs.ksSfcssList+"-"+searchData.zqs.ksSfcssListyxxs,
							x: 'left',
							y: 10
						},
						tooltip: {
							trigger: 'item',
							formatter: "{b} : {d}%/\n{c}"
						},
						calculable: false,//是否可拖拽
						series: [
							{
								type: 'pie',
								radius: ['0%', '25%'],
								center: ['50%', '55%'],
								startAngle: 45,
								itemStyle: {
									normal: {
										label: {
											formatter : "{b}\n{d}%/{c}"
										},
										labelLine: {show: true}
									}
								},
								data: datalist
							}]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click", function () {
					});
				}
			}
		},
		{
			id: "yblxSfcssList",
			chart: null,
			el: null,
			render: function (data, searchData) {
				if (data && this.chart) {
					$("#weeklyStatis_jsrq #yblxSfcssListyxxs" ).val(searchData.zqs.yblxSfcssListyxxs);
					var datalist = new Array();
					for (var i = 0; i < (data.length<10?data.length:10); i++) {
						datalist.push({value: data[i].cnum, name: data[i].yblxmc});
					}
					var pieoption = {
						title: {
							subtext : searchData.zqs.yblxSfcssList+"-"+searchData.zqs.yblxSfcssListyxxs,
							x: 'left',
							y: 10
						},
						tooltip: {
							trigger: 'item',
							formatter: "{b} : {d}%/\n{c}"
						},
						calculable: false,//是否可拖拽
						series: [
							{
								type: 'pie',
								radius: ['0%', '25%'],
								center: ['50%', '55%'],
								startAngle: 45,
								itemStyle: {
									normal: {
										label: {
											formatter : "{b}\n{d}%/{c}"
										},
										labelLine: {show: true}
									}
								},
								data: datalist
							}]
					};
					this.chart.hideLoading();
					this.chart.setOption(pieoption);
					//点击事件
					this.chart.on("click", function () {
						//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
					});
				}
			}
		},{
			id:"topHxyxList",
			chart: null,
			el: null,
			render:function(data,searchData) {
				if (data && this.chart) {
					$("#weeklyStatis_jsrq #topHxyxListyxxs" ).val(searchData.zqs.topHxyxListyxxs);
					var category = new Array();
					var barData = new Array();
					var tjData=new Array();
					var kyData=new Array();
					var ryData=new Array();
					var len = data.length;
					if (data.length>10){
						len = 10;
					}
					var  sum = 0;
					for (var i =len-1; i >= 0; i--) {
						category.push(data[i].dwmc);
						barData.push(data[i].zhnum);
						sum= sum +parseInt(data[i].zhnum);
						if (data[i].tjnum==0)
							tjData.push("");
						else
							tjData.push(data[i].tjnum);
						if (data[i].kynum==0)
							kyData.push("");
						else
							kyData.push(data[i].kynum);
						if (data[i].rynum==0)
							ryData.push("");
						else
							ryData.push(data[i].rynum);
						var total = 0;
						for (var j = 0; j < barData.length; j++) {
							total += barData[j];
						}
					}
					var pieoption = {
						title: {
							subtext : searchData.zqs.topHxyxList+"-"+searchData.zqs.topHxyxListyxxs,
							x: 'left',
							y : 10
						},
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								// Use axis to trigger tooltip
								type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
							},
							formatter: function (params) {
								var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
								var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
								var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
								var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
								var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
								var dotHtml6 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:red"></span>'    // 定义第二个数据前的圆点颜色
								// 定义第二个数据前的圆点颜色
								for (let i = 0, l = params.length; i < l; i++) {
									var sum=params[i].dataIndex;
									var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总收费测试数:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
										+ "</br>" + dotHtml4 + ' 特检收费测试数:' + tjData[sum] + "</br>" + dotHtml4 + ' 特检占比:' + (Number((tjData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 科研收费测试数:' + kyData[sum]+ "</br>"
										+ dotHtml5 + ' 科研占比:' + (Number((kyData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>"+ dotHtml6 + ' 入院收费测试数:' + ryData[sum]+ "</br>"
										+ dotHtml6 + ' 入院占比:' + (Number((ryData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
									return result;
								}

							}
							// formatter:"{b} : \n{c} ({d}%)"
						},
						legend: {
							// selected: {
							//     '其他': false//默认“其他”不显示
							// },
							y:35
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: '3%',
							containLabel: true
						},
						xAxis: {
							type: 'value'
						},
						yAxis: {
							type: 'category',
							data: category,
							axisLabel: {
								formatter: function (value) {
									var maxlength = 7;
									if (value.length > maxlength) {
										return value.substring(0, maxlength) + '...';
									} else {
										return value;
									}
								},
							},
						},
						series: [
							{
								name: '特检',
								type: 'bar',
								stack: 'total',
								label: {
									show: true, //开启显示
									position: 'insideBottom', //在右方显示
									textStyle: { //数值样式
										fontSize: 12
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%';
										return percent;
									}
								},
								emphasis: {
									focus: 'series'
								},
								data: tjData
							},
							{
								name: '科研',
								type: 'bar',
								stack: 'total',
								label: {
									show: true, //开启显示
									position: 'insideBottom', //在右方显示
									textStyle: { //数值样式
										fontSize: 12
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%';
										return percent;
									}
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"A:\n{c}({d})%",
								data: kyData
							},
							{
								name: '入院',
								type: 'bar',
								stack: 'total',
								label: {
									show: true, //开启显示
									position: 'insideBottom', //在右方显示
									textStyle: { //数值样式
										fontSize: 12
									},
									formatter: function (params) {//弹窗内容
										var percent =params.data+'/' +Number((params.data / sum) * 100).toFixed(2) + '%';
										return percent;
									}
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"A:\n{c}({d})%",
								data: ryData
							},
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					this.chart.on("click", function () {
					});
				}
			}


		}

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
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = "/ws/statistics/getWeekLeadStatisByJsrq";
		var param ={};
		param["jsrqstart"]= $("#weeklyStatis_jsrq #jsrqstart").val()
		param["jsrqend"]= $("#weeklyStatis_jsrq #jsrqend").val()
		param["jsrqMstart"]= $("#weeklyStatis_jsrq #jsrqMstart").val()
		param["jsrqMend"]= $("#weeklyStatis_jsrq #jsrqMend").val()
		param["jsrqYstart"]= $("#weeklyStatis_jsrq #jsrqYstart").val()
		param["jsrqYend"]= $("#weeklyStatis_jsrq #jsrqYend").val()
		param["zq"]= $("#weeklyStatis_jsrq [name=zq]").val()
		$.ajax({
			type : "post",
			url : _url,
			data:param,
			dataType : "json",
			success : function(datas) {
				if(datas){
					pagedatas = datas;
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
						if(datas[_render.id]&&datas[_render.id]!="searchData"){
							if(datas[_render.id].length==0){
								// $("#echarts_week_lead_"+_render.id).parent().hide();
							}
						}else {
							$("#echarts_week_lead_"+_render.id).parent().hide();
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
//点击展示更多数据
function showMore(id){
	var showData = pagedatas[id];
	//展示数据
	var html = "";
	if ("topDsf20" == id) {
		var sum = 0;
		for (var i = 0; i < showData.length; i++) {
			sum = sum + showData[i].sfnum;
		}
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>合作伙伴</th>' +
			'         <th>收费测试数</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].hbmc + '</td>' +
				'         <td>' + showData[i].sfnum + '</td>' +
				'         <td>' + (Number((showData[i].sfnum / (sum)) * 100).toFixed(2) + '%') + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	}else if("topZx20" == id){
		var sum = 0;
		for (var i = 0; i < showData.length; i++) {
			sum = sum + showData[i].sfnum;

		}
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>合作伙伴</th>' +
			'         <th>收费测试数</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].hbmc + '</td>' +
				'         <td>' + showData[i].sfnum + '</td>' +
				'         <td>' + (Number((showData[i].sfnum / (sum)) * 100).toFixed(2) + '%') + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	}else if("topCSO20" == id){
		var sum = 0;
		for (var i = 0; i < showData.length; i++) {
			sum = sum + showData[i].sfnum;

		}
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>合作伙伴</th>' +
			'         <th>收费测试数</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].hbmc + '</td>' +
				'         <td>' + showData[i].sfnum + '</td>' +
				'         <td>' + (Number((showData[i].sfnum / (sum)) * 100).toFixed(2) + '%') + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	}else if("topRY20" == id){
		var sum = 0;
		for (var i = 0; i < showData.length; i++) {
			sum = sum + showData[i].sfnum;

		}
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>单位名称</th>' +
			'         <th>收费测试数</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].dwmc + '</td>' +
				'         <td>' + showData[i].sfnum + '</td>' +
				'         <td>' + (Number((showData[i].sfnum / (sum)) * 100).toFixed(2) + '%') + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	}else if ("ksSfcssList" == id) {
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>科室</th>' +
			'         <th>测试数</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].ksmc + '</td>' +
				'         <td>' + showData[i].cnum + '</td>' +
				'         <td>' + showData[i].percentage + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	} else if ("yblxSfcssList" == id) {
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>标本类型</th>' +
			'         <th>测试数</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].yblxmc + '</td>' +
				'         <td>' + showData[i].cnum + '</td>' +
				'         <td>' + showData[i].percentage + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	}else if("topHxyxList" == id){
		var sum = 0;
		for (var i = 0; i < showData.length; i++) {
			sum = sum + showData[i].zhnum;
		}
		html += '<table class="table">' +
			'   <thead>' +
			'      <tr>' +
			'         <th>单位名称</th>' +
			'         <th>总和</th>' +
			'         <th>特检</th>' +
			'         <th>科研</th>' +
			'         <th>入院</th>' +
			'         <th>占比</th>' +
			'      </tr>' +
			'   </thead>' +
			'   <tbody>';
		for (let i = 0; i < showData.length; i++) {
			html += '      <tr>' +
				'         <td>' + showData[i].dwmc + '</td>' +
				'         <td>' + showData[i].zhnum + '</td>' +
				'         <td>' + showData[i].tjnum + '</td>' +
				'         <td>' + showData[i].kynum + '</td>' +
				'         <td>' + showData[i].rynum + '</td>' +
				'         <td>' + (Number((showData[i].zhnum / (sum)) * 100).toFixed(2) + '%') + '</td>' +
				'      </tr>';
		}
		html += '   </tbody>' +
			'</table>';
	}

	var dialog = $.dialog({
		title:"展示更多",
		message: html,
		closeButton: true,
		width:"1000px",
	});

}
//点击相应echarts获取相应数据
function clickLeadEcharts(_renderArr,_url,map,id,qhbj,data,searchData){
	if (qhbj){
		for(var i=0; i<_renderArr.length; i++){
			if(id ==  _renderArr[i].id){
				var _render = _renderArr[i];
				_render.render(pagedatas[_render.id]);
			}
		}
	}else {
		for(var i=0; i<_renderArr.length; i++){
			if(id ==  _renderArr[i].id){
				var _render = _renderArr[i];
				if(!_url){
					var _searchData = searchData;
					var _data = data;
					pagedatas[_render.id] = data;
					_render.render(_data,_searchData);
				}else{
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
								pagedatas[_render.id] = datas[_render.id];
								_render.render(_data,_searchData);
							}else{
								//throw "loadClientStatis数据获取异常";
							}
							map['lrsjStart']="";
							map['lrsjEnd']="";
						}
					});
				}
				break;
			}
		}
	}
}

//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
	var url="/ws/statistics/getSjxxStatisByTjAndJsrq";
	var map ={}
	map["access_token"]=$("#ac_tk").val();
	map["jsrqstart"]= $("#weeklyStatis_jsrq #jsrqstart").val()
	map["jsrqend"]= $("#weeklyStatis_jsrq #jsrqend").val()

	$("#weeklyStatis_jsrq  [name='switch']").unbind("click").click(function(e){
		var qhz = $("#weeklyStatis_jsrq #"+e.currentTarget.id).val();
		if (qhz=="1"){
			$("#weeklyStatis_jsrq #"+e.currentTarget.id).val("0");
		}else {
			$("#weeklyStatis_jsrq #"+e.currentTarget.id).val("1");
		}
		var id = e.currentTarget.id.split("_");
		clickLeadEcharts(_renderArr,null,null,id[0],$("#weeklyStatis_jsrq #"+e.currentTarget.id).val());
	});
	//标本情况的年统计按钮
	$("#weeklyStatis_jsrq #rfs5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5q");
		map["method"]="getRfsSjxxYearByRSy";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//标本情况的月统计按钮
	$("#weeklyStatis_jsrq #rfs5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5m");
		map["method"]="getRfsSjxxMonBySy";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//标本情况的周统计按钮
	$("#weeklyStatis_jsrq #rfs5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5w");
		map["method"]="getRfsSjxxWeekBySy";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//标本情况的日统计按钮
	$("#weeklyStatis_jsrq #rfs5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5d");
		map["method"]="getRfsSjxxDayBySy";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//全国周趋势用时的年统计按钮
	$("#weeklyStatis_jsrq #qgqs5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("qgqs5q");
		map["method"]="getQgqsSjxxYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #qgqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'qgqs');
	})
	//全国周趋势用时的月统计按钮
	$("#weeklyStatis_jsrq #qgqs5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("qgqs5m");
		map["method"]="getQgqsSjxxMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #qgqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'qgqs');
	})
	//全国周趋势用时的周统计按钮
	$("#weeklyStatis_jsrq #qgqs5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("qgqs5w");
		map["method"]="getQgqsSjxxWeek";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #qgqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'qgqs');
	})
	//全国周趋势用时的日统计按钮
	$("#weeklyStatis_jsrq #qgqs5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("qgqs5d");
		map["method"]="getQgqsSjxxDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #qgqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'qgqs');
	})

	//产品趋势用时的年统计按钮
	$("#weeklyStatis_jsrq #cpqstj5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("cpqstj5q");
		map["method"]="getCpqstjSjxxYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #cpqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpqstj');
	})
	//产品趋势用时的月统计按钮
	$("#weeklyStatis_jsrq #cpqstj5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("cpqstj5m");
		map["method"]="getCpqstjSjxxMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #cpqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpqstj');
	})
	//产品趋势用时的周统计按钮
	$("#weeklyStatis_jsrq #cpqstj5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("cpqstj5w");
		map["method"]="getCpqstjSjxxWeek";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #cpqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpqstj');
	})
	//产品趋势用时的日统计按钮
	$("#weeklyStatis_jsrq #cpqstj5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("cpqstj5d");
		map["method"]="getCpqstjSjxxDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #cpqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpqstj');
	})

	//平台趋势用时的年统计按钮
	$("#weeklyStatis_jsrq #ptqstj5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("ptqstj5q");
		map["method"]="getPtqstjSjxxYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #ptqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptqstj');
	})
	//平台趋势用时的月统计按钮
	$("#weeklyStatis_jsrq #ptqstj5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("ptqstj5m");
		map["method"]="getPtqstjSjxxMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #ptqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptqstj');
	})
	//平台趋势用时的周统计按钮
	$("#weeklyStatis_jsrq #ptqstj5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("ptqstj5w");
		map["method"]="getPtqstjSjxxWeek";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #ptqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptqstj');
	})
	//平台趋势用时的日统计按钮
	$("#weeklyStatis_jsrq #ptqstj5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("ptqstj5d");
		map["method"]="getPtqstjSjxxDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val();
		map["yxxs"]= $("#weeklyStatis_jsrq #ptqstjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptqstj');
	})


	//检测总次数的年统计按钮
	$("#weeklyStatis_jsrq #tj2q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj2q");
		map["method"]="getSjxxDRByYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["yxxs"]= $("#weeklyStatis_jsrq #jcxmnumyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//检测总次数的月统计按钮
	$("#weeklyStatis_jsrq #tj2m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj2m");
		map["method"]="getSjxxDRByMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["yxxs"]= $("#weeklyStatis_jsrq #jcxmnumyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//检测总次数的周统计按钮
	$("#weeklyStatis_jsrq #tj2w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj2w");
		map["method"]="getSjxxDRByWeek";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["yxxs"]= $("#weeklyStatis_jsrq #jcxmnumyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//检测总次数的日统计按钮
	$("#weeklyStatis_jsrq #tj2d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj2d");
		map["method"]="getSjxxDRByDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["yxxs"]= $("#weeklyStatis_jsrq #jcxmnumyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})

	//收费标本检测项目次数（年）
	$("#weeklyStatis_jsrq #tj4q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj4q");
		map["method"]="getYbxxTypeByYear";
		map["tj"]="year";
		map["yxxs"]= $("#weeklyStatis_jsrq #ybxxTypeyxxs" ).val();
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费标本检测项目次数（月）
	$("#weeklyStatis_jsrq #tj4m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj4m");
		map["method"]="getYbxxTypeByMon";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #ybxxTypeyxxs" ).val();
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费标本检测项目次数（周）
	$("#weeklyStatis_jsrq #tj4w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj4w");
		map["method"]="getYbxxTypeByWeek";
		map["tj"]="day";
		map["yxxs"]= $("#weeklyStatis_jsrq #ybxxTypeyxxs" ).val();
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费标本检测项目次数（日）
	$("#weeklyStatis_jsrq #tj4d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("tj4d");
		map["method"]="getYbxxTypeByDay";
		map["tj"]="day";
		map["yxxs"]= $("#weeklyStatis_jsrq #ybxxTypeyxxs" ).val();
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})

	//科室的圆饼统计图（年）
	$("#weeklyStatis_jsrq #tj6q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6q");
		map["method"]="getKsByYear";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//科室的圆饼统计图（月）
	$("#weeklyStatis_jsrq #tj6m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6m");
		map["method"]="getKsByMon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//科室的圆饼统计图（周）
	$("#weeklyStatis_jsrq #tj6w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6w");
		map["method"]="getKsByWeek";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//科室的圆饼统计图（日）
	$("#weeklyStatis_jsrq #tj6d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6d");
		map["method"]="getKsByDay";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//平台销售指标达成率（年）
	$("#weeklyStatis_jsrq #tj99y").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj99y");
		map["method"]="getPtzbdclByYear";
		map["tj"]="year";
		map["yxxs"]= $("#weeklyStatis_jsrq #ptzbdclsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptzbdcl');
	})
	//平台销售指标达成率（季度）
	$("#weeklyStatis_jsrq #tj99q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj99q");
		map["method"]="getPtzbdclByQuarter";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #ptzbdclsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptzbdcl');
	})
	//平台销售指标达成率（月）
	$("#weeklyStatis_jsrq #tj99m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj99m");
		map["method"]="getPtzbdclByMon";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #ptzbdclsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptzbdcl');
	})
        //平台业务占比指标达成率（年）
	$("#weeklyStatis_jsrq #ptywzbtjy").unbind("click").click(function(){
		initEchartsLeadBtnCss("ptywzbtjy");
		map["method"]="getPtywzbByYear";
		map["tj"]="year";
		map["yxxs"]= $("#weeklyStatis_jsrq #ptywzbtjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptywzbtj');
	})
	//平台业务占比指标达成率（季度）
	$("#weeklyStatis_jsrq #ptywzbtjq").unbind("click").click(function(){
		initEchartsLeadBtnCss("ptywzbtjq");
		map["method"]="getPtywzbByQuarter";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #ptywzbtjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptywzbtj');
	})
	//平台业务占比指标达成率（月）
	$("#weeklyStatis_jsrq #ptywzbtjm").unbind("click").click(function(){
		initEchartsLeadBtnCss("ptywzbtjm");
		map["method"]="getPtywzbByMon";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #ptywzbtjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ptywzbtj');
	})

	//产品业务占比指标达成率（年）
	$("#weeklyStatis_jsrq #cpywzbtjy").unbind("click").click(function(){
		initEchartsLeadBtnCss("cpywzbtjy");
		map["method"]="getCpywzbByYear";
		map["tj"]="year";
		map["yxxs"]= $("#weeklyStatis_jsrq #cpywzbtjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpywzbtj');
	})
	//产品业务占比指标达成率（季度）
	$("#weeklyStatis_jsrq #cpywzbtjq").unbind("click").click(function(){
		initEchartsLeadBtnCss("cpywzbtjq");
		map["method"]="getCpywzbByQuarter";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #cpywzbtjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpywzbtj');
	})
	//产品业务占比指标达成率（月）
	$("#weeklyStatis_jsrq #cpywzbtjm").unbind("click").click(function(){
		initEchartsLeadBtnCss("cpywzbtjm");
		map["method"]="getCpywzbByMon";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #cpywzbtjyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'cpywzbtj');
	})
	//销售渠道类型占比（年）
	$("#weeklyStatis_jsrq #hbfl6y").unbind("click").click(function(){
		initEchartsLeadBtnCss("hbfl6y");
		map["method"]="getHbflcsszbYear";
		map["tj"]="year";
		map["yxxs"]= $("#weeklyStatis_jsrq #hbflcsszbyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'hbflcsszb');
	})
	//销售渠道类型占比（季度）
	$("#weeklyStatis_jsrq #hbfl6q").unbind("click").click(function(){
		initEchartsLeadBtnCss("hbfl6q");
		map["method"]="getHbflcsszbQuarter";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #hbflcsszbyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'hbflcsszb');
	})
	//销售渠道类型占比（月）
	$("#weeklyStatis_jsrq #hbfl6m").unbind("click").click(function(){
		initEchartsLeadBtnCss("hbfl6m");
		map["method"]="getHbflcsszbMon";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #hbflcsszbyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'hbflcsszb');
	})

	//销售性质占比（年）
	$("#weeklyStatis_jsrq #sjqfcsszb_y").unbind("click").click(function(){
		initEchartsLeadBtnCss("sjqfcsszb_y");
		map["method"]="getSjqfcsszbYear";
		map["tj"]="year";
		map["yxxs"]= $("#weeklyStatis_jsrq #sjqfcsszbyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'sjqfcsszb');
	})
	//销售性质占比（季度）
	$("#weeklyStatis_jsrq #sjqfcsszb_q").unbind("click").click(function(){
		initEchartsLeadBtnCss("sjqfcsszb_q");
		map["method"]="getSjqfcsszbQuarter";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #sjqfcsszbyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'sjqfcsszb');
	})
	//销售性质占比（月）
	$("#weeklyStatis_jsrq #sjqfcsszb_m").unbind("click").click(function(){
		initEchartsLeadBtnCss("sjqfcsszb_m");
		map["method"]="getSjqfcsszbMon";
		map["tj"]="mon";
		map["yxxs"]= $("#weeklyStatis_jsrq #sjqfcsszbyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'sjqfcsszb');
	})
	//业务发展部指标达成率（年）
	// $("#weeklyStatis_jsrq #tjywzrfzy").unbind("click").click(function(){
	// 	initEchartsLeadBtnCss("tjywzrfzy");
	// 	map["method"]="getYwzrfzByYear";
	// 	map["tj"]="year";
	// 	map["yxxs"]= $("#weeklyStatis_jsrq #ywzrfzsyxxs" ).val();
	// 	clickLeadEcharts(_renderArr,url,map,'ywzrfz');
	// })
	//业务发展部指标达成率（季度）
	$("#weeklyStatis_jsrq #tjywzrfzq").unbind("click").click(function(){
		initEchartsLeadBtnCss("tjywzrfzq");
		map["method"]="getYwzrfzByQuarter";
		map["tj"]="quarter";
		map["yxxs"]= $("#weeklyStatis_jsrq #ywzrfzsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ywzrfz');
	})
	//业务发展部指标达成率（月）
	// $("#weeklyStatis_jsrq #tjywzrfzm").unbind("click").click(function(){
	// 	initEchartsLeadBtnCss("tjywzrfzm");
	// 	map["method"]="getYwzrfzByMon";
	// 	map["tj"]="mon";
	// 	map["yxxs"]= $("#weeklyStatis_jsrq #ywzrfzsyxxs" ).val();
	// 	clickLeadEcharts(_renderArr,url,map,'ywzrfz');
	// })
	// //合作伙伴的所有统计按钮
	// $("#weeklyStatis_jsrq #tj3q").unbind("click").click(function(){
	// 	initEchartsLeadBtnCss("tj3q");
	// 	map["method"]="getDbByYear";
	// 	map["tj"]="mon";
	// 	clickLeadEcharts(_renderArr,url,map,'dbtj');
	// })
	// //合作伙伴的月统计按钮
	// $("#weeklyStatis_jsrq #tj3m").unbind("click").click(function(){
	// 	initEchartsLeadBtnCss("tj3m");
	// 	map["method"]="getDbByMon";
	// 	clickLeadEcharts(_renderArr,url,map,'dbtj');
	// })
	// //合作伙伴的周统计按钮
	// $("#weeklyStatis_jsrq #tj3w").unbind("click").click(function(){
	// 	initEchartsLeadBtnCss("tj3w");
	// 	map["method"]="getDbByWeek";
	// 	clickLeadEcharts(_renderArr,url,map,'dbtj');
	// })
	// //合作伙伴的日统计按钮
	// $("#weeklyStatis_jsrq #tj3d").unbind("click").click(function(){
	// 	initEchartsLeadBtnCss("tj3d");
	// 	map["method"]="getDbByDay";
	// 	clickLeadEcharts(_renderArr,url,map,'dbtj');
	// })
	//复检情况的所有统计按钮
	$("#weeklyStatis_jsrq #fjq").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjq");
		map["method"]="getFjsqByYear";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况的月统计按钮
	$("#weeklyStatis_jsrq #fjm").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjm");
		map["method"]="getFjsqByMon";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况的周统计按钮
	$("#weeklyStatis_jsrq #fjw").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjw");
		map["method"]="getFjsqByWeek";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况的日统计按钮
	$("#weeklyStatis_jsrq #fjd").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjd");
		map["method"]="getFjsqByDay";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})


	//废弃标本的所有统计按钮
	$("#weeklyStatis_jsrq #fqq").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqq");
		map["method"]="getFqybByYear";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本的月统计按钮
	$("#weeklyStatis_jsrq #fqm").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqm");
		map["method"]="getFqybByMon";
		clickLeadEcharts(_renderArr,url,map,'fqyb');

	})
	//废弃标本的周统计按钮
	$("#weeklyStatis_jsrq #fqw").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqw");
		map["method"]="getFqybByWeek";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本的日统计按钮
	$("#weeklyStatis_jsrq #fqd").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqd");
		map["method"]="getFqybByDay";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//第三方top20排行年统计按钮
	$("#weeklyStatis_jsrq #topDsf20q").unbind("click").click(function(){
		initEchartsLeadBtnCss("topDsf20q");
		map["method"]="getTopDsf20ByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #topDsf20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topDsf20');
	})
	//第三方top20排行月统计按钮
	$("#weeklyStatis_jsrq #topDsf20m").unbind("click").click(function(){
		initEchartsLeadBtnCss("topDsf20m");
		map["method"]="getTopDsf20ByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #topDsf20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topDsf20');
	})
	//第三方top20排行周统计按钮
	$("#weeklyStatis_jsrq #topDsf20w").unbind("click").click(function(){
		initEchartsLeadBtnCss("topDsf20w");
		map["method"]="getTopDsf20ByWeek";
		map["yxxs"]=$("#weeklyStatis_jsrq #topDsf20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topDsf20');
	})
	//第三方top20排行日统计按钮
	$("#weeklyStatis_jsrq #topDsf20d").unbind("click").click(function(){
		initEchartsLeadBtnCss("topDsf20d");
		map["method"]="getTopDsf20ByDay";
		map["yxxs"]=$("#weeklyStatis_jsrq #topDsf20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topDsf20');
	})
	//直销top20排行年统计按钮
	$("#weeklyStatis_jsrq #topZx20q").unbind("click").click(function(){
		initEchartsLeadBtnCss("topZx20q");
		map["method"]="getTopZx20ByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #topZx20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topZx20');
	})
	//直销top20排行月统计按钮
	$("#weeklyStatis_jsrq #topZx20m").unbind("click").click(function(){
		initEchartsLeadBtnCss("topZx20m");
		map["method"]="getTopZx20ByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #topZx20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topZx20');
	})
	//直销top20排行周统计按钮
	$("#weeklyStatis_jsrq #topZx20w").unbind("click").click(function(){
		initEchartsLeadBtnCss("topZx20w");
		map["method"]="getTopZx20ByWeek";
		map["yxxs"]=$("#weeklyStatis_jsrq #topZx20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topZx20');
	})
	//直销top20排行日统计按钮
	$("#weeklyStatis_jsrq #topZx20d").unbind("click").click(function(){
		initEchartsLeadBtnCss("topZx20d");
		map["method"]="getTopZx20ByDay";
		map["yxxs"]=$("#weeklyStatis_jsrq #topZx20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topZx20');
	})
	//CSOtop20排行年统计按钮
	$("#weeklyStatis_jsrq #topCSO20q").unbind("click").click(function(){
		initEchartsLeadBtnCss("topCSO20q");
		map["method"]="getTopCSO20ByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #topCSO20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topCSO20');
	})
	//CSOtop20排行月统计按钮
	$("#weeklyStatis_jsrq #topCSO20m").unbind("click").click(function(){
		initEchartsLeadBtnCss("topCSO20m");
		map["method"]="getTopCSO20ByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #topCSO20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topCSO20');
	})
	//CSOtop20排行周统计按钮
	$("#weeklyStatis_jsrq #topCSO20w").unbind("click").click(function(){
		initEchartsLeadBtnCss("topCSO20w");
		map["method"]="getTopCSO20ByWeek";
		map["yxxs"]=$("#weeklyStatis_jsrq #topCSO20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topCSO20');
	})
	//CSOtop20排行日统计按钮
	$("#weeklyStatis_jsrq #topCSO20d").unbind("click").click(function(){
		initEchartsLeadBtnCss("topCSO20d");
		map["method"]="getTopCSO20ByDay";
		map["yxxs"]=$("#weeklyStatis_jsrq #topCSO20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topCSO20');
	})
	//RYtop20排行日统计按钮
	$("#weeklyStatis_jsrq #topRY20d").unbind("click").click(function(){
		initEchartsLeadBtnCss("topRY20d");
		map["method"]="getTopRY20ByDay";
		map["yxxs"]=$("#weeklyStatis_jsrq #topRY20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topRY20');
	})
	//RYtop20排行年统计按钮
	$("#weeklyStatis_jsrq #topRY20q").unbind("click").click(function(){
		initEchartsLeadBtnCss("topRY20q");
		map["method"]="getTopRY20ByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #topRY20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topRY20');
	})
	//RYtop20排行月统计按钮
	$("#weeklyStatis_jsrq #topRY20m").unbind("click").click(function(){
		initEchartsLeadBtnCss("topRY20m");
		map["method"]="getTopRY20ByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #topRY20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topRY20');
	})
	//RYtop20排行周统计按钮
	$("#weeklyStatis_jsrq #topRY20w").unbind("click").click(function(){
		initEchartsLeadBtnCss("topRY20w");
		map["method"]="getTopRY20ByWeek";
		map["yxxs"]=$("#weeklyStatis_jsrq #topRY20yxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topRY20');
	})
	//核心医院排行日统计按钮
	$("#weeklyStatis_jsrq #topHxyxListd").unbind("click").click(function(){
		initEchartsLeadBtnCss("topHxyxListd");
		map["method"]="getTopHxyxListByDay";
		map["yxxs"]=$("#weeklyStatis_jsrq #topHxyxListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topHxyxList');
	})
	//核心医院排行年统计按钮
	$("#weeklyStatis_jsrq #topHxyxListq").unbind("click").click(function(){
		initEchartsLeadBtnCss("topHxyxListq");
		map["method"]="getTopHxyxListByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #topHxyxListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topHxyxList');
	})
	//核心医院排行月统计按钮
	$("#weeklyStatis_jsrq #topHxyxListm").unbind("click").click(function(){
		initEchartsLeadBtnCss("topHxyxListm");
		map["method"]="getTopHxyxListByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #topHxyxListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topHxyxList');
	})
	//核心医院排行周统计按钮
	$("#weeklyStatis_jsrq #topHxyxListw").unbind("click").click(function(){
		initEchartsLeadBtnCss("topHxyxListw");
		map["method"]="getTopHxyxListByWeek";
		map["yxxs"]=$("#weeklyStatis_jsrq #topHxyxListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'topHxyxList');
	})
		//收费标本类型分布年统计按钮
	$("#weeklyStatis_jsrq #yblxSfcssListy").unbind("click").click(function(){
		initEchartsLeadBtnCss("yblxSfcssListy");
		map["method"]="getChargesDivideByYblxByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #yblxSfcssListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'yblxSfcssList');
	})
	//收费标本科室分布季度统计按钮
	$("#weeklyStatis_jsrq #yblxSfcssListq").unbind("click").click(function(){
		initEchartsLeadBtnCss("yblxSfcssListq");
		map["method"]="getChargesDivideByYblxByQuarter";
		map["yxxs"]=$("#weeklyStatis_jsrq #yblxSfcssListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'yblxSfcssList');
	})
	//收费标本类型分布月统计按钮
	$("#weeklyStatis_jsrq #yblxSfcssListm").unbind("click").click(function(){
		initEchartsLeadBtnCss("yblxSfcssListm");
		map["method"]="getChargesDivideByYblxByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #yblxSfcssListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'yblxSfcssList');
	})
	//收费标本科室分布年统计按钮
	$("#weeklyStatis_jsrq #ksSfcssListy").unbind("click").click(function(){
		initEchartsLeadBtnCss("ksSfcssListy");
		map["method"]="getChargesDivideByKsByYear";
		map["yxxs"]=$("#weeklyStatis_jsrq #ksSfcssListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ksSfcssList');
	})
	//收费标本科室分布季度统计按钮
	$("#weeklyStatis_jsrq #ksSfcssListq").unbind("click").click(function(){
		initEchartsLeadBtnCss("ksSfcssListq");
		map["method"]="getChargesDivideByKsByQuarter";
		map["yxxs"]=$("#weeklyStatis_jsrq #ksSfcssListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ksSfcssList');
	})
	//收费标本科室分布月统计按钮
	$("#weeklyStatis_jsrq #ksSfcssListm").unbind("click").click(function(){
		initEchartsLeadBtnCss("ksSfcssListm");
		map["method"]="getChargesDivideByKsByMon";
		map["yxxs"]=$("#weeklyStatis_jsrq #ksSfcssListyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'ksSfcssList');
	})
	//分类的所有统计按钮
	$("#weeklyStatis_jsrq a[name='tj7q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByYear";
		map["sfflg"]= $("#weeklyStatis_jsrq #sfflg_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_jsrq #zfl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_jsrq #hbmc_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//分类的月统计按钮
	$("#weeklyStatis_jsrq a[name='tj7m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByMon";
		map["sfflg"]= $("#weeklyStatis_jsrq #sfflg_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_jsrq #zfl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_jsrq #hbmc_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//分类的周统计按钮
	$("#weeklyStatis_jsrq a[name='tj7w']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByWeek";
		map["sfflg"]= $("#weeklyStatis_jsrq #sfflg_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_jsrq #zfl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_jsrq #hbmc_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//分类的日统计按钮
	$("#weeklyStatis_jsrq a[name='tj7d']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByDay";
		map["sfflg"]= $("#weeklyStatis_jsrq #sfflg_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis_jsrq #zfl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis_jsrq #hbmc_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//销售达成率（年）
	$("#weeklyStatis_jsrq a[name='tj9y']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getSalesAttainmentRateByYear";
		map["tj"]="year";
		map["qyid"]=$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
		clickLeadEcharts(_renderArr,url,map,'salesAttainmentRate_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//销售达成率（季度）
	$("#weeklyStatis_jsrq a[name='tj9q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getSalesAttainmentRateByQuarter";
		map["tj"]="quarter";
		map["qyid"]=$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
		clickLeadEcharts(_renderArr,url,map,'salesAttainmentRate_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//销售达成率（月）
	$("#weeklyStatis_jsrq a[name='tj9m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getSalesAttainmentRateByMon";
		map["tj"]="mon";
		map["qyid"]=$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
		clickLeadEcharts(_renderArr,url,map,'salesAttainmentRate_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//销售统计平台指标达成率（年）
	// $("#weeklyStatis_jsrq a[name='tjxsdcly']").unbind("click").click(function(e){
	// 	initEchartsLeadBtnCss2(e.currentTarget.id);
	// 	map["method"]="getTjxsdclByYear";
	// 	map["tj"]="year";
	// 	map["zfl"]=$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
	// 	map["yxxs"]= $("#weeklyStatis_jsrq #"+"tjxsdcl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")+"yxxs").val();
	// 	clickLeadEcharts(_renderArr,url,map,'tjxsdcl_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	// })
	//销售统计平台指标达成率（季度）
	$("#weeklyStatis_jsrq a[name='tjxsdclq']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getTjxsdclByQuarter";
		map["tj"]="quarter";
		map["zfl"]=$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
		map["yxxs"]= $("#weeklyStatis_jsrq #"+"tjxsdcl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")+"yxxs").val();
		clickLeadEcharts(_renderArr,url,map,'tjxsdcl_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	})
	//销售统计平台指标达成率（月）
	// $("#weeklyStatis_jsrq a[name='tjxsdclm']").unbind("click").click(function(e){
	// 	initEchartsLeadBtnCss2(e.currentTarget.id);
	// 	map["method"]="getTjxsdclByMon";
	// 	map["tj"]="mon";
	// 	map["zfl"]=$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
	// 	map["yxxs"]= $("#weeklyStatis_jsrq #"+"tjxsdcl_"+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm")+"yxxs").val();
	// 	clickLeadEcharts(_renderArr,url,map,'tjxsdcl_'+$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm"));
	// })
	$("#weeklyStatis_jsrq #xsxzcssqs_d").unbind("click").click(function(e){
		initEchartsLeadBtnCss2("xsxzcssqs_d");
		map["method"]="getXsxzcssqsDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis_jsrq #zq_xsxzcssqs").val();
		map["yxxs"]= $("#weeklyStatis_jsrq #xsxzcssqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'xsxzcssqs');
	})
	$("#weeklyStatis_jsrq #xsxzcssqs_w").unbind("click").click(function(e){
		initEchartsLeadBtnCss2("xsxzcssqs_w");
		map["method"]="getXsxzcssqsWeek";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis_jsrq #zq_xsxzcssqs").val();
		map["yxxs"]= $("#weeklyStatis_jsrq #xsxzcssqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'xsxzcssqs');
	})
	$("#weeklyStatis_jsrq #xsxzcssqs_m").unbind("click").click(function(e){
		initEchartsLeadBtnCss2("xsxzcssqs_m");
		map["method"]="getXsxzcssqsMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis_jsrq #zq_xsxzcssqs").val();
		map["yxxs"]= $("#weeklyStatis_jsrq #xsxzcssqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'xsxzcssqs');
	})
	$("#weeklyStatis_jsrq #xsxzcssqs_q").unbind("click").click(function(e){
		initEchartsLeadBtnCss2("xsxzcssqs_q");
		map["method"]="getXsxzcssqsYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis_jsrq #zq_xsxzcssqs").val();
		map["yxxs"]= $("#weeklyStatis_jsrq #xsxzcssqsyxxs" ).val();
		clickLeadEcharts(_renderArr,url,map,'xsxzcssqs');
	})
	//子类别下拉框改变事件
	$("#weeklyStatis_jsrq select[name='zfl']").unbind("change").change(function(e){
		var zflid = e.currentTarget.id;
		var flid = zflid.split("_")[1];
		var sel_hbmc = $("#weeklyStatis_jsrq #hbmc_"+flid);
		var zfl = e.currentTarget.value;
		$.ajax({
			type:'post',
			url:"/ws/statistics/getWeekLeadStatisPartner",
			cache: false,
			data: {"zfl":zfl,"fl":flid,"access_token":$("#ac_tk").val()},
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

		var statisTitle = $("#weeklyStatis_jsrq #ybxxhead_"+flid+" .bechoosed").attr("title");
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
		zflmap["jsrqstart"]= $("#weeklyStatis_jsrq #jsrqstart").val()
		zflmap["jsrqend"]= $("#weeklyStatis_jsrq #jsrqend").val()
		zflmap["zq"]= $("#weeklyStatis_jsrq #zq_" + flid).val()
		zflmap["sfflg"]= $("#weeklyStatis_jsrq #sfflg_" + flid).val()
		zflmap["hbfl"]= flid
		zflmap["hbzfl"]= $("#weeklyStatis_jsrq #zfl_" + flid).val()
		zflmap["db"]= $("#weeklyStatis_jsrq #hbmc_" + flid).val()
		clickLeadEcharts(_renderArr,url,zflmap,'fltj_'+flid);
	});

	//周期改变事件
	$("#weeklyStatis_jsrq input[name='zq']").unbind("change").change(function(e){
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
		var flid = $("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("dm");
		var dateid = "ybxxhead"+"_"+flid;
		var hz = (($("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("tjm")!=null && $("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("tjm")!="")?"_":"") + flid;
		var method = $("#"+dateid + " #"+$("#weeklyStatis_jsrq #"+dateid+" .bechoosed")[0].id).attr("method")
		map["method"]=method;
		map["zq"]= e.currentTarget.value;
		map["db"]= $("#weeklyStatis_jsrq #hbmc_"+flid).val();
		map["hbzfl"]= $("#weeklyStatis_jsrq #zfl_"+flid).val();
		map["hbfl"]= flid;
		map["sfflg"]= $("#weeklyStatis_jsrq #sfflg_"+flid).val();
		var yxxid=e.currentTarget.id;
		var yxxids=yxxid.split("_");
		if (yxxids.length>0){
			yxxid=yxxids[1]+"yxxs";
			map["yxxs"]=$("#weeklyStatis_jsrq #"+yxxid).val()
		}
		clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq #"+e.currentTarget.id).attr("tjm")+hz);
	});
	$("#weeklyStatis_jsrq  [name='jcxm']").unbind("click").click(function(e){
		var yxxid=e.currentTarget.id+"yxxs";
		var yxxs=$("#weeklyStatis_jsrq #"+yxxid).val();
		if (yxxid.indexOf("hbflcsszbyxxs")!=-1||yxxid.indexOf("sjqfcsszbyxxs")!=-1||yxxid.indexOf("top")!=-1
			||yxxid.indexOf("ptywzbtj")!=-1||yxxid.indexOf("cpywzbtj")!=-1||yxxid.indexOf("yblxSfcssListyxxs")!=-1||yxxid.indexOf("ksSfcssListyxxs")!=-1){
			$.showDialog("/ws/statistics/pagedataScreenContent?bdid="+e.currentTarget.id+"&yxxs="+yxxs+"&flag="+"1",'筛选',ScreenContentConfig);
		}else if(yxxid=="ptzbdclsyxxs"||yxxid=="ywzrfzsyxxs"||yxxid.indexOf("tjxsdcl")!=-1) {
			$.showDialog("/ws/statistics/pagedataScreenContent?bdid="+e.currentTarget.id+"&yxxs="+yxxs+"&flag="+"1"+"&dcl="+"1",'筛选',ScreenContentConfig);
		}else {
			$.showDialog("/ws/statistics/pagedataScreenContent?bdid=" + e.currentTarget.id + "&yxxs=" + yxxs, '筛选', ScreenContentConfig);
		}
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
					var id="ybxxhead_"+$("#weeklyStatis_jsrq_screencontent #bdid").val();
					var yxxs="";
					$("#weeklyStatis_jsrq_screencontent #dyxx input[type='checkbox']").each(function(index){
						if (this.checked){
							yxxs+=","+this.value;
						}
					});
					if (yxxs.length>0){
						yxxs=yxxs.substr(1);
					}
					map["yxxs"]=yxxs;
					if($("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("sjqfcsszb")!=-1){
						if($("#weeklyStatis_jsrq_screencontent #lrsjStart").val()||$("#weeklyStatis_jsrq_screencontent #lrsjEnd").val()){
							map["method"]="getSjqfcsszb";
							map["lrsjStart"]=$("#weeklyStatis_jsrq_screencontent #lrsjStart").val();
							map["lrsjEnd"]=$("#weeklyStatis_jsrq_screencontent #lrsjEnd").val();
						}else{
							var  zqid = document.getElementById("ybxxhead_sjqfcsszb").getElementsByClassName("bechoosed");
							$("#weeklyStatis_jsrq #"+zqid[0].id).val();
							if (zqid[0].name.indexOf("Mon")!=-1){
								map["tj"]="mon";
								map["method"]="getSjqfcsszbMon";
							}else if (zqid[0].name.indexOf("Year")!=-1){
								map["tj"]="year";
								map["method"]="getSjqfcsszbYear";
							}else if (zqid[0].name.indexOf("Quarter")!=-1){
								map["tj"]="quarter";
								map["method"]="getSjqfcsszbQuarter";
							}
						}
						clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val());
					}else if($("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("hbflcsszb")!=-1){
						if($("#weeklyStatis_jsrq_screencontent #lrsjStart").val()||$("#weeklyStatis_jsrq_screencontent #lrsjEnd").val()){
							map["method"]="getHbflcsszb";
							map["lrsjStart"]=$("#weeklyStatis_jsrq_screencontent #lrsjStart").val();
							map["lrsjEnd"]=$("#weeklyStatis_jsrq_screencontent #lrsjEnd").val();
						}else{
							var  zqid = document.getElementById("ybxxhead_hbflcsszb").getElementsByClassName("bechoosed");
							$("#weeklyStatis_jsrq #"+zqid[0].id).val();
							if (zqid[0].name.indexOf("Mon")!=-1){
								map["tj"]="mon";
								map["method"]="getHbflcsszbMon";
							}else if (zqid[0].name.indexOf("Year")!=-1){
								map["tj"]="year";
								map["method"]="getHbflcsszbYear";
							}else if (zqid[0].name.indexOf("Quarter")!=-1){
								map["tj"]="quarter";
								map["method"]="getHbflcsszbQuarter";
							}
						}
						clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val());
					}else{
						var  zqid = document.getElementById(id).getElementsByClassName("bechoosed");
						$("#weeklyStatis_jsrq #"+zqid[0].id).val();
						if (zqid[0].name.indexOf("Day")!=-1){
							map["tj"]="day";
						}else if (zqid[0].name.indexOf("Mon")!=-1){
							map["tj"]="mon";
						}else if (zqid[0].name.indexOf("Week")!=-1){
							map["tj"]="week";
						}else if (zqid[0].name.indexOf("Year")!=-1){
							map["tj"]="year";
						}else if (zqid[0].name.indexOf("Quarter")!=-1){
							map["tj"]="quarter";
						}
						map["zq"]= $("#weeklyStatis_jsrq #zq_"+$("#weeklyStatis_jsrq #"+zqid[0].id).attr("dm")).val();
					if ($("#weeklyStatis_jsrq_screencontent #bdid").val()=="ptzbdcls"||$("#weeklyStatis_jsrq_screencontent #bdid").val()=="ywzrfzs"||$("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("tjxsdcl")!=-1
						||$("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("top")!=-1
						||$("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("ptywzbtj")!=-1
						||$("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("cpywzbtj")!=-1){
							map["lrsjStart"]=$("#weeklyStatis_jsrq_screencontent #lrsjStart").val();
							map["lrsjEnd"]=$("#weeklyStatis_jsrq_screencontent #lrsjEnd").val();
							if ($("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("tjxsdcl")!=-1){
								if (zqid[0].name=="tjxsdcly"){
									map["method"]="getTjxsdclByYear"
								}else if (zqid[0].name=="tjxsdclq"){
									map["method"]="getTjxsdclByQuarter";
								}else if (zqid[0].name=="tjxsdclm"){
									map["method"]="getTjxsdclByMon";
								}
								map["zfl"]=$("#weeklyStatis_jsrq #"+zqid[0].id).attr("dm");
								clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val());
							}else {
								map["method"]=zqid[0].name;
							if ($("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("top")!=-1||$("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("ptywzbtj")!=-1
								||$("#weeklyStatis_jsrq_screencontent #bdid").val().indexOf("cpywzbtj")!=-1){
								clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val().substr(0,$("#weeklyStatis_jsrq_screencontent #bdid").val().length));
							}else {
								clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val().substr(0,$("#weeklyStatis_jsrq_screencontent #bdid").val().length-1));
							}
							}
						}else {
							map["method"]=zqid[0].name;
							clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis_jsrq_screencontent #bdid").val());
						}
					}
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
	var obj = $("#weeklyStatis_jsrq #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weeklyStatis_jsrq #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weeklyStatis_jsrq #"+otherid).removeClass("bechoosed");
		$("#weeklyStatis_jsrq #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weeklyStatis_jsrq #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="季度"){
		$("#weeklyStatis_jsrq #"+getid+" .roundj").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weeklyStatis_jsrq #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weeklyStatis_jsrq #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weeklyStatis_jsrq #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}
}

/**
 * 设置按钮样式(年 季度 月 周 年)
 * @param getid
 * @param _renderArr
 * @returns
 */
function initEchartsLeadBtnCss2(getid,_renderArr){
	var obj = $("#weeklyStatis_jsrq #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weeklyStatis_jsrq #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weeklyStatis_jsrq #"+otherid).removeClass("bechoosed");
		$("#weeklyStatis_jsrq #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weeklyStatis_jsrq #"+getid+" .roundy").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="季度"){
		$("#weeklyStatis_jsrq #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weeklyStatis_jsrq #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weeklyStatis_jsrq #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weeklyStatis_jsrq #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis_jsrq #"+getid).addClass("bechoosed");
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

$("#weeklyStatis_jsrq button[name='hzhb']").unbind("click").click(function(e){
	var hbid = e.currentTarget.id;
	var flid = hbid.split("_")[1];
	var dateid = "ybxxhead"+"_"+flid;
	var method = $("#"+dateid + " #"+$("#weeklyStatis_jsrq #"+dateid + " .bechoosed")[0].id).attr("method");
	var hbzfl = $("#weeklyStatis_jsrq #zfl_" + flid).val();
	if(hbzfl==undefined)
		hbzfl="";
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/weekLeadStatisPartnerPageByJsrq?fl='+flid+'&zfl='+hbzfl+'&load_flg='+load_flg+'&method='+method+'"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis_jsrq").load("/ws/statistics/weekLeadStatisPartnerPageByJsrq?fl="+flid+"&zfl="+hbzfl+"&load_flg="+load_flg+"&method="+method);
	}

	return true;
});

$("#weeklyStatis_jsrq #jcdw_sys").unbind("click").click(function(e){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/weekLeadStatisOtherPageByJsrq?load_flg='+load_flg+'"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis_jsrq").load("/ws/statistics/weekLeadStatisOtherPageByJsrq?load_flg="+load_flg);
	}
	return true;
});


function init(){
	//统计销售平台达成率
	$("#weeklyStatis_jsrq div[name='tjxsdcl']").each(function () {
		var map = {}
		var dm = $("#" + this.id).attr("dm")
		var id = "tjxsdcl_" + dm;
		map["id"] = id;
		map["chart"] = null;
		map["el"] = null;
		var yxxid="tjxsdclyxxs_"+dm;
		var zqid="tjxsdcl_"+dm;
		map["render"] = function (data, searchData) {

			if (data){
				$("#weeklyStatis_jsrq #"+id+"yxxs").val(searchData.zqs[yxxid]);
				for (var i = 0; i < data.length; i++) {
					var table="<table id='tab2'>";
					table+="<thead>";
					table+="<tr>";
					table+="<th >平台</th>";
					table+="<th >m+t/R/指标</th>";
					table+="<th >达成率</th>";
					table+="<th >GAP</th>";
					table+="</tr>";
					table+="</thead>";
					var color="";
					for (var j = 0; j < data.length; j++) {
						if (j+1!==data.length){
							color = "#FFFFFF";
							if (j % 2 == 0) {
								color = "#DDEBF7";
							}
						}else {
							color="#9BC2E6";
						}
						table+="<tbody>"
						table+="<tr>";
						table+="<td  style='background-color:" + color + "!important'>"+data[j].mc+"</td>";
						table+="<td style='background-color:" + color + "!important'>"+data[j].sumnum+"/"+data[j].rsumnum+"/"+data[j].zb+"</td>"
						table+="<td style='background-color:" + color + "!important'>"+data[j].dcl+"</td>"
						table+="<td style='background-color:" + color + "!important'>"+data[j].gap+"</td>"
						table+="</tr>";
						table+="</tbody>"
					}
					table+="</table>";
				}
				$("#weeklyStatis_jsrq #echarts_week_lead_tjxsdcl_" + dm).html("");
				$("#weeklyStatis_jsrq #tjsj_tjxsdcl_" + dm).html("");
				$("#weeklyStatis_jsrq #echarts_week_lead_tjxsdcl_" + dm).append(table)
				$("#weeklyStatis_jsrq #tjsj_tjxsdcl_" + dm).append(searchData.zqs[zqid]+"-"+searchData.zqs[yxxid]);
				if (data.length == 0){
					$("#weeklyStatis_jsrq #tjsj_tjxsdcl_" + dm).html("");
					$("#weeklyStatis_jsrq #echarts_week_lead_"+ dm).html("");
				}
			}
		};
		flModels.push(map);
	});
}


function pttjlistweekly(){
	$("#weeklyStatis_jsrq").load("/ws/statistics/getPlatformWeekListByJsrq?jsrqstart="+$("#weeklyStatis_jsrq #jsrqstart").val()+"&jsrqend="+$("#weeklyStatis_jsrq #jsrqend").val());
}
function showSalesAttainmentRateWeeklyByArea(yhid,zblxmc,qyid,qymc,kszq,jszq){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/salesAttainmentRateByAreaViewAndJsrq?load_flg='+load_flg+'&zblxcsmc='+zblxmc+'&yhid='+yhid+'&qyid='+qyid+'&qymc='+qymc+'&kszq='+kszq+'&jszq='+jszq+'&flag=weekly"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis_jsrq").load("/ws/statistics/salesAttainmentRateByAreaViewAndJsrq?load_flg="+load_flg+"&zblxcsmc="+zblxmc+"&yhid="+yhid+"&qyid="+qyid+"&qymc="+qymc+"&kszq="+kszq+"&jszq="+jszq+"&flag=weekly");
	}
}

//点击数据刷新按钮刷新redis统计数据
function refresh(){
    $.confirm('统计数据刷新需要等待几秒钟，您确定要刷新吗？',function(result){
        if(result){
            $.ajax({
                type : "post",
                url : "/ws/statistics/refreshLocal_weekly_jsrq",
                data:'',
                dataType : "json",
                success : function(datas) {
                }
            });
        }
    });
}


$(function () { $('#weeklyStatis_jsrq #collapseOne').collapse('show')});
$(function () { $('#weeklyStatis_jsrq #collapseTwo').collapse('show')});
$(function () { $('#weeklyStatis_jsrq #collapseThree').collapse('show')});
$(function () { $('#weeklyStatis_jsrq #collapseFour').collapse('show')});
$(function () { $('#weeklyStatis_jsrq #collapseFive').collapse('show')});
$(function () { $('#weeklyStatis_jsrq #collapseSix').collapse('show')});
$(function () { $('#weeklyStatis_jsrq #collapseSeven').collapse('show')});
$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weeklyStatis_jsrq .chosen-select').chosen({width: '100%'});
});