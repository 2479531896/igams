var t_map = [];
// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '4%',
        align: 'left',
        visible:true
    }, {
        field: 'hwmc',
        title: '货物名称',
        titleTooltip:'货物名称',
        width: '30%',
        align: 'left',
        formatter:hwmcformat,
        visible: true
    }, {
        field: 'hwbz',
        title: '货物标准',
        titleTooltip:'货物标准',
        width: '30%',
        align: 'left',
        formatter:hwbzformat,
        visible: true
    }, {
        field: 'rksl',
        title: '入库数量',
        titleTooltip:'入库数量',
        formatter:rkslformat,
        width: '30%',
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
var inStore_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#administrationQTmodForm #tb_list').bootstrapTable({
            url: $('#administrationQTmodForm #urlPrefix').val()+'/purchase/purchase/pagedataAdminInStoreDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#administrationQTmodForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "",				//排序字段
            sortOrder: "",                   //排序方式
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
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map=map;
                var json=[];
                for(var i=0;i<t_map.rows.length;i++){
                    var sz={"index":'',"xzrkmxid":'',"hwmc":'',"hwbz":'',"hwjldw":'',"rksl":'',"kcl":'',"xzrksl":'',"xzkcid":'',"xzkcl":''};
                    sz.index=i;
                    sz.xzrkmxid=t_map.rows[i].xzrkmxid;
                    sz.rksl=t_map.rows[i].rksl;
                    sz.hwmc=t_map.rows[i].hwmc;
                    sz.hwbz=t_map.rows[i].hwbz;
                    sz.hwjldw=t_map.rows[i].hwjldw;
                    sz.kcl=t_map.rows[i].kcl;
                    sz.xzrksl=t_map.rows[i].xzrksl;
                    sz.xzkcid=t_map.rows[i].xzkcid;
                    sz.xzkcl=t_map.rows[i].xzkcl;
                    json.push(sz);
                }
                $("#administrationQTmodForm #qgmx_yjson").val(JSON.stringify(json));
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
            sortLastName: "", //防止同名排位用
            sortLastOrder: "", //防止同名排位用
            xzrkid: $("#administrationQTmodForm #xzrkid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
/**
 * 货物名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hwmcformat(value,row,index){
    if(!row.hwmc){
        row.hwmc=''
    }
    var html ="";
    html += "<input id='hwmc_"+index+"' value='"+row.hwmc+"'  name='hwmc_"+index+"' style='width:100%;border:1px solid #D5D5D5;'  onblur=\"checkSum('"+index+"',this,\'hwmc\')\"></input>";
    return html;
}
/**
 * 货物标准格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hwbzformat(value,row,index){
    if(!row.hwbz){
        row.hwbz=''
    }
    var html ="";
    html += "<input id='hwbz_"+index+"' value='"+row.hwbz+"' name='hwbz_"+index+"' style='width:100%;border:1px solid #D5D5D5;'  onblur=\"checkSum('"+index+"',this,\'hwbz\')\"></input>";
    return html;
}
/**
 * 入库数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function rkslformat(value,row,index){
    if(!row.rksl){
        row.rksl=''
    }
    var html ="";
    html += "<input id='rksl_"+index+"' value='"+row.rksl+"'  name='rksl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{rkslNumber:true}' onblur=\"checkSum('"+index+"',this,\'rksl\')\"></input>";
    return html;
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("rkslNumber", function (value, element){
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
    var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除明细' onclick=\"delPurchaseDetail('" + index + "',event)\" >删除</span>";
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
        if (zdmc == 'rksl') {
            var rksl = e.value;
            t_map.rows[index].rksl = rksl;
        }
        if (zdmc == 'hwmc') {
            var hwmc = e.value;
            t_map.rows[index].hwmc = hwmc;
        }
        if (zdmc == 'hwbz') {
            var hwbz = e.value;
            t_map.rows[index].hwbz = hwbz;
        }
    }
}

/**
 * 删除操作(从合同明细删除)
 * @param index
 * @param event
 * @returns
 */
function delPurchaseDetail(index,event){
    t_map.rows.splice(index,1);
    $("#administrationQTmodForm #tb_list").bootstrapTable('load',t_map);
}

/**
 * 初始化页面
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#administrationQTmodForm #rkrq'
        ,theme: '#2381E9'
    });
}




/**
 * 选择人员列表
 * @returns
 */
function chooseRy(){
    var url=$('#administrationQTmodForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',addFzrConfig);
}
var addFzrConfig = {
    width		: "800px",
    modalName	:"addFzrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#administrationQTmodForm #rkry').val(sel_row[0].yhid);
                    $('#administrationQTmodForm #rkrymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#administrationQTmodForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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

//新增明细
function addnew(){
    var a=[];
    t_map.rows.push(a);
    $("#administrationQTmodForm #tb_list").bootstrapTable('load',t_map);
}

$(document).ready(function(){
    //初始化列表
    var oTable=new inStore_TableInit();
    oTable.Init();
    init();
    //所有下拉框添加choose样式
    jQuery('#administrationQTmodForm .chosen-select').chosen({width: '100%'});
});