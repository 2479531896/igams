var trainAudit_turnOff = true;
var trainAudit_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#trainAudit_audit_formSearch #trainAudit_audit_list").bootstrapTable({
            url: $("#trainAudit_formAudit #urlPrefix").val()+'/train/train/pageGetListTrainAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#trainAudit_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx_sqsj",				//排序字段
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
            uniqueId: "pxid",                     //每一行的唯一标识，一般为主键列
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
                    var options = $('#trainAudit_audit_formSearch #trainAudit_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'pxbt',
                title: '培训标题',
                width: '25%',
                align: 'left',
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '7%',
                align: 'left',
                visible: true
            }, {
                field: 'pxlb',
                title: '类别',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'pxzlb',
                title: '子类别',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'htbj',
                title: '允许回退',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'tzfsbj',
                title: '跳转方式',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'ckdabj',
                title: '查看答案',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'dcckbj',
                title: '当场重考',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'qrry',
                title: '确认人员',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'spxz',
                title: '视频允许下载',
                width: '6%',
                align: 'left',
                formatter:spxzFormat,
                visible: true
            },{
                field: 'gqsj',
                title: '过期时间',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'ssgsmc',
                title: '所属公司',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '15%',
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
                trainAudit_audit_DealById(row.pxid,"view",$("#trainAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#trainAudit_audit_formSearch #trainAudit_audit_list").colResizable({
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
            sortLastName: "pxid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#trainAudit_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#trainAudit_audit_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["qrry"]=cxnr;
        }else if(cxtj=="2"){
            map["pxbt"]=cxnr;
        }else if(cxtj=="3"){
            map["bmmc"]=cxnr;
        }


        var pxlbs = jQuery('#trainAudit_audit_formSearch #pxlb_id_tj').val();
        map["pxlbs"] = pxlbs;
        var pxzlbs = jQuery('#trainAudit_audit_formSearch #pxzlb_id_tj').val();
        map["pxzlbs"] = pxzlbs.replace(/'/g, "");
        var ssgss = jQuery('#trainAudit_audit_formSearch #ssgs_id_tj').val();
        map["ssgss"] = ssgss.replace(/'/g, "");

        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

var trainAuditAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#trainAudit_audit_audited #tb_list").bootstrapTable({
            url:$("#trainAudit_formAudit #urlPrefix").val()+'/train/train/pageGetListTrainAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#trainAudit_audit_audited #toolbar',                //工具按钮用哪个容器
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
                    var options = $('#trainAudit_audit_audited #tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'pxbt',
                title: '培训标题',
                width: '25%',
                align: 'left',
                visible: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '7%',
                align: 'left',
                visible: true
            }, {
                field: 'pxlb',
                title: '类别',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'pxzlb',
                title: '子类别',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'qrry',
                title: '确认人员',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'spxz',
                title: '视频允许下载',
                width: '7%',
                align: 'left',
                formatter:spxzFormat,
                visible: true
            },{
                field: 'gqsj',
                title: '过期时间',
                width: '7%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'ssgsmc',
                title: '所属公司',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
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
                trainAudit_audit_DealById(row.pxid,"view",$("#trainAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#trainAudit_audit_audited #tb_list").colResizable({
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
            sortLastName: "pxid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#trainAudit_audit_audited #cxtj").val();
        var cxnr=$.trim(jQuery('#trainAudit_audit_audited #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["qrry"]=cxnr;
        }else if(cxtj=="2"){
            map["pxbt"]=cxnr;
        }else if(cxtj=="3"){
            map["bmmc"]=cxnr;
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

//视频是否允许下载格式化
function spxzFormat(value,row,index){
    if(row.spxz=='1'){
        return '允许';
    }else if(row.spxz=='0'){
        return '不允许';
    }
}

function trainAudit_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#trainAudit_formAudit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?pxid="+id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_TRAIN',
            url:tourl,
            data:{'ywzd':'pxid'},
            title:"培训审核",
            preSubmitCheck:'preSubmitDevice',
            prefix:$('#trainAudit_formAudit #urlPrefix').val(),
            callback:function(){
                searchTrain_audit_Result(true);//回调
            },
            dialogParam:{width:1525}
        });
    }
}

function preSubmitDevice(){
    return true;
}

var trainAudit_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#trainAudit_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#trainAudit_audit_audited #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#trainAudit_audit_audited #btn_cancelAudit");//取消审核
        var btn_view = $("#trainAudit_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#trainAudit_audit_formSearch #btn_audit");//审核

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchTrain_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchTrainAudited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#trainAudit_audit_formSearch #trainAudit_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                trainAudit_audit_DealById(sel_row[0].pxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#trainAudit_audit_formSearch #trainAudit_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                trainAudit_audit_DealById(sel_row[0].pxid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#trainAudit_formAudit #trainAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='trainAudit_auditing'){
                    var oTable= new trainAudit_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new trainAuditAudited_TableInit();
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
                putInStorage_params.prefix=$('#trainAudit_formAudit #urlPrefix').val();
                cancelAudit($('#trainAudit_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
                    searchTrainAudited();
                },null,putInStorage_params);
            })
        }
        /**显示隐藏**/
        $("#trainAudit_audit_formSearch #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (trainAudit_turnOff) {
                $("#trainAudit_audit_formSearch #searchMore").slideDown("low");
                trainAudit_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#trainAudit_audit_formSearch #searchMore").slideUp("low");
                trainAudit_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
            //showMore();
        });
    };
    return oInit;
};

var viewConfig = {
    width		: "1000px",
    modalName	:"viewConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchTrain_audit_Result(isTurnBack){
    //关闭高级搜索条件
    $("#trainAudit_audit_formSearch #searchMore").slideUp("low");
    trainAudit_turnOff=true;
    $("#trainAudit_audit_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#trainAudit_audit_formSearch #trainAudit_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#trainAudit_audit_formSearch #trainAudit_audit_list').bootstrapTable('refresh');
    }
}

function searchTrainAudited(isTurnBack){
    if(isTurnBack){
        $('#trainAudit_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#trainAudit_audit_audited #tb_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new trainAudit_audit_TableInit();
    oTable.Init();

    var oButtonInit = new trainAudit_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#trainAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#trainAudit_audit_audited .chosen-select').chosen({width: '100%'});
    $("#trainAudit_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});