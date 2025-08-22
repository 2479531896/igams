var t_map = [];
// 显示字段
var columnsArray = [
    {
        title: '序',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序',
        width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'jzkcid',
        title: '库存ID',
        titleTooltip:'库存ID',
        width: '4%',
        align: 'left',
        visible: false
    }, {
        field: 'mc',
        title: '名称',
        titleTooltip:'名称',
        width: '18%',
        align: 'left',
        visible: true
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'ph',
        title: '批号',
        titleTooltip:'批号',
        width: '18%',
        align: 'left',
        visible: true
    }, {
        field: 'kbs',
        title: '拷贝数',
        titleTooltip:'拷贝数',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'klsl',
        title: '可领数量',
        titleTooltip:'可领数量',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'qlsl',
        title: '请领数量',
        titleTooltip:'请领数量',
        width: '12%',
        align: 'left',
        formatter:qlslformat,
        visible: true
    },
    {
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var editGermPicking_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editGermPickingForm #tb_list').bootstrapTable({
            url: $('#editGermPickingForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editGermPickingForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "mc",					//排序字段
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
            uniqueId: "jzkcid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "jzkcid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            llid: $("#editGermPickingForm #llid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};


/**
 * 请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qlslformat(value,row,index){
    var qlsl="";
    if(row.qlsl!=null){
        qlsl=row.qlsl;
    }
    var html="";
    html += "<input id='qlsl_"+index+"' autocomplete='off' value='"+qlsl+"' name='qlsl_"+index+"' style='border:1px solid #D5D5D5;' validate='{qlslNumber:true}' ";
    html += "onblur=\"checkSum('"+index+"',this,\'qlsl\')\" ";
    html += "></input>";
    return html;
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("qlslNumber", function (value, element){
    if(!value){
        $.error("请填写数量!");
        return false;
    }else{
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确数量格式，可保留两位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");


/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html="";
    html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除信息' onclick=\"delDetail('" + index + "',event)\" >删除</span>";
    return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delDetail(index,event){
    t_map.rows.splice(index,1);
    $("#editGermPickingForm #tb_list").bootstrapTable('load',t_map);
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
    t_map.rows[index].qlsl = e.value;
}





/**
 * 初始化页面
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#sqrq'
        ,theme: '#2381E9'
    });
}

/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
    var url ="/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择领料人', chooseLlrConfig);
}
var chooseLlrConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseLlrModal",
    formName	: "editGermPickingForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#taskListFzrForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editGermPickingForm #llr').val(sel_row[0].yhid);
                    $('#editGermPickingForm #llrmc').val(sel_row[0].zsxm);
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

/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
    var url="/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择部门',addBmConfig);
}
var addBmConfig = {
    width		: "800px",
    modalName	:"addBmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editGermPickingForm #bm').val(sel_row[0].jgid);
                    $('#editGermPickingForm #bmmc').val(sel_row[0].jgmc);
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

/**
 * 根据名称模糊查询
 */
$('#editGermPickingForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : '/germ/inventory/pagedataQueryGerm',
            type : 'post',
            data : {
                "entire" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.jzkcxxDtos
                    .map(function(item) {
                        var aItem = {
                            id : item.jzkcid,
                            name : item.mc+"-"+item.ph+"-"+item.gg
                        };
                        return JSON.stringify(aItem);
                    });
                return process(resultList);
            }
        });
    },
    matcher : function(obj) {
        var item = JSON.parse(obj);
        return ~item.name.toLowerCase().indexOf(
            this.query.toLowerCase())
    },
    sorter : function(items) {
        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
        while (aItem = items.shift()) {
            var item = JSON.parse(aItem);
            if (!item.name.toLowerCase().indexOf(
                this.query.toLowerCase()))
                beginswith.push(JSON.stringify(item));
            else if (~item.name.indexOf(this.query))
                caseSensitive.push(JSON.stringify(item));
            else
                caseInsensitive.push(JSON.stringify(item));
        }
        return beginswith.concat(caseSensitive,
            caseInsensitive)
    },
    highlighter : function(obj) {
        var item = JSON.parse(obj);
        var query = this.query.replace(
            /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
        return item.name.replace(new RegExp('(' + query
            + ')', 'ig'), function($1, match) {
            return '<strong>' + match + '</strong>'
        })
    },
    updater : function(obj) {
        var item = JSON.parse(obj);
        $('#editGermPickingForm #addid').attr('value', item.id);
        return item.name;
    }
});

/**
 * 回车键添加至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
    var jzkcid=$('#editGermPickingForm #addid').val();
    if (event.keyCode == "13") {
        //判断新增输入框是否有内容
        if(!$('#editGermPickingForm #addnr').val()){
            return false;
        }
        setTimeout(function(){ if(jzkcid != null && jzkcid != ''){
            $.ajax({
                type:'post',
                url:"/germ/inventory/pagedataQueryKcxxById",
                cache: false,
                data: {"jzkcid":jzkcid,"access_token":$("#ac_tk").val()},
                dataType:'json',
                success:function(data){
                    //返回值
                    if(data.jzkcxxDto!=null && data.jzkcxxDto!=''){
                        if(t_map.rows==null || t_map.rows ==""){
                            t_map.rows=[];
                        }
                        var flag=false;
                        for(var i=0;i<t_map.rows.length;i++){
                            if(t_map.rows[i].jzkcid==data.jzkcxxDto.jzkcid){
                                flag=true;
                                break;
                            }
                        }
                        if(!flag){
                            t_map.rows.push(data.jzkcxxDto);
                        }
                        $("#editGermPickingForm #tb_list").bootstrapTable('load',t_map);
                        $('#editGermPickingForm #addid').val("");
                        $('#editGermPickingForm #addnr').val("");
                    }else{
                        $.confirm("该菌种不存在!");
                    }
                }
            });
        }else{
            var addnr = $('#editGermPickingForm #addnr').val();
            if(addnr != null && addnr != ''){
                $.confirm("请选择菌种信息!");
            }
        }}, '200')
    }
})

/**
 * 根据具体元素删除
 */
function delByTargetById(arr,target){
    let temp = [];
    let pos =0;
    for (let i = 0; i < arr.length; i++) {
        if (arr[i].jzkcid != target.jzkcid){
            temp[pos++] = arr[i]
        }
    }
    return temp;
}

/**
 * 点击确定添加
 * @returns
 */
function cofirmadd(){
    var jzkcid=$('#editGermPickingForm #addid').val();
    //判断新增输入框是否有内容
    if(!$('#editGermPickingForm #addnr').val()){
        return false;
    }
    setTimeout(function(){ if(jzkcid != null && jzkcid != ''){
        $.ajax({
            type:'post',
            url:"/germ/inventory/pagedataQueryKcxxById",
            cache: false,
            data: {"jzkcid":jzkcid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.jzkcxxDto!=null && data.jzkcxxDto!=''){
                    if(t_map.rows==null || t_map.rows ==""){
                        t_map.rows=[];
                    }
                    var flag=false;
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].jzkcid==data.jzkcxxDto.jzkcid){
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        t_map.rows.push(data.jzkcxxDto);
                    }
                    $("#editGermPickingForm #tb_list").bootstrapTable('load',t_map);
                    $('#editGermPickingForm #addid').val("");
                    $('#editGermPickingForm #addnr').val("");
                }else{
                    $.confirm("该菌种不存在!");
                }
            }
        });
    }else{
        var addnr = $('#editGermPickingForm #addnr').val();
        if(addnr != null && addnr != ''){
            $.confirm("请选择菌种信息!");
        }
    }}, '200')
}

$(document).ready(function(){
    //初始化列表
    var oTable=new editGermPicking_TableInit();
    oTable.Init();
    // 初始化页面
    init();
    //所有下拉框添加choose样式
    jQuery('#editGermPickingForm .chosen-select').chosen({width: '100%'});
});