$("#ajaxForm .jcxm").bind("click",function(e){
    var cskz3=$("#ajaxForm .jcxm:checked").attr("cskz3");
    var cskz1=$("#ajaxForm .jcxm:checked").attr("cskz1");
	$("#ajaxForm #cskz1").val(cskz1);
	$("#ajaxForm #cskz3").val(cskz3);
	$("#ajaxForm #ywlx").val(cskz3+"_"+cskz1);
    $("#ajaxForm #w_ywlx").val(cskz3+"_"+cskz1+"_WORD");
    // 查询检测子项目
    getSubDetect(e.currentTarget.value);
    //0.初始化fileinput
    var oInspectFileInput = new InspectFileInput();
    oInspectFileInput.Init("ajaxForm","displayUpInfo",2,1,"det_file",cskz3+"_"+cskz1);
    //1.初始化fileinput
    var oInspectFileInput = new InspectFileInput();
    oInspectFileInput.Init("ajaxForm","t_displayUpInfo",2,1,"word_file",cskz3+"_"+cskz1+"_WORD");
})

function clickSubDetect(){
    var cskz3=$("#ajaxForm .jczxm:checked").attr("cskz3");
    var cskz1=$("#ajaxForm .jczxm:checked").attr("cskz1");
	$("#ajaxForm #cskz1").val(cskz1);
	$("#ajaxForm #cskz3").val(cskz3);
	$("#ajaxForm #ywlx").val(cskz3+"_"+cskz1);
    $("#ajaxForm #w_ywlx").val(cskz3+"_"+cskz1+"_WORD");
    //点击过子项目后所有的附件 的ywlx都需要重置
    //0.初始化fileinput
    var oInspectFileInput = new InspectFileInput();
    oInspectFileInput.Init("ajaxForm","displayUpInfo",2,1,"det_file",cskz3+"_"+cskz1);
    //1.初始化fileinput
    var oInspectFileInput = new InspectFileInput();
    oInspectFileInput.Init("ajaxForm","t_displayUpInfo",2,1,"word_file",cskz3+"_"+cskz1+"_WORD");
}

