var awardManagement_turnOff=true;
var awardManagement_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#awardManagementFormSearch #awardManagement_list').bootstrapTable({
            url: $("#awardManagementFormSearch #urlPrefix").val()+'/award/award/pageGetListAwardManagement',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#awardManagementFormSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jpgl.lrsj",					// 排序字段
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
            uniqueId: "jpglid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'jpglid',
                title: '奖品管理ID',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'bt',
                title: '标题',
                width: '16%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'ksrq',
                title: '开始日期',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jsrq',
                title: '结束日期',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'ssbmmc',
                title: '所属部门',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'jplxmc',
                title: '奖品类型',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'lrrymc',
                title: '录入人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'sffs',
                title: '是否发送',
                width: '8%',
                align: 'left',
                formatter:sffsformat,
                sortable:true,
                visible: true
            },{
                field: 'tzqmc',
                title: '通知群',
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
                awardManagementById(row.jpglid,'view',$("#awardManagementFormSearch #btn_view").attr("tourl"));
            }
        });
        $("#awardManagementFormSearch #awardManagement_list").colResizable({
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
            sortLastName: "jpgl.jpglid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return awardManagementSearchData(map);
    };
    return oTableInit;
};

function sffsformat(value,row,index){
    if(row.sffs=="1"){
        return "<span style='color:green;'>已发送</span>";
    }else{
        return "<span style='color:red;'>未发送</span>";
    }
}

