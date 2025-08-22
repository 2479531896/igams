var t_map = [];
// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'fpmxid',
        title: '发票明细ID',
        titleTooltip:'发票明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'u8rkdh',
        title: 'U8入库单号',
        titleTooltip:'U8入库单号',
        width: '8%',
        align: 'left',
        formatter:u8rkdhformat,
        visible: true
    },{
        field: 'htnbbh',
        title: '合同单号',
        titleTooltip:'合同单号',
        width: '10%',
        align: 'left',
        formatter:htdhformat,
        visible: true
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '7%',
        formatter:MaterialCodeformat,
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '7%',
        align: 'left',
        visible: true
    },{
        field: 'gg',
        title: '规格/标准',
        titleTooltip:'规格/标准',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        formatter:slformat,
        visible: true
    }, {
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'wwhsl',
        title: '未维护数量',
        titleTooltip:'未维护数量',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'qglbdm',
        title: '请购类别',
        titleTooltip:'请购类别',
        formatter:qglbformat,
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'hjje',
        title: '总金额',
        titleTooltip:'总金额',
        width: '5%',
        align: 'left',
        formatter:zjeformat,
        visible: true
    }];
var invoiceAudit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#auditInvoiceForm #tb_list').bootstrapTable({
            url: $('#auditInvoiceForm #urlPrefix').val()+'/invoice/invoice/pagedataGetInvoiceDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#auditInvoiceForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "xh",				//排序字段
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
            uniqueId: "fpmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {

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
            sortLastName: "fpmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            ids: $("#auditInvoiceForm #ids").val(),
            fpid: $("#auditInvoiceForm #fpid").val(),
            sffpwh:"0",
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

/**
 * 请购类别格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qglbformat(value,row,index){
    if(row.qglbdm == null){
        row.qglbdm = "";
    }
    var html ="";
    if(row.qglbdm=='MATERIAL'){
        html += "<span>物料</span>";
    }else  if(row.qglbdm=='ADMINISTRATION'){
        html += "<span>行政</span>";
    }else  if(row.qglbdm=='SERVICE'){
        html += "<span>服务</span>";
    }else  if(row.qglbdm=='DEVICE'){
        html += "<span>设备</span>";
    }

    return html;
}


/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function MaterialCodeformat(value,row,index){
    var html = "";
    if(row.wlbm)
        html += "<a href='javascript:void(0);' onclick=\"queryByMaterialCode('"+row.wlid+"')\">"+row.wlbm+"</a>";
    return html;
}
function queryByMaterialCode(wlid){
    var url=$("#auditInvoiceForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewMaterialCodeConfig);
}
var viewMaterialCodeConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * U8入库单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function u8rkdhformat(value,row,index){
    var html="";
    if(row.fpmxDtos!=null&&row.fpmxDtos.length>0){
        for(var i=0;i<row.fpmxDtos.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            if(row.fpmxDtos[i].lbcskz1!=null&&row.fpmxDtos[i].lbcskz1!=''){
                if(row.fpmxDtos[i].u8rkdh!=null&&row.fpmxDtos[i].u8rkdh!=''){
                    html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByDhdh('"+row.fpmxDtos[i].dhid+"')\">"+row.fpmxDtos[i].u8rkdh+"</a>";
                }
            }else{
                if(row.fpmxDtos[i].u8rkdh!=null&&row.fpmxDtos[i].u8rkdh!=''){
                    html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByRkid('"+row.fpmxDtos[i].rkid+"')\">"+row.fpmxDtos[i].u8rkdh+"</a>";
                }
            }
            html+="</span>";
        }
    }
    return html;
}

function getInfoByDhdh(dhid){
    var url=$("#auditInvoiceForm #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到货信息',viewDhConfig);
}
var viewDhConfig = {
    width		: "1600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function getInfoByRkid(rkid){
    var url=$("#auditInvoiceForm #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'入库信息',viewHwConfig);
}
var viewHwConfig={
    width		: "1600px",
    modalName	:"viewHwModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 查看详情格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function htdhformat(value,row,index){
    var html="";
    if(row.htid!=null && row.htid!=''){
        html="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryByHtid('"+row.htid+"')\">"+row.htnbbh+"</a>";
    }
    return html;
}

function queryByHtid(htid){
    var url=$("#auditInvoiceForm #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'合同信息',viewHtConfig);
}

var viewHtConfig={
    width		: "1500px",
    modalName	:"viewHtModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function slformat(value,row,index){
    if(row.sl == null){
        row.sl = "";
    }
    var html ="";
    html += "<span>"+row.sl+"</span>";
    if(row.mxsl == null){
        row.mxsl = "";
    }else{
        row.mxsl=parseFloat( row.mxsl).toFixed(2);
    }
    html += "<div style='background-color: green'><p  id='mxsl_"+index+"' style='font-size:14px;width:100%;margin-left:3px;'>/"+row.mxsl+"</p></div>";
    return html;
}



/**
 * 总金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zjeformat(value,row,index){
    if(row.hjje == null){
        row.hjje = "";
    }
    var html =""
    html += "<span>"+row.hjje+"</span>";
    return html;
}


/**
 * 初始化页面
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#auditInvoiceForm #kprq'
        ,theme: '#2381E9'
    });
}


$(document).ready(function(){
    //初始化列表
    var oTable=new invoiceAudit_TableInit();
    oTable.Init();
    init();
    //所有下拉框添加choose样式
    jQuery('#auditInvoiceForm .chosen-select').chosen({width: '100%'});
});