var equipmentAcceptance_turnOff=true;

var equipmentAcceptance_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptance_formSearch #equipmentAcceptance_list").bootstrapTable({
            url: $("#equipmentAcceptance_formSearch #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pageGetListEquipmentAcceptance',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptance_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sbys.sbmc",				//排序字段
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
            uniqueId: "sbysid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'sbysid',
                title: '设备验收id',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sbccbh',
                title: '设备编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gdzcbh',
                title: '固定资产编号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sbmc',
                title: '设备名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ggxh',
                title: '规格型号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xlh',
                title: '序列号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sccj',
                title: '生产厂家',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sydd',
                title: '使用地点',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xsyrmc',
                title: '现使用人',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xsybmmc',
                title: '现使用部门',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lysj',
                title: '领用时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'ysbbh',
                title: '原设备编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'ygdzcbh',
                title: '原固定资产编号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sblxmc',
                title: '设备类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'gzsj',
                title: '购置时间',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'glrymc',
                title: '管理人员',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'dzwz',
                title: '定置位置',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xgry',
                title: '修改人员',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xgsj',
                title: '修改时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sbzt',
                title: '设备状态',
                width: '6%',
                align: 'left',
                formatter:sbztFormat,
                sortable: true,
                visible: true
            },{
                field: 'pkzt',
                title: '盘库确认',
                width: '4%',
                align: 'left',
                formatter:pkztFormat,
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                equipmentAcceptanceDealById(row.sbysid,'view',$("#equipmentAcceptance_formSearch #btn_view").attr("tourl"),'');
            },
        });
        $("#equipmentAcceptance_formSearch #equipmentAcceptance_list").colResizable({
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
            sortLastName: "sbys.gdzcbh", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return equipmentAcceptanceSearchData(map);
    }
    return oTableInit
}

