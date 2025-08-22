function reloadWzxx(){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        //返回值
        var html="";
        var thtml="";
        //提取信息表
        for(var i=0;i<sjwzxx_json.length;i++){
            if(!sjwzxx_json[i].sfhb){
                sjwzxx_json[i].sfhb="1";//设置是否汇报标记，若不为1，则报告上不显示
            }
            var checked = sjwzxx_json[i].sfhb=="1"?true:false;
//            sjwzxx_json[i].sfhb="1";//先统计设置为1，后续版本更新后再解除上面限制
            var sjwzid = sjwzxx_json[i].sjwzid?sjwzxx_json[i].sjwzid:'';
            var wzid = sjwzxx_json[i].wzid?sjwzxx_json[i].wzid:'';
            var wzzwm = sjwzxx_json[i].wzzwm?sjwzxx_json[i].wzzwm:'';
            var wzywm = sjwzxx_json[i].wzywm?sjwzxx_json[i].wzywm:'';
            var reads = sjwzxx_json[i].reads?sjwzxx_json[i].reads:'';
            var refANI = sjwzxx_json[i].refANI?sjwzxx_json[i].refANI:'';
            var target = sjwzxx_json[0].target?sjwzxx_json[0].target:'';
            var CleanQ30 = sjwzxx_json[0].cleanQ30?sjwzxx_json[0].cleanQ30:'';
            var RawReads = sjwzxx_json[0].rawReads?sjwzxx_json[0].rawReads:'';
            var CleanReads = sjwzxx_json[0].cleanReads?sjwzxx_json[0].cleanReads:'';
            var spike = sjwzxx_json[0].spike?sjwzxx_json[0].spike:'';
            if(spike && JSON.parse(spike).counts!== undefined){
                spike=JSON.parse(spike).counts;
            }
            if(refANI){
                refANI = Math.floor(refANI * 100) / 100;
            }
            if(sjwzid){
                if(wzid){
                    html+="<tr><td><input class='form-control' type='text' name='"+wzid+"' id='"+wzid+"' value='"+wzid+"' onkeydown=\"if(event.keyCode==13) {getWzxx('"+i+"',this); return false;}\" onblur=\"getWzxx('"+i+"',this)\" readonly/></td>"+
                    "<td 'background-color: #f5f5f5' title='"+wzzwm+"'><a href='#' onclick=\"viewBlast('"+sjwzid+"',this)\">"+wzzwm+"</a></td>"+
                    "<td title='"+wzywm+"'>"+wzywm+"</td>"+
                    "<td><input class='form-control' type='text' name='"+reads+"' id='"+reads+"' value='"+reads+"' onchange=\"updateReads('"+i+"',this)\" readonly/></td>"+
                    "<td><input class='form-control' type='text' name='"+refANI+"' id='"+refANI+"' value='"+refANI+"' onchange=\"updateRefANI('"+i+"',this)\" readonly/></td>"+
                    "<td><div class='input-group'><span class='input-group-addon'><input type='checkbox' id='wz"+i+"' aria-label='...' class='deterpret' value='mngs' onclick=\"delwzxx('" + i + "',event)\" name='mngs'"+(checked?"checked":"")+"></span><input type='text' class='form-control' aria-label='...' value='发送' name='fs' readonly></div></td>"+
                    "</tr>";
                }
            }else{
                html+="<tr><td><input class='form-control' type='text' name='"+wzid+"' id='"+wzid+"' value='"+wzid+"' onkeydown=\"if(event.keyCode==13) {getWzxx('"+i+"',this); return false;}\" onblur=\"getWzxx('"+i+"',this)\" /></td>"+
                "<td title='"+wzzwm+"'>"+wzzwm+"</td>"+
                "<td title='"+wzywm+"'>"+wzywm+"</td>"+
                "<td><input class='form-control' type='text' name='"+reads+"' id='"+reads+"' value='"+reads+"' onchange=\"updateReads('"+i+"',this)\" /></td>"+
                "<td><input class='form-control' type='text' name='"+refANI+"' id='"+refANI+"' value='"+refANI+"' onchange=\"updateRefANI('"+i+"',this)\" /></td>"+
                "<td><div class='input-group'><span class='input-group-addon'><input type='checkbox' id='wz"+i+"' aria-label='...' class='deterpret' onclick=\"delwzxx('" + i + "',event)\" value='mngs' name='mngs'"+(checked?"checked":"")+"></span><input type='text' class='form-control' aria-label='...' value='发送' name='fs' readonly></div></td>"+
                "</tr>";
            }
            if(i==0){
                thtml="<tr>"+
                            "<td title='"+CleanQ30+"'>"+CleanQ30+"</td>"+
                            "<td title='"+RawReads+"'>"+RawReads+"</td>"+
                            "<td title='"+CleanReads+"'>"+CleanReads+"</td>"+
                            "<td title='"+target+"'>"+target+"</td>"+
                            "<td title='"+spike+"'>"+spike+"</td>"+
                "</tr>";
            }
        }

        // 清空元素的内容
        $("#sendReportAjaxForm #editZkxx").empty();
        $("#sendReportAjaxForm #editZkxx").append(thtml);
        // 清空元素的内容
        $("#sendReportAjaxForm #editWzxx").empty();
        $("#sendReportAjaxForm #editWzxx").append(html);
        $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));//将加了区分的sjwzxx_json重新赋值给隐藏域
    }
}

