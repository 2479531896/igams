//显示字段
var t_map = [];
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
        field: 'qgid',
        title: '请购id',
        titleTooltip:'请购id',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'qgdh',
        title: '请购单号',
        titleTooltip:'请购单号',
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
        field: 'sfrk',
        title: '是否入库',
        width: '3%',
        align: 'left',
        formatter:sfrkformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '5%',
        align: 'left',
        formatter:xzComfirmCar_czFormat,
        visible: true
    }];

var inConfirmCar_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#xzComfirmCarForm #qrmx_list').bootstrapTable({
            url: $('#xzComfirmCarForm #urlPrefix').val()+'/administration/purchase/pagedataConfirmCarList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#xzComfirmCarForm #toolbar',                //工具按钮用哪个容器
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
                var dzfsid=$("#xzComfirmCarForm #dzfs").val();
                var dzfsdm=$("#xzComfirmCarForm #"+dzfsid).attr("csdm");
                $("#xzComfirmCarForm #dzfsdm").val(dzfsdm);
                var data=$('#xzComfirmCarForm #qrmx_list').bootstrapTable('getData');
                //刷新页面总金额
                var zje=0;
                for(var i=0;i<data.length;i++){
                    if(data[i].jg!=null && data[i].jg!='' && data[i].sl!=null && data[i].sl!=''){
                        zje=parseInt(zje)+parseInt(data[i].jg*100000)*parseInt(data[i].sl*100);
                    }
                }
                zje=(zje/10000000).toFixed(2);
                if(dzfsdm=="MB") {//月结
                    $("#xzComfirmCarForm #zje").val(zje);
                }else{
                    $("#xzComfirmCarForm #fkje").val(zje);
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
            qgmxid: $("#xzComfirmCarForm #qgmxid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

function sfrkformat(value,row,index){
    var html = "";
    if (row.sfrk == null || row.sfrk == ''){
        row.sfrk = '1';
    }
    if (row.sfrk == '1'){
        html = html +
            "<label className='radio-inline' onchange='changeSfrk("+row.sfrk+","+index+")'>" +
            "    <input type='radio' id='sfrk1_"+index+"' value='1' name='sfrk"+index+"' validate='{required:true}' checked>是" +
            "</label>" +
            "<label className='radio-inline' onchange='changeSfrk("+row.sfrk+","+index+")'>" +
            "    <input type='radio' id='sfrk0_"+index+"' value='0' name='sfrk"+index+"' validate='{required:true}'>否" +
            "</label>"
    }else if (row.sfrk == '0'){
        html = html +
            "<label className='radio-inline' onchange='changeSfrk("+row.sfrk+","+index+")'>" +
            "    <input type='radio' id='sfrk1_"+index+"' value='1' name='sfrk"+index+"' validate='{required:true}'>是" +
            "</label>" +
            "<label className='radio-inline' onchange='changeSfrk("+row.sfrk+","+index+")'>" +
            "    <input type='radio' id='sfrk0_"+index+"' value='0' name='sfrk"+index+"' validate='{required:true}' checked>否" +
            "</label>"
    }
    return html;
}

function changeSfrk(row,index){
    var data=$("#xzComfirmCarForm #qrmx_list").bootstrapTable("getData");
    if(data!=null && data.length>0){
        data[index].sfrk=$('input:radio[name="sfrk'+index+'"]:checked').val();
    }
}

//操作格式化
function xzComfirmCar_czFormat(value,row,index){
    return "<span style='margin-left:5px;' class='btn btn-default' title='移出确认车' onclick=\"delConfirmCarMater('" + index + "',event)\" >删除</span>";
}

function xzComfirmCar_jgformat(value,row,index){
    var jg=row.jg;
    if(!row.jg)
        jg="";
    return "<input id='jg_"+index+"' value='"+jg+"'  name='jg_"+index+"' style='width:100%;border-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{qrdjNumber:true,required:true}' autocomplete='off' onblur=\"checkConfirmSum('"+index+"',this,\'jg\')\"></input>";
}

function checkConfirmSum(index,data,zdmc){
    var dzfsid=$("#xzComfirmCarForm #dzfs").val();
    var dzfsdm=$("#xzComfirmCarForm #"+dzfsid).attr("csdm");
    var data=$('#xzComfirmCarForm #qrmx_list').bootstrapTable('getData');
    data[index].jg=$("#xzComfirmCarForm #jg_"+index).val();
    //刷新页面总金额
    var zje=0;
    for(var i=0;i<data.length;i++){
        if(data[i].jg!=null && data[i].jg!='' && data[i].sl!=null && data[i].sl!=''){
            zje=parseInt(zje)+parseInt(data[i].jg*100000)*parseInt(data[i].sl*100);
        }
    }
    zje=(zje/10000000).toFixed(2);
    if(dzfsdm=="MB") {//月结
        $("#xzComfirmCarForm #zje").val(zje);
    }else{
        $("#xzComfirmCarForm #fkje").val(zje);
    }
}

/**
 * 验证数量格式(五位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("qrdjNumber", function (value, element){
    if(value && !/^\d+(\.\d{1,5})?$/.test(value)){
        $.error("请填写正确单价格式，可保留五位小数!");
    }
    return this.optional(element) || /^\d+(\.\d{1,5})?$/.test(value);
},"请填写正确格式，可保留五位小数。");

//将未确认明细移出确认车
function delConfirmCarMater(index,event){
    var sel_row = $('#xzComfirmCarForm #qrmx_list').bootstrapTable('getData');//获取选择行数据
    var qgmxid=sel_row[index].qgmxid;
    $.ajax({
        type:'post',
        url:$('#xzComfirmCarForm #urlPrefix').val()+"/administration/purchase/pagedataDelConfirmCar",
        cache: false,
        data: {"qgmxid":qgmxid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
        	t_map.rows.splice(index,1);
            $('#xzComfirmCarForm #qrmx_list').bootstrapTable('load',t_map);
            ConfirmCar_addOrDelNum("del");
			$("#unconfirmPurchaseAdministration_formSearch #t"+qgmxid).remove();
			var html="<span id='t"+qgmxid+"' class='btn btn-info' title='加入确认车' onclick=\"addConfirmCar('" + qgmxid + "',event)\" >确认</span>";
			$("#unconfirmPurchaseAdministration_formSearch #qgmxid"+qgmxid).append(html);
        }
    });
}

//点击选择按钮选择未确认信息
function chooseUnConfirmQgmx(){
    var url = $('#xzComfirmCarForm #urlPrefix').val()+"/administration/purchase/pagedataGetUnConfirmMsgPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择需要确认的信息', chooseConfirmConfig);
}

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
                var t_data=$('#xzComfirmCarForm #qrmx_list').bootstrapTable('getData');
                var dzfsid=$("#xzComfirmCarForm #dzfs").val();
                var dzfsdm=$("#xzComfirmCarForm #"+dzfsid).attr("csdm");
                //刷新页面总金额
                var zje=0;
                for(var i=0;i<t_data.length;i++){
                    if(t_data[i].jg!=null && t_data[i].jg!='' && t_data[i].sl!=null && t_data[i].sl!=''){
                        zje=parseInt(zje)+parseInt(t_data[i].jg*100000)*parseInt(t_data[i].sl*100);
                    }
                }
                if(data!=null && data.length>0){
                    for(var i=0;i<data.length;i++){
                        t_data.push(data[i]);
                        if(data[i].jg!=null && data[i].jg!='' && data[i].sl!=null && data[i].sl!=''){
                            zje=parseInt(zje)+parseInt(data[i].jg*100000)*parseInt(data[i].sl*100);
                        }
                    }
                }
                zje=(zje/10000000).toFixed(2);
                if(dzfsdm=="MB") {//月结
                    $("#xzComfirmCarForm #zje").val(zje);
                }else{
                    $("#xzComfirmCarForm #fkje").val(zje);
                }
                $("#xzComfirmCarForm #qrmx_list").bootstrapTable("load",t_data);
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

function btnBind(){
    var sel_dzfs=$("#xzComfirmCarForm #dzfs");
    //对账方式下拉事件
    sel_dzfs.unbind("change").change(function(){
        dzfsEvent();
    });
}

function dzfsEvent(){
    var dzfsid=$("#xzComfirmCarForm #dzfs").val();
    var dzfsdm=$("#xzComfirmCarForm #"+dzfsid).attr("csdm");
    if(dzfsdm=="MB"){//月结
        $("#xzComfirmCarForm .yj").attr("style","display:block;height:40px;");
        $("#xzComfirmCarForm .gdg").attr("style","display:none;");
        $("#xzComfirmCarForm .gdg_form").removeAttr("validate");
        $("#xzComfirmCarForm .yj_form").attr("validate","{required:true}");
        $("#xzComfirmCarForm #zje").val($("#xzComfirmCarForm #fkje").val());
    }else{//公对公2
        $("#xzComfirmCarForm .gdg").attr("style","display:block;height:40px;");
        $("#xzComfirmCarForm .yj").attr("style","display:none;");
        $("#xzComfirmCarForm .yj_form").removeAttr("validate");
        $("#xzComfirmCarForm .gdg_form").attr("validate","{required:true}");
        $("#xzComfirmCarForm #fkje").val($("#xzComfirmCarForm #zje").val());
    }
    $.ajax({
        type:'post',
        url:$('#xzComfirmCarForm #urlPrefix').val()+"/administration/purchase/pagedataRegenerateDjh",
        cache: false,
        data: {"dzfsdm":dzfsdm,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            if(dzfsdm=="MB"){
                $("#xzComfirmCarForm #qrdh").val(data.djh);
            }else{
                $("#xzComfirmCarForm #fkdh").val(data.djh);
            }
        }
    });

}

//初始化
function init(){
    var dzfsid=$("#xzComfirmCarForm #dzfs").val();
    var dzfsdm=$("#xzComfirmCarForm #"+dzfsid).attr("csdm");
    if(dzfsdm=="MB"){//月结
        $("#xzComfirmCarForm .yj").attr("style","display:block;height:40px;");
        $("#xzComfirmCarForm .gdg").attr("style","display:none;");
        $("#xzComfirmCarForm .gdg_form").removeAttr("validate");
        $("#xzComfirmCarForm .yj_form").attr("validate","{required:true}");
    }else{//公对公
        $("#xzComfirmCarForm .gdg").attr("style","display:block;height:40px;");
        $("#xzComfirmCarForm .yj").attr("style","display:none;");
        $("#xzComfirmCarForm .yj_form").removeAttr("validate");
        $("#xzComfirmCarForm .gdg_form").attr("validate","{required:true}");
    }
}

/**
 * 选择负责人列表
 * @returns
 */
function chooseFzr(){
    var url=$('#xzComfirmCarForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
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
                    $('#xzComfirmCarForm #sqr').val(sel_row[0].yhid);
                    $('#xzComfirmCarForm #sqrmc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#xzComfirmCarForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
    var url=$('#xzComfirmCarForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
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
                    $('#xzComfirmCarForm #sqbm').val(sel_row[0].jgid);
                    $('#xzComfirmCarForm #sqbmmc').val(sel_row[0].jgmc);
                    $('#xzComfirmCarForm #jgdm').val(sel_row[0].jgdm);
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
        elem: '#xzComfirmCarForm #zwfkrq'
        ,theme: '#2381E9'
    });
    // 1.初始化Table
    var oTable = new inConfirmCar_TableInit();
    oTable.Init();
    init();
    btnBind();
    //所有下拉框添加choose样式
    jQuery('#xzComfirmCarForm .chosen-select').chosen({width: '100%'});
});