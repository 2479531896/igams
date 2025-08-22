var labelPrintingList=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#labelPrinting_formSearch #labelPrinting_list").bootstrapTable({
            url: $("#labelPrinting_formSearch #urlPrefix").val()+'/storehouse/arrivalGoods/pagedataLabelPrinting',
            method: 'get',                      // 请求方式（*）
            toolbar: '#labelPrinting_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'hwid',
                title: '货物id',
                titleTooltip:'货物id',
                width: '1%',
                align: 'left',
                visible: false
            },{
                field: 'u8rkdh',
                title: 'u8入库单号',
                titleTooltip:'u8入库单号',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '4%',
                align: 'left',
                formatter:wlbmformat,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'dhsl',
                title: '到货数量',
                titleTooltip:'到货数量',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'yxq',
                title: '有效期',
                titleTooltip:'有效期',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'scph',
                title: '生产批号',
                width: '7%',
                align: 'left',
                titleTooltip:'生产批号',
                visible: true
            },{
                field: 'zsh',
                title: '追溯号',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'dhbz',
                title: '到货备注',
                width: '6%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber:  1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "hwxx.hwid", // 防止同名排位用
            sortLastOrder: "desc", // 防止同名排位用
            dhid:$("#labelPrinting_formSearch #dhid").val()

            // 搜索框使用
            // search:params.search

        };
        return map;
    };
    return oTableInit;
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
    var html = "";
    html += "<a href='javascript:void(0);' onclick=\"ajaxForm_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    return html;
}
function ajaxForm_queryByWlbm(wlid){
    var url=$("#labelPrinting_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}
var viewWlConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewDhConfig = {
    width		: "1600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(function(){
    var oTable = new labelPrintingList();
    oTable.Init();
});