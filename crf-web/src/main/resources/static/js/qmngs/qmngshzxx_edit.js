//事件绑定
function btnBind() {
}
var kgrynum = $("#kgrynum").val();

function initPage() {
	if ($('#sfsd').is(':checked'))
		$('#sfsd').bootstrapSwitch('state', true);
	else
		$('#sfsd').bootstrapSwitch('state', false);
	var chk_jsid = $('#hzxxqmngs_ajaxForm input[name="jsids"]');
	$.each(chk_jsid, function(i) {
		if ($(chk_jsid[i]).is(':checked'))
			$(chk_jsid[i]).bootstrapSwitch('state', true);
	})

}
//添加日期控件
laydate.render({
	elem: '#nrsjhz'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#rysjhz'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#jlrqOne'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #jlrqThree'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #jlrqFive'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #jlrqSeven'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #jlrqtwentyTwentyEight'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #bgsj'
	, theme: '#2381E9'
});
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #txrqid'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #cysjid'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #sjsjid'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_sjsj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_sjsj2'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_sjsj3'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_sjsj4'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_jgsj'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_jgsj2'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_jgsj3'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxqmngs_ajaxForm #ndzxxjlDtoTwentyEight_jgsj4'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm:ss'//保留时分
});
//添加时间控件
laydate.render({
	elem: '#hzxxqmngs_ajaxForm #kjywkssjid'
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
	elem: '#hzxxqmngs_ajaxForm #kjywtzsjid'
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
	elem: '#hzxxqmngs_ajaxForm #xghxykssjid'
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
	elem: '#hzxxqmngs_ajaxForm #crrtkssjid'
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
	elem: '#hzxxqmngs_ajaxForm #hxjkssjid'
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
	elem: '#hzxxqmngs_ajaxForm #ricusjid'
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
	elem: '#hzxxqmngs_ajaxForm #zgrysjid'
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
	elem: '#hzxxqmngs_ajaxForm #xghxytysjid'
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
	elem: '#hzxxqmngs_ajaxForm #crrttysjid'
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
	elem: '#hzxxqmngs_ajaxForm #hxjtysjid'
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
	elem: '#hzxxqmngs_ajaxForm #cicusjid'
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
	elem: '#hzxxqmngs_ajaxForm #zgcysjid'
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
function Addspgw(obj) {
	examine(obj);
	institutions(obj);
}

$('input[type=radio][name=yz]').change(function() {
	var myvalue = $(this).val();
	console.log(myvalue);
	var yzType = $("#" + myvalue).val();
	//由各种类型显示不同页面
	changeHide(yzType);

});
function changeHide(yzType) {
	if (yzType == 'ADULT') {
		$("#dayThreeLi").show();
		$("#dayFiveLi").show();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).hide();
  		});
		//隐藏
	}else if(yzType == 'IMMUNOSUPPRESSION'){
		$("#dayThreeLi").show();
		$("#dayFiveLi").show();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).show();
    		
  		});
	}else if(yzType == 'CHILD'){
		$("#dayThreeLi").hide();
		$("#dayFiveLi").hide();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).hide();

  		});
	}else if(yzType == 'NON_INFECTION'){
		$("#dayThreeLi").hide();
		$("#dayFiveLi").hide();
		$("input[name='sfyc1']").each(function(j,item){
    		$("#"+item.value).show();
  		});
	}
}
var viewPreModConfig = {
	width        : "900px",
	height        : "800px",
	offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
	buttons        : {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};



$(document).ready(function() {
	var sign_params=[];


	//所有下拉框添加choose样式
	jQuery('#hzxxqmngs_ajaxForm .chosen-select').chosen({ width: '100%' });
	var formate = $('#ss_formAction').val();
	console.log("修改页");
	if (formate == 'add') {
		$('#nryjbhadd').hide();
	}
	//绑定日期
    	var kgrysize = parseInt($("#hzxxqmngs_ajaxForm #kgrysize").val());
    	for(var i=0;i<kgrysize;i++){
    	//添加日期控件
        laydate.render({
        	elem: ' #hzxxqmngs_ajaxForm #kssjid'+i
        	, theme: '#2381E9'
        	, type: 'datetime'
        	, format: 'yyyy-MM-dd HH:mm'//保留时分
        });
        //添加日期控件
        laydate.render({
        	elem: ' #hzxxqmngs_ajaxForm #tzsjid'+i
        	, theme: '#2381E9'
        	, type: 'datetime'
        	, format: 'yyyy-MM-dd HH:mm'//保留时分
        });
        }
});