
$(function(){
    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #kfrqstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #kfrqend'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #qrrqstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #qrrqend'
        ,theme: '#2381E9'
    });

    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #csrqstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #csrqend'
        ,theme: '#2381E9'
    });

    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #lrsjstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#task_table_formSearch #lrsjend'
        ,theme: '#2381E9'
    });
    // 所有下拉框添加choose样式
    jQuery('#task_table_formSearch .chosen-select').chosen({width: '100%'});
    var zt = $("#task_table_formSearch a[id^='zt_id_']");
    $.each(zt, function(i, n){
        var id = $(this).attr("id");
        var code = id.substring(id.lastIndexOf('_')+1,id.length);
        if(code === '00' || code === '10'){
            addTj('zt',code,'task_table_formSearch');
        }
    });
    renderingTable();
});

function renderingTable() {
    $.ajax({
        type:'post',
        url: "/systemmain/task/pageGetListTable",
        data: searchTableData(),
        dataType:'json',
        success:function(data){
            $("#task_table_formSearch #table").empty();
            var html="<div class=\"row\" style='padding: 0;margin: 0;'> <div class=\"col-md-12 col-sm-12\" style='padding: 0'>";
            html+="<div class='frame' style=\"width: 40px;\"><p>序号</p></div>";
            html+="<div class='frame' style=\"width: 140px;\"><p>任务名称</p></div>";
            html+="<div class='frame' style=\"width: 90px;\"><p>录入时间</p></div>";
            html+="<div class='frame' style=\"width: 70px;\"><p>负责人</p></div>";
            for (let i = 0; i < 3; i++) {
                if (i==1){
                    html+="<div class='frame' style=\"width: 70px;\"><p>确认人</p></div>";
                }
                if (i==2){
                    html+="<div class='frame' style=\"width: 70px;\"><p>测试人</p></div>";
                }
                html+="<div class='frame' style=\"width: 90px;\"><p>计划开始</p></div>";
                html+="<div class='frame' style=\"width: 90px;\"><p>计划结束</p></div>";
                html+="<div class='frame' style=\"width: 90px;\"><p>实际结束</p></div>";
            }
            html+="<div class='frame' style=\"width: 60px;\"><p>停留(天)</p></div>";
            html+="<div class='frame' style=\"width: 60px;\"><p>总耗(天)</p></div>";
            html+="<div class='frame' style=\"width: 60px;\"><p>状态</p></div>";
            html+="</div>";
            if (data.list && data.list.length > 0){
                html+="<div class=\"col-md-12 col-sm-12\" style='padding: 0'>";
                for (let i = 0; i < data.list.length; i++) {
                    let flag = true;
                    if (data.list[i].zt == '无需确认' || data.list[i].zt == '已完成')
                        flag = false;
                    html+="<div class='frame' style=\"width: 40px;\"><p>"+(i+1)+"</p></div>";
//                    if (data.list[i].ywmc.length>11){
//                        let mc = ""
//                        for (let j = 0; j < 8; j++) {
//                            mc += data.list[i].ywmc[j];
//                        }
//                        mc += "..."
//                        html+="<div class='frame' title='"+data.list[i].ywmc+"' style=\"width: 140px;\"><p onclick=\"showTaskInfo('"+data.list[i].rwid+"','"+data.list[i].gzid+"')\" class='ywmcColor'>"+mc+"</p></div>";
//                    }else{
//                        html+="<div class='frame' style=\"width: 140px;\"><p onclick=\"showTaskInfo('"+data.list[i].rwid+"','"+data.list[i].gzid+"')\" class='ywmcColor'>"+data.list[i].ywmc+"</p></div>";
//                    }
                    html+="<div class='frame' style=\"width: 140px;\"><p  style=\"text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick=\"showTaskInfo('"+data.list[i].rwid+"','"+data.list[i].gzid+"')\" class='ywmcColor'>"+data.list[i].ywmc+"</p></div>";
                    let jdmcList;
                    html+="<div class='frame' style=\"width: 90px;\"><p>"+data.list[i].lrsj+"</p></div>";
                    if(data.list[i].jdmc){ jdmcList = data.list[i].jdmc.split(",");}
                    html+="<div class='frame' style=\"width: 70px;";
                    if (flag && (data.list[i].dqjd == jdmcList[0] || data.list[i].dqjd == jdmcList[1])){
                        html+= "background-color: #a1abff";
                    }
                    html+="\"><p style='color: darkgreen !important;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'>"+data.list[i].fzr+"</p></div>";
                    let clryList;
                    let jhksrqList;
                    let jhjsrqList;
                    let sjjsrqList;
                    if(data.list[i].clry){ clryList = data.list[i].clry.split(",");}
                    if(data.list[i].jhksrq){ jhksrqList = data.list[i].jhksrq.split(",");}
                    if(data.list[i].jhjsrq){ jhjsrqList = data.list[i].jhjsrq.split(",");}
                    if(data.list[i].sjjsrq){ sjjsrqList = data.list[i].sjjsrq.split(",");}

                    if (jhksrqList && jhksrqList.length >=2){ html+="<div class='frame' style=\"width: 90px;\"><p>"+jhksrqList[1]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    if (jhjsrqList && jhjsrqList.length >=2){ html+="<div class='frame' style=\"width: 90px;\"><p>"+jhjsrqList[1]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    if (sjjsrqList && sjjsrqList.length >=2){ html+="<div class='frame' style=\"width: 90px;\"><p style='color: seagreen !important;'>"+sjjsrqList[1]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}

                    if (clryList && clryList.length >=3){
                        html+="<div class='frame' style=\"width: 70px;";
                        if (flag && data.list[i].dqjd == jdmcList[2]){
                            html+= "background-color: #a1abff";
                        }
                        html+="\"><p style='color: darkgreen !important;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'>"+clryList[2]+"</p></div>";
                    }else{html+="<div class='frame' style=\"width: 70px;\"><p></p></div>";}
                    if (jhksrqList && jhksrqList.length >=3){ html+="<div class='frame' style=\"width: 90px;\"><p>"+jhksrqList[2]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    if (jhjsrqList && jhjsrqList.length >=3){ html+="<div class='frame' style=\"width: 90px;\"><p>"+jhjsrqList[2]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    if (sjjsrqList && sjjsrqList.length >=3){ html+="<div class='frame' style=\"width: 90px;\"><p style='color: seagreen !important;'>"+sjjsrqList[2]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}

                    if (clryList && clryList.length >=4){
                        html+="<div class='frame' style=\"width: 70px;";
                        if (flag && data.list[i].dqjd == jdmcList[3]){
                            html+= "background-color: #a1abff";
                        }
                        html+="\"><p style='color: darkgreen !important;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'>"+clryList[3]+"</p></div>";
                    }else{html+="<div class='frame' style=\"width: 70px;\"><p></p></div>";}
                    if (jhksrqList && jhksrqList.length >=4){ html+="<div class='frame' style=\"width: 90px;\"><p>"+jhksrqList[3]+"</p></div>"; }
                     else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    if (jhjsrqList && jhjsrqList.length >=4){ html+="<div class='frame' style=\"width: 90px;\"><p>"+jhjsrqList[3]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    if (sjjsrqList && sjjsrqList.length >=4){ html+="<div class='frame' style=\"width: 90px;\"><p style='color: seagreen !important;'>"+sjjsrqList[3]+"</p></div>"; }
                        else{html+="<div class='frame' style=\"width: 90px;\"><p></p></div>";}
                    html+="<div class='frame' style=\"width: 60px;\"><p>"+data.list[i].jdhs+"</p></div>"
                    html+="<div class='frame' style=\"width: 60px;\"><p>"+data.list[i].gzhs+"</p></div>"
                    html+="<div class='frame' style=\"width: 60px;\"><p onclick=\"showHistoryInfo('"+data.list[i].gzid+"')\" class='ztColor'>"+data.list[i].zt+"</p></div>"

                }

                html+="</div>";
            }
            html+="</div>";
            $("#task_table_formSearch #table").append(html);
        },
        error: function () {
            $.error("查询数据失败，请联系管理员解决！");
        }
    });

}


function showTaskInfo(rwid,gzid) {
    var url= "/systemmain/task/viewTask" + "?gzid=" +gzid+ "&rwid=" +rwid;
    $.showDialog(url,'查看任务',viewCommonConfig);
}
var viewCommonConfig = {
    width		: "700px",
    height		: "500px",
    modalName	: "viewTaskModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                tableEnter();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() { });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() { });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function showHistoryInfo(gzid) {
    var url= "/systemmain/task/pagedataTaskHistory" + "?gzid=" +gzid;
    $.showDialog(url,'查看任务',viewCommonTaskRwqrlsConfig);
}

var viewCommonTaskRwqrlsConfig = {
    width		: "900px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var commonTask_Table_turnOff=true;
function searchMoreTable() {
    var ev=ev||event;
    if(commonTask_Table_turnOff){
        $("#task_table_formSearch #searchMore").slideDown("low");
        commonTask_Table_turnOff=false;
        this.innerHTML="基本筛选";
    }else{
        $("#task_table_formSearch #searchMore").slideUp("low");
        commonTask_Table_turnOff=true;
        this.innerHTML="高级筛选";
    }
    ev.cancelBubble=true;
}

$("#task_table_formSearch #sl_searchMore").on("click", function(ev){
    var ev=ev||event;
    if(commonTask_Table_turnOff){
        $("#task_table_formSearch #searchMore").slideDown("low");
        commonTask_Table_turnOff=false;
        this.innerHTML="基本筛选";
    }else{
        $("#task_table_formSearch #searchMore").slideUp("low");
        commonTask_Table_turnOff=true;
        this.innerHTML="高级筛选";
    }
    ev.cancelBubble=true;
});


function tableEnter(){
    $("#task_table_formSearch #searchMore").slideUp("low");
    commonTask_Table_turnOff=true;
    $("#task_table_formSearch #sl_searchMore").html("高级筛选");
    renderingTable();
}

function searchTableData(){
    var map = {
        access_token:$("#ac_tk").val(),
        zts:$("#task_table_formSearch #zt_id_tj").val(),
        kfrqstart:$("#task_table_formSearch #kfrqstart").val(),
        kfrqend:$("#task_table_formSearch #kfrqend").val(),
        qrrqstart:$("#task_table_formSearch #qrrqstart").val(),
        qrrqend:$("#task_table_formSearch #qrrqend").val(),
        csrqstart:$("#task_table_formSearch #csrqstart").val(),
        csrqend:$("#task_table_formSearch #csrqend").val(),
        lrsjstart:$("#task_table_formSearch #lrsjstart").val(),
        lrsjend:$("#task_table_formSearch #lrsjend").val(),
    };
    var cxtj = $("#task_table_formSearch #cxtj").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#task_table_formSearch #cxnr').val());
    // '0':'任务名称','1':'业务名称','2':'负责人',
    if (cxtj == "0") {
        map["ywmc"] = cxnr;
    } else if(cxtj == "1") {
        map["fzrxm"] = cxnr;
    } else if(cxtj == "2") {
        map["qrrymc"] = cxnr;
    }else if(cxtj == "3") {
        map["lrryxm"] = cxnr;
    }else if(cxtj == "4") {
        map["csrmc"] = cxnr;
    }
    // if (cxnr)
    //     map["sjflag"] = "1";
    // if ($("#task_table_formSearch #kfrqstart").val() || $("#task_table_formSearch #kfrqend").val() ||
    //     $("#task_table_formSearch #qrrqstart").val() || $("#task_table_formSearch #qrrqend").val() ||
    //     $("#task_table_formSearch #csrqstart").val() || $("#task_table_formSearch #csrqend").val())
    //     map["sjflag"] = "1";
    return map;
}