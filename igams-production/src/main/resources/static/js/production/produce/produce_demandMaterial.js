var produceACMaterial_turnOff=true;
var produceACMaterial_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable({
            url: $('#produceACMaterial_formSearch #urlPrefix').val() + '/progress/produce/pageGetListDemandMaterial',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#produceACMaterial_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "xqjhmx.lrsj",				//排序字段
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
            uniqueId: "xqjhmx.xqjhmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'xqjhmxid',
                title: '需求计划明细id',
                width: '8%',
                align: 'left',
                visible: false
            },
                {
                    field: 'xqdh',
                    title: '需求单号',
                    titleTooltip:'需求单号',
                    formatter:xqdhFormat,
                    width: '8%',
                    align: 'left',
                    sortable: true,
                    visible: true

                },
                {
                    field: 'xqrq',
                    title: '需求日期',
                    sortable: true,
                    titleTooltip:'需求日期',
                    width: '6%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'wlbm',
                    title: '物料编码',
                    titleTooltip:'物料编码',
                    width: '6%',
                    align: 'left',
                    sortable: true,
                    visible: true
                }, {
                    field: 'wlmc',
                    title: '物料名称',
                    titleTooltip:'物料名称',
                    width: '6%',
                    align: 'left',
                    sortable: true,
                    visible: true
                }, {
                    field: 'gg',
                    title: '规格',
                    titleTooltip:'规格',
                    width: '6%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'sl',
                    title: '需求数量',
                    titleTooltip:'需求数量',
                    sortable: true,
                    width: '3%',
                    align: 'left',
                    visible: true

                }, {
                    field: 'jldw',
                    title: '单位',
                    titleTooltip:'单位',
                    width: '2%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'rksj',
                    title: '入库时间',
                    titleTooltip:'入库时间',
                    width: '6%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'dhsl',
                    title: '入库数量',
                    titleTooltip:'入库数量',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'ychh',
                    title: '货号',
                    titleTooltip:'货号',
                    width: '4%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'rkzt',
                    title: '入库状态',
                    titleTooltip:'入库状态',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'zt',
                    title: '审核状态',
                    titleTooltip:'审核状态',
                    width: '3%',
                    align: 'left',
                    formatter:shztFormat,
                    visible: true
                }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                produceACMaterialDealById(row.xqjhmxid, 'view',$("#produceACMaterial_formSearch #btn_view").attr("tourl"));
            },
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "xqjhmx.xqjhmxid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return getProduceSearchData(map);
    };
    return oTableInit;
};

function searchProduceResult(isTurnBack){
    //关闭高级搜索条件
    $("#produceACMaterial_formSearch #searchMore").slideUp("low");
    produceACMaterial_turnOff=true;
    $("#produceACMaterial_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable('refresh');
    }
}
function shztFormat(value,row,index) {
    if (row.zt=='00'){
        return '未提交'
    }else if (row.zt=='10'){
        return '审核中'
    }else if (row.zt=='15'){
        return '审核不通过'
    }else if (row.zt='80'){
        return '审核通过'
    }
}
function getProduceSearchData(map){
    var cxtj = $("#produceACMaterial_formSearch #cxtj").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#produceACMaterial_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr;
    }else if (cxtj == "1") {
        map["sqrmc"] = cxnr;
    }else if (cxtj == "2") {
        map["xqdh"] = cxnr;
    }else if (cxtj == "3") {
        map["wlbm"] = cxnr;
    }else if (cxtj == "4") {
        map["wlmc"] = cxnr;
    }else if (cxtj == "5") {
        map["ychh"] = cxnr;
    }
    // 入库开始日期
    var rksjstart = jQuery('#produceACMaterial_formSearch #rksjstart').val();
    map["rksjstart"] = rksjstart;
    // 入库结束日期
    var rksjend = jQuery('#produceACMaterial_formSearch #rksjend').val();
    map["rksjend"] = rksjend;
    // 申请开始日期
    var sqsjstart = jQuery('#produceACMaterial_formSearch #sqsjstart').val();
    map["sqsjstart"] = sqsjstart;
    // 申请结束日期
    var sqsjend = jQuery('#produceACMaterial_formSearch #sqsjend').val();
    map["sqsjend"] = sqsjend;
    var rkzts = jQuery('#produceACMaterial_formSearch #rkzt_id_tj').val();
    map["rkzts"] = rkzts.replace(/'/g, "");
    var zts = jQuery('#produceACMaterial_formSearch #zt_id_tj').val();
    map["zts"] = zts.replace(/'/g, "");
    return map;
}
function xqdhFormat(value,row,index) {
    if (row.xqdh){
        return "<a href='javascript:void(0);' onclick='queryByXqdh(\"" + row.cpxqid + "\")' >"+row.xqdh+"</a>";
    }else{
        row.xqdh = "-";
        return "<a href='javascript:void(0);'>"+row.xqdh+"</a>";
    }
}

function queryByXqdh(cpxqid){
    var url=$("#produceACMaterial_formSearch #urlPrefix").val()+"/progress/progress/pagedataProgress?cpxqid="+cpxqid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'需求计划',viewProduceXqdhMaterialConfig);
}

var viewProduceXqdhMaterialConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewProduceACConfig = {
    width		: "1400px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//提供给导出用的回调函数
function ACMaterialSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="xqjhmx.xgsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="xqjhmx.lrsj";
    map["sortOrder"]="desc";
    return getProduceSearchData(map);
}
//按钮动作函数
function produceACMaterialDealById(id,action,tourl) {
    if (!tourl) {
        return;
    }
    tourl = $('#produceACMaterial_formSearch #urlPrefix').val() + tourl;
    if (action == 'view') {
        var url = tourl + "?xqjhmxid=" + id;
        $.showDialog(url, '查看', viewProduceACConfig);
    }
}
var produce_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#produceACMaterial_formSearch #btn_view");
        var btn_query = $("#produceACMaterial_formSearch #btn_query");
        var btn_searchexport = $("#produceACMaterial_formSearch #btn_searchexport");
        var btn_selectexport = $("#produceACMaterial_formSearch #btn_selectexport");

        //添加日期控件
        laydate.render({
            elem: '#sqsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sqsjend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#rksjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#rksjend'
            ,theme: '#2381E9'
        });
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchProduceMaterialResult(true);
            });
        }
        btn_view.unbind("click").click(function(){
            var sel_row = $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                produceACMaterialDealById(sel_row[0].xqjhmxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].xqjhmxid;
                }
                ids = ids.substr(1);
                $.showDialog($('#produceACMaterial_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ACMATERIAL_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#produceACMaterial_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ACMATERIAL_SEARCH&expType=search&callbackJs=ACMaterialSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#produceACMaterial_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(produceACMaterial_turnOff){
                $("#produceACMaterial_formSearch #searchMore").slideDown("low");
                produceACMaterial_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#produceACMaterial_formSearch #searchMore").slideUp("low");
                produceACMaterial_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };

    return oInit;
};
function searchProduceMaterialResult(isTurnBack){
    //关闭高级搜索条件
    $("#produceACMaterial_formSearch #searchMore").slideUp("low");
    produceACMaterial_turnOff=true;
    $("#produceACMaterial_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#produceACMaterial_formSearch #produce_tb_list').bootstrapTable('refresh');
    }
}
var produceAC_PageInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var zt = $("#produceACMaterial_formSearch a[id^='zt_id_']");
        $.each(zt, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code === '80'){
                addTj('zt',code,'produceACMaterial_formSearch');
            }
        });
    }
    return oInit;
}
$(function(){
    var pageInit=produceAC_PageInit();
    pageInit.Init();
    //1.初始化Table
    var oTable = new produceACMaterial_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new produce_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#produceACMaterial_formSearch .chosen-select').chosen({width: '100%'});

});
