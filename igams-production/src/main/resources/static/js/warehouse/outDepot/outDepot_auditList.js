var MaterialAudit_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#outDepotAudit_audit_formSearch #outDepotAudit_auditing_list").bootstrapTable({
            url: $("#outDepotAudit_formAudit #urlPrefix").val()+'/warehouse/outDepot/pageGetListOutDepotAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#outDepotAudit_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "ckgl.lrsj",				// 排序字段
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
            uniqueId: "ckid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                field: 'ckdh',
                title: '出库单号',
                width: '13%',
                align: 'left',
                visible: true
            },{
                field: 'ckrq',
                title: '出库日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'ckmc',
                title: '出库仓库',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'cklbmc',
                title: '出库类别',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'llrmc',
                title: '领料人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'flrmc',
                title: '发料人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lldh',
                title: '领料单号',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'u8ckdh',
                title: 'U8出库单号',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '16%',
                align: 'left',
                visible:true,
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
                material_audit_DealById(row.ckid,'view',$("#outDepotAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#outDepotAudit_audit_formSearch #outDepotAudit_auditing_list").colResizable({
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
            sortLastName: "ckgl.ckid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
        };
        var outDepotAudit_select=$("#outDepotAudit_audit_formSearch #outDepotAudit_select").val();
        var outDepotAudit_input=$.trim(jQuery('#outDepotAudit_audit_formSearch #outDepotAudit_input').val());
        if(outDepotAudit_select=="0"){
            map["entire"]=outDepotAudit_input
        }else if(outDepotAudit_select=="1"){
            map["ckdh"]=outDepotAudit_input;
        }else if(outDepotAudit_select=="2"){
            map["ckmc"]=outDepotAudit_input;
        }else if(outDepotAudit_select=="3"){
            map["cklbmc"]=outDepotAudit_input;
        }else if(outDepotAudit_select=="4"){
            map["lldh"]=outDepotAudit_input;
        }
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
}

//是否通过格式化
function materialAudit_wcbjformat(value,row,index){
    if (row.shxx_sftg == '1') {
        return '通过';
    }else{
        return '未通过';
    }
}

var MaterialAudit_audited_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#outDepotAudit_audited_formSearch #outDepotAudit_audited_list").bootstrapTable({
            url: $("#outDepotAudit_formAudit #urlPrefix").val()+'/warehouse/outDepot/pageGetListOutDepotAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#outDepotAudit_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "ckgl.lrsj",				// 排序字段
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
            uniqueId: "shxx_shxxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'ckdh',
                title: '出库单号',
                width: '13%',
                align: 'left',
                visible: true
            },{
                field: 'ckrq',
                title: '出库日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'ckmc',
                title: '出库仓库',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'cklbmc',
                title: '出库类别',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'llrmc',
                title: '领料人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'flrmc',
                title: '发料人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lldh',
                title: '领料单号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'u8ckdh',
                title: 'U8出库单号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '16%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'shxx_sftg',
                title: '是否通过',
                width: '10%',
                align: 'left',
                visible:true,
                formatter:materialAudit_wcbjformat,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                material_audit_DealById(row.ckid,'view',$("#outDepotAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#outDepotAudit_audited_formSearch #outDepotAudit_audited_list").colResizable({
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
            sortLastName: "ckgl.ckid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
//		return getMaterialAuditSearchData(map);
        var outDepotAudit_select=$("#outDepotAudit_audited_formSearch #outDepotAudit_select").val();
        var outDepotAudit_input=$.trim(jQuery('#outDepotAudit_audited_formSearch #outDepotAudit_input').val());
        if(outDepotAudit_select=="0"){
            map["entire"]=outDepotAudit_input
        }else if(outDepotAudit_select=="1"){
            map["ckdh"]=outDepotAudit_input;
        }else if(outDepotAudit_select=="2"){
            map["ckmc"]=outDepotAudit_input;
        }else if(outDepotAudit_select=="3"){
            map["cklbmc"]=outDepotAudit_input;
        }else if(outDepotAudit_select=="4"){
            map["lldh"]=outDepotAudit_input;
        }
        map["dqshzt"] = 'ysh';
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

//待审核列表的按钮路径
function material_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $('#outDepotAudit_formAudit #urlPrefix').val() + tourl;
    if(action=="view"){
        var url=tourl+"?ckid="+id;
        $.showDialog(url,'查看出库信息',viewMaterialAuditConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_GOODS_DELIVERY',
            url:tourl,
            data:{'ywzd':'ckid'},
            title:"出库审核",
            preSubmitCheck:'preSubmitputmaterial',
            prefix:$('#outDepotAudit_formAudit #urlPrefix').val(),
            callback:function(){
                searchMaterial_audit_Result(true);//回调
            },
            dialogParam:{width:1500}
        });
    }
}

function preSubmitputmaterial(){
    var json = [];
    if(t_map.rows != null && t_map.rows.length > 0){
        for(var i=0;i<t_map.rows.length;i++){
            if(t_map.rows[i].cksl==null || t_map.rows[i].cksl==''){
                $.alert("出库数量不能为空！");
                return false;
            }
            if(t_map.rows[i].scph==null || t_map.rows[i].scph==''){
                $.alert("生产批号不能为空！");
                return false;
            }
            if(t_map.rows[i].scrq==null || t_map.rows[i].scrq==''){
                $.alert("生产日期不能未空！");
                return false;
            }
            if(t_map.rows[i].yxq==null || t_map.rows[i].yxq==''){
                $.alert("有效期不能为空！");
                return false;
            }
            if(t_map.rows[i].cksl==0){
                $.alert("出库数量不能为0！");
                return false;
            }
            var sz = {"ckmxid":'',"yymxckd":'',"hwid":'',"wlid":'',"sl":'',"kcl":'',"yxq":'',"ckid":'',"kwbh":'',"bz":'',"scrq":'',"scph":'',"htmxid":'',"cpzch":''};
            sz.wlid = t_map.rows[i].wlid;
            sz.hwid = t_map.rows[i].hwid;
            sz.ckmxid = t_map.rows[i].ckmxid;
            sz.yymxckd = t_map.rows[i].yymxckd;
            sz.sl = t_map.rows[i].cksl;
            sz.kcl = t_map.rows[i].cksl;
            sz.yxq = t_map.rows[i].yxq;
            sz.ckid = $("#editOutPunchForm #ck").val();
            sz.kwbh = t_map.rows[i].kwbh;
            sz.bz = t_map.rows[i].bz;
            sz.scrq = t_map.rows[i].scrq;
            sz.scph = t_map.rows[i].scph;
            sz.htmxid = t_map.rows[i].htmxid;
            sz.qgmxid = t_map.rows[i].qgmxid;
            sz.cpzch = t_map.rows[i].cpzch;
            json.push(sz);
        }
        $("#editOutPunchForm #hwxx_json").val(JSON.stringify(json));
    }else{
        $.alert("出库明细信息不能为空！");
        return false;
    }
    return true;
}

var materialAudit_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#outDepotAudit_audit_formSearch #btn_query");//模糊查询（待审核）
        var btn_queryAudited = $("#outDepotAudit_audited_formSearch #btn_query");//模糊查询（审核历史）
        var btn_cancelAudit = $("#outDepotAudit_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
        var btn_view = $("#outDepotAudit_audit_formSearch #btn_view");//查看页面（待审核）
        var btn_audit = $("#outDepotAudit_audit_formSearch #btn_audit");//审核（待审核）

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchMaterial_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchMaterial_audited_Result(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#outDepotAudit_audit_formSearch #outDepotAudit_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                material_audit_DealById(sel_row[0].ckid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#outDepotAudit_audit_formSearch #outDepotAudit_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                material_audit_DealById(sel_row[0].ckid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#outDepotAudit_formAudit #outDepotAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='outDepotAudit_auditing'){
                    var oTable= new MaterialAudit_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new MaterialAudit_audited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #outDepotAudit_audited_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var materialAudit_params=[];
                materialAudit_params.prefix=$('#outDepotAudit_formAudit #urlPrefix').val();
                cancelAudit($('#outDepotAudit_audited_formSearch #outDepotAudit_audited_list').bootstrapTable('getSelections'),function(){
                    searchMaterial_audited_Result();
                },null,materialAudit_params);
            })
        }
        /*-----------------------------------------------------------------------------*/
    }
    return oInit;
}


function searchMaterial_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#outDepotAudit_audit_formSearch #outDepotAudit_auditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#outDepotAudit_audit_formSearch #outDepotAudit_auditing_list').bootstrapTable('refresh');
    }
}

function searchMaterial_audited_Result(isTurnBack){
    if(isTurnBack){
        $('#outDepotAudit_audited_formSearch #outDepotAudit_audited_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#outDepotAudit_audited_formSearch #outDepotAudit_audited_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new MaterialAudit_audit_TableInit();
    oTable.Init();

    var oButtonInit = new materialAudit_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#outDepotAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#outDepotAudit_audited_formSearch .chosen-select').chosen({width: '100%'});
})