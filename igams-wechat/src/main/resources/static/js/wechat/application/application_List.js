var application_turnOff=true;
var application_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#application_formSearch #application_list').bootstrapTable({
            url: '/application/application/pageGetListApplication',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#application_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"tssq.lrsj",					// 排序字段
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
            uniqueId: "tssqid",                     // 每一行的唯一标识，一般为主键列
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
                field: 'tssqid',
                title: '特殊申请ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'hzxm',
                title: '患者姓名',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'xbmc',
                title: '性别',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'dwjc',
                title: '单位简称',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'yblxmc',
                title: '标本类型',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'jcxmmc',
                title: '检测项目',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'jcdwmc',
                title: '检测单位',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'db',
                title: '合作伙伴',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'bgrq',
                title: '报告日期',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'lrsj',
                title: '申请日期',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'sqxmmc',
                title: '申请项目',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'sqzxmmc',
                title: '申请子项目',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'sqyymc',
                title: '申请原因',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'qtsqyy',
                title: '其他原因',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'clbj',
                title: '处理标记',
                width: '5%',
                align: 'left',
                formatter:clbjformat,
                sortable:true,
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'zt',
                title: '状态',
                width: '8%',
                formatter:ztFormat,
                align: 'left',
                visible: true
            },{
                field: 'cz',
                title: '操作',
                width: '5%',
                align: 'left',
                formatter:czformat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                applicationById(row.tssqid,'view',$("#application_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#application_formSearch #application_list").colResizable({
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
            sortLastName: "tssqid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return applicationSearchData(map);
    };
    return oTableInit;
};
function applicationSearchData(map){
    var cxtj=$("#application_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#application_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["ybbh"]=cxnr;
    }else if(cxtj=="2"){
        map["nbbm"]=cxnr;
    }else if(cxtj=="3"){
        map["hzxm"]=cxnr;
    }else if(cxtj=="4"){
        map["db"]=cxnr;
    }else if(cxtj=="5"){
        map["jcdwmc"]=cxnr;
    }

    // 报告日期开始日期
    var bgrqstart = jQuery('#application_formSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    // 报告日期结束日期
    var bgrqend = jQuery('#application_formSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;
    // 申请日期开始日期
    var sqrqstart = jQuery('#application_formSearch #sqrqstart').val();
    map["sqrqstart"] = sqrqstart;
    // 申请日期结束日期
    var sqrqend = jQuery('#application_formSearch #sqrqend').val();
    map["sqrqend"] = sqrqend;
    var yblxs = jQuery('#application_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    var jcxm=jQuery('#application_formSearch #jcxm_id_tj').val()
    map["jcxms"]=jcxm;
    var sqyy=jQuery('#application_formSearch #sqyy_id_tj').val()
    map["sqyys"]=sqyy;
    var sqxm=jQuery('#application_formSearch #sqxm_id_tj').val()
    map["sqxms"]=sqxm;
    var clbj=jQuery('#application_formSearch #clbj_id_tj').val()
    map["clbj"]=clbj;
    var jcdws=jQuery('#application_formSearch #jcdw_id_tj').val()
    map["jcdws"]=jcdws;
    var load_flag=jQuery('#application_formSearch #load_flag').val()
    map["load_flag"]=load_flag;
    return map;
}

//提供给导出用的回调函数
function DcApplicationSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="sj.bgrq";
    map["sortLastOrder"]="desc";
    map["sortName"]="tssq.lrsj";
    map["sortOrder"]="desc";
    return applicationSearchData(map);
}
function clbjformat(value,row,index){
    if(row.clbj==1){
        var clbj="<span style='color:green;'>"+"已处理"+"</span>";
    }else if(row.clbj==0){
        var clbj="<span style='color:orange;'>"+"未处理"+"</span>";
    }else if(row.clbj==2){
        var clbj="<span style='color:red;'>"+"已驳回"+"</span>";
    }
    return clbj;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czformat(value,row,index) {
    if (row.zt == '10'&&row.sqxmdm=='FREE' ) {
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.tssqid +"','" + "AUDIT_FREESAMPLES"+ "',event)\" >撤回</span>";
    }else if(row.zt == '10'&&row.sqxmdm=='VIP'){
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.tssqid +"','" + "AUDIT_VIPSAMPLES"+ "',event)\" >撤回</span>";
    }else if(row.zt == '10'&&row.sqxmdm=='PK'){
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.tssqid +"','" + "AUDIT_PKSAMPLES"+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}


function recallRequisitions(tssqid,auditType,event){
    var msg = '您确定要撤回该记录吗？';
    var purchase_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,tssqid,function(){
                applicationResult();
            },purchase_params);
        }
    });
}

function ztFormat(value,row,index) {
    if("FREE"==row.sqxmdm){
        if (row.zt == '00') {
            return '未提交';
        }else if (row.zt == '80') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_FREESAMPLES\")' >审核通过</a>";
        }else if (row.zt == '15') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_FREESAMPLES\")' >审核未通过</a>";
        }else if(row.zt == '10'){
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_FREESAMPLES\")' >" + row.shxx_dqgwmc + "审核中</a>";
        }
        else{
            return '未提交';
        }
    }else if("VIP"==row.sqxmdm){
        if (row.zt == '00') {
            return '未提交';
        }else if (row.zt == '80') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_VIPSAMPLES\")' >审核通过</a>";
        }else if (row.zt == '15') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_VIPSAMPLES\")' >审核未通过</a>";
        }else if(row.zt == '10'){
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_VIPSAMPLES\")' >" + row.shxx_dqgwmc + "审核中</a>";
        }
        else{
            return '未提交';
        }
    }else if("PK"==row.sqxmdm){
        if (row.zt == '00') {
            return '未提交';
        }else if (row.zt == '80') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_PKSAMPLES\")' >审核通过</a>";
        }else if (row.zt == '15') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_PKSAMPLES\")' >审核未通过</a>";
        }else if(row.zt == '10'){
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.tssqid + "\",event,\"AUDIT_PKSAMPLES\")' >" + row.shxx_dqgwmc + "审核中</a>";
        }
        else{
            return '未提交';
        }
    }

}

