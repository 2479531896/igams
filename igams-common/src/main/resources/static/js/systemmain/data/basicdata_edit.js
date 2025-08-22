var pre_basicType=null;
function btnBasicEditBind(){
	$("#ajaxForm #jclb").unbind("change").change(function(){
		//清空fjids
		$("#ajaxForm #fjids_D").val("");
		$("#ajaxForm #fjids_R").val("");
		$("#ajaxForm #fjids_C").val("");
		var html = "<option value=''>--请选择--</option>";
		jQuery("#ajaxForm #fcsid").empty();
    	jQuery("#ajaxForm #fcsid").append(html);
		jQuery("#ajaxForm #fcsid").val("");
		jQuery("#ajaxForm #ffcsid").empty();
    	jQuery("#ajaxForm #ffcsid").append(html);
		jQuery("#ajaxForm #ffcsid").val("");
		pre_basicType = $("#ajaxForm #jclb").find("option:selected");
		if(pre_basicType.attr("attr_pid")!=null&&pre_basicType.attr("attr_pid")!=""){
			showPcsdmData(pre_basicType.attr("attr_pid"));
		}else{
			if(pre_basicType.attr("attr_p")!=null&&pre_basicType.attr("attr_p")!=""){
				showFcsdmData(pre_basicType.attr("attr_p"));
			}
		}
		hiddenData();
	});
}
$("#ajaxForm #ffcsid").unbind("change").change(function(){
	pre_basicType = $("#ajaxForm #jclb");
	p_type = pre_basicType.attr("attr_p");
	showChildcsdmData(p_type,$("#ajaxForm  #ffcsid").val());
});
function hiddenData(){
	if(pre_basicType == null)
		return;
	if(pre_basicType.attr("attr_p")==null||pre_basicType.attr("attr_p")==""){
		$("#ajaxForm #divfcs").hide();
		$("#ajaxForm #divpcs").hide();
		$("#ajaxForm #fcsid").val("");
		$("#ajaxForm #fcsid").prop("disabled",true);
		$("#ajaxForm #ffcsid").val("");
		$("#ajaxForm #ffcsid").prop("disabled",true);
	}
	else{
		$("#divfcs").show();
		$("#fcsid").prop("disabled",false);
		if(pre_basicType.attr("attr_pid")==null||pre_basicType.attr("attr_pid")==""){
			$("#divpcs").hide();
			$("#ffcsid").val("");
			$("#ffcsid").prop("disabled",true);
		}else{
			$("#divpcs").show();
			$("#ffcsid").prop("disabled",false);
		}
	}
	if(pre_basicType.attr("attr_ex1_sel")!=null&&pre_basicType.attr("attr_ex1_sel")!=""){
		showExSelect(pre_basicType.attr("attr_ex1_sel"),"1");
	}else if(pre_basicType.attr("attr_ex1")!=null&&pre_basicType.attr("attr_ex1")!=""){
		showExInput(pre_basicType,"1");
	}else{
		$("#divextend1").hide();
	}
	
	if(pre_basicType.attr("attr_ex2_sel")!=null&&pre_basicType.attr("attr_ex2_sel")!=""){
		showExSelect(pre_basicType.attr("attr_ex2_sel"),"2");
	}else if(pre_basicType.attr("attr_ex2")!=null&&pre_basicType.attr("attr_ex2")!=""){
		showExInput(pre_basicType,"2");
	}else {
		$("#divextend2").hide();
	}
	
	if(pre_basicType.attr("attr_ex3_sel")!=null&&pre_basicType.attr("attr_ex3_sel")!=""){
		showExSelect(pre_basicType.attr("attr_ex3_sel"),"3");
	}else if(pre_basicType.attr("attr_ex3")!=null&&pre_basicType.attr("attr_ex3")!=""){
		showExInput(pre_basicType,"3");
	}else{
		$("#divextend3").hide();
	}
	
	
	if(pre_basicType.attr("attr_ex4_sel")!=null&&pre_basicType.attr("attr_ex4_sel")!=""){
		showExSelect(pre_basicType.attr("attr_ex4_sel"),"4");
	}else if(pre_basicType.attr("attr_ex4")!=null&&pre_basicType.attr("attr_ex4")!=""){
		showExInput(pre_basicType,"4");
	}else{
		$("#divextend4").hide();
	}

	if(pre_basicType.attr("attr_ex5_sel")!=null&&pre_basicType.attr("attr_ex4_sel")!=""){
    		showExSelect(pre_basicType.attr("attr_ex4_sel"),"5");
    }else if(pre_basicType.attr("attr_ex5")!=null&&pre_basicType.attr("attr_ex5")!=""){
        showExInput(pre_basicType,"5","11");
    }else{
        $("#divextend11").hide();
    }
	
	if(pre_basicType.attr("attr_ex5_flie")==null||pre_basicType.attr("attr_ex5_flie")==""){
		$("#divextend5").hide();
		$("#files").hide();
	}else{
		$("#divextend5").show();
		$("#files").show();
		$("#divextend5 label").text(pre_basicType.attr("attr_file_name_1"));
	}
	if(pre_basicType.attr("attr_ex6_flie")==null||pre_basicType.attr("attr_ex6_flie")==""){
		$("#divextend6").hide();
	}else{
		$("#divextend6").show();
		$("#divextend6 label").text(pre_basicType.attr("attr_file_name_2"));
	}
	if(pre_basicType.attr("attr_ex7_flie")==null||pre_basicType.attr("attr_ex7_flie")==""){
		$("#divextend7").hide();
	}else{
		$("#divextend7").show();
		$("#divextend7 label").text(pre_basicType.attr("attr_file_name_3"));
	}
	
	report_D_init(pre_basicType);
	report_R_init(pre_basicType);
	report_C_init(pre_basicType);
}

