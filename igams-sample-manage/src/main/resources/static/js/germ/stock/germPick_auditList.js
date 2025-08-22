var Pick_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#germPick_audit_formSearch #germPick_auditing_list").bootstrapTable({
            url: '/germ/auditing/pageGetListGermAuditing',
            method: 'get',                      // 请求方式（*）
            toolbar: '#germPick_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "llid",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这germ里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "llid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '0.5%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '1%',
                align: 'center',
                visible:true
            },{
                field: 'llid',
                title: '领料id',
                width: '8%',
                align: 'center',
                visible: false,
                sortable: true
            }, {
                field: 'lldh',
                title: '领料单号',
                width: '4%',
                align: 'center',
                sortable: true,
                visible: true
            }, {
                field: 'llrmc',
                title: '领料人',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'flrmc',
                title: '发料人',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'bmmc',
                title: '部门',
                width: '5%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '4%',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'ckzt',
                title: '是否出库',
                width: '3%',
                align: 'center',
                visible: true,
                sortable: true,
                formatter:ckztformat,
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'center',
                visible: true,
                sortable: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                pick_audit_DealById(row.llid,'view',$("#germPick_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#germPick_audit_formSearch #germPick_auditing_list").colResizable({
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#germPick_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#germPick_audit_formSearch #cxnr').val());
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["mc"] = cxnr;
        }else if (cxtj == "2") {
            map["bh"] = cxnr;
        }else if (cxtj == "3") {
            map["wz"] = cxnr;
        }else if (cxtj == "4") {
            map["ly"] = cxnr;
        }else if (cxtj == "5") {
            map["ph"] = cxnr;
        }else if (cxtj == "6") {
            map["gg"] = cxnr;
        }else if (cxtj == "7") {
            map["lldh"] = cxnr;
        }else if (cxtj == "8") {
            map["flrmc"] = cxnr;
        }else if (cxtj == "9") {
            map["llrmc"] = cxnr;
        }else if (cxtj == "10") {
            map["bmmc"] = cxnr;
        }
        // 申请开始日期
        var sqsjstart = jQuery('#germPick_audit_formSearch #sqsjstart').val();
        map["sqsjstart"] = sqsjstart;
        // 申请结束日期
        var sqsjend = jQuery('#germPick_audit_formSearch #sqsjend').val();
        map["sqsjend"] = sqsjend;
        return map;
    };
    return oTableInit;
}

//借出列表的提交状态格式化函数
function ckztformat(value,row,index) {
    if (row.ckzt == '1')
        return '已出库';
    else return '未出库';


}
var Pick_audited_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#germPick_audited_formSearch #germPick_audited_list").bootstrapTable({
            url: '/germ/auditing/pageGetListGermAuditing',
            method: 'get',                      // 请求方式（*）
            toolbar: '#germPick_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "llid",				// 排序字段
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
            uniqueId: "llid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'center',
                visible:true
            },{
                field: 'llid',
                title: '领料id',
                width: '8%',
                align: 'center',
                visible: false,
                sortable: true
            }, {
                field: 'lldh',
                title: '领料单号',
                width: '4%',
                align: 'center',
                sortable: true,
                visible: true
            }, {
                field: 'llrmc',
                title: '领料人',
                width: '5%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'flrmc',
                title: '发料人',
                width: '5%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'bmmc',
                title: '部门',
                width: '5%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '6%',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'ckzt',
                title: '是否出库',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true,
                formatter:ckztformat,
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'center',
                visible: true,
                sortable: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                pick_audit_DealById(row.llid,'view',$("#germPick_audited_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#germPick_audited_formSearch #germPick_audited_list").colResizable({
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#germPick_audited_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#germPick_audited_formSearch #cxnr').val());
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["mc"] = cxnr;
        }else if (cxtj == "2") {
            map["bh"] = cxnr;
        }else if (cxtj == "3") {
            map["wz"] = cxnr;
        }else if (cxtj == "4") {
            map["ly"] = cxnr;
        }else if (cxtj == "5") {
            map["ph"] = cxnr;
        }else if (cxtj == "6") {
            map["gg"] = cxnr;
        }else if (cxtj == "7") {
            map["lldh"] = cxnr;
        }else if (cxtj == "8") {
            map["flrmc"] = cxnr;
        }else if (cxtj == "9") {
            map["llrmc"] = cxnr;
        }else if (cxtj == "10") {
            map["bmmc"] = cxnr;
        }
        // 申请开始日期
        var sqsjstart = jQuery('#germPick_audited_formSearch #sqsjstart').val();
        map["sqsjstart"] = sqsjstart;
        // 申请结束日期
        var sqsjend = jQuery('#germPick_audited_formSearch #sqsjend').val();
        map["sqsjend"] = sqsjend;
        return map;
    };
    return oTableInit;
}
//待审核列表的按钮路径
function pick_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?llid="+id;
        $.showDialog(url,'查看信息',viewMaterialAuditConfig);
    }else if (action == 'audit') {
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_GERM_PICK',
            url:tourl,
            data:{'ywzd':'llid'},
            title:"菌种领料审核",
            preSubmitCheck:'preSubmitGermPicking',
            callback:function(){
                searchPick_audit_Result();
            },
            dialogParam:{width:1550}
        });
    }
}

