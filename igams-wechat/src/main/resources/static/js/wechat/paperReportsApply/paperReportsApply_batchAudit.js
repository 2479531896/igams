$(function(){
    jQuery('#batchAuditPaperReportsApplyForm .chosen-select').chosen({width: '100%'});
    var kdlxdm = $("#batchAuditPaperReportsApplyForm #kdlx").find("option:selected").attr("csdm");
    if("JD"==kdlxdm){
        $("#batchAuditPaperReportsApplyForm #jdkdDiv").show();
    }else{
        $("#batchAuditPaperReportsApplyForm #jdkdDiv").hide();
    }
});

function xz(){
    var ywids = $("#batchAuditPaperReportsApplyForm #ids").val();
    ywids = ywids.replace("[","").replace("]","").replace(/\s*/g,"");
    var url="/inspection/inspection/commonReportdownloadSaveZip?ids="+ywids+"&bglxbj=pdf"+"&access_token=" + $("#ac_tk").val();
    $.post(url,map,function(data){
        if(data){
            if(data.status == 'success'){
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);
                setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
                //绑定导出取消按钮事件
                $("#exportCancel").click(function(){
                    //先移除导出提示，然后请求后台
                    if($("#cardiv").length>0) $("#cardiv").remove();
                    $.ajax({
                        type : "POST",
                        url : "/common/export/commCancelExport",
                        data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
                        dataType : "json",
                        success:function(res){
                            if(res != null && res.result==false){
                                if(res.msg && res.msg!="")
                                    $.error(res.msg);
                            }

                        }
                    });
                });
            }else if(data.status == 'fail'){
                $.error(data.error,function() {
                });
            }else{
                preventResubmitForm(".modal-footer > button", false);
                if(data.error){
                    $.alert(data.error,function() {
                    });
                }
            }
        }
    },'json');
}

//快递类型改变事件
$("#batchAuditPaperReportsApplyForm select[name='kdlx']").unbind("change").change(function(e){
    var kdlxdm = $("#batchAuditPaperReportsApplyForm #kdlx").find("option:selected").attr("csdm");
    if("JD"==kdlxdm){
        $("#batchAuditPaperReportsApplyForm #jdkdDiv").show();
    }else{
        $("#batchAuditPaperReportsApplyForm #jdkdDiv").hide();
    }
});