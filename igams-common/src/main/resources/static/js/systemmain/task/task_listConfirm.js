var taskListConfirming_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#task_confirming #tb_list').bootstrapTable({
            url: $("#taskListConfirmForm #urlprefix").val()+'/systemmain/task/pageGetListTaskConfirm',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#task_confirming #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"rwjb.cspx asc,gzgl.lrsj",					// 排序字段
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
            uniqueId: "gzid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            }, {
				field: 'no',
				title: '序号',
				titleTooltip:'序号',
				width: '5%',
				align: 'left',
				visible: true,
				formatter:xhFormat
			}, {
				field: 'gzid',
				title: '工作ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'ywid',
				title: '业务ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'rwid',
				title: '任务ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'jgid',
				title: '机构ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'ywmc',
				title: '业务名称',
				titleTooltip:'业务名称',
				width: '31%',
				align: 'left',
				visible: true
			}, {
				field: 'rwmc',
				title: '任务名称',
				titleTooltip:'任务名称',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'fzr',
				title: '负责人id',
				titleTooltip:'负责人id',
				width: '7%',
				align: 'left',
				visible: false
			},{
				field: 'lrryxm',
				title: '创建人员',
				titleTooltip:'创建人员',
				width: '7%',
				align: 'left',
				visible: true
			},{
				field: 'lrsj',
				title: '录入时间',
				titleTooltip:'录入时间',
				width: '7%',
				align: 'left',
				visible: true
			},{
				field: 'yjgzl',
				title: '预计工作',
				titleTooltip:'预计工作',
				width: '7%',
				align: 'left',
				visible: true
			},{
				field: 'sjgzl',
				title: '实际工作',
				titleTooltip:'实际工作',
				width: '7%',
				align: 'left',
				visible: true
			}, {
				field: 'fqwwcsj',
				title: '期望完成日期',
				titleTooltip:'期望完成日期',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'fwcsj',
				title: '完成时间',
				titleTooltip:'完成时间',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'dqjd',
				title: '进度',
				titleTooltip:'进度',
				width: '6%',
				align: 'left',
				visible: true,
				formatter:dqjdFormat
			}, {
				field: 'ywdz',
				title: '业务地址',
				titleTooltip:'业务地址',
				width: '12%',
				align: 'left',
				visible: false
			}, {
				field: 'gzlx',
				title: '工作类型',
				titleTooltip:'工作类型',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'zt',
				title: '状态',
				titleTooltip:'状态',
				width: '7%',
				align: 'left',
				formatter:rwztFormat,
				visible: true
			}, {
				field: 'jjdmc',
				title: '紧急度',
				titleTooltip:'紧急度',
				width: '7%',
				align: 'left',
				visible: true
			}, {
				field: 'jdmc',
				title: '项目阶段',
				titleTooltip:'项目阶段',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'scbj',
				title: '删除标记',
				titleTooltip:'删除标记',
				width: '12%',
				align: 'left',
				visible: false
			}, {
				field: 'bz',
				title: '备注',
				titleTooltip:'备注',
				width: '22%',
				align: 'left',
				visible: true
			}],
            onLoadSuccess: function () {
				$(".bs-checkbox").each(function(){
					var model=$(this).next().attr("style");
					if(model.split(";")[0].indexOf("background-color")!=-1){
						$(this).attr("style",model.split(";")[0])
					}

				})
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	taskListConfirmDealById(row.gzid, 'view',$("#task_confirming #btn_view").attr("tourl"),row.rwid);
            },
			rowStyle: function (row, index) {
				if(row.rwjbcskz1){
					return { css: {"background-color": '#'+row.rwjbcskz1}}
				}else{
					return {};
				}
			},
        });
        $("#task_confirming #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
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
            sortLastName: "gzgl.gzid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };

    	var cxtj = $("#task_confirming #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#task_confirming #cxnr').val());
    	// '0':'任务名称','1':'业务名称',
    	if (cxtj == "0") {
    		map["rwmc"] = cxnr;
    	} else if(cxtj == "1") {
    		map["ywmc"] = cxnr;
    	}else if(cxtj == "2") {
			map["lrryxm"] = cxnr;
		}
    	return map;
    };
    return oTableInit;
};
var taskListConfirmed_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#task_confirmed #tb_list').bootstrapTable({
			url: $("#taskListConfirmForm #urlprefix").val()+'/systemmain/task/pagedataTaskConfirmed',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#task_confirmed #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"gzlc.shsj",					// 排序字段
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
			uniqueId: "lcid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
			}, {
				field: 'lcid',
				title: '流程ID',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			}, {
				field: 'rwmc',
				title: '任务名称',
				titleTooltip:'任务名称',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'ywmc',
				title: '业务名称',
				titleTooltip:'业务名称',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'sqr',
				title: '负责人',
				titleTooltip:'负责人',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'lrry',
				title: '确认人',
				titleTooltip:'确认人',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'shsj',
				title: '确认时间',
				titleTooltip:'确认时间',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'shyj',
				title: '意见',
				titleTooltip:'意见',
				width: '20%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sftg',
				title: '是否通过',
				titleTooltip:'是否通过',
				width: '3%',
				align: 'left',
				sortable: true,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				taskListConfirmDealById(row.gzid, 'view',$("#task_confirming #btn_view").attr("tourl"),row.rwid);
			},
		});
		$("#task_confirmed #tb_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true}
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
			sortLastName: "gzlc.shyj", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};

		var cxtj = $("#task_confirmed #cxtj").val();
		// 查询条件
		var cxnr = $.trim(jQuery('#task_confirmed #cxnr').val());
		// '0':'任务名称','1':'业务名称',
		if (cxtj == "0") {
			map["rwmc"] = cxnr;
		} else if(cxtj == "1") {
			map["ywmc"] = cxnr;
		}else if(cxtj == "2") {
			map["sqr"] = cxnr;
		}else if(cxtj == "3") {
			map["shry"] = cxnr;
		}
		return map;
	};
	return oTableInit;
};
//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#task_confirming #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#task_confirming #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}

