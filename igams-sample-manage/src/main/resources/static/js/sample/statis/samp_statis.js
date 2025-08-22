function initMapPage(){
	var myChart = echarts.init(document.getElementById('main'),'macarons');
	
	var geoCoordMap = {
		    '上海': [121.4648,31.2891],
		    '东莞': [113.8953,22.901],
		    '东营': [118.7073,37.5513],
		    '中山': [113.4229,22.478],
		    '临汾': [111.4783,36.1615],
		    '临沂': [118.3118,35.2936],
		    '丹东': [124.541,40.4242],
		    '丽水': [119.5642,28.1854],
		    '乌鲁木齐': [87.9236,43.5883],
		    '佛山': [112.8955,23.1097],
		    '保定': [115.0488,39.0948],
		    '兰州': [103.5901,36.3043],
		    '包头': [110.3467,41.4899],
		    '北京': [116.4551,40.2539],
		    '北海': [109.314,21.6211],
		    '南京': [118.8062,31.9208],
		    '南宁': [108.479,23.1152],
		    '南昌': [116.0046,28.6633],
		    '南通': [121.1023,32.1625],
		    '厦门': [118.1689,24.6478],
		    '台州': [121.1353,28.6688],
		    '合肥': [117.29,32.0581],
		    '呼和浩特': [111.4124,40.4901],
		    '咸阳': [108.4131,34.8706],
		    '哈尔滨': [127.9688,45.368],
		    '唐山': [118.4766,39.6826],
		    '嘉兴': [120.9155,30.6354],
		    '大同': [113.7854,39.8035],
		    '大连': [122.2229,39.4409],
		    '天津': [117.4219,39.4189],
		    '太原': [112.3352,37.9413],
		    '威海': [121.9482,37.1393],
		    '宁波': [121.5967,29.6466],
		    '宝鸡': [107.1826,34.3433],
		    '宿迁': [118.5535,33.7775],
		    '常州': [119.4543,31.5582],
		    '广州': [113.5107,23.2196],
		    '廊坊': [116.521,39.0509],
		    '延安': [109.1052,36.4252],
		    '张家口': [115.1477,40.8527],
		    '徐州': [117.5208,34.3268],
		    '德州': [116.6858,37.2107],
		    '惠州': [114.6204,23.1647],
		    '成都': [103.9526,30.7617],
		    '扬州': [119.4653,32.8162],
		    '承德': [117.5757,41.4075],
		    '拉萨': [91.1865,30.1465],
		    '无锡': [120.3442,31.5527],
		    '日照': [119.2786,35.5023],
		    '昆明': [102.9199,25.4663],
		    '杭州': [119.5313,29.8773],
		    '枣庄': [117.323,34.8926],
		    '柳州': [109.3799,24.9774],
		    '株洲': [113.5327,27.0319],
		    '武汉': [114.3896,30.6628],
		    '汕头': [117.1692,23.3405],
		    '江门': [112.6318,22.1484],
		    '沈阳': [123.1238,42.1216],
		    '沧州': [116.8286,38.2104],
		    '河源': [114.917,23.9722],
		    '泉州': [118.3228,25.1147],
		    '泰安': [117.0264,36.0516],
		    '泰州': [120.0586,32.5525],
		    '济南': [117.1582,36.8701],
		    '济宁': [116.8286,35.3375],
		    '海口': [110.3893,19.8516],
		    '淄博': [118.0371,36.6064],
		    '淮安': [118.927,33.4039],
		    '深圳': [114.5435,22.5439],
		    '清远': [112.9175,24.3292],
		    '温州': [120.498,27.8119],
		    '渭南': [109.7864,35.0299],
		    '湖州': [119.8608,30.7782],
		    '湘潭': [112.5439,27.7075],
		    '滨州': [117.8174,37.4963],
		    '潍坊': [119.0918,36.524],
		    '烟台': [120.7397,37.5128],
		    '玉溪': [101.9312,23.8898],
		    '珠海': [113.7305,22.1155],
		    '盐城': [120.2234,33.5577],
		    '盘锦': [121.9482,41.0449],
		    '石家庄': [114.4995,38.1006],
		    '福州': [119.4543,25.9222],
		    '秦皇岛': [119.2126,40.0232],
		    '绍兴': [120.564,29.7565],
		    '聊城': [115.9167,36.4032],
		    '肇庆': [112.1265,23.5822],
		    '舟山': [122.2559,30.2234],
		    '苏州': [120.6519,31.3989],
		    '莱芜': [117.6526,36.2714],
		    '菏泽': [115.6201,35.2057],
		    '营口': [122.4316,40.4297],
		    '葫芦岛': [120.1575,40.578],
		    '衡水': [115.8838,37.7161],
		    '衢州': [118.6853,28.8666],
		    '西宁': [101.4038,36.8207],
		    '西安': [109.1162,34.2004],
		    '贵阳': [106.6992,26.7682],
		    '连云港': [119.1248,34.552],
		    '邢台': [114.8071,37.2821],
		    '邯郸': [114.4775,36.535],
		    '郑州': [113.4668,34.6234],
		    '鄂尔多斯': [108.9734,39.2487],
		    '重庆': [107.7539,30.1904],
		    '金华': [120.0037,29.1028],
		    '铜川': [109.0393,35.1947],
		    '银川': [106.3586,38.1775],
		    '镇江': [119.4763,31.9702],
		    '长春': [125.8154,44.2584],
		    '长沙': [113.0823,28.2568],
		    '长治': [112.8625,36.4746],
		    '阳泉': [113.4778,38.0951],
		    '青岛': [120.4651,36.3373],
		    '韶关': [113.7964,24.7028]
		};
	
		var levelColorMap = {
	        '1': 'rgba(241, 109, 115, .8)',
	        '2': 'rgba(255, 235, 59, .7)',
	        '3': 'rgba(147, 235, 248, 1)'
	    };

		var HZData = [
		    [{name:'杭州'}, {name:'长沙',value:95}],
		    [{name:'杭州'}, {name:'嘉兴',value:90}],
		    [{name:'杭州'}, {name:'北京',value:80}]
		];
		
		var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';

		var convertData = function (data) {
		    var res = [];
		    for (var i = 0; i < data.length; i++) {
		        var dataItem = data[i];
		        var fromCoord = geoCoordMap[dataItem[0].name];
		        var toCoord = geoCoordMap[dataItem[1].name];
		        if (fromCoord && toCoord) {
		            res.push({
		                fromName: dataItem[0].name,
		                toName: dataItem[1].name,
		                coords: [fromCoord, toCoord]
		            });
		        }
		    }
		    return res;
		};
        var color = ['#a6c84c', '#ffa022', '#46bee9'];
		var series = [];
		[['杭州', HZData]].forEach(function (item, i) {
		    series.push(
    		{
		        name: item[0] + ' Top10',
		        type: 'lines',
		        zlevel: 1,
		        effect: {
		            show: true,
		            period: 6,    //速度
		            trailLength: 0.7,  //飞过以后的残留透明度
		            color: '#fff',   //颜色
		            symbolSize: 3    //路径的线的宽度
		        },
		        lineStyle: {      //底版线路
		            normal: {
		                color: color[i],	//底版颜色
		                width: 0,			//底版宽度
		                curveness: 0.2		//底版线路弯曲度
		            }
		        },
		        data: convertData(item[1])
		    },{
		        name: item[0] + ' Top10',
		        type: 'lines',
		        zlevel: 2,
		        symbol: ['none', 'arrow'],
		        symbolSize: 10,
		        effect: {
		            show: true,
		            period: 6,
		            trailLength: 0,
		            symbol: planePath,
		            symbolSize: 15
		        },
		        lineStyle: {
		            normal: {
		                color: color[i],
		                width: 1,
		                opacity: 0.6,
		                curveness: 0.2
		            }
		        },
		        data: convertData(item[1])
		    },{
		        name: item[0] + ' Top10',
		        type: 'effectScatter',		//特效散点图
		        coordinateSystem: 'geo',	//'cartesian2d'使用二维的直角坐标系。'geo'使用地理坐标系
		        zlevel: 2,
		        rippleEffect: {				//涟漪特效相关配置。
		            brushType: 'stroke'		//波纹的绘制方式，可选 'stroke' 和 'fill'。
		        },
		        label: {
		            normal: {
		                show: true,
		                position: 'right',  //城市标题显示位置
		                formatter: '{b}'
		            }
		        },
		        symbolSize: function (val) {	//标记的大小，可以设置成诸如 10 这样单一的数字，也可以用数组分开表示宽和高，例如 [20, 10] 表示标记宽为20，高为10。
		            return val[2] / 8;   	//圆圈大小
		        },
		        itemStyle: {				//图形样式，normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
		            normal: {
		                color: color[i]
		            }
		        },
		        data: item[1].map(function (dataItem) {
		            return {
		                name: dataItem[1].name,
		                value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])
		            };
		        })
		    });
		});

		option = {
		    backgroundColor: '#154e90',
		    title : {
		        text: '检测统计',
		        subtext: '合作形态图',
		        left: 'center',
		        textStyle : {
		            color: '#fff'
		        }
		    },
		    tooltip : {
		        trigger: 'item'
		    },
		    legend: {  //说明
		        orient: 'vertical',
		        bottom: '20px',
		        right: '80px',
		        data:['杭州 Top10'],
		        textStyle: {
		            color: '#000'
		        },
		        selectedMode: 'single'
		    },
		    geo: {
		        map: 'china',
		        label: {
		        	normal: {
	                    show: true,
	                    textStyle: {
	                        color: '#fff'
	                    }
	                },
		            emphasis: {		//是否在高亮状态下显示标签
		                show: false,
		                textStyle: {
	                        color: '#fff'
	                    }
		            }
		        },
		        roam: "move", //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
		        itemStyle: {
	                normal: {
	                    borderColor: 'rgba(147, 235, 248, 1)',
	                    borderWidth: 1,
	                    areaColor: {
	                        type: 'radial',
	                        x: 0.5,
	                        y: 0.5,
	                        r: 0.8,
	                        colorStops: [{
	                            offset: 0,
	                            color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
	                        }, {
	                            offset: 1,
	                            color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
	                        }],
	                        globalCoord: false // 缺省为 false
	                    },
	                    shadowColor: 'rgba(128, 217, 248, 1)',
	                    // shadowColor: 'rgba(255, 255, 255, 1)',
	                    shadowOffsetX: -2,
	                    shadowOffsetY: 2,
	                    shadowBlur: 10
	                },
	                emphasis: {
	                    areaColor: '#389BB7',
	                    borderWidth: 0
	                }
	            },
		    },
		    series: series
		};
		myChart.setOption(option);
		myChart.hideLoading();
		var _cfg = this;
		$(window).off(this.eventTag).on("resize"+this.eventTag,function(){ //resize事件
			if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
				return;
			}
			pieoption.legend.x = (_cfg.el.offsetWidth / 2) + 10;//位置重新计算
			_cfg.chart.setOption(pieoption);
			_cfg.chart.resize();
		});
}

