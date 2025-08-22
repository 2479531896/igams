
var flModels = [];
$("#weeklyStatis div[name='echarts_week_lead_fltj']").each(function(){
	var map = {}
	map["id"] = "fltj_"+$("#"+this.id).attr("dm");
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function(data,searchData){
		var prerq = "";
		if(data&&this.chart){

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

				datalegend.push(rq)
				databsf.push(data[i].bsfnum);
				datasf.push(data[i].sfnum);

				tmp_y = parseInt(data[i].bsfnum)+parseInt(data[i].sfnum);
				if(maxY < tmp_y)
					maxY = parseInt(tmp_y)

				datahj.push(tmp_y);
			}
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
					data: datalegend,
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
//销售达成率
/*$("#weeklyStatis div[name='salesAttainmentRate']").each(function () {
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
		$("#weeklyStatis #echarts_week_lead_salesAttainmentRate_" + dm).html("");
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
			$("#weeklyStatis #echarts_week_lead_salesAttainmentRate_" + dm).append(table);
		}
		$("#weeklyStatis #tjsj_salesAttainmentRate_"+dm).html("");
		$("#weeklyStatis #tjsj_salesAttainmentRate_"+dm).append(searchData.zqs.salesAttainmentRate);
	};
	flModels.push(map);
});*/
//加载统计数据
var loadWeekLeadStatis = function(){
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
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
						//clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
					});
				}
			}

		},
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
				$("#weeklyStatis #tjsj_jyjgOfYblx").html("");
				$("#weeklyStatis #jyjgOfYblx").html("");
				$("#weeklyStatis #tjsj_jyjgOfYblx").append(searchData.zqs.jyjgOfYblx);
				$("#weeklyStatis #jyjgOfYblx").append(table);
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
				$("#weeklyStatis #tjsj_sjdwOfsjysOfks").append(searchData.zqs.sjdwOfsjysOfks)
				$("#weeklyStatis #sjdwOfsjysOfks").append(table);
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
							text : '标本数：'+sum + '个',
							subtext : searchData.zqs.ybtj,
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
		},
		{
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
						clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
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
						/*  	subtext : searchData.zqs.fjsq,*/
						x : 'left'
					},
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b}: \n{c} ({d}%)"
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
					var load_flg=$("#weeklyStatis #load_flg").val();
					var url="/ws/statistics/week_PageList_Fjsq?lx="+param.data.flg+"&lrsj="+searchData.jsrq+"&lxmc="+param.data.name+"&xh="+param.data.xh+"&lrsjstart="+searchData.jsrqstart+"&lrsjend="+searchData.jsrqend+"&lrsjMstart="+searchData.jsrqMstart+"&lrsjMend="+searchData.jsrqMend+"&lrsjYstart="+searchData.jsrqYstart+"&lrsjYend="+searchData.jsrqYend+"&load_flg="+load_flg;
					if(load_flg=="0"){
						dis_toForward(url);
					}else if(load_flg=="1"){
						$("#weeklyStatis").load(url);
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
					var load_flg=$("#weeklyStatis #load_flg").val();
					var url="/ws/statistics/week_PageList_Fqyb?zt="+param.data.zt+"&jsrq="+searchData.jsrq+"&ybztmc="+param.data.name+"&jsrqstart="+searchData.jsrqstart+"&jsrqend="+searchData.jsrqend+"&jsrqMstart="+searchData.jsrqMstart+"&jsrqMend="+searchData.jsrqMend+"&jsrqYstart="+searchData.jsrqYstart+"&jsrqYend="+searchData.jsrqYend+"&load_flg="+load_flg;
					if(load_flg=="0"){
						dis_toForward(url);
					}else if(load_flg=="1"){
						$("#weeklyStatis").load(url);
					}
				});
			}
		},
		{
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
		},
		{
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
							subtext : searchData.jsrqstart + '~' + searchData.jsrqend,
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
						var load_flg=$("#weeklyStatis #load_flg").val();
						var url="/ws/statistics/positive_PageList?lxmc="+param.name+"&bgrqstart="+searchData.jsrqstart+"&bgrqend="+searchData.jsrqend+"&bgrqMstart="+searchData.jsrqMstart+"&bgrqMend="+searchData.jsrqMend+"&bgrqYstart="+searchData.jsrqYstart+"&bgrqYend="+searchData.jsrqYend+"&load_flg="+load_flg;
						if(load_flg=="0"){
							dis_toForward(url);
						}else if(load_flg=="1"){
							$("#weeklyStatis").load(url);
						}
					});
				}
			}
		},
		{
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
						/*title : {
							subtext : startrq + '~' + endrq,
							x : 'left',
							y : 10
						},*/
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
							data:['送检数量'],
							textStyle: {
								color: '#3E9EE1'
							}
						},
						grid: {
							right: 50
						},*/
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
		},
		{
			id: "rfspjys",
			chart: null,
			el: null,
			render: function (data, searchData) {
				if (data && this.chart) {
					var jsdqyDatas = [];
					var qydtqDatas = [];
					var tqdkzDatas = [];
					var kzdjcDatas = [];
					var jcdbgDatas = [];
					if (data.length>0){
						var rqs = [];
						for (let i = 0; i < data.length; i++) {
							var rq = data[i].rq;
							//若rq不存在于rqs，则添加
							if (rqs.indexOf(rq) == -1) {
								rqs.push(rq)
							}
						}
						for (let i = 0; i < rqs.length; i++) {
							var rq = rqs[i];
							for (let j = 0; j < data.length; j++) {
								if (rq == data[j].rq) {
									jsdqyDatas.push(data[j].jsdqy);
									qydtqDatas.push(data[j].qydtq);
									tqdkzDatas.push(data[j].tqdkz);
									kzdjcDatas.push(data[j].kzdjc);
									jcdbgDatas.push(data[j].jcdbg);
								}
							}
						}
					}
					var pieoption = {
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								// Use axis to trigger tooltip
								type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
							},
							formatter: function (params) {//弹窗内容
								let relVal = params[0].name;
								let value = 0;
								for (let i = 0, l = params.length; i < l; i++) {
									value += params[i].value;
								}
								for (let i = 0, l = params.length; i < l; i++) {
									//marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
									relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
									// + '(' + (100 * parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
								}
								// relVal += '<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green;"></span>合计: ' + value;
								return relVal;
							}
							// formatter:"{b} : \n{c} ({d}%)"
						},
						legend: {
							// selected: {
							//     '其他': false//默认“其他”不显示
							// },
						},
						grid: {
							left: '3%',
							right: '10%',
							bottom: '3%',
							containLabel: true
						},
						xAxis: {
							name: '分钟',
							type: 'value'
						},
						yAxis: {
							type: 'category',
							data: rqs
						},
						series: [
							{
								name: '接收-取样',
								type: 'bar',
								stack: 'total',
								label: {
									show: true
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"A+:\n{c}({d})%",
								data: jsdqyDatas
							},
							{
								name: '取样-提取',
								type: 'bar',
								stack: 'total',
								label: {
									show: true
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"A:\n{c}({d})%",
								data: qydtqDatas
							},
							{
								name: '提取-扩增',
								type: 'bar',
								stack: 'total',
								label: {
									show: true
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"B:\n{c}({d})%",
								data: tqdkzDatas
							},
							{
								name: '扩增-检测',
								type: 'bar',
								stack: 'total',
								label: {
									show: true
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"C:\n{c}({d})%",
								data: kzdjcDatas
							},
							{
								name: '检测-报告',
								type: 'bar',
								stack: 'total',
								label: {
									show: true
								},
								emphasis: {
									focus: 'series'
								},
								// formatter:"其他:\n{c}({d})%",
								data: jcdbgDatas
							}
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click", function () {
					});
				}
			}

		},{
			id:"rfszq",
			chart: null,
			el: null,
			render:function(data,searchData){
				var prerq = "";
				if(data&&this.chart){
					var dataxAxis= new Array();
					var datajsdbg=new Array();
					var databl=new Array();
					var maxY=0;
					var maxBL=0;
					var tmp_y=0;
					var intval;
					for (var i = 0; i < data.length; i++) {
						if(data[i].rq.length>8){
							dataxAxis.push(data[i].rq.substring(5,10));
						}else{
							dataxAxis.push(data[i].rq);
						}
						databl.push(data[i].bl);
						datajsdbg.push(data[i].zqsj);
						if(prerq != data[i].rq){
							if (tmp_y<parseFloat(data[i].zqsj)){
								tmp_y =  parseInt(data[i].zqsj);
							}
							if(maxY < tmp_y)
								maxY = parseInt(tmp_y)
							if(i!=0 && maxBL < Math.abs(data[i-1].bl)){
								maxBL =Math.abs(data[i-1].bl);
							}
							prerq = data[i].rq
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
							data: ['接收-报告'],
							textStyle: {
								color: '#3E9EE1'
							},
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: '3%',
							containLabel: true
						},
						yAxis:  [{
							name: '时',
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
								name: '接收-报告',
								type: 'bar',
								stack: '总量',
								barGap: 0,
								label: {
									normal: {
										show: true,
										position: 'insideBottom'
									}
								},
								data: datajsdbg
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
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = "/ws/statistics/getWeekLeadStatis";
		var param ={};
		param["jsrqstart"]= $("#weeklyStatis #jsrqstart").val()
		param["jsrqend"]= $("#weeklyStatis #jsrqend").val()
		param["jsrqMstart"]= $("#weeklyStatis #jsrqMstart").val()
		param["jsrqMend"]= $("#weeklyStatis #jsrqMend").val()
		param["jsrqYstart"]= $("#weeklyStatis #jsrqYstart").val()
		param["jsrqYend"]= $("#weeklyStatis #jsrqYend").val()
		param["zq"]= $("#weeklyStatis [name=zq]").val()
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
								$("#echarts_week_lead_"+_render.id).parent().hide();
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
	map["jsrqstart"]= $("#weeklyStatis #jsrqstart").val()
	map["jsrqend"]= $("#weeklyStatis #jsrqend").val()

	//标本情况的年统计按钮
	$("#weeklyStatis #rfs5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5q");
		map["method"]="getRfsSjxxYearByRSy";
		map["tj"]="year";
		// map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//标本情况的月统计按钮
	$("#weeklyStatis #rfs5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5m");
		map["method"]="getRfsSjxxMonBySy";
		map["tj"]="mon";
		// map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//标本情况的周统计按钮
	$("#weeklyStatis #rfs5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5w");
		map["method"]="getRfsSjxxWeekBySy";
		map["tj"]="week";
		// map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})
	//标本情况的日统计按钮
	$("#weeklyStatis #rfs5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfs5d");
		map["method"]="getRfsSjxxDayBySy";
		map["tj"]="day";
		// map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfs');
	})


	//标本情况的年统计按钮
	$("#weeklyStatis #tj5q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5q");
		map["method"]="getSjxxYearBySy";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的月统计按钮
	$("#weeklyStatis #tj5m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5m");
		map["method"]="getSjxxMonBySy";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的周统计按钮
	$("#weeklyStatis #tj5w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5w");
		map["method"]="getSjxxWeekBySy";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本情况的日统计按钮
	$("#weeklyStatis #tj5d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj5d");
		map["method"]="getSjxxDayBySy";
		clickLeadEcharts(_renderArr,url,map,'ybqk');
	})
	//标本信息的所有统计按钮
	$("#weeklyStatis #tj1q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1q");
		map["method"]="getYblxByYear";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//标本信息的月统计按钮
	$("#weeklyStatis #tj1m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1m");
		map["method"]="getYblxByMon";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//标本信息的周统计按钮
	$("#weeklyStatis #tj1w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1w");
		map["method"]="getYblxByWeek";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})
	//标本信息的日统计按钮
	$("#weeklyStatis #tj1d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj1d");
		map["method"]="getYblxByDay";
		clickLeadEcharts(_renderArr,url,map,'ybtj');
	})

	//检测总次数的年统计按钮
	$("#weeklyStatis #tj2q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2q");
		map["method"]="getSjxxDRByYear";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//检测总次数的月统计按钮
	$("#weeklyStatis #tj2m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2m");
		map["method"]="getSjxxDRByMon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//检测总次数的周统计按钮
	$("#weeklyStatis #tj2w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2w");
		map["method"]="getSjxxDRByWeek";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})
	//检测总次数的日统计按钮
	$("#weeklyStatis #tj2d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj2d");
		map["method"]="getSjxxDRByDay";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'jcxmnum');
	})

	//收费标本检测项目次数（年）
	$("#weeklyStatis #tj4q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4q");
		map["method"]="getYbxxTypeByYear";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费标本检测项目次数（月）
	$("#weeklyStatis #tj4m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4m");
		map["method"]="getYbxxTypeByMon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费标本检测项目次数（周）
	$("#weeklyStatis #tj4w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4w");
		map["method"]="getYbxxTypeByWeek";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})
	//收费标本检测项目次数（日）
	$("#weeklyStatis #tj4d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj4d");
		map["method"]="getYbxxTypeByDay";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ybxxType');
	})

	//科室的圆饼统计图（年）
	$("#weeklyStatis #tj6q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6q");
		map["method"]="getKsByYear";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//科室的圆饼统计图（月）
	$("#weeklyStatis #tj6m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6m");
		map["method"]="getKsByMon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//科室的圆饼统计图（周）
	$("#weeklyStatis #tj6w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6w");
		map["method"]="getKsByWeek";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//科室的圆饼统计图（日）
	$("#weeklyStatis #tj6d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj6d");
		map["method"]="getKsByDay";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'ksList');
	})
	//标本阳性率（年）
	$("#weeklyStatis #tj8q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8q");
		map["method"]="getjyjgOfYblxByYear";
		map["tj"]="year";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//标本阳性率（月）
	$("#weeklyStatis #tj8m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8m");
		map["method"]="getjyjgOfYblxByMon";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//标本阳性率（周）
	$("#weeklyStatis #tj8w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8w");
		map["method"]="getjyjgOfYblxByWeek";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//标本阳性率（日）
	$("#weeklyStatis #tj8d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj8d");
		map["method"]="getjyjgOfYblxByDay";
		map["tj"]="day";
		clickLeadEcharts(_renderArr,url,map,'jyjgOfYblx');
	})
	//合作伙伴的所有统计按钮
	$("#weeklyStatis #tj3q").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3q");
		map["method"]="getDbByYear";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//合作伙伴的月统计按钮
	$("#weeklyStatis #tj3m").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3m");
		map["method"]="getDbByMon";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//合作伙伴的周统计按钮
	$("#weeklyStatis #tj3w").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3w");
		map["method"]="getDbByWeek";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//合作伙伴的日统计按钮
	$("#weeklyStatis #tj3d").unbind("click").click(function(){
		initEchartsLeadBtnCss("tj3d");
		map["method"]="getDbByDay";
		clickLeadEcharts(_renderArr,url,map,'dbtj');
	})
	//复检情况的所有统计按钮
	$("#weeklyStatis #fjq").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjq");
		map["method"]="getFjsqByYear";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况的月统计按钮
	$("#weeklyStatis #fjm").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjm");
		map["method"]="getFjsqByMon";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况的周统计按钮
	$("#weeklyStatis #fjw").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjw");
		map["method"]="getFjsqByWeek";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})
	//复检情况的日统计按钮
	$("#weeklyStatis #fjd").unbind("click").click(function(){
		initEchartsLeadBtnCss("fjd");
		map["method"]="getFjsqByDay";
		clickLeadEcharts(_renderArr,url,map,'fjsq');
	})


	//废弃标本的所有统计按钮
	$("#weeklyStatis #fqq").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqq");
		map["method"]="getFqybByYear";
		map["tj"]="mon";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本的月统计按钮
	$("#weeklyStatis #fqm").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqm");
		map["method"]="getFqybByMon";
		clickLeadEcharts(_renderArr,url,map,'fqyb');

	})
	//废弃标本的周统计按钮
	$("#weeklyStatis #fqw").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqw");
		map["method"]="getFqybByWeek";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//废弃标本的日统计按钮
	$("#weeklyStatis #fqd").unbind("click").click(function(){
		initEchartsLeadBtnCss("fqd");
		map["method"]="getFqybByDay";
		clickLeadEcharts(_renderArr,url,map,'fqyb');
	})
	//rfs平均用时的年统计按钮
	$("#weeklyStatis #rfspjys5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfspjys5q");
		map["method"]="getRFSAvgTimeByYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfspjys');
	})
	//rfs平均用时的月统计按钮
	$("#weeklyStatis #rfspjys5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfspjys5m");
		map["method"]="getRFSAvgTimeByMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfspjys');
	})
	//rfs平均用时的周统计按钮
	$("#weeklyStatis #rfspjys5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfspjys5w");
		map["method"]="getRFSAvgTimeByWeek";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfspjys');
	})
	//rfs平均用时的日统计按钮
	$("#weeklyStatis #rfspjys5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfspjys5d");
		map["method"]="getRFSAvgTimeByDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfspjys');
	})
	//rfs周期用时的年统计按钮
	$("#weeklyStatis #rfszq5q").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfszq5q");
		map["method"]="getRfsZqByYear";
		map["tj"]="year";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfszq');
	})
	//rfs周期用时的月统计按钮
	$("#weeklyStatis #rfszq5m").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfszq5m");
		map["method"]="getRfsZqByMon";
		map["tj"]="mon";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfszq');
	})
	//rfs周期用时的周统计按钮
	$("#weeklyStatis #rfszq5w").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfszq5w");
		map["method"]="getRfsZqByWeek";
		map["tj"]="week";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfszq');
	})
	//rfs周期用时的日统计按钮
	$("#weeklyStatis #rfszq5d").unbind("click").click(function(e){
		initEchartsLeadBtnCss("rfszq5d");
		map["method"]="getRfsZqByDay";
		map["tj"]="day";
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'rfszq');
	})
