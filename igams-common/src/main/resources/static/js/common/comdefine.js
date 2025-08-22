var commitAuditUrl = "/systemcheck/auditProcess/commCommitAudit";
var auditFlowChooseUrl = "/systemcheck/auditProcess/chooseFlow";
var auditUrl = "/systemcheck/auditProcess/audit";
var batchAuditUrl = "/systemcheck/auditProcess/batchAuditInfo";
var cancelAuditUrl = "/systemcheck/auditProcess/cancelAudit";
var auditViewUrl = "/systemcheck/auditProcess/auditView";
var exportSelectUrl = "/common/export/exportSelect";
var titleSelectUrl = "/common/title/commTitleSelectPrepare";
var exportSearchUrl = "/common/export/exportSearch";
var exportPrepareUrl = "/common/export/commExportPrepare";
var recallAuditUrl = "/systemcheck/auditProcess/recallAudit";
/**
 * 相同审核类型的数据，弹出提交审核Dialog
 * 参数说明：
 * type:审核类型
 * ids:string或Array：单个字符串id或者多个的话传数组
 * callback：回调函数
 **/
var showAuditFlowDialog = function(type,ids,callback,dialogParam,params,extend_1){
	var _idsArr = [];
	if(typeof ids =="string"){
		_idsArr.push(ids);
	}else{
		_idsArr = ids;
	}
	if (_idsArr.length == 0){
		$.alert('请选择您要操作的记录！');
		return;
	}
	if(!type){
		throw "缺少审核类型！";
		return;
	}
	var _data = {"shlb":type,"extend_1":JSON.stringify(_idsArr),"access_token":$("#ac_tk").val()};
	if(extend_1 && extend_1!=''){
		_data = $.extend({},_data,extend_1);
	}
	var _dialogParam = $.extend({},dialogParam);
	_dialogParam.data = $.extend({},_data,_dialogParam.data);
	jQuery.ajaxSetup({async:false});
	jQuery.post((params?(params.prefix?params.prefix:""):"") + commitAuditUrl,_data,function(responseText){
		setTimeout(function(){
			if(responseText){
				var popDialog = responseText.popDialog;
				var status = responseText.status;
				var _msg = responseText.message;
				if(popDialog===true){
					//if(responseText.popDialog===true){
						$.showDialog((params?(params.prefix?params.prefix:""):"") + auditFlowChooseUrl, '审批流程选择', 
							$.extend({}, getComCallbackConfig(callback,params, "auditFlowAjaxForm"), 
								{"width" : "500"}, _dialogParam));
					//}
				}else if(_msg.indexOf("成功") > -1){
					$.success(_msg,function() {
						if (callback) {
							callback(responseText, params)
						}
					});
				}else if(_msg.indexOf("失败") > -1){
					preventResubmitForm(".modal-footer > button", false);
					$.error(_msg);
				} else{
					preventResubmitForm(".modal-footer > button", false);
					$.alert(_msg);
				}
			}
		},1);
	},'json');
	jQuery.ajaxSetup({async:true});
};


//获取通用表单提交并回调的Config
//callback:回调函数，params：传给回调函数的参数，submitFormName：提交的表单id(默认ajaxForm)
var getComCallbackConfig = function(callback,params,submitFormName,f_preSubmitCheck){
	var randomModalName = "RandomModal"+new Date().getTime();
	return $.extend({},{
		width : "auto",
		modalName : randomModalName,
		buttons : {
		success : {
		label : "确定",
		className : "btn-primary",
		callback : function() {
		var _formId = submitFormName||'ajaxForm';
		var _formObj = $("#"+_formId);
		//设置弹出操作标示符
		var _flagObj = _formObj.find("[name=dialBtnOpe]");
		if(_flagObj.length==0){
			_flagObj = $('<input type="hidden" name="dialBtnOpe" />');
			_formObj.append(_flagObj);
		}
		_flagObj.val(true);

		if(f_preSubmitCheck!=null && f_preSubmitCheck!=""){
			var res =false;
			if(!eval(f_preSubmitCheck+"(randomModalName)"))
				return false;
		}

		submitForm(_formId, function (data) {
			var msg;
			//如果返回结果是字符串
			if(typeof data =="string"){
				msg = data;
				//如果返回结果为对象，从对象中获取提示信息
			}else{
				if (typeof data.result == "boolean") {
					msg = data.msg;
				} else {
					msg = data.result;
				}
			}
			if (msg.indexOf('成功') > - 1) {
				$.success(msg, function () {
					//关闭Modal
					jQuery.closeModal(randomModalName);
					//调用回调
					if(callback){
						callback(data,params);
					}
				});
			} else {
				if (msg.indexOf('失败') > - 1) {
					$.error(msg, function () {})
				} else {
					$.alert(msg, function () {});
				}
				preventResubmitForm(".modal-footer > button", false);
			}
		},".modal-footer > button");

		_flagObj.remove();//用完移除

		return false
	}
	},
	cancel : {
		label : "关闭",
		className : "btn-default"
	}
	}
	});
};

