
var flModels = [];

//加载统计数据
var loadTrainStatis = function(){
    var _eventTag = "trainStatis";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "echarts_train_"; //id前缀
    var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [
        {
            id:"pxlb",
            chart:null,
            el:null,
            render:function(data){
                var seriesData= new Array();
                var dataAxis= new Array();
                if(data!=null){
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].pxlbmc);
                        seriesData.push({value:data[i].lbgs,name:data[i].pxlbmc});
                    }
                }
                var pieoption = {
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    //color:[ '#FECEFF','#2Fc6CB','#37D8FF'],
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        top:'20',
                        data: dataAxis
                    },
                    series : [
                        {
                            name: '',
                            type: 'pie',
                            radius : '45%',
                            center: ['50%', '60%'],
                            startAngle:45,
                            data:seriesData,
                            label: {
                                normal: {
                                    formatter:"{b}:\n {c}(个)"
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
            id:"pxyf",
            chart: null,
            el: null,
            width: 300,
            render:function(data,searchData){
                if(data&&this.chart){
                    var dataxAxis= new Array();
                    var newData=new Array();
                    var datars=new Array();
                    var maxY=0;
                    var maxBL=0;
                    var intval;
                    for (var i = 0; i < 12; i++) {
                        dataxAxis.push((i+1).toString()+'月');
                    }
                    for (var i = 0; i < 12; i++) {
                        var flag=false;
                        var num;
                        for (var j = 0; j < data.length; j++) {
                            if((i+1)==parseInt(data[j].lrsj)){
                                flag=true;
                                num=j;
                            }
                        }
                        if(flag){
                            newData.push(data[num].pxgs);
                            datars.push(data[num].pxrs);
                            if(parseInt(data[num].pxgs)>maxY){
                                maxY=parseInt(data[num].pxgs)+10;
                            }
                            if(parseInt(data[num].pxrs)>maxBL){
                                maxBL=parseInt(data[num].pxrs)+10;
                            }
                        }else{
                            newData.push("0");
                            datars.push("0");
                        }
                    }
                    var maxNum;
                    if(maxBL<maxY){
                        maxNum=maxY;
                    }else{
                        maxNum=maxBL;
                    }
                    var pieoption = {
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend: {
                            data: ['培训个数','培训人数'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y:35
                        },
                        yAxis:  [{
                            name: '培训个数',
                            type: 'value',
                            min: 0,
                            max: maxNum,
                            interval: maxNum,
                            axisLabel: {
                                formatter: '{value} 个'
                            }
                        },{
                                type: 'value',
                                name: '培训人数',
                                min: 0,
                                max: maxNum,
                                interval: maxNum,
                                axisLabel: {
                                    formatter: '{value} 个'
                                }
                            }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                name: '培训个数',
                                type: 'bar',
                                stack: '总量',
                                barGap: 0,
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom'
                                    }
                                },
                                data: newData
                            },
                            {
                                name:'培训人数',
                                type:'line',
                                yAxisIndex: 1,
                                data:datars,
                                itemStyle : { normal: {label : {show: false,formatter:"{c}个"}}}
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click",function(){
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
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
                    if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
                        return;
                    }
                    _cfg.chart.resize();
                }})(_render));
            }
        }
        //加载数据
        var _url = $("#trainStatis #urlPrefix").val() +"/train/train/pagedataTrainStatsInfo";
        $.ajax({
            type : "post",
            url : _url,
            data: { "access_token": $("#ac_tk").val(),"lrsj":$("#trainStatis #year option:selected").val() },
            dataType : "json",
            success : function(datas) {
                if(datas){
                    var _searchData = datas['gzglDtoList'];
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

    echartsBtnInit(_renderArr);
};


//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
    var url=$("#trainStatis #urlPrefix").val() + "/train/train/pagedataTrainStatsInfo";
    $("#trainStatis #btn_pxlb_query").unbind("click").click(function(){
        var val = $("#trainStatis #year option:selected").val();
        if(val){
            clickLeadEcharts(_renderArr,url,null,'pxyf');
        }else{
            loadTrainStatis()
        }
    })

}

//点击相应echarts获取相应数据
function clickLeadEcharts(_renderArr,_url,cs,id){
    for(var i=0; i<_renderArr.length; i++){
        if(id ==  _renderArr[i].id){
            var _render = _renderArr[i];
            //加载数据
            $.ajax({
                type : "post",
                url : _url,
                data: { "access_token": $("#ac_tk").val(),lrsj: $("#trainStatis #year option:selected").val()
                },
                dataType : "json",
                success : function(datas) {
                    if(datas){
                        var _searchData = datas['gzglDtoList'];
                        for(var i=0; i<_renderArr.length; i++){
                            var _render = _renderArr[i];
                            var _data = datas[_render.id];
                            if (_data && _data!="undefined"){
                                _render.render(_data,_searchData);
                            }
                        }
                    }
                }
            });
            break;
        }
    }
}


$(function(){
    loadTrainStatis();
    //所有下拉框添加choose样式
    jQuery('#trainStatis .chosen-select').chosen({width: '25%'});
});