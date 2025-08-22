function changHb() {
	let val = $("#addSjpdForm #hbmc").val();
	$.ajax({
		type : "POST",
		url : "/logistics/sjpdgl/pagedataGetMdd",
		data : {"hbmc":val,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success : function(data){
			$("#addSjpdForm #jcdwDiv").empty();
			$("#addSjpdForm #jcdwmc").val('');
			let html = ""
			html += "<select id='jcdw' name='jcdw' class='form-control chosen-select' validate='{required:true}' onchange=\"changejcdw()\">";
			html += "<option value=''>--请选择--</option>";
			for(var i = 0; i < data.list.length; i++) {
				html += "<option id='"+data.list[i].csid+"' value='"+data.list[i].csid+"' jcdwmc='"+data.list[i].csmc+"'";
				html += ">"+data.list[i].csmc+"</option>";
			}
			html +="</select>";
			$("#addSjpdForm #jcdwDiv").html(html);
			jQuery('#addSjpdForm .chosen-select').chosen({width: '100%'})
		}
	});
}

function chooseTz() {
	var url = "/logistics/sjpdgl/pagedataTz?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择通知员', chooseTzryConfig);
}
var chooseTzryConfig = {
	width		: "1000px",
	modalName	: "allocationModal",
	formName	: "taskListPartnerForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {

				var selWx = $("#taskListPartnerForm #t_xzrys").tagsinput('items');
				$("#taskListPartnerForm #hbmc").val(selWx);
				var hbmcs='';
				if(selWx!=null&&selWx!=undefined){
					for(var i = 0; i < selWx.length; i++){
						var value = selWx[i].yhid;
						var text = selWx[i].zsxm;
						hbmcs=hbmcs+selWx[i].zsxm+',';
					}
					$('#addSjpdForm #hbmcs').val(hbmcs)
					$('#addSjpdForm #tzry_str').val($('#taskListPartnerForm #t_xzrys').val());
					if(selWx.length>1){
						$('#addSjpdForm #tzrymc').val(selWx[0].zsxm+'等'+selWx.length+'人');
					}
					else if(selWx.length==1){
						$('#addSjpdForm #tzrymc').val(selWx[0].zsxm);
					}
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#taskListPartnerForm input[name='access_token']").val($("#ac_tk").val());
				$.closeModal(opts.modalName);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function changejcdw(){
	var jcdwmc=$("#addSjpdForm #jcdw option:selected").attr("jcdwmc");
	$("#addSjpdForm #jcdwmc").val(jcdwmc);
}
function changebblx(){
	var bblxmc=$("#addSjpdForm #bblx option:selected").attr("bblxmc");
	$("#addSjpdForm #bblxmc").val(bblxmc);
}
function changejsfs(){
	var jsfsmc=$("#addSjpdForm #jsfs option:selected").attr("jsfsmc");
	$("#addSjpdForm #jsfsmc").val(jsfsmc);
	var csdm=$("#addSjpdForm #jsfs option:selected").attr("csdm");
	$("#addSjpdForm #jsfsdm").val(csdm);
	if(csdm=='SF'||csdm=='JD'||csdm=='TC'){
		$("#addSjpdForm #bh").show();
		$("#addSjpdForm #ps").show();
		$('#addSjpdForm #yj').show();
		$('#addSjpdForm #yjqy').hide();
		$("#addSjpdForm #yjsj").val("");
	}else if (csdm=='ZS'){
		$("#addSjpdForm #bh").show();
		$("#addSjpdForm #yj").show();
		$('#addSjpdForm #ps').show();
		$("#addSjpdForm #wlfy").val("");
		$('#addSjpdForm #yjqy').hide();
		$("#addSjpdForm #yjsj").val("");
	}else if (csdm=='GT'){
		$('#addSjpdForm #bh').hide();
		$("#addSjpdForm #bbbh").val("");
		$('#addSjpdForm #ps').hide();
		$("#addSjpdForm #wlfy").val("");
		$('#addSjpdForm #yj').hide();
		$("#addSjpdForm #yjddsj").val("");
		$('#addSjpdForm #yjqy').show();
	}else if (csdm=='QYY'){
		$('#addSjpdForm #yjqy').show();
		$('#addSjpdForm #bh').hide();
		$("#addSjpdForm #bbbh").val("");
		$('#addSjpdForm #ps').hide();
		$("#addSjpdForm #wlfy").val("");
		$('#addSjpdForm #yj').hide();
		$("#addSjpdForm #yjddsj").val("");
	}
}
$('input[type=radio][name=sfsf]').change(function() {
	if (this.value == '1') {
		$("#addSjpdForm #je").show();
	} else if (this.value == '0') {
		$("#addSjpdForm #sfje").val("");
		$('#addSjpdForm #je').hide();
	}
});
//打开文件上传界面
function editfile(divName,btnName){
	$("#addSjpdForm"+"  #"+btnName).hide();
	$("#addSjpdForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
	$("#addSjpdForm"+"  #"+btnName).show();
	$("#addSjpdForm"+"  #"+divName).hide();
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url="/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
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

	if(!$("#addSjpdForm #fjids").val()){
		$("#addSjpdForm #fjids").val(fjid);
	}else{
		$("#addSjpdForm #fjids").val($("#addSjpdForm #fjids").val()+","+fjid);
	}
}

$(document).ready(function () {
	laydate.render({
		elem: '#addSjpdForm #yjsj'
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
		elem: '#addSjpdForm #yjddsj'
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

	//所有下拉框添加choose样式
	jQuery('#addSjpdForm .chosen-select').chosen({width: '100%'});
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("addSjpdForm","displayUpInfo",2,1,"sign_file",null,null);

	$("#addSjpdForm #bh").hide();
	$("#addSjpdForm #yj").hide();
	$("#addSjpdForm #je").hide();
	$("#addSjpdForm #ps").hide();
	$("#addSjpdForm #yjqy").hide();
	var jsfsmc=$("#addSjpdForm #jsfs option:selected").attr("jsfsmc");
	$("#addSjpdForm #jsfsmc").val(jsfsmc);
	var csdm=$("#addSjpdForm #jsfs option:selected").attr("csdm");
	$("#addSjpdForm #jsfsdm").val(csdm);
	if(csdm=='SF'||csdm=='JD'||csdm=='TC'){
		$("#addSjpdForm #bh").show();
		$("#addSjpdForm #ps").show();
		$('#addSjpdForm #yj').show();
		$('#addSjpdForm #yjqy').hide();
	}else if (csdm=='ZS'){
		$("#addSjpdForm #bh").show();
		$("#addSjpdForm #yj").show();
		$('#addSjpdForm #ps').show();
		$('#addSjpdForm #yjqy').hide();
	}else if (csdm=='GT'){
		$('#addSjpdForm #bh').hide();
		$('#addSjpdForm #ps').hide();
		$('#addSjpdForm #yj').hide();
		$('#addSjpdForm #yjqy').show();
	}else if (csdm=='QYY'){
		$('#addSjpdForm #yjqy').show();
		$('#addSjpdForm #bh').hide();
		$('#addSjpdForm #ps').hide();
		$('#addSjpdForm #yj').hide();
	}
	if ($('input[type=radio][name=sfsf]').val() == '1') {
		$("#addSjpdForm #je").show();
	} else if (this.value == '0') {
		$('#addSjpdForm #je').hide();
	}
});