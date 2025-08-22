var feedback_turnOff=true;
var feedback_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#feedback_formSearch #feedback_list').bootstrapTable({
            url: $("#feedback_formSearch #urlPrefix").val()+'/saleFeedback/saleFeedback/pageGetListSaleFeedback',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#feedback_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"lrsj",					// 排序字段
            sortOrder: "DESC",                   // 排序方式
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
            uniqueId: "shfkid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '2%',
                'sortable': 'true',
                'formatter': function (value, row, index) {
                    var options = $('#feedback_formSearch #feedback_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'shfkid',
                title: '售后付款id',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'khid',
                title: '客户id',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'lrsj',
                title: '登记时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'yqmc',
                title: '标题',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'lxmc',
                title: '类型',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'xlh',
                title: '序列号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            },
             {
                field: 'khmc',
                title: '客户',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                    field: 'lxr',
                    title: '客户联系人',
                    width: '8%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'wtms',
                    title: '问题描述',
                    width: '20%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                field: 'clyj',
                title: '处理意见',
                width: '15%',
                align: 'left',
                visible: true,
                sortable: true
            },
                {
                    field: 'djry',
                    title: '登记人',
                    width: '5%',
                    align: 'left',
                    visible: true,
                    sortable: true
                },

                {
                    field: 'fzrmc',
                    title: '负责人',
                    width: '5%',
                    align: 'left',
                    visible: true,
                    sortable: true
                },{
                field: 'jd',
                title: '进度',
                width: '4%',
                align: 'left',
                visible: true,
                formatter:jdformat,
                sortable: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                feedbackById(row.shfkid,'processingrecords',$("#feedback_formSearch #btn_processingrecords").attr("tourl"));
            },
        });
        $("#feedback_formSearch #feedback_list").colResizable({
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
            sortLastName: "shfkdj.lrsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getfeedbackSearchData(map);
    };
    return oTableInit;
};

