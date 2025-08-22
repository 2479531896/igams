function reportExport() {
    var url= "/finance/finance/pagedataFinanceExport"
    $.showDialog(url,'费用报表导出',reportExportConfig);
}
function reportExport_yj() {
    var url= "/finance/finance/pagedataFinanceMedlabExport"
    $.showDialog(url,'医检所收入对比报表导出',reporMedlabtExportConfig);
}
function reportExport_srcbb() {
    var url= "/finance/finance/pagedataRevenueCostExport"
    $.showDialog(url,'收入成本表',revenueCostExportConfig);
}
var reporMedlabtExportConfig = {
    width		: "800px",
    modalName	: "financeModal",
    formName	: "financeExport_meadlab_formSearch",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
            success : {
                label : "确 定",
                className : "btn-primary",
                callback : function() {
                    var $this = this;
                    var opts = $this["options"]||{};
                    $("#financeExport_meadlab_formSearch input[name='access_token']").val($("#ac_tk").val());
                     $.ajax({
                                        async:false,
                                        type: "POST",
                                        url: "/finance/finance/pagedataExportMedlabFinance",
                                        data: {"databaseName": $("#financeExport_meadlab_formSearch #databaseName").val(),"dbyear":$("#financeExport_meadlab_formSearch #dbyear").val(),"ccodes":$("#financeExport_meadlab_formSearch #ccodes").val(),"ztcsdm":$("#financeExport_meadlab_formSearch #ztcsdm").val(), "access_token": $("#ac_tk").val()},
                                        dataType: "json",
                                        success: function (data) {
                                        console.log(data)
                                            if(data.status=='error'){
                                                $.error(data.message)
                                                return false;
                                            }

                                            if(data.wjid){
                                                //创建objectNode
                                                var cardiv =document.createElement("div");
                                                cardiv.id="cardiv";
                                                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' +data.totalCount  + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                                                cardiv.innerHTML=s_str;
                                                //将上面创建的P元素加入到BODY的尾部
                                                document.body.appendChild(cardiv);

                                                setTimeout("financeCheckdownLoadStatus('"+ data.wjid + "',1000)",1000);

                                                //绑定导出取消按钮事件
                                                $("#exportCancel").click(function(){
                                                    //先移除导出提示，然后请求后台
                                                    if($("#cardiv").length>0) $("#cardiv").remove();
                                                    $.ajax({
                                                        type : "POST",
                                                        url : $('#financeExport_formSearch #urlPrefix').val() + "/common/export/commCancelExport",
                                                        data : {"wjid" : data.wjid+"","access_token":$("#ac_tk").val()},
                                                        dataType : "json",
                                                        success:function(data){
                                                            if(data != null && data.result==false){
                                                                if(data.msg && data.msg!="")
                                                                    $.error(data.msg);
                                                            }
                                                        }
                                                    });
                                                });

                                            }
                                        }
                     })

                    return true;
                  }
                },
                cancel : {
                    label : "关 闭",
                    className : "btn-default"
                }
    }
}
var reportExportConfig = {
    width		: "800px",
    modalName	: "financeModal",
    formName	: "financeExport_formSearch",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#financeExport_formSearch").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                if($("#financeExport_formSearch #kssjstart").val()==""||$("#financeExport_formSearch #kssjstart").val()==null){
                    $.alert("请输入开始时间！");
                    return false;
                }else if($("#financeExport_formSearch #jssjend").val()==""||$("#financeExport_formSearch #jssjend").val()==null){
                    $.alert("请输入结束时间！");
                    return false;
                }else if($("#financeExport_formSearch input:checkbox[name='ztcsdms']:checked").length==0){
                    $.alert("请选择账套！");
                    return false;
                }else if($("#financeExport_formSearch input:checkbox[name='bbcsdms']:checked").length==0){
                    $.alert("请选择报表！");
                    return false;
                }
                var ztcsdms=[];
                $('input[name="ztcsdms"]:checked').each(function () {
                    ztcsdms.push($(this).val());
                });
                $("#financeExport_formSearch #ztcsdms").val(ztcsdms);
                var bbcsdms=[];
                $('input[name="bbcsdms"]:checked').each(function () {
                    bbcsdms.push($(this).val());
                });
                $("#financeExport_formSearch #bbcsdms").val(bbcsdms);
                if ( $("#financeExport_formSearch #kssjstart").val()>$("#financeExport_formSearch #jssjend").val()){
                    $.error("开始时间不允许大于结束时间！")
                    return;
                }
                var months=[];
                var years=[];
                var msg = "";
                $.ajax({
                    async:false,
                    type: "POST",
                    url: "/finance/finance/pagedataGetAllDates",
                    data: {"startDate": $("#financeExport_formSearch #kssjstart").val(),"endDate":$("#financeExport_formSearch #jssjend").val(), "access_token": $("#ac_tk").val()},
                    dataType: "json",
                    success: function (data) {
                        months=data.months
                        years=data.years
                        $("#financeExport_formSearch input[name='access_token']").val($("#ac_tk").val());
                        for (var i = 0; i <ztcsdms.length ; i++) {
                            for (var j = 0; j <bbcsdms.length ; j++) {
                                if (bbcsdms[j].indexOf("XSFY")!=-1){
                                    for (var k = 0; k <months.length ; k++) {
                                        msg += expFinace(months[k],ztcsdms[i],bbcsdms[j]);
                                    }
                                }
                            }
                        }
                        for (var j = 0; j < bbcsdms.length; j++) {
                            if (bbcsdms[j].indexOf("CBFY")!=-1||bbcsdms[j].indexOf("BMFY")!=-1){
                                for (var k = 0; k <years.length ; k++) {
                                    msg += expFinace(years[k],ztcsdms.join(","),bbcsdms[j]);
                                }
                            }
                        }
                }});
                if (msg!=''){
                    $.error(msg)
                }
                return true;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var revenueCostExportConfig = {
    width		: "800px",
    modalName	: "financeExport_RevenueCostModal",
    formName	: "financeExport_RevenueCost_formSearch",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#financeExport_RevenueCost_formSearch").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
               if($("#financeExport_RevenueCost_formSearch input:checkbox[name='ztcsdms']:checked").length==0){
                    $.alert("请选择账套！");
                    return false;
                }
                var ztcsdms=[];
                $('#financeExport_RevenueCost_formSearch input[name="ztcsdms"]:checked').each(function () {
                    ztcsdms.push($(this).val());
                });
                var year = $("#financeExport_RevenueCost_formSearch #year").val()
                var msg = "";
                for (var i = 0; i<ztcsdms.length; i++){
                    msg += revenueCostExport(ztcsdms[i],year);
                }
                if (msg!=''){
                    $.error(msg)
                }
                return true;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function revenueCostExport(ztcsdm,year){
    var msg = "";
    $.ajax({
        async:false,
        type:'post',
        url:"/finance/finance/pagedataExportRevenueCostExport",
        cache: false,
        data: {"year":year,"ztcsdm":ztcsdm,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(resp){
            if(resp.wjid){
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + resp.totalCount + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                setTimeout("financeCheckdownLoadStatus('"+ resp.wjid + "',1000)",1000);

                //绑定导出取消按钮事件
                $("#exportCancel").click(function(){
                    //先移除导出提示，然后请求后台
                    if($("#cardiv").length>0) $("#cardiv").remove();
                    $.ajax({
                        type : "POST",
                        url : $('#financeExport_formSearch #urlPrefix').val() + "/common/export/commCancelExport",
                        data : {"wjid" : resp.wjid+"","access_token":$("#ac_tk").val()},
                        dataType : "json",
                        success:function(data){
                            if(data != null && data.result==false){
                                if(data.msg && data.msg!="")
                                $.error(data.msg);
                            }
                        }
                    });
                });

            }
            if (!resp.msg){
                resp.msg = '';
            }
            msg = resp.msg;
        }
    });
    return msg;
}
function expFinace(rq,ztcsdm,bbcsdm){
    var msg = "";
    $.ajax({
        async:false,
        type:'post',
        url:"/finance/finance/pagedataExportFinance",
        cache: false,
        data: {"rq":rq,"ztcsdm":ztcsdm,"bbcsdm":bbcsdm,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(resp){
            if(resp.wjid){
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + resp.totalCount + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //将上面创建的P元素加入到BODY的尾部
                document.body.appendChild(cardiv);

                setTimeout("financeCheckdownLoadStatus('"+ resp.wjid + "',1000)",1000);

                //绑定导出取消按钮事件
                $("#exportCancel").click(function(){
                    //先移除导出提示，然后请求后台
                    if($("#cardiv").length>0) $("#cardiv").remove();
                    $.ajax({
                        type : "POST",
                        url : $('#financeExport_formSearch #urlPrefix').val() + "/common/export/commCancelExport",
                        data : {"wjid" : resp.wjid+"","access_token":$("#ac_tk").val()},
                        dataType : "json",
                        success:function(data){
                            if(data != null && data.result==false){
                                if(data.msg && data.msg!="")
                                    $.error(data.msg);
                            }
                        }
                    });
                });

            }
            if (!resp.msg){
                resp.msg = '';
            }
            msg = resp.msg;
        }
    });
    return msg;
}
var financeCheckdownLoadStatus = function(wjid,intervalTime){
    $.ajax({
        type : "POST",
        url : "/common/export/commCheckExport",
        data : {"wjid" : wjid+"","access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.cancel){//取消导出则直接return
                return;
            }
            if(data.result==false){
                if(data.msg && data.msg!=""){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if(intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if($("#exportCount")){$("#exportCount").html(data.currentCount)}
                    setTimeout("financeCheckdownLoadStatus('"+wjid+"',"+ intervalTime +")",intervalTime)
                }
            }else{
                if(data.msg && data.msg!=""){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if($("#cardiv")) $("#cardiv").remove();
                    window.open("/common/export/commDownloadExport?wjid="+wjid + "&access_token="+$("#ac_tk").val());
                }
            }
        }
    });
}