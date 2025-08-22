var redpacketsetting_turnOff=true;

var redpacketsetting_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#redpacketsetting_formSearch #redpacketsetting_list").bootstrapTable({
            url: $("#redpacketsetting_formSearch #urlPrefix").val()+'/redpacket/redpacket/pageGetListRedpacketsetting',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#redpacketsetting_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "hbsz.fssj",				//排序字段
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
            uniqueId: "hbszid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '0.5%',
                align: 'center',

            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'hbszid',
                title: '红包设置id',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false,
            },{
                field: 'hbmc',
                title: '红包名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'tzqmc',
                title: '通知群',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fssj',
                title: '发送时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'tzxx',
                title: '通知消息',
                width: '20%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                    redpacketsettingDealById(row.hbszid,'view',$("#redpacketsetting_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#redpacketsetting_formSearch #redpacketsetting_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "hbsz.hbmc", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return redpacketsettingSearchData(map);
    }
    return oTableInit
}

function redpacketsettingSearchData(map) {
    var cxtj = $("#redpacketsetting_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#redpacketsetting_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == "1") {
        map["hbmc"] = cxnr
    }else if (cxtj == "2") {
        map["tzqmc"] = cxnr
    }
    return map;
}
function redpacketsettingDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#redpacketsetting_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?hbszid=" +id;
        $.showDialog(url,'查看',viewredpacketsettingConfig);
    }else if (action=='add'){
        var url=tourl;
        $.showDialog(url,'新增',addRedpacketsettingConfig);
    }else if (action=='mod'){
        var url=tourl+"?hbszid="+id;
        $.showDialog(url,'修改',modRedpacketsettingConfig);
    }else if (action=='copy'){
        var url=tourl+"?hbszid="+id;
        $.showDialog(url,'复制',addRedpacketsettingConfig);
    }
}

/**
 * 查看页面模态框
 */
var viewredpacketsettingConfig={
    width		: "1000px",
    modalName	:"viewredpacketsettingModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
var addRedpacketsettingConfig = {
    width		: "1200px",
    modalName	: "addRedpacketsettingModal",
    formName	: "editRedpacketsettingForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};

                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        let sz = {"je":'',"sl":'',"zdf":'',"zgf":'',"sftz":'',"tznr":'',"bs":''};
                        sz.je = t_map.rows[i].je;
                        sz.sl = t_map.rows[i].sl;
                        sz.zdf = t_map.rows[i].zdf;
                        sz.zgf = t_map.rows[i].zgf;
                        sz.sftz = t_map.rows[i].sftz;
                        sz.tznr = t_map.rows[i].tznr;
                        sz.bs = t_map.rows[i].bs;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(json));
                $("#editRedpacketsettingForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editRedpacketsettingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchredpacketsettingResult();
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
var modRedpacketsettingConfig = {
    width		: "1200px",
    modalName	: "modRedpacketsettingModal",
    formName	: "editRedpacketsettingForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        let sz = {"hbszid":'',"hbmxid":'',"je":'',"sl":'',"zdf":'',"zgf":'',"sftz":'',"tznr":'',"bs":''};
                        sz.hbszid = t_map.rows[i].hbszid;
                        sz.hbmxid = t_map.rows[i].hbmxid;
                        sz.je = t_map.rows[i].je;
                        sz.sl = t_map.rows[i].sl;
                        sz.zdf = t_map.rows[i].zdf;
                        sz.zgf = t_map.rows[i].zgf;
                        sz.sftz = t_map.rows[i].sftz;
                        sz.tznr = t_map.rows[i].tznr;
                        sz.bs = t_map.rows[i].bs;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(json));
                $("#editRedpacketsettingForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editRedpacketsettingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchredpacketsettingResult();
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
var redpacketsetting_ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#redpacketsetting_formSearch #btn_query");
        var btn_view = $("#redpacketsetting_formSearch #btn_view");
        var btn_add = $("#redpacketsetting_formSearch #btn_add");
        var btn_copy = $("#redpacketsetting_formSearch #btn_copy");
        var btn_del = $("#redpacketsetting_formSearch #btn_del");
        var btn_mod = $("#redpacketsetting_formSearch #btn_mod");
        /* ---------------------------模糊查询-----------------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchredpacketsettingResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#redpacketsetting_formSearch #redpacketsetting_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                redpacketsettingDealById(sel_row[0].hbszid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            redpacketsettingDealById(null,"add",btn_add.attr("tourl"));
        });
        /*---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#redpacketsetting_formSearch #redpacketsetting_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                redpacketsettingDealById(sel_row[0].hbszid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------复制-----------------------------------*/
        btn_copy.unbind("click").click(function(){
            var sel_row = $('#redpacketsetting_formSearch #redpacketsetting_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                redpacketsettingDealById(sel_row[0].hbszid,"copy",btn_copy.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除领料信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#redpacketsetting_formSearch #redpacketsetting_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                var rwids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].hbszid;
                    rwids= rwids + ","+ sel_row[i].rwid;
                }
                ids=ids.substr(1);
                rwids=rwids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#redpacketsetting_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{'ids':ids,'rwids':rwids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchredpacketsettingResult();
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
    };
    return oInit;
};


function searchredpacketsettingResult(isTurnBack) {
    if (isTurnBack) {
        $('#redpacketsetting_formSearch #redpacketsetting_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#redpacketsetting_formSearch #redpacketsetting_list').bootstrapTable('refresh');
    }
}
$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new redpacketsetting_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new redpacketsetting_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#redpacketsetting_formSearch .chosen-select').chosen({width: '100%'});
});
