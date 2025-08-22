function tochangebj(){
	var getclass=$('#detectionForm #jcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionForm #jcbj').removeClass("no");
			$('#detectionForm #jcbj').addClass("yes");
			$('#detectionForm #syrqdiv').attr("style","display:block;");
			if($('#detectionForm #syrqfirst').val()==null||$('#detectionForm #syrqfirst').val()==""){
				$('#detectionForm #syrq').val(formatDate());
			}else{
				$('#detectionForm #syrq').val($('#detectionForm #syrqfirst').val());
			}
			$('#detectionForm #syrq').removeAttr("disabled");
		}else{
			$('#detectionForm #jcbj').removeClass("yes");
			$('#detectionForm #jcbj').addClass("no");
			$('#detectionForm #syrqdiv').attr("style","display:none;");
			$('#detectionForm #syrq').val(null);
			$('#detectionForm #syrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionForm #jcbj').removeClass("open");
	}
}

function tochangedbj(){
	var getclass=$('#detectionForm #djcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionForm #djcbj').removeClass("no");
			$('#detectionForm #djcbj').addClass("yes");
			$('#detectionForm #dsyrqdiv').attr("style","display:block;");
			if($('#detectionForm #dsyrqfirst').val()==null||$('#detectionForm #dsyrqfirst').val()==""){
				$('#detectionForm #dsyrq').val(formatDate());
			}else{
				$('#detectionForm #dsyrq').val($('#detectionForm #dsyrqfirst').val());
			}
			$('#detectionForm #dsyrq').removeAttr("disabled");
		}else{
			$('#detectionForm #djcbj').removeClass("yes");
			$('#detectionForm #djcbj').addClass("no");
			$('#detectionForm #dsyrqdiv').attr("style","display:none;");
			$('#detectionForm #dsyrq').val(null);
			$('#detectionForm #dsyrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionForm #djcbj').removeClass("open");
	}
}
/**
 * 自免项目检测
 * @returns
 */
function tochangezbj(){
	var getclass = $('#detectionForm #qtjcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionForm #qtjcbj').removeClass("no");
			$('#detectionForm #qtjcbj').addClass("yes");
			$('#detectionForm #qtsyrqdiv').attr("style","display:block;");
			if($('#detectionForm #qtsyrqfirst').val()==null||$('#detectionForm #qtsyrqfirst').val()==""){
				$('#detectionForm #qtsyrq').val(formatDate());
			}else{
				$('#detectionForm #qtsyrq').val($('#detectionForm #qtsyrqfirst').val());
			}
			$('#detectionForm #qtsyrq').removeAttr("disabled");
		}else{
			$('#detectionForm #qtjcbj').removeClass("yes");
			$('#detectionForm #qtjcbj').addClass("no");
			$('#detectionForm #qtsyrqdiv').attr("style","display:none;");
			$('#detectionForm #qtsyrq').val(null);
			$('#detectionForm #qtsyrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionForm #qtjcbj').removeClass("open");
	}
}

//添加日期控件
laydate.render({
   elem: '#detectionForm #syrq'
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
   elem: '#detectionForm #dsyrq'
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
   elem: '#detectionForm #qtsyrq'
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
	$('#detectionForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//基本初始化
	//初始化页面数据
	if($("#detectionForm #jcbjfirst").val()==1 && $("#detectionForm #syrqfirst").val()!=null && $("#detectionForm #syrqfirst").val()!=''){
		$('#detectionForm #jcbj').bootstrapSwitch('state', true);
		$('#detectionForm #syrqdiv').attr("style","display:block;");
		$("#detectionForm #syrq").val($("#detectionForm #syrqfirst").val());
		$('#detectionForm #syrq').removeAttr("disabled");
	}else{
		$("#detectionForm #jcbj").addClass("no");
		$('#detectionForm #jcbj').bootstrapSwitch('state', false);
		$("#detectionForm #syrq").val(null);
		$('#detectionForm #syrq').attr("disabled","disabled");
	}
	if($("#detectionForm #djcbjfirst").val()==1 && $("#detectionForm #dsyrqfirst").val()!=null && $("#detectionForm #dsyrqfirst").val()!=''){
		$('#detectionForm #djcbj').bootstrapSwitch('state', true);
		$('#detectionForm #dsyrqdiv').attr("style","display:block;");
		$("#detectionForm #dsyrq").val($("#detectionForm #dsyrqfirst").val());
		$('#detectionForm #dsyrq').removeAttr("disabled");
	}else{
		$("#detectionForm #djcbj").addClass("no");
		$('#detectionForm #djcbj').bootstrapSwitch('state', false);
		$("#detectionForm #dsyrq").val(null);
		$('#detectionForm #dsyrq').attr("disabled","disabled");
	}
	if($("#detectionForm #qtjcbjfirst").val()==1 && $("#detectionForm #qtsyrqfirst").val()!=null && $("#qtsyrqfirst").val()!=''){
		$('#detectionForm #qtjcbj').bootstrapSwitch('state', true);
		$('#detectionForm #qtsyrqdiv').attr("style","display:block;");
		$("#detectionForm #qtsyrq").val($("#detectionForm #qtsyrqfirst").val());
		$('#detectionForm #qtsyrq').removeAttr("disabled");
	}else{
		$("#detectionForm #qtjcbj").addClass("no");
		$('#detectionForm #qtjcbj').bootstrapSwitch('state', false);
		$("#detectionForm #qtsyrq").val(null);
		$('#detectionForm #qtsyrq').attr("disabled","disabled");
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