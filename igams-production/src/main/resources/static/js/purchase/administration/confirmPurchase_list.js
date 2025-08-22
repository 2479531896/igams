var PurchaseConfirm_turnOff=true;

var confirmPurchaseAdministration_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#confirmPurchase_xz_formSearch #purchaseConfirm_list").bootstrapTable({
            url: $("#confirmPurchase_xz_formSearch #urlPrefix").val()+'/administration/purchase/pageGetListConfirmPurchaseAdministration',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#confirmPurchase_xz_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qrgl.lrsj",				//排序字段
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
            uniqueId: "qrid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width:'4%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'qrid',
                title: '确认id',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'qrdh',
                title: '确认单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '确认人员',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qrrq',
                title: '确认日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dzfsmc',
                title: '对账方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zfdx',
                title: '支付对象',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zje',
                title: '总金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:confirm_ztFormat,
                sortable: true,
                visible:true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:confirm_czFormat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                confirmPurchaseAdministrationDealById(row.qrid,'view',$("#confirmPurchase_xz_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#confirmPurchase_xz_formSearch #purchaseConfirm_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "qrgl.qrid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return confirmPurchaseAdministrationSearchData(map);
    }
    return oTableInit
}

//操作按钮格式化
function confirm_czFormat(value,row,index){
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallConfirms('" + row.qrid +"','" + row.shlx+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

/**
 * 审核列表的状态格式化函数
 * @returns
 */
function confirm_ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qrid + "\",event,\"AUDIT_ADMINISTRATIONCONFIRMPURCHASE\",{prefix:\"" + $('#confirmPurchase_xz_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qrid + "\",event,\"AUDIT_ADMINISTRATIONCONFIRMPURCHASE\",{prefix:\"" + $('#confirmPurchase_xz_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else if(row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qrid + "\",event,\"AUDIT_ADMINISTRATIONCONFIRMPURCHASE\",{prefix:\"" + $('#confirmPurchase_xz_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
    else{
        return '未提交';
    }
}

//撤回请购单提交
function recallConfirms(qrid,shlx,event){
    var auditType = "AUDIT_ADMINISTRATIONCONFIRMPURCHASE";
    var msg = '您确定要撤回该请购单吗？';
    var confirmPurchase_params = [];
    confirmPurchase_params.prefix = $("#confirmPurchase_xz_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,qrid,function(){
            	searchconfirmPurchaseAdministratioResult();
            },confirmPurchase_params);
        }
    });
}

function confirmPurchaseAdministrationDealById(id,action,tourl){
    var url= tourl;
    if(!tourl){
        return;
    }
    tourl = $("#confirmPurchase_xz_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl+"?qrid="+id;
        $.showDialog(url,'查看确认信息',viewPurchaseConfirmConfig);
    }else if(action == 'mod'){
        var url= tourl+"?qrid="+id;
        $.showDialog(url,'修改确认信息',editPurchaseConfirmConfig);
    }else if(action == 'submit'){
        var url= tourl+"?qrid="+id;
        $.showDialog(url,'提交',submitPurchaseConfirmConfig);
    }
}

var confirmPurchaseAdministratio_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#confirmPurchase_xz_formSearch #btn_query");
        var btn_view=$("#confirmPurchase_xz_formSearch #btn_view");
        var btn_mod=$("#confirmPurchase_xz_formSearch #btn_mod");
        var btn_del=$("#confirmPurchase_xz_formSearch #btn_del");
        var btn_submit=$("#confirmPurchase_xz_formSearch #btn_submit");
        var btn_finish=$("#confirmPurchase_xz_formSearch #btn_finish");
        //添加日期控件
        laydate.render({
            elem: '#confirmPurchase_xz_formSearch #qrrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#confirmPurchase_xz_formSearch #qrrqend'
            ,theme: '#2381E9'
        });
        /*--------------------------------完成---------------------------*/
        btn_finish.unbind("click").click(function(){
            var sel_row = $('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0 || sel_row.length>1){
                $.error("请选中一行");
                return;
            }else {
                $.confirm('您要确定完成所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#confirmPurchase_xz_formSearch #urlPrefix").val()+btn_finish.attr("tourl");
                        jQuery.post(url,{qrid:sel_row[0].qrid,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchconfirmPurchaseAdministratioResult(true);
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
            }
        });
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchconfirmPurchaseAdministratioResult(true);
            });
        }
        /* ------------------------------查看确认信息-----------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                confirmPurchaseAdministrationDealById(sel_row[0].qrid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------提交---------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row=$('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    confirmPurchaseAdministrationDealById(sel_row[0].qrid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.alert("该状态不允许提交!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------高级修改请购信息(审核通过调用) ---------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                confirmPurchaseAdministrationDealById(sel_row[0].qrid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除请购信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].qrid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#confirmPurchase_xz_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchconfirmPurchaseAdministratioResult(true);
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
            }
        });
        // /*-----------------------显示隐藏------------------------------------*/
        // $("#confirmPurchase_xz_formSearch #sl_searchMore").on("click", function(ev){
        //     var ev=ev||event;
        //     if(Purchase_turnOff){
        //         $("#confirmPurchase_xz_formSearch #searchMore").slideDown("low");
        //         Purchase_turnOff=false;
        //         this.innerHTML="基本筛选";
        //     }else{
        //         $("#confirmPurchase_xz_formSearch #searchMore").slideUp("low");
        //         Purchase_turnOff=true;
        //         this.innerHTML="高级筛选";
        //     }
        //     ev.cancelBubble=true;
        //     //showMore();
        // });
    }
    return oInit;
}

