var produce_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#produceAudit_audit_formSearch #produce_auditing_list").bootstrapTable({
            url: $("#produceAudit_formAudit #urlPrefix").val()+'/progress/produce/pageGetListProduceAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#produceAudit_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sczlmx.lrsj",				//排序字段
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
            uniqueId: "sczlmx.sczlmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'sczlmxid',
                title: '生产指令明细id',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'zldh',
                title: '指令单号',
                titleTooltip:'指令单号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },
            {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                formatter:xqdhFormat,
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true

            },
            {
                field: 'zlrq',
                title: '指令日期',
                sortable: true,
                titleTooltip:'指令日期',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xlh',
                title: '序列号',
                titleTooltip:'序列号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'cpbh',
                title: '产品编号',
                titleTooltip:'产品编号',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: true

            }, {
                field: 'jhcl',
                title: '计划产量',
                titleTooltip:'计划产量',
                width: '6%',
                align: 'left',
                visible: false
            }, {
                field: 'scsl',
                title: '生产数量',
                titleTooltip:'生产数量',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'cplxmc',
                title: '产品类型',
                titleTooltip:'产品类型',
                width: '6%',
                align: 'left',
                visible: true
            },{
                    field: 'shxx_shyj',
                    title: '审核意见',
                    width: '15%',
                    align: 'left',
                    visible:true,
                }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                produce_audit_DealById(row.sczlmxid,'view',$("#produceAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#produceAudit_audit_formSearch #produce_auditing_list").colResizable({
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
            sortLastName: "sczlmx.sczlmxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
//		return getMaterialAuditSearchData(map);
        var cxtj=$("#produceAudit_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#produceAudit_audit_formSearch #cxnr').val());
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["zldh"] = cxnr;
        }else if (cxtj == "2") {
            map["cpbh"] = cxnr;
        }else if (cxtj == "3") {
            map["xlh"] = cxnr;
        }else if (cxtj == "4") {
            map["wlbm"] = cxnr;
        }else if (cxtj == "5") {
            map["wlmc"] = cxnr;
        }
        map["dqshzt"] = 'dsh';
        // 需求开始日期
        var zlrqstart = jQuery('#produceAudit_audit_formSearch #zlrqstart').val();
        map["zlrqstart"] = zlrqstart;
        // 需求结束日期
        var zlrqend = jQuery('#produceAudit_audit_formSearch #zlrqend').val();
        map["zlrqend"] = zlrqend;

        // 高级筛选
        // 分组
        var cplxs = jQuery('#produceAudit_audit_formSearch #cplx_id_tj').val();
        map["cplxs"] = cplxs;
        return map;
    };
    return oTableInit;
}

function xqdhFormat(value,row,index) {
    if (row.xqdh){
        return "<a href='javascript:void(0);' onclick='queryByXqdh(\"" + row.cpxqid + "\")' >"+row.xqdh+"</a>";
    }else{
        row.xqdh = "-";
        return "<a href='javascript:void(0);'>"+row.xqdh+"</a>";
    }
}

function queryByXqdh(cpxqid){
    var url=$("#produceAudit_formAudit #urlPrefix").val()+"/progress/progress/viewProgress?cpxqid="+cpxqid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'需求计划',viewProduceXqdhConfig);
}
var viewProduceXqdhConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var produce_audited_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#produceAudit_audited_formSearch #produce_audited_list").bootstrapTable({
            url: $("#produceAudit_formAudit #urlPrefix").val()+'/progress/produce/pageGetListProduceAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#produceAudit_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sczlmx.lrsj",				//排序字段
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
            uniqueId: "sczlmx.sczlmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'sczlmxid',
                title: '生产指令明细id',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'zldh',
                title: '指令单号',
                titleTooltip:'指令单号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },
            {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                formatter:xqdhFormat,
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true

            },
            {
                field: 'zlrq',
                title: '指令日期',
                sortable: true,
                titleTooltip:'指令日期',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'cpbh',
                title: '产品编号',
                titleTooltip:'产品编号',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: true

            }, {
                field: 'jhcl',
                title: '计划产量',
                titleTooltip:'计划产量',
                width: '6%',
                align: 'left',
                visible: false
            }, {
                field: 'scsl',
                title: '生产数量',
                titleTooltip:'生产数量',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'cplxmc',
                title: '产品类型',
                titleTooltip:'产品类型',
                width: '6%',
                align: 'left',
                visible: true
            },{field: 'shxx_lrryxm',
                    title: '审核人',
                    width: '10%',
                    align: 'left',
                    visible:true,
                },{
                    field: 'shxx_shyj',
                    title: '审核意见',
                    width: '15%',
                    align: 'left',
                    visible:true,
                },{
                    field: 'shxx_shsj',
                    title: '审核时间',
                    width: '10%',
                    align: 'left',
                    visible:true,
                }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                produce_audit_DealById(row.sczlmxid,'view',$("#produceAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#produceAudit_audited_formSearch #produce_audited_list").colResizable({
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
            sortLastName: "sczlmx.sczlmxid", //防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
//		return getMaterialAuditSearchData(map);
        var cxtj=$("#produceAudit_audited_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#produceAudit_audited_formSearch #cxnr').val());
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["zldh"] = cxnr;
        }else if (cxtj == "2") {
            map["cpbh"] = cxnr;
        }else if (cxtj == "3") {
            map["xlh"] = cxnr;
        }else if (cxtj == "4") {
            map["wlbm"] = cxnr;
        }else if (cxtj == "5") {
            map["wlmc"] = cxnr;
        }
        map["dqshzt"] = 'ysh';
        // 需求开始日期
        var zlrqstart = jQuery('#produceAudit_audited_formSearch #zlrqstart').val();
        map["zlrqstart"] = zlrqstart;
        // 需求结束日期
        var zlrqend = jQuery('#produceAudit_audited_formSearch #zlrqend').val();
        map["zlrqend"] = zlrqend;

        // 高级筛选
        // 分组
        var cplxs = jQuery('#produceAudit_audited_formSearch #cplx_id_tj').val();
        map["cplxs"] = cplxs;
        return map;
    };
    return oTableInit;
}


var viewProduceAuditConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

//待审核列表的按钮路径
function produce_audit_DealById(id,action,tourl,type){
    if(!tourl){
        return;
    }
    tourl = $('#produceAudit_formAudit #urlPrefix').val() + tourl;
    if(action=="view"){
        var url=tourl+"?sczlmxid="+id;
        $.showDialog(url,'查看',viewProduceAuditConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: type,
            url:tourl+"?czbs=1",
            data:{'ywzd':'sczlmxid'},
            title:"生产指令审核",
            preSubmitCheck:'preSubmitputmaterial',
            prefix:$('#produceAudit_formAudit #urlPrefix').val(),
            callback:function(){
                searchProduce_audit_Result(true);//回调
            },
            dialogParam:{width:1200}
        });
    }else if(action =='batchaudit'){
        $.ajax({
            type : "POST",
            url : $('#produceAudit_formAudit #urlPrefix').val()+"/systemcheck/auditProcess/judgment",
            data : {"ywids" : id,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success:function(data){
                if(data.status == "true"){
                    batchAudit({
                        ids :id,
                        type: type,
                        url:tourl+"?ids="+id,
                        data:{'ywzd':'sczlmxid'},
                        title:"批量审核",
                        preSubmitCheck:'preSubmitputmaterial',
                        prefix:$('#produceAudit_formAudit #urlPrefix').val(),
                        callback:function(){
                            searchProduce_audit_Result(true);//回调
                        },
                        dialogParam:{width:1200}
                    });
                }else{
                    $.error(data.message);
                }
            }
        });

    }
}

/**
 * 批量审核
 * @param params
 */
var batchAudit = function(params){
    params = params||{};
    var _dialogParam = $.extend({},params.dialogParam);
    _dialogParam.data = $.extend({"ywids":params.ids,'business_url':params.url,'shlb':params.type},params.data,_dialogParam.data);
    $.showDialog((params?(params.prefix?params.prefix:""):"") + "/systemcheck/auditProcess/batchAuditInfo",params.title||'批量审核', $.extend({},getAuditCallbackConfigAddLoading(params.callback,params.callbackParam,"auditAjaxForm",params.preSubmitCheck,{successBtn:"audit_confirm_btn"}),{"width":"900px"},_dialogParam));
};
function preSubmitputmaterial(){
    return true;
}

var produce_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#produceAudit_audit_formSearch #btn_query");//模糊查询（待审核）
        var btn_queryAudited = $("#produceAudit_audited_formSearch #btn_query");//模糊查询（审核历史）
        var btn_cancelAudit = $("#produceAudit_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
        var btn_view = $("#produceAudit_audit_formSearch #btn_view");//查看页面（待审核）
        var btn_audit = $("#produceAudit_audit_formSearch #btn_audit");//审核（待审核）
        var btn_batchaudit = $("#produceAudit_audit_formSearch #btn_batchaudit");
        //添加日期控件
        laydate.render({
            elem: '#zlrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#zlrqend'
            ,theme: '#2381E9'
        });
        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchProduce_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchProduce_audited_Result(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#produceAudit_audit_formSearch #produce_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                produce_audit_DealById(sel_row[0].sczlmxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#produceAudit_audit_formSearch #produce_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                let type = ""
                if (sel_row.cplxdm != "YQ"){
                    type = "AUDIT_YQ_PRODUCE";
                }else {
                    type = "AUDIT_SJ_PRODUCE";
                }
                produce_audit_DealById(sel_row[0].sczlmxid,"audit",btn_audit.attr("tourl"),type);
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#produceAudit_formAudit #produceAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='produceAudit_auditing'){
                    var oTable= new produce_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new produce_audited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #produce_audited_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var materialAudit_params=[];
                materialAudit_params.prefix=$('#produceAudit_formAudit #urlPrefix').val();
                cancelAudit($('#produceAudit_audited_formSearch #produce_audited_list').bootstrapTable('getSelections'),function(){
                    searchProduce_audited_Result();
                },null,materialAudit_params);
            })
        }
        //批量审核
        btn_batchaudit.unbind("click").click(function(){
            var sel_row = $('#produceAudit_audit_formSearch #produce_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if (sel_row[i].cplxdm != "YQ"){
                    $.error("请选择产品类别是仪器的数据！");
                    return;
                }
                ids = ids + ","+ sel_row[i].sczlmxid;
            }
            ids = ids.substr(1);
            produce_audit_DealById(ids,"batchaudit",btn_batchaudit.attr("tourl"),"AUDIT_YQ_PRODUCE");
        });
        /*-----------------------------------------------------------------------------*/
        /*-----------------------显示隐藏------------------------------------*/
        $("#produceAudit_audit_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(produce_audit_turnOff){
                $("#produceAudit_audit_formSearch #searchMore").slideDown("low");
                produce_audit_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#produceAudit_audit_formSearch #searchMore").slideUp("low");
                produce_audit_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#produceAudit_audited_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(produce_audited_turnOff){
                $("#produceAudit_audited_formSearch #searchMore").slideDown("low");
                produce_audited_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#produceAudit_audited_formSearch #searchMore").slideUp("low");
                produce_audited_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oInit;
}
var produce_audit_turnOff=true;
var produce_audited_turnOff=true;

function searchProduce_audit_Result(isTurnBack){
    //关闭高级搜索条件
    $("#produceAudit_audit_formSearch #searchMore").slideUp("low");
    produce_audit_turnOff=true;
    $("#produceAudit_audit_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#produceAudit_audit_formSearch #produce_auditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#produceAudit_audit_formSearch #produce_auditing_list').bootstrapTable('refresh');
    }
}

function searchProduce_audited_Result(isTurnBack){
    //关闭高级搜索条件
    $("#produceAudit_audited_formSearch #searchMore").slideUp("low");
    produce_audited_turnOff=true;
    $("#produceAudit_audited_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#produceAudit_audited_formSearch #produce_audited_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#produceAudit_audited_formSearch #produce_audited_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new produce_audit_TableInit();
    oTable.Init();

    var oButtonInit = new produce_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#produceAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#produceAudit_audited_formSearch .chosen-select').chosen({width: '100%'});
})