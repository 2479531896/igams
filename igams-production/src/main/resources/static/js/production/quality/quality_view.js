// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'zlxymxid',
        title: '质量协议明细id',
        titleTooltip:'质量协议明细id',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'zlxyid',
        title: '质量协议id',
        titleTooltip:'质量协议id',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料id',
        titleTooltip:'物料id',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '8%',
        align: 'left',
        formatter:wlbmformat,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'sjxmhmc',
        title: '涉及项目号',
        titleTooltip:'涉及项目号',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'jszb',
        title: '技术指标',
        titleTooltip:'技术指标',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'zlyq',
        title: '质量要求',
        titleTooltip:'质量要求',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'ysbz',
        title: '验收标准',
        titleTooltip:'验收标准',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'zt',
        title: '状态',
        titleTooltip:'状态',
        width: '4%',
        align: 'left',
        visible: true,
        formatter:ztformat
    }];
var qualityView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#qualityViewForm #qualitymx_list').bootstrapTable({
            url: $("#qualityViewForm #urlPrefix").val()+'/agreement/agreement/pagedataAgreementMx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#qualityViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "zlxymx.lrsj",				//排序字段
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
            uniqueId: "zlxymxid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "zlxymx.xgsj", //防止同名排位用
            sortLastOrder: "desc", //防止同名排位用
            zlxyid: $("#qualityViewForm #zlxyid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
// 显示字段
var columnsArray_t = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'wlbm',
        title: '物料编码',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'wlmc',
        title: '物料名称',
        width: '10%',
        align: 'left',
        formatter:view_wlmcformat,
        visible: true
    },{
        field: 'scs',
        title: '生产厂家',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'sjxmhmc',
        title: '涉及项目号',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'jszb',
        title: '技术指标',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'zlyq',
        title: '质量要求',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'ysbz',
        title: '验收标准',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'zlxybh',
        title: '质量协议编号',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'cjsj',
        title: '创建时间',
        width: '7%',
        align: 'left',
        visible: true
    },{
        field: 'kssj',
        title: '开始时间',
        width: '7%',
        align: 'left',
        visible: true
    },{
        field: 'dqsj',
        title: '到期时间',
        width: '7%',
        align: 'left',
        visible: true
    }];
var qualityContract_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#qualityViewForm #contract_list').bootstrapTable({
            url: $("#qualityViewForm #urlPrefix").val()+'/agreement/agreement/pagedataGetDetails',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#qualityViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination:true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "zlxymx.lrsj",				//排序字段
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
            uniqueId: "zlxymxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray_t,
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
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "zlxymxid", //防止同名排位用
            sortLastOrder: "desc", //防止同名排位用
            gysid: $('#qualityViewForm #gysid').val(),
            zlxyid: $('#qualityViewForm #zlxyid').val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
function queryByGysid(gysid){
    $.showDialog($("#qualityViewForm #urlPrefix").val()+"/warehouse/supplier/pagedataViewSupplier?gysid="+gysid+"&access_token="+$("#ac_tk").val(),'供应商详细信息',viewSupplierConfig);
}
function wlbmformat(value,row,index){
    var html = "";
    if(row.wlbm==null){
        html += "<span></span>"
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    }
    return html;
}
function ztformat(value,row,index) {
    var html = "";
    if(row.zt==null){
        html += "<span></span>"
    }else if (row.zt=='0'){
         html="<span style='color:red;'>"+"停用"+"</span>";
    }else if (row.zt=='1'){
         html="<span style='color:#5cb85c;'>"+"正常"+"</span>";
    }
    return html;
}
/**
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function view_wlmcformat(value,row,index){
    if(row.wlmc){
        return row.wlmc;
    }else{
        return row.fwmc;
    }
}
/**
 * 物料信息
 * @returns
 */
function queryByWlbm(wlid){
    var url=$("#qualityViewForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
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
var viewSupplierConfig = {
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//下载模板
function xz(fjid){
    jQuery('<form action="' + $("#qualityViewForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#qualityViewForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',viewPreViewConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#qualityViewForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',viewPreViewConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var viewPreViewConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
$(document).ready(function(){
    //初始化列表
    var oTable=new qualityView_TableInit();
    oTable.Init();
    var oTable_t=new qualityContract_TableInit();
    oTable_t.Init();
});


