/**
 * 初始设置
 */
// 请求地址
var url = $("#detail_formSearch #urlPrefix").val()+'/commonViewPage/commonViewPage/viewCommonDetailList?qgmxid='+$("#detail_formSearch #qgmxid").val()+'&htid='+$("#detail_formSearch #htid").val()+'&htmxid='+$("#detail_formSearch #htmxid").val()+'&dhid='+$("#detail_formSearch #dhid").val()+'&dhjyid='+$("#detail_formSearch #dhjyid").val()+'&rkid='+$("#detail_formSearch #rkid").val();
// 显示字段
var columnsArray = [
	{
	checkbox: true,
	width: '2%'
	}, {				
		field: 'htid',
		title: '合同id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'htnbbh',
		title: '合同单号',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: true
	}, {				
		field: 'wlbm',
		title: '物料编码',
		width: '10%',
		align: 'left',
		sortable: true,
		visible: true
	}, {				
		field: 'wlmc',
		title: '物料名称',
		width: '10%',
		align: 'left',
		sortable: true,
		visible: true
	}, {				
		field: 'gg',
		title: '规格',
		width: '10%',
		align: 'left',
		visible: true
	}, {				
		field: 'jldw',
		title: '单位',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'dhdh',
		title: '货号',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'dhsl',
		title: '到货数量',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'zsh',
		title: '追溯号',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'thsl',
		title: '退回数量',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'scrq',
		title: '生产日期',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'yxq',
		title: '失效日期',
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'bz',
		title: '备注',
		width: '10%',
		align: 'left',
		visible: true
	}, {				
		field: 'hwid',
		title: '货物id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'wlid',
		title: '物料id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'htmxid',
		title: '合同明细id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'qgmxid',
		title: '请购明细id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'sl',
		title: '数量',
		width: '5%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'dhid',
		title: '到货id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'cythsl',
		title: '初验退回数量',
		width: '5%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'zjthsl',
		title: '质检退回数量',
		width: '5%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'qyl',
		title: '取样量',
		width: '5%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'qyrq',
		title: '取样日期',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'bgdh',
		title: '报关单号',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'zjrq',
		title: '质检日期',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'zjry',
		title: '质检人员',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'jybz',
		title: '检验备注',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'dhjyid',
		title: '到货检验id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'rkid',
		title: '入库id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'ckid',
		title: '仓库id',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'kwbh',
		title: '库位编号',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'rkry',
		title: '入库人员',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'rkrq',
		title: '入库日期',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'rkbz',
		title: '入库备注',
		width: '12%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'kcl',
		title: '库存量',
		width: '5%',
		align: 'left',
		sortable: true,
		visible: false
	}, {				
		field: 'yds',
		title: '预定数',
		width: '5%',
		align: 'left',
		sortable: true,
		visible: false
	}];
