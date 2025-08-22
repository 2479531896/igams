var research_turnOff=true;

var research_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#research_formSearch #tb_list').bootstrapTable({
			url: '/research/research/listResearch',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#research_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"yf.yfid",					// 排序字段
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
			uniqueId: "yfid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			}, {
				field: 'no',
				title: '序号',
				titleTooltip:'序号',
				width: '8%',
				align: 'left',
				visible: true,
				formatter:xhFormat
			}, {
				field: 'yfid',
				title: '研发ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'dcmc',
				title: '调查名称',
				titleTooltip:'调查名称',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'yfmc',
				title: '研发名称',
				titleTooltip:'研发名称',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'yfdm',
				title: '研发代码',
				titleTooltip:'研发代码',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'yflxmc',
				title: '研发类型',
				titleTooltip:'研发类型',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'dqjd',
				title: '当前进度',
				titleTooltip:'当前进度',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'kssj',
				title: '开始时间',
				titleTooltip:'开始时间',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'qwwcsj',
				title: '期望完成时间',
				titleTooltip:'期望完成时间',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'sjwcsj',
				title: '实际完成时间',
				titleTooltip:'实际完成时间',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'bz',
				title: '备注',
				titleTooltip:'备注',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'scbj',
				title: '状态',
				titleTooltip:'状态',
				width: '12%',
				align: 'left',
				formatter:scbjFormat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				researchDealById(row.yfid, 'view',$("#research_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#research_formSearch #tb_list").colResizable({
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
			sortLastName: "yf.yfid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};

		var cxtj = $("#research_formSearch #cxtj").val();
		// 查询条件
		var cxnr = $.trim(jQuery('#research_formSearch #cxnr').val());
		// '0':'研发名称','1':'研发代码','2':'备注',
		if (cxtj == "0") {
			map["yfmc"] = cxnr;
		}else if (cxtj == "1") {
			map["yfdm"] = cxnr;
		}else if (cxtj == "2") {
			map["bz"] = cxnr;
		}
		// 研发类型
		var yflx = jQuery('#research_formSearch #yflx_id_tj').val();
		map["yflxs"] = yflx;
		// 当前进度
		var dqjd = jQuery('#research_formSearch #dqjd_id_tj').val();
		map["dqjds"] = dqjd;
		
		// 开始开始日期
		var kssjstart = jQuery('#research_formSearch #kssjstart').val();
		map["kssjstart"] = kssjstart;
		
		// 开始结束日期
		var kssjend = jQuery('#research_formSearch #kssjend').val();
		map["kssjend"] = kssjend;
		
		// 实际完成开始日期
		var sjwcsjstart = jQuery('#research_formSearch #sjwcsjstart').val();
		map["sjwcsjstart"] = sjwcsjstart;
		
		// 实际完成结束日期
		var sjwcsjend = jQuery('#research_formSearch #sjwcsjend').val();
		map["sjwcsjend"] = sjwcsjend;
		
		return map;
	};
	return oTableInit;
};
//序号格式化
function xhFormat(value, row, index) {
	//获取每页显示的数量
	var pageSize=$('#research_formSearch #tb_list').bootstrapTable('getOptions').pageSize;
	//获取当前是第几页
	var pageNumber=$('#research_formSearch #tb_list').bootstrapTable('getOptions').pageNumber;
	//返回序号，注意index是从0开始的，所以要加上1
	return pageSize * (pageNumber - 1) + index + 1;
}

//使用状态（删除标记）格式化
function scbjFormat(value,row,index) {
	if (row.scbj == '2') {
		return '停用';
	}
	else{
		return '正常';
	}
}

var research_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
		var btn_add = $("#research_formSearch #btn_add");
		var btn_mod = $("#research_formSearch #btn_mod");
		var btn_del = $("#research_formSearch #btn_del");
		var btn_view = $("#research_formSearch #btn_view");
    	var btn_query = $("#research_formSearch #btn_query");
    	
		//添加日期控件
		laydate.render({
		   elem: '#research_formSearch #kssjstart'
		  ,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
		   elem: '#research_formSearch #kssjend'
		  ,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
		   elem: '#research_formSearch #sjwcsjstart'
		  ,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
		   elem: '#research_formSearch #sjwcsjend'
		  ,theme: '#2381E9'
		});

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchResearchResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		researchDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#research_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			researchDealById(sel_row[0].yfid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#research_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			researchDealById(sel_row[0].yfid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#research_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].yfid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchResearchResult();
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
    	/**显示隐藏**/      
    	$("#research_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(research_turnOff){
    			$("#research_formSearch #searchMore").slideDown("low");
    			research_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#research_formSearch #searchMore").slideUp("low");
    			research_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});

    };

    return oInit;
};

//按钮动作函数
function researchDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?yfid=" +id;
		$.showDialog(url,'查看研发信息',viewResearchConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增研发信息',addResearchConfig);
	}else if(action =='mod'){
		var url=tourl + "?yfid=" +id;
		$.showDialog(url,'编辑研发信息',modResearchConfig);
	}
}

var viewResearchConfig = {
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

var addResearchConfig = {
	width		: "900px",
	modalName	: "addResearchModal",
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
								searchResearchResult();
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

var modResearchConfig = {
	width		: "900px",
	modalName	: "modResearchModal",
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
								searchResearchResult();
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

function searchResearchResult(isTurnBack){
	if(isTurnBack){
		$('#research_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#research_formSearch #tb_list').bootstrapTable('refresh');
	}
	//关闭高级搜索条件
	$("#research_formSearch #searchMore").slideUp("low");
	research_turnOff=true;
	$("#research_formSearch #sl_searchMore").html("高级筛选");
}

$(function(){
	//0.界面初始化
//    var oInit = new research_PageInit();
//    oInit.Init();
	// 1.初始化Table
	var oTable = new research_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new research_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#research_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	$("#research_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});