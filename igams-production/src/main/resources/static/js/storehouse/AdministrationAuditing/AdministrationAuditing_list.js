var Administration_pick_audit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#AdministrationAuditing_formSearch #AdministrationAuditing_list").bootstrapTable({
            url: $("#AdministrationAuditing_formAuditing #urlPrefix").val()+'/storehouse/receiveAdministrationMateriel/pageGetListReceiveAdministrationAuditing',
            method: 'get',                      // 请求方式（*）
            toolbar: '#AdministrationAuditing_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xzllid",				// 排序字段
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
            uniqueId: "xzllid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                title: '序号',
                width: '4%',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'xzllid',
                title: '领料ID',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'lldh',
                title: '领料单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbm',
                title: '申请部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'llry',
                title: '领料人员',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'flry',
                title: '发料人员',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                administration_audit(row.xzllid,'view',$("#AdministrationAuditing_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#AdministrationAuditing_formSearch #AdministrationAuditing_list").colResizable({
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
            sortLastName: "xzllid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
        };
        var cxtj=$("#AdministrationAuditing_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#AdministrationAuditing_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr
        }else if(cxtj=="1"){
            map["lldh"]=cxnr
        }else if(cxtj=="2"){
            map["sqbm"]=cxnr
        }else if(cxtj=="3"){
            map["llry"]=cxnr
        }
        else if(cxtj=="4"){
            map["flry"]=cxnr
        }
        return map;
    };
    return oTableInit;
}

var Administration_pick_audited=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#AdministrationAuditinged_formSearch #AdministrationAuditinged_list").bootstrapTable({
            url: $("#AdministrationAuditing_formAuditing #urlPrefix").val()+'/storehouse/receiveAdministrationMateriel/pageGetListReceiveAdministrationAuditing',
            method: 'get',                      // 请求方式（*）
            toolbar: '#AdministrationAuditinged_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xzllid",				// 排序字段
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
            uniqueId: "xzllid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:[{
                checkbox: true,
                width: '4%'
            },{
                title: '序号',
                width: '4%',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'xzllid',
                title: '领料ID',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'lldh',
                title: '领料单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbm',
                title: '申请部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'llry',
                title: '领料人员',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'flry',
                title: '发料人员',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                sortable: true,
                width: '16%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                administration_audit(row.xzllid,'view',$("#AdministrationAuditinged_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#AdministrationAuditinged_formSearch #AdministrationAuditinged_list").colResizable({
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
            sortLastName: "xzllid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#AdministrationAuditinged_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#AdministrationAuditinged_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr
        }else if(cxtj=="1"){
            map["lldh"]=cxnr
        }else if(cxtj=="2"){
            map["sqbm"]=cxnr
        }else if(cxtj=="3"){
            map["llry"]=cxnr
        }
        else if(cxtj=="4"){
            map["flry"]=cxnr
        }
        return map;
    };
    return oTableInit;
}

var viewMaterialAuditConfig = {
    width		: "1600px",
    modalName	:"viewMaterialAuditModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function administration_audit(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $('#AdministrationAuditing_formAuditing #urlPrefix').val() + tourl;
    if(action=="view"){
        var url=tourl+"?xzllid="+id;
        $.showDialog(url,'查看领料信息',viewMaterialAuditConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_ADMINISTRATION',
            url:tourl,
            data:{'ywzd':'xzllid'},
            title:"行政领料审核",
            preSubmitCheck:'preSubmitputmaterial',
            prefix:$('#AdministrationAuditing_formAuditing #urlPrefix').val(),
            callback:function(){
                searchPickAdminidtration_audit(true);//回调
            },
            dialogParam:{width:1500}
        });
    }
}


function preSubmitputmaterial(){
    var json = [];
    if(t_map.rows != null && t_map.rows.length > 0){
        for(var i=0;i<t_map.rows.length;i++){
            if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
                $.alert("领料数量不能为空！");
                return false;
            }
            if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].klsl)){
                $.alert("领用数量不能大于可领数量！");
                return false;
            }
            if(t_map.rows[i].qlsl==0){
                $.alert("请领数量不能为0！");
                return false;
            }
            var sz = {"xzkcid":'',"qlsl":'',"xh":'',"yds":'',"sbysid":''};
            sz.xzkcid = t_map.rows[i].xzkcid;
            sz.qlsl = t_map.rows[i].qlsl;
            sz.yds = t_map.rows[i].yds;
            sz.sbysid = t_map.rows[i].sbysid;
            sz.xh = i+1;
            json.push(sz);
        }
        $("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
    }else{
        $.alert("领料信息不能为空！");
        return false;
    }
    return true;
}

var pickadminidtrationAuditing_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#AdministrationAuditing_formSearch #btn_query");//模糊查询（待审核）
        var btn_queryAudited = $("#AdministrationAuditinged_formSearch #btn_query");//模糊查询（审核历史）
        var btn_cancelAudit = $("#AdministrationAuditinged_formSearch #btn_cancelAudit");//取消审核（审核历史）
        var btn_view = $("#AdministrationAuditing_formSearch #btn_view");//查看页面（待审核）
        var btn_audit = $("#AdministrationAuditing_formSearch #btn_audit");//审核页面（待审核）
        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPickAdminidtration_audit(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchPickAdminidtration_audited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#AdministrationAuditing_formSearch #AdministrationAuditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                administration_audit(sel_row[0].xzllid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row=$('#AdministrationAuditing_formSearch #AdministrationAuditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                administration_audit(sel_row[0].xzllid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var materialAudit_params=[];
                materialAudit_params.prefix=$('#AdministrationAuditing_formSearch #urlPrefix').val();
                cancelAudit($('#AdministrationAuditing_formSearch #AdministrationAuditing_list').bootstrapTable('getSelections'),function(){
                    searchPickAdminidtration_audited();
                },null,materialAudit_params);
            })
        }
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#AdministrationAuditing_formAuditing #AdministrationAuditing_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='AdministrationAuditing'){
                    var oTable= new Administration_pick_audit();
                    oTable.Init();

                }else{
                    var oTable= new Administration_pick_audited();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #outDepotAudit_audited_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

    }
    return oInit;
}


function searchPickAdminidtration_audit(isTurnBack){
    if(isTurnBack){
        $('#AdministrationAuditing_formSearch #AdministrationAuditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#AdministrationAuditing_formSearch #AdministrationAuditing_list').bootstrapTable('refresh');
    }
}

function searchPickAdminidtration_audited(isTurnBack){
    if(isTurnBack){
        $('#AdministrationAuditinged_formSearch #AdministrationAuditinged_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#AdministrationAuditinged_formSearch #AdministrationAuditinged_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new Administration_pick_audit();
    oTable.Init();

    var oButtonInit = new pickadminidtrationAuditing_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#AdministrationAuditing_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#AdministrationAuditinged_formSearch .chosen-select').chosen({width: '100%'});
})