function init(){
	//添加日期控件
	laydate.render({
		elem: '#ajaxForm #bgrq'
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
	//判断是否为外部调用
	var flg = $("#ajaxForm #flg").val();
	if(flg == 1){
		var button = "";
		button += "<div class='col-md-12 col-sm-12 col-xs-12' style='text-align:center;'>"
		button += "<button type='button' id='pathogen_upload_confirm' onclick='confirm()' class='btn btn-primary'>确认</button>"
		button += "</div>";
    	$("#ajaxForm .body-content .row").append(button);
	}
}

function confirm(){
	var ywlx = $("#ajaxForm #ywlx").val();
	var w_ywlx = $("#ajaxForm #w_ywlx").val();
	var ywlx_q = $("#ajaxForm #ywlx_q").val();
	var w_ywlx_q = $("#ajaxForm #w_ywlx_q").val();
	// var pdf_ywlx_x = $("#ajaxForm #pdf_ywlx_x").val();
	// var pdf_ywlx_y = $("#ajaxForm #pdf_ywlx_y").val();
	// var pdf_ywlx_g = $("#ajaxForm #pdf_ywlx_g").val();
	var fjids = $("#ajaxForm #fjids").val();
	var w_fjids = $("#ajaxForm #w_fjids").val();
	var fjids_q = $("#ajaxForm #fjids_q").val();
	var w_fjids_q = $("#ajaxForm #w_fjids_q").val();
	// var x_fjids_pdf = $("#ajaxForm #x_fjids_pdf").val();
	// var y_fjids_pdf = $("#ajaxForm #y_fjids_pdf").val();
	// var g_fjids_pdf = $("#ajaxForm #g_fjids_pdf").val();
	var sjid = $("#ajaxForm #sjid").val();
	var w_fjid_z=$("#ajaxForm #w_fjids_z").val();
	var access_token = $("#ajaxForm input[name='access_token']").val();
	var cskz1 = $("#ajaxForm #cskz1").val();
	var cskz3 = $("#ajaxForm #cskz3").val();
	$("#pathogen_upload_confirm").prop("disabled",true);
	$.ajax({
		url: '/inspection/pathogen/uploadSaveReport',
		type: 'post',
		dataType: 'json',
		data : {"ywlx" : ywlx,"w_ywlx" : w_ywlx,"ywlx_q" : ywlx_q,"w_ywlx_q" : w_ywlx_q,
				"fjids" : fjids,"w_fjids" : w_fjids,"fjids_q" : fjids_q,"w_fjids_q" : w_fjids_q,
				"sjid" : sjid,"w_fjid_z":w_fjid_z,"access_token" : access_token,"cskz3":cskz3,"cskz1":cskz1},
		success: function(result) {
			$.alert(result.message);
			$("#pathogen_upload_confirm").prop("disabled",false);
		}
	});
}

function getBgrq(){
	//默认期望完成日期
	var bgrq = new Date();
	bgrq.setDate(bgrq.getDate()); 
	y = bgrq.getFullYear();
	m = bgrq.getMonth() + 1;
	d = bgrq.getDate();
	h = bgrq.getHours();
	min = bgrq.getMinutes();
	s = bgrq.getSeconds();
	bgrq = y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d) + " " + h + ":" + min + ":" + s;
	$("#ajaxForm #bgrq").val(bgrq);
}

//点击文件上传
function editfile(fileDiv,file_btn){
	$("#"+fileDiv).show();
	$("#"+file_btn).hide();
}
//点击隐藏文件上传
function cancelfile(fileDiv,file_btn){
	$("#"+file_btn).show();
	$("#"+fileDiv).hide();
}


//初始化fileinput
var InspectFileInput = function () {
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
		control.fileinput("destroy").fileinput({
			language: 'zh', //设置语言
			uploadUrl: "/common/file/saveImportFile?access_token="+access_token, //上传的地址
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
		.off('filepredelete').on('filepredelete', function(event, key, jqXHR, data) {
			
        })
        //删除打开页面后新上传文件时执行
        .off('filesuccessremove').on('filesuccessremove', function(event, key, jqXHR, data) {
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
        	// }else if(fileName == 'x_file_q'){
			// 	$("#"+ ctrlName + " #x_fjids_pdf").val("");
			// }else if(fileName == 'y_file_q'){
			// 	$("#"+ ctrlName + " #y_fjids_pdf").val("");
			// }else if(fileName == 'g_file_q'){
			// 	$("#"+ ctrlName + " #g_fjids_pdf").val("");
			}
        })

		// 实现图片被选中后自动提交
		.off('filebatchselected').on('filebatchselected', function(event, files) {
			// 选中事件
			$(event.target).fileinput('upload');
			filecnt = files.length;
			
		})
		// 异步上传错误结果处理
		.off('fileerror').on('fileerror', function(event, data, msg) {  //一个文件上传失败
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
		.off('fileuploaded').on('fileuploaded', function(event, data) {
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

function displayUpInfo(fjid){
    if(!$("#ajaxForm #fjids").val()){
        $("#ajaxForm #fjids").val(fjid);
    }else{
        $("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
    }
}
function t_displayUpInfo(fjid){
    if(!$("#ajaxForm #w_fjids").val()){
        $("#ajaxForm #w_fjids").val(fjid);
    }else{
        $("#ajaxForm #w_fjids").val($("#ajaxForm #w_fjids").val()+","+fjid);
    }
}

function displayUpInfo_q(fjid){
    if(!$("#ajaxForm #fjids_q").val()){
        $("#ajaxForm #fjids_q").val(fjid);
    }else{
        $("#ajaxForm #fjids_q").val($("#ajaxForm #fjids_q").val()+","+fjid);
    }
}
function t_displayUpInfo_q(fjid){
    if(!$("#ajaxForm #w_fjids_q").val()){
        $("#ajaxForm #w_fjids_q").val(fjid);
    }else{
        $("#ajaxForm #w_fjids_q").val($("#ajaxForm #w_fjids_q").val()+","+fjid);
    }
}

function t_displayUpInfo_z(fjid){
    if(!$("#ajaxForm #w_fjids_z").val()){
        $("#ajaxForm #w_fjids_z").val(fjid);
    }else{
        $("#ajaxForm #w_fjids_z").val($("#ajaxForm #w_fjids_z").val()+","+fjid);
    }
}


/**
 * 根据检测项目获取检测子项目
 * @returns
 */
function getSubDetect(jcxmid){

    var cskz3=$("#ajaxForm .jcxm:checked").attr("cskz3");
    //没有选中，则情况子项目
    if(!cskz3 || cskz3==""){
        var jczxmHtml = "";
        $("#ajaxForm #jczxm_div").hide();
        $("#ajaxForm #jczxmDiv").append(jczxmHtml);
        return;
    }

	$.ajax({
		url: '/inspection/inspection/pagedataSubDetect',
		type: 'post',
		async: false,
		data: {
			"jcxmid": jcxmid,
			"access_token": $("#ac_tk").val(),
		},
		dataType: 'json',
		success: function(result) {
			var data = result.subdetectlist;
            var jczxmHtml = "";
            if(data != null && data.length > 0){
                $.each(data,function(i){
                    jczxmHtml += "<div class='col-sm-4' id = '"+data[i].csid+"' value='" + data[i].csid + "' style='padding-left:0px;' >" +
                        "<label class='checkboxLabel checkbox-inline' style='padding-left:16px;'>" +
                        "<input class ='jczxm' id='"+data[i].csid+"' name='jczxmid' type='radio' value='" + data[i].csid + "' jcxmid='" + data[i].fcsid + "' cskz1='" + data[i].cskz1 + "' cskz3='" + data[i].cskz3 + "' csmc='" + data[i].csmc + "' validate='{required:true}' onclick='clickSubDetect()'/>" +
                        "<span class='color_"+data[i].fcskz2+"' style='padding-left: 3px;'>"+data[i].csmc+"</span>" +
                        "</label></div>";
                });
                $("#ajaxForm #jczxm_div").show();
                $("#ajaxForm #jczxmDiv").html(jczxmHtml);
            }else{
                $("#ajaxForm #jczxm_div").hide();
                $("#ajaxForm #jczxmDiv").html(jczxmHtml);
            }
		}
	});
}

$(document).ready(function(){
    init();
    //所有下拉框添加choose样式
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
    if(!$("#ajaxForm #bgrq").val()){
    	getBgrq();
    }
});
