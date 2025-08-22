/**
 * 显示统计图点击事件
 * @param wlid
 * @returns
 */
function showImg(domId, item, zdmc,width,height){
	var xwidth = 400;
	if (width){
		xwidth = width;
	}
	var xheight = 200;
	if (height){
		xheight = height;
	}
	$("#"+domId+" div[name='box']").attr("style","display:block;width:"+xwidth+"px;height:"+xheight+"px;position:absolute;top:50%;left:40%;background-color:white;border:1px solid black;z-index:98;");
	$("#"+domId+" div[name='echarts_common_static']").attr("id","div_"+domId);
	//获取元素
	var dv = $("#"+domId+" div[name='box']").get(0);
	var x = 0;
	var y = 0;
	var l = 0;
	var t = 0;
	var isDown = false;
	//鼠标按下事件
	dv.onmousedown = function(e) {
	    //获取x坐标和y坐标
	    x = e.clientX;
	    y = e.clientY;

	    //获取左部和顶部的偏移量
	    l = dv.offsetLeft;
	    t = dv.offsetTop;
	    //开关打开
	    isDown = true;
	    //设置样式  
	    dv.style.cursor = 'move';
	}
	//鼠标移动
	window.onmousemove = function(e) {
	    if (isDown == false) {
	        return;
	    }
	    //获取x和y
	    var nx = e.clientX;
	    var ny = e.clientY;
	    //计算移动后的左偏移量和顶部的偏移量
	    var nl = nx - (x - l);
	    var nt = ny - (y - t);

	    dv.style.left = nl + 'px';
	    dv.style.top = nt + 'px';
	}
	//鼠标抬起事件
	dv.onmouseup = function() {
	    //开关关闭
	    isDown = false;
	    dv.style.cursor = 'default';
	}
	
	// 查询数据加载统计图
	selectStatistic(domId, item, zdmc);
	
	// 鼠标移入关闭按钮
	var mouseover = $("#"+domId+" button[name='close']").mouseover(function(){
		$("#"+domId+" div[name='close']").attr("style","color:red;float:right;border:0px;background-color:white;");
	});
	
	// 鼠标移出关闭按钮
	var mouseout = $("#"+domId+" button[name='close']").mouseout(function(){
		$("#"+domId+" div[name='close']").attr("style","color:black;float:right;border:0px;background-color:white;");
	});
	
	// 关闭按钮点击事件
	var closeBox = $("#"+domId+" button[name='close']").click(function(){
		$("#"+domId+" div[name='box']").attr("style","display:none;");
	})
}

/**
 * 加载柱状图
 */
function loadBarStatis(domId) {
	var myChart = echarts.init(document.getElementById("div_"+domId));
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
		xAxis : [ {
			type : 'category',
			data : []
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : []
	}
	myChart.setOption(pieoption, true);
}

/**
 * 组装统计参数
 * @returns
 */
function buildStatistic(domId, data,width,height){
	var xwidth = 400;
	if (width){
		xwidth = width;
	}
	var xheight = 200;
	if (height){
		xheight = height;
	}
	$("#"+domId+" div[name='echarts_common_static']").css("width", xwidth);
	$("#"+domId+" div[name='echarts_common_static']").css("height", xheight);
	var myChart = echarts.init(document.getElementById("div_"+domId));
	myChart.resize();
	loadBarStatis(domId);
	var Xdata = new Array();
	var Ydata = new Array();
	var seriesData = new Array();
	// 组装Xdata
	var count = 0;
	for (var i = 0; i < data.resultList.length; i++) {
		if (data.resultList[i].tjmc == null) {
			count++;
			continue;
		}
		Xdata.push(data.resultList[i].tjmc);
	}
	// 判断字段数
	if (Xdata.length + count < data.resultList.length) {
		// 组装seriesData
		for (var i = 0; i < data.resultList.length; i++) {
			if (data.resultList[i].tjmc2 != null && seriesData.indexOf(data.resultList[i].tjmc2) == -1) {
				seriesData.push(data.resultList[i].tjmc2);
			}
		}
		// 组装series
		for (var i = 0; i < seriesData.length; i++) {
			var temData = new Array();
			for (var k = 0; k < Xdata.length; k++) {
				var tjsl = 0;
				for (var j = 0; j < data.resultList.length; j++) {
					if (Xdata[k] == data.resultList[j].tjmc && data.resultList[j].tjmc2 == seriesData[i]) {
						tjsl = data.resultList[j].tjsl;
						xname = data.resultList[j].xname;
					}
				}
				var tem = {"name":xname, "value":tjsl};
				temData.push(tem);
			}
			Ydata.push({
				name : seriesData[i],
				type : 'line',
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
		for (var i = 0; i < data.resultList.length; i++) {
			var xname = data.resultList[i].xname;
			var tem = {"name":xname, "value":data.resultList[i].tjsl};
			temData.push(tem);
		}
		Ydata.push({
			name : '数量',
			type : 'line',
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
	var myChart = echarts.init(document.getElementById("div_"+domId));
	myChart.setOption({
		grid : {
			left : '3%',
			right : '4%',
		},
		tooltip: {
	        trigger: 'axis',
	        formatter(params){
	            for(x in params){
	                return params[x].name +" : "+params[x].value;
	            }
	        },
	        axisPointer: {
	            type: 'shadow',
	            label: {
	                show: true
	            }
	        }
	        
	    },
		legend : {
			data : seriesData,
		},
		xAxis : [ {
			axisLabel:{ //展示角度
				rotate:60
			},
			data : Xdata,
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : Ydata
	});
}
