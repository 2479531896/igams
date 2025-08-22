var flag = $("#paymentreceivedForm #flag").val()
function EditMoneyHos(fywid,ywid,ywlx,djrq,xsid,dysyje,hkzq){
    var url=$("#paymentreceivedForm #urlPrefix").val()+"/storehouse/sale/pagedataMoneyHos?fywid="+fywid+"&ywid="+ywid+"&ywlx="+ywlx+"&djrq="+djrq+"&xsid="+xsid+"&dysyje="+dysyje+"&hkzq="+hkzq+"&flag="+flag+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到款记录',moneyHistorysFormConfig);
}

//到款记录
var moneyHistorysFormConfig = {
    width		: "1000px",
    modalName	: "MoneyHosModal",
    formName	: "MoneyHosForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"dkjlid":'',"dky":'',"dkje":'',"lrsj":'',"lb":'',"ywlx":'',"ywid":'',"fywid":''};
                        sz.dkjlid = t_map.rows[i].dkjlid;
                        sz.dky = t_map.rows[i].dky;
                        sz.dkje = t_map.rows[i].dkje;
                        sz.lrsj = t_map.rows[i].lrsj;
                        sz.lb = t_map.rows[i].lb;
                        sz.ywlx = t_map.rows[i].ywlx;
                        sz.ywid = t_map.rows[i].ywid;
                        sz.fywid = t_map.rows[i].fywid;
                        json.push(sz);
                    }
                }
                $("#MoneyHosForm #dkjlmx_json").val(JSON.stringify(json));
                $("#MoneyHosForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"MoneyHosForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                $("#paymentreceivedForm").load($("#paymentreceivedForm #urlPrefix").val()+"/storehouse/sale/paymentreceivedSale?xsid="+$("#paymentreceivedForm #xsid").val());
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                        });
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
$(document).ready(function(){
    //所有下拉框添加choose样式
    jQuery('#paymentreceivedForm .chosen-select').chosen({width: '100%'});
});