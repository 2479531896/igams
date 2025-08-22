var t_map = [];
var nowdate = $("#printgrantForm #nowdate").val();
//显示字段
var columnsArray = [
    {
        title: '序',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'jgmc',
        title: '部门名称',
        titleTooltip:'部门名称',
        width: '20%',
        align: 'left',
        visible: true
    }, {
        field: 'fffs',
        title: '发放份数',
        titleTooltip:'发放份数',
        width: '6%',
        align: 'left',
        visible: true,
        formatter:fffsformat
    }, {
        field: 'sfff',
        title: '是否发放',
        titleTooltip:'是否发放',
        width: '6%',
        align: 'left',
        visible: true,
        formatter:sfffformat
    }, {
        field: 'ffrq',
        title: '发放日期',
        titleTooltip:'发放日期',
        width: '10%',
        align: 'left',
        visible: true,
        formatter:ffrqformat
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '8%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];

var printgrantForm_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#printgrantForm #printgrant_list').bootstrapTable({
            url: $('#printgrantForm #urlPrefix').val()+'/production/document/pagedataGetPrintgrantDocumentList?wjid='+$('#printgrantForm #wjid').val(),         //请求后台的URL（*）
            method: 'get',                       //请求方式（*）
            toolbar: '#printgrantForm #toolbar',                //工具按钮用哪个容器
            striped: true,                       //是否显示行间隔色
            cache: false,                        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                      //是否启用排序
            sortName: "bm",				     //排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams, //传递参数（*）
            sidePagination: "server",            //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                        //初始化加载第一页，默认第一页
            pageSize: 15,                        //每页的记录行数（*）
            pageList: [15, 30, 50, 100],         //可供选择的每页的行数（*）
            paginationPreText: '‹',				 //指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			 //指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,              //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                       //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "bm",                    //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                     //是否显示详细视图
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
            }
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
            sortLastName: "ffjlid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

function sfffformat(value,row,index){
    var html="";
    html +=  "<select id='sfff_"+index+"' name='sfff_"+index+"' validate='{required:true}' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'sfff\')\">";
    if(row.sfff=='1'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' selected>是</option>";
        html += "<option value='0'>否</option>";
    }else if(row.sfff=='0'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' selected>否</option>";
    }
    html += "</select>";
    return html;
}
/**
 * 发放份数格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fffsformat(value,row,index){
    if(row.fffs == null){
        row.fffs = "";
    }
    var html = "<input id='fffs_"+index+"' value='"+row.fffs+"' name='fffs_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{slNumber:true,required:true}' onblur= \"checkSum('"+index+"',this,\'fffs\')\"/>";
    return html;
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
    if(!value){
        $("#printgrantForm #"+element.id).val("")
        return false;
    }else{
        if (value==0){
            $.error("发放份数不能为0!");
            $("#printgrantForm #"+element.id).val("")
        }
        if(!/^\d+(\.\d{1})?$/.test(value)){
            $.error("请填写正确数量格式!");
            $("#printgrantForm #"+element.id).val("")
        }
    }
    return this.optional(element) || /^\d+(\.\d{1})?$/.test(value);
},"请填写正确格式");

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delmx(index,event){
    t_map.rows.splice(index,1);
    $("#printgrantForm #printgrant_list").bootstrapTable('load',t_map);
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
    html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除明细' onclick=\"delmx('" + index + "',event)\" >删除</span></div>";
    return html;
}
/**
 * 发放日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ffrqformat(value,row,index){
    if(row.ffrq == null){
        row.ffrq = "";
    }
    var html = "<input id='ffrq_"+index+"' autocomplete='off' value='"+row.ffrq+"' name='ffrq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onchange=\"checkSum('"+index+"',this,'"+row.wlid+"',\'ffrq\')\"/>";
    setTimeout(function() {
        laydate.render({
            elem: '#printgrantForm #ffrq_'+index
            ,theme: '#2381E9'
            ,done: function(value, date, endDate){
                t_map.rows[index].ffrq = value;
                $("#printgrantForm #ffmx_json").val(JSON.stringify(t_map.rows));
            }
        });
    }, 100);
    return html;
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
    if(t_map.rows.length > index){
        if (zdmc == 'sfff') {
            if ("1"==e.value){
                $("#printgrantForm #ffrq_"+index).val(nowdate);
                t_map.rows[index].ffrq = nowdate;
            }else {
                $("#printgrantForm #ffrq_"+index).val('');
                t_map.rows[index].ffrq = '';
            }
            t_map.rows[index].sfff = e.value;
        } else if (zdmc == 'fffs') {
            if (e.value>999){
                e.value = '';
                $.error("发放份数不允许大于999！");
                return false;
            }
            t_map.rows[index].fffs = e.value;
        }else if (zdmc == 'ffrq') {
            t_map.rows[index].ffrq = e.value;
        }
    }
    $("#printgrantForm #ffmx_json").val(JSON.stringify(t_map.rows));
}
//添加日期控件
laydate.render({
    elem: '#djrq'
    ,theme: '#2381E9'
});
/**
 * 选择部门
 * @returns
 */
function chooseBm() {
    var url = $('#printgrantForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseBmModal",
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
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        for (let j = 0; j < yxjgs.length; j++) {
                            if (t_map.rows[i].bm==yxjgs[j].jgid){
                                yxjgs.splice(j,1);
                            }
                        }
                    }
                }
                if (yxjgs.length>0){
                    for (let i = 0; i < yxjgs.length; i++) {
                        var json={"jgmc":'',"fffs":'',"sfff":'',"ffrq":'',"bm":''};
                        json.jgmc = yxjgs[i].jgmc;
                        json.bm = yxjgs[i].jgid;
                        json.sfff = '0';
                        t_map.rows.push(json);
                    }
                }
                $("#printgrantForm #printgrant_list").bootstrapTable('load',t_map);
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

$(function(){
    // 所有下拉框添加choose样式
    jQuery('#printgrantForm .chosen-select').chosen({width: '100%'});

    //初始化列表
    var oTable=new printgrantForm_TableInit();
    oTable.Init();
});