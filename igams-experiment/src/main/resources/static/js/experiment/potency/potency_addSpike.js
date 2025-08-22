$("#addSpikeForm .connect").click(function(){
    var zt=this.checked;
    var szval=$(this).val();
    var zmval=$("#addSpikeForm #choose").val();
    var jtval=zmval+szval;
    var thisclass=$(this).attr("class").replace("connect ","");
    if($("#addSpikeForm #flag").val()=="1" && $("#addSpikeForm #positionnr").children('div').length-1>0 && zt==true){
        $(this).prop("checked",false);
    }else{
        if(zt==false){
            $("#addSpikeForm #"+thisclass).prop("checked",false);
            $("#addSpikeForm #"+jtval).remove();
        }
        if(zt==true){
            if($("#addSpikeForm #positionnr").children('div').length-1<10){
                $("#addSpikeForm #positionnr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
                var xzsl=0;
                for(var i=0;i<$("#addSpikeForm ."+thisclass).length;i++){
                    if($("#addSpikeForm ."+thisclass)[i].checked==true){
                        xzsl++
                    }
                }
                if(xzsl==8){
                    $("#addSpikeForm #"+thisclass).prop("checked",true);
                }
            }else{
                $.confirm("已选spike不得超过10个!");
                $(this).prop("checked",false);
            }
        }
    }
});

$("#addSpikeForm #choose").change(function(){
    var zmval=$("#addSpikeForm #choose").val();
    var jtqlist=$("#addSpikeForm ."+zmval).length;
    $("#addSpikeForm .connect").prop("checked",false);
    if(jtqlist>0){
        for(var i=0;i<jtqlist;i++){
            var id=($("#addSpikeForm ."+zmval)[i].id).replace(zmval,"l");
            $("#addSpikeForm #"+id).prop("checked",true);
        }
    }else{
        $("#addSpikeForm .lqx").prop("checked",false);
    }
})

function qxxz(a){
    var sftl=0;
    var conid="l"+a.id;
    var fysid=$("#addSpikeForm #"+a.id).parent().attr("id");
    if(fysid==($("#addSpikeForm #choose").val()+a.id)){
        $("#addSpikeForm #"+conid).prop("checked",false);
    }
    $("#addSpikeForm #"+fysid).remove();
    if ($("#addSpikeForm #"+("l"+fysid.substr(1,2))).attr("class")){
        var sclie=$("#addSpikeForm #"+("l"+fysid.substr(1,2))).attr("class").replace("connect ","");
        var yxlength=$("#addSpikeForm .yxjt").length;
        for(var i=0;i<yxlength;i++){
            var jtxx=$("#addSpikeForm .yxjt")[i].id;
            var boxid="l"+jtxx.substr(1,2);
            if ($("#addSpikeForm #"+boxid).attr("class")){
                var lieid=$("#addSpikeForm #"+boxid).attr("class").replace("connect ","");
                if(sclie==lieid){
                    sftl=sftl+1;
                }
            }
        }
        if(sftl==7){
            $("#addSpikeForm #"+sclie).prop("checked",false);
        }
    }
}

$("#addSpikeForm .lqx").click(function(){
    var lqxid=$(this).attr("id");
    var szval;
    var zmval;
    var jtval;
    if($("#addSpikeForm #flag").val()!="1"){
        if(this.checked==true){
            for(var i=0;i<8;i++){
                szval=$("#addSpikeForm ."+lqxid)[i].value;
                zmval=$("#addSpikeForm #choose").val();
                var zt=$("#addSpikeForm ."+lqxid)[i].checked;
                jtval=zmval+szval;
                var yxsl=$("#addSpikeForm #positionnr").children('div').length-1;
                if(!zt){
                    if(yxsl<10){
                        $("#addSpikeForm #positionnr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
                        $("#addSpikeForm #"+$("#addSpikeForm ."+lqxid)[i].id).prop("checked",true);
                    }else{
                        $(this).prop("checked",false);
                        $.confirm("所选spike不得超过10个!如需修改请手动调整!");
                        return;
                    }
                }
            }
        }else{
            $("#addSpikeForm ."+lqxid).prop("checked",false);
            for(var i=0;i<8;i++){
                szval=$("#addSpikeForm ."+lqxid)[i].value;
                zmval=$("#addSpikeForm #choose").val();
                jtval=zmval+szval;
                $("#addSpikeForm #"+jtval).remove();
            }
        }
    }else{
        $(this).prop("checked",false);
    }
})

$(function(){
    //显示已选spike信息
    if($("#addSpikeForm #flag").val()!="1"){
        var text=$("#addSpikeForm #text").val();
        var zmmr=$("#addSpikeForm #choose").val();

        var list = text.split(",");
        for(var i=0;i<list.length;i++){
            var jtval=list[i];
            var zmval=list[i].substr(0,2);
            var szval=jtval.replace(zmval,"");
            if (jtval){
                $("#addSpikeForm #positionnr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
                if (zmmr === zmval){
                    $("#addSpikeForm #l"+szval).prop("checked",true);
                }
            }
        }
        for(var i=1;i<7;i++){
            var xzsl=0;
            for(var j=0;j<$("#addSpikeForm .l"+i).length;j++){
                if($("#addSpikeForm .l"+i)[j].checked==true){
                    xzsl++
                }
            }
            if(xzsl==8){
                $("#addSpikeForm #l"+i).prop("checked",true);
            }
        }

    }
    //下拉框样式
    jQuery('#addSpikeForm .chosen-select').chosen({width: '100%'});
});