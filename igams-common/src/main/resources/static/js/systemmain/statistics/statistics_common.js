//加载统计数据
var loadCommonStatis = function(){
	var _eventTag = "commonStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_common_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	
	// 路径配置
	setTimeout(function(){
		//加载数据
		var _url = $('#commonStatis #urlPrefix').val()+"/systemmain/query/pageGetListByType";
		var param ={};
		var pageflag = $('#commonStatis #pageflag').val();
		var yhid = $('#commonStatis #yhid').val();
		if(pageflag){
			_url = $('#commonStatis #urlPrefix').val()+"/ws/query/getStaticDataByType?yhid="+$('#commonStatis #yhid').val()+"&lbqf="+$('#commonStatis #lbqf').val();
		}
		param["access_token"]=$("#ac_tk").val();
		param["sortName"]="px";
		param["sortOrder"]="asc";
		$.ajax({
			type : "post",
			url : _url,
			data:param,
			dataType : "json",
			success : function(datas) {
				if(datas){
					var statics_commons = $(".statics_common_lscx")
					for(var j=0; j<statics_commons.length; j++){
						var t_id = $(statics_commons[j]).attr("id");
						var t_xsfsdm = $(statics_commons[j]).attr("xsfsdm");
						t_id = t_id.replace("echarts_common_","");
						if("Table" == t_xsfsdm){
							var bthtml = "";
							var nrhtml = "";
							for (var i = 0; i < datas[t_id].headList.length; i++) {
								bthtml = bthtml + "<th title='"+ datas[t_id].headList[i] + "'>" + datas[t_id].headList[i] + "</th>";
							}
							for (var i = 0; i < datas[t_id].resultList.length; i++) {
								nrhtml = nrhtml + "<tr>";
								for (var k = 0; k < datas[t_id].headList.length; k++) {
									if (datas[t_id].resultList[i][datas[t_id].headList[k]] == "undefined" || datas[t_id].resultList[i][datas[t_id].headList[k]] == null) {
										nrhtml = nrhtml + "<td title=''></td>";
									} else {
										nrhtml = nrhtml + "<td title='" + datas[t_id].resultList[i][datas[t_id].headList[k]] + "'>" + datas[t_id].resultList[i][datas[t_id].headList[k]] + "</td>";
									}
								}
								nrhtml = nrhtml + "</tr>";
							}

							$("#statis_common_bt_"+t_id).append(bthtml);
							$("#statis_common_nr_"+t_id).append(nrhtml);
						}else if("Pie" == t_xsfsdm){
							var myChart = echarts.init(statics_commons[j],_statis_theme);
							loadStatisticsCommonPierStatis(myChart);
							var legendData = new Array();
							var seriesData = new Array();
							for (var i = 0; i < datas[t_id].resultList.length; i++) {
								seriesData.push({
									value : datas[t_id].resultList[i].tjsl,
									name : datas[t_id].resultList[i].tjmc
								});
								legendData.push(datas[t_id].resultList[i].tjmc);
								if(datas[t_id].resultList[i].name !=null && datas[t_id].resultList[i].name !=""){
									name = datas[t_id].resultList[i].name;
								}else{
									name = "";
								}								
							}
							myChart.setOption({
								/*legend : {
									data : legendData
								},*/
								series : [ {
									name : name,
									radius : [ '0', '45%' ],
									data : seriesData
								} ]
							});
						}else if("Bar" == t_xsfsdm){
							var myChart = echarts.init(statics_commons[j],_statis_theme);
							loadStatisticsCommonBarStatis(myChart);
							var Xdata = new Array();
							var Ydata = new Array();
							var seriesData = new Array();
							// 组装Xdata
							var count = 0;
							for (var i = 0; i < datas[t_id].resultList.length; i++) {
								if (datas[t_id].resultList[i].tjmc == null) {
									count++;
									continue;
								}
								if (Xdata.indexOf(datas[t_id].resultList[i].tjmc) == -1) {
									Xdata.push(datas[t_id].resultList[i].tjmc);
								}
							}
							// 判断字段数
							if (Xdata.length + count < datas[t_id].resultList.length) {
								// 组装seriesData
								for (var i = 0; i < datas[t_id].resultList.length; i++) {
									if (datas[t_id].resultList[i].tjmc2 != null && seriesData.indexOf(datas[t_id].resultList[i].tjmc2) == -1) {
										seriesData.push(datas[t_id].resultList[i].tjmc2);
									}
								}
								// 组装series
								for (var i = 0; i < seriesData.length; i++) {
									var temData = new Array();
									for (var k = 0; k < Xdata.length; k++) {
										var tjsl = 0;
										for (var h = 0; h < datas[t_id].resultList.length; h++) {
											if (Xdata[k] == datas[t_id].resultList[h].tjmc && datas[t_id].resultList[h].tjmc2 == seriesData[i]) {
												tjsl = datas[t_id].resultList[h].tjsl;
											}
										}
										temData.push(tjsl);
									}
									Ydata.push({
										name : seriesData[i],
										type : 'bar',
										barGap : 0,
										label : {
											normal : {
												show : true,
												position : 'top'
											}
										},
										data : temData
									});
								}
							} else {
								var temData = new Array();
								for (var i = 0; i < datas[t_id].resultList.length; i++) {
									temData.push(datas[t_id].resultList[i].tjsl);
								}
								Ydata.push({
									name : '数量',
									type : 'bar',
									barGap : 0,
									label : {
										normal : {
											show : true,
											position : 'top'
										}
									},
									data : temData
								});
							}
							myChart.setOption({
								grid : {
									left : '3%',
									right : '4%',
								},

								legend : {
									data : seriesData,
								},
								xAxis : [ {
									data : Xdata,
								} ],
								yAxis : [ {
									type : 'value'
								} ],
								series : Ydata
							});
						}
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
}

/**
 * 加载圆饼统计图
 */
var loadStatisticsCommonPierStatis=function(myChart){
	var pieoption = {
			tooltip : {
			        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        x : 'center',
		        y : 'top',
		        data:[],
		        textStyle: {
					color: '#3E9EE1'
				}
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {
		                show: true,
		                type: ['pie', 'funnel']
		            },
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : [
		    	 {	 
		    		 name: [],
		             type: 'pie',
		             radius : [],
		             center: ['50%', '50%'],
		             data:[],
		             itemStyle: {
		                 emphasis: {
		                     shadowBlur: 10,
		                     shadowOffsetX: 0,
		                     shadowColor: 'rgba(0, 0, 0, 0.5)'
		                 }
		             },
		             label: {
		                 formatter:'{b}：{c}({d}% )'
		             }
		         }
		    ]
		}
	myChart.setOption(pieoption,true);
}

/**
 * 加载柱状图
 */
var loadStatisticsCommonBarStatis = function(myChart) {
	var pieoption = {
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'cross',
				crossStyle : {
					color : '#999'
				}
			}
		},
		toolbox : {
			feature : {
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		grid : {
			containLabel : true,
		},
		 axisLabel: {
             interval:0,
             rotate:90
          },
		xAxis : [ {
			type : 'category',
			data : [],
			axisLabel:{
	            rotate:90,
	            interval:0
	        }
		}],
		yAxis : [ {
			type : 'value'
		} ],
		series : []
	}
	myChart.setOption(pieoption, true);
}



function init(){
}

$(function(){
	init();
	loadCommonStatis();
});