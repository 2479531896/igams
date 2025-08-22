var paperReportsApply_turnOff=true;
// 列表初始化
var paperReportsApply_TableInit=function(){
    var oTableInit=new Object();
    // 初始化table
    oTableInit.Init=function(){
        $("#paperReportsApply_formSearch #paperReportsApply_list").bootstrapTable({
            url: '/inspection/pageGetListPaperReportsApply',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#paperReportsApply_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sjzzsq.sfsd asc,sjzzsq.sdrq desc,sjzzsq.sjr,sjzzsq.dz,sjxx.bgrq ",					//排序字段
            sortOrder: "desc",               	//排序方式
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
            //height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "zzsqid",                	//每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                	//是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '6%',
                align: 'left',
                formatter: function ybbhFormat(value,row,index) {
                    if(row.sjid){
                        return "<a style='cursor:pointer;' onclick=\"toViewInspection('"+row.sjid+"')\"><span>"+row.ybbh+"</span></a>";
                    }else{
                        return "<span>"+row.ybbh+"</span>";
                    }
                },
                sortable: true,
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sjhb',
                title: '伙伴',
                titleTooltip:'伙伴',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yblx',
                title: '样本类型',
                titleTooltip:'样本类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jcxm',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sjr',
                title: '收件人',
                titleTooltip:'收件人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fs',
                title: '份数',
                titleTooltip:'份数',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'dh',
                title: '电话',
                titleTooltip:'电话',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dz',
                title: '地址',
                titleTooltip:'地址',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqr',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sfsd',
                title: '是否锁定',
                titleTooltip:'是否锁定',
                width: '6%',
                align: 'left',
                formatter:sfsdFormat,
                sortable: true,
                visible: true
            },{
                field: 'sdrymc',
                title: '锁定人员',
                titleTooltip:'锁定人员',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sdrq',
                title: '锁定日期',
                titleTooltip:'锁定日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bh',
                title: '包号',
                titleTooltip:'包号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'mailno',
                title: '快递单号',
                titleTooltip:'快递单号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter: function mailnoFormat(value,row,index) {
                    if(row.mailno!=null&&row.mailno!=''){
                        return "<a style='cursor:pointer;' onclick='toViewExpress(text)'><span>"+row.mailno+"</span></a>";
                    }else{
                        return "";
                    }
                }
            },{
                field: 'sfqx',
                title: '取消订单',
                titleTooltip:'取消订单',
                width: '6%',
                formatter:sfqxFormat,
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'zt',
                title: '状态',
                titleTooltip:'状态',
                width: '8%',
                formatter:ztFormat,
                align: 'left',
                sortable: true,
                visible: true
            },{
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
                // 双击事件
                paperReportsApplyDealById(row.zzsqid,'view',$("#paperReportsApply_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#paperReportsApply_formSearch #paperReportsApply_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjzzsq.lrsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return paperReportsApplySearchData(map);
    };
    return oTableInit
};
function toViewExpress(kdh){
    var url="/express/express/pagedataExpress?mailno="+kdh+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物流详情',viewExpressConfig);
}
/*查看详情信息模态框*/
var viewExpressConfig = {
    width		: "800px",
    height		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function toViewInspection(sjid){
    var url="/inspection/inspection/viewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewInspectionConfig);

}
/*查看送检信息模态框*/
var viewInspectionConfig = {
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

function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.zzsqid + "\",event,\"AUDIT_PAPERREPORTAPPLY\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.zzsqid + "\",event,\"AUDIT_PAPERREPORTAPPLY\")' >审核未通过</a>";
    }else if(row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.zzsqid + "\",event,\"AUDIT_PAPERREPORTAPPLY\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

function sfqxFormat(value,row,index) {
    if (row.sfqx == '1') {
        return "<span style='color: red'>已取消<span>";
    }else {
        return "";
    }
}

function sfsdFormat(value,row,index) {
    if (row.sfsd == '1') {
        return "<span style='color: red'>是<span>";
    }else{
        return "<span style='color: green'>否<span>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czformat(value,row,index) {
    if (row.zt == '10' ) {
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.zzsqid +"','" + "AUDIT_PAPERREPORTAPPLY"+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

function recallRequisitions(zzsqid,auditType,event){
    var msg = '您确定要撤回该记录吗？';
    var purchase_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,zzsqid,function(){
                searchPaperReportsApplyResult();
            },purchase_params);
        }
    });
}

// 条件搜索
function paperReportsApplySearchData(map){
    var cxtj=$("#paperReportsApply_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#paperReportsApply_formSearch #cxnr').val());
    if(cxtj=="0"){
        // 全部
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        // 标本编号
        map["ybbh"]=cxnr
    }else if(cxtj=="2"){
        // 内部编码
        map["nbbm"]=cxnr
    }else if(cxtj=='3'){
        // 患者姓名
        map["hzxm"]=cxnr
    }else if(cxtj=='4'){
        // 伙伴
        map["sjhb"]=cxnr
    }else if(cxtj=='5'){
        // 收件人
        map["sjr"]=cxnr
    }else if(cxtj=='6'){
        // 电话
        map["dh"]=cxnr
    }else if(cxtj=='7'){
        // 快递单号
        map["mailno"]=cxnr
    }else if(cxtj=='8'){
        map["sdrymc"]=cxnr
    }else if(cxtj=='9'){
        map["bh"]=cxnr
    }

    // 开始日期
    var sdrqstart = jQuery('#paperReportsApply_formSearch #sdrqstart').val();
    map["sdrqstart"] = sdrqstart;
    // 结束日期
    var sdrqend = jQuery('#paperReportsApply_formSearch #sdrqend').val();
    map["sdrqend"] = sdrqend;
    // 开始日期
    var sqsjstart = jQuery('#paperReportsApply_formSearch #sqsjstart').val();
    map["sqsjstart"] = sqsjstart;
    // 结束日期
    var sqsjend = jQuery('#paperReportsApply_formSearch #sqsjend').val();
    map["sqsjend"] = sqsjend;
    var kdlxs = jQuery('#paperReportsApply_formSearch #kdlx_id_tj').val();
    map["kdlxs"] = kdlxs;
    var sfsd=jQuery('#paperReportsApply_formSearch #sfsd_id_tj').val();
    map["sfsd"]=sfsd;
    var single_flag=jQuery('#paperReportsApply_formSearch #single_flag').val();
    map["single_flag"]=single_flag;
    return map;
}

// 列表刷新
function searchPaperReportsApplyResult(isTurnBack){
//关闭高级搜索条件
    $("#paperReportsApply_formSearch #searchMore").slideUp("low");
    paperReportsApply_turnOff=true;
    $("#paperReportsApply_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('refresh');
    }
}

//提供给导出用的回调函数
function sjzzsqSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="sjzzsq.zzsqid";
    map["sortLastOrder"]="asc";
    map["sortName"]="sjzzsq.lrsj";
    map["sortOrder"]="asc";
    map["yhid"]=$("#yhid").val();
    return paperReportsApplySearchData(map);
}

// 按钮动作函数
function paperReportsApplyDealById(id,action,tourl){
    var url= tourl;
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?zzsqid="+id;
        $.showDialog(url,'详情',viewPaperReportsApplyConfig);
    }else if(action =='add'){
        $.showDialog(url,'新增',addPaperReportsApplyConfig);
    }else if(action =='mod'){
        var url= tourl + "?zzsqid="+id;
        $.showDialog(url,'修改',modPaperReportsApplyConfig);
    }else if(action =='submit'){
        var url= tourl+"?zzsqid="+id;
        $.showDialog(url,'提交',submitPaperReportsApplyConfig);
    }else if(action == "pack"){
        var url= tourl+"?ids="+id;
        $.showDialog(url,'打包',packPaperReportsApplyConfig);
    }
}

// 按钮初始化
var paperReportsApply_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化页面上面的按钮事件
        // 查询
        var btn_query=$("#paperReportsApply_formSearch #btn_query");
        // 查看
        var btn_view=$("#paperReportsApply_formSearch #btn_view");
        // 新增
        var btn_add=$("#paperReportsApply_formSearch #btn_add");
        // 修改
        var btn_submit = $("#paperReportsApply_formSearch #btn_submit");
        // 提交
        var btn_mod = $("#paperReportsApply_formSearch #btn_mod");
        // 删除
        var btn_del = $("#paperReportsApply_formSearch #btn_del");
        // 锁定
        var btn_lock = $("#paperReportsApply_formSearch #btn_lock");
        // 取消快递订单号
        var btn_cancelOrder = $("#paperReportsApply_formSearch #btn_cancelorder");
        // 打包
        var btn_pack = $("#paperReportsApply_formSearch #btn_pack");
        //取消锁定
        var btn_cancelLock = $("#paperReportsApply_formSearch #btn_cancellock");
        //添加日期控件
        laydate.render({
            elem: '#paperReportsApply_formSearch #sdrqstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#paperReportsApply_formSearch #sdrqend'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: ' #paperReportsApply_formSearch #sqsjstart'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        //添加日期控件
        laydate.render({
            elem: ' #paperReportsApply_formSearch #sqsjend'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        var btn_selectexport = $("#paperReportsApply_formSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#paperReportsApply_formSearch #btn_searchexport");//搜索导出
        /*--------------------模糊查询--------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPaperReportsApplyResult(true);
            });
        }
        /*--------------------重新下单--------------------*/
        btn_pack.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].zzsqid;
                }
                ids=ids.substr(1);
                paperReportsApplyDealById(ids, "pack", btn_pack.attr("tourl"));
            }
        });
        /*--------------------取消订单--------------------*/
        btn_cancelOrder.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    var mailno = sel_row[0].mailno;
                    var bh = sel_row[0].bh;
                    if ( sel_row[i].mailno != mailno ){
                        $.error("确认选中数据快递单号是否一致");
                        return
                    }
                    if ( sel_row[i].bh != bh ){
                        $.error("确认选中数据包号是否一致");
                        return
                    }
                    // if ( sel_row[i].sfqx == '1' ){
                    //     $.error(sel_row[i].hzxm + "的快递" + sel_row[i].mailno + "快递订单已被取消");
                    //     return
                    // }
                    if ( sel_row[i].zt == '00' ){
                        $.error(sel_row[i].hzxm + "的纸质申请数据，该状态未提交不允许取消");
                        return
                    }
                    ids= ids + ","+ sel_row[i].zzsqid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要取消快递订单吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_cancelOrder.attr("tourl");
                        jQuery.post(url,{ids:ids,bh:sel_row[0].bh,mailno:sel_row[0].mailno,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchPaperReportsApplyResult(true);
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
        /*--------------------查　　看--------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                paperReportsApplyDealById(sel_row[0].zzsqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------新　　增--------------------*/
        btn_add.unbind("click").click(function(){
            paperReportsApplyDealById(null,"add",btn_add.attr("tourl"));
        });
        /*--------------------提　　交--------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row=$('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    paperReportsApplyDealById(sel_row[0].zzsqid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.alert("该状态不允许提交!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------修　　改--------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                paperReportsApplyDealById(sel_row[0].zzsqid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------删　　除--------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].zzsqid;
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
                                        searchPaperReportsApplyResult(true);
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


        /*-----------------------选中导出------------------------------------*/
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].zzsqid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=PAPER_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
            }else{
                $.error("请选择数据");
            }
        });
        /*-----------------------搜索导出------------------------------------*/
        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=PAPER_SEARCH&expType=search&callbackJs=sjzzsqSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });


        /*--------------------锁   定--------------------*/
        btn_lock.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].bgrq!=null&&sel_row[i].bgrq!=''){
                        ids= ids + ","+ sel_row[i].zzsqid;
                    }else{
                        $.error("未出报告日期的数据无法进行锁定！");
                        return;
                    }

                }
                ids=ids.substr(1);
                $.confirm('您确定要对所选择的信息进行锁定吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_lock.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchPaperReportsApplyResult(true);
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
        /*--------------------取消锁定--------------------*/
        btn_cancelLock.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_formSearch #paperReportsApply_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].sfsd!='1'){
                        $.error("所选数据存在未锁定的数据！");
                        return;
                    }else{
                        ids= ids + ","+ sel_row[i].zzsqid+"_"+ sel_row[i].ybbh+"_"+ sel_row[i].sdry;
                    }
                }
                ids=ids.substr(1);
                $.confirm('您确定要对所选择的信息进行取消锁定吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_cancelLock.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchPaperReportsApplyResult(true);
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
        $("#paperReportsApply_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(paperReportsApply_turnOff){
                $("#paperReportsApply_formSearch #searchMore").slideDown("low");
                paperReportsApply_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#paperReportsApply_formSearch #searchMore").slideUp("low");
                paperReportsApply_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oInit;
}

// 查看
var viewPaperReportsApplyConfig = {
    width : "800px",
    modalName : "viewPaperReportsApplyModal",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关　闭",
            className : "btn-default"
        }
    }
};

