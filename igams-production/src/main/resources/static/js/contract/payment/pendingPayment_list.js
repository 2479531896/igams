var pendingPayment_turnOff=true;
var pendingPayment_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#pendingPayment_formSearch #pendingPayment_list').bootstrapTable({
            url: $("#pendingPayment_formSearch #urlPrefix").val()+'/contract/pendingPayment/pageGetListPendingPayment',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#pendingPayment_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"cjrq",					// 排序字段
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
            uniqueId: "fktxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'htnbbh',
                title: '合同内部编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fkfsmc',
                title: '付款方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zje',
                title: '原总金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xzje',
                title: '现总金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wfkje',
                title: '未付金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yfje',
                title: '待付金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '负责人',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '提醒备注',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:pendingPayment_ztformat,
                sortable: true,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                pendingPaymentById(row.htid,'view',$("#pendingPayment_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#pendingPayment_formSearch #pendingPayment_list").colResizable({
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
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "htid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return pendingPaymentSearchData(map);
    };
    return oTableInit;
};

//状态格式化
function pendingPayment_ztformat(value,row,index){
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#pendingPayment_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {

        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#pendingPayment_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{

        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#pendingPayment_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}


function pendingPaymentSearchData(map){
    var cxtj=$("#pendingPayment_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#pendingPayment_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["htnbbh"]=cxnr;
    }else if (cxtj == "2") {
        map["gysmc"] = cxnr;
    }else if (cxtj == "3") {
        map["lrrymc"] = cxnr;
    }

    return map;
}

function pendingPaymentById(id,action,tourl,htids){
    if(!tourl){
        return;
    }
    tourl = $("#pendingPayment_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?htid=" +id;
        $.showDialog(url,'合同详细信息',viewContractConfig);
    }else if(action =='pay'){
        var url=tourl+ "?ids=" +id+"&htids="+htids;
        $.showDialog(url,'合同付款申请',payContractConfig);
    }
}
function preSubmitRecheck(){
    return true;
}


var viewContractConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var payContractConfig = {
    width		: "1500px",
    modalName	: "payContractModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提交",
            className : "btn-primary",
            callback : function() {
                if(!$("#pendingPaymentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                var json=[];
                $("#pendingPaymentForm input[name='access_token']").val($("#ac_tk").val());
                var t_data = $('#pendingPaymentForm #htfk_list').bootstrapTable('getData');
                for(var i=0;i<t_data.length;i++){
                    var sz={"htid":'',"xh":'',"yfbl":'',"yfrq":'',"yfje":'',"fktxid":'',"htnbbh":'',"zje":'',"wfkje":'',"gyskhh":'',"gysyyzh":'',"gysid":'',"ykbm":'',"fkje":'',"jg":'',"fkbfb":'',"xzje":''};
                    sz.htid=t_data[i].htid;
                    sz.xh=i+1;
                    sz.yfbl=t_data[i].yfbl;
                    sz.yfrq=t_data[i].yfrq;
                    sz.yfje=t_data[i].yfje;
                    sz.fktxid=t_data[i].fktxid;
                    sz.htnbbh=t_data[i].htnbbh;
                    sz.wfkje=t_data[i].wfkje;
                    sz.zje=t_data[i].zje;
                    sz.gyskhh=t_data[i].gyskhh;
                    sz.gysyyzh=t_data[i].gysyyzh;
                    sz.gysid=t_data[i].gysid;
                    sz.ykbm=t_data[i].ykbm;
                    sz.fkje=t_data[i].fkje;
                    sz.jg=t_data[i].jg;
                    sz.fkbfb=t_data[i].fkbfb;
                    sz.xzje=t_data[i].xzje;
                    json.push(sz);
                }
                $("#pendingPaymentForm #fkmxJson").val(JSON.stringify(json));

                submitForm(opts["formName"]||"pendingPaymentForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            //新增提交审
                            var payment_params=[];
                            payment_params.prefix=$('#pendingPaymentForm #urlPrefix').val();
                            var auditType = $("#pendingPaymentForm #auditType").val();
                            var ywid = responseText["ywid"];
                            showAuditFlowDialog(auditType,ywid,function(){
                                $.closeModal(opts.modalName);
                                pengdingPaymentResult();
                            },null,payment_params);
                            $.closeModal(opts.modalName);
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
        successtwo : {
            label : "保存",
            className : "btn-success",
            callback : function() {
                if(!$("#pendingPaymentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#pendingPaymentForm input[name='access_token']").val($("#ac_tk").val());
                $("#pendingPaymentForm #fkmxJson").val(JSON.stringify($('#pendingPaymentForm #htfk_list').bootstrapTable('getData')));

                submitForm(opts["formName"]||"pendingPaymentForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                            pengdingPaymentResult();
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

var pendingPayment_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#pendingPayment_formSearch #btn_view");
        var btn_query = $("#pendingPayment_formSearch #btn_query");
        var btn_del = $("#pendingPayment_formSearch #btn_del");
        var btn_pay = $("#pendingPayment_formSearch #btn_pay");

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                pengdingPaymentResult(true);
            });
        }

        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#pendingPayment_formSearch #pendingPayment_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                pendingPaymentById(sel_row[0].htid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /* ---------------------------付款----------------------------------*/
        btn_pay.unbind("click").click(function(){
            var sel_row = $('#pendingPayment_formSearch #pendingPayment_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                if(sel_row[0].zt!="80"){
                    $.alert("该记录合同审核未通过，不允许付款!");
                }else{
                    var ids = "";
                    var htids = "";
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + "," + sel_row[i].fktxid;
                        htids = htids + "," + sel_row[i].htid;
                    }
                    ids = ids.substr(1);
                    htids = htids.substr(1);
                    pendingPaymentById(ids,"pay",btn_pay.attr("tourl"),htids);
                }
            }
        });

        /* ------------------------------删除合同信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#pendingPayment_formSearch #pendingPayment_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fktxid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#pendingPayment_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        pengdingPaymentResult();
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
    };
    return oInit;
};


function pengdingPaymentResult(isTurnBack){
    pendingPayment_turnOff=true;
    if(isTurnBack){
        $('#pendingPayment_formSearch #pendingPayment_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#pendingPayment_formSearch #pendingPayment_list').bootstrapTable('refresh');
    }
}


$(function(){

    // 1.初始化Table
    var oTable = new pendingPayment_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new pendingPayment_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#pendingPayment_formSearch .chosen-select').chosen({width: '100%'});

});