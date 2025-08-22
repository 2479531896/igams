var t_map=[];

var awardManagementDetails_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#addAwardManagementForm #tb_list').bootstrapTable({
            url: $("#addAwardManagementForm #urlPrefix").val()+'/award/award/pagedataGetAwardManagementDetails',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#addAwardManagementForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jpmx.xh",					// 排序字段
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
            uniqueId: "jpmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
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
                width: '30%',
                align: 'left',
                formatter:jpmcformat,
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '8%',
                align: 'left',
                formatter:slformat,
                visible: true
            },{
                field: 'bs',
                title: '倍数',
                width: '8%',
                align: 'left',
                formatter:bsformat,
                visible: true
            }, {
                field: 'sftz',
                title: '是否通知',
                titleTooltip:'是否通知',
                width: '10%',
                align: 'left',
                formatter:sftzformat,
                visible: true
            }, {
                field: 'tznr',
                title: '通知内容',
                titleTooltip:'通知内容',
                width: '30%',
                align: 'left',
                formatter:tznrformat,
                visible: true
            },{
                field: 'fj',
                title: '附件',
                width: '6%',
                align: 'left',
                formatter:fjformat,
                visible: true
            },{
                field: 'cz',
                title: '操作',
                width: '5%',
                align: 'left',
                formatter:czformat,
                visible: true
            }],
            onLoadSuccess: function (map) {
                t_map=map;
                if(t_map.rows == null || t_map.rows.length == 0){
                    var a={"jpmxid":'',"jpmc":'谢谢参与',"sl":'0',"bs":'1',"sftz":'0',"tznr":'',"fjid":''};
                    t_map.rows.push(a);
                    $("#addAwardManagementForm #tb_list").bootstrapTable('load',t_map);
                }
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

            jpglid:  $("#addAwardManagementForm #jpglid").val(),
            copyflag:  $("#addAwardManagementForm #copyflag").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
};

function jpmcformat(value,row,index){
    if(row.jpmc == null){
        row.jpmc = "" ;
    }
    var html="<input id='jpmc_"+index+"' name='jpmc_"+index+"' value='"+row.jpmc+"'  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'jpmc\')\" autocomplete='off'></input>";
    return html;
}

function slformat(value,row,index){
    if(row.sl == null){
        row.sl = "" ;
    }
    var html="<input type='number' id='sl_"+index+"' name='sl_"+index+"' value='"+row.sl+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'sl\')\" autocomplete='off'></input>";
    return html;
}

function bsformat(value,row,index){
    if(row.bs == null){
        row.bs = "" ;
    }
    var html="<input type='number' id='bs_"+index+"' name='bs_"+index+"' value='"+row.bs+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bs\')\" autocomplete='off'></input>";
    return html;
}

function sftzformat(value,row,index){
    var html="";
    html +=  "<select id='sftz_"+index+"' name='sftz_"+index+"'  class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'sftz\')\">";
    if(row.sftz=='1'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' selected>是</option>";
        html += "<option value='0'>否</option>";
    }else if(row.sftz=='0'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' selected>否</option>";
    }else{
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' >否</option>";
    }
    html += "</select>";
    return html;
}

function tznrformat(value,row,index){
    if(row.tznr == null){
        row.tznr = "" ;
    }
    var html="<input id='tznr_"+index+"' name='tznr_"+index+"' value='"+row.tznr+"'  placeholder='替换规则 姓名：【name】 奖品：【jpmc】' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'tznr\')\"></input>";
    return html;
}

function czformat(value,row,index){
    var html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"delDetail('" + index + "',event)\" >删除</span>";
    return html;
}

function delDetail(index,event){
    t_map.rows.splice(index,1);
    $("#addAwardManagementForm #tb_list").bootstrapTable('load',t_map);
}

function checkSum(index,e,zdmc){
    if(zdmc=='jpmc'){
        t_map.rows[index].jpmc=e.value;
    }else if(zdmc=='sl'){
        t_map.rows[index].sl=e.value;
    }else if(zdmc=='bs'){
        t_map.rows[index].bs=e.value;
    }else if(zdmc=='sftz'){
        t_map.rows[index].sftz=e.value;
    }else if(zdmc=='tznr'){
        t_map.rows[index].tznr=e.value;
    }
}

function fjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    if(row.fjid){
        html += "<a href='javascript:void(0);' onclick='uploadAwardFile(\"" + index + "\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='uploadAwardFile(\"" + index + "\")' >否</a>";
    }
    html += "</div>";
    return html;
}

var currentUploadIndex = "";
function uploadAwardFile(index) {
    if(index){
        var ywlx = $("#addAwardManagementForm #ywlx").val();
        var fjid ="";
        if(t_map.rows[index].fjid){
            fjid = t_map.rows[index].fjid;
        }
        var url=$('#addAwardManagementForm #urlPrefix').val()+"/award/award/pagedataGetUploadFilePage?access_token=" + $("#ac_tk").val()+"&ywlx="+ywlx+"&fjid="+fjid;
        currentUploadIndex = index;
    }
    $.showDialog(url, '上传附件', uploadFileConfig);
}
var uploadFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                if($("#ajaxForm #fjids").val()){
                    if($("#ajaxForm #fjids").val().split(",").length>1){
                        $.error("只允许上传一个文件!");
                        return false;
                    }else{
                        t_map.rows[currentUploadIndex].fjid=$("#ajaxForm #fjids").val();
                        $("#addAwardManagementForm #tb_list").bootstrapTable('load',t_map);
                    }
                }else{
                    t_map.rows[currentUploadIndex].fjid="";
                    $("#addAwardManagementForm #tb_list").bootstrapTable('load',t_map);
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

function addnew(){
    var a={"jpmxid":'',"jpmc":'',"sl":'',"bs":'',"sftz":'',"tznr":'',"fjid":''};
    t_map.rows.push(a);
    $("#addAwardManagementForm #tb_list").bootstrapTable('load',t_map);
}

function chooseBm() {
    var url = $('#addAwardManagementForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseBmModal",
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
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#addAwardManagementForm #ssbm').val(sel_row[0].jgid);
                    $('#addAwardManagementForm #ssbmmc').val(sel_row[0].jgmc);
                    $.closeModal(opts.modalName);
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


$(function(){
    // 1.初始化Table
    var oTable = new awardManagementDetails_TableInit();
    oTable.Init();
    //添加日期控件
    laydate.render({
        elem: '#addAwardManagementForm #ksrq'
        , theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#addAwardManagementForm #jsrq'
        , theme: '#2381E9'
    });
    // 所有下拉框添加choose样式
    jQuery('#addAwardManagementForm .chosen-select').chosen({width: '100%'});
});