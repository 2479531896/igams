

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
        field: 'htnbbh',
        title: '合同内部编号',
        width: '25%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'fzrmc',
        title: '负责人',
        width: '10%',
        align: 'left',
        visible:true
    },{
        field: 'gysmc',
        title: '供应商',
        width: '25%',
        align: 'left',
        sortable: true,
        visible: true
    }, {
        field: 'cjrq',
        title: '创建日期',
        width: '15%',
        align: 'left',
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '20%',
        align: 'left',
        formatter:czFormat,
        visible: true
    }]
var ChooseContract_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#chooseContractForm #contract_list").bootstrapTable({
            url: $("#chooseContractForm input[name='urlPrefix']").val() + '/contract/contract/pagedataGetPageListContract',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseContractForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "htgl.lrsj",				//排序字段
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
            uniqueId: "htid",                     //每一行的唯一标识，一般为主键列
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
                // 判断是否更新
                var htmxJson = [];
                var refresh = true;
                if($("#editInvoiceForm #gys").val()){
                    if($("#editInvoiceForm #gys").val()!= row.gys){
                        $.error("请选择与已选单号供应商一致的到货信息！");
                        $("#chooseContractForm #contract_list").bootstrapTable('uncheck', row.index);
                        return;
                    }
                }
                if($("#chooseContractForm #htmx_json").val()){
                    htmxJson = JSON.parse($("#chooseContractForm #htmx_json").val());
                    if(htmxJson.length > 0){
                        for (var i = htmxJson.length-1; i >= 0; i--) {
                            if(htmxJson[i].htid == row.htid){
                                refresh = false;
                                break;
                            }
                            if(htmxJson[i].gysid != row.gys){
                                $.error("请选择供应商一致的到货信息！");
                                $("#chooseContractForm #"+row.htid).unselected;
                                $("#chooseContractForm #contract_list").bootstrapTable('uncheck', row.index);
                                return;
                            }
                        }
                    }
                }
                if($("#chooseContractForm #cz_"+row.htid).text()){
                    if($("#chooseContractForm #cz_"+row.htid).text() != "-"){
                        $("#chooseContractForm #cz_"+row.htid).removeAttr("class");
                        $("#chooseContractForm #cz_"+row.htid).text("");
                    }
                }else{
                    $("#chooseContractForm #cz_"+row.htid).attr("class","btn btn-success");
                    $("#chooseContractForm #cz_"+row.htid).text("调整明细");
                    if(refresh){
                        // 查询到货明细信息
                        $.ajax({
                            type:'post',
                            url: $("#chooseContractForm input[name='urlPrefix']").val() + "/contract/contract/pagedataGetContractChooseInfo",
                            cache: false,
                            data: {"htid":row.htid,"sffpwh":"0", "access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.htmxDtos != null && result.htmxDtos.length > 0){
                                    for (var i = 0; i < result.htmxDtos.length; i++) {
                                        var sz = {"htid":'',"htmxid":'',"gysid":'',"gysmc":''};
                                        sz.htid = result.htmxDtos[i].htid;
                                        sz.htmxid = result.htmxDtos[i].htmxid;
                                        sz.gysid = row.gys;
                                        sz.gysmc = row.gysmc;
                                        htmxJson.push(sz);
                                    }
                                    $("#chooseContractForm #htmx_json").val(JSON.stringify(htmxJson));
                                    $("#chooseContractForm  #yxdjh").tagsinput('add',{"value":row.htid,"text":row.htnbbh});
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
            sortLastName: "htgl.htid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: $("#chooseContractForm  #zt").val()
            // 搜索框使用
            // search:params.search
        };
        return arrivalSearchData(map);
    }
    return oTableInit
}
function arrivalSearchData(map){
    var cxtj=$("#chooseContractForm #cxtj").val();
    var cxnr=$.trim(jQuery('#chooseContractForm #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["htnbbh"]=cxnr;
    }else if(cxtj=="2"){
        map["htwbbh"]=cxnr;
    }else if (cxtj == "3") {
        map["gysmc"] = cxnr;
    }else if (cxtj == "4") {
        map["wlbm"] = cxnr;
    }else if (cxtj == "5") {
        map["wlmc"] = cxnr;
    }else if (cxtj == "6") {
        map["gg"] = cxnr;
    }
    return map;
}
function searchContractResult(isTurnBack){
    if(isTurnBack){
        $('#chooseContractForm #contract_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#chooseContractForm #contract_list').bootstrapTable('refresh');
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    return "<span id='cz_"+row.htid+"' onclick=\"reviseDetail('" + row.htid + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param htid
 * @param event
 * @returns
 */
function reviseDetail(htid, event){
    if($("#cz_"+htid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $("#chooseContractForm input[name='urlPrefix']").val() + "/contract/contract/pagedataChooseContractInfoList?access_token=" + $("#ac_tk").val() + "&htid=" + htid;
    $.showDialog(url, '选择合同明细', chooseHtmxConfig);
}
var chooseHtmxConfig = {
    width : "1000px",
    modalName	: "chooseHtmxModal",
    formName	: "chooseContractInfoForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseContractInfoForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseContractInfoForm input[name='checkHtmx']:checked").length == 0){
                    $.alert("未选中明细信息！");
                    return false;
                }
                var htmxJson = [];
                var gysid="";
                var gysmc="";
                if($("#chooseContractForm #htmx_json").val()){
                    htmxJson = JSON.parse($("#chooseContractForm #htmx_json").val());
                    gysid=htmxJson[0].gysid;
                    gysmc=htmxJson[0].gysmc;
                }
                var htid = $("#chooseContractInfoForm input[name='htid']").val();
                if(htmxJson.length > 0){
                    for (var i = htmxJson.length-1; i >= 0; i--) {
                        if(htmxJson[i].htid == htid){
                            htmxJson.splice(i,1);
                        }
                    }
                }
                $("#chooseContractInfoForm input[name='checkHtmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"htid":'',"htmxid":'',"gysid":'',"gysmc":''};
                        sz.htid = htid;
                        sz.htmxid = this.dataset.htmxid;
                        sz.gysid = gysid;
                        sz.gysmc = gysmc;
                        htmxJson.push(sz);
                    }
                })
                $("#chooseContractForm #htmx_json").val(JSON.stringify(htmxJson));
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
var ChooseContract_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化已选单据号
        initTagsinput();

        var btn_query=$("#chooseContractForm #btn_query");
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchContractResult(true);
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
    $("#chooseContractForm  #yxdjh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
    // 初始化已选明细json
    $("#chooseContractForm #djhs").val($("#editContractForm #fpmx_json").val());
}

/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseContractForm").on('click','.label-info',function(e){
    event.stopPropagation();
    var url = $("#chooseContractForm input[name='urlPrefix']").val() + "/contract/contract/pagedataChooseContractInfoList?access_token=" + $("#ac_tk").val() + "&htid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择合同明细', chooseHtmxConfig);
});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseContractForm #yxdjh').on('beforeItemRemove', function(event) {
    var htmxJson = [];
    if($("#chooseContractForm #htmx_json").val()){
        htmxJson = JSON.parse($("#chooseContractForm #htmx_json").val());
    }
    var htid = event.item.value;
    if(htmxJson.length > 0){
        for (var i = htmxJson.length-1; i >= 0; i--) {
            if(htmxJson[i].htid == htid){
                htmxJson.splice(i,1);
            }
        }
    }
    $("#chooseContractForm #htmx_json").val(JSON.stringify(htmxJson));
});


$(function(){
    // 1.初始化Table
    var oTable = new ChooseContract_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ChooseContract_ButtonInit();
    oButtonInit.Init();
    jQuery('#chooseContractForm .chosen-select').chosen({width: '100%'});
})