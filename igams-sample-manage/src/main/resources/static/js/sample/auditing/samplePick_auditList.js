var Pick_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#samplePick_audit_formSearch #samplePick_auditing_list").bootstrapTable({
            url: $("#samplePick_formAudit #urlPrefix").val()+'/sample/auditing/pageGetListYbllsh',
            method: 'get',                      // 请求方式（*）
            toolbar: '#samplePick_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "lldh",				// 排序字段
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
                width: '2%',
                align: 'left',
                visible: false
            },{
                field: 'lldh',
                title: '领料单号',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'llrmc',
                title: '领料人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                pick_audit_DealById(row.llid,'view',$("#samplePick_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#samplePick_audit_formSearch #samplePick_auditing_list").colResizable({
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
        var cxtj=$("#samplePick_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#samplePick_audit_formSearch #cxnr').val());
        if(cxtj=="3"){
            map["entire"]=cxnr;
        }else if(cxtj=="0"){
            map["lldh"]=cxnr;
        }else if(cxtj=="1"){
            map["llrmc"]=cxnr;
        }else if(cxtj=="2"){
            map["bmmc"]=cxnr;
        }
        // 申请开始日期
        var sqrqstart = jQuery('#samplePick_audit_formSearch #sqrqstart').val();
        map["sqrqstart"] = sqrqstart;
        // 申请结束日期
        var sqrqend = jQuery('#samplePick_audit_formSearch #sqrqend').val();
        map["sqrqend"] = sqrqend;

        map["dqshzt"] = "dsh";
        return map;
    };
    return oTableInit;
}


var Pick_audited_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#samplePick_audited_formSearch #samplePick_audited_list").bootstrapTable({
            url: $("#samplePick_formAudit #urlPrefix").val()+'/sample/auditing/pageGetListYbllsh',
            method: 'get',                      // 请求方式（*）
            toolbar: '#samplePick_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "lldh",				// 排序字段
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
                width: '2%',
                align: 'left',
                visible: false
            },{
                field: 'lldh',
                title: '领料单号',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'llrmc',
                title: '领料人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '5%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                pick_audit_DealById(row.llid,'view',$("#samplePick_audited_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#samplePick_audited_formSearch #samplePick_audited_list").colResizable({
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
        var cxtj=$("#samplePick_audited_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#samplePick_audited_formSearch #cxnr').val());
        if(cxtj=="3"){
            map["entire"]=cxnr;
        }else if(cxtj=="0"){
            map["lldh"]=cxnr;
        }else if(cxtj=="1"){
            map["llrmc"]=cxnr;
        }else if(cxtj=="2"){
            map["bmmc"]=cxnr;
        }
        // 申请开始日期
        var sqrqstart = jQuery('#samplePick_audited_formSearch #sqrqstart').val();
        map["sqrqstart"] = sqrqstart;
        // 申请结束日期
        var sqrqend = jQuery('#samplePick_audited_formSearch #sqrqend').val();
        map["sqrqend"] = sqrqend;
        map["dqshzt"] = "ysh";
        return map;
    };
    return oTableInit;
}
//待审核列表的按钮路径
function pick_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $('#samplePick_formAudit #urlPrefix').val() + tourl;
    if(action=="view"){
        var url=tourl+"?llid="+id;
        $.showDialog(url,'查看领料信息',viewMaterialAuditConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_SAMPLE',
            url:tourl,
            data:{'ywzd':'llid'},
            title:"样本领料审核",
            preSubmitCheck:'preSubmitYbll',
            prefix:$('#samplePick_formAudit #urlPrefix').val(),
            callback:function(){
                searchPick_audit_Result(true);//回调
            },
            dialogParam:{width:1500}
        });
    }else if(action =='batchaudit'){
        var url=tourl + "?ywids=" +id+"&shlb=AUDIT_SAMPLE&ywzd=llid&business_url="+tourl;
        $.showDialog(url,'批量审核',batchAuditConfig);
    }
}

var batchAuditConfig = {
    width		: "800px",
    modalName	: "batchAuditModal",
    formName	: "batchAuditAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#batchAuditAjaxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#batchAuditAjaxForm input[name='access_token']").val($("#ac_tk").val());
                var loadYmCode = $("#batchAuditAjaxForm #batchaudit_loadYmCode").val();
                var sel_row = $('#samplePick_audit_formSearch #samplePick_auditing_list').bootstrapTable('getSelections');//获取选择行数据
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
                    //1.启动进度条检测
                    setTimeout("checkAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);

                    //绑定导出取消按钮事件
                    $("#exportCancel").click(function(){
                        //先移除导出提示，然后请求后台
                        if($("#cardiv").length>0) $("#cardiv").remove();
                        $.ajax({
                            type : "POST",
                            url : "/systemcheck/auditProcess/cancelAuditProcess",
                            data : {"loadYmCode" : loadYmCode+"","access_token":$("#ac_tk").val()},
                            dataType : "json",
                            success:function(data){
                                if(data != null && data.result==false){
                                    if(data.msg && data.msg!="")
                                    $.error(data.msg);
                                }
                            }
                        });
                    });
                });
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


