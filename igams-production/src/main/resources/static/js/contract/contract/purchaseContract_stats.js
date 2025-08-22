var flModels = [];
//加载统计数据
var loadWeekLeadStatis = function(){
    var _eventTag = "weekLeadStatis";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "echarts_week_lead_"; //id前缀
    var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [
    {
            id:"purchaseAudit",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var datays=new Array();
                    datays.push(data.bmjl);
                    datays.push(data.cggl);
                    datays.push(data.cgjl);
                    datays.push(data.gsld);
                    var datapjys=new Array();
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    var maxY=0;
                    var tmp_y=0;
                    var intval;
                    tmp_y = parseInt(data.bmjl)+parseInt(data.cggl)+parseInt(data.cgjl)+parseInt(data.gsld);
                    if(maxY < tmp_y)
                        maxY = parseInt(tmp_y)
                    maxY= parseInt(maxY) + 5 - parseInt(maxY%5)
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
                            data: ['岗位用时', '平均用时'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y:12
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top:50,
                            height:250,
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: ['部门经理','采购管理','采购经理','公司领导'],
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#d14a61'
                                    },
                                }
                            }
                        ],
                        yAxis:  [{
                            name: '用时(时)',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }],
                        series: [
                            {
                                name: '岗位用时',
                                type: 'line',
                                barGap: 0,
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        formatter: function (params) {
                                            if (params.value > 0) {
                                                return params.value;
                                            } else {
                                                return '';
                                            }
                                        }
                                    }
                                },
                                data: datays
                            },
                            {
                                name: '平均用时',
                                type: 'line',
                                stack: '用时',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        textStyle: { //数值样式
                                            fontSize: 12
                                        },
                                        formatter: function (params) {
                                            if (params.value > 0) {
                                                return params.value;
                                            } else {
                                                return '';
                                            }
                                        }
                                    }
                                },
                                data: datapjys,
                                itemStyle:{
                                    normal:{
                                        lineStyle:{
                                            width:2,
                                            type:'dashed'  //'dotted'点型虚线 'solid'实线 'dashed'线性虚线
                                        }
                                    }
                                },
                            }
                        ]
                    };
                    this.chart.clear();
                    this.chart.setOption(pieoption);
                }
            }
        },{
            id:"purchaseAuditToContractSubmit",
            chart:null,
            el:null,
            render:function(data,searchData){
                if(data!=null){
                    $("#purchaseContractStats #echarts_week_lead_purchaseAuditToContractSubmit").text(data.pjys);
                    var purchaseAuditToContractSubmit={
                        el:$(".purchaseAuditToContractSubmitChange"),
                        start:$("#purchaseContractStats #echarts_week_lead_purchaseAuditToContractSubmit").text(),//初始值
                        end:data.pjys//结束值
                    };
                    numChange(purchaseAuditToContractSubmit);
                }
            }
        },{
            id:"countOrders",
            chart:null,
            el:null,
            render:function(data,searchData){
                var seriesData= new Array();
                if(data!=null){
                    for (var i = 0; i < data.length; i++) {
                        seriesData.push({value:data[i].count,name:data[i].ywlxmc});
                    }
                }
                var pieoption = {
                    title : {
                        subtext : searchData.tjsjstart+'~'+searchData.tjsjend,
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
                                    formatter:"{b}: {c}(个)"
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
        },{
            id:"contractAudit",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var datays=new Array();
                    datays.push(data.cgjl);
                    datays.push(data.fwsp);
                    datays.push(data.gsld);
                    datays.push(data.kj);
                    var datapjys=new Array();
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    var maxY=0;
                    var tmp_y=0;
                    var intval;
                    tmp_y = parseInt(data.cgjl)+parseInt(data.fwsp)+parseInt(data.gsld)+parseInt(data.kj);
                    if(maxY < tmp_y)
                        maxY = parseInt(tmp_y)
                    maxY= parseInt(maxY) + 5 - parseInt(maxY%5)
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
                            data: ['岗位用时', '平均用时'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y:12
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top:50,
                            height:250,
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: ['采购经理','法务审批','公司领导','会计'],
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#d14a61'
                                    },
                                }
                            }
                        ],
                        yAxis:  [{
                            name: '用时(时)',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }],
                        series: [
                            {
                                name: '岗位用时',
                                type: 'line',
                                barGap: 0,
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        formatter: function (params) {
                                            if (params.value > 0) {
                                                return params.value;
                                            } else {
                                                return '';
                                            }
                                        }
                                    }
                                },
                                data: datays
                            },
                            {
                                name: '平均用时',
                                type: 'line',
                                stack: '用时',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        textStyle: { //数值样式
                                            fontSize: 12
                                        },
                                        formatter: function (params) {
                                            if (params.value > 0) {
                                                return params.value;
                                            } else {
                                                return '';
                                            }
                                        }
                                    }
                                },
                                data: datapjys,
                                itemStyle:{
                                    normal:{
                                        lineStyle:{
                                            width:2,
                                            type:'dashed'  //'dotted'点型虚线 'solid'实线 'dashed'线性虚线
                                        }
                                    }
                                },
                            }
                        ]
                    };
                    this.chart.clear();
                    this.chart.setOption(pieoption);
                }
            }
        },{
            id:"contractPay",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var datays=new Array();
                    datays.push(data.cgjl);
                    datays.push(data.cwqy);
                    datays.push(data.cwqe);
                    datays.push(data.gsld);
                    datays.push(data.cn);
                    var datapjys=new Array();
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    datapjys.push(data.pjys);
                    var maxY=0;
                    var tmp_y=0;
                    var intval;
                    tmp_y = parseInt(data.cgjl)+parseInt(data.cwqy)+parseInt(data.cwqe)+parseInt(data.gsld)+parseInt(data.cn);
                    if(maxY < tmp_y)
                        maxY = parseInt(tmp_y)
                    maxY= parseInt(maxY) + 5 - parseInt(maxY%5)
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
                            data: ['岗位用时', '平均用时'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y:12
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top:50,
                            height:250,
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: ['采购经理','财务签一','财务签二','公司领导','出纳'],
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#d14a61'
                                    },
                                }
                            }
                        ],
                        yAxis:  [{
                            name: '用时(时)',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }],
                        series: [
                            {
                                name: '岗位用时',
                                type: 'line',
                                barGap: 0,
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        formatter: function (params) {
                                            if (params.value > 0) {
                                                return params.value;
                                            } else {
                                                return '';
                                            }
                                        }
                                    }
                                },
                                data: datays
                            },
                            {
                                name: '平均用时',
                                type: 'line',
                                stack: '用时',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        textStyle: { //数值样式
                                            fontSize: 12
                                        },
                                        formatter: function (params) {
                                            if (params.value > 0) {
                                                return params.value;
                                            } else {
                                                return '';
                                            }
                                        }
                                    }
                                },
                                data: datapjys,
                                itemStyle:{
                                    normal:{
                                        lineStyle:{
                                            width:2,
                                            type:'dashed'  //'dotted'点型虚线 'solid'实线 'dashed'线性虚线
                                        }
                                    }
                                },
                            }
                        ]
                    };
                    this.chart.clear();
                    this.chart.setOption(pieoption);
                }
            }
        },{
            id:"contractAmount",
            chart:null,
            el:null,
            render:function(data,searchData){
                var seriesData= new Array();
                if(data!=null){
                    for (var i = 0; i < data.length; i++) {
                        seriesData.push({value:data[i].hjzye,name:data[i].sqbmmc});
                    }
                }
                var pieoption = {
                    title : {
                        subtext : searchData.tjsjstart+'~'+searchData.tjsjend,
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
//
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
                                    formatter:"{b}: {c}(元)"
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
        }, {
            id: "matterArrive",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var jsdhData = new Array();
                var namedata=new Array();
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].jsdhl){
                            var js = data[i].jsdhl;
                        }else js='0';
                        if (data[i].wlflmc){
                            var name=data[i].wlflmc;
                        }
                        else name='';
                        jsdhData.push(js);
                        namedata.push(name);

                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                        },
                        formatter: function (params) {//弹窗内容
                            var relVal=params[0].name;
                            for (let i = 0, l = params.length; i < l; i++) {
                                if (params[i].value==""){
                                    params[i].value = 0;
                                }
                                relVal += '<br/>' + params[i].marker  + params[i].seriesName + '  : ' + parseFloat(params[i].value)+'%'
                            }
                            return relVal;
                        },
                        legend: {
                            data: namedata,
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top:50,
                            height:250,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '百分比',
                            type: 'value',
                            min: 0,
                            max:100,
                            interval:10,
                        }],
                        xAxis: {
                            type: 'category',
                            data: namedata,
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#d14a61'
                                },
                            }
                        }, toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        series: [
                            {
                                data: jsdhData,
                                name: '及时到货',
                                type: 'bar',
                                stack: '总量',
                                barGap: 0,
                                itemStyle:{
                                    color: '#6DC8EC',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            }
                            ]

                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                }
            }
        },
        {
            id: "contractClassArrive",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var jsdhData = new Array();
                var namedata = new Array();
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].jsdhl) {
                            var js = data[i].jsdhl;
                        } else js = '0';
                        if (data[i].htlxmc) {
                            var name = data[i].htlxmc;
                        } else name = '';
                        jsdhData.push(js);
                        namedata.push(name);

                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                        },
                        formatter: function (params) {//弹窗内容
                            var relVal = params[0].name;
                            for (let i = 0, l = params.length; i < l; i++) {
                                if (params[i].value == "") {
                                    params[i].value = 0;
                                }
                                relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '%'
                            }
                            return relVal;
                        },
                        legend: {
                            data: namedata,
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top: 50,
                            height: 250,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '百分比',
                            type: 'value',
                            min: 0,
                            max: 100,
                            interval: 10,
                        }],
                        xAxis: {
                            type: 'category',
                            data: namedata,
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#d14a61'
                                },
                            }
                        }, toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        series: [
                            {
                                data: jsdhData,
                                name: '及时到货',
                                type: 'bar',
                                stack: '总量',
                                barGap: 0,
                                itemStyle: {
                                    color: '#6DC8EC',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                }
            }
        },
        {
            id: "chargePerArrivePer",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var jsdhData = new Array();
                var namedata = new Array();
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].jsdhl) {
                            var js = data[i].jsdhl;
                        } else js = '0';
                        if (data[i].fzrmc) {
                            var name = data[i].fzrmc;
                        } else name = '';
                        jsdhData.push(js);
                        namedata.push(name);

                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                        },
                        formatter: function (params) {//弹窗内容
                            var relVal = params[0].name;
                            for (let i = 0, l = params.length; i < l; i++) {
                                if (params[i].value == "") {
                                    params[i].value = 0;
                                }
                                relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '%'
                            }
                            return relVal;
                        },
                        legend: {
                            data: namedata,
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top: 50,
                            height: 250,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '百分比',
                            type: 'value',
                            min: 0,
                            max: 100,
                            interval: 10,
                        }],
                        xAxis: {
                            type: 'category',
                            data: namedata,
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#d14a61'
                                },
                            }
                        },  toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        series: [
                            {
                                data: jsdhData,
                                name: '及时到货',
                                type: 'bar',
                                stack: '总量',
                                barGap: 0,
                                itemStyle: {
                                    color: '#6DC8EC',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                }
            }
        },   {
            id: "supplierArrivePer",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var jsdhData = new Array();
                var namedata = new Array();
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].jsdhl) {
                            var js = data[i].jsdhl;
                        } else js = '0';
                        if (data[i].gysmc) {
                            var name = data[i].gysmc;
                        } else name = '';
                        jsdhData.push(js);
                        namedata.push(name);

                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                        },
                        formatter: function (params) {//弹窗内容
                            var relVal = params[0].name;
                            for (let i = 0, l = params.length; i < l; i++) {
                                if (params[i].value == "") {
                                    params[i].value = 0;
                                }
                                relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '%'
                            }
                            return relVal;
                        },
                        legend: {
                            data: namedata,
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: '4%',
                            right: '6%',
                            top: 50,
                            height: 250,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '百分比',
                            type: 'value',
                            min: 0,
                            max: 100,
                            interval: 10,
                        }],
                        xAxis: {
                            type: 'category',
                            data: namedata,
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#d14a61'
                                },
                            }
                        },  toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        series: [
                            {
                                data: jsdhData,
                                name: '及时到货',
                                type: 'bar',
                                stack: '总量',
                                barGap: 0,
                                itemStyle: {
                                    color: '#6DC8EC',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
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
                    if(!_cfg.chart||$(document).find(_cfg.chart.dom).length==0){//元素不存在，则不操作
                        return;
                    }
                    _cfg.chart.resize();
                }})(_render));
            }
        }
        //加载数据
        var _url = $("#purchaseContractStats #urlPrefix").val() +"/contract/contract/pagedataPurchaseContractStatsInfo?gysids="+ $("#purchaseContractStats #gysids").val();
        $.ajax({
            type : "post",
            url : _url,
            data: { "access_token": $("#ac_tk").val()},
            dataType : "json",
            success : function(datas) {
                var _searchData = datas['searchData'];
                if(datas){
                    for(var i=0; i<_renderArr.length; i++){
                        var _depotrender = _renderArr[i];
                        var _data = datas[_depotrender.id];
                        _depotrender.render(_data,_searchData);
                        if(datas[_render.id]&&datas[_render.id]!="searchData"){
                            if(datas[_render.id].length==0){
                                // $("#echarts_week_lead_"+_render.id).parent().hide();
                            }
                        }else {
                            $("#echarts_week_lead_"+_render.id).parent().hide();
                        }
                    }
                }else{
                }
            }
        });
    });
    echartsBtnInit(_renderArr);
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

