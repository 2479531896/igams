var flModels = [];
$("#daliyStatis div[name='echarts_day_lead_jcdwccs']").each(function(){
	var map = {}
	map["id"] = "jcdwccs_"+$("#"+this.id).attr("dm");
	map["chart"] = null;
	map["el"] = null;
	map["render"] = function(data,searchData){
		var prerq = "";
		if(data&&this.chart){
			var dataxAxis= new Array();
			var datasf=new Array();
			var databsf=new Array();
			var datahj=new Array();
			var datalegend=new Array();
			var maxY=0;
			var tmp_y=0;
			var intval;

			var datalist = new Array();
			for(var i=0;i<data.length;i++){
				if(data[i].sfjs=='0'){
					datalist.push({value:data[i].num,name:'废弃'});
				}else if(data[i].sfsf=='0'){
					datalist.push({value:data[i].num,name:'不收费'});
				}else if(data[i].sfsf=='1'){
					datalist.push({value:data[i].num,name:'收费'});
				}
			}
			var pieoption = {
					title : {
						subtext : searchData.jsrq,
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
			this.chart.clear();
			this.chart.setOption(pieoption);
		}
		this.chart.hideLoading();
		this.chart.on('click',function(e){
		});
	};
	flModels.push(map);
});

var render;
//加载统计数据
var loadWeekLeadStatis = function(){
	var _eventTag = "dayLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_day_lead_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
  		{
			id:"jcdwccs",
			chart: null,
			el: null,
			render:function(data,searchData){
				for(var i=0;i<data.length;i++){
					data[i].jcdwmc=data[i].jcdwmc.replace("实验室","");
				}
				var predb = "";
				if(data&&this.chart){
					var datalist = new Array();
					for(var i=0;i<data.length;i++){
						if(data[i].sum != 0)
							datalist.push({value:data[i].sum,name:data[i].jcdwmc});
					}
					var pieoption = {
						title : {
							subtext : searchData.jsrq,
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
		var _url = "/ws/statistics/getCssxxByJcdwAndJsrq";
		$.ajax({
			type : "post",
			url : _url,
			data:{"sign":$("#sign").val(),"jsrq":$("#jsrq").val()},
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
	
	render=_renderArr;
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

function init(){

}

function daily_back(){
	var jsrq=$("#daliyStatis #jsrq").val();
	$("#daliyStatis").load("/wechat/statistics/pageListLocal_daliy_jsrq?jsrq="+jsrq);
}
$(function(){
	init();
	loadWeekLeadStatis();
	//所有下拉框添加choose样式
	jQuery('#weekly_Statis_jcdw .chosen-select').chosen({width: '100%'});
});