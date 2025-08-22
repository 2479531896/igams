var sysxx_turnOff=true;
var sysxx_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#sysxx_formSearch #sysxx_list').bootstrapTable({
            url: '/wechat/sys/pagedataSysxx',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#sysxx_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"sysxx.sysid",					// 排序字段
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
            uniqueId: "sysid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '5%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '10%',
                'sortable': 'true',
                'formatter': function (value, row, index) {
                    var options = $('#sysxx_formSearch #sysxx_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'dfsysmc',
                title: '对方实验室名称',
                width: '20%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'wfsysmc',
                title: '我方实验室名称',
                width: '20%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'wfsysdm',
                title: '我方实验室代码',
                width: '20%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zq',
                title: '周期',
                width: '20%',
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
                sysxxByCode(row.sysid,'view',$("#sysxx_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#sysxx_formSearch #sysxx_list").colResizable({
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
            sortLastName: "dfsysmc", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getSysxxSearchData(map);
    };
    return oTableInit;
};

function getSysxxSearchData(map) {
    var cxtj = $("#sysxx_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#sysxx_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == '1'){
        map["dfsysmc"] = cxnr
    }else if (cxtj =='2'){
        map["wfsysmc"]=cxnr
    }
    return map;
}

function sysxxResult(isTurnBack){
    if(isTurnBack){
        $('#sysxx_formSearch #sysxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sysxx_formSearch #sysxx_list').bootstrapTable('refresh');
    }
}

function sysxxByCode(sysid,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?sysid=" +sysid;
        $.showDialog(url,'详细信息',viewSysxxConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'增加信息',addSysxxConfig);
    }else if(action =='mod'){
        var url=tourl + "?sysid=" +sysid;
        $.showDialog(url,'修改信息',modSysxxConfig);
    }
}


var sysxx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#sysxx_formSearch #btn_view");
        var btn_query = $("#sysxx_formSearch #btn_query");
        var btn_add = $("#sysxx_formSearch #btn_add");
        var btn_mod = $("#sysxx_formSearch #btn_mod");
        var btn_del = $("#sysxx_formSearch #btn_del");

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchSysxxResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#sysxx_formSearch #sysxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sysxxByCode(sel_row[0].sysid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------新增信息-----------------------------------*/
        btn_add.unbind("click").click(function(){
            sysxxByCode(null,"add",btn_add.attr("tourl"));
        });
        /*---------------------------编辑信息-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#sysxx_formSearch #sysxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sysxxByCode(sel_row[0].sysid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------删除信息-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#sysxx_formSearch #sysxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].sysid;
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
                                    searchSysxxResult();
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
        $("#sysxx_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(sysxx_turnOff){
                $("#sysxx_formSearch #searchMore").slideDown("low");
                sysxx_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#sysxx_formSearch #searchMore").slideUp("low");
                sysxx_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchSysxxResult(isTurnBack){
    //关闭高级搜索条件
    $("#sysxx_formSearch #searchMore").slideUp("low");
    sysxx_turnOff=true;
    $("#sysxx_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#sysxx_formSearch #sysxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sysxx_formSearch #sysxx_list').bootstrapTable('refresh');
    }
}

var viewSysxxConfig = {
    width		: "400px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addSysxxConfig = {
    width		: "1000px",
    modalName	: "addSysxxModal",
    formName	: "addSysxxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addSysxxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#addSysxxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"addSysxxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                sysxxResult();
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

var modSysxxConfig = {
    width		: "800px",
    modalName	: "modSysxxModal",
    formName	: "addSysxxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addSysxxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#addSysxxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"addSysxxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                sysxxResult();
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

$(function(){

    //0.界面初始化
    // 1.初始化Table
    var oTable = new sysxx_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new sysxx_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#sysxx_formSearch .chosen-select').chosen({width: '100%'});


    $("#sysxx_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});