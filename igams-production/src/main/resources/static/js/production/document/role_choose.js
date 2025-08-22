var chooseRole_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#chooseRoleForm #tb_list').bootstrapTable({
            url: $("#chooseRoleForm #urlPrefix").val()+'/ws/production/role/pagedataGetUnSelectJs?njsids='+$("#chooseRoleForm #njsids").val(),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#chooseRoleForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "xtjs.lrsj",				//排序字段
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
            uniqueId: "jsid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '3%'
            }, {
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '2%',
                align: 'left',
                visible:true
            }, {
                field: 'jsmc',
                title: '角色名称',
                titleTooltip:'角色名称',
                width: '15%',
                align: 'left',
                visible: true,
                sortable: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onCheck: function (row, $element) {
                var jsid = row.jsid;
                var jsmc = row.jsmc;
                $("#chooseRoleForm  #yxjs").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                $("#chooseRoleForm  #yxjs").tagsinput('add',{"value":jsid,"text":jsmc});
                return;
            },
            //取消每一个单选框时对应的操作；
            onUncheck:function(row){
                var jsid = row.jsid;
                var jsmc = row.jsmc;
                $("#chooseRoleForm  #yxjs").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                $("#chooseRoleForm  #yxjs").tagsinput('remove',{"value":jsid,"text":jsmc});
                return;
            }
        });
        /* $("#chooseRoleForm #tb_list").colResizable({
             liveDrag:true, 
             gripInnerHtml:"<div class='grip'></div>", 
             draggingClass:"dragging", 
             resizeMode:'fit', 
             postbackSafe:true,
             partialRefresh:true}        
         );*/
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
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "xtjs.jsid", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        var cxtj = $("#chooseRoleForm #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#chooseRoleForm #cxnr').val());
        if (cxtj == "0") {
            map["jsmc"] = cxnr;
        }
        return map;
    };
    return oTableInit;
};


var chooseRole_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query = $("#chooseRoleForm #btn_query");

        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchChooseRoleResult();
            });
        }
    };

    return oInit;
};

function searchChooseRoleResult(){
    $('#chooseRoleForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
}


$(function(){
    // 1.初始化Table
    var oTable = new chooseRole_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new chooseRole_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#chooseRoleForm .chosen-select').chosen({width: '100%'});
});