$(function(){
    laydate.render({
        elem: '#recordForm #jlsj'
        ,type: 'datetime'
        ,ready: function(date){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
        }
    });
// 所有下拉框添加choose样式
    jQuery('#recordForm .chosen-select').chosen({width: '100%'});
});