var contract_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#contract_audit_formSearch #contract_audit_list").bootstrapTable({
			url: $('#contract_formAudit #urlPrefix').val()+'/contract/contract/pageGetListAuditContract',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#contract_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "shgc.sqsj",				// 排序字段
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
            uniqueId: "htid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '4%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#contract_audit_formSearch #contract_audit_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			},{				
				field: 'htnbbh',
				title: '合同内部编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'htwbbh',
				title: '合同外部编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			}, {				
				field: 'htlx',
				title: '采购类型',
				width: '6%',
				align: 'left',
				sortable: true,
				formatter:htlxformat,
				visible: false
			},{				
				field: 'ywlxmc',
				title: '合同类型',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'cglx',
				title: '采购类型',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			}, {				
				field: 'ddrq',
				title: '双章日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'ddrq',
				title: '订单日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'fkje',
				title: '付款金额',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'fzrmc',
				title: '负责人',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'sqbmmc',
				title: '申请部门',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'fpje',
				title: '发票金额',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			}, {				
				field: 'bizmc',
				title: '币种',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			}, {				
				field: 'sl',
				title: '税率',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'hl',
				title: '汇率',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'fkfsmc',
				title: '付款方式',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'fpfsmc',
				title: '发票方式',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'bz',
				title: '备注',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'gysmc',
				title: '供应商',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'zje',
				title: '总金额',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'yfje',
				title: '已付金额',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},  {				
				field: 'cjrq',
				title: '创建日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'htwfrq',
				title: '外发日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'tjrq',
				title: '提交日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'szbj',
				title: '双章标记',
				width: '8%',
				align: 'left',
				formatter:szbjformat,
				visible: false
			}, {
	            field: 'shxx_sqsj',
	            title: '申请时间',
	            width: '10%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        }, {
				field: 'shxx_shyj',
				title: '审核意见',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
	            field: 'lrrymc',
	            title: '录入人员',
	            width: '10%',
	            align: 'left',
	            visible: false
	        },{
	            field: 'lrsj',
	            title: '录入时间',
	            width: '10%',
	            align: 'left',
	            visible: false,
	            sortable: true   
	        }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            },
            onDblClickRow: function (row, $element) {
            	htgl_audit_DealById(row.htid,"view",$("#contract_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#contract_audit_formSearch #contract_audit_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "htgl.htid", // 防止同名排位用
            sortLastOrder: "asc",// 防止同名排位用
            zt:"10"
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#contract_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#contract_audit_formSearch #cxnr').val());
		if(cxtj=="1"){
			map["htnbbh"]=cxnr
		}else if (cxtj == "2") {
    		map["htwbbh"] = cxnr;
    	}else if (cxtj == "3") {
    		map["gys"] = cxnr;
    	}else if (cxtj == "5") {
    		map["sqbm"] = cxnr;
    	}
		map["dqshzt"] = 'dsh';
		return map;
	}
	return oTableInit;
}

var sbyzAudited_TableInit=function(){
	var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
    	$("#contract_audit_audited #tb_list").bootstrapTable({
			url: $('#contract_formAudit #urlPrefix').val()+'/contract/contract/pageGetListAuditContract',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#contract_audit_audited #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "shxx.shsj",				// 排序字段
            sortOrder: "desc",                  // 排序方式
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
            uniqueId: "shxx_shxxid",            // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                  // 是否显示父子表
            isForceTable:true,
            columns: [{
            	width: '1%',
                checkbox: true
            },{				
				field: 'htnbbh',
				title: '合同内部编号',
				width: '10%',
				align: 'left',
				visible: true
			}, {				
				field: 'htwbbh',
				title: '合同外部编号',
				width: '10%',
				align: 'left',
				visible: false
			},{				
				field: 'htlx',
				title: '采购类型',
				width: '6%',
				align: 'left',
				sortable: true,
				formatter:htlxformat,
				visible: true
			}, {				
				field: 'ywlxmc',
				title: '合同类型',
				width: '8%',
				align: 'left',
				visible: false
			},{				
				field: 'cglx',
				title: '采购类型',
				width: '8%',
				align: 'left',
				visible: false
			}, {				
				field: 'ddrq',
				title: '双章日期',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'ddrq',
				title: '订单日期',
				width: '8%',
				align: 'left',
				visible: false
			},{				
				field: 'fkje',
				title: '付款金额',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'fzr',
				title: '负责人',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'sqbmmc',
				title: '申请部门',
				width: '8%',
				align: 'left',
				visible: false
			},{				
				field: 'fpje',
				title: '发票金额',
				width: '8%',
				align: 'left',
				visible: false
			}, {				
				field: 'bizmc',
				title: '币种',
				width: '8%',
				align: 'left',
				visible: false
			}, {				
				field: 'sl',
				title: '税率',
				width: '8%',
				align: 'left',
				visible: false
			},{				
				field: 'hl',
				title: '汇率',
				width: '8%',
				align: 'left',
				visible: false
			},{				
				field: 'fkfsmc',
				title: '付款方式',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'fpfsmc',
				title: '发票方式',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'bz',
				title: '备注',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'gys',
				title: '供应商',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'zje',
				title: '总金额',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'yfje',
				title: '已付金额',
				width: '8%',
				align: 'left',
				visible: false
			},  {				
				field: 'cjrq',
				title: '创建日期',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'htwfrq',
				title: '外发日期',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'tjrq',
				title: '提交日期',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'szbj',
				title: '双章标记',
				width: '8%',
				align: 'left',
				formatter:szbjformat,
				visible: false
			}, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            },{
                field: 'shxx_shxxid',
                align: 'center',
                title: '审核信息ID',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            },
            onDblClickRow: function (row, $element) {
            	htgl_audit_DealById(row.htid,"view",$("#contract_audit_formSearch #btn_view").attr("tourl"));
             },
        });
    	$("#contract_audit_audited #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
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
            sortLastName: "htgl.htid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };

        
		
        var cxtj=$("#contract_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#contract_audit_audited #cxnr').val());
		if(cxtj=="1"){
			map["htnbbh"]=cxnr
		}else if (cxtj == "2") {
    		map["htwbbh"] = cxnr;
    	}else if (cxtj == "3") {
    		map["gys"] = cxnr;
    	}else if (cxtj == "5") {
    		map["sqbm"] = cxnr;
    	}
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
}

//双章标记格式化
function szbjformat(value,row,index){
	if(row.szbj==0){
		var szbj="<span style='color:green;'>"+"是"+"</span>";
	}else if(row.szbj==1){
		var szbj="<span style='color:red;'>"+"否"+"</span>";
	}
	return szbj;
}

//采购类型格式化
function htlxformat(value,row,index){
	if(row.htlx==1){
		return "OA采购";
	}else if (row.htlx==2){
        return "委外采购";
	} else{
		return "普通采购";
	}
}

function htgl_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#contract_formAudit #urlPrefix").val()+tourl;
	if(action=="view"){
		var url=tourl+"?htid="+id;
		$.showDialog(url,'查看信息',viewVerification_audit_Config);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_CONTRACT',
			url: tourl,
			data:{'ywzd':'htid'},
			title:"合同审核",
			preSubmitCheck:'preSubmitContract',
			prefix:$('#contract_formAudit #urlPrefix').val(),
			callback:function(){
				searchHtgl_audit_Result(true);// 回调
			},
			dialogParam:{width:1500}
		});
	}
}

