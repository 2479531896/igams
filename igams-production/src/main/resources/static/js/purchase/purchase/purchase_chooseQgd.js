var Purchase_turnOff=true;

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
        field: 'qgmxid',
        title: '请购明细ID',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: false
    },{
        field: 'djh',
        title: '请购单号',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'sqrmc',
        title: '申请人',
        width: '20%',
        align: 'left',
        visible:true
    },{
        field: 'wlmc_t',
        title: '设备名称',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'gg_t',
        title: '规格',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'jldw_t',
        title: '单位',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    }]
var ChoosePurchase_TableInit=function(){
    var oTableInit=new Object();
    var url='/purchase/purchase/pagedataChooseListPurchaseMx';
    oTableInit.Init=function(){
        $("#chooseQgdForm #purchase_list").bootstrapTable({
            url: $("#chooseQgdForm input[name='urlPrefix']").val() +url,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseQgdForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qggl.lrsj",				//排序字段
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
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
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
            sortLastName: "qgmx.qgmxid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            gdzcflag: "0", // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return purchaseSearchData(map);
    }
    return oTableInit
}
function purchaseSearchData(map){
    var djh=$.trim(jQuery('#chooseQgdForm #djh').val());
        map["djh"]=djh;
    var sbmc=$.trim(jQuery('#chooseQgdForm #sbmc').val());
    map["sbmc"]=sbmc;
    //请购类别
    var qglbs = jQuery('#chooseQgdForm #qglb_id_tj').val();
    map["qglbs"] = qglbs;
    return map;
}
function searchPurchaseDetatilsResult(isTurnBack){
    //关闭高级搜索条件
    $("#chooseQgdForm #searchMore").slideUp("low");
    Purchase_turnOff=true;
    $("#chooseQgdForm #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#chooseQgdForm #purchase_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#chooseQgdForm #purchase_list').bootstrapTable('refresh');
    }
}
/**
 * 初始化页面
 */
var ChoosePurchase_ButtonInit= function (){
    var oInit=new Object();
    oInit.Init=function(){
        var btn_query=$("#chooseQgdForm #btn_query");
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPurchaseDetatilsResult(true);
            });
        }
        /*-----------------------显示隐藏------------------------------------*/
        $("#chooseQgdForm #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Purchase_turnOff){
                $("#chooseQgdForm #searchMore").slideDown("low");
                Purchase_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#chooseQgdForm #searchMore").slideUp("low");
                Purchase_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oInit;
}
$(function(){
    // 1.初始化Table
    var oTable = new ChoosePurchase_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ChoosePurchase_ButtonInit();
    oButtonInit.Init();
    jQuery('#chooseQgdForm .chosen-select').chosen({width: '100%'});
})