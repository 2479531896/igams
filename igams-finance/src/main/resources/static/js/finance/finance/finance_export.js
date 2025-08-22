$(function(){
    //添加日期控件
    laydate.render({
        elem: '#financeExport_formSearch #jssjend'
        ,theme: '#2381E9',
        type: 'month',
        trigger: 'click'
    });
    //添加日期控件
    laydate.render({
        elem: '#financeExport_formSearch #kssjstart'
        ,theme: '#2381E9',
        type: 'month',
        trigger: 'click'
    });
});