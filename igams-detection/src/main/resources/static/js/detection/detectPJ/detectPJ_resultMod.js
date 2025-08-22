

function selectRe(value,index){
    if (value!="阳性"){
        $("#resultModDetectionPJForm #ctz_"+index).hide();
        $("#resultModDetectionPJForm #ctz_"+index).val("");
    }
    else {
        $("#resultModDetectionPJForm #ctz_"+index).show();
    }
}
//撤回提交
function recallDetection(fzxmid,auditType,event){
    var msg = '您确定要撤回该记录吗？';
    var detection_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fzxmid,function(){
                //
                var ids = $("#resultModDetectionPJForm #ids").val();
                $("#resultModDetectionPJForm").load("/detectionPJ/detectionPJ/resultmodDetectionPJ?ids="+ids);
            },detection_params);
        }
    });
}
//保存并提交
function saveAndSubmitDetection(ywid,fzjcid,auditType,index){
    $.confirm('您确定要提交该记录吗？',function(result){
        if(result){
            var tab=$("#resultModDetectionPJForm ."+ywid);
            var json=[];
            for(var i=0;i<tab.length;i++){
                var trIndex = tab[i].id.split("_")[1];
                var sz = {"fzxmid":'',"jcjg":'',"fzjcid":'',"jgid":'',"ctz":'',"jcjgmc":''};
                sz.fzxmid = ywid;
                sz.jgid = $("#resultModDetectionPJForm #pjjg_"+trIndex).val();
                sz.fzjcid = fzjcid;
                sz.ctz = $("#resultModDetectionPJForm #ctz_"+trIndex).val();
                sz.jcjg= $("#resultModDetectionPJForm input[name=jg_"+trIndex+"]:checked").val();
                sz.jcjgmc= $("#resultModDetectionPJForm input[name=jg_"+trIndex+"]:checked").attr("csmc");
                json.push(sz);
            }
            $("#resultModDetectionPJForm #jcjg_json").val(JSON.stringify(json));
            var jcjg_json = JSON.stringify(json);
            jQuery.ajaxSetup({async:false});
            var url="/detectionPJ/detectionPJ/resultmodSaveDetectionPJ";
            jQuery.post(url,{"fzxmid":ywid,"fzjcid":fzjcid,"jcjg_json":jcjg_json,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        showAuditFlowDialog(auditType,ywid,function(){
                            var ids = $("#resultModDetectionPJForm #ids").val();
                            $("#resultModDetectionPJForm").load("/detectionPJ/detectionPJ/resultmodDetectionPJ?ids="+ids);
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);

            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}
/**
 * 表格指定列，根据相同值跨行
 * @param   tableId
 *  表格ID
 * @param   columnNum
 *  要合并的列数（第几列）
 * @explain 第一行不参与跨行合并
 */
function mergeTableRows(tableId, columnNum) {
    var tableObj = document.getElementById(tableId);
    // 列数所对应的下标数
    var colIdx = columnNum - 1;
    var lastTdName = null;
    var rowLen = tableObj.rows.length;
    for (var i = rowLen - 1; i >= 1; i--) {
        var currName = tableObj.rows[i].cells[colIdx].className;
        if (lastTdName == null) {
            sameCount = 1;
        } else if (lastTdName != currName) {
            if (sameCount > 1) {
                tableObj.rows[i + 1].cells[colIdx].rowSpan = sameCount;
            }

            sameCount = 1;
        } else if (lastTdName == currName) {
            tableObj.rows[i + 1].deleteCell(colIdx);
            sameCount++;
        }

        lastTdName = currName;
    }

    if (sameCount > 1) {
        tableObj.rows[1].cells[colIdx].rowSpan = sameCount;
    }
};
$(function(){
    let size = $("#resultModDetectionPJForm #size").val();
    if (size && size*1>0){
        for (let i = 0; i < size; i++) {
            var csmc = $("#resultModDetectionPJForm input[name=jg_"+i+"]:checked").attr("csmc");
            if (csmc!='阳性'){
                $("#resultModDetectionPJForm #ctz_"+i).hide();
            }
        }
    }
    mergeTableRows('detectionTable',9);
    mergeTableRows('detectionTable',8);
});