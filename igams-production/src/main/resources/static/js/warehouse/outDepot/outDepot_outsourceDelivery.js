var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
    {
        title: '序',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序',
        width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'ylsl',
        title: '应出数量',
        titleTooltip:'应出数量',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'klsl',
        title: '可出数量',
        titleTooltip:'可出数量',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var outsourceDelivery_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#outsourceDeliveryForm #tb_list').bootstrapTable({
            url: $('#outsourceDeliveryForm #urlPrefix').val()+$('#outsourceDeliveryForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#outsourceDeliveryForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlbm",					//排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map=map;
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "wlid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            //搜索框使用
            //search:params.search
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
function czformat(value,row,index){
    var html="";
    html = html+"<span id='ckid_"+index+"' style='margin-left:5px;height:24px !important;' class='btn btn-success' title='选择库存' onclick=\"chooseStock('" + index + "','"+row.wlid+"')\" >选择库存</span>";
    html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除信息' onclick=\"delDetail('" + index + "','"+row.qgmxid+"',event)\" >删除</span>";
    return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delDetail(index,value,event){
    t_map.rows.splice(index,1);
    $("#outsourceDeliveryForm #tb_list").bootstrapTable('load',t_map);
}


/**
 * 选择库存列表
 * @returns
 */
function chooseStock(index,wlid){
    t_index=index;
    var ck=$('#outsourceDeliveryForm #ck').val();
    if(!ck){
        $.error("请选择仓库！");
        return;
    }
    var url=$('#outsourceDeliveryForm #urlPrefix').val() + "/warehouse/outDepot/pagedataChooseStockList?access_token=" + $("#ac_tk").val()+"&wlid="+wlid+"&index="+t_index+"&ckid="+$('#outsourceDeliveryForm #ck').val();
    $.showDialog(url,'选择库存',chooseStockConfig);
}
var chooseStockConfig = {
    width : "1550px",
    modalName	: "chooseStockModal",
    formName	: "chooseStockForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseStockForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取更改后的请购单和明细信息
                var hwJson = [];
                var Json = [];
                var ckJson=[];
                if($("#chooseStockForm #hwxx_json").val()){
                    hwJson = JSON.parse($("#chooseStockForm #hwxx_json").val());
                    for (var i = 0; i < hwJson.length; i++) {
                        var kcsl = parseFloat(hwJson[i].kcsl);
                        var sl = parseFloat(hwJson[i].sl);
                        if(kcsl<sl){
                            $.error("数量不得大于可出数量！")
                            return false;
                        }
                        var sz = {"hwid":'',"wlid":'',"kcl":'',"sl":'',"ckkcl":'',"ckhwid":'',"kcsl":'',"wlmc":'',"scph":''};
                        sz.hwid = hwJson[i].hwid;
                        sz.wlid = hwJson[i].wlid;
                        sz.kcl = hwJson[i].kcl;
                        sz.sl = hwJson[i].sl;
                        sz.ckkcl = hwJson[i].ckkcl;
                        sz.ckhwid = hwJson[i].ckhwid;
                        sz.kcsl = hwJson[i].kcsl;
                        sz.wlmc = hwJson[i].wlmc;
                        sz.scph = hwJson[i].scph;
                        Json.push(sz);
                    }
                }
                t_map.rows[t_index].ckJson = JSON.stringify(Json);
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
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#ckrq'
        ,theme: '#2381E9'
    });
}



/**
 * 选择部门列表
 * @returns
 */
function chooseBm(){
    var url=$('#outsourceDeliveryForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择部门',addBmConfig);
}
var addBmConfig = {
    width		: "800px",
    modalName	:"addBmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#outsourceDeliveryForm #bm').val(sel_row[0].jgid);
                    $('#outsourceDeliveryForm #bmmc').val(sel_row[0].jgmc);
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 选择明细
 * @returns
 */
function chooseContract(){
    var url = $('#outsourceDeliveryForm #urlPrefix').val() + "/warehouse/outDepot/pagedataChooseContractDetailList?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择合同单', chooseContractConfig);
}
var chooseContractConfig = {
    width : "1000px",
    modalName	: "chooseContractModal",
    formName	: "chooseContractDetailForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseContractDetailForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取更改后的请购单和明细信息
                var htmxJson = [];
                if($("#chooseContractDetailForm #htmx_json").val()){
                    htmxJson = JSON.parse($("#chooseContractDetailForm #htmx_json").val());
                    for (var i = 0; i < htmxJson.length; i++) {
                        htmxJson[i].ckJson=[];
                        var isAdd = true;
                        for (var j = 0; j < t_map.rows.length; j++) {
                            if(t_map.rows[j].cpjgmxid == htmxJson[i].cpjgmxid){
                                isAdd = false;
                                break;
                            }
                        }
                        if(isAdd){
                            t_map.rows.push(htmxJson[i]);
                        }
                    }
                    $("#outsourceDeliveryForm #tb_list").bootstrapTable('load',t_map);
                    $.closeModal(opts.modalName);
                }else{
                    $.alert("未获取到选中信息！");
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};




$(document).ready(function(){
    //初始化列表
    var oTable=new outsourceDelivery_TableInit();
    oTable.Init();
    // 初始化页面
    init();
    jQuery('#outsourceDeliveryForm .chosen-select').chosen({width: '100%'});
});