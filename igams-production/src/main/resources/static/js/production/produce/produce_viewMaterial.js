function queryByXqdh(cpxqid){
    var url=$("#produceACMaterial_formSearch #urlPrefix").val()+"/progress/progress/pagedataProgress?cpxqid="+cpxqid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'需求计划',viewProduceXqdhMaterialConfig);
}

var viewProduceXqdhMaterialConfig = {
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