function applicationById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?tssqid=" +id;
        $.showDialog(url,'查看详情',viewApplicationConfig);
    }else  if(action =='mod'){
        var url= tourl + "?tssqid=" +id;
        $.showDialog(url,'修改',modApplicationConfig);
    }else  if(action =='submit'){
        var url= tourl + "?tssqid=" +id;
        $.showDialog(url,'提交',submitApplicationConfig);
    }else  if(action =='reject'){
        var url= tourl + "?tssqid=" +id;
        $.showDialog(url,'驳回',rejectApplicationConfig);
    }
}

var viewApplicationConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var rejectApplicationConfig = {
    width		: "1000px",
    modalName	:"applicationModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#rejectApplicationForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#rejectApplicationForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"rejectApplicationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            applicationResult();
                        });
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

var modApplicationConfig = {
    width		: "1000px",
    modalName	:"applicationModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#applicationForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#applicationForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"applicationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            applicationResult();
                        });
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

var submitApplicationConfig = {
    width		: "1000px",
    modalName	:"applicationModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#applicationForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#applicationForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"applicationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                            	showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    applicationResult();
                            	});
                            }else{
                                applicationResult();
                            }
                        });
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

var application_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#application_formSearch #btn_view");
        var btn_query = $("#application_formSearch #btn_query");
        var btn_add = $("#application_formSearch #btn_add");
        var btn_del = $("#application_formSearch #btn_del");
        var btn_mod = $("#application_formSearch #btn_mod");
        var btn_deal = $("#application_formSearch #btn_deal");
        var btn_submit = $("#application_formSearch #btn_submit");
        var btn_reject = $("#application_formSearch #btn_reject");
        var btn_searchexport = $("#application_formSearch #btn_searchexport");
        var btn_selectexport = $("#application_formSearch #btn_selectexport");
        //添加日期控件
        laydate.render({
            elem: '#application_formSearch #bgrqstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#application_formSearch #bgrqend'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#application_formSearch #sqrqstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#application_formSearch #sqrqend'
            ,type: 'date'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                applicationResult(true);
            });
        }
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            applicationById(null, "add", btn_add.attr("tourl"));
        });
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                applicationById(sel_row[0].tssqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------驳回-----------------------------------*/
        btn_reject.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                applicationById(sel_row[0].tssqid,"reject",btn_reject.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改列表-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='10'){
                    $.error("此状态不允许修改！");
                    return;
                }else{
                    applicationById(sel_row[0].tssqid,"mod",btn_mod.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提交-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt!='00'){
                    $.error("此状态不允许修改！");
                    return;
                }else{
                    if(sel_row[0].clbj=='2') {
                        $.error("该条记录已经驳回，不允许再次提交！");
                        return;
                    }else{
                        applicationById(sel_row[0].tssqid,"submit",btn_submit.attr("tourl"));
                    }
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].tssqid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        applicationResult();
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
        /* ------------------------------删除-----------------------------*/
        btn_deal.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].zt!='10'){
                        ids= ids + ","+ sel_row[i].tssqid;
                    }else{
                        $.error("请选择未在审核中的数据！");
                        return;
                    }

                }
                ids=ids.substr(1);
                $.confirm('您确定要处理所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_deal.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        applicationResult();
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
        /* ---------------------------导出-----------------------------------*/
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#application_formSearch #application_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].tssqid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=APPLICATION_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=APPLICATION_SEARCH&expType=search&callbackJs=DcApplicationSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /**显示隐藏**/
        $("#application_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(application_turnOff){
                $("#application_formSearch #searchMore").slideDown("low");
                application_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#application_formSearch #searchMore").slideUp("low");
                application_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};



function applicationResult(isTurnBack){
    //关闭高级搜索条件
    $("#application_formSearch #searchMore").slideUp("low");
    application_turnOff=true;
    $("#application_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#application_formSearch #application_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#application_formSearch #application_list').bootstrapTable('refresh');
    }
}

var application_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var sqxm=$("#application_formSearch a[id^='sqxm_id_']")
        addTj('clbj','0','application_formSearch');
        $.each(sqxm, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            var sfmr=$("#application_formSearch #"+id).attr("sfmr");
            if(sfmr == '1'){
                addTj('sqxm',code,'application_formSearch');
            }

        });
    }
    return oInit;
}

$(function(){
    var oInit = new application_PageInit();
    oInit.Init();
    // 1.初始化Table
    var oTable = new application_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new application_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#application_formSearch .chosen-select').chosen({width: '100%'});

    $("#application_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});