function preSubmitContract(){
	return true;
}

var contract_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#contract_audit_formSearch #btn_query");// 模糊查询
		var btn_queryAudited = $("#contract_audit_audited #btn_query");// 审核记录列表模糊查询
    	var btn_cancelAudit = $("#contract_audit_audited #btn_cancelAudit");// 取消审核
    	var btn_view = $("#contract_audit_formSearch #btn_view");// 查看页面
    	var btn_audit = $("#contract_audit_formSearch #btn_audit");// 审核
    	
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchHtgl_audit_Result(true);
    		});
		}
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchHtglAudited(true);
    		});
		}
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#contract_audit_formSearch #contract_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				htgl_audit_DealById(sel_row[0].htid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#contract_audit_formSearch #contract_audit_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			htgl_audit_DealById(sel_row[0].htid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	// 选项卡切换事件回调
    	$('#contract_formAudit #contract_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){// 只调用一次
    			if(_hash=='#contract_auditing'){
    				var oTable= new contract_audit_TableInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new sbyzAudited_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{// 重新加载
    			// $(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');// 触发第一个选中事件
    	
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			var contract_params=[];
    			contract_params.prefix=$('#contract_formAudit #urlPrefix').val();
    			cancelAudit($('#contract_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchHtglAudited();
    			},null,contract_params);
    		})
    	}
	}
	return oInit;
}


var viewVerification_audit_Config = {
		width		: "1080px",
		modalName	:"viewVerificationConfig",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


function searchHtgl_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#contract_audit_formSearch #contract_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#contract_audit_formSearch #contract_audit_list').bootstrapTable('refresh');
	}
}

function searchHtglAudited(isTurnBack){
	if(isTurnBack){
		$('#contract_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#contract_audit_audited #tb_list').bootstrapTable('refresh');
	}
}


$(function(){
	var oTable= new contract_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new contract_audit_oButtton();
	oButtonInit.Init();
	
	// 所有下拉框添加choose样式
	jQuery('#contract_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#contract_audit_audited .chosen-select').chosen({width: '100%'});
})