var annualPlan_turnOff=true;
var annualPlan_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#annualPlanFormSearch #annualPlan_list').bootstrapTable({
            url: $("#annualPlanFormSearch #urlPrefix").val()+'/annualPlan/annualPlan/pageGetListAnnualPlan',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#annualPlanFormSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"ndpx.nd",					// 排序字段
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
            uniqueId: "ndpxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'ndpxid',
                title: '年度培训ID',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'nd',
                title: '年度',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'pxmc',
                title: '培训名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'jhpxsj',
                title: '计划培训时间',
                width: '7%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'cpbmry',
                title: '参培实施部门/人员',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ssbmmc',
                title: '实施部门',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'jsxm',
                title: '培训讲师',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'ks',
                title: '培训课时(小时)',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'pxfsmc',
                title: '培训方式',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'lb',
                title: '类别',
                width: '6%',
                align: 'left',
                formatter:lbFormat,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '9%',
                align: 'left',
                visible: true
            },{
                field: 'sjpxsj',
                title: '实际培训时间',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ycjrs',
                title: '应参加人数',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'cprs',
                title: '参培人数',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'qqrs',
                title: '缺勤人数',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'pxjlbh',
                title: '培训记录编号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '8%',
                align: 'left',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                annualPlanById(row.ndpxid,'view',$("#annualPlanFormSearch #btn_view").attr("tourl"));
            }
        });
        $("#annualPlanFormSearch #annualPlan_list").colResizable({
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
            sortLastName: "ndpx.jhpxsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return annualPlanSearchData(map);
    };
    return oTableInit;
};

function lbFormat(value,row,index){
    if(row.lb=="1"){
        var sfsc="<span style='color:red;'>计划外</span>"
        return sfsc;
    }else{
        var sfsc="<span style='color:green;'>计划内</span>"
        return sfsc;
    }
}

function annualPlanSearchData(map){
    var cxtj=$("#annualPlanFormSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#annualPlanFormSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["pxmc"]=cxnr;
    }else if(cxtj=="2"){
        map["cpbmry"]=cxnr;
    }else if(cxtj=="3"){
        map["jsxm"]=cxnr;
    }else if(cxtj=="4"){
        map["ssbmmc"]=cxnr;
    }else if(cxtj=="5"){
        map["pxjlbh"]=cxnr;
    }
    var jhpxsjstart = jQuery('#annualPlanFormSearch #jhpxsjstart').val();
    map["jhpxsjstart"] = jhpxsjstart;
    var jhpxsjend = jQuery('#annualPlanFormSearch #jhpxsjend').val();
    map["jhpxsjend"] = jhpxsjend;
    
    var sjpxsjstart = jQuery('#annualPlanFormSearch #sjpxsjstart').val();
    map["sjpxsjstart"] = sjpxsjstart;
    var sjpxsjend = jQuery('#annualPlanFormSearch #sjpxsjend').val();
    map["sjpxsjend"] = sjpxsjend;
    
    var nds = jQuery('#annualPlanFormSearch #nd_id_tj').val();
    map["nds"] = nds.replace(/'/g, "");
    
    var pxfss = jQuery('#annualPlanFormSearch #pxfs_id_tj').val();
    map["pxfss"] = pxfss.replace(/'/g, "");

    var lb = jQuery('#annualPlanFormSearch #lb_id_tj').val();
    map["lb"] = lb;
    return map;
}

function annualPlanById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#annualPlanFormSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?ndpxid=" +id;
        $.showDialog(url,'查看',viewAnnualPlanConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增年度培训',addAnnualPlanConfig);
    }else if(action =='mod'){
        var url=tourl + "?ndpxid=" +id;
        $.showDialog(url,'修改年度培训',modAnnualPlanConfig);
    }
}

var viewAnnualPlanConfig = {
    width		: "1400px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addAnnualPlanConfig = {
    width		: "1200px",
    modalName	: "addAnnualPlanModal",
    formName	: "editAnnualPlanForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editAnnualPlanForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editAnnualPlanForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editAnnualPlanForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        preventResubmitForm(".modal-footer > button", false);
                        $.success(responseText["message"],function() {
                            searchAnnualPlanResult();
                            $("#editAnnualPlanForm #pxmc").val("");
                            $("#editAnnualPlanForm #cpbmry").val("");
                            $("#editAnnualPlanForm #nd").val("");
                            $("#editAnnualPlanForm #jhpxsj").val("");
                            $("#editAnnualPlanForm #ssbmmc").val("");
                            $("#editAnnualPlanForm #ssbm").val("");
                            $("#editAnnualPlanForm #jsxm").val("");
                            $("#editAnnualPlanForm #js").val("");
                            $("#editAnnualPlanForm #ks").val("");
                            $("#editAnnualPlanForm #pxfs").val("");
                            $("#editAnnualPlanForm #pxfs").trigger("chosen:updated");
                            $("#editAnnualPlanForm #bz").text("");
                            $("#editAnnualPlanForm #bz").val("");
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
            className : "btn-default",
            callback : function() {
                searchAnnualPlanResult();
            }
        }
    }
};

var modAnnualPlanConfig = {
    width		: "1200px",
    modalName	: "modAnnualPlanModal",
    formName	: "editAnnualPlanForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editAnnualPlanForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editAnnualPlanForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editAnnualPlanForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchAnnualPlanResult();
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
            className : "btn-default",
        }
    }
};

