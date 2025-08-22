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
        field: 'kssj',
        title: '开始时间',
        titleTooltip:'开始时间',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'jssj',
        title: '结束时间',
        titleTooltip:'结束时间',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'zjrymc',
        title: '质检人员',
        titleTooltip:'质检人员',
        width: '12%',
        align: 'left',
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '6%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var qualitySetting_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#qualitySetting #tb_list').bootstrapTable({
            url:$('#qualitySetting #urlPrefix').val()+'/retention/retention/pagedataQualitySetting?lykcid='+$('#qualitySetting #lykcid').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#qualitySetting #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "zjsz.kssj",				        //排序字段
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
            uniqueId: "zjszid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "bxh", //防止同名排位用
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
function delDetail(index,event){
    $.ajax({
        type:'post',
        url:$('#qualitySetting #urlPrefix').val()+"/retention/retention/pagedataDelQualitySetting",
        cache: false,
        data: {"zjszid":t_map.rows[index].zjszid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if (data){
                t_map.rows.splice(index,1);
                $("#qualitySetting #tb_list").bootstrapTable('load',t_map);
            }else {
                $.error("删除失败！");
            }
        }
    });
}
/**
 * 选择质检人员列表
 * @returns
 */
function chooseZjr(){
    var url=$('#qualitySetting #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择质检人员',addZjrConfig);
}
var addZjrConfig = {
    width		: "800px",
    modalName	:"addZjrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#qualitySetting #zjry').val(sel_row[0].yhid);
                    $('#qualitySetting #zjrymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#qualitySetting #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
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
function cofirm(){
    var lykcid = $('#qualitySetting #lykcid').val();
    var kssj = $('#qualitySetting #kssj').val();
    var jssj = $('#qualitySetting #jssj').val();
    var zjry = $('#qualitySetting #zjry').val();
    $.ajax({
        type:'post',
        url:$('#qualitySetting #urlPrefix').val()+"/retention/retention/pagedataAddQualitySetting",
        cache: false,
        data: {"lykcid":lykcid,"kssj":kssj,"jssj":jssj,"zjry":zjry,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if (data.status=='success'){
                t_map.rows.push(data.zjszDto)
                $("#qualitySetting #tb_list").bootstrapTable('load',t_map);
            }else {
                alert("添加失败");
            }
        }
    });
}
$(document).ready(function(){
    //初始化列表
    var oTable=new qualitySetting_TableInit();
    oTable.Init();
    //所有下拉框添加choose样式
    jQuery('#qualitySetting .chosen-select').chosen({width: '100%'});

    //添加日期控件
    laydate.render({
        elem: '#qualitySetting #kssj'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#qualitySetting #jssj'
        ,theme: '#2381E9'
    });
});