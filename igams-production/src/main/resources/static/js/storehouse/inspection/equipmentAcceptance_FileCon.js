var equipmentAcceptance_FileConForm_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptance_FileConForm #sbFiletable_list").bootstrapTable({
            url: $("#equipmentAcceptance_FileConForm #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataDeviceFiles',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptance_FileConForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sbglwj.lrsj",				//排序字段
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
            uniqueId: "sbglwjid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序号',
                width: '2%',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                align: 'left',
                visible:true
            },{
                field: 'wlmc',
                title: '文件名称',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:wjmcformat
            },{
                field: 'wjbm',
                title: '文件编码',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sxrq',
                title: '生效日期',
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
        });
        $("#equipmentAcceptance_FileConForm #sbFiletable_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            sbglid: $("#equipmentAcceptance_FileConForm #sbglid").val(), //
            lb:$("#equipmentAcceptance_FileConForm #lb").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    }
    return oTableInit
}
function wjmcformat(value, row, index){
    return "<a href='javascript:void(0);' onclick=\"viewFlieList('" + row.wjid + "','/production/document/viewDocument')\" >" + row.wjmc + "</a>";
}
function viewFlieList(id,url){
    url=$("#equipmentAcceptance_FileConForm #urlPrefix").val()+url+"?wjid="+id;
    $.showDialog(url,'查看调试记录',viewfilelistConfig);
}
var viewfilelistConfig = {
    width        : "1000px",
    height        : "500px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
$(function(){
    // 1.初始化Table
    var oTable = new equipmentAcceptance_FileConForm_TableInit();
    oTable.Init();
    jQuery('#equipmentAcceptance_FileConForm .chosen-select').chosen({width: '100%'});
})