var submitPurchaseConfirmConfig={
    width		: "1500px",
    modalName	: "submitPurchaseConfirmModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提交",
            className : "btn-success",
            callback : function() {
                if(!$("#xzComfirmEditForm").valid()){
                    $.alert("所选信息有误！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                if($("#xzComfirmEditForm #qrmx_list").bootstrapTable("getData")==null || $("#xzComfirmEditForm #qrmx_list").bootstrapTable("getData").length<=0){
                    $.alert("明细不能为空！");
                    return false;
                }
                $("#xzComfirmEditForm #qgqrmx_json").val(JSON.stringify($("#xzComfirmEditForm #qrmx_list").bootstrapTable("getData")));

                $("#xzComfirmEditForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"xzComfirmEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                //提交审核
                                var purchaseConfirm_params=[];
                                purchaseConfirm_params.prefix=$('#xzComfirmEditForm #urlPrefix').val();
                                var auditType = $("#xzComfirmEditForm #auditType").val();
                                var ywid= $("#xzComfirmEditForm #qrid").val();
                                showAuditFlowDialog(auditType,ywid,function(){
                                    searchconfirmPurchaseAdministratioResult();
                                },null,purchaseConfirm_params);
                                $.closeModal(opts.modalName);
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
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

var editPurchaseConfirmConfig={
    width		: "1500px",
    modalName	: "editPurchaseConfirmModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保存",
            className : "btn-success",
            callback : function() {
                if(!$("#xzComfirmEditForm").valid()){
                    $.alert("所选信息有误！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                if($("#xzComfirmEditForm #qrmx_list").bootstrapTable("getData")==null || $("#xzComfirmEditForm #qrmx_list").bootstrapTable("getData").length<=0){
                    $.alert("明细不能为空！");
                    return false;
                }
                $("#xzComfirmEditForm #qgqrmx_json").val(JSON.stringify($("#xzComfirmEditForm #qrmx_list").bootstrapTable("getData")));

                $("#xzComfirmEditForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"xzComfirmEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchconfirmPurchaseAdministratioResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
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

/**
 * 查看页面模态框
 */
var viewPurchaseConfirmConfig={
    width		: "1600px",
    modalName	:"viewPurchaseConfirmModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

function confirmPurchaseAdministrationSearchData(map){
    var cxtj=$("#confirmPurchase_xz_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#confirmPurchase_xz_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["qrdh"]=cxnr
    }else if(cxtj=="2"){
        map["hwmc"]=cxnr
    }else if(cxtj=="3"){
        map["djh"]=cxnr
    }else if(cxtj=="4"){
        map["qrrymc"]=cxnr
    }else if(cxtj=="5"){
        map["zfdx"]=cxnr
    }
    // 确认开始日期
    var qrrqstart = jQuery('#confirmPurchase_xz_formSearch #qrrqstart').val();
    map["qrrqstart"] = qrrqstart;
    // 确认结束日期
    var qrrqend = jQuery('#confirmPurchase_xz_formSearch #qrrqend').val();
    map["qrrqend"] = qrrqend;
    //对账方式
    var dzfss=jQuery('#confirmPurchase_xz_formSearch #dzfs_id_tj').val();
    map["dzfss"] = dzfss.replace(/'/g, "");;
    return map;
}

/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchconfirmPurchaseAdministratioResult(isTurnBack){
    //关闭高级搜索条件
    $("#confirmPurchase_xz_formSearch #searchMore").slideUp("low");
    PurchaseConfirm_turnOff=true;
    if(isTurnBack){
        $('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#confirmPurchase_xz_formSearch #purchaseConfirm_list').bootstrapTable('refresh');
    }
}
/**显示隐藏**/
$("#confirmPurchase_xz_formSearch #sl_searchMore").on("click", function(ev){
    var ev=ev||event;
    if(PurchaseConfirm_turnOff){
        $("#confirmPurchase_xz_formSearch #searchMore").slideDown("low");
        PurchaseConfirm_turnOff=false;
        this.innerHTML="基本筛选";
    }else{
        $("#confirmPurchase_xz_formSearch #searchMore").slideUp("low");
        PurchaseConfirm_turnOff=true;
        this.innerHTML="高级筛选";
    }
    ev.cancelBubble=true;
});

$(function(){
    // 1.初始化Table
    var oTable = new confirmPurchaseAdministration_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new confirmPurchaseAdministratio_ButtonInit();
    oButtonInit.Init();
    jQuery('#confirmPurchase_xz_formSearch .chosen-select').chosen({width: '100%'});
})