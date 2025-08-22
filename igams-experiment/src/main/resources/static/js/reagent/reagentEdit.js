$(document).ready(function(){
    laydate.render({
    	   elem: '#editReagentForm #sjrq'
    	   ,type: 'datetime'
            ,trigger: 'click'
    	   ,ready: function(date){
    			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
    				var myDate = new Date(); //实例一个时间对象；
    				this.dateTime.hours=myDate.getHours();
    				this.dateTime.minutes=myDate.getMinutes();
    	        	this.dateTime.seconds=myDate.getSeconds();
    			}
    		}
    	});
	jQuery('#editReagentForm .chosen-select').chosen({width: '100%'});
});