function viewBlast(sjwzid){
    $.ajax({
        type:'post',
        url:"/inspection/inspection/pagedataViewBlast",
        data: {
            sjwzid : sjwzid,
            sjid : $("#sendReportAjaxForm #sjid").val(),
            access_token : $("#ac_tk").val()
        },
        dataType:'json',
        success:function(map){
            //返回值
            if(map.str){
                // 直接使用encodeURIComponent编码，为了处理参数的换行符，让blast可以正确解析参数
                const encodedStr = encodeURIComponent(map.str);
                window.open(`https://blast.ncbi.nlm.nih.gov/Blast.cgi?PROGRAM=blastn&PAGE_TYPE=BlastSearch&LINK_LOC=blasthome&QUERY=${encodedStr}`);
            }else{
                alert("未查询到blast结果");
            }
        }
    });
}

function reloadNyx(){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        //返回值
        var html="";
        //提取信息表
        for(var i=0;i<sjnyx_json.length;i++){
            var add=false;
            if(!sjnyx_json[i].sfhb){
                sjnyx_json[i].sfhb="1";//设置是否汇报标记，若不为1，则报告上不显示
            }
//            if(sjnyx_json[i].sfhb!='2'){
//                sjnyx_json[i].sfhb="1";//先统计设置为1，后续版本更新后再解除上面限制,2的数据除外
//            }
            if(sjnyx_json[i].sfhb!='2'){
                add=true;
            }
            if(add){
                var tbjy = "";
                var tbjg = sjnyx_json[i].tbjg?sjnyx_json[i].tbjg:'';
                if(tbjg){//如果突变结果不为空，截取突变结果_前的内容作为突变基因显示内容，否则直接取突变基因
                    tbjy=tbjg.split("_")[0];
                }else{
                    tbjy = sjnyx_json[i].tbjy?sjnyx_json[i].tbjy:'';
                }
                var nyx = sjnyx_json[i].nyx?sjnyx_json[i].nyx:'';
                var tbsd = sjnyx_json[i].tbsd?sjnyx_json[i].tbsd:'';
                var tbpl = sjnyx_json[i].tbpl?sjnyx_json[i].tbpl:'';
                var bjtb = sjnyx_json[i].bjtb?sjnyx_json[i].bjtb:'';
                var jcbl = sjnyx_json[i].jcbl?sjnyx_json[i].jcbl:'';
                var sjnyxid = sjnyx_json[i].sjnyxid?sjnyx_json[i].sjnyxid:'';
                var checked = sjnyx_json[i].sfhb=="1"?true:false;
                html+="<tr><td><input class='form-control' type='text' name='"+tbjy+"' id='"+tbjy+"' value='"+tbjy+"' onchange=\"updateTbjy('"+i+"',this)\" /></td>"+
                "<td><input class='form-control' type='text' name='"+tbjg+"' id='"+tbjg+"' value='"+tbjg+"' onchange=\"updateTbjg('"+i+"',this)\" /></td>"+
                "<td><input class='form-control' type='text' name='"+nyx+"' id='"+nyx+"' value='"+nyx+"' onchange=\"updateNyx('"+i+"',this)\" /></td>"+
                "<td><input class='form-control' type='text' name='"+tbsd+"' id='"+tbsd+"' value='"+tbsd+"' onchange=\"updateTbsd('"+i+"',this)\" /></td>"+
                "<td><input class='form-control' type='text' name='"+tbpl+"' id='"+tbpl+"' value='"+tbpl+"' onchange=\"updateTbpl('"+i+"',this)\" /></td>"+
                "<td><input class='form-control' type='text' name='"+bjtb+"' id='"+bjtb+"' value='"+bjtb+"' onchange=\"updateBjtb('"+i+"',this)\" /></td>"+
                "<td><a href='javascript:void(0);' onclick='toViewOther(\"" + tbjy+"\",\""+ sjnyxid + "\",\""+ tbjg+ "\")' >"+jcbl+"</a></td>"+
                "<td><div class='input-group'><span class='input-group-addon'><input id='nyx"+i+"' onclick=\"delnyx('" + i + "',event)\"  type='checkbox' aria-label='...' class='deterpret' value='mngs' name='mngs'"+(checked?"checked":"")+"></span><input type='text' class='form-control' aria-label='...' value='汇报' name='fs' readonly></div></td>"+
                "</tr>";
            }
        }
        // 清空元素的内容
        $("#sendReportAjaxForm #editNyx").empty();
        $("#sendReportAjaxForm #editNyx").append(html);
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));//将加了区分的sjnyx_json重新赋值给隐藏域
    }
}

