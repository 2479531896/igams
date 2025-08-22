
function init(){
    setTimeout("$('#recheckAjaxForm #ybbh').focus()", 100);
}
$(function(){
    //所有下拉框添加choose样式
    jQuery('#recheckAjaxForm .chosen-select').chosen({width: '100%'});
    init();
    //添加日期控件
    laydate.render({
        elem: '#recheckAjaxForm #syrq'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    laydate.render({
        elem: '#recheckAjaxForm #dsyrq'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    laydate.render({
        elem: '#recheckAjaxForm #qtsyrq'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });


})



function modifJcdw(){
    $.confirm('确定要修改当前检测单位?',function(result){
        if(result){
            var fjid=$("#recheckAjaxForm #fjid").val();
            var jcdw=$("#recheckAjaxForm #jcdw").val();
            var url=$("#recheckAjaxForm #url").val();
            $.ajax({
                url:url,
                type : "POST",
                data:{"fjid":fjid,"jcdw":jcdw,"access_token":$("#ac_tk").val()},
                dataType:"json",
                success:function(data){
                    if(data.status){
                        $.success("修改成功！");
                    }else{
                        $.error("修改失败！");
                    }
                }
            })
        }
    })
}

function getInfo() {
//回车执行查询
    let ybbh = $("#recheckAjaxForm  #ybbh").val();
    if (ybbh){
        var url = "/recheck/recheck/pagedataInfoByYbbh";
        $.ajax({
            type: 'post',
            url: url,
            data: {"ybbh": ybbh, "access_token": $("#ac_tk").val()},
            dataType: 'json',
            success: function (data) {
                if (data.sjxxDto != null) {
                    $("#recheckAjaxForm #ts").text("已找到记录!");
                    var div = document.getElementById('jg');
                    div.setAttribute("class", "green");
                    if(data.sjxxDto.hzxm){
                        $("#recheckAjaxForm #hzxm").text(data.sjxxDto.hzxm);
                    }
                    if(data.sjxxDto.nl){
                        $("#recheckAjaxForm #nl").text(data.sjxxDto.nl);
                    }
                    if(data.sjxxDto.nldw){
                        $("#recheckAjaxForm #nldw").text(data.sjxxDto.nldw);
                    }
                    if(data.sjxxDto.xbmc){
                        $("#recheckAjaxForm #xbmc").text(data.sjxxDto.xbmc);
                    }
                    if(data.sjxxDto.sjdw){
                        $("#recheckAjaxForm #sjdw").text(data.sjxxDto.hospitalname);
                    }
                    if(data.sjxxDto.ksmc){
                        $("#recheckAjaxForm #ksmc").text(data.sjxxDto.ksmc);
                    }
                    if(data.sjxxDto.jcxmmc){
                        $("#recheckAjaxForm #jcxmmc").text(data.sjxxDto.jcxmmc);
                    }
                    if(data.sjxxDto.lxmc){
                        $("#recheckAjaxForm #lxmc").text(data.sjxxDto.lxmc);
                    }
                    if(data.sjxxDto.nbbm){
                        $("#recheckAjaxForm #nbbm").val(data.sjxxDto.nbbm);
                    }
                    if(data.sjxxDto.sjid){
                        $("#recheckAjaxForm #sjid").val(data.sjxxDto.sjid);
                    }
                    if(data.fjsqDto.fjid){
                        $("#recheckAjaxForm #fjid").val(data.fjsqDto.fjid);
                    }
                    if(data.sjxxDto.yblxmc){
                        $("#recheckAjaxForm #yblxmc").text(data.sjxxDto.yblxmc);
                    }
                    if(data.sjxxDto.db){
                        $("#recheckAjaxForm #db").text(data.sjxxDto.db);
                    }
                    if(data.sjxxDto.cskz1){
                        $("#recheckAjaxForm #cskz1").val(data.sjxxDto.cskz1);
                    }
                    if(data.fjsqDto.lx){
                        $("#recheckAjaxForm #lx").val(data.fjsqDto.lx);
                    }
                    if(data.fjsqDto.fjlxdm){
                        $("#recheckAjaxForm #fjlxdm").val(data.fjsqDto.fjlxdm);
                    }
                    if(data.fjsqDto.f_bz){
                        $("#recheckAjaxForm #bz").text(data.fjsqDto.f_bz);
                    }
                    if(data.fjsqDto.bgbj){
                        if(data.fjsqDto.bgbj=='0'){
                            $("[name='bgbj']").get(0).checked = true
                        }else if(data.fjsqDto.bgbj=='1'){
                            $("[name='bgbj']").get(1).checked = true
                        }
                    }
                    if (data.fjsqDto.sfff) {
                        if (null != data.fjsqDto.sfff && '' != data.fjsqDto.sfff) {
                            $("#recheckAjaxForm #sfff").val(data.fjsqDto.sfff);
                            $('#recheckAjaxForm #sfff').trigger("chosen:updated");
                        }
                    }
                    if (data.fjsqDto.jcdw) {
                        if (null != data.fjsqDto.jcdw && '' != data.fjsqDto.jcdw) {
                            $("#recheckAjaxForm #jcdw").val(data.fjsqDto.jcdw);
                            $('#recheckAjaxForm #jcdw').trigger("chosen:updated");
                        }
                    }

                    if(data.fjyylist!=null&&data.fjyylist.length>0){
                        for(var i = 0; i < data.fjyylist.length; i++){
                            var yhHtml="<div class='col-xs-12 col-sm-12 col-md-12 col-lg-12 yyDiv'  id='yy_"+data.fjyylist[i].csid+"' style='padding-left:0px;'>";
                            yhHtml+="<label style='padding-left:16px;' class='checkboxLabel checkbox-inline'>";
                            if(data.fjyylist[i].checked==1){yhHtml+="<input class='yy' style='margin-left: 0px;' type='radio' name='yyinput'  value='"+data.fjyylist[i].csid+"' fcsid='"+data.fjyylist[i].fcsid+"' checked='checked'>";}
                            else{yhHtml+="<input class='yy' style='margin-left: 0px;' type='radio' name='yyinput'  value='"+data.fjyylist[i].csid+"' fcsid='"+data.fjyylist[i].fcsid+"'>";}
                            yhHtml+="<span style='margin-left: 20px;'>"+data.fjyylist[i].csmc+"</span>";
                            yhHtml+="</label>";
                            yhHtml+="</div>";
                            $("#recheckAjaxForm #fcyyDiv").append(yhHtml);
                        }
                    }
                    var lxdm=$("#recheckAjaxForm #fjlxdm").val();
                    //加测项目
                    if(lxdm=="ADDDETECT"||lxdm=="LAB_RECHECK"){
                        $("#recheckAjaxForm .jcxmDiv").each(function(){
                            if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1){
                                $(this).hide();
                            }
                        });
                        $("[name='jcxm']").each(function(){
                            if (data.fjsqDto.jcxm) {
                                if(data.fjsqDto.jcxm.indexOf($(this).val())!=-1){
                                    $(this).attr("checked",true);
                                }
                            }
                        });

                        if(lxdm=="ADDDETECT"){
                            $("#recheckAjaxForm #jcxm_span").text("加测项目");
                        }else{
                            $("#recheckAjaxForm #jcxm_span").text("实验室加测项目");
                        }
                    }else if(lxdm=="REM"||lxdm=="LAB_REM"){
                        $("#recheckAjaxForm .jcxmDiv").each(function(){
                            if($(this).attr("cskz3")==null||$(this).attr("cskz3").indexOf("_REM")==-1||this.id =="jcxm_R"||this.id =="jcxm_C"){
                                $(this).hide();
                            }
                        });
                        $("[name='jcxm']").each(function(){
                            if (data.fjsqDto.jcxm) {
                                if(data.fjsqDto.jcxm.indexOf($(this).val())!=-1){
                                    $(this).attr("checked",true);
                                }
                            }
                        });
                        if(lxdm=="REM"){
                            $("#recheckAjaxForm #jcxm_span").text("加测去人源");
                        }else{
                            $("#recheckAjaxForm #jcxm_span").text("实验室加测去人源");
                        }

                    }else if(lxdm=="RECHECK"||lxdm=="LAB_ADDDETECT"){
                        var cskz1=$("#recheckAjaxForm #cskz1").val();
                        if(cskz1=="D"){
                            $("#recheckAjaxForm #jcxm_D").show();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="C"){
                            $("#recheckAjaxForm #jcxm_D").show();
                            $("#recheckAjaxForm #jcxm_R").show();
                            $("#recheckAjaxForm #jcxm_C").show();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="R"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").show();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        // }else if(cskz1=="Z6"){
                        }else if(cskz1=="Z" || cskz1=="Z6"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").show();
                            $("#recheckAjaxForm #jcxm_Z6").show();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="Z8"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").show();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="Z12"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").show();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="Z18"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").show();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="F"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").show();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="T3"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").show();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="T6"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").show();
                            $("#recheckAjaxForm #jcxm_K").hide();
                        }else if(cskz1=="K"){
                            $("#recheckAjaxForm #jcxm_D").hide();
                            $("#recheckAjaxForm #jcxm_R").hide();
                            $("#recheckAjaxForm #jcxm_C").hide();
                            $("#recheckAjaxForm #jcxm_Z").hide();
                            $("#recheckAjaxForm #jcxm_Z6").hide();
                            // $("#recheckAjaxForm #jcxm_Z8").hide();
                            // $("#recheckAjaxForm #jcxm_Z12").hide();
                            // $("#recheckAjaxForm #jcxm_Z18").hide();
                            $("#recheckAjaxForm #jcxm_F").hide();
                            $("#recheckAjaxForm #jcxm_T3").hide();
                            $("#recheckAjaxForm #jcxm_T6").hide();
                            $("#recheckAjaxForm #jcxm_K").show();
                        }

                        $("#recheckAjaxForm .jcxmDiv").each(function(){
                            if($(this).attr("cskz3")!=null&&$(this).attr("cskz3").indexOf("_REM")!=-1){
                                $(this).hide();
                            }
                        });
                        $("[name='jcxm']").each(function(){
                            if (data.fjsqDto.jcxm) {
                                if(data.fjsqDto.jcxm.indexOf($(this).val())!=-1){
                                    $(this).attr("checked",true);
                                }
                            }
                        });
                        if(lxdm=="RECHECK"){
                            $("#recheckAjaxForm #jcxm_span").text("复测项目");
                        }else{
                            $("#recheckAjaxForm #jcxm_span").text("实验室复测项目");
                        }

                    }else{
                        $("[name='jcxm']").each(function(){
                            if (data.fjsqDto.jcxm) {
                                if(data.fjsqDto.jcxm.indexOf($(this).val())!=-1){
                                    $(this).attr("checked",true);
                                }
                            }
                        });
                        $("#recheckAjaxForm #jcxm_span").text("项目");
                    }

                    if($("#fixtop #lastStep").val()=="true"|| $("#auditAjaxForm #lastStep").val() =="true"){
                        $("#recheckAjaxForm .jcxm_radio").each(function(index){
                            if(this.checked==true){
                                var cskz1=$(this).attr("cskz1");
                                switchs(cskz1);
                            }
                        })
                    }
                } else {
                    $("#recheckAjaxForm #ts").text("未找到记录!");
                    var div = document.getElementById('jg');
                    div.setAttribute("class", "red");
                }
            }
        });
        //判断是否显示检测单位修改按钮
        //判断审核类别是否存在，如果审核类别存在说明是什么操作，如果不存在说明是修改操作
        if($("#fixtop #shlb").val()){
            $("#recheckAjaxForm #modifBtn").show();
            $("#recheckAjaxForm #url").val("/recheck/recheck/pagedataUpdateJcdw");
        }else if($("#auditAjaxForm #shlb").val()){
            $("#recheckAjaxForm #modifBtn").show();
            $("#recheckAjaxForm #url").val("/ws/recheck/pagedataUpdateJcdw");
        }else{
            $("#recheckAjaxForm #modifBtn").hide();
        }
        function switchs(cskz1){
            if(cskz1=="D"){
                $("#recheckAjaxForm #D_div").show();
                $("#recheckAjaxForm #R_div").hide();
                $("#recheckAjaxForm #qt_div").hide();
                $("#recheckAjaxForm #dsyrq").removeAttr("disabled");
                $("#recheckAjaxForm #syrq").attr("disabled","disabled");
                $("#recheckAjaxForm #qtsyrq").attr("disabled","disabled");
            }else if(cskz1=="R"){
                $("#recheckAjaxForm #R_div").show();
                $("#recheckAjaxForm #D_div").hide();
                $("#recheckAjaxForm #qt_div").hide();
                $("#recheckAjaxForm #syrq").removeAttr("disabled");
                $("#recheckAjaxForm #dsyrq").attr("disabled","disabled");
                $("#recheckAjaxForm #qtsyrq").attr("disabled","disabled");
            }else if(cskz1=="C"){
                $("#recheckAjaxForm #D_div").show();
                $("#recheckAjaxForm #R_div").show();
                $("#recheckAjaxForm #qt_div").hide();
                $("#recheckAjaxForm #dsyrq").removeAttr("disabled");
                $("#recheckAjaxForm #syrq").removeAttr("disabled");
                $("#recheckAjaxForm #qtsyrq").attr("disabled","disabled");
            }else if(cskz1=="Z" || cskz1=="F"){
                $("#recheckAjaxForm #R_div").hide();
                $("#recheckAjaxForm #D_div").hide();
                $("#recheckAjaxForm #qt_div").show();
                $("#recheckAjaxForm #qtsyrq").removeAttr("disabled");
                $("#recheckAjaxForm #dsyrq").attr("disabled","disabled");
                $("#recheckAjaxForm #syrq").attr("disabled","disabled");
            }
        }

        $("#recheckAjaxForm .jcxm_radio").click(function(){
            if($("#fixtop #lastStep").val()=="true"|| $("#auditAjaxForm #lastStep").val() =="true"){
                if(this.checked==true){
                    var cskz1=$(this).attr("cskz1");
                    switchs(cskz1);
                }
            }
        })
    }else{
        $("#recheckAjaxForm #ts").text("请输入样本编号!");
        var div = document.getElementById('jg');
        div.setAttribute("class", "red");
    }

}


$("#keydown").bind("keydown", function (e) {
    let evt = window.event || e;
    if (evt.keyCode == 13) {
        getInfo();
    }
});
