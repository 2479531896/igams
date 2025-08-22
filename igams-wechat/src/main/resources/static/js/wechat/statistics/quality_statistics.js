var flModels = [];
var pagedatas;
var laterpagedatas;
var temp;
var sign;
var pagedatasMoney;
var qdmf;
var ywlxmf;
//加载统计数据
var loadWeekLeadStatis = function () {
    var _eventTag = "qualityStatis";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "echarts_quality_"; //id前缀
    var _statis_theme = "macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [
        {
            id: "channelChargeList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var prerq = "";
                var dataxAxis = new Array();
                var datadl = new Array();
                var datazx = new Array();
                var datadsf = new Array();
                var datahj = new Array();
                var maxY = 0;
                var intval;
                var tmp_y = 0;
                var dlnum = 0;
                var zxnum = 0;
                var dsfnum = 0;
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        var zx = data[i].直销;
                        var dls = data[i].代理商;
                        var dsf = data[i].第三方;
                        if (data[i].rq.length > 8) {
                            dataxAxis.push(data[i].rq.substring(5, 10));
                        } else {
                            dataxAxis.push(data[i].rq);
                        }
                        if (zx==undefined){
                            zx = 0;
                        }
                        if (dls ==undefined){
                            dls = 0;
                        }
                        if (dsf ==undefined){
                            dsf = 0;
                        }
                        if (zx == 0) {
                            datazx.push('');
                        } else if (zx != 0) {
                            datazx.push(zx);
                        }
                        if (dls == 0) {
                            datadl.push('');
                        } else if (dls != 0) {
                            datadl.push(dls);
                        }
                        if (dsf == 0) {
                            datadsf.push('');
                        } else if (dsf != 0) {
                            datadsf.push(dsf);
                        }
                        datahj.push(parseInt(zx) + parseInt(dls) + parseInt(dsf))
                    }
                    for (var i = 0; i < datazx.length; i++) {
                        if (datazx[i] != "") {
                            if (datazx[i] > zxnum) {
                                zxnum = datazx[i];
                            }
                        }
                        if (datadl[i] != "") {
                            if (datadl[i] > dlnum) {
                                dlnum = datadl[i];
                            }
                        }
                        if (datadsf[i] != "") {
                            if (datadsf[i] > dsfnum) {
                                dsfnum = datadsf[i];
                            }
                        }
                    }
                    maxY = parseInt(zxnum) + parseInt(dlnum) + parseInt(dsfnum);
                    maxY = parseInt(maxY) + 4 - parseInt(maxY % 4)
                    intval = maxY / 4
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.jcxmnum,
                        //     x : 'left'
                        // },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l-1; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    if (i<l-1){
                                        //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                            parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
                                    }else {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
                                    }
                                }
                                return relVal;
                            }
                        },
                        legend: {
                            data: ['直销', '代理', '第三方'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: 10,
                            right: 0,
                            top: 50,
                            height: 300,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '个数',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval
                        }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                data: datazx.map(function (item,index) {
                                    return{
                                        value:item,
                                        total:datahj[index],
                                    }

                                }),
                                name: '直销',
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
                            },
                            {
                                data: datadl,
                                name: '代理',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#945FB9',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            },
                            {
                                data: datadsf,
                                name: '第三方',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#FF9845',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            },
                            {
                                name: '合计',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                        textStyle: {color: '#ff4848', fontSize: 12}
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: 'rgba(128, 128, 128, 0)'
                                    }
                                },
                                data: datahj
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id: "businessTypeChargeList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var prerq = "";
                var dataxAxis = new Array();
                var dataky = new Array();
                var datary = new Array();
                var datatj = new Array();
                var datahj = new Array();
                var maxY = 0;
                var intval;
                var tmp_y = 0;
                var kynum = 0;
                var rynum = 0;
                var tjnum = 0;
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        var ky = data[i].科研;
                        var ry = data[i].入院;
                        var tj = data[i].特检;
                        if (data[i].rq.length > 8) {
                            dataxAxis.push(data[i].rq.substring(5, 10));
                        } else {
                            dataxAxis.push(data[i].rq);
                        }
                        if (ky==undefined){
                            ky = 0;
                        }
                        if (ry ==undefined){
                            ry = 0;
                        }
                        if (tj ==undefined){
                            tj = 0;
                        }
                        if (ky == 0) {
                            dataky.push('');
                        } else if (ky != 0) {
                            dataky.push(ky);
                        }
                        if (ry == 0) {
                            datary.push('');
                        } else if (ry != 0) {
                            datary.push(ry);
                        }
                        if (tj == 0) {
                            datatj.push('');
                        } else if (tj != 0) {
                            datatj.push(tj);
                        }
                        datahj.push(parseInt(ky) + parseInt(ry) + parseInt(tj))
                    }
                    for (var i = 0; i < dataky.length; i++) {
                        if (dataky[i] != "") {
                            if (dataky[i] > kynum) {
                                kynum = dataky[i];
                            }
                        }
                        if (datary[i] != "") {
                            if (datary[i] > rynum) {
                                rynum = datary[i];
                            }
                        }
                        if (datatj[i] != "") {
                            if (datatj[i] > tjnum) {
                                tjnum = datatj[i];
                            }
                        }
                    }
                    maxY = parseInt(kynum) + parseInt(rynum) + parseInt(tjnum);
                    maxY = parseInt(maxY) + 4 - parseInt(maxY % 4)
                    intval = maxY / 4
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.jcxmnum,
                        //     x : 'left'
                        // },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l-1; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    if (i<l-1){
                                        //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                            parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
                                    }else {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
                                    }
                                }
                                return relVal;
                            }
                        },
                        legend: {
                            data: ['科研', '入院', '特检'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: 10,
                            right: 0,
                            top: 50,
                            height: 300,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '个数',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval
                        }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                data: dataky,
                                name: '科研',
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
                                    },
                                },
                            },
                            {
                                data: datary,
                                name: '入院',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#945FB9',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    },
                                },
                            },
                            {
                                data: datatj,
                                name: '特检',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#FF9845',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    },
                                },
                            },
                            {
                                name: '合计',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                        textStyle: {color: '#ff4848', fontSize: 12}
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: 'rgba(128, 128, 128, 0)'
                                    }
                                },
                                data: datahj
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id: "channelChargeSfMfList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var prerq = "";
                var dataxAxis = new Array();
                var datasf = new Array();
                var databsf = new Array();
                var datahj = new Array();
                var maxY = 0;
                var intval;
                var tmp_y = 0;
                var sfnum = 0;
                var bsfnum = 0;
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].rq.length > 8) {
                            dataxAxis.push(data[i].rq.substring(5, 10));
                        } else {
                            dataxAxis.push(data[i].rq);
                        }
                        if (data[i].sfnum == 0) {
                            datasf.push('');
                        } else if (data[i].sfnum != 0) {
                            datasf.push(data[i].sfnum);
                        }
                        if (data[i].bsfnum == 0) {
                            databsf.push('');
                        } else if (data[i].bsfnum != 0) {
                            databsf.push(data[i].bsfnum);
                        }
                        datahj.push(parseInt(data[i].bsfnum) + parseInt(data[i].sfnum))
                    }
                    for (var i = 0; i < datasf.length; i++) {
                        if (datasf[i] != "") {
                            if (datasf[i] > sfnum) {
                                sfnum = datasf[i];
                            }
                        }
                        if (databsf[i] != "") {
                            if (databsf[i] > bsfnum) {
                                bsfnum = databsf[i];
                            }
                        }
                    }
                    maxY = parseInt(sfnum) + parseInt(bsfnum);
                    maxY = parseInt(maxY) + 4 - parseInt(maxY % 4)
                    intval = maxY / 4
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l-1; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    if (i<l-1){
                                        //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                            parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
                                    }else {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
                                    }
                                }
                                return relVal;
                            }
                        },
                        legend: {
                            data: ['收费', '免费'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: 10,
                            right: 0,
                            top: 50,
                            height: 300,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '个数',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval
                        }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                data: datasf,
                                name: '收费',
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
                            },
                            {
                                data: databsf,
                                name: '免费',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#945FB9',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            },
                            {
                                name: '合计',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                        textStyle: {color: '#ff4848', fontSize: 12}
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: 'rgba(128, 128, 128, 0)'
                                    }
                                },
                                data: datahj
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                    });
                }
            }

        },
        {
            id: "businessTypeChargeSfMfList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var prerq = "";
                var dataxAxis = new Array();
                var datasf = new Array();
                var databsf = new Array();
                var datahj = new Array();
                var maxY = 0;
                var intval;
                var tmp_y = 0;
                var bsfnum = 0;
                var sfnum = 0;
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].rq.length > 8) {
                            dataxAxis.push(data[i].rq.substring(5, 10));
                        } else {
                            dataxAxis.push(data[i].rq);
                        }
                        if (data[i].sfnum == 0) {
                            datasf.push('');
                        } else if (data[i].sfnum != 0) {
                            datasf.push(data[i].sfnum);
                        }
                        if (data[i].bsfnum == 0) {
                            databsf.push('');
                        } else if (data[i].bsfnum != 0) {
                            databsf.push(data[i].bsfnum);
                        }
                        datahj.push(parseInt(data[i].bsfnum) + parseInt(data[i].sfnum))
                    }
                    for (var i = 0; i < datasf.length; i++) {
                        if (datasf[i] != "") {
                            if (datasf[i] > sfnum) {
                                sfnum = datasf[i];
                            }
                        }
                        if (databsf[i] != "") {
                            if (databsf[i] > bsfnum) {
                                bsfnum = databsf[i];
                            }
                        }
                    }
                    maxY = parseInt(sfnum) + parseInt(bsfnum);
                    maxY = parseInt(maxY) + 4 - parseInt(maxY % 4)
                    intval = maxY / 4
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.jcxmnum,
                        //     x : 'left'
                        // },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l-1; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    if (i<l-1){
                                        //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                            parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
                                    }else {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
                                    }
                                }
                                return relVal;
                            }
                        },
                        legend: {
                            data: ['收费', '免费'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: 10,
                            right: 0,
                            top: 50,
                            height: 300,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '个数',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval
                        }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                data: datasf,
                                name: '收费',
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
                                    },
                                },
                            },
                            {
                                data: databsf,
                                name: '免费',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#945FB9',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    },
                                },
                            },
                            {
                                name: '合计',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                        textStyle: {color: '#ff4848', fontSize: 12}
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: 'rgba(128, 128, 128, 0)'
                                    }
                                },
                                data: datahj
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                    });
                }
            }

        },
        {
            id: "zxzdyyList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                if (data && this.chart) {
                    var AplusDatas = [];
                    var ADatas = [];
                    var BDatas = [];
                    var CDatas = [];
                    var OthersDatas = [];
                    if (data.length>0){
                        var rqs = [];
                        for (let i = 0; i < data.length; i++) {
                            var rq = data[i].rq;
                            //若rq不存在于rqs，则添加
                            if (rqs.indexOf(rq) == -1) {
                                rqs.push(rq)
                            }
                        }
                        for (let i = 0; i < rqs.length; i++) {
                            var rq = rqs[i];
                            for (let j = 0; j < data.length; j++) {
                                if (rq == data[j].rq) {
                                    if (data[j].yyzddj == 'A+') {
                                        AplusDatas.push(data[j].cnum);
                                    } else if (data[j].yyzddj == 'A') {
                                        ADatas.push(data[j].cnum);
                                    } else if (data[j].yyzddj == 'B') {
                                        BDatas.push(data[j].cnum);
                                    } else if (data[j].yyzddj == 'C') {
                                        CDatas.push(data[j].cnum);
                                    } else {
                                        OthersDatas.push(data[j].cnum);
                                    }
                                }
                            }
                        }
                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l; i++) {
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                    relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                        parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
                                }
                                relVal += '<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green;"></span>合计: ' + value;
                                return relVal;
                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: rqs
                        },
                        series: [
                            {
                                name: 'A+',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A+:\n{c}({d})%",
                                data: AplusDatas
                            },
                            {
                                name: 'A',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: ADatas
                            },
                            {
                                name: 'B',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"B:\n{c}({d})%",
                                data: BDatas
                            },
                            {
                                name: 'C',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"C:\n{c}({d})%",
                                data: CDatas
                            },
                            {
                                name: '其他',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"其他:\n{c}({d})%",
                                data: OthersDatas
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id: "ksSfcssList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                if (data && this.chart) {
                    var datalist = new Array();
                    for (var i = 0; i < (data.length<10?data.length:10); i++) {
                        datalist.push({value: data[i].cnum, name: data[i].ksmc});
                    }
                    var pieoption = {
                        title: {
                            subtext: "",
                            x: 'left',
                            y: 10
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{b} : \n{c} ({d}%)"
                        },
                        calculable: false,//是否可拖拽
                        series: [
                            {
                                type: 'pie',
                                radius: ['0%', '50%'],
                                center: ['50%', '55%'],
                                startAngle: 45,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            formatter: "{b} : {c}\n({d}%)"
                                        },
                                        labelLine: {show: true}
                                    }
                                },
                                data: datalist
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }
        },
        {
            id: "yblxSfcssList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                if (data && this.chart) {
                    var datalist = new Array();
                    for (var i = 0; i < (data.length<10?data.length:10); i++) {
                        datalist.push({value: data[i].cnum, name: data[i].yblxmc});
                    }
                    var pieoption = {
                        title: {
                            subtext: "",
                            x: 'left',
                            y: 10
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{b} : \n{c} ({d}%)"
                        },
                        calculable: false,//是否可拖拽
                        series: [
                            {
                                type: 'pie',
                                radius: ['0%', '50%'],
                                center: ['50%', '55%'],
                                startAngle: 45,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            formatter: "{b} : {c}\n({d}%)"
                                        },
                                        labelLine: {show: true}
                                    }
                                },
                                data: datalist
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }
        },
        {
            id: "kpiList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var sjzShow = false;
                var sjzcd = 0;//时间轴长度
                var dqsjcd = 0;//当前时间长度
                var zqlx = map.zqlx;
                if (zqlx == 'quarter'){
                    var yf = map.yf;
                    var yfs = yf.split(",");
                    if (yfs.length == 3){
                        sjzShow = true;
                        var bigMonth = yfs[0];
                        var smallMonth = yfs[0];
                        var fullYear = new Date().getFullYear();
                        var fullMonth = new Date().getMonth()+ 1;
                        var nf = map.nf;
                        for (let lsyf of yfs) {
                            dateNum = new Date(nf,lsyf, 0).getDate();
                            sjzcd += dateNum;
                            if (bigMonth<lsyf){
                                bigMonth = lsyf;
                            }
                            if (smallMonth>lsyf){
                                smallMonth = lsyf;
                            }
                        }
                        if (fullYear > nf){
                            dqsjcd = sjzcd;//若今年>所选年份，当前时间长度为时间轴长度
                        }else if (fullYear < nf){
                            dqsjcd = 0;//若今年<所选年份，当前时间长度为0
                        }else {
                            //若今年=所选年份
                            if (fullMonth > bigMonth){
                                dqsjcd = sjzcd;//若今月>所选最大月份，前时间长度为时间轴长度
                            }else if (fullMonth<smallMonth){
                                dqsjcd = 0;//若今月<所选最小月份，前时间长度为0
                            }else {
                                for (let lsyf of yfs) {
                                    if (fullMonth > lsyf){
                                        var yfDateNum = new Date(nf,lsyf, 0).getDate();
                                        dqsjcd += yfDateNum;
                                    }else if (fullMonth == lsyf){
                                        dqsjcd += new Date().getDate();
                                    }
                                }
                            }
                        }
                    }
                }
                var hbflsData = [];
                var endData = [];
                if (data && this.chart) {
                    if (data.length>0){
                        for (let i = 0; i < data.length; i++) {
                            let hbfl = data[i].hbflmc;
                            //获取伙伴分类
                            if (hbflsData.indexOf(hbfl) == -1) {
                                hbflsData.push(hbfl);
                            }
                        }
                        var zbData = [];//指标
                        var sjzData = [];//时间轴
                        var cssData = [];//测试数
                        for (let i = 0; i < hbflsData.length; i++) {
                            var zbs = 0;
                            var csss = 0;
                            for (let j = 0; j < data.length; j++) {
                                if (hbflsData[i] == data[j].hbflmc) {
                                    zbs += parseInt(data[j].endsz);
                                    csss += parseInt(data[j].endnum);
                                }
                            }
                            zbData.push(zbs);
                            sjzData.push(zbs/sjzcd*dqsjcd);
                            cssData.push(csss);
                        }
                        let zzbs = 0;
                        let zcss = 0;
                        for (let i = 0; i < zbData.length; i++) {
                            zzbs += parseInt(zbData[i]);
                        }
                        for (let i = 0; i < cssData.length; i++) {
                            zcss += parseInt(cssData[i]);
                        }
                        hbflsData.push("总");
                        zbData.push(zzbs);
                        sjzData.push(zzbs/sjzcd*dqsjcd);
                        cssData.push(zcss);
                        var zbsObj = {};
                        var sjzObj = {};
                        var cssObj = {};
                        const sjOption = {
                            show: true,
                            formatter: '{a}: {c}',
                            rich: {
                                name: {}
                            },
                            position:'insideLeft'
                        };
                        var emphasis = {};
                        emphasis.focus = 'series';
                        zbsObj.name = "指标";
                        sjzObj.name = "时间轴";
                        cssObj.name = "测试数";
                        zbsObj.type = 'bar';
                        sjzObj.type = 'bar';
                        cssObj.type = 'bar';
                        zbsObj.barGap = 0;
                        sjzObj.barGap = 0;
                        cssObj.barGap = 0;
                        sjzObj.barWidth = 5;
                        zbsObj.label = sjOption;
                        cssObj.label = sjOption;
                        zbsObj.emphasis = emphasis;
                        sjzObj.emphasis = emphasis;
                        cssObj.emphasis = emphasis;
                        zbsObj.data = zbData;
                        sjzObj.data = sjzData;
                        cssObj.data = cssData;
                        endData.push(zbsObj);
                        if (sjzShow){
                            endData.push(sjzObj);
                        }
                        endData.push(cssObj);
                        console.log(endData);
                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let zb = 0;
                                let css = 0;
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if ("指标" == params[i].seriesName) {
                                        zb = params[i].value;
                                    }
                                    if ("测试数" == params[i].seriesName) {
                                        css = params[i].value;
                                    }
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                    if ("时间轴" != params[i].seriesName) {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value);
                                    }
                                }
                                relVal += '<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green;"></span>达成率: ' + (100 *
                                    parseFloat(css) / parseFloat(zb)).toFixed(2) + '%';
                                return relVal;
                            }
                        },
                        legend: {},
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value',
                            boundaryGap: [0, 0.01]
                        },
                        yAxis: {
                            type: 'category',
                            data: hbflsData
                        },
                        series: endData
                    };
                    this.chart.setOption(pieoption,true);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id:"yyxxphlist",
            chart: null,
            el: null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var category = new Array();
                    var barData = new Array();
                    var zxData=new Array();
                    var dlsData=new Array();
                    for (var i = 0; i < data.length; i++) {
                        if (i <= 10) {
                            category.push(data[i].dwmc);
                            barData.push(data[i].zh);
                            if (data[i].zxsfnum==0)
                                zxData.push("");
                            else
                                zxData.push(data[i].zxsfnum);
                            if (data[i].dlssfnum==0)
                                dlsData.push("");
                            else
                                dlsData.push(data[i].dlssfnum);
                            var total = 0;
                            for (var j = 0; j < barData.length; j++) {
                                total += barData[j];
                            }
                        }
                    }
                    barData.reverse();
                    category.reverse();
                    zxData.reverse();
                    dlsData.reverse();
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {
                                var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
                                var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
                                var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
                                // 定义第二个数据前的圆点颜色
                                for (let i = 0, l = params.length; i < l; i++) {
                                    var sum=params[i].dataIndex;
                                    var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总收费测试数量:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
                                        + "</br>" + dotHtml4 + ' 直销收费测试数:' + zxData[sum] + "</br>" + dotHtml4 + ' 直销占比:' + (Number((zxData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 代理商收费测试数:' + dlsData[sum]+ "</br>"
                                        + dotHtml5 + ' 代理商占比:' + (Number((dlsData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
                                    return result;
                                }

                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: category,
                            axisLabel: {
                                formatter: function (value) {
                                    var maxlength = 7;
                                    if (value.length > maxlength) {
                                        return value.substring(0, maxlength) + '...';
                                    } else {
                                        return value;
                                    }
                                },
                            },
                        },
                        series: [
                            {
                                name: '直销',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                data: zxData
                            },
                            {
                                name: '代理商',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: dlsData
                            },
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                }
            }
        },
        {
            id:"ndyrjsfcssList",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].rq);
                        dataseries.push(data[i].roundn);
                    }
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.dbtj,
                        //     x : 'left',
                        //     y : 10
                        // },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                axisLabel: {
                                    rotate: -45,
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                },
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                }
                            }
                        ],
                        series : [
                            {
                                name:'年度月人均收费测试数',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
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
                                }
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click",function(param){
                    //     var map ={}
                    //     map["method"]="getSjdwBydb";
                    //     map["db"]=param.name;
                    //     map["jsrqstart"]=searchData.jsrqstart;
                    //     map["jsrqend"]=searchData.jsrqend;
                    //     clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
                    // });
                }
            }
        },
        {
            id:"tjndyrjsfcssList",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].rq);
                        dataseries.push(data[i].roundn);
                    }
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.dbtj,
                        //     x : 'left',
                        //     y : 10
                        // },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                axisLabel: {
                                    rotate: -45,
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                },
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                }
                            }
                        ],
                        series : [
                            {
                                name:'特检年度月人均收费测试数',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
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
                                }
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click",function(param){
                    //     var map ={}
                    //     map["method"]="getSjdwBydb";
                    //     map["db"]=param.name;
                    //     map["jsrqstart"]=searchData.jsrqstart;
                    //     map["jsrqend"]=searchData.jsrqend;
                    //     clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
                    // });
                }
            }
        },
        {
            id:"ryndyrjsfcssList",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].rq);
                        dataseries.push(data[i].roundn);
                    }
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.dbtj,
                        //     x : 'left',
                        //     y : 10
                        // },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                axisLabel: {
                                    rotate: -45,
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                },
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                }
                            }
                        ],
                        series : [
                            {
                                name:'入院年度月人均收费测试数',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
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
                                }
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click",function(param){
                    //     var map ={}
                    //     map["method"]="getSjdwBydb";
                    //     map["db"]=param.name;
                    //     map["jsrqstart"]=searchData.jsrqstart;
                    //     map["jsrqend"]=searchData.jsrqend;
                    //     clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
                    // });
                }
            }
        },
        {
            id:"ryyysfcssList",
            chart: null,
            el: null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var category = new Array();
                    var barData = new Array();
                    var zxData=new Array();
                    var dlsData=new Array();
                    for (var i = 0; i < data.length; i++) {
                        if (i <= 10) {
                            category.push(data[i].dwmc);
                            barData.push(data[i].cnum);
                            if (data[i].zxsfnum==0)
                                zxData.push("");
                            else
                                zxData.push(data[i].zxsfnum);
                            if (data[i].dlssfnum==0)
                                dlsData.push("");
                            else
                                dlsData.push(data[i].dlssfnum);
                            var total = 0;
                            for (var j = 0; j < barData.length; j++) {
                                total += barData[j];
                            }
                        }
                    }
                    barData.reverse();
                    category.reverse();
                    zxData.reverse();
                    dlsData.reverse();
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {
                                var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
                                var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
                                var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
                                // 定义第二个数据前的圆点颜色
                                for (let i = 0, l = params.length; i < l; i++) {
                                    var sum=params[i].dataIndex;
                                    var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总收费测试数量:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
                                        + "</br>" + dotHtml4 + ' 直销收费测试数:' + zxData[sum] + "</br>" + dotHtml4 + ' 直销占比:' + (Number((zxData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 代理商收费测试数:' + dlsData[sum]+ "</br>"
                                        + dotHtml5 + ' 代理商占比:' + (Number((dlsData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
                                    return result;
                                }

                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: category,
                            axisLabel: {
                                formatter: function (value) {
                                    var maxlength = 7;
                                    if (value.length > maxlength) {
                                        return value.substring(0, maxlength) + '...';
                                    } else {
                                        return value;
                                    }
                                },
                            },
                        },
                        series: [
                            {
                                name: '直销',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                data: zxData
                            },
                            {
                                name: '代理商',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: dlsData
                            },
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    this.chart.on("click", function () {
                    });
                }
            }
        },
        {
            id:"hxyyxxphlist",
            chart: null,
            el: null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var category = new Array();
                    var barData = new Array();
                    var zxData=new Array();
                    var dlsData=new Array();
                    for (var i = 0; i < data.length; i++) {
                        if (i <= 10) {
                            category.push(data[i].dwmc);
                            barData.push(data[i].zh);
                            if (data[i].zxsfnum==0)
                                zxData.push("");
                            else
                                zxData.push(data[i].zxsfnum);
                            if (data[i].dlssfnum==0)
                                dlsData.push("");
                            else
                                dlsData.push(data[i].dlssfnum);
                            var total = 0;
                            for (var j = 0; j < barData.length; j++) {
                                total += barData[j];
                            }
                        }
                    }
                    barData.reverse();
                    category.reverse();
                    zxData.reverse();
                    dlsData.reverse();
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {
                                var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
                                var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
                                var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
                                // 定义第二个数据前的圆点颜色
                                for (let i = 0, l = params.length; i < l; i++) {
                                    var sum=params[i].dataIndex;
                                    var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总收费测试数量:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
                                        + "</br>" + dotHtml4 + ' 直销收费测试数:' + zxData[sum] + "</br>" + dotHtml4 + ' 直销占比:' + (Number((zxData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 代理商收费测试数:' + dlsData[sum]+ "</br>"
                                        + dotHtml5 + ' 代理商占比:' + (Number((dlsData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
                                    return result;
                                }

                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: category,
                            axisLabel: {
                                formatter: function (value) {
                                    var maxlength = 7;
                                    if (value.length > maxlength) {
                                        return value.substring(0, maxlength) + '...';
                                    } else {
                                        return value;
                                    }
                                },
                            },
                        },
                        series: [
                            {
                                name: '直销',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                data: zxData
                            },
                            {
                                name: '代理商',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: dlsData
                            },
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    this.chart.on("click", function () {
                    });
                }
            }


        },
        {
            id:"totalpal",
            chart:null,
            el:null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var table = "<table id='tab1'>"
                    table += "<thead>";
                    table += "<tr>";
                    table += "<th  class='yblx_td' style='background-color: #00b2ee'>所属大区</th>";
                    table += "<th  class='yblx_td' style='background-color: #00b2ee'>合作伙伴</th>";
                    table += "<th class='other_td' style='background-color: #00b2ee'>接收日期</th>";
                    table += "</tr>";
                    table += "</thead>";
                    table += "<body>"
                    var j=0
                    for (var i = 0; i < data.length; i++) {
                        if (j < 10) {
                            var color = "#FFE1FF";
                            j++;
                            if (data[i].dqxx==null)
                                data[i].dqxx="";
                            let str = data[i].dqxx;
                            let x = -1;
                            let y=0;
                            if (str != null) {
                                x = str.indexOf("（");
                                y = str.indexOf("）")
                            }
                            if (x != -1)
                                str = data[i].dqxx.substring(x + 1, y)
                            table += "<td class='other_td' style='background-color:" + color + "'>" + str + "</td>";
                            table += "<td class='other_td' style='background-color:" + color + "'>" + data[i].db + "</td>";
                            table += "<td class='other_td' style='background-color:" + color + "'>" + data[i].jsrq + "</td>";
                            table += "</tr>";
                            if (j==10){
                                table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                table += "</tr>";
                            }

                        }
                    }
                    table += "</tr>";
                    table += "</body>"
                    table += "</table>"
                    $("#qualityStatis #totalpal").empty();
                    $("#qualityStatis #totalpal").append(table);
                }
            }
        },
        {
            id:"topzx",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    if (data.length>9){
                        for (var i = 9; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }else {
                        for (var i = data.length-1; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }
                    var pieoption = {
                        title : {
                            x : 'left',
                            y : 10
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                offset: 10,
                                nameTextStyle: {
                                    fontSize: 10,
                                    fontcolor:'#00b2ee',
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                },
                            }
                        ],
                        series : [
                            {
                                name:'数量',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'right', //在右方显示
                                            textStyle: { //数值样式
                                                fontSize: 12
                                            }
                                        }
                                    }
                                },
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
            id:"topdls",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    if (data.length>9){
                        for (var i = 9; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }else {
                        for (var i = data.length-1; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }
                    var pieoption = {
                        title : {
                            x : 'left',
                            y : 10
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                offset: 10,
                                nameTextStyle: {
                                    fontSize: 10,
                                    fontcolor:'#00b2ee',
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                },
                            }
                        ],
                        series : [
                            {
                                name:'数量',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'right', //在右方显示
                                            textStyle: { //数值样式
                                                fontSize: 12
                                            }
                                        }
                                    }
                                },
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
            id:"topdsf",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    if (data.length>9){
                        for (var i = 9; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }else {
                        for (var i = data.length-1; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }
                    var pieoption = {
                        title : {
                            x : 'left',
                            y : 10
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                offset: 10,
                                nameTextStyle: {
                                    fontSize: 10,
                                    fontcolor:'#00b2ee',
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                },
                            }
                        ],
                        series : [
                            {
                                name:'数量',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'right', //在右方显示
                                            textStyle: { //数值样式
                                                fontSize: 12
                                            }
                                        }
                                    }
                                },
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
            id:"thpartpal",
            chart:null,
            el:null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var table = "<table id='tab1'>"
                    table += "<thead>";
                    table += "<tr>";
                    table += "<th  class='other_td' style='width:30%;background-color: #00b2ee'>所属大区</th>";
                    table += "<th  class='other_td' style='width:40%;background-color: #00b2ee'>合作伙伴</th>";
                    table += "<th class='other_td' style='width:30%;background-color: #00b2ee'>接收日期</th>";
                    table += "</tr>";
                    table += "</thead>";
                    table += "<body>"
                    var j=0
                    for (var i = 0; i < data.length; i++) {
                        if (j < 10) {
                            var color = "#FFE1FF";
                            j++;
                            let str=data[i].dqxx;
                            let x=-1
                            var y=0;
                            if (str!=null) {
                                x = str.indexOf("（");
                                y=str.indexOf("）");
                            }
                            if (x!=-1)
                            {
                                str=data[i].dqxx.substring(x+1,y)
                            }
                            table += "<td class='other_td' style='width:30%;background-color:" + color + "'title='"+str+"'>" + str + "</td>";
                            table += "<td class='other_td' style='width:40%;background-color:" + color + "'title='"+data[i].db+"'>" + data[i].db + "</td>";
                            table += "<td class='other_td' style='width:30%;background-color:" + color + "'title='"+data[i].jsrq+"'>" + data[i].jsrq + "</td>";
                            table += "</tr>";
                            if (j==10){
                                table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                table += "</tr>";
                            }

                        }
                    }
                    table+="</body>"
                    table+="</table>"
                    $("#qualityStatis #echarts_quality_thpartpal").empty();
                    $("#qualityStatis #echarts_quality_thpartpal").append(table);
                }
            }
        },
    ];

    for (var i = 0; i < flModels.length; i++) {
        _renderArr.push(flModels[i]);
    }
    // 路径配置
    setTimeout(function () {
        for (var i = 0; i < _renderArr.length; i++) {
            var _render = _renderArr[i];
            var _el = _render.el = document.getElementById(_idPrefix + _render.id);
            if (_el) {
                _render.chart = echarts.init(_el, _statis_theme);
                _render.eventTag = "." + _eventTag + "-" + _render.id;
                if (_isShowLoading) {
                    _render.chart.showLoading({
                        effect: _loadEffect
                    });
                }
                $(window).off(_render.eventTag).on("resize" + _render.eventTag, (function (_cfg) {
                    return function () {// resize事件
                        if (!_cfg.chart || $(document).find(_cfg.chart.dom).length == 0) {//元素不存在，则不操作
                            return;
                        }
                        _cfg.chart.resize();
                    }
                })(_render));
            }
        }
        //加载数据
        var _url = "/ws/statistics/getMarketStstictisData";
        $.ajax({
            type: "post",
            url: _url,
            data: map,
            dataType: "json",
            success: function (datas) {
                if (datas) {
                    pagedatas = datas;
                    qdmf=datas["channelChargeSfMfList"];
                    ywlxmf = datas["businessTypeChargeSfMfList"];
                    var _searchData = datas['searchData'];
                    for (var i = 0; i < _renderArr.length; i++) {
                        var _render = _renderArr[i];
                        var _data = datas[_render.id];
                        _render.render(_data, _searchData);
                        if (datas[_render.id] && datas[_render.id] != "searchData") {
                            if (datas[_render.id].length == 0) {
                                $("#echarts_quality_" + _render.id).parent().hide();
                            }
                            else {
                                $("#echarts_quality_" + _render.id).parent().show();
                            }
                        } else {
                            $("#echarts_quality_" + _render.id).parent().hide();
                        }
                    }
                } else {
                    throw "loadClientStatis数据获取异常";
                }
            }
        });
    });

    echartsBtnInitOne(_renderArr);
    loadLatelyWeekLeadStatis();
}
//加载统计数据
var loadLatelyWeekLeadStatis = function () {
    var _eventTag = "qualityStatis";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "echarts_quality_"; //id前缀
    var _statis_theme = "macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [
        {
            id:"losepartnerlist",
            chart:null,
            el:null,
            render:function(data,searchData) {
                if (data != null && data.length > 0) {
                    var table = "<table id='tab1'>"
                    table += "<thead>";
                    table += "<tr>";
                    table += "<th  class='yblx_td' style='width:30%;background-color: yellow'>所属大区</th>";
                    table += "<th  class='yblx_td' style='width:40%;background-color: yellow'>合作伙伴</th>";
                    table += "<th class='other_td' style='width:30%;background-color: yellow'>最后送检时间</th>";
                    table += "</tr>";
                    table += "</thead>";
                    table += "<body>"
                    var j=0
                    for (var i = 0; i < data.length; i++) {
                        if (j < 10) {
                            var color = "#FFE1FF";
                            if ("1" == (data[i].yellow)) {
                                if (data[i].dqxx==null)
                                    data[i].dqxx="";
                                j++;
                                let str = data[i].dqxx;
                                let x = -1;
                                let y=0;
                                if (str != null) {
                                    x = str.indexOf("（");
                                    y = str.indexOf("）")
                                }
                                if (x != -1)
                                    str = data[i].dqxx.substring(x + 1, y)
                                table += "<td class='other_td' style='width:30%;background-color:" + color + "'title='"+str+"'>" + str + "</td>";
                                table += "<td class='other_td' style='width:40%;background-color:" + color + "'title='"+data[i].db+"'>" + data[i].db + "</td>";
                                table += "<td class='other_td' style='width:30%;background-color:" + color + "'title='"+data[i].jsrq+"'>" + data[i].jsrq + "</td>";
                                table += "</tr>";
                                if (j==10){
                                    table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table += "</tr>";
                                }
                            }
                        }
                    }
                    table += "</body>"
                    table += "</table>"


                    var table2 = "<table id='tab1'>"
                    table2 += "<thead>";
                    table2 += "<tr>";
                    table2 += "<th  class='yblx_td' style='width:30%;background-color: red'>所属大区</th>";
                    table2 += "<th  class='yblx_td' style='width:40%;background-color: red'>合作伙伴</th>";
                    table2 += "<th class='other_td' style='width:30%;background-color: red'>最后送检时间</th>";
                    table2 += "</tr>";
                    table2 += "</thead>";
                    table2 += "<body>"
                    var j=0
                    for (var i = 0; i < data.length; i++) {
                        var color = "#FFE1FF";
                        if ("1" == (data[i].red)) {
                            if (j < 10) {
                                if (data[i].dqxx==null)
                                    data[i].dqxx="";
                                j++;
                                let str = data[i].dqxx;
                                let x = -1;
                                let y=0;
                                if (str != null) {
                                    x = str.indexOf("（");
                                    y = str.indexOf("）")
                                }
                                if (x != -1)
                                    str = data[i].dqxx.substring(x + 1, y)
                                table2 += "<td class='other_td' style='width:30%;background-color:" + color + "'title='"+str+"'>" +str + "</td>";
                                table2 += "<td class='other_td' style='width:40%;background-color:" + color + "'title='"+data[i].db+"'>" + data[i].db + "</td>";
                                table2 += "<td class='other_td' style='width:30%;background-color:" + color + "'title='"+data[i].jsrq+"'>" + data[i].jsrq + "</td>";
                                table2 += "</tr>";
                                if (j==10){
                                    table2 += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table2 += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table2 += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table2 += "</tr>";
                                }
                            }
                        }
                    }
                    table2 += "</body>"
                    table2 += "</table>"

                    var table3 ="<table id='tab1'>"
                    table3+="<thead>";
                    table3+="<tr>";
                    table3+="<th  class='yblx_td1' style='width:30%;background-color: black'>所属大区</th>";
                    table3+="<th  class='yblx_td2' style='width:40%;background-color: black'>合作伙伴</th>";
                    table3+="<th class='other_td2' style='width:30%;background-color: black'>最后送检时间</th>";;
                    table3+="</tr>";
                    table3+="</thead>";
                    table3+="<body>"
                    var j=0
                    for (var i = 0; i < data.length; i++) {
                        var color = "#FFE1FF";
                        if ("1" == (data[i].black)) {
                            if (j < 10) {
                                if (data[i].dqxx==null)
                                    data[i].dqxx="";
                                j++;
                                let str = data[i].dqxx;
                                let x = -1;
                                let y=0;
                                if (str != null) {
                                    x = str.indexOf("（");
                                    y = str.indexOf("）")
                                }
                                if (x != -1)
                                    str = data[i].dqxx.substring(x + 1, y)

                                table3 += "<td class='other_td' style='width:30%;background-color:" + color + "'title='" + str + "'>" + str + "</td>";
                                table3 += "<td class='other_td' style='width:40%;background-color:" + color + "'title='" + data[i].db + "'>" + data[i].db + "</td>";
                                table3 += "<td class='other_td' style='width:30%;background-color:" + color + "'title='" + data[i].jsrq + "'>" + data[i].jsrq + "</td>";
                                table3 += "</tr>";
                                if (j==10){
                                    table3 += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table3 += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table3 += "<td class='other_td' style='background-color:" + color + "'>" + "..." + "</td>";
                                    table3 += "</tr>";
                                }
                            }

                        }
                        }
                    table3+="</body>"
                    table3+="</table>"
                    }
                    $("#qualityStatis #echarts_quality_losepartnerlisty").empty();
                    $("#qualityStatis #echarts_quality_losepartnerlisty").append(table);
                    $("#qualityStatis #echarts_quality_losepartnerlistr").empty();
                    $("#qualityStatis #echarts_quality_losepartnerlistr").append(table2);
                    $("#qualityStatis #echarts_quality_losepartnerlistb").empty();
                    $("#qualityStatis #echarts_quality_losepartnerlistb").append(table3);
                }


        },
        {
            id: "qysfcssList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                if (data && this.chart) {
                    var datalist = new Array();
                    for (var i = 0; i < (data.length<10?data.length:10); i++) {
                        var dqjc = data[i].dq;
                        if (data[i].dq.indexOf("（")>=0 && data[i].dq.indexOf("）")>=0){
                            dqjc = data[i].dq.substring(data[i].dq.indexOf("（")+1,data[i].dq.indexOf("）"))
                        }
                        datalist.push({value: data[i].cn, name: dqjc});
                    }
                    var pieoption = {
                        title: {
                            subtext: "",
                            x: 'left',
                            y: 10
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{b} : \n{c} ({d}%)"
                        },
                        calculable: false,//是否可拖拽
                        series: [
                            {
                                type: 'pie',
                                radius: ['0%', '50%'],
                                center: ['50%', '55%'],
                                startAngle: 45,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            formatter: "{b} : {c}\n({d}%)"
                                        },
                                        labelLine: {show: true}
                                    }
                                },
                                data: datalist
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click", function () {
                    //     //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    // });
                }
            }
        },
    ];

    for (var i = 0; i < flModels.length; i++) {
        _renderArr.push(flModels[i]);
    }
    // 路径配置
    setTimeout(function () {
        for (var i = 0; i < _renderArr.length; i++) {
            var _render = _renderArr[i];
            var _el = _render.el = document.getElementById(_idPrefix + _render.id);
            if (_el) {
                _render.chart = echarts.init(_el, _statis_theme);
                _render.eventTag = "." + _eventTag + "-" + _render.id;
                if (_isShowLoading) {
                    _render.chart.showLoading({
                        effect: _loadEffect
                    });
                }
                $(window).off(_render.eventTag).on("resize" + _render.eventTag, (function (_cfg) {
                    return function () {// resize事件
                        if (!_cfg.chart || $(document).find(_cfg.chart.dom).length == 0) {//元素不存在，则不操作
                            return;
                        }
                        _cfg.chart.resize();
                    }
                })(_render));
            }
        }
        //加载数据
        var _url = "/ws/statistics/getMarketStstictisDataLately";
        $.ajax({
            type: "post",
            url: _url,
            data: map,
            dataType: "json",
            success: function (datas) {
                if (datas) {
                    laterpagedatas = datas;
                    var _searchData = datas['searchData'];
                    for (var i = 0; i < _renderArr.length; i++) {
                        var _render = _renderArr[i];
                        var _data = datas[_render.id];
                        _render.render(_data, _searchData);
                        if (datas[_render.id] && datas[_render.id] != "searchData") {
                            if (datas[_render.id].length == 0) {
                                $("#echarts_quality_" + _render.id).parent().hide();
                            }else {
                                $("#echarts_quality_" + _render.id).parent().show();
                            }
                        } else {
                            $("#echarts_quality_" + _render.id).parent().hide();
                        }
                    }
                } else {
                    throw "loadClientStatis数据获取异常";
                }
            }
        });
    });

    echartsBtnInit(_renderArr);
}
//加载金额统计页面
var loadMoneyWeekLeadStatis = function () {
    var _eventTag = "qualityStatisMoney";//事件标签
    var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
    var _isShowLoading = false; //是否显示加载动画
    var _idPrefix = "echarts_qualityAmount_"; //id前缀
    var _statis_theme = "macarons";//显示主题 macarons,infographic,shine,world
    var _renderArr = [
        {
            id: "channelChargeList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var prerq = "";
                var dataxAxis = new Array();
                var datadl = new Array();
                var datazx = new Array();
                var datadsf = new Array();
                var datahj = new Array();
                var maxY = 0;
                var intval;
                var tmp_y = 0;
                var dlnum = 0;
                var zxnum = 0;
                var dsfnum = 0;
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        var zx = data[i].直销;
                        var dls = data[i].代理商;
                        var dsf = data[i].第三方;
                        if (data[i].rq.length > 8) {
                            dataxAxis.push(data[i].rq.substring(5, 10));
                        } else {
                            dataxAxis.push(data[i].rq);
                        }
                        if (zx==undefined){
                            zx = 0;
                        }
                        if (dls ==undefined){
                            dls = 0;
                        }
                        if (dsf ==undefined){
                            dsf = 0;
                        }
                        if (zx == 0) {
                            datazx.push('');
                        } else if (zx != 0) {
                            datazx.push(zx);
                        }
                        if (dls == 0) {
                            datadl.push('');
                        } else if (dls != 0) {
                            datadl.push(dls);
                        }
                        if (dsf == 0) {
                            datadsf.push('');
                        } else if (dsf != 0) {
                            datadsf.push(dsf);
                        }
                        datahj.push(parseInt(zx) + parseInt(dls) + parseInt(dsf))
                    }
                    for (var i = 0; i < datazx.length; i++) {
                        if (datazx[i] != "") {
                            if (datazx[i] > zxnum) {
                                zxnum = datazx[i];
                            }
                        }
                        if (datadl[i] != "") {
                            if (datadl[i] > dlnum) {
                                dlnum = datadl[i];
                            }
                        }
                        if (datadsf[i] != "") {
                            if (datadsf[i] > dsfnum) {
                                dsfnum = datadsf[i];
                            }
                        }
                    }
                    maxY = parseInt(zxnum) + parseInt(dlnum) + parseInt(dsfnum);
                    maxY = parseInt(maxY) + 4 - parseInt(maxY % 4)
                    intval = maxY / 4
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.jcxmnum,
                        //     x : 'left'
                        // },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {			// 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l-1; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    if (i<l-1){
                                        //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                            (params[i].value==0?0:(parseFloat(params[i].value) / parseFloat(value)))).toFixed(2) + "%)";
                                    }else {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
                                    }
                                }
                                return relVal;
                            }
                        },
                        legend: {
                            data: ['直销', '代理', '第三方'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: 10,
                            right: 0,
                            top: 50,
                            height: 300,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '金额',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval
                        }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                data: datazx.map(function (item,index) {
                                    return{
                                        value:item,
                                        total:datahj[index],
                                    }

                                }),
                                name: '直销',
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
                            },
                            {
                                data: datadl,
                                name: '代理',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#945FB9',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            },
                            {
                                data: datadsf,
                                name: '第三方',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#FF9845',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    }
                                },
                            },
                            {
                                name: '合计',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                        textStyle: {color: '#ff4848', fontSize: 12}
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: 'rgba(128, 128, 128, 0)'
                                    }
                                },
                                data: datahj
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id: "businessTypeChargeList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                var prerq = "";
                var dataxAxis = new Array();
                var dataky = new Array();
                var datary = new Array();
                var datatj = new Array();
                var datahj = new Array();
                var maxY = 0;
                var intval;
                var tmp_y = 0;
                var kynum = 0;
                var rynum = 0;
                var tjnum = 0;
                if (data && this.chart) {
                    for (var i = 0; i < data.length; i++) {
                        var ky = data[i].科研;
                        var ry = data[i].入院;
                        var tj = data[i].特检;
                        if (data[i].rq.length > 8) {
                            dataxAxis.push(data[i].rq.substring(5, 10));
                        } else {
                            dataxAxis.push(data[i].rq);
                        }
                        if (ky==undefined){
                            ky = 0;
                        }
                        if (ry ==undefined){
                            ry = 0;
                        }
                        if (tj ==undefined){
                            tj = 0;
                        }
                        if (ky == 0) {
                            dataky.push('');
                        } else if (ky != 0) {
                            dataky.push(ky);
                        }
                        if (ry == 0) {
                            datary.push('');
                        } else if (ry != 0) {
                            datary.push(ry);
                        }
                        if (tj == 0) {
                            datatj.push('');
                        } else if (tj != 0) {
                            datatj.push(tj);
                        }
                        datahj.push(parseInt(ky) + parseInt(ry) + parseInt(tj))
                    }
                    for (var i = 0; i < dataky.length; i++) {
                        if (dataky[i] != "") {
                            if (dataky[i] > kynum) {
                                kynum = dataky[i];
                            }
                        }
                        if (datary[i] != "") {
                            if (datary[i] > rynum) {
                                rynum = datary[i];
                            }
                        }
                        if (datatj[i] != "") {
                            if (datatj[i] > tjnum) {
                                tjnum = datatj[i];
                            }
                        }
                    }
                    maxY = parseInt(kynum) + parseInt(rynum) + parseInt(tjnum);
                    maxY = parseInt(maxY) + 4 - parseInt(maxY % 4)
                    intval = maxY / 4
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.jcxmnum,
                        //     x : 'left'
                        // },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l-1; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    if (params[i].value==""){
                                        params[i].value = 0;
                                    }
                                    if (i<l-1){
                                        //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                            (params[i].value==0?0:(parseFloat(params[i].value) / parseFloat(value)))).toFixed(2) + "%)";
                                    }else {
                                        relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value)
                                    }
                                }
                                return relVal;
                            }
                        },
                        legend: {
                            data: ['科研', '入院', '特检'],
                            textStyle: {
                                color: '#3E9EE1'
                            },
                            y: 0
                        },
                        grid: {
                            left: 10,
                            right: 0,
                            top: 50,
                            height: 300,
                            containLabel: true
                        },
                        yAxis: [{
                            name: '金额',
                            type: 'value',
                            min: 0,
                            max: maxY,
                            interval: intval
                        }],
                        xAxis: {
                            type: 'category',
                            data: dataxAxis
                        },
                        series: [
                            {
                                data: dataky,
                                name: '科研',
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
                                    },
                                },
                            },
                            {
                                data: datary,
                                name: '入院',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#945FB9',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    },
                                },
                            },
                            {
                                data: datatj,
                                name: '特检',
                                type: 'bar',
                                stack: '总量',
                                itemStyle:{
                                    color: '#FF9845',
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                    },
                                },
                            },
                            {
                                name: '合计',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'insideBottom',
                                        textStyle: {color: '#ff4848', fontSize: 12}
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: 'rgba(128, 128, 128, 0)'
                                    }
                                },
                                data: datahj
                            }]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id: "zxzdyyList",
            chart: null,
            el: null,
            render: function (data, searchData) {
                if (data && this.chart) {
                    var AplusDatas = [];
                    var ADatas = [];
                    var BDatas = [];
                    var CDatas = [];
                    var OthersDatas = [];
                    if (data.length>0){
                        var rqs = [];
                        for (let i = 0; i < data.length; i++) {
                            var rq = data[i].rq;
                            //若rq不存在于rqs，则添加
                            if (rqs.indexOf(rq) == -1) {
                                rqs.push(rq)
                            }
                        }
                        for (let i = 0; i < rqs.length; i++) {
                            var rq = rqs[i];
                            for (let j = 0; j < data.length; j++) {
                                if (rq == data[j].rq) {
                                    if (data[j].yyzddj == 'A+') {
                                        AplusDatas.push(data[j].cnum);
                                    } else if (data[j].yyzddj == 'A') {
                                        ADatas.push(data[j].cnum);
                                    } else if (data[j].yyzddj == 'B') {
                                        BDatas.push(data[j].cnum);
                                    } else if (data[j].yyzddj == 'C') {
                                        CDatas.push(data[j].cnum);
                                    } else {
                                        OthersDatas.push(data[j].cnum);
                                    }
                                }
                            }
                        }
                    }
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {//弹窗内容
                                let relVal = params[0].name;
                                let value = 0;
                                for (let i = 0, l = params.length; i < l; i++) {
                                    value += params[i].value;
                                }
                                for (let i = 0, l = params.length; i < l; i++) {
                                    //marker 就是带颜色的小圆圈 seriesName x轴名称  value  y轴值 后面就是计算百分比
                                    relVal += '<br/>' + params[i].marker + params[i].seriesName + '  : ' + parseFloat(params[i].value) + '(' + (100 *
                                        parseFloat(params[i].value) / parseFloat(value)).toFixed(2) + "%)";
                                }
                                relVal += '<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green;"></span>合计: ' + value;
                                return relVal;
                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: rqs
                        },
                        series: [
                            {
                                name: 'A+',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A+:\n{c}({d})%",
                                data: AplusDatas
                            },
                            {
                                name: 'A',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: ADatas
                            },
                            {
                                name: 'B',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"B:\n{c}({d})%",
                                data: BDatas
                            },
                            {
                                name: 'C',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"C:\n{c}({d})%",
                                data: CDatas
                            },
                            {
                                name: '其他',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"其他:\n{c}({d})%",
                                data: OthersDatas
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    this.chart.on("click", function () {
                        //clickMenu('N040101','/researchproject/projectManage_list.html','项目列表',null,'');
                    });
                }
            }

        },
        {
            id:"yyxxphlist",
            chart: null,
            el: null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var category = new Array();
                    var barData = new Array();
                    var zxData=new Array();
                    var dlsData=new Array();
                    for (var i = 0; i < data.length; i++) {
                        if (i <= 10) {
                            category.push(data[i].dwmc);
                            barData.push(data[i].zh);
                            if (data[i].zxsfnum==0)
                                zxData.push("");
                            else
                                zxData.push(data[i].zxsfnum);
                            if (data[i].dlssfnum==0)
                                dlsData.push("");
                            else
                                dlsData.push(data[i].dlssfnum);
                            var total = 0;
                            for (var j = 0; j < barData.length; j++) {
                                total += barData[j];
                            }
                        }
                    }
                    barData.reverse();
                    category.reverse();
                    zxData.reverse();
                    dlsData.reverse();
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {
                                var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
                                var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
                                var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
                                // 定义第二个数据前的圆点颜色
                                for (let i = 0, l = params.length; i < l; i++) {
                                    var sum=params[i].dataIndex;
                                    var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总销售额:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
                                        + "</br>" + dotHtml4 + ' 直销销售额:' + zxData[sum] + "</br>" + dotHtml4 + ' 直销占比:' + (Number((zxData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 代理商销售额:' + dlsData[sum]+ "</br>"
                                        + dotHtml5 + ' 代理商占比:' + (Number((dlsData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
                                    return result;
                                }

                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: category,
                            axisLabel: {
                                formatter: function (value) {
                                    var maxlength = 7;
                                    if (value.length > maxlength) {
                                        return value.substring(0, maxlength) + '...';
                                    } else {
                                        return value;
                                    }
                                },
                            },
                        },
                        series: [
                            {
                                name: '直销',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                data: zxData
                            },
                            {
                                name: '代理商',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: dlsData
                            },
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                }
            }
        },
        {
            id:"ryyysfcssList",
            chart: null,
            el: null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var category = new Array();
                    var barData = new Array();
                    var zxData=new Array();
                    var dlsData=new Array();
                    for (var i = 0; i < data.length; i++) {
                        if (i <= 10) {
                            category.push(data[i].dwmc);
                            barData.push(data[i].cnum);
                            if (data[i].zxsfnum==0)
                                zxData.push("");
                            else
                                zxData.push(data[i].zxsfnum);
                            if (data[i].dlssfnum==0)
                                dlsData.push("");
                            else
                                dlsData.push(data[i].dlssfnum);
                            var total = 0;
                            for (var j = 0; j < barData.length; j++) {
                                total += barData[j];
                            }
                        }
                    }
                    barData.reverse();
                    category.reverse();
                    zxData.reverse();
                    dlsData.reverse();
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {
                                var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
                                var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
                                var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
                                // 定义第二个数据前的圆点颜色
                                for (let i = 0, l = params.length; i < l; i++) {
                                    var sum=params[i].dataIndex;
                                    var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总销售额:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
                                        + "</br>" + dotHtml4 + ' 直销销售额:' + zxData[sum] + "</br>" + dotHtml4 + ' 直销占比:' + (Number((zxData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 代理商销售额:' + dlsData[sum]+ "</br>"
                                        + dotHtml5 + ' 代理商占比:' + (Number((dlsData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
                                    return result;
                                }

                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: category,
                            axisLabel: {
                                formatter: function (value) {
                                    var maxlength = 7;
                                    if (value.length > maxlength) {
                                        return value.substring(0, maxlength) + '...';
                                    } else {
                                        return value;
                                    }
                                },
                            },
                        },
                        series: [
                            {
                                name: '直销',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                data: zxData
                            },
                            {
                                name: '代理商',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: dlsData
                            },
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    this.chart.on("click", function () {
                    });
                }
            }
        },
        {
            id:"hxyyxxphlist",
            chart: null,
            el: null,
            render:function(data,searchData) {
                if (data && this.chart) {
                    var category = new Array();
                    var barData = new Array();
                    var zxData=new Array();
                    var dlsData=new Array();
                    for (var i = 0; i < data.length; i++) {
                        if (i <= 10) {
                            category.push(data[i].dwmc);
                            barData.push(data[i].zh);
                            if (data[i].zxsfnum==0)
                                zxData.push("");
                            else
                                zxData.push(data[i].zxsfnum);
                            if (data[i].dlssfnum==0)
                                dlsData.push("");
                            else
                                dlsData.push(data[i].dlssfnum);
                            var total = 0;
                            for (var j = 0; j < barData.length; j++) {
                                total += barData[j];
                            }
                        }
                    }
                    barData.reverse();
                    category.reverse();
                    zxData.reverse();
                    dlsData.reverse();
                    var pieoption = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                // Use axis to trigger tooltip
                                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                            },
                            formatter: function (params) {
                                var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#F1E67F"></span>'
                                var dotHtml2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#2BA8F1"></span>'
                                var dotHtml3 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml4 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:green"></span>'    // 定义第二个数据前的圆点颜色
                                var dotHtml5 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:purple"></span>'    // 定义第二个数据前的圆点颜色
                                // 定义第二个数据前的圆点颜色
                                for (let i = 0, l = params.length; i < l; i++) {
                                    var sum=params[i].dataIndex;
                                    var result = dotHtml3 + ' 单位: ' + params[i].name + "</br>" + dotHtml + ' 总销售额:' +barData[sum]+ "</br>" + dotHtml2 + ' 总占比:' + (Number((barData[sum] / total) * 100).toFixed(2) + '%')
                                        + "</br>" + dotHtml4 + ' 直销销售额:' + zxData[sum] + "</br>" + dotHtml4 + ' 直销占比:' + (Number((zxData[sum] / (barData[sum])) * 100).toFixed(2) + '%')+ "</br>" + dotHtml5 + ' 代理商销售额:' + dlsData[sum]+ "</br>"
                                        + dotHtml5 + ' 代理商占比:' + (Number((dlsData[sum] / (barData[sum])) * 100).toFixed(2) + '%');
                                    return result;
                                }

                            }
                            // formatter:"{b} : \n{c} ({d}%)"
                        },
                        legend: {
                            // selected: {
                            //     '其他': false//默认“其他”不显示
                            // },
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value'
                        },
                        yAxis: {
                            type: 'category',
                            data: category,
                            axisLabel: {
                                formatter: function (value) {
                                    var maxlength = 7;
                                    if (value.length > maxlength) {
                                        return value.substring(0, maxlength) + '...';
                                    } else {
                                        return value;
                                    }
                                },
                            },
                        },
                        series: [
                            {
                                name: '直销',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                data: zxData
                            },
                            {
                                name: '代理商',
                                type: 'bar',
                                stack: 'total',
                                label: {
                                    show: true
                                },
                                emphasis: {
                                    focus: 'series'
                                },
                                // formatter:"A:\n{c}({d})%",
                                data: dlsData
                            },
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    this.chart.on("click", function () {
                    });
                }
            }


        },
        {
            id:"topzx",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    data.sort(function(a, b) {
                        return b.total - a.total;
                    });
                    if (data.length>9){
                        for (var i = 9; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }else {
                        for (var i = data.length-1; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }
                    var pieoption = {
                        title : {
                            x : 'left',
                            y : 10
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                offset: 10,
                                nameTextStyle: {
                                    fontSize: 10,
                                    fontcolor:'#00b2ee',
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                },
                            }
                        ],
                        series : [
                            {
                                name:'金额',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'right', //在右方显示
                                            textStyle: { //数值样式
                                                fontSize: 12
                                            }
                                        }
                                    }
                                },
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
            id:"topdls",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    if (data.length>9){
                        for (var i = 9; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }else {
                        for (var i = data.length-1; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }
                    var pieoption = {
                        title : {
                            x : 'left',
                            y : 10
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                offset: 10,
                                nameTextStyle: {
                                    fontSize: 10,
                                    fontcolor:'#00b2ee',
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                },
                            }
                        ],
                        series : [
                            {
                                name:'金额',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'right', //在右方显示
                                            textStyle: { //数值样式
                                                fontSize: 12
                                            }
                                        }
                                    }
                                },
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
            id:"topdsf",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    data.sort(function(a, b) {
                        return b.total - a.total;
                    });
                    if (data.length>9){
                        for (var i = 9; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }else {
                        for (var i = data.length-1; i >=0 ; i--) {
                            dataAxis.push(data[i].swmc);
                            dataseries.push(data[i].total);
                        }
                    }
                    var pieoption = {
                        title : {
                            x : 'left',
                            y : 10
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                offset: 10,
                                nameTextStyle: {
                                    fontSize: 10,
                                    fontcolor:'#00b2ee',
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#00b2ee'
                                    },
                                },
                            }
                        ],
                        series : [
                            {
                                name:'金额',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            position: 'right', //在右方显示
                                            textStyle: { //数值样式
                                                fontSize: 12
                                            }
                                        }
                                    }
                                },
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
            id:"ndyrjsfxseList",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].rq);
                        dataseries.push(data[i].roundn);
                    }
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.dbtj,
                        //     x : 'left',
                        //     y : 10
                        // },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                axisLabel: {
                                    rotate: -45,
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                },
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                }
                            }
                        ],
                        series : [
                            {
                                name:'年度月人均收费销售额',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
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
                                }
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click",function(param){
                    //     var map ={}
                    //     map["method"]="getSjdwBydb";
                    //     map["db"]=param.name;
                    //     map["jsrqstart"]=searchData.jsrqstart;
                    //     map["jsrqend"]=searchData.jsrqend;
                    //     clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
                    // });
                }
            }
        },
        {
            id:"tjndyrjsfxseList",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].rq);
                        dataseries.push(data[i].roundn);
                    }
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.dbtj,
                        //     x : 'left',
                        //     y : 10
                        // },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                axisLabel: {
                                    rotate: -45,
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                },
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                }
                            }
                        ],
                        series : [
                            {
                                name:'特检年度月人均收费销售额',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
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
                                }
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click",function(param){
                    //     var map ={}
                    //     map["method"]="getSjdwBydb";
                    //     map["db"]=param.name;
                    //     map["jsrqstart"]=searchData.jsrqstart;
                    //     map["jsrqend"]=searchData.jsrqend;
                    //     clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
                    // });
                }
            }
        },
        {
            id:"ryndyrjsfxseList",
            chart: null,
            el: null,
            render:function(data,searchData){
                var prekyfx = "";
                if(data&&this.chart){
                    var dataAxis= new Array();
                    var dataseries=new Array();
                    for (var i = 0; i < data.length; i++) {
                        dataAxis.push(data[i].rq);
                        dataseries.push(data[i].roundn);
                    }
                    var pieoption = {
                        // title : {
                        //     subtext : searchData.zqs.dbtj,
                        //     x : 'left',
                        //     y : 10
                        // },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {			// 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data:dataAxis,
                                axisLabel: {
                                    rotate: -45,
                                },
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                },
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine: {
                                    lineStyle: {
                                        color: '#3E9EE1'
                                    },
                                }
                            }
                        ],
                        series : [
                            {
                                name:'入院年度月人均收费销售额',
                                type:'bar',
                                barWidth: '70%',
                                data:dataseries,
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
                                }
                            }
                        ]
                    };
                    this.chart.setOption(pieoption);
                    this.chart.hideLoading();
                    //点击事件
                    // this.chart.on("click",function(param){
                    //     var map ={}
                    //     map["method"]="getSjdwBydb";
                    //     map["db"]=param.name;
                    //     map["jsrqstart"]=searchData.jsrqstart;
                    //     map["jsrqend"]=searchData.jsrqend;
                    //     clickLeadEcharts(_renderArr,'/ws/statistics/getWeekLeadStatisByrq',map,'dbtj');
                    // });
                }
            }
        },
    ];

    for (var i = 0; i < flModels.length; i++) {
        _renderArr.push(flModels[i]);
    }
    // 路径配置
    setTimeout(function () {
        for (var i = 0; i < _renderArr.length; i++) {
            var _render = _renderArr[i];
            var _el = _render.el = document.getElementById(_idPrefix + _render.id);
            if (_el) {
                _render.chart = echarts.init(_el, _statis_theme);
                _render.eventTag = "." + _eventTag + "-" + _render.id;
                if (_isShowLoading) {
                    _render.chart.showLoading({
                        effect: _loadEffect
                    });
                }
                $(window).off(_render.eventTag).on("resize" + _render.eventTag, (function (_cfg) {
                    return function () {// resize事件
                        if (!_cfg.chart || $(document).find(_cfg.chart.dom).length == 0) {//元素不存在，则不操作
                            return;
                        }
                        _cfg.chart.resize();
                    }
                })(_render));
            }
        }
        //加载数据
        var _url = "/ws/statistics/getMarketStstictisData";
        $.ajax({
            type: "post",
            url: _url,
            data: map,
            dataType: "json",
            success: function (datas) {
                if (datas) {
                    pagedatasMoney = datas;
                    var _searchData = datas['searchData'];
                    for (var i = 0; i < _renderArr.length; i++) {
                        var _render = _renderArr[i];
                        var _data = datas[_render.id];
                        _render.render(_data, _searchData);
                        if (datas[_render.id] && datas[_render.id] != "searchData") {
                            if (datas[_render.id].length == 0) {
                                $("#echarts_qualityAmount_" + _render.id).parent().hide();
                            }
                            else {
                                $("#echarts_qualityAmount_" + _render.id).parent().show();
                            }
                        } else {
                            $("#echarts_qualityAmount_" + _render.id).parent().hide();
                        }
                    }
                } else {
                    throw "loadClientStatis数据获取异常";
                }
            }
        });
    });
    echartsBtnInit(_renderArr);
}
//点击展示更多数据
function showMore(id,flag){
    var topdlsnew;
    var showData;
    if (id=="businessTypeChargeSfMfLis"){
        mfzb =ywlxmf;
    }else if (id=="channelChargeSfMfList"){
        mfzb=qdmf;
    }
    if (flag=="css"){
        showData = pagedatas[id];
        topdlsnew = pagedatas["topdlsnew"]
    }else if (flag=="money") {
        showData = pagedatasMoney[id];
        topdlsnew = pagedatasMoney["topdlsnew"]
    }else {
        showData = laterpagedatas[id];
    }
    //展示数据
    var html = "";
    if (flag=='css') {
        if ("ksSfcssList" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>科室</th>' +
                '         <th>测试数</th>' +
                '         <th>占比</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                html += '      <tr>' +
                    '         <td>' + showData[i].ksmc + '</td>' +
                    '         <td>' + showData[i].cnum + '</td>' +
                    '         <td>' + showData[i].percentage + '</td>' +
                    '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("yblxSfcssList" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>标本类型</th>' +
                '         <th>测试数</th>' +
                '         <th>占比</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                html += '      <tr>' +
                    '         <td>' + showData[i].yblxmc + '</td>' +
                    '         <td>' + showData[i].cnum + '</td>' +
                    '         <td>' + showData[i].percentage + '</td>' +
                    '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("totalpal" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>合作伙伴</th>' +
                '         <th>所属大区</th>' +
                '         <th>接收日期</th>' +
                '         <th>状态</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                let str = showData[i].dqxx;
                let x = -1
                let y = 0;
                if (str != null) {
                    x = str.indexOf("（");
                    y = str.indexOf("）")
                }
                if (x != -1) {
                    str = showData[i].dqxx.substring(x + 1, y)
                }
                html += '      <tr>' +
                    '         <td>' + showData[i].db + '</td>' +
                    '         <td>' + str + '</td>' +
                    '         <td>' + showData[i].jsrq + '</td>' +
                    '         <td>' + showData[i].zt + '</td>' +
                    '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("topzx" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>商务名称</th>' +
                '         <th>总数</th>' +
                '         <th>科研</th>' +
                '         <th>入院</th>' +
                '         <th>特检</th>' +
                '         <th>大区</th>' +
                '         <th>状态</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                let str = showData[i].dqxx;
                let x = -1
                let y = 0;
                if (str != null) {
                    x = str.indexOf("（");
                    y = str.indexOf("）")
                }
                if (x != -1) {
                    str = showData[i].dqxx.substring(x + 1, y)
                }
                html += '      <tr>' +
                    '         <td>' + showData[i].swmc + '</td>' +
                    '         <td>' + showData[i].total + '</td>' +
                    '         <td>' + showData[i].kj + '</td>' +
                    '         <td>' + showData[i].ry + '</td>' +
                    '         <td>' + showData[i].tj + '</td>'
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + str + '</td>'
                }
                html += '<td>' + showData[i].zt + '</td>'
                    + '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("topdsf" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>商务名称</th>' +
                '         <th>总数</th>' +
                '         <th>科研</th>' +
                '         <th>入院</th>' +
                '         <th>特检</th>' +
                '         <th>大区</th>' +
                '         <th>正常</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                html += '      <tr>' +
                    '         <td>' + showData[i].swmc + '</td>' +
                    '         <td>' + showData[i].total + '</td>' +
                    '         <td>' + showData[i].kj + '</td>' +
                    '         <td>' + showData[i].ry + '</td>' +
                    '         <td>' + showData[i].tj + '</td>'
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + showData[i].dqxx + '</td>'
                }
                html += '<td>' + showData[i].zt + '</td>'
                    + '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("topdls" == id) {
            var sjs = $("#qualityStatis #sjs").val();
            $("#qualityStatis #rqlx").val()
            var szsjs = sjs.split(",");
            szsjs.sort(function (a, b) {
                return a - b;
            });
            var count = 0;
            html += '<table class="table" >' +
                '   <thead>' +
                '      <tr>' +
                '         <th>商务名称</th>' +
                '         <th>总数</th>'
            for (let i = 0; i < szsjs.length; i++) {
                html += '<th>' + szsjs[i] + '</th>'
            }
            html += '         <th>大区</th>' +
                '         <th>状态</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                let str = showData[i].dqxx;
                let x = -1;
                let y = 0;
                var consl = 0;
                if (str != null) {
                    x = str.indexOf("（");
                    y = str.indexOf("）")
                }
                if (x != -1) {
                    str = showData[i].dqxx.substring(x + 1, y)
                }
                html += '      <tr>' +
                    '         <td>' + showData[i].swmc + '</td>' +
                    '         <td>' + showData[i].total + '</td>'
                for (let y = 0; y < szsjs.length; y++) {
                    var sf = true;
                    for (let z = 0; z < topdlsnew.length; z++) {
                        if (topdlsnew[z].swmc == showData[i].swmc && topdlsnew[z].zt == showData[i].zt && szsjs[y] == topdlsnew[z].jsrq) {
                            html += '<th style="background:white">' + topdlsnew[z].total + '</th>'
                            sf = false;
                        }
                    }
                    if (sf) {
                        html += '<th style="background:white"></th>'
                    }
                }
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + str + '</td>'
                }
                html += '<td>' + showData[i].zt + '</td>'
                    + '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("ryyysfcssList" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>入院医院</th>' +
                '         <th>医院重点等级</th>' +
                '         <th>总收费测试数</th>' +
                '         <th>直销收费测试数</th>' +
                '         <th>代理商收费测试数</th>' +
                '         <th>省份</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                var yyzddj = '';
                if (showData[i].yyzddj != undefined) {
                    yyzddj = showData[i].yyzddj
                }
                html += '      <tr>' +
                    '         <td>' + showData[i].dwmc + '</td>' +
                    '         <td>' + yyzddj + '</td>' +
                    '         <td>' + showData[i].cnum + '</td>' +
                    '         <td>' + showData[i].zxsfnum + '</td>' +
                    '         <td>' + showData[i].dlssfnum + '</td>'
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    let str = showData[i].dqxx;
                    let x = -1
                    let y = 0;
                    if (str != null)
                        x = str.indexOf("（");
                    y = str.indexOf("）")
                    if (x != -1) {
                        str = showData[i].dqxx.substring(x + 1, y)
                    }
                    html += '<td>' + str + '</td>'
                }
                html += '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        }
        if ("thpartpal" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>所属大区</th>' +
                '         <th>合作伙伴</th>' +
                '         <th>最后送检时间</th>' +
                '         <th>状态</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                let str = showData[i].dqxx;
                let x = -1
                let y = 0;
                if (str != null) {
                    x = str.indexOf("（");
                    y = str.indexOf("）")
                }
                if (x != -1) {
                    str = showData[i].dqxx.substring(x + 1, y)
                }
                html += '      <tr>' +
                    '         <td>' + showData[i].db + '</td>' +
                    '         <td>' + str + '</td>' +
                    '         <td>' + showData[i].jsrq + '</td>' +
                    '         <td>' + showData[i].zt + '</td>' +
                    '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
    }else if ("channelChargeList" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>直销收费测试数</th>' +
            '         <th>代理商收费测试数</th>' +
            '         <th>第三方收费测试数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < showData.length; i++) {
            var str1="";
            var str2="";
            var str3="";
            if (showData[i].直销!=null&&showData[i].直销!='undefined'){
                str1=showData[i].直销;
            }
            if (showData[i].代理商!=null&&showData[i].代理商!='undefined'){
                str2=showData[i].代理商;
            }
            if (showData[i].第三方!=null&&showData[i].第三方!='undefined'){
                str3=showData[i].第三方;
            }
            html += '      <tr>' +
                '         <td>'+showData[i].rq+'</td>' +
                '         <td>'+str1+'</td>' +
                '         <td>'+str2+'</td>' +
                '         <td>'+str3+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
    }else if ("businessTypeChargeList" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>科研收费测试数</th>' +
            '         <th>入院收费测试数</th>' +
            '         <th>特检收费测试数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < showData.length; i++) {
            var str1="";
            var str2="";
            var str3="";
            if (showData[i].科研!=null&&showData[i].科研!='undefined'){
                str1=showData[i].科研;
            }
            if (showData[i].入院!=null&&showData[i].入院!='undefined'){
                str2=showData[i].入院;
            }
            if (showData[i].特检!=null&&showData[i].特检!='undefined'){
                str3=showData[i].特检;
            }
            html += '      <tr>' +
                '         <td>'+showData[i].rq+'</td>' +
                '         <td>'+str1+'</td>' +
                '         <td>'+str2+'</td>' +
                '         <td>'+str3+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
    }else if ("ndyrjsfcssList" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>收费测试数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < showData.length; i++) {
            html += '      <tr>' +
                '         <td>'+showData[i].rq+'</td>' +
                '         <td>'+showData[i].roundn+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
    }else if ("tjndyrjsfcssList" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>收费测试数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < showData.length; i++) {
            html += '      <tr>' +
                '         <td>'+showData[i].rq+'</td>' +
                '         <td>'+showData[i].roundn+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
    }else if ("ryndyrjsfcssList" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>收费测试数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < showData.length; i++) {
            html += '      <tr>' +
                '         <td>'+showData[i].rq+'</td>' +
                '         <td>'+showData[i].roundn+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
    }else if ("channelChargeSfMfList" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>类别</th>' +
            '         <th>收费个数</th>' +
            '         <th>免费个数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < mfzb.length; i++) {
            html += '      <tr>' +
                '         <td>'+mfzb[i].rq+'</td>' +
                '         <td>'+mfzb[i].qdmc+'</td>' +
                '         <td>'+mfzb[i].sfnum+'</td>' +
                '         <td>'+mfzb[i].bsfnum+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
    }else if ("businessTypeChargeSfMfLis" == id){
        html += '<table class="table">' +
            '   <thead>' +
            '      <tr>' +
            '         <th>日期</th>' +
            '         <th>类别</th>' +
            '         <th>收费个数</th>' +
            '         <th>免费个数</th>' +
            '      </tr>' +
            '   </thead>' +
            '   <tbody>';
        for (let i = 0; i < mfzb.length; i++) {
            html += '      <tr>' +
                '         <td>'+mfzb[i].rq+'</td>' +
                '         <td>'+mfzb[i].qfmc+'</td>' +
                '         <td>'+mfzb[i].sfnum+'</td>' +
                '         <td>'+mfzb[i].bsfnum+'</td>' +
                '      </tr>';
        }
        html += '   </tbody>' +
            '</table>';
        }
    }
    else {
     if ("ryyysfcssList" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>入院医院</th>' +
                '         <th>医院重点等级</th>' +
                '         <th>总销售额</th>' +
                '         <th>直销销售额</th>' +
                '         <th>代理商销售额</th>' +
                '         <th>省份</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                var yyzddj = '';
                if (showData[i].yyzddj != undefined) {
                    yyzddj = showData[i].yyzddj
                }
                html += '      <tr>' +
                    '         <td>' + showData[i].dwmc + '</td>' +
                    '         <td>' + yyzddj + '</td>' +
                    '         <td>' + showData[i].cnum + '</td>' +
                    '         <td>' + showData[i].zxsfnum + '</td>' +
                    '         <td>' + showData[i].dlssfnum + '</td>'
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    let str = showData[i].dqxx;
                    let x = -1
                    let y = 0;
                    if (str != null)
                        x = str.indexOf("（");
                    y = str.indexOf("）")
                    if (x != -1) {
                        str = showData[i].dqxx.substring(x + 1, y)
                    }
                    html += '<td>' + str + '</td>'
                }
                html += '      </tr>';
            }
            html += '   </tbody>' +
                '</table>';
        }else if ("topzx" == id) {
         showData.sort(function(a, b) {
             return b.total - a.total;
         });
         html += '<table class="table">' +
             '   <thead>' +
             '      <tr>' +
             '         <th>商务名称</th>' +
             '         <th>总销售额</th>' +
             '         <th>科研销售额</th>' +
             '         <th>入院销售额</th>' +
             '         <th>特检销售额</th>' +
             '         <th>大区</th>' +
             '         <th>状态</th>' +
             '      </tr>' +
             '   </thead>' +
             '   <tbody>';
         for (let i = 0; i < showData.length; i++) {
             let str = showData[i].dqxx;
             let x = -1
             let y = 0;
             if (str != null) {
                 x = str.indexOf("（");
                 y = str.indexOf("）")
             }
             if (x != -1) {
                 str = showData[i].dqxx.substring(x + 1, y)
             }
             html += '      <tr>' +
                 '         <td>' + showData[i].swmc + '</td>' +
                 '         <td>' + showData[i].total + '</td>' +
                 '         <td>' + showData[i].kj + '</td>' +
                 '         <td>' + showData[i].ry + '</td>' +
                 '         <td>' + showData[i].tj + '</td>'
             if (showData[i].dqxx == null) {
                 html += '<td>' + '' + '</td>'
             } else {
                 html += '<td>' + str + '</td>'
             }
             html += '<td>' + showData[i].zt + '</td>'
                 + '      </tr>';
         }
         html += '   </tbody>' +
             '</table>';
     }else if ("topdsf" == id) {
         showData.sort(function(a, b) {
             return b.total - a.total;
         });
         html += '<table class="table">' +
             '   <thead>' +
             '      <tr>' +
             '         <th>商务名称</th>' +
             '         <th>总销售额</th>' +
             '         <th>科研销售额</th>' +
             '         <th>入院销售额</th>' +
             '         <th>特检销售额</th>' +
             '         <th>大区</th>' +
             '         <th>正常</th>' +
             '      </tr>' +
             '   </thead>' +
             '   <tbody>';
         for (let i = 0; i < showData.length; i++) {
             html += '      <tr>' +
                 '         <td>' + showData[i].swmc + '</td>' +
                 '         <td>' + showData[i].total + '</td>' +
                 '         <td>' + showData[i].kj + '</td>' +
                 '         <td>' + showData[i].ry + '</td>' +
                 '         <td>' + showData[i].tj + '</td>'
             if (showData[i].dqxx == null) {
                 html += '<td>' + '' + '</td>'
             } else {
                 html += '<td>' + showData[i].dqxx + '</td>'
             }
             html += '<td>' + showData[i].zt + '</td>'
                 + '      </tr>';
         }
         html += '   </tbody>' +
             '</table>';
     } else if ("topdls" == id) {
         var sjs = $("#qualityStatis #sjs").val();
         $("#qualityStatis #rqlx").val()
         var szsjs = sjs.split(",");
         szsjs.sort(function (a, b) {
             return a - b;
         });
         var count = 0;
         html += '<table class="table" >' +
             '   <thead>' +
             '      <tr>' +
             '         <th>商务名称</th>' +
             '         <th>总销售额</th>'
         for (let i = 0; i < szsjs.length; i++) {
             html += '<th>' + szsjs[i] + '</th>'
         }
         html += '         <th>大区</th>' +
             '         <th>状态</th>' +
             '      </tr>' +
             '   </thead>' +
             '   <tbody>';
         for (let i = 0; i < showData.length; i++) {
             let str = showData[i].dqxx;
             let x = -1;
             let y = 0;
             var consl = 0;
             if (str != null) {
                 x = str.indexOf("（");
                 y = str.indexOf("）")
             }
             if (x != -1) {
                 str = showData[i].dqxx.substring(x + 1, y)
             }
             html += '      <tr>' +
                 '         <td>' + showData[i].swmc + '</td>' +
                 '         <td>' + showData[i].total + '</td>'
             for (let y = 0; y < szsjs.length; y++) {
                 var sf = true;
                 for (let z = 0; z < topdlsnew.length; z++) {
                     if (topdlsnew[z].swmc == showData[i].swmc && topdlsnew[z].zt == showData[i].zt && szsjs[y] == topdlsnew[z].jsrq) {
                         html += '<th style="background:white">' + topdlsnew[z].total + '</th>'
                         sf = false;
                     }
                 }
                 if (sf) {
                     html += '<th style="background:white"></th>'
                 }
             }
             if (showData[i].dqxx == null) {
                 html += '<td>' + '' + '</td>'
             } else {
                 html += '<td>' + str + '</td>'
             }
             html += '<td>' + showData[i].zt + '</td>'
                 + '      </tr>';
         }
         html += '   </tbody>' +
             '</table>';
     }else if ("channelChargeList" == id){
         html += '<table class="table">' +
             '   <thead>' +
             '      <tr>' +
             '         <th>日期</th>' +
             '         <th>直销销售额</th>' +
             '         <th>代理商销售额</th>' +
             '         <th>第三方销售额</th>' +
             '      </tr>' +
             '   </thead>' +
             '   <tbody>';
         for (let i = 0; i < showData.length; i++) {
             var str1="";
             var str2="";
             var str3="";
             if (showData[i].直销!=null&&showData[i].直销!='undefined'){
                 str1=showData[i].直销;
             }
             if (showData[i].代理商!=null&&showData[i].代理商!='undefined'){
                 str2=showData[i].代理商;
             }
             if (showData[i].第三方!=null&&showData[i].第三方!='undefined'){
                 str3=showData[i].第三方;
             }
             html += '      <tr>' +
                 '         <td>'+showData[i].rq+'</td>' +
                 '         <td>'+str1+'</td>' +
                 '         <td>'+str2+'</td>' +
                 '         <td>'+str3+'</td>' +
                 '      </tr>';
         }
         html += '   </tbody>' +
             '</table>';
     }else if ("businessTypeChargeList" == id){
         html += '<table class="table">' +
             '   <thead>' +
             '      <tr>' +
             '         <th>日期</th>' +
             '         <th>科研收费销售额</th>' +
             '         <th>入院收费销售额</th>' +
             '         <th>特检收费销售额</th>' +
             '      </tr>' +
             '   </thead>' +
             '   <tbody>';
         for (let i = 0; i < showData.length; i++) {
             var str1="";
             var str2="";
             var str3="";
             if (showData[i].科研!=null&&showData[i].科研!='undefined'){
                 str1=showData[i].科研;
             }
             if (showData[i].入院!=null&&showData[i].入院!='undefined'){
                 str2=showData[i].入院;
             }
             if (showData[i].特检!=null&&showData[i].特检!='undefined'){
                 str3=showData[i].特检;
             }
             html += '      <tr>' +
                 '         <td>'+showData[i].rq+'</td>' +
                 '         <td>'+str1+'</td>' +
                 '         <td>'+str2+'</td>' +
                 '         <td>'+str3+'</td>' +
                 '      </tr>';
         }
         html += '   </tbody>' +
             '</table>';
     }
    }

    var dialog = $.dialog({
        title:"展示更多",
        message: html,
        closeButton: true,
        width:"1000px",
    });

}
var map={'nf':'','yf':'','qy':'','jcxm':'','zqlx':'','hbflid':'','flag':'','sjqf':'','sf':''};
//Tab切换监听
$('a[data-bs-toggle="tab"]').on('show.bs.tab', function (e) {
    if (e.target.toString().split("#")[1]=='qualityStatisMoney'){
        map.moneyflag='1';
        $("#qualityStatis #qualityStatisMoney").attr("class","tab-pane fade active in");
        if(!e.target.isLoaded){
            loadMoneyWeekLeadStatis();
        }
        e.target.isLoaded =true;
    }else {
        $("#qualityStatis #qualityStatisMoney").attr("class","tab-pane fade");
        map.moneyflag=null;
    }
})
var quality_turnOff=true;
function init(){
    $("#qualityStatis #newDiv").hide();
    /**显示隐藏**/
    $("#qualityStatis #sl_searchMore").on("click", function(ev){
        var ev=ev||event;
        if(quality_turnOff){
            $("#qualityStatis #searchMore").slideDown("low");
            quality_turnOff=false;
            this.innerHTML="基本筛选";
        }else{
            $("#qualityStatis #searchMore").slideUp("low");
            quality_turnOff=true;
            this.innerHTML="高级筛选";
        }
        ev.cancelBubble=true;
    });

    var date=new Date();
    var year=date.getFullYear();
    var month=date.getMonth()+1;
    var zqlx = $("#qualityStatis a[id^='zqlx_id_']");
    var yf = $("#qualityStatis a[id^='yf_id_']");
    var qy = $("#qualityStatis a[id^='qy_id_']");
    var dqnf = $("#qualityStatis a[id^='dqnf_id_']");
    $.each(zqlx, function(i, n){
        var id = $(this).attr("id");
        var code = id.substring(id.lastIndexOf('_')+1,id.length);
        if(code == 'yf'){
            addTj('zqlx',code,'qualityStatis');
            map.zqlx='month';
        }
    });
    $.each(dqnf, function(i, n){
        var id = $(this).attr("id");
        var code = id.substring(id.lastIndexOf('_')+1,id.length);
        if(code == year){
            addTj('dqnf',code,'qualityStatis');
            map.nf=year.toString();
        }
    });
    $.each(yf, function(i, n){
        var id = $(this).attr("id");
        var code = id.substring(id.lastIndexOf('_')+1,id.length);
        for(var i=0;i<3;i++){
            if(code == month-i){
                addTj('yf',code,'qualityStatis');
                map.yf=map.yf+","+code;
            }
        }
    });
    map.yf=map.yf.substring(1);
    $("#qualityStatis #sjs").val(map.yf)
    $.each(qy, function(i, n){
        var id = $(this).attr("id");
        var code = id.substring(id.lastIndexOf('_')+1,id.length);
        if(code == 'ALL'){
            addTj('qy',code,'qualityStatis');
            map.qy='ALL';
        }
    });
    map.flag = null;
    map.sjqf =  $("#qualityStatis #qflb").find("option:selected").val();
    map.hbflid =  $("#qualityStatis #qdlb").find("option:selected").val();
    var jcxms = JSON.parse($("#qualityStatis #detectList").val());
    var jcxm ='';
    for (var i = 0; i <jcxms.length; i++) {
        if (jcxms[i].cskz3!='IMP_REPORT_RFS_TEMEPLATE'){
            jcxm = jcxm + "," + jcxms[i].csid;
            addTj('jcxm',jcxms[i].csid,'qualityStatis');
        }
    }
    if (jcxm.length>0){
        jcxm=jcxm.substring(1);
    }
    map.jcxm = jcxm;
}


