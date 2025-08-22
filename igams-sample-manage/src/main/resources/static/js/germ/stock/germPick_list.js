
var germPick_turnOff=true;

var germPick_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#germPick_formSearch #tb_list').bootstrapTable({
            url: '/germ/inventory/pageGetListGermPick',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#germPick_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jzllgl.lrsj",				//排序字段
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
            uniqueId: "llid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                width: '0.5%',
                checkbox: true,
            }, {
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '1%',
                align: 'left',
                visible:true
            },{
                field: 'llid',
                title: '领料id',
                width: '8%',
                align: 'center',
                visible: false,
                sortable: true
            }, {
                field: 'lldh',
                title: '领料单号',
                width: '5%',
                align: 'center',
                sortable: true,
                visible: true
            }, {
                field: 'llrmc',
                title: '领料人',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'flrmc',
                title: '发料人',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'bmmc',
                title: '部门',
                width: '5%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '4%',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'ckzt',
                title: '是否出库',
                width: '3%',
                align: 'center',
                visible: true,
                sortable: true,
                formatter:ckztformat,
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'zt',
                title: '审核状态',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true,
                formatter:germPick_ztformat,
            },{
                field: 'cz',
                title: '操作',
                width: '3%',
                align: 'center',
                visible: true,
                sortable: true,
                formatter:germPick_czFormat,
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                germPickDealById(row.llid, 'view',$("#germPick_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#germPick_formSearch #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );

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
            sortLastName: "jzllgl.llid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#germPick_formSearch #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#germPick_formSearch #cxnr').val());
        // '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["mc"] = cxnr;
        }else if (cxtj == "2") {
            map["bh"] = cxnr;
        }else if (cxtj == "3") {
            map["wz"] = cxnr;
        }else if (cxtj == "4") {
            map["ly"] = cxnr;
        }else if (cxtj == "5") {
            map["ph"] = cxnr;
        }else if (cxtj == "6") {
            map["gg"] = cxnr;
        }else if (cxtj == "7") {
            map["lldh"] = cxnr;
        }else if (cxtj == "8") {
            map["flrmc"] = cxnr;
        }else if (cxtj == "9") {
            map["llrmc"] = cxnr;
        }else if (cxtj == "10") {
            map["bmmc"] = cxnr;
        }
        // 菌种类型
        var jzlxs = jQuery('#germPick_formSearch #jzlx_id_tj').val();
        map["jzlxs"] = jzlxs.replace(/'/g, "");

        // 菌种分类
        var jzfls = jQuery('#germPick_formSearch #jzfl_id_tj').val();
        map["jzfls"] = jzfls.replace(/'/g, "");
        // 开始时间
        var sqsjstart = jQuery('#germPick_formSearch #sqsjstart').val();
        map["sqsjstart"] = sqsjstart;

        // 结束时间
        var sqsjend = jQuery('#germPick_formSearch #sqsjend').val();
        map["sqsjend"] = sqsjend;
        return map;
    };
    return oTableInit;
};
//借出列表的提交状态格式化函数
function ckztformat(value,row,index) {
    if (row.ckzt == '1')
        return '已出库';
    else return '未出库';


}
//状态格式化
function germPick_ztformat(value,row,index){
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GERM_PICK\",{prefix:\"" + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {

        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GERM_PICK\",{prefix:\""  + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GERM_PICK\",{prefix:\""  + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
/**
 * 操作按钮格式化函数
 * @returns
 */
function germPick_czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallGermPick('" + row.llid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null){
        return "<span class='btn btn-warning' onclick=\"recallGermPick('" + row.llid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}
//撤回项目提交
function recallGermPick(llid,event){
    var auditType = $("#germPick_formSearch #auditType").val();
    var msg = '您确定要撤回吗？';
    var germPick_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,llid,function(){
                searchgermPickResult();
            },germPick_params);
        }
    });
}
var viewgermPickConfig = {
    width		: "1600px",
    modalName	: "viewgermPickModal",
    formName	: "germPickStockView_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var deliveryGermPickingConfig = {
    width		: "1200px",
    modalName	: "deliveryGermPickingModal",
    formName	: "deliveryGermPickingForm",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#deliveryGermPickingForm").valid()){
                    return false;
                }
                var json=[];
                var data=$('#deliveryGermPickingForm #tb_list').bootstrapTable('getData');//获取选择行数据
                for(var i=0;i<data.length;i++){
                    var sz={"jzkcid":'',"llmxid":'',"cksl":'',"yds":'',"kcl":''};
                    sz.jzkcid=data[i].jzkcid;
                    sz.llmxid=data[i].llmxid;
                    var qlsl = parseFloat(data[i].qlsl);
                    var cksl = parseFloat(data[i].cksl);
                    if(cksl.toFixed(2)-qlsl.toFixed(2)>0){
                        $.error("第"+(i+1)+"行 出库数量不能大于请领数量！");
                        return false;
                    }
                    if (data[i].cksl){
                        sz.cksl=data[i].cksl;
                    }else {
                        sz.cksl=data[i].qlsl;
                    }
                    sz.yds=data[i].yds;
                    sz.kcl=data[i].kcl;
                    json.push(sz);
                }
                $("#deliveryGermPickingForm #ckmx_json").val(JSON.stringify(json));

                var $this = this;
                var opts = $this["options"]||{};
                $("#deliveryGermPickingForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"deliveryGermPickingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $.closeModal(opts.modalName);
                            searchgermPickResult();
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

var addGermPickingConfig = {
    width		: "1200px",
    modalName	: "addGermPickingModal",
    formName	: "editGermPickingForm",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editGermPickingForm").valid()){
                    return false;
                }
                var json=[];
                var data=$('#editGermPickingForm #tb_list').bootstrapTable('getData');//获取选择行数据
                if(data.length<1){
                    $.error("请新增明细！");
                    return false;
                }
                for(var i=0;i<data.length;i++){
                    var sz={"jzkcid":'',"klsl":'',"qlsl":'',"yds":''};
                    sz.jzkcid=data[i].jzkcid;
                    var qlsl = parseFloat(data[i].qlsl);
                    var klsl = parseFloat(data[i].klsl);
                    if(qlsl.toFixed(2)-klsl.toFixed(2)>0){
                        $.error("第"+(i+1)+"行 请领数量不能大于可请领数！");
                        return false;
                    }
                    sz.klsl=data[i].klsl;
                    sz.qlsl=data[i].qlsl;
                    sz.yds=data[i].yds;
                    json.push(sz);
                }
                $("#editGermPickingForm #llmx_json").val(JSON.stringify(json));

                var $this = this;
                var opts = $this["options"]||{};
                $("#editGermPickingForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editGermPickingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $.closeModal(opts.modalName);
                            //提交审核
                            if(responseText["auditType"]!=null){
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    searchgermPickResult();
                                });
                            }else{
                                searchgermPickResult();
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

var modGermPickingConfig = {
    width		: "1200px",
    modalName	: "modGermPickingModal",
    formName	: "editGermPickingForm",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editGermPickingForm").valid()){
                    return false;
                }
                var json=[];
                var data=$('#editGermPickingForm #tb_list').bootstrapTable('getData');//获取选择行数据
                if(data.length<1){
                    $.error("请新增明细！");
                    return false;
                }
                for(var i=0;i<data.length;i++){
                    var sz={"jzkcid":'',"klsl":'',"qlsl":'',"yds":''};
                    sz.jzkcid=data[i].jzkcid;
                    var qlsl = parseFloat(data[i].qlsl);
                    var klsl = parseFloat(data[i].klsl);
                    if(qlsl.toFixed(2)-klsl.toFixed(2)>0){
                        $.error("第"+(i+1)+"行 请领数量不能大于可请领数！");
                        return false;
                    }
                    sz.klsl=data[i].klsl;
                    sz.qlsl=data[i].qlsl;
                    sz.yds=data[i].yds;
                    json.push(sz);
                }
                $("#editGermPickingForm #llmx_json").val(JSON.stringify(json));

                var $this = this;
                var opts = $this["options"]||{};
                $("#editGermPickingForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editGermPickingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $.closeModal(opts.modalName);
                            searchgermPickResult();
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


//按钮动作函数
function germPickDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?llid=" +id;
        $.showDialog(url,'查看',viewgermPickConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'领料新增',addGermPickingConfig);
    }else if(action =='mod'){
        var url= tourl + "?llid=" +id;
        $.showDialog(url,'领料修改',modGermPickingConfig);
    }else if(action =='submit'){
        var url= tourl + "?llid=" +id;
        $.showDialog(url,'领料提交',addGermPickingConfig);
    }else if(action =='delivery'){
        var url= tourl + "?llid=" +id;
        $.showDialog(url,'领料出库',deliveryGermPickingConfig);
    }
}


