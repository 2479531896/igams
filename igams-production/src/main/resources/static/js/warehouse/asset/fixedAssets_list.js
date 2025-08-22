var fixedAssets_turnOff=true;
var fixedAssets_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#fixedAssets_formSearch #fixedAssets_list").bootstrapTable({
            url: $("#fixedAssets_formSearch #urlPrefix").val()+'/asset/asset/pageGetListFixedAssets',
            method: 'get',                      // 请求方式（*）
            toolbar: '#fixedAssets_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "lrsj",				// 排序字段
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
            uniqueId: "hwid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'u8rkdh',
                title: 'U8入库单号',
                titleTooltip:'U8入库单号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'htmxid',
                title: '发票详情',
                titleTooltip:'发票详情',
                width: '8%',
                align: 'left',
                formatter: fphformat,
                sortable: true,
                visible:true
            },{
                field: 'wlbm',
                title: '设备编号',
                titleTooltip:'设备编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'wlmc',
                title: '固定资产名称',
                titleTooltip:'固定资产名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'gg',
                title: '规格型号',
                titleTooltip:'规格型号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '6%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'lbmc',
                title: '类别名称',
                titleTooltip:'类别名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'bmmc',
                title: '使用部门',
                titleTooltip:'使用部门',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sydd',
                title: '使用地点',
                titleTooltip:'使用地点',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'gdzcbh',
                title: '固定资产编号',
                titleTooltip:'固定资产编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sbccbh',
                title: '设备出厂编号',
                titleTooltip:'设备出厂编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'kpbh',
                title: '卡片编号',
                titleTooltip:'卡片编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'syzkmc',
                title: '使用情况',
                titleTooltip:'使用情况',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'synx',
                title: '使用年限(月)',
                titleTooltip:'使用年限(月)',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'lrrymc',
                title: '录入人员',
                titleTooltip:'录入人员',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'lrsj',
                title: '录入时间',
                titleTooltip:'录入时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                fixedAssets_DealById(row.hwid,'view',$("#fixedAssets_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#fixedAssets_formSearch #fixedAssets_list").colResizable({
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
            sortLastName: "hwxx.hwid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getFixedAssetsSearchData(map);
    };
    return oTableInit;
}


function getFixedAssetsSearchData(map){
    var fixedAssets_select=$("#fixedAssets_formSearch #fixedAssets_select").val();
    var fixedAssets_input=$.trim(jQuery('#fixedAssets_formSearch #fixedAssets_input').val());
    if(fixedAssets_select=="0"){//全部
        map["entire"]=fixedAssets_input
    }else if(fixedAssets_select=="2"){
        map["wlbm"]=fixedAssets_input
    }else if(fixedAssets_select=="3"){
        map["gdzcbh"]=fixedAssets_input
    }else if(fixedAssets_select=="4"){
        map["gg"]=fixedAssets_input
    }else if(fixedAssets_select=="5"){
        map["bmmc"]=fixedAssets_input
    }else if(fixedAssets_select=="6"){
        map["sydd"]=fixedAssets_input
    }else if(fixedAssets_select=="7"){
        map["sbccbh"]=fixedAssets_input
    }else if(fixedAssets_select=="8"){
        map["kpbh"]=fixedAssets_input
    }else if(fixedAssets_select=="9"){
        map["htnbbh"]=fixedAssets_input
    }else if(fixedAssets_select=="10"){
        map["djh"]=fixedAssets_input
    }else if(fixedAssets_select=="11"){
        map["dhdh"]=fixedAssets_input
    }else if(fixedAssets_select=="12"){
        map["u8rkdh"]=fixedAssets_input
    }else if(fixedAssets_select=="13"){
        map["scph"]=fixedAssets_input
    }else if(fixedAssets_select=="14"){
        map["zsh"]=fixedAssets_input
    }else if(fixedAssets_select=="15"){
        map["wlmc"]=fixedAssets_input
    }

    // 类别
    var lbs = jQuery('#fixedAssets_formSearch #lb_id_tj').val();
    map["lbs"] = lbs;
    // 使用状况
    var syzks = jQuery('#fixedAssets_formSearch #syzk_id_tj').val();
    map["syzks"] = syzks;
    // 增加方式
    var zjfss = jQuery('#fixedAssets_formSearch #zjfs_id_tj').val();
    map["zjfss"] = zjfss;
    // 折旧方法
    var zjffs = jQuery('#fixedAssets_formSearch #zjffs_id_tj').val();
    map["zjffs"] = zjffs;
    // 录入开始日期
    var lrsjstart = jQuery('#fixedAssets_formSearch #lrsjstart').val();
    map["lrsjstart"] = lrsjstart;

    // 录入结束日期
    var lrsjend = jQuery('#fixedAssets_formSearch #lrsjend').val();
    map["lrsjend"] = lrsjend;
    // 开始使用开始日期
    var kssyrqstart = jQuery('#fixedAssets_formSearch #kssyrqstart').val();
    map["kssyrqstart"] = kssyrqstart;

    // 开始使用结束日期
    var kssyrqend = jQuery('#fixedAssets_formSearch #kssyrqend').val();
    map["kssyrqend"] = kssyrqend;

    return map;
}

