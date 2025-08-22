var zdxz = $("#ajaxForm #zdxz").val();
var flag = $("#ajaxForm #flag").val()
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
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '5%',
        align: 'left',
        formatter:wlbmFormatter,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'wsdj',
        title: '无税单价',
        titleTooltip:'无税单价',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'jsze',
        title: '价税总额',
        titleTooltip:'价税总额',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'wszje',
        title: '无税总金额',
        titleTooltip:'无税总金额',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'dky',
        title: '到款月',
        titleTooltip:'到款月',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'dkje',
        title: '到款金额',
        titleTooltip:'到款金额',
        width: '8%',
        align: 'left',
        visible: true,
    }, {
        field: 'suil',
        title: '税率',
        titleTooltip:'税率',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'yfhrq',
        title: '预发货日期',
        titleTooltip:'预发货日期',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'cplx',
        title: '产品类型',
        titleTooltip:'产品类型',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'yfhsl',
        title: '已发货数量',
        titleTooltip:'已发货数量',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'sczt',
        title: '生产状态',
        titleTooltip:'生产状态',
        width: '6%',
        formatter:scztFormat,
        align: 'left',
        visible: true
    }];
// 限制字段
var xzColumnsArray = [
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
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '5%',
        align: 'left',
        formatter:wlbmFormatter,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'suil',
        title: '税率',
        titleTooltip:'税率',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'yfhrq',
        title: '预发货日期',
        titleTooltip:'预发货日期',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'cplx',
        title: '产品类型',
        titleTooltip:'产品类型',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'yfhsl',
        title: '已发货数量',
        titleTooltip:'已发货数量',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'sczt',
        title: '生产状态',
        titleTooltip:'生产状态',
        width: '6%',
        formatter:scztFormat,
        align: 'left',
        visible: true
    }];
var contractEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #xsmx_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/storehouse/sale/pagedataGetViewSaleMx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlmc",				//排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "xsmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: '0'==zdxz?columnsArray:xzColumnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
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
            sortLastName: "wlbm", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            xsid:$("#ajaxForm #xsid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
var columnsArray1 = [
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
        field: 'kdgs',
        title: '快递公司',
        titleTooltip:'快递公司',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'wldh',
        title: '快递单号',
        titleTooltip:'快递单号',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'fhrq',
        title: '发货日期',
        titleTooltip:'发货日期',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'qsrq',
        title: '签收时间',
        titleTooltip:'签收时间',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'yf',
        title: '运费(元)',
        titleTooltip:'运费(元)',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'sfqr',
        title: '是否确认',
        titleTooltip:'是否确认',
        width: '5%',
        align: 'left',
        formatter:sfqrformat,
        visible: true
    },{
        field: 'fjids',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
    }];
var logisticsUpholdView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #tb_list').bootstrapTable({
            url:$('#ajaxForm #urlPrefix').val()+'/storehouse/requisition/pagedataGetWlxxByYwid?ywid='+$('#ajaxForm #xsid').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#logisticsUpholdForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlxx.lrsj",				        //排序字段
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
            uniqueId: "wlxxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray1,
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
            sortLastName: "wldh", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
/**
 * 生产状态
 */
function scztFormat(value,row,index) {
    if (row.sczt == '1') {
        return '需要生产';
    }else if (row.sczt == '0')
        return "无需生产";
    else return "";

}
function sfqrformat(value,row,index) {
    if (row.sfqr == '1') {
        return "是";
    }else if (row.sfqr == '0') {
        return "否";
    }else {
        return "";
    }
}

/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    html += "<input type='hidden' id='wlfj_"+index+"' name='wlfj_"+index+"'/>";
    if(row.fjbj == "0"){
        html += "<a href='javascript:void(0);' onclick='ViewLogisFile(\"" + index + "\",\""+row.wlxxid+"\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='ViewLogisFile(\"" + index + "\",\""+row.wlxxid+"\")' >否</a>";
    }
    html += "</div>";
    return html;
}
function ViewLogisFile(index,zywid) {
    url="/inspectionGoods/pendingInspection/pagedataGetUploadFilePage?access_token=" + $("#ac_tk").val()+"&zywid="+zywid+"&ywid="+$("#ajaxForm #xsid").val()+"&ckbj=1";
    $.showDialog($("#ajaxForm #urlPrefix").val()+url, '查看附件', viewFileConfig);
}

var viewFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    formName	: "view_ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
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
function queryByWlbm(wlid){
    var url=$("#ajaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}
function viewYxht(htid){
    var url=$("#ajaxForm #urlPrefix").val()+"/marketingContract/marketingContract/pagedataViewMarketingContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'营销合同信息',viewMarketingContractConfig);
}
var viewMarketingContractConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function wlbmFormatter(value,row,index) {
    var html = "";
    if(row.wlbm==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";

    }
    return html;
}
var viewWlConfig = {
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
function queryByJydh(jcjyid ){
    var url=$("#ajaxForm #urlPrefix").val()+"/borrowing/borrowing/pagedataViewBorrowing?jcjyid="+jcjyid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'借用信息',ViewBorrowConfig);
}
var ViewBorrowConfig = {
    width		: "1600px",
    modalName	:"ViewBorrowConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function EditMoneyHos(fywid,ywid,ywlx,djrq,xsid,dysyje,hkzq){
    var url=$("#ajaxForm #urlPrefix").val()+"/storehouse/sale/pagedataMoneyHos?fywid="+fywid+"&ywid="+ywid+"&ywlx="+ywlx+"&djrq="+djrq+"&xsid="+xsid+"&dysyje="+dysyje+"&hkzq="+hkzq+"&flag="+flag+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到款记录',moneyHistorysFormConfig);
}

//到款记录
var moneyHistorysFormConfig = {
    width		: "1000px",
    modalName	: "MoneyHosModal",
    formName	: "MoneyHosForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
$(document).ready(function(){
    //初始化列表
    var oTable=new contractEdit_TableInit();
    oTable.Init();
    var oTable1=new logisticsUpholdView_TableInit();
    oTable1.Init();
});
