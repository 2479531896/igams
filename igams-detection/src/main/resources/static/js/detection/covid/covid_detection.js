function selectnbbh() {
//回车执行查询
    let nbbh = $("#detection_Form #NbbhTab #nbbh").val();
    if (nbbh){
        var url = "/detection/detection/pagedataInfoByNbbh";
        $.ajax({
            type: 'post',
            url: url,
            data: {"nbbh": nbbh, "access_token": $("#ac_tk").val()},
            dataType: 'json',
            success: function (data) {
                if (data.fzjcxxDtoInfo != null) {
                    if (data.fzjcxxDtoInfo.nbbh) {
                        $("#detection_Form #nbbh").val(data.fzjcxxDtoInfo.nbbh);
                    }
                    if (data.fzjcxxDtoInfo.nbbh) {
                        $("#detection_Form #nbbhspan").text(data.fzjcxxDtoInfo.nbbh);
                    }
                    if (data.fzjcxxDtoInfo.xm) {
                        $("#detection_Form #xm").text(data.fzjcxxDtoInfo.xm);
                    }
                    if (data.fzjcxxDtoInfo.jcxmmc) {
                        $("#detection_Form #jcxmmc").text(data.fzjcxxDtoInfo.jcxmmc);
                    }
                    if (data.fzjcxxDtoInfo.sysj){
                        $("#detection_Form #ts").text("该记录已被实验！");
                        var div = document.getElementById('jg');
                        div.setAttribute("class", "red");
                    }else{
                        var url = "/detection/detection/updateInfoByNbbh";
                        $.ajax({
                            type: 'post',
                            url: url,
                            data: {"nbbh": nbbh, "access_token": $("#ac_tk").val()},
                            dataType: 'json',
                            success: function (data) {
                                if (data.success != "" && null != data){
                                    $("#detection_Form #ts").text(data.success);
                                    var div = document.getElementById('jg');
                                    div.setAttribute("class", "green");
                                }else{
                                    $("#detection_Form #ts").text(data.fail);
                                    var div = document.getElementById('jg');
                                    div.setAttribute("class", "red");
                                }
                            }
                        });
                    }
                } else {
                    $("#detection_Form #nbbh").val("");
                    $("#detection_Form #nbbhspan").text("");
                    $("#detection_Form #xm").text("");
                    $("#detection_Form #jcxmmc").text("");
                    $("#detection_Form #ts").text("没该条检测记录!");
                    var div = document.getElementById('jg');
                    div.setAttribute("class", "red");
                }
            }
        });
    }else{
        $("#detection_Form #ts").text("请输入内部编号!");
        var div = document.getElementById('jg');
        div.setAttribute("class", "red");
    }

}
$("#keydown").bind("keydown", function (e) {
    let evt = window.event || e;
    if (evt.keyCode == 13) {
        selectnbbh()
    }
});

$(function(){
    $("#detection_Form #checkbox").bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
        onText : "是",      // 设置ON文本
        offText : "否",    // 设置OFF文本
        onColor : "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
        offColor : "info",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
        size : "normal",    // 设置控件大小,从小到大  (mini/small/normal/large)
        // 当开关状态改变时触发
        onSwitchChange : function(event, state) {
            if (state == true) {
                $('#detection_Form #sysjdiv').attr("style","display:block;");
                $('#detection_Form #sfsy').val("1");
            } else {
                $('#detection_Form #sysjdiv').attr("style","display:none;");
                $('#detection_Form #sfsy').val("");
            }
        }
    });
});


