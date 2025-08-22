function selectnbbhPJ() {
//回车执行查询
    let nbbh = $("#detectionPJ_Form #NbbhTab #nbbh").val();
    if (nbbh){
        var url = "/detectionPJ/detectionPJ/pagedataGetInfoByNbbh?jclx="+$("#detectionPJ_Form #jclx").val();
        $.ajax({
            type: 'post',
            url: url,
            data: {"nbbh": nbbh, "access_token": $("#ac_tk").val()},
            dataType: 'json',
            success: function (data) {
                if (data.fzjcxxDtoInfo != null) {
                    if (data.fzjcxxDtoInfo.nbbh) {
                        $("#detectionPJ_Form #nbbh").val(data.fzjcxxDtoInfo.nbbh);
                    }
                    if (data.fzjcxxDtoInfo.nbbh) {
                        $("#detectionPJ_Form #nbbhspan").text(data.fzjcxxDtoInfo.nbbh);
                    }
                    if (data.fzjcxxDtoInfo.xm) {
                        $("#detectionPJ_Form #xm").text(data.fzjcxxDtoInfo.xm);
                    }
                    if (data.fzjcxxDtoInfo.jcxmmc) {
                        $("#detectionPJ_Form #jcxmmc").text(data.fzjcxxDtoInfo.jcxmmc);
                    }
                    if (data.fzjcxxDtoInfo.sysj){
                        $("#detectionPJ_Form #ts").text("该记录已被实验！");
                        var div = document.getElementById('jg');
                        div.setAttribute("class", "red");
                    }else{
                        var url = "/detectionPJ/detectionPJ/pagedataUpdateInfoByNbbh";
                        $.ajax({
                            type: 'post',
                            url: url,
                            data: {"nbbh": nbbh, "access_token": $("#ac_tk").val()},
                            dataType: 'json',
                            success: function (data) {
                                if (data.success != "" && null != data){
                                    $("#detectionPJ_Form #ts").text(data.success);
                                    var div = document.getElementById('jg');
                                    div.setAttribute("class", "green");
                                }else{
                                    $("#detectionPJ_Form #ts").text(data.fail);
                                    var div = document.getElementById('jg');
                                    div.setAttribute("class", "red");
                                }
                            }
                        });
                    }
                } else {
                    $("#detectionPJ_Form #nbbh").val("");
                    $("#detectionPJ_Form #nbbhspan").text("");
                    $("#detectionPJ_Form #xm").text("");
                    $("#detectionPJ_Form #jcxmmc").text("");
                    $("#detectionPJ_Form #ts").text("没该条检测记录!");
                    var div = document.getElementById('jg');
                    div.setAttribute("class", "red");
                }
            }
        });
    }else{
        $("#detectionPJ_Form #ts").text("请输入内部编号!");
        var div = document.getElementById('jg');
        div.setAttribute("class", "red");
    }

}
$("#keydown").bind("keydown", function (e) {
    let evt = window.event || e;
    if (evt.keyCode == 13) {
        selectnbbhPJ()
    }
});
function changeTab(tab){
    if (tab=="NbbhTab"){
        $("#btn_success7").hide();
    }
    if (tab=="syTab"){
        $("#btn_success7").show();
    }
}
$(function(){
    $("#detectionPJ_Form #checkbox").bootstrapSwitch({
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
                $('#detectionPJ_Form #sysjdiv').attr("style","display:block;");
                $('#detectionPJ_Form #sfsy').val("1");
            } else {
                $('#detectionPJ_Form #sysjdiv').attr("style","display:none;");
                $('#detectionPJ_Form #sfsy').val("");
            }
        }
    });
    if ( "1"== $('#detectionPJ_Form #sfsy').val()){
        $('#detectionPJ_Form #sysjdiv').attr("style","display:block;");
        $("#detectionPJ_Form #checkbox").bootstrapSwitch("state", true, true);
    }
});


