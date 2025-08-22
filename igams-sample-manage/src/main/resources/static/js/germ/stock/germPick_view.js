
// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'llmxid',
        title: '领料明细id',
        titleTooltip:'领料明细id',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'mc',
        title: '名称',
        titleTooltip:'名称',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'bh',
        title: '编号',
        titleTooltip:'编号',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'kcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '3%',
        align: 'left',
        visible: true
    },{
        field: 'kbs',
        title: '拷贝数',
        titleTooltip:'拷贝数',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'ph',
        title: '批号',
        titleTooltip:'批号',
        width: '5%',
        align: 'left',
        visible: false
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'qlsl',
        title: '请领数量',
        titleTooltip:'请领数量',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'cksl',
        title: '出库数量',
        titleTooltip:'出库数量',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'ly',
        title: '来源',
        titleTooltip:'来源',
        width: '15%',
        align: 'left',
        visible: true
    },{
        field: 'wz',
        title: '位置',
        titleTooltip:'位置',
        width: '15%',
        align: 'left',
        visible: true
    }];
var germPickView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#germPickView_Form #tb_list').bootstrapTable({
            url:'/germ/inventory/pagedataGermPickMx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#germPickView_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "qlsl",				//排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "llmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "llmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            llid: $("#germPickView_Form #llid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

$(document).ready(function(){
    //初始化列表
    var oTable=new germPickView_TableInit();
    oTable.Init();
    // 初始化页面
    //所有下拉框添加choose样式
    jQuery('#germPickView_Form .chosen-select').chosen({width: '100%'});
});