function awardManagementSearchData(map){
    var cxtj=$("#awardManagementFormSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#awardManagementFormSearch #cxnr').val());
    if(cxtj=="0"){
        map["bt"]=cxnr;
    }
    var jplxs = jQuery('#awardManagementFormSearch #jplx_id_tj').val();
    map["jplxs"] = jplxs.replace(/'/g, "");

    var tzqs = jQuery('#awardManagementFormSearch #tzq_id_tj').val();
    map["tzqs"] = tzqs.replace(/'/g, "");
    return map;
}

function awardManagementById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#awardManagementFormSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?jpglid=" +id;
        $.showDialog(url,'查看',viewAwardManagementConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增奖品',addAwardManagementConfig);
    }else if(action =='mod'){
        var url=tourl + "?jpglid=" +id;
        $.showDialog(url,'修改奖品',modAwardManagementConfig);
    }else if(action =='copy'){
        var url=tourl + "?jpglid=" +id;
        $.showDialog(url,'复制奖品',addAwardManagementConfig);
    }
}

var viewAwardManagementConfig = {
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addAwardManagementConfig = {
    width		: "1200px",
    modalName	: "addAwardManagementModal",
    formName	: "addAwardManagementForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addAwardManagementForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                if(t_map.rows != null && t_map.rows.length > 0){
                    var isFind=false;
                    var json = [];
                    for(var i=0;i<t_map.rows.length;i++){
                        if("谢谢参与"==t_map.rows[i].jpmc){
                            isFind=true;
                        }
                        if(!t_map.rows[i].jpmc||!t_map.rows[i].sl||!t_map.rows[i].bs){
                            $.error("奖品名称,数量,倍数为必填项!");
                            return false;
                        }
                        if(t_map.rows[i].sftz&&"1"==t_map.rows[i].sftz&&!t_map.rows[i].tznr){
                            $.error("是否通知为是时必须填写通知内容!");
                            return false;
                        }
                        var sz = {"xh":i+1,"jpmxid":'',"jpmc":'',"sl":'',"bs":'',"sftz":'',"tznr":'',"fjid":''};
                        sz.jpmxid = t_map.rows[i].jpmxid;
                        sz.jpmc = t_map.rows[i].jpmc;
                        sz.sl = t_map.rows[i].sl;
                        sz.bs = t_map.rows[i].bs;
                        sz.sftz = t_map.rows[i].sftz;
                        sz.tznr = t_map.rows[i].tznr;
                        sz.fjid = t_map.rows[i].fjid;
                        json.push(sz);
                    }
                    if(!isFind){
                        $.error("必须有一条明细的奖品名称为:  谢谢参与  !数量可以为0!");
                        return false;
                    }
                    $("#addAwardManagementForm #jpmx_json").val(JSON.stringify(json));
                }else{
                    $.error("明细不允许为空!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#addAwardManagementForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"addAwardManagementForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        preventResubmitForm(".modal-footer > button", false);
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAwardManagementResult();
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
            className : "btn-default",
        }
    }
};

var modAwardManagementConfig = {
    width		: "1200px",
    modalName	: "modAwardManagementModal",
    formName	: "addAwardManagementForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addAwardManagementForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                if(t_map.rows != null && t_map.rows.length > 0){
                    var isFind=false;
                    var json = [];
                    for(var i=0;i<t_map.rows.length;i++){
                        if("谢谢参与"==t_map.rows[i].jpmc){
                            isFind=true;
                        }
                        if(!t_map.rows[i].jpmc||!t_map.rows[i].sl||!t_map.rows[i].bs){
                            $.error("奖品名称,数量,倍数为必填项!");
                            return false;
                        }
                        if(t_map.rows[i].sftz&&"1"==t_map.rows[i].sftz&&!t_map.rows[i].tznr){
                            $.error("是否通知为是时必须填写通知内容!");
                            return false;
                        }
                        var sz = {"xh":i+1,"jpmxid":'',"jpmc":'',"sl":'',"bs":'',"sftz":'',"tznr":'',"fjid":''};
                        sz.jpmxid = t_map.rows[i].jpmxid;
                        sz.jpmc = t_map.rows[i].jpmc;
                        sz.sl = t_map.rows[i].sl;
                        sz.bs = t_map.rows[i].bs;
                        sz.sftz = t_map.rows[i].sftz;
                        sz.tznr = t_map.rows[i].tznr;
                        sz.fjid = t_map.rows[i].fjid;
                        json.push(sz);
                    }
                    if(!isFind){
                        $.error("必须有一条明细的奖品名称为:  谢谢参与  !数量可以为0!");
                        return false;
                    }
                    $("#addAwardManagementForm #jpmx_json").val(JSON.stringify(json));
                }else{
                    $.error("明细不允许为空!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#addAwardManagementForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"addAwardManagementForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        preventResubmitForm(".modal-footer > button", false);
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAwardManagementResult();
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
            className : "btn-default",
        }
    }
};

var awardManagement_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        var btn_query = $("#awardManagementFormSearch #btn_query");
        var btn_view = $("#awardManagementFormSearch #btn_view");
        var btn_add = $("#awardManagementFormSearch #btn_add");
        var btn_mod = $("#awardManagementFormSearch #btn_mod");
        var btn_del=$("#awardManagementFormSearch #btn_del");
        var btn_copy=$("#awardManagementFormSearch #btn_copy");
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchAwardManagementResult(true);
            });
        }
        /* ---------------------------查看-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#awardManagementFormSearch #awardManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                awardManagementById(sel_row[0].jpglid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            awardManagementById(null, "add", btn_add.attr("tourl"));
        });
        /* ---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#awardManagementFormSearch #awardManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if("1"==sel_row[0].sffs){
                    $.error("已发送的奖品无法修改!");
                }else{
                    awardManagementById(sel_row[0].jpglid,"mod",btn_mod.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------复制-----------------------------------*/
        btn_copy.unbind("click").click(function(){
            var sel_row = $('#awardManagementFormSearch #awardManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                awardManagementById(sel_row[0].jpglid,"copy",btn_copy.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------删除-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#awardManagementFormSearch #awardManagement_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].jpglid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#awardManagementFormSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchAwardManagementResult();
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
        /*-----------------------显示隐藏------------------------------------*/
        $("#awardManagementFormSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(awardManagement_turnOff){
                $("#awardManagementFormSearch #searchMore").slideDown("low");
                awardManagement_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#awardManagementFormSearch #searchMore").slideUp("low");
                awardManagement_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};



function searchAwardManagementResult(isTurnBack){
    //关闭高级搜索条件
    $("#awardManagementFormSearch #searchMore").slideUp("low");
    awardManagement_turnOff=true;
    $("#awardManagementFormSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#awardManagementFormSearch #awardManagement_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#awardManagementFormSearch #awardManagement_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new awardManagement_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new awardManagement_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#awardManagementFormSearch .chosen-select').chosen({width: '100%'});

    $("#awardManagementFormSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});