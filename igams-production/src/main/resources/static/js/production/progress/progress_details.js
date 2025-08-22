var progress_details_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#progress_details #progress_details_tb_list').bootstrapTable({
            url: $('#progress_details #urlPrefix').val() + '/progress/progress/pagedataWaitProgress',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#progress_details #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "cpxqjh.lrsj",				//排序字段
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
            uniqueId: "xqjhmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            }, {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'kcl',
                title: '库存量',
                titleTooltip:'库存量',
                width: '8%',
                align: 'left',
                visible: true,
                formatter:kclformat
            }, {
                field: 'sl',
                title: '需求数量',
                titleTooltip:'预计用途',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'jldw',
                title: '单位',
                titleTooltip:'备注',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '10%',
                align: 'left',
                formatter:czFormat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
                if($("#progress_details #cz_"+row.xqjhmxid).text()){
                    if($("#progress_details #cz_"+row.xqjhmxid).text() != "-"){
                        $("#progress_details #cz_"+row.xqjhmxid).removeAttr("class");
                        $("#progress_details #cz_"+row.xqjhmxid).text("");
                    }
                }else{
                    $("#progress_details #cz_"+row.xqjhmxid).attr("class","btn btn-success");
                    $("#progress_details #cz_"+row.xqjhmxid).text("调整明细");
                    // 判断是否更新请购信息
                    var bommx_json = [];
                    var refresh = true;
                    if($("#progress_details #bommx_json").val()){
                        bommx_json = JSON.parse($("#progress_details #bommx_json").val());
                        if(bommx_json.length > 0){
                            for (var i = bommx_json.length-1; i >= 0; i--) {
                                if(bommx_json[i].xqjhmxid == row.xqjhmxid){
                                    refresh = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(refresh){
                        $.ajax({
                            type:'post',
                            url: $("#progress_details input[name='urlPrefix']").val() + "/progress/progress/pagedataListBommxInfo",
                            cache: false,
                            data: {"xqjhmxid":row.xqjhmxid, "access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.dtoList != null && result.dtoList.length > 0){
                                    for (var i = 0; i < result.dtoList.length; i++) {
                                        var sz = {"xqjhmxid":'',"bommxid":''};
                                        sz.xqjhmxid = row.xqjhmxid;
                                        sz.bommxid = result.dtoList[i].bommxid;
                                        bommx_json.push(sz);
                                    }
                                    $("#progress_details #bommx_json").val(JSON.stringify(bommx_json));
                                        $("#progress_details  #xqdh").tagsinput('add',{"value":row.xqjhmxid,"text":row.xqdh});
                                }
                            }
                        });
                    }
                }
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
            sortLastName: "cpxqjh.cpxqid", //防止同名排位用
            flag: "80", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return progress_detailsSearchData(map);
    };
    return oTableInit;
};
function kclformat(value,row,index){
    if (row.kcl){
        return row.kcl;
    }
    return "";
}
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    return "<span id='cz_"+row.xqjhmxid+"' onclick=\"reviseXqDetail('" + row.xqjhmxid + "',event)\"/>";
}
function searchProgress_detailsResult(isTurnBack){
    if(isTurnBack){
        $('#progress_details #progress_details_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#progress_details #progress_details_tb_list').bootstrapTable('refresh');
    }
}

/**
 * 调整明细点击事件
 * @param qgid
 * @param event
 * @returns
 */
function reviseXqDetail(xqjhmxid,event){
    if($("#cz_"+xqjhmxid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $("#progress_details input[name='urlPrefix']").val() + "/progress/progress/pagedataChooseListBommxInfo?access_token=" + $("#ac_tk").val()+"&xqjhmxid="+xqjhmxid;
    $.showDialog(url, '选择明细', chooseXqmxConfig);
}

function progress_detailsSearchData(map){
    var cxtj = $("#progress_details #cxtj").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#progress_details #cxnr').val());
    // '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
    if (cxtj == "0") {
        map["entire"] = cxnr;
    }else if (cxtj == "1") {
        map["xqdh"] = cxnr;
    }else if (cxtj == "2") {
        map["sqbmmc"] = cxnr;
    }else if (cxtj == "3") {
        map["sqrmc"] = cxnr;
    }else if (cxtj == "4") {
        map["wlbm"] = cxnr;
    }else if (cxtj == "5") {
        map["wlmc"] = cxnr;
    }
    // 需求开始日期
    var xqrqstart = jQuery('#progress_details #xqrqstart').val();
    map["xqrqstart"] = xqrqstart;
    // 需求结束日期
    var xqrqend = jQuery('#progress_details #xqrqend').val();
    map["xqrqend"] = xqrqend;

    return map;
}


var progress_details_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query = $("#progress_details #btn_query");

        //添加日期控件
        laydate.render({
            elem: '#progress_details #xqrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#progress_details #xqrqend'
            ,theme: '#2381E9'
        });
        initTagsinput();
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchProgress_detailsResult(true);
            });
        }
    };

    return oInit;
};
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#progress_details #xqdh').on('beforeItemRemove', function(event) {
    var bommx_json = [];
    if($("#progress_details #bommx_json").val()){
        bommx_json = JSON.parse($("#progress_details #bommx_json").val());
    }
    var xqjhmxid = event.item.value;
    if(bommx_json.length > 0){
        for (var i = bommx_json.length-1; i >= 0; i--) {
            if(bommx_json[i].xqjhmxid == xqjhmxid){
                bommx_json.splice(i,1);
            }
        }
    }
    $("#progress_details #bommx_json").val(JSON.stringify(bommx_json));
});

var chooseXqmxConfig = {
    width : "1000px",
    modalName	: "chooseBommxModal",
    formName	: "chooseBommxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseBommxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseBommxForm input[name='checkBomx']:checked").length == 0){
                    $.alert("未选中Bom明细信息！");
                    return false;
                }
                var bommx_json = [];
                if($("#progress_details #bommx_json").val()){
                    bommx_json = JSON.parse($("#progress_details #bommx_json").val());
                }
                var xqjhmxid = $("#chooseBommxForm input[name='xqjhmxid']").val();
                if(bommx_json.length > 0){
                    for (var i = bommx_json.length-1; i >= 0; i--) {
                        if(bommx_json[i].xqjhmxid == xqjhmxid){
                            bommx_json.splice(i,1);
                        }
                    }
                }
                $("#chooseBommxForm input[name='checkBomx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"xqjhmxid":'',"xqjhmxid":''};
                        sz.xqjhmxid = xqjhmxid;
                        sz.bommxid = this.dataset.bommxid;
                        bommx_json.push(sz);
                    }
                })
                $("#progress_details #bommx_json").val(JSON.stringify(bommx_json));
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
 * 监听标签点击事件
 */
var tagClick = $("#progress_details").on('click','.label-info',function(e){

    event.stopPropagation();
    var url = $("#progress_details input[name='urlPrefix']").val() + "/progress/progress/pagedataChooseListBommxInfo?access_token=" + $("#ac_tk").val() + "&xqjhmxid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择明细', chooseXqmxConfig);
});
/**
 * 初始化需求单号
 * @returns
 */
function initTagsinput(){
    $("#progress_details  #xqdh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
}
$(function(){
    //1.初始化Table
    var oTable = new progress_details_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new progress_details_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#progress_details .chosen-select').chosen({width: '100%'});

});
