var risk_board_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#risk_board_formSearch #risk_board_list').bootstrapTable({
            url: '/risk/board/pageGetListBoard',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#risk_board_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"fxsjgl.zt asc,fxsjgl.lrsj",					// 排序字段
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
            uniqueId: "code",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'fxsjid',
                title: 'ID',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'yblxmc',
                title: '样本类型',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'hzxm',
                title: '患者姓名',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                 field: 'sjdwmc',
                 title: '医院名称',
                 width: '10%',
                 align: 'left',
                 visible: true,
                 sortable: true
            }, {
                field: 'fxlbmc',
                title: '风险类别',
                width: '12%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bbclmc',
                title: '标本处理',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'tzrq',
                title: '通知日期',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'qrsj',
                title: '确认时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ztFormat,
                visible: true,
                sortable: true
            },{
                field: 'cz',
                title: '操作',
                width: '8%',
                formatter:czFormat,
                align: 'left',
                visible: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                riskBoardDealById(row.fxsjid,'view',$("#risk_board_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#risk_board_formSearch #risk_board_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
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
            sortLastName: "fxsjid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getRiskBoardSearchData(map);
    };
    return oTableInit;
};

function getRiskBoardSearchData(map) {
    var cxtj = $("#risk_board_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#risk_board_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == '1'){
        map["fxlbmc"] = cxnr
    }else if (cxtj =='2'){
        map["ybbh"]=cxnr
    }else if (cxtj =='3'){
        map["nbbm"]=cxnr
    }else if (cxtj =='4'){
        map["hzxm"]=cxnr
    }else if (cxtj =='5'){
         map["sjdwmc"]=cxnr
     }
    var fxlb=jQuery('#risk_board_formSearch #fxlb_id_tj').val()
    map["fxlbids"]=fxlb;
    var bbcl=jQuery('#risk_board_formSearch #bbcl_id_tj').val()
    map["bbclids"]=bbcl;
    var tzrqstart = jQuery('#risk_board_formSearch #tzrqstart').val();
    map["tzrqstart"] = tzrqstart;
    var tzrqend = jQuery('#risk_board_formSearch #tzrqend').val();
    map["tzrqend"] = tzrqend;
    var qrsjstart = jQuery('#risk_board_formSearch #qrsjstart').val();
    map["qrsjstart"] = qrsjstart;
    var qrsjend = jQuery('#risk_board_formSearch #qrsjend').val();
    map["qrsjend"] = qrsjend;
    return map;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10'&&row.chbj&&row.chbj=='1') {
        return "<span class='btn btn-warning' onclick=\"riskBoardRecallRecheck('" + row.fxsjid +"')\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回提交
function riskBoardRecallRecheck(fxsjid){
    var auditType = $("#risk_board_formSearch #auditType").val()

    var msg = '您确定要撤回申请吗？';
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fxsjid,function(){searchRiskBoardResult();});
        }
    });
}

/**
 * 状态格式化
 * @returns
 */
function ztFormat(value,row,index) {
    var shlxdm = $("#risk_board_formSearch #auditType").val();
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fxsjid + "\",event,\""+shlxdm+"\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fxsjid + "\",event,\""+shlxdm+"\")' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fxsjid + "\",event,\""+shlxdm+"\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
//复检预览
var viewPreViewConfig={
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

function riskBoardDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?fxsjid=" +id;
        $.showDialog(url,'详细信息',viewRiskBoardConfig);
    }else if (action =='mod'){
        var url= tourl + "?fxsjid=" +id;
        $.showDialog(url,'修改',riskBoardConfig);
    }else if (action =='submit'){
        var url= tourl + "?fxsjid=" +id;
        $.showDialog(url,'提交',riskBoardConfig);
    }
}

var riskBoardConfig = {
    width		: "900px",
    modalName	: "riskBoardModal",
    formName	: "riskBoardAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#riskBoardAjaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#riskBoardAjaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"riskBoardAjaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    searchRiskBoardResult();
                                });
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
                var timers=$("#riskBoardModal").attr("dsq")
                        if(timers){
                            var arr=timers.split("_")
                            for(var i=0;i<arr.length;i++){
                                if(arr[i]!=""){
                                   window.clearTimeout(parseInt(arr[i]))
                                }

                            }
                        }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default",
            callback : function() {
             delphoneredis();
             var timers=$("#riskBoardModal").attr("dsq")
             if(timers){
               var arr=timers.split("_")
               for(var i=0;i<arr.length;i++){
                 if(arr[i]!=""){
                    window.clearTimeout(parseInt(arr[i]))
                 }

               }
             }
            }
        }
    }
};
var risk_board_turnOff=true;
function delphoneredis(){
 jQuery.ajaxSetup({async:false});
                    var url= "/common/file/pagedataDelPhoneUpload";
                    jQuery.post(url,{"ywlx":$("#ywlx").val(),"ywid":$("#phoneywid").val(),"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){

                        },1);

                    },'json');
                    jQuery.ajaxSetup({async:true});
}
function searchRiskBoardResult(isTurnBack){
    $("#risk_board_formSearch #searchMore").slideUp("low");
    risk_board_turnOff=true;
    $("#risk_board_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#risk_board_formSearch #risk_board_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#risk_board_formSearch #risk_board_list').bootstrapTable('refresh');
    }
}

var riskBoard_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#risk_board_formSearch #btn_view");
        var btn_query = $("#risk_board_formSearch #btn_query");
        var btn_mod = $("#risk_board_formSearch #btn_mod");
        var btn_submit = $("#risk_board_formSearch #btn_submit");
        var btn_del = $("#risk_board_formSearch #btn_del");

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchRiskBoardResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#risk_board_formSearch #risk_board_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                riskBoardDealById(sel_row[0].fxsjid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------编辑信息-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#risk_board_formSearch #risk_board_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].zt =='10' || sel_row[0].zt =='80'){
                    $.error("该状态不允许操作！");
                    return;
                }
                riskBoardDealById(sel_row[0].fxsjid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------编辑信息-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#risk_board_formSearch #risk_board_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].zt =='10' || sel_row[0].zt =='80'){
                    $.error("该状态不允许操作！");
                    return;
                }
                riskBoardDealById(sel_row[0].fxsjid,"submit",btn_submit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------删除信息-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#risk_board_formSearch #risk_board_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fxsjid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchRiskBoardResult();
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

        /**显示隐藏**/
        $("#risk_board_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(risk_board_turnOff){
                $("#risk_board_formSearch #searchMore").slideDown("low");
                risk_board_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#risk_board_formSearch #searchMore").slideUp("low");
                risk_board_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });

        laydate.render({
            elem: '#risk_board_formSearch #tzrqstart'
            ,type: 'date'
        });

        laydate.render({
            elem: '#risk_board_formSearch #tzrqend'
            ,type: 'date'
        });

        laydate.render({
            elem: '#risk_board_formSearch #qrsjstart'
            ,type: 'date'
        });

        laydate.render({
            elem: '#risk_board_formSearch #qrsjend'
            ,type: 'date'
        });

    };
    return oInit;
};





var viewRiskBoardConfig = {
    width		: "900px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new risk_board_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new riskBoard_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#risk_board_formSearch .chosen-select').chosen({width: '100%'});

});