var commonTask_turnOff=true;

var commonTask_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#commonTask_formSearch #tb_list').bootstrapTable({
            url: $("#commontask #urlprefix").val()+'/systemmain/task/pageGetListCommonTask',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#commonTask_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            sortable: true,                     // 是否启用排序
            sortName:"gzgl.wcsj desc,gzgl.qwwcsj",					// 排序字段
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
            },{
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
				field: 'fzr2',
				title: '机构ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'ywmc',
				title: '业务名称',
				titleTooltip:'业务名称',
				width: '31%',
				sortable: true,
				align: 'left',
				visible: true
			}, {
				field: 'rwmc',
				title: '任务名称',
				titleTooltip:'任务名称',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'zsxm',
				title: '负责人',
				titleTooltip:'负责人',
				width: '7%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'lrryxm',
				title: '创建人员',
				titleTooltip:'创建人员',
				width: '7%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'lrsj',
				title: '录入时间',
				titleTooltip:'录入时间',
				width: '7%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'qrrymc',
				title: '当前确认人',
				titleTooltip:'当前确认人',
				width: '7%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'yjgzl',
				title: '预计工作',
				titleTooltip:'预计工作',
				width: '7%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sjgzl',
				title: '实际工作',
				titleTooltip:'实际工作',
				width: '7%',
				align: 'left',
				sortable: true,
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
				field: 'jssj',
				title: '节省时间',
				titleTooltip:'节省时间',
				width: '10%',
				align: 'left',
				visible: false,
				sortable: false
			}, {
				field: 'dqjd',
				title: '进度',
				titleTooltip:'进度',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true,
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
				field: 'jgmc',
				title: '机构名称',
				titleTooltip:'机构名称',
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
				sortable: true,
				visible: true
			}, {
				field: 'jjdmc',
				title: '紧急度',
				titleTooltip:'紧急度',
				width: '7%',
				align: 'left',
				sortable: true,
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
				sortable: true,
				visible: true
			}],
			onLoadSuccess: function (map) {
				$("#zs").text(map.zs);
				$("#cssl").text(map.cssl);
				$("#ljsl").text(map.ljsl);
			},
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	commonTaskDealById(row.gzid, 'view',$("#commonTask_formSearch #btn_view").attr("tourl"),row.rwid);
            },
        });
        $("#commonTask_formSearch #tb_list").colResizable({
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

    	var cxtj = $("#commonTask_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#commonTask_formSearch #cxnr').val());
    	// '0':'任务名称','1':'业务名称','2':'负责人',
    	if (cxtj == "0") {
    		map["rwmc"] = cxnr;
    	} else if(cxtj == "1") {
    		map["ywmc"] = cxnr;
    	} else if(cxtj == "2") {
    		map["fzrxm"] = cxnr;
    	}else if(cxtj == "3") {
    		map["qrrymc"] = cxnr;
    	}else if(cxtj == "4") {
			map["jgmc"] = cxnr;
		}else if(cxtj == "5") {
			map["lrryxm"] = cxnr;
		}
    	
    	// 任务状态
    	var zts = jQuery('#commonTask_formSearch #zt_id_tj').val();
    	map["zts"] = zts;
    	
    	// 期望完成开始日期
    	var qwwcsjstart = jQuery('#commonTask_formSearch #qwwcsjstart').val();
    	map["qwwcsjstart"] = qwwcsjstart;
    	
    	// 期望完成结束日期
    	var qwwcsjend = jQuery('#commonTask_formSearch #qwwcsjend').val();
    	map["qwwcsjend"] = qwwcsjend;
    	
    	// 完成开始日期
    	var wcsjstart = jQuery('#commonTask_formSearch #wcsjstart').val();
    	map["wcsjstart"] = wcsjstart;
    	
    	// 完成结束日期
    	var wcsjend = jQuery('#commonTask_formSearch #wcsjend').val();
    	map["wcsjend"] = wcsjend;

		// 确认开始时间
		var qrsjstart = jQuery('#commonTask_formSearch #qrsjstart').val();
		map["qrsjstart"] = qrsjstart;

		// 确认结束时间
		var qrsjend = jQuery('#commonTask_formSearch #qrsjend').val();
		map["qrsjend"] = qrsjend;

		map["listFlag"] = jQuery('#commonTask_formSearch #listFlag').val();
		btnColorChange( jQuery('#commonTask_formSearch #listFlag').val() );
		// 机构
		var jgs = jQuery('#commonTask_formSearch #jg_id_tj').val();
		map["jgs"] = jgs.replace(/'/g, "");
		// 任务名称
		var rwmcs = jQuery('#commonTask_formSearch #rwmc_id_tj').val();
		map["rwmcs"] = rwmcs.replace(/'/g, "");
    	
    	return map;
    };
    return oTableInit;
};

//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#commonTask_formSearch #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#commonTask_formSearch #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}
/**
 * 任务列表的状态格式化函数
 * @returns
 */
function rwztFormat(value,row,index) {
    if (row.zt == '00') {
        return "<a href='javascript:void(0);' onclick=\"commonTaskDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >未完成</a>";
    }else if (row.zt == '10') {
    	return "<a href='javascript:void(0);' onclick=\"commonTaskDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >确认中</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick=\"commonTaskDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >无需完成</a>";
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick=\"commonTaskDealById('" + row.gzid + "', 'viewRwqrls','/systemmain/task/pagedataTaskHistory')\" >已完成</a>";
    }
}

//当前进度格式化
function dqjdFormat(value, row, index) {
	if(row.dqjd){
		return row.dqjd + "%";
	}
}

//按钮动作函数
function commonTaskDealById(id,action,tourl,value,value1,value2){
	if(!tourl){
		return;
	}
	tourl=$("#commontask #urlprefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?gzid=" +id+ "&rwid=" +value;
		$.showDialog(url,'查看任务',viewCommonTaskConfig);
	}else if(action =='progresssubmit'){
		var url= tourl + "?gzid=" +id;
		$.showDialog(url,'进度提交',progressSubmitCommonTaskConfig);
	}else if(action =='taskcare'){
		var url= tourl + "?ids=" +id+"&zt="+value;
		$.showDialog(url,'任务转交',taskCareCommonConfig);
	}else if(action =='viewRwqrls'){
		var url=tourl + "?gzid=" +id;
		$.showDialog(url,'查看任务历史进度',viewCommonTaskRwqrlsConfig);
	}else if(action =='confirmcare'){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'确认任务转交',confirmCareConfig);
	}else if(action =='mod'){
		var url= tourl + "?rwid=" +value+ "&xmjdid=" +value1+ "&xmid=" +value2;
		$.showDialog(url,'任务修改',modProjectTaskConfig);
	}
}

//编辑项目任务
var modProjectTaskConfig = {
	width		: "1200px",
	modalName	: "modProjectTaskModal",
	formName	: "modSubTaskForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var ksrq=new Date($("#modSubTaskForm #ksrq").val()).getTime();
				var jsrq=new Date($("#modSubTaskForm #jsrq").val()).getTime();
				if(ksrq>jsrq){
					$.error("开始日期不能大于结束日期");
					return false;
				}
				if(!$("#modSubTaskForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#modSubTaskForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"modSubTaskForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchCommonTaskResult();
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
								searchCommonTaskResult();
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

var viewCommonTaskConfig = {
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
								searchCommonTaskResult();
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
var viewCommonTaskRwqrlsConfig = {
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
var progressSubmitCommonTaskConfig = {
	width		: "900px",
	modalName	: "progressSubmitCommonTaskModal",
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
				if($("#ajaxForm input[type='radio']:checked").val() == "80" && !$("#ajaxForm #qrry").val()){
					$.error("请按照提示或选择按钮中选择负责人");
					return false;
				}
				if($("#ajaxForm #fzr").val() == $("#ajaxForm #qrry").val()){
					$.error("申请人不能作为确认人！");
					return false;
				}
				if($("#ajaxForm input[type='radio']:checked").val() == "80"){
					var val = $("#ajaxForm #old_xmjdid").val();
					if(val){
						var xmjdid = $("#xmjdid").find("option:selected").val();
						if(val==xmjdid){
							$.error("请切换项目阶段！");
							return false;
						}
					}
				}
				if($("#ajaxForm input[type='radio']:checked").val() == "80"){
					var val = $("#ajaxForm #jzrq").val();
					if(!val){
						$.error("请填写阶段截止日期！");
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
								searchCommonTaskResult();
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

var taskCareCommonConfig = {
	width		: "1000px",
	modalName	: "taskCareCommonModal",
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
				if ($("#ajaxForm #zt").val() == "00"){
					if(!$("#ajaxForm #fzr").val()){
						$.error("请按照提示或选择按钮中选择负责人");
						return false;
					}
				}else if($("#ajaxForm #zt").val() == "10"){
					if(!$("#ajaxForm #fzr").val()){
						$.error("请按照提示或选择按钮中选择确认人");
						return false;
					}else{
						var fzr=$("#ajaxForm #fzr").val();
						var split = fzr.split(",");
						if(split.length>1){
							$.error("确认人只能选择一个");
							return false;
						}
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
								searchCommonTaskResult();
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

var vm = new Vue({
	el:'#commontask',
	data: {
	},
	methods: {
		handButton(data){
			var btn_progresssubmit = $("#commonTask_formSearch #btn_progresssubmit");
	    	var btn_taskcare = $("#commonTask_formSearch #btn_taskcare");
	    	var btn_del = $("#commonTask_formSearch #btn_del");
	    	var btn_view = $("#commonTask_formSearch #btn_view");
			var btn_confirmcare = $("#commonTask_formSearch #btn_confirmcare");//确认转交
			var btn_mod = $("#commonTask_formSearch #btn_mod");
			
			if(data == "view"){
				var sel_row = $('#commonTask_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					commonTaskDealById(sel_row[0].gzid,"view",btn_view.attr("tourl"),sel_row[0].rwid);
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "taskcare"){
				var sel_row = $('#commonTask_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==0){
	    			$.error("请至少选中一行");
	    			return;
	    		}
				var ids="";
				var zt = sel_row[0].zt;
            	for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
            		ids = ids + ","+ sel_row[i].gzid;
					if(sel_row[i].zt == '80'){
						$.error("请选择未完成或确认中的数据！");
						return;
					}
					if(sel_row[i].zt != zt){
						$.error("请选择同一种状态的数据！");
						return;
					}
            	}
            	ids = ids.substr(1);
            	commonTaskDealById(ids,"taskcare",btn_taskcare.attr("tourl"),zt);
			}else if(data == "progresssubmit"){
				var sel_row = $('#commonTask_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if(sel_row[0].zt != '00'){
		    			$.error("选中数据已提交或已完成");
		    			return;
		    		}
					commonTaskDealById(sel_row[0].gzid,"progresssubmit",btn_progresssubmit.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "del"){
				var sel_row = $('#commonTask_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length ==0){
	    			$.error("请至少选中一行");
	    			return;
	    		}
	    		var ids="";
	    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	    			ids = ids + ","+ sel_row[i].gzid;
	    		}
	    		ids = ids.substr(1);
	    		$.confirm('您确定要删除所选择的记录吗？',function(result){
	    			if(result){
	    				jQuery.ajaxSetup({async:false});
	    				var url= $("#commontask #urlprefix").val()+btn_del.attr("tourl");
	    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
	    					setTimeout(function(){
	    						if(responseText["status"] == 'success'){
	    							$.success(responseText["message"],function() {
	    								searchCommonTaskResult();
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
			}else if(data == "confirmcare"){
				var sel_row = $('#commonTask_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length ==0){
					$.error("请至少选中一行");
					return;
				}
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					if( sel_row[i].zt!='10'){
						$.error("请选择状态为确认中的任务！");
						return;
					}else{
						ids = ids + ","+ sel_row[i].gzid;
					}
				}
				ids = ids.substr(1);
				commonTaskDealById(ids,"confirmcare",btn_confirmcare.attr("tourl"));
			}else if(data == "mod"){
				var sel_row = $('#commonTask_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if(sel_row[0].xmjdid&&sel_row[0].xmid){
						commonTaskDealById(sel_row[0].gzid,"mod",btn_mod.attr("tourl"),sel_row[0].rwid,sel_row[0].xmjdid,sel_row[0].xmid);
					}else{
						$.error("请选择项目任务进行修改!");
						return;
					}
				}else{
					$.error("请选中一行");
					return;
				}
			}
		},
		search(){
			searchCommonTaskResult();
		},
		enter(){
			searchCommonTaskResult();
		},
		searchMore(){
			//添加日期控件
			laydate.render({
			   elem: '#commonTask_formSearch #qwwcsjstart'
			  ,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
			   elem: '#commonTask_formSearch #qwwcsjend'
			  ,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
			   elem: '#commonTask_formSearch #wcsjstart'
			  ,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
			   elem: '#commonTask_formSearch #wcsjend'
			  ,theme: '#2381E9'
			});

			//添加日期控件
			laydate.render({
				elem: '#commonTask_formSearch #qrsjstart'
				,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
				elem: '#commonTask_formSearch #qrsjend'
				,theme: '#2381E9'
			});
			
			var ev=ev||event; 
			if(commonTask_turnOff){
				$("#commonTask_formSearch #searchMore").slideDown("low");
				commonTask_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#commonTask_formSearch #searchMore").slideUp("low");
				commonTask_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		},
	}
})

function selectListInfo(flag) {
	if(flag=='0'){
		$('#commonTask_formSearch #listFlag').val("");
	}else if(flag=='1'){
		$('#commonTask_formSearch #listFlag').val("1");
	}else if(flag=='2'){
		$('#commonTask_formSearch #listFlag').val("2");
	}
	searchCommonTaskResult();
}
function btnColorChange(value){
	$("#commonTask_btns #all").attr("style","background-color:#00a0e9");
	$("#commonTask_btns #overtime").attr("style","background-color:#00a0e9");
	$("#commonTask_btns #near").attr("style","background-color:#00a0e9");

	if (value == ''){
		$("#commonTask_btns #all").attr("style","background-color:orange");
	}else if (value == '1'){
		$("#commonTask_btns #overtime").attr("style","background-color:orange");
	}else if (value == '2'){
		$("#commonTask_btns #near").attr("style","background-color:orange");
	}
}

function searchCommonTaskResult(){
	$('#commonTask_formSearch #tb_list').bootstrapTable('refresh');
	//关闭高级搜索条件
	$("#commonTask_formSearch #searchMore").slideUp("low");
	commonTask_turnOff=true;
	$("#commonTask_formSearch #sl_searchMore").html("高级筛选");
}

var commonTask_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var zt = $("#commonTask_formSearch a[id^='zt_id_']");
    	$.each(zt, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		if(code === '00'){
    			addTj('zt',code,'commonTask_formSearch');
    		}
    	});
    }
    return oInit;
}

$(function(){
//	//0.界面初始化
    var oInit = new commonTask_PageInit();
    oInit.Init();
	
    // 1.初始化Table
    var oTable = new commonTask_TableInit();
    oTable.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#commonTask_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	$("#commonTask_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});