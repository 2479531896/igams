
function btnBind(){

}

function initPage(){

}

$("#applicationForm #jcxmid").on('change',function(){
    var sqxmdm=$("#applicationForm #jcxmid").find("option:selected").attr("csdm");
    $("#applicationForm #jcxmdm").val(sqxmdm);
    $("#applicationForm #sqxm").val();
    $("#applicationForm #sqxm").text();
    $("#applicationForm #sqxm01").prop("selected","selected");
    $("#applicationForm #sqxm").trigger("chosen:updated");
    var zlbHtml = "";
    zlbHtml += "<option value=''>--请选择--</option>";
    $("#applicationForm #sqzxm").empty();
    $("#applicationForm #sqzxm").append(zlbHtml);
    $("#applicationForm #sqzxm").trigger("chosen:updated");
})

$("#applicationForm #sqxm").on('change',function(){
    var sqxm=$("#applicationForm #sqxm").val();
    var sqxmdm=$("#applicationForm #sqxm").find("option:selected").attr("csdm");
    var csdm=$("#applicationForm #jcxmdm").val();
    $.ajax({
        url : "/application/pagedataApplicationTypes",
        type : "post",
        data : {fcsid:sqxm,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data != null && data.length != 0){
                var csHtml = "";
                var yyHtml = "";
                var jcsjlist=data.jcsjlist;
                csHtml += "<option value=''>--请选择--</option>";
                for(var i=0;i<jcsjlist.length;i++){
                    if(sqxmdm=='FREE'){
                        if(jcsjlist[i].cskz2!=null&&jcsjlist[i].cskz2!=''){
                            if(jcsjlist[i].cskz2.indexOf(csdm)>=0){
                                csHtml += "<option value='" + jcsjlist[i].csid + "'>" + jcsjlist[i].csmc + "</option>";
                            }
                        }
                    }else{
                        csHtml += "<option value='" + jcsjlist[i].csid + "'>" + jcsjlist[i].csmc + "</option>";
                    }
                }
                $("#applicationForm #sqzxm").empty();
                $("#applicationForm #sqzxm").append(csHtml);
                $("#applicationForm #sqzxm").trigger("chosen:updated");
                var reasonlist=data.reasonlist;
                yyHtml += "<option value=''>--请选择--</option>";
                for(var i=0;i<reasonlist.length;i++){
                    if(reasonlist[i].cskz2!=null&&reasonlist[i].cskz2!=''){
                        if(reasonlist[i].cskz2.indexOf(sqxmdm)>=0){
                            yyHtml += "<option value='" + reasonlist[i].csid + "'>" + reasonlist[i].csmc + "</option>";
                        }
                    }
                }
                $("#applicationForm #sqyy").empty();
                $("#applicationForm #sqyy").append(yyHtml);
                $("#applicationForm #sqyy").trigger("chosen:updated");
            }else{
                var csHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $("#applicationForm #sqzxm").empty();
                $("#applicationForm #sqzxm").append(csHtml);
                $("#applicationForm #sqzxm").trigger("chosen:updated");
                $("#applicationForm #sqyy").empty();
                $("#applicationForm #sqyy").append(csHtml);
                $("#applicationForm #sqyy").trigger("chosen:updated");
            }
        }
    });
})

$(document).ready(function(){
    //绑定事件
    btnBind();
    //初始化页面数据
    initPage();
    //所有下拉框添加choose样式
    jQuery('#applicationForm .chosen-select').chosen({width: '100%'});
});