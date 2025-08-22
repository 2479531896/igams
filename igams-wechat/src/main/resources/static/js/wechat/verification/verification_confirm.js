function getVerifyInfo() {
//回车执行查询
    let ybbh = $("#ajaxConfirmForm  #ybbh").val();
    if (ybbh){
        var url = "/verification/verification/getInfoByYbbh";
        $.ajax({
            type: 'post',
            url: url,
            data: {"ybbh": ybbh, "access_token": $("#ac_tk").val()},
            dataType: 'json',
            success: function (data) {
                if (data.sjxxDto != null) {
                    if(data.sjxxDto.hzxm){
                        $("#ajaxConfirmForm #hzxm").text(data.sjxxDto.hzxm);
                    }
                    if(data.sjxxDto.nl){
                        $("#ajaxConfirmForm #nl").text(data.sjxxDto.nl);
                    }
                    if(data.sjxxDto.nldw){
                        $("#ajaxConfirmForm #nldw").text(data.sjxxDto.nldw);
                    }
                    if(data.sjxxDto.xbmc){
                        $("#ajaxConfirmForm #xbmc").text(data.sjxxDto.xbmc);
                    }
                    if(data.sjxxDto.yymc){
                        $("#ajaxConfirmForm #sjdw").text(data.sjxxDto.yymc);
                    }

                    if(data.sjxxDto.sjid){
                        $("#ajaxConfirmForm #sjid").val(data.sjxxDto.sjid);
                    }
                    if(data.sjyzDto.yzid){
                        $("#ajaxConfirmForm #yzid").val(data.sjyzDto.yzid);
                    }
                    if(data.sjyzDto.sjph){
                        $("#ajaxConfirmForm #sj").val(data.sjyzDto.sjph);
                    }
                    if(data.sjyzDto.ywbh){
                        $("#ajaxConfirmForm #yw").val(data.sjyzDto.ywbh);
                    }
                    if(data.sjxxDto.nbbm){
                        $("#ajaxConfirmForm #nbbm").val(data.sjxxDto.nbbm.trim());
                    }
                    if(data.sjxxDto.yblxmc){
                        $("#ajaxConfirmForm #yblxmc").text(data.sjxxDto.yblxmc);
                    }
                    if(data.sjxxDto.bz){
                        $("#ajaxConfirmForm #bz").val(data.sjxxDto.bz);
                    }
                    if(data.sjxxDto.ksmc){
                        $("#ajaxConfirmForm #ks").text(data.sjxxDto.ksmc);
                    }
                    if(data.sjxxDto.db){
                        $("#ajaxConfirmForm #db").text(data.sjxxDto.db);
                    }
                    if(data.sjxxDto.jcxmmc){
                        $("#ajaxConfirmForm #jcxm").text(data.sjxxDto.jcxmmc);
                    }
                    if(data.sjyzDto.jcjg){
                        $("#ajaxConfirmForm #jcjg").text(data.sjyzDto.jcjg);
                    }
                    if(data.sjyzDto.ysjg){
                        $("#ajaxConfirmForm #ysjg").text(data.sjyzDto.ysjg);
                    }

                    if(data.sjxxDto.yblxmc){
                        $("#ajaxConfirmForm #yblx").text(data.sjxxDto.yblxmc);
                    }
                    if(data.sjxxDto.sfsc=='1'){
                        $("#ajaxConfirmForm #sfsc").text("是");
                    }else if(data.sjxxDto.sfsc=='0'){
                        $("#ajaxConfirmForm #sfsc").text("否");
                    }

                    if (data.sjxxDto.jcdw) {
                        if (null != data.sjxxDto.jcdw && '' != data.sjxxDto.jcdw) {
                            $("#ajaxConfirmForm #jcdw").val(data.sjxxDto.jcdw);
                            $('#ajaxConfirmForm #jcdw').trigger("chosen:updated");
                        }
                    }

                    if(data.sjxsbj=='0'){
                        $("#ajaxConfirmForm #sjph").val(data.sjyzDto.sjph);
                    }
                    if(data.sjxsbj=='1'){
                        $("#ajaxConfirmForm #sjph").attr( "placeholder" , data.sjyzDto.sjph);
                    }

                    if(data.ywxsbj=='0'){
                        $("#ajaxConfirmForm #ywbh").val(data.sjyzDto.ywbh);
                    }
                    if(data.ywxsbj=='1'){
                        $("#ajaxConfirmForm #ywbh").attr( "placeholder" , data.sjyzDto.ywbh);
                    }
                    if (data.sjyzDto.yzlb) {
                        if (null != data.sjyzDto.yzlb && '' != data.sjyzDto.yzlb) {
                            $("#ajaxConfirmForm #yzlb").val(data.sjyzDto.yzlb);
                            $('#ajaxConfirmForm #yzlb').trigger("chosen:updated");
                        }
                    }

                    if (data.sjyzDto.qf) {
                        if (null != data.sjyzDto.qf && '' != data.sjyzDto.qf) {
                            $("#ajaxConfirmForm #qf").val(data.sjyzDto.qf);
                            $('#ajaxConfirmForm #qf').trigger("chosen:updated");
                        }
                    }
                    if (data.sjyzDto.khtz) {
                        if (null != data.sjyzDto.khtz && '' != data.sjyzDto.khtz) {
                            $("#ajaxConfirmForm #khtz").val(data.sjyzDto.khtz);
                            $('#ajaxConfirmForm #khtz').trigger("chosen:updated");
                        }
                    }
                    if($("#ajaxConfirmForm #xsbj").val()=="1"){
                        $("#ajaxConfirmForm #sjphDiv").show();
                        $("#ajaxConfirmForm #ywbhDiv").show();
                        $("#sjph").attr("validate","{required:true,stringMaxLength:64}");
                        $("#ywbh").attr("validate","{required:true,stringMaxLength:64}");
                    }else{
                        $("#ajaxConfirmForm #sjphDiv").hide();
                        $("#ajaxConfirmForm #ywbhDiv").hide();
                    }
                    if($("#ajaxConfirmForm #yzlb").val()){
                        $("#ajaxConfirmForm #yzjgtable").show();
                        $("#ajaxConfirmForm #historytable").show();
                        showChildcsdmData();
                        //历史记录
                        showHistoryData();
                    }else{
                        $("#ajaxConfirmForm #yzjgtable").hide();
                        $("#ajaxConfirmForm #historytable").hide();
                    }
                    if($("#fixtop #lastStep").val()=="true"){
                        $("#ajaxConfirmForm #yzjgSpan").show();
                        $("#ajaxConfirmForm #lastStep_flg").val("true");
                    }else{
                        $("#ajaxConfirmForm #yzjgSpan").hide();
                        $("#ajaxConfirmForm #lastStep_flg").val("false");
                    }
                    if(data.yzmxlist!=null&&data.yzmxlist.length>0){
                        for(var i = 0; i < data.yzmxlist.length; i++){
                            if(data.yzmxlist[i].scbj=='0'){
                                var Html="<tr>";
                                if(data.yzmxlist[i].yzjgmc!=null&&data.yzmxlist[i].yzjgmc!=""){
                                    Html+="<td style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].yzjgmc+"</td>";
                                }else{
                                    Html+="<td style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+""+"</td>";
                                }
                                // if(data.yzmxlist[i].mbjy!=null&&data.yzmxlist[i].mbjy!=""){
                                //     Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].mbjy+"</td>";
                                // }else{
                                //     Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+""+"</td>";
                                // }
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].ct1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].bz1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].ct2+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].bz2+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].nct1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].nct2+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].pct1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].pct2+"</td>";
                                Html+="</tr>";
                                $("#ajaxConfirmForm #yzmxDiv").append(Html);
                            }else  if(data.yzmxlist[i].scbj=='1'){
                                var Html="<tr>";
                                Html+="<td style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].yzjgmc+"</td>";
                                // Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].mbjy+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].ct1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].bz1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].ct2+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].bz2+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].nct1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].nct2+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].pct1+"</td>";
                                Html+="<td  style='line-height:450%;white-space: nowrap;text-overflow: clip;overflow: hidden;'>"+data.yzmxlist[i].pct2+"</td>";
                                Html+="</tr>";
                                $("#ajaxConfirmForm #lsDiv").append(Html);
                            }
                        }
                    }
                    $("#ajaxConfirmForm #ts").text("已找到记录!");
                    var div = document.getElementById('jg');
                    div.setAttribute("class", "green");
                } else {
                    $("#ajaxConfirmForm #ts").text("未找到记录!");
                    var div = document.getElementById('jg');
                    div.setAttribute("class", "red");
                }
            }
        });
    }else{
        $("#ajaxConfirmForm #ts").text("请输入样本编号!");
        var div = document.getElementById('jg');
        div.setAttribute("class", "red");
    }

}

