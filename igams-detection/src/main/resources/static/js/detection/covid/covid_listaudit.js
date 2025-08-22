var covidAudit_turnOff = true;

var covidAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#covid_auditing #covid_list').bootstrapTable({
            url: '/detection/detection/pageGetListExamineCovid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#covid_auditing #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fzjcxx.ybbh",				//排序字段
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
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'tw',
                title: '体温',
                titleTooltip:'体温',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: false
            }, {
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'jczxmmc',
                title: '检测子项目',
                titleTooltip:'检测子项目',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'yblx',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'dwmc',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'zffs',
                title: '支付方式',
                titleTooltip:'支付方式',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'cjsj',
                title: '采集日期',
                titleTooltip:'采集日期',
                sortable: true,
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'jssj',
                title: '接收日期',
                titleTooltip:'接收日期',
                sortable: true,
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'sysj',
                title: '实验时间',
                titleTooltip:'实验时间',
                sortable: true,
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'jcjgmc',
                title: '检测结果',
                titleTooltip:'检测结果',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'jcdxcsdm',
                title: '检测对象',
                titleTooltip:'检测对象',
                width: '8%',
                align: 'left',
                formatter:jcdxformat,
                visible: true
            }, {
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
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
                CovidAuditDealById(row.fzjcid, 'view', $("#covid_auditing #btn_view").attr("tourl"));
            },
        });
        $("#covid_auditing #covid_list").colResizable({
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
            sortLastName: "fzjcxx.fzjcid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#covid_auditing #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#covid_auditing #cxnr').val());
        // '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
        if (cxtj == "0") {
            map["ybbh"] = cxnr
        } else if (cxtj == "1") {
            map["xm"] = cxnr
        } else if (cxtj == "2") {
            map["nl"] = cxnr
        }
        var jcdxlx = jQuery('#covid_auditing #jcdxlx_id_tj').val();
        map["jcdxlxs"] = jcdxlx;
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

var covidAudit_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var jcdxlx=$("#covid_auditing a[id^='jcdxlx_id_']")

        $.each(jcdxlx, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            var csdm=$("#covid_auditing #"+id).attr("csdm");
            if(csdm == 'R'){
                addTj('jcdxlx',code,'covid_auditing');
            }
        });
    }
    return oInit;
}

function jcdxformat(value,row,index){
    var html="";
    if(row.jcdxcsdm=='W'){
        html="<span>"+"物检"+"</span>";
    }else if(row.jcdxcsdm=='R'){
        html="<span>"+"人检"+"</span>";
    }
    return html;
}


var covidAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#covid_audited #audit_list').bootstrapTable({
            url: '/detection/detection/pageGetListExamineCovid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#covid_audited #toolbar',                //工具按钮用哪个容器
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
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'syh',
                title: '实验号',
                titleTooltip:'实验号',
                sortable: true,
                width: '14%',
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
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '6%',
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
                field: 'jczxmmc',
                title: '检测子项目',
                titleTooltip:'检测子项目',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'jcdxcsdm',
                title: '检测对象',
                titleTooltip:'检测对象',
                width: '8%',
                align: 'left',
                formatter:jcdxformat,
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
        $("#covid_audited #audit_list").colResizable({
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
            sortLastName: "fzjcxx.fzjcid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#covid_audited #cxtj_audited").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#covid_audited #cxnr_audited').val());
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
var viewCovidConfig = {
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

var batchAuditCovidConfig = {
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
                var sel_row = $('#covid_auditing #covid_list').bootstrapTable('getSelections');//获取选择行数据
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
                    //1.启动进度条检测
                    setTimeout("checkCovidAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);

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
var checkCovidAuditCheckStatus = function(intervalTime,loadYmCode){
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
                    setTimeout("checkCovidAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
                }
            }else{
                if(data.status == "2"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.success(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchCovidAudit();
                    });
                }else if(data.status == "0"){
                    if($("#cardiv")) $("#cardiv").remove();
                    $.alert(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchCovidAudit();
                    });
                }else{
                    if($("#cardiv")) $("#cardiv").remove();
                    $.error(data.msg,function(){
                        $.closeModal("batchAuditModal");
                        searchCovidAudit();
                    });
                }
            }
        }
    });
}

