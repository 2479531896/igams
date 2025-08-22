$(function(){
    jQuery('#setMessageForm .chosen-select').chosen({width: '100%'});
    var jsonString = $("#setMessageForm #hbJson").val();
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
    $("#setMessageForm #HbDiv").append(html);
    btnBind();
})

function btnBind(){
    var sel_hb = $("#setMessageForm #hb");
    sel_hb.unbind("change").change(function(){
        hbEvent();
    });
}
function hbEvent(){
    var hb = $("#setMessageForm #hb").val();
    var ids = $("#setMessageForm #ids").val();
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
        var jsonString = $("#setMessageForm #hbJson").val();
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
        $("#setMessageForm #HbDiv").append(html);
    }else{
        ids = ids + "," + hb;
        $("#setMessageForm #ids").val(ids);
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
                    $("#setMessageForm #HbDiv").append(html);
                    var jsonString = $("#setMessageForm #hbJson").val();
                    var hbJson = JSON.parse(jsonString);
                    hbJson = hbJson.concat(data);
                    $("#setMessageForm #hbJson").val(JSON.stringify(hbJson));
                }
            }
        });
    }
}

function xxOnclick(szlb,glxx){
    if("HB"==szlb){
        var hb = $("#setMessageForm #hb").val();
        var jsonString = $("#setMessageForm #hbJson").val();
        var hbJson = JSON.parse(jsonString);
        for(var i=0;i<hbJson.length;i++){
            if(hbJson[i].glxx==glxx && hbJson[i].yhid==hb){
                hbJson[i].szz=hbJson[i].szz=="0"?"1":"0";
            }
        }
        $("#setMessageForm #hbJson").val(JSON.stringify(hbJson));
    }else{
        var jsonString = $("#setMessageForm #yhJson").val();
        var yhJson = JSON.parse(jsonString);
        for(var i=0;i<yhJson.length;i++){
            if(yhJson[i].glxx==glxx){
                yhJson[i].szz=yhJson[i].szz=="0"?"1":"0";
            }
        }
        $("#setMessageForm #yhJson").val(JSON.stringify(yhJson));
    }
}