function search(flag){
    map.sjqf =  $("#qualityStatis #qflb").find("option:selected").val();
    map.hbflid =  $("#qualityStatis #qdlb").find("option:selected").val();
    map.flag = flag;
    //获取当前选中条件
    var qy=$("#qualityStatis #qy_id_tj").val().replaceAll("'","");
    var jcxm=$("#qualityStatis #jcxm_id_tj").val().replaceAll("'","");
    var zqlx=$("#qualityStatis #zqlx_id_tj").val().replaceAll("'","");
    var nf=$("#qualityStatis #nf_id_tj").val().replaceAll("'","");
    var dqnf=$("#qualityStatis #dqnf_id_tj").val().replaceAll("'","");
    var jd=$("#qualityStatis #jd_id_tj").val().replaceAll("'","");
    var yf=$("#qualityStatis #yf_id_tj").val().replaceAll("'","");
    var sf=$("#qualityStatis #sf_id_tj").val().replaceAll("'","");
    if (jcxm==null||jcxm==""){
        $.confirm("请选择检测项目!");
        return;
    }
    map.qy=qy;
    map.jcxm=jcxm;
    map.sf=sf;
    if(zqlx=='jd'){
        if(dqnf==null || dqnf==''){
            $.confirm("请选择当前年份!");
            return;
        }
        map.zqlx='quarter';
        if(jd!=null && jd!=''){
            var sz='';
            for(var i=0;i<jd.split(",").length;i++){
                if(jd.split(",")[i]=='Q1'){
                    sz=sz+','+'01,02,03';
                }
                if(jd.split(",")[i]=='Q2'){
                    sz=sz+','+'04,05,06';
                }
                if(jd.split(",")[i]=='Q3'){
                    sz=sz+','+'07,08,09';
                }
                if(jd.split(",")[i]=='Q4'){
                    sz=sz+','+'10,11,12';
                }
            }
            map.yf=sz.substring(1);
            $("#qualityStatis #sjs").val(map.yf)
        }
    }else if (zqlx=='yf'){
        if(dqnf==null || dqnf==''){
            $.confirm("请选择当前年份!");
            return;
        }
        map.zqlx='month';
        map.yf=yf;
        $("#qualityStatis #sjs").val(map.yf)
    }else if (zqlx=='nf'){
        map.zqlx='year';
        $("#qualityStatis #sjs").val(nf)
    }else{//未选择类型
        map.zqlx='month';
        map.yf='';
    }
    if(nf!=null && nf!=''){
        map.nf=nf;
    }else{
        map.nf=dqnf;
    }
    $("#qualityStatis #rqlx").val(map.zqlx)
    return true;
}
//统计图中各按钮的点击事件初始化
function echartsBtnInitOne(_renderArr){
    var url="/ws/statistics/getMarketStstictisData";
    //伙伴分类改变事件
    $("#qualityStatis select[name='qdlb']").unbind("change").change(function(e){
        var flag =  'qd';
        var result = search(flag);
        if (result) {
            clickLeadEcharts(_renderArr, url, map, 'channelChargeSfMfList');
        }
    })
    //送检区分改变事件
    $("#qualityStatis select[name='qflb']").unbind("change").change(function(e){
        var flag =  'qf';
        var result = search(flag);
        if (result) {
            clickLeadEcharts(_renderArr, url, map, 'businessTypeChargeSfMfList');
        }
    });
}
//点击查询事件
$("#qualityStatis #btn_query").unbind("click").click(function(e){
    var flag = null;
    var result = search(flag);
    if (result){
        if (map.moneyflag=='1'){
            loadMoneyWeekLeadStatis();
        }else {
            loadWeekLeadStatis();
        }
    }
});
//点击清空事件
$("#qualityStatis #btn_empty").unbind("click").click(function(){
    delTj('zqlx','nf','qualityStatis');
    delTj('zqlx','yf','qualityStatis');
    delTj('zqlx','jd','qualityStatis');
    delTj('zqlx','nf','qualityStatis');
    delTj('zqlx','nf','qualityStatis');
    var years = JSON.parse($("#qualityStatis #yearList").val());
    for (var i = 0; i <years.length; i++) {
        delTj('nf',years[i],'qualityStatis');
    }
    for (var i = 0; i <years.length; i++) {
        delTj('dqnf',years[i],'qualityStatis');
    }
    delTj('yf','01','qualityStatis');
    delTj('yf','02','qualityStatis');
    delTj('yf','03','qualityStatis');
    delTj('yf','04','qualityStatis');
    delTj('yf','05','qualityStatis');
    delTj('yf','06','qualityStatis');
    delTj('yf','07','qualityStatis');
    delTj('yf','08','qualityStatis');
    delTj('yf','09','qualityStatis');
    delTj('yf','10','qualityStatis');
    delTj('yf','11','qualityStatis');
    delTj('yf','12','qualityStatis');

    delTj('jd','Q1','qualityStatis');
    delTj('jd','Q2','qualityStatis');
    delTj('jd','Q3','qualityStatis');
    delTj('jd','Q4','qualityStatis');
    var jcxms = JSON.parse($("#qualityStatis #detectList").val());
    for (var i = 0; i <jcxms.length; i++) {
        delTj('jcxm',jcxms[i].csid,'qualityStatis');
    }
    var sfs = JSON.parse($("#qualityStatis #provinceList").val());
    for (var i = 0; i <sfs.length; i++) {
        delTj('sf',sfs[i].csid,'qualityStatis');
    }
    var yhs = JSON.parse($("#qualityStatis #userDtos").val());
    for (var i = 0; i <yhs.length; i++) {
        delTj('qy',yhs[i].yhid,'qualityStatis');
    }
    map.nf='';
    map.yf='';
    map.qy='';
    map.jcxm='';
    map.sf='';
    map.zqlx='';
    map.hbflid='';
    map.flag=null;
    map.sjqf='';
});
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
                        if (_render.id=="channelChargeSfMfList"){
                            qdmf=datas[_render.id];
                        }else if (_render.id=="businessTypeChargeSfMfList"){
                            ywlxmf=datas[_render.id];
                        }
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
function getLosePartnerBlack() {
    var id="losepartnerlist";
    var showData = laterpagedatas[id];
    var html = "";
    html+= '<table class="table">' +
        '   <thead>' +
        '      <tr>' +
        '         <th>所属大区</th>' +
        '         <th>合作伙伴</th>' +
        '         <th>最后送检时间</th>' +
        '         <th>状态</th>' +
        '      </tr>' +
        '   </thead>' +
        '   <tbody>';
    for (let i=0;i<showData.length; i++){
        if ("1"==(showData[i].black)){
            if (showData[i].dqxx==null)
                showData[i].dqxx="";
            let str=showData[i].dqxx;
            let x=-1
            let y=0;
            if (str!=null)
                x=str.indexOf("（");
                y=str.indexOf("）")
            if (x!=-1)
            {
                str=showData[i].dqxx.substring(x+1,y)
            }
            html += '      <tr>' +
                '         <td>'+str+'</td>' +
                '         <td>'+showData[i].db+'</td>' +
                '         <td>'+showData[i].jsrq+'</td>' +
                '         <td>'+showData[i].zt+'</td>' +
                '      </tr>';
        }
    }
    html += '   </tbody>' +
        '</table>'

    var dialog = $.dialog({
        title:"展示更多",
        message: html,
        closeButton: true,
        width:"1000px",
    });
}
function getLosePartnerYellow() {
    var id="losepartnerlist";
    var showData = laterpagedatas[id];
    var html = "";
    html+= '<table class="table">' +
        '   <thead>' +
        '      <tr>' +
        '         <th>所属大区</th>' +
        '         <th>合作伙伴</th>' +
        '         <th>最后送检时间</th>' +
        '         <th>状态</th>' +
        '      </tr>' +
        '   </thead>' +
        '   <tbody>';
    for (let i=0;i<showData.length; i++){
        if ("1" == (showData[i].yellow)) {
            if (showData[i].dqxx==null)
                showData[i].dqxx="";
            let str=showData[i].dqxx;
            let x=-1
            let y=0;
            if (str!=null)
                x=str.indexOf("（");
            y=str.indexOf("）")
            if (x!=-1)
            {
                str=showData[i].dqxx.substring(x+1,y)
            }
            html += '      <tr>' +
                '         <td>'+str+'</td>' +
                '         <td>'+showData[i].db+'</td>' +
                '         <td>'+showData[i].jsrq+'</td>' +
                '         <td>'+showData[i].zt+'</td>' +
                '      </tr>';
        }
    }
    html += '   </tbody>' +
        '</table>'

    var dialog = $.dialog({
        title:"展示更多",
        message: html,
        closeButton: true,
        width:"1000px",
    });
}
function getLosePartnerRed() {
    var id="losepartnerlist";
    var showData = laterpagedatas[id];
    var html = "";
    html+= '<table class="table">' +
        '   <thead>' +
        '      <tr>' +
        '         <th>所属大区</th>' +
        '         <th>合作伙伴</th>' +
        '         <th>最后送检时间</th>' +
        '         <th>状态</th>' +
        '      </tr>' +
        '   </thead>' +
        '   <tbody>';
    for (let i=0;i<showData.length; i++){
        if ("1"==(showData[i].red)){
            if (showData[i].dqxx==null)
                showData[i].dqxx="";
            let str=showData[i].dqxx;
            let x=-1
            let y=0;
            if (str!=null)
                x=str.indexOf("（");
            y=str.indexOf("）")
            if (x!=-1)
            {
                str=showData[i].dqxx.substring(x+1,y)
            }
            html += '      <tr>' +
                '         <td>'+str+'</td>' +
                '         <td>'+showData[i].db+'</td>' +
                '         <td>'+showData[i].jsrq+'</td>' +
                '         <td>'+showData[i].zt+'</td>' +
                '      </tr>';
        }
    }
    html += '   </tbody>' +
        '</table>'

    var dialog = $.dialog({
        title:"展示更多",
        message: html,
        closeButton: true,
        width:"1000px",
    });
}
function getAllYxxx(id,flag){
    //加载数据
    // var url="/ws/statistics/viewAllYyxx"
    // $.showDialog(url,'总医院排行详细信息',viewYyxxRankConfig);
    var showData;
    if (flag=='css'){
        showData = pagedatas[id];
    }else{
        showData = pagedatasMoney[id];
    }

    //展示数据
    var html = "";
    if (flag=='css') {
        if ("yyxxphlist" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>排名</th>' +
                '         <th>医院</th>' +
                '         <th>总收费测试数</th>' +
                '         <th>直销收费测试数</th>' +
                '         <th>代理商收费测试数</th>' +
                '         <th>医院重点等级</th>' +
                '         <th>省份</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                var j = i + 1;
                html += '      <tr>' +
                    '         <td>' + j + '</td>' +
                    '         <td>' + showData[i].dwmc + '</td>' +
                    '         <td>' + showData[i].zh + '</td>' +
                    '         <td>' + showData[i].zxsfnum + '</td>' +
                    '         <td>' + showData[i].dlssfnum + '</td>'
                if (showData[i].yyzddj == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + showData[i].yyzddj + '</td>'
                }
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    let str = showData[i].dqxx;
                    let x = -1
                    let y = 0;
                    if (str != null)
                        x = str.indexOf("（");
                    y = str.indexOf("）")
                    if (x != -1) {
                        str = showData[i].dqxx.substring(x + 1, y)
                    }
                    html += '<td>' + str + '</td>'
                }
                html += '</tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("hxyyxxphlist" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>排名</th>' +
                '         <th>医院</th>' +
                '         <th>总收费测试数</th>' +
                '         <th>直销收费测试数</th>' +
                '         <th>代理商收费测试数</th>' +
                '         <th>医院重点等级</th>' +
                '         <th>省份</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                var j = i + 1;
                html += '      <tr>' +
                    '         <td>' + j + '</td>' +
                    '         <td>' + showData[i].dwmc + '</td>' +
                    '         <td>' + showData[i].zh + '</td>' +
                    '         <td>' + showData[i].zxsfnum + '</td>' +
                    '         <td>' + showData[i].dlssfnum + '</td>'
                if (showData[i].yyzddj == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + showData[i].yyzddj + '</td>'
                }
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    let str = showData[i].dqxx;
                    let x = -1
                    let y = 0;
                    if (str != null)
                        x = str.indexOf("（");
                    y = str.indexOf("）")
                    if (x != -1) {
                        str = showData[i].dqxx.substring(x + 1, y)
                    }
                    html += '<td>' + str + '</td>'
                }
                html += '</tr>';
            }
            html += '   </tbody>' +
                '</table>';
        }
    }
    else{
        if ("yyxxphlist" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>排名</th>' +
                '         <th>医院</th>' +
                '         <th>总销售额</th>' +
                '         <th>直销销售额</th>' +
                '         <th>代理商销售额</th>' +
                '         <th>医院重点等级</th>' +
                '         <th>省份</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                var j = i + 1;
                html += '      <tr>' +
                    '         <td>' + j + '</td>' +
                    '         <td>' + showData[i].dwmc + '</td>' +
                    '         <td>' + showData[i].zh + '</td>' +
                    '         <td>' + showData[i].zxsfnum + '</td>' +
                    '         <td>' + showData[i].dlssfnum + '</td>'
                if (showData[i].yyzddj == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + showData[i].yyzddj + '</td>'
                }
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    let str = showData[i].dqxx;
                    let x = -1
                    let y = 0;
                    if (str != null)
                        x = str.indexOf("（");
                    y = str.indexOf("）")
                    if (x != -1) {
                        str = showData[i].dqxx.substring(x + 1, y)
                    }
                    html += '<td>' + str + '</td>'
                }
                html += '</tr>';
            }
            html += '   </tbody>' +
                '</table>';
        } else if ("hxyyxxphlist" == id) {
            html += '<table class="table">' +
                '   <thead>' +
                '      <tr>' +
                '         <th>排名</th>' +
                '         <th>医院</th>' +
                '         <th>总销售额</th>' +
                '         <th>直销销售额</th>' +
                '         <th>代理商销售额</th>' +
                '         <th>医院重点等级</th>' +
                '         <th>省份</th>' +
                '      </tr>' +
                '   </thead>' +
                '   <tbody>';
            for (let i = 0; i < showData.length; i++) {
                var j = i + 1;
                html += '      <tr>' +
                    '         <td>' + j + '</td>' +
                    '         <td>' + showData[i].dwmc + '</td>' +
                    '         <td>' + showData[i].zh + '</td>' +
                    '         <td>' + showData[i].zxsfnum + '</td>' +
                    '         <td>' + showData[i].dlssfnum + '</td>'
                if (showData[i].yyzddj == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    html += '<td>' + showData[i].yyzddj + '</td>'
                }
                if (showData[i].dqxx == null) {
                    html += '<td>' + '' + '</td>'
                } else {
                    let str = showData[i].dqxx;
                    let x = -1
                    let y = 0;
                    if (str != null)
                        x = str.indexOf("（");
                    y = str.indexOf("）")
                    if (x != -1) {
                        str = showData[i].dqxx.substring(x + 1, y)
                    }
                    html += '<td>' + str + '</td>'
                }
                html += '</tr>';
            }
            html += '   </tbody>' +
                '</table>';
        }
    }
    var dialog = $.dialog({
        title:"展示更多",
        message: html,
        closeButton: true,
        width:"1000px",
    });
}
var viewYyxxRankConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function getHxYxxx(){
    var url="/ws/statistics/viewHxYyxx"
    $.showDialog(url,'核心医院排行详细信息',viewYyxxRankConfig);
}

