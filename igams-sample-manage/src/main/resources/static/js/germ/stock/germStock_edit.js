$(document).ready(function(){
    //所有下拉框添加choose样式
    jQuery('#germEdit_Form .chosen-select').chosen({width: '100%'});
});
/**
 * 验证格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
function checknum(name) {
    var value=$("#"+name).val();
    var result=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
    if(!result.test(value)){
        $.error("请填写正确格式，可保留两位小数!");
        $("#"+name).val("");
    }
}