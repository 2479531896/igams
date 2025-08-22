var t_map=[];
var kwlist=[];
var rows=[];
var allocationCar_TableInit = function () {
    $.ajax({
        async: false,
        type:'post',
        url:$('#allocationCar_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataAllocationCar",
        cache: false,
        data: {"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data.kwlist!=null && data.kwlist.length>0){
                kwlist=data.kwlist;
                rows=data.rows;
            }
        }
    });
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#allocationCar_formSearch #allocationCar_list').bootstrapTable({
            url: $("#allocationCar_formSearch #urlPrefix").val()+'/storehouse/stock/pagedataAllocationCar',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#allocationCar_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"hwmc",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "xzkcid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [
           {
                field: 'hwmc',
                title: '货物名称',
                width: '2%',
                align: 'left',
                visible: true
            }, {
                field: 'kw',
                title: '调出库位',
                width: '2%',
                align: 'left',
                visible: true
            }, {
                field: 'drkw',
                title: '调入库位',
                width: '2%',
                align: 'left',
                formatter:drkwformat,
                visible: true
            },  {
                    field: 'klsl',
                    title: '可调拨数量',
                    width: '2%',
                    align: 'left',
                    visible: true
                }, {
                field: 'dbsl',
                title: '调拨数量',
                width: '2%',
                align: 'left',
                formatter:dbslformat,
                visible: true
            }, {
                    field: 'cz',
                    title: '操作',
                    width: '1%',
                    align: 'left',
                    formatter: allocationCarForm_czformat,
                    visible: true
                }

            ],
            onLoadSuccess: function (map) {
                t_map=map;
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
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
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "xzkcid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
};
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function allocationCarForm_czformat(value,row,index){
    var html="";
    html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='从调拨车中删除' onclick=\"delFromCar('" + index + "','"+row.xzkcid+"',event)\" >删除</span>";
    return html;
}
function drkwformat(value,row,index){
    var html="";
    if(kwlist!=null&& kwlist.length>0){
        html="<select name='drkw' onchange=\"changekw('"+index+"',this,\'drkw\')\"  class='form-control chosen-select' id='drkw"+index+"' style='padding:0px;'>";
        for(var i=0;i<kwlist.length;i++){
            if(row.kw){
                if(row.kwid==kwlist[i].csid){
                    html+="<option selected='true' id='"+kwlist[i].csid+"' value='"+kwlist[i].csid+"' index='"+i+"'>"+kwlist[i].csmc+"</option>";
                }else{
                    html+="<option id='"+kwlist[i].csid+"' value='"+kwlist[i].csid+"' index='"+i+"'>"+kwlist[i].csmc+"</option>";
                }
            }else{
                html+="<option id='"+kwlist[i].csid+"' value='"+kwlist[i].csid+"' index='"+i+"'>"+kwlist[i].csmc+"</option>";
                if (rows.length > 0){
                    rows[index].drkw=kwlist[i].csid;
                }
            }
        }
        html+="</select>";
    }
    return html;
}
function dbslformat(value,row,index){
    if(row.dbsl == null){
        row.dbsl = "";
    }
    var html = "<input id='dbsl"+index+"' value='"+row.dbsl+"' name='dbsl"+index+"' onblur=\"checkSl('"+index+"',this,\'dbsl\')\" type='number' validate='{dbsl:true}'></input>";
    return html;
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function changekw(index, e, zdmc){

    t_map.rows[index].drkw = e.value;

}
function checkSl(index, e, zdmc) {
    t_map.rows[index].dbsl = e.value;
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("dbsl", function (value, element){
    if(!value){
        $.error("请填写数量!");
        return false;
    }else{
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确数量格式，可保留两位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");


/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delFromCar(index,xzkcid,event){
    var preJson = t_map.rows;
    for (let i = 0; i < preJson.length; i++) {
        if (preJson[i].xzkcid == xzkcid){
            preJson.splice(i,1);
            break
        }
    }
    $("#allocationCar_formSearch #allocationCar_list").bootstrapTable('load',t_map);
    $("#allocationCar_formSearch #dbmx_json").val(JSON.stringify(preJson));
}


$(function(){

    //0.界面初始化
    // 1.初始化Table
    var oTable = new allocationCar_TableInit();
    oTable.Init();
    // 所有下拉框添加choose样式
    jQuery('#allocationCar_formSearch .chosen-select').chosen({width: '100%'});
});