var count = 1;
function closeNav() {
	$("#ajaxForm #t_div").animate({left:'-380px'},"slow");
	$("#ajaxForm #cjrqstart").val('')
	$("#ajaxForm #cjrqend").val('')
}
function viewImg(wlid){
	laydate.render({
		elem: '#ajaxForm #cjrqstart'
		,theme: '#2381E9'
		,trigger: 'click'
	});
	//添加日期控件
	laydate.render({
		elem: '#ajaxForm #cjrqend'
		,theme: '#2381E9'
		,trigger: 'click'
	});
	loadWeekLeadStatis(wlid);
	$("#ajaxForm #t_div").animate({left:'0px'},"slow");
	$("#ajaxForm #lswlid").val(wlid)
	if (count==1){
		// 1.初始化Table
		var oTable = new rkmx_TableInit();
		oTable.Init();
		count++;
	}else {
		$('#ajaxForm #rkmxTable').bootstrapTable('refresh',{pageNumber:1});
	}
}


var rkmx_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#ajaxForm #rkmxTable').bootstrapTable({
			url: $("#ajaxForm #urlPrefix").val()+'/ws/production/getContractArrivalInfo',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "htgl.cjrq",				//排序字段
			sortOrder: "DESC",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 5,                       //每页的记录行数（*）
			pageList: [5, 10, 15],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "htid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				titleTooltip:'序',
				width: '1%',
				align: 'left',
				visible: true,
				formatter: function (value, row, index) {
					return index+1;
				},
			},{
				field: 'htnbbh',
				title: '合同内部编号/供应商',
				width: '80%',
				align: 'left',
				visible: true,
				formatter:htnbbhformatter
			},{
				field: 'dhsl',
				title: '入库数量/金额(元)',
				width: '5%',
				align: 'left',
				visible: true,
				formatter:dhslformatter
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
	};

	// 得到查询的参数
	oTableInit.queryParams = function(params){
		// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
		// 例如 toolbar 中的参数
		// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
		// limit, offset, search, sort, order
		// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
		// 返回false将会终止请求
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "htgl.lrsj", // 防止同名排位用
			sortLastOrder: "desc", // 防止同名排位用
			cjrqstart:$("#ajaxForm #cjrqstart").val(),
			cjrqend:$("#ajaxForm #cjrqend").val(),
			wlid:$("#ajaxForm #lswlid").val()
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
};
function htnbbhformatter(value,row,index){
	var html="<span style='width:70%;line-height:24px;' title='"+row.htnbbh+"'>"+row.htnbbh+"</span>";
	html+="<br/>";
	html+="<span style='width:70%;line-height:24px;' title='"+row.gysmc+"'>"+row.gysmc+"</span>";
	return html;
}
function dhslformatter(value,row,index){
	var html="<span style='width:70%;line-height:24px;' title='"+row.dhsl+'('+row.jldw+')'+"'>"+row.dhsl+'('+row.jldw+')'+"</span>";
	html+="<br/>";
	html+="<span style='width:70%;line-height:24px;' title='"+row.hsdj+'(元)'+"'>"+row.hsdj+'(元)'+"</span>";
	return html;
}
//加载统计数据
var loadWeekLeadStatis = function(wlid){
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "wlStatis_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"hsdjxx",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var dataxAxis= new Array();
					var dataseries=new Array();
					var maxY=0;
					var interval;
					for (var i = 0; i < data.length; i++) {
						dataxAxis.push(data[i].xname);
						if (data[i].tjsl==""){
							data[i].tjsl='0';
						}
						dataseries.push(data[i].tjsl);
						if(parseInt(maxY) < parseInt(data[i].tjsl))
							maxY = data[i].tjsl
					}
					maxY= parseInt(maxY) + data.length - parseInt(maxY%(data.length))
					interval=maxY/data.length

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
						grid: {
							left:'20%',
							bottom:'50%'
						},
						xAxis : [
							{
								type : 'category',
								data:dataxAxis,
								axisTick: {
									alignWithLabel: true
								},
								axisLabel:{ //展示角度
									rotate:60
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
								name: '金额(元)',
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
								name:'金额(元)',
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
		}
	];
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
					if(!_cfg.chart||!_cfg.chart.dom||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = $("#ajaxForm #urlPrefix").val()+"/ws/statistic/pagedataStatisticTaxPrice";
		var param ={};
		param["wlid"]= wlid;
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
								$("#wlStatis_"+_render.id).parent().hide();
							}
						}else {
							$("#wlStatis_"+_render.id).parent().hide();
						}
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
}
function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	var urlPrefix= $("#ajaxForm #urlPrefix").val();
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=urlPrefix+"/ws/sjxxpripreview?pageflg=1&fjid="+fjid
		window.location.href=url;
	}else if(type.toLowerCase()==".pdf"){
		var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
		window.location.href=url;
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
/*function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}*/
function xz(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    var spxz = $("#ajaxForm #spxz").val();
    if (spxz=='0'&&type.toLowerCase()==".mp4"){
        $.error("该视频不允许点击下载")
    }else {
        var url =  $("#ajaxForm #urlPrefix").val() + "/ws/production/downloadFile?fjid="+fjid;
        window.location.href=url;
    }
}

$(document).ready(function(){
	$("#ajaxForm  #btn_query").click(function(){
		$('#ajaxForm #rkmxTable').bootstrapTable('refresh',{pageNumber:1});
	});
});


