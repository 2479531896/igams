// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '5%',
        align: 'left',
        visible:true
    }, {
        field: 'xmmc',
        title: '清场项目',
        titleTooltip:'清场项目',
        width: '20%',
        align: 'left',
        visible: true
    }, {
        field: 'xmcskz1',
        title: '操作要点',
        titleTooltip:'操作要点',
        width: '50%',
        align: 'left',
        visible: true
    }, {
        field: 'jg',
        title: '自查情况',
        titleTooltip:'自查情况',
        width: '30%',
        align: 'left',
        formatter:jgformat,
        visible: true
    }];
var clearClearingRecords_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#clearClearingRecordsForm #tb_list').bootstrapTable({
            url: $("#clearClearingRecordsForm #urlPrefix").val()+'/clearing/records/pagedataGetDetails',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#clearClearingRecordsForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "cspx",				//排序字段
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
            uniqueId: "qcxmid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "qcxmid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            qcjlid: $("#clearClearingRecordsForm #qcjlid").val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

function jgformat(value,row,index){
    var html="";
    if (row.jg == '0'){
        html =  "<input type='radio' id='jg1_"+index+"' value='1' name='jg"+index+"' >合格 &nbsp;&nbsp;&nbsp;&nbsp;"+
        "<input type='radio' id='jg0_"+index+"' value='0' name='jg"+index+"'  checked>不合格";
    }else{
        html =  "<input type='radio' id='jg1_"+index+"' value='1' name='jg"+index+"' checked>合格 &nbsp;&nbsp;&nbsp;&nbsp;"+
        "<input type='radio' id='jg0_"+index+"' value='0' name='jg"+index+"' >不合格";
    }
    return html;
}


function chooseCzr(){
    var url=$('#clearClearingRecordsForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择清场人',addCzrConfig);
}
var addCzrConfig = {
    width		: "800px",
    modalName	:"addCzrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#clearClearingRecordsForm #czr').val(sel_row[0].yhid);
                    $('#clearClearingRecordsForm #czrmc').val(sel_row[0].zsxm);
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

function chooseWl(){
    var url = $('#clearClearingRecordsForm #urlPrefix').val()+"/storehouse/materiel/pagedataListMater?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择物料', wlChooseConfig);
}

var wlChooseConfig = {
    width : "1300px",
    modalName	: "chooseDdhModal",
    formName	: "chooseDdhForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#mater_choose_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length !=1 ){
                    $.error("有且只能选中一行");
                    return false;
                }
                if (!sel_row[0].wlid){
                    $.error("未获取到物料！");
                    return false;
                }
                $('#clearClearingRecordsForm #wlmc').val(sel_row[0].wlbm+'-'+sel_row[0].wlmc);
                $('#clearClearingRecordsForm #wlid').val(sel_row[0].wlid);
                return true;
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
    var oTable=new clearClearingRecords_TableInit();
    oTable.Init();
    //添加日期控件
    laydate.render({
        elem: '#clearClearingRecordsForm #qcrq',
        theme: '#2381E9',
        done: function(value, ydate){ // 选择日期后的回调
            // 将时间字符串转换为 Date 对象
            var date = new Date(value);

            // 将日期加7天
            date.setDate(date.getDate() + 7);

            // 获取新日期的年、月、日
            var year = date.getFullYear();
            var month = ('0' + (date.getMonth() + 1)).slice(-2); // 月份需要加1，并且确保两位数格式
            var day = ('0' + date.getDate()).slice(-2); // 确保两位数格式
            $('#clearClearingRecordsForm #yxq').val( year + '-' + month + '-' + day)
        }
    });
});