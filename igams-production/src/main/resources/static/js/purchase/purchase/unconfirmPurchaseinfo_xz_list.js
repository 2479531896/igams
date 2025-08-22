var unconfirmPurchase_xz_turnOff=true;

var UnconfirmPurchase_xz_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#unconfirminfo_xz_formSearch #xzwqr_list").bootstrapTable({
            url: $("#unconfirminfo_xz_formSearch #urlPrefix").val()+'/administration/purchase/pageGetListUnconfirmPurchaseAdministration',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#unconfirminfo_xz_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qgmx.hwmc",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width:'4%'
            },{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'hwmc',
                title: '货物名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hwbz',
                title: '货物规格',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sl',
                title: '货物数量',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hwjldw',
                title: '货物单位',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qgdh',
                title: '请购单号',
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
                if(!$("#unconfirminfo_xz_formSearch #flag").val()){
                    UnconfirmPurchaseAdministrationDealById(row.qgmxid,'view',$("#unconfirminfo_xz_formSearch #btn_view").attr("tourl"));
                }
            },
        });
        $("#unconfirminfo_xz_formSearch #xzwqr_list").colResizable({
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
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "qggl.sqrq", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return UnconfirmPurchase_XzSearchData(map);
    }
    return oTableInit
}

function UnconfirmPurchase_XzSearchData(map){
    var cxtj=$("#unconfirminfo_xz_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#unconfirminfo_xz_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["djh"]=cxnr
    }else if(cxtj=="2"){
        map["hwmc"]=cxnr
    }else if(cxtj=="3"){
        map["sqbmmc"]=cxnr
    }
    map["qrc_flag"]="1";
    return map;
}

/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchUnconfirmPurchase_xzResult(isTurnBack){
    //关闭高级搜索条件
    $("#unconfirminfo_xz_formSearch #searchMore").slideUp("low");
    unconfirmunconfirmPurchase_xz_turnOff=true;
    $("#unconfirminfo_xz_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#unconfirminfo_xz_formSearch #xzwqr_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#unconfirminfo_xz_formSearch #xzwqr_list').bootstrapTable('refresh');
    }
}

var UnconfirmPurchase_xz_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#unconfirminfo_xz_formSearch #btn_query");

        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchUnconfirmPurchase_xzResult(true);
            });
        }
    }
    return oInit;
}

$(function(){
    // 1.初始化Table
    var oTable = new UnconfirmPurchase_xz_TableInit();
    oTable.Init();
    var oButtonInit = new UnconfirmPurchase_xz_ButtonInit();
    oButtonInit.Init();
    jQuery('#unconfirminfo_xz_formSearch .chosen-select').chosen({width: '100%'});
})