var arrivalGoods_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#arrivalGoods_audit_formSearch #arrivalGoods_audit_list").bootstrapTable({
			url: $('#arrivalGoods_formAudit #urlPrefix').val()+'/storehouse/arrivalGoods/pageGetListAudit',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#arrivalGoods_audit_formSearch #toolbar',                // 工具按钮用哪个容器
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
            uniqueId: "dhid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
				width: '2%',
                checkbox: true
            },{
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '4%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#arrivalGoods_audit_formSearch #arrivalGoods_audit_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			},{				
				field: 'dhdh',
				title: '到货单号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'gysmc',
				title: '供应商名称',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhrq',
				title: '到货日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: true
			}, {
	            field: 'shxx_sqsj',
	            title: '申请时间',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
				field: 'shxx_shyj',
				title: '审核意见',
				width: '15%',
				align: 'left',
				visible: true,
				sortable: true
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
            	alert("数据加载失败！"); 
            },
            onDblClickRow: function (row, $element) {
            	dhxx_audit_DealById(row.dhid,"view",$("#arrivalGoods_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#arrivalGoods_audit_formSearch #arrivalGoods_audit_list").colResizable({
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
            sortLastName: "dhxx.dhid", // 防止同名排位用
            sortLastOrder: "asc",// 防止同名排位用
            zt:"10"
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#arrivalGoods_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#arrivalGoods_audit_formSearch #cxnr').val());
		if(cxtj=="1"){
			map["dhdh"]=cxnr
		}else if (cxtj == "2") {
    		map["gysmc"] = cxnr;
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
    	$("#arrivalGoods_audit_audited #tb_list").bootstrapTable({
			url: $('#arrivalGoods_formAudit #urlPrefix').val()+'/storehouse/arrivalGoods/pageGetListAudit',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#arrivalGoods_audit_audited #toolbar',                // 工具按钮用哪个容器
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
            	width: '2%',
                checkbox: true
            },{				
				field: 'dhdh',
				title: '到货单号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'gysmc',
				title: '供应商名称',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'dhrq',
				title: '到货日期',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'bz',
				title: '备注',
				width: '20%',
				align: 'left',
				visible: true
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
            	alert("数据加载失败！");
            },
            /*onDblClickRow: function (row, $element) {
            	htgl_audit_DealById(row.htid,"view",$("#contract_audit_formSearch #btn_view").attr("tourl"));
             },*/
        });
    	$("#arrivalGoods_audit_audited #tb_list").colResizable({
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
            sortLastName: "dhxx.dhid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };

        
		
        var cxtj=$("#arrivalGoods_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#arrivalGoods_audit_audited #cxnr').val());
		if(cxtj=="1"){
			map["dhdh"]=cxnr
		}else if (cxtj == "2") {
    		map["gysmc"] = cxnr;
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

function dhxx_audit_DealById(id,action,tourl,rklxdm,clbj){
	if(!tourl){
		return;
	}
	tourl = $("#arrivalGoods_formAudit #urlPrefix").val()+tourl;
	if(action=="view"){
		var url=tourl+"?dhid="+id;
		$.showDialog(url,'查看信息',viewVerification_audit_Config);
	}else if(action=="audit"){
		let type = "AUDIT_GOODS_ARRIVAL";
		let preSubmitCheck = "preSubmitArrival";
		if (rklxdm == "cghz"&&clbj!='1'){
			tourl = "/inspectionGoods/pending/pagedataPurchasePendingMod";
			tourl = $("#arrivalGoods_formAudit #urlPrefix").val()+tourl;
			type = "AUDIT_GOODS_PENDING";
			preSubmitCheck = "pendingSubmitArrival";
		}
		if (rklxdm == "cghz"){
			type = "AUDIT_GOODS_PENDING";
		}
		showAuditDialogWithLoading({
			id: id,
			type: type,
			url: tourl,
			data:{'ywzd':'dhid'},
			title:"到货审核",
			preSubmitCheck: preSubmitCheck,
			prefix:$('#arrivalGoods_formAudit #urlPrefix').val(),
			callback:function(){
				searchDhxx_audit_Result(true);// 回调
			},
			dialogParam:{width:1500}
		});
	}
}

function pendingSubmitArrival(){
	var json = [];
	if(t_map.rows != null && t_map.rows.length > 0){
		for(var i=0;i<t_map.rows.length;i++){
			var sz = {"hwid":'',"chhwid":'',"dhsl":'',"bz":'',"wlbm":''};
			sz.hwid = t_map.rows[i].hwid;
			sz.chhwid = t_map.rows[i].chhwid;
			sz.dhsl = t_map.rows[i].hcsl;
			sz.bz = t_map.rows[i].bz;
			sz.wlbm = t_map.rows[i].wlbm;
			json.push(sz);
		}
		$("#pendingpurchaseForm #hwxx_json").val(JSON.stringify(json));
	}else{
		$.alert("明细信息不能为空！");
		return false;
	}
	return true;
}

function preSubmitArrival(){
	if(t_map.rows != null && t_map.rows.length > 0){
		// 初始化htmxJson
		var json = [];
		var hw_json = [];
		for (var i = 0; i < t_map.rows.length; i++) {
			var sz = {"yhwkcl":'',"yhwbhgsl":'',"ckid":'',"chhwid":'',"ckdh":'',"rkid":'',"hwid":'',"htmxid":'',"qgmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"cythsl":'',"htid":'',"htsl":'',"wlbm":''};
			sz.yhwkcl = t_map.rows[i].yhwkcl;
			sz.yhwbhgsl = t_map.rows[i].yhwbhgsl;
			sz.ckid = t_map.rows[i].ckid;
			sz.chhwid = t_map.rows[i].chhwid;
			sz.ckdh = t_map.rows[i].ckdh;
			sz.rkid = t_map.rows[i].rkid;
			sz.hwid = t_map.rows[i].hwid;
			sz.htmxid = t_map.rows[i].htmxid;
			sz.qgmxid = t_map.rows[i].qgmxid;
			sz.wlid = t_map.rows[i].wlid;
			sz.yxq = t_map.rows[i].yxq;
			sz.dhsl = t_map.rows[i].dhsl;
			sz.dhbz = t_map.rows[i].dhbz;
			sz.kwbh = t_map.rows[i].kwbh;
			sz.scrq = t_map.rows[i].scrq;
			sz.zsh = t_map.rows[i].zsh;
			sz.scph = t_map.rows[i].scph;
			sz.cskw = t_map.rows[i].cskw;						
			sz.cythsl = t_map.rows[i].cythsl;
			sz.htid = t_map.rows[i].htid;
			sz.htsl = t_map.rows[i].htsl;					
			sz.wlbm = t_map.rows[i].wlbm;
			json.push(sz);
			if (cskz3=='CGHZ'){
				$("#arrivalGoodsEditForm #ckid").val(sz.ckid);
			}
			var hw_sz = {"sczlmxid":'',"wlid":'',"yxq":'',"dhsl":'',"dhbz":'',"kwbh":'',"scrq":'',"zsh":'',"scph":'',"cskw":'',"wlbm":'',"sbysid":''};
			hw_sz.sczlmxid = t_map.rows[i].sczlmxid;
			hw_sz.wlid = t_map.rows[i].wlid;
			hw_sz.yxq = t_map.rows[i].yxq;
			hw_sz.dhsl = t_map.rows[i].dhsl;
			hw_sz.dhbz = t_map.rows[i].dhbz;
			hw_sz.kwbh = t_map.rows[i].kwbh;
			hw_sz.scrq = t_map.rows[i].scrq;
			hw_sz.zsh = t_map.rows[i].zsh;
			hw_sz.scph = t_map.rows[i].scph;
			hw_sz.cskw = t_map.rows[i].cskw;
			hw_sz.wlbm = t_map.rows[i].wlbm;
			hw_sz.sbysid = t_map.rows[i].sbysid;
			hw_json.push(hw_sz);
		}
		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(json));
		$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(hw_json));
		//去除无关json
		$("#arrivalGoodsEditForm #dhmx_json").val("");
	}
	return true;
}

var arrivalGoods_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#arrivalGoods_audit_formSearch #btn_query");// 模糊查询
		var btn_queryAudited = $("#arrivalGoods_audit_audited #btn_query");// 审核记录列表模糊查询
    	var btn_cancelAudit = $("#arrivalGoods_audit_audited #btn_cancelAudit");// 取消审核
    	var btn_view = $("#arrivalGoods_audit_formSearch #btn_view");// 查看页面
    	var btn_audit = $("#arrivalGoods_audit_formSearch #btn_audit");// 审核
    	
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchDhxx_audit_Result(true);
    		});
		}
		
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#arrivalGoods_audit_formSearch #arrivalGoods_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			dhxx_audit_DealById(sel_row[0].dhid,"audit",btn_audit.attr("tourl"), sel_row[0].rklxdm, sel_row[0].clbj);
    		}else{
    			$.error("请选中一行");
    		}
    	});
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchDhxxAudited(true);
    		});
		}
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#arrivalGoods_audit_formSearch #arrivalGoods_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				dhxx_audit_DealById(sel_row[0].dhid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		
    	/*---------------------------查看审核记录--------------------------------*/
    	// 选项卡切换事件回调
    	$('#arrivalGoods_formAudit #arrivalGoods_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){// 只调用一次
    			if(_hash=='arrivalGoods_auditing'){
    				var oTable= new arrivalGoods_audit_TableInit();
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
    			contract_params.prefix=$('#arrivalGoods_formAudit #urlPrefix').val();
    			cancelAudit($('#arrivalGoods_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchDhxxAudited();
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


function searchDhxx_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#arrivalGoods_audit_formSearch #arrivalGoods_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#arrivalGoods_audit_formSearch #arrivalGoods_audit_list').bootstrapTable('refresh');
	}
}

function searchDhxxAudited(isTurnBack){
	if(isTurnBack){
		$('#arrivalGoods_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#arrivalGoods_audit_audited #tb_list').bootstrapTable('refresh');
	}
}


$(function(){
	var oTable= new arrivalGoods_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new arrivalGoods_audit_oButtton();
	oButtonInit.Init();
	
	// 所有下拉框添加choose样式
	jQuery('#arrivalGoods_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#AdministrationAuditinged_formSearch .chosen-select').chosen({width: '100%'});
})