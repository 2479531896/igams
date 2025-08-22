var AllStock_turnOff=true;

var allStock_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#allstock_formSearch #allstock_list').bootstrapTable({
            url: $("#allstock_formSearch #urlPrefix").val()+'/storehouse/stock/pageGetListAllStock',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#allstock_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wlbm",					// 排序字段
            sortOrder: "desc",                   // 排序方式
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
            uniqueId: "hwid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'hwid',
                title: '货物ID',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wlid',
                title: '物料ID',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '5%',
                align: 'left',
                formatter:wlbmFormat,
                sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '5%',
                align: 'left',
                visible: true
            }, {
                field: 'ychh',
                title: '货号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'scs',
                title: '生产商',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jldw',
                title: '单位',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'klsl',
                title: '库存量',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gys',
                title: '供应商',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'scph',
                title: '生产批号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zsh',
                title: '追溯号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ckmc',
                title: '仓库',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'kwmc',
                title: '库位',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'qgdh',
                title: '请购单号',
                width: '10%',
                align: 'left',
                formatter:qgdhFormat,
                visible: true
            },{
                field: 'htdh',
                title: '合同单号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'dhdh',
                title: '到货单号',
                width: '10%',
                align: 'left',
                formatter:dhdhFormat,
                visible: true
            },{
                field: 'jydh',
                title: '检验单号',
                width: '10%',
                align: 'left',
                formatter:jydhFormat,
                visible: true
            },{
                field: 'rkdh',
                title: '入库单号',
                width: '10%',
                align: 'left',
                formatter:rkdhFormat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                allStockById(row.hwid,'view',$("#allstock_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#allstock_formSearch #allstock_list").colResizable({
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
            sortLastName: "hwxx.hwid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return allStockSearchData(map);
    }
    return oTableInit
}




// 根据查询条件查询
function allStockSearchData(map){
    var cxtj=$("#allstock_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#allstock_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="2"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="3"){
        map["gys"]=cxnr;
    }else if(cxtj=="4"){
        map["scph"]=cxnr;
    }else if(cxtj=="5"){
        map["zsh"]=cxnr;
    }else if(cxtj=="6"){
        map["ckmc"]=cxnr;
    }else if(cxtj=="7"){
        map["kwmc"]=cxnr;
    }else if(cxtj=="8"){
        map["qgdh"]=cxnr;
    }else if(cxtj=="9"){
        map["htdh"]=cxnr;
    }else if(cxtj=="10"){
        map["dhdh"]=cxnr;
    }else if(cxtj=="11"){
        map["rkdh"]=cxnr;
    }else if(cxtj=="12"){
        map["jydh"]=cxnr;
    }

    var kcl = jQuery('#allstock_formSearch #kcl_id_tj').val();
    map["kcl"] = kcl;
    //到货日期开始时间
    var dhrqstart = jQuery('#allstock_formSearch #dhrqstart').val();
    map["dhrqstart"] = dhrqstart;
    //到货日期结束时间
    var dhrqend = jQuery('#allstock_formSearch #dhrqend').val();
    map["dhrqend"] = dhrqend;
    //入库日期开始时间
    var rkrqstart = jQuery('#allstock_formSearch #rkrqstart').val();
    map["rkrqstart"] = rkrqstart;
    //入库日期结束时间
    var rkrqend = jQuery('#allstock_formSearch #rkrqend').val();
    map["rkrqend"] = rkrqend;
    //质检日期开始时间
    var zjrqstart = jQuery('#allstock_formSearch #zjrqstart').val();
    map["zjrqstart"] = zjrqstart;
    //质检日期结束时间
    var zjrqend = jQuery('#allstock_formSearch #zjrqend').val();
    map["zjrqend"] = zjrqend;
    return map;
}


//提供给导出用的回调函数
function StockSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="hw.hwid";
    map["sortLastOrder"]="desc";
    map["sortName"]="hw.wlbm";
    map["sortOrder"]="desc";
    return allStockSearchData(map);
}


function allStockById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#allstock_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?hwid=" +id;
        $.showDialog(url,'详细信息',viewallstockConfig);
    }
}


