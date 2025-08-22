var Llxx_turnOff=true;
var columnsArray = [
    {
        checkbox: true
    },{
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '10%',
        align: 'left',
        visible:true
    },{
        field: 'llid',
        title: '领料ID',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: false
    },{
        field: 'lldh',
        title: '领料单号',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'sqbm',
        title: '申请部门',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'sqry',
        title: '申请人',
        width: '20%',
        align: 'left',
        visible:true
    },{
        field: 'sqrq',
        title: '申请日期',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '20%',
        align: 'left',
        visible: true,
        formatter:czFormat
    }]
var ChooseLlxx_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#chooseLlxxForm #llxx_list").bootstrapTable({
            url: $('#chooseLlxxForm #urlPrefix').val() +'/storehouse/receiveMateriel/pagedataGetListReceiveMateriel',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseLlxxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "llgl.lrsj",				//排序字段
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
            uniqueId: "llid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
                if($("#chooseLlxxForm #cz_"+row.llid).text()){
                    if($("#chooseLlxxForm #cz_"+row.llid).text() != "-"){
                        $("#chooseLlxxForm #cz_"+row.llid).removeAttr("class");
                        $("#chooseLlxxForm #cz_"+row.llid).text("");
                    }
                }else{
                    $("#chooseLlxxForm #cz_"+row.llid).attr("class","btn btn-success");
                    $("#chooseLlxxForm #cz_"+row.llid).text("调整明细");
                    // 判断是否更新请购信息
                    var llmxJson = [];
                    var refresh = true;
                    if($("#chooseLlxxForm #llmx_json").val()){
                        llmxJson = JSON.parse($("#chooseLlxxForm #llmx_json").val());
                        if(llmxJson.length > 0){
                            for (var i = llmxJson.length-1; i >= 0; i--) {
                                if(llmxJson[i].llid == row.llid){
                                    refresh = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(refresh){
                        // 查询请购明细信息
                        $.ajax({
                            type:'post',
                            url: $('#chooseLlxxForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataGetHwllmx",
                            cache: false,
                            data: {"llid":row.llid,"access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.hwllmxDtos != null && result.hwllmxDtos.length > 0){
                                    var isUpdate = false;
                                    for (var i = 0; i < result.hwllmxDtos.length; i++) {
                                        if(parseFloat(result.hwllmxDtos[i].slsl) > 0){
                                            isUpdate = true;
                                            var sz = {"llid":'',"llmxid":''};
                                            sz.llid = result.hwllmxDtos[i].llid;
                                            sz.llmxid = result.hwllmxDtos[i].llmxid;
                                            llmxJson.push(sz);
                                        }
                                    }
                                    $("#chooseLlxxForm #llmx_json").val(JSON.stringify(llmxJson));
                                    // 判断是否更新订单号
                                    if(isUpdate){
                                        $("#chooseLlxxForm  #yxlldh").tagsinput('add',{"value":row.llid,"text":row.lldh});
                                    }else{
                                        $("#chooseLlxxForm #cz_"+row.llid).removeAttr("class");
                                        $("#chooseLlxxForm #cz_"+row.llid).text("-");
                                        $.alert("此领料单没有实领数量！");
                                    }
                                }
                            }
                        });
                    }
                }
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "llgl.llid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            ckzt: "80", // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return llxxSearchData(map);
    }
    return oTableInit;
}
function llxxSearchData(map){
    var cxtj=$("#chooseLlxxForm #cxtj").val();
    var cxnr=$.trim(jQuery('#chooseLlxxForm #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["lldh"]=cxnr
    }else if(cxtj=="2"){
        map["sqbmmc"]=cxnr
    }else if(cxtj=="3"){
        map["sqrmc"]=cxnr
    }else if(cxtj=="4"){
        map["wlbm"]=cxnr
    }else if(cxtj=="5"){
        map["wlmc"]=cxnr
    }
    // 领料开始日期
    var sqrqstart = jQuery('#chooseLlxxForm #sqrqstart').val();
    map["sqrqstart"] = sqrqstart;

    // 领料结束日期
    var sqrqend = jQuery('#chooseLlxxForm #sqrqend').val();
    map["sqrqend"] = sqrqend;
    return map;
}
function searchLlxxResult(isTurnBack){
    //关闭高级搜索条件
    $("#chooseLlxxForm #searchMore").slideUp("low");
    Llxx_turnOff=true;
    $("#chooseLlxxForm #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#chooseLlxxForm #llxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#chooseLlxxForm #llxx_list').bootstrapTable('refresh');
    }
}
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    return "<span id='cz_"+row.llid+"' onclick=\"reviseDetail('" + row.llid + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param llid
 * @param event
 * @returns
 */
function reviseDetail(llid, event){
    if($("#cz_"+llid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $('#chooseLlxxForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataChooseLlmx?access_token=" + $("#ac_tk").val() + "&llid=" + llid;
    $.showDialog(url, '选择领料明细', chooseLlmxConfig);
}
var chooseLlmxConfig = {
    width : "1200px",
    modalName	: "chooseLlmxModal",
    formName	: "chooseLlmxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseLlmxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseLlmxForm input[name='checkLlmx']:checked").length == 0){
                    $.alert("未选中领料明细信息！");
                    return false;
                }
                var llmxJson = [];
                if($("#chooseLlxxForm #llmx_json").val()){
                    llmxJson = JSON.parse($("#chooseLlxxForm #llmx_json").val());
                }
                var llid = $("#chooseLlmxForm input[name='llid']").val();
                if(llmxJson.length > 0){
                    for (var i = llmxJson.length-1; i >= 0; i--) {
                        if(llmxJson[i].llid == llid){
                            llmxJson.splice(i,1);
                        }
                    }
                }
                $("#chooseLlmxForm input[name='checkLlmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"llid":'',"llmxid":''};
                        sz.llid = llid;
                        sz.llmxid = this.dataset.llmxid;
                        llmxJson.push(sz);
                    }
                })
                $("#chooseLlxxForm #llmx_json").val(JSON.stringify(llmxJson));
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
/**
 * 初始化页面
 */
var ChooseLlxx_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //添加日期控件
        laydate.render({
            elem: '#chooseLlxxForm #sqrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#chooseLlxxForm #sqrqend'
            ,theme: '#2381E9'
        });
        //初始化已选单据号
        initTagsinput();
        var btn_query=$("#chooseLlxxForm #btn_query");
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchLlxxResult(true);
            });
        }
    }
    return oInit;
}

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
    $("#chooseLlxxForm  #yxlldh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
    // 初始化已选明细json
    $("#chooseLlxxForm #llmx_json").val($("#arrivalGoodsEditForm #dhmx_json").val());
}

/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseLlxxForm").on('click','.label-info',function(e){
    event.stopPropagation();
    var url = $('#chooseLlxxForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataChooseLlmx?access_token=" + $("#ac_tk").val() + "&llid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择领料明细', chooseLlmxConfig);
});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseLlxxForm #yxlldh').on('beforeItemRemove', function(event) {
    var llmxJson = [];
    if($("#chooseLlxxForm #llmx_json").val()){
        llmxJson = JSON.parse($("#chooseLlxxForm #llmx_json").val());
    }
    var llid = event.item.value;
    if(llmxJson.length > 0){
        for (var i = llmxJson.length-1; i >= 0; i--) {
            if(llmxJson[i].llid == llid){
                llmxJson.splice(i,1);
            }
        }
    }
    $("#chooseLlxxForm #llmx_json").val(JSON.stringify(llmxJson));
});

$(function(){
    // 1.初始化Table
    var oTable = new ChooseLlxx_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ChooseLlxx_ButtonInit();
    oButtonInit.Init();
    jQuery('#chooseLlxxForm .chosen-select').chosen({width: '100%'});
})