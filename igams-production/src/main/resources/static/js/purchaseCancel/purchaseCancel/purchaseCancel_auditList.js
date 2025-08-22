var purchase_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#purchaseCancel_audit_formSearch #purchaseCancel_audit_list").bootstrapTable({
			url: $("#purchaseCancel_formAudit #urlPrefix").val()+'/purchaseCancel/purchaseCancel/pageGetListAuditPurchaseCancel',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchaseCancel_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qgqxgl.lrsj",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "qgqxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
	            field: 'djh',
	            title: '请购单号',
	            width: '20%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqrmc',
	            title: '申请人',
	            width: '13%',
	            align: 'left',
	            visible:true
	        },{
	            field: 'sqbmmc',
	            title: '申请部门',
	            width: '15%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqrq',
	            title: '申请日期',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqly',
	            title: '申请理由',
	            width: '15%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'bz',
	            title: '备注',
	            width: '15%',
	            align: 'left',
	            visible: true
	        },{
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
            	alert("数据加载失败！"); 
            },
            onDblClickRow: function (row, $element) {
            	qgqxgl_audit_DealById(row.qgqxid,"view",$("#purchaseCancel_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#purchaseCancel_audit_formSearch #purchaseCancel_audit_list").colResizable({
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
            sortLastName: "qgqxgl.qgqxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#purchaseCancel_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#purchaseCancel_audit_formSearch #cxnr').val());
		if(cxtj=="0"){
			map["djh"]=cxnr
		}
		map["dqshzt"] = 'dsh';
		return map;
	}
	return oTableInit;
}

var sbyzAudited_TableInit=function(){
	var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$("#purchaseCancel_audit_audited #tb_list").bootstrapTable({
			url: $("#purchaseCancel_formAudit #urlPrefix").val()+'/purchaseCancel/purchaseCancel/pageGetListAuditPurchaseCancel',          //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchaseCancel_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "desc",                  //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true,
                width: '4%'
            },{
	            field: 'djh',
	            title: '请购单号',
	            width: '6%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqrmc',
	            title: '申请人',
	            width: '3%',
	            align: 'left',
	            visible:true
	        },{
	            field: 'sqbmmc',
	            title: '申请部门',
	            width: '4%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqrq',
	            title: '申请日期',
	            width: '4%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqly',
	            title: '申请理由',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'bz',
	            title: '备注',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '7%',
                align: 'left',
                visible: true
            },{
				field: 'shxx_shyj',
				title: '审核意见',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '3%',
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
            onDblClickRow: function (row, $element) {
            	qgqxgl_audit_DealById(row.qgqxid,"view",$("#purchaseCancel_audit_formSearch #btn_view").attr("tourl"));
            },
        });
    	$("#purchaseCancel_audit_audited #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "qgqxgl.qgqxid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#purchaseCancel_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#purchaseCancel_audit_audited #cxnr').val());
		if(cxtj=="0"){
			map["djh"]=cxnr
		}
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
}

function qgqxgl_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#purchaseCancel_formAudit #urlPrefix").val()+tourl;
	if(action=="view"){
		var url=tourl+"?qgqxid="+id;
		$.showDialog(url,'查看信息',viewPurchaseCancel_audit_Config);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_REQUISITIONS',
			url:$("#purchaseCancel_formAudit #urlPrefix").val()+$("#purchaseCancel_audit_formSearch #btn_audit").attr("tourl"),
			data:{'ywzd':'qgqxid'},
			title:"取消请购审核",
			preSubmitCheck:'preSubmitPurchase',
			prefix:$("#purchaseCancel_formAudit #urlPrefix").val(),
			callback:function(){
				searchQggl_audit_Result(true);//回调
			},
			dialogParam:{width:1500}
		});
	}
}

function preSubmitPurchase(){
	return true;
}

var purchase_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#purchaseCancel_audit_formSearch #btn_query");//模糊查询
		var btn_queryAudited = $("#purchase_audit_audited #btn_query");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#purchase_audit_audited #btn_cancelAudit");//取消审核
    	var btn_view = $("#purchaseCancel_audit_formSearch #btn_view");//查看页面
    	var btn_audit = $("#purchaseCancel_audit_formSearch #btn_audit");//审核
    	
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchQggl_audit_Result(true);
    		});
		}
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchQgglAudited(true);
    		});
		}
		
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#purchaseCancel_audit_formSearch #purchaseCancel_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				qgqxgl_audit_DealById(sel_row[0].qgqxid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#purchaseCancel_audit_formSearch #purchaseCancel_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			qgqxgl_audit_DealById(sel_row[0].qgqxid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#purchaseCancel_formAudit #purchaseCancel_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='purchaseCancel_auditing'){
    				var oTable= new purchase_audit_TableInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new sbyzAudited_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		var purchaseCancel_params=[];
			purchaseCancel_params.prefix=$('#purchaseCancel_formAudit #urlPrefix').val();
    		btn_cancelAudit.click(function(){
    			cancelAudit($('#purchase_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchQgglAudited();
    			});
    		},null,purchaseCancel_params)
    	}
	}
	return oInit;
}

var viewPurchaseCancel_audit_Config = {
		width		: "1080px",
		modalName	:"viewPurchaseCancel",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : $("#purchaseCancel_formAudit #urlPrefix").val()+"/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
						if(("/"+data.currentCount) == $("#totalCount").text()){
							$("#totalCount").html($("#totalCount").text()+" 压缩中....")
						}
					}
					setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open($("#purchaseCancel_formAudit #urlPrefix").val()+"/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}

function searchQggl_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#purchaseCancel_audit_formSearch #purchaseCancel_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchaseCancel_audit_formSearch #purchaseCancel_audit_list').bootstrapTable('refresh');
	}
}

function searchQgglAudited(isTurnBack){
	if(isTurnBack){
		$('#purchase_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchase_audit_audited #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
	var oTable= new purchase_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new purchase_audit_oButtton();
	oButtonInit.Init();
	
	// 所有下拉框添加choose样式
	jQuery('#purchaseCancel_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#purchase_audit_audited .chosen-select').chosen({width: '100%'});
})