var Llxx_turnOff=true;
var columnsArray = [
    {
        checkbox: true
    },{
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '10%',
        align: 'left',
        visible:true
    },{
        field: 'llid',
        title: '领料ID',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: false
    },{
        field: 'lldh',
        title: '领料单号',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'sqry',
        title: '申请人',
        width: '20%',
        align: 'left',
        visible:true
    },{
        field: 'sqrq',
        title: '申请时间',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'sqbm',
        title: '申请部门',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    }]
var ChooseLlxx_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#chooseLlxxForm #llxx_list").bootstrapTable({
            url: $("#chooseLlxxForm input[name='urlPrefix']").val() +'/storehouse/receiveMateriel/pageGetListReceiveMateriel',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseLlxxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "llgl.lrsj",				//排序字段
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
            uniqueId: "llid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
                var selLldh = $("#chooseLlxxForm #yxlldh").tagsinput('items');
                var flag = true;
                for(var i = 0; i < selLldh.length; i++){
                    var value = selLldh[i].value;
                    if (row.llid == value){
                        flag = false;
                    }
                }
                if (flag){
                    $("#chooseLlxxForm #yxlldh").tagsinput('add', { "value": row.llid, "text": row.lldh });
                }else {
                }
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "llgl.llid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return llxxSearchData(map);
    }
    return oTableInit
}
function llxxSearchData(map){
    var cxtj=$("#chooseLlxxForm #cxtj").val();
    var cxnr=$.trim(jQuery('#chooseLlxxForm #cxnr').val());
    if(cxtj=="0"){
        map["lldh"]=cxnr
    }else if(cxtj=="1"){
        map["sqrmc"]=cxnr
    }else if(cxtj=="2"){
        map["sqbmmc"]=cxnr
    }else if(cxtj=="3"){
        map["bz"]=cxnr
    }
    return map;
}
function searchLlxxResult(isTurnBack){
    //关闭高级搜索条件
    $("#chooseLlxxForm #searchMore").slideUp("low");
    Llxx_turnOff=true;
    $("#chooseLlxxForm #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#chooseLlxxForm #llxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#chooseLlxxForm #llxx_list').bootstrapTable('refresh');
    }
}
/**
 * 初始化页面
 */
var ChooseLlxx_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化已选单据号
        initTagsinput();
        var btn_query=$("#chooseLlxxForm #btn_query");
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchLlxxResult(true);
            });
        }
    }
    return oInit;
}

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
    $("#chooseLlxxForm  #yxlldh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
}

$(function(){
    // 1.初始化Table
    var oTable = new ChooseLlxx_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ChooseLlxx_ButtonInit();
    oButtonInit.Init();
    jQuery('#chooseLlxxForm .chosen-select').chosen({width: '100%'});
})