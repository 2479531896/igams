var supplier_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#choosesupplier_formSearch #tb_list').bootstrapTable({
            url: $('#choosesupplier_formSearch #urlPrefix').val()+'/warehouse/supplier/pagedataListSupplier',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#choosesupplier_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"gysxx.fzrq",					// 排序字段
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
            uniqueId: "gysid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width:'2%'
            }, {
                field: 'gysid',
                title: '供应商ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'gysdm',
                title: '供应商代码',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gysmc',
                title: '供应商名称',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'gysjc',
                title: '供应商简称',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'gfbh',
                title: '供方编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'dq',
                title: '地区',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'fzrq',
                title: '发展日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'lxr',
                title: '联系人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'dh',
                title: '电话',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'sj',
                title: '手机',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'qq',
                title: 'QQ',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'wx',
                title: '微信',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'gfgllbmc',
                title: '供方管理类别',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'sfyfj',
                title: '是否有附件',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:sfyfjformat,
            }, {
                field: 'sl',
                title: '税率',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                visible: false
            }, {
               field: 'cz',
               title: '传真',
               width: '8%',
               align: 'left',
               visible: false
           }, {
                field: 'sfczkjht',
                title: '是否存在框架合同',
                width: '12%',
                align: 'left',
                visible: true,
                formatter:sfczkjhtformatter
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
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
            sortLastName: "gysxx.gysid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            scbj: $("#choosesupplier_formSearch #scbj").val(),
            jxflg: $("#choosesupplier_formSearch #jxflg").val()
            // 搜索框使用
            // search:params.search
        };
        return getSupplierData(map);
    };
    return oTableInit;
};
function sfczkjhtformatter(value,row,index){
    if(row.sfczkjht==1){
        return "是";
    }else if(row.sfczkjht==2){
        return "否";
    }
}
function getSupplierData(map){
    var cxtj=$("#choosesupplier_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#choosesupplier_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["gysdm"]=cxnr
    }else if(cxtj=="2"){
        map["gysmc"]=cxnr
    }else if(cxtj=="3"){
        map["dq"]=cxnr
    }else if(cxtj=="4"){
        map["lxr"]=cxnr
    }else if(cxtj=="5"){
        map["dh"]=cxnr
    }else if(cxtj=="6"){
        map["sj"]=cxnr
    }else if(cxtj=="7"){
        map["qq"]=cxnr
    }else if(cxtj=="8"){
        map["wx"]=cxnr
    }else if(cxtj=="9"){
        map["gysjc"]=cxnr
    }else if(cxtj=="10"){
        map["gfgllbmc"]=cxnr
    }else if(cxtj=="11"){
        map["bz"]=cxnr
    }
    return map;
}


function SupplierResult(isTurnBack){
    if(isTurnBack){
        $('#choosesupplier_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#choosesupplier_formSearch #tb_list').bootstrapTable('refresh');
    }
}
function sfyfjformat(value,row,index){
    var sfyfj = "";
    if(row.sfyfj=='1'){
        sfyfj = "<span style='color:green;'>"+"是"+"</span>";
    }else if(row.sfyfj=='0'){
        sfyfj = "<span style='color:red;'>"+"否"+"</span>";
    }
    return sfyfj;
}


var supplier_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#choosesupplier_formSearch #btn_query");

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                SupplierResult(true);
            });
        }
    };
    return oInit;
};


var viewSupplierConfig = {
    width		: "800px",
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
    var oTable = new supplier_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new supplier_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#choosesupplier_formSearch .chosen-select').chosen({width: '100%'});

});