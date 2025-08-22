var adminConfirmPurchaseAudit_turnOff = true;

var adminConfirmPurchaseAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#adminConfirmPurchase_auditing #adminConfirmPurchase_list').bootstrapTable({
            url: $("#adminConfirmPurchase_formAudit #urlPrefix").val()+'/administration/purchase/pageGetListAuditConfirmPurchaseAdministration',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#adminConfirmPurchase_auditing #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qrgl.qrrq",				//排序字段
            sortOrder: "ASC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
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
            uniqueId: "qrid",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                field: 'qrdh',
                title: '确认单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '确认人员',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qrrq',
                title: '确认日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dzfsmc',
                title: '对账方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zfdx',
                title: '支付对象',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zje',
                title: '总金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                adminConfirmPurchaseAuditDealById(row.qrid, 'view', $("#adminConfirmPurchase_auditing #btn_view").attr("tourl"));
            },
        });
        $("#adminConfirmPurchase_auditing #adminConfirmPurchase_list").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit',
                postbackSafe: true,
                partialRefresh: true
            }
        );
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token: $("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "qrid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#adminConfirmPurchase_auditing #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#adminConfirmPurchase_auditing #cxnr').val());
        // '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["qrdh"]=cxnr
        }
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

var adminConfirmPurchaseAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#adminConfirmPurchase_audited #audit_list').bootstrapTable({
            url: $("#adminConfirmPurchase_formAudit #urlPrefix").val()+'/administration/purchase/pageGetListAuditConfirmPurchaseAdministration',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#adminConfirmPurchase_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
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
            uniqueId: "qrid",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'qrdh',
                title: '确认单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qrrymc',
                title: '确认人员',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qrrq',
                title: '确认日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dzfsmc',
                title: '对账方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zfdx',
                title: '支付对象',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zje',
                title: '总金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                sortable: true,
                width: '16%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                titleTooltip:'审核人',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                titleTooltip:'审核时间',
                sortable: true,
                width: '16%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                titleTooltip:'是否通过',
                width: '10%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },

        });
        $("#adminConfirmPurchase_audited #audit_list").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit',
                postbackSafe: true,
                partialRefresh: true
            }
        );
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token: $("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "qrid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#adminConfirmPurchase_audited #cxtj_audited").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#adminConfirmPurchase_audited #cxnr_audited').val());
        // '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["qrdh"]=cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

/*查看详情信息模态框*/
var viewAdminConfirmPurchaseConfig = {
    width: "800px",
    height: "500px",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

//按钮动作函数
function adminConfirmPurchaseAuditDealById(id, action, tourl) {
    if (!tourl) {
        return;
    }
    tourl = $("#adminConfirmPurchase_formAudit #urlPrefix").val()+tourl;
    if (action == 'view') {
        var url = tourl + "?qrid=" + id;
        $.showDialog(url, '详情', viewAdminConfirmPurchaseConfig);
    } else if (action == 'audit') {
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_ADMINISTRATIONCONFIRMPURCHASE',
            url: tourl,
            data: {'ywzd': 'qrid'},
            title: "行政请购确认审核",
            prefix:$("#adminConfirmPurchase_formAudit #urlPrefix").val(),
            callback: function () {
                searchAdminConfirmPurchaseAudit();//回调
            },
            dialogParam: {width: 1000}
        });
    }
}


var adminConfirmPurchaseAudit_ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_audit = $("#adminConfirmPurchase_auditing #btn_audit");
        var btn_view = $("#adminConfirmPurchase_auditing #btn_view");
        var btn_query = $("#adminConfirmPurchase_auditing #btn_query");
        var btn_queryAudited = $("#adminConfirmPurchase_audited #btn_queryAudited");
        var btn_cancelAudit = $("#adminConfirmPurchase_audited #btn_cancelAudit");


        //绑定搜索发送功能
        if (btn_query != null) {
            btn_query.unbind("click").click(function () {
                searchAdminConfirmPurchaseAudit(true);
            });
        }
        //绑定搜索发送功能
        if (btn_queryAudited != null) {
            btn_queryAudited.unbind("click").click(function () {
                searchAdminConfirmPurchaseAudited(true);
            });
        }
        //取消审核
        if (btn_cancelAudit != null) {
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function () {
                cancelAudit($('#adminConfirmPurchase_audited #audit_list').bootstrapTable('getSelections'), function () {
                    searchAdminConfirmPurchaseAudited();
                });
            })
        }
        btn_view.unbind("click").click(function () {
            var sel_row = $('#adminConfirmPurchase_auditing #adminConfirmPurchase_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                adminConfirmPurchaseAuditDealById(sel_row[0].qrid, "view", btn_view.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });

        btn_audit.unbind("click").click(function () {
            var sel_row = $('#adminConfirmPurchase_auditing #adminConfirmPurchase_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                adminConfirmPurchaseAuditDealById(sel_row[0].qrid, "audit", btn_audit.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });


        //选项卡切换事件回调
        $('#adminConfirmPurchase_formAudit #audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#", '');
            var _gridId = "#adminConfirmPurchase_" + _hash;
            if (!e.target.isLoaded) {//只调用一次
                if (_hash == 'audited') {
                    var oTable = new adminConfirmPurchaseAudited_TableInit();
                    oTable.Init();
                } else {
                    var oTable = new adminConfirmPurchaseAuditing_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            } else {//重新加载
                //$(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

    };

    return oInit;
};

function searchAdminConfirmPurchaseAudit(isTurnBack) {
    if (isTurnBack) {
        $('#adminConfirmPurchase_auditing #adminConfirmPurchase_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#adminConfirmPurchase_auditing #adminConfirmPurchase_list').bootstrapTable('refresh');
    }
}

function searchAdminConfirmPurchaseAudited(isTurnBack) {
    if (isTurnBack) {
        $('#adminConfirmPurchase_audited #audit_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#adminConfirmPurchase_audited #audit_list').bootstrapTable('refresh');
    }
}

$(function () {

    var oTable= new adminConfirmPurchaseAuditing_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new adminConfirmPurchaseAudit_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#adminConfirmPurchase_auditing .chosen-select').chosen({width: '100%'});
    jQuery('#adminConfirmPurchase_audited .chosen-select').chosen({width: '100%'});

});
