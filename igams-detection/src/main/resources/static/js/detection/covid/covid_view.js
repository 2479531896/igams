function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

function xzword(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
function view(fjid){
    $("#covidview_formSearch input[name='access_token']").val($("#ac_tk").val());
    var url= "/common/file/pdfPreview?fjid=" + fjid;
    $.showDialog(url,'报告',PdfMaterConfig);
}
var PdfMaterConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var CovidView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#covidview_formSearch #hjsj_list').bootstrapTable({
            url: '/detection/detection/pagedataListHzxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#covidview_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fzjcxx.bgrq",				//排序字段
            sortOrder: "DESC NULLS LAST",                   //排序方式
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
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'sj',
                title: '电话',
                titleTooltip:'电话',
                width: '6%',
                align: 'left',
                visible:true
            }, {
                field: 'bbzbh',
                title: '标本子编号',
                titleTooltip:'标本子编号',
                width: '6%',
                align: 'left',
                visible:true
            }, {
                field: 'yblx',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '6%',
                align: 'left',
                visible:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '6%',
                align: 'left',
                visible:true
            },{
                field: 'cjsj',
                title: '采集时间',
                titleTooltip:'采集时间',
                width: '6%',
                align: 'left',
                visible:true
            }, {
                field: 'cjryxm',
                title: '采集人员',
                titleTooltip:'采集人员',
                width: '5%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            // onDblClickRow: function (row, $element) {
            //     CovidDealById(row.fzjcid,'view',$("#covidview_formSearch #btn_view").attr("tourl"));
            // },
        });
        $("#covidview_formSearch #covid_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true,
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            ybbh: $("#ybbh").val(),
            jclx: $("#covidview_formSearch #jclx").val()
        };
        return map;
    };
    return oTableInit;
}
$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new CovidView_TableInit();
    oTable.Init();
    // 所有下拉框添加choose样式
    jQuery('#covidview_formSearch .chosen-select').chosen({width: '100%'});
});
