
var subsidyset_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#subsidyset_FormSearch #tb_list').bootstrapTable({
            url: $("#subsidyset_FormSearch #urlPrefix").val()+ '/attendance/attendance/pagedataSubsidys',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#subsidyset_FormSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"btgl.lrsj",					//排序字段
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
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "btglid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width:'1%'
            }, {
                field: 'btglid',
                title: '补贴管理ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'btmc',
                title: '补贴名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bzsc',
                title: '标准时长',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'bzsj',
                title: '标准时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'bzje',
                title: '标准金额',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'dzje',
                title: '递增金额',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'dzjg',
                title: '递增间隔',
                align: 'left',
                width: '4%',
                visible: true
            },{
                field: 'dzkssj',
                title: '递增开始时间',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'fdje',
                title: '封顶金额',
                align: 'left',
                width: '3%',
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'xgrymc',
                title: '修改人员',
                align: 'left',
                width: '6%',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
        $("#subsidyset_FormSearch #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
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
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "btgl.xgsj", //防止同名排位用
            sortLastOrder: "desc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};


$(function(){
    //1.初始化Table
    var oTable = new subsidyset_TableInit();
    oTable.Init();
});
