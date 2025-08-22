/*获取冰箱list*/
function getDcAndDrBxList() {
    var jcdw = document.getElementById("jcdw").value;
    if (jcdw){
        $.ajax({
            type: "post",
            url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByJcdw",
            data: {"jcdws":jcdw,"access_token":$("#ac_tk").val()},
            dataType: "json",
            success: function (data) {
                if(data != null && data.length != 0){
                    //调出冰箱
                    var DcBxZHtml = "";
                    $.each(data,function(i){
                        DcBxZHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                    });
                    $("#ybModBoxForm #dcbxid").empty();
                    $("#ybModBoxForm #dcbxid").append(DcBxZHtml);
                    $("#ybModBoxForm #dcbxid").trigger("chosen:updated");
                    //调入冰箱
                    var DrBxZHtml = "";
                    $.each(data,function(i){
                        DrBxZHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                    });
                    $("#ybModBoxForm #drbxid").empty();
                    $("#ybModBoxForm #drbxid").append(DrBxZHtml);
                    $("#ybModBoxForm #drbxid").trigger("chosen:updated");
                    //随便拿一个
                    var dcbxid = document.getElementById("dcbxid").value;
                    $.ajax({
                        type: "post",
                        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                        data: {"fids":dcbxid,"access_token":$("#ac_tk").val()},
                        dataType: "json",
                        success: function (data) {
                            if(data != null && data.length != 0){
                                //调出抽屉
                                var DcCtZHtml = "";
                                $.each(data,function(i){
                                    DcCtZHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
                                });
                                $("#ybModBoxForm #dcchtid").empty();
                                $("#ybModBoxForm #dcchtid").append(DcCtZHtml);
                                $("#ybModBoxForm #dcchtid").trigger("chosen:updated");
                                //调入抽屉
                                var DrCtZHtml = "";
                                $.each(data,function(i){
                                    DrCtZHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
                                });
                                $("#ybModBoxForm #drchtid").empty();
                                $("#ybModBoxForm #drchtid").append(DrCtZHtml);
                                $("#ybModBoxForm #drchtid").trigger("chosen:updated");
                                //随便拿一个
                                var dcchtid = document.getElementById("dcchtid").value;
                                /*获取盒子list*/
                                $.ajax({
                                    type: "post",
                                    url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                                    data: {"fids":dcchtid,"access_token":$("#ac_tk").val()},
                                    dataType: "json",
                                    success: function (data) {
                                        if(data != null && data.length != 0){
                                            //调出盒子
                                            var DcHzZHtml = "";
                                            $.each(data,function(i){
                                                DcHzZHtml += "<option sbh= '"+ data[i].sbh+"' ycfs= '"+ data[i].ycfs+"' xgsj= '"+ data[i].xgsj+"' sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':"("+data[i].sbh+")") + "</option>";
                                            });
                                            $("#ybModBoxForm #dchzid").empty();
                                            $("#ybModBoxForm #dchzid").append(DcHzZHtml);
                                            $("#ybModBoxForm #dchzid").trigger("chosen:updated");
                                            //调入盒子
                                            var DrHzZHtml = "";
                                            $.each(data,function(i){
                                                DrHzZHtml += "<option sbh= '"+ data[i].sbh+"' ycfs= '"+ data[i].ycfs+"' xgsj= '"+ data[i].xgsj+"' sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':"("+data[i].sbh+")") + "</option>";
                                            });
                                            $("#ybModBoxForm #drhzid").empty();
                                            $("#ybModBoxForm #drhzid").append(DrHzZHtml);
                                            $("#ybModBoxForm #drhzid").trigger("chosen:updated");

                                        }else{
                                            EmptySb('dchzid','该抽屉下没有盒子')
                                            EmptySb('drhzid','该抽屉下没有盒子')
                                        }
                                    }
                                })
                            }else{
                                EmptySb('dcchtid','该冰箱下没有抽屉')
                                EmptySb('drchtid','该冰箱下没有抽屉')

                                EmptySb('dchzid','该抽屉下没有盒子')
                                EmptySb('drhzid','该抽屉下没有盒子')
                            }
                        }
                    })
                }else{
                    EmptySb('dcbxid','该存储单位下没有冰箱')
                    EmptySb('drbxid','该存储单位下没有冰箱')

                    EmptySb('dcchtid','该冰箱下没有抽屉')
                    EmptySb('drchtid','该冰箱下没有抽屉')

                    EmptySb('dchzid','该抽屉下没有盒子')
                    EmptySb('drhzid','该抽屉下没有盒子')
                }
            }
        })
    }else{
        EmptySb('dcbxid','该存储单位下没有冰箱')
        EmptySb('drbxid','该存储单位下没有冰箱')

        EmptySb('dcchtid','该冰箱下没有抽屉')
        EmptySb('drchtid','该冰箱下没有抽屉')

        EmptySb('dchzid','该抽屉下没有盒子')
        EmptySb('drhzid','该抽屉下没有盒子')
    }
}
//空设备
function EmptySb(id,emptyData){
    var HzZHtml = "";
    HzZHtml += "<option value=''>--"+emptyData+"--</option>";
    $("#ybModBoxForm #"+id).empty();
    $("#ybModBoxForm #"+id).append(HzZHtml);
    $("#ybModBoxForm #"+id).trigger("chosen:updated");
}
/*获取抽屉list*/
function getDcCtList() {
    var dcbxid = document.getElementById("dcbxid").value;
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
        data: {"fids":dcbxid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
                });
                $("#ybModBoxForm #dcchtid").empty();
                $("#ybModBoxForm #dcchtid").append(zHtml);
                $("#ybModBoxForm #dcchtid").trigger("chosen:updated");
                var dcchtid = document.getElementById("dcchtid").value;
                /*获取盒子list*/
                $.ajax({
                    type: "post",
                    url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                    data: {"fids":dcchtid,"access_token":$("#ac_tk").val()},
                    dataType: "json",
                    success: function (data) {
                        if(data != null && data.length != 0){
                            var zHtml = "";
                            $.each(data,function(i){
                                zHtml += "<option sbh= '"+ data[i].sbh+"' ycfs= '"+ data[i].ycfs+"' xgsj= '"+ data[i].xgsj+"' sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                            });
                            $("#ybModBoxForm #dchzid").empty();
                            $("#ybModBoxForm #dchzid").append(zHtml);
                            $("#ybModBoxForm #dchzid").trigger("chosen:updated");
                            
                        }else{
                            EmptySb('dchzid','该抽屉下没有盒子')
                        }
                    }
                })
            }else{
                EmptySb('dcchtid','该冰箱下没有抽屉')
                EmptySb('dchzid','该抽屉下没有盒子')
            }
        }
    })
}
/*获取盒子list*/
function getDcHzList(){
    var dcchtid = document.getElementById("dcchtid").value;
    /*获取盒子list*/
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
        data: {"fids":dcchtid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option sbh= '"+ data[i].sbh+"' ycfs= '"+ data[i].ycfs+"' xgsj= '"+ data[i].xgsj+"'  sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                });
                $("#ybModBoxForm #dchzid").empty();
                $("#ybModBoxForm #dchzid").append(zHtml);
                $("#ybModBoxForm #dchzid").trigger("chosen:updated");
                
            }else{
                EmptySb('dchzid','该抽屉下没有盒子')
            }
        }
    })
}
/*获取抽屉list*/
function getDrCtList() {
    var drbxid = document.getElementById("drbxid").value;
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
        data: {"fids":drbxid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
                });
                $("#ybModBoxForm #drchtid").empty();
                $("#ybModBoxForm #drchtid").append(zHtml);
                $("#ybModBoxForm #drchtid").trigger("chosen:updated");
                var drchtid = document.getElementById("drchtid").value;
                /*获取盒子list*/
                $.ajax({
                    type: "post",
                    url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                    data: {"fids":drchtid,"access_token":$("#ac_tk").val()},
                    dataType: "json",
                    success: function (data) {
                        if(data != null && data.length != 0){
                            var zHtml = "";
                            $.each(data,function(i){
                                zHtml += "<option sbh= '"+ data[i].sbh+"' ycfs= '"+ data[i].ycfs+"' xgsj= '"+ data[i].xgsj+"' sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                            });
                            $("#ybModBoxForm #drhzid").empty();
                            $("#ybModBoxForm #drhzid").append(zHtml);
                            $("#ybModBoxForm #drhzid").trigger("chosen:updated");
                            
                        }else{
                            EmptySb('drhzid','该抽屉下没有盒子')
                        }
                    }
                })
            }else{
                EmptySb('drchtid','该冰箱下没有抽屉')
                EmptySb('drhzid','该抽屉下没有盒子')
            }
        }
    })
}
/*获取盒子list*/
function getDrHzList(){
    var drchtid = document.getElementById("drchtid").value;
    /*获取盒子list*/
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
        data: {"fids":drchtid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option sbh= '"+ data[i].sbh+"' ycfs= '"+ data[i].ycfs+"' xgsj= '"+ data[i].xgsj+"'  sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                });
                $("#ybModBoxForm #drhzid").empty();
                $("#ybModBoxForm #drhzid").append(zHtml);
                $("#ybModBoxForm #drhzid").trigger("chosen:updated");
                
            }else{
                EmptySb('drhzid','该抽屉下没有盒子')
            }
        }
    })
}
$(function(){
    //所有下拉框添加choose样式
    jQuery('#ybModBoxForm .chosen-select').chosen({width: '100%'});
});

