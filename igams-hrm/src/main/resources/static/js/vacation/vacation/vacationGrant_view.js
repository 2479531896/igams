var t_map = [];
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
        field: 'ffjlid',
        title: '发放记录ID',
        titleTooltip:'发放记录ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'yhm',
        title: '用户名',
        titleTooltip:'用户名',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'zsxm',
        title: '姓名',
        titleTooltip:'姓名',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'jqlxmc',
        title: '假期类型',
        titleTooltip:'假期类型',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'kssj',
        title: '开始时间',
        titleTooltip:'开始时间',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'yxq',
        title: '有效期',
        titleTooltip:'有效期',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'fffs',
        title: '发放方式',
        titleTooltip:'发放方式',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'nd',
        title: '年度',
        titleTooltip:'年度',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'sc',
        title: '时长',
        titleTooltip:'时长',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'dw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'jeze',
        title: '假期总额',
        titleTooltip:'假期总额',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'syed',
        title: '剩余额度',
        titleTooltip:'剩余额度',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'ddze',
        title: '钉钉总额',
        titleTooltip:'钉钉总额',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'ddyyed',
        title: '钉钉已用额度',
        titleTooltip:'钉钉已用额度',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'ddsyed',
        title: '钉钉剩余额度',
        titleTooltip:'钉钉剩余额度',
        width: '6%',
        align: 'left',
        visible: true
    }
];
var viewVacationGrant_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewVacationGrant_Form #tb_list').bootstrapTable({
            url: $("#viewVacationGrant_Form #urlPrefix").val()+'/vacation/vacation/pagedataViewVacationGrant',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewVacationGrant_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "jqffjl.lrsj",				//排序字段
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
            uniqueId: "ffjlid",                     //每一行的唯一标识，一般为主键列
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
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "", //防止同名排位用
            sortLastOrder: "", //防止同名排位用
            ffszid: $("#viewVacationGrant_Form #ffszid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
var columnsArray1 = [
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
        field: 'jqxzid',
        title: '假期限制ID',
        titleTooltip:'假期限制ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'jqlxmc',
        title: '假期类型',
        titleTooltip:'假期类型',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'sc',
        title: '时长',
        titleTooltip:'时长',
        width: '3%',
        align: 'left',
        visible: true
    }
];
var viewVacationLimitation_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewVacationGrant_Form #jqxz_list').bootstrapTable({
            url: $("#viewVacationGrant_Form #urlPrefix").val()+'/vacation/vacation/pagedataViewVacationLimitation',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewVacationGrant_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "jqxz.jqlx",				//排序字段
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
            uniqueId: "jqxzid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray1,
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
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "", //防止同名排位用
            sortLastOrder: "", //防止同名排位用
            ffszid: $("#viewVacationGrant_Form #ffszid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
$(document).ready(function(){
    //初始化列表
    var oTable=new viewVacationGrant_TableInit();
    oTable.Init();
    var oTable1=new viewVacationLimitation_TableInit();
    oTable1.Init();
    //所有下拉框添加choose样式
    jQuery('#viewVacationGrant_Form .chosen-select').chosen({width: '100%'});
});