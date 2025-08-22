//事件绑定
function btnBind() {
}

//打开文件上传界面
function editfile(divName,btnName){
	$("#hzxxnv_ajaxForm"+"  #"+btnName).hide();
	$("#hzxxnv_ajaxForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
	$("#hzxxnv_ajaxForm"+"  #"+btnName).show();
	$("#hzxxnv_ajaxForm"+"  #"+divName).hide();
}
function initPage() {
	if ($('#sfsd').is(':checked'))
		$('#sfsd').bootstrapSwitch('state', true);
	else
		$('#sfsd').bootstrapSwitch('state', false);
	var chk_jsid = $('#hzxxnv_ajaxForm input[name="jsids"]');
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
	elem: '#hzxxnv_ajaxForm #rysjhz'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxnv_ajaxForm #jlrqOne'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxnv_ajaxForm #jlrqThree'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxnv_ajaxForm #jlrqFive'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxnv_ajaxForm #jlrqSeven'
	, theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#hzxxnv_ajaxForm #bgsj'
	, theme: '#2381E9'
});

//添加日期控件
laydate.render({
	elem: ' #hzxxnv_ajaxForm #cysjhz'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxnv_ajaxForm #cicusjhz'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: '#hzxxnv_ajaxForm #hxjtysjhz'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxnv_ajaxForm #crrttysjhz'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
});
//添加日期控件
laydate.render({
	elem: ' #hzxxnv_ajaxForm #xghxytysjhz'
	, theme: '#2381E9'
	, type: 'datetime'
	, format: 'yyyy-MM-dd HH:mm'//保留时分
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

//下载模板
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
//删除模板
function del(fjid,wjlj,div){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
						});
						$("#ajaxForm"+"  #"+div).remove();
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},1);
			},'json');
			jQuery.ajaxSetup({async:true});
		}
	});
}


function displayUpInfo(fjid){
	if(!$("#hzxxnv_ajaxForm #fjids").val()){
		$("#hzxxnv_ajaxForm #fjids").val(fjid);
	}else{
		$("#hzxxnv_ajaxForm #fjids").val($("#hzxxnv_ajaxForm #fjids").val()+","+fjid);
	}
}
$(document).ready(function() {
	var sign_params=[];
	sign_params.prefix=$('#hzxxnv_ajaxForm #urlPrefix').val();
	var oFileInput = new FileInput();
	oFileInput.Init("hzxxnv_ajaxForm","displayUpInfo",2,1,"sign_file",null,sign_params);
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
	//所有下拉框添加choose样式
	jQuery('#hzxxnv_ajaxForm .chosen-select').chosen({ width: '100%' });
	var formate = $('#ss_formAction').val();
	console.log("修改页");
	if (formate == 'add') {
		$('#nryjbhadd').hide();
	}
 var cT = $('input[name="yz"]').filter(':checked').val();
 var yzType = $("#" + cT).val();
	//由各种类型显示不同页面
   changeHide(yzType);
});