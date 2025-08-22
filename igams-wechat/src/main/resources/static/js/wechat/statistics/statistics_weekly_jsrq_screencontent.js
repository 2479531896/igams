$(function(){
    laydate.render({
        elem: '#weeklyStatis_jsrq_screencontent #lrsjStart'
        ,theme: '#2381E9',
        type: 'month',
        trigger: 'click'
    });
    //添加日期控件
    laydate.render({
        elem: '#weeklyStatis_jsrq_screencontent #lrsjEnd'
        ,theme: '#2381E9',
        type: 'month',
        trigger: 'click'
    });

});