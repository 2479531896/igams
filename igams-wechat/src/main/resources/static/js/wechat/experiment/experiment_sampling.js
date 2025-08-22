var flag=true;

/**
 * 绑定按钮事件
 */
function btnBind(){
    $("#samplingExperimentForm #nbbm").on('keyup',function(e){
        if(e.keyCode==123 || e.keyCode==74|| e.keyCode==13){
            //若键入F12或J或回车，则自动查询
            var nbbm = $("#samplingExperimentForm #nbbm").val();
            if(nbbm){
                getInspection();
                var zdbc = $("input[name='zdbc']:checked").val();
                var sjid = $("#samplingExperimentForm #sjid").val();
                var ybbh = $("#samplingExperimentForm #ybbh").val();
                var print_num = $("#samplingExperimentForm #print_num").val();
                var print_flg = $("input[name='print_flg']:checked").val();
                var suffixs = '';
                var ids = '';
                $("#samplingExperimentForm #jclx input[type='checkbox']").each(function(index){
                    if (this.checked){
                        ids+=","+this.value;
                        suffixs+=","+$("#samplingExperimentForm #"+this.value).attr("nbzbm")
                    }
                });
                if("1"==zdbc){
                    if (suffixs==''){
                        $.alert("请选择检测类型！");
                        return false;
                    }
                    flag=false;
                    $.ajax({
                        type : "POST",
                        url : "/experimentS/experimentS/samplingSaveExperiment",
                        async:false,
                        data : {"nbbm":nbbm,"ybbh":ybbh,"sjid":sjid,"ids":ids.substring(1),"suffixs":suffixs.substring(1),"print_num":print_num,"print_flg":print_flg,"access_token":$("#ac_tk").val()},
                        dataType : "json",
                        success : function(data){
                            if(data.status == 'success'){
                                preventResubmitForm(".modal-footer > button", false);

                                if(data.print && data.sz_flg){
                                    var list=data.print;
                                    var sz_flg=data.sz_flg;
                                    if (list && list.length>0){
                                        for (let i = 0; i < list.length; i++) {
                                            print_nbbm_qy(list[i],sz_flg);
                                        }
                                    }
                                }

                                $("#samplingExperimentForm #tipDiv").show();
                                $("#samplingExperimentForm #tip").text(data.message+"  内部编码为:"+$("#samplingExperimentForm #nbbm").val());
                                $("#samplingExperimentForm #tip").css("color","green");
                                $("#samplingExperimentForm #nbbm").val("");
                                $("#samplingExperimentForm #sjid").val("");
                                $("#samplingExperimentForm #ybbh").val("");
                                $("#samplingExperimentForm label[name='jclxs']").remove()
                                $("#samplingExperimentForm tr[name='mxList']").remove()
                                init();
                            }else if(data.status == "fail"){
                                preventResubmitForm(".modal-footer > button", false);
                                $("#samplingExperimentForm #tipDiv").show();
                                $("#samplingExperimentForm #tip").text(data.message);
                                $("#samplingExperimentForm #tip").css("color","red");
                            }
                            flag=true;
                        }
                    });
                }
            }
        }
    });

    $("#samplingExperimentForm #nbbm").blur(function(){
        if(flag){
            var nbbm = $("#samplingExperimentForm #nbbm").val();
            if(nbbm){
                getInspection();
            }
        }
    });

}

function print(host,sz_flg){
    var print_url=null
    if(sz_flg=="0"){
        print_url="http://localhost"+host;
        var openWindow = window.open(print_url);
        setTimeout(function(){
            openWindow.close();
        }, 600);
    }else if(sz_flg=="1"){
        var glxx=$("#samplingExperimentForm #glxx").val();
        print_url="http://"+glxx+host;
        var openWindow = window.open(print_url);
        setTimeout(function(){
            openWindow.close();
        }, 600);
    }
}

