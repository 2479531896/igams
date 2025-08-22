var t_map=[];
var editHealth_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#eidtHealthForm #edit_list").bootstrapTable({
            url:   $('#eidtHealthForm #urlPrefix').val()+'/roster/roster/pagedataGetHealth',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#eidtHealthForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: false,                     // 是否启用排序
            sortName:"jkda.jcsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
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
            uniqueId: "jkdaid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    checkbox: true,
                    width: '3%'
                }
                ,{
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '5%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'jkdaid',
                    title: '健康答案id',
                    width: '6%',
                    align: 'left',
                    visible: false
                 },{
                    field: 'bm',
                    title: '部门',
                    width: '10%',
                    align: 'left',
                    visible: false
                },{
                    field: 'gwmc',
                    title: '岗位名称',
                    width: '15%',
                    align: 'left',
                    visible: false
                },{
                    field: 'jcxm',
                    title: '检查项目',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jcsj',
                    title: '检查时间',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jcjg',
                    title: '检查结果',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'clyj',
                    title: '处理意见',
                    width: '35%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '10%',
                    align: 'left',
                    formatter:edit_czformat,
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
            onPostBody:function(){
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            yghmcid: $('#eidtHealthForm #yghmcid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}

function edit_czformat(value,row,index){
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-default' title='修改' onclick=\"editDetail('" + row.jkdaid + "')\" >修改</span>";
    return html;
}

function editDetail(jkdaid){
    var url=$('#eidtHealthForm #urlPrefix').val() + "/roster/roster/pagedataModHealth?access_token=" + $("#ac_tk").val()+"&jkdaid="+jkdaid;
    $.showDialog(url,'健康答案修改',modHealthConfig);
}
var modHealthConfig = {
    width		: "800px",
    modalName	: "addHealthModal",
    formName	: "addHealthForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $("#addHealthForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addHealthForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                $('#eidtHealthForm #edit_list').bootstrapTable('refresh');
                            }
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;

            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function delHealth(){
    var sel_row = $('#eidtHealthForm #edit_list').bootstrapTable('getSelections');//获取选择行数据
    if(sel_row.length ==0){
        $.error("请至少选中一行");
        return;
    }
    var ids="";
    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        ids = ids + ","+ sel_row[i].jkdaid;
    }
    ids = ids.substr(1);
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url=$("#eidtHealthForm #urlPrefix").val()+"/roster/roster/pagedataDelHealth";
            jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $('#eidtHealthForm #edit_list').bootstrapTable('refresh');
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);

            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}

$(function(){
    var oTable_t=new editHealth_TableInit();
    oTable_t.Init();
})
