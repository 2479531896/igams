var render;
var flModels = [];
$("#retentionStasis_formSearch div[name='echarts_retentionEquipment']").each(function(){
    var map = {}
    map["id"] = $("#"+this.id).attr("dm");
    map["chart"] = null;
    map["el"] = null;
    map["render"] = function(data,searchData){
        var prerq = "";
        if(data&&map.chart){
            var dataX=new Array();
            var datawd=new Array();
            var datazgwd=new Array();
            var datazdwd=new Array();
            var datasd=new Array();
            var maxY=0;
            var minY=0;
            for (var i = 0; i < data.length; i++) {
                dataX.push(data[i].jlsj)
                datawd.push(data[i].wd)
                datazgwd.push(data[i].zgwd)
                datazdwd.push(data[i].zdwd)
                datasd.push(data[i].sd)
                if ( parseInt(data[i].sd)>maxY){
                    maxY =  parseInt(data[i].sd)
                }
                if ( parseInt(data[i].zdwd)>maxY){
                    maxY =  parseInt(data[i].zdwd)
                }
                if ( parseInt(data[i].zgwd)>maxY){
                    maxY =  parseInt(data[i].zgwd)
                }
                if ( parseInt(data[i].wd)>maxY){
                    maxY =  parseInt(data[i].wd)
                }
                if ( parseInt(data[i].sd)<minY){
                    minY =  parseInt(data[i].sd)
                }
                if ( parseInt(data[i].zdwd)<minY){
                    minY =  parseInt(data[i].zdwd)
                }
                if ( parseInt(data[i].zgwd)<minY){
                    minY =  parseInt(data[i].zgwd)
                }
                if ( parseInt(data[i].wd)<minY){
                    minY =  parseInt(data[i].wd)
                }
            }
            var intval;
            maxY= parseInt(maxY) + 10 - parseInt(maxY%5)
            minY= parseInt(minY)==0?0:parseInt(minY) - 10
            intval=maxY/5
            var pieoption = {
                tooltip : {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                legend: {
                    data: ['温度', '最高温度','最低温度','湿度'],
                    textStyle: {
                        color: '#3E9EE1'
                    },
                    y:12
                },
                toolbox: {
                    show : true,
                    feature : {
                        saveAsImage : {show: true}
                    }
                },
                grid: {
                    left: '4%',
                    right: '6%',
                    top:50,
                    height:450,
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: dataX,
                        axisTick: {
                            alignWithLabel: true
                        },
                        axisLabel:{ //展示角度
                            rotate:45,

                        },
                        axisLine: {
                            lineStyle: {
                                color: '#d14a61'
                            },
                        }
                    }
                ],
                yAxis:  [{
                    name: '°C',
                    type: 'value',
                    min: minY,
                    max: maxY,
                    interval: intval,
                    axisLabel: {
                        formatter: '{value}'
                    }
                }],
                series: [
                    {
                        name: '温度',
                        type: 'line',
                        barGap: 0,
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                            }
                        },
                        data: datawd
                    }, {
                        name: '最高温度',
                        type: 'line',
                        barGap: 0,
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                            }
                        },
                        data: datazgwd
                    }, {
                        name: '最低温度',
                        type: 'line',
                        barGap: 0,
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                            }
                        },
                        data: datazdwd
                    }, {
                        name: '湿度',
                        type: 'line',
                        barGap: 0,
                        label: {
                            normal: {
                                show: true,
                                position: 'top',
                            }
                        },
                        data: datasd
                    }
                ]
            };
            map.chart.clear();
            map.chart.setOption(pieoption);
        }
        map.chart.hideLoading();
        map.chart.on('click',function(e){
        });
    };
    flModels.push(map);
});
//加载统计数据
var loadRetentionEquipmentStatis = function(){
    var _eventTag = "weekLeadStatis";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "echarts_retentionEquipment_"; //id前缀
    var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [];
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
                    if (!_cfg.chart.dom){
                        return;
                    }
                    if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
                        return;
                    }
                    _cfg.chart.resize();
                }})(_render));
            }
        }
        //加载数据
        var _url = $("#retentionStasis_formSearch #urlPrefix").val()+"/retention/retention/pagedataStatisticsRetention";
        var param ={};
        searchRetention(param)

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
                        if (datas[_render.id]) {
                            if (datas[_render.id].length == 0) {
                                $("#echarts_retentionEquipment_" + _render.id).parent().hide();
                            }
                            else {
                                $("#echarts_retentionEquipment_" + _render.id).parent().show();
                            }
                        } else {
                            $("#echarts_retentionEquipment_" + _render.id).parent().hide();
                        }
                    }
                }else{

                }
            }
        });
    });
    render=_renderArr;
}
//点击查询事件
$("#retentionStasis_formSearch #btn_query").unbind("click").click(function(e){
    RetentionEquipmentStatisResult(true);
    loadRetentionEquipmentStatis();
});
function searchRetention(param){
    //获取当前选中条件
    var bhlbmcs=$("#retentionStasis_formSearch #bhlbmcs_id_tj").val().replaceAll("'","");
    var jlsjstart=$("#retentionStasis_formSearch #jlsjstart").val();
    var jlsjend=$("#retentionStasis_formSearch #jlsjend").val();
    param["bhlbmcs"] = bhlbmcs;
    param["jlsjstart"] = jlsjstart;
    param["jlsjend"] = jlsjend;
    param["access_token"] = $("#ac_tk").val();
    return true;
}

var retentionStatis_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_query = $("#retentionStasis_formSearch #btn_query");
        //添加日期控件
        laydate.render({
            elem: '#jlsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jlsjend'
            ,theme: '#2381E9'
        });

        /*-----------------------模糊查询------------------------------------*/
        /**显示隐藏**/
        $("#retentionStasis_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(retentionStasis_turnOff){
                $("#retentionStasis_formSearch #searchMore").slideDown("low");
                retentionStasis_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#retentionStasis_formSearch #searchMore").slideUp("low");
                retentionStasis_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};
var retentionStasis_turnOff=true;

function RetentionEquipmentStatisResult(isTurnBack){
    //关闭高级搜索条件
    $("#retentionStasis_formSearch #searchMore").slideUp("low");
    retentionStasis_turnOff=true;
    $("#retentionStasis_formSearch #sl_searchMore").html("高级筛选");
}
$(function () {
    //所有下拉框添加choose样式
    jQuery('#retentionStasis_formSearch .chosen-select').chosen({width: '100%'});
    $("#retentionStasis_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    var retentionStatisButtonInit = new retentionStatis_ButtonInit();
    retentionStatisButtonInit.Init();
    loadRetentionEquipmentStatis();
});