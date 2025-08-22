var PurOverTime_turnOff=true;
var Purchase_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#purOverTime_formSearch #purOverTime_list").bootstrapTable({
			url: $("#purOverTime_formSearch #urlPrefix").val()+'/purchase/purchase/pageGetListOverTime',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purOverTime_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "tmp.shys",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "qgid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'djh',
                title: '请购单号',
                width: '13%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '7%',
                align: 'left',
                visible:true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fzrmc',
                title: '负责人',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jlbh',
                title: '记录编号',
                width: '13%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shys',
                title: $("#purOverTime_formSearch #titleString").val(),
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqly',
                title: '申请理由',
                width: '25%',
                align: 'left',
                visible: true
            },{
                field: 'cszt',
                title: '状态',
                width: '7%',
                align: 'left',
                formatter:csztFormat,
                visible: true
            },{
                field: 'csbz',
                title: '超时备注',
                width: '25%',
                align: 'left',
                visible: true
            },{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter: czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	$.showDialog($("#purOverTime_formSearch #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+row.qgid,'查看请购信息',viewPurchaseConfig);
             },
		});
		$("#purOverTime_formSearch #purOverTime_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "tmp.sqrq", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return purchaseSearchData(map);
	}
	return oTableInit
}

function czFormat(value,row,index){
    var csbz = "";
    if(row.csbz){
        csbz=row.csbz;
    }
    return "<span class='btn btn-warning' onclick=\"disposeOn('" + row.qgid +"','" + row.cszt+ "','" + csbz+"')\" >处理</span>";
}

function disposeOn(qgid,cszt,csbz){
    $.showDialog($("#purOverTime_formSearch #urlPrefix").val()+"/purchase/purchase/pagedataDispose?qgid="+qgid+"&cszt="+cszt+"&csbz="+csbz,'处理',disposeConfig);
}

