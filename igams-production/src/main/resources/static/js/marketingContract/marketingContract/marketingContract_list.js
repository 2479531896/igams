var marketingContract_turnOff=true;
var marketingContract_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#marketingContract_formSearch #marketingContract_list').bootstrapTable({
            url: $("#marketingContract_formSearch #urlPrefix").val()+'/marketingContract/marketingContract/pageGetListMarketingContract',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#marketingContract_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"yxht.lrsj",					// 排序字段
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
            uniqueId: "htid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'htid',
                title: '合同id',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'htbh',
                title: '合同编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'htmc',
                title: '合同名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'htlxmc',
                title: '合同类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ssywymc',
                title: '所属业务员',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'htrq',
                title: '合同日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khmc',
                title: '客户名称',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:khformat,
            },{
                field: 'khlxmc',
                title: '客户类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zd',
                title: '终端',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htzt',
                title: '合同主体',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htjbrmc',
                title: '经办人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htjbbmmc',
                title: '经办部门',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htgsbm',
                title: '归属部门',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'htje',
                title: '金额',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'htsl',
                title: '数量',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htzk',
                title: '折扣',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'htqx',
                title: '期限',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'hkfsmc',
                title: '回款方式',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'szht',
                title: '双章合同',
                width: '6%',
                align: 'left',
                sortable: true,
                formatter:szbjformat,
                visible: true
            },{
                field: 'yxhtlxmc',
                title: '营销合同类型',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xslbmc',
                title: '销售类别',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yzlxmc',
                title: '用章类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false,
            }, {
                field: 'htyjxx',
                title: '邮寄信息',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'sfjh',
                title: '是否寄回',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:sfjhformat,
                visible: true
            }, {
                field: 'ht_zt',
                title: '合同状态',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:htztformat,
                visible: true
            }, {
                field: 'htqdzt',
                title: '签订状态',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:htqdztformat,
                visible: true
            }, {
                field: 'zt',
                title: '状态',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:ztformat,
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '7%',
                align: 'left',
                formatter:czFormat,
                visible: true
            }
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                marketingContractById(row.htid,'view',$("#marketingContract_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#marketingContract_formSearch #marketingContract_list").colResizable({
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
        return marketingContractSearchData(map);
    };
    return oTableInit;
};
//双章标记格式化
function szbjformat(value,row,index){
    if(row.szht==1){
        var szht="<span style='color:green;'>"+"是"+"</span>";
    }else if(row.szht==0){
        var szht="<span style='color:red;'>"+"否"+"</span>";
    }
    return szht;
}
function sfjhformat(value,row,index){
    var html = "";
    if(row.sfjh==1){
        var html="<span style='color:green;'>"+"是"+"</span>";
    }else if(row.sfjh==0){
        var html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallmarketingContract('" + row.htid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"recallmarketingContract('" + row.htid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallmarketingContract(htid,event){
    var auditType = $("#marketingContract_formSearch #auditType").val();
    var msg = '您确定要撤回该合同吗？';
    var purchase_params = [];
    purchase_params.prefix = $("#marketingContract_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,htid,function(){
                marketingContractResult();
            },purchase_params);
        }
    });
}
function khformat(value,row,index) {
    var html = "";
    if(row.khmc==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"viewKh('"+row.khid+"')\">"+row.khmc+"</a>";

    }
    return html;
}
function viewKh(khid){
    var url=$("#marketingContract_formSearch #urlPrefix").val()+"/storehouse/khgl/pagedataViewKhgl?khid="+khid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'客户信息',viewKhConfig);
}
var viewKhConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