/**
 * 弹出单条数据的审核Dialog
 * 参数说明：
 * {
 * id:审核对象的id
 * url(String):查看页面url
 * type(String)：审核类别
 * title(String):弹出标题，可选
 * data(JSON):传递给url的参数对象
 * callback(Function)：审核成功后的回调函数：如刷新列表
 * dialogParam(JSON): 弹窗参数设置，可选
 * callbackParam(Object): 传递给回调的参数(一般不需要)，可选
 * }
 **/
var showAuditDialog = function(params){
	params = params||{};
	var _dialogParam = $.extend({},params.dialogParam);
	_dialogParam.data = $.extend({"ywid":params.id,'business_url':params.url,'shlb':params.type},params.data,_dialogParam.data);
	$.showDialog((params?(params.prefix?params.prefix:""):"") + auditUrl,params.title||'审核', $.extend({},getAuditCallbackConfig(params.callback,params.callbackParam,"auditAjaxForm",params.preSubmitCheck,{successBtn:"audit_confirm_btn"}),{"width":"900px"},_dialogParam));
};

/**
 * 弹出单条数据的审核Dialog（添加loading页面）
 * 参数说明：
 * {
 * id:审核对象的id
 * url(String):查看页面url
 * type(String)：审核类别
 * title(String):弹出标题，可选
 * data(JSON):传递给url的参数对象
 * callback(Function)：审核成功后的回调函数：如刷新列表
 * dialogParam(JSON): 弹窗参数设置，可选
 * callbackParam(Object): 传递给回调的参数(一般不需要)，可选
 * }
 **/
var showAuditDialogWithLoading = function(params){
	params = params||{};
	var _dialogParam = $.extend({},params.dialogParam);
	_dialogParam.data = $.extend({"ywid":params.id,'business_url':params.url,'shlb':params.type},params.data,_dialogParam.data);
	$.showDialog((params?(params.prefix?params.prefix:""):"") + auditUrl,params.title||'审核', $.extend({},getAuditCallbackConfigAddLoading(params.callback,params.callbackParam,"auditAjaxForm",params.preSubmitCheck,{successBtn:"audit_confirm_btn"}),{"width":"900px"},_dialogParam));
};
/**
 * 批量审核
 * @param params
 */
var batchAudit = function(params){
	params = params||{};
	var _dialogParam = $.extend({},params.dialogParam);
	_dialogParam.data = $.extend({"ywids":params.ids,'business_url':params.url,'shlb':params.type},params.data,_dialogParam.data);
	$.showDialog((params?(params.prefix?params.prefix:""):"") + batchAuditUrl,params.title||'批量审核', $.extend({},getAuditCallbackConfigAddLoading(params.callback,params.callbackParam,"auditAjaxForm",params.preSubmitCheck,{successBtn:"audit_confirm_btn"}),{"width":"900px"},_dialogParam));
};

/**
 * 审核回调Config
 * opeButtons:页面内部的操作按钮
 * {successBtn:"按钮ID"}
 */
