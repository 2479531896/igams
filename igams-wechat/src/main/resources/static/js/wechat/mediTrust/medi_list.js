var medi_turnOff=true;
var medi_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#medi_formSearch #medi_list').bootstrapTable({
            url: '/medi/medi/listMediTrust',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#medi_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"idName",					// 排序字段
            sortOrder: "asc",                   // 排序方式
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
            uniqueId: "mxid",                     // 每一行的唯一标识，一般为主键列
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
                field: 'mxid',
                title: '镁信ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'projectCode',
                title: '项目代码',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'orgName',
                title: '企业名称',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'orgId',
                title: '企业编号',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'comboCode',
                title: '渠道编码',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'mxOrderNo',
                title: '镁信订单号',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'phoneNo',
                title: '手机号',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'amount',
                title: '单价(分)',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'quantity',
                title: '购买数量',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'totalAmount',
                title: '总价(分)',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'equityName',
                title: '商品名称',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'payTime',
                title: '支付时间',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'orderNo',
                title: '服务商订单号',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'idName',
                title: '用户名',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'idNum',
                title: '证件号',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'idType',
                title: '证件类型',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'orderTime',
                title: '预约时间',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'status',
                title: '订单状态',
                width: '5%',
                align: 'left',
                formatter:statusformat,
                sortable:true,
                visible: true
            }, {
                field: 'hasReport',
                title: '后续报告',
                width: '8%',
                align: 'left',
                formatter:reportformat,
                sortable:true,
                visible: true
            }, {
                field: 'reportUrl',
                title: '报告URL',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: false
            }, {
                field: 'statusTime',
                title: '出报告时间',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                mediById(row.mxid,'view',$("#medi_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#medi_formSearch #medi_list").colResizable({
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
            sortLastName: "mxid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return mediSearchData(map);
    };
    return oTableInit;
};
function mediSearchData(map){
    var cxtj=$("#medi_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#medi_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["orgName"]=cxnr;
    }else if(cxtj=="1"){
        map["orgId"]=cxnr;
    }else if(cxtj=="2"){
        map["mxOrderNo"]=cxnr;
    }else if(cxtj=="3"){
        map["orderNo"]=cxnr;
    }else if(cxtj=="4"){
        map["idName"]=cxnr;
    }


    var yysjstart = jQuery('#medi_formSearch #yysjstart').val();
    map["yysjstart"] = yysjstart;
    var yysjend = jQuery('#medi_formSearch #yysjend').val();
    map["yysjend"] = yysjend;

    var cbgsjstart = jQuery('#medi_formSearch #cbgsjstart').val();
    map["cbgsjstart"] = cbgsjstart;
    var cbgsjend = jQuery('#medi_formSearch #cbgsjend').val();
    map["cbgsjend"] = cbgsjend;

    var status_id_tj = jQuery('#medi_formSearch #status_id_tj').val();
    map["statuses"] = status_id_tj;
    var idType_id_tj=jQuery('#medi_formSearch #idType_id_tj').val()
    map["types"]=idType_id_tj;

    return map;
}


function statusformat(value,row,index){
    if(row.status==3){
        var status="<span style='color:green;'>"+"未预约"+"</span>";
    }else if(row.status==0){
        var status="<span style='color:red;'>"+"已预约"+"</span>";
    }else if(row.status==1){
        var status="<span style='color:blue;'>"+"已使用"+"</span>";
    }else if(row.status==2){
        var status="<span style='color:purple;'>"+"已出报告"+"</span>";
    }
    return status;
}

function reportformat(value,row,index){
    if(row.hasReport=='true'){
        var status="<span style='color:green;'>"+"流程结束"+"</span>";
    }else if(row.hasReport=='false'){
        var status="<span style='color:red;'>"+"流程未结束"+"</span>";
    }
    return status;
}


function mediById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?mxid=" +id;
        $.showDialog(url,'查看详情',viewMediTrustConfig);
    }else if(action =='notice'){
        var url= tourl + "?mxid=" +id;
        $.showDialog(url,'分发',noticeMediTrustConfig);
    }else if(action =='relationinspect'){
        var url= tourl + "?mxid=" +id;
        $.showDialog(url,'关联特检',relationMediTrustConfig);
    }
}

var viewMediTrustConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var noticeMediTrustConfig = {
    width		: "600px",
    modalName	: "noticeMediTrustModal",
    formName	: "medi_ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#medi_ajaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#medi_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"medi_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                mediResult();
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

var relationMediTrustConfig = {
    width : "800px",
    height : "500px",
    modalName	: "relationMediTrustModal",
    formName	: "sjxxListForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};



var medi_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#medi_formSearch #btn_view");
        var btn_query = $("#medi_formSearch #btn_query");
        var btn_notice = $("#medi_formSearch #btn_notice");
        var btn_relationinspect= $("#medi_formSearch #btn_relationinspect");
        //添加日期控件
        laydate.render({
            elem: '#medi_formSearch #bgrqstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#medi_formSearch #bgrqend'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#medi_formSearch #sqrqstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#medi_formSearch #sqrqend'
            ,type: 'date'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                mediResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#medi_formSearch #medi_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                mediById(sel_row[0].mxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------查看列表-----------------------------------*/
        btn_notice.unbind("click").click(function(){
            var sel_row = $('#medi_formSearch #medi_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                mediById(sel_row[0].mxid,"notice",btn_notice.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------关联特检-----------------------------------*/
        btn_relationinspect.unbind("click").click(function(){
            var sel_row = $('#medi_formSearch #medi_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].sjid!=null&&sel_row[0].sjid!=""){
                    $.error("此条信息已与特检关联");
                    return;
                }
                mediById(sel_row[0].mxid,"relationinspect",btn_relationinspect.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /**显示隐藏**/
        $("#medi_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(medi_turnOff){
                $("#medi_formSearch #searchMore").slideDown("low");
                medi_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#medi_formSearch #searchMore").slideUp("low");
                medi_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};



function mediResult(isTurnBack){
    //关闭高级搜索条件
    $("#medi_formSearch #searchMore").slideUp("low");
    medi_turnOff=true;
    $("#medi_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#medi_formSearch #medi_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#medi_formSearch #medi_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new medi_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new medi_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#medi_formSearch .chosen-select').chosen({width: '100%'});

    $("#medi_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});