var t_map=[];
var sampleStockGcInfoTab_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#sampleStockAjaxForm #tbQy_list').bootstrapTable({
            url: $("#sampleStockAjaxForm #urlPrefix").val()+'/retention/retention/pagedataQySampleStock',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampleStockAjaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "",				//排序字段
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
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
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
                    field: 'qyid',
                    title: '取样id',
                    titleTooltip:'取样id',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'scph',
                    title: '取样单号',
                    titleTooltip:'取样单号',
                    width: '6%',
                    align: 'left',
                    visible: true,
                    formatter:sampleStock_qydhformat
                }, {
                    field: 'lrsj',
                    title: '取样日期',
                    titleTooltip:'取样日期',
                    width: '5%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'qyl',
                    title: '取样量',
                    titleTooltip:'取样量',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'yt',
                    title: '用途',
                    titleTooltip:'用途',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'qyr',
                    title: '取样人',
                    titleTooltip:'取样人',
                    width: '4%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'bz',
                    title: '备注',
                    titleTooltip:'备注',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'lyxj',
                    title: '留样小结',
                    titleTooltip:'留样小结',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'qyxj',
                    title: '取样小结',
                    titleTooltip:'取样小结',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'bg',
                    title: '报告',
                    titleTooltip:'报告',
                    width: '3%',
                    align: 'left',
                    formatter:bgformat,
                    visible: true
                }],
            onLoadSuccess: function (map) {
                t_map=map;
            },
            onLoadError: function () {
                alert("数据加载失败！");
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
            sortLastName: "", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            lykcid:$('#sampleStockAjaxForm #lykcid').val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
var sampleStockQyInfoTab_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#sampleStockAjaxForm #tbGc_list').bootstrapTable({
            url: $("#sampleStockAjaxForm #urlPrefix").val()+'/retention/retention/pagedataGcSampleStock',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampleStockAjaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "htid",				//排序字段
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
            uniqueId: "htfkmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
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
                    field: 'lykcid',
                    title: '留样库存id',
                    titleTooltip:'留样库存id',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'lrsj',
                    title: '观察时间',
                    titleTooltip:'观察时间',
                    width: '6%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'gcry',
                    title: '观察人员',
                    titleTooltip:'观察人员',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'gcyq',
                    title: '观察要求',
                    titleTooltip:'观察要求',
                    width: '20%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'gcjg',
                    title: '观察结果',
                    titleTooltip:'观察结果',
                    width: '3%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'bz',
                    title: '观察备注',
                    titleTooltip:'观察备注',
                    width: '20%',
                    align: 'left',
                    visible: true
                }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
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
            sortLastName: "", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            lykcid:$('#sampleStockAjaxForm #lykcid').val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
//下载模板
function xz(fjid){
    jQuery('<form action="' + $("#sampleStocksampleStockAjaxForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

function bgformat(value,row,index){
    var html = "<div id='fj"+row.qyid+"'>";
    html += "<input type='hidden'>";
    if(row.fjbj == "0"){
        html += "<a href='javascript:void(0);' onclick='uploadQyxxFile(\"" + index + "\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='uploadQyxxFile(\"" + index + "\")' >否</a>";
    }
    html += "</div>";
    return html;
}
/**
 * 取样单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sampleStock_qydhformat(value,row,index){
    var html = "";
    if(row.scph!=null && row.scph!=''){
        html += "<a href='javascript:void(0);' onclick=\"sampleStock_queryByQydh('"+row.qyid+"')\">"+row.scph+"</a>";
    }else {
        html +="-";
    }
    return html;
}
function sampleStock_queryByQydh(qyid){
    var url=$("#sampleStockAjaxForm #urlPrefix").val()+"/retention/retention/pagedataViewSampling?qyid="+qyid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'取样信息',viewQyConfig);
}
var viewQyConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 上传附件
 * @param index
 * @returns
 */
function uploadQyxxFile(index) {
    if(index){
        var qyid = t_map.rows[index].qyid;
       var url=$('#sampleStockAjaxForm #urlPrefix').val()+"/retention/retention/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qyid="+qyid;
        $.showDialog(url, '附件查看', qyxxFileConfig);
    }
}
var qyxxFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "qyxxFileModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#sampleStocksampleStockAjaxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',viewPreViewConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#sampleStocksampleStockAjaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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
    var oTable=new sampleStockGcInfoTab_TableInit();
    oTable.Init();
    var oTablePay=new sampleStockQyInfoTab_TableInit();
    oTablePay.Init();
    // 初始化页面
});