function sbztFormat(value,row,index){
    if(row.sbzt=='0')
        return "<span style='color:black;'>"+"闲置"+"</span>";
    else if (row.sbzt=='1')
    return "<span style='color:#337ab7;'>"+row.xsyrmc+"使用中"+"</span>";
    else if (row.sbzt=='2')
        return "<span style='color:red;'>"+"报废"+"</span>";
    else if (row.sbzt=='3')
        return "<span style='color:grey;'>"+"售出"+"</span>";
    else if (row.sbzt=='4')
        return "<span style='color:#f0ad4e;'>"+"移交确认中"+"</span>";
    else if (row.sbzt=='5')
    return "<span style='color:#ba8448;'>"+"移交确认不通过"+"</span>";

else
        return "";
}
function pkztFormat(value,row,index){
    if(row.pkzt=='0')
    return "<span style='color:red;'>"+"不通过"+"</span>";
else if (row.pkzt=='1')
    return "<span style='color:rgb(156, 217, 183);'>"+"通过"+"</span>";
    else if (row.pkzt=='3')
    return "<span style='color:#f0ad4e;'>"+"确认中"+"</span>";

else
        return "";
}
function equipmentAcceptanceSearchData(map){
    var cxtj=$("#equipmentAcceptance_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#equipmentAcceptance_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["sbysdh"]=cxnr
    }else if(cxtj=="2"){
        map["gdzcbh"]=cxnr
    }else if(cxtj=="3"){
        map["sbmc"]=cxnr
    }else if(cxtj=="4"){
        map["ggxh"]=cxnr
    }else if(cxtj=="5"){
        map["gysmc"]=cxnr
    }else if(cxtj=="6"){
        map["sccj"]=cxnr
    }else if(cxtj=="7"){
        map["dzwz"]=cxnr
    }else if(cxtj=="8"){
        map["xsyrmc"]=cxnr
    }else if(cxtj=="9"){
        map["xlh"]=cxnr
    }

    // 设备状态多
    var sbzts = jQuery('#equipmentAcceptance_formSearch #sbzt_id_tj').val();
    map["sbzts"] = sbzts;
    // 盘库状态多
    var pkzts = jQuery('#equipmentAcceptance_formSearch #pkzt_id_tj').val();
    map["pkzts"] = pkzts;
    // 设备类型多
    var sblxs = jQuery('#equipmentAcceptance_formSearch #sblx_id_tj').val();
    map["sblxs"] = sblxs;
    // 现用部门多
    var xsybms = jQuery('#equipmentAcceptance_formSearch #xsybm_id_tj').val();
    map["xsybms"] = xsybms;
    // 时间
    var lysjstart = jQuery('#equipmentAcceptance_formSearch #lysjstart').val();
    map["lysjstart"] = lysjstart;
    // 时间
    var lysjend = jQuery('#equipmentAcceptance_formSearch #lysjend').val();
    map["lysjend"] = lysjend;
    return map;
}
//提供给导出用的回调函数
function equipmentSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="sbys.sbzt";
    map["sortLastOrder"]="desc";
    map["sortName"]="sbys.lrsj";
    map["sortOrder"]="desc";
    return equipmentAcceptanceSearchData(map);
}
var equipmentAcceptance_ButtonInit= function () {
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        var btn_query = $("#equipmentAcceptance_formSearch #btn_query");
        var btn_add = $("#equipmentAcceptance_formSearch #btn_add");
        var btn_mod = $("#equipmentAcceptance_formSearch #btn_mod");
        var btn_view = $("#equipmentAcceptance_formSearch #btn_view");
        var btn_turnover = $("#equipmentAcceptance_formSearch #btn_turnover");
        var btn_searchexport = $("#equipmentAcceptance_formSearch #btn_searchexport");
        var btn_selectexport = $("#equipmentAcceptance_formSearch #btn_selectexport");
        var btn_scrap = $("#equipmentAcceptance_formSearch #btn_scrap");
        var btn_sold = $("#equipmentAcceptance_formSearch #btn_sold");
        var btn_inactive = $("#equipmentAcceptance_formSearch #btn_inactive");
        //添加日期控件
        laydate.render({
            elem: '#equipmentAcceptance_formSearch #lysjstart'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#equipmentAcceptance_formSearch #lysjend'
            , theme: '#2381E9'
        });
        /*--------------------------------模糊查询---------------------------*/
        if (btn_query != null) {
            btn_query.unbind("click").click(function () {
                searchequipmentAcceptanceResult(true);
            });
        }
        /* ------------------------------查看-----------------------------*/
        btn_view.unbind("click").click(function () {
            var sel_row = $('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');
            if (sel_row.length == 1) {
                equipmentAcceptanceDealById(sel_row[0].sbysid, "view", btn_view.attr("tourl"),'');
            } else {
                $.error("请选中一行");
            }
        });
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            equipmentAcceptanceDealById(null, "add", btn_add.attr("tourl"),'');
        });
        /*----------------------修改----------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                equipmentAcceptanceDealById(sel_row[0].sbysid,"mod",btn_mod.attr("tourl"),'');
            }else{
                $.error("请选中一行");
            }
        });
        /*----------------------移交----------------------*/
        btn_turnover.unbind("click").click(function(){
            var sel_row=$('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if (sel_row[0].sbzt=='0'){
                    equipmentAcceptanceDealById(sel_row[0].sbysid,"turnover",btn_turnover.attr("tourl"),sel_row[0].sbmc);
                }else {
                    $.error("只有闲置状态的设备可以移交！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].sbysid;
                }
                ids = ids.substr(1);
                $.showDialog($('#equipmentAcceptance_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=EQUIPMENTACCEPTANCE_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        /*----------------------报废----------------------*/
        btn_scrap.unbind("click").click(function(){
            var sel_row=$('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');
            if(sel_row.length==0){
                $.error("请至少选中一行");
            }else if(sel_row.length>=1){
                var ids="";
                var zts="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if (sel_row[i].sbzt=='0'||sel_row[i].sbzt=='1'){
                        ids = ids + "," + sel_row[i].sbysid;
                        zts = zts + "," + sel_row[i].sbzt;
                    }else {
                        $.error("只有闲置和使用中的设备才允许报废!");
                        return false;
                    }
                }
                ids = ids.substr(1);
                zts= zts.substr(1);
                $.confirm('您确定要报废所选择的设备吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_scrap.attr("tourl");
                        url=$("#equipmentAcceptance_formSearch #urlPrefix").val()+url;
                        jQuery.post(url,{ids:ids,sbzts_t:zts,lx:'scrap',"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchequipmentAcceptanceResult();
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
        /*----------------------售出----------------------*/
        btn_sold.unbind("click").click(function(){
            var sel_row=$('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');
            if(sel_row.length==0){
                $.error("请至少选中一行");
            }else if(sel_row.length>=1){
                var ids="";
                var zts="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if (sel_row[i].sbzt=='0'||sel_row[i].sbzt=='1'){
                        ids = ids + "," + sel_row[i].sbysid;
                        zts = zts + "," + sel_row[i].sbzt;
                    }else {
                        $.error("只有闲置和使用中的设备才允许售出!");
                        return false;
                    }
                }
                ids = ids.substr(1);
                zts= zts.substr(1);
                $.confirm('您确定要售出所选择的设备吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_scrap.attr("tourl");
                        url=$("#equipmentAcceptance_formSearch #urlPrefix").val()+url;
                        jQuery.post(url,{ids:ids,sbzts_t:zts,lx:'sold',"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchequipmentAcceptanceResult();
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
        /*----------------------闲置----------------------*/
        btn_inactive.unbind("click").click(function(){
            var sel_row=$('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('getSelections');
            if(sel_row.length==0){
                $.error("请至少选中一行");
            }else if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if (sel_row[i].sbzt=='1'){
                        ids = ids + "," + sel_row[i].sbysid;
                    }else {
                        $.error("使用中的设备才允许闲置!");
                        return false;
                    }
                }
                ids = ids.substr(1);
                $.confirm('您确定要闲置所选择的设备吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_inactive.attr("tourl");
                        url=$("#equipmentAcceptance_formSearch #urlPrefix").val()+url;
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchequipmentAcceptanceResult();
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
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#equipmentAcceptance_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=EQUIPMENTACCEPTANCE_SEARCH&expType=search&callbackJs=equipmentSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
    }
    /*-----------------------显示隐藏------------------------------------*/
    $("#equipmentAcceptance_formSearch #sl_searchMore").on("click", function (ev) {
        var ev = ev || event;
        if (equipmentAcceptance_turnOff) {
            $("#equipmentAcceptance_formSearch #searchMore").slideDown("low");
            equipmentAcceptance_turnOff = false;
            this.innerHTML = "基本筛选";
        } else {
            $("#equipmentAcceptance_formSearch #searchMore").slideUp("low");
            equipmentAcceptance_turnOff = true;
            this.innerHTML = "高级筛选";
        }
        ev.cancelBubble = true;
        //showMore();
    });
    return oInit;
}
function equipmentAcceptanceDealById(id,action,tourl,sbmc){
    if(!tourl){
        return;
    }
    tourl = $("#equipmentAcceptance_formSearch #urlPrefix").val()+tourl;
    if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增',addequipmentAcceptanceConfig);
    }else if(action =='mod'){
        var url= tourl+"?sbysid="+id;
        $.showDialog(url,'修改',addequipmentAcceptanceConfig);
    }else if(action =='view'){
        var url= tourl+"?sbysid="+id;
        $.showDialog(url,'查看',viewequipmentAcceptanceConfig);
    }else if(action =='turnover'){
        var url= tourl+"?sbysid="+id+"&sbmc="+sbmc;
        $.showDialog(url,'移交',turnoverequipmentAcceptanceConfig);
    }
}
// 新增
var addequipmentAcceptanceConfig = {
    width : "1600px",
    modalName : "addequipmentAcceptanceModal",
    formName : "equipmentAdd_formSearch",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确　定",
            className : "btn-primary",
            callback : function() {
                if(!$("#equipmentAdd_formSearch").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#equipmentAdd_formSearch input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"equipmentAdd_formSearch",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchequipmentAcceptanceResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {});
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
}
// 移交
var turnoverequipmentAcceptanceConfig = {
    width : "1000px",
    modalName : "turnoverequipmentAcceptanceModal",
    formName : "turnover_formSearch",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确　定",
            className : "btn-primary",
            callback : function() {
                if(!$("#turnover_formSearch").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#turnover_formSearch input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"turnover_formSearch",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchequipmentAcceptanceResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {});
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
}
/**
 * 查看页面模态框
 */
var viewequipmentAcceptanceConfig={
    width		: "1600px",
    modalName	:"viewequipmentAcceptanceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}


/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchequipmentAcceptanceResult(isTurnBack){
    //关闭高级搜索条件
    $("#equipmentAcceptance_formSearch #searchMore").slideUp("low");
    Purchase_turnOff=true;
    $("#equipmentAcceptance_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#equipmentAcceptance_formSearch #equipmentAcceptance_list').bootstrapTable('refresh');
    }
}

$(function(){
    // 1.初始化Table
    var oTable = new equipmentAcceptance_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new equipmentAcceptance_ButtonInit();
    oButtonInit.Init();
    jQuery('#equipmentAcceptance_formSearch .chosen-select').chosen({width: '100%'});
})