//当前进度格式化
function dqjdFormat(value, row, index) {
	if(row.dqjd){
		return row.dqjd + "%";
	}
}
/**
 * 物料列表的状态格式化函数
 * @returns
 */
function rwztFormat(value,row,index) {
    if (row.zt == '00') {
        return "<a href='javascript:void(0);' onclick=\"taskListConfirmDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >未完成</a>";
    }else if (row.zt == '10') {
    	return "<a href='javascript:void(0);' onclick=\"taskListConfirmDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >确认中</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick=\"taskListConfirmDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >无需完成</a>";
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick=\"taskListConfirmDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >已完成</a>";
    }
}

//按钮动作函数
function taskListConfirmDealById(id,action,tourl,rwid,xmjdid){
	if(!tourl){
		return;
	}
	tourl=$("#taskListConfirmForm #urlprefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?gzid=" +id+ "&rwid=" +rwid;
		$.showDialog(url,'查看任务',viewConfirmTaskConfig);
	}else if(action =='confirm'){
		var url= tourl + "?gzid=" +id;
		$.showDialog(url,'任务确认',confirmTaskConfig);
	}else if(action =='viewRwqrls'){
		var url=tourl + "?gzid=" +id;
		$.showDialog(url,'查看任务历史进度',viewConfirmTaskRwqrlsConfig);
	}else if(action =='batchconfirm'){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'批量确认',batchConfirmTaskConfig);
	}else if(action =='confirmcare'){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'确认任务转交',confirmCareConfig);
	}else if(action =='taskPlan'){
		var url= tourl + "?gzid=" +id+ "&rwid=" +rwid+ "&xmjdid=" +xmjdid;
		$.showDialog(url,'任务计划',taskPlanConfig);
	}
}