// 新增
var addPaperReportsApplyConfig = {
    width : "800px",
    modalName : "addPaperReportsApplyModal",
    formName : "addPaperReportsApplyForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确　定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addPaperReportsApplyForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                if($("#addPaperReportsApplyForm #ybbh").val()==""||$("#addPaperReportsApplyForm #ybbh").val()==null){
                    $.alert("请输入标本编号！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#addPaperReportsApplyForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addPaperReportsApplyForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    searchPaperReportsApplyResult();
                                });
                            }else{
                                searchPaperReportsApplyResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {});
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
}

// 修改
var modPaperReportsApplyConfig = {
    width : "800px",
    modalName : "modPaperReportsApplyModal",
    formName : "addPaperReportsApplyForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addPaperReportsApplyForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#addPaperReportsApplyForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addPaperReportsApplyForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchPaperReportsApplyResult(true);
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        $.alert(responseText["message"],function() {});
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

// 提交
var submitPaperReportsApplyConfig = {
    width : "800px",
    modalName : "submitPaperReportsApplyModal",
    formName : "addPaperReportsApplyForm",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addPaperReportsApplyForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#addPaperReportsApplyForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addPaperReportsApplyForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    searchPaperReportsApplyResult();
                                });
                            }else{
                                searchPaperReportsApplyResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        $.alert(responseText["message"],function() {});
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
}

