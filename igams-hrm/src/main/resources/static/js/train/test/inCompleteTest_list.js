var inCompleteTest_turnOff=true;

var inCompleteTest_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#inCompleteTest_formSearch #inCompleteTest_list").bootstrapTable({
            url: $("#inCompleteTest_formSearch #urlPrefix").val()+'/train/test/pageGetListIncomplete',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#inCompleteTest_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "tab.fzrmc",				//排序字段
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
            uniqueId: "tmp.pxid",                     //每一行的唯一标识，一般为主键列
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
                width: '1%',
                align: 'left',
                visible:true
            },{
                field: 'zjid',
                title: '主键id',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false,
            },{
                field: 'pxid',
                title: '培训id',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false,
            },{
                field: 'fzrmc',
                title: '姓名',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jgmc',
                title: '部门',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'pxbt',
                title: '培训标题',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:getTrainxx
            },{
                field: 'pxlbmc',
                title: '类别',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qrrymc',
                title: '确认人员',
                width: '2%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'ssgsmc',
                title: '所属公司',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sfcsmc',
                title: '是否测试',
                width: '2%',
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
                inCompleteTestDealById(row.pxid,row.ddid,"",'view',$("#inCompleteTest_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#inCompleteTest_formSearch #inCompleteTest_list").colResizable({
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
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return inCompleteTestSearchData(map);
    }
    return oTableInit
}
//提供给导出用的回调函数
function InCompleteSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="";
    map["sortLastOrder"]="desc";
    map["sortName"]="";
    map["sortOrder"]="desc";
    return inCompleteTestSearchData(map);
}

function inCompleteTestSearchData(map) {
    var cxtj = $("#inCompleteTest_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#inCompleteTest_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["xm"] = cxnr
    } else if (cxtj == "1") {
        map["jgmc"] = cxnr
    }else if (cxtj == "2") {
        map["pxbt"] = cxnr
    }else if (cxtj == "3") {
        map["qrry"] = cxnr
    }else if (cxtj == "4") {
        map["entire"] = cxnr
    }
    var pxlbs = jQuery('#inCompleteTest_formSearch #pxlb_id_tj').val();
    map["pxlbs"] = pxlbs.replace(/'/g, "");
    var pxzlbs = jQuery('#inCompleteTest_formSearch #pxzlb_id_tj').val();
    map["pxzlbs"] = pxzlbs.replace(/'/g, "");
    var ssgss = jQuery('#inCompleteTest_formSearch #ssgs_id_tj').val();
    map["ssgss"] = ssgss.replace(/'/g, "");
    var sfcss = jQuery('#inCompleteTest_formSearch #sfcs_id_tj').val();
    map["sfcss"] = sfcss.replace(/'/g, "");
    return map;
}
function getTrainxx(value, row, index) {
    var html="";
    if (row.pxbt==null)
        html = "";
    else html = "<a href='javascript:void(0);' onclick='queryBypxid(\""+row.pxid+"\")'>"+row.pxbt+"</a>"
    return html
}
function queryBypxid(id) {
    var url=$("#inCompleteTest_formSearch #urlPrefix").val()+"/train/test/viewTrain?pxid="+id;
    $.showDialog(url,'培训详细信息',viewinCompleteTestConfig);
}
function inCompleteTestDealById(id,ddid,ids,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#inCompleteTest_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?pxid=" +id+"&ddid="+ddid;
        $.showDialog(url,'详细信息',viewinCompleteTestConfig);
    }  else if(action =='remind'){
        var url= tourl + "?ids=" +id+"&idss="+ids;
        $.showDialog(url,'提醒',remindinCompleteTestConfig);
    }
}
/**
 * 查看页面模态框
 */
var viewinCompleteTestConfig={
    width		: "1000px",
    modalName	:"viewInCompleteTestModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var inCompleteTest_ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#inCompleteTest_formSearch #btn_query");
        var btn_view = $("#inCompleteTest_formSearch #btn_view");
        var btn_searchexport = $("#inCompleteTest_formSearch #btn_searchexport");
        var btn_selectexport = $("#inCompleteTest_formSearch #btn_selectexport");
        var btn_remind= $("#inCompleteTest_formSearch #btn_remind");
        /* ---------------------------模糊查询-----------------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchinCompleteTestResult(true);
            });
        }

        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#inCompleteTest_formSearch #inCompleteTest_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                inCompleteTestDealById(sel_row[0].pxid,sel_row[0].ddid,"","view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提醒-----------------------------------*/
        btn_remind.unbind("click").click(function(){
            var sel_row = $('#inCompleteTest_formSearch #inCompleteTest_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                var json = [];
                var idss="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].ddid;
                    idss=idss+","+sel_row[i].pxbt;
                    var sz = {"ddid": '', "yhm": ''};
                    sz.ddid = sel_row[i].ddid;
                    sz.yhm = sel_row[i].yhm;
                    json.push(sz);
                }
                var tzry_json = JSON.stringify(json)
                ids = ids.substr(1);
                idss=idss.substr(1);
                $.confirm('您确定要发送提醒消息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#inCompleteTest_formSearch #urlPrefix').val()+btn_remind.attr("tourl");
                        jQuery.post(url,{ids:ids,idss:idss,"tzry_json":tzry_json,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchinCompleteTestResult();
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
            }else{
                $.error("请选择数据");
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#inCompleteTest_formSearch #inCompleteTest_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].zjid;
                }
                ids = ids.substr(1);
                $.showDialog($('#inCompleteTest_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=KSWWC_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#inCompleteTest_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=KSWWC_SEARCH&expType=search&callbackJs=InCompleteSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

        /*-----------------------高级筛选------------------------------------*/
        $("#inCompleteTest_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(inCompleteTest_turnOff){
                $("#inCompleteTest_formSearch #searchMore").slideDown("low");
                inCompleteTest_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#inCompleteTest_formSearch #searchMore").slideUp("low");
                inCompleteTest_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};


function searchinCompleteTestResult(isTurnBack) {
    //关闭高级搜索条件
    $("#inCompleteTest_formSearch #searchMore").slideUp("low");
    inCompleteTest_turnOff=true;
    $("#inCompleteTest_formSearch #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#inCompleteTest_formSearch #inCompleteTest_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#inCompleteTest_formSearch #inCompleteTest_list').bootstrapTable('refresh');
    }
}
$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new inCompleteTest_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new inCompleteTest_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#inCompleteTest_formSearch .chosen-select').chosen({width: '100%'});

});