/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmFormat(value,row,index){
    var html = "";
    html += "<a href='javascript:void(0);' onclick=\"queryByWl('"+row.wlid+"')\">"+row.wlbm+"</a>";
    return html;
}
function queryByWl(wlid){
    var url=$("#allstock_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}

var viewWlConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 单据号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qgdhFormat(value,row,index){
    var html = "";
    if(row.qgdh==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByQgid('"+row.qgid+"')\">"+row.qgdh+"</a>";

    }
    return html;
}

function queryByQgid(qgid){
    var url=$("#allstock_formSearch #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'请购信息',viewQgConfig);
}

var viewQgConfig={
    width		: "1500px",
    modalName	:"viewQgModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


/**
 * 到货单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhdhFormat(value,row,index){
    var html = "";
    if(row.dhdh!=null){
        html += "<a href='javascript:void(0);' onclick=\"queryByDhdh('"+row.dhid+"')\">"+row.dhdh+"</a>";
    }
    return html;
}
function queryByDhdh(dhid){
    var url=$("#allstock_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到货信息',viewhwConfig);
}

var viewhwConfig = {
    width		: "1600px",
    modalName	:"viewHwModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 入库单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function rkdhFormat(value,row,index){
    var html = "";
    if(row.rkdh!=null){
        html += "<a href='javascript:void(0);' onclick=\"queryByHwdh('"+row.rkid+"')\">"+row.rkdh+"</a>";
    }
    return html;
}
function queryByHwdh(rkid){
    var url=$("#allstock_formSearch #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'货物信息',viewhwConfig);
}

/**
 * 检验单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jydhFormat(value,row,index){
    var html = "";
    if(row.jydh!=null && row.jydh!=""){
        html += "<a href='javascript:void(0);' onclick=\"queryByJy('"+row.dhjyid+"')\">"+row.jydh+"</a>";
    }
    return html;
}
function queryByJy(dhjyid){
    var url=$("#allstock_formSearch #urlPrefix").val()+"/inspectionGoods/inspectionGoods/viewInspectionGoods?dhjyid="+dhjyid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'检验信息',viewJyConfig);
}

var viewJyConfig = {
    width		: "1200px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var viewallstockConfig = {
    width		: "800px",
    modalName	: "viewstockModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var allStock_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#allstock_formSearch #btn_view");
        var btn_query = $("#allstock_formSearch #btn_query");
        var btn_searchexport = $("#allstock_formSearch #btn_searchexport");
        var btn_selectexport = $("#allstock_formSearch #btn_selectexport");
        //添加日期控件
        laydate.render({
            elem: '#dhrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#dhrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#zjrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#zjrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#rkrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#rkrqend'
            ,theme: '#2381E9'
        });
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                allStockResult(true);
            });
        }

        /* ---------------------------查看仓库货物-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#allstock_formSearch #allstock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                allStockById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        //---------------------------导出--------------------------------------------------
        //选择导出
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#allstock_formSearch #allstock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].hwid;
                }
                ids = ids.substr(1);
                $.showDialog($('#allstock_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ALLSTOCK_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#allstock_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ALLSTOCK_SEARCH&expType=search&callbackJs=StockSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });



        /*-----------------------显示隐藏------------------------------------*/
        $("#allstock_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(AllStock_turnOff){
                $("#allstock_formSearch #searchMore").slideDown("low");
                AllStock_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#allstock_formSearch #searchMore").slideUp("low");
                AllStock_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function allStockResult(isTurnBack){
    //关闭高级搜索条件
    $("#allstock_formSearch #searchMore").slideUp("low");
    AllStock_turnOff=true;
    $("#allstock_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#allstock_formSearch #allstock_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#allstock_formSearch #allstock_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new allStock_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new allStock_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#allstock_formSearch .chosen-select').chosen({width: '100%'});

    $("#allstock_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});