var annualPlan_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    //添加日期控件
    laydate.render({
        elem: '#annualPlanFormSearch #jhpxsjstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#annualPlanFormSearch #jhpxsjend'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#annualPlanFormSearch #sjpxsjstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#annualPlanFormSearch #sjpxsjend'
        ,theme: '#2381E9'
    });
    oInit.Init = function () {
        var btn_query = $("#annualPlanFormSearch #btn_query");
        var btn_view = $("#annualPlanFormSearch #btn_view");
        var btn_searchexport = $("#annualPlanFormSearch #btn_searchexport");
        var btn_selectexport = $("#annualPlanFormSearch #btn_selectexport");
        var btn_add = $("#annualPlanFormSearch #btn_add");
        var btn_mod = $("#annualPlanFormSearch #btn_mod");
        var btn_del=$("#annualPlanFormSearch #btn_del");
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchAnnualPlanResult(true);
            });
        }
        /* ---------------------------查看-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#annualPlanFormSearch #annualPlan_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                annualPlanById(sel_row[0].ndpxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#annualPlanFormSearch #annualPlan_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].ndpxid;
                }
                ids = ids.substr(1);
                $.showDialog($('#annualPlanFormSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ANNUALPLAN_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#annualPlanFormSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=ANNUALPLAN_SEARCH&expType=search&callbackJs=annualPlanExportData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            annualPlanById(null, "add", btn_add.attr("tourl"));
        });
        /* ---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#annualPlanFormSearch #annualPlan_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                annualPlanById(sel_row[0].ndpxid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------删除-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#annualPlanFormSearch #annualPlan_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].ndpxid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#annualPlanFormSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchAnnualPlanResult();
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
        /*-----------------------显示隐藏------------------------------------*/
        $("#annualPlanFormSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(annualPlan_turnOff){
                $("#annualPlanFormSearch #searchMore").slideDown("low");
                annualPlan_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#annualPlanFormSearch #searchMore").slideUp("low");
                annualPlan_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};

function annualPlanExportData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="ndpx.jhpxsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="ndpx.nd";
    map["sortOrder"]="desc";
    return annualPlanSearchData(map);
}


function searchAnnualPlanResult(isTurnBack){
    //关闭高级搜索条件
    $("#annualPlanFormSearch #searchMore").slideUp("low");
    annualPlan_turnOff=true;
    $("#annualPlanFormSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#annualPlanFormSearch #annualPlan_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#annualPlanFormSearch #annualPlan_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new annualPlan_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new annualPlan_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#annualPlanFormSearch .chosen-select').chosen({width: '100%'});

    $("#annualPlanFormSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});