//加载统计数据
function initEchartsPage(){
	
	$('.desktop_analysisBar .desktop_analysisBar-head .ocFlag').click(function (e) {
        e.preventDefault();
        var $desktop = $(this).closest('.desktop_analysisBar');
        if($desktop.children('.desktop_analysisBar-con').is(':visible')){
            $desktop.children('.desktop_analysisBar-con').hide(150);
        }else{
            $desktop.children('.desktop_analysisBar-con').show(150);
        }
    });

    $('.desktop_analysisBar .desktop_analysisBar-con .ocFlag').click(function (e) {
        e.preventDefault();
        var $desktop = $(this).closest('.desktop_analysisBar');
        if($desktop.children('.desktop_analysisBar-con').is(':visible')){
            $desktop.children('.desktop_analysisBar-con').hide(150);
        }else{
            $desktop.children('.desktop_analysisBar-con').show(150);
        }
    });
    
	var _eventTag = "manageStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = true; //是否显示加载动画
	var _idPrefix = "echarts_"; //id前缀
	var _renderArr = [
		{
			id:"yb",
			chart: "macarons",
			el: null,
			render:function(data){
				if(data&&this.chart){
					var sum = 0;//合计
					for(var i=0;i<data.length;i++){
						sum+=data[i].value;
					}
					var pieoption = {
						title : {
							text : '标本',
							x : 'center',
							textStyle:{}
						},
						tooltip : {
							trigger : 'item',
							formatter : "{b} : {c} ({d}%)"
						},
						legend:{
							orient : 'vertical',
							x : (this.el.offsetWidth / 2) + 55,
							y : 55,
							itemGap : 1,	// 主副标题纵向间隔,单位px
							textStyle:{
								color : '#83bff6',
							}
						},
						calculable : true,//是否可拖拽
						series : [ {
							type : 'pie',
							radius :  [20, 60], //展示中心到边缘的位置，20代表 中间0-20为空
							center : [ '40%', '50%' ],  //中心所在位置
							roseType : 'radius',  //radius 南丁格尔玫瑰图  普通的圆饼图不需要加这个属性
							startAngle:170,
							label: {
				                normal: {
				                    show: false
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
							data : data.length==0?[{}]:data
						}]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					var _cfg = this;
					$(window).off(this.eventTag).on("resize"+this.eventTag,function(){ //resize事件
						if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
							return;
						}
						pieoption.legend.x = (_cfg.el.offsetWidth / 2) + 10;//位置重新计算
						_cfg.chart.setOption(pieoption);
						_cfg.chart.resize();
					});
				}
			}
		},
		{
			id:"bxkx",
			chart: "macarons",
			el: null,
			render:function(data){
				var zybar ={};
				var kxbar ={};
				var axisData =[];
				var zyData =[];
				var kxData =[];
				if(data&&this.chart){
					var ztsl =0;

					zybar.name = "占用";
					zybar.type = "bar";
					zybar.stack = "总量";
					zybar.label = [];
					zybar.label.normal = [];
					zybar.label.normal.show = true;
					zybar.label.normal.position = "insideRight";
					zybar.data = zyData;
					
					kxbar.name = "空闲";
					kxbar.type = "bar";
					kxbar.stack = "总量";
					kxbar.label = [];
					kxbar.label.normal = [];
					kxbar.label.normal.show = true;
					kxbar.label.normal.position = "insideRight";
					kxbar.data = kxData;
					
					var prelx = "";
					for(var i=0;i<data.length;i++){
						if(prelx =="zt" && data[i].lx =="zt"){
							zybar.data.push(0);
							kxbar.data.push(ztsl);
						}
						if(data[i].lx =="zt"){
							ztsl = data[i].value;
							axisData.push(data[i].id);
						}else{
							zybar.data.push(data[i].value);
							kxbar.data.push(ztsl - data[i].value);
						}
						prelx = data[i].lx;
					}
					var pieoption = {
						title : {
							text : '冰箱空闲',
							x : 'center'
						},
						tooltip : {
							trigger : 'axis',  //提示：分组全部显示
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
						},
						grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    /*toolbox: {
					        show: true,
					        feature: {
					            dataZoom: {
					                yAxisIndex: 'none'
					            },
					            dataView: {readOnly: false},
					            magicType: {type: ['line', 'bar']},
					            restore: {},
					            saveAsImage: {}
					        }
					    },*/
					    xAxis:  {
					        type: 'value'
					    },
					    yAxis: {
					        type: 'category',
					        data: axisData,
					        axisLabel: {
					            formatter: '冰箱：{value}'
					        }
					    },
						legend:{
							x:'center',
							y:30,
							textStyle:{
								color : '#83bff6',
							}
						},
						calculable : true,//是否可拖拽
						series : [zybar,kxbar ]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					var _cfg = this;
					$(window).off(this.eventTag).on("resize"+this.eventTag,function(){ //resize事件
						if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
							return;
						}
						pieoption.legend.x = (_cfg.el.offsetWidth / 2) + 10;//位置重新计算
						_cfg.chart.setOption(pieoption);
						_cfg.chart.resize();
					});
				}
			}
		},
		{
			id:"ybsy",
			chart: "macarons",
			el: null,
			render:function(data){
				if(data&&this.chart){
					var pieoption = {
						title : {
							text : '标本数量情况',
							x : 'center'
						},
						tooltip : {
							trigger : 'axis',  //提示：分组全部显示
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
					        }
						},
						legend:{
							x:'center',
							y:30,
							textStyle:{
								color : '#83bff6',
							}
						},
						/*toolbox: {
					        show: true,
					        feature: {
					            dataZoom: {
					                yAxisIndex: 'none'
					            },
					            dataView: {readOnly: false},
					            magicType: {type: ['line', 'bar']},
					            restore: {},
					            saveAsImage: {}
					        }
					    },*/
						grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    xAxis:  {
					        type: 'category',
					        data:data.cateList,
					    },
					    yAxis: {
					        type: 'value',
					    },
						calculable : true,//是否可拖拽
						series : [{
				            name:'标本新增',
				            type:'line',
				            data:data.xzList,
				            label: {//图形上的文本标签
				                normal:{
				                    formatter: '{@data}',
				                    show: true,//显示数据
				                    color: '#00af58',
				                    position: 'top',
				                    fontSize:'14',
				                },
				            }
				        },
				        {
				            name:'标本使用',
				            type:'line',
				            data:data.syList,
				            label: {//图形上的文本标签
				                normal:{
				                    formatter: '{@data}',
				                    show: true,//显示数据
				                    position: 'top',
				                    fontSize:'12',
				                },
				            },
				           /* markPoint: {
				                data: [
				                    {name: '最高', value: -2, xAxis: 1, yAxis: -1.5}
				                ]
				            }*/
				        }]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					var _cfg = this;
					$(window).off(this.eventTag).on("resize"+this.eventTag,function(){ //resize事件
						if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
							return;
						}
						pieoption.legend.x = (_cfg.el.offsetWidth / 2) + 10;//位置重新计算
						_cfg.chart.setOption(pieoption);
						_cfg.chart.resize();
					});
				}
			}
		},{
			id:"ybs",
			chart: "macarons",
			el: null,
			render:function(data){
				if(data&&this.chart){
					var sum = 0;//合计
					var baroption = {
						title : {
							text : '标本数',
							x : 'center'
						},
						xAxis: {
					        data: ['DNA','血样','组织'],
					        axisLabel: {
					            inside: false
					        },
					        axisTick: {
					            show: false
					        },
					        axisLine: {
					            show: false
					        },
					        z: 10
					    },
					    yAxis: {
					        axisLine: {
					            show: false
					        },
					        axisTick: {
					            show: false
					        },
					    },
					    grid: {
					        left: 35
					    },
					    dataZoom: [
			               {
			                   type: 'inside'
			               }
			           ],
						series : [ 
						    {
					            type: 'bar',
					            itemStyle: {
					                normal: {color: 'rgba(0,0,0,0.05)'}
					            },
					            barGap:'-100%',
					            barCategoryGap:'40%',
					            data: [200],
					            animation: false
					        },{
							type : 'bar',
							itemStyle: {
				                normal: {
				                    color: new echarts.graphic.LinearGradient(
				                        0, 0, 0, 1,
				                        [
				                            {offset: 0, color: '#83bff6'},
				                            {offset: 0.5, color: '#188df0'},
				                            {offset: 1, color: '#188df0'}
				                        ]
				                    )
				                },
				                emphasis: {
				                    color: new echarts.graphic.LinearGradient(
				                        0, 0, 0, 1,
				                        [
				                            {offset: 0, color: '#2378f7'},
				                            {offset: 0.7, color: '#2378f7'},
				                            {offset: 1, color: '#83bff6'}
				                        ]
				                    )
				                }
				            },
				            data: [15,60,38]
						}]
					};
					this.chart.setOption(baroption);
					this.chart.hideLoading();
					var _cfg = this;
					$(window).off(this.eventTag).on("resize"+this.eventTag,function(){ //resize事件
						if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
							return;
						}
						pieoption.legend.x = (_cfg.el.offsetWidth / 2) + 10;//位置重新计算
						_cfg.chart.setOption(pieoption);
						_cfg.chart.resize();
					});
				}
			}
		},
	];
	
	// 路径配置
	setTimeout(function(){
		for(var i=0; i<_renderArr.length; i++){
			var _render = _renderArr[i];
			var _el = _render.el = document.getElementById(_idPrefix+_render.id);
			if(_el){
				_render.chart = echarts.init(_el,_render.chart);
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
		var _url = "/sample/stock/pagedataSjxxStatisByTj?access_token="+ $("#ac_tk").val()+ "&tj="+$("#index_ybsy_tj").val()+"&yblx="+$("#index_ybsy_lx").val();
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			success : function(datas) {
				if(datas){
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data);
					}
				}else{
				}
			}
		});
	});
	

	var ybsyqk = function(){
		var $desktop = $("#index_ybsy_tj").closest('.desktop_analysisBar');
		$desktop.children('.desktop_analysisBar-con').hide(150);
		//加载数据
		var _url = "/sample/stock/pagedataAnsyStatisSampUse?access_token="+ $("#ac_tk").val() + "&tj="+$("#index_ybsy_tj").val()+"&yblx="+$("#index_ybsy_lx").val();
		
		$.ajax({
			type : "post",
			url : _url,
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _render = _renderArr[2];
					_render.render(datas);
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	}
	
	$("#index_ybsy_tj").unbind("change").change(function(){
		ybsyqk();
    });
	$("#index_ybsy_lx").unbind("change").change(function(){
    	ybsyqk();
    });
}

$(document).ready(function(){
	initMapPage();
	initEchartsPage();
});