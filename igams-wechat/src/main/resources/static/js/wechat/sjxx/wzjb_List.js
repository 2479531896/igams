
var Wzjb_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#wzjb_formSearch #wzjb_List').bootstrapTable({
            url: '/wzjb/wzjb/pageGetListWzjb',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#wzjb_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "wzid",				//排序字段
            sortOrder: "DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
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
            uniqueId: "wzid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true,
                width: '4%'
            },{
                field: 'wzzwm',
                title: '物种中文名',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'wzywm',
                title: '物种英文名',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'fldj',
                title: '分类等级',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'sflj',
                title: '是否拦截',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'crbjb',
                title: '传染病级别',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'wzid',
                title: '物种编号',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'wzfl',
                title: '物种分类',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'wzlx',
                title: '物种类型',
                width: '15%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                WzjbDealById(row.wzid,'view',$("#wzjb_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#wzjb_formSearch #wzjb_List").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
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
            sortLastName: "wzid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getWzjbSearchData(map);
    };
    return oTableInit;
}

function getWzjbSearchData(map){
    var cxtj=$("#wzjb_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#wzjb_formSearch #cxnr').val());
    var crbjb=$.trim(jQuery('#wzjb_formSearch #crbjb').val());
    var sflj=$.trim(jQuery('#wzjb_formSearch #sflj').val());
    if(cxtj=="0"){
        map["wzzwm"]=cxnr
    }else if(cxtj=="1"){
        map["wzywm"]=cxnr
    }else if(cxtj=="2"){
        map["wzid"]=cxnr
    }else if(cxtj=="3"){
        map["wzfl"]=cxnr
    }

    if(crbjb=="0"){
        map["crbjb"]="甲级"
    }else if(crbjb=="1"){
        map["crbjb"]="乙级"
    }else if(crbjb=="2"){
        map["crbjb"]="丙级"
    }

    if(sflj=="0"){
        map["sflj"]="0"
    }else if(sflj=="1"){
        map["sflj"]="1"
    }
    return map;
}

// 按钮动作函数
function WzjbDealById(wzid,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl+"?wzid="+wzid;
        $.showDialog(url,'查看',viewWzjbConfig);
    }else if(action=="mod"){
        var url=tourl+"?wzid="+wzid;
        $.showDialog(url,'修改',addWzjbConfig);
    }
}

var Wzjb_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#wzjb_formSearch #btn_view");
        var btn_mod = $("#wzjb_formSearch #btn_mod");
        var btn_query =$("#wzjb_formSearch #btn_query");//模糊查询

        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchWzjbResult(true);
            });
        };
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#wzjb_formSearch #wzjb_List').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                WzjbDealById(sel_row[0].wzid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------修改------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#wzjb_formSearch #wzjb_List').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                WzjbDealById(sel_row[0].wzid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
    };
    return oInit;
};

var addWzjbConfig = {
    width		: "800px",
    modalName	:"addWzjbModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#wzjb_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#wzjb_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"wzjb_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchWzjbResult();
                        });
                    }else if(responseText["status"] == "fail"){
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


var viewWzjbConfig={
    width		: "800px",
    modalName	:"viewWzjbModel",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
function searchWzjbResult(isTurnBack){
    if(isTurnBack){
        $('#wzjb_formSearch #wzjb_List').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#wzjb_formSearch #wzjb_List').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Wzjb_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Wzjb_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#wzjb_formSearch .chosen-select').chosen({width: '100%'});
    $("#wzjb_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});

