var document_turnOff=true;

var document_TableInit = function () {
    var oTableInit = new Object();
    $('#tab_012001').on('scroll',function(){
    	if($('#document_formSearch #tb_list').offset().top - 122 < 0){
    		if(!$('#document_formSearch .js-affix').hasClass("affix"))
    		{
    			$('#document_formSearch .js-affix').removeClass("affix-top").addClass("affix");
    		}
    		$('#document_formSearch .js-affix').css({
		    	'top': '100px',
		        "z-index":1000,
		        "width":'100%'
		      });
    	}else{
    		if(!$('#document_formSearch .js-affix').hasClass("affix-top"))
    		{
    			$('#document_formSearch .js-affix').removeClass("affix").addClass("affix-top");
    		}
    		$('#document_formSearch .js-affix').css({
		    	'top': '0px',
		        "z-index":1,
		        "width":'100%'
		      });
    	}
    })
    // 初始化Table
    oTableInit.Init = function () {
        $('#document_formSearch #tb_list').bootstrapTable({
            url: $('#document_formSearch #urlPrefix').val() + $('#document_formSearch #formAction').val(),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#document_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"tmp.lrsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            }, {
				field: 'no',
				title: '序号',
				titleTooltip:'序号',
				width: '6%',
				align: 'left',
				visible: true,
				formatter:xhFormat
			}, {
				field: 'wjid',
				title: '文件ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'glwjid',
				title: '关联文件ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'ywjid',
				title: '原文件ID',
				titleTooltip:'原文件ID',
				width: '7%',
				align: 'left',
				visible: false
			}, {
				field: 'wjbh',
				title: '文件编号',
				titleTooltip:'文件编号',
				width: '12%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'wjmc',
				title: '文件名称',
				titleTooltip:'文件名称',
				width: '29%',
				align: 'left',
				formatter:wjmcFormat,
				visible: true
			}, {
				field: 'wjflmc',
				title: '文件分类',
				titleTooltip:'文件分类',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'wjlbmc',
				title: '文件类别',
				titleTooltip:'文件类别',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'bbh',
				title: '版本',
				titleTooltip:'版本',
				width: '7%',
				align: 'left',
				visible: true
			}, {
				field: 'jgmc',
				title: '所属单位',
				titleTooltip:'所属单位',
				width: '9%',
				align: 'left',
				visible: true
			}, {
				field: 'sxrq',
				title: '生效日期',
				titleTooltip:'生效日期',
				width: '9%',
				align: 'left',
				visible: true,
				sortable: true
			}, /*{
				field: 'bzsj',
				title: '编制时间',
				titleTooltip:'编制时间',
				width: '9%',
				align: 'left',
				visible: true,
				sortable: true
			},*/ {
				field: 'zt',
				title: '审核状态',
				titleTooltip:'审核状态',
				width: '6%',
				align: 'left',
				formatter:wjztFormat,
				visible: true
			}, {
				field: 'shsj',
				title: '审核时间',
				titleTooltip:'审核时间',
				width: '9%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'pzsj',
				title: '批准时间',
				titleTooltip:'批准时间',
				width: '9%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'rwjd',
				title: '任务进度',
				titleTooltip:'任务进度',
				width: '9%',
				align: 'left',
				formatter:rwjdFormat,
				visible: true
			}, {
				field: 'bz',
				title: '备注',
				titleTooltip:'备注',
				width: '12%',
				align: 'left',
				visible: false
			}, {
				field: 'ffzt',
				title: '发放状态',
				titleTooltip:'发放状态',
				width: '8%',
				align: 'left',
				formatter:ffztFormat,
				visible: true
			}, {
				field: 'scbj',
				title: '状态',
				titleTooltip:'状态',
				width: '5%',
				align: 'left',
				formatter:scbjFormat,
				visible: true
			}, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {

            },
            onDblClickRow: function (row, $element) {
            	documentDealById(row.wjid, 'view',$("#document_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#document_formSearch #tb_list").colResizable(
    		{
                liveDrag:true, 
                gripInnerHtml:"<div class='grip'></div>", 
                draggingClass:"dragging", 
                resizeMode:'fit', 
                postbackSafe:true,
                partialRefresh:true,
                pid:"document_formSearch"
    		}
        );
    };

    // 得到查询的参数
    oTableInit.queryParams = function(params){
    	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	// 例如 toolbar 中的参数
    	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
    	// limit, offset, search, sort, order
    	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
    	// 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "tmp.wjid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
    	
    	return getDocumentSearchData(map);
    };
    return oTableInit;
};

function getDocumentSearchData(map){
	var cxtj = $("#document_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#document_formSearch #cxnr').val());
	// '0':'文件编码','1':'文件名称','3':'版本号','4':'跟踪记录','5':'备注'
	if (cxtj == "0") {
		map["wjbh"] = cxnr;
	}else if (cxtj == "1") {
		map["wjmc"] = cxnr;
	}else if (cxtj == "3") {
		map["bbh"] = cxnr;
	}else if (cxtj == "4") {
		map["gzjl"] = cxnr;
	}else if (cxtj == "5") {
		map["bz"] = cxnr;
	}else if (cxtj == "6") {
		map["jgmc"] = cxnr;
	}
	var jsid = $("#document_formSearch #jsid").val();
	map["jsid"] = jsid;
	var yhid = $("#document_formSearch #yhid").val();
	map["yhid"] = yhid;
	var fwbj = jQuery('#document_formSearch #fwbj').val();
	map["fwbj"] = fwbj;
	// 文件分类
	var wjfl = jQuery('#document_formSearch #wjfl_id_tj').val();
	var wjfls_str=$('#document_formSearch #wjfls_str').val();
	if(wjfls_str){
		if(!wjfl){
			wjfl=wjfls_str;
		}
	}
	map["wjfls"] = wjfl;
	// 文件类别
	var wjlb = jQuery('#document_formSearch #wjlb_id_tj').val();
	map["wjlbs"] = wjlb;
	
	// 审核状态
	var zts = jQuery('#document_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	
	// 使用状态
	var scbjs = jQuery('#document_formSearch #scbj_id_tj').val();
	map["scbjs"] = scbjs;

	// 发放状态
	var ffzts = jQuery('#document_formSearch #ffzt_id_tj').val();
	map["ffzts"] = ffzts;
	
	// 生效开始日期
	var sxrqstart = jQuery('#document_formSearch #sxrqstart').val();
	map["sxrqstart"] = sxrqstart;
	
	// 生效结束日期
	var sxrqend = jQuery('#ngs_formSearch #sxrqend').val();
	map["sxrqend"] = sxrqend;
	
	// 编制开始日期
	var bzsjstart = jQuery('#document_formSearch #bzsjstart').val();
	map["bzsjstart"] = bzsjstart;
	
	// 编制结束日期
	var bzsjend = jQuery('#document_formSearch #bzsjend').val();
	map["bzsjend"] = bzsjend;
	return map;
}
//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#document_formSearch #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#document_formSearch #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}
//使用状态（删除标记）格式化
function scbjFormat(value,row,index) {
    if (row.scbj == '2') {
        return '停用';
    }else if (row.scbj == '3') {
        return '作废';
    }else{
        return '正常';
    }
}//发放状态格式化
function ffztFormat(value,row,index) {
	if(row.ffzt=='1'){
		return "<span style='color:green;'>全部发放</span>";
	}else if(row.ffzt=='2'){
		return "<span style='color:orange;'>部分发放</span>";
	}else if(row.ffzt=='0'){
		return "<span style='color:red;'>未发放</span>";
	}else {
		return '';
	}
}

function wjmcFormat(value,row,index) {
    var cs =$("#document_formSearch #cs").val();
    if(cs && cs=='1'){
        return "<a href='javascript:void(0);' onclick='ylFun(\""+row.num+"\",\""+row.wjm+"\",\""+row.fjid+"\",\""+row.wjid+"\")'>"+row.wjmc+"</a>";
    }else{
        return row.wjmc;
    }
}


function ylFun(num,wjm,fjid,wjid){
    if(num){
        if(num=='1'){
            var begin=wjm.lastIndexOf(".");
            var end=wjm.length;
            var type=wjm.substring(begin,end);
            if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
                var url=$("#document_formSearch #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
                $.showDialog(url,'图片预览',viewPreViewConfig);
            }else if(type.toLowerCase()==".pdf"){
                var url= $("#document_formSearch #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
                $.showDialog(url,'文件预览',viewPreViewConfig);
            }else {
                $.alert("暂不支持其他文件的预览，敬请期待！");
            }
        }else{
            $.showDialog($('#document_formSearch #urlPrefix').val()+'/production/document/pagedataGetUploadFile?wjid='+wjid,'文件查看',viewDocumentConfig);
        }
    }
}

var viewPreViewConfig = {
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
 * 文件列表的状态格式化函数
 * @returns
 */
function wjztFormat(value,row,index) {
	var param = {prefix:$('#document_formSearch #urlPrefix').val()};
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.wjid + "\",event,\"QUALITY_FILE_ADD,QUALITY_FILE_MOD\",{prefix:\"" + $('#document_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {

		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.wjid + "\",event,\"QUALITY_FILE_ADD,QUALITY_FILE_MOD\",{prefix:\"" + $('#document_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{

		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.wjid + "\",event,\"QUALITY_FILE_ADD,QUALITY_FILE_MOD\",{prefix:\"" + $('#document_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
/**
 * 任务进度格式化函数
 * @returns
 */
function rwjdFormat(value,row,index) {
	return "<a href='javascript:void(0);' onclick=\"documentDealById('" + row.wjid + "', 'viewRwjd','/production/document/pagedataViewRwjd')\" >"+row.rwjd+"</a>";
}


//撤回项目提交
function recallDocument(wjid,ywjid,event){
	var auditType = $("#document_formSearch #auditType").val();
	var auditModType = $("#document_formSearch #auditModType").val();
	var msg = '您确定要撤回该文件吗？';
	var document_params=[];
	document_params.prefix=$('#document_formSearch #urlPrefix').val();
	$.confirm(msg,function(result){
		if(result){
			if(ywjid && ywjid != "null"){
				doAuditRecall(auditModType,wjid,function(){
					searchDocumentResult();
				},document_params);
			}else{
				doAuditRecall(auditType,wjid,function(){
					searchDocumentResult();
				},document_params);
			}
		}
	});
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallDocument('" + row.wjid + "','" + row.ywjid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//按钮动作函数
function documentDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#document_formSearch #urlPrefix').val() + tourl; 
	if(action =='view'){
		var url= tourl + "?wjid=" +id;
		$.showDialog(url,'查看文件',viewDocumentConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增文件',addDocumentConfig);
	}else if(action =='mod'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'编辑文件',modDocumentConfig);
	}else if(action =='advancedmod'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'编辑文件',modDocumentConfig);
	}else if(action =='revise'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'文件修订',reviseDocumentConfig);
	}else if(action =='time'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'更新时间',modDocumentTimeConfig);
	}else if(action =='viewRwjd'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'查看任务进度',viewDocumentRwjdConfig);
	}else if(action =='submit'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'提交审核',submitDocumentConfig);
	}else if(action =='qaadd'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'新增文件',addDocumentConfig);
	}else if(action =='batchpermit'){
	    var url=tourl;
	    if(id.indexOf(",")!=-1){
		    url=url + "?ids=" +id;
	    }else{
	        url=url + "?wjid=" +id + "&ids=" +id;
	    }
		$.showDialog(url,'批量设置权限',batchPermitDocumentConfig);
	}else if(action =='viewmore'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'详细查看文件',viewDocumentConfig);
	}else if(action =='transform'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'文件转换',transformConfig);
	}else if(action =='predownload'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'原文件下载',predownloadConfig);
	}else if(action =='reldocument'){
		var url=tourl + "?wjid=" +id;
		$.showDialog(url,'选择文件新版本',reldocumentConfig);
	}else if(action =='printgrant'){
		var url= tourl+ "?wjid=" +id;
		$.showDialog(url,'打印发放',printgrantDocumentConfig);
	}else if(action =='bulkimports'){
        var url= tourl;
        $.showDialog(url,'批量导入',bulkimportsDocumentConfig);
	}
}

var bulkimportsDocumentConfig = {
	width		: "1000px",
	modalName	: "bulkimportsModal",
	formName	: "bulkimportsForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#bulkimportsForm #sqbm").val()){
					$.error("请选择部门！");
					return false;
				}
				if(!$("#bulkimportsForm").valid()){
					$.error("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var wjlbcskz2 = $("#bulkimportsForm #wjlb").find("option:selected").attr("cskz2");
				$("#bulkimportsForm #wjlbcskz2").val(wjlbcskz2);
				$("#bulkimportsForm input[name='access_token']").val($("#ac_tk").val());
				var cskz3 = $("#bulkimportsForm #wjlb").find("option:selected").attr("cskz3");
				if (cskz3=="null"){
					cskz3='';
				}
				submitForm(opts["formName"]||"bulkimportsForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
                        var document_params=[];
                        document_params.prefix=responseText["urlPrefix"];
                        var auditType = responseText["auditType"];
                        var extend_2={"extend_2":cskz3};
                        var ywids = responseText["ywids"];
                        if(ywids){
                            var ywidList = ywids.split(",");
                            showAuditFlowDialog(auditType,ywidList,function(){
                                searchDocumentResult();
                            },null,document_params,extend_2);
                        }
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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


var printgrantDocumentConfig = {
	width		: "900px",
	modalName	: "printgrantModal",
	formName	: "printgrantForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#printgrantForm #ffmx_json").val(JSON.stringify(t_map.rows));
				$("#printgrantForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"printgrantForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchDocumentResult();
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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
var viewDocumentConfig = {
	width		: "1000px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var viewDocumentRwjdConfig = {
	width		: "900px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var addDocumentConfig = {
	width		: "1000px",
	modalName	: "addDocumentModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm #sqbm").val()){
					$.error("请选择部门！");
					return false;
				}
				if(!$("#ajaxForm").valid()){
					$.error("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				let json = [];
				if(t_map.rows != null && t_map.rows.length > 0) {
					for (let i = 0; i < t_map.rows.length; i++) {
						var sz = {"index":'',"zwjid":''};
						sz.index=t_map.rows.length;
						sz.zwjid = t_map.rows[i].zwjid;
						json.push(sz);
					}
				}
				var wjlbcskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
				$("#ajaxForm #wjlbcskz2").val(wjlbcskz2);
				$("#ajaxForm #glwj_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				var cskz3 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz3");
				if (cskz3=="null"){
					cskz3='';
				}
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
						var document_params=[];
						document_params.prefix=$('#document_formSearch #urlPrefix').val();
						var auditType = $("#document_formSearch #auditType").val();
						var extend_2={"extend_2":cskz3};
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							searchDocumentResult();
						},null,document_params,extend_2);
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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

var modDocumentConfig = {
	width		: "1000px",
	modalName	: "modDocumentModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm #sqbm").val()){
					$.error("请选择部门！");
					return false;
				}
				if(!$("#ajaxForm").valid()){
					$.error("请填写完整信息！");
					return false;
				}
				if(!$("#ajaxForm #wj_qwwcsj").val() && $("#ajaxForm #jsids").val() && $("#ajaxForm input[name=scbj]:checked").val() == '0'){
					$.error("请选择期望完成时间！");
					return false;
				}
				let json = [];
				if(t_map.rows != null && t_map.rows.length > 0) {
					for (let i = 0; i < t_map.rows.length; i++) {
						var sz = {"index":'',"zwjid":''};
						sz.index=t_map.rows.length;
						sz.zwjid = t_map.rows[i].zwjid;
						json.push(sz);
					}
				}
				var wjlbcskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
				$("#ajaxForm #wjlbcskz2").val(wjlbcskz2);
				$("#ajaxForm #glwj_json").val(JSON.stringify(json));
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchDocumentResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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

var reviseDocumentConfig = {
	width		: "1000px",
	modalName	: "reviseDocumentModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm #sqbm").val()){
					$.error("请选择部门！");
					return false;
				}
				if(!$("#ajaxForm").valid()){
					$.error("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				let json = [];
				if(t_map.rows != null && t_map.rows.length > 0) {
					for (let i = 0; i < t_map.rows.length; i++) {
						var sz = {"index":'',"zwjid":''};
						sz.index=t_map.rows.length;
						sz.zwjid = t_map.rows[i].zwjid;
						json.push(sz);
					}
				}
				var wjlbcskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
				var cskz3=$("#ajaxForm #wjlb").find("option:selected").attr("cskz3");
				if (cskz3=="null"){
					cskz3='';
				}
				$("#ajaxForm #wjlbcskz2").val(wjlbcskz2);
				$("#ajaxForm #glwj_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
							//提交审核
							var document_params=[];
							document_params.prefix=$('#document_formSearch #urlPrefix').val();
							var auditModType = $("#document_formSearch #auditModType").val();
							var extend_2={"extend_2":cskz3};
							showAuditFlowDialog(auditModType,responseText["ywid"],function(){
								searchDocumentResult();
							},null,document_params,extend_2);
						}
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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

var modDocumentTimeConfig = {
	width		: "800px",
	modalName	: "modDocumentTimeModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.error("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchDocumentResult();
							}
						});
						
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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


var submitDocumentConfig = {
	width		: "1000px",
	modalName	: "submitDocumentConfig",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.error("请填写完整信息！");
					return false;
				}
				if(!$("#ajaxForm #sqbm").val()){
					$.error("请选择部门！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				let json = [];
				if(t_map.rows != null && t_map.rows.length > 0) {
					for (let i = 0; i < t_map.rows.length; i++) {
						var sz = {"index":'',"zwjid":''};
						sz.index=t_map.rows.length;
						sz.zwjid = t_map.rows[i].zwjid;
						json.push(sz);
					}
				}
				var wjlbcskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
				$("#ajaxForm #wjlbcskz2").val(wjlbcskz2);
				$("#ajaxForm #glwj_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				var cskz3=$("#ajaxForm #wjlb").find("option:selected").attr("cskz3");
				if (cskz3=="null"){
					cskz3='';
				}
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							var wjid=$("#ajaxForm #wjid").val();
							var gwjid=$("#ajaxForm #gwjid").val();
							if(wjid!=gwjid){
								//修订提交审核
								var document_params=[];
								document_params.prefix=$('#document_formSearch #urlPrefix').val();
								var auditModType = $("#document_formSearch #auditModType").val();
								var extend_2={"extend_2":cskz3};
								showAuditFlowDialog(auditModType,responseText["ywid"],function(){
									searchDocumentResult();
								},null,document_params,extend_2);
							}else{
								//新增提交审核
								var document_params=[];
								document_params.prefix=$('#document_formSearch #urlPrefix').val();
								var auditType = $("#document_formSearch #auditType").val();
								var extend_2={"extend_2":cskz3};
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									searchDocumentResult();
								},null,document_params,extend_2);
							}
							$.closeModal(opts.modalName);
						}
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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
var batchPermitDocumentConfig = {
	width		: "900px",
	modalName	: "batchPermitDocumentModal",
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
				if(!$("#ajaxForm #wj_qwwcsj").val() && $("#ajaxForm #jsids").val()){
					$.error("请选择期望完成时间！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchDocumentResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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

var transformConfig = {
	width		: "800px",
	modalName	: "transformModal",
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

var predownloadConfig = {
	width		: "800px",
	modalName	: "predownloadModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//关联用户信息列表模态框
var reldocumentConfig = {
	width		: "800px",
	modalName	: "reldocumentModal",
	height		: "00px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#reldocformSearch #reldoc_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length!=1){
	    			$.error("请选中一行");
	    			if(!$("#ajaxForm").valid()){
						return false;
					}
	    		}
				$.confirm('您确定以选择文件作为新版本吗？',function(result){
	    			if(result){
	    				jQuery.ajaxSetup({async:false});
	    				var prewjid = $("#reldocformSearch #prewjid").val();
	    				var pregwjid = $("#reldocformSearch #pregwjid").val();
	    				var wjid = sel_row[0].wjid;
	    				var gwjid = sel_row[0].gwjid;
	    				var url= $('#document_formSearch #urlPrefix').val()+"/production/document/reldocumentSavePage";
	    				jQuery.post(url,{prewjid:prewjid,pregwjid:pregwjid,gwjid:gwjid,wjid:wjid,"access_token":$("#ac_tk").val()},function(responseText){
	    					setTimeout(function(){
	    						if(responseText["status"] == 'success'){
	    							$.success(responseText["message"],function() {
	    								$.closeModal("reldocumentModal");
	    								searchDocumentResult();
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
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//提供给导出用的回调函数
function documentSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="wj.wjid";
	map["sortLastOrder"]="asc";
	map["sortName"]="wj.wjbh";
	map["sortOrder"]="asc";
	return getDocumentSearchData(map);
}

var vm = new Vue({
	el:'#documentapp',
	data: {
	},
	methods: {
		handButton(data){
			var btn_add = $("#document_formSearch #btn_add");
	    	var btn_mod = $("#document_formSearch #btn_mod");
	    	var btn_del = $("#document_formSearch #btn_del");
	    	var btn_view = $("#document_formSearch #btn_view");
	    	var btn_time = $("#document_formSearch #btn_time");
	    	var btn_revise = $("#document_formSearch #btn_revise");
	    	var btn_submit = $("#document_formSearch #btn_submit");
	    	var btn_qaadd = $("#document_formSearch #btn_qaadd");
	    	var btn_batchpermit = $("#document_formSearch #btn_batchpermit");
	    	var btn_viewmore=$("#document_formSearch #btn_viewmore");
	    	var btn_transform=$("#document_formSearch #btn_transform");
	    	var btn_predownload=$("#document_formSearch #btn_predownload");
	    	var btn_discard = $("#document_formSearch #btn_discard");
	    	var btn_reldocument = $("#document_formSearch #btn_reldocument");
	    	var btn_searchexport = $("#document_formSearch #btn_searchexport");
	    	var btn_selectexport = $("#document_formSearch #btn_selectexport");
	    	var btn_advancedmod = $("#document_formSearch #btn_advancedmod");
	    	var btn_printgrant = $("#document_formSearch #btn_printgrant");
	    	var btn_bulkimports = $("#document_formSearch #btn_bulkimports");

			if(data == "view"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					documentDealById(sel_row[0].wjid,"view",btn_view.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "bulkimports"){
                documentDealById(null,"bulkimports",btn_bulkimports.attr("tourl"));
            }else if(data == "viewmore"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					
					documentDealById(sel_row[0].wjid,"viewmore",btn_viewmore.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "add"){
				documentDealById(null,"add",btn_add.attr("tourl"));
			}else if(data == "qaadd"){
				documentDealById(null,"qaadd",btn_qaadd.attr("tourl"));
			}else if(data=="transform"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==1){
					if(sel_row[0].zt=="80"){
						documentDealById(sel_row[0].wjid,"transform",btn_transform.attr("tourl"));
					}else{
						$.error("审核通过时才能提交转换！");
					}
				}else{
					$.error("请选中一行");
				}
			}else if(data == "del"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length ==0){
	    			$.error("请至少选中一行");
	    			return;
	    		}
	    		var ids="";
	    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	    			ids = ids + ","+ sel_row[i].wjid;
	    			if(sel_row[i].glwjid){
	    				$.error("该文件已更改版本，不允许删除");
	    				return;
	    			}
	    			if(sel_row[i].zt =='10'){
	    				$.error("该文件正在审核中，不允许删除");
	    				return;
	    			}
	    		}
	    		ids = ids.substr(1);
	    		$.confirm('您确定要删除所选择的记录吗？',function(result){
	    			if(result){
	    				jQuery.ajaxSetup({async:false});
	    				var url= $('#document_formSearch #urlPrefix').val() + btn_del.attr("tourl");
	    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
	    					setTimeout(function(){
	    						if(responseText["status"] == 'success'){
	    							$.success(responseText["message"],function() {
	    								searchDocumentResult();
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
			}else if(data == "mod"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if(sel_row[0].glwjid){
	    				$.error("该文件已更改版本，不允许修改");
	    				return;
	    			}
					if(sel_row[0].zt == '10'){
						$.error("该文件在审核中，不允许修改");
	    				return;
					}
					documentDealById(sel_row[0].wjid,"mod",btn_mod.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "advancedmod"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if(sel_row[0].zt == '10'){
						$.error("该文件在审核中，不允许高级修改!");
	    				return;
					}
					documentDealById(sel_row[0].wjid,"advancedmod",btn_advancedmod.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "revise"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if(sel_row[0].glwjid){
	    				$.error("该文件已更改版本，不允许修订");
	    				return;
	    			}
					if(sel_row[0].zt == '10'){
						$.error("该文件在审核中，不允许修订");
	    				return;
					}
					if(sel_row[0].zt == '15'){
						$.error("该文件审核未通过，不允许修订");
	    				return;
					}
					documentDealById(sel_row[0].wjid,"revise",btn_revise.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "time"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					documentDealById(sel_row[0].wjid,"time",btn_time.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "submit"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if(sel_row[0].zt !='00' && sel_row[0].zt !='15'){
	    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
	    				return;
	    			}
					documentDealById(sel_row[0].wjid,"submit",btn_submit.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "batchpermit"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length ==0){
	    			$.error("请至少选中一行");
	    			return;
	    		}
	    		var ids="";
	    		var sfxd="0";
	    		var wjbhs="";
	    		var sfsh="0";
	    		var wjbhs_t="";
	    		if(sel_row.length>0){
	    			for (var i = 0; i < sel_row.length; i++) {
		    			//循环读取选中行数据
		    			ids = ids + ","+ sel_row[i].wjid;
		    			if(sel_row[i].glwjid){
		    				sfxd="1";
		    				wjbhs=wjbhs+","+sel_row[i].wjbh;
		    			}
		    			if(sel_row[i].zt!=null && sel_row[i].zt=='10'){
		    				sfsh="1";
		    				wjbhs_t=wjbhs_t+","+sel_row[i].wjbh;
		    			}
		    		}
	    		}
	    		
	    		if(sfxd=="1"){
	    			wjbhs=wjbhs.substr(1);
	    			$.error(wjbhs+"该文件已更改版本，不允许修改");
    				return;
	    		}
	    		if(sfsh=="1"){
	    			wjbhs_t=wjbhs_t.substr(1);
	    			$.error(wjbhs_t+"该文件正在审核中，不允许修改");
					return;
	    		}	    		
	    		ids = ids.substr(1);
	    		documentDealById(ids,"batchpermit",btn_batchpermit.attr("tourl"));
			}else if(data=="predownload"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==1){
					documentDealById(sel_row[0].wjid,"predownload",btn_predownload.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			}else if(data == "discard"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length ==0){
	    			$.error("请至少选中一行");
	    			return;
	    		}
	    		var ids="";
	    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	    			ids = ids + ","+ sel_row[i].wjid;
	    			if(sel_row[i].glwjid){
	    				$.error("该文件已更改版本，不允许删除");
	    				return;
	    			}
	    			if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
	    				$.error("该文件正在审核中或已审核，不允许删除");
	    				return;
	    			}
	    		}
	    		ids = ids.substr(1);
	    		$.confirm('您确定要删除所选择的记录吗？',function(result){
	    			if(result){
	    				jQuery.ajaxSetup({async:false});
	    				var url= $('#document_formSearch #urlPrefix').val()+btn_discard.attr("tourl");
	    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
	    					setTimeout(function(){
	    						if(responseText["status"] == 'success'){
	    							$.success(responseText["message"],function() {
	    								searchDocumentResult();
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
			}else if(data=="reldocument"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==1){
					if(sel_row[0].glwjid){
	    				$.error("该文件已更改版本，不允许关联文件");
	    				return;
	    			}
	    			if(sel_row[0].zt =='10'){
	    				$.error("该文件正在审核中，不允许关联文件");
	    				return;
	    			}
					documentDealById(sel_row[0].wjid,"reldocument",btn_reldocument.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			}else if(data=="selectexport"){
	    		var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length>=1){
	    			var ids="";
	        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	        			ids = ids + ","+ sel_row[i].wjid;
	        		}
	        		ids = ids.substr(1);
	        		$.showDialog($('#document_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=DOCUMENT_SELECT&expType=select&ids="+ids
	        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
	    		}else{
	    			$.error("请选择数据");
	    		}
	    	}else if(data=="searchexport"){
	    		$.showDialog($('#document_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=DOCUMENT_SEARCH&expType=search&callbackJs=documentSearchData"
	    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
	    	}else if(data == "printgrant"){
				var sel_row = $('#document_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==1){
					documentDealById(sel_row[0].wjid,"printgrant",btn_printgrant.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			}
		},
		search(){
			searchDocumentResult(true);
		},
		searchMore(){
			//添加日期控件
			laydate.render({
			   elem: '#sxrqstart'
			  ,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
			   elem: '#sxrqend'
			  ,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
			   elem: '#bzsjstart'
			  ,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
			   elem: '#bzsjend'
			  ,theme: '#2381E9'
			});
			
			var ev=ev||event; 
    		if(document_turnOff){
    			$("#document_formSearch #searchMore").slideDown("low");
    			document_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#document_formSearch #searchMore").slideUp("low");
    			document_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
		},
		wjflBind(){
			setTimeout(function(){getWjlbs();}, 10);
		},
		enter(){
			searchDocumentResult(true);
		},
	}
})

//文件类别的取得
function getWjlbs() {
	// 文件分类
	var wjfl = jQuery('#document_formSearch #wjfl_id_tj').val();
	if (!isEmpty(wjfl)) {
		wjfl = "'" + wjfl + "'";
		jQuery("#document_formSearch #wjlb_id").removeClass("hidden");
	}else{
		jQuery("#document_formSearch #wjlb_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(wjfl)) {
		var url = $('#document_formSearch #urlPrefix').val()+"/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":wjfl,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						if(data[i].scbj=="2"){
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wjlb','" + data[i].csid + "','document_formSearch');\" id=\"wjlb_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" +"</span></a>";
						}else
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wjlb','" + data[i].csid + "','document_formSearch');\" id=\"wjlb_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#document_formSearch #ul_wjlb").html(html);
					jQuery("#document_formSearch #ul_wjlb").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#document_formSearch #ul_wjlb").empty();
					
				}
				jQuery("#document_formSearch [id^='wjlb_li_']").remove();
				$("#document_formSearch #wjlb_id_tj").val("");
			}
		});
	} else {
		jQuery("#document_formSearch #div_wjlb").empty();
		jQuery("#document_formSearch [id^='wjlb_li_']").remove();
		$("#document_formSearch #wjlb_id_tj").val("");
	}
}


function searchDocumentResult(isTurnBack){
	if(isTurnBack){
		$('#document_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#document_formSearch #tb_list').bootstrapTable('refresh');
	}
	//关闭高级搜索条件
	$("#document_formSearch #searchMore").slideUp("low");
	document_turnOff=true;
	$("#document_formSearch #sl_searchMore").html("高级筛选");
}

var document_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var scbj = $("#document_formSearch a[id^='scbj_id_']");
    	$.each(scbj, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		if(code === '0'){
    			addTj('scbj',code,'document_formSearch');
    		}
    	});
    	var zt = $("#document_formSearch a[id^='zt_id_']");
    	$.each(zt, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		//暂时不限定未只有通过的才显示  20190522
    		if(code === '80'){
    			addTj('zt',code,'document_formSearch');
    		}
    	});
		var wjfls_str=$('#document_formSearch #wjfls_str').val();
		if(wjfls_str){
			var split=wjfls_str.split(",");
			for(var i=0;i<split.length;i++){
				addTj('wjfl',split[i],'document_formSearch');
			}
			setTimeout(function(){getWjlbs();}, 10);
		}
    }
    return oInit;
}

$(function(){
	
	//0.界面初始化
	//1.初始化Table
    var oInit = new document_PageInit();
    oInit.Init();
	
    // 1.初始化Table
    var oTable = new document_TableInit();
    oTable.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#document_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	$("#document_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});