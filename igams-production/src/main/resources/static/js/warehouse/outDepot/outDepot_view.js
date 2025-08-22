function queryBywlid(wlid){
    var url=$("#ajaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}

var viewWlConfig = {
    width		: "1400px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function queryByLlid(llid){
    var url=$("#ajaxForm #urlPrefix").val()+"/storehouse/receiveMateriel/viewReceiveMateriel?llid="+llid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'领料信息',viewLlConfig);
}

var viewLlConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};