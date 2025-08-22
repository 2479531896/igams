$(document).ready(function(){
    //所有下拉框添加choose样式
    jQuery('#editChipForm .chosen-select').chosen({width: '100%'});

    //添加日期控件
    laydate.render({
        elem: '#cxkssj',
        theme: '#2381E9',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'//保留时分
    });

    //添加日期控件
    laydate.render({
        elem: '#cxjssj',
        theme: '#2381E9',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'//保留时分
    });

    //添加日期控件
    laydate.render({
        elem: '#fxkssj',
        theme: '#2381E9',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'//保留时分
    });

    //添加日期控件
    laydate.render({
        elem: '#fxjssj',
        theme: '#2381E9',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'//保留时分
    });
});