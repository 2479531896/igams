var vacationRecord_turnOff=true;

var vacationRecord_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#vacationRecord_formSearch #tab_list').bootstrapTable({
            url:$("#vacationRecord_formSearch #urlPrefix").val()+'/vacation/vacation/pageGetListVacationRecord',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#vacationRecord_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jqffjl.lrsj",			// 排序字段
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
            uniqueId: "ffjlid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '1%',
                align: 'left',
                visible:true
            }, {
                field: 'ffjlid',
                title: '发放记录id',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'lrsj',
                title: '发放时间',
                titleTooltip:'发放时间',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'fffs',
                title: '发放方式',
                titleTooltip:'发放方式',
                width: '4%',
                align: 'left',
                visible: true,
                formatter:fffsFormat
            }, {
                field: 'nd',
                title: '年度',
                width: '3%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'yhm',
                title: '用户名',
                width: '3%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zsxm',
                title: '姓名',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jqlxmc',
                title: '假期类型',
                titleTooltip:'假期类型',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sc',
                title: '时长',
                titleTooltip:'时长',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'dw',
                title: '单位',
                titleTooltip:'单位',
                width: '3%',
                align: 'left',
                visible: true,
                formatter:dwFormat
            },{
                field: 'kssj',
                title: '开始时间',
                titleTooltip:'开始时间',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'yxq',
                title: '有效期',
                titleTooltip:'有效期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lrrymc',
                title: '录入人员',
                titleTooltip:'录入人员',
                width: '4%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'jeze',
                title: '假期总额',
                titleTooltip:'假期总额',
                width: '4%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'yyed',
                title: '已用额度',
                titleTooltip:'已用额度',
                width: '4%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'syed',
                title: '剩余额度',
                titleTooltip:'剩余额度',
                width: '4%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'ddze',
                title: '钉钉总额',
                titleTooltip:'钉钉总额',
                width: '4%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'ddyyed',
                title: '钉钉已用额度',
                titleTooltip:'钉钉已用额度',
                width: '6%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'ddsyed',
                title: '钉钉剩余额度',
                titleTooltip:'钉钉剩余额度',
                width: '6%',
                align: 'left',
                visible: false,
                sortable: true
            }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                vacationRecordDealById(row.ffjlid, 'view',$("#vacationRecord_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#vacationRecord_formSearch #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return  vacationRecordSearchData(map);
    };
    return oTableInit;
};
function vacationRecordSearchData(map){
    var cxtj=$("#vacationRecord_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#vacationRecord_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["yhm"]=cxnr;
    }else if(cxtj=="2"){
        map["zsxm"]=cxnr;
    }
    // 创建开始日期
    var kssjstart = jQuery('#vacationRecord_formSearch #kssjstart').val();
    map["kssjstart"] = kssjstart;

    // 创建结束日期
    var kssjend = jQuery('#vacationRecord_formSearch #kssjend').val();
    map["kssjend"] = kssjend;
    // 结束开始日期
    var yxqstart = jQuery('#vacationRecord_formSearch #yxqstart').val();
    map["yxqstart"] = yxqstart;

    // 结束结束日期
    var yxqend = jQuery('#vacationRecord_formSearch #yxqend').val();
    map["yxqend"] = yxqend;
    // 删除标记
    var fffs = jQuery('#vacationRecord_formSearch #fffs_id_tj').val();
    map["fffs"] = fffs;

    //假期类型
    var jqlxs=jQuery('#vacationRecord_formSearch #jqlx_id_tj').val();
    map["jqlxs"] = jqlxs.replace(/'/g, "");
    //年度
    var nds=jQuery('#vacationRecord_formSearch #nd_id_tj').val();
    map["nds"] = nds.replace(/'/g, "");
    return map;
}

function dwFormat(value, row, index) {
    if (row.dw=='0'){
        return "小时";
    }else if (row.dw=='1'){
        return "天";
    }

}
function fffsFormat(value, row, index) {
    if (row.fffs=='0'){
        return "自动发放";
    }else if (row.fffs=='1'){
        return "手动发放";
    }

}
//按钮动作函数
function vacationRecordDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl=$("#vacationRecord_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?ffjlid=" +id;
        $.showDialog(url,'查看假期发放记录',viewVacationRecordConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增',addVacationRecordConfig);
    }
}
/**
 * 新增
 */
var addVacationRecordConfig = {
    width		: "1000px",
    modalName	: "vacationRecordModal",
    formName	: "vacationRecord_Form",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#vacationRecord_Form").valid()){
                    $.alert("请填写完整信息!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#vacationRecord_Form input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"vacationRecord_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchvacationRecordResult(true);
                        });
                    }else if(responseText["status"] == "fail"){
                        var cwlb = responseText["lb"];
                        if ('addOne'==cwlb){
                            confirmSaveOne(responseText["message"],responseText["cs"],opts);
                        }else if ('addTwo'==cwlb){
                            confirmSaveTwo(responseText["message"],responseText["cs"],opts);
                        }else {
                            $.error(responseText["message"],function() {
                            });
                        }
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
function confirmSaveOne (msg,cs,opts){
    $.confirm(msg,function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $('#vacationRecord_Form #urlPrefix').val() + "/vacation/vacation/pagedataConfirmSaveOne";
            jQuery.post(url,{'jqffjlDto':cs,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchvacationRecordResult(true);
                        });
                    }else if(responseText["status"] == "fail"){
                        var cwlb = responseText["lb"];
                        if ('addTwo'==cwlb){
                            confirmSaveTwo(responseText["message"],responseText["cs"],opts);
                        }else {
                            $.error(responseText["message"],function() {
                            });
                        }
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
function confirmSaveTwo (msg,cs,opts){
    $.confirm(msg,function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $('#vacationRecord_Form #urlPrefix').val() + "/vacation/vacation/pagedataConfirmSaveTwo";
            jQuery.post(url,{'jqffjlDto':cs,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            searchvacationRecordResult(true);
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
var viewVacationRecordConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var vacationRecord_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#vacationRecord_formSearch #btn_view");
        var btn_query = $("#vacationRecord_formSearch #btn_query");
        var btn_del = $("#vacationRecord_formSearch #btn_del");
        var btn_add = $("#vacationRecord_formSearch #btn_add");
        //添加日期控件
        laydate.render({
            elem: '#vacationRecord_formSearch #kssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#vacationRecord_formSearch #kssjend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#vacationRecord_formSearch #yxqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#vacationRecord_formSearch #yxqend'
            ,theme: '#2381E9'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchvacationRecordResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#vacationRecord_formSearch #tab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                vacationRecordDealById(sel_row[0].ffjlid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 新增假期记录 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            vacationRecordDealById(null, "add", btn_add.attr("tourl"));
        });
        /*----------------------------删除假期发放记录信息--------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#vacationRecord_formSearch #tab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                var yhjqids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].ffjlid;
                    yhjqids= yhjqids + ","+ sel_row[i].yhjqid;
                }
                ids=ids.substr(1);
                yhjqids=yhjqids.substr(1);
                $.confirm('您确定要删除所选择的记录吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url=  $('#vacationRecord_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                        jQuery.post(url,{"ids":ids,"yhjqids":yhjqids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchvacationRecordResult(true);
                                    });
                                }else if(responseText["status"] == "fail"){
                                    var cwlb = responseText["lb"];
                                    if ('delOne'==cwlb){
                                        confirmDelOne(responseText["message"],responseText["cs"]);
                                    }else {
                                        $.error(responseText["message"],function() {
                                        });
                                    }
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
        })
        /**显示隐藏**/
        $("#vacationRecord_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(vacationRecord_turnOff){
                $("#vacationRecord_formSearch #searchMore").slideDown("low");
                vacationRecord_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#vacationRecord_formSearch #searchMore").slideUp("low");
                vacationRecord_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};
function confirmDelOne (msg,cs){
    $.confirm(msg,function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $('#vacationRecord_formSearch #urlPrefix').val() + "/vacation/vacation/pagedataConfirmDelVacationRecord";
            jQuery.post(url,{'jqffjlDto':cs,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            searchvacationRecordResult(true);
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



function searchvacationRecordResult(isTurnBack){
    //关闭高级搜索条件
    $("#vacationRecord_formSearch #searchMore").slideUp("low");
    vacationRecord_turnOff=true;
    $("#vacationRecord_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#vacationRecord_formSearch #tab_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#vacationRecord_formSearch #tab_list').bootstrapTable('refresh');
    }
}



$(function(){
    // 1.初始化Table
    var oTable = new vacationRecord_TableInit();
    oTable.Init();
    var ButtonInit=vacationRecord_ButtonInit();
    ButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#vacationRecord_formSearch .chosen-select').chosen({width: '100%'});

    // 初始绑定显示更多的事件
    $("#vacationRecord_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});