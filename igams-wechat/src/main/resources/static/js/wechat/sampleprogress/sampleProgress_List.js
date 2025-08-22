var sampleProgress_turnOff=true;
var SampleProgress_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#sampleProgress_formSearch #sampleProgress_list").bootstrapTable({
            url: '/sampleprogress/sampleprogress/pageGetListSampleProgress',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampleProgress_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sj.dsyrq is not null,sj.syrq is not null,wksjgl.sjsj is not null,wksjgl.xjsj is not null,sj.bgrq is not null,sj.jsrq ",				//排序字段
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
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            /*rowStyle:rowStyle,                  //通过自定义函数设置行样式
*/          columns: [{
                checkbox: true,
                width: '4%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '7%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jsrq',
                title: '接收时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dsyrq',
                title: 'D实验日期',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'syrq',
                title: 'R实验日期',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sjsj',
                title: '上机时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xjsj',
                title: '下机时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bgrq',
                title: '报告日期',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'hospitalname',
                title: '医院名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'db',
                title: '合作伙伴',
                width: '6%',
                sortable: true,
                align: 'left',
                visible: false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getSampleProgressSearchData(map);
    };

    return oTableInit;
}

function getSampleProgressSearchData(map){
    var cxtj=$("#sampleProgress_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#sampleProgress_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["ybbh"]=cxnr
    }else if(cxtj=="1"){
        map["hzxm"]=cxnr
    }else if(cxtj=="2"){
        map["nbbm"]=cxnr
    }else if(cxtj=="3"){
        map["sjdwmc"]=cxnr
    }else if(cxtj=="4"){
        map["db"]=cxnr
    }

    //标本类型
    var yblxs = jQuery('#sampleProgress_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;

    var jcdws=$("#sampleProgress_formSearch #jcdw_id_tj").val();
    map["jcdws"]=jcdws

    var jcxm=jQuery('#sampleProgress_formSearch #jcxm_id_tj').val()
    map["jcxms"]=jcxm
    return map;
}

var SampleProgress_oButtton= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query = $("#sampleProgress_formSearch #btn_query");

        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchSampleProgressResult(true);
            });
        }
        /**显示隐藏**/
        $("#sampleProgress_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(sampleProgress_turnOff){
                $("#sampleProgress_formSearch #searchMore").slideDown("low");
                sampleProgress_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#sampleProgress_formSearch #searchMore").slideUp("low");
                sampleProgress_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};

/**
 * 刷新bootstraptable
 */
function searchSampleProgressResult(isTurnBack){
    //关闭高级搜索条件
    $("#sampleProgress_formSearch #searchMore").slideUp("low");
    sampleProgress_turnOff=true;
    $("#sampleProgress_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#sampleProgress_formSearch #sampleProgress_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sampleProgress_formSearch #sampleProgress_list').bootstrapTable('refresh');
    }
}
$(function(){
    var oTable=new SampleProgress_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new SampleProgress_oButtton();
    oButtonInit.Init();
    jQuery('#sampleProgress_formSearch .chosen-select').chosen({width: '100%'});
    // 初始绑定显示更多的事件
    $("#sampleProgress_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});