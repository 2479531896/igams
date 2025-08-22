/**
 * 选择负责人列表
 * @returns
 */
var lx="";
function chooseYwy(flag){
    var url=$('#stewardsetForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',addYwyConfig);
    lx = flag;
}
var addYwyConfig = {
    width		: "800px",
    modalName	:"addYwyModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    if (lx=='yfzr'){
                        $('#stewardsetForm #yfzr').val(sel_row[0].yhid);
                        $('#stewardsetForm #yfzrmc').val(sel_row[0].zsxm);
                    }else if (lx=='fzr'){
                        $('#stewardsetForm #fzrmc').val(sel_row[0].zsxm);
                        $('#stewardsetForm #fzr').val(sel_row[0].yhid);
                    }
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#stewardsetForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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