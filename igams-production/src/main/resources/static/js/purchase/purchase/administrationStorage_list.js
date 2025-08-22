var administrationStorage_turnOff=true;
var administrationStorage_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#administrationStorage_formSearch #administrationStorage_list').bootstrapTable({
            url: $("#administrationStorage_formSearch #urlPrefix").val()+'/purchase/purchase/pageGetListAdministrationStorage',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#administrationStorage_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"rkdh",					// 排序字段
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
            uniqueId: "xzrkid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'center',
                visible:true
            },{
                field: 'rkdh',
                title: '入库单号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'rklbmc',
                title: '入库类别',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'rkrq',
                title: '入库日期',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'rkrymc',
                title: '入库人员',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kwmc',
                title: '库位',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lrsj',
                title: '录入时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                administrationStorageById(row.xzrkid,'view',$("#administrationStorage_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#administrationStorage_formSearch #administrationStorage_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:false,
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
            sortLastName: "lrsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return administrationStorageSearchData(map);
    };
    return oTableInit;
};

function administrationStorageSearchData(map){
    var cxtj=$("#administrationStorage_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#administrationStorage_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["rkdh"]=cxnr;
    }else if(cxtj=="1"){
        map["rkrymc"]=cxnr;
    }else if(cxtj=="2"){
        map["hwmc"]=cxnr;
    }else if(cxtj=="3"){
        map["hwbz"]=cxnr;
    }else if(cxtj=="4"){
        map["entire"]=cxnr;
    }


    // 行政入库开始日期
    var xzrksjstart = jQuery('#administrationStorage_formSearch #xzrksjstart').val();
    map["xzrksjstart"] = xzrksjstart;

    // 行政入库结束日期
    var xzrksjend = jQuery('#administrationStorage_formSearch #xzrksjend').val();
    map["xzrksjend"] = xzrksjend;


    // 库位
    var kws = jQuery('#administrationStorage_formSearch #kw_id_tj').val();
    map["kws"] = kws.replace(/'/g, "");

    // 入库类别
    var rklbs = jQuery('#administrationStorage_formSearch #rklb_id_tj').val();
    map["rklbs"] = rklbs;

    return map;
}

function administrationStorageById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#administrationStorage_formSearch #urlPrefix").val()+tourl;
    var url= tourl + "?xzrkid=" +id;
    if(action =='view'){
        $.showDialog(url,'行政入库详细信息',viewAdministrationStorageConfig);
    }else if (action=='mod'){
        $.showDialog(url,'行政入库修改',modAdministrationStorageConfig);
    }
}
var viewAdministrationStorageConfig = {
    width		: "1400px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var modAdministrationStorageConfig={
    width		: "1500px",
    modalName	: "modPurchaseModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保存",
            className : "btn-success",
            callback : function() {
                var $this = this;
                var opts = $this["options"] || {};
                var json = [];
                if ($('#administrationQGmodForm #rklbdm').val() == "QG") {
                    var data = $('#administrationQGmodForm #tb_list').bootstrapTable('getData');//获取选择行数据
                    for (var i = 0; i < data.length; i++) {
                        var sz = {
                            "index": '',
                            "xzrkmxid": '',
                            "qgmxid": '',
                            "djh": '',
                            "hwmc": '',
                            "hwbz": '',
                            "hwjldw": '',
                            "krksl": '',
                            "rksl": '',
                            "yrksl": '',
                            "xzrksl": '',
                            "xzkcid": '',
                            "kcl": '',
                            "xzkcl": ''
                        };
                        sz.index = i;
                        sz.qgmxid = data[i].qgmxid;
                        sz.djh = data[i].djh;
                        sz.xzrkmxid = data[i].xzrkmxid;
                        if ($("#rksl_" + i).val() == null || $("#rksl_" + i).val() == '') {
                            $.confirm("第" + (i + 1) + "行入库数量不能为空!");
                            return false;
                        }
                        if ($("#rksl_" + i).val() == null || $("#rksl_" + i).val() == '') {
                            $.confirm("第" + (i + 1) + "行入库数量不能为空!");
                            return false;
                        }

                        if (parseInt(data[i].krksl)<(parseInt($("#rksl_" + i).val())-parseInt(data[i].xzrksl))){
                            $.confirm("第" + (i + 1) + "可入库数量不足，不允许修改!");
                            return false;
                        }
                        if (parseInt(data[i].xzkcl)< (parseInt(data[i].xzrksl)-parseInt($("#rksl_" + i).val()))) {
                            $.confirm("第" + (i + 1) + "行库存不足，不允许修改!");
                            return false;
                        } else {
                            sz.rksl = $("#rksl_" + i).val();
                        }
                        sz.hwmc = data[i].hwmc;
                        sz.hwbz = data[i].hwbz;
                        sz.hwjldw = data[i].hwjldw;
                        sz.krksl = data[i].krksl;
                        sz.yrksl = data[i].yrksl;
                        sz.xzrksl = data[i].xzrksl;
                        sz.xzkcid = data[i].xzkcid;
                        sz.kcl = data[i].kcl;
                        sz.xzkcl = data[i].xzkcl;
                        json.push(sz);
                    }

                $("#administrationQGmodForm #qgmx_json").val(JSON.stringify(json));

                $("#administrationQGmodForm input[name='access_token']").val($("#ac_tk").val());


                submitForm(opts["formName"] || "administrationQGmodForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            if (opts.offAtOnce) {
                                $.closeModal(opts.modalName);
                            }
                            administrationStoragesResult();
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
            }else {  var data = $('#administrationQTmodForm #tb_list').bootstrapTable('getData');//获取选择行数据
                    for (var i = 0; i < data.length; i++) {

                        var sz = {
                            "index": '',
                            "xzrkmxid": '',
                            "hwmc": '',
                            "hwbz": '',
                            "hwjldw": '',
                            "rksl": '',
                            "xzrksl": '',
                            "xzkcid": '',
                            "kcl": '',
                            "xzkcl": ''
                        };
                        sz.index = i;
                        sz.xzrkmxid = data[i].xzrkmxid;
                        if (parseInt(data[i].xzkcl)< (parseInt(data[i].xzrksl)-parseInt($("#rksl_" + i).val()))) {
                            $.confirm("第" + (i + 1) + "行库存不足，不允许修改!");
                            return false;
                        } else {
                            sz.rksl = $("#rksl_" + i).val();
                        }
                        sz.hwmc = data[i].hwmc;
                        sz.hwbz = data[i].hwbz;
                        sz.xzrksl = data[i].xzrksl;
                        sz.xzkcid = data[i].xzkcid;
                        sz.kcl = data[i].kcl;
                        sz.xzkcl = data[i].xzkcl;
                        json.push(sz);
                    }

                    $("#administrationQTmodForm #qgmx_json").val(JSON.stringify(json));

                    $("#administrationQTmodForm input[name='access_token']").val($("#ac_tk").val());
                    submitForm(opts["formName"] || "administrationQTmodForm", function (responseText, statusText) {
                        if (responseText["status"] == 'success') {
                            $.success(responseText["message"], function () {
                                if (opts.offAtOnce) {
                                    $.closeModal(opts.modalName);
                                }
                                administrationStoragesResult();
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
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var administrationStorage_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#administrationStorage_formSearch #btn_view");
        var btn_query = $("#administrationStorage_formSearch #btn_query");
        var btn_del = $("#administrationStorage_formSearch #btn_del");
        var btn_mod = $("#administrationStorage_formSearch #btn_mod");
        //添加日期控件
        laydate.render({
            elem: '#xzrksjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#xzrksjend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                administrationStoragesResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#administrationStorage_formSearch #administrationStorage_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                administrationStorageById(sel_row[0].xzrkid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#administrationStorage_formSearch #administrationStorage_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                administrationStorageById(sel_row[0].xzrkid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#administrationStorage_formSearch #administrationStorage_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].xzrkid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#administrationStorage_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    administrationStoragesResult(true);
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
        /**显示隐藏**/
        $("#administrationStorage_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(administrationStorage_turnOff){
                $("#administrationStorage_formSearch #searchMore").slideDown("low");
                administrationStorage_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#administrationStorage_formSearch #searchMore").slideUp("low");
                administrationStorage_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function administrationStoragesResult(isTurnBack){
    //关闭高级搜索条件
    $("#administrationStorage_formSearch #searchMore").slideUp("low");
    arrivalGoods_turnOff=true;
    $("#administrationStorage_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#administrationStorage_formSearch #administrationStorage_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#administrationStorage_formSearch #administrationStorage_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new administrationStorage_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new administrationStorage_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#administrationStorage_formSearch .chosen-select').chosen({width: '100%'});
});