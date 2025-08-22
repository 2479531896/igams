var paperReportsApplyAudit_turnOff = true;
var paperReportsApply_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#paperReportsApply_audit_formSearch #paperReportsApply_audit_list").bootstrapTable({
            url: '/inspection/pageGetListAuditPaperReportsApply',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#paperReportsApply_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "bh asc,sjr asc,dz asc,sqsj",				//排序字段
            sortOrder: "asc",                   //排序方式
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
            uniqueId: "zzsqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'bh',
                title: '包号',
                titleTooltip:'包号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'zzsqid',
                title: '纸质申请ID',
                titleTooltip:'纸质申请ID',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'nbbm',
                title: '内部编码',
                titleTooltip:'内部编码',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'sjhb',
                title: '伙伴',
                titleTooltip:'伙伴',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'yblx',
                title: '样本类型',
                titleTooltip:'样本类型',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'jcxm',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'sjr',
                title: '收件人',
                titleTooltip:'收件人',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'dh',
                title: '电话',
                titleTooltip:'电话',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'dz',
                title: '地址',
                titleTooltip:'地址',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'fs',
                title: '份数',
                titleTooltip:'份数',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'lrrymc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'mailno',
                title: '快递单号',
                titleTooltip:'快递单号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '审核申请时间',
                titleTooltip:'审核申请时间',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Apply_audit_DealById(row.zzsqid,"view",$("#paperReportsApply_audit_formSearch #btn_view").attr("tourl"));
            },
            rowStyle: function (row, index) {
                //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
                if(row.bh){
                    var s = row.bh.substr(0,4);
                    var bh=parseInt(s);
                    if (bh%2==0) {
                        return { classes: 'success' }
                    }else{
                        return { classes: 'warning' }
                    }
                }else{
                    return {};
                }
            },
        });
        $("#paperReportsApply_audit_formSearch #paperReportsApply_audit_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "zzsqid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#paperReportsApply_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#paperReportsApply_audit_formSearch #cxnr').val());
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
            // 快递单号
            map["dz"]=cxnr
        }
        map["dqshzt"] = 'dsh';
        var bgzt = jQuery('#paperReportsApply_audit_formSearch #bgzt_id_tj').val();
        map["bgzt"] = bgzt
        return map;
    };
    return oTableInit;
};

var paperReportsApplyAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#paperReportsApply_audit_audited #tb_list").bootstrapTable({
            url:'/inspection/pageGetListAuditPaperReportsApply',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#paperReportsApply_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "desc",                  //排序方式
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
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#paperReportsApply_audit_audited #tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'nbbm',
                title: '内部编码',
                titleTooltip:'内部编码',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'sjhb',
                title: '伙伴',
                titleTooltip:'伙伴',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'yblx',
                title: '样本类型',
                titleTooltip:'样本类型',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'jcxm',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'sjr',
                title: '收件人',
                titleTooltip:'收件人',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'dh',
                title: '电话',
                titleTooltip:'电话',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'dz',
                title: '地址',
                titleTooltip:'地址',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'fs',
                title: '份数',
                titleTooltip:'份数',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'lrrymc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'mailno',
                title: '快递单号',
                titleTooltip:'快递单号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                titleTooltip:'审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                titleTooltip:'审核时间',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                titleTooltip:'是否通过',
                width: '5%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Apply_audit_DealById(row.zzsqid,"view",$("#paperReportsApply_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#paperReportsApply_audit_audited #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        });
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
            sortLastName: "zzsqid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#paperReportsApply_audit_audited #cxtj").val();
        var cxnr=$.trim(jQuery('#paperReportsApply_audit_audited #cxnr').val());
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
            // 快递单号
            map["dz"]=cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

function Apply_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?zzsqid="+id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_PAPERREPORTAPPLY',
            url: tourl,
            data:{'ywzd':'zzsqid'},
            title:"纸质报告申请审核",
            preSubmitCheck:'preSubmitPaperReportsApply',
            callback:function(){
                searchApply_audit_Result(true);//回调
            },
            dialogParam:{width:1000}
        });
    }else if(action =='batchaudit'){
        var url="/systemcheck/auditProcess/batchaudit?ywids=" +id+"&shlb=AUDIT_PAPERREPORTAPPLY&ywzd=zzsqid&business_url="+tourl;
        $.showDialog(url,'批量审核',batchAuditConfig);
    }
}

