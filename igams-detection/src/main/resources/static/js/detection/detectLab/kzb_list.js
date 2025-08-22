var Kzb_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#kzb_formSearch #kzb_list").bootstrapTable({
            url: '/detectlab/kzbinfo/pageGetListKzb',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#kzb_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "kzbgl.syrq desc, kzbgl.jcdw",				// 排序字段
            sortOrder: "asc",                   // 排序方式
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
            uniqueId: "fzjcid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                field: 'kzbid',
                title: '扩增板ID',
                titleTooltip:'扩增板ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'kzbh',
                title: '扩增板号',
                titleTooltip:'扩增板号',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'syrq',
                title: '实验日期',
                titleTooltip:'实验日期',
                sortable: true,
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'kzsjph',
                title: '扩增试剂',
                titleTooltip:'扩增试剂',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:true
            },{
                field: 'tqsjph',
                title: '提取试剂',
                titleTooltip:'提取试剂',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'lrrymc',
                title: '录入人员',
                titleTooltip:'录入人员',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'lrsj',
                title: '录入时间',
                titleTooltip:'录入时间',
                width: '5%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Kzb_DealById(row.kzbid,'view',$("#kzb_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#kzb_formSearch #kzb_list").colResizable({
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
            sortLastName: "kzbgl.kzbid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getKzbSearchData(map);
    };
    return oTableInit;
}

// function kzbztformat(value,row,index){
//     if (row.zt == '00') {
//         return '未提交';
//     }else if (row.zt == '80'){
//         return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPay_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
//     }else if (row.zt == '15') {
//
//         return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPay_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
//     }else{
//
//         return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPay_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
//     }
// }

function getKzbSearchData(map){
    var kzb_select=$("#kzb_formSearch #kzb_select").val();
    var kzb_input=$.trim(jQuery('#kzb_formSearch #kzb_input').val());
    if(kzb_select=="0"){
        map["kzbh"]=kzb_input
    }else if(kzb_select=="1"){
        map["jcdwmc"]=kzb_input
    }else if(kzb_select=="2"){
        map["kzsjph"]=kzb_input
    }else if(kzb_select=="3"){
        map["tqsjph"]=kzb_input
    }else if(kzb_select=="4"){
        map["ybbh"]=kzb_input
    }
    return map;
}

function searchKzbResult(isTurnBack){
    if(isTurnBack){
        $('#kzb_formSearch #kzb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#kzb_formSearch #kzb_list').bootstrapTable('refresh');
    }
}
function Kzb_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增扩增板信息',addKzbConfig);
    }else if(action=="view"){
        var url=tourl+"?kzbid="+id
        $.showDialog(url,'查看扩增板信息',viewKzbConfig);
    }else if(action=="mod"){
        var url=tourl+"?kzbid="+id
        $.showDialog(url,'修改扩增板信息',modKzbConfig);
    }else if(action=="del"){
        var url=tourl+"?kzbid="+id
        $.showDialog(url,'删除扩增板信息',delKzbConfig);
    }
}

