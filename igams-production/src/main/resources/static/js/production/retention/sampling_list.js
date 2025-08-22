var samping_turnOff=true;
var samping_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#samping_formSearch #samping_list').bootstrapTable({
            url: $("#samping_formSearch #urlPrefix").val()+'/retention/retention/pageGetListSampling',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#samping_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wlgl.wlbm",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
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
            uniqueId: "qyid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'sortable': 'true',
                'formatter': function (value, row, index) {
                    var options = $('#samping_formSearch #samping_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'qyid',
                title: '取样id',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '25%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'scph',
                title: '生产批号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jldw',
                title: '单位',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'qyl',
                title: '取样量',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'lrsj',
                title: '取样时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'qyr',
                title: '取样人',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'yt',
                title: '用途',
                width: '15%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'qyxj',
                title: '取样小结',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'lyxj',
                title: '留样小结',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                sampingById(row.qyid,'view',$("#samping_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#samping_formSearch #samping_list").colResizable({
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
            sortLastName: "yxq", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getsampingSearchData(map);
    };
    return oTableInit;
};

function getsampingSearchData(map) {
    var cxtj = $("#samping_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#samping_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["wlbm"] = cxnr
    }else if (cxtj == "1"){
        map["wlmc"] = cxnr
    }else if (cxtj == "2") {
        map["scph"] = cxnr
    }
    else if (cxtj == "3"){
        map["qyr"] = cxnr
    }
    else if (cxtj == "4"){
        map["entire"] = cxnr
    }
    // 取样开始日期
    var qysjstart = jQuery('#samping_formSearch #qysjstart').val();
    map["qysjstart"] = qysjstart;
    // 取样结束日期
    var qysjend = jQuery('#samping_formSearch #qysjend').val();
    map["qysjend"] = qysjend;
    return map;
}

/**
 * 列表刷新
 * @param isTurnBack
 */

function sampingResult(isTurnBack){
    if(isTurnBack){
        $('#samping_formSearch #samping_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#samping_formSearch #samping_list').bootstrapTable('refresh');
    }
}



function sampingById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#samping_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?qyid=" +id;
        $.showDialog(url,'详细信息',viewsampingConfig);
    }else if(action =='summary'){
        var url= tourl + "?qyid=" +id;
        $.showDialog(url,'取样小结',summaryConfig);
    }
}


var samping_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#samping_formSearch #btn_view");
        var btn_query = $("#samping_formSearch #btn_query");
        var btn_del = $("#samping_formSearch #btn_del");
        var btn_summary = $("#samping_formSearch #btn_summary");
        //添加日期控件
        laydate.render({
            elem: '#qysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#qysjend'
            ,theme: '#2381E9'
        });
        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchsampingResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#samping_formSearch #samping_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampingById(sel_row[0].qyid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------取样小结-----------------------------------
        btn_summary.unbind("click").click(function(){
            var sel_row = $('#samping_formSearch #samping_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampingById(sel_row[0].qyid,"summary",btn_summary.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#samping_formSearch #samping_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].qyid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#samping_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchsampingResult();
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
        $("#samping_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(samping_turnOff){
                $("#samping_formSearch #searchMore").slideDown("low");
                samping_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#samping_formSearch #searchMore").slideUp("low");
                samping_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchsampingResult(isTurnBack){
    //关闭高级搜索条件
    $("#samping_formSearch #searchMore").slideUp("low");
    samping_turnOff=true;
    $("#samping_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#samping_formSearch #samping_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#samping_formSearch #samping_list').bootstrapTable('refresh');
    }
}



var sfbc=0;//是否继续保存
var viewsampingConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var summaryConfig={
    width		: "800px",
    modalName	: "summaryModal",
    formName	: "summaryForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#summaryForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#summaryForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"summaryForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchsampingResult();
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
$(function(){

    //0.界面初始化
    // 1.初始化Table
    var oTable = new samping_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new samping_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#samping_formSearch .chosen-select').chosen({width: '100%'});


    $("#samping_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});