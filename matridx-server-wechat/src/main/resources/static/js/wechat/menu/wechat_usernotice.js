//信息显示配置
var showConfig = function(confirmmsg){
	var formname = "#ajaxForm";
	$(formname + " #errordiv").addClass("hidden");
	$(formname + " #infodiv").addClass("hidden");
	$(formname + " #confirmdiv").addClass("hidden");
	$(formname + " #warndiv").addClass("hidden");
	$(formname + " #btn-cancel").addClass("hidden");
	$(formname + " #btn-continue").addClass("hidden");
	$(formname + " #btn-complete").addClass("hidden");
	var existsMsg = false;
	if(confirmmsg){
		$(formname + " #confirmmsg").html(confirmmsg);	
		$(formname + " #confirmdiv").removeClass("hidden");
		$(formname + " #btn-cancel").removeClass("hidden");
		$(formname + " #btn-continue").removeClass("hidden");
		existsMsg = true;
	}
}

//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
	var formname = "#ajaxForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpInfo",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.endflg==1){
				$(formname +" #confirmmsg").html(data.confirmmsg);
				showConfig(data.confirmmsg);
			}else{
				if(data.errormsg && data.errormsg!=""){
					$(formname +" #errormsg").html(data.errormsg);
					showConfig(data.errormsg);
				}
				else{
					if(intervalTime < 2000)
						intervalTime = intervalTime + 1000;
					if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.msg)}
					setTimeout("checkImpCheckStatus("+ intervalTime +",'"+fjid+"')",intervalTime)
				}
			}
		}
	});
};

//自动检查服务器插入数据库进度
var checkImpInsertStatus = function(intervalTime,fjid){
	var formname = "#ajaxForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpSave",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.insertEndflg==1){
				if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
				$(formname + " #infodiv").removeClass("hidden");
				$(formname + " #btn-complete").removeClass("hidden");
				$.alert("<h>以下信息未发送成功</h>"+data.insertWarn);
			}else{
				if(intervalTime < 2000)
					intervalTime = intervalTime + 1000;
				if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
				$(formname + " #infodiv").removeClass("hidden");
				setTimeout("checkImpInsertStatus("+ intervalTime +",'"+fjid+"')",intervalTime)
			}
		}
	});
};

var smsImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#ajaxForm";
    oInits.Init = function () {
    	//取消按钮
    	$(formname + " #btn-cancel").unbind("click").click(function(){
    		$(formname + " #imp_file")
				.fileinput('clear')
				.fileinput('unlock');
			$(formname + " #imp_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			$('#ajaxForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
    	});
    	//继续
    	$(formname + " #btn-continue").unbind("click").click(function(){
    		if(!$("#ajaxForm").valid()){
				return false;
			}
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			var s_fjid = $("#ajaxForm #fjid").val();
			var dxmb = $("#ajaxForm #dxmb").val();
			var sms_sign = $("#ajaxForm #sms_sign").val();
    		$.ajax({
    			type : "POST",
    			url : "/common/file/saveImportRecord",
    			data : {"fjid":s_fjid,"access_token":$("#ac_tk").val(),ywlx: $("#ajaxForm #ywlx").val(),impflg:"3",sms_sign:sms_sign,dxmb:dxmb},
    			dataType : "json",
    			success:function(data){
    				if(data.status == "fail")
    				{
    					$.alert("服务器异常,请稍后再次尝试。错误信息：" + data.msg);
    					$(formname + " #btn-cancel").removeClass("hidden");
    					$(formname + " #btn-continue").removeClass("hidden");
    				}else{
        				setTimeout("checkImpInsertStatus(2000,'"+ s_fjid + "')",1000);
    				}
    			}
    		});
    	});
    	
    	//完成
    	$(formname + " #btn-complete").unbind("click").click(function(){
    		$(formname + " #sms_file")
			.fileinput('clear')
			.fileinput('unlock');
			$(formname + " #sms_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			$('#ajaxForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			
    	});
    	
    	//下载模板按钮
    	$(formname + " #template_dx").unbind("click").click(function(){
    		$("#updateAjaxForm #access_token").val($("#ac_tk").val());
    		$("#updateAjaxForm").submit();
    	});
    }
    return oInits;
}


//初始化fileinput
var FileInput = function () {
	var oFile = new Object();
	//初始化fileinput控件（第一次初始化）
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
	oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName) {
		var control = $('#' + ctrlName + ' #' + fileName);
		var filecnt = 0;

		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl: "/common/file/saveImportFile?access_token="+$("#ac_tk").val(), //上传的地址
			showUpload: false, //是否显示上传按钮
			showPreview: true, //展前预览
			showCaption: true,//是否显示标题
			showRemove: true,
			showCancel: false,
			showClose: false,
			showUploadedThumbs: true,
			encodeUrl: false,
			purifyHtml: false,
			browseClass: "btn btn-primary", //按钮样式	 
			dropZoneEnabled: false,//是否显示拖拽区域
			//minImageWidth: 50, //图片的最小宽度
			//minImageHeight: 50,//图片的最小高度
			//maxImageWidth: 1000,//图片的最大宽度
			//maxImageHeight: 1000,//图片的最大高度
			maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
			//minFileCount: 0,
			maxFileCount: 5, //表示允许同时上传的最大文件个数
			enctype: 'multipart/form-data',
			validateInitialCount:true,
			previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
			msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
			layoutTemplates :{
				//actionDelete:'', //去除上传预览的缩略图中的删除图标
				actionUpload:'',//去除上传预览缩略图中的上传图片；
				//actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
			},
			
			uploadExtraData:function (previewId, index) {
				//向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
				var obj = {};
				obj.access_token = $("#ac_tk").val();
				obj.ywlx = $("#"+ ctrlName + " #ywlx").val();
				if(impflg){
					obj.impflg = 3;
				}
				obj.fileName = fileName;
				return obj;
			},
			initialPreviewAsData:false,
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
	return oFile;
};
function displayUpInfo(fjid){
	$("#ajaxForm #fjid").val(fjid);
	//1.初始化Table
	setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}
$(function(){
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",3,1,"sms_file");
	var oimpButtonInit = new smsImp_ButtonInit();
	oimpButtonInit.Init();
})