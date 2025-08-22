var wbaqyz_turnOff=true;
var wbaqyz_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable({
            url: '/wbaqyz/wbaqyz/pagedataWbaqyz',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#wbaqyz_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wb.code",					// 排序字段
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
            uniqueId: "code",                     // 每一行的唯一标识，一般为主键列
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
                    var options = $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'code',
                title: '代码',
                width: '20%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'mc',
                title: '名称',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'key',
                title: '密钥',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'word',
                title: '关键词',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'address',
                title: '地址',
                width: '15%',
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
                wbaqyzByCode(row.code,'view',$("#wbaqyz_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#wbaqyz_formSearch #wbaqyz_list").colResizable({
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
            sortLastName: "mc", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getWbaqyzSearchData(map);
    };
    return oTableInit;
};

function getWbaqyzSearchData(map) {
    var cxtj = $("#wbaqyz_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#wbaqyz_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["code"] = cxnr
    } else if (cxtj == '1'){
        map["mc"] = cxnr
    }else if (cxtj =='2'){
        map["entire"]=cxnr
    }

    return map;
}



function wbaqyzResult(isTurnBack){
    if(isTurnBack){
        $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('refresh');
    }
}



function wbaqyzByCode(code,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?code=" +code;
        $.showDialog(url,'详细信息',viewWbaqyzConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'增加信息',addWbaqyzConfig);
    }else if(action =='mod'){
        var url=tourl + "?code=" +code;
        $.showDialog(url,'修改信息',modWbaqyzConfig);
    }
    else if(action =='relpartner'){
        var url=tourl + "?code=" +code;
        $.showDialog(url,'关联伙伴',relWbaqyzConfig);
    }
}


var wbaqyz_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#wbaqyz_formSearch #btn_view");
        var btn_query = $("#wbaqyz_formSearch #btn_query");
        var btn_add = $("#wbaqyz_formSearch #btn_add");
        var btn_mod = $("#wbaqyz_formSearch #btn_mod");
        var btn_del = $("#wbaqyz_formSearch #btn_del");
        var btn_relpartner = $("#wbaqyz_formSearch #btn_relpartner");

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchWbaqyzResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                wbaqyzByCode(sel_row[0].code,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------新增信息-----------------------------------*/
        btn_add.unbind("click").click(function(){
            wbaqyzByCode(null,"add",btn_add.attr("tourl"));
        });
        /*---------------------------关联信息-----------------------------------*/
        btn_relpartner.unbind("click").click(function(){
            var sel_row = $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                wbaqyzByCode(sel_row[0].code,"relpartner",btn_relpartner.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------编辑信息-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                wbaqyzByCode(sel_row[0].code,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------删除信息-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].code;
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
                                    searchWbaqyzResult();
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
        $("#wbaqyz_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(wbaqyz_turnOff){
                $("#wbaqyz_formSearch #searchMore").slideDown("low");
                wbaqyz_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#wbaqyz_formSearch #searchMore").slideUp("low");
                wbaqyz_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchWbaqyzResult(isTurnBack){
    //关闭高级搜索条件
    $("#wbaqyz_formSearch #searchMore").slideUp("low");
    wbaqyz_turnOff=true;
    $("#wbaqyz_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#wbaqyz_formSearch #wbaqyz_list').bootstrapTable('refresh');
    }
}



var sfbc=0;//是否继续保存
var viewWbaqyzConfig = {
    width		: "400px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var relWbaqyzConfig = {
    width		: "1000px",
    modalName	: "allocationModal",
    formName	: "taskListPartnerForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#taskListPartnerForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#taskListPartnerForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"taskListPartnerForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                wbaqyzResult ();
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

var addWbaqyzConfig = {
    width		: "1000px",
    modalName	: "addWbaqyzModal",
    formName	: "ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                wbaqyzResult ();
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

var modWbaqyzConfig = {
    width		: "800px",
    modalName	: "modWbaqyzModal",
    formName	: "ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                wbaqyzResult();
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
    var oTable = new wbaqyz_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new wbaqyz_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#wbaqyz_formSearch .chosen-select').chosen({width: '100%'});


    $("#wbaqyz_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});