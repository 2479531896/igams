var contractPayment_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#contractPayment_audit_formSearch #contractPayment_audit_list").bootstrapTable({
            url: $('#contractPayment_formAudit #urlPrefix').val()+'/contract/payment/pageGetListAuditPayMent',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#contractPayment_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "shgc.sqsj",				// 排序字段
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
            uniqueId: "htfkid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#contractPayment_audit_formSearch #contractPayment_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'fkdh',
                title: '付款单号',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zfdxmc',
                title: '支付对象',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fkzje',
                title: '付款总金额',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'fkrq',
                title: '付款日期',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '操作人',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yfje',
                title: '已付金额',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                htfkqk_audit_DealById(row.htfkid,"view",$("#contractPayment_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#contractPayment_audit_formSearch #contractPayment_audit_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "htfkqk.htfkid", // 防止同名排位用
            sortLastOrder: "asc",// 防止同名排位用
            zt:"10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#contractPayment_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#contractPayment_audit_formSearch #cxnr').val());
        if(cxtj=="1"){
            map["fkdh"]=cxnr
        }
        map["dqshzt"] = 'dsh';
        return map;
    }
    return oTableInit;
}


var sbyzAudited_TableInit=function(){
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $("#contractPayment_audit_audited #tb_list").bootstrapTable({
            url: $('#contractPayment_formAudit #urlPrefix').val()+'/contract/payment/pageGetListAuditPayMent',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#contractPayment_audit_audited #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "shxx.shsj",				// 排序字段
            sortOrder: "desc",                  // 排序方式
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
            uniqueId: "shxx_shxxid",            // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                  // 是否显示父子表
            isForceTable:true,
            columns: [{
                width: '2%',
                checkbox: true
            },{
                field: 'htnbbh',
                title: '合同内部编号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'fkje',
                title: '付款金额',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'fkrq',
                title: '付款日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'fkbfb',
                title: '付款百分比',
                width: '10%',
                align: 'left',
                formatter:audit_fkbfbformat,
                sortable: true,
                visible: true
            },{
                field: 'lrry',
                title: '操作人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'fpje',
                title: '发票金额',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'zje',
                title: '总金额',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'yfje',
                title: '已付金额',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            },{
                field: 'shxx_shxxid',
                align: 'center',
                title: '审核信息ID',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                htfkqk_audit_DealById(row.htfkid,"view",$("#contractPayment_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#contractPayment_audit_audited #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        });
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
            sortLastName: "htfkqk.htfkid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };



        var cxtj=$("#contractPayment_audit_audited #cxtj").val();
        var cxnr=$.trim(jQuery('#contractPayment_audit_audited #cxnr').val());
        if(cxtj=="1"){
            map["htnbbh"]=cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
}


function audit_fkbfbformat(value,row,index){
    return row.fkbfb+"%";
}

function htfkqk_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#contractPayment_formAudit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?htfkid="+id;
        $.showDialog(url,'查看信息',viewcontractPayment_audit_Config);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_CONTRACT_PAYMENT',
            url: tourl,
            data:{'ywzd':'htfkid'},
            title:"合同审核",
            preSubmitCheck:'preSubmitcontractPayment',
            prefix:$('#contractPayment_formAudit #urlPrefix').val(),
            callback:function(){
                searchHtfkqk_audit_Result(true);// 回调
            },
            dialogParam:{width:1500}
        });
    }
}

function preSubmitcontractPayment(){
    return true;
}

var contractPayment_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#contractPayment_audit_formSearch #btn_query");// 模糊查询
        var btn_queryAudited = $("#contractPayment_audit_audited #btn_query");// 审核记录列表模糊查询
        var btn_cancelAudit = $("#contractPayment_audit_audited #btn_cancelAudit");// 取消审核
        var btn_view = $("#contractPayment_audit_formSearch #btn_view");// 查看页面
        var btn_audit = $("#contractPayment_audit_formSearch #btn_audit");// 审核

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchHtfkqk_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchHtfkqkAudited(true);
            });
        }
        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#contractPayment_audit_formSearch #contractPayment_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                htfkqk_audit_DealById(sel_row[0].htfkid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#contractPayment_audit_formSearch #contractPayment_audit_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                htfkqk_audit_DealById(sel_row[0].htfkid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        // 选项卡切换事件回调
        $('#contractPayment_formAudit #contractPayment_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){// 只调用一次
                if(_hash=='contractPayment_auditing'){
                    var oTable= new contractPayment_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new sbyzAudited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{// 重新加载
                // $(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');// 触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var contractPayment_params=[];
                contractPayment_params.prefix=$('#contractPayment_formAudit #urlPrefix').val();
                cancelAudit($('#contractPayment_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
                    searchHtfkqkAudited();
                },null,contractPayment_params);
            })
        }
    }
    return oInit;
}


var viewcontractPayment_audit_Config = {
    width		: "1080px",
    modalName	:"viewVerificationConfig",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchHtfkqk_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#contractPayment_audit_formSearch #contractPayment_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#contractPayment_audit_formSearch #contractPayment_audit_list').bootstrapTable('refresh');
    }
}

function searchHtfkqkAudited(isTurnBack){
    if(isTurnBack){
        $('#contractPayment_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#contractPayment_audit_audited #tb_list').bootstrapTable('refresh');
    }
}


$(function(){
    var oTable= new contractPayment_audit_TableInit();
    oTable.Init();

    var oButtonInit = new contractPayment_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#contractPayment_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#contractPayment_audit_audited .chosen-select').chosen({width: '100%'});
})