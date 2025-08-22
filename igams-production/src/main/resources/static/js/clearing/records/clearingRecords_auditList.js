var clearingRecordsAudit_turnOff = true;
var clearingRecordsAudit_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list").bootstrapTable({
            url: $("#clearingRecordsAudit_formAudit #urlPrefix").val()+'/clearing/records/pageGetListAuditClearingRecords',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#clearingRecordsAudit_audit_formSearch #toolbar',                //工具按钮用哪个容器
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
            uniqueId: "qcjlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                'field': '',
                'title': '序',
                'align': 'center',
                'width': '2%',
                'formatter': function (value, row, index) {
                    var options = $('#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
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
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '10%',
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
                clearingRecordsAudit_audit_DealById(row.qcjlid,"view",$("#clearingRecordsAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list").colResizable({
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
            sortLastName: "qcjlid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#clearingRecordsAudit_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#clearingRecordsAudit_audit_formSearch #cxnr').val());
        if(cxtj=="0"){//全部
            map["entire"]=cxnr
        }else if(cxtj=="1"){
            map["jlbh"]=cxnr
        }else if(cxtj=="2"){
            map["fkmc"]=cxnr
        }else if(cxtj=="3"){
            map["ssgx"]=cxnr
        }else if(cxtj=="4"){
            map["wlbm"]=cxnr
        }else if(cxtj=="5"){
            map["wlmc"]=cxnr
        }else if(cxtj=="6"){
            map["ph"]=cxnr
        }else if(cxtj=="7"){
            map["czrmc"]=cxnr
        }else if(cxtj=="8"){
            map["jcrmc"]=cxnr
        }

        var jczp = jQuery('#clearingRecordsAudit_audit_formSearch #jczp_id_tj').val();
        map["jczp"] = jczp;

        var qcrqstart = jQuery('#clearingRecordsAudit_audit_formSearch #qcrqstart').val();
        map["qcrqstart"] = qcrqstart;

        var qcrqend = jQuery('#clearingRecordsAudit_audit_formSearch #qcrqend').val();
        map["qcrqend"] = qcrqend;
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

var clearingRecordsAuditAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#clearingRecordsAudit_audited_formSearch #tb_list").bootstrapTable({
            url:$("#clearingRecordsAudit_formAudit #urlPrefix").val()+'/clearing/records/pageGetListAuditClearingRecords',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#clearingRecordsAudit_audited_formSearch #toolbar',                //工具按钮用哪个容器
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
                width: '2%'
            },{
                'field': '',
                'title': '序',
                'align': 'center',
                'width': '2%',
                'formatter': function (value, row, index) {
                    var options = $('#clearingRecordsAudit_audited_formSearch #tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
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
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jlbh',
                title: '记录编号',
                titleTooltip:'记录编号',
                width: '8%',
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
                width: '8%',
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
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '14%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '6%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            },
            onDblClickRow: function (row, $element) {
                clearingRecordsAudit_audit_DealById(row.qcjlid,"view",$("#clearingRecordsAudit_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#clearingRecordsAudit_audited_formSearch #tb_list").colResizable({
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
            sortLastName: "qcjlid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#clearingRecordsAudit_audited_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#clearingRecordsAudit_audited_formSearch #cxnr').val());
        if(cxtj=="0"){//全部
            map["entire"]=cxnr
        }else if(cxtj=="1"){
            map["jlbh"]=cxnr
        }else if(cxtj=="2"){
            map["fkmc"]=cxnr
        }else if(cxtj=="3"){
            map["ssgx"]=cxnr
        }else if(cxtj=="4"){
            map["wlbm"]=cxnr
        }else if(cxtj=="5"){
            map["wlmc"]=cxnr
        }else if(cxtj=="6"){
            map["ph"]=cxnr
        }else if(cxtj=="7"){
            map["czrmc"]=cxnr
        }else if(cxtj=="8"){
            map["jcrmc"]=cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

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
    var url=$("#clearingRecordsAudit_formAudit #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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
function clearingRecordsAudit_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#clearingRecordsAudit_formAudit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?qcjlid="+id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_CLEARING_RECORDS',
            url:tourl,
            data:{'ywzd':'qcjlid'},
            title:"清场记录审核",
            preSubmitCheck:'preSubmitCheck',
            prefix:$('#clearingRecordsAudit_formAudit #urlPrefix').val(),
            callback:function(){
                searchClearingRecordsAuditResult(true);//回调
            },
            dialogParam:{width:1200}
        });
    }
}

function preSubmitCheck(){
    var json=[];
    var rows=$('#clearClearingRecordsForm #tb_list').bootstrapTable('getData');
    for(var i=0;i<rows.length;i++){
        var sz={"xmid":'',"jg":''};
        sz.xmid=rows[i].xmid;
        sz.jg=$('input:radio[name="jg'+i+'"]:checked').val();
        json.push(sz);
    }
    $("#clearClearingRecordsForm #qcxm_json").val(JSON.stringify(json));
    return true;
}

var clearingRecordsAudit_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#clearingRecordsAudit_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#clearingRecordsAudit_audited_formSearch #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#clearingRecordsAudit_audited_formSearch #btn_cancelAudit");//取消审核
        var btn_view = $("#clearingRecordsAudit_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#clearingRecordsAudit_audit_formSearch #btn_audit");//审核
        //添加日期控件
        laydate.render({
            elem: '#clearingRecordsAudit_audit_formSearch #qcrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#clearingRecordsAudit_audit_formSearch #qcrqend'
            ,theme: '#2381E9'
        });

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchClearingRecordsAuditResult(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchClearingRecordsAuditedResult(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                clearingRecordsAudit_audit_DealById(sel_row[0].qcjlid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                clearingRecordsAudit_audit_DealById(sel_row[0].qcjlid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#clearingRecordsAudit_formAudit #clearingRecordsAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='clearingRecordsAudit_auditing'){
                    var oTable= new clearingRecordsAudit_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new clearingRecordsAuditAudited_TableInit();
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
                putInStorage_params.prefix=$('#clearingRecordsAudit_formAudit #urlPrefix').val();
                cancelAudit($('#clearingRecordsAudit_audited_formSearch #tb_list').bootstrapTable('getSelections'),function(){
                    searchClearingRecordsAuditedResult();
                },null,putInStorage_params);
            })
        }
        /**显示隐藏**/
        $("#clearingRecordsAudit_audit_formSearch #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (clearingRecordsAudit_turnOff) {
                $("#clearingRecordsAudit_audit_formSearch #searchMore").slideDown("low");
                clearingRecordsAudit_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#clearingRecordsAudit_audit_formSearch #searchMore").slideUp("low");
                clearingRecordsAudit_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
            //showMore();
        });
    };
    return oInit;
};

var viewConfig = {
    width		: "800px",
    modalName	:"viewConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchClearingRecordsAuditResult(isTurnBack){
    //关闭高级搜索条件
    $("#clearingRecordsAudit_audit_formSearch #searchMore").slideUp("low");
    clearingRecordsAudit_turnOff=true;
    $("#clearingRecordsAudit_audit_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#clearingRecordsAudit_audit_formSearch #clearingRecordsAudit_audit_list').bootstrapTable('refresh');
    }
}

function searchClearingRecordsAuditedResult(isTurnBack){
    if(isTurnBack){
        $('#clearingRecordsAudit_audited_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#clearingRecordsAudit_audited_formSearch #tb_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new clearingRecordsAudit_audit_TableInit();
    oTable.Init();

    var oButtonInit = new clearingRecordsAudit_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#clearingRecordsAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#clearingRecordsAudit_audited_formSearch .chosen-select').chosen({width: '100%'});
    $("#clearingRecordsAudit_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});