$("#qualityStatis #zqlx_id ul li a").on("click", function(){
    setTimeout(function(){
        getLxzqs();
    }, 10);
});

function getLxzqs(){
    // 类型
    var lx = jQuery('#qualityStatis #zqlx_id_tj').val();
    for(var i=0;i<$("#qualityStatis #dqnf_id a").length;i++){
        var dqnfid=$("#qualityStatis #dqnf_id a")[i].id;
        $("#qualityStatis #"+dqnfid).attr("onclick",$("#qualityStatis #"+dqnfid).attr("onclick").replace("delTj","addTj"));
    }
    for(var i=0;i<$("#qualityStatis #nf_id a").length;i++){
        var nfid=$("#qualityStatis #nf_id a")[i].id;
        $("#qualityStatis #"+nfid).attr("onclick",$("#qualityStatis #"+nfid).attr("onclick").replace("delTj","addTj"));
    }
    if(lx=='nf'){
        jQuery("#qualityStatis #nf_id").removeClass("hidden");
        jQuery("#qualityStatis #dqnf_id").addClass("hidden");
        jQuery("#qualityStatis #yf_id").addClass("hidden");
        jQuery("#qualityStatis #jd_id").addClass("hidden");
        jQuery("#qualityStatis [id^='dqnf_li']").remove();
        jQuery("#qualityStatis [id^='yf_li']").remove();
        jQuery("#qualityStatis [id^='jd_li']").remove();
        $("#qualityStatis #dqnf_id_tj").val("");
        $("#qualityStatis #yf_id_tj").val("");
        $("#qualityStatis #jd_id_tj").val("");
        map.zqlx='year';
        map.yf='';
        map.nf='';
        $("#qualityStatis #dqnf_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #yf_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #jd_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #nf_id a").attr("style","text-decoration:none;cursor:pointer;");
    }else if (lx=='jd'){
        jQuery("#qualityStatis #nf_id").addClass("hidden");
        jQuery("#qualityStatis #dqnf_id").removeClass("hidden");
        jQuery("#qualityStatis #yf_id").addClass("hidden");
        jQuery("#qualityStatis #jd_id").removeClass("hidden");
        jQuery("#qualityStatis [id^='nf_li']").remove();
        jQuery("#qualityStatis [id^='yf_li']").remove();
        $("#qualityStatis #nf_id_tj").val("");
        $("#qualityStatis #yf_id_tj").val("");
        map.zqlx='quarter';
        map.nf='';
        map.yf='';
        $("#qualityStatis #yf_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #jd_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #nf_id a").attr("style","text-decoration:none;cursor:pointer;");
    }else if (lx=='yf'){
        jQuery("#qualityStatis #nf_id").addClass("hidden");
        jQuery("#qualityStatis #dqnf_id").removeClass("hidden");
        jQuery("#qualityStatis #yf_id").removeClass("hidden");
        jQuery("#qualityStatis #jd_id").addClass("hidden");
        jQuery("#qualityStatis [id^='jd_li']").remove();
        jQuery("#qualityStatis [id^='nf_li']").remove();
        $("#qualityStatis #jd_id_tj").val("");
        map.zqlx='month';
        map.yf='';
        $("#qualityStatis #yf_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #jd_id a").attr("style","text-decoration:none;cursor:pointer;");
        $("#qualityStatis #nf_id a").attr("style","text-decoration:none;cursor:pointer;");
    }
}
//放大查看
function viewEnlarge(id,signal){
    $("#qualityStatis #searchMore").slideUp("low");
    quality_turnOff=true;
    $("#qualityStatis #sl_searchMore").text("高级筛选");
    if (signal=='css') {
        temp = $("#qualityStatisAmount #accordion");
    }else {
        temp=$("#qualityStatisMoney #accordionMoney");
    }
    sign = signal;
    console.log(temp);
    var html="";
    var bt="";
    var tp="";
    if (signal=='css') {
        if ('channelChargeList' == id) {
            bt = "<span style='color:#000000;'>渠道收费情况</span>\n";
            tp = "<div id='echarts_quality_channelChargeList' style='height:400px;top: 50px'></div>"
        } else if ('businessTypeChargeList' == id) {
            bt = "<span style='color:#000000;'>业务类型收费情况</span>\n";
            tp = "<div id='echarts_quality_businessTypeChargeList' style='height:400px;top: 50px'></div>"
        } else if ('channelChargeSfMfList' == id) {
            bt = "<span style='color:#000000;'>渠道免费占比</span>\n";
            tp = "<div id='echarts_quality_channelChargeSfMfList' style='height:400px;top: 50px'></div>"
        } else if ('businessTypeChargeSfMfLis' == id) {
            bt = "<span style='color:#000000;'>业务类型免费占比</span>\n";
            tp = "<div id='echarts_quality_businessTypeChargeSfMfList' style='height:400px;top: 50px'></div>"
        } else if ('topzx' == id) {
            bt = "<span style='color:#000000;'>直销top10排行</span>\n";
            tp = "<div id='echarts_quality_topzx' style='height:400px;' onclick=\"showMore('topzx','css')\"></div>"
        } else if ('topdsf' == id) {
            bt = "<span style='color:#000000;'>第三方top10排行</span>\n";
            tp = "<div id='echarts_quality_topdsf' style='height:400px;' onclick=\"showMore('topdsf','css')\"></div>"
        } else if ('topdls' == id) {
            bt = "<span style='color:#000000;'>(代理商+CSO)top10排行</span>\n";
            tp = "<div id='echarts_quality_topdls' style='height:400px;' onclick=\"showMore('topdls','css')\"></div>"
        } else if ('zxzdyyList' == id) {
            bt = "<span style='color:#000000;'>直销核心医院占比</span>\n";
            tp = "<div id='echarts_quality_zxzdyyList' style='height:400px;top: 50px'></div>"
        } else if ('kpiList' == id) {
            bt = "<span style='color:#000000;'>达成率</span>\n";
            tp = "<div id='echarts_quality_kpiList' style='height:400px;padding-top:5px;'></div>"
        } else if ('yyxxphlist' == id) {
            bt = "<span style='color:#000000;'>医院送检排行</span>\n";
            tp = "<div id='echarts_quality_yyxxphlist' style='height:400px' onclick=\"getAllYxxx('yyxxphlist','css')\"></div>"
        } else if ('hxyyxxphlist' == id) {
            bt = "<span style='color:#000000;'>核心医院送检排行</span>\n";
            tp = "<div id='echarts_quality_hxyyxxphlist' style='height:400px' onclick=\"getAllYxxx('hxyyxxphlist','css')\"></div>"
        } else if ('ryyysfcssList' == id) {
            bt = "<span style='color:#000000;'>入院医院送检排行</span>\n";
            tp = "<div id='echarts_quality_ryyysfcssList' style='height: 400px' class='col-md-12 col-sm-12 col-xs-12' onclick=\"showMore('ryyysfcssList','css')\"></div>"
        } else if ('totalpal' == id) {
            bt = "<span style='color:#000000;'>新增代理统计</span>\n";
            tp = "<span id='echarts_quality_totalpal' style='width:100%;display:inline-block;font-size: 13px !important;color:#A9A9A9;margin-top:35px;' ></span>\n" +
                "            <div id='totalpal'  class='col-md-12 col-sm-12 col-xs-12'  style='height:260px;padding-left:2px;padding-right:3px ' onclick=\"showMore('totalpal','css')\"></div>";
        } else if ('losepartnerlisty' == id) {
            bt = "<span style='color:#000000;'>一个月未送检</span>\n";
            tp = "<span  style='width:100%;display:inline-block;font-size: 13px !important;color:#A9A9A9;margin-top:35px;'></span>\n" +
                "                <div id='losepartnerlisty' class='col-md-12 col-sm-12 col-xs-12' style='max-height:220px; padding-left:2px;padding-right:3px;'>\n" +
                "                    <div id='echarts_quality_losepartnerlisty' style='height: 300px;top: 50px'>\n" +
                "                    </div>\n" +
                "            </div>";
        } else if ('losepartnerlistr' == id) {
            bt = "<span style='color:#000000;'>两个月未送检</span>\n";
            tp = "<span  style='width:100%;display:inline-block;font-size: 13px !important;color:#A9A9A9;margin-top:35px;'></span>\n" +
                "                <div id='losepartnerlistr' class='col-md-12 col-sm-12 col-xs-12' style='max-height:220px; padding-left:2px;padding-right:3px;'>\n" +
                "                    <div id='echarts_quality_losepartnerlistr' style='height: 300px;top: 50px'>\n" +
                "                    </div>\n" +
                "            </div>";
        } else if ('losepartnerlistb' == id) {
            bt = "<span style='color:#000000;'>三个月未送检</span>\n";
            tp = "<span  style='width:100%;display:inline-block;font-size: 13px !important;color:#A9A9A9;margin-top:35px;'></span>\n" +
                "                <div id='losepartnerlistb' class='col-md-12 col-sm-12 col-xs-12' style='max-height:220px; padding-left:2px;padding-right:3px;'>\n" +
                "                    <div id='echarts_quality_losepartnerlistb' style='height: 300px;top: 50px'>\n" +
                "                    </div>\n" +
                "            </div>";
        } else if ('qysfcssList' == id) {
            bt = "<span style='color:#000000;'>区域收费占比情况</span>\n";
            tp = "<div id='echarts_quality_qysfcssList' style='height:400px;top:20px;'></div>";
        } else if ('ndyrjsfcssList' == id) {
            bt = "<span style='color:#000000;'>月人均收费测试数</span>\n";
            tp = "<div id='echarts_quality_ndyrjsfcssList' style='height:400px;'></div>";
        } else if ('ksSfcssList' == id) {
            bt = "<span style='color:#000000;'>收费科室分布（按照收费测试数统计）</span>\n";
            tp = "<div id='echarts_quality_ksSfcssList' style='height:400px;' onclick=\"showMore('ksSfcssList','css')\"></div>";
        } else if ('yblxSfcssList' == id) {
            bt = "<span style='color:#000000;'>收费标本类型分布（按照收费测试数统计）</span>\n";
            tp = "<div id='echarts_quality_yblxSfcssList' style='height:400px;''onclick=\"showMore('yblxSfcssList')\"></div>";
        } else if ('thpartpal' == id) {
            bt = "<span style='color:#000000;'>新增第三方统计</span>\n";
            tp = "<span id='echarts_quality_thpartpal' style='width:100%;display:inline-block;font-size: 13px !important;color:#A9A9A9;margin-top:35px;' ></span>\n" +
                "            <div id='thpartpal'  class='col-md-12 col-sm-12 col-xs-12'  style='height:260px;padding-left:2px;padding-right:3px ' onclick=\"showMore('thpartpal','css')\"></div>";
        }
    }
    else {
        if ('channelChargeList' == id) {
            bt = "<span style='color:#000000;'>渠道收费情况</span>\n";
            tp = "<div id='echarts_qualityAmount_channelChargeList' style='height:400px;top: 50px'></div>"
        } else if ('businessTypeChargeList' == id) {
            bt = "<span style='color:#000000;'>业务类型收费情况</span>\n";
            tp = "<div id='echarts_qualityAmount_businessTypeChargeList' style='height:400px;top: 50px'></div>"
        }
       else if ('yyxxphlist' == id) {
            bt = "<span style='color:#000000;'>医院送检排行</span>\n";
            tp = "<div id='echarts_qualityAmount_yyxxphlist' style='height:400px' onclick=\"getAllYxxx('yyxxphlist','money')\"></div>"
        } else if ('hxyyxxphlist' == id) {
            bt = "<span style='color:#000000;'>核心医院送检排行</span>\n";
            tp = "<div id='echarts_qualityAmount_hxyyxxphlist' style='height:400px' onclick=\"getAllYxxx('hxyyxxphlist','money')\"></div>"
        } else if ('ryyysfcssList' == id) {
            bt = "<span style='color:#000000;'>入院医院送检排行</span>\n";
            tp = "<div id='echarts_qualityAmount_ryyysfcssList' style='height: 400px' class='col-md-12 col-sm-12 col-xs-12' onclick=\"showMore('ryyysfcssList','money')\"></div>"
        } else if ('totalpal' == id) {
            bt = "<span style='color:#000000;'>新增代理统计</span>\n";
            tp = "<span id='echarts_qualityAmount_totalpal' style='width:100%;display:inline-block;font-size: 13px !important;color:#A9A9A9;margin-top:35px;' ></span>\n" +
                "            <div id='totalpal'  class='col-md-12 col-sm-12 col-xs-12'  style='height:260px;padding-left:2px;padding-right:3px ' onclick=\"showMore('totalpal','money')\"></div>";
        } else if ('topzx' == id) {
            bt = "<span style='color:#000000;'>直销top10排行</span>\n";
            tp = "<div id='echarts_qualityAmount_topzx' style='height:400px;' onclick=\"showMore('topzx','money')\"></div>"
        } else if ('topdsf' == id) {
            bt = "<span style='color:#000000;'>第三方top10排行</span>\n";
            tp = "<div id='echarts_qualityAmount_topdsf' style='height:400px;' onclick=\"showMore('topdsf','money')\"></div>"
        } else if ('topdls' == id) {
            bt = "<span style='color:#000000;'>(代理商+CSO)top10排行</span>\n";
            tp = "<div id='echarts_qualityAmount_topdls' style='height:400px;' onclick=\"showMore('topdls','money')\"></div>"
        } else if ('zxzdyyList' == id) {
            bt = "<span style='color:#000000;'>直销核心医院占比</span>\n";
            tp = "<div id='echarts_qualityAmount_zxzdyyList' style='height:400px;top: 50px'></div>"
        }
    }

    html+=bt+"<img class='titletop' src='/images/censusimages/ksh33.png' style='width:100%;height:50px;'>\n" +
        "<div class='desktop_analysisBar' style='position: absolute;top:30px;margin-left:-20px;width:100%;'>\n" +
        "<div  class='desktop_analysisBar-head' style='margin-left:-1%;width:100%;'>\n" +
        "<a href='javascript:void(0);' onclick=\"reduction()\" class='tj1 ocFlag pull-right ' title='缩小' ><span class='fa-stack'><i class='glyphicon glyphicon-zoom-out' style='font-size: 20px'></i></span></a>\n" +
        "</div>\n" +
        "</div>"+
        tp;
    if (signal=='css') {
        $("#qualityStatisAmount #newDiv").append(html);
        $("#qualityStatisAmount #newDiv").show();
    }else {
        $("#qualityStatisMoney #newDivMoney").append(html);
        $("#qualityStatisMoney #newDivMoney").show();
    }
    var flag = null;
    if('channelChargeSfMfList'==id){
        flag =  'qd';
    }
    if('businessTypeChargeSfMfLis'==id){
        flag =  'qf';
    }
    var result = search(flag);
        if (result) {
            if (signal=='css'){
                $("#qualityStatisAmount #accordion").remove();
                loadWeekLeadStatis();
            }else {
                $("#qualityStatisMoney #accordionMoney").remove();
                loadMoneyWeekLeadStatis();
            }
            }
        }

