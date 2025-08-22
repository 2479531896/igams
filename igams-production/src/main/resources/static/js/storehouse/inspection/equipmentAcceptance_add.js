$(document).ready(function(){
    //添加日期控件
    laydate.render({
        elem: '#gzsj'
        ,theme: '#2381E9'
    });
    jQuery('#equipmentAdd_formSearch .chosen-select').chosen({width: '100%'});
});

/**
 * 判断金额格式
 */
function checkJe(e) {
    if (e.value!='') {
        if (!/^\d+(\.\d{1,2})?$/.test(e.value) || e.value <= 0) {
            $.error("请填写正确格式，可保留两位小数!");
            $("#equipmentAdd_formSearch #je").val("")
        }
    }
}
/**
 * 选择请购单据号(及明细)
 * @returns
 */
function chooseQgd(){
    var url = $('#equipmentAdd_formSearch #urlPrefix').val() + "/purchase/purchase/pagedataChooseQgd?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择请购单号', chooseQgdConfig);
}
var chooseQgdConfig = {
    width : "1000px",
    modalName	: "chooseQgdModal",
    formName	: "chooseQgdForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#chooseQgdForm #purchase_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length !=1){
                    $.alert("请选中一行");
                    return false;
                }else {
                    $('#equipmentAdd_formSearch #djh').val(sel_row[0].djh);
                    $('#equipmentAdd_formSearch #je').val(sel_row[0].jg);
                    $('#equipmentAdd_formSearch #sbmc').val(sel_row[0].wlmc_t);
                    $('#equipmentAdd_formSearch #ggxh').val(sel_row[0].gg_t);
                    $('#equipmentAdd_formSearch #sccj').val(sel_row[0].scs);
                    $('#equipmentAdd_formSearch #qgmxid').val(sel_row[0].qgmxid);
                }
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
    var url=$('#equipmentAdd_formSearch #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择供应商',addGysConfig);
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
                    $('#equipmentAdd_formSearch #gys').val(sel_row[0].gysid);
                    $('#equipmentAdd_formSearch #gysmc').val(sel_row[0].gysmc);
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
 * 选择管理员
 * @returns
 */
function selectglry(){
    var url=$('#equipmentAdd_formSearch #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择管理人员',addGlryConfig);
}
var addGlryConfig = {
    width		: "800px",
    modalName	:"addGlryModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#equipmentAdd_formSearch #glry').val(sel_row[0].yhid);
                    $('#equipmentAdd_formSearch #glrymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#equipmentAdd_formSearch #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"glry":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
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