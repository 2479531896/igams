var DetectCsm_turnOff=true;
var DetectCsm_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#detectCsm_formSearch #detectCsm_list").bootstrapTable({
            url: '/detection/detection/pageGetListCsm',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#detectCsm_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "csmid",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100,1000],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "csmid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                field: 'csmid',
                title: '场所码ID',
                titleTooltip:'场所码ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'yhid',
                title: '用户ID',
                titleTooltip:'用户ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '用户姓名',
                titleTooltip:'用户姓名',
                sortable: true,
                width: '6%',
                align: 'left',
                visible:true
            },{
                field: 'cydmc',
                title: '采样点',
                titleTooltip:'采样点',
                width: '14%',
                align: 'left',
                visible:true
            },{
                field: 'sjdwmc',
                title: '医院名称',
                titleTooltip:'医院名称',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'yxrq',
                title: '有效日期',
                titleTooltip:'有效日期',
                width: '10%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                DetectCsm_DealById(row.csmid,"view", $("#detectCsm_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#detectCsm_formSearch #detectCsm_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "csm.csmid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getDetectCsmSearchData(map);
    };
    return oTableInit;
}

function getDetectCsmSearchData(map){
    var detectCsm_select=$("#detectCsm_formSearch #detectCsm_select").val();
    var detectCsm_input=$.trim(jQuery('#detectCsm_formSearch #detectCsm_input').val());
    if(detectCsm_select=="0"){
        map["cyd"]=detectCsm_input
    }else if(detectCsm_select=="1"){
        map["cyd"]=detectCsm_input
    }
    return map;
}

function searchDetectCsmResult(isTurnBack){
    //关闭高级搜索条件
    $("#detectCsm_formSearch #searchMore").slideUp("low");
    DetectCsm_turnOff=true;
    $("#detectCsm_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#detectCsm_formSearch #detectCsm_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectCsm_formSearch #detectCsm_list').bootstrapTable('refresh');
    }
}

function DetectCsm_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?csmid="+id
        $.showDialog(url,'查看场所码',viewDetectCsmConfig);
    }else if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增场所码',addDetectCsmConfig);
    }else if(action=='mod'){
        var url=tourl + "?csmid=" +id;
        $.showDialog(url,'修改场所码',editDetectCsmConfig);
    }
}

/**修改**/
var editDetectCsmConfig = {
    width		: "800px",
    modalName   :"editDetectCsmConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function(){
                if(!$("#editCsmForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editCsmForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editCsmForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchDetectCsmResult();
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

var viewDetectCsmConfig = {
    width		: "800px",
    modalName	:"viewDetectCsmModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addDetectCsmConfig = {
    width		: "800px",
    modalName	:"addDetectCsmModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editCsmForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editCsmForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editCsmForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                            if (opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchDetectCsmResult()
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
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
/*查看详情信息模态框*/
var payDetectionConfigxx = {
    width		: "600px",
    height		: "300px",
    modalName	: "payDetectionConfigxxx",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保存图片",
            className : "btn-primary",
            callback : function() {
                clearTimeout(timer);
                // let fzjcid = $("#payOrderInfoxxx #csmid").val();
                // if (!fzjcid){
                //     $.error("更新失败!");
                //     return;
                // }
                // let sfje =$("#fkje").val();
                // zfwc(fzjcid,sfje);
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default",
            callback : function() {
                clearTimeout(timer);
            }
        }
    }
};

var DetectCsm_oButton=function(){
    var oButtonInit = new Object();
    oButtonInit.Init=function(){
        var btn_query=$("#detectCsm_formSearch #btn_query");
        var btn_view = $("#detectCsm_formSearch #btn_view");//查看
        var btn_add=$("#detectCsm_formSearch #btn_add");//新增
        var btn_mod=$("#detectCsm_formSearch #btn_mod");//修改
        var btn_del=$("#detectCsm_formSearch #btn_del");//删除
        var btn_print=$("#detectCsm_formSearch #btn_print");//打印二维码
        //添加日期控件
        laydate.render({
            elem: '#yyjcrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yyjcrqend'
            ,theme: '#2381E9'
        });
        /*--------------------------------打印---------------------------*/
        btn_print.unbind("click").click(function(){
            var sel_row = $('#detectCsm_formSearch #detectCsm_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                htmlurl= btn_print.attr("tourl")+"?access_token="+$("#ac_tk").val()+"&csmid="+sel_row[0].csmid;
                openWindow = window.open(htmlurl);
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            DetectCsm_DealById(null,"add",btn_add.attr("tourl"));
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#detectCsm_formSearch #detectCsm_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].csmid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchDetectCsmResult();
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
        /*--------------------------------修改---------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#detectCsm_formSearch #detectCsm_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                DetectCsm_DealById(sel_row[0].csmid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchDetectCsmResult(true);
            });
        }
        /*--------------------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#detectCsm_formSearch #detectCsm_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                DetectCsm_DealById(sel_row[0].csmid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------------------------------------*/
        /**显示隐藏**/
        $("#detectCsm_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(DetectCsm_turnOff){
                $("#detectCsm_formSearch #searchMore").slideDown("low");
                DetectCsm_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#detectCsm_formSearch #searchMore").slideUp("low");
                DetectCsm_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}

$(function(){
    var oTable = new DetectCsm_TableInit();
    oTable.Init();
    var oButton = new DetectCsm_oButton();
    oButton.Init();
    jQuery('#detectCsm_formSearch .chosen-select').chosen({width: '100%'});
    $("#detectCsm_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
})