var flModels = [];
var _renderArr = [];
var loadHtOverTimeStatis = function(){
	var _eventTag = "echarts";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "echarts_grOverTime_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	_renderArr = [
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
                            if((i+1)==parseInt(data[j].cjrq)){
                                flag=true;
                                num=j;
                            }
                        }
                        if(flag){
                            dataseries.push(data[num].count);
                            if(maxY < parseInt(data[num].count)){
                                maxY = parseInt(data[num].count);
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
                                name: '审核用时',
                                min: 0,
                                axisLabel: {
                                    formatter: '{value}'
                                }
                            }
                        ],
                        series : [
                            {
                                name:'审核用时',
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
    });
};

var conOverTime_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#contractGrOverTime_Form #btn_query");
		if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPurchaseResult();
            });
        }
    }
    return oInit;
};

function searchPurchaseResult(){
    var _url = $("#contractGrOverTime_Form #urlPrefix").val() +"/contract/contract/pagedataGetGrOverTime";
    var shsjstart = $("#contractGrOverTime_Form #startTime").val();
    if(!shsjstart){
        $.error("请输入年份！");
        return false;
    }
    var selectElement = document.getElementById("auditType");
    var selectedValue = selectElement.value;
    if(!selectedValue){
        $.error("请选择审核类型！");
        return false;
    }
    var cxtj=$("#contractGrOverTime_Form #cxtj").val();
    var cxnr=$.trim(jQuery('#contractGrOverTime_Form #cxnr').val());
    if(!cxnr){
        $.error("请输入用户名或姓名！");
        return false;
    }
    var yhm = "";
    var zsxm = "";
    if(cxtj=="0"){
        yhm=cxnr;
    }else if(cxtj=="1"){
        zsxm=cxnr
    }
    var flg=$("#contractGrOverTime_Form #cxxz").val();
    if(!flg){
        $.error("请选择规则！");
        return false;
    }
    $.ajax({
        type : "post",
        url : _url,
        data: { "access_token": $("#ac_tk").val(),"shsjstart":shsjstart,"auditType":selectedValue,"yhm":yhm,"zsxm":zsxm,"flg":flg },
        dataType : "json",
        success : function(datas) {
            if(datas){
                for(var i=0; i<_renderArr.length; i++){
                    var _depotrender = _renderArr[i];
                    var _data = datas[_depotrender.id];
                    _depotrender.render(_data);
                }
            }
        }
    });
}


$(function(){
    loadHtOverTimeStatis();
    var oButtonInit = new conOverTime_ButtonInit();
    oButtonInit.Init();
    jQuery('#contractGrOverTime_Form .chosen-select').chosen({width: '100%'});
})