function getInspection(){
    $("#samplingExperimentForm #sjid").val("");
    $("#samplingExperimentForm label[name='jclxs']").remove()
    $("#samplingExperimentForm tr[name='mxList']").remove()
    $("#samplingExperimentForm #ids").val("");
    $("#samplingExperimentForm #ybbh").val("");
    $("#samplingExperimentForm #suffixs").val("");
    var url = "/experimentS/experimentS/pagedataGetInspect";
    var nbbm = $("#samplingExperimentForm #nbbm").val();
    $.ajax({
        type : "POST",
        url : url,
        async:false,
        data : {"nbbm":nbbm,"jcdws":$("#experiment_formSearch #jcdw_id_tj").val(),"access_token":$("#ac_tk").val()},
        dataType : "json",
        success : function(data){
            var html = "";
            if (data.map){
                for(var key in data.map){
                    var mapElement = data.map[key];
                    var xmmc='';
                    for(var i=0;i<mapElement.length;i++){
                        xmmc+="/"+mapElement[i].xmmc;
                    }
                    if(xmmc){
                        xmmc=xmmc.substring(1);
                    }
                    var zxmmc='';
                    for(var i=0;i<mapElement.length;i++){
                        if(mapElement[i].jczxmmc){
                            zxmmc+="/"+mapElement[i].jczxmmc;
                        }
                    }
                    if(zxmmc){
                        zxmmc=zxmmc.substring(1);
                    }
                    $("#samplingExperimentForm #sjid").val(mapElement[0].sjid);
                    $("#samplingExperimentForm #ybbh").val(mapElement[0].ybbh);
                    html+="<label name='jclxs' id='"+mapElement[0].syglid+"' nbzbm='"+mapElement[0].nbzbm+"' class=\"checkboxLabel checkbox-inline\"style=\"padding-left:0px;\">";
                    html+="<input value='"+mapElement[0].syglid+"' type=\"checkbox\" checked='true' name=\"ids\"/>";
                    if (!mapElement[0].nbzbm)
                    $.alert("检测类型信息丢失请联系管理员解决！");
                    html+="<span style=\"padding-right: 25px;\">"+xmmc+(zxmmc?("_"+zxmmc):"")+"(<span style=\"color:red\">"+mapElement[0].nbzbm+")<span/>)</span></label>";
                }
            }else{
                $("#samplingExperimentForm #tipDiv").show();
                $("#samplingExperimentForm #tip").text("未查询到未取样样品信息");
                $("#samplingExperimentForm #tip").css("color","red");
            }
            $("#samplingExperimentForm #jclx").append(html);
            var infoHtml = "";
            if (data.infoMap){
                var xh=0;
                for(var key in data.infoMap){
                    var mapElement = data.infoMap[key];
                    if (mapElement[0].sfjs == "1"){
                        infoHtml +="<tr name='mxList' style='background-color: darksalmon'>";
                    }else{
                        infoHtml +="<tr name='mxList' style='background-color: moccasin'>";
                    }
                    infoHtml +="<td style=\"line-height:160%;white-space: normal;\">"+(xh+1)+"</td>";
                    xh++;
                    infoHtml +="<td style=\"line-height:160%;white-space: normal;\">"+mapElement[0].jcxmmc+"</td>";
                    infoHtml +="<td style=\"line-height:160%;white-space: normal;\">"+mapElement[0].nbzbm+"</td>";
                    if(mapElement[0].bz){
                        infoHtml +="<td style=\"line-height:160%;white-space: normal;\">"+mapElement[0].bz+"</td></tr>";
                    }else{
                        infoHtml +="<td style=\"line-height:160%;white-space: normal;\"></td></tr>";
                    }
                }
            }
            $("#samplingExperimentForm #infoHtml").append(infoHtml);

        }
    });
}


function init(){
    setTimeout("$('#samplingExperimentForm #nbbm').focus()", 100);
}

function localIpCheck() {
    $("#samplingExperimentForm #remoteDiv").hide();
    $("#samplingExperimentForm #glxx").attr("disabled","disabled");
}

function remoteIpCheck() {
    $("#samplingExperimentForm #remoteDiv").show();
    $("#samplingExperimentForm #glxx").removeAttr("disabled");
}

function printerIpChecked(){
    if($("#samplingExperimentForm #local_ip").attr("checked")){
        $("#samplingExperimentForm #remoteDiv").hide();
        $("#samplingExperimentForm #glxx").attr("disabled","disabled");
    }else if($("#samplingExperimentForm #remote_ip").attr("checked")){
        $("#samplingExperimentForm #remoteDiv").show();
        $("#samplingExperimentForm #glxx").removeAttr("disabled");
    }
}


$(document).ready(function(){
    btnBind();
    init();
    //所有下拉框添加choose样式
    jQuery('#samplingExperimentForm .chosen-select').chosen({width: '100%'});
    printerIpChecked();
    $("#samplingExperimentForm #print_num").val("3");
    $("#samplingExperimentForm #tipDiv").hide();
    if($("#samplingExperimentForm #nbbm").val()){
        getInspection();
    }
});
