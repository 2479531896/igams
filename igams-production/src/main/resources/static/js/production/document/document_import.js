
var impDocument_TableInit = function (fjid) {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#documentImportForm #tb_list').bootstrapTable({
			url: $('#documentImportForm #urlPrefix').val() + '/common/file/listImpInfo',		 //请求后台的URL（*）
			method: 'get',					  //请求方式（*）
			toolbar: '#documentImportForm #toolbar', //工具按钮用哪个容器
			striped: true,					  //是否显示行间隔色
			cache: false,					   //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,				   //是否显示分页（*）
			sortable: true,					 //是否启用排序
			sortName:"lrsj",					//排序字段
			sortOrder: "desc",				   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",		   //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,					   //初始化加载第一页，默认第一页
			pageSize: 999,					  //每页的记录行数（*）
			pageList: [999, 1999],				//可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,					  //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,				  //是否显示所有的列
			showRefresh: false,				  //是否显示刷新按钮
			minimumCountColumns: 2,			 //最少允许的列数
			clickToSelect: false,				//是否启用点击选中行
			//height: 500,						//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "fjid",					 //每一行的唯一标识，一般为主键列
			showToggle:false,					//是否显示详细视图和列表视图的切换按钮
			cardView: false,					//是否显示详细视图
			detailView: false,				   //是否显示父子表
			columns: [{
				field: 'sfsc',
				title: '是否上传文件',
				titleTooltip:'是否上传文件',
				width: '6%',
				align: 'left'
			}, {
				field: 'wjfl',
				title: '文件分类',
				titleTooltip:'文件分类',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'wjlb',
				title: '文件类别',
				titleTooltip:'文件类别',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'wjmc',
				title: '文件名称',
				titleTooltip:'文件名称',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'wjbh',
				title: '文件编号',
				titleTooltip:'文件编号',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'bbh',
				title: '版本号',
				titleTooltip:'版本号',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'sxrq',
				title: '生效日期',
				titleTooltip:'生效日期',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'syxwj',
				title: '受影响文件',
				titleTooltip:'受影响文件',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'ggly',
				title: '更改来源',
				titleTooltip:'更改来源',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'jgid',
				title: '机构',
				titleTooltip:'机构',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'bz',
				title: '备注',
				titleTooltip:'备注',
				width: '8%',
				align: 'left',
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
	};

	//得到查询的参数
	oTableInit.queryParams = function(params){
		//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
		//例如 toolbar 中的参数 
		//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
		//limit, offset, search, sort, order 
		//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
		//返回false将会终止请求
		var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   //页面大小
			pageNumber: (params.offset / params.limit) + 1,  //页码
			access_token: $("#ac_tk").val(),
			fjid: fjid,
			ywlx: $("#documentImportForm #ywlx").val(),
		};

		return map;
	};
	return oTableInit;
};


