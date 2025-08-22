var delllmxids = [];
var addmxkcids = [];
var t_map = [];
var yybids = [];
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
    },{
        field: 'ybkcid',
        title: '样本库存ID',
        titleTooltip:'样本库存ID',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'nbbm',
        title: '内部编码',
        titleTooltip:'内部编码',
        formatter: nbbm_formatter,
        width: '12%',
        align: 'left',
        visible: true
    },{
        field: 'ybbh',
        title: '样本编号',
        titleTooltip:'样本编号',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'yblxmc',
        title: '样本类型',
        titleTooltip:'样本类型',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'jcdwmc',
        title: '存储单位',
        titleTooltip:'存储单位',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'tj',
        title: '体积',
        titleTooltip:'体积',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'bxh',
        title: '冰箱',
        titleTooltip:'冰箱',
        width: '20%',
        align: 'left',
        visible: true,
        formatter:bxformater
    }, {
        field: 'ctwz',
        title: '抽屉',
        titleTooltip:'抽屉',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'hh',
        title: '盒子',
        titleTooltip:'盒子',
        width: '12%',
        align: 'left',
        visible: true,
        formatter:hzformater
    }, {
        field: 'wz',
        title: '位置',
        titleTooltip:'位置',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var ybllmxEdit_TableInit = function () {
    var oTableInit = new Object();
    var llid =  $('#ybpickingCarForm #llid').val();
    //初始化Table
    oTableInit.Init = function () {
        $('#ybpickingCarForm #ybllmx_list').bootstrapTable({
            url: $('#ybpickingCarForm #urlPrefix').val()+'/sample/inventory/pagedataYbllcList?llid='+llid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ybpickingCarForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            // sortName: "",				        //排序字段
            // sortOrder: "asc",                   //排序方式
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
            uniqueId: "nbbm",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
                if(t_map.rows!=null){
                    // 初始化ybllmx_json
                    var json = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"ybkcid":'',"nbbm":'',"ybbh":'',"yblx":'',"tj":'',"bxh":'',"cth":'',"hh":'',"wz":'',"cz":'',"llmxid":'',"sjid":'',"addkcid":''};
                        sz.index=t_map.rows.length;
                        sz.ybkcid=t_map.rows.ybkcid;
                        sz.nbbm = t_map.rows[i].nbbm;
                        sz.ybbh = t_map.rows[i].ybbh;
                        sz.yblx = t_map.rows[i].yblx;
                        sz.tj = t_map.rows[i].tj;
                        sz.bxh = t_map.rows[i].bxh;
                        sz.cth = t_map.rows[i].cth;
                        sz.hh = t_map.rows[i].hh;
                        sz.wz = t_map.rows[i].wz;
                        sz.cz = t_map.rows[i].cz;
                        sz.llmxid = t_map.rows[i].llmxid;
                        sz.sjid = t_map.rows[i].sjid;
                        sz.addkcid = t_map.rows[i].addkcid;
                        json.push(sz);
                        delllmxids.push(sz);
                        addmxkcids.push(sz);
                    }
                    $("#ybpickingCarForm #delllmxids").val(JSON.stringify(delllmxids));
                    $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));
                }
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
laydate.render({
    elem: '#ybpickingCarForm #sqrq'
    , theme: '#2381E9'
});
function bxformater(value,row,index) {
    return (row.bxwz==null?'':row.bxwz)+(row.bxh==null?'':"("+row.bxh+")");
}
function hzformater(value,row,index) {
    return (row.hzwz==null?'':row.hzwz)+(row.hzbh==null?(row.hh==null?'':"("+row.hh+")"):"("+row.hzbh+")");
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    //如果状态为80不显示删除按钮
    var tg = $('#CHECK_PASS').val();
    if (row.zt!=tg){
        var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除' onclick=\"delYbDetail('" + index + "',event)\">删除</span>";
        return html;
    }
}
//内部编码
function nbbm_formatter(value,row,index) {
    var html = "";
    if(row.nbbm==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"openByt('" + row.sjid + "',event)\">"+row.nbbm+"</a>";
    }
    return html;
}
function openByt(sjid,event) {
    var url=$("#ybkcxx_formSearch #urlPrefix").val()+"/inspection/inspection/pagedataViewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewBytConfig);
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delYbDetail(index,event){
    //
    var szybids = yybids.split(",");
    for (var i = 0; i < szybids.length; i++) {
        if (szybids[i]==t_map.rows[index].ybkcid){
            szybids.splice(i,1)
        }
    }
    yybids = szybids.toString();
    if (delllmxids.length>0&&delllmxids!=''){
        for (var i = 0; i < delllmxids.length; i++) {
            if (delllmxids[i].llmxid==t_map.rows[index].llmxid){
                delllmxids.splice(i,1)
            }
        }
        $("#ybpickingCarForm #delllmxids").val(JSON.stringify(delllmxids));
    }

    if (addmxkcids.length>0&&addmxkcids!='') {
        for (var i = 0; i < addmxkcids.length; i++) {
            if (addmxkcids[i].addkcid==t_map.rows[index].ybkcid){
                addmxkcids.splice(i,1);
            }
        }
        $("#ybpickingCarForm #addmxkcids").val(JSON.stringify(addmxkcids));
    }
    t_map.rows.splice(index,1);
    $("#ybpickingCarForm #ybllmx_list").bootstrapTable('load',t_map);
}
/**
 * 选择领料人
 * @returns
 */
function chooseLlr() {
    var url = $('#ybpickingCarForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择领料人', chooseQrrConfig);
}
var chooseQrrConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseSqrModal",
    formName	: "pickingCarForm",
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
                    $('#ybpickingCarForm #llr').val(sel_row[0].yhid);
                    $('#ybpickingCarForm #llrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#ybpickingCarForm #urlPrefix').val()+"/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
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
 * 选择发料人
 * @returns
 */
function chooseFlr() {
    var url = $('#ybpickingCarForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择发料人', chooseFlrConfig);
}
var chooseFlrConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseFlrModal",
    formName	: "pickingCarForm",
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
                    $('#ybpickingCarForm #flr').val(sel_row[0].yhid);
                    $('#ybpickingCarForm #flrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#ybpickingCarForm #urlPrefix').val()+"/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
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
 * 选择申请部门
 * @returns
 */
function chooseBm() {
    var url = $('#ybpickingCarForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseBmModal",
    formName	: "pickingCarForm",
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
                    $('#ybpickingCarForm #bm').val(sel_row[0].jgid);
                    $('#ybpickingCarForm #bmmc').val(sel_row[0].jgmc);
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
 * 点击确定添加样本
 * @returns
 */
function cofirmaddyb(){
    var ybid=$('#ybpickingCarForm #addybid').val();
    if (yybids!=''&&yybids!=null){
        var szybids = yybids.split(",");
        for (var i = 0; i < szybids.length; i++) {
            if (szybids[i]==ybid){
                $.confirm("该样本已选择!");
            }
        }
    }
    //判断输入框是否有内容
    if(!$('#ybpickingCarForm #addyb').val()){
        return false;
    }
    setTimeout(function(){ if(ybid != null && ybid != ''){
        $.ajax({
            type:'post',
            url:$('#ybpickingCarForm #urlPrefix').val()+"/sample/inventory/pagedataYbmxByYbkcid",
            cache: false,
            data: {"ybkcid":ybid,"ids":yybids,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data!=null && data!=''){
                     t_map.rows.push(data);
                     yybids=yybids+","+ybid;
                     addmxkcids.push(data);
                     $("#ybpickingCarForm #addmxkcids").val(JSON.stringify(addmxkcids));
                     $("#ybpickingCarForm #ybllmx_list").bootstrapTable('load',t_map);
                     $('#ybpickingCarForm #addybid').val("");
                     $('#ybpickingCarForm #addyb').val("");
                }
            }
        });
    }else{
        var addyb = $('#ybpickingCarForm #addyb').val();
        if(addyb != null && addyb != ''){
            $.confirm("请选择样本信息!");
        }
    }}, '200')
}
/**
 * 根据内部编码模糊查询库存
 */
$('#ybpickingCarForm #addyb').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#ybpickingCarForm #urlPrefix').val()+'/sample/inventory/pagedataYbkcxxByNbbm',
            type : 'post',
            data : {
                "nbbm" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.map(function(item) {
                        var aItem = {
                            id: item.ybkcid,
                            name : item.nbbm+"-"+item.ybbh+"-"+item.yblxmc+"-"+item.tj+"-"+item.bxh+"-"+item.cth+"-"+item.hh+"-"+item.wz
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
        $('#ybpickingCarForm #addybid').attr('value', item.id);
        return item.name;
    }
});

$(function(){
    //初始化列表
    var oTable=new ybllmxEdit_TableInit();
    oTable.Init();
    //更改前样本kcids
    yybids = $('#ybpickingCarForm #idsyb').val();
    //如果状态为80(审核通过)设置只读
    var zt = $('#zt').val();
    if (zt==$('#CHECK_PASS').val()){
        $('#lldh').attr("disabled", true);
        $('#xllr').attr("disabled", true);
        $('#xbm').attr("disabled", true);
        $('#sqrq').attr("disabled", true);
    }
});

