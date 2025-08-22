// 通知类型改变事件
$("#repeatObjectForm #tzlx").change(function () {
    chooseQrtzlx();
    var tzrys = $("#repeatObjectForm #tzrys").val();
    if (tzrys != null && tzrys != '') {
        $("#repeatObjectForm  #tzrys").tagsinput('removeAll');
    }
})

// 确认类型改变事件
$("#repeatObjectForm #qrlx").change(function () {
    chooseQrtzlx()
    var qrry = $("#repeatObjectForm #qrr").val();
    if (qrry != null && qrry != '') {
        $("#repeatObjectForm  #qrr").val("");
        $("#repeatObjectForm  #qrrmc").val("");
    }
})

//确认通知类型改变事件
function chooseQrtzlx(type){
    var qrlx = $("#repeatObjectForm #qrlx").val();
    var tzlx = $("#repeatObjectForm #tzlx").val();
    if (type!='tzlx'){
        if (qrlx == 'USER_TYPE'){
            $("#repeatObjectForm #qrryjs").removeClass("hidden");
            $("#repeatObjectForm #qrryText").removeClass("hidden");
            $("#repeatObjectForm #qrjsText").addClass("hidden");
            if (type){
                var url = $("#repeatObjectForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
                $.showDialog(url, '选择确认人员', chooseQrryConfig);
            }
        }else if (qrlx == 'ROLE_TYPE'){
            $("#repeatObjectForm #qrryjs").removeClass("hidden");
            $("#repeatObjectForm #qrjsText").removeClass("hidden");
            $("#repeatObjectForm #qrryText").addClass("hidden");
            if (type){
                url = $("#repeatObjectForm #urlPrefix").val()+"/systemrole/role/pagedataSelectListRole?access_token=" + $("#ac_tk").val();
                $.showDialog(url, '选择确认角色', chooseQrjsConfig);
            }
        }else {
            $("#repeatObjectForm #qrryjs").addClass("hidden");
        }
    }
    if (type!='qrlx'){
        if (tzlx == 'USER_TYPE'){
            $("#repeatObjectForm #tzryjs").removeClass("hidden");
            $("#repeatObjectForm #tzryText").removeClass("hidden");
            $("#repeatObjectForm #tzjsText").addClass("hidden");
            if (type){
                var url = $("#repeatObjectForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
                $.showDialog(url, '选择通知人员', chooseTzryConfig);
            }
        }else if (tzlx == 'ROLE_TYPE'){
            $("#repeatObjectForm #tzryjs").removeClass("hidden");
            $("#repeatObjectForm #tzjsText").removeClass("hidden");
            $("#repeatObjectForm #tzryText").addClass("hidden");
            if (type){
                url = $("#repeatObjectForm #urlPrefix").val()+"/systemrole/role/pagedataSelectListRole?access_token=" + $("#ac_tk").val();
                $.showDialog(url, '选择通知角色', chooseTzjsConfig);
            }
        }else {
            $("#repeatObjectForm #tzryjs").addClass("hidden");
        }
    }
}

// 通知角色选择框
var chooseTzjsConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseTzjsModal",
    formName: "repeatObjectForm",
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
                $("#repeatObjectForm  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                json = data;
                var jsonStr = eval('(' + json + ')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#repeatObjectForm  #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 通知人员选择框
var chooseTzryConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseTzryModal",
    formName: "repeatObjectForm",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#taskListFzrForm").valid()) {
                    return false;
                }
                var json = [];
                var data = $('#taskListFzrForm #xzrys').val();// 获取选择行数据
                for (; data.indexOf("yhid") != -1;) {
                    data = data.replace("yhid", "value")
                }
                for (; data.indexOf("zsxm") != -1;) {
                    data = data.replace("zsxm", "text")
                }
                $("#repeatObjectForm  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                json = data;
                var jsonStr = eval('(' + json + ')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#repeatObjectForm #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 通知角色选择框
var chooseQrjsConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseQrjsModal",
    formName: "repeatObjectForm",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#listRoleForm").valid()) {
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                var sel_row = $('#listRoleForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
                if (sel_row.length == 1) {
                    $('#repeatObjectForm #qrr').val(sel_row[0].jsid);
                    $('#repeatObjectForm #qrrmc').val(sel_row[0].jsmc);
                    $.closeModal(opts.modalName);
                } else {
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 确认人员选择框
var chooseQrryConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseFzrModal",
    formName: "repeatObjectForm",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#taskListFzrForm").valid()) {
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
                if (sel_row.length == 1) {
                    $('#repeatObjectForm #qrr').val(sel_row[0].yhid);
                    $('#repeatObjectForm #qrrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                } else {
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

function initTzJsrys(){
    var ycid = $("#repeatObjectForm #ycid").val();
    var tzlx = $("#repeatObjectForm #tzlx").val();
    if (tzlx!=null && tzlx!=''){
        $.ajax({
            type:'post',
            url:$("#repeatObjectForm #urlPrefix").val()+"/exception/exception/pagedataNotices",
            cache: false,
            data: {"ycid":ycid,"tzlx":tzlx,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(result){
                var tzryjss = result.sjyctzDtoList;
                var jsonStr = [];
                if (tzryjss.length>0){
                    for (var i = 0; i < tzryjss.length; i++) {
                        var text = tzryjss[i].mc;
                        var value = tzryjss[i].id;
                        jsonStr.push({'text':text,'value':value})
                    }
                }
                $("#repeatObjectForm  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#repeatObjectForm  #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        });
    }
}

$(function () {
    // 所有下拉框添加choose样式
    jQuery('#repeatObjectForm .chosen-select').chosen({width: '100%'});
    chooseQrtzlx();
    initTzJsrys();
})