//扩展参数为输入框时进入
function showExInput(pre_basicType,index,index1){
	var kzcsid = $("#cskz"+index).val();
	var html = "";
	html += "<label for='' class='col-sm-3 control-label'>参数扩展"+index+"</label> ";
	html += "<div class='col-sm-8'><input type='text' name='cskz"+index+"' value='' id='cskz"+index+"' class='form-control success' aria-invalid='false'></div>";

	if(index1!=undefined){
	    jQuery("#divextend"+index1).empty();
    	jQuery("#divextend"+index1).append(html);
        $("#divextend"+index1).show();
	}else{
        jQuery("#divextend"+index).empty();
        jQuery("#divextend"+index).append(html);
	    $("#divextend"+index).show();
	}


	$("#cskz"+index).val(kzcsid);
	$("#cskz"+index).attr("placeholder",pre_basicType.attr("attr_ex"+index));
}

//扩展参数为下拉框时进入
function showExSelect(basicType,index){
	$.ajax({
		type : "post",
		url: $("#basicDataSearch #urlPrefix").val() + "/systemmain/data/ansyGetJcsjList", 
		data : {"jclb":basicType,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success : function(data){
        	var kzcsid = $("#ajaxForm #cskz"+index).val();
        	var html = "";
        	html += "<label for='' class='col-sm-3 control-label'><span style='color:red;'>*</span>参数扩展"+index+"</label> ";
        	html += "<div class='col-sm-8'>";
        	html +=  "<select name='cskz"+index+"' id='cskz"+index+"' class='form-control chosen-select' validate='{required:true}'>";
        	//父节点数据
        	html += "<option value=''>--请选择--</option>";
        	$.each(data,function(i){
        		if(kzcsid == data[i].csid){
        			html += "<option value='" + data[i].csid + "' selected='selected'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}else{
        			html += "<option value='" + data[i].csid + "'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}
        	});
        	html += "</select>";
        	html += "</div>";
        	$("#ajaxForm #divextend"+index).empty();
        	$("#ajaxForm #divextend"+index).append(html);
        	$("#ajaxForm #divextend"+index).show();
        	$("#ajaxForm #cskz"+index).chosen({width: '100%'});
        	$("#ajaxForm #cskz"+index).trigger("chosen:updated");
        }
	});
}

function showPcsdmData(pId){
   $.ajax({
		type : "post",
		url: $("#basicDataSearch #urlPrefix").val() + "/systemmain/data/ansyGetJcsjList", 
        data : {"jclb":pId,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success : function(data){
        	var ffcsid = $("#ajaxForm #ffcsid").val();
        	//父节点数据
        	var html = "<option value=''>--请选择--</option>";
        	$.each(data,function(i){
        		if(ffcsid == data[i].csid){
        			html += "<option value='" + data[i].csid + "' selected='selected'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}else{
        			html += "<option value='" + data[i].csid + "'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}
        	});
        	$("#ajaxForm #ffcsid").empty();
        	$("#ajaxForm #ffcsid").append(html);
        	$("#ajaxForm #ffcsid").chosen({width: '100%'});
			$("#ajaxForm #ffcsid").trigger("chosen:updated");
        }
	});
}

function showFcsdmData(parentId){
   $.ajax({
		type : "post",
		url: $("#basicDataSearch #urlPrefix").val() + "/systemmain/data/ansyGetJcsjList",
        data : {"jclb":parentId,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success : function(data){
        	var fcsid = $("#ajaxForm #fcsid").val();
        	//父节点数据
        	var html = "<option value=''>--请选择--</option>";
        	$.each(data,function(i){
        		if(fcsid == data[i].csid){
        			html += "<option value='" + data[i].csid + "' selected='selected'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}else{
        			html += "<option value='" + data[i].csid + "'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}
        	});
        	$("#ajaxForm #fcsid").empty();
        	$("#ajaxForm #fcsid").append(html);
        	$("#ajaxForm #fcsid").chosen({width: '100%'});
        	$("#ajaxForm #fcsid").trigger("chosen:updated");
        }
	});
}

function showChildcsdmData(pType,ffcsid){
	if(ffcsid==null || ffcsid==""){
		var html = "<option value=''>--请选择--</option>";
		$("#ajaxForm #fcsid").empty();
		$("#ajaxForm #fcsid").append(html);
		$("#ajaxForm #fcsid").chosen({width: '100%'});
		$("#ajaxForm #fcsid").trigger("chosen:updated");
   }else{
	   $.ajax({
		type : "post",
		url: $("#basicDataSearch #urlPrefix").val() + "/systemmain/data/ansyGetJcsjList",
        data : {"jclb":pType,"fcsid":ffcsid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success : function(data){
        	var fcsid = $("#ajaxForm #fcsid").val();
        	//父节点数据
        	var html = "<option value=''>--请选择--</option>";
        	$.each(data,function(i){
        		if(fcsid == data[i].csid){
        			html += "<option value='" + data[i].csid + "' selected='selected'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}else{
        			html += "<option value='" + data[i].csid + "'>"+data[i].csdm + "--" + data[i].csmc + "</option>";
        		}
        	});
        	$("#ajaxForm #fcsid").empty();
        	$("#ajaxForm #fcsid").append(html);
    		$("#ajaxForm #fcsid").chosen({width: '100%'});
        	$("#ajaxForm #fcsid").trigger("chosen:updated");
        }
	  });
   }
}
//初始化DNA上传框
function report_D_init(pre_basicType){
	var ywlx=pre_basicType.attr("attr_ex5_flie");
	$("#ajaxForm #ywlx_D").val(ywlx);
	var oTemeplateFileInput = new TemeplateFileInput();
	oTemeplateFileInput.Init("ajaxForm","displayUpInfo_D",2,1,"report_file",ywlx,"_D");
}

function displayUpInfo_D(fjid){
	if(!$("#ajaxForm #fjids_D").val()){
		$("#ajaxForm #fjids_D").val(fjid);
	}else{
		$("#ajaxForm #fjids_D").val($("#ajaxForm #fjids_D").val()+","+fjid);
	}
}
//初始化RNA上传框
function report_R_init(pre_basicType){
	var ywlx=pre_basicType.attr("attr_ex6_flie");
	$("#ajaxForm #ywlx_R").val(ywlx);
	var oTemeplateFileInput = new TemeplateFileInput();
	oTemeplateFileInput.Init("ajaxForm","displayUpInfo_R",2,1,"report_file",ywlx,"_R");
}

function displayUpInfo_R(fjid){
	if(!$("#ajaxForm #fjids_R").val()){
		$("#ajaxForm #fjids_R").val(fjid);
	}else{
		$("#ajaxForm #fjids_R").val($("#ajaxForm #fjids_R").val()+","+fjid);
	}
}

//初始化DNA+RNA上传框
function report_C_init(pre_basicType){
	var ywlx=pre_basicType.attr("attr_ex7_flie");
	$("#ajaxForm #ywlx_C").val(ywlx);
	var oTemeplateFileInput = new TemeplateFileInput();
	oTemeplateFileInput.Init("ajaxForm","displayUpInfo_C",2,1,"report_file",ywlx,"_C");
}

function displayUpInfo_C(fjid){
	if(!$("#ajaxForm #fjids_C").val()){
		$("#ajaxForm #fjids_C").val(fjid);
	}else{
		$("#ajaxForm #fjids_C").val($("#ajaxForm #fjids_C").val()+","+fjid);
	}
}

//下载模板
function xz(fjid){
	 jQuery('<form action="'+$("#basicDataSearch #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
			var url= $("#basicDataSearch #urlPrefix").val()+"/common/file/delFile";
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
//初始化插件
var TemeplateFileInput=function(){
	var oTemeplateFileInput = new Object();
	//初始化fileinput控件（第一次初始化）
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
	oTemeplateFileInput.Init = function(ctrlName,callback,impflg,singleFlg,fileName, ywlx,jcxmflg) {
		var control = $('#' + ctrlName + ' #' + fileName+jcxmflg);
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
			uploadUrl: $("#basicDataSearch #urlPrefix").val()+"/common/file/saveImportFile?access_token="+access_token, //上传的地址
			showUpload: false, //是否显示上传按钮
			showPreview: true, //展前预览
			showCaption: false,//是否显示标题
			maxWidth:100,
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
			},otherActionButtons:'<button type="button" class="kv-file-down btn btn-xs btn-default" {dataKey} title="下载附件"><i class="fa fa-cloud-download"></i></button>',
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
				obj.fileName = fileName+jcxmflg;
				return obj;
			},
			initialPreviewAsData:true,
		    initialPreview: [        //这里配置需要初始展示的图片连接数组，字符串类型的，通常是通过后台获取后然后组装成数组直接赋给initialPreview就行了
		          //"http://localhost:8086/common/file/getFileInfo",
            ],
            initialPreviewConfig: [ //配置预览中的一些参数 
	        ]
		})//删除初始化存在文件时执行
		.on('filepredelete', function(event, key, jqXHR, data) {
			
        })
        //删除打开页面后新上传文件时执行
        .on('filesuccessremove', function(event, key, jqXHR, data) {
        		var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
        		var index = 0;
        		for(var i=0;i<frames.length;i++){
        			if(key == frames[i].id){
        				index = i;
        				break;
        			}
        		}
        		var str = $("#"+ ctrlName + " #fjids"+jcxmflg).val();
        		var arr=str.split(",");
        		arr.splice(index,1);
        		var str=arr.join(",");
        		$("#"+ ctrlName + " #fjids"+jcxmflg).val(str);
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
	return oTemeplateFileInput;
}
jQuery(function(){
	pre_basicType = $("#ajaxForm #jclb").find("option:selected");
	btnBasicEditBind();
	hiddenData();
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	
});