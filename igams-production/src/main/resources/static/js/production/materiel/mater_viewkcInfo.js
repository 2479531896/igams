//加载统计数据
var loadWeekLeadStatis = function(){
    var _eventTag = "weekLeadStatis";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "mater_viewkcInfo_"; //id前缀
    var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [
        {
            id:"hqxxType",
            chart: null,
            el: null,
            render:function(data,searchData){
                if(data&&this.chart){
                    var dataxAxis= new Array();
                    var dataseries=new Array();
                    var maxY=0;
                    var interval;
                    for (var i = 0; i < data.length; i++) {
                        dataxAxis.push(data[i].htnbbh);
                        if (data[i].hq==""){
                            data[i].hq='0';
                        }
                        dataseries.push(data[i].hq);
                        if(parseInt(maxY) < parseInt(data[i].hq))
                            maxY = data[i].hq
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

                            bottom:'30%'
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataxAxis,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLabel:{ //展示角度
                                    rotate:30,

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
                                name: '天数',
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
                                name:'货期',
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
                    if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
                        return;
                    }
                    _cfg.chart.resize();
                }})(_render));
            }
        }

        //加载数据
        var _url = $("#mater_viewkcInfoForm #urlPrefix").val()+"/production/materiel/pagedataGetHq";
        var param ={};
        param["wlid"]= $("#mater_viewkcInfoForm #wlid").val();
        param["access_token"]=$("#ac_tk").val();
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
                                $("#mater_viewkcInfo_"+_render.id).parent().hide();
                            }
                        }else {
                            $("#mater_viewkcInfo_"+_render.id).parent().hide();
                        }
                    }
                }else{
                    //throw "loadClientStatis数据获取异常";
                }
            }
        });
    });
}

$(function(){
    loadWeekLeadStatis();
});