function reloadZkxx(){
    var zkxx_jsonStr = $("#sendReportAjaxForm #zkxx_json").val();
    if (zkxx_jsonStr){
        var zkxx_json = JSON.parse(zkxx_jsonStr);
        //返回值
        var html="";
        //提取信息表
        for(var i=0;i<zkxx_json.length;i++){
            var sjwzid = zkxx_json[i].sjwzid?zkxx_json[i].sjwzid:'';
            var wzid = zkxx_json[i].wzid?zkxx_json[i].wzid:'';
            var nbbm = zkxx_json[i].nbbm?zkxx_json[i].nbbm:'';
            var wzzwm = zkxx_json[i].wzzwm?zkxx_json[i].wzzwm:'';
            var wzywm = zkxx_json[i].wzywm?zkxx_json[i].wzywm:'';
            var reads = zkxx_json[i].reads?zkxx_json[i].reads:'';
            var refANI = zkxx_json[i].refANI?zkxx_json[i].refANI:'';
            var target = zkxx_json[0].target?zkxx_json[0].target:'';
            var CleanQ30 = zkxx_json[0].cleanQ30?zkxx_json[0].cleanQ30:'';
            var RawReads = zkxx_json[0].rawReads?zkxx_json[0].rawReads:'';
            var CleanReads = zkxx_json[0].cleanReads?zkxx_json[0].cleanReads:'';
            var spike = zkxx_json[0].spike?zkxx_json[0].spike:'';
            if(refANI){
                refANI = Math.floor(refANI * 100) / 100;
            }
            html+="<tr>"+
            "<td title='"+nbbm+"'>"+nbbm+"</td>"+
            "<td title='"+wzzwm+"'>"+wzzwm+"</td>"+
            "<td title='"+wzywm+"'>"+wzywm+"</td>"+
            "<td title='"+reads+"'>"+reads+"</td>"+
            "<td title='"+target+"'>"+target+"</td>"+
            "</tr>";
        }

        // 清空元素的内容
        $("#sendReportAjaxForm #editBbzk").empty();
        $("#sendReportAjaxForm #editBbzk").append(html);
    }
}

function toViewOther(tbjy,sjnyxid,tbjg){
    var url="/inspection/inspection/pagedataOtherNyxxPage?sjid="+$("#sendReportAjaxForm #sjid").val()+"&jclx=" + $("#sendReportAjaxForm #jclx").val()+"&tbjy=" + tbjy+"&tbjg=" + tbjg+"&sjnyxid=" + sjnyxid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'同位检出信息',viewOtherConfig);
}

var viewOtherConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function updateReads(index,e){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        sjwzxx_json[index].reads = e.value;
        $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
    }
}
function updateRefANI(index,e){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        sjwzxx_json[index].refANI = e.value;
        $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
    }
}
function updateTarget(index,e){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        sjwzxx_json[index].target = e.value;
        $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
    }
}
function updateTbjg(index,e){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json[index].tbjg = e.value;
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
function updateNyx(index,e){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json[index].nyx = e.value;
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
function updateTbsd(index,e){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json[index].tbsd = e.value;
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
function updateTbpl(index,e){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json[index].tbpl = e.value;
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
function updateTbjy(index,e){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json[index].tbjy = e.value;
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
function updateBjtb(index,e){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json[index].bjtb = e.value;
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
/**
 * 删除操作(删除物种信息)
 * @param index
 * @param event
 * @returns
 */
function delwzxx(index,event){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        if($("#sendReportAjaxForm #wz"+index).prop("checked")==false){
            sjwzxx_json[index].sfhb="0";
        }else{
            sjwzxx_json[index].sfhb="1";
        }
        $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
    }
}
/**
 * 删除操作(删除耐药信息)
 * @param index
 * @param event
 * @returns
 */
function delnyx(index,event){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        if($("#sendReportAjaxForm #nyx"+index).prop("checked")==false){
            sjnyx_json[index].sfhb="0";
        }else{
            sjnyx_json[index].sfhb="1";
        }
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
    }
}
function getWzxx(index,e){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        if (e.value!= sjwzxx_json[index].wzid){
            if (e.value){
                // 添加逻辑以避免重复触发
                if (!$(e).hasClass('processing')) {
                    $(e).addClass('processing');
                    $.ajax({
                        type : 'get',
                        url :"/wzjb/wzjb/pagedataGetInfoByWzid",
                        cache : false,
                        data : {
                            "access_token" : $("#ac_tk").val(),
                            "wzid" : e.value
                        },
                        dataType : 'json',
                        success : function(data) {
                            $(e).removeClass('processing');
                            if (data.wzglDto){
                                var wzid = data.wzglDto.wzid;
                                var wzzwm = data.wzglDto.wzzwm;
                                var wzywm = data.wzglDto.wzywm;
                                var wzfl =data.wzglDto.wzfl;
                                sjwzxx_json[index].wzzwm = wzzwm;
                                sjwzxx_json[index].wzywm = wzywm;
                                sjwzxx_json[index].wzid = wzid;
                                sjwzxx_json[index].jglx = "possible";
                                sjwzxx_json[index].wzfl=wzfl;
                                $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
                                reloadWzxx()
                            }else{
                                sjwzxx_json[index].wzzwm = '';
                                sjwzxx_json[index].wzywm = '';
                                sjwzxx_json[index].wzid = e.value;
                                $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
                                reloadWzxx();
                                $.alert("未找到对应物种信息！");
                            }
                        },
                        error: function(xhr, status, error) {
                            $(e).removeClass('processing');
                            $.alert("请求失败，请重试！");
                        }
                    });
                }
            }else{
                sjwzxx_json[index].wzzwm = '';
                sjwzxx_json[index].wzywm = '';
                sjwzxx_json[index].wzid = e.value;
                $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
                reloadWzxx();
                $.alert("请输入物种ID（TaxID）！");
            }
        }
    }


}
function addWzxx(){
    var sjwzxx_jsonStr = $("#sendReportAjaxForm #sjwzxx_json").val();
    if (sjwzxx_jsonStr){
        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
        sjwzxx_json.push({
            "sjwzid" : "",
            "wzid" : "",
            "wzzwm" : "",
            "wzywm" : "",
            "mappingReads" : "",
            "reads" : "",
            "refANI" : "",
            "target" : ""
        });
        $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(sjwzxx_json));
        reloadWzxx()
    }
}
function addNyx(){
    var sjnyx_jsonStr = $("#sendReportAjaxForm #sjnyx_json").val();
    if (sjnyx_jsonStr){
        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
        sjnyx_json.push({
            "sjnyxid":"",
            "wkbh":"",
            "tbjy" : "",
            "tbjg" : "",
            "nyx" : "",
            "tbsd" : "",
            "tbpl" : "",
            "bjtb" : ""
        });
        $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(sjnyx_json));
        reloadNyx()
    }
}

function viewSjxx(sjid){
    var url="/inspection/inspection/viewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewInspectionConfig);
}

function viewSxpage(ybbh){
    //http://dx.matridx.com:8000/mngs/library-result/?sample=" +ybbh+"&access_token="+ $("#ac_tk").val()
    var bioaudurl = $("#sendReportAjaxForm #bioaudurl").val()
    window.open(bioaudurl + "/mngs/library-result/?sample=" +ybbh+"&access_token="+ $("#ac_tk").val());
}

/*查看送检信息模态框*/
var viewInspectionConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function toViewLl(llh){
    var url="/inspection/inspection/pagedataLl?sjid="+$("#sendReportAjaxForm #sjid").val()+"&jclx="+$("#sendReportAjaxForm #jclx").val()+"&llh="+llh+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'结核耐药履历',viewWzysxxConfig);
}

function toViewTngs(){
    var url="/inspection/inspection/pagedataTngs?sjid="+$("#sendReportAjaxForm #sjid").val()+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'tNGS结果查看',viewWzysxxConfig);
}

/*查看送检信息模态框*/
var viewWzysxxConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


$(function(){
    reloadWzxx();
    reloadNyx();
    reloadZkxx();
})

    $(function () { $('#collapseTwo').collapse('show')});
    $(function () { $('#collapseOne').collapse('hide')});