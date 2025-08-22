var expense_turnOff=true;
var ddExpense_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#ddExpense_formSearch #ddExpense_list").bootstrapTable({
            url: $("#ddExpense_formSearch #urlPrefix").val()+'/expense/expense/pageGetListDdExpense',
            method: 'get',                      // 请求方式（*）
            toolbar: '#ddExpense_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "ddbxid",				// 排序字段
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
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ddbxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'spslid',
                title: '审批实例id',
                width: '18%',
                align: 'left',
                visible:false
            },{
                field: 'u8glid',
                title: 'U8关联id',
                width: '20%',
                align: 'left',
                visible:false
            },{
                field: 'bxlxmc',
                title: '报销类型',
                width: '14%',
                align: 'left',
                visible:true
            },{
                field: 'zy',
                title: '摘要',
                width: '18%',
                align: 'left',
                visible: true
            },{
                field: 'ssgs',
                title: '所属公司',
                width: '18%',
                align: 'left',
                visible: true
            },{
                field: 'sprmc',
                title: '审批人',
                width: '14%',
                align: 'left',
                visible:true,
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '11%',
                align: 'left',
                visible:true,
            },{
                field: 'zje',
                title: '总金额',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'bmmc',
                title: '部门',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'pzbh',
                title: '凭证编号',
                width: '20%',
                align: 'left',
                visible:true,
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '20%',
                align: 'left',
                visible:true,
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible:true,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                //todo
                ddExpense_DealById(row.ddbxid,'view',$("#ddExpense_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#ddExpense_formSearch #ddExpense_list").colResizable({
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
            sortLastName: "spslid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getddExpenseSearchData(map);
    };
    return oTableInit;
}


function getddExpenseSearchData(map){
    var ddExpense_select=$("#ddExpense_formSearch #ddExpense_select").val();
    var ddExpense_input=$.trim(jQuery('#ddExpense_formSearch #ddExpense_input').val());
    if(ddExpense_select=="0"){
        map["ssgs"]=ddExpense_input;
    }
    else if(ddExpense_select=="1"){
        map["sprmc"]=ddExpense_input;
    }
    else if(ddExpense_select=="2"){
        map["sqrmc"]=ddExpense_input;
    }
    else if(ddExpense_select=="3"){
        map["bmmc"]=ddExpense_input;
    }
    else if(ddExpense_select=="4"){
        map["zy"]=ddExpense_input;
    }
    else if(ddExpense_select=="5"){
        map["lrsj"]=ddExpense_input;
    }
    //提醒类别
    var bxlxs= jQuery('#ddExpense_formSearch #bxlx_id_tj').val();
    map["bxlxs"] = bxlxs.replace(/'/g, "");
    return map;
}



function searchddExpenseResult(isTurnBack){
    if(isTurnBack){
        $('#ddExpense_formSearch #ddExpense_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#ddExpense_formSearch #ddExpense_list').bootstrapTable('refresh');
    }
}
function ddExpense_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#ddExpense_formSearch #urlPrefix").val() + tourl;
    if(action=="view"){
        //todo
        var url=tourl+"?ddbxid="+id
        $.showDialog(url,'查看消息详细信息',viewddExpenseConfig);
    }
}

var viewddExpenseConfig = {
    width		: "1500px",
    modalName	:"viewddExpenseModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var ddExpense_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#ddExpense_formSearch #btn_query");
        var btn_view = $("#ddExpense_formSearch #btn_view");
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchddExpenseResult(true);
            });
        }
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#ddExpense_formSearch #ddExpense_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                ddExpense_DealById(sel_row[0].ddbxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------------------------------------*/
        $("#ddExpense_formSearch #sl_searchMore").on("click",function(ev){
            var ev=ev||event;
            if(expense_turnOff){
                $("#ddExpense_formSearch #searchMore").slideDown("low");
                expense_turnOff = false;
                this.innerHTML="基本筛选";
            }else{
                $("#ddExpense_formSearch #searchMore").slideUp("low");
                expense_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}


$(function(){
    var oTable = new ddExpense_TableInit();
    oTable.Init();

    var oButton = new ddExpense_oButton();
    oButton.Init();

    jQuery('#ddExpense_formSearch .chosen-select').chosen({width: '100%'});

    $("#ddExpense_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
})