var produceBom_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#produceBom_formSearch #produceBom_tb_list').bootstrapTable({
            url: $('#produceBom_formSearch #urlPrefix').val() + '/progress/produce/pageGetListProduceBom',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#produceBom_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "bomgl.lrsj",				//排序字段
            sortOrder: "desc",                   //排序方式
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
            uniqueId: "bomgl.bomid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'bomid',
                title: 'BOMid',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'bomlbmc',
                title: 'BOM类别',
                titleTooltip:'BOM类别',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'mjwlbm',
                title: '母件物料编码',
                titleTooltip:'母件物料编码',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'mjwlmc',
                title: '母件物料名称',
                titleTooltip:'母件物料名称',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bbh',
                title: '版本号',
                titleTooltip:'版本号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bbrq',
                title: '版本日期',
                titleTooltip:'版本日期',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bbsm',
                title: '版本说明',
                titleTooltip:'版本说明',
                width: '25%',
                align: 'left',
                sortable: true,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                produceBomDealById(row.bomid, 'view',$("#produceBom_formSearch #btn_view").attr("tourl"));
            },
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "bomgl.xgsj", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return getproduceBomSearchData(map);
    };
    return oTableInit;
};

function searchproduceBomResult(isTurnBack){
    //关闭高级搜索条件
    $("#produceBom_formSearch #searchMore").slideUp("low");
    produceBom_turnOff=true;
    $("#produceBom_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#produceBom_formSearch #produceBom_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#produceBom_formSearch #produceBom_tb_list').bootstrapTable('refresh');
    }
}


function getproduceBomSearchData(map){
    var cxtj = $("#produceBom_formSearch #cxtj").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#produceBom_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr;
    }else if (cxtj == "1") {
        map["mjwlbm"] = cxnr;
    }else if (cxtj == "2") {
        map["zjwlbm"] = cxnr;
    }else if (cxtj == "3") {
        map["mjwlmc"] = cxnr;
    }else if (cxtj == "4") {
        map["zjwlmc"] = cxnr;
    }else if (cxtj == "5") {
        map["gg"] = cxnr;
    }else if (cxtj == "6") {
        map["bbh"] = cxnr;
    }else if (cxtj == "7") {
        map["bbsm"] = cxnr;
    }
    // 需求开始日期
    var bbrqstart = jQuery('#produceBom_formSearch #bbrqstart').val();
    map["bbrqstart"] = bbrqstart;
    // 需求结束日期
    var bbrqend = jQuery('#produceBom_formSearch #bbrqend').val();
    map["bbrqend"] = bbrqend;

    // 高级筛选
    var bomlbs = jQuery('#produceBom_formSearch #bomlb_id_tj').val();
    map["bomlbs"] = bomlbs.replace(/'/g, "");
    return map;
}
//按钮动作函数
function produceBomDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $('#produceBom_formSearch #urlPrefix').val() + tourl;
    if(action =='view'){
        var url= tourl + "?bomid=" +id;
        $.showDialog(url,'查看',viewProduceBomConfig);
    }else if (action =='add'){
        $.showDialog(tourl,'新增',addProduceBomConfig);
    } else if (action =='mod'){
        var url= tourl + "?bomid=" +id;
        $.showDialog(url,'修改',modProduceBomConfig);
    }
}
var viewProduceBomConfig = {
    width		: "1400px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addProduceBomConfig = {
    width		: "1600px",
    modalName	:"addProduceBomModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#produceBomEditForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"zjwlid":'',"bzyl":''};
                        sz.zjwlid = t_map.rows[i].zjwlid;
                        sz.bzyl = t_map.rows[i].bzyl;
                        json.push(sz);
                    }
                    $("#produceBomEditForm #bommx_json").val(JSON.stringify(json));
                }else{
                    $.alert("明细信息不能为空！");
                    return false;
                }
                $("#produceBomEditForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"produceBomEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            searchproduceBomResult();
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
            className : "btn-default"
        }
    }
};
var modProduceBomConfig = {
    width		: "1600px",
    modalName	:"modProduceBomModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#produceBomEditForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"zjwlid":'',"bzyl":'',"bommxid":''};
                        sz.zjwlid = t_map.rows[i].zjwlid;
                        sz.bzyl = t_map.rows[i].bzyl;
                        sz.bommxid = t_map.rows[i].bommxid;
                        json.push(sz);
                    }
                    $("#produceBomEditForm #bommx_json").val(JSON.stringify(json));
                }else{
                    $.alert("明细信息不能为空！");
                    return false;
                }
                $("#produceBomEditForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"produceBomEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            searchproduceBomResult();
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
            className : "btn-default"
        }
    }
};

var produceBom_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#produceBom_formSearch #btn_view");
        var btn_query = $("#produceBom_formSearch #btn_query");
        var btn_add = $("#produceBom_formSearch #btn_add");
        var btn_mod=$("#produceBom_formSearch #btn_mod");// 修改
        var btn_del = $("#produceBom_formSearch #btn_del");// 删除
        //添加日期控件
        laydate.render({
            elem: '#bbrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bbrqend'
            ,theme: '#2381E9'
        });

        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchproduceBomResult(true);
            });
        }
        btn_view.unbind("click").click(function(){
            var sel_row = $('#produceBom_formSearch #produceBom_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                produceBomDealById(sel_row[0].bomid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------新增------------------*/
        btn_add.unbind("click").click(function(){
            produceBomDealById(null,"add",btn_add.attr("tourl"));
        });

        /* --------------------------- 修改 -----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#produceBom_formSearch #produceBom_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                produceBomDealById(sel_row[0].bomid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除Bom信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#produceBom_formSearch #produceBom_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].bomid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#produceBom_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchproduceBomResult();
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
        $("#produceBom_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(produceBom_turnOff){
                $("#produceBom_formSearch #searchMore").slideDown("low");
                produceBom_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#produceBom_formSearch #searchMore").slideUp("low");
                produceBom_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };

    return oInit;
};
var produceBom_turnOff=true;

$(function(){
    //1.初始化Table
    var oTable = new produceBom_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new produceBom_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#produceBom_formSearch .chosen-select').chosen({width: '100%'});

});
