
var cellcounting_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$("#cellcounting_formSearch #tb_list").bootstrapTable({
			url: "/experiment/counting/pageGetListCounting",         // 请求后台的URL（*）
			method: "get",                      // 请求方式（*）
			toolbar: "#cellcounting_formSearch #toolbar", // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"lrsj",					// 排序字段
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
			uniqueId: "qclid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '5%',
                align: 'left',
                visible:true
            },{
				field: 'qclid',
				title: '前处理编号',
				width: '19%',
				align: 'left',
				visible: false
			},{
				field: 'mc',
				title: '名称',
				width: '19%',
				align: 'left',
				visible: true
			},{
				field: 'jcdwmc',
				title: '检测单位',
				width: '19%',
				align: 'left',
				visible: true
			},{
				field: 'zt',
				title: '状态',
				width: '19%',
				align: 'left',
				formatter:ztFormat,
				visible: true
			},{
				field: 'lrrymc',
				title: '录入人员',
				width: '19%',
				align: 'left',
				visible: true
			},{
				field: 'lrsj',
				title: '录入时间',
				width: '19%',
				align: 'left',
				visible: true
			}],
			onLoadSuccess:function(){
	        },
	        onLoadError:function(){
	        	alert("数据加载失败！");
	        },
	        onDblClickRow: function (row, $element) {
	        	CountingById(row.qclid,'view',$("#cellcounting_formSearch #btn_view").attr("tourl"));
	         },
		})
	}
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
	        sortLastName: "lrsj", // 防止同名排位用
	        sortLastOrder: "desc" // 防止同名排位用
	        // 搜索框使用
	        // search:params.search
	    };
	    return getCountingSearchData(map);
	};
	return oTableInit;
}

function ztFormat(value,row,index){
	if(row.zt=="80"){
		return "完成"
	}
}
/**
 * 模糊检索
 */
function getCountingSearchData(map){
	var cxtj=$("#cellcounting_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#cellcounting_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["allrow"]=cxnr
	}else if(cxtj=="1"){
		map["mc"]=cxnr
	}else if(cxtj=="2"){
		map["lrrymc"]=cxnr
	}else if(cxtj=="3"){
		map["nbbh"]=cxnr
	}
	 return map;
}

/**
 * 刷新列表
 */
function CountingResult(isTurnBack){
	if(isTurnBack){
		$('#cellcounting_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#cellcounting_formSearch #tb_list').bootstrapTable('refresh');
	}
}

/**
 * 加载按钮
 */
var cellcounting_ButtonInit=function(){
	 var oInit = new Object();
	 var postdata = {};
	 oInit.Init = function () {
		var btn_query = $("#cellcounting_formSearch #btn_query");
		var btn_view = $("#cellcounting_formSearch #btn_view");
		var btn_del = $("#cellcounting_formSearch #btn_del");
		//模糊检索
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			CountingResult(true);
    		});
    	}
		//查看页面
		btn_view.unbind("click").click(function(){
    		var sel_row = $('#cellcounting_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			CountingById(sel_row[0].qclid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		//删除
		btn_del.unbind("click").click(function(){
    		var sel_row = $('#cellcounting_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].qclid;
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
    								CountingResult();
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
	 }
	   return oInit;
}

/**
 * 按钮点击操作
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function CountingById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?qclid=" +id;
		$.showDialog(url,'前处理查看',viewCountingConfig);
	}
}

var viewCountingConfig = {
		width		: "1000px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
$(function(){
	// 所有下拉框添加choose样式
	jQuery("#cellcounting_formSearch .chosen-select").chosen({width: '100%'});
	
	var oTable = new cellcounting_TableInit();
	oTable.Init();
	
	//2.初始化Button的点击事件
    var oButtonInit = new cellcounting_ButtonInit();
    oButtonInit.Init();
})