var record_turnOff=true;
var record_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#record_formSearch #record_list").bootstrapTable({
            url: '/record/record/pageGetListRecord',
            method: 'get',                      // 请求方式（*）
            toolbar: '#record_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "createtime",				// 排序字段
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
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "processinstanceid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'title',
                title: '标题',
                titleTooltip:'标题',
                width: '12%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'originatordeptname',
                title: '部门',
                titleTooltip:'部门',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'zsxm',
                title: '申请人',
                titleTooltip:'申请人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'splx',
                title: '审批类型',
                titleTooltip:'审批类型',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'createtime',
                title: '创建时间',
                titleTooltip:'创建日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'finishtime',
                title: '完成时间',
                titleTooltip:'完成时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'result',
                title: '审批结果',
                titleTooltip:'审批结果',
                width: '6%',
                align: 'left',
                formatter:resultformat,
                sortable: true,
                visible:true
            },{
                field: 'status',
                title: '状态',
                titleTooltip:'状态',
                width: '8%',
                align: 'left',
                formatter:statusformat,
                sortable: true,
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                record_DealById(row.processinstanceid,'view',$("#record_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#record_formSearch #record_list").colResizable({
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
            sortLastName: "processinstanceid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getRecordSearchData(map);
    };
    return oTableInit;
}


function getRecordSearchData(map){
    var record_select=$("#record_formSearch #record_select").val();
    var records_input=$.trim(jQuery('#record_formSearch #records_input').val());
    if(record_select=="0"){//全部
        map["entire"]=records_input
    }else if(record_select=="1"){
        map["originatordeptname"]=records_input
    }else if(record_select=="2"){
        map["zsxm"]=records_input
    }else if(record_select=="3"){
        map["title"]=records_input
    }
    var splxs = jQuery('#record_formSearch #splx_id_tj').val();
    map["splxs"] = splxs;

    var createStart = jQuery('#record_formSearch #createStart').val();
    map["createStart"] = createStart;

    var createEnd = jQuery('#record_formSearch #createEnd').val();
    map["createEnd"] = createEnd;

    var finishStart = jQuery('#record_formSearch #finishStart').val();
    map["finishStart"] = finishStart;

    var finishEnd = jQuery('#record_formSearch #finishEnd').val();
    map["finishEnd"] = finishEnd;
    return map;
}

function resultformat(value,row,index){
    var html = "";
    if(row.result=='agree'){
        html="<span style='color:green;'>"+"同意"+"</span>";
    }else if(row.result=='refuse'){
        html="<span style='color:red;'>"+"拒绝"+"</span>";
    }
    return html;
}
function statusformat(value,row,index){
    var html = "";
    if(row.status=='COMPLETED'){
        html="<span style='color:green;'>"+"审批完成"+"</span>";
    }else if(row.status=='TERMINATED'){
        html="<span style='color:red;'>"+"已撤销"+"</span>";
    }else if(row.status=='RUNNING'){
        html="<span style='color:warning;'>"+"审批中"+"</span>";
     }
    return html;
}



function searchRecordResult(isTurnBack){
    //关闭高级搜索条件
    $("#record_formSearch #searchMore").slideUp("low");
    record_turnOff=true;
    $("#record_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#record_formSearch #record_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#record_formSearch #record_list').bootstrapTable('refresh');
    }
}

function record_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?processinstanceid="+id;
        $.showDialog(url,'查看详细信息',viewRecordConfig);
    }
}
var viewRecordConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var record_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#record_formSearch #btn_query");
        var btn_view=$("#record_formSearch #btn_view");
        laydate.render({
            elem: ' #record_formSearch #createStart'
            , theme: '#2381E9'
        });
        laydate.render({
            elem: ' #record_formSearch #createEnd'
            , theme: '#2381E9'
        });
        laydate.render({
            elem: ' #record_formSearch #finishStart'
            , theme: '#2381E9'
        });
        laydate.render({
            elem: ' #record_formSearch #finishEnd'
            , theme: '#2381E9'
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchRecordResult(true);
            });
        }
        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#record_formSearch #record_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                record_DealById(sel_row[0].processinstanceid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /**显示隐藏**/
        $("#record_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(record_turnOff){
                $("#record_formSearch #searchMore").slideDown("low");
                record_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#record_formSearch #searchMore").slideUp("low");
                record_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oButtonInit;
}


$(function(){
    var oTable = new record_TableInit();
    oTable.Init();

    var oButton = new record_oButton();
    oButton.Init();

    jQuery('#record_formSearch .chosen-select').chosen({width: '100%'});
})