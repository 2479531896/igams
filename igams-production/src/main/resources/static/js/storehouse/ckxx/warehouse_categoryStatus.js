
var catrgoryFlModels = [];

//加载统计数据
var catrgoryLoadWeekLeadStatis = function(){
	var _eventTag = "catrgoryWeekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_week_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _catrgoryRenderArr = [
		{
			id:"wllb",
			chart: null,
			el: null,
			categoryRender:function(data,searchData){

				var xAxis= new Array();
				var sl=new Array();
				var zl=new Array();
				for (var i = 0; i < data.length; i++) {
					xAxis.push(data[i].lbmc);
					zl.push(data[i].zl);
					sl.push(data[i].sl);

				}
				var pieoption = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					legend: {
						data: ['种类', '数量']
					},
					xAxis: [
						{
							type: 'category',
							data: xAxis,
							axisLabel: {
								rotate: 35,
								margin: 15,
								fontSize: 10,
							},
						}
					],
					yAxis: [
						{
							type: 'value'
						}
					],
					series: [
						{
							name: '种类',
							type: 'bar',
							barWidth: '20%',
							data: zl
						},{
							name: '数量',
							type: 'bar',
							barWidth: '20%',
							data: sl
						}

					]
				};
				this.chart.clear();
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				//点击事件
				this.chart.off("click");
				this.chart.on("click",function(param){
					if ($("#categoryStatus #lx").val()){
						$.showDialog($("#depotStatus #urlPrefix").val() +"/warehouse/warehouse/itemInfo?lbmc="+param.name+"&access_token="+$("#ac_tk").val()+"&khmc="+$("#categoryStatus #khmc").val()+"&ckmc="+$("#categoryStatus #ckmc").val()+"&jgmc="+$("#categoryStatus #jgmc").val()+"&lx="+$("#categoryStatus #lx").val()+"&startTime="+startTime+"&endTime="+endTime,"物料详情"+param.seriesName,itemInfoConfig);
					}
				});
			}

		}
	];

	for (var i = 0; i < catrgoryFlModels.length; i++) {
		_catrgoryRenderArr.push(catrgoryFlModels[i]);
	}
	// 路径配置
	setTimeout(function(){
		for(var i=0; i<_catrgoryRenderArr.length; i++){
			var _render = _catrgoryRenderArr[i];
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
		var _url = $("#categoryStatus #urlPrefix").val() +"/warehouse/warehouse/categoryStatusInfo";
		$.ajax({
			type : "post",
			url : _url,
			data: { "access_token": $("#ac_tk").val(),"ckmc":$("#categoryStatus #ckmc").val(),"khmc":$("#categoryStatus #khmc").val(),"jgmc":$("#categoryStatus #jgmc").val(),"lx":$("#categoryStatus #lx").val(),"startTime":startTime,"endTime":endTime},
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['gzglDtoList'];
					for(var i=0; i<_catrgoryRenderArr.length; i++){
						var _render = _catrgoryRenderArr[i];
						var _data = datas[_render.id];
						_render.categoryRender(_data,_searchData);
					}
				}else{
				}
			}
		});
	});
}

itemInfoConfig = {
	width		: "1600px",
	modalName	:"itemInfoModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

let startTime ="";
let endTime ="";
function init(){
	if($("#categoryStatus #id").val() == "lc"){
		startTime = $("#depotStatus #lc #lcstartTime").val();
		endTime =  $("#depotStatus #lc #lcendTime").val();
	}else if ($("#categoryStatus #id").val() == "bm"){
		startTime = $("#depotStatus #bm #startTime").val();
		endTime =  $("#depotStatus #bm #endTime").val();
	}else if ($("#categoryStatus #id").val() == "zt"){
		startTime = $("#depotStatus #zt #ztstartTime").val();
		endTime =  $("#depotStatus #zt #ztendTime").val();
	}else if ($("#categoryStatus #id").val() == "khfh"){
		startTime = $("#depotStatus #khfh #khfhstartTime").val();
		endTime =  $("#depotStatus #khfh #khfhendTime").val();
	}else if ($("#categoryStatus #id").val() == "khjysl"){
		startTime = $("#depotStatus #khjysl #khjyslstartTime").val();
		endTime =  $("#depotStatus #khjysl #khjyslendTime").val();
	}
}
$(function(){
	init();
	catrgoryLoadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#categoryStatus .chosen-select').chosen({width: '100%'});
});