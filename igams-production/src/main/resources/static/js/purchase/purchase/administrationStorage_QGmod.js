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
        field: 'djh',
        title: '请购单号',
        titleTooltip:'请购单号',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'hwmc',
        title: '货物名称',
        titleTooltip:'货物名称',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'hwbz',
        title: '货物标准',
        titleTooltip:'货物标准',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'hwjldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'krksl',
        title: '可入库数量',
        titleTooltip:'可入库数量',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'rksl',
        title: '入库数量',
        titleTooltip:'入库数量',
        formatter:rkslformat,
        width: '5%',
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
        $('#administrationQGmodForm #tb_list').bootstrapTable({
            url: $('#administrationQGmodForm #urlPrefix').val()+'/purchase/purchase/pagedataAdminInStoreDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#administrationQGmodForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "xh",				//排序字段
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
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                var json=[];
                t_map = map;
                for(var i=0;i<t_map.rows.length;i++){
                    var sz={"index":'',"xzrkmxid":'',"qgmxid":'',"djh":'',"hwmc":'',"hwbz":'',"hwjldw":'',"krksl":'',"rksl":'',"yrksl":'',"kcl":'',"xzrksl":'',"xzkcid":'',"xzkcl":''};
                    sz.index=i;
                    sz.qgmxid=t_map.rows[i].qgmxid;
                    sz.xzrkmxid=t_map.rows[i].xzrkmxid;
                    sz.djh=t_map.rows[i].djh;
                    sz.rksl=t_map.rows[i].rksl;
                    sz.hwmc=t_map.rows[i].hwmc;
                    sz.hwbz=t_map.rows[i].hwbz;
                    sz.hwjldw=t_map.rows[i].hwjldw;
                    sz.krksl=t_map.rows[i].krksl;
                    sz.yrksl=t_map.rows[i].yrksl;
                    sz.kcl=t_map.rows[i].kcl;
                    sz.xzrksl=t_map.rows[i].xzrksl;
                    sz.xzkcid=t_map.rows[i].xzkcid;
                    sz.xzkcl=t_map.rows[i].xzkcl;
                    json.push(sz);
                }
                $("#administrationQGmodForm #qgmx_yjson").val(JSON.stringify(json));
                if(t_map.rows.length>0){
                    $("#administrationQGmodForm #qgid").tagsinput({
                        itemValue: "qgid",
                        itemText: "djh",
                    })
                    for(var i = 0; i < t_map.rows.length; i++){
                        var value =  t_map.rows[i].qgid;
                        var text =  t_map.rows[i].djh;
                        $("#administrationQGmodForm #qgid").tagsinput('add', {"qgid":value,"djh":text});
                    }
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
            sortLastName: "qgmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            xzrkid: $("#administrationQGmodForm #xzrkid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};


/**
 * 入库数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function rkslformat(value,row,index){

    if(row.rksl == null){
        row.rksl = row.krksl;
    }
    var html ="";
    html += "<input id='rksl_"+index+"' value='"+row.rksl+"' name='rksl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{rkslNumber:true}' onblur=\"checkSum('"+index+"',this,\'rksl\')\"></input>";
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
    $("#administrationQGmodForm #tb_list").bootstrapTable('load',t_map);
}

/**
 * 初始化页面
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#administrationQGmodForm #rkrq'
        ,theme: '#2381E9'
    });
}

/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#administrationQGmodForm #qgid').on('beforeItemRemove', function(event) {
    $.confirm('您确定要删除请购单下的所有明细记录吗？',function(result){
        if(result){
            var qgid = event.item.qgid;
            for (var i = t_map.rows.length-1; i >= 0 ; i--) {
                if(t_map.rows[i].qgid == qgid){
                    t_map.rows.splice(i,1);

                }
            }
            $("#administrationQGmodForm #tb_list").bootstrapTable('load',t_map);
        }
    })
});
/**
 * 监听单据号标签点击事件
 */
var tagClick = $("#administrationQGmodForm").on('click','.label-info',function(e){
    event.stopPropagation();
    var url = $('#administrationQGmodForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseListAdminPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择明细', chooseEditMxConfig);
});
var chooseEditMxConfig = {
    width : "1000px",
    modalName	: "chooseEditMxModal",
    formName	: "chooseMxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseMxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                if($("#chooseMxForm input[name='checkQgmx']:checked").length == 0){
                    $.alert("未选中请购明细信息！");
                    return false;
                }
                // 获取更改请购单和明细信息
                var qgmxJson = [];
                $("#chooseMxForm input[name='checkQgmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"qgid":'',"qgmxid":''};
                        sz.qgid = qgid;
                        sz.qgmxid = this.dataset.qgmxid;
                        qgmxJson.push(sz);
                    }
                })

                // 获取新增请购明细
                var ids="";
                for (var i = 0; i < qgmxJson.length; i++) {
                    var isAdd = true;
                    for (var j = 0; j < t_map.rows.length; j++) {
                        if(t_map.rows[j].qgmxid == qgmxJson[i].qgmxid){
                            isAdd = false;
                            break;
                        }
                    }
                    if(isAdd){
                        ids = ids + ","+ qgmxJson[i].qgmxid;
                    }
                }
                // 获取删除请购明细
                var qgid = $("#chooseMxForm input[name='qgid']").val();
                for (var j = t_map.rows.length-1; j >=0; j--) {
                    if(qgid == t_map.rows[j].qgid){
                        var isDel = true;
                        for (var i = 0; i < qgmxJson.length; i++) {
                            if(t_map.rows[j].qgmxid == qgmxJson[i].qgmxid){
                                isDel = false;
                                break;
                            }
                        }
                        if(isDel){
                            t_map.rows.splice(j,1);
                        }
                    }
                }
                if(ids.length > 0){
                    ids = ids.substr(1);
                    // 查询信息并更新到页面
                    $.post($('#administrationQGmodForm #urlPrefix').val() + "/purchase/purchase/pagedataGetPurchaseInfo",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
                        var htmxDtos = data.htmxDtos;
                        if(htmxDtos != null && htmxDtos.length > 0){
                            // 更新页面列表(新增)
                            for (var i = 0; i < htmxDtos.length; i++) {
                                if(t_map.rows){
                                    t_map.rows.push(htmxDtos[i]);
                                }else{
                                    t_map.rows= [];
                                    t_map.rows.push(htmxDtos[i]);
                                }
                            }
                            $("#administrationQGmodForm #tb_list").bootstrapTable('load',t_map);
                            $.closeModal(opts.modalName);
                        }
                    },'json');
                }else{
                    $("#administrationQGmodForm #tb_list").bootstrapTable('load',t_map);
                    $.closeModal(opts.modalName);
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
 * 选择人员列表
 * @returns
 */
function chooseRy(){
    var url=$('#administrationQGmodForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
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
                    $('#administrationQGmodForm #rkry').val(sel_row[0].yhid);
                    $('#administrationQGmodForm #rkrymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#administrationQGmodForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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

/**
 * 选择明细
 * @returns
 */
function chooseQg(){
    var url = $('#administrationQGmodForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseAdminPurchaseList?access_token=" + $("#ac_tk").val()+"&zt=80";
    $.showDialog(url, '选择请购单', chooseQgConfig);
}
var chooseQgConfig = {
    width : "1000px",
    modalName	: "chooseQgModal",
    formName	: "chooseQgForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseQgForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取更改后的请购单和明细信息
                var qgmxJson = [];
                if($("#chooseQgForm #qgmx_json").val()){
                    qgmxJson = JSON.parse($("#chooseQgForm #qgmx_json").val());
                    // 判断新增的请购明细信息
                    var ids="";
                    for (var i = 0; i < qgmxJson.length; i++) {
                        var isAdd = true;
                        for (var j = 0; j < t_map.rows.length; j++) {
                            if(t_map.rows[j].qgmxid == qgmxJson[i].qgmxid){
                                isAdd = false;
                                break;
                            }
                        }
                        if(isAdd){
                            ids = ids + ","+ qgmxJson[i].qgmxid;
                        }
                    }
                    if(ids.length > 0){
                        ids = ids.substr(1);
                        // 查询信息并更新到页面
                        $.post($('#administrationQGmodForm #urlPrefix').val() + "/purchase/purchase/pagedataGetPurchaseInfo",{"ids":ids,"access_token":$("#ac_tk").val()},function(data){
                            var mxdtos = data.htmxDtos;
                            if(mxdtos != null && mxdtos.length > 0){
                                // 更新页面列表(新增)
                                for (var i = 0; i < mxdtos.length; i++) {
                                    if(i == 0){
                                        if(!t_map.rows){
                                            t_map.rows= [];
                                        }
                                    }
                                    t_map.rows.push(mxdtos[i]);
                                }
                            }
                            $("#administrationQGmodForm #tb_list").bootstrapTable('load',t_map);
                            // 请购单号
                            var selDjh = $("#chooseQgForm #yxdjh").tagsinput('items');
                            $("#administrationQGmodForm #qgid").tagsinput({
                                itemValue: "qgid",
                                itemText: "djh",
                            })
                            for(var i = 0; i < selDjh.length; i++){
                                var value = selDjh[i].value;
                                var text = selDjh[i].text;
                                $("#administrationQGmodForm #qgid").tagsinput('add', {"qgid":value,"djh":text});
                            }
                            $.closeModal(opts.modalName);
                        },'json');
                    }else{
                        $("#administrationQGmodForm #tb_list").bootstrapTable('load',t_map);
                        // 请购单号
                        var selDjh = $("#chooseQgForm #yxdjh").tagsinput('items');
                        $("#administrationQGmodForm #qgid").tagsinput({
                            itemValue: "qgid",
                            itemText: "djh",
                        })
                        for(var i = 0; i < selDjh.length; i++){
                            var value = selDjh[i].value;
                            var text = selDjh[i].text;
                            $("#administrationQGmodForm #qgid").tagsinput('add', {"qgid":value,"djh":text});
                        }
                        $.closeModal(opts.modalName);
                    }
                }else{
                    $.alert("未获取到选中的请购信息！");
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



$(document).ready(function(){
    //初始化列表
    var oTable=new inStore_TableInit();
    oTable.Init();
    init();
    //所有下拉框添加choose样式
    jQuery('#administrationQGmodForm .chosen-select').chosen({width: '100%'});
});