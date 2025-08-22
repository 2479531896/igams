
var scheduling_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#scheduling_FormSearch #tb_list').bootstrapTable({
            url: $("#scheduling_FormSearch #urlPrefix").val()+'/attendance/attendance/pageGetListScheduling',
            method: 'get',                      //请求方式（*）
            toolbar: '#scheduling_FormSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"bcgl.sbdksj",					//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
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
            uniqueId: "bcglid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width:'1%'
            }, {
                field: 'bcglid',
                title: '班次管理ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'bcmc',
                title: '班次名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sbdksj',
                title: '班次开始时间',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'xbdksj',
                title: '班次结束时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'btmc',
                title: '补贴名称',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'dzje',
                title: '递增金额',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'bzsc',
                title: '标准时长',
                align: 'left',
                width: '4%',
                visible: true
            },{
                field: 'bzsj',
                title: '标准时间',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'bzje',
                title: '标准金额',
                align: 'left',
                width: '3%',
                visible: true
            },{
                field: 'dzjg',
                title: '递增间隔',
                align: 'left',
                width: '3%',
                visible: true
            },{
                field: 'dzkssj',
                title: '递增开始时间',
                align: 'left',
                width: '3%',
                visible: true
            },{
                field: 'fdje',
                title: '封顶金额',
                align: 'left',
                width: '3%',
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'xgrymc',
                title: '修改人员',
                align: 'left',
                width: '6%',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                schedulingDealById(row.bcglid, 'view',$("#scheduling_FormSearch #btn_view").attr("tourl"));
            },
        });
        $("#scheduling_FormSearch #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "bcgl.sbdksj", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxbt = $("#scheduling_FormSearch #cxbt").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#scheduling_FormSearch #cxnr').val());
        if (cxbt == "0") {
            map["bcmc"] = cxnr;
        }else if (cxbt == "1"){
            map["btmc"] = cxnr;
        }
        return map;
    };
    return oTableInit;
};



var viewSchedulingConfig = {
    width		: "1000px",
    modalName	: "viewSchedulingModal",
    formName	: "viewScheduling_ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var subsidysetSchedulingConfig = {
    width		: "1400px",
    modalName	: "subsidysetModal",
    formName	: "subsidyset_FormSearch",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#subsidyset_FormSearch").valid()){
                    $.alert("请填写完整信息！")
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#subsidyset_FormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if (sel_row.length!=1){
                    $.error("请选中一行");
                    return false;
                }else {
                    $('#subsidyset_FormSearch #btglid').val(sel_row[0].btglid);
                }
                $("#subsidyset_FormSearch input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"subsidyset_FormSearch",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchSchedulingResult();
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
//按钮动作函数
function schedulingDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?bcglid=" +id;
        $.showDialog(url,'查看',viewSchedulingConfig);
    }else if(action =='subsidyset'){
        var url=tourl+"?ids="+id;
        $.showDialog(url,'补贴设置',subsidysetSchedulingConfig);
    }
}


var scheduling_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_add = $("#scheduling_FormSearch #btn_add");
        var btn_mod = $("#scheduling_FormSearch #btn_mod");
        var btn_view = $("#scheduling_FormSearch #btn_view");
        var btn_query = $("#scheduling_FormSearch #btn_query");
        var btn_synchronous = $("#scheduling_FormSearch #btn_synchronous");
        var btn_subsidyset = $("#scheduling_FormSearch #btn_subsidyset");
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchSchedulingResult(true);
            });
        }
        btn_add.unbind("click").click(function(){
            schedulingDealById(null,"add",btn_add.attr("tourl"));
        });
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#scheduling_FormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                schedulingDealById(sel_row[0].bcglid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_view.unbind("click").click(function(){
            var sel_row = $('#scheduling_FormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                schedulingDealById(sel_row[0].bcglid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_subsidyset.unbind("click").click(function(){
            var sel_row = $('#scheduling_FormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            var ids="";
            if(sel_row.length!=0){
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].bcglid;
                }
                ids = ids.substr(1);
                schedulingDealById(ids,"subsidyset",btn_subsidyset.attr("tourl"));
            }else{
                $.error("请至少选中一行");
            }
        });
        btn_synchronous.unbind("click").click(function(){
            $.confirm('您确定要更新班次吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= $('#scheduling_FormSearch #urlPrefix').val() +btn_synchronous.attr("tourl");
                    jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchSchedulingResult();
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
    };

    return oInit;
};

function searchSchedulingResult(isTurnBack){
    if(isTurnBack){
        $('#scheduling_FormSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#scheduling_FormSearch #tb_list').bootstrapTable('refresh');
    }
}

$(function(){

    //1.初始化Table
    var oTable = new scheduling_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new scheduling_ButtonInit();
    oButtonInit.Init();

    //所有下拉框添加choose样式
    jQuery('#scheduling_FormSearch .chosen-select').chosen({width: '100%'});
});
