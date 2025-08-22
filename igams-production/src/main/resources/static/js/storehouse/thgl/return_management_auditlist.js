var returnManagement_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#returnManagement_audit_formSearch #returnManagement_audit_list").bootstrapTable({
            url: $('#returnManagement_audit #urlPrefix').val()+'/storehouse/returnManagement/pageGetListReturnManagementsh',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#returnManagement_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "thgl.thdh",				// 排序字段
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
            uniqueId: "thid",                     // 每一行的唯一标识，一般为主键列
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
                    var options = $('#returnManagement_audit_formSearch #returnManagement_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'thdh',
                title: '退货单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'u8thdh',
                title: 'u8退货单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jsrmc',
                title: '经手人',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'djrq',
                title: '单据日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xsbmmc',
                title: '销售部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ddh',
                title: '订单号',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khjc',
                title: '客户简称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'lrsj',
                title: '录入时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
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
                returnManagementl_audit_DealById(row.thid,"view",$("#returnManagement_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#returnManagement_audit_formSearch #returnManagement_audit_list").colResizable({
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
            sortLastName: "thgl.thid", // 防止同名排位用
            sortLastOrder: "asc",// 防止同名排位用
            zt:"10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#returnManagement_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#returnManagement_audit_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["thdh"]=cxnr;
        }else if(cxtj=="2"){
            map["jsrmc"]=cxnr;
        }else if(cxtj=="3"){
            map["xslxmc"]=cxnr;
        }else if(cxtj=="4"){
            map["xsbmmc"]=cxnr;
        }
        map["dqshzt"] = 'dsh';
        return map;
    }
    return oTableInit;
}

var returnManagementAudited_TableInit=function(){
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $("#returnManagement_audited_formSearch #returnManagement_audited_list").bootstrapTable({
            url: $('#returnManagement_audit #urlPrefix').val()+'/storehouse/returnManagement/pageGetListReturnManagementsh',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#returnManagement_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "thgl.thdh",				// 排序字段
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
            // isForceTable:true,
            columns: [{
                checkbox: true,
                width: '3%',
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'center',
                visible:true
            },{
                field: 'thdh',
                title: '退货单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'u8thdh',
                title: 'u8退货单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jsrmc',
                title: '经手人',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'djrq',
                title: '单据日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xsbmmc',
                title: '销售部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ddh',
                title: '订单号',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khjc',
                title: '客户简称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                visible: true
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
                returnManagementl_audit_DealById(row.thid,"view",$("#returnManagement_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#returnManagement_audited_formSearch #returnManagement_audited_list").colResizable({
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
            sortLastName: "thgl.thid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#returnManagement_audited_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#returnManagement_audited_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["thdh"]=cxnr;
        }else if(cxtj=="2"){
            map["jsrmc"]=cxnr;
        }else if(cxtj=="3"){
            map["xslxmc"]=cxnr;
        }else if(cxtj=="4"){
            map["xsbmmc"]=cxnr;
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
}

function returnManagementl_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#returnManagement_audit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?thid="+id;
        $.showDialog(url,'查看信息',viewReturnManagementConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'RETURN_GOODS',
            url: tourl,
            data:{'ywzd':'thid'},
            title:"退货审核",
            preSubmitCheck:'preSubmitReturnManagement',
            prefix:$('#returnManagement_audit #urlPrefix').val(),
            callback:function(){
                searchThgl_audit_Result(true);// 回调
            },
            dialogParam:{width:1500}
        });
    }
}

function preSubmitReturnManagement(){
    var $this = this;
    var opts = $this["options"]||{};
    let json = [];
    if(t_map.rows != null && t_map.rows.length > 0) {
        for (let i = 0; i < t_map.rows.length; i++) {
            var sz = {"index":'',"wlid":'',"hwid":'',"thsl":'',"bz":'',"xsdd":'',"fhmxglid":'',"ck":'',"kw":'',"bj":'',"suil":'',"wsdj":'',"hsdj":'',"xsmxid":'',"fhmxid":'',"thmxid":'',"thid":'',"scrq":'',"yxq":'',"scph":'',"zsh":''};
            sz.index=t_map.rows.length;
            sz.hwid = t_map.rows[i].hwid;
            sz.wlid = t_map.rows[i].wlid;
            sz.thsl = t_map.rows[i].thsl;
            sz.bz = t_map.rows[i].bz;
            sz.xsdd = t_map.rows[i].xsdd;
            sz.fhmxglid = t_map.rows[i].fhmxglid;
            sz.bj = t_map.rows[i].bj;
            sz.suil = t_map.rows[i].suil;
            sz.wsdj = t_map.rows[i].wsdj;
            sz.hsdj = t_map.rows[i].hsdj;
            sz.ck = t_map.rows[i].ck;
            sz.kw = t_map.rows[i].kw;
            sz.xsmxid = t_map.rows[i].xsmxid;
            sz.fhmxid = t_map.rows[i].fhmxid;
            sz.thmxid = t_map.rows[i].thmxid;
            sz.thid = t_map.rows[i].thid;
            sz.scrq = t_map.rows[i].scrq;
            sz.yxq = t_map.rows[i].yxq;
            sz.scph = t_map.rows[i].scph;
            sz.zsh = t_map.rows[i].zsh;
            json.push(sz);
        }
        $("#editReturnManagementForm #cklist").val("");
        $("#editReturnManagementForm #kwlist").val("");
        $("#editReturnManagementForm #thmx_json").val(JSON.stringify(json));
        $("#editReturnManagementForm input[name='access_token']").val($("#ac_tk").val());
    }else {
        $.error("明细不允许为空！");
        return false;
    }
    return true;
}

var returnManagement_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#returnManagement_audit_formSearch #btn_query");// 模糊查询
        var btn_queryAudited = $("#returnManagement_audited_formSearch #btn_query");// 审核记录列表模糊查询
        var btn_cancelAudit = $("#returnManagement_audited_formSearch #btn_cancelAudit");// 取消审核
        var btn_view = $("#returnManagement_audit_formSearch #btn_view");// 查看页面
        var btn_audit = $("#returnManagement_audit_formSearch #btn_audit");// 审核

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchThgl_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchThglAudited(true);
            });
        }
        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#returnManagement_audit_formSearch #returnManagement_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                returnManagementl_audit_DealById(sel_row[0].thid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#returnManagement_audit_formSearch #returnManagement_audit_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                returnManagementl_audit_DealById(sel_row[0].thid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        // 选项卡切换事件回调
        $('#returnManagement_audit #returnManagement_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){// 只调用一次
                if(_hash=='returnManagement_auditingtab'){
                    var oTable= new returnManagement_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new returnManagementAudited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{// 重新加载
                // $(_gridId + ' #returnManagement_audited_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');// 触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var returnManagement_params=[];
                returnManagement_params.prefix=$('#returnManagement_audit #urlPrefix').val();
                cancelAudit($('#returnManagement_audited_formSearch #returnManagement_audited_list').bootstrapTable('getSelections'),function(){
                    searchThglAudited();
                },null,returnManagement_params);
            })
        }
    }
    return oInit;
}


var viewReturnManagementConfig = {
    width		: "1600px",
    modalName	:"viewReturnManagementConfig",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchThgl_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#returnManagement_audit_formSearch #returnManagement_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#returnManagement_audit_formSearch #returnManagement_audit_list').bootstrapTable('refresh');
    }
}

function searchThglAudited(isTurnBack){
    if(isTurnBack){
        $('#returnManagement_audited_formSearch #returnManagement_audited_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#returnManagement_audited_formSearch #returnManagement_audited_list').bootstrapTable('refresh');
    }
}


$(function(){
    var oTable= new returnManagement_audit_TableInit();
    oTable.Init();

    var oButtonInit = new returnManagement_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#returnManagement_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#returnManagement_audited_formSearch .chosen-select').chosen({width: '100%'});
})