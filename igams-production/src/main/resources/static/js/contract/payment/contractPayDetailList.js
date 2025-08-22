var contractPayDetail_TableInit = function () {
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#contractPayDetail_formSearch #contractPayDetail_list').bootstrapTable({
			url: $("#contractPayDetail_formSearch #urlPrefix").val()+'/contract/payInfo/pageGetListPayInfoDetail',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#contractPayDetail_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"fkrq",					// 排序字段
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
			uniqueId: "htfkmxid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '3%',
			},{
				'field': '',
				'title': '序号',
				'align': 'center',
				'width': '3%',
				'formatter': function (value, row, index) {
					var options = $('#contractPayDetail_formSearch #contractPayDetail_list').bootstrapTable('getOptions');
					return options.pageSize * (options.pageNumber - 1) + index + 1;
				}
			},{
				field: 'htfkmxid',
				title: '合同付款明细ID',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'htnbbh',
				title: '合同内部编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'zje',
				title: '合同金额',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'fkje',
				title: '付款金额',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'zfdxmc',
				title: '支付对象',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'fkbfb',
				title: '付款比例',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'fkrq',
				title: '付款日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'lrrymc',
				title: '操作人',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zt',
				title: '状态',
				width: '10%',
				align: 'left',
				formatter:fkztformat,
				sortable: true,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				contractPayDetailById(row.htfkmxid,'view',$("#contractPayDetail_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#contractPayDetail_formSearch #contractPayDetail_list").colResizable({
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
        sortLastName: "htfkmxid", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return contractPayDetailSearchData(map);
	};
	return oTableInit;
};

function contractPayDetailSearchData(map){
	var cxtj=$("#contractPayDetail_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#contractPayDetail_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="1"){
		map["lrrymc"]=cxnr;
	}
	return map;
}
//提供给导出用的回调函数
function ConFkmxSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="fkmx.fkje";
	map["sortLastOrder"]="desc";
	map["sortName"]="fkmx.lrsj";
	map["sortOrder"]="desc";
	return contractPayDetailSearchData(map);
}
function fkztformat(value,row,index){
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPayDetail_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	}else if (row.zt == '15') {

		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPayDetail_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	}else{

		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htfkid + "\",event,\"AUDIT_CONTRACT_PAYMENT\",{prefix:\"" + $('#contractPayDetail_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	}
}

var contractPayDetail_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#contractPayDetail_formSearch #btn_view");
		var btn_query = $("#contractPayDetail_formSearch #btn_query");
		var btn_searchexport = $("#contractPayDetail_formSearch #btn_searchexport");
		var btn_selectexport = $("#contractPayDetail_formSearch #btn_selectexport");
    	//添加日期控件
    	laydate.render({
    	   elem: '#fksjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#fksjend'
    	  ,theme: '#2381E9'
    	});
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			contractPayDetailResult(true);
    		});
    	}

		/* ---------------------------查看列表-----------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#contractPayDetail_formSearch #contractPayDetail_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				contractPayDetailById(sel_row[0].htfkmxid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#contractPayDetail_formSearch #contractPayDetail_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].htfkmxid;
				}
				ids = ids.substr(1);
				$.showDialog($('#contractPayDetail_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTFKMX_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#contractPayDetail_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=HTFKMX_SEARCH&expType=search&callbackJs=ConFkmxSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
    };
    	return oInit;
};

function contractPayDetailById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#contractPayDetail_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?htfkmxid=" +id;
		$.showDialog(url,'付款详细信息',viewPayContractConfig);
	}
}


var viewPayContractConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};



function contractPayDetailResult(isTurnBack){
	if(isTurnBack){
		$('#contractPayDetail_formSearch #contractPayDetail_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#contractPayDetail_formSearch #contractPayDetail_list').bootstrapTable('refresh');
	}
}


$(function() {


	// 1.初始化Table
	var oTable = new contractPayDetail_TableInit();
	oTable.Init();

	//2.初始化Button的点击事件
	var oButtonInit = new contractPayDetail_ButtonInit();
	oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#contractPayDetail_formSearch .chosen-select').chosen({width: '100%'});

	$("#contractPayDetail_formSearch [name='more']").each(function () {
		$(this).on("click", s_showMoreFn);
	});
});
