var t_map=[];
var viewPerformance_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#performanceViewForm #view_list").bootstrapTable({
            url:   $('#performanceViewForm #urlPrefix').val()+'/performance/performance/pagedataGetPerformance',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#performanceViewForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: false,                     // 是否启用排序
            sortName:"jxmx.jxmxid",					// 排序字段
            sortOrder: "asc",                   // 排序方式
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
            uniqueId: "jxmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    field: 'jxmxid',
                    title: '绩效明细id',
                    width: '6%',
                    align: 'left',
                    visible: false
                 },{
                    field: 'xmmc',
                    title: '评估项目',
                    width: '12%',
                    align: 'left',
                    visible: true
                },{
                    field: 'nrmc',
                    title: '评估内容',
                    width: '12%',
                    align: 'left',
                    visible: true
                },{
                    field: 'bizmc',
                    title: '标准',
                    width: '20%',
                    align: 'left',
                    formatter:edit_bizformat,
                    visible: true
                },{
                    field: 'bz',
                    title: '备注',
                    width: '25%',
                    align: 'left',
                    formatter:edit_bzformat,
                    visible: true
                },{
                    field: 'df',
                    title: '得分',
                    width: '10%',
                    align: 'left',
                    formatter:view_dfformat,
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
                $('#performanceViewForm #view_list').bootstrapTable('mergeCells',{
                    index:0,
                    field:'xmmc',
                    rowspan:3
                });
                $('#performanceViewForm #view_list').bootstrapTable('mergeCells',{
                    index:4,
                    field:'xmmc',
                    rowspan:3
                });
                $('#performanceViewForm #view_list').bootstrapTable('mergeCells',{
                    index:7,
                    field:'xmmc',
                    rowspan:2
                });
                $('#performanceViewForm #view_list').bootstrapTable('mergeCells',{
                    index:9,
                    field:'xmmc',
                    rowspan:3
                });
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            gfjxid: $('#performanceViewForm #gfjxid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}

function edit_bizformat(value,row,index){
    var html = "";
    if(row.bizmc!=null && row.bizmc!=""){
        html = html + "<span>"+row.bizmc + "</span>"
    }
    return html;
}


function edit_bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "";
    }
    var html = "<textarea readonly='readonly' id='bz_"+index+"'row='3' value='"+row.bz+"' text='"+row.bz+"' name='bz_"+index+"'  style='height: auto;resize: vertical;'  class='form-control'>"+row.bz+"</textarea>";
    return html;
}

function view_dfformat(value,row,index){
    var df = "";
    if(row.dfbj!=null && row.dfbj!=""){
        df= row.dfbj;
    }else{
        if(row.df!=null && row.df!=""){
            df = row.df;
        }
    }
    html = "<span>"+ df +"</span>"
    return html;
}

$(function(){
    var oTable_t=new viewPerformance_TableInit();
    oTable_t.Init();
})
