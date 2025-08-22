var deviceCheck_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#progress_audit_formSearch #progressCheck_audit_list").bootstrapTable({
            url: $("#progress_formAudit #urlPrefix").val()+'/progress/progress/pageGetListDeviceAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#progress_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "cpxqjh.lrsj",				//排序字段
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
            uniqueId: "cpxqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'cpxqid',
                title: '成本需求ID',
                width: '8%',
                align: 'left',
                visible: false
            },
            {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sqrmc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
                visible: true
            },
            {
                field: 'sqbmmc',
                title: '申请部门',
                titleTooltip:'申请部门',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'xqrq',
                title: '需求日期',
                titleTooltip:'需求日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yjyt',
                title: '预计用途',
                titleTooltip:'预计用途',
                width: '12%',
                align: 'left',
                visible: true
            },{
                    field: 'shxx_shyj',
                    title: '审核意见',
                    width: '15%',
                    align: 'left',
                    visible:true,
                }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '12%',
                align: 'left',
                visible: true
                    
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                progress_audit_DealById(row.cpxqid,"view",$("#progress_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#progress_audit_formSearch #progressCheck_audit_list").colResizable({
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
            sortLastName: "cpxqjh.cpxqid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#progress_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#progress_audit_formSearch #cxnr').val());
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["xqdh"] = cxnr;
        }else if (cxtj == "2") {
            map["sqbmmc"] = cxnr;
        }else if (cxtj == "3") {
            map["sqrmc"] = cxnr;
        }else if (cxtj == "4") {
            map["wlbm"] = cxnr;
        }else if (cxtj == "5") {
            map["wlmc"] = cxnr;
        }
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

var deviceCheckAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#progress_audit_audited #progress_tb_list").bootstrapTable({
            url:$("#progress_formAudit #urlPrefix").val()+'/progress/progress/pageGetListDeviceAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#progress_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "cpxqjh.lrsj",				//排序字段
            sortOrder: "desc",                  //排序方式
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
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "cpxqid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'cpxqid',
                title: '成本需求ID',
                width: '8%',
                align: 'left',
                visible: false
            },
            {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sqrmc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
                visible: true
            },
            {
                field: 'sqbmmc',
                title: '申请部门',
                titleTooltip:'申请部门',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'xqrq',
                title: '需求日期',
                titleTooltip:'需求日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yjyt',
                title: '预计用途',
                titleTooltip:'预计用途',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '12%',
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
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                progress_audit_DealById(row.cpxqid,"view",$("#progress_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#progress_audit_audited #progress_tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "cpxqjh.cpxqid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        var cxtj=$("#progress_audit_audited #cxtj").val();
        // 查询条件
        var cxnr=$.trim(jQuery('#progress_audit_audited #cxnr').val());
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["xqdh"] = cxnr;
        }else if (cxtj == "2") {
            map["sqbmmc"] = cxnr;
        }else if (cxtj == "3") {
            map["sqrmc"] = cxnr;
        }else if (cxtj == "4") {
            map["wlbm"] = cxnr;
        }else if (cxtj == "5") {
            map["wlmc"] = cxnr;
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};
function progress_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#progress_formAudit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url= tourl + "?cpxqid=" +id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_FG_PLAN',
            url:tourl,
            data:{'ywzd':'cpxqid'},
            title:"发货审核",
            preSubmitCheck:'preSubmitSHip',
            prefix:$('#progress_formAudit #urlPrefix').val(),
            callback:function(){
                searchProgress_audit_Result(true);//回调
            },
            dialogParam:{width:1200}
        });
    }
}

function preSubmitSHip(){
    var json = [];
    if(t_map.rows != null && t_map.rows.length > 0){
        for(var i=0;i<t_map.rows.length;i++){
            if(t_map.rows[i].sl==null || t_map.rows[i].sl==''){
                $.alert("数量不能为空！");
                return false;
            }
            if(t_map.rows[i].sl==0){
                $.alert("请领数量不能为0！");
                return false;
            }
            if(t_map.rows[i].yq==null || t_map.rows[i].yq==''){
                $.alert("批次和批量要求不能为空！");
                return false;
            }

            var sz = {"xqjhmxid":'',"wlbm":'',"wlmc":'',"sl":'',"wlid":'',"scsl":'',"yq":''};
            sz.xqjhmxid = t_map.rows[i].xqjhmxid;
            sz.wlbm = t_map.rows[i].wlbm;
            sz.wlmc = t_map.rows[i].wlmc;
            sz.sl = t_map.rows[i].sl;
            sz.wlid = t_map.rows[i].wlid;
            sz.scsl = t_map.rows[i].sl;
            sz.yq = t_map.rows[i].yq;
            json.push(sz);
        }
        $("#progress_edit_ajaxForm #xqjhmx_json").val(JSON.stringify(json));
    }else{
        $.alert("需求明细信息不能为空！");
        return false;
    }
    return true;
}

var deviceCheck_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#progress_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#progress_audit_audited #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#progress_audit_audited #btn_cancelAudit");//取消审核
        var btn_view = $("#progress_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#progress_audit_formSearch #btn_audit");//审核

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchProgress_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchProgressAudited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#progress_audit_formSearch #progressCheck_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                progress_audit_DealById(sel_row[0].cpxqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#progress_audit_formSearch #progressCheck_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                progress_audit_DealById(sel_row[0].cpxqid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#progress_formAudit #deviceCheck_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='progress_auditing'){
                    var oTable= new deviceCheck_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new deviceCheckAudited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #progress_tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var putInStorage_params=[];
                putInStorage_params.prefix=$('#progress_formAudit #urlPrefix').val();
                cancelAudit($('#progress_audit_audited #progress_tb_list').bootstrapTable('getSelections'),function(){
                    searchProgressAudited();
                },null,putInStorage_params);
            })
        }
    }
    return oInit;
}

var viewConfig = {
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


function searchProgress_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#progress_audit_formSearch #progressCheck_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#progress_audit_formSearch #progressCheck_audit_list').bootstrapTable('refresh');
    }
}

function searchProgressAudited(isTurnBack){
    if(isTurnBack){
        $('#progress_audit_audited #progress_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#progress_audit_audited #progress_tb_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new deviceCheck_audit_TableInit();
    oTable.Init();

    var oButtonInit = new deviceCheck_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#progress_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#progress_audit_audited .chosen-select').chosen({width: '100%'});
});