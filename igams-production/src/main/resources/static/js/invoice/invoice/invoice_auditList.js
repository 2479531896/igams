var invoice_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#invoice_audit_formSearch #invoice_audit_list").bootstrapTable({
            url: $("#invoice_formAudit #urlPrefix").val()+'/invoice/invoice/pageGetListInvoiceAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#invoice_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "kprq",				//排序字段
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
            uniqueId: "fpid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
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
                formatter: fphformat,
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
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kprq',
                title: '开票日期',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '12%',
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
                width: '8%',
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
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '15%',
                align: 'left',
                sortable: false,
                visible: false
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '15%',
                align: 'left',
                visible: true,
                sortable: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                invoice_audit_DealById(row.fpid,"view",$("#invoice_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#invoice_audit_formSearch #invoice_audit_list").colResizable({
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
            sortLastName: "fpid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#invoice_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#invoice_audit_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["fph"]=cxnr
        }else if(cxtj=="1"){
            map["gysmc"]=cxnr
        }else if(cxtj=="2"){
            map["dddwmc"]=cxnr
        }else if(cxtj=="3"){
            map["bmmc"]=cxnr
        }
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

/**
 * 发票号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fphformat(value,row,index){
    var html="";
    if(row.fph!=null&&row.fph!=''){
        html+="<span class='col-md-12 col-sm-12'>";
        html += "<a href='javascript:void(0);' onclick=\"queryByFpid('"+row.fpid+"')\">"+row.fph+"</a>";
        html+="</span>";
    }
    return html;
}

function queryByFpid(fpid){
    var url=$("#invoice_formAudit #urlPrefix").val()+"/invoice/invoice/pagedataAuditInvoice?fpid="+fpid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'发票详情',viewFpConfig);
}
var viewFpConfig = {
    width		: "1400px",
    modalName	:"viewInvoiceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var invoiceAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#invoice_audit_audited #tb_list").bootstrapTable({
            url:$("#invoice_formAudit #urlPrefix").val()+'/invoice/invoice/pageGetListInvoiceAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#invoice_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
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
            uniqueId: "shxx_shxxid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#invoice_audit_audited #tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
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
                formatter: fphformat,
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
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kprq',
                title: '开票日期',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '12%',
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
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '10%',
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
                width: '15%',
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
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                invoice_audit_DealById(row.fpid,"view",$("#invoice_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#invoice_audit_audited #tb_list").colResizable({
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
            sortLastName: "fpid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#invoice_audit_audited #cxtj").val();
        var cxnr=$.trim(jQuery('#invoice_audit_audited #cxnr').val());
        if(cxtj=="0"){
            map["fph"]=cxnr
        }else if(cxtj=="1"){
            map["gysmc"]=cxnr
        }else if(cxtj=="2"){
            map["dddwmc"]=cxnr
        }else if(cxtj=="3"){
            map["bmmc"]=cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

function invoice_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#invoice_formAudit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?fpid="+id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_INVOICE',
            url:tourl,
            data:{'ywzd':'fpid'},
            title:"发票审核",
            preSubmitCheck:'preSubmitInvoice',
            prefix:$('#invoice_formAudit #urlPrefix').val(),
            callback:function(){
                searchInvoice_audit_Result(true);//回调
            },
            dialogParam:{width:1550}
        });
    }else if(action =='batchaudit'){
        var url=tourl + "?ywids=" +id+"&shlb=AUDIT_INVOICE&ywzd=fpid&business_url="+tourl;
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
                var sel_row = $('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('getSelections');//获取选择行数据
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
                    //1.启动进度条检测
                    setTimeout("checkInvoiceAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);

                    //绑定导出取消按钮事件
                    $("#exportCancel").click(function(){
                        //先移除导出提示，然后请求后台
                        if($("#cardiv").length>0) $("#cardiv").remove();
                        $.ajax({
                            type : "POST",
                            url : $('#invoice_formAudit #urlPrefix').val() + "/systemcheck/auditProcess/cancelAuditProcess",
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
                        html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByDhdh('"+split1[2]+"')\">"+split1[0]+"</a>";
                    }
                }else{
                    if(split1[0]!=null&&split1[0]!=''){
                        html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByRkid('"+split1[1]+"')\">"+split1[0]+"</a>";
                    }
                }
                html+="</span>";
            }
        }
    }
    return html;
}
function getInfoByDhdh(dhid){
    var url=$("#invoice_formAudit #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
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

function getInfoByRkid(rkid){
    var url=$("#invoice_formAudit #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
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

//上传后自动提交服务器检查进度
var checkInvoiceAuditCheckStatus = function(intervalTime,loadYmCode){
    $.ajax({
        type : "POST",
        url : $("#invoice_formAudit #urlPrefix").val()+"/systemcheck/auditProcess/commCheckAudit",
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
                    setTimeout("checkInvoiceAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
                }
            }else{
                if(data.status == "2"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.success(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchInvoice_audit_Result();
                    });
                }else if(data.status == "0"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.alert(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchInvoice_audit_Result();
                    });
                }else{
                    if($("#cardiv")) $("#cardiv").remove();
                    $.error(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchInvoice_audit_Result();
                    });
                }
            }
        }
    });
};

function preSubmitInvoice(){
    return true;
}

var invoice_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#invoice_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#invoice_audit_audited #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#invoice_audit_audited #btn_cancelAudit");//取消审核
        var btn_view = $("#invoice_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#invoice_audit_formSearch #btn_audit");//审核
        var btn_batchaudit = $("#invoice_audit_formSearch #btn_batchaudit");

        //批量审核
        btn_batchaudit.unbind("click").click(function(){
            var sel_row = $('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fpid;
            }
            ids = ids.substr(1);
            invoice_audit_DealById(ids,"batchaudit",btn_batchaudit.attr("tourl"));
        });
        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchInvoice_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchInvoiceAudited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                invoice_audit_DealById(sel_row[0].fpid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                invoice_audit_DealById(sel_row[0].fpid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#invoice_formAudit #invoice_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='invoice_auditing'){
                    var oTable= new invoice_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new invoiceAudited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var putInStorage_params=[];
                putInStorage_params.prefix=$('#invoice_formAudit #urlPrefix').val();
                cancelAudit($('#invoice_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
                    searchInvoiceAudited();
                },null,putInStorage_params);
            })
        }
    }
    return oInit;
}

var viewConfig = {
    width		: "1400px",
    modalName	:"viewConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchInvoice_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#invoice_audit_formSearch #invoice_audit_list').bootstrapTable('refresh');
    }
}

function searchInvoiceAudited(isTurnBack){
    if(isTurnBack){
        $('#invoice_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#invoice_audit_audited #tb_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new invoice_audit_TableInit();
    oTable.Init();

    var oButtonInit = new invoice_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#invoice_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#invoice_audit_audited .chosen-select').chosen({width: '100%'});
});