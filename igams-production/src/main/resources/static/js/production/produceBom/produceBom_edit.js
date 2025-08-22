var t_map = [];
var formAction = $('#produceBomEditForm #formAction').val();
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
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '10%',
        align: 'left',
        formatter:wlbmformat,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'scs',
        title: '生产商',
        titleTooltip:'生产商',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'bzyl',
        title: '标准用量',
        titleTooltip:'标准用量',
        width: '10%',
        align: 'left',
        formatter: bzylformat,
        visible: true
    }, {
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '10%',
        align: 'left',
        formatter: produceBomEditForm_czformat,
        visible: true
    }];

var produceBomEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#produceBomEditForm #produceBom_edit_list').bootstrapTable({
            url: $('#produceBomEditForm #urlPrefix').val()+'/progress/produce/pagedataGetBomDetails?bomid='+$('#produceBomEditForm #bomid').val(),         //请求后台的URL（*）
            method: 'get',                       //请求方式（*）
            toolbar: '#produceBomEditForm #toolbar',                //工具按钮用哪个容器
            striped: true,                       //是否显示行间隔色
            cache: false,                        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                      //是否启用排序
            sortName: "bommx.lrsj",				     //排序字段
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
            uniqueId: "bommx.bommxid",                    //每一行的唯一标识，一般为主键列
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
            sortLastName: "bommx.xgsj", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
function queryByWlbm(wlid){
    var url=$("#produceBomEditForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}
var viewWlConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
    var html = "";
    if(row.wlbm!=null && row.wlbm!=''){
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.zjwlid+"')\">"+row.wlbm+"</a>";
    }
    return html;
}

/**
 * 根据物料名称模糊查询
 */
$('#produceBomEditForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#produceBomEditForm #urlPrefix').val()+'/progress/progress/pagedataQueryWlxxById',
            type : 'post',
            data : {
                "entire" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.dtoList
                    .map(function(item) {
                        var aItem = {
                            id : item.wlid,
                            name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg
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
        $('#produceBomEditForm #addwlid').attr('value', item.id);
        return item.name;
    }
});
/**
 * 选择物料
 * @returns
 */
function chooseWl(){
    var url = $('#produceBomEditForm #urlPrefix').val()+"/storehouse/materiel/pagedataListMater?access_token=" + $("#ac_tk").val();
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
                $.ajax({
                    type : "POST",
                    url : $('#produceBomEditForm #urlPrefix').val()+'/progress/produce/pagedataGetBomByWl',
                    data : {"mjwlid" :sel_row[0].wlid+"","access_token":$("#ac_tk").val(),"nowwlid":$('#produceBomEditForm #nowwlid').val()},
                    dataType : "json",
                    success:function(data){
                        if(data != null && data.status == false){
                            $.error("已存在该母料！");
                            return false;
                        }else {
                            $('#produceBomEditForm #mjwlbm').val(sel_row[0].wlbm);
                            $('#produceBomEditForm #mjwlmc').text(sel_row[0].wlmc);
                            $('#produceBomEditForm #mjwlid').val(sel_row[0].wlid);
                        }
                    }
                });
                return true;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
    if (event.keyCode == "13") {
        cofirmaddBomwl()
    }
})
function cofirmaddBomwl(){
    var wlid=$('#produceBomEditForm #addwlid').val();
    //判断新增输入框是否有内容
    if(!$('#produceBomEditForm #addnr').val()){
        return false;
    }
    setTimeout(function(){ if(wlid != null && wlid != ''){
        $.ajax({
            type:'post',
            url:$('#produceBomEditForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxxById",
            cache: false,
            data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.dtoList!=null && data.dtoList.length>0){
                    if(t_map.rows==null || t_map.rows ==""){
                        t_map.rows=[];
                    }
                    t_map.rows.push(data.dtoList[0]);
                    $("#produceBomEditForm #produceBom_edit_list").bootstrapTable('load',t_map);
                    $('#produceBomEditForm #addwlid').val("");
                    $('#produceBomEditForm #addnr').val("");

                }else{
                    $.confirm("该物料不存在!");
                }
            }
        });
    }else{
        var addnr = $('#produceBomEditForm #addnr').val();
        if(addnr != null && addnr != ''){
            $.confirm("请选择物料信息!");
        }
    }}, '200')
}
/**
 * 标准用量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzylformat(value,row,index){
    if(row.bzyl == null){
        row.bzyl = "";
    }
    var html = "<input id='bzyl_"+index+"' value='"+row.bzyl+"' name='bzyl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  validate='{bzylNumber:true}' onblur= \"checkSum('"+index+"',this,\'bzyl\')\"/>";
    return html;
}

/**
 * 验证标准用量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("bzylNumber", function (value, element){
    if(!value){
        $("#produceBomEditForm #"+element.id).val("")
        return false;
    }else{
        if (value<0){
            $.error("标准用量不能小于0!");
            $("#produceBomEditForm #"+element.id).val("")
        }
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确标准用量格式，可保留两位小数!");
            $("#produceBomEditForm #"+element.id).val("")
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delBommx(index,event){
    t_map.rows.splice(index,1);
    $("#produceBomEditForm #produceBom_edit_list").bootstrapTable('load',t_map);
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function produceBomEditForm_czformat(value,row,index){
    var html="<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除' onclick=\"delBommx('" + index + "',event)\" >删除</span>";
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
        if (zdmc == 'bzyl') {
            t_map.rows[index].bzyl = e.value;
        }
    }
}
$(function(){
    //添加日期控件
    laydate.render({
        elem: '#bbrq'
        ,theme: '#2381E9'
    });
    // 所有下拉框添加choose样式
    jQuery('#produceBomEditForm .chosen-select').chosen({width: '100%'});

    //初始化列表
    var oTable=new produceBomEdit_TableInit();
    oTable.Init();
});