/**
 * 发票号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fphformat(value,row,index){
    var html="";
    if(row.htmxid!=null&&row.htmxid!='') {
        html += "<span class='col-md-12 col-sm-12'>";
        html += "<a href='javascript:void(0);' onclick=\"queryByHtmxid('" + row.htmxid + "')\">发票详情</a>";
        html += "</span>";
    }
    return html;
}

function queryByHtmxid(htmxid){
    var url=$("#fixedAssets_formSearch #urlPrefix").val()+"/invoice/invoice/pagedataGetListByHtmxid?htmxid="+htmxid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'发票详情',viewFpConfig);
}
var viewFpConfig = {
    width		: "1400px",
    modalName	:"viewInvoiceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};



function searchFixedAssetsResult(isTurnBack){
    //关闭高级搜索条件
    $("#fixedAssets_formSearch #searchMore").slideUp("low");
    fixedAssets_turnOff=true;
    $("#fixedAssets_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#fixedAssets_formSearch #fixedAssets_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#fixedAssets_formSearch #fixedAssets_list').bootstrapTable('refresh');
    }
}

function fixedAssets_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#fixedAssets_formSearch #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?hwid="+id;
        $.showDialog(url,'查看详细信息',viewFixedAssetConfig);
    }else if(action=="assetuphold"){
        var url=tourl+"?hwid="+id;
        $.showDialog(url,'固定资产维护',upholdFixedAssetConfig);
    }
}
var viewFixedAssetConfig = {
    width		: "1650px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var upholdFixedAssetConfig = {
    width		: "1650px",
    modalName	: "upholdFixedAssetModel",
    formName	: "upholdAssetForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#upholdAssetForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#upholdAssetForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"upholdAssetForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }

                            // searchDeviceCheckResult();
                            //提交审核
                            // if(responseText["auditType"]!=null){
                            //     var deviceCheck_params=[];
                            //     deviceCheck_params.prefix=responseText["urlPrefix"];
                            //     showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                            //         $.closeModal(opts.modalName);
                            //         searchDeviceCheckResult();
                            //     },null,deviceCheck_params);
                            // }else{
                            //     searchDeviceCheckResult();
                            // }
                            searchFixedAssetsResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var fixedAssets_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#fixedAssets_formSearch #btn_query");
        var btn_view=$("#fixedAssets_formSearch #btn_view");
        var btn_assetuphold=$("#fixedAssets_formSearch #btn_assetuphold");
        var btn_del=$("#fixedAssets_formSearch #btn_del");
        var btn_searchexport = $("#fixedAssets_formSearch #btn_searchexport");
        var btn_selectexport = $("#fixedAssets_formSearch #btn_selectexport");

        //添加日期控件
        laydate.render({
            elem: ' #fixedAssets_formSearch #lrsjstart'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        //添加日期控件
        laydate.render({
            elem: ' #fixedAssets_formSearch #lrsjend'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        //添加日期控件
        laydate.render({
            elem: ' #fixedAssets_formSearch #kssyrqstart'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: ' #fixedAssets_formSearch #kssyrqend'
            , theme: '#2381E9'
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchFixedAssetsResult(true);
            });
        }
        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#fixedAssets_formSearch #fixedAssets_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                fixedAssets_DealById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------资产维护--------------------------------*/
        btn_assetuphold.unbind("click").click(function(){
            var sel_row=$('#fixedAssets_formSearch #fixedAssets_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].gdzcid!=null&&sel_row[0].gdzcid!=''){
                    $.error("此条数据已被资产维护，无法重复维护！");
                    return;
                }else{
                    fixedAssets_DealById(sel_row[0].hwid,"assetuphold",btn_assetuphold.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#fixedAssets_formSearch #fixedAssets_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(sel_row[i].gdzcid!=null&&sel_row[i].gdzcid!=''){
                    ids= ids + ","+ sel_row[i].gdzcid;
                }
            }
            if(ids==''){
                $.error("您所选择的数据都未维护，无法删除！");
                return;
            }else{
                ids=ids.substr(1);
            }
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#fixedAssets_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchFixedAssetsResult();
                                });
                            }else if(responseText["status"] == "fail"){
                                $.error(responseText["message"],function() {
                                });
                            } else{
                                $.alert(responseText["message"],function() {
                                });
                            }
                        },1);

                    },'json');
                    jQuery.ajaxSetup({async:true});
                }
            });
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#fixedAssets_formSearch #fixedAssets_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].hwid;
                }
                ids = ids.substr(1);
                $.showDialog($('#fixedAssets_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ASSET_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#fixedAssets_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ASSET_SEARCH&expType=search&callbackJs=AssetSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

        /**显示隐藏**/
        $("#fixedAssets_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(fixedAssets_turnOff){
                $("#fixedAssets_formSearch #searchMore").slideDown("low");
                fixedAssets_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#fixedAssets_formSearch #searchMore").slideUp("low");
                fixedAssets_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oButtonInit;
}

//提供给导出用的回调函数
function AssetSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="gdzcid";
    map["sortLastOrder"]="desc";
    map["sortName"]="lrsj";
    map["sortOrder"]="desc";
    return invoiceSearchData(map);
}

$(function(){
    var oTable = new fixedAssets_TableInit();
    oTable.Init();

    var oButton = new fixedAssets_oButton();
    oButton.Init();

    jQuery('#fixedAssets_formSearch .chosen-select').chosen({width: '100%'});
})