/*	//分类的所有统计按钮
	$("#weeklyStatis a[name='tj7q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByYear";
		map["sfflg"]= $("#weeklyStatis #sfflg_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis #zfl_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis #hbmc_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})
	//分类的月统计按钮
	$("#weeklyStatis a[name='tj7m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByMon";
		map["sfflg"]= $("#weeklyStatis #sfflg_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis #zfl_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis #hbmc_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})
	//分类的周统计按钮
	$("#weeklyStatis a[name='tj7w']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByWeek";
		map["sfflg"]= $("#weeklyStatis #sfflg_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis #zfl_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis #hbmc_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})
	//分类的日统计按钮
	$("#weeklyStatis a[name='tj7d']").unbind("click").click(function(e){
		initEchartsLeadBtnCss(e.currentTarget.id);
		map["method"]="getFlByDay";
		map["sfflg"]= $("#weeklyStatis #sfflg_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["zq"]= $("#weeklyStatis #zq_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["hbfl"]= $("#weeklyStatis #"+e.currentTarget.id).attr("dm")
		map["hbzfl"]= $("#weeklyStatis #zfl_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		map["db"]= $("#weeklyStatis #hbmc_"+$("#weeklyStatis #"+e.currentTarget.id).attr("dm")).val()
		clickLeadEcharts(_renderArr,url,map,'fltj_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})*/
	/*//销售达成率（年）
	$("#weeklyStatis a[name='tj9y']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getSalesAttainmentRateByYear";
		map["tj"]="year";
		map["qyid"]=$("#weeklyStatis #"+e.currentTarget.id).attr("dm");
		clickLeadEcharts(_renderArr,url,map,'salesAttainmentRate_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})
	//销售达成率（季度）
	$("#weeklyStatis a[name='tj9q']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getSalesAttainmentRateByQuarter";
		map["tj"]="quarter";
		map["qyid"]=$("#weeklyStatis #"+e.currentTarget.id).attr("dm");
		clickLeadEcharts(_renderArr,url,map,'salesAttainmentRate_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})
	//销售达成率（月）
	$("#weeklyStatis a[name='tj9m']").unbind("click").click(function(e){
		initEchartsLeadBtnCss2(e.currentTarget.id);
		map["method"]="getSalesAttainmentRateByMon";
		map["tj"]="mon";
		map["qyid"]=$("#weeklyStatis #"+e.currentTarget.id).attr("dm");
		clickLeadEcharts(_renderArr,url,map,'salesAttainmentRate_'+$("#weeklyStatis #"+e.currentTarget.id).attr("dm"));
	})*/
	//子类别下拉框改变事件
	$("#weeklyStatis select[name='zfl']").unbind("change").change(function(e){
		var zflid = e.currentTarget.id;
		var flid = zflid.split("_")[1];
		var sel_hbmc = $("#weeklyStatis #hbmc_"+flid);
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

		var statisTitle = $("#weeklyStatis #ybxxhead_"+flid+" .bechoosed").attr("title");
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
		zflmap["jsrqstart"]= $("#weeklyStatis #jsrqstart").val()
		zflmap["jsrqend"]= $("#weeklyStatis #jsrqend").val()
		zflmap["zq"]= $("#weeklyStatis #zq_" + flid).val()
		zflmap["sfflg"]= $("#weeklyStatis #sfflg_" + flid).val()
		zflmap["hbfl"]= flid
		zflmap["hbzfl"]= $("#weeklyStatis #zfl_" + flid).val()
		zflmap["db"]= $("#weeklyStatis #hbmc_" + flid).val()
		clickLeadEcharts(_renderArr,url,zflmap,'fltj_'+flid);
	});

	//周期改变事件
	$("#weeklyStatis input[name='zq']").unbind("change").change(function(e){
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
		var flid = $("#weeklyStatis #"+e.currentTarget.id).attr("dm");
		var dateid = "ybxxhead"+"_"+flid;
		var hz = (($("#weeklyStatis #"+e.currentTarget.id).attr("tjm")!=null && $("#weeklyStatis #"+e.currentTarget.id).attr("tjm")!="")?"_":"") + flid;
		var method = $("#"+$("#weeklyStatis #"+dateid+" .bechoosed")[0].id).attr("method")
		map["method"]=method;
		map["zq"]= e.currentTarget.value;
		map["db"]= $("#weeklyStatis #hbmc_"+flid).val();
		map["hbzfl"]= $("#weeklyStatis #zfl_"+flid).val();
		map["hbfl"]= flid;
		map["sfflg"]= $("#weeklyStatis #sfflg_"+flid).val()
		clickLeadEcharts(_renderArr,url,map,$("#weeklyStatis #"+e.currentTarget.id).attr("tjm")+hz);
	});
}

