var lllist = [];
var processingRecords_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#processingRecords_formSearch #processingRecords_list').bootstrapTable({
            url: $("#processingRecords_formSearch #urlPrefix").val() + '/saleFeedback/saleFeedback/pagedataProcessingRecords',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#processingRecords_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "cljlxx.lrsj",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       // 初始化加载第一页，默认第一页
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
            uniqueId: "",                     // 每一行的唯一标识，一般为主键列
            showToggle: false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index + 1;
                    },
                    titleTooltip: '序号',
                    width: '1%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'lrsj',
                    title: '时间',
                    width: '10%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'lrry',
                    title: '录入人员',
                    width: '5%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'llid',
                    title: '领料单号',
                    width: '50%',
                    align: 'left',
                    visible: true,
                    formatter: lldhformat,
                    sortable: true
                }, {
                    field: 'bz',
                    title: '备注',
                    width: '25%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'sfjs',
                    title: '是否结束',
                    width: '6%',
                    align: 'left',
                    formatter: sfjsformat,
                    visible: true,
                    sortable: true
                }
            ],
            onLoadSuccess: function (map) {
                lllist = map.llglDtos;
                for (var i = 0; i < lllist.length; i++) {
                    $("#processingRecords_formSearch #lldh_"+lllist[i].llid).text(lllist[i].lldh)
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
        $("#processingRecords_formSearch #processingRecords_list").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit',
                postbackSafe: true,
                partialRefresh: true
            }
        );
    };
    //得到查询的参数
    oTableInit.queryParams = function (params) {
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token: $("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "cljlxx.lrry", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            shfkid: $("#processingRecords_formSearch #shfkid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

/**
 * 领料单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function lldhformat(value,row,index){
    if(row.llid == null){
        return row.llid = "" ;
    }
    var szllid = row.llid.split(",");
    var html = ""
    for (var i = 0; i < szllid.length; i++) {
        html = html + ",<a href='javascript:void(0);' id='lldh_"+szllid[i]+"' value='"+szllid[i]+"' title='"+szllid[i]+"' onclick='viewllxx(\""+szllid[i]+"\")'></a>";
    }
    if (html.length>0){
        html = html.substring(1)
    }
    return html;
}
function viewllxx(llid) {
    var url=$("#processingRecords_formSearch #urlPrefix").val()+"/storehouse/receiveMateriel/viewReceiveMateriel?llid="+llid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'领料详细信息',viewllxxConfig);
}
function sfjsformat(value,row,index) {
    var html="";
    if(row.sfjs=='1'){
        html="<span style='color:green;'>是</span>";
    }else if (row.sfjs=='0'){
        html="<span style='color:red;'>否</span>";
    }else
        html="";
    return html;

}
/**
 * 领料查看
 */
var viewllxxConfig={
    width		: "1200px",
    modalName	:"viewllxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
$(function () {

    //0.界面初始化
    // 1.初始化Table
    var oTable = new processingRecords_TableInit();
    oTable.Init();
    // 所有下拉框添加choose样式
    jQuery('#processingRecords_formSearch .chosen-select').chosen({width: '100%'});


    $("#processingRecords_formSearch [name='more']").each(function () {
        $(this).on("click", s_showMoreFn);
    });

});