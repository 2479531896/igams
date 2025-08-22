var rece_Produce_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#rece_Produce #rece_Produce_tb_list').bootstrapTable({
            url: $('#rece_Produce #urlPrefix').val() + '/storehouse/requisition/pagedataGetProduce',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#rece_Produce #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "sczlgl.zlrq",				//排序字段
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
            uniqueId: "sczlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'zldh',
                title: '指令单号',
                titleTooltip:'指令单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'cpbh',
                title: '产品编号',
                titleTooltip:'产品编号',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'jhcl',
                title: '计划产量',
                titleTooltip:'计划产量',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'xlh',
                title: '序列号',
                titleTooltip:'序列号',
                width: '12%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
                var sczlid = "";
                var sel_row = $('#rece_Produce #rece_Produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length>0){
                    var  flag = true;
                    for (var i = 0; i <sel_row.length ; i++) {
                        if (sel_row[i].sczlmxid==row.sczlmxid){
                            sczlid = sel_row[i].sczlid;
                            sel_row.splice(i,1);
                            flag = false;
                        }
                    }
                    if (flag){
                        sel_row.push(row);
                    }
                }else {
                    sel_row.push(row);
                }
                var yxzldh = "";
                if(sel_row.length>0){
                    sczlid = sel_row[0].sczlid;
                    yxzldh = sel_row[0].zldh;
                    var zldh = sel_row[0].zldh;
                    for (var i = 0; i <sel_row.length ; i++) {
                        if (zldh!=sel_row[i].zldh){
                            $.error("只允许选择一个指令单")
                            return;
                        }
                    }
                }
                $("#rece_Produce #yxzldh").tagsinput('remove',{"value":sczlid,"text":yxzldh});
                if (yxzldh.length>0){
                    $("#rece_Produce  #yxzldh").tagsinput('add',{"value":sczlid,"text":yxzldh});
                }
            },
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "sczlgl.sczlid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return getReceProduceSearchData(map);
    };
    return oTableInit;
};

function searchReceProduceResult(isTurnBack){
    if(isTurnBack){
        $('#rece_Produce #rece_Produce_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#rece_Produce #rece_Produce_tb_list').bootstrapTable('refresh');
    }
}

function getReceProduceSearchData(map){
    var cxtj = $("#rece_Produce #cxtj").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#rece_Produce #cxnr').val());
    // '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
    if (cxtj == "0") {
        map["entire"] = cxnr;
    }else if (cxtj == "1") {
        map["zldh"] = cxnr;
    }else if (cxtj == "2") {
        map["wlbm"] = cxnr;
    }else if (cxtj == "3") {
        map["wlmc"] = cxnr;
    }

    return map;
}


var rece_Produce_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query = $("#rece_Produce #btn_query");

        initTagsinput();
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchReceProduceResult(true);
            });
        }
    };

    return oInit;
};
/**
 * 初始化需求单号
 * @returns
 */
function initTagsinput(){
    $("#rece_Produce  #yxzldh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
}
$(function(){
    //1.初始化Table
    var oTable = new rece_Produce_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new rece_Produce_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#rece_Produce .chosen-select').chosen({width: '100%'});

});
