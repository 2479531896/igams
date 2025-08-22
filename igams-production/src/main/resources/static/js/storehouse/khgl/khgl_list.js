var Khgl_turnOff = true;

var Khgl_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#khgllist_formSearch #khgl_list').bootstrapTable({
            url: $("#khgllist_formSearch #urlPrefix").val() + '/storehouse/khgl/pageGetListkhgl',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#khgllist_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "khid",					// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       // 初始化加载第一页，默认第一页
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
            uniqueId: "khid",                     // 每一行的唯一标识，一般为主键列
            showToggle: true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            }, {
                title: '序号',
                formatter: function (value, row, index) {
                    return index + 1;
                },
                titleTooltip: '序号',
                width: '2%',
                align: 'left',
                visible: true
            }, {
                field: 'khid',
                title: '客户id',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'khdm',
                title: '客户代码',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khmc',
                title: '客户名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'khjc',
                title: '客户简称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'fzrq',
                title: '发展日期',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sfmc',
                title: '省份',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bizmc',
                title: '币种',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khgllxmc',
                title: '客户管理类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khlbmc',
                title: '客户类别',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lrrymc',
                title: '录入人员',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khlbmc',
                title: '客户类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lxr',
                title: '联系人',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lxfs',
                title: '联系方式',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lxdz',
                title: '联系地址',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yb',
                title: '邮编',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sfsc',
                title: '生产状态',
                width: '5%',
                align: 'left',
                formatter:scztFormat,
                sortable: true,
                visible: true
            }, {
                 field: 'zdkh',
                 title: '终端客户',
                 width: '25%',
                 align: 'left',
                 sortable: true,
                 visible: false
             }, {
                  field: 'zdlxfs',
                  title: '终端联系方式',
                  width: '25%',
                  align: 'left',
                  sortable: true,
                  visible: false
              }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                KhglDealById(row.khid, 'view', $("#khgllist_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#khgllist_formSearch #khgl_list").colResizable({
            liveDrag: true,
            gripInnerHtml: "<div class='grip'></div>",
            draggingClass: "dragging",
            resizeMode: 'fit',
            postbackSafe: true,
            partialRefresh: true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams = function (params) {
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的2333
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token: $("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "lrsj", // 防止同名排位用
            sortLastOrder: "desc"// 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return khglSearchData(map);
    };
    return oTableInit;
};
//提供给导出用的回调函数
function KhglDcSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="khgl.lrsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="khgl.khdm";
    map["sortOrder"]="desc";
    return khglSearchData(map);
}

/**
 * 生产状态
 */
function scztFormat(value,row,index) {
    if (row.sfsc == '1') {
        return '需要生产';
    }else
        return "无需生产";

}
function khglSearchData(map) {
    var cxtj = $("#khgllist_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#khgllist_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == "1") {
        map["sfmc"] = cxnr
    } else if (cxtj == "2") {
        map["khdm"] = cxnr
    } else if (cxtj == "3") {
        map["khmc"] = cxnr
    } else if (cxtj == "4") {
        map["khjc"] = cxnr
    }else if (cxtj == "5") {
        map["lxr"] = cxnr
    }else if (cxtj == "6") {
        map["lxfs"] = cxnr
    }else if (cxtj == "7") {
        map["yb"] = cxnr
    }
    // 发展开始日期
    var fzrqstart = jQuery('#khgllist_formSearch #fzrqstart').val();
    map["fzrqstart"] = fzrqstart;
    // 发展结束日期
    var fzrqend = jQuery('#khgllist_formSearch #fzrqend').val();
    map["fzrqend"] = fzrqend;
    var khlbs = jQuery('#khgllist_formSearch #khlb_id_tj').val();
    map["khlbs"] = khlbs.replace(/'/g, "");
    return map;
}

function ProjectKhglResult(isTurnBack){
    if(isTurnBack){
        $('#khgllist_formSearch #khgl_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#khgllist_formSearch #khgl_list').bootstrapTable('refresh');
    }
}

function KhglDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#khgllist_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?khid=" +id;
        $.showDialog(url,'详细信息',viewHhglConfig);
    }else if(action == 'add'){
        var url= tourl;
        $.showDialog(url,'新增',addKhglConfig);
    }else if(action == 'mod'){
        var url= tourl+ "?khid=" +id;
        $.showDialog(url,'修改',modKhglConfig);
    }else if(action == 'productioncontrol'){
        var url= tourl+ "?ids=" +id;
        $.showDialog(url,'生产控制',controlKhglConfig);
    }
}

var addKhglConfig = {
    width		: "800px",
    modalName	: "addKhglModal",
    formName	: "khglxxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#khglxxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#khglxxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"khglxxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                ProjectKhglResult (true);
                            }
                        });
                    }else if(responseText["status"] == "khdmrepetition"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error("客户编码冲突！",function() {
                        });
                    }else if(responseText["status"] == "jcrepetition"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error("客户简称冲突！",function() {
                        });
                    } else if(responseText["status"] == "fail"){
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
/**
 * 查看页面模态框
 */
var viewHhglConfig={
    width		: "1400px",
    modalName	:"viewInvoiceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var modKhglConfig = {
    width		: "800px",
    modalName	: "modKhglModal",
    formName	: "khglxxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#khglxxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#khglxxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"khglxxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                ProjectKhglResult(true);
                            }
                        });
                    }else if(responseText["status"] == "khdmrepetition"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error("客户编码冲突！",function() {
                        });
                    }else if(responseText["status"] == "jcrepetition"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error("客户简称冲突！",function() {
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
var controlKhglConfig = {
    width		: "800px",
    modalName	: "controlKhglModal",
    formName	: "controlKhglForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#controlKhglForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#controlKhglForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"controlKhglForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                ProjectKhglResult(true);
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

var Khgl_ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#khgllist_formSearch #btn_query");
        var btn_add = $("#khgllist_formSearch #btn_add");
        var btn_mod = $("#khgllist_formSearch #btn_mod");
        var btn_view = $("#khgllist_formSearch #btn_view");
        var btn_del = $("#khgllist_formSearch #btn_del");
        var btn_searchexport = $("#khgllist_formSearch #btn_searchexport");
        var btn_selectexport = $("#khgllist_formSearch #btn_selectexport");
        var btn_productioncontrol = $("#khgllist_formSearch #btn_productioncontrol");
        //添加日期控件
        laydate.render({
            elem: '#khgllist_formSearch #fzrqstart'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#khgllist_formSearch #fzrqend'
            , theme: '#2381E9'
        });



        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#khgllist_formSearch #khgl_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                KhglDealById(sel_row[0].khid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------模糊查询-----------------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchKhglResult(true);
            });
        }
        /* ------------------------------新增-----------------------------*/
        btn_add.unbind("click").click(function(){
            KhglDealById(null,"add",btn_add.attr("tourl"));
        });
        /*-------------------------------修改------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#khgllist_formSearch #khgl_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                KhglDealById(sel_row[0].khid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------生产控制------------------------------*/
        btn_productioncontrol.unbind("click").click(function(){
            var sel_row = $('#khgllist_formSearch #khgl_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 0){
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i=0;i<sel_row.length;i++){
                    ids=ids+","+sel_row[i].khid;
                }
                ids=ids.substr(1);
                KhglDealById(ids, "productioncontrol", btn_productioncontrol.attr("tourl"));
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#khgllist_formSearch #khgl_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].khid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#khgllist_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchKhglResult(true);
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
        //---------------------------导出--------------------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#khgllist_formSearch #khgl_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].khid;
                }
                ids = ids.substr(1);
                $.showDialog($('#khgllist_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=KHGL_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#khgllist_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=KHGL_SEARCH&expType=search&callbackJs=KhglDcSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#khgllist_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Khgl_turnOff){
                $("#khgllist_formSearch #searchMore").slideDown("low");
                Khgl_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#khgllist_formSearch #searchMore").slideUp("low");
                Khgl_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};


function searchKhglResult(isTurnBack) {
    if (isTurnBack) {
        $('#khgllist_formSearch #khgl_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#khgllist_formSearch #khgl_list').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Khgl_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new Khgl_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#khgllist_formSearch .chosen-select').chosen({width: '100%'});

});
