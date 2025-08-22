var AdminPurchasePay_turnOff=true;
var AdminPurchasePay_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#adminPurchasePay_formSearch #table_list").bootstrapTable({
            url: $("#adminPurchasePay_formSearch #urlPrefix").val()+'/administration/administrationPay/pageGetListAdministrationPay',
            method: 'get',                      // 请求方式（*）
            toolbar: '#adminPurchasePay_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "fkgl.zwfkrq",				// 排序字段
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
            uniqueId: "fkid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'fkid',
                title: '付款ID',
                titleTooltip:'付款ID',
                width: '1%',
                align: 'left',
                visible:false
            },{
                field: 'dzfsmc',
                title: '对账方式',
                titleTooltip:'对账方式',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'fkdh',
                title: '付款单号',
                titleTooltip:'付款单号',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'sqrmc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'zwfkrq',
                title: '最晚支付日期',
                titleTooltip:'最晚支付日期',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'fkje',
                title: '付款金额',
                titleTooltip:'付款金额',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'fkfmc',
                title: '付款方',
                titleTooltip:'付款方',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'lrry',
                title: '录入人员',
                titleTooltip:'录入人员',
                width: '6%',
                align: 'left',
                visible:false,
            },{
                field: 'fkfsmc',
                title: '付款方式',
                titleTooltip:'付款方式',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'zfdx',
                title: '支付对象',
                titleTooltip:'支付对象',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'zffkhh',
                title: '支付方开户行',
                titleTooltip:'支付方开户行',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'zffyhzh',
                title: '支付方银行账号',
                titleTooltip:'支付方银行账号',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'fygsmc',
                title: '费用归属',
                titleTooltip:'费用归属',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'fksy',
                title: '付款事由',
                titleTooltip:'付款事由',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'fylbmc',
                title: '费用类别',
                titleTooltip:'费用类别',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'zt',
                title: '状态',
                titleTooltip:'状态',
                width: '6%',
                align: 'left',
                formatter:adminPurchasePay_ztformat,
                visible:true,
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:adminPurchasePay_czFormat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                AdminPurchasePay_DealById(row.fkid,'view',$("#adminPurchasePay_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#adminPurchasePay_formSearch #table_list").colResizable({
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
            sortLastName: "fkgl.fkje", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getAdminPurchasePaySearchData(map);
    };
    return oTableInit;
}

//提供给导出用的回调函数
function adminPurchasePaySearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="fkgl.fkid";
    map["sortLastOrder"]="asc";
    map["sortName"]="fkgl.lrsj";
    map["sortOrder"]="asc";
    map["yhid"]=$("#yhid").val();
    return getAdminPurchasePaySearchData(map);
}

function adminPurchasePay_ztformat(value,row,index){
    var dzfsdm=row.dzfsdm;
    var audittype="";
    if(dzfsdm=="PTP"){
        audittype=$("#adminPurchasePay_formSearch #auditTypePTP").val();
    }else{
        audittype=$("#adminPurchasePay_formSearch #auditType").val();
    }
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fkid + "\",event,\""+audittype+"\",{prefix:\"" + $('#adminPurchasePay_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fkid + "\",event,\""+audittype+"\",{prefix:\"" + $('#adminPurchasePay_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else if(row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fkid + "\",event,\""+audittype+"\",{prefix:\"" + $('#adminPurchasePay_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
    else{
        return '未提交';
    }
}

function adminPurchasePay_czFormat(value,row,index){
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallPays('" + row.fkid +"','" + row.shlx+ "',event,'" + row.dzfsdm+ "')\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回付款单提交
function recallPays(fkid,shlx,event,dzfsdm){
    var auditType ="";
    if(dzfsdm=="PTP"){
        auditType=$("#adminPurchasePay_formSearch #auditTypePTP").val();
    }else{
        auditType=$("#adminPurchasePay_formSearch #auditType").val();
    }
    var msg = '您确定要撤回该请购单吗？';
    var payPurchase_params = [];
    payPurchase_params.prefix = $("#adminPurchasePay_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fkid,function(){
                searchAdminPurchasePayResult();
            },payPurchase_params);
        }
    });
}


