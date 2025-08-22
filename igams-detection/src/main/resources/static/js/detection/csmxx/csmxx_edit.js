function selectHospitaljc() {
    url = "/wechat/hospital/pagedataCheckUnitView?access_token=" + $("#editCsmForm #access_token").val();
    $.showDialog(url, '医院名称', SelectCsmHospitalConfigjc);
};
//送检单位模板框
var SelectCsmHospitalConfigjc = {
    width: "1000px",
    modalName: "SelectCsmHospitalConfigjc",
    offAtOnce: false,  //当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function() {
                var sel_row = $('#hospital_formSearch #hospital_list').bootstrapTable('getSelections');
                if (sel_row.length == 1) {
                    var dwid = sel_row[0].dwid;
                    var dwmc = sel_row[0].dwmc;
                    $("#editCsmForm #sjdw").val(dwid);
                    $("#editCsmForm #sjdwmc").val(dwmc);
                } else {
                    $.error("请选中一行!");
                    return false;
                }
            },
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

function chooseFzr(){
    var url="/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#editCsmForm #access_token").val();
    $.showDialog(url,'选择用户',addFzrConfig);
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
                    $('#editCsmForm #yhid').val(sel_row[0].yhid);
                    $('#editCsmForm #xm').val(sel_row[0].zsxm);
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


$(document).ready(function () {
    //有效时间
    laydate.render({
        elem: '#editCsmForm #yxrq'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    //所有下拉框添加choose样式
    jQuery('#editCsmForm .chosen-select').chosen({width: '100%'});

});
