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
        field: 'rzrq',
        title: '入职日期',
        titleTooltip:'入职日期',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'lzrq',
        title: '离职日期',
        titleTooltip:'离职日期',
        width: '15%',
        align: 'left',
        visible: true
    },{
        field: 'fjids',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible:true
    }];
var yglzUpholdForm_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#yglzUpholdForm #tb_list').bootstrapTable({
            url:$('#yglzUpholdForm #urlPrefix').val()+'/roster/roster/pagedataGetLzxxById?yghmcid='+$('#yglzUpholdForm #yghmcid').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#yglzUpholdForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "htqsr",				        //排序字段
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
            uniqueId: "wlxxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
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
            sortLastName: "lrsj", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
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
    var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除明细' onclick=\"delDetail('" + index + "',event)\" >删除</span>";
    return html;
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
    html += "<input type='hidden' id='lzfj_"+index+"' name='lzfj_"+index+"'/>";
    if(row.fjbj == "1"){
        html += "<a href='javascript:void(0);' onclick='uploadLogisFile(\"" + index + "\",\""+row.yglzid+"\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='uploadLogisFile(\"" + index + "\",\""+row.yglzid+"\")' >否</a>";
    }
    html += "</div>";
    return html;
}
/**
 * 离职维护上传附件
 * @param index
 * @returns
 */
var currentUploadIndex = "";
function uploadLogisFile(index,yglzid) {
    if(index){
        var ywid = $("#yglzUpholdForm #yghmcid").val();
        var fjids = $("#yglzUpholdForm #fj_"+ index +" input").val();
        var url=$('#yglzUpholdForm #urlPrefix').val()+"/inspectionGoods/pendingInspection/pagedataGetUploadFilePage?access_token=" + $("#ac_tk").val()+"&ywid="+ywid+"&zywid="+yglzid;
        if(fjids){
            url=url + "&fjids="+fjids;
        }
        currentUploadIndex = index;
    }
    $.showDialog(url, '修改附件', uploadFileConfig);
}
var uploadFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    formName	: "yglzUpholdForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                if($("#ajaxForm #fjids").val()){
                    $("#yglzUpholdForm #fj_"+currentUploadIndex+" input").val($("#ajaxForm #fjids").val());
                    $("#yglzUpholdForm #fj_"+currentUploadIndex+" a").text("是");
                    $('#yglzUpholdForm #tb_list').bootstrapTable('getData')[currentUploadIndex].fjbj="0";
                }else {
                    //判断正式文件是否存在
                    var dom = $("#ajaxForm div[name='formalFile']").html();
                    $("#yglzUpholdForm #fj_"+currentUploadIndex+" input").val($("#ajaxForm #fjids").val());
                    if ((dom == null || dom == "" || dom == undefined)) {
                        $("#yglzUpholdForm #fj_" + currentUploadIndex + " input").val("");
                        $("#yglzUpholdForm #fj_" + currentUploadIndex + " a").text("否");
                        $('#yglzUpholdForm #tb_list').bootstrapTable('getData')[currentUploadIndex].fjbj = "1";
                    }
                }
                $.closeModal(opts.modalName);
                return true;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function delDetail(index,event){
    t_map.rows.splice(index,1);
    $("#yglzUpholdForm #tb_list").bootstrapTable('load',t_map);
}
function cofirm(){
    var rzrq = $('#yglzUpholdForm #rzrq').val();
    if (rzrq==null||rzrq==''){
        $.error("请输入入职日期");
        return false;
    }
    var lzrq = $('#yglzUpholdForm #lzrq').val();
    if (lzrq==null||lzrq==''){
        $.error("请输入离职日期");
        return false;
    }
    var sz = {"rzrq":'',"lzrq":''};
    sz.rzrq = rzrq;
    sz.lzrq = lzrq;
    t_map.rows.push(sz)
    $('#yglzUpholdForm #rzrq').val('');
    $('#yglzUpholdForm #lzrq').val('');
    $("#yglzUpholdForm #tb_list").bootstrapTable('load',t_map);
}
$(document).ready(function(){
    //初始化列表
    var oTable=new yglzUpholdForm_TableInit();
    oTable.Init();
    //添加日期控件
    laydate.render({
        elem: '#yglzUpholdForm #rzrq'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#yglzUpholdForm #lzrq'
        ,theme: '#2381E9'
    });
    //所有下拉框添加choose样式
    jQuery('#yglzUpholdForm .chosen-select').chosen({width: '100%'});
});