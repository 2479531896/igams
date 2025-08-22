var allocateAdministration_turnOff=true;
var allocateAdministrationList=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#allocateAdministration_formSearch #allocateAdministration_list").bootstrapTable({
            url: $("#allocateAdministration_formSearch #urlPrefix").val()+'/allocate/allocateAdministration/pageGetListAllocateAdministration',
            method: 'get',                      // 请求方式（*）
            toolbar: '#allocateAdministration_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "dbsj",				// 排序字段
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
            uniqueId: "xzdbid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                field: 'xzdbid',
                title: '行政调拨id',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'hwmc',
                title: '货物名称',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'hwbz',
                title: '货物标准',
                width: '2%',
                align: 'left',
                visible: true
            },{
                field: 'dckwmc',
                title: '调出库位',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'drkwmc',
                title: '调入库位',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'dbsl',
                title: '调拨数量',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'dbsj',
                title: '调拨日期',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'dbrymc',
                title: '调拨人员',
                width: '4%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                allocateAdministrationById(row.xzdbid,'view',$("#allocateAdministration_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#allocateAdministration_formSearch #allocateAdministration_list").colResizable({
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
            sortLastName: "dbsl", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return allocateAdministrationSearchData(map);
    };
    return oTableInit;
}


function allocateAdministrationSearchData(map){
    var allocateAdministration_select=$("#allocateAdministration_formSearch #allocateAdministration_select").val();
    var allocateAdministration_input=$.trim(jQuery('#allocateAdministration_formSearch #allocateAdministration_input').val());
    if(allocateAdministration_select=="0"){
        map["hwmc"]=allocateAdministration_input;
    }else if(allocateAdministration_select=="1"){
        map["hwbz"]=allocateAdministration_input;
    }else if(allocateAdministration_select=="2"){
        map["dbry"]=allocateAdministration_input;
    }
    // 调拨开始日期
    var dbsjstart = jQuery('#allocateAdministration_formSearch #dbsjstart').val();
    map["dbsjstart"] = dbsjstart;

    // 调拨结束日期
    var dbsjend = jQuery('#allocateAdministration_formSearch #dbsjend').val();
    map["dbsjend"] = dbsjend;
    //调出库位
    var dckws=jQuery('#allocateAdministration_formSearch #dckw_id_tj').val();
    map["dckws"] = dckws.replace(/'/g, "");
    //调入库位
    var drkws=jQuery('#allocateAdministration_formSearch #drkw_id_tj').val();
    map["drkws"] = drkws.replace(/'/g, "");
    return map;
}
function allocateAdministrationById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#allocateAdministration_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?xzdbid=" +id;
        $.showDialog(url,'调拨信息',viewallocateAdministrationConfig);
    }else if(action=='allocation'){
        var url=tourl;
        $.showDialog(url,'调拨',allocationallocateAdministrationConfig);
    }
}

function allocateAdministration_queryByWlbm(wlid){
    var url=$("#allocateAdministration_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',WlConfig);
}
var WlConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function preSubmitRecheck(){
    return true;
}
var allocationallocateAdministrationConfig = {
    width		: "1600px",
    modalName    : "allocationModal",
    formName    : "allocationForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if (!$("#allocationForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var dbmx_json=JSON.parse(t_map.rows[i].dbmx_json);
                        if(dbmx_json!=null && dbmx_json!=""){
                            for(var j=0;j<dbmx_json.length;j++){
                                var sz = {"dchwid":'',"dchw":'',"drhw":'',"dbsl":''};
                                sz.dchwid = dbmx_json[j].hwid;
                                sz.dchw = dbmx_json[j].dckw;
                                sz.drhw = dbmx_json[j].drkw;
                                sz.dbsl = dbmx_json[j].dbsl;
                                if (sz.dbsl != "0"){
                                    json.push(sz);
                                }
                            }
                        }
                    }
                    $("#allocationForm #dbmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("调拨明细不能为空！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"] || {};

                $("#allocationForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "allocationForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            allocateAdministrationResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");

                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var viewallocateAdministrationConfig = {
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var allocateAdministration_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#allocateAdministration_formSearch #btn_query");
        var btn_view = $("#allocateAdministration_formSearch #btn_view");
        var btn_allocation= $("#allocateAdministration_formSearch #btn_allocation");
        //添加日期控件
        laydate.render({
            elem: '#dbsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#dbsjend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                allocateAdministrationResult(true);
            });
        }
        /* ---------------------------查看调拨信息-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#allocateAdministration_formSearch #allocateAdministration_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                allocateAdministrationById(sel_row[0].xzdbid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------调拨------------------------------------------*/
        btn_allocation.unbind("click").click(function(){
            allocateAdministrationById(null,"allocation",btn_allocation.attr("tourl"));
        });
    };
    /**显示隐藏**/
    $("#allocateAdministration_formSearch #sl_searchMore").on("click", function(ev){
        var ev=ev||event;
        if(allocateAdministration_turnOff){
            $("#allocateAdministration_formSearch #searchMore").slideDown("low");
            allocateAdministration_turnOff=false;
            this.innerHTML="基本筛选";
        }else{
            $("#allocateAdministration_formSearch #searchMore").slideUp("low");
            allocateAdministration_turnOff=true;
            this.innerHTML="高级筛选";
        }
        ev.cancelBubble=true;
    });
    return oInit;
};

var allocateAdministration_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var scbj = $("#allocateAdministration_formSearch a[id^='scbj_id_']");
        $.each(scbj, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code === '0'){
                addTj('scbj',code,'allocateAdministration_formSearch');
            }
        });
    }
    return oInit;
}

function allocateAdministrationResult(isTurnBack){
    if(isTurnBack){
        $('#allocateAdministration_formSearch #allocateAdministration_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#allocateAdministration_formSearch #allocateAdministration_list').bootstrapTable('refresh');
    }
}


$(function(){
    //0.界面初始化
    var oInit = new allocateAdministration_PageInit();
    oInit.Init();

    // 1.初始化Table
    var oTable = new allocateAdministrationList();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new allocateAdministration_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#allocateAdministration_formSearch .chosen-select').chosen({width: '100%'});

    $("#allocateAdministration_ButtonInit [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});