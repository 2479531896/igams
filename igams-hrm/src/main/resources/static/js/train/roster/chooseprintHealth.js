var t_map=[];
var chooseprintHealth_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#chooseprintHealthForm #print_list").bootstrapTable({
            url:   $('#chooseprintHealthForm #urlPrefix').val()+'/roster/roster/pagedataGetHealth',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#chooseprintHealthForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: false,                     // 是否启用排序
            sortName:"jkda.jcsj",					// 排序字段
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
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jkdaid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    checkbox: true,
                    width: '3%'
                }
                ,{
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '5%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'jkdaid',
                    title: '健康答案id',
                    width: '6%',
                    align: 'left',
                    visible: false
                 },{
                    field: 'bm',
                    title: '部门',
                    width: '10%',
                    align: 'left',
                    visible: false
                },{
                    field: 'gwmc',
                    title: '岗位名称',
                    width: '15%',
                    align: 'left',
                    visible: false
                },{
                    field: 'jcxm',
                    title: '检查项目',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jcsj',
                    title: '检查时间',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jcjg',
                    title: '检查结果',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'clyj',
                    title: '处理意见',
                    width: '35%',
                    align: 'left',
                    visible: true
                }],
            onLoadSuccess:function(map){
                t_map=map;
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
            onPostBody:function(){
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            yghmcid: $('#chooseprintHealthForm #yghmcid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}

$(function(){
    var oTable_t=new chooseprintHealth_TableInit();
    oTable_t.Init();
})