// 排序字段
var sortName = "wlgl.wlbm";
// 每一行的唯一标识，一般为主键列
var uniqueId = "hwxx.hwid";
// 防止同名排位用
var sortLastName = "wlgl.wlbm";
// 按钮事件
var detail_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_qg = $("#detail_formSearch #btn_qg");
		var btn_ht = $("#detail_formSearch #btn_ht");
		var btn_dh = $("#detail_formSearch #btn_dh");
		var btn_jy = $("#detail_formSearch #btn_jy");
		var btn_rk = $("#detail_formSearch #btn_rk");
		/* --------------------------- 请购信息 -----------------------------------*/
    	btn_qg.unbind("click").click(function(){
    		var sel_row = $('#detail_formSearch #detail_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length >= 1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].qgmxid;
        		}
        		ids = ids.substr(1);
    			detailById(ids, "qg");
    			//detailResult(true);
    			
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 合同信息 -----------------------------------*/
    	btn_ht.unbind("click").click(function(){
    		var sel_row = $('#detail_formSearch #detail_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length >= 1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].htmxid;
        		}
        		ids = ids.substr(1);
    			detailById(ids, "ht");
    			//detailResult(true);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 到货信息 -----------------------------------*/
    	btn_dh.unbind("click").click(function(){
    		var sel_row = $('#detail_formSearch #detail_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length >= 1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].dhid;
        		}
        		ids = ids.substr(1);
    			detailById(ids, "dh");
//    			detailResult(true);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	/* --------------------------- 检验信息 -----------------------------------*/
    	btn_jy.unbind("click").click(function(){
    		var sel_row = $('#detail_formSearch #detail_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length >= 1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].dhjyid;
        		}
        		ids = ids.substr(1);
    			detailById(ids, "jy");
//    			detailResult(true);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 入库信息 -----------------------------------*/
    	btn_rk.unbind("click").click(function(){
    		var sel_row = $('#detail_formSearch #detail_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length >= 1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].rkid;
        		}
        		ids = ids.substr(1);
    			detailById(ids, "rk");
//    			detailResult(true);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    };
    return oInit;
};

// 列表信息
var detail_TableInit = function () { 
	var oTableInit = new Object();    
	// 初始化Table
	oTableInit.Init = function () {
		$('#detail_formSearch #detail_list').bootstrapTable({
			url: url,											// 请求后台的URL（*）
			method: 'get',										// 请求方式（*）
			toolbar: '#detail_formSearch #toolbar',				// 工具按钮用哪个容器
			striped: true,										// 是否显示行间隔色
			cache: false,										// 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable: true,										// 是否启用排序
			sortName: sortName,									// 排序字段
			sortOrder: "desc",									// 排序方式
			queryParams: oTableInit.queryParams,				// 传递参数（*）
			sidePagination: "server",							// 分页方式：client客户端分页，server服务端分页（*）
			minimumCountColumns: 2,								// 最少允许的列数
            search: false,                      				// 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            isForceTable: true,
			strictSearch: true,
			showColumns: true,                 				 	// 是否显示所有的列
            showRefresh: true,                  				// 是否显示刷新按钮
			clickToSelect: true,								// 是否启用点击选中行
			uniqueId: uniqueId,									// 每一行的唯一标识，一般为主键列
            showToggle:true,                    				// 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    				// 是否显示详细视图
            detailView: false,                  				// 是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
		});
		// 调整列宽
		$("#detail_formSearch #detail_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		});
	};

	/**
	 * 	得到查询的参数
	 *	请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	 *	例如 toolbar 中的参数
	 *	如果 queryParamsType = ‘limit’ ,返回参数必须包含
	 *	limit, offset, search, sort, order
	 *	否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	 *	返回false将会终止请求
	 */
	oTableInit.queryParams = function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
				pageSize: params.limit,   // 页面大小
				access_token:$("#ac_tk").val(),
				sortName: params.sort,      // 排序列名
				sortOrder: params.order, // 排位命令（desc，asc）
				sortLastName: sortLastName, // 防止同名排位用
				sortLastOrder: "asc",// 防止同名排位用
		};
		return map;
	};
	return oTableInit;
};

// 按钮对应的点击事件
function detailById(ids,action){
	tourl = $("#detail_formSearch #urlPrefix").val();
	if(action =='qg'){
		// 按钮是否可点击
		document.getElementById("btn_qg").disabled = true;
		document.getElementById("btn_ht").disabled = false;
		document.getElementById("btn_dh").disabled = true;
		document.getElementById("btn_jy").disabled = true;
		document.getElementById("btn_rk").disabled = true;
		// 按钮颜色
		document.getElementById("btn_qg").style="background-color:green";
		document.getElementById("btn_ht").style=null;
		document.getElementById("btn_dh").style=null;
		document.getElementById("btn_jy").style=null;
		document.getElementById("btn_rk").style=null;
		// 请求地址
		url= tourl + "/commonViewPage/commonViewPage/viewCommonPurchaseList?ids=" + ids;
		// 显示字段
		columnsArray = [
			{
			checkbox: true,
			width: '2%'
			},{
                field: 'qgmxid',
                title: '请购明细id',
                width: '5%',
                align: 'left',
                visible: false
            },{				
        		field: 'htmxid',
        		title: '合同明细id',
        		width: '12%',
        		align: 'left',
        		sortable: true,
        		visible: false
        	},{
                field: 'wlbm',
                title: '物料编码',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: false
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'wllbmc',
                title: '物料类别',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gys',
                title: '生产厂家',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlzlbmc',
                title: '物料子类别',
                width: '5%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'wlzlbtcmc',
                title: '子类别统称',
                width: '5%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'gg',
                title: '规格',
                width: '10%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'ychh',
                title: '货号',
                width: '6%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '3%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                width: '2%',
                align: 'left',
                sortable: true, 
                visible: true
            },{               
            	field: 'qwrq',
                title: '期望到货',
                width: '6%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'hwyt',
                title: '货物用途',
                width: '6%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'hwbz',
                title: '服务期限',
                width: '6%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'yq',
                title: '服务要求',
                width: '6%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '6%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'qxqg',
                title: '请购',
                width: '4%',
                formatter:purchase_qxqgformat,
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'zt',
                title: '审核状态',
                width: '4%',
                formatter:purchase_shztformat,
                align: 'left',
                sortable: true, 
                visible: true
            }];
		// 排序字段
		sortName = "qgid";
		// 每一行的唯一标识，一般为主键列
		uniqueId = "htmxid";
		// 防止同名排位用
		sortLastName = "qgmxid";	
	}else if(action =='ht'){
		// 按钮是否可点击
		document.getElementById("btn_qg").disabled = false;
		document.getElementById("btn_ht").disabled = true;
		document.getElementById("btn_dh").disabled = false;
		document.getElementById("btn_jy").disabled = true;
		document.getElementById("btn_rk").disabled = true;
		// 按钮颜色
		document.getElementById("btn_qg").style=null;
		document.getElementById("btn_ht").style="background-color:green";
		document.getElementById("btn_dh").style=null;
		document.getElementById("btn_jy").style=null;
		document.getElementById("btn_rk").style=null;
		// 请求地址
		url= tourl + "/commonViewPage/commonViewPage/viewCommonContractList?ids=" + ids;
		// 显示字段
		columnsArray = [
			{
			checkbox: true,
			width: '2%'
			}, {
		        field: 'htmxid',
		        title: '合同明细ID',
		        titleTooltip:'合同明细ID',
		        width: '8%',
		        align: 'left',
		        visible: false
		    },{
                field: 'qgmxid',
                title: '请购明细id',
                width: '5%',
                align: 'left',
                visible: false
            },{
                field: 'dhid',
                title: '到货id',
                width: '5%',
                align: 'left',
                visible: false
            },{
		        field: 'htid',
		        title: '合同ID',
		        titleTooltip:'合同ID',
		        width: '8%',
		        align: 'left',
		        visible: false
		    }, {
		        field: 'qgid',
		        title: '请购ID',
		        titleTooltip:'请购ID',
		        width: '8%',
		        align: 'left',
		        visible: false
		    }, {
		        field: 'wlid',
		        title: '物料ID',
		        titleTooltip:'物料ID',
		        width: '8%',
		        align: 'left',
		        visible: false
		    }, {
		        field: 'djh',
		        title: '请购单号',
		        titleTooltip:'请购单号',
		        width: '8%',
		        align: 'left',
		        visible: true,
		    }, {
		        field: 'wlbm',
		        title: '物料编码',
		        titleTooltip:'物料编码',
		        width: '8%',
		        align: 'left',
		        visible: true
		    }, {
		        field: 'wlmc',
		        title: '物料名称',
		        titleTooltip:'物料名称',
		        width: '8%',
		        align: 'left',
		        visible: true
		    },{
		        field: 'gg',
		        title: '规格',
		        titleTooltip:'规格',
		        width: '4%',
		        align: 'left',
		        visible: true
		    }, {
		        field: 'sl',
		        title: '数量',
		        titleTooltip:'数量',
		        width: '6%',
		        align: 'left',
		        visible: true
		    },{
		        field: 'hsdj',
		        title: '含税单价',
		        titleTooltip:'含税单价',
		        width: '8%',
		        align: 'left',
		        visible: true
		    },{
		        field: 'hjje',
		        title: '合计金额',
		        titleTooltip:'合计金额',
		        width: '5%',
		        align: 'left',
		        visible: true
		    },{
		        field: 'jhdhrq',
		        title: '计划到货日期',
		        titleTooltip:'计划到货日期',
		        width: '6%',
		        align: 'left',
		        visible: true
		    },{
		        field: 'bz',
		        title: '备注',
		        titleTooltip:'备注',
		        width: '8%',
		        align: 'left',
		        visible: true
		    }];
		// 排序字段
		sortName = "qgid";
		// 每一行的唯一标识，一般为主键列
		uniqueId = "htmxid";
		// 防止同名排位用
		sortLastName = "qgmxid";
	}else if(action =='dh'){
		// 按钮是否可点击
		document.getElementById("btn_qg").disabled = true;
		document.getElementById("btn_ht").disabled = false;
		document.getElementById("btn_dh").disabled = true;
		document.getElementById("btn_jy").disabled = false;
		document.getElementById("btn_rk").disabled = true;
		// 按钮颜色
		document.getElementById("btn_qg").style=null;
		document.getElementById("btn_ht").style=null;
		document.getElementById("btn_dh").style="background-color:green";
		document.getElementById("btn_jy").style=null;
		document.getElementById("btn_rk").style=null;
		// 请求地址
		url= tourl + "/commonViewPage/commonViewPage/viewCommonArrivalGoodsList?ids=" + ids;
		// 显示字段
		columnsArray = [
			{
			checkbox: true,
			width: '2%'
			}, {
                field: 'dhid',
                title: '到货id',
                width: '5%',
                align: 'left',
                visible: false
            },{
		        field: 'htmxid',
		        title: '合同明细ID',
		        titleTooltip:'合同明细ID',
		        width: '8%',
		        align: 'left',
		        visible: false
		    },{				
				field: 'dhjyid',
				title: '到货检验id',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'dhdh',
				title: '到货单号',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'gysmc',
				title: '供应商名称',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhrq',
				title: '到货日期',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: true
			},{				
				field: 'wcbj',
				title: '是否完成',
				width: '4%',
				align: 'center',
				formatter:wcbjformat,
				visible: true
			}, {				
				field: 'zt',
				title: '审核状态',
				width: '4%',
				align: 'left',
				formatter:arrivalGoods_shztformat,
				visible: true
			}, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '5%',
				align: 'center',
				formatter:arrivalGoods_czFormat,
				visible: true
			}];
		// 排序字段
		sortName = "dhid";
		// 每一行的唯一标识，一般为主键列
		uniqueId = "hwid";
		// 防止同名排位用
		sortLastName = "wlbm";
	}else if(action=="jy"){
		// 按钮是否可点击
		document.getElementById("btn_qg").disabled = true;
		document.getElementById("btn_ht").disabled = true;
		document.getElementById("btn_dh").disabled = false;
		document.getElementById("btn_jy").disabled = true;
		document.getElementById("btn_rk").disabled = false;
		// 按钮颜色
		document.getElementById("btn_qg").style=null;
		document.getElementById("btn_ht").style=null;
		document.getElementById("btn_dh").style=null;
		document.getElementById("btn_jy").style="background-color:green";
		document.getElementById("btn_rk").style=null;
		// 请求地址
		url=tourl + "/commonViewPage/commonViewPage/viewCommonInspectionGoodsList?ids=" + ids;
		// 显示字段
		columnsArray = [
			{
			checkbox: true,
			width: '2%'
			},{
                field: 'dhid',
                title: '到货id',
                width: '5%',
                align: 'left',
                visible: false
            },{
                field: 'rkid',
                title: '入库id',
                width: '5%',
                align: 'left',
                visible: false
            },{				
				field: 'jydh',
				title: '检验单号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'jyrq',
				title: '检验日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'jyfzrmc',
				title: '负责人',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
		    	field: 'htnbbh',
		    	title: '合同编号',
		    	width:'8%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'wlbm',
		    	title: '物料编码',
		    	width:'8%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'wlmc',
		    	title: '物料名称',
		    	width:'10%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'gg',
		    	title: '物料规格',
		    	width:'7%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'jldw',
		    	title: '计量单位',
		    	width:'7%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'ychh',
		    	title: '货号',
		    	width:'7%',
		    	align:'left',
		    	visible:false
		    },{
		    	field: 'sl',
		    	title: '数量',
		    	width:'6%',
		    	align:'left',
		    	visible:true
		    },/*{
		    	field: 'bgdh',
		    	title: '报告单号',
		    	width:'7%',
		    	align:'left',
		    	visible:false
		    },*/{				
				field: 'dhjyid',
				title: '到货检验id',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{
		    	field: 'qyl',
		    	title: '取样量',
		    	titleTooltip:'取样量',
		    	width:'7%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'qyrq',
		    	title: '取样日期',
		    	width:'10%',
		    	align:'left',
		    	visible:true
		    },{
		    	field: 'zjthsl',
		    	title: '退回数量',
		    	width:'7%',
		    	align:'left',
		    	visible:true
		    },{
		        field: 'fj',
		        title: '附件',
		        width: '3%',
		        align: 'left',
		        formatter:inspection_fjformat,
		        visible: true
		    },{
		    	field: 'hwid',
		    	title: '货物id',
		    	width:'7%',
		    	align:'left',
		    	visible:false
		    }, {				
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: false
			},{				
				field: 'wcbj',
				title: '是否完成',
				width: '4%',
				align: 'left',
				formatter:wcbjformat,
				visible: false
			}, {				
				field: 'zt',
				title: '审核状态',
				width: '5%',
				align: 'left',
				formatter:inspection_shztformat,
				visible: true
			},{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
				align: 'left',
				formatter:inspection_czFormat,
				visible: true
			}];
		// 排序字段
		sortName = "dhdh";
		// 每一行的唯一标识，一般为主键列
		uniqueId = "dhjyid";
		// 防止同名排位用
		sortLastName = "dhjyid";
	} else if(action=="rk"){
		// 按钮是否可点击
		document.getElementById("btn_qg").disabled = true;
		document.getElementById("btn_ht").disabled = true;
		document.getElementById("btn_dh").disabled = true;
		document.getElementById("btn_jy").disabled = false;
		document.getElementById("btn_rk").disabled = true;
		// 按钮颜色
		document.getElementById("btn_qg").style=null;
		document.getElementById("btn_ht").style=null;
		document.getElementById("btn_dh").style=null;
		document.getElementById("btn_jy").style=null;
		document.getElementById("btn_rk").style="background-color:green";
		// 请求地址
		url= tourl + "/commonViewPage/commonViewPage/viewCommonputInStorageList?ids=" + ids;
		// 显示字段
		columnsArray = [
			{
			checkbox: true,
			width: '2%'
			}, {				
				field: 'dhjyid',
				title: '到货检验id',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{
                field: 'rkid',
                title: '入库id',
                width: '5%',
                align: 'left',
                visible: false
            },{
                field: 'rkdh',
                title: '入库单号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'rkrq',
                title: '入库日期',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'ckmc',
                title: '仓库名称',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'rklbmc',
                title: '入库类别',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '18%',
                align: 'left',
                visible: true
            },{
                field: 'scbj',
                title: '状态',
                width: '4%',
                align: 'left',
                formatter:putInStorage_ztformat,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'zt',
                title: '审核状态',
                width: '10%',
                align: 'left',
                formatter:putInStorage_shztformat,
                visible: true
            }];
		// 排序字段
		sortName = "rkgl.rkrq";
		// 每一行的唯一标识，一般为主键列
		uniqueId = "rkid";
		// 防止同名排位用
		sortLastName = "rkgl.rkdh";
	}
	/**
	 * 	得到查询的参数
	 *	请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	 *	例如 toolbar 中的参数
	 *	如果 queryParamsType = ‘limit’ ,返回参数必须包含
	 *	limit, offset, search, sort, order
	 *	否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	 *	返回false将会终止请求
	 */
	var listParams = function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
				pageSize: params.limit,   // 页面大小
				access_token:$("#ac_tk").val(),
				sortName: params.sort,      // 排序列名
				sortOrder: params.order, // 排位命令（desc，asc）
				sortLastName: sortLastName, // 防止同名排位用
				sortLastOrder: "asc",// 防止同名排位用
		};
		return map;
	};
	$('#detail_formSearch #detail_list').bootstrapTable('destroy');
		$('#detail_formSearch #detail_list').bootstrapTable({
			url: url,											// 请求后台的URL（*）
			method: 'get',										// 请求方式（*）
			toolbar: '#detail_formSearch #toolbar',				// 工具按钮用哪个容器
			striped: true,										// 是否显示行间隔色
			cache: false,										// 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable: true,										// 是否启用排序
			sortName: sortName,									// 排序字段
			sortOrder: "desc",									// 排序方式
			queryParams: listParams,							// 传递参数（*）
			sidePagination: "server",							// 分页方式：client客户端分页，server服务端分页（*）
			minimumCountColumns: 2,								// 最少允许的列数
            search: false,                      				// 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            isForceTable: true,
			strictSearch: true,
			showColumns: true,                 				 	// 是否显示所有的列
            showRefresh: true,                  				// 是否显示刷新按钮
			clickToSelect: true,								// 是否启用点击选中行
			uniqueId: uniqueId,									// 每一行的唯一标识，一般为主键列
            showToggle:true,                    				// 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    				// 是否显示详细视图
            detailView: false,                  				// 是否显示父子表
			
			columns: columnsArray,
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
		});
		// 调整列宽
		$("#detail_formSearch #detail_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		});
}
// 格式化函数

// 取消请购 格式化
function purchase_qxqgformat(value,row,index){
	var html="";
		if(row.qxqg=='0'){
			html="<span style='color:green;'>正常</span>";
		}else{
			html="<span style='color:red;'>取消</span>";
	}
	return html;	
}
//审核状态 格式化
function purchase_shztformat(value,row,index){
	var html="";
		if(row.zt=='00'||row.zt=='15'){
			html="<span style='color:red;'>拒绝</span>";
		}else{			
			html="<span style='color:green;'>正常</span>";
	}
	return html;	
}
//完成标记 格式化
function wcbjformat(value,row,index){
	   if (row.wcbj == '0') {
	        return '否';
	    }else{
	    	return '是';
	    }
}
//操作格式化
 function arrivalGoods_czFormat(value,row,index){
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallarrivalGoods('" + row.dhid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"recallarrivalGoods('" + row.dhid + "',event)\" >撤回</span>";
	}else{
		return "";
	}	
}
//状态 格式化
function arrivalGoods_shztformat(value,row,index){
	   if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhid + "\",event,\"AUDIT_GOODS_ARRIVAL\",{prefix:\"" + $('#arrivalGoods_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhid + "\",event,\"AUDIT_GOODS_ARRIVAL\",{prefix:\"" + $('#arrivalGoods_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhid + "\",event,\"AUDIT_GOODS_ARRIVAL\",{prefix:\"" + $('#arrivalGoods_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}
//状态格式化
function inspection_shztformat(value,row,index){
		var auditType="";
	  	if(row.lbcskz1=="1"){
	  		auditType="AUDIT_GOODS_INSTRUMENT";
	  	}else{
	  		auditType="AUDIT_GOODS_REAGENT";
	  	}
	  	if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhjyid + "\",event,\""+auditType+"\",{prefix:\"" + $('#inspectionGoods_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhjyid + "\",event,\""+auditType+"\",{prefix:\"" + $('#inspectionGoods_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.dhjyid + "\",event,\""+auditType+"\",{prefix:\"" + $('#inspectionGoods_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}

//撤回操作 格式化
function inspection_czFormat(value,row,index) {
	if(row.zt=='10'){
		return "<div id='dhjyid"+row.dhjyid+"'><span id='t"+row.dhjyid
		+"' class='btn btn-danger' title='撤回审核' onclick=\"inspectionBack('" + row.dhjyid + "','"+row.lbcskz1+"',event)\" >撤回</span></div>";
	}
}

//附件格式化
function inspection_fjformat(value,row,index){
	var html = "<div id='fj_"+index+"'>"; 
	html += "<input type='hidden' id='hwfj_"+index+"' name='hwfj_"+index+"'/>";
	if(row.fjbj == "0"){
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\",\""+row.hwid+"\")' >是</a>";
	}else{
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\",\""+row.hwid+"\")' >否</a>";
	}	
	html += "</div>";
	return html;
}
//状态 格式化
function putInStorage_ztformat(value,row,index){
	var html="";
	if(row.scbj=='0'){
		html="<span style='color:green;'>正常</span>";
	}else{
		html="<span style='color:red;'>废弃</span>";
	}
	return html;
}
//审核状态 格式化
function putInStorage_shztformat(value,row,index){
	   if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.rkid + "\",event,\"AUDIT_GOODS_STORAGE\",{prefix:\"" + $('#putInStorage_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.rkid + "\",event,\"AUDIT_GOODS_STORAGE\",{prefix:\"" + $('#putInStorage_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.rkid + "\",event,\"AUDIT_GOODS_STORAGE\",{prefix:\"" + $('#putInStorage_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}

// 列表刷新
function detailResult(isTurnBack){
	if(isTurnBack){
		$('#detail_formSearch #detail_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#detail_formSearch #detail_list').bootstrapTable('refresh');
	}
}

$(document).ready(function(){
	// 1.初始化列表
	var oTable = new detail_TableInit();
	oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new detail_ButtonInit();
    oButtonInit.Init(); 
	// 所有下拉框添加choose样式
	jQuery('#detail_formSearch .chosen-select').chosen({width: '100%'});
});