var taskPlanConfig = {
	width		: "800px",
	modalName	: "taskPlanModal",
	formName	: "taskPlanForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskPlanForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#taskPlanForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"taskPlanForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchTaskListConfirmResult();
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

var viewConfirmTaskConfig = {
	width		: "700px",
	height		: "500px",
	modalName	: "viewTaskModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchTaskListConfirmResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() { });
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() { });
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
var viewConfirmTaskRwqrlsConfig = {
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

var confirmTaskConfig = {
	width		: "900px",
	modalName	: "confirmTaskModal",
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
				if($("#ajaxForm #zsxm").val() && !$("#ajaxForm #qrry").val()){
					$.error("请按照提示或选择按钮中选择负责人");
					return false;
				}
				if($("#ajaxForm #sftg").val() == "1"){
					if(!$("#ajaxForm #zsxm").val()){
						$("#ajaxForm #qrry").val(null);
					}else{
						if($("#ajaxForm #fzr").val() == $("#ajaxForm #qrry").val()){
							$.error("申请人不能作为确认人！");
							return false;
						}
					}
				}
				if($("#ajaxForm #sftg").val() == "0"){
					if(!$("#ajaxForm #zsxm").val()){
						$.error("请选择退回人！");
						return false;
					}
				}
				var val = $("#ajaxForm #old_xmjdid").val();
				if(val){
					var xmjdid = $("#xmjdid").find("option:selected").val();
					if(val==xmjdid){
						$.error("请切换项目阶段！");
						return false;
					}
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchTaskListConfirmResult();
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

var batchConfirmTaskConfig = {
	width		: "900px",
	modalName	: "batchConfirmModal",
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
								searchTaskListConfirmResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
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

var confirmCareConfig = {
	width		: "400px",
	modalName	: "confirmCareModal",
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
								searchTaskListConfirmResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
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


var taskListConfirming_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#task_confirming #btn_query");
    	var btn_view = $("#task_confirming #btn_view");
    	var btn_confirm = $("#task_confirming #btn_confirm");
    	var btn_batchconfirm = $("#task_confirming #btn_batchconfirm");
	var btn_confirmcare = $("#task_confirming #btn_confirmcare");//确认转交
	var btn_queryTasked = $("#task_confirmed #btn_query");
	var btn_taskPlan = $("#task_confirming #btn_taskPlan");
	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchTaskListConfirmResult(true);
    		});
    	}
		//绑定搜索发送功能
		if(btn_queryTasked != null){
			btn_queryTasked.unbind("click").click(function(){
				searchTaskListConfirmedResult(true);
			});
		}
		btn_taskPlan.unbind("click").click(function(){
			var sel_row = $('#task_confirming #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				taskListConfirmDealById(sel_row[0].gzid,"taskPlan",btn_taskPlan.attr("tourl"),sel_row[0].rwid,sel_row[0].xmjdid);
			}else{
				$.error("请选中一行");
			}
		});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#task_confirming #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				taskListConfirmDealById(sel_row[0].gzid,"view",btn_view.attr("tourl"),sel_row[0].rwid);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_confirm.unbind("click").click(function(){
    		var sel_row = $('#task_confirming #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				if (sel_row[0].zt !== "10"){
					$.error("只能操作确认中得数据");
					return;
				}
				taskListConfirmDealById(sel_row[0].gzid,"confirm",btn_confirm.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_batchconfirm.unbind("click").click(function(){
			var sel_row = $('#task_confirming #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
			var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if (sel_row[i].zt !== "10"){
					$.error("只能操作确认中得数据");
					return;
				}
    			ids = ids + ","+ sel_row[i].gzid;
    		}
    		ids = ids.substr(1);
    		taskListConfirmDealById(ids,"batchconfirm",btn_batchconfirm.attr("tourl"));
    	});
		btn_confirmcare.unbind("click").click(function(){
			var sel_row = $('#task_confirming #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if (sel_row[i].zt !== "10"){
					$.error("只能操作确认中得数据");
					return;
				}
				ids = ids + ","+ sel_row[i].gzid;
			}
			ids = ids.substr(1);
			taskListConfirmDealById(ids,"confirmcare",btn_confirmcare.attr("tourl"));
		});
		//选项卡切换事件回调
		$('#taskListConfirmForm #task_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
			var _hash = e.target.hash.replace("#",'');
			if(!e.target.isLoaded){//只调用一次
				if(_hash=='tasked'){
					var oTable = new taskListConfirmed_TableInit();
					oTable.Init();
				}else{
					var oTable = new taskListConfirming_TableInit();
					oTable.Init();
				}
				e.target.isLoaded = true;
			}
		}).first().trigger('shown.bs.tab');//触发第一个选中事件
    };

    return oInit;
};

function searchTaskListConfirmResult(isTurnBcak){
	if(isTurnBcak){
		$('#task_confirming #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#task_confirming #tb_list').bootstrapTable('refresh');
	}
}
function searchTaskListConfirmedResult(isTurnBcak){
	if(isTurnBcak){
		$('#task_confirmed #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#task_confirmed #tb_list').bootstrapTable('refresh');
	}
}
$(function(){
    //2.初始化Button的点击事件
    var oButtonInit = new taskListConfirming_ButtonInit();
    oButtonInit.Init();
	// 所有下拉框添加choose样式
	jQuery('#task_confirming .chosen-select').chosen({width: '100%'});
	jQuery('#task_confirmed .chosen-select').chosen({width: '100%'});

});