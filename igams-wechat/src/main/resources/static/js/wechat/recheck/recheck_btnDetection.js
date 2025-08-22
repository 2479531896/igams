function tochangebj(){
	var getclass=$('#detectionFjsqForm #jcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionFjsqForm #jcbj').removeClass("no");
			$('#detectionFjsqForm #jcbj').addClass("yes");
			$('#detectionFjsqForm #syrqdiv').attr("style","display:block;");
			if($('#detectionFjsqForm #syrqFj').val()==null||$('#detectionFjsqForm #syrqFj').val()==""){
				$('#detectionFjsqForm #syrq').val(formatDate());
			}else{
				$('#detectionFjsqForm #syrq').val($('#detectionFjsqForm #syrqFj').val());
			}
			$('#detectionFjsqForm #syrq').removeAttr("disabled");
		}else{
			$('#detectionFjsqForm #jcbj').removeClass("yes");
			$('#detectionFjsqForm #jcbj').addClass("no");
			$('#detectionFjsqForm #syrqdiv').attr("style","display:none;");
			$('#detectionFjsqForm #syrq').val(null);
			$('#detectionFjsqForm #syrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionFjsqForm #jcbj').removeClass("open");
	}
}

function tochangedbj(){
	var getclass=$('#detectionFjsqForm #djcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionFjsqForm #djcbj').removeClass("no");
			$('#detectionFjsqForm #djcbj').addClass("yes");
			$('#detectionFjsqForm #dsyrqdiv').attr("style","display:block;");
			if($('#detectionFjsqForm #dsyrqFj').val()==null||$('#detectionFjsqForm #dsyrqFj').val()==""){
				$('#detectionFjsqForm #dsyrq').val(formatDate());
			}else{
				$('#detectionFjsqForm #dsyrq').val($('#detectionFjsqForm #dsyrqFj').val());
			}
			$('#detectionFjsqForm #dsyrq').removeAttr("disabled");
		}else{
			$('#detectionFjsqForm #djcbj').removeClass("yes");
			$('#detectionFjsqForm #djcbj').addClass("no");
			$('#detectionFjsqForm #dsyrqdiv').attr("style","display:none;");
			$('#detectionFjsqForm #dsyrq').val(null);
			$('#detectionFjsqForm #dsyrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionFjsqForm #djcbj').removeClass("open");
	}
}
/**
 * 自免项目检测
 * @returns
 */
function tochangezbj(){
	var getclass = $('#detectionFjsqForm #qtjcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionFjsqForm #qtjcbj').removeClass("no");
			$('#detectionFjsqForm #qtjcbj').addClass("yes");
			$('#detectionFjsqForm #qtsyrqdiv').attr("style","display:block;");
			if($('#detectionFjsqForm #qtsyrqFj').val()==null||$('#detectionFjsqForm #qtsyrqFj').val()==""){
				$('#detectionFjsqForm #qtsyrq').val(formatDate());
			}else{
				$('#detectionFjsqForm #qtsyrq').val($('#detectionFjsqForm #qtsyrqFj').val());
			}
			$('#detectionFjsqForm #qtsyrq').removeAttr("disabled");
		}else{
			$('#detectionFjsqForm #qtjcbj').removeClass("yes");
			$('#detectionFjsqForm #qtjcbj').addClass("no");
			$('#detectionFjsqForm #qtsyrqdiv').attr("style","display:none;");
			$('#detectionFjsqForm #qtsyrq').val(null);
			$('#detectionFjsqForm #qtsyrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionFjsqForm #qtjcbj').removeClass("open");
	}
}

//添加日期控件
laydate.render({
   elem: '#detectionFjsqForm #syrq'
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
laydate.render({
   elem: '#detectionFjsqForm #dsyrq'
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
laydate.render({
   elem: '#detectionFjsqForm #qtsyrq'
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

$(document).ready(function(){
	$('#detectionFjsqForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//基本初始化
	//初始化页面数据
	if($("#detectionFjsqForm #jcbjFj").val()==1 && $("#detectionFjsqForm #syrqFj").val()!=null && $("#detectionFjsqForm #syrqFj").val()!=''){
		$('#detectionFjsqForm #jcbj').bootstrapSwitch('state', true);
		$('#detectionFjsqForm #syrqdiv').attr("style","display:block;");
		$("#detectionFjsqForm #syrq").val($("#detectionFjsqForm #syrqFj").val());
		$('#detectionFjsqForm #syrq').removeAttr("disabled");
	}else{
		$("#detectionFjsqForm #jcbj").addClass("no");
		$('#detectionFjsqForm #jcbj').bootstrapSwitch('state', false);
		$("#detectionFjsqForm #syrq").val(null);
		$('#detectionFjsqForm #syrq').attr("disabled","disabled");
	}
	if($("#detectionFjsqForm #djcbjFj").val()==1 && $("#detectionFjsqForm #dsyrqFj").val()!=null && $("#detectionFjsqForm #dsyrqFj").val()!=''){
		$('#detectionFjsqForm #djcbj').bootstrapSwitch('state', true);
		$('#detectionFjsqForm #dsyrqdiv').attr("style","display:block;");
		$("#detectionFjsqForm #dsyrq").val($("#detectionFjsqForm #dsyrqFj").val());
		$('#detectionFjsqForm #dsyrq').removeAttr("disabled");
	}else{
		$("#detectionFjsqForm #djcbj").addClass("no");
		$('#detectionFjsqForm #djcbj').bootstrapSwitch('state', false);
		$("#detectionFjsqForm #dsyrq").val(null);
		$('#detectionFjsqForm #dsyrq').attr("disabled","disabled");
	}
	if($("#detectionFjsqForm #qtjcbjFj").val()==1 && $("#detectionFjsqForm #qtsyrqFj").val()!=null && $("#qtsyrqFj").val()!=''){
		$('#detectionFjsqForm #qtjcbj').bootstrapSwitch('state', true);
		$('#detectionFjsqForm #qtsyrqdiv').attr("style","display:block;");
		$("#detectionFjsqForm #qtsyrq").val($("#detectionFjsqForm #qtsyrqFj").val());
		$('#detectionFjsqForm #qtsyrq').removeAttr("disabled");
	}else{
		$("#detectionFjsqForm #qtjcbj").addClass("no");
		$('#detectionFjsqForm #qtjcbj').bootstrapSwitch('state', false);
		$("#detectionFjsqForm #qtsyrq").val(null);
		$('#detectionFjsqForm #qtsyrq').attr("disabled","disabled");
	}
});

function formatDate(){
	var myDate=new Date;
	var y = myDate.getFullYear();

	var m = myDate.getMonth() + 1;

	m = m < 10 ? ("0" + m) : m;

	var d = myDate.getDate();

	d = d < 10 ? ("0" + d) : d;

	var h = myDate.getHours();

	h=h < 10 ? ("0" + h) : h;

	var minute = myDate.getMinutes();

	minute = minute < 10 ? ("0" + minute) : minute;

	var second=myDate.getSeconds();

	second=second < 10 ? ("0" + second) : second;

	return y + "-" + m + "-" + d+" "+h+":"+minute+":"+second;

}