var productManual_turnOff=true;
var productManual_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#productManual_formSearch #productManual_list').bootstrapTable({
            url: '/productManual/productManual/pageGetListProductManual',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#productManual_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"cpwj.cpmc DESC,cpwj.bbh DESC,cpwj.xh",					// 排序字段
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
            uniqueId: "cpwjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                field: 'cpwjid',
                title: '产品文件id',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'cpmc',
                title: '产品名称',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cpdm',
                title: '产品代码',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bbh',
                title: '版本号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gxsj',
                title: '更新时间',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sfgk',
                title: '是否公开',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:sfgkformat
            },{
                field: 'xh',
                title: '序号',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: false
            }
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                productManualById(row.cpwjid,'view',$("#productManual_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#productManual_formSearch #productManual_list").colResizable({
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
            sortLastName: "cpwjid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return productManualSearchData(map);
    };
    return oTableInit;
};

function sfgkformat(value,row,index){
    var sfgk= "";
    if(row.sfgk=='1'){
        sfgk="<span style='color:green;'>"+"是"+"</span>";
    }else if(row.sfgk=='0'){
        sfgk="<span style='color:red;'>"+"否"+"</span>";
    }
    return sfgk;
}

function productManualSearchData(map){
    var cxtj=$("#productManual_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#productManual_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["cpmc"]=cxnr;
    }else if(cxtj=="2"){
        map["cpdm"]=cxnr;
    }else if (cxtj == "3") {
        map["lrrymc"] = cxnr;
    }
    //是否公开
    var sfgk =jQuery('#productManual_formSearch #sfgk_id_tj').val();
    map["sfgk"] = sfgk;
    return map;
}

function productManualById(id,action,tourl) {
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?cpwjid=" +id;
        $.showDialog(url,'产品说明书查看',viewProductManualConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增产品说明书',addProductManualConfig);
    }else if(action =='mod'){
        var url=tourl + "?cpwjid=" +id;
        $.showDialog(url,'修改产品说明书',modProductManualConfig);
    }
}

//新增
var addProductManualConfig = {
    width		: "1200px",
    modalName	: "addProductManualModal",
    formName	: "productManualForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                if(!$("#productManualForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#productManualForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"productManualForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            productManualResult();
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

var modProductManualConfig = {
    width		: "1200px",
    modalName	: "modProductManualModal",
    formName	: "productManualForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                if(!$("#productManualForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#productManualForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"productManualForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            productManualResult();
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

var viewProductManualConfig = {
    modalName	: "viewProductManualModal",
    formName	: "productManualViewForm",
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var productManual_ButtonInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        var btn_view = $("#productManual_formSearch #btn_view");
        var btn_query = $("#productManual_formSearch #btn_query");
        var btn_add = $("#productManual_formSearch #btn_add");
        var btn_del = $("#productManual_formSearch #btn_del");
        var btn_publicornot = $("#productManual_formSearch #btn_publicornot");
        var btn_mod = $("#productManual_formSearch #btn_mod");
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                productManualResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#productManual_formSearch #productManual_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                productManualById(sel_row[0].cpwjid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 新增产品说明书 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            productManualById(null, "add", btn_add.attr("tourl"));
        });

        btn_mod.unbind("click").click(function(){
            var sel_row = $('#productManual_formSearch #productManual_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                productManualById(sel_row[0].cpwjid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------删除----------------------------------
        btn_del.unbind("click").click(function(){
            var sel_row = $('#productManual_formSearch #productManual_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].cpwjid;
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
                                    productManualResult(true);
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
        //---------------------------是否公开----------------------------------
        btn_publicornot.unbind("click").click(function(){
            var sel_row = $('#productManual_formSearch #productManual_list').bootstrapTable('getSelections');//获取选择行数据
            var msg = '';
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }else if (sel_row.length >1){
                $.error("请选中一行");
                return;
            }else {
                if (sel_row[0].sfgk=='0'){
                    msg = '您确定要公开所选择的记录吗？';
                }else {
                    msg = '您确定要关闭所选择的记录吗？';
                }
            }
            $.confirm(msg,function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_publicornot.attr("tourl");
                    jQuery.post(url,{"cpwjid":sel_row[0].cpwjid,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    productManualResult(true);
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
        $("#productManual_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(productManual_turnOff){
                $("#productManual_formSearch #searchMore").slideDown("low");
                productManual_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#productManual_formSearch #searchMore").slideUp("low");
                productManual_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};
var productManual_PageInit = function(){
    var oInit = new Object();
    oInit.Init = function () {
        addTj('sfgk','1','productManual_formSearch');
    }
    return oInit;
}
function productManualResult(isTurnBack){
    //关闭高级搜索条件
    $("#productManual_formSearch #searchMore").slideUp("low");
    productManual_turnOff=true;
    $("#productManual_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#productManual_formSearch #productManual_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#productManual_formSearch #productManual_list').bootstrapTable('refresh');
    }
}


$(function(){
    var oInit = new productManual_PageInit();
    oInit.Init();
    // 1.初始化Table
    var oTable = new productManual_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new productManual_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#productManual_formSearch .chosen-select').chosen({width: '100%'});

    $("#productManual_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});