var getAuditCallbackConfig = function(callback,params,submitFormName,f_preSubmitCheck,opeButtons){
	opeButtons = opeButtons||{}; 
	var _preventResubmitBtns = [".modal-footer > button"];
	for(var _btn in opeButtons){
		_preventResubmitBtns.push("#"+opeButtons[_btn]);
	}
	var _preventResubmitBtnStr = _preventResubmitBtns.join(",");
	var randomModalName = "RandomModal"+new Date().getTime();
	var _successFn = function() {
		if(!$("#auditAjaxForm").valid()){
			$.alert("请填写完整信息");
			return false;
		}
		if(f_preSubmitCheck!=null && f_preSubmitCheck!=""){
			if(!eval(f_preSubmitCheck+"()"))
				return false;
		}
		submitForm(submitFormName||'ajaxForm', function (responseText) {
			if(responseText.result===true){//成功
				$.success(responseText.msg,function() {
					//关闭Modal
					jQuery.closeModal(randomModalName);
					//调用回调
					if (callback) {
						callback(responseText, params)
					}
				});
			}else if(responseText.result===false){//失败
				$.error(responseText.msg);
			}else{//警告、部分成功
				$.alert(responseText.msg,function() {
					//关闭Modal
					jQuery.closeModal(randomModalName);
					//调用回调
					if (callback) {
						callback(responseText, params)
					}
				});
			}
			preventResubmitForm(_preventResubmitBtnStr, false);
		},_preventResubmitBtnStr);
		return false
	};
	var _dialogBtns = {
		cancel : {
			label : "关闭",
			className : "btn-default"
		}
	};
	if(!opeButtons.successBtn){
		_dialogBtns.success = {
			label : "确定",
			className : "btn-primary",
			callback : _successFn
		};
	}
	return $.extend({},{
		onShow: function(){
		if(opeButtons.successBtn){
			$("#"+opeButtons.successBtn).click(_successFn);
		}
	},
	width : "auto",
	modalName : randomModalName,
	buttons : _dialogBtns
	});
};

/**
 * 审核回调Config（添加加载中的页面）
 * opeButtons:页面内部的操作按钮
 * {successBtn:"按钮ID"}
 */
var getAuditCallbackConfigAddLoading = function(callback,params,submitFormName,f_preSubmitCheck,opeButtons){
	opeButtons = opeButtons||{}; 
	var _preventResubmitBtns = [".modal-footer > button"];
	for(var _btn in opeButtons){
		_preventResubmitBtns.push("#"+opeButtons[_btn]);
	}
	var _preventResubmitBtnStr = _preventResubmitBtns.join(",");
	var randomModalName = "RandomModal"+new Date().getTime();
	var _successFn = function() {
		var sftg = $("#auditAjaxForm #sftg").val();
		var thlcxh = $("#auditAjaxForm #shxxThlcxh").val();
		if(sftg == null || sftg == ""){
			$.alert("请选择审核结果！")
			return false;
		}else if ( sftg == "0" && thlcxh ==""){
			$.alert("请选择退回的对象！")
			return false;
		}
		if(!$("#auditAjaxForm").valid()&&sftg == "1"){
			$.alert("请填写完整信息");
			return false;
		}
		if(f_preSubmitCheck!=null && f_preSubmitCheck!=""){
			if(!eval(f_preSubmitCheck+"()"))
				return false;
		}
		//获取当前系统时间戳来保证唯一性
		var timestamp = new Date().getTime();
		$("#auditAjaxForm #audit_loadYmCode").val(timestamp);
		$("#auditAjaxForm #access_token").val($("#ac_tk").val());
		var count = 1;
		//添加加载中状态
		//创建objectNode
		var cardiv =document.createElement("div");
		cardiv.id="cardiv";
		var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="position: absolute;top:50%;left:50%;margin-left:-73px;margin-top:-40px;text-align: center;"><img src="/images/loading-2.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + count + '</span></p><p class="padding_t7"></p></div></div>';
		cardiv.innerHTML=s_str;
		//将上面创建的P元素加入到BODY的尾部
		document.body.appendChild(cardiv);
		submitForm(submitFormName||'ajaxForm', function (responseText) {
			document.body.removeChild(cardiv);
			if(responseText["status"] == 'success'){
				var i = 5;
				var interval = setInterval(function(){
					$("#successModal #btn_ok").text("确 定  "+i);
					if(i == 0){
						clearInterval(interval);
						$.closeModal($("#successModal").attr("name"));
						$.closeModal(randomModalName);
						//调用回调
						if (callback) {
							callback(responseText, params)
						}
					}
					i--;
				},1000);
				$.success(responseText["message"],function() {
					clearInterval(interval);
					$.closeModal(randomModalName);
					//调用回调
					if (callback) {
						callback(responseText, params)
					}
				});
			}else if(responseText["status"] == "fail"){
				$.error(responseText["message"],function() {
				});
			} else{
				$.alert(responseText["message"],function() {
				});
			}
			preventResubmitForm(_preventResubmitBtnStr, false);
		},_preventResubmitBtnStr);
		//定时查看审核已审核数量，并修改页面数据
		//setTimeout("checkAuditStatus('"+ timestamp + "',1000,"+callback+",'"+randomModalName+"',"+params+")",1);
		return false
	};
	var _dialogBtns = {
		cancel : {
			label : "关闭",
			className : "btn-default"
		}
	};
	if(!opeButtons.successBtn){
		_dialogBtns.success = {
			label : "确定",
			className : "btn-primary",
			callback : _successFn
		};
	}
	return $.extend({},{
		onShow: function(){
		if(opeButtons.successBtn){
			$("#"+opeButtons.successBtn).click(_successFn);
		}
	},
	width : "auto",
	modalName : randomModalName,
	buttons : _dialogBtns
	});
};

