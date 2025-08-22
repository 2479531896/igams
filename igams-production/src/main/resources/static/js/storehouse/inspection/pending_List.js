var pending_turnOff=true;
var selOptSet = new Set();
var pending_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#pending_formSearch #pending_list').bootstrapTable({
            url: $("#pending_formSearch #urlPrefix").val()+'/inspectionGoods/pending/pageGetListPending',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#pending_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jydh",					// 排序字段
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
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gg',
                title: '规格',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lbcsmc',
                title: '类别',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ckmc',
                title: '仓库',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jldw',
                title: '单位',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'scph',
                title: '生产批号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'dhdh',
                title: '到货单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'dhlxmc',
                title: '到货类型',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jydh',
                title: '检验单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dclsl',
                title: '待处理数量',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'rklbmc',
                title: '入库类别',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htnbbh',
                title: '合同内部编号',
                width: '10%',
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
            }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                pendingById(row.hwid,'view',$("#pending_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#pending_formSearch #pending_list").colResizable({
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
            sortLastName: "hwid", // 防止同名排位用
            sortLastOrder: "desc",// 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return pendingByIdSearchData(map);
    };
    return oTableInit;
};

function pendingByIdSearchData(map){
    var cxtj=$("#pending_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#pending_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="2"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="3"){
        map["dhdh"]=cxnr;
    }else if(cxtj=="4"){
        map["jydh"]=cxnr;
    }else if(cxtj=="5"){
        map["scph"]=cxnr;
    }else if(cxtj=="6"){
        map["rklbmc"]=cxnr;
    }else if(cxtj=="7"){
        map["htnbbh"]=cxnr;
    }else if(cxtj=="8"){
        map["gysmc"]=cxnr;
    }
    // 类别
    var lbs = jQuery('#pending_formSearch #lb_id_tj').val();
    map["lbs"] = lbs;
    
    // 到货类型
    var dhlxs = jQuery('#pending_formSearch #dhlx_id_tj').val();
    map["dhlxs"] = dhlxs;
    var rklbs = jQuery('#pending_formSearch #rklb_id_tj').val();
    map["rklbs"] = rklbs;
    return map;
}

function pendingById(id,action,tourl,value,gysmc,gysid){
    if(!tourl){
        return;
    }
    tourl = $("#pending_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?hwid=" +id;
        $.showDialog(url,'待处理详细信息',viewPendingConfig);
    }else if(action == 'disposal'){
        var url= tourl + "?hwxxids=" +id+"&zcck="+value;
        $.showDialog(url,'处理',disposalPendingConfig);
    }else if(action == 'purchase'){
        $.ajax({
            type : "POST",
            url : $('#pending_formSearch #urlPrefix').val() + "/inspectionGoods/inspection/pagedataGetU8ckdh",
            data : {"ids" : id+"","access_token":$("#ac_tk").val()},
            dataType : "json",
            success:function(data){
                if (data.status == "success"){
                    var url= tourl + "?ids=" +id+"&ckid="+value+"&gysmc="+gysmc+"&gysid="+gysid;
                    $.showDialog(url,'采购红字',purchasePendingConfig);
                }else{
                    $.alert(data.message);
                }
            }
        });

    }else if(action == 'substandard'){
        var url= tourl + "?ids=" +id+"&ckid="+value+"&gysmc="+gysmc+"&gysid="+gysid;
        $.showDialog(url,'成品处理',substandardConfig);
    }
}

var substandardConfig = {
    width		: "1600px",
    modalName	: "substandardModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提交",
            className : "btn-primary",
            callback : function() {
                if(!$("#editPutInStorageForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                if(t_map.rows != null && t_map.rows.length > 0){
                    var ckid=$("#editPutInStorageForm #ckid").val();
                    var hwJson = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz={"hwid":'',"rkbz":'',"kwbh":'',"yclsl":''};
                        sz.hwid = t_map.rows[i].hwid;
                        sz.rkbz = t_map.rows[i].rkbz;
                        sz.yclsl = t_map.rows[i].dclsl;
                        if(t_map.rows[i].kwbh!="" && t_map.rows[i].kwbh!=null){
                            sz.kwbh = t_map.rows[i].kwbh;
                        }else{
                            $.alert("库位信息不能为空！");
                            return false;
                        }
                        hwJson.push(sz);
                    }
                    $("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwJson));
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editPutInStorageForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editPutInStorageForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        var auditType = $("#editPutInStorageForm #auditType").val();
                        var putInStorage_params=[];
                        putInStorage_params.prefix=$('#editPutInStorageForm #urlPrefix').val();
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            showAuditFlowDialog(auditType,responseText["ywid"],function(){
                                pendingResult();
                            },null,putInStorage_params);
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

var purchasePendingConfig = {
    width: "1600px",
    modalName: "purchasePendingModel",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if (!$("#pendingpurchaseForm").valid()) {
                    $.alert("请填写正确信息");
                    return false;
                }
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"hwid":'',"wlid":'',"scph":'',"zsh":'',"htmxid":'',"qgmxid":'',"dhsl":'',"yxq":'',"scrq":'',"bz":'',"cskw":''};
                        sz.hwid = t_map.rows[i].hwid;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scph = t_map.rows[i].scph;
                        sz.zsh = t_map.rows[i].zsh;
                        sz.htmxid = t_map.rows[i].htmxid;
                        sz.qgmxid = t_map.rows[i].qgmxid;
                        sz.dhsl = t_map.rows[i].hcsl;
                        sz.yxq = t_map.rows[i].yxq;
                        sz.bz = t_map.rows[i].bz;
                        sz.cskw = t_map.rows[i].cskw;
                        sz.scrq = t_map.rows[i].scrq;
                        json.push(sz);
                    }
                    $("#pendingpurchaseForm #hwxx_json").val(JSON.stringify(json));
                }else{
                    $.alert("明细信息不能为空！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"] || {};

                $("#pendingpurchaseForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "pendingpurchaseForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            pendingResult();
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
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
}



var disposalPendingConfig = {
    width: "1600px",
    modalName: "disposalPendingModel",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if (!$("#pendingAllocationForm").valid()) {
                    $.alert("请填写正确信息");
                    return false;
                }
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"dchwid":'',"dchw":'',"drhw":'',"dbsl":''};
                        sz.dchwid = t_map.rows[i].hwid;
                        sz.dchw = t_map.rows[i].kwbh;
                        sz.drhw = t_map.rows[i].drkw;
                        sz.dbsl = t_map.rows[i].clsl;
                        if (sz.dbsl == "0"){
                            $.alert("处理数量不能为0！");
                            return false;
                        }
                        json.push(sz);
                    }
                    $("#pendingAllocationForm #dbmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("明细信息不能为空！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"] || {};

                $("#pendingAllocationForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "pendingAllocationForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            pendingResult();
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
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
}

var viewPendingConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var pending_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#pending_formSearch #btn_view");
        var btn_query = $("#pending_formSearch #btn_query");
        var btn_disposal = $("#pending_formSearch #btn_disposal");
        var btn_purchase = $("#pending_formSearch #btn_purchase");
        var btn_substandard = $("#pending_formSearch #btn_substandard");
        //添加日期控件
        laydate.render({
            elem: '#dhsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#dhsjend'
            ,theme: '#2381E9'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                pendingResult(true);
            });
        }



        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#pending_formSearch #pending_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                pendingById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /* ---------------------------处理-----------------------------------*/
        btn_disposal.unbind("click").click(function(){
            var sel_row = $('#pending_formSearch #pending_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length > 0){
                var hwids = "";
                var ckid = sel_row[0].ckid;
                var dhlxdm = sel_row[0].dhlxdm;
                for (var i = 0; i < sel_row.length; i++) {
                    if(ckid != sel_row[i].ckid){
                        $.error("请选择仓库相同的货物");
                        return;
                    }
                    if(dhlxdm != sel_row[i].dhlxdm){
                        $.error("请选择到货类型相同的货物");
                        return;
                    }
                    hwids = hwids + ","+ sel_row[i].hwid;
                }
                hwids = hwids.substr(1);
                pendingById(hwids,"disposal",btn_disposal.attr("tourl"),sel_row[0].ckid);
            }else{
                $.error("请至少选中一行");
            }
        });

        /* ---------------------------采购红字-----------------------------------*/
        btn_purchase.unbind("click").click(function(){
            var sel_row = $('#pending_formSearch #pending_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length > 0){
                var hwids = "";
                var htnbbh = sel_row[0].htnbbh;
                var ckid = sel_row[0].ckid;
                var gysid = sel_row[0].gysid;
                for (var i = 0; i < sel_row.length; i++) {
                    if(ckid != sel_row[i].ckid){
                        $.error("请选择仓库相同的货物！");
                        return;
                    }
                    if(htnbbh != sel_row[i].htnbbh){
                        $.error("请选择合同相同的数据！");
                        return;
                    }
                    if(gysid != sel_row[i].gysid){
                        $.error("请选择供应商相同的数据！");
                        return;
                    }
                    if(sel_row[i].rklbdm!="1"){
                        $.error("请选择入库类别为采购的数据！");
                        return;
                    }
                    hwids = hwids + ","+ sel_row[i].hwid;
                }
                hwids = hwids.substr(1);
                pendingById(hwids,"purchase",btn_purchase.attr("tourl"),sel_row[0].ckid,sel_row[0].gysmc,sel_row[0].gysid);
            }else{
                $.error("请至少选中一行");
            }
        });

        /* ---------------------------成品不合格处理-----------------------------------*/
        btn_substandard.unbind("click").click(function(){
            var sel_row = $('#pending_formSearch #pending_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length > 0){
                var hwids = "";
                var ckid = sel_row[0].ckid;
                var gysid = sel_row[0].gysid;
                for (var i = 0; i < sel_row.length; i++) {
                    if(ckid != sel_row[i].ckid){
                        $.error("请选择仓库相同的货物！");
                        return;
                    }
                    if(gysid != sel_row[i].gysid){
                        $.error("请选择供应商相同的数据！");
                        return;
                    }
                    if(sel_row[i].rklbdm!="c"){
                        $.error("请选择入库类别为成品入库的数据！");
                        return;
                    }
                    hwids = hwids + ","+ sel_row[i].hwid;
                }
                hwids = hwids.substr(1);
                pendingById(hwids,"substandard",btn_substandard.attr("tourl"),sel_row[0].ckid,sel_row[0].gysmc,sel_row[0].gysid);
            }else{
                $.error("请至少选中一行");
            }
        });


        /**显示隐藏**/
        $("#pending_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(pending_turnOff){
                $("#pending_formSearch #searchMore").slideDown("low");
                pending_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#pending_formSearch #searchMore").slideUp("low");
                pending_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function pendingResult(isTurnBack){
    //关闭高级搜索条件
    $("#pending_formSearch #searchMore").slideUp("low");
    pending_turnOff=true;
    $("#pending_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#pending_formSearch #pending_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#pending_formSearch #pending_list').bootstrapTable('refresh');
    }
}

$(function(){

    // 1.初始化Table
    var oTable = new pending_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new pending_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#pending_formSearch .chosen-select').chosen({width: '100%'});

    $("#pending_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});