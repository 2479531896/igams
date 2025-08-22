
// 列表初始化
var packPaperReportsApply_TableInit=function(){
    var oTableInit=new Object();
    // 初始化table
    oTableInit.Init=function(){
        $("#packPaperReportsApplyForm #tb_list").bootstrapTable({
            url: '/inspection/pagedataListReport',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#packPaperReportsApplyForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sjzzsq.lrsj",					//排序字段
            sortOrder: "desc",               	//排序方式
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
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "zzsqid",                	//每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                	//是否显示父子表
            columns: [
             {   checkbox: true,
                 width: '3%'
             },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sjr',
                title: '收件人',
                titleTooltip:'收件人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dh',
                title: '电话',
                titleTooltip:'电话',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'dz',
                title: '地址',
                titleTooltip:'地址',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jcxmmc',
                title: '检测类型',
                titleTooltip:'检测类型',
                width: '15%',
                align: 'left',
                sortable: true,
                formatter:jcxmFormat,
                visible: true
            },{
                    field: 'fs',
                    title: '份数',
                    titleTooltip:'份数',
                    width: '6%',
                    align: 'left',
                    sortable: true,
                    visible: true
                },
                {
                    field: 'bz',
                    title: '备注',
                    titleTooltip:'备注',
                    width: '15%',
                    align: 'left',
                    sortable: true,
                    visible: true
                }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    // 得到查询的参数
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjzzsq.zzsqid", // 防止同名排位用
            sortLastOrder: "desc", // 防止同名排位用
            ids: $("#packPaperReportsApplyForm #ids").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit
};

function jcxmFormat(value,row,index) {
    var html=row.jcxmmc;
    if(row.fjid){
        html=html+"<iframe  src='/ws/file/pdfFileStream?fjid="+row.fjid+"' id='print_"+row.fjid+"' style='display: none'></iframe>";
    }
    return html;
}

// 页面初始化
$(function(){
    // 1.初始化Table
    var oTable = new packPaperReportsApply_TableInit();
    oTable.Init();
    // $('#printDetails').attr('src', '/inspection/viewPaperReportsApply?zzsqid=0F1E387717174A638D2EE81C14D604CC&access_token='+$("#ac_tk").val());
});