var addKzbConfig = {
    width		: "1400px",
    modalName	:"addKzbModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                $("input[name='ybbh']").attr("style","border-color:#ccc;height:30px;");
                var ybbhs=[];
                var hls=[];
                var xhs=[];
                var ybbh;
                for(i=1;i<9;i++){
                    for(j=1;j<13;j++){
                        ybbh=$("#ybbh"+i+"-"+j).val();
                        hl = i+"-"+j;
                        if(ybbh!=null && ybbh!=''){
                            ybbhs.push(ybbh);
                            xhs.push($("#ybbh"+i+"-"+j).attr("xh"));
                            hls.push(hl);
                            var ybbhcxcs=0;//用于判断内部编号是否重复
                            for(var l=0;l<ybbhs.length;l++){
                                if(ybbhs[l]==ybbh && ybbh!=""){
                                    ybbhcxcs++;
                                    if(ybbhcxcs>=2){
                                        $.confirm("内部编号存在重复!"+"(编号信息:"+ybbh+")");
                                        //return false;
                                    }
                                }
                            }
                        }
                    }
                }
                if(ybbhs==''){
                    $.confirm("内容不能为空!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#ajaxForm #ybbhs").val(ybbhs);
                $("#ajaxForm #xhs").val(xhs);
                $("#ajaxForm #hls").val(hls);

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchKzbResult();
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

var modKzbConfig= {
    width		: "1400px",
    modalName   :"modKzbModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                $("input[name='ybbh']").attr("style","border-color:#ccc;height:30px;");
                var ybbhs=[];
                var hls=[];
                var xhs=[];
                var ybbh;
                for(i=1;i<9;i++){
                    for(j=1;j<13;j++){
                        ybbh=$("#ybbh"+i+"-"+j).val();
                        hl = i+"-"+j;
                        if(ybbh!=null && ybbh!=''){
                            ybbhs.push(ybbh);
                            xhs.push($("#ybbh"+i+"-"+j).attr("xh"));
                            hls.push(hl);
                            var ybbhcxcs=0;//用于判断内部编号是否重复
                            for(var l=0;l<ybbhs.length;l++){
                                if(ybbhs[l]==ybbh && ybbh!=""){
                                    ybbhcxcs++;
                                    if(ybbhcxcs>=2){
                                        $.confirm("内部编号存在重复!"+"(编号信息:"+ybbh+")");
                                        //return false;
                                    }
                                }
                            }
                        }
                    }
                }
                if(ybbhs==''){
                    $.confirm("内容不能为空!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#ajaxForm #ybbhs").val(ybbhs);
                $("#ajaxForm #xhs").val(xhs);
                $("#ajaxForm #hls").val(hls);

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchKzbResult();
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
                return false;}
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }

    }
};

var viewKzbConfig = {
    width		: "1800px",
    height      : "500PX",
    modalName	:"viewKzbModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var Kzb_oButton=function(){
    var oButtonInit = new Object();
    oButtonInit.Init=function(){
        var btn_query=$("#kzb_formSearch #btn_query");
        var btn_view = $("#kzb_formSearch #btn_view");//查看
        var btn_mod=$("#kzb_formSearch #btn_mod");//修改
        var btn_add=$("#kzb_formSearch #btn_add");//新增
        var btn_del=$("#kzb_formSearch #btn_del");//删除
        var btn_generatelayout=$("#kzb_formSearch #btn_generatelayout");//生成扩增布板
        /*--------------------------------删除---------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#kzb_formSearch #kzb_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
                    ids= ids + ","+ sel_row[i].kzbid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        // var url= $('#kzb_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(btn_del.attr("tourl"),{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchKzbResult();
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
        /*--------------------------------生成---------------------------*/
        btn_generatelayout.unbind("click").click(function(){
            var sel_row = $('#kzb_formSearch #kzb_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请选中一行");
                return;
            }else{
                $.confirm('您确定要生成所选择的扩增布板吗？',function(result){
                    if(result){
                        window.location.href=btn_generatelayout.attr("tourl")+"?kzbid="+ sel_row[0].kzbid +"&access_token="+  $("#ac_tk").val();
                    }
                });
            }
        });
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            Kzb_DealById(null,"add",btn_add.attr("tourl"));
        });
        /*--------------------------------修改---------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#kzb_formSearch #kzb_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                Kzb_DealById(sel_row[0].kzbid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchKzbResult(true);
            });
        }
        /*--------------------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#kzb_formSearch #kzb_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Kzb_DealById(sel_row[0].kzbid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}

$(function(){
    var oTable = new Kzb_TableInit();
    oTable.Init();
    var oButton = new Kzb_oButton();
    oButton.Init();
    jQuery('#kzb_formSearch .chosen-select').chosen({width: '100%'});
})