function getfeedbackSearchData(map) {
    var cxtj = $("#feedback_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#feedback_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["khmc"] = cxnr
    } else if (cxtj == "1") {
        map["wtms"] = cxnr
    } else if (cxtj == 2){
        map["clyj"] = cxnr
    }else if (cxtj ==3){
        map["djry"]=cxnr
    }
    else if (cxtj ==4){
        map["fzrmc"]=cxnr
    }  else if (cxtj ==5){
        map["entire"]=cxnr
    } else if (cxtj ==6){
        map["lxr"]=cxnr
    } else if (cxtj ==7){
        map["yqmc"]=cxnr
    } else if (cxtj ==8){
        map["xlh"]=cxnr
    }
    // 创建开始日期
    var djsjstart = jQuery('#feedback_formSearch #djsjstart').val();
    map["djsjstart"] = djsjstart;

    // 创建结束日期
    var djsjend = jQuery('#feedback_formSearch #djsjend').val();
    map["djsjend"] = djsjend;
    //进度
    var jds=jQuery('#feedback_formSearch #jd_id_tj').val();
    map["jds"] = jds.replace(/'/g, "");
    //类型
    var lxs=jQuery('#feedback_formSearch #lx_id_tj').val();
    map["lxs"] = lxs.replace(/'/g, "");
    return map;
}
function jdformat(value,row,index) {
    var html="";
    if(row.jd=='0'){
        html="<span style='color:red;'>登记</span>";
    }else if (row.jd=='1'){
        html="<span class='text-warning'style='color:#f59610'>处理中</span>";
    }else if (row.jd=='2')
        html="<span style='color:green;'>结束</span>";
    return html;

}


function feedbackResult(isTurnBack){
    if(isTurnBack){
        $('#feedback_formSearch #feedback_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#feedback_formSearch #feedback_list').bootstrapTable('refresh');
    }
}



function feedbackById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#feedback_formSearch #urlPrefix").val()+tourl;
    if(action =='processingrecords'){
        var url= tourl + "?shfkid=" +id;
        $.showDialog(url,'处理记录信息',processingrecordsfeedbackConfig);
    }else if(action =='comment'){
        var url= tourl + "?shfkid=" +id;
        $.showDialog(url,'售后评论',commentfeedbackConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增',addFeedBackConfig);
    }else if(action =='mod'){
        var url= tourl + "?shfkid=" +id;
        $.showDialog(url,'修改',addFeedBackConfig);
    }else if(action =='sendorders'){
        var url= tourl + "?shfkid=" +id;
        $.showDialog(url,'派单',sendOrdersFeedBackConfig);
    }
}

var feedback_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_processingrecords = $("#feedback_formSearch #btn_processingrecords");
        var btn_query = $("#feedback_formSearch #btn_query");
        var btn_comment = $("#feedback_formSearch #btn_comment");
        var btn_add = $("#feedback_formSearch #btn_add");
        var btn_mod = $("#feedback_formSearch #btn_mod");
        var btn_cancelsendorders = $("#feedback_formSearch #btn_cancelsendorders");
        var btn_sendorders = $("#feedback_formSearch #btn_sendorders");
        var btn_del = $("#feedback_formSearch #btn_del");
        //添加日期控件
        laydate.render({
            elem: '#djsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#djsjend'
            ,theme: '#2381E9'
        });

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchfeedbackResult(true);
            });
        }
        /*-----------------------新增------------------------------------*/
        btn_add.unbind("click").click(function(){
            feedbackById(null,"add",btn_add.attr("tourl"));
        });
        //---------------------------修改-----------------------------------
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#feedback_formSearch #feedback_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jd == '0'){
                    feedbackById(sel_row[0].shfkid,"mod",btn_mod.attr("tourl"));
                }else{
                    $.error("处理中或已结束不允许修改");
                }

            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------查看列表-----------------------------------
        btn_processingrecords.unbind("click").click(function(){
            var sel_row = $('#feedback_formSearch #feedback_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                feedbackById(sel_row[0].shfkid,"processingrecords",btn_processingrecords.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------售后评论-----------------------------------
        btn_comment.unbind("click").click(function(){
            var sel_row = $('#feedback_formSearch #feedback_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jd!=0){
                    feedbackById(sel_row[0].shfkid,"comment",btn_comment.attr("tourl"));
                }else {
                    $.error("该反馈未派单!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------派单-----------------------------------
        btn_sendorders.unbind("click").click(function(){
            var sel_row = $('#feedback_formSearch #feedback_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jd!=0){
                    $.error("该反馈已派单!");
                }else {
                    feedbackById(sel_row[0].shfkid,"sendorders",btn_sendorders.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------取消派单-----------------------------*/
        btn_cancelsendorders.unbind("click").click(function(){
            var sel_row = $('#feedback_formSearch #feedback_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jd==1){
                    $.confirm('您确定要取消派单吗？',function(result){
                        if(result){
                            jQuery.ajaxSetup({async:false});
                            var url=  $('#feedback_formSearch #urlPrefix').val() + btn_cancelsendorders.attr("tourl");
                            jQuery.post(url,{shfkid:sel_row[0].shfkid,"access_token":$("#ac_tk").val()},function(responseText){
                                setTimeout(function(){
                                    if(responseText["status"] == 'success'){
                                        $.success(responseText["message"],function() {
                                            searchfeedbackResult(true);
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
                }else {
                    $.error("该反馈未派单或已结束！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#feedback_formSearch #feedback_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].shfkid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#feedback_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchfeedbackResult(true);
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
        $("#feedback_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(feedback_turnOff){
                $("#feedback_formSearch #searchMore").slideDown("low");
                feedback_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#feedback_formSearch #searchMore").slideUp("low");
                feedback_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchfeedbackResult(isTurnBack){
    //关闭高级搜索条件
    $("#feedback_formSearch #searchMore").slideUp("low");
    feedback_turnOff=true;
    $("#feedback_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#feedback_formSearch #feedback_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#feedback_formSearch #feedback_list').bootstrapTable('refresh');
    }
}
var sendOrdersFeedBackConfig = {
    width		: "800px",
    modalName	: "sendOrdersFeedBackModal",
    formName	: "sendOrdersFeedBackForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#sendOrdersFeedBackForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#sendOrdersFeedBackForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"sendOrdersFeedBackForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchfeedbackResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
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
var addFeedBackConfig = {
    width		: "800px",
    modalName	: "addFeedBackModal",
    formName	: "editFeedBackForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editFeedBackForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editFeedBackForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editFeedBackForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchfeedbackResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
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

var processingrecordsfeedbackConfig = {
    width		: "1500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var commentfeedbackConfig = {
    width : "1000px",
    modalName	: "commentfeedbackModal",
    formName	: "commentSaleFeedbackForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var preJson = [];
                var selLldhs = $("#commentSaleFeedbackForm #lldhs").tagsinput('items');
                // if (selLldhs.length==0){
                //     $.alert("未选择领料信息！");
                //     return false;
                // }
                if(!$("#commentSaleFeedbackForm").valid()){
                    return false;
                }
                var json = [];
                for(var i = 0; i < selLldhs.length; i++){
                    var value = selLldhs[i].value;
                    json.push(value);
                }
                $("#commentSaleFeedbackForm #llid").val(JSON.stringify(json));
                $("#commentSaleFeedbackForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"commentSaleFeedbackForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchfeedbackResult(true);
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
    var oTable = new feedback_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new feedback_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#feedback_formSearch .chosen-select').chosen({width: '100%'});


    $("#feedback_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});