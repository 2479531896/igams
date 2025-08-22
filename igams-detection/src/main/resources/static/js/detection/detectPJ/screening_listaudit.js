var screeningAudit_turnOff = true;

var screeningAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#screening_auditing #screening_list').bootstrapTable({
            url: '/detectionPJ/detectionPJ/pageGetListExamineScreening',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#screening_auditing #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fzjcxm.ybbh",				//排序字段
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
            uniqueId: "fzjcxx.fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                sortable: true,
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'syh',
                title: '实验号',
                titleTooltip:'实验号',
                sortable: true,
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'xm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '4%',
                align: 'left',
                formatter:xbformat,
                visible: true
            }, {
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '4%',
                align: 'left',
                visible: true
            }, {
                field: 'tw',
                title: '体温',
                titleTooltip:'体温',
                sortable: true,
                width: '4%',
                align: 'left',
                visible: false
            }, {
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'jczxmmc',
                title: '检测子项目',
                titleTooltip:'检测子项目',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'yblxmc',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'jssj',
                title: '接收日期',
                titleTooltip:'接收日期',
                sortable: true,
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'sysj',
                title: '实验时间',
                titleTooltip:'实验时间',
                sortable: true,
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                screeningAuditDealById(row.fzjcid, 'view', $("#screening_auditing #btn_view").attr("tourl"));
            },
        });
        $("#screening_auditing #screening_list").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit',
                postbackSafe: true,
                partialRefresh: true,
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
            sortLastName: "fzjcxm.fzjcid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            jclx:$("#screening_formAudit #jclx").val(),
            flag:'audit'

            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#screening_auditing #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#screening_auditing #cxnr').val());
        if(cxtj=="0"){
            map["ybbh"]=cxnr
        }else if(cxtj=="1"){
            map["nbbh"]=cxnr
        }else if(cxtj=='2'){
            map["xm"]=cxnr
        }else if(cxtj=='3'){
            map["syh"]=cxnr
        }else if(cxtj=='4'){
            map["sjdwmc"]=cxnr
        }else if(cxtj=='5'){
            map["nl"]=cxnr
        }else if(cxtj=='6'){
            map["bbzbh"]=cxnr
        }
        // 接收日期
        var jssjstart = jQuery('#screening_auditing #jssjstart').val();
        map["jssjstart"] = jssjstart;
        var jssjend = jQuery('#screening_auditing #jssjend').val();
        map["jssjend"] = jssjend;
        // 检测日期
        var sysjstart = jQuery('#screening_auditing #sysjstart').val();
        map["sysjstart"] = sysjstart;
        var sysjend = jQuery('#screening_auditing #sysjend').val();
        map["sysjend"] = sysjend;
        // 报告日期
        var bgrqstart = jQuery('#screening_auditing #bgrqstart').val();
        map["bgrqstart"] = bgrqstart;
        var bgrqend = jQuery('#screening_auditing #bgrqend').val();
        map["bgrqend"] = bgrqend;
        var yblxs = jQuery('#screening_auditing #yblx_id_tj').val();
        map["yblxs"] = yblxs;
        var zts = jQuery('#screening_auditing #zt_id_tj').val();
        map["zts"] = zts;
        var sfsy = jQuery('#screening_auditing #sfsy_id_tj').val();
        map["sfsy"] = sfsy;

        var sfjs = jQuery('#screening_auditing #sfjs_id_tj').val();
        map["sfjs"] = sfjs;
        return map;
    };
    return oTableInit;
};
//性别格式化
function xbformat(value,row,index){
    if(row.xb=='1'){
        return '男';

    }else  if(row.xb=='2'){
        return '女';
    }else{
        return '未知';
    }
}

var screeningAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#screening_audited #audit_list').bootstrapTable({
            url: '/detectionPJ/detectionPJ/pageGetListExamineScreening',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#screening_audited #toolbar',                //工具按钮用哪个容器
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
            uniqueId: "fzjcxx.fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                sortable: true,
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'syh',
                title: '实验号',
                titleTooltip:'实验号',
                sortable: true,
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'xm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '16%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                sortable: true,
                width: '16%',
                align: 'left',
                sortable: true,
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
                width: '6%',
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
                width: '8%',
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
        $("#screening_audited #audit_list").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit',
                postbackSafe: true,
                partialRefresh: true,
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
            sortLastName: "fzjcxx.fzjcid", //防止同名排位用
            sortLastOrder: "asc" ,//防止同名排位用
            jclx:$("#screening_formAudit #jclx").val(),
            flag:'audited'
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#screening_audited #cxtj_audited").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#screening_audited #cxnr_audited').val());
        // '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
        if (cxtj == "0") {
            map["ybbh"] = cxnr
        } else if (cxtj == "1") {
            map["xm"] = cxnr
        } else if (cxtj == "2") {
            map["nl"] = cxnr
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

/*查看详情信息模态框*/
var viewscreeningConfig = {
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

/*查看详情信息模态框*/
var viewHistoryConfig = {
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

var batchAuditscreeningConfig = {
    width		: "800px",
    modalName	: "batchAuditModal",
    formName	: "batchAuditAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#batchAuditAjaxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#batchAuditAjaxForm input[name='access_token']").val($("#ac_tk").val());
                var loadYmCode = $("#batchAuditAjaxForm #batchaudit_loadYmCode").val();
                var sel_row = $('#screening_auditing #screening_list').bootstrapTable('getSelections');//获取选择行数据
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
                    //1.启动进度条检测
                    setTimeout("checkscreeningAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);

                    //绑定导出取消按钮事件
                    $("#exportCancel").click(function(){
                        //先移除导出提示，然后请求后台
                        if($("#cardiv").length>0) $("#cardiv").remove();
                        $.ajax({
                            type : "POST",
                            url : "/systemcheck/auditProcess/cancelAuditProcess",
                            data : {"loadYmCode" : loadYmCode+"","access_token":$("#ac_tk").val()},
                            dataType : "json",
                            success:function(data){
                                if(data != null && data.result==false){
                                    if(data.msg && data.msg!="")
                                        $.error(data.msg);
                                }
                            }
                        });
                    });
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

//上传后自动提交服务器检查进度
var checkscreeningAuditCheckStatus = function(intervalTime,loadYmCode){
    $.ajax({
        type : "POST",
        url : "/systemcheck/auditProcess/commCheckAudit",
        data : {"loadYmCode":loadYmCode,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.cancel){//取消则直接return
                return;
            }
            if(data.result==false){
                if(data.status == "-2"){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if(intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if($("#exportCount")){
                        $("#exportCount").html(data.currentCount)
                    }
                    setTimeout("checkscreeningAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
                }
            }else{
                if(data.status == "2"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.success(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchscreeningAudit();
                    });
                }else if(data.status == "0"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.alert(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchscreeningAudit();
                    });
                }else{
                    if($("#cardiv")) $("#cardiv").remove();
                    $.error(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchscreeningAudit();
                    });
                }
            }
        }
    });
}

//按钮动作函数
function screeningAuditDealById(id, action, tourl) {
    if (!tourl) {
        return;
    }
    if (action == 'view') {
        var url = tourl + "?fzjcid=" + id;
        $.showDialog(url, '详情', viewscreeningConfig);
    } else if (action == 'audit') {
        var url = tourl;
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_GENERAL_INSPECTION',
            url: $("#screening_auditing #btn_audit").attr("tourl"),
            data: {'ywzd': 'fzxmid'},
            title: $("#screening_auditing #btn_audit").attr("gnm"),
            prefix:false,
            callback: function () {
                searchscreeningAudit();//回调
            },
            dialogParam: {width: 1000}
        });
    }else if(action =='batchaudit'){
        var url=tourl + "?ywids=" +id+"&shlb=AUDIT_GENERAL_INSPECTION&ywzd=fzjcid&business_url="+tourl;
        $.showDialog(url,'批量审核',batchAuditscreeningConfig);
    }
}


var screeningAudit_ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_audit = $("#screening_auditing #btn_audit");
        var btn_view = $("#screening_auditing #btn_view");
        var btn_query = $("#screening_auditing #btn_query");
        var btn_queryAudited = $("#screening_audited #btn_queryAudited");
        var btn_cancelAudit = $("#screening_audited #btn_cancelAudit");
        var btn_batchaudit = $("#screening_auditing #btn_batchaudit");

        //添加日期控件
        laydate.render({
            elem: '#cjsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#cjsjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jssjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sysjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqend'
            ,theme: '#2381E9'
        });
        //绑定搜索发送功能
        if (btn_query != null) {
            btn_query.unbind("click").click(function () {
                searchscreeningAudit(true);
            });
        }
        //绑定搜索发送功能
        if (btn_queryAudited != null) {
            btn_queryAudited.unbind("click").click(function () {
                searchscreeningAudited(true);
            });
        }
        //取消审核
        if (btn_cancelAudit != null) {
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function () {
                cancelAudit($('#screening_audited #audit_list').bootstrapTable('getSelections'), function () {
                    searchscreeningAudited();
                });
            })
        }
        btn_view.unbind("click").click(function () {
            var sel_row = $('#screening_auditing #screening_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                screeningAuditDealById(sel_row[0].fzjcid, "view", btn_view.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });

        btn_audit.unbind("click").click(function () {
            var sel_row = $('#screening_auditing #screening_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                screeningAuditDealById(sel_row[0].fzxmid, "audit", btn_audit.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });

        //批量审核
        btn_batchaudit.unbind("click").click(function(){
            var sel_row = $('#screening_auditing #screening_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fzxmid;
            }
            ids = ids.substr(1);
            screeningAuditDealById(ids,"batchaudit",btn_batchaudit.attr("tourl"));
        });

        //选项卡切换事件回调
        $('#screening_formAudit #audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#", '');
            var _gridId = "#screening_" + _hash;
            if (!e.target.isLoaded) {//只调用一次
                if (_hash == 'auditedScreening') {
                    var oTable = new screeningAudited_TableInit();
                    oTable.Init();
                } else {
                    var oTable = new screeningAuditing_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            } else {//重新加载
                //$(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /**显示隐藏**/
        $("#screening_auditing #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (screeningAudit_turnOff) {
                $("#screening_auditing #searchMore").slideDown("low");
                screeningAudit_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#screening_auditing #searchMore").slideUp("low");
                screeningAudit_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
            //showMore();
        });
    };

    return oInit;
};

function searchscreeningAudit(isTurnBack) {
    //关闭高级搜索条件
    $("#screening_auditing #searchMore").slideUp("low");
    screeningAudit_turnOff=true;
    $("#screening_auditing #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#screening_auditing #screening_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#screening_auditing #screening_list').bootstrapTable('refresh');
    }
}

function searchscreeningAudited(isTurnBack) {
    if (isTurnBack) {
        $('#screening_audited #audit_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#screening_audited #audit_list').bootstrapTable('refresh');
    }
}

$(function () {
    var oTable= new screeningAuditing_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new screeningAudit_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#screening_auditing .chosen-select').chosen({width: '100%'});
    $("#screening_auditing [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    jQuery('#screening_audited .chosen-select').chosen({width: '100%'});

});
