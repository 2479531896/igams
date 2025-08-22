

var columnsArray = [
    {
        checkbox: true
    },{
        field: 'htmxid',
        title: '合同明细ID',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: false
    },{
        field: 'htnbbh',
        title: '合同单号',
        width: '16%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'gysmc',
        title: '供应商',
        width: '16%',
        align: 'left',
        visible:true
    },{
        field: 'wlbm',
        title: '物料编码',
        width: '12%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'wlmc',
        title: '物料名称',
        width: '20%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'sl',
        title: '数量',
        width: '10%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '16%',
        align: 'left',
        formatter:czFormat,
        visible: true
    }]
var ChooseContractDetail_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#chooseContractDetailForm #detail_list").bootstrapTable({
            url: $("#chooseContractDetailForm input[name='urlPrefix']").val() + '/warehouse/outDepot/pagedataGetPageListContractDetail',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseContractDetailForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "htgl.htnbbh",				//排序字段
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
            uniqueId: "htmxid",                     //每一行的唯一标识，一般为主键列
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
                if($("#chooseContractDetailForm #cz_"+row.htmxid).text()){
                    if($("#chooseContractDetailForm #cz_"+row.htmxid).text() != "-"){
                        if($("#chooseContractDetailForm #htmx_json").val()){
                            var Json = JSON.parse($("#chooseContractDetailForm #htmx_json").val());
                            if(Json.length > 0){
                                for (var i = Json.length-1; i >= 0; i--) {
                                    if(Json[i].htmxid == row.htmxid){
                                        Json.splice(i,1);
                                    }
                                }
                                $("#chooseContractDetailForm #htmx_json").val(JSON.stringify(Json));
                            }
                        }
                        $("#chooseContractDetailForm #cz_"+row.htmxid).removeAttr("class");
                        $("#chooseContractDetailForm #cz_"+row.htmxid).text("");
                    }
                }else{
                    // 判断是否更新信息
                    var htmxJson = [];
                    var refresh = true;
                    if($("#chooseContractDetailForm #htmx_json").val()){
                        htmxJson = JSON.parse($("#chooseContractDetailForm #htmx_json").val());
                        if(htmxJson.length > 0){
                            var htnbbh=htmxJson[0].htnbbh;
                            for (var i = htmxJson.length-1; i >= 0; i--) {
                                if(row.htnbbh != htnbbh){
                                    $.error("请选择同一个合同单的数据！");
                                    return;
                                }
                                if(htmxJson[i].htmxid == row.htmxid){
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
                            url: $("#chooseContractDetailForm input[name='urlPrefix']").val() + "/warehouse/outDepot/pagedataGetContractDetailWithStructure",
                            cache: false,
                            data: {"htmxid":row.htmxid, "access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.htmxDtos != null && result.htmxDtos.length > 0){
                                    for (var i = 0; i < result.htmxDtos.length; i++) {
                                        var sz={"htmxid":'',"wlid":'',"gys":'',"cpjgmxid":'',"wlmc":'',"wlbm":'',"gg":'',"jldw":'',"ylsl":'',"klsl":'',"htnbbh":''};
                                        sz.htmxid = result.htmxDtos[i].htmxid;
                                        sz.wlid = result.htmxDtos[i].wlid;
                                        sz.gys = result.htmxDtos[i].gys;
                                        sz.cpjgmxid = result.htmxDtos[i].cpjgmxid;
                                        sz.wlmc = result.htmxDtos[i].wlmc;
                                        sz.wlbm = result.htmxDtos[i].wlbm;
                                        sz.gg = result.htmxDtos[i].gg;
                                        sz.jldw = result.htmxDtos[i].jldw;
                                        sz.ylsl = result.htmxDtos[i].ylsl;
                                        sz.klsl = result.htmxDtos[i].klsl;
                                        sz.htnbbh = result.htmxDtos[i].htnbbh;
                                        htmxJson.push(sz);
                                    }
                                    $("#chooseContractDetailForm #htmx_json").val(JSON.stringify(htmxJson));
                                    $("#chooseContractDetailForm #cz_"+row.htmxid).attr("class","btn btn-success");
                                    $("#chooseContractDetailForm #cz_"+row.htmxid).text("调整明细");
                                }else{
                                    $.error("此条数据明细为空！");
                                    return;
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
            sortLastName: "htmx.htmxid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return contractDetailSearchData(map);
    }
    return oTableInit
}
function contractDetailSearchData(map){
    var cxtj=$("#chooseContractDetailForm #cxtj").val();
    var cxnr=$.trim(jQuery('#chooseContractDetailForm #cxnr').val());
    if(cxtj=="0"){
        map["htnbbh"]=cxnr
    }else if(cxtj=="1"){
        map["gysmc"]=cxnr
    }else if(cxtj=="2"){
        map["wlmc"]=cxnr
    }else if(cxtj=="3"){
        map["wlbm"]=cxnr
    }
    return map;
}
function searchContractDetailResult(isTurnBack){
    if(isTurnBack){
        $('#chooseContractDetailForm #detail_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#chooseContractDetailForm #detail_list').bootstrapTable('refresh');
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    var flag=false;
    if($("#chooseContractDetailForm #htmx_json").val()){
        var htmxJson = JSON.parse($("#chooseContractDetailForm #htmx_json").val());
        if(htmxJson.length > 0){
            for (var i = htmxJson.length-1; i >= 0; i--) {
                if(htmxJson[i].htmxid == row.htmxid){
                    flag = true;
                    break;
                }
            }
        }
    }
    if(flag){
        return "<span id='cz_"+row.htmxid+"' onclick=\"reviseDetail('" + row.htmxid + "',event)\" class='btn btn-success'>调整明细</span>";
    }else{
        return "<span id='cz_"+row.htmxid+"' onclick=\"reviseDetail('" + row.htmxid + "',event)\"/>";
    }
}
/**
 * 调整明细点击事件
 * @param qgid
 * @param event
 * @returns
 */
function reviseDetail(htmxid, event){
    if($("#cz_"+htmxid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $("#chooseContractDetailForm input[name='urlPrefix']").val() + "/warehouse/outDepot/pagedataChooseProductStructureInfo?access_token=" + $("#ac_tk").val() + "&htmxid=" + htmxid;
    $.showDialog(url, '选择生产结构明细', chooseProductStructureConfig);
}
var chooseProductStructureConfig = {
    width : "1000px",
    modalName	: "chooseProductStructureModal",
    formName	: "chooseProductStructureForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseProductStructureForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseProductStructureForm input[name='checkCpjgmx']:checked").length == 0){
                    $.alert("未选中明细信息！");
                    return false;
                }
                var htmxJson = [];
                if($("#chooseContractDetailForm #htmx_json").val()){
                    htmxJson = JSON.parse($("#chooseContractDetailForm #htmx_json").val());
                }
                var htmxid = $("#chooseProductStructureForm input[name='htmxid']").val();
                if(htmxJson.length > 0){
                    for (var i = htmxJson.length-1; i >= 0; i--) {
                        if(htmxJson[i].htmxid == htmxid){
                            htmxJson.splice(i,1);
                        }
                    }
                }
                $("#chooseProductStructureForm input[name='checkCpjgmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"htmxid":'',"wlid":'',"gys":'',"cpjgmxid":'',"wlmc":'',"wlbm":'',"gg":'',"jldw":'',"ylsl":'',"klsl":''};
                        sz.htmxid = htmxid;
                        sz.wlid = this.dataset.wlid;
                        sz.gys = this.dataset.gys;
                        sz.cpjgmxid = this.dataset.cpjgmxid;
                        sz.wlmc =this.dataset.wlmc;
                        sz.wlbm =this.dataset.wlbm;
                        sz.gg = this.dataset.gg;
                        sz.jldw = this.dataset.jldw;
                        sz.ylsl = this.dataset.ylsl;
                        sz.klsl =this.dataset.klsl;
                        htmxJson.push(sz);
                    }
                })
                $("#chooseContractDetailForm #htmx_json").val(JSON.stringify(htmxJson));
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
var ChooseContractDetail_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        initTagsinput();
        var btn_query=$("#chooseContractDetailForm #btn_query");
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchContractDetailResult(true);
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

}


$(function(){
    // 1.初始化Table
    var oTable = new ChooseContractDetail_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ChooseContractDetail_ButtonInit();
    oButtonInit.Init();
    jQuery('#chooseContractDetailForm .chosen-select').chosen({width: '100%'});
})