//统计图中各按钮的点击事件初始化
function echartsBtnInit(_renderArr){
    var url=$("#purchaseContractStats #urlPrefix").val() +"/contract/contract/pagedataGetPurchaseContractStatsByTj";
    var map ={}
    map["access_token"]=$("#ac_tk").val();
    //请购审核用时查询按钮点击
    $("#purchaseContractStats #q #btn_query").click(function(){
        map["method"]="queryQ";
        map["shsjstart"] = $("#purchaseContractStats #q #qstartTime").val();
        map["shsjend"] = $("#purchaseContractStats #q #qendTime").val();
        clickLeadEcharts(_renderArr,url,map,'purchaseAudit');
    })
    //请购审核到合同提交用时查询按钮点击
    $("#purchaseContractStats #qc #btn_query").click(function(){
        map["method"]="queryQc";
        map["tjsjstart"] = $("#purchaseContractStats #qc #qcstartTime").val();
        map["tjsjend"] = $("#purchaseContractStats #qc #qcendTime").val();
        clickLeadEcharts(_renderArr,url,map,'purchaseAuditToContractSubmit');
    })
    //合同审核用时查询按钮点击
    $("#purchaseContractStats #cs #btn_query").click(function(){
        map["method"]="queryCs";
        map["shsjstart"] = $("#purchaseContractStats #cs #csstartTime").val();
        map["shsjend"] = $("#purchaseContractStats #cs #csendTime").val();
        clickLeadEcharts(_renderArr,url,map,'contractAudit');
    })
    //合同付款审核用时查询按钮点击
    $("#purchaseContractStats #cf #btn_query").click(function(){
        map["method"]="queryCf";
        map["shsjstart"] = $("#purchaseContractStats #cf #cfstartTime").val();
        map["shsjend"] = $("#purchaseContractStats #cf #cfendTime").val();
        clickLeadEcharts(_renderArr,url,map,'contractPay');
    })
    //物料到货及时率查询按钮点击
    $("#purchaseContractStats #wlfl #btn_query").click(function(){
        map["method"]="getMatterArrivePer";
        map["tjsjstart"] = $("#purchaseContractStats #wlfl #wlflstartTime").val();
        map["tjsjend"] = $("#purchaseContractStats #wlfl #wlflendTime").val();
        clickLeadEcharts(_renderArr,url,map,'matterArrive');
    })
    //合同类型到货及时率查询按钮点击
    $("#purchaseContractStats #htlx #btn_query").click(function(){
        map["method"]="getContractClassArrivePer";
        map["tjsjstart"] = $("#purchaseContractStats #htlx #htlxstartTime").val();
        map["tjsjend"] = $("#purchaseContractStats #htlx #htlxendTime").val();
        clickLeadEcharts(_renderArr,url,map,'contractClassArrive');
    })
    //合同负责人到货及时率查询按钮点击
    $("#purchaseContractStats #fzr #btn_query").click(function(){
        map["method"]="getChargePerArrivePer";
        map["tjsjstart"] = $("#purchaseContractStats #fzr #fzrstartTime").val();
        map["tjsjend"] = $("#purchaseContractStats #fzr #fzrendTime").val();
        clickLeadEcharts(_renderArr,url,map,'chargePerArrivePer');
    })
    //供应商到货及时率查询按钮点击
    $("#purchaseContractStats #gys #btn_query").click(function(){
        map["method"]="getSupplierArrivePer";
        map["tjsjstart"] = $("#purchaseContractStats #gys #gysstartTime").val();
        map["tjsjend"] = $("#purchaseContractStats #gys #gysendTime").val();
        map["gysids"] = $("#purchaseContractStats #gysids").val();
        clickLeadEcharts(_renderArr,url,map,'supplierArrivePer');
    })
    //统计订单个数的年统计按钮
    $("#purchaseContractStats #coy").unbind("click").click(function(){
        initEchartsLeadBtnCss("coy");
        map["method"]="getCountOrdersByYear";
        clickLeadEcharts(_renderArr,url,map,'countOrders');
    })
    //统计订单个数的月统计按钮
    $("#purchaseContractStats #com").unbind("click").click(function(){
        initEchartsLeadBtnCss("com");
        map["method"]="getCountOrdersByMon";
        clickLeadEcharts(_renderArr,url,map,'countOrders');
    })
    //统计订单个数的周统计按钮
    $("#purchaseContractStats #cow").unbind("click").click(function(){
        initEchartsLeadBtnCss("cow");
        map["method"]="getCountOrdersByWeek";
        clickLeadEcharts(_renderArr,url,map,'countOrders');
    })
    //统计订单个数的日统计按钮
    $("#purchaseContractStats #cod").unbind("click").click(function(){
        initEchartsLeadBtnCss("cod");
        map["method"]="getCountOrdersByDay";
        clickLeadEcharts(_renderArr,url,map,'countOrders');
    })
    //按部门统计合同金额的年统计按钮
    $("#purchaseContractStats #cay").unbind("click").click(function(){
        initEchartsLeadBtnCss("cay");
        map["method"]="getContractAmountByYear";
        clickLeadEcharts(_renderArr,url,map,'contractAmount');
    })
    //按部门统计合同金额月统计按钮
    $("#purchaseContractStats #cam").unbind("click").click(function(){
        initEchartsLeadBtnCss("cam");
        map["method"]="getContractAmountByMon";
        clickLeadEcharts(_renderArr,url,map,'contractAmount');
    })
    //按部门统计合同金额周统计按钮
    $("#purchaseContractStats #caw").unbind("click").click(function(){
        initEchartsLeadBtnCss("caw");
        map["method"]="getContractAmountByWeek";
        clickLeadEcharts(_renderArr,url,map,'contractAmount');
    })
    //按部门统计合同金额日统计按钮
    $("#purchaseContractStats #cad").unbind("click").click(function(){
        initEchartsLeadBtnCss("cad");
        map["method"]="getContractAmountByDay";
        clickLeadEcharts(_renderArr,url,map,'contractAmount');
    })

    /**
     * 选择供应商列表
     * @returns
     */
    $("#purchaseContractStats #gysonick").unbind("click").click(function(){
        var url=$("#purchaseContractStats #urlPrefix").val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
        $.showDialog(url,'选择供应商',addGysConfig);
    });
    var addGysConfig = {
        width		: "1200px",
        modalName	:"addGysModal",
        offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
        buttons		: {
            success : {
                label : "确 定",
                className : "btn-primary",
                callback : function() {
                    var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
                    if(sel_row.length == 0){
                        $.error("请至少选中一行");
                        return false;
                    }else{
                        for (var i=0;i<sel_row.length;i++){
                            var ids="";
                            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                                ids= ids + ","+ sel_row[i].gysid;
                            }
                            ids=ids.substr(1);
                        }
                        $("#purchaseContractStats #gysids").val(ids);
                        var url=$("#purchaseContractStats #urlPrefix").val() +"/contract/contract/pagedataGetPurchaseContractStatsByTj";
                        var map ={}
                        map["access_token"]=$("#ac_tk").val();
                        map["method"]="getSupplierArrivePer";
                        map["tjsjstart"] = $("#purchaseContractStats #gys #gysstartTime").val();
                        map["tjsjend"] = $("#purchaseContractStats #gys #gysendTime").val();
                        map["gysids"] = $("#purchaseContractStats #gysids").val();
                        clickLeadEcharts(_renderArr,url,map,'supplierArrivePer');
                        $("#purchaseContractStats #gys").show();
                    }
                },
            },
            cancel : {
                label : "关 闭",
                className : "btn-default"
            }
        }
    };

}
//设置按钮样式
function initEchartsLeadBtnCss(getid,_renderArr){
    var obj = $("#purchaseContractStats #"+getid)
    var gettitle=obj.attr("title");
    var a=$("#purchaseContractStats #"+getid).siblings();
    var ah=a.length;
    for(var i=0;i<ah;i++){
        var otherid=a[i].id;
        $("#purchaseContractStats #"+otherid).removeClass("bechoosed");
        $("#purchaseContractStats #"+otherid+" .fa").attr("style","");
    }
    //点击统计图按钮，按照全部查询，并显示相应统计图
    //Class="bechoosed"用于处理鼠标移出时导致被选中的按钮样式也被移除的问题
    if(gettitle=="年"){
        $("#purchaseContractStats #"+getid+" .roundq").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
        $("#purchaseContractStats #"+getid).addClass("bechoosed");
    }else if(gettitle=="月"){
        $("#purchaseContractStats #"+getid+" .roundm").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
        $("#purchaseContractStats #"+getid).addClass("bechoosed");
    }else if(gettitle=="周"){
        $("#purchaseContractStats #"+getid+" .roundw").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
        $("#purchaseContractStats #"+getid).addClass("bechoosed");
    }else if(gettitle=="日"){
        $("#purchaseContractStats #"+getid+" .roundd").attr("style","color: #20dbfd;text-shadow: 0 0 25px #00d8ff;");
        $("#purchaseContractStats #"+getid).addClass("bechoosed");
    }
}


$(function(){
    // getheight();
    loadWeekLeadStatis();
    //所有下拉框添加choose样式
    jQuery('#purchaseContractStats .chosen-select').chosen({width: '100%'});

    laydate.render({
        elem: '#qstartTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#qendTime'
        ,theme: '#2381E9',
    });

    laydate.render({
        elem: '#qcstartTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#qcendTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#csstartTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#csendTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#cfstartTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#cfendTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#wlflstartTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#wlflendTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#htlxstartTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#htlxendTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#fzrstartTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#fzrendTime'
        ,theme: '#2381E9',
    });
    //添加日期控件
    laydate.render({
        elem: '#gysstartTime'
        ,theme: '#2381E9',
    });
    laydate.render({
        elem: '#gysendTime'
        ,theme: '#2381E9',
    });
});