var germPick_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#germPick_formSearch #btn_view");
        var btn_add = $("#germPick_formSearch #btn_add");
        var btn_mod = $("#germPick_formSearch #btn_mod");
        var btn_submit = $("#germPick_formSearch #btn_submit");
        var btn_delivery = $("#germPick_formSearch #btn_delivery");
        var btn_query = $("#germPick_formSearch #btn_query");
        var btn_lldprint = $("#germPick_formSearch #btn_lldprint");//领料单打印
        var btn_del = $("#germPick_formSearch #btn_del");
        var btn_discard = $("#germPick_formSearch #btn_discard");

        //添加日期控件
        laydate.render({
            elem: '#sqsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sqsjend'
            ,theme: '#2381E9'
        });
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchgermPickResult(true);
            });
        }
        btn_view.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                germPickDealById(sel_row[0].llid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_add.unbind("click").click(function(){
            germPickDealById(null,"add",btn_add.attr("tourl"));
        });
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    germPickDealById(sel_row[0].llid,"mod",btn_mod.attr("tourl"));
                }else{
                    $.error("请选择未审核或审核未通过的数据！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        btn_delivery.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].ckzt!='1'){
                    if(sel_row[0].zt=='80'){
                        germPickDealById(sel_row[0].llid,"delivery",btn_delivery.attr("tourl"));
                    }else{
                        $.error("请选择审核通过的数据！");
                    }
                }else{
                    $.error("该条数据已经出库！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    germPickDealById(sel_row[0].llid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.error("请选择未审核或审核未通过的数据！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 打印领料单 -----------------------------------*/
        btn_lldprint.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].llid;
            }
            ids = ids.substr(1);
            var url=btn_lldprint.attr("tourl")+"?ids="+ids.toString()+"&access_token="+$("#ac_tk").val();
            window.open(url);
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                var tgids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].zt=='80'){
                        tgids= tgids + ","+ sel_row[i].llid;
                    }else{
                        ids= ids + ","+ sel_row[i].llid;
                    }
                }
                ids=ids.substr(1);
                tgids=tgids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,tgids:tgids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchgermPickResult();
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
        /* ------------------------------废弃-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#germPick_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
                        $.error("该记录在审核中或已审核，不允许废弃!");
                        return;
                    }
                }
                var ids="";
                var tgids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].zt=='80'){
                        tgids= tgids + ","+ sel_row[i].llid;
                    }else{
                        ids= ids + ","+ sel_row[i].llid;
                    }
                }
                ids=ids.substr(1);
                tgids=tgids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,tgids:tgids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchgermPickResult();
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
        $("#germPick_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(germPick_turnOff){
                $("#germPick_formSearch #searchMore").slideDown("low");
                germPick_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#germPick_formSearch #searchMore").slideUp("low");
                germPick_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };

    return oInit;
};

function searchgermPickResult(isTurnBack){
    //关闭高级搜索条件
    $("#germPick_formSearch #searchMore").slideUp("low");
    germPick_turnOff=true;
    $("#germPick_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#germPick_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#germPick_formSearch #tb_list').bootstrapTable('refresh');
    }
}

$(function(){

    //1.初始化Table
    var oTable = new germPick_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new germPick_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#germPick_formSearch .chosen-select').chosen({width: '100%'});
});
