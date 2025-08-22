function checknum(id) {
    var value=$("#"+id).val();
    if (value!=null&&value!=''){
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确格式，只能填写数字(可以保留两位小数)!");
            $("#"+id).val("")
        }
    }
}

// 页面初始化
$(function(){
    //添加日期控件
    laydate.render({
        elem: '#addSubsidys_ajaxForm #bzsj'
        ,type: 'time'
    });
    //添加日期控件
    laydate.render({
        elem: '#addSubsidys_ajaxForm #dzkssj'
        ,type: 'time'
    });
    jQuery('#paperReportsApply_formSearch .chosen-select').chosen({width: '100%'});
});
