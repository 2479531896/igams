var Wzlj_turnOff=true;

var Wzlj_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#wzlj_formSearch #wzlj_List').bootstrapTable({
            url: '/wzjb/wzjb/pageGetListWzlj',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#wzlj_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "bgrq DESC,hzxm DESC,ybbh",				//排序字段
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
            uniqueId: "ljid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true,
                width: '4%'
            },{
                field: 'ljid',
                title: '拦截ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:false
            },{
                field: 'sjid',
                title: '送检ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:false
            },{
                field: 'wzid',
                title: '物种编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'wzzwm',
                title: '物种中文名',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'wzywm',
                title: '物种英文名',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sflj',
                title: '是否拦截',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sjdw',
                title: '医院名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jglx',
                title: '结果类型',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'bgrq',
                title: '结果日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'db',
                title: '合作伙伴',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                WzljDealById(row.ljid,'view',$("#wzlj_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#wzlj_formSearch #wzlj_List").colResizable({
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
            sortLastName: "ljid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getWzljSearchData(map);
    };
    return oTableInit;
}

function getWzljSearchData(map){
    var cxtj=$("#wzlj_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#wzlj_formSearch #cxnr').val());
    var sflj=$.trim(jQuery('#wzlj_formSearch #sflj').val());
    if(cxtj=="0"){
        map["wzzwm"]=cxnr
    }else if(cxtj=="1"){
        map["wzywm"]=cxnr
    }else if(cxtj=="2"){
        map["wzid"]=cxnr
    }else if(cxtj=="3"){
        map["sjdw"]=cxnr
    }else if(cxtj=="4"){
        map["jglx"]=cxnr
    }else if(cxtj=="5"){
        map["hzxm"]=cxnr
    }

    if(sflj=="0"){
        map["sflj"]="0"
    }else if(sflj=="1"){
        map["sflj"]="1"
    }

    var bgrqstart = jQuery('#wzlj_formSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    var bgrqend = jQuery('#wzlj_formSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;
    return map;
}

// 按钮动作函数
function WzljDealById(ljid,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?ljid="+ljid;
        $.showDialog(url,'查看',viewWzljConfig);
    }
}

function toViewInspection(){
    var url="/inspection/inspection/viewSjxx?sjid="+ $('#id').val()+"&ks="+ $('#ks').val()+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewInspectionConfig);

}
/*查看送检信息模态框*/
var viewInspectionConfig = {
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


var Wzlj_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#wzlj_formSearch #btn_view");//详情
        var btn_query =$("#wzlj_formSearch #btn_query");//模糊查询

        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchWzljResult(true);
            });
        };

        //添加日期控件
        laydate.render({
            elem: '#bgrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqend'
            ,theme: '#2381E9'
        });
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#wzlj_formSearch #wzlj_List').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                WzljDealById(sel_row[0].ljid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /**显示隐藏**/
        $("#wzlj_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Wzlj_turnOff){
                $("#wzlj_formSearch #searchMore").slideDown("low");
                Wzlj_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#wzlj_formSearch #searchMore").slideUp("low");
                Wzlj_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};

/*查看详情信息模态框*/
var viewWzljConfig = {
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

function searchWzljResult(isTurnBack){
    //关闭高级搜索条件
    $("#wzlj_formSearch #searchMore").slideUp("low");
    Wzlj_turnOff=true;
    $("#wzlj_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#wzlj_formSearch #wzlj_List').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#wzlj_formSearch #wzlj_List').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Wzlj_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Wzlj_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#wzlj_formSearch .chosen-select').chosen({width: '100%'});
    $("#wzlj_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
