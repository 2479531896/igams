var hbpHzxx_turnOff=true;
var Hbp_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#hbp_formSearch #hbp_list").bootstrapTable({
            url: '/hbp/hbp/listHbpHzxx',
            method: 'get',                      // 请求方式（*）
            toolbar: '#hbp_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "hzxx.ricurq",				// 排序字段
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
            uniqueId: "hbphzxxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            },{
                field: 'hbphzxxid',
                title: '患者ID',
                width: '50%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '患者姓名',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'zyh',
                title: '住院号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ricurq',
                title: '入ICU日期',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xb',
                title: '性别',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true,
                formatter:hbpxb_formatter
            },{
                field: 'nl',
                title: '年龄(岁)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true,
            },{
                field: 'sg',
                title: '身高(cm)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true,
            },{
                field: 'tz',
                title: '体重(kg)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Hbp_DealById(row.hbphzxxid,'view',$("#hbp_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#hbp_formSearch #hbp_list").colResizable({
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
            sortLastName: "hzxx.zyh", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getHbpSearchData(map);
    };
    return oTableInit;
}
function hbpxb_formatter(value,row,index){
    if(row.xb=='0'){
        return '女';
    }else if(row.xb=='1'){
        return '男';
    }
}
function getHbpSearchData(map){
    var hbp_select=$("#hbp_formSearch #hbp_select").val();
    var hbp_input=$.trim(jQuery('#hbp_formSearch #hbp_input').val());
    if(hbp_select=="0"){
        map["xm"]=hbp_input
    }else if(hbp_select=="1"){
        map["zyh"]=hbp_input
    }
    var ricurqstart = jQuery('#hbp_formSearch #ricurqstart').val();
    map["ricurqstart"] = ricurqstart;
    var ricurqend = jQuery('#hbp_formSearch #ricurqend').val();
    map["ricurqend"] = ricurqend;

    var rzrqstart = jQuery('#hbp_formSearch #rzrqstart').val();
    map["rzrqstart"] = rzrqstart;
    var rzrqend = jQuery('#hbp_formSearch #rzrqend').val();
    map["rzrqend"] = rzrqend;

    return map;
}

laydate.render({
    elem: '#hbp_formSearch #ricurqstart'
    ,theme: '#2381E9'
});
laydate.render({
    elem: '#hbp_formSearch #ricurqend'
    ,theme: '#2381E9'
});
laydate.render({
    elem: '#hbp_formSearch #rzrqstart'
    ,theme: '#2381E9'
});
laydate.render({
    elem: '#hbp_formSearch #rzrqend'
    ,theme: '#2381E9'
});

function searchHbpResult(isTurnBack){
    if(isTurnBack){
        $('#hbp_formSearch #hbp_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#hbp_formSearch #hbp_list').bootstrapTable('refresh');
    }
}

function Hbp_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = tourl;
    if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增',addHbpConfig);
    }else if(action=="view"){
        var url=tourl+"?hbphzxxid="+id
        $.showDialog(url,'查看',viewHbpConfig);
    }else if(action=="mod"){
        var url=tourl+"?hbphzxxid="+id
        $.showDialog(url,'修改',modHbpConfig);
    }else if(action=="del"){
        var url=tourl+"?hbphzxxid="+id
        $.showDialog(url,'删除',delHbpConfig);
    }
}

var addHbpConfig = {
    width		: "1200px",
    modalName	:"addHbpModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#hbpedit_ajaxForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#hbpedit_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"hbpedit_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchHbpResult();
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

var modHbpConfig = {
    width		: "1200px",
    modalName	:"modHbpModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#hbpedit_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#hbpedit_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"hbpedit_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchHbpResult();
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

var viewHbpConfig = {
    width		: "1200px",
    modalName	:"viewHbpModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var Hbp_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#hbp_formSearch #btn_query");
        var btn_add = $("#hbp_formSearch #btn_add");
        var btn_mod = $("#hbp_formSearch #btn_mod");
        var btn_view = $("#hbp_formSearch #btn_view");
        var btn_del = $("#hbp_formSearch #btn_del");
        var btn_selectexport = $("#hbp_formSearch #btn_selectexport");//选中导出
        /**显示隐藏**/
        $("#hbp_formSearch #sl_searchMore").on("click", function(ev){
            console.log("121");
            var ev=ev||event;
            if(hbpHzxx_turnOff){
                $("#hbp_formSearch #searchMore").slideDown("low");
                hbpHzxx_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#hbp_formSearch #searchMore").slideUp("low");
                hbpHzxx_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchHbpResult(true);
            });
        }
        /*-----------------------新增------------------------------------*/
        btn_add.unbind("click").click(function(){
            Hbp_DealById(null,"add",btn_add.attr("tourl"));
        });
        /*-----------------------修改------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#hbp_formSearch #hbp_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Hbp_DealById(sel_row[0].hbphzxxid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#hbp_formSearch #hbp_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Hbp_DealById(sel_row[0].hbphzxxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------删除------------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#hbp_formSearch #hbp_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
                    ids= ids + ","+ sel_row[i].hbphzxxid;
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
                                        searchHbpResult();
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
        //---------------------------------选中导出---------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#hbp_formSearch #hbp_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].hbphzxxid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=HBP_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
    }
    return oButtonInit;
}


$(function(){
    var oTable = new Hbp_TableInit();
    oTable.Init();

    var oButton = new Hbp_oButton();
    oButton.Init();

    jQuery('#hbp_formSearch .chosen-select').chosen({width: '100%'});
})