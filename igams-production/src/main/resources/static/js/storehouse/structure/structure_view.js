var jhmx_TableInit = function () {
    //初始化Table
    var oTableInit=new Object();
    var cpjgid=$("#verStructure_Form #cpjgid").val();
    oTableInit.Init=function(){
        $("#verStructure_Form #jhmx_view").bootstrapTable({
            url: $("#verStructure_Form #urlPrefix").val()+'/storehouse/structure/pagedataGetListStructureInfo?cpjgid='+cpjgid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#verStructure_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "cpjgmxid",				//排序字段
            sortOrder: "asc",                   //排序方式
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
            uniqueId: "cpjgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '1%',
                align: 'left',
                visible:true
            },{
                field: 'zjhh',
                title: '子件行号',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'gxhh',
                title: '工序行号',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jbyl',
                title: '基本用量',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jcsl',
                title: '基础数量',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gdylmc',
                title: '固定用量',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zjshl',
                title: '子件损耗率',
                width: '5%',
                formatter: verStructure_Form_zjshlformat,
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlbm',
                title: '子件物料编码',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlmc',
                title: '子件物料名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ckmc',
                title: '仓库',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gylxmc',
                title: '供应类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sysl',
                title: '使用数量',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'scrq',
                title: '生产日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sxrq',
                title: '失效日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            // },{
            //     field: 'llbmmc',
            //     title: '领料部门',
            //     width: '5%',
            //     align: 'left',
            //     sortable: true,
            //     visible: true
            },{
                field: 'ccpmc',
                title: '产出品',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cclxmc',
                title: '产出类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cbxgmc',
                title: '成本相关',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(map){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        })
        $("#verStructure_Form #jhmx_view").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "cpjgid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit
}

/**
 *子件损耗率
 * @param value
 * @param row
 * @param index
 * @returns
 */
function verStructure_Form_zjshlformat(value,row,index){
    let zjshl = 0;
    if (row.zjshl == null){
        zjshl = 0
    }else {
        zjshl = row.zjshl
    }
    var html="<span/>"+zjshl+"<span>%</span>";
    return html;
}
$(function(){
    //0.界面初始化
    //1.初始化Table
    var oTable = new jhmx_TableInit();
    oTable.Init();

})