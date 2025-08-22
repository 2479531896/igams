var qd_map=[];
var editQdyh_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#generatesignincode_Form #qdyh_list").bootstrapTable({
            url:   $('#generatesignincode_Form #urlPrefix').val()+'/train/train/pagedataGetQdyh',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#generatesignincode_Form #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"xtyh.yhm",					// 排序字段
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
            uniqueId: "yhid",                     // 每一行的唯一标识，一般为主键列
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
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'yhm',
                    title: '用户名',
                    titleTooltip:'用户名',
                    width: '15%',
                    align: 'left',
                    visible: false,
                    sortable: true
                }, {
                    field: 'zsxm',
                    title: '姓名',
                    titleTooltip:'姓名',
                    width: '30%',
                    align: 'left',
                    visible: true,
                },{
                    field: 'cz',
                    title: '操作',
                    width: '5%',
                    align: 'left',
                    formatter:yhczformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
                qd_map=map;
                if (qd_map){
                    var  json = []
                    for (var i = qd_map.rows.length-1; i >=0; i--) {
                        json.push(qd_map.rows[i].yhid)
                    }
                    $("#generatesignincode_Form #qdyhids").val(JSON.stringify(json))
                }else {
                    var json = [];
                    $("#generatesignincode_Form #qdyhids").val(JSON.stringify(json))
                }
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
            pxid:$("#generatesignincode_Form #pxid").val(),
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
function yhczformat(value,row,index){
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
    qd_map.rows.splice(index,1);
    $("#generatesignincode_Form #qdyh_list").bootstrapTable('load',qd_map);
    var  json = []
    for (var i = 0; i < qd_map.rows.length; i++) {
        json.push(qd_map.rows[i].yhid)
    }
    $("#generatesignincode_Form #qdyhids").val(JSON.stringify(json));
}
function chooseQdyh() {
    var url=$('#generatesignincode_Form #urlPrefix').val() + "/train/user/pagedataListUserForChoose?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择签到用户', chooseYhConfig);
}
var chooseYhConfig = {
    width : "800px",
    height : "500px",
    modalName	: "addFzrQdyh",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#userListForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#userListForm #yxyh").tagsinput('items');
                var qdyhids_json = $("#generatesignincode_Form #qdyhids").val();
                var qdyhids = JSON.parse(qdyhids_json)
                for(var i = 0; i < sel.length; i++){
                    var isFind = false;
                    for(var m = 0; m < qd_map.rows.length; m++){
                        if (qd_map.rows[m].yhid==sel[i].value){
                            isFind = true;
                        }
                    }
                    if (!isFind){
                        var sz = {"yhid":'',"zsxm":''};
                        sz.yhid = sel[i].value;
                        sz.zsxm = sel[i].text;
                        qd_map.rows.push(sz);
                        qdyhids.push(sel[i].value);
                    }
                }
                $("#generatesignincode_Form #qdyh_list").bootstrapTable('load',qd_map);
                $("#generatesignincode_Form #qdyhids").val(JSON.stringify(qdyhids));
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
$("#generatesignincode_Form #xzbj_no").click(function () {
    $("#generatesignincode_Form #qdyh").hide();
});

$("#generatesignincode_Form #xzbj_yes").click(function () {
    $("#generatesignincode_Form #qdyh").show();
});

$(document).ready(function(){
    var qdyhTable=new editQdyh_TableInit();
    qdyhTable.Init();
    var xzbj=$("#generatesignincode_Form #xzbj").val();
    if(xzbj=='1'){
        $("#generatesignincode_Form #qdyh").show();
    }else{
        $("#generatesignincode_Form #qdyh").hide();
    }
});