//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
	var obj = $("#weeklyStatis #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weeklyStatis #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weeklyStatis #"+otherid).removeClass("bechoosed");
		$("#weeklyStatis #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weeklyStatis #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weeklyStatis #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weeklyStatis #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weeklyStatis #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}
}

/**
 * 设置按钮样式(年 季度 月 周 年)
 * @param getid
 * @param _renderArr
 * @returns
 */
function initEchartsLeadBtnCss2(getid,_renderArr){
	var obj = $("#weeklyStatis #"+getid)
	var gettitle=obj.attr("title");
	var a=$("#weeklyStatis #"+getid).siblings();
	var ah=a.length;
	for(var i=0;i<ah;i++){
		var otherid=a[i].id;
		$("#weeklyStatis #"+otherid).removeClass("bechoosed");
		$("#weeklyStatis #"+otherid+" .fa").attr("style","");
	}
	//点击统计图按钮，按照全部查询，并显示相应统计图
	//Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
	if(gettitle=="年"){
		$("#weeklyStatis #"+getid+" .roundy").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="季度"){
		$("#weeklyStatis #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="月"){
		$("#weeklyStatis #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="周"){
		$("#weeklyStatis #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
	}else if(gettitle=="日"){
		$("#weeklyStatis #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
		$("#weeklyStatis #"+getid).addClass("bechoosed");
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

$("#weeklyStatis button[name='hzhb']").unbind("click").click(function(e){
	console.log("12");
	var hbid = e.currentTarget.id;
	var flid = hbid.split("_")[1];
	var hbzfl = $("#weeklyStatis #zfl_" + flid).val();
	if(hbzfl==undefined)
		hbzfl="";
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/weekLeadStatisPartnerPage?fl='+flid+'&zfl='+hbzfl+'&load_flg='+load_flg+'"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis").load("/ws/statistics/weekLeadStatisPartnerPage?fl="+flid+"&zfl="+hbzfl+"&load_flg="+load_flg);
	}

	return true;
});

$("#weeklyStatis #jcdw_sys").unbind("click").click(function(e){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/weekLeadStatisOtherPage?load_flg='+load_flg+'"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis").load("/ws/statistics/weekLeadStatisOtherPage?load_flg="+load_flg);
	}
	return true;
});


function init(){
}


function qytjlistweekly(){
	$("#weeklyStatis").load("/ws/statistics/getZoneWeekList?jsrqstart="+$("#jsrqstart").val()+"&jsrqend="+$("#jsrqend").val());
}
function showSalesAttainmentRateWeeklyByArea(yhid,zblxmc,qyid,qymc,kszq,jszq){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
			'<input type="text" name="view_url" value="/ws/statistics/salesAttainmentRateByAreaView?load_flg='+load_flg+'&zblxcsmc='+zblxmc+'&yhid='+yhid+'&qyid='+qyid+'&qymc='+qymc+'&kszq='+kszq+'&jszq='+jszq+'&flag=weekly"/>' +
			'</form>')
			.appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis").load("/ws/statistics/salesAttainmentRateByAreaView?load_flg="+load_flg+"&zblxcsmc="+zblxmc+"&yhid="+yhid+"&qyid="+qyid+"&qymc="+qymc+"&kszq="+kszq+"&jszq="+jszq+"&flag=weekly");
	}
}
$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weeklyStatis .chosen-select').chosen({width: '100%'});
});