//检测项目改变事件
$("#reportgeneratePJForm .jcxm").change(function() {
    initjczxm();
});
// 初始化检测子项目
function initjczxm(){
    if($("#reportgeneratePJForm .jczxm").length>0){
        if($("#reportgeneratePJForm .jcxm")!=null){
            for(var i=0;i<$("#reportgeneratePJForm .jczxm").length;i++){
                var zid=$("#reportgeneratePJForm .jczxm")[i].id;
                var fcsid=$("#reportgeneratePJForm #"+zid).attr("fcsid");
                var fid=0;
                var flag=0;
                for (let j = 0; j < $("#reportgeneratePJForm .jcxm").length; j++) {
                    if($("#reportgeneratePJForm .jcxm")[j].checked){
                        fid=$("#reportgeneratePJForm .jcxm")[j].id;
                    }
                    if(fcsid==fid){
                        flag=1;
                        $("#reportgeneratePJForm #zxm-"+zid).attr("style","display:block;padding-left:0px;");
                    }
                }
                if(flag==0){
                    var checkbox = document.getElementById(zid);
                    checkbox.checked = false;
                    $("#reportgeneratePJForm #zxm-" + zid).attr("style", "display:none");
                }
            }
        }
    }
}
$(document).ready(function () {
    initjczxm();
    jcfxm();
    jczxm();
  
});
