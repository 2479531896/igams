var clearingRecords_turnOff=true;
var clearingRecords_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#clearingRecords_formSearch #clearingRecords_list").bootstrapTable({
            url: $("#clearingRecords_formSearch #urlPrefix").val()+'/clearing/records/pageGetListClearingRecords',
            method: 'get',                      // 请求方式（*）
            toolbar: '#clearingRecords_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "qcrq",				// 排序字段
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
            uniqueId: "qcjlid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'qcjlid',
                title: '清场记录ID',
                titleTooltip:'清场记录ID',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:false
            },{
                field: 'qcrq',
                title: '清场日期',
                titleTooltip:'清场日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jlbh',
                title: '记录编号',
                titleTooltip:'记录编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'fjmc',
                title: '房间名称',
                titleTooltip:'房间名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'ssgx',
                title: '所属工序',
                titleTooltip:'所属工序',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '10%',
                align: 'left',
                formatter:wlbmformat,
                sortable: true,
                visible:true
            },{
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'ph',
                title: '批号',
                titleTooltip:'批号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'czrmc',
                title: '清场人',
                titleTooltip:'清场人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jcrmc',
                title: '检查人',
                titleTooltip:'检查人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jczp',
                title: '检查总评',
                titleTooltip:'检查总评',
                width: '6%',
                align: 'left',
                formatter:jczpformat,
                sortable: true,
                visible:true
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '12%',
                align: 'left',
                sortable: true,
                visible:true
            }, {
                field: 'zt',
                title: '审核状态',
                width: '8%',
                align: 'left',
                formatter:ztformat,
                sortable: true,
                visible: true
            }, {
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:czformat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                clearingRecords_DealById(row.qcjlid,'view',$("#clearingRecords_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#clearingRecords_formSearch #clearingRecords_list").colResizable({
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
            sortLastName: "qcjlid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getClearingRecordsSearchData(map);
    };
    return oTableInit;
}


function getClearingRecordsSearchData(map){
    var clearingRecords_select=$("#clearingRecords_formSearch #clearingRecords_select").val();
    var clearingRecords_input=$.trim(jQuery('#clearingRecords_formSearch #clearingRecords_input').val());
    if(clearingRecords_select=="0"){//全部
        map["entire"]=clearingRecords_input
    }else if(clearingRecords_select=="1"){
        map["jlbh"]=clearingRecords_input
    }else if(clearingRecords_select=="2"){
        map["fkmc"]=clearingRecords_input
    }else if(clearingRecords_select=="3"){
        map["ssgx"]=clearingRecords_input
    }else if(clearingRecords_select=="4"){
        map["wlbm"]=clearingRecords_input
    }else if(clearingRecords_select=="5"){
        map["wlmc"]=clearingRecords_input
    }else if(clearingRecords_select=="6"){
        map["ph"]=clearingRecords_input
    }else if(clearingRecords_select=="7"){
        map["czrmc"]=clearingRecords_input
    }else if(clearingRecords_select=="8"){
        map["jcrmc"]=clearingRecords_input
    }

    var jczp = jQuery('#clearingRecords_formSearch #jczp_id_tj').val();
    map["jczp"] = jczp;

    var qcrqstart = jQuery('#clearingRecords_formSearch #qcrqstart').val();
    map["qcrqstart"] = qcrqstart;

    var qcrqend = jQuery('#clearingRecords_formSearch #qcrqend').val();
    map["qcrqend"] = qcrqend;

    return map;
}

function wlbmformat(value,row,index) {
    var html = "";
    if(row.wlbm==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"viewByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";

    }
    return html;
}

function viewByWlbm(wlid){
    var url=$("#clearingRecords_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlbmConfig);
}

var viewWlbmConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function jczpformat(value,row,index){
    if(row.jczp=='1'){
        var jczp="<span style='color:green;'>"+"合格"+"</span>";
    }else if(row.jczp=='0'){
        var jczp="<span style='color:red;'>"+"不合格"+"</span>";
    }
    return jczp;
}

function ztformat(value,row,index) {
    var type = $("#clearingRecords_formSearch #auditType").val()
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qcjlid + "\",event,\""+type+"\",{prefix:\"" + $('#clearingRecords_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qcjlid + "\",event,\""+type+"\",{prefix:\"" + $('#clearingRecords_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qcjlid + "\",event,\""+type+"\",{prefix:\"" + $('#clearingRecords_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

function czformat(value,row,index) {
    if (row.zt == '10') {
        return "<span class='btn btn-warning' onclick=\"recallClearingRecords('" + row.qcjlid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallClearingRecords(qcjlid,event){
    var auditType = $("#clearingRecords_formSearch #auditType").val();
    var msg = '您确定要撤回该条记录审核吗？';
    var params = [];
    params.prefix = $("#clearingRecords_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,qcjlid,function(){
                searchClearingRecordsResult();
            },params);
        }
    });
}


function searchClearingRecordsResult(isTurnBack){
    //关闭高级搜索条件
    $("#clearingRecords_formSearch #searchMore").slideUp("low");
    clearingRecords_turnOff=true;
    $("#clearingRecords_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#clearingRecords_formSearch #clearingRecords_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#clearingRecords_formSearch #clearingRecords_list').bootstrapTable('refresh');
    }
}

function clearingRecords_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#clearingRecords_formSearch #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?qcjlid="+id;
        $.showDialog(url,'查看详细信息',viewClearingRecordsConfig);
    }else if(action=="clear"){
        var url=tourl;
        $.showDialog(url,'清场',clearClearingRecordsConfig);
    }else if(action=="mod"){
        var url=tourl+"?qcjlid="+id;
        $.showDialog(url,'修改',clearClearingRecordsConfig);
    }
}
var viewClearingRecordsConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var clearClearingRecordsConfig = {
    width		: "1200px",
    modalName	: "clearClearingRecordsModel",
    formName	: "clearClearingRecordsForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#clearClearingRecordsForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var json=[];
                var rows=$('#clearClearingRecordsForm #tb_list').bootstrapTable('getData');
                for(var i=0;i<rows.length;i++){
                    var sz={"xmid":'',"jg":''};
                    sz.xmid=rows[i].xmid;
                    sz.jg=$('input:radio[name="jg'+i+'"]:checked').val();
                    json.push(sz);
                }
                $("#clearClearingRecordsForm #qcxm_json").val(JSON.stringify(json));

                var $this = this;
                var opts = $this["options"]||{};

                $("#clearClearingRecordsForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"clearClearingRecordsForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                             //提交审核
                             if(responseText["auditType"]!=null){
                                 var params=[];
                                 params.prefix=$("#clearingRecords_formSearch #urlPrefix").val();
                                 showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                     $.closeModal(opts.modalName);
                                     searchClearingRecordsResult();
                                 },null,params);
                             }else{
                                 searchClearingRecordsResult();
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


var clearingRecords_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#clearingRecords_formSearch #btn_query");
        var btn_view=$("#clearingRecords_formSearch #btn_view");
        var btn_del=$("#clearingRecords_formSearch #btn_del");
        var btn_clear=$("#clearingRecords_formSearch #btn_clear");
        var btn_mod=$("#clearingRecords_formSearch #btn_mod");
        var btn_print=$("#clearingRecords_formSearch #btn_print");
        var btn_certificateprint=$("#clearingRecords_formSearch #btn_certificateprint");
        //添加日期控件
        laydate.render({
            elem: ' #clearingRecords_formSearch #qcrqstart'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: ' #clearingRecords_formSearch #qcrqend'
            , theme: '#2381E9'
        });

        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchClearingRecordsResult(true);
            });
        }
        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#clearingRecords_formSearch #clearingRecords_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                clearingRecords_DealById(sel_row[0].qcjlid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------新增页面--------------------------------*/
        btn_clear.unbind("click").click(function(){
            clearingRecords_DealById(null,"clear",btn_clear.attr("tourl"));
        });
        /*---------------------------修改页面--------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#clearingRecords_formSearch #clearingRecords_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    clearingRecords_DealById(sel_row[0].qcjlid,"mod",btn_mod.attr("tourl"));
                }else{
                    $.error("未提交以及审核未通过数据可以进行修改操作!");
                    return;
                }
            }else{
                $.error("请选中一行");
            }
        });
         /*---------------------------打印页面--------------------------------*/
        btn_print.unbind("click").click(function(){
                    var sel_row = $("#clearingRecords_formSearch #clearingRecords_list").bootstrapTable('getSelections');//获取选择行数据
                    if(sel_row.length == 1){
                        var url=$('#clearingRecords_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?qcjlid="+sel_row[0].qcjlid+"&access_token="+$("#ac_tk").val();
                        window.open(url);
                    }else{
                        $.error("请选中一行");
                        return;
                    }
                });
        /*---------------------------打印页面--------------------------------*/

        btn_certificateprint.unbind("click").click(function(){
                    var sel_row = $("#clearingRecords_formSearch #clearingRecords_list").bootstrapTable('getSelections');//获取选择行数据
                    if(sel_row.length == 1){
                        var url=$('#clearingRecords_formSearch #urlPrefix').val()+btn_certificateprint.attr("tourl")+"?qcjlid="+sel_row[0].qcjlid+"&access_token="+$("#ac_tk").val();
                        window.open(url);
                    }else{
                        $.error("请选中一行");
                        return;
                    }
                });



        /* ------------------------------删除列表-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#clearingRecords_formSearch #clearingRecords_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].qcjlid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#clearingRecords_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchClearingRecordsResult();
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
        $("#clearingRecords_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(clearingRecords_turnOff){
                $("#clearingRecords_formSearch #searchMore").slideDown("low");
                clearingRecords_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#clearingRecords_formSearch #searchMore").slideUp("low");
                clearingRecords_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oButtonInit;
}


$(function(){
    var oTable = new clearingRecords_TableInit();
    oTable.Init();

    var oButton = new clearingRecords_oButton();
    oButton.Init();

    jQuery('#clearingRecords_formSearch .chosen-select').chosen({width: '100%'});
})