function init(){
    setTimeout("$('#ajaxConfirmForm #ybbh').focus()", 100);
}

$("#keydown").bind("keydown", function (e) {
    let evt = window.event || e;
    if (evt.keyCode == 13) {
        getInfo();
    }
});
$(function(){
    jQuery('#ajaxConfirmForm .chosen-select').chosen({width: '100%'});
})

function yzjgtable(){
    if($("#ajaxConfirmForm #yzlb").val()){
        $("#ajaxConfirmForm #yzjgtable").show();
        $("#ajaxConfirmForm #historytable").show();
        showChildcsdmData();
        showHistoryData();
    }else{
        $("#ajaxConfirmForm #yzjgtable").hide();
        $("#ajaxConfirmForm #historytable").hide();
    }
}
function showChildcsdmData(){
    let pType = "VERIFY_RESULT";
    let fcsid = $("#ajaxConfirmForm #yzlb").val();
    let yzid = $("#ajaxConfirmForm #yzid").val();

    if(fcsid==null || fcsid==""){
        let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>验证结果</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
        html += "<thead><tr><th width='50%'>名称</th><th width='50%'>结果</th></tr></thead><tbody>";
        html += "</tbody></table></div>";
        $("#ajaxConfirmForm #yzjgtable").empty();
        $("#ajaxConfirmForm #yzjgtable").append(html);
    }else{
        $.ajax({
            type : "post",
            url: "/ws/verification/pagedataAnsyGetJcsjListAndJl",
            data : {"jclb":pType,"fcsid":fcsid,"yzid":yzid,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success : function(data){
                //父节点数据
                let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>验证结果</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
                html += "<thead><tr><th width='50%'>名称</th><th width='50%'>结果</th></tr></thead><tbody>";
                let json = [];
                let lastStep_flg = $("#ajaxConfirmForm #lastStep_flg").val();
                $.each(data.jcsjDtos,function(i){
                    // html += "<option value='" + data.jcsjDtos[i].csid + "' selected='selected'>"+data.jcsjDtos[i].csdm + "--" + data.jcsjDtos[i].csmc + "</option>";
                    html += "<tr><td value='" + data.jcsjDtos[i].csid + "'>"+data.jcsjDtos[i].csdm+"--"+data.jcsjDtos[i].csmc+"</td><td>";
                    if (data.jcsjDtos[i].yzjgjl){
                        $.each(data.jcsjDtoList,function(j){
                            html += "<label class='radio-inline'><input type='radio' name='" + data.jcsjDtos[i].csid + "' id='optionsRadio"+i+"' value='" + data.jcsjDtos[i].csmc +":"+data.jcsjDtoList[j].csmc+"'" ;
                            if ( data.jcsjDtos[i].yzjgjl == data.jcsjDtoList[j].csid){
                                html += "checked='true'";
                            }
                            if ( lastStep_flg == "true"){
                                html += "validate='{required:true}'";
                            }
                            html += ">"+ data.jcsjDtoList[j].csmc+ "</label>";
                        });
                    }else{
                        $.each(data.jcsjDtoList,function(j){
                            html += "<label class='radio-inline'><input type='radio' name='" + data.jcsjDtos[i].csid + "' id='optionsRadio"+i+"' value='" + data.jcsjDtos[i].csmc +":"+data.jcsjDtoList[j].csmc+"'" ;
                            // if ( j == "0"){
                            // 	html += "checked";
                            // }
                            if ( lastStep_flg == "true"){
                                html += "validate='{required:true}'";
                            }
                            html += ">"+ data.jcsjDtoList[j].csmc+ "</label>";
                        });
                    }
                    let sz = {"csid":'',"csmc":''};
                    sz.csid = data.jcsjDtos[i].csid;
                    sz.csmc = data.jcsjDtos[i].csmc;
                    json.push(sz);
                });
                html += "</td></tr></tbody></table></div>";
                $("#ajaxConfirmForm #yzjg").val(JSON.stringify(json));
                $("#ajaxConfirmForm #yzjgtable").empty();
                $("#ajaxConfirmForm #yzjgtable").append(html);
            }
        });
    }
}

function showHistoryData(){

    let yzlb = $("#ajaxConfirmForm #yzlb").val();
    let sjid = $("#ajaxConfirmForm #sjid").val();

    if(yzlb==null || yzlb==""){
        let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
        html += "<thead><tr><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>检测单位</th><th width='25%'>状态</th></tr></thead><tbody>";
        html += "</tbody></table></div>";
        $("#ajaxConfirmForm #historytable").empty();
        $("#ajaxConfirmForm #historytable").append(html);
    }else{
        $.ajax({
            type : "post",
            url: "/ws/sjyzHistory/pagedataSjyzHistory",
            data : {"sjid":sjid,"yzlb":yzlb,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success : function(data){

                let html = "<div class='form-group' ><label class='col-xs-4 col-sm-2 col-md-2  col-lg-2 control-label'>历史记录</label><table class='table table-hover table-bordered' style='width: 80%;table-layout: fixed;margin: auto;'>";
                html += "<thead><tr><th width='25%'>录入人员</th><th width='25%'>录入时间</th><th width='25%'>检测单位</th><th width='25%'>状态</th></tr></thead><tbody>";
                let json = [];
                $.each(data.sjyzlist,function(i){
                    if ('00'==(data.sjyzlist[i].zt)){
                        zthtml = '<td>未提交</td>' ;
                    }else if ('80'==(data.sjyzlist[i].zt)){
                        zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">审核通过</a></td>' ;
                    }else if ('15'==(data.sjyzlist[i].zt)){
                        zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">审核未通过</a></td>' ;
                    }
                    else if ('20'==(data.sjyzlist[i].zt)){
                        zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">'+data.sjyzlist[i].shxx_dqgwmc+'审核中</a></td>' ;
                    }
                    else {
                        zthtml = '<td><a href="javascript:void(0);" onclick="showAuditInfo(\''+data.sjyzlist[i].yzid+'\',event,\'AUDIT_VERIFICATION\')">'+data.sjyzlist[i].shxx_dqgwmc+'审核中</a></td>' ;
                    }
                    html += '<tr>' +
                        '<td value="' + data.sjyzlist[i].hzxm + '">' +data.sjyzlist[i].hzxm +'</td>'+
                        '<td value="'+data.sjyzlist[i].lrsj+'"><a href="javascript:void(0);" onclick=\'queryByYzid("'+ data.sjyzlist[i].yzid +'")\' >' +data.sjyzlist[i].lrsj+ '</a></td>'+
                        '<td value="'+data.sjyzlist[i].jcdwmc+'">' +data.sjyzlist[i].jcdwmc+'</td>'+
                        // '<td value="'+data.sjyzlist[i].zt+'">' +data.sjyzlist[i].zt+'</td>'
                        zthtml +
                        '</tr>'
                });
                html += "</tbody></table></div>";
                // $("#ajaxConfirmForm #yzjg").val(JSON.stringify(json));
                $("#ajaxConfirmForm #historytable").empty();
                $("#ajaxConfirmForm #historytable").append(html);
            }
        });
    }
}

function queryByYzid(yzid){
    var url= "/verification/verification/viewVerifi?yzid="+yzid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'验证历史信息',viewYzConfig);
}

/**
 * 查看送检验证页面
 */
var viewYzConfig={
    width		: "1000px",
    modalName	:"viewYzModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

/**
 * 修改检测单位
 * @returns
 */
function modifJcdw(){
    $.confirm('确定要修改当前检测单位?',function(result){
        if(result){
            var yzid=$("#ajaxConfirmForm #yzid").val();
            var jcdw=$("#ajaxConfirmForm #jcdw").val();
            var url =$("#ajaxConfirmForm #url").val();
            $.ajax({
                url:url,
                type : "POST",
                data:{"yzid":yzid,"jcdw":jcdw,"access_token":$("#ac_tk").val()},
                dataType:"json",
                success:function(data){
                    if(data.status){
                        $.success("修改成功！");
                    }else{
                        $.error("修改失败！");
                    }
                }
            })
        }
    })
}

function sjphlr(){
    $("#ajaxConfirmForm #sjph").val($("#ajaxConfirmForm #sj").val());
}

function ywbhlr(){
    $("#ajaxConfirmForm #ywbh").val($("#ajaxConfirmForm #yw").val());
}
$(document).ready(function(){
    init();
});