//状态格式化
function ztformat(value,row,index){
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_MARKETING_CONTRACT\",{prefix:\"" + $('#marketingContract_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_MARKETING_CONTRACT\",{prefix:\"" + $('#marketingContract_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_MARKETING_CONTRACT\",{prefix:\"" + $('#marketingContract_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
//合同签订状态格式化
function htqdztformat(value,row,index){
    var html="";
    if(row.htqdzt=='0'){
        html="签订中";
    }else if (row.htqdzt=='1'){
        html="<span style='color:green;'>"+"签订完成"+"</span>";
    }else if (row.htqdzt=='2'){
        html="<span style='color:red;'>"+"已终止"+"</span>";
    }
    return html;
}

function htztformat(value,row,index){
     var html="";
     if(row.ht_zt=='0'){
         html="<span style='color:red;'>"+"即将逾期"+"</span>";
     }else if (row.ht_zt=='1'){
         html="<span style='color:green;'>"+"已逾期"+"</span>";
     }
     return html;
 }

function marketingContractSearchData(map){
    var cxtj=$("#marketingContract_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#marketingContract_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["htbh"]=cxnr;
    }else if(cxtj=="2"){
        map["zd"]=cxnr;
    }else if (cxtj == "3") {
        map["ssywymc"] = cxnr;
    }else if (cxtj == "4") {
        map["khmc"] = cxnr;
    }else if (cxtj == "6") {
        map["htzk"] = cxnr;
    }else if (cxtj == "7") {
        map["xsdh"] = cxnr;
    }else if (cxtj == "8") {
        map["u8xsdh"] = cxnr;
    }else if (cxtj == "9") {
        map["lldh"] = cxnr;
    }else if (cxtj == "10") {
        map["u8lldh"] = cxnr;
    }else if (cxtj == "11") {
        map["jydh"] = cxnr;
    }else if (cxtj == "12") {
        map["u8jcdh"] = cxnr;
    }else if (cxtj == "13") {
        map["ghdh"] = cxnr;
    }else if (cxtj == "14") {
        map["u8ghdh"] = cxnr;
    }else if (cxtj == "15") {
        map["htzt"] = cxnr;
    }else if (cxtj == "16") {
        map["htjbrmc"] = cxnr;
    }else if (cxtj == "17") {
        map["htjbbmmc"] = cxnr;
    }else if (cxtj == "18") {
        map["htgsbm"] = cxnr;
    }

    // 合同类型
    var htlxs=jQuery('#marketingContract_formSearch #htlx_id_tj').val();
    map["htlxs"] = htlxs.replace(/'/g, "");
    // 客户类型
    var khlxs=jQuery('#marketingContract_formSearch #khlx_id_tj').val();
    map["khlxs"] = khlxs.replace(/'/g, "");
    // 合同期限
    var htqxs=jQuery('#marketingContract_formSearch #htqx_id_tj').val();
    map["htqxs"] = htqxs.replace(/'/g, "");
    // 双章标记
    var szbjs=jQuery('#marketingContract_formSearch #szbj_id_tj').val();
    map["szbjs"] = szbjs.replace(/'/g, "");
    // 销售类型
    var xslxs=jQuery('#marketingContract_formSearch #xslx_id_tj').val();
    map["xslxs"] = xslxs.replace(/'/g, "");
    //合同风险程度
    var yxhtlxs=jQuery('#marketingContract_formSearch #yxhtlx_id_tj').val();
    map["yxhtlxs"] = yxhtlxs.replace(/'/g, "");
    //合同签订状态
    var htqdzts=jQuery('#marketingContract_formSearch #htqdzt_id_tj').val();
    map["htqdzts"] = htqdzts.replace(/'/g, "");
    var htzts=jQuery('#marketingContract_formSearch #htzt_id_tj').val();
    map["htzts"] = htzts.replace(/'/g, "");
    var sfjhs=jQuery('#marketingContract_formSearch #sfjh_id_tj').val();
    map["sfjhs"] = sfjhs.replace(/'/g, "");
    //用章类别
    var yzlbs=jQuery('#marketingContract_formSearch #yzlb_id_tj').val();
    map["yzlbs"] = yzlbs.replace(/'/g, "");
    var htrqstart=jQuery('#marketingContract_formSearch #htrqstart').val();
    map["htrqstart"] = htrqstart;
    var htrqend=jQuery('#marketingContract_formSearch #htrqend').val();
    map["htrqend"] = htrqend;
    var htjemin=jQuery('#marketingContract_formSearch #htjemin').val();
    map["htjemin"] = htjemin;
    var htjemax=jQuery('#marketingContract_formSearch #htjemax').val();
    map["htjemax"] = htjemax;
    return map;
}


//提供给导出用的回调函数
function yxhtDcSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="yxht.htrq";
    map["sortLastOrder"]="desc";
    map["sortName"]="yxht.lrsj";
    map["sortOrder"]="desc";
    return marketingContractSearchData(map);
}



function marketingContractById(id,action,tourl) {
    if(!tourl){
        return;
    }
    tourl = $("#marketingContract_formSearch #urlPrefix").val() + tourl;
    if(action =='view'){
        var url= tourl + "?htid=" +id;
        $.showDialog(url,'营销合同详细信息',viewMarketingContractConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增营销合同信息',addMarketingContractConfig);
    }else if(action =='submit'){
        var url= tourl+"?htid="+id;
        $.showDialog(url,'提交营销合同',submitMarketingContractConfig);
    }else if(action=="mod"){
        var url=tourl+"?htid="+id;
        $.showDialog(url,'营销合同信息修改',modMarketingContractConfig);
    }else if(action =='formal'){
        var url=tourl + "?htid=" +id;
        $.showDialog(url,'上传双章合同',uploadMarketingContractConfig);
    }else if(action =='copy'){
        var url=tourl + "?htid=" +id;
        $.showDialog(url,'复制营销合同',addMarketingContractConfig);
    }else if(action =='sendback'){
        var url=tourl+id;
        $.showDialog(url,'原件寄回',sendbackContractConfig);
    }
}

var sendbackContractConfig = {
    width		: "500px",
    modalName	: "sendbackContractModal",
    formName	: "sendbackForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#sendbackForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#sendbackForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"sendbackForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            marketingContractResult();
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

var uploadMarketingContractConfig = {
    width		: "600px",
    modalName	: "uploadMarketingContractModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "上传",
            className : "btn-primary",
            callback : function() {
                if(!$("#uploadMarketingContractForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var fjids = $("#uploadMarketingContractForm #fjids").val();

                if (fjids.length<=0){
                    $.error("未上传附件不允许保存！");
                    return false;
                }
                $("#uploadMarketingContractForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"uploadMarketingContractForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                marketingContractResult();
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
//提交
var submitMarketingContractConfig = {
    width		: "1500px",
    modalName	: "submitMarketingContractModal",
    formName	: "editMarketingContractForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提 交",
            className : "btn-primary",
            callback : function() {
                if(!$("#editMarketingContractForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var dqrqflg = $("#editMarketingContractForm [name='dqrqflg']");
                if(dqrqflg[0].checked){
                    $("#editMarketingContractForm #dqrq").val("");
                }else{
                    $("#editMarketingContractForm #htqx").val("");
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editMarketingContractForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editMarketingContractForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        //提交审核
                        if(responseText["auditType"]!=null){
                            var thgl_params=[];
                            thgl_params.prefix=responseText["urlPrefix"];
                            showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                $.closeModal(opts.modalName);
                                marketingContractResult();
                            },null,thgl_params);
                        }else{
                            marketingContractResult();
                        }
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
//新增
var addMarketingContractConfig = {
    width		: "1500px",
    modalName	: "addMarketingContractModal",
    formName	: "editMarketingContractForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提 交",
            className : "btn-primary",
            callback : function() {
                if(!$("#editMarketingContractForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var dqrqflg = $("#editMarketingContractForm [name='dqrqflg']");
                if(dqrqflg[0].checked){
                    $("#editMarketingContractForm #dqrq").val("");
                }else{
                    $("#editMarketingContractForm #htqx").val("");
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editMarketingContractForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editMarketingContractForm",function(responseText,statusText){
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
                                    marketingContractResult();
                                },null,thgl_params);
                            }else{
                                marketingContractResult();
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
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#editMarketingContractForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editMarketingContractForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editMarketingContractForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            marketingContractResult();
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
//修改
var modMarketingContractConfig = {
    width		: "1500px",
    modalName	: "modMarketingContractModal",
    formName	: "editMarketingContractForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editMarketingContractForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var dqrqflg = $("#editMarketingContractForm [name='dqrqflg']");
                if(dqrqflg[0].checked){
                    $("#editMarketingContractForm #dqrq").val("");
                }else{
                    $("#editMarketingContractForm #htqx").val("");
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editMarketingContractForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editMarketingContractForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            marketingContractResult();
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
var viewMarketingContractConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var marketingContract_ButtonInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var btn_view = $("#marketingContract_formSearch #btn_view");
        var btn_query = $("#marketingContract_formSearch #btn_query");
        var btn_add = $("#marketingContract_formSearch #btn_add");
        var btn_mod = $("#marketingContract_formSearch #btn_mod");
        var btn_submit = $("#marketingContract_formSearch #btn_submit");
        var btn_del = $("#marketingContract_formSearch #btn_del");
        var btn_discard = $("#marketingContract_formSearch #btn_discard");
        var btn_formal = $("#marketingContract_formSearch #btn_formal");
        var btn_copy = $("#marketingContract_formSearch #btn_copy");
        var btn_selectexport = $("#marketingContract_formSearch #btn_selectexport");
        var btn_searchexport = $("#marketingContract_formSearch #btn_searchexport");
        var btn_sendback = $("#marketingContract_formSearch #btn_sendback");
        //添加日期控件
        laydate.render({
            elem: '#htrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#htrqend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                marketingContractResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                marketingContractById(sel_row[0].htid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 新增营销合同信息 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            marketingContractById(null, "add", btn_add.attr("tourl"));
        });
        /* --------------------------- 修改营销合同信息 -----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if(sel_row[0].scbj =='2'){
                    $.error("您好！该条记录已废弃，不允许修改！");
                    return false;
                }
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    marketingContractById(sel_row[0].htid, "mod", btn_mod.attr("tourl"));
                }else{
                    $.alert("该记录在审核中或已审核，不允许修改!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 复制营销合同信息 -----------------------------------*/
        btn_copy.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                marketingContractById(sel_row[0].htid, "copy", btn_copy.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提交-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt !='00' && sel_row[0].zt !='15' && sel_row[0].zt !=null){
                    $.error("您好！该条记录已提交或者已通过，不允许重复提交！");
                    return false;
                }
                if(sel_row[0].scbj =='2'){
                    $.error("您好！该条记录已废弃，不允许提交！");
                    return false;
                }
                marketingContractById(sel_row[0].htid,"submit",btn_submit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------正式合同上传-----------------------------------*/
        btn_formal.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt !='80'){
                    $.error("您好！该条记录还未通过审核，不允许上传双章合同！");
                    return false;
                }
                marketingContractById(sel_row[0].htid,"formal",btn_formal.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------导出--------------------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].htid;
                }
                ids = ids.substr(1);
                $.showDialog($('#marketingContract_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=YXHT_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#marketingContract_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=YXHT_SEARCH&expType=search&callbackJs=yxhtDcSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        //---------------------------删除----------------------------------
        btn_del.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].htid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){

                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= $('#marketingContract_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    marketingContractResult(true);
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
        /* --------------------------- 原件寄回 -----------------------------------*/
        btn_sendback.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                marketingContractById("?htid="+sel_row[0].htid+"&sfjh="+sel_row[0].sfjh, "sendback", btn_sendback.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        //---------------------------废弃----------------------------------
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(sel_row[i].zt=="10" || sel_row[i].zt=="80"){
                    $.alert("有记录在审核中或已审核，不允许废弃!");
                    return;
                }
                ids = ids + ","+ sel_row[i].htid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要废弃所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= $('#marketingContract_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    marketingContractResult(true);
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
        /**显示隐藏**/
        $("#marketingContract_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(marketingContract_turnOff){
                $("#marketingContract_formSearch #searchMore").slideDown("low");
                marketingContract_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#marketingContract_formSearch #searchMore").slideUp("low");
                marketingContract_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};
function marketingContractResult(isTurnBack){
    //关闭高级搜索条件
    $("#marketingContract_formSearch #searchMore").slideUp("low");
    marketingContract_turnOff=true;
    $("#marketingContract_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#marketingContract_formSearch #marketingContract_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new marketingContract_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new marketingContract_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#marketingContract_formSearch .chosen-select').chosen({width: '100%'});

    $("#marketingContract_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});