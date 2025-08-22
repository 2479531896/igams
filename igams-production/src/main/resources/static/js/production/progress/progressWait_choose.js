

var produceConfig = {
    width		: "1600px",
    modalName	:"produceConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#progress_produce_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].xlh==null || t_map.rows[i].xlh==''){
                            $.alert("序列号不能为空！");
                            return false;
                        }

                        var sz = {"xqjhmxid":'',"cpxqid":'',"xlh":'',"wlid":'',"scsl":'',"yjwcsj":''};
                        sz.xqjhmxid = t_map.rows[i].xqjhmxid;
                        sz.cpxqid = t_map.rows[i].xqjhid;
                        sz.xlh = t_map.rows[i].xlh;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = t_map.rows[i].scsl;
                        sz.yjwcsj = t_map.rows[i].yjwcsj;
                        json.push(sz);
                    }
                    $("#progress_produce_ajaxForm #sczlmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("指令明细明细信息不能为空！");
                    return false;
                }
                $("#progress_produce_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "progress_produce_ajaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);

                            }
                            searchWaitProgressResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function goProduce(csid,csdm){
    var url= ""
    if ($("#progress_choose_ajaxForm #xqjhmxid").val()) {
        url = $("#progress_choose_ajaxForm #urlPrefix").val() + "/progress/progress/pagedataProduce?xqjhmxid=" + $("#progress_choose_ajaxForm #xqjhmxid").val()+ "&sfsc=" + $("#progress_choose_ajaxForm #sfsc").val() + "&access_token=" + $("#ac_tk").val() + "&cplxmc=" + csdm + "&cplx=" + csid;
        $.showDialog(url,csdm,produceConfig);
    }else{
        url = $("#progress_choose_ajaxForm #urlPrefix").val() + "/progress/produce/pagedataProduceAdd?access_token=" + $("#ac_tk").val() + "&cplxmc=" + csdm + "&cplx=" + csid;
        $.closeModal("addProduceConfigModal");
        $.showDialog(url,csdm,produceAddConfig);
    }

}
produceAddConfig = {
    width		: "1600px",
    modalName	:"produceAddConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提  交",
            className : "btn-primary",
            callback : function() {
                if(!$("#produce_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].xlh==null || t_map.rows[i].xlh==''){
                            $.alert("序列号不能为空！");
                            return false;
                        }
                        var sz = {"xlh":'',"wlid":'',"scsl":'',"yjwcsj":''};
                        sz.xlh = t_map.rows[i].xlh;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = t_map.rows[i].scsl;
                        sz.yjwcsj = t_map.rows[i].yjwcsj;
                        json.push(sz);
                    }
                    $("#produce_ajaxForm #sczlmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("指令明细明细信息不能为空！");
                    return false;
                }
                $("#produce_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"produce_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        // let cplx = $("#produce_ajaxForm #cplxmc").val();
                        // var auditType = "";
                        // if (cplx == "YQ"){
                        //     auditType = $("#produce_formSearch #YQ_auditType").val();
                        // }else{
                        //     auditType = $("#produce_formSearch #SJ_auditType").val();
                        // }
                        var auditType = $("#produce_ajaxForm #shlx").val();
                        var progress_params=[];
                        progress_params.prefix=$('#produce_formSearch #urlPrefix').val();
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        showAuditFlowDialog(auditType,  responseText["ywids"],function(){
                            searchProduceResult();
                        },null,progress_params);
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#produce_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].xlh==null || t_map.rows[i].xlh==''){
                            $.alert("序列号不能为空！");
                            return false;
                        }

                        var sz = {"xlh":'',"wlid":'',"scsl":'',"yjwcsj":''};
                        sz.xlh = t_map.rows[i].xlh;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = t_map.rows[i].scsl;
                        sz.yjwcsj = t_map.rows[i].yjwcsj;
                        json.push(sz);
                    }
                    $("#produce_ajaxForm #sczlmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("指令明细明细信息不能为空！");
                    return false;
                }
                $("#produce_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "produce_ajaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchProduceResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};