var disposeConfig = {
		width		: "800px",
		modalName	: "disposeConfigModal",
		formName	: "disposeForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "保存",
				className : "btn-primary",
				callback : function() {
					var $this = this;
                    var opts = $this["options"]||{};
					$("#disposeForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"disposeForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchPurchaseResult();
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

function csztFormat(value,row,index) {
    var cszt = "超时";
    if(row.cszt!=null && row.cszt!=""){
        if(row.cszt=="0"){
            cszt = "处理正常";
        }
    }
	if(row.qglx=='1'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\" ,{prefix:\"" + $('#purOverTime_formSearch #urlPrefix').val() + "\"})'>" + cszt + "</a>";
	}else{
		if(row.qglbdm=='MATERIAL' ||  row.qglbdm==null || row.qglbdm==''){
		    return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\" ,{prefix:\"" + $('#purOverTime_formSearch #urlPrefix').val() + "\"})'>" + cszt + "</a>";
		}else if(row.qglbdm=='SERVICE'){
		    return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\" ,{prefix:\"" + $('#purOverTime_formSearch #urlPrefix').val() + "\"})'>" + cszt + "</a>";
		}else if(row.qglbdm=='DEVICE'){
		    return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\" ,{prefix:\"" + $('#purOverTime_formSearch #urlPrefix').val() + "\"})'>" + cszt + "</a>";
		}else if(row.qglbdm=='OUTSOURCE'){
		    return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\" ,{prefix:\"" + $('#purOverTime_formSearch #urlPrefix').val() + "\"})'>" + cszt + "</a>";
        }
	}
}

function purchaseSearchData(map){
	var cxtj=$("#purOverTime_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#purOverTime_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["djh"]=cxnr
	}else if(cxtj=="2"){
		map["sqrmc"]=cxnr
	}else if(cxtj=="3"){
		map["sqbmmc"]=cxnr
	}else if(cxtj=="4"){
		map["jlbh"]=cxnr
	}else if(cxtj=="5"){
		map["sqly"]=cxnr
	}else if(cxtj=="6"){
        map["fzrmc"]=cxnr
    }
	var cszt = jQuery('#purOverTime_formSearch #cszt_id_tj').val();
	map["cszt"] = cszt;
	// 申请开始日期
	var sqrqstart = jQuery('#purOverTime_formSearch #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 申请结束日期
	var sqrqend = jQuery('#purOverTime_formSearch #sqrqend').val();
	map["sqrqend"] = sqrqend;
	return map;
}

/**
 * 查看页面模态框
 */
var viewPurchaseConfig={
	width		: "1600px",
	modalName	:"viewPurchaseModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
var PurOverTime_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#purOverTime_formSearch #btn_query");
		if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPurchaseResult(true);
            });
        }
        $("#purOverTime_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(PurOverTime_turnOff){
                $("#purOverTime_formSearch #searchMore").slideDown("low");
                PurOverTime_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#purOverTime_formSearch #searchMore").slideUp("low");
                PurOverTime_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    }
    return oInit;
};
/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchPurchaseResult(isTurnBack){
	//关闭高级搜索条件
	$("#purOverTime_formSearch #searchMore").slideUp("low");
	PurOverTime_turnOff=true;
	$("#purOverTime_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#purOverTime_formSearch #purOverTime_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purOverTime_formSearch #purOverTime_list').bootstrapTable('refresh');
	}
    var _url = $("#purOverTime_formSearch #urlPrefix").val() +"/purchase/purchase/pagedataGetOverTimeStatistics";
    var rqstart = $("#purOverTime_formSearch #sqrqstart").val();
    var rqend = $("#purOverTime_formSearch #sqrqend").val();
    $.ajax({
        type : "post",
        url : _url,
        data: { "access_token": $("#ac_tk").val(),"rqstart":rqstart,"rqend":rqend },
        dataType : "json",
        success : function(datas) {
            if(datas){
                for(var i=0; i<_renderArr.length; i++){
                    var _depotrender = _renderArr[i];
                    var _data = datas[_depotrender.id];
                    _depotrender.render(_data);
                }
                var timeliness = "";
                var sqrqTime = "";
                var sqrqYear = "";
                if(datas["timeliness"]!=null && datas["timeliness"]!=""){
                    timeliness = datas["timeliness"];
                }
                if(datas["sqrqTime"]!=null && datas["sqrqTime"]!=""){
                    sqrqTime = datas["sqrqTime"];
                }
                if(datas["sqrqYear"]!=null && datas["sqrqYear"]!=""){
                    sqrqYear = datas["sqrqYear"];
                }
                document.getElementById("timeliness").innerHTML = timeliness;
                document.getElementById("sqrqYear").innerHTML = sqrqYear;
                document.getElementById("sqrqYear2").innerHTML = sqrqYear;
                document.getElementById("sqrqTime").innerHTML = sqrqTime;
                document.getElementById("sqrqTime2").innerHTML = sqrqTime;
            }
        }
    });
}



//加载统计数据
var flModels = [];
var _renderArr = [];
var loadOverTimeStatis = function(){
	var _eventTag = "echarts";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_overTime_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	_renderArr = [
        {
			id:"Table",
			chart:null,
			el:null,
			render:function(data,searchData){
				var seriesData= new Array();
				if(data!=null){
					for (var i = 0; i < data.length; i++) {
						seriesData.push({value:data[i].count,name:data[i].cszt});
					}
				}
				var pieoption = {
					title : {
						x : 'center'
					},
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b}: \n{c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						left: 'left',
						top:'20'
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
									formatter : "{b}\n{d}%"
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
		},
        {
            id:"month",
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
                    for(var i = 0; i < 12; i++){
                        dataxAxis.push((i+1).toString()+"月");
                    }
                    for (var i = 0; i < 12; i++) {
                        var flag=false;
                        var num;
                        for (var j = 0; j < data.length; j++) {
                            if((i+1)==parseInt(data[j].sqrq)){
                                flag=true;
                                num=j;
                            }
                        }
                        if(flag){
                            dataseries.push(data[num].sl);
                            if(maxY < parseInt(data[num].sl)){
                                maxY = parseInt(data[num].sl);
                            }
                        }else{
                            dataseries.push("0");
                        }
                    }
                    maxY= maxY + 5 - maxY%5
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
                                axisLabel: {
                                    formatter: '{value}'
                                }
                            }
                        ],
                        series : [
                            {
                                name:'数量',
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
			id:"time",
			chart: null,
			el: null,
			render:function(data,searchData){

				var xAxis= new Array();
				var series=new Array();
				if($("#purOverTime_formSearch #unit").val()=="day"){
				    xAxis.push("超时小于1天");
                    xAxis.push("超时1-2天");
                    xAxis.push("超时2-3天");
                    xAxis.push("超时3-4天");
                    xAxis.push("超时4-5天");
                    xAxis.push("超时5-6天");
                    xAxis.push("超时大于6天");
				}else{
				    xAxis.push("超时小于1小时");
                    xAxis.push("超时1-2小时");
                    xAxis.push("超时2-3小时");
                    xAxis.push("超时3-4小时");
                    xAxis.push("超时4-5小时");
                    xAxis.push("超时5-6小时");
                    xAxis.push("超时大于6小时");
				}

				for(var j = 0; j < xAxis.length; j++){
                    var flag=false;
                    for (var i = 0; i < data.length; i++) {
                        if(xAxis[j]==data[i].sqrq){
                            series.push(data[i].sl);
                            flag=true;
                        }
                    }
                    if(!flag){
                        series.push(0);
                    }
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
					xAxis: [
						{
							type: 'category',
							data: xAxis,
							axisTick: {
								alignWithLabel: true
							}
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '数量'
						}
					],
					series: [
						{
							name: '数量',
							type: 'bar',
							label: {
								show: true,
								position: 'top'
							},
							barWidth: '60%',
							data: series
						}
					]
				};
				this.chart.setOption(pieoption);
				this.chart.hideLoading();
				//点击事件
				this.chart.on("click",function(){
				});
			}
		},
		{
            id:"percentage",
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
                    for(var i = 0; i < 12; i++){
                        dataxAxis.push((i+1).toString()+"月");
                    }
                    for (var i = 0; i < 12; i++) {
                        var flag=false;
                        var num;
                        for (var j = 0; j < data.length; j++) {
                            if((i+1)==parseInt(data[j].sqrq)){
                                flag=true;
                                num=j;
                            }
                        }
                        if(flag){
                            dataseries.push(data[num].sl);
                            if(maxY < parseInt(data[num].sl)){
                                maxY = parseInt(data[num].sl);
                            }
                        }else{
                            dataseries.push("0");
                        }
                    }
                    maxY= maxY + 5 - maxY%5
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
                                name: '百分比',
                                min: 0,
                                max: 100,
                                interval: 20,
                                axisLabel: {
                                    formatter: '{value}'
                                }
                            }
                        ],
                        series : [
                            {
                                name:'百分比',
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
		var _url = $("#purOverTime_formSearch #urlPrefix").val() +"/purchase/purchase/pagedataGetOverTimeStatistics";
		var rqstart = $("#purOverTime_formSearch #sqrqstart").val();
		var rqend = $("#purOverTime_formSearch #sqrqend").val();
		$.ajax({
			type : "post",
			url : _url,
			data: { "access_token": $("#ac_tk").val(),"rqstart":rqstart,"rqend":rqend },
			dataType : "json",
			success : function(datas) {
				if(datas){
					for(var i=0; i<_renderArr.length; i++){
						var _depotrender = _renderArr[i];
						var _data = datas[_depotrender.id];
						_depotrender.render(_data);
					}
					var timeliness = "";
					if(datas["timeliness"]!=null && datas["timeliness"]!=""){
					    timeliness = datas["timeliness"];
					}
					document.getElementById("timeliness").innerHTML = timeliness;
				}
			}
		});
	});

};




$(function(){
    loadOverTimeStatis();
	 // 1.初始化Table
	var oTable = new Purchase_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new PurOverTime_ButtonInit();
    oButtonInit.Init();
    laydate.render({
       elem: '#purOverTime_formSearch #sqrqstart'
      ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
       elem: '#purOverTime_formSearch #sqrqend'
      ,theme: '#2381E9'
    });
    jQuery('#purOverTime_formSearch .chosen-select').chosen({width: '100%'});
})