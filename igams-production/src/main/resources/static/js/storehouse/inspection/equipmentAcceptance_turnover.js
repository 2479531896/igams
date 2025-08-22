/**
 * 选择使用人员
 * @returns
 */
function selectxsyry(){
    var url=$('#turnover_formSearch #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择管理人员',addXsyryConfig);
}
var addXsyryConfig = {
    width		: "800px",
    modalName	:"addXsyryModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#turnover_formSearch #xsyr').val(sel_row[0].yhid);
                    $('#turnover_formSearch #xsyrmc').val(sel_row[0].zsxm);
                    $('#turnover_formSearch #xsyrddid').val(sel_row[0].ddid);
                    $('#turnover_formSearch #xsyryhm').val(sel_row[0].yhm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#turnover_formSearch #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(){
                            $.ajax({
                                type:'post',
                                url:$('#turnover_formSearch #urlPrefix').val() + "/recruit/recruit/pagedataJgxx",
                                cache: false,
                                data: {"fqr":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                                dataType:'json',
                                success:function(data){
                                    $('#turnover_formSearch #xsybmmc').val(data.rszpDto[0].jgmc);
                                    $('#turnover_formSearch #xsybm').val(data.rszpDto[0].jgid);
                                }
                            });
                        }
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
 * 选择现使用部门
 * @returns
 */
function chooseXsybm(){
    var url=$('#turnover_formSearch #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择使用部门',addXsybmConfig);
}
var addXsybmConfig = {
    width		: "800px",
    modalName	:"addXsybmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#turnover_formSearch #xsybm').val(sel_row[0].jgid);
                    $('#turnover_formSearch #xsybmmc').val(sel_row[0].jgmc);
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
        elem: '#turnover_formSearch #lysj'
        ,type: 'datetime'
        , theme: '#2381E9'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    jQuery('#turnover_formSearch .chosen-select').chosen({width: '100%'});
});