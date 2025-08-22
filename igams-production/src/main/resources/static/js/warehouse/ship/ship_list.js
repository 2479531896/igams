
var ship_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#ship_formSearch #ship_list').bootstrapTable({
            url: $("#ship_formSearch #urlPrefix").val()+'/ship/ship/pageGetListShip',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#ship_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"fhdh",					// 排序字段
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
            uniqueId: "hwid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'fhid',
                title: '发货ID',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fhdh',
                title: '发货单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'jsrmc',
                title: '经手人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'djrq',
                title: '单据日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xsbmmc',
                title: '销售部门',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'xsdd',
                title: '订单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter: shipXSformat,
            },{
                field: 'khmc',
                title: '客户',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'u8fhdh',
                title: 'U8发货单号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                visible: true,
                formatter: shipZTformat,
            }, {
                field: 'cz',
                title: '操作',
                width: '6%',
                align: 'left',
                formatter:shipCZFormat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                shipById(row.fhid,'view',$("#ship_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#ship_formSearch #ship_list").colResizable({
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
            sortLastName: "fhid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return shipSearchData(map);
    }
    return oTableInit
}


//借出列表的提交状态格式化函数
function shipZTformat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fhid + "\",event,\"AUDIT_SHIPPING\",{prefix:\"" + $('#ship_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fhid + "\",event,\"AUDIT_SHIPPING\",{prefix:\"" + $('#ship_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fhid + "\",event,\"AUDIT_SHIPPING\",{prefix:\"" + $('#ship_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
//借出列表的提交状态格式化函数
function shipXSformat(value,row,index) {
    return "<a href='javascript:void(0);' onclick='showXsInfo(\"" + row.xsid + "\")' >" + row.xsdd + "</a>";
}
//借出列表的提交状态格式化函数
function showXsInfo(value) {
    $.showDialog($("#ship_formSearch #urlPrefix").val()+"/storehouse/sale/pagedataViewShipSale?xsid="+value,'详细信息',viewXsConfig);
}
var viewXsConfig = {
    width		: "1600px",
    modalName	: "viewsaleModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//操作按钮格式化函数
function shipCZFormat(value,row,index) {
    //var param = {prefix:$('#receiveMaterielList_formSearch #urlPrefix').val()};
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"ship_recallReceiveMateriel('" + row.fhid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"ship_recallReceiveMateriel('" + row.fhid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function ship_recallReceiveMateriel(fhid,event){
    var auditType = $("#ship_formSearch #auditType").val();
    var msg = '您确定要撤回该发货单吗？';
    var receiveMateriel_params = [];
    receiveMateriel_params.prefix = $("#ship_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fhid,function(){
                shipResult();
            },receiveMateriel_params);
        }
    });
}
//提供给导出用的回调函数
function shSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="fhid";
	map["sortLastOrder"]="asc";
	map["sortName"]="fhdh";
	map["sortOrder"]="desc";
	return shipSearchData(map);
}

// 根据查询条件查询
function shipSearchData(map){
    var cxtj=$("#ship_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#ship_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["fhdh"]=cxnr;
    }else if(cxtj=="2"){
        map["jsr"]=cxnr;
    }else if(cxtj=="3"){
        map["xsdd"]=cxnr;
    }else if(cxtj=="4"){
        map["kh"]=cxnr;
    }else if(cxtj=="5"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="6"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="7"){
        map["shdz"]=cxnr;
    }
    // 创建开始日期
    var fhsjstart = jQuery('#ship_formSearch #fhsjstart').val();
    map["fhsjstart"] = fhsjstart;

    // 创建结束日期
    var fhsjend = jQuery('#ship_formSearch #fhsjend').val();
    map["fhsjend"] = fhsjend;

    return map;
}


