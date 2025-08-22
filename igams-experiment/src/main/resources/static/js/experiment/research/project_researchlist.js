var research_turnOff=true;

var research_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#projectresearch_formSearch #tb_list').bootstrapTable({
			url: '/experiment/research/pageGetListProjectResearch',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#projectresearch_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"rwid",					// 排序字段
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
				field: 'xmid',
				title: '项目ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'fxmid',
				title: '父项目ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'xmmc',
				title: '项目名称',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'xmjdid',
				title: '项目阶段id',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'jdmc',
				title: '当前阶段',
				width: '12%',
				align: 'left',
				visible: true
			},{
				field: 'rwid',
				title: '任务id',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'rwmc',
				title: '任务名称',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'ksrq',
				title: '项目开始日期',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'jsrq',
				title: '项目结束日期',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'xmgkx',
				title: '项目公开性',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'fzrmc',
				title: '负责人',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'rwms',
				title: '任务描述',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'xmlb',
				title: '项目类别',
				width: '8%',
				align: 'left',
				visible: false
			},{
                title: '操作',
                align: 'left',
                width: '20%',
                valign: 'middle',
                formatter:dealFormatter,
                visible: true
            }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				ProjectResearchById(row.rwid,row.xmid,'view',$("#projectresearch_formSearch #btn_view").attr("tourl"));
			},
		});
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
	    return getProjectSearchData(map);
	};
	return oTableInit;
};

function getProjectSearchData(map){
	var cxtj=$("#projectresearch_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#projectresearch_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["xmmc"]=cxnr
	}else if(cxtj=="1"){
		map["rwmc"]=cxnr
	}else if(cxtj=="2"){
		map["mbmc"]=cxnr
	}else if(cxtj=="3"){
		map["jdmc"]=cxnr
	}else if(cxtj=="4"){
		map["xmgkx"]=cxnr
	}else if(cxtj=="5"){
		map["xmlb"]=cxnr
	}
	 return map;
}
//修改阶段转换按钮样式
function dealFormatter(value, row, index) {
	var id = row.rwid;
	var xygxmjdid=row.xygxmjdid;
	var xygxmjdmc=row.xygxmjdmc;
	if(row.xygxmjdid!=null){
		var result = "<div class='btn-group'>";
        result += "<button class='btn btn-default' type='button' onclick=\"ProjectResearchById('" + id + "','', 'modJd','/experiment/research/pagedataModJdxx')\" title='阶段转换'>跳转"+xygxmjdmc+"</button>";
	    result += "</div>"; 
	}
    return result;
}
function ProjectResearchResult(isTurnBack){
	if(isTurnBack){
		$('#projectresearch_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#projectresearch_formSearch #tb_list').bootstrapTable('refresh');
	}
}

function ProjectResearchById(id,xmid,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?rwid=" +id+"&xmid="+xmid;
		$.showDialog(url,'项目研发详细信息',viewResearchConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增项目研发任务',addResearchConfig);
	}else if(action =='mod'){
		var url=tourl + "?rwid=" +id;
		$.showDialog(url,'编辑项目研发任务',modResearchConfig);
	}else if(action =='modJd'){
		var url= tourl + "?rwid=" +id;
		$.showDialog(url,'阶段转换',modResearchJdConfig);
	}
}


var research_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#projectresearch_formSearch #btn_view");
		var btn_add = $("#projectresearch_formSearch #btn_add");
		var btn_mod = $("#projectresearch_formSearch #btn_mod");
		var btn_del = $("#projectresearch_formSearch #btn_del");
		var btn_query = $("#projectresearch_formSearch #btn_query");
		
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			ProjectResearchResult(true);
    		});
    	}
        /*---------------------------查看送检信息表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#projectresearch_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			ProjectResearchById(sel_row[0].rwid,sel_row[0].xmid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------新增项目研发任务信息-----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		ProjectResearchById(null,null,"add",btn_add.attr("tourl"));
    	});
    	/*---------------------------编辑定时任务信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#projectresearch_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			ProjectResearchById(sel_row[0].rwid,null,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------删除项目研发任务信息-----------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#projectresearch_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].rwid;
    		}
    		var xmids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			xmids = xmids + ","+ sel_row[i].xmid;
    		}
    		xmids=xmids.substr(1);
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,xmids:xmids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								ProjectResearchResult();
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

 
var modResearchJdConfig = {
		width		: "500px",
		modalName	: "modJdProjectResearchModal",
		formName	: "chageJdForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#chageJdForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#chageJdForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"chageJdForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									ProjectResearchResult ();
								}
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
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

var viewResearchConfig = {
		width		: "1000px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var addResearchConfig = {
		width		: "900px",
		modalName	: "addProjectResearchModal",
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
					var yhids=$("#ajaxForm #yxcy").val();
					if(yhids!=null&&yhids!=""){
						$("#ajaxForm #yhids").val(yhids);
					}else{
						$.confirm("项目成员不能为空!");
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
									ProjectResearchResult ();
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						}else if(responseText["status"] == "caution"){
							$.alert(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									ProjectResearchResult ();
								}
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

var modResearchConfig = {
		width		: "900px",
		modalName	: "modProjectResearchModal",
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
					
					var yhids=$("#ajaxForm #yxcy").val();
					if(yhids!=null&&yhids!=""){
						$("#ajaxForm #yhids").val(yhids);
					}else{
						$.confirm("项目成员不能为空!");
						return false;
					}
//					debugger
//					var jdlist=[];
//					var jdjhksrqlist=[];
//					var jdjhjsrqlist=[];
//					var jdsl=$("#tab ul").children("li").length;
//					for(var i=0;i<jdsl;i++){
//						var jdid=$("#tab ul li:eq(0)").children("a").attr("href").replace("#","");
//						var jdjhksrq=$("#"+jdid+" #jhksrq").val();
//						var jdjhjsrq=$("#"+jdid+" #jhjsrq").val();
//						jdlist.push(jdid);
//						jdjhksrqlist.push(jdjhksrq);
//						jdjhjsrqlist.push(jdjhjsrq);
//					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									ProjectResearchResult();
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
	var oTable = new research_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new research_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#projectresearch_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
//	$("#projectresearch_formSearch [name='more']").each(function(){
//		$(this).on("click", s_showMoreFn);
//	});
});