//按钮动作函数
function CovidAuditDealById(id, action, tourl,hzid,jcxmmc,xm) {
    if (!tourl) {
        return;
    }
    if (action == 'view') {
        var url = tourl + "?fzjcid=" + id;
        $.showDialog(url, '详情', viewCovidConfig);
    }else  if (action == 'historicalrecords') {
        var url = tourl + "?fzjcid=" + id+"&hzid="+hzid+"&jcxmmc="+jcxmmc+"&xm="+xm+"&jclx="+$("#covid_formAudit #jclx").val();
        $.showDialog(url, '历史记录', viewHistoryConfig);
    } else if (action == 'audit') {
        var url = tourl;
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_COVID',
            url: $("#covid_auditing #btn_audit").attr("tourl"),
            data: {'ywzd': 'fzjcid'},
            title: $("#covid_auditing #btn_audit").attr("gnm"),
            prefix:false,
            callback: function () {
                searchCovidAudit();//回调
            },
            dialogParam: {width: 1000}
        });
    }else if(action =='batchaudit'){
        var url=tourl + "?ywids=" +id+"&shlb=AUDIT_COVID&ywzd=fzjcid&business_url="+tourl;
        $.showDialog(url,'批量审核',batchAuditCovidConfig);
    }
}


var covidAudit_ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_audit = $("#covid_auditing #btn_audit");
        var btn_view = $("#covid_auditing #btn_view");
        var btn_query = $("#covid_auditing #btn_query");
        var btn_historicalrecords = $("#covid_auditing #btn_historicalrecords");
        var btn_queryAudited = $("#covid_audited #btn_queryAudited");
        var btn_cancelAudit = $("#covid_audited #btn_cancelAudit");
        var btn_batchaudit = $("#covid_auditing #btn_batchaudit");


        //绑定搜索发送功能
        if (btn_query != null) {
            btn_query.unbind("click").click(function () {
                searchCovidAudit(true);
            });
        }
        //绑定搜索发送功能
        if (btn_queryAudited != null) {
            btn_queryAudited.unbind("click").click(function () {
                searchCovidAudited(true);
            });
        }
        //取消审核
        if (btn_cancelAudit != null) {
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function () {
                cancelAudit($('#covid_audited #audit_list').bootstrapTable('getSelections'), function () {
                    searchCovidAudited();
                });
            })
        }
        btn_view.unbind("click").click(function () {
            var sel_row = $('#covid_auditing #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                CovidAuditDealById(sel_row[0].fzjcid, "view", btn_view.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });
        btn_historicalrecords.unbind("click").click(function () {
            var sel_row = $('#covid_auditing #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                if("R"==sel_row[0].jcdxcsdm){
                    CovidAuditDealById(sel_row[0].fzjcid,"historicalrecords", btn_historicalrecords.attr("tourl"),sel_row[0].hzid,sel_row[0].jcxmmc,sel_row[0].xm);
                }else{
                    $.error("请选择检测对象为人检的记录！");
                    return;
                }
            } else {
                $.error("请选中一行");
            }
        });

        btn_audit.unbind("click").click(function () {
            var sel_row = $('#covid_auditing #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                CovidAuditDealById(sel_row[0].fzjcid, "audit", btn_audit.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });

        //批量审核
        btn_batchaudit.unbind("click").click(function(){
            var sel_row = $('#covid_auditing #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fzjcid;
            }
            ids = ids.substr(1);
            CovidAuditDealById(ids,"batchaudit",btn_batchaudit.attr("tourl"));
        });

        //选项卡切换事件回调
        $('#covid_formAudit #audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#", '');
            var _gridId = "#covid_" + _hash;
            if (!e.target.isLoaded) {//只调用一次
                if (_hash == 'audited') {
                    var oTable = new covidAudited_TableInit();
                    oTable.Init();
                } else {
                    var oTable = new covidAuditing_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            } else {//重新加载
                //$(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /**显示隐藏**/
        $("#covid_auditing #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (covidAudit_turnOff) {
                $("#covid_auditing #searchMore").slideDown("low");
                covidAudit_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#covid_auditing #searchMore").slideUp("low");
                covidAudit_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
            //showMore();
        });
    };

    return oInit;
};

function searchCovidAudit(isTurnBack) {
    //关闭高级搜索条件
    $("#covid_auditing #searchMore").slideUp("low");
    covidAudit_turnOff=true;
    $("#covid_auditing #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#covid_auditing #covid_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#covid_auditing #covid_list').bootstrapTable('refresh');
    }
}

function searchCovidAudited(isTurnBack) {
    if (isTurnBack) {
        $('#covid_audited #audit_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#covid_audited #audit_list').bootstrapTable('refresh');
    }
}

$(function () {
    var oInit = new covidAudit_PageInit();
    oInit.Init();
    var oTable= new covidAuditing_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new covidAudit_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#covid_auditing .chosen-select').chosen({width: '100%'});
    $("#covid_auditing [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    jQuery('#covid_audited .chosen-select').chosen({width: '100%'});

});
