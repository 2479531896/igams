var detectionApplicationAudit_turnOff = true;
/**显示隐藏**/
$("#wzzk_formSearch #sl_searchMore").on("click", function (ev) {
    var ev = ev || event;
    if (detectionApplicationAudit_turnOff) {
        $("#wzzk_formSearch #searchMore").slideDown("low");
        detectionApplicationAudit_turnOff = false;
        this.innerHTML = "基本筛选";
    } else {
        $("#wzzk_formSearch #searchMore").slideUp("low");
        detectionApplicationAudit_turnOff = true;
        this.innerHTML = "高级筛选";
    }
    ev.cancelBubble = true;
    //showMore();
});

function getwzzkinfo(){
    var str="?yxxs="+$("#xm").val()+"&jcdw="+$("#jcdw").val()+"&zkmc="+$("#zkmc").val()+"&cxy="+$("#cxy").val()+"&wkrqstart="+$("#wkrqstart").val()+"&wkrqend="+$("#wkrqend").val()+"&access_token="+$("#ac_tk").val()
    $.ajax({
        type:"post",
        url:"/wzzk/statistics/pagedataGetInfoStatisticsByParam"+ str,
        cache: false,
        success:function(data){
            console.log(data)
            $("#wzzk_formSearch #tableList").empty();
                        var t_html="";
                        var html="<table><tbody>";
                        if(data.list.length>0){
                            if(value!='1'){
                                t_html="<thead>"+
                                            "<tr>"+
                                              "<th>标本编码</th>"+
                                              "<th>开始时间</th>"+
                                              "<th>结束时间</th></tr>"+
                                          "</thead>";
                                $("#wbybxx_formSearch #tb").append(t_html);
                            }
                            for(var i=0;i<data.list.length;i++){
                                html+="<tr>"+
                                    "<td >"+data.list[i].bbbm+"</td>";
                                    if(value!='1'){
                                       html+= "<td style='color:green;'>"+(data.list[i].kssj!=null?data.list[i].kssj:'')+"</td>"+
                                        "<td style='color:orange;'>"+(data.list[i].jssj!=null?data.list[i].jssj:'')+"</td>";
                                    }else{
                                       html+= "<td style='color:green;'>"+(data.list[i].lcmc!=null?data.list[i].lcmc:'')+(data.list[i].lcxh!=null?data.list[i].lcxh:'')+"</td>"+
                                       "<td style='color:orange;'>"+(data.list[i].zlcmc!=null?data.list[i].zlcmc:'')+(data.list[i].zlcxh!=null?data.list[i].zlcxh:'')+"</td>";
                                    }

                                html+="</tr>";
                            }
                            html+="</tbody></table>";
                        }
                        $("#wzzk_formSearch #tableList").append(html);

        },
        error:function(event, XMLHttpRequest, ajaxOptions, thrownError){

        }
    });
}

var detectionApplication_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#wzzk_formSearch #btn_query");//模糊查询
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                getwzzkinfo();
            });
        }
    }
    return oInit;
}
$(function(){
    var oTable= new detectionApplication_audit_TableInit();
    oTable.Init();

    var oButtonInit = new detectionApplication_audit_oButtton();
    console.log(332)
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#wzzk_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#detectionApplication_audit_audited .chosen-select').chosen({width: '100%'});
    $("#detectionApplication_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});

$("#wzzk_formSearch #jcdw").on('change',function(){
	var jcdw=$("#wzzk_formSearch #jcdw option:selected").val();
	$.ajax({
            type:"post",
            url:"/wzzk/statistics/pagedataGetCxyxxBy?jcdw="+ jcdw+"&access_token="+$("#ac_tk").val(),
            cache: false,
            success:function(data){
                       var cxyList=data.newcxyList
                    if(cxyList != null && cxyList.length != 0){
                        var csbHtml = "";
                        csHtml += "<option value=''>--请选择--</option>";
                        $.each(cxyList,function(i){
                            csHtml += "<option value='" + cxyList[i].csid + "'>" + cxyList[i].csmc + "</option>";
                        });
                        $("#cxyxlhdiv #cxy").empty();
                        $("#cxyxlhdiv #cxy").append(csHtml);
                        $("#cxyxlhdiv #cxy").trigger("chosen:updated");
                    }else{
                        var csHtml = "";
                        csHtml += "<option value=''>--请选择--</option>";
                        $("#cxyxlhdiv #cxy").empty();
                        $("#cxyxlhdiv #cxy").append(csHtml);
                        $("#cxyxlhdiv #cxy").trigger("chosen:updated");
                    }
            },
            error:function(event, XMLHttpRequest, ajaxOptions, thrownError){

            }
        });
})

