// 初始化
$(function () {
    //添加日期控件
    laydate.render({
        elem: '#modTrainRecordDetailForm #qwwcsj'
        ,theme: '#2381E9'
    });
    // 所有下拉框添加choose样式
    jQuery('#modTrainRecordDetailForm .chosen-select').chosen({width: '100%'});
})