// 查看
var printDetailsConfig = {
    width : "800px",
    modalName : "printDetailsModal",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关　闭",
            className : "btn-default"
        }
    }
};

// 打包
var packPaperReportsApplyConfig = {
    width : "1000px",
    modalName : "packPaperReportsApplyModal",
    formName : "packPaperReportsApplyForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        print : {
            label : "打印",
            className : "btn-success",
            callback : function() {
                var sel_row = $('#packPaperReportsApplyForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==0){
                    $.error("请至少选中一行");
                    return false;
                }else {
                    var ids="";
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids= ids + ","+ sel_row[i].zzsqid;
                        if (sel_row[i].fjid) {
                            // window.open("http://172.17.53.52:8086/ws/file/pdfFileStream?fjid="+sel_row[i].fjid, "_blank")
                            $('#packPaperReportsApplyForm #print_'+sel_row[i].fjid)[0].contentWindow.print();
                        }
                    }
                    ids=ids.substr(1);
                    var url='/ws/paperReport/printDetails?ids='+ids+'&access_token='+$("#ac_tk").val()
                    window.open(url,"printDetails")
                    // $.showDialog(url,'打印清单',printDetailsConfig);
                }
                return false;
            }
        },
        success : {
            label : "打包",
            className : "btn-primary",
            callback : function() {
                if(!$("#packPaperReportsApplyForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#packPaperReportsApplyForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"packPaperReportsApplyForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchPaperReportsApplyResult(true);
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
        successtwo : {
            label : "取消打包",
            className : "btn-danger",
            callback : function() {
                if(!$("#packPaperReportsApplyForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $('#packPaperReportsApplyForm #cz_flg').val("cancel");
                $("#packPaperReportsApplyForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"packPaperReportsApplyForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchPaperReportsApplyResult(true);
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
}

// 页面初始化
$(function(){
    // 0.初始化界面
    // var oInit = new paperReportsApply_PageInit();
    // oInit.Init();
    // 1.初始化Table
    var oTable = new paperReportsApply_TableInit();
    oTable.Init();
    // 2.初始化Button的点击事件
    var oButtonInit = new paperReportsApply_ButtonInit();
    oButtonInit.Init();
    jQuery('#paperReportsApply_formSearch .chosen-select').chosen({width: '100%'});
});