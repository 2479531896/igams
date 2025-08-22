function chooseTzr() {
    var tzlx=$("#chooseTzryForm #tzlx").val();
    if(!tzlx){
        $.error("请选择通知类型！");
        return false;
    }
    var url = $("#chooseTzryForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    if(tzlx=="ROLE_TYPE"){
        url=$("#chooseTzryForm #urlPrefix").val()+"/systemrole/role/pagedataSelectListRole?access_token=" + $("#ac_tk").val();
        $.showDialog(url, '选择通知角色', chooseTzjsConfig);
    }else{
        $.showDialog(url, '选择通知人员', chooseXtyhConfig);
    }
}

// 通知角色选择框
var chooseTzjsConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseTzjsModal",
    formName: "ajaxForm",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#listRoleForm").valid()) {
                    return false;
                }
                var json = [];
                var data = $('#listRoleForm #xzjss').val();// 获取选择行数据
                for (; data.indexOf("jsid") != -1;) {
                    data = data.replace("jsid", "value")
                }
                for (; data.indexOf("jsmc") != -1;) {
                    data = data.replace("jsmc", "text")
                }
                $("#chooseTzryForm  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                json = data;
                var jsonStr = eval('(' + json + ')');
                for (var i = 0; i < jsonStr.length; i++) {
                    jsonStr[i].value="ROLE_TYPE-"+jsonStr[i].value;
                    $("#chooseTzryForm  #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

var chooseXtyhConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseXtyhModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var json=[];
                var data = $('#taskListFzrForm #xzrys').val();//获取选择行数据
                for (; data.indexOf("yhid") != -1;) {
                    data = data.replace("yhid", "value")
                }
                for (; data.indexOf("zsxm") != -1;) {
                    data = data.replace("zsxm", "text")
                }
                $("#chooseTzryForm  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                json=data;
                var jsonStr=eval('('+json+')');
                for (var i = 0; i < jsonStr.length; i++) {
                    jsonStr[i].value="USER_TYPE-"+jsonStr[i].value;
                    $("#chooseTzryForm  #tzrys").tagsinput('add',jsonStr[i]);
                }
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(function(){
    // 所有下拉框添加choose样式
    jQuery('#chooseTzryForm .chosen-select').chosen({width: '100%'});
})
