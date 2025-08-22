$(function(){
    jQuery('#administrationStockAllocationForm .chosen-select').chosen({width: '100%'});
})
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("dbsl", function (value, element){
    if(!value){
        $.error("请填写数量!");
        return false;
    }else{
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确数量格式，可保留两位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

