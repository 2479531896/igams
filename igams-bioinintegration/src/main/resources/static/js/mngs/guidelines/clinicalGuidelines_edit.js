$(function () {
    //添加日期控件
    laydate.render({
        elem: '#editClinicalGuidelinesForm #fbsj'
        ,theme: '#2381E9'
    });

    //所有下拉框添加choose样式
    jQuery('#editClinicalGuidelinesForm .chosen-select').chosen({width: '100%'});
});