var goodBack_turnOff=true;
var goodBack_TableInit = function () { 
	 var oTableInit = new Object();
	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#goodBack_formSearch #goodback_list').bootstrapTable({
			url: $("#goodBack_formSearch #urlPrefix").val()+'/storehouse/goodback/goodbackpageList',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#goodBack_formSearch #toolbar', // 工具按钮用哪个容器
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
			uniqueId: "dhthid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
                align: 'left',
                visible:true
            },{				
				field: 'wlbm',
				title: '物料编码',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'wlmc',
				title: '物料名称',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'gg',
				title: '规格',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'jldw',
				title: '单位',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'dhdh',
				title: '到货单号',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'gysmc',
				title: '供应商',
				width: '4%',
				align: 'left',
				visible: true
			}, {				
				field: 'dhrq',
				title: '到货日期',
				width: '5%',
				align: 'left',
				visible: true
			}, {				
				field: 'clbh',
				title: '处置编号',
				width: '5%',
				align: 'left',
				visible: true
			}, {				
				field: 'clcs',
				title: '处置措施',
				width: '5%',
				align: 'left',
				visible: true
			}, {				
				field: 'zsh',
				title: '追溯号',
				width: '5%',
				align: 'left',
				visible: false
			}, {				
				field: 'ysqk',
				title: '验收情况',
				width: '5%',
				align: 'left',
				visible: false
			}, {				
				field: 'lrrymc',
				title: '录入人员',
				width: '5%',
				align: 'left',
				visible: false
			}, {				
				field: 'lrsj',
				title: '录入时间',
				width: '5%',
				align: 'left',
				visible: false
			},{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:chczFormat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				googBackById(row.hwid,'view',$("#goodBack_formSearch #btn_view").attr("tourl"));
			}
		});
		$("#goodBack_formSearch #goodback_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
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
        sortLastName: "dhthid", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return goodbackSearchData(map);
	};
	return oTableInit;
};
/**
 * 撤回操作
 */
function chczFormat(value,row,index) {
	if(row.zt=='10'){
		return "<div id='dhthid_"+row.dhthid+"'><span id='thid"+row.dhthid
		+"' class='btn btn-danger' title='撤回审核' onclick=\"submitBack('" + row.dhthid + "',event)\" >撤回</span></div>";
	}
}
function submitBack(dhthid){
	var msg = '您确定要撤回该检验信息吗？';
	var purchase_params = [];
	var auditType = $("#goodBack_formSearch #auditType").val();
	purchase_params.prefix = $("#goodBack_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,dhthid,function(){
				goodBackResult(false);
			},purchase_params);
		}
	});
}
function goodbackSearchData(map){
	var cxtj=$("#goodBack_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#goodBack_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["dhdh"]=cxnr;
	}
	// 创建开始日期
	var dhrqstart = $('#goodBack_formSearch #dhrqstart').val();
	map["dhrqstart"] = dhrqstart;
	
	// 创建结束日期
	var dhrqend = $('#goodBack_formSearch #dhrqend').val();
	map["dhrqend"] = dhrqend;
	
	//供应商
	var gysmc = $('#goodBack_formSearch #gysmc').val();
	map["gysmc"] = gysmc;
	//合同号
	var htnbbh = $('#goodBack_formSearch #htnbbh').val();
	map["htnbbh"] = htnbbh;
		//备注
	var bz = $('#goodBack_formSearch #bz').val();
	map["bz"] = bz;
	
	return map;
}
var goodback_ButtonInit = function(){
	 var oInit = new Object();
	 var postdata = {};
	 oInit.Init = function () {
		var btn_view = $("#goodBack_formSearch #btn_view");
		var btn_query = $("#goodBack_formSearch #btn_query");
		var btn_searchexport = $("#goodBack_formSearch #btn_searchexport");
    	var btn_selectexport = $("#goodBack_formSearch #btn_selectexport");
    	var btn_dispose = $("#goodBack_formSearch #btn_dispose");
		//添加日期控件
    	laydate.render({
    	   elem: '#dhrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#dhrqend'
    	  ,theme: '#2381E9'
    	});
		if(btn_query != null){
	    	btn_query.unbind("click").click(function(){
	    		goodBackResult(true);
	    	});
	    };
		if(btn_view != null){
			btn_view.unbind("click").click(function(){
				var sel_row = $('#goodBack_formSearch #goodback_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length==1){
	    			googBackById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			});
		};
		btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#goodBack_formSearch #goodback_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].dhthid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#goodBack_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=GOODBACK_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#goodBack_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=GOODBACK_SEARCH&expType=search&callbackJs=insSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	btn_dispose.unbind("click").click(function(){
    		var sel_row = $('#goodBack_formSearch #goodback_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			googBackById(sel_row[0].dhthid,"dispose",btn_dispose.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/**显示隐藏**/      
    	$("#goodBack_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(goodBack_turnOff){
    			$("#goodBack_formSearch #searchMore").slideDown("low");
    			goodBack_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#goodBack_formSearch #searchMore").slideUp("low");
    			goodBack_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
	 };
	 return oInit;
};
//提供给导出用的回调函数
function insSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="dhth.lrsj";
	map["sortLastOrder"]="desc";
	map["sortName"]="hwxx.dhrq";
	map["sortOrder"]="desc";
	return goodbackSearchData(map);
}
function goodBackResult(isTurnBack){
	//关闭高级搜索条件
	$("#goodBack_formSearch #searchMore").slideUp("low");
	goodBack_turnOff=true;
	$("#goodBack_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#goodBack_formSearch #goodback_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#goodBack_formSearch #goodback_list').bootstrapTable('refresh');
	}
}
function googBackById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#goodBack_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= $("#goodBack_formSearch #urlPrefix").val() + "/inspectionGoods/pendingInspection/viewPendingInspection?hwid=" +id;
		$.showDialog(url,'到货退回详细信息',viewgoodBackConfig);
	}else if(action=='dispose'){
		var auditType = $("#goodBack_formSearch #auditType").val();
		showAuditDialogWithLoading({
			id: id,
			type: auditType,
			url:tourl,
			data:{'ywzd':'dhthid'},
			title:"货物退回审核",
			prefix:$('#goodBack_formSearch #urlPrefix').val(),
			callback:function(){
				goodBackResult(true);//回调
			},
			dialogParam:{width:800}
		});
	}
}
var viewgoodBackConfig = {
		width		: "1600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 
	};
$(function(){
	// 1.初始化Table
	var oTable = new goodBack_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new goodback_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#goodBack_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#goodBack_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});