var Check_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //添加日期控件
        laydate.render({
            elem: '#startdate'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#enddate'
            ,theme: '#2381E9'
        });
    };
    return oInit;
};

$(function(){

    var oButtonInit = new Check_ButtonInit();
    oButtonInit.Init();
    jQuery('#updateCheckForm .chosen-select').chosen({width: '80%'});
});