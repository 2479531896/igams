/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
    if(!/^\d+(\.\d{1,2})?$/.test(value)){
        $.error("请填写正确安全库存格式，可保留两位小数!");
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

$("#depsettingForm").keydown(function(e) {
    if(e.keyCode == 13) {
        e.preventDefault()
    }
})