var t_map = [];
//显示字段
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
    },{
        field: 'qgdh',
        title: '请购单号',
        titleTooltip:'请购单号',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'qrdh',
        title: '确认单号',
        titleTooltip:'确认单号',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'hwmc',
        title: '货物名称',
        titleTooltip:'货物名称',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'hwgg',
        title: '货物规格',
        titleTooltip:'货物规格',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'jg',
        title: '价格',
        titleTooltip:'价格',
        width: '5%',
        align: 'left',
        formatter:xzComfirmCar_jgformat,
        visible: true
    },{
        field: 'hwjldw',
        title: '单位',
        titleTooltip:'单位',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '5%',
        align: 'left',
        formatter:xzComfirmCar_czFormat,
        visible: true
    },{
        field: 'fkmxid',
        title: '付款明细ID',
        titleTooltip:'付款明细ID',
        width: '5%',
        align: 'left',
        visible: false
    }];

var inConfirmCar_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#xzqgfkEditForm #xzfkmx_list').bootstrapTable({
            url: $('#xzqgfkEditForm #urlPrefix').val()+'/administration/administrationPay/pagedataFkmxlist?fkid='+$("#xzqgfkEditForm #fkid").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#xzqgfkEditForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "hwmc",				//排序字段
            sortOrder: "desc",                   //排序方式
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
                t_map = map;
                if(t_map.rows){
                    // 初始化fkmx_json
                    var json = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"qrid":'',"qrmxid":''};
                        sz.qrid = t_map.rows[i].qrid;
                        sz.qrmxid = t_map.rows[i].qrmxid;
                        json.push(sz);
                    }
                    $("#xzqgfkEditForm #fkmx_json").val(JSON.stringify(json));
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
            qgmxid: $("#xzqgfkEditForm #qgmxid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
/**
 * 选择供应商列表
 * @returns
 */
function chooseZfdx(){
    var url=$('#xzqgfkEditForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择支付对象',addGysConfig);
}
var addGysConfig = {
    width		: "1200px",
    modalName	:"addGysModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
                if(sel_row.length == 1){
                    var gysid = sel_row[0].gysid;
                    var gysmc = sel_row[0].gysmc;
                    var khh = sel_row[0].khh;
                    var zh = sel_row[0].zh;
                    $("#xzqgfkEditForm #zfdxid").val(gysid);
                    $("#xzqgfkEditForm #zfdx").val(gysmc);
                    $("#xzqgfkEditForm #zffkhh").val(khh);
                    $("#xzqgfkEditForm #zffyhzh").val(zh);
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
//操作格式化
function xzComfirmCar_czFormat(value,row,index){
    return "<span style='margin-left:5px;' class='btn btn-default' title='移出确认车' onclick=\"delConfirmCarMater('" + index + "',event)\" >删除</span>";
}

function xzComfirmCar_jgformat(value,row,index){
    var jg=row.jg;
    if(!row.jg)
        jg="";
    return "<input id='jg_"+index+"' value='"+jg+"'  name='jg_"+index+"' style='width:100%;border-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{qrdjNumber:true}' autocomplete='off' onblur=\"checkConfirmSum('"+index+"',this,\'jg\')\"></input>";
}

function checkConfirmSum(index,data,zdmc){
    var dzfsid=$("#xzqgfkEditForm #dzfs").val();
    var dzfsdm=$("#xzqgfkEditForm #"+dzfsid).attr("csdm");
    var data=$('#xzqgfkEditForm #xzfkmx_list').bootstrapTable('getData');
    data[index].jg=$("#xzqgfkEditForm #jg_"+index).val();
    //刷新页面总金额
    var zje=0;
    for(var i=0;i<data.length;i++){
        if(data[i].jg!=null && data[i].jg!='' && data[i].sl!=null && data[i].sl!=''){
            zje=parseInt(zje)+parseInt(data[i].jg*100000)*parseInt(data[i].sl*100);
        }
    }
    zje=(zje/10000000).toFixed(2);
    $("#xzqgfkEditForm #fkje").val(zje);
}

//刷新总价
function zjinit(){
    var dzfsid=$("#xzqgfkEditForm #dzfs").val();
    var dzfsdm=$("#xzqgfkEditForm #dzfsdm").val();
    var data=$('#xzqgfkEditForm #xzfkmx_list').bootstrapTable('getData');
    //刷新页面总金额
    var zje=0;
    for(var i=0;i<data.length;i++){
        if(data[i].jg!=null && data[i].jg!='' && data[i].sl!=null && data[i].sl!=''){
            zje=parseInt(zje)+parseInt(data[i].jg*100000)*parseInt(data[i].sl*100);
        }
    }
    zje=(zje/10000000).toFixed(2);
    $("#xzqgfkEditForm #fkje").val(zje);
}

//校验数量格式，五位小数
jQuery.validator.addMethod("qrdjNumber", function (value, element){
    if(value && !/^\d+(\.\d{1,5})?$/.test(value)){
        $.error("请填写正确单价格式，可保留五位小数!");
    }
    return this.optional(element) || /^\d+(\.\d{1,5})?$/.test(value);
},"请填写正确格式，可保留五位小数。");

//将未确认明细移出确认车
function delConfirmCarMater(index,event){
    var list = new Array();
    var sel_row = $('#xzqgfkEditForm #xzfkmx_list').bootstrapTable('getData');//获取选择行数据
    var qgmxid=sel_row[index].qgmxid;
    for (var i =0; i<sel_row.length; i++){
        var qgmxid_x = sel_row[i].qgmxid;
        if (qgmxid != qgmxid_x){
            list.push(sel_row[i]);
        }
    }
    $("#xzqgfkEditForm #xzfkmx_list").bootstrapTable("load",list);
}

//对账方式点击修改时候，根据选中项是公对公还是月结来显示不同内容
function btnBind(){
    var sel_dzfs=$("#xzqgfkEditForm #dzfs");
    //对账方式下拉事件
    sel_dzfs.unbind("change").change(function(){
        dzfsEvent();
    });
}

function dzfsEvent(){
    var dzfsid=$("#xzqgfkEditForm #dzfs").val();
    var dzfsdm=$("#xzqgfkEditForm #"+dzfsid).attr("csdm");
    $("#xzqgfkEditForm #fkje").val($("#xzqgfkEditForm #zje").val());
    t_map=[];
    $("#xzqgfkEditForm #xzfkmx_list").bootstrapTable('load',t_map);
}

//点击选中按钮选中数据，选择月结则是已确认数据，公对公则是未确认数据
function chooseData(){
    var dzfs=$("#xzqgfkEditForm #dzfs").val();
    if(dzfs==null || dzfs==''){
        $.confirm("请先选择对账方式!");
        return false;
    }
    var dzfsdm=$("#xzqgfkEditForm #"+dzfs).attr("csdm");
    var url="";
    if(dzfsdm=="MB"){//月结
        url = $('#xzqgfkEditForm #urlPrefix').val()+"/administration/administration/pagedataChooseListConfirm?access_token=" + $("#ac_tk").val();
        $.showDialog(url, '选择需要付款的信息', chooseQrxxConfig);
    }else{//公对公
        var url = $('#xzqgfkEditForm #urlPrefix').val()+"/administration/purchase/pagedataGetUnConfirmMsgPage?access_token=" + $("#ac_tk").val()+"&flag=fkxz";
        $.showDialog(url, '选择需要付款的信息', chooseConfirmConfig);
    }
}

var chooseQrxxConfig = {
    width : "1000px",
    modalName	: "chooseQrxxModal",
    formName	: "chooseQrxxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#confirmChoose_xz_formSearch").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取未更改请购单和明细信息
                var preJson = [];
                if($("#xzqgfkEditForm #fkmx_json").val()){
                    preJson = JSON.parse($("#xzqgfkEditForm #fkmx_json").val());
                }
                // 获取更改后的请购单和明细信息
                var qrmxJson = [];
                if($("#confirmChoose_xz_formSearch #qrmx_json").val()){
                    qrmxJson = JSON.parse($("#confirmChoose_xz_formSearch #qrmx_json").val());

                    // 判断新增的请购明细信息
                    var ids="";
                    for (var i = 0; i < qrmxJson.length; i++) {
                        var isAdd = true;
                        for (var j = 0; j < preJson.length; j++) {
                            if(preJson[j].qrmxid == qrmxJson[i].qrmxid){
                                isAdd = false;
                                break;
                            }
                        }
                        if(isAdd){
                            ids = ids + ","+ qrmxJson[i].qrmxid;
                        }
                    }
                    if(ids.length > 0){
                        ids = ids.substr(1);
                        var fkid = $("#xzqgfkEditForm #fkid").val();
                        // 查询信息并更新到页面
                        $.post($('#xzqgfkEditForm #urlPrefix').val() + "/administration/purchase/pagedataListQrmxListInfo",{"ids":ids, "fkid":fkid, "access_token":$("#ac_tk").val()},function(data){
                            var fkmxDtos = data.rows;
                            if(fkmxDtos != null && fkmxDtos.length > 0){
                                // 更新页面列表(新增)
                                for (var i = 0; i < fkmxDtos.length; i++) {
                                    if(i == 0){
                                        if(!t_map.rows){
                                            t_map.rows= [];
                                        }
                                        if(!$("#xzqgfkEditForm #sqbm").val()){
                                            $("#xzqgfkEditForm #sqbm").val(fkmxDtos[i].sqbm);
                                            $("#xzqgfkEditForm #sqbmmc").val(fkmxDtos[i].sqbmmc);
                                        }
                                        if(!$("#xzqgfkEditForm #fzr").val()){
                                            $("#xzqgfkEditForm #fzr").val(fkmxDtos[i].fzr);
                                            $("#xzqgfkEditForm #fzrmc").val(fkmxDtos[i].fzrmc);
                                        }
                                    }
                                    t_map.rows.push(fkmxDtos[i]);
                                    var sz = {"qrid":'',"qrmxid":''};
                                    sz.qrid = fkmxDtos[i].qrid;
                                    sz.qrmxid = fkmxDtos[i].qrmxid;
                                    preJson.push(sz);
                                }
                            }
                            $("#xzqgfkEditForm #xzfkmx_list").bootstrapTable('load',t_map.rows);
                            $("#xzqgfkEditForm #fkmx_json").val(JSON.stringify(preJson));
                            zjinit();
                            $.closeModal(opts.modalName);
                        },'json');
                    }else{
                        $("#xzqgfkEditForm #xzfkmx_list").bootstrapTable('load',t_map.rows);
                        $("#xzqgfkEditForm #fkmx_json").val(JSON.stringify(preJson));
                        zjinit();
                        $.closeModal(opts.modalName);
                    }

                }else{
                    $.alert("未获取到选中的确认信息！");
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

var chooseConfirmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseConfirmModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var data=$("#unconfirminfo_xz_formSearch #xzwqr_list").bootstrapTable("getSelections");
                var t_data=$('#xzqgfkEditForm #xzfkmx_list').bootstrapTable('getData');
                if(data!=null && data.length>0){
                    for(var i=0;i<data.length;i++){
                        t_data.push(data[i]);
                    }
                }
                $("#xzqgfkEditForm #xzfkmx_list").bootstrapTable("load",t_map);
                zjinit();
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

/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
    var url=$('#xzqgfkEditForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择申请部门',addSqbmConfig);
}
var addSqbmConfig = {
    width		: "800px",
    modalName	:"addSqbmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#xzqgfkEditForm #sqbm').val(sel_row[0].jgid);
                    $('#xzqgfkEditForm #sqbmmc').val(sel_row[0].jgmc);
                    $('#xzqgfkEditForm #jgdm').val(sel_row[0].jgdm);
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
 * 选择负责人列表
 * @returns
 */
function chooseFzr(){
    var url=$('#xzqgfkEditForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
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
                    $('#xzqgfkEditForm #sqr').val(sel_row[0].yhid);
                    $('#xzqgfkEditForm #sqrmc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#xzqgfkEditForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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



$(document).ready(function(){
    //添加日期控件
    laydate.render({
        elem: '#xzqgfkEditForm #zwfkrq'
        ,theme: '#2381E9'
    });
    // 1.初始化Table
    var oTable = new inConfirmCar_TableInit();
    oTable.Init();

    btnBind();
    //所有下拉框添加choose样式
    jQuery('#xzqgfkEditForm .chosen-select').chosen({width: '100%'});
});