//放大查看还原
function reduction(){
    if (sign=='css'){
        $("#qualityStatisAmount #newDiv").html("");
        $("#qualityStatisAmount #newDiv").hide();
        $("#qualityStatisAmount #all").append(temp);
    }
    else {
        $("#qualityStatisMoney #newDivMoney").html("");
        $("#qualityStatisMoney #newDivMoney").hide();
        $("#qualityStatisMoney #allMoney").append(temp);
    }
    $(function () { $('#collapseFour').collapse('show')});
    $(function () { $('#collapseTwo').collapse('show')});
    $(function () { $('#collapseThree').collapse('show')});
    $(function () { $('#collapseOne').collapse('show')});
    $(function () { $('#collapseFive').collapse('show')});
    $(function () { $('#collapseSix').collapse('show')});
    $(function () { $('#collapseSeven').collapse('show')});
    var flag = null;
    var result = search(flag);
    if (result) {
        if (sign=='css') {
            loadWeekLeadStatis();
            $("#qualityStatisAmount #qdlb_chosen").remove();
            $("#qualityStatisAmount #qflb_chosen").remove();
        }
        else {
            loadMoneyWeekLeadStatis();
            $("#qualityStatisMoney #qdlb_chosen").remove();
            $("#qualityStatisMoney #qflb_chosen").remove();
        }
        //将remove的代码重新加回去时，下拉框由于样式原因无法点击，这时候删除原有的样式，重新添加样式

    }
    jQuery('#qualityStatis .chosen-select').chosen({width: '100%'});
}


$(function () {
    //所有下拉框添加choose样式
    jQuery('#qualityStatis .chosen-select').chosen({width: '100%'});
    $("#qualityStatis [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});