/**
 * 防重复提交FORM
 * @param btnDom 需要失效的按钮DOM对象
 * @param flag 传1表示按钮可操作，其它表示不可操作;
 * 	不传且表单验证通过，不可再操作
 * @return
 * @author goofus
 */
function preventResubmitForm(btnDomSelector, flag, formID){
	//console.log(btnDomSelector);
	var btnDom = $(btnDomSelector);
	if(btnDom && flag != undefined && flag != null){
		if(!flag){
			btnDom.prop("disabled",false);
		}else{
			btnDom.prop("disabled","disabled");
		}
	}else if(btnDom) {
		btnDom.prop("disabled","disabled");
	
		setTimeout(function(){
			var _form = null;
			if(formID) 
				_form = $("#"+formID);
			else
				return;
			if(_form && !_form.valid())
				btnDom.prop("disabled",false);//若表单验证通过，不可再提交
		},1000);//无论表单验证是否通过，至少等待1秒
	}
}
/**
 * 查看审核信息
 * @param xmid
 * @param event
 * @param shlb
 */
function showAuditInfo(ywid,event,shlb,params){
	if(shlb.split(",").length>1){
		$.showDialog(
			(params?(params.prefix?params.prefix:""):"") + auditViewUrl,
			'审核信息',
			$.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlbs':shlb}})
		);
	}else{
		$.showDialog(
			(params?(params.prefix?params.prefix:""):"") + auditViewUrl,
			'审核信息',
			$.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlb':shlb}})
		);
	}
	event.stopPropagation();
}

var viewConfig = {
	width: "960px",
	modalName: "viewModal",
	buttons:{
		cancel : {
			label : "关闭",
			className : "btn-default"
		}
	}
};

//初始化fileinput
var FileInput = function () {
	var oFile = new Object();
	//初始化fileinput控件（第一次初始化）
	//参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
	//impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
	//importType用于区分固定表头的导入类别
	//ywlxIdName id的名称
	oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName,importType,params,ywlxIdName) {
		var control = $('#' + ctrlName + ' #' + fileName);
		var filecnt = 0;
		//初始化上传控件的样式
		control.fileinput({
			language: 'zh', //设置语言
			uploadUrl: (params?(params.prefix?params.prefix:""):"")+"/common/file/saveImportFile?access_token="+$("#ac_tk").val()+"&suffix="+(params?(params.suffix?params.suffix:""):""), //上传的地址
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
				if (ywlxIdName){
					obj.ywlx = $("#"+ ctrlName + " #"+ywlxIdName).val();
				}else {
					obj.ywlx = $("#"+ ctrlName + " #ywlx").val();
				}
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
        	if(fileName == 'doc_file' || fileName == 'pro_file' || fileName == 'sign_file'|| fileName=='report_file'){
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
					eval(callback+"('"+ data.response.fjcfbDto.fjid+"')");
				}
			} else {
				// 失败回调
				
				var msg = data.response.msg;
				if(msg!=null && msg!=""){
					$.error(msg,function() {
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
						
					});
				}else{
					$.error("请求失败，请稍后重试",function() {
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
					});
				}				
			}
		})
	}
	return oFile;
};

