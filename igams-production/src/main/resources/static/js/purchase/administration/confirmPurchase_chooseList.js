var PurchaseConfirm_turnOff=true;

var confirmChooseAdministration_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#confirmChoose_xz_formSearch #chooseConfirm_list").bootstrapTable({
            url: $("#confirmChoose_xz_formSearch #urlPrefix").val()+'/administration/purchase/pageGetListConfirmPurchaseAdministration?zt=80&fkwcbj=0',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#confirmChoose_xz_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qrgl.lrsj",				//排序字段
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
            uniqueId: "qrid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width:'4%'
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
                field: 'qrid',
                title: '确认id',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'qrdh',
                title: '确认单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '确认人员',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qrrq',
                title: '确认日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dzfsmc',
                title: '对账方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zfdx',
                title: '支付对象',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zje',
                title: '总金额',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:confirm_czFormat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                if($("#confirmChoose_xz_formSearch #cz_"+row.qrid).text()){
                    if($("#confirmChoose_xz_formSearch #cz_"+row.qrid).text() != "-"){
                        $("#confirmChoose_xz_formSearch #cz_"+row.qrid).removeAttr("class");
                        $("#confirmChoose_xz_formSearch #cz_"+row.qrid).text("");
                    }
                }else{
                    $("#confirmChoose_xz_formSearch #cz_"+row.qrid).attr("class","btn btn-success");
                    $("#confirmChoose_xz_formSearch #cz_"+row.qrid).text("调整明细");
                    // 判断是否更新请购信息
                    var qrmxJson = [];
                    var refresh = true;
                    if($("#confirmChoose_xz_formSearch #qrmx_json").val()){
                        qrmxJson = JSON.parse($("#confirmChoose_xz_formSearch #qrmx_json").val());
                        if(qrmxJson.length > 0){
                            for (var i = qrmxJson.length-1; i >= 0; i--) {
                                if(qrmxJson[i].qrid == row.qrid){
                                    refresh = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(refresh){
                        var fkid = $("#xzqgfkEditForm #fkid").val();
                        // 查询请购明细信息
                        $.ajax({
                            type:'post',
                            url: $("#confirmChoose_xz_formSearch input[name='urlPrefix']").val() + "/administration/purchase/pagedataListQrmxListInfo",
                            cache: false,
                            data: {"qrid":row.qrid, "fkid":fkid, "access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.rows != null && result.rows.length > 0){
                                    var isUpdate = false;
                                    for (var i = 0; i < result.rows.length; i++) {
                                        var sl = 0;
                                        if(result.rows[i].sl){
                                            sl = result.rows[i].sl;
                                        }
                                        isUpdate = true;
                                        var sz = {"qrid":'',"qrmxid":''};
                                        sz.qrid = result.rows[i].qrid;
                                        sz.qrmxid = result.rows[i].qrmxid;
                                        qrmxJson.push(sz);
                                    }
                                    $("#confirmChoose_xz_formSearch #qrmx_json").val(JSON.stringify(qrmxJson));
                                    // 判断是否更新订单号
                                    if(isUpdate){
                                        $("#confirmChoose_xz_formSearch  #yxdjh").tagsinput('add',{"value":row.qrid,"text":row.qrdh});
                                    }else{
                                        $("#confirmChoose_xz_formSearch #cz_"+row.qrid).removeAttr("class");
                                        $("#confirmChoose_xz_formSearch #cz_"+row.qrid).text("-");
                                    }
                                }
                            }
                        });
                    }
                }
            },
        });
        $("#confirmChoose_xz_formSearch #chooseConfirm_list").colResizable({
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
            sortLastName: "qrgl.qrid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return confirmPurchaseAdministrationSearchData(map);
    }
    return oTableInit
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function confirm_czFormat(value,row,index) {
    return "<span id='cz_"+row.qrid+"' onclick=\"reviseDetail('" + row.qrid + "',event)\"/>";
}

/**
 * 调整明细点击事件
 * @param qrid
 * @param event
 * @returns
 */
function reviseDetail(qrid, event){
    if($("#cz_"+qrid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $("#confirmChoose_xz_formSearch input[name='urlPrefix']").val() + "/administration/administration/getXzqrmxListPage?access_token=" + $("#ac_tk").val() + "&qrid=" + qrid;
    $.showDialog(url, '选择确认明细', chooseQrmxConfig);
}
var chooseQrmxConfig = {
    width : "1000px",
    modalName	: "chooseQrmxModal",
    formName	: "chooseQrmxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseQrmxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseQrmxForm input[name='checkQrmx']:checked").length == 0){
                    $.alert("未选中确认明细信息！");
                    return false;
                }
                var qrmxJson = [];
                if($("#confirmChoose_xz_formSearch #qrmx_json").val()){
                    qrmxJson = JSON.parse($("#confirmChoose_xz_formSearch #qrmx_json").val());
                }
                var qrid = $("#chooseQrmxForm input[name='qrid']").val();
                if(qrmxJson.length > 0){
                    for (var i = qrmxJson.length-1; i >= 0; i--) {
                        if(qrmxJson[i].qrid == qrid){
                            qrmxJson.splice(i,1);
                        }
                    }
                }
                $("#chooseQrmxForm input[name='checkQrmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"qrid":'',"qrmxid":''};
                        sz.qrid = qrid;
                        sz.qrmxid = this.dataset.qrmxid;
                        qrmxJson.push(sz);
                    }
                })
                $("#confirmChoose_xz_formSearch #qrmx_json").val(JSON.stringify(qrmxJson));
                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var confirmChooseAdministratio_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#confirmChoose_xz_formSearch #btn_query");
        //添加日期控件
        laydate.render({
            elem: '#confirmChoose_xz_formSearch #qrrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#confirmChoose_xz_formSearch #qrrqend'
            ,theme: '#2381E9'
        });
        initTagsinput();
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchconfirmPurchaseAdministratioResult(true);
            });
        }
    }
    return oInit;
}




function confirmPurchaseAdministrationSearchData(map){
    var cxtj=$("#confirmChoose_xz_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#confirmChoose_xz_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["qrdh"]=cxnr
    }
    // 确认开始日期
    var qrrqstart = jQuery('#confirmChoose_xz_formSearch #qrrqstart').val();
    map["qrrqstart"] = qrrqstart;
    // 确认结束日期
    var qrrqend = jQuery('#confirmChoose_xz_formSearch #qrrqend').val();
    map["qrrqend"] = qrrqend;
    return map;
}

/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchconfirmPurchaseAdministratioResult(isTurnBack){
    //关闭高级搜索条件
    $("#confirmChoose_xz_formSearch #searchMore").slideUp("low");
    PurchaseConfirm_turnOff=true;
    if(isTurnBack){
        $('#confirmChoose_xz_formSearch #chooseConfirm_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#confirmChoose_xz_formSearch #chooseConfirm_list').bootstrapTable('refresh');
    }
}

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
    $("#confirmChoose_xz_formSearch  #yxdjh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
    // 初始化已选明细json
    $("#confirmChoose_xz_formSearch #djhs").val($("#xzqgfkEditForm #fkmx_json").val());
}

$(function(){
    // 1.初始化Table
    var oTable = new confirmChooseAdministration_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new confirmChooseAdministratio_ButtonInit();
    oButtonInit.Init();
    jQuery('#confirmChoose_xz_formSearch .chosen-select').chosen({width: '100%'});
})