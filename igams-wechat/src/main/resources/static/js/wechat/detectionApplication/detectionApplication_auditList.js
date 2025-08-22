var detectionApplicationAudit_turnOff = true;
var detectionApplication_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#detectionApplication_audit_formSearch #detectionApplication_audit_list").bootstrapTable({
            url: '/application/application/pageGetListAuditDetectionApplication',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#detectionApplication_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shgc.sqsj",				//排序字段
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
            uniqueId: "sqglid",                     //每一行的唯一标识，一般为主键列
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
                    var options = $('#detectionApplication_audit_formSearch #detectionApplication_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'sqglid',
                title: '申请管理ID',
                titleTooltip:'申请管理ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sqlxmc',
                title: '申请类型',
                titleTooltip:'申请类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'hb',
                title: '合作伙伴',
                titleTooltip:'合作伙伴',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'sqyy',
                title: '申请原因',
                titleTooltip:'申请原因',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'xqsm',
                title: '需求说明',
                titleTooltip:'需求说明',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'lx',
                title: '类型',
                titleTooltip:'类型',
                width: '5%',
                align: 'left',
                formatter:lxFormat,
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '申请人员',
                titleTooltip:'申请人员',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '审核申请时间',
                titleTooltip:'审核申请时间',
                width: '12%',
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
                detectionApplication_audit_DealById(row.sqglid,"view",$("#detectionApplication_audit_formSearch #btn_view").attr("tourl"));
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
        $("#detectionApplication_audit_formSearch #detectionApplication_audit_list").colResizable({
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
            sortLastName: "jcsqgl.sqglid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#detectionApplication_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#detectionApplication_audit_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["hb"]=cxnr
        }else if(cxtj=="1"){
            map["sqyy"]=cxnr
        }else if(cxtj=="2"){
            map["xqsm"]=cxnr
        }

        var sqlxs = jQuery('#detectionApplication_formSearch #sqlx_id_tj').val();
        map["sqlxs"] = sqlxs;
        var lxs=jQuery('#detectionApplication_formSearch #lx_id_tj').val();
        map["lxs"]=lxs;
        var zts=jQuery('#detectionApplication_formSearch #zt_id_tj').val();
        map["zts"]=zts;
        // 开始日期
        var lrsjstart = jQuery('#detectionApplication_formSearch #lrsjstart').val();
        map["lrsjstart"] = lrsjstart;
        // 结束日期
        var lrsjend = jQuery('#detectionApplication_formSearch #lrsjend').val();
        map["lrsjend"] = lrsjend;
        map["dqshzt"] = 'dsh';

        return map;
    };
    return oTableInit;
};

function lxFormat(value,row,index) {
    if (row.lx == '1') {
        return "<span>自定义<span>";
    }else if (row.lx == '0') {
        return "<span>清单<span>";
    }
}

var detectionApplicationAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#detectionApplication_audit_audited #tb_list").bootstrapTable({
            url:'/application/application/pageGetListAuditDetectionApplication',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#detectionApplication_audit_audited #toolbar',                //工具按钮用哪个容器
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
                    var options = $('#detectionApplication_audit_audited #tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'sqglid',
                title: '申请管理ID',
                titleTooltip:'申请管理ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sqlxmc',
                title: '申请类型',
                titleTooltip:'申请类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'hb',
                title: '合作伙伴',
                titleTooltip:'合作伙伴',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'sqyy',
                title: '申请原因',
                titleTooltip:'申请原因',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'xqsm',
                title: '需求说明',
                titleTooltip:'需求说明',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'lx',
                title: '类型',
                titleTooltip:'类型',
                width: '5%',
                align: 'left',
                formatter:lxFormat,
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
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
                width: '8%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                detectionApplication_audit_DealById(row.sqglid,"view",$("#detectionApplication_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#detectionApplication_audit_audited #tb_list").colResizable({
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
            sortLastName: "sqglid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#detectionApplication_audit_audited #cxtj").val();
        var cxnr=$.trim(jQuery('#detectionApplication_audit_audited #cxnr').val());
        if(cxtj=="0"){
            map["hb"]=cxnr
        }else if(cxtj=="1"){
            map["sqyy"]=cxnr
        }else if(cxtj=="2"){
            map["xqsm"]=cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

function detectionApplication_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?sqglid="+id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_DETECTIONAPPLICATION',
            url: tourl,
            data:{'ywzd':'sqglid'},
            title:"检出申请审核",
            preSubmitCheck:'preSubmitDetectionApplication',
            callback:function(){
                detectionApplication_audit_Result(true);//回调
            },
            dialogParam:{width:1000}
        });
    }
}

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
                        detectionApplication_audit_Result();
                    });
                }else if(data.status == "0"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.alert(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        detectionApplication_audit_Result();
                    });
                }else{
                    if($("#cardiv")) $("#cardiv").remove();
                    $.error(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        detectionApplication_audit_Result();
                    });
                }
            }
        }
    });
}

function preSubmitDetectionApplication(){
    // if(!$("#viewdetectionApplicationForm").valid()){
    //     $.alert("请填写完整信息");
    //     return false;
    // }
    return true;
}

var detectionApplication_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#detectionApplication_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#detectionApplication_audit_audited #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#detectionApplication_audit_audited #btn_cancelAudit");//取消审核
        var btn_view = $("#detectionApplication_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#detectionApplication_audit_formSearch #btn_audit");//审核
        

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                detectionApplication_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchDetectionApplicationAudited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#detectionApplication_audit_formSearch #detectionApplication_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                detectionApplication_audit_DealById(sel_row[0].sqglid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
                return;
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#detectionApplication_audit_formSearch #detectionApplication_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                detectionApplication_audit_DealById(sel_row[0].sqglid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
                return;
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#detectionApplication_formAudit #detectionApplication_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='Apply_auditing'){
                    var oTable= new detectionApplication_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new detectionApplicationAudited_TableInit();
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
                putInStorage_params.prefix=$('#detectionApplication_formAudit #urlPrefix').val();
                cancelAudit($('#detectionApplication_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
                    searchDetectionApplicationAudited();
                },null,putInStorage_params);
            })
        }
        /**显示隐藏**/
        $("#detectionApplication_audit_formSearch #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (detectionApplicationAudit_turnOff) {
                $("#detectionApplication_audit_formSearch #searchMore").slideDown("low");
                detectionApplicationAudit_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#detectionApplication_audit_formSearch #searchMore").slideUp("low");
                detectionApplicationAudit_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
            //showMore();
        });
    }
    return oInit;
}

var viewConfig = {
    width		: "700px",
    modalName	:"viewConfigModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function detectionApplication_audit_Result(isTurnBack){
    //关闭高级搜索条件
    $("#detectionApplication_audit_formSearch #searchMore").slideUp("low");
    detectionApplicationAudit_turnOff=true;
    $("#detectionApplication_audit_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#detectionApplication_audit_formSearch #detectionApplication_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectionApplication_audit_formSearch #detectionApplication_audit_list').bootstrapTable('refresh');
    }
}

function searchDetectionApplicationAudited(isTurnBack){
    if(isTurnBack){
        $('#detectionApplication_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectionApplication_audit_audited #tb_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new detectionApplication_audit_TableInit();
    oTable.Init();

    var oButtonInit = new detectionApplication_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#detectionApplication_audit_formSearch .chosen-select').chosen({width: '100%'});
    $("#detectionApplication_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});