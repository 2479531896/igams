/**
 * 绑定按钮事件
 */
function btnBind(){
	
}

function init(){
	
}

//初始化fileinput
var FileInput = function () {
	var oFile = new Object();
	//初始化fileinput控件（第一次初始化）
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
	//importType用于区分固定表头的导入类别
	oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName,importType) {
		var control = $('#' + ctrlName + ' #' + fileName);
		var filecnt = 0;
		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl: "/common/file/saveImportFile?access_token="+$("#ac_tk").val(), //上传的地址
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
				obj.access_token = $("#ac_tk").val();
				obj.ywlx = $("#"+ ctrlName + " #ywlx").val();
				if(impflg){
					obj.impflg = impflg;
				}
				obj.fileName = fileName;
				obj.importType = importType;
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
        	if(fileName == 'msg_file'){
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
					eval(callback+"('"+ data.response.fjcfbDto.fjid+"','"+ data.response.fjcfbDto.lsbcdz+"')");
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

function displayUpInfo(fjid,lsbcdz){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
	if(!$("#ajaxForm #lsbcdz").val()){
		$("#ajaxForm #lsbcdz").val(lsbcdz);
	}else{
		$("#ajaxForm #lsbcdz").val($("#ajaxForm #lsbcdz").val()+","+lsbcdz);
	}
	
}

$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"msg_file");
	btnBind();
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});