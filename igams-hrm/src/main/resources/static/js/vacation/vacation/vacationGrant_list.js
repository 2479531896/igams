var vacationGrant_turnOff=true;

var vacationGrant_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#vacationGrant_formSearch #tab_list').bootstrapTable({
            url:$("#vacationGrant_formSearch #urlPrefix").val()+'/vacation/vacation/pageGetListVacationGrant',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#vacationGrant_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jqffsz.lrsj",			// 排序字段
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
            uniqueId: "ffszid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                title: '序号',
                width: '4%',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'left',
                visible:true
            }, {
                field: 'ffszid',
                title: '方法设置ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'yhm',
                title: '用户名',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'zsxm',
                title: '姓名',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jqlxmc',
                title: '假期类型',
                titleTooltip:'假期类型',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sc',
                title: '时长',
                titleTooltip:'时长',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'dw',
                title: '单位',
                titleTooltip:'单位',
                width: '3%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter:dwFormat
            },{
                field: 'kssj',
                title: '开始时间',
                titleTooltip:'开始时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jssj',
                title: '结束时间',
                titleTooltip:'结束时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jzlx',
                title: '截止类型',
                titleTooltip:'截止类型',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter:jzlxFormat
            },{
                field: 'jzrq',
                title: '截止日期',
                titleTooltip:'截止日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lrrymc',
                title: '录入人员',
                titleTooltip:'录入人员',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'lrsj',
                title: '录入时间',
                titleTooltip:'录入时间',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'scbj',
                title: '状态',
                titleTooltip:'状态',
                width: '3%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter:ztFormat
            }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                vacationGrantDealById(row.ffszid, 'view',$("#vacationGrant_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#vacationGrant_formSearch #tb_list").colResizable({
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return  vacationGrantSearchData(map);
    };
    return oTableInit;
};
//状态格式化
function ztFormat(value, row, index) {
    if(row.scbj=='0'){
        return "正常";
    }else if (row.scbj=='2'){
        return "停用";
    }
}
function vacationGrantSearchData(map){
    var cxtj=$("#vacationGrant_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#vacationGrant_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["yhm"]=cxnr;
    }else if(cxtj=="2"){
        map["zsxm"]=cxnr;
    }else if (cxtj == "3") {
        map["lrrymc"] = cxnr;
    }
    // 创建开始日期
    var kssjstart = jQuery('#vacationGrant_formSearch #kssjstart').val();
    map["kssjstart"] = kssjstart;

    // 创建结束日期
    var kssjend = jQuery('#vacationGrant_formSearch #kssjend').val();
    map["kssjend"] = kssjend;
    // 结束开始日期
    var jssjstart = jQuery('#vacationGrant_formSearch #jssjstart').val();
    map["jssjstart"] = jssjstart;

    // 结束结束日期
    var jssjend = jQuery('#vacationGrant_formSearch #jssjend').val();
    map["jssjend"] = jssjend;
    // 删除标记
    var scbjs = jQuery('#vacationGrant_formSearch #scbj_id_tj').val();
    map["scbjs"] = scbjs;

    //假期类型
    var jqlxs=jQuery('#vacationGrant_formSearch #jqlx_id_tj').val();
    map["jqlxs"] = jqlxs.replace(/'/g, "");
    return map;
}
//截止类型
function jzlxFormat(value, row, index) {
    if (row.jzlx=='0'){
        return "本年";
    }else if (row.jzlx=='1'){
        return "次年";
    }

}
function dwFormat(value, row, index) {
    if (row.dw=='0'){
        return "小时";
    }else if (row.dw=='1'){
        return "天";
    }

}
//按钮动作函数
function vacationGrantDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl=$("#vacationGrant_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?ffszid=" +id;
        $.showDialog(url,'查看任务',viewVacationGrantConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增',addVacationGrantConfig);
    }else if(action =='setadmin'){
        var url=tourl;
        $.showDialog(url,'设置管理员',setadminVacationGrantConfig);
    }
}
/**
 * 新增
 */
var addVacationGrantConfig = {
    width		: "1000px",
    modalName	: "vacationGrantModal",
    formName	: "vacationGrant_Form",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
//					$("#addVersionInfoForm #bbid").val($("#addVersionInfoForm #bbid").val());
                if(!$("#vacationGrant_Form").valid()){
                    $.alert("请填写完整信息!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#vacationGrant_Form input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"vacationGrant_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchvacationGrantResult(true);
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
/**
 * 设置管理员
 */
var setadminVacationGrantConfig = {
    width		: "600px",
    modalName	: "vacationAdminModal",
    formName	: "vacationAdmin_Form",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#vacationAdmin_Form").valid()){
                    $.alert("请填写完整信息!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#vacationAdmin_Form input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"vacationAdmin_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchvacationGrantResult(true);
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

var viewVacationGrantConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var vacationGrant_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#vacationGrant_formSearch #btn_view");
        var btn_query = $("#vacationGrant_formSearch #btn_query");
        var btn_del = $("#vacationGrant_formSearch #btn_del");
        var btn_add = $("#vacationGrant_formSearch #btn_add");
        var btn_setadmin = $("#vacationGrant_formSearch #btn_setadmin");
        //添加日期控件
        laydate.render({
            elem: '#vacationGrant_formSearch #kssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#vacationGrant_formSearch #kssjend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#jssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jssjend'
            ,theme: '#2381E9'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchvacationGrantResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#vacationGrant_formSearch #tab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                vacationGrantDealById(sel_row[0].ffszid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 新增假期 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            vacationGrantDealById(null, "add", btn_add.attr("tourl"));
        });
        /* --------------------------- 设置管理员 -----------------------------------*/
        btn_setadmin.unbind("click").click(function(){
            vacationGrantDealById(null, "setadmin", btn_setadmin.attr("tourl"));
        });
        /*----------------------------删除假期设置信息--------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#vacationGrant_formSearch #tab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].ffszid;
                }
                ids=ids.substr(1);
                var message="";
                $.ajax({
                    url: $("#vacationGrant_formSearch #urlPrefix").val() + "/vacation/vacation/pagedataDelVacationGrant",
                    type: "post",
                    dataType: 'json',
                    data: { "ffszids": ids, "access_token": $("#ac_tk").val() },
                    success: function(data) {
                        message=data.message;
                        $.confirm(message,function(result){
                            if(result){
                                jQuery.ajaxSetup({async:false});
                                var url= btn_del.attr("tourl");
                                jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                                    setTimeout(function(){
                                        if(responseText["status"] == 'success'){
                                            $.success(responseText["message"],function() {
                                                searchvacationGrantResult();
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


            }
        })
        /**显示隐藏**/
        $("#vacationGrant_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(vacationGrant_turnOff){
                $("#vacationGrant_formSearch #searchMore").slideDown("low");
                vacationGrant_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#vacationGrant_formSearch #searchMore").slideUp("low");
                vacationGrant_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};




function searchvacationGrantResult(isTurnBack){
    //关闭高级搜索条件
    $("#vacationGrant_formSearch #searchMore").slideUp("low");
    vacationGrant_turnOff=true;
    $("#vacationGrant_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#vacationGrant_formSearch #tab_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#vacationGrant_formSearch #tab_list').bootstrapTable('refresh');
    }
}

var vacationGrant_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var scbj = $("#vacationGrant_formSearch a[id^='scbj_id_']");
        $.each(scbj, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code === '0'){
                addTj('scbj',code,'vacationGrant_formSearch');
            }
        });
    }
    return oInit;
}

$(function(){
//	//0.界面初始化
    var oInit = new vacationGrant_PageInit();
    oInit.Init();

    // 1.初始化Table
    var oTable = new vacationGrant_TableInit();
    oTable.Init();
    var ButtonInit=vacationGrant_ButtonInit();
    ButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#vacationGrant_formSearch .chosen-select').chosen({width: '100%'});

    // 初始绑定显示更多的事件
    $("#vacationGrant_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});