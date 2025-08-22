var awardManagementDetails_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#viewAwardManagementForm #tb_list').bootstrapTable({
            url: $("#viewAwardManagementForm #urlPrefix").val()+'/award/award/pagedataGetAwardManagementDetails',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#viewAwardManagementForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "fkmxid",				//排序字段
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
            uniqueId: "jpmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [ {
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'jpmc',
                title: '奖品名称',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sysl',
                title: '剩余数量',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bs',
                title: '倍数',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sftz',
                title: '是否通知',
                width: '8%',
                align: 'left',
                formatter:sftzformat,
                visible: true
            },{
                field: 'tznr',
                title: '通知内容',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'fj',
                title: '附件',
                width: '30%',
                align: 'left',
                formatter:fjformat,
                visible: true
            }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            }
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
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "jpmx.jpmxid", // 防止同名排位用
            sortLastOrder: "desc" ,// 防止同名排位用
            jpglid:  $("#viewAwardManagementForm #jpglid").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
};

function sftzformat(value,row,index){
    var html="";
    if(row.sftz=='1'){
        html += "<span style='color:green'>是</span>";
    }else{
        html += "<span style='color:red'>否</span>";
    }
    return html;
}

function fjformat(value,row,index){
    var html = "";
    if(row.fjid){
        html += "<button style='outline:none;margin-bottom:5px;padding:0px;cursor:pointer;' class='btn btn-link' type='button' title='下载' onclick=\"xz(\'"+row.fjid+"\')\" >";
        html += "<span>"+row.wjm+"</span>";
        html += "<span class='glyphicon glyphicon glyphicon-save'></span>";
        html += "</button>";
        html += "<button title='预览' class='f_button' type='button' style='cursor:pointer;'>";
        html += "<span class='glyphicon glyphicon-eye-open' onclick=\"view(\'"+row.fjid+"\',\'"+row.wjm+"\')\"></span>";
        html += "</button>";
    }
    return html;
}

function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#viewAwardManagementForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

var JPGMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function xz(fjid){
    jQuery('<form action="'+$("#viewAwardManagementForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
    '<input type="text" name="fjid" value="'+fjid+'"/>' +
    '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
    '</form>')
        .appendTo('body').submit().remove();
}




$(function(){
    // 1.初始化Table
    var oTable = new awardManagementDetails_TableInit();
    oTable.Init();
});