//信息显示配置
var showConfig = function(errormsg,infomsg,confirmmsg,warnmsg,successmsg,updList){
	var formname = "#documentImportForm";
	$(formname + " #errordiv").addClass("hidden");
	$(formname + " #infodiv").addClass("hidden");
	$(formname + " #confirmdiv").addClass("hidden");
	$(formname + " #warndiv").addClass("hidden");
	$(formname + " #btn-cancel").addClass("hidden");
	$(formname + " #btn-continue").addClass("hidden");
	$(formname + " #btn-complete").addClass("hidden");

	if(errormsg){
		$(formname + " #errormsg").html(errormsg);	
		$(formname + " #errordiv").removeClass("hidden");
		$(formname + " #btn-complete").removeClass("hidden");
		return ;
	}else{
		var existsMsg = false;
		if(infomsg){
			$(formname + " #infomsg").html(infomsg);	
			$(formname + " #infodiv").removeClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			existsMsg = true;
		}
		if(confirmmsg){
			$(formname + " #confirmmsg").html(confirmmsg);	
			$(formname + " #confirmdiv").removeClass("hidden");
			$(formname + " #btn-cancel").removeClass("hidden");
			$(formname + " #btn-continue").removeClass("hidden");
			existsMsg = true;
		}
		if(warnmsg){
			$(formname + " #warnmsg").html(warnmsg);	
			$(formname + " #warndiv").removeClass("hidden");
			$(formname + " #btn-cancel").removeClass("hidden");
			$(formname + " #btn-continue").removeClass("hidden");
			existsMsg = true;
		}
		if(successmsg){
			$(formname + " #infomsg").html(successmsg);	
			$(formname + " #infodiv").removeClass("hidden");
			$(formname + " #btn-complete").removeClass("hidden");
			existsMsg = true;
		}

		if (!existsMsg) {
			$(formname + " #infomsg").html("数据检查没有问题，请点击继续按钮继续或点击取消按钮取消上传。");	
			$(formname + " #infodiv").removeClass("hidden");
			$(formname + " #btn-cancel").removeClass("hidden");
			$(formname + " #btn-continue").removeClass("hidden");
		}
	}
}
//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
	var formname = "#documentImportForm";
	$.ajax({
		type : "POST",
		url : $('#documentImportForm #urlPrefix').val() + "/common/file/checkImpInfo",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.endflg==1){
				if(data.errormsg && data.errormsg!=""){
					$(formname +" #errormsg").html(data.errormsg);
					showConfig(data.errormsg);
				}else if(data.warnmsg && data.warnmsg!=""){
					$(formname +" #warnmsg").html(data.warnmsg);
					showConfig(data.warnmsg);
				}else if(data.confirmmsg && data.confirmmsg!=""){
					$(formname +" #confirmmsg").html(data.confirmmsg);
					showConfig(data.confirmmsg);
					$(formname +" #tb_list").bootstrapTable('refresh');
				}else if(data.infomsg && data.infomsg!=""){
					$(formname +"#infomsg").html(data.infomsg);
					showConfig(data.infomsg);
					//销毁
					$('#tb_list').bootstrapTable('destroy');
					//1.初始化Table
					var oTable = new impDocument_TableInit();
					oTable.Init();
				}else{
					showConfig(data.infomsg);
					//销毁
					$('#tb_list').bootstrapTable('destroy');
					//1.初始化Table
					var oTable = new impDocument_TableInit(fjid);
					oTable.Init();
				}
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
	var formname = "#documentImportForm";
	$.ajax({
		type : "POST",
		url : $('#documentImportForm #urlPrefix').val() + "/common/file/checkImpSave",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.insertEndflg==1){
				if(data.insertWarn){
					$(formname + " #errormsg").html(data.insertWarn);
					$(formname + " #infodiv").addClass("hidden");
					$(formname + " #errordiv").removeClass("hidden");
					$(formname + " #btn-complete").removeClass("hidden");
				}else{
					if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
					$(formname + " #infodiv").removeClass("hidden");
					$(formname + " #btn-complete").removeClass("hidden");	
				}
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

var documentImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#documentImportForm";
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
			$('#documentImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
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
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			var s_fjid = $("#documentImportForm #fjid").val();
			var ysblj = $("#documentImportForm #ysblj").val();
    		$.ajax({
    			type : "POST",
    			url : $('#documentImportForm #urlPrefix').val() + "/common/file/saveImportRecord",
    			data : {"fjid":s_fjid,"access_token":$("#ac_tk").val(),ywlx: $("#documentImportForm #ywlx").val(),"ysblj":ysblj},
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
    		$(formname + " #imp_file")
			.fileinput('clear')
			.fileinput('unlock');
			$(formname + " #imp_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			$('#documentImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			
    	});
    	
    	
    	//下载模板按钮
    	$(formname + " #template_dw").unbind("click").click(function(){
    		$("#updateDocumentForm #access_token").val($("#ac_tk").val());
    		$("#updateDocumentForm").submit();
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
	//importType用于区分固定表头的导入类别
	oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName,importType) {
		var control = $('#' + ctrlName + ' #' + fileName);
		var filecnt = 0;
		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl: $('#documentImportForm #urlPrefix').val() + "/common/file/saveImportFile?access_token="+$("#ac_tk").val(), //上传的地址
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
				obj.prefjid=$("#"+ ctrlName + " #prefjid").val();
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
					eval(callback+"('"+ data.response.fjcfbDto.fjid+"','"+data.response.fjcfbDto.lsbcdz+"')");
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

function displayUpInfo(fjid,ysblj){
	//获取扩展名
	var suffix = ysblj.substring(ysblj.lastIndexOf("."), ysblj.length);
	if(suffix == ".zip" || suffix == ".rar"){
		$("#documentImportForm #ysblj").val(ysblj);
		setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
	}else{
		$("#documentImportForm #fjid").val(fjid);
		$("#documentImportForm #prefjid").val(fjid);
		//1.初始化Table
		setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
	}
}

$(function () {
	//0.初始化fileinput,这是公用方法，调用comdefine.js里的方法
	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$('#documentImportForm #urlPrefix').val();
	//ctrlName,callback,impflg,singleFlg,fileName,importType
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存 3固定表头
	oFileInput.Init("documentImportForm","displayUpInfo",1,1,"imp_file","",sign_params);
	//2.初始化Button的点击事件
    var oimpButtonInit = new documentImp_ButtonInit();
	oimpButtonInit.Init();
});