/**
 * 单个取消审核操作
 * 参数说明：
 * ids(String):单个字符串id
 * callback(Function)：取消成功后的回调函数：如刷新列表
 * type(String)：审核类别
 */
var cancelAudit = function(id,callback,type,params){
	var _idsArr = [];
	if(typeof id =="string"){
		_idsArr.push(id);
	}else{
		_idsArr = id;
	}
	if (_idsArr.length != 1){
		$.alert('请选择一条您要操作的记录！');
		return;
	}
	$.confirm('您确定要取消审批吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			jQuery.post((params?(params.prefix?params.prefix:""):"") + cancelAuditUrl,{"shxxid":_idsArr[0].shxx_shxxid,'shlb':type,"access_token":$("#ac_tk").val()},function(responseText){
				if(responseText){
					setTimeout(function(){
						var status = responseText.status;
						var _msg = responseText.message;
						if(_msg.indexOf("成功") > -1){
							$.success(_msg,function() {
								if (callback) {
									callback(responseText)
								}
							});
						}else if(_msg.indexOf("失败") > -1){
							$.error(_msg);
						} else{
							$.alert(_msg);
						}
					},10);
				}
			},'json');
			jQuery.ajaxSetup({async:true});
		}
	});
};


/**
 * 客户端提交撤回功能
 * 参数说明：
 * type:审核类型
 * ids:string或Array：单个字符串id或者多个的话传数组
 * callback：回调函数
 **/
var doAuditRecall = function(type,ids,callback,params){
	var _idsArr = [];
	if(typeof ids =="string"){
		_idsArr.push(ids);
	}else{
		_idsArr = ids;
	}
	if (_idsArr.length == 0){
		$.alert('请选择您要操作的记录！');
		return;
	}
	if(!type){
		throw "缺少审核类型！";
		return;
	}
	var _data = {"shlb":type,"ids":ids,"access_token":$("#ac_tk").val()};
	jQuery.ajaxSetup({async:false});
	jQuery.post((params?(params.prefix?params.prefix:""):"") + recallAuditUrl,$.extend({},_data),function(responseText){
		setTimeout(function(){
			if(responseText){
				var _msg = responseText.msg;
				if(_msg==null){
					$.error("无错误信息");
				}else if(_msg.indexOf("成功") > -1){
					$.success(_msg,function() {
						if (callback) {
							callback(responseText, params)
						}
					});
				}else if(_msg.indexOf("失败") > -1){
					$.error(_msg);
				} else{
					$.alert(_msg);
				}
			}
		},1);
	},'json');
	jQuery.ajaxSetup({async:true});
};


var doAuditRecallnoback = function(type,ids,callback,params){
	var _idsArr = [];
	if(typeof ids =="string"){
		_idsArr.push(ids);
	}else{
		_idsArr = ids;
	}
	if (_idsArr.length == 0){
		$.alert('请选择您要操作的记录！');
		return;
	}
	if(!type){
		throw "缺少审核类型！";
		return;
	}
	var _data = {"shlb":type,"ids":ids,"access_token":$("#ac_tk").val()};
	jQuery.ajaxSetup({async:false});
	jQuery.post((params?(params.prefix?params.prefix:""):"") + recallAuditUrl,$.extend({},_data),function(responseText){
		setTimeout(function(){
			if(responseText){
				var _msg = responseText.msg;
				if(_msg==null){
					$.error("无错误信息");
				}else if(_msg.indexOf("成功") > -1){

				}else if(_msg.indexOf("失败") > -1){
					$.error(_msg);
				} else{
					$.alert(_msg);
				}
			}
		},1);
	},'json');
	jQuery.ajaxSetup({async:true});
};