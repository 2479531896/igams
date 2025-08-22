var Invoice_turnOff=true;

var Invoice_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#invoice_formSearch #invoice_list').bootstrapTable({
            url: $("#invoice_formSearch #urlPrefix").val()+'/invoice/invoice/pageGetListInvoice',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#invoice_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"fpgl.lrsj",					// 排序字段
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
            uniqueId: "fpid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
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
                field: 'fpid',
                title: '发票ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fph',
                title: '发票号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fpzlmc',
                title: '发票种类',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kpje',
                title: '发票总金额',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kprq',
                title: '开票日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bizmc',
                title: '币种',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sl',
                title: '税率',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ywymc',
                title: '申请人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'u8rkdh',
                title: 'U8入库单号',
                width: '10%',
                align: 'left',
                formatter:u8rkdhformat,
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                title: '交给财务时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable:true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ztFormat,
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '7%',
                align: 'left',
                formatter:czFormat,
                visible: true
            }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                InvoiceDealById(row.fpid,'view',$("#invoice_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#invoice_formSearch #invoice_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的2333
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fpgl.fpid", // 防止同名排位用
            sortLastOrder: "desc",// 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return invoiceSearchData(map);
    };
    return oTableInit;
};

/**
 * U8入库单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function u8rkdhformat(value,row,index){
    var html="";
    if(row.u8rkdh!=null&&row.u8rkdh!=''){
        var split = row.u8rkdh.split(',');
        if(split!=null&&split.length>0){
            for(var i=0;i<split.length;i++){
                html+="<span class='col-md-12 col-sm-12'>";
                var split1 = split[i].split("-");
                if(split1[3]!=null&&split1[3]!=''){
                    if(split1[0]!=null&&split1[0]!=''){
                        html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoFromDhdh('"+split1[2]+"')\">"+split1[0]+"</a>";
                    }
                }else{
                    if(split1[0]!=null&&split1[0]!=''){
                        html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoFromRkid('"+split1[1]+"')\">"+split1[0]+"</a>";
                    }
                }
                html+="</span>";
            }
        }
    }
    return html;
}

function getInfoFromDhdh(dhid){
    var url=$("#invoice_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到货信息',viewDhConfig);
}
var viewDhConfig = {
    width		: "1600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function getInfoFromRkid(rkid){
    var url=$("#invoice_formSearch #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'入库信息',viewHwConfig);
}
var viewHwConfig={
    width		: "1600px",
    modalName	:"viewHwModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fpid + "\",event,\"AUDIT_INVOICE\",{prefix:\"" + $('#invoice_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fpid + "\",event,\"AUDIT_INVOICE\",{prefix:\"" + $('#invoice_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fpid + "\",event,\"AUDIT_INVOICE\",{prefix:\"" + $('#invoice_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallInvoice('" + row.fpid +"','" + row.shlx+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallInvoice(fpid,event){
    var auditType = $("#invoice_formSearch #auditType").val();
    var msg = '您确定要撤回该发票吗？';
    var purchase_params = [];
    purchase_params.prefix = $("#invoice_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fpid,function(){
                searchInvoiceResult();
            },purchase_params);
        }
    });
}

function invoiceSearchData(map){
    var cxtj=$("#invoice_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#invoice_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["fph"]=cxnr
    }else if(cxtj=="2"){
        map["gysmc"]=cxnr
    }else if(cxtj=="3"){
        map["ywymc"]=cxnr
    }else if(cxtj=="4"){
        map["htnbbh"]=cxnr
    }

    // 发票种类
    var fpzls = jQuery('#invoice_formSearch #fpzl_id_tj').val();
    map["fpzls"] = fpzls;
    // 状态
    var zts = jQuery('#invoice_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    // 申请开始日期
    var kprqstart = jQuery('#invoice_formSearch #kprqstart').val();
    map["kprqstart"] = kprqstart;
    // 申请结束日期
    var kprqend = jQuery('#invoice_formSearch #kprqend').val();
    map["kprqend"] = kprqend;
    return map;
}

function InvoiceDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#invoice_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?fpid=" +id;
        $.showDialog(url,'详细信息',viewInvoiceConfig);
    }else if(action == 'add'){
        var url= tourl;
        $.showDialog(url,'新增',editInvoiceConfig);
    }else if(action == 'mod'){
        var url= tourl + "?fpid=" +id;
        $.showDialog(url,'修改',editInvoiceConfig);
    }else if(action == 'submit'){
        var url= tourl + "?fpid=" +id;
        $.showDialog(url,'提交',submitInvoiceConfig);
    }
}

var editInvoiceConfig = {
    width		: "1550px",
    modalName	: "editInvoiceModel",
    formName	: "editInvoiceForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#editInvoiceForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }

                var fpmxJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
                if(fpmxJson.length!=0) {
                    for (var i = 0; i < fpmxJson.length; i++) {
                        if(parseFloat(fpmxJson[i].sl).toFixed(2) - parseFloat(fpmxJson[i].wwhsl).toFixed(2) > 0){
                            $.alert("第"+(i+1)+"行数量超过未维护数量！");
                            return false;
                        }
                    }
                }  else{
                    $.alert("明细不允许为空！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editInvoiceForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editInvoiceForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchInvoiceResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
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

var submitInvoiceConfig = {
    width		: "1550px",
    modalName	: "editInvoiceModel",
    formName	: "editInvoiceForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#editInvoiceForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }

                var fpmxJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
                if(fpmxJson.length!=0) {
                    for (var i = 0; i < fpmxJson.length; i++) {
                        if(parseFloat(fpmxJson[i].sl).toFixed(2) - parseFloat(fpmxJson[i].wwhsl).toFixed(2) > 0){
                            $.alert("第"+(i+1)+"行数量超过未维护数量！");
                            return false;
                        }
                    }
                }  else{
                    $.alert("明细不允许为空！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#editInvoiceForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editInvoiceForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                            	var invoice_params=[];
                            	invoice_params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                	$.closeModal(opts.modalName);
                                	searchInvoiceResult();
								},null,invoice_params);
                            }else{
                                searchInvoiceResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
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
var viewInvoiceConfig={
    width		: "1400px",
    modalName	:"viewInvoiceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var Invoice_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query=$("#invoice_formSearch #btn_query");
        var btn_add=$("#invoice_formSearch #btn_add");
        var btn_view=$("#invoice_formSearch #btn_view");
        var btn_mod=$("#invoice_formSearch #btn_mod");
        var btn_del=$("#invoice_formSearch #btn_del");
        var btn_submit=$("#invoice_formSearch #btn_submit");
        var btn_searchexport = $("#invoice_formSearch #btn_searchexport");
        var btn_selectexport = $("#invoice_formSearch #btn_selectexport");
        var btn_batchsubmit = $("#invoice_formSearch #btn_batchsubmit");
        //添加日期控件
        laydate.render({
            elem: '#invoice_formSearch #kprqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#invoice_formSearch #kprqend'
            ,theme: '#2381E9'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchInvoiceResult(true);
            });
        }



        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#invoice_formSearch #invoice_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                InvoiceDealById(sel_row[0].fpid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#invoice_formSearch #invoice_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    InvoiceDealById(sel_row[0].fpid,"mod",btn_mod.attr("tourl"));
                }else{
                    $.error("请选择状态为未提交或者未通过的记录！")
                    return;
                }

            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提交-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#invoice_formSearch #invoice_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    InvoiceDealById(sel_row[0].fpid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.error("请选择状态为未提交或者未通过的记录！")
                    return;
                }

            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------新增-----------------------------*/
        btn_add.unbind("click").click(function(){
            InvoiceDealById(null,"add",btn_add.attr("tourl"));
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#invoice_formSearch #invoice_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fpid;
                }
                ids = ids.substr(1);
                $.showDialog($('#invoice_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=INVOICE_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#invoice_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=INVOICE_SEARCH&expType=search&callbackJs=InvoiceSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#invoice_formSearch #invoice_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fpid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#invoice_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchInvoiceResult();
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
        });
        /* ------------------------------批量提交-----------------------------*/
        btn_batchsubmit.unbind("click").click(function(){
            var sel_row = $('#invoice_formSearch #invoice_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(sel_row[i].zt=='00'||sel_row[i].zt=='15'){
                    ids = ids + ","+ sel_row[i].fpid;
                }else{
                    $.error("请选择未提交或者审核失败的数据！");
                    return;
                }
            }
            ids = ids.substr(1);
            $.confirm('您确定要进行批量提交吗？',function(result){
                if(result){
                    var auditType = $("#invoice_formSearch #auditType").val();
                    var ywids=ids.split(",");
                    //提交审核
                    if(auditType!=null){
                        var invoice_params=[];
                        invoice_params.prefix= $('#invoice_formSearch #urlPrefix').val();
                        showAuditFlowDialog(auditType,ywids,function(){
                            searchInvoiceResult();
                        },null,invoice_params);
                    }else{
                        searchInvoiceResult();
                    }
                }
            });
        });
        /**显示隐藏**/
        $("#invoice_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Invoice_turnOff){
                $("#invoice_formSearch #searchMore").slideDown("low");
                Invoice_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#invoice_formSearch #searchMore").slideUp("low");
                Invoice_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};

//提供给导出用的回调函数
function InvoiceSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="fpmx.fpmxid";
    map["sortLastOrder"]="desc";
    map["sortName"]="fpgl.lrsj";
    map["sortOrder"]="desc";
    return invoiceSearchData(map);
}


function searchInvoiceResult(isTurnBack){
    //关闭高级搜索条件
    $("#invoice_formSearch #searchMore").slideUp("low");
    Invoice_turnOff=true;
    $("#invoice_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#invoice_formSearch #invoice_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#invoice_formSearch #invoice_list').bootstrapTable('refresh');
    }
}

$(function(){

    // 1.初始化Table
    var oTable = new Invoice_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Invoice_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#invoice_formSearch .chosen-select').chosen({width: '100%'});

    $("#invoice_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});