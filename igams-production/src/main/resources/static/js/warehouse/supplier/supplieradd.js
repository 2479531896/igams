function initPage(){
 	//添加日期控件
 	laydate.render({
 	   elem: '#fzrq'
 	  ,theme: '#2381E9'
 	});
}
//打开文件上传界面
function editfile(divName,btnName){
	$("#ajaxForm"+"  #"+btnName).hide();
	$("#ajaxForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
	$("#ajaxForm"+"  #"+btnName).show();
	$("#ajaxForm"+"  #"+divName).hide();
}
jQuery.validator.addMethod("isPhone", function(value, element) {
    var length = value.length;
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
   }, "请填写正确的手机号码");//可以自定义默认提示信息

jQuery.validator.addMethod("isTel", function(value, element) {
          var length = value.length;
          var phone = /(^(\d{3,4}-)?\d{6,8}$)|(^(\d{3,4}-)?\d{6,8}(-\d{1,5})?$)|(\d{11})/;
          return this.optional(element) || (phone.test(value));
         }, "请填写正确的固定电话");//可以自定义默认提示信息

$.validator.addMethod("mobile", function(value, element) {
    if(/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/.test(value) || /^1[34578][0-9]\d{8}$/.test(value)){
        return true;
    }
    return false;
}, "请输入正确的固话或手机号码"); 

jQuery.validator.addMethod("qq", function(value, element) {
	var tel = /^[1-9]\d{4,9}$/;
	return this.optional(element) || (tel.test(value));
	}, "qq号码格式错误");

jQuery.validator.addMethod("sj", function(value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(19[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(15[0-9]{1}))+\d{8})$/
	return this.optional(element) || (length == 11 && mobile.test(value));
	}, "手机号码格式错误");

jQuery.validator.addMethod("chrnum", function(value, element) {
	var chrnum = /^([a-zA-Z0-9]+)$/;
	return this.optional(element) || (chrnum.test(value));
	}, "微信号格式错误");

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
		var url=$("#ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

//下载模板
function xz(fjid){
	jQuery('<form action="'+$("#ajaxForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
			var url= $("#ajaxForm #urlPrefix").val()+"/common/file/delFile";
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
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}
function displayUpInfo_t(fjid){
	if(!$("#ajaxForm #mbfjids").val()){
		$("#ajaxForm #mbfjids").val(fjid);
	}else{
		$("#ajaxForm #mbfjids").val($("#ajaxForm #mbfjids").val()+","+fjid);
	}
}
//初始化fileinput
var supplierFileInput = function () {
	var oInspectFileInput = new Object();
	//初始化fileinput控件（第一次初始化）
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
	oInspectFileInput.Init = function(ctrlName,callback,impflg,singleFlg,fileName, ywlx) {
		var control = $('#' + ctrlName + ' #' + fileName);
		var filecnt = 0;

		var access_token = null;
		if($("#ac_tk").val()){
			access_token = $("#ac_tk").val();
		}
		if($("#ajaxForm input[name='access_token']").val()){
			access_token = $("#ajaxForm input[name='access_token']").val();
		}
		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl:  $("#ajaxForm #urlPrefix").val()+"/common/file/saveImportFile?access_token="+access_token, //上传的地址
			showUpload: false, //是否显示上传按钮
			showPreview: true, //展前预览
			showCaption: false,//是否显示标题
			browseClass: "btn btn-primary", //按钮样式
			dropZoneEnabled: true,//是否显示拖拽区域
			//minImageWidth: 50, //图片的最小宽度
			//minImageHeight: 50,//图片的最小高度
			//maxImageWidth: 1000,//图片的最大宽度
			//maxImageHeight: 1000,//图片的最大高度
			maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
			//minFileCount: 0,
			maxFileCount: 10, //表示允许同时上传的最大文件个数
			enctype: 'multipart/form-data',
			validateInitialCount:true,
			previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
			msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
			layoutTemplates :{
				// actionDelete:'', //去除上传预览的缩略图中的删除图标
				actionUpload:'',//去除上传预览缩略图中的上传图片；
				//actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
			},
			otherActionButtons:'<button type="button" class="kv-file-down btn btn-xs btn-default" {dataKey} title="下载附件"><i class="fa fa-cloud-download"></i></button>',
			uploadExtraData:function (previewId, index) {
				//向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
				var obj = {};
				if($("#ac_tk").val()){
					obj.access_token = $("#ac_tk").val();
				}
				if($("#ajaxForm input[name='access_token']").val()){
					obj.access_token = $("#ajaxForm input[name='access_token']").val();
				}
				obj.ywlx = ywlx;
				if(impflg && impflg == 1){
					obj.impflg = 1;
				}
				obj.fileName = fileName;
				return obj;
			},
			initialPreviewAsData:true,
			initialPreview: [        //这里配置需要初始展示的图片连接数组，字符串类型的，通常是通过后台获取后然后组装成数组直接赋给initialPreview就行了
				//"http://localhost:8086/common/file/getFileInfo",
			],
			initialPreviewConfig: [ //配置预览中的一些参数
			]
		})
			//删除初始化存在文件时执行
			.on('filepredelete', function(event, key, jqXHR, data) {

			})
			//删除打开页面后新上传文件时执行
			.on('filesuccessremove', function(event, key, jqXHR, data) {
				if(fileName == 'det_file'){
					var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
					var index = 0;
					for(var i=0;i<frames.length;i++){
						if(key == frames[i].id){
							index = i;
							break;
						}
					}
					var str = $("#"+ ctrlName + " #fjids").val();
					var arr=str.split(",");
					arr.splice(index,1);
					var str=arr.join(",");
					$("#"+ ctrlName + " #fjids").val(str);
				}else if(fileName == 'word_file'){
					$("#"+ ctrlName + " #w_fjids").val("");
				}else if(fileName == 'det_file_q'){
					var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
					var index = 0;
					for(var i=0;i<frames.length;i++){
						if(key == frames[i].id){
							index = i;
							break;
						}
					}
					var str = $("#"+ ctrlName + " #fjids_q").val();
					var arr=str.split(",");
					arr.splice(index,1);
					var str=arr.join(",");
					$("#"+ ctrlName + " #fjids_q").val(str);
				}else if(fileName == 'word_file_q'){
					$("#"+ ctrlName + " #w_fjids_q").val("");
				}
			})

			// 实现图片被选中后自动提交
			.on('filebatchselected', function(event, files) {
				// 选中事件
				$(event.target).fileinput('upload');
				filecnt = files.length;

			})
			// 异步上传错误结果处理
			.on('fileerror', function(event, data, msg) {  //一个文件上传失败
				// 清除当前的预览图 ，并隐藏 【移除】 按钮
				$(event.target)
					.fileinput('clear')
					.fileinput('unlock');
				if(singleFlg && singleFlg==1){
					$(event.target)
						.parent()
						.siblings('.fileinput-remove')
						.hide()
				}
				// 打开失败的信息弹窗
				$.error("请求失败，请稍后重试",function() {});
			})
			// 异步上传成功结果处理
			.on('fileuploaded', function(event, data) {
				// 判断 code 是否为  0	0 代表成功
				status = data.response.status
				if (status === "success") {

					if(callback){
						eval(callback+"('"+ data.response.fjcfbDto.fjid+"')");
					}
				} else {
					// 失败回调
					// 清除当前的预览图 ，并隐藏 【移除】 按钮
					$(event.target)
						.fileinput('clear')
						.fileinput('unlock');
					if(singleFlg && singleFlg==1){
						$(event.target)
							.parent()
							.siblings('.fileinput-remove')
							.hide()
					}
					// 打开失败的信息弹窗
					$.error("请求失败，请稍后重试",function() {});
				}
			})
	}
	return oInspectFileInput;
};
$(function(){

	//0.初始化fileinput
	var sign_params=[];
	sign_params.prefix=$('#ajaxForm #urlPrefix').val();
	//0.初始化fileinput
	var oFileInput = new supplierFileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"sign_file",$("#ajaxForm #ywlx").val());
	var oFileInput_t = new supplierFileInput();
	oFileInput_t.Init("ajaxForm","displayUpInfo_t",2,1,"mbsign_file",$("#ajaxForm #mbywlx").val());
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	var fjid=$("#ajaxForm #fjid").val();
	if(fjid!=null&&fjid!=""){
		$("#ajaxForm #ycfj").show();
		$("#ajaxForm #fjsc").hide();
	}else{
		$("#ajaxForm #ycfj").hide();
		$("#ajaxForm #fjsc").show();
	}
	initPage();
});