var chooseDocument_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#chooseDocumentForm #tb_list').bootstrapTable({
            url: $("#chooseDocumentForm #urlPrefix").val()+'/production/document/pagedataGetListDocument?nwjid='+$("#chooseDocumentForm #nwjid").val(),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#chooseDocumentForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "wj.lrsj",				//排序字段
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
            uniqueId: "wjid",                     //每一行的唯一标识，一般为主键列
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
                field: 'wjbh',
                title: '文件编号',
                titleTooltip:'文件编号',
                width: '15%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'wjmc',
                title: '文件名称',
                titleTooltip:'文件名称',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'bbh',
                title: '版本',
                titleTooltip:'版本',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'sxrq',
                title: '生效日期',
                titleTooltip:'生效日期',
                width: '10%',
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
                var wjid = row.wjid;
                var wjbh = row.wjbh;
                $("#chooseDocumentForm  #yxwj").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                $("#chooseDocumentForm  #yxwj").tagsinput('add',{"value":wjid,"text":wjbh});
                return;
            },
            //取消每一个单选框时对应的操作；
            onUncheck:function(row){
                var wjid = row.wjid;
                var wjbh = row.wjbh;
                $("#chooseDocumentForm  #yxwj").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                $("#chooseDocumentForm  #yxwj").tagsinput('remove',{"value":wjid,"text":wjbh});
                return;
            }
        });
        /* $("#chooseDocumentForm #tb_list").colResizable({
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
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "wj.wjid", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        var cxtj = $("#chooseDocumentForm #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#chooseDocumentForm #cxnr').val());
        // '0':'文件编码','1':'文件名称','3':'版本号','4':'跟踪记录','5':'备注'
        if (cxtj == "0") {
            map["wjbh"] = cxnr;
        }else if (cxtj == "1") {
            map["wjmc"] = cxnr;
        }else if (cxtj == "3") {
            map["bbh"] = cxnr;
        }else if (cxtj == "4") {
            map["gzjl"] = cxnr;
        }else if (cxtj == "5") {
            map["bz"] = cxnr;
        }else if (cxtj == "6") {
            map["jgmc"] = cxnr;
        }
        return map;
    };
    return oTableInit;
};


var chooseDocument_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query = $("#chooseDocumentForm #btn_query");

        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchChooseDocumentResult();
            });
        }
    };

    return oInit;
};

function searchChooseDocumentResult(){
    $('#chooseDocumentForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
}


$(function(){
    var rows = $("#ajaxForm #glwj_list").bootstrapTable('getData');
    if(rows!=null&&rows.length){
        $("#chooseDocumentForm  #yxwj").tagsinput({
            itemValue: "value",
            itemText: "text",
        })
        for (var i=0; i < rows.length; i++){
            if(rows[i].zwjid){
                $("#chooseDocumentForm  #yxwj").tagsinput('add',{"value":rows[i].zwjid,"text":rows[i].wjbh});
            }
        }
    }
    // 1.初始化Table
    var oTable = new chooseDocument_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new chooseDocument_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#chooseDocumentForm .chosen-select').chosen({width: '100%'});
});