function getAdminPurchasePaySearchData(map){
    var cxtj=$("#adminPurchasePay_formSearch #adminPurchasePay_select").val();
    var cxnr=$.trim(jQuery('#adminPurchasePay_formSearch #adminPurchasePay_input').val());
    if(cxtj=="0"){
        map["fkdh"]=cxnr
    }else if(cxtj=="1"){
        map["dzfsmc"]=cxnr
    }else if(cxtj=="2"){
        map["zfdx"]=cxnr
    }else if(cxtj=="3"){
        map["sqrmc"]=cxnr
    }else if(cxtj=="4"){
        map["sqbmmc"]=cxnr
    }
    //费用类别
    var fylbs=jQuery('#adminPurchasePay_formSearch #fylb_id_tj').val();
    map["fylbs"] = fylbs.replace(/'/g, "");;
    //付款方式
    var fkfss=jQuery('#adminPurchasePay_formSearch #fkfs_id_tj').val();
    map["fkfss"] = fkfss.replace(/'/g, "");;
    // 创建开始日期
    var fkrqstart = jQuery('#adminPurchasePay_formSearch #fkrqstart').val();
    map["fkrqstart"] = fkrqstart;
    // 创建结束日期
    var fkrqend = jQuery('#adminPurchasePay_formSearch #fkrqend').val();
    map["fkrqend"] = fkrqend;
    return map;
}

function searchAdminPurchasePayResult(isTurnBack){
    if(isTurnBack){
        $('#adminPurchasePay_formSearch #table_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#adminPurchasePay_formSearch #table_list').bootstrapTable('refresh');
    }
}
function AdminPurchasePay_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#adminPurchasePay_formSearch #urlPrefix").val() + tourl;
    if(action=="view"){
        var url=tourl+"?fkid="+id
        $.showDialog(url,'查看行政请购付款信息',viewAdminPurchasePayConfig);
    }else if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增行政请购付款信息',addAdminPurchasePayConfig);
    }else if(action=="mod"){
        var url=tourl+"?fkid="+id
        $.showDialog(url,'修改行政请购付款信息',modAdminPurchasePayConfig);
    }else if(action=="del"){
        var url=tourl+"?fkid="+id
        $.showDialog(url,'删除行政请购付款信息',delAdminPurchasePayConfig);
    }else if(action =='submit'){
        var url=tourl + "?fkid=" +id;
        $.showDialog(url,'提交行政请购付款信息',submitAdminPurchasePayConfig);
    }
}
//提交页面模态框
var submitAdminPurchasePayConfig = {
    width		: "1400px",
    modalName	: "submitAdminPurchasePayModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提交",
            className : "btn-primary",
            callback : function() {
                if(!$("#xzqgfkEditForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#xzqgfkEditForm input[name='access_token']").val($("#ac_tk").val());
                if(t_map.rows != null && t_map.rows.length > 0){
                    $("#xzqgfkEditForm #fkmxJson").val(JSON.stringify(t_map.rows));
                }

                submitForm(opts["formName"]||"xzqgfkEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            //新增提交审
                            var confirmPay_params=[];
                            confirmPay_params.prefix=$('#xzqgfkEditForm #urlPrefix').val();
                            var dzfsid=$("#xzqgfkEditForm #dzfs").val();
                            var dzfsdm=$("#xzqgfkEditForm #"+dzfsid).attr("csdm");
                            var auditType = $("#xzqgfkEditForm #auditType").val();
                            if(dzfsdm=='PTP'){
                                auditType=$("#xzqgfkEditForm #auditTypePTP").val();
                            }
                            var ywid = $("#xzqgfkEditForm #fkid").val();
                            showAuditFlowDialog(auditType,ywid,function(){
                                $.closeModal(opts.modalName);
                                searchAdminPurchasePayResult();
                            },null,confirmPay_params);
                        }
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
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
//新增页面模态框
var addAdminPurchasePayConfig = {
    width		: "1400px",
    modalName	:"addAdminPurchasePayModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#xzqgfkEditForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#xzqgfkEditForm #fkmxJson").val(JSON.stringify($("#xzqgfkEditForm #xzfkmx_list").bootstrapTable("getData")));
                $("#xzqgfkEditForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"xzqgfkEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                //新增提交审
                                var auditType = $("#xzqgfkEditForm #auditType").val();
                                var dzfsid=$("#xzqgfkEditForm #dzfs").val();
                                var dzfsdm=$("#xzqgfkEditForm #"+dzfsid).attr("csdm");
                                if(dzfsdm=='PTP'){
                                    auditType=$("#xzqgfkEditForm #auditTypePTP").val();
                                }
                                var xzpayment_params=[];
                                xzpayment_params.prefix=$('#xzqgfkEditForm #urlPrefix').val();
                                var ywid = responseText["ywid"];
                                showAuditFlowDialog(auditType,ywid,function(){
                                    $.closeModal(opts.modalName);
                                    searchAdminPurchasePayResult();
                                },null,xzpayment_params);
                            }
                            $.closeModal(opts.modalName);
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
//修改页面模态框
var modAdminPurchasePayConfig = {
    width		: "1400px",
    modalName	:"modAdminPurchasePayModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#xzqgfkEditForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#xzqgfkEditForm input[name='access_token']").val($("#ac_tk").val());
                $("#xzqgfkEditForm #fkmxJson").val(JSON.stringify($("#xzqgfkEditForm #xzfkmx_list").bootstrapTable("getData")));
                submitForm(opts["formName"]||"xzqgfkEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAdminPurchasePayResult();
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
//查看页面模态框
var viewAdminPurchasePayConfig = {
    width		: "1200px",
    modalName	:"viewAdminPurchasePayModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var AdminPurchasePay_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#adminPurchasePay_formSearch #btn_query");
        var btn_add = $("#adminPurchasePay_formSearch #btn_add");
        var btn_mod = $("#adminPurchasePay_formSearch #btn_mod");
        var btn_view = $("#adminPurchasePay_formSearch #btn_view");
        var btn_del = $("#adminPurchasePay_formSearch #btn_del");
        var btn_submit = $("#adminPurchasePay_formSearch #btn_submit");
        var btn_selectexport = $("#adminPurchasePay_formSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#adminPurchasePay_formSearch #btn_searchexport");//搜索导出
        //添加日期控件
        laydate.render({
            elem: '#fkrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#fkrqend'
            ,theme: '#2381E9'
        });
        /*-----------------------选中导出------------------------------------*/
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#adminPurchasePay_formSearch #table_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fkid;
                }
                ids = ids.substr(1);
                $.showDialog($('#adminPurchasePay_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ADMINPURCHASEPAY_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
            }else{
                $.error("请选择数据");
            }
        });
        /*-----------------------搜索导出------------------------------------*/
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#adminPurchasePay_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ADMINPURCHASEPAY_SEARCH&expType=search&callbackJs=adminPurchasePaySearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /*-----------------------提交-----------------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#adminPurchasePay_formSearch #table_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    AdminPurchasePay_DealById(sel_row[0].fkid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.alert("该状态不允许提交!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchAdminPurchasePayResult(true);
            });
        }
        /*-----------------------新增------------------------------------*/
        btn_add.unbind("click").click(function(){
            AdminPurchasePay_DealById(null,"add",btn_add.attr("tourl"));
        });
        /*-----------------------修改------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#adminPurchasePay_formSearch #table_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    AdminPurchasePay_DealById(sel_row[0].fkid,"mod",btn_mod.attr("tourl"));
                }else{
                    $.alert("该记录在审核中或已审核通过，不允许修改!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#adminPurchasePay_formSearch #table_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                AdminPurchasePay_DealById(sel_row[0].fkid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------删除------------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#adminPurchasePay_formSearch #table_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fkid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#adminPurchasePay_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchAdminPurchasePayResult();
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
        /**显示隐藏**/
        $("#adminPurchasePay_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(AdminPurchasePay_turnOff){
                $("#adminPurchasePay_formSearch #searchMore").slideDown("low");
                AdminPurchasePay_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#adminPurchasePay_formSearch #searchMore").slideUp("low");
                AdminPurchasePay_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}


$(function(){
    var oTable = new AdminPurchasePay_TableInit();
    oTable.Init();

    var oButton = new AdminPurchasePay_oButton();
    oButton.Init();

    jQuery('#adminPurchasePay_formSearch .chosen-select').chosen({width: '100%'});
})