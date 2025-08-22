var flowing_turnOff=true;
var flowing_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $("#flowing_formSearch #flowing_list").bootstrapTable({
            url: $("#flowing_formSearch #urlPrefix").val()+'/warehouse/flowing/pageGetListFlowing',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#flowing_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wlbm",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "lsid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#flowing_formSearch #flowing_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'lsid',
                title: '流水id',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'dcid',
                title: '导出id',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'djh',
                title: '单号',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:dhxxFormat,
            }, {
                field: 'djlx',
                title: '单据类型',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'sflb',
                title: '收发类别',
                width: '10%',
                align: 'left',
                visible: true

            }, {
                field: 'sl',
                title: '数量',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'bm',
                title: '部门',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'viewId',
                title: '查看',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'gys',
                title: '供应商',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'ggxh',
                title: '规格型号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'zdr',
                title: '制单人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'ph',
                title: '批号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'ck',
                title: '仓库',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'btbz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: false
            },
            {
                field: 'lsrq',
                title: '日期',
                width: '10%',
                align: 'left',
                visible: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                flowingBylsid(row.lsid,row.viewId,row.djlx,row.sflb, 'view',$("#flowing_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#flowing_formSearch #flowing_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
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
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        // 开水日期
        var lsrqstart = jQuery('#rq_id #lsrqstart').val();
        map["lsrqstart"] = lsrqstart;
        // 结束日期
        var lsrqend = jQuery('#rq_id #lsrqend').val();
        map["lsrqend"] = lsrqend;
        return getFlowingSearchData(map);
    };
    return oTableInit;
};

function getFlowingSearchData(map){
    var cxtj=$("#flowing_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#flowing_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["djh"]=cxnr
    }else if(cxtj=="1"){
        map["bm"]=cxnr
    }else if(cxtj=="2"){
        map["wlbm"]=cxnr
    }else if(cxtj=="3"){
        map["gys"]=cxnr
    }
    else if(cxtj=="4"){
        map["zdr"]=cxnr
    }
    else if(cxtj=="5"){
        map["wlmc"]=cxnr
    }
    else if(cxtj=="6"){
        map["ph"]=cxnr
    }
    else if(cxtj=="7"){
        map["ck"]=cxnr
    }
    else if(cxtj=="8"){
        map["entire"]=cxnr
    }
    // 收发类别
    var sflbs = jQuery('#flowing_formSearch #sflb_id_tj').val();
    map["sflbs"] = sflbs.replace(/'/g, "");
    // 单据类型
    var djlxs = jQuery('#flowing_formSearch #djlx_id_tj').val();
    map["djlxs"] = djlxs.replace(/'/g, "");
    return map;
}
//提供给导出用的回调函数
function FlowingSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="tmp.dcid";
    map["sortLastOrder"]="asc";
    map["sortName"]="tmp.wlbm";
    map["sortOrder"]="asc";
    return getFlowingSearchData(map);
}
function flowingResult(isTurnBack){
    //关闭高级搜索条件
    $("#flowing_formSearch #searchMore").slideUp("low");
    flowing_turnOff=true;
    $("#flowing_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#flowing_formSearch #flowing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#flowing_formSearch #flowing_list').bootstrapTable('refresh');
    }
}

//按钮动作函数

function flowingBylsid(lsid,viewId,djlx,sflb,action,tourl){
    if(!tourl){
        return;
    }
    tourl=$("#flowing_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?viewId=" +viewId+"&djlx="+djlx+"&sflb="+sflb;
        $.showDialog(url,'详细信息',viewFlowingConfig);
    }else if (action=='viewDhxx'){
            var url=tourl+"?dhid="+lsid;
        $.showDialog(url,'查看单号信息',viewDhxxConfig);
    }
    else if (action=='viewChxx'){
        var url=tourl+"?ckid="+lsid;
        $.showDialog(url,'查看单号信息',viewChxxConfig);
    }
    else if (action=='viewRkxx'){
        var url=tourl+"?rkid="+lsid;
        $.showDialog(url,'查看单号信息',viewRkxxConfig);
    }
    else if (action=='viewDbxx'){
        var url=tourl+"?dbid="+lsid;
        $.showDialog(url,'查看单号信息',viewDbxxConfig);
    }
    else if (action=='viewJcxx'){
        var url=tourl+"?jcjyid="+lsid;
        $.showDialog(url,'查看单号信息',viewJyxxConfig);
    }
    else if (action=='viewGhxx'){
        var url=tourl+"?jcghid="+lsid;
        $.showDialog(url,'查看单号信息',viewJyxxConfig);
    }
}
//显示对应单号的数据
function dhxxFormat(value,row,index) {
    if (row.djlx=='到货单')
    {
        return "<a href='javascript:void(0);' onclick=\"flowingBylsid('" + row.lsid + "', '" + row.viewId + "','" + row.djlx + "','" + row.sflb + "','viewDhxx','/storehouse/arrivalGoods/pagedataViewArrivalGoods')\" >"+row.djh +"</a>";
    }
    else if (row.djlx == '出库单') {
        return "<a href='javascript:void(0);' onclick=\"flowingBylsid('" + row.lsid + "', '" + row.viewId + "','"  + row.djlx + "','" + row.sflb + "', 'viewChxx','/warehouse/outDepot/viewOutbound')\" >" + row.djh + "</a>";
    }
    else if (row.djlx == '入库单') {
        return "<a href='javascript:void(0);' onclick=\"flowingBylsid('" + row.lsid + "', '" + row.viewId + "','"  + row.djlx + "', '" + row.sflb + "','viewRkxx','/warehouse/putInStorage/pagedataViewPutInStorage')\" >" + row.djh + "</a>";
    }
   else if (row.djlx == '调拨单') {
        return "<a href='javascript:void(0);' onclick=\"flowingBylsid('" + row.lsid + "', '" + row.viewId + "','"  + row.djlx + "','" + row.sflb + "','viewDbxx','/allocate/allocate/viewAllocate')\" >" + row.djh + "</a>";
    }
    else if (row.djlx == '借出单') {
        return "<a href='javascript:void(0);' onclick=\"flowingBylsid('" + row.lsid + "', '" + row.viewId + "','"  + row.djlx + "', '" + row.sflb + "','viewJcxx','/borrowing/borrowing/viewBorrowing')\" >" + row.djh + "</a>";
    }
    else if (row.djlx == '归还单') {
        return "<a href='javascript:void(0);' onclick=\"flowingBylsid('" + row.lsid + "', '" + row.viewId + "','"  + row.djlx + "', '" + row.sflb + "','viewGhxx','/repaid/repaid/viewRepaid')\" >" + row.djh + "</a>";
    }
}
var flowing_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#flowing_formSearch #btn_view");
        var btn_query = $("#flowing_formSearch #btn_query");
        var btn_selectexport = $("#flowing_formSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#flowing_formSearch #btn_searchexport");//搜索导出
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                flowingResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#flowing_formSearch #flowing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){

                flowingBylsid(sel_row[0].lsid,sel_row[0].viewId, sel_row[0].djlx,sel_row[0].sflb,"view",btn_view.attr("tourl"));

            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------选中导出------------------------------------*/
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#flowing_formSearch #flowing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].dcid;
                }
                ids = ids.substr(1);
                $.showDialog($('#flowing_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=FLOWING_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        /*-----------------------搜索导出------------------------------------*/
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#flowing_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=FLOWING_SEARCH&expType=search&callbackJs=FlowingSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

        /**显示隐藏**/
        $("#flowing_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(flowing_turnOff){
                $("#flowing_formSearch #searchMore").slideDown("low");
                flowing_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#flowing_formSearch #searchMore").slideUp("low");
                flowing_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};

var sfbc=0;//是否继续保存
var viewFlowingConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewDhxxConfig={
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewChxxConfig={
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var viewRkxxConfig={
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var viewJyxxConfig={
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewDbxxConfig={
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new flowing_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new flowing_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#flowing_formSearch .chosen-select').chosen({width: '100%'});


    $("#flowing_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    laydate.render({
       elem: '#rq_id #lsrqstart'
      ,theme: '#2381E9'
    });
    laydate.render({
       elem: '#rq_id #lsrqend'
      ,theme: '#2381E9'
    });
});