//上传后自动提交服务器检查进度
var checkAuditCheckStatus = function(intervalTime,loadYmCode){
    $.ajax({
        type : "POST",
        url : "/systemcheck/auditProcess/commCheckAudit",
        data : {"loadYmCode":loadYmCode,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.cancel){//取消则直接return
                return;
            }
            if(data.result==false){
                if(data.status == "-2"){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if(intervalTime < 5000)
                    intervalTime = intervalTime + 1000;
                    if($("#exportCount")){
                        $("#exportCount").html(data.currentCount)
                    }
                    setTimeout("checkAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
                }
            }else{
                if(data.status == "2"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.success(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchPick_audit_Result();
                    });
                }else if(data.status == "0"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.alert(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchPick_audit_Result();
                    });
                }else{
                    if($("#cardiv")) $("#cardiv").remove();
                    $.error(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchPick_audit_Result();
                    });
                }
            }
        }
    });
}


function preSubmitYbll(){
    let json = [];
    if(t_map.rows != null && t_map.rows.length > 0) {
        var checkJcdw=t_map.rows[0].jcdwmc;
        jcdwdm=t_map.rows[0].jcdwdm;
        for (let i = 0; i < t_map.rows.length; i++) {
            if(checkJcdw!=t_map.rows[i].jcdwmc){
                $.error("明细需选择存储单位一致的数据！");
                return false;
            }
            let sz = {"ybkcid": ''};
            sz.ybkcid = t_map.rows[i].ybkcid;
            json.push(sz);
        }
    }else {
        $.error("明细不允许为空！");
        return false;
    }
    $("#ybpickingCarForm input[name='access_token']").val($("#ac_tk").val());
    $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));
    var $this = this;
    var opts = $this["options"]||{};
    return true;
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
function searchPick_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#samplePick_audit_formSearch #samplePick_auditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#samplePick_audit_formSearch #samplePick_auditing_list').bootstrapTable('refresh');
    }
}

function searchPick_audited_Result(isTurnBack){
    if(isTurnBack){
        $('#samplePick_audited_formSearch #samplePick_audited_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#samplePick_audited_formSearch #samplePick_audited_list').bootstrapTable('refresh');
    }
}
var samplePick_ButtonInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#samplePick_audit_formSearch #btn_query");
        var btn_view = $("#samplePick_audit_formSearch #btn_view");
        var btn_audit = $("#samplePick_audit_formSearch #btn_audit");
        var btn_cancelAudit = $("#samplePick_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
        var btn_queryAudited = $("#samplePick_audited_formSearch #btn_query");
        var btn_batchaudit = $("#samplePick_audit_formSearch #btn_batchaudit");

        //添加日期控件
        laydate.render({
            elem: '#samplePick_audit_formSearch #sqrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#samplePick_audit_formSearch #sqrqend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                auditingResult(true);
            });
        }
        /*-----------------------批量审核------------------------------------*/
        btn_batchaudit.unbind("click").click(function(){
            var sel_row=$('#samplePick_audit_formSearch #samplePick_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].llid;
            }
            ids = ids.substr(1);
            pick_audit_DealById(ids,"batchaudit",btn_batchaudit.attr("tourl"));
        });
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
        /* ---------------------------查看样本信息-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#samplePick_audit_formSearch #samplePick_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                pick_audit_DealById(sel_row[0].llid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------样本领料审核------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#samplePick_audit_formSearch #samplePick_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                pick_audit_DealById(sel_row[0].llid,"audit", btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#samplePick_formAudit #samplePick_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='samplePick_auditing'){
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
                materialAudit_params.prefix=$('#samplePick_formAudit #urlPrefix').val();
                cancelAudit($('#samplePick_audited_formSearch #samplePick_audited_list').bootstrapTable('getSelections'),function(){
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
    var oButtonInit = new samplePick_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#samplePick_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#samplePick_audited_formSearch .chosen-select').chosen({width: '100%'});
});