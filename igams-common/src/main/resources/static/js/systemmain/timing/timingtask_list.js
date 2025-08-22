var TimingTask_turnOff=true;

var TimingTask_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#timingtasklist_formSearch #tb_list').bootstrapTable({
//			url: '/systemmain/timing/listTimingTask',         // 请求后台的URL（*）
			url: $("#timingtasklist_formSearch #urlPrefix").val()+'/systemmain/timing/pageGetListTimingTask',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#timingtasklist_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
		    paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"rwmc",					// 排序字段
			sortOrder: "asc",                   // 排序方式
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
			uniqueId: "rwid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			}, {
				field: 'rwid',
				title: '任务ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'rwmc',
				title: '任务名称',
				width: '18%',
				align: 'left',
				visible: true,
                sortable: true
			}, {
				field: 'dsxx',
				title: '定时信息',
				width: '10%',
				align: 'left',
				visible: true,
                sortable: true
			}, {
				field: 'zxl',
				title: '执行类',
				width: '10%',
				align: 'left',
				visible: true,
                sortable: true
			}, {
				field: 'zxff',
				title: '执行方法',
				width: '10%',
				align: 'left',
				visible: true,
                sortable: true
			}, {
				field: 'cs',
				title: '参数',
				width: '20%',
				align: 'left',
				visible: true,
                sortable: true
			}, {
				field: 'remindtype',
				title: '类别',
				width: '10%',
				align: 'left',
				visible: true,
                sortable: true
			}, {
				field: 'lrry',
				title: '录入人员',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'lrsj',
				title: '录入时间',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'xgry',
				title: '修改人员',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'xgsj',
				title: '修改时间',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'scry',
				title: '删除人员',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'scsj',
				title: '删除时间',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'scbj',
				title: '删除标记',
				width: '8%',
				align: 'left',
				visible: false
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				TimingTaskDealById(row.rwid, 'view',$("#timingtasklist_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#timingtasklist_formSearch #tb_list").colResizable(
            {
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true,
                pid:"timingtasklist_formSearch"
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
        sortLastName: "rwid", // 防止同名排位用
        sortLastOrder: "asc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getTimingTaskData(map);
	};
	return oTableInit;
};

function getTimingTaskData(map){
	var cxtj=$("#timingtasklist_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#timingtasklist_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["rwmc"]=cxnr
	}else if(cxtj=="1"){
		map["dsxx"]=cxnr
	}else if(cxtj=="2"){
		map["zxff"]=cxnr
	}else if(cxtj=="3"){
		map["zxl"]=cxnr
	}
	 return map;
}

function TimingTaskDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#timingtasklist_formSearch #urlPrefix").val() + tourl;
	if(action =='view'){
		var url= tourl + "?rwid=" +id;
		$.showDialog(url,'定时任务详细信息',	viewTimingTaskConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增定时任务',addTimingTaskConfig);
	}else if(action =='mod'){
		var url=tourl + "?rwid=" +id;
		$.showDialog(url,'编辑定时任务信息',modTimingTaskConfig);
	}else if(action =='distinguish'){
		var url=tourl + "?rwids=" +id;
		$.showDialog(url,'用户区分',distinguishTimingTaskConfig);
	}
}
var distinguishTimingTaskConfig = {
	width		: "800px",
	modalName	: "distinguishTimingTaskModal",
	formName	: "configUserForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {

			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#configUserForm").valid()){
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};

				$("#configUserForm input[name='access_token']").val($("#ac_tk").val());
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

function timingTaskResearchResult(isTurnBack){
	if(isTurnBack){
		$('#timingtasklist_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#timingtasklist_formSearch #tb_list').bootstrapTable('refresh');
	}
}

var TimingTask_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#timingtasklist_formSearch #btn_view");
		var btn_add = $("#timingtasklist_formSearch #btn_add");
		var btn_mod = $("#timingtasklist_formSearch #btn_mod");
		var btn_del = $("#timingtasklist_formSearch #btn_del");
		var btn_query = $("#timingtasklist_formSearch #btn_query");
		var btn_execute=$("#timingtasklist_formSearch #btn_execute");
		var btn_distinguish = $("#timingtasklist_formSearch #btn_distinguish");
		
		
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			timingTaskResearchResult();
    		});
    	}
        /*---------------------------查看任务信息表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#timingtasklist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			TimingTaskDealById(sel_row[0].rwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*---------------------------执行任务信息-----------------------------------*/
		btn_execute.unbind("click").click(function(){
			var sel_row = $('#timingtasklist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			$.confirm('您确定要执行所选择的方法吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url= $("#timingtasklist_formSearch #urlPrefix").val()+btn_execute.attr("tourl");
					jQuery.post(url,{rwid:sel_row[0].rwid,"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									timingTaskResearchResult();
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
		});
    	/*---------------------------新增定时任务信息-----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		TimingTaskDealById(null,"add",btn_add.attr("tourl"));
    	});
    	/*---------------------------编辑定时任务信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#timingtasklist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			TimingTaskDealById(sel_row[0].rwid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		btn_distinguish.unbind("click").click(function(){
			var sel_row = $('#timingtasklist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var rwids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				rwids = rwids + ","+ sel_row[i].rwid;
			}
			rwids = rwids.substr(1);

			TimingTaskDealById(rwids,"distinguish",btn_distinguish.attr("tourl"));
		});
    	/*---------------------------删除定时任务信息-----------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#timingtasklist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].rwid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#timingtasklist_formSearch #urlPrefix").val()+btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								timingTaskResearchResult();
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
    	});
    };
    return oInit;
};

var viewTimingTaskConfig = {
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

var executeTimingTaskConfig = {
	width		: "700px",
	modalName	: "executeTimingTaskModal",
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
								timingTaskResearchResult();
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

var addTimingTaskConfig = {
		width		: "700px",
		modalName	: "addTimingTaskModal",
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
									timingTaskResearchResult();
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

var modTimingTaskConfig = {
		width		: "700px",
		modalName	: "addTimingTaskModal",
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
									timingTaskResearchResult();
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
$(function(){
	//0.界面初始化
	// 1.初始化Table
	var oTable = new TimingTask_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new TimingTask_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#timingtasklist_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
//	$("#projectresearch_formSearch [name='more']").each(function(){
//		$(this).on("click", s_showMoreFn);
//	});
});