var batchAuditConfig = {
    width		: "800px",
    modalName	: "batchAuditModal",
    formName	: "batchAuditAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#batchAuditAjaxForm").valid()){
                    return false;
                }
                var data = {};
                data["ywids"] =  $("#batchAuditPaperReportsApplyForm #ids").val();
                data["kdlx"] =  $("#batchAuditPaperReportsApplyForm #kdlx").find("option:selected").val();
                data["jdlx"] = $("#batchAuditPaperReportsApplyForm #jdlx").find("option:selected").val();
                data["access_token"] = $("#ac_tk").val();
                var status=true;
                $.ajax({
                    async:false,
                    type : "post",
                    url: "/inspection/batchauditSavePaperReportsApply",
                    data : data,
                    dataType : "json",
                    success : function(data){
                        if(data.status=='fail'){
                            $.error(data.message);
                            status=false;
                        }
                    }
                });
                if(!status){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#batchAuditAjaxForm input[name='access_token']").val($("#ac_tk").val());
                var loadYmCode = $("#batchAuditAjaxForm #batchaudit_loadYmCode").val();
                var sel_row = $('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('getSelections');//获取选择行数据
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
                    //1.启动进度条检测
                    setTimeout("checkAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);

                    //绑定导出取消按钮事件
                    $("#exportCancel").click(function(){
                        //先移除导出提示，然后请求后台
                        if($("#cardiv").length>0) $("#cardiv").remove();
                        $.ajax({
                            type : "POST",
                            url : "/systemcheck/auditProcess/cancelAuditProcess",
                            data : {"loadYmCode" : loadYmCode+"","access_token":$("#ac_tk").val()},
                            dataType : "json",
                            success:function(data){
                                if(data != null && data.result==false){
                                    if(data.msg && data.msg!="")
                                        $.error(data.msg);
                                }
                            }
                        });
                    });
                });
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

//上传后自动提交服务器检查进度
var checkAuditCheckStatus = function(intervalTime,loadYmCode){
    $.ajax({
        type : "POST",
        url : "/systemcheck/auditProcess/commCheckAudit",
        data : {"loadYmCode":loadYmCode,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.cancel){//取消则直接return
                return;
            }
            if(data.result==false){
                if(data.status == "-2"){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if(intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if($("#exportCount")){
                        $("#exportCount").html(data.currentCount)
                    }
                    setTimeout("checkAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
                }
            }else{
                if(data.status == "2"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.success(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchApply_audit_Result();
                    });
                }else if(data.status == "0"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.alert(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchApply_audit_Result();
                    });
                }else{
                    if($("#cardiv")) $("#cardiv").remove();
                    $.error(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchApply_audit_Result();
                    });
                }
            }
        }
    });
}

function preSubmitPaperReportsApply(){
    // if(!$("#viewPaperReportsApplyForm").valid()){
    //     $.alert("请填写完整信息");
    //     return false;
    // }
    return true;
}

var paperReportsApply_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#paperReportsApply_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#paperReportsApply_audit_audited #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#paperReportsApply_audit_audited #btn_cancelAudit");//取消审核
        var btn_view = $("#paperReportsApply_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#paperReportsApply_audit_formSearch #btn_audit");//审核
        var btn_batchaudit = $("#paperReportsApply_audit_formSearch #btn_batchaudit");

        //批量审核
        btn_batchaudit.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            var bh=sel_row[0].bh;
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(!sel_row[i].bh){
                    $.error("没有包号无法审核！");
                    return;
                }
                if(bh==sel_row[i].bh){
                    ids = ids + ","+ sel_row[i].zzsqid;
                }else{
                    $.error("包号不一致！无法进行批量审核！");
                    return;
                }
            }
            ids = ids.substr(1);
            Apply_audit_DealById(ids,"batchaudit",btn_batchaudit.attr("tourl"));
        });

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchApply_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchApplyAudited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                Apply_audit_DealById(sel_row[0].zzsqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
                return;
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].bh){
                    Apply_audit_DealById(sel_row[0].zzsqid,"audit",btn_audit.attr("tourl"));
                }else{
                    $.error("没有包号无法审核！");
                    return;
                }
            }else{
                $.error("请选中一行");
                return;
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#paperReportsApply_formAudit #paperReportsApply_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='Apply_auditing'){
                    var oTable= new paperReportsApply_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new paperReportsApplyAudited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var putInStorage_params=[];
                putInStorage_params.prefix=$('#paperReportsApply_formAudit #urlPrefix').val();
                cancelAudit($('#paperReportsApply_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
                    searchApplyAudited();
                },null,putInStorage_params);
            })
        }
        /**显示隐藏**/
        $("#paperReportsApply_audit_formSearch #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (paperReportsApplyAudit_turnOff) {
                $("#paperReportsApply_audit_formSearch #searchMore").slideDown("low");
                paperReportsApplyAudit_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#paperReportsApply_audit_formSearch #searchMore").slideUp("low");
                paperReportsApplyAudit_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
            //showMore();
        });
    }
    return oInit;
}

var viewConfig = {
    width		: "1000px",
    modalName	:"viewConfigModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchApply_audit_Result(isTurnBack){
    //关闭高级搜索条件
    $("#paperReportsApply_audit_formSearch #searchMore").slideUp("low");
    paperReportsApplyAudit_turnOff=true;
    $("#paperReportsApply_audit_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#paperReportsApply_audit_formSearch #paperReportsApply_audit_list').bootstrapTable('refresh');
    }
}

function searchApplyAudited(isTurnBack){
    if(isTurnBack){
        $('#paperReportsApply_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#paperReportsApply_audit_audited #tb_list').bootstrapTable('refresh');
    }
}

var default_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var bgzt = $("#paperReportsApply_audit_formSearch a[id^='bgzt_id_']");
        $.each(bgzt, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code == '1'){
                addTj('bgzt',code,'paperReportsApply_audit_formSearch');
            }
        });
    }
    return oInit;
}

$(function(){
    var oInit = new default_PageInit();
    oInit.Init();
    var oTable= new paperReportsApply_audit_TableInit();
    oTable.Init();

    var oButtonInit = new paperReportsApply_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#paperReportsApply_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#paperReportsApply_audit_audited .chosen-select').chosen({width: '100%'});
    $("#paperReportsApply_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});