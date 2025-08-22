
var t_map=[];
var tb_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#batchpermitCkxxForm #tb_list").bootstrapTable({
            url:   $("#batchpermitCkxxForm #urlPrefix").val()+'/warehouse/warehouse/pagedataGetDepartmentsData',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#batchpermitCkxxForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"ckjgxx.ckid",					// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ckjgid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    titleTooltip:'序号',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'jgmc',
                    title: '部门',
                    width: '82%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '10%',
                    align: 'left',
                    formatter:czformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
                t_map=map;
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            ckid: $('#batchpermitCkxxForm #ckid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"deleteDetail('" + index + "')\" >删除</span>";
    return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function deleteDetail(index){
    t_map.rows.splice(index,1);
    $("#batchpermitCkxxForm #tb_list").bootstrapTable('load',t_map);
}

function addDepartments() {
    var url = $('#batchpermitCkxxForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择部门', chooseDepartmentsConfig);
}
var chooseDepartmentsConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseDepartmentsModal",
    formName	: "printgrantForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#listDepartmentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var yxjgs = $("#listDepartmentForm #t_xzjgs").tagsinput('items');

                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==0){
                    $.error("请至少选中一行");
                    return;
                }
                for (var i = 0; i < yxjgs.length; i++) {
                    var isFind=false;
                    for (var j = 0; j < t_map.rows.length; j++) {
                        if(yxjgs[i].jgid==t_map.rows[j].jgid){
                            isFind=true;
                            break;
                        }
                    }
                    if(!isFind){
                        var json={"jgmc":'',"jgid":''};
                        json.jgmc = yxjgs[i].jgmc;
                        json.jgid = yxjgs[i].jgid;
                        t_map.rows.push(json);
                    }
                }
                $("#batchpermitCkxxForm #tb_list").bootstrapTable('load',t_map);
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


$(function () {
    var oTable_t=new tb_TableInit();
    oTable_t.Init();
});