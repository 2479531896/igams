// 计时器
var timer = null;
function time() {
	clearTimeout(timer); // 清除定时器
	var currtime = dateFtt("yyyy-MM-dd hh:mm:ss",new Date());
	$("#timeSpan").text(currtime);
	timer = setTimeout(time, 1000); //设定定时器，循环运行
}
/**
 * 日期格式化
 * @param fmt
 * @param date
 * @returns
 */
function dateFtt(fmt,date) {
	var o = { 
		"M+" : date.getMonth()+1, // 月份
		"d+" : date.getDate(), // 日
		"h+" : date.getHours(), // 小时
		"m+" : date.getMinutes(), // 分
		"s+" : date.getSeconds(), // 秒
		"q+" : Math.floor((date.getMonth()+3)/3), // 季度
		"S" : date.getMilliseconds() // 毫秒
	}; 
	if(/(y+)/.test(fmt)) 
		fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	for(var k in o) 
		if(new RegExp("("+ k +")").test(fmt)) 
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
	return fmt; 
}

// 初始化地图
function initMapPage() {
	var myChart = echarts.init(document.getElementById('lasmap'),'macarons');
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

	var option = {
	    title : {
	        text: '选择实验室',
	        subtext: '',
	        left: 'center',
	        textStyle : {
	            color: '#fff'
	        }
	    },
	    tooltip : {
	        trigger: 'item'
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
	            emphasis: { //是否在高亮状态下显示标签
	                show: false,
	                textStyle: {
                        color: '#fff'
                    }
	            }
	        },
	        roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
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
                    shadowOffsetX: -2,
                    shadowOffsetY: 2,
                    shadowBlur: 10
                },
                emphasis: {
                    areaColor: '#389BB7',
                    borderWidth: 0
                }
            },
	    }
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
	
	myChart.on('click', function (params) {
		// 查询实验室数据
		$("#lasList").empty();
		var length = 100;
		if(params.name == "新疆")
			length = 2;
		if(params.name == "西藏")
			length = 5;
		if(params.name == "内蒙古")
			length = 8;
		if(params.name == "湖南")
			length = 10;
		if(params.name == "青海")
			length = 15;
		if(params.name == "四川")
			length = 6;
		if(length != null && length > 0){
			var html = '';
			for(var i = 0; i < length; i++){
				html += '<div style="margin:10px;"><button type="button" style="width:100%;height:30px !important;background:rgba(255, 128, 0, 0.5);" class="btn btn-warn" onclick=\'checkLas(event,"'+ params.name +'实验室'+i+'");\'>'+ params.name +'实验室'+i+'</button></div>';
			}
			$("#lasList").append(html);
		}
	});
}

/**
 * 选择实验室，跳转页面
 * @returns
 */
function checkLas(event, name){
	// 跳转实验室结构页面
	console.log(name);
}

/**
 * 旋转动画和页面载入动画
 * @returns
 */
function xzAndzr(){
	$("#outaction").rotate({animateTo: 180,duration:2000});
	$("#inaction").rotate({animateTo: -180,duration:2000});
	$("#xuanzhuan").fadeOut(2000);
	setTimeout(function(){
		$("#xuanzhuan").attr("style","display:none;");
	},2000);
	
	$("#lasPanel").attr('style','padding:5px;position:absolute;left:45px; top:50px;background-color:rgba(147, 235, 248, 0.1);overflow:auto;max-height:'+($("#lasmap").height()-100)+'px;');
}

$(document).ready(function() {
	// 启动计时器
	timer = setTimeout(time, 1000);
	// 初始化地图
	initMapPage();
	// 载入动画
	xzAndzr();
});