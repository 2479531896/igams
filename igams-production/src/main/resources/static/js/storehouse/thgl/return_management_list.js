var returnManagement_turnOff=true;
var returnManagement_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#returnManagement_formSearch #returnManagement_list').bootstrapTable({
            url: $("#returnManagement_formSearch #urlPrefix").val()+'/storehouse/returnManagement/pageGetListReturnManagement',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#returnManagement_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"thdh",					// 排序字段
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
            uniqueId: "thid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '4%',
                align: 'center',
                visible:true
            },{
                field: 'thdh',
                title: '退货单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'u8thdh',
                title: 'u8退货单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jsrmc',
                title: '经手人',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'djrq',
                title: '单据日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xsbmmc',
                title: '销售部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ddh',
                title: '订单号',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khjc',
                title: '客户简称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:returnManagement_ztformat,
                sortable: true,
                visible: true
            }, {
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:czFormat,
                visible: true
            }, {
                field: 'lrsj',
                title: '录入时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                returnManagementById(row.thid,'view',$("#returnManagement_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#returnManagement_formSearch #returnManagement_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:false,
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
            sortLastName: "thid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return returnManagementSearchData(map);
    };
    return oTableInit;
};
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallReturnManagementPick('" + row.thid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"recallReturnManagementPick('" + row.thid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}
//撤回项目提交
function recallReturnManagementPick(thid,event){
    var auditType = $("#returnManagement_formSearch #auditType").val();
    var msg = '您确定要撤回吗？';
    var returnManagement_params = [];
    returnManagement_params.prefix = $("#returnManagement_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,thid,function(){
                returnManagementResult();
            },returnManagement_params);
        }
    });
}
//状态格式化
function returnManagement_ztformat(value,row,index){
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.thid + "\",event,\"RETURN_GOODS\",{prefix:\"" + $('#returnManagement_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.thid + "\",event,\"RETURN_GOODS\",{prefix:\"" + $('#returnManagement_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.thid + "\",event,\"RETURN_GOODS\",{prefix:\"" + $('#returnManagement_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
//查询条件
function returnManagementSearchData(map){
    var cxtj=$("#returnManagement_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#returnManagement_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["thdh"]=cxnr;
    }else if(cxtj=="2"){
        map["jsrmc"]=cxnr;
    }else if(cxtj=="3"){
        map["xslxmc"]=cxnr;
    }else if(cxtj=="4"){
        map["xsbmmc"]=cxnr;
    }
    // 单据开始日期
    var djrqstart = jQuery('#returnManagement_formSearch #djrqstart').val();
    map["djrqstart"] = djrqstart;

    // 单据结束日期
    var djrqend = jQuery('#returnManagement_formSearch #djrqend').val();
    map["djrqend"] = djrqend;

    return map;
}
//打开模态框
function returnManagementById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#returnManagement_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?thid=" +id;
        $.showDialog(url,'退货详细信息',viewReturnManagementConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增退货信息',addReturnManagementConfig);
    }else if(action =='submit'){
        var url= tourl+"?thid="+id;
        $.showDialog(url,'提交退货信息',submitReturnManagementConfig);
    }else if(action=="mod"){
        var url=tourl+"?thid="+id;
        $.showDialog(url,'退货信息修改',modReturnManagementConfig);
    }
}
//查看
var viewReturnManagementConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//新增
var addReturnManagementConfig = {
    width		: "1500px",
    modalName	: "addReturnManagementModal",
    formName	: "editReturnManagementForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editReturnManagementForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];

                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"wlid":'',"hwid":'',"thsl":'',"bz":'',"xsdd":'',"fhmxglid":'',"bj":'',"suil":'',"wsdj":'',"hsdj":'',"xsmxid":'',"fhmxid":'',"ck":'',"kw":'',"scrq":'',"yxq":'',"scph":'',"zsh":''};
                        sz.index=t_map.rows.length;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.hwid = t_map.rows[i].hwid;
                        sz.thsl = t_map.rows[i].thsl;
                        sz.bz = t_map.rows[i].bz;
                        sz.xsdd = t_map.rows[i].xsdd;
                        sz.fhmxglid = t_map.rows[i].fhmxglid;
                        sz.bj = t_map.rows[i].bj;
                        sz.suil = t_map.rows[i].suil;
                        sz.wsdj = t_map.rows[i].wsdj;
                        sz.hsdj = t_map.rows[i].hsdj;
                        sz.xsmxid = t_map.rows[i].xsmxid;
                        sz.fhmxid = t_map.rows[i].fhmxid;
                        sz.ck = t_map.rows[i].ck;
                        sz.kw = t_map.rows[i].kw;
                        sz.scrq = t_map.rows[i].scrq;
                        sz.yxq = t_map.rows[i].yxq;
                        sz.scph = t_map.rows[i].scph;
                        sz.zsh = t_map.rows[i].zsh;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#editReturnManagementForm #cklist").val("");
                $("#editReturnManagementForm #kwlist").val("");
                $("#editReturnManagementForm #thmx_json").val(JSON.stringify(json));
                $("#editReturnManagementForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editReturnManagementForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var thgl_params=[];
                                thgl_params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    returnManagementResult();
                                },null,thgl_params);
                            }else{
                                returnManagementResult();
                            }
                        });
                    } else if(responseText["status"] == "fail"){
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
var modReturnManagementConfig = {
    width		: "1500px",
    modalName	: "addReturnManagementModal",
    formName	: "editReturnManagementForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editReturnManagementForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"wlid":'',"hwid":'',"thsl":'',"bz":'',"xsdd":'',"fhmxglid":'',"bj":'',"ck":'',"kw":'',"suil":'',"wsdj":'',"hsdj":'',"xsmxid":'',"fhmxid":'',"thmxid":'',"thid":'',"scrq":'',"yxq":'',"scph":'',"zsh":''};
                        sz.index=t_map.rows.length;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.hwid = t_map.rows[i].hwid;
                        sz.thsl = t_map.rows[i].thsl;
                        sz.bz = t_map.rows[i].bz;
                        sz.xsdd = t_map.rows[i].xsdd;
                        sz.fhmxglid = t_map.rows[i].fhmxglid;
                        sz.bj = t_map.rows[i].bj;
                        sz.suil = t_map.rows[i].suil;
                        sz.wsdj = t_map.rows[i].wsdj;
                        sz.hsdj = t_map.rows[i].hsdj;
                        sz.ck = t_map.rows[i].ck;
                        sz.kw = t_map.rows[i].kw;
                        sz.xsmxid = t_map.rows[i].xsmxid;
                        sz.fhmxid = t_map.rows[i].fhmxid;
                        sz.thmxid = t_map.rows[i].thmxid;
                        sz.thid = t_map.rows[i].thid;
                        sz.scrq = t_map.rows[i].scrq;
                        sz.yxq = t_map.rows[i].yxq;
                        sz.scph = t_map.rows[i].scph;
                        sz.zsh = t_map.rows[i].zsh;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#editReturnManagementForm #cklist").val("");
                $("#editReturnManagementForm #kwlist").val("");
                $("#editReturnManagementForm #thmx_json").val(JSON.stringify(json));
                $("#editReturnManagementForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editReturnManagementForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            returnManagementResult();
                        });
                    } else if(responseText["status"] == "fail"){
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
var submitReturnManagementConfig = {
    width		: "1500px",
    modalName	: "addReturnManagementModal",
    formName	: "editReturnManagementForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editReturnManagementForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"wlid":'',"hwid":'',"thsl":'',"bz":'',"xsdd":'',"fhmxglid":'',"bj":'',"ck":'',"kw":'',"suil":'',"wsdj":'',"hsdj":'',"xsmxid":'',"fhmxid":'',"thmxid":'',"thid":'',"scrq":'',"yxq":'',"scph":'',"zsh":''};
                        sz.index=t_map.rows.length;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.hwid = t_map.rows[i].hwid;
                        sz.thsl = t_map.rows[i].thsl;
                        sz.bz = t_map.rows[i].bz;
                        sz.xsdd = t_map.rows[i].xsdd;
                        sz.fhmxglid = t_map.rows[i].fhmxglid;
                        sz.bj = t_map.rows[i].bj;
                        sz.suil = t_map.rows[i].suil;
                        sz.wsdj = t_map.rows[i].wsdj;
                        sz.hsdj = t_map.rows[i].hsdj;
                        sz.ck = t_map.rows[i].ck;
                        sz.kw = t_map.rows[i].kw;
                        sz.xsmxid = t_map.rows[i].xsmxid;
                        sz.fhmxid = t_map.rows[i].fhmxid;
                        sz.thmxid = t_map.rows[i].thmxid;
                        sz.thid = t_map.rows[i].thid;
                        sz.scrq = t_map.rows[i].scrq;
                        sz.yxq = t_map.rows[i].yxq;
                        sz.scph = t_map.rows[i].scph;
                        sz.zsh = t_map.rows[i].zsh;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#editReturnManagementForm #cklist").val("");
                $("#editReturnManagementForm #kwlist").val("");
                $("#editReturnManagementForm #thmx_json").val(JSON.stringify(json));
                $("#editReturnManagementForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editReturnManagementForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var thgl_params=[];
                                thgl_params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    returnManagementResult();
                                },null,thgl_params);
                            }else{
                                returnManagementResult();
                            }
                        });
                    } else if(responseText["status"] == "fail"){
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

var returnManagement_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#returnManagement_formSearch #btn_view");
        var btn_query = $("#returnManagement_formSearch #btn_query");
        var btn_del = $("#returnManagement_formSearch #btn_del");
        var btn_add = $("#returnManagement_formSearch #btn_add");
        var btn_mod = $("#returnManagement_formSearch #btn_mod");
        var btn_submit = $("#returnManagement_formSearch #btn_submit");
        var btn_discard = $("#returnManagement_formSearch #btn_discard");//到货废弃
        //添加日期控件
        laydate.render({
            elem: '#djrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#djrqend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                returnManagementResult(true);
            });
        }
        /* --------------------------- 新增到货信息 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            returnManagementById(null, "add", btn_add.attr("tourl"));
        });
        /* --------------------------- 修改退货信息 -----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    returnManagementById(sel_row[0].thid, "mod", btn_mod.attr("tourl"));
                }else{
                    $.alert("该记录在审核中或已审核，不允许修改!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提交-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt !='00' && sel_row[0].zt !='15' && sel_row[0].zt !=null){
                    $.error("您好！该条记录已提交或者已通过，不允许重复提交！");
                    return false;
                }
                returnManagementById(sel_row[0].thid,"submit",btn_submit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                returnManagementById(sel_row[0].thid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------废弃到货信息-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('getSelections');//获取选择行数据
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
                    $.error("该记录在审核中或已审核，不允许废弃!");
                    return;
                }
            }
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].thid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要废弃所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url=$('#returnManagement_formSearch #returnManagement_list').val() + btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        arrivalGoodsResult(true);
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

        //---------------------------删除----------------------------------
        btn_del.unbind("click").click(function(){
            var sel_row = $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].thid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= $('#returnManagement_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    returnManagementResult(true);
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

    };
    return oInit;
};

//刷新
function returnManagementResult(isTurnBack){
    //关闭高级搜索条件
    $("#returnManagement_formSearch #searchMore").slideUp("low");
    arrivalGoods_turnOff=true;
    $("#returnManagement_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#returnManagement_formSearch #returnManagement_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new returnManagement_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new returnManagement_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#returnManagement_formSearch .chosen-select').chosen({width: '100%'});

    $("#returnManagement_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});