
function veriPurchase_queryByWlbm(wlid){
    var url=$("#contractopenclose_Form #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',WlConfig);
}
var WlConfig = {
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
function queryByqgid(qgid){
    var url=$("#contractopenclose_Form #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'请购信息',viewQgConfig);
}

var viewQgConfig={
    width		: "1500px",
    modalName	:"viewQgModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
function openHt(htmxid){
    $.confirm('您确定开启合同行吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $('#contractopenclose_Form #urlPrefix').val()+"/contract/contract/pagedataOpenContract"
            jQuery.post(url,{"htmxid":htmxid,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            var zHtml = "<span id='"+htmxid+"' class='btn btn-danger' onclick=\"closeHt('"+htmxid+"')\">关闭</span>";
                            $('#contractopenclose_Form #'+htmxid).remove();
                            $('#contractopenclose_Form #ht_'+htmxid).append(zHtml);
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);

            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}
function closeHt(htmxid){
    $.confirm('您确定关闭合同行吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $('#contractopenclose_Form #urlPrefix').val()+"/contract/contract/pagedataCloseContract"
            jQuery.post(url,{"htmxid":htmxid,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            var zHtml = "<span id='"+htmxid+"' class='btn btn-info' onclick=\"openHt('"+htmxid+"')\">开启</span>";
                            $('#contractopenclose_Form #'+htmxid).remove();
                            $('#contractopenclose_Form #ht_'+htmxid).append(zHtml);
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);

            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}