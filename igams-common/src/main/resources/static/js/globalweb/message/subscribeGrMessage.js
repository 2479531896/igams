$(function(){
    $("#setGrMessageForm input[name='access_token']").val($("#ac_tk").val());
    jQuery('#setGrMessageForm .chosen-select').chosen({width: '100%'});
    var jsonString = $("#setGrMessageForm #hbJson").val();
    var hbJson = JSON.parse(jsonString);
    var html = "";
    for(var i=0;i<hbJson.length;i++){
        html = html + "<div class='RadioStyle'><div class='Block PaddingL col-md-2 padding_l0'>"
                +"<input id='"+hbJson[i].glxx +"' value='"+hbJson[i].xxnr + "' name='"+ hbJson[i].xxnr + "' type='checkbox'";
        if(hbJson[i].szz!='0'){
            html = html + "checked='checked'";
        }
        html = html + "/><label  for='" + hbJson[i].glxx
                + "' onclick=\"xxOnclick('"+hbJson[i].szlb+"','"+ hbJson[i].glxx+"')\" style='overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width:120px;'>"
                +hbJson[i].xxnr + "</label></div></div></div>";
    }
    $("#setGrMessageForm #HbDiv").append(html);
    btnBind();
})

function btnBind(){
    var sel_hb = $("#setGrMessageForm #hb");
    sel_hb.unbind("change").change(function(){
        hbEvent();
    });
}
function hbEvent(){
    var hb = $("#setGrMessageForm #hb").val();
    var ids = $("#setGrMessageForm #ids").val();
    var flg = false;
    if(ids){
        var list = ids.split(",");
        for(var i=0;i<list.length;i++){
            if(list[i]==hb){
                flg = true;
            }
        }
    }
    if(flg){
        var jsonString = $("#setGrMessageForm #hbJson").val();
        var data = JSON.parse(jsonString);
        var html = "";
        document.getElementById("HbDiv").innerHTML = "";
        for(var i=0;i<data.length;i++){
            if(data[i].yhid==hb){
                html = html + "<div class='RadioStyle'><div class='Block PaddingL col-md-2 padding_l0'>"
                        +"<input id='"+data[i].glxx +"' value='"+data[i].xxnr + "' name='"+ data[i].xxnr + "' type='checkbox'";
                if(data[i].szz!='0'){
                    html = html + "checked='checked'";
                }
                html = html + "/><label  for='" + data[i].glxx
                        + "' onclick=\"xxOnclick('"+data[i].szlb+"','"+ data[i].glxx+"')\" style='overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width:120px;'>"
                        +data[i].xxnr + "</label></div></div></div>";
            }
        }
        $("#setGrMessageForm #HbDiv").append(html);
    }else{
        ids = ids + "," + hb;
        $("#setGrMessageForm #ids").val(ids);
        url = "/systemrole/user/pagedataGetHbMessage";
        $.ajax({
            type:'post',
            url:url,
            cache: false,
            data: {"yhid":hb,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                document.getElementById("HbDiv").innerHTML = "";
                if(data != null && data.length != 0){
                    var html = "";
                    for(var i=0;i<data.length;i++){
                        html = html + "<div class='RadioStyle'><div class='Block PaddingL col-md-2 padding_l0'>"
                                +"<input id='"+data[i].glxx +"' value='"+data[i].xxnr + "' name='"+ data[i].xxnr + "' type='checkbox'";
                        if(data[i].szz!='0'){
                            html = html + "checked='checked'";
                        }
                        html = html + "/><label  for='" + data[i].glxx
                                + "' onclick=\"xxOnclick('"+data[i].szlb+"','"+ data[i].glxx+"')\" style='overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width:120px;'>"
                                +data[i].xxnr + "</label></div></div></div>";
                    }
                    $("#setGrMessageForm #HbDiv").append(html);
                    var jsonString = $("#setGrMessageForm #hbJson").val();
                    var hbJson = JSON.parse(jsonString);
                    hbJson = hbJson.concat(data);
                    $("#setGrMessageForm #hbJson").val(JSON.stringify(hbJson));
                }
            }
        });
    }
}

function xxOnclick(szlb,glxx){
    if("HB"==szlb){
        var hb = $("#setGrMessageForm #hb").val();
        var jsonString = $("#setGrMessageForm #hbJson").val();
        var hbJson = JSON.parse(jsonString);
        for(var i=0;i<hbJson.length;i++){
            if(hbJson[i].glxx==glxx && hbJson[i].yhid==hb){
                hbJson[i].szz=hbJson[i].szz=="0"?"1":"0";
            }
        }
        $("#setGrMessageForm #hbJson").val(JSON.stringify(hbJson));
    }else{
        var jsonString = $("#setGrMessageForm #yhJson").val();
        var yhJson = JSON.parse(jsonString);
        for(var i=0;i<yhJson.length;i++){
            if(yhJson[i].glxx==glxx){
                yhJson[i].szz=yhJson[i].szz=="0"?"1":"0";
            }
        }
        $("#setGrMessageForm #yhJson").val(JSON.stringify(yhJson));
    }
}

function successBtn() {
    var url = $("#setGrMessageForm #formAction").val();
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data : {"access_token" : $("#ac_tk").val(),"hbJson" : $("#setGrMessageForm #hbJson").val(), "yhJson" : $("#setGrMessageForm #yhJson").val(),"yhid": $("#setGrMessageForm #yhid").val(),"ids": $("#setGrMessageForm #ids").val()},
        success: function(data) {
            if(data.status == 'success'){
                $.success(data.message);
            }else{
                $.error(data.message);
            }
        }
    });
}