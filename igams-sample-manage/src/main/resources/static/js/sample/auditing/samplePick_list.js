var auditing_turnOff=true;
var auditingList=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#samplePick_formSearch #auditing_list").bootstrapTable({
            url: $("#samplePick_formSearch #urlPrefix").val()+'/sample/auditing/pageGetListYbll',
            method: 'get',                      // 请求方式（*）
            toolbar: '#samplePick_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "lldh",				// 排序字段
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
            uniqueId: "llid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
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
                field: 'llid',
                title: '领料id',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'lldh',
                title: '领料单号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'llrmc',
                title: '领料人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'flrmc',
                title: '发料人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true
                }, {
                field: 'zt',
                title: '审核状态',
                width: '10%',
                align: 'left',
                formatter:samplePick_ztformat,
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:samplePick_czFormat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                auditingById(row.llid,'view',$("#samplePick_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#samplePick_formSearch #auditing_list").colResizable({
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return auditingSearchData(map);
    };
    return oTableInit;
}

//状态格式化
function samplePick_ztformat(value,row,index){
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_SAMPLE\",{prefix:\"" + $('#samplePick_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {

        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_SAMPLE\",{prefix:\"" + $('#samplePick_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_SAMPLE\",{prefix:\"" + $('#samplePick_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}


/**
 * 操作按钮格式化函数
 * @returns
 */
function samplePick_czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallAamplePick('" + row.llid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj =='0' ){
        return "<span class='btn btn-warning' onclick=\"recallAamplePick('" + row.llid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}
//撤回项目提交
function recallAamplePick(llid,event){
    var auditType = $("#samplePick_formSearch #auditType").val();
    var msg = '您确定要撤回吗？';
    var samplePick_params = [];
    samplePick_params.prefix = $("#samplePick_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,llid,function(){
                auditingResult();
            },samplePick_params);
        }
    });
}
function auditingSearchData(map){
    var cxtj=$("#samplePick_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#samplePick_formSearch #cxnr').val());
    if(cxtj=="5"){
        map["entire"]=cxnr;
    }else if(cxtj=="0"){
        map["lldh"]=cxnr;
    }else if(cxtj=="1"){
        map["llrmc"]=cxnr;
    }else if(cxtj=="2"){
        map["bmmc"]=cxnr;
    }else if(cxtj=="3"){
        map["flrmc"]=cxnr;
    }else if(cxtj=="4"){
        map["ybbh"]=cxnr;
    }else if(cxtj=="5"){
        map["nbbm"]=cxnr;
    }
    // 样本类型
    var yblxs = jQuery('#samplePick_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs.replace(/'/g, "");
    // 申请开始日期
    var sqrqstart = jQuery('#samplePick_formSearch #sqrqstart').val();
    map["sqrqstart"] = sqrqstart;
    // 申请结束日期
    var sqrqend = jQuery('#samplePick_formSearch #sqrqend').val();
    map["sqrqend"] = sqrqend;
    return map;
}
function auditingResult(isTurnBack){
    //关闭高级搜索条件
    $("#samplePick_formSearch #searchMore").slideUp("low");
    auditing_turnOff=true;
    $("#samplePick_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#samplePick_formSearch #auditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#samplePick_formSearch #auditing_list').bootstrapTable('refresh');
    }
}
function auditingById(llid,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#samplePick_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?llid=" +llid;
        $.showDialog(url,'样本领料信息',viewAuditingConfig);
    }else if (action =='add'){
        var url= tourl;
        $.showDialog(url,'新增样本领料',addYbllConfig);
    }else if(action == 'mod'){
        var url= tourl+ "?llid=" +llid;
        $.showDialog(url,'修改样本领料',modYbllConfig);
    }else if(action == 'submit'){
        var url= tourl+ "?llid=" +llid;
        $.showDialog(url,'提交样本领料',submitYbllConfig);
    }else if(action == 'delivery'){
        var url= tourl+ "?llid=" +llid;
        $.showDialog(url,'样本领料出库',deliveryYbllConfig);
    }
}
var auditing_ButtonInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#samplePick_formSearch #btn_query");
        var btn_view = $("#samplePick_formSearch #btn_view");
        var btn_add = $("#samplePick_formSearch #btn_add");
        var btn_mod = $("#samplePick_formSearch #btn_mod");
        var btn_submit = $("#samplePick_formSearch #btn_submit");
        var btn_delivery = $("#samplePick_formSearch #btn_delivery");
        var btn_del = $("#samplePick_formSearch #btn_del");
        var btn_discard = $("#samplePick_formSearch #btn_discard");
        var btn_lldprint = $("#samplePick_formSearch #btn_lldprint");//领料单打印
        //添加日期控件
        laydate.render({
            elem: '#samplePick_formSearch #sqrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#samplePick_formSearch #sqrqend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                auditingResult(true);
            });
        }
        /* ---------------------------查看样本信息-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                auditingById(sel_row[0].llid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------新增样本领料-----------------------------------*/
        btn_add.unbind("click").click(function(){
            auditingById(null,"add",btn_add.attr("tourl"));
        });
        /*-------------------------------修改------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if (sel_row[0].zt=='00'||sel_row[0].zt=='15') {
                    auditingById(sel_row[0].llid, "mod", btn_mod.attr("tourl"));
                }else{
                    $.error("请选择状态为未提交或者未通过的记录！")
                    return;
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------出库------------------------------*/
        btn_delivery.unbind("click").click(function(){
            var sel_row = $('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if(sel_row[0].flrmc !=null && sel_row[0].flrmc !=""){
                    $.error("已出库，不允许重复出库！")
                    return;
                }
                    if (sel_row[0].zt=='80') {
                        auditingById(sel_row[0].llid, "delivery", btn_delivery.attr("tourl"));
                    }else{
                        $.error("请选择审核通过的领料单出库！")
                        return;
                    }
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------提交---------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row=$('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if (sel_row[0].zt=='00'||sel_row[0].zt=='15') {
                    auditingById(sel_row[0].llid,"submit",btn_submit.attr("tourl"));
                }else {
                    $.error("请选择状态为未提交或者未通过的记录！")
                    return;
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].llid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#samplePick_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchYbllResult(true);
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
        /* ------------------------------废弃-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].llid;
                if (sel_row[i].zt!='00'){
                    $.error("请选择未提交的数据");
                    return;
                }
            }
            ids = ids.substr(1);
            $.confirm('您确定要废弃所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#samplePick_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchYbllResult(true);
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
        /* --------------------------- 打印领料单 -----------------------------------*/
        btn_lldprint.unbind("click").click(function(){
            var sel_row = $('#samplePick_formSearch #auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].llid;
            }
            ids = ids.substr(1);
            var url=$('#samplePick_formSearch #urlPrefix').val()+btn_lldprint.attr("tourl")+"?ids="+ids.toString()+"&access_token="+$("#ac_tk").val();
            window.open(url);
        });
        /**显示隐藏**/
        $("#samplePick_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(auditing_turnOff){
                $("#samplePick_formSearch #searchMore").slideDown("low");
                auditing_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#samplePick_formSearch #searchMore").slideUp("low");
                auditing_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};
var viewAuditingConfig = {
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var addYbllConfig = {
    width		: "1500px",
    modalName	: "addYbllModal",
    formName	: "ybpickingCarForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#ybpickingCarForm").valid()){
                    return false;
                }
                let json = [];
                var jcdwdm="";
                if(t_map.rows != null && t_map.rows.length > 0) {
                    var checkJcdw=t_map.rows[0].jcdwmc;
                    jcdwdm=t_map.rows[0].jcdwdm;
                    for (let i = 0; i < t_map.rows.length; i++) {
                        if(checkJcdw!=t_map.rows[i].jcdwmc){
                            $.error("明细需选择存储单位一致的数据！");
                            return false;
                        }
                        let sz = {"ybkcid": ''};
                        sz.ybkcid = t_map.rows[i].ybkcid;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#ybpickingCarForm input[name='access_token']").val($("#ac_tk").val());
                $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));
                var $this = this;
                var opts = $this["options"]||{};
                submitForm(opts["formName"]||"ybpickingCarForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var ybpickingCar_params=[];
                                ybpickingCar_params.prefix=responseText["urlPrefix"];
                                var extend={"mrsz": "01"};
                                if(jcdwdm&&"0Y"==jcdwdm){
                                    extend={"extend_2": "0Y"};
                                }
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    searchYbllResult();
                                },null,ybpickingCar_params,extend);
                            }else{
                                searchYbllResult();
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
var modYbllConfig = {
    width		: "1500px",
    modalName	: "modYbllModal",
    formName	: "ybpickingCarForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#ybpickingCarForm").valid()){
                    return false;
                }
                let json = [];
                var jcdwdm="";
                if(t_map.rows != null && t_map.rows.length > 0) {
                    var checkJcdw=t_map.rows[0].jcdwmc;
                    jcdwdm=t_map.rows[0].jcdwdm;
                    for (let i = 0; i < t_map.rows.length; i++) {
                        if(checkJcdw!=t_map.rows[i].jcdwmc){
                            $.error("明细需选择存储单位一致的数据！");
                            return false;
                        }
                        let sz = {"ybkcid": ''};
                        sz.ybkcid = t_map.rows[i].ybkcid;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#ybpickingCarForm input[name='access_token']").val($("#ac_tk").val());
                $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));
                var $this = this;
                var opts = $this["options"]||{};
                submitForm(opts["formName"]||"ybpickingCarForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var ybpickingCar_params=[];
                                ybpickingCar_params.prefix=responseText["urlPrefix"];
                                var extend={"mrsz": "01"};
                                if(jcdwdm&&"0Y"==jcdwdm){
                                    extend={"extend_2": "0Y"};
                                }
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    searchYbllResult();
                                },null,ybpickingCar_params,extend);
                            }else{
                                searchYbllResult();
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

var submitYbllConfig = {
    width		: "1500px",
    modalName	: "modYbllModal",
    formName	: "ybpickingCarForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提 交",
            className : "btn-primary",
            callback : function() {
                if(!$("#ybpickingCarForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                var jcdwdm="";
                if(t_map.rows != null && t_map.rows.length > 0) {
                    var checkJcdw=t_map.rows[0].jcdwmc;
                    jcdwdm=t_map.rows[0].jcdwdm;
                    for (let i = 0; i < t_map.rows.length; i++) {
                        if(checkJcdw!=t_map.rows[i].jcdwmc){
                            $.error("明细需选择存储单位一致的数据！");
                            return false;
                        }
                        let sz = {"ybkcid": ''};
                        sz.ybkcid = t_map.rows[i].ybkcid;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));
                $("#ybpickingCarForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ybpickingCarForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        //提交审核
                        if(responseText["auditType"]!=null){
                            var ybpickingCar_params=[];
                            ybpickingCar_params.prefix=responseText["urlPrefix"];
                            var extend={"mrsz": "01"};
                            if(jcdwdm&&"0Y"==jcdwdm){
                                extend={"extend_2": "0Y"};
                            }
                            showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                $.closeModal(opts.modalName);
                                searchYbllResult();
                            },null,ybpickingCar_params,extend);
                        }else{
                            searchYbllResult();
                        }
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

var deliveryYbllConfig = {
    width		: "1500px",
    modalName	: "modYbllModal",
    formName	: "ybpickingCarForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "出 库",
            className : "btn-primary",
            callback : function() {
                if(!$("#ybpickingCarForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        let sz = {"ybkcid": ''};
                        sz.ybkcid = t_map.rows[i].ybkcid;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));
                $("#ybpickingCarForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ybpickingCarForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchYbllResult(true);
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

var auditing_PageInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var scbj = $("#samplePick_formSearch a[id^='scbj_id_']");
        $.each(scbj, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code === '0'){
                addTj('scbj',code,'samplePick_formSearch');
            }
        });
    }
    return oInit;
}

function searchYbllResult(isTurnBack) {
    //关闭高级搜索条件
    $("#samplePick_formSearch #searchMore").slideUp("low");
    auditing_turnOff=true;
    $("#samplePick_formSearch #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#samplePick_formSearch #auditing_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#samplePick_formSearch #auditing_list').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    var oInit = new auditing_PageInit();
    oInit.Init();

    // 1.初始化Table
    var oTable = new auditingList();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new auditing_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#samplePick_formSearch .chosen-select').chosen({width: '100%'});

    $("#auditing_ButtonInit [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});