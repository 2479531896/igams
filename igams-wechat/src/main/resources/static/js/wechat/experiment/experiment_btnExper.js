function tochangebj(){
	var getclass=$('#detectionFormExper #jcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionFormExper #jcbj').removeClass("no");
			$('#detectionFormExper #jcbj').addClass("yes");
			$('#detectionFormExper #syrqdivE').attr("style","display:block;");
			if($('#detectionFormExper #syrqFirst').val()==null||$('#detectionFormExper #syrqFirst').val()==""){
				$('#detectionFormExper #syrq').val(formatDate());
			}else{
				$('#detectionFormExper #syrq').val($('#detectionFormExper #syrqFirst').val());
			}
			$('#detectionFormExper #syrq').removeAttr("disabled");
		}else{
			$('#detectionFormExper #jcbj').removeClass("yes");
			$('#detectionFormExper #jcbj').addClass("no");
			$('#detectionFormExper #syrqdivE').attr("style","display:none;");
			$('#detectionFormExper #syrq').val(null);
			$('#detectionFormExper #syrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionFormExper #jcbj').removeClass("open");
	}
}

function tochangedbj(){
	var getclass=$('#detectionFormExper #djcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionFormExper #djcbj').removeClass("no");
			$('#detectionFormExper #djcbj').addClass("yes");
			$('#detectionFormExper #dsyrqdivE').attr("style","display:block;");
			if($('#detectionFormExper #dsyrqFirst').val()==null||$('#detectionFormExper #dsyrqFirst').val()==""){
				$('#detectionFormExper #dsyrq').val(formatDate());
			}else{
				$('#detectionFormExper #dsyrq').val($('#detectionFormExper #dsyrqFirst').val());
			}
			$('#detectionFormExper #dsyrq').removeAttr("disabled");
		}else{
			$('#detectionFormExper #djcbj').removeClass("yes");
			$('#detectionFormExper #djcbj').addClass("no");
			$('#detectionFormExper #dsyrqdivE').attr("style","display:none;");
			$('#detectionFormExper #dsyrq').val(null);
			$('#detectionFormExper #dsyrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionFormExper #djcbj').removeClass("open");
	}
}
/**
 * 自免项目检测
 * @returns
 */
function tochangezbj(){
	var getclass = $('#detectionFormExper #qtjcbj').attr("class");
	if(getclass.indexOf("open")==-1){//过滤掉点击实验按钮触发这个事件
		if(getclass.indexOf("no")!=-1){
			$('#detectionFormExper #qtjcbj').removeClass("no");
			$('#detectionFormExper #qtjcbj').addClass("yes");
			$('#detectionFormExper #qtsyrqdivE').attr("style","display:block;");
			if($('#detectionFormExper #qtsyrqFirst').val()==null||$('#detectionFormExper #qtsyrqFirst').val()==""){
				$('#detectionFormExper #qtsyrq').val(formatDate());
			}else{
				$('#detectionFormExper #qtsyrq').val($('#detectionFormExper #qtsyrqFirst').val());
			}
			$('#detectionFormExper #qtsyrq').removeAttr("disabled");
		}else{
			$('#detectionFormExper #qtjcbj').removeClass("yes");
			$('#detectionFormExper #qtjcbj').addClass("no");
			$('#detectionFormExper #qtsyrqdivE').attr("style","display:none;");
			$('#detectionFormExper #qtsyrq').val(null);
			$('#detectionFormExper #qtsyrq').attr("disabled","disabled");
		}
	}else{
		$('#detectionFormExper #qtjcbj').removeClass("open");
	}
}

//添加日期控件
laydate.render({
   elem: '#detectionFormExper #syrq'
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
   elem: '#detectionFormExper #dsyrq'
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
   elem: '#detectionFormExper #qtsyrq'
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
	$('#detectionFormExper input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//基本初始化
	//初始化页面数据
	if($("#detectionFormExper #jcbjFirst").val()==1 && $("#detectionFormExper #syrqFirst").val()!=null && $("#detectionFormExper #syrqFirst").val()!=''){
		$('#detectionFormExper #jcbj').bootstrapSwitch('state', true);
		$('#detectionFormExper #syrqdivE').attr("style","display:block;");
		$("#detectionFormExper #syrq").val($("#detectionFormExper #syrqFirst").val());
		$('#detectionFormExper #syrq').removeAttr("disabled");
	}else{
		$("#detectionFormExper #jcbj").addClass("no");
		$('#detectionFormExper #jcbj').bootstrapSwitch('state', false);
		$("#detectionFormExper #syrq").val(null);
		$('#detectionFormExper #syrq').attr("disabled","disabled");
	}
	if($("#detectionFormExper #djcbjFirst").val()==1 && $("#detectionFormExper #dsyrqFirst").val()!=null && $("#detectionFormExper #dsyrqFirst").val()!=''){
		$('#detectionFormExper #djcbj').bootstrapSwitch('state', true);
		$('#detectionFormExper #dsyrqdivE').attr("style","display:block;");
		$("#detectionFormExper #dsyrq").val($("#detectionFormExper #dsyrqFirst").val());
		$('#detectionFormExper #dsyrq').removeAttr("disabled");
	}else{
		$("#detectionFormExper #djcbj").addClass("no");
		$('#detectionFormExper #djcbj').bootstrapSwitch('state', false);
		$("#detectionFormExper #dsyrq").val(null);
		$('#detectionFormExper #dsyrq').attr("disabled","disabled");
	}
	if($("#detectionFormExper #qtjcbjFirst").val()==1 && $("#detectionFormExper #qtsyrqFirst").val()!=null && $("#qtsyrqFirst").val()!=''){
		$('#detectionFormExper #qtjcbj').bootstrapSwitch('state', true);
		$('#detectionFormExper #qtsyrqdivE').attr("style","display:block;");
		$("#detectionFormExper #qtsyrq").val($("#detectionFormExper #qtsyrqFirst").val());
		$('#detectionFormExper #qtsyrq').removeAttr("disabled");
	}else{
		$("#detectionFormExper #qtjcbj").addClass("no");
		$('#detectionFormExper #qtjcbj').bootstrapSwitch('state', false);
		$("#detectionFormExper #qtsyrq").val(null);
		$('#detectionFormExper #qtsyrq').attr("disabled","disabled");
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