function preSubmitGermPicking(){
    var json=[];
    var data=$('#editGermPickingForm #tb_list').bootstrapTable('getData');//获取选择行数据
    if(data.length<1){
        $.error("请新增明细！");
        return false;
    }
    for(var i=0;i<data.length;i++){
        var sz={"jzkcid":'',"klsl":'',"qlsl":'',"yds":''};
        sz.jzkcid=data[i].jzkcid;
        var qlsl = parseFloat(data[i].qlsl);
        var klsl = parseFloat(data[i].klsl);
        if(qlsl.toFixed(2)-klsl.toFixed(2)>0){
            $.error("第"+(i+1)+"行 请领数量不能大于可请领数！");
            return false;
        }
        sz.klsl=data[i].klsl;
        sz.qlsl=data[i].qlsl;
        sz.yds=data[i].yds;
        json.push(sz);
    }
    $("#editGermPickingForm #llmx_json").val(JSON.stringify(json));
    return true;
}
var viewMaterialAuditConfig = {
    width		: "1400px",
    modalName	:"viewMaterialAuditModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function searchPick_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#germPick_audit_formSearch #germPick_auditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#germPick_audit_formSearch #germPick_auditing_list').bootstrapTable('refresh');
    }
}

function searchPick_audited_Result(isTurnBack){
    if(isTurnBack){
        $('#germPick_audited_formSearch #germPick_audited_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#germPick_audited_formSearch #germPick_audited_list').bootstrapTable('refresh');
    }
}
var germPick_ButtonInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#germPick_audit_formSearch #btn_query");
        var btn_view = $("#germPick_audit_formSearch #btn_view");
        var btn_cancelAudit = $("#germPick_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
        var btn_queryAudited = $("#germPick_audited_formSearch #btn_query");
        var btn_audit = $("#germPick_audit_formSearch #btn_audit");

        //添加日期控件
        laydate.render({
            elem: '#germPick_audit_formSearch #sqsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#germPick_audit_formSearch #sqsjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#germPick_audited_formSearch #sqsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#germPick_audited_formSearch #sqsjend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                auditingResult(true);
            });
        }
        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPick_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchPick_audited_Result(true);
            });
        }
        /* ---------------------------查看信息-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#germPick_audit_formSearch #germPick_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                pick_audit_DealById(sel_row[0].llid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------审核信息-----------------------------------*/
        btn_audit.unbind("click").click(function () {
            var sel_row = $('#germPick_audit_formSearch #germPick_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                pick_audit_DealById(sel_row[0].llid, "audit", btn_audit.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#germPick_formAudit #germPick_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='germPick_auditing'){
                    var oTable= new Pick_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new Pick_audited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #outDepotAudit_audited_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var materialAudit_params=[];
                cancelAudit($('#germPick_audited_formSearch #germPick_audited_list').bootstrapTable('getSelections'),function(){
                    searchPick_audited_Result();
                },null,materialAudit_params);
            })
        }
    }
    return oInit;
};



$(function(){
    // 1.初始化Table
    var oTable= new Pick_audit_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new germPick_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#germPick_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#germPick_audited_formSearch .chosen-select').chosen({width: '100%'});
});