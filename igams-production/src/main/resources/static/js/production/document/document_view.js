//事件绑定
function btnBind(){
}

var viewDocumentCompareConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var vm = new Vue({
	el:'#document_view',
	data: {
	},
	methods: {
		view(fjid,hzm){
			if(hzm.toLowerCase()=="pdf"){
				var url= $('#viewDoc_ajaxForm #urlPrefix').val() + "/common/file/pdfPreview?fjid=" + fjid +"&pdf_flg="+"1";
				$.showDialog(url,'文件预览',viewPreViewConfig);
			}else if(hzm.toLowerCase()=="jpg" || hzm.toLowerCase()=="jpeg" || hzm.toLowerCase()=="jfif" || hzm.toLowerCase()=="png"){
				var url=$('#viewDoc_ajaxForm #urlPrefix').val() + "/ws/pripreview?fjid="+fjid
				$.showDialog(url,'图片预览',viewPreViewConfig);
			}else if(hzm.toLowerCase()=="doc" || hzm.toLowerCase()=="ppt" || hzm.toLowerCase()=="pptx" || hzm.toLowerCase()=="docx" || hzm.toLowerCase()=="xls"|| hzm.toLowerCase()=="xlsx"){
				var url= $('#viewDoc_ajaxForm #urlPrefix').val() + "/common/file/pdfPreview?fjid=" + fjid +"&pdf_flg="+"1";
				$.showDialog(url,'文件预览',viewPreViewConfig);
			}else{
				$.alert("暂不支持其他文件的预览，敬请期待！");
			}
			
		},
		xz(fjid){
    		jQuery('<form action="'+$('#viewDoc_ajaxForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		shzt(wjid){
			var url = $('#viewDoc_ajaxForm #urlPrefix').val() + "/systemcheck/auditProcess/auditView?ywid="+wjid+"&access_token="+$("#ac_tk").val();
			$.showDialog(url,'查看审核',viewDocumentCompareConfig);
		},
		updateTime(wjid){
			var url = $('#viewDoc_ajaxForm #urlPrefix').val() + "/production/document/timeUpdateDocument?wjid=" + wjid + "&prezhgz="+$("#viewDoc_ajaxForm #zhgz").val()+"&presfth="+$("#viewDoc_ajaxForm #sfth").val()+"&access_token="+$("#ac_tk").val();
			$.showDialog(url,'更新时间',viewDocumentUpdateTimeConfig);
		}
	}
})
var viewwordConfig = {
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
var viewPreViewConfig = {
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

function viewdoc(fjid){
	var url=$('#viewDoc_ajaxForm #urlPrefix').val()+"/ws/file/wordPreview?fjid="+fjid;
	$.showDialog(url,'文件预览',viewwordConfig);
}

var viewDocumentUpdateTimeConfig = {
	width		: "800px",
	modalName	: "viewDocumentUpdateTimeModel",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$("#viewDoc_ajaxForm #"+ $("#ajaxForm #wjid").val() +" span[name='viewshsj']").text(responseText["wjglDto"].shsj);
						$("#viewDoc_ajaxForm #"+ $("#ajaxForm #wjid").val() +" span[name='viewpzsj']").html(responseText["wjglDto"].pzsj);
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchDocumentResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function initPage(){
}
var document_TableInit = function () {
	//初始化Table
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#viewDoc_ajaxForm #glwj_list").bootstrapTable({
			url: $("#viewDoc_ajaxForm #urlPrefix").val()+'/production/document/pagedataGetGlWj',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#viewDoc_ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "wjgl.wjbh",				//排序字段
			sortOrder: "desc",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "zwjid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			}, {
				field: 'wjbh',
				title: '文件编号',
				titleTooltip:'文件编号',
				width: '15%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'wjmc',
				title: '文件名称',
				titleTooltip:'文件名称',
				width: '30%',
				align: 'left',
				visible: true
			}, {
				field: 'bbh',
				title: '版本',
				titleTooltip:'版本',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'sxrq',
				title: '生效日期',
				titleTooltip:'生效日期',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: '',
				title: '附件',
				titleTooltip:'附件',
				width: '8%',
				align: 'left',
				formatter:fjformat,
				visible: true
			}],
			onLoadSuccess:function(map){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "wjgl.wjbh", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			wjid: $("#viewDoc_ajaxForm #wjid").val() // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}


//下载模板
function xz(fjid){
	jQuery('<form action="' + $("#viewDoc_ajaxForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
function fjformat(value,row,index) {
	return "<a href='javascript:void(0);' onclick=\"viewWjfj('"+row.zwjid+"')\">"+"查看"+"</a>";
}

function viewWjfj(ywid) {
	var url="/agreement/agreement/pagedataViewFj?access_token=" + $("#ac_tk").val()+"&ywid="+ywid+"&ywlx="+"IMP_DOCUMENT";
	$.showDialog($("#viewDoc_ajaxForm #urlPrefix").val()+url, '查看附件', viewFileConfig);
}
var viewFileConfig = {
	width : "800px",
	height : "500px",
	modalName	: "uploadFileModal",
	formName	: "view_ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
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

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#viewDoc_ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#viewDoc_ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var viewPreViewConfig = {
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
var train_TableInit = function () {
	//初始化Table
	var oTableInit=new Object();
	oTableInit.Init=function(){
		var urlPrefix = $("#viewDoc_ajaxForm #urlPrefix").val();
		if (urlPrefix!=null&&urlPrefix!=''){
			urlPrefix= '/matridxhrm';
		}else {
			urlPrefix = '';
		}
		$("#viewDoc_ajaxForm #pxxx_list").bootstrapTable({
			//目前先写死查询生物培训信息
			url: urlPrefix+'/train/train/pagedataGetTrainByWj',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#viewDoc_ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "pxgl.pxbt",				//排序字段
			sortOrder: "ASC",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "pxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			}, {
				field: 'pxbt',
				title: '培训标题',
				titleTooltip:'培训标题',
				width: '15%',
				align: 'left',
				visible: true,
				sortable: true,
				formatter:pxbtformat
			}, {
				field: 'pxlbmc',
				title: '培训类别',
				titleTooltip:'培训类别',
				width: '30%',
				align: 'left',
				visible: true
			}, {
				field: 'pxzlbmc',
				title: '培训子类别',
				titleTooltip:'培训子类别',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'gqsj',
				title: '过期时间',
				titleTooltip:'过期时间',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}],
			onLoadSuccess:function(map){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			access_token:$("#ac_tk").val(),
			glid: $("#viewDoc_ajaxForm #wjid").val() // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}
function pxbtformat(value,row,index){
	var html = "";
	if(row.pxid==null){
		html += "<span>"+row.pxbt+"</span>"
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByPxbt('"+row.pxid+"')\">"+row.pxbt+"</a>";
	}
	return html;
}
function queryByPxbt(pxid){
	var urlPrefix = $("#viewDoc_ajaxForm #urlPrefix").val();
	if (urlPrefix!=null&&urlPrefix!=''){
		urlPrefix= '/matridxhrm';
	}else {
		urlPrefix = '';
	}
	var url=urlPrefix+"/train/test/viewTrain?pxid="+pxid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'培训信息',viewTrainConfig);
}
var viewTrainConfig = {
	width		: "1400px",
	modalName	:"viewTrainModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

$(document).ready(function(){
	var timeStr = $("#document_formSearch #btn_time span").text();
	if(timeStr == ""){
		$("#viewDoc_ajaxForm button[name='timeButton']").hide();
	}
    //所有下拉框添加choose样式
	jQuery('#viewDoc_ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	var oTable = new document_TableInit();
	oTable.Init();
	var train_oTable = new train_TableInit();
	train_oTable.Init();
});