function shipById(id,action,tourl,fhdh){
    if(!tourl){
        return;
    }
    tourl = $("#ship_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?fhid=" +id;
        $.showDialog(url,'详细信息',viewShipConfig);
    }else if(action=='ship'){
        var url=tourl;
        if (id){
            url += "?fhid=" +id;
        }
        $.showDialog(url,'发货',shipAllocateConfig);
    }else if(action=='expressmaintenance'){
        var url= tourl + "?fhid=" +id;
        $.showDialog(url,'快递维护',shipExpressmaintenanceConfig);
    }else if(action=='logisticsuphold'){
        var url= tourl + "?ywid=" +id+"&ywlx=fh&ywdh="+fhdh;
        $.showDialog(url,'物流维护',addlogisticsUpholdFormConfig);
    }else if(action =='signfor'){
        var url= tourl+"?ywid="+id+"&ywlx=fh&ywdh="+fhdh;
        $.showDialog(url,'物流签收',addlogisticsUpholdFormConfig);
    }else if(action =='signforconfirm'){
        var url= tourl+"?ywid="+id+"&ywlx=fh&ywdh="+fhdh;
        $.showDialog(url,'物流签收确认',addlogisticsUpholdFormConfig);
    }
}
//物流维护
var addlogisticsUpholdFormConfig = {
    width		: "1000px",
    modalName	: "logisticsUpholdFormModal",
    formName	: "logisticsUpholdForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].kdgs==null || t_map.rows[i].kdgs==''){
                            $.alert("快递公司不能为空！");
                            return false;
                        }
                        if(t_map.rows[i].wldh==null || t_map.rows[i].wldh==''){
                            $.alert("快递单号不能为空！");
                            return false;
                        }
                        var fjids = $("#wlfj_"+i).val().split(",");
                        var sz = {"kdgs":'',"wldh":'',"ywid":'',"wlxxid":'',"qsrq":'',"sfqr":'',"lrsj":'',"fjids":'',"fhrq":'',"yf":''};
                        sz.kdgs = t_map.rows[i].kdgs;
                        sz.wldh = t_map.rows[i].wldh;
                        sz.ywid = t_map.rows[i].ywid;
                        sz.wlxxid = t_map.rows[i].wlxxid;
                        sz.qsrq = t_map.rows[i].qsrq;
                        sz.sfqr = t_map.rows[i].sfqr;
                        sz.lrsj = t_map.rows[i].lrsj;
                        sz.fhrq = t_map.rows[i].fhrq;
                        sz.yf = t_map.rows[i].yf;
                        sz.fjids = fjids;
                        json.push(sz);
                    }
                    $("#logisticsUpholdForm #wlmx_json").val(JSON.stringify(json));
                }
                $("#logisticsUpholdForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"logisticsUpholdForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            shipResult();
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
var shipExpressmaintenanceConfig = {
    width		: "1000px",
    modalName	: "shipExpressmaintenanceModal",
    formName	: "shipExpressmaintenanceForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#shipExpressmaintenanceForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#shipExpressmaintenanceForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"shipExpressmaintenanceForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                shipResult ();
                            }
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
var shipAllocateConfig = {
    width		: "1600px",
    modalName    : "allocationModal",
    formName    : "shipAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提  交",
            className : "btn-primary",
            callback : function() {
                if (!$("#shipAjaxForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                let json = [];
                var extend={"mrsz": "01"};
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(let i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].wlzllbcskz1&&"3"==t_map.rows[i].wlzllbcskz1){
                            extend={"extend_2": "IF"};
                        }
                        if (!t_map.rows[i].wlmx_json){
                            $.alert(t_map.rows[i].wlmc + "发货数量为0！");
                            return false;
                        }
                        if (t_map.rows[i].wlmx_json.length == "0"){
                            $.alert(t_map.rows[i].wlmc+"发货数量为0！");
                            return false;
                        }
                        let wlmx_json=JSON.parse(t_map.rows[i].wlmx_json);
                        let pd_json = [];
                        if(wlmx_json!=null && wlmx_json!=""){
                            for(let j=0;j<wlmx_json.length;j++){
                                let sz = {"xsid":'',"xsmxid":'',"wlid":'',"ck":'',"ckqxlx":'',"khdm":'',"ddmxid":'',"wlbm":'',"khmc":'',"xsdd":'',"kl":'',"kl2":'',"suil":'',"hsdj":'',"wsdj":'',"bj":'',"ddxh":'',"hwid":'',"fhsl":''};
                                sz.xsid = t_map.rows[i].xsid;
                                sz.xsdd = t_map.rows[i].oaxsdh;
                                sz.khdm = t_map.rows[i].khdm;
                                sz.xsmxid = t_map.rows[i].xsmxid;
                                sz.wlbm = t_map.rows[i].wlbm;
                                sz.khmc = t_map.rows[i].khjcmc;
                                sz.khid = t_map.rows[i].khjc;
                                sz.suil  = t_map.rows[i].suil;
                                sz.hsdj  = t_map.rows[i].hsdj;
                                sz.wsdj  = t_map.rows[i].wsdj;
                                sz.bj = t_map.rows[i].bj;
                                sz.yfhrq= t_map.rows[i].yfhrq;
                                sz.hwid = wlmx_json[j].hwid;
                                sz.fhsl = wlmx_json[j].cksl;
                                sz.wlid = wlmx_json[j].wlid;
                                sz.ckqxlx = wlmx_json[j].ckqxlx;
                                sz.ck = wlmx_json[j].ckid;
                                if (sz.fhsl > "0"){
                                    json.push(sz);
                                    pd_json.push(sz)
                                }
                            }
                        }
                        if (!pd_json){
                            $.alert(t_map.rows[i].wlmc + "发货数量为0！");
                            return false;
                        }
                        if (pd_json.length == "0"){
                            $.alert(t_map.rows[i].wlmc + "发货数量为0！");
                            return false;
                        }
                    }
                    $("#shipAjaxForm #xsmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("发货明细不能为空！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"] || {};

                $("#shipAjaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "shipAjaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        var auditType = $("#shipAjaxForm #auditType").val();
                        var receiveMateriel_params=[];
                        receiveMateriel_params.prefix=$('#shipAjaxForm #urlPrefix').val();
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        //提交审核
                        showAuditFlowDialog(auditType, responseText["ywid"],function(){
                            shipResult();
                        },null,receiveMateriel_params,extend);
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");

                return false;
            }
        },
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if (!$("#shipAjaxForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(let i=0;i<t_map.rows.length;i++){
                        if (!t_map.rows[i].wlmx_json){
                            $.alert(t_map.rows[i].wlmc + "发货数量为0！");
                            return false;
                        }
                        if (t_map.rows[i].wlmx_json.length == "0"){
                            $.alert(t_map.rows[i].wlmc+"发货数量为0！");
                            return false;
                        }
                        let wlmx_json=JSON.parse(t_map.rows[i].wlmx_json);
                        let pd_json = [];
                        if(wlmx_json!=null && wlmx_json!=""){
                            for(let j=0;j<wlmx_json.length;j++){

                                let sz = {"xsid":'',"xsmxid":'',"wlid":'',"ck":'',"ckqxlx":'',"khdm":'',"ddmxid":'',"wlbm":'',"khmc":'',"xsdd":'',"kl":'',"kl2":'',"suil":'',"hsdj":'',"wsdj":'',"bj":'',"ddxh":'',"hwid":'',"fhsl":''};
                                sz.xsid = t_map.rows[i].xsid;
                                sz.xsdd = t_map.rows[i].oaxsdh;
                                sz.khdm = t_map.rows[i].khdm;
                                sz.xsmxid = t_map.rows[i].xsmxid;
                                sz.wlbm = t_map.rows[i].wlbm;
                                sz.khmc = t_map.rows[i].khjcmc;
                                sz.khid = t_map.rows[i].khjc;
                                sz.suil  = t_map.rows[i].suil;
                                sz.hsdj  = t_map.rows[i].hsdj;
                                sz.wsdj  = t_map.rows[i].wsdj;
                                sz.bj = t_map.rows[i].bj;
                                sz.yfhrq= t_map.rows[i].yfhrq;
                                sz.hwid = wlmx_json[j].hwid;
                                sz.fhsl = wlmx_json[j].cksl;
                                sz.wlid = wlmx_json[j].wlid;
                                sz.ckqxlx = wlmx_json[j].ckqxlx;
                                sz.ck = wlmx_json[j].ckid;
                                if (sz.fhsl > "0"){
                                    json.push(sz);
                                    pd_json.push(sz)
                                }
                            }
                        }
                        if (!pd_json){
                            $.alert(t_map.rows[i].wlmc + "发货数量为0！");
                            return false;
                        }
                        if (pd_json.length == "0"){
                            $.alert(t_map.rows[i].wlmc + "发货数量为0！");
                            return false;
                        }
                    }

                    $("#shipAjaxForm #xsmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("发货明细不能为空！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"] || {};

                $("#shipAjaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "shipAjaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            shipResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");

                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewShipConfig = {
    width		: "1600px",
    modalName	: "viewShipkModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var ship_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        var btn_view = $("#ship_formSearch #btn_view");
        var btn_query = $("#ship_formSearch #btn_query");
        var btn_ship= $("#ship_formSearch #btn_ship");
        var btn_del = $("#ship_formSearch #btn_del");// 删除
        var btn_expressmaintenance=$("#ship_formSearch #btn_expressmaintenance");//快递维护
        var btn_logisticsuphold=$("#ship_formSearch #btn_logisticsuphold");//物流维护
        var btn_signfor =  $("#ship_formSearch #btn_signfor");//物流签收
        var btn_signforconfirm =  $("#ship_formSearch #btn_signforconfirm");//物流签收确认
        var btn_saleprint = $("#ship_formSearch #btn_saleprint");//销售出库单打印
        var btn_searchexport = $("#ship_formSearch #btn_searchexport");
        var btn_selectexport = $("#ship_formSearch #btn_selectexport");
        laydate.render({
            elem: '#fhsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#fhsjend'
            ,theme: '#2381E9'
        });

        /*--------------------------------模糊查询---------------------------*/
        if (btn_query != null) {
            btn_query.unbind("click").click(function () {
                shipResult(true);
            });
        }
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#ship_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=SHIP_SEARCH&expType=search&callbackJs=shSearchData"
                    ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fhid;
                }
                ids = ids.substr(1);
                $.showDialog($('#ship_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=SHIP_SELECT&expType=select&ids="+ids
                        ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        /* ---------------------------查看-----------------------------------*/
        btn_view.unbind("click").click(function () {
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                shipById(sel_row[0].fhid, "view", btn_view.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });
        /*--------------------------------发货------------------------------------------*/
        btn_ship.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                if (sel_row[0].zt != "00" && sel_row[0].zt != "15"){
                    $.error("该状态不允许发货！");
                    return;
                }
                shipById(sel_row[0].fhid,"ship",btn_ship.attr("tourl"));
            } else if (sel_row.length == 0){
                shipById(null,"ship",btn_ship.attr("tourl"));
            }else {
                $.error("请选中一行");
            }

        });

        /* ------------------------------删除领料信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fhid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#ship_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        shipResult();
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
        /* ------------------------------快递维护-----------------------------*/
        btn_expressmaintenance.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length!=1){
                $.error("请选中一行");
                return;
            }else {
                    if (sel_row[0].zt != "80"){
                        $.error("未发货，不允许维护！");
                        return;
                    }
                    else
                        shipById(sel_row[0].fhid, "expressmaintenance", btn_expressmaintenance.attr("tourl"));
            }
        });
        /* --------------------------- 物流维护 -----------------------------------*/
        btn_logisticsuphold.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                shipById(sel_row[0].fhid, "logisticsuphold", btn_logisticsuphold.attr("tourl"),sel_row[0].fhdh);
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 物流签收 -----------------------------------*/
        btn_signfor.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                shipById(sel_row[0].fhid, "signfor", btn_signfor.attr("tourl"),sel_row[0].fhdh);
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 物流签收确认 -----------------------------------*/
        btn_signforconfirm.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                shipById(sel_row[0].fhid, "signforconfirm", btn_signforconfirm.attr("tourl"),sel_row[0].fhdh);
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 打印销售出库单 -----------------------------------*/
        btn_saleprint.unbind("click").click(function(){
            var sel_row = $('#ship_formSearch #ship_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fhid;
            }
            ids = ids.substr(1);
            var url=$('#ship_formSearch #urlPrefix').val()+btn_saleprint.attr("tourl")+"?ids="+ids.toString()+"&access_token="+$("#ac_tk").val();
            window.open(url);
        });
    };
    return oInit;
};


function shipResult(isTurnBack){
    if(isTurnBack){
        $('#ship_formSearch #ship_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#ship_formSearch #ship_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new ship_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ship_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#ship_formSearch .chosen-select').chosen({width: '100%'});

    $("#ship_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});