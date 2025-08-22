
$(function(){


    //添加时间控件
    laydate.render({
        elem: '#ajaxForm #rysj'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    //添加时间控件
    laydate.render({
        elem: '#ajaxForm #cysj'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    //添加时间控件
    laydate.render({
        elem: '#ajaxForm #tbsj'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    //添加时间控件
    laydate.render({
        elem: '#ajaxForm #jnsjjcsj'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });


});