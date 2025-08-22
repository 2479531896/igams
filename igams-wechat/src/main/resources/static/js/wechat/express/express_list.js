var Express_turnOff=true;

var Express_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#express_formSearch #express_List').bootstrapTable({
            url: '/express/express/pageGetListExpress',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#express_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "starttime DESC, endtime",				//排序字段
            sortOrder: "DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
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
            uniqueId: "sjkdid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true,
                width: '4%'
            },{
                field: 'sjkdid',
                title: '送检快递ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'mailno',
                title: '快递单号',
                width: '25%',
                align: 'left',
                visible:true
            },{
                field: 'starttime',
                title: '发出时间',
                width: '25%',
                align: 'left',
                visible:true
            },{
                field: 'endtime',
                title: '送达时间',
                width: '25%',
                align: 'left',
                visible:true
            },{
                field: 'kdzt',
                title: '快递状态',
                width: '25%',
                align: 'left',
                formatter:kdztFormat,
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                ExpressDealById(row.mailno,'view',$("#express_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#express_formSearch #express_List").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjkdid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getExpressSearchData(map);
    };
    return oTableInit;
}

function getExpressSearchData(map){
    var cxtj=$("#express_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#express_formSearch #cxnr').val());
    var kdzt=$.trim(jQuery('#express_formSearch #kdzt').val());
    if(cxtj=="0"){
        map["mailno"]=cxnr
    }


    if(kdzt=="0"){
        map["kdzt"]="0"
    }else if(kdzt=="1"){
        map["kdzt"]="1"
    }

    // 快递发出日期
    var kdfcstart = jQuery('#express_formSearch #kdfcstart').val();
    map["kdfcstart"] = kdfcstart;
    var kdfcend = jQuery('#express_formSearch #kdfcend').val();
    map["kdfcend"] = kdfcend;
    // 快递接收日期
    var kdjsstart = jQuery('#express_formSearch #kdjsstart').val();
    map["kdjsstart"] = kdjsstart;
    var kdjsend = jQuery('#express_formSearch #kdjsend').val();
    map["kdjsend"] = kdjsend;
    return map;
}

// 按钮动作函数
function ExpressDealById(mailno,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?mailno="+mailno;
        $.showDialog(url,'物流信息',viewExpressConfig);
    }
}


function kdztFormat(value,row,index){
    if(row.kdzt=='未签收'||row.kdzt==null){
        var kdzt="<span style='color:red;'>未签收</span>";
        return kdzt;
    }else if(row.kdzt=='已签收'){
        var kdzt="<span style='color:green;'>已签收</span>";
        return kdzt;
    }
}
var Express_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#express_formSearch #btn_view");//详情
        var btn_query =$("#express_formSearch #btn_query");//模糊查询

        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchExpressResult(true);
            });
        };

        //添加日期控件
        laydate.render({
            elem: '#kdfcstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#kdfcend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#kdjsstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#kdjsend'
            ,theme: '#2381E9'
        });
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#express_formSearch #express_List').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                ExpressDealById(sel_row[0].mailno,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /**显示隐藏**/
        $("#express_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Express_turnOff){
                $("#express_formSearch #searchMore").slideDown("low");
                Express_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#express_formSearch #searchMore").slideUp("low");
                Express_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};

/*查看详情信息模态框*/
var viewExpressConfig = {
    width		: "800px",
    height		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function searchExpressResult(isTurnBack){
    //关闭高级搜索条件
    $("#express_formSearch #searchMore").slideUp("low");
    Express_turnOff=true;
    $("#express_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#express_formSearch #express_List').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#express_formSearch #express_list').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Express_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Express_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#express_formSearch .chosen-select').chosen({width: '100%'});
    $("#express_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
